package com.siarex247.catalogos.Empleados;

import com.siarex247.seguridad.Usuarios.UsuariosForm;

public class EmpleadosForm {

	
	private int idRegistro = 0;
	private String idEmpleado;
	private String nombreCompleto;
	private String correo;
	private String idSupervisor;
	private String shipTo;
	private String permitirAlta;
	private UsuariosForm usuariosForm;
	

	
	public int getIdRegistro() {
		return idRegistro;
	}
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getIdEmpleado() {
		return idEmpleado;
	}
	public void setIdEmpleado(String idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getIdSupervisor() {
		return idSupervisor;
	}
	public void setIdSupervisor(String idSupervisor) {
		this.idSupervisor = idSupervisor;
	}
	public String getShipTo() {
		return shipTo;
	}
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	public String getPermitirAlta() {
		return permitirAlta;
	}
	public void setPermitirAlta(String permitirAlta) {
		this.permitirAlta = permitirAlta;
	}
	public UsuariosForm getUsuariosForm() {
		return usuariosForm;
	}
	public void setUsuariosForm(UsuariosForm usuariosForm) {
		this.usuariosForm = usuariosForm;
	}
	
	
	
}
