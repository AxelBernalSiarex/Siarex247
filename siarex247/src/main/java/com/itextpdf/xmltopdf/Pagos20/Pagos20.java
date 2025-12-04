/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos20;

import java.util.ArrayList;

/**
 *
 * @author Faustino
 */
public class Pagos20 {

    private Totales _totales = new Totales();
    private ArrayList<Pago> _pago = new ArrayList<Pago>();
    private String _version = "2.0";

    /**
     * <value>Requerido</value>
     * <p>
     * Nodo requerido para especificar el monto total de los pagos y el total de
     * los impuestos, deben ser expresados en MXN
     * </p>
     */
    public Totales getTotales() {
        return _totales;
    }

    public void setTotales(Totales value) {
        _totales = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Elemento requerido para incorporar la información de la recepción de
     * pagos.
     * </p>
     */
    public ArrayList<Pago> getPago() {
        return _pago;
    }

    public void setPago(ArrayList<Pago> value) {
        _pago = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Indica la versión del complemento para recepción de pagos.
     * </p>
     */
    public String getVersion() {
        return _version;
    }

    public void setVersion(String value) {
        _version = value;
    }

}
