package com.siarex247.registro;

import java.io.PrintWriter;
import java.sql.Connection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.cumplimientoFiscal.ExportarXML.ExportarXMLBean;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class RegistroAction extends RegistroSupport{

	private static final long serialVersionUID = -6960404156310142279L;

	
	public String registrarAcceso() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		Connection conEmpresa = null;
		ResultadoConexion rcEmpresa = null;
		
		RegistroBean registroBean = new RegistroBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		RegistroModel registroModel = new RegistroModel();
		 String retorno = "success";
		try{
			
			rc = getConnectionSiarex();
			con = rc.getCon();
			
			String tienePermiso [] = null;
			
			ResultadoConexion rcSiarex = null;
			Connection conSiarex = null;
			//int idEmpresa = 10014;
			int idEmpresa = getClaveEmpresa();
			String idUsuario = getUsuario(request);
			try {
				//int idEmpresa = getClaveEmpresa();
				rcSiarex = getConnectionSiarex();
				conSiarex = rcSiarex.getCon();
				tienePermiso = registroBean.tieneAccesoAplicacion(conSiarex, idEmpresa , idUsuario);	
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally {
				try {
					if (conSiarex != null) {
						conSiarex.close();
					}
					conSiarex = null;
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				}
			}
			
			if ("true".equalsIgnoreCase(tienePermiso[0])) {
				RegistroForm registroForm = registroBean.datosEmpresa(con, idEmpresa);
					SiarexSession session = ObtenerSession.getSession(request);
					if ("N".equalsIgnoreCase(tienePermiso[1])) {
							retorno = "noPermiso";	
					}else {
						rcEmpresa = getConnection(registroForm.getEsquemaEmpresa());
						conEmpresa = rcEmpresa.getCon();
						
						// UsuariosBean usuarioBean = new UsuariosBean();
						// UsuariosForm usuariosForm = usuarioBean.datosUsuario(conEmpresa, rcEmpresa.getEsquema(), getIdUsuario());
						
						registroModel.setCodError("000");
						registroModel.setMensajeError("Registro satisfactorio...");
						// session.setClaveEmpresa(getClaveEmpresa());
						// session.setClaveUsuarioHTTP(idUsuario);
						session.setEsquemaEmpresa(registroForm.getEsquemaEmpresa());
						if ("".equalsIgnoreCase(Utils.noNuloNormal(getLenguaje()))) {
							session.setLenguaje("MX");	
						}else {
							session.setLenguaje(getLenguaje());
						}
						// session.setNombreEmpresa(registroForm.getNombreEmpresa());
						// session.setNombrePerfil(usuariosForm.getNombreCortoPerfil());
						// session.setImagenEmpresa(registroForm.getLogoEmpresa());
					}
			}else {
				retorno = "noPermiso";	
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (con != null){
					con.close();
				}
				con = null;
				if (conEmpresa != null){
					conEmpresa.close();
				}
				conEmpresa = null;
			}catch(Exception e){
				con = null;
			}
		}
		return retorno;
	}
	
	
	
	public String consultarPerfil() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		PrintWriter out = response.getWriter();
		RegistroModel registroModel = new RegistroModel();
		ResultadoConexion rc = null;
		Connection con = null;
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa()))
			{
				return Action.LOGIN;
			} else {
				
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				UsuariosBean usuariosBean = new UsuariosBean();
				UsuariosForm UsuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request)); 	
				
					registroModel.setDesPerfil(UsuariosForm.getDesPerfil());
					registroModel.setMensajeError("Exito");
				
			}
			
			JSONObject json = new JSONObject(registroModel);
		    out.print(json);
            out.flush();
            out.close();
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			}catch(Exception e) {
				con = null;
			}
		}
		return null;
	}
	
	
	
	public String validarAccesoDescarga() throws Exception {
		ResultadoConexion rc = null;
		Connection conSiarex = null;
		
		 
		RegistroBean registroBean = new RegistroBean();
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		RegistroModel registroModel = new RegistroModel();
		
		ExportarXMLBean exportBean = new ExportarXMLBean();
		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			 PrintWriter out = response.getWriter();
			
			rc = getConnectionSiarex();
			conSiarex = rc.getCon();
			
			int idArchivo = Utils.noNuloINT(request.getParameter("idArchivo"));

			
			String fechaGeneracion = exportBean.getDescarga(conSiarex, rc.getEsquema(), idArchivo, getUsuario(request));

			int annio = 0;
			int mes = 0;
			int dia = 0;
			long tiempoTranscurrido = 0;
			if ("".equalsIgnoreCase(fechaGeneracion)){
				tiempoTranscurrido = Utils.tiempoTranscurridoActual(2020, mes, dia);
			}else {
				annio = Integer.parseInt(fechaGeneracion.substring(0, 4));
				mes = Integer.parseInt(fechaGeneracion.substring(5, 7));
				dia = Integer.parseInt(fechaGeneracion.substring(8, 10));			
				tiempoTranscurrido = Utils.tiempoTranscurridoActual(annio, mes, dia);
			}
			

			
			// String pwdUsuario = Utils.dobleEncryptarMD5(getPwdUsuario().trim());

			boolean isCorrecto = registroBean.validarUsuarioDescarga(conSiarex, "", getUsuario(request));
			logger.info("************ isCorrecto *****************"+isCorrecto);
			logger.info("************ fechaGeneracion *****************"+fechaGeneracion);
			logger.info("************ tiempoTranscurrido *****************"+tiempoTranscurrido);
			
			if (!isCorrecto) {
				registroModel.setCodError("001");
				registroModel.setMensajeError("Acceso denegado, favor de revisar su usuario y la contraseña...");
			}else if ("".equalsIgnoreCase(fechaGeneracion)) {
				registroModel.setCodError("001");
				registroModel.setMensajeError("Descarga de archivo no permitida, debe accesar con el usuario que generó el archivo...");
			}else if (tiempoTranscurrido > 7) {
				registroModel.setCodError("001");
				registroModel.setMensajeError("La solicitud de descarga requerida ha expirado, favor de realizar una nueva solicitud.");
			}else {
				registroModel.setCodError("000");
				registroModel.setMensajeError("La descarga se ha realizado satisfactoriamente, favor de revisar su directorio de descargas...");
			}

			JSONObject json = new JSONObject(registroModel);
		    out.print(json);
            out.flush();
            out.close();
            
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (conSiarex != null){
					conSiarex.close();
				}
				conSiarex = null;
				
			}catch(Exception e){
				conSiarex = null;
			}
		}
		return null;
	}
		
}
