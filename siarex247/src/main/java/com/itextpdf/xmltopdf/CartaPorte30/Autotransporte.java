package com.itextpdf.xmltopdf.CartaPorte30;

public class Autotransporte {
    private String permSCT = "";
    private String numPermisoSCT = "";
    private IdentificacionVehicular identificacionVehicular;
    private Seguros seguros;
    private Remolques remolques;

    public String getPermSCT() {
        return permSCT;
    }

    public void setPermSCT(String permSCT) {
        this.permSCT = permSCT;
    }

    public String getNumPermisoSCT() {
        return numPermisoSCT;
    }

    public void setNumPermisoSCT(String numPermisoSCT) {
        this.numPermisoSCT = numPermisoSCT;
    }

    public IdentificacionVehicular getIdentificacionVehicular() {
        return identificacionVehicular;
    }

    public void setIdentificacionVehicular(IdentificacionVehicular identificacionVehicular) {
        this.identificacionVehicular = identificacionVehicular;
    }

    public Seguros getSeguros() {
        return seguros;
    }

    public void setSeguros(Seguros seguros) {
        this.seguros = seguros;
    }

    public Remolques getRemolques() {
        return remolques;
    }

    public void setRemolques(Remolques remolques) {
        this.remolques = remolques;
    }

}