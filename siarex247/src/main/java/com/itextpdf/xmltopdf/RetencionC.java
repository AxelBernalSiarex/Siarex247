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
public class RetencionC {
	private double _base = 0;
	private String _impuesto = "";
	private String _tipoFactor = "";
	private double _tasaOCuota = 0;
	private double _importe = 0;

	public final double getBase() {
		return _base;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para señalar la base para el calculo de la retención,
	 *      la determinación de la base se realiza de acuerdo con las disposiciones
	 *      fiscales vigentes. No se permiten valores negativos.
	 */
	public final void setBase(double value) {
		_base = value;
	}

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

	public final String getTipoFactor() {
		return _tipoFactor;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para señalar la clave del tipo de factor que se
	 *      aplica a la base del impuesto.
	 */
	public final void setTipoFactor(String value) {
		_tipoFactor = value;
	}

	public final double getTasaOCuota() {
		return _tasaOCuota;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para señalar la tasa o cuota del impuesto que se
	 *      retiene para el presente concepto.
	 */
	public final void setTasaOCuota(double value) {
		_tasaOCuota = value;
	}

	public final double getImporte() {
		return _importe;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para señalar el importe del impuesto retenido que
	 *      aplica al concepto. No se permiten valore negativos.
	 */
	public final void setImporte(double value) {
		_importe = value;
	}
}
