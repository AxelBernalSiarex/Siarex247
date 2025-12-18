package com.siarex247.configSistema.CorreosRespaldo;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class CorreosRespaldoSupport  extends ActionDB{

	private static final long serialVersionUID = 3798986114348854699L;

	private String correoOrdenes;
	private String correoPagos;
	private String correoRespaldo;
	private String correoComplemento;
	private String CorreoListaNegra1;
	private String CorreoListaNegra2;
	private String CorreoListaNegra3;
	private String CorreoListaNegra4;
	private String CorreoListaNegra5;
	private String correoAvisoUuidBoveda1;
	private String correoAvisoUuidBoveda2;
	public String getCorreoOrdenes() {
		return correoOrdenes;
	}
	@StrutsParameter
	public void setCorreoOrdenes(String correoOrdenes) {
		this.correoOrdenes = correoOrdenes;
	}
	public String getCorreoPagos() {
		return correoPagos;
	}
	@StrutsParameter
	public void setCorreoPagos(String correoPagos) {
		this.correoPagos = correoPagos;
	}
	public String getCorreoRespaldo() {
		return correoRespaldo;
	}
	@StrutsParameter
	public void setCorreoRespaldo(String correoRespaldo) {
		this.correoRespaldo = correoRespaldo;
	}
	public String getCorreoComplemento() {
		return correoComplemento;
	}
	@StrutsParameter
	public void setCorreoComplemento(String correoComplemento) {
		this.correoComplemento = correoComplemento;
	}
	public String getCorreoListaNegra1() {
		return CorreoListaNegra1;
	}
	@StrutsParameter
	public void setCorreoListaNegra1(String correoListaNegra1) {
		CorreoListaNegra1 = correoListaNegra1;
	}
	public String getCorreoListaNegra2() {
		return CorreoListaNegra2;
	}
	@StrutsParameter
	public void setCorreoListaNegra2(String correoListaNegra2) {
		CorreoListaNegra2 = correoListaNegra2;
	}
	public String getCorreoListaNegra3() {
		return CorreoListaNegra3;
	}
	@StrutsParameter
	public void setCorreoListaNegra3(String correoListaNegra3) {
		CorreoListaNegra3 = correoListaNegra3;
	}
	public String getCorreoListaNegra4() {
		return CorreoListaNegra4;
	}
	@StrutsParameter
	public void setCorreoListaNegra4(String correoListaNegra4) {
		CorreoListaNegra4 = correoListaNegra4;
	}
	public String getCorreoListaNegra5() {
		return CorreoListaNegra5;
	}
	@StrutsParameter
	public void setCorreoListaNegra5(String correoListaNegra5) {
		CorreoListaNegra5 = correoListaNegra5;
	}
	public String getCorreoAvisoUuidBoveda1() {
		return correoAvisoUuidBoveda1;
	}
	@StrutsParameter
	public void setCorreoAvisoUuidBoveda1(String correoAvisoUuidBoveda1) {
		this.correoAvisoUuidBoveda1 = correoAvisoUuidBoveda1;
	}
	public String getCorreoAvisoUuidBoveda2() {
		return correoAvisoUuidBoveda2;
	}
	@StrutsParameter
	public void setCorreoAvisoUuidBoveda2(String correoAvisoUuidBoveda2) {
		this.correoAvisoUuidBoveda2 = correoAvisoUuidBoveda2;
	}
	
	
	
}
