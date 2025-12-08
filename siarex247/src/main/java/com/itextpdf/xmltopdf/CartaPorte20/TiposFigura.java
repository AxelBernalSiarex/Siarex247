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
public class TiposFigura {

    private ArrayList<PartesTransporte> partesTransporte;
    private Domicilio domicilio;
    private String tipoFigura = "";
    private String rFCFigura = "";
    private String numLicencia = "";
    private String nombreFigura = "";
    private String numRegIdTribFigura = "";
    private String residenciaFiscalFigura = "";

    public final ArrayList<PartesTransporte> getPartesTransporte() {
        return this.partesTransporte;
    }

    public final void setPartesTransporte(ArrayList<PartesTransporte> value) {
        this.partesTransporte = value;
    }

    public final Domicilio getDomicilio() {
        return this.domicilio;
    }

    public final void setDomicilio(Domicilio value) {
        this.domicilio = value;
    }

    public final String getTipoFigura() {
        return this.tipoFigura;
    }

    public final void setTipoFigura(String value) {
        this.tipoFigura = value;
    }

    public final String getRFCFigura() {
        return this.rFCFigura;
    }

    public final void setRFCFigura(String value) {
        this.rFCFigura = value;
    }

    public final String getNumLicencia() {
        return this.numLicencia;
    }

    public final void setNumLicencia(String value) {
        this.numLicencia = value;
    }

    public final String getNombreFigura() {
        return this.nombreFigura;
    }

    public final void setNombreFigura(String value) {
        this.nombreFigura = value;
    }

    public final String getNumRegIdTribFigura() {
        return this.numRegIdTribFigura;
    }

    public final void setNumRegIdTribFigura(String value) {
        this.numRegIdTribFigura = value;
    }

    public final String getResidenciaFiscalFigura() {
        return this.residenciaFiscalFigura;
    }

    public final void setResidenciaFiscalFigura(String value) {
        this.residenciaFiscalFigura = value;
    }
}
