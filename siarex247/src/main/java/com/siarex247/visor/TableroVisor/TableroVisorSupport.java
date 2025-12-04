package com.siarex247.visor.TableroVisor;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class TableroVisorSupport extends ActionDB {

	
	private static final long serialVersionUID = -9137809493305746609L;

	private String tipoMoneda;

	public String getTipoMoneda() {
		return tipoMoneda;
	}
	@StrutsParameter
	
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	
	
	
	
}
