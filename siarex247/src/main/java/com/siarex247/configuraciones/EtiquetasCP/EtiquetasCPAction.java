package com.siarex247.configuraciones.EtiquetasCP;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EtiquetasCPAction extends EtiquetasCPSupport{

	private static final long serialVersionUID = -3804928035362920831L;

	
	public String detConfEtiquetasCP() {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	EtiquetasCPBean etiquetasCPBean = new EtiquetasCPBean();
    	try{
  		  SiarexSession session = ObtenerSession.getSession(request);

  		  if ("".equals(session.getEsquemaEmpresa())){
  			  return Action.LOGIN;
  		  }
  		  else{
  	    	response.setContentType("text/html; charset=UTF-8");
  			response.setCharacterEncoding("UTF-8");
  			  
  			  PrintWriter out = response.getWriter();
		      rc = getConnection(session.getEsquemaEmpresa());
		      con = rc.getCon();
		      JSONArray jsonArray  = null;
		      Map<String, Object > mapaRes = null;
            
		      mapaRes  = etiquetasCPBean.detConfEtiquetasCP(con, session.getEsquemaEmpresa());
            									   
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
			Utils.imprimeLog("detConfEtiquetasCP(): ", e);
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
	
	
	
	 public String buscarConfCP() {
			Connection con = null;
			ResultadoConexion rc = null;
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();
			EtiquetasCPBean etiquetasCPBean = new EtiquetasCPBean();
			try{
	  		  SiarexSession session = ObtenerSession.getSession(request);	
	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  }
	  		  else{
	  			  
	  			response.setContentType("text/html; charset=UTF-8");
	  			response.setCharacterEncoding("UTF-8");
	  			
	  			  rc = getConnection(session.getEsquemaEmpresa());
	  			  con = rc.getCon();
	  			  PrintWriter out = response.getWriter();
	  			  
	  			  JSONObject jsonobj   = etiquetasCPBean.buscarConfMapCP(con, session.getEsquemaEmpresa(), getClaveRegistro());
		          out.print(JSONObject.toJSONString(jsonobj));
	  			  out.flush();
	  			  out.close();
	  		  }
			}
			catch(Exception e){
				Utils.imprimeLog("buscarConfCP(): ", e);
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
	 
	 
	 
	 public String actualizaConfiguracion(){
			Connection con = null;
			ResultadoConexion rc = null;
			PrintWriter out = null;
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			EtiquetasCPBean etiquetasCPBean = new EtiquetasCPBean();
			try{
	  		  SiarexSession session = ObtenerSession.getSession(request);

	  		  if ("".equals(session.getEsquemaEmpresa())){
	  			return Action.LOGIN;
	  		  }
	  		  else{
	  		    rc = getConnection(session.getEsquemaEmpresa());
	  		    con = rc.getCon();
	  		    out = response.getWriter();

	  		    String activado = "N";
	  		    if (isActivadaSW()) {
	  		    	activado = "S";
	  		    }
	  		    
	  		    String valEtiqueta = "N";
	  		    if (isValEtiquetaSW()) {
	  		    	valEtiqueta = "S";
	  		    }
	  		    
	  		    int res = 0;
	  		    res = etiquetasCPBean.actualizaConfCP(con, session.getEsquemaEmpresa(), getClaveRegistro(), getEtiqueta(), activado,
	  		    		 								    getFechaInicial(), getFechaFinal(), getSubject(), getMensajeError(), getUsuario(request), valEtiqueta);
	  		    
		        	Map<String, Object> json = new HashMap<String, Object>();
		        	json.put("ESTATUS", res > 0 ? "OK" : "ERROR");
		            out.print(JSONObject.toJSONString(json));
		            out.flush();
		            out.close();
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

	 
	 public String detalleEtiquetas(){
			Connection con = null;
			ResultadoConexion rc = null;
			
			HttpServletResponse response = ServletActionContext.getResponse();
	    	HttpServletRequest request = ServletActionContext.getRequest();
	    	Map<String, Object> json = new HashMap<String, Object>();
	    	EtiquetasCPBean etiquetasCPBean = new EtiquetasCPBean();
	    	try{
			  SiarexSession session = ObtenerSession.getSession(request);	
	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  }
	  		  else{
	  			response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

	  			PrintWriter out = response.getWriter();
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    JSONArray jsonArray  = null;
	            Map<String, Object > mapaRes = null;
	            
	            mapaRes = etiquetasCPBean.detalleEtiqueta(con, session.getEsquemaEmpresa(), Utils.noNuloNormal(getEtiqueta()), getVersion());
	            									   
	            jsonArray  = (JSONArray) mapaRes.get("detalle");
	            json.put("data", jsonArray);
	            String jsonobj = JSONObject.toJSONString(json);
	            out.print(jsonobj);
	            out.flush();
	            out.close(); 
	  		  }
			}
	    	catch(Exception e){
				Utils.imprimeLog("detalleEtiquetas(): ", e);
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
	 
	 
	 public String agregarEtiqueta(){
			Connection con = null;
			ResultadoConexion rc = null;
			PrintWriter out = null;
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			EtiquetasCPBean etiquetasCPBean = new EtiquetasCPBean();
			try{
			  SiarexSession session = ObtenerSession.getSession(request);	
	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  }
	  		  else{
	  		    rc = getConnection(session.getEsquemaEmpresa());
	  		    con = rc.getCon();
	  		    out = response.getWriter();

	  		    int res = 0;
	  		    res = etiquetasCPBean.altaEtiqueta(con, session.getEsquemaEmpresa(), getEtiqueta(), getDatoValida(), getVersion(), getUsuario(request));
	  		    
	        	Map<String, Object> json = new HashMap<String, Object>();
	        	json.put("ESTATUS", res > 0 ? "OK" : "ERROR");
	            out.print(JSONObject.toJSONString(json));
	            out.flush();
	            out.close();
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
			EtiquetasCPBean etiquetasCPBean = new EtiquetasCPBean();
			try{
				
				
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);

			    response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				} else{
					rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();

				    int eliminado = etiquetasCPBean.eliminaEtiqueta(con, session.getEsquemaEmpresa(), getClaveRegistro());

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
