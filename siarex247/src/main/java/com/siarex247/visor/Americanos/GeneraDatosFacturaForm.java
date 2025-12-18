package com.siarex247.visor.Americanos;

import java.util.ArrayList;

public class GeneraDatosFacturaForm {

	private String[][] datosFechaFactura;
	private String[] datosProveedorEmisor;
	private String[] datosEmpresaReceptor;
	private String[][] datosTabla1Encabezado;
	private ArrayList<String []> datosTablaContenido;
	private String[][] datosTablaSubtotal;
	
	
	public String[][] getDatosFechaFactura() {
		return datosFechaFactura;
	}
	public void setDatosFechaFactura(String[][] datosFechaFactura) {
		this.datosFechaFactura = datosFechaFactura;
	}
	public String[] getDatosProveedorEmisor() {
		return datosProveedorEmisor;
	}
	public void setDatosProveedorEmisor(String[] datosProveedorEmisor) {
		this.datosProveedorEmisor = datosProveedorEmisor;
	}
	public String[] getDatosEmpresaReceptor() {
		return datosEmpresaReceptor;
	}
	public void setDatosEmpresaReceptor(String[] datosEmpresaReceptor) {
		this.datosEmpresaReceptor = datosEmpresaReceptor;
	}
	public String[][] getDatosTabla1Encabezado() {
		return datosTabla1Encabezado;
	}
	public void setDatosTabla1Encabezado(String[][] datosTabla1Encabezado) {
		this.datosTabla1Encabezado = datosTabla1Encabezado;
	}
	
	
	
	public ArrayList<String[]> getDatosTablaContenido() {
		return datosTablaContenido;
	}
	public void setDatosTablaContenido(ArrayList<String[]> datosTablaContenido) {
		this.datosTablaContenido = datosTablaContenido;
	}
	public String[][] getDatosTablaSubtotal() {
		return datosTablaSubtotal;
	}
	public void setDatosTablaSubtotal(String[][] datosTablaSubtotal) {
		this.datosTablaSubtotal = datosTablaSubtotal;
	}
	
}
