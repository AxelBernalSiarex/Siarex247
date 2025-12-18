package com.siarex247.configuraciones.RegimenFiscal;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configuraciones.ConfClaveUsoCFDI.ConfClaveUsoCFDIBean;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class RegimenFiscalAction extends RegimenFiscalSupport{

	
	
	private static final long serialVersionUID = -8944594026568631657L;

	
	
	
	public String detalleRF(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	JSONArray jsonArray  = null;
    	RegimenFiscalBean regimenFiscalBean = new RegimenFiscalBean();
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
			    
			    Map<String, Object > mapaRes = regimenFiscalBean.detalleRF(con, session.getEsquemaEmpresa());
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
	
	public String buscarConfRF(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	RegimenFiscalBean regimen = new RegimenFiscalBean();
    	try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
				
				JSONObject json = regimen.buscarConfRF(con, session.getEsquemaEmpresa(), getIdRegistro());
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
	
	
	public String movimientoRF(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	RegimenFiscalBean regimen = new RegimenFiscalBean();

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
			    	totReg = regimen.grabarRF(con, session.getEsquemaEmpresa(), getRazonSocialRF(), getRegimenFiscal(), getUsuario(request));
			    }
			    else{
			    	totReg = regimen.updateRF(con, session.getEsquemaEmpresa(), getRazonSocialRF(), getRegimenFiscal(), getIdRegistro(), getUsuario(request));
			    }

	        	json.put("ESTATUS", totReg > 0 ? "OK" : "ERROR");
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
		RegimenFiscalBean regimenFiscalBean = new RegimenFiscalBean();
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

			    int eliminado = regimenFiscalBean.deleteRF(con, session.getEsquemaEmpresa(), getIdRegistro(), getUsuario(request));

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
	
	
	
	public String comboClavesRegimen(){
        Connection con = null;
        ResultadoConexion rc = null;
        RegimenFiscalBean regimenFiscalBean = new RegimenFiscalBean();
        try{
        	HttpServletResponse response = ServletActionContext.getResponse();
        	HttpServletRequest request = ServletActionContext.getRequest();
        	response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
        	SiarexSession session = ObtenerSession.getSession(request);
        	PrintWriter out = response.getWriter();
            rc = getConnection(session.getEsquemaEmpresa());
            con = rc.getCon();
            int accion = Utils.noNuloINT(request.getParameter("accion"));

            RegimenFiscalModel regModel = new RegimenFiscalModel();
            ArrayList<RegimenForm> listaCombo  = regimenFiscalBean.comboClavesRegimen(con, session.getEsquemaEmpresa(), accion);
          
            regModel.setData(listaCombo);
            
            org.json.JSONObject json = new org.json.JSONObject(regModel);
			out.print(json);
            out.flush();
            out.close();
        }catch(Exception e){
        	Utils.imprimeLog("comboClavesRegimen(): ", e);
            logger.error(e);
        }
        
        try{
            if(con != null)
                con.close();
            con = null;
        }catch(Exception e){
            con = null;
        }
        return null;
    }
}
