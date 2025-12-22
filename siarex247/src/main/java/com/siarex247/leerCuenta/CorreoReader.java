package com.siarex247.leerCuenta;

import java.io.File;
import java.math.BigDecimal;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;

import org.apache.log4j.Logger;

import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsPATH;

public class CorreoReader {

    private static final Logger logger = Logger.getLogger("siarex247");

    // ================= CÓDIGOS DE ERROR =================
    private static final String E001_ORDEN_VACIA        = "E001"; // 1) Orden vacía
    private static final String E002_MONEDA_INVALIDA    = "E002"; // 2) Moneda inválida
    private static final String E003_EMPRESA_NO_EXISTE  = "E003"; // 3) Empresa (DESDE) no existe en EMPRESAS
    private static final String E004_PROV_NO_EXISTE     = "E004"; // 4) Proveedor (PARA) no existe en PROVEEDORES
    private static final String E005_IMPORTE_INVALIDO   = "E005"; // 5) Importe <= 0 / nulo
    private static final String E006_CLASIF_VACIA       = "E006"; // 6) Clasificación vacía
    private static final String E999_ERROR_GENERAL      = "E999"; // Cualquier otro error inesperado
    // ====================================================

    /**
     * Lee la cuenta de correo de una empresa y procesa adjuntos HTM
     */
    public void leerCuenta(EmpresasForm empresaSesion) {

        Store store = null;
        Folder inbox = null;

        try {

            String usuario = empresaSesion.getEmailDominio();
            String password = UtilsPATH.PASSWORD_DOMINIOS_SIAREX;
            String host = UtilsPATH.HOST_CORREO_PROCESO;

            // Correo configurado para omitir
            String correoNoDeseadoCfg = UtilsPATH.CORREO_NO_DESEADO;

            logger.info("Conectando a IMAPS " + host + " con usuario: " + usuario);

            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imaps.host", host);
            props.put("mail.imaps.port", "993");
            props.put("mail.imaps.ssl.enable", "true");
            props.put("mail.imaps.auth", "true");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usuario, password);
                }
            });

            store = session.getStore("imaps");
            store.connect(host, usuario, password);

            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // SOLO correos NO LEÍDOS
            FlagTerm noLeidos = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message[] mensajes = inbox.search(noLeidos);

            logger.info("Correos no leídos encontrados: " + mensajes.length);

            for (Message msg : mensajes) {

                try {

                    String correoDe = ((InternetAddress) msg.getFrom()[0]).getAddress();
                    logger.info("Correo entrante de: " + correoDe);

                    // CORREO NO DESEADO
                    if (esCorreoNoDeseado(correoDe, correoNoDeseadoCfg)) {
                        logger.info("Correo OMITIDO por configuración: " + correoDe);
                        msg.setFlag(Flags.Flag.SEEN, true);
                        continue;
                    }

                    procesarMensaje(msg, empresaSesion);

                    // ✅ SOLO si no truena, se marca leído
                    msg.setFlag(Flags.Flag.SEEN, true);
                    logger.info("Correo procesado y marcado como LEÍDO");

                } catch (ValidacionHtmException vex) {
                    // ❌ No se marca como leído (la bitácora ya se insertó dentro de procesarMensaje)
                    logger.error("❌ Validación HTM falló [" + vex.getCodigoError() + "]: " + vex.getMessage(), vex);
                } catch (Exception e) {
                    // ❌ No se marca como leído (si quieres también bitacorizar aquí, se puede, pero
                    // normalmente aquí ya se bitacorizó por attachment dentro de procesarMensaje)
                    logger.error("❌ Error procesando mensaje, NO se marca como leído", e);
                }
            }

        } catch (Exception e) {
            Utils.imprimeLog("leerCuenta", e);

        } finally {
            try {
                if (inbox != null && inbox.isOpen()) inbox.close(false);
                if (store != null) store.close();
            } catch (Exception e) {
                Utils.imprimeLog("cerrarCorreo", e);
            }
        }
    }

    /**
     * Procesa un mensaje y guarda adjuntos HTM
     * IMPORTANTE: Aquí se integra BITÁCORA si falla.
     */
    private void procesarMensaje(Message msg, EmpresasForm empresaSesion) throws Exception {

        if (msg.getContentType() == null || !msg.getContentType().toLowerCase().contains("multipart")) return;

        String correoDe = ((InternetAddress) msg.getFrom()[0]).getAddress();
        String asunto = msg.getSubject();

        logger.info("Procesando correo de: " + correoDe);
        logger.info("Asunto: " + asunto);

        Multipart mp = (Multipart) msg.getContent();

        for (int i = 0; i < mp.getCount(); i++) {

            BodyPart part = mp.getBodyPart(i);

            if (!Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) continue;

            String fileName = part.getFileName();
            if (fileName == null || !fileName.toLowerCase().endsWith(".htm")) continue;

            File carpeta = new File(
                UtilsPATH.RUTA_PUBLIC_HTML
                + File.separator + empresaSesion.getEsquema()
                + File.separator + "CORREO_FACTURAS"
            );

            if (!carpeta.exists()) carpeta.mkdirs();

            File archivo = new File(carpeta, fileName);
            ((MimeBodyPart) part).saveFile(archivo);

            logger.info("HTM guardado correctamente: " + archivo.getAbsolutePath());

            OrdenCompraHtmData data = null;

            try {
                // ================= PARSE HTM =================
                AribaHtmParser parser = new AribaHtmParser();
                data = parser.parse(archivo);

                // LOG DEPURACIÓN (Verificamos qué extrajo el parser)
                logger.info("--- DATOS EXTRAÍDOS DEL PARSER ---");
                logger.info("DESDE (Proveedor): " + data.getDesde());
                logger.info("PARA  (Destinatario/Empleado): " + data.getPara());
                logger.info("----------------------------------");

                // ================= VALIDACIONES (CON CÓDIGO) =================
                validarNumeroOrden(data);
                validarMoneda(data);
                validarImporte(data);
                validarClasificacion(data);

                // 3) Validar EMPRESA en EMPRESAS (DESDE)
                validarRazonSocialHTM(data);

                // 4) Validar PROVEEDOR en PROVEEDORES (PARA) en contrare_<esquema>
                validarProveedorPara(data, empresaSesion);

                // ================= LOG FINAL =================
                logDatos(data);

            } catch (ValidacionHtmException vex) {

                // --------- BITÁCORA (ERROR DE VALIDACIÓN) ----------
                String numOrden = obtenerNumOrdenSeguro(data, fileName);
                insertarBitacoraSeguro(
                    empresaSesion,
                    numOrden,
                    vex.getCodigoError(),
                    vex.getMessage(),
                    correoDe,
                    asunto,
                    archivo
                );
                // ---------------------------------------------------

                logger.error("❌ HTM inválido [" + vex.getCodigoError() + "] numOrden=" + numOrden
                        + " esquema=" + empresaSesion.getEsquema()
                        + " msg=" + vex.getMessage());

                // Propaga para que NO se marque como leído
                throw vex;

            } catch (Exception ex) {

                // --------- BITÁCORA (ERROR GENERAL) ----------
                String numOrden = obtenerNumOrdenSeguro(data, fileName);
                String desc = (ex.getMessage() != null && !ex.getMessage().trim().isEmpty())
                        ? ex.getMessage()
                        : "Error general procesando HTM";

                insertarBitacoraSeguro(
                    empresaSesion,
                    numOrden,
                    E999_ERROR_GENERAL,
                    desc,
                    correoDe,
                    asunto,
                    archivo
                );
                // --------------------------------------------

                logger.error("❌ Error general HTM [" + E999_ERROR_GENERAL + "] numOrden=" + numOrden
                        + " esquema=" + empresaSesion.getEsquema()
                        + " err=" + desc, ex);

                throw new ValidacionHtmException(E999_ERROR_GENERAL, "Error general procesando HTM: " + desc, ex);
            }
        }
    }

    // ================= VALIDACIONES =================

    private void validarNumeroOrden(OrdenCompraHtmData data) throws ValidacionHtmException {
        String orden = (data != null) ? data.getOrdenCompra() : null;

        if (orden == null || orden.trim().isEmpty()) {
            logger.error("❌ NÚMERO DE ORDEN VACÍO");
            throw new ValidacionHtmException(E001_ORDEN_VACIA, "Número de orden obligatorio");
        }

        logger.info("✔ Número de orden válido: " + orden);
    }

    private void validarMoneda(OrdenCompraHtmData data) throws ValidacionHtmException {
        String moneda = (data != null) ? data.getMoneda() : null;

        if (moneda == null || moneda.trim().isEmpty()) {
            logger.error("❌ MONEDA NULA/VACÍA");
            throw new ValidacionHtmException(E002_MONEDA_INVALIDA, "Moneda obligatoria (MXN o USD)");
        }

        if (!"MXN".equalsIgnoreCase(moneda) && !"USD".equalsIgnoreCase(moneda)) {
            logger.error("❌ MONEDA NO VÁLIDA: " + moneda);
            throw new ValidacionHtmException(E002_MONEDA_INVALIDA, "Moneda no permitida: " + moneda);
        }

        logger.info("✔ Moneda válida: " + moneda);
    }

    private void validarImporte(OrdenCompraHtmData data) throws ValidacionHtmException {
        BigDecimal monto = (data != null) ? data.getMonto() : null;

        if (monto == null) {
            logger.error("❌ IMPORTE NULO");
            throw new ValidacionHtmException(E005_IMPORTE_INVALIDO, "Importe obligatorio");
        }

        if (monto.doubleValue() <= 0) {
            logger.error("❌ IMPORTE INVÁLIDO: " + monto);
            throw new ValidacionHtmException(E005_IMPORTE_INVALIDO, "Importe debe ser mayor a 0");
        }

        logger.info("✔ Importe válido: " + monto);
    }

    private void validarClasificacion(OrdenCompraHtmData data) throws ValidacionHtmException {
        String clasificacion = (data != null) ? data.getClasificacionCodigo() : null;

        if (clasificacion == null || clasificacion.trim().isEmpty()) {
            logger.error("❌ CLASIFICACIÓN VACÍA");
            throw new ValidacionHtmException(E006_CLASIF_VACIA, "Clasificación obligatoria");
        }

        logger.info("✔ Clasificación válida: " + clasificacion);
    }

    /**
     * 3) Valida que la razón social (DESDE) del HTM exista en EMPRESAS.NOMBRE_LARGO
     */
    private void validarRazonSocialHTM(OrdenCompraHtmData data) throws ValidacionHtmException {

        String razonHtm = (data != null) ? data.getDesde() : null;

        if (razonHtm == null || razonHtm.trim().isEmpty()) {
            throw new ValidacionHtmException(E003_EMPRESA_NO_EXISTE, "Razón social (DESDE) vacía");
        }

        // normalización básica para EMPRESAS (tu query usa LOWER/TRIM, así que es seguro)
        String razonNormalizada = normalizarBasica(razonHtm);

        try {
            AccesoBean accesoBean = new AccesoBean();
            EmpresasForm empresaBd = accesoBean.consultaEmpresaPorNombreLargo(razonNormalizada);

            if (empresaBd == null) {
                logger.error("❌ RAZÓN SOCIAL NO REGISTRADA EN EMPRESAS: " + razonHtm);
                throw new ValidacionHtmException(E003_EMPRESA_NO_EXISTE, "Empresa emisora no registrada: " + razonHtm);
            }

            logger.info("✔ Empresa emisora válida: " + empresaBd.getNombreLargo());

        } catch (ValidacionHtmException vex) {
            throw vex;
        } catch (Exception e) {
            throw new ValidacionHtmException(E003_EMPRESA_NO_EXISTE, "Error validando EMPRESA (DESDE): " + e.getMessage(), e);
        }
    }

    /**
     * 4) Valida proveedor por RAZON SOCIAL del "PARA" en PROVEEDORES usando contrare_<esquema>.
     */
    private void validarProveedorPara(OrdenCompraHtmData data, EmpresasForm empresaSesion) throws ValidacionHtmException {

        String proveedorRaw = (data != null) ? data.getPara() : null;

        if (proveedorRaw == null || proveedorRaw.trim().isEmpty()) {
            throw new ValidacionHtmException(E004_PROV_NO_EXISTE, "Proveedor (PARA) vacío");
        }

        try {
            ProveedoresBean proveedoresBean = new ProveedoresBean();

            logger.info("Validando proveedor (PARA) EXACTO: [" + proveedorRaw + "] esquema=" + empresaSesion.getEsquema());

            // OJO: este método debe consultar en contrare_<esquema>
            boolean existe = proveedoresBean.existeProveedorPorRazonSocial(proveedorRaw, empresaSesion.getEsquema());

            if (!existe) {
                logger.error("❌ PROVEEDOR (PARA) NO REGISTRADO exacto: [" + proveedorRaw + "]");
                throw new ValidacionHtmException(E004_PROV_NO_EXISTE, "Proveedor no registrado: " + proveedorRaw);
            }

            logger.info("✔ Proveedor válido (PARA): " + proveedorRaw);

        } catch (ValidacionHtmException vex) {
            throw vex;
        } catch (Exception e) {
            throw new ValidacionHtmException(E004_PROV_NO_EXISTE, "Error validando PROVEEDOR (PARA): " + e.getMessage(), e);
        }
    }

    // ================= BITÁCORA =================

    /**
     * Inserta bitácora SIN romper el flujo (si falla bitácora, solo loggea).
     */
    private void insertarBitacoraSeguro(EmpresasForm empresaSesion,
                                       String numOrden,
                                       String codError,
                                       String descError,
                                       String emailOrigen,
                                       String asunto,
                                       File archivoHtm) {

        try {
            BitacoraOrdenCompraHtmForm form = new BitacoraOrdenCompraHtmForm();
            form.setNumOrden(numOrden);
            form.setCodError(codError);
            form.setDescError(descError);

            form.setEmailOrigen(emailOrigen);
            form.setAsunto(asunto);
            form.setArchivoHtm(archivoHtm != null ? archivoHtm.getAbsolutePath() : null);

            BitacoraOrdenCompraHtmBean bean = new BitacoraOrdenCompraHtmBean();
            boolean ok = bean.insertar(form, empresaSesion.getEsquema()); // "mario" -> contrare_mario

            logger.info("BITACORA_ORDEN_COMPRA_HTM insert ok=" + ok
                    + " esquema=" + empresaSesion.getEsquema()
                    + " numOrden=" + numOrden
                    + " cod=" + codError);

        } catch (Exception e) {
            // NO debe tumbar el proceso, solo dejamos evidencia
            logger.error("❌ No se pudo insertar bitácora HTM. esquema=" + empresaSesion.getEsquema()
                    + " numOrden=" + numOrden
                    + " cod=" + codError
                    + " desc=" + descError, e);
        }
    }

    private String obtenerNumOrdenSeguro(OrdenCompraHtmData data, String fallback) {
        try {
            if (data != null) {
                String o = data.getOrdenCompra();
                if (o != null && !o.trim().isEmpty()) return o.trim();
            }
        } catch (Exception ignore) {}
        return (fallback != null) ? fallback : "SIN_ORDEN";
    }

    // ================= HELPERS =================

    private String normalizarBasica(String texto) {
        return texto == null ? null :
            texto.toLowerCase()
                 .replace(".", "")
                 .replace(",", "")
                 .replaceAll("\\s+", " ")
                 .trim();
    }

    private boolean esCorreoNoDeseado(String correoDe, String config) {
        if (correoDe == null || config == null || config.trim().isEmpty()) return false;
        return correoDe.trim().equalsIgnoreCase(config.trim());
    }

    /**
     * Log de datos extraídos del HTM
     */
    private void logDatos(OrdenCompraHtmData data) {

        logger.info("========== DATOS ORDEN (HTM) ==========");
        logger.info("ORDEN: " + data.getOrdenCompra());
        logger.info("PROVEEDOR (DESDE): " + data.getDesde());
        logger.info("EMPLEADO (PARA): " + data.getPara());
        logger.info("EMPRESA (RAW): " + data.getEmpresa());
        logger.info("RFC/TAXID: " + data.getTaxId());
        logger.info("MONEDA: " + data.getMoneda());
        logger.info("MONTO: " + data.getMonto());
        logger.info("CLASIFICACIÓN: " + data.getClasificacionCodigo());
        logger.info("======================================");
    }
    
    
    
    
 // ================= USO EXTERNO (LeeCorreo 2012) =================

    /**
     * Procesa un archivo HTM YA GUARDADO en disco (lo llama LeeCorreo).
     * - Aplica parser + validaciones con código
     * - Inserta bitácora en caso de error
     * - Si falla, lanza ValidacionHtmException para que el caller NO marque el correo como leído.
     */
    public void procesarHtmArchivo(File archivoHtm,
                                  EmpresasForm empresaSesion,
                                  String emailOrigen,
                                  String asunto) throws ValidacionHtmException {

        if (archivoHtm == null || !archivoHtm.exists()) {
            throw new ValidacionHtmException(E999_ERROR_GENERAL, "Archivo HTM no existe: " + (archivoHtm != null ? archivoHtm.getAbsolutePath() : "null"));
        }

        OrdenCompraHtmData data = null;
        String fileName = archivoHtm.getName();

        try {
            // 1) PARSE
            AribaHtmParser parser = new AribaHtmParser();
            data = parser.parse(archivoHtm);

            logger.info("--- HTM (EXTERNO) DATOS EXTRAÍDOS ---");
            logger.info("DESDE (Proveedor): " + data.getDesde());
            logger.info("PARA  (Empleado):  " + data.getPara());
            logger.info("------------------------------------");

            // 2) VALIDACIONES (CON CÓDIGO)
            validarNumeroOrden(data);
            validarMoneda(data);
            validarImporte(data);
            validarClasificacion(data);

            // 3) EMPRESA (DESDE) exista en EMPRESAS
            validarRazonSocialHTM(data);

            // 4) PROVEEDOR (PARA) exista en PROVEEDORES (contrare_<esquema>)
            validarProveedorPara(data, empresaSesion);

            // OK
            logDatos(data);

        } catch (ValidacionHtmException vex) {

            String numOrden = obtenerNumOrdenSeguro(data, fileName);

            // BITÁCORA error validación
            insertarBitacoraSeguro(
                empresaSesion,
                numOrden,
                vex.getCodigoError(),
                vex.getMessage(),
                emailOrigen,
                asunto,
                archivoHtm
            );

            throw vex;

        } catch (Exception ex) {

            String numOrden = obtenerNumOrdenSeguro(data, fileName);
            String desc = (ex.getMessage() != null && !ex.getMessage().trim().isEmpty())
                    ? ex.getMessage()
                    : "Error general procesando HTM";

            // BITÁCORA error general
            insertarBitacoraSeguro(
                empresaSesion,
                numOrden,
                E999_ERROR_GENERAL,
                desc,
                emailOrigen,
                asunto,
                archivoHtm
            );

            throw new ValidacionHtmException(E999_ERROR_GENERAL, "Error general procesando HTM: " + desc, ex);
        }
    }

    
}
