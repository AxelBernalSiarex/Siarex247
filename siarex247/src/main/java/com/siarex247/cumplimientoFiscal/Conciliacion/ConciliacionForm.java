package com.siarex247.cumplimientoFiscal.Conciliacion;

public class ConciliacionForm {

	
	private double idRegistro = 0;
	private long folioEmpresa = 0;
	private long folioRegistro = 0;
	private String rfc = "";
	private String razonSocial = "";
	private String emailPrimario = "";
	private double montoPagado = 0;
	private String uuid = "";
	private String uuidComplemento = "";
	private String tipoOrden = "";
	private String tipoMoneda = "";
	private String fechaPago = "";
	private String fechaTimbrado = "";
	private String fechaPagoXML = "";
	
	private String serieOrden = "";
	
	private int annio;
	
	private double subTotal = 0;
	private double iva = 0;
	private double ivaRet = 0;
	private double impLocales = 0;
	private double total = 0;
	
	private double totalFactura = 0;
	private double totalComplemento = 0;
	
	private String estatusFactura = "";
	private String estatusComplemento = "";
	private String nombreXML = "";
	private String nombrePDF = "";
	private String estatusConciliacion = "";
	private String estatusConciliacionExcel = "";
	private int claveProveedor = 0;
	
	
	
	
	public double getIdRegistro() {
		return idRegistro;
	}
	public void setIdRegistro(double idRegistro) {
		this.idRegistro = idRegistro;
	}
	
	
	
	public String getEstatusConciliacion() {
		return estatusConciliacion;
	}
	public void setEstatusConciliacion(String estatusConciliacion) {
		this.estatusConciliacion = estatusConciliacion;
	}
	public String getEstatusConciliacionExcel() {
		return estatusConciliacionExcel;
	}
	public void setEstatusConciliacionExcel(String estatusConciliacionExcel) {
		this.estatusConciliacionExcel = estatusConciliacionExcel;
	}
	public String getNombreXML() {
		return nombreXML;
	}
	public void setNombreXML(String nombreXML) {
		this.nombreXML = nombreXML;
	}
	public String getNombrePDF() {
		return nombrePDF;
	}
	public void setNombrePDF(String nombrePDF) {
		this.nombrePDF = nombrePDF;
	}
	public String getEstatusFactura() {
		return estatusFactura;
	}
	public void setEstatusFactura(String estatusFactura) {
		this.estatusFactura = estatusFactura;
	}
	public String getEstatusComplemento() {
		return estatusComplemento;
	}
	public void setEstatusComplemento(String estatusComplemento) {
		this.estatusComplemento = estatusComplemento;
	}
	public double getTotalFactura() {
		return totalFactura;
	}
	public void setTotalFactura(double totalFactura) {
		this.totalFactura = totalFactura;
	}
	public double getTotalComplemento() {
		return totalComplemento;
	}
	public void setTotalComplemento(double totalComplemento) {
		this.totalComplemento = totalComplemento;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public double getIva() {
		return iva;
	}
	public void setIva(double iva) {
		this.iva = iva;
	}
	public double getIvaRet() {
		return ivaRet;
	}
	public void setIvaRet(double ivaRet) {
		this.ivaRet = ivaRet;
	}
	public double getImpLocales() {
		return impLocales;
	}
	public void setImpLocales(double impLocales) {
		this.impLocales = impLocales;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public long getFolioRegistro() {
		return folioRegistro;
	}
	public void setFolioRegistro(long folioRegistro) {
		this.folioRegistro = folioRegistro;
	}
	public long getFolioEmpresa() {
		return folioEmpresa;
	}
	public void setFolioEmpresa(long folioEmpresa) {
		this.folioEmpresa = folioEmpresa;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getEmailPrimario() {
		return emailPrimario;
	}
	public void setEmailPrimario(String emailPrimario) {
		this.emailPrimario = emailPrimario;
	}
	public double getMontoPagado() {
		return montoPagado;
	}
	public void setMontoPagado(double montoPagado) {
		this.montoPagado = montoPagado;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUuidComplemento() {
		return uuidComplemento;
	}
	public void setUuidComplemento(String uuidComplemento) {
		this.uuidComplemento = uuidComplemento;
	}
	public String getTipoOrden() {
		return tipoOrden;
	}
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}
	public String getSerieOrden() {
		return serieOrden;
	}
	public void setSerieOrden(String serieOrden) {
		this.serieOrden = serieOrden;
	}
	public String getFechaTimbrado() {
		return fechaTimbrado;
	}
	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}
	public String getFechaPagoXML() {
		return fechaPagoXML;
	}
	public void setFechaPagoXML(String fechaPagoXML) {
		this.fechaPagoXML = fechaPagoXML;
	}
	public int getClaveProveedor() {
		return claveProveedor;
	}
	public void setClaveProveedor(int claveProveedor) {
		this.claveProveedor = claveProveedor;
	}
	public int getAnnio() {
		return annio;
	}
	public void setAnnio(int annio) {
		this.annio = annio;
	}
	


	
	
	
}
