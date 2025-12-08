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
public class NSubContratacion {
        private String _rfcLabora = "";
        private double _porcentajeTiempo = 0;

        public final String getRfcLabora() {
                return _rfcLabora;
        }

        /**
         * @param value Requerido
         * @see el RFC de la persona que subcontrata
         */
        public final void setRfcLabora(String value) {
                _rfcLabora = value;
        }

        public final double getPorcentajeTiempo() {
                return _porcentajeTiempo;
        }

        /**
         * @param value Requerido
         * @see Expresar el porcentaje del tiempo que prestó sus servicios con el RFC
         *      que lo subcontrata
         */
        public final void setPorcentajeTiempo(double value) {
                _porcentajeTiempo = value;
        }
}
