package com.siarex247.cumplimientoFiscal.Boveda;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class BovedaSupport extends ActionDB{

	private static final long serialVersionUID = -1769806897501645161L;



	private String rfc;
	private String razonSocial;
	private String folio;
	private String serie;
	private String uuid;
	private String fechaInicial;
	private String fechaFinal;
	private String tipoComprobante;
	private String fechaFactura;

	private String idRegistro;
	private String filterCondicion;
	private String filterOperador;
	private String valorInicial;
	private String valorFinal;
	private String emailNotificacion;
	
	private int draw;
	private int length;
	private int start;
	
    
    
	private String correoResponsable;
	private String modoAgrupar;
	private String validarSAT;
	private String complementoSAT;
	private String notaCreditoSAT;
	private String iden;
	
	//Filtros Letra
	private String rfcOperator;
	private String razonOperator;
	private String serieOperator;
	private String tipoOperator;
	private String uuidOperator;
	
	// === NUMERIC FILTERS ===
	// Folio
	private String folioOperator;   // eq, ne, lt, gt, le, ge, between
	private String folioV1;         // primer valor (o único)
	private String folioV2;         // segundo valor si between

	// Total
	private String totalOperator;
	private String totalV1;
	private String totalV2;

	// SubTotal
	private String subOperator;
	private String subV1;
	private String subV2;

	// IVA trasladado
	private String ivaOperator;
	private String ivaV1;
	private String ivaV2;

	// IVA retenido
	private String ivaRetOperator;
	private String ivaRetV1;
	private String ivaRetV2;

	// ISR retenido
	private String isrOperator;
	private String isrV1;
	private String isrV2;

	// Impuestos locales
	private String impLocOperator;
	private String impLocV1;
	private String impLocV2;
	
	// Fecha con operadores
	private String dateOperator; // eq, ne, lt, gt, le, ge, bt
	private String dateV1;       // YYYY-MM-DD
	private String dateV2;       // YYYY-MM-DD

	
    
	public String getIdRegistro() {
		return idRegistro;
	}
	
	@StrutsParameter 
	public void setIdRegistro(String idRegistro) {
		this.idRegistro = idRegistro;
	}
	
	
	public String getRfc() {
		return rfc;
	}
	@StrutsParameter
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getFolio() {
		return folio;
	}
	@StrutsParameter
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getUuid() {
		return uuid;
	}
	@StrutsParameter
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	@StrutsParameter
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	@StrutsParameter
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public int getDraw() {
		return draw;
	}
	@StrutsParameter
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getLength() {
		return length;
	}
	@StrutsParameter
	public void setLength(int length) {
		this.length = length;
	}
	public int getStart() {
		return start;
	}
	@StrutsParameter
	public void setStart(int start) {
		this.start = start;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	@StrutsParameter
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getFilterCondicion() {
		return filterCondicion;
	}
	@StrutsParameter
	public void setFilterCondicion(String filterCondicion) {
		this.filterCondicion = filterCondicion;
	}
	public String getFilterOperador() {
		return filterOperador;
	}
	@StrutsParameter
	public void setFilterOperador(String filterOperador) {
		this.filterOperador = filterOperador;
	}
	public String getValorInicial() {
		return valorInicial;
	}
	public String getValorFinal() {
		return valorFinal;
	}
	@StrutsParameter
	public void setValorInicial(String valorInicial) {
		this.valorInicial = valorInicial;
	}
	@StrutsParameter
	public void setValorFinal(String valorFinal) {
		this.valorFinal = valorFinal;
	}
	public String getSerie() {
		return serie;
	}
	@StrutsParameter
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getEmailNotificacion() {
		return emailNotificacion;
	}
	@StrutsParameter
	public void setEmailNotificacion(String emailNotificacion) {
		this.emailNotificacion = emailNotificacion;
	}
	
	public String getModoAgrupar() {
		return modoAgrupar;
	}
	public String getValidarSAT() {
		return validarSAT;
	}
	public String getComplementoSAT() {
		return complementoSAT;
	}
	
	@StrutsParameter
	public void setModoAgrupar(String modoAgrupar) {
		this.modoAgrupar = modoAgrupar;
	}
	@StrutsParameter
	public void setValidarSAT(String validarSAT) {
		this.validarSAT = validarSAT;
	}
	@StrutsParameter
	public void setComplementoSAT(String complementoSAT) {
		this.complementoSAT = complementoSAT;
	}

	public String getNotaCreditoSAT() {
		return notaCreditoSAT;
	}
	@StrutsParameter
	public void setNotaCreditoSAT(String notaCreditoSAT) {
		this.notaCreditoSAT = notaCreditoSAT;
	}

	public String getCorreoResponsable() {
		return correoResponsable;
	}
	@StrutsParameter
	public void setCorreoResponsable(String correoResponsable) {
		this.correoResponsable = correoResponsable;
	}

	public String getIden() {
		return iden;
	}
	@StrutsParameter
	public void setIden(String iden) {
		this.iden = iden;
	}
	
	public String getRfcOperator() {
	    return rfcOperator;
	}
	@StrutsParameter
	public void setRfcOperator(String rfcOperator) {
	    this.rfcOperator = rfcOperator;
	}
	
	//OPERADORES
	
	// Razón Social
	public String getRazonOperator() { return razonOperator; }
	@StrutsParameter
	public void setRazonOperator(String razonOperator) {
	    this.razonOperator = (razonOperator == null || razonOperator.trim().isEmpty())
	            ? "contains"
	            : razonOperator.trim().toLowerCase();
	}

	// Serie
	public String getSerieOperator() { return serieOperator; }
	@StrutsParameter
	public void setSerieOperator(String serieOperator) {
	    this.serieOperator = (serieOperator == null || serieOperator.trim().isEmpty())
	            ? "contains"
	            : serieOperator.trim().toLowerCase();
	}

	// Tipo de Comprobante
	public String getTipoOperator() { return tipoOperator; }
	@StrutsParameter
	public void setTipoOperator(String tipoOperator) {
	    this.tipoOperator = (tipoOperator == null || tipoOperator.trim().isEmpty())
	            ? "equals"     // <- default recomendado para tipo
	            : tipoOperator.trim().toLowerCase();
	}

	// UUID
	public String getUuidOperator() { return uuidOperator; }
	@StrutsParameter
	public void setUuidOperator(String uuidOperator) {
	    this.uuidOperator = (uuidOperator == null || uuidOperator.trim().isEmpty())
	            ? "contains"
	            : uuidOperator.trim().toLowerCase();
	}
	
	public String getFolioOperator() { return folioOperator; }
	@StrutsParameter
	public void setFolioOperator(String folioOperator) {
	    this.folioOperator = (folioOperator==null||folioOperator.trim().isEmpty())
	            ? "eq" : folioOperator.trim().toLowerCase();
	}
	public String getFolioV1() { return folioV1; }
	@StrutsParameter public void setFolioV1(String folioV1) { this.folioV1 = folioV1; }
	public String getFolioV2() { return folioV2; }
	@StrutsParameter public void setFolioV2(String folioV2) { this.folioV2 = folioV2; }

	// Total
	public String getTotalOperator() { return totalOperator; }
	@StrutsParameter
	public void setTotalOperator(String totalOperator) {
	    this.totalOperator = (totalOperator==null||totalOperator.trim().isEmpty())
	            ? "eq" : totalOperator.trim().toLowerCase();
	}
	public String getTotalV1() { return totalV1; }
	@StrutsParameter public void setTotalV1(String totalV1) { this.totalV1 = totalV1; }
	public String getTotalV2() { return totalV2; }
	@StrutsParameter public void setTotalV2(String totalV2) { this.totalV2 = totalV2; }

	// SubTotal
	public String getSubOperator() { return subOperator; }
	@StrutsParameter
	public void setSubOperator(String subOperator) {
	    this.subOperator = (subOperator==null||subOperator.trim().isEmpty())
	            ? "eq" : subOperator.trim().toLowerCase();
	}
	public String getSubV1() { return subV1; }
	@StrutsParameter public void setSubV1(String subV1) { this.subV1 = subV1; }
	public String getSubV2() { return subV2; }
	@StrutsParameter public void setSubV2(String subV2) { this.subV2 = subV2; }

	// IVA
	public String getIvaOperator() { return ivaOperator; }
	@StrutsParameter
	public void setIvaOperator(String ivaOperator) {
	    this.ivaOperator = (ivaOperator==null||ivaOperator.trim().isEmpty())
	            ? "eq" : ivaOperator.trim().toLowerCase();
	}
	public String getIvaV1() { return ivaV1; }
	@StrutsParameter public void setIvaV1(String ivaV1) { this.ivaV1 = ivaV1; }
	public String getIvaV2() { return ivaV2; }
	@StrutsParameter public void setIvaV2(String ivaV2) { this.ivaV2 = ivaV2; }

	// IVA Ret
	public String getIvaRetOperator() { return ivaRetOperator; }
	@StrutsParameter
	public void setIvaRetOperator(String ivaRetOperator) {
	    this.ivaRetOperator = (ivaRetOperator==null||ivaRetOperator.trim().isEmpty())
	            ? "eq" : ivaRetOperator.trim().toLowerCase();
	}
	public String getIvaRetV1() { return ivaRetV1; }
	@StrutsParameter public void setIvaRetV1(String ivaRetV1) { this.ivaRetV1 = ivaRetV1; }
	public String getIvaRetV2() { return ivaRetV2; }
	@StrutsParameter public void setIvaRetV2(String ivaRetV2) { this.ivaRetV2 = ivaRetV2; }

	// ISR Ret
	public String getIsrOperator() { return isrOperator; }
	@StrutsParameter
	public void setIsrOperator(String isrOperator) {
	    this.isrOperator = (isrOperator==null||isrOperator.trim().isEmpty())
	            ? "eq" : isrOperator.trim().toLowerCase();
	}
	public String getIsrV1() { return isrV1; }
	@StrutsParameter public void setIsrV1(String isrV1) { this.isrV1 = isrV1; }
	public String getIsrV2() { return isrV2; }
	@StrutsParameter public void setIsrV2(String isrV2) { this.isrV2 = isrV2; }

	// Imp. locales
	public String getImpLocOperator() { return impLocOperator; }
	@StrutsParameter
	public void setImpLocOperator(String impLocOperator) {
	    this.impLocOperator = (impLocOperator==null||impLocOperator.trim().isEmpty())
	            ? "eq" : impLocOperator.trim().toLowerCase();
	}
	public String getImpLocV1() { return impLocV1; }
	@StrutsParameter public void setImpLocV1(String impLocV1) { this.impLocV1 = impLocV1; }
	public String getImpLocV2() { return impLocV2; }
	@StrutsParameter public void setImpLocV2(String impLocV2) { this.impLocV2 = impLocV2; }
	
	public String getDateOperator(){ return dateOperator; }
	@StrutsParameter
	public void setDateOperator(String v){
	  this.dateOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase();
	}
	public String getDateV1(){ return dateV1; }
	@StrutsParameter public void setDateV1(String v){ this.dateV1 = v; }
	public String getDateV2(){ return dateV2; }
	@StrutsParameter public void setDateV2(String v){ this.dateV2 = v; }

	
	
		
	
}
