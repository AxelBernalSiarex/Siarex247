package com.siarex247.cumplimientoFiscal.BovedaEmitidos;

import com.siarex247.utils.Utils;

public class XMLEmitidosForm {
	
	private String uuid;
	private String version;
	private String serie;
	private String folio;
	private String fechaFactura;
	private String sello;
	private String formaPago;
	private String NoCertificado;
	private String certificado;
	private String condicionesPago;
	private double subTotal;
	private double total;
	private double descuento;
	private String moneda;
	private double tipoCambio;
	private String tipoComprobante;
	private String metodoPago;
	private String lugarExpedicion;
	
	private String emisorRFC;
	private String emisorNombre;
	private String emisorRegimen;
	private String receptorRFC;
	private String receptorNombre;
	private String receptorResidencia;
	private String receptorUso;
	private String receptorDomicilio;
	private String receptorRegimen;
	
	private double totalImpuestoRet;
	private double totalImpuestoTranslado;
	
	private String fechaPago;
	private String versionComplemento;
	private String fechaTimbrado;
	private String rfcProvCert;
	private String selloCFD;
	private String noCertificadoSAT;
	private String selloSAT;
	private String certificadoPago;
	public String getUuid() {
		return uuid;
	}
	public String getVersion() {
		return version;
	}
	public String getSerie() {
		return serie;
	}
	public String getFolio() {
		return folio;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public String getSello() {
		return sello;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public String getNoCertificado() {
		return NoCertificado;
	}
	public String getCertificado() {
		return certificado;
	}
	public String getCondicionesPago() {
		return condicionesPago;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public double getTotal() {
		return total;
	}
	public double getDescuento() {
		return descuento;
	}
	public String getMoneda() {
		return moneda;
	}
	public double getTipoCambio() {
		return tipoCambio;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public String getLugarExpedicion() {
		return lugarExpedicion;
	}
	public String getEmisorRFC() {
		return emisorRFC;
	}
	public String getEmisorNombre() {
		return emisorNombre;
	}
	public String getEmisorRegimen() {
		return emisorRegimen;
	}
	public String getReceptorRFC() {
		return receptorRFC;
	}
	public String getReceptorNombre() {
		return receptorNombre;
	}
	public String getReceptorResidencia() {
		return receptorResidencia;
	}
	public String getReceptorUso() {
		return receptorUso;
	}
	public String getReceptorDomicilio() {
		return receptorDomicilio;
	}
	public String getReceptorRegimen() {
		return receptorRegimen;
	}
	public double getTotalImpuestoRet() {
		return totalImpuestoRet;
	}
	public double getTotalImpuestoTranslado() {
		return totalImpuestoTranslado;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public String getVersionComplemento() {
		return versionComplemento;
	}
	public String getFechaTimbrado() {
		return fechaTimbrado;
	}
	public String getRfcProvCert() {
		return rfcProvCert;
	}
	public String getSelloCFD() {
		return selloCFD;
	}
	public String getNoCertificadoSAT() {
		return noCertificadoSAT;
	}
	public String getSelloSAT() {
		return selloSAT;
	}
	public String getCertificadoPago() {
		return certificadoPago;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public void setSello(String sello) {
		this.sello = sello;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public void setNoCertificado(String noCertificado) {
		NoCertificado = noCertificado;
	}
	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}
	public void setCondicionesPago(String condicionesPago) {
		this.condicionesPago = condicionesPago;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	public void setLugarExpedicion(String lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
	}
	public void setEmisorRFC(String emisorRFC) {
		this.emisorRFC = emisorRFC;
	}
	public void setEmisorNombre(String emisorNombre) {
		this.emisorNombre = emisorNombre;
	}
	public void setEmisorRegimen(String emisorRegimen) {
		this.emisorRegimen = emisorRegimen;
	}
	public void setReceptorRFC(String receptorRFC) {
		this.receptorRFC = receptorRFC;
	}
	public void setReceptorNombre(String receptorNombre) {
		this.receptorNombre = receptorNombre;
	}
	public void setReceptorResidencia(String receptorResidencia) {
		this.receptorResidencia = receptorResidencia;
	}
	public void setReceptorUso(String receptorUso) {
		this.receptorUso = receptorUso;
	}
	public void setReceptorDomicilio(String receptorDomicilio) {
		this.receptorDomicilio = receptorDomicilio;
	}
	public void setReceptorRegimen(String receptorRegimen) {
		this.receptorRegimen = receptorRegimen;
	}
	public void setTotalImpuestoRet(double totalImpuestoRet) {
		this.totalImpuestoRet = totalImpuestoRet;
	}
	public void setTotalImpuestoTranslado(double totalImpuestoTranslado) {
		this.totalImpuestoTranslado = totalImpuestoTranslado;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public void setVersionComplemento(String versionComplemento) {
		this.versionComplemento = versionComplemento;
	}
	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}
	public void setRfcProvCert(String rfcProvCert) {
		this.rfcProvCert = rfcProvCert;
	}
	public void setSelloCFD(String selloCFD) {
		this.selloCFD = selloCFD;
	}
	public void setNoCertificadoSAT(String noCertificadoSAT) {
		this.noCertificadoSAT = noCertificadoSAT;
	}
	public void setSelloSAT(String selloSAT) {
		this.selloSAT = selloSAT;
	}
	public void setCertificadoPago(String certificadoPago) {
		this.certificadoPago = certificadoPago;
	}
	
	
	
	
		
	
}
