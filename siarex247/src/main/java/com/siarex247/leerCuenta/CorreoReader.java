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

import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsPATH;

public class CorreoReader {

    private static final Logger logger = Logger.getLogger("siarex247");

    /**
     * Lee la cuenta de correo de una empresa y procesa adjuntos HTM
     */
    public void leerCuenta(EmpresasForm empresa) {

        Store store = null;
        Folder inbox = null;

        try {

            String usuario = empresa.getEmailDominio();
            String password = UtilsPATH.PASSWORD_DOMINIOS_SIAREX;
            String host = "mail.siarex.com";

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
                    procesarMensaje(msg, empresa);

                    // ✅ SOLO si todo salió bien
                    msg.setFlag(Flags.Flag.SEEN, true);
                    logger.info("Correo marcado como LEÍDO");

                } catch (Exception e) {
                    logger.error("Error procesando mensaje, NO se marca como leído", e);
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
    private void procesarMensaje(Message msg, EmpresasForm empresa) throws Exception {

        if (!msg.getContentType().toLowerCase().contains("multipart")) return;

        String correoDe = ((InternetAddress) msg.getFrom()[0]).getAddress();
        logger.info("Procesando correo de: " + correoDe);
        logger.info("Asunto: " + msg.getSubject());

        Multipart mp = (Multipart) msg.getContent();

        for (int i = 0; i < mp.getCount(); i++) {

            BodyPart part = mp.getBodyPart(i);

            if (!Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) continue;

            String fileName = part.getFileName();
            if (fileName == null) continue;

            String lower = fileName.toLowerCase();

            if (!lower.endsWith(".htm")) continue;

            File carpeta = new File(
                UtilsPATH.RUTA_PUBLIC_HTML
                + File.separator + empresa.getEsquema()
                + File.separator + "CORREO_FACTURAS"
            );

            if (!carpeta.exists()) {
                boolean creada = carpeta.mkdirs();
                logger.info("Creando carpeta destino: " + creada);
            }

            File archivo = new File(carpeta, fileName);

            ((MimeBodyPart) part).saveFile(archivo);

            logger.info("HTM guardado correctamente: " + archivo.getAbsolutePath());

            // 🔽 AQUÍ después va el parser HTM
            AribaHtmParser parser = new AribaHtmParser();
             OrdenCompraHtmData data = parser.parse(archivo);
             logDatos(data);
        }
    }
    
    private void logDatos(OrdenCompraHtmData data) {

        if (data == null) return;

        logger.info("========== DATOS HTM ==========");
        logger.info("ORDEN: " + data.getOrdenCompra());
        logger.info("EMPRESA: " + data.getEmpresa());
        logger.info("TAX ID: " + data.getTaxId());
        logger.info("MONEDA: " + data.getMoneda());
        logger.info("MONTO: " + data.getMonto());
        logger.info("CLASIFICACION: " + data.getClasificacionCodigo());
        logger.info("EMAIL: " + data.getEmailDestino());
        logger.info("================================");
    }
}
