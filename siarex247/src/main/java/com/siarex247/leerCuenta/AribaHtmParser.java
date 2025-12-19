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

        // ORDEN DE COMPRA
        Element orden = doc.selectFirst("span.po-INSPON-doc-num");
        if (orden != null)
            data.setOrdenCompra(orden.text().trim());

        // EMPRESA
        Element empresa = doc.selectFirst(".fdml-ov-bill-to-gf-addr-name-val");
        if (empresa != null)
            data.setEmpresa(empresa.text().trim());

        // TAX ID
        data.setTaxId(extraerPorLabel(doc, "Customer VAT/Tax ID:"));

        // MONEDA Y MONTO
        Element monto = doc.selectFirst(".po-INSPON-std-money");
        if (monto != null) {
            String txt = monto.text(); // $800,000.00 MXN
            data.setMoneda(txt.contains("MXN") ? "MXN" : null);
            String limpio = txt.replaceAll("[^0-9.,]", "").replace(",", "");
            data.setMonto(new BigDecimal(limpio));
        }

        // CLASIFICACIÓN
        data.setClasificacionDominio(extraerPorLabel(doc, "Classification Domain:"));
        data.setClasificacionCodigo(extraerPorLabel(doc, "Classification Code:"));

        // EMAIL
        data.setEmailDestino(extraerPorLabel(doc, "Email:"));

        return data;
    }

    private String extraerPorLabel(Document doc, String label) {
        Element td = doc.selectFirst("td:contains(" + label + ") + td");
        return td != null ? td.text().trim() : null;
    }
}
