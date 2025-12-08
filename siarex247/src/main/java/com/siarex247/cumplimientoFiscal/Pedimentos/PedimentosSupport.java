package com.siarex247.cumplimientoFiscal.Pedimentos;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class PedimentosSupport extends ActionDB{

	private static final long serialVersionUID = -2858152594871813544L;

	
	
	private String noPedimento;
	private String fechaInicial;
	private String fechaFinal;

    private InputStream inputStream;

    
	
	public String getNoPedimento() {
		return noPedimento;
	}
	@StrutsParameter
	public void setNoPedimento(String noPedimento) {
		this.noPedimento = noPedimento;
	}
	public String getFechaInicial() {
		return fechaInicial;
	}
	@StrutsParameter
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public String getFechaFinal() {
		return fechaFinal;
	}
	@StrutsParameter
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	@StrutsParameter
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
	
	
}
