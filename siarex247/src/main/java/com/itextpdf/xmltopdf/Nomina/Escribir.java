package com.itextpdf.xmltopdf.Nomina;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.BorderCollapsePropertyValue;
import com.itextpdf.layout.properties.BorderRadius;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.xmltopdf.Catalogos;
import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.Concepto;
import com.itextpdf.xmltopdf.NumberToLetterConvert;
import com.itextpdf.xmltopdf.Utilidades;

public class Escribir {
        private Comprobante COMPROBANTE;
        private Document DOCUMENTO;
        private PdfDocument DOCUMENTO_PDF;

        public Escribir(Comprobante comprobante, Document documento, PdfDocument documentoPDF) {
                this.COMPROBANTE = comprobante;
                this.DOCUMENTO = documento;
                this.DOCUMENTO_PDF = documentoPDF;

        }

        public void AgregarDatos() {
                DOCUMENTO.add(NAgregarTitulo(COMPROBANTE.getComplemento().Nomina.getReceptor().getPeriodicidadPago(),
                                COMPROBANTE.getComplemento().Nomina.getFechaFinalPago(),
                                COMPROBANTE.getComplemento().Nomina.getFechaFinalPago()));
                // Table tablaDatos = new Table(columnas);
                DOCUMENTO.add(NAgregarDatosLaborales());
                // DOCUMENTO.add(NAgregarEmisor());
                DOCUMENTO.add(NAgregarConceptos());
                // tablaDatos.addCell(NAgregarPercepcionesDeducciones());
                // DOCUMENTO.add(tablaDatos);

                DOCUMENTO.add(NAgregarPercepcionesDeducciones());
                DOCUMENTO.add(NAgregarTotal(COMPROBANTE.getTotal()));

                // DOCUMENTO.add(NAgregarDatosFiscales());
                DOCUMENTO.add(NAgregarSellos());
                DOCUMENTO.add(NAgregarMensaje());

        }

        private IBlockElement NAgregarTitulo(String periodicidadPago, LocalDate fechaInicialPago,
                        LocalDate fechaFinalPago) {
                Color borderColor = new DeviceRgb(142, 175, 223);

                Style spTitulo = new Style();
                spTitulo.setBold();
                spTitulo.setPadding(0);
                spTitulo.setFontSize(8);

                Style spMensaje = new Style();
                spMensaje.setFontSize(8);
                spMensaje.setPadding(0);

                Style sc = new Style();
                sc.setPaddingTop(0);
                sc.setPaddingBottom(0);
                sc.setBorder(Border.NO_BORDER);

                Style scTitulo = new Style();
                scTitulo.setPaddingTop(0);
                scTitulo.setPaddingBottom(0);
                scTitulo.setTextAlignment(TextAlignment.RIGHT);
                scTitulo.setBorder(Border.NO_BORDER);

                float[] columnas = Utilidades.cmToFloat(new float[] { 13f, 7f });
                Table tabla = new Table(columnas);
                Table tablaDatosFactura = new Table(Utilidades.cmToFloat(new float[] { 3f, 4f }));
                Table tablaReceptor = new Table(Utilidades.cmToFloat(new float[] { 2, 4.5f, 2f, 4.5f }));

                Text pNombre = new Text(COMPROBANTE.getEmisor().getNombre() + "\n").setFontSize(14);
                Text pRfc = new Text(COMPROBANTE.getEmisor().getRfc() + "\n"
                                + COMPROBANTE.getEmisor().getRegimenFiscal()
                                + Catalogos.ObtenerRegimenFiscal(COMPROBANTE.getEmisor().getRegimenFiscal())
                                + "\n");
                pRfc.setFontSize(8);

                Paragraph parrafo = new Paragraph();
                parrafo.add(pNombre);
                parrafo.add(pRfc);

                Cell celdaEmisor = new Cell();
                celdaEmisor.setBorder(Border.NO_BORDER);
                celdaEmisor.setTextAlignment(TextAlignment.RIGHT);
                celdaEmisor.add(parrafo);
                // Div divCaption = new Div();
                // divCaption.add(new Paragraph("Emisión").setFontSize(8));
                // divCaption.setBackgroundColor(borderColor);
                // divCaption.setMarginTop(0);
                // divCaption.setPaddingTop(0);
                // tablaDatosFactura.setCaption(divCaption, CaptionSide.TOP);

                Cell celdaFolio = new Cell(1, 2);
                celdaFolio.add(new Paragraph(COMPROBANTE.getSerie() + COMPROBANTE.getFolio()));
                celdaFolio.setFontSize(10);
                celdaFolio.setFontColor(ColorConstants.RED);
                celdaFolio.setBorder(Border.NO_BORDER);
                celdaFolio.setTextAlignment(TextAlignment.CENTER);
                tablaDatosFactura.addCell(celdaFolio);

                Cell celdaTituloFolioFiscal = new Cell(1, 2);
                celdaTituloFolioFiscal.setFontSize(8);
                celdaTituloFolioFiscal.setBold();
                celdaTituloFolioFiscal.setPadding(0);
                celdaTituloFolioFiscal.add(new Paragraph("Folio fiscal"));
                celdaTituloFolioFiscal.setBorder(Border.NO_BORDER);
                celdaTituloFolioFiscal.setTextAlignment(TextAlignment.CENTER);
                tablaDatosFactura.addCell(celdaTituloFolioFiscal);

                Cell celdaFolioFiscal = new Cell(1, 2);
                celdaFolioFiscal.setFontSize(8);
                celdaFolioFiscal.setPadding(0);
                celdaFolioFiscal.add(new Paragraph(COMPROBANTE.getComplemento().getTimbreFiscalDigital().getUUID()));
                celdaFolioFiscal.setBorder(Border.NO_BORDER);
                celdaFolioFiscal.setTextAlignment(TextAlignment.CENTER);

                SolidBorder sb = new SolidBorder(borderColor, .8f);

                tablaDatosFactura.setBorder(new SolidBorder(borderColor, .6f));
                tablaDatosFactura.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
                tablaDatosFactura.setBorderRadius(new BorderRadius(5));

                tablaDatosFactura.addCell(celdaFolioFiscal);
                tablaDatosFactura.addCell(new Cell().add(new Paragraph("Emisión").addStyle(spTitulo)).addStyle(sc));
                tablaDatosFactura.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getFecha().format(DateTimeFormatter.ISO_DATE_TIME))
                                                .addStyle(spMensaje))
                                .addStyle(sc));
                tablaDatosFactura.addCell(new Cell().add(new Paragraph("Expedido en").addStyle(spTitulo)).addStyle(sc));
                tablaDatosFactura.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getLugarExpedicion()).addStyle(spMensaje)).addStyle(sc));
                tablaDatosFactura.addCell(new Cell().add(new Paragraph("No. CSD emisor").addStyle(spTitulo))
                                .addStyle(sc).setBorder(Border.NO_BORDER));
                tablaDatosFactura.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getNoCertificado()).addStyle(spMensaje)).addStyle(sc));
                tablaDatosFactura.addCell(new Cell().add(new Paragraph("Moneda").addStyle(spTitulo)).addStyle(sc)
                                .setBorder(Border.NO_BORDER));
                tablaDatosFactura.addCell(new Cell().add(new Paragraph(COMPROBANTE.getMoneda()).addStyle(spMensaje))
                                .addStyle(sc));

                Cell celdaNombre = new Cell(1, 3);
                celdaNombre.setFontSize(8);
                celdaNombre.setPadding(0);
                celdaNombre.add(new Paragraph(COMPROBANTE.getReceptor().getNombre()));
                celdaNombre.setBorder(Border.NO_BORDER);

                tablaReceptor.setBorder(new SolidBorder(borderColor, .8f));
                tablaReceptor.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
                tablaReceptor.setBorderRadius(new BorderRadius(5));

                tablaReceptor.addCell(new Cell().add(new Paragraph("Empleado").addStyle(spTitulo)).addStyle(scTitulo));
                tablaReceptor.addCell(celdaNombre);
                tablaReceptor.addCell(new Cell().add(new Paragraph("RFC").addStyle(spTitulo)).addStyle(scTitulo));
                tablaReceptor.addCell(
                                new Cell().add(new Paragraph(COMPROBANTE.getReceptor().getRfc()).addStyle(spMensaje))
                                                .addStyle(sc));
                tablaReceptor.addCell(
                                new Cell().add(new Paragraph("Forma pago").addStyle(spTitulo)).addStyle(scTitulo));
                tablaReceptor.addCell(new Cell().add(new Paragraph(COMPROBANTE.getFormaPago() + " "
                                + Catalogos.ObtenerFormaPago(COMPROBANTE.getFormaPago())).addStyle(spMensaje))
                                .addStyle(sc));
                tablaReceptor.addCell(
                                new Cell().add(new Paragraph("Método pago").addStyle(spTitulo)).addStyle(scTitulo));
                tablaReceptor.addCell(new Cell().add(new Paragraph(COMPROBANTE.getMetodoPago() + " "
                                + Catalogos.ObtenerMetodoPago(COMPROBANTE.getMetodoPago())).addStyle(spMensaje))
                                .addStyle(sc));
                tablaReceptor.addCell(new Cell().add(new Paragraph("Uso CFDI").addStyle(spTitulo)).addStyle(scTitulo));
                tablaReceptor.addCell(new Cell().add(new Paragraph(COMPROBANTE.getReceptor().getUsoCFDI() + " "
                                + Catalogos.ObtenerUsoCFDI(COMPROBANTE.getReceptor().getUsoCFDI())).addStyle(spMensaje))
                                .addStyle(sc));

                tabla.addCell(celdaEmisor);
                Cell celdaDatosFactura = new Cell(2, 1);
                celdaDatosFactura.setBorder(Border.NO_BORDER);
                celdaDatosFactura.add(tablaDatosFactura);

                Cell celdaDatosReceptor = new Cell(1, 1);
                celdaDatosReceptor.setVerticalAlignment(VerticalAlignment.BOTTOM);
                celdaDatosReceptor.setBorder(Border.NO_BORDER);
                celdaDatosReceptor.add(tablaReceptor);

                tabla.addCell(celdaDatosFactura);
                tabla.addCell(celdaDatosReceptor);

                return tabla;
        }

        private Table NAgregarDatosLaborales() {
                Color borderColor = new DeviceRgb(142, 175, 223);

                Style spTitulo = new Style();
                spTitulo.setBold();
                spTitulo.setPadding(0);
                spTitulo.setFontSize(8);

                Style spMensaje = new Style();
                spMensaje.setFontSize(8);
                spMensaje.setPadding(0);

                Style sc = new Style();
                sc.setTextAlignment(TextAlignment.CENTER);
                sc.setPaddingTop(0);
                sc.setPaddingBottom(0);
                sc.setBorder(Border.NO_BORDER);

                Style scTitulo = new Style();
                scTitulo.setPaddingTop(0);
                scTitulo.setPaddingBottom(0);
                scTitulo.setTextAlignment(TextAlignment.CENTER);
                scTitulo.setBorder(Border.NO_BORDER);

                Style scConcepto = new Style();
                scConcepto.setPaddingTop(0);
                scConcepto.setPaddingBottom(0);
                scConcepto.setTextAlignment(TextAlignment.CENTER);
                scConcepto.setBorder(Border.NO_BORDER);

                float[] columnas = Utilidades.cmToFloat(new float[] { 2f, 3.3f, 2.5f, 1.5f, 1, 2, 2.3f, 3.5f, 1.8f });
                Table tabla = new Table(columnas);
                tabla.setBorder(new SolidBorder(borderColor, .8f));
                tabla.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
                tabla.setBorderRadius(new BorderRadius(5));

                Nomina nomina = COMPROBANTE.getComplemento().getNomina();
                tabla.addCell(new Cell().add(new Paragraph("No. empleado").addStyle(spTitulo)).addStyle(scTitulo));
                tabla.addCell(new Cell().add(new Paragraph("CURP").addStyle(spTitulo)).addStyle(scTitulo));
                tabla.addCell(new Cell().add(new Paragraph("IMSS").addStyle(spTitulo)).addStyle(scTitulo));
                tabla.addCell(new Cell().add(new Paragraph("Pago").addStyle(spTitulo)).addStyle(scTitulo));
                tabla.addCell(new Cell().add(new Paragraph("Días").addStyle(spTitulo)).addStyle(scTitulo));
                tabla.addCell(new Cell().add(new Paragraph("Salario diario").addStyle(spTitulo)).addStyle(scTitulo));
                tabla.addCell(new Cell().add(new Paragraph("Fecha de pago").addStyle(spTitulo)).addStyle(scTitulo));
                tabla.addCell(new Cell().add(new Paragraph("Del - periodo - al").addStyle(spTitulo))
                                .addStyle(scTitulo));
                tabla.addCell(new Cell().add(new Paragraph("Ingreso").addStyle(spTitulo)).addStyle(scTitulo));

                tabla.addCell(
                                new Cell().add(new Paragraph(nomina.getReceptor().getNumEmpleado()).addStyle(spMensaje))
                                                .addStyle(sc));
                tabla.addCell(new Cell().add(new Paragraph(nomina.getReceptor().getCurp()).addStyle(spMensaje))
                                .addStyle(sc));
                tabla.addCell(new Cell()
                                .add(new Paragraph(nomina.getReceptor().getNumSeguridadSocial()).addStyle(spMensaje))
                                .addStyle(sc));
                tabla.addCell(
                                new Cell().add(new Paragraph(nomina.getReceptor().getTipoJornada()).addStyle(spMensaje))
                                                .addStyle(sc));
                tabla.addCell(
                                new Cell().add(new Paragraph(String.format("%.3f", nomina.getNumDiasPagados()))
                                                .addStyle(spMensaje))
                                                .addStyle(sc));
                tabla.addCell(
                                new Cell().add(new Paragraph(String.format("$%.2f",
                                                nomina.getReceptor().getSalarioDiarioIntegrado()))
                                                .addStyle(spMensaje)).addStyle(sc));
                tabla.addCell(new Cell()
                                .add(new Paragraph(nomina.getFechaPago().format(DateTimeFormatter.ISO_DATE))
                                                .addStyle(spMensaje))
                                .addStyle(sc));
                tabla.addCell(new Cell()
                                .add(new Paragraph(
                                                nomina.getFechaInicialPago().format(DateTimeFormatter.ISO_DATE) + " - "
                                                                + nomina.getFechaFinalPago()
                                                                                .format(DateTimeFormatter.ISO_DATE))
                                                .addStyle(spMensaje))
                                .addStyle(sc));
                tabla.addCell(new Cell()
                                .add(new Paragraph(nomina.getReceptor().getFechaInicioRelLaboral()
                                                .format(DateTimeFormatter.ISO_DATE))
                                                .addStyle(spMensaje))
                                .addStyle(sc));

                tabla.addCell(new Cell(1, 4).add(new Paragraph("Tipo de contracto").addStyle(spTitulo))
                                .addStyle(scTitulo));
                tabla.addCell(new Cell(1, 3).add(new Paragraph("Departamento").addStyle(spTitulo)).addStyle(scTitulo));
                tabla.addCell(new Cell(1, 2).add(new Paragraph("Puesto").addStyle(spTitulo)).addStyle(scTitulo));

                tabla.addCell(new Cell(1, 4)
                                .add(new Paragraph(nomina.getReceptor().getTipoContrato() + " - "
                                                + Catalogos.ObtenerTipoContrato(nomina.getReceptor().getTipoContrato()))
                                                .addStyle(spMensaje))
                                .addStyle(sc));
                tabla.addCell(new Cell(1, 3)
                                .add(new Paragraph(nomina.getReceptor().getDepartamento()).addStyle(spMensaje))
                                .addStyle(sc));
                tabla.addCell(
                                new Cell(1, 2).add(new Paragraph(nomina.getReceptor().getPuesto()).addStyle(spMensaje))
                                                .addStyle(sc));
                return tabla;
        }

        private Table NAgregarConceptos() {
                Color borderColor = new DeviceRgb(142, 175, 223);
                Color backgroundColor = new DeviceRgb(184, 204, 228);

                Style spTitulo = new Style();
                spTitulo.setBold();
                spTitulo.setPadding(0);
                spTitulo.setFontSize(8);

                Style spConcepto = new Style();
                spConcepto.setPadding(0);
                spConcepto.setFontSize(8);

                Style sc = new Style();
                sc.setPaddingTop(0);
                sc.setPaddingBottom(0);
                sc.setBorder(Border.NO_BORDER);
                sc.setBackgroundColor(backgroundColor);

                Style scConcepto = new Style();
                scConcepto.setPaddingTop(0);
                scConcepto.setPaddingBottom(0);
                scConcepto.setBorder(Border.NO_BORDER);

                float[] columnas = Utilidades.cmToFloat(new float[] { 2f, 1.5f, 2f, 10.5f, 2, 2f });
                Table tabla = new Table(columnas);
                tabla.setMarginTop(5);
                // tabla.setBorder(new SolidBorder(borderColor, 1));
                tabla.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
                // tabla.setBorderRadius(new BorderRadius(5));

                tabla.addCell(new Cell().add(new Paragraph("Cve. Prod.").addStyle(spTitulo)).addStyle(sc)
                                .setBorderTopLeftRadius(new BorderRadius(4))
                                .setBorderBottomLeftRadius(new BorderRadius(4)));
                tabla.addCell(new Cell().add(new Paragraph("Cantidad").addStyle(spTitulo)).addStyle(sc)
                                .setTextAlignment(TextAlignment.CENTER));
                tabla.addCell(new Cell().add(new Paragraph("Unidad").addStyle(spTitulo)).addStyle(sc));
                tabla.addCell(new Cell().add(new Paragraph("Descripción").addStyle(spTitulo)).addStyle(sc));
                tabla.addCell(new Cell().add(new Paragraph("Precio").addStyle(spTitulo)).addStyle(sc)
                                .setTextAlignment(TextAlignment.RIGHT));
                tabla.addCell(new Cell().add(new Paragraph("Importe").addStyle(spTitulo)).addStyle(sc)
                                .setBorderTopRightRadius(new BorderRadius(4))
                                .setBorderBottomRightRadius(new BorderRadius(4))
                                .setTextAlignment(TextAlignment.RIGHT));

                for (int i = 0; i < COMPROBANTE.getConceptos().getConcepto().size(); i++) {
                        Concepto concepto = COMPROBANTE.getConceptos().getConcepto().get(i);
                        tabla.addCell(new Cell().add(new Paragraph(concepto.getClaveProdServ()).addStyle(spConcepto))
                                        .addStyle(scConcepto));
                        tabla.addCell(
                                        new Cell().add(new Paragraph(String.format("$%.2f", concepto.getCantidad()))
                                                        .addStyle(spConcepto))
                                                        .addStyle(scConcepto));
                        tabla.addCell(
                                        new Cell().add(new Paragraph(concepto.getUnidad()).addStyle(spConcepto))
                                                        .addStyle(scConcepto));
                        tabla.addCell(
                                        new Cell().add(new Paragraph(concepto.getDescripcion()).addStyle(spConcepto))
                                                        .addStyle(scConcepto));
                        tabla.addCell(new Cell()
                                        .add(new Paragraph(String.format("$%.2f", concepto.getValorUnitario()))
                                                        .addStyle(spConcepto))
                                        .addStyle(scConcepto).setTextAlignment(TextAlignment.RIGHT));
                        tabla.addCell(
                                        new Cell().add(new Paragraph(String.format("$%.2f", concepto.getImporte()))
                                                        .addStyle(spConcepto))
                                                        .addStyle(scConcepto).setTextAlignment(TextAlignment.RIGHT));
                }
                return tabla;

        }

        private double NObtenerSubsidioCausado(Nomina nomina) {
                double totalSubsidiosCausado = 0;
                if (nomina.getOtrosPagos() != null) {
                        for (int i = 0; i < nomina.getOtrosPagos().getOtroPago().size(); i++) {
                                NOtroPago item = nomina.getOtrosPagos().getOtroPago().get(i);
                                if ("Subsidio efectivo".equals(item.getConcepto())) {
                                        totalSubsidiosCausado = item.getSubsidioAlEmpleo().getSubsidioCausado();
                                }
                        }
                }
                return totalSubsidiosCausado;
        }

        private Table NAgregarEmisor() {
                Color backgroundColor = new DeviceRgb(249, 249, 249);
                Color borderColor = new DeviceRgb(241, 241, 241);

                Style estiloSubtitle = new Style();
                estiloSubtitle.setBold();
                estiloSubtitle.setFontSize(8);

                Style estiloNormal = new Style();
                estiloNormal.setFontSize(8);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setPaddingTop(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                float[] columnas = Utilidades.cmToFloat(new float[] { 3.5f, 6.5f, 3.5f, 6.5f });
                Table tablaDatos = new Table(columnas);
                tablaDatos.setMarginTop(5);
                tablaDatos.setBackgroundColor(backgroundColor);
                tablaDatos.setBorder(new SolidBorder(borderColor, .5f));
                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Patrón / Emisor").addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                tablaDatos.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getEmisor().getNombre()).addStyle(estiloNormal))
                                .addStyle(estiloCelda));
                // Cell celdaNombre = new Cell(1, 3);
                // celdaNombre.add(new
                // Paragraph(COMPROBANTE.getEmisor().getNombre()).addStyle(estiloNormal));
                // celdaNombre.addStyle(estiloCelda);
                // tablaDatos.addCell(celdaNombre);
                tablaDatos.addCell(new Cell().add(new Paragraph("RFC").addStyle(estiloSubtitle)).addStyle(estiloCelda));
                tablaDatos.addCell(
                                new Cell().add(new Paragraph(COMPROBANTE.getEmisor().getRfc()).addStyle(estiloNormal))
                                                .addStyle(estiloCelda));
                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Regimen fiscal").addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                tablaDatos.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getEmisor().getRegimenFiscal() + " - "
                                                + Catalogos.ObtenerRegimenFiscal(
                                                                COMPROBANTE.getEmisor().getRegimenFiscal()))
                                                .addStyle(estiloNormal))
                                .addStyle(estiloCelda));
                // Cell celdaRegimen = new Cell(1, 3);
                // celdaRegimen.addStyle(estiloCelda);
                // celdaRegimen.add(new
                // Paragraph(COMPROBANTE.getEmisor().getRfc()).addStyle(estiloNormal));
                // tablaDatos.addCell(celdaRegimen);
                // if (COMPROBANTE.getAddenda() != null) {
                // Cell celdaDireccion = new Cell(1, 5);
                // celdaDireccion.add(new Cell().add(new
                // Paragraph(COMPROBANTE.getAddenda().getDireccion()).addStyle(estiloNormal)).addStyle(estiloCelda));;
                // celdaDireccion.addStyle(estiloCelda);
                // tablaDatos.addCell(new Cell().add(new
                // Paragraph("Dirección").addStyle(estiloSubtitle)).addStyle(estiloCelda));;
                // tablaDatos.addCell(celdaDireccion);
                // }
                if (!COMPROBANTE.getComplemento().Nomina.getEmisor().getRegistroPatronal().isEmpty()) {
                        tablaDatos.addCell(
                                        new Cell().add(new Paragraph("Registro patronal").addStyle(estiloSubtitle))
                                                        .addStyle(estiloCelda));
                        tablaDatos.addCell(
                                        new Cell().add(new Paragraph(COMPROBANTE.getComplemento().Nomina.getEmisor()
                                                        .getRegistroPatronal())
                                                        .addStyle(estiloNormal)).addStyle(estiloCelda));
                }
                if (!COMPROBANTE.getComplemento().Nomina.getEmisor().getCurp().isEmpty()) {
                        tablaDatos.addCell(new Cell().add(new Paragraph("CURP").addStyle(estiloSubtitle))
                                        .addStyle(estiloCelda));
                        tablaDatos.addCell(new Cell().add(
                                        new Paragraph(COMPROBANTE.getComplemento().Nomina.getEmisor().getCurp())
                                                        .addStyle(estiloNormal))
                                        .addStyle(estiloCelda));
                }
                if (!COMPROBANTE.getComplemento().Nomina.getEmisor().getRfcPatronOrigen().isEmpty()) {
                        tablaDatos.addCell(new Cell().add(new Paragraph("CURP").addStyle(estiloSubtitle))
                                        .addStyle(estiloCelda));
                        tablaDatos.addCell(
                                        new Cell().add(new Paragraph(COMPROBANTE.getComplemento().Nomina.getEmisor()
                                                        .getRfcPatronOrigen())
                                                        .addStyle(estiloNormal)).addStyle(estiloCelda));
                }

                tablaDatos.startNewRow();
                return tablaDatos;
        }

        private Table NAgregarPercepcionesDeducciones() {
                Color borderColor = new DeviceRgb(142, 175, 223);

                // Estilo de parrafos
                Style estiloTitle = new Style();
                estiloTitle.setBold();
                estiloTitle.setTextAlignment(TextAlignment.CENTER);
                estiloTitle.setFontSize(8);

                Style estiloSubtitle = new Style();
                estiloSubtitle.setFontSize(8);
                estiloSubtitle.setBold();

                Style estiloNormal = new Style();
                estiloNormal.setFontSize(8);

                Style estiloMoneda = new Style();
                estiloMoneda.setFontSize(8);
                estiloMoneda.setTextAlignment(TextAlignment.RIGHT);

                // Estilo celdas
                Style estiloCelda = new Style();
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setPaddingTop(0);
                estiloCelda.setBorder(Border.NO_BORDER);

                Style estiloCeldaPercepciones = new Style();
                estiloCeldaPercepciones.setPaddingBottom(0);
                estiloCeldaPercepciones.setPaddingTop(0);
                estiloCeldaPercepciones.setBorderLeft(Border.NO_BORDER);
                estiloCeldaPercepciones.setBorderTop(Border.NO_BORDER);
                estiloCeldaPercepciones.setBorderBottom(Border.NO_BORDER);
                // estiloCeldaPercepciones.setBorder(Border.NO_BORDER);

                Style estiloCeldaDeducciones = new Style();
                estiloCeldaDeducciones.setPaddingBottom(0);
                estiloCeldaDeducciones.setPaddingTop(0);

                estiloCeldaDeducciones.setBorderRight(Border.NO_BORDER);
                estiloCeldaDeducciones.setBorderTop(Border.NO_BORDER);
                estiloCeldaDeducciones.setBorderBottom(Border.NO_BORDER);

                float[] anchoColumnasTablaProductos = Utilidades.cmToFloat(new float[] { 10.5f, 10.5f });
                Table tablaProductosPrincipal = new Table(anchoColumnasTablaProductos);
                tablaProductosPrincipal.setMarginTop(5);
                tablaProductosPrincipal.setHorizontalAlignment(HorizontalAlignment.LEFT);

                // Datos de los productos
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 1.5f, 5f, 2f, 2f });
                Table tablaPercepciones = new Table(tamanoColumnas);
                tablaPercepciones.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
                tablaPercepciones.setHorizontalAlignment(HorizontalAlignment.LEFT);
                tablaPercepciones.setBorder(Border.NO_BORDER);

                Cell celdaTituloPercepciones = new Cell(1, 4);
                celdaTituloPercepciones.setBorder(new SolidBorder(borderColor, .8f));
                celdaTituloPercepciones.setBorderBottom(Border.NO_BORDER);
                celdaTituloPercepciones.setBorderTopRightRadius(new BorderRadius(4));
                celdaTituloPercepciones.setBorderTopLeftRadius(new BorderRadius(4));
                celdaTituloPercepciones.add(new Paragraph("PERCEPCIONES").addStyle(estiloTitle));
                celdaTituloPercepciones.setPadding(0);
                celdaTituloPercepciones.setHorizontalAlignment(HorizontalAlignment.LEFT);

                Cell celdaConcepto1 = new Cell();
                celdaConcepto1.setBorder(new SolidBorder(borderColor, .8f));
                celdaConcepto1.add(new Paragraph("Clave").addStyle(estiloSubtitle));
                celdaConcepto1.setBorderBottomLeftRadius(new BorderRadius(4));
                celdaConcepto1.setPaddingBottom(0);
                celdaConcepto1.setPaddingTop(0);

                Cell celdaConcepto = new Cell();
                celdaConcepto.add(new Paragraph("Concepto").addStyle(estiloSubtitle));
                celdaConcepto.setBorder(new SolidBorder(borderColor, .8f));
                celdaConcepto.setPaddingBottom(0);
                celdaConcepto.setPaddingTop(0);
                celdaConcepto.setHorizontalAlignment(HorizontalAlignment.LEFT);

                Cell celdaExento = new Cell();
                celdaExento.add(new Paragraph("Exento").addStyle(estiloSubtitle));
                celdaExento.setBorder(new SolidBorder(borderColor, .8f));
                celdaExento.setPaddingBottom(0);
                celdaExento.setPaddingTop(0);
                celdaExento.setHorizontalAlignment(HorizontalAlignment.LEFT);

                Cell celdaGravado = new Cell();
                celdaGravado.add(new Paragraph("Gravado").addStyle(estiloSubtitle));
                celdaGravado.setBorder(new SolidBorder(borderColor, .8f));
                celdaGravado.setBorderBottomRightRadius(new BorderRadius(4));
                celdaGravado.setPaddingBottom(0);
                celdaGravado.setPaddingTop(0);
                celdaGravado.setTextAlignment(TextAlignment.RIGHT);

                Cell celdaImporte = new Cell();
                celdaImporte.add(new Paragraph("Importe").addStyle(estiloSubtitle));
                celdaImporte.setBorder(new SolidBorder(borderColor, .8f));
                celdaImporte.setBorderBottomRightRadius(new BorderRadius(4));
                celdaImporte.setPaddingBottom(0);
                celdaImporte.setPaddingTop(0);
                celdaImporte.setTextAlignment(TextAlignment.RIGHT);

                tablaPercepciones.addCell(celdaTituloPercepciones);
                tablaPercepciones.addCell(celdaConcepto1);
                tablaPercepciones.addCell(celdaConcepto);
                tablaPercepciones.addCell(celdaExento);
                tablaPercepciones.addCell(celdaGravado);

                // int numPercepciones = COMPROBANTE.getComplemento().Nomina.getPercepciones()
                // != null ?
                // COMPROBANTE.getComplemento().Nomina.getPercepciones().getPercepcion().size()
                // : 0;
                // int numDeducciones = COMPROBANTE.getComplemento().Nomina.getOtrosPagos() !=
                // null ?
                // COMPROBANTE.getComplemento().Nomina.getDeducciones().getDeduccion().size() :
                // 0;
                // int maxRows = numPercepciones > numDeducciones ? numPercepciones :
                // numDeducciones;
                if (COMPROBANTE.getComplemento().Nomina.getPercepciones() != null) {
                        for (int i = 0; i < COMPROBANTE.getComplemento().Nomina.getPercepciones().getPercepcion()
                                        .size(); i++) {
                                NPercepcion p = COMPROBANTE.getComplemento().Nomina.getPercepciones().getPercepcion()
                                                .get(i);
                                Cell clave = new Cell();
                                clave.setPaddingTop(0);
                                clave.setPaddingBottom(0);
                                clave.add(new Paragraph(p.getClave()).addStyle(estiloNormal));
                                clave.setBorder(Border.NO_BORDER);
                                tablaPercepciones.addCell(clave);

                                Cell concepto = new Cell();
                                concepto.setPaddingTop(0);
                                concepto.setPaddingBottom(0);
                                concepto.add(new Paragraph(p.getConcepto()).addStyle(estiloNormal));
                                concepto.setBorder(Border.NO_BORDER);
                                tablaPercepciones.addCell(concepto);

                                Cell exento = new Cell();
                                exento.setPaddingTop(0);
                                exento.setPaddingBottom(0);
                                exento.add(new Paragraph(String.format("$%.2f", p.getImporteExento()))
                                                .addStyle(estiloNormal));
                                exento.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                exento.setBorder(Border.NO_BORDER);
                                tablaPercepciones.addCell(exento);

                                Cell importe = new Cell();
                                importe.setBorder(Border.NO_BORDER);
                                importe.setPaddingTop(0);
                                importe.setPaddingBottom(0);
                                importe.add(new Paragraph(String.format("$%.2f", p.getImporteGravado()))
                                                .addStyle(estiloMoneda));
                                importe.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                tablaPercepciones.addCell(importe);
                        }
                }

                Cell celdaSumaPercepciones = new Cell(1, 2);
                celdaSumaPercepciones.add(new Paragraph("Suma de percepciones").addStyle(estiloSubtitle));
                celdaSumaPercepciones.setBorder(new SolidBorder(borderColor, .8f));
                celdaSumaPercepciones.setBorderLeft(Border.NO_BORDER);
                celdaSumaPercepciones.setBorderRight(Border.NO_BORDER);
                celdaSumaPercepciones.setBorderBottom(Border.NO_BORDER);
                celdaSumaPercepciones.setTextAlignment(TextAlignment.RIGHT);
                tablaPercepciones.addCell(celdaSumaPercepciones);

                double totalPercepciones = COMPROBANTE.getConceptos().getConcepto().get(0).getImporte();
                Cell celdaTotalPercepciones = new Cell();
                celdaTotalPercepciones
                                .add(new Paragraph(Utilidades.convertir(totalPercepciones)).addStyle(estiloMoneda));
                celdaTotalPercepciones.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                celdaTotalPercepciones.setBorder(new SolidBorder(borderColor, .8f));
                celdaTotalPercepciones.setBorderLeft(Border.NO_BORDER);
                celdaTotalPercepciones.setBorderRight(Border.NO_BORDER);
                celdaTotalPercepciones.setBorderBottom(Border.NO_BORDER);
                tablaPercepciones.addCell(celdaTotalPercepciones);

                double totalGravado = COMPROBANTE.getConceptos().getConcepto().get(0).getImporte();
                Cell celdaTotalGravado = new Cell();
                celdaTotalGravado.add(new Paragraph(Utilidades.convertir(totalPercepciones)).addStyle(estiloMoneda));
                celdaTotalGravado.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                celdaTotalGravado.setBorder(new SolidBorder(borderColor, .8f));
                celdaTotalGravado.setBorderLeft(Border.NO_BORDER);
                celdaTotalGravado.setBorderRight(Border.NO_BORDER);
                celdaTotalGravado.setBorderBottom(Border.NO_BORDER);
                tablaPercepciones.addCell(celdaTotalGravado);

                // LLENAMOS LA TABLA DEDUCCIONES
                tamanoColumnas = Utilidades.cmToFloat(new float[] { 1.5f, 5.5f, 3.5f });
                Table tablaDeducciones = new Table(tamanoColumnas);
                tablaDeducciones.setBorder(Border.NO_BORDER);
                tablaDeducciones.setHorizontalAlignment(HorizontalAlignment.LEFT);
                tablaDeducciones.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);

                Cell celdaTituloDeducciones = new Cell(1, 3);
                celdaTituloDeducciones.setBorderTopRightRadius(new BorderRadius(4));
                celdaTituloDeducciones.setBorderTopLeftRadius(new BorderRadius(4));
                celdaTituloDeducciones.setBorderBottom(Border.NO_BORDER);
                celdaTituloDeducciones.add(new Paragraph("DEDUCCIONES").addStyle(estiloTitle));
                celdaTituloDeducciones.setPadding(0);
                celdaTituloDeducciones.setBorder(new SolidBorder(borderColor, .8f));
                celdaTituloDeducciones.setHorizontalAlignment(HorizontalAlignment.CENTER);

                tablaDeducciones.addCell(celdaTituloDeducciones);
                tablaDeducciones.addCell(celdaConcepto1);
                tablaDeducciones.addCell(celdaConcepto);
                tablaDeducciones.addCell(celdaImporte.setBorderLeft(Border.NO_BORDER));
                if (COMPROBANTE.getComplemento().Nomina.getDeducciones() != null) {
                        for (int i = 0; i < COMPROBANTE.getComplemento().Nomina.getDeducciones().getDeduccion()
                                        .size(); i++) {
                                NDeduccion p = COMPROBANTE.getComplemento().Nomina.getDeducciones().getDeduccion()
                                                .get(i);

                                Cell clave = new Cell();
                                clave.setPaddingTop(0);
                                clave.setPaddingBottom(0);
                                clave.add(new Paragraph(p.getClave()).addStyle(estiloNormal));
                                clave.setBorder(Border.NO_BORDER);
                                tablaDeducciones.addCell(clave);

                                Cell concepto = new Cell();
                                concepto.setPaddingTop(0);
                                concepto.setPaddingBottom(0);
                                concepto.add(new Paragraph(p.getConcepto()).addStyle(estiloNormal));
                                concepto.setBorder(Border.NO_BORDER);
                                tablaDeducciones.addCell(concepto);

                                Cell importe = new Cell();
                                importe.setPaddingTop(0);
                                importe.setPaddingBottom(0);
                                importe.add(new Paragraph(String.format("$%.2f", (p.getImporte())))
                                                .addStyle(estiloMoneda));
                                importe.setBorder(Border.NO_BORDER);
                                importe.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                tablaDeducciones.addCell(importe);

                        }
                }
                Cell celdaSumaDeducciones = new Cell(1, 2);
                celdaSumaDeducciones.add(new Paragraph("Suma de deducciones").addStyle(estiloSubtitle));
                celdaSumaDeducciones.setBorder(new SolidBorder(borderColor, .8f));
                celdaSumaDeducciones.setBorderLeft(Border.NO_BORDER);
                celdaSumaDeducciones.setBorderRight(Border.NO_BORDER);
                celdaSumaDeducciones.setBorderBottom(Border.NO_BORDER);
                celdaSumaDeducciones.setTextAlignment(TextAlignment.RIGHT);
                tablaDeducciones.addCell(celdaSumaDeducciones);

                Cell celdaTotalDeducciones = new Cell();
                celdaTotalDeducciones.add(
                                new Paragraph(Utilidades.convertir(COMPROBANTE.getDescuento())).addStyle(estiloMoneda));
                celdaTotalDeducciones.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                celdaTotalDeducciones.setBorder(new SolidBorder(borderColor, .8f));
                celdaTotalDeducciones.setBorderLeft(Border.NO_BORDER);
                celdaTotalDeducciones.setBorderRight(Border.NO_BORDER);
                celdaTotalDeducciones.setBorderBottom(Border.NO_BORDER);
                tablaDeducciones.addCell(celdaTotalDeducciones);

                // LLENAMOS LA TABLA OTROS PAGOS
                tamanoColumnas = Utilidades.cmToFloat(new float[] { 1.5f, 5.5f, 3.5f });
                Table tablaOtrosPagos = new Table(tamanoColumnas);
                tablaOtrosPagos.setHorizontalAlignment(HorizontalAlignment.LEFT);
                tablaOtrosPagos.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
                tablaOtrosPagos.setBorder(Border.NO_BORDER);
                tablaOtrosPagos.setMarginTop(4);

                if (COMPROBANTE.getComplemento().Nomina.getOtrosPagos() != null) {
                        if (COMPROBANTE.getComplemento().Nomina.getOtrosPagos().getOtroPago().size() > 0) {
                                double totalOtrosPagos = 0;
                                Cell celdaTituloOtrosPagos = new Cell(1, 3);
                                celdaTituloOtrosPagos.setBorderTopRightRadius(new BorderRadius(4));
                                celdaTituloOtrosPagos.setBorderTopLeftRadius(new BorderRadius(4));
                                celdaTituloOtrosPagos.setBorder(new SolidBorder(borderColor, .8f));
                                celdaTituloOtrosPagos.setBorderBottom(Border.NO_BORDER);
                                celdaTituloOtrosPagos.add(new Paragraph("OTROS PAGOS").addStyle(estiloTitle));
                                celdaTituloOtrosPagos.setPadding(0);

                                tablaOtrosPagos.addCell(celdaTituloOtrosPagos);
                                tablaOtrosPagos.addCell(celdaConcepto1);
                                tablaOtrosPagos.addCell(celdaConcepto);
                                tablaOtrosPagos.addCell(celdaImporte.setBorderLeft(Border.NO_BORDER));

                                for (int i = 0; i < COMPROBANTE.getComplemento().Nomina.getOtrosPagos().getOtroPago()
                                                .size(); i++) {
                                        NOtroPago p = COMPROBANTE.getComplemento().Nomina.getOtrosPagos().getOtroPago()
                                                        .get(i);
                                        if (p.getImporte() != 0.01) {
                                                totalOtrosPagos += p.getImporte();
                                                Cell clave = new Cell();
                                                clave.add(new Paragraph(p.getClave()).addStyle(estiloNormal));
                                                clave.setBorder(Border.NO_BORDER);
                                                tablaOtrosPagos.addCell(clave);

                                                Cell concepto = new Cell();
                                                concepto.add(new Paragraph(p.getConcepto()).addStyle(estiloNormal));
                                                concepto.setBorder(Border.NO_BORDER);
                                                tablaOtrosPagos.addCell(concepto);

                                                Cell importe = new Cell();
                                                importe.add(new Paragraph(String.format("$%.2f", p.getImporte()))
                                                                .addStyle(estiloMoneda));
                                                importe.setBorder(Border.NO_BORDER);
                                                importe.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                                tablaOtrosPagos.addCell(importe);
                                        }
                                }
                                Cell celdaSumaOtrosPagos = new Cell(1, 2);
                                celdaSumaOtrosPagos.add(new Paragraph("Suma de otros pagos").addStyle(estiloSubtitle));
                                celdaSumaOtrosPagos.setBorder(new SolidBorder(borderColor, .8f));
                                celdaSumaOtrosPagos.setBorderLeft(Border.NO_BORDER);
                                celdaSumaOtrosPagos.setBorderRight(Border.NO_BORDER);
                                celdaSumaOtrosPagos.setBorderBottom(Border.NO_BORDER);
                                celdaSumaOtrosPagos.setTextAlignment(TextAlignment.RIGHT);
                                tablaOtrosPagos.addCell(celdaSumaOtrosPagos);

                                Cell celdaTotalOtrosPagos = new Cell();
                                celdaTotalOtrosPagos.add(new Paragraph(Utilidades.convertir(totalOtrosPagos))
                                                .addStyle(estiloMoneda));
                                celdaTotalOtrosPagos.setBorder(new SolidBorder(borderColor, .8f));
                                celdaTotalOtrosPagos.setBorderLeft(Border.NO_BORDER);
                                celdaTotalOtrosPagos.setBorderRight(Border.NO_BORDER);
                                celdaTotalOtrosPagos.setBorderBottom(Border.NO_BORDER);
                                celdaTotalOtrosPagos.setHorizontalAlignment(HorizontalAlignment.RIGHT);
                                tablaOtrosPagos.addCell(celdaTotalOtrosPagos);
                        }
                }

                //
                Cell celdaPercepciones = new Cell();
                celdaPercepciones.add(tablaPercepciones);
                celdaPercepciones.addStyle(estiloCelda);
                tablaProductosPrincipal.addCell(celdaPercepciones);

                Cell celdaDeducciones = new Cell();
                celdaDeducciones.add(tablaDeducciones);
                celdaDeducciones.addStyle(estiloCelda);
                tablaProductosPrincipal.addCell(celdaDeducciones);

                if (COMPROBANTE.getComplemento().Nomina.getOtrosPagos() != null) {
                        Cell celdaOtrosPagos = new Cell();
                        celdaOtrosPagos.add(tablaOtrosPagos);
                        celdaOtrosPagos.addStyle(estiloCelda);
                        tablaProductosPrincipal.addCell(celdaOtrosPagos);
                }

                return tablaProductosPrincipal;
        }

        private IBlockElement NAgregarMensaje() {
                Color fontColor = new DeviceRgb(142, 175, 223);

                float[] columnas = Utilidades.cmToFloat(new float[] { 11, 9f });
                Table tablaDatos = new Table(columnas);
                tablaDatos.setMarginTop(5);

                tablaDatos.addCell(new Cell(2, 1).add(new Paragraph("RECIBÍ DE " + COMPROBANTE.getEmisor().getNombre()
                                + "LA CANTIDAD INDICADA QUE CUBRE A LA FECHA EL IMPORTE DE MI SALARIO, TIEMPO EXTRA, SEPTIMO DÍA Y TODAS LAS PERCEPCIONES Y PRESTACIONES A QUE TENGO DERECHO SIN QUE SE ME ADEUDE ALGUNA CANTIDAD POR OTRO CONCEPTO.")
                                .setFontSize(8).setFixedLeading(8).setBold()).setBorder(Border.NO_BORDER));

                Cell celdaLinea = new Cell();
                celdaLinea.setPadding(0);
                celdaLinea.add(new Paragraph("\n____________________________________________").setFontSize(8)
                                .setTextAlignment(TextAlignment.CENTER).setFontColor(fontColor));
                celdaLinea.setVerticalAlignment(VerticalAlignment.BOTTOM);
                celdaLinea.setBorder(Border.NO_BORDER);
                tablaDatos.addCell(celdaLinea);

                Cell celdaNombre = new Cell();
                celdaNombre.setPadding(0);
                celdaNombre.setVerticalAlignment(VerticalAlignment.TOP);
                celdaNombre.add(
                                new Paragraph("FIRMA DEL EMPLEADO").setBold().setFontSize(8)
                                                .setTextAlignment(TextAlignment.CENTER));
                celdaNombre.setBorder(Border.NO_BORDER);
                tablaDatos.addCell(celdaNombre);

                // tablaDatos.addCell(new Cell().add(new Paragraph("El importe de este recibo
                // cubre toda percepción, deducción, subsidio fiscal, descansos y prestaciones
                // legales por mi trabajo a éste patrón (o derivadas de la prestación de
                // servicios independientes a éste Emisor) por el periodo marcado, no quedando
                // cantidad alguna por pagar o prestación por
                // reclamar.").setFontSize(8).setFixedLeading(8)).setBorder(Border.NO_BORDER));
                // tablaDatos.addCell(new Cell().add(new Paragraph("Acepto y me doy por pagado
                // en forma y tiempo, segun lo marcado en las Leyes Fiscales y Laborales
                // vigentes, solo quedando pendiente la recepción del archivo CFDI a mi
                // dirección de correo electrónico, la que me ha sido solicitada y que haré
                // saber en su oportunidad a éste Patrón o
                // Emisor.").setFontSize(8).setFixedLeading(8)).setBorder(Border.NO_BORDER));
                return tablaDatos;
        }

        private IBlockElement NAgregarSellos() {
                Color borderColor = new DeviceRgb(142, 175, 223);

                Style estiloSubtitle = new Style();
                estiloSubtitle.setBold();
                estiloSubtitle.setFontSize(7);

                Style estiloNormal = new Style();
                estiloNormal.setFontSize(8);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBorder(Border.NO_BORDER);
                estiloCelda.setPaddingTop(0);

                float[] columnas = Utilidades.cmToFloat(new float[] { 16.3f, 3.7f });
                Table tablaDatos = new Table(columnas);
                tablaDatos.setWidth(Utilidades.cmToFloat(20));
                tablaDatos.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
                tablaDatos.setBorderRadius(new BorderRadius(5));
                tablaDatos.setBorder(new SolidBorder(borderColor, .8f));
                tablaDatos.setMarginTop(5);
                // tablaDatos.setWidth(columnas);
                tablaDatos.setFixedLayout();

                // Agregamos el codigo QR al documento
                StringBuilder codigoQR = new StringBuilder();
                codigoQR.append("?re" + COMPROBANTE.getEmisor().getRfc()); // RFC del Emisor
                codigoQR.append("&rr" + COMPROBANTE.getReceptor().getRfc()); // RFC del receptor
                codigoQR.append("&tt" + COMPROBANTE.getTotal()); // Total del COMPROBANTE 10 enteros y 6 decimales
                codigoQR.append("&id" + COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID()); // UUID del
                                                                                                     // COMPROBANTE

                BarcodeQRCode pdfCodigoQR = new BarcodeQRCode(codigoQR.toString());
                Image img = new Image(pdfCodigoQR.createFormXObject(ColorConstants.BLACK, DOCUMENTO_PDF));
                // img.setBackgroundColor(borderColor);
                img.scaleAbsolute(95, 95);

                Paragraph parrafoSelloSAT = new Paragraph();
                parrafoSelloSAT.add(new Text("Sello SAT ").addStyle(estiloSubtitle));
                parrafoSelloSAT.add(new Text(COMPROBANTE.getComplemento().TimbreFiscalDigital.getSelloSAT())
                                .setFontSize(6));
                // parrafoSelloSAT.add().setFontSize(5));
                Cell celdaSelloSAT = new Cell();
                celdaSelloSAT.addStyle(estiloCelda);
                celdaSelloSAT.add(parrafoSelloSAT);
                tablaDatos.addCell(celdaSelloSAT);

                Cell celdaQR = new Cell(3, 1);
                celdaQR.add(img);
                celdaQR.setPadding(0);
                // celdaQR.setHeight(110);
                celdaQR.addStyle(estiloCelda);
                celdaQR.setHorizontalAlignment(HorizontalAlignment.CENTER);
                celdaQR.setVerticalAlignment(VerticalAlignment.MIDDLE);
                tablaDatos.addCell(celdaQR);

                Paragraph parrafoSelloDE = new Paragraph();
                parrafoSelloDE.add(new Text("Sello digital Emisor ").addStyle(estiloSubtitle));
                parrafoSelloDE.add(new Text(COMPROBANTE.getComplemento().TimbreFiscalDigital.getSelloCFD())
                                .setFontSize(6));
                // parrafoSelloDE.add("Sello digital Emisor " +
                // COMPROBANTE.getComplemento().TimbreFiscalDigital.getSelloCFD());

                Cell celdaSDE = new Cell();
                celdaSDE.addStyle(estiloCelda);
                celdaSDE.add(parrafoSelloDE);
                tablaDatos.addCell(celdaSDE);

                Paragraph parrafoCadenaOriginal = new Paragraph();
                parrafoCadenaOriginal
                                .add(new Text("Cadena original Complemento de certificado digital del SAT ")
                                                .addStyle(estiloSubtitle));
                StringBuilder cadenaOriginal = new StringBuilder();
                cadenaOriginal.append("||");
                cadenaOriginal.append("1.0|");
                cadenaOriginal.append(COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID() + "|");
                cadenaOriginal.append(Utilidades.ToDate(COMPROBANTE.getFecha().toString()) + "|");
                cadenaOriginal.append(COMPROBANTE.getComplemento().TimbreFiscalDigital.getSelloSAT() + "|");
                cadenaOriginal.append(COMPROBANTE.getComplemento().TimbreFiscalDigital.getNoCertificadoSAT() + "||");
                parrafoCadenaOriginal.add(new Text(cadenaOriginal.toString()).setFontSize(6));

                Cell celdaCadenaOriginal = new Cell();
                celdaCadenaOriginal.addStyle(estiloCelda);
                celdaCadenaOriginal.add(parrafoCadenaOriginal);
                tablaDatos.addCell(celdaCadenaOriginal);

                return tablaDatos;
        }

        private IBlockElement NAgregarDatosFiscales() {
                Color backgroundColor = new DeviceRgb(249, 249, 249);
                Color borderColor = new DeviceRgb(241, 241, 241);

                Style estiloSubtitle = new Style();
                estiloSubtitle.setBold();
                estiloSubtitle.setFontSize(8);

                Style estiloNormal = new Style();
                estiloNormal.setFontSize(8);

                Style estiloCelda = new Style();
                estiloCelda.setPaddingBottom(0);
                estiloCelda.setBackgroundColor(backgroundColor);
                estiloCelda.setBorder(Border.NO_BORDER);
                estiloCelda.setPaddingTop(0);

                float[] columnas = { 130f, 180f, 100f, 160f };
                Table tablaDatos = new Table(columnas);
                tablaDatos.setBorder(new SolidBorder(borderColor, .5f));
                tablaDatos.setMarginTop(5);
                tablaDatos.setWidth(470f);
                tablaDatos.setFixedLayout();

                // Cell celdaUI = new Cell(1, 4);
                // celdaUI.add(new Paragraph("Representación parcial del CFDI correspondiente al
                // folio fiscal (UUID): " +
                // COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID()).addStyle(estiloNormal));
                // celdaUI.setTextAlignment(TextAlignment.CENTER);
                // celdaUI.addStyle(estiloCelda);
                // tablaDatos.addCell(celdaUI);
                double total = COMPROBANTE.getTotal();
                if (ExisteSubsidioParaElEmpleo(COMPROBANTE.getComplemento().Nomina)) {
                        total = total - 0.01f;
                }
                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Folio:").addStyle(estiloSubtitle)).addStyle(estiloCelda));
                tablaDatos.addCell(
                                new Cell().add(new Paragraph(COMPROBANTE.getFolio()).addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));

                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Método de pago:").addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                ;
                tablaDatos.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getMetodoPago() + " - "
                                                + Catalogos.ObtenerMetodoPago(COMPROBANTE.getMetodoPago()))
                                                .addStyle(estiloNormal))
                                .addStyle(estiloCelda));

                total = COMPROBANTE.getTotal();
                if (ExisteSubsidioParaElEmpleo(COMPROBANTE.getComplemento().Nomina)) {
                        total = total - 0.01f;
                }

                // tablaDatos.addCell(new Cell().add(new Paragraph("Importe con
                // letra:").addStyle(estiloSubtitle)).addStyle(estiloCelda));
                // Cell celdaImporteLetra = new Cell(1, 3);
                // celdaImporteLetra.add(new Cell().add(new
                // Paragraph(NumberToLetterConvert.convertNumberToLetter(total)).addStyle(estiloNormal)).addStyle(estiloCelda));
                // tablaDatos.addCell(celdaImporteLetra);
                tablaDatos.addCell(
                                new Cell().add(new Paragraph("UUID:").addStyle(estiloSubtitle)).addStyle(estiloCelda));
                tablaDatos.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getComplemento().TimbreFiscalDigital.getUUID())
                                                .addStyle(estiloNormal))
                                .addStyle(estiloCelda));

                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Forma de pago:").addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                tablaDatos.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getFormaPago() + " - "
                                                + Catalogos.ObtenerFormaPago(COMPROBANTE.getFormaPago()))
                                                .addStyle(estiloNormal))
                                .addStyle(estiloCelda));
                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Lugar expedición:").addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                tablaDatos.addCell(
                                new Cell().add(new Paragraph(COMPROBANTE.getLugarExpedicion()).addStyle(estiloNormal))
                                                .addStyle(estiloCelda));

                tablaDatos.addCell(new Cell().add(new Paragraph("Moneda:").addStyle(estiloSubtitle))
                                .addStyle(estiloCelda));
                tablaDatos.addCell(
                                new Cell().add(new Paragraph(COMPROBANTE.getMoneda() + " - "
                                                + Catalogos.ObtenerMoneda(COMPROBANTE.getMoneda()))
                                                .addStyle(estiloNormal)).addStyle(estiloCelda));
                // tablaDatos.addCell(new Paragraph(_templatePDF.folio, new
                // Font(Font.FontFamily.HELVETICA, 8)));
                // Agregar Totales
                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Fecha de expedición:").addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                tablaDatos.addCell(new Cell().add(
                                new Paragraph(COMPROBANTE.getFecha().format(DateTimeFormatter.ISO_DATE_TIME))
                                                .addStyle(estiloNormal))
                                .addStyle(estiloCelda));
                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Certificado Emisor:").addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                tablaDatos.addCell(new Cell().add(new Paragraph(COMPROBANTE.getNoCertificado()).addStyle(estiloNormal))
                                .addStyle(estiloCelda));

                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Fecha y hora de certificación:").addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                tablaDatos
                                .addCell(new Cell()
                                                .add(new Paragraph(COMPROBANTE.getComplemento().TimbreFiscalDigital
                                                                .getFechaTimbrado()
                                                                .format(DateTimeFormatter.ISO_DATE_TIME))
                                                                .addStyle(estiloNormal))
                                                .addStyle(estiloCelda));
                ;
                ;

                tablaDatos.addCell(
                                new Cell().add(new Paragraph("Certificado SAT:").addStyle(estiloSubtitle))
                                                .addStyle(estiloCelda));
                tablaDatos.addCell(
                                new Cell().add(new Paragraph(
                                                COMPROBANTE.getComplemento().TimbreFiscalDigital.getNoCertificadoSAT())
                                                .addStyle(estiloNormal)).addStyle(estiloCelda));

                return tablaDatos;

        }

        private boolean ExisteSubsidioParaElEmpleo(Nomina nomina) {
                boolean band = false;
                if (nomina.getOtrosPagos() != null) {
                        for (int i = 0; i < nomina.getOtrosPagos().getOtroPago().size(); i++) {
                                NOtroPago op = nomina.getOtrosPagos().getOtroPago().get(i);
                                if (op.getImporte() == 0.01) {
                                        return true;
                                }
                        }
                }
                return band;
        }

        private Table NAgregarTotal(double total) {
                Color backgroundColor = new DeviceRgb(249, 249, 249);
                Color borderColor = new DeviceRgb(142, 175, 223);

                Style estiloSubtitle = new Style();
                // estiloSubtitle.setBold();
                estiloSubtitle.setFontSize(8);

                float[] colTotalNeto = Utilidades.cmToFloat(new float[] { 2.5f, 9.5f, 4f, 4f });
                Table tablaTotalNeto = new Table(colTotalNeto);
                tablaTotalNeto.setBorder(new SolidBorder(borderColor, .8f));
                tablaTotalNeto.setBorderCollapse(BorderCollapsePropertyValue.SEPARATE);
                tablaTotalNeto.setBorderRadius(new BorderRadius(4));
                // tablaTotalNeto.setBackgroundColor(backgroundColor);
                tablaTotalNeto.setMarginTop(4);

                String pacRfc = COMPROBANTE.getComplemento().getTimbreFiscalDigital().getRfcProvCertif() + " / "
                                + COMPROBANTE.getNoCertificado();

                Cell celdaNeto = new Cell(3, 1);
                celdaNeto.setPaddingTop(0);
                celdaNeto.setPaddingBottom(0);
                celdaNeto.add(new Paragraph("Neto a recibir:").setBold().setFontSize(14));
                celdaNeto.setVerticalAlignment(VerticalAlignment.MIDDLE);
                celdaNeto.setBorder(Border.NO_BORDER);

                Cell celdaImporteLetra = new Cell();
                celdaImporteLetra.setPaddingTop(0);
                celdaImporteLetra.setPaddingBottom(0);
                celdaImporteLetra
                                .add(new Paragraph(NumberToLetterConvert.convertNumberToLetter(total))
                                                .addStyle(estiloSubtitle));
                celdaImporteLetra.setBorder(Border.NO_BORDER);
                celdaImporteLetra.setVerticalAlignment(VerticalAlignment.MIDDLE);
                // celdaImporteLetra.setTextAlignment(TextAlignment.CENTER);

                Cell celdaTotal = new Cell(3, 1);
                celdaTotal.setPaddingTop(0);
                celdaTotal.setPaddingBottom(0);
                celdaTotal.setBorder(Border.NO_BORDER);
                celdaTotal.add(new Paragraph(Utilidades.convertir(total)).setFontSize(14));
                celdaTotal.setVerticalAlignment(VerticalAlignment.MIDDLE);
                celdaTotal.setTextAlignment(TextAlignment.RIGHT);
                celdaTotal.setPaddingRight(5.5f);

                tablaTotalNeto.addCell(new Cell().add(new Paragraph("PAC RFC / CSD").setFontSize(8).setBold())
                                .setPaddingBottom(0).setPaddingTop(0).setBorder(Border.NO_BORDER));
                tablaTotalNeto.addCell(new Cell().add(new Paragraph(pacRfc).setFontSize(8)).setPaddingBottom(0)
                                .setPaddingTop(0)
                                .setBorder(Border.NO_BORDER));
                tablaTotalNeto.addCell(celdaNeto);
                tablaTotalNeto.addCell(celdaTotal);

                tablaTotalNeto.addCell(new Cell().add(new Paragraph("Certificación").setFontSize(8).setBold())
                                .setPaddingBottom(0).setPaddingTop(0).setBorder(Border.NO_BORDER));
                tablaTotalNeto.addCell(new Cell()
                                .add(new Paragraph(COMPROBANTE.getComplemento().getTimbreFiscalDigital()
                                                .getFechaTimbrado().toString())
                                                .setFontSize(8))
                                .setPaddingBottom(0).setPaddingTop(0).setBorder(Border.NO_BORDER));
                tablaTotalNeto.addCell(new Cell().add(new Paragraph("Importe").setFontSize(8).setBold())
                                .setBorder(Border.NO_BORDER).setPaddingBottom(0).setPaddingTop(0));
                tablaTotalNeto.addCell(celdaImporteLetra);
                return tablaTotalNeto;
        }

}
