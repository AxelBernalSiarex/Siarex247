/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos20;

/**
 *
 * @author Faustino
 */
public class DoctoRelacionado {

    private String _idDocumento = "";
    private String _serie = "";
    private String _folio = "";
    private String _monedaDR = "";
    private double _equivalenciaDR = 0;
    private String _numParcialidad = "";
    private double _impSaldoAnt = 0;
    private double _impPagado = 0;
    private double _impSaldoInsoluto = 0;
    private String _objetoImpDR = "";
    private ImpuestosDR _impuestosDR;

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para expresar el identificador del documento
     * relacionado con el pago. Este dato puede ser un Folio Fiscal de la
     * Factura Electrónica o bien el número de operación de un documento
     * digital.
     * </p>
     */
    public final String getIdDocumento() {
        return _idDocumento;
    }

    public final void setIdDocumento(String value) {
        _idDocumento = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * precisar la serie del comprobante para control interno del contribuyente,
     * acepta una cadena de caracteres.
     * </p>
     */
    public final String getSerie() {
        return _serie;
    }

    public final void setSerie(String value) {
        _serie = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * Atributo opcional para precisar el folio del comprobante para control
     * interno del contribuyente, acepta una cadena de caracteres.
     * </p>
     */
    public final String getFolio() {
        return _folio;
    }

    public final void setFolio(String value) {
        _folio = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para identificar la clave de la moneda utilizada en
     * los importes del documento relacionado, cuando se usa moneda nacional o
     * el documento relacionado no especifica la moneda se registra MXN. Los
     * importes registrados en los atributos "ImpSaldoAnt”, “ImpPagado” e
     * “ImpSaldoInsoluto” de éste nodo, deben corresponder a esta moneda.
     * Conforme con la especificación ISO 4217. </para>
     */
    public final String getMonedaDR() {
        return _monedaDR;
    }

    public final void setMonedaDR(String value) {
        _monedaDR = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * Atributo condicional para expresar el tipo de cambio conforme con la
     * moneda registrada en el documento relacionado. Es requerido cuando la
     * moneda del documento relacionado es distinta de la moneda de pago. Se
     * debe registrar el número de unidades de la moneda señalada en el
     * documento relacionado que equivalen a una unidad de la moneda del pago.
     * Por ejemplo: El documento relacionado se registra en USD. El pago se
     * realiza por 100 EUR. Este atributo se registra como 1.114700 USD/EUR. El
     * importe pagado equivale a 100 EUR * 1.114700 USD/EUR = 111.47 USD.
     * </p>
     */
    public final double getEquivalenciaDR() {
        return _equivalenciaDR;
    }

    public final void setEquivalenciaDR(double value) {
        _equivalenciaDR = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * Atributo requerido para expresar el número de parcialidad que corresponde
     * al pago.
     * </p>
     */
    public final String getNumParcialidad() {
        return _numParcialidad;
    }

    public final void setNumParcialidad(String value) {
        _numParcialidad = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * expresar el monto del saldo insoluto de la parcialidad anterior. Es
     * requerido cuando MetodoDePagoDR contiene: "PPD” Pago en parcialidades o
     * diferido.En el caso de que sea la primer parcialidad este campo debe
     * contener el importe total del documento relacionado.</para>
     */
    public final double getImpSaldoAnt() {
        return _impSaldoAnt;
    }

    public final void setImpSaldoAnt(double value) {
        _impSaldoAnt = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para expresar el importe pagado para el documento
     * relacionado.
     * </p>
     */
    public final double getImpPagado() {
        return _impPagado;
    }

    public final void setImpPagado(double value) {
        _impPagado = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para expresar la diferencia entre el importe del saldo
     * anterior y el monto del pago.
     * </p>
     */
    public final double getImpSaldoInsoluto() {
        return _impSaldoInsoluto;
    }

    public final void setImpSaldoInsoluto(double value) {
        _impSaldoInsoluto = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Atributo requerido para expresar si el pago del documento relacionado es
     * objeto o no de impuesto.
     * </p>
     */
    public final String getObjetoImpDR() {
        return _objetoImpDR;
    }

    public final void setObjetoImpDR(String value) {
        _objetoImpDR = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * Nodo opcional para capturar los impuestos retenidos aplicables conforme
     * al monto del pago recibido.
     * </p>
     */
    public final ImpuestosDR getImpuestosDR() {
        return _impuestosDR;
    }

    public final void setImpuestosDR(ImpuestosDR value) {
        _impuestosDR = value;
    }
}
