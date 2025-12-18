package com.siarex247.configuraciones.RegimenFiscal;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class RegimenFiscalSupport extends ActionDB{

	private static final long serialVersionUID = 2337279503133898078L;
	
	private int idRegistro;
	private String accion;
	private String razonSocialRF;
	private String regimenFiscal;
	
	
	public String getRazonSocialRF() {
		return razonSocialRF;
	}
	@StrutsParameter
	public void setRazonSocialRF(String razonSocialRF) {
		this.razonSocialRF = razonSocialRF;
	}
	public String getRegimenFiscal() {
		return regimenFiscal;
	}
	@StrutsParameter
	public void setRegimenFiscal(String regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}
	public int getIdRegistro() {
		return idRegistro;
	}
	@StrutsParameter
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getAccion() {
		return accion;
	}
	@StrutsParameter
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	
	
}
