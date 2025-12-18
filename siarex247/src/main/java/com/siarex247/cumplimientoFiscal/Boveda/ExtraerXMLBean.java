package com.siarex247.cumplimientoFiscal.Boveda;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.itextpdf.xmltopdf.CfdiRelacionado;
import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.Concepto;
import com.itextpdf.xmltopdf.Retencion;
import com.itextpdf.xmltopdf.RetencionC;
import com.itextpdf.xmltopdf.Traslado;
import com.itextpdf.xmltopdf.TrasladoC;
import com.itextpdf.xmltopdf.Pagos.PDoctoRelacionado;
import com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado;
import com.siarex247.catalogos.sat.Catalogos.CatalogosBean;
import com.siarex247.catalogos.sat.Catalogos.CatalogosForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsColor;
import com.siarex247.utils.UtilsFechas;

public class ExtraerXMLBean {

	public static final Logger logger = Logger.getLogger("siarex247");

	
	public ArrayList<DoctoRelacionadoForm> getDoctoRelacionados(ArrayList<DoctoRelacionado> listaDoctoRelacionado20, ArrayList<PDoctoRelacionado> listaDoctoRelacionado) {
		ArrayList<DoctoRelacionadoForm> listaDoctos = new ArrayList<>();
		DoctoRelacionadoForm doctoRelacionadoForm = new DoctoRelacionadoForm();
		try {

			if (listaDoctoRelacionado20 != null) {
				DoctoRelacionado _doctoRelacionado = null;
				for (int x = 0; x < listaDoctoRelacionado20.size(); x++) {
					_doctoRelacionado = listaDoctoRelacionado20.get(x);
					doctoRelacionadoForm.setIdDocumento(_doctoRelacionado.getIdDocumento());
					doctoRelacionadoForm.setSerie(_doctoRelacionado.getSerie());
					doctoRelacionadoForm.setFolio(_doctoRelacionado.getFolio());
					doctoRelacionadoForm.setMonedaDR(_doctoRelacionado.getMonedaDR());
					doctoRelacionadoForm.setTipoCambioDR(0);
					doctoRelacionadoForm.setMetodoDePagoDR("");
					doctoRelacionadoForm.setNumParcialidad(_doctoRelacionado.getNumParcialidad());
					doctoRelacionadoForm.setImpSaldoAnt(_doctoRelacionado.getImpSaldoAnt());
					doctoRelacionadoForm.setImpPagado(_doctoRelacionado.getImpPagado());
					doctoRelacionadoForm.setImpSaldoInsoluto(_doctoRelacionado.getImpSaldoInsoluto());
					listaDoctos.add(doctoRelacionadoForm);
					doctoRelacionadoForm = new DoctoRelacionadoForm();
				}
			}
			
			
			if (listaDoctoRelacionado != null) {
				PDoctoRelacionado _pDoctoRelacionado = null;
				for (int x = 0; x < listaDoctoRelacionado.size(); x++) {
					_pDoctoRelacionado = listaDoctoRelacionado.get(x);
					doctoRelacionadoForm.setIdDocumento(_pDoctoRelacionado.getIdDocumento());
					doctoRelacionadoForm.setSerie(_pDoctoRelacionado.getSerie());
					doctoRelacionadoForm.setFolio(_pDoctoRelacionado.getFolio());
					doctoRelacionadoForm.setMonedaDR(_pDoctoRelacionado.getMonedaDR());
					doctoRelacionadoForm.setTipoCambioDR(_pDoctoRelacionado.getTipoCambioDR());
					doctoRelacionadoForm.setMetodoDePagoDR(_pDoctoRelacionado.getMetodoDePagoDR());
					doctoRelacionadoForm.setNumParcialidad(_pDoctoRelacionado.getNumParcialidad());
					doctoRelacionadoForm.setImpSaldoAnt(_pDoctoRelacionado.getImpSaldoAnt());
					doctoRelacionadoForm.setImpPagado(_pDoctoRelacionado.getImpPagado());
					doctoRelacionadoForm.setImpSaldoInsoluto(_pDoctoRelacionado.getImpSaldoInsoluto());
					listaDoctos.add(doctoRelacionadoForm);
					doctoRelacionadoForm = new DoctoRelacionadoForm();
				}
			}
			
			
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return listaDoctos;
	}
	

	public ArrayList<ConceptosForm> getConceptos(ArrayList<Concepto> listaConceptoXML, Comprobante _comprobante) {
		ArrayList<ConceptosForm> listaConceptos = new ArrayList<>();
		ConceptosForm conceptosForm = new ConceptosForm();
		// boolean bandRetencion = false;
		try {
			ArrayList<TrasladoC> listaTranslados = null;
			TrasladoC trasladoC = null;
			
			ArrayList<RetencionC> listaRetenciones = null;
			RetencionC retencionC = null;
			
			
			double  baseTranslado = 0;
			String  tipoFactorTranslado = "";
			double  tasaOCuotaTranslado = 0;
			double  importeTranslado = 0;
			String  claveImpuestoTranslado = "";
			String  impuestoTranslado = "";
			
				Concepto conceptoXML = null;
				for (int x = 0; x < listaConceptoXML.size(); x++) {
					conceptoXML = listaConceptoXML.get(x);
					// logger.info("descripcion Concepto==>"+conceptoXML.getDescripcion());
					conceptosForm.setImporte(conceptoXML.getImporte());
					conceptosForm.setDescripcion(conceptoXML.getDescripcion());
					conceptosForm.setClaveUnidad(conceptoXML.getClaveUnidad());
					conceptosForm.setClaveProdServ(conceptoXML.getClaveProdServ());
					conceptosForm.setCantidad(conceptoXML.getCantidad());
					conceptosForm.setValorUnitario(conceptoXML.getValorUnitario());
					conceptosForm.setNoIdentificacion(conceptoXML.getNoIdentificacion());
					conceptosForm.setUnidad(conceptoXML.getUnidad());
					 
					// bandRetencion = true;
						if (conceptoXML.getImpuestos() != null) {
							listaTranslados = conceptoXML.getImpuestos().getTraslados();
							if (listaTranslados != null) {
								for (int y = 0; y < listaTranslados.size(); y++) {
									trasladoC = listaTranslados.get(y);
									if ("002".equalsIgnoreCase(trasladoC.getImpuesto())) {
										baseTranslado = trasladoC.getBase();
										tipoFactorTranslado = trasladoC.getTipoFactor();
										tasaOCuotaTranslado = trasladoC.getTasaOCuota();
										importeTranslado = trasladoC.getImporte();
										claveImpuestoTranslado = "IVA";
										impuestoTranslado = trasladoC.getImpuesto();
										
										conceptosForm.setClaveImpuestoTranslado(claveImpuestoTranslado);
										conceptosForm.setImpuestoTranslado(impuestoTranslado);
										conceptosForm.setBaseTranslado(baseTranslado);
										conceptosForm.setTipoFactorTranslado(tipoFactorTranslado);
										conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
										conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
										conceptosForm.setImporteTranslado(importeTranslado);
										
									}
								}
							}
							
							listaRetenciones = conceptoXML.getImpuestos().getRetenciones();
							if (listaRetenciones == null || listaRetenciones.size() == 0) {
								listaConceptos.add(conceptosForm);
								conceptosForm = new ConceptosForm();
							}else {
								for (int y = 0; y < listaRetenciones.size(); y++) {
									retencionC = listaRetenciones.get(y);
									if ("001".equalsIgnoreCase(retencionC.getImpuesto())) {
										
										conceptosForm.setClaveImpuestoRetencion("ISR");
										conceptosForm.setBaseRetencion(retencionC.getBase());
										conceptosForm.setImpuestoRetencion(retencionC.getImpuesto());
										conceptosForm.setTipoFactorRetencion(retencionC.getTipoFactor());
										conceptosForm.setTasaOCuotaRetencion(retencionC.getTasaOCuota());
										conceptosForm.setImporteRetencion(retencionC.getImporte());
										
										conceptosForm.setClaveImpuestoTranslado(claveImpuestoTranslado);
										conceptosForm.setImpuestoTranslado(impuestoTranslado);
										conceptosForm.setBaseTranslado(baseTranslado);
										conceptosForm.setTipoFactorTranslado(tipoFactorTranslado);
										conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
										conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
										conceptosForm.setImporteTranslado(importeTranslado);
										
										listaConceptos.add(conceptosForm);
										conceptosForm = new ConceptosForm();
										// bandRetencion = false;
										
										conceptosForm.setImporte(conceptoXML.getImporte());
										conceptosForm.setDescripcion(conceptoXML.getDescripcion());
										conceptosForm.setClaveUnidad(conceptoXML.getClaveUnidad());
										conceptosForm.setClaveProdServ(conceptoXML.getClaveProdServ());
										conceptosForm.setCantidad(conceptoXML.getCantidad());
										conceptosForm.setValorUnitario(conceptoXML.getValorUnitario());
										conceptosForm.setNoIdentificacion(conceptoXML.getNoIdentificacion());
										conceptosForm.setUnidad(conceptoXML.getUnidad());
									}
									
									if ("002".equalsIgnoreCase(retencionC.getImpuesto())) {
										conceptosForm.setClaveImpuestoRetencion("IVA");
										conceptosForm.setBaseRetencion(retencionC.getBase());
										conceptosForm.setImpuestoRetencion(retencionC.getImpuesto());
										conceptosForm.setTipoFactorRetencion(retencionC.getTipoFactor());
										conceptosForm.setTasaOCuotaRetencion(retencionC.getTasaOCuota());
										conceptosForm.setImporteRetencion(retencionC.getImporte());
										
										conceptosForm.setClaveImpuestoTranslado(claveImpuestoTranslado);
										conceptosForm.setImpuestoTranslado(impuestoTranslado);
										conceptosForm.setBaseTranslado(baseTranslado);
										conceptosForm.setTipoFactorTranslado(tipoFactorTranslado);
										conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
										conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
										conceptosForm.setImporteTranslado(importeTranslado);
										
										listaConceptos.add(conceptosForm);
										conceptosForm = new ConceptosForm();
										// bandRetencion = false;
										
										conceptosForm.setImporte(conceptoXML.getImporte());
										conceptosForm.setDescripcion(conceptoXML.getDescripcion());
										conceptosForm.setClaveUnidad(conceptoXML.getClaveUnidad());
										conceptosForm.setClaveProdServ(conceptoXML.getClaveProdServ());
										conceptosForm.setCantidad(conceptoXML.getCantidad());
										conceptosForm.setValorUnitario(conceptoXML.getValorUnitario());
										conceptosForm.setNoIdentificacion(conceptoXML.getNoIdentificacion());
										conceptosForm.setUnidad(conceptoXML.getUnidad());
									}
								}
							}
						}else {
							if ("3.2".equalsIgnoreCase(_comprobante.getVersion())) {
								ArrayList<Traslado> listaTraslados32 = _comprobante.getImpuestos().getTraslados();
								if (listaTraslados32 != null) {
									for (Traslado traslado : listaTraslados32) {
										if ("IVA".equalsIgnoreCase(traslado.getImpuesto())) {
											baseTranslado = conceptosForm.getImporte();
											tipoFactorTranslado = traslado.getTipoFactor();
											tasaOCuotaTranslado = traslado.getTasaOCuota();
											importeTranslado = traslado.getImporte();
											claveImpuestoTranslado = "IVA";
											impuestoTranslado = "002";
											
											conceptosForm.setClaveImpuestoTranslado(claveImpuestoTranslado);
											conceptosForm.setImpuestoTranslado(impuestoTranslado);
											conceptosForm.setBaseTranslado(baseTranslado);
											conceptosForm.setTipoFactorTranslado(tipoFactorTranslado);
											conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
											conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
											conceptosForm.setImporteTranslado(importeTranslado);
											
										}
									}
								}
								
								ArrayList<Retencion> listaRetenciones32 = _comprobante.getImpuestos().getRetenciones();
								if (listaRetenciones32 == null || listaRetenciones32.size() == 0) {
									listaConceptos.add(conceptosForm);
									conceptosForm = new ConceptosForm();
								}else {
									for (Retencion retencion : listaRetenciones32) {
										if ("ISR".equalsIgnoreCase(retencion.getImpuesto())) {
											
											conceptosForm.setClaveImpuestoRetencion("ISR");
											conceptosForm.setBaseRetencion(conceptosForm.getImporte());
											conceptosForm.setImpuestoRetencion("001");
											conceptosForm.setTipoFactorRetencion("");
											conceptosForm.setTasaOCuotaRetencion(0);
											conceptosForm.setImporteRetencion(retencion.getImporte());
											
											conceptosForm.setClaveImpuestoTranslado(claveImpuestoTranslado);
											conceptosForm.setImpuestoTranslado(impuestoTranslado);
											conceptosForm.setBaseTranslado(baseTranslado);
											conceptosForm.setTipoFactorTranslado(tipoFactorTranslado);
											conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
											conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
											conceptosForm.setImporteTranslado(importeTranslado);
											
											listaConceptos.add(conceptosForm);
											conceptosForm = new ConceptosForm();
											// bandRetencion = false;
											
											conceptosForm.setImporte(conceptoXML.getImporte());
											conceptosForm.setDescripcion(conceptoXML.getDescripcion());
											conceptosForm.setClaveUnidad(conceptoXML.getClaveUnidad());
											conceptosForm.setClaveProdServ(conceptoXML.getClaveProdServ());
											conceptosForm.setCantidad(conceptoXML.getCantidad());
											conceptosForm.setValorUnitario(conceptoXML.getValorUnitario());
											conceptosForm.setNoIdentificacion(conceptoXML.getNoIdentificacion());
											conceptosForm.setUnidad(conceptoXML.getUnidad());
										}
										
										if ("IVA".equalsIgnoreCase(retencion.getImpuesto())) {
											conceptosForm.setClaveImpuestoRetencion("IVA");
											conceptosForm.setBaseRetencion(conceptosForm.getImporte());
											conceptosForm.setImpuestoRetencion("002");
											conceptosForm.setTipoFactorRetencion("");
											conceptosForm.setTasaOCuotaRetencion(0);
											conceptosForm.setImporteRetencion(retencion.getImporte());
											
											conceptosForm.setClaveImpuestoTranslado(claveImpuestoTranslado);
											conceptosForm.setImpuestoTranslado(impuestoTranslado);
											conceptosForm.setBaseTranslado(baseTranslado);
											conceptosForm.setTipoFactorTranslado(tipoFactorTranslado);
											conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
											conceptosForm.setTasaOCuotaTranslado(tasaOCuotaTranslado);
											conceptosForm.setImporteTranslado(importeTranslado);
											
											listaConceptos.add(conceptosForm);
											conceptosForm = new ConceptosForm();
											// bandRetencion = false;
											
											conceptosForm.setImporte(conceptoXML.getImporte());
											conceptosForm.setDescripcion(conceptoXML.getDescripcion());
											conceptosForm.setClaveUnidad(conceptoXML.getClaveUnidad());
											conceptosForm.setClaveProdServ(conceptoXML.getClaveProdServ());
											conceptosForm.setCantidad(conceptoXML.getCantidad());
											conceptosForm.setValorUnitario(conceptoXML.getValorUnitario());
											conceptosForm.setNoIdentificacion(conceptoXML.getNoIdentificacion());
											conceptosForm.setUnidad(conceptoXML.getUnidad());
										}
									}
								}
								
							}else {
								listaConceptos.add(conceptosForm);
								conceptosForm = new ConceptosForm();
							}
						}
				}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return listaConceptos;
	}

	public void generarExcelComplemento( Connection conSat, String esquemaSAT, SXSSFSheet hoja, ArrayList<Comprobante> listaComprobantes, SXSSFWorkbook objLibro, boolean isExportar, HashMap<String, String> MAPA_VALIDACION_SAT) {
		/* String[] encabezados = {"UUID",	"RFC Receptor",	"Nombre Receptor", "Fecha Comprobante", "Fecha Pago", "Serie", "Folio", "RFC Emisor", "Nombre Emisor",  "Moneda Pago",
				 "Forma Pago Desc", "Tipo Cambio Pago", "Monto Pago", "DR Serie", "DR Folio", "DR Moneda", "DR Saldo Anterior", "DR Importe Pagado", "DR Saldo Insoluto", "DR Identificador",
				 "Estatus", "Tipo Documento", "Tipo Relación", "Tipo Relación Desc", "UUID Relacionado", "Versión Compl. Pago", "Año Fecha Pago", "Mes Fecha Pago", "Forma Pago", "Moneda Pago Desc",
				 "Núm. Operación Pago", "RFC Banco Orden.", "Nombre Banco Extr.", "Cta. Banco Orden.", "RFC  Banco Benef.", "Cta. Banco Benef.", "Tipo Cadena Pago", "UUID", "Versión Comprobante",
				 "Año Comprobante", "Mes Comprobante", "Núm. Certificado SAT", "Lugar Expedición", "Lugar Expedición Desc", "Fecha Timbrado", "Tipo Cadena Pago Desc", "Certificado Pago",
				 "Cadena Pago", "Sello Pago", "Año Timbrado", "Mes Timbrado", "DR Moneda Desc", "DR Tipo Cambio", "DR Método Pago", "DR Método Pago Desc", "DR Núm. Parcialidad",
				 "Guid", "Total", "Tipo Comprobante Desc", "Tipo Comprobante", "IdPago"};
			*/
		
		 String[] encabezados = {"UUID",	"RFC Receptor",	"Nombre Receptor", "Fecha Comprobante", "Fecha Pago", "Serie", "Folio", "RFC Emisor", "Nombre Emisor",  "Moneda Pago",
				 "Forma Pago Desc", "Tipo Cambio Pago", "Monto Pago", "DR Serie", "DR Folio", "DR Moneda", "DR Saldo Anterior", "DR Importe Pagado", "DR Saldo Insoluto", "DR Identificador",
				 "Tipo Documento", "Tipo Relación", "Tipo Relación Desc", "UUID Relacionado", "Versión Compl. Pago", "Año Fecha Pago", "Mes Fecha Pago", "Forma Pago", "Moneda Pago Desc",
				 "Núm. Operación Pago", "RFC Banco Orden.", "Nombre Banco Extr.", "Cta. Banco Orden.", "RFC  Banco Benef.", "Cta. Banco Benef.", "Tipo Cadena Pago", "UUID", "Versión Comprobante",
				 "Año Comprobante", "Mes Comprobante", "Núm. Certificado SAT", "Lugar Expedición", "Lugar Expedición Desc", "Fecha Timbrado", "Certificado Pago",
				 "Cadena Pago", "Sello Pago", "Año Timbrado", "Mes Timbrado", "DR Moneda Desc", "DR Tipo Cambio", "DR Método Pago", "DR Método Pago Desc", "DR Núm. Parcialidad",
				 "Total", "Tipo Comprobante Desc", "Tipo Comprobante"};
		 // Estatus
		 // Tipo Cadena Pago Desc
		 // Guid
		 // IdPago
		 
		  try {
			  
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
		       
		       
			   int numColE = 1;
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 15000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 15000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   hoja.setColumnWidth(numColE++, 10000);
			   
			  
			   Row header = hoja.createRow(0);
			   header.setHeightInPoints(18);
			   
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("SIAREX - Sistema de Recepcion de XML");
			   monthCell.setCellStyle(styleTitulo);
			   
			   if (isExportar) {
				   //hoja.addMergedRegion(CellRangeAddress.valueOf("A1:BJ1"));
				   hoja.addMergedRegion(CellRangeAddress.valueOf("A1:BF1"));
			   }else {
				   //hoja.addMergedRegion(CellRangeAddress.valueOf("A1:BI1"));   
				   hoja.addMergedRegion(CellRangeAddress.valueOf("A1:BE1"));
			   }
			   
			   header = hoja.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle XML");
			    //estiloCelda2.setAlignment(HSSFCellStyle. ALIGN_CENTER);
			   monthCell2.setCellStyle(styleSubTitulo);
			   
			   if (isExportar) {
				   hoja.addMergedRegion(CellRangeAddress.valueOf("A2:BF2"));
			   }else {
				   hoja.addMergedRegion(CellRangeAddress.valueOf("A2:BJ2"));   
			   }
			   
			  header = hoja.createRow(2);
			  header.setHeightInPoints(18);

			  int numColumna = 0;
			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    // monthCell.setCellStyle(encabezadoDetalle);
			    numColumna++;
			  }
			  
			  if (isExportar) {
				    monthCell = header.createCell(numColumna);
				    monthCell.setCellValue("Estatus SAT");
				   // monthCell.setCellStyle(encabezadoDetalle);
			  }
			  
			  Cell celda = null;
			  Row fila = null;

			  Comprobante _comprobante = null;
			  int numCelda = 0;
			  String fechaComprobante = null;
			  String fechaPago = null;
			  String monedaPago = null;
			  double tipoCambioP = 0;
			  double montoP = 0;
			  String versionP = null;
			  String formaDePagoP = null;
			  String NumOperacionP = null;
			  String rfcEmisorCtaOrdP  = null;
			  String nomBancoOrdExtP = null;
			  String ctaOrdenanteP  = null;
			  String ctaBeneficiarioP   = null;
			  String tipoCadPagoP   = null;
			  String certPagoP = null;
			  String cadPagoP = null;
			  String SelloPagoP = null;
			  String rfcEmisorCtaBenP = null;
			  
			  
			  String fechaTimbrado = null;
			  
			  ArrayList<DoctoRelacionado> listaDoctoRelacionado20 = null;
			  ArrayList<PDoctoRelacionado> listaDoctoRelacionado = null;
			  ArrayList<DoctoRelacionadoForm> listaDoctosRelacionados = null;
			  DoctoRelacionadoForm _doctoRelacionado = null;
			  
			 //  String CATALOGO_SAT = "CATALOGO_SAT";
			 // String DE_DONDE_SALE = "De donde sale ?";
			  
			 //  String CATALOGO_SAT = "";
			  String DE_DONDE_SALE = "";
			  
			  // SE LLENAN LOS CATALOGOS
			  CatalogosBean catalogosBean = new CatalogosBean();
			   
			  HashMap<String, CatalogosForm> mapaMonedas =  catalogosBean.detalleMoneda(conSat, esquemaSAT);
			  CatalogosForm monedaPagoForm = null;
			  
			  HashMap<String, CatalogosForm> mapaFormas =  catalogosBean.detalleFormas(conSat, esquemaSAT);
			  CatalogosForm formaPagoForm = null;
			  
			  HashMap<String, CatalogosForm> mapaTipoRelacion =  catalogosBean.detalleTipoRelacion(conSat, esquemaSAT);
			  CatalogosForm tipoRelacionForm = null;
			  
			  
			  HashMap<String, CatalogosForm> mapaCodigoPostal =  catalogosBean.detalleCodigoPostal(conSat, esquemaSAT);
			  CatalogosForm codigoPostalForm = null;
			  
			  
			  HashMap<String, CatalogosForm> mapaMetodoPago =  catalogosBean.detalleMetodoPago(conSat, esquemaSAT);
			  CatalogosForm metodoPagoForm = null;
			  
			  int numRow = 3;
			  for (int x = 0; x < listaComprobantes.size(); x++) {
				  _comprobante = listaComprobantes.get(x);
				  
				  if (_comprobante != null && "P".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
					  
					  
					  if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
		                    ((SXSSFSheet) hoja).flushRows(100); // Retain the last 100 rows in memory
		                }

					  
					  listaDoctoRelacionado20 = null;
					  listaDoctoRelacionado = null;
					  if ("4.0".equalsIgnoreCase(_comprobante.getVersion())) {
						  listaDoctoRelacionado20 = _comprobante.getComplemento().getPagos20().getPago().get(0).getDoctoRelacionado();
					  }else {
						  listaDoctoRelacionado = _comprobante.getComplemento().getPagos().getPago().get(0).getDoctoRelacionado();
					  }
					  
					  listaDoctosRelacionados  = getDoctoRelacionados(listaDoctoRelacionado20, listaDoctoRelacionado);
					  for (int y = 0; y < listaDoctosRelacionados.size(); y++) {
						  _doctoRelacionado = listaDoctosRelacionados.get(y);
						  fila = hoja.createRow(numRow++);
						  
						   numCelda = 0;
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getComplemento().TimbreFiscalDigital.getUUID());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getReceptor().getRfc());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getReceptor().getNombre());
						   
						   
						   fechaComprobante = UtilsFechas.formatFechaSat(_comprobante.getFecha());
							   
						   if (fechaComprobante.length() > 10) {
							   fechaComprobante = fechaComprobante.substring(0, 10);
						   }
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaComprobante);
						   
						   
						   if ("4.0".equalsIgnoreCase(_comprobante.getVersion())) {
							   fechaPago = UtilsFechas.formatFechaSat(_comprobante.getComplemento().getPagos20().getPago().get(0).getFechaPago());
							   monedaPago = _comprobante.getComplemento().getPagos20().getPago().get(0).getMonedaP();
							   tipoCambioP = _comprobante.getComplemento().getPagos20().getPago().get(0).getTipoCambioP();
							   montoP = _comprobante.getComplemento().getPagos20().getPago().get(0).getMonto();
							   versionP = _comprobante.getComplemento().getPagos20().getVersion();
							   formaDePagoP = _comprobante.getComplemento().getPagos20().getPago().get(0).getFormaDePagoP();
							   NumOperacionP= _comprobante.getComplemento().getPagos20().getPago().get(0).getNumOperacion();
							   rfcEmisorCtaOrdP = _comprobante.getComplemento().getPagos20().getPago().get(0).getRfcEmisorCtaOrd();
							   nomBancoOrdExtP = _comprobante.getComplemento().getPagos20().getPago().get(0).getNomBancoOrdExt();
							   ctaOrdenanteP = _comprobante.getComplemento().getPagos20().getPago().get(0).getCtaOrdenante();
							   tipoCadPagoP= _comprobante.getComplemento().getPagos20().getPago().get(0).getTipoCadPago();
							   certPagoP = _comprobante.getComplemento().getPagos20().getPago().get(0).getCertPago();
							   cadPagoP = _comprobante.getComplemento().getPagos20().getPago().get(0).getCadPago();
							   SelloPagoP  = _comprobante.getComplemento().getPagos20().getPago().get(0).getSelloPago();
							   ctaBeneficiarioP = _comprobante.getComplemento().getPagos20().getPago().get(0).getCtaBeneficiario();
							   rfcEmisorCtaBenP	   = _comprobante.getComplemento().getPagos20().getPago().get(0).getRfcEmisorCtaBen();
							   
						   }else {
							   fechaPago = UtilsFechas.formatFechaSat(_comprobante.getComplemento().getPagos().getPago().get(0).getFechaPago());
							   monedaPago = _comprobante.getComplemento().getPagos().getPago().get(0).getMonedaP();
							   tipoCambioP = _comprobante.getComplemento().getPagos().getPago().get(0).getTipoCambioP();
							   montoP = _comprobante.getComplemento().getPagos().getPago().get(0).getMonto();
							   versionP = _comprobante.getComplemento().getPagos().getVersion();
							   formaDePagoP = _comprobante.getComplemento().getPagos().getPago().get(0).getFormaDePagoP();
							   NumOperacionP= _comprobante.getComplemento().getPagos().getPago().get(0).getNumOperacion();
							   rfcEmisorCtaOrdP = _comprobante.getComplemento().getPagos().getPago().get(0).getRfcEmisorCtaOrd();
							   nomBancoOrdExtP = _comprobante.getComplemento().getPagos().getPago().get(0).getNomBancoOrdExt();
							   ctaOrdenanteP = _comprobante.getComplemento().getPagos().getPago().get(0).getCtaOrdenante();
							   tipoCadPagoP= _comprobante.getComplemento().getPagos().getPago().get(0).getTipoCadPago();
							   certPagoP = _comprobante.getComplemento().getPagos().getPago().get(0).getCertPago();
							   cadPagoP = _comprobante.getComplemento().getPagos().getPago().get(0).getCadPago();
							   SelloPagoP  = _comprobante.getComplemento().getPagos().getPago().get(0).getSelloPago();
							   ctaBeneficiarioP = _comprobante.getComplemento().getPagos().getPago().get(0).getCtaBeneficiario();
							   rfcEmisorCtaBenP	   = _comprobante.getComplemento().getPagos().getPago().get(0).getRfcEmisorCtaBen();
						   }
						   if (fechaPago.length() > 10) {
							   fechaPago = fechaPago.substring(0, 10);
						   }
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaPago);
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getSerie());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getFolio());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getEmisor().getRfc());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getEmisor().getNombre());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(monedaPago);
						   
						   
						   formaPagoForm = mapaFormas.get(formaDePagoP);
						   if (formaPagoForm == null) {
							   formaPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(formaPagoForm.getDescripcion()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(tipoCambioP));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(montoP));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_doctoRelacionado.getSerie());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_doctoRelacionado.getFolio());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_doctoRelacionado.getMonedaDR());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(_doctoRelacionado.getImpSaldoAnt()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(_doctoRelacionado.getImpPagado()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(_doctoRelacionado.getImpSaldoInsoluto()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_doctoRelacionado.getIdDocumento());
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("CFDI");
						   
						   
						   if (_comprobante.getCfdiRelacionados() == null) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
							   tipoRelacionForm = new CatalogosForm();
						   }else {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(_comprobante.getCfdiRelacionados().getTipoRelacion());
							   
							   tipoRelacionForm = mapaTipoRelacion.get(_comprobante.getCfdiRelacionados().getTipoRelacion());
							   if (tipoRelacionForm == null) {
								   tipoRelacionForm = new CatalogosForm();
							   }
									   
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(tipoRelacionForm.getDescripcion()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_doctoRelacionado.getIdDocumento());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(versionP);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaPago.substring(0, 4));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaPago.substring(5, 7));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(formaDePagoP);
						   
						   monedaPagoForm = mapaMonedas.get(monedaPago);
						   if (monedaPagoForm == null) {
							   monedaPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(monedaPagoForm.getDescripcion()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(NumOperacionP);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(rfcEmisorCtaOrdP);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(nomBancoOrdExtP);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(ctaOrdenanteP);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue( rfcEmisorCtaBenP );
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue( ctaBeneficiarioP );
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(tipoCadPagoP);
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getComplemento().TimbreFiscalDigital.getUUID());
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getVersion());
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaComprobante.substring(0, 4));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaComprobante.substring(5, 7));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getNoCertificadoSAT());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getLugarExpedicion());
						   
						   
						   
						   codigoPostalForm = mapaCodigoPostal.get(_comprobante.getLugarExpedicion());
						   if (codigoPostalForm == null) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
						   }else {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(codigoPostalForm.getCodigoPostal() + ", " + codigoPostalForm.getEstado());
						   }
						   
						   
						   fechaTimbrado = UtilsFechas.formatFechaSat(_comprobante.getComplemento().getTimbreFiscalDigital().getFechaTimbrado());
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaTimbrado.substring(0, 10));
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(certPagoP);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(cadPagoP);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(SelloPagoP);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaTimbrado.substring(0, 4));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaTimbrado.substring(5, 7));
						   
						   
						   monedaPagoForm = mapaMonedas.get(_doctoRelacionado.getMonedaDR());
						   if (monedaPagoForm == null) {
							   monedaPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(monedaPagoForm.getDescripcion());
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_doctoRelacionado.getTipoCambioDR());
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_doctoRelacionado.getMetodoDePagoDR());
						   
						   
						   metodoPagoForm = mapaMetodoPago.get(_doctoRelacionado.getMetodoDePagoDR());
						   if (metodoPagoForm == null) {
							   metodoPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(metodoPagoForm.getDescripcion()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_doctoRelacionado.getNumParcialidad());
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getTotal());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("Pago");
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getTipoDeComprobante());
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   if (isExportar) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloNormal(MAPA_VALIDACION_SAT.get(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID())));   
						   }
						   
					  }
					  
				  }
				  
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("xmlToExcelP(): ", e);
		  }
	}
	

	
	public void generarExcelFacturas(Connection conSAT, String esquemaSAT, SXSSFSheet hojaFacturas, ArrayList<Comprobante> listaFacturas, SXSSFWorkbook objLibro, boolean isExportar, HashMap<String, String> MAPA_VALIDACION_SAT) {
	/*	 final String[] encabezados = {
				 "UUID", "RFC Receptor", "Nombre Receptor", "RFC Emisor", "Nombre Emisor", "Fecha Comprobante", "Fecha Timbrado",  "Serie",
				 "Folio", "Forma Pago", "Forma Pago Desc", "Método Pago", "Condiciones Pago", "Lugar Expedición", "Uso CFDI", "Uso CFDI Desc",
				 "Régimen Fiscal", "Régimen Fiscal Desc", "Clave Unidad", "Clave Unidad Desc", "Clave Prod/Serv", "Cantidad", "Valor Unitario",
				 "Descripción", "Moneda", "Importe", "Clave Impuesto Tras. Desc.", "Clave Impuesto Tras.", "Base Traslado", "Importe Traslado",
				 "Tasa Cuota Tras.",  "Clave Impuesto Ret. Desc",  "Importe Retención", "Tasa Cuota Ret.", "Clave Impuesto Ret.", "Tipo Cambio",
				 "Subtotal", "Descuento", "Tipo Factor Ret.", "Tipo Factor Tras.", "ImpLoc Total Traslados", "ImpLoc Tasa Trasladado", "ImpLoc Tasa Retención",
				 "ImpLoc Nombre Retención", "ImpLoc Importe Trasladado", "ImpLoc Total Retenciones", "ImpLoc Importe Retención", "ImpLoc Nombre Trasladado",
				 "Total Impuestos Retenidos", "Total Impuestos Trasladados", "Total IVA Trasladado", "Total ISR Retenido", "Total IVA Retenido",
				 "Total IEPS Retenido", "Total IEPS Trasladado", "Base Retención",	"Total", "Núm. Identificación", "Unidad", "Clave Prod/Serv Desc",
				 "Tipo Relación", "Tipo Relación Desc", "UUID Relacionado", "Estatus", "Proceso", "Tipo Documento", 
				 "Año Comprobante", "Versión Comprobante", "Mes Comprobante", "Núm. Certificado SAT", "Método Pago Desc", "Núm. Cta. Pago", "Lugar Expedición Desc",
				 "Año Timbrado", "Mes Timbrado", "Receptor Residencia Fiscal", "Receptor Residencia Fiscal Desc", "Receptor Núm. Registro Tributario",
				 "Moneda Desc", "Total Descuento", "Tipo Comprobante", "Tipo Comprobante Desc", "Guid", "Tipo Comprobante"
				 
		 };
		*/
		
		 final String[] encabezados = {
				 "UUID", "RFC Receptor", "Nombre Receptor", "RFC Emisor", "Nombre Emisor", "Fecha Comprobante", "Fecha Timbrado",  "Serie",
				 "Folio", "Forma Pago", "Forma Pago Desc", "Método Pago", "Condiciones Pago", "Lugar Expedición", "Uso CFDI", "Uso CFDI Desc",
				 "Régimen Fiscal", "Régimen Fiscal Desc", "Clave Unidad", "Clave Unidad Desc", "Clave Prod/Serv", "Cantidad", "Valor Unitario",
				 "Descripción", "Moneda", "Importe", "Clave Impuesto Tras. Desc.", "Clave Impuesto Tras.", "Base Traslado", "Importe Traslado",
				 "Tasa Cuota Tras.",  "Clave Impuesto Ret. Desc",  "Importe Retención", "Tasa Cuota Ret.", "Clave Impuesto Ret.", "Tipo Cambio",
				 "Subtotal", "Descuento", "Tipo Factor Ret.", "Tipo Factor Tras.", 
				 "Total Impuestos Retenidos", "Total Impuestos Trasladados", "Total IVA Trasladado", "Total ISR Retenido", "Total IVA Retenido",
				 "Base Retención",	"Total", "Núm. Identificación", "Unidad", "Clave Prod/Serv Desc",
				 "Tipo Relación", "Tipo Relación Desc", "UUID Relacionado", "Estatus", "Tipo Documento", 
				 "Año Comprobante", "Versión Comprobante", "Mes Comprobante", "Núm. Certificado SAT", "Método Pago Desc", "Lugar Expedición Desc",
				 "Año Timbrado", "Mes Timbrado", 
				 "Moneda Desc", "Total Descuento", "Tipo Comprobante", "Tipo Comprobante Desc", "Tipo Comprobante"
				 
		 };
// ImpLoc Total Traslados
// ImpLoc Tasa Trasladado
// ImpLoc Tasa Retención
// ImpLoc Nombre Retención
// ImpLoc Importe Trasladado
// ImpLoc Total Retenciones
// ImpLoc Importe Retención
// ImpLoc Nombre Trasladado
// Total IEPS Retenido
// Total IEPS Trasladado
// Proceso
// Núm. Cta. Pago
// Receptor Residencia Fiscal
// Receptor Residencia Fiscal Desc
// Receptor Núm. Registro Tributario
// Guid
		 
		  try {
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
		       
			  int numColE = 1;
			  hojaFacturas.setColumnWidth(0, 10000);
			   
			  
			   Row header = hojaFacturas.createRow(0);
			   header.setHeightInPoints(18);
			   
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("SIAREX - Sistema de Recepcion de XML ( Facturas )");
			   monthCell.setCellStyle(styleTitulo);
			   
			   if (isExportar) {
				   //hojaFacturas.addMergedRegion(CellRangeAddress.valueOf("A1:CG1"));
				   hojaFacturas.addMergedRegion(CellRangeAddress.valueOf("A1:BQ1"));
			   }else {
				   //hojaFacturas.addMergedRegion(CellRangeAddress.valueOf("A1:CF1"));   
				   hojaFacturas.addMergedRegion(CellRangeAddress.valueOf("A1:Bp1"));
			   }
			   
			  
			   header = hojaFacturas.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle XML");
			    //estiloCelda2.setAlignment(HSSFCellStyle. ALIGN_CENTER);
			   monthCell2.setCellStyle(styleSubTitulo);
			   hojaFacturas.addMergedRegion(CellRangeAddress.valueOf("A2:CM2"));
			    
			  header = hojaFacturas.createRow(2);
			  header.setHeightInPoints(18);

			  int numColumna = 0;
			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    // monthCell.setCellStyle(encabezadoDetalle);
			    numColumna++;
			  }
			  
			  if (isExportar) {
				    monthCell = header.createCell(numColumna);
				    monthCell.setCellValue("Estatus SAT");
				   // monthCell.setCellStyle(encabezadoDetalle);
			  }
			  
			  
			  Cell celda = null;
			  Row fila = null;

			  Comprobante _comprobante = null;
			  String fechaComprobante = null;
			  String fechaTimbrado = null;
			  
			  int numCelda = 0;
			  
			  // String CATALOGO_SAT = "CATALOGO_SAT";
			 String DE_DONDE_SALE = "De donde sale ?";
			  
			 //  String CATALOGO_SAT = " ";
			   //String DE_DONDE_SALE = "DE_DONDE_SALE";
			 // String DE_DONDE_SALE = "";
			  
			// SE LLENAN LOS CATALOGOS
			    CatalogosBean catalogosBean = new CatalogosBean();
				  HashMap<String, CatalogosForm> mapaMonedas =  catalogosBean.detalleMoneda(conSAT, esquemaSAT);
				  CatalogosForm monedaPagoForm = null;
				  
				  HashMap<String, CatalogosForm> mapaFormas =  catalogosBean.detalleFormas(conSAT, esquemaSAT);
				  CatalogosForm formaPagoForm = null;
				  
				  HashMap<String, CatalogosForm> mapaTipoRelacion =  catalogosBean.detalleTipoRelacion(conSAT, esquemaSAT);
				  CatalogosForm tipoRelacionForm = null;
				  
				  HashMap<String, CatalogosForm> mapaCodigoPostal =  catalogosBean.detalleCodigoPostal(conSAT, esquemaSAT);
				  CatalogosForm codigoPostalForm = null;
				  
				  HashMap<String, CatalogosForm> mapaMetodoPago =  catalogosBean.detalleMetodoPago(conSAT, esquemaSAT);
				  CatalogosForm metodoPagoForm = null;
				  
				  HashMap<String, CatalogosForm> mapaRegimen =  catalogosBean.detalleRegimenFiscal(conSAT, esquemaSAT);
				  CatalogosForm regimenFiscalForm = null;
			   
				  HashMap<String, CatalogosForm> mapaUso =  catalogosBean.detalleUsoCFDI(conSAT, esquemaSAT);
				  CatalogosForm usoCFDIForm = null;
				  
				  HashMap<String, CatalogosForm> mapaProductos =  catalogosBean.detalleClaveProductos(conSAT, esquemaSAT);
				  CatalogosForm claveProductoForm = null;
				  
				  
				  
			  ArrayList<ConceptosForm> listaConceptos = null;
			  ConceptosForm conceptosForm = null;
			  int numRow = 3;
			  for (int x = 0; x < listaFacturas.size(); x++) {
				  _comprobante = listaFacturas.get(x);
				  // logger.info("Obteniendo UUID..."+_comprobante.getComplemento().getTimbreFiscalDigital().getUUID());
				  
				  if (_comprobante != null && ("I".equalsIgnoreCase(_comprobante.getTipoDeComprobante()) || "ingreso".equalsIgnoreCase(_comprobante.getTipoDeComprobante()))) {
					  
					  if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
		                    ((SXSSFSheet) hojaFacturas).flushRows(100); // Retain the last 100 rows in memory
		                }
					  
					  listaConceptos = getConceptos(_comprobante.getConceptos().getConcepto(), _comprobante);
					  
					  for (int y = 0; y < listaConceptos.size(); y++) {
						   numCelda = 0;
						   conceptosForm = listaConceptos.get(y);
						   fila = hojaFacturas.createRow(numRow++);
						   
						   
						   double totalIvaTranslado = 0;
						   double totalIsrRetenido = 0;
						   double totalIvaRetenido = 0;
						  // double totalIepsRetenido = 0;
						  // double totalIepsTranslado = 0;
						   
						   double totalImpuestoRetenido = 0;
						   double totalImpuestoTranslado = 0;
						   
						   
						   if (_comprobante.getImpuestos() != null) {
							   
							   totalImpuestoRetenido = _comprobante.getImpuestos().getTotalImpuestosRetenidos();
							   totalImpuestoTranslado = _comprobante.getImpuestos().getTotalImpuestosTrasladados();
							   
							   if (totalImpuestoRetenido == -1) {
								   totalImpuestoRetenido = 0;
							   }
							   
							   if (totalImpuestoTranslado == -1) {
								   totalImpuestoTranslado = 0;
							   }
							   
							   
							   ArrayList<Traslado> listaTraslado = _comprobante.getImpuestos().getTraslados();
							   if (listaTraslado == null ) {
								   totalIvaTranslado = 0;
							   }else {
								   for (int a = 0; a < listaTraslado.size(); a++) {
									   Traslado traslado = listaTraslado.get(a);
									   
									   if ("002".equalsIgnoreCase(traslado.getImpuesto())) {
										   totalIvaTranslado = traslado.getImporte();
									   }  
								   }
							   }
							   
							   ArrayList<Retencion> listaRetencion = _comprobante.getImpuestos().getRetenciones();
							   if (listaRetencion == null ) {
								   totalIsrRetenido = 0;
							   }else {
								   for (int a = 0; a < listaRetencion.size(); a++) {
									   Retencion retencion = listaRetencion.get(a);
									   
									   if ("001".equalsIgnoreCase(retencion.getImpuesto())) {
										   totalIsrRetenido = retencion.getImporte();
									   }  
									   
									   if ("002".equalsIgnoreCase(retencion.getImpuesto())) {
										   totalIvaRetenido = retencion.getImporte();
									   }  
								   }
							   }
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID());

						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getReceptor().getRfc());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getReceptor().getNombre());

						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getEmisor().getRfc());

						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getEmisor().getNombre());
						   
						   fechaComprobante = UtilsFechas.formatFechaSat(_comprobante.getFecha());
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaComprobante.substring(0, 10));
						   
						   fechaTimbrado = UtilsFechas.formatFechaSat(_comprobante.getComplemento().getTimbreFiscalDigital().getFechaTimbrado());
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaTimbrado.substring(0, 10));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getSerie());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getFolio());
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getFormaPago());
						   
						   
						   formaPagoForm = mapaFormas.get(_comprobante.getFormaPago());
						   if (formaPagoForm == null) {
							   formaPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(formaPagoForm.getDescripcion()));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getMetodoPago());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getCondicionesDePago());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getLugarExpedicion());
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getReceptor().getUsoCFDI());
						   
						   
						   usoCFDIForm = mapaUso.get(_comprobante.getReceptor().getUsoCFDI());
						   if (usoCFDIForm == null) {
							   usoCFDIForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(usoCFDIForm.getDescripcion()));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getEmisor().getRegimenFiscal());
						   
						   
						   
						   regimenFiscalForm = mapaRegimen.get(_comprobante.getEmisor().getRegimenFiscal());
						   if (regimenFiscalForm == null) {
							   regimenFiscalForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(regimenFiscalForm.getDescripcion()));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getClaveUnidad());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getUnidad());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getClaveProdServ());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getCantidad());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getValorUnitario() ));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getDescripcion());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getMoneda());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getImporte()));
						   
						   /*if ("3.2".equalsIgnoreCase(_comprobante.getVersion())) {
							   ArrayList<Traslado> _traslados = _comprobante.getImpuestos().getTraslados();
							   if (_traslados != null) {
								   for (Traslado traslado : _traslados) {
									   if ("IVA".equalsIgnoreCase(traslado.getImpuesto())) {
										   celda = fila.createCell(numCelda++);
										   celda.setCellValue(traslado.getImpuesto());
										   
										   celda = fila.createCell(numCelda++);
										   celda.setCellValue("002");

										   celda = fila.createCell(numCelda++);
										   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getImporte()));
										   
										   celda = fila.createCell(numCelda++);
										   celda.setCellValue(Utils.noNuloDouble( traslado.getImporte()));
										   
										   celda = fila.createCell(numCelda++);
										   celda.setCellValue(Utils.noNuloDouble( traslado.getTasaOCuota()));
									   }
								   }
							   }else {
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue("");
								   
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue("");

								   celda = fila.createCell(numCelda++);
								   celda.setCellValue(0);
								   
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue(0);
								   
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue(0);
							   }
						   }else {*/
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(conceptosForm.getClaveImpuestoTranslado());

							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(conceptosForm.getImpuestoTranslado());
							   
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getBaseTranslado()));
							   
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getImporteTranslado()));
							   
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getTasaOCuotaTranslado()));
						  // }
						   
						   
						  /* if ("3.2".equalsIgnoreCase(_comprobante.getVersion())) {
							   ArrayList<Retencion> _retenciones = _comprobante.getImpuestos().getRetenciones();
							   if (_retenciones != null) {
								   double importeRetencion = 0;
								   for (Retencion retencion : _retenciones) {
									   importeRetencion+=retencion.getImporte();
								   }

								   celda = fila.createCell(numCelda++);
								   celda.setCellValue("PDT");
								   
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue(importeRetencion);
								   
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue(5000);
								  
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue("PDT");
								   
							   }
							   
						    }else { */
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(conceptosForm.getClaveImpuestoRetencion());
							   
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloDouble(conceptosForm.getImporteRetencion()));
							   
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloDouble(conceptosForm.getTasaOCuotaRetencion()));
							  
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(conceptosForm.getImpuestoRetencion());
							   
						   //}
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( _comprobante.getTipoCambio()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( _comprobante.getSubTotal()));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(conceptosForm.getDescuento()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getTipoFactorRetencion());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getTipoFactorTranslado());
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalImpuestoRetenido));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalImpuestoTranslado));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalIvaTranslado));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalIsrRetenido));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalIvaRetenido));
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue( Utils.noNuloDouble(conceptosForm.getBaseRetencion()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(_comprobante.getTotal()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getNoIdentificacion());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getUnidad());
						   
						   
						   
						   claveProductoForm = mapaProductos.get(conceptosForm.getClaveProdServ());
						   if (claveProductoForm == null) {
							   claveProductoForm = new CatalogosForm();
						   }
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(claveProductoForm.getDescripcion()));
						   
						   
						   
						   if (_comprobante.getCfdiRelacionados() == null) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
							   
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
							   
						   }else {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(_comprobante.getCfdiRelacionados().getTipoRelacion());
							   
							   tipoRelacionForm = mapaTipoRelacion.get(_comprobante.getCfdiRelacionados().getTipoRelacion());
							   if (tipoRelacionForm == null) {
								   tipoRelacionForm = new CatalogosForm();
							   }
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloNormal(tipoRelacionForm.getDescripcion()));
						   }
						   
						   
						    
						   if (_comprobante.getCfdiRelacionados() == null) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
						   }else {
							   ArrayList<CfdiRelacionado>  cfdiRelacionados = _comprobante.getCfdiRelacionados().getCfdiRelacionado();
							   if (cfdiRelacionados.size() > 0) {
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue(_comprobante.getCfdiRelacionados().getCfdiRelacionado().get(0).getUUID());
							   }else {
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue("");
							   }
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("Recibido");
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("CFDI");
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaComprobante.substring(0, 4));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getVersion());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaComprobante.substring(5, 7));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getNoCertificadoSAT());
						   
						   
						   metodoPagoForm = mapaMetodoPago.get(_comprobante.getMetodoPago());
						   if (metodoPagoForm == null) {
							   metodoPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(metodoPagoForm.getDescripcion()));
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   codigoPostalForm = mapaCodigoPostal.get(_comprobante.getLugarExpedicion());
						   if (codigoPostalForm == null) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
						   }else {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNulo(codigoPostalForm.getCodigoPostal()) + ", " + codigoPostalForm.getEstado());
							   
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaTimbrado.substring(0, 4));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaTimbrado.substring(5, 7));
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   
						   monedaPagoForm = mapaMonedas.get(_comprobante.getMoneda());
						   if (monedaPagoForm == null) {
							   monedaPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(monedaPagoForm.getDescripcion()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getDescuento());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getTipoDeComprobante());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("Ingreso");
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("CFDI");
						   
						   if (isExportar) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloNormal(MAPA_VALIDACION_SAT.get(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID())));   
						   }
						   
						   
					  }
					  
				  }
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("xmlToExcelP(): ", e);
		  }
	}

	
	public void generarExcelNotaCredito(Connection conSAT, String esquemaSAT, SXSSFSheet hojaNotaCredito, ArrayList<Comprobante> listaFacturas, SXSSFWorkbook objLibro, boolean isExportar, HashMap<String, String> MAPA_VALIDACION_SAT) {
		/* final String[] encabezados = {
				 "UUID", "RFC Receptor", "Nombre Receptor", "RFC Emisor", "Nombre Emisor", "Fecha Comprobante", "Fecha Timbrado",  "Serie",
				 "Folio", "Forma Pago", "Forma Pago Desc", "Método Pago", "Condiciones Pago", "Lugar Expedición", "Uso CFDI", "Uso CFDI Desc",
				 "Régimen Fiscal", "Régimen Fiscal Desc", "Clave Unidad", "Clave Unidad Desc", "Clave Prod/Serv", "Cantidad", "Valor Unitario",
				 "Descripción", "Moneda", "Importe", "Clave Impuesto Tras. Desc.", "Clave Impuesto Tras.", "Base Traslado", "Importe Traslado",
				 "Tasa Cuota Tras.",  "Clave Impuesto Ret. Desc",  "Importe Retención", "Tasa Cuota Ret.", "Clave Impuesto Ret.", "Tipo Cambio",
				 "Subtotal", "Descuento", "Tipo Factor Ret.", "Tipo Factor Tras.", "ImpLoc Total Traslados", "ImpLoc Tasa Trasladado", "ImpLoc Tasa Retención",
				 "ImpLoc Nombre Retención", "ImpLoc Importe Trasladado", "ImpLoc Total Retenciones", "ImpLoc Importe Retención", "ImpLoc Nombre Trasladado",
				 "Total Impuestos Retenidos", "Total Impuestos Trasladados", "Total IVA Trasladado", "Total ISR Retenido", "Total IVA Retenido",
				 "Total IEPS Retenido", "Total IEPS Trasladado", "Base Retención",	"Total", "Núm. Identificación", "Unidad", "Clave Prod/Serv Desc",
				 "Tipo Relación", "Tipo Relación Desc", "UUID Relacionado", "Estatus", "Proceso", "Tipo Documento", 
				 "Año Comprobante", "Versión Comprobante", "Mes Comprobante", "Núm. Certificado SAT", "Método Pago Desc", "Núm. Cta. Pago", "Lugar Expedición Desc",
				 "Año Timbrado", "Mes Timbrado", "Receptor Residencia Fiscal", "Receptor Residencia Fiscal Desc", "Receptor Núm. Registro Tributario",
				 "Moneda Desc", "Total Descuento", "Tipo Comprobante", "Tipo Comprobante Desc", "Guid", "Tipo Comprobante"
				 
		 };
	*/
		 final String[] encabezados = {
				 "UUID", "RFC Receptor", "Nombre Receptor", "RFC Emisor", "Nombre Emisor", "Fecha Comprobante", "Fecha Timbrado",  "Serie",
				 "Folio", "Forma Pago", "Forma Pago Desc", "Método Pago", "Condiciones Pago", "Lugar Expedición", "Uso CFDI", "Uso CFDI Desc",
				 "Régimen Fiscal", "Régimen Fiscal Desc", "Clave Unidad", "Clave Unidad Desc", "Clave Prod/Serv", "Cantidad", "Valor Unitario",
				 "Descripción", "Moneda", "Importe", "Clave Impuesto Tras. Desc.", "Clave Impuesto Tras.", "Base Traslado", "Importe Traslado",
				 "Tasa Cuota Tras.",  "Clave Impuesto Ret. Desc",  "Importe Retención", "Tasa Cuota Ret.", "Clave Impuesto Ret.", "Tipo Cambio",
				 "Subtotal", "Descuento", "Tipo Factor Ret.", "Tipo Factor Tras.",
				 "Total Impuestos Retenidos", "Total Impuestos Trasladados", "Total IVA Trasladado", "Total ISR Retenido", "Total IVA Retenido",
				 "Base Retención",	"Total", "Núm. Identificación", "Unidad", "Clave Prod/Serv Desc",
				 "Tipo Relación", "Tipo Relación Desc", "UUID Relacionado", "Estatus", "Tipo Documento", 
				 "Año Comprobante", "Versión Comprobante", "Mes Comprobante", "Núm. Certificado SAT", "Método Pago Desc", "Lugar Expedición Desc",
				 "Año Timbrado", "Mes Timbrado", 
				 "Moneda Desc", "Total Descuento", "Tipo Comprobante", "Tipo Comprobante Desc", "Tipo Comprobante"
				 
		 };
// ImpLoc Total Traslados
// ImpLoc Tasa Trasladado
// ImpLoc Tasa Retención
// ImpLoc Nombre Retención
// ImpLoc Importe Trasladado
// ImpLoc Total Retenciones
// ImpLoc Importe Retención
// ImpLoc Nombre Trasladado
// Total IEPS Retenido
// Total IEPS Trasladado
// Proceso
// Núm. Cta. Pago
// Receptor Residencia Fiscal
// Receptor Residencia Fiscal Desc
// Receptor Núm. Registro Tributario
// Guid

		  try {
			  
			  int numColE = 1;
			  hojaNotaCredito.setColumnWidth(0, 10000);
			   
			  
			   Row header = hojaNotaCredito.createRow(0);
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
			   monthCell.setCellValue("SIAREX - Sistema de Recepcion de XML ( Notas de Credito )");
			   monthCell.setCellStyle(styleTitulo);
			   // hojaNotaCredito.addMergedRegion(CellRangeAddress.valueOf("A1:CM1"));
			   
			   if (isExportar) {
				   //hojaNotaCredito.addMergedRegion(CellRangeAddress.valueOf("A1:CG1"));
				   hojaNotaCredito.addMergedRegion(CellRangeAddress.valueOf("A1:BQ1"));
			   }else {
				   //hojaNotaCredito.addMergedRegion(CellRangeAddress.valueOf("A1:CF1"));   
				   hojaNotaCredito.addMergedRegion(CellRangeAddress.valueOf("A1:BP1"));
			   }
			   
			   header = hojaNotaCredito.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle XML");
			   monthCell2.setCellStyle(styleSubTitulo);
			   
			    
			   if (isExportar) {
				   hojaNotaCredito.addMergedRegion(CellRangeAddress.valueOf("A2:CG2"));   
			   }else {
				   hojaNotaCredito.addMergedRegion(CellRangeAddress.valueOf("A2:CM2"));   
			   }
			   
			  header = hojaNotaCredito.createRow(2);
			  header.setHeightInPoints(18);

			  int numColumna = 0;
			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    //monthCell.setCellStyle(encabezadoDetalle);
			    numColumna++;
			  }
			  
			  if (isExportar) {
				    monthCell = header.createCell(numColumna);
				    monthCell.setCellValue("Estatus SAT");
				    //monthCell.setCellStyle(encabezadoDetalle);
			  }
			  
			  Cell celda = null;
			  Row fila = null;

			  Comprobante _comprobante = null;
			  String fechaComprobante = null;
			  String fechaTimbrado = null;
			  
			  int numCelda = 0;
			  
			  // String CATALOGO_SAT = "CATALOGO_SAT";
			  String DE_DONDE_SALE = "De donde sale ?";
			  
			 //  String CATALOGO_SAT = " ";
			  //  String DE_DONDE_SALE = "";
			  
			// SE LLENAN LOS CATALOGOS
			    CatalogosBean catalogosBean = new CatalogosBean();
				  HashMap<String, CatalogosForm> mapaMonedas =  catalogosBean.detalleMoneda(conSAT, esquemaSAT);
				  CatalogosForm monedaPagoForm = null;
				  
				  
				  HashMap<String, CatalogosForm> mapaFormas =  catalogosBean.detalleFormas(conSAT, esquemaSAT);
				  CatalogosForm formaPagoForm = null;
				  
				  HashMap<String, CatalogosForm> mapaTipoRelacion =  catalogosBean.detalleTipoRelacion(conSAT, esquemaSAT);
				  CatalogosForm tipoRelacionForm = null;
				  
				  
				  HashMap<String, CatalogosForm> mapaCodigoPostal =  catalogosBean.detalleCodigoPostal(conSAT, esquemaSAT);
				  CatalogosForm codigoPostalForm = null;
				  
				  
				  HashMap<String, CatalogosForm> mapaMetodoPago =  catalogosBean.detalleMetodoPago(conSAT, esquemaSAT);
				  CatalogosForm metodoPagoForm = null;
				  
				  
				  HashMap<String, CatalogosForm> mapaRegimen =  catalogosBean.detalleRegimenFiscal(conSAT, esquemaSAT);
				  CatalogosForm regimenFiscalForm = null;
			   
			   
				  HashMap<String, CatalogosForm> mapaUso =  catalogosBean.detalleUsoCFDI(conSAT, esquemaSAT);
				  CatalogosForm usoCFDIForm = null;
				  
				  HashMap<String, CatalogosForm> mapaProductos =  catalogosBean.detalleClaveProductos(conSAT, esquemaSAT);
				  CatalogosForm claveProductoForm = null;
				  
				  
				  
			  ArrayList<ConceptosForm> listaConceptos = null;
			  ConceptosForm conceptosForm = null;
			  int numRow = 3;
			  for (int x = 0; x < listaFacturas.size(); x++) {
				  _comprobante = listaFacturas.get(x);

				  if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hojaNotaCredito).flushRows(100); // Retain the last 100 rows in memory
	                }
				  
				  if (_comprobante != null && ("E".equalsIgnoreCase(_comprobante.getTipoDeComprobante()) || "egreso".equalsIgnoreCase(_comprobante.getTipoDeComprobante()))) {
					  listaConceptos = getConceptos(_comprobante.getConceptos().getConcepto(), _comprobante);
					  
					  for (int y = 0; y < listaConceptos.size(); y++) {
						   numCelda = 0;
						   conceptosForm = listaConceptos.get(y);
						   fila = hojaNotaCredito.createRow(numRow++);
						   
						   double totalIvaTranslado = 0;
						   double totalIsrRetenido = 0;
						   double totalIvaRetenido = 0;
						  // double totalIepsRetenido = 0;
						  // double totalIepsTranslado = 0;
						   
						   double totalImpuestoRetenido = 0;
						   double totalImpuestoTranslado = 0;
						   
						   
						   if (_comprobante.getImpuestos() != null) {
							   
							   totalImpuestoRetenido = _comprobante.getImpuestos().getTotalImpuestosRetenidos();
							   totalImpuestoTranslado = _comprobante.getImpuestos().getTotalImpuestosTrasladados();
							   
							   if (totalImpuestoRetenido == -1) {
								   totalImpuestoRetenido = 0;
							   }
							   
							   if (totalImpuestoTranslado == -1) {
								   totalImpuestoTranslado = 0;
							   }
							   
							   
							   ArrayList<Traslado> listaTraslado = _comprobante.getImpuestos().getTraslados();
							   if (listaTraslado == null ) {
								   totalIvaTranslado = 0;
							   }else {
								   for (int a = 0; a < listaTraslado.size(); a++) {
									   Traslado traslado = listaTraslado.get(a);
									   
									   if ("002".equalsIgnoreCase(traslado.getImpuesto())) {
										   totalIvaTranslado = traslado.getImporte();
									   }  
								   }
							   }
							   
							   ArrayList<Retencion> listaRetencion = _comprobante.getImpuestos().getRetenciones();
							   if (listaRetencion == null ) {
								   totalIsrRetenido = 0;
							   }else {
								   for (int a = 0; a < listaRetencion.size(); a++) {
									   Retencion retencion = listaRetencion.get(a);
									   
									   if ("001".equalsIgnoreCase(retencion.getImpuesto())) {
										   totalIsrRetenido = retencion.getImporte();
									   }  
									   
									   if ("002".equalsIgnoreCase(retencion.getImpuesto())) {
										   totalIvaRetenido = retencion.getImporte();
									   }  
								   }
							   }
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID());

						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getReceptor().getRfc());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getReceptor().getNombre());

						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getEmisor().getRfc());

						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getEmisor().getNombre());
						   
						   fechaComprobante = UtilsFechas.formatFechaSat(_comprobante.getFecha());
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaComprobante.substring(0, 10));
						   
						   fechaTimbrado = UtilsFechas.formatFechaSat(_comprobante.getComplemento().getTimbreFiscalDigital().getFechaTimbrado());
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaTimbrado.substring(0, 10));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getSerie());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getFolio());
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getFormaPago());
						   
						   
						   formaPagoForm = mapaFormas.get(_comprobante.getFormaPago());
						   if (formaPagoForm == null) {
							   formaPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(formaPagoForm.getDescripcion()));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getMetodoPago());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getCondicionesDePago());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getLugarExpedicion());
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getReceptor().getUsoCFDI());
						   
						   
						   usoCFDIForm = mapaUso.get(_comprobante.getReceptor().getUsoCFDI());
						   if (usoCFDIForm == null) {
							   usoCFDIForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(usoCFDIForm.getDescripcion()));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getEmisor().getRegimenFiscal());
						   
						   
						   
						   regimenFiscalForm = mapaRegimen.get(_comprobante.getEmisor().getRegimenFiscal());
						   if (regimenFiscalForm == null) {
							   regimenFiscalForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(regimenFiscalForm.getDescripcion()));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getClaveUnidad());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getUnidad());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getClaveProdServ());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getCantidad());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getValorUnitario() ));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getDescripcion());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getMoneda());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getImporte()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getClaveImpuestoTranslado());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getImpuestoTranslado());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getBaseTranslado()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getImporteTranslado()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( conceptosForm.getTasaOCuotaTranslado()));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getClaveImpuestoRetencion());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(conceptosForm.getImporteRetencion()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(conceptosForm.getTasaOCuotaRetencion()));
						  
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getImpuestoRetencion());
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( _comprobante.getTipoCambio()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble( _comprobante.getSubTotal()));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(conceptosForm.getDescuento()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getTipoFactorRetencion());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getTipoFactorTranslado());
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalImpuestoRetenido));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalImpuestoTranslado));
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalIvaTranslado));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalIsrRetenido));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(totalIvaRetenido));
						   
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue( Utils.noNuloDouble(conceptosForm.getBaseRetencion()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloDouble(_comprobante.getTotal()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getNoIdentificacion());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(conceptosForm.getUnidad());
						   
						   
						   
						   claveProductoForm = mapaProductos.get(conceptosForm.getClaveProdServ());
						   if (claveProductoForm == null) {
							   claveProductoForm = new CatalogosForm();
						   }
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(claveProductoForm.getDescripcion()));
						   
						   
						   
						   if (_comprobante.getCfdiRelacionados() == null) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
							   
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
							   
						   }else {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(_comprobante.getCfdiRelacionados().getTipoRelacion());
							   
							   tipoRelacionForm = mapaTipoRelacion.get(_comprobante.getCfdiRelacionados().getTipoRelacion());
							   if (tipoRelacionForm == null) {
								   tipoRelacionForm = new CatalogosForm();
							   }
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloNormal(tipoRelacionForm.getDescripcion()));
						   }
						   
						   
						    
						   if (_comprobante.getCfdiRelacionados() == null) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
						   }else {
							   ArrayList<CfdiRelacionado>  cfdiRelacionados = _comprobante.getCfdiRelacionados().getCfdiRelacionado();
							   if (cfdiRelacionados.size() > 0) {
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue(_comprobante.getCfdiRelacionados().getCfdiRelacionado().get(0).getUUID());
							   }else {
								   celda = fila.createCell(numCelda++);
								   celda.setCellValue("");
							   }
						   }
						   
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("Recibido");
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("CFDI");
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaComprobante.substring(0, 4));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getVersion());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaComprobante.substring(5, 7));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getNoCertificadoSAT());
						   
						   
						   metodoPagoForm = mapaMetodoPago.get(_comprobante.getMetodoPago());
						   if (metodoPagoForm == null) {
							   metodoPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(metodoPagoForm.getDescripcion()));
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   codigoPostalForm = mapaCodigoPostal.get(_comprobante.getLugarExpedicion());
						   if (codigoPostalForm == null) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue("");
						   }else {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNulo(codigoPostalForm.getCodigoPostal()) + ", " + codigoPostalForm.getEstado());
							   
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaTimbrado.substring(0, 4));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(fechaTimbrado.substring(5, 7));
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   
						   monedaPagoForm = mapaMonedas.get(_comprobante.getMoneda());
						   if (monedaPagoForm == null) {
							   monedaPagoForm = new CatalogosForm();
						   }
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(Utils.noNuloNormal(monedaPagoForm.getDescripcion()));
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getDescuento());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(_comprobante.getTipoDeComprobante());
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("Egreso");
						   
						   /*
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue(DE_DONDE_SALE);
						   */
						   
						   celda = fila.createCell(numCelda++);
						   celda.setCellValue("CFDI");
						   
						   if (isExportar) {
							   celda = fila.createCell(numCelda++);
							   celda.setCellValue(Utils.noNuloNormal(MAPA_VALIDACION_SAT.get(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID())));   
						   }
						   
					  }
					  
				  }
				  
			  }
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("xmlToExcelP(): ", e);
		  }
	}
	
	
	/*
	public void generarExcelNotaCredito(Connection con, String esquema, HSSFSheet hojaNotaC, ArrayList<Comprobante> listaFacturas, HSSFWorkbook objLibro) {
		 final String[] encabezados = {
				 "Version", "UUID", "Estatus", "Tipo De Comprobante", "Año","Mes","Dia","Fecha Emision","Fecha Timbrado","Pac Certifico","Serie","Folio",
				 "Forma Pago","Metodo Pago","Tipo Cambio","Moneda","Lugar Expedicion","Sub Total","Descuento","Total","Total Impuestos Retenidos","Total Impuestos Trasladados",
				 "RFC Emisor","Nombre Emisor","Regimen Fiscal Emisor","RFC Receptor","Nombre Receptor","No Certificado SAT","Sello CFD","Sello SAT","Cadena Original","Conceptos"
		 };
		  try {
			  
			  int numColE = 1;
			  hojaNotaC.setColumnWidth(0, 10000);
			   
			  
			   Row header = hojaNotaC.createRow(0);
			   header.setHeightInPoints(18);
			   HSSFPalette palette = objLibro.getCustomPalette();
			   
			   HSSFFont fuente = objLibro.createFont();
			   fuente.setFontHeightInPoints((short)10);
			   fuente.setFontName(HSSFFont.FONT_ARIAL);
			   
			   HSSFFont fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(HSSFColor.WHITE.index);

			   HSSFFont fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			   HSSFCellStyle estiloCelda = objLibro.createCellStyle();
			   //estiloCelda.setWrapText(true);
			   estiloCelda.setAlignment(HSSFCellStyle. ALIGN_JUSTIFY);
			   estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			   estiloCelda.setFont(fuente);
			   estiloCelda.setFillForegroundColor(palette.findSimilarColor((byte) 170, (byte) 218, (byte) 252).getIndex());
			   estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda2 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda2.setAlignment(HSSFCellStyle. ALIGN_JUSTIFY);
			   estiloCelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			   estiloCelda2.setFont(fuente);
			   estiloCelda2.setFillForegroundColor(HSSFColor.WHITE.index);
			   estiloCelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			   
			   HSSFCellStyle encabezadoPrincipal = objLibro.createCellStyle();
			   //encabezadoPrincipal.setWrapText(true);
			   encabezadoPrincipal.setAlignment(HSSFCellStyle. ALIGN_CENTER);
			   encabezadoPrincipal.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			   encabezadoPrincipal.setFont(fuenteEncabezado);
			   encabezadoPrincipal.setFillForegroundColor(palette.findSimilarColor((byte) 12, (byte) 57, (byte) 90).getIndex());
			   encabezadoPrincipal.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			   HSSFCellStyle encabezadoDetalle = objLibro.createCellStyle();
			   //encabezadoDetalle.setWrapText(true);
			   encabezadoDetalle.setAlignment(HSSFCellStyle. ALIGN_CENTER);
			   encabezadoDetalle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			   encabezadoDetalle.setFont(fuenteEncabezado);
			   encabezadoDetalle.setFillForegroundColor(palette.findSimilarColor((byte) 6, (byte) 103, (byte) 172).getIndex());
			   encabezadoDetalle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			   HSSFCellStyle estiloCelda3 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda3.setAlignment(HSSFCellStyle. ALIGN_CENTER);
			   estiloCelda3.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(HSSFColor.WHITE.index);
			   estiloCelda3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			   
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML ( Nota de Credito )");
			   monthCell.setCellStyle(encabezadoPrincipal);
			   hojaNotaC.addMergedRegion(CellRangeAddress.valueOf("A1:AF1"));
			  
			   header = hojaNotaC.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle XML");
			    //estiloCelda2.setAlignment(HSSFCellStyle. ALIGN_CENTER);
			   monthCell2.setCellStyle(estiloCelda3);
			   hojaNotaC.addMergedRegion(CellRangeAddress.valueOf("A2:AF2"));
			    
			  header = hojaNotaC.createRow(2);
			  header.setHeightInPoints(18);

			  for (int i = 0; i < encabezados.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(encabezados[i]);
			    monthCell.setCellStyle(encabezadoDetalle);
			  }
			  
			  HSSFCell celda = null;
			  HSSFRow fila = null;

			  Comprobante _comprobante = null;
			  String fechaComprobante = null;
			  String fechaTimbrado = null;
			  
			  int numCelda = 0;
			  
			  // String CATALOGO_SAT = "CATALOGO_SAT";
			  // String DE_DONDE_SALE = "De donde sale ?";
			  
			 //  String CATALOGO_SAT = " ";
			    String DE_DONDE_SALE = "DE_DONDE_SALE";
			  
			// SE LLENAN LOS CATALOGOS
				  MonedaPagoBean monedaPagoBean = new MonedaPagoBean();
				  HashMap<String, MonedaPagoForm> mapaMonedas =  monedaPagoBean.detalleMoneda(con, esquema);
				  MonedaPagoForm monedaPagoForm = null;
				  
				  FormaPagoBean formaPagoBean = new FormaPagoBean();
				  HashMap<String, FormaPagoForm> mapaFormas =  formaPagoBean.detalleFormas(con, esquema);
				  FormaPagoForm formaPagoForm = null;
				  
				  TipoRelacionBean tipoRelacionBean = new TipoRelacionBean();
				  HashMap<String, TipoRelacionForm> mapaTipoRelacion =  tipoRelacionBean.detalleTipoRelacion(con, esquema);
				  TipoRelacionForm tipoRelacionForm = null;
				  
				  
				  CodigoPostalBean codigoPostalBean = new CodigoPostalBean();
				  HashMap<String, CodigoPostalForm> mapaCodigoPostal =  codigoPostalBean.detalleCodigoPostal(con, esquema);
				  CodigoPostalForm codigoPostalForm = null;
				  
				  
				  MetodoPagoBean metodoPagoBean = new MetodoPagoBean();
				  HashMap<String, MetodoPagoForm> mapaMetodoPago =  metodoPagoBean.detalleMetodoPago(con, esquema);
				  MetodoPagoForm metodoPagoForm = null;
			   
				  
				  RegimenFiscalBean regimenFiscalBean = new RegimenFiscalBean();
				  HashMap<String, RegimenFiscalForm> mapaRegimen =  regimenFiscalBean.detalleRegimenFiscal(con, esquema);
				  RegimenFiscalForm regimenFiscalForm = null;
			   
			   
				  UsoCFDIBean usoCFDIBean = new UsoCFDIBean();
				  HashMap<String, UsoCFDIForm> mapaUso =  usoCFDIBean.detalleUsoCFDI(con, esquema);
				  UsoCFDIForm usoCFDIForm = null;
				  
				  ClaveProductoBean claveProductoBean = new ClaveProductoBean();
				  HashMap<String, ClaveProductoForm> mapaProductos =  claveProductoBean.detalleClaveProductos(con, esquema);
				  ClaveProductoForm claveProductoForm = null;
				  
				  
				  
			  ArrayList<ConceptosForm> listaConceptos = null;
			  ConceptosForm conceptosForm = null;
			  int numRow = 3;
			  String fechaXML = null;
			  for (int x = 0; x < listaFacturas.size(); x++) {
				  _comprobante = listaFacturas.get(x);
				
				  numCelda = 0;
				  fila = hojaNotaC.createRow(numRow++);
				   
				  celda = fila.createCell(numCelda++);
				  celda.setCellValue(_comprobante.getVersion());
				  
				  celda = fila.createCell(numCelda++);
				  celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID());
				  
				  celda = fila.createCell(numCelda++);
				  celda.setCellValue(DE_DONDE_SALE);
				  
				  celda = fila.createCell(numCelda++);
				  celda.setCellValue("E-EGRESO");
				  
				  
				  fechaXML = UtilsFechas.formatFechaSat(_comprobante.getFecha());
				  if (fechaXML != null || fechaXML.length() >= 10 ) {
					  celda = fila.createCell(numCelda++);
					  celda.setCellValue(fechaXML.substring(0, 4)); // Año
					  celda = fila.createCell(numCelda++);
					  celda.setCellValue(fechaXML.substring(5, 7)); // mes
					  celda = fila.createCell(numCelda++);
					  celda.setCellValue(fechaXML.substring(8, 10)); // dia
					  celda = fila.createCell(numCelda++);
					  celda.setCellValue(fechaXML); // fecha Emision
					  
				  }else {
					  celda = fila.createCell(numCelda++);
					  celda.setCellValue(""); // Año
					  celda = fila.createCell(numCelda++);
					  celda.setCellValue(""); // mes
					  celda = fila.createCell(numCelda++);
					  celda.setCellValue(""); // dia
					  celda = fila.createCell(numCelda++);
					  celda.setCellValue(""); // fechaEmision
				  }
				  
				  fechaTimbrado = UtilsFechas.formatFechaSat(_comprobante.getComplemento().getTimbreFiscalDigital().getFechaTimbrado());
				  celda = fila.createCell(numCelda++);
				  celda.setCellValue(fechaTimbrado);
				  
				  celda = fila.createCell(numCelda++);
				  celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getRfcProvCertif());
				  
				  celda = fila.createCell(numCelda++);
				  celda.setCellValue(_comprobante.getSerie());
				  
				  celda = fila.createCell(numCelda++);
				  celda.setCellValue(_comprobante.getFolio());
				  
				  formaPagoForm = mapaFormas.get(_comprobante.getFormaPago());
				   if (formaPagoForm == null) {
					   formaPagoForm = new FormaPagoForm();
				   }
				   
				  celda = fila.createCell(numCelda++);
				  celda.setCellValue(_comprobante.getFormaPago() + " - " + formaPagoForm.getDescripcion());
				  
				  metodoPagoForm = mapaMetodoPago.get(_comprobante.getMetodoPago());
				   if (metodoPagoForm == null) {
					   metodoPagoForm = new MetodoPagoForm();
				   }
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getMetodoPago() + " - " + metodoPagoForm.getDescripcion());
				  
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getTipoCambio());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getMoneda());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getLugarExpedicion());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getSubTotal());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getDescuento());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getTotal());
				   
				   double totalImpuestoRetenido = 0;
				   double totalImpuestoTranslado = 0;
				   if (_comprobante.getImpuestos() != null) {
					   totalImpuestoRetenido = _comprobante.getImpuestos().getTotalImpuestosRetenidos();
					   if (totalImpuestoRetenido == -1) {
						   totalImpuestoRetenido = 0;
					   }
					   
					   totalImpuestoTranslado = _comprobante.getImpuestos().getTotalImpuestosTrasladados();
					   if (totalImpuestoTranslado == -1) {
						   totalImpuestoTranslado = 0;
					   }
					   
				   }
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(Utils.noNuloDouble(totalImpuestoRetenido));
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(Utils.noNuloDouble(totalImpuestoTranslado));
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getEmisor().getRfc());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getEmisor().getNombre());
				   
				   
				   regimenFiscalForm = mapaRegimen.get(_comprobante.getEmisor().getRegimenFiscal());
				   if (regimenFiscalForm == null) {
					   regimenFiscalForm = new RegimenFiscalForm();
				   }
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getEmisor().getRegimenFiscal() + " - " + regimenFiscalForm.getDescripcion());
					  
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getReceptor().getRfc());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getReceptor().getNombre());
				   
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getNoCertificadoSAT());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getSelloCFD());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(_comprobante.getComplemento().getTimbreFiscalDigital().getSelloSAT());
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(DE_DONDE_SALE); // cadena sat
				   
				   // *** 1.00 | 52101510 (TAPETES ANTI FATIGA) | H-1177 | TAPETE CON PLACA DE PATRON DIAMANTE - GROSOR DE 9/16 , 3 X 16, NEGRO/AMARILLO 22 19 3794 2000197 13/01/22 | H87 | PZ | 9460.000000 | 9460.009460.000000 
				   // *** 1.00 | 52101510 (TAPETES ANTI FATIGA) | H-1178 | TAPETE CON PLACA DE PATRON DIAMANTE - GROSOR DE 9/16 , 4 X 8, NEGRO/AMARILLO 19 19 3021 9014014 01/10/19 | H87 | PZ | 6886.000000 | 6886.006886.000000 
				   // *** 1.00 | 52101510 (TAPETES ANTI FATIGA) | H-1179 | TAPETE CON PLACA DE PATRON DIAMANTE - GROSOR DE 9/16 , 4 X 12, NEGRO/AMARILLO 21 19 3794 1011914 16/11/21 | H87 | PZ | 9460.000000 | 9460.009460.000000
				   
				   listaConceptos = getConceptos(_comprobante.getConceptos().getConcepto());
				   StringBuffer sbConceptos = new StringBuffer();
				   for (int y = 0; y < listaConceptos.size(); y++) {
					   conceptosForm = listaConceptos.get(y);
					   
					   claveProductoForm = mapaProductos.get(conceptosForm.getClaveProdServ());
					   
					   sbConceptos.append(" *** ").append(conceptosForm.getCantidad()).append(" | ")
					   			  .append(conceptosForm.getClaveProdServ()).append(" ( ").append(claveProductoForm.getDescripcion()).append(" ) ").append(" | ")
					   			  .append(conceptosForm.getNoIdentificacion()).append(" | ")
					   			  .append(conceptosForm.getDescripcion()).append(" | ")
					   			  .append(conceptosForm.getClaveUnidad()).append(" | ")
					   			  .append(conceptosForm.getUnidad()).append(" | ")
					   			  .append(conceptosForm.getValorUnitario()).append(" | ")
					   			  .append(conceptosForm.getImporte());
					   
				   }
				   
				   celda = fila.createCell(numCelda++);
				   celda.setCellValue(sbConceptos.toString()); // cadena sat
				   sbConceptos.setLength(0);
				   
			  }
			  
		  }
		  catch (Exception e) {
			  Utils.imprimeLog("xmlToExcelP(): ", e);
		  }
	}
	
	*/
	
	
/*
	public void generarExcel(String rutaExcel) {
		try {
			
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Hoja1");
			Row row = null;
			Cell celda = null;
			for (int x = 0; x < 200000; x++) {
		        row = sheet.createRow(x);
		        for (int y = 0; y <= 50; y++) {
		        	celda = row.createCell(y); 
		        	celda.setCellValue("BURGOS ");
		        	celda = null;
		        }
	        	 if (x % 1000 == 0) {
	                 ((SXSSFSheet) sheet).flushRows(1000);
	             }

		        row = null;
		    }

			try (FileOutputStream outputStream = new FileOutputStream(rutaExcel)) {
		        workbook.write(outputStream);
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            workbook.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
*/
	
	public void generarExcel(SXSSFWorkbook workbook, SXSSFSheet sheet) {
        
        try {
        	Row row = null;
        	Cell cell = null;
            for (int rownum = 0; rownum < 200000; rownum++) {
                row = sheet.createRow(rownum);
                for (int y = 0; y < 50; y++) {
                	 cell = row.createCell(y);
                     cell.setCellValue("Burgos " + y);
                }
                
                if (rownum % 100 == 0 && rownum != 0) { // Flush every 100 rows (after the first 100)
                    ((SXSSFSheet) sheet).flushRows(100); // Retain the last 100 rows in memory
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	
}
