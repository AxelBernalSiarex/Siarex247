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
public class Contenedor {

    private String matriculaContenedor = "";
    private String tipoContenedor = "";
    private String numPrecinto = "";

    public final String getMatriculaContenedor() {
        return this.matriculaContenedor;
    }

    public final void setMatriculaContenedor(String value) {
        this.matriculaContenedor = value;
    }

    public final String getTipoContenedor() {
        return this.tipoContenedor;
    }

    public final void setTipoContenedor(String value) {
        this.tipoContenedor = value;
    }

    public final String getNumPrecinto() {
        return this.numPrecinto;
    }

    public final void setNumPrecinto(String value) {
        this.numPrecinto = value;
    }
}
