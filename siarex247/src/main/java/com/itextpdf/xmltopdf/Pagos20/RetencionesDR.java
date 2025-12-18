/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos20;

import java.util.ArrayList;

/**
 *
 * @author frack
 */
public class RetencionesDR {

    private ArrayList<RetencionDR> _retencionDR = new ArrayList<>();

    /**
     * <value>Opcional</value>
     * <p>
     * Nodo opcional para capturar los impuestos retenidos aplicables conforme
     * al monto del pago recibido.
     * </p>
     */
    public final ArrayList<RetencionDR> getRetencionDR() {
        return _retencionDR;
    }

    public final void setRetencionDR(ArrayList<RetencionDR> value) {
        _retencionDR = value;
    }
}
