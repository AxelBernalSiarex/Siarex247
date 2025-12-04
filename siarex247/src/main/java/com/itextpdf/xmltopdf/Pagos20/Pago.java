/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos20;

import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 *
 * @author Faustino
 */
public class Pago {

    private LocalDateTime _fechaPago = LocalDateTime.MIN; // Ya
    private String _formaDePagoP = ""; // Ya
    private String _monedaP = ""; // Ya
    private double _tipoCambioP = 0; // Ya
    private double _monto = 0; // Ya
    private String _numOperacion = "";  // Ya
    private String _rfcEmisorCtaOrd = "";
    private String _nomBancoOrdExt = ""; // Ya
    private String _ctaOrdenante = ""; // Ya
    private String _rfcEmisorCtaBen = ""; // Ya
    private String _ctaBeneficiario = ""; // Ya
    private String _tipoCadPago = "";
    private String _certPago = ""; // Pendiente
    private String _cadPago = ""; // Pendiente
    private String _selloPago = ""; // Pendiente
    private ArrayList<DoctoRelacionado> _doctoRelacionado = new ArrayList<DoctoRelacionado>();
    private ImpuestosP _impuestos;

    /**
     * <value>Requerido</value>
     * <p>
     * expresar la fecha y hora en la que el beneficiario recibe el pago. Se
     * expresa en la forma aaaa-mm-ddThh:mm:ss, de acuerdo con la especificación
     * ISO 8601.En caso de no contar con la hora se debe registrar 12:00:00.
     * </p>
     */
    public final LocalDateTime getFechaPago() {
        return _fechaPago;
    }

    public final void setFechaPago(LocalDateTime value) {
        _fechaPago = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * expresar la clave de la forma en que se realiza el pago.
     * </p>
     */
    public final String getFormaDePagoP() {
        return _formaDePagoP;
    }

    public final void setFormaDePagoP(String value) {
        _formaDePagoP = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Identificar la clave de la moneda utilizada para realizar el pago, cuando
     * se usa moneda nacional se registra MXN. El atributo Pagos:Pago:Monto y
     * los atributos TotalImpuestosRetenidos, TotalImpuestosTrasladados,
     * Traslados:Traslado:Importe y Retenciones:Retencion:Importe del nodo
     * Pago:Impuestos deben ser expresados en esta moneda. Conforme con la
     * especificación ISO 4217.
     * </p>
     */
    public final String getMonedaP() {
        return _monedaP;
    }

    public final void setMonedaP(String value) {
        _monedaP = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * expresar el tipo de cambio de la moneda a la fecha en que se realizó el
     * pago. El valor debe reflejar el número de pesos mexicanos que equivalen a
     * una unidad de la divisa señalada en el atributo MonedaP. Es requerido
     * cuando el atributo MonedaP es diferente a MXN.
     * </p>
     */
    public final double getTipoCambioP() {
        return _tipoCambioP;
    }

    public final void setTipoCambioP(double value) {
        _tipoCambioP = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * expresar el importe del pago.
     * </p>
     */
    public final double getMonto() {
        return _monto;
    }

    public final void setMonto(double value) {
        _monto = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * expresar el número de cheque, número de autorización, número de
     * referencia, clave de rastreo en caso de ser SPEI, línea de captura o
     * algún número de referencia análogo que identifique la operación que
     * ampara el pago efectuado.
     * </p>
     */
    public final String getNumOperacion() {
        return _numOperacion;
    }

    public final void setNumOperacion(String value) {
        _numOperacion = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * Expresar la clave RFC de la entidad emisora de la cuenta origen, es
     * decir, la operadora, el banco, la institución financiera, emisor de
     * monedero electrónico, etc., en caso de ser extranjero colocar
     * XEXX010101000, considerar las reglas de obligatoriedad publicadas en la
     * página del SAT para éste atributo de acuerdo con el catálogo
     * catCFDI:c_FormaPago.
     * </p>
     */
    public final String getRfcEmisorCtaOrd() {
        return _rfcEmisorCtaOrd;
    }

    public final void setRfcEmisorCtaOrd(String value) {
        _rfcEmisorCtaOrd = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * expresar el nombre del banco ordenante, es requerido en caso de ser
     * extranjero. Considerar las reglas de obligatoriedad publicadas en la
     * página del SAT para éste atributo de acuerdo con el catálogo
     * catCFDI:c_FormaPago.
     * </p>
     */
    public final String getNomBancoOrdExt() {
        return _nomBancoOrdExt;
    }

    public final void setNomBancoOrdExt(String value) {
        _nomBancoOrdExt = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * incorporar el número de la cuenta con la que se realizó el pago.
     * Considerar las reglas de obligatoriedad publicadas en la página del SAT
     * para éste atributo de acuerdo con el catálogo catCFDI:c_FormaPago
     * </p>
     */
    public final String getCtaOrdenante() {
        return _ctaOrdenante;
    }

    public final void setCtaOrdenante(String value) {
        _ctaOrdenante = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * Expresar la clave RFC de la entidad operadora de la cuenta destino, es
     * decir, la operadora, el banco, la institución financiera, emisor de
     * monedero electrónico, etc. Considerar las reglas de obligatoriedad
     * publicadas en la página del SAT para éste atributo de acuerdo con el
     * catálogo catCFDI:c_FormaPago.
     * </p>
     */
    public final String getRfcEmisorCtaBen() {
        return _rfcEmisorCtaBen;
    }

    public final void setRfcEmisorCtaBen(String value) {
        _rfcEmisorCtaBen = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * incorporar el número de cuenta en donde se recibió el pago. Considerar
     * las reglas de obligatoriedad publicadas en la página del SAT para éste
     * atributo de acuerdo con el catálogo catCFDI:c_FormaPago.
     * </p>
     * Longitud Mínima 10 Longitud Máxima 50 Espacio en Blanco Colapsar Patrón
     * [A-Z0-9_]{10,50}
     */
    public final String getCtaBeneficiario() {
        return _ctaBeneficiario;
    }

    public final void setCtaBeneficiario(String value) {
        _ctaBeneficiario = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * identificar la clave del tipo de cadena de pago que genera la entidad
     * receptora del pago. Considerar las reglas de obligatoriedad publicadas en
     * la página del SAT para éste atributo de acuerdo con el catálogo
     * catCFDI:c_FormaPago.
     * </p>
     */
    public final String getTipoCadPago() {
        return _tipoCadPago;
    }

    public final void setTipoCadPago(String value) {
        _tipoCadPago = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * Incorporar el certificado que ampara al pago, como una cadena de texto en
     * formato base 64. Es requerido en caso de que el atributo TipoCadPago
     * contenga información.
     * </p>
     */
    public final String getCertPago() {
        return _certPago;
    }

    public final void setCertPago(String value) {
        _certPago = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * expresar la cadena original del comprobante de pago generado por la
     * entidad emisora de la cuenta beneficiaria. Es requerido en caso de que el
     * atributo TipoCadPago contenga información.
     * </p>
     */
    public final String getCadPago() {
        return _cadPago;
    }

    public final void setCadPago(String value) {
        _cadPago = value;
    }

    /**
     * <value>Opcional</value>
     * <p>
     * Integrar el sello digital que se asocie al pago. La entidad que emite el
     * comprobante de pago, ingresa una cadena original y el sello digital en
     * una sección de dicho comprobante, este sello digital es el que se debe
     * registrar en este campo. Debe ser expresado como una cadena de texto en
     * formato base 64. Es requerido en caso de que el atributo TipoCadPago
     * contenga información.
     * </p>
     */
    public final String getSelloPago() {
        return _selloPago;
    }

    public final void setSelloPago(String value) {
        _selloPago = value;
    }

    /**
     * <value>Requerido</value>
     * <p>
     * Nodo requerido para expresar la lista de documentos relacionados con los
     * pagos. Por cada documento que se relacione se debe generar un nodo
     * DoctoRelacionado.
     * </p>
     */
    public final ArrayList<DoctoRelacionado> getDoctoRelacionado() {
        return _doctoRelacionado;
    }

    public final void setDoctoRelacionado(ArrayList<DoctoRelacionado> value) {
        _doctoRelacionado = value;
    }

    /**
     * <value>Condicional</value>
     * <p>
     * Nodo condicional para registrar el resumen de los impuestos aplicables
     * conforme al monto del pago recibido, expresados a la moneda de pago.
     * </p>
     */
    public final ImpuestosP getImpuestos() {
        return _impuestos;
    }

    public final void setImpuestos(ImpuestosP value) {
        _impuestos = value;
    }
}
