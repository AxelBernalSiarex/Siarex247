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
public class Mercancia {

    private ArrayList<CantidadTransporta> cantidadTransporta;
    private DetalleMercancia detalleMercancia;
    private String bienesTransp = "";
    private String claveSTCC = "";
    private String descripcion = "";
    private double cantidad = 0;
    private String claveUnidad = "";
    private String unidad = "";
    private String dimensiones = "";
    private String materialPeligroso = "";
    private String cveMaterialPeligroso = "";
    private String embalaje = "";
    private String descripEmbalaje = "";
    private double pesoEnKg = 0;
    private double valorMercancia = 0;
    private String moneda = "";
    private String fraccionArancelaria = "";
    private String uUIDComercioExt = "";

    public final ArrayList<CantidadTransporta> getCantidadTransporta() {
        return this.cantidadTransporta;
    }

    public final void setCantidadTransporta(ArrayList<CantidadTransporta> value) {
        this.cantidadTransporta = value;
    }

    public final DetalleMercancia getDetalleMercancia() {
        return this.detalleMercancia;
    }

    public final void setDetalleMercancia(DetalleMercancia value) {
        this.detalleMercancia = value;
    }

    public final String getBienesTransp() {
        return this.bienesTransp;
    }

    public final void setBienesTransp(String value) {
        this.bienesTransp = value;
    }

    public final String getClaveSTCC() {
        return this.claveSTCC;
    }

    public final void setClaveSTCC(String value) {
        this.claveSTCC = value;
    }

    public final String getDescripcion() {
        return this.descripcion;
    }

    public final void setDescripcion(String value) {
        this.descripcion = value;
    }

    public final double getCantidad() {
        return this.cantidad;
    }

    public final void setCantidad(double value) {
        this.cantidad = value;
    }

    public final String getClaveUnidad() {
        return this.claveUnidad;
    }

    public final void setClaveUnidad(String value) {
        this.claveUnidad = value;
    }

    public final String getUnidad() {
        return this.unidad;
    }

    public final void setUnidad(String value) {
        this.unidad = value;
    }

    public final String getDimensiones() {
        return this.dimensiones;
    }

    public final void setDimensiones(String value) {
        this.dimensiones = value;
    }

    public final String getMaterialPeligroso() {
        return this.materialPeligroso;
    }

    public final void setMaterialPeligroso(String value) {
        this.materialPeligroso = value;
    }

    public final String getCveMaterialPeligroso() {
        return this.cveMaterialPeligroso;
    }

    public final void setCveMaterialPeligroso(String value) {
        this.cveMaterialPeligroso = value;
    }

    public final String getEmbalaje() {
        return this.embalaje;
    }

    public final void setEmbalaje(String value) {
        this.embalaje = value;
    }

    public final String getDescripEmbalaje() {
        return this.descripEmbalaje;
    }

    public final void setDescripEmbalaje(String value) {
        this.descripEmbalaje = value;
    }

    public final double getPesoEnKg() {
        return this.pesoEnKg;
    }

    public final void setPesoEnKg(double value) {
        this.pesoEnKg = value;
    }

    public final double getValorMercancia() {
        return this.valorMercancia;
    }

    public final void setValorMercancia(double value) {
        this.valorMercancia = value;
    }

    public final String getMoneda() {
        return this.moneda;
    }

    public final void setMoneda(String value) {
        this.moneda = value;
    }

    public final String getFraccionArancelaria() {
        return this.fraccionArancelaria;
    }

    public final void setFraccionArancelaria(String value) {
        this.fraccionArancelaria = value;
    }

    public final String getUUIDComercioExt() {
        return this.uUIDComercioExt;
    }

    public final void setUUIDComercioExt(String value) {
        this.uUIDComercioExt = value;
    }
}
