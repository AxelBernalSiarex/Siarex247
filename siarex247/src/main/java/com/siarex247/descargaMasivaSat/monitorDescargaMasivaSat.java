package com.siarex247.descargaMasivaSat;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.Date;

import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;

import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaSATBean;

import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;

import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;

import Models.CredencialesSAT;
import Models.EstadoComprobante;
import Models.SatServicioUrl;
import Models.SolicitaDescargaEmitidos;
import Models.SolicitaDescargaEmitidosResponse;
import Models.TipoComprobante;
import Models.TipoSolicitud;

import Models.SolicitaDescargaEmitidosParser;

import Models.VerificaSolicitudRequest;
import Models.VerificaSolicitudResponse;
import Models.VerificaSolicitudParser;

import Models.DescargaPaqueteRequest;
import Models.DescargaPaqueteResponse;
import Models.DescargaPaqueteParser;

import Models.PaqueteUtils;

import tokennativo.Autenticacion;
import tokennativo.Enveloped;
import tokennativo.SoapClient;

public class monitorDescargaMasivaSat {

    public static final Logger logger = Logger.getLogger("siarex247");

    // Ruta fija donde tienes cer/key
    private static final String CERTS_DIR = "C:\\Users\\AXELS\\OneDrive\\Escritorio\\nullmario\\CERTIFICADOS";
    private static final String METADATA_DIR = CERTS_DIR + "\\METADATA\\";
    private static final String LOG_TAG = "[SAT-MASIVA]";

    // --- helper interno para reutilizar credenciales/token ---
    private static class SatContext {
        X509Certificate cerX509;
        Path cerPath;
        Path keyPath;
        String password;
        String certBase64;
        CredencialesSAT credenciales;
    }

    // ====== Resultados parsing ======
    private static class ParseResult {
        int total = 0;
        int insertados = 0;
        int duplicados = 0;
        int errores = 0;
    }

    public void monitorDescargaMasivaSat(int diaProceso) {

        Connection con = null;
        ResultadoConexion rc = null;

        try {
            AccesoBean accesoBean = new AccesoBean();
            ConexionDB connPool = new ConexionDB();

            // ===== 1) conexión SOLO para listaEmpresas =====
            rc = connPool.getConnectionSiarex();
            con = rc.getCon();

            ArrayList<EmpresasForm> listaEmpresas =
                    accesoBean.listaEmpresas(con, rc.getEsquema());
            // OJO: listaEmpresas() CIERRA la conexión por dentro (por tu diseño)

            DescargaSATBean bean = new DescargaSATBean();

            for (EmpresasForm empresa : listaEmpresas) {

                if (!"A".equalsIgnoreCase(Utils.noNulo(empresa.getEstatus()))) {
                    continue;
                }

                logger.info("==============================================");
                logger.info(" MONITOREO DESCARGA MASIVA SAT ");
                logger.info(" Empresa: " + empresa.getEsquema());
                logger.info(" RFC: " + empresa.getRfc());
                logger.info("==============================================");

                // ===== 2) Consulta: Metadata HOY (trae N registros) =====
                ArrayList<HistoricoProcesoSATForm> historicoHoy =
                        obtenerHistoricoMetadataHoy(bean, empresa.getEsquema());

                logger.info("Metadata HOY total=" + (historicoHoy == null ? 0 : historicoHoy.size()));

                // ===== 3) INI -> ACCION 1 (Solicitar) =====
                ArrayList<HistoricoProcesoSATForm> historicoIni =
                        filtrarPorEstatus(historicoHoy, "INI");

                logger.info("Metadata HOY en INI total=" + historicoIni.size());

                for (HistoricoProcesoSATForm h : historicoIni) {

                    String fiStr = Utils.noNulo(h.getFechaInicio());
                    String ffStr = Utils.noNulo(h.getFechaFin());

                    logger.info("INI -> ID=" + h.getClaveHistorico()
                            + " FI=" + fiStr
                            + " FF=" + ffStr
                            + " SOL=" + h.getSolicitudSat()
                            + " PAQ=" + h.getPaqueteSat()
                            + " EST_DESC=" + h.getEstatusDescarga()
                            + " FECHA=" + h.getFechaDescarga());

                    SolicitaDescargaEmitidosResponse resp =
                            ejecutarSolicitudSATAccion1EmitidosMetadata(empresa, fiStr, ffStr);

                    // === Persistir resultado en HISTORICO_PROCESO_SAT ===
                    actualizarHistoricoAccion1(bean, empresa.getEsquema(), h.getClaveHistorico(), resp);

                    if (resp != null) {
                        logger.info("RESP SAT (ACCION=1) -> codigoEstatus=" + resp.getCodEstatus()
                                + " mensaje=" + resp.getMensaje()
                                + " idSolicitud=" + resp.getIdSolicitud());
                    } else {
                        logger.info("RESP SAT (ACCION=1) -> null");
                    }
                }

                // ===== 4) SOL -> ACCION 4/5 (Verificar + Descargar ZIP) =====
                ArrayList<HistoricoProcesoSATForm> historicoSol =
                        filtrarPorEstatus(historicoHoy, "SOL");

                logger.info("Metadata HOY en SOL total=" + historicoSol.size());

                for (HistoricoProcesoSATForm h : historicoSol) {
                    procesarSolAccion4y5(bean, empresa, h);
                }

                // ===== 5) MET -> ACCION 6 (Descomprimir ZIP + Leer TXT + Insertar tabla) =====
                ArrayList<HistoricoProcesoSATForm> historicoMet =
                        filtrarPorEstatus(historicoHoy, "MET");

                logger.info("Metadata HOY en MET total=" + historicoMet.size());

                for (HistoricoProcesoSATForm h : historicoMet) {
                    procesarMetAccion6Timbrado(bean, empresa, h);
                }

                // ===== 6) NOT -> ACCION 7 (Notificar correo + FIN) =====
                // (Opcional) refrescar lista para capturar los que se acaban de mover a NOT
                ArrayList<HistoricoProcesoSATForm> historicoHoy2 =
                        obtenerHistoricoMetadataHoy(bean, empresa.getEsquema());

                ArrayList<HistoricoProcesoSATForm> historicoNot =
                        filtrarPorEstatus(historicoHoy2, "NOT");

                logger.info("Metadata HOY en NOT total=" + historicoNot.size());

                for (HistoricoProcesoSATForm h : historicoNot) {
                    procesarNotAccion7Notifica(bean, empresa, h);
                }
            }

        } catch (Exception e) {
            Utils.imprimeLog("monitorDescargaMasivaSat", e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    // ============================================================
    // ========================= SOL FLOW =========================
    // ============================================================

    private void procesarSolAccion4y5(DescargaSATBean bean, EmpresasForm empresa, HistoricoProcesoSATForm h) {

        String esquema = Utils.noNulo(empresa.getEsquema());
        int idHist = h.getClaveHistorico();

        String idSolicitud = Utils.noNulo(h.getSolicitudSat()).trim();
        if (idSolicitud.isEmpty()) {
            logger.error(LOG_TAG + " [" + esquema + "][HIST=" + idHist + "][SOL] Inconsistente: sin SOLICITUD_SAT");
            actualizarHistoricoAccion5(bean, esquema, idHist, false, "", "ERR", "SOL sin SOLICITUD_SAT");
            return;
        }

        try {
            logStep(esquema, idHist, "SOL", "Entrando. idSolicitud=" + idSolicitud
                    + " PAQ_BD=" + Utils.noNulo(h.getPaqueteSat())
                    + " ACCION_BD=" + Utils.noNulo(h.getAccionSat())
                    + " EST_DESC_BD=" + Utils.noNulo(h.getEstatusDescarga()));

            // 1) Credenciales/token
            SatContext ctx = buildSatContext(empresa);
            if (ctx == null || ctx.credenciales == null) {
                logStep(esquema, idHist, "SOL", "No se pudieron preparar credenciales/token");
                actualizarHistoricoAccion5(bean, esquema, idHist, false, "", "ERR", "No se pudieron preparar credenciales/token");
                return;
            }

            String rfcSolicitante = Utils.noNulo(empresa.getRfc()).trim();
            if (rfcSolicitante.isEmpty()) {
                logStep(esquema, idHist, "SOL", "RFC solicitante vacío");
                actualizarHistoricoAccion5(bean, esquema, idHist, false, "", "ERR", "RFC solicitante vacío");
                return;
            }

            logStep(esquema, idHist, "VERIF", "Llamando VERIFICACION_URL con rfcSolicitante=" + rfcSolicitante);

            // 2) ACCION 4: verificar
            VerificaSolicitudRequest reqVer = new VerificaSolicitudRequest(idSolicitud, rfcSolicitante);
            VerificaSolicitudResponse respVer = verificarEstatusDescarga(reqVer, ctx.credenciales);

            if (respVer == null) {
                logStep(esquema, idHist, "VERIF", "respVer=null (SAT sin respuesta). Reintentar en siguiente corrida.");
                actualizarHistoricoAccion5(bean, esquema, idHist, false, "", "SOL", "Verificación SAT sin respuesta (respVer=null)");
                return;
            }

            // Log detallado de verificación
            int nPaquetes = (respVer.getIdsPaquetes() == null ? 0 : respVer.getIdsPaquetes().size());
            logStep(esquema, idHist, "VERIF", "Mensaje=" + safe(respVer.getMensaje(), 250));
            logStep(esquema, idHist, "VERIF", "PaquetesCount=" + nPaquetes
                    + " idsPaquetes=" + (respVer.getIdsPaquetes() == null ? "null" : respVer.getIdsPaquetes().toString()));

            if (nPaquetes == 0) {
                logStep(esquema, idHist, "VERIF", "Aún sin paquetes -> se queda SOL");
                actualizarHistoricoAccion5(bean, esquema, idHist, false, "", "SOL",
                        "Aún sin paquetes: " + Utils.noNulo(respVer.getMensaje()));
                return;
            }

            // 3) Descargar paquetes (ZIP)
            try { Files.createDirectories(Paths.get(METADATA_DIR)); } catch (Exception ignore) {}

            ArrayList<String> ids = new ArrayList<>(respVer.getIdsPaquetes());
            String paquetesCsv = String.join(",", ids);

            logStep(esquema, idHist, "DESC", "Iniciando descarga paquetes. METADATA_DIR=" + METADATA_DIR);
            logStep(esquema, idHist, "DESC", "PaquetesCSV=" + paquetesCsv);

            int descargados = 0;
            String primerZip = "";

            for (String idPaquete : ids) {
                idPaquete = Utils.noNulo(idPaquete).trim();
                if (idPaquete.isEmpty()) continue;

                logStep(esquema, idHist, "DESC", "Descargando idPaquete=" + idPaquete);

                DescargaPaqueteRequest reqPack = new DescargaPaqueteRequest(idPaquete, rfcSolicitante);
                DescargaPaqueteResponse respPack = descargarPaquete(reqPack, ctx.credenciales);

                if (respPack == null) {
                    logStep(esquema, idHist, "DESC", "respPack=null idPaquete=" + idPaquete);
                    continue;
                }

                boolean has = respPack.hasPaquete();
                logStep(esquema, idHist, "DESC", "respPack.hasPaquete=" + has + " idPaquete=" + idPaquete);

                if (has) {
                    String zipPath = METADATA_DIR + rfcSolicitante + "_" + idPaquete + ".zip";

                    File archivoZip = PaqueteUtils.guardarPaquete(respPack.getPaqueteBase64(), zipPath);

                    boolean existe = (archivoZip != null && archivoZip.exists());
                    long size = -1;
                    try { if (existe) size = archivoZip.length(); } catch (Exception ignore) {}

                    descargados++;
                    if (primerZip.isEmpty()) primerZip = (archivoZip == null ? zipPath : archivoZip.getAbsolutePath());

                    logStep(esquema, idHist, "DESC", "ZIP guardado path=" + primerZip + " existe=" + existe + " size=" + size);
                } else {
                    logStep(esquema, idHist, "DESC", "Paquete NO disponible aún idPaquete=" + idPaquete);
                }
            }

            if (descargados > 0) {
                logStep(esquema, idHist, "FIN", "OK descargados=" + descargados + " primerZip=" + primerZip + " -> MET");
                actualizarHistoricoAccion5(bean, esquema, idHist, true, paquetesCsv, "MET",
                        "ZIP_OK descargados=" + descargados + (primerZip.isEmpty() ? "" : (" primerZip=" + primerZip)));
            } else {
                logStep(esquema, idHist, "FIN", "SIN_DESCARGA: paquetes listados pero no descargables aún -> SOL");
                actualizarHistoricoAccion5(bean, esquema, idHist, false, paquetesCsv, "SOL",
                        "Paquetes listados pero aún no descargables. msg=" + Utils.noNulo(respVer.getMensaje()));
            }

        } catch (Exception e) {
            logger.error(LOG_TAG + " [" + esquema + "][HIST=" + idHist + "][SOL] ERROR", e);
            actualizarHistoricoAccion5(bean, esquema, idHist, false, "", "ERR", "ERROR_SOL: " + e.getMessage());
        }
    }

    private VerificaSolicitudResponse verificarEstatusDescarga(VerificaSolicitudRequest request, CredencialesSAT credenciales) throws Exception {
        Document doc = Enveloped.GeneraXMLVerificaDescarga(request);
        String xmlFirmado = Enveloped.FirmarXml(doc, credenciales);

        logger.info(LOG_TAG + " [SOAP][VERIF][REQ_XML] " + safe(xmlFirmado, 900));

        SoapClient client = new SoapClient(
                SatServicioUrl.VERIFICACION_URL.toString(),
                SatServicioUrl.VERIFICACION_SOAP_ACTION.toString()
        );
        String envelope = Enveloped.CrearSoapSolicitud(xmlFirmado);

        String response = client.send(envelope, credenciales.getTokenString());

        logger.info(LOG_TAG + " [SOAP][VERIF][RESP] " + safe(response, 900));

        return VerificaSolicitudParser.parse(response);
    }

    private DescargaPaqueteResponse descargarPaquete(DescargaPaqueteRequest request, CredencialesSAT credenciales) throws Exception {
        Document doc = Enveloped.GeneraXMLPaqueteDescarga(request);
        String xmlFirmado = Enveloped.FirmarXml(doc, credenciales);

        logger.info(LOG_TAG + " [SOAP][DESCARGA][REQ_XML] " + safe(xmlFirmado, 900));

        SoapClient client = new SoapClient(
                SatServicioUrl.DESCARGA_URL.toString(),
                SatServicioUrl.DESCARGAR_SOAP_ACTION.toString()
        );
        String envelope = Enveloped.CrearSoapSolicitud(xmlFirmado);

        String response = client.send(envelope, credenciales.getTokenString());

        logger.info(LOG_TAG + " [SOAP][DESCARGA][RESP] " + safe(response, 900));

        return DescargaPaqueteParser.parse(response);
    }

    // ============================================================
    // ========================= MET FLOW =========================
    // ============================================================

    private void procesarMetAccion6Timbrado(DescargaSATBean bean, EmpresasForm empresa, HistoricoProcesoSATForm h) {

        String esquema = Utils.noNulo(empresa.getEsquema());
        int idHist = h.getClaveHistorico();
        String rfcSolicitante = Utils.noNulo(empresa.getRfc()).trim();

        String paqCsv = Utils.noNulo(h.getPaqueteSat()).trim();
        if (paqCsv.isEmpty()) {
            logStep(esquema, idHist, "MET", "Sin PAQUETE_SAT (no puedo ubicar ZIP).");
            actualizarHistoricoAccion6(bean, esquema, idHist, Utils.noNulo(h.getPaqueteSat()), "ERR", "MET sin PAQUETE_SAT");
            return;
        }

        try {
            ArrayList<Path> zips = resolverZips(rfcSolicitante, paqCsv);
            if (zips.isEmpty()) {
                logStep(esquema, idHist, "MET", "No encontré ZIPs para PAQ=" + paqCsv + " en " + METADATA_DIR);
                actualizarHistoricoAccion6(bean, esquema, idHist, paqCsv, "ERR", "No se encontró ZIP en disco");
                return;
            }

            ConexionDB connPool = new ConexionDB();
            ResultadoConexion rc = null;
            Connection con = null;

            ParseResult totalRes = new ParseResult();

            try {
                rc = connPool.getConnectionSiarex();
                con = (rc == null ? null : rc.getCon());

                if (con == null) {
                    actualizarHistoricoAccion6(bean, esquema, idHist, paqCsv, "ERR", "Sin conexión BD");
                    return;
                }

                for (Path zip : zips) {

                    Path outDir = Paths.get(METADATA_DIR, "UNZIPPED", esquema, "HIST_" + idHist + "_" + System.currentTimeMillis());
                    ArrayList<Path> extraidos = unzip(zip, outDir);

                    ArrayList<Path> txts = new ArrayList<>();
                    for (Path p : extraidos) {
                        if (p != null && p.toString().toLowerCase().endsWith(".txt")) {
                            txts.add(p);
                        }
                    }

                    if (txts.isEmpty()) {
                        logStep(esquema, idHist, "MET", "ZIP sin .txt: " + zip.getFileName());
                        totalRes.errores++;
                        continue;
                    }

                    for (Path txt : txts) {
                        ParseResult r = leerEInsertarTxtTimbrado(con, bean, esquema, txt);
                        totalRes.total += r.total;
                        totalRes.insertados += r.insertados;
                        totalRes.duplicados += r.duplicados;
                        totalRes.errores += r.errores;
                    }
                }

                String msg = "CARGA_TIMBRADO total=" + totalRes.total
                        + " ins=" + totalRes.insertados
                        + " dup=" + totalRes.duplicados
                        + " err=" + totalRes.errores;

                logStep(esquema, idHist, "MET", msg);

                // MET -> NOT
                actualizarHistoricoAccion6(bean, esquema, idHist, paqCsv, "NOT", msg);

            } finally {
                try { if (con != null) con.close(); } catch (Exception ignore) {}
            }

        } catch (Exception e) {
            logger.error(LOG_TAG + " [" + esquema + "][HIST=" + idHist + "][MET] ERROR", e);
            actualizarHistoricoAccion6(bean, esquema, idHist, paqCsv, "ERR", "ERROR_MET: " + e.getMessage());
        }
    }

    private ArrayList<Path> resolverZips(String rfc, String paqCsv) {
        ArrayList<Path> out = new ArrayList<>();

        String[] tokens = Utils.noNulo(paqCsv).split(",");
        for (String t0 : tokens) {
            String idPaquete = Utils.noNulo(t0).trim();
            if (idPaquete.isEmpty()) continue;

            Path p1 = Paths.get(METADATA_DIR, rfc + "_" + idPaquete + ".zip");
            if (Files.exists(p1)) { out.add(p1); continue; }

            Path p2 = Paths.get(METADATA_DIR, idPaquete + ".zip");
            if (Files.exists(p2)) { out.add(p2); continue; }
        }

        ArrayList<Path> uniq = new ArrayList<>();
        for (Path p : out) if (!uniq.contains(p)) uniq.add(p);
        return uniq;
    }

    private ArrayList<Path> unzip(Path zipFile, Path outDir) throws Exception {
        ArrayList<Path> extraidos = new ArrayList<>();
        Files.createDirectories(outDir);

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                Path outPath = outDir.resolve(entry.getName()).normalize();

                // protección zip-slip
                if (!outPath.startsWith(outDir)) continue;

                if (entry.isDirectory()) {
                    Files.createDirectories(outPath);
                } else {
                    if (outPath.getParent() != null) Files.createDirectories(outPath.getParent());
                    Files.copy(zis, outPath, StandardCopyOption.REPLACE_EXISTING);
                    extraidos.add(outPath);
                }
            }
        }
        return extraidos;
    }

    private ParseResult leerEInsertarTxtTimbrado(Connection con, DescargaSATBean bean, String esquema, Path txtFile) {

        ParseResult res = new ParseResult();

        Charset[] charsets = new Charset[]{ StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1 };

        for (Charset cs : charsets) {
            try (BufferedReader br = Files.newBufferedReader(txtFile, cs)) {

                String line;
                boolean primera = true;

                while ((line = br.readLine()) != null) {

                    line = Utils.noNulo(line).trim();
                    if (line.isEmpty()) continue;

                    if (primera) {
                        primera = false;
                        if (line.startsWith("Uuid~") && line.toLowerCase().contains("rfcemisor")) {
                            continue;
                        }
                    }

                    String[] c = line.split("~", -1);
                    if (c.length < 11) continue;

                    String uuid = Utils.noNulo(c[0]).trim();
                    if (uuid.isEmpty()) continue;

                    res.total++;

                    int idExiste = bean.existeUuidMetadataTimbrado(con, esquema, uuid);
                    if (idExiste > 0) {
                        res.duplicados++;
                        continue;
                    }

                    String emisorRfc = Utils.noNulo(c[1]).trim();
                    String emisorNombre = Utils.noNulo(c[2]).trim();
                    String receptorRfc = Utils.noNulo(c[3]).trim();
                    String receptorNombre = Utils.noNulo(c[4]).trim();
                    String receptorPac = Utils.noNulo(c[5]).trim();

                    Timestamp fechaEmision = parseTimestampFlex(Utils.noNulo(c[6]).trim());
                    Date fechaCert = parseDateFromTimestampStr(Utils.noNulo(c[7]).trim());

                    double monto = 0.0;
                    try { monto = Double.parseDouble(Utils.noNulo(c[8]).trim()); } catch (Exception ignore) {}

                    String efecto = Utils.noNulo(c[9]).trim();
                    String estatus = "VIGENTE";
                    Timestamp fechaCancelacion = null;
                    if (c.length >= 12) {
                        fechaCancelacion = parseTimestampFlex(Utils.noNulo(c[11]).trim());
                    }

                    String tipoMoneda = "";

                    int rows = bean.guardarMetadataTimbrado(
                            con, esquema,
                            uuid,
                            emisorRfc, emisorNombre,
                            receptorRfc, receptorNombre,
                            receptorPac,
                            fechaEmision,
                            fechaCert,
                            monto,
                            efecto,
                            tipoMoneda,
                            estatus,
                            fechaCancelacion,
                            "N",
                            "SAT-MASIVA"
                    );

                    if (rows > 0) res.insertados++;
                }

                return res;

            } catch (Exception e) {
                // intenta con siguiente charset
            }
        }

        res.errores++;
        return res;
    }

    private Timestamp parseTimestampFlex(String s) {
        String v = Utils.noNulo(s).trim();
        if (v.isEmpty()) return null;

        v = v.replace("T", " ");
        if (v.length() == 10) v = v + " 00:00:00";
        if (v.length() > 19) v = v.substring(0, 19);

        try { return Timestamp.valueOf(v); } catch (Exception e) { return null; }
    }

    private Date parseDateFromTimestampStr(String s) {
        String v = Utils.noNulo(s).trim();
        if (v.isEmpty()) return null;

        if (v.length() >= 10) {
            String d = v.substring(0, 10);
            try { return Date.valueOf(d); } catch (Exception ignore) {}
        }

        Timestamp ts = parseTimestampFlex(v);
        return (ts == null ? null : new Date(ts.getTime()));
    }

    private void actualizarHistoricoAccion6(DescargaSATBean bean, String esquema, int claveHistorico,
                                           String paqueteSat, String estatus, String mensaje) {

        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        Connection con = null;

        try {
            rc = connPool.getConnectionSiarex();
            con = (rc == null ? null : rc.getCon());

            if (con == null) {
                logger.error("actualizarHistoricoAccion6 -> con == null (no se pudo abrir conexión)");
                return;
            }

            bean.actualizarHistoricoPaqueteSat(
                    con,
                    esquema,
                    claveHistorico,
                    "6",
                    Utils.noNulo(paqueteSat),
                    Utils.noNulo(estatus),
                    Utils.noNulo(mensaje)
            );

        } catch (Exception e) {
            logger.error("actualizarHistoricoAccion6 ERROR id=" + claveHistorico + " esquema=" + esquema, e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    // ============================================================
    // ========================= NOT FLOW =========================
    // ============================================================

    private void procesarNotAccion7Notifica(DescargaSATBean bean, EmpresasForm empresa, HistoricoProcesoSATForm h) {

        String esquema = Utils.noNulo(empresa.getEsquema());
        int idHist = h.getClaveHistorico();

        String fiStr = Utils.noNulo(h.getFechaInicio());
        String ffStr = Utils.noNulo(h.getFechaFin());

        Timestamp fi = parseTimestampFlex(fiStr);
        Timestamp ff = parseTimestampFlex(ffStr);

        // fallback básico si vienen nulas
        if (fi == null || ff == null) {
            LocalDateTime now = LocalDateTime.now();
            if (fi == null) fi = Timestamp.valueOf(now.minusDays(1));
            if (ff == null) ff = Timestamp.valueOf(now);
        }

        String paq = Utils.noNulo(h.getPaqueteSat());

        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        Connection con = null;

        try {
            rc = connPool.getConnectionSiarex();
            con = (rc == null ? null : rc.getCon());

            if (con == null) {
                logStep(esquema, idHist, "NOT", "Sin conexión BD");
                actualizarHistoricoAccion7(bean, esquema, idHist, paq, "ERR", "NOT sin conexión BD");
                return;
            }

            // Correos destino (mismos keys que usan en DescargaCFDIMetadataTimbrado)
            String c1 = Utils.noNulo(ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "CORREO_AVISO_UUID_BOVEDA_1")).trim();
            String c2 = Utils.noNulo(ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "CORREO_AVISO_UUID_BOVEDA_2")).trim();
            c1 = "Axelsbernal@hotmail.com";
            c2 = "axel.bernal@siarex.com";

            ArrayList<String> dest = new ArrayList<>();
            if (!c1.isEmpty()) dest.add(c1);
            if (!c2.isEmpty() && !c2.equalsIgnoreCase(c1)) dest.add(c2);

            if (dest.isEmpty()) {
                logStep(esquema, idHist, "NOT", "Sin correos configurados (CORREO_AVISO_UUID_BOVEDA_1/2). Paso a FIN.");
                actualizarHistoricoAccion7(bean, esquema, idHist, paq, "FIN", "SIN_CORREOS_DESTINO");
                return;
            }

            // Generar CSV (por rango y RFC emisor/receptor)
            String pathCSV = generarCSVNotificacion(con, bean, esquema, empresa, fi, ff);

            String mensajeCorreo = UtilsHTML.generaHTMLDescargaMasiva();
            String subject = "SIAREX - XML no encontrados en Boveda (SAT Masiva Metadata)";
            String[] to = dest.toArray(new String[0]);

            boolean adjuntar = (!Utils.noNulo(pathCSV).isEmpty() && new File(pathCSV).exists());

            logStep(esquema, idHist, "NOT", "Enviando correo. to=" + String.join(",", dest) + " adjunto=" + adjuntar);

            EnviaCorreoGrid.enviarCorreo(
                    adjuntar ? pathCSV : null,
                    mensajeCorreo,
                    adjuntar,
                    to,
                    null,
                    subject,
                    empresa.getEmailDominio(),
                    empresa.getPwdCorreo()
            );

            actualizarHistoricoAccion7(bean, esquema, idHist, paq, "FIN",
                    "NOTIFICACION_ENVIADA adjunto=" + (adjuntar ? "S" : "N") + " FI=" + fi + " FF=" + ff);

        } catch (Exception e) {
            logger.error(LOG_TAG + " [" + esquema + "][HIST=" + idHist + "][NOT] ERROR", e);
            actualizarHistoricoAccion7(bean, esquema, idHist, paq, "ERR", "ERROR_NOT: " + e.getMessage());
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    private String generarCSVNotificacion(Connection con, DescargaSATBean bean, String esquema,
                                         EmpresasForm empresa, Timestamp fi, Timestamp ff) {

        String pathCSV = "";
        try {
            // "N" = no encontrado en boveda (si tu Bean lo usa igual)
        	ArrayList<String> lines = bean.exportarCSVPorTransRango(con, esquema, fi, ff, Utils.noNulo(empresa.getRfc()), "N");

            if (lines == null || lines.size() <= 1) {
                return "";
            }

            String dir = UtilsPATH.RUTA_PUBLIC_HTML + esquema + File.separator + "EXPORTAR_METADATA" + File.separator;
            File fdir = new File(dir);
            if (!fdir.exists()) fdir.mkdirs();

            String stamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String nombre = "detalleMetadataBoveda_" + Utils.noNulo(empresa.getNombreCorto()) + "_" + stamp + ".csv";

            pathCSV = dir + nombre;

            UtilsFile.crearArchivoSalto(lines, pathCSV);

        } catch (Exception e) {
            Utils.imprimeLog("generarCSVNotificacion", e);
            pathCSV = "";
        }
        return pathCSV;
    }

    private void actualizarHistoricoAccion7(DescargaSATBean bean, String esquema, int claveHistorico,
                                           String paqueteSat, String estatus, String mensaje) {

        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        Connection con = null;

        try {
            rc = connPool.getConnectionSiarex();
            con = (rc == null ? null : rc.getCon());

            if (con == null) {
                logger.error("actualizarHistoricoAccion7 -> con == null (no se pudo abrir conexión)");
                return;
            }

            bean.actualizarHistoricoPaqueteSat(
                    con,
                    esquema,
                    claveHistorico,
                    "7",
                    Utils.noNulo(paqueteSat),
                    Utils.noNulo(estatus),
                    Utils.noNulo(mensaje)
            );

        } catch (Exception e) {
            logger.error("actualizarHistoricoAccion7 ERROR id=" + claveHistorico + " esquema=" + esquema, e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    // ============================================================
    // ========================== DB HELPERS ======================
    // ============================================================

    private ArrayList<HistoricoProcesoSATForm> obtenerHistoricoMetadataHoy(
            DescargaSATBean bean, String esquema) {

        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        Connection con = null;

        ArrayList<HistoricoProcesoSATForm> lista = new ArrayList<>();

        try {
            rc = connPool.getConnectionSiarex();
            con = rc.getCon();

            if (con == null) {
                logger.info("obtenerHistoricoMetadataHoy -> con == null (no se pudo abrir conexión)");
                return lista;
            }

            lista = bean.consultarHistoricoMetadataHoy(con, esquema);

            int total = (lista == null ? 0 : lista.size());
            logger.info("Histórico Metadata HOY - total registros: " + total);

            if (lista != null) {
                for (HistoricoProcesoSATForm h : lista) {
                    logger.info("ID=" + h.getClaveHistorico()
                            + " SOL=" + h.getSolicitudSat()
                            + " PAQ=" + h.getPaqueteSat()
                            + " EST_DESC=" + h.getEstatusDescarga()
                            + " EST=" + h.getEstatus()
                            + " FI=" + h.getFechaInicio()
                            + " FF=" + h.getFechaFin());
                }
            }

        } catch (Exception e) {
            Utils.imprimeLog("obtenerHistoricoMetadataHoy", e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }

        return (lista == null ? new ArrayList<HistoricoProcesoSATForm>() : lista);
    }

    private ArrayList<HistoricoProcesoSATForm> filtrarPorEstatus(
            ArrayList<HistoricoProcesoSATForm> lista, String estatusBuscado) {

        ArrayList<HistoricoProcesoSATForm> out = new ArrayList<>();
        String est = Utils.noNulo(estatusBuscado).trim();

        if (lista == null || lista.isEmpty()) return out;

        for (HistoricoProcesoSATForm h : lista) {
            String estatusDescarga = Utils.noNulo(h.getEstatusDescarga()).trim();
            if (estatusDescarga.equalsIgnoreCase(est)) {
                out.add(h);
            }
        }
        return out;
    }

    // ============================================================
    // ====================== ACCION 1 (SOL) ======================
    // ============================================================

    private SolicitaDescargaEmitidosResponse ejecutarSolicitudSATAccion1EmitidosMetadata(
            EmpresasForm empresa, String fechaInicioStr, String fechaFinStr) {

        try {
            LocalDateTime fi = parseLocalDateTimeFlex(fechaInicioStr);
            LocalDateTime ff = parseLocalDateTimeFlex(fechaFinStr);

            if (fi == null || ff == null) {
                logger.info("ACCION=1 -> fechas inválidas FI=" + fechaInicioStr + " FF=" + fechaFinStr);
                return null;
            }

            if (fi.isAfter(ff)) {
                LocalDateTime tmp = fi;
                fi = ff;
                ff = tmp;
                logger.info("ACCION=1 -> SWAP fechas (FI>FF). Nuevo FI=" + fi + " FF=" + ff);
            }

            SatContext ctx = buildSatContext(empresa);
            if (ctx == null || ctx.credenciales == null) return null;

            SolicitaDescargaEmitidos solicitud = new SolicitaDescargaEmitidos();
            solicitud.setRfcEmisor(Utils.noNulo(empresa.getRfc()));
            solicitud.setTipoSolicitud(TipoSolicitud.METADATA);
            solicitud.setTipoComprobante(TipoComprobante.TODOS);
            solicitud.setEstadoComprobante(EstadoComprobante.TODOS);
            solicitud.setFechaInicial(fi);
            solicitud.setFechaFinal(ff);

            Document doc = Enveloped.GeneraXMLSolicitudDescargaEmitidos(solicitud);
            String xmlFirmado = Enveloped.FirmarXml(doc, ctx.credenciales);
            String envelope = Enveloped.CrearSoapSolicitud(xmlFirmado);

            SoapClient cliente = new SoapClient(
                    SatServicioUrl.SOLICITUD_URL.toString(),
                    SatServicioUrl.SOLICITUD_SOAP_ACTION_EMITIDOS.toString()
            );

            String response = cliente.send(envelope, ctx.credenciales.getTokenString());
            return SolicitaDescargaEmitidosParser.parse(response);

        } catch (Exception e) {
            logger.error("ejecutarSolicitudSATAccion1EmitidosMetadata() ERROR", e);
            return null;
        }
    }

    private void actualizarHistoricoAccion1(DescargaSATBean bean, String esquema, int claveHistorico,
            SolicitaDescargaEmitidosResponse resp) {

        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        Connection con = null;

        try {
            rc = connPool.getConnectionSiarex();
            con = (rc == null ? null : rc.getCon());

            if (con == null) {
                logger.error("actualizarHistoricoAccion1 -> con == null (no se pudo abrir conexión)");
                return;
            }

            boolean ok = (resp != null
                    && "5000".equals(Utils.noNulo(resp.getCodEstatus()).trim())
                    && !Utils.noNulo(resp.getIdSolicitud()).trim().isEmpty());

            if (ok) {
                bean.actualizarHistoricoSolicitudSat(
                        con,
                        esquema,
                        claveHistorico,
                        "1",
                        resp.getIdSolicitud(),
                        "SOL",
                        resp.getMensaje()
                );
            } else {
                String msg;
                if (resp == null) {
                    msg = "SIN_RESPUESTA_SAT";
                } else {
                    msg = "COD=" + Utils.noNulo(resp.getCodEstatus()) + " MSG=" + Utils.noNulo(resp.getMensaje());
                }
                bean.actualizarHistoricoErrorSat(con, esquema, claveHistorico, "ERR", msg);
            }

        } catch (Exception e) {
            logger.error("actualizarHistoricoAccion1 ERROR id=" + claveHistorico + " esquema=" + esquema, e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    private void actualizarHistoricoAccion5(DescargaSATBean bean, String esquema, int claveHistorico,
            boolean ok, String paqueteSat, String estatus, String mensaje) {

        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        Connection con = null;

        try {
            rc = connPool.getConnectionSiarex();
            con = (rc == null ? null : rc.getCon());

            if (con == null) {
                logger.error("actualizarHistoricoAccion5 -> con == null (no se pudo abrir conexión)");
                return;
            }

            String accion = ok ? "5" : "4";

            bean.actualizarHistoricoPaqueteSat(
                    con,
                    esquema,
                    claveHistorico,
                    accion,
                    Utils.noNulo(paqueteSat),
                    Utils.noNulo(estatus),
                    Utils.noNulo(mensaje)
            );

        } catch (Exception e) {
            logger.error("actualizarHistoricoAccion5 ERROR id=" + claveHistorico + " esquema=" + esquema, e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

    // ============================================================
    // ===================== CERT/TOKEN HELPERS ===================
    // ============================================================

    private SatContext buildSatContext(EmpresasForm empresa) {
        String esquema = Utils.noNulo(empresa.getEsquema());
        try {
            String password = Utils.noNulo(empresa.getPwdSat());
            password = "Nobody13";

            if (password.isEmpty()) {
                logger.error(LOG_TAG + " [" + esquema + "][CTX] Falta PWD SAT");
                return null;
            }

            Path baseDir = Paths.get(CERTS_DIR);
            Path cerPath = findFirstByExt(baseDir, ".cer");
            Path keyPath = findFirstByExt(baseDir, ".key");

            if (cerPath == null || keyPath == null) {
                logger.error(LOG_TAG + " [" + esquema + "][CTX] No encontré CER/KEY en: " + CERTS_DIR
                        + " cer=" + (cerPath == null ? "null" : cerPath.toString())
                        + " key=" + (keyPath == null ? "null" : keyPath.toString()));
                return null;
            }

            logger.info(LOG_TAG + " [" + esquema + "][CTX] CER=" + cerPath);
            logger.info(LOG_TAG + " [" + esquema + "][CTX] KEY=" + keyPath);

            X509Certificate cerX509 = loadX509(cerPath);
            String serial = (cerX509 == null ? "" : Utils.noNulo(cerX509.getSerialNumber().toString(16)));
            logger.info(LOG_TAG + " [" + esquema + "][CTX] CertSerialHex=" + serial);

            byte[] certBytes = Files.readAllBytes(cerPath);
            String certBase64 = Base64.getEncoder().encodeToString(certBytes);
            logger.info(LOG_TAG + " [" + esquema + "][CTX] CertBytes=" + certBytes.length + " CertB64Len=" + certBase64.length());

            CredencialesSAT credenciales = new CredencialesSAT(cerX509, keyPath.toString(), password, null);

            Autenticacion autenticacion = new Autenticacion();
            String token = autenticacion.ObtenerToken(certBase64, keyPath.toString(), password);
            credenciales.setTokenString(token);

            logger.info(LOG_TAG + " [" + esquema + "][CTX] Token=" + maskToken(token));

            SatContext ctx = new SatContext();
            ctx.cerX509 = cerX509;
            ctx.cerPath = cerPath;
            ctx.keyPath = keyPath;
            ctx.password = password;
            ctx.certBase64 = certBase64;
            ctx.credenciales = credenciales;

            return ctx;

        } catch (Exception e) {
            logger.error(LOG_TAG + " [" + esquema + "][CTX] buildSatContext ERROR", e);
            return null;
        }
    }

    // ============================================================
    // ========================== UTILS ===========================
    // ============================================================

    private LocalDateTime parseLocalDateTimeFlex(String s) {
        String v = Utils.noNulo(s).trim();
        if (v.isEmpty()) return null;

        try {
            if (v.contains("T")) {
                if (v.length() > 19) v = v.substring(0, 19);
                return LocalDateTime.parse(v);
            }
        } catch (Exception ignore) {}

        try {
            if (v.length() > 19) v = v.substring(0, 19);
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(v, f);
        } catch (Exception ignore) {}

        return null;
    }

    private Path findFirstByExt(Path dir, String extLower) {
        if (dir == null) return null;
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(dir)) {
            for (Path p : ds) {
                String name = p.getFileName().toString().toLowerCase();
                if (name.endsWith(extLower)) return p;
            }
        } catch (Exception ignore) {}
        return null;
    }

    private X509Certificate loadX509(Path cerPath) throws Exception {
        try (java.io.InputStream in = Files.newInputStream(cerPath)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(in);
        }
    }

    private String safe(String s, int max) {
        s = Utils.noNulo(s);
        if (s.length() <= max) return s;
        return s.substring(0, max) + "...(" + s.length() + ")";
    }

    private String maskToken(String token) {
        token = Utils.noNulo(token);
        if (token.length() <= 30) return token;
        return token.substring(0, 12) + "..." + token.substring(token.length() - 12) + " (len=" + token.length() + ")";
    }

    private void logStep(String esquema, int idHist, String step, String msg) {
        logger.info(LOG_TAG + " [" + esquema + "][HIST=" + idHist + "][" + step + "] " + msg);
    }
}
