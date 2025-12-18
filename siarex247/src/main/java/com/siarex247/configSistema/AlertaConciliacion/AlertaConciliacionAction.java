package com.siarex247.configSistema.AlertaConciliacion;

import java.io.PrintWriter;
import java.sql.Connection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
	
public class AlertaConciliacionAction extends AlertaConciliacionSupport {
	private static final long serialVersionUID = 3702680182545645L;
	
	public String datosConfProceso() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		AlertaConciliacionBean confOutBean = new AlertaConciliacionBean();
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
				JSONObject json = null;
				
				AlertaConciliacionForm configForm = confOutBean.buscarConfProceso(con, rc.getEsquema(), "PRO01");
				json = new JSONObject(configForm);
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
	
	
	public String configuracionProceso(){
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	AlertaConciliacionBean confBean = new AlertaConciliacionBean();

		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");

			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    int res = confBean.configuracionProceso(con, rc.getEsquema(), getDiaEjecucion(), getSubject(), getTexto(), getDestinatario1(), getDestinatario2(), getDestinatario3(), getDestinatario4(), getDestinatario5(),getAdjuntar(), getActivar(), getUsuario(request), "PRO01");
	            AlertaConciliacionModel alertaModel = new AlertaConciliacionModel();
	            if (res == 0) {
	            	alertaModel.setCodError("001");
	            	alertaModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
				}else {
					alertaModel.setCodError("000");
					alertaModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				}
	            
	            JSONObject json = new JSONObject(alertaModel);
				out.print(json);
	            out.flush();
	            out.close();
	            
			}
		}
		catch(Exception e){
			Utils.imprimeLog("configuracionProceso(): ", e);
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
	
}
