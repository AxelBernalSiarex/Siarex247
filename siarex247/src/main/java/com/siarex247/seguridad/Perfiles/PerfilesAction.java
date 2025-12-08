package com.siarex247.seguridad.Perfiles;

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

public class PerfilesAction extends PerfilesSupport{
	private static final long serialVersionUID = -9014607594418418656L;
	
	public String listaPerfiles() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		PerfilesBean perfilesBean = new PerfilesBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
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
				
				UsuariosBean usuarioBean = new UsuariosBean();
	            UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
	            
	            PerfilesModel perfilesModel = new PerfilesModel();
				ArrayList<PerfilesForm> listaPerfiles = perfilesBean.detallePerfiles(con, rc.getEsquema(), usuarioForm.getNombreCortoPerfil());
				perfilesModel.setData(listaPerfiles);
				perfilesModel.setRecordsFiltered(20);
				perfilesModel.setDraw(-1);
				perfilesModel.setRecordsTotal(listaPerfiles.size());
				JSONObject json = new JSONObject(perfilesModel);
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
	
	
	public String consultaPerfil() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		PerfilesBean perfilesBean = new PerfilesBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				PerfilesModel perfilesModel = new PerfilesModel();
				JSONObject json = null;
				if (getClavePerfil() == 0) {
					perfilesModel.setCodError("001");
					perfilesModel.setMensajeError("Error al consultar informacion del registro, debe especificar un id de perfil para consultar.");
					json = new JSONObject(perfilesModel);
				}else {
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					PerfilesForm perfilesForm = perfilesBean.consultaPerfil(con, rc.getEsquema(), getClavePerfil());
					json = new JSONObject(perfilesForm);
				}
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
	
	public String altaPerfil() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		PerfilesBean usuariosBean = new PerfilesBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Connection conAcceso = null;
		
		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				PerfilesModel perfilesModel = new PerfilesModel();
				//logger.info("************** ALTA DE PERFIL *********************");
				if (getClavePerfil() == 0) {
					perfilesModel.setCodError("001");
					perfilesModel.setMensajeError("Error el guardar la informacion del registro, debe especificar un perfil para el usuario.");
				}else {
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();

					int totReg = usuariosBean.altaPerfiles(con, rc.getEsquema(), getNombreCorto(), getDescripcion(), "", getTipoPerfil(), getUsuario(request));
					if (totReg == -100) {
						perfilesModel.setCodError("001");
						perfilesModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					} else {
						perfilesModel.setCodError("000");
						perfilesModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}	
				}
				
				JSONObject json = new JSONObject(perfilesModel);
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
	
	public String actualizaPerfil() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		PerfilesBean perfilesBean = new PerfilesBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				PerfilesModel perfilesModel = new PerfilesModel();
				if (getClavePerfil() == 0) {
					perfilesModel.setCodError("001");
					perfilesModel.setMensajeError("Error el guardar la informacion del registro, debe especificar un perfil para el usuario.");
				}else {
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					
					int totReg = perfilesBean.updatePerfiles(con, rc.getEsquema(), getClavePerfil(), getNombreCorto(), getDescripcion(), "", getTipoPerfil(), getUsuario(request));
					if (totReg == -100) {
						perfilesModel.setCodError("001");
						perfilesModel.setMensajeError("Error el guardar la informacion del registro, consulte a su administrador.");
					}else {
						perfilesModel.setCodError("000");
						perfilesModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}
				}
				JSONObject json = new JSONObject(perfilesModel);
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
	
	public String eliminaPerfil() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		PerfilesBean perfilesBean = new PerfilesBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				PerfilesModel perfilesModel = new PerfilesModel();
				//logger.info("**************** ELIMINANDO PERFILES **************");
				if (getClavePerfil() == 0) {
					perfilesModel.setCodError("001");
					perfilesModel.setMensajeError("Error el eliminar el registro, debe especificar un perfil para eliminar.");
				}else {
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();

					int totReg = perfilesBean.deletePerfiles(con, rc.getEsquema(), getClavePerfil(), getUsuario(request));
					if (totReg == 1) {
						perfilesModel.setCodError("000");
						perfilesModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}else {
						perfilesModel.setCodError("001");
						perfilesModel.setMensajeError("Error al eliminar el registro, consulte a su administrador.");
					}
				}
				JSONObject json = new JSONObject(perfilesModel);
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
	
	public String comboPerfiles(){
        Connection con = null;
        ResultadoConexion rc = null;
        PerfilesBean perfilesBean = new PerfilesBean();
        try{
        	
        	
        	HttpServletResponse response = ServletActionContext.getResponse();
        	HttpServletRequest request = ServletActionContext.getRequest();
        	response.setCharacterEncoding("UTF-8");
        	SiarexSession session = ObtenerSession.getSession(request);
        	PrintWriter out = response.getWriter();
            rc = getConnection(session.getEsquemaEmpresa());
            con = rc.getCon();

            PerfilesModel perfilesModel = new PerfilesModel();
            ArrayList<PerfilesForm> listaPerfiles = perfilesBean.comboPerfiles(con, rc.getEsquema(), "");
			perfilesModel.setData(listaPerfiles);
			JSONObject json = new JSONObject(perfilesModel);
			out.print(json);
            out.flush();
            out.close();
        }catch(Exception e){
        	Utils.imprimeLog("comboPerfiles(): ", e);
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
	
	
	public String comboPerfilesUsuario(){
        Connection con = null;
        ResultadoConexion rc = null;
        PerfilesBean perfilesBean = new PerfilesBean();
        try{
        	HttpServletResponse response = ServletActionContext.getResponse();
        	HttpServletRequest request = ServletActionContext.getRequest();
        	response.setCharacterEncoding("UTF-8");
        	SiarexSession session = ObtenerSession.getSession(request);
        	PrintWriter out = response.getWriter();
            rc = getConnection(session.getEsquemaEmpresa());
            con = rc.getCon();

            UsuariosBean usuarioBean = new UsuariosBean();
            UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
            
            PerfilesModel perfilesModel = new PerfilesModel();
            ArrayList<PerfilesForm> listaPerfiles = perfilesBean.comboPerfiles(con, rc.getEsquema(), usuarioForm.getNombreCortoPerfil());
			perfilesModel.setData(listaPerfiles);
			JSONObject json = new JSONObject(perfilesModel);
			out.print(json);
            out.flush();
            out.close();
        }catch(Exception e){
        	Utils.imprimeLog("comboPerfiles(): ", e);
            //logger.error(e);
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
	
	
	public String comboPerfilesEmpleado(){
        Connection con = null;
        ResultadoConexion rc = null;
        PerfilesBean perfilesBean = new PerfilesBean();
        try{
        	
        	HttpServletResponse response = ServletActionContext.getResponse();
        	HttpServletRequest request = ServletActionContext.getRequest();
        	response.setCharacterEncoding("UTF-8");
        	SiarexSession session = ObtenerSession.getSession(request);
        	PrintWriter out = response.getWriter();
            rc = getConnection(session.getEsquemaEmpresa());
            con = rc.getCon();

            PerfilesModel perfilesModel = new PerfilesModel();
            ArrayList<PerfilesForm> listaPerfiles = perfilesBean.comboPerfilesEmpleado(con, rc.getEsquema());
			perfilesModel.setData(listaPerfiles);
			JSONObject json = new JSONObject(perfilesModel);
			out.print(json);
            out.flush();
            out.close();
        }catch(Exception e){
        	Utils.imprimeLog("comboPerfiles(): ", e);
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
