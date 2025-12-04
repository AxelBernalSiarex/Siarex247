/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

import java.util.ArrayList;

/**
 *
 * @author Faustino
 */
public class ImpuestosC {

    private ArrayList<TrasladoC> _traslados = new ArrayList<>();
    private ArrayList<RetencionC> _retenciones = new ArrayList<>();

    public final ArrayList<TrasladoC> getTraslados() {
        return _traslados;
    }

    /**
     * @param value Opcional.
     * @seeNodo opcional para asentar los impuestos trasladados aplicables al
     *          presente concepto.
     */
    public final void setTraslados(ArrayList<TrasladoC> value) {
        _traslados = value;
    }

    public final ArrayList<RetencionC> getRetenciones() {
        return _retenciones;
    }

    /**
     * @param value Opcional.
     * @seeNodo opcional para asentar los impuestos retenidos aplicables al
     *          presente concepto.
     */
    public final void setRetenciones(ArrayList<RetencionC> value) {
        _retenciones = value;
    }
}
