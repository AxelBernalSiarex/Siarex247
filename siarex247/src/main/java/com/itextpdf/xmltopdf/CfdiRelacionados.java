/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

import java.util.ArrayList;

/**
 *
 * @author Faustino
 */
public class CfdiRelacionados {
	private String _tipoRelacion = "";
	private ArrayList<CfdiRelacionado> _cfdiRelacionado = new ArrayList<>();

	public final String getTipoRelacion() {
		return _tipoRelacion;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para indicar la clave de la relación que existe entre
	 *      este que se esta generando y el o los CFDI previos.
	 */
	public final void setTipoRelacion(String value) {
		_tipoRelacion = value;
	}

	public final ArrayList<CfdiRelacionado> getCfdiRelacionado() {
		return _cfdiRelacionado;
	}

	/**
	 * @param value Requerido.
	 * @see Nodo requerido para precisar la información de los comprobantes
	 *      relacionados.
	 */
	public final void setCfdiRelacionado(ArrayList<CfdiRelacionado> value) {
		_cfdiRelacionado = value;
	}
}
