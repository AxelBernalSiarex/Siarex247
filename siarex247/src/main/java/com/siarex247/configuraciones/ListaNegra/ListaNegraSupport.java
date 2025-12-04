package com.siarex247.configuraciones.ListaNegra;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ListaNegraSupport extends ActionDB{

	private static final long serialVersionUID = 6808275417355527498L;

	private int idRegistro;
	private String diaMes;

	
	
	public int getIdRegistro() {
		return idRegistro;
	}
	@StrutsParameter
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}

	public String getDiaMes() {
		return diaMes;
	}
	@StrutsParameter
	public void setDiaMes(String diaMes) {
		this.diaMes = diaMes;
	}
	
	
	
	
}
