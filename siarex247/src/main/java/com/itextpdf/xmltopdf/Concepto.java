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
public class Concepto {
	private String _claveProdServ = "";
	private String _noIdentificacion = ""; // Opcional
	private float _cantidad = 0;
	private String _claveUnidad = "";
	private String _unidad = ""; // Opcional
	private String _descripcion = "";
	private double _valorUnitario = 0;
	private double _importe = 0;
	private double _descuento = 0; // Opcional
	private String _objetoImp = "";
	private ImpuestosC _impuestos;
	private ArrayList<InformacionAduaneraC> _informacionAduanera = new ArrayList<>();
	private CuentaPredialC _cuentaPredial;
	public Complemento _complemento;
	private ArrayList<ParteC> _parte = new ArrayList<>();

	public final String getClaveProdServ() {
		return _claveProdServ;
	}

	/**
	 * @param value Requerido.
	 * @seeExpresa la clave del producto o del servicio amparado por el presente
	 *             concepto. Se deben utilizar las claves del catalogo de productos
	 *             y servicios.
	 */
	public final void setClaveProdServ(String value) {
		_claveProdServ = value;
	}

	public final String getNoIdentificacion() {
		return _noIdentificacion;
	}

	/**
	 * @param value Opcional.
	 * @see Expresa el número de parte, identificador del producto o del servicio,
	 *      SKU o equivalente.
	 */
	public final void setNoIdentificacion(String value) {
		_noIdentificacion = value;
	}

	public final float getCantidad() {
		return _cantidad;
	}

	/**
	 * @param value Requerido.
	 * @see Precisa la cantidad de bienes o servicios del tipo particular definido
	 *      por el presente concepto.
	 */
	public final void setCantidad(float value) {
		_cantidad = value;
	}

	public final String getClaveUnidad() {
		return _claveUnidad;
	}

	/**
	 * @param value Requerido.
	 * @see Se debe utilizar la clave del catalogo de unidades. La unidad debe
	 *      corresponder con la descripción del concepto.
	 */
	public final void setClaveUnidad(String value) {
		_claveUnidad = value;
	}

	public final String getUnidad() {
		return _unidad;
	}

	/**
	 * @param value Opcional.
	 * @see Precisa la unidad de medida propia de la operación del emisor, aplicable
	 *      para la cantidad expresada en el concepto.
	 */
	public final void setUnidad(String value) {
		_unidad = value;
	}

	public final String getDescripcion() {
		return _descripcion;
	}

	/**
	 * @param value Requerido.
	 * @see Precisa la descripción del bien o servicio cubierto por el presente
	 *      concepto.
	 */
	public final void setDescripcion(String value) {
		_descripcion = value;
	}

	public final double getValorUnitario() {
		return _valorUnitario;
	}

	/**
	 * @param value Requerido.
	 * @see Precisa el valor o precio unitario del bien o servicio cubierto por el
	 *      presente concepto.
	 */
	public final void setValorUnitario(double value) {
		_valorUnitario = value;
	}

	public final double getImporte() {
		return _importe;
	}

	/**
	 * @param value Requerido.
	 * @see Precisa el importe total de los bienes o servicios del presente
	 *      concepto. Debe ser equivalente al resultado de multiplicar la cantidad
	 *      por el valor unitario expresado en el concepto. No se permiten valores
	 *      negativos.
	 */
	public final void setImporte(double value) {
		_importe = value;
	}

	public final double getDescuento() {
		return _descuento;
	}

	/**
	 * @param value Opcional.
	 * @see Representa el importe de los descuentos aplicables al concepto. No se
	 *      permiten valores negativos.
	 */
	public final void setDescuento(double value) {
		_descuento = value;
	}

	public final String getObjetoImp() {
		return _objetoImp;
	}

	/**
	 * @param value Opcional.
	 * @see Atributo requerido para expresar si la operación comercial es objeto o
	 *      no de impuesto.
	 */
	public final void setObjetoImp(String value) {
		_objetoImp = value;
	}

	public final ImpuestosC getImpuestos() {
		return _impuestos;
	}

	/**
	 * @param value Opcional.
	 * @see Nodo opcional para capturar los impuestos aplicables al presente
	 *      concepto. Cuando un concepto no registra un impuesto, implica que no es
	 *      objeto del mismo.
	 */
	public final void setImpuestos(ImpuestosC value) {
		_impuestos = value;
	}

	public final ArrayList<InformacionAduaneraC> getInformacionAduanera() {
		return _informacionAduanera;
	}

	/**
	 * @param value mOpcional.
	 * @see Nodo opcional para introducir la información aduanera cuando se trate de
	 *      ventas de primera mano de mercancias importadas o se trate de
	 *      operaciones de comercion exterior con bienes o servicios.
	 */
	public final void setInformacionAduanera(ArrayList<InformacionAduaneraC> value) {
		_informacionAduanera = value;
	}

	public final CuentaPredialC getCuentaPredial() {
		return _cuentaPredial;
	}

	/**
	 * @param value Opcional.
	 * @see Nodo opcional para asentar el número de cuenta predial con el que fue
	 *      registrado el inmueble, en el sistema catastra de la entidad federativa
	 *      de que trate.
	 */
	public final void setCuentaPredial(CuentaPredialC value) {
		_cuentaPredial = value;
	}

	public final ArrayList<ParteC> getParte() {
		return _parte;
	}

	/*************************
	 * Falta agregar la clase ComplementoConcepto
	 *************************************/
	/**
	 * @param value Opcional.
	 * @see Nodo opcional para expresar las partes o componentes que integran la
	 *      totalidad del concepto expresado en el CFDI.
	 */
	public final void setParte(ArrayList<ParteC> value) {
		_parte = value;
	}
}