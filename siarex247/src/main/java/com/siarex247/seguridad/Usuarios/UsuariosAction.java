package com.siarex247.seguridad.Usuarios;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.struts2.ServletActionContext;
//import com.sai247.utils.UtilsHTML;
//import com.sai247.utils.UtilsPATH;
import org.apache.struts2.action.Action;
import org.json.JSONObject;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class UsuariosAction extends UsuariosSupport{
	
	
	private static final long serialVersionUID = -9014607594418418656L;
	

	
	public String listaUsuarios() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		UsuariosBean usuariosBean = new UsuariosBean();
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
				UsuariosModel usuariosModel = new UsuariosModel();
				ArrayList<UsuariosForm> listaUsuarios = usuariosBean.detalleUsuarios(con, rc.getEsquema());
				usuariosModel.setData(listaUsuarios);
				usuariosModel.setRecordsFiltered(20);
				usuariosModel.setDraw(-1);
				usuariosModel.setRecordsTotal(listaUsuarios.size());
				JSONObject json = new JSONObject(usuariosModel);
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
	
	
	public String consultaUsuarios() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		UsuariosBean usuariosBean = new UsuariosBean();
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
				UsuariosForm usuariosForm = usuariosBean.consultaUsuarios(con, rc.getEsquema(), getIdRegistro());
				
				JSONObject json = new JSONObject(usuariosForm);
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
	
	
	public String altaUsuarios() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		UsuariosBean usuariosBean = new UsuariosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ResultadoConexion rcAcceso = null;
		Connection conAcceso = null;
		
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				PrintWriter out = response.getWriter();
				UsuariosModel usuariosModel = new UsuariosModel();
				
				if (getIdPerfil() == 0) {
					usuariosModel.setCodError("001");
					usuariosModel.setMensajeError("Error el guardar la informacion del registro, debe especificar un perfil para el usuario.");
				}else {
					
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					int totReg = usuariosBean.altaUsuarios(con, rc.getEsquema(), getIdUsuario(), getIdEmpleado(), getNombreCompleto(), getCorreo(), getIdPerfil(), getUsuario(request));
					if (totReg == -100) {
						usuariosModel.setCodError("001");
						usuariosModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}else {
						usuariosModel.setCodError("000");
						usuariosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
						
						AccesoBean accesoBean = new AccesoBean();
						EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
						String pwdUsuario = accesoBean.existeSiarexActivo(getIdUsuario());
						long tiempoAcceso = System.currentTimeMillis();
					  	String codigoAcceso = Utils.encryptarMD5(String.valueOf( tiempoAcceso ));
					  	
					  	// logger.info("pwdUsuario====>"+pwdUsuario);
						if ("".equalsIgnoreCase(pwdUsuario) ) { // si NO existe, se envia correo
						  	accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getNombreCompleto(), getIdUsuario(), null, getCorreo(), getIdPerfil(), getIdEmpleado(), "N", codigoAcceso, getUsuario(request));
						  	
						  	String urlAcceso = "https://"+ UtilsPATH.DOMINIO_PRINCIPAL + "/siarexLogin/registroSiarex.jsp?c="+ codigoAcceso;
						    String htmlAcceso = UtilsHTML.generaHTMLAcceso(getNombreCompleto(), urlAcceso, UtilsPATH.DOMINIO_PRINCIPAL);
						   // logger.info("htmlAcceso====>"+htmlAcceso);
						    String emailTO [] = {getCorreo()};
						    EnviaCorreoGrid.enviarCorreo(null, htmlAcceso, false, emailTO, null, "SIAREX - Registrar Acceso", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());						    
						    
						}else {
							accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getNombreCompleto(), getIdUsuario(), "", getCorreo(), getIdPerfil(), getIdEmpleado(), "S", codigoAcceso, getUsuario(request));
						}
					}	
				}
				
				logger.info("*************** setCodError..******************"+usuariosModel.getCodError());
				logger.info("*************** setMensajeError..******************"+usuariosModel.getMensajeError());
				
				JSONObject json = new JSONObject(usuariosModel);
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
				if (conAcceso != null){
					conAcceso.close();
				}
				conAcceso = null;
				
			}catch(Exception e){
				con = null;
			}
		}
		return SUCCESS;
	}
	
	
	
	

	
	public String actualizaUsuarios() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		UsuariosBean usuariosBean = new UsuariosBean();
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
				UsuariosModel usuariosModel = new UsuariosModel();
				if (getIdPerfil() == 0) {
					usuariosModel.setCodError("001");
					usuariosModel.setMensajeError("Error el guardar la informacion del registro, debe especificar un perfil para el usuario.");
				}else {
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					
					// UsuariosForm usuariosForm = usuariosBean.consultaUsuarios(con, rc.getEsquema(), getIdRegistro());
					
					int totReg = usuariosBean.updateUsuarios(con, rc.getEsquema(), getIdRegistro(), getNombreCompleto(), getCorreo(), getIdPerfil(), getUsuario(request));
					
					if (totReg == -100) {
						usuariosModel.setCodError("001");
						usuariosModel.setMensajeError("Error el guardar la informaci�n del registro, consulte a su administrador.");
					}else {
						usuariosModel.setCodError("000");
						usuariosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");

						 AccesoBean accesoBean = new AccesoBean();
						 EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
						 accesoBean.actualizaAcceso(empresaForm.getClaveEmpresa(), getNombreCompleto(), getIdUsuario(), getIdPerfil(), getCorreo(), getIdEmpleado(), getUsuario(request));
						
						
					}
				}
				JSONObject json = new JSONObject(usuariosModel);
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
	
	

	public String eliminaUsuarios() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		UsuariosBean usuariosBean = new UsuariosBean();
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
				UsuariosModel usuariosModel = new UsuariosModel();
				
				if (getIdRegistro() == 0) {
					usuariosModel.setCodError("001");
					usuariosModel.setMensajeError("Error el eliminar el registro, debe especificar un usuarios para eliminar.");
				}else {
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					UsuariosForm usuariosForm = usuariosBean.consultaUsuarios(con, rc.getEsquema(), getIdRegistro());
					
					int totReg = usuariosBean.deleteUsuarios(con, rc.getEsquema(), getIdRegistro(), "D", getUsuario(request));
					if (totReg == 1) {
						usuariosModel.setCodError("000");
						usuariosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
						AccesoBean accesoBean = new AccesoBean();
						EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
						accesoBean.eliminaAcceso(empresaForm.getClaveEmpresa(),  usuariosForm.getIdUsuario());
						
						String codeAcceso = accesoBean.existeSiarexActivo(usuariosForm.getIdUsuario());
						
						if ("".equalsIgnoreCase(codeAcceso)) {
							accesoBean.eliminaAccesoTomcat(usuariosForm.getIdUsuario());
						}
						
					}else {
						usuariosModel.setCodError("001");
						usuariosModel.setMensajeError("Error al eliminar el registro, consulte a su administrador.");
					}
				}
				JSONObject json = new JSONObject(usuariosModel);
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
	
	
	
	
	public String consultaPermisos() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		UsuariosBean usuariosBean = new UsuariosBean();
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
				
				UsuariosForm usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				
				int claveProveedor = 0;
				if (usuariosForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuariosForm.getIdEmpleado().substring(5));
					ProveedoresBean provBean = new ProveedoresBean();
					ProveedoresForm provForm = provBean.consultarProveedor(con, rc.getEsquema(), claveProveedor);
					if ("USA".equalsIgnoreCase(provForm.getTipoProveedor())) {
						usuariosForm.setIsAmericano("S");
					}
				}
				
				
				JSONObject json = new JSONObject(usuariosForm);
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
	
	
	public UsuariosForm consultaPermisosJSP(HttpServletRequest request) throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		UsuariosBean usuariosBean = new UsuariosBean();
		UsuariosForm usuariosForm = null;

		try{
			SiarexSession session = ObtenerSession.getSession(request);
			 
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				if (usuariosForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuariosForm.getIdEmpleado().substring(5));
					ProveedoresBean provBean = new ProveedoresBean();
					ProveedoresForm provForm = provBean.consultarProveedor(con, rc.getEsquema(), claveProveedor);
					if ("USA".equalsIgnoreCase(provForm.getTipoProveedor())) {
						usuariosForm.setIsAmericano("S");
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
		return usuariosForm;
	}
}
