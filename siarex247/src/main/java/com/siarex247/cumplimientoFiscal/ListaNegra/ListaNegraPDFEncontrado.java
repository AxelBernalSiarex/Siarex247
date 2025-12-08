package com.siarex247.cumplimientoFiscal.ListaNegra;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class ListaNegraPDFEncontrado {

	private static String fontClasspath = "/fonts/Arial.ttf";
	
    public static void main(String[] args) throws Exception {
    	Path salida = Paths.get("C:\\testPDF\\"+System.currentTimeMillis()+".pdf");
    	HashMap<String, String> mapaLista = new HashMap<String, String>();
    	
    	mapaLista.put("CANCELADOS POR INSOLVENCIA", "NO ENCONTRADO");
    	mapaLista.put("CANCELADOS", "NO ENCONTRADO");
    	mapaLista.put("CANCELADOS POR INCOSTEABILIDAD", "NO ENCONTRADO");
    	mapaLista.put("Artículo 74", "NO ENCONTRADO");
    	mapaLista.put("CONDONADOS", "NO ENCONTRADO");
    	mapaLista.put("RETORNO INVERSIONES", "ENCONTRADO");
    	mapaLista.put("Definitivo", "NO ENCONTRADO");
    	mapaLista.put("Desvirtuado", "NO ENCONTRADO");
    	mapaLista.put("Presunto", "NO ENCONTRADO");
    	mapaLista.put("Sentencia Favorable", "NO ENCONTRADO");
    	mapaLista.put("CONDONADOS0715", "NO ENCONTRADO");
    	mapaLista.put("CANCELADOS0715", "ENCONTRADO");
    	mapaLista.put("EXIGIBLES", "NO ENCONTRADO");
    	mapaLista.put("FIRMES", "NO ENCONTRADO");
    	mapaLista.put("NO LOCALIZADOS", "NO ENCONTRADO");
    	mapaLista.put("SENTENCIAS", "NO ENCONTRADO");
    	
    	
    	
    	String imagePathOrClasspath = "/opt/tomcat11/logos/logoRojo.png";
        String razonSocial = "JOSE GUADALUPE BURGOS MARTINEZ KALIMAN DE ACERO INOXIDABLE ERES LO MEJOR QUE ME HA PASADO EN ESTE MUNDO TRAIDOR";
        String rfc = "BUMG8008188A4";
        String tipoPersona = "PERSONA MORAL";
        String nombreUsuario = "ADMINISTRADOR SIAREX";
        
        new ListaNegraPDFEncontrado().crearPDF(mapaLista, salida, imagePathOrClasspath, razonSocial, rfc, tipoPersona,  nombreUsuario, 1);
        System.out.println("✅ PDF generado en: " + salida.toAbsolutePath());
    }

    public void crearPDF(HashMap<String, String> mapaLista, Path outputPdf,
                         String imagePathOrClasspath,
                         String razonSocial,
                         String rfc,
                         String tipoPersona,
                         String nombreUsuario,
                         int totalPages) throws IOException {

        try (PdfWriter writer = new PdfWriter(outputPdf.toFile());
             PdfDocument pdf = new PdfDocument(writer);
             Document doc = new Document(pdf, PageSize.A4)) {
        	
        	String fechaActual = UtilsFechas.obtenerFechaMesCompleto();
            // 👇 Fecha actual formateada automáticamente
            String fechaEmision = UtilsFechas.obtenerFechaFormateada();

            
        	String headerText = "Reporte de contribuyente publicado | "+fechaActual+" | Confidencial";
        	
            PdfFont arial = loadFontOrFallback(fontClasspath);
            ImageData logoData = loadLogoDataFlexible(imagePathOrClasspath);

            float marginLeft = 36f, marginRight = 36f, marginTop = 36f;
            float headerHeight = 50f;

            pdf.addEventHandler(PdfDocumentEvent.START_PAGE,
                    new HeaderHandler(logoData, headerText, arial, 8f,
                            marginLeft, marginRight, marginTop, headerHeight));

            doc.setMargins(marginTop + headerHeight + 8f, marginRight, 48f, marginLeft);

            // 🎨 Color corporativo (#2C6FC0)
            Color azul = new DeviceRgb(44, 111, 192);
            Color rojo = new DeviceRgb(237, 28, 36);
            Color negro = new DeviceRgb(0, 0, 0);

            // ======= TABLA PRINCIPAL 70% / 30% =======
            float[] colPercents = {70f, 30f};
            Table container = new Table(UnitValue.createPercentArray(colPercents))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setBorder(Border.NO_BORDER)
                    .setMarginTop(20)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            // ======= IZQUIERDA =======
            Table left = new Table(UnitValue.createPercentArray(new float[]{100f}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setBorder(Border.NO_BORDER)
                    .setMarginTop(5);

            left.addCell(new Cell().add(new Paragraph("Reporte individual")
                    .setFont(arial).setFontSize(15).setFontColor(azul)
                    .setTextAlignment(TextAlignment.LEFT))
                    .setBorder(Border.NO_BORDER).setPaddingBottom(5));

            left.addCell(new Cell().add(new Paragraph("Contribuyente Publicado en Listas Negras")
                    .setFont(arial).setFontSize(17).setFontColor(azul).setBold()
                    .setTextAlignment(TextAlignment.LEFT))
                    .setBorder(Border.NO_BORDER).setPaddingBottom(6));

            left.addCell(new Cell().add(new Paragraph(nombreUsuario + " | " + fechaEmision + " |")
                    .setFont(arial).setFontSize(8).setFontColor(negro)
                    .setTextAlignment(TextAlignment.LEFT))
                    .setBorder(Border.NO_BORDER));

            // ======= TABLA DETALLES (3x2) =======
            
            Div leftStack = new Div();
            leftStack.add(left);
//            leftStack.add(leftDetails);

            // ======= DERECHA =======
/*            
            Table right = new Table(UnitValue.createPercentArray(new float[]{100f}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setMarginTop(5);

            right.addCell(new Cell().add(new Paragraph("Último estatus")
                    .setFont(arial).setFontSize(15).setFontColor(negro)
                    .setTextAlignment(TextAlignment.RIGHT))
                    .setBorder(Border.NO_BORDER).setPaddingBottom(5));

            right.addCell(new Cell().add(new Paragraph("Publicado")
            		.setBold()
                    .setFont(arial).setFontSize(15).setFontColor(rojo)
                    .setTextAlignment(TextAlignment.RIGHT))
                    .setBorder(Border.NO_BORDER).setPaddingBottom(5));

            right.addCell(new Cell().add(new Paragraph("Nivel de riesgo general")
                    .setFont(arial).setFontSize(14).setFontColor(negro)
                    .setTextAlignment(TextAlignment.RIGHT))
                    .setBorder(Border.NO_BORDER).setPaddingBottom(5));

            right.addCell(new Cell().add(new Paragraph("Con Riesgo")
                    .setFont(arial).setFontSize(18).setBold().setFontColor(rojo).setBold()
                    .setTextAlignment(TextAlignment.RIGHT))
                    .setBorder(Border.NO_BORDER));
*/
         // ======= DERECHA =======
            Table right = new Table(UnitValue.createPercentArray(new float[]{100f}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setMarginTop(5);

            right.addCell(new Cell().add(new Paragraph("Último estatus")
                    .setFont(arial)
                    .setFontSize(15)
                    .setFontColor(negro)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMultipliedLeading(0.9f))   // <-- interlineado más compacto
                    .setBorder(Border.NO_BORDER)
                    .setPadding(2));               // <-- menos padding

            right.addCell(new Cell().add(new Paragraph("Publicado")
                    .setBold()
                    .setFont(arial)
                    .setFontSize(15)
                    .setFontColor(rojo)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMultipliedLeading(0.9f))
                    .setBorder(Border.NO_BORDER)
                    .setPadding(2));

            right.addCell(new Cell().add(new Paragraph("Nivel de riesgo general")
                    .setFont(arial)
                    .setFontSize(14)
                    .setFontColor(negro)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMultipliedLeading(0.9f))
                    .setBorder(Border.NO_BORDER)
                    .setPadding(2));

            right.addCell(new Cell().add(new Paragraph("Con riesgo")
                    .setFont(arial)
                    .setFontSize(18)
                    .setBold()
                    .setFontColor(rojo)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMultipliedLeading(0.9f))
                    .setBorder(Border.NO_BORDER)
                    .setPadding(2));
            

            container.addCell(new Cell().add(leftStack).setBorder(Border.NO_BORDER));
            container.addCell(new Cell().add(right).setBorder(Border.NO_BORDER));
            doc.add(container);
            
            
            Table leftDetails = new Table(UnitValue.createPercentArray(new float[]{15f, 85f}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setBorder(Border.NO_BORDER)
                    .setMarginTop(8);

            String rfcTipo = rfc + " | " + tipoPersona;
            leftDetails.addCell(new Cell()
                    .add(new Paragraph("RFC").setFont(arial).setFontSize(10))
                    .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            leftDetails.addCell(new Cell()
                    .add(new Paragraph(rfcTipo).setFont(arial).setFontSize(10))
                    .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            leftDetails.addCell(new Cell()
                    .add(new Paragraph("Contribuyente").setFont(arial).setFontSize(10))
                    .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            leftDetails.addCell(new Cell()
                    .add(new Paragraph(razonSocial).setFont(arial).setFontSize(10))
                    .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));

            doc.add(leftDetails);
            
            Table supuestos = new Table(UnitValue.createPercentArray(new float[]{25f, 25f, 25f, 25f}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                    .setMarginTop(8);
            supuestos.addCell(new Cell()
                    .add(new Paragraph("Supuesto").setFont(arial).setFontSize(10)).setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            supuestos.addCell(new Cell()
                    .add(new Paragraph("Estatus").setFont(arial).setFontSize(10)).setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            supuestos.addCell(new Cell()
                    .add(new Paragraph("Supuesto").setFont(arial).setFontSize(10)).setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            supuestos.addCell(new Cell()
                    .add(new Paragraph("Estatus").setFont(arial).setFontSize(10)).setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            
            
            List<String> lisSupuestos = new ArrayList<>(mapaLista.keySet());
			Collections.sort(lisSupuestos);
			// setFontColor(azul)
			for (int x = 0; x < lisSupuestos.size(); x++) {
				//for (int y = 1; y <=2; y++) {
		            supuestos.addCell(new Cell()
		                    .add(new Paragraph(retornaSupuesto(lisSupuestos.get(x))).setFont(arial).setFontSize(8))
		                    .setTextAlignment(TextAlignment.LEFT));
		            if ("ENCONTRADO".equalsIgnoreCase(Utils.noNulo(mapaLista.get(lisSupuestos.get(x))))) {
		            	supuestos.addCell(new Cell()
			                    .add(new Paragraph(mapaLista.get(lisSupuestos.get(x))).setFont(arial).setFontSize(8)).setFontColor(rojo)
			                    .setTextAlignment(TextAlignment.LEFT));
		            }else {
		            	supuestos.addCell(new Cell()
			                    .add(new Paragraph(mapaLista.get(lisSupuestos.get(x))).setFont(arial).setFontSize(8))
			                    .setTextAlignment(TextAlignment.LEFT));
		            }
				//}
			}
            
            doc.add(supuestos);
            
            // ======= 5 SALTOS =======
           // for (int i = 0; i < 5; i++) doc.add(new Paragraph(" "));

            // ======= RESULTADO =======
            Table resultado = new Table(UnitValue.createPercentArray(new float[]{35f, 65f}))
                    .setWidth(UnitValue.createPercentValue(80))
                    .setBorder(Border.NO_BORDER)
                    .setMarginTop(8);
            
            resultado.addCell(new Cell()
                    .add(new Paragraph("Resultado búsqueda: ").setFont(arial).setFontSize(15))
                    .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            resultado.addCell(new Cell()
                    .add(new Paragraph("Publicado en Listas Negras").setFont(arial).setFontSize(15)).setFontColor(rojo).setBold()
                    .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            
            doc.add(resultado);

           
            // ======= PÁRRAFO FINAL JUSTIFICADO =======
            Paragraph parrafoFinal = new Paragraph()
                    .add(new Text("Con base en la información vigente al momento de la emisión de este reporte, el día de hoy ")
                            .setFont(arial).setFontSize(10))
                    .add(new Text("el contribuyente señalado aparece publicado ")
                            .setFont(arial).setFontSize(10).setBold())
                    .add(new Text("en los listados previstos en los artículos 69, 69-B y 69-B Bis del Código Fiscal de la Federación.")
                            .setFont(arial).setFontSize(10))
                    .setTextAlignment(TextAlignment.JUSTIFIED);
            doc.add(parrafoFinal);

            // ======= SALTO + ADVERTENCIA =======
            doc.add(new Paragraph("\n"));
            Paragraph advertenciaTitulo = new Paragraph("Advertencia")
                    .setFont(arial)
                    .setFontSize(8)
                    .setBold()
                    .setItalic()
                    .setUnderline()
                    .setTextAlignment(TextAlignment.LEFT);
            doc.add(advertenciaTitulo);
            doc.add(new Paragraph(" "));

            String textoAdvertencia = """
                    El presente reporte se generó a partir de los datos capturados por el usuario en el sistema SIAREX Technologies. Se recomienda utilizar siempre el Registro Federal de Contribuyentes (RFC) como criterio principal de búsqueda y verificar que éste haya sido ingresado correctamente antes de generar el reporte. SIAREX Technologies actualiza de forma continua su base de datos con la información pública que el Servicio de Administración Tributaria (SAT) y el Diario Oficial de la Federación (DOF) publican periódicamente en sus respectivos portales oficiales (www.sat.gob.mx y www.dof.gob.mx). No obstante, SIAREX Technologies no tiene control alguno sobre la exactitud, oportunidad o veracidad de dicha información, ni garantiza su disponibilidad o integridad. La información relativa a notificaciones por estrados se encuentra disponible a partir del 27 de abril de 2016 y se actualiza conforme a las publicaciones oficiales del SAT. El hecho de que, a la fecha y hora de emisión de este reporte, el contribuyente consultado no aparezca publicado en los listados a que se refieren los artículos 69, 69-B y 69-B Bis del Código Fiscal de la Federación, no constituye garantía alguna de que no pueda ser incluido en futuras publicaciones. Este reporte no representa una autorización, certificación o constancia oficial para realizar operaciones con el contribuyente consultado. El uso de este reporte no sustituye la obligación legal del usuario de consultar directamente las publicaciones del SAT y del DOF. En consecuencia, SIAREX Technologies no será responsable de daños, perjuicios, pérdidas o consecuencias legales derivadas del uso, interpretación o decisiones tomadas con base en la información aquí contenida, aun cuando dichas decisiones se hubieran tomado de buena fe. La información incluida en este documento no constituye asesoría, opinión o recomendación legal, fiscal ni contable. Se recomienda consultar a un abogado o asesor fiscal especializado en las disposiciones del artículo 69-B del Código Fiscal de la Federación y en materia de materialidad de operaciones antes de emprender cualquier acción o decisión.
                    """;

            Paragraph advertenciaTexto = new Paragraph(textoAdvertencia)
                    .setFont(arial)
                    .setFontSize(8)
                    .setBold()
                    .setTextAlignment(TextAlignment.JUSTIFIED);
            doc.add(advertenciaTexto);
            
        }
    }

    // ======= MÉTODO FECHA FORMATEADA =======
    private static String obtenerFechaFormateada() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy hh:mm a", new Locale("es", "MX"));
        String fecha = ahora.format(formatter);
        return fecha.substring(0, 1).toUpperCase() + fecha.substring(1);
    }

    private ImageData loadLogoDataFlexible(String imagePathOrClasspath) throws IOException {
        String normalized = imagePathOrClasspath.replace("file://", "").replace("file:", "");
        Path p = Paths.get(normalized);
        if (Files.exists(p)) return ImageDataFactory.create(p.toAbsolutePath().toString());
        try (InputStream is = getClass().getResourceAsStream(imagePathOrClasspath)) {
            if (is == null) throw new IOException("No se encontró la imagen en resources.");
            return ImageDataFactory.create(is.readAllBytes());
        }
    }

    private PdfFont loadFontOrFallback(String fontClasspath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(fontClasspath)) {
            if (is == null) return PdfFontFactory.createFont(StandardFonts.HELVETICA);
            Path tmp = Files.createTempFile("font-", ".ttf");
            Files.write(tmp, is.readAllBytes());
            FontProgram fp = FontProgramFactory.createFont(tmp.toAbsolutePath().toString());
            return PdfFontFactory.createFont(fp);
        } catch (Exception ex) {
            return PdfFontFactory.createFont(StandardFonts.HELVETICA);
        }
    }

    // ======= HEADER: logo fijo =======
    static class HeaderHandler implements IEventHandler {
        private final ImageData logoData;
        private final String text;
        private final PdfFont font;
        private final float fontSize;
        private final float marginLeft, marginRight, marginTop, headerHeight;

        HeaderHandler(ImageData logoData, String text, PdfFont font, float fontSize,
                      float marginLeft, float marginRight, float marginTop, float headerHeight) {
            this.logoData = logoData;
            this.text = text;
            this.font = font;
            this.fontSize = fontSize;
            this.marginLeft = marginLeft;
            this.marginRight = marginRight;
            this.marginTop = marginTop;
            this.headerHeight = headerHeight;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            var page = docEvent.getPage();
            var ps = page.getPageSize();

            float headerTopY = ps.getHeight() - marginTop;
            float headerBottomY = headerTopY - headerHeight;
            Rectangle headerRect = new Rectangle(
                    marginLeft, headerBottomY, ps.getWidth() - marginLeft - marginRight, headerHeight);

            Image placed = new Image(logoData);
            float maxImgW = 160f, maxImgH = headerHeight - 8f;
            float imgW = placed.getImageWidth();
            float imgH = placed.getImageHeight();
            float scale = Math.min(Math.min(maxImgW / imgW, maxImgH / imgH), 1f);
            float drawW = imgW * scale;
            float drawH = imgH * scale;

            float imgX = headerRect.getLeft();
            float imgY = headerRect.getBottom() + (headerRect.getHeight() - drawH) / 2f;

            placed.scaleAbsolute(drawW, drawH);
            int pageNum = pdf.getPageNumber(page);
            placed.setFixedPosition(pageNum, imgX, imgY);

            try (Canvas canvas = new Canvas(new PdfCanvas(page), ps)) {
                canvas.add(placed);
                canvas.setFont(font).setFontSize(fontSize);
                canvas.showTextAligned(
                        text,
                        headerRect.getRight(),
                        headerRect.getBottom() + headerRect.getHeight() / 2f,
                        TextAlignment.RIGHT,
                        VerticalAlignment.MIDDLE,
                        0
                );
            }
        }
    }
    
    private String retornaSupuesto(String llaveSupuesto) {
    	HashMap<String, String> mapaLista = new HashMap<String, String>();
    	mapaLista.put("CANCELADOS POR INSOLVENCIA", "Cancelados por Insolvencia");
    	mapaLista.put("CANCELADOS", "Cancelados");
    	mapaLista.put("CANCELADOS POR INCOSTEABILIDAD", "Cancelados por Incosteabilidad");
    	mapaLista.put("Artículo 74", "Artículo 74");
    	mapaLista.put("CONDONADOS", "Condonados");
    	mapaLista.put("RETORNO INVERSIONES", "Retorno de Inversiones");
    	mapaLista.put("Definitivo", "Definitivo");
    	mapaLista.put("Desvirtuado", "Desvirtuado");
    	mapaLista.put("Presunto", "Presunto");
    	mapaLista.put("Sentencia Favorable", "Sentencia Favorable");
    	mapaLista.put("CONDONADOS0715", "Condonados 0715");
    	mapaLista.put("CANCELADOS0715", "Cancelados 0715");
    	mapaLista.put("EXIGIBLES", "Exigibles");
    	mapaLista.put("FIRMES", "Firmes");
    	mapaLista.put("NO LOCALIZADOS", "No Localizados");
    	mapaLista.put("SENTENCIAS", "Sentencias");
    	
    	String valSupuesto = mapaLista.get(llaveSupuesto);
    	if (valSupuesto == null) {
    		valSupuesto = llaveSupuesto;
    	}
    	return valSupuesto;
    	
    }
    
}
    
