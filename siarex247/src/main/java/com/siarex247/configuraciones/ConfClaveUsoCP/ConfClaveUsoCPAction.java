package com.siarex247.configuraciones.ConfClaveUsoCP;

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
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;

public class ConfClaveUsoCPAction extends ConfClaveUsoCPSupport{

	private static final long serialVersionUID = 1976084938621119717L;

	
	public String detalleCP(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	JSONArray jsonArray  = null;
    	ConfClaveUsoCPBean confClaveUsoCPBean = new ConfClaveUsoCPBean();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    Map<String, Object > mapaRes = confClaveUsoCPBean.detalleUsoCP(con, session.getEsquemaEmpresa());
			    jsonArray  = (JSONArray) mapaRes.get("detalle");
	            
	            Map<String, Object> json = new HashMap<String, Object>();
	            
	        	json.put("data", jsonArray);
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
	 
	public String buscarConfUsoCP(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCPBean confClaveUsoCPBean = new ConfClaveUsoCPBean();
    	try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
				
			    JSONObject json = confClaveUsoCPBean.buscarUsoCP(con, session.getEsquemaEmpresa(), getIdRegistro());
				out.print(json);
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
		  }
		  catch(Exception e){
			con = null;
		  }
		}
		return SUCCESS;
	}
	
	public String movimientoUsoCP(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCPBean confClaveUsoCPBean = new ConfClaveUsoCPBean();

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    Map<String, Object> json = new HashMap<String, Object>();
			    int totReg = 0;
			    
			    if (getAccion() == null || getAccion().equalsIgnoreCase("ALTA")){
			    	if ("SIN DESCRIPCION".equalsIgnoreCase(confClaveUsoCPBean.getDescripcionUSOCFDICP(con, session.getEsquemaEmpresa(), getUsoCFDI()))) {
			    		json.put("ESTATUS", "NO_USO");
			    	}
			    	else if ("SIN DESCRIPCION".equalsIgnoreCase(confClaveUsoCPBean.getDescripcionClaveProdCP(con, session.getEsquemaEmpresa(), getClaveProducto()))) {
			    		json.put("ESTATUS", "NO_CLAVE");
			    	}else {
			    		totReg = confClaveUsoCPBean.grabarUsoCP(con, session.getEsquemaEmpresa(), getRfc(), getUsoCFDI(), getClaveProducto(), getUsuario(request));
			    		json.put("ESTATUS", totReg > 0 ? "OK" : "ERROR");
			    	}
			    }
			    else{
			    	if ("SIN DESCRIPCION".equalsIgnoreCase(confClaveUsoCPBean.getDescripcionUSOCFDICP(con, session.getEsquemaEmpresa(), getUsoCFDI()))) {
			    		json.put("ESTATUS", "NO_USO");
			    	}
			    	else if ("SIN DESCRIPCION".equalsIgnoreCase(confClaveUsoCPBean.getDescripcionClaveProdCP(con, session.getEsquemaEmpresa(), getClaveProducto()))) {
			    		json.put("ESTATUS", "NO_CLAVE");
			    	}
			    	else {
			    		totReg = confClaveUsoCPBean.updateUsoCP(con, session.getEsquemaEmpresa(), getRfc(), getUsoCFDI(), getClaveProducto(), getIdRegistro(), getUsuario(request));
			    		json.put("ESTATUS", totReg > 0 ? "OK" : "ERROR");
			    	}
			    }
	        	
	            out.print(JSONObject.toJSONString(json));
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
		ConfClaveUsoCPBean confClaveUsoCPBean = new ConfClaveUsoCPBean();
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

			    int eliminado = confClaveUsoCPBean.deleteUsoCP(con, session.getEsquemaEmpresa(), getIdRegistro(), getUsuario(request));

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
	
	
	public String importProveedoresCP(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfClaveUsoCPBean confClaveUsoCPBean = new ConfClaveUsoCPBean();

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    Part filePart = request.getPart("fileTXT");
	          	File fileTXT = UtilsFile.getFileFromPart(filePart);
	          	  
				String bandElimina = request.getParameter("bandElimina");
				String mensaje = confClaveUsoCPBean.importarProvTXTCP(con, session.getEsquemaEmpresa(), fileTXT, bandElimina, getUsuario(request));

	        	Map<String, Object> json = new HashMap<String, Object>();
	        	json.put("ESTATUS", mensaje.equals("") ? "ERROR" : "OK");
	            out.print(JSONObject.toJSONString(json));
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
}
