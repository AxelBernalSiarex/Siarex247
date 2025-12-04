package com.siarex247.cumplimientoFiscal.BovedaNomina;

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
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.CreaPDF;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaForm;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaModel;
import com.siarex247.estadisticas.reporteNomina.ReporteNominaBean;
import com.siarex247.estadisticas.reporteNomina.ReporteNominaForm;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.ConvierteEXCEL;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


public class BovedaNominaAction extends BovedaNominaSupport{

	private static final long serialVersionUID = -112258213613275729L;
	private InputStream inputStream;
	
	
	public List <File> filesXMLProceso = new ArrayList<>();
	
	public String detalleBoveda(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	BovedaNominaBean bovedaBean = new BovedaNominaBean();
    	
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
		  		BovedaNominaModel bovedaModel = new BovedaNominaModel();
		  		//ArrayList<BovedaNominaForm> listaDetalle  = bovedaBean.detalleBoveda(con, rc.getEsquema(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), getStart(), 20, false);
		  		//int totalRegistro = bovedaBean.totalRegistros(con, rc.getEsquema(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal));
		  	// ===== Detalle (Nómina) =====
		  		int pageSize = (getLength() > 0 ? getLength() : 20);
		  		ArrayList<BovedaNominaForm> listaDetalle = bovedaBean.detalleBoveda(
		  		    con,
		  		    session.getEsquemaEmpresa(),
		  		    Utils.noNulo(getRfc()),
		  		    Utils.noNulo(getRazonSocial()),
		  		    Utils.noNulo(getFolio()),
		  		    Utils.noNulo(getSerie()),
		  		    Utils.noNulo(fechaInicial),
		  		    Utils.noNulo(getUuid()),
		  		    Utils.noNulo(fechaFinal),
		  		    getStart(),
		  		    pageSize,
		  		    false,
		  		    // operadores texto
		  		    Utils.noNulo(getRfcOperator()),
		  		    Utils.noNulo(getRazonOperator()),
		  		    Utils.noNulo(getSerieOperator()),
		  		    Utils.noNulo(getUuidOperator()),
		  		    // fecha
		  		    Utils.noNulo(dateOperator),
		  		    Utils.noNulo(getDateV1()),
		  		    Utils.noNulo(getDateV2()),
		  		    // numéricos
		  		    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
		  		    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
		  		    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
		  		    Utils.noNulo(getDescOperator()),   Utils.noNulo(getDescV1()),   Utils.noNulo(getDescV2()),
		  		    Utils.noNulo(getPercOperator()),   Utils.noNulo(getPercV1()),   Utils.noNulo(getPercV2()),
		  		    Utils.noNulo(getDedOperator()),    Utils.noNulo(getDedV1()),    Utils.noNulo(getDedV2())
		  		);
		  	// ===== Total de registros (Nómina) =====
		  		int totalRegistro = bovedaBean.totalRegistros(
		  		    con,
		  		    session.getEsquemaEmpresa(),
		  		    Utils.noNulo(getRfc()),
		  		    Utils.noNulo(getRazonSocial()),
		  		    Utils.noNulo(getFolio()),
		  		    Utils.noNulo(getSerie()),
		  		    Utils.noNulo(fechaInicial),
		  		    Utils.noNulo(getUuid()),
		  		    Utils.noNulo(fechaFinal),
		  		    // operadores texto
		  		    Utils.noNulo(getRfcOperator()),
		  		    Utils.noNulo(getRazonOperator()),
		  		    Utils.noNulo(getSerieOperator()),
		  		    Utils.noNulo(getUuidOperator()),
		  		    // fecha
		  		    Utils.noNulo(dateOperator),
		  		    Utils.noNulo(getDateV1()),
		  		    Utils.noNulo(getDateV2()),
		  		    // numéricos
		  		    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
		  		    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
		  		    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
		  		    Utils.noNulo(getDescOperator()),   Utils.noNulo(getDescV1()),   Utils.noNulo(getDescV2()),
		  		    Utils.noNulo(getPercOperator()),   Utils.noNulo(getPercV1()),   Utils.noNulo(getPercV2()),
		  		    Utils.noNulo(getDedOperator()),    Utils.noNulo(getDedV1()),    Utils.noNulo(getDedV2())
		  		);


		  		bovedaModel.setData(listaDetalle);
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
	
	public String consultarFechasNomina() throws Exception {
		/**********ENTRO************/
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		
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
				
				BovedaForm bovedaForm = new BovedaForm();
				String fechaFinal = UtilsFechas.getFechayyyyMMdd();
				String  fechaInicial  = UtilsFechas.restarDiasFecha(fechaFinal, 365);
				
				bovedaForm.setFechaInicial(fechaInicial);
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
	
	public String validarFechas() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		
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
				
				BovedaModel bovedaModel = new BovedaModel();
				
				boolean isMayor =  Utils.compararFechaMayor(Utils.noNulo(getFechaInicial()), Utils.noNulo(getFechaFinal()));
				// logger.info("isMayor====>"+isMayor);
				
				if (!isMayor) {
					bovedaModel.setCodError("001");
					bovedaModel.setMensajeError("La fecha inicial no puede ser mayor que la fecha final.");
				}else {
					boolean bandFechas = UtilsFechas.validaFecha(Utils.noNulo(getFechaInicial()), Utils.noNulo(getFechaFinal()), 366);
					if (bandFechas) {
						bovedaModel.setCodError("001");
						bovedaModel.setMensajeError("Debe especificar rango de fechas menor a 1 año.");
					}else {
						bovedaModel.setCodError("000");
						bovedaModel.setMensajeError("Filtro seleccionado correctamente.");
					}
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

	public String obtenerTotales(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	BovedaNominaBean bovedaBean = new BovedaNominaBean();
    	
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
		  		BovedaNominaModel bovedaModel = new BovedaNominaModel();
		  		//int totalRegistro = bovedaBean.totalRegistros(con, rc.getEsquema(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal));
		  	// ===== Total de registros (Nómina) =====
		  		int totalRegistro = bovedaBean.totalRegistros(
		  		    con,
		  		    session.getEsquemaEmpresa(),
		  		    Utils.noNulo(getRfc()),
		  		    Utils.noNulo(getRazonSocial()),
		  		    Utils.noNulo(getFolio()),
		  		    Utils.noNulo(getSerie()),
		  		  Utils.noNulo(fechaInicial),
		  		    Utils.noNulo(getUuid()),
		  		  Utils.noNulo(fechaFinal),
		  		    // operadores texto
		  		    Utils.noNulo(getRfcOperator()),
		  		    Utils.noNulo(getRazonOperator()),
		  		    Utils.noNulo(getSerieOperator()),
		  		    Utils.noNulo(getUuidOperator()),
		  		    // fecha
		  		    Utils.noNulo(dateOperator),
		  		    Utils.noNulo(getDateV1()),
		  		    Utils.noNulo(getDateV2()),
		  		    // numéricos
		  		    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
		  		    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
		  		    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
		  		    Utils.noNulo(getDescOperator()),   Utils.noNulo(getDescV1()),   Utils.noNulo(getDescV2()),
		  		    Utils.noNulo(getPercOperator()),   Utils.noNulo(getPercV1()),   Utils.noNulo(getPercV2()),
		  		    Utils.noNulo(getDedOperator()),    Utils.noNulo(getDedV1()),    Utils.noNulo(getDedV2())
		  		);

		  		bovedaModel.setRecordsTotal(totalRegistro);
		  		bovedaModel.setCodError("000");
		  		
		  		int TOT_REGISTROS_DESCARGAR = Utils.noNuloINT(ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "TOT_REGISTROS_DESCARGAR"));
		  		logger.info("TOT_REGISTROS_DESCARGAR=====>"+TOT_REGISTROS_DESCARGAR);
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
				BovedaNominaBean bovedaBean = new BovedaNominaBean();
				Collection<Part> colectionPart  = request.getParts();
		        ArrayList<File> listFilesXML = com.siarex247.utils.UtilsFile.getFilesFromPart(colectionPart);
		        
				//LeerXML crea = null;
					Integer arrResultado [] = {0,0,0,0,0};
			    // for (File fileHTTP : getFilesXML()){
			    	rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
			    	bovedaBean.procesarXmlBoveda(con, rc.getEsquema(), session.getEsquemaEmpresa(), listFilesXML, arrResultado, getUsuario(request), true);
			    	// con.close();
			    // }
			    // se actualiza el metadata para señalar los UUID encotnrados
				  bovedaBean.actualizarEncontradosBoveda(con, rc.getEsquema());
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
					
	        	Map<String, Object> jsonRetorno = new HashMap<String, Object>();
	        	if (numFilesNG > 0 || numFilesRFC > 0 || numFilesXML > 0){
	        		jsonRetorno.put("ESTATUS", "OK_CON_ERROR");
	        		jsonRetorno.put("MENSAJE",  "Total de Archivos : "+numFiles +"<br> Archivos Exitosos : "+numFilesOK+"<br> Archivos Duplicados : "+numFilesNG+ "<br> Archivos con error en RFC : " + numFilesRFC+ "<br> Archivos con error en XML : " + numFilesXML);
	        		
	        	}else {
	        		jsonRetorno.put("ESTATUS", "OK");
	        		jsonRetorno.put("MENSAJE",  "Total de Archivos : "+numFiles +"<br> Archivos Exitosos : "+numFilesOK+"<br> Archivos Duplicados : "+numFilesNG+ "<br> Archivos con error en RFC : " + numFilesRFC+ "<br> Archivos con error en XML : " + numFilesXML);
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
		BovedaNominaBean bovedaBean = new BovedaNominaBean();
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
				
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +  session.getEsquemaEmpresa() + File.separator +  "BOVEDA_NOMINA" + File.separator;
				
				 int eliminado = bovedaBean.eliminaBoveda(con, rc.getEsquema(), getIdRegistro(), rutaBoveda);
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
	        
			Thread procDescargaNomina = new Thread(new Runnable() {
				public void run() {
					// exportBovedaZIP();
					
					
					procesoDescargaZIP(session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(getFechaInicial()), 
							Utils.noNulo(getUuid()), Utils.noNulo(getFechaFinal()), Utils.noNulo(getIdRegistro()), 
							emailNotificacion, empresasForm.getRfc(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo(),
							getUsuario(request), usuariosForm.getNombreCompleto());
				}
			});
			procDescargaNomina.setName("procDescargaNomina");
			procDescargaNomina.start();

			response.setContentType("text/html; charset=UTF-8");
	  		response.setCharacterEncoding("UTF-8");
	  		
	  		LenguajeBean lenguajeBean = LenguajeBean.instance();
	  		HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "BOVEDA_NOMINA");
	  		
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
	
	public String procesoDescargaZIP(String esquemaEmpresa, String rfc, String razonSocial, String  folio, 
			String serie, String fechaInicial, String uuid, String fechaFinal, 
			String idRegistro, String emailNotificacion, String rfcEmpresa, String emailDominio, String pwdCorreo, 
			String usuarioHTTP, String nombreCompleto ) {
		Connection con = null;
		ResultadoConexion rc = null;
		
    	BovedaNominaBean bovedaBean = new BovedaNominaBean();
    	
    	String directorioXML = null;
    	// ArrayList<String> alFiles = new ArrayList<String>();
    	
    	String logo = "logoNomina.png";
    	String bandLogo = "S";
    	String rutaHTML = null;
		try{
			//PrintWriter out = response.getWriter();
				rc = getConnection(esquemaEmpresa);
				con = rc.getCon();
				
				String fechaInicial1 = Utils.noNulo(getFechaInicial());
		  		String fechaFinal1 = Utils.noNulo(getFechaFinal());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			 dateOperator = "bt";
		  		}else {
		  			dateOperator = getDateOperator();
		  		}
		  		dateOperator = "bt";
				rutaHTML = UtilsPATH.RUTA_PUBLIC_HTML + esquemaEmpresa + File.separator +  "TEMP_PDF" + File.separator;				
				File fileTemp = new File(rutaHTML);
				if (!fileTemp.exists()) {
					fileTemp.mkdirs();
				}
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +esquemaEmpresa + "/BOVEDA_NOMINA/";
				
				String pathPDF = "";
				
				//ArrayList<BovedaNominaForm> listaDetalle  = bovedaBean.detalleBovedaZIP(con, esquemaEmpresa, Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), idRegistro);
				ArrayList<BovedaNominaForm> listaDetalle = bovedaBean.detalleBovedaZIP(
					    con,
					    esquemaEmpresa,
					    Utils.noNulo(getRfc()),
					    Utils.noNulo(getRazonSocial()),
					    Utils.noNulo(getFolio()),
					    Utils.noNulo(getSerie()),
					    Utils.noNulo(fechaInicial1),
					    Utils.noNulo(getUuid()),
					    Utils.noNulo(fechaFinal1),
					    Utils.noNulo(idRegistro),             // uuids seleccionados separados por ";"

					    // ==== Operadores texto ====
					    Utils.noNulo(getRfcOperator()),
					    Utils.noNulo(getRazonOperator()),
					    Utils.noNulo(getSerieOperator()),
					    Utils.noNulo(getUuidOperator()),

					    // ==== Fecha ====
					    Utils.noNulo(dateOperator),
					    Utils.noNulo(getDateV1()),
					    Utils.noNulo(getDateV2()),

					    // ==== Numéricos ====
					    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
					    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
					    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
					    Utils.noNulo(getDescOperator()),   Utils.noNulo(getDescV1()),   Utils.noNulo(getDescV2()),
					    Utils.noNulo(getPercOperator()),   Utils.noNulo(getPercV1()),   Utils.noNulo(getPercV2()),
					    Utils.noNulo(getDedOperator()),    Utils.noNulo(getDedV1()),    Utils.noNulo(getDedV2())
					);

				// HashMap<String, String> mapaConfig = configAdicionalesBean.obtenerConfiguraciones(con, esquemaEmpresa);
				bandLogo = Utils.noNulo(ConfigAdicionalesBean.obtenerValorVariable(con, esquemaEmpresa, "BANDERA_LOGO_TOYOTA"));
				
				if(bandLogo.equalsIgnoreCase("S")) {
					logo = "logoNomina.png";
				} else {
					logo = "logoVacio.png";
				}
				BovedaNominaForm bovedaForm = null;
				final String EXTENCION_XML = ".xml";
				final String EXTENCION_PDF = ".pdf";
				
				Date fecha = new Date();
				SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmm");
				String fechaActual = formatDate.format(fecha);
				//String rutaArchivos = "/REPOSITORIOS/"+esquemaEmpresa+"/EXPORTAR/" + fechaActual + "/" + usuarioHTTP +"_"+fechaActual;
				String rutaArchivos = "/REPOSITORIOS/"+esquemaEmpresa+"/EXPORTAR/" + fechaActual + "/" + rfcEmpresa + "_PERCEPCIONES_Y_DEDUCCIONES";
				
				String rutaDestinoXML = null;
				String rutaDestinoPDF = null;
				for (int x = 0; x < listaDetalle.size(); x++){
					bovedaForm = listaDetalle.get(x);
						directorioXML = rutaBoveda + bovedaForm.getUuid() + EXTENCION_XML;
						// alFiles.add(directorioXML);
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
								// alFiles.add(pathPDF);
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
				// + File.separator + rfcEmpresa+"_PERCEPCIONES_Y_DEDUCCIONES.zip"
				String rutaRep = "/REPOSITORIOS/"+esquemaEmpresa+"/EXPORTAR/" + fechaActual + File.separator + rfcEmpresa+"_PERCEPCIONES_Y_DEDUCCIONES" + ".zip";
				String rutaEliminar = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + "/REPOSITORIOS/"+esquemaEmpresa+"/EXPORTAR/" + fechaActual + File.separator + rfcEmpresa+"_PERCEPCIONES_Y_DEDUCCIONES";
				String rutaZippear = "/REPOSITORIOS/"+esquemaEmpresa+"/EXPORTAR/"+fechaActual;
				logger.info("*********** iniciando comprension de archivos **************************");
				ZipFiles zipFiles = new ZipFiles();
				String rutaZIP = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaZippear;
				String zipDirName = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaRep;
				File dir = new File(rutaZIP); // origen
				zipFiles.zipDirectory(dir, zipDirName); // se genera el archivo .zip
				logger.info("*********** fin de comprencion de archivos **************************"+rutaZIP);
				boolean bandZIP = true;
				int idArchivo = 0;
		        if (bandZIP){
		        	idArchivo = bovedaBean.grabarDescarga(esquemaEmpresa, usuarioHTTP, rutaRep);	
				}
		        
				// String dominio = Utils.getInfoCorreo("DOM");
		        String dominio = UtilsPATH.DOMINIO_PRINCIPAL;
		        String urlZIP = "https://"+dominio+"/siarexDescarga/descargarSiarex.jsp?idArchivo="+idArchivo;
		        // String urlZIP = "http://localhost:8080/siarexDescarga/descargarSiarex.jsp?idArchivo="+idArchivo;
		        
		        logger.info("*********** emailUsuario **************************"+emailNotificacion);
		        String listaCorreos [] = {emailNotificacion};
	        	String sbHTML = UtilsHTML.generaHTMLExport(nombreCompleto, urlZIP, dominio);
	        	logger.info("*********** sbHTML **************************"+sbHTML);
	        	EnviaCorreoGrid.enviarCorreo(null, sbHTML, false, listaCorreos, null,  "Descarga de Nomina", emailDominio, pwdCorreo );
	        	
	        	logger.info("Eliminando Directorio....."+rutaEliminar);
	        	File fileEliminar = new File(rutaEliminar);
	            FileUtils.deleteDirectory(fileEliminar);
	        	
	        	
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
	
	
	public String exportBovedaZIP() {
		Connection con = null;
		ResultadoConexion rc = null;
		
    	HttpServletRequest request = ServletActionContext.getRequest();
    	BovedaNominaBean bovedaBean = new BovedaNominaBean();
    	
    	String directorioXML = null;
    	ArrayList<String> alFiles = new ArrayList<String>();
    	
    	String logo = "logoNomina.png";
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
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +session.getEsquemaEmpresa() + "/BOVEDA_NOMINA/";
				
				String idRegistro = getIdRegistro();
				String pathPDF = "";
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

			//	ArrayList<BovedaNominaForm> listaDetalle  = bovedaBean.detalleBovedaZIP(con, rc.getEsquema(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), idRegistro);
		  		ArrayList<BovedaNominaForm> listaDetalle = bovedaBean.detalleBovedaZIP(
		  			    con,
		  			    rc.getEsquema(),
		  			    Utils.noNulo(getRfc()),
		  			    Utils.noNulo(getRazonSocial()),
		  			    Utils.noNulo(getFolio()),
		  			    Utils.noNulo(getSerie()),
		  			    Utils.noNulo(fechaInicial),
		  			    Utils.noNulo(getUuid()),
		  			    Utils.noNulo(fechaFinal),
		  			    Utils.noNulo(idRegistro),             // uuids seleccionados separados por ";"

		  			    // ==== Operadores texto ====
		  			    Utils.noNulo(getRfcOperator()),
		  			    Utils.noNulo(getRazonOperator()),
		  			    Utils.noNulo(getSerieOperator()),
		  			    Utils.noNulo(getUuidOperator()),

		  			    // ==== Fecha ====
		  			    Utils.noNulo(dateOperator),
		  			    Utils.noNulo(getDateV1()),
		  			    Utils.noNulo(getDateV2()),

		  			    // ==== Numéricos ====
		  			    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
		  			    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
		  			    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
		  			    Utils.noNulo(getDescOperator()),   Utils.noNulo(getDescV1()),   Utils.noNulo(getDescV2()),
		  			    Utils.noNulo(getPercOperator()),   Utils.noNulo(getPercV1()),   Utils.noNulo(getPercV2()),
		  			    Utils.noNulo(getDedOperator()),    Utils.noNulo(getDedV1()),    Utils.noNulo(getDedV2())
		  			);

				bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA");
				
				if(bandLogo.equalsIgnoreCase("S")) {
					logo = "logoNomina.png";
				} else {
					logo = "logoVacio.png";
				}
				BovedaNominaForm bovedaForm = null;
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

	 
	public String exportExcel() {
		SXSSFWorkbook  myWorkBook = new SXSSFWorkbook(100); // Keep 100 rows in memory
		SXSSFSheet mySheet = myWorkBook.createSheet("Detalle Nomina");
		Connection con = null;
		ResultadoConexion rc = null;
		BovedaNominaBean bovedaBean = new BovedaNominaBean();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
	      HttpServletRequest request = ServletActionContext.getRequest();
		  SiarexSession session = ObtenerSession.getSession(request);
		  
		  response.setContentType("text/html; charset=UTF-8");
	  	  response.setCharacterEncoding("UTF-8");
	  		
  			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
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
			  //ArrayList<BovedaNominaForm> datosBoveda  = bovedaBean.detalleBoveda(con, rc.getEsquema(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(getFechaInicial()), Utils.noNulo(getUuid()), Utils.noNulo(getFechaFinal()), getStart(), 20, true);
			  ArrayList<BovedaNominaForm> datosBoveda = bovedaBean.detalleBoveda(
					    con,
					    rc.getEsquema(),
					    Utils.noNulo(getRfc()),
					    Utils.noNulo(getRazonSocial()),
					    Utils.noNulo(getFolio()),
					    Utils.noNulo(getSerie()),
					    Utils.noNulo(fechaInicial),
					    Utils.noNulo(getUuid()),
					    Utils.noNulo(fechaFinal),
					    getStart(),
					    20,
					    true, // isExcel

					    // ==== Operadores de texto ====
					    Utils.noNulo(getRfcOperator()),
					    Utils.noNulo(getRazonOperator()),
					    Utils.noNulo(getSerieOperator()),
					    Utils.noNulo(getUuidOperator()),

					    // ==== Fecha ====
					    Utils.noNulo(dateOperator),
					    Utils.noNulo(getDateV1()),
					    Utils.noNulo(getDateV2()),

					    // ==== Numéricos ====
					    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
					    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
					    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
					    Utils.noNulo(getDescOperator()),   Utils.noNulo(getDescV1()),   Utils.noNulo(getDescV2()),
					    Utils.noNulo(getPercOperator()),   Utils.noNulo(getPercV1()),   Utils.noNulo(getPercV2()),
					    Utils.noNulo(getDedOperator()),    Utils.noNulo(getDedV1()),    Utils.noNulo(getDedV2())
					);

			  ConvierteEXCEL convExcel = new ConvierteEXCEL();
			int REG_NUEVA_PAGINA = 1000000;
	  		if (datosBoveda.size() >= REG_NUEVA_PAGINA) {
	  			SXSSFSheet mySheet2 = myWorkBook.createSheet("Detalle Nomina (2)");
	  			convExcel.toExcelNomina(mySheet, datosBoveda, myWorkBook, 0, REG_NUEVA_PAGINA, session.getLenguaje());
	  			convExcel.toExcelNomina(mySheet2, datosBoveda, myWorkBook, REG_NUEVA_PAGINA, datosBoveda.size(), session.getLenguaje());
	  		}else {
	  			convExcel.toExcelNomina(mySheet, datosBoveda, myWorkBook, 0, datosBoveda.size(), session.getLenguaje());
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
	
	 
	public String reporteResumen() {
		XSSFWorkbook myWorkBook = new XSSFWorkbook();
        XSSFSheet mySheet = myWorkBook.createSheet("Reporte Resumen");
		
		Connection con = null;
		ResultadoConexion rc = null;
		BovedaNominaBean bovedaBean = new BovedaNominaBean();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
	      HttpServletRequest request = ServletActionContext.getRequest();
		  SiarexSession session = ObtenerSession.getSession(request);
		  
		  response.setContentType("text/html; charset=UTF-8");
	  	  response.setCharacterEncoding("UTF-8");
	  		
		  rc = getConnection(session.getEsquemaEmpresa());
		  con = rc.getCon();
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
		 // ArrayList<BovedaNominaForm> datosBoveda  = bovedaBean.detalleBoveda(con, rc.getEsquema(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(getFechaInicial()), Utils.noNulo(getUuid()), Utils.noNulo(getFechaFinal()), getStart(), 20, true);
		  ArrayList<BovedaNominaForm> datosBoveda = bovedaBean.detalleBoveda(
				    con,
				    rc.getEsquema(),
				    Utils.noNulo(getRfc()),
				    Utils.noNulo(getRazonSocial()),
				    Utils.noNulo(getFolio()),
				    Utils.noNulo(getSerie()),
				    Utils.noNulo(fechaInicial),
				    Utils.noNulo(getUuid()),
				    Utils.noNulo(fechaFinal),
				    getStart(),
				    20,
				    true, // isExcel

				    // ==== Operadores de texto ====
				    Utils.noNulo(getRfcOperator()),
				    Utils.noNulo(getRazonOperator()),
				    Utils.noNulo(getSerieOperator()),
				    Utils.noNulo(getUuidOperator()),

				    // ==== Fecha ====
				    Utils.noNulo(dateOperator),
				    Utils.noNulo(getDateV1()),
				    Utils.noNulo(getDateV2()),

				    // ==== Numéricos ====
				    Utils.noNulo(getFolioOperator()),  Utils.noNulo(getFolioV1()),  Utils.noNulo(getFolioV2()),
				    Utils.noNulo(getTotalOperator()),  Utils.noNulo(getTotalV1()),  Utils.noNulo(getTotalV2()),
				    Utils.noNulo(getSubOperator()),    Utils.noNulo(getSubV1()),    Utils.noNulo(getSubV2()),
				    Utils.noNulo(getDescOperator()),   Utils.noNulo(getDescV1()),   Utils.noNulo(getDescV2()),
				    Utils.noNulo(getPercOperator()),   Utils.noNulo(getPercV1()),   Utils.noNulo(getPercV2()),
				    Utils.noNulo(getDedOperator()),    Utils.noNulo(getDedV1()),    Utils.noNulo(getDedV2())
				);

		  try {
			  ConvierteEXCEL convExcel = new ConvierteEXCEL();
			    int REG_NUEVA_PAGINA = 1000000;
		  		if (datosBoveda.size() >= REG_NUEVA_PAGINA) {
		  			XSSFSheet mySheet2 = myWorkBook.createSheet("Reporte Resumen (2)");
		  			convExcel.toExcelNominaResumen(mySheet, datosBoveda, myWorkBook, 0, REG_NUEVA_PAGINA);
		  			convExcel.toExcelNominaResumen(mySheet2, datosBoveda, myWorkBook, REG_NUEVA_PAGINA, datosBoveda.size());
		  		}else {
		  			convExcel.toExcelNominaResumen(mySheet, datosBoveda, myWorkBook, 0, datosBoveda.size());
		  		}
			  ByteArrayOutputStream boas = new ByteArrayOutputStream();
			  myWorkBook.write(boas);
			  setInputStream(new ByteArrayInputStream(boas.toByteArray()));
			  myWorkBook.close();
		  }
		  catch (IOException e) {
			  Utils.imprimeLog("exportExcel(IOE): ", e);
		  }
	  } catch (Exception e) {
	    Utils.imprimeLog("exportExcel(e): ", e);
	  } finally{
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
	 

	public String reporteDetalle() {

	    XSSFWorkbook myWorkBook = null;
	    Connection con = null, conSAT = null;
	    ResultadoConexion rc = null, rcSAT = null;

	    BovedaNominaBean bovedaBean = new BovedaNominaBean();
	    HttpServletResponse response = ServletActionContext.getResponse();

	    try {
	        HttpServletRequest request = ServletActionContext.getRequest();
	        SiarexSession session = ObtenerSession.getSession(request);

	        response.setContentType("text/html; charset=UTF-8");
	        response.setCharacterEncoding("UTF-8");

	        rc = getConnection(session.getEsquemaEmpresa());
	        con = rc.getCon();
	         String fechaInicial = Utils.noNulo(getOrReq(request, "fechaInicial"));
		     String fechaFinal   = Utils.noNulo(getOrReq(request, "fechaFinal"));
		        
		      String dateOperator    = Utils.noNulo(getOrReq(request, "dateOperator"));
	  		if ("".equalsIgnoreCase(fechaInicial)) {
	  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
	  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
	  			dateOperator = "bt";
	  		}else {
	  			dateOperator = getDateOperator();
	  		}
	  		dateOperator = "bt";
	        // ====== LECTURA DE PARÁMETROS (básicos + DX-like) ======
	        String rfc          = Utils.noNulo(getOrReq(request, "rfc"));
	        String razonSocial  = Utils.noNulo(getOrReq(request, "razonSocial"));
	        String folio        = Utils.noNulo(getOrReq(request, "folio"));
	        String serie        = Utils.noNulo(getOrReq(request, "serie"));
	      //  String fechaInicial = Utils.noNulo(getOrReq(request, "fechaInicial"));
	      //  String fechaFinal   = Utils.noNulo(getOrReq(request, "fechaFinal"));
	        String uuidBoveda   = Utils.noNulo(getOrReq(request, "uuid"));

	        // Selección de filas (UUIDs concatenados con ';')
	        String idRegistro   = Utils.noNulo(getOrReq(request, "idRegistro"));

	        // Operadores/valores TEXTO
	        String rfcOperator     = Utils.noNulo(getOrReq(request, "rfcOperator"));
	        String razonOperator   = Utils.noNulo(getOrReq(request, "razonOperator"));
	        String serieOperator   = Utils.noNulo(getOrReq(request, "serieOperator"));
	        String uuidOperator    = Utils.noNulo(getOrReq(request, "uuidOperator"));

	        // FECHA
	      //  String dateOperator    = Utils.noNulo(getOrReq(request, "dateOperator"));
	        String dateV1          = Utils.noNulo(getOrReq(request, "dateV1"));
	        String dateV2          = Utils.noNulo(getOrReq(request, "dateV2"));

	        // NUMÉRICOS
	        String folioOperator   = Utils.noNulo(getOrReq(request, "folioOperator"));
	        String folioV1         = Utils.noNulo(getOrReq(request, "folioV1"));
	        String folioV2         = Utils.noNulo(getOrReq(request, "folioV2"));

	        String totalOperator   = Utils.noNulo(getOrReq(request, "totalOperator"));
	        String totalV1         = Utils.noNulo(getOrReq(request, "totalV1"));
	        String totalV2         = Utils.noNulo(getOrReq(request, "totalV2"));

	        String subOperator     = Utils.noNulo(getOrReq(request, "subOperator"));
	        String subV1           = Utils.noNulo(getOrReq(request, "subV1"));
	        String subV2           = Utils.noNulo(getOrReq(request, "subV2"));

	        String descOperator    = Utils.noNulo(getOrReq(request, "descOperator"));
	        String descV1          = Utils.noNulo(getOrReq(request, "descV1"));
	        String descV2          = Utils.noNulo(getOrReq(request, "descV2"));

	        String percOperator    = Utils.noNulo(getOrReq(request, "percOperator"));
	        String percV1          = Utils.noNulo(getOrReq(request, "percV1"));
	        String percV2          = Utils.noNulo(getOrReq(request, "percV2"));

	        String dedOperator     = Utils.noNulo(getOrReq(request, "dedOperator"));
	        String dedV1           = Utils.noNulo(getOrReq(request, "dedV1"));
	        String dedV2           = Utils.noNulo(getOrReq(request, "dedV2"));

	        // ====== Ejecuta consulta DETALLE con filtros completos (sin paginar para Excel) ======
	        ArrayList<BovedaNominaForm> datosBoveda = bovedaBean.reporteDetalleXML(
	                con, rc.getEsquema(),
	                rfc, razonSocial, folio, serie,
	                fechaInicial, uuidBoveda, fechaFinal,
	                /* selección de UUIDs */ idRegistro,
	                /* paginación y bandera excel */ 0, 0, true,
	                // ----- operadores/valores -----
	                rfcOperator,   razonOperator, serieOperator, uuidOperator,
	                dateOperator,  dateV1,        dateV2,
	                folioOperator, folioV1,       folioV2,
	                totalOperator, totalV1,       totalV2,
	                subOperator,   subV1,         subV2,
	                descOperator,  descV1,        descV2,
	                percOperator,  percV1,        percV2,
	                dedOperator,   dedV1,         dedV2
	        );

	        // ====== Excel ======
	        rcSAT = getConnectionSAT();
	        conSAT = rcSAT.getCon();

	        ConvierteEXCEL convExcel = new ConvierteEXCEL();
	        myWorkBook = convExcel.toExcelNominaDetalleXML(conSAT, con, rc.getEsquema(), datosBoveda);

	        ByteArrayOutputStream boas = new ByteArrayOutputStream();
	        myWorkBook.write(boas);
	        setInputStream(new ByteArrayInputStream(boas.toByteArray()));

	    } catch (Exception e) {
	        Utils.imprimeLog("reporteDetalle(): ", e);
	    } finally {
	        try {
	            if (myWorkBook != null) myWorkBook.close();
	            if (con != null) con.close();
	            if (conSAT != null) conSAT.close();
	        } catch (Exception ignore) { }
	    }
	    return SUCCESS;
	}

	/** Helper: usa el getter si existe; si no, toma de request */
	private String getOrReq(HttpServletRequest req, String name){
	    try {
	        // Si tu Action ya tiene getters tipo getRfc(), getRazonOperator(), etc.
	        java.lang.reflect.Method m = this.getClass().getMethod("get" + Character.toUpperCase(name.charAt(0)) + name.substring(1));
	        Object v = m.invoke(this);
	        if (v != null) return String.valueOf(v);
	    } catch (Exception ignore) { }
	    return req.getParameter(name);
	}
	 
	
	public String reporteNomina() {

	    SXSSFWorkbook myWorkBook = new SXSSFWorkbook(100); // Keep 100 rows in memory
	    SXSSFSheet mySheet = myWorkBook.createSheet("Reporte Nomina");

	    Connection con = null;
	    ResultadoConexion rc = null;

	    Connection conSAT = null;
	    ResultadoConexion rcSAT = null;

	    ReporteNominaBean reporteNominaBean = new ReporteNominaBean();
	    HttpServletResponse response = ServletActionContext.getResponse();
	    try {
	        HttpServletRequest request = ServletActionContext.getRequest();
	        SiarexSession session = ObtenerSession.getSession(request);

	        response.setContentType("text/html; charset=UTF-8");
	        response.setCharacterEncoding("UTF-8");

	        rc = getConnection(session.getEsquemaEmpresa());
	        con = rc.getCon();
	        String fechaInicial = Utils.noNulo(request.getParameter("fechaInicial"));
	        String fechaFinal   = Utils.noNulo(request.getParameter("fechaFinal"));
	        String dateOperator   = nz(request.getParameter("dateOperator"));
	  		if ("".equalsIgnoreCase(fechaInicial)) {
	  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
	  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
	  			dateOperator = "bt";
	  		}else {
	  			dateOperator = getDateOperator();
	  		}
	  		dateOperator = "bt";
	        rcSAT = getConnectionSAT();
	        conSAT = rcSAT.getCon();

	        // ===== Básicos (compatibilidad legado) =====
	        String rfc          = Utils.noNulo(request.getParameter("rfc"));
	        String razonSocial  = Utils.noNulo(request.getParameter("razonSocial"));
	        String folio        = Utils.noNulo(request.getParameter("folio"));
	        String serie        = Utils.noNulo(request.getParameter("serie"));
	       // String fechaInicial = Utils.noNulo(request.getParameter("fechaInicial"));
	        String uuid         = Utils.noNulo(request.getParameter("uuid"));
	      //  String fechaFinal   = Utils.noNulo(request.getParameter("fechaFinal"));

	        // ===== Operadores/valores DX-like =====
	        String rfcOperator    = nz(request.getParameter("rfcOperator"));
	        String razonOperator  = nz(request.getParameter("razonOperator"));
	        String serieOperator  = nz(request.getParameter("serieOperator"));
	        String uuidOperator   = nz(request.getParameter("uuidOperator"));

	        //String dateOperator   = nz(request.getParameter("dateOperator"));
	        String dateV1         = nz(request.getParameter("dateV1"));
	        String dateV2         = nz(request.getParameter("dateV2"));

	        String folioOperator  = nz(request.getParameter("folioOperator"));
	        String folioV1        = nz(request.getParameter("folioV1"));
	        String folioV2        = nz(request.getParameter("folioV2"));

	        String totalOperator  = nz(request.getParameter("totalOperator"));
	        String totalV1        = nz(request.getParameter("totalV1"));
	        String totalV2        = nz(request.getParameter("totalV2"));

	        String subOperator    = nz(request.getParameter("subOperator"));
	        String subV1          = nz(request.getParameter("subV1"));
	        String subV2          = nz(request.getParameter("subV2"));

	        String descOperator   = nz(request.getParameter("descOperator"));
	        String descV1         = nz(request.getParameter("descV1"));
	        String descV2         = nz(request.getParameter("descV2"));

	        String percOperator   = nz(request.getParameter("percOperator"));
	        String percV1         = nz(request.getParameter("percV1"));
	        String percV2         = nz(request.getParameter("percV2"));

	        String dedOperator    = nz(request.getParameter("dedOperator"));
	        String dedV1          = nz(request.getParameter("dedV1"));
	        String dedV2          = nz(request.getParameter("dedV2"));

	        // ===== Datos para Excel con filtros DX-like =====
	        ArrayList<ReporteNominaForm> datosReporte = reporteNominaBean.detalleReporteNomina(
	            con, rc.getEsquema(),
	            // texto/fecha legado (valor libre)
	            rfc, razonSocial, folio, serie, fechaInicial, uuid, fechaFinal,
	            // operadores/valores DX-like
	            rfcOperator, razonOperator, serieOperator, uuidOperator,
	            dateOperator, dateV1, dateV2,
	            folioOperator, folioV1, folioV2,
	            totalOperator, totalV1, totalV2,
	            subOperator,   subV1,   subV2,
	            descOperator,  descV1,  descV2,
	            percOperator,  percV1,  percV2,
	            dedOperator,   dedV1,   dedV2
	        );

	        ArrayList<String> listaPercepciones = reporteNominaBean.percepcionesReporte(con, rc.getEsquema());
	        ArrayList<String> listaDeducciones  = reporteNominaBean.deduccionesReporte(con, rc.getEsquema());
	        ArrayList<String> listaOtroPagos    = reporteNominaBean.otroPagosReporte(con, rc.getEsquema());

	        int REG_NUEVA_PAGINA = 1000000;
	        if (datosReporte.size() >= REG_NUEVA_PAGINA) {
	            SXSSFSheet mySheet2 = myWorkBook.createSheet("Reporte Nomina (2)");
	            reporteNominaBean.toExcelNomina(conSAT, mySheet,  datosReporte, listaPercepciones, listaDeducciones, listaOtroPagos, myWorkBook, 0, REG_NUEVA_PAGINA);
	            reporteNominaBean.toExcelNomina(conSAT, mySheet2, datosReporte, listaPercepciones, listaDeducciones, listaOtroPagos, myWorkBook, REG_NUEVA_PAGINA, datosReporte.size());
	        } else {
	            reporteNominaBean.toExcelNomina(conSAT, mySheet, datosReporte, listaPercepciones, listaDeducciones, listaOtroPagos, myWorkBook, 0, datosReporte.size());
	        }

	        ByteArrayOutputStream boas = new ByteArrayOutputStream();
	        myWorkBook.write(boas);
	        setInputStream(new ByteArrayInputStream(boas.toByteArray()));
	    }
	    catch (Exception e) {
	        Utils.imprimeLog("reporteNomina()", e);
	    }
	    finally{
	        try{
	            if (myWorkBook != null){
	                myWorkBook.close();
	            }
	            myWorkBook = null;
	            if (con != null){
	                con.close();
	            }
	            con = null;
	            if (conSAT != null){
	                conSAT.close();
	            }
	            conSAT = null;

	        } catch(Exception e){
	            con = null;
	        }
	    }
	    return SUCCESS;
	}

	private static String nz(String s){ return (s==null) ? "" : s; }


	public String generaPDF(int idRegistro, HttpServletRequest request) {
		String pathPDF = "";
		BovedaNominaBean bovedaBean = new BovedaNominaBean();
		ResultadoConexion rc = null;
		BovedaNominaForm bovedaForm = null;
    	Connection con = null;
    	String documentoPDF = "";
    	String logo = "logoNomina.png";
    	String bandLogo = "S";

		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA");
				
				if(bandLogo.equalsIgnoreCase("S")) {
					logo = "logoNomina.png";
				} else {
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
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +  session.getEsquemaEmpresa() + File.separator +  "BOVEDA_NOMINA" + File.separator;
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
		BovedaNominaBean bovedaBean = new BovedaNominaBean();
		ResultadoConexion rc = null;
		BovedaNominaForm bovedaForm = null;
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
				
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +  session.getEsquemaEmpresa() + File.separator +  "BOVEDA_NOMINA" + File.separator;
				String destFile = request.getSession().getServletContext().getRealPath("/")  + File.separator + "files" + File.separator + Utils.dobleEncryptarMD5(bovedaForm.getUuid()) + ".xml";
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
	    	BovedaNominaBean bovedaBean = new BovedaNominaBean();
	    	
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
			  		
			  		BovedaNominaModel bovedaModel = new BovedaNominaModel();
			  		ArrayList<BovedaNominaForm> listaDetalle  = bovedaBean.comboProveedores(con, rc.getEsquema(), session.getLenguaje());
			  		
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
			BovedaNominaBean bovedaBean = new BovedaNominaBean();
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
					
					BovedaNominaForm bovedaForm = new BovedaNominaForm();
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
	
	

	
}


