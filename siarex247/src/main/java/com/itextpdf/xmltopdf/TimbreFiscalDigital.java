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
import java.time.*;

public class TimbreFiscalDigital {
	private String _version = "1.1";
	private String _UUID = "";
	private LocalDateTime _fechaTimbrado = LocalDateTime.MIN;
	private String _rfcProvCertif = "";
	private String _leyenda = "";
	private String _selloCFD = "";
	private String _noCertificadoSAT = "";
	private String _selloSAT = "";

	public final String getVersion() {
		return _version;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para la expresión de la versión del estándar del
	 *      Timbre Fiscal Digital.
	 */
	public final void setVersion(String value) {
		_version = value;
	}

	public final String getUUID() {
		return _UUID;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para expresar los 36 caracteres del folio fiscal
	 *      (UUID) de la transacción de timbrado conforme al estándar RFC 4122.
	 */
	public final void setUUID(String value) {
		_UUID = value;
	}

	public final LocalDateTime getFechaTimbrado() {
		return _fechaTimbrado;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para expresar la fecha y hora, de la generación del
	 *      timbre por la certificación digital del SAT. Se expresa en la forma
	 *      AAAA-MM-DDThh:mm:ss y debe corresponder con la hora de la Zona Centro
	 *      del Sistema de Horario en México.
	 */
	public final void setFechaTimbrado(LocalDateTime value) {
		_fechaTimbrado = value;
	}

	public final String getRfcProvCertif() {
		return _rfcProvCertif;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para expresar el RFC del proveedor de certificación
	 *      de comprobantes fiscales digitales que genera el timbre fiscal digital.
	 */
	public final void setRfcProvCertif(String value) {
		_rfcProvCertif = value;
	}

	public final String getLeyenda() {
		return _leyenda;
	}

	/**
	 * @param value Opcional.
	 * @see Atributo opcional para registrar información que el SAT comunique a los
	 *      usuarios del CFDI.
	 */
	public final void setLeyenda(String value) {
		_leyenda = value;
	}

	public final String getSelloCFD() {
		return _selloCFD;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para contener el sello digital del comprobante fiscal
	 *      o del comprobante de retenciones, que se ha timbrado. El sello debe ser
	 *      expresado como una cadena de texto en formato Base 64.
	 */
	public final void setSelloCFD(String value) {
		_selloCFD = value;
	}

	public final String getNoCertificadoSAT() {
		return _noCertificadoSAT;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para expresar el número de serie del certificado del
	 *      SAT usado para generar el sello digital del Timbre Fiscal Digital.
	 */
	public final void setNoCertificadoSAT(String value) {
		_noCertificadoSAT = value;
	}

	public final String getSelloSAT() {
		return _selloSAT;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para contener el sello digital del Timbre Fiscal
	 *      Digital, al que hacen referencia las reglas de la Resolución Miscelánea
	 *      vigente. El sello debe ser expresado como una cadena de texto en formato
	 *      Base 64.
	 */
	public final void setSelloSAT(String value) {
		_selloSAT = value;
	}
}
