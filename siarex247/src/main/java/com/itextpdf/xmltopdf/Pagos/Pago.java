/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos;

import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 *
 * @author Faustino
 */
public class Pago {
        private LocalDateTime _fechaPago = LocalDateTime.MIN;
        private String _formaDePagoP = "";
        private String _monedaP = "";
        private double _tipoCambioP = 0;
        private double _monto = 0;
        private String _numOperacion = "";
        private String _rfcEmisorCtaOrd = "";
        private String _nomBancoOrdExt = "";
        private String _ctaOrdenante = "";
        private String _rfcEmisorCtaBen = "";
        private String _ctaBeneficiario = "";
        private String _tipoCadPago = "";
        private String _certPago = "";
        private String _cadPago = "";
        private String _selloPago = "";
        private ArrayList<PDoctoRelacionado> _doctoRelacionado = new ArrayList<PDoctoRelacionado>();
        private ArrayList<PImpuestos> _impuestos = new ArrayList<PImpuestos>();

        public final LocalDateTime getFechaPago() {
                return _fechaPago;
        }

        /**
         * @param value Requerido
         * @see expresar la fecha y hora en la que el beneficiario recibe el pago. Se
         *      expresa en la forma aaaa-mm-ddThh:mm:ss, de acuerdo con la
         *      especificación ISO 8601.En caso de no contar con la hora se debe
         *      registrar 12:00:00.
         */
        public final void setFechaPago(LocalDateTime value) {
                _fechaPago = value;
        }

        public final String getFormaDePagoP() {
                return _formaDePagoP;
        }

        /**
         * @param value Requerido
         * @see expresar la clave de la forma en que se realiza el pago.
         */
        public final void setFormaDePagoP(String value) {
                _formaDePagoP = value;
        }

        public final String getMonedaP() {
                return _monedaP;
        }

        /**
         * @param value Requerido
         * @see Identificar la clave de la moneda utilizada para realizar el pago,
         *      cuando se usa moneda nacional se registra MXN. El atributo
         *      Pagos:Pago:Monto y los atributos TotalImpuestosRetenidos,
         *      TotalImpuestosTrasladados, Traslados:Traslado:Importe y
         *      Retenciones:Retencion:Importe del nodo Pago:Impuestos deben ser
         *      expresados en esta moneda. Conforme con la especificación ISO 4217.
         */
        public final void setMonedaP(String value) {
                _monedaP = value;
        }

        public final double getTipoCambioP() {
                return _tipoCambioP;
        }

        /**
         * @param value Opcional
         * @see expresar el tipo de cambio de la moneda a la fecha en que se realizó el
         *      pago. El valor debe reflejar el número de pesos mexicanos que equivalen
         *      a una unidad de la divisa señalada en el atributo MonedaP. Es requerido
         *      cuando el atributo MonedaP es diferente a MXN.
         */
        public final void setTipoCambioP(double value) {
                _tipoCambioP = value;
        }

        public final double getMonto() {
                return _monto;
        }

        /**
         * @param value Requerido
         * @see expresar el importe del pago.
         */
        public final void setMonto(double value) {
                _monto = value;
        }

        public final String getNumOperacion() {
                return _numOperacion;
        }

        /**
         * @param value Opcional
         * @see expresar el número de cheque, número de autorización, número de
         *      referencia, clave de rastreo en caso de ser SPEI, línea de captura o
         *      algún número de referencia análogo que identifique la operación que
         *      ampara el pago efectuado.
         */
        public final void setNumOperacion(String value) {
                _numOperacion = value;
        }

        public final String getRfcEmisorCtaOrd() {
                return _rfcEmisorCtaOrd;
        }

        /**
         * @param value Opcional
         * @see Expresar la clave RFC de la entidad emisora de la cuenta origen, es
         *      decir, la operadora, el banco, la institución financiera, emisor de
         *      monedero electrónico, etc., en caso de ser extranjero colocar
         *      XEXX010101000, considerar las reglas de obligatoriedad publicadas en la
         *      página del SAT para éste atributo de acuerdo con el catálogo
         *      catCFDI:c_FormaPago.
         */
        public final void setRfcEmisorCtaOrd(String value) {
                _rfcEmisorCtaOrd = value;
        }

        public final String getNomBancoOrdExt() {
                return _nomBancoOrdExt;
        }

        /**
         * @param value Opcional
         * @see expresar el nombre del banco ordenante, es requerido en caso de ser
         *      extranjero. Considerar las reglas de obligatoriedad publicadas en la
         *      página del SAT para éste atributo de acuerdo con el catálogo
         *      catCFDI:c_FormaPago.
         */
        public final void setNomBancoOrdExt(String value) {
                _nomBancoOrdExt = value;
        }

        public final String getCtaOrdenante() {
                return _ctaOrdenante;
        }

        /**
         * @param value Opcional
         * @see incorporar el número de la cuenta con la que se realizó el pago.
         *      Considerar las reglas de obligatoriedad publicadas en la página del SAT
         *      para éste atributo de acuerdo con el catálogo catCFDI:c_FormaPago
         */
        public final void setCtaOrdenante(String value) {
                _ctaOrdenante = value;
        }

        public final String getRfcEmisorCtaBen() {
                return _rfcEmisorCtaBen;
        }

        /**
         * @param value Opcional
         * @see Expresar la clave RFC de la entidad operadora de la cuenta destino, es
         *      decir, la operadora, el banco, la institución financiera, emisor de
         *      monedero electrónico, etc. Considerar las reglas de obligatoriedad
         *      publicadas en la página del SAT para éste atributo de acuerdo con el
         *      catálogo catCFDI:c_FormaPago.
         */
        public final void setRfcEmisorCtaBen(String value) {
                _rfcEmisorCtaBen = value;
        }

        public final String getCtaBeneficiario() {
                return _ctaBeneficiario;
        }

        /**
         * @param value Opcional
         * @see incorporar el número de cuenta en donde se recibió el pago. Considerar
         *      las reglas de obligatoriedad publicadas en la página del SAT para éste
         *      atributo de acuerdo con el catálogo catCFDI:c_FormaPago.
         *      Longitud Mínima 10 Longitud Máxima 50 Espacio en Blanco Colapsar Patrón
         *      [A-Z0-9_]{10,50}
         */
        public final void setCtaBeneficiario(String value) {
                _ctaBeneficiario = value;
        }

        public final String getTipoCadPago() {
                return _tipoCadPago;
        }

        /**
         * @param value Opcional
         * @see identificar la clave del tipo de cadena de pago que genera la entidad
         *      receptora del pago. Considerar las reglas de obligatoriedad publicadas
         *      en la página del SAT para éste atributo de acuerdo con el catálogo
         *      catCFDI:c_FormaPago.
         */
        public final void setTipoCadPago(String value) {
                _tipoCadPago = value;
        }

        public final String getCertPago() {
                return _certPago;
        }

        /**
         * @param value Opcional
         * @see Incorporar el certificado que ampara al pago, como una cadena de texto
         *      en formato base 64. Es requerido en caso de que el atributo TipoCadPago
         *      contenga información.
         */
        public final void setCertPago(String value) {
                _certPago = value;
        }

        public final String getCadPago() {
                return _cadPago;
        }

        /**
         * @param value Opcional
         * @see expresar la cadena original del comprobante de pago generado por la
         *      entidad emisora de la cuenta beneficiaria. Es requerido en caso de que
         *      el atributo TipoCadPago contenga información.
         */
        public final void setCadPago(String value) {
                _cadPago = value;
        }

        public final String getSelloPago() {
                return _selloPago;
        }

        /**
         * @param value Opcional
         * @see Integrar el sello digital que se asocie al pago. La entidad que emite el
         *      comprobante de pago, ingresa una cadena original y el sello digital en
         *      una sección de dicho comprobante, este sello digital es el que se debe
         *      registrar en este campo. Debe ser expresado como una cadena de texto en
         *      formato base 64. Es requerido en caso de que el atributo TipoCadPago
         *      contenga información.
         */
        public final void setSelloPago(String value) {
                _selloPago = value;
        }

        public final ArrayList<PDoctoRelacionado> getDoctoRelacionado() {
                return _doctoRelacionado;
        }

        /**
         * @param value Requerido
         * @see DoctoRelacionado (0, Ilimitado) Impuestos (0, Ilimitado)
         */
        public final void setDoctoRelacionado(ArrayList<PDoctoRelacionado> value) {
                _doctoRelacionado = value;
        }

        public final ArrayList<PImpuestos> getImpuestos() {
                return _impuestos;
        }

        /**
         * @param value Requerido
         * @see Nodo condicional para expresar el resumen de los impuestos aplicables
         *      cuando este documento sea un anticipo.
         */
        public final void setImpuestos(ArrayList<PImpuestos> value) {
                _impuestos = value;
        }
}
