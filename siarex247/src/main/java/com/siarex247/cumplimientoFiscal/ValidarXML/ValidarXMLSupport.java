package com.siarex247.cumplimientoFiscal.ValidarXML;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ValidarXMLSupport extends ActionDB{

	private static final long serialVersionUID = 4880658989776382429L;

	
	private String tipoGrupo;
	private String validarFacturas;

	
	
	
	public String getValidarFacturas() {
		return validarFacturas;
	}

	@StrutsParameter
	public void setValidarFacturas(String validarFacturas) {
		this.validarFacturas = validarFacturas;
	}


	public String getTipoGrupo() {
		return tipoGrupo;
	}

	@StrutsParameter
	public void setTipoGrupo(String tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}

	
}
