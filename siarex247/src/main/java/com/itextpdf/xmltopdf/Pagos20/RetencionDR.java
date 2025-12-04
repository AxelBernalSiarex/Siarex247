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
public class RetencionDR {

    private double _baseDR = 0;
    private String _impuestoDR = "";
    private String _tipoFactorDR = "";
    private double _tasaOCuotaDR = 0;
    private double _importeDR = 0;

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para señalar la base para el cálculo de la retención
     * conforme al monto del pago, aplicable al documento relacionado, la
     * determinación de la base se realiza de acuerdo con las disposiciones
     * fiscales vigentes. No se permiten valores negativos.
     * </p>
     */
    public final double getBaseDR() {
        return _baseDR;
    }

    public final void setBaseDR(double value) {
        _baseDR = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para señalar la clave del tipo de impuesto retenido
     * conforme al monto del pago, aplicable al documento relacionado.
     * </p>
     */
    public final String getImpuestoDR() {
        return _impuestoDR;
    }

    public final void setImpuestoDR(String value) {
        _impuestoDR = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para señalar la clave del tipo de factor que se aplica
     * a la base del impuesto.
     * </p>
     */
    public final String getTipoFactorDR() {
        return _tipoFactorDR;
    }

    public final void setTipoFactorDR(String value) {
        _tipoFactorDR = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para señalar el valor de la tasa o cuota del impuesto
     * que se retiene.
     * </p>
     */
    public final double getTasaOCuotaDR() {
        return _tasaOCuotaDR;
    }

    public final void setTasaOCuotaDR(double value) {
        _tasaOCuotaDR = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para señalar el importe del impuesto retenido conforme
     * al monto del pago, aplicable al documento relacionado.
     * </p>
     */
    public final double getImporteDR() {
        return _importeDR;
    }

    public final void setImporteDR(double value) {
        _importeDR = value;
    }
}
