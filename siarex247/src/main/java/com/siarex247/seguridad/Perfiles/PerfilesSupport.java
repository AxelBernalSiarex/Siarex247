package com.siarex247.seguridad.Perfiles;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class PerfilesSupport extends ActionDB{
	private static final long serialVersionUID = -7743759894612168310L;

	private int clavePerfil;
	private String nombreCorto;
	private String descripcion;
	private String mostrarProyectos;
	private String tipoPerfil;
	private String estatusRegistro;

	public int getClavePerfil() {
		return clavePerfil;
	}
	@StrutsParameter
	public void setClavePerfil(int clavePerfil) {
		this.clavePerfil = clavePerfil;
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
	
	public String getTipoPerfil() {
		return tipoPerfil;
	}
	@StrutsParameter
	public void setTipoPerfil(String tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}
	public String getEstatusRegistro() {
		return estatusRegistro;
	}
	@StrutsParameter
	public void setEstatusRegistro(String estatusRegistro) {
		this.estatusRegistro = estatusRegistro;
	}
}
