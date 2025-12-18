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
public class TrasladosDR {

    private ArrayList<TrasladoDR> _trasladoDR = new ArrayList<>();

    /**
     * <value>Requerido</value>
     * <p>
     * Nodo requerido para asentar la información detallada de un traslado de
     * impuesto específico conforme al monto del pago recibido.
     * </p>
     */
    public final ArrayList<TrasladoDR> getTrasladoDR() {
        return _trasladoDR;
    }

    public final void setTrasladoDR(ArrayList<TrasladoDR> value) {
        _trasladoDR = value;
    }
}
