package com.siarex247.configuraciones.ListaNegra;

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
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class ListaNegraAction extends ListaNegraSupport{

	private static final long serialVersionUID = -8332885761439481062L;

	
	
	
	public String detalleLista(){
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
		Connection con = null;
		ResultadoConexion rc = null;
		ListaNegraBean listaNegraBean = new ListaNegraBean();
		Map<String, Object> json = new HashMap<String, Object>();

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
			    JSONArray jsonArray = listaNegraBean.detalleListaConf(con, session.getEsquemaEmpresa());  

	            json.put("data", jsonArray);
			    String jsonobj = JSONObject.toJSONString(json);
	            out.print(jsonobj);
	            out.flush();
	            out.close(); 
			}
		}
		catch(Exception e){
			Utils.imprimeLog("detalleLista(): ", e);
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
	
	
	public String buscarListaNegra(){
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
				rc = getConnectionSiarex();
			    con = rc.getCon();
			    JSONObject jsonobj = listaNegraBean.buscarListaConf(con, session.getEsquemaEmpresa(), getIdRegistro());  
	            out.print(jsonobj);
	            out.flush();
	            out.close(); 
			}
		}
		catch(Exception e){
			Utils.imprimeLog("buscarListaNegra(): ", e);
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
	
	public String altaListaNegra() {
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
				rc = getConnectionSiarex();
			    con = rc.getCon();
			    
			    int totReg = listaNegraBean.altaListaNegra(con, session.getEsquemaEmpresa(), getDiaMes());
			    
	        	Map<String, Object> json = new HashMap<String, Object>();
	        	json.put("ESTATUS", totReg > 0 ? "OK" : "ERROR");
	            out.print(JSONObject.toJSONString(json));
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
	
	
	public String actualizaListaNegra() {
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
				rc = getConnectionSiarex();
			    con = rc.getCon();
			    
			    int totReg = listaNegraBean.modificarListaNegra(con, session.getEsquemaEmpresa(), getIdRegistro(), getDiaMes());
			    
	        	Map<String, Object> json = new HashMap<String, Object>();
	        	json.put("ESTATUS", totReg > 0 ? "OK" : "ERROR");
	            out.print(JSONObject.toJSONString(json));
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
	
	public String eliminarRegistro(){
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
		Connection con = null;
		ResultadoConexion rc = null;

		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				rc = getConnectionSiarex();
			    con = rc.getCon();

			    int eliminado = new ListaNegraBean().eliminaListaNegra(con, session.getEsquemaEmpresa(), getIdRegistro());

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
