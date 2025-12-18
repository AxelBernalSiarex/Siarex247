package com.siarex247.configuraciones.Etiquetas;

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

public class EtiquetasAction extends EtiquetasSupport {

	private static final long serialVersionUID = 3702680182545645L;
	
	
	public String detalleConf() {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	EtiquetasBean etiquetasBean = new EtiquetasBean();
    	try{
    		response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
  		  SiarexSession session = ObtenerSession.getSession(request);	
  		  if ("".equals(session.getEsquemaEmpresa())){
  				return Action.LOGIN;
  		  }else{
  			PrintWriter out = response.getWriter();
		    rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();
		    JSONArray jsonArray  = null;
            Map<String, Object > mapaRes = null;
            mapaRes  = etiquetasBean.detalleConfiguracion(con, session.getEsquemaEmpresa());
            									   
            jsonArray  = (JSONArray) mapaRes.get("detalle");
          
            Map<String, Object> json = new HashMap<String, Object>();
	        json.put("data", jsonArray);
            String jsonobj = JSONObject.toJSONString(json);
            out.print(jsonobj);
            out.flush();
            out.close(); 
  		  }
		}catch(Exception e){
			Utils.imprimeLog("detalleConf_", e);
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
	
	
	
	public String actualizaConfiguracion(){
		Connection con = null;
		ResultadoConexion rc = null;
		PrintWriter out = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		EtiquetasBean etiquetasBean = new EtiquetasBean();
		try{
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
  		  SiarexSession session = ObtenerSession.getSession(request);	
  		  if ("".equals(session.getEsquemaEmpresa())){
  			  return Action.LOGIN;
  		  }else{
  		    rc = getConnection(session.getEsquemaEmpresa());
  		    con = rc.getCon();
  		    out = response.getWriter();
  		  
  		    UsuariosBean usuarioBean = new UsuariosBean();
  		    UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
  		    
  		    Map<String, Object> json = new HashMap<String, Object>();
  		    if ("ADM".equalsIgnoreCase(usuarioForm.getNombreCortoPerfil())) {
  		    	int res = 0;
  	  		    String activado = "N";
  	  		    if (isActivadaSW()) {
  	  		    	activado = "S";
  	  		    }
  	  		    res = etiquetasBean.actualizaConfiguracion(con, session.getEsquemaEmpresa(), getClaveRegistro(), getEtiqueta(),activado,
  	  		    		 								 getFechaInicial(), getFechaFinal(), getSubject(), getMensajeError(), getUsuario(request));
  	        	
  	        	json.put("ESTATUS", res > 0 ? "OK" : "ERROR");
  	            out.print(JSONObject.toJSONString(json));
  	            out.flush();
  	            out.close();
  		    }else {
  		    	json.put("ESTATUS", "NO_PERMISO");
  	            out.print(JSONObject.toJSONString(json));
  	            out.flush();
  	            out.close();
  		    }
  		    
  		  }
		}
		catch(Exception e){
		  Utils.imprimeLog("actualizaConfiguracion():", e);
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
	
	
	public String buscarConf() {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		EtiquetasBean etiquetasBean = new EtiquetasBean();

		try{
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
  		  SiarexSession session = ObtenerSession.getSession(request);	
  		  if ("".equals(session.getEsquemaEmpresa())){
  				return Action.LOGIN;
  		  }
  		  else{
  			  rc = getConnection(session.getEsquemaEmpresa());
  			  con = rc.getCon();
  			  PrintWriter out = response.getWriter();
  			  	JSONObject jsonResultado  = etiquetasBean.buscarConfMap(con, session.getEsquemaEmpresa(), getClaveRegistro());
	            out.print(JSONObject.toJSONString(jsonResultado));
	            out.flush();
	            out.close(); 
  		  }
		}
		catch(Exception e){
			Utils.imprimeLog("buscarConf(): ", e);
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
	
	
	
	public String detalleEtiquetas(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	Map<String, Object> json = new HashMap<String, Object>();
    	EtiquetasBean etiquetasBean = new EtiquetasBean();
    	try{
    		response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
		  SiarexSession session = ObtenerSession.getSession(request);	
  		  if ("".equals(session.getEsquemaEmpresa())){
  				return Action.LOGIN;
  		  }else{
  			PrintWriter out = response.getWriter();
		    rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();
		    JSONArray jsonArray  = null;
            Map<String, Object > mapaRes = null;
            
            
            // logger.info("etiqueta ==> "+getEtiqueta());
            // logger.info("version ==> "+getVersion());
            
            if (!"".equalsIgnoreCase(getEtiqueta())) {
            	mapaRes  = etiquetasBean.detalleEtiqueta(con, session.getEsquemaEmpresa(), getEtiqueta(), getVersion());	
            }else {
            	mapaRes = new HashMap<>();
            	jsonArray = new JSONArray();
            	mapaRes.put("detalle", jsonArray);
            }
            
            									   
            jsonArray  = (JSONArray) mapaRes.get("detalle");
            json.put("data", jsonArray);
            String jsonobj = JSONObject.toJSONString(json);
            out.print(jsonobj);
            out.flush();
            out.close(); 
  		  }
		}catch(Exception e){
			Utils.imprimeLog("detalleEtiquetas_", e);
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
	
	
	
	public String agregarEtiqueta(){
		Connection con = null;
		ResultadoConexion rc = null;
		PrintWriter out = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		EtiquetasBean etiquetasBean = new EtiquetasBean();
		try{
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
		  SiarexSession session = ObtenerSession.getSession(request);	
  		  if ("".equals(session.getEsquemaEmpresa())){
  				return Action.LOGIN;
  		  }
  		  else{
  		    rc = getConnection(session.getEsquemaEmpresa());
  		    con = rc.getCon();
  		    out = response.getWriter();
  		    
  		    UsuariosBean usuarioBean = new UsuariosBean();
		    UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
		    
		    Map<String, Object> json = new HashMap<String, Object>();
		    if ("ADM".equalsIgnoreCase(usuarioForm.getNombreCortoPerfil())) {
  		    	int res = 0;
  		    	res = etiquetasBean.altaEtiqueta(con, session.getEsquemaEmpresa(), getEtiqueta(), getDatoValida(), getVersion());
	        	json.put("ESTATUS", res > 0 ? "OK" : "ERROR");
	            out.print(JSONObject.toJSONString(json));
	            out.flush();
	            out.close();
		    }else {
		    	json.put("ESTATUS", "NO_PERMISO");
	            out.print(JSONObject.toJSONString(json));
	            out.flush();
	            out.close();
		    }
	      }
	  }
	  catch(Exception e){
		  Utils.imprimeLog("agregarEtiqueta(): ", e);
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
	
	
	public String eliminarRegistro(){
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
		Connection con = null;
		ResultadoConexion rc = null;
		EtiquetasBean etiquetasBean = new EtiquetasBean();
		try{
			
			
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

		    response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();


			    int eliminado = etiquetasBean.eliminaEtiqueta(con, session.getEsquemaEmpresa(), getEtiqueta(), getDatoValida(), getVersion());
			    
			    Map<String, Object> json = new HashMap<String, Object>();
			    json.put("ESTATUS", eliminado > 0 ? "OK" : "ERROR");
	            out.print(JSONObject.toJSONString(json));
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("eliminarRegistro(): ", e);
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
}
