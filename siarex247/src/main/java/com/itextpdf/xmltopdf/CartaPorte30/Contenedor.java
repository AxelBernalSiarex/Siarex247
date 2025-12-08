package com.itextpdf.xmltopdf.CartaPorte30;

import java.time.LocalDateTime;

public class Contenedor {
    private String tipoContenedor = "";
    private String matriculaContenedor = "";
    private String numPrecinto = "";
    private String idCCPRelacionado = "";
    private String placaVMCCP = "";
    private LocalDateTime fechaCertificacionCCP;

    public String getTipoContenedor() {
        return tipoContenedor;
    }

    public void setTipoContenedor(String tipoContenedor) {
        this.tipoContenedor = tipoContenedor;
    }

    public String getMatriculaContenedor() {
        return matriculaContenedor;
    }

    public void setMatriculaContenedor(String matriculaContenedor) {
        this.matriculaContenedor = matriculaContenedor;
    }

    public String getNumPrecinto() {
        return numPrecinto;
    }

    public void setNumPrecinto(String numPrecinto) {
        this.numPrecinto = numPrecinto;
    }

    public String getIdCCPRelacionado() {
        return idCCPRelacionado;
    }

    public void setIdCCPRelacionado(String idCCPRelacionado) {
        this.idCCPRelacionado = idCCPRelacionado;
    }

    public String getPlacaVMCCP() {
        return placaVMCCP;
    }

    public void setPlacaVMCCP(String placaVMCCP) {
        this.placaVMCCP = placaVMCCP;
    }

    public LocalDateTime getFechaCertificacionCCP() {
        return fechaCertificacionCCP;
    }

    public void setFechaCertificacionCCP(LocalDateTime fechaCertificacionCCP) {
        this.fechaCertificacionCCP = fechaCertificacionCCP;
    }

}
