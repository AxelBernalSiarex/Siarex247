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
import java.time.*;

public class Origen {

    private String iDOrigen = "";
    private String rFCRemitente = "";
    private String nombreRemitente = "";
    private String numRegIdTrib = "";
    private String residenciaFiscal = "";
    private String numEstacion = "";
    private String nombreEstacion = "";
    private String navegacionTrafico = "";
    private LocalDateTime fechaHoraSalida = LocalDateTime.MIN;

    public final String getIDOrigen() {
        return this.iDOrigen;
    }

    public final void setIDOrigen(String value) {
        this.iDOrigen = value;
    }

    public final String getRFCRemitente() {
        return this.rFCRemitente;
    }

    public final void setRFCRemitente(String value) {
        this.rFCRemitente = value;
    }

    public final String getNombreRemitente() {
        return this.nombreRemitente;
    }

    public final void setNombreRemitente(String value) {
        this.nombreRemitente = value;
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

    public final LocalDateTime getFechaHoraSalida() {
        return this.fechaHoraSalida;
    }

    public final void setFechaHoraSalida(LocalDateTime value) {
        this.fechaHoraSalida = value;
    }
}
