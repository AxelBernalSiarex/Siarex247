package com.siarex247.visor.VisorOrdenes;

import java.util.ArrayList;

public class VisorOrdenesModel {

	
	private ArrayList<VisorOrdenesForm> data;
	private String codError;
	private String mensajeError;
	
	private int recordsTotal;
	private int draw;
	private int recordsFiltered;
	
	private String mensajeValidacion;
	private String uuidValida;
	private String razonSocial;
	private String estadoSAT;
	private String estatusSAT;
	private String claveProveedor;
	
    
	public ArrayList<VisorOrdenesForm> getData() {
		return data;
	}
	public void setData(ArrayList<VisorOrdenesForm> data) {
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
	public String getMensajeValidacion() {
		return mensajeValidacion;
	}
	public void setMensajeValidacion(String mensajeValidacion) {
		this.mensajeValidacion = mensajeValidacion;
	}
	public String getUuidValida() {
		return uuidValida;
	}
	public void setUuidValida(String uuidValida) {
		this.uuidValida = uuidValida;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getEstadoSAT() {
		return estadoSAT;
	}
	public void setEstadoSAT(String estadoSAT) {
		this.estadoSAT = estadoSAT;
	}
	public String getEstatusSAT() {
		return estatusSAT;
	}
	public void setEstatusSAT(String estatusSAT) {
		this.estatusSAT = estatusSAT;
	}
	public String getClaveProveedor() {
		return claveProveedor;
	}
	public void setClaveProveedor(String claveProveedor) {
		this.claveProveedor = claveProveedor;
	}
	


	
	
	
}
