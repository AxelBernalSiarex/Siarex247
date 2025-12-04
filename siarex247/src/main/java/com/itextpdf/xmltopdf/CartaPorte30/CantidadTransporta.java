package com.itextpdf.xmltopdf.CartaPorte30;

public class CantidadTransporta {
    public double cantidad = 0;
    public String iDOrigen = "";
    public String iDDestino = "";
    public String cvesTransporte = "";

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getIDOrigen() {
        return iDOrigen;
    }

    public void setIDOrigen(String iDOrigen) {
        this.iDOrigen = iDOrigen;
    }

    public String getIDDestino() {
        return iDDestino;
    }

    public void setIDDestino(String iDDestino) {
        this.iDDestino = iDDestino;
    }

    public String getCvesTransporte() {
        return cvesTransporte;
    }

    public void setCvesTransporte(String cvesTransporte) {
        this.cvesTransporte = cvesTransporte;
    }

}
