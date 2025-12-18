package com.siarex247.cumplimientoFiscal.ConciliacionBoveda;

import java.io.File;
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

import com.itextpdf.xmltopdf.CreaPDF;
import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Puestos.PuestosBean;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.Conciliacion.ConciliacionForm;
import com.siarex247.cumplimientoFiscal.Conciliacion.ConciliacionModel;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class ConciliacionBovedaAction extends ConciliacionBovedaSupport{

	private static final long serialVersionUID = 7737754247063380436L;

	
	public String detalleConsiliados() {
    	
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		
		ConciliacionBovedaBean conciliadosBovedaBean = new ConciliacionBovedaBean();
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
	    	response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
	    	
	    	
	    	Map<String, Object > mapaRes = null;
	    	
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
                String fechaIni  = null;
                String fechaFin  = null;
                
                String uuid_XML = request.getParameter("UUID");
			    if (uuid_XML == null) {
			    	uuid_XML = "";
			    }
			    
			    int anio = getAnio();
                String mesCombo  = Utils.noNulo(getMesCombo());
                String tipoComple = Utils.noNulo(getTipoComple());
                
                if ("".equals(tipoComple)) {
                	tipoComple = "CON_SIN_COMPLE";
                }
                
                
                if (mesCombo.equalsIgnoreCase("NONE") || "".equals(mesCombo)) {
                	fechaIni = anio + "-01-01 01:01:01";
                	fechaFin = anio + "-12-31 23:59:59";
                }else {
                	if ("02".equalsIgnoreCase(mesCombo) && (anio == 2016 || anio == 2020 || anio == 2024 || anio == 2028 || anio == 2032 || anio == 2036)) {
                		fechaIni = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("01 01:01:01").toString();
                    	fechaFin = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("29 23:59:59").toString();
                	}else if ("02".equalsIgnoreCase(mesCombo)) {
                		fechaIni = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("01 01:01:01").toString();
                    	fechaFin = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("28 23:59:59").toString();
                	}else if ("04".equalsIgnoreCase(mesCombo) || "06".equalsIgnoreCase(mesCombo) || "09".equalsIgnoreCase(mesCombo) || "11".equalsIgnoreCase(mesCombo)) {
                		fechaIni = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("01 01:01:01").toString();
                    	fechaFin = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("30 23:59:59").toString();
                	}else {
                		fechaIni = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("01 01:01:01").toString();
                    	fechaFin = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("31 23:59:59").toString();
                	}
                }
			    // mapaRes = bovedaBean.detalleConsiliados(con, session.getEsquemaEmpresa(), fechaIni, fechaFin);
			   /*
                mapaRes = conciliadosBovedaBean.detalleConsiliados(con, session.getEsquemaEmpresa(), fechaIni, fechaFin, tipoComple);
			    jsonArray  = (JSONArray) mapaRes.get("detalle");
	            Map<String, Object> json = new HashMap<String, Object>();
            	json.put("data", jsonArray);// rows  list
	            String jsonobj = JSONObject.toJSONString(json);
	            out.print(jsonobj);
	            out.flush();
	            out.close(); 
			    */
                ConciliacionBovedaModel conciliacionModel = new ConciliacionBovedaModel();
				ArrayList<ConciliacionBovedaForm> listaDetalle = conciliadosBovedaBean.detalleConsiliados(con, session.getEsquemaEmpresa(), fechaIni, fechaFin, tipoComple);
				conciliacionModel.setData(listaDetalle);
				conciliacionModel.setRecordsFiltered(20);
				conciliacionModel.setDraw(-1);
				conciliacionModel.setRecordsTotal(listaDetalle.size());
				JSONObject json = new JSONObject(conciliacionModel);
				out.print(json);
	            out.flush();
	            out.close();
	            // logger.info("json==>"+json);
	            
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
	
	
	
	public String generaPDF(String idRegistro, HttpServletRequest request) {
		String pathPDF = "";
		ConciliacionBovedaBean bovedaBean = new ConciliacionBovedaBean();
		ResultadoConexion rc = null;
    	org.json.simple.JSONObject jsonobj = null;
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
				
				bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA");
				
				if("S".equalsIgnoreCase(bandLogo)) {
					logo = "logoToyota.png";
				}
				else {
					logo = "logoVacio.png";
				}
				
				try {
					jsonobj = bovedaBean.consultaBovedaRegistro(con, session.getEsquemaEmpresa(), Integer.parseInt(idRegistro));	
				}catch(Exception e) {
					jsonobj = bovedaBean.consultaBovedaUUID(con, session.getEsquemaEmpresa(), idRegistro);
				}
				
				//pathPDF = request.getRealPath(".") + File.separator + "files"+File.separator+"bovedaPDF.pdf";
				pathPDF = request.getSession().getServletContext().getRealPath("/") + File.separator + "files"+File.separator+ jsonobj.get("UUID") + ".pdf";;
				
				String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
				String repBoveda = session.getEsquemaEmpresa()+"/BOVEDA/";
				String rutaBoveda = rutaFinal + repBoveda;
				String xmlBoveda = rutaBoveda + jsonobj.get("XML");
				logger.info("pathPDF ===>" + pathPDF);
				logger.info("rutaBoveda ===>" + rutaBoveda);
				//new CreaPDF(pathPDF ,xmlBoveda, (rutaBoveda + "/" + logo));
				new CreaPDF().GenerarByXML(xmlBoveda, pathPDF, (rutaBoveda + "/" + logo));
				documentoPDF = "/siarex247/files/"+jsonobj.get("UUID")+".pdf";
				
				logger.info("documentoPDF ===>" + documentoPDF);
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
	
	
	public String generaXML(String idRegistro, HttpServletRequest request) {
		// String pathPDF = "";
		ConciliacionBovedaBean bovedaBean = new ConciliacionBovedaBean();
		ResultadoConexion rc = null;
    	org.json.simple.JSONObject jsonobj = null;
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
					jsonobj = bovedaBean.consultaBovedaRegistro(con, session.getEsquemaEmpresa(), Integer.parseInt(idRegistro));	
				}catch(Exception e) { // busca por el UUID
					jsonobj = bovedaBean.consultaBovedaUUID(con, session.getEsquemaEmpresa(), idRegistro);
				}
				
				//pathPDF = request.getRealPath(".") + File.separator + "files"+File.separator+"bovedaPDF.pdf";
				// pathPDF = request.getSession().getServletContext().getRealPath("/") + File.separator + "files"+File.separator+ "bovedaPDF.pdf";;
				
				String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
				String repBoveda = session.getEsquemaEmpresa()+"/BOVEDA/";
				
				String destFile = request.getSession().getServletContext().getRealPath("/") + File.separator + "files" + File.separator + jsonobj.get("XML");
				
				String rutaBoveda = rutaFinal + repBoveda;
				String xmlBoveda = rutaBoveda + jsonobj.get("XML");
				
				documentoXML = "/siarex247/files/"+jsonobj.get("XML");
				
				File sourceArchivo = new File(xmlBoveda);
				File destArchivo = new File(destFile);
				UtilsFile.moveFileDirectory(sourceArchivo, destArchivo, true, true, true, false);
				
				// 	/home/siarex247/jvm/apache-tomcat-9.0.30/domains/siarex247.com/siarex
				// //jvm/apache-tomcat-9.0.30/domains/siarex247.com/siarex/files
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
	
	
	
	 public String comboAnnios() throws Exception {
			Connection con = null;
			ResultadoConexion rc = null;
			PuestosBean puestosBean = new PuestosBean();
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
					Date fechaActual = new Date();
					SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
					String fechaHoy = formatDate.format(fechaActual);

					int anioActual = Integer.parseInt(fechaHoy.substring(0, 4));
					int anioFuncion   = 2017; 
					

					ConciliacionModel conciliacionModel = new ConciliacionModel();
					ArrayList<ConciliacionForm> listaCombo = new ArrayList<>();
					
					for (int x = anioFuncion; x <= anioActual; x++){
						ConciliacionForm conciliacionForm = new ConciliacionForm();
						conciliacionForm.setAnnio(x);
						listaCombo.add(conciliacionForm);	
					}
					
					conciliacionModel.setData(listaCombo);
					org.json.JSONObject json = new org.json.JSONObject(conciliacionModel);
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
