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
public class NDeducciones {
        private double _totalOtrasDeducciones = 0; // Condicional
        private double _totalImpuestosRetenidos = 0; // Condicional
        private ArrayList<NDeduccion> _deduccion;

        public final double getTotalOtrasDeducciones() {
                return _totalOtrasDeducciones;
        }

        /**
         * @param value Opcional
         * @see expresar el total de deducciones que se relacionan en el comprobante,
         *      donde la clave de tipo de deducción sea distinta a la 002
         *      correspondiente a ISR.
         */
        public final void setTotalOtrasDeducciones(double value) {
                _totalOtrasDeducciones = value;
        }

        public final double getTotalImpuestosRetenidos() {
                return _totalImpuestosRetenidos;
        }

        /**
         * @param value Opcional
         * @see expresar el total de los impuestos federales retenidos, es decir, donde
         *      la clave de tipo de deducción sea 002 correspondiente a ISR.
         */
        public final void setTotalImpuestosRetenidos(double value) {
                _totalImpuestosRetenidos = value;
        }

        public final ArrayList<NDeduccion> getDeduccion() {
                return _deduccion;
        }

        /**
         * @param value Requerido
         * @see Nodo requerido para expresar la información detallada de una deducción.
         */
        public final void setDeduccion(ArrayList<NDeduccion> value) {
                _deduccion = value;
        }

}
