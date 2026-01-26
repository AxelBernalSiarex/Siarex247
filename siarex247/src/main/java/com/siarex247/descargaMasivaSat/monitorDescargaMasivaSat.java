package com.siarex247.descargaMasivaSat;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaSATBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import Models.CredencialesSAT;
import Models.EstadoComprobante;
import Models.SatServicioUrl;
import Models.SolicitaDescargaEmitidos;
import Models.SolicitaDescargaEmitidosResponse;
import Models.TipoComprobante;
import Models.TipoSolicitud;

import tokennativo.Autenticacion;
import tokennativo.Enveloped;
import tokennativo.SoapClient;

import Models.SolicitaDescargaEmitidosParser;

import org.w3c.dom.Document;

public class monitorDescargaMasivaSat {

    public static final Logger logger = Logger.getLogger("siarex247");

    // Ruta fija donde tienes cer/key
    private static final String CERTS_DIR = "C:\\Users\\AXELS\\OneDrive\\Escritorio\\nullmario\\CERTIFICADOS";

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

                // ===== 2) Primera consulta: Metadata HOY (trae N registros) =====
                ArrayList<HistoricoProcesoSATForm> historicoHoy =
                        obtenerHistoricoMetadataHoy(bean, empresa.getEsquema());

                logger.info("Metadata HOY total=" + (historicoHoy == null ? 0 : historicoHoy.size()));

                // ===== 3) Comparar uno por uno: solo los que estén en INI =====
                ArrayList<HistoricoProcesoSATForm> historicoIni =
                        filtrarPorEstatus(historicoHoy, "INI");

                logger.info("Metadata HOY en INI total=" + historicoIni.size());

                // ===== 4) Branch INI: obtener FECHA_INICIO/FECHA_FIN y ejecutar SAT (ACCION=1) =====
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

                // AHORITA HASTA AHI: solo probar el JAR/flujo.
            }

        } catch (Exception e) {
            Utils.imprimeLog("monitorDescargaMasivaSat", e);
        } finally {
            try { if (con != null) con.close(); } catch (Exception ignore) {}
        }
    }

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
            String estatusDescarga = Utils.noNulo(h.getEstatusDescarga()).trim(); // CHAR(3)
            if (estatusDescarga.equalsIgnoreCase(est)) {
                out.add(h);
            }
        }
        return out;
    }

    private SolicitaDescargaEmitidosResponse ejecutarSolicitudSATAccion1EmitidosMetadata(
            EmpresasForm empresa, String fechaInicioStr, String fechaFinStr) {

        try {
            LocalDateTime fi = parseLocalDateTimeFlex(fechaInicioStr);
            LocalDateTime ff = parseLocalDateTimeFlex(fechaFinStr);

            if (fi == null || ff == null) {
                logger.info("ACCION=1 -> fechas inválidas FI=" + fechaInicioStr + " FF=" + fechaFinStr);
                return null;
            }

            // si vienen volteadas en BD, swap
            if (fi.isAfter(ff)) {
                LocalDateTime tmp = fi;
                fi = ff;
                ff = tmp;
                logger.info("ACCION=1 -> SWAP fechas (FI>FF). Nuevo FI=" + fi + " FF=" + ff);
            }

            String password = Utils.noNulo(empresa.getPwdSat());
            password = "Nobody13";
            if (password.isEmpty()) {
                logger.info("ACCION=1 -> falta PWD SAT para empresa=" + empresa.getEsquema());
                return null;
            }

            // ======= localizar CER/KEY en carpeta fija =======
            Path baseDir = Paths.get(CERTS_DIR);
            Path cerPath = findFirstByExt(baseDir, ".cer");
            Path keyPath = findFirstByExt(baseDir, ".key");

            if (cerPath == null || keyPath == null) {
                logger.info("ACCION=1 -> no encontré CER o KEY en: " + CERTS_DIR
                        + " cer=" + (cerPath == null ? "null" : cerPath.toString())
                        + " key=" + (keyPath == null ? "null" : keyPath.toString()));
                return null;
            }

            logger.info("ACCION=1 -> CER=" + cerPath.toString());
            logger.info("ACCION=1 -> KEY=" + keyPath.toString());

            // ======= cargar X509 + base64 =======
            X509Certificate cerX509 = loadX509(cerPath);
            byte[] cerBytes = Files.readAllBytes(cerPath);
            String certBase64 = java.util.Base64.getEncoder().encodeToString(cerBytes);

            CredencialesSAT credenciales = new CredencialesSAT(cerX509, keyPath.toString(), password, null);

            // ======= token =======
            Autenticacion autenticacion = new Autenticacion();
            

            logger.info("PWD SAT len=" + (password == null ? 0 : password.trim().length()));
            logger.info("PWD SAT raw=[" + password + "]");

            String token = autenticacion.ObtenerToken(certBase64, keyPath.toString(), password);
            credenciales.setTokenString(token);

            logger.info("ACCION=1 -> token OK (len=" + (token == null ? 0 : token.length()) + ")");

            // ======= solicitud (Emitidos, METADATA) =======
            SolicitaDescargaEmitidos solicitud = new SolicitaDescargaEmitidos();
            solicitud.setRfcEmisor(Utils.noNulo(empresa.getRfc()));
            solicitud.setTipoSolicitud(TipoSolicitud.METADATA);
            solicitud.setTipoComprobante(TipoComprobante.TODOS);
            solicitud.setEstadoComprobante(EstadoComprobante.TODOS);
            solicitud.setFechaInicial(fi);
            solicitud.setFechaFinal(ff);

            Document doc = Enveloped.GeneraXMLSolicitudDescargaEmitidos(solicitud);
            String xmlFirmado = Enveloped.FirmarXml(doc, credenciales);
            String envelope = Enveloped.CrearSoapSolicitud(xmlFirmado);

            SoapClient cliente = new SoapClient(
                    SatServicioUrl.SOLICITUD_URL.toString(),
                    SatServicioUrl.SOLICITUD_SOAP_ACTION_EMITIDOS.toString()
            );

            String response = cliente.send(envelope, credenciales.getTokenString());
            return SolicitaDescargaEmitidosParser.parse(response);

        } catch (Exception e) {
            logger.error("ejecutarSolicitudSATAccion1EmitidosMetadata() ERROR", e);
            return null;
        }
    }

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

}
