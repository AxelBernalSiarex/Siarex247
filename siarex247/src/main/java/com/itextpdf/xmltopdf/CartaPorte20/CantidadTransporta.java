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
public class CantidadTransporta {

    private double cantidad;
    private String iDOrigen = "";
    private String iDDestino = "";
    private String cvesTransporte = "";

    public final double getCantidad() {
        return this.cantidad;
    }

    public final void setCantidad(double value) {
        this.cantidad = value;
    }

    public final String getIDOrigen() {
        return this.iDOrigen;
    }

    public final void setIDOrigen(String value) {
        this.iDOrigen = value;
    }

    public final String getIDDestino() {
        return this.iDDestino;
    }

    public final void setIDDestino(String value) {
        this.iDDestino = value;
    }

    public final String getCvesTransporte() {
        return this.cvesTransporte;
    }

    public final void setCvesTransporte(String value) {
        this.cvesTransporte = value;
    }
}
