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
public class AutotransporteFederal {

    private IdentificacionVehicular identificacionVehicular;

    private Remolques remolques;

    private String permSCT = "";

    private String numPermisoSCT = "";

    private String nombreAseg = "";

    private String numPolizaSeguro = "";

    public final IdentificacionVehicular getIdentificacionVehicular() {
        return this.identificacionVehicular;
    }

    public final void setIdentificacionVehicular(IdentificacionVehicular value) {
        this.identificacionVehicular = value;
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
