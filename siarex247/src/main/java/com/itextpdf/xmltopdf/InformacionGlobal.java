/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

/**
 *
 * @author frack
 */
public class InformacionGlobal {

    private String _periodicidad = "";
    private String _meses = "";
    private int _ano = 0;

    public final String getPeriodicidad() {
        return _periodicidad;
    }

    /**
     * @param value Requerido.
     * @see Atributo requerido para expresar el período al que corresponde la
     *      información del comprobante global.
     */
    public final void setPeriodicidad(String value) {
        _periodicidad = value;
    }

    public final String getMeses() {
        return _meses;
    }

    /**
     * @param value Requerido.
     * @see Atributo requerido para expresar el mes o los meses al que corresponde
     *      la
     *      información del comprobante global.
     */
    public final void setMeses(String value) {
        _meses = value;
    }

    public final int getAno() {
        return _ano;
    }

    /**
     * @param value Requerido.
     * @see Atributo requerido para expresar el año al que corresponde la
     *      información
     *      del comprobante global.
     */
    public final void setAno(int value) {
        _ano = value;
    }
}
