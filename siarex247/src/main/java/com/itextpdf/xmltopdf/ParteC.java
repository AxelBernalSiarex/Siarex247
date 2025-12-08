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
public class ParteC {
	private String _claveProdServ = "";
	private String _noIdentificacion = ""; // Opcional
	private double _cantidad = 0;
	private String _unidad = ""; // Opcional
	private String _descripcion = "";
	private double _valorUnitario = 0;
	private double _importe = 0;
	private ArrayList<InformacionAduaneraC> _informacionAduanera;

	public final String getClaveProdServ() {
		return _claveProdServ;
	}

	/**
	 * @param value Requerido.
	 * @see Expresa la clave del producto o del servicio amparado por la presente
	 *      parte. Es requerido y deben utilizar las claves del cátalogo de
	 *      productos y servicios, cuando los conceptos que registren por sus
	 *      actividades corresponden con dichos conceptos.
	 */
	public final void setClaveProdServ(String value) {
		_claveProdServ = value;
	}

	public final String getNoIdentificacion() {
		return _noIdentificacion;
	}

	/**
	 * @param value Opcional.
	 * @see Expresa el número de serie, número de parte del bien o identificador del
	 *      producto o del servicio amparado por la presente parte.
	 */
	public final void setNoIdentificacion(String value) {
		_noIdentificacion = value;
	}

	public final double getCantidad() {
		return _cantidad;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para precisar la cantidad de bienes o servicios del
	 *      tipo particular definido por la presente parte.
	 */
	public final void setCantidad(double value) {
		_cantidad = value;
	}

	public final String getUnidad() {
		return _unidad;
	}

	/**
	 * @param value Opcional.
	 * @see Atributo opcional para precisar la unidad de medida propia de la
	 *      operación del emisor, aplicable para la cantidad expresada en la parte.
	 *      La unidad debe corresponder con la descripción de la parte.
	 */
	public final void setUnidad(String value) {
		_unidad = value;
	}

	public final String getDescripcion() {
		return _descripcion;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo requerido para precisar la descripción del bien o servicio
	 *      cubierto por la presente parte.
	 */
	public final void setDescripcion(String value) {
		_descripcion = value;
	}

	public final double getValorUnitario() {
		return _valorUnitario;
	}

	/**
	 * @param value Requerido.
	 * @see Atributo opcional para precisar el valor o precio unitario del bien o
	 *      servicio cubierto por la presente parte. No se permiten valores
	 *      negativos.
	 */
	public final void setValorUnitario(double value) {
		_valorUnitario = value;
	}

	public final double getImporte() {
		return _importe;
	}

	/**
	 * @param value Opcional.
	 * @see Atributo opcional para precisar el importe total de los bienes o
	 *      servicios de la presente parte. Debe ser equivalente al resultado de
	 *      multiplicar la cantidad por el valor unitario expresado en la parte. No
	 *      se permiten valores negativos.
	 */
	public final void setImporte(double value) {
		_importe = value;
	}

	public final ArrayList<InformacionAduaneraC> getInformacionAduanera() {
		return _informacionAduanera;
	}

	/**
	 * @param value Opcional.
	 * @see Nodo opcional para introducir la información aduanera aplicable cuando
	 *      se trate de ventas de primera mano de mercancías importadas o se trate
	 *      de operaciones de comercio exterior con bienes o servicios.
	 */
	public final void setInformacionAduanera(ArrayList<InformacionAduaneraC> value) {
		_informacionAduanera = value;
	}
}
