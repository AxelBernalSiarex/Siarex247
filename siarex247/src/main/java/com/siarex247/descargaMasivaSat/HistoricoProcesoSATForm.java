package com.siarex247.descargaMasivaSat;

public class HistoricoProcesoSATForm {
    
    private int claveHistorico;
    private String tipoDescarga;
    private String tipoComprobando;
    private String accionSat;
    private String solicitudSat;
    private String paqueteSat;
    private String fechaInicio;     
    private String fechaFin;        
    private String fechaDescarga;   
    private String estatusDescarga;
    private String mensajeSat;
    private int totalArchivos;
    private int archivosExitosos;
    private int archivosDuplicados;
    private int archivosErrorRfc;
    private int archivosNomina;
    private String estatus;

    // --- Getters y Setters ---

    public int getClaveHistorico() {
        return claveHistorico;
    }

    public void setClaveHistorico(int claveHistorico) {
        this.claveHistorico = claveHistorico;
    }

    public String getTipoDescarga() {
        return tipoDescarga;
    }

    public void setTipoDescarga(String tipoDescarga) {
        this.tipoDescarga = tipoDescarga;
    }

    public String getTipoComprobando() {
        return tipoComprobando;
    }

    public void setTipoComprobando(String tipoComprobando) {
        this.tipoComprobando = tipoComprobando;
    }

    public String getAccionSat() {
        return accionSat;
    }

    public void setAccionSat(String accionSat) {
        this.accionSat = accionSat;
    }

    public String getSolicitudSat() {
        return solicitudSat;
    }

    public void setSolicitudSat(String solicitudSat) {
        this.solicitudSat = solicitudSat;
    }

    public String getPaqueteSat() {
        return paqueteSat;
    }

    public void setPaqueteSat(String paqueteSat) {
        this.paqueteSat = paqueteSat;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaDescarga() {
        return fechaDescarga;
    }

    public void setFechaDescarga(String fechaDescarga) {
        this.fechaDescarga = fechaDescarga;
    }

    public String getEstatusDescarga() {
        return estatusDescarga;
    }

    public void setEstatusDescarga(String estatusDescarga) {
        this.estatusDescarga = estatusDescarga;
    }

    public String getMensajeSat() {
        return mensajeSat;
    }

    public void setMensajeSat(String mensajeSat) {
        this.mensajeSat = mensajeSat;
    }

    public int getTotalArchivos() {
        return totalArchivos;
    }

    public void setTotalArchivos(int totalArchivos) {
        this.totalArchivos = totalArchivos;
    }

    public int getArchivosExitosos() {
        return archivosExitosos;
    }

    public void setArchivosExitosos(int archivosExitosos) {
        this.archivosExitosos = archivosExitosos;
    }

    public int getArchivosDuplicados() {
        return archivosDuplicados;
    }

    public void setArchivosDuplicados(int archivosDuplicados) {
        this.archivosDuplicados = archivosDuplicados;
    }

    public int getArchivosErrorRfc() {
        return archivosErrorRfc;
    }

    public void setArchivosErrorRfc(int archivosErrorRfc) {
        this.archivosErrorRfc = archivosErrorRfc;
    }

    public int getArchivosNomina() {
        return archivosNomina;
    }

    public void setArchivosNomina(int archivosNomina) {
        this.archivosNomina = archivosNomina;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}