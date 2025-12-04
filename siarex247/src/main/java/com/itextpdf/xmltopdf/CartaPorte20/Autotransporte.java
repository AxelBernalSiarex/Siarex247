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
public class Autotransporte {

    private IdentificacionVehicular identificacionVehicular = null;
    private Seguros seguros = null;
    private Remolques remolques = null;
    private String permSCT = "";
    private String numPermisoSCT = "";

    public final IdentificacionVehicular getIdentificacionVehicular() {
        return this.identificacionVehicular;
    }

    public final void setIdentificacionVehicular(IdentificacionVehicular value) {
        this.identificacionVehicular = value;
    }

    public final Seguros getSeguros() {
        return this.seguros;
    }

    public final void setSeguros(Seguros value) {
        this.seguros = value;
    }

    public final Remolques getRemolques() {
        return this.remolques;
    }

    public final void setRemolques(Remolques value) {
        this.remolques = value;
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
}
