/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos20;

/**
 *
 * @author frack
 */
public class ImpuestosP {

    private RetencionesP _retencionesP;
    private TrasladosP _trasladosP;

    /**
     * <value>Condicional</value>
     * <p>
     * Nodo condicional para señalar los impuestos retenidos aplicables conforme
     * al monto del pago recibido. Es requerido cuando en los documentos
     * relacionados se registre algún impuesto retenido.
     * </p>
     */
    public final RetencionesP getRetencionesP() {
        return _retencionesP;
    }

    public final void setRetencionesP(RetencionesP value) {
        _retencionesP = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Nodo condicional para capturar los impuestos trasladados aplicables
     * conforme al monto del pago recibido. Es requerido cuando en los
     * documentos relacionados se registre un impuesto trasladado.
     * </p>
     */
    public final TrasladosP getTrasladosP() {
        return _trasladosP;
    }

    public final void setTrasladosP(TrasladosP value) {
        _trasladosP = value;
    }
}
