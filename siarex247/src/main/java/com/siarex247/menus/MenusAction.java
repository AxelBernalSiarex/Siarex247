package com.siarex247.menus;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

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


public class MenusAction extends MenusSupport {

	private static final long serialVersionUID = 1L;
	
	public String menuPrincipal() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean beanMenu = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				UsuariosForm usuariosForm = new UsuariosBean().datosUsuario(con, rc.getEsquema(), getUsuario(request));
				ArrayList<MenusForm> listaOpciones = beanMenu.obtenerMenuPrincipal(con,rc.getEsquema(), session.getLenguaje(), usuariosForm.getIdPerfil());
				setListaOpciones(listaOpciones);	
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
	
	public ArrayList<MenusForm> menuPrincipal2(HttpServletRequest request) throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean beanMenu = new MenusBean();
		ArrayList<MenusForm> listaOpciones = null;

		try{
			SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return listaOpciones;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				UsuariosForm usuariosForm = new UsuariosBean().datosUsuario(con, rc.getEsquema(), getUsuario(request));
				listaOpciones = beanMenu.obtenerMenuPrincipal(con,rc.getEsquema(), session.getLenguaje(), usuariosForm.getIdPerfil());
			}
		}
		catch(Exception e){
			Utils.imprimeLog("menuPrincipal2(): ", e);
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
		return listaOpciones;
	}

	
	public String obtenerMenuUsuario() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean beanMenu = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		ArrayList<MenusForm> listaMenus = null;
		HttpServletResponse response = ServletActionContext.getResponse();

		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa()))
			{
				return Action.LOGIN;
			}
			else {
				PrintWriter out = response.getWriter();

				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				UsuariosForm usuariosForm = new UsuariosBean().datosUsuario(con, rc.getEsquema(), getUsuario(request));
				listaMenus = beanMenu.obtenerMenuPrincipal(con,rc.getEsquema(), session.getLenguaje(), usuariosForm.getIdPerfil());
				
				MenusForm menusForm = new MenusForm();
				for (int x = 0; x < listaMenus.size(); x++) {
					
					ArrayList<MenusForm> listaOpciones = beanMenu.obtenerOpcionesxMenu(con, rc.getEsquema(), session.getLenguaje(), usuariosForm.getIdPerfil(), getIdMenu());
					
				}
				
				/*
				supervisorModel.setData(listaSupervisores);
				supervisorModel.setRecordsFiltered(20);
				supervisorModel.setDraw(-1);
				supervisorModel.setRecordsTotal(listaSupervisores.size());
				JSONObject json = new JSONObject(supervisorModel);
				out.print(json);
				out.flush();
				out.close();
				*/
				
			}
			
			

			
			
		}
		catch(Exception e){
			Utils.imprimeLog("menuPrincipal2(): ", e);
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
		return null;
	}

	
	public String menuOpciones() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean beanMenu = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				UsuariosForm usuariosForm = new UsuariosBean().datosUsuario(con, rc.getEsquema(), getUsuario(request));
				ArrayList<MenusForm> listaOpciones = beanMenu.obtenerOpcionesxMenu(con, rc.getEsquema(), session.getLenguaje(), usuariosForm.getIdPerfil(), getIdMenu());
				setListaOpciones(listaOpciones);
				if (listaOpciones.size() > 0) {
					setMenusForm(listaOpciones.get(0));
				}
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
	
	//public ArrayList<ArrayList<String>> menuOpciones2(int idMenu) throws Exception {
	public ArrayList<MenusForm> menuOpciones2(int idMenu, HttpServletRequest request ) throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean beanMenu = new MenusBean();
		// HttpServletRequest request = ServletActionContext.getRequest();
		//ArrayList<ArrayList<String>> listaOpciones = null;
		ArrayList<MenusForm> listaOpciones = null;
		try{
			SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return listaOpciones;
			}else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				UsuariosForm usuariosForm = new UsuariosBean().datosUsuario(con, rc.getEsquema(), getUsuario(request));
				listaOpciones = beanMenu.obtenerOpcionesxMenu(con, rc.getEsquema(), session.getLenguaje(), usuariosForm.getIdPerfil(), idMenu);
			}
		}
		catch(Exception e){
			Utils.imprimeLog("menuOpciones2(): ", e);
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
		return listaOpciones;
	}
	

	public String menuSuperior() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean beanMenu = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		//HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				MenusForm menusForm = beanMenu.obtenerInfoMenuOpcion(con, rc.getEsquema(), getIdMenu(), getIdOpcion());
				setMenusForm(menusForm);
				
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
	
	
	
	public String listaMenus() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean menusBean = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				MenusModel menusModel = new MenusModel();
				ArrayList<MenusForm> listaMenus = menusBean.detalleMenus(con, rc.getEsquema());
				menusModel.setData(listaMenus);
				menusModel.setRecordsFiltered(20);
				menusModel.setDraw(-1);
				menusModel.setRecordsTotal(listaMenus.size());
				JSONObject json = new JSONObject(menusModel);
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
		return OK;
	}
	
	
	
	public String comboMenus() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean menusBean = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				MenusModel menusModel = new MenusModel();
				ArrayList<MenusForm> listaMenus = menusBean.comboMenus(con, rc.getEsquema(), getIdPerfil());
				menusModel.setData(listaMenus);
				JSONObject json = new JSONObject(menusModel);
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
		return null;
	}
	
	
	
	public String menusDisponiblesPerfil() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean menusBean = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				MenusModel menusModel = new MenusModel();
				ArrayList<MenusForm> listaMenus = null;
				if (getIdPerfil() > 0) {
					listaMenus = menusBean.getMenusDisponiblesPerfil(con, rc.getEsquema(), getIdPerfil());	
				}else {
					listaMenus = new ArrayList<MenusForm>();
				}
				menusModel.setData(listaMenus);
				menusModel.setRecordsFiltered(20);
				menusModel.setDraw(-1);
				menusModel.setRecordsTotal(listaMenus.size());
				JSONObject json = new JSONObject(menusModel);
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
		return null;
	}
	
	
	
	public String menusAsignadosPerfil() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean menusBean = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				MenusModel menusModel = new MenusModel();
				ArrayList<MenusForm> listaMenus = null;
				if (getIdPerfil() > 0) {
					listaMenus = menusBean.getMenusAsignadosPerfil(con, rc.getEsquema(), getIdPerfil());	
				}else {
					listaMenus = new ArrayList<MenusForm>();
				}
				menusModel.setData(listaMenus);
				menusModel.setRecordsFiltered(20);
				menusModel.setDraw(-1);
				menusModel.setRecordsTotal(listaMenus.size());
				JSONObject json = new JSONObject(menusModel);
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
		return null;
	}
	
	
	
	
	
	public String opcionesDisponiblesUsuario() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean menusBean = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				MenusModel menusModel = new MenusModel();
				ArrayList<MenusForm> listaMenus = null;
				if (getIdMenu() > 0) {
					listaMenus = menusBean.getOpcionesDisponiblesUsuario(con, rc.getEsquema(), getIdMenu(), getIdUsuario());	
				}else {
					listaMenus = new ArrayList<MenusForm>();
				}
				menusModel.setData(listaMenus);
				menusModel.setRecordsFiltered(20);
				menusModel.setDraw(-1);
				menusModel.setRecordsTotal(listaMenus.size());
				JSONObject json = new JSONObject(menusModel);
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
		return null;
	}
	
	
	
	public String opcionesAsignadasUsuario() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean menusBean = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				MenusModel menusModel = new MenusModel();
				ArrayList<MenusForm> listaMenus = null;
				if (getIdMenu() > 0) {
					listaMenus = menusBean.getOpcionesAsignadosUsuario(con, rc.getEsquema(), getIdMenu(), getIdUsuario());	
				}else {
					listaMenus = new ArrayList<MenusForm>();
				}
				menusModel.setData(listaMenus);
				menusModel.setRecordsFiltered(20);
				menusModel.setDraw(-1);
				menusModel.setRecordsTotal(listaMenus.size());
				JSONObject json = new JSONObject(menusModel);
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
		return null;
	}
	
	
	
	
	public String guardarOpcionAsignadaUsuario() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean menusBean = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				MenusModel menusModel = new MenusModel();
				int totReg = menusBean.altaOpcionUsuario(con, rc.getEsquema(), getIdOpcion(), getIdUsuario());
				if (totReg == -100) {
					menusModel.setCodError("001");
					menusModel.setMensajeError("Error el guardar la informacion del registro, consulte a su administrador.");
				}else {
					menusModel.setCodError("000");
					menusModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				}	
				JSONObject json = new JSONObject(menusModel);
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
		return null;
	}
	
	
	public String eliminarOpcionAsignadaUsuario() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		MenusBean menusBean = new MenusBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				MenusModel menusModel = new MenusModel();
				int totReg = menusBean.eliminaOpcionUsuario(con, rc.getEsquema(), getIdOpcion(), getIdUsuario());
				if (totReg == -100) {
					menusModel.setCodError("001");
					menusModel.setMensajeError("Error el guardar la informacion del registro, consulte a su administrador.");
				}else {
					menusModel.setCodError("000");
					menusModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				}	
				JSONObject json = new JSONObject(menusModel);
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
		return null;
	}
}
