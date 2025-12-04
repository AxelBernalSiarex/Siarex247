package com.siarex247.cumplimientoFiscal.BovedaNomina;

import java.io.File;
import java.util.List;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class BovedaNominaSupport extends ActionDB{

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
	
	  // ===== Operadores (coinciden con los filtros del JSP) =====
    // --- Texto ---
    private String rfcOperator;       // contains, notContains, startsWith, endsWith, equals, notEquals
    private String razonOperator;     // idem
    private String serieOperator;     // idem
    private String uuidOperator;      // idem

    // --- Numéricos (op: eq, ne, lt, gt, le, ge, between/bt) + valores 1/2
    private String folioOperator;  private String folioV1;  private String folioV2;
    private String totalOperator;  private String totalV1;  private String totalV2;
    private String subOperator;    private String subV1;    private String subV2;
    private String descOperator;   private String descV1;   private String descV2;   // Descuento
    private String percOperator;   private String percV1;   private String percV2;   // Percepciones
    private String dedOperator;    private String dedV1;    private String dedV2;    // Deducciones

    // --- Fecha ---
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
	
	 // ===== Operadores: Get/Set con defaults =====
    // Texto
    public String getRfcOperator()   { return rfcOperator; }
    public String getRazonOperator() { return razonOperator; }
    public String getSerieOperator() { return serieOperator; }
    public String getUuidOperator()  { return uuidOperator; }

    @StrutsParameter public void setRfcOperator(String v){   this.rfcOperator   = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
    @StrutsParameter public void setRazonOperator(String v){ this.razonOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
    @StrutsParameter public void setSerieOperator(String v){ this.serieOperator = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }
    @StrutsParameter public void setUuidOperator(String v){  this.uuidOperator  = (v==null||v.trim().isEmpty()) ? "contains" : v.trim().toLowerCase(); }

    // Numéricos
    public String getFolioOperator(){ return folioOperator; }
    public String getFolioV1(){ return folioV1; }
    public String getFolioV2(){ return folioV2; }
    @StrutsParameter public void setFolioOperator(String v){ this.folioOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
    @StrutsParameter public void setFolioV1(String v){ this.folioV1 = v; }
    @StrutsParameter public void setFolioV2(String v){ this.folioV2 = v; }

    public String getTotalOperator(){ return totalOperator; }
    public String getTotalV1(){ return totalV1; }
    public String getTotalV2(){ return totalV2; }
    @StrutsParameter public void setTotalOperator(String v){ this.totalOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
    @StrutsParameter public void setTotalV1(String v){ this.totalV1 = v; }
    @StrutsParameter public void setTotalV2(String v){ this.totalV2 = v; }

    public String getSubOperator(){ return subOperator; }
    public String getSubV1(){ return subV1; }
    public String getSubV2(){ return subV2; }
    @StrutsParameter public void setSubOperator(String v){ this.subOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
    @StrutsParameter public void setSubV1(String v){ this.subV1 = v; }
    @StrutsParameter public void setSubV2(String v){ this.subV2 = v; }

    public String getDescOperator(){ return descOperator; }
    public String getDescV1(){ return descV1; }
    public String getDescV2(){ return descV2; }
    @StrutsParameter public void setDescOperator(String v){ this.descOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
    @StrutsParameter public void setDescV1(String v){ this.descV1 = v; }
    @StrutsParameter public void setDescV2(String v){ this.descV2 = v; }

    public String getPercOperator(){ return percOperator; }
    public String getPercV1(){ return percV1; }
    public String getPercV2(){ return percV2; }
    @StrutsParameter public void setPercOperator(String v){ this.percOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
    @StrutsParameter public void setPercV1(String v){ this.percV1 = v; }
    @StrutsParameter public void setPercV2(String v){ this.percV2 = v; }

    public String getDedOperator(){ return dedOperator; }
    public String getDedV1(){ return dedV1; }
    public String getDedV2(){ return dedV2; }
    @StrutsParameter public void setDedOperator(String v){ this.dedOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
    @StrutsParameter public void setDedV1(String v){ this.dedV1 = v; }
    @StrutsParameter public void setDedV2(String v){ this.dedV2 = v; }

    // Fecha
    public String getDateOperator(){ return dateOperator; }
    public String getDateV1(){ return dateV1; }
    public String getDateV2(){ return dateV2; }
    @StrutsParameter public void setDateOperator(String v){ this.dateOperator = (v==null||v.trim().isEmpty()) ? "eq" : v.trim().toLowerCase(); }
    @StrutsParameter public void setDateV1(String v){ this.dateV1 = v; }
    @StrutsParameter public void setDateV2(String v){ this.dateV2 = v; }
	
}
