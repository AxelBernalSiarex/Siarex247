package com.siarex247.dashboard.Monitor360;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class Monitor360Support extends ActionDB {

	
	private static final long serialVersionUID = -3819634336995170520L;

	private String tipoConsulta;
	private int annio;
	private String mes;
	private String tipo;
	private String contribuyente;
	private String tipoMoneda;
	
	public int getAnnio() {
		return annio;
	}
	public String getMes() {
		return mes;
	}
	public String getTipo() {
		return tipo;
	}
	public String getContribuyente() {
		return contribuyente;
	}
	@StrutsParameter
	public void setAnnio(int annio) {
		this.annio = annio;
	}
	@StrutsParameter
	public void setMes(String mes) {
		this.mes = mes;
	}
	@StrutsParameter
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	@StrutsParameter
	public void setContribuyente(String contribuyente) {
		this.contribuyente = contribuyente;
	}
	public String getTipoConsulta() {
		return tipoConsulta;
	}
	@StrutsParameter
	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	@StrutsParameter
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	
	
	
	
}
