/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.xmltopdf.Nomina.Escribir;
import com.itextpdf.xmltopdf.Pagos.PDoctoRelacionado;
import com.itextpdf.xmltopdf.Pagos.PImpuestos;
import com.itextpdf.xmltopdf.Pagos.PRetencion;
import com.itextpdf.xmltopdf.Pagos.PTraslado;
import com.itextpdf.xmltopdf.Pagos.Pago;

/**
 *
 * @author Faustino Rojas Arellano
 * @version 24/12/2021
 * @see www.hunterpos.com
 */
public final class CreaPDF {

        private Document DOCUMENTO;
        private PdfDocument DOCUMENTO_PDF;
        private Comprobante COMPROBANTE;
        private String RUTA_LOGO;

        public static final String REGULAR = "/opt/tomcat11/fuentes/fonts/arialmt.ttf";
        public static final String BOLD = "/opt/tomcat11/fuentes/fonts/arial-mt-bold.ttf";
        PdfFont bold;
        PdfFont regular;
        public static final Logger logger = Logger.getLogger("siarex247");
        
        public CreaPDF() {
                try {
                        FontProgram fontProgramRegular = FontProgramFactory.createFont(REGULAR);
                        FontProgram fontProgramBold = FontProgramFactory.createFont(BOLD);

                        bold = PdfFontFactory.createFont(fontProgramBold);
                        regular = PdfFontFactory.createFont(fontProgramRegular);
                } catch (IOException error) {
                        System.out.print(error);
                }
        }

        public void Generar(Comprobante COMPROBANTE, String rutaPDF, String rutaLogo) {
                if (COMPROBANTE == null) {
                        return;
                }
                this.COMPROBANTE = COMPROBANTE;
                this.RUTA_LOGO = rutaLogo;
                // FileOutputStream fos;
                try {
                        // fos = new FileOutputStream(rutaPDF);
                        PdfWriter writer = new PdfWriter(rutaPDF);
                        DOCUMENTO_PDF = new PdfDocument(writer);
                        DOCUMENTO_PDF.addEventHandler(PdfDocumentEvent.END_PAGE, new MyEventHandler());
                        DOCUMENTO = new Document(DOCUMENTO_PDF, new PageSize(612, 792));
                        DOCUMENTO.setMargins(Utilidades.cmToFloat(.8f), Utilidades.cmToFloat(0.7f),
                                        Utilidades.cmToFloat(2f), Utilidades.cmToFloat(.7F));
                        SeleccionarFormato();
                } catch (Exception e) {

                        return;
                }
        }

        public void GenerarByXML(String rutaXML, String rutaPDF, String rutaLogo) {
                try {
                        COMPROBANTE = LeerXML.ObtenerComprobante(rutaXML);
                        if (COMPROBANTE == null) {
                                return;
                        }
                        this.RUTA_LOGO = rutaLogo;
                        // FileOutputStream fos = new FileOutputStream(rutaPDF);
                        PdfWriter writer = new PdfWriter(rutaPDF);
                        DOCUMENTO_PDF = new PdfDocument(writer);
                        DOCUMENTO_PDF.addEventHandler(PdfDocumentEvent.END_PAGE, new MyEventHandler());
                        DOCUMENTO = new Document(DOCUMENTO_PDF, new PageSize(612, 792));
                        DOCUMENTO.setMargins(Utilidades.cmToFloat(.8f), Utilidades.cmToFloat(0.7f),
                                        Utilidades.cmToFloat(2f), Utilidades.cmToFloat(.7F));
                        SeleccionarFormato();
                        DOCUMENTO.close();
                } catch (FileNotFoundException e) {
                        javax.swing.JOptionPane.showMessageDialog(null, e.toString(), "Erro al abrir archivo.",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        private void SeleccionarFormato() {
                if (COMPROBANTE.getTipoDeComprobante().equals("N")) {
                        FormatoNomina(); // Opcional FormatoNomina()
                } else if (COMPROBANTE.getTipoDeComprobante().equals("P")
                                && COMPROBANTE.getComplemento().Pagos20 != null) {
                        FormatoPagos20();// FormatoPagos();
                } else if (COMPROBANTE.getTipoDeComprobante().equals("P")
                                && COMPROBANTE.getComplemento().Pagos != null) {
                        FormatoPagos();// FormatoPagos();
                } else if (COMPROBANTE.AcuseCancelacion != null) {
                        FormatoAcuseCancelacion();
                } else if (COMPROBANTE.getComplemento() != null && COMPROBANTE.getComplemento().CartaPorte10 != null) {
                        FormatoCartaPorte10SAT();
                } else if (COMPROBANTE.getComplemento() != null && COMPROBANTE.getComplemento().CartaPorte20 != null) {
                        FormatoCartaPorte20();
                } else if (COMPROBANTE.getComplemento() != null && COMPROBANTE.getComplemento().CartaPorte30 != null) {
                        FormatoCartaPorte30();
                } else if (COMPROBANTE.getComplemento() != null && COMPROBANTE.getComplemento().CartaPorte31 != null) {
                	FormatoCartaPorte31();
                } else {
                        FormatoNormal();
                }
        }

        private void FormatoNormal() {
                AgregarEncabezado();
                if (COMPROBANTE.getInformacionGlobal() != null) {
                        AgregarInformacionGlobal();
                }
                if (COMPROBANTE.getCfdiRelacionados() != null) {
                        AgregarCfdisRelacionados();
                }
                AgregarDatosProductos();
                AgregarTotales();
                AgregarSellos();
        }

        private void FormatoPagos() {
                AgregarEncabezado();
                AgregarTablaPagos();
                AgregarDatosProductos();
                AgregarTotales();
                AgregarSellos();
        }

        private void FormatoPagos20() {
                AgregarEncabezado();
                AgregarTablaPagos20();
                AgregarDatosProductos();
                AgregarTotales();
                AgregarSellos();
        }

        private void FormatoNomina() {
                com.itextpdf.xmltopdf.Nomina.Escribir escribirNomina = new Escribir(COMPROBANTE, DOCUMENTO,
                                DOCUMENTO_PDF);
                escribirNomina.AgregarDatos();

        }

        private void FormatoCartaPorte10SAT() {
                AgregarEncabezadoCP10SAT();
                AgregarDetalleCartaPorte10SAT();
                AgregarDocumentosRelacionadosSAT();
                AgregarConceptosSAT();
                AgregarUbicaciones10SAT();
                AgregarMercancias10SAT();
                AgregarFiguraTransporte10SAT();
                AgregarSellosSAT();
        }

        private void FormatoCartaPorte20() {
                AgregarEncabezado();
                com.itextpdf.xmltopdf.CartaPorte20.Escribir escribirCP20 = new com.itextpdf.xmltopdf.CartaPorte20.Escribir(
                                COMPROBANTE, DOCUMENTO);
                escribirCP20.AgregarConceptosCartaPorte20();
                escribirCP20.AgregarConceptosCartaPorte20();
                escribirCP20.AgregarDatosCartaPorte20();
                escribirCP20.AgregarUbicacionesCartaPorte20();
                escribirCP20.AgregarMercanciasCartaPorte20();
                escribirCP20.AgregarFigurasTransporteCartaPorte20();
                AgregarSellos();
        }

        private void FormatoCartaPorte30() {
                AgregarEncabezado();
                com.itextpdf.xmltopdf.CartaPorte30.Escribir escribirCP30 = new com.itextpdf.xmltopdf.CartaPorte30.Escribir(
                                COMPROBANTE, DOCUMENTO);
                escribirCP30.AgregarConceptos();
                escribirCP30.AgregarDatos();
                escribirCP30.AgregarUbicaciones();
                escribirCP30.AgregarMercancias();
                escribirCP30.AgregarFigurasTransporte();
                AgregarSellos();
        }

        
        private void FormatoCartaPorte31() {
            AgregarEncabezado();
            com.itextpdf.xmltopdf.CartaPorte31.Escribir escribirCP31 = new com.itextpdf.xmltopdf.CartaPorte31.Escribir(
                            COMPROBANTE, DOCUMENTO);
            escribirCP31.AgregarConceptos();
            escribirCP31.AgregarDatos();
            escribirCP31.AgregarUbicaciones();
            escribirCP31.AgregarMercancias();
            escribirCP31.AgregarFigurasTransporte();
            AgregarSellos();
    }
        
        private void FormatoAcuseCancelacion() {
                DOCUMENTO.add(new Paragraph("Acuse de Solicitud Cancelación de CFDI").setFontSize(12).setBold()
                                .setHorizontalAlignment(HorizontalAlignment.CENTER));
                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 6f, 10f });
                Table tablaDatos = new Table(anchoColumnas);
                tablaDatos.addCell(new Paragraph("Fecha y hora de cancelación:").setBold());
                tablaDatos.addCell(new Paragraph(
                                COMPROBANTE.AcuseCancelacion.getFecha().format(DateTimeFormatter.ISO_DATE)));
                tablaDatos.addCell(new Paragraph("Rfc Emisor:").setBold());
                tablaDatos.addCell(new Paragraph(COMPROBANTE.AcuseCancelacion.getRfcEmisor()));
                tablaDatos.addCell(new Paragraph("Sello digital SAT:"));
                tablaDatos.addCell(new Paragraph(COMPROBANTE.AcuseCancelacion.getSelloDigitalSAT()));
                DOCUMENTO.add(tablaDatos);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 8.0F, 8.0F });
                Table tablaFolios = new Table(anchoColumnas);
                tablaFolios.addCell(new Paragraph("Folio fiscal UUID:"));
                tablaFolios.addCell(new Paragraph("Estado CFDI:"));
                tablaFolios.addCell(new Paragraph(COMPROBANTE.AcuseCancelacion.getFolios().getUUID()).setBold()
                                .setPaddingTop(10).setPaddingBottom(10));
                tablaFolios.addCell(new Paragraph(COMPROBANTE.AcuseCancelacion.getFolios().getEstatusUUID() + " - "
                                + Catalogos.ObtenerEstatusUUID(
                                                COMPROBANTE.AcuseCancelacion.getFolios().getEstatusUUID()))
                                .setBold()
                                .setPaddingTop(10).setPaddingBottom(10));
                DOCUMENTO.add(tablaFolios);
                DOCUMENTO.add(new Paragraph(COMPROBANTE.XML));
        }

        // private void AgregaPropiedadesDocumento() {
        // DOCUMENTO.addAuthor("Advansys S.A de C.V");
        // DOCUMENTO.addCreator("Sample application using iParagraphSharp");
        // DOCUMENTO.addKeywords("Reporte de Hunter");
        // DOCUMENTO.addSubject("Document subject - Describing the steps creating a PDF
        // document");
        // DOCUMENTO.addTitle("Reporte del sistema Hunter");
        // DOCUMENTO.setMargins(Utilidades.cmToFloat(.5f), Utilidades.cmToFloat(.5f),
        // Utilidades.cmToFloat(.5f),
        // Utilidades.cmToFloat(.5f));
        // }
        private void AgregarCfdisRelacionados() {
                Color color = new DeviceRgb(241, 241, 241);
                Border borde = new SolidBorder(color, 1);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setBackgroundColor(ColorConstants.BLACK);
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setBorder(borde);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(borde);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(7);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setFontColor(ColorConstants.WHITE);

                Style estiloValor = new Style();
                estiloValor.setFontSize(7);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                CfdiRelacionados cfdisRelacionados = COMPROBANTE.getCfdiRelacionados();
                if (cfdisRelacionados == null) {
                        return;
                }
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 20F });
                Table tablaCFDISRelacionados = new Table(tamanoColumnas);
                StringBuilder relacion = new StringBuilder();
                relacion.append("CFDI'S RELACIONADOS (");
                relacion.append(cfdisRelacionados.getTipoRelacion()).append("- ");
                relacion.append(Catalogos.ObtenerTipoRelacion(cfdisRelacionados.getTipoRelacion())).append(")");
                Cell ctitulo = new Cell(1, 6).add(new Paragraph(relacion.toString()).addStyle(estiloTitle));
                ctitulo.addStyle(estiloCeldaTitle);
                tablaCFDISRelacionados.addCell(ctitulo);
                for (int i = 0; i < cfdisRelacionados.getCfdiRelacionado().size(); i++) {
                	    //CfdiRelacionado cfdiRelacionado = new CfdiRelacionado();
                		CfdiRelacionado cfdiRelacionado = cfdisRelacionados.getCfdiRelacionado().get(i);
                	    tablaCFDISRelacionados.addCell(new Cell()
                                        .add(new Paragraph(cfdiRelacionado.getUUID()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                DOCUMENTO.add(tablaCFDISRelacionados);
        }

        private void AgregarEncabezadoCP10SAT() {
                Style estiloTitulo = new Style();
                estiloTitulo.setFontSize(10);
                estiloTitulo.setFont(bold);
                // estiloTitulo.setBold();

                Style estiloCelda = new Style();
                estiloCelda.setBorder(Border.NO_BORDER);
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setFont(bold);
                // estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 2.8F, 5.7F });
                Table tablaIzquierda = new Table(anchoColumnas);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 4.5F, 6F });
                Table tablaDerecha = new Table(anchoColumnas);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 8.5F, 10.5F });
                Table tablaEncabezado = new Table(anchoColumnas);

                tablaIzquierda
                                .addCell(new Cell().add(new Paragraph("RFC emisor:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaIzquierda.addCell(
                                new Cell().add(new Paragraph(COMPROBANTE.getEmisor().getRfc()).addStyle(estiloValor))
                                                .addStyle(estiloCelda));

                tablaIzquierda.addCell(
                                new Cell().add(new Paragraph("Nombre emisor:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaIzquierda.addCell(
                                new Cell().add(new Paragraph(COMPROBANTE.getEmisor().getNombre()).addStyle(estiloValor))
                                                .addStyle(estiloCelda));

                if (COMPROBANTE.getCfdiRelacionados() != null) {
                        tablaIzquierda.addCell(
                                        new Cell().add(new Paragraph("Tipo de relación:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaIzquierda.addCell(new Cell()
                                        .add(new Paragraph(Catalogos.ObtenerTipoRelacion(
                                                        COMPROBANTE.getCfdiRelacionados().getTipoRelacion()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                tablaIzquierda
                                .addCell(new Cell().add(new Paragraph("RFC Receptor:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaIzquierda.addCell(
                                new Cell().add(new Paragraph(COMPROBANTE.getReceptor().getRfc()).addStyle(estiloValor))
                                                .addStyle(estiloCelda));

                tablaIzquierda.addCell(
                                new Cell().add(new Paragraph("Nombre Receptor").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaIzquierda.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getReceptor().getNombre()).addStyle(estiloValor))
                                .addStyle(estiloCelda));

                tablaIzquierda
                                .addCell(new Cell().add(new Paragraph("Uso CFDI:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaIzquierda.addCell(new Cell()
                                .add(new Paragraph(Catalogos.ObtenerUsoCFDI(COMPROBANTE.getReceptor().getUsoCFDI()))
                                                .addStyle(estiloValor))
                                .addStyle(estiloCelda));

                tablaDerecha
                                .addCell(new Cell().add(new Paragraph("Folio fiscal:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaDerecha.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID())
                                                .addStyle(estiloValor))
                                .addStyle(estiloCelda));

                tablaDerecha.addCell(
                                new Cell().add(new Paragraph("No. de serie del CSD:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaDerecha.addCell(new Cell().add(new Paragraph(COMPROBANTE.getNoCertificado()).addStyle(estiloValor))
                                .addStyle(estiloCelda));

                if (!COMPROBANTE.getSerie().isEmpty()) {
                        tablaDerecha.addCell(
                                        new Cell().add(new Paragraph("Serie y folio:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaDerecha.addCell(
                                        new Cell().add(new Paragraph(COMPROBANTE.getSerie() + COMPROBANTE.getFolio())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }

                tablaDerecha.addCell(
                                new Cell().add(new Paragraph("Código postal, fecha y hora de emisión:")
                                                .addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaDerecha.addCell(
                                new Cell().add(new Paragraph(COMPROBANTE.getLugarExpedicion() + " "
                                                + Utilidades.ToString(COMPROBANTE.getFecha()))
                                                .addStyle(estiloValor)).addStyle(estiloCelda));
                tablaDerecha.addCell(
                                new Cell().add(new Paragraph("Efecto de comprobante:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaDerecha.addCell(new Cell()
                                .add(new Paragraph(Catalogos.ObtenerTipoComprobante(COMPROBANTE.getTipoDeComprobante()))
                                                .addStyle(estiloValor))
                                .addStyle(estiloCelda));
                tablaDerecha.addCell(
                                new Cell().add(new Paragraph("Régimen fiscal:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaDerecha.addCell(new Cell().add(
                                new Paragraph(Catalogos
                                                .ObtenerRegimenFiscal(COMPROBANTE.getEmisor().getRegimenFiscal()))
                                                .addStyle(estiloValor))
                                .addStyle(estiloCelda));

                tablaEncabezado.addCell(new Cell().add(tablaIzquierda).addStyle(estiloCelda));
                tablaEncabezado.addCell(new Cell().add(tablaDerecha).addStyle(estiloCelda));
                DOCUMENTO.add(new Paragraph("Carta porte").addStyle(estiloTitulo));
                DOCUMENTO.add(tablaEncabezado);
        }

        private void AgregarDocumentosRelacionadosSAT() {
                Style estiloCelda = new Style();
                estiloCelda.setBorder(Border.NO_BORDER);
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setFont(bold);
                // estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                if (COMPROBANTE.getCfdiRelacionados() == null) {
                        return;
                }
                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5F, 7.7F });
                Table tablaCfdiRelacionados = new Table(anchoColumnas);
                for (int i = 0; i < COMPROBANTE.getCfdiRelacionados().getCfdiRelacionado().size(); i++) {
                        CfdiRelacionado f = COMPROBANTE.getCfdiRelacionados().getCfdiRelacionado().get(i);
                        tablaCfdiRelacionados.addCell(new Cell()
                                        .add(new Paragraph("Folio fiscal a relacionar:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaCfdiRelacionados
                                        .addCell(new Cell().add(new Paragraph(f.getUUID()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                DOCUMENTO.add(tablaCfdiRelacionados);
        }

        private void AgregarConceptosSAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setFontSize(10);
                estiloSubtitle.setFont(bold);
                // estiloSubtitle.setBold();

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(6);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setVerticalAlignment(VerticalAlignment.MIDDLE);
                // estiloTitle.setBold();
                estiloTitle.setFont(bold);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(6);
                estiloAtributo.setTextAlignment(TextAlignment.CENTER);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setTextAlignment(TextAlignment.CENTER);
                estiloValor.setFontSize(6);
                estiloAtributo.setFont(regular);

                Style estiloValorDescripcion = new Style();
                estiloValorDescripcion.setTextAlignment(TextAlignment.LEFT);
                estiloValorDescripcion.setFontSize(6);
                estiloAtributo.setFont(regular);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setVerticalAlignment(VerticalAlignment.MIDDLE);
                estiloCeldaTitle.setBackgroundColor(new DeviceRgb(191, 191, 191));

                Style estiloCeldaValor = new Style();
                estiloCeldaValor.setPaddingTop(0);
                estiloCeldaValor.setPaddingBottom(0);

                Style estiloCeldaImpuesto = new Style();
                estiloCeldaImpuesto.setPadding(0);
                estiloCeldaImpuesto.setBorder(Border.NO_BORDER);

                DOCUMENTO.add(new Paragraph("Conceptos").addStyle(estiloSubtitle));

                float[] anchoColumnas = Utilidades
                                .cmToFloat(new float[] { 2f, 2.3f, 1.6f, 1.4f, 1.8f, 2f, 1.8f, 1.8f, 2f, 2.2f });
                Table tablaConceptos = new Table(anchoColumnas);
                tablaConceptos
                                .addCell(new Cell()
                                                .add(new Paragraph("Clave del producto\n" + "y/o servicio")
                                                                .addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaConceptos.addCell(
                                new Cell().add(new Paragraph("No. identificación").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaConceptos
                                .addCell(new Cell().add(new Paragraph("Cantidad").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaConceptos.addCell(
                                new Cell().add(new Paragraph("Clave de unidad").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaConceptos
                                .addCell(new Cell().add(new Paragraph("Unidad").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaConceptos.addCell(
                                new Cell().add(new Paragraph("Valor unitario").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaConceptos
                                .addCell(new Cell().add(new Paragraph("Importe").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaConceptos
                                .addCell(new Cell().add(new Paragraph("Descuento").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaConceptos.addCell(
                                new Cell().add(new Paragraph("No. de pedimiento").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaConceptos.addCell(new Cell().add(new Paragraph("No. de cuenta predial").addStyle(estiloTitle))
                                .addStyle(estiloCeldaTitle));

                for (int i = 0; i < COMPROBANTE.getConceptos().getConcepto().size(); i++) {
                        Concepto c = COMPROBANTE.getConceptos().getConcepto().get(i);
                        tablaConceptos.addCell(new Cell().add(new Paragraph(c.getClaveProdServ()).addStyle(estiloValor))
                                        .addStyle(estiloCeldaValor));
                        tablaConceptos.addCell(
                                        new Cell().add(new Paragraph(c.getNoIdentificacion()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaConceptos.addCell(
                                        new Cell().add(new Paragraph(Utilidades.convertir(c.getCantidad()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaConceptos.addCell(
                                        new Cell().add(new Paragraph(c.getClaveUnidad()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaConceptos.addCell(
                                        new Cell().add(new Paragraph(c.getUnidad()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaConceptos.addCell(new Cell()
                                        .add(new Paragraph(Utilidades.convertir(c.getValorUnitario()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCeldaValor));
                        tablaConceptos.addCell(
                                        new Cell().add(new Paragraph(Utilidades.convertir(c.getImporte()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaConceptos.addCell(
                                        new Cell().add(new Paragraph(Utilidades.convertir(c.getDescuento()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        if (c.getInformacionAduanera() != null && c.getInformacionAduanera().size() > 0) {
                                tablaConceptos.addCell(
                                                new Paragraph(c.getInformacionAduanera().get(0).getNumeroPedimento()))
                                                .addStyle(estiloValor);
                        } else {
                                tablaConceptos.addCell(new Paragraph("").addStyle(estiloValor));
                        }
                        if (c.getCuentaPredial() != null) {
                                tablaConceptos.addCell(
                                                new Paragraph(c.getCuentaPredial().getNumero()).addStyle(estiloValor));
                        } else {
                                tablaConceptos.addCell(new Paragraph("").addStyle(estiloValor));
                        }
                        tablaConceptos
                                        .addCell(
                                                        new Cell()
                                                                        .add(new Paragraph("Descripción")
                                                                                        .addStyle(estiloTitle)
                                                                                        .setVerticalAlignment(
                                                                                                        VerticalAlignment.MIDDLE))
                                                                        .addStyle(estiloCeldaTitle));
                        tablaConceptos.addCell(new Cell(1, 4)
                                        .add(new Paragraph(c.getDescripcion()).addStyle(estiloValorDescripcion)
                                                        .setVerticalAlignment(VerticalAlignment.MIDDLE))
                                        .setVerticalAlignment(VerticalAlignment.MIDDLE));
                        if (c.getImpuestos() != null) {
                                anchoColumnas = Utilidades.cmToFloat(new float[] { 1.6f, 1f, 2.2f, 1.3f, 1.4f, 2.2f });
                                Table tablaImpuestos = new Table(anchoColumnas);
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Impuesto").addStyle(estiloAtributo))
                                                                .addStyle(estiloCeldaImpuesto));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Tipo").addStyle(estiloAtributo))
                                                                .addStyle(estiloCeldaImpuesto));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Base").addStyle(estiloAtributo))
                                                                .addStyle(estiloCeldaImpuesto));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Tipo \nFactor").addStyle(estiloAtributo))
                                                                .addStyle(estiloCeldaImpuesto));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Tasa o\nCouta").addStyle(estiloAtributo))
                                                                .addStyle(estiloCeldaImpuesto));
                                tablaImpuestos.addCell(new Cell().add(new Paragraph("Importe").addStyle(estiloAtributo))
                                                .addStyle(estiloCeldaImpuesto));
                                if (c.getImpuestos().getRetenciones().size() > 0) {
                                        for (int j = 0; j < c.getImpuestos().getRetenciones().size(); j++) {
                                                RetencionC r = c.getImpuestos().getRetenciones().get(j);
                                                tablaImpuestos.addCell(
                                                                new Cell().add(new Paragraph(
                                                                                Catalogos.ObtenerImpuesto(
                                                                                                r.getImpuesto()))
                                                                                .addStyle(estiloValor))
                                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos.addCell(new Cell()
                                                                .add(new Paragraph("Retención").addStyle(estiloValor))
                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos
                                                                .addCell(new Cell().add(
                                                                                new Paragraph(Utilidades
                                                                                                .convertir(r.getBase()))
                                                                                                .addStyle(estiloValor))
                                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos.addCell(new Cell()
                                                                .add(new Paragraph(r.getTipoFactor())
                                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos.addCell(new Cell().add(
                                                                new Paragraph(String.format("%.4f%%",
                                                                                r.getTasaOCuota() * 100f))
                                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos
                                                                .addCell(
                                                                                new Cell()
                                                                                                .add(new Paragraph(
                                                                                                                Utilidades.convertir(
                                                                                                                                r.getImporte()))
                                                                                                                .addStyle(estiloValor)
                                                                                                                .setTextAlignment(
                                                                                                                                TextAlignment.RIGHT))
                                                                                                .addStyle(estiloCeldaImpuesto));
                                        }
                                }
                                if (c.getImpuestos().getTraslados().size() > 0) {
                                        for (int j = 0; j < c.getImpuestos().getTraslados().size(); j++) {
                                                TrasladoC t = c.getImpuestos().getTraslados().get(j);
                                                tablaImpuestos.addCell(
                                                                new Cell().add(new Paragraph(
                                                                                Catalogos.ObtenerImpuesto(
                                                                                                t.getImpuesto()))
                                                                                .addStyle(estiloValor))
                                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos.addCell(new Cell()
                                                                .add(new Paragraph("Traslado").addStyle(estiloValor))
                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos
                                                                .addCell(new Cell().add(
                                                                                new Paragraph(Utilidades
                                                                                                .convertir(t.getBase()))
                                                                                				.addStyle(estiloValor))
                                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos.addCell(new Cell()
                                                                .add(new Paragraph(t.getTipoFactor())
                                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos.addCell(new Cell().add(
                                                                new Paragraph(String.format("%.4f%%",
                                                                                t.getTasaOCuota() * 100f))
                                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCeldaImpuesto));
                                                tablaImpuestos
                                                                .addCell(
                                                                                new Cell()
                                                                                                .add(new Paragraph(
                                                                                                                Utilidades.convertir(
                                                                                                                                t.getImporte()))
                                                                                                                .addStyle(estiloValor)
                                                                                                                .setTextAlignment(
                                                                                                                                TextAlignment.RIGHT))
                                                                                                .addStyle(estiloCeldaImpuesto));
                                        }
                                }
                                tablaConceptos.addCell(new Cell(1, 5).add(tablaImpuestos));
                        } else {
                                tablaConceptos.startNewRow();
                        }
                        DOCUMENTO.add(tablaConceptos);
                }
        }

        private void AgregarTotalesSAT() {
                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloAtributoTotales = new Style();
                estiloAtributoTotales.setFontSize(8);
                estiloAtributoTotales.setTextAlignment(TextAlignment.RIGHT);
                // estiloAtributoTotales.setBold();
                estiloAtributoTotales.setFont(regular);

                Style estiloValor = new Style();
                estiloValor.setTextAlignment(TextAlignment.LEFT);
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloValorMoneda = new Style();
                estiloValorMoneda.setTextAlignment(TextAlignment.RIGHT);
                estiloValorMoneda.setFontSize(8);

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 9.2f, 9.7f });
                Table tablaTotales = new Table(anchoColumnas);
                tablaTotales.setMarginTop(10);

                anchoColumnas = Utilidades.cmToFloat(new float[] { 3.2f, 6f });
                Table tablaIzquierda = new Table(anchoColumnas);

                anchoColumnas = Utilidades.cmToFloat(new float[] { 4f, 3.6f, 2.4f });
                Table tablaDerecha = new Table(anchoColumnas);

                tablaIzquierda.addCell(new Cell().add(new Paragraph("Moneda:").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tablaIzquierda
                                .addCell(new Cell()
                                                .add(new Paragraph(Catalogos.ObtenerMoneda(COMPROBANTE.getMoneda()))
                                                                .addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                if (!COMPROBANTE.getFormaPago().isEmpty()) {
                        tablaIzquierda.addCell(
                                        new Cell().add(new Paragraph("Forma de pago:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaIzquierda.addCell(
                                        new Cell().add(new Paragraph(
                                                        Catalogos.ObtenerFormaPago(COMPROBANTE.getFormaPago()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!COMPROBANTE.getMetodoPago().isEmpty()) {
                        tablaIzquierda.addCell(
                                        new Cell().add(new Paragraph("Metodo de pago:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaIzquierda.addCell(
                                        new Cell().add(new Paragraph(
                                                        Catalogos.ObtenerMetodoPago(COMPROBANTE.getMetodoPago()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }

                tablaDerecha.addCell(new Cell().add(new Paragraph("Subtotal:").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tablaDerecha.addCell(new Cell().add(new Paragraph("").addStyle(estiloValor)).addStyle(estiloCelda));
                tablaDerecha
                                .addCell(new Cell()
                                                .add(new Paragraph(Utilidades.convertir(COMPROBANTE.getSubTotal()))
                                                                .addStyle(estiloValorMoneda))
                                                .addStyle(estiloCelda));
                if (COMPROBANTE.getImpuestos() != null) {
                        for (int i = 0; i < COMPROBANTE.getImpuestos().getTraslados().size(); i++) {
                                Traslado t = COMPROBANTE.getImpuestos().getTraslados().get(i);
                                tablaDerecha.addCell(new Cell()
                                                .add(new Paragraph("Impuestos trasladados:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tablaDerecha.addCell(new Cell()
                                                .add(new Paragraph(Catalogos.ObtenerImpuesto(t.getImpuesto())
                                                                + String.format("%.6f", t.getTasaOCuota()))
                                                                .addStyle(estiloValorMoneda))
                                                .addStyle(estiloCelda));
                                tablaDerecha
                                                .addCell(new Cell()
                                                                .add(new Paragraph(Utilidades.convertir(t.getImporte()))
                                                                                .addStyle(estiloValorMoneda))
                                                                .addStyle(estiloCelda));

                        }
                        for (int i = 0; i < COMPROBANTE.getImpuestos().getRetenciones().size(); i++) {
                                Retencion r = COMPROBANTE.getImpuestos().getRetenciones().get(i);
                                tablaDerecha.addCell(new Cell()
                                                .add(new Paragraph("Impuestos Retenidos:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tablaDerecha.addCell(
                                                new Cell().add(new Paragraph(Catalogos.ObtenerImpuesto(r.getImpuesto()))
                                                                .addStyle(estiloValorMoneda))
                                                                .addStyle(estiloCelda));
                                tablaDerecha
                                                .addCell(new Cell()
                                                                .add(new Paragraph(Utilidades.convertir(r.getImporte()))
                                                                                .addStyle(estiloValorMoneda))
                                                                .addStyle(estiloCelda));
                        }
                }
                tablaDerecha.addCell(
                                new Cell().add(new Paragraph("Total:").addStyle(estiloAtributo)).addStyle(estiloCelda));
                tablaDerecha.addCell(new Cell().add(new Paragraph("").addStyle(estiloValor)).addStyle(estiloCelda));
                tablaDerecha.addCell(
                                new Cell().add(new Paragraph(Utilidades.convertir(COMPROBANTE.getTotal()))
                                                .addStyle(estiloValorMoneda).setBold())
                                                .addStyle(estiloCelda));

                tablaTotales.addCell(new Cell().add(tablaIzquierda).addStyle(estiloCelda));
                tablaTotales.addCell(new Cell().add(tablaDerecha).addStyle(estiloCelda).setMarginLeft(6));
                DOCUMENTO.add(tablaTotales);
        }

        private void AgregarPagosSAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setMarginLeft(7);
                estiloSubtitle.setFontSize(10);
                estiloSubtitle.setFont(bold);
                // estiloSubtitle.setBold();

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloCeldaTabla = new Style();
                estiloCeldaTabla.setPadding(0);
                estiloCeldaTabla.setBorder(Border.NO_BORDER);

                SolidLine line = new SolidLine(.8f);
                line.setColor(ColorConstants.BLACK);
                LineSeparator ls = new LineSeparator(line);
                ls.setMarginTop(5);
                ls.setWidth(Utilidades.cmToFloat(19));

                if (COMPROBANTE.getComplemento() == null || COMPROBANTE.getComplemento().Pagos == null) {
                        return;
                }
                for (int i = 0; i < COMPROBANTE.getComplemento().Pagos.getPago().size(); i++) {
                        Pago p = COMPROBANTE.getComplemento().Pagos.getPago().get(i);
                        DOCUMENTO.add(new Paragraph("Información del pago").addStyle(estiloSubtitle));
                        float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 10.8f, 8.1f });
                        Table tablaPago = new Table(anchoColumnas);
                        tablaPago = new Table(anchoColumnas);

                        anchoColumnas = Utilidades.cmToFloat(new float[] { 5f, 5.7f });
                        Table tablaPagoIzquierda = new Table(anchoColumnas);

                        anchoColumnas = Utilidades.cmToFloat(new float[] { 3.1f, 4.8f });
                        Table tablaPagoDerecha = new Table(anchoColumnas);

                        tablaPagoIzquierda.addCell(
                                        new Cell().add(new Paragraph("Forma de pago:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaPagoIzquierda
                                        .addCell(new Cell()
                                                        .add(new Paragraph(
                                                                        Catalogos.ObtenerFormaPago(p.getFormaDePagoP()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                        if (!p.getNumOperacion().isEmpty()) {
                                tablaPagoIzquierda.addCell(new Cell()
                                                .add(new Paragraph("Número operación:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tablaPagoIzquierda.addCell(
                                                new Cell().add(new Paragraph(
                                                                Catalogos.ObtenerFormaPago(p.getFormaDePagoP()))
                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                        }

                        tablaPagoDerecha.addCell(
                                        new Cell().add(new Paragraph("Fecha de pago:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaPagoDerecha.addCell(
                                        new Cell().add(new Paragraph(Utilidades.ToString(p.getFechaPago()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                        tablaPagoDerecha.addCell(
                                        new Cell().add(new Paragraph("Moneda de pago:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaPagoDerecha.addCell(new Cell()
                                        .add(new Paragraph(Catalogos.ObtenerMoneda(p.getMonedaP()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                        tablaPagoDerecha
                                        .addCell(new Cell().add(new Paragraph("Total:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaPagoDerecha.addCell(
                                        new Cell().add(new Paragraph(Utilidades.convertir(p.getMonto()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));

                        tablaPago.addCell(new Cell().add(tablaPagoIzquierda).addStyle(estiloCelda));
                        tablaPago.addCell(new Cell().add(tablaPagoDerecha).addStyle(estiloCelda));

                        for (int j = 0; j < p.getDoctoRelacionado().size(); j++) {
                                PDoctoRelacionado d = p.getDoctoRelacionado().get(j);
                                anchoColumnas = Utilidades.cmToFloat(new float[] { 10.4f, 11f });
                                Table tablaCfdisRelacionados = new Table(anchoColumnas);
                                tablaCfdisRelacionados.setFixedLayout();

                                anchoColumnas = Utilidades.cmToFloat(new float[] { 3.3f, 7.1f });
                                Table tablaCfdisRelacionadosIzquierda = new Table(anchoColumnas);

                                anchoColumnas = Utilidades.cmToFloat(new float[] { 6.9f, 3.9f });
                                Table tablaCfdisRelacionadosDerecha = new Table(anchoColumnas);

                                tablaCfdisRelacionadosIzquierda.addCell(
                                                new Cell().add(new Paragraph("Id documento:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosIzquierda.addCell(
                                                new Cell().add(new Paragraph(d.getIdDocumento()).addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosIzquierda.addCell(
                                                new Cell().add(new Paragraph("Folio:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosIzquierda.addCell(
                                                new Cell().add(new Paragraph(d.getFolio()).addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosIzquierda.addCell(new Cell()
                                                .add(new Paragraph("Número parcialidad:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosIzquierda.addCell(new Cell()
                                                .add(new Paragraph(d.getNumParcialidad()).addStyle(estiloValor))
                                                .addStyle(estiloCelda));

                                tablaCfdisRelacionadosDerecha.addCell(
                                                new Cell().add(new Paragraph("Moneda del documento relacionado:")
                                                                .addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosDerecha
                                                .addCell(new Cell()
                                                                .add(new Paragraph(Catalogos
                                                                                .ObtenerMoneda(d.getMonedaDR()))
                                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosDerecha.addCell(new Cell()
                                                .add(new Paragraph("Metodo de pago del documento relacionado:")
                                                                .addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosDerecha.addCell(
                                                new Cell().add(new Paragraph(
                                                                Catalogos.ObtenerMetodoPago(d.getMetodoDePagoDR()))
                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosDerecha.addCell(new Cell()
                                                .add(new Paragraph("Importe saldo anterior:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosDerecha.addCell(new Cell()
                                                .add(new Paragraph(Utilidades.convertir(d.getImpSaldoAnt()))
                                                                .addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosDerecha.addCell(new Cell()
                                                .add(new Paragraph("Importe pagado:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosDerecha.addCell(new Cell()
                                                .add(new Paragraph(Utilidades.convertir(d.getImpPagado()))
                                                                .addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosDerecha
                                                .addCell(new Cell()
                                                                .add(new Paragraph("Importe de saldo insoluto:")
                                                                                .addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tablaCfdisRelacionadosDerecha
                                                .addCell(new Cell()
                                                                .add(new Paragraph(Utilidades
                                                                                .convertir(d.getImpSaldoInsoluto()))
                                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));

                                tablaCfdisRelacionados
                                                .addCell(new Cell(1, 2)
                                                                .add(new Paragraph("Documento relacionado")
                                                                                .addStyle(estiloAtributo))
                                                                .addStyle(estiloCeldaTabla));
                                tablaCfdisRelacionados
                                                .addCell(new Cell().add(tablaCfdisRelacionadosIzquierda)
                                                                .addStyle(estiloCeldaTabla));
                                tablaCfdisRelacionados
                                                .addCell(new Cell().add(tablaCfdisRelacionadosDerecha)
                                                                .addStyle(estiloCeldaTabla));

                                tablaPago.addCell(
                                                new Cell(1, 2).add(tablaCfdisRelacionados).addStyle(estiloCeldaTabla));
                        }

                        DOCUMENTO.add(tablaPago);
                        DOCUMENTO.add(ls);

                }
        }

        private void AgregarSellosSAT() {
                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(7);
                estiloAtributo.setFont(bold);
                // estiloAtributo.setBold();

                Style estiloValor = new Style();
                estiloValor.setFontSize(7);
                estiloValor.setFont(regular);

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 4.5f, 3.2f, 3.8f, 4f });
                Table tablaSellos = new Table(anchoColumnas);
                tablaSellos.setKeepTogether(true);
                tablaSellos.setWidth(22.5f);
                tablaSellos.setFixedLayout();
                tablaSellos.setMarginTop(10);
                tablaSellos.addCell(
                                new Cell(1, 5).add(new Paragraph("Sello digital del CFDI:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaSellos.addCell(new Cell(1, 5).add(
                                new Paragraph(COMPROBANTE.getComplemento().TimbreFiscalDigital.getSelloCFD())
                                                .addStyle(estiloValor))
                                .addStyle(estiloCelda));
                tablaSellos.addCell(new Cell(1, 5).add(new Paragraph("Sello digital del SAT").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tablaSellos.addCell(new Cell(1, 5).add(
                                new Paragraph(COMPROBANTE.getComplemento().TimbreFiscalDigital.getSelloSAT())
                                                .addStyle(estiloValor))
                                .addStyle(estiloCelda));

                // Agregamos el codigo QR al documento
                StringBuilder codigoQR = new StringBuilder();
                codigoQR.append("https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx?"); // La URL del
                                                                                                     // acceso al
                                                                                                     // servicio que
                                                                                                     // pueda
                                                                                                     // mostrar los
                                                                                                     // datos de la
                                                                                                     // versión pública
                                                                                                     // del
                                                                                                     // COMPROBANTE.
                codigoQR.append("&id=" + COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID()); // UUID del
                                                                                                      // COMPROBANTE
                codigoQR.append("&re=" + COMPROBANTE.getEmisor().getRfc()); // RFC del Emisor
                codigoQR.append("&rr=" + COMPROBANTE.getReceptor().getRfc()); // RFC del receptor
                codigoQR.append("&tt=" + String.format("%.2f", COMPROBANTE.getTotal())); // Total del COMPROBANTE 10
                                                                                         // enteros y 6
                                                                                         // decimales
                codigoQR.append("&fe=" + COMPROBANTE.getSello().substring(COMPROBANTE.getSello().length() - 8)); // Total
                                                                                                                 // del
                                                                                                                 // COMPROBANTE
                                                                                                                 // 10
                                                                                                                 // enteros
                                                                                                                 // y
                                                                                                                 // 6
                                                                                                                 // decimales
                BarcodeQRCode pdfCodigoQR = new BarcodeQRCode(codigoQR.toString(), null);
                Image img = new Image(pdfCodigoQR.createFormXObject(ColorConstants.BLACK, 2, DOCUMENTO_PDF));
                img.scaleAbsolute(Utilidades.cmToFloat(3.3f), Utilidades.cmToFloat(3.3f));
                img.setHorizontalAlignment(HorizontalAlignment.LEFT);

                StringBuilder cadenaOriginal = new StringBuilder();
                cadenaOriginal.append("||");
                cadenaOriginal.append("1.0|");
                cadenaOriginal.append(COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID() + "|");
                cadenaOriginal.append(COMPROBANTE.getComplemento().TimbreFiscalDigital.getFechaTimbrado()
                                .format(DateTimeFormatter.ISO_DATE_TIME) + "|");
                cadenaOriginal.append(COMPROBANTE.getSello() + "|");
                cadenaOriginal.append(COMPROBANTE.getComplemento().TimbreFiscalDigital.getNoCertificadoSAT() + "||");

                tablaSellos.addCell(new Cell(4, 1).add(img).addStyle(estiloCelda));
                tablaSellos.addCell(
                                new Cell(1, 4).add(new Paragraph(
                                                "Cadena original del complemento de certificacion digital del SAT:")
                                                .addStyle(estiloAtributo)).addStyle(estiloCelda));
                tablaSellos.addCell(new Cell(1, 4).add(new Paragraph(cadenaOriginal.toString()).addStyle(estiloValor))
                                .addStyle(estiloCelda));

                tablaSellos
                                .addCell(new Cell()
                                                .add(new Paragraph("RFC del proveedor de certificación:")
                                                                .addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaSellos.addCell(
                                new Cell().add(new Paragraph(
                                                COMPROBANTE.getComplemento().TimbreFiscalDigital.getRfcProvCertif())
                                                .addStyle(estiloValor)).addStyle(estiloCelda));
                tablaSellos.addCell(
                                new Cell().add(new Paragraph("Fecha y hora de certificación:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaSellos.addCell(new Cell()
                                .add(new Paragraph(Utilidades.ToString(
                                                COMPROBANTE.getComplemento().TimbreFiscalDigital.getFechaTimbrado()))
                                                .addStyle(estiloValor))
                                .addStyle(estiloCelda));
                tablaSellos.addCell(new Cell()
                                .add(new Paragraph("No. de serie del certificado SAT").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tablaSellos.addCell(
                                new Cell().add(new Paragraph(
                                                COMPROBANTE.getComplemento().TimbreFiscalDigital.getNoCertificadoSAT())
                                                .addStyle(estiloValor)).addStyle(estiloCelda));
                tablaSellos.startNewRow();

                DOCUMENTO.add(tablaSellos);

        }

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Formato Carta Porte 10">
        private void AgregarDetalleCartaPorte10SAT() {
                Style estiloCelda = new Style();
                estiloCelda.setBorder(Border.NO_BORDER);
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setFont(bold);
                // estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 2.8f, 5.7F, 4.6F, 5.4F });
                Table tablaEncabezado = new Table(anchoColumnas);

                tablaEncabezado.addCell(
                                new Cell().add(new Paragraph("Transporte internacional:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaEncabezado.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getComplemento().CartaPorte10.getTranspInternac())
                                                .addStyle(estiloValor))
                                .addStyle(estiloCelda));
                if (!COMPROBANTE.getComplemento().CartaPorte10.getViaEntradaSalida().isEmpty()) {
                        tablaEncabezado.addCell(
                                        new Cell().add(new Paragraph("Vía Entrada/Salida:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaEncabezado.addCell(new Cell().add(new Paragraph(
                                        Catalogos.ObtenerClaveTransporte(COMPROBANTE.getComplemento().CartaPorte10
                                                        .getViaEntradaSalida()))
                                        .addStyle(estiloValor)).addStyle(estiloCelda));
                }
                if (!COMPROBANTE.getComplemento().CartaPorte10.getEntradaSalidaMerc().isEmpty()) {
                        tablaEncabezado.addCell(new Cell()
                                        .add(new Paragraph("Entrada/Salida mercancia:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaEncabezado.addCell(
                                        new Cell().add(new Paragraph(COMPROBANTE.getComplemento().CartaPorte10
                                                        .getEntradaSalidaMerc())
                                                        .addStyle(estiloValor)).addStyle(estiloCelda));
                }

                if (COMPROBANTE.getComplemento().CartaPorte10.getTotalDistRec() > 0) {
                        tablaEncabezado.addCell(new Cell()
                                        .add(new Paragraph("Total distancia recorrida:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaEncabezado.addCell(new Cell().add(
                                        new Paragraph(String.format("%.2f",
                                                        COMPROBANTE.getComplemento().CartaPorte10.getTotalDistRec()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                tablaEncabezado.startNewRow();
                DOCUMENTO.add(tablaEncabezado);
        }

        private void AgregarUbicaciones10SAT() {
                Style estiloTitle = new Style();
                estiloTitle.setFontSize(10);
                // estiloTitle.setBold();
                estiloTitle.setFont(bold);

                Style estiloCelda = new Style();
                estiloCelda.setBorder(Border.NO_BORDER);
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                SolidLine line = new SolidLine(.8f);
                line.setColor(ColorConstants.BLACK);
                LineSeparator ls = new LineSeparator(line);
                ls.setMarginTop(15);
                ls.setMarginBottom(5);
                ls.setWidth(Utilidades.cmToFloat(19));

                if (COMPROBANTE.getComplemento().CartaPorte10 == null
                                || COMPROBANTE.getComplemento().CartaPorte10.getUbicaciones() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte10.CartaPorte10 cartaporte = COMPROBANTE.getComplemento().CartaPorte10;
                DOCUMENTO.add(new Paragraph("Ubicaciones").addStyle(estiloTitle));

                for (int i = 0; i < cartaporte.getUbicaciones().getUbicacion().size(); i++) {
                        float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 6f, 3.5f, 6f });
                        Table tablaUbicaciones = new Table(anchoColumnas);
                        anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 6f, 3.5f, 6f });
                        Table tablaOrigen = new Table(anchoColumnas);
                        anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 6f, 3.5f, 6f });
                        Table tablaDestino = new Table(anchoColumnas);
                        anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 6f, 3.5f, 6f });
                        Table tablaDomicilio = new Table(anchoColumnas);

                        com.itextpdf.xmltopdf.CartaPorte10.Ubicacion ubicacion = cartaporte.getUbicaciones()
                                        .getUbicacion().get(i);
                        DOCUMENTO.add(new Paragraph("Ubicacion " + (i + 1)).addStyle(estiloTitle));
                        tablaUbicaciones.addCell(
                                        new Cell().add(new Paragraph("Tipo de estación:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaUbicaciones.addCell(new Cell()
                                        .add(new Paragraph(Catalogos.ObtenerTipoEstacion(ubicacion.getTipoEstacion()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                        tablaUbicaciones.addCell(
                                        new Cell().add(new Paragraph("Distancia recorrida:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaUbicaciones.addCell(new Cell()
                                        .add(new Paragraph(String.format("%.2f", ubicacion.getDistanciaRecorrida()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                        if (!ubicacion.getTipoEstacion().isEmpty() || ubicacion.getDistanciaRecorrida() > 0) {
                                DOCUMENTO.add(tablaUbicaciones);
                        }

                        if (ubicacion.getOrigen() != null) {
                                com.itextpdf.xmltopdf.CartaPorte10.Origen origen = ubicacion.getOrigen();
                                DOCUMENTO.add(new Paragraph("Origen").addStyle(estiloTitle));
                                if (!origen.getIDOrigen().isEmpty()) {
                                        tablaOrigen.addCell(
                                                        new Cell().add(new Paragraph("ID origen:")
                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph(origen.getIDOrigen()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!origen.getRFCRemitente().isEmpty()) {
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph("RFC remitente:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph(origen.getRFCRemitente())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!origen.getNombreRemitente().isEmpty()) {
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph("Nombre remitente:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph(origen.getNombreRemitente())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!origen.getNumRegIdTrib().isEmpty()) {
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph("Núm. Reg. Id. Trib:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph(origen.getNumRegIdTrib())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!origen.getResidenciaFiscal().isEmpty()) {
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph("Residencia fiscal:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaOrigen
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(origen.getResidenciaFiscal())
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!origen.getNumEstacion().isEmpty()) {
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph("No. de estación:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph(origen.getNumEstacion())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!origen.getNombreEstacion().isEmpty()) {
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph("Nombre de estación:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph(origen.getNombreEstacion())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!origen.getNavegacionTrafico().isEmpty()) {
                                        tablaOrigen.addCell(new Cell()
                                                        .add(new Paragraph("Navegación trafico:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaOrigen
                                                        .addCell(new Cell().add(
                                                                        new Paragraph(origen.getNavegacionTrafico())
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                tablaOrigen.addCell(new Cell()
                                                .add(new Paragraph("Fecha hora salida:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tablaOrigen.addCell(
                                                new Cell().add(new Paragraph(
                                                                Utilidades.ToString(origen.getFechaHoraSalida()))
                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));

                                tablaOrigen.startNewRow();

                                DOCUMENTO.add(tablaOrigen);
                        }
                        if (ubicacion.getDestino() != null) {
                                com.itextpdf.xmltopdf.CartaPorte10.Destino destino = ubicacion.getDestino();
                                DOCUMENTO.add(new Paragraph("Destino").addStyle(estiloTitle));
                                if (!destino.getIDDestino().isEmpty()) {
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph("ID destino:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph(destino.getIDDestino())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!destino.getRFCDestinatario().isEmpty()) {
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph("RFC destinatario:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDestino
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(destino.getRFCDestinatario())
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!destino.getNombreDestinatario().isEmpty()) {
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph("Nombre destinatario:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDestino.addCell(
                                                        new Cell().add(new Paragraph(destino.getNombreDestinatario())
                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!destino.getNumRegIdTrib().isEmpty()) {
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph("Núm. Reg. Id. Trib:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph(destino.getNumRegIdTrib())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!destino.getResidenciaFiscal().isEmpty()) {
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph("Residencia fiscal:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDestino
                                                        .addCell(new Cell().add(
                                                                        new Paragraph(destino.getResidenciaFiscal())
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!destino.getNumEstacion().isEmpty()) {
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph("No. de estación:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph(destino.getNumEstacion())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!destino.getNombreEstacion().isEmpty()) {
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph("Nombre de estación:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDestino
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(destino.getNombreEstacion())
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!destino.getNavegacionTrafico().isEmpty()) {
                                        tablaDestino.addCell(new Cell()
                                                        .add(new Paragraph("Navegación trafico:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDestino
                                                        .addCell(new Cell().add(
                                                                        new Paragraph(destino.getNavegacionTrafico())
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                tablaDestino.addCell(new Cell()
                                                .add(new Paragraph("Fecha y hora de llegada:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tablaDestino.addCell(
                                                new Cell().add(new Paragraph(
                                                                Utilidades.ToString(destino.getFechaHoraProgLlegada()))
                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));

                                tablaDestino.startNewRow();

                                DOCUMENTO.add(tablaDestino);
                        }
                        if (ubicacion.getDomicilio() != null) {
                                com.itextpdf.xmltopdf.CartaPorte10.Domicilio domicilio = ubicacion.getDomicilio();
                                DOCUMENTO.add(new Paragraph("Domicilio").addStyle(estiloTitle));
                                if (!domicilio.getCalle().isEmpty()) {
                                        tablaDomicilio.addCell(
                                                        new Cell().add(new Paragraph("Calle:").addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getCalle()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getNumeroExterior().isEmpty()) {
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph("Núm. exterior:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilio
                                                        .addCell(new Cell().add(
                                                                        new Paragraph(domicilio.getNumeroExterior())
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getNumeroInterior().isEmpty()) {
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph("Núm. interior:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilio
                                                        .addCell(new Cell().add(
                                                                        new Paragraph(domicilio.getNumeroExterior())
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getColonia().isEmpty()) {
                                        tablaDomicilio.addCell(
                                                        new Cell().add(new Paragraph("Colonia:")
                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getColonia())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getLocalidad().isEmpty()) {
                                        tablaDomicilio.addCell(
                                                        new Cell().add(new Paragraph("Localidad:")
                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getLocalidad())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getReferencia().isEmpty()) {
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph("Referencia:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getReferencia())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getMunicipio().isEmpty()) {
                                        tablaDomicilio.addCell(
                                                        new Cell().add(new Paragraph("Municipio:")
                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getMunicipio())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getEstado().isEmpty()) {
                                        tablaDomicilio.addCell(
                                                        new Cell().add(new Paragraph("Estado:")
                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getEstado()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                tablaDomicilio.addCell(
                                                new Cell().add(new Paragraph("Código postal:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tablaDomicilio.addCell(new Cell()
                                                .add(new Paragraph(domicilio.getCodigoPostal()).addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                                if (!domicilio.getPais().isEmpty()) {
                                        tablaDomicilio.addCell(
                                                        new Cell().add(new Paragraph("Pais:").addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilio.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getPais()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                tablaDomicilio.startNewRow();

                                DOCUMENTO.add(tablaDomicilio);
                        }
                }
                DOCUMENTO.add(ls);
        }

        private void AgregarMercancias10SAT() {

                Style estiloSubtitle = new Style();
                estiloSubtitle.setFontSize(10);
                // estiloSubtitle.setBold();
                estiloSubtitle.setFont(bold);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(6);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setVerticalAlignment(VerticalAlignment.MIDDLE);
                // estiloTitle.setBold();
                estiloTitle.setFont(bold);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(6);
                estiloAtributo.setTextAlignment(TextAlignment.CENTER);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setTextAlignment(TextAlignment.CENTER);
                estiloValor.setFontSize(6);
                estiloValor.setFont(regular);

                Style estiloValorDescripcion = new Style();
                estiloValorDescripcion.setTextAlignment(TextAlignment.LEFT);
                estiloValorDescripcion.setFontSize(6);
                estiloValorDescripcion.setFont(regular);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setVerticalAlignment(VerticalAlignment.MIDDLE);
                estiloCeldaTitle.setBackgroundColor(new DeviceRgb(191, 191, 191));

                Style estiloCeldaValor = new Style();
                estiloCeldaValor.setPaddingTop(0);
                estiloCeldaValor.setPaddingBottom(0);

                Style estiloCeldaImpuesto = new Style();
                estiloCeldaImpuesto.setPadding(0);
                estiloCeldaImpuesto.setBackgroundColor(new DeviceRgb(191, 191, 191));

                SolidLine line = new SolidLine(.8f);
                line.setColor(ColorConstants.BLACK);
                LineSeparator ls = new LineSeparator(line);
                ls.setMarginTop(15);
                ls.setMarginBottom(5);
                ls.setWidth(Utilidades.cmToFloat(19));

                DOCUMENTO.add(new Paragraph("Mercancias").addStyle(estiloSubtitle));

                float[] anchoColumnas = Utilidades.cmToFloat(
                                new float[] { 1.3f, 1.3f, 1.3f, 1.7f, 1.4f, 1.3f, 1.3f, 1.9f, 1.4f, 1.4f, 1.5f, 1.5f,
                                                1.5f });
                Table tablaMercancias = new Table(anchoColumnas);
                tablaMercancias.addCell(
                                new Cell().add(new Paragraph("Clave\n bienes\ntrasportados").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias
                                .addCell(new Cell().add(new Paragraph("Clave STCC").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias
                                .addCell(new Cell().add(new Paragraph("Cantidad").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias
                                .addCell(new Cell().add(new Paragraph("Unidad").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias
                                .addCell(new Cell().add(new Paragraph("Dimensiones").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias.addCell(
                                new Cell().add(new Paragraph("Mercania \nPeligrosa").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias.addCell(
                                new Cell().add(new Paragraph("Clave Mercancia\nPeligrosa").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias.addCell(
                                new Cell().add(new Paragraph("Descripcion embalaje").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias
                                .addCell(new Cell().add(new Paragraph("Peso en Kg.").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias.addCell(
                                new Cell().add(new Paragraph("Valor mercancia").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias
                                .addCell(new Cell().add(new Paragraph("Moneda").addStyle(estiloTitle))
                                                .addStyle(estiloCeldaTitle));
                tablaMercancias.addCell(new Cell().add(new Paragraph("Fraccion\nArancelaria").addStyle(estiloTitle))
                                .addStyle(estiloCeldaTitle));
                tablaMercancias.addCell(new Cell().add(new Paragraph("UUID\nComercio\nExterior").addStyle(estiloTitle))
                                .addStyle(estiloCeldaTitle));

                for (int i = 0; i < COMPROBANTE.getComplemento().CartaPorte10.getMercancias().getMercancia()
                                .size(); i++) {
                        com.itextpdf.xmltopdf.CartaPorte10.Mercancia m = COMPROBANTE.getComplemento().CartaPorte10
                                        .getMercancias()
                                        .getMercancia().get(i);
                        tablaMercancias.addCell(new Cell().add(new Paragraph(m.getBienesTransp()).addStyle(estiloValor))
                                        .addStyle(estiloCeldaValor));
                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph(m.getClaveSTCC()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias
                                        .addCell(new Cell()
                                                        .add(new Paragraph(String.format("%.6f", m.getCantidad()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph(m.getUnidad()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph(m.getDimensiones()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph(m.getMaterialPeligroso()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph(m.getCveMaterialPeligroso()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph(m.getDescripEmbalaje()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias
                                        .addCell(new Cell()
                                                        .add(new Paragraph(String.format("%.2f", m.getPesoEnKg()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias
                                        .addCell(new Cell()
                                                        .add(new Paragraph(Utilidades.convertir(m.getValorMercancia()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph(m.getMoneda()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph(m.getFraccionArancelaria()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));
                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph(m.getUUIDComercioExt()).addStyle(estiloValor))
                                                        .addStyle(estiloCeldaValor));

                        tablaMercancias.addCell(
                                        new Cell().add(new Paragraph("Descripción").addStyle(estiloTitle))
                                                        .addStyle(estiloCeldaTitle));
                        tablaMercancias
                                        .addCell(new Cell(1, 6)
                                                        .add(new Paragraph(m.getDescripcion())
                                                                        .addStyle(estiloValorDescripcion))
                                                        .addStyle(estiloCeldaValor));
                        if (m.getCantidadTransporta().size() > 0) {
                                anchoColumnas = Utilidades.cmToFloat(new float[] { 1.5f, 1f, 2f, 1.2f, 1.3f, 2.2f });
                                Table tablaCantidadTransporta = new Table(anchoColumnas);
                                tablaCantidadTransporta.addCell(
                                                new Cell().add(new Paragraph("Cantidad").addStyle(estiloTitle))
                                                                .addStyle(estiloCeldaTitle));
                                tablaCantidadTransporta.addCell(
                                                new Cell().add(new Paragraph("ID Origen").addStyle(estiloTitle))
                                                                .addStyle(estiloCeldaTitle));
                                tablaCantidadTransporta.addCell(
                                                new Cell().add(new Paragraph("ID Destino").addStyle(estiloTitle))
                                                                .addStyle(estiloCeldaTitle));
                                tablaCantidadTransporta.addCell(
                                                new Cell().add(new Paragraph("Clave").addStyle(estiloTitle))
                                                                .addStyle(estiloCeldaTitle));
                                for (int j = 0; j < m.getCantidadTransporta().size(); j++) {
                                        com.itextpdf.xmltopdf.CartaPorte10.CantidadTransporta cantidadTransporta = m
                                                        .getCantidadTransporta()
                                                        .get(j);
                                        tablaCantidadTransporta.addCell(
                                                        new Cell().add(new Paragraph(String.format("%.2f",
                                                                        cantidadTransporta.getCantidad()))
                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCeldaValor));
                                        tablaCantidadTransporta.addCell(
                                                        new Cell().add(new Paragraph(cantidadTransporta.getIDOrigen())
                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCeldaValor));
                                        tablaCantidadTransporta.addCell(
                                                        new Cell().add(new Paragraph(cantidadTransporta.getIDDestino())
                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCeldaValor));
                                        tablaCantidadTransporta.addCell(
                                                        new Cell().add(new Paragraph(
                                                                        cantidadTransporta.getCvesTransporte())
                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCeldaValor));

                                }
                                tablaMercancias.addCell(new Cell(1, 6).add(tablaCantidadTransporta));
                        } else {
                                tablaMercancias.addCell(
                                                new Cell(1, 6).add(new Paragraph("").addStyle(estiloValorDescripcion))
                                                                .addStyle(estiloCeldaValor));
                        }

                        if (m.getDetalleMercancia() != null) {
                                anchoColumnas = Utilidades.cmToFloat(new float[] { 1.5f, 1f, 2f, 1.2f, 1.3f, 2.2f });
                                Table tablaImpuestos = new Table(anchoColumnas);
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Unidad peso").addStyle(estiloTitle))
                                                                .addStyle(estiloCeldaTitle));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Peso bruto").addStyle(estiloTitle))
                                                                .addStyle(estiloCeldaTitle));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Pero neto").addStyle(estiloTitle))
                                                                .addStyle(estiloCeldaTitle));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Pero tara").addStyle(estiloTitle))
                                                                .addStyle(estiloCeldaTitle));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph("Num. piezas").addStyle(estiloTitle))
                                                                .addStyle(estiloCeldaTitle));

                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph(m.getDetalleMercancia().getUnidadPeso())
                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCeldaValor));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph(String.format("%.2f",
                                                                m.getDetalleMercancia().getPesoBruto()))
                                                                .addStyle(estiloValor)).addStyle(estiloCeldaValor));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph(String.format("%.2f",
                                                                m.getDetalleMercancia().getPesoNeto()))
                                                                .addStyle(estiloValor)).addStyle(estiloCeldaValor));
                                tablaImpuestos.addCell(
                                                new Cell().add(new Paragraph(String.format("%.2f",
                                                                m.getDetalleMercancia().getPesoTara()))
                                                                .addStyle(estiloValor)).addStyle(estiloCeldaValor));
                                tablaImpuestos.addCell(new Cell().add(
                                                new Paragraph(String.valueOf(m.getDetalleMercancia().getNumPiezas()))
                                                                .addStyle(estiloValor))
                                                .addStyle(estiloCeldaValor));
                                tablaMercancias.addCell(new Cell(1, 13).add(tablaImpuestos));
                        } else {
                                tablaMercancias.addCell(
                                                new Cell(1, 13).add(new Paragraph("").addStyle(estiloValorDescripcion))
                                                                .addStyle(estiloCeldaValor));
                        }
                }
                DOCUMENTO.add(tablaMercancias);
                AgregarAutoTransporteFederal10SAT();
                AgregarTransporteMaritimo10SAT();
                AgregarTransporteAereo10SAT();
                AgregarTransporteFerroviario10SAT();
        }

        private void AgregarAutoTransporteFederal10SAT() {
                Style estiloTitulo = new Style();
                estiloTitulo.setFontSize(10);
                estiloTitulo.setFont(bold);
                // estiloTitulo.setBold();

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloCeldaNormal = new Style();
                estiloCeldaNormal.setPaddingTop(0);
                estiloCeldaNormal.setPaddingBottom(0);

                SolidLine line = new SolidLine(.8f);
                line.setColor(ColorConstants.BLACK);
                LineSeparator ls = new LineSeparator(line);
                ls.setMarginTop(15);
                ls.setMarginBottom(5);
                ls.setWidth(Utilidades.cmToFloat(19));

                if (COMPROBANTE.getComplemento().CartaPorte10.getMercancias().getAutotransporteFederal() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte10.AutotransporteFederal autoTransporte = COMPROBANTE
                                .getComplemento().CartaPorte10.getMercancias().getAutotransporteFederal();
                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 3.6f, 5.9f, 3.6f, 5.9f });
                Table tablaAutoTransporteFederal = new Table(anchoColumnas);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 6f, 1.5f });
                Table tablaRemolque = new Table(anchoColumnas);

                tablaAutoTransporteFederal
                                .addCell(new Cell().add(new Paragraph("Permiso SCT:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaAutoTransporteFederal.addCell(
                                new Cell().add(new Paragraph(autoTransporte.getPermSCT()).addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                tablaAutoTransporteFederal.addCell(
                                new Cell().add(new Paragraph("Núm. del permiso SCT:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaAutoTransporteFederal.addCell(new Cell()
                                .add(new Paragraph(autoTransporte.getNumPermisoSCT()).addStyle(estiloValor))
                                .addStyle(estiloCelda));
                tablaAutoTransporteFederal.addCell(new Cell()
                                .add(new Paragraph("Nombre de la aseguradora:").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tablaAutoTransporteFederal.addCell(new Cell()
                                .add(new Paragraph(autoTransporte.getNombreAseg()).addStyle(estiloValor))
                                .addStyle(estiloCelda));
                tablaAutoTransporteFederal.addCell(
                                new Cell().add(new Paragraph("Núm. de poliza seguro:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaAutoTransporteFederal.addCell(new Cell()
                                .add(new Paragraph(autoTransporte.getNumPolizaSeguro()).addStyle(estiloValor))
                                .addStyle(estiloCelda));

                if (autoTransporte.getIdentificacionVehicular() != null) {
                        com.itextpdf.xmltopdf.CartaPorte10.IdentificacionVehicular identificacionVehicular = autoTransporte
                                        .getIdentificacionVehicular();
                        tablaAutoTransporteFederal.addCell(
                                        new Cell().add(new Paragraph("Placa vehicular:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaAutoTransporteFederal
                                        .addCell(new Cell()
                                                        .add(new Paragraph(identificacionVehicular.getPlacaVM())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                        tablaAutoTransporteFederal.addCell(
                                        new Cell().add(new Paragraph("Año modelo:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaAutoTransporteFederal.addCell(new Cell().add(
                                        new Paragraph(Integer.toString(identificacionVehicular.getAnioModeloVM()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                        tablaAutoTransporteFederal.addCell(new Cell()
                                        .add(new Paragraph("Config. del autotransporte:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaAutoTransporteFederal.addCell(new Cell()
                                        .add(new Paragraph(Catalogos.ObtenerConfigVehicular(
                                                        identificacionVehicular.getConfigVehicular()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda).setVerticalAlignment(VerticalAlignment.MIDDLE));
                }
                tablaAutoTransporteFederal.startNewRow();

                if (autoTransporte.getRemolques() != null && autoTransporte.getRemolques().getRemolque().size() > 0) {
                        tablaRemolque.addCell(new Cell()
                                        .add(new Paragraph("Subtipo de remolque").addStyle(estiloAtributo)
                                                        .setTextAlignment(TextAlignment.CENTER))
                                        .addStyle(estiloCeldaNormal));
                        tablaRemolque.addCell(new Cell()
                                        .add(new Paragraph("Placa").addStyle(estiloAtributo)
                                                        .setTextAlignment(TextAlignment.CENTER))
                                        .addStyle(estiloCeldaNormal));
                        for (int j = 0; j < autoTransporte.getRemolques().getRemolque().size(); j++) {
                                com.itextpdf.xmltopdf.CartaPorte10.Remolque remolque = autoTransporte.getRemolques()
                                                .getRemolque()
                                                .get(j);
                                tablaRemolque
                                                .addCell(
                                                                new Cell()
                                                                                .add(new Paragraph(Catalogos
                                                                                                .ObtenerSubTipoRem(
                                                                                                                remolque.getSubTipoRem()))
                                                                                                .addStyle(estiloValor)
                                                                                                .setTextAlignment(
                                                                                                                TextAlignment.CENTER))
                                                                                .addStyle(estiloCeldaNormal));
                                tablaRemolque.addCell(new Cell().add(
                                                new Paragraph(remolque.getPlaca()).addStyle(estiloValor)
                                                                .setTextAlignment(TextAlignment.CENTER))
                                                .addStyle(estiloCeldaNormal));
                        }
                }
                DOCUMENTO.add(new Paragraph("Autotransporte federal").addStyle(estiloTitulo));
                DOCUMENTO.add(tablaAutoTransporteFederal);
                if (tablaRemolque.getNumberOfRows() > 0) {
                        DOCUMENTO.add(new Paragraph("Remolques").addStyle(estiloTitulo));
                        DOCUMENTO.add(tablaRemolque);
                }
                DOCUMENTO.add(ls);
        }

        private void AgregarTransporteMaritimo10SAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setMarginTop(15);
                estiloSubtitle.setMarginLeft(0);
                estiloSubtitle.setFontSize(10);
                // estiloSubtitle.setBold();
                estiloSubtitle.setFont(bold);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(6);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setVerticalAlignment(VerticalAlignment.MIDDLE);
                // estiloTitle.setBold();
                estiloTitle.setFont(bold);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloValorDescripcion = new Style();
                estiloValorDescripcion.setTextAlignment(TextAlignment.LEFT);
                estiloValorDescripcion.setFontSize(6);
                estiloValorDescripcion.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloCeldaValor = new Style();
                estiloCeldaValor.setPaddingTop(0);
                estiloCeldaValor.setPaddingBottom(0);

                Style estiloCeldaImpuesto = new Style();
                estiloCeldaImpuesto.setPadding(0);
                estiloCeldaImpuesto.setBackgroundColor(new DeviceRgb(191, 191, 191));

                SolidLine line = new SolidLine(.8f);
                line.setColor(ColorConstants.BLACK);
                LineSeparator ls = new LineSeparator(line);
                ls.setMarginTop(15);
                ls.setMarginBottom(5);
                ls.setWidth(Utilidades.cmToFloat(19));
                if (COMPROBANTE.getComplemento().CartaPorte10.getMercancias().getTransporteMaritimo() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte10.TransporteMaritimo transporteMaritimo = COMPROBANTE
                                .getComplemento().CartaPorte10.getMercancias().getTransporteMaritimo();

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.5f, 6f, 4.5f, 6f });
                Table tablaTransporteMaritimo = new Table(anchoColumnas);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 6.7f, 3.5f });
                Table tablaContenedor = new Table(anchoColumnas);

                if (!transporteMaritimo.getPermSCT().isEmpty()) {
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Permiso SCT:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(
                                                        Catalogos.ObtenerTipoPermiso(transporteMaritimo.getPermSCT()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumPermisoSCT().isEmpty()) {
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Núm. permiso SCT:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteMaritimo.getNumPermisoSCT())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNombreAseg().isEmpty()) {
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Nombre aseguradora:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteMaritimo.getNombreAseg())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumPolizaSeguro().isEmpty()) {
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Núm. poliza seguro:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNumPolizaSeguro())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getTipoEmbarcacion().isEmpty()) {
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Tipo embarcación:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell().add(
                                        new Paragraph(Catalogos
                                                        .ObtenerConfigMaritima(transporteMaritimo.getTipoEmbarcacion()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getMatricula().isEmpty()) {
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Matricula:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(transporteMaritimo.getMatricula()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumeroOMI().isEmpty()) {
                        tablaTransporteMaritimo
                                        .addCell(new Cell().add(new Paragraph("Núm. OMI:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(transporteMaritimo.getNumeroOMI()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getAnioEmbarcacion() > 0) {
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Año embarcación:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(Integer.toString(transporteMaritimo.getAnioEmbarcacion()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNombreEmbarc().isEmpty()) {
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Nombre embarcación:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteMaritimo.getNombreEmbarc())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNacionalidadEmbarc().isEmpty()) {
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Nacionalidad embarcación:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNacionalidadEmbarc())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getUnidadesDeArqBruto() > 0) {
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Unidades de arqueo bruto :").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(String.format("%.2f",
                                                        transporteMaritimo.getUnidadesDeArqBruto()))
                                                        .addStyle(estiloValor)).addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getTipoCarga().isEmpty()) {
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Tipo de carga:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(
                                                        Catalogos.ObtenerTipoCarga(transporteMaritimo.getTipoCarga()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumCertITC().isEmpty()) {
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Núm. certificado ITC:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteMaritimo.getNumCertITC())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getEslora() > 0) {
                        tablaTransporteMaritimo
                                        .addCell(new Cell().add(new Paragraph("Eslora:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(String.format("%.2f", transporteMaritimo.getEslora()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getManga() > 0) {
                        tablaTransporteMaritimo
                                        .addCell(new Cell().add(new Paragraph("Manga:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(String.format("%.2f", transporteMaritimo.getManga()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getCalado() > 0) {
                        tablaTransporteMaritimo
                                        .addCell(new Cell().add(new Paragraph("Calado:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(String.format("%.2f", transporteMaritimo.getCalado()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getLineaNaviera().isEmpty()) {
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Linea naviera:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteMaritimo.getLineaNaviera())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNombreAgenteNaviero().isEmpty()) {
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Nombre agente naviero:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNombreAgenteNaviero())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumAutorizacionNaviero().isEmpty()) {
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Núm. autorización naviero:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNumAutorizacionNaviero())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumViaje().isEmpty()) {
                        tablaTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Núm. viaje:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(transporteMaritimo.getNumViaje()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumConocEmbarc().isEmpty()) {
                        tablaTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Núm. conocimiento embarque:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteMaritimo.getNumConocEmbarc())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                tablaTransporteMaritimo.startNewRow();

                if (transporteMaritimo.getContenedor() != null && transporteMaritimo.getContenedor().size() > 0) {
                        tablaContenedor.addCell(
                                        new Cell().add(new Paragraph("Matricula contenedor").addStyle(estiloAtributo)));
                        tablaContenedor.addCell(
                                        new Cell().add(new Paragraph("Tipo contenedor").addStyle(estiloAtributo)));
                        tablaContenedor.addCell(
                                        new Cell().add(new Paragraph("Núm. precinto").addStyle(estiloAtributo)));
                        for (int i = 0; i < transporteMaritimo.getContenedor().size(); i++) {
                                com.itextpdf.xmltopdf.CartaPorte10.Contenedor contenedor = transporteMaritimo
                                                .getContenedor().get(i);
                                tablaContenedor.addCell(
                                                new Cell().add(new Paragraph(contenedor.getMatriculaContenedor())
                                                                .addStyle(estiloValor)));
                                tablaContenedor
                                                .addCell(new Cell()
                                                                .add(new Paragraph(Catalogos.ObtenerContenedorMaritimo(
                                                                                contenedor.getTipoContenedor()))
                                                                                .addStyle(estiloValor)));
                                tablaContenedor
                                                .addCell(new Cell().add(new Paragraph(contenedor.getNumPrecinto())
                                                                .addStyle(estiloValor)));
                        }
                }
                DOCUMENTO.add(new Paragraph("Transporte maritimo").addStyle(estiloSubtitle));
                DOCUMENTO.add(tablaTransporteMaritimo);
                if (tablaContenedor.getNumberOfRows() > 0) {
                        DOCUMENTO.add(new Paragraph("Contenedor").addStyle(estiloSubtitle));
                        DOCUMENTO.add(tablaContenedor);
                }
                DOCUMENTO.add(ls);
        }

        private void AgregarTransporteAereo10SAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setMarginTop(15);
                estiloSubtitle.setMarginLeft(0);
                estiloSubtitle.setFontSize(10);
                // estiloSubtitle.setBold();
                estiloSubtitle.setFont(bold);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(6);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setVerticalAlignment(VerticalAlignment.MIDDLE);
                // estiloTitle.setBold();
                estiloTitle.setFont(bold);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloValorDescripcion = new Style();
                estiloValorDescripcion.setTextAlignment(TextAlignment.LEFT);
                estiloValorDescripcion.setFontSize(6);
                estiloValorDescripcion.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloCeldaValor = new Style();
                estiloCeldaValor.setPaddingTop(0);
                estiloCeldaValor.setPaddingBottom(0);

                Style estiloCeldaImpuesto = new Style();
                estiloCeldaImpuesto.setPadding(0);
                estiloCeldaImpuesto.setBackgroundColor(new DeviceRgb(191, 191, 191));

                SolidLine line = new SolidLine(.8f);
                line.setColor(ColorConstants.BLACK);
                LineSeparator ls = new LineSeparator(line);
                ls.setMarginTop(15);
                ls.setMarginBottom(5);
                ls.setWidth(Utilidades.cmToFloat(19));
                if (COMPROBANTE.getComplemento().CartaPorte10.getMercancias().getTransporteAereo() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte10.TransporteAereo transporteAereo = COMPROBANTE
                                .getComplemento().CartaPorte10
                                .getMercancias().getTransporteAereo();

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.5f, 6f, 4.5f, 6f });
                Table tablaTransporteAereo = new Table(anchoColumnas);

                if (!transporteAereo.getPermSCT().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Permiso SCT:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph(Catalogos.ObtenerTipoPermiso(transporteAereo.getPermSCT()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNumPermisoSCT().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Núm. permiso SCT:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getNumPermisoSCT())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getMatriculaAeronave().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Matricula aeronave:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getMatriculaAeronave())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNombreAseg().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Nombre aseguradora:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph(transporteAereo.getNombreAseg()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNumPolizaSeguro().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Núm poliza seguro:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getNumPolizaSeguro())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNumeroGuia().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Núm de guía:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph(transporteAereo.getNumeroGuia()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getLugarContrato().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Lugar de contrato:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getLugarContrato())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getRFCTransportista().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("RFC transportista:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getRFCTransportista())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getCodigoTransportista().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Código transportista:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getCodigoTransportista())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNumRegIdTribTranspor().isEmpty()) {
                        tablaTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph("NúmRegIDTrib transportista:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getNumRegIdTribTranspor())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getResidenciaFiscalTranspor().isEmpty()) {
                        tablaTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Residencia fiscal transportista:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getResidenciaFiscalTranspor())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNombreTransportista().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Nombre transportista:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getNombreTransportista())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getRFCEmbarcador().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("RFC Embarcador:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getRFCEmbarcador())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNumRegIdTribEmbarc().isEmpty()) {
                        tablaTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph("NúmRegIDTrib Embarcador:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getNumRegIdTribEmbarc())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getResidenciaFiscalEmbarc().isEmpty()) {
                        tablaTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Residencia fiscal embarcador:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getResidenciaFiscalEmbarc())
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNombreEmbarcador().isEmpty()) {
                        tablaTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Nombre embarcador:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getNombreEmbarcador())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                tablaTransporteAereo.startNewRow();

                DOCUMENTO.add(new Paragraph("Transporte aereo").addStyle(estiloSubtitle));
                DOCUMENTO.add(tablaTransporteAereo);
                DOCUMENTO.add(ls);
        }

        private void AgregarTransporteFerroviario10SAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setMarginTop(15);
                estiloSubtitle.setMarginLeft(0);
                estiloSubtitle.setFontSize(10);
                // estiloSubtitle.setBold();
                estiloSubtitle.setFont(bold);

                Style estiloDescAtributo = new Style();
                estiloDescAtributo.setFontSize(8);
                // estiloDescAtributo.setBold();
                estiloDescAtributo.setFont(bold);

                Style estiloDescValor = new Style();
                estiloDescValor.setFontSize(8);
                estiloDescValor.setFont(regular);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setTextAlignment(TextAlignment.CENTER);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setTextAlignment(TextAlignment.CENTER);
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                SolidLine line = new SolidLine(.8f);
                line.setColor(ColorConstants.BLACK);
                LineSeparator ls = new LineSeparator(line);
                ls.setMarginTop(15);
                ls.setMarginBottom(5);
                ls.setWidth(Utilidades.cmToFloat(19));
                if (COMPROBANTE.getComplemento().CartaPorte10.getMercancias().getTransporteFerroviario() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte10.TransporteFerroviario transporteFerroviario = COMPROBANTE
                                .getComplemento().CartaPorte10.getMercancias().getTransporteFerroviario();

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.4f, 6f, 4.5f, 6f });
                Table tablaTransporteFerroviario = new Table(anchoColumnas);

                anchoColumnas = Utilidades.cmToFloat(new float[] { 4.5f, 4f });
                Table tablaDerechosDePaso = new Table(anchoColumnas);

                anchoColumnas = Utilidades.cmToFloat(new float[] { 4f, 2.2f, 2.2f, 2.2f, 8.4f });
                Table tablaCarros = new Table(anchoColumnas);

                if (!transporteFerroviario.getTipoDeServicio().isEmpty()) {
                        tablaTransporteFerroviario.addCell(new Cell()
                                        .add(new Paragraph("Tipo de servicio:").addStyle(estiloDescAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteFerroviario.addCell(
                                        new Cell().add(new Paragraph(
                                                        Catalogos.ObtenerTipoServicio(
                                                                        transporteFerroviario.getTipoDeServicio()))
                                                        .addStyle(estiloDescValor)).addStyle(estiloCelda));
                }
                if (!transporteFerroviario.getNombreAseg().isEmpty()) {
                        tablaTransporteFerroviario.addCell(new Cell()
                                        .add(new Paragraph("Nombre aseguradora:").addStyle(estiloDescAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteFerroviario.addCell(
                                        new Cell().add(new Paragraph(transporteFerroviario.getNombreAseg())
                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteFerroviario.getNumPolizaSeguro().isEmpty()) {
                        tablaTransporteFerroviario.addCell(new Cell()
                                        .add(new Paragraph("Núm. poliza seguro:").addStyle(estiloDescAtributo))
                                        .addStyle(estiloCelda));
                        tablaTransporteFerroviario.addCell(
                                        new Cell().add(new Paragraph(transporteFerroviario.getNumPolizaSeguro())
                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteFerroviario.getConcesionario().isEmpty()) {
                        tablaTransporteFerroviario.addCell(
                                        new Cell().add(new Paragraph("Consecionario:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                        tablaTransporteFerroviario.addCell(
                                        new Cell().add(new Paragraph(transporteFerroviario.getNombreAseg())
                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                }
                tablaTransporteFerroviario.startNewRow();

                if (transporteFerroviario.getDerechosDePaso() != null
                                && transporteFerroviario.getDerechosDePaso().size() > 0) {
                        tablaDerechosDePaso.addCell(
                                        new Cell().add(new Paragraph("Tipo derecho de paso").addStyle(estiloAtributo)));
                        tablaDerechosDePaso.addCell(
                                        new Cell().add(new Paragraph("Kilometraje pagado").addStyle(estiloAtributo)));
                        for (int i = 0; i < transporteFerroviario.getDerechosDePaso().size(); i++) {
                                com.itextpdf.xmltopdf.CartaPorte10.DerechosDePaso derechosDePaso = transporteFerroviario
                                                .getDerechosDePaso().get(i);
                                tablaDerechosDePaso.addCell(
                                                new Cell().add(new Paragraph(derechosDePaso.getTipoDerechoDePaso())
                                                                .addStyle(estiloValor)));
                                tablaDerechosDePaso.addCell(
                                                new Cell().add(new Paragraph(String.format("%.2f",
                                                                derechosDePaso.getKilometrajePagado()))
                                                                .addStyle(estiloValor)));
                        }
                }
                if (transporteFerroviario.getCarro() != null && transporteFerroviario.getCarro().size() > 0) {
                        tablaCarros.addCell(new Cell().add(new Paragraph("Tipo de carro").addStyle(estiloAtributo)));
                        tablaCarros.addCell(new Cell().add(new Paragraph("Matricula carro").addStyle(estiloAtributo)));
                        tablaCarros.addCell(new Cell().add(new Paragraph("Guia carro").addStyle(estiloAtributo)));
                        tablaCarros.addCell(new Cell()
                                        .add(new Paragraph("Toneladas\nnetas carro").addStyle(estiloAtributo)));
                        tablaCarros.addCell(new Cell().add(new Paragraph("Contenedor").addStyle(estiloAtributo)));

                        for (int i = 0; i < transporteFerroviario.getCarro().size(); i++) {
                                com.itextpdf.xmltopdf.CartaPorte10.Carro carro = transporteFerroviario.getCarro()
                                                .get(i);
                                tablaCarros.addCell(
                                                new Cell().add(new Paragraph(
                                                                Catalogos.ObtenerTipoCarro(carro.getTipoCarro()))
                                                                .addStyle(estiloValor)));
                                tablaCarros.addCell(new Cell()
                                                .add(new Paragraph(carro.getMatriculaCarro()).addStyle(estiloValor)));
                                tablaCarros.addCell(new Cell()
                                                .add(new Paragraph(carro.getGuiaCarro()).addStyle(estiloValor)));
                                tablaCarros.addCell(new Cell().add(
                                                new Paragraph(String.format("%.2f", carro.getToneladasNetasCarro()))
                                                                .addStyle(estiloValor)));
                                if (carro.getContenedor() != null && carro.getContenedor().size() > 0) {
                                        anchoColumnas = Utilidades.cmToFloat(new float[] { 2.7f, 2.7f, 3.1f });
                                        Table tablaContenedor = new Table(anchoColumnas);
                                        tablaContenedor.addCell(new Cell()
                                                        .add(new Paragraph("Tipo de\n contenedor")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaContenedor
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph("Peso contenedor\n vacio")
                                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaContenedor
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph("Peso neto de\n mercancia")
                                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        for (int j = 0; j < carro.getContenedor().size(); j++) {
                                                com.itextpdf.xmltopdf.CartaPorte10.CarroContenedor contenedor = carro
                                                                .getContenedor().get(i);
                                                tablaContenedor.addCell(
                                                                new Cell().add(new Paragraph(
                                                                                Catalogos.ObtenerTipoContenedor(
                                                                                                contenedor.getTipoContenedor()))
                                                                                .addStyle(estiloValor))
                                                                                .setBorder(Border.NO_BORDER));
                                                tablaContenedor.addCell(
                                                                new Cell().add(new Paragraph(String.format("%.2f",
                                                                                contenedor.getPesoContenedorVacio()))
                                                                                .addStyle(estiloValor))
                                                                                .setBorder(Border.NO_BORDER));
                                                tablaContenedor.addCell(
                                                                new Cell().add(new Paragraph(String.format("%.2f",
                                                                                contenedor.getPesoNetoMercancia()))
                                                                                .addStyle(estiloValor))
                                                                                .setBorder(Border.NO_BORDER));
                                        }
                                        tablaCarros.addCell(new Cell().add(tablaContenedor));
                                } else {
                                        tablaCarros.addCell(new Cell().add(new Paragraph("").addStyle(estiloValor)));
                                }
                        }

                }

                DOCUMENTO.add(new Paragraph("Transporte ferroviario").addStyle(estiloSubtitle));
                DOCUMENTO.add(tablaTransporteFerroviario);
                if (tablaDerechosDePaso.getNumberOfRows() > 0) {
                        DOCUMENTO.add(new Paragraph("Derechos de paso").addStyle(estiloSubtitle));
                        DOCUMENTO.add(tablaDerechosDePaso);
                }
                if (tablaCarros.getNumberOfRows() > 0) {
                        DOCUMENTO.add(new Paragraph("Carros").addStyle(estiloSubtitle));
                        DOCUMENTO.add(tablaCarros);
                }

                DOCUMENTO.add(ls);
        }

        private void AgregarFiguraTransporte10SAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setMarginTop(15);
                estiloSubtitle.setMarginLeft(0);
                estiloSubtitle.setFontSize(10);
                // estiloSubtitle.setBold();
                estiloSubtitle.setFont(bold);

                Style estiloDescAtributo = new Style();
                estiloDescAtributo.setFontSize(8);
                // estiloDescAtributo.setBold();
                estiloDescAtributo.setFont(bold);

                Style estiloDescValor = new Style();
                estiloDescValor.setFontSize(8);
                estiloDescValor.setFont(regular);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setTextAlignment(TextAlignment.CENTER);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setTextAlignment(TextAlignment.CENTER);
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                SolidLine line = new SolidLine(.8f);
                line.setColor(ColorConstants.BLACK);
                LineSeparator ls = new LineSeparator(line);
                ls.setMarginTop(15);
                ls.setMarginBottom(5);
                ls.setWidth(Utilidades.cmToFloat(19));
                if (COMPROBANTE.getComplemento().CartaPorte10.getFiguraTransporte() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte10.FiguraTransporte figuraTransporte = COMPROBANTE
                                .getComplemento().CartaPorte10
                                .getFiguraTransporte();
                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 3f, 5f });
                Table tablaFiguraTransporte = new Table(anchoColumnas);
                tablaFiguraTransporte.addCell(
                                new Cell().add(new Paragraph("Tipo de servicio:").addStyle(estiloDescAtributo))
                                                .addStyle(estiloCelda));
                tablaFiguraTransporte.addCell(new Cell().add(
                                new Paragraph(Catalogos.ObtenerClaveTransporte(figuraTransporte.getCveTransporte()))
                                                .addStyle(estiloDescValor))
                                .addStyle(estiloCelda));

                DOCUMENTO.add(new Paragraph("Figura de transporte").addStyle(estiloSubtitle));
                DOCUMENTO.add(new Cell().add(tablaFiguraTransporte));
                AgregarOperadores10SAT();
                AgregarPropietario10SAT();
                AgregarArrendatario10SAT();
                AgregarNotificado10SAT();
                DOCUMENTO.add(ls);

        }

        private void AgregarOperadores10SAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setMarginTop(15);
                estiloSubtitle.setMarginLeft(0);
                estiloSubtitle.setFontSize(10);
                // estiloSubtitle.setBold();
                estiloSubtitle.setFont(bold);

                Style estiloDescAtributo = new Style();
                estiloDescAtributo.setFontSize(8);
                // estiloDescAtributo.setBold();
                estiloDescAtributo.setFont(bold);

                Style estiloDescValor = new Style();
                estiloDescValor.setFontSize(8);
                estiloDescValor.setFont(regular);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setTextAlignment(TextAlignment.CENTER);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setTextAlignment(TextAlignment.CENTER);
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                com.itextpdf.xmltopdf.CartaPorte10.FiguraTransporte figuraTransporte = COMPROBANTE
                                .getComplemento().CartaPorte10
                                .getFiguraTransporte();
                if (figuraTransporte.getOperadores() == null && figuraTransporte.getOperadores().isEmpty()) {
                        return;
                }

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 1.2f, 2.6f, 2.6f, 6.4f, 2.6f, 3.5f });
                Table tablaOperadores = new Table(anchoColumnas);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 6f, 3.4f, 6f });
                Table tablaDomicilios = new Table(anchoColumnas);
                tablaOperadores.addCell(new Cell().add(new Paragraph("Núm").addStyle(estiloAtributo)));
                tablaOperadores.addCell(new Cell().add(new Paragraph("RFC").addStyle(estiloAtributo)));
                tablaOperadores.addCell(new Cell().add(new Paragraph("Núm. licencia").addStyle(estiloAtributo)));
                tablaOperadores.addCell(new Cell().add(new Paragraph("Nombre").addStyle(estiloAtributo)));
                tablaOperadores.addCell(new Cell().add(new Paragraph("Num. Reg. Id Trib.").addStyle(estiloAtributo)));
                tablaOperadores.addCell(new Cell().add(new Paragraph("Residencia fiscal").addStyle(estiloAtributo)));
                int contador = 0;
                for (int i = 0; i < figuraTransporte.getOperadores().size(); i++) {
                        com.itextpdf.xmltopdf.CartaPorte10.Operadores operadores = figuraTransporte.getOperadores()
                                        .get(i);
                        for (int j = 0; j < operadores.getOperador().size(); j++) {
                                contador++;
                                com.itextpdf.xmltopdf.CartaPorte10.Operador operador = operadores.getOperador().get(j);
                                tablaOperadores
                                                .addCell(new Cell().add(new Paragraph(Integer.toString(contador))
                                                                .addStyle(estiloValor)));
                                tablaOperadores.addCell(new Cell()
                                                .add(new Paragraph(operador.getRFCOperador()).addStyle(estiloValor)));
                                tablaOperadores.addCell(new Cell()
                                                .add(new Paragraph(operador.getNumLicencia()).addStyle(estiloValor)));
                                tablaOperadores
                                                .addCell(new Cell().add(new Paragraph(operador.getNombreOperador())
                                                                .addStyle(estiloValor)));
                                tablaOperadores.addCell(
                                                new Cell().add(new Paragraph(operador.getNumRegIdTribOperador())
                                                                .addStyle(estiloValor)));
                                tablaOperadores.addCell(
                                                new Cell().add(new Paragraph(operador.getResidenciaFiscalOperador())
                                                                .addStyle(estiloValor)));

                                if (operador.getDomicilio() != null) {
                                        com.itextpdf.xmltopdf.CartaPorte10.Domicilio domicilio = operador
                                                        .getDomicilio();
                                        tablaDomicilios.addCell(new Cell(1, 4).add(
                                                        new Paragraph("Domicilio operador "
                                                                        + Integer.toString(contador))
                                                                        .addStyle(estiloSubtitle))
                                                        .addStyle(estiloCelda));
                                        if (!domicilio.getCalle().isEmpty()) {
                                                tablaDomicilios.addCell(new Cell()
                                                                .add(new Paragraph("Calle:")
                                                                                .addStyle(estiloDescAtributo))
                                                                .addStyle(estiloCelda));
                                                tablaDomicilios
                                                                .addCell(new Cell().add(new Paragraph(
                                                                                domicilio.getCalle())
                                                                                .addStyle(estiloDescValor))
                                                                                .addStyle(estiloCelda));
                                        }
                                        if (!domicilio.getNumeroExterior().isEmpty()) {
                                                tablaDomicilios
                                                                .addCell(new Cell().add(new Paragraph("Núm. exterior:")
                                                                                .addStyle(estiloDescAtributo))
                                                                                .addStyle(estiloCelda));
                                                tablaDomicilios.addCell(
                                                                new Cell().add(new Paragraph(
                                                                                domicilio.getNumeroExterior())
                                                                                .addStyle(estiloDescValor))
                                                                                .addStyle(estiloCelda));
                                        }
                                        if (!domicilio.getNumeroExterior().isEmpty()) {
                                                tablaDomicilios
                                                                .addCell(new Cell().add(new Paragraph("Núm. interior:")
                                                                                .addStyle(estiloDescAtributo))
                                                                                .addStyle(estiloCelda));
                                                tablaDomicilios.addCell(
                                                                new Cell().add(new Paragraph(
                                                                                domicilio.getNumeroInterior())
                                                                                .addStyle(estiloDescValor))
                                                                                .addStyle(estiloCelda));
                                        }
                                        if (!domicilio.getColonia().isEmpty()) {
                                                tablaDomicilios.addCell(new Cell()
                                                                .add(new Paragraph("Colonia:")
                                                                                .addStyle(estiloDescAtributo))
                                                                .addStyle(estiloCelda));
                                                tablaDomicilios
                                                                .addCell(new Cell().add(new Paragraph(
                                                                                domicilio.getColonia())
                                                                                .addStyle(estiloDescValor))
                                                                                .addStyle(estiloCelda));
                                        }
                                        if (!domicilio.getLocalidad().isEmpty()) {
                                                tablaDomicilios.addCell(new Cell()
                                                                .add(new Paragraph("Localidad:")
                                                                                .addStyle(estiloDescAtributo))
                                                                .addStyle(estiloCelda));
                                                tablaDomicilios.addCell(
                                                                new Cell().add(new Paragraph(domicilio.getLocalidad())
                                                                                .addStyle(estiloDescValor))
                                                                                .addStyle(estiloCelda));
                                        }
                                        if (!domicilio.getReferencia().isEmpty()) {
                                                tablaDomicilios.addCell(new Cell()
                                                                .add(new Paragraph("Referencia:")
                                                                                .addStyle(estiloDescAtributo))
                                                                .addStyle(estiloCelda));
                                                tablaDomicilios.addCell(
                                                                new Cell().add(new Paragraph(domicilio.getReferencia())
                                                                                .addStyle(estiloDescValor))
                                                                                .addStyle(estiloCelda));
                                        }
                                        if (!domicilio.getMunicipio().isEmpty()) {
                                                tablaDomicilios.addCell(new Cell()
                                                                .add(new Paragraph("Municipio:")
                                                                                .addStyle(estiloDescAtributo))
                                                                .addStyle(estiloCelda));
                                                tablaDomicilios.addCell(
                                                                new Cell().add(new Paragraph(domicilio.getMunicipio())
                                                                                .addStyle(estiloDescValor))
                                                                                .addStyle(estiloCelda));
                                        }
                                        if (!domicilio.getEstado().isEmpty()) {
                                                tablaDomicilios.addCell(new Cell()
                                                                .add(new Paragraph("Estado:")
                                                                                .addStyle(estiloDescAtributo))
                                                                .addStyle(estiloCelda));
                                                tablaDomicilios
                                                                .addCell(new Cell().add(new Paragraph(
                                                                                domicilio.getEstado())
                                                                                .addStyle(estiloDescValor))
                                                                                .addStyle(estiloCelda));
                                        }
                                        if (!domicilio.getPais().isEmpty()) {
                                                tablaDomicilios.addCell(new Cell()
                                                                .add(new Paragraph("Pais:")
                                                                                .addStyle(estiloDescAtributo))
                                                                .addStyle(estiloCelda));
                                                tablaDomicilios
                                                                .addCell(new Cell().add(new Paragraph(
                                                                                domicilio.getPais())
                                                                                .addStyle(estiloDescValor))
                                                                                .addStyle(estiloCelda));
                                        }
                                        tablaDomicilios.startNewRow();
                                }
                        }
                }
                DOCUMENTO.add(new Paragraph("Operadores").addStyle(estiloSubtitle));
                DOCUMENTO.add(tablaOperadores);
                if (tablaDomicilios.getNumberOfRows() > 0) {
                        DOCUMENTO.add(tablaDomicilios);
                }
        }

        private void AgregarPropietario10SAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setMarginTop(15);
                estiloSubtitle.setMarginLeft(0);
                estiloSubtitle.setFontSize(10);
                // estiloSubtitle.setBold();
                estiloSubtitle.setFont(bold);

                Style estiloDescAtributo = new Style();
                estiloDescAtributo.setFontSize(8);
                // estiloDescAtributo.setBold();
                estiloDescAtributo.setFont(bold);

                Style estiloDescValor = new Style();
                estiloDescValor.setFontSize(8);
                estiloDescValor.setFont(regular);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setTextAlignment(TextAlignment.CENTER);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setTextAlignment(TextAlignment.CENTER);
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                com.itextpdf.xmltopdf.CartaPorte10.FiguraTransporte figuraTransporte = COMPROBANTE
                                .getComplemento().CartaPorte10
                                .getFiguraTransporte();
                if (figuraTransporte.getPropietario() == null || figuraTransporte.getPropietario().isEmpty()) {
                        return;
                }

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 1.2f, 4.5f, 4.4f, 4.4f, 4.4f });
                Table tablaPropietarios = new Table(anchoColumnas);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 6f, 3.4f, 6f });
                Table tablaDomicilios = new Table(anchoColumnas);
                tablaPropietarios.addCell(new Cell().add(new Paragraph("Núm").addStyle(estiloAtributo)));
                tablaPropietarios.addCell(new Cell().add(new Paragraph("RFC").addStyle(estiloAtributo)));
                tablaPropietarios.addCell(new Cell().add(new Paragraph("Nombre").addStyle(estiloAtributo)));
                tablaPropietarios.addCell(new Cell().add(new Paragraph("Num. Reg. Id Trib.").addStyle(estiloAtributo)));
                tablaPropietarios.addCell(new Cell().add(new Paragraph("Residencia fiscal").addStyle(estiloAtributo)));
                int contador = 0;
                for (int i = 0; i < figuraTransporte.getPropietario().size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte10.Propietario propietario = figuraTransporte.getPropietario()
                                        .get(i);
                        tablaPropietarios.addCell(new Cell()
                                        .add(new Paragraph(Integer.toString(contador)).addStyle(estiloValor)));
                        tablaPropietarios
                                        .addCell(new Cell().add(new Paragraph(propietario.getRFCPropietario())
                                                        .addStyle(estiloValor)));
                        tablaPropietarios
                                        .addCell(new Cell().add(new Paragraph(propietario.getNombrePropietario())
                                                        .addStyle(estiloValor)));
                        tablaPropietarios.addCell(
                                        new Cell().add(new Paragraph(propietario.getNumRegIdTribPropietario())
                                                        .addStyle(estiloValor)));
                        tablaPropietarios.addCell(
                                        new Cell().add(new Paragraph(propietario.getResidenciaFiscalPropietario())
                                                        .addStyle(estiloValor)));
                        if (propietario.getDomicilio() != null) {
                                com.itextpdf.xmltopdf.CartaPorte10.Domicilio domicilio = propietario.getDomicilio();
                                tablaDomicilios.addCell(new Cell(1, 4).add(
                                                new Paragraph("Domicilio propietario " + Integer.toString(contador))
                                                                .addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                                if (!domicilio.getCalle().isEmpty()) {
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph("Calle:")
                                                                        .addStyle(estiloDescAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getCalle())
                                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getNumeroExterior().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Núm. exterior:")
                                                                        .addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph(domicilio.getNumeroExterior())
                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getNumeroExterior().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Núm. interior:")
                                                                        .addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph(domicilio.getNumeroInterior())
                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getColonia().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Colonia:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getColonia())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getLocalidad().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Localidad:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getLocalidad())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getReferencia().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Referencia:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getReferencia())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getMunicipio().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Municipio:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getMunicipio())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getEstado().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Estado:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getEstado())
                                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getPais().isEmpty()) {
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph("Pais:")
                                                                        .addStyle(estiloDescAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getPais())
                                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                                }
                                tablaDomicilios.startNewRow();

                        }
                }
                DOCUMENTO.add(new Paragraph("Propietarios").addStyle(estiloSubtitle));
                DOCUMENTO.add(tablaPropietarios);
                if (tablaDomicilios.getNumberOfRows() > 0) {
                        DOCUMENTO.add(tablaDomicilios);
                }
        }

        private void AgregarArrendatario10SAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setMarginTop(15);
                estiloSubtitle.setMarginLeft(0);
                estiloSubtitle.setFontSize(10);
                // estiloSubtitle.setBold();
                estiloSubtitle.setFont(bold);

                Style estiloDescAtributo = new Style();
                estiloDescAtributo.setFontSize(8);
                // estiloDescAtributo.setBold();
                estiloDescAtributo.setFont(bold);

                Style estiloDescValor = new Style();
                estiloDescValor.setFontSize(8);
                estiloDescValor.setFont(regular);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setTextAlignment(TextAlignment.CENTER);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setTextAlignment(TextAlignment.CENTER);
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                com.itextpdf.xmltopdf.CartaPorte10.FiguraTransporte figuraTransporte = COMPROBANTE
                                .getComplemento().CartaPorte10
                                .getFiguraTransporte();
                if (figuraTransporte.getArrendatario() == null || figuraTransporte.getArrendatario().isEmpty()) {
                        return;
                }

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 1.2f, 4.5f, 4.4f, 4.4f, 4.4f });
                Table tablaArrendatario = new Table(anchoColumnas);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 6f, 3.4f, 6f });
                Table tablaDomicilios = new Table(anchoColumnas);
                tablaArrendatario.addCell(new Cell().add(new Paragraph("Núm").addStyle(estiloAtributo)));
                tablaArrendatario.addCell(new Cell().add(new Paragraph("RFC").addStyle(estiloAtributo)));
                tablaArrendatario.addCell(new Cell().add(new Paragraph("Nombre").addStyle(estiloAtributo)));
                tablaArrendatario.addCell(new Cell().add(new Paragraph("Num. Reg. Id Trib.").addStyle(estiloAtributo)));
                tablaArrendatario.addCell(new Cell().add(new Paragraph("Residencia fiscal").addStyle(estiloAtributo)));
                int contador = 0;
                for (int i = 0; i < figuraTransporte.getArrendatario().size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte10.Arrendatario arrendatario = figuraTransporte
                                        .getArrendatario().get(i);
                        tablaArrendatario.addCell(new Cell()
                                        .add(new Paragraph(Integer.toString(contador)).addStyle(estiloValor)));
                        tablaArrendatario
                                        .addCell(new Cell().add(new Paragraph(arrendatario.getRFCArrendatario())
                                                        .addStyle(estiloValor)));
                        tablaArrendatario
                                        .addCell(new Cell().add(new Paragraph(arrendatario.getNombreArrendatario())
                                                        .addStyle(estiloValor)));
                        tablaArrendatario.addCell(
                                        new Cell().add(new Paragraph(arrendatario.getNumRegIdTribArrendatario())
                                                        .addStyle(estiloValor)));
                        tablaArrendatario.addCell(new Cell()
                                        .add(new Paragraph(arrendatario.getResidenciaFiscalArrendatario())
                                                        .addStyle(estiloValor)));
                        if (arrendatario.getDomicilio() != null) {
                                com.itextpdf.xmltopdf.CartaPorte10.Domicilio domicilio = arrendatario.getDomicilio();
                                tablaDomicilios.addCell(new Cell(1, 4).add(
                                                new Paragraph("Domicilio arrendatario " + Integer.toString(contador))
                                                                .addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                                if (!domicilio.getCalle().isEmpty()) {
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph("Calle:")
                                                                        .addStyle(estiloDescAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getCalle())
                                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getNumeroExterior().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Núm. exterior:")
                                                                        .addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph(domicilio.getNumeroExterior())
                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getNumeroExterior().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Núm. interior:")
                                                                        .addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph(domicilio.getNumeroInterior())
                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getColonia().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Colonia:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getColonia())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getLocalidad().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Localidad:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getLocalidad())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getReferencia().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Referencia:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getReferencia())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getMunicipio().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Municipio:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getMunicipio())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getEstado().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Estado:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getEstado())
                                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getPais().isEmpty()) {
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph("Pais:")
                                                                        .addStyle(estiloDescAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getPais())
                                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                                }
                                tablaDomicilios.startNewRow();

                        }
                }
                DOCUMENTO.add(new Paragraph("Arrendatarios").addStyle(estiloSubtitle));
                DOCUMENTO.add(tablaArrendatario);
                if (tablaDomicilios.getNumberOfRows() > 0) {
                        DOCUMENTO.add(tablaDomicilios);
                }
        }

        private void AgregarNotificado10SAT() {
                Style estiloSubtitle = new Style();
                estiloSubtitle.setMarginTop(15);
                estiloSubtitle.setMarginLeft(0);
                estiloSubtitle.setFontSize(10);
                // estiloSubtitle.setBold();
                estiloSubtitle.setFont(bold);

                Style estiloDescAtributo = new Style();
                estiloDescAtributo.setFontSize(8);
                // estiloDescAtributo.setBold();
                estiloDescAtributo.setFont(bold);

                Style estiloDescValor = new Style();
                estiloDescValor.setFontSize(8);
                estiloDescValor.setFont(regular);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(8);
                estiloAtributo.setTextAlignment(TextAlignment.CENTER);
                // estiloAtributo.setBold();
                estiloAtributo.setFont(bold);

                Style estiloValor = new Style();
                estiloValor.setTextAlignment(TextAlignment.CENTER);
                estiloValor.setFontSize(8);
                estiloValor.setFont(regular);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                com.itextpdf.xmltopdf.CartaPorte10.FiguraTransporte figuraTransporte = COMPROBANTE
                                .getComplemento().CartaPorte10
                                .getFiguraTransporte();
                if (figuraTransporte.getNotificado() == null || figuraTransporte.getNotificado().isEmpty()) {
                        return;
                }

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 1.2f, 4.5f, 4.4f, 4.4f, 4.4f });
                Table tablaNotificado = new Table(anchoColumnas);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 6f, 3.4f, 6f });
                Table tablaDomicilios = new Table(anchoColumnas);
                tablaNotificado.addCell(new Cell().add(new Paragraph("Núm").addStyle(estiloAtributo)));
                tablaNotificado.addCell(new Cell().add(new Paragraph("RFC").addStyle(estiloAtributo)));
                tablaNotificado.addCell(new Cell().add(new Paragraph("Nombre").addStyle(estiloAtributo)));
                tablaNotificado.addCell(new Cell().add(new Paragraph("Num. Reg. Id Trib.").addStyle(estiloAtributo)));
                tablaNotificado.addCell(new Cell().add(new Paragraph("Residencia fiscal").addStyle(estiloAtributo)));
                int contador = 0;
                for (int i = 0; i < figuraTransporte.getNotificado().size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte10.Notificado notificado = figuraTransporte.getNotificado()
                                        .get(i);
                        tablaNotificado.addCell(new Cell()
                                        .add(new Paragraph(Integer.toString(contador)).addStyle(estiloValor)));
                        tablaNotificado.addCell(new Cell()
                                        .add(new Paragraph(notificado.getRFCNotificado()).addStyle(estiloValor)));
                        tablaNotificado
                                        .addCell(new Cell().add(new Paragraph(notificado.getNombreNotificado())
                                                        .addStyle(estiloValor)));
                        tablaNotificado.addCell(
                                        new Cell().add(new Paragraph(notificado.getNumRegIdTribNotificado())
                                                        .addStyle(estiloValor)));
                        tablaNotificado.addCell(
                                        new Cell().add(new Paragraph(notificado.getResidenciaFiscalNotificado())
                                                        .addStyle(estiloValor)));
                        if (notificado.getDomicilio() != null) {
                                com.itextpdf.xmltopdf.CartaPorte10.Domicilio domicilio = notificado.getDomicilio();
                                tablaDomicilios.addCell(new Cell(1, 4).add(
                                                new Paragraph("Domicilio notificado " + Integer.toString(contador))
                                                                .addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                                if (!domicilio.getCalle().isEmpty()) {
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph("Calle:")
                                                                        .addStyle(estiloDescAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getCalle())
                                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getNumeroExterior().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Núm. exterior:")
                                                                        .addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph(domicilio.getNumeroExterior())
                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getNumeroExterior().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Núm. interior:")
                                                                        .addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph(domicilio.getNumeroInterior())
                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getColonia().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Colonia:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getColonia())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getLocalidad().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Localidad:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getLocalidad())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getReferencia().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Referencia:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getReferencia())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getMunicipio().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Municipio:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(domicilio.getMunicipio())
                                                                                        .addStyle(estiloDescValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getEstado().isEmpty()) {
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph("Estado:").addStyle(estiloDescAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getEstado())
                                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (!domicilio.getPais().isEmpty()) {
                                        tablaDomicilios.addCell(
                                                        new Cell().add(new Paragraph("Pais:")
                                                                        .addStyle(estiloDescAtributo))
                                                                        .addStyle(estiloCelda));
                                        tablaDomicilios.addCell(new Cell()
                                                        .add(new Paragraph(domicilio.getPais())
                                                                        .addStyle(estiloDescValor))
                                                        .addStyle(estiloCelda));
                                }
                                tablaDomicilios.startNewRow();

                        }
                }
                DOCUMENTO.add(new Paragraph("Notificados").addStyle(estiloSubtitle));
                DOCUMENTO.add(tablaNotificado);
                if (tablaDomicilios.getNumberOfRows() > 0) {
                        DOCUMENTO.add(tablaDomicilios);
                }
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Agregar Pagos">

        private Table PAgregarDocumentosRelacionados(List<PDoctoRelacionado> documentosRelacionados) {
                Color color = new DeviceRgb(241, 241, 241);
                Border borde = new SolidBorder(color, 1);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(borde);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setBackgroundColor(ColorConstants.BLACK);
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setBorder(borde);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(7);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setFontColor(ColorConstants.WHITE);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(7);
                estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(7);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.0F, 6.0F, 4.0F, 6.0F });
                Table tDocumentos = new Table(anchoColumnas);
                tDocumentos.setMarginTop(7);
                if (documentosRelacionados.size() > 0) {
                        int count = 1;
                        for (int i = 0; i < documentosRelacionados.size(); i++) {
                                PDoctoRelacionado dr = documentosRelacionados.get(i);
                                Cell cEncabezado = new Cell(1, 4);
                                cEncabezado.add(new Cell()
                                                .add(new Paragraph("DOCUMENTO RELACIONADO " + count)
                                                                .addStyle(estiloTitle)));
                                cEncabezado.addStyle(estiloCeldaTitle);
                                tDocumentos.addCell(cEncabezado);
                                tDocumentos.startNewRow();
                                tDocumentos.addCell(
                                                new Cell().add(new Paragraph("Id Documento:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tDocumentos.addCell(
                                                new Cell().add(new Paragraph(dr.getIdDocumento()).addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                tDocumentos.addCell(
                                                new Cell().add(new Paragraph("Folio:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tDocumentos.addCell(
                                                new Cell().add(new Paragraph(dr.getSerie() + " " + dr.getFolio())
                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                tDocumentos.addCell(
                                                new Cell().add(new Paragraph("Moneda:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tDocumentos.addCell(
                                                new Cell().add(new Paragraph(dr.getMonedaDR()).addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                if (dr.getTipoCambioDR() > 0) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Tipo de cambio:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(
                                                        new Cell().add(new Paragraph(
                                                                        Utilidades.convertir(dr.getTipoCambioDR()))
                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                tDocumentos.addCell(new Cell()
                                                .add(new Paragraph("Metodo de Pago:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                                tDocumentos.addCell(new Cell()
                                                .add(new Paragraph(dr.getMetodoDePagoDR() + " - "
                                                                + Catalogos.ObtenerMetodoPago(dr.getMetodoDePagoDR()))
                                                                .addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                                if (!dr.getNumParcialidad().isEmpty()) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Núm. de parcialidad:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph(dr.getNumParcialidad())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (dr.getImpSaldoAnt() != 0) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Saldo anterior:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos
                                                        .addCell(new Cell().add(
                                                                        new Paragraph(Utilidades
                                                                                        .convertir(dr.getImpSaldoAnt()))
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (dr.getImpPagado() != 0) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Importe pagado:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(Utilidades
                                                                                        .convertir(dr.getImpPagado()))
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (dr.getImpSaldoInsoluto() != 0) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Saldo insoluto:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(
                                                        new Cell().add(new Paragraph(
                                                                        Utilidades.convertir(dr.getImpSaldoInsoluto()))
                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                tDocumentos.startNewRow();
                                count += 1;
                        }
                }
                return tDocumentos;
        }

        private Table PAgregarPago(Pago pago, int contador) {
                Color color = new DeviceRgb(241, 241, 241);
                Border borde = new SolidBorder(color, 1);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(borde);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setBackgroundColor(ColorConstants.BLACK);
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setBorder(borde);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(7);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setFontColor(ColorConstants.WHITE);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(7);
                estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(7);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.0F, 6.0F, 4.0F, 6.0F });
                Table tPagos = new Table(anchoColumnas);
                tPagos.setMarginTop(7);
                tPagos.setHorizontalAlignment(HorizontalAlignment.CENTER);

                Cell ctitulo = new Cell(1, 4)
                                .add(new Paragraph("PAGO " + contador).addStyle(estiloTitle))
                                .addStyle(estiloCeldaTitle);
                tPagos.addCell(ctitulo);
                tPagos.addCell(new Cell().add(new Paragraph("Fecha de pago:").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tPagos.addCell(new Cell().add(new Paragraph(pago.getFechaPago().toString()).addStyle(estiloValor))
                                .addStyle(estiloCelda));
                tPagos.addCell(new Cell().add(new Paragraph("Forma de pago:").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tPagos.addCell(
                                new Cell().add(new Paragraph(pago.getFormaDePagoP() + " - "
                                                + Catalogos.ObtenerFormaPago(pago.getFormaDePagoP()))
                                                .addStyle(estiloValor)).addStyle(estiloCelda));
                tPagos.addCell(new Cell().add(new Paragraph("Moneda:").addStyle(estiloAtributo)).addStyle(estiloCelda));
                tPagos.addCell(new Cell().add(new Paragraph(pago.getMonedaP()).addStyle(estiloValor))
                                .addStyle(estiloCelda));

                if (pago.getTipoCambioP() != 0) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("Tipo de cambio:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph(Utilidades.convertir(pago.getTipoCambioP()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                tPagos.addCell(new Cell().add(new Paragraph("Monto:").addStyle(estiloAtributo)).addStyle(estiloCelda));
                ;
                tPagos.addCell(
                                new Cell().add(new Paragraph(Utilidades.convertir(pago.getMonto()))
                                                .addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                if (!pago.getNumOperacion().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("Núm. de operación:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getNumOperacion()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getRfcEmisorCtaOrd().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("RFC Emisor Cta:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        ;
                        tPagos.addCell(new Cell().add(new Paragraph(pago.getRfcEmisorCtaOrd()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!pago.getNomBancoOrdExt().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("NomBancoOrdExt:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        ;
                        tPagos.addCell(new Cell().add(new Paragraph(pago.getNomBancoOrdExt()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!pago.getCtaOrdenante().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("CtaOrdenante:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        ;
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getCtaOrdenante()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getRfcEmisorCtaOrd().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("RFC Emisor Cta:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell().add(new Paragraph(pago.getRfcEmisorCtaOrd()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!pago.getRfcEmisorCtaBen().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("RfcEmisorCtaBen:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell().add(new Paragraph(pago.getRfcEmisorCtaBen()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!pago.getCtaBeneficiario().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("CtaBeneficiario:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell().add(new Paragraph(pago.getCtaBeneficiario()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!pago.getTipoCadPago().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("TipoCadPago:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getTipoCadPago()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getCertPago().isEmpty()) {
                        tPagos.addCell(new Cell().add(new Paragraph("CertPago:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getCertPago()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getCadPago().isEmpty()) {
                        tPagos.addCell(new Cell().add(new Paragraph("CadPago:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getCadPago()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getSelloPago().isEmpty()) {
                        tPagos.addCell(new Cell().add(new Paragraph("SelloPago:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getSelloPago()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                tPagos.startNewRow();

                if (pago.getImpuestos() != null && pago.getImpuestos().size() > 0) {
                        Cell cdr = new Cell(1, 3).add(new Paragraph("- Impuestos:").addStyle(estiloAtributo));
                        cdr.addStyle(estiloCelda);
                        tPagos.addCell(cdr);
                        tPagos.startNewRow();
                        for (int i = 0; i < pago.getImpuestos().size(); i++) {
                                PImpuestos impuestos = pago.getImpuestos().get(i);
                                for (int j = 0; j < impuestos.getRetenciones().getRetencion().size(); j++) {
                                        PRetencion r = impuestos.getRetenciones().getRetencion().get(j);
                                        if (!r.getImpuesto().isEmpty()) {
                                                tPagos.addCell(new Cell()
                                                                .add(new Paragraph("Impuesto:")
                                                                                .addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                                tPagos.addCell(new Cell()
                                                                .add(new Paragraph(r.getImpuesto())
                                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                        }
                                        if (r.getImporte() != 0) {
                                                tPagos.addCell(new Cell()
                                                                .add(new Paragraph("Importe:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                                tPagos.addCell(new Cell()
                                                                .add(new Paragraph(Utilidades.convertir(r.getImporte()))
                                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                        }
                                        tPagos.startNewRow();
                                }
                                for (int k = 0; k < impuestos.getTraslados().getTraslado().size(); i++) {
                                        PTraslado t = impuestos.getTraslados().getTraslado().get(i);
                                        tPagos.addCell(
                                                        new Cell().add(new Paragraph("Impuesto:")
                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tPagos.addCell(
                                                        new Cell().add(new Paragraph(t.getImpuesto())
                                                                        .addStyle(estiloValor)).addStyle(estiloCelda));
                                        tPagos.addCell(new Cell()
                                                        .add(new Paragraph("Tipo factor:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tPagos.addCell(new Cell()
                                                        .add(new Paragraph(t.getTipoFactor()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                        tPagos.addCell(
                                                        new Cell().add(new Paragraph("Tasa:").addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tPagos.addCell(new Cell()
                                                        .add(new Paragraph(Utilidades.convertir(t.getTasaOCuota()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                        tPagos.addCell(
                                                        new Cell().add(new Paragraph("Importe:")
                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tPagos.addCell(new Cell()
                                                        .add(new Paragraph(Utilidades.convertir(t.getImporte()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                        tPagos.startNewRow();
                                }
                                if (impuestos.getTotalImpuestosRetenidos() > 0) {
                                        tPagos.addCell(new Cell()
                                                        .add(new Paragraph("Total impuestos retenidos:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tPagos.addCell(new Cell()
                                                        .add(new Paragraph(Utilidades.convertir(
                                                                        impuestos.getTotalImpuestosRetenidos()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                        tPagos.startNewRow();
                                }
                                if (impuestos.getTotalImpuestosTrasladados() > 0) {
                                        tPagos.addCell(
                                                        new Cell().add(new Paragraph("Total impuestos trasladados:")
                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tPagos.addCell(new Cell().add(
                                                        new Paragraph(Utilidades.convertir(
                                                                        impuestos.getTotalImpuestosTrasladados()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                        tPagos.startNewRow();
                                }
                        }
                }
                return tPagos;
        }

        private void AgregarTablaPagos() {
                if (COMPROBANTE.getComplemento().Pagos == null
                                || COMPROBANTE.getComplemento().Pagos.getPago().size() <= 0) {
                        return;
                }
                int contador = 1;
                for (int i = 0; i < COMPROBANTE.getComplemento().Pagos.getPago().size(); i++) {
                        Pago pago = COMPROBANTE.getComplemento().Pagos.getPago().get(i);
                        DOCUMENTO.add(PAgregarPago(pago, contador));
                        if (pago.getDoctoRelacionado() != null && pago.getDoctoRelacionado().size() > 0) {
                                DOCUMENTO.add(PAgregarDocumentosRelacionados(pago.getDoctoRelacionado()));
                        }
                        contador += 1;
                }
        }

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Agregar Pagos 20">
        private void PAgregarDocumentosRelacionados20(
                        List<com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado> documentosRelacionados) {
                Color color = new DeviceRgb(241, 241, 241);
                Border borde = new SolidBorder(color, 1);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(borde);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setBackgroundColor(ColorConstants.BLACK);
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setBorder(borde);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(7);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setFontColor(ColorConstants.WHITE);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(7);
                estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(7);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                if (documentosRelacionados.size() > 0) {
                        int count = 1;
                        float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.0F, 6.0F, 4.0F, 6.0F });
                        Table tDocumentos = new Table(anchoColumnas);
                        tDocumentos.setMarginTop(7);

                        for (int i = 0; i < documentosRelacionados.size(); i++) {
                                com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado dr = documentosRelacionados.get(i);
                                Cell cEncabezado = new Cell(1, 4);
                                cEncabezado.add(new Cell()
                                                .add(new Paragraph("DOCUMENTO RELACIONADO " + count)
                                                                .addStyle(estiloTitle)));
                                cEncabezado.addStyle(estiloCeldaTitle);
                                tDocumentos.addCell(cEncabezado);
                                tDocumentos.startNewRow();
                                tDocumentos.addCell(
                                                new Cell().add(new Paragraph("Id Documento:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tDocumentos.addCell(
                                                new Cell().add(new Paragraph(dr.getIdDocumento()).addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                if (!dr.getSerie().isEmpty()) {
                                        tDocumentos.addCell(
                                                        new Cell().add(new Paragraph("Serie:").addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(
                                                        new Cell().add(new Paragraph(dr.getSerie())
                                                                        .addStyle(estiloValor)).addStyle(estiloCelda));
                                }
                                if (!dr.getFolio().isEmpty()) {
                                        tDocumentos.addCell(
                                                        new Cell().add(new Paragraph("Folio:").addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(
                                                        new Cell().add(new Paragraph(dr.getFolio())
                                                                        .addStyle(estiloValor)).addStyle(estiloCelda));
                                }
                                if (!dr.getMonedaDR().isEmpty()) {
                                        tDocumentos.addCell(
                                                        new Cell().add(new Paragraph("Moneda:")
                                                                        .addStyle(estiloAtributo))
                                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph(dr.getMonedaDR()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (dr.getEquivalenciaDR() > 0) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Equivalencia:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(
                                                        new Cell().add(new Paragraph(
                                                                        Utilidades.convertir(dr.getEquivalenciaDR()))
                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!dr.getNumParcialidad().isEmpty()) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Núm. de parcialidad:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph(dr.getNumParcialidad())
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                if (dr.getImpSaldoAnt() != 0) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Saldo anterior:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos
                                                        .addCell(new Cell().add(
                                                                        new Paragraph(Utilidades
                                                                                        .convertir(dr.getImpSaldoAnt()))
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (dr.getImpPagado() != 0) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Importe pagado:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(Utilidades
                                                                                        .convertir(dr.getImpPagado()))
                                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (dr.getImpSaldoInsoluto() != 0) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Saldo insoluto:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(
                                                        new Cell().add(new Paragraph(
                                                                        Utilidades.convertir(dr.getImpSaldoInsoluto()))
                                                                        .addStyle(estiloValor))
                                                                        .addStyle(estiloCelda));
                                }
                                if (!dr.getObjetoImpDR().isEmpty()) {
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph("Objeto de impuesto:")
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tDocumentos.addCell(new Cell()
                                                        .add(new Paragraph(dr.getObjetoImpDR() + " - "
                                                                        + Catalogos.ObtenerObjetoImp(
                                                                                        dr.getObjetoImpDR()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                                }
                                tDocumentos.startNewRow();
                                count += 1;
                        }
                        DOCUMENTO.add(tDocumentos);
                }
        }

        private void PAgregarPago20(com.itextpdf.xmltopdf.Pagos20.Pago pago, int contador) {
                Color color = new DeviceRgb(241, 241, 241);
                Border borde = new SolidBorder(color, 1);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(borde);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setBackgroundColor(ColorConstants.BLACK);
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setBorder(borde);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(7);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setFontColor(ColorConstants.WHITE);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(7);
                estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(7);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.0F, 6.0F, 4.0F, 6.0F });
                Table tPagos = new Table(anchoColumnas);
                tPagos.setMarginTop(7);
                tPagos.setHorizontalAlignment(HorizontalAlignment.CENTER);

                Cell ctitulo = new Cell(1, 4)
                                .add(new Paragraph("PAGO " + contador).addStyle(estiloTitle))
                                .addStyle(estiloCeldaTitle);
                tPagos.addCell(ctitulo);
                tPagos.addCell(new Cell().add(new Paragraph("Fecha de pago:").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tPagos.addCell(new Cell().add(new Paragraph(pago.getFechaPago().toString()).addStyle(estiloValor))
                                .addStyle(estiloCelda));
                tPagos.addCell(new Cell().add(new Paragraph("Forma de pago:").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tPagos.addCell(
                                new Cell().add(new Paragraph(pago.getFormaDePagoP() + " - "
                                                + Catalogos.ObtenerFormaPago(pago.getFormaDePagoP()))
                                                .addStyle(estiloValor)).addStyle(estiloCelda));
                tPagos.addCell(new Cell().add(new Paragraph("Moneda:").addStyle(estiloAtributo)).addStyle(estiloCelda));
                tPagos.addCell(new Cell().add(new Paragraph(pago.getMonedaP()).addStyle(estiloValor))
                                .addStyle(estiloCelda));

                if (pago.getTipoCambioP() != 0) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("Tipo de cambio:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph(Utilidades.convertir(pago.getTipoCambioP()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                tPagos.addCell(new Cell().add(new Paragraph("Monto:").addStyle(estiloAtributo)).addStyle(estiloCelda));
                ;
                tPagos.addCell(
                                new Cell().add(new Paragraph(Utilidades.convertir(pago.getMonto()))
                                                .addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                if (!pago.getNumOperacion().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("Núm. de operación:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getNumOperacion()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getRfcEmisorCtaOrd().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("RFC Emisor Cta:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        ;
                        tPagos.addCell(new Cell().add(new Paragraph(pago.getRfcEmisorCtaOrd()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!pago.getNomBancoOrdExt().isEmpty()) {
                        tPagos.addCell(new Cell().add(new Paragraph("Nombre banco ordenante:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        ;
                        tPagos.addCell(new Cell().add(new Paragraph(pago.getNomBancoOrdExt()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!pago.getCtaOrdenante().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("Cuenta ordenante:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        ;
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getCtaOrdenante()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getRfcEmisorCtaBen().isEmpty()) {
                        tPagos.addCell(new Cell().add(new Paragraph("Rfc emisor cuenta Ben.:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell().add(new Paragraph(pago.getRfcEmisorCtaBen()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!pago.getCtaBeneficiario().isEmpty()) {
                        tPagos.addCell(new Cell().add(new Paragraph("Cuenta beneficiario:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell().add(new Paragraph(pago.getCtaBeneficiario()).addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!pago.getTipoCadPago().isEmpty()) {
                        tPagos.addCell(new Cell().add(new Paragraph("Tipo cadena de pago:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getTipoCadPago()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getCertPago().isEmpty()) {
                        tPagos.addCell(new Cell().add(new Paragraph("Certificado de pago:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getCertPago()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getCadPago().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("Cadena de pago:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getCadPago()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (!pago.getSelloPago().isEmpty()) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("Sello de pago:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(pago.getSelloPago()).addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                tPagos.startNewRow();
                DOCUMENTO.add(tPagos);
        }

        private void AgregarImpuestosP20(com.itextpdf.xmltopdf.Pagos20.ImpuestosP impuestos) {
                Color color = new DeviceRgb(241, 241, 241);
                Border borde = new SolidBorder(color, 1);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(borde);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setBackgroundColor(ColorConstants.BLACK);
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setBorder(borde);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(7);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setFontColor(ColorConstants.WHITE);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(7);
                estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(7);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                if (impuestos == null) {
                        return;
                }
                if (impuestos.getRetencionesP() != null) {
                        float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.0F, 6.0F, 4.0F, 6.0F });
                        Table tRetenciones = new Table(anchoColumnas);
                        tRetenciones.setMarginTop(7);
                        tRetenciones.setHorizontalAlignment(HorizontalAlignment.CENTER);
                        Cell cTituloRetencion = new Cell(1, 4)
                                        .add(new Paragraph("RETENCIONES").addStyle(estiloTitle))
                                        .addStyle(estiloCeldaTitle);
                        tRetenciones.addCell(cTituloRetencion);
                        for (int i = 0; i < impuestos.getRetencionesP().getRetencionP().size(); i++) {
                                com.itextpdf.xmltopdf.Pagos20.RetencionP retencionP = impuestos.getRetencionesP()
                                                .getRetencionP()
                                                .get(i);
                                tRetenciones.addCell(
                                                new Cell().add(new Paragraph("Impuesto:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tRetenciones.addCell(new Cell().add(
                                                new Paragraph(retencionP.getImpuestoP() + " - "
                                                                + Catalogos.ObtenerImpuesto(retencionP.getImpuestoP()))
                                                                .addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                                tRetenciones.addCell(
                                                new Cell().add(new Paragraph("Importe:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tRetenciones.addCell(
                                                new Cell().add(new Paragraph(
                                                                Utilidades.convertir(retencionP.getImporteP()))
                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                        }
                        DOCUMENTO.add(tRetenciones);
                }
                if (impuestos.getTrasladosP() != null) {
                        float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.0F, 6.0F, 4.0F, 6.0F });
                        Table tTraslados = new Table(anchoColumnas);
                        tTraslados.setMarginTop(7);
                        tTraslados.setHorizontalAlignment(HorizontalAlignment.CENTER);

                        Cell cTituloTraslado = new Cell(1, 4)
                                        .add(new Paragraph("TRASLADOS").addStyle(estiloTitle))
                                        .addStyle(estiloCeldaTitle);
                        tTraslados.addCell(cTituloTraslado);
                        for (int i = 0; i < impuestos.getTrasladosP().getTrasladoP().size(); i++) {
                                com.itextpdf.xmltopdf.Pagos20.TrasladoP trasladoP = impuestos.getTrasladosP()
                                                .getTrasladoP().get(i);
                                tTraslados
                                                .addCell(new Cell().add(new Paragraph("Base:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tTraslados.addCell(new Cell()
                                                .add(new Paragraph(Utilidades.convertir(trasladoP.getBaseP()))
                                                                .addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                                tTraslados.addCell(
                                                new Cell().add(new Paragraph("Impuesto:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tTraslados.addCell(new Cell()
                                                .add(new Paragraph(trasladoP.getImpuestoP() + " - "
                                                                + Catalogos.ObtenerImpuesto(trasladoP.getImpuestoP()))
                                                                .addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                                tTraslados.addCell(
                                                new Cell().add(new Paragraph("Tipo factor:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tTraslados.addCell(new Cell()
                                                .add(new Paragraph(trasladoP.getTipoFactorP()).addStyle(estiloValor))
                                                .addStyle(estiloCelda));
                                tTraslados.addCell(
                                                new Cell().add(new Paragraph("Tasa o cuota:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tTraslados.addCell(
                                                new Cell().add(new Paragraph(
                                                                Utilidades.convertir(trasladoP.getTasaOCuotaP()))
                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                                tTraslados.addCell(
                                                new Cell().add(new Paragraph("Importe:").addStyle(estiloAtributo))
                                                                .addStyle(estiloCelda));
                                tTraslados
                                                .addCell(new Cell()
                                                                .add(new Paragraph(Utilidades
                                                                                .convertir(trasladoP.getImporteP()))
                                                                                .addStyle(estiloValor))
                                                                .addStyle(estiloCelda));
                        }
                        DOCUMENTO.add(tTraslados);
                }
        }

        private void AgregarTablaPagos20() {
                Color color = new DeviceRgb(241, 241, 241);
                Border borde = new SolidBorder(color, 1);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(borde);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setBackgroundColor(ColorConstants.BLACK);
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setBorder(borde);

                Style estiloTitle = new Style();
                estiloTitle.setFontSize(7);
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setFontColor(ColorConstants.WHITE);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(7);
                estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);

                Style estiloValor = new Style();
                estiloValor.setFontSize(7);
                estiloValor.setTextAlignment(TextAlignment.LEFT);

                if (COMPROBANTE.getComplemento().Pagos20 == null) {
                        return;
                }
                com.itextpdf.xmltopdf.Pagos20.Pagos20 pagos = COMPROBANTE.getComplemento().Pagos20;
                com.itextpdf.xmltopdf.Pagos20.Totales totales = COMPROBANTE.getComplemento().Pagos20.getTotales();

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4.0F, 6.0F, 4.0F, 6.0F });
                Table tPagos = new Table(anchoColumnas);
                tPagos.setMarginTop(7);
                tPagos.setHorizontalAlignment(HorizontalAlignment.CENTER);

                Cell ctitulo = new Cell(1, 4)
                                .add(new Paragraph("PAGOS").addStyle(estiloTitle))
                                .addStyle(estiloCeldaTitle);
                tPagos.addCell(ctitulo);
                if (totales.getTotalRetencionesIEPS() > 0) {
                        tPagos.addCell(new Cell().add(new Paragraph("Total retenciones IEPS:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalRetencionesIEPS()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (totales.getTotalRetencionesISR() > 0) {
                        tPagos.addCell(new Cell().add(new Paragraph("Total retenciones ISR:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalRetencionesISR()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (totales.getTotalRetencionesIVA() > 0) {
                        tPagos.addCell(new Cell().add(new Paragraph("Total retenciones IVA:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalRetencionesIVA()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (totales.getTotalTrasladosBaseIVA0() > 0) {
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph("Total traslados base IVA 0:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalTrasladosBaseIVA0()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (totales.getTotalTrasladosBaseIVA16() > 0) {
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph("Total traslados base IVA 16:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalTrasladosBaseIVA16()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (totales.getTotalTrasladosBaseIVA8() > 0) {
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph("Total traslados base IVA 8:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(
                                        new Cell().add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalTrasladosBaseIVA8()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (totales.getTotalTrasladosBaseIVAExento() > 0) {
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph("Total traslados base IVA exento:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalTrasladosBaseIVAExento()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (totales.getTotalTrasladosImpuestoIVA0() > 0) {
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph("Total traslados impuestos IVA 0:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalTrasladosImpuestoIVA0()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (totales.getTotalTrasladosImpuestoIVA16() > 0) {
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph("Total traslados impuestos IVA 16:")
                                                        .addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalTrasladosImpuestoIVA16()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (totales.getTotalTrasladosImpuestoIVA8() > 0) {
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph("Total traslados impuestos IVA 8:").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph(
                                                        Utilidades.convertir(totales.getTotalTrasladosImpuestoIVA8()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (totales.getMontoTotalPagos() > 0) {
                        tPagos.addCell(
                                        new Cell().add(new Paragraph("Total monto pagos:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tPagos.addCell(new Cell()
                                        .add(new Paragraph(Utilidades.convertir(totales.getMontoTotalPagos()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                tPagos.startNewRow();
                DOCUMENTO.add(tPagos);

                int contador = 1;
                for (int i = 0; i < pagos.getPago().size(); i++) {
                        com.itextpdf.xmltopdf.Pagos20.Pago pago = pagos.getPago().get(i);
                        PAgregarPago20(pago, contador);
                        AgregarImpuestosP20(pago.getImpuestos());
                        PAgregarDocumentosRelacionados20(pago.getDoctoRelacionado());
                        contador += 1;
                }
        }

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Formato normal">
        private Cell AgregarLogo() {
                Cell celda;
                try {
                        // Path path = Paths.get(RUTA_LOGO);
                        // Files.exists(path)
                        File file = new File(RUTA_LOGO);
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

        private void AgregarEncabezado() {
                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloEncabezado = new Style();
                estiloEncabezado.setFontSize(7);
                estiloEncabezado.setTextAlignment(TextAlignment.RIGHT);

                Style estiloEncabezadoDatosFiscales = new Style();
                estiloEncabezadoDatosFiscales.setFontSize(7);
                estiloEncabezadoDatosFiscales.setTextAlignment(TextAlignment.RIGHT);
                estiloEncabezadoDatosFiscales.setBold();

                // FUENTE_TITULO.Size = 10;
                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 5, 8.5f, 6.5f });
                Table tablaDatos = new Table(anchoColumnas);
                tablaDatos.addCell(AgregarLogo());
                Paragraph prc = new Paragraph();
                prc.setTextAlignment(TextAlignment.CENTER);
                prc.add(new Text(COMPROBANTE.getEmisor().getNombre() + "\n").setFontSize(10).setBold());
                prc.add(new Text(COMPROBANTE.getEmisor().getRfc() + "\n").addStyle(estiloEncabezado));
                if (COMPROBANTE.getVersion().equals("3.2")) {
                        prc.add(new Text("RÉGIMEN FISCAL: "
                                        + COMPROBANTE.getEmisor().getRegimenFiscal()
                                        + "\n").addStyle(estiloEncabezado));

                } else {
                        prc.add(new Text("RÉGIMEN FISCAL: "
                                        + COMPROBANTE.getEmisor().getRegimenFiscal()
                                        + " - "
                                        + Catalogos.ObtenerRegimenFiscal(COMPROBANTE.getEmisor().getRegimenFiscal())
                                        + "\n").addStyle(estiloEncabezado));
                }
                if (!COMPROBANTE.getEmisor().getDomicilioFiscalCompleto().isEmpty()) {
                        prc.add(new Text("DOMICILIO FISCAL: "
                                        + COMPROBANTE.getEmisor().getDomicilioFiscalCompleto()
                                        + "\n").addStyle(estiloEncabezado));
                }
                if (!COMPROBANTE.getEmisor().getExpedidoEnCompleto().isEmpty()) {
                        prc.add(new Text("Expedido En: "
                                        + COMPROBANTE.getEmisor().getExpedidoEnCompleto()
                                        + "\n").addStyle(estiloEncabezado));
                }
                if (!COMPROBANTE.getEmisor().getFacAtrAdquirente().isEmpty()) {
                        prc.add(new Text("NÚM. OPERACIÓN SAT: " + COMPROBANTE.getEmisor().getFacAtrAdquirente() + "\n")
                                        .addStyle(estiloEncabezado));
                }
                prc.add(new Text("\nCLIENTE\n").setTextAlignment(TextAlignment.CENTER).setFontSize(10).setBold());
                prc.add(new Text(COMPROBANTE.getReceptor().getNombre() + "\n").addStyle(estiloEncabezado));
                prc.add(new Text(COMPROBANTE.getReceptor().getRfc() + "\n").addStyle(estiloEncabezado));
                if (!COMPROBANTE.getReceptor().getDomicilioFiscalReceptor().isEmpty()) {
                        prc.add(new Text("DOMICILIO FISCAL: " + COMPROBANTE.getReceptor().getDomicilioFiscalReceptor()
                                        + "\n")
                                        .addStyle(estiloEncabezado));
                }
                if (!COMPROBANTE.getReceptor().getResidenciaFiscal().isEmpty()) {
                        prc.add(new Text("RESIDENCIA FISCAL: " + COMPROBANTE.getReceptor().getResidenciaFiscal() + "\n")
                                        .addStyle(estiloEncabezado));
                }
                if (!COMPROBANTE.getReceptor().getNumRegIdTrib().isEmpty()) {
                        prc.add(new Text("NumRegIdTrib:" + COMPROBANTE.getReceptor().getNumRegIdTrib())
                                        .addStyle(estiloEncabezado));
                }
                if (!COMPROBANTE.getReceptor().getRegimenFiscalReceptor().isEmpty()) {
                        prc.add(new Text("RÉGIMEN FISCAL: "
                                        + COMPROBANTE.getReceptor().getRegimenFiscalReceptor()
                                        + " - "
                                        + Catalogos.ObtenerRegimenFiscal(
                                                        COMPROBANTE.getReceptor().getRegimenFiscalReceptor())
                                        + "\n").addStyle(estiloEncabezado));
                }
                if (!COMPROBANTE.getReceptor().getUsoCFDI().isEmpty()) {
                        prc.add(new Text("USO DE CFDI: "
                                        + COMPROBANTE.getReceptor().getUsoCFDI()
                                        + " - "
                                        + Catalogos.ObtenerUsoCFDI(COMPROBANTE.getReceptor().getUsoCFDI())
                                        + "\n").addStyle(estiloEncabezado));
                }

                Cell celda = new Cell();
                // celda.setHorizontalAlignment(HorizontalAlignment.CENTER);
                celda.setBorder(Border.NO_BORDER);
                celda.setPaddingLeft(12);
                celda.setPaddingRight(12);
                celda.add(prc);
                tablaDatos.addCell(celda);
                anchoColumnas = Utilidades.cmToFloat(new float[] { 6.3f });
                Table tablaDatosFactura = new Table(anchoColumnas);
                tablaDatosFactura.addStyle(estiloCelda);
                tablaDatosFactura
                                .addCell(
                                                new Cell()
                                                                .add(new Paragraph("Factura: " + COMPROBANTE.getSerie()
                                                                                + " " + COMPROBANTE.getFolio())
                                                                                .addStyle(estiloEncabezadoDatosFiscales)
                                                                                .setFontSize(10))
                                                                .addStyle(estiloCelda));
                tablaDatosFactura
                                .addCell(new Cell()
                                                .add(new Paragraph("FOLIO FISCAL (UUID)")
                                                                .addStyle(estiloEncabezadoDatosFiscales))
                                                .addStyle(estiloCelda));
                tablaDatosFactura.addCell(new Cell().add(
                                new Paragraph(COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID())
                                                .addStyle(estiloEncabezado))
                                .addStyle(estiloCelda));
                tablaDatosFactura.addCell(new Cell()
                                .add(new Paragraph("NO. DE SERIE DEL CERTIFICADO DEL SAT")
                                                .addStyle(estiloEncabezadoDatosFiscales))
                                .addStyle(estiloCelda));
                tablaDatosFactura.addCell(
                                new Cell().add(new Paragraph(
                                                COMPROBANTE.getComplemento().TimbreFiscalDigital.getNoCertificadoSAT())
                                                .addStyle(estiloEncabezado)).addStyle(estiloCelda));
                tablaDatosFactura.addCell(new Cell()
                                .add(new Paragraph("NO. DE SERIE DEL CERTIFICADO DEL EMISOR")
                                                .addStyle(estiloEncabezadoDatosFiscales))
                                .addStyle(estiloCelda));
                tablaDatosFactura.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getNoCertificado()).addStyle(estiloEncabezado))
                                .addStyle(estiloCelda));
                tablaDatosFactura.addCell(
                                new Cell().add(new Paragraph("FECHA Y HORA DE CERTIFICACIÓN")
                                                .addStyle(estiloEncabezadoDatosFiscales))
                                                .addStyle(estiloCelda));
                tablaDatosFactura
                                .addCell(new Cell()
                                                .add(new Paragraph(COMPROBANTE.getComplemento().TimbreFiscalDigital
                                                                .getFechaTimbrado()
                                                                .format(DateTimeFormatter.ISO_DATE_TIME))
                                                                .addStyle(estiloEncabezado))
                                                .addStyle(estiloCelda));
                tablaDatosFactura.addCell(
                                new Cell().add(new Paragraph("FECHA Y HORA DE EMISIÓN DE CFDI")
                                                .addStyle(estiloEncabezadoDatosFiscales))
                                                .addStyle(estiloCelda));
                tablaDatosFactura.addCell(new Cell().add(
                                new Paragraph(COMPROBANTE.getFecha().format(DateTimeFormatter.ISO_DATE))
                                                .addStyle(estiloEncabezado))
                                .addStyle(estiloCelda));
                tablaDatosFactura.addCell(
                                new Cell().add(new Paragraph("LUGAR DE EXPEDICIÓN").setBold()
                                                .addStyle(estiloEncabezadoDatosFiscales))
                                                .addStyle(estiloCelda));
                tablaDatosFactura.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getLugarExpedicion()).addStyle(estiloEncabezado))
                                .addStyle(estiloCelda));
                tablaDatos.addCell(new Cell().add(tablaDatosFactura).setBorder(Border.NO_BORDER));
                tablaDatos.startNewRow();
                DOCUMENTO.add(tablaDatos);
        }

        private void AgregarInformacionGlobal() {
                Color color = new DeviceRgb(241, 241, 241);
                Border borde = new SolidBorder(color, 1);
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 3.4f, 3.4F, 3.3f, 3.3f, 3.3f, 3.3f });
                Table tablaProductos = new Table(tamanoColumnas);
                tablaProductos.setMarginTop(7);
                tablaProductos.setBorder(borde);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setBackgroundColor(ColorConstants.BLACK);
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setBorder(borde);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(borde);

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloAtributo = new Style();
                estiloAtributo.setFontSize(7);
                estiloAtributo.setBold();
                estiloAtributo.setTextAlignment(TextAlignment.LEFT);
                estiloAtributo.setFontColor(ColorConstants.BLACK);

                Style estiloValor = new Style();
                estiloValor.setFontSize(7);
                estiloValor.setTextAlignment(TextAlignment.LEFT);
                estiloValor.setFontColor(ColorConstants.BLACK);

                InformacionGlobal informacionGlobal = COMPROBANTE.getInformacionGlobal();

                tablaProductos.addCell(
                                new Cell(1, 6).add(new Paragraph("INFORMACION GLOBAL").addStyle(estiloParrafoTitle))
                                                .addStyle(estiloCeldaTitle));

                if (!informacionGlobal.getPeriodicidad().isEmpty()) {
                        tablaProductos.addCell(
                                        new Cell().add(new Paragraph("Periodicidad").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaProductos.addCell(new Cell()
                                        .add(new Paragraph(informacionGlobal.getPeriodicidad() + " - "
                                                        + Catalogos.ObtenerPeriodicidad(
                                                                        informacionGlobal.getPeriodicidad()))
                                                        .addStyle(estiloValor))
                                        .addStyle(estiloCelda));
                }
                if (!informacionGlobal.getMeses().isEmpty()) {
                        tablaProductos.addCell(new Cell().add(new Paragraph("Mes").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaProductos
                                        .addCell(new Cell()
                                                        .add(new Paragraph(informacionGlobal.getPeriodicidad() + " - "
                                                                        + Catalogos.ObtenerMeses(
                                                                                        informacionGlobal.getMeses()))
                                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                if (informacionGlobal.getAno() > 0) {
                        tablaProductos.addCell(new Cell().add(new Paragraph("Año").addStyle(estiloAtributo))
                                        .addStyle(estiloCelda));
                        tablaProductos.addCell(
                                        new Cell().add(new Paragraph(Integer.toString(informacionGlobal.getAno()))
                                                        .addStyle(estiloValor))
                                                        .addStyle(estiloCelda));
                }
                DOCUMENTO.add(tablaProductos);
        }

        private void AgregarDatosProductos() {
                Color color = new DeviceRgb(241, 241, 241);
                Border borde = new SolidBorder(color, 1);
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 1.4f, 2.4F, 2.4f, 9f, 2.4f, 2.4f });
                Table tablaProductos = new Table(tamanoColumnas);
                tablaProductos.setMarginTop(7);
                tablaProductos.setBorder(borde);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(0);
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(borde);

                Style estiloCeldaTitle = new Style();
                estiloCeldaTitle.setBackgroundColor(ColorConstants.BLACK);
                estiloCeldaTitle.setPaddingTop(0);
                estiloCeldaTitle.setPaddingBottom(0);
                estiloCeldaTitle.setBorder(borde);

                Style estiloCeldaDescripcion = new Style();
                estiloCeldaDescripcion.setPadding(0);
                estiloCeldaDescripcion.setMargin(0);
                estiloCeldaDescripcion.setBorder(Border.NO_BORDER);

                Style estiloParrafoSubtitle = new Style();
                estiloParrafoSubtitle.setFontSize(7);
                estiloParrafoSubtitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafo = new Style();
                estiloParrafo.setFontSize(7);
                estiloParrafo.setTextAlignment(TextAlignment.LEFT);
                estiloParrafo.setFontColor(ColorConstants.BLACK);

                tablaProductos.addCell(
                                new Cell(1, 6).add(new Paragraph("CONCEPTOS").addStyle(estiloParrafoTitle))
                                                .addStyle(estiloCeldaTitle));

                tablaProductos.addCell(new Cell()
                                .add(new Paragraph("Cantidad").addStyle(estiloParrafoSubtitle)
                                                .setTextAlignment(TextAlignment.CENTER))
                                .addStyle(estiloCeldaTitle));
                tablaProductos.addCell(new Cell()
                                .add(new Paragraph("Unidad").addStyle(estiloParrafoSubtitle)
                                                .setTextAlignment(TextAlignment.CENTER))
                                .addStyle(estiloCeldaTitle));
                tablaProductos.addCell(
                                new Cell().add(new Paragraph("No. Identificación").addStyle(estiloParrafoSubtitle)
                                                .setTextAlignment(TextAlignment.CENTER)).addStyle(estiloCeldaTitle));
                tablaProductos.addCell(new Cell().add(new Paragraph("Descripción").addStyle(estiloParrafoSubtitle))
                                .addStyle(estiloCeldaTitle));
                tablaProductos.addCell(new Cell().add(
                                new Paragraph("Precio Unitario").addStyle(estiloParrafoSubtitle)
                                                .setTextAlignment(TextAlignment.RIGHT))
                                .addStyle(estiloCeldaTitle));
                tablaProductos.addCell(new Cell()
                                .add(new Paragraph("Importe").addStyle(estiloParrafoSubtitle)
                                                .setTextAlignment(TextAlignment.RIGHT))
                                .addStyle(estiloCeldaTitle));

                for (int i = 0; i < COMPROBANTE.getConceptos().getConcepto().size(); i++) {
                        Concepto concepto = COMPROBANTE.getConceptos().getConcepto().get(i);
                        StringBuilder descripcion = new StringBuilder();
                        tablaProductos.addCell(new Cell()
                                        .add(new Paragraph(Utilidades.convertir(concepto.getCantidad()))
                                                        .addStyle(estiloParrafo).setTextAlignment(TextAlignment.CENTER))
                                        .addStyle(estiloCelda));
                        if (COMPROBANTE.getVersion().equals("3.2")) {
                                tablaProductos.addCell(new Cell()
                                                .add(new Paragraph(concepto.getUnidad()).addStyle(estiloParrafo)
                                                                .setTextAlignment(TextAlignment.CENTER))
                                                .addStyle(estiloCelda));
                        } else {
                                tablaProductos.addCell(new Cell()
                                                .add(new Paragraph(concepto.getClaveUnidad() + "-"
                                                                + Catalogos.ObtenerUnidad(concepto.getClaveUnidad()))
                                                                .addStyle(estiloParrafo)
                                                                .setTextAlignment(TextAlignment.CENTER))
                                                .addStyle(estiloCelda));
                        }
                        tablaProductos.addCell(new Cell()
                                        .add(new Paragraph(concepto.getNoIdentificacion()).addStyle(estiloParrafo)
                                                        .setTextAlignment(TextAlignment.CENTER))
                                        .addStyle(estiloCelda));
                        if (COMPROBANTE.getVersion().equals("3.2")) {
                                tablaProductos.addCell(new Cell()
                                                .add(new Paragraph(concepto.getDescripcion()).addStyle(estiloParrafo))
                                                .addStyle(estiloCelda));
                        } else {
                                descripcion.append(concepto.getDescripcion());
                                descripcion.append("\nClave Prod. Serv.: ");
                                descripcion.append(concepto.getClaveProdServ());
                                if (concepto.getInformacionAduanera().size() > 0) {
                                        descripcion.append("\nInformación aduanera");
                                        for (int j = 0; j < concepto.getInformacionAduanera().size(); j++) {
                                                InformacionAduaneraC info = new InformacionAduaneraC();
                                                info = concepto.getInformacionAduanera().get(j);
                                                descripcion.append("\nNúmero de pedimiento: ");
                                                descripcion.append(info.getNumeroPedimento());
                                        }
                                }
                                if (concepto.getParte().size() > 0) {
                                        descripcion.append("\nPartes:");
                                        for (int j = 0; j < concepto.getParte().size(); j++) {
                                                ParteC parte = concepto.getParte().get(j);
                                                descripcion.append("\nParte: ");
                                                descripcion.append(parte.getDescripcion());
                                                descripcion.append("\nClave Prod. Serv.: ");
                                                descripcion.append(concepto.getClaveProdServ());
                                                descripcion.append(" No. Identificación: ");
                                                descripcion.append(concepto.getNoIdentificacion());
                                                descripcion.append("\nCantidad: ");
                                                descripcion.append(concepto.getCantidad());
                                                descripcion.append(" Unidad: ");
                                                descripcion.append(concepto.getUnidad());
                                                descripcion.append(" Precio unitario: ");
                                                descripcion.append(Utilidades.convertir(concepto.getValorUnitario()));
                                                descripcion.append(" Importe: ");
                                                descripcion.append(Utilidades.convertir(concepto.getImporte()));
                                                for (int k = 0; k < parte.getInformacionAduanera().size(); k++) {
                                                        InformacionAduaneraC infoP = parte.getInformacionAduanera()
                                                                        .get(k);
                                                        descripcion.append("\nNúmero de pedimiento: ");
                                                        descripcion.append(infoP.getNumeroPedimento());
                                                }
                                                // descripcion.append("\n");
                                        }
                                }
                                // Solo es aplicable a la version 4.0 y es obligaatorio
                                if (!concepto.getObjetoImp().isEmpty()) {
                                        descripcion.append("\nObjeto de impuesto: ");
                                        descripcion.append(concepto.getObjetoImp() + " - "
                                                        + Catalogos.ObtenerObjetoImp(concepto.getObjetoImp()));
                                }
                                // descripcion.append("\n");
                                if (concepto.getImpuestos() != null) {
                                        descripcion.append("\nImpuestos:\n");
                                        if (concepto.getImpuestos().getTraslados().size() > 0) {
                                                descripcion.append("Traslados:\n");
                                                for (int j = 0; j < concepto.getImpuestos().getTraslados()
                                                                .size(); j++) {
                                                        TrasladoC t = concepto.getImpuestos().getTraslados().get(j);
                                                        descripcion.append("");
                                                        descripcion.append(t.getImpuesto());
                                                        descripcion.append(" - ");
                                                        descripcion.append(Catalogos.ObtenerImpuesto(t.getImpuesto()));
                                                        descripcion.append(" | Base - ");
                                                        descripcion.append(Utilidades.convertir(t.getBase()));
                                                        descripcion.append(" | Tasa - ");
                                                        descripcion.append(String.format("%.6f", t.getTasaOCuota())); // a
                                                                                                                      // 6
                                                                                                                      // decimales
                                                        descripcion.append(" | Importe - ");
                                                        descripcion.append(Utilidades.convertir(t.getImporte()));
                                                        descripcion.append("\n");
                                                }
                                        }
                                }
                                tablaProductos.addCell(new Cell()
                                                .add(new Paragraph(descripcion.toString()).addStyle(estiloParrafo))
                                                .addStyle(estiloCelda));
                        }
                        tablaProductos.addCell(new Cell()
                                        .add(new Paragraph(Utilidades.convertir(concepto.getValorUnitario()))
                                                        .addStyle(estiloParrafo).setTextAlignment(TextAlignment.RIGHT))
                                        .addStyle(estiloCelda));
                        tablaProductos.addCell(new Cell().add(new Paragraph(Utilidades.convertir(concepto.getImporte()))
                                        .addStyle(estiloParrafo).setTextAlignment(TextAlignment.RIGHT))
                                        .addStyle(estiloCelda));
                }
                if (!COMPROBANTE.getTipoDeComprobante().equals("P")
                                && !COMPROBANTE.getTipoDeComprobante().equals("N")) {
                        IRenderer tableRenderer = tablaProductos.createRendererSubTree()
                                        .setParent(DOCUMENTO.getRenderer());
                        LayoutResult tableLayoutResult = tableRenderer
                                        .layout(new LayoutContext(new LayoutArea(0, new Rectangle(1000, 1000))));
                        float tableHeightTotal = tableLayoutResult.getOccupiedArea().getBBox().getHeight();

                        if (tableHeightTotal < 340) {
                                float total = 340 - tableHeightTotal;
                                Cell celdaVacia = new Cell(1, 6);
                                celdaVacia.setHeight(total);
                                celdaVacia.add(new Paragraph(""));
                                celdaVacia.setBorder(new SolidBorder(ColorConstants.WHITE, 1));
                                celdaVacia.setBorderTop(new SolidBorder(ColorConstants.BLACK, 1));
                                tablaProductos.addCell(celdaVacia);
                        }
                }
                DOCUMENTO.add(tablaProductos);
        }

        private String ObtenerTotalEnLetra(double total) {
                return NumberToLetterConvert.convertNumberToLetter(total).toUpperCase();
                // NumberToLetterConvert numaLet =
                // NumberToLetterConvert.convertNumberToLetter(total);
                // numaLet.MascaraSalidaDecimal = "00/100 M.N.";
                // numaLet.SeparadorDecimalSalida = "pesos";
                // numaLet.ApocoparUnoParteEntera = true;
                // numaLet.LetraCapital = true;
                // return numaLet.ToCustomString(total).ToUpper();
        }

        private void AgregarTotales() {
                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(1);
                estiloCelda.setPaddingBottom(1);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloMoneda = new Style();
                estiloMoneda.setFontSize(7);
                estiloMoneda.setTextAlignment(TextAlignment.RIGHT);

                Style estiloAtributo = new Style();
                estiloAtributo.setBold();
                estiloAtributo.setFontSize(7);

                Style estiloDescripcion = new Style();
                estiloDescripcion.setFontSize(7);

                float[] anchoColumasTablaTotales = Utilidades.cmToFloat(new float[] { 13.5f, 6.5f });
                Table tablaTotales = new Table(anchoColumasTablaTotales);
                tablaTotales.setMarginTop(7);
                tablaTotales.setHorizontalAlignment(HorizontalAlignment.CENTER);

                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 4f, 2.5f });
                Table tablaImportes = new Table(anchoColumnas);

                float[] columnasLetra = Utilidades.cmToFloat(new float[] { 3.3f, 3.1f, 2.6f, 4f });
                Table tablaImporteLetra = new Table(columnasLetra);

                if (COMPROBANTE.getDescuento() > 0) {
                        // double subtotal = COMPROBANTE.getSubTotal() + COMPROBANTE.getDescuento();
                        tablaImportes
                                        .addCell(new Cell().add(new Paragraph("SUBTOTAL:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaImportes
                                        .addCell(new Cell()
                                                        .add(new Paragraph(
                                                                        Utilidades.convertir(COMPROBANTE.getSubTotal()))
                                                                        .addStyle(estiloMoneda))
                                                        .addStyle(estiloCelda));
                        tablaImportes.addCell(
                                        new Cell().add(new Paragraph("DESCUENTO:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaImportes
                                        .addCell(new Cell()
                                                        .add(new Paragraph(Utilidades
                                                                        .convertir(COMPROBANTE.getDescuento()))
                                                                        .addStyle(estiloMoneda))
                                                        .addStyle(estiloCelda));
                }
                tablaImportes
                                .addCell(new Cell().add(new Paragraph("SUBTOTAL:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaImportes.addCell(new Cell()
                                .add(new Paragraph(Utilidades.convertir(COMPROBANTE.getSubTotal()))
                                                .addStyle(estiloMoneda))
                                .addStyle(estiloCelda));
                
                
                if (COMPROBANTE.getImpuestos() != null) {
                        if (COMPROBANTE.getImpuestos().getTraslados() != null) {
                                for (int i = 0; i < COMPROBANTE.getImpuestos().getTraslados().size(); i++) {
                                        Traslado t = COMPROBANTE.getImpuestos().getTraslados().get(i);
                                        
                                        tablaImportes.addCell(new Cell()
                                                        .add(new Paragraph("TRASLADO "
                                                                        + Catalogos.ObtenerImpuesto(t.getImpuesto())
                                                                        + " TASA "
                                                                        + String.format("%.6f", t.getTasaOCuota()))
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaImportes
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(Utilidades
                                                                                        .convertir(t.getImporte()))
                                                                                        .addStyle(estiloMoneda))
                                                                        .addStyle(estiloCelda));
                                }
                        }
                        if (COMPROBANTE.getImpuestos().getRetenciones() != null) {
                                for (int j = 0; j < COMPROBANTE.getImpuestos().getRetenciones().size(); j++) {
                                        Retencion r = COMPROBANTE.getImpuestos().getRetenciones().get(j);
                                        tablaImportes.addCell(new Cell().add(
                                                        new Paragraph("RETENCIÓN "
                                                                        + Catalogos.ObtenerImpuesto(r.getImpuesto()))
                                                                        .addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                                        tablaImportes
                                                        .addCell(new Cell()
                                                                        .add(new Paragraph(Utilidades
                                                                                        .convertir(r.getImporte()))
                                                                                        .addStyle(estiloMoneda))
                                                                        .addStyle(estiloCelda));
                                }
                        }
                }
                tablaImportes.addCell(
                                new Cell().add(new Paragraph("TOTAL:").addStyle(estiloAtributo)).addStyle(estiloCelda));
                tablaImportes.addCell(
                                new Cell().add(new Paragraph(Utilidades.convertir(COMPROBANTE.getTotal()))
                                                .addStyle(estiloMoneda))
                                                .addStyle(estiloCelda));
                tablaImporteLetra.addCell(
                                new Cell().add(new Paragraph("IMPORTE CON LETRA: ").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaImporteLetra.addCell(new Cell(1, 3)
                                .add(new Paragraph(ObtenerTotalEnLetra(COMPROBANTE.getTotal()) + " "  + COMPROBANTE.getMoneda())
                                                .addStyle(estiloDescripcion))
                                .addStyle(estiloCelda));
                // logger.info("Total con Letra...."+ObtenerTotalEnLetra(COMPROBANTE.getTotal()));
                
                // tablaImporteLetra.addCell(celtaTotalLetra);
                // tablaImporteLetra.addCell(new Cell().add(new Paragraph("
                // ").addStyle(estiloDescripcion)).addStyle(estiloCelda));
                tablaImporteLetra.addCell(
                                new Cell().add(new Paragraph("TIPO DE COMPROBANTE:").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                if (COMPROBANTE.getVersion().equals("3.2")) {
                        tablaImporteLetra.addCell(
                                        new Cell().add(new Paragraph(COMPROBANTE.getTipoDeComprobante())
                                                        .addStyle(estiloDescripcion))
                                                        .addStyle(estiloCelda));
                } else {
                        tablaImporteLetra.addCell(new Cell()
                                        .add(new Paragraph(COMPROBANTE.getTipoDeComprobante() + " - "
                                                        + Catalogos.ObtenerTipoComprobante(
                                                                        COMPROBANTE.getTipoDeComprobante()))
                                                        .addStyle(estiloDescripcion))
                                        .addStyle(estiloCelda));
                }
                if (!COMPROBANTE.getExportacion().isEmpty()) {
                        tablaImporteLetra.addCell(
                                        new Cell().add(new Paragraph("EXPORTACIÓN:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        tablaImporteLetra
                                        .addCell(new Cell()
                                                        .add(new Paragraph(COMPROBANTE.getExportacion() + " - "
                                                                        + Catalogos.ObtenerExportacion(
                                                                                        COMPROBANTE.getExportacion()))
                                                                        .addStyle(estiloDescripcion))
                                                        .addStyle(estiloCelda));
                } else {
                        tablaImporteLetra
                                        .addCell(new Cell(1, 2).add(new Paragraph("").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                }

                if (!COMPROBANTE.getFormaPago().isEmpty()) {
                        tablaImporteLetra.addCell(
                                        new Cell().add(new Paragraph("FORMA DE PAGO:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        if (COMPROBANTE.getVersion().equals("3.2")) {
                                tablaImporteLetra
                                                .addCell(new Cell()
                                                                .add(new Paragraph(COMPROBANTE.getFormaPago())
                                                                                .addStyle(estiloDescripcion))
                                                                .addStyle(estiloCelda));
                        } else {
                                tablaImporteLetra
                                                .addCell(new Cell()
                                                                .add(new Paragraph(COMPROBANTE.getFormaPago() + " - "
                                                                                + Catalogos.ObtenerFormaPago(COMPROBANTE
                                                                                                .getFormaPago()))
                                                                                .addStyle(estiloDescripcion))
                                                                .addStyle(estiloCelda));
                        }

                }
                if (!COMPROBANTE.getMetodoPago().isEmpty()) {
                        tablaImporteLetra.addCell(
                                        new Cell().add(new Paragraph("MÉTODO DE PAGO:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        if (COMPROBANTE.getVersion().equals("3.2")) {
                                tablaImporteLetra
                                                .addCell(new Cell()
                                                                .add(new Paragraph(COMPROBANTE.getMetodoPago())
                                                                                .addStyle(estiloDescripcion))
                                                                .addStyle(estiloCelda));
                        } else {
                                tablaImporteLetra
                                                .addCell(new Cell()
                                                                .add(new Paragraph(COMPROBANTE.getMetodoPago() + " - "
                                                                                + Catalogos.ObtenerMetodoPago(
                                                                                                COMPROBANTE
                                                                                                                .getMetodoPago()))
                                                                                .addStyle(estiloDescripcion))
                                                                .addStyle(estiloCelda));

                        }
                }
                if (!COMPROBANTE.getMoneda().isEmpty()) {
                        tablaImporteLetra
                                        .addCell(new Cell().add(new Paragraph("MONEDA:").addStyle(estiloAtributo))
                                                        .addStyle(estiloCelda));
                        if (COMPROBANTE.getVersion().equals("3.2")) {
                                tablaImporteLetra.addCell(new Cell()
                                                .add(new Paragraph(COMPROBANTE.getMoneda()).addStyle(estiloDescripcion))
                                                .addStyle(estiloCelda));
                        } else {
                                tablaImporteLetra.addCell(new Cell()
                                                .add(new Paragraph(COMPROBANTE.getMoneda() + " - "
                                                                + Catalogos.ObtenerMoneda(COMPROBANTE.getMoneda()))
                                                                .addStyle(estiloDescripcion))
                                                .addStyle(estiloCelda));
                        }
                }

                tablaImporteLetra.startNewRow();

                tablaTotales.addCell(
                                new Cell().add(tablaImporteLetra).addStyle(estiloCelda));
                tablaTotales.addCell(
                                new Cell().add(tablaImportes).addStyle(estiloCelda));
                DOCUMENTO.add(tablaTotales);
        }

        private void AgregarSellos() {
                Style estiloCelda = new Style();
                estiloCelda.setPaddingTop(1);
                estiloCelda.setPaddingBottom(1);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloAtributo = new Style();
                estiloAtributo.setBold();
                estiloAtributo.setFontSize(7);

                Style estiloDescripcion = new Style();
                estiloDescripcion.setFontSize(6);

                // Agregamos el codigo QR al documento
                StringBuilder codigoQR = new StringBuilder();
                codigoQR.append("https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx?"); // La URL del
                                                                                                     // acceso al
                                                                                                     // servicio que
                                                                                                     // pueda
                                                                                                     // mostrar los
                                                                                                     // datos de la
                                                                                                     // versión pública
                                                                                                     // del
                                                                                                     // COMPROBANTE.
                codigoQR.append("&id=" + COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID()); // UUID del
                                                                                                      // COMPROBANTE
                codigoQR.append("&re=" + COMPROBANTE.getEmisor().getRfc()); // RFC del Emisor
                codigoQR.append("&rr=" + COMPROBANTE.getReceptor().getRfc()); // RFC del receptor
                codigoQR.append("&tt=" + String.format("%.2f", COMPROBANTE.getTotal())); // Total del COMPROBANTE 10
                                                                                         // enteros y 6
                                                                                         // decimales
                codigoQR.append("&fe=" + COMPROBANTE.getSello().substring(COMPROBANTE.getSello().length() - 8)); // Total
                                                                                                                 // del
                                                                                                                 // COMPROBANTE
                                                                                                                 // 10
                                                                                                                 // enteros
                                                                                                                 // y
                                                                                                                 // 6
                                                                                                                 // decimales

                BarcodeQRCode pdfCodigoQR = new BarcodeQRCode(codigoQR.toString(), null);
                Image img = new Image(pdfCodigoQR.createFormXObject(ColorConstants.BLACK, 2, DOCUMENTO_PDF));
                img.scaleAbsolute(Utilidades.cmToFloat(3.4f), Utilidades.cmToFloat(3.4f));
                // img.SpacingAfter = 0.0f;
                // img.SpacingBefore = 0.0f;
                // img.BorderWidth = 0.0f;
                img.setHorizontalAlignment(HorizontalAlignment.LEFT);
                StringBuilder cadenaOriginal = new StringBuilder();
                cadenaOriginal.append("||");
                cadenaOriginal.append("1.0|");
                cadenaOriginal.append(COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID() + "|");
                cadenaOriginal.append(COMPROBANTE.getComplemento().TimbreFiscalDigital.getFechaTimbrado()
                                .format(DateTimeFormatter.ISO_DATE_TIME) + "|");
                cadenaOriginal.append(COMPROBANTE.getSello() + "|");
                cadenaOriginal.append(COMPROBANTE.getSerie() + "||");
                float[] anchoColumnas = Utilidades.cmToFloat(new float[] { 3.5f, 16.5f });
                Table tablaSellosQR = new Table(anchoColumnas);
                tablaSellosQR.setMarginTop(7);
                // tablaSellosQR.DefaultCell.HorizontalAlignment = Element.ALIGN_LEFT;
                tablaSellosQR.setFixedLayout();
                tablaSellosQR.setMarginTop(7);
                Cell celdaimagen = new Cell();
                celdaimagen.setBorder(Border.NO_BORDER);
                celdaimagen.setVerticalAlignment(VerticalAlignment.TOP);
                celdaimagen.setPaddingTop(0);
                celdaimagen.add(img);
                celdaimagen.setHeight(Utilidades.cmToFloat(3.5f));
                tablaSellosQR.addCell(celdaimagen);
                float[] anchoColumnas1 = Utilidades.cmToFloat(new float[] { 16.5f });
                Table tablaSellos = new Table(anchoColumnas1);
                tablaSellos.setFixedLayout();
                tablaSellos.setWidth(Utilidades.cmToFloat(16.5f));
                // tablaSellos.seth(anchoColumnas1);
                tablaSellos.setHorizontalAlignment(HorizontalAlignment.CENTER);
                tablaSellos.addCell(new Cell().add(new Paragraph("SELLO DIGITAL DEL CFDI:").addStyle(estiloAtributo))
                                .addStyle(estiloCelda));
                tablaSellos.addCell(new Cell().add(new Paragraph(COMPROBANTE.getSello()).addStyle(estiloDescripcion))
                                .addStyle(estiloCelda));
                tablaSellos.addCell(
                                new Cell().add(new Paragraph("SELLO DIGITAL DEL SAT").addStyle(estiloAtributo))
                                                .addStyle(estiloCelda));
                tablaSellos.addCell(new Cell().add(new Paragraph(COMPROBANTE.getSello()).addStyle(estiloDescripcion))
                                .addStyle(estiloCelda));
                tablaSellos.addCell(
                                new Cell().add(new Paragraph(
                                                "CADENA ORIGINAL DEL COMPLEMENTO DE CERTIFICACIÓN DIGITAL DEL SAT:")
                                                .addStyle(estiloAtributo)).addStyle(estiloCelda));
                tablaSellos.addCell(new Cell().add(new Paragraph(cadenaOriginal.toString()).addStyle(estiloDescripcion))
                                .addStyle(estiloCelda));
                tablaSellosQR.addCell(new Cell().add(tablaSellos).setBorder(Border.NO_BORDER));
                DOCUMENTO.add(tablaSellosQR);
        }
}
