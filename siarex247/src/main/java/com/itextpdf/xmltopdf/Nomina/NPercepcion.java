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
public class NPercepcion {
        private String _tipoPercepcion = "";
        private String _clave = "";
        private String _concepto = "";
        private double _importeGravado = 0;
        private double _importeExento = 0;
        private NAccionesOTitulos _accionesOTitulos; // Opcional
        private ArrayList<NHorasExtra> _horasExtra = new ArrayList<>(); // Opcional

        public final String getTipoPercepcion() {
                return _tipoPercepcion;
        }

        /**
         * @param value Requerido
         * @see Expresar la Clave agrupadora bajo la cual se clasifica la percepción.
         */
        public final void setTipoPercepcion(String value) {
                _tipoPercepcion = value;
        }

        public final String getClave() {
                return _clave;
        }

        /**
         * @param value Requerido
         * @see expresar la clave de percepción de nómina propia de la contabilidad de
         *      cada patrón, puede conformarse desde 3 hasta 15 caracteres.
         *      Longitud Mínima 3, Longitud Máxima 15, Patrón [^|]{3,15}
         */
        public final void setClave(String value) {
                _clave = value;
        }

        public final String getConcepto() {
                return _concepto;
        }

        /**
         * @param value Requerido
         * @see Requerido para la descripción del concepto de percepción
         */
        public final void setConcepto(String value) {
                _concepto = value;
        }

        public final double getImporteGravado() {
                return _importeGravado;
        }

        /**
         * @param value Requerido
         * @see Representa el importe gravado de un concepto de percepción.
         */
        public final void setImporteGravado(double value) {
                _importeGravado = value;
        }

        public final double getImporteExento() {
                return _importeExento;
        }

        /**
         * @param value Requerido
         * @see representa el importe exento de un concepto de percepción.
         */
        public final void setImporteExento(double value) {
                _importeExento = value;
        }

        public final NAccionesOTitulos getAccionesOTitulos() {
                return _accionesOTitulos;
        }

        /**
         * @param value Opcional
         * @see Nodo condicional para expresar ingresos por acciones o títulos valor que
         *      representan bienes. Se vuelve requerido cuando existan ingresos por
         *      sueldos derivados de adquisición de acciones o títulos (Art. 94,
         *      fracción VII LISR).
         */
        public final void setAccionesOTitulos(NAccionesOTitulos value) {
                _accionesOTitulos = value;
        }

        public final ArrayList<NHorasExtra> getHorasExtras() {
                return _horasExtra;
        }

        /**
         * @param value Opcional
         * @see Nodo condicional para expresar las horas extra aplicables.
         */
        public final void setHorasExtras(ArrayList<NHorasExtra> value) {
                _horasExtra = value;
        }
}
