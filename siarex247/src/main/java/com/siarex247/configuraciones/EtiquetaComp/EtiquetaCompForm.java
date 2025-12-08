package com.siarex247.configuraciones.EtiquetaComp;

import java.sql.Date;

public class EtiquetaCompForm {

	
	private int claveRegistro = 0;
	private String etiqueta = "";
	private Date fechaIni = null;
	private Date fechaFin = null;
	private String subject = "";
	private String mensaje = "";
	
	private String rutaXML = "";
	private String propiedad = "";
	private String tipoEtiqueta = "";
	
	
	public int getClaveRegistro() {
		return claveRegistro;
	}
	public void setClaveRegistro(int claveRegistro) {
		this.claveRegistro = claveRegistro;
	}
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getRutaXML() {
		return rutaXML;
	}
	public void setRutaXML(String rutaXML) {
		this.rutaXML = rutaXML;
	}
	public String getPropiedad() {
		return propiedad;
	}
	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}
	public String getTipoEtiqueta() {
		return tipoEtiqueta;
	}
	public void setTipoEtiqueta(String tipoEtiqueta) {
		this.tipoEtiqueta = tipoEtiqueta;
	}
	
	
	
	
}
