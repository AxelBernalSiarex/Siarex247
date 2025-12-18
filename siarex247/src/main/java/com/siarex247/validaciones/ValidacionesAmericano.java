package com.siarex247.validaciones;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.configSistema.CierreAnnio.CierreAnnioBean;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.utils.MensajesSIAREX;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesBean;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesQuerys;

public class ValidacionesAmericano {

	public static final Logger logger = Logger.getLogger("siarex247");
	public static MensajesSIAREX mensajeSIAREX = null;
	public static String MENSAJE_VALIDACIONES = null;
	public static String SUBJECT_VALIDACIONES = null;

	
	
	public String [] iniciarProceso(String esquemaEmpresa, long folioEmpresa, File fileTXT, File filePDF, String idLenguaje, int idPerfil, String usuarioAdjunto) {
		String respuestaValidacion [] = {"", "", ""}; // MENSAJE, SUBJECT Y RESULTADO VALIDACION (ERROR o EXITO)
		try {
			
			mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			VisorOrdenesForm visorForm = existeOrden(esquemaEmpresa, folioEmpresa);
			ProveedoresForm provForm = obtenerProveedor(esquemaEmpresa, visorForm.getClaveProveedor());
			
			if (visorForm.getFolioOrden() == 0) { // existe la orden de compra
				respuestaValidacion[0] = Utils.regresaCaracteresNormales(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE6, "<< FOLIO_FACTURA >>", String.valueOf(folioEmpresa)));
				respuestaValidacion[1] = Utils.regresaCaracteresNormales(mensajeSIAREX.SUBJECT2);
				respuestaValidacion[2] = "ERROR";
			}else if (provForm.getClaveRegistro() == 0) {  // existe el proveedor en base de datos
				respuestaValidacion[0] = "El Proveedor no existe en nuestra base de datos.";
				respuestaValidacion[1] = "Siarex - Proveedor No existe";
				respuestaValidacion[2] = "ERROR";
			}else if (!"OK".equalsIgnoreCase(iniciarValidaciones(esquemaEmpresa, fileTXT, filePDF, visorForm, provForm, idPerfil, usuarioAdjunto))) {
				respuestaValidacion[0] = Utils.regresaCaracteresNormales(MENSAJE_VALIDACIONES);
				respuestaValidacion[1] = "Siarex - " + SUBJECT_VALIDACIONES;
				respuestaValidacion[2] = "ERROR";
			}else {
				
				respuestaValidacion[0]  = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE26, "<< FOLIO_FACTURA >>", String.valueOf(folioEmpresa));
				respuestaValidacion[1] = "Siarex - " +  mensajeSIAREX.SUBJECT1;
				respuestaValidacion[2] = "EXITO";
			}
			
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return respuestaValidacion;
	}
	
	
	private VisorOrdenesForm existeOrden(String esquemaEmpresa, long folioEmpresa) {
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		VisorOrdenesBean visorBean = new VisorOrdenesBean();
		VisorOrdenesForm visorForm = null;
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			visorForm = visorBean.consultarOrdenXfolioEmpresa(con, rc.getEsquema(), folioEmpresa);
			
			
		}catch(Exception e) {
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
		return visorForm;
	}
	
	

	private ProveedoresForm obtenerProveedor(String esquemaEmpresa, int claveProveedor) {
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		ProveedoresBean provBean = new ProveedoresBean();
		ProveedoresForm provForm = null;
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			provForm = provBean.consultarProveedor(con, rc.getEsquema(), claveProveedor);
			
			
		}catch(Exception e) {
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
		return provForm;
	}
	
	
	private String iniciarValidaciones(String esquemaEmpresa, File fileTXT, File filePDF, VisorOrdenesForm visorForm, ProveedoresForm provForm, int idPerfil, String usuarioAdjunto) {
		String mensajeValidaciones = "OK";
		//String respuestaValidacion [] = {"NG", ""};
		String infoOrden = null;
		try {
			infoOrden = UtilsFile.leeArchivo(fileTXT.getAbsolutePath());
			String cadOrden [] = infoOrden.replace("|", "&").split("&");
			if (cadOrden.length > 0 ){ // si contiene informacion
				
				String numeroFacturaTXT = cadOrden[0];
				String fechaFacturaTXT = cadOrden[1];
				String fechaFactura = "";
				double montoTXT = Double.parseDouble( cadOrden[2]);
				String tipoMonedaTXT = cadOrden[3];
				try{
					// 01/05/2017 // 01/06/2023
					logger.info("************* fechaFacturaTXT ******************************"+fechaFacturaTXT);
					fechaFactura = new StringBuffer(fechaFacturaTXT.substring(6,10)).append("-")
									.append(fechaFacturaTXT.substring(0,2)).append("-")
									.append(fechaFacturaTXT.substring(3,5)).toString();
				}catch(Exception e){
					fechaFactura = "NO_VALIDA";
				}
				
				boolean isExisteSerie = existeFactura(esquemaEmpresa, numeroFacturaTXT);
				
				logger.info("************* montoTXT ******************************"+montoTXT);
				logger.info("************* numeroFacturaTXT **********************"+numeroFacturaTXT);
				logger.info("************* fechaFactura **************************"+fechaFactura);
				logger.info("************* tipoMonedaTXT **************************"+tipoMonedaTXT);
				logger.info("************* isExisteSerie **************************"+isExisteSerie);
				
				
				String mensajeFecha = validarFechaRecepcion(esquemaEmpresa, idPerfil, fechaFactura, numeroFacturaTXT); //  (esquemaEmpresa, fechaFactura, numeroFacturaTXT, "F");
				int numeroValidacion = 0;
				if (montoTXT != Utils.noNuloDouble(visorForm.getMonto())){
					logger.info("********************** El monto de la factura es incorrecto! **********************");
					//mensajeValidacion = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a que no coincide el monto de su factura contra la orden de compra.";
					logger.info("********************** montoTXT **********************"+montoTXT);
					logger.info("**********************visorForm.getMonto() **********************"+visorForm.getMonto());
					
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE22, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidaciones  = "NO_EXITOSO";
					numeroValidacion = 1;
					
					
				}else if (!tipoMonedaTXT.equalsIgnoreCase(visorForm.getTipoMoneda())){
					logger.info("********************** Tipo de Moneda incorrecto! **********************");
					
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE23, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidaciones  = "NO_EXITOSO";
					numeroValidacion = 2;
					
					
				}else if (!mensajeFecha.equals("OK") || fechaFactura.equals("NO_VALIDA") ){
					if (fechaFactura.equals("NO_VALIDA") || mensajeFecha.equals("NG")){
						logger.info("********************** Fecha de Factura incorrecto! **********************");
						
						MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE24, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidaciones  = "NO_EXITOSO";
						numeroValidacion = 3;
						
					}else{
						logger.info("********************** La factura ya no se puede pagar este anio! **********************");
						
						MENSAJE_VALIDACIONES = mensajeFecha;
						SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
						mensajeValidaciones  = "NO_EXITOSO";
						numeroValidacion = 4;
						
						
					}
				}else if (!"A2".equalsIgnoreCase(visorForm.getEstatusOrden()) && 
					  	    !"A5".equalsIgnoreCase(visorForm.getEstatusOrden())){
					
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE25, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));;
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidaciones  = "NO_EXITOSO";
					numeroValidacion = 5;
					
					
					
				}else if (isExisteSerie) {
					MENSAJE_VALIDACIONES = Utils.getMensajeValidacion(Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE33, "<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())), "<< SERIE_FOLIO >>", numeroFacturaTXT);
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
					mensajeValidaciones  = "NO_EXITOSO";
					numeroValidacion = 6;
					
					
				}else {
					
					String rutaRepositorio = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquemaEmpresa + File.separator + "PROVEEDORES" + File.separator + provForm.getClaveRegistro() + File.separator ;
					//visorForm.setNombreTXT("&="+provForm.getRfc()+"-"+visorForm.getFolioEmpresa()+"-" + numeroFacturaTXT +".txt");
					visorForm.setNombreTXT(null);
					visorForm.setNombrePDF("&="+provForm.getRfc()+"-"+visorForm.getFolioEmpresa()+"-" + numeroFacturaTXT +".pdf");
					visorForm.setTotal(String.valueOf(montoTXT));
					visorForm.setSerieFolio(numeroFacturaTXT);
					String nombreFinalTXT = null;
					String nombreFinalPDF = null;
					nombreFinalTXT = rutaRepositorio +  provForm.getRfc()+"-"+ visorForm.getFolioEmpresa() +"-" + numeroFacturaTXT +".txt";
					nombreFinalPDF = rutaRepositorio +  provForm.getRfc()+"-"+ visorForm.getFolioEmpresa() +"-" + numeroFacturaTXT+".pdf";
					logger.info("************* nombreFinalPDF **********************"+nombreFinalPDF);
					logger.info("************* nombreFinaltxt **********************"+nombreFinalTXT);
					logger.info("************* fechaFacturaFormato **********************"+fechaFactura);
					logger.info("Ruta a depositar : "+rutaRepositorio);
					int totReg = actualizaOrden(esquemaEmpresa, visorForm.getFolioEmpresa(), visorForm, usuarioAdjunto, fechaFactura);
					if (totReg == 1){
						logger.info("Eliminando los registros :");
						
						File fileDestTXT = new File(nombreFinalTXT);
						File fileDestPDF = new File(nombreFinalPDF);
						
						UtilsFile.moveFileDirectory(fileTXT,fileDestTXT, true, false, true);
						UtilsFile.moveFileDirectory(filePDF,fileDestPDF, true, false, true);
						
						mensajeValidaciones  = "OK";
						
					}
					
				}
				
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return mensajeValidaciones;
	}
	
	
	private boolean existeFactura(String esquemaEmpresa, String serieFolio){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isExiste = false;
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			stmt = con.prepareStatement(VisorOrdenesQuerys.getExisteSerieFolio(rc.getEsquema()));
			stmt.setString(1, serieFolio);
			rs = stmt.executeQuery();
			if (rs.next()){
				isExiste = true;
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				if (con != null){
					con.close();
				}
				con = null;
				
			}catch(Exception e){
				stmt = null;
				con = null;
			}
		}
		return isExiste;
	}

	
	
	private String validarFechaRecepcion(String esquemaEmpresa, int idPerfil, String fechaFact, String factura) {
		String mensajeFechas = "OK";
		CierreAnnioBean fechaCierre = new CierreAnnioBean();
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			
			// fechaFactura  01/06/2023
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
			Date fecha = new Date();
			String fechaActualSimple = "";
			fechaActualSimple = formatDate.format(fecha);
			int anio = Integer.parseInt(fechaFact.substring(0, 4));
			logger.info("obtenerFechas anio---->"+anio);
			String [] fechasRecepcion = fechaCierre.obtenerFechas(con, rc.getEsquema(), anio, "F");
			logger.info("Fecha Factura---->"+fechaFact);
			logger.info("Fecha Apartir---->"+fechasRecepcion[1]);
			logger.info("Fecha Hasta---->"+fechasRecepcion[3]);
			logger.info("Fecha Actual ---->"+fechaActualSimple);

			
			
			long fechaHasta = 0;
			long fechaApartir = 0;
			long fechaFactura = 0;
			long fechaActual = 0;
			
			StringBuffer sbFecha = new StringBuffer();
			if (!fechasRecepcion[1].equals("")){
				fechaApartir = Long.parseLong(Utils.replaceCaracteresEspeciales(sbFecha.append(fechasRecepcion[1].substring(0,4))
						 .append(fechasRecepcion[1].substring(5,7))
						 .append(fechasRecepcion[1].substring(8,10)).toString()));
			}
			
			sbFecha.setLength(0);
			if (!fechasRecepcion[3].equals("")){
				fechaHasta = Long.parseLong(Utils.replaceCaracteresEspeciales(sbFecha.append(fechasRecepcion[3].substring(0,4))
						 .append(fechasRecepcion[3].substring(5,7))
						 .append(fechasRecepcion[3].substring(8,10)).toString()));
			}
			
			sbFecha.setLength(0);
			if (!fechaFact.equals("")){
				fechaFactura = Long.parseLong(Utils.replaceCaracteresEspeciales(sbFecha.append(fechaFact.substring(0,4))
						 .append(fechaFact.substring(5,7))
						 .append(fechaFact.substring(8,10)).toString()));
			}
			
			sbFecha.setLength(0);
			
			if (!fechaActualSimple.equals("")){
				fechaActual = Long.parseLong(fechaActualSimple);
			}

			logger.info("Fecha FacturaLong---->"+fechaFactura);
			logger.info("Fecha ApartirLong---->"+fechaApartir);
			logger.info("Fecha HastaLong---->"+fechaHasta);
			logger.info("Fecha ActualLong ---->"+fechaActual);

			
			boolean cumpleHasta = false;
			boolean cumpleApartir = false;
			boolean bandNoValidar = true;
			
			// fechaFactura = 20160103
			// fechaHasta   = 20161218
			
			// fechaApartir = 20160105
			// fechaActual  = 20160104
			
			// true, si cumplio no se manda mensaje
			// false, no cumplio no se manda mensaje
			
			if (fechaHasta > 0 && fechaHasta >= fechaFactura){
				if (fechaHasta >= fechaActual){
					cumpleHasta = true;
				}else{
					bandNoValidar = false;
				}
			}else{
				bandNoValidar = false;
			}
			
			if (bandNoValidar){ // significa que cumplio la fecha hasta y se valida las de apartir
				if (fechaApartir > 0 && fechaApartir <= fechaFactura){
					if (fechaApartir <= fechaActual){
						cumpleApartir = true;
					}
				}
			}
			
			logger.info("cumpleHasta(2) ---->"+cumpleHasta);
			logger.info("cumpleApartir ---->"+cumpleApartir);
			logger.info("cumpleHasta---->"+cumpleHasta);
			logger.info("fechaHasta---->"+fechaHasta);
			logger.info("cumpleApartir--->"+cumpleApartir);
			logger.info("fechaApartir--->"+fechaApartir);
			
			String VALIDAR_CIERRE_ADMIN = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "VALIDAR_CIERRE_ADMIN");
			  if ("".equalsIgnoreCase(VALIDAR_CIERRE_ADMIN)) {
				  VALIDAR_CIERRE_ADMIN = "S";
			  }
			  
			  boolean bandValidaCierre = true;
			  if (idPerfil == 1 && "N".equalsIgnoreCase(VALIDAR_CIERRE_ADMIN)) {
				  bandValidaCierre = false;  
			  }
			  
			  if (!cumpleHasta && fechaHasta > 0 && bandValidaCierre){
					mensajeFechas = fechasRecepcion[4].replace("<<factura>>", factura).replace("<<fecha_hasta>>", fechasRecepcion[6]);
					MENSAJE_VALIDACIONES = mensajeFechas;
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
			 }else if (!cumpleApartir && fechaApartir > 0 && bandValidaCierre){
					mensajeFechas = fechasRecepcion[2].replace("<<factura>>", factura).replace("<<fecha_apartir>>", fechasRecepcion[5]);
					MENSAJE_VALIDACIONES = mensajeFechas;
					SUBJECT_VALIDACIONES = mensajeSIAREX.SUBJECT2;
			 }
			  
			  
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
			mensajeFechas = "NG";
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
		return mensajeFechas;
	}
	
	
	public int actualizaOrden(String esquemaEmpresa, long folioFactura, VisorOrdenesForm visorForm,  String usuarioHTTP, String fechaFactura){
		PreparedStatement stmt = null;
		int cantidadFactura = 0;
		
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		Connection con = null;
		
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			stmt = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrden(rc.getEsquema()));
			
			
			stmt.setString(1, "A9");
			stmt.setDouble(2, Utils.noNuloDouble(visorForm.getTotal()));
			stmt.setString(3, visorForm.getSerieFolio());
			stmt.setString(4, visorForm.getNombreTXT());
			stmt.setString(5, visorForm.getNombrePDF());
			stmt.setString(6, usuarioHTTP);
			stmt.setString(7, fechaFactura);
			stmt.setLong(8, folioFactura);
			stmt.executeUpdate();
			cantidadFactura = 1;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				if (con != null){
					con.close();
				}
				con = null;
			}catch(Exception e){
				stmt = null;
			}
		}
		return cantidadFactura;
	}
}
