package com.siarex247.prodigia.data;

import java.util.ArrayList;

public class ConsultaEFO {
	private String consultaOk; // Valores permitidos, true | false, indican el resultado de la operaci�n. En concreto si todo result� exitosamente y el proceso no present� errores, el valor esperado ser� true, por el contrario false.
	private String fechaLista; //: Cadena de texto con informaci�n sobre la fecha de actualizaci�n de la lista por parte del SAT
	private String codigo;     //Indica el estatus de la operaci�n, si el proceso se realiz� correctamente el valor esperado es 0, de lo contrario se especifica el codigo de error.
	private String mensaje;
	private ArrayList<DetalleConsultaEFO> detalles;    //: Nodo que contiene los atributos de un registro de manera individual
	
	public String getConsultaOk() {
		return consultaOk;
	}

	public void setConsultaOk(String consultaOk) {
		this.consultaOk = consultaOk;
	}

	public String getFechaLista() {
		return fechaLista;
	}

	public void setFechaLista(String fechaLista) {
		this.fechaLista = fechaLista;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public ArrayList<DetalleConsultaEFO> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<DetalleConsultaEFO> detalles) {
		this.detalles = detalles;
	}

}
