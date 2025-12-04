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

public class Ubicacion {

    private Origen origen;
    private Destino destino;
    private Domicilio domicilio;
    private String tipoEstacion = "";
    private double distanciaRecorrida = 0;

    public final Origen getOrigen() {
        return this.origen;
    }

    public final void setOrigen(Origen value) {
        this.origen = value;
    }

    public final Destino getDestino() {
        return this.destino;
    }

    public final void setDestino(Destino value) {
        this.destino = value;
    }

    public final Domicilio getDomicilio() {
        return this.domicilio;
    }

    public final void setDomicilio(Domicilio value) {
        this.domicilio = value;
    }

    public final String getTipoEstacion() {
        return this.tipoEstacion;
    }

    public final void setTipoEstacion(String value) {
        this.tipoEstacion = value;
    }

    public final double getDistanciaRecorrida() {
        return this.distanciaRecorrida;
    }

    public final void setDistanciaRecorrida(double value) {
        this.distanciaRecorrida = value;
    }

}
