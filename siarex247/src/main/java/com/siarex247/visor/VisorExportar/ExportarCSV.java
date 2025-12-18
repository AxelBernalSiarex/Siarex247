package com.siarex247.visor.VisorExportar;

import org.apache.log4j.Logger;

import com.csvreader.CsvWriter;
import com.siarex247.utils.Utils;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;

public class ExportarCSV {

	public static final Logger logger = Logger.getLogger("siarex");
	
	public String generarLine(VisorOrdenesForm visorForm, String tipoLinea, String esquemaEmpresa, String labelEmpresa, String labelMultiple){
		StringBuffer sbLine1 = new StringBuffer("|"); 
		try{
			// Tipo de Orden
			
			/*
			if (visorForm.getTipoOrden().length() > 1 && 
					visorForm.getTipoOrden().substring(0, 1).equals("M")){
				
				if ("tmmbcservicios".equalsIgnoreCase(esquemaEmpresa)) {
					sbLine1.append("TBCS2").append("|");
				}else {
					sbLine1.append("TMBCS").append("|");
				}
				
			}else{
				if ("tmmbcservicios".equalsIgnoreCase(esquemaEmpresa)) {
					sbLine1.append("TBCS1").append("|");
				}else {
					sbLine1.append("TMBCA").append("|");	
				}
			}
			*/
			if (visorForm.getTipoOrden().length() > 1 && 
					visorForm.getTipoOrden().substring(0, 1).equals("M")){
					sbLine1.append(labelMultiple).append("|");
			}else{
					sbLine1.append(labelEmpresa).append("|");	
			}
			
			if ("IVA".equalsIgnoreCase(tipoLinea)){
				// IVA
				sbLine1.append("VAT").append("|");
			}else if ("IVA_RET".equalsIgnoreCase(tipoLinea)){
				// Iva Retenido
				sbLine1.append("WVAT").append("|");
			}else if ("ISR_RET".equalsIgnoreCase(tipoLinea)){
				// ISR Retenido
				sbLine1.append("WIRS").append("|");
			}else{
				// Numero de Orden
				sbLine1.append(rellenaOrden(visorForm.getFolioEmpresa())).append("|");
			}
			
			// Serie y Folio
			//sbLine1.append(visorForm.getSerie()).append("|");
			if ("".equals(visorForm.getSerieFolio())) {
				sbLine1.append(Utils.cortarValor(visorForm.getUuid(), 30 )).append("|");	
			}else {
				sbLine1.append(visorForm.getSerieFolio()).append("|");
			}
			
			// Fecha de Factura
			 sbLine1.append(visorForm.getFechaFactura()).append("|");
			
			if ("IVA".equalsIgnoreCase(tipoLinea)){
				// IVA
				sbLine1.append(visorForm.getSubTotal()).append("|");
			}else if ("IVA_RET".equalsIgnoreCase(tipoLinea)){
				// Iva Retenido
				sbLine1.append(visorForm.getSubTotal()).append("|");
			}else if ("ISR_RET".equalsIgnoreCase(tipoLinea)){
				// ISR Retenido
				sbLine1.append(visorForm.getSubTotal()).append("|");
			}else{
				//Monto BD
				sbLine1.append(visorForm.getMonto()).append("|");
			}
			
			// Total
			sbLine1.append(visorForm.getTotal()).append("|");
			
			// Tipo de Moneda
			sbLine1.append(visorForm.getTipoMoneda()).append("|");
			
			if ("IVA".equalsIgnoreCase(tipoLinea)){
				// IVA
				sbLine1.append(visorForm.getIva()).append("|");
			}else if ("IVA_RET".equalsIgnoreCase(tipoLinea)){
				// Iva Retenido
				sbLine1.append("0.00").append("|");
			}else if ("ISR_RET".equalsIgnoreCase(tipoLinea)){
				// ISR Retenido
				sbLine1.append("0.00").append("|");
			}else{
				// Numero de Orden
				sbLine1.append(visorForm.getSubTotal()).append("|");
			}
			
			
			if ("IVA_RET".equalsIgnoreCase(tipoLinea)){
				// IVA 0.00
				if (Utils.noNuloDouble(visorForm.getIvaRet()) == 0){
					sbLine1.append(visorForm.getIvaRet()).append("|");
				}else{
					sbLine1.append("-").append(visorForm.getIvaRet()).append("|");	
				}
				
			}else{
				// Fijo
				sbLine1.append("0.00").append("|");
			}
			
			
			if ("ISR_RET".equalsIgnoreCase(tipoLinea)){
				// IVA
				if (Utils.noNuloDouble(visorForm.getIsrRet()) == 0){
					sbLine1.append(visorForm.getIsrRet()).append("|");
				}else{
					sbLine1.append("-").append(visorForm.getIsrRet()).append("|");	
				}
				
			}else{
				// Fijo
				sbLine1.append("0.00").append("|");
			}
			
			// UUID
			sbLine1.append(visorForm.getUuid()).append("|");
			
			// Sello Digital
			sbLine1.append("").append("|");
			
			// Concepto de Servicio
			sbLine1.append(Utils.cortarValor(visorForm.getConServicio(), 30)).append("|");
			
			// Datos Bancarios
			
			if ("USA".equalsIgnoreCase(visorForm.getTipoProveedor())){
				if ("WIR".equalsIgnoreCase(visorForm.getPagoDolares())) {
					sbLine1.append("BAN").append("|");	
					sbLine1.append("CHUS").append("|");
				}else {	
					sbLine1.append("BOM").append("|");	
					sbLine1.append("CKBC").append("|");
				}
				sbLine1.append(visorForm.getPagoDolares()).append("|");
			}else {
				sbLine1.append("BAN").append("|");	
				if ("USD".equalsIgnoreCase(visorForm.getTipoMoneda())){ // si es pago en dolar
					sbLine1.append("CHUS").append("|");
					sbLine1.append(visorForm.getPagoDolares()).append("|");
				}else {
					sbLine1.append("CHMX").append("|"); // el pago es en pesos
					sbLine1.append(visorForm.getPagoDolares()).append("|");
				}
			}
			
			if (visorForm.getTipoOrden().length() > 1 && 
					visorForm.getTipoOrden().substring(0, 1).equals("M")){
				sbLine1.append("GS").append("|");
			}else{
				sbLine1.append("FA").append("|");
			}
			
			// Fijo
			sbLine1.append("BC").append("|");
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return sbLine1.toString();
	}
	
	
	
	
	
	
	public void agregaRegistro(ExportarCVSForm exportForm, CsvWriter csvOutput){
		try{
			
			csvOutput.write(exportForm.getTipoOrden());
            csvOutput.write(exportForm.getNumeroOrden());
        	csvOutput.write(exportForm.getSerieFolio());
        	csvOutput.write(exportForm.getFechaFactura());
        	csvOutput.write(exportForm.getMonto());
        	csvOutput.write(exportForm.getTotal());
        	csvOutput.write(exportForm.getTipoMoneda());
        	csvOutput.write(exportForm.getMontoImpuesto());
        	csvOutput.write(exportForm.getMontoIvaRet());
        	csvOutput.write(exportForm.getMontoISRRet());
        	csvOutput.write(exportForm.getUuid());
        	csvOutput.write(exportForm.getSelloDigital());
        	csvOutput.write(exportForm.getConceptoServicio());
        	csvOutput.write(exportForm.getDatosBancarios());
        	csvOutput.write(exportForm.getTipoFactura());
        	csvOutput.write(exportForm.getFormaPago());
        	csvOutput.write(exportForm.getIdenOrden());
        	csvOutput.write(exportForm.getFijoBC());
        	
            csvOutput.endRecord();
            
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
	
	private String rellenaOrden(long folioEmpresa){
		StringBuffer ordenRellena = new StringBuffer();
		try{
			for (int y = String.valueOf(folioEmpresa).length(); y < 10; y++){
				ordenRellena.append("0");
			}
			ordenRellena.append(folioEmpresa);
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return ordenRellena.toString();
	}
	
}
