package com.siarex247.configuraciones.EtiquetasCP;

import java.sql.Date;

public class EtiquetasCPForm {

	
	private int idRegistro = 0;
	private String idCatalogo = "";
	private String idEtiqueta = "";
	private String pathXML = "";
	private String fechaIni = null;
	private String fechaFin = null;
	private String subject = "";
	private String mensaje = "";
	private String validarVacio= "";
	
	
	public int getIdRegistro() {
		return idRegistro;
	}
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getIdCatalogo() {
		return idCatalogo;
	}
	public void setIdCatalogo(String idCatalogo) {
		this.idCatalogo = idCatalogo;
	}
	
	public String getIdEtiqueta() {
		return idEtiqueta;
	}
	public void setIdEtiqueta(String idEtiqueta) {
		this.idEtiqueta = idEtiqueta;
	}
	public String getPathXML() {
		return pathXML;
	}
	public void setPathXML(String pathXML) {
		this.pathXML = pathXML;
	}
	
	
	public String getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(String fechaIni) {
		this.fechaIni = fechaIni;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
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
	public String getValidarVacio() {
		return validarVacio;
	}
	public void setValidarVacio(String validarVacio) {
		this.validarVacio = validarVacio;
	}

	
	
	
	
	
	
	
	
}
