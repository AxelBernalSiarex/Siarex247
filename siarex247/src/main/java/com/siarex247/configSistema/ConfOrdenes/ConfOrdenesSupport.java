package com.siarex247.configSistema.ConfOrdenes;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ConfOrdenesSupport extends ActionDB{

	/*
	private String LLAVE_FINPDF = "";
	private String LLAVE_ORDENES = ""; 
	private String LLAVE_FIN_ORDENES = "";
	private String LLAVE_TOTAL = "";
	private String LLAVE_FIN_TOTAL = "";
	private String LLAVE_VENDOR = "";
	private String LLAVE_FIN_VENDOR = ""; 
	private String LLAVE_MONEDA = "";
	private String LLAVE_FIN_MONEDA = "";
	*/
	private String llaveFinPDF;
	private String llaveOrdenes; 
	private String llaveFinOrdenes;
	private String llaveTotal;
	private String llaveFinTotal;
	private String llaveVendor;
	private String llaveFinVendor; 
	private String llaveMoneda;
	private String llaveFinMondeda;
	
	
	private static final long serialVersionUID = -7172687241905262276L;


	public String getLlaveFinPDF() {
		return llaveFinPDF;
	}

	@StrutsParameter
	public void setLlaveFinPDF(String llaveFinPDF) {
		this.llaveFinPDF = llaveFinPDF;
	}


	public String getLlaveOrdenes() {
		return llaveOrdenes;
	}

	@StrutsParameter
	public void setLlaveOrdenes(String llaveOrdenes) {
		this.llaveOrdenes = llaveOrdenes;
	}


	public String getLlaveFinOrdenes() {
		return llaveFinOrdenes;
	}

	@StrutsParameter
	public void setLlaveFinOrdenes(String llaveFinOrdenes) {
		this.llaveFinOrdenes = llaveFinOrdenes;
	}


	public String getLlaveTotal() {
		return llaveTotal;
	}

	@StrutsParameter
	public void setLlaveTotal(String llaveTotal) {
		this.llaveTotal = llaveTotal;
	}


	public String getLlaveFinTotal() {
		return llaveFinTotal;
	}

	@StrutsParameter
	public void setLlaveFinTotal(String llaveFinTotal) {
		this.llaveFinTotal = llaveFinTotal;
	}


	public String getLlaveVendor() {
		return llaveVendor;
	}

	@StrutsParameter
	public void setLlaveVendor(String llaveVendor) {
		this.llaveVendor = llaveVendor;
	}


	public String getLlaveFinVendor() {
		return llaveFinVendor;
	}

	@StrutsParameter
	public void setLlaveFinVendor(String llaveFinVendor) {
		this.llaveFinVendor = llaveFinVendor;
	}


	public String getLlaveMoneda() {
		return llaveMoneda;
	}

	@StrutsParameter
	public void setLlaveMoneda(String llaveMoneda) {
		this.llaveMoneda = llaveMoneda;
	}


	public String getLlaveFinMondeda() {
		return llaveFinMondeda;
	}

	@StrutsParameter
	public void setLlaveFinMondeda(String llaveFinMondeda) {
		this.llaveFinMondeda = llaveFinMondeda;
	}


	
}
