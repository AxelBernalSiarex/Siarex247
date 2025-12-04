package com.siarex247.configSistema.ConfigAdmin;

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

public class ConfigAdminAction extends ConfigAdminSupport{

	private static final long serialVersionUID = -7353981535606940789L;

	
	
	public String obtenerConfiguracion(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfigAdminBean configAdicionalesBean = new ConfigAdminBean();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    HashMap<String, String> mapaOrd = configAdicionalesBean.obtenerConfiguraciones(con, rc.getEsquema());
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
	
	
	
	public String grabarAdmin(){
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfigAdminBean configAdminBean = new ConfigAdminBean();

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				
				logger.info("************** GUARDANDO VARIABLES ******************");
				
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    int res = configAdminBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALIDA_SAT");
			    res = configAdminBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALIDAR_CIERRE_ADMIN");
			    res = configAdminBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALIDAR_COMPLEMENTO_ADMIN");
			    res = configAdminBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALIDAR_CONTRATO_CONFIDENCIALIDAD");
			    res = configAdminBean.eliminarVarible(con, session.getEsquemaEmpresa(), "BANDERA_OUTSOURCING_PROVEEDOR");
			    res = configAdminBean.eliminarVarible(con, session.getEsquemaEmpresa(), "BANDERA_LOGO_TOYOTA");
			    res = configAdminBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALIDA_XML_TIMBRADO");
			    
			    String bandValida = "N";
			    if ("on".equalsIgnoreCase(getValidaSat())) {
			    	bandValida = "S";
			    }
			    
			    String bandTimbrado = "N";
			    if ("on".equalsIgnoreCase(getValidaXMLTimbrado())) {
			    	bandTimbrado = "S";
			    }

			    String bandValidaCierre = "N";
			    if ("on".equalsIgnoreCase(getValidarCierreAdmin())) {
			    	bandValidaCierre = "S";
			    }
			    
			    String bandValidaComple = "N";
			    if ("on".equalsIgnoreCase(getValidarComplementoAdmin())) {
			    	bandValidaComple = "S";
			    }
			   
			    String bandValidaConfidencial = "N";
			    if ("on".equalsIgnoreCase(getValidarContratoConfidencialidad())) {
			    	bandValidaConfidencial = "S";
			    }
			    
			    String bandOutsourcingProveedor = "N";
			    if ("on".equalsIgnoreCase(getBanderaOutsourcingProveedor())) {
			    	bandOutsourcingProveedor = "S";
			    }
			    
			    String bandLogoToyota = "N";
			    if ("on".equalsIgnoreCase(getBanderaLogoToyota())) {
			    	bandLogoToyota = "S";
			    }
			    
			    res = configAdminBean.insertVarible(con, session.getEsquemaEmpresa(), "VALIDA_SAT", "Validacion de XML en SAT", bandValida);
			    res = configAdminBean.insertVarible(con, session.getEsquemaEmpresa(), "VALIDA_XML_TIMBRADO", "Bandera para validar XML en timbrado con cobro", bandTimbrado);
			    res = configAdminBean.insertVarible(con, session.getEsquemaEmpresa(), "VALIDAR_CIERRE_ADMIN", "Bandera para validar fecha de cierre el administrador.", bandValidaCierre);
			    res = configAdminBean.insertVarible(con, session.getEsquemaEmpresa(), "VALIDAR_COMPLEMENTO_ADMIN", "Bandera para validar complementos el administrador", bandValidaComple);
			    res = configAdminBean.insertVarible(con, session.getEsquemaEmpresa(), "VALIDAR_CONTRATO_CONFIDENCIALIDAD", "Bandera para validar contrato de Confidencialidad", bandValidaConfidencial);
			    res = configAdminBean.insertVarible(con, session.getEsquemaEmpresa(), "BANDERA_OUTSOURCING_PROVEEDOR", "Bandera para activar y desactivar la pantalla del visor y mostrar outsourcing", bandOutsourcingProveedor);
			    res = configAdminBean.insertVarible(con, session.getEsquemaEmpresa(), "BANDERA_LOGO_TOYOTA", "Bandera para activar y desactivar el logo de toyota", bandLogoToyota);
			    
			    
			    ConfigAdicionalesBean.setMapaVariables();
		        
			    Map<String, Object> json = new HashMap<String, Object>();
		        json.put("ESTATUS", res > 0 ? "OK" : "ERROR");
		        out.print(JSONObject.toJSONString(json));
		        out.flush();
		        out.close();
		       
			}
		}
		catch(Exception e){
			Utils.imprimeLog("grabarAdmin(): ", e);
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
