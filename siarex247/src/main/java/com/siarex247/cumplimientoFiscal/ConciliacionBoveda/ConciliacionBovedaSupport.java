package com.siarex247.cumplimientoFiscal.ConciliacionBoveda;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ConciliacionBovedaSupport extends ActionDB{

	private static final long serialVersionUID = -1158476458339205656L;

	
	 private int anio;
	 private String mesCombo;
	 private String tipoComple;
	 
	 
	public int getAnio() {
		return anio;
	}
	public String getMesCombo() {
		return mesCombo;
	}
	public String getTipoComple() {
		return tipoComple;
	}
	@StrutsParameter
	public void setAnio(int anio) {
		this.anio = anio;
	}
	@StrutsParameter
	public void setMesCombo(String mesCombo) {
		this.mesCombo = mesCombo;
	}
	@StrutsParameter
	public void setTipoComple(String tipoComple) {
		this.tipoComple = tipoComple;
	}

	 
	 
	 
	
	
}
