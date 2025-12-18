package com.siarex247.visor.VisorOrdenes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.JSONObject;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.CentroCostos.CentroCostosBean;
import com.siarex247.catalogos.CentroCostos.CentroCostosForm;
import com.siarex247.catalogos.Empleados.EmpleadosBean;
import com.siarex247.catalogos.Empleados.EmpleadosForm;
import com.siarex247.catalogos.Motivos.MotivosBean;
import com.siarex247.catalogos.Motivos.MotivosForm;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.configuraciones.ConfClaveUsoCFDI.ConfClaveUsoCFDIBean;
import com.siarex247.configuraciones.ConfClaveUsoCFDI.ConfClaveUsoCFDIForm;
import com.siarex247.configuraciones.ConfClaveUsoCFDI.ConfClaveUsoCFDIModel;
import com.siarex247.cumplimientoFiscal.BovedaNomina.BovedaNominaForm;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.CompressionImagen;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.MensajesSIAREX;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsBD;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.validaciones.CartasPorteForm;
import com.siarex247.validaciones.UtilsValidaciones;
import com.siarex247.validaciones.ValidacionesAmericano;
import com.siarex247.validaciones.ValidacionesCartaPorte;
import com.siarex247.validaciones.ValidacionesComplemento;
import com.siarex247.validaciones.ValidacionesComplementoModel;
import com.siarex247.validaciones.ValidacionesFactura;
import com.siarex247.validaciones.ValidacionesMultiple;
import com.siarex247.validaciones.ValidacionesNotaCredito;
import com.siarex247.validaciones.ValidacionesNotaCreditoModel;
import com.siarex247.visor.Americanos.GeneraDatosFacturaBean;
import com.siarex247.visor.Americanos.GeneraDatosFacturaForm;
import com.siarex247.visor.Americanos.GenerarPDFAmericano;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class VisorOrdenesAction extends VisorOrdenesSupport {

	private static final long serialVersionUID = -6534903925530335197L;
	private InputStream inputStream;
	private String reportFile;

	
	
	public String detalleOrdenes() throws Exception {
	    Connection con = null;
	    ResultadoConexion rc = null;
	    VisorOrdenesBean visorBean = new VisorOrdenesBean();
	    HttpServletRequest request  = ServletActionContext.getRequest();
	    HttpServletResponse response = ServletActionContext.getResponse();
	    
	    
	    // helper para selects: ALL -> null (vacío)
	    java.util.function.Function<String,String> normSel = v -> {
	        if (v == null) return null;
	        String t = v.trim();
	        return "ALL".equalsIgnoreCase(t) ? null : t;
	    };

	    try{
	        SiarexSession session = ObtenerSession.getSession(request);
	        if ("".equals(session.getEsquemaEmpresa())){
	            return Action.LOGIN;
	        }

	        response.setContentType("application/json; charset=UTF-8");
	        response.setCharacterEncoding("UTF-8");
	        PrintWriter out = response.getWriter();

	        rc = getConnection(session.getEsquemaEmpresa());
	        con = rc.getCon();

	        // ===== Usuario / permisos =====
	        UsuariosBean uBean = new UsuariosBean();
	        UsuariosForm uForm = uBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
	        int  claveProveedor = 0;
	        boolean isProveedor = false;
	        boolean isSoloConsulta = false;

	        if (uForm.getIdPerfil() == 4) { // Proveedor
	            claveProveedor = Integer.parseInt(uForm.getIdEmpleado().substring(5));
	            isProveedor = true;
	        } else {
	            claveProveedor = getClaveProveedor();
	        }
	        if (uForm.getIdPerfil() == 5) { // Solo consulta
	            isSoloConsulta = true;
	        }

	        String fechaInicial = Utils.noNulo(getUltmovV1());
	  		String fechaFinal = Utils.noNulo(getUltmovV2());
	  		
	  		String dateOperator = null;
	  		if ("".equalsIgnoreCase(fechaInicial)) {
	  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
	  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
	  			 dateOperator = "bt";
	  		}else {
	  			dateOperator = getUltmovOperator();
	  		}
	  		dateOperator = "bt";
	  		
	     // Normaliza selects: "ALL" -> "" (sin filtro)
	        java.util.function.Function<String,String> normSel1 = v -> {
	            String x = Utils.noNulo(v);
	            return "ALL".equalsIgnoreCase(x) ? "" : x;
	        };

	        final int pageSize = 50; // ajusta si usas otro tamaño

	       
	     // ===========================
	   //   DETALLE (DX-like)
	   // ===========================
	   ArrayList<VisorOrdenesForm> data = visorBean.detalleOrdenes(
	       con, rc.getEsquema(),
	       session.getLenguaje(),
	       // flags y paginado
	       isProveedor, isSoloConsulta, claveProveedor,
	       getStart(), pageSize, /*isExcel*/ false,

	       // ====== TEXTO / SELECTS (valor, operador) ======
	       Utils.noNuloNormal(getRazonSocial()), Utils.noNulo(getRsOperator()),

	       // 3 Orden de Compra (numérico: operador, v1, v2)
	       Utils.noNulo(getOcOperator()), Utils.noNulo(getOcV1()), Utils.noNulo(getOcV2()),

	       // resto de TEXTO/SELECTS
	       Utils.noNuloNormal(getDescripcion()), Utils.noNulo(getDescOperator()),
	       normSel1.apply(getTipoMoneda()),       Utils.noNulo(getMonedaOperator()),
	       normSel1.apply(getServicioRecibo()),   Utils.noNulo(getReciboOperator()),
	       normSel1.apply(getEstatusPago()),      Utils.noNulo(getEstatusPagoOperator()),
	       Utils.noNuloNormal(getSerieFolio()),   Utils.noNulo(getSerieFolioOperator()),
	       Utils.noNuloNormal(getAsignarA()),     Utils.noNulo(getAsignarOperator()),
	       Utils.noNuloNormal(getEstadoCfdi()),   Utils.noNulo(getEstadoCfdiOperator()),
	       normSel1.apply(getEstatusSat()),       Utils.noNulo(getEstatusSatOperator()),
	       normSel1.apply(getUsoCfdi()),          Utils.noNulo(getUsoCfdiOperator()),
	       Utils.noNuloNormal(getCps()),          Utils.noNulo(getCpsOperator()),

	       // ====== NUMÉRICOS (operador, v1, v2) ======
	       Utils.noNulo(getMontoOperator()),     Utils.noNulo(getMontoV1()),     Utils.noNulo(getMontoV2()),
	       Utils.noNulo(getTotalOperator()),     Utils.noNulo(getTotalV1()),     Utils.noNulo(getTotalV2()),
	       Utils.noNulo(getSubtotalOperator()),  Utils.noNulo(getSubtotalV1()),  Utils.noNulo(getSubtotalV2()),
	       Utils.noNulo(getIvaOperator()),       Utils.noNulo(getIvaV1()),       Utils.noNulo(getIvaV2()),
	       Utils.noNulo(getIvaretOperator()),    Utils.noNulo(getIvaretV1()),    Utils.noNulo(getIvaretV2()),
	       Utils.noNulo(getIsrretOperator()),    Utils.noNulo(getIsrretV1()),    Utils.noNulo(getIsrretV2()),
	       Utils.noNulo(getImplocOperator()),    Utils.noNulo(getImplocV1()),    Utils.noNulo(getImplocV2()),
	       Utils.noNulo(getTotalncOperator()),   Utils.noNulo(getTotalncV1()),   Utils.noNulo(getTotalncV2()),
	       Utils.noNulo(getPagotOperator()),     Utils.noNulo(getPagotV1()),     Utils.noNulo(getPagotV2()),
	       Utils.noNulo(getIvaretncOperator()),  Utils.noNulo(getIvaretncV1()),  Utils.noNulo(getIvaretncV2()),

	       // ====== FECHAS (operador, v1, v2) ======
	       Utils.noNulo(getFechapagoOperator()), Utils.noNulo(getFechapagoV1()), Utils.noNulo(getFechapagoV2()),
	       dateOperator,    fechaInicial,    fechaFinal
	   );

	        // ===========================
	        //   TOTAL (DX-like)
	        // ===========================
	// ===========================
	//   TOTAL (DX-like)
	// ===========================
	int total = visorBean.totalRegistros(
	    con, rc.getEsquema(),
	    isProveedor, isSoloConsulta, claveProveedor,

	    // TEXTO / SELECTS (valor, operador)
	    Utils.noNuloNormal(getRazonSocial()), Utils.noNulo(getRsOperator()),

	    // 3 Orden de Compra (numérico: operador, v1, v2)
	    Utils.noNulo(getOcOperator()), Utils.noNulo(getOcV1()), Utils.noNulo(getOcV2()),

	    // resto de TEXTO/SELECTS
	    Utils.noNuloNormal(getDescripcion()), Utils.noNulo(getDescOperator()),
	    normSel1.apply(getTipoMoneda()),       Utils.noNulo(getMonedaOperator()),
	    normSel1.apply(getServicioRecibo()),   Utils.noNulo(getReciboOperator()),
	    normSel1.apply(getEstatusPago()),      Utils.noNulo(getEstatusPagoOperator()),
	    Utils.noNuloNormal(getSerieFolio()),   Utils.noNulo(getSerieFolioOperator()),
	    Utils.noNuloNormal(getAsignarA()),     Utils.noNulo(getAsignarOperator()),
	    Utils.noNuloNormal(getEstadoCfdi()),   Utils.noNulo(getEstadoCfdiOperator()),
	    normSel1.apply(getEstatusSat()),       Utils.noNulo(getEstatusSatOperator()),
	    normSel1.apply(getUsoCfdi()),          Utils.noNulo(getUsoCfdiOperator()),
	    Utils.noNuloNormal(getCps()),          Utils.noNulo(getCpsOperator()),

	    // NUMÉRICOS (operador, v1, v2)
	    Utils.noNulo(getMontoOperator()),     Utils.noNulo(getMontoV1()),     Utils.noNulo(getMontoV2()),
	    Utils.noNulo(getTotalOperator()),     Utils.noNulo(getTotalV1()),     Utils.noNulo(getTotalV2()),
	    Utils.noNulo(getSubtotalOperator()),  Utils.noNulo(getSubtotalV1()),  Utils.noNulo(getSubtotalV2()),
	    Utils.noNulo(getIvaOperator()),       Utils.noNulo(getIvaV1()),       Utils.noNulo(getIvaV2()),
	    Utils.noNulo(getIvaretOperator()),    Utils.noNulo(getIvaretV1()),    Utils.noNulo(getIvaretV2()),
	    Utils.noNulo(getIsrretOperator()),    Utils.noNulo(getIsrretV1()),    Utils.noNulo(getIsrretV2()),
	    Utils.noNulo(getImplocOperator()),    Utils.noNulo(getImplocV1()),    Utils.noNulo(getImplocV2()),
	    Utils.noNulo(getTotalncOperator()),   Utils.noNulo(getTotalncV1()),   Utils.noNulo(getTotalncV2()),
	    Utils.noNulo(getPagotOperator()),     Utils.noNulo(getPagotV1()),     Utils.noNulo(getPagotV2()),
	    Utils.noNulo(getIvaretncOperator()),  Utils.noNulo(getIvaretncV1()),  Utils.noNulo(getIvaretncV2()),

	    // FECHAS (operador, v1, v2)
	    Utils.noNulo(getFechapagoOperator()), Utils.noNulo(getFechapagoV1()), Utils.noNulo(getFechapagoV2()),
	       dateOperator,    fechaInicial,    fechaFinal
	);


	     
	        VisorOrdenesModel model = new VisorOrdenesModel();
	        model.setData(data);
	        model.setRecordsTotal(total);
	        model.setRecordsFiltered(total);
	        model.setDraw(getDraw());

	        out.print(new org.json.JSONObject(model));
	        out.flush();
	        out.close();

	    }catch(Exception e){
	        Utils.imprimeLog("", e);
	    }finally{
	        try{ if (con != null) con.close(); }catch(Exception ignore){}
	        con = null;
	    }
	    return SUCCESS;
	}

	
	
	public String consultarOrden() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		VisorOrdenesBean visorBean = new VisorOrdenesBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				//logger.info("**** CONSULTANDO ORDENES ***************");
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				VisorOrdenesForm visorForm  = visorBean.consultarOrden(con, rc.getEsquema(), getFolioOrden());
				visorForm.setProveedor(false);
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				if ("003".equalsIgnoreCase(usuariosForm.getNombreCortoPerfil())) {
					visorForm.setProveedor(true);
				}
				if ("004".equalsIgnoreCase(usuariosForm.getNombreCortoPerfil())) {
					visorForm.setSoloConsulta(true);
				}
				
				JSONObject json = new JSONObject(visorForm);
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
	
	
	public String nuevaOrden() {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	VisorOrdenesBean visorBean = new VisorOrdenesBean();
    	VisorOrdenesModel visorModel = new VisorOrdenesModel();
    	
    	LenguajeBean lenguajeBean = LenguajeBean.instance();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

		    if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "VISOR");
				
				
				
				if (getClaveProveedor() == 0) {
					visorModel.setCodError("001");
					//visorModel.setMensajeError("Error el guardar la información del registro, debe especificar un proveedor.");
					visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG27")));
				}else {
					rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    String nombreEmpleado = null;
				    String idEmpleado = null;
				    String emailEmpleado = null;
				    String asignarTO = null;
				    String estatus = null;
				    
				    
				    UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					
					
					int resultado = 0;
				    if (usuariosForm.getIdPerfil() == 4) {
				    	resultado = 4;
				    }else {
				    	
				    	 if (!"".equalsIgnoreCase(Utils.noNulo(getIdEmpleado()))) {
						    	EmpleadosBean empleadosBean = new EmpleadosBean();
						    	EmpleadosForm empleadosForm = empleadosBean.consultaEmpleadoXid(con, rc.getEsquema(), getIdEmpleado());
						    	nombreEmpleado = empleadosForm.getNombreCompleto();
								emailEmpleado  = empleadosForm.getCorreo();
								idEmpleado = empleadosForm.getIdEmpleado();
								asignarTO = "E_" + empleadosForm.getIdEmpleado();
								  
						    }
						    
						    if (!"".equalsIgnoreCase(Utils.noNulo(getIdCentro()))) {
						    	asignarTO = "C_" + getIdCentro();
						    }
						    
						    String servRecibido = null;
						    if ("on".equalsIgnoreCase(getSerRecibido())) {
						    	estatus = "A2";
						    	servRecibido = "1";
						    }else {
						    	estatus = "A5";
						    	servRecibido = "0";
						    }
						    
				    	
				    	resultado = visorBean.nuevaOrden(con, rc.getEsquema(), getFolioEmpresa(), getDescripcion(), getTipoMoneda(), Utils.noNuloDouble(getMonto()), getClaveProveedor(), servRecibido, asignarTO, estatus, getCentroCostosProveedor(), getNumeroCuenta(), getUsuario(request), null);
					    // logger.info("resultado====>"+resultado);
					    
					    
					    ConfigAdicionalesBean confAicionales = new ConfigAdicionalesBean();
						HashMap<String, String> mapaConf = confAicionales.obtenerConfiguraciones(con, rc.getEsquema());
						String bandEnviaCorreo = Utils.noNulo(mapaConf.get("NOTIF_CORREO_ORDEN"));
						bandEnviaCorreo = "S";
						if (asignarTO != null && resultado == 1 && "S".equalsIgnoreCase(bandEnviaCorreo) ) {
						       ProveedoresBean provBean = new ProveedoresBean();
						       LenguajeBean lenBean = LenguajeBean.instance();
							  // Estimado Usuario << usuario >> por este medio le informamos que fue asignada la orden de compra << ordenCompra >> a cargo del proveedor << razonSocial >> 
						      // por un monto de $<< monto >>, favor de subir su factura .pdf y .xml a la brevedad para que pueda ser procesado su pago.
						      HashMap <String, String> mapaLen = lenBean.obtenerEtiquetas(session.getLenguaje(), "VISOR");
						      String mensajeTabla = null;
						      String listaCorreos [] = null;
						      StringBuffer sbMail = new StringBuffer();
						      if(asignarTO.startsWith("C_")) {
						    	  mensajeTabla = Utils.regresaCaracteresNormales(mapaLen.get("MSG12"));
						    	  CentroCostosBean centrosBean = new CentroCostosBean();
						    	  CentroCostosForm centrosForm =  centrosBean.consultaCentrosXid(con, session.getEsquemaEmpresa(), getIdCentro());
						    	  sbMail.append(centrosForm.getCorreoCentro()).append(";");
								  listaCorreos = sbMail.toString().split(";");
						      }else {
						    	  sbMail.append(emailEmpleado).append(";");
						    	  mensajeTabla = Utils.regresaCaracteresNormales(mapaLen.get("MSG11"));
						    	  listaCorreos = sbMail.toString().split(";");
						      }
							  String subjectCorreo = Utils.regresaCaracteresNormales(mapaLen.get("SUB1"));
							  
							  ProveedoresForm proveedoresForm = provBean.consultarProveedor(con, rc.getEsquema(), getClaveProveedor());
						      String mensajeCorreo = UtilsHTML.generaHTMLNuevaOrden(proveedoresForm.getRazonSocial(), idEmpleado, nombreEmpleado, getFolioEmpresa(), Utils.noNuloDouble(getMonto()), mensajeTabla);
						      
						      AccesoBean accesoBean = new AccesoBean();
						      EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
						      EnviaCorreoGrid.enviarCorreo(null, mensajeCorreo, false, listaCorreos, null, subjectCorreo, empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
						      // CorreoBean.enviarCorreo(null, mensajeCorreo, false, listaCorreos, null,  subjectCorreo, session.getEmailEmpresa(),session.getPwdEmpresa());
						    }	
				    }
					if (resultado == 1) {
						visorModel.setCodError("000");
						visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
						
					}else if (resultado == 1062 || resultado == -1062) {
						visorModel.setCodError("001");
						// visorModel.setMensajeError("Error el guardar la información del registro, el número de orden especificado ya existe en la base de datos de SIAREX.");
						visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG28")));
					}else if (resultado == 4 ) {
						visorModel.setCodError("001");
						// visorModel.setMensajeError("Error el guardar la información del registro, Usuario no permitido para generar ordenes.");
						visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG29")));
					}else {
						visorModel.setCodError("001");
						// visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
						visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG30")));
					}
				    
				}
				JSONObject json = new JSONObject(visorModel);
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
	
	
	public String modificaOrden(){
	Connection con = null;
	ResultadoConexion rc = null;
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpServletRequest request = ServletActionContext.getRequest();
	VisorOrdenesBean visorBean = new VisorOrdenesBean();
	VisorOrdenesModel visorModel = new VisorOrdenesModel();
	LenguajeBean lenguajeBean = LenguajeBean.instance();
	try{
		PrintWriter out = response.getWriter();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		SiarexSession session = ObtenerSession.getSession(request);
		if ("".equals(session.getEsquemaEmpresa())){
			return Action.LOGIN;
		}else{
		    rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();
		    
		   // logger.info("***** PAGANDO FACTURAS ******");
		    HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "VISOR");
		    
		    UsuariosBean usuarioBean = new UsuariosBean();
			UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
			
			if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
				visorModel.setCodError("001");
				//visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
				visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG29")));
			}else {
				String asignarTO = "";
			    
			    if (!"".equalsIgnoreCase(Utils.noNulo(getIdEmpleado()))) {
			    	EmpleadosBean empleadosBean = new EmpleadosBean();
			    	EmpleadosForm empleadosForm = empleadosBean.consultaEmpleadoXid(con, rc.getEsquema(), getIdEmpleado());
					asignarTO = "E_" + empleadosForm.getIdEmpleado();
			    }
			    
			    if (!"".equalsIgnoreCase(Utils.noNulo(getIdCentro()))) {
			    	asignarTO = "C_" + getIdCentro();
			    }
			    
			    VisorOrdenesForm visorForm =  visorBean.consultarOrden(con, rc.getEsquema(), getFolioOrden());
			    ProveedoresBean provBean = new ProveedoresBean();
		    	ProveedoresForm provForm = provBean.consultarProveedor(con, rc.getEsquema(), visorForm.getClaveProveedor());
		    	
			    AccesoBean accesoBean = new AccesoBean();
		    	EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
		    	
			    String estatusPago = visorForm.getEstatusOrden();
			    
			    
			    String servRecibido = null;
			    if ("A4".equalsIgnoreCase(visorForm.getEstatusOrden())) {
			    	servRecibido = visorForm.getServicioRecibido();	
			    }else {
			    	if ("on".equalsIgnoreCase(getSerRecibido())) {
				    	servRecibido = "1";
				    }else {
				    	servRecibido = "0";
				    }	
			    }
			    
			    String eliminarFactura = getEliminarFactura();
			    if ("on".equalsIgnoreCase(getEliminarFactura())) {
			    	eliminarFactura = "1";
			    }else {
			    	eliminarFactura = "0";
			    }
				
			    String fechaPago = null;
			    if ("A1".equalsIgnoreCase(estatusPago) && "1".equalsIgnoreCase(servRecibido) ){
			    	estatusPago = "A3";
			    	//ConfigAdicionalesBean confAicionales = new ConfigAdicionalesBean();
	        		HashMap<String, String> mapaConf = ConfigAdicionalesBean.obtenerConfiguraciones(con, rc.getEsquema());
					fechaPago = UtilsValidaciones.obtenerFechaPago(mapaConf);  

			    }else if ("A5".equalsIgnoreCase(estatusPago) && "1".equalsIgnoreCase(servRecibido) ){
			    	estatusPago = "A2";
			    }else if ("A3".equalsIgnoreCase(estatusPago) && "0".equalsIgnoreCase(servRecibido) ){
			    	estatusPago = "A1";
			    }else if ("A2".equalsIgnoreCase(estatusPago) && "0".equalsIgnoreCase(servRecibido) ){
			    	estatusPago = "A5";
			    }
			    
			    
			    // se evalua el estatus final
			    boolean bandElimina = false;
			    boolean pagoFactura = false;
			    if ("A3".equalsIgnoreCase(estatusPago) && "1".equalsIgnoreCase(eliminarFactura) && "1".equalsIgnoreCase(servRecibido)){
			    	estatusPago = "A2";
			    	bandElimina = true;
			    }else if ("A1".equalsIgnoreCase(estatusPago) && "1".equalsIgnoreCase(eliminarFactura) && "1".equalsIgnoreCase(servRecibido)){
			    	estatusPago = "A2";
			    	bandElimina = true;
			    }else if ("A1".equalsIgnoreCase(estatusPago) && "1".equalsIgnoreCase(eliminarFactura) ){
			    	estatusPago = "A5";
			    	bandElimina = true;
			    }else if ("A3".equalsIgnoreCase(estatusPago) && "1".equalsIgnoreCase(eliminarFactura) ){
			    	estatusPago = "A5";
			    	bandElimina = true;
			    }else if ("A3".equalsIgnoreCase(estatusPago) && "on".equalsIgnoreCase(getFacturaPagada())) {
			    	fechaPago = getFechaPago() + " 01:01:01.1";
			    	pagoFactura = true;
			    }
			    // finaliza
			    
			    String tipoOrden = visorForm.getTipoOrden();
			    
			    String centroCostosProveedor = Utils.noNulo(getCentroCostosProveedor());
			    String numeroCuenta = Utils.noNulo(getNumeroCuenta());
			    
			    int claveProveedor = getClaveProveedor();
			    String tipoMoneda  = getTipoMoneda();
			    if ("A3".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A4".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A1".equalsIgnoreCase(visorForm.getEstatusOrden())) {
			    	claveProveedor = visorForm.getClaveProveedor();
			    	tipoMoneda  = visorForm.getTipoMoneda();
			    }
			    
			    if (pagoFactura && "".equalsIgnoreCase(Utils.noNulo(getFechaPago()))) {
			    	visorModel.setCodError("001");
					// visorModel.setMensajeError("Error el guardar la información del registro, debe especificar una fecha de pago.");
			    	visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG31")));
			    }else {
			    	int resultado = visorBean.actualizarOrden(con, session.getEsquemaEmpresa(), getFolioOrden(), getDescripcion(), tipoMoneda, Utils.noNuloDouble(getMonto()), claveProveedor, servRecibido, getUsuario(request), asignarTO, estatusPago, eliminarFactura, fechaPago, tipoOrden, centroCostosProveedor, numeroCuenta);
			    	if ("on".equalsIgnoreCase(getFacturaPagada()) && !bandElimina && resultado >= 1){
					    int cant = visorBean.actualizarOrdenPagada(con, session.getEsquemaEmpresa(), getFolioOrden(), getUsuario(request), tipoOrden, fechaPago);
					    if (cant > 0 && "on".equalsIgnoreCase(getEnvioCorreo())){
					    	
					    	String subJect = "SIAREX - Factura Pagada por "+empresasForm.getNombreLargo();
					    	
					    	String coma = ";";
					    	StringBuffer sbDatos = new StringBuffer(); 
					    	
					    	
					    	if ("".equalsIgnoreCase(tipoOrden)) {
					    		sbDatos.append(visorForm.getSerieFolio()).append(coma)
							       .append(visorForm.getUuid()).append(coma)
							       .append(visorForm.getTotal()).append(coma)
							       .append(visorForm.getTipoMoneda()).append(coma)
							       .append(visorForm.getFolioEmpresa()).append(coma)
							       .append("N").append(coma);
					    		
					    	}else {
					    		ArrayList<VisorOrdenesForm> listaMultiples = visorBean.listadoOrdenMultiple(con, rc.getEsquema(), tipoOrden);
					    		for (int x = 0; x < listaMultiples.size(); x++) {
					    			sbDatos.append(visorForm.getSerieFolio()).append(coma)
								       .append(visorForm.getUuid()).append(coma)
								       .append(visorForm.getTotal()).append(coma)
								       .append(visorForm.getTipoMoneda()).append(coma)
								       .append(visorForm.getFolioEmpresa()).append(coma)
								       .append("N").append(coma);
					    			sbDatos.append("&");
					    		}
					    	}
					    	
					    	String sbHTML = UtilsHTML.generaHTML(sbDatos.toString(), empresasForm.getNombreLargo(), provForm.getRazonSocial(), Utils.getFechayyyyMMdd());
					    	//logger.info("sbHTML===>"+sbHTML);
					    	String [] listaCorreos = UtilsBD.obtenerCorreorProveedor(con, session.getEsquemaEmpresa(), provForm.getClaveRegistro(), "S", "N", "N", "N");
					    	EnviaCorreoGrid.enviarCorreo(null, sbHTML, false, listaCorreos, null, subJect, empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
					    }
				    }
				    
				    if (bandElimina){
				    	// se elimina la nota de credito
				    	// NotasCreditoBean notaCreditoBean = new NotasCreditoBean();  quitar Lupillo
				    	// JSONObject jsonNOTA = notaCreditoBean.buscarNotaCredito(con, session.getEsquemaEmpresa(), ordenBuscar);  quitar Lupillo
						
				    	ConfClaveUsoCFDIBean confUsoBean = new ConfClaveUsoCFDIBean();
				    	confUsoBean.eliminaClavesProductoMultiple(con, session.getEsquemaEmpresa(), tipoOrden);

				    	
				    	// Se elimina la carta porte
				    	// CartasPorteBean cartasPorteBean = new CartasPorteBean();  quitar Lupillo
				    	// JSONObject jsonCARTA_PORTE = cartasPorteBean.buscarDocumento(con, session.getEsquemaEmpresa(), ordenBuscar);  quitar Lupillo
				    	
				    	String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
						String directorio = "";
						directorio =  session.getEsquemaEmpresa()+"/PROVEEDORES/" + visorForm.getClaveProveedor() + "/";
						String rutaArchivo = rutaFinal + directorio;
						logger.info("Eliminando los archivos de la factura..."+rutaArchivo);
						
						String rutaXMLSource = rutaArchivo + visorForm.getNombreXML();
						String rutaPDFSource = rutaArchivo + visorForm.getNombrePDF();
						
				    	// UtilsFile.eliminaArchivo(rutaXML);
				    	// UtilsFile.eliminaArchivo(rutaPDF);
						
						String rutaEliminar = UtilsPATH.RUTA_PUBLIC_HTML + "DOCUMENTOS_ELIMINADOS" + File.separator + session.getEsquemaEmpresa() + File.separator + "PROVEEDORES" + File.separator + visorForm.getClaveProveedor() + File.separator;
						String rutaXMLDest = rutaEliminar + visorForm.getNombreXML();
						String rutaPDFDest = rutaEliminar + visorForm.getNombrePDF();
						
						try {
							
							File fileXMLSource = new File(rutaXMLSource);
							File filePDFSource = new File(rutaPDFSource);
							
							File fileXMLDest = new File(rutaXMLDest);
							File filePDFDest = new File(rutaPDFDest);
							
							UtilsFile.moveFileDirectory(fileXMLSource, fileXMLDest, true, false, true, true);
							UtilsFile.moveFileDirectory(filePDFSource, filePDFDest, true, false, true, true);
							
						}catch(Exception e) {
							Utils.imprimeLog("", e);
						}
						
				    	
				    	ValidacionesNotaCredito visorNota = new ValidacionesNotaCredito();
						NotaCreditoForm notaForm = visorNota.buscarNotaCredito(con, rc.getEsquema(), visorForm.getFolioOrden());
						if (!"".equalsIgnoreCase(notaForm.getNombreXML())) {
							String rutaXML_Nota = rutaArchivo + notaForm.getNombreXML();
							String rutaPDF_Nota = rutaArchivo + notaForm.getNombrePDF();
							logger.info("Eliminando los archivos de la nota de credito..."+rutaXML_Nota);
							UtilsFile.eliminaArchivo(rutaXML_Nota);
					    	UtilsFile.eliminaArchivo(rutaPDF_Nota);
					    	visorNota.eliminarNotaCredito(con, rc.getEsquema(), notaForm.getUuidNotaCredito(), "OK");
						}
						
						
				    	
				    	ValidacionesCartaPorte valCartaBean = new ValidacionesCartaPorte();
				    	CartasPorteForm cartaForm =  valCartaBean.consultaCartas(con, rc.getEsquema(), visorForm.getFolioEmpresa());
				    	if (!"".equalsIgnoreCase(cartaForm.getNombreXML())) {
				    		String rutaXML_Carta = rutaArchivo + cartaForm.getNombreXML();
							String rutaPDF_Carta = rutaArchivo + cartaForm.getNombrePDF();
							logger.info("Eliminando los archivos de la carta porte..."+rutaXML_Carta);
							UtilsFile.eliminaArchivo(rutaXML_Carta);
					    	UtilsFile.eliminaArchivo(rutaPDF_Carta);
				    	}
				    	valCartaBean.eliminarCartaPorteFolioEmpresa(con, rc.getEsquema(), visorForm.getFolioEmpresa());
				    	
				    	
				    	String subJect = "SIAREX - Factura Incorrecta";
				    	String [] listaCorreos = UtilsBD.obtenerCorreorProveedor(con, session.getEsquemaEmpresa(), provForm.getClaveRegistro(), "N", "S", "N","N");
				    	// String mensajeCorreo = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+getFolioEmpresa()+", no pudo ser procesada con exito debido a que la informacion proporcionada en el archivo es incorrecta, favor de proporcionar nuevamente los datos de la orden de compra";
				    	String mensajeCorreo = UtilsHTML.generaHTMLFacturaIncorrecta(visorForm.getFolioEmpresa(), provForm.getRazonSocial());
				    	
				    	logger.info("mensajeCorreo--->"+mensajeCorreo);
				    	EnviaCorreoGrid.enviarCorreo(null, mensajeCorreo, false, listaCorreos, null, subJect, empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
				    }
				    
				    if (resultado >= 1) {
						visorModel.setCodError("000");
						visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");	
					}else if (resultado == 1062 || resultado == -1062) {
						visorModel.setCodError("001");
						// visorModel.setMensajeError("Error el guardar la información del registro, el número de orden especificado ya existe en la base de datos de SIAREX.");
						visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG28")));
					}else {
						visorModel.setCodError("001");
						// visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
						visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG30")));
					}
			    }
				
			}
		    
		    
		    JSONObject json = new JSONObject(visorModel);
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
	
	
	
	
	public String modificaOrdenOmitirComple()
    {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	VisorOrdenesBean visorBean = new VisorOrdenesBean();
    	VisorOrdenesModel visorModel = new VisorOrdenesModel();
		try{
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    
			    UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				
				if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
					
				}else {
				    String OMITIR_COMPLEMENTO = Utils.noNulo(request.getParameter("omitirComplemento"));
					
				    if ("on".equalsIgnoreCase(OMITIR_COMPLEMENTO)) {
				    	OMITIR_COMPLEMENTO = "S";
				    }else {
				    	OMITIR_COMPLEMENTO = "N";
				    }
				    
				    VisorOrdenesForm visorForm =  visorBean.consultarOrden(con, rc.getEsquema(), getFolioOrden());
				    String tipoOrden = visorForm.getTipoOrden();
				    
				    int resultado = visorBean.actualizarOrdenOmitirComplemento(con, session.getEsquemaEmpresa(), getFolioOrden(), OMITIR_COMPLEMENTO, tipoOrden, getUsuario(request));
				    if (resultado >= 1) {
						visorModel.setCodError("000");
						visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");	
					}else if (resultado == 1062 || resultado == -1062) {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, el número de orden especificado ya existe en la base de datos de SIAREX.");
					}else {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}
				}
			    
			    JSONObject json = new JSONObject(visorModel);
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
	
	
	
	
	public String modificarFechaPago() {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	VisorOrdenesBean visorBean = new VisorOrdenesBean();
    	VisorOrdenesModel visorModel = new VisorOrdenesModel();
    	
		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
					String fechaRecibido = getFechaPago();
				
				
				logger.info("fechaRecibido------------->"+fechaRecibido);
				logger.info("folioOrden------------->"+getFolioOrden());
			
					rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    
				    UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					
					if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
					}else {
						String HoraPago = null;
						Date HoraActual = new Date();
						SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm:ss");
						HoraPago = formatDate.format(HoraActual);
						
						StringBuffer fechaPagoFinal = UtilsValidaciones.validaFechaPado(fechaRecibido);
						fechaPagoFinal.append(HoraPago);
						VisorOrdenesForm visorForm =  visorBean.consultarOrden(con, rc.getEsquema(), getFolioOrden());
					    
					    String tipoOrden = visorForm.getTipoOrden();
					    
						int cant = visorBean.actualizarFechaPagoRecibida(con, session.getEsquemaEmpresa(), getFolioOrden(), getUsuario(request), fechaPagoFinal.toString(), tipoOrden);
						if (cant >= 1) {
							visorModel.setCodError("000");
							visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");	
						}else {
							visorModel.setCodError("001");
							visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
						}
						
					}

				    JSONObject json = new JSONObject(visorModel);
					out.print(json);
		            out.flush();
		            out.close();
					
			}
		}
		catch(Exception e){
			Utils.imprimeLog("modificarFechaPago()_ ", e);
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
	
	
	
	public String eliminaOrdenes(){
	HttpServletResponse response = null;
	HttpServletRequest request = null;
	PrintWriter out = null;
	Connection con = null;
	ResultadoConexion rc = null;
	VisorOrdenesModel visorModel = new VisorOrdenesModel();
	VisorOrdenesBean visorBean = new VisorOrdenesBean();
	LenguajeBean lenguajeBean = LenguajeBean.instance();
	try{
		response = ServletActionContext.getResponse();
    	request = ServletActionContext.getRequest();
		out = response.getWriter();
	    SiarexSession session = ObtenerSession.getSession(request);
	    response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		if ("".equals(session.getEsquemaEmpresa())){
			return Action.LOGIN;
		}
		else{
			rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();
		    
			HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "VISOR");

		    UsuariosBean usuarioBean = new UsuariosBean();
			UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
			
			if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
				visorModel.setCodError("001");
				// visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
				visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG29")));
			}else {
				int resEliminar = visorBean.eliminaOrden(con, rc.getEsquema(), getFoliosEliminar());
			    if (resEliminar == 1) {
					visorModel.setCodError("000");
					visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");	
				}else if (resEliminar == -1 ) {
					visorModel.setCodError("001");
					//visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar ordenes de compra con estatus de A5 y A2.");
					visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG32")));
				}else {
					visorModel.setCodError("001");
					// visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG30")));
				}
			}
		    
		    
		    
		    JSONObject json = new JSONObject(visorModel);
			out.print(json);
            out.flush();
            out.close();
		}
	}
	catch(Exception e){
		Utils.imprimeLog("eliminaOrden(): ", e);
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
	
	
	public String eliminaCartaPorte(){
		HttpServletResponse response = null;
		HttpServletRequest request = null;
		PrintWriter out = null;
		Connection con = null;
		ResultadoConexion rc = null;
		VisorOrdenesModel visorModel = new VisorOrdenesModel();
		VisorOrdenesBean visorBean = new VisorOrdenesBean();
		LenguajeBean lenguajeBean = LenguajeBean.instance();
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
		    response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    
			    HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "VISOR");
			    
			    UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				
				if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
					visorModel.setCodError("001");
					// visorModel.setMensajeError("Error el guardar la información del registro, Operacion no Permitida.");
					visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG29")));

				}else {
					VisorOrdenesForm visorForm = visorBean.consultarOrden(con, rc.getEsquema(), getFolioOrden());
				    if ("A3".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A1".equalsIgnoreCase(visorForm.getEstatusOrden())) {
				    	ValidacionesCartaPorte valCartaBean = new ValidacionesCartaPorte();
				    	CartasPorteForm cartaForm =  valCartaBean.consultaCartas(con, rc.getEsquema(), visorForm.getFolioEmpresa());
				    	if (!"".equalsIgnoreCase(cartaForm.getUuidCarta())) {
				    		int totEliminar = valCartaBean.eliminarCartaPorteFolioEmpresa(con, rc.getEsquema(), visorForm.getFolioEmpresa());
				    		if (totEliminar == 1) {
				    			visorModel.setCodError("000");
								visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
								
								if (!"".equalsIgnoreCase(cartaForm.getNombreXML())) {
									String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
									String directorio = "";
									directorio =  session.getEsquemaEmpresa()+"/PROVEEDORES/" + visorForm.getClaveProveedor() + "/";
									String rutaArchivo = rutaFinal + directorio;
									
									String rutaXML_Carta = rutaArchivo + cartaForm.getNombreXML();
									String rutaPDF_Carta = rutaArchivo + cartaForm.getNombrePDF();
									logger.info("Eliminando los archivos de la carta porte..."+rutaXML_Carta);
									UtilsFile.eliminaArchivo(rutaXML_Carta);
							    	UtilsFile.eliminaArchivo(rutaPDF_Carta);
								}
								
				    		}else {
				    			visorModel.setCodError("001");
								// visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
				    			visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG30")));
				    		}
				    	}else {
				    		String mensaje33 = Utils.noNuloNormal(mapaLenguaje.get("MSG33")).replaceAll("<< FOLIO_EMPRESA >>",  String.valueOf(visorForm.getFolioEmpresa()));
				    		visorModel.setCodError("001");
							// visorModel.setMensajeError("Error el guardar la información del registro, la orden de compra "+visorForm.getFolioEmpresa()+", no tiene asignado una carta porte.");
				    		visorModel.setMensajeError(mensaje33);
				    	}
				    	
				    }else {
				    	visorModel.setCodError("001");
						//visorModel.setMensajeError("Error el guardar la información del registro, estatus de la orden incorrecta para eliminar carta porte.");
				    	visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG34")));
				    }
				}
			    
			    
			    JSONObject json = new JSONObject(visorModel);
				out.print(json);
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("eliminaOrden(): ", e);
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
	
	
	public String cargarFactura() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		VisorOrdenesModel visorModel = new VisorOrdenesModel();
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
				
				Part filePart = request.getPart("filePDF");
            	File filePDF = UtilsFile.getFileFromPart(filePart);
            	
            	filePart = request.getPart("fileXML");
            	File fileXML = UtilsFile.getFileFromPart(filePart);
				if (filePDF == null) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .PDF para continuar.");
				}else if (fileXML == null) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .XML para continuar.");
				}else if (getFolioEmpresa() == 0) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, debe especificar un número de orden de compra.");
				}else {
					ValidacionesFactura valFacturaBean = new ValidacionesFactura();
					UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					String arrValidaciones [] = valFacturaBean.iniciarProceso(session.getEsquemaEmpresa(), getFolioEmpresa(), fileXML, filePDF, session.getLenguaje(), usuariosForm.getIdPerfil(), getUsuario(request));
					
					if ("ERROR".equalsIgnoreCase(arrValidaciones[2])) {
						visorModel.setCodError("001");
					}else {
						visorModel.setCodError("000");	
					}
					visorModel.setMensajeError(arrValidaciones[0]);
				}
				
				JSONObject json = new JSONObject(visorModel);
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
	
	
	
	public String cargarCartaPorte() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		VisorOrdenesModel visorModel = new VisorOrdenesModel();
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
				
				Part filePart = request.getPart("filePDF");
            	File filePDF = UtilsFile.getFileFromPart(filePart);
            	
            	filePart = request.getPart("fileXML");
            	File fileXML = UtilsFile.getFileFromPart(filePart);
            	
				
				if (filePDF == null) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .PDF para continuar.");
				}else if (fileXML == null) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .XML para continuar.");
				}else if (getFolioEmpresa() == 0) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, debe especificar un número de orden de compra.");
				}else {

					VisorOrdenesBean visorBean = new VisorOrdenesBean();
					VisorOrdenesForm visorForm = visorBean.consultarOrdenXfolioEmpresa(con, rc.getEsquema(), getFolioEmpresa());
					
					ValidacionesCartaPorte cartasBean = new ValidacionesCartaPorte();
					HashMap<String, Object> MAPA_RESULTADO = cartasBean.procesarXML(con, rc.getEsquema(), fileXML, filePDF, getUsuario(request), session.getLenguaje(), true, getFolioEmpresa(), 0, visorForm.getFolioOrden(), "CARTA");
					
					String MENSAJE = null;
					if (!"".equals(Utils.noNulo(MAPA_RESULTADO.get("MENSAJE")))) {
						MENSAJE = Utils.noNulo(MAPA_RESULTADO.get("MENSAJE")).toString();
					}else {
						MENSAJE = "ERROR";
					}
					
					if ("OK".equalsIgnoreCase(MENSAJE)) {
						visorModel.setCodError("000");
						visorModel.setMensajeError(Utils.regresaCaracteresNormales("Estimado Proveedor su Carta Porte ha sigo validada satisfactoriamente."));
					}else{
						visorModel.setCodError("001");
						visorModel.setMensajeError(MENSAJE);
						
					}
					
				}
				
				JSONObject json = new JSONObject(visorModel);
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
	

	public String cargarFacturaMultiple() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		VisorOrdenesModel visorModel = new VisorOrdenesModel();
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
				
				Part filePart = request.getPart("fileTXT");
            	File fileTXT = UtilsFile.getFileFromPart(filePart);
            	
            	filePart = request.getPart("filePDF");
            	File filePDF = UtilsFile.getFileFromPart(filePart);
            	
            	filePart = request.getPart("fileXML");
            	File fileXML = UtilsFile.getFileFromPart(filePart);
            	
				if (fileTXT == null) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .TXT para continuar.");
				}else if (filePDF == null) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .PDF para continuar.");
				}else if (fileXML == null) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .XML para continuar.");
				}else {
					ValidacionesMultiple valMultipleBean = new ValidacionesMultiple();
					UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					String arrValidaciones [] = valMultipleBean.iniciarProceso(session.getEsquemaEmpresa(), fileTXT, fileXML, filePDF, session.getLenguaje(), usuariosForm.getIdPerfil(), getUsuario(request));
					if ("ERROR".equalsIgnoreCase(arrValidaciones[2])) {
						visorModel.setCodError("001");
					}else {
						visorModel.setCodError("000");	
					}
					visorModel.setMensajeError(arrValidaciones[0]);
				}
				
				JSONObject json = new JSONObject(visorModel);
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
	
	
	public String consultarClaveProductoPorOrden(){

		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	VisorClavesCFDI visorClavesBean = new VisorClavesCFDI();
    	try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				logger.info("********************* VALIDANDO DATOS ******************************"+getFolioEmpresa());
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    String idMultiple = visorClavesBean.isMultiple(con, session.getEsquemaEmpresa(), getFolioEmpresa());
			    logger.info("idMultiple---->"+idMultiple);
			    VisorOrdenesForm visorForm = null;
			    if ("".equals(idMultiple)) {
			    	visorForm = visorClavesBean.getDescripciones(con, session.getEsquemaEmpresa(), getFolioEmpresa());
			    }else {
			    	visorForm = visorClavesBean.getDescripcionesMultiple(con, session.getEsquemaEmpresa(), idMultiple, getFolioEmpresa());					    	
			    }
			    JSONObject json = new JSONObject(visorForm);
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
	
	
	
	public String detalleClaveProductoPorOrdenXML() {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	VisorClavesCFDI visorClavesBean = new VisorClavesCFDI();
    	try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
		    
		    response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
				    String idMultiple = visorClavesBean.isMultiple(con, session.getEsquemaEmpresa(), getFolioEmpresa());
				    VisorOrdenesForm visorForm = null;
				    ArrayList<ConfClaveUsoCFDIForm> listaDetalle = null;
				    if ("".equals(idMultiple)) {
				    	visorForm = visorClavesBean.getDescripciones(con, session.getEsquemaEmpresa(), getFolioEmpresa());
				    	
				    	if(visorForm.getFolioEmpresa() == 0) {
				    		listaDetalle = visorClavesBean.buscarClavePorOrden(con, session.getEsquemaEmpresa(), getFolioEmpresa(), "", "");
					    }
					    else {
					    	listaDetalle = visorClavesBean.buscarClavePorOrden(con, session.getEsquemaEmpresa(), getFolioEmpresa(), visorForm.getRfc(), visorForm.getUsoCFDI());
					    }
				    	
				    	if (listaDetalle == null || listaDetalle.size() == 0) {
				    		listaDetalle = visorClavesBean.buscarClavePorOrdenSinUso(con, session.getEsquemaEmpresa(), getFolioEmpresa(), visorForm.getRfc(), visorForm.getUsoCFDI());
				    	}
				    	
				    }else {
				    	visorForm = visorClavesBean.getDescripcionesMultiple(con, session.getEsquemaEmpresa(), idMultiple, getFolioEmpresa());
				    	listaDetalle = visorClavesBean.buscarClavePorOrdenMultiple(con, session.getEsquemaEmpresa(), idMultiple, visorForm.getRfc(), visorForm.getUsoCFDI());
				    }
				    
				    
				    ConfClaveUsoCFDIModel visorModel = new ConfClaveUsoCFDIModel();
					visorModel.setData(listaDetalle);
					visorModel.setRecordsFiltered(20);
					visorModel.setDraw(-1);
					visorModel.setRecordsTotal(listaDetalle.size());
					JSONObject json = new JSONObject(visorModel);
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

	
	
	
	
	public String modificaOrdenClaveProducto()
    {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	VisorOrdenesBean visorBean = new VisorOrdenesBean();
    	ProveedoresBean provBean = new ProveedoresBean();
    	ProveedoresForm provForm = new ProveedoresForm();
    	
    	
    	ConfClaveUsoCFDIBean confUsoBean = new ConfClaveUsoCFDIBean();
    	VisorClavesCFDI visorClavesBean = new VisorClavesCFDI();
    	
    	VisorOrdenesModel visorModel = new VisorOrdenesModel();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				String tipoAccion = Utils.noNulo(request.getParameter("tipoAccion"));
				String usoCFDI = getUsoCFDI();
				
			    UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				
				if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
				}else {
					VisorOrdenesForm visorForm = visorBean.consultarOrdenXfolioEmpresa(con, rc.getEsquema(), getFolioEmpresa());
					provForm = provBean.consultarProveedor(con, rc.getEsquema(), visorForm.getClaveProveedor());
					
					// logger.info("tipoAccion------>"+tipoAccion);
					String estatusPago = "A5";
					if (tipoAccion.equalsIgnoreCase("CORRECTA") && "1".equalsIgnoreCase(visorForm.getServicioRecibido())){
						estatusPago = "A3";
					}else if (tipoAccion.equalsIgnoreCase("CORRECTA") && "0".equalsIgnoreCase(visorForm.getServicioRecibido())) {
						estatusPago = "A1";
					}else if (tipoAccion.equalsIgnoreCase("INCORRECTA") && "1".equalsIgnoreCase(visorForm.getServicioRecibido())) {
						estatusPago = "A2";
					}else if (tipoAccion.equalsIgnoreCase("INCORRECTA") && "0".equalsIgnoreCase(visorForm.getServicioRecibido())) {
						estatusPago = "A5";
					}
					
					String mensajeCorreo = "";
					String idMultiple = visorClavesBean.isMultiple(con, session.getEsquemaEmpresa(), getFolioEmpresa());
				    int resultado = -1;
				    logger.info("idMultiple---->"+idMultiple);
				    if ("".equals(idMultiple)) {
						resultado = visorBean.actualizarOrdenClaveProducto(con, session.getEsquemaEmpresa(), getFolioEmpresa(), tipoAccion, estatusPago, getUsuario(request));
						if (tipoAccion.equalsIgnoreCase("CORRECTA")) {
							int totReg = visorClavesBean.guardarClavesProducto(con, session.getEsquemaEmpresa(), getFolioEmpresa(), provForm.getRfc(), usoCFDI, getUsuario(request));
							logger.info("Total de Registros agregados de clave producto......"+totReg);
							mensajeCorreo = UtilsHTML.generaHTMLClavesCFDIOK(getFolioEmpresa(), provForm.getRazonSocial());
							
						}else {
							mensajeCorreo = UtilsHTML.generaHTMLClavesCFDING(getFolioEmpresa(), provForm.getRazonSocial());
							visorClavesBean.eliminaClavesProducto(con, session.getEsquemaEmpresa(), getFolioEmpresa());
						}
				    }else {
				    	String todasOrdenes = visorClavesBean.getTodasOrdenesMultiple(con, session.getEsquemaEmpresa(), idMultiple);
				    	resultado = visorBean.actualizarOrdenClaveProductoMultiple(con, session.getEsquemaEmpresa(), idMultiple, tipoAccion, estatusPago);
				    	if (tipoAccion.equalsIgnoreCase("CORRECTA")) {
							int totReg = visorClavesBean.guardarClavesProductoMultiple(con, session.getEsquemaEmpresa(), idMultiple, provForm.getRfc(), usoCFDI, getUsuario(request));
							// logger.info("Total de Registros agregados de clave producto......"+totReg);
							mensajeCorreo = UtilsHTML.generaHTMLClavesCFDIMultipleOK(todasOrdenes, provForm.getRazonSocial());
						}else {
							confUsoBean.eliminaClavesProductoMultiple(con, session.getEsquemaEmpresa(), idMultiple);
							mensajeCorreo = UtilsHTML.generaHTMLClavesCFDIMultipleNG(todasOrdenes, provForm.getRazonSocial());
						}
				    }
				    
				    logger.info("mensajeCorreo------>"+mensajeCorreo);
					
					String listaCorreosProveedores [] = null;
					String subject = "";
					
					if (tipoAccion.equalsIgnoreCase("CORRECTA")){
						subject = "SIAREX - Factura Procesada Exitosamente";
						
					}else{
						
						subject = "SIAREX - Error al Procesar su Factura";
						
						String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
						String directorio = "";
						directorio = session.getEsquemaEmpresa()+"/PROVEEDORES/" + visorForm.getClaveProveedor() + "/";
						String rutaArchivo = rutaFinal + directorio;
						
						String rutaPDF = rutaArchivo + visorForm.getNombrePDF();
						String rutaXML = rutaArchivo + visorForm.getNombreXML();
						logger.info("Ruta PDF---->"+rutaPDF);
						logger.info("rutaXML---->"+rutaXML);
				    	UtilsFile.eliminaArchivo(rutaPDF);
				    	UtilsFile.eliminaArchivo(rutaXML);

					}
					
					listaCorreosProveedores = UtilsBD.obtenerCorreorProveedor(con, session.getEsquemaEmpresa(), visorForm.getClaveProveedor(), "N", "S", "N","N");
			        AccesoBean accesoBean = new AccesoBean();
			        EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
					EnviaCorreoGrid.enviarCorreo(null, mensajeCorreo, false, listaCorreosProveedores, null, subject, empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
			        
			       
					if (resultado >= 1) {
						visorModel.setCodError("000");
						visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");	
					}else {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}
				}
			    
			    JSONObject json = new JSONObject(visorModel);
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
	
	
	
	public String consultarClaveProductoCartaPorte(){

		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ValidacionesCartaPorte visorClavesBean = new ValidacionesCartaPorte();
    	try{
    		response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon(); 
			    
			    CartasPorteForm cartasPorteForm = visorClavesBean.getDescripciones(con, session.getEsquemaEmpresa(), getFolioEmpresa());
			    
			    JSONObject json = new JSONObject(cartasPorteForm);
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
	
	
	public String detalleClaveProductoCartaPorte() {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ValidacionesCartaPorte valCartaPorte = new ValidacionesCartaPorte();

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

			    long folioEmpresa = getFolioEmpresa();
			    	ArrayList<ConfClaveUsoCFDIForm> listaDetalle = null;
	
				    CartasPorteForm cartasPorteForm = valCartaPorte.getDescripciones(con, session.getEsquemaEmpresa(), getFolioEmpresa());
				    
				    if(cartasPorteForm.getFolioEmpresa() == 0) {
				    	listaDetalle = valCartaPorte.buscarClavePorOrden(con, session.getEsquemaEmpresa(), folioEmpresa, "", "");
				    }
				    else {
				    	listaDetalle = valCartaPorte.buscarClavePorOrden(con, session.getEsquemaEmpresa(), folioEmpresa, cartasPorteForm.getRfc(), cartasPorteForm.getUsoCFDI());
				    }
			    	
			    	if (listaDetalle == null || listaDetalle.size() == 0) {
			    		listaDetalle = valCartaPorte.buscarClavePorOrdenSinUso(con, session.getEsquemaEmpresa(), getFolioEmpresa(), cartasPorteForm.getRfc(), cartasPorteForm.getUsoCFDI());
			    	}
			    	
			    
			    		ConfClaveUsoCFDIModel visorModel = new ConfClaveUsoCFDIModel();
						visorModel.setData(listaDetalle);
						visorModel.setRecordsFiltered(20);
						visorModel.setDraw(-1);
						visorModel.setRecordsTotal(listaDetalle.size());
						JSONObject json = new JSONObject(visorModel);
						out.print(json);
			            out.flush();
			            out.close();  
			}
		}
    	catch(Exception e){
			Utils.imprimeLog("", e);
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
	
	
	
	public String modificaOrdenClaveProductoCartaPorte(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	
    	VisorOrdenesBean visorBean = new VisorOrdenesBean();
    	ProveedoresBean provBean = new ProveedoresBean();
    	
    	ValidacionesCartaPorte valCartaBean = new ValidacionesCartaPorte();
    	
    	VisorOrdenesModel visorModel = new VisorOrdenesModel();
		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				logger.info("*********** MODIFICANDO CARTA PORTE CLAVES **********");
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				
			    UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				
				if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
					visorModel.setCodError("001");
					visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
				}else {
					String tipoAccion = Utils.noNulo(request.getParameter("tipoAccion"));
					String folioEmpresa = Utils.noNulo(request.getParameter("folioEmpresa"));
					String usoCFDI = getUsoCFDI();
					
					VisorOrdenesForm visorForm = visorBean.consultarOrdenXfolioEmpresa(con, rc.getEsquema(), getFolioEmpresa());
					ProveedoresForm provForm = provBean.consultarProveedor(con, rc.getEsquema(), visorForm.getClaveProveedor());
					
					
					CartasPorteForm cartaForm =  valCartaBean.consultaCartas(con, session.getEsquemaEmpresa(), Double.parseDouble(folioEmpresa));
					String estatusPago = "A5";
					if ("T".equalsIgnoreCase(cartaForm.getTipoComprobante())) {
						estatusPago = cartaForm.getEstatusOrden();
					}else {
						if (tipoAccion.equalsIgnoreCase("CORRECTA") && "1".equalsIgnoreCase(visorForm.getServicioRecibido())){
							estatusPago = "A3";
						}
						else if (tipoAccion.equalsIgnoreCase("CORRECTA") && "0".equalsIgnoreCase(visorForm.getServicioRecibido())) {
							estatusPago = "A1";
						}
						else if (tipoAccion.equalsIgnoreCase("INCORRECTA") && "1".equalsIgnoreCase(visorForm.getServicioRecibido())) {
							estatusPago = "A2";
						}
						else if (tipoAccion.equalsIgnoreCase("INCORRECTA") && "0".equalsIgnoreCase(visorForm.getServicioRecibido())) {
							estatusPago = "A5";
						}
					}
					
					String mensajeCorreo = "";
					int resultado = visorBean.actualizarOrdenClaveProducto(con, session.getEsquemaEmpresa(), Long.parseLong(folioEmpresa), tipoAccion, estatusPago, getUsuario(request));
					if (tipoAccion.equalsIgnoreCase("CORRECTA")) {
						int totReg = valCartaBean.guardarClavesProducto(con, session.getEsquemaEmpresa(), Long.parseLong(folioEmpresa), provForm.getRfc(), usoCFDI, getUsuario(request));
						logger.info("Total de Registros agregados de clave producto......"+totReg);
						// mensajeCorreo = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioEmpresa+" fue procesada exitosamente y sera pagada de acuerdo a nuestro calendario de pagos.";
						mensajeCorreo = UtilsHTML.generaHTMLClavesCFDIOK(getFolioEmpresa(), provForm.getRazonSocial());
					} else {
						// mensajeCorreo = "Estimado proveedor le informamos que su orden de compra "+folioEmpresa+" no pudo ser procesada exitosamente debido a que la clave producto seleccionado en su carta porte es incorrecto.";
						mensajeCorreo = UtilsHTML.generaHTMLClavesCartaPorteNG(getFolioEmpresa(), provForm.getRazonSocial());
					}
				    
				    logger.info("mensajeCorreo------>"+mensajeCorreo);
					
					String listaCorreosProveedores [] = null;
					String subject = "";
					
					if (tipoAccion.equalsIgnoreCase("CORRECTA")){
						subject = "SIAREX - Factura Procesada Exitosamente";
					} else{
						String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
						String directorio = "";
						directorio = session.getEsquemaEmpresa()+"/PROVEEDORES/" + visorForm.getClaveProveedor() + "/";
						String rutaArchivo = rutaFinal + directorio;
						
						// JSONObject jsonCARTA_PORTE = cartasPorteBean.buscarDocumentoFolioEmpresa(con, session.getEsquemaEmpresa(), Long.parseLong(folioEmpresa));
						
						if ("T".equalsIgnoreCase(cartaForm.getTipoComprobante())) {
							String rutaPDF = rutaArchivo + cartaForm.getNombrePDF();
							String rutaXML = rutaArchivo + cartaForm.getNombreXML();
							logger.info("Ruta PDF Carta Porte---->"+rutaPDF);
							logger.info("rutaXML Carta Porte---->"+rutaXML);
					    	UtilsFile.eliminaArchivo(rutaPDF);
					    	UtilsFile.eliminaArchivo(rutaXML);
							
						}
						
						valCartaBean.eliminarCartaPorteFolioEmpresa(con, session.getEsquemaEmpresa(), Integer.parseInt(folioEmpresa));
						
						subject = "SIAREX - Error al Procesar su Factura";
					}
					
					listaCorreosProveedores = UtilsBD.obtenerCorreorProveedor(con, session.getEsquemaEmpresa(), visorForm.getClaveProveedor(), "N", "S", "N","N");
			        
			        AccesoBean accesoBean = new AccesoBean();
			        EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
					EnviaCorreoGrid.enviarCorreo(null, mensajeCorreo, false, listaCorreosProveedores, null, subject, empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
			        
			       
					if (resultado >= 1) {
						visorModel.setCodError("000");
						visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");	
					}else {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}
				}
				
			    JSONObject json = new JSONObject(visorModel);
				out.print(json);
	            out.flush();
	            out.close();
	            
		        
		        
			}
		}
		catch(Exception e){
			Utils.imprimeLog("", e);
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
	
	
	 public String validarComplemento()
	    {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesComplemento visorCompleBean = new ValidacionesComplemento();
			logger.info("*************** PROCESANDO ************");
			
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
				    rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    
				    Part filePart = request.getPart("filePDF");
	            	File filePDF = UtilsFile.getFileFromPart(filePart);
	            	
	            	filePart = request.getPart("fileXML");
	            	File fileXML = UtilsFile.getFileFromPart(filePart);
	            	
				    
				    HashMap<String, Object> MAPA_RESULTADO = visorCompleBean.procesarXML(con, session.getEsquemaEmpresa(), fileXML.getAbsolutePath(), filePDF.getAbsolutePath(), getUsuario(request), session.getLenguaje(), true); 
					
					logger.info("MAPA_RESULTADO==="+MAPA_RESULTADO);
					String MENSAJE = null;
					if (!"".equals(Utils.noNulo(MAPA_RESULTADO.get("MENSAJE")))) {
						MENSAJE = Utils.noNulo(MAPA_RESULTADO.get("MENSAJE")).toString();
					}else {
						MENSAJE = "ERROR";
					}
					// StringBuffer sbRetorno = new StringBuffer();
					VisorOrdenesModel visorModel = new VisorOrdenesModel();
					if ("OK".equalsIgnoreCase(MENSAJE)) {
						/*
						sbRetorno.append(MENSAJE).append(";")
						         .append(MAPA_RESULTADO.get("UUID_COMPLEMENTO")).append(";")
						         .append(MAPA_RESULTADO.get("RAZON_SOCIAL")).append(";")
						         .append(MAPA_RESULTADO.get("ESTADO_SAT")).append(";")
						         .append(MAPA_RESULTADO.get("ESTATUS_SAT")).append(";")
						         .append(Utils.regresaCaracteresNormales("Estimado Proveedor su Complemento de Pago ha sigo validado satisfactoriamente, presione el botón de \"Asignar\" para terminar el proceso.")).append(";");
						  */
						visorModel.setCodError("000");
						visorModel.setMensajeValidacion(Utils.regresaCaracteresNormales("Estimado Proveedor su Complemento de Pago ha sigo validado satisfactoriamente, presione el botón de \"Asignar\" para terminar el proceso."));
						visorModel.setUuidValida(MAPA_RESULTADO.get("UUID_COMPLEMENTO").toString());
						visorModel.setRazonSocial(MAPA_RESULTADO.get("RAZON_SOCIAL").toString());
						visorModel.setEstadoSAT(MAPA_RESULTADO.get("ESTADO_SAT").toString());
						visorModel.setEstatusSAT(MAPA_RESULTADO.get("ESTATUS_SAT").toString());
						
					}else{
						// sbRetorno.append(MENSAJE).append(";");
						visorModel.setCodError("001");
						visorModel.setMensajeValidacion(MENSAJE);
						if ("NO_CUMPLIO".equalsIgnoreCase(Utils.noNulo(MAPA_RESULTADO.get("BAND_TOTAL")).toString()) ) {
							/*sbRetorno.append(MAPA_RESULTADO.get("UUID_COMPLEMENTO")).append(";")
							         .append("TOTAL_NG").append(";");
							         */
							visorModel.setUuidValida(MAPA_RESULTADO.get("UUID_COMPLEMENTO").toString());
						}
					}
					
					
					JSONObject json = new JSONObject(visorModel);
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

	 
	 public String detalleComplemento() {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesComplemento compleBean = new ValidacionesComplemento();
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    
				    ValidacionesComplementoModel visorCompleModel = new ValidacionesComplementoModel();
				    ArrayList<ComplementosForm> listaDetalle = compleBean.detalleXMLCompleTrabajo(con, session.getEsquemaEmpresa(), getUuid());
				    
				    visorCompleModel.setData(listaDetalle);
				    visorCompleModel.setRecordsFiltered(20);
				    visorCompleModel.setDraw(-1);
				    visorCompleModel.setRecordsTotal(listaDetalle.size());
					JSONObject json = new JSONObject(visorCompleModel);
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
	 
	 public String cambiaEstatusComplemento()
	    {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesComplemento compleBean = new ValidacionesComplemento();
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					logger.info("**************** CAMBIANDO ESTATUS COMPLEMENTO *************");
				    rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    
				    int totUpdate = compleBean.guardarComplementaria(con, rc.getEsquema(), getUuid());
				    totUpdate = 1;
				    logger.info("totUpdate===>"+totUpdate);
				    ValidacionesComplementoModel visorModel = new ValidacionesComplementoModel();
					    if (totUpdate == 0) {
					    	visorModel.setCodError("001");
							visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");	
						}else  {
							compleBean.actualizaEstatusOrden(con, rc.getEsquema(), getUuid(), "A6", getUsuario(request));
							
							visorModel.setCodError("000");
							visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
						}

					    JSONObject json = new JSONObject(visorModel);
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

	 
	 public String eliminarComplementoPantalla()
	    {
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesComplemento compleBean = new ValidacionesComplemento();
			ValidacionesComplementoModel visorModel = new ValidacionesComplementoModel();
			LenguajeBean lenguajeBean = LenguajeBean.instance();
			try{
				logger.info("*********** CANCELANDO COMPLEMENTO ********************");
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
				    rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    
				    HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "VISOR");
				    
				    UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					
					if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
						visorModel.setCodError("001");
						// visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
						visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG29")));
						
					}else {
						int totReg =  compleBean.eliminarComplementariaTrabajo(con, session.getEsquemaEmpresa(), getUuid());	
					}
					visorModel.setCodError("000");
					visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				     
				}
				    JSONObject json = new JSONObject(visorModel);
					out.print(json);
		            out.flush();
		            out.close();
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
	    
	 
	 public String consultaComplemento()
	    {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesComplemento compleBean = new ValidacionesComplemento();
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					
				    rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    
				    ComplementosForm compleForm = compleBean.consultaComplemento(con, session.getEsquemaEmpresa(), getFolioEmpresa());
				    JSONObject json = new JSONObject(compleForm);
					out.print(json);
		            out.flush();
		            out.close();
				    
				    // logger.info("json==>"+json);
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
	 
	 
	 public String listadoOrdenesComplemento() {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesComplemento compleBean = new ValidacionesComplemento();
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    
				    ValidacionesComplementoModel visorCompleModel = new ValidacionesComplementoModel();
				    ArrayList<ComplementosForm> listaDetalle = compleBean.listadoComplementoOrdenes(con, session.getEsquemaEmpresa(), getUuid());
				    
				    visorCompleModel.setData(listaDetalle);
				    visorCompleModel.setRecordsFiltered(20);
				    visorCompleModel.setDraw(-1);
				    visorCompleModel.setRecordsTotal(listaDetalle.size());
					JSONObject json = new JSONObject(visorCompleModel);
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
	 
	 
	 public String eliminarComplemento()
	    {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesComplemento compleBean = new ValidacionesComplemento();
			ValidacionesComplementoModel visorModel = new ValidacionesComplementoModel();
			
			LenguajeBean lenguajeBean = LenguajeBean.instance();
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					logger.info("*************** ELIMINANDO LOS COMPLEMENTOS ******************"+getUsuario(request));
				    rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "VISOR");
				    UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					
					if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
						visorModel.setCodError("001");
						//visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
						visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG29")));
					}else {
						logger.info("*************** folioEmpresa ******************"+getFolioEmpresa());
					    logger.info("*************** uuid_XML ******************"+getUuid());
					    ComplementosForm compleForm =  compleBean.consultaComplemento(con, session.getEsquemaEmpresa(), getFolioEmpresa());
					    
					    ArrayList<ComplementosForm> listaDetalle = compleBean.listadoComplementoOrdenes(con, session.getEsquemaEmpresa(), getUuid());
					    compleBean.actualizaEstatusOrden(con, rc.getEsquema(), getUuid(), "A4", getUsuario(request));
					    int totReg =  compleBean.eliminarComplementaria(con, rc.getEsquema(), getUuid());
					    if (totReg > 0) {
					    	  String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
								
								 int claveProveedor = compleForm.getClaveProveedor();
								// String nombreXML = Utils.noNuloNormal(request.getParameter("nombreXML"));
								// String nombrePDF = Utils.noNuloNormal(request.getParameter("nombrePDF"));
								
					    	  // logger.info("*************** jsonObject ******************"+jsonObject);
								
								String directorio = "";
								directorio =  session.getEsquemaEmpresa()+"/PROVEEDORES/" + claveProveedor + "/";
								String rutaArchivo = rutaFinal + directorio;
								
								String rutaXMLSource = rutaArchivo + compleForm.getNombreXML();
								String rutaPDFSource = rutaArchivo + compleForm.getNombrePDF();
						    	// UtilsFile.eliminaArchivo(rutaXML);
						    	// UtilsFile.eliminaArchivo(rutaPDF);
								
								String rutaEliminar = UtilsPATH.RUTA_PUBLIC_HTML + "DOCUMENTOS_ELIMINADOS" + File.separator + session.getEsquemaEmpresa() + File.separator + "PROVEEDORES" + File.separator + claveProveedor + File.separator;
								String rutaXMLDest = rutaEliminar + compleForm.getNombreXML();
								String rutaPDFDest = rutaEliminar + compleForm.getNombrePDF();
								
								try {
									
									File fileXMLSource = new File(rutaXMLSource);
									File filePDFSource = new File(rutaPDFSource);
									
									File fileXMLDest = new File(rutaXMLDest);
									File filePDFDest = new File(rutaPDFDest);
									
									UtilsFile.moveFileDirectory(fileXMLSource, fileXMLDest, true, false, true, true);
									UtilsFile.moveFileDirectory(filePDFSource, filePDFDest, true, false, true, true);
									
								}catch(Exception e) {
									Utils.imprimeLog("", e);
								}
		        				
						    	
						    	String subJect = "SIAREX - Rechazo Complemento Pago";
						    	String [] listaCorreos = UtilsBD.obtenerCorreorProveedor(con, session.getEsquemaEmpresa(), claveProveedor, "N", "S", "N","N");
						    	
						    	
						    	String mensajeCorreo = UtilsHTML.generaHTMLComplementoEliminar(listaDetalle, compleForm.getRazonSocial(), getUuid() );
						    	logger.info("mensajeCorreo----->"+mensajeCorreo);
						    	logger.info("listaCorreos----->"+listaCorreos);
						    	
						    	logger.info("mensajeCorreo--->"+mensajeCorreo);
						    	
						    	AccesoBean accesoBean = new AccesoBean();
						    	EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
						    			
						    	EnviaCorreoGrid.enviarCorreo(null, mensajeCorreo, false, listaCorreos, null,  subJect, empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
							   
						    	
								visorModel.setCodError("000");
								visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
							
							   
					    }else {
					    	visorModel.setCodError("001");
							// visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					    	visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG30")));
					    }
					}
				    
				    JSONObject json = new JSONObject(visorModel);
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
	 
	 // termina complementos de pago
	 public String validarNotaCredito()
	    {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesNotaCredito notaCreditoBean = new ValidacionesNotaCredito();
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
				    rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    String pathRepositorio = UtilsPATH.REPOSITORIO_DOCUMENTOS;
				    
				    Part filePart = request.getPart("filePDF");
	            	File filePDF = UtilsFile.getFileFromPart(filePart);
	            	
	            	filePart = request.getPart("fileXML");
	            	File fileXML = UtilsFile.getFileFromPart(filePart);
	            	
				    
					HashMap<String, Object> MAPA_RESULTADO = notaCreditoBean.procesarXML(con, rc.getEsquema(), session.getEsquemaEmpresa(), fileXML, filePDF, pathRepositorio, getUsuario(request), session.getLenguaje(), true); 
					
					
					String MENSAJE = null;
					if (!"".equals(Utils.noNulo(MAPA_RESULTADO.get("MENSAJE")))) {
						MENSAJE = Utils.noNulo(MAPA_RESULTADO.get("MENSAJE")).toString();
					}else {
						MENSAJE = "ERROR";
					}
					
					VisorOrdenesModel visorModel = new VisorOrdenesModel();
					if ("OK".equalsIgnoreCase(MENSAJE)) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(session.getLenguaje());
						//String mensajeFinal = Utils.regresaCaracteresNormales("Estimado proveedor, le informamos que su nota de cr(e)dito fue procesada de manera exitosa y esta ser(a) aplicada dentro nuestro est(a)ndares de pago, presione el bot(o)n de \"Asignar\" para terminar el proceso.")
						visorModel.setCodError("000");
						String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE53);
						visorModel.setMensajeValidacion(mensajeFinal);
						visorModel.setUuidValida(MAPA_RESULTADO.get("UUID_NOTA").toString());
						visorModel.setRazonSocial(MAPA_RESULTADO.get("RAZON_SOCIAL").toString());
						visorModel.setEstadoSAT(MAPA_RESULTADO.get("ESTADO_SAT").toString());
						visorModel.setEstatusSAT(MAPA_RESULTADO.get("ESTATUS_SAT").toString());
						visorModel.setClaveProveedor(MAPA_RESULTADO.get("CLAVE_PROVEEDOR").toString());
					}else{
						visorModel.setCodError("001");
						visorModel.setMensajeValidacion(MENSAJE);
						if ("NO_CUMPLIO".equalsIgnoreCase(Utils.noNulo(MAPA_RESULTADO.get("BAND_TOTAL")).toString()) ) {
							visorModel.setUuidValida(MAPA_RESULTADO.get("UUID_COMPLEMENTO").toString());
						}
					}
					
					JSONObject json = new JSONObject(visorModel);
					out.print(json);
		            out.flush();
		            out.close();
		            logger.info("json==>"+json);
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
	 
	 
	 
	 public String detalleNotaCredito()
	    {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesNotaCredito notaCreditoBean = new ValidacionesNotaCredito();
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				   
				    ValidacionesNotaCreditoModel visorNota = new ValidacionesNotaCreditoModel();
				    ArrayList<NotaCreditoForm> listaDetalle = notaCreditoBean.detalleXMLNotaCredito(con, session.getEsquemaEmpresa(), getUuid());
				    visorNota.setData(listaDetalle);
				    visorNota.setRecordsFiltered(20);
				    visorNota.setDraw(-1);
				    visorNota.setRecordsTotal(listaDetalle.size());
					JSONObject json = new JSONObject(visorNota);
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
	 
	 
	 
	 public String cambiaEstatus()
	    {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesNotaCredito notaCreditoBean = new ValidacionesNotaCredito();
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					
				    rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    String uuid_XML = request.getParameter("UUID");
				    if (uuid_XML == null) {
				    	uuid_XML = "";
				    }
				    logger.info("************ actualizando estatus ***********"+getUuid());
				    int totUpdate = notaCreditoBean.actualizaEstatus(con, session.getEsquemaEmpresa(), getUuid(), getUsuario(request));
				    //totUpdate = 1;
				    
				    logger.info("totUpdate===>"+totUpdate);
				   ValidacionesNotaCreditoModel visorModel = new ValidacionesNotaCreditoModel();
				    if (totUpdate == 0) {
				    	visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");	
					}else  {
						visorModel.setCodError("000");
						visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}

				    JSONObject json = new JSONObject(visorModel);
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
	 
	 
	 	public String buscarNotaCredito() throws Exception {
			Connection con = null;
			Connection conEmpresa = null;
			ResultadoConexion rc = null;
			ValidacionesNotaCredito notaCreditoBean = new ValidacionesNotaCredito();
			try{
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				PrintWriter out = response.getWriter();
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				 SiarexSession session = ObtenerSession.getSession(request);
					if ("".equals(session.getEsquemaEmpresa())){
						return Action.LOGIN;
					}else{
						rc = getConnection(session.getEsquemaEmpresa());
						conEmpresa = rc.getCon();
						NotaCreditoForm notaForm  =  notaCreditoBean.buscarNotaCredito(conEmpresa, session.getEsquemaEmpresa(), getFolioOrden());
						if (notaForm.getFolioEmpresa() > 0) {
							mostrarNotaCredito(request, notaForm.getNombrePDF(), session.getEsquemaEmpresa(), notaForm.getClaveProveedor());
						}
						
						JSONObject json = new JSONObject(notaForm);
							out.print(json);
				            out.flush();
				            out.close();
			            
					}
			}catch(Exception e){
				Utils.imprimeLog("", e);
			}finally{
				try{
					if (conEmpresa != null){
						conEmpresa.close();
					}
					conEmpresa = null;
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
	 	
	 	
	 	 public String modificaNotaCredito()
		    {
		    	
		    	HttpServletResponse response = null;
		    	HttpServletRequest request = null;
		    	PrintWriter out = null;
		    
				Connection con = null;
				ResultadoConexion rc = null;
				
				ValidacionesNotaCredito notaCreditoBean = new ValidacionesNotaCredito();

				try{
					response = ServletActionContext.getResponse();
			    	request = ServletActionContext.getRequest();
			    	
					out = response.getWriter();
					
					response.setContentType("text/html; charset=UTF-8");
					response.setCharacterEncoding("UTF-8");
					
				    SiarexSession session = ObtenerSession.getSession(request);
					if ("".equals(session.getEsquemaEmpresa())){
						return Action.LOGIN;
					}else{
						
					    rc = getConnection(session.getEsquemaEmpresa());
					    con = rc.getCon();
					    
					    UsuariosBean usuarioBean = new UsuariosBean();
						UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
						
						ValidacionesNotaCreditoModel visorModel = new ValidacionesNotaCreditoModel();
						if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
							visorModel.setCodError("001");
							visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
						}else {
						    NotaCreditoForm notaForm = notaCreditoBean.buscarNotaCredito(con, rc.getEsquema(), getFolioOrden());
						    
							   String subject = "";
						    	String mensajeCorreo = "";
						    	String tipoAccion = Utils.noNulo(request.getParameter("tipoAccion"));
						    	
						    	int totUpdate = 0;
						    	
							    if ("CORRECTO".equalsIgnoreCase(tipoAccion)) {
							    	totUpdate = notaCreditoBean.actualizaEstatusCuentas(con, rc.getEsquema(), notaForm.getUuidOrden(), notaForm.getUuidNotaCredito());
							    	
							    	mensajeCorreo = UtilsHTML.generaHTMLNotaCreditoAceptada(notaForm.getRazonSocial());
							    	subject = "SIAREX - Nota de Credito Aceptada";
							    	
							    }else {
							    	totUpdate = notaCreditoBean.regresaEstatusOrden(con, session.getEsquemaEmpresa(), notaForm.getUuidOrden(), notaForm.getUuidNotaCredito());
								    
							    	MotivosBean motivosBean = new MotivosBean();
									MotivosForm motivosForm =  motivosBean.obtenerMotivo(con, session.getEsquemaEmpresa(), getClaveMotivo());
									subject = "SIAREX - " +  motivosForm.getSubject();
									mensajeCorreo = motivosForm.getMensaje();
									
									String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
									String directorio = "";
									directorio = session.getEsquemaEmpresa()+"/PROVEEDORES/" + notaForm.getClaveProveedor() + "/";
									String rutaArchivo = rutaFinal + directorio;
									logger.info("Iniciando proceso de carga rutaArchivo..."+rutaArchivo);
									
									String rutaPDF = rutaArchivo + notaForm.getNombrePDF();
							    	UtilsFile.eliminaArchivo(rutaPDF);
							    	
							    	String rutaXML = rutaArchivo + notaForm.getNombreXML();
							    	UtilsFile.eliminaArchivo(rutaXML);
							    }
							    
							    String listaCorreosProveedores [] = UtilsBD.obtenerCorreorProveedor(con, session.getEsquemaEmpresa(), notaForm.getClaveProveedor(), "N", "S", "N","N");
							    logger.info("mensajeCorreo------->"+mensajeCorreo);
							    
							    String mensajeFinal = mensajeCorreo.replaceAll("<<orden_compra>>", String.valueOf(notaForm.getFolioEmpresa()));
							    
							    AccesoBean accesoBean = new AccesoBean();
							    EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
							    EnviaCorreoGrid.enviarCorreo(null, mensajeFinal, false, listaCorreosProveedores, null,  subject, empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
							    
							    
							    logger.info("totUpdate------->"+totUpdate);
							    if (totUpdate == 0) {
							    	visorModel.setCodError("001");
									visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");	
								}else  {
									visorModel.setCodError("000");
									visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
								}							
						}
					    
					    
					    JSONObject json = new JSONObject(visorModel);
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
		 
	 	 
	 	
	 	 public String mostrarNotaCredito(HttpServletRequest request, String nombreArchivo, String nombreRepositorio, int claveProveedor) throws Exception {
				String pathArchivo = "";
				try{
					String fileFactura = UtilsPATH.REPOSITORIO_DOCUMENTOS + nombreRepositorio + File.separator +  "PROVEEDORES" + File.separator + claveProveedor + File.separator + nombreArchivo;
					InputStream imagenEmpleado = new FileInputStream(new File(fileFactura));
					
					//InputStream imagenEmpleado = tableroBean.consultaArchivo(id);
					String filePath = request.getSession().getServletContext().getRealPath("/");
						File file = new File(filePath + "/files/", nombreArchivo);
						BufferedInputStream in = new BufferedInputStream(imagenEmpleado);
						BufferedOutputStream out  = new BufferedOutputStream(new FileOutputStream(file));
						byte[] data = new byte[8896];
						int len = 0;
						while ((len = in.read(data)) > 0) {
							out.write(data, 0, len);
						}
						out.flush();
						out.close();
						in.close();
						pathArchivo = "/siarex247/files/"+nombreArchivo;
				}catch(Exception e){
					Utils.imprimeLog("", e);
					pathArchivo = "/siarex247/files/sinOrden.html";
				}
				return pathArchivo;
			}
	 	 
	 	public String eliminarNotaCredito()
	    {
	    	
	    	HttpServletResponse response = null;
	    	HttpServletRequest request = null;
	    	PrintWriter out = null;
	    
			Connection con = null;
			ResultadoConexion rc = null;
			
			ValidacionesNotaCredito notaCreditoBean = new ValidacionesNotaCredito();
			ValidacionesNotaCreditoModel visorModel = new ValidacionesNotaCreditoModel();
			LenguajeBean lenguajeBean = LenguajeBean.instance();
			try{
				response = ServletActionContext.getResponse();
		    	request = ServletActionContext.getRequest();
		    	
				out = response.getWriter();
				
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					logger.info("*************** ELIMINANDO LA NOTA DE CREDITO ******************"+getUsuario(request));
				    rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    
				    HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "VISOR");
				    UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					
					if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
						visorModel.setCodError("001");
						//visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
						visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG29")));
					}else {
						 logger.info("*************** folioEmpresa ******************"+getFolioEmpresa());
						    logger.info("*************** uuid_XML ******************"+getUuid());
						    
						    ValidacionesComplemento compleBean = new ValidacionesComplemento();
						    ComplementosForm compleForm =  compleBean.consultaComplemento(con, session.getEsquemaEmpresa(), getFolioEmpresa());
						    
						    if (compleForm.getClaveProveedor() == 0) {
						    	 NotaCreditoForm notaForm  =  notaCreditoBean.buscarNotaCredito(con, rc.getEsquema(), getFolioOrden());
								    ArrayList<NotaCreditoForm> listaDetalle = notaCreditoBean.detalleXMLNotaCredito(con, session.getEsquemaEmpresa(), getUuid());
								    int totReg =  notaCreditoBean.eliminarNotaCredito(con, session.getEsquemaEmpresa(), getUuid(), "OK");
								    if (totReg > 0) {
								    	String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
											String directorio = "";
											directorio =  session.getEsquemaEmpresa()+"/PROVEEDORES/" + notaForm.getClaveProveedor() + "/";
											String rutaArchivo = rutaFinal + directorio;
											logger.info("Iniciando proceso de carga rutaArchivo..."+rutaArchivo);
											
											String rutaXMLSource = rutaArchivo + notaForm.getNombreXML();
											String rutaPDFSource = rutaArchivo + notaForm.getNombrePDF();
											logger.info("rutaXML..."+rutaXMLSource);
											logger.info("rutaPDF..."+rutaPDFSource);
											
									    	// UtilsFile.eliminaArchivo(rutaXML);
									    	// UtilsFile.eliminaArchivo(rutaPDF);
											
											String rutaEliminar = UtilsPATH.RUTA_PUBLIC_HTML + "DOCUMENTOS_ELIMINADOS" + File.separator + session.getEsquemaEmpresa() + File.separator + "PROVEEDORES" + File.separator + notaForm.getClaveProveedor() + File.separator;
											String rutaXMLDest = rutaEliminar + notaForm.getNombreXML();
											String rutaPDFDest = rutaEliminar + notaForm.getNombrePDF();
											
											try {
												
												File fileXMLSource = new File(rutaXMLSource);
												File filePDFSource = new File(rutaPDFSource);
												
												File fileXMLDest = new File(rutaXMLDest);
												File filePDFDest = new File(rutaPDFDest);
												
												UtilsFile.moveFileDirectory(fileXMLSource, fileXMLDest, true, false, true, true);
												UtilsFile.moveFileDirectory(filePDFSource, filePDFDest, true, false, true, true);
												
											}catch(Exception e) {
												Utils.imprimeLog("", e);
											}
											
											
									    	String subJect = "SIAREX - Rechazo Nota de Credito";
									    	String [] listaCorreos = UtilsBD.obtenerCorreorProveedor(con, session.getEsquemaEmpresa(), notaForm.getClaveProveedor(), "N", "S", "N","N");
									    	
									    	
									    	String mensajeCorreo = UtilsHTML.generaHTMLNotaCreditoEliminar(listaDetalle, notaForm.getRazonSocial(), getUuid());
									    	logger.info("listaCorreos----->"+listaCorreos);
									    	
									    	AccesoBean accesoBean = new AccesoBean();
									    	EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
									    			
									    	EnviaCorreoGrid.enviarCorreo(null, mensajeCorreo, false, listaCorreos, null,  subJect, empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
									    	
									    	visorModel.setCodError("000");
											visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
										   
								    }else {
								    	visorModel.setCodError("001");
										// visorModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
								    	visorModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG30")));
								    }				    	
						    }else {
						    	visorModel.setCodError("001");
								visorModel.setMensajeError("Error el guardar la información del registro, debe eliminar el complemento de pago asignado a la orden de compra "+getFolioEmpresa());	
						    }
					}
				    JSONObject json = new JSONObject(visorModel);
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

	 	
	 	
	 	
	 	
	 	public String cargarFacturaAmericanaArchivo() throws Exception {
			Connection con = null;
			ResultadoConexion rc = null;
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			VisorOrdenesModel visorModel = new VisorOrdenesModel();
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
					
					Part filePart = request.getPart("filePDF");
		            File filePDF = UtilsFile.getFileFromPart(filePart);
		            	
		            filePart = request.getPart("fileTXT");
		            File fileTXT = UtilsFile.getFileFromPart(filePart);
		            	
		            	
					
					if (filePDF == null) {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .PDF para continuar.");
					}else if (fileTXT == null) {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .TXT para continuar.");
					}else if (getFolioEmpresa() == 0) {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, debe especificar un número de orden de compra.");
					}else {
						ValidacionesAmericano valFacturaBean = new ValidacionesAmericano();
						UsuariosBean usuarioBean = new UsuariosBean();
						UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
						String arrValidaciones [] = valFacturaBean.iniciarProceso(session.getEsquemaEmpresa(), getFolioEmpresa(), fileTXT, filePDF, session.getLenguaje(), usuariosForm.getIdPerfil(), getUsuario(request));
						
						if ("ERROR".equalsIgnoreCase(arrValidaciones[2])) {
							visorModel.setCodError("001");
						}else {
							visorModel.setCodError("000");	
						}
						visorModel.setMensajeError(arrValidaciones[0]);
					}
					
					JSONObject json = new JSONObject(visorModel);
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
	 	
	 	
	 	
	 	public String cargarFacturaAmericanaCaptura() throws Exception {
			Connection con = null;
			ResultadoConexion rc = null;
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			VisorOrdenesModel visorModel = new VisorOrdenesModel();
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
					Part filePart = request.getPart("filePDF");
		            File filePDF = UtilsFile.getFileFromPart(filePart);
		            	
					if (filePDF == null) {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .PDF para continuar.");
					}else if (getFolioEmpresa() == 0) {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, debe especificar un número de orden de compra.");
						
					}else if ("".equalsIgnoreCase(Utils.noNulo(getFechaFactura()))) {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, debe especificar la fecha de la factura.");
						
					}else {
						
						ValidacionesAmericano valFacturaBean = new ValidacionesAmericano();
						UsuariosBean usuarioBean = new UsuariosBean();
						UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
						
						
						// se genera el archivo TXT a procesar....
						
						StringBuffer sbTXT = new StringBuffer();
						sbTXT.append(getSerieFolio()).append("|")
					     	 .append(getFechaFactura()).append("|")
					         .append(getMonto()).append("|")
					         .append(getTipoMoneda());
						
						
						ArrayList<String> datosTXT = new ArrayList<>();
						datosTXT.add(sbTXT.toString());
						
						String rutaDestino = UtilsPATH.RUTA_PUBLIC_HTML + session.getEsquemaEmpresa() + File.separator + "AMERICANOS" + File.separator + System.currentTimeMillis() + ".txt";
						UtilsFile.crearArchivoSalto(datosTXT, rutaDestino);
						
						File fileTXT = new File(rutaDestino);
						
						String arrValidaciones [] = valFacturaBean.iniciarProceso(session.getEsquemaEmpresa(), getFolioEmpresa(), fileTXT, filePDF, session.getLenguaje(), usuariosForm.getIdPerfil(), getUsuario(request));
						
						if ("ERROR".equalsIgnoreCase(arrValidaciones[2])) {
							visorModel.setCodError("001");
						}else {
							visorModel.setCodError("000");	
						}
						visorModel.setMensajeError(arrValidaciones[0]);
						
						fileTXT.delete();
					}
					
					JSONObject json = new JSONObject(visorModel);
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

	 	
	 	
	 	
		public String buscarOrdenAmericana() throws Exception {
			Connection con = null;
			Connection conEmpresa = null;
			ResultadoConexion rc = null;
			VisorOrdenesBean visorBean = new VisorOrdenesBean();
			try{
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				PrintWriter out = response.getWriter();
				
				 SiarexSession session = ObtenerSession.getSession(request);
					if ("".equals(session.getEsquemaEmpresa())){
						return Action.LOGIN;
					}else{
						response.setContentType("text/html; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");
						
						rc = getConnection(session.getEsquemaEmpresa());
						conEmpresa = rc.getCon();
						VisorOrdenesForm visorForm  =  visorBean.consultarOrden(conEmpresa, rc.getEsquema(), getFolioOrden());
						
						if (visorForm.getFolioEmpresa() == 0) {
							
						}else {
							VisorDocumentos visorDoc = new VisorDocumentos();
							ProveedoresBean provBean = new ProveedoresBean();
							ProveedoresForm provForm = provBean.consultarProveedor(conEmpresa, rc.getEsquema(), visorForm.getClaveProveedor());
							visorForm.setRazonSocial(provForm.getRazonSocial());
							visorDoc.mostrarDocumentos(request);	
						}
						
						
						JSONObject json = new JSONObject(visorForm);
						out.print(json);
			            out.flush();
			            out.close();
						
						
					}
			}catch(Exception e){
				Utils.imprimeLog("", e);
			}finally{
				try{
					if (conEmpresa != null){
						conEmpresa.close();
					}
					conEmpresa = null;
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
		
		
		public String modificaOrdenAmericana()
	    {
			Connection con = null;
			ResultadoConexion rc = null;
			
			HttpServletResponse response = ServletActionContext.getResponse();
	    	HttpServletRequest request = ServletActionContext.getRequest();
	    	VisorOrdenesBean visorBean = new VisorOrdenesBean();
	    	String listaCorreosProveedores [] = null;
	    	String mensajeCorreo = "";
	    	String subject = "";
	    	
	    	VisorOrdenesForm visorForm = null;
	    	ProveedoresForm provForm = null;
	    	
	    	VisorOrdenesModel visorModel = new VisorOrdenesModel();
			try{
				PrintWriter out = response.getWriter();
			    SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					
					response.setContentType("text/html; charset=UTF-8");
					response.setCharacterEncoding("UTF-8");
					
					UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					
					if (usuariosForm.getIdPerfil() == 4 || usuariosForm.getIdPerfil() == 5) {
						visorModel.setCodError("001");
						visorModel.setMensajeError("Error el guardar la información del registro, Operación no permitida.");
					}else {
						String tipoAccion = request.getParameter("tipoAccion");
						int claveMotivo = getClaveMotivo();
						
						if (tipoAccion == null){
							tipoAccion = "";
						}
						
						visorForm = visorBean.consultarOrden(con, rc.getEsquema(), getFolioOrden());
						
						ProveedoresBean provBean = new ProveedoresBean();
						provForm = provBean.consultarProveedor(con, rc.getEsquema(), visorForm.getClaveProveedor());
						
						logger.info("serRecibido.........."+visorForm.getServicioRecibido());
						logger.info("ordenSistema.........."+getFolioOrden());
						logger.info("tipoAccion.........."+request.getParameter("tipoAccion"));
						logger.info("ordenCompra.........."+visorForm.getFolioEmpresa());
						logger.info("claveMotivo.........."+claveMotivo);
						
						
						String estatusPago = "";
						String fechaPago = null;
						String serRecibido = visorForm.getServicioRecibido();
						if (serRecibido.equalsIgnoreCase("1") && tipoAccion.equalsIgnoreCase("CORRECTO")){
							estatusPago = "A3";
			        		ConfigAdicionalesBean confSistemaBean = new ConfigAdicionalesBean();
							HashMap<String, String> mapaConf = confSistemaBean.obtenerConfiguraciones(con, session.getEsquemaEmpresa());
							fechaPago =  UtilsValidaciones.obtenerFechaPago(mapaConf);  
							
						}else if (serRecibido.equalsIgnoreCase("0") && tipoAccion.equalsIgnoreCase("CORRECTO")){
							estatusPago = "A1";
						}else if (serRecibido.equalsIgnoreCase("1") && tipoAccion.equalsIgnoreCase("INCORRECTO")){
							estatusPago = "A2";
						}else if (serRecibido.equalsIgnoreCase("0") && tipoAccion.equalsIgnoreCase("INCORRECTO")){
							estatusPago = "A5";
						}
						int resultado = visorBean.actualizarOrdenAmericana(con, session.getEsquemaEmpresa(), getFolioOrden(), estatusPago, tipoAccion, fechaPago, getUsuario(request));
						
						logger.info("fechaPago.........."+fechaPago);
						
						
						if (tipoAccion.equalsIgnoreCase("CORRECTO")){
							subject = "SIAREX - Factura Validada con Exito";
							// mensajeCorreo = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+visorForm.getFolioEmpresa()+" fue procesada exitosamente y sera pagada de acuerdo a nuestro calendario de pagos.";
							mensajeCorreo = UtilsHTML.generaHTMLValidarAmericanaOK(provForm.getRazonSocial(), visorForm.getFolioEmpresa());
						}else{
							MotivosBean motivosBean = new MotivosBean();
							MotivosForm motivosForm = motivosBean.obtenerMotivo(con, rc.getEsquema(), claveMotivo);
							
							subject = "SIAREX - "+ motivosForm.getSubject();
							String mensajeBD = motivosForm.getMensaje();
							
							mensajeCorreo = mensajeBD.replaceAll("<<orden_compra>>", String.valueOf(visorForm.getFolioEmpresa()))
													 .replaceAll("<<serieFactura>>", visorForm.getSerieFolio());
							
					    	String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
							String directorio = "";
							directorio = session.getEsquemaEmpresa()+"/PROVEEDORES/" + visorForm.getClaveProveedor() + "/";
							String rutaArchivo = rutaFinal + directorio;
							logger.info("Iniciando proceso de carga rutaArchivo..."+rutaArchivo);
							
							String rutaPDF = rutaArchivo + visorForm.getNombrePDF();
					    	UtilsFile.eliminaArchivo(rutaPDF);
						}
					}
					
					
					listaCorreosProveedores = UtilsBD.obtenerCorreorProveedor(con, session.getEsquemaEmpresa(), visorForm.getClaveProveedor(), "N", "S", "N","N");
			        
			        AccesoBean accesoBean = new AccesoBean();
				    EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
				    EnviaCorreoGrid.enviarCorreo(null, mensajeCorreo, false, listaCorreosProveedores, null,  subject, empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());			        

			    	visorModel.setCodError("000");
					visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}
					
			    JSONObject json = new JSONObject(visorModel);
				out.print(json);
	            out.flush();
	            out.close();
	            
				
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
			

		public String solicitudLiberacionPago(){
			Connection conEmpresa = null;
			ResultadoConexion rc = null;
			VisorOrdenesBean visorBean = new VisorOrdenesBean();
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			VisorOrdenesModel visorModel = new VisorOrdenesModel();
			
			try{
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				 int folioOrden = Utils.noNuloINT(request.getParameter("folioOrden"));
				 SiarexSession session = ObtenerSession.getSession(request);
					if ("".equals(session.getEsquemaEmpresa())){
						return Action.LOGIN;
					}else{
						rc = getConnection(session.getEsquemaEmpresa());
						conEmpresa = rc.getCon();
						logger.info("Realizando Liberacion de Pago........."+folioOrden);		
						VisorOrdenesForm visorForm =  visorBean.consultarOrden(conEmpresa, rc.getEsquema(), getFolioOrden());  // buscarOrden(conEmpresa, session.getEsquemaEmpresa(), folioOrden, session.getLenguaje());
						ConfigAdicionalesBean confBean = new ConfigAdicionalesBean();
						logger.info("Orden de Compra........."+visorForm.getFolioEmpresa());
						
						String servRecibido =  visorForm.getServicioRecibido();
						logger.info("servRecibido===>"+servRecibido);
						if ("0".equalsIgnoreCase(servRecibido)) {
							HashMap<String, String> mapaConf = confBean.obtenerConfiguraciones(conEmpresa, session.getEsquemaEmpresa());
							AccesoBean accesoBean = new AccesoBean();
						     EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
							
							String listaCorreosAdicionales = mapaConf.get("LISTA_CORREO_SOLPAGO"); //  confBean.obtenerConfiguracionesVariable(conEmpresa, session.getEsquemaEmpresa(), "LISTA_CORREO_SOLPAGO");
							String emailConf [] = listaCorreosAdicionales.split(";");
							String emailTO [] = { "N","N","N","N","N"};
							String emailCC [] = UtilsBD.obtenerCorreorProveedor(conEmpresa, session.getEsquemaEmpresa(), visorForm.getClaveProveedor(), "N", "N", "N","N");
							String asignarTO = visorForm.getAsignarA();
							EmpleadosBean empleadosBean = new EmpleadosBean();
							EmpleadosForm empleadoForm = empleadosBean.consultaEmpleadoXid(conEmpresa, rc.getEsquema(), asignarTO);
							
							String mensajeHTML = UtilsHTML.generaHTMLSolicitudPago(empresasForm.getNombreCorto().toUpperCase());
							logger.info("mensajeHTML : "+mensajeHTML );

							int contador = 0;
							if ("".equalsIgnoreCase(Utils.noNuloNormal(empleadoForm.getCorreo()))) {
								emailTO[contador++] = Utils.noNuloNormal(empleadoForm.getCorreo());
							}
							for (int x = 0; x < emailConf.length; x++) {
								emailTO[contador++] = emailConf[x];
							}
							
							 
						     EnviaCorreoGrid.enviarCorreo(null, mensajeHTML, false, emailTO, emailCC, "Receipt request for PO "+visorForm.getFolioEmpresa(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
						     
						    visorModel.setCodError("000");
							visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
								
						}else {
							
							visorModel.setCodError("001");
							visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
						}
						
						
					    JSONObject json = new JSONObject(visorModel);
						out.print(json);
			            out.flush();
			            out.close();
					}

			}catch(Exception e){
				Utils.imprimeLog("", e);
			}finally{
				try{
					if (conEmpresa != null){
						conEmpresa.close();
					}
					conEmpresa = null;
				}catch(Exception e){
					conEmpresa = null;
				}
			}
			return SUCCESS;
		}

		
		
		public String generarFactura(){
			HttpServletRequest request = ServletActionContext.getRequest();
			Connection con = null;
			ResultadoConexion rc = null;
			// String sbConsoler = "";
			HttpServletResponse response = ServletActionContext.getResponse();
			// String mensajeValidacion = "OK";
			ProveedoresForm provForm = null;
			VisorOrdenesModel visorModel = new VisorOrdenesModel();
			try {
				SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					PrintWriter out = response.getWriter();
					logger.info("folioEmpresa---->"+getFolioEmpresa());
						rc = getConnection(session.getEsquemaEmpresa());
					    con = rc.getCon();
					    VisorOrdenesBean visorBean = new VisorOrdenesBean();
					    VisorOrdenesForm visorConsulta = visorBean.consultarOrdenXfolioEmpresa(con, rc.getEsquema(), getFolioEmpresa());
					    
					    if ("A2".equalsIgnoreCase(visorConsulta.getEstatusOrden()) || "A5".equalsIgnoreCase(visorConsulta.getEstatusOrden())) {
					    	 	UsuariosBean usuarioBean = new UsuariosBean();
								UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
								int claveProveedor = 0;
								if (usuarioForm.getIdPerfil() == 4) {
									claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
								}
								
							    if (claveProveedor > 0) {
									ProveedoresBean provBean = new ProveedoresBean();
									provForm = provBean.consultarProveedor(con, rc.getEsquema(), claveProveedor);
								}
							    
							    // CargasProveedorUtils cargasUtils = new CargasProveedorUtils();
								// ProveedoresForm proveForm = cargasUtils.buscarInfoProveedor(con, session.getEsquemaEmpresa(), session.getClaveProveedor());
								
							    logger.info("SERIE AMERICANOS===>"+provForm.getAMERICANOS_SERIE());
								if ("".equalsIgnoreCase(provForm.getAMERICANOS_SERIE())) {
									// se obtiene bandera de tipo de validacion
									  ConfigAdicionalesBean confSistemaBean = new ConfigAdicionalesBean();
									  HashMap<String, String> mapaConf = confSistemaBean.obtenerConfiguraciones(con, session.getEsquemaEmpresa());
									// Termina
									  
									  String PREDEFINIR_VALOR_SERIE = Utils.noNulo(mapaConf.get("PREDEFINIR_VALOR_SERIE"));
									  if ("S".equalsIgnoreCase(PREDEFINIR_VALOR_SERIE)) {
										  	String VALOR_SERIE_AMERICANAS = Utils.noNulo(mapaConf.get("VALOR_SERIE_AMERICANAS"));
										  	provForm.setAMERICANOS_SERIE(VALOR_SERIE_AMERICANAS);	  
									  }
								}
								
								if ("".equalsIgnoreCase(provForm.getAMERICANOS_SERIE())) {
									 // mensajeValidacion = "NO_SERIE";
									    visorModel.setCodError("001");
										visorModel.setMensajeError("No es posible generar factura, no se tiene configurado ningun valor predifinido.");
										
							         // return null;
								}else {
									
									String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
									  //String rutaRepositorios = rutaFinal + "/REPOSITORIOS/" + File.separator + session.getEsquemaEmpresa()+ File.separator +  "LOGOS_PROVEEDORES" + File.separator + session.getClaveProveedor() + File.separator +   getFileLogoFileName();
									 String rutaLogo = rutaFinal + session.getEsquemaEmpresa()+ File.separator +  "LOGOS_PROVEEDORES" + File.separator + claveProveedor + File.separator +   "LOGO.png";
									 logger.info("rutaLogo...."+rutaLogo);
									 boolean tieneLogo = true;
									 File fileImagen = new File(rutaLogo);
									 if (!fileImagen.exists()) {
										 logger.info("El logo no existe, se agrega una por default.");
										 rutaLogo = rutaFinal + session.getEsquemaEmpresa() + File.separator +  "LAY_OUT" + File.separator +   "NO_IMAGEN.jpg";
										 tieneLogo = false;
									 }
									 logger.info("Ruta del Logo..."+rutaLogo);
									 
									 
									 String rutaFacturaPDF = UtilsPATH.RUTA_CORREO + "PLANTILLA_FACTURA.pdf";
									 GenerarPDFAmericano generaPDF = new GenerarPDFAmericano();
										 
									GeneraDatosFacturaBean generaBean = new GeneraDatosFacturaBean();
									String listaOrdenes [] = { String.valueOf(getFolioEmpresa())};
									GeneraDatosFacturaForm generaForm = generaBean.calculaDatos(con, session.getEsquemaEmpresa(), claveProveedor, listaOrdenes, getUsuario(request), rutaLogo);
									boolean isGenero = generaPDF.generarFactura(generaForm, rutaFacturaPDF, tieneLogo);
									
									
									VisorOrdenesForm ordenesAmeForm = new VisorOrdenesForm();
									
									String[][] datosFechaFactura = generaForm.getDatosFechaFactura();
									String folioFactura = null;
									if (datosFechaFactura != null && datosFechaFactura.length > 1) {
										String [] numeroFactura = datosFechaFactura[0];
										folioFactura = numeroFactura[1];
									}
									logger.info("folioFactura Generada---->"+folioFactura);
									logger.info("isGenero Factura---->"+isGenero);
									if (folioFactura != null && isGenero) {
										
										VisorOrdenesForm visorForm = null;
										String rutaRepositorio = rutaFinal +  session.getEsquemaEmpresa()+ File.separator + "/PROVEEDORES/" +File.separator + claveProveedor + File.separator;
										SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
										Date fechaHoy = new Date();
										
										String tipoFactura = "";
										
										if (listaOrdenes.length == 1) {
											tipoFactura = listaOrdenes[0];
										}else {
											tipoFactura = "MULTIPLE";
										}
										
										for (int x = 0; x < listaOrdenes.length; x++) {
											visorForm = generaBean.consultaDatosOrdenes(con, session.getEsquemaEmpresa(), Long.parseLong( listaOrdenes[x] ));
											
											// ordenesAmeForm.setNombreTXT("&="+provForm.getRfc()+"-"+tipoFactura+"-" + folioFactura.trim() +".txt");
											ordenesAmeForm.setNombrePDF("&="+provForm.getRfc()+"-"+tipoFactura+"-" + folioFactura.trim() +".pdf");
											ordenesAmeForm.setTotal(visorForm.getMonto());
											ordenesAmeForm.setSerieFolio(folioFactura.trim());
											ValidacionesAmericano valFacturaBean = new ValidacionesAmericano();
											int totReg = valFacturaBean.actualizaOrden(session.getEsquemaEmpresa(), visorForm.getFolioEmpresa(), ordenesAmeForm, provForm.getEmail(), formatDate.format(fechaHoy));
											logger.info("Guardando la factura...."+visorForm.getFolioEmpresa() + " : "+ folioFactura.trim());
											
										}
										
										//String nombreFinalTXT = null;
										String nombreFinalPDF = null;
										//nombreFinalTXT = rutaRepositorio +  proveForm.getRfc() + "-"+ tipoFactura +"-" + folioFactura.trim() +".txt";
										nombreFinalPDF = rutaRepositorio  + provForm.getRfc() + "-"+ tipoFactura +"-" + folioFactura.trim()+".pdf";
										logger.info("************* nombreFinalPDF **********************"+nombreFinalPDF);
										//logger.info("************* nombreFinaltxt **********************"+nombreFinalTXT);
										
										logger.info("Ruta a depositar : "+rutaRepositorio);
										logger.info("Eliminando los registros :");
										// CargasProveedorUtils cargasPro = new CargasProveedorUtils();
										//cargasPro.moverArchivo(nombreFinalPDF, filePDF);
										
										File filePDF = new File(rutaFacturaPDF);
										File fileDestPDF = new File(nombreFinalPDF);
										UtilsFile.moveFileDirectory(filePDF,fileDestPDF, true, false, true);
										
										visorModel.setCodError("000");
										visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
								          
									}
								}
					    }else {
					    	visorModel.setCodError("001");
							visorModel.setMensajeError("Estatus de la Orden es incorrecto para generar la factura.");
					    }
						
						JSONObject json = new JSONObject(visorModel);
						out.print(json);
			            out.flush();
			            out.close();
			            
				}
				
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally {
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
		
		
		public String cargarLogo(){
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			ResultadoConexion rc = null;
			Connection con = null;
			VisorOrdenesModel visorModel = new VisorOrdenesModel();
			try {
				SiarexSession session = ObtenerSession.getSession(request);
				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}else{
					PrintWriter out = response.getWriter();
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					
					UsuariosBean usuarioBean = new UsuariosBean();
					UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
					int claveProveedor = 0;
					if (usuarioForm.getIdPerfil() == 4) {
						claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
					}
					 String rutaRepositorios = UtilsPATH.REPOSITORIO_DOCUMENTOS + File.separator + session.getEsquemaEmpresa()+ File.separator +  "LOGOS_PROVEEDORES" + File.separator + claveProveedor + File.separator +   "LOGO.png";
					 logger.info("El logo se ha cargado al directorio..."+rutaRepositorios);
					 
					 Part filePart = request.getPart("fileLogo");
			         File fileLogo = UtilsFile.getFileFromPart(filePart);
			            	
			         CompressionImagen.comprimirArchivo(fileLogo.getAbsolutePath(), rutaRepositorios);
					  
					 visorModel.setCodError("000");
					 visorModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					 JSONObject json = new JSONObject(visorModel);
					 out.print(json);
			         out.flush();
			         out.close(); 
				}
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally {
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
		
		 public String exportFacturasDetails() {
			 Connection con = null;
			ResultadoConexion rc = null;
			VisorOrdenesBean visorBean = new VisorOrdenesBean();
			try{
				HttpServletRequest request = ServletActionContext.getRequest();
			    SiarexSession session = ObtenerSession.getSession(request);
			    SXSSFWorkbook  myWorkBook = new SXSSFWorkbook(100); // Keep 100 rows in memory
			    SXSSFSheet mySheet = myWorkBook.createSheet("Detalle Ordenes de Compra");
			    rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				UsuariosBean usuarioBean = new UsuariosBean();
				UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				int claveProveedor = 0;
				boolean isProveedor = false;
				if (usuarioForm.getIdPerfil() == 4) {
					claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
					isProveedor = true;
				}else {
					claveProveedor = getClaveProveedor();
				}
				boolean isSoloConsulta = false;
				if (usuarioForm.getIdPerfil() == 5){
					isSoloConsulta = true;
				}
				
				 String fechaInicial = Utils.noNulo(getUltmovV1());
			  		String fechaFinal = Utils.noNulo(getUltmovV2());
			  		String dateOperator = null;
			  		if ("".equalsIgnoreCase(fechaInicial)) {
			  			 fechaFinal = UtilsFechas.getFechayyyyMMdd();
			  			 fechaInicial= UtilsFechas.restarDiasFecha(fechaFinal, 365);
			  			 dateOperator = "bt";
			  		}else {
			  			dateOperator = getUltmovOperator();
			  		}
			  		 dateOperator = "bt";
			    /*ArrayList<VisorOrdenesForm> listaDetalle  = visorBean.detalleOrdenes(con, rc.getEsquema(), session.getLenguaje(), getTipoMoneda(), getEstatusOrden(), claveProveedor, 
					 	getFolioEmpresa(), getRfc(), getRazonSocial(), getUuid(), getSerieFolio(), fechaInicial, fechaFinal, isProveedor, isSoloConsulta, 0, 0, true);
			     */
		  		
		  		int pageSize = 50; // o el que uses
		  	//// Helper: normaliza selects "ALL" -> ""
		  		java.util.function.Function<String,String> normSel = v -> {
		  		    String x = Utils.noNulo(v);
		  		    return "ALL".equalsIgnoreCase(x) ? "" : x;
		  		};

		  		ArrayList<VisorOrdenesForm> listaDetalle = visorBean.detalleOrdenes(
		  			    con, rc.getEsquema(), session.getLenguaje(),
		  			    // flags y paginado
		  			    isProveedor, isSoloConsulta, claveProveedor,
		  			    getStart(), pageSize, /*isExcel*/ false,

		  			    // ====== TEXTO / SELECTS (valor, operador) ======
		  			    Utils.noNuloNormal(getRazonSocial()), Utils.noNulo(getRsOperator()),
		  			    // 3 Orden de Compra (numérico)
		  			    Utils.noNulo(getOcOperator()), Utils.noNulo(getOcV1()), Utils.noNulo(getOcV2()),
		  			    // resto
		  			    Utils.noNuloNormal(getDescripcion()), Utils.noNulo(getDescOperator()),
		  			    normSel.apply(getTipoMoneda()),       Utils.noNulo(getMonedaOperator()),
		  			    normSel.apply(getServicioRecibo()),   Utils.noNulo(getReciboOperator()),
		  			    normSel.apply(getEstatusPago()),      Utils.noNulo(getEstatusPagoOperator()),
		  			    Utils.noNuloNormal(getSerieFolio()),  Utils.noNulo(getSerieFolioOperator()),
		  			    Utils.noNuloNormal(getAsignarA()),    Utils.noNulo(getAsignarOperator()),
		  			    Utils.noNuloNormal(getEstadoCfdi()),  Utils.noNulo(getEstadoCfdiOperator()),
		  			    normSel.apply(getEstatusSat()),       Utils.noNulo(getEstatusSatOperator()),
		  			    normSel.apply(getUsoCfdi()),          Utils.noNulo(getUsoCfdiOperator()),
		  			    Utils.noNuloNormal(getCps()),         Utils.noNulo(getCpsOperator()),

		  			    // ====== NUMÉRICOS (operador, v1, v2) ======
		  			    Utils.noNulo(getMontoOperator()),     Utils.noNulo(getMontoV1()),     Utils.noNulo(getMontoV2()),
		  			    Utils.noNulo(getTotalOperator()),     Utils.noNulo(getTotalV1()),     Utils.noNulo(getTotalV2()),
		  			    Utils.noNulo(getSubtotalOperator()),  Utils.noNulo(getSubtotalV1()),  Utils.noNulo(getSubtotalV2()),
		  			    Utils.noNulo(getIvaOperator()),       Utils.noNulo(getIvaV1()),       Utils.noNulo(getIvaV2()),
		  			    Utils.noNulo(getIvaretOperator()),    Utils.noNulo(getIvaretV1()),    Utils.noNulo(getIvaretV2()),
		  			    Utils.noNulo(getIsrretOperator()),    Utils.noNulo(getIsrretV1()),    Utils.noNulo(getIsrretV2()),
		  			    Utils.noNulo(getImplocOperator()),    Utils.noNulo(getImplocV1()),    Utils.noNulo(getImplocV2()),
		  			    Utils.noNulo(getTotalncOperator()),   Utils.noNulo(getTotalncV1()),   Utils.noNulo(getTotalncV2()),
		  			    Utils.noNulo(getPagotOperator()),     Utils.noNulo(getPagotV1()),     Utils.noNulo(getPagotV2()),
		  			    Utils.noNulo(getIvaretncOperator()),  Utils.noNulo(getIvaretncV1()),  Utils.noNulo(getIvaretncV2()),

		  			    // ====== FECHAS (operador, v1, v2) ======
		  			    Utils.noNulo(getFechapagoOperator()), Utils.noNulo(getFechapagoV1()), Utils.noNulo(getFechapagoV2()),
		  			  dateOperator,    fechaInicial,    fechaFinal
		  			);


				  VisorOrdenesExcel visorExcel = new VisorOrdenesExcel();
				  visorExcel.exportarOrdenes(mySheet, listaDetalle, myWorkBook, session.getLenguaje());
			  		
				  try {
					  AccesoBean accesoBean = new AccesoBean();
					  EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
					  reportFile = empresasForm.getRfc() + "_DETALLE_ORDENES.xlsx";
					  
					  ByteArrayOutputStream boas = new ByteArrayOutputStream();
					  myWorkBook.write(boas);
					  setInputStream(new ByteArrayInputStream(boas.toByteArray()));
					  myWorkBook.close();
				  }
				  catch (IOException e) {
					  Utils.imprimeLog("exportExcel(IOE): ", e);
				  }
				
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally{
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
		
		/*
		 public String exportFacturasDetails() {
				XSSFWorkbook myWorkBook = new XSSFWorkbook();
			    XSSFSheet mySheet = myWorkBook.createSheet("Detalle Ordenes de Compra");
				Connection con = null;
				ResultadoConexion rc = null;
				VisorOrdenesBean visorBean = new VisorOrdenesBean();
				try{
			      HttpServletRequest request = ServletActionContext.getRequest();
				  SiarexSession session = ObtenerSession.getSession(request);

		  			  rc = getConnection(session.getEsquemaEmpresa());
					  con = rc.getCon();

					    UsuariosBean usuarioBean = new UsuariosBean();
						UsuariosForm usuarioForm = usuarioBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
						int claveProveedor = 0;
						boolean isProveedor = false;
						if (usuarioForm.getIdPerfil() == 4) {
							claveProveedor = Integer.parseInt( usuarioForm.getIdEmpleado().substring(5));
							isProveedor = true;
						}else {
							claveProveedor = getClaveProveedor();
						}
						boolean isSoloConsulta = false;
						if (usuarioForm.getIdPerfil() == 5){
							isSoloConsulta = true;
						}
						
						String fechaInicial = Utils.noNulo(getFechaInicial());
				  		String fechaFinal = Utils.noNulo(getFechaFinal());
					  
					  ArrayList<VisorOrdenesForm> listaDetalle  = visorBean.detalleOrdenes(con, rc.getEsquema(), session.getLenguaje(), getTipoMoneda(), getEstatusOrden(), claveProveedor, 
							 	getFolioEmpresa(), getRfc(), getRazonSocial(), getUuid(), getSerieFolio(), fechaInicial, fechaFinal, isProveedor, isSoloConsulta, 0, 0, true);
					  
					  VisorOrdenesExcel visorExcel = new VisorOrdenesExcel();
					  visorExcel.exportarOrdenes(mySheet, listaDetalle, myWorkBook, session.getLenguaje());
				  		
					  try {
						  AccesoBean accesoBean = new AccesoBean();
						  EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
							
						  reportFile = empresasForm.getRfc() + "_DETALLE_ORDENES.xlsx";
						  
						  ByteArrayOutputStream boas = new ByteArrayOutputStream();
						  myWorkBook.write(boas);
						  setInputStream(new ByteArrayInputStream(boas.toByteArray()));
						  myWorkBook.close();
					  }
					  catch (IOException e) {
						  Utils.imprimeLog("exportExcel(IOE): ", e);
					  }
			  }
			  catch (Exception e) {
			    Utils.imprimeLog("exportExcel(e): ", e);
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
*/

		 
		 public String consultarFechaMinima() throws Exception {
				Connection con = null;
				ResultadoConexion rc = null;
				VisorOrdenesBean visorBean = new VisorOrdenesBean();
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				try{
					SiarexSession session = ObtenerSession.getSession(request);
					if ("".equals(session.getEsquemaEmpresa())){
						return Action.LOGIN;
					}else{
				    	response.setContentType("text/html; charset=UTF-8");
						response.setCharacterEncoding("UTF-8");

						//logger.info("**** CONSULTANDO ORDENES ***************");
						PrintWriter out = response.getWriter();
						rc = getConnection(session.getEsquemaEmpresa());
						con = rc.getCon();
						
						BovedaNominaForm bovedaForm = new BovedaNominaForm();
						String fechaMinima = visorBean.consultarFechaMinima(con, rc.getEsquema());
						String fechaFinal = UtilsFechas.getFechayyyyMMdd();
						
						bovedaForm.setFechaInicial(fechaMinima.substring(0, 10));
						bovedaForm.setFechaFinal(fechaFinal);
						
						
						JSONObject json = new JSONObject(bovedaForm);
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
			
		 
		public InputStream getInputStream() {
			return inputStream;
		}


		public String getReportFile() {
			return reportFile;
		}


		public void setInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}


		public void setReportFile(String reportFile) {
			this.reportFile = reportFile;
		}
		 
		 
}
