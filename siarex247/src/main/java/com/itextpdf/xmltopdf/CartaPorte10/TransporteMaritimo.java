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
public class TransporteMaritimo {

    private ArrayList<Contenedor> contenedor;
    private String permSCT = "";
    private String numPermisoSCT = "";
    private String nombreAseg = "";
    private String numPolizaSeguro = "";
    private String tipoEmbarcacion = "";
    private String matricula = "";
    private String numeroOMI = "";
    private int anioEmbarcacion;
    private String nombreEmbarc = "";
    private String nacionalidadEmbarc = "";
    private double unidadesDeArqBruto = 0;
    private String tipoCarga = "";
    private String numCertITC = "";
    private double eslora = 0;
    private double manga = 0;
    private double calado = 0;
    private String lineaNaviera = "";
    private String nombreAgenteNaviero = "";
    private String numAutorizacionNaviero = "";
    private String numViaje = "";
    private String numConocEmbarc = "";

    public final ArrayList<Contenedor> getContenedor() {
        return this.contenedor;
    }

    public final void setContenedor(ArrayList<Contenedor> value) {
        this.contenedor = value;
    }

    public final String getPermSCT() {
        return this.permSCT;
    }

    public final void setPermSCT(String value) {
        this.permSCT = value;
    }

    public final String getNumPermisoSCT() {
        return this.numPermisoSCT;
    }

    public final void setNumPermisoSCT(String value) {
        this.numPermisoSCT = value;
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

    public final String getTipoEmbarcacion() {
        return this.tipoEmbarcacion;
    }

    public final void setTipoEmbarcacion(String value) {
        this.tipoEmbarcacion = value;
    }

    public final String getMatricula() {
        return this.matricula;
    }

    public final void setMatricula(String value) {
        this.matricula = value;
    }

    public final String getNumeroOMI() {
        return this.numeroOMI;
    }

    public final void setNumeroOMI(String value) {
        this.numeroOMI = value;
    }

    public final int getAnioEmbarcacion() {
        return this.anioEmbarcacion;
    }

    public final void setAnioEmbarcacion(int value) {
        this.anioEmbarcacion = value;
    }

    public final String getNombreEmbarc() {
        return this.nombreEmbarc;
    }

    public final void setNombreEmbarc(String value) {
        this.nombreEmbarc = value;
    }

    public final String getNacionalidadEmbarc() {
        return this.nacionalidadEmbarc;
    }

    public final void setNacionalidadEmbarc(String value) {
        this.nacionalidadEmbarc = value;
    }

    public final double getUnidadesDeArqBruto() {
        return this.unidadesDeArqBruto;
    }

    public final void setUnidadesDeArqBruto(double value) {
        this.unidadesDeArqBruto = value;
    }

    public final String getTipoCarga() {
        return this.tipoCarga;
    }

    public final void setTipoCarga(String value) {
        this.tipoCarga = value;
    }

    public final String getNumCertITC() {
        return this.numCertITC;
    }

    public final void setNumCertITC(String value) {
        this.numCertITC = value;
    }

    public final double getEslora() {
        return this.eslora;
    }

    public final void setEslora(double value) {
        this.eslora = value;
    }

    public final double getManga() {
        return this.manga;
    }

    public final void setManga(double value) {
        this.manga = value;
    }

    public final double getCalado() {
        return this.calado;
    }

    public final void setCalado(double value) {
        this.calado = value;
    }

    public final String getLineaNaviera() {
        return this.lineaNaviera;
    }

    public final void setLineaNaviera(String value) {
        this.lineaNaviera = value;
    }

    public final String getNombreAgenteNaviero() {
        return this.nombreAgenteNaviero;
    }

    public final void setNombreAgenteNaviero(String value) {
        this.nombreAgenteNaviero = value;
    }

    public final String getNumAutorizacionNaviero() {
        return this.numAutorizacionNaviero;
    }

    public final void setNumAutorizacionNaviero(String value) {
        this.numAutorizacionNaviero = value;
    }

    public final String getNumViaje() {
        return this.numViaje;
    }

    public final void setNumViaje(String value) {
        this.numViaje = value;
    }

    public final String getNumConocEmbarc() {
        return this.numConocEmbarc;
    }

    public final void setNumConocEmbarc(String value) {
        this.numConocEmbarc = value;
    }
}
