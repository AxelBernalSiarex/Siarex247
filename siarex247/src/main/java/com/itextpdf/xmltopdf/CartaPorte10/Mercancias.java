/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.CartaPorte10;

import java.util.ArrayList;

/**
 *
 * @author frack
 */
public class Mercancias {

    private ArrayList<Mercancia> mercancia;
    private AutotransporteFederal autotransporteFederal;
    private TransporteMaritimo transporteMaritimo;
    private TransporteAereo transporteAereo;
    private TransporteFerroviario transporteFerroviario;
    private double pesoBrutoTotal = 0;
    private String unidadPeso = "";
    private double pesoNetoTotal = 0;
    private int numTotalMercancias;
    private double cargoPorTasacion = 0;

    public final ArrayList<Mercancia> getMercancia() {
        return this.mercancia;
    }

    public final void setMercancia(ArrayList<Mercancia> value) {
        this.mercancia = value;
    }

    public final AutotransporteFederal getAutotransporteFederal() {
        return this.autotransporteFederal;
    }

    public final void setAutotransporteFederal(AutotransporteFederal value) {
        this.autotransporteFederal = value;
    }

    public final TransporteMaritimo getTransporteMaritimo() {
        return this.transporteMaritimo;
    }

    public final void setTransporteMaritimo(TransporteMaritimo value) {
        this.transporteMaritimo = value;
    }

    public final TransporteAereo getTransporteAereo() {
        return this.transporteAereo;
    }

    public final void setTransporteAereo(TransporteAereo value) {
        this.transporteAereo = value;
    }

    public final TransporteFerroviario getTransporteFerroviario() {
        return this.transporteFerroviario;
    }

    public final void setTransporteFerroviario(TransporteFerroviario value) {
        this.transporteFerroviario = value;
    }

    public final double getPesoBrutoTotal() {
        return this.pesoBrutoTotal;
    }

    public final void setPesoBrutoTotal(double value) {
        this.pesoBrutoTotal = value;
    }

    public final String getUnidadPeso() {
        return this.unidadPeso;
    }

    public final void setUnidadPeso(String value) {
        this.unidadPeso = value;
    }

    public final double getPesoNetoTotal() {
        return this.pesoNetoTotal;
    }

    public final void setPesoNetoTotal(double value) {
        this.pesoNetoTotal = value;
    }

    public final int getNumTotalMercancias() {
        return this.numTotalMercancias;
    }

    public final void setNumTotalMercancias(int value) {
        this.numTotalMercancias = value;
    }

    public final double getCargoPorTasacion() {
        return this.cargoPorTasacion;
    }

    public final void setCargoPorTasacion(double value) {
        this.cargoPorTasacion = value;
    }
}
