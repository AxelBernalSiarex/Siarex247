package com.siarex247.configSistema.AlertaComplemento;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class AlertaComplementoSupport extends ActionDB{

	private static final long serialVersionUID = -7070783080648372048L;

	private String diaEjecucion;
	private String subject;
	private String texto;
	private String destinatario1;
	private String destinatario2;
	private String destinatario3;
	private String destinatario4;
	private String destinatario5;
	private String adjuntar;
	private String activar;
	private String fechaPago;
	private String diasProcesar;
	
	public String getDiaEjecucion() {
		return diaEjecucion;
	}
	public String getSubject() {
		return subject;
	}
	 
	public String getDestinatario1() {
		return destinatario1;
	}
	public String getDestinatario2() {
		return destinatario2;
	}
	public String getDestinatario3() {
		return destinatario3;
	}
	public String getDestinatario4() {
		return destinatario4;
	}
	public String getDestinatario5() {
		return destinatario5;
	}
	public String getAdjuntar() {
		return adjuntar;
	}
	public String getActivar() {
		return activar;
	}
	@StrutsParameter
	public void setDiaEjecucion(String diaEjecucion) {
		this.diaEjecucion = diaEjecucion;
	}
	@StrutsParameter
	public void setSubject(String subject) {
		this.subject = subject;
	}
	 
	@StrutsParameter
	public void setDestinatario1(String destinatario1) {
		this.destinatario1 = destinatario1;
	}
	@StrutsParameter
	public void setDestinatario2(String destinatario2) {
		this.destinatario2 = destinatario2;
	}
	@StrutsParameter
	public void setDestinatario3(String destinatario3) {
		this.destinatario3 = destinatario3;
	}
	@StrutsParameter
	public void setDestinatario4(String destinatario4) {
		this.destinatario4 = destinatario4;
	}
	@StrutsParameter
	public void setDestinatario5(String destinatario5) {
		this.destinatario5 = destinatario5;
	}
	@StrutsParameter
	public void setAdjuntar(String adjuntar) {
		this.adjuntar = adjuntar;
	}
	@StrutsParameter
	public void setActivar(String activar) {
		this.activar = activar;
	}
	public String getTexto() {
		return texto;
	}
	@StrutsParameter
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public String getDiasProcesar() {
		return diasProcesar;
	}
	@StrutsParameter
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	@StrutsParameter
	public void setDiasProcesar(String diasProcesar) {
		this.diasProcesar = diasProcesar;
	}
	
	
}
