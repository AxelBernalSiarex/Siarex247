package com.siarex247.cumplimientoFiscal.ConciliacionBoveda;

import java.util.ArrayList;

public class ConciliacionBovedaModel {

	
	private ArrayList<ConciliacionBovedaForm> data;
	private String codError;
	private String mensajeError;
	private int recordsTotal;
	private int draw;
	private int recordsFiltered;
	
	public ArrayList<ConciliacionBovedaForm> getData() {
		return data;
	}
	public String getCodError() {
		return codError;
	}
	public String getMensajeError() {
		return mensajeError;
	}
	public int getRecordsTotal() {
		return recordsTotal;
	}
	public int getDraw() {
		return draw;
	}
	public int getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setData(ArrayList<ConciliacionBovedaForm> data) {
		this.data = data;
	}
	public void setCodError(String codError) {
		this.codError = codError;
	}
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	
	
	
}
