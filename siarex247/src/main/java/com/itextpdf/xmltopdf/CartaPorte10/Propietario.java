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
public class Propietario {

    private Domicilio domicilio;
    private String rFCPropietario = "";
    private String nombrePropietario = "";
    private String numRegIdTribPropietario = "";
    private String residenciaFiscalPropietario = "";

    public final Domicilio getDomicilio() {
        return this.domicilio;
    }

    public final void setDomicilio(Domicilio value) {
        this.domicilio = value;
    }

    public final String getRFCPropietario() {
        return this.rFCPropietario;
    }

    public final void setRFCPropietario(String value) {
        this.rFCPropietario = value;
    }

    public final String getNombrePropietario() {
        return this.nombrePropietario;
    }

    public final void setNombrePropietario(String value) {
        this.nombrePropietario = value;
    }

    public final String getNumRegIdTribPropietario() {
        return this.numRegIdTribPropietario;
    }

    public final void setNumRegIdTribPropietario(String value) {
        this.numRegIdTribPropietario = value;
    }

    public final String getResidenciaFiscalPropietario() {
        return this.residenciaFiscalPropietario;
    }

    public final void setResidenciaFiscalPropietario(String value) {
        this.residenciaFiscalPropietario = value;
    }

}
