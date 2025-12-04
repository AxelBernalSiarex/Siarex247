package com.siarex247.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class LeerPDFOrden {

	/*
	 public static void lecturaPDF(String rutaArchivos, String nombreArchivoProcesar) {
		 PDDocument pd = null;
		 PDPage page = null;
		 StringBuffer sbContenido = new StringBuffer();
		 HashMap<String, String> mapaValores = new HashMap<>();
		 try {
			  ArrayList<PDPage> listaPaginas = new ArrayList<PDPage>();
			  String ruta=new String();
	          ruta = rutaArchivos + nombreArchivoProcesar; 
	          File filePDF = new File(ruta);
              pd = PDDocument.load(filePDF);
              Splitter splitter = new Splitter();
              List<PDDocument> splittedDocuments = splitter.split(pd);
              PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
              
              String noOrden = "";
              String vendorID = "";
              String total = "";
              String ship_to = "";
              	 mapaValores.put("ORDEN_COMPRA", "");
	 			 mapaValores.put("VENDOR_ID", "");
	 			 mapaValores.put("TOTAL", "");
	 			 mapaValores.put("MONEDA", "");
	 			 mapaValores.put("SHIPTO", "");
	 			 mapaValores.put("DESCRIPCION", "");
	 			 
              for (int x = 0; x < splittedDocuments.size(); x++) {
            	  page = pd.getPage(x);
            	  sbContenido.append(pdfTextStripper.getText(splittedDocuments.get(x)));
            	  //System.err.println("TextLine===>"+textLine);
            	  int posIni = sbContenido.indexOf("1 of  1"); 
            	  if (posIni > -1) { // se busca la orden de compra
            		  int posIniCad = posIni - 70; 
            		  int posFinCada = posIni - 30;
            		  noOrden = sbContenido.substring(posIniCad, posFinCada);
            		 // System.err.println("textLine........"+noOrden.trim());
            		  mapaValores.put("ORDEN_COMPRA", noOrden);
            	  }
            	  posIni = sbContenido.indexOf("FORMA  DE EMBARQUE"); 
            	  if (posIni > -1) { // se busca la orden de compra
            		  int posIniCad = posIni + 80; 
            		  int posFinCada = posIniCad + 10;
            		  vendorID = sbContenido.substring(posIniCad, posFinCada);
            		  mapaValores.put("VENDOR_ID", vendorID.trim());
            	  }
            	  
            	  posIni = sbContenido.indexOf("FORMA  DE EMBARQUE"); 
            	  if (posIni > -1) { // se busca la orden de compra
            		  int posIniCad = posIni + 80; 
            		  int posFinCada = posIniCad + 10;
            		  vendorID = sbContenido.substring(posIniCad, posFinCada);
            		  mapaValores.put("VENDOR_ID", vendorID.trim());
            	  }
            	  
            	  posIni = sbContenido.indexOf("TOTAL"); 
            	  if (posIni > -1) { // se busca la orden de compra
            		  int posIniCad = posIni + 15; 
            		  int posFinCada = posIniCad + 20;
            		  total = sbContenido.substring(posIniCad, posFinCada).trim();
            		  String totalSinMoneda = total.substring(0, total.length() - 3);
            		  String moneda = total.substring(total.length() - 3);
            		  mapaValores.put("TOTAL", totalSinMoneda.trim());
            		  mapaValores.put("MONEDA", moneda.trim());
            	  }
            	  
            	  posIni = sbContenido.indexOf("COMPRADOR"); 
            	  if (posIni > -1) { // se busca la orden de compra
            		  int posIniCad = posIni + 190; 
            		  int posFinCada = posIniCad + 70;
            		  ship_to = sbContenido.substring(posIniCad, posFinCada);
            		  mapaValores.put("SHIPTO", ship_to.trim());
            	  }
            	  
              }
              
             // System.err.println(sbContenido.toString());
		 }catch(Exception e) {
			 e.printStackTrace();
		 }finally {
			 try {
				 if (pd != null) {
					 pd.close(); 
	             }	 
			 }catch(Exception e) {
				 pd = null;
			 }
		 }
	 }
	 
	 */
	
	
	 public static void lecturaHTML(String rutaArchivos, String nombreArchivoProcesar) {
		 PDDocument pd = null;
		 PDPage page = null;
		 StringBuffer sbContenido = new StringBuffer();
		 HashMap<String, String> mapaValores = new HashMap<>();
		 try {
			 String nombreArchivo =  rutaArchivos + nombreArchivoProcesar;
			 System.err.println("nombreArchivo===>"+nombreArchivo);
 
			 ArrayList<String> listaHTML = UtilsFile.leeArchivoTXT(nombreArchivo);
	 	 
			 for (int x = 0; x < listaHTML.size(); x++) {
				 System.err.println("Line===>"+listaHTML.get(x));
			 }
			 
              String noOrden = "";
              String vendorID = "";
              String total = "";
              String ship_to = "";
              	 mapaValores.put("ORDEN_COMPRA", "");
	 			 mapaValores.put("VENDOR_ID", "");
	 			 mapaValores.put("TOTAL", "");
	 			 mapaValores.put("MONEDA", "");
	 			 mapaValores.put("SHIPTO", "");
	 			 mapaValores.put("DESCRIPCION", "");

	 			
              
            System.err.println(mapaValores);
		 }catch(Exception e) {
			 e.printStackTrace();
		 }finally {
			 try {
				 if (pd != null) {
					 pd.close(); 
	             }	 
			 }catch(Exception e) {
				 pd = null;
			 }
		 }
	 }
	 
	
	public static void main(String[] args) {
		try {
			String rutaArchivos = "C:\\testPDF\\";
			String nombreArchivoProcesar = "9100301754.htm";
			
			lecturaHTML(rutaArchivos, nombreArchivoProcesar);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
