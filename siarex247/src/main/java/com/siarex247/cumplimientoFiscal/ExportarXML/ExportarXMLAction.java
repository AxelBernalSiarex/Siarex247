package com.siarex247.cumplimientoFiscal.ExportarXML;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.catalogos.Proveedores.ProveedoresModel;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaBean;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaModel;
import com.siarex247.registro.RegistroBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class ExportarXMLAction extends ExportarXMLSupport{

	private static final long serialVersionUID = -8787969302367805763L;

	
	public String exportarFacturas(){
    	HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	SiarexSession session = ObtenerSession.getSession(request);
    	Connection con = null;
		ResultadoConexion rc = null;
		
		// Connection conSiarex = null;
		// ResultadoConexion rcSiarex = null;
		
		ExportarXMLBean exportarXMLBean = new ExportarXMLBean();
		
		LenguajeBean lenguajeBean = LenguajeBean.instance();
		
    	try{
			PrintWriter out = response.getWriter();
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				// rcSiarex = getConnectionSiarex();
				// conSiarex = rcSiarex.getCon();
				
				ExportarXMLModel exportarModel = new ExportarXMLModel();
				
				String modoAgrupar = getModoAgrupar();
				if (modoAgrupar == null) {
					modoAgrupar = "NONE";
				}
				
				String CORREO_RESPONSABLE = getCorreoResponsable();
				
				String validarSAT = getValidarSAT();
				String complementoSAT = getComplementoSAT();
				String descargarNotaCredito = Utils.noNulo(getDescargarNotaCredito());
				String notaCreditoSAT = Utils.noNulo(getNotaCreditoSAT());
				
				
				String descargarFacturas = Utils.noNulo(getDescargarFacturas());
				String descargarComplemento = Utils.noNulo(getDescargarComplemento());
				
				String rfcProveedor = Utils.noNulo(getRfcProveedor());
				String fechaInicial = Utils.noNulo(getFechaInicial());
				String fechaFinal = Utils.noNulo(getFechaFinal());
				String tipoBusqueda = Utils.noNulo(getTipoExportacion());
				
				
				if ("on".equalsIgnoreCase(validarSAT)) {
					validarSAT = "S";
				}else {
					validarSAT = "N";
				}
				
				if ("on".equalsIgnoreCase(complementoSAT)) {
					complementoSAT = "S";
				}else {
					complementoSAT = "N";
				}
				
				if ("on".equalsIgnoreCase(notaCreditoSAT)) {
					notaCreditoSAT = "S";
				}else {
					notaCreditoSAT = "N";
				}
				
				if ("on".equalsIgnoreCase(descargarFacturas)) {
					descargarFacturas = "S";
				}else {
					descargarFacturas = "N";
				}
				
				if ("on".equalsIgnoreCase(descargarComplemento)) {
					descargarComplemento = "S";
				}else {
					descargarComplemento = "N";
				}
				
				if ("on".equalsIgnoreCase(descargarNotaCredito)) {
					descargarNotaCredito = "S";
				}else {
					descargarNotaCredito = "N";
				}
				
				logger.info("modoAgrupar-------->"+modoAgrupar);
				logger.info("CORREO_RESPONSABLE-------->"+CORREO_RESPONSABLE);
				logger.info("validarSAT-------->"+validarSAT);
				logger.info("complementoSAT-------->"+complementoSAT);
				logger.info("notaCreditoSAT-------->"+notaCreditoSAT);
				logger.info("descargarFacturas-------->"+descargarFacturas);
				logger.info("descargarComplemento-------->"+descargarComplemento);
				logger.info("descargarNotaCredito-------->"+descargarNotaCredito);
				logger.info("rfcProveedor-------->"+rfcProveedor);
				logger.info("fechaInicial-------->"+fechaInicial);
				logger.info("fechaFinal-------->"+fechaFinal);
				logger.info("tipoBusqueda-------->"+tipoBusqueda);
				
				boolean cumpleValidaciones = true;
				File fileXMLDest = null;
				String rutaXMLProcesar = "";
				long codeOperacion = 0;
				
				HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "EXPORTARXML");
				
				if ("TEXTO".equalsIgnoreCase(tipoBusqueda)) {
					
					Part filePart = request.getPart("fileTXT");
	            	File fileTXT = UtilsFile.getFileFromPart(filePart);
	            	
					
					if (fileTXT == null) {
						exportarModel.setCodError("001");
						//exportarModel.setMensajeError("Error el procesar la información, debe especifica un archivo TXT continuar.");
						exportarModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG1")));
						cumpleValidaciones = false;
					}else {
						exportarModel.setCodError("000");
						exportarModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
						
						String rutaDest =  UtilsPATH.RUTA_PUBLIC_LAYOUT +fileTXT.getName();
						
						fileXMLDest = new File(rutaDest);
						UtilsFile.moveFileDirectory(fileTXT, fileXMLDest, true, true, true, false);
						
					}
				}else if ("XML".equalsIgnoreCase(tipoBusqueda)) {
					logger.info("Exportando por XML............");
					
					Collection<Part> colectionPart  = request.getParts();
			        ArrayList<File> listFilesXML = com.siarex247.utils.UtilsFile.getFilesFromPart(colectionPart);
			          
					
					if (listFilesXML == null || listFilesXML.size() == 0) {
						exportarModel.setCodError("001");
						// exportarModel.setMensajeError("Error el procesar la información, debe especificar los XML a procesar.");
						exportarModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG2")));
						cumpleValidaciones = false;
					}else {
						exportarModel.setCodError("000");
						exportarModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
						
						codeOperacion = System.currentTimeMillis();
						rutaXMLProcesar = UtilsPATH.RUTA_PUBLIC_HTML + session.getEsquemaEmpresa() + File.separator + "TEMP_FACTURAS" + File.separator + File.separator + codeOperacion + File.separator;
						String rutaDestinoXML = null;
						int nameXML = 1; 
						for (File fileXML : listFilesXML) {
							
							rutaDestinoXML = rutaXMLProcesar + "XML_"+nameXML+".xml";
							fileXMLDest = new File(rutaDestinoXML); // XML
							UtilsFile.moveFileDirectory(fileXML, fileXMLDest, true, true, true, false);
							nameXML++;
							// String nameExtencion = FilenameUtils.getExtension(rutaDestinoXML);
						}
					}
					
				}else { // es por RFC
					
					if ("".equalsIgnoreCase(rfcProveedor)) {
						exportarModel.setCodError("001");
						// exportarModel.setMensajeError("Error el procesar la información, debe especifica un RFC para continuar.");
						exportarModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG3")));
						cumpleValidaciones = false;
					}else if ("".equalsIgnoreCase(fechaInicial)) {
						exportarModel.setCodError("001");
						// exportarModel.setMensajeError("Error el procesar la información, debe especifica una Fecha Inicial para continuar.");
						exportarModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG4")));
						cumpleValidaciones = false;
					}else if ("".equalsIgnoreCase(fechaFinal)) {
						exportarModel.setCodError("001");
						// exportarModel.setMensajeError("Error el procesar la información, debe especifica una Fecha Final para continuar.");
						exportarModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG5")));
						cumpleValidaciones = false;
					}else if (!UtilsFechas.compararFechaMayor(fechaFinal, fechaInicial)) {
						exportarModel.setCodError("001");
						// exportarModel.setMensajeError("Error el procesar la información, la fecha inicial no debe ser mayor que la fecha final.");
						exportarModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG6")));
						cumpleValidaciones = false;
					}else {
						exportarModel.setCodError("000");
						exportarModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
						cumpleValidaciones = true;
					}
				}
				
				if (cumpleValidaciones) {
					AccesoBean accesoBean = new AccesoBean();
					EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());

					UsuariosBean usuariosBean = new UsuariosBean();
			        UsuariosForm usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request)); 
			        
					exportarXMLBean.procesaArchivo(fileXMLDest, session.getEsquemaEmpresa(), getUsuario(request),
							empresasForm.getEmailDominio(), empresasForm.getPwdCorreo(), usuariosForm.getNombreCompleto(),
		                     CORREO_RESPONSABLE, modoAgrupar, validarSAT, complementoSAT, notaCreditoSAT, descargarFacturas, 
		                     descargarComplemento, descargarNotaCredito, tipoBusqueda, rfcProveedor, fechaInicial, fechaFinal, rutaXMLProcesar, codeOperacion );
				}
				
				JSONObject json = new JSONObject(exportarModel);
				out.print(json);
				out.flush();
				out.close();
			}
		}
    	catch(Exception e){
			Utils.imprimeLog("", e);
		}
    	finally{
			try{
				if (con != null){
					con.close();
				}
				con = null;
			}catch(Exception e){
				con = null;
			}
		}
		return SUCCESS;
    }
	
	
	
	public String iniciarDescarga() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		Connection conSiarex = null;
		ResultadoConexion rcSiarex = null;
		
		ExportarXMLBean exportarBean = new ExportarXMLBean();
		
		RegistroBean registroBean = new RegistroBean();
		
		String retorno = "success";
		try{
			
			logger.info("********* DESCARGANDO ARCHIVO ********************");
			rcSiarex = getConnectionSiarex();
			conSiarex = rcSiarex.getCon(); 
			String pathArchivo = exportarBean.consultarDescarga(conSiarex, "", getIdArchivo(), getUsuario(request));
			
			// String pwdUsuario = Utils.dobleEncryptarMD5(Utils.noNuloNormal(getPwdUsuario()));
			boolean isCorrecto = registroBean.validarUsuarioDescarga(conSiarex, "", getUsuario(request));
			
			if ("".equalsIgnoreCase(pathArchivo)) {
				setMensajeError("Codigo de acceso invalido.");
				retorno = "ERROR";
			}else if (!isCorrecto) {
				setMensajeError("Acceso denegado.");
				retorno = "ERROR";
			}else {
				
				String documentoZip = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + pathArchivo;   
				logger.info("********* documentoZip ********************"+documentoZip); 
				// File archivo =  new File(documentoZip);
				 InputStream inputStream = new FileInputStream(new File(documentoZip));
				 setInputStream(inputStream);
				
			}
			
            
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				
				if (conSiarex != null){
					conSiarex.close();
				}
				conSiarex = null;
			}catch(Exception e){
				conSiarex = null;
			}
		}
		return retorno;
	}
	
	
	public String comboProveedores(){
        Connection con = null;
        ResultadoConexion rc = null;
        HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ExportarXMLBean exportarBean = new ExportarXMLBean();
        try{
        	response.setCharacterEncoding("UTF-8");
        	SiarexSession session = ObtenerSession.getSession(request);
        	PrintWriter out = response.getWriter();
            rc = getConnection(session.getEsquemaEmpresa());
            con = rc.getCon();

            ProveedoresModel provModel = new ProveedoresModel();
            ArrayList<ProveedoresForm> listaCombo = exportarBean.comboProveedores(con, rc.getEsquema(), session.getLenguaje());  
            provModel.setData(listaCombo);
            
			JSONObject json = new JSONObject(provModel);
			out.print(json);
            out.flush();
            out.close();
            
        }
        catch(Exception e){
        	Utils.imprimeLog("comboProveedoresExt(): ", e);
            logger.error(e);
        }finally {
	        try{
	            if(con != null) {
	                con.close();
	            }
	            con = null;
	        }
	        catch(Exception e){
	            con = null;
	        }
		}
        return null;
    }
	
	
	/*
	public String exportBovedaZIP() {
		Connection con = null;
		ResultadoConexion rc = null;
		
    	HttpServletRequest request = ServletActionContext.getRequest();
    	HttpServletResponse response = ServletActionContext.getResponse();
    	BovedaBean bovedaBean = new BovedaBean();
    	
    	String rutaHTML = null;
		try{
			logger.info("************ detonando exportacion de facturas desde facturacion ************");
			Map<String, Object > mapaRes = null;
			JSONArray jsonArray  = null;
			PrintWriter out = response.getWriter();
			String esquemaEmpresa = getNombreEmpresa();
			rc = getConnection(esquemaEmpresa);
			con = rc.getCon();

			rutaHTML = UtilsPATH.RUTA_PUBLIC_HTML +esquemaEmpresa + File.separator +  "TEMP_PDF" + File.separator;				
			File fileTemp = new File(rutaHTML);
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}
			// String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +session.getEsquemaEmpresa() + "/BOVEDA/";
			
			String idRegistro = getIdRegistro();
			String fechaInicial = Utils.noNulo(getFechaInicial());
	  		String fechaFinal = Utils.noNulo(getFechaFinal());
	  		
			mapaRes  = bovedaBean.detalleBovedaZIP(con, esquemaEmpresa, Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), idRegistro);
			jsonArray  = (JSONArray) mapaRes.get("detalle");
			org.json.simple.JSONObject jsonobj = null;
			
			ArrayList<String> listaTXT = new ArrayList<String>();
			
			String fecA = UtilsFechas.getFechaActualNumero();
			String nombreReporte = "descargaCFDI_Boveda_"+"_"+ esquemaEmpresa + "_" +fecA+ ".txt";
			String pathCSV = UtilsPATH.RUTA_PUBLIC_HTML + esquemaEmpresa + File.separator + "TEMP_PDF" + File.separator + nombreReporte;
			
			for (int x = 0; x < jsonArray.size(); x++){
				jsonobj = (org.json.simple.JSONObject) jsonArray.get(x);
				listaTXT.add(jsonobj.get("UUID").toString());
			}
		
			if (listaTXT.size() > 0) {
				UtilsFile.crearArchivoSalto(listaTXT, pathCSV);
			}
				
				
				String modoAgrupar = getModoAgrupar();
				if (modoAgrupar == null) {
					modoAgrupar = "NONE";
				}
				String CORREO_RESPONSABLE = getCorreoResponsable();
				String validarSAT = getValidarSAT();
				String complementoSAT = getComplementoSAT();
				String notaCreditoSAT = Utils.noNulo(getNotaCreditoSAT());
				
				if ("true".equalsIgnoreCase(validarSAT)) {
					validarSAT = "S";
				}else {
					validarSAT = "N";
				}
				
				if ("true".equalsIgnoreCase(complementoSAT)) {
					complementoSAT = "S";
				}else {
					complementoSAT = "N";
				}
				
				if ("true".equalsIgnoreCase(notaCreditoSAT)) {
					notaCreditoSAT = "S";
				}else {
					notaCreditoSAT = "N";
				}
				
				
				logger.info("CORREO_RESPONSABLE===>"+CORREO_RESPONSABLE);
				logger.info("validarSAT===>"+validarSAT);
				logger.info("complementoSAT===>"+complementoSAT);
				logger.info("notaCreditoSAT===>"+notaCreditoSAT);
				logger.info("modoAgrupar===>"+modoAgrupar);
				
				String rutaDest =  UtilsPATH.RUTA_PUBLIC_LAYOUT +nombreReporte;
				
				File fileXMLDest = new File(rutaDest);
				File fileTXT = new File(pathCSV);
				
				UtilsFile.moveFileDirectory(fileTXT, fileXMLDest, true, true, true, false);
				
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(esquemaEmpresa);

				String descargarFacturas = "S";
				String descargarComplemento = "S";
				String descargarNotaCredito = "S";
				String tipoBusqueda = "TEXTO";
				String rfcProveedor = "";
				String rutaXMLProcesar = "";
				long codeOperacion = 0;
				
				UsuariosBean usuariosBean = new UsuariosBean();
				ExportarXMLBean exportarXMLBean = new ExportarXMLBean();
		        UsuariosForm usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request)); 
				exportarXMLBean.procesaArchivo(fileXMLDest, esquemaEmpresa, getUsuario(request),
						empresasForm.getEmailDominio(), empresasForm.getPwdCorreo(), usuariosForm.getNombreCompleto(),
	                     CORREO_RESPONSABLE, modoAgrupar, validarSAT, complementoSAT, notaCreditoSAT, descargarFacturas, 
	                     descargarComplemento, descargarNotaCredito, tipoBusqueda, rfcProveedor, fechaInicial, fechaFinal, rutaXMLProcesar, codeOperacion );
				
				
				BovedaModel bovedaModel = new BovedaModel();
				bovedaModel.setCodError("000");
				bovedaModel.setMensajeError("Filtro seleccionado correctamente.");
				
				org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
				out.print(json);
	            out.flush();
	            out.close();
            
			
		}catch(Exception e){
			Utils.imprimeLog("exportBovedaZIP(): ", e);
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
		return SUCCESS;
	}
	*/
	
}
