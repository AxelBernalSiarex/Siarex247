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
public class NSeparacionIndemnizacion {
        private double _totalPagado = 0;
        private int _numAnosServicio = -1;
        private double _ultimoSueldoMensOrd = 0;
        private double _ingresoAcumulable = 0;
        private double _ingresoNoAcumulable = 0;

        public final double getTotalPagado() {
                return _totalPagado;
        }

        /**
         * @param value Requerido
         * @see indica el monto total del pago.
         */
        public final void setTotalPagado(double value) {
                _totalPagado = value;
        }

        public final int getNumAnosServicio() {
                return _numAnosServicio;
        }

        /**
         * @param value Requerido
         * @see expresar el número de años de servicio del trabajador. Se redondea al
         *      entero superior si la cifra contiene años y meses y hay más de 6 meses.
         *      Valor Mínimo Incluyente 0, Valor Máximo Incluyente 99
         */
        public final void setNumAnosServicio(int value) {
                _numAnosServicio = value;
        }

        public final double getUltimoSueldoMensOrd() {
                return _ultimoSueldoMensOrd;
        }

        /**
         * @param value Requerido
         * @see e indica el último sueldo mensual ordinario.
         */
        public final void setUltimoSueldoMensOrd(double value) {
                _ultimoSueldoMensOrd = value;
        }

        public final double getIngresoAcumulable() {
                return _ingresoAcumulable;
        }

        /**
         * @param value Requerido
         * @see expresar los ingresos acumulables.
         */
        public final void setIngresoAcumulable(double value) {
                _ingresoAcumulable = value;
        }

        public final double getIngresoNoAcumulable() {
                return _ingresoNoAcumulable;
        }

        /**
         * @param value Requerido
         * @see indica los ingresos no acumulables.
         */
        public final void setIngresoNoAcumulable(double value) {
                _ingresoNoAcumulable = value;
        }
}
