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
public class TrasladoP {

    private double _baseP = 0;
    private String _impuestoP = "";
    private String _tipoFactorP = "";
    private double _tasaOCuotaP = 0;
    private double _importeP = 0;

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para señalar la suma de los atributos BaseDR de los
     * documentos relacionados del impuesto trasladado. No se permiten valores
     * negativos.
     * </p>
     */
    public final double getBaseP() {
        return _baseP;
    }

    public final void setBaseP(double value) {
        _baseP = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Señalar la clave del tipo de impuesto trasladado.
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
     * señalar la clave del tipo de factor que se aplica a la base del
     * impuesto.
     * </p>
     */
    public final String getTipoFactorP() {
        return _tipoFactorP;
    }

    public final void setTipoFactorP(String value) {
        _tipoFactorP = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Señalar el valor de la tasa o cuota del impuesto que se traslada.
     * </p>
     */
    public final double getTasaOCuotaP() {
        return _tasaOCuotaP;
    }

    public final void setTasaOCuotaP(double value) {
        _tasaOCuotaP = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Señalar el importe del impuesto trasladado. No se permiten valores
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
