package com.siarex247.cumplimientoFiscal.ListaNegra;

import java.io.File;
import java.net.MalformedURLException;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.xmltopdf.Utilidades;

public class ListaNegraPDF {
private static String RUTA_LOGO;
private Document DOCUMENTO;

	public void creaPDF(String rutaPDF) {
		try {
			 
			//String rutaPDF = "C:\\opt\\test.pdf";
			RUTA_LOGO = "C:\\opt\\logoRojo.png";
			PdfWriter writer = new PdfWriter(rutaPDF);
			PdfDocument DOCUMENTO_PDF = new PdfDocument(writer);
			DOCUMENTO_PDF.addEventHandler(PdfDocumentEvent.END_PAGE, new MyEventHandlerLista());
			DOCUMENTO = new Document(DOCUMENTO_PDF, new PageSize(612, 792));
            DOCUMENTO.setMargins(Utilidades.cmToFloat(.8f), Utilidades.cmToFloat(0.7f),
                            Utilidades.cmToFloat(2f), Utilidades.cmToFloat(.7F));
            
            FormatoNormal();
            
            DOCUMENTO.close();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void FormatoNormal() {
        AgregarEncabezado();
    }
	
	
	private void AgregarEncabezado() {
		float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 5, 15f });
        Table tablaDatos = new Table(anchoColumnas);
        tablaDatos.addCell(AgregarLogo());
        Paragraph prc = new Paragraph();
        prc.setTextAlignment(TextAlignment.RIGHT);
        prc.add(new Text("Reporte de contribuyente publicado | 30 abr 2025 | Confidencial" + "\n").setFontSize(8).setBold());
        
        Cell celda = new Cell();
        celda.setBorder(Border.NO_BORDER);
        celda.setPaddingLeft(12);
        celda.setPaddingRight(12);
        celda.add(prc);
        tablaDatos.addCell(celda);
        
        tablaDatos.startNewRow();
        DOCUMENTO.add(tablaDatos);
	}

	
	private Cell AgregarLogo() {
        Cell celda;
        try {
                // Path path = Paths.get(RUTA_LOGO);
                // Files.exists(path)
                File file = new File(RUTA_LOGO);
                System.err.println("Existe archivo==>"+file.exists());
                if (file.exists()) {
                        Image imagen = new Image(ImageDataFactory.create(RUTA_LOGO));
                        imagen.scaleToFit(110, 110);
                        celda = new Cell().add(imagen);
                        celda.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        celda.setBorder(Border.NO_BORDER);
                } else {
                        celda = new Cell().add(new Paragraph());
                        celda.setBorder(Border.NO_BORDER);
                }
        } catch (MalformedURLException e) {
                celda = new Cell().add(new Paragraph());
                celda.setBorder(Border.NO_BORDER);
        }
        return celda;
	}
		
	
	public static void main(String[] args) {
		try {
			new ListaNegraPDF().creaPDF("");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
