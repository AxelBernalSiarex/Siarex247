/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.CartaPorte10;

import java.time.LocalDateTime;

/**
 *
 * @author frack
 */
public class Destino {

    private String iDDestino = "";
    private String rFCDestinatario = "";
    private String nombreDestinatario = "";
    private String numRegIdTrib = "";
    private String residenciaFiscal = "";
    private String numEstacion = "";
    private String nombreEstacion = "";
    private String navegacionTrafico = "";
    private LocalDateTime fechaHoraProgLlegada = LocalDateTime.MIN;

    public final String getIDDestino() {
        return this.iDDestino;
    }

    public final void setIDDestino(String value) {
        this.iDDestino = value;
    }

    public final String getRFCDestinatario() {
        return this.rFCDestinatario;
    }

    public final void setRFCDestinatario(String value) {
        this.rFCDestinatario = value;
    }

    public final String getNombreDestinatario() {
        return this.nombreDestinatario;
    }

    public final void setNombreDestinatario(String value) {
        this.nombreDestinatario = value;
    }

    public final String getNumRegIdTrib() {
        return this.numRegIdTrib;
    }

    public final void setNumRegIdTrib(String value) {
        this.numRegIdTrib = value;
    }

    public final String getResidenciaFiscal() {
        return this.residenciaFiscal;
    }

    public final void setResidenciaFiscal(String value) {
        this.residenciaFiscal = value;
    }

    public final String getNumEstacion() {
        return this.numEstacion;
    }

    public final void setNumEstacion(String value) {
        this.numEstacion = value;
    }

    public final String getNombreEstacion() {
        return this.nombreEstacion;
    }

    public final void setNombreEstacion(String value) {
        this.nombreEstacion = value;
    }

    public final String getNavegacionTrafico() {
        return this.navegacionTrafico;
    }

    public final void setNavegacionTrafico(String value) {
        this.navegacionTrafico = value;
    }

    public final LocalDateTime getFechaHoraProgLlegada() {
        return this.fechaHoraProgLlegada;
    }

    public final void setFechaHoraProgLlegada(LocalDateTime value) {
        this.fechaHoraProgLlegada = value;
    }
}
