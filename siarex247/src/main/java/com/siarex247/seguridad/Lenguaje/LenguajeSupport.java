package com.siarex247.seguridad.Lenguaje;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class LenguajeSupport extends ActionDB {

	private static final long serialVersionUID = 3206617236881240197L;

	private String nombrePantalla;

	public String getNombrePantalla() {
		return nombrePantalla;
	}
	@StrutsParameter
	public void setNombrePantalla(String nombrePantalla) {
		this.nombrePantalla = nombrePantalla;
	}
	
	
	
}
