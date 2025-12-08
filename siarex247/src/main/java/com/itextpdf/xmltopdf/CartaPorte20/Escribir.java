package com.itextpdf.xmltopdf.CartaPorte20;

import java.util.List;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.xmltopdf.Concepto;
import com.itextpdf.xmltopdf.TrasladoC;
import com.itextpdf.xmltopdf.Utilidades;
import com.itextpdf.xmltopdf.Catalogos;
import com.itextpdf.xmltopdf.Comprobante;

public class Escribir {
        Comprobante COMPROBANTE;
        Document DOCUMENTO;

        public Escribir(Comprobante comprobante, Document documento) {
                this.COMPROBANTE = comprobante;
                this.DOCUMENTO = documento;
        }

        // <editor-fold defaultstate="collapsed" desc="Formato CartaPorte 20">
        public void AgregarDatosCartaPorte20() {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tCartaPorte = new Table(tamanoColumnas);
                tCartaPorte.setMarginTop(7);

                tCartaPorte.addCell(new Cell(1, 4).add(new Paragraph("CARTA PORTE").addStyle(estiloParrafoTitle))
                                .addStyle(estiloCeldaTitle));
                com.itextpdf.xmltopdf.CartaPorte20.CartaPorte20 cartaporte = COMPROBANTE.getComplemento().CartaPorte20;
                if (!cartaporte.getTranspInternac().isEmpty()) {
                        tCartaPorte
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Transporte internacional")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tCartaPorte
                                        .addCell(new Cell()
                                                        .add(new Paragraph(cartaporte.getTranspInternac())
                                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!cartaporte.getEntradaSalidaMerc().isEmpty()) {
                        tCartaPorte
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Entrada-Salida mercancías")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tCartaPorte.addCell(
                                        new Cell().add(new Paragraph(cartaporte.getEntradaSalidaMerc())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!cartaporte.getPaisOrigenDestino().isEmpty()) {
                        tCartaPorte.addCell(new Cell()
                                        .add(new Paragraph("Pais origen o destino").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tCartaPorte.addCell(
                                        new Cell().add(new Paragraph(cartaporte.getPaisOrigenDestino())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!cartaporte.getViaEntradaSalida().isEmpty()) {
                        tCartaPorte.addCell(new Cell()
                                        .add(new Paragraph("Vía entrada o salida").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tCartaPorte.addCell(
                                        new Cell().add(new Paragraph(cartaporte.getViaEntradaSalida())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (cartaporte.getTotalDistRec() > 0) {
                        tCartaPorte
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Total distancia recorrida")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tCartaPorte.addCell(new Cell().add(
                                        new Paragraph(String.format("%.2f km", cartaporte.getTotalDistRec()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                tCartaPorte.startNewRow();

                DOCUMENTO.add(tCartaPorte);

        }

        public void AgregarConceptosCartaPorte20() {
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
                        tablaProductos.addCell(new Cell()
                                        .add(new Paragraph(concepto.getClaveUnidad() + "-"
                                                        + Catalogos.ObtenerUnidad(concepto.getClaveUnidad()))
                                                        .addStyle(estiloParrafo).setTextAlignment(TextAlignment.CENTER))
                                        .addStyle(estiloCelda));
                        tablaProductos.addCell(new Cell()
                                        .add(new Paragraph(concepto.getNoIdentificacion()).addStyle(estiloParrafo)
                                                        .setTextAlignment(TextAlignment.CENTER))
                                        .addStyle(estiloCelda));
                        descripcion.append(concepto.getDescripcion());
                        descripcion.append("\n\n Clave Prod. Serv. ");
                        descripcion.append(concepto.getClaveProdServ());
                        descripcion.append("\n");
                        if (concepto.getImpuestos() != null) {
                                descripcion.append("Impuestos:\n");
                                if (concepto.getImpuestos().getTraslados().size() > 0) {
                                        descripcion.append("\t   Traslados:\n");
                                        for (int j = 0; j < concepto.getImpuestos().getTraslados().size(); j++) {
                                                TrasladoC t = concepto.getImpuestos().getTraslados().get(j);
                                                descripcion.append("    ");
                                                descripcion.append(t.getImpuesto());
                                                descripcion.append(" ");
                                                descripcion.append(Catalogos.ObtenerImpuesto(t.getImpuesto()));
                                                descripcion.append("Base - ");
                                                descripcion.append(Utilidades.convertir(t.getBase()));
                                                descripcion.append(" Tasa - ");
                                                descripcion.append(String.format("%.2f", t.getTasaOCuota())); // a 6
                                                                                                              // decimales
                                                descripcion.append(" Importe - ");
                                                descripcion.append(String.format("%.2f", t.getImporte()));
                                                descripcion.append("\n");
                                        }
                                }
                        }
                        tablaProductos.addCell(
                                        new Cell().add(new Paragraph(descripcion.toString()).addStyle(estiloParrafo))
                                                        .addStyle(estiloCelda));
                        tablaProductos.addCell(new Cell()
                                        .add(new Paragraph(Utilidades.convertir(concepto.getValorUnitario()))
                                                        .addStyle(estiloParrafo).setTextAlignment(TextAlignment.RIGHT))
                                        .addStyle(estiloCelda));
                        tablaProductos.addCell(new Cell().add(new Paragraph(Utilidades.convertir(concepto.getImporte()))
                                        .addStyle(estiloParrafo).setTextAlignment(TextAlignment.RIGHT))
                                        .addStyle(estiloCelda));
                }
                DOCUMENTO.add(tablaProductos);
        }

        public void AgregarUbicacionesCartaPorte20() {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                if (COMPROBANTE.getComplemento().CartaPorte20.getUbicaciones() == null) {
                        return;
                }
                int contador = 0;
                for (int i = 0; i < COMPROBANTE.getComplemento().CartaPorte20.getUbicaciones().getUbicacion()
                                .size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte20.Ubicacion ubicacion = COMPROBANTE
                                        .getComplemento().CartaPorte20
                                        .getUbicaciones().getUbicacion().get(i);
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tUbicaciones = new Table(tamanoColumnas);
                        tUbicaciones.setMarginTop(7);

                        tUbicaciones.addCell(new Cell(1, 4)
                                        .add(new Paragraph("UBICACIÓN " + contador).addStyle(estiloParrafoTitle))
                                        .addStyle(estiloCeldaTitle));
                        if (!ubicacion.getTipoUbicacion().isEmpty()) {
                                tUbicaciones.addCell(new Cell()
                                                .add(new Paragraph("Tipo de ubicación").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tUbicaciones.addCell(
                                                new Cell().add(new Paragraph(ubicacion.getTipoUbicacion())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!ubicacion.getIDUbicacion().isEmpty()) {
                                tUbicaciones.addCell(new Cell()
                                                .add(new Paragraph("ID ubicación").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tUbicaciones
                                                .addCell(new Cell()
                                                                .add(new Paragraph(ubicacion.getIDUbicacion())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!ubicacion.getRFCRemitenteDestinatario().isEmpty()) {
                                tUbicaciones.addCell(new Cell().add(new Paragraph(
                                                "R.F.C del " + (ubicacion.getTipoUbicacion().equals("Origen")
                                                                ? "Remitente"
                                                                : "Destinario"))
                                                .addStyle(estiloParrafoAtributo)).addStyle(estiloCelda));
                                tUbicaciones.addCell(new Cell()
                                                .add(new Paragraph(ubicacion.getRFCRemitenteDestinatario())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!ubicacion.getNombreRemitenteDestinatario().isEmpty()) {
                                tUbicaciones.addCell(new Cell().add(new Paragraph(
                                                "Nombre del " + (ubicacion.getTipoUbicacion().equals("Origen")
                                                                ? "Remitente"
                                                                : "Destinario"))
                                                .addStyle(estiloParrafoAtributo)).addStyle(estiloCelda));
                                tUbicaciones.addCell(new Cell()
                                                .add(new Paragraph(ubicacion.getNombreRemitenteDestinatario())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!ubicacion.getNumRegIdTrib().isEmpty()) {
                                tUbicaciones
                                                .addCell(new Cell()
                                                                .add(new Paragraph("Núm. Reg. Id. Trib.")
                                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tUbicaciones
                                                .addCell(new Cell()
                                                                .add(new Paragraph(ubicacion.getNumRegIdTrib())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!ubicacion.getResidenciaFiscal().isEmpty()) {
                                tUbicaciones.addCell(new Cell()
                                                .add(new Paragraph("Residencia fiscal").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tUbicaciones.addCell(
                                                new Cell().add(new Paragraph(ubicacion.getResidenciaFiscal())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!ubicacion.getNumEstacion().isEmpty()) {
                                tUbicaciones.addCell(new Cell()
                                                .add(new Paragraph("Número de estación")
                                                                .addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tUbicaciones
                                                .addCell(new Cell()
                                                                .add(new Paragraph(ubicacion.getNumEstacion())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!ubicacion.getNombreEstacion().isEmpty()) {
                                tUbicaciones.addCell(new Cell()
                                                .add(new Paragraph("Nombre de estación")
                                                                .addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tUbicaciones.addCell(
                                                new Cell().add(new Paragraph(ubicacion.getNombreEstacion())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!ubicacion.getNavegacionTrafico().isEmpty()) {
                                tUbicaciones.addCell(new Cell().add(new Paragraph("Tipo de puerto de"
                                                + (ubicacion.getTipoUbicacion().equals("Origen") ? "Remitente"
                                                                : "Destinario"))
                                                .addStyle(estiloParrafoAtributo)).addStyle(estiloCelda));
                                tUbicaciones.addCell(
                                                new Cell().add(new Paragraph(ubicacion.getNavegacionTrafico())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }

                        tUbicaciones.addCell(
                                        new Cell().add(new Paragraph("Fecha y hora").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tUbicaciones.addCell(new Cell()
                                        .add(new Paragraph(Utilidades.ToString(ubicacion.getFechaHoraSalidaLlegada()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));

                        if (!ubicacion.getTipoEstacion().isEmpty()) {
                                tUbicaciones.addCell(new Cell()
                                                .add(new Paragraph("Tipo de estación").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tUbicaciones
                                                .addCell(new Cell()
                                                                .add(new Paragraph(ubicacion.getTipoEstacion())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (ubicacion.getDistanciaRecorrida() > 0) {
                                tUbicaciones
                                                .addCell(new Cell()
                                                                .add(new Paragraph("Distancia recorrida")
                                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tUbicaciones.addCell(
                                                new Cell().add(new Paragraph(String.format("%.2f km",
                                                                ubicacion.getDistanciaRecorrida()))
                                                                .addStyle(estiloParrafoValor)).addStyle(estiloCelda));
                        }
                        tUbicaciones.startNewRow();
                        DOCUMENTO.add(tUbicaciones);
                        AgregarDomicilioCartaPorte20(ubicacion.getDomicilio());
                }
        }

        private void AgregarDomicilioCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Domicilio domicilio) {
                if (domicilio == null) {
                        return;
                }
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tDomicilio = new Table(tamanoColumnas);
                tDomicilio.setMarginTop(7);

                tDomicilio.addCell(
                                new Cell(1, 4).add(new Paragraph("DOMICILIO").addStyle(estiloParrafoTitle))
                                                .addStyle(estiloCeldaTitle));
                if (!domicilio.getCalle().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Calle").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph(domicilio.getCalle()).addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!domicilio.getNumeroExterior().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Número exterior").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(new Cell()
                                        .add(new Paragraph(domicilio.getNumeroExterior()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!domicilio.getNumeroInterior().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Número interior").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(new Cell()
                                        .add(new Paragraph(domicilio.getNumeroInterior()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!domicilio.getColonia().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Colonia").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(new Cell()
                                        .add(new Paragraph(domicilio.getColonia()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!domicilio.getLocalidad().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Localidad").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(new Cell()
                                        .add(new Paragraph(domicilio.getLocalidad()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!domicilio.getReferencia().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Referencia").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(new Cell()
                                        .add(new Paragraph(domicilio.getReferencia()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!domicilio.getMunicipio().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Municipio").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(new Cell()
                                        .add(new Paragraph(domicilio.getMunicipio()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!domicilio.getEstado().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Estado").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(new Cell()
                                        .add(new Paragraph(domicilio.getEstado()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!domicilio.getPais().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Pais").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph(domicilio.getPais()).addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!domicilio.getCodigoPostal().isEmpty()) {
                        tDomicilio.addCell(
                                        new Cell().add(new Paragraph("Código postal").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDomicilio.addCell(new Cell()
                                        .add(new Paragraph(domicilio.getCodigoPostal()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }

                tDomicilio.startNewRow();

                DOCUMENTO.add(tDomicilio);

        }

        public void AgregarMercanciasCartaPorte20() {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tMercancias = new Table(tamanoColumnas);
                tMercancias.setMarginTop(7);

                tMercancias.addCell(new Cell(1, 4).add(new Paragraph("MERCANCIAS").addStyle(estiloParrafoTitle))
                                .addStyle(estiloCeldaTitle));
                com.itextpdf.xmltopdf.CartaPorte20.Mercancias mercancias = COMPROBANTE.getComplemento().CartaPorte20
                                .getMercancias();
                if (mercancias.getPesoBrutoTotal() > 0) {
                        tMercancias.addCell(new Cell()
                                        .add(new Paragraph("Peso bruto total").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tMercancias.addCell(new Cell().add(
                                        new Paragraph(String.format("%.2f", mercancias.getPesoBrutoTotal()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!mercancias.getUnidadPeso().isEmpty()) {
                        tMercancias.addCell(
                                        new Cell().add(new Paragraph("Unidad de peso").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tMercancias.addCell(new Cell()
                                        .add(new Paragraph(mercancias.getUnidadPeso()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (mercancias.getPesoNetoTotal() > 0) {
                        tMercancias.addCell(
                                        new Cell().add(new Paragraph("Peso neto total").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tMercancias.addCell(new Cell().add(
                                        new Paragraph(String.format("%.2f", mercancias.getPesoNetoTotal()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (mercancias.getNumTotalMercancias() > 0) {
                        tMercancias
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Número total de mercancias")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tMercancias.addCell(new Cell().add(
                                        new Paragraph(Integer.toString(mercancias.getNumTotalMercancias()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (mercancias.getCargoPorTasacion() > 0) {
                        tMercancias.addCell(new Cell()
                                        .add(new Paragraph("Cargo por tasación").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tMercancias.addCell(new Cell().add(
                                        new Paragraph(String.format("%.2f", mercancias.getCargoPorTasacion()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                tMercancias.startNewRow();
                DOCUMENTO.add(tMercancias);
                AgregarMercanciaCartaPorte20(mercancias);
                AgregarAutotransporteCartaPorte20(mercancias);
                AgregarTransporteMaritimoCartaPorte20(mercancias);
                AgregarTransporteAereoCartaPorte20(mercancias);
                AgregarTransporteFerroviarioCartaPorte20(mercancias);

        }

        private void AgregarMercanciaCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Mercancias mercancias) {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                int contador = 0;
                for (int i = 0; i < mercancias.getMercancia().size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte20.Mercancia mercancia = mercancias.getMercancia().get(i);
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tMercancias = new Table(tamanoColumnas);
                        tMercancias.setMarginTop(7);

                        tMercancias.addCell(new Cell(1, 4)
                                        .add(new Paragraph("MERCANCIA " + contador).addStyle(estiloParrafoTitle))
                                        .addStyle(estiloCeldaTitle));
                        if (!mercancia.getBienesTransp().isEmpty()) {
                                tMercancias
                                                .addCell(new Cell()
                                                                .add(new Paragraph("Bienes transportados")
                                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tMercancias
                                                .addCell(new Cell()
                                                                .add(new Paragraph(mercancia.getBienesTransp())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getClaveSTCC().isEmpty()) {
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph("Clave STTC").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph(mercancia.getClaveSTCC())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getDescripcion().isEmpty()) {
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph("Descripción").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tMercancias
                                                .addCell(new Cell()
                                                                .add(new Paragraph(mercancia.getDescripcion())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (mercancia.getCantidad() > 0) {
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph("Cantidad").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph(String.format("%.2f", mercancia.getCantidad()))
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getClaveUnidad().isEmpty()) {
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph("Clave unidad").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tMercancias
                                                .addCell(new Cell()
                                                                .add(new Paragraph(mercancia.getClaveUnidad())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getUnidad().isEmpty()) {
                                tMercancias.addCell(
                                                new Cell().add(new Paragraph("Unidad").addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph(mercancia.getUnidad()).addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getDimensiones().isEmpty()) {
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph("Dimensiones").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tMercancias
                                                .addCell(new Cell()
                                                                .add(new Paragraph(mercancia.getDimensiones())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getMaterialPeligroso().isEmpty()) {
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph("Material peligroso")
                                                                .addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tMercancias.addCell(
                                                new Cell().add(new Paragraph(mercancia.getMaterialPeligroso())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getCveMaterialPeligroso().isEmpty()) {
                                tMercancias.addCell(
                                                new Cell().add(new Paragraph("Clave material peligroso")
                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tMercancias.addCell(
                                                new Cell().add(new Paragraph(mercancia.getCveMaterialPeligroso())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getEmbalaje().isEmpty()) {
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph("Embalaje").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph(mercancia.getEmbalaje())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getDescripEmbalaje().isEmpty()) {
                                tMercancias
                                                .addCell(new Cell()
                                                                .add(new Paragraph("Descripción embalaje")
                                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tMercancias.addCell(
                                                new Cell().add(new Paragraph(mercancia.getDescripEmbalaje())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (mercancia.getPesoEnKg() > 0) {
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph("Peso en kg").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tMercancias.addCell(new Cell().add(
                                                new Paragraph(String.format("%.3f km", mercancia.getPesoEnKg()))
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (mercancia.getValorMercancia() > 0) {
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph("Valor mercancia").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tMercancias
                                                .addCell(new Cell()
                                                                .add(new Paragraph(String.format("%.2f km",
                                                                                mercancia.getValorMercancia()))
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getMoneda().isEmpty()) {
                                tMercancias.addCell(
                                                new Cell().add(new Paragraph("Moneda").addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tMercancias.addCell(new Cell()
                                                .add(new Paragraph(mercancia.getMoneda()).addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getFraccionArancelaria().isEmpty()) {
                                tMercancias
                                                .addCell(new Cell()
                                                                .add(new Paragraph("Fraccion arancelaria")
                                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tMercancias.addCell(
                                                new Cell().add(new Paragraph(mercancia.getFraccionArancelaria())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!mercancia.getUUIDComercioExt().isEmpty()) {
                                tMercancias
                                                .addCell(new Cell()
                                                                .add(new Paragraph("UUID comercio exterior")
                                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tMercancias.addCell(
                                                new Cell().add(new Paragraph(mercancia.getUUIDComercioExt())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        tMercancias.startNewRow();
                        DOCUMENTO.add(tMercancias);
                        AgregarPedimentosCartaPorte20(mercancia);
                        AgregarGuiasIdentificacionCartaPorte20(mercancia);
                        AgregarCantidadTransportaCartaPorte20(mercancia);
                        AgregarDetalleMercanciaCartaPorte20(mercancia);
                }
        }

        private void AgregarPedimentosCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Mercancia mercancia) {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                int contador = 0;
                for (int i = 0; i < mercancia.getPedimentos().size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte20.Pedimentos pedimento = mercancia.getPedimentos().get(i);
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tPedimentos = new Table(tamanoColumnas);
                        tPedimentos.setMarginTop(7);

                        tPedimentos.addCell(new Cell(1, 4)
                                        .add(new Paragraph("PEDIMENTO " + contador).addStyle(estiloParrafoTitle))
                                        .addStyle(estiloCeldaTitle));
                        if (!pedimento.getPedimento().isEmpty()) {
                                tPedimentos.addCell(new Cell()
                                                .add(new Paragraph("Pedimento").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tPedimentos.addCell(new Cell()
                                                .add(new Paragraph(pedimento.getPedimento())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }

                        tPedimentos.startNewRow();
                        DOCUMENTO.add(tPedimentos);
                }
        }

        private void AgregarGuiasIdentificacionCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Mercancia mercancia) {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                int contador = 0;
                for (int i = 0; i < mercancia.getGuiasIdentificacion().size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte20.GuiasIdentificacion guiasIdentificacion = mercancia
                                        .getGuiasIdentificacion().get(i);
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tguiasIdentificacion = new Table(tamanoColumnas);
                        tguiasIdentificacion.setMarginTop(7);

                        tguiasIdentificacion.addCell(
                                        new Cell(1, 4).add(new Paragraph("GUÍA IDENTIFICACIÓN " + contador)
                                                        .addStyle(estiloParrafoTitle))
                                                        .addStyle(estiloCeldaTitle));
                        if (!guiasIdentificacion.getNumeroGuiaIdentificacion().isEmpty()) {
                                tguiasIdentificacion.addCell(new Cell()
                                                .add(new Paragraph("Número de guía").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tguiasIdentificacion.addCell(new Cell().add(
                                                new Paragraph(guiasIdentificacion.getNumeroGuiaIdentificacion())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!guiasIdentificacion.getDescripGuiaIdentificacion().isEmpty()) {
                                tguiasIdentificacion.addCell(new Cell()
                                                .add(new Paragraph("Descripción").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tguiasIdentificacion.addCell(new Cell().add(
                                                new Paragraph(guiasIdentificacion.getDescripGuiaIdentificacion())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!guiasIdentificacion.getNumeroGuiaIdentificacion().isEmpty()) {
                                tguiasIdentificacion.addCell(new Cell()
                                                .add(new Paragraph("Peso del paquete").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tguiasIdentificacion.addCell(new Cell().add(
                                                new Paragraph(guiasIdentificacion.getNumeroGuiaIdentificacion())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }

                        tguiasIdentificacion.startNewRow();
                        DOCUMENTO.add(tguiasIdentificacion);
                }
        }

        private void AgregarCantidadTransportaCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Mercancia mercancia) {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                int contador = 0;
                for (int i = 0; i < mercancia.getCantidadTransporta().size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte20.CantidadTransporta cantidadTransporta = mercancia
                                        .getCantidadTransporta()
                                        .get(i);
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tCantidadTransporta = new Table(tamanoColumnas);
                        tCantidadTransporta.setMarginTop(7);

                        tCantidadTransporta.addCell(
                                        new Cell(1, 4).add(new Paragraph("CANTIDAD TRANSPORTA " + contador)
                                                        .addStyle(estiloParrafoTitle))
                                                        .addStyle(estiloCeldaTitle));
                        if (cantidadTransporta.getCantidad() > 0) {
                                tCantidadTransporta.addCell(new Cell()
                                                .add(new Paragraph("Cantidad").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tCantidadTransporta
                                                .addCell(new Cell()
                                                                .add(new Paragraph(String.format("%.6f",
                                                                                cantidadTransporta.getCantidad()))
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!cantidadTransporta.getIDOrigen().isEmpty()) {
                                tCantidadTransporta.addCell(new Cell()
                                                .add(new Paragraph("ID origen").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tCantidadTransporta.addCell(
                                                new Cell().add(new Paragraph(cantidadTransporta.getIDOrigen())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!cantidadTransporta.getIDDestino().isEmpty()) {
                                tCantidadTransporta.addCell(new Cell()
                                                .add(new Paragraph("ID destino").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tCantidadTransporta.addCell(
                                                new Cell().add(new Paragraph(cantidadTransporta.getIDDestino())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!cantidadTransporta.getCvesTransporte().isEmpty()) {
                                tCantidadTransporta.addCell(new Cell()
                                                .add(new Paragraph("Clave transporte").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tCantidadTransporta.addCell(new Cell()
                                                .add(new Paragraph(cantidadTransporta.getCvesTransporte())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        tCantidadTransporta.startNewRow();
                        DOCUMENTO.add(tCantidadTransporta);
                }
        }

        private void AgregarDetalleMercanciaCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Mercancia mercancia) {
                if (mercancia.getDetalleMercancia() == null) {
                        return;
                }
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                com.itextpdf.xmltopdf.CartaPorte20.DetalleMercancia detalleMercancia = mercancia.getDetalleMercancia();
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tDetalleMercancia = new Table(tamanoColumnas);
                tDetalleMercancia.setMarginTop(7);

                tDetalleMercancia.addCell(
                                new Cell(1, 4).add(new Paragraph("DETALLE MERCANCIA").addStyle(estiloParrafoTitle))
                                                .addStyle(estiloCeldaTitle));
                if (!detalleMercancia.getUnidadPesoMerc().isEmpty()) {
                        tDetalleMercancia.addCell(
                                        new Cell().add(new Paragraph("Unidad peso").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDetalleMercancia.addCell(
                                        new Cell().add(new Paragraph(detalleMercancia.getUnidadPesoMerc())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (detalleMercancia.getPesoBruto() > 0) {
                        tDetalleMercancia.addCell(
                                        new Cell().add(new Paragraph("Peso bruto").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDetalleMercancia.addCell(new Cell().add(
                                        new Paragraph(String.format("%.6f", detalleMercancia.getPesoBruto()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (detalleMercancia.getPesoNeto() > 0) {
                        tDetalleMercancia.addCell(
                                        new Cell().add(new Paragraph("Peso neto").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDetalleMercancia.addCell(new Cell().add(
                                        new Paragraph(String.format("%.6f", detalleMercancia.getPesoBruto()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (detalleMercancia.getPesoTara() > 0) {
                        tDetalleMercancia.addCell(
                                        new Cell().add(new Paragraph("Peso tara").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tDetalleMercancia.addCell(new Cell().add(
                                        new Paragraph(String.format("%.6f", detalleMercancia.getPesoBruto()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (detalleMercancia.getNumPiezas() > 0) {
                        tDetalleMercancia.addCell(new Cell()
                                        .add(new Paragraph("Número de piezas").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tDetalleMercancia.addCell(new Cell()
                                        .add(new Paragraph(Integer.toString(detalleMercancia.getNumPiezas()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }

                tDetalleMercancia.startNewRow();
                DOCUMENTO.add(tDetalleMercancia);

        }

        private void AgregarAutotransporteCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Mercancias mercancias) {
                if (mercancias.getAutotransporte() == null) {
                        return;
                }
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                com.itextpdf.xmltopdf.CartaPorte20.Autotransporte autoTransporte = mercancias.getAutotransporte();
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tAutoTransporte = new Table(tamanoColumnas);
                tAutoTransporte.setMarginTop(7);

                tAutoTransporte.addCell(new Cell(1, 4).add(new Paragraph("AUTOTRANSPORTE").addStyle(estiloParrafoTitle))
                                .addStyle(estiloCeldaTitle));
                if (!autoTransporte.getPermSCT().isEmpty()) {
                        tAutoTransporte.addCell(
                                        new Cell().add(new Paragraph("Permiso SCT").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tAutoTransporte
                                        .addCell(new Cell()
                                                        .add(new Paragraph(autoTransporte.getPermSCT())
                                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!autoTransporte.getNumPermisoSCT().isEmpty()) {
                        tAutoTransporte.addCell(
                                        new Cell().add(new Paragraph("Núm permiso SCT").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tAutoTransporte.addCell(
                                        new Cell().add(new Paragraph(autoTransporte.getNumPermisoSCT())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                tAutoTransporte.startNewRow();
                DOCUMENTO.add(tAutoTransporte);
                AgregarIdentificacionVehicularCartaPorte20(autoTransporte);
                AgregarSegurosCartaPorte20(autoTransporte);
                AgregarRemolquesCartaPorte20(autoTransporte);
        }

        private void AgregarIdentificacionVehicularCartaPorte20(
                        com.itextpdf.xmltopdf.CartaPorte20.Autotransporte autoTransporte) {
                if (autoTransporte.getIdentificacionVehicular() == null) {
                        return;
                }
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                com.itextpdf.xmltopdf.CartaPorte20.IdentificacionVehicular identificacionVehicular = autoTransporte
                                .getIdentificacionVehicular();
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tIdentificacionVehicular = new Table(tamanoColumnas);
                tIdentificacionVehicular.setMarginTop(7);

                tIdentificacionVehicular
                                .addCell(new Cell(1, 4)
                                                .add(new Paragraph("IDENTIFICACIÓN VEHICULAR")
                                                                .addStyle(estiloParrafoTitle))
                                                .addStyle(estiloCeldaTitle));
                if (!identificacionVehicular.getConfigVehicular().isEmpty()) {
                        tIdentificacionVehicular.addCell(new Cell()
                                        .add(new Paragraph("Clave autotransporte").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tIdentificacionVehicular.addCell(new Cell()
                                        .add(new Paragraph(identificacionVehicular.getConfigVehicular())
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!identificacionVehicular.getPlacaVM().isEmpty()) {
                        tIdentificacionVehicular.addCell(
                                        new Cell().add(new Paragraph("Placa").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tIdentificacionVehicular.addCell(
                                        new Cell().add(new Paragraph(identificacionVehicular.getPlacaVM())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (identificacionVehicular.getAnioModeloVM() > 0) {
                        tIdentificacionVehicular.addCell(
                                        new Cell().add(new Paragraph("Año").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tIdentificacionVehicular
                                        .addCell(new Cell()
                                                        .add(new Paragraph(Integer.toString(
                                                                        identificacionVehicular.getAnioModeloVM()))
                                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                tIdentificacionVehicular.startNewRow();
                DOCUMENTO.add(tIdentificacionVehicular);
        }

        private void AgregarSegurosCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Autotransporte autoTransporte) {
                if (autoTransporte.getSeguros() == null) {
                        return;
                }
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                com.itextpdf.xmltopdf.CartaPorte20.Seguros seguros = autoTransporte.getSeguros();
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tSeguros = new Table(tamanoColumnas);
                tSeguros.setMarginTop(7);

                tSeguros.addCell(
                                new Cell(1, 4).add(new Paragraph("SEGUROS").addStyle(estiloParrafoTitle))
                                                .addStyle(estiloCeldaTitle));
                if (!seguros.getAseguraRespCivil().isEmpty()) {
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph("Nombre aseguradora civil").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph(seguros.getAseguraRespCivil()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!seguros.getPolizaRespCivil().isEmpty()) {
                        tSeguros.addCell(
                                        new Cell().add(new Paragraph("Núm. poliza aseguradora civil")
                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph(seguros.getPolizaRespCivil()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!seguros.getAseguraMedAmbiente().isEmpty()) {
                        tSeguros.addCell(
                                        new Cell().add(new Paragraph("Nombre aseguradora medio ambiente")
                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph(seguros.getAseguraMedAmbiente())
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!seguros.getPolizaMedAmbiente().isEmpty()) {
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph("Número poloza aseguradora medio ambiente")
                                                        .addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph(seguros.getPolizaMedAmbiente()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!seguros.getAseguraCarga().isEmpty()) {
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph("Nombre aseguradora carga").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph(seguros.getAseguraCarga()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!seguros.getPolizaCarga().isEmpty()) {
                        tSeguros.addCell(
                                        new Cell().add(new Paragraph("Número poliza aseguradora carga")
                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph(seguros.getPolizaCarga()).addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }

                if (seguros.getPrimaSeguro() > 0) {
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph("Valor importe asegurado").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tSeguros.addCell(new Cell()
                                        .add(new Paragraph(String.format("%.2f", seguros.getPrimaSeguro()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                tSeguros.startNewRow();
                DOCUMENTO.add(tSeguros);
        }

        private void AgregarRemolquesCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Autotransporte autoTransporte) {
                if (autoTransporte.getRemolques() == null) {
                        return;
                }
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                List<com.itextpdf.xmltopdf.CartaPorte20.Remolque> remolques = autoTransporte.getRemolques()
                                .getRemolque();
                int contador = 0;
                for (int i = 0; i < remolques.size(); i++) {
                        contador++;
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tRemolques = new Table(tamanoColumnas);
                        tRemolques.setMarginTop(7);

                        tRemolques.addCell(new Cell(1, 4)
                                        .add(new Paragraph("REMOLQUE" + contador).addStyle(estiloParrafoTitle))
                                        .addStyle(estiloCeldaTitle));
                        com.itextpdf.xmltopdf.CartaPorte20.Remolque remolque = remolques.get(i);
                        if (!remolque.getSubTipoRem().isEmpty()) {
                                tRemolques.addCell(new Cell()
                                                .add(new Paragraph("Subtipo de remolque o semiremolque")
                                                                .addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tRemolques.addCell(new Cell()
                                                .add(new Paragraph(remolque.getSubTipoRem())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!remolque.getPlaca().isEmpty()) {
                                tRemolques.addCell(
                                                new Cell().add(new Paragraph("Placa").addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tRemolques.addCell(new Cell()
                                                .add(new Paragraph(remolque.getPlaca()).addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        tRemolques.startNewRow();
                        DOCUMENTO.add(tRemolques);
                }
        }

        private void AgregarTransporteMaritimoCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Mercancias mercancias) {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                if (mercancias.getTransporteMaritimo() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte20.TransporteMaritimo transporteMaritimo = mercancias
                                .getTransporteMaritimo();
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tTransporteMaritimo = new Table(tamanoColumnas);
                tTransporteMaritimo.setMarginTop(7);

                tTransporteMaritimo.addCell(new Cell(1, 4)
                                .add(new Paragraph("TRANSPORTE MARITIMO").addStyle(estiloParrafoTitle))
                                .addStyle(estiloCeldaTitle));
                if (!transporteMaritimo.getPermSCT().isEmpty()) {
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Permiso SCT").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteMaritimo.getPermSCT())
                                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumPermisoSCT().isEmpty()) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Núm. permiso SCT").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNumPermisoSCT())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNombreAseg().isEmpty()) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Nombre aseguradora").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNombreAseg())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumPolizaSeguro().isEmpty()) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Núm. poliza seguro").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNumPolizaSeguro())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getTipoEmbarcacion().isEmpty()) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Tipo de embarcación").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getTipoEmbarcacion())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getMatricula().isEmpty()) {
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Matricula").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getMatricula())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumeroOMI().isEmpty()) {
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Número OMI").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNumeroOMI())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getAnioEmbarcacion() > 0) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Año de embarcación").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(Integer.toString(
                                                                        transporteMaritimo.getAnioEmbarcacion()))
                                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNombreEmbarc().isEmpty()) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Nombre de embarcación").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNombreEmbarc())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNacionalidadEmbarc().isEmpty()) {
                        tTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Nacionalidad embarcación")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(transporteMaritimo.getNacionalidadEmbarc())
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getUnidadesDeArqBruto() > 0) {
                        tTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Unidades de arqueo bruto")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(String.format("%.3f",
                                                        transporteMaritimo.getUnidadesDeArqBruto()))
                                                        .addStyle(estiloParrafoValor)).addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getTipoCarga().isEmpty()) {
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Tipo de carga").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getTipoCarga())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumCertITC().isEmpty()) {
                        tTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Número de certificado ITC")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNumCertITC())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getEslora() > 0) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Longitud de eslora").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(new Cell().add(
                                        new Paragraph(String.format("%.3f", transporteMaritimo.getEslora()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getManga() > 0) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Longitud de manga").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(new Cell().add(
                                        new Paragraph(String.format("%.3f", transporteMaritimo.getManga()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (transporteMaritimo.getCalado() > 0) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Longitud del calado").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(new Cell().add(
                                        new Paragraph(String.format("%.3f", transporteMaritimo.getCalado()))
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getLineaNaviera().isEmpty()) {
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Linea naviera").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getLineaNaviera())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNombreAgenteNaviero().isEmpty()) {
                        tTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Nombre del agente naviero")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(transporteMaritimo.getNombreAgenteNaviero())
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumAutorizacionNaviero().isEmpty()) {
                        tTransporteMaritimo
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Número de la autorización")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph(transporteMaritimo.getNumAutorizacionNaviero())
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumViaje().isEmpty()) {
                        tTransporteMaritimo.addCell(new Cell()
                                        .add(new Paragraph("Número del viaje").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNumViaje())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteMaritimo.getNumConocEmbarc().isEmpty()) {
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph("Número de conocimiento de embarque")
                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteMaritimo.addCell(
                                        new Cell().add(new Paragraph(transporteMaritimo.getNumConocEmbarc())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                tTransporteMaritimo.startNewRow();
                DOCUMENTO.add(tTransporteMaritimo);
                AgregarContenedorCartaPorte20(transporteMaritimo);
        }

        private void AgregarContenedorCartaPorte20(
                        com.itextpdf.xmltopdf.CartaPorte20.TransporteMaritimo transporteMaritimo) {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                int contador = 0;
                for (int i = 0; i < transporteMaritimo.getContenedor().size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte20.Contenedor contenedor = transporteMaritimo.getContenedor()
                                        .get(i);
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tContenedor = new Table(tamanoColumnas);
                        tContenedor.setMarginTop(7);

                        tContenedor.addCell(new Cell(1, 4)
                                        .add(new Paragraph("CONTENEDOR " + contador).addStyle(estiloParrafoTitle))
                                        .addStyle(estiloCeldaTitle));
                        if (!contenedor.getMatriculaContenedor().isEmpty()) {
                                tContenedor.addCell(
                                                new Cell().add(new Paragraph("Matricula del contenedor")
                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tContenedor.addCell(
                                                new Cell().add(new Paragraph(contenedor.getMatriculaContenedor())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!contenedor.getTipoContenedor().isEmpty()) {
                                tContenedor.addCell(new Cell()
                                                .add(new Paragraph("Tipo de contenedor")
                                                                .addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tContenedor.addCell(
                                                new Cell().add(new Paragraph(contenedor.getTipoContenedor())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!contenedor.getNumPrecinto().isEmpty()) {
                                tContenedor.addCell(
                                                new Cell().add(new Paragraph("Núm del sello o precinto")
                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tContenedor
                                                .addCell(new Cell()
                                                                .add(new Paragraph(contenedor.getNumPrecinto())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }

                        tContenedor.startNewRow();
                        DOCUMENTO.add(tContenedor);
                }
        }

        private void AgregarTransporteAereoCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Mercancias mercancias) {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                if (mercancias.getTransporteMaritimo() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte20.TransporteAereo transporteAereo = mercancias.getTransporteAereo();
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tTransporteAereo = new Table(tamanoColumnas);
                tTransporteAereo.setMarginTop(7);

                tTransporteAereo.addCell(
                                new Cell(1, 4).add(new Paragraph("TRANSPORTE AEREO").addStyle(estiloParrafoTitle))
                                                .addStyle(estiloCeldaTitle));
                if (!transporteAereo.getPermSCT().isEmpty()) {
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Permiso SCT").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getPermSCT())
                                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNumPermisoSCT().isEmpty()) {
                        tTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph("Núm. permiso SCT").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getNumPermisoSCT())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getMatriculaAeronave().isEmpty()) {
                        tTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph("Matricula aeronave").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getMatriculaAeronave())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNombreAseg().isEmpty()) {
                        tTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph("Nombre aseguradora").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getNombreAseg())
                                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNumPolizaSeguro().isEmpty()) {
                        tTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph("Núm. poliza seguro").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getNumPolizaSeguro())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNumeroGuia().isEmpty()) {
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Núm. de guía").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteAereo
                                        .addCell(new Cell()
                                                        .add(new Paragraph(transporteAereo.getNumeroGuia())
                                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getLugarContrato().isEmpty()) {
                        tTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph("Lugar de contrato").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getLugarContrato())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getCodigoTransportista().isEmpty()) {
                        tTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph("Código transportista").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getCodigoTransportista())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getRFCEmbarcador().isEmpty()) {
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("RFC Embarcador").addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getRFCEmbarcador())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNumRegIdTribEmbarc().isEmpty()) {
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Num. Reg. Id. Trib. Embarcador")
                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getNumRegIdTribEmbarc())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getResidenciaFiscalEmbarc().isEmpty()) {
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph("Residencia fiscal embarcador")
                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph(transporteAereo.getResidenciaFiscalEmbarc())
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteAereo.getNombreEmbarcador().isEmpty()) {
                        tTransporteAereo.addCell(new Cell()
                                        .add(new Paragraph("Nombre embarcador").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteAereo.addCell(
                                        new Cell().add(new Paragraph(transporteAereo.getNombreEmbarcador())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                tTransporteAereo.startNewRow();
                DOCUMENTO.add(tTransporteAereo);
        }

        private void AgregarTransporteFerroviarioCartaPorte20(
                        com.itextpdf.xmltopdf.CartaPorte20.Mercancias mercancias) {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                if (mercancias.getTransporteFerroviario() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte20.TransporteFerroviario transporteFerroviario = mercancias
                                .getTransporteFerroviario();
                float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                Table tTransporteFerroviario = new Table(tamanoColumnas);
                tTransporteFerroviario.setMarginTop(7);

                tTransporteFerroviario.addCell(new Cell(1, 4)
                                .add(new Paragraph("TRANSPORTE FERROVIARIO").addStyle(estiloParrafoTitle))
                                .addStyle(estiloCeldaTitle));
                if (!transporteFerroviario.getTipoDeServicio().isEmpty()) {
                        tTransporteFerroviario.addCell(new Cell()
                                        .add(new Paragraph("Tipo de servicio").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteFerroviario.addCell(new Cell()
                                        .add(new Paragraph(transporteFerroviario.getTipoDeServicio())
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                if (!transporteFerroviario.getTipoDeTrafico().isEmpty()) {
                        tTransporteFerroviario.addCell(new Cell()
                                        .add(new Paragraph("Tipo de trafico").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteFerroviario.addCell(
                                        new Cell().add(new Paragraph(transporteFerroviario.getTipoDeTrafico())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteFerroviario.getNombreAseg().isEmpty()) {
                        tTransporteFerroviario
                                        .addCell(new Cell()
                                                        .add(new Paragraph("Nombre de la aseguradora")
                                                                        .addStyle(estiloParrafoAtributo))
                                                        .addStyle(estiloCelda));
                        tTransporteFerroviario.addCell(
                                        new Cell().add(new Paragraph(transporteFerroviario.getNombreAseg())
                                                        .addStyle(estiloParrafoValor))
                                                        .addStyle(estiloCelda));
                }
                if (!transporteFerroviario.getNumPolizaSeguro().isEmpty()) {
                        tTransporteFerroviario.addCell(new Cell()
                                        .add(new Paragraph("Núm. poliza seguro").addStyle(estiloParrafoAtributo))
                                        .addStyle(estiloCelda));
                        tTransporteFerroviario.addCell(new Cell()
                                        .add(new Paragraph(transporteFerroviario.getNumPolizaSeguro())
                                                        .addStyle(estiloParrafoValor))
                                        .addStyle(estiloCelda));
                }
                tTransporteFerroviario.startNewRow();
                DOCUMENTO.add(tTransporteFerroviario);
                AgregarDerechosDePasoCartaPorte20(transporteFerroviario);
                AgregarCarroCartaPorte20(transporteFerroviario);
        }

        private void AgregarDerechosDePasoCartaPorte20(
                        com.itextpdf.xmltopdf.CartaPorte20.TransporteFerroviario transporteFerroviario) {
                if (transporteFerroviario.getDerechosDePaso().isEmpty()) {
                        return;
                }
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                List<com.itextpdf.xmltopdf.CartaPorte20.DerechosDePaso> derechosDePaso = transporteFerroviario
                                .getDerechosDePaso();
                int contador = 0;
                for (int i = 0; i < derechosDePaso.size(); i++) {
                        contador++;
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tDerechoDePaso = new Table(tamanoColumnas);
                        tDerechoDePaso.setMarginTop(7);

                        tDerechoDePaso.addCell(
                                        new Cell(1, 4).add(new Paragraph("DERECHO DE PASO " + contador)
                                                        .addStyle(estiloParrafoTitle))
                                                        .addStyle(estiloCeldaTitle));
                        com.itextpdf.xmltopdf.CartaPorte20.DerechosDePaso derechoDePaso = derechosDePaso.get(i);
                        if (!derechoDePaso.getTipoDerechoDePaso().isEmpty()) {
                                tDerechoDePaso
                                                .addCell(new Cell()
                                                                .add(new Paragraph("Tipo derecho de paso")
                                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tDerechoDePaso.addCell(
                                                new Cell().add(new Paragraph(derechoDePaso.getTipoDerechoDePaso())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (derechoDePaso.getKilometrajePagado() > 0) {
                                tDerechoDePaso
                                                .addCell(new Cell()
                                                                .add(new Paragraph("Kilometraje pagado")
                                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tDerechoDePaso.addCell(
                                                new Cell().add(new Paragraph(String.format("%.2f",
                                                                derechoDePaso.getKilometrajePagado()))
                                                                .addStyle(estiloParrafoValor)).addStyle(estiloCelda));
                        }
                        tDerechoDePaso.startNewRow();
                        DOCUMENTO.add(tDerechoDePaso);
                }
        }

        private void AgregarCarroCartaPorte20(
                        com.itextpdf.xmltopdf.CartaPorte20.TransporteFerroviario transporteFerroviario) {
                if (transporteFerroviario.getCarro().isEmpty()) {
                        return;
                }
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                List<com.itextpdf.xmltopdf.CartaPorte20.Carro> carros = transporteFerroviario.getCarro();
                int contador = 0;
                for (int i = 0; i < carros.size(); i++) {
                        contador++;
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tCarro = new Table(tamanoColumnas);
                        tCarro.setMarginTop(7);

                        tCarro.addCell(new Cell(1, 4)
                                        .add(new Paragraph("CARRO " + contador).addStyle(estiloParrafoTitle))
                                        .addStyle(estiloCeldaTitle));
                        com.itextpdf.xmltopdf.CartaPorte20.Carro carro = carros.get(i);
                        if (!carro.getTipoCarro().isEmpty()) {
                                tCarro.addCell(new Cell()
                                                .add(new Paragraph("Tipo de carro").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tCarro.addCell(new Cell()
                                                .add(new Paragraph(carro.getTipoCarro()).addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!carro.getMatriculaCarro().isEmpty()) {
                                tCarro.addCell(new Cell()
                                                .add(new Paragraph("Matricula carro").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tCarro.addCell(new Cell()
                                                .add(new Paragraph(carro.getMatriculaCarro())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (!carro.getGuiaCarro().isEmpty()) {
                                tCarro.addCell(new Cell()
                                                .add(new Paragraph("Guía carro").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tCarro.addCell(new Cell()
                                                .add(new Paragraph(carro.getGuiaCarro()).addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        if (carro.getToneladasNetasCarro() > 0) {
                                tCarro.addCell(new Cell()
                                                .add(new Paragraph("Toneladas de carro")
                                                                .addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tCarro.addCell(new Cell().add(
                                                new Paragraph(String.format("%.2f", carro.getToneladasNetasCarro()))
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        tCarro.startNewRow();
                        DOCUMENTO.add(tCarro);
                        AgregarContenedorCartaPorte20(carro);
                }
        }

        private void AgregarContenedorCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.Carro carro) {
                if (carro.getContenedor().isEmpty()) {
                        return;
                }
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                List<com.itextpdf.xmltopdf.CartaPorte20.CarroContenedor> contenedores = carro.getContenedor();
                int contador = 0;
                for (int i = 0; i < contenedores.size(); i++) {
                        contador++;
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tContenedor = new Table(tamanoColumnas);
                        tContenedor.setMarginTop(7);

                        tContenedor.addCell(new Cell(1, 4)
                                        .add(new Paragraph("CONTENEDOR " + contador).addStyle(estiloParrafoTitle))
                                        .addStyle(estiloCeldaTitle));
                        com.itextpdf.xmltopdf.CartaPorte20.CarroContenedor contenedor = contenedores.get(i);
                        if (!contenedor.getTipoContenedor().isEmpty()) {
                                tContenedor.addCell(new Cell()
                                                .add(new Paragraph("Tipo de contenedor")
                                                                .addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tContenedor.addCell(
                                                new Cell().add(new Paragraph(contenedor.getTipoContenedor())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (contenedor.getPesoContenedorVacio() > 0) {
                                tContenedor
                                                .addCell(new Cell()
                                                                .add(new Paragraph("Peso contenedor vacio")
                                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tContenedor.addCell(
                                                new Cell().add(new Paragraph(String.format("%.3f",
                                                                contenedor.getPesoContenedorVacio()))
                                                                .addStyle(estiloParrafoValor)).addStyle(estiloCelda));
                        }
                        if (contenedor.getPesoNetoMercancia() > 0) {
                                tContenedor.addCell(
                                                new Cell().add(new Paragraph("Peso neto mercancias carro")
                                                                .addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tContenedor
                                                .addCell(new Cell().add(new Paragraph(String.format("%.3f",
                                                                contenedor.getPesoNetoMercancia()))
                                                                .addStyle(estiloParrafoValor)).addStyle(estiloCelda));
                        }
                        tContenedor.startNewRow();
                        DOCUMENTO.add(tContenedor);
                }
        }

        public void AgregarFigurasTransporteCartaPorte20() {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                if (COMPROBANTE.getComplemento().CartaPorte20.getFiguraTransporte() == null) {
                        return;
                }
                com.itextpdf.xmltopdf.CartaPorte20.FiguraTransporte figuraTransporte = COMPROBANTE
                                .getComplemento().CartaPorte20
                                .getFiguraTransporte();
                int contador = 0;
                for (int i = 0; i < figuraTransporte.getTiposFigura().size(); i++) {
                        contador++;
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tTipoFigura = new Table(tamanoColumnas);
                        tTipoFigura.setMarginTop(7);
                        com.itextpdf.xmltopdf.CartaPorte20.TiposFigura tipoFigura = figuraTransporte.getTiposFigura()
                                        .get(i);
                        if (contador == 1) {
                                tTipoFigura
                                                .addCell(new Cell(1, 4)
                                                                .add(new Paragraph("FIGURAS DE TRANSPORTE")
                                                                                .addStyle(estiloParrafoTitle))
                                                                .addStyle(estiloCeldaTitle));
                        }
                        tTipoFigura.addCell(
                                        new Cell(1, 4).add(new Paragraph("TIPO DE FIGURA " + contador)
                                                        .addStyle(estiloParrafoTitle))
                                                        .addStyle(estiloCeldaTitle));
                        if (!tipoFigura.getTipoFigura().isEmpty()) {
                                tTipoFigura.addCell(new Cell()
                                                .add(new Paragraph("Tipo de figura").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tTipoFigura
                                                .addCell(new Cell()
                                                                .add(new Paragraph(tipoFigura.getTipoFigura())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!tipoFigura.getRFCFigura().isEmpty()) {
                                tTipoFigura.addCell(
                                                new Cell().add(new Paragraph("R.F.C").addStyle(estiloParrafoAtributo))
                                                                .addStyle(estiloCelda));
                                tTipoFigura
                                                .addCell(new Cell()
                                                                .add(new Paragraph(tipoFigura.getRFCFigura())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!tipoFigura.getNumLicencia().isEmpty()) {
                                tTipoFigura.addCell(new Cell()
                                                .add(new Paragraph("Número de licencia")
                                                                .addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tTipoFigura
                                                .addCell(new Cell()
                                                                .add(new Paragraph(tipoFigura.getNumLicencia())
                                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!tipoFigura.getNombreFigura().isEmpty()) {
                                tTipoFigura.addCell(new Cell()
                                                .add(new Paragraph("Nombre de figura").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tTipoFigura.addCell(
                                                new Cell().add(new Paragraph(tipoFigura.getNombreFigura())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!tipoFigura.getNumRegIdTribFigura().isEmpty()) {
                                tTipoFigura.addCell(new Cell()
                                                .add(new Paragraph("Núm. Reg. Id. Trib.")
                                                                .addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tTipoFigura.addCell(
                                                new Cell().add(new Paragraph(tipoFigura.getNumRegIdTribFigura())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        if (!tipoFigura.getResidenciaFiscalFigura().isEmpty()) {
                                tTipoFigura.addCell(new Cell()
                                                .add(new Paragraph("Residencia fiscal").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tTipoFigura.addCell(new Cell()
                                                .add(new Paragraph(tipoFigura.getResidenciaFiscalFigura())
                                                                .addStyle(estiloParrafoValor))
                                                .addStyle(estiloCelda));
                        }
                        tTipoFigura.startNewRow();
                        DOCUMENTO.add(tTipoFigura);
                        AgregarPartesTransporteCartaPorte20(tipoFigura);
                        AgregarDomicilioCartaPorte20(tipoFigura.getDomicilio());
                }
        }

        private void AgregarPartesTransporteCartaPorte20(com.itextpdf.xmltopdf.CartaPorte20.TiposFigura tipoFigura) {
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

                Style estiloParrafoTitle = new Style();
                estiloParrafoTitle.setFontSize(7);
                estiloParrafoTitle.setTextAlignment(TextAlignment.CENTER);
                estiloParrafoTitle.setFontColor(ColorConstants.WHITE);

                Style estiloParrafoAtributo = new Style();
                estiloParrafoAtributo.setFontSize(7);
                estiloParrafoAtributo.setBold();

                Style estiloParrafoValor = new Style();
                estiloParrafoValor.setFontSize(7);

                int contador = 0;
                for (int i = 0; i < tipoFigura.getPartesTransporte().size(); i++) {
                        contador++;
                        com.itextpdf.xmltopdf.CartaPorte20.PartesTransporte parteTransporte = tipoFigura
                                        .getPartesTransporte()
                                        .get(i);
                        float[] tamanoColumnas = Utilidades.cmToFloat(new float[] { 4f, 6f, 4f, 6f });
                        Table tPedimentos = new Table(tamanoColumnas);
                        tPedimentos.setMarginTop(7);

                        tPedimentos.addCell(
                                        new Cell(1, 4).add(new Paragraph("PARTE TRANSPORTE " + contador)
                                                        .addStyle(estiloParrafoTitle))
                                                        .addStyle(estiloCeldaTitle));
                        if (!parteTransporte.getParteTransporte().isEmpty()) {
                                tPedimentos.addCell(new Cell()
                                                .add(new Paragraph("Parte transporte").addStyle(estiloParrafoAtributo))
                                                .addStyle(estiloCelda));
                                tPedimentos.addCell(
                                                new Cell().add(new Paragraph(parteTransporte.getParteTransporte())
                                                                .addStyle(estiloParrafoValor))
                                                                .addStyle(estiloCelda));
                        }
                        tPedimentos.startNewRow();
                        DOCUMENTO.add(tPedimentos);
                }
        }

        // </editor-fold>

}
