package com.siarex247.catalogos.Puestos;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;



public class PuestosAction extends PuestosSupport{

	private static final long serialVersionUID = 476672439059737532L;

	
	
	public String listaPuestos() throws Exception {
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
				PuestosModel puestosModel = new PuestosModel();
				ArrayList<PuestosForm> listaPuestos = puestosBean.detallePuestos(con, rc.getEsquema());
				puestosModel.setData(listaPuestos);
				puestosModel.setRecordsFiltered(20);
				puestosModel.setDraw(-1);
				puestosModel.setRecordsTotal(listaPuestos.size());
				JSONObject json = new JSONObject(puestosModel);
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
	
	
	public String consultaPuestos() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		PuestosBean puestosBean = new PuestosBean();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
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
				PuestosForm puestosForm = puestosBean.consultaPuesto(con, rc.getEsquema(), getIdPuesto());
				JSONObject json = new JSONObject(puestosForm);
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
	
	public String altaPuestos() throws Exception {
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
				PuestosModel puestosModel = new PuestosModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					int totReg = puestosBean.altaPuestos(con, rc.getEsquema(), getNombreCorto(), getDescripcion(), getUsuario(request));
					if (totReg == -100) {
						puestosModel.setCodError("001");
						puestosModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}else {
						puestosModel.setCodError("000");
						puestosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}	
				
				JSONObject json = new JSONObject(puestosModel);
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
	

	public String actualizaPuestos() throws Exception {
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
				PuestosModel puestosModel = new PuestosModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					int totReg = puestosBean.modificaPuestos(con, rc.getEsquema(), getIdPuesto(), getNombreCorto(), getDescripcion(), getUsuario(request));
					if (totReg == -100) {
						puestosModel.setCodError("001");
						puestosModel.setMensajeError("Error el guardar la informaci�n del registro, consulte a su administrador.");
					}else {
						puestosModel.setCodError("000");
						puestosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}	
				
				JSONObject json = new JSONObject(puestosModel);
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
	
	
	
	public String eliminaPuestos() throws Exception {
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
				
				PrintWriter out = response.getWriter();
				PuestosModel puestosModel = new PuestosModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					int totReg = puestosBean.eliminaPuesto(con, rc.getEsquema(), getIdPuesto(), getUsuario(request));
					if (totReg == -100) {
						puestosModel.setCodError("001");
						puestosModel.setMensajeError("Error el guardar la informaci�n del registro, consulte a su administrador.");
					}else {
						puestosModel.setCodError("000");
						puestosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}	
				
				JSONObject json = new JSONObject(puestosModel);
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
	
	
	public String comboPuestos() throws Exception {
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
				PuestosModel puestosModel = new PuestosModel();
				ArrayList<PuestosForm> listaPuestos = puestosBean.comboPuestos(con, rc.getEsquema(), getIdPuesto());
				puestosModel.setData(listaPuestos);
				JSONObject json = new JSONObject(puestosModel);
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
}
