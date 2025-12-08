/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.CartaPorte10;

/**
 *
 * @author frack
 */
public class Operador {

    private Domicilio domicilio;
    private String rFCOperador = "";
    private String numLicencia = "";
    private String nombreOperador = "";
    private String numRegIdTribOperador = "";
    private String residenciaFiscalOperador = "";

    public final Domicilio getDomicilio() {
        return this.domicilio;
    }

    public final void setDomicilio(Domicilio value) {
        this.domicilio = value;
    }

    public final String getRFCOperador() {
        return this.rFCOperador;
    }

    public final void setRFCOperador(String value) {
        this.rFCOperador = value;
    }

    public final String getNumLicencia() {
        return this.numLicencia;
    }

    public final void setNumLicencia(String value) {
        this.numLicencia = value;
    }

    public final String getNombreOperador() {
        return this.nombreOperador;
    }

    public final void setNombreOperador(String value) {
        this.nombreOperador = value;
    }

    public final String getNumRegIdTribOperador() {
        return this.numRegIdTribOperador;
    }

    public final void setNumRegIdTribOperador(String value) {
        this.numRegIdTribOperador = value;
    }

    public final String getResidenciaFiscalOperador() {
        return this.residenciaFiscalOperador;
    }

    public final void setResidenciaFiscalOperador(String value) {
        this.residenciaFiscalOperador = value;
    }

}
