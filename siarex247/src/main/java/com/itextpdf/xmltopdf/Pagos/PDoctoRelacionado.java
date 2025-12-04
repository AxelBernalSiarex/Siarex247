/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos;

/**
 *
 * @author Faustino
 */
public class PDoctoRelacionado {
        private String _idDocumento = "";
        private String _serie = "";
        private String _folio = "";
        private String _monedaDR = "";
        private double _tipoCambioDR = 0;
        private String _metodoDePagoDR = "";
        private String _numParcialidad = "";
        private double _impSaldoAnt = 0;
        private double _impPagado = 0;
        private double _impSaldoInsoluto = 0;

        public final String getIdDocumento() {
                return _idDocumento;
        }

        /**
         * @param value Requerido
         * @see expresar el identificador del documento relacionado con el pago. Este
         *      dato puede ser un Folio Fiscal de la Factura Electrónica o bien el
         *      número de operación de un documento digital.
         */
        public final void setIdDocumento(String value) {
                _idDocumento = value;
        }

        public final String getSerie() {
                return _serie;
        }

        /**
         * @param value Opcional
         * @see precisar la serie del comprobante para control interno del
         *      contribuyente, acepta una cadena de caracteres.
         */
        public final void setSerie(String value) {
                _serie = value;
        }

        public final String getFolio() {
                return _folio;
        }

        /**
         * @param value Opcional
         * @see precisar el folio del comprobante para control interno del
         *      contribuyente, acepta una cadena de caracteres.
         */
        public final void setFolio(String value) {
                _folio = value;
        }

        public final String getMonedaDR() {
                return _monedaDR;
        }

        /**
         * @param value Requerido
         * @see identificar la clave de la moneda utilizada en los importes del
         *      documento relacionado, cuando se usa moneda nacional o el documento
         *      relacionado no especifica la moneda se registra MXN. Los importes
         *      registrados en los atributos "ImpSaldoAnt”, “ImpPagado” e
         *      “ImpSaldoInsoluto” de éste nodo, deben corresponder a esta moneda.
         *      Conforme con la especificación ISO 4217. </para>
         */
        public final void setMonedaDR(String value) {
                _monedaDR = value;
        }

        public final double getTipoCambioDR() {
                return _tipoCambioDR;
        }

        /**
         * @param value Opcional
         * @see expresar el tipo de cambio conforme con la moneda registrada en el
         *      documento relacionado. Es requerido cuando la moneda del documento
         *      relacionado es distinta de la moneda de pago. Se debe registrar el
         *      número de unidades de la moneda señalada en el documento relacionado que
         *      equivalen a una unidad de la moneda del pago. Por ejemplo: El documento
         *      relacionado se registra en USD El pago se realiza por 100 EUR. Este
         *      atributo se registra como 1.114700 USD/EUR. El importe pagado equivale a
         *      100 EUR * 1.114700 USD/EUR = 111.47 USD.
         */
        public final void setTipoCambioDR(double value) {
                _tipoCambioDR = value;
        }

        public final String getMetodoDePagoDR() {
                return _metodoDePagoDR;
        }

        /**
         * @param value Requerido
         * @see expresar la clave del método de pago que se registró en el documento
         *      relacionado.
         */
        public final void setMetodoDePagoDR(String value) {
                _metodoDePagoDR = value;
        }

        public final String getNumParcialidad() {
                return _numParcialidad;
        }

        /**
         * @param value Opcional
         * @see expresar el número de parcialidad que corresponde al pago. Es requerido
         *      cuando MetodoDePagoDR contiene: "PPD” Pago en parcialidades o
         *      diferido.</para>
         */
        public final void setNumParcialidad(String value) {
                _numParcialidad = value;
        }

        public final double getImpSaldoAnt() {
                return _impSaldoAnt;
        }

        /**
         * @param value Opcional
         * @see expresar el monto del saldo insoluto de la parcialidad anterior. Es
         *      requerido cuando MetodoDePagoDR contiene: "PPD” Pago en parcialidades o
         *      diferido.En el caso de que sea la primer parcialidad este campo debe
         *      contener el importe total del documento relacionado.</para>
         */
        public final void setImpSaldoAnt(double value) {
                _impSaldoAnt = value;
        }

        public final double getImpPagado() {
                return _impPagado;
        }

        /**
         * @param value Opcional
         * @see expresar el importe pagado para el documento relacionado. Es obligatorio
         *      cuando exista más de un documento relacionado o cuando existe un
         *      documento relacionado y el TipoCambioDR tiene un valor.
         */
        public final void setImpPagado(double value) {
                _impPagado = value;
        }

        public final double getImpSaldoInsoluto() {
                return _impSaldoInsoluto;
        }

        /**
         * @param value Opcional
         * @see Expresar la diferencia entre el importe del saldo anterior y el monto
         *      del pago. Es requerido cuando MetodoDePagoDR contiene: "PPD” Pago en
         *      parcialidades o diferido.</para>
         */
        public final void setImpSaldoInsoluto(double value) {
                _impSaldoInsoluto = value;
        }
}
