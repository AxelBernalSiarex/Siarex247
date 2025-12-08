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
public class Notificado {

    private Domicilio domicilio;
    private String rFCNotificado = "";
    private String nombreNotificado = "";
    private String numRegIdTribNotificado = "";
    private String residenciaFiscalNotificado = "";

    public final Domicilio getDomicilio() {
        return this.domicilio;
    }

    public final void setDomicilio(Domicilio value) {
        this.domicilio = value;
    }

    public final String getRFCNotificado() {
        return this.rFCNotificado;
    }

    public final void setRFCNotificado(String value) {
        this.rFCNotificado = value;
    }

    public final String getNombreNotificado() {
        return this.nombreNotificado;
    }

    public final void setNombreNotificado(String value) {
        this.nombreNotificado = value;
    }

    public final String getNumRegIdTribNotificado() {
        return this.numRegIdTribNotificado;
    }

    public final void setNumRegIdTribNotificado(String value) {
        this.numRegIdTribNotificado = value;
    }

    public final String getResidenciaFiscalNotificado() {
        return this.residenciaFiscalNotificado;
    }

    public final void setResidenciaFiscalNotificado(String value) {
        this.residenciaFiscalNotificado = value;
    }

}
