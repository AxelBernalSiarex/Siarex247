/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

/**
 *
 * @author Faustino
 */

public class Retencion {
	private String _impuesto = "";
	private double _importe = 0;

	public final String getImpuesto() {
		return _impuesto;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para señalar la clave del tipo de impuesto retenido
	 *      aplicable al concepto.
	 */
	public final void setImpuesto(String value) {
		_impuesto = value;
	}

	public final double getImporte() {
		return _importe;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para señalar el importe del impuesto retenido que
	 *      aplica al concepto. No se permiten valores negativos.
	 */
	public final void setImporte(double value) {
		_importe = value;
	}
}
