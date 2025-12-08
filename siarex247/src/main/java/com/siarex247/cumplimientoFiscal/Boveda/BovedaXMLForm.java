package com.siarex247.cumplimientoFiscal.Boveda;

public class BovedaXMLForm {
	int claveProdServ = 0;
	private int valorUnitario = 0;
	private String claveUnidad = ""; 
	private String descripcion = "";
	private String noIdentificacion = "";

	public int getClaveProdServ() {
		return claveProdServ;
	}
	public void setClaveProdServ(int claveProdServ) {
		this.claveProdServ = claveProdServ;
	}
	public int getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(int valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public String getClaveUnidad() {
		return claveUnidad;
	}
	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNoIdentificacion() {
		return noIdentificacion;
	}
	public void setNoIdentificacion(String noIdentificacion) {
		this.noIdentificacion = noIdentificacion;
	}
}
