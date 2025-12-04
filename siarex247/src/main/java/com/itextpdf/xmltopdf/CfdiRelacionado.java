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
public class CfdiRelacionado {
	private String _uuid;

	public String getUUID() {
		return _uuid;
	}

	/**
	 * @param value Requerido.
	 * @see Atributos requerido para registrar el folio fiscal (UUID) de un CFDI
	 *      relacionado con el presente comprobante.
	 *      </p>
	 */
	public void setUUID(String value) {
		_uuid = value;
	}
}
