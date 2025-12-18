package com.itextpdf.xmltopdf;

public class TestPDF {

	
	
	public static void main(String[] args) {
		try {
		    String rutaPDF = "C:\\Users\\jose_\\Desktop\\VINCULAR_BOVEDA\\TEST_PDF\\0a1ec152-fb35-4fb5-bbc0-1275be345b1e.pdf";
			String rutaLogo = "C:\\Tomcat9\\REPOSITORIO_DOCUMENTOS\\SAI247\\REPOSITORIOS\\NAPS\\LOGOS_EMPRESAS\\KIA.png";
			String rutaXML = "C:\\\\Users\\\\jose_\\\\Desktop\\\\VINCULAR_BOVEDA\\\\TEST_PDF\\\\0a1ec152-fb35-4fb5-bbc0-1275be345b1e.xml";
			new CreaPDF().GenerarByXML(rutaXML, rutaPDF, rutaLogo);			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
