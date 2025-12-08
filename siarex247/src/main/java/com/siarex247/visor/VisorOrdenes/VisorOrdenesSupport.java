package com.siarex247.visor.VisorOrdenes;

import org.apache.struts2.interceptor.parameter.StrutsParameter;
import com.siarex247.bd.ActionDB;

/**
 * Support del Visor de Órdenes con filtros DX-like.
 * Incluye alias setters para parámetros que envía el frontend:
 *   - rsValue, ocValue, descValue, sfValue/serieFolioValue, asignarValue, estadoCfdiValue, cpsValue
 *   - monedaValue, reciboValue, estatusPagoValue, estatusSatValue, usoCfdiValue
 *   - fechapagoDateOperator/V1/V2, ultmovDateOperator/V1/V2
 */
public class VisorOrdenesSupport extends ActionDB {
	private static final long serialVersionUID = 2780119050856144863L;

	// =========================
	// Campos base (legacy)
	// =========================
	private String tipoMoneda;
	private String estatusOrden;
	private long   folioOrden;
	private long   folioEmpresa;
	private String descripcion;
	private String monto;
	private String centroCostosProveedor;
	private String numeroCuenta;

	private int    claveProveedor;
	private String idEmpleado;
	private String idCentro;
	private String serRecibido;

	private String facturaPagada;
	private String envioCorreo;
	private String eliminarFactura;
	private String fechaPago;
	private String fechaFactura;

	private String usoCFDI; // legado (en mayúsculas)
	private String rfc;
	private String razonSocial;
	private String uuid;

	private String foliosEliminar;
	private String foliosExportar;

	private String serieFolio;

	private int    claveMotivo;
	private String fechaInicial;
	private String fechaFinal;

	// DataTables
	private int draw;
	private int length;
	private int start;

	// =========================
	// Filtros Visor (DX-like)
	// =========================

	// TEXTOS (valores visibles)
	
	// descripcion ya existe arriba (col 4)
	// razonSocial ya existe arriba (col 2)
	// serieFolio ya existe arriba (col 9)
	private String asignarA;     // col 28
	private String estadoCfdi;   // col 30
	private String cps;          // col 33

	// SELECTS (valores visibles: ALL / opción)
	// tipoMoneda ya existe arriba (col 5)
	private String servicioRecibo; // col 7 (ALL/S/N)
	private String estatusPago;    // col 8 (ALL/A1..A11)
	private String estatusSat;     // col 31 (ALL/S/N)
	private String usoCfdi;        // col 32 (ALL/G03/S01/…)

	// NUMÉRICOS (v1/v2 + operador)
	private String montoV1,    montoV2,    montoOperator;    // col 6
	private String totalV1,    totalV2,    totalOperator;    // col 10
	private String subtotalV1, subtotalV2, subtotalOperator; // col 11
	private String ivaV1,      ivaV2,      ivaOperator;      // col 12
	private String ivaretV1,   ivaretV2,   ivaretOperator;   // col 13
	private String isrretV1,   isrretV2,   isrretOperator;   // col 14
	private String implocV1,   implocV2,   implocOperator;   // col 15
	private String totalncV1,  totalncV2,  totalncOperator;  // col 24
	private String pagotV1,    pagotV2,    pagotOperator;    // col 25
	private String ivaretncV1, ivaretncV2, ivaretncOperator; // col 26

	// FECHAS (d1/d2 + operador)
	private String fechapagoV1, fechapagoV2, fechapagoOperator; // col 27
	private String ultmovV1,    ultmovV2,    ultmovOperator;     // col 29

	// =========================
	// Operadores TEXTO/SELECT
	// =========================
	private String rsOperator;
	//private String ocOperator;
	private String descOperator;
	private String serieFolioOperator;
	private String asignarOperator;
	private String estadoCfdiOperator;
	private String cpsOperator;

	private String monedaOperator;
	private String reciboOperator;
	private String estatusPagoOperator;
	private String estatusSatOperator;
	private String usoCfdiOperator;

	// ======================================
	// Normalizadores de operadores (helpers)
	// ======================================
	private static String tOp(String v){
		return (v == null || v.isBlank()) ? "contains" : v.trim().toLowerCase();
	}
	private static String nOp(String v){
		String x = (v == null ? "" : v.trim().toLowerCase());
		if (x.isEmpty()) return "eq";
		return "between".equals(x) ? "bt" : x;
	}

	// =========
	// Getters
	// =========
	public String getTipoMoneda() { return tipoMoneda; }
	public String getEstatusOrden(){ return estatusOrden; }
	public long   getFolioOrden()  { return folioOrden; }
	public long   getFolioEmpresa(){ return folioEmpresa; }
	public String getDescripcion() { return descripcion; }
	public String getMonto()       { return monto; }
	public String getCentroCostosProveedor(){ return centroCostosProveedor; }
	public String getNumeroCuenta(){ return numeroCuenta; }

	public int    getClaveProveedor(){ return claveProveedor; }
	public String getIdEmpleado()   { return idEmpleado; }
	public String getIdCentro()     { return idCentro; }
	public String getSerRecibido()  { return serRecibido; }

	public String getFacturaPagada(){ return facturaPagada; }
	public String getEnvioCorreo()  { return envioCorreo; }
	public String getEliminarFactura(){ return eliminarFactura; }
	public String getFechaPago()    { return fechaPago; }
	public String getFechaFactura() { return fechaFactura; }

	public String getUsoCFDI()      { return usoCFDI; } // legado
	public String getRfc()          { return rfc; }
	public String getRazonSocial()  { return razonSocial; }
	public String getUuid()         { return uuid; }

	public String getFoliosEliminar(){ return foliosEliminar; }
	public String getFoliosExportar(){ return foliosExportar; }

	public String getSerieFolio()   { return serieFolio; }

	public int    getClaveMotivo()  { return claveMotivo; }
	public String getFechaInicial() { return fechaInicial; }
	public String getFechaFinal()   { return fechaFinal; }

	public int    getDraw()         { return draw; }
	public int    getLength()       { return length; }
	public int    getStart()        { return start; }

	// Filtros DX (valores)
	//public String getOrdenCompra(){ return ordenCompra; }
	public String getAsignarA()   { return asignarA; }
	public String getEstadoCfdi() { return estadoCfdi; }
	public String getCps()        { return cps; }

	public String getServicioRecibo(){ return servicioRecibo; }
	public String getEstatusPago()   { return estatusPago; }
	public String getEstatusSat()    { return estatusSat; }
	public String getUsoCfdi()       { return usoCfdi; }

	// Operadores num/fecha/otros
	public String getMontoV1(){ return montoV1; }      public String getMontoV2(){ return montoV2; }      public String getMontoOperator(){ return montoOperator; }
	public String getTotalV1(){ return totalV1; }      public String getTotalV2(){ return totalV2; }      public String getTotalOperator(){ return totalOperator; }
	public String getSubtotalV1(){ return subtotalV1; }public String getSubtotalV2(){ return subtotalV2; }public String getSubtotalOperator(){ return subtotalOperator; }
	public String getIvaV1(){ return ivaV1; }          public String getIvaV2(){ return ivaV2; }          public String getIvaOperator(){ return ivaOperator; }
	public String getIvaretV1(){ return ivaretV1; }    public String getIvaretV2(){ return ivaretV2; }    public String getIvaretOperator(){ return ivaretOperator; }
	public String getIsrretV1(){ return isrretV1; }    public String getIsrretV2(){ return isrretV2; }    public String getIsrretOperator(){ return isrretOperator; }
	public String getImplocV1(){ return implocV1; }    public String getImplocV2(){ return implocV2; }    public String getImplocOperator(){ return implocOperator; }
	public String getTotalncV1(){ return totalncV1; }  public String getTotalncV2(){ return totalncV2; }  public String getTotalncOperator(){ return totalncOperator; }
	public String getPagotV1(){ return pagotV1; }      public String getPagotV2(){ return pagotV2; }      public String getPagotOperator(){ return pagotOperator; }
	public String getIvaretncV1(){ return ivaretncV1; }public String getIvaretncV2(){ return ivaretncV2; }public String getIvaretncOperator(){ return ivaretncOperator; }

	public String getFechapagoV1(){ return fechapagoV1; } public String getFechapagoV2(){ return fechapagoV2; } public String getFechapagoOperator(){ return fechapagoOperator; }
	public String getUltmovV1(){ return ultmovV1; }       public String getUltmovV2(){ return ultmovV2; }       public String getUltmovOperator(){ return ultmovOperator; }

	public String getRsOperator(){ return rsOperator; }
	//public String getOcOperator(){ return ocOperator; }
	public String getDescOperator(){ return descOperator; }
	public String getSerieFolioOperator(){ return serieFolioOperator; }
	public String getAsignarOperator(){ return asignarOperator; }
	public String getEstadoCfdiOperator(){ return estadoCfdiOperator; }
	public String getCpsOperator(){ return cpsOperator; }

	public String getMonedaOperator(){ return monedaOperator; }
	public String getReciboOperator(){ return reciboOperator; }
	public String getEstatusPagoOperator(){ return estatusPagoOperator; }
	public String getEstatusSatOperator(){ return estatusSatOperator; }
	public String getUsoCfdiOperator(){ return usoCfdiOperator; }

	// =========
	// Setters
	// =========

	// Base/legacy
	@StrutsParameter public void setTipoMoneda(String v){ this.tipoMoneda = v; }
	@StrutsParameter public void setEstatusOrden(String v){ this.estatusOrden = v; }
	@StrutsParameter public void setFolioOrden(long v){ this.folioOrden = v; }
	@StrutsParameter public void setFolioEmpresa(long v){ this.folioEmpresa = v; }
	@StrutsParameter public void setDescripcion(String v){ this.descripcion = v; }
	@StrutsParameter public void setMonto(String v){ this.monto = v; }
	@StrutsParameter public void setCentroCostosProveedor(String v){ this.centroCostosProveedor = v; }
	@StrutsParameter public void setNumeroCuenta(String v){ this.numeroCuenta = v; }

	@StrutsParameter public void setClaveProveedor(int v){ this.claveProveedor = v; }
	@StrutsParameter public void setIdEmpleado(String v){ this.idEmpleado = v; }
	@StrutsParameter public void setIdCentro(String v){ this.idCentro = v; }
	@StrutsParameter public void setSerRecibido(String v){ this.serRecibido = v; }

	@StrutsParameter public void setFacturaPagada(String v){ this.facturaPagada = v; }
	@StrutsParameter public void setEnvioCorreo(String v){ this.envioCorreo = v; }
	@StrutsParameter public void setEliminarFactura(String v){ this.eliminarFactura = v; }
	@StrutsParameter public void setFechaPago(String v){ this.fechaPago = v; }
	@StrutsParameter public void setFechaFactura(String v){ this.fechaFactura = v; }

	@StrutsParameter public void setUsoCFDI(String v){ this.usoCFDI = v; } // legado
	@StrutsParameter public void setRfc(String v){ this.rfc = v; }
	@StrutsParameter public void setRazonSocial(String v){ this.razonSocial = v; }
	@StrutsParameter public void setUuid(String v){ this.uuid = v; }

	@StrutsParameter public void setFoliosEliminar(String v){ this.foliosEliminar = v; }
	@StrutsParameter public void setFoliosExportar(String v){ this.foliosExportar = v; }

	@StrutsParameter public void setSerieFolio(String v){ this.serieFolio = v; }

	@StrutsParameter public void setClaveMotivo(int v){ this.claveMotivo = v; }
	@StrutsParameter public void setFechaInicial(String v){ this.fechaInicial = v; }
	@StrutsParameter public void setFechaFinal(String v){ this.fechaFinal = v; }

	@StrutsParameter public void setDraw(int v){ this.draw = v; }
	@StrutsParameter public void setLength(int v){ this.length = v; }
	@StrutsParameter public void setStart(int v){ this.start = v; }

	// Filtros: valores visibles
	//@StrutsParameter public void setOrdenCompra(String v){ this.ordenCompra = v; }
	@StrutsParameter public void setAsignarA(String v){ this.asignarA = v; }
	@StrutsParameter public void setEstadoCfdi(String v){ this.estadoCfdi = v; }
	@StrutsParameter public void setCps(String v){ this.cps = v; }

	@StrutsParameter public void setServicioRecibo(String v){ this.servicioRecibo = v; }
	@StrutsParameter public void setEstatusPago(String v){ this.estatusPago = v; }
	@StrutsParameter public void setEstatusSat(String v){ this.estatusSat = v; }
	@StrutsParameter public void setUsoCfdi(String v){ this.usoCfdi = v; }

	// Operadores TEXTO/SELECT → default contains
	@StrutsParameter public void setRsOperator(String v){ this.rsOperator = tOp(v); }
//	@StrutsParameter public void setOcOperator(String v){ this.ocOperator = tOp(v); }
	@StrutsParameter public void setDescOperator(String v){ this.descOperator = tOp(v); }
	@StrutsParameter public void setSerieFolioOperator(String v){ this.serieFolioOperator = tOp(v); }
	@StrutsParameter public void setAsignarOperator(String v){ this.asignarOperator = tOp(v); }
	@StrutsParameter public void setEstadoCfdiOperator(String v){ this.estadoCfdiOperator = tOp(v); }
	@StrutsParameter public void setCpsOperator(String v){ this.cpsOperator = tOp(v); }

	@StrutsParameter public void setMonedaOperator(String v){ this.monedaOperator = tOp(v); }
	@StrutsParameter public void setReciboOperator(String v){ this.reciboOperator = tOp(v); }
	@StrutsParameter public void setEstatusPagoOperator(String v){ this.estatusPagoOperator = tOp(v); }
	@StrutsParameter public void setEstatusSatOperator(String v){ this.estatusSatOperator = tOp(v); }
	@StrutsParameter public void setUsoCfdiOperator(String v){ this.usoCfdiOperator = tOp(v); }

	// Operadores NUM/FECHA → default eq/bt
	@StrutsParameter public void setMontoV1(String v){ this.montoV1 = v; }
	@StrutsParameter public void setMontoV2(String v){ this.montoV2 = v; }
	@StrutsParameter public void setMontoOperator(String v){ this.montoOperator = nOp(v); }

	@StrutsParameter public void setTotalV1(String v){ this.totalV1 = v; }
	@StrutsParameter public void setTotalV2(String v){ this.totalV2 = v; }
	@StrutsParameter public void setTotalOperator(String v){ this.totalOperator = nOp(v); }

	@StrutsParameter public void setSubtotalV1(String v){ this.subtotalV1 = v; }
	@StrutsParameter public void setSubtotalV2(String v){ this.subtotalV2 = v; }
	@StrutsParameter public void setSubtotalOperator(String v){ this.subtotalOperator = nOp(v); }

	@StrutsParameter public void setIvaV1(String v){ this.ivaV1 = v; }
	@StrutsParameter public void setIvaV2(String v){ this.ivaV2 = v; }
	@StrutsParameter public void setIvaOperator(String v){ this.ivaOperator = nOp(v); }

	@StrutsParameter public void setIvaretV1(String v){ this.ivaretV1 = v; }
	@StrutsParameter public void setIvaretV2(String v){ this.ivaretV2 = v; }
	@StrutsParameter public void setIvaretOperator(String v){ this.ivaretOperator = nOp(v); }

	@StrutsParameter public void setIsrretV1(String v){ this.isrretV1 = v; }
	@StrutsParameter public void setIsrretV2(String v){ this.isrretV2 = v; }
	@StrutsParameter public void setIsrretOperator(String v){ this.isrretOperator = nOp(v); }

	@StrutsParameter public void setImplocV1(String v){ this.implocV1 = v; }
	@StrutsParameter public void setImplocV2(String v){ this.implocV2 = v; }
	@StrutsParameter public void setImplocOperator(String v){ this.implocOperator = nOp(v); }

	@StrutsParameter public void setTotalncV1(String v){ this.totalncV1 = v; }
	@StrutsParameter public void setTotalncV2(String v){ this.totalncV2 = v; }
	@StrutsParameter public void setTotalncOperator(String v){ this.totalncOperator = nOp(v); }

	@StrutsParameter public void setPagotV1(String v){ this.pagotV1 = v; }
	@StrutsParameter public void setPagotV2(String v){ this.pagotV2 = v; }
	@StrutsParameter public void setPagotOperator(String v){ this.pagotOperator = nOp(v); }

	@StrutsParameter public void setIvaretncV1(String v){ this.ivaretncV1 = v; }
	@StrutsParameter public void setIvaretncV2(String v){ this.ivaretncV2 = v; }
	@StrutsParameter public void setIvaretncOperator(String v){ this.ivaretncOperator = nOp(v); }

	@StrutsParameter public void setFechapagoV1(String v){ this.fechapagoV1 = v; }
	@StrutsParameter public void setFechapagoV2(String v){ this.fechapagoV2 = v; }
	@StrutsParameter public void setFechapagoOperator(String v){ this.fechapagoOperator = nOp(v); }

	@StrutsParameter public void setUltmovV1(String v){ this.ultmovV1 = v; }
	@StrutsParameter public void setUltmovV2(String v){ this.ultmovV2 = v; }
	@StrutsParameter public void setUltmovOperator(String v){ this.ultmovOperator = nOp(v); }

	// ======================================
	// Alias setters (lo que manda el FRONT)
	// ======================================

	// TEXTOS
	@StrutsParameter public void setRsValue(String v){ this.razonSocial = v; }
	//@StrutsParameter public void setOcValue(String v){ this.ordenCompra = v; }
	@StrutsParameter public void setDescValue(String v){ this.descripcion = v; }
	@StrutsParameter public void setSfValue(String v){ this.serieFolio = v; }
	@StrutsParameter public void setSerieFolioValue(String v){ this.serieFolio = v; }
	@StrutsParameter public void setAsignarValue(String v){ this.asignarA = v; }
	@StrutsParameter public void setEstadoCfdiValue(String v){ this.estadoCfdi = v; }
	@StrutsParameter public void setCpsValue(String v){ this.cps = v; }

	// SELECTS
	@StrutsParameter public void setMonedaValue(String v){ this.tipoMoneda = v; }
	@StrutsParameter public void setReciboValue(String v){ this.servicioRecibo = v; }
	@StrutsParameter public void setEstatusPagoValue(String v){ this.estatusPago = v; }
	@StrutsParameter public void setEstatusSatValue(String v){ this.estatusSat = v; }
	@StrutsParameter public void setUsoCfdiValue(String v){ this.usoCfdi = v; }

	// FECHAS (el front manda …Date…)
	@StrutsParameter public void setFechapagoDateV1(String v){ this.fechapagoV1 = v; }
	@StrutsParameter public void setFechapagoDateV2(String v){ this.fechapagoV2 = v; }
	@StrutsParameter public void setFechapagoDateOperator(String v){ this.fechapagoOperator = nOp(v); }

	@StrutsParameter public void setUltmovDateV1(String v){ this.ultmovV1 = v; }
	@StrutsParameter public void setUltmovDateV2(String v){ this.ultmovV2 = v; }
	@StrutsParameter public void setUltmovDateOperator(String v){ this.ultmovOperator = nOp(v); }
	
	// ✅ NUEVO: OC como número
	private String ocV1, ocV2, ocOperator; // eq, ne, lt, le, gt, ge, bt

	public String getOcV1(){ return ocV1; }
	public String getOcV2(){ return ocV2; }
	public String getOcOperator(){ return ocOperator; }

	@StrutsParameter public void setOcV1(String v){ this.ocV1 = v; }
	@StrutsParameter public void setOcV2(String v){ this.ocV2 = v; }
	// "between" -> "bt", vacío -> "eq"
	@StrutsParameter public void setOcOperator(String v){
	  String x = (v==null ? "" : v.trim().toLowerCase());
	  if (x.isEmpty()) x = "eq";
	  if ("between".equals(x)) x = "bt";
	  this.ocOperator = x;
	}

}
