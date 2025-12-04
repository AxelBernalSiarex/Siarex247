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

public class CartaPorte10 {

    private Ubicaciones ubicaciones;
    private Mercancias mercancias;
    private FiguraTransporte figuraTransporte;
    private String version;
    private String transpInternac;
    private String entradaSalidaMerc;
    private String viaEntradaSalida;
    private double totalDistRec = 0;

    public CartaPorte10() {
        this.version = "1.0";
    }

    public final Ubicaciones getUbicaciones() {
        return this.ubicaciones;
    }

    public final void setUbicaciones(Ubicaciones value) {
        this.ubicaciones = value;
    }

    public final Mercancias getMercancias() {
        return this.mercancias;
    }

    public final void setMercancias(Mercancias value) {
        this.mercancias = value;
    }

    public final FiguraTransporte getFiguraTransporte() {
        return this.figuraTransporte;
    }

    public final void setFiguraTransporte(FiguraTransporte value) {
        this.figuraTransporte = value;
    }

    public final String getVersion() {
        return this.version;
    }

    public final void setVersion(String value) {
        this.version = value;
    }

    public final String getTranspInternac() {
        return this.transpInternac;
    }

    public final void setTranspInternac(String value) {
        this.transpInternac = value;
    }

    public final String getEntradaSalidaMerc() {
        return this.entradaSalidaMerc;
    }

    public final void setEntradaSalidaMerc(String value) {
        this.entradaSalidaMerc = value;
    }

    public final String getViaEntradaSalida() {
        return this.viaEntradaSalida;
    }

    public final void setViaEntradaSalida(String value) {
        this.viaEntradaSalida = value;
    }

    public final double getTotalDistRec() {
        return this.totalDistRec;
    }

    public final void setTotalDistRec(double value) {
        this.totalDistRec = value;
    }
}
