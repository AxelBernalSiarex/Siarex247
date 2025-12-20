package com.siarex247.leerCuenta;

import java.io.File;
import java.math.BigDecimal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AribaHtmParser {

    public OrdenCompraHtmData parse(File file) throws Exception {

        Document doc = Jsoup.parse(file, "UTF-8");
        OrdenCompraHtmData data = new OrdenCompraHtmData();

        // ================= ORDEN DE COMPRA =================
        // Se mantiene igual, ya que el selector es correcto para el span del número
        Element orden = doc.selectFirst("span.po-INSPON-doc-num");
        if (orden != null) {
            data.setOrdenCompra(orden.text().trim());
        }

        // ================= DESDE / EMPRESA =================
        // En Ariba, el emisor está en la clase 'fdml-ov-bill-to-gf-addr-name-val'
        Element empresaEmisora = doc.selectFirst(".fdml-ov-bill-to-gf-addr-name-val");
        if (empresaEmisora != null) {
            String nombreEmpresa = empresaEmisora.text().trim();
            data.setDesde(nombreEmpresa);
            data.setEmpresa(nombreEmpresa);
        } else {
            // Intento secundario por texto visual "From:"
            data.setDesde(extraerPorTextoVisual(doc, "From:"));
        }

        // ================= PARA (Receptor / Contacto) =================
        // El receptor suele estar bajo la etiqueta "To:" o en la sección de dirección
        Element receptor = doc.selectFirst(".po-INSSAddr-addr-details.ANXLabel b");
        if (receptor != null) {
            data.setPara(receptor.text().trim());
        } else {
            // Intento secundario por etiqueta visual "To:"
            data.setPara(extraerPorTextoVisual(doc, "To:"));
        }

        // ================= TAX ID =================
        // Se usa el método corregido de etiquetas de tabla
        data.setTaxId(extraerPorLabel(doc, "Customer VAT/Tax ID:"));

        // ================= MONEDA Y MONTO =================
        Element monto = doc.selectFirst(".po-INSPON-std-money");
        if (monto != null) {
            String txt = monto.text(); // Ej: $800,000.00 MXN
            if (txt.contains("MXN")) {
                data.setMoneda("MXN");
            } else if (txt.contains("USD")) {
                data.setMoneda("USD");
            }
            
            // Limpieza robusta del monto
            String limpio = txt.replaceAll("[^0-9.]", ""); 
            // Si el HTM usa formato 800,000.00, quitamos la coma y mantenemos el punto
            if (!limpio.isEmpty()) {
                data.setMonto(new BigDecimal(limpio));
            }
        }

        // ================= CLASIFICACIÓN =================
        data.setClasificacionDominio(extraerPorLabel(doc, "Classification Domain:"));
        data.setClasificacionCodigo(extraerPorLabel(doc, "Classification Code:"));

        // ================= EMAIL =================
        // El email está en una celda de tabla o bajo una etiqueta específica
        String email = extraerPorLabel(doc, "Email:");
        if (email == null) {
            // Búsqueda alternativa en la sección de Ship To
            Element emailElement = doc.selectFirst("span:contains(Email:) + span");
            email = (emailElement != null) ? emailElement.text().trim() : null;
        }
        data.setEmailDestino(email);

        return data;
    }

    /**
     * Busca el valor que sigue a una etiqueta visual de negrita o texto simple.
     */
    private String extraerPorTextoVisual(Document doc, String etiqueta) {
        // Busca elementos que contengan la etiqueta y extrae el texto siguiente
        Element el = doc.selectFirst("td:contains(" + etiqueta + "), b:contains(" + etiqueta + "), span:contains(" + etiqueta + ")");
        if (el != null) {
            // Si es un "To:", a veces el nombre está en el siguiente elemento o en el padre
            Element sibling = el.nextElementSibling();
            if (sibling != null) return sibling.text().trim();
            
            // Si no hay hermano, intentamos limpiar el texto del elemento mismo
            return el.text().replace(etiqueta, "").trim();
        }
        return null;
    }

    /**
     * Extrae valores tipo tabla:
     * <td>Label</td><td>Valor</td>
     */
    private String extraerPorLabel(Document doc, String label) {
        // Busca el TD que contiene el texto exacto y toma el TD hermano
        Element td = doc.selectFirst("td.base-ncd-label-top:contains(" + label + ") + td");
        if (td == null) {
            td = doc.selectFirst("td:contains(" + label + ") + td");
        }
        return td != null ? td.text().trim() : null;
    }
}