package com.siarex247.cumplimientoFiscal.ConciliacionCP;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ConciliacionCPSupport extends ActionDB{

	private static final long serialVersionUID = -1262109792966255806L;
	
	
	 private int anio;
	 private String razonSocial;
	 private String mesCombo;
	 private String tipoComple;
	 
	 
	public int getAnio() {
		return anio;
	}
	@StrutsParameter
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	@StrutsParameter
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getMesCombo() {
		return mesCombo;
	}
	@StrutsParameter
	public void setMesCombo(String mesCombo) {
		this.mesCombo = mesCombo;
	}
	public String getTipoComple() {
		return tipoComple;
	}
	@StrutsParameter
	public void setTipoComple(String tipoComple) {
		this.tipoComple = tipoComple;
	}
	 
	 
	 
	 

}
