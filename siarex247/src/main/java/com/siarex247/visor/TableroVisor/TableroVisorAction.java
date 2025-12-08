package com.siarex247.visor.TableroVisor;

import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class TableroVisorAction extends TableroVisorSupport {

	private static final long serialVersionUID = 2338143373493028199L;

	private DecimalFormat decimalEntero = new DecimalFormat("###,###");
	
	
	public String obtenerTotalOrdenes() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TableroVisorBean tableroBean = new TableroVisorBean();
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
				
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
				
				
				int totalOrdenes = tableroBean.obtenerTotalOrdenes(con, rc.getEsquema(), claveProveedor);
				TableroVisorForm tableroForm = new TableroVisorForm();
				tableroForm.setTotalOrdenes(totalOrdenes);
				tableroForm.setDesTotalOrdenes(decimalEntero.format(totalOrdenes));
				JSONObject json = new JSONObject(tableroForm);
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
	
	public String obtenerTotalMoneda() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TableroVisorBean tableroBean = new TableroVisorBean();
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
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
				
				String totalMoneda = tableroBean.obtenerTotalXmoneda(con, rc.getEsquema(), getTipoMoneda(), claveProveedor);
				TableroVisorForm tableroForm = new TableroVisorForm();
				tableroForm.setTotalXmoneda("$ "+ totalMoneda);
				JSONObject json = new JSONObject(tableroForm);
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
	
	
	
	public String obtenerTotalEstatus() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TableroVisorBean tableroBean = new TableroVisorBean();
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
				
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
				
				HashMap<String, TableroVisorForm> mapaEstatus = tableroBean.obtenerTotalEstatus(con, rc.getEsquema(), session.getLenguaje(), claveProveedor);
				JSONObject json = new JSONObject(mapaEstatus);
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
	
	
	
	public String obtenerTotalFacturas() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TableroVisorBean tableroBean = new TableroVisorBean();
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
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
				
				int totalFacturas = tableroBean.obtenerTotalFactura(con, rc.getEsquema(), claveProveedor);
				TableroVisorForm tableroForm = new TableroVisorForm();
				tableroForm.setDesTotalFacturas(decimalEntero.format(totalFacturas));
				JSONObject json = new JSONObject(tableroForm);
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
	
	
	
	public String obtenerTotalComplemento() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TableroVisorBean tableroBean = new TableroVisorBean();
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
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
				
				int totalComplemento = tableroBean.obtenerTotalComplemento(con, rc.getEsquema(), claveProveedor);
				TableroVisorForm tableroForm = new TableroVisorForm();
				tableroForm.setDesTotalComplementos(decimalEntero.format(totalComplemento));
				JSONObject json = new JSONObject(tableroForm);
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
	
	
	public String obtenerTotalNotaCredito() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TableroVisorBean tableroBean = new TableroVisorBean();
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
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
				
				int totalNota = tableroBean.obtenerTotalNotasCredito(con, rc.getEsquema(), claveProveedor);
				TableroVisorForm tableroForm = new TableroVisorForm();
				tableroForm.setDesTotalNotaCredito(decimalEntero.format(totalNota));
				JSONObject json = new JSONObject(tableroForm);
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
	
	public String obtenerTotalProveedores() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TableroVisorBean tableroBean = new TableroVisorBean();
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
				
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
				
				int totalProveedores = tableroBean.obtenerTotalProveedores(con, rc.getEsquema(), claveProveedor);
				TableroVisorForm tableroForm = new TableroVisorForm();
				tableroForm.setDesTotalProveedores(decimalEntero.format(totalProveedores));
				JSONObject json = new JSONObject(tableroForm);
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
	
	
	
	public String listaProveedoresTOP() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TableroVisorBean tableroBean = new TableroVisorBean();
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
				
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
				
				TableroVisorModel tableroModel = new TableroVisorModel();
				ArrayList<TableroVisorForm> listaDetalle  = tableroBean.listaProveedoresTOP(con, rc.getEsquema(), getTipoMoneda(), claveProveedor);
				tableroModel.setData(listaDetalle);
				tableroModel.setRecordsFiltered(20);
				tableroModel.setDraw(-1);
				tableroModel.setRecordsTotal(listaDetalle.size());
				JSONObject json = new JSONObject(tableroModel);
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
	
	
	
	public String listaOrdenes() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TableroVisorBean tableroBean = new TableroVisorBean();
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
				
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
				}
				
				
				TableroVisorModel tableroModel = new TableroVisorModel();
				ArrayList<TableroVisorForm> listaDetalle  = tableroBean.listaOrdenes(con, rc.getEsquema(), session.getLenguaje(), claveProveedor);
				tableroModel.setData(listaDetalle);
				tableroModel.setRecordsFiltered(20);
				tableroModel.setDraw(-1);
				tableroModel.setRecordsTotal(listaDetalle.size());
				JSONObject json = new JSONObject(tableroModel);
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
