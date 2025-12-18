package com.siarex247.catalogos.Empleados;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class EmpleadosSupport   extends ActionDB{

	private static final long serialVersionUID = -4469323191939062936L;

	private int idRegistro;
	private String idEmpleado;
	private String nombreCompleto;
	private String correo;
	private String idSupervisor;
	private String shipTo;
	private String permitirAlta;
	private boolean bandAcceso;
	private String usuarioAcceso;
	private int idPerfil;
	
	
	public int getIdRegistro() {
		return idRegistro;
	}
	@StrutsParameter
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getIdEmpleado() {
		return idEmpleado;
	}
	@StrutsParameter
	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	@StrutsParameter
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getCorreo() {
		return correo;
	}
	@StrutsParameter
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getIdSupervisor() {
		return idSupervisor;
	}
	@StrutsParameter
	public void setIdSupervisor(String idSupervisor) {
		this.idSupervisor = idSupervisor;
	}
	public String getShipTo() {
		return shipTo;
	}
	@StrutsParameter
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	public String getPermitirAlta() {
		return permitirAlta;
	}
	@StrutsParameter
	public void setPermitirAlta(String permitirAlta) {
		this.permitirAlta = permitirAlta;
	}
	public boolean isBandAcceso() {
		return bandAcceso;
	}
	@StrutsParameter
	public void setBandAcceso(boolean bandAcceso) {
		this.bandAcceso = bandAcceso;
	}
	public String getUsuarioAcceso() {
		return usuarioAcceso;
	}
	@StrutsParameter
	public void setUsuarioAcceso(String usuarioAcceso) {
		this.usuarioAcceso = usuarioAcceso;
	}
	public int getIdPerfil() {
		return idPerfil;
	}
	@StrutsParameter
	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}


	
	
	
}
