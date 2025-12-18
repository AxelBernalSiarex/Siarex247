package com.siarex247.seguridad.Usuarios;


import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class UsuariosSupport extends ActionDB{

	private static final long serialVersionUID = -7743759894612168310L;

	private String idUsuario;
	private String nombreCompleto;
	private String correo;
	private int idPerfil;
	private int idRegistro;
	private String idEmpleado;
	
	 
	public String getIdUsuario() {
		return idUsuario;
	}
	@StrutsParameter
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
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
	public int getIdPerfil() {
		return idPerfil;
	}
	@StrutsParameter
	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}
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
	
	
	
	
}
