package com.siarex247.cumplimientoFiscal.BovedaEmitidos;

import java.io.File;
import java.util.List;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class BovedaEmitidosSupport extends ActionDB{

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
	
	// ==== TEXTO ====
    private String rfcOperator;      // contains, notContains, startsWith, endsWith, equals, notEquals
    private String razonOperator;    // "
    private String serieOperator;    // "
    private String tipoOperator;     // equals / notEquals / startsWith / ...
    private String uuidOperator;     // "

    // ==== NUMÉRICOS ====
    private String folioOperator;  private String folioV1;   private String folioV2;
    private String totalOperator;  private String totalV1;   private String totalV2;
    private String subOperator;    private String subV1;     private String subV2;
    private String ivaOperator;    private String ivaV1;     private String ivaV2;
    private String ivaRetOperator; private String ivaRetV1;  private String ivaRetV2;
    private String isrOperator;    private String isrV1;     private String isrV2;
    private String impLocOperator; private String impLocV1;  private String impLocV2;

    // ==== FECHA ====
    private String dateOperator;   // eq, ne, lt, gt, le, ge, bt
    private String dateV1;         // YYYY-MM-DD
    private String dateV2;         // YYYY-MM-DD
	
    
	public String getRfc() {
		return rfc;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public String getFolio() {
		return folio;
	}
	public String getSerie() {
		return serie;
	}
	public String getUuid() {
		return uuid;
	}
	public String getFechaInicial() {
		return fechaInicial;
	}
	public String getFechaFinal() {
		return fechaFinal;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public String getIdRegistro() {
		return idRegistro;
	}
	public String getFilterCondicion() {
		return filterCondicion;
	}
	public String getFilterOperador() {
		return filterOperador;
	}
	public String getValorInicial() {
		return valorInicial;
	}
	public String getValorFinal() {
		return valorFinal;
	}
	public int getDraw() {
		return draw;
	}
	public int getLength() {
		return length;
	}
	public int getStart() {
		return start;
	}
	@StrutsParameter
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	@StrutsParameter
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	@StrutsParameter
	public void setFolio(String folio) {
		this.folio = folio;
	}
	@StrutsParameter
	public void setSerie(String serie) {
		this.serie = serie;
	}
	@StrutsParameter
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@StrutsParameter
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	@StrutsParameter
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	@StrutsParameter
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	@StrutsParameter
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	@StrutsParameter
	public void setIdRegistro(String idRegistro) {
		this.idRegistro = idRegistro;
	}
	@StrutsParameter
	public void setFilterCondicion(String filterCondicion) {
		this.filterCondicion = filterCondicion;
	}
	@StrutsParameter
	public void setFilterOperador(String filterOperador) {
		this.filterOperador = filterOperador;
	}
	@StrutsParameter
	public void setValorInicial(String valorInicial) {
		this.valorInicial = valorInicial;
	}
	@StrutsParameter
	public void setValorFinal(String valorFinal) {
		this.valorFinal = valorFinal;
	}
	@StrutsParameter
	public void setDraw(int draw) {
		this.draw = draw;
	}
	@StrutsParameter
	public void setLength(int length) {
		this.length = length;
	}
	@StrutsParameter
	public void setStart(int start) {
		this.start = start;
	}
	
	public String getEmailNotificacion() {
		return emailNotificacion;
	}
	@StrutsParameter
	public void setEmailNotificacion(String emailNotificacion) {
		this.emailNotificacion = emailNotificacion;
	}
	public String getCorreoResponsable() {
		return correoResponsable;
	}
	@StrutsParameter
	public void setCorreoResponsable(String correoResponsable) {
		this.correoResponsable = correoResponsable;
	}
	public String getModoAgrupar() {
		return modoAgrupar;
	}
	@StrutsParameter
	public void setModoAgrupar(String modoAgrupar) {
		this.modoAgrupar = modoAgrupar;
	}
	public String getValidarSAT() {
		return validarSAT;
	}
	@StrutsParameter
	public void setValidarSAT(String validarSAT) {
		this.validarSAT = validarSAT;
	}
	public String getComplementoSAT() {
		return complementoSAT;
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
	public String getIden() {
		return iden;
	}
	@StrutsParameter
	public void setIden(String iden) {
		this.iden = iden;
	}
	
	// ==== GET/SET con defaults ====
    public String getRfcOperator(){ return rfcOperator; }
    @StrutsParameter
    public void setRfcOperator(String v){
        this.rfcOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase();
    }

    public String getRazonOperator(){ return razonOperator; }
    @StrutsParameter
    public void setRazonOperator(String v){
        this.razonOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase();
    }

    public String getSerieOperator(){ return serieOperator; }
    @StrutsParameter
    public void setSerieOperator(String v){
        this.serieOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase();
    }

    public String getTipoOperator(){ return tipoOperator; }
    @StrutsParameter
    public void setTipoOperator(String v){
        this.tipoOperator = (v==null||v.trim().isEmpty()) ? "equals" : v.trim().toLowerCase();
    }

    public String getUuidOperator(){ return uuidOperator; }
    @StrutsParameter
    public void setUuidOperator(String v){
        this.uuidOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase();
    }

    // Numéricos
    public String getFolioOperator(){ return folioOperator; }
    @StrutsParameter public void setFolioOperator(String v){ this.folioOperator = (v==null||v.trim().isEmpty())?"eq":v.trim().toLowerCase(); }
    public String getFolioV1(){ return folioV1; }
    @StrutsParameter public void setFolioV1(String v){ this.folioV1=v; }
    public String getFolioV2(){ return folioV2; }
    @StrutsParameter public void setFolioV2(String v){ this.folioV2=v; }

    public String getTotalOperator(){ return totalOperator; }
    @StrutsParameter public void setTotalOperator(String v){ this.totalOperator = (v==null||v.trim().isEmpty())?"eq":v.trim().toLowerCase(); }
    public String getTotalV1(){ return totalV1; }
    @StrutsParameter public void setTotalV1(String v){ this.totalV1=v; }
    public String getTotalV2(){ return totalV2; }
    @StrutsParameter public void setTotalV2(String v){ this.totalV2=v; }

    public String getSubOperator(){ return subOperator; }
    @StrutsParameter public void setSubOperator(String v){ this.subOperator = (v==null||v.trim().isEmpty())?"eq":v.trim().toLowerCase(); }
    public String getSubV1(){ return subV1; }
    @StrutsParameter public void setSubV1(String v){ this.subV1=v; }
    public String getSubV2(){ return subV2; }
    @StrutsParameter public void setSubV2(String v){ this.subV2=v; }

    public String getIvaOperator(){ return ivaOperator; }
    @StrutsParameter public void setIvaOperator(String v){ this.ivaOperator = (v==null||v.trim().isEmpty())?"eq":v.trim().toLowerCase(); }
    public String getIvaV1(){ return ivaV1; }
    @StrutsParameter public void setIvaV1(String v){ this.ivaV1=v; }
    public String getIvaV2(){ return ivaV2; }
    @StrutsParameter public void setIvaV2(String v){ this.ivaV2=v; }

    public String getIvaRetOperator(){ return ivaRetOperator; }
    @StrutsParameter public void setIvaRetOperator(String v){ this.ivaRetOperator = (v==null||v.trim().isEmpty())?"eq":v.trim().toLowerCase(); }
    public String getIvaRetV1(){ return ivaRetV1; }
    @StrutsParameter public void setIvaRetV1(String v){ this.ivaRetV1=v; }
    public String getIvaRetV2(){ return ivaRetV2; }
    @StrutsParameter public void setIvaRetV2(String v){ this.ivaRetV2=v; }

    public String getIsrOperator(){ return isrOperator; }
    @StrutsParameter public void setIsrOperator(String v){ this.isrOperator = (v==null||v.trim().isEmpty())?"eq":v.trim().toLowerCase(); }
    public String getIsrV1(){ return isrV1; }
    @StrutsParameter public void setIsrV1(String v){ this.isrV1=v; }
    public String getIsrV2(){ return isrV2; }
    @StrutsParameter public void setIsrV2(String v){ this.isrV2=v; }

    public String getImpLocOperator(){ return impLocOperator; }
    @StrutsParameter public void setImpLocOperator(String v){ this.impLocOperator = (v==null||v.trim().isEmpty())?"eq":v.trim().toLowerCase(); }
    public String getImpLocV1(){ return impLocV1; }
    @StrutsParameter public void setImpLocV1(String v){ this.impLocV1=v; }
    public String getImpLocV2(){ return impLocV2; }
    @StrutsParameter public void setImpLocV2(String v){ this.impLocV2=v; }

    // Fecha
    public String getDateOperator(){ return dateOperator; }
    @StrutsParameter public void setDateOperator(String v){ this.dateOperator=(v==null||v.trim().isEmpty())?"eq":v.trim().toLowerCase(); }
    public String getDateV1(){ return dateV1; }
    @StrutsParameter public void setDateV1(String v){ this.dateV1=v; }
    public String getDateV2(){ return dateV2; }
    @StrutsParameter public void setDateV2(String v){ this.dateV2=v; }
    
}
