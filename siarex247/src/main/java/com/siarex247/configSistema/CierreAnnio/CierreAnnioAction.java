package com.siarex247.configSistema.CierreAnnio;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class CierreAnnioAction extends CierreAnnioSupport {

	private static final long serialVersionUID = -3063802938077971850L;

	
	public String cierreAnio(){
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	CierreAnnioBean cierreAnnioBean = new CierreAnnioBean();

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
			    
			    String tipoCierre = Utils.noNulo(getTipoCierre());
			    if ("".equals(tipoCierre)) {
			    	tipoCierre = "F";
			    }
			    
			    int estatus = cierreAnnioBean.altaCierre(con, rc.getEsquema(), getAnio(), getFechaApartir(), getMensajeError1(), getFechaHasta(), getMensajeError2(), getUsuario(request), tipoCierre);

		        Map<String, Object> json = new HashMap<String, Object>();
		        json.put("ESTATUS", estatus > 0 ? "OK" : "ERROR");
		        out.print(JSONObject.toJSONString(json));
		        out.flush();
		        out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("cierreAnio(): ", e);
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
	
	
	
	public String obtenerCierre(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	CierreAnnioBean cierreAnnioBean = new CierreAnnioBean();
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
			    String tipoCierre = Utils.noNulo(getTipoCierre());
			    if ("".equals(tipoCierre)) {
			    	tipoCierre = "F";
			    }
			    JSONObject json = cierreAnnioBean.obtenerCierre(con, session.getEsquemaEmpresa(), getAnio(), tipoCierre);
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
}
