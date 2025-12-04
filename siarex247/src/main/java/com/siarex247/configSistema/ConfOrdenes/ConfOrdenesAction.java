package com.siarex247.configSistema.ConfOrdenes;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.simple.JSONObject;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ConfOrdenesAction extends ConfOrdenesSupport{

	private static final long serialVersionUID = 4296431825330318602L;

	
	
	public String obtenerConfOrden()
    {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfOrdenesBean confOrdenesBean = new ConfOrdenesBean();
    	
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    HashMap<String, String> mapaOrd = confOrdenesBean.obtenerConfiguracion(con, session.getEsquemaEmpresa());
		
			    AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
				
			    mapaOrd.put("EMPRESA", empresaForm.getNombreLargo());
			    
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
	
	
	
	public String actualizaOrdenes(){
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfOrdenesBean confOrdenesBean = new ConfOrdenesBean();

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    int res = confOrdenesBean.guardarConfiguracion(con, session.getEsquemaEmpresa(), getLlaveFinPDF(), 
			    		getLlaveOrdenes(), getLlaveFinOrdenes(), getLlaveTotal(), getLlaveFinTotal(), getLlaveVendor(), getLlaveFinVendor(), getLlaveMoneda(), getLlaveFinMondeda());
            	Map<String, Object> json = new HashMap<String, Object>();
            	
            	ConfigAdicionalesBean.setMapaVariables();
            	
	        	json.put("ESTATUS", res > 0 ? "OK" : "ERROR");
	            out.print(JSONObject.toJSONString(json));
	            out.flush();
	            out.close();
		       
			}
		}
		catch(Exception e){
			Utils.imprimeLog("actualizaOrdenes(): ", e);
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
