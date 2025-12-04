package com.siarex247.cumplimientoFiscal.ListaNegraEmpresa;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class ListaNegraEmpresaAction extends ListaNegraEmpresaSupport{

	private static final long serialVersionUID = 7225150357462396029L;
	private InputStream inputStream;
	private String reportFile;

	
	public String detalleListaNegra(){
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		
		ListaNegraEmpresaBean listaNegraBean = new ListaNegraEmpresaBean();
		
		JSONArray jsonArray  = null;
		
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
	    	
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rc = getConnectionSiarex();
			   con = rc.getCon();
			   response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
		  		
			 // logger.info("getStart===>"+getStart());
			// logger.info("getDraw===>"+getDraw());
			  // logger.info("annio===>"+getAnioListaNegra());
			   
			   Map<String, Object > mapaRes = listaNegraBean.detalleListaNegra(con, session.getEsquemaEmpresa(), session.getEsquemaEmpresa(), getRazonSocial(), getRfcListaNegra(), getIdSupuesto(), getIdNombreArticulo(), getIdEstatus(), getAnioListaNegra(), getTipoFactura(), false, getStart(), 50);  
			   jsonArray  = (JSONArray) mapaRes.get("detalle");
	            
			   int totRegistro = listaNegraBean.obtenerTotal(con, rc.getEsquema(), session.getEsquemaEmpresa(), getRazonSocial(), getRfcListaNegra(), getIdSupuesto(), getIdNombreArticulo(), getIdEstatus(), getAnioListaNegra(), getTipoFactura());
			   
	            ListaNegraEmpresaModel listaNegraModel = new ListaNegraEmpresaModel();
	            
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
		
		ListaNegraEmpresaBean listaNegraBean = new ListaNegraEmpresaBean();
		
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
	    	
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rc = getConnectionSiarex();
			   con = rc.getCon();
			   response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
		  		
			    
			   int totRegistro = listaNegraBean.obtenerTotal(con, rc.getEsquema(), session.getEsquemaEmpresa(), "", getRfcListaNegra(), "", "", "", 2025, getTipoFactura());
			   ListaNegraEmpresaModel listaNegraModel = new ListaNegraEmpresaModel();
	           listaNegraModel.setRecordsTotal(totRegistro);
				 
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
			ListaNegraEmpresaPDF listaPDF = new ListaNegraEmpresaPDF();
			try{
		      HttpServletRequest request = ServletActionContext.getRequest();
			  SiarexSession session = ObtenerSession.getSession(request);

	  			  rc = getConnection(session.getEsquemaEmpresa());
				  con = rc.getCon();

				  try {
					  logger.info("**************** generando el archivo pdf ****************");
					  reportFile = System.currentTimeMillis() + ".pdf";
					  String rutaPDF = UtilsPATH.RUTA_PUBLIC_HTML + session.getEsquemaEmpresa() + File.separator + "TEMP_PDF" + File.separator + reportFile;
					  listaPDF.creaPDF(rutaPDF);
					  					  
					  InputStream imagenDocumento = new FileInputStream(new File(rutaPDF));
			  		  setInputStream(imagenDocumento);
						
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

	
	
	
	public String generarCSV() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		ListaNegraEmpresaBean listaNegraBean = new ListaNegraEmpresaBean();
		
		Connection con = null;
		ResultadoConexion rc = null;
		
		PrintWriter out = null;
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
				out = response.getWriter();
				rc = getConnectionSiarex();
				con = rc.getCon();
				  
				response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
		  		
				ArrayList<String> listaTXT = listaNegraBean.detalleListaNegraCSV(con, rc.getEsquema(), session.getEsquemaEmpresa(), getRazonSocial(), getRfcListaNegra(), getIdSupuesto(), getIdNombreArticulo(), getIdEstatus(), getAnioListaNegra(), getTipoFactura());
				
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
				
				ListaNegraEmpresaModel listaNegraModel = new ListaNegraEmpresaModel();
				
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
