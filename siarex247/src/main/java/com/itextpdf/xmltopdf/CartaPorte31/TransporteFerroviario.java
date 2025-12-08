package com.itextpdf.xmltopdf.CartaPorte31;

import java.util.*;

/**
 *
 * @author frack
 */
public class TransporteFerroviario {
    private String tipoDeServicio = "";
    private String tipoDeTrafico = "";
    private String nombreAseg = "";
    private String numPolizaSeguro = "";

    private ArrayList<DerechosDePaso> derechosDePaso;
    private ArrayList<Carro> carro;

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
}
