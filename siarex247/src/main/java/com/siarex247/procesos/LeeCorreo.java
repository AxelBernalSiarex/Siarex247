package com.siarex247.procesos;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.layOut.OrdenesCompra.CargasComprasBean;
import com.siarex247.layOut.OrdenesCompra.OrdenesCompraBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Perfiles.PerfilesBean;
import com.siarex247.seguridad.Perfiles.PerfilesForm;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.validaciones.ValidacionesFactura;

public class LeeCorreo {

	public static final Logger logger = Logger.getLogger("siarex247");

	
	public void monitoreaCorreo(){
		try{
				AccesoBean accesoBean = new AccesoBean();
				ConexionDB connPool = new ConexionDB();
				Connection con = null;
				ResultadoConexion rc = null;
				try{
					rc = connPool.getConnectionSiarex();
					con = rc.getCon();
					ArrayList<EmpresasForm> listaEmpresas = accesoBean.listaEmpresas(con, rc.getEsquema()); //  detalleEmpresas(con, "siarex");
					EmpresasForm empresasForm = null;
					
					for (int y = 0; y < listaEmpresas.size(); y++){
						empresasForm = listaEmpresas.get(y);
						// logger.info("Buscando Correos de la empresa : "+empresasForm.getEmailDominio());
						if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
							try {
								// enviaCorreo(empresasForm);
								descargaAdjuntos(empresasForm);
							} catch (Exception e) {
								Utils.imprimeLog("", e);
							}
						}
					}
				}catch(Exception e){
					Utils.imprimeLog("monitoreaCorreo ", e);
				}finally{
					try{
						if (con != null){
							con.close();
						}
					}catch(Exception e){
						con = null;
					}
				}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
	
	
 private void descargaAdjuntos(final EmpresasForm empresasForm ){
		
		ArrayList<String> archivos = new ArrayList<String>();
		Store store = null;
		Folder folder = null;
		String numeroOrden = "";
		String correoDe = "";
		String nombreDe = "";
		String rutaDepositar = "";
		
		boolean esOrdenCompra = false;
		boolean noTieneArchivo = false;
		try {
			
			String hostCorreo = UtilsPATH.HOST_CORREO_PROCESO;
			String PUBLIC_HTML = UtilsPATH.RUTA_PUBLIC_HTML;
			String REPOSITORIO_DOCUMENTOS = UtilsPATH.REPOSITORIO_DOCUMENTOS;
			String CORREO_FACTURAS = "CORREO_FACTURAS"; 
			String PWD_DOMINIOS = UtilsPATH.PASSWORD_DOMINIOS_SIAREX;
			
			/*
			logger.info("hostCorreo====>"+hostCorreo);
			logger.info("PUBLIC_HTML====>"+PUBLIC_HTML);
			logger.info("REPOSITORIO_DOCUMENTOS====>"+REPOSITORIO_DOCUMENTOS);
			logger.info("CORREO_FACTURAS====>"+CORREO_FACTURAS);
			logger.info("PWD_DOMINIOS====>"+PWD_DOMINIOS);
			logger.info("emailDominio====>"+empresasForm.getEmailDominio());
			*/
			
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", hostCorreo);
			properties.put("mail.smtp.auth", "true");

			Session session = Session.getDefaultInstance(properties,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(empresasForm.getEmailDominio(), PWD_DOMINIOS);
						}
					});
	    	   
			store = session.getStore("imap");
			//store.connect(host, empresasForm.getEmailDominio(), empresasForm.getPwdCorreoProceso());
			store.connect(hostCorreo, empresasForm.getEmailDominio(), PWD_DOMINIOS);
			// logger.info("************* Se ha conectado al servidor de correo **************");
			folder = store.getFolder("inbox");
			folder.open(Folder.READ_WRITE);

			Message[] m = folder.getMessages();

			FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			Message[] message = folder.search(ft);
			
			//logger.info("************* PUBLIC_HTML **************"+PUBLIC_HTML);
			// logger.info("************* repositorio **************"+empresasForm.getEsquema());
			
			// logger.info("************* message **************"+message);
			
			//Folder folderProcesados = null;
			String subject = "";
			// logger.info("************* message (2) **************"+message.length);
			for (int a = 0; a < message.length; a++) {
				noTieneArchivo = false;
				correoDe = ((InternetAddress) message[a].getFrom()[0]).getAddress();
				//nombreDe = ((InternetAddress) message[a].getFrom()[0]).getPersonal();
				nombreDe = ((InternetAddress) message[a].getFrom()[0]).getAddress();
				numeroOrden = message[a].getSubject();
				subject = message[a].getSubject();
				
				logger.info("************* De **************"+nombreDe);
				logger.info("************* Correo De **************"+correoDe);
				logger.info("************* Sent **************"+message[a].getSentDate());
				logger.info("************* Received **************"+message[a].getReceivedDate());
				logger.info("************* subject **************"+subject);
				
				
				//mapaResult.put("CORREO_DE", correoDe);
				//mapaResult.put("NUMERO_ORDEN", message[a].getSubject());
				/*
				if (a == 1) {
					break;
				}
				 */
				
				if (nombreDe == null || nombreDe.toLowerCase().indexOf("delivery") > -1 ||  subject.toLowerCase().indexOf("delivery") > -1 ||  subject.indexOf("Mail Delivery") > -1  ||  "postmaster@pavfsmtp03.sonepar-us.com".equalsIgnoreCase(correoDe) || "MAILER-DAEMON@mailchannels.net".equalsIgnoreCase(correoDe) || "postmaster@ARCS.local".equalsIgnoreCase(correoDe) || "postmaster@toyota.com".equalsIgnoreCase(correoDe) || subject.indexOf("Undeliverable") > -1  || subject.indexOf("Error al procesar su factura") > -1){
					// logger.info("************* El correo se regreso **************");
					//break;
					message[a].setFlag(Flags.Flag.DELETED, true);
					if (message[a].isSet(Flags.Flag.DELETED)) {
						// logger.info("************* El correo fue borrado **************"+correoDe);
						//MailJava mail = new MailJava();
						//mail.eliminaCorreos();
				    }
				}else{
					//Multipart multipart = (Multipart) message[a].getContent();

					String contentType = message[a].getContentType();
					// logger.info("contentType---->"+contentType);
					// store attachment file name, separated by comma
					StringBuffer messageContent = new StringBuffer();
					if (contentType.contains("multipart")) {
						// content may contain attachments
						Multipart multiPart = (Multipart) message[a].getContent();
						int numberOfParts = multiPart.getCount();
						for (int partCount = 0; partCount < numberOfParts; partCount++) {
							MimeBodyPart part = (MimeBodyPart) multiPart
									.getBodyPart(partCount);
							
							if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
								// this part is attachment
								String fileName = part.getFileName();
								// logger.info("************* fileName **************"+fileName);
								if (fileName == null) {
									fileName = "";
									noTieneArchivo = true;
								}
								
								if (fileName.trim().toLowerCase().endsWith(".pdf") || fileName.trim().toLowerCase().endsWith(".xml") || fileName.trim().toLowerCase().endsWith(".txt")) {
									if(fileName.trim().toLowerCase().endsWith(".pdf")){
										// archivoPdf = PUBLIC_HTML + File.separator + empresasForm.getEsquema() +  File.separator  + CORREO_FACTURAS + File.separator + fileName;
										rutaDepositar = PUBLIC_HTML + File.separator  + empresasForm.getEsquema() +  File.separator  + CORREO_FACTURAS + File.separator + fileName;
									}
									if(fileName.trim().toLowerCase().endsWith(".xml")){
										// archivoXML = PUBLIC_HTML + File.separator  + empresasForm.getEsquema() +  File.separator  + CORREO_FACTURAS + File.separator + fileName;
										rutaDepositar = PUBLIC_HTML + File.separator  + empresasForm.getEsquema() +  File.separator  + CORREO_FACTURAS + File.separator + fileName;
									}
									
									if(( fileName.trim().toLowerCase().endsWith(".txt") || fileName.trim().toLowerCase().endsWith(".xls") || fileName.trim().toLowerCase().endsWith(".xlsx") ) 
												&& "CARGAR_ORDENES".equalsIgnoreCase(subject)){ // es para cargar las ordenes de compra
										rutaDepositar = REPOSITORIO_DOCUMENTOS + File.separator + empresasForm.getEsquema() +  File.separator  + "ORDENES_COMPRA" + File.separator + fileName;
									}
									
									archivos.add(rutaDepositar);
									part.saveFile(rutaDepositar); 
								}
								rutaDepositar = null;
							}else{
								messageContent.append(dumpPart(part));
							}
						}
					} else if (contentType.contains("text/plain")
							|| contentType.contains("text/html")) {
							// logger.info("El contenido es un texto................");
					}
					
					message[a].setFlag(Flags.Flag.SEEN, true);//SEEN MARCA COMO VISTO LOS MENSAJES NO LEIDOS,FLAGGED LES PONE UNA ESTRELLITA
					
					// se cierran los objetos..
					try {
						if(folder!=null){
							try {
								folder.close(true);
							} catch (Exception e2) {
								Utils.imprimeLog("", e2);
							}
						}
						if(store!=null){
							try {
								store.close();
							} catch (Exception e2) {
								Utils.imprimeLog("", e2);
							}
						}
						session = null;
					}catch(Exception e) {
						folder = null;
						store = null;
						session = null;
					}
					
					
					File fileMail = null;
					String MENSAJE_RESPUESTA = null;
					 String SUBJECT_RESPUESTA = null;
					if (archivos.size() == 2 && !subject.equalsIgnoreCase("MULTIPLE") && !subject.equalsIgnoreCase("COMPLEMENTO") && !subject.equalsIgnoreCase("CARGAR_ORDENES")){
						logger.info("********** ENTRO A VALIDAR ORDENES DE COMPRA ****************"); 
						long numeroOrdenProcesada = 0;
						 ValidacionesFactura valFacturaBean = new ValidacionesFactura();
						 File fileXML = null;
						 File filePDF = null;
						 
						 try{
							 String emailTO [] = {correoDe};
							 numeroOrdenProcesada = Utils.noNuloLong(numeroOrden);
							 
							 for (int x = 0; x < archivos.size(); x++) {
								 fileMail = new File(archivos.get(x));
								 if (fileMail.exists()) {
									 if (fileMail.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
										 filePDF = new File(archivos.get(x));
									 }else if (fileMail.getAbsolutePath().toLowerCase().endsWith(".xml")) {
										 fileXML = new File(archivos.get(x));
									 }
								 }
							 }
							
							 logger.info("********** numeroOrdenProcesada****************"+numeroOrdenProcesada);
							 if (filePDF == null) {
								 MENSAJE_RESPUESTA = "Estimado proveedor favor de proporcionar el archivo .PDF para darle seguimiento a sus facturas";
								 SUBJECT_RESPUESTA = "Siarex - Orden no Existe";
							 }else if (fileXML == null) {
								 MENSAJE_RESPUESTA = "Estimado proveedor favor de proporcionar el archivo .XML para darle seguimiento a sus facturas";
								 SUBJECT_RESPUESTA = "Siarex - Orden no Existe";
							 }else if (numeroOrdenProcesada == 0) {
								 MENSAJE_RESPUESTA = "Estimado proveedor favor de proporcionar el numero de orden de compra para darle seguimiento a sus facturas";
								 SUBJECT_RESPUESTA = "Siarex - Orden no Existe";
							 }else {
								 String arrValidaciones [] = valFacturaBean.iniciarProceso(empresasForm.getEsquema(), numeroOrdenProcesada, fileXML, filePDF, "MX", 0, correoDe );
								 MENSAJE_RESPUESTA = arrValidaciones [0];
								 SUBJECT_RESPUESTA = arrValidaciones [1];
							 }
							 logger.info("MENSAJE_RESPUESTA===>"+MENSAJE_RESPUESTA);
							 logger.info("SUBJECT_RESPUESTA===>"+SUBJECT_RESPUESTA);
							 
							 EnviaCorreoGrid.enviarCorreo(null, MENSAJE_RESPUESTA, false, emailTO, null, SUBJECT_RESPUESTA, empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
						 }catch(Exception e){
							Utils.imprimeLog("", e);
						 }
					 }else if ("CARGAR_ORDENES".equalsIgnoreCase(subject)) {
						 logger.info("********************************************************");
						 
						 if (archivos.size() == 0) {
							 MENSAJE_RESPUESTA = "Estimado usuario favor de proporcionar el archivo .TXT para cargar sus ordenes de compra en el sistema SIAREX";
							 SUBJECT_RESPUESTA = "Siarex - Orden no Existe";
							 logger.info("MENSAJE_RESPUESTA===>"+MENSAJE_RESPUESTA);
						 }else {
							 File fileTXT = new File(archivos.get(0));
							 //String emailTO [] = {correoDe};
							 
							 UsuariosBean usuariosBean = new UsuariosBean();
							 UsuariosForm usuariosForm = usuariosBean.datosUsuarioEsquema(empresasForm.getEsquema(), correoDe);
							 
							 PerfilesBean perfilesBean = new PerfilesBean();
							 PerfilesForm perfilesForm = perfilesBean.consultaPerfilEsquema(empresasForm.getEsquema(), usuariosForm.getIdRegistro());
							 
							 if ("001".equalsIgnoreCase(perfilesForm.getNombreCorto())) {
								 if (fileTXT.exists()) {
									 OrdenesCompraBean ordenesCompraBean = new OrdenesCompraBean();
									 int claveHistorico = ordenesCompraBean.grabarHistorialCorreo(empresasForm.getEsquema(), fileTXT.getName(), 0, "1",1, 0, 0, correoDe, null);
									 int resultado = ordenesCompraBean.actualizaHistorialCorreo(empresasForm.getEsquema(), claveHistorico, 0, 2, 0, 0, correoDe);
									 
									  CargasComprasBean cargasCompras = new CargasComprasBean();
									  cargasCompras.iniciaCarga(fileTXT.getAbsolutePath(), fileTXT.getName(), correoDe, empresasForm.getEsquema(), claveHistorico, empresasForm.getRequisicion());
									  
									  MENSAJE_RESPUESTA = "El proceso de carga de ordenes de compra ha iniciado satisfactoriamente, revise el estatus una vez que ha terminado.";
								      SUBJECT_RESPUESTA = "Siarex - Proceso iniciado satisfactoriamente";
								    //  EnviaCorreoGrid.enviarCorreo(null, MENSAJE_RESPUESTA, false, emailTO, null, SUBJECT_RESPUESTA, empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
								 }else {
									 logger.info("No existe archivo .TXT para procesar, favor de revisar la ruta del repositorio.");
									  MENSAJE_RESPUESTA = "Estimado usuario favor de adjuntar su archivo .TXT para procesar.";
								      SUBJECT_RESPUESTA = "Siarex - Proceso iniciado satisfactoriamente";
								    //  EnviaCorreoGrid.enviarCorreo(null, MENSAJE_RESPUESTA, false, emailTO, null, SUBJECT_RESPUESTA, empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
								 }
								 
							 }else {
								 MENSAJE_RESPUESTA = "Usuario no permitido para registrar Ordenes de compra.";
								 SUBJECT_RESPUESTA = "Siarex - Usuario no Permitido";
								 logger.info("MENSAJE_RESPUESTA===>"+MENSAJE_RESPUESTA);
								 
							 }
							 
							 logger.info("MENSAJE_RESPUESTA===>"+MENSAJE_RESPUESTA);
							 
						 }
					 }
				}
				
			}
			// logger.info("*************  finalizo la carga  **************");
		}catch (Exception e) {
			logger.info("*************  error(1) **************---- Folio Orden : "+numeroOrden);
			logger.info("*************  CORREO de : "+correoDe);
			Utils.imprimeLog("", e);
			//MailJava mail = new MailJava();
			//mail.eliminaCorreos();
		} 
		finally {
			try {
				if(folder!=null){
					try {
						// folder.close(true);
					} catch (Exception e2) {
						Utils.imprimeLog("", e2);
					}
				}
				if(store!=null){
					try {
						store.close();
					} catch (Exception e2) {
						Utils.imprimeLog("", e2);
					}
				}
			}catch(Exception e) {
				folder = null;
				store = null;
			}
		}
	}

	
	private StringBuffer dumpPart(Part p) throws MessagingException, IOException{
		StringBuffer sb = new StringBuffer();
	    if (p.isMimeType("text/plain"))
	    {
	        if (!p.getContent().toString().equals(null))
	        	sb.append((String)p.getContent());
	
	    }
	    else if (p.isMimeType("multipart/*"))
	    {
	        Multipart mp = (Multipart)p.getContent();
	
	        for (int x = 0; x < mp.getCount(); x++)
	        {
	            sb.append(dumpPart(mp.getBodyPart(x)));
	        }	
	       
	    }
	    return sb;
	}

	
}
