package com.siarex247.cumplimientoFiscal.BovedaEmitidos;

import java.util.ArrayList;

public class BovedaEmitidosModel {

	
	private ArrayList<BovedaEmitidosForm> data;
	private String codError;
	private String mensajeError;
	private int recordsTotal;
	private int draw;
	private int recordsFiltered;

	

	public ArrayList<BovedaEmitidosForm> getData() {
		return data;
	}

	public void setData(ArrayList<BovedaEmitidosForm> data) {
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

	
}
