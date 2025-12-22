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
        String nombreEmpresa = null;

        // 1) Prioridad: Clase CSS de Ariba (Bill To / From)
        Element empresaEmisora = doc.selectFirst(".fdml-ov-bill-to-gf-addr-name-val");
        if (empresaEmisora != null) {
            nombreEmpresa = empresaEmisora.text().trim();
        }

        // 2) Fallback: Buscar por etiqueta "From/Desde"
        if (nombreEmpresa == null || nombreEmpresa.isEmpty()) {
            Element desdeLabel = doc.selectFirst("td:contains(From:), td:contains(Desde:), td:contains(Desde:)");
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
                } else if (text.contains("ha sido enviado por ")) {
                    // Variante en español
                    try {
                        nombreEmpresa = text.split("ha sido enviado por ")[1].split(" y ")[0].trim();
                    } catch (Exception ignore) {}
                }
            }
        }

        if (nombreEmpresa != null && !nombreEmpresa.isEmpty()) {
            data.setDesde(nombreEmpresa.replaceAll("\\s+", " ").trim());
            data.setEmpresa(data.getDesde());
        }

        // ================= PARA (Receptor / Contacto) =================
        String para = null;

        // 1) Ubica el bloque "Para:/To:" y toma el nombre dentro de esa misma tabla
        Element toTd = doc.selectFirst("td.po-INSSAddr-To-label"); // contiene <b>Para:&nbsp;</b> o <b>To:&nbsp;</b>
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

        // ================= TAX ID =================
        data.setTaxId(extraerPorLabelMulti(doc,
                "Customer VAT/Tax ID:",
                "RFC del cliente:",
                "RFC:"
        ));

        // ================= MONEDA Y MONTO =================
        Element monto = doc.selectFirst(".po-INSPON-std-money");
        if (monto != null) {
            String txt = monto.text();

            if (txt.contains("MXN")) data.setMoneda("MXN");
            else if (txt.contains("USD")) data.setMoneda("USD");

            // "66,500.00" trae coma -> se elimina
            String limpio = txt.replaceAll("[^0-9.,]", "");
            limpio = limpio.replace(",", "");

            if (!limpio.isEmpty()) {
                data.setMonto(new BigDecimal(limpio));
            }
        }

        // ================= CLASIFICACIÓN (EN/ES) =================
        // 1) Inglés (como HP)
        String dom = extraerPorLabelMulti(doc,
                "Classification Domain:",
                "Dominio de la clasificación:",
                "Dominio de la clasificacion:"
        );
        String cod = extraerPorLabelMulti(doc,
                "Classification Code:",
                "Código de clasificación:",
                "Codigo de clasificacion:"
        );

        data.setClasificacionDominio(dom);
        data.setClasificacionCodigo(cod);

        // ================= EMAIL (EN/ES) =================
        String email = extraerPorLabelMulti(doc,
                "Email:",
                "Correo electrónico:",
                "Correo electronico:"
        );

        // Fallback para el bloque "Para:" (como en el HTM de Toyota)
        if (email == null || email.trim().isEmpty()) {
            Element emailTd = doc.selectFirst("td.po-INSSAddr-addr-details:matchesOwn((?i)Correo\\s+electr[oó]nico:.*)");
            if (emailTd != null) {
                String t = emailTd.text().trim();
                int idx = t.indexOf(":");
                if (idx >= 0 && idx + 1 < t.length()) {
                    email = t.substring(idx + 1).trim();
                }
            }
        }

        data.setEmailDestino((email != null && !email.trim().isEmpty()) ? email.trim() : null);

        // ✅ LOGS ya con valores seteados
        logger.info("HTM PARSED → DESDE: " + data.getDesde());
        logger.info("HTM PARSED → PARA : " + data.getPara());
        logger.info("HTM PARSED → CLASIF_DOM: " + data.getClasificacionDominio());
        logger.info("HTM PARSED → CLASIF_COD: " + data.getClasificacionCodigo());

        return data;
    }

    // ================= MÉTODOS AUXILIARES =================

    /**
     * Busca un valor en tabla tipo:
     * td.base-ncd-label-top:contains(LABEL) + td
     * y soporta múltiples labels (EN/ES).
     */
    private String extraerPorLabelMulti(Document doc, String... labels) {
        if (labels == null) return null;

        for (String label : labels) {
            String val = extraerPorLabel(doc, label);
            if (val != null && !val.trim().isEmpty()) {
                return val.trim();
            }
        }
        return null;
    }

    /**
     * Extrae texto de "LABEL" en:
     * td.base-ncd-label-top:contains(LABEL) + td
     * fallback: td:contains(LABEL) + td
     */
    private String extraerPorLabel(Document doc, String label) {
        if (doc == null || label == null || label.trim().isEmpty()) return null;

        // 1) Caso típico en "Otra información"
        Element td = doc.selectFirst("td.base-ncd-label-top:contains(" + label + ") + td");
        if (td == null) {
            // 2) Fallback general (por si la etiqueta cambia de clase)
            td = doc.selectFirst("td:contains(" + label + ") + td");
        }

        if (td == null) return null;

        String txt = td.text();
        return (txt != null) ? txt.trim() : null;
    }
}
