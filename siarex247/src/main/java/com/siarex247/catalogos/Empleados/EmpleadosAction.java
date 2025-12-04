package com.siarex247.catalogos.Empleados;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;



public class EmpleadosAction extends EmpleadosSupport{

	private static final long serialVersionUID = 476672439059737532L;

	
	
	public String listaEmpleados() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		EmpleadosBean empleadosBean = new EmpleadosBean();
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
				EmpleadosModel empleadosModel = new EmpleadosModel();
				ArrayList<EmpleadosForm> listaEmpleados = empleadosBean.detalle(con, rc.getEsquema());
				empleadosModel.setData(listaEmpleados);
				empleadosModel.setRecordsFiltered(20);
				empleadosModel.setDraw(-1);
				empleadosModel.setRecordsTotal(listaEmpleados.size());
				JSONObject json = new JSONObject(empleadosModel);
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
	
	
	public String consultaEmpleados() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		EmpleadosBean empleadosBean = new EmpleadosBean();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		
		UsuariosBean usuariosBean = new UsuariosBean();
		UsuariosForm usuariosForm = null;
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
				EmpleadosForm empleadosForm = empleadosBean.consultaEmpleado(con, rc.getEsquema(), getIdRegistro());
				
				usuariosForm = usuariosBean.informacionUsuario(con, rc.getEsquema(), empleadosForm.getIdEmpleado());
				
				empleadosForm.setUsuariosForm(usuariosForm);
				
				JSONObject json = new JSONObject(empleadosForm);
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
	
	public String altaEmpleados() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		EmpleadosBean empleadosBean = new EmpleadosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		UsuariosBean usuariosBean = new UsuariosBean();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				PrintWriter out = response.getWriter();
				EmpleadosModel empleadosModel = new EmpleadosModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					
					boolean pasoValidaciones = true;
					logger.info("bandAcceso:==>"+isBandAcceso());
					logger.info("getUsuarioAcceso:==>"+getUsuarioAcceso());
					
					if (isBandAcceso()) {
						
						if ("".equalsIgnoreCase(Utils.noNuloNormal(getUsuarioAcceso()))) {
							pasoValidaciones = false;
							empleadosModel.setCodError("001");
							empleadosModel.setMensajeError("Error el guardar la información del registro, Debe especificar una clave de acceso para el empleado.");
						}else if (getIdPerfil() == 0) {
							pasoValidaciones = false;
							empleadosModel.setCodError("001");
							empleadosModel.setMensajeError("Error el guardar la información del registro, Debe especificar un perfil para el empleado.");
						}else {
							UsuariosForm usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuarioAcceso());
							if (!"".equalsIgnoreCase(Utils.noNulo(usuariosForm.getNombreCompleto()))) {
								pasoValidaciones = false;
								empleadosModel.setCodError("001");
								empleadosModel.setMensajeError("Error el guardar la información del registro, La clave de acceso especificada ya se encuentra registrada para otro empleado.");
							}
						}
					}
					
					if (pasoValidaciones) {
						int totReg = empleadosBean.altaEmpleado(con, rc.getEsquema(), getIdEmpleado(), getNombreCompleto(), getCorreo(), getIdSupervisor(), getShipTo(), getPermitirAlta(), getUsuario(request));
						if (totReg == -100) {
							empleadosModel.setCodError("001");
							empleadosModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
						}else {
							empleadosModel.setCodError("000");
							empleadosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
							
							
							if (isBandAcceso()) {
								
								usuariosBean.altaUsuarios(con, rc.getEsquema(), getUsuarioAcceso().toLowerCase(), getIdEmpleado(), getNombreCompleto(), getCorreo(), getIdPerfil(), getUsuario(request));
								
								AccesoBean accesoBean = new AccesoBean();
								EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
								String pwdUsuario = accesoBean.existeSiarexActivo(getUsuarioAcceso());
								long tiempoAcceso = System.currentTimeMillis();
							  	String codigoAcceso = Utils.encryptarMD5(String.valueOf( tiempoAcceso ));
							  	
								if ("".equalsIgnoreCase(pwdUsuario) ) { // si NO existe, se envia correo
								  	accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getNombreCompleto(), getUsuarioAcceso(), null, getCorreo(), getIdPerfil(), getIdEmpleado(), "N", codigoAcceso, getUsuario(request));
								  	
								  	String urlAcceso = "https://"+ UtilsPATH.DOMINIO_PRINCIPAL + "/siarexLogin/registroSiarex.jsp?c="+ codigoAcceso;
								    String htmlAcceso = UtilsHTML.generaHTMLAcceso(getNombreCompleto(), urlAcceso, UtilsPATH.DOMINIO_PRINCIPAL);
								    logger.info("htmlAcceso====>"+htmlAcceso);
								    String emailTO [] = {getCorreo()};
								    EnviaCorreoGrid.enviarCorreo(null, htmlAcceso, false, emailTO, null, "SIAREX - Registrar Acceso", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());						    
								    
								}else {
									accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getNombreCompleto(), getUsuarioAcceso(), pwdUsuario, getCorreo(), getIdPerfil(), getIdEmpleado(), "S", codigoAcceso, getUsuario(request));
								}							
							}
						}	
					}
					
				
				JSONObject json = new JSONObject(empleadosModel);
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
	

	public String actualizaEmpleados() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		EmpleadosBean empleadosBean = new EmpleadosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		UsuariosBean usuariosBean = new UsuariosBean();
		UsuariosForm usuariosForm = null;
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				PrintWriter out = response.getWriter();
				EmpleadosModel empleadosModel = new EmpleadosModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					
					boolean pasoValidaciones = true;
					logger.info("bandAcceso:==>"+isBandAcceso());
					logger.info("getUsuarioAcceso:==>"+getUsuarioAcceso());
					
					usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuarioAcceso());
					
					if (isBandAcceso()) {
						
						if ("".equalsIgnoreCase(Utils.noNuloNormal(getUsuarioAcceso()))) {
							pasoValidaciones = false;
							empleadosModel.setCodError("001");
							empleadosModel.setMensajeError("Error el guardar la información del registro, Debe especificar una clave de acceso para el empleado.");
						}else if (getIdPerfil() == 0) {
							pasoValidaciones = false;
							empleadosModel.setCodError("001");
							empleadosModel.setMensajeError("Error el guardar la información del registro, Debe especificar un perfil para el empleado.");
						}
					}
					
					if (pasoValidaciones) {
						int totReg = empleadosBean.modificaEmpleado(con, rc.getEsquema(), getIdRegistro(), getIdEmpleado(), getNombreCompleto(), getCorreo(), getIdSupervisor(), getShipTo(), getPermitirAlta(), getUsuario(request));
						if (totReg == -100) {
							empleadosModel.setCodError("001");
							empleadosModel.setMensajeError("Error el guardar la informaci�n del registro, consulte a su administrador.");
						}else {
							
							
							if (isBandAcceso()) {
								if ("".equalsIgnoreCase(Utils.noNulo(usuariosForm.getNombreCompleto()))) {
									usuariosBean.altaUsuarios(con, rc.getEsquema(), getUsuarioAcceso().toLowerCase(), getIdEmpleado(), getNombreCompleto(), getCorreo(), getIdPerfil(), getUsuario(request));
									
									AccesoBean accesoBean = new AccesoBean();
									EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
									String pwdUsuario = accesoBean.existeSiarexActivo(getUsuarioAcceso());
									long tiempoAcceso = System.currentTimeMillis();
								  	String codigoAcceso = Utils.encryptarMD5(String.valueOf( tiempoAcceso ));
								  	
									if ("".equalsIgnoreCase(pwdUsuario) ) { // si NO existe, se envia correo
									  	accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getNombreCompleto(), getUsuarioAcceso(), null, getCorreo(), getIdPerfil(), getIdEmpleado(), "N", codigoAcceso, getUsuario(request));
									  	
									  	String urlAcceso = "https://"+ UtilsPATH.DOMINIO_PRINCIPAL + "/siarexLogin/registroSiarex.jsp?c="+ codigoAcceso;
									    String htmlAcceso = UtilsHTML.generaHTMLAcceso(getNombreCompleto(), urlAcceso, UtilsPATH.DOMINIO_PRINCIPAL);
									    logger.info("htmlAcceso====>"+htmlAcceso);
									    String emailTO [] = {getCorreo()};
									    EnviaCorreoGrid.enviarCorreo(null, htmlAcceso, false, emailTO, null, "SIAREX - Registrar Acceso", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());						    
									    
									}else {
										accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getNombreCompleto(), getUsuarioAcceso(), pwdUsuario, getCorreo(), getIdPerfil(), getIdEmpleado(), "S", codigoAcceso, getUsuario(request));
									}
								}
							}else {
								
								usuariosBean.deleteUsuarios(con, rc.getEsquema(), usuariosForm.getIdRegistro(), "D", getUsuario(request));
							}
							empleadosModel.setCodError("000");
							empleadosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
						}	
					}
					
				
				JSONObject json = new JSONObject(empleadosModel);
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
	
	
	
	public String eliminaEmpleados() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		EmpleadosBean empleadosBean = new EmpleadosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
				PrintWriter out = response.getWriter();
				EmpleadosModel empleadosModel = new EmpleadosModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					int totReg = empleadosBean.eliminaEmpleado(con, rc.getEsquema(), getIdRegistro(), getUsuario(request));
					if (totReg == -100) {
						empleadosModel.setCodError("001");
						empleadosModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}else {
						empleadosModel.setCodError("000");
						empleadosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}	
				
				JSONObject json = new JSONObject(empleadosModel);
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
	
	
	public String comboEmpleados() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		EmpleadosBean empleadosBean = new EmpleadosBean();
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
				EmpleadosModel empleadosModel = new EmpleadosModel();
				ArrayList<EmpleadosForm> listaEmpleados = empleadosBean.comboEmpleados(con, rc.getEsquema(), session.getLenguaje());
				empleadosModel.setData(listaEmpleados);
				JSONObject json = new JSONObject(empleadosModel);
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
