package com.siarex247.catalogos.ConstanciaSituacion;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ConstanciaSituacionSupport extends ActionDB{

	private static final long serialVersionUID = -7070783080648372048L;

	private int idRegistro;
	private int claveProveedor;
    
    
    private String rfc;
    private String razonSocial;
    private String regimenCapital;
    private String fechaNacimiento;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String situacionContribuyente;
    private String curp;
    private String fechaUltimoCambio;
    private String estado;
    private String ciudad;
    private String colonia;
    private String tipoVialidad;
    private String nombreVialidad;
    private String fechaInicioOperaciones;
    private String numExterior;
    private String numInterior;
    private String codigoPostal;
    private String correo;
    private String fechaAlta;
    private String regimenFiscal;
    private String reemplazarConstancia;
    private String cedulaFiscal;
    private String tipoAccion;
    
    
	
    
    
	public int getIdRegistro() {
		return idRegistro;
	}
	@StrutsParameter
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public int getClaveProveedor() {
		return claveProveedor;
	}
	@StrutsParameter
	public void setClaveProveedor(int claveProveedor) {
		this.claveProveedor = claveProveedor;
	}
	public String getRfc() {
		return rfc;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public String getRegimenCapital() {
		return regimenCapital;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public String getNombre() {
		return nombre;
	}
	public String getApellidoPaterno() {
		return apellidoPaterno;
	}
	public String getApellidoMaterno() {
		return apellidoMaterno;
	}
	public String getSituacionContribuyente() {
		return situacionContribuyente;
	}
	public String getCurp() {
		return curp;
	}
	public String getFechaUltimoCambio() {
		return fechaUltimoCambio;
	}
	public String getEstado() {
		return estado;
	}
	public String getCiudad() {
		return ciudad;
	}
	public String getColonia() {
		return colonia;
	}
	public String getTipoVialidad() {
		return tipoVialidad;
	}
	public String getNombreVialidad() {
		return nombreVialidad;
	}
	public String getFechaInicioOperaciones() {
		return fechaInicioOperaciones;
	}
	public String getNumExterior() {
		return numExterior;
	}
	public String getNumInterior() {
		return numInterior;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public String getCorreo() {
		return correo;
	}
	public String getFechaAlta() {
		return fechaAlta;
	}
	public String getRegimenFiscal() {
		return regimenFiscal;
	}
	@StrutsParameter
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	@StrutsParameter
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	@StrutsParameter
	public void setRegimenCapital(String regimenCapital) {
		this.regimenCapital = regimenCapital;
	}
	@StrutsParameter
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	@StrutsParameter
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@StrutsParameter
	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}
	@StrutsParameter
	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}
	@StrutsParameter
	public void setSituacionContribuyente(String situacionContribuyente) {
		this.situacionContribuyente = situacionContribuyente;
	}
	@StrutsParameter
	public void setCurp(String curp) {
		this.curp = curp;
	}
	@StrutsParameter
	public void setFechaUltimoCambio(String fechaUltimoCambio) {
		this.fechaUltimoCambio = fechaUltimoCambio;
	}
	@StrutsParameter
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@StrutsParameter
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	@StrutsParameter
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	@StrutsParameter
	public void setTipoVialidad(String tipoVialidad) {
		this.tipoVialidad = tipoVialidad;
	}
	@StrutsParameter
	public void setNombreVialidad(String nombreVialidad) {
		this.nombreVialidad = nombreVialidad;
	}
	@StrutsParameter
	public void setFechaInicioOperaciones(String fechaInicioOperaciones) {
		this.fechaInicioOperaciones = fechaInicioOperaciones;
	}
	@StrutsParameter
	public void setNumExterior(String numExterior) {
		this.numExterior = numExterior;
	}
	@StrutsParameter
	public void setNumInterior(String numInterior) {
		this.numInterior = numInterior;
	}
	@StrutsParameter
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	@StrutsParameter
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	@StrutsParameter
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	@StrutsParameter
	public void setRegimenFiscal(String regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}
	public String getReemplazarConstancia() {
		return reemplazarConstancia;
	}
	@StrutsParameter
	public void setReemplazarConstancia(String reemplazarConstancia) {
		this.reemplazarConstancia = reemplazarConstancia;
	}
	public String getCedulaFiscal() {
		return cedulaFiscal;
	}
	@StrutsParameter
	public void setCedulaFiscal(String cedulaFiscal) {
		this.cedulaFiscal = cedulaFiscal;
	}
	public String getTipoAccion() {
		return tipoAccion;
	}
	@StrutsParameter
	public void setTipoAccion(String tipoAccion) {
		this.tipoAccion = tipoAccion;
	}
    
	
}
