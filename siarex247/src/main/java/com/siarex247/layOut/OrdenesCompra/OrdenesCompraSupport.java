package com.siarex247.layOut.OrdenesCompra;

import java.io.File;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class OrdenesCompraSupport extends  ActionDB{

	private static final long serialVersionUID = -1421171700465360511L;

	private int claveRegistro;
	private String bandProcesar;
	
	private String fechaPago;
	private String tipoReg;
	
	
	public int getClaveRegistro() {
		return claveRegistro;
	}

	@StrutsParameter
	public void setClaveRegistro(int claveRegistro) {
		this.claveRegistro = claveRegistro;
	}

	public String getBandProcesar() {
		return bandProcesar;
	}
	@StrutsParameter
	public void setBandProcesar(String bandProcesar) {
		this.bandProcesar = bandProcesar;
	}

	public String getFechaPago() {
		return fechaPago;
	}
	@StrutsParameter
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}

	public String getTipoReg() {
		return tipoReg;
	}
	@StrutsParameter
	public void setTipoReg(String tipoReg) {
		this.tipoReg = tipoReg;
	}

	

	
}
