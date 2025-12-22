package com.siarex247.leerCuenta;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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

                    msg.setFlag(Flags.Flag.SEEN, true);
                    logger.info("Correo procesado y marcado como LEÍDO");

                } catch (Exception e) {
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
     */
    private void procesarMensaje(Message msg, EmpresasForm empresaSesion) throws Exception {

        if (!msg.getContentType().toLowerCase().contains("multipart")) return;

        String correoDe = ((InternetAddress) msg.getFrom()[0]).getAddress();
        logger.info("Procesando correo de: " + correoDe);
        logger.info("Asunto: " + msg.getSubject());

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

            // ================= PARSE HTM =================
            AribaHtmParser parser = new AribaHtmParser();
            OrdenCompraHtmData data = parser.parse(archivo);

            // LOG DEPURACIÓN (Verificamos qué extrajo el parser)
            logger.info("--- DATOS EXTRAÍDOS DEL PARSER ---");
            logger.info("DESDE (Proveedor): " + data.getDesde());
            logger.info("PARA  (Destinatario/Empleado): " + data.getPara());
            logger.info("----------------------------------");

            // ================= VALIDACIONES OBLIGATORIAS =================
            validarNumeroOrden(data);
            validarMoneda(data);
            validarImporte(data);
            validarClasificacion(data);

            // ================= VALIDACIÓN EMPRESA (EMISOR) =================
            // Validamos que el DESDE (HP) exista como empresa
            validarRazonSocialHTM(data);

            // ================= VALIDACIÓN PROVEEDOR (EMISOR) =================
            // Validamos que el DESDE (HP) exista como proveedor
            validarProveedorEmisorFlexible(data, empresaSesion);


            // ================= LOG FINAL =================
            logDatos(data);
        }
    }

    /**
     * Valida que la razón social (DESDE) del HTM exista en EMPRESAS.NOMBRE_LARGO
     */
    private void validarRazonSocialHTM(OrdenCompraHtmData data) throws Exception {

        String razonHtm = data.getDesde(); // HP Inc.

        if (razonHtm == null || razonHtm.trim().isEmpty()) {
            throw new Exception("Razón social HTM vacía");
        }

        // OJO: aquí sí normalizas porque tu EMPRESAS está guardada así
        String razonNormalizada = normalizarBasica(razonHtm);

        AccesoBean accesoBean = new AccesoBean();
        EmpresasForm empresaBd = accesoBean.consultaEmpresaPorNombreLargo(razonNormalizada);

        if (empresaBd == null) {
            logger.error("❌ RAZÓN SOCIAL NO REGISTRADA EN EMPRESAS: " + razonHtm);
            throw new Exception("Empresa emisora no registrada");
        }

        logger.info("✔ Empresa emisora válida: " + empresaBd.getNombreLargo());
    }

    /**
     * Valida PROVEEDOR usando EXACTAMENTE el valor "PARA" tal como viene del HTM.
     * (Sin trims, sin replace, sin limpiar espacios, sin candidatas)
     */
    private void validarProveedorEmisorFlexible(OrdenCompraHtmData data, EmpresasForm empresaSesion) throws Exception {

        String proveedorRaw = data.getPara(); // TAL CUAL viene del HTM

        if (proveedorRaw == null || proveedorRaw.isEmpty()) {
            throw new Exception("Proveedor (PARA) vacío");
        }

        ProveedoresBean proveedoresBean = new ProveedoresBean();

        logger.info("Validando proveedor (PARA) EXACTO: [" + proveedorRaw + "] esquema=" + empresaSesion.getEsquema());

        // ✅ AQUÍ ya consulta en contrare_<esquema>
        boolean existe = proveedoresBean.existeProveedorPorRazonSocial(proveedorRaw, empresaSesion.getEsquema());

        if (!existe) {
            logger.error("❌ PROVEEDOR (PARA) NO REGISTRADO exacto: [" + proveedorRaw + "]");
            throw new Exception("Proveedor no registrado: " + proveedorRaw);
        }

        logger.info("✔ Proveedor válido (PARA): " + proveedorRaw);
    }




    private List<String> generarCandidatasProveedor(String razonRaw) {

        String raw = colapsarEspacios(razonRaw);

        Set<String> set = new LinkedHashSet<>();
        set.add(raw); // 1. Completa

        // 2. Sin Company Code (Quita US71...)
        String sinCompanyCode = raw.replaceAll("(?i)\\bUS\\d{2,}\\b.*$", "").trim();
        if (!sinCompanyCode.isEmpty()) set.add(sinCompanyCode);

        // 3. Primeras 2 palabras
        String primeras2 = primerasPalabras(raw, 2);
        if (!primeras2.isEmpty()) set.add(primeras2);
        
        // 4. Primeras 3 palabras (A veces HP Inc. necesita el punto o una palabra más)
        String primeras3 = primerasPalabras(raw, 3);
        if (!primeras3.isEmpty()) set.add(primeras3);

        // 5. Solo Texto y Espacios (quita puntos y comas)
        String sinPuntos = raw.replace(".", "").replace(",", "").trim();
        if (!sinPuntos.isEmpty()) set.add(sinPuntos);

        return new ArrayList<>(set);
    }

    private String primerasPalabras(String texto, int n) {
        if (texto == null) return "";
        String t = colapsarEspacios(texto);
        if (t.isEmpty()) return "";
        String[] parts = t.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length && i < n; i++) {
            if (i > 0) sb.append(" ");
            sb.append(parts[i]);
        }
        return sb.toString().trim();
    }

    private String colapsarEspacios(String texto) {
        return texto == null ? "" : texto.replaceAll("\\s+", " ").trim();
    }

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

    private void validarMoneda(OrdenCompraHtmData data) throws Exception {
        String moneda = data.getMoneda();
        if (moneda == null) {
            logger.error("❌ MONEDA NULA");
            throw new Exception("Moneda obligatoria");
        }
        if (!"MXN".equalsIgnoreCase(moneda) && !"USD".equalsIgnoreCase(moneda)) {
            logger.error("❌ MONEDA NO VÁLIDA: " + moneda);
            throw new Exception("Moneda no permitida: " + moneda);
        }
        logger.info("✔ Moneda válida: " + moneda);
    }

    private void validarImporte(OrdenCompraHtmData data) throws Exception {
        if (data.getMonto() == null) {
            logger.error("❌ IMPORTE NULO");
            throw new Exception("Importe obligatorio");
        }
        if (data.getMonto().doubleValue() <= 0) {
            logger.error("❌ IMPORTE INVÁLIDO: " + data.getMonto());
            throw new Exception("Importe debe ser mayor a 0");
        }
        logger.info("✔ Importe válido: " + data.getMonto());
    }

    private void validarClasificacion(OrdenCompraHtmData data) throws Exception {
        String clasificacion = data.getClasificacionCodigo();
        if (clasificacion == null || clasificacion.trim().isEmpty()) {
            logger.error("❌ CLASIFICACIÓN VACÍA");
            throw new Exception("Clasificación obligatoria");
        }
        logger.info("✔ Clasificación válida: " + clasificacion);
    }

    private void validarNumeroOrden(OrdenCompraHtmData data) throws Exception {
        String orden = data.getOrdenCompra();
        if (orden == null || orden.trim().isEmpty()) {
            logger.error("❌ NÚMERO DE ORDEN VACÍO");
            throw new Exception("Número de orden obligatorio");
        }
        logger.info("✔ Número de orden válido: " + orden);
    }

    private void logDatos(OrdenCompraHtmData data) {
        logger.info("========== DATOS ORDEN (HTM) ==========");
        logger.info("ORDEN: " + data.getOrdenCompra());
        logger.info("PROVEEDOR (DESDE): " + data.getDesde()); // Quien envía la factura
        logger.info("EMPLEADO (PARA): " + data.getPara());    // Quien recibe
        logger.info("EMPRESA (RAW): " + data.getEmpresa());
        logger.info("RFC/TAXID: " + data.getTaxId());
        logger.info("MONEDA: " + data.getMoneda());
        logger.info("MONTO: " + data.getMonto());
        logger.info("CLASIFICACIÓN: " + data.getClasificacionCodigo());
        logger.info("======================================");
    }
}