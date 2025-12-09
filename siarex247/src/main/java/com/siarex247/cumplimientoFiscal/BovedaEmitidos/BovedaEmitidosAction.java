package com.siarex247.cumplimientoFiscal.BovedaEmitidos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.CreaPDF;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaModel;
import com.siarex247.cumplimientoFiscal.Boveda.ExtraerXMLBean;
import com.siarex247.cumplimientoFiscal.BovedaNomina.BovedaNominaBean;
import com.siarex247.cumplimientoFiscal.ExportarXML.ExportarXMLBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.ConvierteEXCEL;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.LeerDatosXML;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


public class BovedaEmitidosAction extends BovedaEmitidosSupport{

	private static final long serialVersionUID = -112258213613275729L;
	private InputStream inputStream;
	private String reportFile;
	
	public String detalleBoveda(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
    	
		try{
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
		  		
		  		int draw = getDraw();
		  		if (draw == 0) {
		  			draw = 1;
		  		}
				
		  		String fechaInicial = Utils.noNulo(getFechaInicial());
		  		String fechaFinal = Utils.noNulo(getFechaFinal());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			dateOperator = "bt";
		  		}else {
		  			dateOperator = getDateOperator();
		  		}
		  		dateOperator = "bt";
		  		BovedaEmitidosModel bovedaModel = new BovedaEmitidosModel();
		  		//ArrayList<BovedaEmitidosForm> listaDetalle  = bovedaBean.detalleBoveda(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), getStart(), 20, false);
		  		//int totalRegistro = bovedaBean.totalRegistros(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal));
		  		int pageSize = (getLength() > 0 ? getLength() : 20);

		  		ArrayList<BovedaEmitidosForm> listaDetalle = bovedaBean.detalleBoveda(
		  		    con,
		  		    session.getEsquemaEmpresa(),
		  		    Utils.noNulo(getRfc()),
		  		    Utils.noNulo(getRazonSocial()),
		  		    Utils.noNulo(getFolio()),
		  		    Utils.noNulo(getSerie()),
		  		    Utils.noNulo(fechaInicial),
		  		    Utils.noNulo(getTipoComprobante()),
		  		    Utils.noNulo(getUuid()),
		  		    Utils.noNulo(fechaFinal),
		  		    getStart(),
		  		    pageSize,
		  		    false,
		  		    // operadores texto
		  		    Utils.noNulo(getRfcOperator()),
		  		    Utils.noNulo(getRazonOperator()),
		  		    Utils.noNulo(getSerieOperator()),
		  		    Utils.noNulo(getTipoOperator()),
		  		    Utils.noNulo(getUuidOperator()),
		  		    // fecha
		  		    Utils.noNulo(dateOperator),
		  		    Utils.noNulo(getDateV1()),
		  		    Utils.noNulo(getDateV2()),
		  		    // numéricos
		  		    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
		  		    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
		  		    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
		  		    Utils.noNulo(getIvaOperator()),    Utils.noNulo(getIvaV1()),    Utils.noNulo(getIvaV2()),
		  		    Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
		  		    Utils.noNulo(getIsrOperator()),    Utils.noNulo(getIsrV1()),    Utils.noNulo(getIsrV2()),
		  		    Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2())
		  		);
		  		
		  		int totalRegistro = bovedaBean.totalRegistros(
		  			    con,
		  			    session.getEsquemaEmpresa(),
		  			    Utils.noNulo(getRfc()),
		  			    Utils.noNulo(getRazonSocial()),
		  			    Utils.noNulo(getFolio()),
		  			    Utils.noNulo(getSerie()),
		  			    Utils.noNulo(fechaInicial),
		  			    Utils.noNulo(getTipoComprobante()),
		  			    Utils.noNulo(getUuid()),
		  			    Utils.noNulo(fechaFinal),

		  			    // operadores de texto
		  			    Utils.noNulo(getRfcOperator()),
		  			    Utils.noNulo(getRazonOperator()),
		  			    Utils.noNulo(getSerieOperator()),
		  			    Utils.noNulo(getTipoOperator()),
		  			    Utils.noNulo(getUuidOperator()),

		  			    // fecha con operadores
		  			    Utils.noNulo(dateOperator),
		  			    Utils.noNulo(getDateV1()),
		  			    Utils.noNulo(getDateV2()),

		  			    // numéricos
		  			    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
		  			    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
		  			    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
		  			    Utils.noNulo(getIvaOperator()),    Utils.noNulo(getIvaV1()),    Utils.noNulo(getIvaV2()),
		  			    Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
		  			    Utils.noNulo(getIsrOperator()),    Utils.noNulo(getIsrV1()),    Utils.noNulo(getIsrV2()),
		  			    Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2())
		  			);

		  		bovedaModel.setData(listaDetalle);
		  		//bovedaModel.setRecordsTotal(listaDetalle.size());
		  		bovedaModel.setRecordsTotal(totalRegistro);
		  		bovedaModel.setRecordsFiltered(totalRegistro);
		  		bovedaModel.setDraw(getDraw());

		  		
				org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
				out.print(json);
	            out.flush();
	            out.close();
	          //  logger.info("json==>"+json);
			}
			
            
		}catch(Exception e){
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
		return SUCCESS;
	}
	
	public String obtenerTotales(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
    	
		try{
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
		  		
		  		int draw = getDraw();
		  		if (draw == 0) {
		  			draw = 1;
		  		}
				
		  		String fechaInicial = Utils.noNulo(getFechaInicial());
		  		String fechaFinal = Utils.noNulo(getFechaFinal());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			dateOperator = "bt";
		  		}else {
		  			dateOperator = getDateOperator();
		  		}
		  		dateOperator = "bt";
		  		BovedaEmitidosModel bovedaModel = new BovedaEmitidosModel();
		  		//int totalRegistro = bovedaBean.totalRegistros(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal));
		  		int totalRegistro = bovedaBean.totalRegistros(
		  			    con,
		  			    session.getEsquemaEmpresa(),
		  			    Utils.noNulo(getRfc()),
		  			    Utils.noNulo(getRazonSocial()),
		  			    Utils.noNulo(getFolio()),
		  			    Utils.noNulo(getSerie()),
		  			    Utils.noNulo(fechaInicial),
		  			    Utils.noNulo(getTipoComprobante()),
		  			    Utils.noNulo(getUuid()),
		  			    Utils.noNulo(fechaFinal),

		  			    // operadores de texto
		  			    Utils.noNulo(getRfcOperator()),
		  			    Utils.noNulo(getRazonOperator()),
		  			    Utils.noNulo(getSerieOperator()),
		  			    Utils.noNulo(getTipoOperator()),
		  			    Utils.noNulo(getUuidOperator()),

		  			    // fecha con operadores
		  			    Utils.noNulo(dateOperator),
		  			    Utils.noNulo(getDateV1()),
		  			    Utils.noNulo(getDateV2()),

		  			    // numéricos
		  			    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
		  			    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
		  			    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
		  			    Utils.noNulo(getIvaOperator()),    Utils.noNulo(getIvaV1()),    Utils.noNulo(getIvaV2()),
		  			    Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
		  			    Utils.noNulo(getIsrOperator()),    Utils.noNulo(getIsrV1()),    Utils.noNulo(getIsrV2()),
		  			    Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2())
		  			);
		  		bovedaModel.setRecordsTotal(totalRegistro);
		  		bovedaModel.setCodError("000");
		  		
		  		int TOT_REGISTROS_DESCARGAR = Utils.noNuloINT(ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "TOT_REGISTROS_DESCARGAR"));
		  		if (totalRegistro > TOT_REGISTROS_DESCARGAR){
		  			bovedaModel.setCodError("001");
		  		}
		  		
				org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
				out.print(json);
	            out.flush();
	            out.close();
			}
			
            
		}catch(Exception e){
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
		return SUCCESS;
	}
	
	public String iniciaCargaXML(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Connection con = null;
		ResultadoConexion rc = null;
		PrintWriter out = null;
		
		logger.info("Tiempo Inicial : "+System.currentTimeMillis());
		try{
		   SiarexSession session = ObtenerSession.getSession(request);
		  if ("".equals(session.getEsquemaEmpresa())){
			return Action.LOGIN;
		  }else{
			  
				out = response.getWriter();
				BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
				
				Collection<Part> colectionPart  = request.getParts();
		        ArrayList<File> listFilesXML = com.siarex247.utils.UtilsFile.getFilesFromPart(colectionPart);
		        
				//LeerXML crea = null;
				Integer arrResultado [] = {0,0,0,0,0};
			    for (File fileHTTP : listFilesXML){
			    	rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
			    	bovedaBean.procesarXmlBoveda(con, rc.getEsquema(), session.getEsquemaEmpresa(), fileHTTP, arrResultado, getUsuario(request), true);
			    	con.close();
			    }
			    // se actualiza el metadata para señalar los UUID encotnrados
			    rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				 bovedaBean.actualizarEncontradosBoveda(con, session.getEsquemaEmpresa());
				 try{
					if (con != null){
						con.close();
					}
					con = null;
				  }catch(Exception e){
					con = null;
				  }
				// logger.info("numFilesNG---------------------------->"+numFilesNG);
				 	int numFiles = arrResultado[0] + arrResultado[1] + arrResultado[2] + arrResultado[3];
					int numFilesOK = arrResultado[0];
					int numFilesNG = arrResultado[1];
					int numFilesRFC = arrResultado[2];
					int numFilesXML = arrResultado[3];
					int numFilesNomina = arrResultado[4];
					
	        	Map<String, Object> jsonRetorno = new HashMap<String, Object>();
	        	if (numFilesNG > 0 || numFilesRFC > 0 || numFilesXML > 0){
	        		jsonRetorno.put("ESTATUS", "OK_CON_ERROR");
	        		jsonRetorno.put("MENSAJE",  "Total de Archivos : "+numFiles +"<br> Archivos Exitosos : "+numFilesOK+"<br> Archivos Duplicados : "+numFilesNG+ "<br> Archivos con error en RFC : " + numFilesRFC+ "<br> Archivos con error en XML : " + numFilesXML+ "<br> Archivos de Nomina : "+numFilesNomina);
	        		
	        	}else {
	        		jsonRetorno.put("ESTATUS", "OK");
	        		jsonRetorno.put("MENSAJE",  "Total de Archivos : "+numFiles +"<br> Archivos Exitosos : "+numFilesOK+"<br> Archivos Duplicados : "+numFilesNG+ "<br> Archivos con error en RFC : " + numFilesRFC+ "<br> Archivos con error en XML : " + numFilesXML+ "<br> Archivos de Nomina : "+numFilesNomina);
	        	}
	            out.print(JSONObject.toJSONString(jsonRetorno));
	            out.flush();
	            out.close();
	           // logger.info("JsonRespuesta===>"+JSONObject.toJSONString(jsonRetorno));
	        }
			logger.info("Tiempo Final.............."+System.currentTimeMillis());
			
		}catch(Exception e){
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
		return null;
	}
	
	
	public String eliminarBoveda() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +  session.getEsquemaEmpresa() + File.separator +  "BOVEDA_EMITIDOS" + File.separator;
				
				 int eliminado = bovedaBean.eliminaBoveda(con, session.getEsquemaEmpresa(), getIdRegistro(), rutaBoveda);
				 Map<String, Object> json = new HashMap<String, Object>();
				    if (eliminado == 1) {
				    	json.put("codError", "000");
				    	json.put("mensajeError", "El registro se ha guardado satisfactoriamente.");
				    }else {
				    	json.put("codError", "001");
				    	json.put("mensajeError", "Error al eliminar el registro, consulte a su administrador.");
				    }
				    // json.put("ESTATUS", eliminado > 0 ? "000" : "001");
		            out.print(JSONObject.toJSONString(json));
		            out.flush();
		            out.close();
		            
					
			}
		}catch(Exception e){
			Utils.imprimeLog("elimnarBoveda(): ", e);
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
	

	public String procesaDescargarCFDI(){
    	HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	Connection con = null;
		ResultadoConexion rc = null;
    	try{
    		PrintWriter out = response.getWriter();
    		SiarexSession session = ObtenerSession.getSession(request);
    		rc = getConnection(session.getEsquemaEmpresa());
			con = rc.getCon();
			
			logger.info("*********** EXPORTANDO ARCHIVOS DE NOMINA ****************");
			String emailNotificacion = getEmailNotificacion();
			logger.info("*********** emailNotificacion****************"+emailNotificacion);
			AccesoBean accesoBean = new AccesoBean();
			EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());

			UsuariosBean usuariosBean = new UsuariosBean();
	        UsuariosForm usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
	        
			Thread procDescargaRecibidos = new Thread(new Runnable() {
				public void run() {
					procesoDescargaZIP(session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(getFechaInicial()), 
							Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()), Utils.noNulo(getFechaFinal()), Utils.noNulo(getIdRegistro()), 
							emailNotificacion, empresasForm.getRfc(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo(),
							getUsuario(request), usuariosForm.getNombreCompleto());
				}
			});
			procDescargaRecibidos.setName("procDescargaRecibidos");
			procDescargaRecibidos.start();

			response.setContentType("text/html; charset=UTF-8");
	  		response.setCharacterEncoding("UTF-8");
	  		
	  		LenguajeBean lenguajeBean = LenguajeBean.instance();
	  		HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "BOVEDA_EMITIDOS");
	  		
			BovedaModel bovedaModel = new BovedaModel();
	  		bovedaModel.setCodError("000");
	  		
	  		if ("".equalsIgnoreCase(mapaLenguaje.get("ETQ32"))) {
	  			bovedaModel.setMensajeError("El proceso de descarga de facturas se ha iniciado satisfactoriamente. En unos momentos recibirá un correo para descargar el archivo de facturas.");
	  		}else {
	  			bovedaModel.setMensajeError(mapaLenguaje.get("ETQ32"));
	  		}
	  		
	  		org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
			out.print(json);
            out.flush();
            out.close();
		}
    	catch(Exception e){
			Utils.imprimeLog("procesaDescargarCFDI ", e);
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
	

	private String procesoDescargaZIP(String esquemaEmpresa, String rfc, String razonSocial, String  folio, 
			String serie, String fechaInicial, String tipoComprobante, String uuid, String fechaFinal, 
			String idRegistro, String emailNotificacion, String rfcEmpresa, String emailDominio, String pwdCorreo, 
			String usuarioHTTP, String nombreCompleto ) {
		Connection con = null;
		ResultadoConexion rc = null;
		
		String rutaHTML = null;
		BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
		
		String directorioXML = null;
		String logo = "logoToyota.png";
    	String bandLogo = "S";
		try{
			String dateOperator = null;
	  		if ("".equalsIgnoreCase(fechaInicial)) {
	  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
	  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
	  			dateOperator = "bt";
	  		}else {
	  			dateOperator = getDateOperator();
	  		}
	  		dateOperator = "bt";
			//PrintWriter out = response.getWriter();
				rc = getConnection(esquemaEmpresa);
				con = rc.getCon();

				rutaHTML = UtilsPATH.RUTA_PUBLIC_HTML + esquemaEmpresa + File.separator +  "TEMP_PDF" + File.separator;				
				File fileTemp = new File(rutaHTML);
				if (!fileTemp.exists()) {
					fileTemp.mkdirs();
				}
				Date fecha = new Date();
				SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmm");
				String fechaActual = formatDate.format(fecha);
				
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquemaEmpresa + "/BOVEDA_EMITIDOS/";
				String rutaArchivos = "/REPOSITORIOS/"+esquemaEmpresa+"/EXPORTAR/" + fechaActual + "/" + rfcEmpresa + "_EMITIDOS";

				String pathPDF = "";
				ArrayList<BovedaEmitidosForm> listaDetalle  = bovedaBean.detalleBovedaZIP(con, esquemaEmpresa, Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), idRegistro);
				
				bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, esquemaEmpresa, "BANDERA_LOGO_TOYOTA");
				
				if(bandLogo.equalsIgnoreCase("S")) {
					logo = "logoToyota.png";
				} else {
					logo = "logoVacio.png";
				}
				BovedaEmitidosForm bovedaForm = null;
				String rutaDestinoXML = null;
				String rutaDestinoPDF = null;
				final String EXTENCION_XML = ".xml";
				final String EXTENCION_PDF = ".pdf";
				for (int x = 0; x < listaDetalle.size(); x++){
					bovedaForm = listaDetalle.get(x);
						directorioXML = rutaBoveda + bovedaForm.getUuid() + EXTENCION_XML;
						File fileXML = new File(directorioXML); // XML
						rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + bovedaForm.getUuid() + EXTENCION_XML;	
						File fileXMLDest = new File(rutaDestinoXML); // XML
						UtilsFile.moveFileDirectory(fileXML, fileXMLDest, true, true, true, false);
						fileXML = null;
						fileXMLDest = null;
						
						pathPDF = rutaHTML + bovedaForm.getUuid() + EXTENCION_PDF;
						if(!pathPDF.equals("")) {
							try {
								new CreaPDF().GenerarByXML(directorioXML, pathPDF, (rutaBoveda + "/" + logo));
								File filePDF = new File(pathPDF);   // PDF Complemento
								rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + bovedaForm.getUuid() + EXTENCION_PDF;
								File filePDFDest = new File(rutaDestinoPDF); // PDF
								UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
								filePDF = null;
								filePDFDest = null;	
							}catch(Exception e) {
								
							}
						}
				}
			
			// se elimina los fuentes PDF del directorio..
			fileTemp = new File(rutaHTML);
			fileTemp.delete();
			logger.info("El directorio ha sido borrado.....");
			
			String rutaRep = "/REPOSITORIOS/"+esquemaEmpresa+"/EXPORTAR/" + fechaActual + File.separator + rfcEmpresa+"_EMITIDOS" + ".zip";
			String rutaEliminar = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + "/REPOSITORIOS/"+esquemaEmpresa+"/EXPORTAR/" + fechaActual + File.separator + rfcEmpresa+"_EMITIDOS";
			String rutaZippear = "/REPOSITORIOS/"+esquemaEmpresa+"/EXPORTAR/"+fechaActual;
			ZipFiles zipFiles = new ZipFiles();
			String rutaZIP = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaZippear;
			String zipDirName = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaRep;
			File dir = new File(rutaZIP); // origen
			zipFiles.zipDirectory(dir, zipDirName); // se genera el archivo .zip
			boolean bandZIP = true;
			int idArchivo = 0;
	        if (bandZIP){
	        	BovedaNominaBean bovNomina = new BovedaNominaBean();
	        	idArchivo = bovNomina.grabarDescarga(esquemaEmpresa, usuarioHTTP, rutaRep);	
			}
	        
			// String dominio = Utils.getInfoCorreo("DOM");
	        String dominio = UtilsPATH.DOMINIO_PRINCIPAL;
	        String urlZIP = "https://"+dominio+"/siarexDescarga/descargarSiarex.jsp?idArchivo="+idArchivo;
	        // String urlZIP = "http://localhost:8080/siarexDescarga/descargarSiarex.jsp?idArchivo="+idArchivo;
	        
	        logger.info("*********** emailUsuario **************************"+emailNotificacion);
	        String listaCorreos [] = {emailNotificacion};
        	String sbHTML = UtilsHTML.generaHTMLExport(nombreCompleto, urlZIP, dominio);
        	logger.info("*********** sbHTML **************************"+sbHTML);
        	EnviaCorreoGrid.enviarCorreo(null, sbHTML, false, listaCorreos, null,  "Descarga de XML Emitidos", emailDominio, pwdCorreo );
        	
        	logger.info("Eliminando Directorio....."+rutaEliminar);
        	File fileEliminar = new File(rutaEliminar);
            FileUtils.deleteDirectory(fileEliminar);
					        	
		}catch(Exception e){
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
		return SUCCESS;
	}
	
	
	/*
	public String exportBovedaZIP() {
		Connection con = null;
		ResultadoConexion rc = null;
		
    	HttpServletRequest request = ServletActionContext.getRequest();
    	BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
    	
    	String directorioXML = null;
    	ArrayList<String> alFiles = new ArrayList<String>();
    	
    	String logo = "logoToyota.png";
    	String bandLogo = "S";
    	String rutaHTML = null;
		try{
			//PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			} else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				rutaHTML = UtilsPATH.RUTA_PUBLIC_HTML +session.getEsquemaEmpresa() + File.separator +  "TEMP_PDF" + File.separator;				
				File fileTemp = new File(rutaHTML);
				if (!fileTemp.exists()) {
					fileTemp.mkdirs();
				}
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +session.getEsquemaEmpresa() + "/BOVEDA_EMITIDOS/";
				
				String idRegistro = getIdRegistro();
				String pathPDF = "";
				String fechaInicial = Utils.noNulo(getFechaInicial());
		  		String fechaFinal = Utils.noNulo(getFechaFinal());
		  		
		  		
				ArrayList<BovedaEmitidosForm> listaDetalle  = bovedaBean.detalleBovedaZIP(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), idRegistro);
				
				bandLogo = Utils.noNulo(ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA"));
				
				if(bandLogo.equalsIgnoreCase("S")) {
					logo = "logoToyota.png";
				} else {
					logo = "logoVacio.png";
				}
				BovedaEmitidosForm bovedaForm = null;
				final String EXTENCION_XML = ".xml";
				final String EXTENCION_PDF = ".pdf";
				for (int x = 0; x < listaDetalle.size(); x++){
					bovedaForm = listaDetalle.get(x);
						directorioXML = rutaBoveda + bovedaForm.getUuid() + EXTENCION_XML;
						alFiles.add(directorioXML);
						pathPDF = rutaHTML + bovedaForm.getUuid() + EXTENCION_PDF;
						if(!pathPDF.equals("")) {
							try {
								new CreaPDF().GenerarByXML(directorioXML, pathPDF, (rutaBoveda + "/" + logo));
								alFiles.add(pathPDF);	
							}catch(Exception e) {
								
							}
						}
				}
			}
			if (alFiles.isEmpty()){
				addActionMessage("Usurio y/o Pasword Incorrecto!");
				return  ERROR;
			} else{
				ZipFiles zipFiles = new ZipFiles();
				ByteArrayOutputStream dest = zipFiles.zipFiles(alFiles);
				setInputStream(new ByteArrayInputStream(dest.toByteArray()));
			}
			
			// se elimina los fuentes PDF del directorio..
			File fileTemp = new File(rutaHTML);
			fileTemp.delete();
			logger.info("El directorio ha sido borrado.....");
			
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
	
	public String exportBovedaZIP() {
		Connection con = null;
		ResultadoConexion rc = null;
		
    	HttpServletRequest request = ServletActionContext.getRequest();
    	HttpServletResponse response = ServletActionContext.getResponse();
    	BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
    	
    	
    	String rutaHTML = null;
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			PrintWriter out = response.getWriter();
			
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			} else{
				
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				rutaHTML = UtilsPATH.RUTA_PUBLIC_HTML +session.getEsquemaEmpresa() + File.separator +  "TEMP_PDF" + File.separator;				
				File fileTemp = new File(rutaHTML);
				if (!fileTemp.exists()) {
					fileTemp.mkdirs();
				}
				// String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +session.getEsquemaEmpresa() + "/BOVEDA/";
				
				String fechaInicial = Utils.noNulo(getFechaInicial());
		  		String fechaFinal = Utils.noNulo(getFechaFinal());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			dateOperator = "bt";
		  		}else {
		  			dateOperator = getDateOperator();
		  		}
		  		dateOperator = "bt";
		  		
		  		//ArrayList<BovedaEmitidosForm> listaDetalle  = bovedaBean.detalleBoveda(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), getStart(), 20, true);
		  		int pageSize = (getLength() > 0 ? getLength() : 20);

		  		ArrayList<BovedaEmitidosForm> listaDetalle = bovedaBean.detalleBoveda(
		  		    con,
		  		    session.getEsquemaEmpresa(),
		  		    Utils.noNulo(getRfc()),
		  		    Utils.noNulo(getRazonSocial()),
		  		    Utils.noNulo(getFolio()),
		  		    Utils.noNulo(getSerie()),
		  		    Utils.noNulo(fechaInicial),
		  		    Utils.noNulo(getTipoComprobante()),
		  		    Utils.noNulo(getUuid()),
		  		    Utils.noNulo(fechaFinal),
		  		    getStart(),
		  		    pageSize,
		  		    false,
		  		    // operadores texto
		  		    Utils.noNulo(getRfcOperator()),
		  		    Utils.noNulo(getRazonOperator()),
		  		    Utils.noNulo(getSerieOperator()),
		  		    Utils.noNulo(getTipoOperator()),
		  		    Utils.noNulo(getUuidOperator()),
		  		    // fecha
		  		    Utils.noNulo(dateOperator),
		  		    Utils.noNulo(getDateV1()),
		  		    Utils.noNulo(getDateV2()),
		  		    // numéricos
		  		    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
		  		    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
		  		    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
		  		    Utils.noNulo(getIvaOperator()),    Utils.noNulo(getIvaV1()),    Utils.noNulo(getIvaV2()),
		  		    Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
		  		    Utils.noNulo(getIsrOperator()),    Utils.noNulo(getIsrV1()),    Utils.noNulo(getIsrV2()),
		  		    Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2())
		  		);
		  		BovedaEmitidosForm bovedaForm = null;
				ArrayList<String> listaTXT = new ArrayList<String>();
				
				String fecA = UtilsFechas.getFechaActualNumero();
				String nombreReporte = "descargaCFDI_BovedaEmitidos_"+"_"+ session.getEsquemaEmpresa() + "_" +fecA+ ".txt";
				String pathCSV = UtilsPATH.RUTA_PUBLIC_HTML + session.getEsquemaEmpresa() + File.separator + "TEMP_PDF" + File.separator + nombreReporte;
				
				for (int x = 0; x < listaDetalle.size(); x++){
					bovedaForm = listaDetalle.get(x);
					listaTXT.add(bovedaForm.getUuid());
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
				
				// logger.info("validarSAT arriba===>"+validarSAT);
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
				EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());

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
				exportarXMLBean.procesaArchivo(fileXMLDest, session.getEsquemaEmpresa(), getUsuario(request),
						empresasForm.getEmailDominio(), empresasForm.getPwdCorreo(), usuariosForm.getNombreCompleto(),
	                    CORREO_RESPONSABLE, modoAgrupar, validarSAT, complementoSAT, notaCreditoSAT, descargarFacturas, 
	                    descargarComplemento, descargarNotaCredito, tipoBusqueda, rfcProveedor, fechaInicial, fechaFinal, rutaXMLProcesar, codeOperacion );
				
				
			}
			
			BovedaEmitidosModel bovedaModel = new BovedaEmitidosModel();
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
	
	public String convXMLAExcel(){
		SXSSFWorkbook  libro = new SXSSFWorkbook(100); // Keep 100 rows in memory
		SXSSFSheet hoja1 = libro.createSheet("Complementos Recibidas");
		SXSSFSheet hoja2 = libro.createSheet("Ingresos Recibidas");
		SXSSFSheet hoja3 = libro.createSheet("Egresos Recibidas");
		HttpServletRequest request = ServletActionContext.getRequest();
		Connection con = null;
		ResultadoConexion rc = null;
		
		Connection conSAT = null;
		ResultadoConexion rcSAT = null;
		
		// ArrayList<XMLForm> datosPForm = new ArrayList<XMLForm>();
		// ArrayList<XMLForm> datosIForm = new ArrayList<XMLForm>();
		// XMLForm cargasXMLForm = new XMLForm();
		// LeerFactura factura = new LeerFactura();
		int x = 0;
		
		try{
			SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				libro.close();
				return Action.LOGIN;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
					
				rcSAT = getConnectionSAT();
				conSAT = rcSAT.getCon();

		  		String fechaInicial = Utils.noNulo(getFechaInicial());
		  		String fechaFinal = Utils.noNulo(getFechaFinal());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			dateOperator = "bt";
		  		}else {
		  			dateOperator = getDateOperator();
		  		}
		  		dateOperator = "bt";
		  		
		  	   BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
		  	// === PAGO (P) ===
		  	 ArrayList<BovedaEmitidosForm> datosBovedaP = bovedaBean.detalleBovedaEXCEL(
		  	     con,
		  	     session.getEsquemaEmpresa(),
		  	     Utils.noNulo(getRfc()),
		  	     Utils.noNulo(getRazonSocial()),
		  	     Utils.noNulo(getFolio()),
		  	     Utils.noNulo(getSerie()),
		  	     Utils.noNulo(fechaInicial),
		  	     "P",
		  	     Utils.noNulo(getUuid()),
		  	     Utils.noNulo(fechaFinal),
		  	     Utils.noNulo(getIdRegistro()),

		  	     // ----- TEXTO -----
		  	     Utils.noNulo(getRfcOperator()),
		  	     Utils.noNulo(getRazonOperator()),
		  	     Utils.noNulo(getSerieOperator()),
		  	     Utils.noNulo(getTipoOperator()),
		  	     Utils.noNulo(getUuidOperator()),

		  	     // ----- FECHA -----
		  	     Utils.noNulo(dateOperator),
		  	     Utils.noNulo(getDateV1()),
		  	     Utils.noNulo(getDateV2()),

		  	     // ----- NUMÉRICOS -----
		  	     // Folio
		  	     Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
		  	     // Total
		  	     Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
		  	     // Subtotal
		  	     Utils.noNulo(getSubOperator()),   Utils.noNulo(getSubV1()),   Utils.noNulo(getSubV2()),
		  	     // Traslado (usamos IVA como traslado)
		  	     Utils.noNulo(getIvaOperator()),   Utils.noNulo(getIvaV1()),   Utils.noNulo(getIvaV2()),
		  	     // Retenciones (usamos IVA_RET como retenciones)
		  	     Utils.noNulo(getIvaRetOperator()),Utils.noNulo(getIvaRetV1()),Utils.noNulo(getIvaRetV2()),
		  	     // ISR
		  	     Utils.noNulo(getIsrOperator()),   Utils.noNulo(getIsrV1()),   Utils.noNulo(getIsrV2()),
		  	     // Impuestos locales
		  	     Utils.noNulo(getImpLocOperator()),Utils.noNulo(getImpLocV1()),Utils.noNulo(getImpLocV2())
		  	 );

		  	 // === INGRESO (I) ===
		  	 ArrayList<BovedaEmitidosForm> datosBovedaI = bovedaBean.detalleBovedaEXCEL(
		  	     con,
		  	     session.getEsquemaEmpresa(),
		  	     Utils.noNulo(getRfc()),
		  	     Utils.noNulo(getRazonSocial()),
		  	     Utils.noNulo(getFolio()),
		  	     Utils.noNulo(getSerie()),
		  	     Utils.noNulo(fechaInicial),
		  	     "I",
		  	     Utils.noNulo(getUuid()),
		  	     Utils.noNulo(fechaFinal),
		  	     Utils.noNulo(getIdRegistro()),

		  	     // TEXTO
		  	     Utils.noNulo(getRfcOperator()),
		  	     Utils.noNulo(getRazonOperator()),
		  	     Utils.noNulo(getSerieOperator()),
		  	     Utils.noNulo(getTipoOperator()),
		  	     Utils.noNulo(getUuidOperator()),
		  	     // FECHA
		  	     Utils.noNulo(dateOperator),
		  	     Utils.noNulo(getDateV1()),
		  	     Utils.noNulo(getDateV2()),
		  	     // NUMÉRICOS
		  	     Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
		  	     Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
		  	     Utils.noNulo(getSubOperator()),   Utils.noNulo(getSubV1()),   Utils.noNulo(getSubV2()),
		  	     Utils.noNulo(getIvaOperator()),   Utils.noNulo(getIvaV1()),   Utils.noNulo(getIvaV2()),
		  	     Utils.noNulo(getIvaRetOperator()),Utils.noNulo(getIvaRetV1()),Utils.noNulo(getIvaRetV2()),
		  	     Utils.noNulo(getIsrOperator()),   Utils.noNulo(getIsrV1()),   Utils.noNulo(getIsrV2()),
		  	     Utils.noNulo(getImpLocOperator()),Utils.noNulo(getImpLocV1()),Utils.noNulo(getImpLocV2())
		  	 );

		  	 // === EGRESO (E) ===
		  	 ArrayList<BovedaEmitidosForm> datosBovedaE = bovedaBean.detalleBovedaEXCEL(
		  	     con,
		  	     session.getEsquemaEmpresa(),
		  	     Utils.noNulo(getRfc()),
		  	     Utils.noNulo(getRazonSocial()),
		  	     Utils.noNulo(getFolio()),
		  	     Utils.noNulo(getSerie()),
		  	     Utils.noNulo(fechaInicial),
		  	     "E",
		  	     Utils.noNulo(getUuid()),
		  	     Utils.noNulo(fechaFinal),
		  	     Utils.noNulo(getIdRegistro()),

		  	     // TEXTO
		  	     Utils.noNulo(getRfcOperator()),
		  	     Utils.noNulo(getRazonOperator()),
		  	     Utils.noNulo(getSerieOperator()),
		  	     Utils.noNulo(getTipoOperator()),
		  	     Utils.noNulo(getUuidOperator()),
		  	     // FECHA
		  	     Utils.noNulo(dateOperator),
		  	     Utils.noNulo(getDateV1()),
		  	     Utils.noNulo(getDateV2()),
		  	     // NUMÉRICOS
		  	     Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
		  	     Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
		  	     Utils.noNulo(getSubOperator()),   Utils.noNulo(getSubV1()),   Utils.noNulo(getSubV2()),
		  	     Utils.noNulo(getIvaOperator()),   Utils.noNulo(getIvaV1()),   Utils.noNulo(getIvaV2()),
		  	     Utils.noNulo(getIvaRetOperator()),Utils.noNulo(getIvaRetV1()),Utils.noNulo(getIvaRetV2()),
		  	     Utils.noNulo(getIsrOperator()),   Utils.noNulo(getIsrV1()),   Utils.noNulo(getIsrV2()),
		  	     Utils.noNulo(getImpLocOperator()),Utils.noNulo(getImpLocV1()),Utils.noNulo(getImpLocV2())
		  	 );

			    LeerDatosXML leerXML = new LeerDatosXML();
			    ArrayList<Comprobante> listaComplementos = leerXML.leerElementosEmitidos(datosBovedaP, session.getEsquemaEmpresa());
			    ArrayList<Comprobante> listaFacturas = leerXML.leerElementosEmitidos(datosBovedaI, session.getEsquemaEmpresa());
			    ArrayList<Comprobante> listaNotaC = leerXML.leerElementosEmitidos(datosBovedaE, session.getEsquemaEmpresa());

			    ExtraerXMLBean extraerBean = new ExtraerXMLBean();
			    extraerBean.generarExcelComplemento(conSAT, rcSAT.getEsquema(), hoja1, listaComplementos, libro, false, null);
			    extraerBean.generarExcelFacturas(conSAT, rcSAT.getEsquema(), hoja2, listaFacturas, libro, false, null);
			    extraerBean.generarExcelNotaCredito(conSAT, rcSAT.getEsquema(), hoja3, listaNotaC, libro, false, null);
			    
			    try {
			    	AccesoBean accesoBean = new AccesoBean();
					EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
					
			    	String fechaHoy = UtilsFechas.getFechaActualNumero();
			    	reportFile = empresasForm.getRfc() +"_InformacionCompleta_"+fechaHoy.substring(0, 8) + "_" + fechaHoy.substring(8, 12) + ".xlsx";
			    	
			    	logger.info("Generando Reporte de Salido....."+reportFile);
			    	
					ByteArrayOutputStream boas = new ByteArrayOutputStream();
					libro.write(boas);
					setInputStream(new ByteArrayInputStream(boas.toByteArray()));
			    }catch (IOException e) {
			    	Utils.imprimeLog("convXMLAExcel(IOE): ", e);
			    }
			    /*
			    try {
					ByteArrayOutputStream boas = new ByteArrayOutputStream();
					libro.write(boas);
					setInputStream(new ByteArrayInputStream(boas.toByteArray()));
			    }catch (IOException e) {
			    	Utils.imprimeLog("convXMLAExcel(IOE): ", e);
			    }
			    */
			   
			}
		}
		catch(Exception e){
			Utils.imprimeLog("convXMLAExcel(): ", e);
		}
		finally{
		  try{
			if (con != null){
				con.close();
			}
			con = null;
			if (conSAT != null){
				conSAT.close();
			}
			conSAT = null;
		  }catch(Exception e){
			con = null;
			conSAT = null;
		  }
		}
		return SUCCESS;
	}
	
	public String exportExcel() {
		SXSSFWorkbook  myWorkBook = new SXSSFWorkbook(100); // Keep 100 rows in memory
		SXSSFSheet mySheet = myWorkBook.createSheet("Detalle Emitidos");
        
		Connection con = null;
		ResultadoConexion rc = null;
		BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
		try{
	      HttpServletRequest request = ServletActionContext.getRequest();
		  SiarexSession session = ObtenerSession.getSession(request);

  			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
			//  ArrayList<BovedaEmitidosForm> datosBoveda  = bovedaBean.detalleBoveda(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(getFechaInicial()), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()), Utils.noNulo(getFechaFinal()), getStart(), 20, true);
			  
			    String fechaInicial = Utils.noNulo(getFechaInicial());
		  		String fechaFinal = Utils.noNulo(getFechaFinal());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			dateOperator = "bt";
		  		}else {
		  			dateOperator = getDateOperator();
		  		}
		  		
		  		dateOperator = "bt";
			  ArrayList<BovedaEmitidosForm> datosBoveda = bovedaBean.detalleBoveda(
					    con,
					    session.getEsquemaEmpresa(),
					    Utils.noNulo(getRfc()),
					    Utils.noNulo(getRazonSocial()),
					    Utils.noNulo(getFolio()),
					    Utils.noNulo(getSerie()),
					    Utils.noNulo(fechaInicial),
					    Utils.noNulo(getTipoComprobante()),
					    Utils.noNulo(getUuid()),
					    Utils.noNulo(fechaFinal),
					    getStart(),                 // ignorado cuando isExcel=true
					    20,                         // idem; puedes poner 0 si quieres
					    true,                       // isExcel

					    // ==== operadores de texto ====
					    Utils.noNulo(getRfcOperator()),
					    Utils.noNulo(getRazonOperator()),
					    Utils.noNulo(getSerieOperator()),
					    Utils.noNulo(getTipoOperator()),
					    Utils.noNulo(getUuidOperator()),

					    // ==== fecha con operadores ====
					    Utils.noNulo(dateOperator),   // eq, ne, lt, gt, le, ge, bt
					    Utils.noNulo(getDateV1()),         // YYYY-MM-DD
					    Utils.noNulo(getDateV2()),         // YYYY-MM-DD

					    // ==== numéricos ====
					    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
					    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
					    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
					    Utils.noNulo(getIvaOperator()),    Utils.noNulo(getIvaV1()),    Utils.noNulo(getIvaV2()),
					    Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
					    Utils.noNulo(getIsrOperator()),    Utils.noNulo(getIsrV1()),    Utils.noNulo(getIsrV2()),
					    Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2())
					);

			  // new ConvierteEXCEL().toExcelEmitidos(mySheet, datosBoveda, myWorkBook);
			  ConvierteEXCEL convExcel = new ConvierteEXCEL();
			  int REG_NUEVA_PAGINA = 1000000;
			  if (datosBoveda.size() >= REG_NUEVA_PAGINA) {
				  	SXSSFSheet mySheet2 = myWorkBook.createSheet("Detalle Emitidos (2)");
		  			convExcel.toExcelEmitidos(mySheet, datosBoveda, myWorkBook, 0, REG_NUEVA_PAGINA, session.getLenguaje());
		  			convExcel.toExcelEmitidos(mySheet2, datosBoveda, myWorkBook, REG_NUEVA_PAGINA, datosBoveda.size(), session.getLenguaje());
		  		}else {
		  			convExcel.toExcelEmitidos(mySheet, datosBoveda, myWorkBook, 0, datosBoveda.size(), session.getLenguaje());
		  		}
			  
			  try {
				  ByteArrayOutputStream boas = new ByteArrayOutputStream();
				  myWorkBook.write(boas);
				  setInputStream(new ByteArrayInputStream(boas.toByteArray()));
				  myWorkBook.close();
			  }
			  catch (IOException e) {
				  Utils.imprimeLog("exportExcel(IOE): ", e);
			  }
	  }
	  catch (Exception e) {
	    Utils.imprimeLog("exportExcel(e): ", e);
	  }
	  finally{
		  try{
			if (con != null){
				con.close();
			}
			con = null;
		  }
		  catch(Exception e){
			con = null;
		  }
	  }
	  return SUCCESS;
	}
	
	public String generaPDF(int idRegistro, HttpServletRequest request) {
		String pathPDF = "";
		BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
		ResultadoConexion rc = null;
		BovedaEmitidosForm bovedaForm = null;
    	Connection con = null;
    	String documentoPDF = "";
    	String logo = "logoToyota.png";
    	String bandLogo = "S";

		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				//HashMap<String, String> mapaConfig = configAdicionalesBean.obtenerConfiguraciones(con, session.getEsquemaEmpresa());
				
				bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA");
				if(bandLogo.equalsIgnoreCase("S")) {
					logo = "logoToyota.png";
				}
				else {
					logo = "logoVacio.png";
				}
				
				try {
					bovedaForm = bovedaBean.consultaBovedaRegistro(con, session.getEsquemaEmpresa(), idRegistro);	
				}catch(Exception e) {
					//jsonobj = bovedaBean.consultaBovedaUUID(con, session.getEsquemaEmpresa(), idRegistro);
				}
				
				//pathPDF = request.getRealPath(".") + File.separator + "files"+File.separator+"bovedaPDF.pdf";
				pathPDF = request.getSession().getServletContext().getRealPath("/") + File.separator + "files"+File.separator+ bovedaForm.getUuid() + ".pdf";;
				
				// String repBoveda = "/REPOSITORIOS/"+session.getEsquemaEmpresa()+"/BOVEDA/";
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +  session.getEsquemaEmpresa() + File.separator +  "BOVEDA_EMITIDOS" + File.separator;
				String xmlBoveda = rutaBoveda + bovedaForm.getUuid() + ".xml";
				
				new CreaPDF().GenerarByXML(xmlBoveda, pathPDF, (rutaBoveda + "/" + logo));
				// com.itextpdf.xmltopdf.CreaPDF.CreaPDF()
				documentoPDF = "/siarex247/files/"+bovedaForm.getUuid()+".pdf";
			}
		}
		catch(Exception e) {
			Utils.imprimeLog("generaPDF(): ", e);
		}
		finally{
			try{
				if (con != null){
					con.close();
				}
				con = null;
			  }
			  catch(Exception e){
				con = null;
			  }
			}
		return documentoPDF;
	}

	public String generaXML(int idRegistro, HttpServletRequest request) {
		// String pathPDF = "";
		BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
		ResultadoConexion rc = null;
		BovedaEmitidosForm bovedaForm = null;
    	Connection con = null;
    	String documentoXML = "";
		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				try {
					bovedaForm = bovedaBean.consultaBovedaRegistro(con, session.getEsquemaEmpresa(), idRegistro);	
				}catch(Exception e) {
					e.printStackTrace();
					//jsonobj = bovedaBean.consultaBovedaUUID(con, session.getEsquemaEmpresa(), idRegistro);
				}
				
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +  session.getEsquemaEmpresa() + File.separator +  "BOVEDA_EMITIDOS" + File.separator;
				String destFile = request.getSession().getServletContext().getRealPath("/") + File.separator + "files" + File.separator + Utils.dobleEncryptarMD5(bovedaForm.getUuid()) + ".xml";
				String xmlBoveda = rutaBoveda + bovedaForm.getUuid() + ".xml";
				
				// logger.info("xmlBoveda====>"+xmlBoveda);
				documentoXML = "/siarex247/files/"+ Utils.dobleEncryptarMD5(bovedaForm.getUuid()) + ".xml";
				
				File sourceArchivo = new File(xmlBoveda);
				File destArchivo = new File(destFile);
				UtilsFile.moveFileDirectory(sourceArchivo, destArchivo, true, true, true, false);
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
		return documentoXML;
	}

	
	 public String catProveedores(){
			Connection con = null;
			ResultadoConexion rc = null;
			
			HttpServletResponse response = ServletActionContext.getResponse();
	    	HttpServletRequest request = ServletActionContext.getRequest();
	    	BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
	    	
			try{
				PrintWriter out = response.getWriter();
				SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					response.setContentType("text/html; charset=UTF-8");
			  		response.setCharacterEncoding("UTF-8");
			  		
			  		BovedaEmitidosModel bovedaModel = new BovedaEmitidosModel();
			  		ArrayList<BovedaEmitidosForm> listaDetalle  = bovedaBean.comboProveedores(con, rc.getEsquema(), session.getLenguaje());
			  		
			  		bovedaModel.setData(listaDetalle);
			  		
			  		
					org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
					out.print(json);
		            out.flush();
		            out.close();
		          //  logger.info("json==>"+json);
				}
				
	            
			}catch(Exception e){
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
			return SUCCESS;
		}
	 
	 
	 public String consultarFechaMinima() throws Exception {
			Connection con = null;
			ResultadoConexion rc = null;
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			BovedaEmitidosBean bovedaBean = new BovedaEmitidosBean();
			try{
				SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
			    	response.setContentType("text/html; charset=UTF-8");
					response.setCharacterEncoding("UTF-8");

					PrintWriter out = response.getWriter();
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					
					BovedaEmitidosForm bovedaForm = new BovedaEmitidosForm();
					String fechaMinima = bovedaBean.consultarFechaMinima(con, rc.getEsquema());
					String fechaFinal = UtilsFechas.getFechayyyyMMdd();
					
					bovedaForm.setFechaInicial(fechaMinima.substring(0, 10));
					bovedaForm.setFechaFinal(fechaFinal);
					
					org.json.JSONObject json = new org.json.JSONObject(bovedaForm);
					out.print(json);
		            out.flush();
		            out.close();
		            
				}
				
			}catch(Exception e){
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
			return SUCCESS;
		}
		
	 
	public InputStream getInputStream() {
		return inputStream;
	}



	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getReportFile() {
		return reportFile;
	}

	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}
	
	
	public String consultarFechaEmitidos() {
	    HttpServletResponse response = ServletActionContext.getResponse();
	    response.setContentType("application/json; charset=UTF-8");

	    try {
	        PrintWriter out = response.getWriter();
	        HttpServletRequest request = ServletActionContext.getRequest();

	        SiarexSession session = ObtenerSession.getSession(request);
	        if (session == null) return null;

	        ResultadoConexion rc = getConnection(session.getEsquemaEmpresa());
	        Connection con = rc.getCon();

	        BovedaEmitidosBean bean = new BovedaEmitidosBean();
	        String fecha = bean.obtenerUltimaFechaEmitidos(con, rc.getEsquema());

	        // NO hay fecha → NO regreses JSON → AJAX lo tomará como vacío
	        if (fecha == null || fecha.trim().equals("") || fecha.trim().equals("---")) {
	            return null;  
	        }

	        JSONObject json = new JSONObject();
	        json.put("fechaDescarga", fecha);

	        out.print(json.toString());
	        out.flush();

	    } catch (Exception e) {
	        Utils.imprimeLog("consultarFechaEmitidos()", e);
	    }

	    return null;  // ← MUY IMPORTANTE
	}




	
}
