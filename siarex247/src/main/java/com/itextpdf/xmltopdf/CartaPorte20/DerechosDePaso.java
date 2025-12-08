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
public class DerechosDePaso {

    private String tipoDerechoDePaso = "";
    private double kilometrajePagado;

    public final String getTipoDerechoDePaso() {
        return this.tipoDerechoDePaso;
    }

    public final void setTipoDerechoDePaso(String value) {
        this.tipoDerechoDePaso = value;
    }

    public final double getKilometrajePagado() {
        return this.kilometrajePagado;
    }

    public final void setKilometrajePagado(double value) {
        this.kilometrajePagado = value;
    }
}
