package com.siarex247.catalogos.ProvExternos;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ProvExternosSupport extends ActionDB {

	private static final long serialVersionUID = 155332917882810956L;

	private int claveRegistro;
	private String razonSocial;
	private String rfc;
	private String calle;
	private String numeroInt;
	private String numeroExt;
	private String colonia;
	private String delegacion;
	private String ciudad;
	private String estado;
	private String codigoPostal;
	private String telefono;
	private String nombreContacto;
	private String email;
	private String idProveedor;
	private int idReceptor;
	
	
	public int getClaveRegistro() {
		return claveRegistro;
	}
	@StrutsParameter
	public void setClaveRegistro(int claveRegistro) {
		this.claveRegistro = claveRegistro;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	@StrutsParameter
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRfc() {
		return rfc;
	}
	@StrutsParameter
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getCalle() {
		return calle;
	}
	@StrutsParameter
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumeroInt() {
		return numeroInt;
	}
	@StrutsParameter
	public void setNumeroInt(String numeroInt) {
		this.numeroInt = numeroInt;
	}
	public String getNumeroExt() {
		return numeroExt;
	}
	@StrutsParameter
	public void setNumeroExt(String numeroExt) {
		this.numeroExt = numeroExt;
	}
	public String getColonia() {
		return colonia;
	}
	@StrutsParameter
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getDelegacion() {
		return delegacion;
	}
	@StrutsParameter
	public void setDelegacion(String delegacion) {
		this.delegacion = delegacion;
	}
	public String getCiudad() {
		return ciudad;
	}
	@StrutsParameter
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getEstado() {
		return estado;
	}
	@StrutsParameter
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	@StrutsParameter
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getTelefono() {
		return telefono;
	}
	@StrutsParameter
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	
	
	public String getNombreContacto() {
		return nombreContacto;
	}
	@StrutsParameter
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	public String getEmail() {
		return email;
	}
	@StrutsParameter
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdProveedor() {
		return idProveedor;
	}
	@StrutsParameter
	public void setIdProveedor(String idProveedor) {
		this.idProveedor = idProveedor;
	}
	public int getIdReceptor() {
		return idReceptor;
	}
	@StrutsParameter
	public void setIdReceptor(int idReceptor) {
		this.idReceptor = idReceptor;
	}

	
	
}
