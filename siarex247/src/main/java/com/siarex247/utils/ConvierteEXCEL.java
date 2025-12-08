package com.siarex247.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.catalogos.sat.Catalogos.CatalogosBean;
import com.siarex247.catalogos.sat.Catalogos.CatalogosForm;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaForm;
import com.siarex247.cumplimientoFiscal.Boveda.XMLForm;
import com.siarex247.cumplimientoFiscal.BovedaEmitidos.BovedaEmitidosForm;
import com.siarex247.cumplimientoFiscal.BovedaNomina.BovedaNominaBean;
import com.siarex247.cumplimientoFiscal.BovedaNomina.BovedaNominaForm;
import com.siarex247.cumplimientoFiscal.BovedaNomina.BovedaNominaQuerys;
import com.siarex247.cumplimientoFiscal.BovedaNomina.NominaForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.validaciones.CartasPorteForm;

public class ConvierteEXCEL {
	
	public static final Logger logger = Logger.getLogger("siarex247");

	
	
	public void xmlToExcelP(HSSFSheet hoja, ArrayList<XMLForm> datosForm, HSSFWorkbook objLibro) {
		 final String[] encabezados = {"UUID", "RFC Receptor", "Nombre Receptor", "RFC Emisor", "Nombre Emisor", "Fecha Comprobante",
				                       "Fecha Timbrado", "Serie", "Folio", "Forma Pago", "Forma Pago Desc", "Método Pago", "Condiciones Pago",
				                       "Lugar Expedición", "Uso CFDI", "Uso CFDI Desc", "Régimen Fiscal", "Régimen Fiscal Desc", "Clave Unidad",
				                       "Clave Unidad Desc", "Clave Prod/Serv", "Cantidad", "Valor Unitario", "Descripción", "Moneda", "Importe",
				                       "Clave Impuesto Tras. Desc.", "Clave Impuesto Tras.", "Base Traslado", "Importe Traslado", "Tasa Cuota Tras.",
				                       "Clave Impuesto Ret. Desc", "Importe Retención", "Tasa Cuota Ret.", "Clave Impuesto Ret.", "Tipo Cambio",
				                       "Subtotal", "Descuento", "Tipo Factor Ret.", "Tipo Factor Tras.", "ImpLoc Total Traslados", "ImpLoc Tasa Trasladado",
				                       "ImpLoc Tasa Retención", "ImpLoc Nombre Retención", "ImpLoc Importe Trasladado", "ImpLoc Total Retenciones",
				                       "ImpLoc Importe Retención", "ImpLoc Nombre Trasladado", "Total Impuestos Retenidos", "Total Impuestos Trasladados",
				                       "Total IVA Trasladado", "Total ISR Retenido", "Total IVA Retenido", "Total IEPS Retenido", "Total IEPS Trasladado",
				                       "Base Retención", "Total", "Núm. Identificación", "Unidad", "Clave Prod/Serv Desc", "Tipo Relación", "Tipo Relación Desc",
				                       "UUID Relacionado", "Fecha de Cancelación del Documento", "Estatus", "Proceso", "Tipo Documento", "Validez", "Estado Pagado",
				                       "Observaciones", "Referencia", "Asociado Bancos", "Asociado Comercial", "Asociado Contabilidad", "Estado de Cancelación del Documento",
				                       "Responsable", "Año Comprobante", "Versión Comprobante", "Mes Comprobante", "Núm. Certificado SAT", "Método Pago Desc",
				                       "Núm. Cta. Pago", "Lugar Expedición Desc", "Año Timbrado", "Mes Timbrado", "Receptor Residencia Fiscal", "Receptor Residencia Fiscal Desc",
				                       "Receptor Núm. Registro Tributario", "Moneda Desc", "Total Descuento", "Tipo Comprobante", "Tipo Comprobante Desc", "Guid", "IdPago",
				                       "Fecha Pago", "Moneda Pago", "Tipo Cambio Pago", "Monto Pago", "DR Serie", "DR Folio", "DR Moneda", "DR Saldo Anterior", "DR Importe Pagado",
				                       "DR Saldo Insoluto", "DR Identificador", "Versión Compl. Pago", "Año Fecha Pago", "Mes Fecha Pago", "Moneda Pago Desc", "Núm. Operación Pago",
				                       "RFC Banco Orden.", "Nombre Banco Extr.", "Cta. Banco Orden.", "RFC  Banco Benef.", "Cta. Banco Benef.", "Tipo Cadena Pago", "Tipo Cadena Pago Desc",
				                       "Certificado Pago", "Cadena Pago", "Sello Pago", "DR Moneda Desc", "DR Tipo Cambio", "DR Método Pago", "DR Método Pago Desc", "DR Núm. Parcialidad"};
		  try {
			   Row header = hoja.createRow(0);
			   header.setHeightInPoints(18);
			   HSSFPalette palette = objLibro.getCustomPalette();
			   
			   HSSFFont fuente = objLibro.createFont();
			   fuente.setFontHeightInPoints((short)10);
			   fuente.setFontName(HSSFFont.FONT_ARIAL);
			   
			   HSSFFont fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(IndexedColors.WHITE.index);

			   HSSFFont fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBold(true);

			   HSSFCellStyle estiloCelda = objLibro.createCellStyle();
			   //estiloCelda.setWrapText(true);
			   estiloCelda.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda.setFont(fuente);
			   estiloCelda.setFillForegroundColor(palette.findSimilarColor((byte) 170, (byte) 218, (byte) 252).getIndex());
			   estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda2 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda2.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda2.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda2.setFont(fuente);
			   estiloCelda2.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   HSSFCellStyle encabezadoPrincipal = objLibro.createCellStyle();
			   //encabezadoPrincipal.setWrapText(true);
			   encabezadoPrincipal.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoPrincipal.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoPrincipal.setFont(fuenteEncabezado);
			   encabezadoPrincipal.setFillForegroundColor(palette.findSimilarColor((byte) 12, (byte) 57, (byte) 90).getIndex());
			   encabezadoPrincipal.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle encabezadoDetalle = objLibro.createCellStyle();
			   //encabezadoDetalle.setWrapText(true);
			   encabezadoDetalle.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoDetalle.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoDetalle.setFont(fuenteEncabezado);
			   encabezadoDetalle.setFillForegroundColor(palette.findSimilarColor((byte) 6, (byte) 103, (byte) 172).getIndex());
			   encabezadoDetalle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda3 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML");
			   monthCell.setCellStyle(encabezadoPrincipal);
			   hoja.addMergedRegion(CellRangeAddress.valueOf("A1:U1"));
			  
			   header = hoja.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle XML");
			    //estiloCelda2.setAlignment(HorizontalAlignment.CENTER);
			   monthCell2.setCellStyle(estiloCelda3);
			   hoja.addMergedRegion(CellRangeAddress.valueOf("A2:U2"));
			    
			  header = hoja.createRow(2);
			  header.setHeightInPoints(18);

			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    monthCell.setCellStyle(encabezadoDetalle);
			  }
			  
			  HSSFCell celda = null;
				
			  XMLForm cargasXMLForm = null;
			  HSSFRow fila = null;

			  for (int x = 0; x < datosForm.size(); x++) {
				  cargasXMLForm = datosForm.get(x);

				   fila = hoja.createRow(x + 3);
				   fila.setHeightInPoints(18);

				   celda = fila.createCell(0);
				   celda.setCellValue(cargasXMLForm.getUuid());

				   celda = fila.createCell(1);
				   celda.setCellValue(cargasXMLForm.getReceptorRFC());

				   celda = fila.createCell(2);
				   celda.setCellValue(cargasXMLForm.getReceptorNombre());

				   celda = fila.createCell(3);
				   celda.setCellValue(cargasXMLForm.getEmisorRFC());

				   celda = fila.createCell(4);
				   celda.setCellValue(cargasXMLForm.getEmisorNombre());
				   
				   celda = fila.createCell(5);
				   celda.setCellValue(cargasXMLForm.getFechaComprobante());

				   celda = fila.createCell(6);
				   celda.setCellValue(cargasXMLForm.getFechaTimbrado());

				   celda = fila.createCell(7);
				   celda.setCellValue(cargasXMLForm.getSerie());

				   celda = fila.createCell(8);
				   celda.setCellValue(cargasXMLForm.getFolio());

				   celda = fila.createCell(9);
				   celda.setCellValue(cargasXMLForm.getFormaPago());
				   
				   celda = fila.createCell(10);
				   celda.setCellValue(cargasXMLForm.getFormaPagoDesc());
				   
				   celda = fila.createCell(11);
				   celda.setCellValue(cargasXMLForm.getMetodoPago());

				   celda = fila.createCell(12);
				   celda.setCellValue(cargasXMLForm.getCondicionesPago());
				   
				   celda = fila.createCell(13);
				   celda.setCellValue(cargasXMLForm.getLugarExpedicion());
				   
				   celda = fila.createCell(14);
				   celda.setCellValue(cargasXMLForm.getUsoCFDI());
				   
				   celda = fila.createCell(15);
				   celda.setCellValue(cargasXMLForm.getUsoCFDIDesc());
				   
				   celda = fila.createCell(16);
				   celda.setCellValue(cargasXMLForm.getRegimenFiscal());
				   
				   celda = fila.createCell(17);
				   celda.setCellValue(cargasXMLForm.getRegimenFiscalDesc());
				   
				   celda = fila.createCell(18);
				   celda.setCellValue(cargasXMLForm.getClaveUnidad());
				   
				   celda = fila.createCell(19);
				   celda.setCellValue(cargasXMLForm.getClaveUnidadDesc());
				   
				   celda = fila.createCell(20);
				   celda.setCellValue(cargasXMLForm.getClaveProdServ());
				   
				   celda = fila.createCell(21);
				   celda.setCellValue(cargasXMLForm.getCantidad());
				   
				   celda = fila.createCell(22);
				   celda.setCellValue(cargasXMLForm.getValorUnitario());
				   
				   celda = fila.createCell(23);
				   celda.setCellValue(cargasXMLForm.getDescripcion());
				   
				   celda = fila.createCell(24);
				   celda.setCellValue(cargasXMLForm.getTipoMoneda());
				   
				   celda = fila.createCell(25);
				   celda.setCellValue(cargasXMLForm.getImporte());
				   
				   celda = fila.createCell(26);
				   celda.setCellValue(cargasXMLForm.getClaveImpuestoTrasDesc());
				   
				   celda = fila.createCell(27);
				   celda.setCellValue(cargasXMLForm.getClaveImpuestoTras());
				   
				   celda = fila.createCell(28);
				   celda.setCellValue(cargasXMLForm.getBaseTraslado());
				   
				   celda = fila.createCell(29);
				   celda.setCellValue("");//cargasXMLForm.getImporteTraslado
				   
				   celda = fila.createCell(30);
				   celda.setCellValue(cargasXMLForm.getTasaCuotaTraslado());
				   
				   celda = fila.createCell(31);
				   celda.setCellValue(cargasXMLForm.getClaveImpuestoRetDesc());
				   
				   celda = fila.createCell(32);
				   celda.setCellValue(cargasXMLForm.getImporteRetencion());
				   
				   celda = fila.createCell(33);
				   celda.setCellValue(cargasXMLForm.getTasaCuotaRet());
				   
				   celda = fila.createCell(34);
				   celda.setCellValue(cargasXMLForm.getClaveImpuestoRet());
				   
				   celda = fila.createCell(35);
				   celda.setCellValue(cargasXMLForm.getTipoCambio());
				   
				   celda = fila.createCell(36);
				   celda.setCellValue(cargasXMLForm.getSubTotal());
				   
				   celda = fila.createCell(37);
				   celda.setCellValue(cargasXMLForm.getDescuento());
				   
				   celda = fila.createCell(38);
				   celda.setCellValue(cargasXMLForm.getTipoFactorRet());
				   
				   celda = fila.createCell(39);
				   celda.setCellValue(cargasXMLForm.getTipoFactorTras());
				   
				   celda = fila.createCell(40);
				   celda.setCellValue(cargasXMLForm.getImpLocTotalTras());
				   
				   celda = fila.createCell(41);
				   celda.setCellValue(cargasXMLForm.getImpLocTasaTras());
				   
				   celda = fila.createCell(42);
				   celda.setCellValue(cargasXMLForm.getImpLocTasaRet());
				   
				   celda = fila.createCell(43);
				   celda.setCellValue(cargasXMLForm.getImpLocNombreRet());
				   
				   celda = fila.createCell(44);
				   celda.setCellValue(cargasXMLForm.getImpLocNombreTras());
				   
				   celda = fila.createCell(45);
				   celda.setCellValue(cargasXMLForm.getImpLocTotalRet());
				   
				   celda = fila.createCell(46);
				   celda.setCellValue(cargasXMLForm.getImpLocImporteRet());
				   
				   celda = fila.createCell(47);
				   celda.setCellValue(cargasXMLForm.getImpLocNombreTras());
				   
				   celda = fila.createCell(48);
				   celda.setCellValue(cargasXMLForm.getTotalImpuestoRet());
				   
				   celda = fila.createCell(49);
				   celda.setCellValue(cargasXMLForm.getTotalImpuestoTranslado());
				   
				   celda = fila.createCell(50);
				   celda.setCellValue(cargasXMLForm.getTotalIvaTras());
				   
				   celda = fila.createCell(51);
				   celda.setCellValue(cargasXMLForm.getTotalIsrRet());
				   
				   celda = fila.createCell(52);
				   celda.setCellValue(cargasXMLForm.getTotalIvaRet());
				   
				   celda = fila.createCell(53);
				   celda.setCellValue(cargasXMLForm.getTotalIepsRet());
				   
				   celda = fila.createCell(54);
				   celda.setCellValue(cargasXMLForm.getTotalIepsTras());
				   
				   celda = fila.createCell(55);
				   celda.setCellValue(cargasXMLForm.getBaseRetencion());
				   
				   celda = fila.createCell(56);
				   celda.setCellValue(cargasXMLForm.getTotal());
				   
				   celda = fila.createCell(57);
				   celda.setCellValue(cargasXMLForm.getNumIdentificacion());
				   
				   celda = fila.createCell(58);
				   celda.setCellValue(cargasXMLForm.getUnidad());
				   
				   celda = fila.createCell(59);
				   celda.setCellValue(cargasXMLForm.getClaveProdServDesc());
				   
				   celda = fila.createCell(60);
				   celda.setCellValue(cargasXMLForm.getTipoRelacion());
				   
				   celda = fila.createCell(61);
				   celda.setCellValue(cargasXMLForm.getTipoRelacionDesc());
				   
				   celda = fila.createCell(62);
				   celda.setCellValue(cargasXMLForm.getUuidRelacionado());
				   
				   celda = fila.createCell(63);
				   celda.setCellValue(cargasXMLForm.getFechaCancelacionDoc());
				   
				   celda = fila.createCell(64);
				   celda.setCellValue(cargasXMLForm.getEstatus());
				   
				   celda = fila.createCell(65);
				   celda.setCellValue(cargasXMLForm.getProceso());
				   
				   celda = fila.createCell(66);
				   celda.setCellValue(cargasXMLForm.getTipoDocumento());
				   
				   celda = fila.createCell(67);
				   celda.setCellValue(cargasXMLForm.getValidez());
				   
				   celda = fila.createCell(68);
				   celda.setCellValue(cargasXMLForm.getEstadoPagado());
				   
				   celda = fila.createCell(69);
				   celda.setCellValue(cargasXMLForm.getObservaciones());
				   
				   celda = fila.createCell(70);
				   celda.setCellValue(cargasXMLForm.getReferencia());
				   
				   celda = fila.createCell(71);
				   celda.setCellValue(cargasXMLForm.getAsociadoBancos());
				   
				   celda = fila.createCell(72);
				   celda.setCellValue(cargasXMLForm.getAsociadoComercial());
				   
				   celda = fila.createCell(73);
				   celda.setCellValue(cargasXMLForm.getAsociadoContabilidad());
				   
				   celda = fila.createCell(74);
				   celda.setCellValue(cargasXMLForm.getEstadoCancelacionDoc());
				   
				   celda = fila.createCell(75);
				   celda.setCellValue(cargasXMLForm.getResponsable());
				   
				   celda = fila.createCell(76);
				   celda.setCellValue(cargasXMLForm.getAnnioComprobante());
				   
				   celda = fila.createCell(77);
				   celda.setCellValue(cargasXMLForm.getVersionComprobante());
				   
				   celda = fila.createCell(78);
				   celda.setCellValue(cargasXMLForm.getMesComprobante());
				   
				   celda = fila.createCell(79);
				   celda.setCellValue(cargasXMLForm.getNumCertificadoSat());
				   
				   celda = fila.createCell(80);
				   celda.setCellValue(cargasXMLForm.getMetodoPagoDesc());
				   
				   celda = fila.createCell(81);
				   celda.setCellValue(cargasXMLForm.getNumeroCuentaPago());
				   
				   celda = fila.createCell(82);
				   celda.setCellValue(cargasXMLForm.getLugarExpDesc());
				   
				   celda = fila.createCell(83);
				   celda.setCellValue(cargasXMLForm.getAnnioTimbrado());
				   
				   celda = fila.createCell(84);
				   celda.setCellValue(cargasXMLForm.getMesTimbrado());
				   
				   celda = fila.createCell(85);
				   celda.setCellValue(cargasXMLForm.getReceptorResidenciaFiscal());
				   
				   celda = fila.createCell(86);
				   celda.setCellValue(cargasXMLForm.getReceptorResidenciaFiscalDesc());
				   
				   celda = fila.createCell(87);
				   celda.setCellValue(cargasXMLForm.getReceptorNumRegistroTributario());
				   
				   celda = fila.createCell(88);
				   celda.setCellValue(cargasXMLForm.getMonedaDesc());
				   
				   celda = fila.createCell(89);
				   celda.setCellValue(cargasXMLForm.getTotalDescuento());
				   
				   celda = fila.createCell(90);
				   celda.setCellValue(cargasXMLForm.getTipoDeComprobante());
				   
				   celda = fila.createCell(91);
				   celda.setCellValue(cargasXMLForm.getTipoDeComprobanteDesc());
				   
				   celda = fila.createCell(92);
				   celda.setCellValue(cargasXMLForm.getGuid());
				   
				   celda = fila.createCell(93);
				   celda.setCellValue(cargasXMLForm.getIdPago());
				   
				   celda = fila.createCell(94);
				   celda.setCellValue(cargasXMLForm.getFechaPago());
				   
				   celda = fila.createCell(95);
				   celda.setCellValue(cargasXMLForm.getTipoMoneda());
				   
				   celda = fila.createCell(96);
				   celda.setCellValue("");//Tipo Cambio Pago
				   
				   celda = fila.createCell(97);
				   celda.setCellValue("");//Monto Pago
				   
				   celda = fila.createCell(98);
				   celda.setCellValue(cargasXMLForm.getDrSerie());
				   
				   celda = fila.createCell(99);
				   celda.setCellValue(cargasXMLForm.getDrFolio());
				   
				   celda = fila.createCell(100);
				   celda.setCellValue(cargasXMLForm.getDrMoneda());
				   
				   celda = fila.createCell(101);
				   celda.setCellValue(cargasXMLForm.getDrSaldoAnterior());
				   
				   celda = fila.createCell(102);
				   celda.setCellValue(cargasXMLForm.getDrImportePagado());
				   
				   celda = fila.createCell(103);
				   celda.setCellValue(cargasXMLForm.getDrSaldoInsoluto());
				   
				   celda = fila.createCell(104);
				   celda.setCellValue(cargasXMLForm.getDrIdentificador());
				   
				   celda = fila.createCell(105);
				   celda.setCellValue(cargasXMLForm.getVersionComplePago());
				   
				   celda = fila.createCell(106);
				   celda.setCellValue(cargasXMLForm.getAnnioFechaPago());
				   
				   celda = fila.createCell(107);
				   celda.setCellValue(cargasXMLForm.getMesFechaPago());
				   
				   celda = fila.createCell(108);
				   celda.setCellValue(cargasXMLForm.getMonedaPagoDesc());
				   
				   celda = fila.createCell(109);
				   celda.setCellValue(cargasXMLForm.getNumOperacionPago());
				   
				   celda = fila.createCell(110);
				   celda.setCellValue(cargasXMLForm.getRfcBancoOrden());
				   
				   celda = fila.createCell(111);
				   celda.setCellValue(cargasXMLForm.getNombreBancoExt());
				   
				   celda = fila.createCell(112);
				   celda.setCellValue(cargasXMLForm.getCtaBancoOrden());
				   
				   celda = fila.createCell(113);
				   celda.setCellValue(cargasXMLForm.getRfcBancoBenef());
				   
				   celda = fila.createCell(114);
				   celda.setCellValue("");//Cta. Banco Benef.
				   
				   celda = fila.createCell(115);
				   celda.setCellValue(cargasXMLForm.getTipoCadenaPago());
				   
				   celda = fila.createCell(116);
				   celda.setCellValue(cargasXMLForm.getTipoCadenaPagoDesc());
				   
				   celda = fila.createCell(117);
				   celda.setCellValue(cargasXMLForm.getCertificadoPago());
				   
				   celda = fila.createCell(118);
				   celda.setCellValue(cargasXMLForm.getCadenaPago());
				   
				   celda = fila.createCell(119);
				   celda.setCellValue(cargasXMLForm.getSelloPago());
				   
				   celda = fila.createCell(120);
				   celda.setCellValue(cargasXMLForm.getDrMonedaDesc());
				   
				   celda = fila.createCell(121);
				   celda.setCellValue(cargasXMLForm.getDrTipoCambio());
				   
				   celda = fila.createCell(122);
				   celda.setCellValue(cargasXMLForm.getDrMetodoPago());
				   
				   celda = fila.createCell(123);
				   celda.setCellValue(cargasXMLForm.getDrMetodoPagoDesc());
				   
				   celda = fila.createCell(124);
				   celda.setCellValue(cargasXMLForm.getDrNumParcialidad());
				   
				   celda = null;
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("xmlToExcelP(): ", e);
		  }
	}
	
	public void xmlToExcelI(HSSFSheet hoja, ArrayList<XMLForm> datosForm, HSSFWorkbook objLibro) {
		 final String[] encabezados = {"UUID", "RFC Receptor", "Nombre Receptor", "RFC Emisor", "Nombre Emisor", "Fecha Comprobante",
				                       "Fecha Timbrado", "Serie", "Folio", "Forma Pago", "Forma Pago Desc", "Método Pago", "Condiciones Pago",
				                       "Lugar Expedición", "Uso CFDI", "Uso CFDI Desc", "Régimen Fiscal", "Régimen Fiscal Desc", "Clave Unidad",
				                       "Clave Unidad Desc", "Clave Prod/Serv", "Cantidad", "Valor Unitario", "Descripción", "Moneda", "Importe",
				                       "Clave Impuesto Tras. Desc.", "Clave Impuesto Tras.", "Base Traslado", "Importe Traslado", "Tasa Cuota Tras.",
				                       "Clave Impuesto Ret. Desc", "Importe Retención", "Tasa Cuota Ret.", "Clave Impuesto Ret.", "Tipo Cambio",
				                       "Subtotal", "Descuento", "Tipo Factor Ret.", "Tipo Factor Tras.", "ImpLoc Total Traslados", "ImpLoc Tasa Trasladado",
				                       "ImpLoc Tasa Retención", "ImpLoc Nombre Retención", "ImpLoc Importe Trasladado", "ImpLoc Total Retenciones",
				                       "ImpLoc Importe Retención", "ImpLoc Nombre Trasladado", "Total Impuestos Retenidos", "Total Impuestos Trasladados",
				                       "Total IVA Trasladado", "Total ISR Retenido", "Total IVA Retenido", "Total IEPS Retenido", "Total IEPS Trasladado",
				                       "Base Retención", "Total", "Núm. Identificación", "Unidad", "Clave Prod/Serv Desc", "Tipo Relación", "Tipo Relación Desc",
				                       "UUID Relacionado", "Fecha de Cancelación del Documento", "Estatus", "Proceso", "Tipo Documento", "Validez", "Estado Pagado",
				                       "Observaciones", "Referencia", "Asociado Bancos", "Asociado Comercial", "Asociado Contabilidad", "Estado de Cancelación del Documento",
				                       "Responsable", "Año Comprobante", "Versión Comprobante", "Mes Comprobante", "Núm. Certificado SAT", "Método Pago Desc",
				                       "Núm. Cta. Pago", "Lugar Expedición Desc", "Año Timbrado", "Mes Timbrado", "Receptor Residencia Fiscal", "Receptor Residencia Fiscal Desc",
				                       "Receptor Núm. Registro Tributario", "Moneda Desc", "Total Descuento", "Tipo Comprobante", "Tipo Comprobante Desc", "Guid", "IdPago"};
		  try {
			   Row header = hoja.createRow(0);
			   header.setHeightInPoints(18);
			   HSSFPalette palette = objLibro.getCustomPalette();
			   
			   HSSFFont fuente = objLibro.createFont();
			   fuente.setFontHeightInPoints((short)10);
			   fuente.setFontName(HSSFFont.FONT_ARIAL);
			   
			   HSSFFont fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(IndexedColors.WHITE.index);

			   HSSFFont fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBold(true);

			   HSSFCellStyle estiloCelda = objLibro.createCellStyle();
			   //estiloCelda.setWrapText(true);
			   estiloCelda.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda.setFont(fuente);
			   estiloCelda.setFillForegroundColor(palette.findSimilarColor((byte) 170, (byte) 218, (byte) 252).getIndex());
			   estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda2 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda2.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda2.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda2.setFont(fuente);
			   estiloCelda2.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   HSSFCellStyle encabezadoPrincipal = objLibro.createCellStyle();
			   //encabezadoPrincipal.setWrapText(true);
			   encabezadoPrincipal.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoPrincipal.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoPrincipal.setFont(fuenteEncabezado);
			   encabezadoPrincipal.setFillForegroundColor(palette.findSimilarColor((byte) 12, (byte) 57, (byte) 90).getIndex());
			   encabezadoPrincipal.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle encabezadoDetalle = objLibro.createCellStyle();
			   //encabezadoDetalle.setWrapText(true);
			   encabezadoDetalle.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoDetalle.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoDetalle.setFont(fuenteEncabezado);
			   encabezadoDetalle.setFillForegroundColor(palette.findSimilarColor((byte) 6, (byte) 103, (byte) 172).getIndex());
			   encabezadoDetalle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda3 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML");
			   monthCell.setCellStyle(encabezadoPrincipal);
			   hoja.addMergedRegion(CellRangeAddress.valueOf("A1:U1"));
			  
			   header = hoja.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle XML");
			    //estiloCelda2.setAlignment(HorizontalAlignment.CENTER);
			   monthCell2.setCellStyle(estiloCelda3);
			   hoja.addMergedRegion(CellRangeAddress.valueOf("A2:U2"));
			    
			  header = hoja.createRow(2);
			  header.setHeightInPoints(18);

			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    monthCell.setCellStyle(encabezadoDetalle);
			  }
			  
			  HSSFCell celda = null;
				
			  XMLForm cargasXMLForm = null;
			  HSSFRow fila = null;

			  for (int x = 0; x < datosForm.size(); x++) {
				  cargasXMLForm = datosForm.get(x);

				   fila = hoja.createRow(x + 3);
				   fila.setHeightInPoints(18);

				   celda = fila.createCell(0);
				   celda.setCellValue(cargasXMLForm.getUuid());

				   celda = fila.createCell(1);
				   celda.setCellValue(cargasXMLForm.getReceptorRFC());

				   celda = fila.createCell(2);
				   celda.setCellValue(cargasXMLForm.getReceptorNombre());

				   celda = fila.createCell(3);
				   celda.setCellValue(cargasXMLForm.getEmisorRFC());

				   celda = fila.createCell(4);
				   celda.setCellValue(cargasXMLForm.getEmisorNombre());
				   
				   celda = fila.createCell(5);
				   celda.setCellValue(cargasXMLForm.getFechaComprobante());

				   celda = fila.createCell(6);
				   celda.setCellValue(cargasXMLForm.getFechaTimbrado());

				   celda = fila.createCell(7);
				   celda.setCellValue(cargasXMLForm.getSerie());

				   celda = fila.createCell(8);
				   celda.setCellValue(cargasXMLForm.getFolio());

				   celda = fila.createCell(9);
				   celda.setCellValue(cargasXMLForm.getFormaPago());
				   
				   celda = fila.createCell(10);
				   celda.setCellValue(cargasXMLForm.getFormaPagoDesc());
				   
				   celda = fila.createCell(11);
				   celda.setCellValue(cargasXMLForm.getMetodoPago());

				   celda = fila.createCell(12);
				   celda.setCellValue(cargasXMLForm.getCondicionesPago());
				   
				   celda = fila.createCell(13);
				   celda.setCellValue(cargasXMLForm.getLugarExpedicion());
				   
				   celda = fila.createCell(14);
				   celda.setCellValue(cargasXMLForm.getUsoCFDI());
				   
				   celda = fila.createCell(15);
				   celda.setCellValue(cargasXMLForm.getUsoCFDIDesc());
				   
				   celda = fila.createCell(16);
				   celda.setCellValue(cargasXMLForm.getRegimenFiscal());
				   
				   celda = fila.createCell(17);
				   celda.setCellValue(cargasXMLForm.getRegimenFiscalDesc());
				   
				   celda = fila.createCell(18);
				   celda.setCellValue(cargasXMLForm.getClaveUnidad());
				   
				   celda = fila.createCell(19);
				   celda.setCellValue(cargasXMLForm.getClaveUnidadDesc());
				   
				   celda = fila.createCell(20);
				   celda.setCellValue(cargasXMLForm.getClaveProdServ());
				   
				   celda = fila.createCell(21);
				   celda.setCellValue(cargasXMLForm.getCantidad());
				   
				   celda = fila.createCell(22);
				   celda.setCellValue(cargasXMLForm.getValorUnitario());
				   
				   celda = fila.createCell(23);
				   celda.setCellValue(cargasXMLForm.getDescripcion());
				   
				   celda = fila.createCell(24);
				   celda.setCellValue(cargasXMLForm.getTipoMoneda());
				   
				   celda = fila.createCell(25);
				   celda.setCellValue(cargasXMLForm.getImporte());
				   
				   celda = fila.createCell(26);
				   celda.setCellValue(cargasXMLForm.getClaveImpuestoTrasDesc());
				   
				   celda = fila.createCell(27);
				   celda.setCellValue(cargasXMLForm.getClaveImpuestoTras());
				   
				   celda = fila.createCell(28);
				   celda.setCellValue(cargasXMLForm.getBaseTraslado());
				   
				   celda = fila.createCell(29);
				   celda.setCellValue("");//cargasXMLForm.getImporteTraslado
				   
				   celda = fila.createCell(30);
				   celda.setCellValue(cargasXMLForm.getTasaCuotaTraslado());
				   
				   celda = fila.createCell(31);
				   celda.setCellValue(cargasXMLForm.getClaveImpuestoRetDesc());
				   
				   celda = fila.createCell(32);
				   celda.setCellValue(cargasXMLForm.getImporteRetencion());
				   
				   celda = fila.createCell(33);
				   celda.setCellValue(cargasXMLForm.getTasaCuotaRet());
				   
				   celda = fila.createCell(34);
				   celda.setCellValue(cargasXMLForm.getClaveImpuestoRet());
				   
				   celda = fila.createCell(35);
				   celda.setCellValue(cargasXMLForm.getTipoCambio());
				   
				   celda = fila.createCell(36);
				   celda.setCellValue(cargasXMLForm.getSubTotal());
				   
				   celda = fila.createCell(37);
				   celda.setCellValue(cargasXMLForm.getDescuento());
				   
				   celda = fila.createCell(38);
				   celda.setCellValue(cargasXMLForm.getTipoFactorRet());
				   
				   celda = fila.createCell(39);
				   celda.setCellValue(cargasXMLForm.getTipoFactorTras());
				   
				   celda = fila.createCell(40);
				   celda.setCellValue(cargasXMLForm.getImpLocTotalTras());
				   
				   celda = fila.createCell(41);
				   celda.setCellValue(cargasXMLForm.getImpLocTasaTras());
				   
				   celda = fila.createCell(42);
				   celda.setCellValue(cargasXMLForm.getImpLocTasaRet());
				   
				   celda = fila.createCell(43);
				   celda.setCellValue(cargasXMLForm.getImpLocNombreRet());
				   
				   celda = fila.createCell(44);
				   celda.setCellValue(cargasXMLForm.getImpLocNombreTras());
				   
				   celda = fila.createCell(45);
				   celda.setCellValue(cargasXMLForm.getImpLocTotalRet());
				   
				   celda = fila.createCell(46);
				   celda.setCellValue(cargasXMLForm.getImpLocImporteRet());
				   
				   celda = fila.createCell(47);
				   celda.setCellValue(cargasXMLForm.getImpLocNombreTras());
				   
				   celda = fila.createCell(48);
				   celda.setCellValue(cargasXMLForm.getTotalImpuestoRet());
				   
				   celda = fila.createCell(49);
				   celda.setCellValue(cargasXMLForm.getTotalImpuestoTranslado());
				   
				   celda = fila.createCell(50);
				   celda.setCellValue(cargasXMLForm.getTotalIvaTras());
				   
				   celda = fila.createCell(51);
				   celda.setCellValue(cargasXMLForm.getTotalIsrRet());
				   
				   celda = fila.createCell(52);
				   celda.setCellValue(cargasXMLForm.getTotalIvaRet());
				   
				   celda = fila.createCell(53);
				   celda.setCellValue(cargasXMLForm.getTotalIepsRet());
				   
				   celda = fila.createCell(54);
				   celda.setCellValue(cargasXMLForm.getTotalIepsTras());
				   
				   celda = fila.createCell(55);
				   celda.setCellValue(cargasXMLForm.getBaseRetencion());
				   
				   celda = fila.createCell(56);
				   celda.setCellValue(cargasXMLForm.getTotal());
				   
				   celda = fila.createCell(57);
				   celda.setCellValue(cargasXMLForm.getNumIdentificacion());
				   
				   celda = fila.createCell(58);
				   celda.setCellValue(cargasXMLForm.getUnidad());
				   
				   celda = fila.createCell(59);
				   celda.setCellValue(cargasXMLForm.getClaveProdServDesc());
				   
				   celda = fila.createCell(60);
				   celda.setCellValue(cargasXMLForm.getTipoRelacion());
				   
				   celda = fila.createCell(61);
				   celda.setCellValue(cargasXMLForm.getTipoRelacionDesc());
				   
				   celda = fila.createCell(62);
				   celda.setCellValue(cargasXMLForm.getUuidRelacionado());
				   
				   celda = fila.createCell(63);
				   celda.setCellValue(cargasXMLForm.getFechaCancelacionDoc());
				   
				   celda = fila.createCell(64);
				   celda.setCellValue(cargasXMLForm.getEstatus());
				   
				   celda = fila.createCell(65);
				   celda.setCellValue(cargasXMLForm.getProceso());
				   
				   celda = fila.createCell(66);
				   celda.setCellValue(cargasXMLForm.getTipoDocumento());
				   
				   celda = fila.createCell(67);
				   celda.setCellValue(cargasXMLForm.getValidez());
				   
				   celda = fila.createCell(68);
				   celda.setCellValue(cargasXMLForm.getEstadoPagado());
				   
				   celda = fila.createCell(69);
				   celda.setCellValue(cargasXMLForm.getObservaciones());
				   
				   celda = fila.createCell(70);
				   celda.setCellValue(cargasXMLForm.getReferencia());
				   
				   celda = fila.createCell(71);
				   celda.setCellValue(cargasXMLForm.getAsociadoBancos());
				   
				   celda = fila.createCell(72);
				   celda.setCellValue(cargasXMLForm.getAsociadoComercial());
				   
				   celda = fila.createCell(73);
				   celda.setCellValue(cargasXMLForm.getAsociadoContabilidad());
				   
				   celda = fila.createCell(74);
				   celda.setCellValue(cargasXMLForm.getEstadoCancelacionDoc());
				   
				   celda = fila.createCell(75);
				   celda.setCellValue(cargasXMLForm.getResponsable());
				   
				   celda = fila.createCell(76);
				   celda.setCellValue(cargasXMLForm.getAnnioComprobante());
				   
				   celda = fila.createCell(77);
				   celda.setCellValue(cargasXMLForm.getVersionComprobante());
				   
				   celda = fila.createCell(78);
				   celda.setCellValue(cargasXMLForm.getMesComprobante());
				   
				   celda = fila.createCell(79);
				   celda.setCellValue(cargasXMLForm.getNumCertificadoSat());
				   
				   celda = fila.createCell(80);
				   celda.setCellValue(cargasXMLForm.getMetodoPagoDesc());
				   
				   celda = fila.createCell(81);
				   celda.setCellValue(cargasXMLForm.getNumeroCuentaPago());
				   
				   celda = fila.createCell(82);
				   celda.setCellValue(cargasXMLForm.getLugarExpDesc());
				   
				   celda = fila.createCell(83);
				   celda.setCellValue(cargasXMLForm.getAnnioTimbrado());
				   
				   celda = fila.createCell(84);
				   celda.setCellValue(cargasXMLForm.getMesTimbrado());
				   
				   celda = fila.createCell(85);
				   celda.setCellValue(cargasXMLForm.getReceptorResidenciaFiscal());
				   
				   celda = fila.createCell(86);
				   celda.setCellValue(cargasXMLForm.getReceptorResidenciaFiscalDesc());
				   
				   celda = fila.createCell(87);
				   celda.setCellValue(cargasXMLForm.getReceptorNumRegistroTributario());
				   
				   celda = fila.createCell(88);
				   celda.setCellValue(cargasXMLForm.getMonedaDesc());
				   
				   celda = fila.createCell(89);
				   celda.setCellValue(cargasXMLForm.getTotalDescuento());
				   
				   celda = fila.createCell(90);
				   celda.setCellValue(cargasXMLForm.getTipoDeComprobante());
				   
				   celda = fila.createCell(91);
				   celda.setCellValue(cargasXMLForm.getTipoDeComprobanteDesc());
				   
				   celda = fila.createCell(92);
				   celda.setCellValue(cargasXMLForm.getGuid());
				   
				   celda = fila.createCell(93);
				   celda.setCellValue(cargasXMLForm.getIdPago());
				   
				   celda = null;
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("xmlToExcelI(): ", e);
		  }
	}
	
	public void toExcel(SXSSFSheet hoja1 , ArrayList<BovedaForm> datosBoveda, SXSSFWorkbook objLibro, int regInicial, int regFinal, String idLenguaje) {
		  try {
			  
			  LenguajeBean lenguajeBean = LenguajeBean.instance();
		  	  HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(idLenguaje, "BOVEDA");
		  	  /*
				 final String[] encabezados = {"RFC", "Razon Social", "Serie", "Tipo de Comprobante", "Folio", "Total",
	                       "Sub-Total", "IVA", "IVA Ret.", "ISR Ret.", "Imp. Locales", "UUID", "Fecha Factura"};
		  	  */
		  	  
			  final String[] encabezados = {mapaLenguaje.get("ETQ12"), mapaLenguaje.get("ETQ13"), mapaLenguaje.get("ETQ14"), mapaLenguaje.get("ETQ15"), mapaLenguaje.get("ETQ16"), mapaLenguaje.get("ETQ17"),
					  mapaLenguaje.get("ETQ18"), mapaLenguaje.get("ETQ19"), mapaLenguaje.get("ETQ20"), mapaLenguaje.get("ETQ21"), mapaLenguaje.get("ETQ22"), mapaLenguaje.get("ETQ25"), mapaLenguaje.get("ETQ26")};

				 
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   
			   
			   CellStyle styleTitulo = objLibro.createCellStyle();
			   Font headerFont = objLibro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true); // new java.awt.Color(12, 57, 90)
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
			   monthCell.setCellValue("Sistema de Recepcion de XML - Boveda Recibidos");
			   monthCell.setCellStyle(styleTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:M1"));
			  
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle Boveda XML");
			   monthCell2.setCellStyle(styleSubTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:M2"));
			    
			  header = hoja1.createRow(2);
			  header.setHeightInPoints(18);

			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			  }
			  
			  Cell celda = null;
				
			  BovedaForm bovedaForm = null;
			  Row fila = null;
			  hoja1.setColumnWidth(0, 5000);
			  hoja1.setColumnWidth(1, 10000);
			  hoja1.setColumnWidth(2, 3500);
			  hoja1.setColumnWidth(3, 5000);
			  hoja1.setColumnWidth(4, 3500);
			  hoja1.setColumnWidth(5, 3500);
			  hoja1.setColumnWidth(6, 3500);
			  hoja1.setColumnWidth(7, 3500);
			  hoja1.setColumnWidth(8, 3500);
			  hoja1.setColumnWidth(9, 3500);
			  hoja1.setColumnWidth(10, 3500);
			  hoja1.setColumnWidth(11, 10000);
			  hoja1.setColumnWidth(12, 6000);
			  int numRow = 3;
			  for (int x = regInicial; x < regFinal; x++) {
				  bovedaForm = datosBoveda.get(x);
				  if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hoja1).flushRows(100); // Retain the last 100 rows in memory
	                }

				  
				   fila = hoja1.createRow(numRow++);
				  // fila.setHeightInPoints(18);
				   celda = fila.createCell(0);
				   celda.setCellValue(bovedaForm.getRfc());

				   celda = fila.createCell(1);
				   celda.setCellValue(bovedaForm.getRazonSocial());

				   celda = fila.createCell(2);
				   celda.setCellValue(bovedaForm.getSerie());

				   celda = fila.createCell(3);
				   celda.setCellValue(bovedaForm.getTipoComprobante());

				   celda = fila.createCell(4);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getFolio()));
				   
				   celda = fila.createCell(5);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getTotal()));

				   celda = fila.createCell(6);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getSubTotal()));

				   celda = fila.createCell(7);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getIva()));

				   celda = fila.createCell(8);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getRetIVA()));

				   celda = fila.createCell(9);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getRetISR()));
				   
				   celda = fila.createCell(10);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getImpLocales()));
				   
				   celda = fila.createCell(11);
				   celda.setCellValue(bovedaForm.getUuid());

				   celda = fila.createCell(12);
				   celda.setCellValue(bovedaForm.getFechaFactura());
				   
				   celda = null;
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("toExcel(): ", e);
		  }
	}
	
	
	/*
	public void toExcel(XSSFSheet hoja1 , ArrayList<BovedaForm> datosBoveda, XSSFWorkbook objLibro) {
		 final String[] encabezados = {"RFC", "Razon Social", "Serie", "Tipo de Comprobante", "Folio", "Total",
				                       "Sub-Total", "IVA", "IVA Ret.", "ISR Ret.", "Imp. Locales", "UUID", "Fecha Factura"};
		  try {
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   HSSFPalette palette = objLibro.getCustomPalette();
			   
			   HSSFFont fuente = objLibro.createFont();
			   fuente.setFontHeightInPoints((short)10);
			   fuente.setFontName(HSSFFont.FONT_ARIAL);
			   
			   HSSFFont fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(IndexedColors.WHITE.index);

			   HSSFFont fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			   HSSFCellStyle estiloCelda = objLibro.createCellStyle();
			   //estiloCelda.setWrapText(true);
			   estiloCelda.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda.setFont(fuente);
			   estiloCelda.setFillForegroundColor(palette.findSimilarColor((byte) 170, (byte) 218, (byte) 252).getIndex());
			   estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda2 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda2.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda2.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda2.setFont(fuente);
			   estiloCelda2.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   HSSFCellStyle encabezadoPrincipal = objLibro.createCellStyle();
			   //encabezadoPrincipal.setWrapText(true);
			   encabezadoPrincipal.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoPrincipal.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoPrincipal.setFont(fuenteEncabezado);
			   encabezadoPrincipal.setFillForegroundColor(palette.findSimilarColor((byte) 12, (byte) 57, (byte) 90).getIndex());
			   encabezadoPrincipal.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle encabezadoDetalle = objLibro.createCellStyle();
			   //encabezadoDetalle.setWrapText(true);
			   encabezadoDetalle.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoDetalle.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoDetalle.setFont(fuenteEncabezado);
			   encabezadoDetalle.setFillForegroundColor(palette.findSimilarColor((byte) 6, (byte) 103, (byte) 172).getIndex());
			   encabezadoDetalle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda3 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML - Boveda Recibidos");
			   monthCell.setCellStyle(encabezadoPrincipal);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:M1"));
			  
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle Boveda XML");
			    //estiloCelda2.setAlignment(HorizontalAlignment.CENTER);
			   monthCell2.setCellStyle(estiloCelda3);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:M2"));
			    
			  header = hoja1.createRow(2);
			  header.setHeightInPoints(18);

			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    monthCell.setCellStyle(encabezadoDetalle);
			  }
			  
			  HSSFCell celda = null;
				
			  BovedaForm bovedaForm = null;
			  HSSFRow fila = null;
			  //boolean bandRen = true;
			  //HSSFCellStyle estiloCeldaFinal = null;

			  hoja1.setColumnWidth(0, 5000);
			  hoja1.setColumnWidth(1, 10000);
			  hoja1.setColumnWidth(2, 3500);
			  hoja1.setColumnWidth(3, 5000);
			  hoja1.setColumnWidth(4, 3500);
			  hoja1.setColumnWidth(5, 3500);
			  hoja1.setColumnWidth(6, 3500);
			  hoja1.setColumnWidth(7, 3500);
			  hoja1.setColumnWidth(8, 3500);
			  hoja1.setColumnWidth(9, 3500);
			  hoja1.setColumnWidth(10, 3500);
			  hoja1.setColumnWidth(11, 10000);
			  hoja1.setColumnWidth(12, 6000);
			  
			  for (int x = 0; x < datosBoveda.size(); x++) {
				  bovedaForm = datosBoveda.get(x);
				  
				   fila = hoja1.createRow(x + 3);
				   fila.setHeightInPoints(18);
				  
				   celda = fila.createCell(0);
				   //celda.setCellStyle(estiloCeldaFinal);
				   // celda.setCellType(CellType.STRING);
				   celda.setCellValue(bovedaForm.getRfc());

				   celda = fila.createCell(1);
				   //celda.setCellStyle(estiloCeldaFinal);
				   // celda.setCellType(CellType.STRING);
				   celda.setCellValue(bovedaForm.getRazonSocial());

				   celda = fila.createCell(2);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(bovedaForm.getSerie());

				   celda = fila.createCell(3);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(bovedaForm.getTipoComprobante());

				   celda = fila.createCell(4);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getFolio()));
				   
				   celda = fila.createCell(5);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getTotal()));

				   celda = fila.createCell(6);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getSubTotal()));

				   celda = fila.createCell(7);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getIva()));

				   celda = fila.createCell(8);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getRetIVA()));

				   celda = fila.createCell(9);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getRetISR()));
				   
				   celda = fila.createCell(10);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getImpLocales()));
				   
				   celda = fila.createCell(11);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(bovedaForm.getUuid());

				   celda = fila.createCell(12);
				   //celda.setCellStyle(estiloCeldaFinal);
				   //celda.setCellType(CellType.STRING);
				   celda.setCellValue(bovedaForm.getFechaFactura());
				   
				   celda = null;
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("toExcel(): ", e);
		  }
	}
	
	*/
	
	public void toExcelEmitidos(SXSSFSheet hoja1 , ArrayList<BovedaEmitidosForm> datosBoveda, SXSSFWorkbook objLibro, int regIncial, int regFinal, String lenguaje) {
		 
		  try {
		  		LenguajeBean lenguajeBean = LenguajeBean.instance();
		  		HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(lenguaje, "BOVEDA_EMITIDOS");
			  
			  
//				final String[] encabezados = {"RFC Receptor", "Razon Social Receptor", "Serie", "Tipo de Comprobante", "Folio", "Total",
//	                       "Sub-Total", "Total Retenciones", "Total Traslado.", "UUID", "Fecha Factura"};
			  
				final String[] encabezados = {mapaLenguaje.get("ETQ7"), mapaLenguaje.get("ETQ8"), mapaLenguaje.get("ETQ13"), mapaLenguaje.get("ETQ11"), mapaLenguaje.get("ETQ14"), mapaLenguaje.get("ETQ21"),
						mapaLenguaje.get("ETQ22"), mapaLenguaje.get("ETQ23"), mapaLenguaje.get("ETQ24"), mapaLenguaje.get("ETQ27"), mapaLenguaje.get("ETQ28")};
			  
			   Row header = hoja1.createRow(0);
			   

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
		       
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML - Boveda Emitidos");
			   monthCell.setCellStyle(styleTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:K1"));
			  
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle Boveda XML");
			   monthCell2.setCellStyle(styleSubTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:K2"));
			    
			  header = hoja1.createRow(2);
			  header.setHeightInPoints(18);
			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			  }
			  Cell celda = null;
			  BovedaEmitidosForm bovedaForm = null;
			  Row fila = null;
			  //boolean bandRen = true;
			  //HSSFCellStyle estiloCeldaFinal = null;

			  hoja1.setColumnWidth(0, 5000);
			  hoja1.setColumnWidth(1, 10000);
			  hoja1.setColumnWidth(2, 3500);
			  hoja1.setColumnWidth(3, 5000);
			  hoja1.setColumnWidth(4, 3500);
			  hoja1.setColumnWidth(5, 3500);
			  hoja1.setColumnWidth(6, 3500);
			  hoja1.setColumnWidth(7, 4500);
			  hoja1.setColumnWidth(8, 4500);
			  hoja1.setColumnWidth(9, 10000);
			  hoja1.setColumnWidth(10, 6000);
			  int numRow = 3;
			  for (int x = regIncial; x < regFinal; x++) {
				   bovedaForm = datosBoveda.get(x);
				   if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hoja1).flushRows(100); // Retain the last 100 rows in memory
	                }
				   fila = hoja1.createRow(numRow++);
				   // fila.setHeightInPoints(18);
				   
				   celda = fila.createCell(0);
				   celda.setCellValue(bovedaForm.getRfcReceptor());

				   celda = fila.createCell(1);
				   celda.setCellValue(bovedaForm.getRazonSocialReceptor());

				   celda = fila.createCell(2);
				   celda.setCellValue(bovedaForm.getSerie());

				   celda = fila.createCell(3);
				   celda.setCellValue(bovedaForm.getTipoComprobante());

				   celda = fila.createCell(4);
				   celda.setCellValue(bovedaForm.getFolio());
				   
				   celda = fila.createCell(5);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getTotal()));

				   celda = fila.createCell(6);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getSubTotal()));

				   celda = fila.createCell(7);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getTotalImpuestoRetenido()));

				   celda = fila.createCell(8);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getTotalImpuestoTraslado()));

				   
				   celda = fila.createCell(9);
				   celda.setCellValue(bovedaForm.getUuid());

				   celda = fila.createCell(10);
				   celda.setCellValue(bovedaForm.getFechaFactura());
				   
				   celda = null;
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("toExcel(): ", e);
		  }
	}
	
	public void toExcelNomina(SXSSFSheet hoja1, ArrayList<BovedaNominaForm> datosBoveda, 
			SXSSFWorkbook objLibro, int regInicial, int regFinal, String lenguaje) {
		  try {
			  
		  	   LenguajeBean lenguajeBean = LenguajeBean.instance();
		  	   HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(lenguaje, "BOVEDA_NOMINA");

//				 final String[] encabezados = {"RFC Receptor", "Razon Social Receptor", "Serie", "Tipo de Comprobante", "Folio", "Total",
//	                       "Sub-Total", "Descuento", "Total Percepciones", "Total Deducciones.", "UUID", "Fecha Factura"};

				 final String[] encabezados = {mapaLenguaje.get("ETQ16"), mapaLenguaje.get("ETQ17"), mapaLenguaje.get("ETQ18"), mapaLenguaje.get("ETQ19"), mapaLenguaje.get("ETQ20"),
						 mapaLenguaje.get("ETQ21"), mapaLenguaje.get("ETQ22"), mapaLenguaje.get("ETQ23"), mapaLenguaje.get("ETQ24"), mapaLenguaje.get("ETQ27"), mapaLenguaje.get("ETQ28")};

				 
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   
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
			   
		       
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML - Boveda Nomina");
			   monthCell.setCellStyle(styleTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:K1"));
			   
			   
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle Boveda XML");
			   monthCell2.setCellStyle(styleSubTitulo);
			    //estiloCelda2.setAlignment(HorizontalAlignment.CENTER);
			   //monthCell2.setCellStyle(estiloCelda3);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:K2"));
			    
			  header = hoja1.createRow(2);
			  header.setHeightInPoints(18);
			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    // monthCell.setCellStyle(encabezadoDetalle);
			    		    
			  }
			  hoja1.trackAllColumnsForAutoSizing();
			  
			  Cell celda = null;
			  BovedaNominaForm bovedaForm = null;
			  Row fila = null;
			  //boolean bandRen = true;
			  //HSSFCellStyle estiloCeldaFinal = null;

			  hoja1.setColumnWidth(0, 5000);
			  hoja1.setColumnWidth(1, 10000);
			  hoja1.setColumnWidth(2, 3500);
			  hoja1.setColumnWidth(3, 3500);
			  hoja1.setColumnWidth(4, 3500);
			  hoja1.setColumnWidth(5, 3500);
			  hoja1.setColumnWidth(6, 4500);
			  hoja1.setColumnWidth(7, 4500);
			  hoja1.setColumnWidth(8, 4500);
			  hoja1.setColumnWidth(9, 10000);
			  hoja1.setColumnWidth(10, 6000);
			  
			  int numRow = 3;
			  for (int x = regInicial; x < regFinal; x++) {
				   bovedaForm = datosBoveda.get(x);
				   fila = hoja1.createRow(numRow++);

				   if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hoja1).flushRows(100); // Retain the last 100 rows in memory
	                }

				   
				   celda = fila.createCell(0);
				   celda.setCellValue(bovedaForm.getRfcReceptor());

				   celda = fila.createCell(1);
				   celda.setCellValue(bovedaForm.getRazonSocialReceptor());

				   celda = fila.createCell(2);
				   celda.setCellValue(bovedaForm.getSerie());

				   celda = fila.createCell(3);
				   celda.setCellValue(bovedaForm.getFolio());
				   
				   celda = fila.createCell(4);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getTotal()));

				   celda = fila.createCell(5);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getSubTotal()));

				   celda = fila.createCell(6);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getDescuento()));

				   celda = fila.createCell(7);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getTotalPercepciones()));

				   celda = fila.createCell(8);
				   celda.setCellValue(Utils.convertirDouble(bovedaForm.getTotalDeducciones()));
				   
				   celda = fila.createCell(9);
				   celda.setCellValue(bovedaForm.getUuid());

				   celda = fila.createCell(10);
				   celda.setCellValue(bovedaForm.getFechaFactura());
				   
				   celda = null;
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("toExcel(): ", e);
		  }
	}

	
	public void toExcelNominaResumen(XSSFSheet hoja1 , ArrayList<BovedaNominaForm> datosBoveda, XSSFWorkbook objLibro, 
			int regInicial, int regFinal) {
		 
		
		
		final String[] encabezados = {"Tipo", "Efecto", "UUID", "Emision", "Timbrado", "RFC",
				                       "Razon Social", "RFC", "Razon Social", "Total"};
		  try {
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   
			   XSSFCellStyle styleTitulo = objLibro.createCellStyle();
			   XSSFFont headerFont = objLibro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Calibri");
		       headerFont.setBold(true);
		       styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
		       styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleTitulo.setFont(headerFont);
		       styleTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       
		       XSSFCellStyle styleSubTitulo = objLibro.createCellStyle();
			   XSSFFont fontSub = objLibro.createFont();
			   fontSub.setFontHeightInPoints((short)10);
			   fontSub.setFontName("Calibri");
			   fontSub.setBold(true);
		       styleSubTitulo.setFont(fontSub);
		       styleSubTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       XSSFCellStyle styleAgrupador = objLibro.createCellStyle();
			   XSSFFont fontAgrupador = objLibro.createFont();
			   fontAgrupador.setFontHeightInPoints((short)10);
			   fontAgrupador.setFontName("Calibri");
			   fontAgrupador.setBold(true);
		       styleAgrupador.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(211, 211, 211)));
		       styleAgrupador.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleAgrupador.setFont(fontAgrupador);
		       styleAgrupador.setAlignment(HorizontalAlignment.CENTER);
		       styleAgrupador.setBorderBottom(BorderStyle.THIN);
		       styleAgrupador.setBorderTop(BorderStyle.THIN);
		       styleAgrupador.setBorderLeft(BorderStyle.THIN);
		       styleAgrupador.setBorderRight(BorderStyle.THIN);
		       
		       styleAgrupador.setLeftBorderColor(new XSSFColor(UtilsColor.getBytes(184, 184, 184)));
		       styleAgrupador.setRightBorderColor(new XSSFColor(UtilsColor.getBytes(184, 184, 184)));
		       styleAgrupador.setTopBorderColor(new XSSFColor(UtilsColor.getBytes(184, 184, 184)));
		       styleAgrupador.setBottomBorderColor(new XSSFColor(UtilsColor.getBytes(184, 184, 184)));
		         
		       
		       
		       XSSFCellStyle styleColumnas = objLibro.createCellStyle();
			   XSSFFont fontColumnas = objLibro.createFont();
			   fontColumnas.setFontHeightInPoints((short)10);
			   fontColumnas.setFontName("Calibri");
			   styleColumnas.setFont(fontColumnas);
			   styleColumnas.setAlignment(HorizontalAlignment.CENTER);
		       
		       
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML");
			   monthCell.setCellStyle(styleTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:J1"));
			   
			   
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle Nomina XML");
			   monthCell2.setCellStyle(styleSubTitulo);
			    //estiloCelda2.setAlignment(HorizontalAlignment.CENTER);
			   //monthCell2.setCellStyle(estiloCelda3);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:J2"));
			    
			   
			   header = hoja1.createRow(2);
			   
			   header.setHeightInPoints(18);
			   Cell monthCell3 = header.createCell(0);
			   monthCell3.setCellValue("");
			   monthCell3.setCellStyle(styleAgrupador);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A3:E3"));
			   
			   monthCell3 = header.createCell(5);
			   monthCell3.setCellValue("EMISOR");
			   monthCell3.setCellStyle(styleAgrupador);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("F3:G3"));
			   
			   monthCell3 = header.createCell(7);
			   monthCell3.setCellValue("RECEPTOR");
			   monthCell3.setCellStyle(styleAgrupador);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("H3:I3"));
			   
			   
			   monthCell3 = header.createCell(9);
			   monthCell3.setCellValue("");
			   monthCell3.setCellStyle(styleAgrupador);
			   
			  header = hoja1.createRow(3);
			  header.setHeightInPoints(18);
			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    monthCell.setCellStyle(styleColumnas);
			  }
			  Cell celda = null;
			  BovedaNominaForm bovedaForm = null;
			  Row fila = null;
			  //boolean bandRen = true;
			  //HSSFCellStyle estiloCeldaFinal = null;

			  hoja1.setColumnWidth(0, 3000);
			  hoja1.setColumnWidth(1, 3000);
			  hoja1.setColumnWidth(2, 10000);
			  hoja1.setColumnWidth(3, 5000);
			  hoja1.setColumnWidth(4, 4500);
			  hoja1.setColumnWidth(5, 3500);
			  hoja1.setColumnWidth(6, 7000);
			  hoja1.setColumnWidth(7, 4500);
			  hoja1.setColumnWidth(8, 7000);
			  hoja1.setColumnWidth(9, 4500);
			  
			  int numRow = 4;
			  final String EMITIDO = "Emitido";
			  final String NOMINA = "Nómina";
			  
			  for (int x = regInicial; x < regFinal; x++) {
				   bovedaForm = datosBoveda.get(x);
				   
				   fila = hoja1.createRow(numRow++);
				   
				   celda = fila.createCell(0);
				   celda.setCellValue(EMITIDO);

				   celda = fila.createCell(1);
				   celda.setCellValue(NOMINA);

				   celda = fila.createCell(2);
				   celda.setCellValue(bovedaForm.getUuid());

				   celda = fila.createCell(3);
				   celda.setCellValue(bovedaForm.getFechaFactura());

				   celda = fila.createCell(4);
				   celda.setCellValue(bovedaForm.getFechaTimbrado());
				   
				   celda = fila.createCell(5);
				   celda.setCellValue(bovedaForm.getRfcEmisor());

				   celda = fila.createCell(6);
				   celda.setCellValue(bovedaForm.getRazonSocialEmisor());

				   celda = fila.createCell(7);
				   celda.setCellValue(bovedaForm.getRfcReceptor());

				   celda = fila.createCell(8);
				   celda.setCellValue(bovedaForm.getRazonSocialReceptor());

				   celda = fila.createCell(9);
				   celda.setCellValue(bovedaForm.getTotalDouble());
				   
				   celda = null;
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	}
	
	private XSSFSheet createHoja (XSSFWorkbook objLibro, String nameHoja) {
		XSSFSheet hoja = objLibro.createSheet(nameHoja);
		try {
			final String[] encabezados = {"UUID", "Num. Empleado", "Puesto", "Registro Patronal", "RFC Receptor",
                    "Nombre Receptor", "Fecha Pago", "Clase", "Tipo", "Clave", "Concepto", 
                    "Importe Gravado", "Importe Excento"};
			
			   Row header = hoja.createRow(0);
			   header.setHeightInPoints(18);
			   
			   XSSFCellStyle styleTitulo = objLibro.createCellStyle();
			   XSSFFont headerFont = objLibro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true);
		       styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
		       styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleTitulo.setFont(headerFont);
		       styleTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       
		       XSSFCellStyle styleSubTitulo = objLibro.createCellStyle();
			   XSSFFont fontSub = objLibro.createFont();
			   fontSub.setFontHeightInPoints((short)10);
			   fontSub.setFontName("Arial");
			   fontSub.setBold(true);
		       styleSubTitulo.setFont(fontSub);
		       styleSubTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       XSSFCellStyle styleColumnas = objLibro.createCellStyle();
			   XSSFFont fontColumnas = objLibro.createFont();
			   fontColumnas.setFontHeightInPoints((short)10);
			   fontColumnas.setFontName("Arial");
			   styleColumnas.setFont(fontColumnas);
			   styleColumnas.setAlignment(HorizontalAlignment.CENTER);
		       
		       
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML");
			   monthCell.setCellStyle(styleTitulo);
			   hoja.addMergedRegion(CellRangeAddress.valueOf("A1:M1"));
			   
			   
			   header = hoja.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle Nomina XML");
			   monthCell2.setCellStyle(styleSubTitulo);
			   hoja.addMergedRegion(CellRangeAddress.valueOf("A2:M2"));
			   
			  header = hoja.createRow(2);
			  header.setHeightInPoints(18);
			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    monthCell.setCellStyle(styleColumnas);
			  }
			  hoja.setColumnWidth(0, 10000);
			 // hoja.setColumnWidth(1, 3000);
			  hoja.setColumnWidth(1, 3000);
			  hoja.setColumnWidth(2, 4500);
			  hoja.setColumnWidth(3, 4500);
			  hoja.setColumnWidth(4, 5000);
			  hoja.setColumnWidth(5, 7000);
			  hoja.setColumnWidth(6, 4500);
			  hoja.setColumnWidth(7, 4500);
			  hoja.setColumnWidth(8, 10000);
			  hoja.setColumnWidth(9, 2500);
			  hoja.setColumnWidth(10, 6500);
			  hoja.setColumnWidth(11, 4500);
			  hoja.setColumnWidth(12, 4500);
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return hoja;
		
	}
	
	public XSSFWorkbook toExcelNominaDetalleXML(Connection conSAT, Connection con, String esquema, ArrayList<BovedaNominaForm> datosBoveda) {
		 
		XSSFWorkbook objLibro = new XSSFWorkbook();
        XSSFSheet hoja1 = null;
        XSSFSheet hoja2 = null;
        XSSFSheet hojaTemp = null;
        Cell celda = null;
	    BovedaNominaForm bovedaForm = null;
	    Row fila = null;
	    PreparedStatement stmtDeducciones = null;
	    PreparedStatement stmtOtroPago = null;

		  try {
			  int REG_NUEVA_PAGINA = 1040000;
		  	  hoja1 = createHoja(objLibro, "Reporte Detalle");
		  	 
		  	  
			  stmtDeducciones = con.prepareStatement(BovedaNominaQuerys.getObtenerDeducciones(esquema));
			  stmtOtroPago = con.prepareStatement(BovedaNominaQuerys.getObtenerOtroPago(esquema)); 
		  	  
			  int numRow = 3;
			  // final String ESTATUS = "Vigente";
			  int contadorRegistro = 0;
			  boolean resetContador = true;
			  String uuidAnterior = null;
			  
			  BovedaNominaForm bovedaTempForm = null;
			  BovedaNominaBean bovedaBean = new BovedaNominaBean();
			  hojaTemp = hoja1;
			  
			  
			  CatalogosBean catalogosBean = new CatalogosBean();
			  HashMap<String, CatalogosForm> mapaTipoNomina =  catalogosBean.detalleTipoNomina(conSAT, "");
			  CatalogosForm tipoNominaForm = null;
			  
			  for (int x = 0; x < datosBoveda.size(); x++) {
				   bovedaForm = datosBoveda.get(x);
				   
				   if (uuidAnterior != null && !bovedaForm.getUuid().equalsIgnoreCase(uuidAnterior)) {
					   ArrayList<NominaForm> listaDeducciones =  bovedaBean.obtenerDeduccionesXML(stmtDeducciones, uuidAnterior);
					   imprimirDeducciones(listaDeducciones, hojaTemp, bovedaTempForm, numRow, mapaTipoNomina, "DEDUCCION");
					   numRow+=listaDeducciones.size();
					   contadorRegistro+=listaDeducciones.size();
				   }
				   
				   if (uuidAnterior != null && !bovedaForm.getUuid().equalsIgnoreCase(uuidAnterior)) {
					   ArrayList<NominaForm> listaOtroPago =  bovedaBean.obtenerOtroPagos(stmtOtroPago, uuidAnterior);
					   imprimirDeducciones(listaOtroPago, hojaTemp, bovedaTempForm, numRow, mapaTipoNomina, "OTRO PAGO");
					   numRow+=listaOtroPago.size();
					   contadorRegistro+=listaOtroPago.size();
				   }
				   
			  	  if (contadorRegistro >= REG_NUEVA_PAGINA) {
			  		if (resetContador) {
			  			hoja2 = createHoja(objLibro, "Reporte Detalle (2)");
			  			numRow = 3;
			  			resetContador = false;
			  			hojaTemp = hoja2;
			  		}
			  		fila = hoja2.createRow(numRow++);
			  		
			  	  }else {
			  		fila = hoja1.createRow(numRow++);
			  		
			  	  }
			  	  
			  	   contadorRegistro++;
				   celda = fila.createCell(0);
				   celda.setCellValue(bovedaForm.getUuid());

				   celda = fila.createCell(1);
				   celda.setCellValue(bovedaForm.getNumEmpleado());

				   celda = fila.createCell(2);
				   celda.setCellValue(bovedaForm.getPuesto());

				   celda = fila.createCell(3);
				   celda.setCellValue(bovedaForm.getRegistroPatronal());
				   
				   celda = fila.createCell(4);
				   celda.setCellValue(bovedaForm.getRfcReceptor());

				   celda = fila.createCell(5);
				   celda.setCellValue(bovedaForm.getRazonSocialReceptor());

				   celda = fila.createCell(6);
				   celda.setCellValue(bovedaForm.getFechaPago());

				   celda = fila.createCell(7);
				   celda.setCellValue("PERCEPCION");

				   tipoNominaForm = mapaTipoNomina.get(bovedaForm.getTipo());
				   if (tipoNominaForm == null) {
					   tipoNominaForm = new CatalogosForm();
				   }
				   celda = fila.createCell(8);
				   celda.setCellValue(bovedaForm.getTipo().concat("-").concat(Utils.noNulo(tipoNominaForm.getDescripcion())));
				   
				   celda = fila.createCell(9);
				   celda.setCellValue(bovedaForm.getClave());
				   
				   celda = fila.createCell(10);
				   celda.setCellValue(bovedaForm.getConcepto());
				   
				   celda = fila.createCell(11);
				   celda.setCellValue(bovedaForm.getImporteGravadoDouble());
				   
				   celda = fila.createCell(12);
				   celda.setCellValue(bovedaForm.getImporteExcentoDouble());
				   celda = null;
				   uuidAnterior = bovedaForm.getUuid();
				   bovedaTempForm = bovedaForm;
				   
			  }
			  
			  if (datosBoveda.size() > 0) {
				   ArrayList<NominaForm> listaDeducciones =  bovedaBean.obtenerDeduccionesXML(stmtDeducciones, bovedaForm.getUuid());
				   imprimirDeducciones(listaDeducciones, hojaTemp, bovedaTempForm, numRow, mapaTipoNomina, "DEDUCCION");
				   numRow+=listaDeducciones.size();
				   
				   ArrayList<NominaForm> listaOtroPago =  bovedaBean.obtenerOtroPagos(stmtOtroPago, uuidAnterior);
				   imprimirDeducciones(listaOtroPago, hojaTemp, bovedaTempForm, numRow, mapaTipoNomina, "OTRO PAGO");
				   
			  }
			  
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }finally {
			try {
				if (stmtDeducciones != null) {
					stmtDeducciones.close();
				}
				stmtDeducciones = null;
				if (stmtOtroPago != null) {
					stmtOtroPago.close();
				}
				stmtOtroPago = null;
			} catch (Exception e2) {
				stmtDeducciones = null;
				stmtOtroPago = null;
			}
		}
		  return objLibro;
	}
	
	
	
	public void imprimirDeducciones(ArrayList<NominaForm> listaDeducciones, XSSFSheet hoja, 
			BovedaNominaForm bovedaForm, int numRow, HashMap<String, CatalogosForm> mapaTipoNomina, String tipoNomina) {
		 
        Cell celda = null;
        NominaForm nominaForm = null;
	    Row fila = null;
		  try {
			  // final String ESTATUS = "Vigente";
			  CatalogosForm tipoNominaForm = null;
			  for (int x = 0; x < listaDeducciones.size(); x++) {
				  nominaForm = listaDeducciones.get(x);
				  fila = hoja.createRow(numRow++);
				  
				   celda = fila.createCell(0);
				   celda.setCellValue(bovedaForm.getUuid());
/*
				   celda = fila.createCell(1);
				   celda.setCellValue(ESTATUS);
*/
				   celda = fila.createCell(1);
				   celda.setCellValue(bovedaForm.getNumEmpleado());

				   celda = fila.createCell(2);
				   celda.setCellValue(bovedaForm.getPuesto());

				   celda = fila.createCell(3);
				   celda.setCellValue(bovedaForm.getRegistroPatronal());
				   
				   celda = fila.createCell(4);
				   celda.setCellValue(bovedaForm.getRfcReceptor());

				   celda = fila.createCell(5);
				   celda.setCellValue(bovedaForm.getRazonSocialReceptor());

				   celda = fila.createCell(6);
				   celda.setCellValue(bovedaForm.getFechaPago());

				   celda = fila.createCell(7);
				   celda.setCellValue(tipoNomina);

				   if ("DEDUCCION".equalsIgnoreCase(tipoNomina)) {
					   tipoNominaForm = mapaTipoNomina.get(nominaForm.getTipo());
					   if (tipoNominaForm == null) {
						   tipoNominaForm = new CatalogosForm();
					   }
					   celda = fila.createCell(8);
					   celda.setCellValue(nominaForm.getTipo().concat("-").concat(Utils.noNulo(tipoNominaForm.getDescripcion())));
					   
				   }else {
					   celda = fila.createCell(8);
					   celda.setCellValue(nominaForm.getTipo());
					   
				   }
				   celda = fila.createCell(9);
				   celda.setCellValue(Utils.noNuloINT(bovedaForm.getClave()));
				   
				   celda = fila.createCell(10);
				   celda.setCellValue(nominaForm.getConcepto());
				   
				   celda = fila.createCell(11);
				   celda.setCellValue(nominaForm.getImporteGravadoDouble());
				   
				   celda = fila.createCell(12);
				   celda.setCellValue(nominaForm.getImporteExcentoDouble());
				   
				   celda = null;
				   
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	}
	
	public void toExcelCP(HSSFSheet hoja1 , ArrayList<CartasPorteForm> listaRow, HSSFWorkbook objLibro) {
		  try {
			  final String[] errorSource = {"Business Unit", "RFC", "Razón Social", "Orden de Compra", "Tipo de Moneda", "Fecha de Pago", 
					  						"UUID Factura", "Sub-Total", "IVA Acreditable", "IVA Retenido", "Imp. Locales", "Total", "Fecha de Pago Carta Porte","Fecha de Timbrado", "UUID Carta Porte",
					  						"Total Factura", "Estatus Factura", "Estatus Carta Porte", "Estatus Factura vs Carta Porte"};
			  
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   HSSFPalette palette = objLibro.getCustomPalette();
			   
			   HSSFFont fuente = objLibro.createFont();
			   fuente.setFontHeightInPoints((short)10);
			   fuente.setFontName(HSSFFont.FONT_ARIAL);
			   
			   HSSFFont fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(IndexedColors.WHITE.index);

			   HSSFFont fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBold(true);

			   HSSFCellStyle estiloCelda = objLibro.createCellStyle();
			   //estiloCelda.setWrapText(true);
			   estiloCelda.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda.setFont(fuente);
			   estiloCelda.setFillForegroundColor(palette.findSimilarColor((byte) 170, (byte) 218, (byte) 252).getIndex());
			   estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda2 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda2.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda2.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda2.setFont(fuente);
			   estiloCelda2.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   HSSFCellStyle encabezadoPrincipal = objLibro.createCellStyle();
			   //encabezadoPrincipal.setWrapText(true);
			   encabezadoPrincipal.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoPrincipal.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoPrincipal.setFont(fuenteEncabezado);
			   encabezadoPrincipal.setFillForegroundColor(palette.findSimilarColor((byte) 12, (byte) 57, (byte) 90).getIndex());
			   encabezadoPrincipal.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle encabezadoDetalle = objLibro.createCellStyle();
			   //encabezadoDetalle.setWrapText(true);
			   encabezadoDetalle.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoDetalle.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoDetalle.setFont(fuenteEncabezado);
			   encabezadoDetalle.setFillForegroundColor(palette.findSimilarColor((byte) 6, (byte) 103, (byte) 172).getIndex());
			   encabezadoDetalle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   

			   HSSFCellStyle estiloCelda3 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   
			    Cell monthCell = header.createCell(0);
			    monthCell.setCellValue("Conciliados");
			    monthCell.setCellStyle(encabezadoPrincipal);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:R1"));
			  
			    
			    
			    header = hoja1.createRow(1);
			    header.setHeightInPoints(18);
			    Cell monthCell2 = header.createCell(0);
			    monthCell2.setCellValue("Detalle de Conciliados Carta Porte");
			    //estiloCelda2.setAlignment(HorizontalAlignment.CENTER);
			    monthCell2.setCellStyle(estiloCelda3);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:R2"));

			    
			   header = hoja1.createRow(2);
			   header.setHeightInPoints(18);
			  for (int i = 0; i < errorSource.length; i++) {
				    monthCell = header.createCell(i);
				    monthCell.setCellValue(errorSource[i]);
				    monthCell.setCellStyle(encabezadoDetalle);
			  }
			  
			  HSSFCell celda1 = null;
			  HSSFCell celda2 = null;
			  HSSFCell celda3 = null;
			  HSSFCell celda4 = null;
			  HSSFCell celda5 = null;
			  HSSFCell celda6 = null;
			  HSSFCell celda7 = null;
			  HSSFCell celda8 = null;
			  HSSFCell celda9 = null;
			  HSSFCell celda10 = null;
			  HSSFCell celda11 = null;
			  HSSFCell celda12 = null;
			  HSSFCell celda13 = null;
			  HSSFCell celda14 = null;
			  HSSFCell celda15 = null;
			  HSSFCell celda16 = null;
			  HSSFCell celda18 = null;
			  HSSFCell celda19 = null;
			  HSSFCell celda20 = null;
			  
			  HSSFRow fila = null;
			  boolean bandRen = true;
			  boolean bandAutoSize = true;
			  
			  CartasPorteForm cartaForm = null;
			  for (int x = 0; x < listaRow.size(); x++) {
				  cartaForm = listaRow.get(x);
				  
				   fila = hoja1.createRow(x + 3);
				   // fila.setHeightInPoints(18);
				   
				   if (bandRen){
					   bandRen = false;
				   }
				   else{
					   bandRen = true;
				   }
				   
				   celda1 = fila.createCell(0);
				   //celda1.setCellStyle(estiloCeldaFinal);
				   celda1.setCellType(CellType.STRING);
				   celda1.setCellValue(cartaForm.getTipoOrden());
				   
				   celda2 = fila.createCell(1);
				   //celda1.setCellStyle(estiloCeldaFinal);
				   celda2.setCellType(CellType.STRING);
				   celda2.setCellValue(cartaForm.getRfc());
				   
				   celda3 = fila.createCell(2);
				   //celda2.setCellStyle(estiloCeldaFinal);
				   celda3.setCellType(CellType.STRING);
				   celda3.setCellValue(cartaForm.getRazonSocial());
				   
				   celda4 = fila.createCell(3);
				   //celda3.setCellStyle(estiloCeldaFinal);
				   celda4.setCellType(CellType.NUMERIC);
				   celda4.setCellValue(cartaForm.getFolioEmpresa());
				   
				   celda5 = fila.createCell(4);
				   //celda4.setCellStyle(estiloCeldaFinal);
				   celda5.setCellType(CellType.STRING);
				   celda5.setCellValue(cartaForm.getTipoMoneda());

				   celda6 = fila.createCell(5);
				   //celda5.setCellStyle(estiloCeldaFinal);
				   celda6.setCellType(CellType.STRING);
				   celda6.setCellValue(cartaForm.getFechaPago());
				   
				   celda7 = fila.createCell(6);
				   //celda6.setCellStyle(estiloCeldaFinal);
				   celda7.setCellType(CellType.STRING);
				   celda7.setCellValue(cartaForm.getUuid());
				   
				   celda8 = fila.createCell(7);
				   //celda7.setCellStyle(estiloCeldaFinal);
				   celda8.setCellType(CellType.NUMERIC);
				   celda8.setCellValue(cartaForm.getSubTotal());
				   
				   celda9 = fila.createCell(8);
				   //celda8.setCellStyle(estiloCeldaFinal);
				   celda9.setCellType(CellType.NUMERIC);
				   celda9.setCellValue(cartaForm.getIva());
				   
				   celda10 = fila.createCell(9);
				   //celda9.setCellStyle(estiloCeldaFinal);
				   celda10.setCellType(CellType.NUMERIC);
				   celda10.setCellValue(cartaForm.getIvaRet());

				   celda11 = fila.createCell(10);
				   //celda10.setCellStyle(estiloCeldaFinal);
				   celda11.setCellType(CellType.NUMERIC);
				   celda11.setCellValue(cartaForm.getImpLocales());

				   celda12 = fila.createCell(11);
				   //celda11.setCellStyle(estiloCeldaFinal);
				   celda12.setCellType(CellType.NUMERIC);
				   celda12.setCellValue(cartaForm.getTotal());

				   celda13 = fila.createCell(12);
				   //celda11.setCellStyle(estiloCeldaFinal);
				   celda13.setCellType(CellType.STRING);
				   celda13.setCellValue(cartaForm.getFechaPagoXML());

				   celda14 = fila.createCell(13);
				   //celda11.setCellStyle(estiloCeldaFinal);
				   celda14.setCellType(CellType.STRING);
				   celda14.setCellValue(cartaForm.getFechaTimbrado());
				   
				   celda15 = fila.createCell(14);
				   //celda12.setCellStyle(estiloCeldaFinal);
				   celda15.setCellType(CellType.NUMERIC);
				   celda15.setCellValue(cartaForm.getUuidCarta());

				   celda16 = fila.createCell(15);
				   //celda13.setCellStyle(estiloCeldaFinal);
				   celda16.setCellType(CellType.NUMERIC);
				   celda16.setCellValue(cartaForm.getTotalFactura());

				   celda18 = fila.createCell(16);
				   //celda15.setCellStyle(estiloCeldaFinal);
				   celda18.setCellType(CellType.NUMERIC);
				   celda18.setCellValue(cartaForm.getEstatusFactura());

				   celda19 = fila.createCell(17);
				   //celda16.setCellStyle(estiloCeldaFinal);
				   celda19.setCellType(CellType.NUMERIC);
				   celda19.setCellValue(cartaForm.getEstatusCarta());

				   celda20 = fila.createCell(18);
				   //celda17.setCellStyle(estiloCeldaFinal);
				   celda20.setCellType(CellType.NUMERIC);
				   celda20.setCellValue(cartaForm.getEstatusConciliacionExcel());
				   /*
				   if (bandAutoSize) {
				   }
				   */
				   bandAutoSize = false;
				  
				    celda1 = null;
				    celda2 = null;
				    celda3 = null;
				    celda4 = null;
				    celda5 = null;
				    celda6 = null;
				    celda7 = null;
				    celda8 = null;
				    celda9 = null;
				    celda10 = null;
				    celda11 = null;
				    celda12 = null;
				    celda13 = null;
				    celda14 = null;
				    celda15 = null;
				    celda16 = null;
			  }
		  } catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	 }
	
	public void toClaveUsoCFDICP(HSSFSheet hoja1 , JSONArray jsonArray, HSSFWorkbook objLibro) {
		  try {
			  final String[] errorSource = {"RFC","Razón Social", "Uso CFDI Carta Porte", "Descripcion Uso CFDI Carta Porte", "Clave de Producto", 
					  						"Descripción Clave de Producto", "División", "Grupo", "Clase"};

			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   HSSFPalette palette = objLibro.getCustomPalette();
			   
			   HSSFFont fuente = objLibro.createFont();
			   fuente.setFontHeightInPoints((short)10);
			   fuente.setFontName(HSSFFont.FONT_ARIAL);
			   
			   HSSFFont fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(IndexedColors.WHITE.index);

			   HSSFFont fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBold(true);

			   HSSFCellStyle estiloCelda = objLibro.createCellStyle();
			   //estiloCelda.setWrapText(true);
			   estiloCelda.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda.setFont(fuente);
			   estiloCelda.setFillForegroundColor(palette.findSimilarColor((byte) 170, (byte) 218, (byte) 252).getIndex());
			   estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda2 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda2.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda2.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda2.setFont(fuente);
			   estiloCelda2.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   HSSFCellStyle encabezadoPrincipal = objLibro.createCellStyle();
			   //encabezadoPrincipal.setWrapText(true);
			   encabezadoPrincipal.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoPrincipal.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoPrincipal.setFont(fuenteEncabezado);
			   encabezadoPrincipal.setFillForegroundColor(palette.findSimilarColor((byte) 12, (byte) 57, (byte) 90).getIndex());
			   encabezadoPrincipal.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle encabezadoDetalle = objLibro.createCellStyle();
			   //encabezadoDetalle.setWrapText(true);
			   encabezadoDetalle.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoDetalle.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoDetalle.setFont(fuenteEncabezado);
			   encabezadoDetalle.setFillForegroundColor(palette.findSimilarColor((byte) 6, (byte) 103, (byte) 172).getIndex());
			   encabezadoDetalle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda3 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			    Cell monthCell = header.createCell(0);
			    monthCell.setCellValue("Configuración de Clave y Uso CFDI Carta Porte");
			    monthCell.setCellStyle(encabezadoPrincipal);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:I1"));
			  
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			    Cell monthCell2 = header.createCell(0);
			    monthCell2.setCellValue("Detalle de Configuraciones");
			    //estiloCelda2.setAlignment(HorizontalAlignment.CENTER);
			    monthCell2.setCellStyle(estiloCelda3);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:I2"));
			    
			   header = hoja1.createRow(2);
			   header.setHeightInPoints(18);

			  for (int i = 0; i < errorSource.length; i++) {
				    monthCell = header.createCell(i);
				    monthCell.setCellValue(errorSource[i]);
				    monthCell.setCellStyle(encabezadoDetalle);
			  }

			  JSONObject jsonobj = null;
			  HSSFRow fila = null;
			  boolean bandRen = true;

			  for (int x = 0; x < jsonArray.size(); x++) {
				  jsonobj = (JSONObject) jsonArray.get(x);
				  
				   fila = hoja1.createRow(x + 3);
				   //fila.setHeightInPoints(18);
				   
				   if (bandRen){
					   bandRen = false;
					   //estiloCeldaFinal = estiloCelda;
				   }
				   else{
					   bandRen = true;
					   //estiloCeldaFinal = estiloCelda2;
				   }
				   
				   fila.createCell(0).setCellValue(jsonobj.get("RFC").toString());
				   fila.createCell(1).setCellValue(Utils.regresaCaracteresNormales(jsonobj.get("RAZON_SOCIAL").toString()));
				   fila.createCell(2).setCellValue(jsonobj.get("USO_CFDI").toString());
				   fila.createCell(3).setCellValue(Utils.regresaCaracteresNormales(jsonobj.get("DES_USO_CFDI").toString()));
				   fila.createCell(4).setCellValue(jsonobj.get("CLAVE_PRODUCTO").toString());
				   fila.createCell(5).setCellValue(Utils.regresaCaracteresNormales(jsonobj.get("DES_CLAVE_PRODUCTO").toString()));
				   fila.createCell(6).setCellValue(Utils.regresaCaracteresNormales(jsonobj.get("DIVISION").toString()));
				   fila.createCell(7).setCellValue(Utils.regresaCaracteresNormales(jsonobj.get("GRUPO").toString()));
				   fila.createCell(8).setCellValue(Utils.regresaCaracteresNormales(jsonobj.get("CLASE").toString()));
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	 }
	
	public void toExcelPedimento(HSSFSheet hoja1 , JSONArray jsonArray, HSSFWorkbook objLibro) {
		  try {
			  final String[] errorSource = {"Numero de Pedimento", "Clave de Pedimento", "Regimen", "DTA", "IVA", "IGI", "PRV", "IVAPRV", "Efectivo", "Otros", "Total", "Banco",
					  						"Linea de Captura", "Importe de Pago", "Fecha de Pago", "Numero de Operacion", "Numero SAT", "Medio de Presentacion", "Medio de Recepcion"};

			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   HSSFPalette palette = objLibro.getCustomPalette();
			   
			   HSSFFont fuente = objLibro.createFont();
			   fuente.setFontHeightInPoints((short)10);
			   fuente.setFontName(HSSFFont.FONT_ARIAL);
			   
			   HSSFFont fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(IndexedColors.WHITE.index);

			   HSSFFont fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBold(true);

			   HSSFCellStyle estiloCelda = objLibro.createCellStyle();
			   estiloCelda.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda.setFont(fuente);
			   estiloCelda.setFillForegroundColor(palette.findSimilarColor((byte) 170, (byte) 218, (byte) 252).getIndex());
			   estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda2 = objLibro.createCellStyle();
			   estiloCelda2.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda2.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda2.setFont(fuente);
			   estiloCelda2.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   HSSFCellStyle encabezadoPrincipal = objLibro.createCellStyle();
			   encabezadoPrincipal.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoPrincipal.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoPrincipal.setFont(fuenteEncabezado);
			   encabezadoPrincipal.setFillForegroundColor(palette.findSimilarColor((byte) 12, (byte) 57, (byte) 90).getIndex());
			   encabezadoPrincipal.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle encabezadoDetalle = objLibro.createCellStyle();
			   encabezadoDetalle.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoDetalle.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoDetalle.setFont(fuenteEncabezado);
			   encabezadoDetalle.setFillForegroundColor(palette.findSimilarColor((byte) 6, (byte) 103, (byte) 172).getIndex());
			   encabezadoDetalle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda3 = objLibro.createCellStyle();
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			    Cell monthCell = header.createCell(0);
			    monthCell.setCellValue("Pedimentos");
			    monthCell.setCellStyle(encabezadoPrincipal);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:S1"));
			  
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			    Cell monthCell2 = header.createCell(0);
			    monthCell2.setCellValue("Detalle de Pedimentos");
			    monthCell2.setCellStyle(estiloCelda3);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:S2"));
			    
			   header = hoja1.createRow(2);
			   header.setHeightInPoints(18);

			  for (int i = 0; i < errorSource.length; i++) {
				    monthCell = header.createCell(i);
				    monthCell.setCellValue(errorSource[i]);
				    monthCell.setCellStyle(encabezadoDetalle);
			  }

			  JSONObject jsonobj = null;
			  HSSFRow fila = null;
			  boolean bandRen = true;

			  for (int x = 0; x < jsonArray.size(); x++) {
				  jsonobj = (JSONObject) jsonArray.get(x);
				  
				   fila = hoja1.createRow(x + 3);
				   // fila.setHeightInPoints(18);
				   
				   if (bandRen){
					   bandRen = false;
				   }
				   else{
					   bandRen = true;
				   }
				   
				   fila.createCell(0).setCellValue(jsonobj.get("NUM_PEDIMENTO").toString());
				   fila.createCell(1).setCellValue(jsonobj.get("CVE_PEDIMENTO").toString());
				   fila.createCell(2).setCellValue(jsonobj.get("REGIMEN").toString());
				   fila.createCell(3).setCellValue(jsonobj.get("DTA").toString());
				   fila.createCell(4).setCellValue(jsonobj.get("IVA").toString());
				   fila.createCell(5).setCellValue(jsonobj.get("IGI").toString());
				   fila.createCell(6).setCellValue(jsonobj.get("PRV").toString());
				   fila.createCell(7).setCellValue(jsonobj.get("IVAPRV").toString());
				   fila.createCell(8).setCellValue(jsonobj.get("EFECTIVO").toString());
				   fila.createCell(9).setCellValue(jsonobj.get("OTROS").toString());
				   fila.createCell(10).setCellValue(jsonobj.get("TOTAL").toString());
				   fila.createCell(11).setCellValue(jsonobj.get("BANCO").toString());
				   fila.createCell(12).setCellValue(jsonobj.get("LINEA_CAPTURA").toString());
				   fila.createCell(13).setCellValue(jsonobj.get("IMPORTE_PAGO").toString());
				   fila.createCell(14).setCellValue(jsonobj.get("FECHA_PAGO").toString());
				   fila.createCell(15).setCellValue(jsonobj.get("NUMERO_OPERACION").toString());
				   fila.createCell(16).setCellValue(jsonobj.get("NUMERO_SAT").toString());
				   fila.createCell(17).setCellValue(jsonobj.get("MEDIO_PRESENTACION").toString());
				   fila.createCell(18).setCellValue(jsonobj.get("MEDIO_RECEPCION").toString());
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	 }
	
	public void toExcelRF(HSSFSheet hoja1 , JSONArray jsonArray, HSSFWorkbook objLibro) {
		  try {
			  final String[] errorSource = {"Id. Proveedor", "Razon Social", "Regimen Fiscal", "Descripción de Regimen"};

			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   HSSFPalette palette = objLibro.getCustomPalette();
			   
			   HSSFFont fuente = objLibro.createFont();
			   fuente.setFontHeightInPoints((short)10);
			   fuente.setFontName(HSSFFont.FONT_ARIAL);
			   
			   HSSFFont fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(IndexedColors.WHITE.index);

			   HSSFFont fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBold(true);

			   HSSFCellStyle estiloCelda = objLibro.createCellStyle();
			   estiloCelda.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda.setFont(fuente);
			   estiloCelda.setFillForegroundColor(palette.findSimilarColor((byte) 170, (byte) 218, (byte) 252).getIndex());
			   estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda2 = objLibro.createCellStyle();
			   estiloCelda2.setAlignment(HorizontalAlignment.JUSTIFY);
			   estiloCelda2.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda2.setFont(fuente);
			   estiloCelda2.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   HSSFCellStyle encabezadoPrincipal = objLibro.createCellStyle();
			   encabezadoPrincipal.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoPrincipal.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoPrincipal.setFont(fuenteEncabezado);
			   encabezadoPrincipal.setFillForegroundColor(palette.findSimilarColor((byte) 12, (byte) 57, (byte) 90).getIndex());
			   encabezadoPrincipal.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle encabezadoDetalle = objLibro.createCellStyle();
			   encabezadoDetalle.setAlignment(HorizontalAlignment.CENTER);
			   encabezadoDetalle.setVerticalAlignment(VerticalAlignment.TOP);
			   encabezadoDetalle.setFont(fuenteEncabezado);
			   encabezadoDetalle.setFillForegroundColor(palette.findSimilarColor((byte) 6, (byte) 103, (byte) 172).getIndex());
			   encabezadoDetalle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda3 = objLibro.createCellStyle();
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			    Cell monthCell = header.createCell(0);
			    monthCell.setCellValue("Configuracion Regimen Fiscal");
			    monthCell.setCellStyle(encabezadoPrincipal);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:D1"));
			  
			    
		    
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			    Cell monthCell2 = header.createCell(0);
			    monthCell2.setCellValue("Detalle Regimen Fiscal");
			    monthCell2.setCellStyle(estiloCelda3);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:D2"));
			    
			   header = hoja1.createRow(2);
			   header.setHeightInPoints(18);

			  for (int i = 0; i < errorSource.length; i++) {
				    monthCell = header.createCell(i);
				    monthCell.setCellValue(errorSource[i]);
				    monthCell.setCellStyle(encabezadoDetalle);
			  }

			  JSONObject jsonobj = null;
			  HSSFRow fila = null;
			 
			  for (int x = 0; x < jsonArray.size(); x++) {
				  jsonobj = (JSONObject) jsonArray.get(x);
				  
				   fila = hoja1.createRow(x + 3);
				   // fila.setHeightInPoints(18);
				   
				   
				   fila.createCell(0).setCellValue(jsonobj.get("CLAVE_PROVEEDOR").toString());
				   fila.createCell(1).setCellValue(jsonobj.get("RAZON_SOCIAL").toString());
				   fila.createCell(2).setCellValue(jsonobj.get("CLAVE_REGIMEN").toString());
				   fila.createCell(3).setCellValue(jsonobj.get("DES_REGIMEN").toString());
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	 }
}
