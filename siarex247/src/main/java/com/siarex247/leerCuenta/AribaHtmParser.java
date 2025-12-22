package com.siarex247.leerCuenta;

import java.io.File;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AribaHtmParser {

    public OrdenCompraHtmData parse(File file) throws Exception {

        final Logger logger = Logger.getLogger("siarex247");

        Document doc = Jsoup.parse(file, "UTF-8");
        OrdenCompraHtmData data = new OrdenCompraHtmData();

        // ================= ORDEN DE COMPRA =================
        Element orden = doc.selectFirst("span.po-INSPON-doc-num");
        if (orden != null) {
            data.setOrdenCompra(orden.text().trim());
        }

        // ================= DESDE / EMPRESA (NOMBRE COMPLETO RAW) =================
        // Extrae exactamente lo que viene en el HTML (Ej: "HP INC. HP Inc US71 IC Plant")
        String nombreEmpresa = null;

        // 1) Prioridad: Clase CSS de Ariba (Bill To / From)
        Element empresaEmisora = doc.selectFirst(".fdml-ov-bill-to-gf-addr-name-val");
        if (empresaEmisora != null) {
            nombreEmpresa = empresaEmisora.text().trim();
        }

        // 2) Fallback: Buscar por etiqueta "From/Desde"
        if (nombreEmpresa == null || nombreEmpresa.isEmpty()) {
            Element desdeLabel = doc.selectFirst("td:contains(From:), td:contains(Desde:)");
            if (desdeLabel != null) {
                Element parent = desdeLabel.parent();
                if (parent != null) {
                    Element b = parent.selectFirst("b, .fdml-ov-bill-to-gf-addr-name-val");
                    if (b != null) nombreEmpresa = b.text().trim();
                }
            }
        }

        // 3) Fallback Final: Footer (sent by / enviado por)
        if (nombreEmpresa == null || nombreEmpresa.isEmpty()) {
            Element footerInfo = doc.selectFirst("tr.po-INSPOD-rel-po-date td");
            if (footerInfo != null) {
                String text = footerInfo.text();
                if (text.contains("sent by ")) {
                    nombreEmpresa = text.split("sent by ")[1].split(" AN")[0].trim();
                } else if (text.contains("enviado por ")) {
                    nombreEmpresa = text.split("enviado por ")[1].split(" AN")[0].trim();
                }
            }
        }

        if (nombreEmpresa != null && !nombreEmpresa.isEmpty()) {
            data.setDesde(nombreEmpresa.replaceAll("\\s+", " "));
            data.setEmpresa(data.getDesde());
        }

        // ================= PARA (Receptor / Contacto) - FIX DEFINITIVO =================
        // En tu HTML real, el "To:" está en:
        // td.po-INSSAddr-To-label  -> luego td.po-INSSAddr-addr-details con <b>Nombre</b>
        String para = null;

        // 1) Ubica el bloque "To:" y toma el nombre dentro de esa misma tabla
        Element toTd = doc.selectFirst("td.po-INSSAddr-To-label"); // contiene <b>To:&nbsp;</b>
        if (toTd != null) {
            Element toTable = toTd.closest("table"); // WrapTableOverflowContents
            if (toTable != null) {
                Element nombre = toTable.selectFirst("td.po-INSSAddr-addr-details b");
                if (nombre != null) {
                    para = nombre.text().trim();
                }
            }
        }

        // 2) Fallback: primer nombre en ese formato (suele ser el To)
        if (para == null || para.isEmpty()) {
            Element b = doc.selectFirst("td.po-INSSAddr-addr-details b");
            if (b != null) {
                para = b.text().trim();
            }
        }

        data.setPara(para);

        // ✅ LOGS ya con valores seteados
        logger.info("HTM PARSED → DESDE: " + data.getDesde());
        logger.info("HTM PARSED → PARA : " + data.getPara());

        // ================= TAX ID =================
        data.setTaxId(extraerPorLabel(doc, "Customer VAT/Tax ID:"));
        if (data.getTaxId() == null) data.setTaxId(extraerPorLabel(doc, "RFC del cliente:"));

        // ================= MONEDA Y MONTO =================
        Element monto = doc.selectFirst(".po-INSPON-std-money");
        if (monto != null) {
            String txt = monto.text();

            if (txt.contains("MXN")) data.setMoneda("MXN");
            else if (txt.contains("USD")) data.setMoneda("USD");

            // OJO: "800,000.00" trae coma, esto lo normaliza
            String limpio = txt.replaceAll("[^0-9.,]", "");
            limpio = limpio.replace(",", "");

            if (!limpio.isEmpty()) {
                data.setMonto(new BigDecimal(limpio));
            }
        }

        // ================= OTROS DATOS =================
        data.setClasificacionDominio(extraerPorLabel(doc, "Classification Domain:"));
        data.setClasificacionCodigo(extraerPorLabel(doc, "Classification Code:"));

        // Email (en este HTML el email está en un td con "Email:" dentro del bloque To)
        String email = extraerPorLabel(doc, "Email:");
        if (email == null) {
            // Fallback alterno (por si el HTML cambia)
            Element emailElement = doc.selectFirst("span:contains(Email:) + span");
            email = (emailElement != null) ? emailElement.text().trim() : null;
        }
        data.setEmailDestino(email);

        return data;
    }

    // ================= MÉTODOS AUXILIARES =================

    private String extraerPorTextoVisual(Document doc, String etiqueta) {
        Element el = doc.selectFirst(
                "td:contains(" + etiqueta + "), b:contains(" + etiqueta + "), span:contains(" + etiqueta + ")"
        );
        if (el != null) {
            Element sibling = el.nextElementSibling();
            if (sibling != null) return sibling.text().trim();
            return el.text().replace(etiqueta, "").trim();
        }
        return null;
    }

    private String extraerPorLabel(Document doc, String label) {
        Element td = doc.selectFirst("td.base-ncd-label-top:contains(" + label + ") + td");
        if (td == null) {
            td = doc.selectFirst("td:contains(" + label + ") + td");
        }
        return td != null ? td.text().trim() : null;
    }
}
