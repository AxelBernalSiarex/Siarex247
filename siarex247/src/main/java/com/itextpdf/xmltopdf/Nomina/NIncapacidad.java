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
public class NIncapacidad {
        private int _diasIncapacidad = -1;
        private String _tipoIncapacidad = "";
        private double _importeMonetario = 0;

        public final int getDiasIncapacidad() {
                return _diasIncapacidad;
        }

        /**
         * @param value Requerido
         * @see expresar el número de días enteros que el trabajador se incapacitó en el
         *      periodo.
         */
        public final void setDiasIncapacidad(int value) {
                _diasIncapacidad = value;
        }

        public final String getTipoIncapacidad() {
                return _tipoIncapacidad;
        }

        /**
         * @param value Requerido
         * @see Expresar la razón de la incapacidad.
         */
        public final void setTipoIncapacidad(String value) {
                _tipoIncapacidad = value;
        }

        public final double getImporteMonetario() {
                return _importeMonetario;
        }

        /**
         * @param value Opcional.
         * @see expresar el monto del importe monetario de la incapacidad.
         */
        public final void setImporteMonetario(double value) {
                _importeMonetario = value;
        }
}
