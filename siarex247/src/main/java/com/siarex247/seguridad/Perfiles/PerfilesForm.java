package com.siarex247.seguridad.Perfiles;

public class PerfilesForm {
	private int clavePerfil;
	private String nombreCorto;
	private String descripcion;
	private String mostrarProyectos;
	private String tipoPerfil;
	private String estatusRegistro;

	public int getClavePerfil() {
		return clavePerfil;
	}
	public void setClavePerfil(int clavePerfil) {
		this.clavePerfil = clavePerfil;
	}
	public String getNombreCorto() {
		return nombreCorto;
	}
	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getMostrarProyectos() {
		return mostrarProyectos;
	}
	public void setMostrarProyectos(String mostrarProyectos) {
		this.mostrarProyectos = mostrarProyectos;
	}
	public String getTipoPerfil() {
		return tipoPerfil;
	}
	public void setTipoPerfil(String tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}
	public String getEstatusRegistro() {
		return estatusRegistro;
	}
	public void setEstatusRegistro(String estatusRegistro) {
		this.estatusRegistro = estatusRegistro;
	}
}
