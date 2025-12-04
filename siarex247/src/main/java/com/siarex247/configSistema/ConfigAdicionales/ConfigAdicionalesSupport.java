package com.siarex247.configSistema.ConfigAdicionales;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ConfigAdicionalesSupport extends ActionDB{


	private static final long serialVersionUID = 7410300564176107747L;

	
	private String rechazarComple;
	private String bloquearProveedores;
	private String bandValidFechasComple;
	private String notifCorreoOrden;
	private String validarNotas;
	private String predefinirValorSerie;
	private String permitirAccesoGenerador;
	private String bloquearImss;
	private String bloquearSat;
	private String rfcReceptor;
	private String validarCP;
	private String permitirCartaPorte;
	private String serieFaltante;
	private String diaApartirComple;
	private String fechaApartirComple;
	private String valorSerieAmericanas;
	private String valorRfcReceptor;
	private String labelLayOutMultiple;
	private String labelLayOutOrden;
	
	
	public String getRechazarComple() {
		return rechazarComple;
	}
	@StrutsParameter
	public void setRechazarComple(String rechazarComple) {
		this.rechazarComple = rechazarComple;
	}
	public String getBloquearProveedores() {
		return bloquearProveedores;
	}
	@StrutsParameter
	public void setBloquearProveedores(String bloquearProveedores) {
		this.bloquearProveedores = bloquearProveedores;
	}
	public String getBandValidFechasComple() {
		return bandValidFechasComple;
	}
	@StrutsParameter
	public void setBandValidFechasComple(String bandValidFechasComple) {
		this.bandValidFechasComple = bandValidFechasComple;
	}
	public String getNotifCorreoOrden() {
		return notifCorreoOrden;
	}
	@StrutsParameter
	public void setNotifCorreoOrden(String notifCorreoOrden) {
		this.notifCorreoOrden = notifCorreoOrden;
	}
	public String getValidarNotas() {
		return validarNotas;
	}
	@StrutsParameter
	public void setValidarNotas(String validarNotas) {
		this.validarNotas = validarNotas;
	}
	public String getPredefinirValorSerie() {
		return predefinirValorSerie;
	}
	@StrutsParameter
	public void setPredefinirValorSerie(String predefinirValorSerie) {
		this.predefinirValorSerie = predefinirValorSerie;
	}
	public String getPermitirAccesoGenerador() {
		return permitirAccesoGenerador;
	}
	@StrutsParameter
	public void setPermitirAccesoGenerador(String permitirAccesoGenerador) {
		this.permitirAccesoGenerador = permitirAccesoGenerador;
	}
	public String getBloquearImss() {
		return bloquearImss;
	}
	@StrutsParameter
	public void setBloquearImss(String bloquearImss) {
		this.bloquearImss = bloquearImss;
	}
	public String getBloquearSat() {
		return bloquearSat;
	}
	@StrutsParameter
	public void setBloquearSat(String bloquearSat) {
		this.bloquearSat = bloquearSat;
	}
	public String getRfcReceptor() {
		return rfcReceptor;
	}
	@StrutsParameter
	public void setRfcReceptor(String rfcReceptor) {
		this.rfcReceptor = rfcReceptor;
	}
	public String getValidarCP() {
		return validarCP;
	}
	@StrutsParameter
	public void setValidarCP(String validarCP) {
		this.validarCP = validarCP;
	}
	public String getPermitirCartaPorte() {
		return permitirCartaPorte;
	}
	@StrutsParameter
	public void setPermitirCartaPorte(String permitirCartaPorte) {
		this.permitirCartaPorte = permitirCartaPorte;
	}
	public String getSerieFaltante() {
		return serieFaltante;
	}
	@StrutsParameter
	public void setSerieFaltante(String serieFaltante) {
		this.serieFaltante = serieFaltante;
	}
	public String getDiaApartirComple() {
		return diaApartirComple;
	}
	@StrutsParameter
	public void setDiaApartirComple(String diaApartirComple) {
		this.diaApartirComple = diaApartirComple;
	}
	public String getFechaApartirComple() {
		return fechaApartirComple;
	}
	@StrutsParameter
	public void setFechaApartirComple(String fechaApartirComple) {
		this.fechaApartirComple = fechaApartirComple;
	}
	public String getValorSerieAmericanas() {
		return valorSerieAmericanas;
	}
	@StrutsParameter
	public void setValorSerieAmericanas(String valorSerieAmericanas) {
		this.valorSerieAmericanas = valorSerieAmericanas;
	}
	public String getValorRfcReceptor() {
		return valorRfcReceptor;
	}
	@StrutsParameter
	public void setValorRfcReceptor(String valorRfcReceptor) {
		this.valorRfcReceptor = valorRfcReceptor;
	}
	public String getLabelLayOutMultiple() {
		return labelLayOutMultiple;
	}
	@StrutsParameter
	public void setLabelLayOutMultiple(String labelLayOutMultiple) {
		this.labelLayOutMultiple = labelLayOutMultiple;
	}
	public String getLabelLayOutOrden() {
		return labelLayOutOrden;
	}
	@StrutsParameter
	public void setLabelLayOutOrden(String labelLayOutOrden) {
		this.labelLayOutOrden = labelLayOutOrden;
	}
	
}
