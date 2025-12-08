package com.siarex247.configSistema.CorreosRespaldo;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class CorreosRespaldoAction extends CorreosRespaldoSupport{

	private static final long serialVersionUID = 1160533765093848194L;

	
	
	public String obtenerCorreos(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	CorreosRespaldoBean correosRespaldoBean = new CorreosRespaldoBean();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    HashMap<String, String> mapaOrd = correosRespaldoBean.obtenerConfiguraciones(con, session.getEsquemaEmpresa());
	            String jsonobj = JSONObject.toJSONString(mapaOrd);
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
	
	
	
	public String grabarConf(){
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	CorreosRespaldoBean correosRespaldoBean = new CorreosRespaldoBean();

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    int res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_ORDENES");
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_PAGOS");
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_RESPALDO");
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_COMPLEMENTO");
			    
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_1");
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_2");
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_3");
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_4");
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_5");
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_AVISO_UUID_BOVEDA_1");
			    res = correosRespaldoBean.eliminarVarible(con, session.getEsquemaEmpresa(), "CORREO_AVISO_UUID_BOVEDA_2");
			    
			    
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_ORDENES", "Correo de envio ordenes de compra", getCorreoOrdenes());
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_PAGOS", "Correo de envio ordenes de Pagos", getCorreoPagos());
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_RESPALDO", "Correo para respaldo de Facturas", getCorreoRespaldo());
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_COMPLEMENTO", "Correo de notificacion de complemento", getCorreoComplemento());
			    
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_1", "Correo Aviso Lista Negra SAT 1", getCorreoListaNegra1());
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_2", "Correo Aviso Lista Negra SAT 2", getCorreoListaNegra2());
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_3", "Correo Aviso Lista Negra SAT 3", getCorreoListaNegra3());
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_4", "Correo Aviso Lista Negra SAT 4", getCorreoListaNegra4());
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_LISTA_NEGRA_SAT_5", "Correo Aviso Lista Negra SAT 5", getCorreoListaNegra5());
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_AVISO_UUID_BOVEDA_1", "Correo Aviso Lista Negra SAT 5", getCorreoAvisoUuidBoveda1());
			    res = correosRespaldoBean.insertVarible(con, session.getEsquemaEmpresa(), "CORREO_AVISO_UUID_BOVEDA_2", "Correo Aviso Lista Negra SAT 5", getCorreoAvisoUuidBoveda2());

			    ConfigAdicionalesBean.setMapaVariables();
			    
	        	Map<String, Object> json = new HashMap<String, Object>();
	        	json.put("ESTATUS", res > 0 ? "OK" : "ERROR");
	            out.print(JSONObject.toJSONString(json));
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("grabarConf(): ", e);
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
