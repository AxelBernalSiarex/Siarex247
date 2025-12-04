package com.siarex247.catalogos.CentroCostos;

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
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class CentroCostosAction extends CentroCostosSupport{

	private static final long serialVersionUID = -891071124456254140L;

	
	public String detalleCentros() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object > mapaRes = null;
		JSONArray jsonArray  = null;
		CentroCostosBean centroBean = new CentroCostosBean();
		Map<String, Object> json = new HashMap<String, Object>();

		try{
		  PrintWriter out = response.getWriter();
		  
		  response.setContentType("text/html; charset=UTF-8");
		  response.setCharacterEncoding("UTF-8");
			
		  SiarexSession session = ObtenerSession.getSession(request);
		  if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
		  }else{
			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
			  
			  // String jsonRespuesta = centroBean.detalleCentros();
			  
			  mapaRes = centroBean.detalleCentros(con, session.getEsquemaEmpresa());
			  
			  jsonArray  = (JSONArray) mapaRes.get("detalle");
	         
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


	
	public String altaCentro() {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();

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
			  
			  CentroCostosBean centroBean = new CentroCostosBean();
			  int totReg = centroBean.grabarCentroCosto(con, session.getEsquemaEmpresa(), getIdCentro(), getDepartamento(), getCorreoCentro(), getUsuario(request));

			  CentroCostosModel centroModel = new CentroCostosModel();
			  if (totReg == -100) {
				  centroModel.setCodError("001");
				  centroModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
				}else {
					centroModel.setCodError("000");
					centroModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				}
			  
			    org.json.JSONObject json = new org.json.JSONObject(centroModel);
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
	
	

	public String modificaCentro() {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	
		try{
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
		  SiarexSession session = ObtenerSession.getSession(request);
		  if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
		  }
		  else{
			  PrintWriter out = response.getWriter();
			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
			  
			  CentroCostosBean centroBean = new CentroCostosBean();
			  int totReg = centroBean.modificarCentroCosto(con, session.getEsquemaEmpresa(), getIdRegistro(), getIdCentro(), getDepartamento(), getCorreoCentro(), getUsuario(request));

			  CentroCostosModel centroModel = new CentroCostosModel();
			  if (totReg == -100) {
				  centroModel.setCodError("001");
				  centroModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
				}else {
					centroModel.setCodError("000");
					centroModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				}

			    org.json.JSONObject json = new org.json.JSONObject(centroModel);
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
	
	
	public String eliminarCentro() {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	
		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
		  SiarexSession session = ObtenerSession.getSession(request);
		  if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
		  }
		  else{
			  PrintWriter out = response.getWriter();
			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
			  
			  CentroCostosBean centroBean = new CentroCostosBean();
			  int totReg = centroBean.eliminaCentroCosto(con, rc.getEsquema(), getIdRegistro());

			  CentroCostosModel centroModel = new CentroCostosModel();
			  if (totReg == -100) {
				  centroModel.setCodError("001");
				  centroModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
				}else {
					centroModel.setCodError("000");
					centroModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				}

			    org.json.JSONObject json = new org.json.JSONObject(centroModel);
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
	
	
	public String buscarCentroCosto() {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
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
			  
			  CentroCostosBean centroBean = new CentroCostosBean();
			  JSONObject jsonRetorno = centroBean.buscarCentroCosto(con, session.getEsquemaEmpresa(), getIdRegistro());
			  out.print(jsonRetorno);
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
	
	
	public String comboCentrosCostos()
    {
        Connection con = null;
        
        ResultadoConexion rc = null;
        CentroCostosBean centroBean = new CentroCostosBean();
        CentroCostosModel costosModel = new CentroCostosModel();
        try{
        	HttpServletResponse response = ServletActionContext.getResponse();
        	HttpServletRequest request = ServletActionContext.getRequest();
        	response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
        	SiarexSession session = ObtenerSession.getSession(request);
        	PrintWriter out = response.getWriter();
            rc = getConnection(session.getEsquemaEmpresa());
            con = rc.getCon();
            
            ArrayList<CentroCostosForm> listaCombo  = centroBean.comboCentroCosto(con, session.getEsquemaEmpresa(), session.getLenguaje());
            costosModel.setData(listaCombo);
            org.json.JSONObject json = new org.json.JSONObject(costosModel);
			out.print(json);
            out.flush();
            out.close();
            
            
        }catch(Exception e){
        	Utils.imprimeLog("", e);
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
