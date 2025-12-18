package com.siarex247.cumplimientoFiscal.ListaNegra;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ListaNegraSupport extends ActionDB{

	private static final long serialVersionUID = 8456715465327857674L;

	
	private String diaMes = "";
	private String razonSocial = "";
	private String rfcListaNegra = "";
	private String idSupuesto = "";
	private String idNombreArticulo = "";
	private String idEstatus = "";
	private int idRegistro = 0;
	private int anioListaNegra = 0;
	
	private String existeListaNegra;
	
	private int draw;
	private int length;
	private int start;
	
	
	
	public String getDiaMes() {
		return diaMes;
	}
	@StrutsParameter
	public void setDiaMes(String diaMes) {
		this.diaMes = diaMes;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	@StrutsParameter
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getRfcListaNegra() {
		return rfcListaNegra;
	}
	@StrutsParameter
	public void setRfcListaNegra(String rfcListaNegra) {
		this.rfcListaNegra = rfcListaNegra;
	}
	public String getIdSupuesto() {
		return idSupuesto;
	}
	@StrutsParameter
	public void setIdSupuesto(String idSupuesto) {
		this.idSupuesto = idSupuesto;
	}
	public String getIdNombreArticulo() {
		return idNombreArticulo;
	}
	@StrutsParameter
	public void setIdNombreArticulo(String idNombreArticulo) {
		this.idNombreArticulo = idNombreArticulo;
	}
	public String getIdEstatus() {
		return idEstatus;
	}
	@StrutsParameter
	public void setIdEstatus(String idEstatus) {
		this.idEstatus = idEstatus;
	}
	public int getIdRegistro() {
		return idRegistro;
	}
	@StrutsParameter
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public int getAnioListaNegra() {
		return anioListaNegra;
	}
	@StrutsParameter
	public void setAnioListaNegra(int anioListaNegra) {
		this.anioListaNegra = anioListaNegra;
	}
	public int getDraw() {
		return draw;
	}
	@StrutsParameter
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public int getLength() {
		return length;
	}
	@StrutsParameter
	public void setLength(int length) {
		this.length = length;
	}
	public int getStart() {
		return start;
	}
	@StrutsParameter
	public void setStart(int start) {
		this.start = start;
	}
	public String getExisteListaNegra() {
		return existeListaNegra;
	}
	
	@StrutsParameter
	public void setExisteListaNegra(String existeListaNegra) {
		this.existeListaNegra = existeListaNegra;
	}
	
	
	
	
}
