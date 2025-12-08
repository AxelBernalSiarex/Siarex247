package com.siarex247.configuraciones.EtiquetaComp;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class EtiquetaCompSupport extends ActionDB{

	private static final long serialVersionUID = -3206779658169335228L;

	private String versionXML;
	private String version;
	private int claveRegistro;	
	private String etiqueta;
	private String fechaInicial;
	private String fechaFinal;
	private String datoValida;
	private String subject;
	private String mensajeError;
	private String activada = "N";

	private boolean activadaSW;
	
	public String getVersionXML() {
		return versionXML;
	}
	@StrutsParameter
	public void setVersionXML(String versionXML) {
		this.versionXML = versionXML;
	}

	public int getClaveRegistro() {
		return claveRegistro;
	}
	@StrutsParameter
	public void setClaveRegistro(int claveRegistro) {
		this.claveRegistro = claveRegistro;
	}

	public String getEtiqueta() {
		return etiqueta;
	}
	@StrutsParameter
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}
	@StrutsParameter
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}
	@StrutsParameter
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getDatoValida() {
		return datoValida;
	}
	@StrutsParameter
	public void setDatoValida(String datoValida) {
		this.datoValida = datoValida;
	}

	public String getSubject() {
		return subject;
	}
	@StrutsParameter
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMensajeError() {
		return mensajeError;
	}
	@StrutsParameter
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String getActivada() {
		return activada;
	}
	@StrutsParameter
	public void setActivada(String activada) {
		this.activada = activada;
	}

	public boolean isActivadaSW() {
		return activadaSW;
	}
	@StrutsParameter
	public void setActivadaSW(boolean activadaSW) {
		this.activadaSW = activadaSW;
	}

	public String getVersion() {
		return version;
	}
	@StrutsParameter
	public void setVersion(String version) {
		this.version = version;
	}
	
	

	
	
	
}
