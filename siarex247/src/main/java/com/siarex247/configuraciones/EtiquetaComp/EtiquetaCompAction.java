package com.siarex247.configuraciones.EtiquetaComp;

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

public class EtiquetaCompAction extends EtiquetaCompSupport{

	private static final long serialVersionUID = 5003890083128325586L;

	
	public String detConfComplementos() {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		EtiquetaCompBean etiquetaCompBean = new EtiquetaCompBean();
	
		try{
			  SiarexSession session = ObtenerSession.getSession(request);	
			  if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
			  }else{
				PrintWriter out = response.getWriter();
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    JSONArray jsonArray  = null;
		        Map<String, Object > mapaRes = null;
		        
		        mapaRes  = etiquetaCompBean.detConfComplementos(con, session.getEsquemaEmpresa(), Utils.noNulo(getVersionXML()));
		        jsonArray  = (JSONArray) mapaRes.get("detalle");
		        Map<String, Object> json = new HashMap<String, Object>();
	            json.put("data", jsonArray);
		        
		        String jsonobj = JSONObject.toJSONString(json);
		       // logger.info("jsonobj===>"+jsonobj);
		        out.print(jsonobj);
		        out.flush();
		        out.close(); 
			  }
		}catch(Exception e){
			Utils.imprimeLog("detConfComplementos: ", e);
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
	
	
	public String buscarConfComp() {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		EtiquetaCompBean etiquetaCompBean = new EtiquetaCompBean();

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
  			  JSONObject jsonResultado  = etiquetaCompBean.buscarConfMap(con, session.getEsquemaEmpresa(), getClaveRegistro());
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
	
	
	
	public String actualizaConfComplementos(){
		Connection con = null;
		ResultadoConexion rc = null;
		PrintWriter out = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		EtiquetaCompBean etiquetaCompBean = new EtiquetaCompBean();
		try{
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
			    
	  		    res = etiquetaCompBean.actualizaConfComp(con, session.getEsquemaEmpresa(), getClaveRegistro(), getEtiqueta(), activado,
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
		  Utils.imprimeLog("actualizaConfComplementos():", e);
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

	
	
	public String detalleEtiquetasComplementos(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	
    	Map<String, Object> json = new HashMap<String, Object>();
    	EtiquetaCompBean etiquetaCompBean = new EtiquetaCompBean();
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
		    JSONArray jsonArray  = null;
            Map<String, Object > mapaRes = null;
            
            // mapaRes  = etiquetaCompBean.detalleEtiquetaComp(con, session.getEsquemaEmpresa(), getEtiqueta(), getVersion());
           // logger.info("etiqueta ==> "+getEtiqueta());
           // logger.info("version ==> "+getVersion());
            			
            if (!"".equalsIgnoreCase(getEtiqueta())) {
            	mapaRes  = etiquetaCompBean.detalleEtiquetaComp(con, session.getEsquemaEmpresa(), getEtiqueta(), getVersion());
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
			Utils.imprimeLog("detalleEtiquetasComplementos: ", e);
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

	public String agregarEtiquetaComplemento(){
		Connection con = null;
		ResultadoConexion rc = null;
		PrintWriter out = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		EtiquetaCompBean etiquetaCompBean = new EtiquetaCompBean();
		try{
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
	  		    res = etiquetaCompBean.altaEtiquetaComp(con, session.getEsquemaEmpresa(), getEtiqueta(), getDatoValida(), getVersion());
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
		  Utils.imprimeLog("agregarEtiquetaComplemento(): ", e);
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
		EtiquetaCompBean etiquetaCompBean = new EtiquetaCompBean();
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

			    //int eliminado = etiquetaCompBean.eliminaEtiqueta(con, session.getEsquemaEmpresa(), getClaveRegistro());
			    int eliminado = etiquetaCompBean.eliminaEtiqueta(con, session.getEsquemaEmpresa(), getEtiqueta(), getDatoValida(), getVersion());
			    
			    
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
