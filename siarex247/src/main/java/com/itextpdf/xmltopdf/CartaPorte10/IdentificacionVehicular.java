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
public class IdentificacionVehicular {

    private String configVehicular = "";
    private String placaVM = "";
    private int anioModeloVM;

    public final String getConfigVehicular() {
        return this.configVehicular;
    }

    public final void setConfigVehicular(String value) {
        this.configVehicular = value;
    }

    public final String getPlacaVM() {
        return this.placaVM;
    }

    public final void setPlacaVM(String value) {
        this.placaVM = value;
    }

    public final int getAnioModeloVM() {
        return this.anioModeloVM;
    }

    public final void setAnioModeloVM(int value) {
        this.anioModeloVM = value;
    }
}
