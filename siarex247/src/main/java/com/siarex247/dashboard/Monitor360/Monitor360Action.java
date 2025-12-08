package com.siarex247.dashboard.Monitor360;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.JSONObject;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaSATForm;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaSATModel;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Monitor360Action extends Monitor360Support{


	private static final long serialVersionUID = -225268995146274831L;

	
	public String calcularGrafica() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		Monitor360Bean monitor360Bean = new Monitor360Bean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				logger.info("********** calculando grafica **********");
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
			//	logger.info("Annio===>"+getAnnio());
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
				Monitor360Form monitor360Form = monitor360Bean.calcularGrafica(con, rc.getEsquema(), empresaForm.getRfc(), getAnnio(), getMes(), getTipo(), getContribuyente(), getTipoMoneda());
				JSONObject json = new JSONObject(monitor360Form);
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
	
	public String calcularCabecero() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		Monitor360Bean monitor360Bean = new Monitor360Bean();
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
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
				logger.info("Annio===>"+getAnnio());
				logger.info("Contribuyente===>"+getContribuyente());
				logger.info("tipoMoneda===>"+getTipoMoneda());
				Monitor360Form monitor360Form = monitor360Bean.calcularUniverso(con, rc.getEsquema(), empresaForm.getRfc(), getAnnio(), getMes(), getTipo(), getContribuyente(), getTipoMoneda());
				JSONObject json = new JSONObject(monitor360Form);
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
	
	
	public String calcularIngresos() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		Monitor360Bean monitor360Bean = new Monitor360Bean();
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
				Monitor360Form monitor360Form = new Monitor360Form();
				HashMap<String, String>  mapaIngresos = monitor360Bean.calcularIngresos(con, rc.getEsquema(), getAnnio(), getMes(), getTipo(), getTipoMoneda());
				HashMap<String, String>  mapaEgresos  = monitor360Bean.calcularEgresos(con, rc.getEsquema(), getAnnio(), getMes(), getTipo(), getContribuyente(), getTipoMoneda());
				
				
				monitor360Form.setTotalIngreso(mapaIngresos.get("TOTAL_INGRESOS"));
				monitor360Form.setTotalEgreso(mapaEgresos.get("TOTAL_EGRESOS"));
				
				monitor360Form.setSubTotalIngreso(mapaIngresos.get("SUBTOTAL_INGRESOS"));
				monitor360Form.setSubTotaEngreso(mapaEgresos.get("SUBTOTAL_EGRESOS"));
				
				monitor360Form.setImpRetenidoIngreso(mapaIngresos.get("IMPRETENIDOS_INGRESOS"));
				monitor360Form.setImpRetenidoEgreso(mapaEgresos.get("IMPRETENIDOS_EGRESOS"));
				
				monitor360Form.setImpTrasladoIngreso(mapaIngresos.get("IMPTRASLADO_INGRESOS"));
				monitor360Form.setImpTrasladoEgreso(mapaEgresos.get("IMPTRASLADO_EGRESOS"));
				
				JSONObject json = new JSONObject(monitor360Form);
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
	
	public String obtenerAnnios() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		Monitor360Bean monitor360Bean = new Monitor360Bean();
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
				Monitor360Model monitor360Model = new Monitor360Model();
				ArrayList<Monitor360Form> listaPuestos = monitor360Bean.obtenerAnnios(con, rc.getEsquema());
				monitor360Model.setData(listaPuestos);
				JSONObject json = new JSONObject(monitor360Model);
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
	
	
	public String obtenerContribuyentes() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		Monitor360Bean monitor360Bean = new Monitor360Bean();
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
				Monitor360Model monitor360Model = new Monitor360Model();
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
				
				ArrayList<Monitor360Form> listaDetalle = monitor360Bean.obtenerContribuyentes(con, rc.getEsquema(), getTipo(), empresaForm.getRfc(), empresaForm.getNombreLargo());
				monitor360Model.setData(listaDetalle);
				JSONObject json = new JSONObject(monitor360Model);
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
	
	
	public String detalleMetadata(){
    	
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		Monitor360Bean monitor360Bean = new Monitor360Bean();
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
	    	
	    	response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    DescargaSATModel descargaModel = new DescargaSATModel();
			    
			    AccesoBean accesoBean = new AccesoBean();
			    EmpresasForm  empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
			    
			    String fechaIni  = monitor360Bean.armarFechaInicial(getAnnio(), getMes());
	            String fechaFin  = monitor360Bean.armarFechaFinal(getAnnio(), getMes());
	            
	           // logger.info(" ************* METADATA *********** ");
			    // con, rc.getEsquema(), empresaForm.getRfc(), getAnnio(), getMes(), getTipo(), getContribuyente()
			    ArrayList<DescargaSATForm> listaDetalle = monitor360Bean.detalleDescarga(con, rc.getEsquema(), empresasForm.getRfc(), 
			    		fechaIni, fechaFin, getTipoConsulta(), getTipo(), getContribuyente(), getTipoMoneda());
			    
			    descargaModel.setData(listaDetalle);
			    descargaModel.setRecordsTotal(listaDetalle.size());
			    descargaModel.setRecordsFiltered(listaDetalle.size());
			    descargaModel.setDraw(-1);
				 
			    descargaModel.setRecordsTotal(listaDetalle.size());
				JSONObject json = new JSONObject(descargaModel);
				out.print(json);
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog(" ", e);
		}
		finally{
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
	
	
	public String detalleListaNegra(){
    	
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		Monitor360Bean monitor360Bean = new Monitor360Bean();
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
	    	
	    	response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    DescargaSATModel descargaModel = new DescargaSATModel();
			    
			    AccesoBean accesoBean = new AccesoBean();
			    EmpresasForm  empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
			    
			    String fechaIni  = monitor360Bean.armarFechaInicial(getAnnio(), getMes());
	            String fechaFin  = monitor360Bean.armarFechaFinal(getAnnio(), getMes());
	            
	            //logger.info(" ************* METADATA LISTA *********** ");
			    // con, rc.getEsquema(), empresaForm.getRfc(), getAnnio(), getMes(), getTipo(), getContribuyente()
			    ArrayList<DescargaSATForm> listaDetalle = monitor360Bean.detalleListaNegra(con, rc.getEsquema(), empresasForm.getRfc(), 
			    		getAnnio(), fechaIni, fechaFin, getTipoConsulta(), getTipo(), getContribuyente(), getTipoMoneda());
			    
			    descargaModel.setData(listaDetalle);
			    descargaModel.setRecordsTotal(listaDetalle.size());
			    descargaModel.setRecordsFiltered(listaDetalle.size());
			    descargaModel.setDraw(-1);
				 
			    descargaModel.setRecordsTotal(listaDetalle.size());
				JSONObject json = new JSONObject(descargaModel);
				out.print(json);
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog(" ", e);
		}
		finally{
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
	
	public String calcularGraficaPorTipoComprobante() throws Exception {
	    Connection con = null;
	    ResultadoConexion rc = null;
	    PrintWriter out = null;
	   


	    try {
	        HttpServletRequest request = ServletActionContext.getRequest();
	        HttpServletResponse response = ServletActionContext.getResponse();
	        SiarexSession session = ObtenerSession.getSession(request);
	        
	        String esquema = session.getEsquemaEmpresa();
	        if (esquema == null || "".equals(esquema)) {
	            return Action.LOGIN;
	        }

	        String annio = request.getParameter("annio");
	        String tipoMoneda = request.getParameter("tipoMoneda");
	        String contribuyente = request.getParameter("contribuyente");
	        String mes = request.getParameter("mes"); //

	        rc = getConnection(esquema);
	        con = rc.getCon();

	        Monitor360Bean monitor360Bean = new Monitor360Bean();
	        Monitor360Form form = monitor360Bean.calcularGraficaPorTipoComprobante( con, rc.getEsquema(), contribuyente, annio, tipoMoneda, mes  );

	        response.setContentType("application/json;charset=UTF-8");
	        response.setCharacterEncoding("UTF-8");
	        out = response.getWriter();

	        // Serializar manualmente
	        StringBuilder json = new StringBuilder();
	        json.append("{");

	        // 1. mapaPorTipo
	        json.append("\"data\":{");
	        Map<String, double[]> mapa = form.getMapaPorTipo();
	        int tipoIndex = 0;
	        for (Map.Entry<String, double[]> entry : mapa.entrySet()) {
	            json.append("\"").append(entry.getKey()).append("\":[");
	            double[] valores = entry.getValue();
	            for (int i = 0; i < valores.length; i++) {
	                json.append(valores[i]);
	                if (i < valores.length - 1) json.append(",");
	            }
	            json.append("]");
	            if (tipoIndex < mapa.size() - 1) json.append(",");
	            tipoIndex++;
	        }
	        json.append("},");

	        // 2. número mayor
	        json.append("\"numeroMayor\":\"").append(form.getNumeroMayor()).append("\",");

	        // 3. intervalo
	        json.append("\"intervalo\":").append(form.getIntervalo());

	        json.append("}");

	        out.print(json.toString());
	        
	        
	       // logger.info("json=====>"+json.toString());
	        

	    } catch (Exception e) {
	        throw new Exception("Error en calcularGraficaPorTipoComprobante(): " + e.getMessage(), e);
	    } finally {
	    	try {
	    		if (con != null) {
	    			con.close();
	    		}
	    		con = null;
	    		
	    		if (out != null) out.close();
		        
	    	}catch(Exception e) {
	    		con = null;
	    	}
	        
	    }

	    return null;
	}
	
}
