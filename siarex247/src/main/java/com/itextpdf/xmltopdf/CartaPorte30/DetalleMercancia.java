package com.itextpdf.xmltopdf.CartaPorte30;

public class DetalleMercancia {
    public String unidadPesoMerc = "";
    public double pesoBruto = 0;
    public double pesoNeto = 0;
    public double pesoTara = 0;
    public int numPiezas = 0;

    public String getUnidadPesoMerc() {
        return unidadPesoMerc;
    }

    public void setUnidadPesoMerc(String unidadPesoMerc) {
        this.unidadPesoMerc = unidadPesoMerc;
    }

    public double getPesoBruto() {
        return pesoBruto;
    }

    public void setPesoBruto(double pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    public double getPesoNeto() {
        return pesoNeto;
    }

    public void setPesoNeto(double pesoNeto) {
        this.pesoNeto = pesoNeto;
    }

    public double getPesoTara() {
        return pesoTara;
    }

    public void setPesoTara(double pesoTara) {
        this.pesoTara = pesoTara;
    }

    public int getNumPiezas() {
        return numPiezas;
    }

    public void setNumPiezas(int numPiezas) {
        this.numPiezas = numPiezas;
    }
}
