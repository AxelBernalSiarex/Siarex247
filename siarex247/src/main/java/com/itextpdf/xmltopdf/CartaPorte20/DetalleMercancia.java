/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.CartaPorte20;

/**
 *
 * @author frack
 */
public class DetalleMercancia {

    private String unidadPesoMerc = "";
    private double pesoBruto;
    private double pesoNeto;
    private double pesoTara;
    private int numPiezas;

    public final String getUnidadPesoMerc() {
        return this.unidadPesoMerc;
    }

    public final void setUnidadPesoMerc(String value) {
        this.unidadPesoMerc = value;
    }

    public final double getPesoBruto() {
        return this.pesoBruto;
    }

    public final void setPesoBruto(double value) {
        this.pesoBruto = value;
    }

    public final double getPesoNeto() {
        return this.pesoNeto;
    }

    public final void setPesoNeto(double value) {
        this.pesoNeto = value;
    }

    public final double getPesoTara() {
        return this.pesoTara;
    }

    public final void setPesoTara(double value) {
        this.pesoTara = value;
    }

    public final int getNumPiezas() {
        return this.numPiezas;
    }

    public final void setNumPiezas(int value) {
        this.numPiezas = value;
    }

}
