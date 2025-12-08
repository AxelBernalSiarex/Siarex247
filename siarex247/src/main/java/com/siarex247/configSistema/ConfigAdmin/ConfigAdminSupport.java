package com.siarex247.configSistema.ConfigAdmin;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ConfigAdminSupport extends ActionDB{


	private static final long serialVersionUID = 7410300564176107747L;

	private String validaSat;
	private String validarCierreAdmin;
	private String validarComplementoAdmin;
	private String validarContratoConfidencialidad;
	private String banderaOutsourcingProveedor;
	private String banderaLogoToyota;
	private String validaXMLTimbrado;
	
	public String getValidaSat() {
		return validaSat;
	}
	@StrutsParameter
	public void setValidaSat(String validaSat) {
		this.validaSat = validaSat;
	}
	public String getValidarCierreAdmin() {
		return validarCierreAdmin;
	}
	@StrutsParameter
	public void setValidarCierreAdmin(String validarCierreAdmin) {
		this.validarCierreAdmin = validarCierreAdmin;
	}
	public String getValidarComplementoAdmin() {
		return validarComplementoAdmin;
	}
	@StrutsParameter
	public void setValidarComplementoAdmin(String validarComplementoAdmin) {
		this.validarComplementoAdmin = validarComplementoAdmin;
	}
	public String getValidarContratoConfidencialidad() {
		return validarContratoConfidencialidad;
	}
	@StrutsParameter
	public void setValidarContratoConfidencialidad(String validarContratoConfidencialidad) {
		this.validarContratoConfidencialidad = validarContratoConfidencialidad;
	}
	public String getBanderaOutsourcingProveedor() {
		return banderaOutsourcingProveedor;
	}
	@StrutsParameter
	public void setBanderaOutsourcingProveedor(String banderaOutsourcingProveedor) {
		this.banderaOutsourcingProveedor = banderaOutsourcingProveedor;
	}
	public String getBanderaLogoToyota() {
		return banderaLogoToyota;
	}
	@StrutsParameter
	public void setBanderaLogoToyota(String banderaLogoToyota) {
		this.banderaLogoToyota = banderaLogoToyota;
	}
	public String getValidaXMLTimbrado() {
		return validaXMLTimbrado;
	}
	@StrutsParameter
	public void setValidaXMLTimbrado(String validaXMLTimbrado) {
		this.validaXMLTimbrado = validaXMLTimbrado;
	}

	
	
}
