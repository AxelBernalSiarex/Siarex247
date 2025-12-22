package com.siarex247.leerCuenta;

public class BitacoraOrdenCompraHtmForm {

    private Long idBitacora;

    private String numOrden;
    private String codError;
    private String descError;

    private String emailOrigen;
    private String asunto;
    private String archivoHtm;

    public Long getIdBitacora() {
        return idBitacora;
    }

    public void setIdBitacora(Long idBitacora) {
        this.idBitacora = idBitacora;
    }

    public String getNumOrden() {
        return numOrden;
    }

    public void setNumOrden(String numOrden) {
        this.numOrden = numOrden;
    }

    public String getCodError() {
        return codError;
    }

    public void setCodError(String codError) {
        this.codError = codError;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public String getEmailOrigen() {
        return emailOrigen;
    }

    public void setEmailOrigen(String emailOrigen) {
        this.emailOrigen = emailOrigen;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getArchivoHtm() {
        return archivoHtm;
    }

    public void setArchivoHtm(String archivoHtm) {
        this.archivoHtm = archivoHtm;
    }
}
