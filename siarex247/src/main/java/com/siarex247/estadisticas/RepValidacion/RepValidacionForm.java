package com.siarex247.estadisticas.RepValidacion;

public class RepValidacionForm {
	private int idRegistro;
	private double folioEmpresa;
	private String razonSocial;
	private String etiqueta;
	private String valorXML;
	private String estatus;
	private String resultado;
	private String fechaTrans;
	
	public int getIdRegistro() {
		return idRegistro;
	}
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public double getFolioEmpresa() {
		return folioEmpresa;
	}
	public void setFolioEmpresa(double folioEmpresa) {
		this.folioEmpresa = folioEmpresa;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public String getValorXML() {
		return valorXML;
	}
	public void setValorXML(String valorXML) {
		this.valorXML = valorXML;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getFechaTrans() {
		return fechaTrans;
	}
	public void setFechaTrans(String fechaTrans) {
		this.fechaTrans = fechaTrans;
	}	
}
