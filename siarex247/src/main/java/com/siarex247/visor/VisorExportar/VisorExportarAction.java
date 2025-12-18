package com.siarex247.visor.VisorExportar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

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
import org.apache.struts2.ServletActionContext;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsColor;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesSupport;

import jakarta.servlet.http.HttpServletRequest;

public class VisorExportarAction extends VisorOrdenesSupport{

	
	private static final long serialVersionUID = -6082482083199594551L;

	
	private String reportFile;
	private InputStream inputStream;
	
	/*
	public String exportarPlantilla() {
		  SXSSFWorkbook  myWorkBook = new SXSSFWorkbook(100); // Keep 100 rows in memory
		  SXSSFSheet mySheet = myWorkBook.createSheet();
		  Connection con = null;
			ResultadoConexion rc = null;
			VisorExportarBean visorBean = new VisorExportarBean();
			try{
				HttpServletRequest request = ServletActionContext.getRequest();	
				SiarexSession session = ObtenerSession.getSession(request);
			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
			  SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
			  java.util.Date fechaActual = new java.util.Date();
			  
			  String fechaInicial = Utils.noNulo(getFechaInicial());
		  	  String fechaFinal = Utils.noNulo(getFechaFinal());
			  
	            ArrayList<VisorOrdenesForm> listaDetalle  = visorBean.detallePlantillas(con, rc.getEsquema(), getTipoMoneda(), getEstatusOrden(), getFolioEmpresa(), getRfc(), 
			  			  getRazonSocial(), getUuid(), getSerieFolio(), fechaInicial, fechaFinal, getFoliosExportar());
	            reportFile = "SIAREX_PLANTILLAS_" + formatDate.format(fechaActual) + ".xlsx";

			   setVisorAllInfo(mySheet, listaDetalle, myWorkBook, session.getLenguaje());
		
		   try {
		     ByteArrayOutputStream boas = new ByteArrayOutputStream();
		     myWorkBook.write(boas);
		     setInputStream(new ByteArrayInputStream(boas.toByteArray()));
		     myWorkBook.close();
		     
		   } catch (IOException e) {
		     Utils.imprimeLog("", e);
		   }
		  } catch (Exception e) {
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
		 } */
	
	public String exportarPlantilla() {
		  SXSSFWorkbook  myWorkBook = new SXSSFWorkbook(100);
		  SXSSFSheet mySheet = myWorkBook.createSheet();

		  Connection con = null;
		  ResultadoConexion rc = null;

		  try{
		    HttpServletRequest request = ServletActionContext.getRequest();
		    SiarexSession session = ObtenerSession.getSession(request);
		    rc  = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();

		    // Defaults de FECHA ULT MOV (365 días) si no viene nada del front
		    String fechaInicial = Utils.noNulo(getUltmovV1());
		    String fechaFinal   = Utils.noNulo(getUltmovV2());
		    String dateOperator;

		    if ("".equalsIgnoreCase(fechaInicial)) {
		        fechaFinal   = UtilsFechas.getFechayyyyMMdd();
		        fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
		        dateOperator = "bt";
		    } else {
		        dateOperator = Utils.noNulo(getUltmovOperator()); // 'bt', 'eq', etc.
		    }
		    dateOperator = "bt";
		    
		 // ... arriba igual
		    String fecha1 = Utils.noNulo(getUltmovV1());
		    String fecha2 = Utils.noNulo(getUltmovV2());
		    String operadorFechas = Utils.noNulo(getUltmovOperator());

		    if ("".equals(fecha1)) { // default 365 días
		    	fecha2 = UtilsFechas.getFechayyyyMMdd();
		        fecha1 = UtilsFechas.restarDiasFecha(fecha2, 365);
		      operadorFechas = "bt";
		    }
		    operadorFechas = "bt";
		    
		    // Compat con form viejo (opcional)
		    if ("".equals(fecha1) && !"".equals(Utils.noNulo(getFechaInicial()))) {
		    	fecha1 = Utils.noNulo(getFechaInicial());
		    	fecha2 = Utils.noNulo(getFechaFinal());
		    	// operadorFechas = (!"".equals(fecha1) && !"".equals(fecha1)) ? "bt" : "eq";
		    }
		    VisorExportarBean bean = new VisorExportarBean();
		    
		    ArrayList<VisorOrdenesForm> listaDetalle = bean.detallePlantillas(
		        con, rc.getEsquema(),
		        // TEXTO/SELECTS
		        Utils.noNuloNormal(getRazonSocial()), Utils.noNulo(getRsOperator()),
		        Utils.noNulo(getOcOperator()), Utils.noNulo(getOcV1()), Utils.noNulo(getOcV2()),
		        Utils.noNuloNormal(getDescripcion()), Utils.noNulo(getDescOperator()),
		        Utils.noNulo(getTipoMoneda()),        Utils.noNulo(getMonedaOperator()),
		        Utils.noNulo(getServicioRecibo()),    Utils.noNulo(getReciboOperator()),
		        Utils.noNulo(getEstatusPago()),       Utils.noNulo(getEstatusPagoOperator()),
		        Utils.noNuloNormal(getSerieFolio()),  Utils.noNulo(getSerieFolioOperator()),
		        Utils.noNuloNormal(getAsignarA()),    Utils.noNulo(getAsignarOperator()),
		        Utils.noNuloNormal(getEstadoCfdi()),  Utils.noNulo(getEstadoCfdiOperator()),
		        Utils.noNulo(getEstatusSat()),        Utils.noNulo(getEstatusSatOperator()),
		        Utils.noNulo(getUsoCfdi()),           Utils.noNulo(getUsoCfdiOperator()),
		        Utils.noNuloNormal(getCps()),         Utils.noNulo(getCpsOperator()),
		        // NUM
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
		        // FECHAS  ⬅️  usa op/f1/f2 correctos
		        Utils.noNulo(getFechapagoOperator()), Utils.noNulo(getFechapagoV1()), Utils.noNulo(getFechapagoV2()),
		        operadorFechas,                                   fecha1,                             fecha2,
		        // selección directa
		        Utils.noNulo(getFoliosExportar())
		    );


		    // Reporte
		    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		    reportFile = "SIAREX_PLANTILLAS_" + formatDate.format(new java.util.Date()) + ".xlsx";

		    setVisorAllInfo(mySheet, listaDetalle, myWorkBook, session.getLenguaje());

		    ByteArrayOutputStream boas = new ByteArrayOutputStream();
		    myWorkBook.write(boas);
		    setInputStream(new ByteArrayInputStream(boas.toByteArray()));
		    myWorkBook.close();

		  } catch (Exception e) {
		    Utils.imprimeLog("exportarPlantilla(): ", e);
		    return ERROR;
		  } finally {
		    try { if (con != null) con.close(); } catch (Exception ignore) {}
		  }
		  return SUCCESS;
		}



	
	
	 private void setVisorAllInfo(SXSSFSheet hoja1 ,ArrayList<VisorOrdenesForm> listaVisor, SXSSFWorkbook objLibro, String idLenguaje) {
		  try {
			   LenguajeBean lenBean = LenguajeBean.instance();
			   HashMap <String, String> mapaLen = lenBean.obtenerEtiquetas(idLenguaje, "VISOR");
			
			   //final String[] nameColumns = {"Orden de Compra", "Monto Factura", "RFC", "Razon Social", "Tipo de Moneda", "Tipo de Confirmacion", "Importe Pagado" };
			  final String[] nameColumns = {Utils.regresaCaracteresNormales(mapaLen.get("LABEL3")), Utils.regresaCaracteresNormales(mapaLen.get("LABEL6")), Utils.regresaCaracteresNormales(mapaLen.get("LABEL1")), Utils.regresaCaracteresNormales(mapaLen.get("LABEL2")), 
					  Utils.regresaCaracteresNormales(mapaLen.get("LABEL5")), Utils.regresaCaracteresNormales(mapaLen.get("LABEL64")), Utils.regresaCaracteresNormales(mapaLen.get("LABEL65")) };
			  
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   
			 
			   Font fuente = objLibro.createFont();
			   fuente.setFontHeightInPoints((short)10);
			   fuente.setFontName(HSSFFont.FONT_ARIAL);
			  
			   
			   Font fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBold(true);
			  

			   
			   Font fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(IndexedColors.WHITE.index);
			  
			   
			  
			   
			   CellStyle estiloCeldaTotal = objLibro.createCellStyle();
			   estiloCeldaTotal.setAlignment(HorizontalAlignment.RIGHT);
			   estiloCeldaTotal.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCeldaTotal.setFont(fuenteEncabezado);
			   estiloCeldaTotal.setFillForegroundColor(IndexedColors.GREEN.index);
			   estiloCeldaTotal.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   
			   
			   CellStyle estiloCelda2 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda2.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda2.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda2.setFont(fuente);
			   estiloCelda2.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   
			   CellStyle estiloCelda3 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   
			   
			   CellStyle styleTitulo = objLibro.createCellStyle();
			   Font headerFont = objLibro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true);
		       styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
		       styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleTitulo.setFont(headerFont);
		       styleTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       
		       CellStyle styleSubTitulo = objLibro.createCellStyle();
			   Font fontSub = objLibro.createFont();
			   fontSub.setFontHeightInPoints((short)10);
			   fontSub.setFontName("Arial");
			   fontSub.setBold(true);
		       styleSubTitulo.setFont(fontSub);
		       styleSubTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       /*
			   CellStyle encabezadoPrincipal = objLibro.createCellStyle();
			   //encabezadoPrincipal.setWrapText(true);
			   encabezadoPrincipal.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoPrincipal.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoPrincipal.setFont(fuenteEncabezado);
			   encabezadoPrincipal.setFillForegroundColor(palette.findSimilarColor((byte) 12, (byte) 57, (byte) 90).getIndex());
			   encabezadoPrincipal.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   
			   
			   CellStyle encabezadoDetalle = objLibro.createCellStyle();
			   //encabezadoDetalle.setWrapText(true);
			   encabezadoDetalle.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoDetalle.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoDetalle.setFont(fuenteEncabezado);
			   encabezadoDetalle.setFillForegroundColor(palette.findSimilarColor((byte) 6, (byte) 103, (byte) 172).getIndex());
			   encabezadoDetalle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   */
		       
			   /*
			    Cell monthCell = header.createCell(0);
			    monthCell.setCellValue(Utils.noNuloNormal(mapaLen.get("LABEL67")));
			    monthCell.setCellStyle(encabezadoPrincipal);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:G1"));
			    */
			    Cell monthCell = header.createCell(0);
			    monthCell.setCellValue(Utils.noNuloNormal(mapaLen.get("LABEL67")));
				monthCell.setCellStyle(styleTitulo);
				hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:G1"));
				   
			   
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			    Cell monthCell2 = header.createCell(0);
			    monthCell2.setCellValue(Utils.noNuloNormal(mapaLen.get("LABEL66")));
			    //estiloCelda2.setAlignment(HorizontalAlignment.CENTER);
			    monthCell2.setCellStyle(estiloCelda3);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:G2"));
			    
			    
			    
			   header = hoja1.createRow(2);
			   header.setHeightInPoints(18);
			   for (int i = 0; i < nameColumns.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(nameColumns[i]);
			    monthCell.setCellStyle(styleSubTitulo);
			    			    
			   }
			   
			   hoja1.trackAllColumnsForAutoSizing();;
			   
			   
			   VisorOrdenesForm visorForm = null;
			   Cell celda1 = null;
			   Cell celda2 = null;
			   Cell celda3 = null;
			   Cell celda4 = null;
			   Cell celda5 = null;
			   Cell celda6 = null;
			   Cell celda7 = null;
			   
			   Row fila = null;
			   boolean bandRen = true;
			   boolean bandPrimero = true;

			   for (int x = 0; x < listaVisor.size(); x++) {
				   visorForm = listaVisor.get(x);
				   fila = hoja1.createRow(x + 3);
				   fila.setHeightInPoints(18);
				   
				   if (bandRen){
					   bandRen = false;
				   }else{
					   bandRen = true;
				   }
				   
				   celda1 = fila.createCell(0);
				   // celda1.setCellType(CellType.STRING);
				   celda1.setCellValue(visorForm.getFolioEmpresa());
				   
				   
				   celda2 = fila.createCell(1);
				   // celda2.setCellType(CellType.NUMERIC);
				   if ("A5".equalsIgnoreCase(visorForm.getEstatusOrden())){
					   celda2.setCellValue(Utils.noNuloDouble(visorForm.getMonto()));
				   }else if ("A3".equalsIgnoreCase(visorForm.getEstatusOrden())){
					   celda2.setCellValue(Utils.noNuloDouble(visorForm.getTotal()));
				   }else if ("A2".equalsIgnoreCase(visorForm.getEstatusOrden())){
					   celda2.setCellValue(Utils.noNuloDouble(visorForm.getMonto()));
				   }else{
					   if ("0".equalsIgnoreCase(visorForm.getTipoValidacionPro())){
						   if (visorForm.getTipoProveedor().equalsIgnoreCase("USA")){
							   celda2.setCellValue(Utils.noNuloDouble(visorForm.getTotal()));
						   }else{
							   celda2.setCellValue(Utils.noNuloDouble(visorForm.getMonto()));
						   }
						      
					   }else{
						   celda2.setCellValue(Utils.noNuloDouble(visorForm.getTotal()));
					   }   
				   }
				   
				   celda3 = fila.createCell(2);
				   //celda3.setCellType(CellType.STRING);
				   celda3.setCellValue(visorForm.getRfc());
				   
				   celda4 = fila.createCell(3);
				  // celda4.setCellType(CellType.STRING);
				   celda4.setCellValue(visorForm.getRazonSocial());
				   
				   celda5 = fila.createCell(4);
				   //celda5.setCellType(CellType.STRING);
				   celda5.setCellValue(visorForm.getTipoMoneda());
				   
				   celda6 = fila.createCell(5);
				  // celda6.setCellType(CellType.STRING);
				   if ("0".equalsIgnoreCase(visorForm.getTipoValidacionPro())){
					      celda6.setCellValue("SUB-TOTAL");
				     }else{
				    	 celda6.setCellValue("TOTAL");
				     }
				   
				   celda7 = fila.createCell(6);
				   //celda7.setCellType(CellType.NUMERIC);
				   celda7.setCellValue(0);
				    
				   if (bandPrimero) {
					   hoja1.trackColumnForAutoSizing(0);   
					   hoja1.trackColumnForAutoSizing(1);
					   hoja1.trackColumnForAutoSizing(2);
					   hoja1.trackColumnForAutoSizing(3);
					   hoja1.trackColumnForAutoSizing(4);
					   hoja1.trackColumnForAutoSizing(5);
					   hoja1.trackColumnForAutoSizing(6);
				   }
				    
				   bandPrimero = false;
			   }
		  } catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
			 
	 }	
	
	
	 
	 
	 /*
	 public String exportarFacturas() {
		 logger.info("**** EXPORTAR VISOR ***************");
		  Connection con = null;
			ResultadoConexion rc = null;
			VisorExportarBean visorBean = new VisorExportarBean();
			try{
			  HttpServletRequest request = ServletActionContext.getRequest();	
			  SiarexSession session = ObtenerSession.getSession(request);
			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();

			  String fechaInicial = Utils.noNulo(getFechaInicial());
		  	  String fechaFinal = Utils.noNulo(getFechaFinal());
		  		
			  // ArrayList<VisorOrdenesForm> listaDetalle  = visorBean.detalleFacturas(con, rc.getEsquema(), getFoliosExportar());
		  	  ArrayList<VisorOrdenesForm> listaDetalle  = visorBean.detalleFacturas(con, rc.getEsquema(), getTipoMoneda(), getEstatusOrden(), getFolioEmpresa(), getRfc(), 
		  			  getRazonSocial(), getUuid(), getSerieFolio(), fechaInicial, fechaFinal, getFoliosExportar());
			  
			    
				VisorOrdenesForm visorForm = null;
				String directorioXML = null;
				String directorioPDF = null;
				
				String directorioXML_Complemento = null;
				String directorioPDF_Complemento = null;
				
				String directorioXML_Nota = null;
				String directorioPDF_Nota = null;
				
				// String rutaArchivoXML = null;
				// String rutaArchivoPDF = null;
				ArrayList<String> alFiles = new ArrayList<String>();
				String tipoOrden = "";
				//VisorComplementos compleBean = new VisorComplementos();
				// VisorNotaCredito notasCreditoBean = new VisorNotaCredito();
				// JSONObject jsonobj = null;
				final String PROVEEDORES = File.separator + "PROVEEDORES" + File.separator;
				// final String NOMBRE_XML = "NOMBRE_XML";
				// final String NOMBRE_PDF = "NOMBRE_PDF";
				
				String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + PROVEEDORES ;
				for (int x = 0; x < listaDetalle.size(); x++){
					visorForm = listaDetalle.get(x);
					if ("A1".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A3".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A4".equalsIgnoreCase(visorForm.getEstatusOrden()) 
							|| "A9".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A10".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A11".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A12".equalsIgnoreCase(visorForm.getEstatusOrden())) {

						
						if (!"".equals(visorForm.getNombreXML())){
							if ("".equals(visorForm.getTipoOrden())){
								directorioXML =  rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getNombreXML();
								directorioPDF =  rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getNombrePDF();
								alFiles.add(directorioXML);
								alFiles.add(directorioPDF);
								// jsonobj = compleBean.buscarDocumento(con, session.getEsquemaEmpresa(), visorForm.getFolioOrden());
								if (!"".equalsIgnoreCase(visorForm.getTieneComplementoXML())) {
									directorioXML_Complemento = rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneComplementoXML();
									directorioPDF_Complemento = rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneComplementoPDF();
									if (!alFiles.contains(directorioXML_Complemento)) {
										alFiles.add(directorioXML_Complemento);
										alFiles.add(directorioPDF_Complemento);	
									}
								}
								
								// jsonobj = notasCreditoBean.buscarNotaCreditoOK(con, session.getEsquemaEmpresa(), visorForm.getFolioOrden());
								if (!"".equalsIgnoreCase(visorForm.getTieneNotaCreditoXML())) {
									directorioXML_Nota = rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneNotaCreditoXML();
									directorioPDF_Nota = rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneNotaCreditoPDF();
									if (!alFiles.contains(directorioXML_Nota)) {
										alFiles.add(directorioXML_Nota);
										alFiles.add(directorioPDF_Nota);	
									}
								}
								
							}else if (!tipoOrden.equals(visorForm.getTipoOrden())){
								directorioXML =  rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getNombreXML();
								directorioPDF =  rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getNombrePDF();
								alFiles.add(directorioXML);
								alFiles.add(directorioPDF);
								// jsonobj = compleBean.buscarDocumento(con, session.getEsquemaEmpresa(), visorForm.getFolioOrden());
								if (!"".equalsIgnoreCase(visorForm.getTieneComplementoXML())) {
									directorioXML_Complemento = rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneComplementoXML();
									directorioPDF_Complemento = rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneComplementoPDF();
									if (!alFiles.contains(directorioXML_Complemento)) {
										alFiles.add(directorioXML_Complemento);
										alFiles.add(directorioPDF_Complemento);	
									}
									
								}
								
								// jsonobj = notasCreditoBean.buscarNotaCreditoOK(con, session.getEsquemaEmpresa(), visorForm.getFolioOrden());
								if (!"".equalsIgnoreCase(visorForm.getTieneNotaCreditoXML())) {
									directorioXML_Nota = rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneNotaCreditoXML();
									directorioPDF_Nota = rutaFinal + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneNotaCreditoPDF();
									if (!alFiles.contains(directorioXML_Nota)) {
										alFiles.add(directorioXML_Nota);
										alFiles.add(directorioPDF_Nota);	
									}
								}
								
							}
							tipoOrden = visorForm.getTipoOrden();
						}
						
					}
					
				}
				
				if (!alFiles.isEmpty()){
					ZipFiles zipFiles = new ZipFiles();
					ByteArrayOutputStream dest = zipFiles.zipFiles(alFiles);
					setInputStream(new ByteArrayInputStream(dest.toByteArray()));
				}else{
					addActionMessage("Usurio y/o Pasword Incorrecto!");
					return  ERROR;
				}
		   
		  } catch (Exception e) {
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
		 } */
	 
	 public String exportarFacturas() {
		  logger.info("**** EXPORTAR VISOR DX ***************");
		  Connection con = null;
		  ResultadoConexion rc = null;
		  try{
		    HttpServletRequest request = ServletActionContext.getRequest();	
		    SiarexSession session = ObtenerSession.getSession(request);
		    rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();
		    
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

		    VisorExportarBean bean = new VisorExportarBean();

		    // 1) pedir la lista con filtros DX (REUTILIZA tu clase FiltrosVisorOrdenesFiltros)
		 // 1) Pedir la lista con filtros DX (reusa FiltrosVisorOrdenesFiltros)
		    java.util.List<VisorOrdenesForm> lista = bean.detalleFacturas(
		        con, rc.getEsquema(),

		        // ===== TEXTO / SELECTS (valor, operador) =====
		        Utils.noNuloNormal(getRazonSocial()), Utils.noNulo(getRsOperator()),

		        // 3 Orden de Compra (numérico: operador, v1, v2)
		        Utils.noNulo(getOcOperator()), Utils.noNulo(getOcV1()), Utils.noNulo(getOcV2()),

		        // resto de TEXTO/SELECTS
		        Utils.noNuloNormal(getDescripcion()),        Utils.noNulo(getDescOperator()),
		        normSel1.apply(getTipoMoneda()),            Utils.noNulo(getMonedaOperator()),
		        normSel1.apply(getServicioRecibo()),        Utils.noNulo(getReciboOperator()),
		        normSel1.apply(getEstatusPago()),           Utils.noNulo(getEstatusPagoOperator()),
		        Utils.noNuloNormal(getSerieFolio()),        Utils.noNulo(getSerieFolioOperator()),
		        Utils.noNuloNormal(getAsignarA()),          Utils.noNulo(getAsignarOperator()),
		        Utils.noNuloNormal(getEstadoCfdi()),        Utils.noNulo(getEstadoCfdiOperator()),
		        normSel1.apply(getEstatusSat()),            Utils.noNulo(getEstatusSatOperator()),
		        normSel1.apply(getUsoCfdi()),               Utils.noNulo(getUsoCfdiOperator()),
		        Utils.noNuloNormal(getCps()),               Utils.noNulo(getCpsOperator()),

		        // ===== NUMÉRICOS (operador, v1, v2) =====
		        Utils.noNulo(getMontoOperator()),           Utils.noNulo(getMontoV1()),     Utils.noNulo(getMontoV2()),
		        Utils.noNulo(getTotalOperator()),           Utils.noNulo(getTotalV1()),     Utils.noNulo(getTotalV2()),
		        Utils.noNulo(getSubtotalOperator()),        Utils.noNulo(getSubtotalV1()),  Utils.noNulo(getSubtotalV2()),
		        Utils.noNulo(getIvaOperator()),             Utils.noNulo(getIvaV1()),       Utils.noNulo(getIvaV2()),
		        Utils.noNulo(getIvaretOperator()),          Utils.noNulo(getIvaretV1()),    Utils.noNulo(getIvaretV2()),
		        Utils.noNulo(getIsrretOperator()),          Utils.noNulo(getIsrretV1()),    Utils.noNulo(getIsrretV2()),
		        Utils.noNulo(getImplocOperator()),          Utils.noNulo(getImplocV1()),    Utils.noNulo(getImplocV2()),
		        Utils.noNulo(getTotalncOperator()),         Utils.noNulo(getTotalncV1()),   Utils.noNulo(getTotalncV2()),
		        Utils.noNulo(getPagotOperator()),           Utils.noNulo(getPagotV1()),     Utils.noNulo(getPagotV2()),
		        Utils.noNulo(getIvaretncOperator()),        Utils.noNulo(getIvaretncV1()),  Utils.noNulo(getIvaretncV2()),

		        // ===== FECHAS (operador, v1, v2) =====
		        Utils.noNulo(getFechapagoOperator()),       Utils.noNulo(getFechapagoV1()), Utils.noNulo(getFechapagoV2()),
		        /* ultmov: usa la lógica que ya calculaste arriba */
		        dateOperator,                               fechaInicial,                   fechaFinal,

		        // ===== SELECCIÓN DIRECTA =====
		        Utils.noNulo(getFoliosExportar())
		    );


		    // 2) armar lista de paths y zippear (igual que tu export actual)
		    final String PROV = File.separator + "PROVEEDORES" + File.separator;
		    String rutaBase = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + PROV;

		    java.util.ArrayList<String> files = new java.util.ArrayList<>();
		    String tipoOrdenFlag = "";
		    for (VisorOrdenesForm v : lista){
		      if ("A1".equalsIgnoreCase(v.getEstatusOrden()) || "A3".equalsIgnoreCase(v.getEstatusOrden())
		       || "A4".equalsIgnoreCase(v.getEstatusOrden()) || "A9".equalsIgnoreCase(v.getEstatusOrden())
		       || "A10".equalsIgnoreCase(v.getEstatusOrden())|| "A11".equalsIgnoreCase(v.getEstatusOrden())
		       || "A12".equalsIgnoreCase(v.getEstatusOrden()) || "A6".equalsIgnoreCase(v.getEstatusOrden())) {

		        if (!"".equals(Utils.noNulo(v.getNombreXML()))){
		          if ("".equals(Utils.noNulo(v.getTipoOrden())) || !tipoOrdenFlag.equals(v.getTipoOrden())){
		            String dirXML = rutaBase + v.getClaveProveedor() + File.separator + v.getNombreXML();
		            String dirPDF = rutaBase + v.getClaveProveedor() + File.separator + v.getNombrePDF();
		            files.add(dirXML); files.add(dirPDF);
		            if (!"".equals(Utils.noNulo(v.getTieneComplementoXML()))){
		              String cx = rutaBase + v.getClaveProveedor() + File.separator + v.getTieneComplementoXML();
		              String cp = rutaBase + v.getClaveProveedor() + File.separator + v.getTieneComplementoPDF();
		              if (!files.contains(cx)){ files.add(cx); files.add(cp); }
		            }
		            if (!"".equals(Utils.noNulo(v.getTieneNotaCreditoXML()))){
		              String nx = rutaBase + v.getClaveProveedor() + File.separator + v.getTieneNotaCreditoXML();
		              String np = rutaBase + v.getClaveProveedor() + File.separator + v.getTieneNotaCreditoPDF();
		              if (!files.contains(nx)){ files.add(nx); files.add(np); }
		            }
		          }
		          tipoOrdenFlag = Utils.noNulo(v.getTipoOrden());
		        }
		      }
		    }

		    if (files.isEmpty()){ addActionMessage("Sin documentos para exportar"); return ERROR; }

		    ZipFiles zipFiles = new ZipFiles();
		    ByteArrayOutputStream dest = zipFiles.zipFiles(files);
		    setInputStream(new ByteArrayInputStream(dest.toByteArray()));
		    logger.info("**** FIN VISOR DX ***************");
		  }catch(Exception e){
		    Utils.imprimeLog("exportarFacturasDX(): ", e);
		    return ERROR;
		  }finally{
		    try{ if (con!=null) con.close(); }catch(Exception ignore){}
		  }
		  return SUCCESS;
		}

	 
	 
	 /*
	 public String exportarLayOut(){
		  Connection con = null;
			ResultadoConexion rc = null;
			VisorExportarBean visorBean = new VisorExportarBean();
			try{
			  HttpServletRequest request = ServletActionContext.getRequest();	
			  SiarexSession session = ObtenerSession.getSession(request);
			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
	            
			  String fechaInicial = Utils.noNulo(getFechaInicial());
		  	  String fechaFinal = Utils.noNulo(getFechaFinal());
			  
	          ArrayList<VisorOrdenesForm> listaVisor  = visorBean.detalleLayOut(con, rc.getEsquema(), getTipoMoneda(), getEstatusOrden(), getFolioEmpresa(), getRfc(), 
			  			  getRazonSocial(), getUuid(), getSerieFolio(), fechaInicial, fechaFinal, getFoliosExportar());
			  
				VisorOrdenesForm visorForm = null;
				String tipoOrden = "";
				String tipoOrdenAnterior = "";
				ExportarCSV exportarCSV = new ExportarCSV();
				String line1 = null;
				String line2 = null;
				String line3 = null;
				String line4 = null;
				///REPOSITORIOS/"+session.getEsquemaEmpresa()+"/PROVEEDORES/
				
				ArrayList<String> listaTXT = new ArrayList<String>();
				VisorOrdenesForm visorFormTemp = null;
				boolean bandMultiple = false;
				
				ConfigAdicionalesBean confSistemaBean = new ConfigAdicionalesBean();
				HashMap<String, String> mapaConf = confSistemaBean.obtenerConfiguraciones(session.getEsquemaEmpresa());
				
				String labelEmpresa = Utils.noNulo(mapaConf.get("LABEL_LAYOUT_ORDEN"));
				String labelMultiple = Utils.noNulo(mapaConf.get("LABEL_LAYOUT_MULTIPLE"));
				
				
				for (int x = 0; x < listaVisor.size(); x++){
					visorForm = listaVisor.get(x);
					tipoOrden = visorForm.getTipoOrden();
					if ("".equalsIgnoreCase(tipoOrden)){ //  es orden normal
						if (bandMultiple){ // si esta encendida la bandera
							line2 = exportarCSV.generarLine(visorFormTemp, "IVA", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
							listaTXT.add(line2);
							
							line3 = exportarCSV.generarLine(visorFormTemp, "IVA_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
							listaTXT.add(line3);
							
							line4 = exportarCSV.generarLine(visorFormTemp, "ISR_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
							listaTXT.add(line4);
						}
						
						line1 = exportarCSV.generarLine(visorForm, "ORDEN", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
						listaTXT.add(line1);
						
						line2 = exportarCSV.generarLine(visorForm, "IVA", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
						listaTXT.add(line2);
						
						line3 = exportarCSV.generarLine(visorForm, "IVA_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
						listaTXT.add(line3);
						
						line4 = exportarCSV.generarLine(visorForm, "ISR_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
						listaTXT.add(line4);
						
						bandMultiple = false;
					}else{
						if (!tipoOrden.equalsIgnoreCase(tipoOrdenAnterior) && bandMultiple) {
							line2 = exportarCSV.generarLine(visorFormTemp, "IVA", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
							listaTXT.add(line2);
							
							line3 = exportarCSV.generarLine(visorFormTemp, "IVA_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
							listaTXT.add(line3);
							
							line4 = exportarCSV.generarLine(visorFormTemp, "ISR_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
							listaTXT.add(line4);
							
							line1 = exportarCSV.generarLine(visorForm, "ORDEN", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
							listaTXT.add(line1);
						}else {
							line1 = exportarCSV.generarLine(visorForm, "ORDEN", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
							listaTXT.add(line1);
						}
						bandMultiple = true;
					}
					
					tipoOrdenAnterior = tipoOrden;
					visorFormTemp = visorForm;
				}
				
		   
				if (bandMultiple){ // si esta encendida la bandera
					line2 = exportarCSV.generarLine(visorFormTemp, "IVA", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
					listaTXT.add(line2);
					
					line3 = exportarCSV.generarLine(visorFormTemp, "IVA_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
					listaTXT.add(line3);
					
					line4 = exportarCSV.generarLine(visorFormTemp, "ISR_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple);
					listaTXT.add(line4);
				}
				
				String pathCSV = UtilsPATH.RUTA_PUBLIC_HTML + "LAYOUT/csvFacturas.csv";
				UtilsFile.crearArchivoSalto(listaTXT, pathCSV);
				inputStream = new FileInputStream(new File(pathCSV));
				
		  } catch (Exception e) {
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
		 } */
	 
	 public String exportarLayOut(){
		  logger.info("**** EXPORTAR LAYOUT DX ***************");
		  Connection con = null;
		  ResultadoConexion rc = null;
		  try{
		    HttpServletRequest request = ServletActionContext.getRequest();
		    SiarexSession session = ObtenerSession.getSession(request);
		    rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();

		    // Defaults de FECHA ULT MOV (365 días) si no viene nada del front
		    String fechaInicial = Utils.noNulo(getUltmovV1());
		    String fechaFinal   = Utils.noNulo(getUltmovV2());
		    String dateOperator;

		    if ("".equalsIgnoreCase(fechaInicial)) {
		        fechaFinal   = UtilsFechas.getFechayyyyMMdd();
		        fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
		        dateOperator = "bt";
		    } else {
		        dateOperator = Utils.noNulo(getUltmovOperator()); // 'bt', 'eq', etc.
		    }
		    dateOperator = "bt";
		    
		    /*
		    String f1 = Utils.noNulo(getUltmovV1());
		    String f2 = Utils.noNulo(getUltmovV2());
		    String op = Utils.noNulo(getUltmovOperator());

		    if ("".equals(f1) && !"".equals(Utils.noNulo(getFechaInicial()))) {
		      // Compatibilidad con el form viejo
		      f1 = Utils.noNulo(getFechaInicial());
		      f2 = Utils.noNulo(getFechaFinal());
		      op = ( !"".equals(f1) && !"".equals(f2) ) ? "bt" : "eq";
		    }
		    */
		    
		    java.util.function.Function<String,String> normSel1 = v -> {
		      String x = Utils.noNulo(v);
		      return "ALL".equalsIgnoreCase(x) ? "" : x;
		    };
		    
		    /*
		    if (logger.isInfoEnabled()) {
		    	  final String n  = ""; // por legibilidad
		    	  StringBuilder sb = new StringBuilder(256);
		    	  sb.append("[EXPORT/LAYOUT/PARAMS] ===============================");

		    	  // Selección directa
		    	  sb.append("\n  [SELECCION]")
		    	    .append("\n    foliosExportar=").append(Utils.noNulo(getFoliosExportar()));

		    	  // Texto / Selects
		    	  sb.append("\n  [TEXTO/SELECTS]")
		    	    .append("\n    razonSocial=").append(Utils.noNuloNormal(getRazonSocial()))
		    	      .append(" | rsOperator=").append(Utils.noNulo(getRsOperator()))
		    	    .append("\n    ocOperator=").append(Utils.noNulo(getOcOperator()))
		    	      .append(" | ocV1=").append(Utils.noNulo(getOcV1()))
		    	      .append(" | ocV2=").append(Utils.noNulo(getOcV2()))
		    	    .append("\n    descripcion=").append(Utils.noNuloNormal(getDescripcion()))
		    	      .append(" | descOperator=").append(Utils.noNulo(getDescOperator()))
		    	    .append("\n    tipoMoneda=").append(Utils.noNulo(getTipoMoneda()))
		    	      .append(" | monedaOperator=").append(Utils.noNulo(getMonedaOperator()))
		    	    .append("\n    servicioRecibo=").append(Utils.noNulo(getServicioRecibo()))
		    	      .append(" | reciboOperator=").append(Utils.noNulo(getReciboOperator()))
		    	    .append("\n    estatusPago=").append(Utils.noNulo(getEstatusPago()))
		    	      .append(" | estatusPagoOperator=").append(Utils.noNulo(getEstatusPagoOperator()))
		    	    .append("\n    serieFolio=").append(Utils.noNuloNormal(getSerieFolio()))
		    	      .append(" | serieFolioOperator=").append(Utils.noNulo(getSerieFolioOperator()))
		    	    .append("\n    asignarA=").append(Utils.noNuloNormal(getAsignarA()))
		    	      .append(" | asignarOperator=").append(Utils.noNulo(getAsignarOperator()))
		    	    .append("\n    estadoCfdi=").append(Utils.noNuloNormal(getEstadoCfdi()))
		    	      .append(" | estadoCfdiOperator=").append(Utils.noNulo(getEstadoCfdiOperator()))
		    	    .append("\n    estatusSat=").append(Utils.noNulo(getEstatusSat()))
		    	      .append(" | estatusSatOperator=").append(Utils.noNulo(getEstatusSatOperator()))
		    	    .append("\n    usoCfdi=").append(Utils.noNulo(getUsoCfdi()))
		    	      .append(" | usoCfdiOperator=").append(Utils.noNulo(getUsoCfdiOperator()))
		    	    .append("\n    cps=").append(Utils.noNuloNormal(getCps()))
		    	      .append(" | cpsOperator=").append(Utils.noNulo(getCpsOperator()));

		    	  // Numéricos
		    	  sb.append("\n  [NUMERICOS]")
		    	    .append("\n    montoOperator=").append(Utils.noNulo(getMontoOperator()))
		    	      .append(" | montoV1=").append(Utils.noNulo(getMontoV1()))
		    	      .append(" | montoV2=").append(Utils.noNulo(getMontoV2()))
		    	    .append("\n    totalOperator=").append(Utils.noNulo(getTotalOperator()))
		    	      .append(" | totalV1=").append(Utils.noNulo(getTotalV1()))
		    	      .append(" | totalV2=").append(Utils.noNulo(getTotalV2()))
		    	    .append("\n    subtotalOperator=").append(Utils.noNulo(getSubtotalOperator()))
		    	      .append(" | subtotalV1=").append(Utils.noNulo(getSubtotalV1()))
		    	      .append(" | subtotalV2=").append(Utils.noNulo(getSubtotalV2()))
		    	    .append("\n    ivaOperator=").append(Utils.noNulo(getIvaOperator()))
		    	      .append(" | ivaV1=").append(Utils.noNulo(getIvaV1()))
		    	      .append(" | ivaV2=").append(Utils.noNulo(getIvaV2()))
		    	    .append("\n    ivaretOperator=").append(Utils.noNulo(getIvaretOperator()))
		    	      .append(" | ivaretV1=").append(Utils.noNulo(getIvaretV1()))
		    	      .append(" | ivaretV2=").append(Utils.noNulo(getIvaretV2()))
		    	    .append("\n    isrretOperator=").append(Utils.noNulo(getIsrretOperator()))
		    	      .append(" | isrretV1=").append(Utils.noNulo(getIsrretV1()))
		    	      .append(" | isrretV2=").append(Utils.noNulo(getIsrretV2()))
		    	    .append("\n    implocOperator=").append(Utils.noNulo(getImplocOperator()))
		    	      .append(" | implocV1=").append(Utils.noNulo(getImplocV1()))
		    	      .append(" | implocV2=").append(Utils.noNulo(getImplocV2()))
		    	    .append("\n    totalncOperator=").append(Utils.noNulo(getTotalncOperator()))
		    	      .append(" | totalncV1=").append(Utils.noNulo(getTotalncV1()))
		    	      .append(" | totalncV2=").append(Utils.noNulo(getTotalncV2()))
		    	    .append("\n    pagotOperator=").append(Utils.noNulo(getPagotOperator()))
		    	      .append(" | pagotV1=").append(Utils.noNulo(getPagotV1()))
		    	      .append(" | pagotV2=").append(Utils.noNulo(getPagotV2()))
		    	    .append("\n    ivaretncOperator=").append(Utils.noNulo(getIvaretncOperator()))
		    	      .append(" | ivaretncV1=").append(Utils.noNulo(getIvaretncV1()))
		    	      .append(" | ivaretncV2=").append(Utils.noNulo(getIvaretncV2()));

		    	  // Fechas (usa las variables f1/f2/op que calculaste arriba)
		    	  sb.append("\n  [FECHAS]")
		    	    .append("\n    fechapagoOperator=").append(Utils.noNulo(getFechapagoOperator()))
		    	      .append(" | fechapagoV1=").append(Utils.noNulo(getFechapagoV1()))
		    	      .append(" | fechapagoV2=").append(Utils.noNulo(getFechapagoV2()))
		    	    .append("\n    ultmovOperator=").append(op)
		    	      .append(" | ultmovV1=").append(f1)
		    	      .append(" | ultmovV2=").append(f2);

		    	  logger.info(sb.toString());
		    	}  */

		    VisorExportarBean bean = new VisorExportarBean();

		    // Obtener filas para LayOut (misma API de filtros DX)
		    java.util.List<VisorOrdenesForm> lista = bean.detalleLayOut(
		        con, rc.getEsquema(),
		        // ===== TEXTO/SELECTS =====
		        Utils.noNuloNormal(getRazonSocial()), Utils.noNulo(getRsOperator()),
		        Utils.noNulo(getOcOperator()), Utils.noNulo(getOcV1()), Utils.noNulo(getOcV2()),
		        Utils.noNuloNormal(getDescripcion()),        Utils.noNulo(getDescOperator()),
		        normSel1.apply(getTipoMoneda()),             Utils.noNulo(getMonedaOperator()),
		        normSel1.apply(getServicioRecibo()),         Utils.noNulo(getReciboOperator()),
		        normSel1.apply(getEstatusPago()),            Utils.noNulo(getEstatusPagoOperator()),
		        Utils.noNuloNormal(getSerieFolio()),         Utils.noNulo(getSerieFolioOperator()),
		        Utils.noNuloNormal(getAsignarA()),           Utils.noNulo(getAsignarOperator()),
		        Utils.noNuloNormal(getEstadoCfdi()),         Utils.noNulo(getEstadoCfdiOperator()),
		        normSel1.apply(getEstatusSat()),             Utils.noNulo(getEstatusSatOperator()),
		        normSel1.apply(getUsoCfdi()),                Utils.noNulo(getUsoCfdiOperator()),
		        Utils.noNuloNormal(getCps()),                Utils.noNulo(getCpsOperator()),
		        // ===== NUMÉRICOS =====
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
		        // ===== FECHAS =====
		        Utils.noNulo(getFechapagoOperator()), Utils.noNulo(getFechapagoV1()), Utils.noNulo(getFechapagoV2()),
		        dateOperator,                         fechaInicial,                   fechaFinal,
		        // selección directa
		        Utils.noNulo(getFoliosExportar())
		    );

		    // ===== tu lógica para armar el TXT (idéntica a la que ya tenías) =====
		    ExportarCSV exportarCSV = new ExportarCSV();
		    ArrayList<String> listaTXT = new ArrayList<>();

		    ConfigAdicionalesBean confSistemaBean = new ConfigAdicionalesBean();
		    HashMap<String, String> mapaConf = confSistemaBean.obtenerConfiguraciones(session.getEsquemaEmpresa());
		    String labelEmpresa  = Utils.noNulo(mapaConf.get("LABEL_LAYOUT_ORDEN"));
		    String labelMultiple = Utils.noNulo(mapaConf.get("LABEL_LAYOUT_MULTIPLE"));

		    String tipoOrden = "", tipoOrdenAnterior = "";
		    boolean bandMultiple = false;
		    VisorOrdenesForm visorFormTemp = null;

		    for (VisorOrdenesForm v : lista){
		      tipoOrden = v.getTipoOrden();
		      String line1, line2, line3, line4;

		      if ("".equalsIgnoreCase(tipoOrden)){ // orden normal
		        if (bandMultiple){
		          line2 = exportarCSV.generarLine(visorFormTemp, "IVA",     session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line2);
		          line3 = exportarCSV.generarLine(visorFormTemp, "IVA_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line3);
		          line4 = exportarCSV.generarLine(visorFormTemp, "ISR_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line4);
		        }
		        line1 = exportarCSV.generarLine(v, "ORDEN", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line1);
		        line2 = exportarCSV.generarLine(v, "IVA",   session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line2);
		        line3 = exportarCSV.generarLine(v, "IVA_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line3);
		        line4 = exportarCSV.generarLine(v, "ISR_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line4);
		        bandMultiple = false;
		      }else{
		        if (!tipoOrden.equalsIgnoreCase(tipoOrdenAnterior) && bandMultiple){
		          line2 = exportarCSV.generarLine(visorFormTemp, "IVA",     session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line2);
		          line3 = exportarCSV.generarLine(visorFormTemp, "IVA_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line3);
		          line4 = exportarCSV.generarLine(visorFormTemp, "ISR_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line4);
		          line1 = exportarCSV.generarLine(v, "ORDEN", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line1);
		        }else{
		          line1 = exportarCSV.generarLine(v, "ORDEN", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line1);
		        }
		        bandMultiple = true;
		      }
		      tipoOrdenAnterior = tipoOrden;
		      visorFormTemp = v;
		    }

		    if (bandMultiple){
		      String line2 = exportarCSV.generarLine(visorFormTemp, "IVA",     session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line2);
		      String line3 = exportarCSV.generarLine(visorFormTemp, "IVA_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line3);
		      String line4 = exportarCSV.generarLine(visorFormTemp, "ISR_RET", session.getEsquemaEmpresa(), labelEmpresa, labelMultiple); listaTXT.add(line4);
		    }

		    String pathCSV = UtilsPATH.RUTA_PUBLIC_HTML + "LAYOUT/csvFacturas.csv";
		    UtilsFile.crearArchivoSalto(listaTXT, pathCSV);
		    setInputStream(new FileInputStream(new File(pathCSV)));
		    logger.info("**** FIN LAYOUT DX ***************");
		  }catch(Exception e){
		    Utils.imprimeLog("exportarLayOut(): ", e);
		    return ERROR;
		  }finally{
		    try{ if (con!=null) con.close(); }catch(Exception ignore){}
		  }
		  return SUCCESS;
		}

	 
	
	public String getReportFile() {
		return reportFile;
	}


	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}


	public InputStream getInputStream() {
		return inputStream;
	}


	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	

	
	
}
