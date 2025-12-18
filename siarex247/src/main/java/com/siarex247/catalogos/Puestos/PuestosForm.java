package com.siarex247.catalogos.Puestos;

public class PuestosForm {

	
	private int idPuesto = 0;
	private String nombreCorto;
	private String descripcion;
	private String usuarioTrans;
	private String fechaTrans;
	private boolean selected;
	
	public int getIdPuesto() {
		return idPuesto;
	}
	public void setIdPuesto(int idPuesto) {
		this.idPuesto = idPuesto;
	}
	public String getNombreCorto() {
		return nombreCorto;
	}
	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getUsuarioTrans() {
		return usuarioTrans;
	}
	public void setUsuarioTrans(String usuarioTrans) {
		this.usuarioTrans = usuarioTrans;
	}
	public String getFechaTrans() {
		return fechaTrans;
	}
	public void setFechaTrans(String fechaTrans) {
		this.fechaTrans = fechaTrans;
	}

	
	
	
	
}
