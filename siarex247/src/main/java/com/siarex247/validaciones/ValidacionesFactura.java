package com.siarex247.validaciones;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.siarex247.timbrado.TimbradoBean;
import com.siarex247.timbrado.TimbradoExpressForm;
import com.siarex247.timbrado.TimbradoForm;
import com.siarex247.utils.MensajesSIAREX;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesBean;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;

public class ValidacionesFactura {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	public static MensajesSIAREX mensajeSIAREX = null;
	public static String MENSAJE_VALIDACIONES = null;
	public static String SUBJECT_VALIDACIONES = null;
	
	public String [] iniciarProceso(String esquemaEmpresa, long folioEmpresa, File fileXML, File filePDF, String idLenguaje, int idPerfil, String usuarioAdjunto) {
		
		String respuestaValidacion [] = {"", "", ""}; // MENSAJE, SUBJECT Y RESULTADO VALIDACION (ERROR o EXITO)
		AccesoBean accesoBean = new AccesoBean();
		try {
			mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			VisorOrdenesForm visorForm = existeOrden(esquemaEmpresa, folioEmpresa);
			ProveedoresForm provForm = obtenerProveedor(esquemaEmpresa, visorForm.getClaveProveedor());
			EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquemaEmpresa);
			
			VisorOrdenesForm datosOrdenes = obtenerValoresXML(esquemaEmpresa, fileXML, provForm);
			if (visorForm.getFolioOrden() == 0) { // existe la orden de compra
				respuestaValidacion[0] = Utils.regresaCaracteresNormales(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE6, "<< FOLIO_FACTURA >>", String.valueOf(folioEmpresa)));
				respuestaValidacion[1] = "Siarex - " +Utils.regresaCaracteresNormales(mensajeSIAREX.SUBJECT2);
				respuestaValidacion[2] = "ERROR";
			}else if (provForm.getClaveRegistro() == 0) {  // existe el proveedor en base de datos
				respuestaValidacion[0] = "El Proveedor no existe en nuestra base de datos.";
				respuestaValidacion[1] = "Siarex - Proveedor No existe";
				respuestaValidacion[2] = "ERROR";
			}else if (!"OK".equalsIgnoreCase(validarCertificadosProveedor(esquemaEmpresa, folioEmpresa, provForm))) {  // se validan los certificados
				respuestaValidacion[0] = Utils.regresaCaracteresNormales(MENSAJE_VALIDACIONES);
				respuestaValidacion[1] = "Siarex - " + SUBJECT_VALIDACIONES;
				respuestaValidacion[2] = "ERROR";
			}else if (!"OK".equalsIgnoreCase(validarEstatusOrden(esquemaEmpresa, visorForm, empresaForm))) {  // se validan los estatus de la orden de compra
				respuestaValidacion[0] = Utils.regresaCaracteresNormales(MENSAJE_VALIDACIONES);
				respuestaValidacion[1] = "Siarex - " + SUBJECT_VALIDACIONES;
				respuestaValidacion[2] = "ERROR";
			}else if (!"OK".equalsIgnoreCase(iniciarValidacionesXML(esquemaEmpresa, fileXML, filePDF, visorForm, provForm, idPerfil, empresaForm))) {
				respuestaValidacion[0] = Utils.regresaCaracteresNormales(MENSAJE_VALIDACIONES);
				respuestaValidacion[1] = "Siarex - " + SUBJECT_VALIDACIONES;
				respuestaValidacion[2] = "ERROR";
			}else if (!validarXML(esquemaEmpresa, datosOrdenes, folioEmpresa,  fileXML.getAbsolutePath(), visorForm, usuarioAdjunto)) {
				
				String MENSAJE_VALIDACION_XML = obtenerValidacionXML(esquemaEmpresa, datosOrdenes, folioEmpresa, provForm);
				// logger.info("MENSAJE_VALIDACION_XML===>"+MENSAJE_VALIDACION_XML);
				// respuestaValidacion[0]  = Utils.regresaCaracteresNormales(Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE27, "<< RAZON_EMISOR >>", provForm.getRazonSocial()), "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())));
				respuestaValidacion[0]  = MENSAJE_VALIDACION_XML;
				respuestaValidacion[1] = "Siarex - " + mensajeSIAREX.SUBJECT2;
				respuestaValidacion[2] = "ERROR";
			}else {
				
				// VisorOrdenesForm datosOrdenes = obtenerValoresXML(esquemaEmpresa, fileXML, visorForm, provForm);
				// logger.info("Estatus==>"+visorForm.getDesEstatus());
				String estatusCFDI = "";
				 if ("false".equalsIgnoreCase(visorForm.getDesEstatus())) {
					respuestaValidacion[0]  = Utils.regresaCaracteresNormales(Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE27, "<< RAZON_EMISOR >>", provForm.getRazonSocial()), "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())));
					respuestaValidacion[1] = "Siarex - " + mensajeSIAREX.SUBJECT2;
					respuestaValidacion[2] = "ERROR";
					
				}else {
					boolean bandCartaPorte = true;
					String mensajeCartaPorte = validarCartaPorte(esquemaEmpresa, visorForm.getFolioOrden(), folioEmpresa, provForm, fileXML, filePDF, usuarioAdjunto, idLenguaje);
					if ("OK".equalsIgnoreCase(mensajeCartaPorte)) {
						// logger.info("Validacion de carta porte satisfactoria.....");
						bandCartaPorte = true;
					}else if ("A12".equalsIgnoreCase(mensajeCartaPorte)) {
						estatusCFDI = "A12";
						bandCartaPorte = true;
					}else {
						respuestaValidacion[0]  = Utils.regresaCaracteresNormales(mensajeCartaPorte); // Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE27, "<< RAZON_EMISOR >>", provForm.getRazonSocial()), "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
						respuestaValidacion[1] = "Siarex - " + mensajeSIAREX.SUBJECT2;
						respuestaValidacion[2] = "ERROR";
						bandCartaPorte = false;
					}
					
					// logger.info("bandCartaPorte....."+bandCartaPorte);
					if (bandCartaPorte) { // si cumplio con la carta porte
						if ("A10".equalsIgnoreCase(MENSAJE_VALIDACIONES)) {
							respuestaValidacion[0]  = Utils.regresaCaracteresNormales(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE16, "<< FOLIO_FACTURA >>", String.valueOf(folioEmpresa)));
							respuestaValidacion[1] = "Siarex - " +  "Factura En Proceso de Validacion";
							respuestaValidacion[2] = "EXITO";
							estatusCFDI = "A10";
						}else if ("A12".equalsIgnoreCase(MENSAJE_VALIDACIONES)) {
							respuestaValidacion[0]  = Utils.regresaCaracteresNormales(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE77, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())));
							respuestaValidacion[1] = "Siarex - " +  "Factura En Proceso de Validacion";
							respuestaValidacion[2] = "EXITO";
						}else {
							respuestaValidacion[0]  = Utils.regresaCaracteresNormales(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE17, "<< FOLIO_FACTURA >>", String.valueOf(folioEmpresa)));
							respuestaValidacion[1] = "Siarex - " +  mensajeSIAREX.SUBJECT1;
							respuestaValidacion[2] = "EXITO";
						}
						
						String estatusFactura = "A11";
						if ("0".equals(empresaForm.getTipoAcceso())){
							estatusFactura = visorForm.getEstatusOrden();
						}
						
						long folioGenerado = 0;
						long folioRFCEmisor = 0;
						//long folioFactura = 0;
						
						if ("1".equals(empresaForm.getTipoAcceso())){ // es por factura la generacion
							datosOrdenes.setEstatusOrden("A1");
							double montoFactura = 0;
							if ("0".equalsIgnoreCase(provForm.getTipoConfirmacion())){
								montoFactura = Double.parseDouble(datosOrdenes.getSubTotal());
							}else{
								montoFactura = Double.parseDouble(datosOrdenes.getTotal());
							}
							folioGenerado = generaOrden(esquemaEmpresa, 0, provForm.getRfc(), montoFactura);
							//folioFactura = folioGenerado;
							
							try{
								folioRFCEmisor = Long.parseLong(datosOrdenes.getFolioXML());
							}catch(Exception e){
								folioRFCEmisor = visorForm.getFolioEmpresa();
							}
						}else{
							datosOrdenes.setEstatusOrden(estatusFactura);
							folioGenerado =  visorForm.getFolioOrden();
							folioRFCEmisor = visorForm.getFolioEmpresa();
						}
						
						String nombreFinalXML = "";
						String nombreFinalPDF = "";
						
						String rutaRepositorio = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquemaEmpresa + File.separator + "PROVEEDORES" + File.separator + provForm.getClaveRegistro() + File.separator;
						
						if (datosOrdenes.getSerieFolio().equals("")){
							if (datosOrdenes.getFolioXML().equals("") || datosOrdenes.getFolioXML().equals("0")){
								datosOrdenes.setNombreXML("&="+provForm.getRfc()+ datosOrdenes.getUuid()+".xml");
								datosOrdenes.setNombrePDF("&="+provForm.getRfc()+ datosOrdenes.getUuid()+".pdf");
								//rutaRepositorio + provForm.getRfc() + "-" + folioGenerado+".xml";
								nombreFinalXML = rutaRepositorio +  provForm.getRfc()+ datosOrdenes.getUuid()+".xml";
								nombreFinalPDF = rutaRepositorio +  provForm.getRfc()+ datosOrdenes.getUuid()+".pdf";
							}else{
								  //  se obtiene bandera de tipo de validacion
								  String SERIE_FALTANTE = ConfigAdicionalesBean.obtenerValorVariable(esquemaEmpresa, "SERIE_FALTANTE");
								  if ("".equalsIgnoreCase(SERIE_FALTANTE)) {
									  SERIE_FALTANTE = "A";
								  }
								// Termina
								datosOrdenes.setNombreXML("&="+provForm.getRfc()+"-"+SERIE_FALTANTE+"-" + datosOrdenes.getFolioXML()+".xml");
								datosOrdenes.setNombrePDF("&="+provForm.getRfc()+"-"+SERIE_FALTANTE+"-" + datosOrdenes.getFolioXML()+".pdf");
								//rutaRepositorio + provForm.getRfc() + "-" + folioGenerado+".xml";
								nombreFinalXML = rutaRepositorio +  provForm.getRfc()+"-"+SERIE_FALTANTE+"-" + datosOrdenes.getFolioXML()+".xml";
								nombreFinalPDF = rutaRepositorio +  provForm.getRfc()+"-"+SERIE_FALTANTE+"-" + datosOrdenes.getFolioXML()+".pdf";
							}
						}else{
							datosOrdenes.setNombreXML("&="+provForm.getRfc()+"-"+datosOrdenes.getSerieFolio()+"-" + datosOrdenes.getFolioXML()+".xml");
							datosOrdenes.setNombrePDF("&="+provForm.getRfc()+"-"+datosOrdenes.getSerieFolio()+"-" + datosOrdenes.getFolioXML()+".pdf");
							nombreFinalXML = rutaRepositorio +  provForm.getRfc()+"-"+datosOrdenes.getSerieFolio()+"-" + datosOrdenes.getFolioXML()+".xml";
							nombreFinalPDF = rutaRepositorio +  provForm.getRfc()+"-"+datosOrdenes.getSerieFolio()+"-" + datosOrdenes.getFolioXML()+".pdf";
						}
						
						int totReg = actualizaOrden(esquemaEmpresa, folioGenerado, folioRFCEmisor, datosOrdenes, usuarioAdjunto, estatusCFDI);
						
						if (totReg == 1){
							
							File fileDestXML = new File(nombreFinalXML);
							File fileDestPDF = new File(nombreFinalPDF);
							
							UtilsFile.moveFileDirectory(fileXML,fileDestXML, true, false, true);
							UtilsFile.moveFileDirectory(filePDF,fileDestPDF, true, false, true);
							
							if (filePDF.exists()) {
								filePDF.delete();
							}
							
							if (fileXML.exists()) {
								fileXML.delete();
							}
						}
						
					}
				}
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return respuestaValidacion;
	}
	
	
	
	
	private VisorOrdenesForm existeOrden(String esquemaEmpresa, long folioEmpresa) {
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		VisorOrdenesBean visorBean = new VisorOrdenesBean();
		VisorOrdenesForm visorForm = null;
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			visorForm = visorBean.consultarOrdenXfolioEmpresa(con, rc.getEsquema(), folioEmpresa);
			
			
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
		return visorForm;
	}
	
	
	private ProveedoresForm obtenerProveedor(String esquemaEmpresa, int claveProveedor) {
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		ProveedoresBean provBean = new ProveedoresBean();
		ProveedoresForm provForm = null;
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			provForm = provBean.consultarProveedor(con, rc.getEsquema(), claveProveedor);
			
			
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
	
	
	private String validarCertificadosProveedor(String esquemaEmpresa, long folioEmpresa, ProveedoresForm provForm) {
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		String mensajeCertificacion = "OK";
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			String BLOQUEAR_IMSS = ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "BLOQUEAR_IMSS");
			String BLOQUEAR_SAT = ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "BLOQUEAR_SAT");

			
		    if ("S".equalsIgnoreCase(BLOQUEAR_IMSS) && "S".equalsIgnoreCase(provForm.getBandIMSS())) {
				if ("S".equalsIgnoreCase(provForm.getConfirmarIMSS())) {
					mensajeCertificacion = "OK";
				}else {
					mensajeCertificacion = "ERROR"; 
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE63, "<< FOLIO_FACTURA >>", String.valueOf(folioEmpresa));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT3;
				}
			}
		    
		    if ("OK".equalsIgnoreCase(mensajeCertificacion)) {
				if ("S".equalsIgnoreCase(BLOQUEAR_SAT) && "S".equalsIgnoreCase(provForm.getBandSAT())) {
					if ("S".equalsIgnoreCase(provForm.getConfirmarSAT())) {
						mensajeCertificacion = "OK";
					}else {
						mensajeCertificacion = "ERROR"; 
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE62, "<< FOLIO_FACTURA >>", String.valueOf(folioEmpresa));
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
	
	
	private String validarEstatusOrden(String esquemaEmpresa, VisorOrdenesForm visorForm, EmpresasForm empresaForm) {
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		String mensajeEstatus = "OK";
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			String estatusFactura = "A11";
			if ("0".equals(empresaForm.getTipoAcceso())){
				estatusFactura = visorForm.getEstatusOrden();
			}
			
			if ("A5".equalsIgnoreCase(estatusFactura) 
					|| "A2".equalsIgnoreCase(estatusFactura) 
					   || "A11".equalsIgnoreCase(estatusFactura)
			   ){
				
				mensajeEstatus = "OK";
				
			}else if ("A3".equalsIgnoreCase(estatusFactura) 
				    || "A4".equalsIgnoreCase(estatusFactura)
				      || "A6".equalsIgnoreCase(estatusFactura)
				         || "A10".equalsIgnoreCase(estatusFactura) 
				       	    || "A1".equalsIgnoreCase(estatusFactura)){
				
				if ("".equals(visorForm.getSerieFolio())) {
					mensajeEstatus  = Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE41, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())), "<< SERIE_FOLIO >>", visorForm.getUuid());
					MENSAJE_VALIDACIONES = mensajeEstatus; 
				}else {
					mensajeEstatus  = Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE19, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())), "<< SERIE_FOLIO >>", visorForm.getSerieFolio());
					MENSAJE_VALIDACIONES = mensajeEstatus;
				}
				
				SUBJECT_VALIDACIONES =  Utils.regresaCaracteresNormales(mensajeSIAREX.SUBJECT2);
				
			}else {
				mensajeEstatus  = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE20, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
				MENSAJE_VALIDACIONES = mensajeEstatus;
				SUBJECT_VALIDACIONES = Utils.regresaCaracteresNormales(mensajeSIAREX.SUBJECT2);
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
		return mensajeEstatus;
	}
	
	private String iniciarValidacionesXML(String esquemaEmpresa, File fileXML, File filePDF, VisorOrdenesForm visorForm, ProveedoresForm provForm, int idPerfil, EmpresasForm empresaForm) {
		String mensajeValidacionesXML = "OK";
		Comprobante _comprobante = null;
		double totalCompararXMLFinal = 0;
		double totalComparar = 0;
		double totalCompararXML = 0;
		
		double cantidadFacturada = 0;
		double subTotal = 0;
		
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		boolean bandXML = true;
		try {
			// logger.info("*************** VALIDANDO XML ******************");
			
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			_comprobante = LeerXML.ObtenerComprobante(fileXML.getAbsolutePath());
			if (_comprobante.getVersion().equals("3.3")) {
				// bandXML = true;
			}
			
			if ("A2".equalsIgnoreCase(visorForm.getEstatusOrden())){
				if ("0".equals(provForm.getTipoConfirmacion())){ // es contra el sub-total
					subTotal = Double.parseDouble( visorForm.getMonto() ); 
				}else{ // es contra el total
					cantidadFacturada = Double.parseDouble( visorForm.getTotal() );
				}
			}else{
				if ("0".equals(provForm.getTipoConfirmacion())){ // es contra el sub-total
					subTotal = Double.parseDouble( visorForm.getMonto() );
				}else{ // es contra el total
					cantidadFacturada = Double.parseDouble( visorForm.getMonto() );
				}
			}
			
			if ("0".equals(provForm.getTipoConfirmacion())){ // es contra el sub-total
				totalComparar = subTotal;
				totalCompararXML = _comprobante.getSubTotal();
			}else{ // es contra el total
				totalComparar = cantidadFacturada;
				totalCompararXML = _comprobante.getTotal();
			}
			
//			String rfcFacturaXML =  _comprobante.getEmisor().getRfc();
			
			double descuento    = _comprobante.getDescuento();
			LocalDateTime fechaUUIDXMLLD =  _comprobante.getFecha();
//			String rfcReceptorXML =  _comprobante.getReceptor().getRfc();
			String folioXML =   _comprobante.getFolio();
			String serieXML =  _comprobante.getSerie();
			
			String usoCFDI_XML =  _comprobante.getReceptor().getUsoCFDI();
			String uuidXML =   _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
//			String nombreEmisor =  _comprobante.getEmisor().getNombre();
//			String nombreReceptor =  _comprobante.getReceptor().getNombre();
			String regimenFiscalEmisor =  _comprobante.getEmisor().getRegimenFiscal();
/*			
			int domicilioFiscalEmisor =  0;
			try {
				domicilioFiscalEmisor = Integer.parseInt(_comprobante.getLugarExpedicion());
			}catch(Exception e) {
				domicilioFiscalEmisor = 0;
			}
*/			
			/*
			List<Retencion> listRetencion = null;
			
			if(_comprobante.getImpuestos() != null) {
				listRetencion = _comprobante.getImpuestos().getRetenciones();
			}
			*/
			// HashMap<String, String> mapaISR = UtilsValidaciones.getImporteImpuestoISRNuevo(listRetencion);
			JSONArray jsonConceptos = UtilsValidaciones.getConceptosNuevo(_comprobante);
			 
			String claveProdServ_XML = "";
			
			int domicilioFiscalReceptor = 0;
			try {
				domicilioFiscalReceptor =  Utils.noNuloINT(_comprobante.getReceptor().getDomicilioFiscalReceptor());	
			}catch(Exception e) {
				domicilioFiscalReceptor = 0;
				Utils.imprimeLog("", e);
			}
			
			// JSONObject jsonobj = new ProveedoresBean().infoProveedores2(con, esquema, claveProveedor);
//			int codigoPostalEmisor = provForm.getCodigoPostal();
//			String razonSocial = provForm.getRazonSocial();
			String regimenFiscal = provForm.getRegimenFiscal();
			
			RegimenFiscalBean regimenBean = new RegimenFiscalBean();
			JSONObject jsonRegimenFiscal = regimenBean.buscarConfigurados(con, rc.getEsquema(), provForm.getClaveRegistro());
			
			boolean bandRegimenFiscal = false;
			if(jsonRegimenFiscal == null || jsonRegimenFiscal.size() <= 0) {
				bandRegimenFiscal = false;
			} else {
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
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			String fechaUUIDXML = null;
			if(fechaUUIDXMLLD != null) {
				fechaUUIDXML = fechaUUIDXMLLD.format(formatter);
	    	}
			
			try {
				Conceptos concepto = _comprobante.getConceptos();
				claveProdServ_XML =  concepto.getConcepto().get(0).getClaveProdServ();	
			}catch(Exception e) {
				claveProdServ_XML =  "";
			}
			
			String tipoComprobante =  _comprobante.getTipoDeComprobante();
			
			String monedaXML = "SIN_MONEDA";
			// String desMoneda = "";
			String cadMoneda = "";
			String tipoMoneda = "";
			boolean bandTipoMoneda = false;
			
			// if (!mapaISR.isEmpty()){
				// ivaRet = Utils.noNulo(mapaISR.get("IVA"));
				// isrRet = Utils.noNulo(mapaISR.get("ISR"));
			// }
			
			try{
					monedaXML =  _comprobante.getMoneda(); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Moneda");
				 	cadMoneda =  _comprobante.getMoneda() + ";" + _comprobante.getMoneda(); // UtilsValidaciones.validaDesMoneda(monedaXML);
					if (cadMoneda.indexOf(";") > -1){
						String arrMoneda [] = cadMoneda.split(";");
						// desMoneda = arrMoneda[0];
						tipoMoneda = arrMoneda[1];
						bandTipoMoneda = UtilsValidaciones.validaTiposMoneda(tipoMoneda, visorForm.getTipoMoneda()); // valida el tipo de moneda con la bd
					}
			}catch(Exception e){
				monedaXML = "SIN_MONEDA";
			}
			
			
			String tipoCambio = "";
			String valAnexo24 = "OK";
			
			try{
				if ("DOLARES".equalsIgnoreCase(visorForm.getTipoMoneda()) && "1".equalsIgnoreCase(provForm.getAnexo24())){
					if ("USD".equalsIgnoreCase(monedaXML)){
						tipoCambio =  String.valueOf(_comprobante.getTipoCambio()); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@TipoCambio");		
					}
				}
				valAnexo24 = UtilsValidaciones.validaAnexo24(monedaXML, tipoCambio, provForm.getAnexo24(), visorForm.getTipoMoneda());
			}catch(Exception e){
				Utils.imprimeLog("", e);
			}
			
			logger.info("monedaXML===>"+monedaXML);
			totalCompararXMLFinal = totalCompararXML;

			
			DecimalFormat df = new DecimalFormat("#.##");
			
			if ("N".equalsIgnoreCase(provForm.getBandDescuento())){ // si esta apagada la validacion, entonces busca la bandera de descuento
				if (descuento > 0){
					try{
						//totalCompararXMLFinal  =  totalCompararXMLFinal - Double.parseDouble(descuento);
						String totalDescuento  =  df.format(totalCompararXMLFinal - descuento);
						totalCompararXMLFinal = Double.parseDouble(totalDescuento);
					}catch(Exception e){
						Utils.imprimeLog("", e);
					}
				}
			}

			
			ValidarEtiquetasFactura valEtiquetas = new ValidarEtiquetasFactura();
			HashMap<String, String> mapaEtiquetas = valEtiquetas.validarEtiquetasXML(con, rc.getEsquema(), _comprobante); // eliminar
			 
			logger.info("mapaEtiquetas===>"+mapaEtiquetas);
			
			
			// se validan las etiquetas Base
			HashMap<String, String> mapaMensajeBase = valEtiquetas.validarEtiquetasBase(con, rc.getEsquema(), esquemaEmpresa, _comprobante);
			logger.info("mapaMensajeBase ------------>" + mapaMensajeBase.get("ERROR"));
			
			
			long folioXML2 = 0;
			try{
				// folioXML = "999999"; // JGBM01
				folioXML2 = Long.parseLong(folioXML);
			}catch(Exception e){
				Utils.imprimeLog("", e);
				folioXML2 = 0;
			}
			
			// se obtiene bandera de tipo de validacion
			  String SERIE_FALTANTE = ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "SERIE_FALTANTE");
			  if ("".equalsIgnoreCase(SERIE_FALTANTE)) {
				  SERIE_FALTANTE = "A";
			  }
			// Termina

			String SERIE_FINAL = null;
			if ("".equals(serieXML) && ("".equals(folioXML))) {
				SERIE_FINAL = "";
			}else if ("".equals(serieXML) && !"".equals(folioXML)) {
				SERIE_FINAL = SERIE_FALTANTE.concat(folioXML);
			}else {
				SERIE_FINAL = serieXML.concat(folioXML);
			}
			
			String factura = SERIE_FINAL;
			
			//ordenesForm.setSubjectValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.SUBJECT2));
			
			/*
			String VALIDAR_CIERRE_ADMIN = mapaConf.get("VALIDAR_CIERRE_ADMIN");
			  if ("".equalsIgnoreCase(VALIDAR_CIERRE_ADMIN)) {
				  VALIDAR_CIERRE_ADMIN = "S";
			  }
			 */
			
			  boolean bandValidaComplemento = true;
				
			  String VALIDAR_COMPLEMENTO_ADMIN = ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "VALIDAR_COMPLEMENTO_ADMIN");
			  logger.info("VALIDAR_COMPLEMENTO_ADMIN===>"+VALIDAR_COMPLEMENTO_ADMIN);
			  if ("".equalsIgnoreCase(VALIDAR_COMPLEMENTO_ADMIN)) {
				  VALIDAR_COMPLEMENTO_ADMIN = "S";
			  }
			  
			  if (idPerfil == 1 && "N".equalsIgnoreCase(VALIDAR_COMPLEMENTO_ADMIN)) {
				  bandValidaComplemento = false;  
			  }
				  
			  ValidacionesPdf leePdf = new ValidacionesPdf();
			  
			  
			  int numeroValidacion = 0;
			 
			  // logger.info("*************** totalCompararXMLFinal ******************"+totalCompararXMLFinal+", "+totalComparar);
			  // logger.info("bandTipoMoneda===>"+bandTipoMoneda);
			  // bandTipoMoneda=true;
			  
			  logger.info("domicilioFiscalReceptor==>"+domicilioFiscalReceptor);
			  logger.info("empresaForm.getCodigoPostal()==>"+empresaForm.getCodigoPostal());
			  
			  if (!"I".equalsIgnoreCase(tipoComprobante)){
				  MENSAJE_VALIDACIONES  = mensajeSIAREX.MENSAJE45;
				  SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
				  mensajeValidacionesXML  = "NO_EXITOSO";
				  numeroValidacion = 1;
			   }else if (!"OK".equalsIgnoreCase(validarFechaRecepcion(esquemaEmpresa, idPerfil, _comprobante, SERIE_FINAL))){
				   mensajeValidacionesXML  = "NO_EXITOSO";
				   numeroValidacion = 2;
			   }/*else  if (!nombreReceptor.equalsIgnoreCase(empresaForm.getNombreLargo()) && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
				   MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE80, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
				   SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
				   mensajeValidacionesXML  = "NO_EXITOSO";
				   numeroValidacion = 3;
			   }else if (!nombreEmisor.equalsIgnoreCase(razonSocial) && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
				   MENSAJE_VALIDACIONES  = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE81, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
				   SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
				   mensajeValidacionesXML  = "NO_EXITOSO";
				   numeroValidacion = 4;
				}else if (domicilioFiscalEmisor != codigoPostalEmisor && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE82, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 5;
				}else if (domicilioFiscalReceptor != empresaForm.getCodigoPostal() && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE83, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 6;
				}else if (!bandRegimenFiscal && "4.0".equalsIgnoreCase(_comprobante.getVersion())){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE85, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 7;
				}*/else if (jsonConceptos.size() == 0) {
					MENSAJE_VALIDACIONES = mensajeSIAREX.MENSAJE60;
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 8;
				}/*else if (!provForm.getRfc().equalsIgnoreCase(rfcFacturaXML)){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE1, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 9;
				}else if (!empresaForm.getRfc().equalsIgnoreCase(rfcReceptorXML)){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE2, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 10;
				}*/else if (bandValidaComplemento &&  new ValidacionesComplemento().tieneComplementoPendientes(con, rc.getEsquema(), provForm.getClaveRegistro())){ // se valida si ya cargo todos los complementos de pago
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE37, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 11;
				}else if (leePdf.lecturaPDF(filePDF.getAbsolutePath(), uuidXML) == -1){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE3, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 12;
				}else if ( !algoritoTotal(totalCompararXMLFinal, totalComparar, provForm.getLimiteTolerancia()) && "0".equals(empresaForm.getTipoAcceso())){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE4, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 13;
				}else if (validaUUID(con, rc.getEsquema(), uuidXML)){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE5, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 14;
				}else if (folioXML2 != visorForm.getFolioEmpresa() && "1".equals(empresaForm.getTipoAcceso())){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE6, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 15;
				}else if ("".equals(uuidXML) || "".equals(fechaUUIDXML)) {
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE7, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 16;
				}else if (validaSerieDUPLICADA(con, rc.getEsquema(), provForm, SERIE_FINAL)) { // Valida si la Serie y Folio ya estan registrados en base de datos 
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE39, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 17;
				}//else if (!"SIN_MONEDA".equalsIgnoreCase(monedaXML) &&  "".equals(desMoneda)){
				else if ("SIN_MONEDA".equalsIgnoreCase(monedaXML)){
					
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE8, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 18;
				}else if (!bandTipoMoneda){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE9, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 19;
				}else if ("NG".equalsIgnoreCase(valAnexo24)){
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE10, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 20;
				}else if ("FALSE".equalsIgnoreCase(mapaEtiquetas.get("ERROR"))){
					logger.info("********* ENTRO A VALIDACION DE ETIQUETAS **************");
					String mensaje = mapaEtiquetas.get("MENSAJE");
					MENSAJE_VALIDACIONES =  mensaje.replace("<<folio>>", String.valueOf(visorForm.getFolioEmpresa()))
											     .replace("<<factura>>", factura);;
			     	SUBJECT_VALIDACIONES = mapaEtiquetas.get("SUBJECT");
			     	numeroValidacion = 21;
			     	logger.info("MENSAJE_VALIDACIONES===>"+MENSAJE_VALIDACIONES);
			     	mensajeValidacionesXML  = "NO_EXITOSO";
			     	
				}else if ("FALSE".equalsIgnoreCase(mapaMensajeBase.get("ERROR"))){
					logger.info("********* ENTRO A VALIDACION DE ETIQUETAS **************");
					String mensaje = mapaMensajeBase.get("MENSAJE");
					MENSAJE_VALIDACIONES =  mensaje.replace("<<folio>>", String.valueOf(visorForm.getFolioEmpresa()))
											     .replace("<<factura>>", factura);;
			     	SUBJECT_VALIDACIONES = mapaEtiquetas.get("SUBJECT");
			     	numeroValidacion = 22;
			     	logger.info("MENSAJE_VALIDACIONES===>"+MENSAJE_VALIDACIONES);
			     	mensajeValidacionesXML  = "NO_EXITOSO";
			     	
				}else if (!ValidacionesUSO.existeCFDI(usoCFDI_XML)) {
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE86, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidacionesXML  = "NO_EXITOSO";
					numeroValidacion = 23;
					
				}else if (bandXML){  // se realizan las validaciones de lab 3.3
					if (ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "VALIDACION_USOCFDI").equalsIgnoreCase("VISOR")){
					  if (!visorForm.getUsoCFDI().equalsIgnoreCase(usoCFDI_XML)){
						  MENSAJE_VALIDACIONES = Utils.getMensajeValidacion( 
								  				 	Utils.getMensajeValidacion(
								  							Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE11, "<< RAZON_EMISOR >>", provForm.getRazonSocial()),
								  					"<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())),
								  				 		"<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());
						  SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;	  				
						  mensajeValidacionesXML  = "NO_EXITOSO";
						  numeroValidacion = 24;
					  }else if (!visorForm.getClaveProducto().equalsIgnoreCase(claveProdServ_XML)){
						  MENSAJE_VALIDACIONES = Utils.getMensajeValidacion( Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE12, "<< RAZON_EMISOR >>", provForm.getRazonSocial()),
				  					"<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())),
				  				 "<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());
						  SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;	  				
						  mensajeValidacionesXML  = "NO_EXITOSO";
						  numeroValidacion = 25;
					  }
					}else if (ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "VALIDACION_USOCFDI").equalsIgnoreCase("TABLA")){
						if (!ValidacionesUSO.validarUsoRFC(con, rc.getEsquema(), provForm.getRfc())){  // no encontro el RFC
							MENSAJE_VALIDACIONES = Utils.getMensajeValidacion( 
				  					Utils.getMensajeValidacion(
				  							Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE13, "<< RAZON_EMISOR >>", provForm.getRazonSocial()),
				  					"<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())),
				  				 "<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());
							SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;	  				
							mensajeValidacionesXML  = "NO_EXITOSO";
							numeroValidacion = 26;
						}else if (!ValidacionesUSO.validarUsoCDFI(con, rc.getEsquema(), provForm.getRfc(), usoCFDI_XML)){  // no encontro el Uso CFDI
							//ordenesForm.setMensajeValidacion("Estimado proveedor "+razonSocialEmisor+" le informamos que su orden de compra "+folioFactura+" no pudo ser procesada con (e)xito debido a que el uso del CFDI utilizado en su facturaci(o)n electr(o)nica no concuerda con el registrado por la empresa "+razonSocialReceptor+", le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.");
							MENSAJE_VALIDACIONES = Utils.getMensajeValidacion( 
				  					Utils.getMensajeValidacion(
				  							Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE14, "<< RAZON_EMISOR >>", provForm.getRazonSocial()),
				  					"<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())),
				  				 "<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());
							SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;	  				
							mensajeValidacionesXML  = "NO_EXITOSO";
							numeroValidacion = 27;
						}else if (!ValidacionesUSO.validarUsoClave(con, rc.getEsquema(), provForm.getRfc(), usoCFDI_XML, jsonConceptos, visorForm.getFolioEmpresa())){  // no encontro las claves y producto
							MENSAJE_VALIDACIONES = Utils.getMensajeValidacion( 
				  					Utils.getMensajeValidacion(
				  							Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE15, "<< RAZON_EMISOR >>", provForm.getRazonSocial()),
				  					"<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())),
				  				 "<< RAZON_RECEPTOR >>", empresaForm.getNombreLargo());
							SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;	  				
							mensajeValidacionesXML  = "NO_EXITOSO";
							numeroValidacion = 28;
						}
					}else if (ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "VALIDACION_USOCFDI").equalsIgnoreCase("XML")) {
						if (!ValidacionesUSO.validarUsoClave(con, rc.getEsquema(), provForm.getRfc(), usoCFDI_XML, jsonConceptos, visorForm.getFolioEmpresa())){  // no encontro las claves y producto
							// ordenesForm.setEstatusCFDI("A10");
							// ordenesForm.setMensajeValidacion("OK");
							MENSAJE_VALIDACIONES = "A10";
							mensajeValidacionesXML  = "OK";
							numeroValidacion = 29;
						}
					}
				}
			
			  if (numeroValidacion > 0) {
				  logger.info("La orden de compra "+visorForm.getFolioEmpresa() +", No cumplio con la validacion===>"+numeroValidacion);
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
	
	
	private String validarCartaPorte(String esquemaEmpresa, long folioOrden, long folioEmpresa,  ProveedoresForm provForm, File fileXML, File filePDF, String usuarioHTTP, String idLenguaje) {
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		String mensajeRespuesta = "OK";
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			String VALIDA_CARTA_PORTE = ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "VALIDA_CARTA_PORTE");
			HashMap<String, Object> MAPA_RESULTADO = new HashMap<>();
			MAPA_RESULTADO.put("MENSAJE", "OK");
			
			if ("S".equalsIgnoreCase(VALIDA_CARTA_PORTE) && "S".equalsIgnoreCase(provForm.getCartaPorte())) {
				ValidacionesCartaPorte cartasBean = new ValidacionesCartaPorte();
				MAPA_RESULTADO = cartasBean.procesarXML(con, rc.getEsquema(), fileXML, filePDF, usuarioHTTP, idLenguaje, true, folioEmpresa, 0, folioOrden, "FACTURA");
				
				String ERROR = Utils.noNulo(MAPA_RESULTADO.get("ERROR")).toString();
				if ("A12".equalsIgnoreCase(ERROR)) {
					// ordenesForm.setEstatusCFDI("A12");
					mensajeRespuesta = "A12";
					MENSAJE_VALIDACIONES = "A12";
				}else {
					if (MAPA_RESULTADO == null || "OK".equalsIgnoreCase(MAPA_RESULTADO.get("MENSAJE").toString())) {
						mensajeRespuesta = "OK";
						MENSAJE_VALIDACIONES = "OK";
					}else {
						mensajeRespuesta = MAPA_RESULTADO.get("MENSAJE").toString();
						MENSAJE_VALIDACIONES = mensajeRespuesta;
					}
				}
				// ordenesForm.setMensajeValidacion("OK");
				
			}
			
		} catch (Exception e) {
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
		return mensajeRespuesta;
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
			
			LocalDateTime fechaFactLD =   _comprobante.getFecha();
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
			String [] fechasRecepcion = fechaCierre.obtenerFechas(con, rc.getEsquema(), anio, "F");

			
			
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
			
			 String VALIDAR_CIERRE_ADMIN = ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "VALIDAR_CIERRE_ADMIN");
			  if ("".equalsIgnoreCase(VALIDAR_CIERRE_ADMIN)) {
				  VALIDAR_CIERRE_ADMIN = "S";
			  }
			  
			  boolean bandValidaCierre = true;
			  logger.info("VALIDAR_CIERRE_ADMIN==>"+VALIDAR_CIERRE_ADMIN);
			  if (idPerfil == 1 && "N".equalsIgnoreCase(VALIDAR_CIERRE_ADMIN)) {
				  bandValidaCierre = false;  
			  }
			  logger.info("bandValidaCierre===>"+bandValidaCierre);
			  String factura = SERIE_FINAL;			  
			  logger.info("cumpleHasta===>"+cumpleHasta);
			  logger.info("fechaHasta===>"+fechaHasta);
			  //if (!cumpleHasta && fechaHasta > 0 && bandValidaCierre){
			  if (!cumpleHasta && bandValidaCierre){
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
	
	
	private  boolean algoritoTotal(double totalCompararXMLFinal, double totalComparar, String limiteTolerancia){
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
			if (totalCompararXMLFinal >= totalComparar ){
				bandPrimera = true;
			}
			
			if (totalComparar <= totalCompararXMLFinal){
				bandSegunda = true;
			}
			totalCompararXMLFinal = (totalCompararXMLFinal - totTolerancia);
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
			
			if (bandPrimera && bandSegunda && bandTercera){
				bandTotal = true;
			}
			
		}catch(Exception e){
			Utils.imprimeLog("Validando los totales", e);
		}
		
		return bandTotal;
	}

	
	
	private boolean validaUUID(Connection con, String esquema, String uuid){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean bandRegresa = false;
		try{
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
			if ("".equals(serieFolio)) {
				// JSONObject jsonProv = provBean.infoProveedores(con, esquema, claveProveedor);
				if ("MEX".equalsIgnoreCase(provForm.getTipoProveedor()) ) {
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

	
	private VisorOrdenesForm obtenerValoresXML(String esquemaEmpresa, File fileXML, ProveedoresForm provForm) {
		VisorOrdenesForm visorDatosForm = new VisorOrdenesForm();
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		Comprobante _comprobante = null;
		try {
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			HashMap<String, String> mapaConfig = ConfigAdicionalesBean.obtenerConfiguraciones(con, rc.getEsquema());
			
			HashMap<String, Object> MAPA_RESULTADO = new HashMap<>();
			MAPA_RESULTADO.put("MENSAJE", "OK");
			
			if (MAPA_RESULTADO == null || "OK".equalsIgnoreCase(MAPA_RESULTADO.get("MENSAJE").toString())) {
				_comprobante = LeerXML.ObtenerComprobante(fileXML.getAbsolutePath());
				String rfcEmisorXML =  _comprobante.getEmisor().getRfc();
				String rfcReceptorXML =  _comprobante.getReceptor().getRfc();
				double subTotalXML = _comprobante.getSubTotal();
				double totalXML =  _comprobante.getTotal();
				String uuidXML =   _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
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
				
				// String datosSAT [] = UtilsSAT.validaSAT(rfcFacturaXML, rfcReceptorXML, totalXML, uuidXML);
				// String datosSAT [] = UtilsSAT.validaSATimbradoExpress(con, rc.getEsquema(), visorForm.getFolioEmpresa(), pathXML, usuarioHTTP)
				
				String SERIE_FALTANTE = ConfigAdicionalesBean.obtenerValorVariable(con,  rc.getEsquema(), "SERIE_FALTANTE");
				
				if ("".equalsIgnoreCase(SERIE_FALTANTE)) {
				  SERIE_FALTANTE = "A";
				}
				  
				String SERIE_FINAL = null;
				if ("".equals(serieXML) && ("".equals(folioXML))) {
					SERIE_FINAL = "";
				}else if ("".equals(serieXML) && !"".equals(folioXML)) {
					SERIE_FINAL = SERIE_FALTANTE.concat(folioXML);
				}else {
					SERIE_FINAL = serieXML.concat(folioXML);
				}
				
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
				
				/*
				String estado  = null; 
				String estatus = null;
				if (datosSAT.length > 1) {
					estado = datosSAT[0];
					estatus = datosSAT[1];
				}else {
					estado = "";
					estatus = "";
				}

				String VALIDA_SAT = mapaConfig.get("VALIDA_SAT");
				if ("".equalsIgnoreCase(VALIDA_SAT)) {
					VALIDA_SAT = "S";
				}
				  
				
				if ("S".equalsIgnoreCase(VALIDA_SAT)) {
					if (estado.toUpperCase().indexOf("CANCELADO") > -1 ) {
						visorDatosForm.setDesEstatus("false");
					}else {
						visorDatosForm.setDesEstatus("true");
					}
				}else {
					visorDatosForm.setDesEstatus("true");
				}
				*/
				
					
					visorDatosForm.setSerieFolio(serieXML);
					visorDatosForm.setTotal(String.valueOf(totalXML));
					visorDatosForm.setSubTotal(String.valueOf(subTotalXML));
					visorDatosForm.setIva(String.valueOf(ivaXML));
					visorDatosForm.setIvaRet("0");
					visorDatosForm.setIsrRet("0");
					visorDatosForm.setImpLocales("0");
					visorDatosForm.setUuid(uuidXML);
					visorDatosForm.setFolioXML(String.valueOf(folioXML));
					visorDatosForm.setRfcEmisorXML(rfcEmisorXML);
					visorDatosForm.setRfcReceptorXML(rfcReceptorXML);
					// visorDatosForm.setEstadoCFDI(estado);
					// visorDatosForm.setEstatusCFDI(estatus);
					visorDatosForm.setFechaFactura(fechaFact.substring(0, 10));
					visorDatosForm.setFechaPago(UtilsValidaciones.obtenerFechaPago(mapaConfig)); // Se calcula la fecha de Pago
					visorDatosForm.setUsoCFDI(usoCFDI_XML);
					visorDatosForm.setClaveProducto(claveProdServ_XML);
					
					if (fechaUUIDXML.length() > 10){
						visorDatosForm.setFechaUUID(fechaUUIDXML.substring(0, 10));	
					}else{
						visorDatosForm.setFechaUUID(fechaUUIDXML);
					}
					visorDatosForm.setSerieFolio(SERIE_FINAL);
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
			}
			
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
	

	
	private long generaOrden(String esquemaEmpresa, long folioEmpresa, String rfcProveedor, double montoFactura)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long resultado = 0;
        ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		try {
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
            stmt = con.prepareStatement(ValidacionesQuerys.getGeneraOrden(rc.getEsquema()), PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, folioEmpresa);
            stmt.setString(2, rfcProveedor);
            stmt.setDouble(3, montoFactura);
            int cant = stmt.executeUpdate();
            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next())
                    resultado = rs.getLong(1);
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
	            if (con != null){
					con.close();
				}
				con = null;
	        }catch(Exception e){
	            stmt = null;
	            con = null;
	        }
        }
        return resultado;
    }
	

	private int actualizaOrden(String esquemaEmpresa, long folioSistema, long folioFactura, VisorOrdenesForm visorForm, String usuarioAdjunto, String estatusCFDI){
		PreparedStatement stmt = null;
		int cantidadFactura = 0;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		try {
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			stmt = con.prepareStatement(ValidacionesQuerys.getQueryActualizaOrden(rc.getEsquema()));
			stmt.setLong(1, folioFactura);
			String serRecibido = "0";
			// logger.info("Estatus==>"+visorForm.getEstatusOrden());
			if ("A2".equalsIgnoreCase(visorForm.getEstatusOrden())){
				serRecibido = "1";
				logger.info("estatusCFDI==>"+estatusCFDI);
				if ("A10".equalsIgnoreCase(estatusCFDI)) {
					stmt.setString(2, "A10");
					//ordenesForm.setFechaPago(null);
				}else if ("A12".equalsIgnoreCase(estatusCFDI)) {
					stmt.setString(2, "A12");
				}else {
					stmt.setString(2, "A3");
				}
			}else {
				serRecibido = "0";
				visorForm.setFechaPago(null);
				if ("A10".equalsIgnoreCase(estatusCFDI)) {
					stmt.setString(2, "A10");
				}else if ("A12".equalsIgnoreCase(estatusCFDI)) {
					stmt.setString(2, "A12");
				}else {
					stmt.setString(2, "A1");	
				}
			}

			stmt.setString(3, visorForm.getSerieFolio());
			stmt.setString(4, visorForm.getTotal());
			stmt.setString(5, visorForm.getSubTotal());
			stmt.setString(6, visorForm.getIva());
			stmt.setString(7, visorForm.getIvaRet());
			stmt.setString(8, visorForm.getIsrRet());
			stmt.setString(9, visorForm.getImpLocales());
			stmt.setString(10, visorForm.getNombreXML());
			stmt.setString(11, visorForm.getNombrePDF());
			stmt.setString(12, serRecibido);
			stmt.setString(13, visorForm.getUuid());
			stmt.setString(14, visorForm.getFechaUUID());
			stmt.setString(15, usuarioAdjunto);
			stmt.setString(16, visorForm.getEstadoCFDI());
			stmt.setString(17, visorForm.getEstatusCFDI());
			stmt.setString(18, visorForm.getFechaFactura());
			stmt.setString(19, visorForm.getUsoCFDI());
			stmt.setString(20, visorForm.getClaveProducto());
			stmt.setString(21, visorForm.getFechaPago());
			stmt.setLong(22, folioSistema);
			
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
				if (con != null){
					con.close();
				}
				con = null;
			}catch(Exception e){
				stmt = null;
				con = null;
			}
		}
		return cantidadFactura;
	}


	private boolean validarXML(String nombreEmpresa, VisorOrdenesForm datosOrdenes, long folioEmpresa, String pathXML, VisorOrdenesForm visorForm, String usuarioHTTP) {
		boolean bandValidacion = false;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;

		try {
			rc = connPool.getConnection(nombreEmpresa);
			con = rc.getCon();
			
			
			String VALIDA_XML_TIMBRADO = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDA_XML_TIMBRADO");
			String VALIDA_SAT = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDA_SAT");
			datosOrdenes.setDesEstatus("true");
			//logger.info("VALIDA_XML_TIMBRADO====>"+VALIDA_XML_TIMBRADO);
			if ("S".equalsIgnoreCase(VALIDA_XML_TIMBRADO)) { // configuracion apagada
				con.close();
				TimbradoExpressForm timbradoExpressForm = UtilsSAT.validaSATimbradoExpress(nombreEmpresa, folioEmpresa, pathXML, usuarioHTTP);
				if (timbradoExpressForm.getListaErrores().size() == 0) {
					bandValidacion = true;
				}
				
				if ("200".equalsIgnoreCase(timbradoExpressForm.getCode())) {
					 
					datosOrdenes.setEstadoCFDI(timbradoExpressForm.getStatusSat());
					datosOrdenes.setEstatusCFDI(timbradoExpressForm.getStatusCodeSat());
					
					if ("".equalsIgnoreCase(VALIDA_SAT)) {
						VALIDA_SAT = "S";
					}
					if ("S".equalsIgnoreCase(VALIDA_SAT)) {
						if (timbradoExpressForm.getStatusSat().toUpperCase().indexOf("CANCELADO") > -1 ) {
							bandValidacion = false;
							datosOrdenes.setDesEstatus("false");
							visorForm.setDesEstatus("false");
						}
					}
				}
			}else {
				bandValidacion = true;
				String datosSAT [] = UtilsSAT.validaSAT(datosOrdenes.getRfcEmisorXML(), datosOrdenes.getRfcReceptorXML(), Utils.noNuloDouble(datosOrdenes.getTotal()), datosOrdenes.getUuid());
				
				String estado  = null; 
				String estatus = null;
				if (datosSAT.length > 1) {
					estado = datosSAT[0];
					estatus = datosSAT[1];
				}else {
					estado = "";
					estatus = "";
				}

				if ("".equalsIgnoreCase(VALIDA_SAT)) {
					VALIDA_SAT = "S";
				}
				  
				if ("S".equalsIgnoreCase(VALIDA_SAT)) {
					if (estado.toUpperCase().indexOf("CANCELADO") > -1 ) {
						datosOrdenes.setDesEstatus("false");
						visorForm.setDesEstatus("false");
					}else {
						datosOrdenes.setDesEstatus("true");
						visorForm.setDesEstatus("true");
					}
				}else {
					datosOrdenes.setDesEstatus("true");
					visorForm.setDesEstatus("true");
				}
				
				datosOrdenes.setEstadoCFDI(estado);
				datosOrdenes.setEstatusCFDI(estatus);
				
			}
		} catch (Exception e) {
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
		return bandValidacion;
	}
	
	
	
	private String obtenerValidacionXML(String esquemaEmpresa,  VisorOrdenesForm datosOrdenes, long folioEmpresa, ProveedoresForm provForm) {
		StringBuffer MENSAJE_RESPUESTA = new StringBuffer();
		TimbradoBean timbradoBean = new TimbradoBean();
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;

		try {
			
			// String uuid = "0bbecec0-cd67-4189-8f60-f92a6d23f9d3";
			String uuid = datosOrdenes.getUuid();
			
			TimbradoForm timbradoForm = timbradoBean.consultarRespuestaTimbrado(uuid);
			
			if ("200".equalsIgnoreCase(timbradoForm.getCode())) {
				
				org.json.JSONObject jsonObject = new org.json.JSONObject(timbradoForm.getRespuesta().toString());
				
				String statusSat = jsonObject.get("statusSat").toString();
				/*
				String uuid = jsonObject.get("uuid").toString();
				
				String statusCodeSat = jsonObject.get("statusCodeSat").toString();
				String status = jsonObject.get("status").toString();
				
				logger.info("uuid==>"+uuid);
				logger.info("statusSat==>"+statusSat);
				logger.info("statusCodeSat==>"+statusCodeSat);
				logger.info("status==>"+status);
				*/
				
				// retorno[0] = jsonObject.get("status").toString();
				// retorno[1] = jsonObject.get("statusCodeSat").toString();
				// retorno[2] = jsonObject.get("statusSat").toString();
				
				org.json.JSONArray jsonArray = new org.json.JSONArray(jsonObject.get("detail").toString());
				
				// DetailError detailError = new DetailError();
				for (int x = 0; x < jsonArray.length(); x++) {
					org.json.JSONObject jsonLista = (org.json.JSONObject) jsonArray.get(x);
					// logger.info("************************************************************************************");
					// logger.info("detail==>"+jsonLista.get("detail"));
					// logger.info("section==>"+jsonLista.get("section"));
					
					org.json.JSONArray jsonSubDetail = new org.json.JSONArray(jsonLista.get("detail").toString());
					for (int y = 0; y < jsonSubDetail.length(); y++) {
						org.json.JSONObject jsonSubLista = (org.json.JSONObject) jsonSubDetail.get(y);
						// logger.info("-------------------------------------------------------------------------------------");
						if ("Error".equalsIgnoreCase(jsonSubLista.get("typeValue").toString())) {
							/*
							detailError.setMessage(Utils.noNulo(jsonSubLista.get("message")).toString());
							detailError.setTypeValue(Utils.noNulo(jsonSubLista.get("typeValue")).toString());
							detailError.setMessageDetail(Utils.noNulo(jsonSubLista.get("messageDetail")).toString());
							*/
							
							MENSAJE_RESPUESTA.append(Utils.noNulo(jsonSubLista.get("message")).toString()).append("\n");
							/*
								logger.info("typeValue======>"+jsonSubLista.get("typeValue"));
								logger.info("message========>"+jsonSubLista.get("message"));
								logger.info("messageDetail==>"+jsonSubLista.get("messageDetail"));
							*/
						}
					}
				}
				
				rc = connPool.getConnection(esquemaEmpresa);
				con = rc.getCon();
				
				
				String VALIDA_SAT = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDA_SAT");
				
				if ("".equalsIgnoreCase(VALIDA_SAT)) {
					VALIDA_SAT = "S";
				}
				
				if ("S".equalsIgnoreCase(VALIDA_SAT)) {
					if (statusSat.toUpperCase().indexOf("CANCELADO") > -1 ) {
						MENSAJE_RESPUESTA.append("MSGSIA027 - " +  Utils.regresaCaracteresNormales(Utils.getMensajeValidacion( Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE27, "<< RAZON_EMISOR >>", provForm.getRazonSocial()), "<< FOLIO_FACTURA >>", String.valueOf(folioEmpresa)))).append("\n");
					}
				}
			}else {
				MENSAJE_RESPUESTA.append("OK");
			}
			

		} catch (Exception e) {
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
		return MENSAJE_RESPUESTA.toString();
	}
	
}
