package com.siarex247.configuraciones.ConfClaveUsoCFDI;

import java.io.File;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ConfClaveUsoCFDISupport extends ActionDB{

	private static final long serialVersionUID = 8141247573644278842L;

	private int idRegistro;
	private String rfc;
	private String usoCFDI;
	private String claveProducto;
	private String razonSocial;
	private String TIPO_VALIDACION;
	
	
	public int getIdRegistro() {
		return idRegistro;
	}
	@StrutsParameter
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getRfc() {
		return rfc;
	}
	@StrutsParameter
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getUsoCFDI() {
		return usoCFDI;
	}
	@StrutsParameter
	public void setUsoCFDI(String usoCFDI) {
		this.usoCFDI = usoCFDI;
	}
	public String getClaveProducto() {
		return claveProducto;
	}
	@StrutsParameter
	public void setClaveProducto(String claveProducto) {
		this.claveProducto = claveProducto;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	@StrutsParameter
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getTIPO_VALIDACION() {
		return TIPO_VALIDACION;
	}
	@StrutsParameter
	public void setTIPO_VALIDACION(String tIPO_VALIDACION) {
		TIPO_VALIDACION = tIPO_VALIDACION;
	}

	
	
	
}
