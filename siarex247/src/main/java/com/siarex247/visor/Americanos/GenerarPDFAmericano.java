package com.siarex247.visor.Americanos;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.siarex247.utils.Utils;


public class GenerarPDFAmericano {
	public static final Logger logger = Logger.getLogger("siarex");

	
	public static void drawTable(PDPage page, PDPageContentStream contentStream, float tamTabla, float y, float margin,
			String[][] content, float colWidth[], boolean pintaLineaX, boolean pintaLineaY ) throws IOException {
		final int rows = content.length;
		final int cols = content[0].length;
		final float rowHeight = 17f;
		//final float tableWidth = page.findMediaBox().getWidth() - margin - margin;
		final float tableWidth = tamTabla;
		final float tableHeight = rowHeight * rows;
		
		//final float colWidthTabla = tableWidth / (float) cols;
		
		final float cellMargin = 3f;

		// draw the rows
		float nexty = y;
		for (int i = 0; i <= rows; i++) {
			if (i == 0 || i == rows) {
				contentStream.drawLine(margin, nexty, margin + tableWidth, nexty);
				nexty -= rowHeight;
			}else if (pintaLineaY) {
				contentStream.drawLine(margin, nexty, margin + tableWidth, nexty);
				nexty -= rowHeight;
			}else {
				nexty -= rowHeight;
			}
			
			
		}

		// draw the columns
		float nextx = margin;
		for (int i = 0; i <= cols; i++) {
			if (i == 0 || i == cols) {
				contentStream.drawLine(nextx, y, nextx, y - tableHeight);
				//nextx += colWidthTabla;
				  nextx += colWidth[i];	
			}else if (pintaLineaX) {
				contentStream.drawLine(nextx, y, nextx, y - tableHeight);
				//nextx += colWidthTabla;
				  nextx += colWidth[i];
			}else {
				 nextx += colWidth[i];
			}
			  
		}

		// now add the text
		contentStream.setFont(PDType1Font.HELVETICA, 9);
		
		float textx = margin + cellMargin;
		float texty = y - 15;
		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content[i].length; j++) {
				String text = content[i][j];
				contentStream.beginText();
				contentStream.moveTextPositionByAmount(textx, texty);
				contentStream.drawString(text);
				contentStream.endText();
				//textx += colWidthTabla;
				textx += colWidth[j];
			}
			texty -= rowHeight;
			textx = margin + cellMargin;
		}
	}

	
	public static void drawTableContenido(PDPage page, PDPageContentStream contentStream, float tamTabla, float y, float margin,
			ArrayList<String []> detalleTabla, float colWidth[], boolean pintaLineaX, boolean pintaLineaY ) throws IOException {
		
		final int rows = detalleTabla.size();
		final int cols = 5;
		
		final float rowHeight = 17f;
		//final float tableWidth = page.findMediaBox().getWidth() - margin - margin;
		final float tableWidth = tamTabla;
		final float tableHeight = rowHeight * rows;
		
		//final float colWidthTabla = tableWidth / (float) cols;
		
		final float cellMargin = 3f;

		// draw the rows
		float nexty = y;
		for (int i = 0; i <= rows; i++) {
			if (i == 0 || i == rows) {
				contentStream.drawLine(margin, nexty, margin + tableWidth, nexty);
				nexty -= rowHeight;
			}else if (pintaLineaY) {
				contentStream.drawLine(margin, nexty, margin + tableWidth, nexty);
				nexty -= rowHeight;
			}else {
				nexty -= rowHeight;
			}
			
			
		}

		// draw the columns
		float nextx = margin;
		for (int i = 0; i <= cols; i++) {
			if (i == 0 || i == cols) {
				contentStream.drawLine(nextx, y, nextx, y - tableHeight);
				//nextx += colWidthTabla;
				  nextx += colWidth[i];	
			}else if (pintaLineaX) {
				contentStream.drawLine(nextx, y, nextx, y - tableHeight);
				//nextx += colWidthTabla;
				  nextx += colWidth[i];
			}else {
				 nextx += colWidth[i];
			}
			  
		}

		// now add the text
		contentStream.setFont(PDType1Font.HELVETICA, 9);
		
		float textx = margin + cellMargin;
		float texty = y - 15;
		String contentValor [] = null; 
		for (int i = 0; i < detalleTabla.size(); i++) {
			contentValor = detalleTabla.get(i);
			for (int j = 0; j < contentValor.length; j++) {
				String text = contentValor[j];
				contentStream.beginText();
				contentStream.moveTextPositionByAmount(textx, texty);
				contentStream.drawString(text);
				contentStream.endText();
				//textx += colWidthTabla;
				textx += colWidth[j];
			}
			texty -= rowHeight;
			textx = margin + cellMargin;
		}
	}

	
	private void generaEncabezadoLogo(PDDocument document, PDPageContentStream contentStream, String[] datosProveedorEmisor, boolean tieneLogo) {
		try {
			if (tieneLogo) {
				PDImageXObject pdImage = PDImageXObject.createFromFile(datosProveedorEmisor[0], document);
				contentStream.drawImage(pdImage, 50, 700, 80, 50);// 1ST number is horizontal posn from left	
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	private void generaEncabezadoDatos(PDPage page, PDPageContentStream contentStream, String[][] datosFechaFactura) {
		try {
			
			float colWidth[] = {100, 100, 0};
			drawTable(page, contentStream, 200, 750, 350, datosFechaFactura, colWidth, true, true);
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	
	private void generaEncabezadoEmisor(PDPageContentStream contentStream, String[] datosProveedorEmisor) {
		try {
			PDFont font1 = PDType1Font.HELVETICA;// TIMES_ROMAN;
			contentStream.beginText();
			contentStream.setFont(font1, 9);
			contentStream.newLineAtOffset(0, 0);
			contentStream.setCharacterSpacing(0);
			contentStream.setWordSpacing(0);
			contentStream.setLeading(0);
			contentStream.setLeading(14.5f);// this was key for some reason
			contentStream.moveTextPositionByAmount(50, 690);// sets the start point of text

			
			contentStream.drawString(datosProveedorEmisor[1]);
			contentStream.newLine();
			contentStream.drawString(datosProveedorEmisor[2] + ","+datosProveedorEmisor[3] + " " + datosProveedorEmisor[4]);
			contentStream.newLine();
			contentStream.drawString("Tel. "+datosProveedorEmisor[5]);
			contentStream.newLine();
			contentStream.newLine();
			contentStream.newLine();
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	
	private void generaEncabezadoReceptor(PDPageContentStream contentStream, String[] datosEmpresaReceptor) {
		try {
			
			contentStream.drawString(datosEmpresaReceptor[0]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[1]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[2]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[3]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[4]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[5]);
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	private void generaEncabezadoTabla(PDPage page, PDPageContentStream contentStream, String[][] datosEncabezadoTabla) {
		try {
			
			float colWidth2[] = {60, 125, 315,0};
			drawTable(page, contentStream, 500, 530, 50, datosEncabezadoTabla, colWidth2, true, true);
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	
	private void imprimeContenidoTabla(PDPage page, PDPageContentStream contentStream, ArrayList<String []> datosTablaContenido) {
		try {
			//String[][] content3 = generaForm.getDatosTablaContenido();
			float colWidth3[] = {60, 125, 180, 67, 68,0};
			drawTableContenido(page, contentStream, 500, 496, 50, datosTablaContenido, colWidth3, true, false);
						
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	
	private void imprimeSubTotalDatos(PDPage page, PDPageContentStream contentStream, String[][] datosSubTotalTabla) {
		try {
			float colWidth4[] = {200, 100, 0};
			drawTable(page, contentStream, 300, 173, 250, datosSubTotalTabla, colWidth4, true, true);
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	
	public boolean generarFactura(GeneraDatosFacturaForm generaForm, String rutaArchivo, boolean tieneLogo) {
		PDDocument document = null;
		boolean isGenero = false;
		try {
			document = new PDDocument();
			
			//PDFont font1 = PDType1Font.HELVETICA;

			ArrayList<String[]> datosContenido = generaForm.getDatosTablaContenido();
			ArrayList<String[]> rowContenido = new ArrayList<>();
			boolean bandPrimero = true;
			int contReng = 1;
			PDPage page = null;
			PDPageContentStream contentStream = null;
			String [] paginaFactura = null;
			String [][] datosFechaFactura = null;
			
			
			String paginado = null;
			int numPagina = 1;
			String leyPaginado = new String("                    #                    ");
			
			for (int x = 0; x < datosContenido.size(); x++) {
				rowContenido.add(datosContenido.get(x));
				if (bandPrimero) {
					datosFechaFactura = generaForm.getDatosFechaFactura();
					paginaFactura = datosFechaFactura[2]; 
					paginado = leyPaginado.replace("#", String.valueOf(numPagina));
					paginaFactura[1] = paginado;
					
					datosFechaFactura[2] = paginaFactura;
					
					page = new PDPage();
					document.addPage(page);
					contentStream = new PDPageContentStream(document, page);
					generaEncabezadoLogo(document, contentStream, generaForm.getDatosProveedorEmisor(), tieneLogo);
					generaEncabezadoDatos(page, contentStream, datosFechaFactura);
					generaEncabezadoEmisor(contentStream, generaForm.getDatosProveedorEmisor());
					generaEncabezadoReceptor(contentStream, generaForm.getDatosEmpresaReceptor());
					contentStream.endText();
					
					generaEncabezadoTabla(page, contentStream, generaForm.getDatosTabla1Encabezado());
					bandPrimero = false;
					paginaFactura = null;
					datosFechaFactura = null;
				}
				if (contReng == 18) {
					imprimeContenidoTabla(page, contentStream, rowContenido); // este se va a controlar		
					bandPrimero = true;
					contReng = 1;
					rowContenido.clear();
					contentStream.close();
					numPagina++;
				}else {
					contReng++;	
				}
				
			}
			
			if (contReng <= 17) {
				for (int x = contReng; x <= 18; x++ ) {
					String [] renglones  = {"", "", "", "", ""};
					rowContenido.add(renglones);
				}
				imprimeContenidoTabla(page, contentStream, rowContenido); // este se va a controlar	
			}
			
			imprimeSubTotalDatos(page, contentStream, generaForm.getDatosTablaSubtotal());
			
			if (contentStream != null) {
				contentStream.close();
			}
			
			
			
			
			document.save(rutaArchivo);
			isGenero = true;
			
			/*
			if (tieneLogo) {
				PDImageXObject pdImage = PDImageXObject.createFromFile(datosProveedorEmisor[0], document);
				contentStream.drawImage(pdImage, 50, 700, 80, 50);// 1ST number is horizontal posn from left	
			}
			*/
			
			/*
			String[][] datosFechaFactura =  generaForm.getDatosFechaFactura();
			
					
			float colWidth[] = {100, 100, 0};
					
			drawTable(page, contentStream, 200, 750, 350, datosFechaFactura, colWidth, true, true);
			*/
			
			/*
			contentStream.beginText();
			contentStream.setFont(font1, 9);
			contentStream.newLineAtOffset(0, 0);
			contentStream.setCharacterSpacing(0);
			contentStream.setWordSpacing(0);
			contentStream.setLeading(0);
			contentStream.setLeading(14.5f);
			contentStream.moveTextPositionByAmount(50, 690);

			
			contentStream.drawString(datosProveedorEmisor[1]);
			contentStream.newLine();
			contentStream.drawString(datosProveedorEmisor[2] + ","+datosProveedorEmisor[3] + " " + datosProveedorEmisor[4]);
			contentStream.newLine();
			contentStream.drawString("Tel. "+datosProveedorEmisor[5]);
			contentStream.newLine();
			contentStream.newLine();
			contentStream.newLine();
			*/
			
			
			/*
			String[] datosEmpresaReceptor = generaForm.getDatosEmpresaReceptor();
			
			contentStream.drawString(datosEmpresaReceptor[0]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[1]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[2]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[3]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[4]);
			contentStream.newLine();
			contentStream.drawString(datosEmpresaReceptor[5]);
			*/
			
			//contentStream.endText();
			

			//String[][] content2 = { { "Order No.", "Customer ID", "Purchase Order No." }, { "MULTIPLE", "852612", "REFER TO EACH ITEM" }};
			
			/*
			String[][] content2 = generaForm.getDatosTabla1Encabezado();
			float colWidth2[] = {60, 125, 315,0};
			drawTable(page, contentStream, 500, 530, 50, content2, colWidth2, true, true);
			*/
			

			//ae para cilindro rompe inercia
			/*
			String[][] content3 = { { "Ordered.", "TMMBC PO# - CKD INVOICE", "Description", "Unit Price", "Ext. Price" },
									{ "      1      ", "          00012966618          ", "Fabricacion e instalacion de bae para cilindro", "       10,400", "       10,400" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" },
									{ "", "", "", "", "" }
								  };
			*/
			
			/*
			String[][] content3 = generaForm.getDatosTablaContenido();
			
			float colWidth3[] = {60, 125, 180, 67, 68,0};
			drawTable(page, contentStream, 500, 496, 50, content3, colWidth3, true, false);
			*/


			//String[][] content4 = { { "Subtotal", "                     $8,666.67"}, { "Misc", ""}, { "Freight", ""}, { "Tax", ""},{ "Total", "                     $8,666.67"}};
			
			/*
			String[][] content4 = generaForm.getDatosTablaSubtotal();
			float colWidth4[] = {200, 100, 0};
			
			drawTable(page, contentStream, 300, 173, 250, content4, colWidth4, true, true);
			*/
			
			

			//document.save("C:/EJEMPLO_PDF/factura.pdf");
			/*
			contentStream.close();
			document.save(rutaArchivo);
			isGenero = true;
			*/
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (document != null) {
					document.close();		
				}
				document = null;
			}catch(Exception e) {
				document = null;
			}
		}
		return isGenero;
	}

	public static void main(String[] args) {
		GenerarPDFAmericano g = new GenerarPDFAmericano();
		//g.generarFactura();
	}
}
