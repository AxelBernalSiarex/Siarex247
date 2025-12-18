package com.siarex247.visor.TableroVisor;

import java.util.ArrayList;

public class TableroVisorModel {

	
	private ArrayList<TableroVisorForm> data;
	private String codError;
	private String mensajeError;
	private int recordsTotal;
	private int draw;
	private int recordsFiltered;
	
	
	
	public ArrayList<TableroVisorForm> getData() {
		return data;
	}
	public void setData(ArrayList<TableroVisorForm> data) {
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
