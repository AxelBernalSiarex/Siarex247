package com.siarex247.prodigia.data;

public class ConsultaComprobante {
	private String consultaOk;
	private String codigo;
	private String codigoEstatus;
	private String esCancelable;
	private String estado;
	private String estatusCfdi;
	
	
	public String getConsultaOk() {
		return consultaOk;
	}
	public void setConsultaOk(String consultaOk) {
		this.consultaOk = consultaOk;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getCodigoEstatus() {
		return codigoEstatus;
	}
	public void setCodigoEstatus(String mensaje) {
		this.codigoEstatus = mensaje;
	}
	public String getEsCancelable() {
		return esCancelable;
	}
	public void setEsCancelable(String esCancelable) {
		this.esCancelable = esCancelable;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getEstatusCfdi() {
		return estatusCfdi;
	}
	public void setEstatusCfdi(String estatusCfdi) {
		this.estatusCfdi = estatusCfdi;
	}
}
