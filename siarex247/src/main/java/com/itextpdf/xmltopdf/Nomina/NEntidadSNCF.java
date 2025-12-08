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
public class NEntidadSNCF {
        private String _origenRecurso = "";
        private double _montoRecursoPropio = 0;

        public final String getOrigenRecurso() {
                return _origenRecurso;
        }

        /**
         * @param value Requerido.
         * @see Atributo requerido para identificar el origen del recurso utilizado para
         *      el pago de nómina del personal que presta o desempeña un servicio
         *      personal subordinado o asimilado a salarios en las dependencias.
         */
        public final void setOrigenRecurso(String value) {
                _origenRecurso = value;
        }

        public final double getMontoRecursoPropio() {
                return _montoRecursoPropio;
        }

        /**
         * @param value Opcional.
         * @see Atributo condicional para expresar el monto del recurso pagado con cargo
         *      a sus participaciones u otros ingresos locales (importe bruto de los
         *      ingresos propios, es decir total de gravados y exentos), cuando el
         *      origen es mixto.
         */
        public final void setMontoRecursoPropio(double value) {
                _montoRecursoPropio = value;
        }
}
