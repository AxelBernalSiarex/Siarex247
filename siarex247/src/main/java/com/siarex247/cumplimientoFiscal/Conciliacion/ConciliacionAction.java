package com.siarex247.cumplimientoFiscal.Conciliacion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Puestos.PuestosBean;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;

public class ConciliacionAction extends ConciliacionSupport{

	private static final long serialVersionUID = 4057581230693839658L;

	
	private InputStream inputStream;
	
	public String detalleConsiliados()
    {
    	
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		
		ConciliacionBean conciliacionBean = new ConciliacionBean();
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
			}else{
				
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    String uuid_XML = request.getParameter("UUID");
			    if (uuid_XML == null) {
			    	uuid_XML = "";
			    }
			    
			    int anio = getAnio();
                String razonSocial = Utils.noNulo(getRazonSocial());
                String mesCombo  = Utils.noNulo(getMesCombo());
                String tipoComple = Utils.noNulo(getTipoComple());
                
                String fechaIni  = null;
                String fechaFin  = null;
                

                if ("".equals(tipoComple)) {
                	tipoComple = "CON_SIN_COMPLE";
                }
                
                /*
                if (mesCombo.equalsIgnoreCase("NONE") || "".equals(mesCombo)) {
                	fechaIni = anio + "-01-01 01:01:01";
                	fechaFin = anio + "-12-31 23:59:59";
                }else {
                	fechaIni = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("01 01:01:01").toString();
                	fechaFin = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("31 23:59:59").toString();
                }
                */
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
                
                UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
                
			    mapaRes = conciliacionBean.detalleConsiliados(con, session.getEsquemaEmpresa(), anio, fechaIni, fechaFin, razonSocial, claveProveedor, tipoComple);
			   
			    jsonArray  = (JSONArray) mapaRes.get("detalle");
	            
	            Map<String, Object> json = new HashMap<String, Object>();
            	json.put("data", jsonArray);// rows  list
	            
            	//logger.info("data:=>>"+json);
	            
	            String jsonobj = JSONObject.toJSONString(json);
	            out.print(jsonobj);
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
	
	
	
	 public String exportComplementosZIP() {
		  Connection con = null;
			ResultadoConexion rc = null;
			//Map<String, Object > mapaRes = null;
			ConciliacionBean conciliacionBean = new ConciliacionBean();
			try{
		      HttpServletRequest request = ServletActionContext.getRequest();
			  SiarexSession session = ObtenerSession.getSession(request);	
	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  }else{
	  			  
	  			  logger.info("************ DESCARGANDO ARCHIVOS ***************");
	  			  
	  			  rc = getConnection(session.getEsquemaEmpresa());
				  con = rc.getCon();
				  SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
				  Date fechaActual = new Date();
				  String fechaHoy = formatDate.format(fechaActual);
				  
				    String uuid_XML = request.getParameter("UUID");
				    if (uuid_XML == null) {
				    	uuid_XML = "";
				    }
				    
				    int anio = getAnio();
	                String razonSocial = Utils.noNulo(getRazonSocial());
	                String mesCombo  = Utils.noNulo(getMesCombo());
	                String tipoComple = Utils.noNulo(getTipoComple());
	                
	                /*
	                if (mesCombo.equalsIgnoreCase("NONE") || "".equals(mesCombo)) {
	                	 if (anio == 0) {
	                		 anio = Integer.parseInt(fechaHoy.substring(0, 4));	 
	                	 }
	                	 mesCombo  = fechaHoy.substring(5, 7);
	                }
	                */
	                String fechaIni  = null;
	                String fechaFin  = null;
	                
	                if (mesCombo.equalsIgnoreCase("NONE") || "".equals(mesCombo)) {
	                	fechaIni = anio + "-01-01 01:01:01";
	                	fechaFin = anio + "-12-31 23:59:59";
	                }else {
	                	fechaIni = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("01 01:01:01").toString();
	                	fechaFin = new StringBuffer().append(anio).append("-").append(mesCombo).append("-").append("31 23:59:59").toString();
	                }
	                
	              String idRegistro =  request.getParameter("idRegistro");
	                
	                UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					int claveProveedor = 0;
					if (usuarioForm.getIdPerfil() == 4) {
						claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
					}
	                
	              ArrayList<ConciliacionForm> listaComple = conciliacionBean.detalleExportarZIP(con, session.getEsquemaEmpresa(), fechaIni, fechaFin, razonSocial, claveProveedor, tipoComple, idRegistro);
	              String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
	              String directorioXML = null;
					String directorioPDF = null;
					String rutaArchivoXML = null;
					String rutaArchivoPDF = null;
					ArrayList<String> alFiles = new ArrayList<String>();
					ConciliacionForm compleForm = null;
					
					for (int x = 0; x < listaComple.size(); x++){
						compleForm = listaComple.get(x);
						directorioXML = session.getEsquemaEmpresa()+"/PROVEEDORES/" + compleForm.getClaveProveedor()+"/"+compleForm.getNombreXML();
						directorioPDF = session.getEsquemaEmpresa()+"/PROVEEDORES/" + compleForm.getClaveProveedor()+"/"+compleForm.getNombrePDF();
						rutaArchivoXML = rutaFinal + directorioXML;
						rutaArchivoPDF = rutaFinal + directorioPDF;
						alFiles.add(rutaArchivoXML);
						alFiles.add(rutaArchivoPDF);
					}
					
					if (alFiles.isEmpty()){
						addActionMessage("Usurio y/o Pasword Incorrecto!");
						return  ERROR;
					}else{
						ZipFiles zipFiles = new ZipFiles();
						ByteArrayOutputStream dest = zipFiles.zipFiles(alFiles);
						setInputStream(new ByteArrayInputStream(dest.toByteArray()));
					}
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
		  return SUCCESS;
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
					int anioFuncion   = 2018; 
					

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
	 
	 
	public InputStream getInputStream() {
		return inputStream;
	}



	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	 
	 
	 
	 
}
