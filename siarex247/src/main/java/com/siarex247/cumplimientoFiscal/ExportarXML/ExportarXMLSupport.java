package com.siarex247.cumplimientoFiscal.ExportarXML;

import java.io.InputStream;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ExportarXMLSupport extends ActionDB{

	private static final long serialVersionUID = 3673689896729937848L;

	private String correoResponsable;
	private String modoAgrupar;
	private String validarSAT;
	private String complementoSAT;
	
	
    
    private int idArchivo;
    
    private String codeDescarga;
	private String mensajeError;
	
	private InputStream inputStream;
	
	
    // boveda
	private String rfc;
	private String razonSocial;
	private String folio;
	private String serie;
	private String uuid;
	private String fechaInicial;
	private String fechaFinal;
	private String tipoComprobante;
	private String fechaFactura;
	private String idRegistro;
	private String nombreEmpresa;
	
	
	private String descargarNotaCredito;
	private String notaCreditoSAT;
	private String descargarFacturas;
	private String descargarComplemento;
	private String rfcProveedor;
	private String tipoExportacion;
	
	
	
	public String getCorreoResponsable() {
		return correoResponsable;
	}
	@StrutsParameter
	public void setCorreoResponsable(String correoResponsable) {
		this.correoResponsable = correoResponsable;
	}
	public String getModoAgrupar() {
		return modoAgrupar;
	}
	@StrutsParameter
	public void setModoAgrupar(String modoAgrupar) {
		this.modoAgrupar = modoAgrupar;
	}
	public String getValidarSAT() {
		return validarSAT;
	}
	@StrutsParameter
	public void setValidarSAT(String validarSAT) {
		this.validarSAT = validarSAT;
	}
	public String getComplementoSAT() {
		return complementoSAT;
	}
	@StrutsParameter
	public void setComplementoSAT(String complementoSAT) {
		this.complementoSAT = complementoSAT;
	}
	
	public int getIdArchivo() {
		return idArchivo;
	}
	@StrutsParameter
	public void setIdArchivo(int idArchivo) {
		this.idArchivo = idArchivo;
	}
	public String getCodeDescarga() {
		return codeDescarga;
	}
	@StrutsParameter
	public void setCodeDescarga(String codeDescarga) {
		this.codeDescarga = codeDescarga;
	}
	public String getMensajeError() {
		return mensajeError;
	}
	@StrutsParameter
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	@StrutsParameter
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getRfc() {
		return rfc;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public String getFolio() {
		return folio;
	}
	public String getSerie() {
		return serie;
	}
	public String getUuid() {
		return uuid;
	}
	public String getFechaInicial() {
		return fechaInicial;
	}
	public String getFechaFinal() {
		return fechaFinal;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public String getIdRegistro() {
		return idRegistro;
	}
	@StrutsParameter
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	@StrutsParameter
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	@StrutsParameter
	public void setFolio(String folio) {
		this.folio = folio;
	}
	@StrutsParameter
	public void setSerie(String serie) {
		this.serie = serie;
	}
	@StrutsParameter
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@StrutsParameter
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	@StrutsParameter
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	@StrutsParameter
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	@StrutsParameter
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	@StrutsParameter
	public void setIdRegistro(String idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	@StrutsParameter
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	public String getDescargarNotaCredito() {
		return descargarNotaCredito;
	}
	@StrutsParameter
	public void setDescargarNotaCredito(String descargarNotaCredito) {
		this.descargarNotaCredito = descargarNotaCredito;
	}
	public String getNotaCreditoSAT() {
		return notaCreditoSAT;
	}
	@StrutsParameter
	public void setNotaCreditoSAT(String notaCreditoSAT) {
		this.notaCreditoSAT = notaCreditoSAT;
	}
	public String getDescargarFacturas() {
		return descargarFacturas;
	}
	@StrutsParameter
	public void setDescargarFacturas(String descargarFacturas) {
		this.descargarFacturas = descargarFacturas;
	}
	public String getDescargarComplemento() {
		return descargarComplemento;
	}
	@StrutsParameter
	public void setDescargarComplemento(String descargarComplemento) {
		this.descargarComplemento = descargarComplemento;
	}
	public String getRfcProveedor() {
		return rfcProveedor;
	}
	@StrutsParameter
	public void setRfcProveedor(String rfcProveedor) {
		this.rfcProveedor = rfcProveedor;
	}
	public String getTipoExportacion() {
		return tipoExportacion;
	}
	@StrutsParameter
	public void setTipoExportacion(String tipoExportacion) {
		this.tipoExportacion = tipoExportacion;
	}
	
	
}
