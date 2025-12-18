package com.siarex247.validaciones;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.siarex247.pdf.PDFLayoutTextStripper;
import com.siarex247.utils.Utils;

public class ValidacionesPdf {
	public static final Logger logger = Logger.getLogger("siarex247");
	

	
    public int lecturaPDF(String rutaArchivos, String UUID){
    	String cadenaUUID [] = null;
    	//String UUIDCompleto [] = {UUID};
    	int folioFiscal = -1;
    	try{
    		folioFiscal = buscaUUID(UUID, rutaArchivos);
    		if (folioFiscal == -1){
    			cadenaUUID = UUID.split("-");
    			for (int x = 0; x < cadenaUUID.length; x++) {
    				UUID = cadenaUUID[x];
    				folioFiscal = buscaUUID(UUID, rutaArchivos);
    				if (folioFiscal >= 0) {
    					break;
    				}
    			}
    			 
    		}
    	}catch(Exception e){
    		Utils.imprimeLog("", e);
    	}
    	       
        return folioFiscal;
    }

    
    public int buscaUUID(String UUID , String rutaPDF) {
		 PDDocument pd = null;
		 StringBuffer sbContenido = new StringBuffer();
		 int folioFiscal=0;
		 try {
			  File filePDF = new File(rutaPDF);
             pd = Loader.loadPDF(filePDF);
             Splitter splitter = new Splitter();
             List<PDDocument> splittedDocuments = splitter.split(pd);
             PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
             
             
             for (int x = 0; x < splittedDocuments.size(); x++) {
           	  sbContenido.append(pdfTextStripper.getText(splittedDocuments.get(x)));
           	  folioFiscal = sbContenido.indexOf(UUID);
	           	if (folioFiscal == -1) {
	            	folioFiscal = sbContenido.indexOf(UUID.toUpperCase());
	            }else {
	            	break;
	            }
             }
		 }catch(Exception e) {
			 Utils.imprimeLog("", e);
		 }finally {
			 try {
				 if (pd != null) {
					 pd.close(); 
	             }	 
			 }catch(Exception e) {
				 pd = null;
			 }
		 }
		 return folioFiscal;
	 }
    
}