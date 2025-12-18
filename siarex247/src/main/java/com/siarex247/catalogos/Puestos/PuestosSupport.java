package com.siarex247.catalogos.Puestos;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class PuestosSupport   extends ActionDB{

	private static final long serialVersionUID = -4469323191939062936L;

	private int idPuesto = 0;
	private String nombreCorto;
	private String descripcion;

	public int getIdPuesto() {
		return idPuesto;
	}
	@StrutsParameter
	public void setIdPuesto(int idPuesto) {
		this.idPuesto = idPuesto;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}
	@StrutsParameter
	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	public String getDescripcion() {
		return descripcion;
	}
	@StrutsParameter
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	
	
	
	
	
}
