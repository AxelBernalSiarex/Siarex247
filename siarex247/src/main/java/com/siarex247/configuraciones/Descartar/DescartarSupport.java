package com.siarex247.configuraciones.Descartar;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class DescartarSupport extends ActionDB{

	private static final long serialVersionUID = -7608435476146482991L;

	private long folioEmpresa;
	private String eliminarBase;

	
	
	public long getFolioEmpresa() {
		return folioEmpresa;
	}
	@StrutsParameter
	public void setFolioEmpresa(long folioEmpresa) {
		this.folioEmpresa = folioEmpresa;
	}

	public String getEliminarBase() {
		return eliminarBase;
	}
	@StrutsParameter
	public void setEliminarBase(String eliminarBase) {
		this.eliminarBase = eliminarBase;
	}
}
