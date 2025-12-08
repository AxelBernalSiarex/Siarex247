package com.siarex247.estadisticas.RepVerificacion;

import java.util.ArrayList;

public class RepVerificacionModel {

	private ArrayList<RepVerificacionForm> data;
	private String codError;
	private String mensajeError;
	private int recordsTotal;
	private int draw;
	private int recordsFiltered;
	private int total;
	
	
	
	public ArrayList<RepVerificacionForm> getData() {
		return data;
	}
	public void setData(ArrayList<RepVerificacionForm> data) {
		this.data = data;
	}
	public String getCodError() {
		return codError;
	}
	public void setCodError(String codError) {
		this.codError = codError;
	}
	public String getMensajeError() {
		return mensajeError;
	}
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public int getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	
	
	
}
