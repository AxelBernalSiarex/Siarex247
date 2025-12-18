package com.siarex247.configuraciones.ConfClaveUsoCFDI;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;

public class ConfClaveUsoCFDIAction extends ConfClaveUsoCFDISupport {

	private static final long serialVersionUID = 9136452512727255293L;

	
	
	public String detalleUso(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCFDIBean confClaveUsoCFDIBean = new ConfClaveUsoCFDIBean();
    	JSONArray jsonArray  = null;

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    Map<String, Object > mapaRes = confClaveUsoCFDIBean.detalleUso(con, session.getEsquemaEmpresa());
			    jsonArray  = (JSONArray) mapaRes.get("detalle");
	            
	            Map<String, Object> json = new HashMap<String, Object>();
	            
	        	json.put("data", jsonArray);
	            
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
	
	
	public String buscarConfUso(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCFDIBean confClaveUsoCFDIBean = new ConfClaveUsoCFDIBean();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
				
				JSONObject json = confClaveUsoCFDIBean.buscarUso(con, session.getEsquemaEmpresa(), getIdRegistro());
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
	
	@SuppressWarnings("unchecked")
	public String agregaRegistro(){
	  Connection con = null;
	  ResultadoConexion rc = null;
	
	  HttpServletResponse response = ServletActionContext.getResponse();
	  HttpServletRequest request = ServletActionContext.getRequest();
	  ConfClaveUsoCFDIBean confClaveUsoCFDIBean = new ConfClaveUsoCFDIBean();

	try{
		PrintWriter out = response.getWriter();
	    SiarexSession session = ObtenerSession.getSession(request);
		if ("".equals(session.getEsquemaEmpresa())){
			return Action.LOGIN;
		}
		else{
		    rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon(); 
		    JSONObject json = new JSONObject();
		    int totReg = 0;
	    	if ("SIN DESCRIPCION".equalsIgnoreCase(confClaveUsoCFDIBean.getDescripcionUSOCFDI(con, session.getEsquemaEmpresa(), getUsoCFDI()))) {
	    		json.put("ESTATUS", "NO_USO");
	    	}
	    	else if ("SIN DESCRIPCION".equalsIgnoreCase(confClaveUsoCFDIBean.getDescripcionClaveProd(con, session.getEsquemaEmpresa(), getClaveProducto()))) {
	    		json.put("ESTATUS", "NO_CLAVE");
	    	} else {
	    		totReg = confClaveUsoCFDIBean.grabarUso(con, session.getEsquemaEmpresa(), getRfc(), getUsoCFDI(), getClaveProducto(), getUsuario(request));
	        	json.put("ESTATUS", totReg > 0 ? "OK" : "ERROR");
	    	}
		    
	    	out.print(json);
            out.flush();
            out.close();
		}
	}
	catch(Exception e){
		Utils.imprimeLog("agregaRegistro(): ", e);
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
	return null;
  }
	
	@SuppressWarnings("unchecked")
	public String modificaRegistro(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCFDIBean confClaveUsoCFDIBean = new ConfClaveUsoCFDIBean();

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    JSONObject json = new JSONObject();
			    int totReg = 0;
			    
			    if ("SIN DESCRIPCION".equalsIgnoreCase(confClaveUsoCFDIBean.getDescripcionUSOCFDI(con, session.getEsquemaEmpresa(), getUsoCFDI()))) {
		    		json.put("ESTATUS", "NO_USO");
		    	}
		    	else if ("SIN DESCRIPCION".equalsIgnoreCase(confClaveUsoCFDIBean.getDescripcionClaveProd(con, session.getEsquemaEmpresa(), getClaveProducto()))) {
		    		json.put("ESTATUS", "NO_CLAVE");
		    	}
		    	else {
		    		totReg = confClaveUsoCFDIBean.updateUso(con, session.getEsquemaEmpresa(), getRfc(), getUsoCFDI(), getClaveProducto(), getIdRegistro(), getUsuario(request));
			        json.put("ESTATUS", totReg > 0 ? "OK" : "ERROR");
		    	}
			   
	            out.print(JSONObject.toJSONString(json));
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("modificaRegistro(): ", e);
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
	
	
	public String eliminarRegistro(){
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
		Connection con = null;
		ResultadoConexion rc = null;
		ConfClaveUsoCFDIBean confClaveUsoCFDIBean = new ConfClaveUsoCFDIBean();
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();

			    int eliminado = confClaveUsoCFDIBean.deleteUso(con, session.getEsquemaEmpresa(), getIdRegistro(), getUsuario(request));

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
	
	public String obtenerProveedores(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCFDIBean confClaveUsoCFDIBean = new ConfClaveUsoCFDIBean();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    
			    JSONArray jsonArray = confClaveUsoCFDIBean.comboProveedores(con, session.getEsquemaEmpresa());
				out.print(jsonArray);
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
	
	
	
	public String grabarUsoCFDI(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCFDIBean confClaveUsoCFDIBean = new ConfClaveUsoCFDIBean();
    	try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    confClaveUsoCFDIBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALIDACION_USOCFDI");
			    int res = confClaveUsoCFDIBean.insertVarible(con, session.getEsquemaEmpresa(), "VALIDACION_USOCFDI", "Validacion de Uso de CFDI", getTIPO_VALIDACION());
			    ConfigAdicionalesBean.setMapaVariables();
			    
	        	Map<String, Object> json = new HashMap<String, Object>();
	        	json.put("ESTATUS", res > 0 ? "OK" : "ERROR");
	            out.print(JSONObject.toJSONString(json));
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("grabarUsoCFDI(): ", e);
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
	
	
	
	public String buscarValidarPor(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCFDIBean confClaveUsoCFDIBean = new ConfClaveUsoCFDIBean();
    	try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    HashMap<String, String> mapaConfig =  confClaveUsoCFDIBean.buscarValidarPor(con, session.getEsquemaEmpresa());
			    
	            out.print(JSONObject.toJSONString(mapaConfig));
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("grabarUsoCFDI(): ", e);
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
	
	
	
	public String importProveedores(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCFDIBean confClaveUsoCFDIBean = new ConfClaveUsoCFDIBean();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
				String bandElimina = request.getParameter("bandElimina");
				
				Part filePart = request.getPart("fileTXT");
	          	  File fileTXT = UtilsFile.getFileFromPart(filePart);
	          	  
				String mensaje = confClaveUsoCFDIBean.importarProvTXT(con, session.getEsquemaEmpresa(), fileTXT, bandElimina, getUsuario(request));

	        	Map<String, Object> json = new HashMap<String, Object>();
	        	json.put("ESTATUS", mensaje.equals("") ? "ERROR" : "OK");
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
}
