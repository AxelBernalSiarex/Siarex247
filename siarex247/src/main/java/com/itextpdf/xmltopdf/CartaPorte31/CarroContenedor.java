package com.itextpdf.xmltopdf.CartaPorte31;

/**
 *
 * @author frack
 */
public class CarroContenedor {

    private String tipoContenedor = "";
    private double pesoContenedorVacio;
    private double pesoNetoMercancia;

    public final String getTipoContenedor() {
        return this.tipoContenedor;
    }

    public final void setTipoContenedor(String value) {
        this.tipoContenedor = value;
    }

    public final double getPesoContenedorVacio() {
        return this.pesoContenedorVacio;
    }

    public final void setPesoContenedorVacio(double value) {
        this.pesoContenedorVacio = value;
    }

    public final double getPesoNetoMercancia() {
        return this.pesoNetoMercancia;
    }

    public final void setPesoNetoMercancia(double value) {
        this.pesoNetoMercancia = value;
    }
}
