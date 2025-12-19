package com.siarex247.leerCuenta;

import java.math.BigDecimal;

public class OrdenCompraHtmData {

    private String ordenCompra;
    private String empresa;
    private String taxId;
    private String moneda;
    private BigDecimal monto;
    private String clasificacionDominio;
    private String clasificacionCodigo;
    private String emailDestino;

    public String getOrdenCompra() { return ordenCompra; }
    public void setOrdenCompra(String ordenCompra) { this.ordenCompra = ordenCompra; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getClasificacionDominio() { return clasificacionDominio; }
    public void setClasificacionDominio(String clasificacionDominio) { this.clasificacionDominio = clasificacionDominio; }

    public String getClasificacionCodigo() { return clasificacionCodigo; }
    public void setClasificacionCodigo(String clasificacionCodigo) { this.clasificacionCodigo = clasificacionCodigo; }

    public String getEmailDestino() { return emailDestino; }
    public void setEmailDestino(String emailDestino) { this.emailDestino = emailDestino; }
}
