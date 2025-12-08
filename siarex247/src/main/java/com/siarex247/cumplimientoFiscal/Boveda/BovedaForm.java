package com.siarex247.cumplimientoFiscal.Boveda;

public class BovedaForm {
	private int idRegistro = 0;
	private String uuid = ""; 
	private String rfc = "";
    private String razonSocial = "";
	private String serie = "";
	private String folio = "";
	private String xml = "";
	private String fechaFactura = "";
	private String tipoComprobante = "";
	private String total = "";
	private String subTotal = "";
	private String iva = "";
	private String retIVA = "";
	private String retISR = "";
	private String impLocales = "";


	private String fechaInicial;
	private String fechaFinal;
	private boolean bandFechas;
	
	public int getIdRegistro() {
		return idRegistro;
	}
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getIva() {
		return iva;
	}
	public void setIva(String iva) {
		this.iva = iva;
	}
	public String getRetIVA() {
		return retIVA;
	}
	public void setRetIVA(String retIVA) {
		this.retIVA = retIVA;
	}
	public String getRetISR() {
		return retISR;
	}
	public void setRetISR(String retISR) {
		this.retISR = retISR;
	}
	public String getImpLocales() {
		return impLocales;
	}
	public void setImpLocales(String impLocales) {
		this.impLocales = impLocales;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	public String getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public String getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public boolean isBandFechas() {
		return bandFechas;
	}
	public void setBandFechas(boolean bandFechas) {
		this.bandFechas = bandFechas;
	}
	
	
}
