/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos;

/**
 *
 * @author frack
 */
public class PTraslado {
        private String _impuesto = "";
        private String _tipoFactor = "";
        private double _tasaOCuota = 0;
        private double _importe = 0;

        public final String getImpuesto() {
                return _impuesto;
        }

        /**
         * @param value Requerido</value>
         * @see Señalar la clave del tipo de impuesto trasladado.
         */
        public final void setImpuesto(String value) {
                _impuesto = value;
        }

        public final String getTipoFactor() {
                return _tipoFactor;
        }

        /**
         * @param value Requerido</value>
         * @see señalar la clave del tipo de factor que se aplica a la base del
         *      impuesto.
         */
        public final void setTipoFactor(String value) {
                _tipoFactor = value;
        }

        public final double getTasaOCuota() {
                return _tasaOCuota;
        }

        /**
         * @param value Requerido</value>
         * @see Señalar el valor de la tasa o cuota del impuesto que se traslada.
         */
        public final void setTasaOCuota(double value) {
                _tasaOCuota = value;
        }

        public final double getImporte() {
                return _importe;
        }

        /**
         * @param value Requerido</value>
         * @see Señalar el importe del impuesto trasladado. No se permiten valores
         *      negativos.
         */
        public final void setImporte(double value) {
                _importe = value;
        }
}
