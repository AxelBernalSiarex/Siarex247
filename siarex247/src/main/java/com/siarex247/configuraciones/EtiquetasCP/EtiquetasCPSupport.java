package com.siarex247.configuraciones.EtiquetasCP;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class EtiquetasCPSupport extends ActionDB{

	private static final long serialVersionUID = 3652690909729576423L;

	private int claveRegistro;
	private String etiqueta;
	private String fechaInicial;
	private String fechaFinal;
	private String datoValida;
	private String subject;
	private String mensajeError;
	private String version;
	
	private boolean activadaSW;
	private boolean valEtiquetaSW;
	private String idCatalogo;
	
	
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

	public String getVersion() {
		return version;
	}
	@StrutsParameter
	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isActivadaSW() {
		return activadaSW;
	}
	@StrutsParameter
	public void setActivadaSW(boolean activadaSW) {
		this.activadaSW = activadaSW;
	}

	public boolean isValEtiquetaSW() {
		return valEtiquetaSW;
	}
	@StrutsParameter
	public void setValEtiquetaSW(boolean valEtiquetaSW) {
		this.valEtiquetaSW = valEtiquetaSW;
	}

	public String getIdCatalogo() {
		return idCatalogo;
	}
	@StrutsParameter
	public void setIdCatalogo(String idCatalogo) {
		this.idCatalogo = idCatalogo;
	}
	

	
}
