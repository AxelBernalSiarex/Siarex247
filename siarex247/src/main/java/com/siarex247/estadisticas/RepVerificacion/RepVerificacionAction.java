package com.siarex247.estadisticas.RepVerificacion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsPATH;

public class RepVerificacionAction extends RepVerificacionSupport{

	private static final long serialVersionUID = 4436489872800760049L;
	private InputStream inputStream;
	
	public String detalleReporte(){
    	
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		
		RepVerificacionBean repVerificaBean = new RepVerificacionBean();
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
	    	
	    	response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    
			    RepVerificacionModel repModel = new RepVerificacionModel();
			    ArrayList<RepVerificacionForm> listaDetalle = repVerificaBean.detalleBitacora(con, session.getEsquemaEmpresa());
			    
			    repModel.setData(listaDetalle);
			    repModel.setRecordsFiltered(20);
			    repModel.setDraw(-1);
			    repModel.setRecordsTotal(listaDetalle.size());
				JSONObject json = new JSONObject(repModel);
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
	
	
	public String altaReporte() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		RepVerificacionBean repVerificaBean = new RepVerificacionBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
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
				RepVerificacionModel repModel = new RepVerificacionModel();
				logger.info("descripcion : "+getDescripcion());
				logger.info("getValidarFactura : "+getValidarFactura());
				logger.info("getValidarComplemento : "+getValidarComplemento());
				logger.info("getValidarNota : "+getValidarNota());
				logger.info("getFecIni : "+getFecIni());
				logger.info("getFecFin : "+getFecFin());
				logger.info("getTipoFecha : "+getTipoFecha());

				String validaFactura = "N";
				String validarComplemento = "N";
				String validarNota = "N";
				
				if ("on".equalsIgnoreCase(getValidarFactura())) {
					validaFactura = "S";
				}
				if ("on".equalsIgnoreCase(getValidarComplemento())) {
					validarComplemento = "S";
				}
				if ("on".equalsIgnoreCase(getValidarNota())) {
					validarNota = "S";
				}
				
				String fecIni = getFecIni();
				String fecFin = getFecFin();
				
				int idBitacora = repVerificaBean.altaBitacora(con, session.getEsquemaEmpresa(), getDescripcion(), validaFactura, validarComplemento, validarNota, fecIni, fecFin, "PRO", getTipoFecha(), getUsuario(request));
				if (idBitacora == -100) {
					repModel.setCodError("001");
					repModel.setMensajeError("Error el guardar la informacion del registro, consulte a su administrador.");
				}else {
					repModel.setCodError("000");
					repModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					
					String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
					String pathRepositorio = rutaFinal +  session.getEsquemaEmpresa() + File.separator;
					repVerificaBean.procesoVerifica(session.getEsquemaEmpresa(), fecIni, fecFin, validaFactura, validarComplemento, validarNota, pathRepositorio, idBitacora, getTipoFecha());
				}
				
				JSONObject json = new JSONObject(repModel);
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
	

	public String eliminaReporte() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		RepVerificacionBean repVerificaBean = new RepVerificacionBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				PrintWriter out = response.getWriter();
				RepVerificacionModel repModel = new RepVerificacionModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					int totReg = repVerificaBean.eliminaBitacora(con, session.getEsquemaEmpresa(), getIdBitacora());
					if (totReg == -100) {
						repModel.setCodError("001");
						repModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}else {
						repModel.setCodError("000");
						repModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}	
				
				JSONObject json = new JSONObject(repModel);
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
	
	
	
	public String generaReporte() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		RepVerificacionBean repVerificaBean = new RepVerificacionBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		
		HttpServletResponse response = ServletActionContext.getResponse();
		
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				PrintWriter out = response.getWriter();
				
				
				SXSSFWorkbook  libro = new SXSSFWorkbook(100); // Keep 100 rows in memory
				SXSSFSheet hojaReporte = libro.createSheet("1.- ReporteVerificacion");
				
				
				RepVerificacionModel repModel = new RepVerificacionModel();
				
				repVerificaBean.iniciarExcel(rc.getEsquema(), session.getLenguaje(), libro, hojaReporte, getIdBitacora(), getUsuario(request));
				
				
				Date fechaActual = new Date();
				String fecA = null;
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
				fecA = formato.format(fechaActual);
				
				
				String nombreReporte = "ReportVerificacion_"+fecA+".xls";
				
				String destinoArchivo = request.getSession().getServletContext().getRealPath("/") + "files" + File.separator + nombreReporte;

				 
				 logger.info("destinoArchivo===>"+destinoArchivo);
				File fileXLS = new File(destinoArchivo);
				if (fileXLS.exists()) {
					fileXLS.delete();
				}
				
				FileOutputStream elFichero = new FileOutputStream(destinoArchivo);
	            libro.write(elFichero);
	            elFichero.close();
	            libro.close();
	            
	            boolean generoXLS = true;
	            
				if (generoXLS) {
					repModel.setCodError("000");
					repModel.setMensajeError(nombreReporte);
				}else {
					repModel.setCodError("001");
					repModel.setMensajeError("Error al generar el archivo de nomina, consulte a su administrador.");
				}
				
				JSONObject json = new JSONObject(repModel);
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

	
	
	
	
}
