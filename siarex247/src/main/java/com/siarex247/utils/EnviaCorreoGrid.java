package com.siarex247.utils;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EnviaCorreoGrid {

	public static final Logger logger = Logger.getLogger("siarex247");
	static String smtpHost = "";
    
    //Puerto que se usar� en el servidor.
    static String smtpPuerto = "";
    //Indicamos que vamos a auntenticarnos en el servidor
    static String smtpAuth = "true";
    static Properties props = new Properties();
    
    static String host = UtilsPATH.HOST_CORREO;
    
    public static void enviarCorreo(String rutaArchivo, String mensajeCorreo, boolean enviaArchivo, String [] emailTO, String [] emailCC, String subject,
    		final String usuarioEmisorMensaje, final String passwordEmisorMensaje){

    	String result;
    	try {
    		// logger.info("host====>"+host);
    		// logger.info("usuarioEmisorMensaje====>"+usuarioEmisorMensaje);
    		// logger.info("passwordEmisorMensaje====>"+passwordEmisorMensaje);
    		
    	   // Recipient's email ID needs to be mentioned.
    	   // Sender's email ID needs to be mentioned
    	   // Assuming you are sending email from localhost
    	   // Get system properties object
    	   Properties properties = new Properties();
    	   properties.put("mail.transport.protocol", "smtp");
    	   properties.put("mail.smtp.host", host);
    	   //properties.put("mail.smtps.ssl.trust", host);
    	   properties.put("mail.smtp.port", "465");
    	   properties.put("mail.smtp.auth", "true");
    	   properties.put("mail.smtp.socketFactory.class",
    	           "javax.net.ssl.SSLSocketFactory");
    	   //properties.put("mail.smtp.starttls.enable", "true");

    	   // creates a new session with an authenticator
    	   Authenticator auth = new Authenticator() {
    	       public PasswordAuthentication getPasswordAuthentication() {
    	           return new PasswordAuthentication("apikey", passwordEmisorMensaje);
    	       }
    	   };

    	   // Get the default Session object.
    	   //Session mailSession = Session.getDefaultInstance(properties, auth);
    	   Session mailSession = Session.getInstance(properties, auth);
    	   
    	      // Create a default MimeMessage object.
    	      MimeMessage message = new MimeMessage(mailSession);
    	      
    	      // Set From: header field of the header.
    	      message.setFrom(new InternetAddress(usuarioEmisorMensaje));
    	      
    	      // Set To: header field of the header.
    	      
    	     /* message.addRecipient(Message.RecipientType.TO,
    	                               new InternetAddress(to));
    	      */
    	     String mensajeFinal = Utils.regresaCaracteresHTML(mensajeCorreo);
    	      
    	     BodyPart texto = new MimeBodyPart();
          	texto.setContent(mensajeFinal, "text/html; charset=ISO-8859-1");
          	
          	
    	      MimeMultipart multiParte = new MimeMultipart();
          	
          	if (enviaArchivo){
          		BodyPart adjunto = new MimeBodyPart();
          		File imagen = new File(rutaArchivo);
          		adjunto.setDataHandler(new DataHandler(new FileDataSource(rutaArchivo)));
          		adjunto.setFileName(imagen.getName());
          		multiParte.addBodyPart(adjunto);
          	}
          	multiParte.addBodyPart(texto);
          	
    	      
    	      for (int x = 0; x < emailTO.length; x++){
              	if (!"N".equals(emailTO[x]) && !"".equals(emailTO[x])){
              		logger.info("Enviando corrreo a : "+emailTO[x]);
              		message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTO[x]));	
              	}
              		
              }
    	      
    	      if (emailCC != null ){
              	for (int x = 0; x < emailCC.length; x++){
              		if (!"N".equals(emailCC[x]) && !"".equals(emailCC[x])){
              			logger.info("con copia para : "+emailCC[x]);
              			message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailCC[x]));	
              		}
                  }
              }
    	      // Set Subject: header field
    	      // logger.info("subject====>"+subject);
              message.setSubject(Utils.regresaCaracteresHTML(subject));
    	      
             // logger.info("multiParte====>");
              message.setContent(multiParte);
              
    	      // Now set the actual message
    	     // message.setText("Mensaje generado " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
    	      
    	      // Send message
              // logger.info("antes del envio====>");
              Transport t = null;
              t = mailSession.getTransport("smtp");
              t.connect(usuarioEmisorMensaje,passwordEmisorMensaje);
              t.sendMessage(message,message.getAllRecipients());
              t.close();
              
              
    	      // Transport.send(message);
    	      result = "Sent message successfully....";
    	      logger.info("result==========>"+result);
    	      
    	      
    	   } catch (MessagingException mex) {
    	      Utils.imprimeLog("", mex);
    	      result = "Error: unable to send message....";
    	   }
    }


    public static void enviarCorreoMultipleFiles(String rutaArchivo [], String mensajeCorreo, boolean enviaArchivo, String [] emailTO, String [] emailCC, String subject, final String usuarioEmisorMensaje, final String passwordEmisorMensaje){
    	String result;
    	try {
    	   // Recipient's email ID needs to be mentioned.
    	  // String to = "oacosta@gmail.com";
    	   // Sender's email ID needs to be mentioned
    	   // Assuming you are sending email from localhost
    	   
    	   // Get system properties object
    	   Properties properties = new Properties();
    	   properties.put("mail.transport.protocol", "smtp");
    	   properties.put("mail.smtp.host", host);
    	   //properties.put("mail.smtps.ssl.trust", host);
    	   properties.put("mail.smtp.port", "465");
    	   properties.put("mail.smtp.auth", "true");
    	   properties.put("mail.smtp.socketFactory.class",
    	           "javax.net.ssl.SSLSocketFactory");
    	   //properties.put("mail.smtp.starttls.enable", "true");

    	   // creates a new session with an authenticator
    	   Authenticator auth = new Authenticator() {
    	       public PasswordAuthentication getPasswordAuthentication() {
    	           return new PasswordAuthentication("apikey", passwordEmisorMensaje);
    	       }
    	   };

    	   // Get the default Session object.
    	   Session mailSession = Session.getDefaultInstance(properties, auth);

    	   
    	      // Create a default MimeMessage object.
    	      MimeMessage message = new MimeMessage(mailSession);
    	      
    	      // Set From: header field of the header.
    	      message.setFrom(new InternetAddress(usuarioEmisorMensaje));
    	      
    	      // Set To: header field of the header.
    	      
    	     /* message.addRecipient(Message.RecipientType.TO,
    	                               new InternetAddress(to));
    	      */
    	     String mensajeFinal = Utils.regresaCaracteresHTML(mensajeCorreo);
    	      
    	     BodyPart texto = new MimeBodyPart();
          	texto.setContent(mensajeFinal, "text/html; charset=ISO-8859-1");
          	
          	
    	      MimeMultipart multiParte = new MimeMultipart();
          	
    	      if (enviaArchivo){
          		for (int x = 0; x < rutaArchivo.length; x++) {
              		BodyPart adjunto = new MimeBodyPart();
              		File imagen = new File(rutaArchivo[x]);
              		adjunto.setDataHandler(new DataHandler(new FileDataSource(rutaArchivo[x])));
              		adjunto.setFileName(imagen.getName());
              		multiParte.addBodyPart(adjunto);
          		}
          	}
    	      
          	multiParte.addBodyPart(texto);
          	
    	      
    	      for (int x = 0; x < emailTO.length; x++){
              	if (!"N".equals(emailTO[x]) && !"".equals(emailTO[x])){
              		logger.info("Enviando corrreo a : "+emailTO[x]);
              		message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTO[x]));	
              	}
              		
              }
    	      
    	      if (emailCC != null ){
              	for (int x = 0; x < emailCC.length; x++){
              		if (!"N".equals(emailCC[x]) && !"".equals(emailCC[x])){
              			logger.info("con copia para : "+emailCC[x]);
              			message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailCC[x]));	
              		}
                  }
              }
    	      // Set Subject: header field
              message.setSubject(subject);
    	      
              message.setContent(multiParte);
              
    	      // Now set the actual message
    	     // message.setText("Mensaje generado " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
    	      
    	      // Send message
    	      Transport.send(message);
    	      result = "Sent message successfully....";
    	   } catch (MessagingException mex) {
    	      mex.printStackTrace();
    	      result = "Error: unable to send message....";
    	   }
    }

    
    
    public static void main(String[] args) {
		try {
			String emailTO [] = {"jose.burgos@technologies247.com"};
			enviarCorreo(null, "texto de prueba..", false, emailTO, null, "TETS SUBJECT", null, null);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
    
}
