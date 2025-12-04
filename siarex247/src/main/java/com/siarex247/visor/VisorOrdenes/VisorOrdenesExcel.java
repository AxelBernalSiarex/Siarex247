package com.siarex247.visor.VisorOrdenes;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsColor;

import freemarker.log.Logger;


public class VisorOrdenesExcel {
	public static final Logger logger = Logger.getLogger("siarex247");

	
	public void exportarOrdenes(SXSSFSheet hoja1 , ArrayList<VisorOrdenesForm> listaDetalle, SXSSFWorkbook objLibro, String idLenguaje) {
		  try {
			  
			  LenguajeBean lenBean = LenguajeBean.instance();
			  HashMap <String, String> mapaLen = lenBean.obtenerEtiquetas(idLenguaje, "VISOR");

			  
			  final String[] encabezados = {
					  Utils.regresaCaracteresNormales(mapaLen.get("COL2")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL3")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL4")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL5")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL6")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL7")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL8")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL9")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL10")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL11")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL12")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL13")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL14")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL15")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL24")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL25")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL26")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL27")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL28")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL29")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL30")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL31")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL32")),
					  Utils.regresaCaracteresNormales(mapaLen.get("COL33"))
					  
			  };
			  /*
			  for (int x = 0; x < encabezados.length; x++) {
				  logger.info("Columna["+x+"]===>"+encabezados[x]);
			  }
			   */
				 
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   
			   
			   CellStyle styleTitulo = objLibro.createCellStyle();
			   Font headerFont = objLibro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true);// new java.awt.Color(12, 57, 90)
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
			   
			    
			   
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML - Detalle Ordenes de Compra");
			   monthCell.setCellStyle(styleTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:X1"));
			  
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle Ordenes de Compra");
			   monthCell2.setCellStyle(styleSubTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:X2"));
			    
			  header = hoja1.createRow(2);
			  header.setHeightInPoints(18);

			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			  }
			  
			  Cell celda = null;
				
			  VisorOrdenesForm visorForm = null;
			  Row fila = null;
			  hoja1.setColumnWidth(0, 10000);
			  hoja1.setColumnWidth(1, 5000);
			  hoja1.setColumnWidth(2, 12000);
			  hoja1.setColumnWidth(3, 3500);
			  hoja1.setColumnWidth(4, 3500);
			  hoja1.setColumnWidth(5, 5000);
			  hoja1.setColumnWidth(6, 6000);
			  hoja1.setColumnWidth(7, 3500);
			  hoja1.setColumnWidth(8, 3500);
			  hoja1.setColumnWidth(9, 3500);
			  hoja1.setColumnWidth(10, 3500);
			  hoja1.setColumnWidth(11, 3500);
			  hoja1.setColumnWidth(12, 3500);
			  hoja1.setColumnWidth(13, 3500);
			  hoja1.setColumnWidth(14, 3500);
			  hoja1.setColumnWidth(15, 3500);
			  hoja1.setColumnWidth(16, 3500);
			  hoja1.setColumnWidth(17, 5000);
			  hoja1.setColumnWidth(18, 10000);
			  hoja1.setColumnWidth(19, 6500);
			  hoja1.setColumnWidth(20, 8000);
			  hoja1.setColumnWidth(21, 10000);
			  hoja1.setColumnWidth(22, 5000);
			  hoja1.setColumnWidth(23, 8000);
			  int numRow = 3;
			  
			  
			  
			  for (int x = 0; x < listaDetalle.size(); x++) {
				  visorForm = listaDetalle.get(x);
				  int numCol=0;
				  if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hoja1).flushRows(100); // Retain the last 100 rows in memory
	                }

				   fila = hoja1.createRow(numRow++);
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getRazonSocial());
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getFolioEmpresa());

				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getDescripcion());
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getTipoMoneda());
				   
				   celda = fila.createCell(numCol++);
				   //celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getMonto()));
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getServicioRecibido());
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getDesEstatus() );
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getSerieFolio());
				   
				   celda = fila.createCell(numCol++);
				  // celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getTotal()));
				   
				   celda = fila.createCell(numCol++);
				   //celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getSubTotal()));
				   
				   celda = fila.createCell(numCol++);
				   //celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getIva()));
				   
				   celda = fila.createCell(numCol++);
				  // celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getIva()));
				   
				   celda = fila.createCell(numCol++);
				   //celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getIsrRet()));
				   
				   celda = fila.createCell(numCol++);
				  // celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getImpLocales()));
				   
				   celda = fila.createCell(numCol++);
				   //celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getTotalNC()));
				   
				   celda = fila.createCell(numCol++);
				   //celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getPagoTotal()));
				   
				   celda = fila.createCell(numCol++);
				   //celda.setCellType(0);
				   celda.setCellValue(Utils.convertirDouble(visorForm.getIvaRetNC()));
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getFechaPago());
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getAsignarA());
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getFechaUltimoMovimiento());
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getEstadoCFDI());
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getEstatusCFDI());
				   
				   celda = fila.createCell(numCol++);
				   celda.setCellValue(visorForm.getClaveProducto());
				   
				   celda = null;
			  }
			   
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("toExcel(): ", e);
		  }
	}
	
}
