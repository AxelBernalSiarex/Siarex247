package com.siarex247.validaciones;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.Conceptos;
import com.itextpdf.xmltopdf.LeerXML;
import com.itextpdf.xmltopdf.Retencion;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.configSistema.CierreAnnio.CierreAnnioBean;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.configuraciones.RegimenFiscal.RegimenFiscalBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.MensajesSIAREX;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesBean;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesQuerys;

public class ValidacionesMultiple {

	public static final Logger logger = Logger.getLogger("siarex247");
	public static MensajesSIAREX mensajeSIAREX = null;
	public static String MENSAJE_VALIDACIONES = null;
	public static String SUBJECT_VALIDACIONES = null;
	private HashMap<String, String> MAPA_RESULTADO = new HashMap<String, String>();
	private double TOTAL_GLOBAL = 0.00;
	
   public String [] iniciarProceso(String esquemaEmpresa, File fileTXT, File fileXML, File filePDF, String idLenguaje, int idPerfil, String usuarioAdjunto) {
		
		String respuestaValidacion [] = {"", "", ""}; // MENSAJE, SUBJECT Y RESULTADO VALIDACION (ERROR o EXITO)
		AccesoBean accesoBean = new AccesoBean();
		try {
			mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			// VisorOrdenesForm visorForm = existeOrden(esquemaEmpresa, folioEmpresa);
			 ProveedoresForm provForm = obtenerProveedor(esquemaEmpresa, fileXML);
			EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquemaEmpresa);
			
			String idMultiple = "";
			  long idTiempo = System.currentTimeMillis();
				String idTipo = String.valueOf(idTiempo);
				int lon = idTipo.length();
			  idMultiple = "M_"+idTipo.substring(lon-9, lon);
			  
			if (validarCaracteresEspeciales(fileXML.getAbsolutePath())) {
				respuestaValidacion[0] = "Estimado usuario le informamos que su factura no puede ser procesada por contenener caracteres especiales, le pedimos cambiar el nombre de sus archivos y volverlos a procesar";
				respuestaValidacion[1] = "SIAREX - Error al procesar su factura";
				respuestaValidacion[2] = "ERROR";
				
			}else if (provForm.getClaveRegistro() == 0) {
				respuestaValidacion[0] = "Estimado usuario le informamos que su factura no puede ser procesada ya que el proveedor no existe en nuestra base de datos.";
				respuestaValidacion[1] = "SIAREX - Error al procesar su factura";
				respuestaValidacion[2] = "ERROR";
			}else if (!"OK".equalsIgnoreCase(validarCertificadosProveedor(esquemaEmpresa, provForm))) {  // se validan los certificados
				respuestaValidacion[0] = Utils.regresaCaracteresNormales(MENSAJE_VALIDACIONES);
				respuestaValidacion[1] = "Siarex - " + SUBJECT_VALIDACIONES;
				respuestaValidacion[2] = "ERROR";
			}else if (!"OK".equalsIgnoreCase(iniciarValidacionesXML(esquemaEmpresa, provForm, empresaForm, fileXML, filePDF, idPerfil, idMultiple))) {  // se hacen validacion generales del XML
					respuestaValidacion[0] = Utils.regresaCaracteresNormales(MENSAJE_VALIDACIONES);
					respuestaValidacion[1] = "Siarex - " + SUBJECT_VALIDACIONES;
					respuestaValidacion[2] = "ERROR";
			}else {
				VisorOrdenesForm visorXMLForm = obtenerValoresXML(esquemaEmpresa, fileXML);
				
				ArrayList<String> arreloOrd = UtilsFile.leeArchivoTXT(fileTXT.getAbsolutePath());
				HashMap<Long, String> mapaSeries = new HashMap<Long, String>();
				HashMap<Long, String> mapaEstatus = new HashMap<Long, String>();
				String mensajeTotal = validacionOrdenesTXT(esquemaEmpresa, visorXMLForm, empresaForm, provForm, idPerfil, fileTXT, mapaSeries, mapaEstatus); // se realizan las validaciones del TXT con las ordens de compra
				
				if ("OK".equalsIgnoreCase(mensajeTotal)) {
					String mensajeCorreoFinal = revisaErrores(arreloOrd, mapaSeries);
					
					if ("".equals(mensajeCorreoFinal)){
						String estatusCFDI = "";
						if ("A10".equalsIgnoreCase(MENSAJE_VALIDACIONES)) {
							estatusCFDI = "A10";
						}
						
						actualizarOrdenes(esquemaEmpresa, visorXMLForm, provForm, mapaEstatus, arreloOrd, usuarioAdjunto, idMultiple, fileXML, filePDF, estatusCFDI);
						
						if ("A10".equalsIgnoreCase(estatusCFDI)) {
							respuestaValidacion[0] = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE16, "<< FOLIO_FACTURA >>", "MULTIPLE");
							respuestaValidacion[1] = "Siarex - " +  "Factura En Proceso de Validacion";
							respuestaValidacion[2] = "EXITO";
						}else {
							respuestaValidacion[0] = "Estimado proveedor le informamos que la(s) factura(s) correspondiente(s) a la orden de compra MULTIPLE fue procesada exitosamente y sera pagada de acuerdo a nuestro calendario de pagos.";
							respuestaValidacion[1] = "Siarex - " +  mensajeSIAREX.SUBJECT1;
							respuestaValidacion[2] = "EXITO";
						}
						
						
					}else {
						respuestaValidacion[0]  = mensajeCorreoFinal;
						respuestaValidacion[1] = "Siarex - " +  mensajeSIAREX.SUBJECT2;
						respuestaValidacion[2] = "ERROR";
					}
				}else {
					respuestaValidacion[0]  = mensajeTotal;
					respuestaValidacion[1] = "Siarex - " +  mensajeSIAREX.SUBJECT2;
					respuestaValidacion[2] = "ERROR";
				}
			}
			
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return respuestaValidacion;
	}
   
   
   private boolean validarCaracteresEspeciales(String rutaXMLValida) {
	   boolean bandCaracteres = false;
	   try {
		   
		   bandCaracteres = Utils.validaCaracteresEspeciales(rutaXMLValida);
	   }catch(Exception e) {
		   Utils.imprimeLog("", e);
	   }
	   return bandCaracteres;
   }

   
   private ProveedoresForm obtenerProveedor(String esquemaEmpresa, File fileXML) {
	   ProveedoresForm provForm = null;
	   Comprobante _comprobante = null;
	   ResultadoConexion rc = null;
	   ConexionDB connPool = null;
	   Connection con = null;
	   try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
		   
		   _comprobante = LeerXML.ObtenerComprobante(fileXML.getAbsolutePath());
			
		   if (_comprobante == null) {
			   provForm = new ProveedoresForm();
		   }else {
			   ProveedoresBean provBean = new ProveedoresBean();
			   provForm = provBean.consultarProveedorXrfc(con, esquemaEmpresa, _comprobante.getEmisor().getRfc());
			   if (provForm == null) {
				   provForm = new ProveedoresForm();
			   }
		   }
			
	   }catch(Exception e) {
		   Utils.imprimeLog("", e);
	   }finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			}catch(Exception e) {
				con = null;
			}
		}
	   return provForm;
   }
   
   
   private String validarCertificadosProveedor(String esquemaEmpresa, ProveedoresForm provForm) {
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		String mensajeCertificacion = "OK";
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			String BLOQUEAR_IMSS = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BLOQUEAR_IMSS");
			String BLOQUEAR_SAT = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BLOQUEAR_SAT");
			
		    logger.info("BLOQUEAR_IMSS---->"+BLOQUEAR_IMSS);
		    logger.info("BLOQUEAR_SAT---->"+BLOQUEAR_SAT);
			
		    logger.info("getBandIMSS---->"+provForm.getBandIMSS());
		    logger.info("getBandSAT---->"+provForm.getBandSAT());
		    logger.info("getConfirmarIMSS---->"+provForm.getConfirmarIMSS());
		    logger.info("getConfirmarSAT---->"+provForm.getConfirmarSAT());
		    
		    if ("S".equalsIgnoreCase(BLOQUEAR_IMSS) && "S".equalsIgnoreCase(provForm.getBandIMSS())) {
				if ("S".equalsIgnoreCase(provForm.getConfirmarIMSS())) {
					mensajeCertificacion = "OK";
				}else {
					mensajeCertificacion = "ERROR";
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE63, "<< FOLIO_FACTURA >>", "MULTIPLE");
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT3;
				}
			}
		    
		    if ("OK".equalsIgnoreCase(mensajeCertificacion)) {
				if ("S".equalsIgnoreCase(BLOQUEAR_SAT) && "S".equalsIgnoreCase(provForm.getBandSAT())) {
					if ("S".equalsIgnoreCase(provForm.getConfirmarSAT())) {
						mensajeCertificacion = "OK";
					}else {
						mensajeCertificacion = "ERROR";
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE62, "<< FOLIO_FACTURA >>", "MULTIPLE");
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT3;
					}
				}
				
			}
		    
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			}catch(Exception e) {
				con = null;
			}
		}
		return mensajeCertificacion;
	}

   
   
   private String iniciarValidacionesXML(String esquemaEmpresa, ProveedoresForm provForm, EmpresasForm empresaForm, File fileXML, File filePDF, int clavePerfil, String idMultiple){
	   Comprobante _comprobante = null;
	   ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		boolean bandXML = false;
		String mensajeValidacionesXML = "OK";
		
		ValidacionesPdf leePdf = new ValidacionesPdf();
		
		String usoCFDI = "";
		String claveProdServ = "";
		
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			
		   _comprobante = LeerXML.ObtenerComprobante(fileXML.getAbsolutePath());
		   
		    // double totalXML =  _comprobante.getTotal();
			String rfcFacturaXML = _comprobante.getEmisor().getRfc(); 
			String razonSocialEmisor = _comprobante.getEmisor().getNombre();
			//double subTotalXML = _comprobante.getSubTotal();
			String uuidXML = _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
			LocalDateTime fechaUUIDXMLLD = _comprobante.getFecha();
			String rfcReceptorXML = _comprobante.getReceptor().getRfc();
			String folioXML = _comprobante.getFolio();
			LocalDateTime fechaFactLD = _comprobante.getFecha();
			String usoCFDI_XML = _comprobante.getReceptor().getUsoCFDI();
			String claveProdServ_XML = "";
			String tipoComprobante =  _comprobante.getTipoDeComprobante(); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@TipoDeComprobante");
			
			
			logger.info("tipoComprobante------->"+tipoComprobante);

			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			String fechaUUIDXML = null;
			//String fechaFact = null;
			
			if(fechaUUIDXMLLD != null) {
				fechaUUIDXML = fechaUUIDXMLLD.format(formatter);
	    	}
			
			if(fechaFactLD != null) {
				//fechaFact = fechaFactLD.format(formatter);
	    	}
			
			//--- Nuevas Validaciones
			String nombreEmisor =  _comprobante.getEmisor().getNombre();
			String nombreReceptor =  _comprobante.getReceptor().getNombre();
			
			String regimenFiscalEmisor =  _comprobante.getEmisor().getRegimenFiscal();
			// String regimenFiscalReceptor =  _comprobante.getReceptor().getRegimenFiscalReceptor();
			
			int domicilioFiscalEmisor =  Utils.noNuloINT(_comprobante.getLugarExpedicion());
			int domicilioFiscalReceptor = 0;
			try {
				domicilioFiscalReceptor =  Utils.noNuloINT(_comprobante.getReceptor().getDomicilioFiscalReceptor());	
			}catch(Exception e) {
				domicilioFiscalReceptor = 0;
				// e.printStackTrace();
			}
			
			int codigoPostalEmisor = provForm.getCodigoPostal(); // Utils.noNuloNormal(String.valueOf(jsonobj.get("codigoPostal")));
			String razonSocial = provForm.getRazonSocial(); // Utils.noNuloNormal(String.valueOf(jsonobj.get("razonSocial")));
			String regimenFiscal = provForm.getRegimenFiscal(); // Utils.noNuloNormal(String.valueOf(jsonobj.get("regimenFiscal")));
			
			RegimenFiscalBean regimenBean = new RegimenFiscalBean();
			JSONObject jsonRegimenFiscal = regimenBean.buscarConfigurados(con, rc.getEsquema(), provForm.getClaveRegistro());
			
			boolean bandRegimenFiscal = false;
			if(jsonRegimenFiscal == null || jsonRegimenFiscal.size() <= 0) {
				bandRegimenFiscal = true;
			}
			else {
				if(jsonRegimenFiscal.containsKey(regimenFiscalEmisor)) {
					bandRegimenFiscal = true;
				}
			}

			if(!bandRegimenFiscal) {
				if(!"".equalsIgnoreCase(regimenFiscal)) {
					if(regimenFiscalEmisor.equalsIgnoreCase(regimenFiscal)) {
						bandRegimenFiscal = true;
					}
				}
			}
			
			
			//--- Nuevas Validaciones

			try {
				Conceptos concepto = _comprobante.getConceptos();
				claveProdServ_XML =  concepto.getConcepto().get(0).getClaveProdServ(); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Conceptos/Concepto/@"+ucFirst("claveProdServ", bandXML));	
			}catch(Exception e) {
				claveProdServ_XML =  "";
			}
			
			String serieXML = _comprobante.getSerie();
			JSONArray jsonConceptos = UtilsValidaciones.getConceptosNuevo(_comprobante);
			
			// String monedaXML = "SIN_MONEDA";

			double tipoCambio = 0;
			try{
				tipoCambio = _comprobante.getTipoCambio();		
			}catch(Exception e){
				tipoCambio = 0;
			}
			
			// se obtiene bandera de tipo de validacion
			//ConfigAdicionalesBean confSistemaBean = new ConfigAdicionalesBean();
			//  HashMap<String, String> mapaConf = confSistemaBean.obtenerConfiguraciones(con, rc.getEsquema());
			// Termina
			  
			
			// se obtiene bandera de tipo de validacion
			  String SERIE_FALTANTE = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "SERIE_FALTANTE");
			  
			  if ("".equalsIgnoreCase(SERIE_FALTANTE)) {
				  SERIE_FALTANTE = "A";
			  }
			  
			  
			  String SERIE_FINAL = null;
				if ("".equals(serieXML)){
					SERIE_FINAL = SERIE_FALTANTE.concat(folioXML);
				}else{
					SERIE_FINAL = serieXML.concat(folioXML);
				}
				
				boolean bandValidaCierre = true;
				
				String VALIDAR_CIERRE_ADMIN = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDAR_CIERRE_ADMIN");
				
				  if ("".equalsIgnoreCase(VALIDAR_CIERRE_ADMIN)) {
					  VALIDAR_CIERRE_ADMIN = "S";
				  }
				  
				  if (clavePerfil == 1 && "N".equalsIgnoreCase(VALIDAR_CIERRE_ADMIN)) {
					  bandValidaCierre = false;  
				  }
				  
				
			     boolean bandValidaComplemento = true;
						
				String VALIDAR_COMPLEMENTO_ADMIN = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDAR_COMPLEMENTO_ADMIN");
				
				  if ("".equalsIgnoreCase(VALIDAR_COMPLEMENTO_ADMIN)) {
					  VALIDAR_COMPLEMENTO_ADMIN = "S";
				  }
				  
				  if (clavePerfil == 1 && "N".equalsIgnoreCase(VALIDAR_COMPLEMENTO_ADMIN)) {
					  bandValidaComplemento = false;  
				  }
				  
				  int numeroValidacion = 0;
				  
				  String folioFactura = "MULTIPLE";
				  
				  
				  
				  if (!"I".equalsIgnoreCase(tipoComprobante)){
					  MENSAJE_VALIDACIONES  = mensajeSIAREX.MENSAJE45;
					  SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					  mensajeValidacionesXML  = "NO_EXITOSO";
					  numeroValidacion = 1;
				   }else if (!"OK".equalsIgnoreCase(validarFechaRecepcion(esquemaEmpresa, clavePerfil, _comprobante, SERIE_FINAL))){
					   mensajeValidacionesXML  = "NO_EXITOSO";
					   numeroValidacion = 2;
				   }else  if (!nombreReceptor.equalsIgnoreCase(empresaForm.getNombreLargo()) && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
						//ordenesForm.setMensajeValidacion(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE80, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)));
					   MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE80, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura));
					   SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					   mensajeValidacionesXML  = "NO_EXITOSO";
					   numeroValidacion = 3;
					   
					}else if (!nombreEmisor.equalsIgnoreCase(razonSocial) && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
						//ordenesForm.setMensajeValidacion(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE81, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)));
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE81, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura));
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 4;
					}else if (domicilioFiscalEmisor != codigoPostalEmisor && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
							//ordenesForm.setMensajeValidacion(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE82, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)));
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE82, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura));
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 5;
					}else if (domicilioFiscalReceptor != empresaForm.getCodigoPostal() && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
							//ordenesForm.setMensajeValidacion(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE83, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)));
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE83, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura));
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 6;
					}else if (!bandRegimenFiscal && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
						//ordenesForm.setMensajeValidacion(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE85, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)));
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE85, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura));
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 7;
					}else if (jsonConceptos.size() == 0) {
						// String mensajeCorreo = mensajeSIAREX.MENSAJE60;  
						// ordenesForm.setMensajeValidacion(mensajeCorreo );
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE60, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura));
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 8;
						
					}else if (!provForm.getRfc().equalsIgnoreCase(rfcFacturaXML)){
						// ordenesForm.setMensajeValidacion("Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a no coincide el RFC registrado en nuestra base de datos contra el que usted esta incluyendo en las facturas .xml");
						// String mensajeCorreo = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE1, "<< FOLIO_FACTURA >>", folioFactura);  
						// ordenesForm.setMensajeValidacion(mensajeCorreo );
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE1, "<< FOLIO_FACTURA >>", folioFactura);
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 9;
						
						
					}else if (!empresaForm.getRfc().equalsIgnoreCase(rfcReceptorXML)){
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE2, "<< FOLIO_FACTURA >>", folioFactura);
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 10;
					}else if (bandValidaComplemento && new ValidacionesComplemento().tieneComplementoPendientes(con, rc.getEsquema(), provForm.getClaveRegistro())){ // se valida si ya cargo todos los complementos de pago
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE37, "<< FOLIO_FACTURA >>", String.valueOf(folioFactura));
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 11;
					}else if (leePdf.lecturaPDF(filePDF.getAbsolutePath(), uuidXML) == -1){
						//ordenesForm.setMensajeValidacion("Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a que el Folio Fiscal de su archivo .xml no coincide contra el Folio Fiscal de su factura.PDF.");
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE3, "<< FOLIO_FACTURA >>", folioFactura);
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 12;
						
					}else if (validaUUID(con, rc.getEsquema(), uuidXML, provForm.getRfc())){
						//ordenesForm.setMensajeValidacion("Estimado proveedor su orden de compra "+folioFactura+", no pudo ser procesada debido a que el numero de folio ya se encuentra en nuestra base de datos");
						// String mensajeCorreo = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE5, "<< FOLIO_FACTURA >>", folioFactura);  
						// ordenesForm.setMensajeValidacion(mensajeCorreo );
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE5, "<< FOLIO_FACTURA >>", folioFactura);
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 12;
					}else if (validaSerieDUPLICADA(con, rc.getEsquema(), provForm, SERIE_FINAL)) { // Valida si la Serie y Folio ya estan registrados en base de datos 
						//String mensajeCorreo = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE39, "<< FOLIO_FACTURA >>", folioFactura);  
						//ordenesForm.setMensajeValidacion(mensajeCorreo );
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE39, "<< FOLIO_FACTURA >>", folioFactura); 
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 13;
						
					}else if ("".equals(uuidXML) || "".equals(fechaUUIDXML)) {
						//ordenesForm.setMensajeValidacion("Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a que no se encontro el sello digital en su archivo .xml");
						//String mensajeCorreo = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE7, "<< FOLIO_FACTURA >>", folioFactura);  
						//ordenesForm.setMensajeValidacion(mensajeCorreo );
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE7, "<< FOLIO_FACTURA >>", folioFactura);  
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidacionesXML  = "NO_EXITOSO";
						numeroValidacion = 14;
						
					}else if (bandXML){  // se realizan las validaciones de la 3.3
						if (ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDACION_USOCFDI").equalsIgnoreCase("VISOR")){
							  if (!usoCFDI.equalsIgnoreCase(usoCFDI_XML)){
								//ordenesForm.setMensajeValidacion("Estimado proveedor "+razonSocialEmisor+" le informamos que su orden de compra "+folioFactura+" no pudo ser procesada con (e)xito debido a que el uso del CFDI utilizado en su facturaci(o)n electr(o)nica no concuerda con el registrado por la empresa "+razonSocialReceptor+", le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.");
								// String mensajeCorreo = Utils.getMensajeValidacion( Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE11, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", razonSocialReceptor);
								// ordenesForm.setMensajeValidacion(mensajeCorreo );
								  MENSAJE_VALIDACIONES = Utils.getMensajeValidacion( Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE11, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());  
								  SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
								  mensajeValidacionesXML  = "NO_EXITOSO";
								  numeroValidacion = 15;								
							  }else if (!claveProdServ.equalsIgnoreCase(claveProdServ_XML)){
								//ordenesForm.setMensajeValidacion("Estimado proveedor "+razonSocialEmisor+" le informamos que su orden de compra "+folioFactura+" no pudo ser procesada con (e)xito debido a que la clave del producto o servicio utilizado en su facturaci(o)n electr(o)nica no concuerda con el registrado por la empresa "+razonSocialReceptor+", le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.");
								  // String mensajeCorreo = Utils.getMensajeValidacion( Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE12, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", razonSocialReceptor);
								  // ordenesForm.setMensajeValidacion(mensajeCorreo);
								  MENSAJE_VALIDACIONES = Utils.getMensajeValidacion( Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE12, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());  
								  SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
								  mensajeValidacionesXML  = "NO_EXITOSO";
								  numeroValidacion = 16;								
							  }
							}else if (ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDACION_USOCFDI").equalsIgnoreCase("TABLA")){
								if (!ValidacionesUSO.validarUsoRFC(con, rc.getEsquema(), provForm.getRfc())){  // no encontro el RFC
									//ordenesForm.setMensajeValidacion("Estimado proveedor "+razonSocialEmisor+" le informamos que su orden de compra "+folioFactura+" no pudo ser procesada con (e)xito debido a que la empresa "+razonSocialReceptor+",  aun no configura sus RFC para recibir facturaci(o)n electr(o)nica con versi(o)n 3.3 le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.");
									// String mensajeCorreo = Utils.getMensajeValidacion( Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE13, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", razonSocialReceptor);
								  // ordenesForm.setMensajeValidacion(mensajeCorreo);
									  MENSAJE_VALIDACIONES = Utils.getMensajeValidacion( Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE13, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());  
									  SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
									  mensajeValidacionesXML  = "NO_EXITOSO";
									  numeroValidacion = 17;								
									
								}else if (!ValidacionesUSO.validarUsoCDFI(con, rc.getEsquema(), provForm.getRfc(), usoCFDI_XML)){  // no encontro el Uso CFDI
									// ordenesForm.setMensajeValidacion("Estimado proveedor "+razonSocialEmisor+" le informamos que su orden de compra "+folioFactura+" no pudo ser procesada con (e)xito debido a que el uso del CFDI utilizado en su facturaci(o)n electr(o)nica no concuerda con el registrado por la empresa "+razonSocialReceptor+", le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.");
									// String mensajeCorreo = Utils.getMensajeValidacion(  Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE14, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", razonSocialReceptor);
								    // ordenesForm.setMensajeValidacion(mensajeCorreo);
									  MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(  Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE14, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());  
									  SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
									  mensajeValidacionesXML  = "NO_EXITOSO";
									  numeroValidacion = 18;								
								  
								}else if (!ValidacionesUSO.validarUsoClaveMultiple(con, rc.getEsquema(), provForm.getRfc(), usoCFDI_XML, jsonConceptos, idMultiple)){  // no encontro las claves y producto
									//ordenesForm.setMensajeValidacion("Estimado proveedor "+razonSocialEmisor+" le informamos que su orden de compra "+folioFactura+" no pudo ser procesada con (e)xito debido a que la clave del producto o servicio utilizado en su facturaci(o)n electr(o)nica no concuerda con el registrado por la empresa "+razonSocialReceptor+", le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.");
									// String mensajeCorreo = Utils.getMensajeValidacion( Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE15, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", razonSocialReceptor);
									// ordenesForm.setMensajeValidacion(mensajeCorreo);
									  MENSAJE_VALIDACIONES = Utils.getMensajeValidacion( Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE15, "<< RAZON_EMISOR >>", razonSocialEmisor), "<< FOLIO_FACTURA >>", String.valueOf(folioFactura)), "<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());  
									  SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
									  mensajeValidacionesXML  = "NO_EXITOSO";
									  numeroValidacion = 19;								
									
								}
							}else if (ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDACION_USOCFDI").equalsIgnoreCase("XML")) {
								
								if (!ValidacionesUSO.validarUsoClaveMultiple(con, rc.getEsquema(), provForm.getRfc(), usoCFDI_XML, jsonConceptos, idMultiple)){  // no encontro las claves y producto
									// ordenesForm.setEstatusCFDI("A10");
									// ordenesForm.setMensajeValidacion("OK");
									MENSAJE_VALIDACIONES = "A10";
									mensajeValidacionesXML  = "OK";
									numeroValidacion = 20;
								}
							}
					}
				  
			  
	   }catch(Exception e) {
		   Utils.imprimeLog("", e);
	   }finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			}catch(Exception e) {
				con = null;
			}
		}
	   return mensajeValidacionesXML;
   }
   
   
   
   private String validacionOrdenesTXT(String esquemaEmpresa, VisorOrdenesForm visorXMLForm, EmpresasForm empresaForm, ProveedoresForm provForm, int clavePerfil, File fileTXT, 
		   HashMap<Long, String> mapaSeries, HashMap<Long, String> mapaEstatus) {
	   
	   String cadenaTXT [] = null;
	   double totalCompararTXT = 0;
	   String monedaTXT = "";
	   String desMonedaFolio = "";
	   String valAnexo24 = "";
	   
	    ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		//String mensajeCertificacion = "OK";
		
		VisorOrdenesBean visorBean = new VisorOrdenesBean();
		VisorOrdenesForm visorForm = null;
		
		// HashMap<Long, String> mapaEstatus = new HashMap<Long, String>();
		//HashMap<Long, String> mapaSeries = new HashMap<Long, String>();
		
		String mensajeValidacion = "";
		String mensajeTotal = "OK";
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
		   
		   ArrayList<String> arreloOrd = UtilsFile.leeArchivoTXT(fileTXT.getAbsolutePath());
		   String estatusFactura = "A10";
		   
		   
		   for (int x = 0; x < arreloOrd.size(); x++){
			    estatusFactura = "A10";
				cadenaTXT = arreloOrd.get(x).split(";");
				long folioFactura = Long.parseLong(cadenaTXT[0]);
				logger.info("********* I N I C I A N D O  V A L I D A C I O N E S  P O R  F O L I O  E L  M U L T I P L E ****************");
				totalCompararTXT = Double.parseDouble(cadenaTXT[1]);
				monedaTXT = cadenaTXT[2];
				if ("0".equals(empresaForm.getTipoAcceso())){
					visorForm = visorBean.consultarOrdenXfolioEmpresa(con, rc.getEsquema(), folioFactura);  // obtenerEstatusProveedor(con, rc.getEsquema(), folioFactura);
					estatusFactura = visorForm.getEstatusOrden();
					if (estatusFactura == null) {
						estatusFactura = "NO_EXISTE";
					}
				}else{
					visorForm = new VisorOrdenesForm();
				}
				// logger.info("estatusFactura : "+estatusFactura + " _ Folio : "+folioFactura);
				mapaEstatus.put(folioFactura, estatusFactura); // se agrega el estatus
				mapaSeries.put(folioFactura, Utils.noNuloNormal(visorForm.getSerieFolio())); // se agrega la serie
				mensajeValidacion = verificaFolio(estatusFactura, folioFactura, Utils.noNuloNormal(visorForm.getSerieFolio()));
				//logger.info("mensajeValidacion : "+mensajeValidacion+ " _ Folio : "+folioFactura);
				if ("OK".equalsIgnoreCase(mensajeValidacion)){ // cumplio las validaciones primarias el folio
					double totalComparar = 0;
					//logger.info("tipoAcceso : "+empresaForm.getTipoAcceso());
					if ("0".equals(empresaForm.getTipoAcceso()) || "".equals(empresaForm.getTipoAcceso())){
						if ("A2".equalsIgnoreCase(estatusFactura)){
							if ("0".equals(provForm.getTipoConfirmacion())){ // es contra el sub-total
								totalComparar =  Utils.noNuloDouble(visorForm.getMonto());
							}else{ // es contra el total
								totalComparar = Utils.noNuloDouble(visorForm.getTotal());
							}
						}else{
							totalComparar = Utils.noNuloDouble(visorForm.getMonto());
						}
					}
				
					desMonedaFolio = UtilsValidaciones.validaDesMoneda(monedaTXT);
					valAnexo24 = UtilsValidaciones.validaAnexo24(visorXMLForm.getTipoMoneda(), visorXMLForm.getTipoCambio(), provForm.getAnexo24(), monedaTXT);
					//logger.info("cantidadFacturada : "+totalComparar);
					//logger.info("totalCompararTXT : "+totalCompararTXT);
					TOTAL_GLOBAL+=totalCompararTXT;
					
					validacionesPorOrden(folioFactura, desMonedaFolio, visorXMLForm.getTipoMoneda(), totalComparar, totalCompararTXT, 
							provForm.getLimiteTolerancia(), empresaForm.getTipoAcceso(), valAnexo24,  clavePerfil, con, esquemaEmpresa, provForm.getClaveRegistro());
					
		   }
		 }
		   if ("OK".equalsIgnoreCase(mensajeValidacion)) {
			   validacionTotalGlobal(Utils.noNuloDouble(visorXMLForm.getSubTotal()), provForm.getLimiteTolerancia());  // SE VALIDA EL TOTAL GLOBAL
			  	  mensajeTotal = MAPA_RESULTADO.get("TOTAL_GLOBAL");   
		   }
	  	  if (mensajeTotal == null) {
	  		mensajeTotal = "OK";
	  	  }
	   }catch(Exception e) {
		   Utils.imprimeLog("", e);
	   }finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			}catch(Exception e) {
				con = null;
			}
		}
		return mensajeTotal;
   }
   
   
   private void validacionesPorOrden(long folioFactura, String desMonedaFolio, String monedaXML, double totalCompararBD, 
			double totalCompararTXT, String limiteTolerancia, String tipoAcceso, String anexo24, int clavePerfil,  Connection con, String esquema, int claveProveedor){
		try{  
			logger.info("********************************** APLICANDO VALIDACIONES *******************************************");
			

			/*
			logger.info("folioFactura---->"+folioFactura);
			logger.info("desMonedaFolio---->"+desMonedaFolio);
			logger.info("totalCompararXML---->"+totalCompararTXT);
			logger.info("totalCompararBD---->"+totalCompararBD);
			*/
			ValidacionesComplemento compleBean = new ValidacionesComplemento();
			boolean tieneComplementos = compleBean.descartaValidacionComplementoOrden(con, esquema, folioFactura);
		//	logger.info("tieneComplementos---->"+tieneComplementos);
			
			if (tieneComplementos) {
				MAPA_RESULTADO.put("DESCARTAR_COMPLEMENTO","true");
			}
			
			if (!"SIN_MONEDA".equalsIgnoreCase(monedaXML) &&  "".equals(desMonedaFolio)){
				MAPA_RESULTADO.put("SIN_MONEDA_"+folioFactura, "Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a que el tipo de moneda proporcionado en el archivo .xml no se encontro en la base de datos.");
			}else if ( !algoritoTotal(totalCompararTXT, totalCompararBD, limiteTolerancia) && "0".equals(tipoAcceso)){
				MAPA_RESULTADO.put("TOTAL_ERROR_"+folioFactura,"Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a que no coincide el monto de su factura contra la orden de compra.");
			}else if ("NG".equalsIgnoreCase(anexo24)){
				MAPA_RESULTADO.put("ANEXO24_"+folioFactura,"Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a que no cumple con los requisitos estipulados en el SAT con respecto a la miscelanea fiscal del anexo 24.");
			}else {
				if (esOrdenDelProveedor(con, esquema, folioFactura, claveProveedor)) {
					MAPA_RESULTADO.put("OK_"+folioFactura,""+folioFactura);	
				}else {
					MAPA_RESULTADO.put("ORDEN_CORRESPONDE_"+folioFactura,"Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+" no pudo ser procesada con Exito debido a que la razon social emisora de su archivo .xml no coincide con la registrada en la base de datos");
				}
			}
			
		}catch(Exception e){
			Utils.imprimeLog("folioFactura---->"+folioFactura+"_", e);
			
		}
		logger.info("********************************** FINALIZANDO LAS VALIDACIONES *******************************************");
	}

   
   private void validacionTotalGlobal(double totalCompararXML, String limiteTolerancia){
		try{  
			
			DecimalFormat df2 = new DecimalFormat("#.##"); 
			String totalTXT = df2.format(TOTAL_GLOBAL);
			/*
			logger.info("********************************** APLICANDO VALIDACION DEL TOTAL *******************************************");
			logger.info("********************************** totalCompararXML : ****************************"+totalCompararXML);
			logger.info("********************************** TOTAL_GLOBAL ****************************"+TOTAL_GLOBAL);
			logger.info("********************************** TOTAL TXT FORMATEADO ****************************"+totalTXT);
			*/
			double totValidar = Double.parseDouble(totalTXT);
			//logger.info("********************************** totValidar  ****************************"+totValidar);
			
			if ( !algoritoTotal(totalCompararXML, totValidar, limiteTolerancia)){
				MAPA_RESULTADO.put("TOTAL_GLOBAL","Estimado proveedor le informamos que la solicitud, no pudo ser procesada con exito debido a que no coincide el monto total de su factura contra el monto total de sus ordenes de compra.");
			}
			
		}catch(Exception e){
			Utils.imprimeLog("folioFactura---->_", e);
			
		}
		logger.info("********************************** FINALIZANDO LA VALIDACION DEL TOTAL GLOBAL *******************************************");
	}   
   
		
		private String verificaFolio(String estatusFactura, long folioFactura, String serieFolio){
			String mensajeValidacion = "OK";
			 try{
					if ("A5".equalsIgnoreCase(estatusFactura) 
							|| "A2".equalsIgnoreCase(estatusFactura) 
							   || "A10".equalsIgnoreCase(estatusFactura)){
						mensajeValidacion = "OK";
					}else if ("NO_EXISTE".equalsIgnoreCase(estatusFactura)){
						mensajeValidacion =  "Estimado proveedor le informamos que su factura no pudo ser procesada con exito, debido a que su orden de compra "+folioFactura+" no existe en nuestra base de datos.";
						MAPA_RESULTADO.put("NO_EXISTE_"+folioFactura, mensajeValidacion);
					}else if ("A3".equalsIgnoreCase(estatusFactura) 
							|| "A4".equalsIgnoreCase(estatusFactura)){
						mensajeValidacion =  "Estimado proveedor le informamos que la factura correndiente a la orden de compra "+folioFactura+" no pudo ser procesada con exito debido a que ya se encuentra registrada y asignada a la factura con numero "+serieFolio;
						MAPA_RESULTADO.put("YA_PROCESADA_"+folioFactura, mensajeValidacion);
					}else{
						mensajeValidacion =  "Su factura no pudo ser procesada, favor de verificar la informacion e intentarlo de nuevo.";
						MAPA_RESULTADO.put("CAUSA_DESCONOCIDA_"+folioFactura, mensajeValidacion);
					}
				 
			 }catch(Exception e){
				 Utils.imprimeLog("", e);
			 }
			 return mensajeValidacion;
		}
		
   
		
		
		private String revisaErrores(ArrayList<String> arreloOrd, HashMap<Long, String> mapaSeries){
			String cadenaTXT [] = null;
			StringBuffer sbCorreo = new StringBuffer();
			HashMap<String, String> mapaResultadoCorreo = new HashMap<String, String>();
			String mensajeSinModenda = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIOS_FACTURA>> no pudo ser procesada con exito debido a que el tipo de moneda proporcionado en el archivo .xml no se encontro en la base de datos.\n";
			String mensajeTotalError = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIOS_FACTURA>> no pudo ser procesada con exito debido a que no coincide el monto de su factura contra la orden de compra.\n";
			String mensajeAnexo24 = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIOS_FACTURA>> no pudo ser procesada con exito debido a que no cumple con los requisitos estipulados en el SAT con respecto a la miscelanea fiscal del anexo 24.\n";
			String mensajeRazonEmisor = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIOS_FACTURA>> no pudo ser procesada con Exito debido a que la razon social emisora de su archivo .xml no coincide con la registrada en la base de datos a la cual corresponde esa orden de compra.\n";
			String mensajeNoExiste = "Estimado proveedor le informamos que su factura no pudo ser procesada con exito, debido a que su orden de compra <<FOLIOS_FACTURA>> no existe en nuestra base de datos.\n";
			String mensajeYaProcesada = "Estimado proveedor le informamos que la factura correndiente a la orden de compra <<FOLIOS_FACTURA>> no pudo ser procesada con exito debido a que ya se encuentra registrada y asignada a la factura con numero <<SERIE_FOLIO>>\n";
			String mensajeCausaDesconocida = "Estimado proveedor le informamos que la factura correndiente a la orden de compra <<FOLIOS_FACTURA>> favor de verificar la informacion e intentarlo de nuevo.\n";
			
			String mensajeMapa = "";
			
			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			StringBuffer sb3 = new StringBuffer();
			StringBuffer sb4 = new StringBuffer();
			StringBuffer sb5 = new StringBuffer();
			StringBuffer sb6 = new StringBuffer();
			StringBuffer sb7 = new StringBuffer();
			StringBuffer sb8Series = new StringBuffer();
			try{
				for (int x = 0; x < arreloOrd.size(); x++){
					cadenaTXT = arreloOrd.get(x).split(";");
					long folioFactura = Long.parseLong(cadenaTXT[0]);
					if (MAPA_RESULTADO.get("SIN_MONEDA_"+folioFactura) != null){
						sb1.append(folioFactura).append(",");
						mensajeMapa = mensajeSinModenda.replaceAll("<<FOLIOS_FACTURA>>", sb1.toString());
						mapaResultadoCorreo.put("SIN_MONEDA", mensajeMapa);
						
					}else if (MAPA_RESULTADO.get("TOTAL_ERROR_"+folioFactura) != null){
						sb2.append(folioFactura).append(",");
						mensajeMapa = mensajeTotalError.replaceAll("<<FOLIOS_FACTURA>>", sb2.toString());
						mapaResultadoCorreo.put("TOTAL_ERROR", mensajeMapa);
						
					}else if (MAPA_RESULTADO.get("ANEXO24_"+folioFactura) != null){
						sb3.append(folioFactura).append(",");
						mensajeMapa = mensajeAnexo24.replaceAll("<<FOLIOS_FACTURA>>", sb3.toString());
						mapaResultadoCorreo.put("ANEXO24", mensajeMapa);
						
					}else if (MAPA_RESULTADO.get("ORDEN_CORRESPONDE_"+folioFactura) != null){
						sb4.append(folioFactura).append(",");
						mensajeMapa = mensajeRazonEmisor.replaceAll("<<FOLIOS_FACTURA>>", sb4.toString());
						mapaResultadoCorreo.put("ORDEN_CORRESPONDE", mensajeMapa);
						
					}else if (MAPA_RESULTADO.get("NO_EXISTE_"+folioFactura) != null){
						sb5.append(folioFactura).append(",");
						mensajeMapa = mensajeNoExiste.replaceAll("<<FOLIOS_FACTURA>>", sb5.toString());
						mapaResultadoCorreo.put("NO_EXISTE", mensajeMapa);
						
					}else if (MAPA_RESULTADO.get("YA_PROCESADA_"+folioFactura) != null){
						sb6.append(folioFactura).append(", ");
						sb8Series.append(mapaSeries.get(folioFactura)).append(",");
						mensajeMapa = mensajeYaProcesada.replaceAll("<<FOLIOS_FACTURA>>", sb6.toString()).replaceAll("<<SERIE_FOLIO>>", sb8Series.toString());
						mapaResultadoCorreo.put("YA_PROCESADA", mensajeMapa);
						
					}else if (MAPA_RESULTADO.get("CAUSA_DESCONOCIDA_"+folioFactura) != null){
						sb7.append(folioFactura).append(",");
						mensajeMapa = mensajeCausaDesconocida.replaceAll("<<FOLIOS_FACTURA>>", sb7.toString());
						mapaResultadoCorreo.put("CAUSA_DESCONOCIDA", mensajeMapa);
					}
				}
				
				if (!mapaResultadoCorreo.isEmpty()){
					if (mapaResultadoCorreo.get("SIN_MONEDA") != null){
						sbCorreo.append(mapaResultadoCorreo.get("SIN_MONEDA"));
					}
					if (mapaResultadoCorreo.get("TOTAL_ERROR") != null){
						sbCorreo.append(mapaResultadoCorreo.get("TOTAL_ERROR"));
					}
					if (mapaResultadoCorreo.get("ANEXO24") != null){
						sbCorreo.append(mapaResultadoCorreo.get("ANEXO24"));
					}
					
					if (mapaResultadoCorreo.get("ORDEN_CORRESPONDE") != null){
						sbCorreo.append(mapaResultadoCorreo.get("ORDEN_CORRESPONDE"));
					}
					
					if (mapaResultadoCorreo.get("NO_EXISTE") != null){
						sbCorreo.append(mapaResultadoCorreo.get("NO_EXISTE"));
					}
					if (mapaResultadoCorreo.get("YA_PROCESADA") != null){
						sbCorreo.append(mapaResultadoCorreo.get("YA_PROCESADA"));
					}
					if (mapaResultadoCorreo.get("CAUSA_DESCONOCIDA") != null){
						sbCorreo.append(mapaResultadoCorreo.get("CAUSA_DESCONOCIDA"));
					}
				}
				
			}catch(Exception e){
				Utils.imprimeLog("", e);
			}
			sb1 = null;
			sb2 = null;
			sb3 = null;
			//sb4 = null;
			sb5 = null;
			sb6 = null;
			sb7 = null;
			return sbCorreo.toString();
		}
		
   
   private String validarFechaRecepcion(String esquemaEmpresa, int idPerfil, Comprobante _comprobante, String SERIE_FINAL) {
		String mensajeFechas = "OK";
		CierreAnnioBean fechaCierre = new CierreAnnioBean();
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			LocalDateTime fechaFactLD =   _comprobante.getFecha(); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@"+ucFirst("fecha", bandXML));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String fechaFact = null;
			if(fechaFactLD != null) {
				fechaFact = fechaFactLD.format(formatter);
	    	}
			
			
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
			Date fecha = new Date();
			String fechaActualSimple = "";
			fechaActualSimple = formatDate.format(fecha);
			int anio = Integer.parseInt(fechaFact.substring(0, 4));
			logger.info("obtenerFechas anio---->"+anio);
			String [] fechasRecepcion = fechaCierre.obtenerFechas(con, rc.getEsquema(), anio, "F");
			logger.info("Fecha Factura---->"+fechaFact);
			logger.info("Fecha Apartir---->"+fechasRecepcion[1]);
			logger.info("Fecha Hasta---->"+fechasRecepcion[3]);
			logger.info("Fecha Actual ---->"+fechaActualSimple);

			
			
			long fechaHasta = 0;
			long fechaApartir = 0;
			long fechaFactura = 0;
			long fechaActual = 0;
			
			StringBuffer sbFecha = new StringBuffer();
			if (!fechasRecepcion[1].equals("")){
				fechaApartir = Long.parseLong(Utils.replaceCaracteresEspeciales(sbFecha.append(fechasRecepcion[1].substring(0,4))
						 .append(fechasRecepcion[1].substring(5,7))
						 .append(fechasRecepcion[1].substring(8,10)).toString()));
			}
			
			sbFecha.setLength(0);
			if (!fechasRecepcion[3].equals("")){
				fechaHasta = Long.parseLong(Utils.replaceCaracteresEspeciales(sbFecha.append(fechasRecepcion[3].substring(0,4))
						 .append(fechasRecepcion[3].substring(5,7))
						 .append(fechasRecepcion[3].substring(8,10)).toString()));
			}
			
			sbFecha.setLength(0);
			if (!fechaFact.equals("")){
				fechaFactura = Long.parseLong(Utils.replaceCaracteresEspeciales(sbFecha.append(fechaFact.substring(0,4))
						 .append(fechaFact.substring(5,7))
						 .append(fechaFact.substring(8,10)).toString()));
			}
			
			sbFecha.setLength(0);
			
			if (!fechaActualSimple.equals("")){
				fechaActual = Long.parseLong(fechaActualSimple);
			}

			logger.info("Fecha FacturaLong---->"+fechaFactura);
			logger.info("Fecha ApartirLong---->"+fechaApartir);
			logger.info("Fecha HastaLong---->"+fechaHasta);
			logger.info("Fecha ActualLong ---->"+fechaActual);

			
			boolean cumpleHasta = false;
			boolean cumpleApartir = false;
			boolean bandNoValidar = true;
			
			// fechaFactura = 20160103
			// fechaHasta   = 20161218
			
			// fechaApartir = 20160105
			// fechaActual  = 20160104
			
			// true, si cumplio no se manda mensaje
			// false, no cumplio no se manda mensaje
			
			if (fechaHasta > 0 && fechaHasta >= fechaFactura){
				if (fechaHasta >= fechaActual){
					cumpleHasta = true;
				}else{
					bandNoValidar = false;
				}
			}else{
				bandNoValidar = false;
			}
			
			if (bandNoValidar){ // significa que cumplio la fecha hasta y se valida las de apartir
				if (fechaApartir > 0 && fechaApartir <= fechaFactura){
					if (fechaApartir <= fechaActual){
						cumpleApartir = true;
					}
				}
			}
			
			logger.info("cumpleHasta(2) ---->"+cumpleHasta);
			logger.info("cumpleApartir ---->"+cumpleApartir);
			logger.info("cumpleHasta---->"+cumpleHasta);
			logger.info("fechaHasta---->"+fechaHasta);
			logger.info("cumpleApartir--->"+cumpleApartir);
			logger.info("fechaApartir--->"+fechaApartir);
			
			  String VALIDAR_CIERRE_ADMIN = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDAR_CIERRE_ADMIN");
			
			  if ("".equalsIgnoreCase(VALIDAR_CIERRE_ADMIN)) {
				  VALIDAR_CIERRE_ADMIN = "S";
			  }
			  
			  boolean bandValidaCierre = true;
			  if (idPerfil == 1 && "N".equalsIgnoreCase(VALIDAR_CIERRE_ADMIN)) {
				  bandValidaCierre = false;  
			  }
			  
			  String factura = SERIE_FINAL;			  
			  if (!cumpleHasta && fechaHasta > 0 && bandValidaCierre){
					mensajeFechas = fechasRecepcion[4].replace("<<factura>>", factura).replace("<<fecha_hasta>>", fechasRecepcion[6]);
					MENSAJE_VALIDACIONES = mensajeFechas;
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
			 }else if (!cumpleApartir && fechaApartir > 0 && bandValidaCierre){
					mensajeFechas = fechasRecepcion[2].replace("<<factura>>", factura).replace("<<fecha_apartir>>", fechasRecepcion[5]);
					MENSAJE_VALIDACIONES = mensajeFechas;
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
			 }
			  
			  
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			}catch(Exception e) {
				con = null;
			}
		}
		return mensajeFechas;
	}
   
   
   private boolean validaUUID(Connection con, String esquema, String uuid, String rfcProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean bandRegresa = false;
		try{
			logger.info("************ INICIANDO VALIDACION DEL CODIGO FISCAL PARA ORDEN ******************"+uuid);
			stmt = con.prepareStatement(ValidacionesQuerys.getValidaUUID(esquema));
			stmt.setString(1, uuid);
			rs = stmt.executeQuery();
			if (rs.next()){
				bandRegresa = true;
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				
			}catch(Exception e){
				stmt = null;
			}
		}
		return bandRegresa;
	}
   
   
   public  boolean validaSerieDUPLICADA(Connection con, String esquema, ProveedoresForm provForm, String serieFolio){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean bandRegresa = false;
		try{
			logger.info("************ INICIANDO VALIDACION DE SERIE DUPLICADA ******************");
			if ("".equals(serieFolio)) {
				// JSONObject jsonProv = provBean.infoProveedores(con, esquema, claveProveedor);
				if ("MEX".equalsIgnoreCase(provForm.getTipoProveedor()) ) {
					logger.info("************ NO SE VALIDA LA SERIA DUPLICADA POR SER UN PROVEEDOR MEX ******************");
					return false;
				}
			}
			
			stmt = con.prepareStatement(ValidacionesQuerys.getValidaSERIE(esquema));
			stmt.setInt(1, provForm.getClaveRegistro());
			stmt.setString(2, serieFolio);
			rs = stmt.executeQuery();
			if (rs.next()){
				bandRegresa = true;
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				
			}catch(Exception e){
				stmt = null;
			}
		}
		return bandRegresa;
	}
   
   
   
   private VisorOrdenesForm obtenerValoresXML(String esquemaEmpresa, File fileXML) {
		VisorOrdenesForm visorDatosForm = new VisorOrdenesForm();
		
		Comprobante _comprobante = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		try {
				connPool = new ConexionDB();
				rc = connPool.getConnection(esquemaEmpresa);
				con = rc.getCon();
			
			
				_comprobante = LeerXML.ObtenerComprobante(fileXML.getAbsolutePath());
				String rfcFacturaXML =  _comprobante.getEmisor().getRfc();
				String rfcReceptorXML =  _comprobante.getReceptor().getRfc();
				double subTotalXML = _comprobante.getSubTotal();
				double totalXML =  _comprobante.getTotal();
				String uuidXML =   _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
				String serieXML =  _comprobante.getSerie(); 
				double ivaXML = 0;
				String folioXML =   _comprobante.getFolio();
				String usoCFDI_XML = _comprobante.getReceptor().getUsoCFDI();
				LocalDateTime fechaFactLD =   _comprobante.getFecha();
				LocalDateTime fechaUUIDXMLLD = _comprobante.getFecha();
				List<Retencion> listRetencion = null;
				
				String fechaFact = null;
				String fechaUUIDXML = null;
				String claveProdServ_XML = "";
				
				if(_comprobante.getImpuestos() != null) {
					ivaXML = _comprobante.getImpuestos().getTotalImpuestosTrasladados();
				}

				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				if(fechaFactLD != null) {
					fechaFact = fechaFactLD.format(formatter);
		    	}
				if(fechaUUIDXMLLD != null) {
					fechaUUIDXML = fechaUUIDXMLLD.format(formatter);
		    	}
				
				try {
					Conceptos concepto = _comprobante.getConceptos();
					claveProdServ_XML =  concepto.getConcepto().get(0).getClaveProdServ();	
				}catch(Exception e) {
					claveProdServ_XML =  "";
				}
				
				logger.info("ivaXML---->"+ivaXML);
				
				String datosSAT [] = UtilsSAT.validaSAT(rfcFacturaXML, rfcReceptorXML, totalXML, uuidXML);
				logger.info("serieXML---->"+serieXML);
				logger.info("totalXML---->"+totalXML);
				logger.info("subTotalXML---->"+subTotalXML);
				logger.info("ivaXML---->"+ivaXML);
				logger.info("uuidXML---->"+uuidXML);
				logger.info("rfcReceptorXML---->"+rfcReceptorXML);
				logger.info("folioXML---->"+folioXML);
				logger.info("Fecha Factura---->"+fechaFact);
				logger.info("datosSAT----->"+datosSAT);
				logger.info("usoCFDI_XML---->"+usoCFDI_XML);
				
				
				String ivaRet = "";
				String isrRet = "";
				if(_comprobante.getImpuestos() != null) {
					listRetencion = _comprobante.getImpuestos().getRetenciones();
				}
				HashMap<String, String> mapaISR = UtilsValidaciones.getImporteImpuestoISRNuevo(listRetencion);
				if (!mapaISR.isEmpty()){
					ivaRet = Utils.noNulo(mapaISR.get("IVA"));
					isrRet = Utils.noNulo(mapaISR.get("ISR"));
				}
				
				
				String estado  = null; 
				String estatus = null;
				if (datosSAT.length > 1) {
					estado = datosSAT[0];
					estatus = datosSAT[1];
				}else {
					estado = "";
					estatus = "";
				}

				double tipoCambio = 0;
				try{
					tipoCambio = _comprobante.getTipoCambio();		
				}catch(Exception e){
					tipoCambio = 0;
				}
				
				
					String monedaXML = "SIN_MONEDA";
					visorDatosForm.setSerieFolio(serieXML);
					visorDatosForm.setTotal(String.valueOf(totalXML));
					visorDatosForm.setSubTotal(String.valueOf(subTotalXML));
					visorDatosForm.setIva(String.valueOf(ivaXML));
					visorDatosForm.setIvaRet("0");
					visorDatosForm.setIsrRet("0");
					visorDatosForm.setImpLocales("0");
					visorDatosForm.setUuid(uuidXML);
					visorDatosForm.setFolioXML(String.valueOf(folioXML));
					visorDatosForm.setEstadoCFDI(estado);
					visorDatosForm.setEstatusCFDI(estatus);
					visorDatosForm.setTipoMoneda(monedaXML);
					visorDatosForm.setTipoCambio(String.valueOf(tipoCambio));
					visorDatosForm.setFechaFactura(fechaFact.substring(0, 10));

					logger.info("Fecha de Pago....."+visorDatosForm.getFechaPago());
					visorDatosForm.setUsoCFDI(usoCFDI_XML);
					visorDatosForm.setClaveProducto(claveProdServ_XML);
					
					
					if (fechaUUIDXML.length() > 10){
						visorDatosForm.setFechaUUID(fechaUUIDXML.substring(0, 10));	
					}else{
						visorDatosForm.setFechaUUID(fechaUUIDXML);
					}
					
					try{
						visorDatosForm.setIvaRet(ivaRet);
					}catch(Exception e){
						visorDatosForm.setIvaRet("0");
					}
					
					try{
						visorDatosForm.setIsrRet(isrRet);
					}catch(Exception e){
						visorDatosForm.setIsrRet("0");
					}
				
					// se obtiene bandera de tipo de validacion
					// Termina
					  
					// se obtiene bandera de tipo de validacion
					  
					  String SERIE_FALTANTE = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "SERIE_FALTANTE");
					  logger.info("SERIE_FALTANTE---->"+SERIE_FALTANTE);
					  
					  if ("".equalsIgnoreCase(SERIE_FALTANTE)) {
						  SERIE_FALTANTE = "A";
					  }
					  
					  
					if ("".equals(serieXML)){
						visorDatosForm.setSerieFinal(SERIE_FALTANTE.concat(folioXML));
					}else{
						visorDatosForm.setSerieFinal(serieXML.concat(folioXML));
					}
					
					HashMap<String, String> mapaConf = ConfigAdicionalesBean.obtenerConfiguraciones(con, rc.getEsquema());
					
					visorDatosForm.setFechaPago(UtilsValidaciones.obtenerFechaPago(mapaConf)); // Se calcula la fecha de Pago
					
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (con != null){
					con.close();
				}
				con = null;
								
			}catch(Exception e){
				con = null;
			}
		}
		return visorDatosForm;
	}

   
   
   public  boolean algoritoTotal(double totalCompararXMLFinal, double totalComparar, String limiteTolerancia){
		boolean bandTotal = false;
		try{
			double totTolerancia = 0;
	    	boolean bandPrimera = false;
	    	boolean bandSegunda = false;
	    	boolean bandTercera = false;
			try{
				totTolerancia = Double.parseDouble(limiteTolerancia);
			}catch(Exception e){
				totTolerancia = 0;
			}
			totalCompararXMLFinal = (totalCompararXMLFinal + totTolerancia);
			logger.info("totalCompararXMLFinal------>"+totalCompararXMLFinal);
			logger.info("totalComparar------>"+totalComparar);
			if (totalCompararXMLFinal >= totalComparar ){
				bandPrimera = true;
			}
			
			if (totalComparar <= totalCompararXMLFinal){
				bandSegunda = true;
			}
			totalCompararXMLFinal = (totalCompararXMLFinal - totTolerancia);
			logger.info("totalCompararXMLFinal (2)------>"+totalCompararXMLFinal);
			if (totalCompararXMLFinal <= totalComparar ){
				bandTercera = true;
			}else if (totalCompararXMLFinal <= (totalComparar +totTolerancia)){
				bandTercera = true;
			}
			
			if (totalComparar >= totalCompararXMLFinal){
				bandTercera = true;
			}else if ((totalComparar + totTolerancia) >= totalCompararXMLFinal){
				bandTercera = true;
			}
			logger.info("bandPrimera------>"+bandPrimera);
			logger.info("bandSegunda------>"+bandSegunda);
			logger.info("bandTercera------>"+bandTercera);
			
			if (bandPrimera && bandSegunda && bandTercera){
				bandTotal = true;
			}
			
		}catch(Exception e){
			Utils.imprimeLog("Validando los totales", e);
		}
		
		return bandTotal;
	}
   
   
   
   public boolean esOrdenDelProveedor(Connection con, String esquema, long folioEmpresa, int claveProveedor)
   {
       PreparedStatement stmt = null;
       ResultSet rs = null;
       boolean isOrdenProveedor = false;
       try{
           stmt = con.prepareStatement(VisorOrdenesQuerys.getEsOrdenProveedor(esquema));
           stmt.setLong(1, folioEmpresa);
           stmt.setInt(2, claveProveedor);
           rs = stmt.executeQuery();
           if (rs.next()) {
           	isOrdenProveedor = true;
           }
           
       }
       catch(Exception e){
           Utils.imprimeLog("", e);
       }finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
       }
       return isOrdenProveedor;
   }
   
   
   public void actualizarOrdenes(String esquemaEmpresa,  VisorOrdenesForm visorXMLForm, ProveedoresForm provForm, HashMap<Long, String> mapaEstatus, ArrayList<String> arreloOrd, 
		   String usuarioAdjunto, String idMultiple, File fileXML, File filePDF, String estatusCFDI) {
	   ResultadoConexion rc = null;
	   ConexionDB connPool = null;
	   Connection con = null;
	   
	   String cadenaTXT [] = null;
	   boolean bandArchivos = true;
	   try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			StringBuffer sbMensajeCorreo = new StringBuffer();
			String rfcProveedor = provForm.getRfc();
			String rutaRepositorio = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquemaEmpresa + File.separator + "PROVEEDORES" + File.separator + provForm.getClaveRegistro() + File.separator;
			String nombreFinalXML = null;
			String nombreFinalPDF = null;
			
			//ConfigAdicionalesBean confAdicionales = new ConfigAdicionalesBean();
			HashMap<String, String> mapaConfig = ConfigAdicionalesBean.obtenerConfiguraciones(con, rc.getEsquema());

			
			for (int x = 0; x < arreloOrd.size(); x++){
				cadenaTXT = arreloOrd.get(x).split(";");
				long folioFactura = Long.parseLong(cadenaTXT[0]);
				visorXMLForm.setEstatusOrden(mapaEstatus.get(folioFactura));
				sbMensajeCorreo.append(folioFactura).append(",");
				if (bandArchivos){
					logger.info("Serie al actualizar....."+visorXMLForm.getSerieFolio());
					logger.info("Folio al actualizar....."+visorXMLForm.getFolioXML());
					if (visorXMLForm.getSerieFolio().equals("")){
						if (visorXMLForm.getFolioXML().equals("")  || visorXMLForm.getFolioXML().equals("0")){
							visorXMLForm.setNombreXML("&="+rfcProveedor+ visorXMLForm.getUuid()+".xml");
							visorXMLForm.setNombrePDF("&="+rfcProveedor+ visorXMLForm.getUuid()+".pdf");
							//rutaRepositorio + rfcProveedor + "-" + folioGenerado+".xml";
							nombreFinalXML = rutaRepositorio +  rfcProveedor+ visorXMLForm.getUuid()+".xml";
							nombreFinalPDF = rutaRepositorio +  rfcProveedor+ visorXMLForm.getUuid()+".pdf";
						}else{
							// se obtiene bandera de tipo de validacion
							  logger.info("SERIE_FALTANTE---->"+mapaConfig.get("SERIE_FALTANTE"));
							  String SERIE_FALTANTE = mapaConfig.get("SERIE_FALTANTE");
							  if ("".equalsIgnoreCase(SERIE_FALTANTE)) {
								  SERIE_FALTANTE = "A";
							  }
							// Termina

							
							  visorXMLForm.setNombreXML("&="+rfcProveedor+"-"+SERIE_FALTANTE+"-" + visorXMLForm.getFolioXML()+".xml");
							  visorXMLForm.setNombrePDF("&="+rfcProveedor+"-"+SERIE_FALTANTE+"-" + visorXMLForm.getFolioXML()+".pdf");
							//rutaRepositorio + rfcProveedor + "-" + folioGenerado+".xml";
							nombreFinalXML = rutaRepositorio +  rfcProveedor+"-"+SERIE_FALTANTE+"-" + visorXMLForm.getFolioXML()+".xml";
							nombreFinalPDF = rutaRepositorio +  rfcProveedor+"-"+SERIE_FALTANTE+"-" + visorXMLForm.getFolioXML()+".pdf";
						}
					}else{
						visorXMLForm.setNombreXML("&="+rfcProveedor+"-"+visorXMLForm.getSerieFolio()+"-" + visorXMLForm.getFolioXML()+".xml");
						visorXMLForm.setNombrePDF("&="+rfcProveedor+"-"+visorXMLForm.getSerieFolio()+"-" + visorXMLForm.getFolioXML()+".pdf");
						nombreFinalXML = rutaRepositorio +  rfcProveedor+"-"+visorXMLForm.getSerieFolio()+"-" + visorXMLForm.getFolioXML()+".xml";
						nombreFinalPDF = rutaRepositorio +  rfcProveedor+"-"+visorXMLForm.getSerieFolio()+"-" + visorXMLForm.getFolioXML()+".pdf";
					}
					
					logger.info("Nombre del XML Final : "+nombreFinalXML);
					logger.info("Nombre del PDF Final : "+nombreFinalPDF);
					logger.info("Ruta a depositar : "+rutaRepositorio);
					
				}
				
				int totReg = actualizaOrden(con, esquemaEmpresa, folioFactura, visorXMLForm, usuarioAdjunto, idMultiple, estatusCFDI);
				logger.info("La actualizacion fue : "+totReg );
				if (totReg == 1){
					if (bandArchivos){
						logger.info("nombreFinalXML :"+nombreFinalXML);
						logger.info("nombreFinalPDF :"+nombreFinalPDF);
						File fileDestXML = new File(nombreFinalXML);
						File fileDestPDF = new File(nombreFinalPDF);
						
						UtilsFile.moveFileDirectory(fileXML,fileDestXML, true, false, true);
						UtilsFile.moveFileDirectory(filePDF,fileDestPDF, true, false, true);
						bandArchivos = false;
					}
				}
				
				
			}
			
			
			
			
	   }catch(Exception e) {
		   Utils.imprimeLog("", e);
	   }
   }
   
   
   private int actualizaOrden(Connection con, String esquema, long folioEmpresa, VisorOrdenesForm visorForm, String usuarioHTTP, String idMultiple, String estatusCFDI){
		PreparedStatement stmt = null;
		int cantidadFactura = 0;
		try{
			stmt = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrdenFolioEmpresa(esquema));
			String serRecibido = "0";
			if ("A2".equalsIgnoreCase(visorForm.getEstatusOrden())){
				if ("A10".equalsIgnoreCase(estatusCFDI)) {
					stmt.setString(1, "A10");
					visorForm.setFechaPago(null);
				}else {
					stmt.setString(1, "A3");
				}
				//stmt.setString(1, "A3");	
				serRecibido = "1";
			}else{
				if ("A10".equalsIgnoreCase(estatusCFDI)) {
					stmt.setString(1, "A10");
				}else {
					stmt.setString(1, "A1");	
				}
				
				//stmt.setString(1, "A1");
				serRecibido = "0";
			}
			
			
			stmt.setString(2, visorForm.getSerieFinal());
			stmt.setDouble(3, Utils.noNuloDouble(visorForm.getTotal()));
			stmt.setDouble(4, Utils.noNuloDouble(visorForm.getSubTotal()));
			stmt.setDouble(5, Utils.noNuloDouble(visorForm.getIva()));
			stmt.setDouble(6, Utils.noNuloDouble(visorForm.getIvaRet()));
			stmt.setDouble(7, Utils.noNuloDouble(visorForm.getIsrRet()));
			stmt.setDouble(8, Utils.noNuloDouble(visorForm.getImpLocales()));
			stmt.setString(9, visorForm.getNombreXML());
			stmt.setString(10, visorForm.getNombrePDF());
			stmt.setString(11, serRecibido);
			stmt.setString(12, visorForm.getUuid());
			stmt.setString(13, visorForm.getFechaUUID());
			stmt.setString(14, usuarioHTTP);
			stmt.setString(15, idMultiple); // Multiple
			stmt.setString(16, visorForm.getEstadoCFDI());
			stmt.setString(17, visorForm.getEstatusCFDI());
			stmt.setString(18, visorForm.getUsoCFDI());
			stmt.setString(19, visorForm.getClaveProducto());
			stmt.setString(20, visorForm.getFechaPago());
			stmt.setString(21, visorForm.getFechaFactura());
			stmt.setLong(22, folioEmpresa);
			
			stmt.executeUpdate();
			cantidadFactura = 1;
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				stmt = null;
			}
		}
		return cantidadFactura;
	}
   
}
