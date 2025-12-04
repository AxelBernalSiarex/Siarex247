package com.siarex247.catalogos.sat.Catalogos;

public class CatalogosForm {

	private int idRegistro = 0;
	private String clave;
	private String descripcion;
	private String codigoPostal;
	private String estado;
	
	public int getIdRegistro() {
		return idRegistro;
	}
	public String getClave() {
		return clave;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public String getEstado() {
		return estado;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
