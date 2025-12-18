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
public class ImpuestosDR {

    private RetencionesDR _retencionesDR = new RetencionesDR();
    private TrasladosDR _trasladosDR = new TrasladosDR();

    /**
     * <value>Opcional</value>
     * <p>
     * Nodo opcional para capturar los impuestos retenidos aplicables conforme
     * al monto del pago recibido.
     * </p>
     */
    public final RetencionesDR getRetencionesDR() {
        return _retencionesDR;
    }

    public final void setRetencionesDR(RetencionesDR value) {
        _retencionesDR = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * Nodo opcional para capturar los impuestos trasladados aplicables conforme
     * al monto del pago recibido.
     * </p>
     */
    public final TrasladosDR getTrasladosDR() {
        return _trasladosDR;
    }

    public final void setTrasladosDR(TrasladosDR value) {
        _trasladosDR = value;
    }
}
