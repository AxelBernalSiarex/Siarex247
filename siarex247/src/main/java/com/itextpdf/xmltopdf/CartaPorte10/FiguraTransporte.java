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
public class FiguraTransporte {

    private ArrayList<Operadores> operadores;
    private ArrayList<Propietario> propietario;
    private ArrayList<Arrendatario> arrendatario;
    private ArrayList<Notificado> notificado;
    private String cveTransporte = "";

    public final ArrayList<Operadores> getOperadores() {
        return this.operadores;
    }

    public final void setOperadores(ArrayList<Operadores> value) {
        this.operadores = value;
    }

    public final ArrayList<Propietario> getPropietario() {
        return this.propietario;
    }

    public final void setPropietario(ArrayList<Propietario> value) {
        this.propietario = value;
    }

    public final ArrayList<Arrendatario> getArrendatario() {
        return this.arrendatario;
    }

    public final void setArrendatario(ArrayList<Arrendatario> value) {
        this.arrendatario = value;
    }

    public final ArrayList<Notificado> getNotificado() {
        return this.notificado;
    }

    public final void setNotificado(ArrayList<Notificado> value) {
        this.notificado = value;
    }

    public final String getCveTransporte() {
        return this.cveTransporte;
    }

    public final void setCveTransporte(String value) {
        this.cveTransporte = value;
    }
}
