/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

/**
 *
 * @author frack
 */
public final class GeneraPdf {
    public static void Generar(String rutaXML, String rutaPDF, String rutaImagen) {
        String version = ObtenerVersion(rutaXML);
        if (version.equals("3.3") || version.equals("Acuse") || version.equals("4.0") || version.equals("3.2")) {
            CreaPDF crearPDF = new CreaPDF();
            crearPDF.GenerarByXML(rutaXML, rutaPDF, rutaImagen);
            // CreaPDF.Generar(rutaXML, rutaPDF, rutaImagen, abrir);
        }
    }

    private static String ObtenerVersion(String rutaXML) {
        try {
            File file = new File(rutaXML);
            // an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document documento = db.parse(file);
            documento.getDocumentElement().normalize();

            if (documento.getElementsByTagName("Acuse") != null
                    && documento.getElementsByTagName("Acuse").getLength() > 0)
                return "Acuse";
            else if (documento.getElementsByTagName("cfdi:Comprobante") == null
                    || documento.getElementsByTagName("cfdi:Comprobante").getLength() == 0)
                return "-1";
            NodeList listaComprobante = documento.getElementsByTagName("cfdi:Comprobante");
            if (listaComprobante.getLength() > 0) {
                Element nComprobante = (Element) listaComprobante.item(0);
                if (nComprobante.hasAttribute("version"))
                    return nComprobante.getAttribute("version");
                else if (nComprobante.hasAttribute("Version"))
                    return nComprobante.getAttribute("Version");
                else
                    return "-1";
            } else
                return "-1";
        } catch (IOException | ParserConfigurationException | SAXException e) {
            return "-1";
        }
    }
}
