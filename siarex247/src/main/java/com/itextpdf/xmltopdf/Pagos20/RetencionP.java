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
public class RetencionP {

    private String _impuestoP = "";
    private double _importeP = 0;

    /**
     * <value>Requerido</value>
     * <p>
     * señalar la clave del tipo de impuesto retenido.
     * </p>
     */
    public final String getImpuestoP() {
        return _impuestoP;
    }

    public final void setImpuestoP(String value) {
        _impuestoP = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * señalar el importe o monto del impuesto retenido. No se permiten valores
     * negativos.
     * </p>
     */
    public final double getImporteP() {
        return _importeP;
    }

    public final void setImporteP(double value) {
        _importeP = value;
    }
}
