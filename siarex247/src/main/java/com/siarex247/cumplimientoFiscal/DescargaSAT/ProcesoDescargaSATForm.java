package com.siarex247.cumplimientoFiscal.DescargaSAT;

public class ProcesoDescargaSATForm {

	private int idRegistro;
	private String tipoComprobante;
	private String accionSat;
	private String solicitudSat;
	private String paqueteSat;
	private String fechaInicio;
	private String fechaFin;
	private String fechaDescarga;
	private String estatusDescarga;
	private String mensajeSat;
	
	
	public int getIdRegistro() {
		return idRegistro;
	}
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	
	
	
	public String getAccionSat() {
		return accionSat;
	}
	public void setAccionSat(String accionSat) {
		this.accionSat = accionSat;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
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
	
	
	
	
}
