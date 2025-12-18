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
public class Arrendatario {

    private Domicilio domicilio;
    private String rFCArrendatario = "";
    private String nombreArrendatario = "";
    private String numRegIdTribArrendatario = "";
    private String residenciaFiscalArrendatario = "";

    public final Domicilio getDomicilio() {
        return this.domicilio;
    }

    public final void setDomicilio(Domicilio value) {
        this.domicilio = value;
    }

    public final String getRFCArrendatario() {
        return this.rFCArrendatario;
    }

    public final void setRFCArrendatario(String value) {
        this.rFCArrendatario = value;
    }

    public final String getNombreArrendatario() {
        return this.nombreArrendatario;
    }

    public final void setNombreArrendatario(String value) {
        this.nombreArrendatario = value;
    }

    public final String getNumRegIdTribArrendatario() {
        return this.numRegIdTribArrendatario;
    }

    public final void setNumRegIdTribArrendatario(String value) {
        this.numRegIdTribArrendatario = value;
    }

    public final String getResidenciaFiscalArrendatario() {
        return this.residenciaFiscalArrendatario;
    }

    public final void setResidenciaFiscalArrendatario(String value) {
        this.residenciaFiscalArrendatario = value;
    }

}
