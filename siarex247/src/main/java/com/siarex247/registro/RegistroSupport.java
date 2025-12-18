package com.siarex247.registro;

import java.io.InputStream;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class RegistroSupport extends ActionDB {
	private static final long serialVersionUID = -5552797838922085096L;

	
	private int claveEmpresa = 0;
		
	private String codeDescarga;
	private String mensajeError;
	private String lenguaje;
	
	private InputStream inputStream;
	
	
	public int getClaveEmpresa() {
		return claveEmpresa;
	}
	@StrutsParameter
	public void setClaveEmpresa(int claveEmpresa) {
		this.claveEmpresa = claveEmpresa;
	}

	public String getCodeDescarga() {
		return codeDescarga;
	}
	@StrutsParameter
	public void setCodeDescarga(String codeDescarga) {
		this.codeDescarga = codeDescarga;
	}
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public String getLenguaje() {
		return lenguaje;
	}
	@StrutsParameter
	public void setLenguaje(String lenguaje) {
		this.lenguaje = lenguaje;
	}
	
	

	
	
	
	
}
