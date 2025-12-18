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
public class NJubilacionPensionRetiro {
        private double _totalUnaExhibicion = 0; // condicional
        private double _totalParcialidad = 0; // condicional
        private double _montoDiario = 0; // condicional
        private double _ingresoAcumulable = 0;
        private double _ingresoNoAcumulable = 0;

        public final double getTotalUnaExhibicion() {
                return _totalUnaExhibicion;
        }

        /**
         * @param value Opcional
         * @see Indica el monto total del pago cuando se realiza en una sola exhibición.
         */
        public final void setTotalUnaExhibicion(double value) {
                _totalUnaExhibicion = value;
        }

        public final double getTotalParcialidad() {
                return _totalParcialidad;
        }

        /**
         * @param value Opcional
         * @see expresar los ingresos totales por pago cuando se hace en parcialidades.
         */
        public final void setTotalParcialidad(double value) {
                _totalParcialidad = value;
        }

        public final double getMontoDiario() {
                return _montoDiario;
        }

        /**
         * @param value Opcional
         * @see expresar el monto diario percibido por jubilación, pensiones o haberes
         *      de retiro cuando se realiza en parcialidades.
         */
        public final void setMontoDiario(double value) {
                _montoDiario = value;
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
         * @see expresar los ingresos no acumulables.
         */
        public final void setIngresoNoAcumulable(double value) {
                _ingresoNoAcumulable = value;
        }
}
