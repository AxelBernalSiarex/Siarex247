package com.siarex247.estadisticas.RepValidacion;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class RepValidacionSupport extends ActionDB {

	private static final long serialVersionUID = -4393093253121303900L;

	private String descripcion;
	private String validarFactura;
	private String validarComplemento;
	private String validarNota;
	private String estatus;
	private String fecIni;
	private String fecFin;
	private int rows = 0;
	private int page = 0;
	private int idBitacora = 0;
	private boolean bandEliminar = false;
	private String tipoFecha;
	
	
	
	public String getDescripcion() {
		return descripcion;
	}
	@StrutsParameter
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getValidarFactura() {
		return validarFactura;
	}
	@StrutsParameter
	public void setValidarFactura(String validarFactura) {
		this.validarFactura = validarFactura;
	}
	public String getValidarComplemento() {
		return validarComplemento;
	}
	@StrutsParameter
	public void setValidarComplemento(String validarComplemento) {
		this.validarComplemento = validarComplemento;
	}
	public String getValidarNota() {
		return validarNota;
	}
	@StrutsParameter
	public void setValidarNota(String validarNota) {
		this.validarNota = validarNota;
	}
	public String getEstatus() {
		return estatus;
	}
	@StrutsParameter
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public int getRows() {
		return rows;
	}
	@StrutsParameter
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	@StrutsParameter
	public void setPage(int page) {
		this.page = page;
	}
	public String getFecIni() {
		return fecIni;
	}
	@StrutsParameter
	public void setFecIni(String fecIni) {
		this.fecIni = fecIni;
	}
	public String getFecFin() {
		return fecFin;
	}
	@StrutsParameter
	public void setFecFin(String fecFin) {
		this.fecFin = fecFin;
	}
	public int getIdBitacora() {
		return idBitacora;
	}
	@StrutsParameter
	public void setIdBitacora(int idBitacora) {
		this.idBitacora = idBitacora;
	}
	public boolean isBandEliminar() {
		return bandEliminar;
	}
	@StrutsParameter
	public void setBandEliminar(boolean bandEliminar) {
		this.bandEliminar = bandEliminar;
	}
	public String getTipoFecha() {
		return tipoFecha;
	}
	@StrutsParameter
	public void setTipoFecha(String tipoFecha) {
		this.tipoFecha = tipoFecha;
	}

	
}
