package com.siarex247.utils;

import com.itextpdf.xmltopdf.CreaPDF;

public class UtilsPDF {

	

	public static void main(String[] args) {
		try {
			String pathXML = "C:\\Tomcat9\\REPOSITORIO_DOCUMENTOS\\SIAREX\\REPOSITORIOS\\toyota\\BOVEDA\\008474CB-2AB3-4C81-8F98-F173423FBD9C.xml";
			String pathPDF =  "C:/Tomcat9/REPOSITORIO_DOCUMENTOS/SIAREX/REPOSITORIOS/toyota/EXPORTAR/008474CB-2AB3-4C81-8F98-F173423FBD9C.pdf";
			String rutaLogo = "C:/Tomcat9/REPOSITORIO_DOCUMENTOS/SIAREX/REPOSITORIOS/toyota/BOVEDA/toyotaASASS.png";  
			new CreaPDF().GenerarByXML(pathXML, pathPDF, rutaLogo);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
