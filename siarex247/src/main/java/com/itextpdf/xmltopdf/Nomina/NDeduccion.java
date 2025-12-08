/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Nomina;

/**
 *
 * @author Faustino
 */
public class NDeduccion {
        private String _tipoDeduccion = "";
        private String _clave = "";
        private String _concepto = "";
        private double _importe = 0;

        public final String getTipoDeduccion() {
                return _tipoDeduccion;
        }

        /**
         * @param value Requerido
         * @see registrar la clave agrupadora que clasifica la deducción.
         */
        public final void setTipoDeduccion(String value) {
                _tipoDeduccion = value;
        }

        public final String getClave() {
                return _clave;
        }

        /**
         * @param value Requerido
         * @see la clave de deducción de nómina propia de la contabilidad de cada
         *      patrón, puede conformarse desde 3 hasta 15 caracteres.
         */
        public final void setClave(String value) {
                _clave = value;
        }

        public final String getConcepto() {
                return _concepto;
        }

        /**
         * @param value Requerido
         * @see la descripción del concepto de deducción.
         */
        public final void setConcepto(String value) {
                _concepto = value;
        }

        public final double getImporte() {
                return _importe;
        }

        /**
         * @param value Requerido
         * @see registrar el importe del concepto de deducción.
         */
        public final void setImporte(double value) {
                _importe = value;
        }
}
