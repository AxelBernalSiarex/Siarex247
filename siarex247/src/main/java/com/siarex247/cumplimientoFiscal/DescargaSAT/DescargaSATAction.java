package com.siarex247.cumplimientoFiscal.DescargaSAT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.JSONObject;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaForm;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DescargaSATAction extends DescargaSATSupport{

	private static final long serialVersionUID = -7382017086744937366L;

	private InputStream inputStream;
	
	public String detalleRecibidos(){
    	
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		DescargaSATBean descargaSatBean = new DescargaSATBean();
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
			    
			    
			    int pageSize = 15;

			    String fechaInicial = Utils.noNulo(getFechaInicialDescarga());
		  		String fechaFinal = Utils.noNulo(getFechaFinalDescarga());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			 
		  		}
		  		
		  		dateOperator = "bt";

		  		
			 // ===== Detalle con operadores DX-like =====
			 ArrayList<DescargaSATForm> listaDetalle = descargaSatBean.detalleDescarga(
			     con,
			     rc.getEsquema(),
			     // ===== básicos (valores) =====
			     empresasForm.getRfc(),                               // rfcReceptor
			     Utils.noNulo(getRfcEmisor()),                        // rfcEmisor
			     Utils.noNuloNormal(getRazonSocialEmisor()),          // razonSocialEmisor
			     Utils.noNulo(getExisteBovedaDescarga()),             // existeBovedaDescarga
			     Utils.noNulo(getTipoComprobanteDescarga()),          // tipoComprobante
			     fechaInicial,                           // fechaInicial (global)
			     fechaFinal,                             // fechaFinal   (global)
			     Utils.noNuloNormal(getUuidDescarga()),               // uuidDescarga
			     Utils.noNuloNormal(getEstatusCFDI()),                // estatusCFDI
			     getStart(),                                          // inicio
			     pageSize,                                            // length
			     false,                                               // isExcel

			     // ===== extras de texto (valores) =====
			     Utils.noNuloNormal(getRazonSocialReceptor()),        // nombreReceptor
			     Utils.noNulo(getRfcPac()),                           // rfcPac

			     // ===== operadores/valores DX-like =====
			     // Texto: operador para el valor que ya pasaste arriba
			     Utils.noNulo(getUuidOperator()),                     // uuidOperator   (aplica sobre uuidDescarga)
			     Utils.noNulo(getRfcEmiOperator()),                   // rfcEmiOperator (aplica sobre rfcEmisor)
			     Utils.noNulo(getNomEmiOperator()),                   // nomEmiOperator (aplica sobre razonSocialEmisor)
			     Utils.noNulo(getRfcRecOperator()),                   // rfcRecOperator (aplica sobre rfcReceptor)
			     Utils.noNulo(getNomRecOperator()),                   // nomRecOperator (aplica sobre razón social receptor)
			     Utils.noNulo(getPacOperator()),                      // pacOperator    (aplica sobre PAC)
			     Utils.noNulo("equals"),                   // efectoOperator (aplica sobre tipoComprobante != ALL)
			     Utils.noNulo(getEstatusOperator()),                  // estatusOperator
			     Utils.noNulo(getBovedaOperator()),                   // bovedaOperator

			     // Numérico – Monto
			     Utils.noNulo(getMontoOperator()),                    // montoOperator (eq, ne, lt, gt, le, ge, bt)
			     Utils.noNulo(getMontoV1()),                          // montoV1
			     Utils.noNulo(getMontoV2()),                          // montoV2

			     // Fechas – Emisión (con fallback a las globales en el bean)
			     Utils.noNulo(dateOperator),                  // emiDateOperator
			     Utils.noNulo(getEmiDateV1()),                        // emiDateV1 (YYYY-MM-DD)
			     Utils.noNulo(getEmiDateV2()),                        // emiDateV2 (YYYY-MM-DD)

			     // Fechas – Certificación
			     Utils.noNulo(getCerDateOperator()),                  // cerDateOperator
			     Utils.noNulo(getCerDateV1()),                        // cerDateV1
			     Utils.noNulo(getCerDateV2()),                        // cerDateV2

			     // Fechas – Cancelación
			     Utils.noNulo(getCanDateOperator()),                  // canDateOperator
			     Utils.noNulo(getCanDateV1()),                        // canDateV1
			     Utils.noNulo(getCanDateV2())                         // canDateV2
			 );

			 // ===== Total con los mismos filtros (consistencia con detalle) =====
			 int totRegistros = descargaSatBean.getTotalRegistros(
			     con,
			     rc.getEsquema(),
			     // ===== básicos (valores) =====
			     empresasForm.getRfc(),                               // rfcReceptor
			     Utils.noNulo(getRfcEmisor()),                        // rfcEmisor
			     Utils.noNuloNormal(getRazonSocialEmisor()),          // razonSocialEmisor
			     Utils.noNulo(getExisteBovedaDescarga()),             // existeBovedaDescarga
			     Utils.noNulo(getTipoComprobanteDescarga()),          // tipoComprobante
			     fechaInicial,                           // fechaInicial
			     fechaFinal,                             // fechaFinal
			     Utils.noNuloNormal(getUuidDescarga()),               // uuidDescarga
			     Utils.noNuloNormal(getEstatusCFDI()),                // estatusCFDI

			     // ===== extras de texto (valores) =====
			     Utils.noNuloNormal(getRazonSocialReceptor()),        // nombreReceptor
			     Utils.noNulo(getRfcPac()),                           // rfcPac

			     // ===== operadores/valores DX-like =====
			     Utils.noNulo(getUuidOperator()),
			     Utils.noNulo(getRfcEmiOperator()),
			     Utils.noNulo(getNomEmiOperator()),
			     Utils.noNulo(getRfcRecOperator()),
			     Utils.noNulo(getNomRecOperator()),
			     Utils.noNulo(getPacOperator()),
			     Utils.noNulo(getEfectoOperator()),
			     Utils.noNulo(getEstatusOperator()),
			     Utils.noNulo(getBovedaOperator()),

			     Utils.noNulo(getMontoOperator()),
			     Utils.noNulo(getMontoV1()),
			     Utils.noNulo(getMontoV2()),

			     Utils.noNulo(dateOperator),
			     Utils.noNulo(getEmiDateV1()),
			     Utils.noNulo(getEmiDateV2()),

			     Utils.noNulo(getCerDateOperator()),
			     Utils.noNulo(getCerDateV1()),
			     Utils.noNulo(getCerDateV2()),

			     Utils.noNulo(getCanDateOperator()),
			     Utils.noNulo(getCanDateV1()),
			     Utils.noNulo(getCanDateV2())
			 );

			    
			    
			    descargaModel.setData(listaDetalle);
			    
			    descargaModel.setRecordsTotal(totRegistros);
			    descargaModel.setRecordsFiltered(totRegistros);
			    descargaModel.setDraw(getDraw());
				 
				 
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


	public String consultarFechaMinima(){
    	
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		DescargaSATBean descargaSatBean = new DescargaSATBean();
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
			    
			    AccesoBean accesoBean = new AccesoBean();
			    EmpresasForm  empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
			    
			   
			    BovedaForm bovedaForm = new BovedaForm();
			 // ===== Detalle con operadores DX-like =====
			    String fechaMinima = descargaSatBean.consultarFechaMinima(con, rc.getEsquema(), empresasForm.getRfc());
			    String fechaFinal = UtilsFechas.getFechayyyyMMdd();
			    bovedaForm.setFechaInicial(fechaMinima.substring(0, 10));
				bovedaForm.setFechaFinal(fechaFinal);
			    
				JSONObject json = new JSONObject(bovedaForm);
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



	public String generaReporte() throws Exception {
		  Connection con = null;
		  ResultadoConexion rc = null;
		  DescargaSATBean descargaSatBean = new DescargaSATBean();
		  HttpServletRequest request = ServletActionContext.getRequest();
		  SXSSFWorkbook myWorkBook = new SXSSFWorkbook(100);
		  SXSSFSheet mySheet = myWorkBook.createSheet("Detalle Recibidos");

		  try{
		    SiarexSession session = ObtenerSession.getSession(request);
		    if ("".equals(session.getEsquemaEmpresa())){
		      myWorkBook.close();
		      return Action.LOGIN;
		    } else {
		      rc = getConnection(session.getEsquemaEmpresa());
		      con = rc.getCon();

		      AccesoBean accesoBean = new AccesoBean();
		      EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());

		      // >>>> SIN paginación y con isExcel=true
		      
		      String fechaInicial = Utils.noNulo(getFechaInicialDescarga());
		  		String fechaFinal = Utils.noNulo(getFechaFinalDescarga());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			 
		  		}
		  		
		  		dateOperator = "bt";
		  		
		      ArrayList<DescargaSATForm> listaDetalle = descargaSatBean.detalleDescarga(
		        con,
		        rc.getEsquema(),
		        empresasForm.getRfc(),                      // rfcReceptor
		        Utils.noNulo(getRfcEmisor()),               // rfcEmisor
		        Utils.noNuloNormal(getRazonSocialEmisor()), // razonSocialEmisor
		        Utils.noNulo(getExisteBovedaDescarga()),    // existeBovedaDescarga
		        Utils.noNulo(getTipoComprobanteDescarga()), // tipoComprobante
		        fechaInicial,                  // fechaInicial
		        fechaFinal,                    // fechaFinal
		        Utils.noNuloNormal(getUuidDescarga()),      // uuidDescarga
		        Utils.noNuloNormal(getEstatusCFDI()),       // estatusCFDI
		        0,                                          // startPaginado -> 0
		        0,                                          // endPaginado   -> 0 (ignorado)
		        true,                                       // isExcel       -> true  (NO LIMIT)

		        // extras de texto
		        Utils.noNuloNormal(getRazonSocialReceptor()),
		        Utils.noNulo(getRfcPac()),

		        // operadores
		        Utils.noNulo(getUuidOperator()),
		        Utils.noNulo(getRfcEmiOperator()),
		        Utils.noNulo(getNomEmiOperator()),
		        Utils.noNulo(getRfcRecOperator()),
		        Utils.noNulo(getNomRecOperator()),
		        Utils.noNulo(getPacOperator()),
		        Utils.noNulo(getEfectoOperator()),
		        Utils.noNulo(getEstatusOperator()),
		        Utils.noNulo(getBovedaOperator()),

		        // numérico
		        Utils.noNulo(getMontoOperator()),
		        Utils.noNulo(getMontoV1()),
		        Utils.noNulo(getMontoV2()),

		        // fechas
		        Utils.noNulo(dateOperator),
		        Utils.noNulo(getEmiDateV1()),
		        Utils.noNulo(getEmiDateV2()),
		        Utils.noNulo(getCerDateOperator()),
		        Utils.noNulo(getCerDateV1()),
		        Utils.noNulo(getCerDateV2()),
		        Utils.noNulo(getCanDateOperator()),
		        Utils.noNulo(getCanDateV1()),
		        Utils.noNulo(getCanDateV2())
		      );

		      int REG_NUEVA_PAGINA = 1_000_000;
		      if (listaDetalle.size() >= REG_NUEVA_PAGINA) {
		        SXSSFSheet mySheet2 = myWorkBook.createSheet("Detalle Recibidos (2)");
		        descargaSatBean.generaReporte(myWorkBook, mySheet,  listaDetalle, session.getLenguaje(), 0, REG_NUEVA_PAGINA);
		        descargaSatBean.generaReporte(myWorkBook, mySheet2, listaDetalle, session.getLenguaje(), REG_NUEVA_PAGINA, listaDetalle.size());
		      } else {
		        descargaSatBean.generaReporte(myWorkBook, mySheet, listaDetalle, session.getLenguaje(), 0, listaDetalle.size());
		      }

		      ByteArrayOutputStream boas = new ByteArrayOutputStream();
		      myWorkBook.write(boas);
		      setInputStream(new ByteArrayInputStream(boas.toByteArray()));
		      myWorkBook.close();
		    }
		    myWorkBook.close();
		  } catch(Exception e){
		    Utils.imprimeLog("", e);
		  } finally {
		    try { if (con != null) con.close(); } catch(Exception ignore){}
		  }
		  return SUCCESS;
		}




public String consultarUltimaFecha(){
    	
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		DescargaSATBean descargaSatBean = new DescargaSATBean();
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
			    
			    DescargaSATForm descargaSATForm = descargaSatBean.consultarUltimaFecha(con, rc.getEsquema());
			    
				JSONObject json = new JSONObject(descargaSATForm);
				out.print(json);
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("detalleSat(): ", e);
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
			
			//=========================
			//EMITIDOS – detalle (DX-like)
			//=========================
			public String detalleEmitidos(){
			HttpServletResponse response = null;
			HttpServletRequest  request  = null;
			PrintWriter out = null;
			
			Connection con = null;
			ResultadoConexion rc = null;
			
			DescargaSATBean descargaSatBean = new DescargaSATBean();
			try{
			    response = ServletActionContext.getResponse();
			    request  = ServletActionContext.getRequest();
			
			    response.setContentType("application/json; charset=UTF-8");
			    response.setCharacterEncoding("UTF-8");
			    out = response.getWriter();
			
			    SiarexSession session = ObtenerSession.getSession(request);
			    if ("".equals(session.getEsquemaEmpresa())){
			        return Action.LOGIN;
			    }
			
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			
			    AccesoBean   accesoBean  = new AccesoBean();
			    EmpresasForm empresas    = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
			    int pageSize = 15;

			    String fechaInicial = Utils.noNulo(getFechaInicialDescarga());
		  		String fechaFinal = Utils.noNulo(getFechaFinalDescarga());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			 
		  		}
		  		
		  		dateOperator = "bt";
		  		
		  		
			    // ===== Detalle con operadores DX-like =====
			    ArrayList<DescargaSATForm> listaDetalle = descargaSatBean.detalleDescargaEmitidos(
			        con, rc.getEsquema(),
			        // ===== básicos/valores =====
			        empresas.getRfc(),                           // rfcEmisor (fijo: empresa)
			        Utils.noNulo(getRfcReceptor()),             // rfcReceptor (filtro)
			        Utils.noNuloNormal(getRazonSocialReceptor()),// nombre Receptor
			        Utils.noNulo(getExisteBovedaDescarga()),    // Bóveda
			        Utils.noNulo(getTipoComprobanteDescarga()), // Efecto
			        fechaInicial,                  // Rango (global cabecera)
			        fechaFinal,
			        Utils.noNuloNormal(getUuidDescarga()),      // UUID
			        Utils.noNuloNormal(getEstatusCFDI()),       // Estatus
			        getStart(), pageSize, false,                // paginado, isExcel
			
			        // ===== extras de texto (si los usas en cabecera) =====
			        Utils.noNuloNormal(getRazonSocialEmisor()), // Nombre Emisor (col 3)
			        Utils.noNulo(getRfcPac()),                  // PAC (para emitidos lo normal es EMISOR_PAC)
			
			        // ===== operadores (texto) =====
			        Utils.noNulo(getUuidOperator()),
			        Utils.noNulo(getRfcEmiOperator()),
			        Utils.noNulo(getNomEmiOperator()),
			        Utils.noNulo(getRfcRecOperator()),
			        Utils.noNulo(getNomRecOperator()),
			        Utils.noNulo(getPacOperator()),
			        Utils.noNulo(getEfectoOperator()),
			        Utils.noNulo(getEstatusOperator()),
			        Utils.noNulo(getBovedaOperator()),
			
			        // ===== numérico =====
			        Utils.noNulo(getMontoOperator()),
			        Utils.noNulo(getMontoV1()),
			        Utils.noNulo(getMontoV2()),
			
			        // ===== fechas =====
			        Utils.noNulo(dateOperator), Utils.noNulo(getEmiDateV1()), Utils.noNulo(getEmiDateV2()),
			        Utils.noNulo(getCerDateOperator()), Utils.noNulo(getCerDateV1()), Utils.noNulo(getCerDateV2()),
			        Utils.noNulo(getCanDateOperator()), Utils.noNulo(getCanDateV1()), Utils.noNulo(getCanDateV2())
			    );
			
			    // ===== Total consistente =====
			    int totRegistros = descargaSatBean.getTotalRegistrosEmitidos(
			        con, rc.getEsquema(),
			        empresas.getRfc(),
			        Utils.noNulo(getRfcReceptor()),
			        Utils.noNuloNormal(getRazonSocialReceptor()),
			        Utils.noNulo(getExisteBovedaDescarga()),
			        Utils.noNulo(getTipoComprobanteDescarga()),
			        fechaInicial, fechaFinal,
			        Utils.noNuloNormal(getUuidDescarga()),
			        Utils.noNuloNormal(getEstatusCFDI()),
			
			        Utils.noNuloNormal(getRazonSocialEmisor()),
			        Utils.noNulo(getRfcPac()),
			
			        Utils.noNulo(getUuidOperator()),
			        Utils.noNulo(getRfcEmiOperator()),
			        Utils.noNulo(getNomEmiOperator()),
			        Utils.noNulo(getRfcRecOperator()),
			        Utils.noNulo(getNomRecOperator()),
			        Utils.noNulo(getPacOperator()),
			        Utils.noNulo(getEfectoOperator()),
			        Utils.noNulo(getEstatusOperator()),
			        Utils.noNulo(getBovedaOperator()),
			        Utils.noNulo(getMontoOperator()),
			        Utils.noNulo(getMontoV1()),
			        Utils.noNulo(getMontoV2()),
			        Utils.noNulo(dateOperator),
			        Utils.noNulo(getEmiDateV1()),
			        Utils.noNulo(getEmiDateV2()),
			        Utils.noNulo(getCerDateOperator()),
			        Utils.noNulo(getCerDateV1()),
			        Utils.noNulo(getCerDateV2()),
			        Utils.noNulo(getCanDateOperator()),
			        Utils.noNulo(getCanDateV1()),
			        Utils.noNulo(getCanDateV2())
			    );
			
			    DescargaSATModel descargaModel = new DescargaSATModel();
			    descargaModel.setData(listaDetalle);
			    descargaModel.setRecordsTotal(totRegistros);
			    descargaModel.setRecordsFiltered(totRegistros);
			    descargaModel.setDraw(getDraw());
			
			    out.print(new JSONObject(descargaModel));
			    out.flush();
			    out.close();
			
			}catch(Exception e){
			    Utils.imprimeLog("detalleEmitidos(): ", e);
			}finally{
			    try{ if (con!=null) con.close(); }catch(Exception ignore){}
			}
			return SUCCESS;
			}
			



public String consultarFechaMinimaEmitidos(){
	
	HttpServletResponse response = null;
	HttpServletRequest request = null;
	PrintWriter out = null;

	Connection con = null;
	ResultadoConexion rc = null;
	DescargaSATBean descargaSatBean = new DescargaSATBean();
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
		    
		    AccesoBean accesoBean = new AccesoBean();
		    EmpresasForm  empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
		    
		   
		    BovedaForm bovedaForm = new BovedaForm();
		 // ===== Detalle con operadores DX-like =====
		    String fechaMinima = descargaSatBean.consultarFechaMinimaEmitidos(con, rc.getEsquema(), empresasForm.getRfc());
		    String fechaFinal = UtilsFechas.getFechayyyyMMdd();
		    bovedaForm.setFechaInicial(fechaMinima.substring(0, 10));
			bovedaForm.setFechaFinal(fechaFinal);
		    
			JSONObject json = new JSONObject(bovedaForm);
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



public String generaReporteEmitidos() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		DescargaSATBean descargaSatBean = new DescargaSATBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		SXSSFWorkbook  myWorkBook = new SXSSFWorkbook(100); // Keep 100 rows in memory
		SXSSFSheet mySheet = myWorkBook.createSheet("Detalle Emitidos");
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				myWorkBook.close();
				return Action.LOGIN;
			}else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
			    
				AccesoBean accesoBean = new AccesoBean();
			    EmpresasForm  empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
			    
			 /*   ArrayList<DescargaSATForm> listaDetalle = descargaSatBean.detalleDescargaEmitidos(con, rc.getEsquema(), empresasForm.getRfc(), Utils.noNulo(getRfcReceptor()),  Utils.noNuloNormal(getRazonSocialReceptor()), Utils.noNulo(getExisteBovedaDescarga()), Utils.noNulo(getTipoComprobanteDescarga()), 
						getFechaInicialDescarga(), getFechaFinalDescarga(), Utils.noNuloNormal(getUuidDescarga()),  Utils.noNuloNormal(getEstatusCFDI()), 0, 0, true);
				*/
			    int pageSize = 15;
			    
			    String fechaInicial = Utils.noNulo(getFechaInicialDescarga());
		  		String fechaFinal = Utils.noNulo(getFechaFinalDescarga());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			 
		  		}
		  		
		  		dateOperator = "bt";
		  		
			    ArrayList<DescargaSATForm> listaDetalle = descargaSatBean.detalleDescargaEmitidos(
			            con, rc.getEsquema(),
			            // ===== básicos/valores =====
			            empresasForm.getRfc(),                           // rfcEmisor (fijo: empresa)
			            Utils.noNulo(getRfcReceptor()),             // rfcReceptor (filtro)
			            Utils.noNuloNormal(getRazonSocialReceptor()),// nombre Receptor
			            Utils.noNulo(getExisteBovedaDescarga()),    // Bóveda
			            Utils.noNulo(getTipoComprobanteDescarga()), // Efecto
			            fechaInicial,                  // Rango (global cabecera)
			            fechaFinal,
			            Utils.noNuloNormal(getUuidDescarga()),      // UUID
			            Utils.noNuloNormal(getEstatusCFDI()),       // Estatus
			            getStart(), pageSize, false,                // paginado, isExcel

			            // ===== extras de texto (si los usas en cabecera) =====
			            Utils.noNuloNormal(getRazonSocialEmisor()), // Nombre Emisor (col 3)
			            Utils.noNulo(getRfcPac()),                  // PAC (para emitidos lo normal es EMISOR_PAC)

			            // ===== operadores (texto) =====
			            Utils.noNulo(getUuidOperator()),
			            Utils.noNulo(getRfcEmiOperator()),
			            Utils.noNulo(getNomEmiOperator()),
			            Utils.noNulo(getRfcRecOperator()),
			            Utils.noNulo(getNomRecOperator()),
			            Utils.noNulo(getPacOperator()),
			            Utils.noNulo(getEfectoOperator()),
			            Utils.noNulo(getEstatusOperator()),
			            Utils.noNulo(getBovedaOperator()),

			            // ===== numérico =====
			            Utils.noNulo(getMontoOperator()),
			            Utils.noNulo(getMontoV1()),
			            Utils.noNulo(getMontoV2()),

			            // ===== fechas =====
			            Utils.noNulo(dateOperator), Utils.noNulo(getEmiDateV1()), Utils.noNulo(getEmiDateV2()),
			            Utils.noNulo(getCerDateOperator()), Utils.noNulo(getCerDateV1()), Utils.noNulo(getCerDateV2()),
			            Utils.noNulo(getCanDateOperator()), Utils.noNulo(getCanDateV1()), Utils.noNulo(getCanDateV2())
			        );
				 //descargaSatBean.generaReporteEmitidos(mySheet, listaDetalle, session.getLenguaje());
				
			 	int REG_NUEVA_PAGINA = 1000000;
		  		if (listaDetalle.size() >= REG_NUEVA_PAGINA) {
		  			SXSSFSheet mySheet2 = myWorkBook.createSheet("Detalle Emitidos (2)");
		  			descargaSatBean.generaReporteEmitidos(myWorkBook, mySheet, listaDetalle, session.getLenguaje(), 0, REG_NUEVA_PAGINA);
		  			descargaSatBean.generaReporteEmitidos(myWorkBook, mySheet2, listaDetalle, session.getLenguaje(), REG_NUEVA_PAGINA, listaDetalle.size());
		  		}else {
		  			descargaSatBean.generaReporteEmitidos(myWorkBook, mySheet, listaDetalle, session.getLenguaje(), 0, listaDetalle.size());
		  		}
			  		
				     ByteArrayOutputStream boas = new ByteArrayOutputStream();
				     myWorkBook.write(boas);
				     setInputStream(new ByteArrayInputStream(boas.toByteArray()));
				     myWorkBook.close();
				     
				
			}
			myWorkBook.close();
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

  
  public String detalleNomina(){
	  HttpServletResponse response = null;
	  HttpServletRequest request = ServletActionContext.getRequest();
	  PrintWriter out = null;

	  Connection con = null;
	  ResultadoConexion rc = null;
	  DescargaSATBean descargaSatBean = new DescargaSATBean();
	  try{

	    response = ServletActionContext.getResponse();

	    response.setContentType("text/html; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");
	    out = response.getWriter();

	    SiarexSession session = ObtenerSession.getSession(request);
	    if ("".equals(session.getEsquemaEmpresa())){
	      return Action.LOGIN;
	    } else {
	      rc = getConnection(session.getEsquemaEmpresa());
	      con = rc.getCon();
	      DescargaSATModel descargaModel = new DescargaSATModel();

	      AccesoBean accesoBean = new AccesoBean();
	      EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());

	      int pageSize = 15;
	      
	      String fechaInicial = Utils.noNulo(getFechaInicialDescarga());
	  		String fechaFinal = Utils.noNulo(getFechaFinalDescarga());
	  		String dateOperator = null;
	  		if ("".equalsIgnoreCase(fechaInicial)) {
	  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
	  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
	  			 
	  		}
	  		
	  		dateOperator = "bt";	      
	      // ===== Detalle con operadores DX-like (NÓMINA/EMITIDOS) =====
	      ArrayList<DescargaSATForm> listaDetalle = descargaSatBean.detalleDescargaNomina(
	          con,
	          rc.getEsquema(),
	          // ===== básicos/valores =====
	          empresasForm.getRfc(),                         // rfcEmisor (fijo para Nómina/Emitidos)
	          Utils.noNulo(getRfcReceptor()),               // rfcReceptor (filtro)
	          Utils.noNuloNormal(getRazonSocialReceptor()), // nombre Receptor
	          Utils.noNulo(getExisteBovedaDescarga()),      // existe Bóveda
	          Utils.noNulo("N"),   // efecto/tipo
	          fechaInicial,                    // fechaIni (global)
	          fechaFinal,                      // fechaFin (global)
	          Utils.noNuloNormal(getUuidDescarga()),        // uuid
	          Utils.noNuloNormal(getEstatusCFDI()),         // estatus
	          getStart(),                                   // inicio
	          pageSize,                                     // length
	          false,                                        // isExcel

	          // ===== extras de texto (si los usas en la cabecera) =====
	          Utils.noNuloNormal(getRazonSocialEmisor()),   // nombre Emisor (si lo filtras)
	          Utils.noNulo(getRfcPac()),                    // PAC Emisor/Receptor según tu SELECT

	          // ===== operadores (texto) =====
	          Utils.noNulo(getUuidOperator()),
	          Utils.noNulo(getRfcEmiOperator()),
	          Utils.noNulo(getNomEmiOperator()),
	          Utils.noNulo(getRfcRecOperator()),
	          Utils.noNulo(getNomRecOperator()),
	          Utils.noNulo(getPacOperator()),
	          Utils.noNulo("equals"),
	          Utils.noNulo(getEstatusOperator()),
	          Utils.noNulo(getBovedaOperator()),

	          // ===== numérico =====
	          Utils.noNulo(getMontoOperator()),
	          Utils.noNulo(getMontoV1()),
	          Utils.noNulo(getMontoV2()),

	          // ===== fechas =====
	          Utils.noNulo(dateOperator), Utils.noNulo(getEmiDateV1()), Utils.noNulo(getEmiDateV2()),
	          Utils.noNulo(getCerDateOperator()), Utils.noNulo(getCerDateV1()), Utils.noNulo(getCerDateV2()),
	          Utils.noNulo(getCanDateOperator()), Utils.noNulo(getCanDateV1()), Utils.noNulo(getCanDateV2())
	      );

	      // ===== Total con mismos filtros (consistencia) =====
	      int totRegistros = descargaSatBean.getTotalRegistrosNomina(
	          con,
	          rc.getEsquema(),
	          empresasForm.getRfc(),
	          Utils.noNulo(getRfcReceptor()),
	          Utils.noNuloNormal(getRazonSocialReceptor()),
	          Utils.noNulo(getExisteBovedaDescarga()),
	          Utils.noNulo("N"),
	          fechaInicial, fechaFinal,
	          Utils.noNuloNormal(getUuidDescarga()), Utils.noNuloNormal(getEstatusCFDI()),

	          // extras texto
	          Utils.noNuloNormal(getRazonSocialEmisor()),
	          Utils.noNulo(getRfcPac()),

	          // operadores
	          Utils.noNulo(getUuidOperator()),
	          Utils.noNulo(getRfcEmiOperator()),
	          Utils.noNulo(getNomEmiOperator()),
	          Utils.noNulo(getRfcRecOperator()),
	          Utils.noNulo(getNomRecOperator()),
	          Utils.noNulo(getPacOperator()),
	          Utils.noNulo("equals"),
	          Utils.noNulo(getEstatusOperator()),
	          Utils.noNulo(getBovedaOperator()),

	          // num
	          Utils.noNulo(getMontoOperator()),
	          Utils.noNulo(getMontoV1()),
	          Utils.noNulo(getMontoV2()),

	          // fechas
	          Utils.noNulo(dateOperator), Utils.noNulo(getEmiDateV1()), Utils.noNulo(getEmiDateV2()),
	          Utils.noNulo(getCerDateOperator()), Utils.noNulo(getCerDateV1()), Utils.noNulo(getCerDateV2()),
	          Utils.noNulo(getCanDateOperator()), Utils.noNulo(getCanDateV1()), Utils.noNulo(getCanDateV2())
	      );

	      descargaModel.setData(listaDetalle);
	      descargaModel.setRecordsTotal(totRegistros);
	      descargaModel.setRecordsFiltered(totRegistros);
	      descargaModel.setDraw(getDraw());

	      JSONObject json = new JSONObject(descargaModel);
	      out.print(json);
	      out.flush();
	      out.close();
	    }
	  } catch(Exception e){
	    Utils.imprimeLog("detalleNomina(): ", e);
	  } finally {
	    try{ if (con != null) con.close(); } catch(Exception ignore){}
	    con = null;
	  }
	  return SUCCESS;
	}

  
  public String consultarFechaMinimaNomina(){
		
		HttpServletResponse response = null;
		HttpServletRequest request = null;
		PrintWriter out = null;

		Connection con = null;
		ResultadoConexion rc = null;
		DescargaSATBean descargaSatBean = new DescargaSATBean();
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
			    
			    BovedaForm bovedaForm = new BovedaForm();
			 // ===== Detalle con operadores DX-like =====
			    String fechaMinima = descargaSatBean.consultarFechaMinimaNomina(con, rc.getEsquema());
			    String fechaFinal = UtilsFechas.getFechayyyyMMdd();
			    bovedaForm.setFechaInicial(fechaMinima.substring(0, 10));
				bovedaForm.setFechaFinal(fechaFinal);
			    
				JSONObject json = new JSONObject(bovedaForm);
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

  

  
  public String generaReporteNomina() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		DescargaSATBean descargaSatBean = new DescargaSATBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		SXSSFWorkbook  myWorkBook = new SXSSFWorkbook(100); // Keep 100 rows in memory
		SXSSFSheet mySheet = myWorkBook.createSheet("Detalle Nomina");
		HttpServletRequest req = ServletActionContext.getRequest();
		String efectoParam = Utils.noNulo(req.getParameter("tipoComprobanteDescarga"));
		String efectoOp    = Utils.noNulo(req.getParameter("efectoOperator"));
		/*
		logger.info("[NOMINA/XLS 1] efecto(param)=" + efectoParam + " | efectoOp=" + efectoOp);

		// Si usas getters/setters del Action:
		logger.info("[NOMINA/XLS 1 ] getTipoComprobanteDescarga()=" + Utils.noNulo(getTipoComprobanteDescarga())); */

		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				myWorkBook.close();
				return Action.LOGIN;
			}else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
			    
				AccesoBean accesoBean = new AccesoBean();
			    EmpresasForm  empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
			    
			    String fechaInicial = Utils.noNulo(getFechaInicialDescarga());
		  		String fechaFinal = Utils.noNulo(getFechaFinalDescarga());
		  		String dateOperator = null;
		  		if ("".equalsIgnoreCase(fechaInicial)) {
		  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
		  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
		  			 
		  		}
		  		
		  		dateOperator = "bt";	    
		  		

			      // ===== Detalle con operadores DX-like (NÓMINA/EMITIDOS) =====
			 // ===== Detalle con operadores DX-like (NÓMINA/EMITIDOS) =====
			    ArrayList<DescargaSATForm> listaDetalle = descargaSatBean.detalleDescargaNomina(
			        con,
			        rc.getEsquema(),
			        // ===== básicos/valores =====
			        empresasForm.getRfc(),                         // rfcEmisor (empresa)
			        Utils.noNulo(getRfcReceptor()),               // rfcReceptor
			        Utils.noNuloNormal(getRazonSocialReceptor()), // nombre Receptor
			        Utils.noNulo(getExisteBovedaDescarga()),      // bóveda
			        Utils.noNulo("N"),   // efecto/tipo (N/I/P o vacío)
			        fechaInicial,                    // fechaIni global
			        fechaFinal,                      // fechaFin global
			        Utils.noNuloNormal(getUuidDescarga()),        // uuid
			        Utils.noNuloNormal(getEstatusCFDI()),         // estatus

			        // Para Excel SIEMPRE sin paginar:
			        0,                                            // start
			        0,                                            // end (no se usa cuando isExcel = true)
			        true,                                         // <<<<<< isExcel = true (clave)

			        // ===== extras de texto =====
			        Utils.noNuloNormal(getRazonSocialEmisor()),   // nombre Emisor (si lo filtras)
			        Utils.noNulo(getRfcPac()),                    // PAC

			        // ===== operadores (texto) =====
			        Utils.noNulo(getUuidOperator()),
			        Utils.noNulo(getRfcEmiOperator()),
			        Utils.noNulo(getNomEmiOperator()),
			        Utils.noNulo(getRfcRecOperator()),
			        Utils.noNulo(getNomRecOperator()),
			        Utils.noNulo(getPacOperator()),
			        Utils.noNulo("equals"),
			        Utils.noNulo(getEstatusOperator()),
			        Utils.noNulo(getBovedaOperator()),

			        // ===== numérico =====
			        Utils.noNulo(getMontoOperator()),
			        Utils.noNulo(getMontoV1()),
			        Utils.noNulo(getMontoV2()),

			        // ===== fechas =====
			        Utils.noNulo(dateOperator), Utils.noNulo(getEmiDateV1()), Utils.noNulo(getEmiDateV2()),
			        Utils.noNulo(getCerDateOperator()), Utils.noNulo(getCerDateV1()), Utils.noNulo(getCerDateV2()),
			        Utils.noNulo(getCanDateOperator()), Utils.noNulo(getCanDateV1()), Utils.noNulo(getCanDateV2())
			    );

					
				int REG_NUEVA_PAGINA = 1000000;
		  		if (listaDetalle.size() >= REG_NUEVA_PAGINA) {
		  			SXSSFSheet mySheet2 = myWorkBook.createSheet("Detalle Nomina (2)");
		  			descargaSatBean.generaReporteNomina(myWorkBook, mySheet, listaDetalle, session.getLenguaje(), 0, REG_NUEVA_PAGINA);
		  			descargaSatBean.generaReporteNomina(myWorkBook, mySheet2, listaDetalle, session.getLenguaje(), REG_NUEVA_PAGINA, listaDetalle.size());
		  		}else {
		  			descargaSatBean.generaReporteNomina(myWorkBook, mySheet, listaDetalle, session.getLenguaje(), 0, listaDetalle.size());
		  		}
		  		
			     ByteArrayOutputStream boas = new ByteArrayOutputStream();
			     myWorkBook.write(boas);
			     setInputStream(new ByteArrayInputStream(boas.toByteArray()));
			     myWorkBook.close();
				     
				
			}
			myWorkBook.close();
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
  
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
		
}
