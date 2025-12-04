package com.siarex247.cumplimientoFiscal.ConciliacionBoveda;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.configSistema.AlertaConciliacion.AlertaConciliacionBean;
import com.siarex247.configSistema.AlertaConciliacion.AlertaConciliacionForm;
import com.siarex247.cumplimientoFiscal.DescargaSAT.ProcesoDescargaSATQuerys;
import com.siarex247.cumplimientoFiscal.ListaNegra.ProcesoArchivosQuerys;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsColor;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;

public class AlertaConciliacionProceso {

	public static final Logger logger = Logger.getLogger("siarex247");

	
	public void monitoreaAlertas(){
		try{
				AccesoBean accesoBean = new AccesoBean();
				ConexionDB connPool = new ConexionDB();
				Connection con = null;
				ResultadoConexion rc = null;
				try{
					rc = connPool.getConnectionSiarex();
					con = rc.getCon();
					ArrayList<EmpresasForm> listaEmpresas = accesoBean.listaEmpresas(con, rc.getEsquema()); //  detalleEmpresas(con, "siarex");
					EmpresasForm empresasForm = null;
					
					try{
						if (con != null){
							con.close();
						}
					}catch(Exception e){
						con = null;
					}
					
					for (int y = 0; y < listaEmpresas.size(); y++){
						empresasForm = listaEmpresas.get(y);
						// logger.info("Buscando Correos de la empresa : "+empresasForm.getEmailDominio());
						if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
							try {
								// logger.info("Validacion XML de la empresa====>"+empresasForm.getEsquema());
								if ("toyota".equalsIgnoreCase(empresasForm.getEsquema()) || "tmmbcservicios".equalsIgnoreCase(empresasForm.getEsquema())) {
									
								}else {
									iniciaProcesoVincular(empresasForm);	
								}
								
							} catch (Exception e) {
								Utils.imprimeLog("", e);
							}
						}
					}
				}catch(Exception e){
					Utils.imprimeLog("monitoreaCorreo ", e);
				}finally{
					try{
						if (con != null){
							con.close();
						}
					}catch(Exception e){
						con = null;
					}
				}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
	

 public void iniciaProcesoVincular(EmpresasForm empresasForm) {
	 ConexionDB connPool = null;
     ResultadoConexion rcEmpresa = null;
     Connection conEmpresa = null;
     AlertaConciliacionBean confOutBean = new AlertaConciliacionBean();
     boolean ejecutarProceso = false;
     try{
    	 connPool= new ConexionDB();
    	 rcEmpresa = connPool.getConnection(empresasForm.getEsquema());
    	 conEmpresa = rcEmpresa.getCon();
    	 
    	 AlertaConciliacionForm configForm = confOutBean.buscarConfProceso(conEmpresa, rcEmpresa.getEsquema(), "PRO01");
    	 
    	 String fechaProceso = getFechaHoy(conEmpresa, rcEmpresa.getEsquema());  
    	 int mes = Integer.parseInt(fechaProceso.substring(5, 7));
		 int year = Integer.parseInt(fechaProceso.substring(0, 4));
		 int diaMes = Integer.parseInt(fechaProceso.substring(8, 10));
		 int ultimoDia = UtilsFechas.obtenerUltimoDiaMes(year, mes);
		 
		// logger.info("Esta activo===>"+configForm.getActivar());
    	 if ("S".equalsIgnoreCase(configForm.getActivar())) { // si esta activo, se ejecuta el proceso
    		// logger.info("Dia de ejecucion===>"+configForm.getDiaEjecucion());
    		 if ("001".equalsIgnoreCase(configForm.getDiaEjecucion())) {
					ejecutarProceso = true;
				}else if ("002".equalsIgnoreCase(configForm.getDiaEjecucion())) { // semanal
					if ("DOM".equalsIgnoreCase(obtenerdiaSemana())) {
						ejecutarProceso = true;	
					}
					
				}else if ("003".equalsIgnoreCase(configForm.getDiaEjecucion())) { // quincenal
					if (diaMes == 15) {
						ejecutarProceso = true;	
					}
				}else if ("004".equalsIgnoreCase(configForm.getDiaEjecucion())) { // mensual
					if (diaMes == ultimoDia) {
						ejecutarProceso = true;
					}
				}else if ("005".equalsIgnoreCase(configForm.getDiaEjecucion())) { // bimestral
					if (UtilsFechas.validaBimestre(fechaProceso)) {
						ejecutarProceso = true;
					}
				}else if ("006".equalsIgnoreCase(configForm.getDiaEjecucion())) { // trimestral
					if (UtilsFechas.validaTrimestre(fechaProceso)) {
						ejecutarProceso = true;
					}
				}else if ("007".equalsIgnoreCase(configForm.getDiaEjecucion())) { // annual
					if (UtilsFechas.validaAnnual(fechaProceso)) {
						ejecutarProceso = true;
					}
				}
    	 }
    	 
    	 if (ejecutarProceso) {
    		 boolean isEjecuto = isEjecuto(conEmpresa, rcEmpresa.getEsquema(), fechaProceso);
    		 //float fechaHora = Float.parseFloat(UtilsFechas.getFechaActualNumero().substring(8));
    		 if (isEjecuto) {
    				//logger.info("Proceso ya ejecutado del dia..."+fechaHora);
    			}else {
    				grabarProceso(conEmpresa, rcEmpresa.getEsquema(), 0, "0"); // se grabar en bitacora
    				enviarNotificacionAlerta(conEmpresa, rcEmpresa.getEsquema(), empresasForm, configForm);
    			}
    	 }
			
     }catch(Exception e) {
		Utils.imprimeLog("", e);
	 }finally {
		try {
			if (conEmpresa != null) {
				conEmpresa.close();
			}
			conEmpresa = null;
		}catch(Exception e) {
			conEmpresa = null;
		}
	}
  }
 
 private void enviarNotificacionAlerta(Connection con, String esquemaBD, EmpresasForm empresasForm, AlertaConciliacionForm configForm) {
	 ConciliacionBovedaBean conciliadosBovedaBean = new ConciliacionBovedaBean();
	 ProveedoresBean provBean = new ProveedoresBean();
	 try {
		 String fechaIni = "2017-01-01 01:01:01";
		 String fechaFin = "2050-12-31 23:59:59";
		 String tipoComple = "SIN_COMPLE";
		 ArrayList<ConciliacionBovedaForm> listaDetalle = conciliadosBovedaBean.detalleConsiliados(con, esquemaBD, fechaIni, fechaFin, tipoComple);
		 ConciliacionBovedaForm conciForm = null;
		 String rfcAnterior = "";
		 ArrayList<ConciliacionBovedaForm> listaExcel = new ArrayList<>();
		 ProveedoresForm provForm = null;
		 String rutaDestinoXLS = null;
		 String mensajeCorreo = "";
		 for (int x = 0; x < listaDetalle.size(); x++) {
			 conciForm = listaDetalle.get(x);
			 if ("".equalsIgnoreCase(rfcAnterior)) {
				 listaExcel.add(conciForm);
			 }else {
				 if (rfcAnterior.equalsIgnoreCase(conciForm.getRfc())) {
					 listaExcel.add(conciForm);
				 }else {
					 provForm = provBean.consultarProveedorXrfc(con, esquemaBD, conciForm.getRfc());
					 if (!"".equalsIgnoreCase(Utils.noNuloNormal(provForm.getEmail()))) { // si esta configurado el proveedor
						 rutaDestinoXLS = generaExcel(listaExcel);
						 String emailTO [] = {provForm.getEmail()};
						 String emailCC [] = {configForm.getDestinatario1(), configForm.getDestinatario2(), configForm.getDestinatario3(), configForm.getDestinatario4(), configForm.getDestinatario5()};
						 mensajeCorreo = UtilsHTML.generaHTMLConciliacionBoveda(configForm.getMensajeError(), provForm);
						 //logger.info("mensajeCorreo===>"+mensajeCorreo);
						 EnviaCorreoGrid.enviarCorreo(rutaDestinoXLS, mensajeCorreo, true, emailTO, emailCC, "SIAREX - "+configForm.getSubject(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
						 UtilsFile.eliminaArchivo(rutaDestinoXLS);
					 }
					 
					 listaExcel = new ArrayList<>();
					 listaExcel.add(conciForm);
				 }
			 }
			 rfcAnterior = conciForm.getRfc();
		 }
		 
		 if (listaExcel.size() > 0) {
			 provForm = provBean.consultarProveedorXrfc(con, esquemaBD, conciForm.getRfc());
			 if (!"".equalsIgnoreCase(Utils.noNuloNormal(provForm.getEmail()))) { // si esta configurado el proveedor
				 rutaDestinoXLS = generaExcel(listaExcel);
				 String emailTO [] = {provForm.getEmail()};
				 String emailCC [] = {configForm.getDestinatario1(), configForm.getDestinatario2(), configForm.getDestinatario3(), configForm.getDestinatario4(), configForm.getDestinatario5()};
				 mensajeCorreo = UtilsHTML.generaHTMLConciliacionBoveda(configForm.getMensajeError(), provForm);
				 EnviaCorreoGrid.enviarCorreo(rutaDestinoXLS, mensajeCorreo, true, emailTO, emailCC, "SIAREX - "+configForm.getSubject(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
				 UtilsFile.eliminaArchivo(rutaDestinoXLS);
			 }
		 }
		 
	 }catch(Exception e) {
		 Utils.imprimeLog("", e);
	 }
 }
 
 
 public String generaExcel(ArrayList<ConciliacionBovedaForm> listaDetalle){
	 	SXSSFWorkbook  workbook = new SXSSFWorkbook(100); // Keep 100 rows in memory
	 	SXSSFSheet sheet = workbook.createSheet();
		String rutaDestinoXLS = null;
		try{
			
			workbook.setSheetName(0, "Detalle de Facturas");
			String[] headers = new String[]{
					"Tipo de Moneda",
		            "Serie/Folio",
		            "UUID Factura",
		            "Fecha Factura",
		            "Sub-Total",
		            "Iva",
		            "Iva Ret",
		            "Total",
		            "Total Factura",
		        };
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);
	        
	        CellStyle style = workbook.createCellStyle();
	        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        
	        
	        Font fuenteEncabezado = workbook.createFont();
			fuenteEncabezado.setFontHeightInPoints((short)11);
			fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			fuenteEncabezado.setColor(IndexedColors.WHITE.index);
			   
			 Font fuenteSubTitulo = workbook.createFont();
			 fuenteSubTitulo.setFontHeightInPoints((short)10);
			 fuenteSubTitulo.setFontName(HSSFFont.FONT_ARIAL);
			 fuenteSubTitulo.setBold(true);
			
			 Font fuenteDetalle = workbook.createFont();
			 fuenteDetalle.setFontHeightInPoints((short)10);
			 fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			
			
			
	        CellStyle estiloNumero = workbook.createCellStyle();
			estiloNumero.setAlignment(HorizontalAlignment.RIGHT);
			estiloNumero.setVerticalAlignment(VerticalAlignment.CENTER);
			estiloNumero.setFont(fuenteDetalle);
			
			
			CellStyle styleTitulo = workbook.createCellStyle();
			   Font headerFont = workbook.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true); // new java.awt.Color(12, 57, 90)
		       styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
		       styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleTitulo.setFont(headerFont);
		       styleTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
			   
			 CellStyle estiloCelda3 = workbook.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteSubTitulo);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
	        Font fuente = workbook.createFont();
			fuente.setFontHeightInPoints((short)10);
			fuente.setFontName(HSSFFont.FONT_ARIAL);
			    
			
			Row headerRow = sheet.createRow(0);
		   Cell monthCell = headerRow.createCell(0);
		   monthCell.setCellValue("Sistema de Recepcion de XML - Conciliacion de Complementos");
		   monthCell.setCellStyle(styleTitulo);
		   sheet.addMergedRegion(CellRangeAddress.valueOf("A1:I1"));
		   
			
			Row headerSubdetalle = sheet.createRow(1);
			Cell monthCell2 = headerSubdetalle.createCell(0);
			monthCell2.setCellValue("Detalle de Facturas sin complemento");
			monthCell2.setCellStyle(estiloCelda3);
			sheet.addMergedRegion(CellRangeAddress.valueOf("A2:I2"));
			
			Row headerDetalle = sheet.createRow(2);
			
	        String header = null;
	        Cell cell = null;
	        for (int i = 0; i < headers.length; ++i) {
	            header = headers[i];
	            cell = headerDetalle.createCell(i);
	            cell.setCellStyle(headerStyle);
	            cell.setCellValue(header);
	            
	        }
	        Row dataRow = null;
			sheet.setColumnWidth(0, 4000);
			sheet.setColumnWidth(1, 3000);
			sheet.setColumnWidth(2, 12000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 4000);
			sheet.setColumnWidth(6, 4000);
			sheet.setColumnWidth(7, 4000);
			sheet.setColumnWidth(8, 5000);
			
			ConciliacionBovedaForm conciForm = null;
			String rfcEmisor = "";
			Cell celdaNumero = null;
			for (int i = 0; i < listaDetalle.size(); ++i) {
	        	conciForm = listaDetalle.get(i);
	        	rfcEmisor = conciForm.getRfc();
	            dataRow = sheet.createRow(i + 3);
	            dataRow.createCell(0).setCellValue(conciForm.getTipoMoneda());
	            dataRow.createCell(1).setCellValue(conciForm.getSerieFolio());
	            dataRow.createCell(2).setCellValue(conciForm.getUuidOrden());
	            dataRow.createCell(3).setCellValue(conciForm.getFechaFactura());
	            
	            celdaNumero = dataRow.createCell(4);
	            celdaNumero.setCellValue(conciForm.getSubTotal());
	            celdaNumero.setCellStyle(estiloNumero);
	            
	            celdaNumero = dataRow.createCell(5);
	            celdaNumero.setCellValue(conciForm.getIva());
	            celdaNumero.setCellStyle(estiloNumero);
	            
	            celdaNumero = dataRow.createCell(6);
	            celdaNumero.setCellValue(conciForm.getIvaRet());
	            celdaNumero.setCellStyle(estiloNumero);
	            
	            celdaNumero = dataRow.createCell(7);
	            celdaNumero.setCellValue(conciForm.getTotal());
	            celdaNumero.setCellStyle(estiloNumero);
	            
	            celdaNumero = dataRow.createCell(8);
	            celdaNumero.setCellValue(conciForm.getTotalFactura());
	            dataRow.createCell(7).setCellStyle(estiloNumero);
	        }

	        String fechaHoy = UtilsFechas.getFechaActualNumero().substring(0, 8);
	        rutaDestinoXLS = "C:\\Tomcat9\\siarex247\\public_html\\mvs\\"+rfcEmisor+"_InformacionConciliacion_"+fechaHoy+".xlsx";
	        
	        FileOutputStream file = new FileOutputStream(rutaDestinoXLS);
	        workbook.write(file);
	        file.close();

	        
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				workbook.close();
			}catch(Exception e){
				workbook = null;
			}
		}
		return rutaDestinoXLS;
	}
 
 public String getFechaHoy(Connection con, String esquema) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String fechaHoy = null;
		try {
			fechaHoy = Utils.getFechayyyyMMdd();
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return fechaHoy ;
	}
 
 private boolean isEjecuto(Connection con, String esquema, String fecha){
     PreparedStatement stmt = null;
     ResultSet rs = null;

     try{
         stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getBuscarRespaldo(esquema));
         stmt.setString(1, "ACC"); // Alerta de conciliacion de complemento
         stmt.setString(2, fecha);
         rs = stmt.executeQuery();
			if(rs.next()){
				return true;
         }
			return false;
     }
     catch(Exception e){
     	Utils.imprimeLog("isEjecuto(): ", e);
     }
     finally{
	        try{
	            if(rs != null) {
	                rs.close();
	            }
	            rs = null;
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
     }
     return false;
  }
 
 private void grabarProceso(Connection con, String esquema, int totEnvios, String tipoEnvio)
 {
     PreparedStatement stmt = null;
     try{
         stmt = con.prepareStatement( ProcesoArchivosQuerys.getGrabarProceso(esquema));
         stmt.setString(1, "ACC");
         stmt.setString(2, tipoEnvio);
         stmt.setInt(3, totEnvios);
         stmt.setString(4, "OK");
         
         logger.info("stmt===>"+stmt);
         stmt.executeUpdate();
     }catch(Exception e){
     	Utils.imprimeLog("", e);
     }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
     }
 }

 
 private String obtenerdiaSemana() {
		String diaSemana = "";
		try {
			Calendar now = Calendar.getInstance();
			String[] strDays = new String[]{"DOM","LUN","MAR","MIE","JUE","VIE","SAB"};
			diaSemana = strDays[now.get(Calendar.DAY_OF_WEEK) - 1];
		}
		catch(Exception e) {
			Utils.imprimeLog("obtenerdiaSemana(): ", e);
		}
		return diaSemana;
	}

}
