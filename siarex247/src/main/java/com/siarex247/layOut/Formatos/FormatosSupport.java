package com.siarex247.layOut.Formatos;

import java.io.File;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class FormatosSupport extends ActionDB{

	private static final long serialVersionUID = 5257248914340214934L;

	private int idRegistro;
	private String descripcion;
	private String subjectCorreo;
	private String cuerpoCorreo;
	private String nombreArchivo;
	private String tipoProveedor;
	private String estatus;
	private String listaFormatos;
	private String copiaPara;
	private int idProveedor;
	
	
	
	public int getIdRegistro() {
		return idRegistro;
	}
	@StrutsParameter
	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}
	public String getDescripcion() {
		return descripcion;
	}
	@StrutsParameter
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getSubjectCorreo() {
		return subjectCorreo;
	}
	@StrutsParameter
	public void setSubjectCorreo(String subjectCorreo) {
		this.subjectCorreo = subjectCorreo;
	}
	public String getCuerpoCorreo() {
		return cuerpoCorreo;
	}
	@StrutsParameter
	public void setCuerpoCorreo(String cuerpoCorreo) {
		this.cuerpoCorreo = cuerpoCorreo;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	@StrutsParameter
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getTipoProveedor() {
		return tipoProveedor;
	}
	@StrutsParameter
	public void setTipoProveedor(String tipoProveedor) {
		this.tipoProveedor = tipoProveedor;
	}
	public String getEstatus() {
		return estatus;
	}
	@StrutsParameter
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getListaFormatos() {
		return listaFormatos;
	}
	@StrutsParameter
	public void setListaFormatos(String listaFormatos) {
		this.listaFormatos = listaFormatos;
	}
	public String getCopiaPara() {
		return copiaPara;
	}
	@StrutsParameter
	public void setCopiaPara(String copiaPara) {
		this.copiaPara = copiaPara;
	}
	public int getIdProveedor() {
		return idProveedor;
	}
	@StrutsParameter
	public void setIdProveedor(int idProveedor) {
		this.idProveedor = idProveedor;
	}
	
}
