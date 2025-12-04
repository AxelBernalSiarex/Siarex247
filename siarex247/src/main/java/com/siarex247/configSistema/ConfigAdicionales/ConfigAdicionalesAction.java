package com.siarex247.configSistema.ConfigAdicionales;

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
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class ConfigAdicionalesAction extends ConfigAdicionalesSupport{

	private static final long serialVersionUID = -7353981535606940789L;

	
	
	public String obtenerConfiguracion(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    HashMap<String, String> mapaOrd = ConfigAdicionalesBean.obtenerConfiguraciones(con, rc.getEsquema());
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
	
	
	
	public String grabarValidacionSAT(){
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConfigAdicionalesBean configAdicionalesBean = new ConfigAdicionalesBean();

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 

			    
			    
			    String bandComple = "N";
			    if ("on".equalsIgnoreCase(getRechazarComple())) {
			    	bandComple = "S";
			    }
			    
			    String bandBloq = "N";
			    if ("on".equalsIgnoreCase(getBloquearProveedores())) {
			    	bandBloq = "S";
			    }
			    
			    String bandValFecComple = "N";
			    if ("on".equalsIgnoreCase(getBandValidFechasComple())) {
			    	bandValFecComple = "S";
			    }
			    String bandNotifOrden = "N";
			    if ("on".equalsIgnoreCase(getNotifCorreoOrden())) {
			    	bandNotifOrden = "S";
			    }
			    
			    String bandValidarNotaSAT = "N";
			    if ("on".equalsIgnoreCase(getValidarNotas())) {
			    	bandValidarNotaSAT = "S";
			    }
			    
			    String bandValidarPedefinirSerie = "N";
			    if ("on".equalsIgnoreCase(getPredefinirValorSerie())) {
			    	bandValidarPedefinirSerie = "S";
			    }
			    
			    String bandValidarPermitirAcceso = "N";
			    if ("on".equalsIgnoreCase(getPermitirAccesoGenerador())) {
			    	bandValidarPermitirAcceso = "S";
			    }
			    
			    String bandValidarBloquearIMSS = "N";
			    if ("on".equalsIgnoreCase(getBloquearImss())) {
			    	bandValidarBloquearIMSS = "S";
			    }
			    
			    String bandValidarBloquearSAT = "N";
			    if ("on".equalsIgnoreCase(getBloquearSat())) {
			    	bandValidarBloquearSAT = "S";
			    }
			    
			    String bandValidarRfcReceptor = "N";
			    if ("on".equalsIgnoreCase(getRfcReceptor())) {
			    	bandValidarRfcReceptor = "S";
			    }
			    
			    String bandValidarCP = "N";
			    if ("on".equalsIgnoreCase(getValidarCP())) {
			    	bandValidarCP = "S";
			    }
			    
			    
			    String bandPermitirCartaPorte = "N";
			    if ("on".equalsIgnoreCase(getPermitirCartaPorte())) {
			    	bandPermitirCartaPorte = "S";
			    }
			    
			    
			    //int res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALIDA_SAT");
			    int res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "SERIE_FALTANTE");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "RECHAZAR_COMPLE");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "BLOQUEAR_PROVEEDORES");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "DIA_APARTIR_COMPLE");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "FECHA_APARTIR_COMPLE");
			    
			    
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "PREDEFINIR_VALOR_SERIE");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "PERMITIR_ACCESO_GENERADOR");
			    
			    if ("S".equalsIgnoreCase(bandValidarPedefinirSerie)) {
			    	res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALOR_SERIE_AMERICANAS");	
			    }
			    
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "NOTIF_CORREO_ORDEN");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALIDAR_NOTAS");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "BAND_VALIDFECHAS_COMPLE");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "BLOQUEAR_IMSS");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "BLOQUEAR_SAT");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "RFC_RECEPTOR");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALOR_RFC_RECEPTOR");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "VALIDA_CARTA_PORTE");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "PERMITIR_CARTA_PORTE");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "LABEL_LAYOUT_ORDEN");
			    res = configAdicionalesBean.eliminarVarible(con, session.getEsquemaEmpresa(), "LABEL_LAYOUT_MULTIPLE");
			    
			    
			    //res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "VALIDA_SAT", "Validacion de XML en SAT", bandValida);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "SERIE_FALTANTE", "Serie Faltante", getSerieFaltante());
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "RECHAZAR_COMPLE", "Rechazar Complementos de Pago Con Error", bandComple);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "BLOQUEAR_PROVEEDORES", "Bandera para bloquear operadores", bandBloq);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "DIA_APARTIR_COMPLE", "Dia a partir del cual desea bloquear a los proveedores", getDiaApartirComple());
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "FECHA_APARTIR_COMPLE", "Fecha se deben tomar en cuenta los complementos de pago", getFechaApartirComple());
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "BAND_VALIDFECHAS_COMPLE", "Validar Fechas del Complemento de Pago", bandValFecComple);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "NOTIF_CORREO_ORDEN", "Bandera para notificar correos de orden de compra al empleado.", bandNotifOrden);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "VALIDAR_NOTAS", "Bandera para validar las notas de credito en el SAT.", bandValidarNotaSAT);
			    
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "PREDEFINIR_VALOR_SERIE", "Predifinir valor general para facturas americanas", bandValidarPedefinirSerie);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "PERMITIR_ACCESO_GENERADOR", "Permitir acceso general al Generador de Facturas", bandValidarPermitirAcceso);
			    if ("S".equalsIgnoreCase(bandValidarPedefinirSerie)) {
			    	res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "VALOR_SERIE_AMERICANAS", "Valor General para generar facturas americanas", getValorSerieAmericanas());	
			    }
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "BLOQUEAR_IMSS", "Bloquear Acceso a Proveedores con Cert. IMSS Pendiente.", bandValidarBloquearIMSS);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "BLOQUEAR_SAT", "Bloquear Acceso a Proveedores con Cert. SAT Pendiente.", bandValidarBloquearSAT);

			   // logger.info("bandValidarBloquearSAT===>"+bandValidarRfcReceptor);
			   // logger.info("VALOR_RFC_RECEPTOR===>"+VALOR_RFC_RECEPTOR);
			    
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "RFC_RECEPTOR", "Bandera para validar el RFC Receptor", bandValidarRfcReceptor);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "VALOR_RFC_RECEPTOR", "Valor del RFC Receptor", getValorRfcReceptor());
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "VALIDA_CARTA_PORTE", "Bandera Carta Porte", bandValidarCP);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "PERMITIR_CARTA_PORTE", "Bandera para permitir facturas con carta porte rechazada", bandPermitirCartaPorte);
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "LABEL_LAYOUT_ORDEN", "Etiqueta para identificar layOut de la empresa por orden de compra", getLabelLayOutOrden());
			    res = configAdicionalesBean.insertVarible(con, session.getEsquemaEmpresa(), "LABEL_LAYOUT_MULTIPLE", "Etiqueta para identificar layOut de la empresa por orden multiple", getLabelLayOutMultiple());
			    
			   	
			    ConfigAdicionalesBean.setMapaVariables();
			    
			    Map<String, Object> json = new HashMap<String, Object>();
		       	json.put("ESTATUS", res > 0 ? "OK" : "ERROR");
		        out.print(JSONObject.toJSONString(json));
		        out.flush();
		        out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("grabarValidacionSAT(): ", e);
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
