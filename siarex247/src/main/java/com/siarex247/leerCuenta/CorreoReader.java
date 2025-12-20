package com.siarex247.leerCuenta;

import java.io.File;
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

            // 🔹 SOLO correos NO LEÍDOS
            FlagTerm noLeidos = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message[] mensajes = inbox.search(noLeidos);

            logger.info("Correos no leídos encontrados: " + mensajes.length);

            for (Message msg : mensajes) {

                try {

                    String correoDe = ((InternetAddress) msg.getFrom()[0]).getAddress();
                    logger.info("Correo entrante de: " + correoDe);

                    // 🚫 CORREO NO DESEADO
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

            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            File archivo = new File(carpeta, fileName);
            ((MimeBodyPart) part).saveFile(archivo);

            logger.info("HTM guardado correctamente: " + archivo.getAbsolutePath());

            // ================= PARSE HTM =================
            AribaHtmParser parser = new AribaHtmParser();
            OrdenCompraHtmData data = parser.parse(archivo);

            // ================= VALIDACIÓN RAZÓN SOCIAL =================
            validarRazonSocialHTM(data);

            // ================= LOG DATOS =================
            logDatos(data);
        }
    }

    /**
     * Valida que la razón social (DESDE) del HTM exista en EMPRESAS.NOMBRE_LARGO
     */
    private void validarRazonSocialHTM(OrdenCompraHtmData data) throws Exception {

        String razonHtm = data.getDesde();

        if (razonHtm == null || razonHtm.trim().isEmpty()) {
            throw new Exception("Razón social HTM vacía");
        }

        String razonNormalizada = normalizar(razonHtm);

        AccesoBean accesoBean = new AccesoBean();
        EmpresasForm empresaBd =
            accesoBean.consultaEmpresaPorNombreLargo(razonNormalizada);

        if (empresaBd == null) {
            logger.error("❌ RAZÓN SOCIAL NO REGISTRADA: " + razonHtm);
            throw new Exception("Empresa emisora no registrada");
        }

        logger.info("✔ Empresa emisora válida: " + empresaBd.getNombreLargo());
    }

    /**
     * Normaliza texto para comparación
     */
    private String normalizar(String texto) {

        return texto == null ? null :
            texto.toLowerCase()
                 .replace(".", "")
                 .replace(",", "")
                 .replace("  ", " ")
                 .trim();
    }

    /**
     * Valida correo no deseado (configuración)
     */
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
        logger.info("DESDE (RAZÓN SOCIAL): " + data.getDesde());
        logger.info("PARA (CONTACTO): " + data.getPara());
        logger.info("EMPRESA: " + data.getEmpresa());
        logger.info("RFC: " + data.getTaxId());
        logger.info("MONEDA: " + data.getMoneda());
        logger.info("MONTO: " + data.getMonto());
        logger.info("CLASIFICACIÓN: " + data.getClasificacionCodigo());
        logger.info("======================================");
    }
}
