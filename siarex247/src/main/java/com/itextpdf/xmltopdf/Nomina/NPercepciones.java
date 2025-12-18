/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Nomina;

import java.util.ArrayList;

/**
 *
 * @author Faustino
 */

public class NPercepciones {
        private double _totalSueldos = 0; // Condicional
        private double _totalSeparacionIndemnizacion = 0; // Opcional
        private double _totalJubilacionPensionRetiro = 0; // Opcional
        private double _totalGravado = 0;
        private double _totalExento = 0;
        private ArrayList<NPercepcion> _percepcion;
        private NJubilacionPensionRetiro _jubilacionPensionRetiro;
        private NSeparacionIndemnizacion _separacionIndeminzacion;

        public final double getTotalSueldos() {
                return _totalSueldos;
        }

        /**
         * @param value Opcional
         * @see Expresar el total de percepciones brutas (gravadas y exentas) por
         *      sueldos y salarios y conceptos asimilados a salarios
         */
        public final void setTotalSueldos(double value) {
                _totalSueldos = value;
        }

        public final double getTotalSeparacionIndemnizacion() {
                return _totalSeparacionIndemnizacion;
        }

        /**
         * @param value Opcional
         * @see expresar el importe exento y gravado de las claves tipo percepción 022
         *      Prima por Antigüedad, 023 Pagos por separación y 025 Indemnizaciones.
         */
        public final void setTotalSeparacionIndemnizacion(double value) {
                _totalSeparacionIndemnizacion = value;
        }

        public final double getTotalJubilacionPensionRetiro() {
                return _totalJubilacionPensionRetiro;
        }

        /**
         * @param value Opcional
         * @see expresar el importe exento y gravado de las claves tipo percepción 039
         *      Jubilaciones, pensiones o haberes de retiro en una exhibición y 044
         *      Jubilaciones, pensiones o haberes de retiro en parcialidades.
         */
        public final void setTotalJubilacionPensionRetiro(double value) {
                _totalJubilacionPensionRetiro = value;
        }

        public final double getTotalGravado() {
                return _totalGravado;
        }

        /**
         * @param value Requerido
         * @see Expresar el total de percepciones gravadas que se relacionan en el
         *      comprobante.
         */
        public final void setTotalGravado(double value) {
                _totalGravado = value;
        }

        public final double getTotalExento() {
                return _totalExento;
        }

        /**
         * @param value Requerido
         * @see expresar el total de percepciones exentas que se relacionan en el
         *      comprobante
         */
        public final void setTotalExento(double value) {
                _totalExento = value;
        }

        public final ArrayList<NPercepcion> getPercepcion() {
                return _percepcion;
        }

        /**
         * @param value Nodo condicional para expresar la informacion detallada de pagos
         *              por jubilacion, pensiones o haberes de retiro
         * @see Percepcion(minimon, maximo)
         *      Percepcion(1, Ilimitado)
         */
        public final void setPercepcion(ArrayList<NPercepcion> value) {
                _percepcion = value;
        }

        public final NJubilacionPensionRetiro getJubilacionPensionRetiro() {
                return _jubilacionPensionRetiro;
        }

        /**
         * @param value JubilacionPensionRetiro(minimo, maximo)
         * @see JubilacionPensionRetiro(0,1)
         */
        public final void setJubilacionPensionRetiro(NJubilacionPensionRetiro value) {
                _jubilacionPensionRetiro = value;
        }

        public final NSeparacionIndemnizacion getSeparacionIndeminzacion() {
                return _separacionIndeminzacion;
        }

        /**
         * @param value SeparacionIndeminzacion(minimo, maximo)
         * @see SeparacionIndeminzacion(0, 1)
         */
        public final void setSeparacionIndeminzacion(NSeparacionIndemnizacion value) {
                _separacionIndeminzacion = value;
        }
}
