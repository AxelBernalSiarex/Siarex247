/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siarex247.cumplimientoFiscal.ListaNegra;

import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
//import javafx.scene.text.Font;

/**
 *
 * @author Faustino
 */
class MyEventHandlerLista implements IEventHandler {

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        // PdfFont helvetica = PdfFontFactory.createFont(FontProgram..re.);
        // Rectangle pageSize = page.getPageSize();
        // PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(),
        // page.getResources(), pdf);
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);

        // Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
        int pageNumber = pdf.getNumberOfPages();
        // double x = pageSize.getWidth() / 2 - 150;
        // double y = 20;
        //
        // PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(),
        // page.getResources(), pdf);
        // new Canvas(canvas, new Rectangle(36, 20, page.getPageSize().getWidth() - 72,
        // 50))
        // .Add(table)
        // .Close();
        try {
            pdfCanvas.beginText();
            pdfCanvas.setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), 8);
            pdfCanvas.moveText(200, 23);
            pdfCanvas.showText(String.valueOf(
                    "Todos los derechos reservados Siarex Technologies                                             Página "
                            + pdf.getPageNumber(page) + " de " + pageNumber));
            pdfCanvas.endText();
            pdfCanvas.release();
        } catch (Exception error) {

        }
    }
}
