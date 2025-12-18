package com.siarex247.configuraciones.ConfClaveUsoCFDI;

import java.util.ArrayList;

public class ConfClaveUsoCFDIModel {

	
	private ArrayList<ConfClaveUsoCFDIForm> data;
	private String codError;
	private String mensajeError;
	
	private int recordsTotal;
	private int draw;
	private int recordsFiltered;
	
	
	
	public ArrayList<ConfClaveUsoCFDIForm> getData() {
		return data;
	}
	public void setData(ArrayList<ConfClaveUsoCFDIForm> data) {
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
