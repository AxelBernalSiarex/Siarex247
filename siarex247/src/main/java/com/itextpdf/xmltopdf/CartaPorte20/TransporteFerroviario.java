/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.CartaPorte20;

import java.util.*;

/**
 *
 * @author frack
 */
public class TransporteFerroviario {

    private ArrayList<DerechosDePaso> derechosDePaso;
    private ArrayList<Carro> carro;
    private String tipoDeServicio = "";
    private String tipoDeTrafico = "";
    private String nombreAseg = "";
    private String numPolizaSeguro = "";

    public final ArrayList<DerechosDePaso> getDerechosDePaso() {
        return this.derechosDePaso;
    }

    public final void setDerechosDePaso(ArrayList<DerechosDePaso> value) {
        this.derechosDePaso = value;
    }

    public final ArrayList<Carro> getCarro() {
        return this.carro;
    }

    public final void setCarro(ArrayList<Carro> value) {
        this.carro = value;
    }

    public final String getTipoDeServicio() {
        return this.tipoDeServicio;
    }

    public final void setTipoDeServicio(String value) {
        this.tipoDeServicio = value;
    }

    public final String getTipoDeTrafico() {
        return this.tipoDeTrafico;
    }

    public final void setTipoDeTrafico(String value) {
        this.tipoDeTrafico = value;
    }

    public final String getNombreAseg() {
        return this.nombreAseg;
    }

    public final void setNombreAseg(String value) {
        this.nombreAseg = value;
    }

    public final String getNumPolizaSeguro() {
        return this.numPolizaSeguro;
    }

    public final void setNumPolizaSeguro(String value) {
        this.numPolizaSeguro = value;
    }
}
