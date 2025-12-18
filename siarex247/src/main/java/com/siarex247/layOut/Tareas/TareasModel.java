package com.siarex247.layOut.Tareas;

import java.util.ArrayList;

public class TareasModel {

	
	private ArrayList<TareasForm> data;
	private String codError;
	private String mensajeError;
	private int recordsTotal;
	private int draw;
	private int recordsFiltered;
	private int claveRegistro;
	
	public ArrayList<TareasForm> getData() {
		return data;
	}
	public void setData(ArrayList<TareasForm> data) {
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
	public int getClaveRegistro() {
		return claveRegistro;
	}
	public void setClaveRegistro(int claveRegistro) {
		this.claveRegistro = claveRegistro;
	}
	
	
	
}
