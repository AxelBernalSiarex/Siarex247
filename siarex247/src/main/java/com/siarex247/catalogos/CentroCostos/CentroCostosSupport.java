package com.siarex247.catalogos.CentroCostos;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class CentroCostosSupport extends ActionDB{

	private static final long serialVersionUID = -7070783080648372048L;

	private String idCentro;
	private String departamento;
	private String correoCentro;
	private int idRegistro;
	
	public int getIdRegistro() {
		return idRegistro;
	}
	@StrutsParameter
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getIdCentro() {
		return idCentro;
	}
	@StrutsParameter
	public void setIdCentro(String idCentro) {
		this.idCentro = idCentro;
	}
	public String getDepartamento() {
		return departamento;
	}
	@StrutsParameter
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public String getCorreoCentro() {
		return correoCentro;
	}
	@StrutsParameter
	public void setCorreoCentro(String correoCentro) {
		this.correoCentro = correoCentro;
	}
	
	
	
	
}
