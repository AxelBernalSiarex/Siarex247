package com.siarex247.configSistema.CierreAnnio;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class CierreAnnioSupport extends ActionDB {

	private static final long serialVersionUID = 5622093695116935135L;

	private int anio = 0;
	private String tipoCierre;
	private String fechaApartir = "";
	private String mensajeError1 = "";
	private String fechaHasta = "";
	private String mensajeError2 = "";
	
	public int getAnio() {
		return anio;
	}
	@StrutsParameter
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public String getFechaApartir() {
		return fechaApartir;
	}
	@StrutsParameter
	public void setFechaApartir(String fechaApartir) {
		this.fechaApartir = fechaApartir;
	}
	
	public String getFechaHasta() {
		return fechaHasta;
	}
	@StrutsParameter
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	public String getTipoCierre() {
		return tipoCierre;
	}
	@StrutsParameter
	public void setTipoCierre(String tipoCierre) {
		this.tipoCierre = tipoCierre;
	}
	public String getMensajeError1() {
		return mensajeError1;
	}
	@StrutsParameter
	public void setMensajeError1(String mensajeError1) {
		this.mensajeError1 = mensajeError1;
	}
	public String getMensajeError2() {
		return mensajeError2;
	}
	@StrutsParameter
	public void setMensajeError2(String mensajeError2) {
		this.mensajeError2 = mensajeError2;
	}
	

	
	
	
	
}
