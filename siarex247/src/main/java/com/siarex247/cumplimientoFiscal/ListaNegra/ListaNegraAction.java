package com.siarex247.cumplimientoFiscal.ListaNegra;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ListaNegraAction extends ListaNegraSupport{

	private static final long serialVersionUID = 7225150357462396029L;
	private InputStream inputStream;
	private String reportFile;

	
	public String detalleListaNegra(){
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		
		ListaNegraBean listaNegraBean = new ListaNegraBean();
		
		JSONArray jsonArray  = null;
		
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
	    	
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rc = getConnectionListaNegra();
			   con = rc.getCon();
			   response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
		  		
			 // logger.info("getStart===>"+getStart());
			// logger.info("getDraw===>"+getDraw());
			  // logger.info("annio===>"+getAnioListaNegra());
			   
			   Map<String, Object > mapaRes = listaNegraBean.detalleListaNegra(con, rc.getEsquema(), session.getEsquemaEmpresa(), getRazonSocial(), getRfcListaNegra(), getIdSupuesto(), getIdNombreArticulo(), getIdEstatus(), getAnioListaNegra(), false, getStart(), 20);  
			   jsonArray  = (JSONArray) mapaRes.get("detalle");
	            
			   int totRegistro = listaNegraBean.obtenerTotal(con, rc.getEsquema(), getRazonSocial(), getRfcListaNegra(), getIdSupuesto(), getIdNombreArticulo(), getIdEstatus(), getAnioListaNegra());
			   
	            ListaNegraModel listaNegraModel = new ListaNegraModel();
	            
	             listaNegraModel.setData(jsonArray);
				 listaNegraModel.setRecordsTotal(totRegistro);
				 listaNegraModel.setRecordsFiltered(totRegistro);
				 listaNegraModel.setDraw(getDraw());
				 
				 
				JSONObject json = new JSONObject(listaNegraModel);
				out.print(json);
				out.flush();
				out.close();
				//logger.info("json==>"+json);
				
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
	
	
	public String buscarRFC(){
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		
		ListaNegraBean listaNegraBean = new ListaNegraBean();
		
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
	    	
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rc = getConnectionListaNegra();
			   con = rc.getCon();
			   response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
		  		
			   
		  		String arrGuines [] = getRfcListaNegra().split("-");
		  		StringBuffer sbRFC = new StringBuffer();
		  		
		  		for (int x = 0; x < arrGuines.length; x++) {
		  			sbRFC.append(arrGuines[x]);
		  		}
		  		
			   boolean isEncontro = listaNegraBean.validarEstatus(con, rc.getEsquema(), getRazonSocial(), sbRFC.toString() ,  "", "", "", 0);
			   ListaNegraModel listaNegraModel = new ListaNegraModel();
			   
			   if (isEncontro) {
				   listaNegraModel.setRecordsTotal(1);
			   }else {
				   listaNegraModel.setRecordsTotal(0);   
			   }
	           
				 
	           JSONObject json = new JSONObject(listaNegraModel);
	           out.print(json);
			   out.flush();
			   out.close();
				//logger.info("json==>"+json);
				
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
	

	 public String descargarListaNegraPDF() {
			Connection con = null;
			ResultadoConexion rc = null;
			ListaNegraBean listaNegraBean = new ListaNegraBean();
			try{
		      HttpServletRequest request = ServletActionContext.getRequest();
			  SiarexSession session = ObtenerSession.getSession(request);

	  			  rc = getConnectionListaNegra();
				  con = rc.getCon();

				  try {
					  logger.info("**************** generando el archivo pdf ****************");
					  reportFile = System.currentTimeMillis() + ".pdf";
					  String rutaPDF = UtilsPATH.RUTA_PUBLIC_HTML + session.getEsquemaEmpresa() + File.separator + "TEMP_PDF" + File.separator + reportFile;
					    String arrGuines [] = getRfcListaNegra().split("-");
				  		StringBuffer sbRFC = new StringBuffer();
				  		
				  		for (int x = 0; x < arrGuines.length; x++) {
				  			sbRFC.append(arrGuines[x]);
				  		}
	
				        String tipoPersona = "PERSONA FISICA";
				        if (sbRFC.length() == 12) {
				        	tipoPersona = "PERSONA MORAL";
				        }
				      
				      String imagePathOrClasspath = "/opt/tomcat11/logos/logoRojo.png";
				      Path salida = Paths.get(rutaPDF);
				      
				      UsuariosBean usuariosBean = new UsuariosBean();
				      UsuariosForm usuarioForm = usuariosBean.datosUsuarioEsquema(session.getEsquemaEmpresa(), getUsuario(request));
				      
				      if ("N".equalsIgnoreCase(getExisteListaNegra())) {  // se crea el PDF donde no existe
					        // String arialClasspath = "/fonts/Arial.ttf";
					        new ListaNegraPDFNoEncontrado().crearPDF(salida, imagePathOrClasspath, Utils.noNulo(getRazonSocial()), Utils.noNulo(sbRFC.toString()), tipoPersona, usuarioForm.getNombreCompleto(), 1);
					  }else {
						  logger.info("**************** generando el archivo pdf de SI encontrado ****************");
						  HashMap<String, String> mapaLista =  listaNegraBean.validarEstatusPDF(con, rc.getEsquema(), getRazonSocial(), sbRFC.toString() ,  "", "", "", 0);
						  new ListaNegraPDFEncontrado().crearPDF(mapaLista, salida, imagePathOrClasspath, Utils.noNulo(getRazonSocial()), Utils.noNulo(sbRFC.toString()), tipoPersona, usuarioForm.getNombreCompleto(), 1);
						  // logger.info("mapaLista=====>"+mapaLista);
					  }
					  InputStream imagenDocumento = new FileInputStream(new File(rutaPDF));
			  		  setInputStream(imagenDocumento);
						
				  }
				  catch (IOException e) {
					  Utils.imprimeLog("descargarListaNegraPDF(IOE): ", e);
				  }
		  }
		  catch (Exception e) {
		    Utils.imprimeLog("descargarListaNegraPDF(e): ", e);
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

	
	
	
	public String generarCSV() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		ListaNegraBean listaNegraBean = new ListaNegraBean();
		
		Connection con = null;
		ResultadoConexion rc = null;
		
		PrintWriter out = null;
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
				out = response.getWriter();
				rc = getConnectionListaNegra();
				con = rc.getCon();
				  
				response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
		  		
				ArrayList<String> listaTXT = listaNegraBean.detalleListaNegraCSV(con, rc.getEsquema(), session.getEsquemaEmpresa(), getRazonSocial(), getRfcListaNegra(), getIdSupuesto(), getIdNombreArticulo(), getIdEstatus(), getAnioListaNegra());
				
				Date fechaActual = new Date();
				String fecA = null;
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
				fecA = formato.format(fechaActual);
				
				String nombreReporte = "listaNegraSAT_"+fecA+".csv";
				String pathCSV = UtilsPATH.RUTA_PUBLIC_HTML + nombreReporte;
				UtilsFile.crearArchivoSalto(listaTXT, pathCSV);
				
				InputStream imagenDocumento = new FileInputStream(new File(pathCSV));
	  		    String filePath = request.getSession().getServletContext().getRealPath("/");
				File file = new File(filePath + "/files/", nombreReporte);
				BufferedInputStream in = new BufferedInputStream(imagenDocumento);
				BufferedOutputStream outBuffer  = new BufferedOutputStream(new FileOutputStream(file));

				byte[] data = new byte[8896];
				int len = 0;
				while ((len = in.read(data)) > 0) {
					outBuffer.write(data, 0, len);
				}
				outBuffer.flush();
				outBuffer.close();
				in.close();
				//String documentoCSV = "/siarex247/files/"+nombreReporte;
				
				ListaNegraModel listaNegraModel = new ListaNegraModel();
				
				listaNegraModel.setCodError("000");
				listaNegraModel.setMensajeError(nombreReporte);
				
				JSONObject json = new JSONObject(listaNegraModel);
				out.print(json);
	            out.flush();
	            out.close();
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return SUCCESS;
	}


	public InputStream getInputStream() {
		return inputStream;
	}


	public String getReportFile() {
		return reportFile;
	}


	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}


	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

	
	
}
