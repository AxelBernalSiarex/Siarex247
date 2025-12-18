package com.siarex247.cumplimientoFiscal.ConciliacionCP;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class ConciliacionCPAction extends ConciliacionCPSupport{

	private static final long serialVersionUID = -101979748508392313L;

	
	 public String detalleCartaPorte(){
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
			Connection con = null;
			ResultadoConexion rc = null;
			ConciliacionCPBean cartasBean = new ConciliacionCPBean();

			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
		    	
		    	Map<String, Object > mapaRes = null;
		    	JSONArray jsonArray  = null;
		    	
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}
				else{
				    rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    String uuid_XML = request.getParameter("UUID");
				    if (uuid_XML == null) {
				    	uuid_XML = "";
				    }
				    
				    //int anio = getAnio();
	                String razonSocial = Utils.noNulo(getRazonSocial());
	                String mesCombo  = Utils.noNulo(getMesCombo());
	                String tipoComple = Utils.noNulo(getTipoComple());
	                
	                String fechaIni  = null;
	                String fechaFin  = null;
	                
	                if ("".equals(tipoComple)) {
	                	tipoComple = "CON_SIN_COMPLE";
	                }
	                
	                if (mesCombo.equalsIgnoreCase("NONE") || "".equals(mesCombo)) {
	                	fechaIni = "2010-01-01 01:01:01";
	                	fechaFin = "2050-12-31 23:59:59";
	                }
	                else {
	                	fechaIni = "2010-01-01 01:01:01";
	                	fechaFin = "2050-12-31 23:59:59";
	                }
	                
	                UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					int claveProveedor = 0;
					if (usuarioForm.getIdPerfil() == 4) {
						claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
					}
					
	                
				    mapaRes = cartasBean.detalleCartaPorte(con, session.getEsquemaEmpresa(), fechaIni, fechaFin, razonSocial, claveProveedor, tipoComple);
				   
				    jsonArray  = (JSONArray) mapaRes.get("detalle");
		            
		            Map<String, Object> json = new HashMap<String, Object>();
	            	json.put("data", jsonArray);// rows  list
		            
		            String jsonobj = JSONObject.toJSONString(json);
		            out.print(jsonobj);
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
	 
	 
	 
	 public String generaPDF(int folioOrden, int claveProveedor, HttpServletRequest request) {
			ResultadoConexion rc = null;
	    	Connection con = null;
	    	String documentoPDF = "";
	    	
	    	ConciliacionCPBean cartasBean = new ConciliacionCPBean();
			JSONObject jsonObject = null;
	    	
			try {
				SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					jsonObject =  cartasBean.buscarDocumentoFolioEmpresa(con, session.getEsquemaEmpresa(), folioOrden);
					
					String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
					String repCartas = session.getEsquemaEmpresa()+"/PROVEEDORES/" + File.separator + claveProveedor + File.separator ;
					
					String destFile = request.getSession().getServletContext().getRealPath("/") + File.separator + "files" + File.separator + "cartaPDF.pdf";
					
					logger.info("destFile====>"+destFile);
					String rutaCarta = rutaFinal + repCartas;
					String pdfCartas = rutaCarta + jsonObject.get("NOMBRE_PDF");
					logger.info("pdfCartas====>"+pdfCartas);
					
					documentoPDF = "/siarex247/files/cartasPDF.pdf";
					logger.info("documentoPDF====>"+documentoPDF);
					
					File sourceArchivo = new File(pdfCartas);
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
			return documentoPDF;
		}
		 
		 public String generaXML(int folioOrden, int claveProveedor, HttpServletRequest request) {
			ResultadoConexion rc = null;
	    	Connection con = null;
	    	String documentoXML = "";
	    	
	    	ConciliacionCPBean cartasBean = new ConciliacionCPBean();
			JSONObject jsonObject = null;
	    	
			try {
				SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					logger.info("folioOrden====>"+folioOrden);
					logger.info("claveProveedor====>"+claveProveedor);
					
					jsonObject =  cartasBean.buscarDocumentoFolioEmpresa(con, session.getEsquemaEmpresa(), folioOrden);
					logger.info("jsonObject====>"+jsonObject);
					
					String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
					String repCartas = session.getEsquemaEmpresa()+"/PROVEEDORES/" + File.separator + claveProveedor + File.separator ;
					
					String destFile = request.getSession().getServletContext().getRealPath("/") + File.separator + "files" + File.separator + "cartasXML.xml";
					
					logger.info("destFile====>"+destFile);
					String rutaCartas = rutaFinal + repCartas;
					String xmlCartas = rutaCartas + jsonObject.get("NOMBRE_XML");
					logger.info("xmlCartas====>"+xmlCartas);
					
					documentoXML = "/siarex247/files/cartasXML.xml";
					logger.info("documentoXML====>"+documentoXML);
					
					File sourceArchivo = new File(xmlCartas);
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
		 
}
