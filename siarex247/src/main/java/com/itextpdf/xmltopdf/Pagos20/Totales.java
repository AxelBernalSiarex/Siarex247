/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos20;

/**
 *
 * @author frack
 */
public class Totales {

    private double _totalRetencionesIVA = 0;
    private double _totalRetencionesISR = 0;
    private double _totalRetencionesIEPS = 0;
    private double _totalTrasladosBaseIVA16 = 0;
    private double _totalTrasladosImpuestoIVA16 = 0;
    private double _totalTrasladosBaseIVA8 = 0;
    private double _totalTrasladosImpuestoIVA8 = 0;
    private double _totalTrasladosBaseIVA0 = 0;
    private double _totalTrasladosImpuestoIVA0 = 0;
    private double _totalTrasladosBaseIVAExento = 0;
    private double _montoTotalPagos = 0;

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de los impuestos retenidos de
     * IVA que se desprenden de los pagos. No se permiten valores negativos.
     * </p>
     */
    public final double getTotalRetencionesIVA() {
        return _totalRetencionesIVA;
    }

    public final void setTotalRetencionesIVA(double value) {
        _totalRetencionesIVA = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de los impuestos retenidos de
     * ISR que se desprenden de los pagos. No se permiten valores negativos.
     * </p>
     */
    public final double getTotalRetencionesISR() {
        return _totalRetencionesISR;
    }

    public final void setTotalRetencionesISR(double value) {
        _totalRetencionesISR = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de los impuestos retenidos de
     * IEPS que se desprenden de los pagos. No se permiten valores
     * negativos.
     * </p>
     */
    public final double getTotalRetencionesIEPS() {
        return _totalRetencionesIEPS;
    }

    public final void setTotalRetencionesIEPS(double value) {
        _totalRetencionesIEPS = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de la base de IVA trasladado
     * a la tasa del 16% que se desprende de los pagos. No se permiten valores
     * negativos.
     * </p>
     */
    public final double getTotalTrasladosBaseIVA16() {
        return _totalTrasladosBaseIVA16;
    }

    public final void setTotalTrasladosBaseIVA16(double value) {
        _totalTrasladosBaseIVA16 = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de los impuestos de IVA
     * trasladado a la tasa del 16% que se desprenden de los pagos. No se
     * permiten valores negativos.
     * </p>
     */
    public final double getTotalTrasladosImpuestoIVA16() {
        return _totalTrasladosImpuestoIVA16;
    }

    public final void setTotalTrasladosImpuestoIVA16(double value) {
        _totalTrasladosImpuestoIVA16 = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de la base de IVA trasladado
     * a la tasa del 8% que se desprende de los pagos. No se permiten valores
     * negativos.
     * </p>
     */
    public final double getTotalTrasladosBaseIVA8() {
        return _totalTrasladosBaseIVA8;
    }

    public final void setTotalTrasladosBaseIVA8(double value) {
        _totalTrasladosBaseIVA8 = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de los impuestos de IVA
     * trasladado a la tasa del 8% que se desprenden de los pagos. No se
     * permiten valores negativos.
     * </p>
     */
    public final double getTotalTrasladosImpuestoIVA8() {
        return _totalTrasladosImpuestoIVA8;
    }

    public final void setTotalTrasladosImpuestoIVA8(double value) {
        _totalTrasladosImpuestoIVA8 = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de la base de IVA trasladado
     * a la tasa del 0% que se desprende de los pagos. No se permiten valores
     * negativos.
     * </p>
     */
    public final double getTotalTrasladosBaseIVA0() {
        return _totalTrasladosBaseIVA0;
    }

    public final void setTotalTrasladosBaseIVA0(double value) {
        _totalTrasladosBaseIVA0 = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de los impuestos de IVA
     * trasladado a la tasa del 0% que se desprenden de los pagos. No se
     * permiten valores negativos.
     * </p>
     */
    public final double getTotalTrasladosImpuestoIVA0() {
        return _totalTrasladosImpuestoIVA0;
    }

    public final void setTotalTrasladosImpuestoIVA0(double value) {
        _totalTrasladosImpuestoIVA0 = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Atributo condicional para expresar el total de la base de IVA trasladado
     * exento que se desprende de los pagos. No se permiten valores
     * negativos.
     * </p>
     */
    public final double getTotalTrasladosBaseIVAExento() {
        return _totalTrasladosBaseIVAExento;
    }

    public final void setTotalTrasladosBaseIVAExento(double value) {
        _totalTrasladosBaseIVAExento = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para expresar el total de los pagos que se desprenden
     * de los nodos Pago. No se permiten valores negativos.
     * </p>
     */
    public final double getMontoTotalPagos() {
        return _montoTotalPagos;
    }

    public final void setMontoTotalPagos(double value) {
        _montoTotalPagos = value;
    }

}
