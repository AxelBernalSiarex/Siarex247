package com.siarex247.prodigia.data;

import java.util.ArrayList;

public class ConsultaRfcLco {
	private String codigo;
	private String consultaOk;
	private String rfc;
	private ArrayList<LcoItem> lcoItems;
	
	public ConsultaRfcLco() {
		this.lcoItems = new ArrayList<LcoItem>();
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getConsultaOk() {
		return consultaOk;
	}
	public void setConsultaOk(String consultaOk) {
		this.consultaOk = consultaOk;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public ArrayList<LcoItem> getLcoItems() {
		return lcoItems;
	}
	public void setLcoItems(ArrayList<LcoItem> lcoItems) {
		this.lcoItems = lcoItems;
	}
	
	
}
