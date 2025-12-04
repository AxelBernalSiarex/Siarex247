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
public class Seguros {

    private String aseguraRespCivil = "";
    private String polizaRespCivil = "";
    private String aseguraMedAmbiente = "";
    private String polizaMedAmbiente = "";
    private String aseguraCarga = "";
    private String polizaCarga = "";
    private double primaSeguro;

    public final String getAseguraRespCivil() {
        return this.aseguraRespCivil;
    }

    public final void setAseguraRespCivil(String value) {
        this.aseguraRespCivil = value;
    }

    public final String getPolizaRespCivil() {
        return this.polizaRespCivil;
    }

    public final void setPolizaRespCivil(String value) {
        this.polizaRespCivil = value;
    }

    public final String getAseguraMedAmbiente() {
        return this.aseguraMedAmbiente;
    }

    public final void setAseguraMedAmbiente(String value) {
        this.aseguraMedAmbiente = value;
    }

    public final String getPolizaMedAmbiente() {
        return this.polizaMedAmbiente;
    }

    public final void setPolizaMedAmbiente(String value) {
        this.polizaMedAmbiente = value;
    }

    public final String getAseguraCarga() {
        return this.aseguraCarga;
    }

    public final void setAseguraCarga(String value) {
        this.aseguraCarga = value;
    }

    public final String getPolizaCarga() {
        return this.polizaCarga;
    }

    public final void setPolizaCarga(String value) {
        this.polizaCarga = value;
    }

    public final double getPrimaSeguro() {
        return this.primaSeguro;
    }

    public final void setPrimaSeguro(double value) {
        this.primaSeguro = value;
    }
}
