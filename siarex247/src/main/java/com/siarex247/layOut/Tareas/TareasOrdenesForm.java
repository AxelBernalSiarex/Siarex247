package com.siarex247.layOut.Tareas;

public class TareasOrdenesForm {

	// CLAVE_REGISTRO, NUMERO_ORDEN, CLAVE_PROVEEDOR, VENDOR_ID, RFC, TOTAL, TIPO_MONEDA, NOMBRE_ARCHIVO
	private int claveRegistro = 0;
	private int claveTarea = 0;
	private long numeroOrden = 0;
	private int claveProveedor = 0;
	private String vendorID = "";
	private String rfc = "";
	private double total = 0;
	private String tipoMoneda = "";
	private String nombreArchivo = "";
	
	
	
	public int getClaveRegistro() {
		return claveRegistro;
	}
	public void setClaveRegistro(int claveRegistro) {
		this.claveRegistro = claveRegistro;
	}
	public int getClaveTarea() {
		return claveTarea;
	}
	public void setClaveTarea(int claveTarea) {
		this.claveTarea = claveTarea;
	}
	public long getNumeroOrden() {
		return numeroOrden;
	}
	public void setNumeroOrden(long numeroOrden) {
		this.numeroOrden = numeroOrden;
	}
	public int getClaveProveedor() {
		return claveProveedor;
	}
	public void setClaveProveedor(int claveProveedor) {
		this.claveProveedor = claveProveedor;
	}
	public String getVendorID() {
		return vendorID;
	}
	public void setVendorID(String vendorID) {
		this.vendorID = vendorID;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	
	
	
	
	
	
}
