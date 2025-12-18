package com.siarex247.cumplimientoFiscal.DescargaSAT;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class DescargaSATSupport extends ActionDB {

	private static final long serialVersionUID = 3407124167704166854L;

	// ===== Filtros básicos (cabecera) =====
	private String rfcEmisor;
	private String razonSocialEmisor;
	private String existeBovedaDescarga;
	private String tipoComprobanteDescarga;
	private String fechaInicialDescarga;
	private String fechaFinalDescarga;
	private String uuidDescarga;
	private String estatusCFDI;

	private String rfcReceptor;
	private String razonSocialReceptor;

	// Campos extra de Recibidos (thead)
	private String nombreReceptor; // << NUEVO: Razón Social Receptor
	private String rfcPac;         // << NUEVO: PAC Emisor

	// DataTables
	private int draw;
	private int length;
	private int start;

	// ===== Operadores DX-like (Recibidos) =====
	// --- Texto ---
	private String uuidOperator;    // contains, notContains, startsWith, endsWith, equals, notEquals
	private String rfcEmiOperator;
	private String nomEmiOperator;
	private String rfcRecOperator;
	private String nomRecOperator;
	private String pacOperator;
	private String efectoOperator;
	private String estatusOperator;
	private String bovedaOperator;

	// --- Numérico ---
	private String montoOperator;   // eq, ne, lt, gt, le, ge, between/bt
	private String montoV1;
	private String montoV2;

	// --- Fechas ---
	private String emiDateOperator; private String emiDateV1; private String emiDateV2;
	private String cerDateOperator; private String cerDateV1; private String cerDateV2;
	private String canDateOperator; private String canDateV1; private String canDateV2;

	/* =======================
	 * Getters / Setters base
	 * ======================= */

	public String getRfcEmisor() { return rfcEmisor; }
	@StrutsParameter public void setRfcEmisor(String rfcEmisor) { this.rfcEmisor = rfcEmisor; }

	public String getRazonSocialEmisor() { return razonSocialEmisor; }
	@StrutsParameter public void setRazonSocialEmisor(String razonSocialEmisor) { this.razonSocialEmisor = razonSocialEmisor; }

	public String getExisteBovedaDescarga() { return existeBovedaDescarga; }
	@StrutsParameter public void setExisteBovedaDescarga(String existeBovedaDescarga) { this.existeBovedaDescarga = existeBovedaDescarga; }

	public String getTipoComprobanteDescarga() { return tipoComprobanteDescarga; }
	@StrutsParameter public void setTipoComprobanteDescarga(String tipoComprobanteDescarga) { this.tipoComprobanteDescarga = tipoComprobanteDescarga; }

	public String getFechaInicialDescarga() { return fechaInicialDescarga; }
	@StrutsParameter public void setFechaInicialDescarga(String fechaInicialDescarga) { this.fechaInicialDescarga = fechaInicialDescarga; }

	public String getFechaFinalDescarga() { return fechaFinalDescarga; }
	@StrutsParameter public void setFechaFinalDescarga(String fechaFinalDescarga) { this.fechaFinalDescarga = fechaFinalDescarga; }

	public String getUuidDescarga() { return uuidDescarga; }
	@StrutsParameter public void setUuidDescarga(String uuidDescarga) { this.uuidDescarga = uuidDescarga; }

	public String getEstatusCFDI() { return estatusCFDI; }
	@StrutsParameter public void setEstatusCFDI(String estatusCFDI) { this.estatusCFDI = estatusCFDI; }

	public String getRfcReceptor() { return rfcReceptor; }
	@StrutsParameter public void setRfcReceptor(String rfcReceptor) { this.rfcReceptor = rfcReceptor; }

	public String getRazonSocialReceptor() { return razonSocialReceptor; }
	@StrutsParameter public void setRazonSocialReceptor(String razonSocialReceptor) { this.razonSocialReceptor = razonSocialReceptor; }

	// ===== NUEVOS: necesarios para el Bean y filtros =====
	public String getNombreReceptor() { return nombreReceptor; }
	@StrutsParameter public void setNombreReceptor(String nombreReceptor) { this.nombreReceptor = nombreReceptor; }

	public String getRfcPac() { return rfcPac; }
	@StrutsParameter public void setRfcPac(String rfcPac) { this.rfcPac = rfcPac; }

	// DataTables
	public int getDraw() { return draw; }
	@StrutsParameter public void setDraw(int draw) { this.draw = draw; }

	public int getLength() { return length; }
	@StrutsParameter public void setLength(int length) { this.length = length; }

	public int getStart() { return start; }
	@StrutsParameter public void setStart(int start) { this.start = start; }

	/* =======================
	 * Getters / Setters Filtros DX
	 * ======================= */

	// Texto
	public String getUuidOperator(){ return uuidOperator; }
	public String getRfcEmiOperator(){ return rfcEmiOperator; }
	public String getNomEmiOperator(){ return nomEmiOperator; }
	public String getRfcRecOperator(){ return rfcRecOperator; }
	public String getNomRecOperator(){ return nomRecOperator; }
	public String getPacOperator(){ return pacOperator; }
	public String getEfectoOperator(){ return efectoOperator; }
	public String getEstatusOperator(){ return estatusOperator; }
	public String getBovedaOperator(){ return bovedaOperator; }

	// Numérico
	public String getMontoOperator(){ return montoOperator; }
	public String getMontoV1(){ return montoV1; }
	public String getMontoV2(){ return montoV2; }

	// Fechas
	public String getEmiDateOperator(){ return emiDateOperator; }
	public String getEmiDateV1(){ return emiDateV1; }
	public String getEmiDateV2(){ return emiDateV2; }

	public String getCerDateOperator(){ return cerDateOperator; }
	public String getCerDateV1(){ return cerDateV1; }
	public String getCerDateV2(){ return cerDateV2; }

	public String getCanDateOperator(){ return canDateOperator; }
	public String getCanDateV1(){ return canDateV1; }
	public String getCanDateV2(){ return canDateV2; }

	// Setters con default (texto → contains; num/fecha → eq)
	@StrutsParameter public void setUuidOperator(String v){  this.uuidOperator   = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
	@StrutsParameter public void setRfcEmiOperator(String v){this.rfcEmiOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
	@StrutsParameter public void setNomEmiOperator(String v){this.nomEmiOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
	@StrutsParameter public void setRfcRecOperator(String v){this.rfcRecOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
	@StrutsParameter public void setNomRecOperator(String v){this.nomRecOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
	@StrutsParameter public void setPacOperator(String v){  this.pacOperator    = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
	@StrutsParameter public void setEfectoOperator(String v){this.efectoOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
	@StrutsParameter public void setEstatusOperator(String v){this.estatusOperator=(v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
	@StrutsParameter public void setBovedaOperator(String v){this.bovedaOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }

	@StrutsParameter public void setMontoOperator(String v){ this.montoOperator  = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
	@StrutsParameter public void setMontoV1(String v){ this.montoV1 = v; }
	@StrutsParameter public void setMontoV2(String v){ this.montoV2 = v; }

	@StrutsParameter public void setEmiDateOperator(String v){ this.emiDateOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
	@StrutsParameter public void setEmiDateV1(String v){ this.emiDateV1 = v; }
	@StrutsParameter public void setEmiDateV2(String v){ this.emiDateV2 = v; }

	@StrutsParameter public void setCerDateOperator(String v){ this.cerDateOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
	@StrutsParameter public void setCerDateV1(String v){ this.cerDateV1 = v; }
	@StrutsParameter public void setCerDateV2(String v){ this.cerDateV2 = v; }

	@StrutsParameter public void setCanDateOperator(String v){ this.canDateOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
	@StrutsParameter public void setCanDateV1(String v){ this.canDateV1 = v; }
	@StrutsParameter public void setCanDateV2(String v){ this.canDateV2 = v; }
}
