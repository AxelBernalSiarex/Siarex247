package com.siarex247.cumplimientoFiscal.HistorialPagos;

public class HistorialPagosForm {

    private int idRegistro;
    private String rfc;
    private String fechaPago;
    private String uuidFactura;
    private String tipoMoneda;
    private double total;
    private String estatus;
    private String codigoError;
    private String uuidComplemento;
    private String usuarioTran;
    private String fechaTrans;
    private String usuarioCambio;
    private String fechaCambio;
    
    private String serie;
    private String folio;

    public int getIdRegistro() { return idRegistro; }
    public void setIdRegistro(int idRegistro) { this.idRegistro = idRegistro; }

    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }

    public String getFechaPago() { return fechaPago; }
    public void setFechaPago(String fechaPago) { this.fechaPago = fechaPago; }

    public String getUuidFactura() { return uuidFactura; }
    public void setUuidFactura(String uuidFactura) { this.uuidFactura = uuidFactura; }

    public String getTipoMoneda() { return tipoMoneda; }
    public void setTipoMoneda(String tipoMoneda) { this.tipoMoneda = tipoMoneda; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public String getCodigoError() { return codigoError; }
    public void setCodigoError(String codigoError) { this.codigoError = codigoError; }

    public String getUuidComplemento() { return uuidComplemento; }
    public void setUuidComplemento(String uuidComplemento) { this.uuidComplemento = uuidComplemento; }

    public String getUsuarioTran() { return usuarioTran; }
    public void setUsuarioTran(String usuarioTran) { this.usuarioTran = usuarioTran; }

    public String getFechaTrans() { return fechaTrans; }
    public void setFechaTrans(String fechaTrans) { this.fechaTrans = fechaTrans; }

    public String getUsuarioCambio() { return usuarioCambio; }
    public void setUsuarioCambio(String usuarioCambio) { this.usuarioCambio = usuarioCambio; }

    public String getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(String fechaCambio) { this.fechaCambio = fechaCambio; }
    
    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }
}
