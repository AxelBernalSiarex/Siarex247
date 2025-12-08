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
public class Receptor {
	private String _rfc = "";
	private String _nombre = ""; // Opcional
	private String _domicilioFiscalReceptor = "";
	private String _residenciaFiscal = ""; // Opcional
	private String _numRegIdTrib = ""; // Opcional
	private String _regimenFiscalReceptor = "";
	private String _usoCFDI = "";

	public final String getRfc() {
		return _rfc;
	}

	/**
	 * @param value Requerido.
	 * @seeAtributo para precisar la clave del Registro Federal de Contribuyentes.
	 */
	public final void setRfc(String value) {
		_rfc = value;
	}

	public final String getNombre() {
		return _nombre;
	}

	/**
	 * @param value Opcional.
	 * @seeAtributo opcional para registrar el nombre, denominación o razón social
	 *              del contribuyente receptor del comprobante.
	 */
	public final void setNombre(String value) {
		_nombre = value;
	}

	public final String getDomicilioFiscalReceptor() {
		return _domicilioFiscalReceptor;
	}

	/**
	 * @param value Requerido.
	 * @seeAtributo Atributo requerido para registrar el código postal del domicilio
	 *              fiscal del receptor del comprobante.
	 */
	public final void setDomicilioFiscalReceptor(String value) {
		_domicilioFiscalReceptor = value;
	}

	public final String getResidenciaFiscal() {
		return _residenciaFiscal;
	}

	/**
	 * @param value Opcional.
	 * @seeEs requerido cuando se incluye el complemento de comercio exterior o se
	 *        registre el atributo NumRegIdTrib.
	 */
	public final void setResidenciaFiscal(String value) {
		_residenciaFiscal = value;
	}

	public final String getNumRegIdTrib() {
		return _numRegIdTrib;
	}

	/**
	 * @param value Opcional.
	 * @see Es requerido cuando se incluye el complemento de comercio exterior.
	 */
	public final void setNumRegIdTrib(String value) {
		_numRegIdTrib = value;
	}

	public final String getRegimenFiscalReceptor() {
		return _regimenFiscalReceptor;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para incorporar la clave del régimen fiscal del
	 *      contribuyente receptor al que aplicará el efecto fiscal de este
	 *      comprobante.
	 */
	public final void setRegimenFiscalReceptor(String value) {
		_regimenFiscalReceptor = value;
	}

	public final String getUsoCFDI() {
		return _usoCFDI;
	}

	/**
	 * @param value Requerido.
	 * @seeAtributo requerido para expresar la clave del uso que dará a esta factura
	 *              el receptor del CFDI.
	 */
	public final void setUsoCFDI(String value) {
		_usoCFDI = value;
	}
}
