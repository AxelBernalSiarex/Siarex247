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
public class GuiasIdentificacion {

    private String numeroGuiaIdentificacion = "";
    private String descripGuiaIdentificacion = "";
    private double pesoGuiaIdentificacion;

    public final String getNumeroGuiaIdentificacion() {
        return this.numeroGuiaIdentificacion;
    }

    public final void setNumeroGuiaIdentificacion(String value) {
        this.numeroGuiaIdentificacion = value;
    }

    public final String getDescripGuiaIdentificacion() {
        return this.descripGuiaIdentificacion;
    }

    public final void setDescripGuiaIdentificacion(String value) {
        this.descripGuiaIdentificacion = value;
    }

    public final double getPesoGuiaIdentificacion() {
        return this.pesoGuiaIdentificacion;
    }

    public final void setPesoGuiaIdentificacion(double value) {
        this.pesoGuiaIdentificacion = value;
    }
}
