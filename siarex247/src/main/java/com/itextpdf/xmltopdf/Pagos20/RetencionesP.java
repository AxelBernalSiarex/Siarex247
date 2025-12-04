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
public class RetencionesP {

    private ArrayList<RetencionP> _retencionP = new ArrayList<>();

    /**
     * <value>Requerido</value>
     * <p>
     * Nodo requerido para registrar la información detallada de una retención
     * de impuesto específico.
     * </p>
     */
    public final ArrayList<RetencionP> getRetencionP() {
        return _retencionP;
    }

    public final void setRetencionP(ArrayList<RetencionP> value) {
        _retencionP = value;
    }
}
