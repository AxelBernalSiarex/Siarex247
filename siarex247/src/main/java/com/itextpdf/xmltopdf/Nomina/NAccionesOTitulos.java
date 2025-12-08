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
public class NAccionesOTitulos {
        private String _valorMercado = "";
        private double _precioAlOtorgarse = 0;

        public final String getValorMercado() {
                return _valorMercado;
        }

        /**
         * @param value Requerido
         * @see Expresar el valor de mercado de las Acciones o Títulos valor al ejercer
         *      la opción.
         */
        public final void setValorMercado(String value) {
                _valorMercado = value;
        }

        public final double getPrecioAlOtorgarse() {
                return _precioAlOtorgarse;
        }

        /**
         * @param value Requerido
         * @see expresar el precio establecido al otorgarse la opción de ingresos en
         *      acciones o títulos valor.
         */
        public final void setPrecioAlOtorgarse(double value) {
                _precioAlOtorgarse = value;
        }

}
