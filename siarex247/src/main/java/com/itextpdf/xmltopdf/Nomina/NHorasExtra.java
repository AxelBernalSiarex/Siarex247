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
public class NHorasExtra {
        private int _dias = 0;
        private int _tipoHoras = -1;
        private int _horasExtra = -1;
        private double _importePagado = 0;

        public int getDias() {
                return _dias;
        }

        /**
         * @param value Requerido
         * @see expresar el número de días en que el trabajador realizó horas extra en
         *      el periodo.
         */
        public void setDias(int value) {
                _dias = value;
        }

        public int getTipoHoras() {
                return _tipoHoras;
        }

        /**
         * @param value Requerido
         * @see expresar el tipo de pago de las horas extra.
         */
        public final void setTipoHoras(int value) {
                _tipoHoras = value;
        }

        public final int getHorasExtra() {
                return _horasExtra;
        }

        /**
         * @param value Requerido
         * @see expresar el número de horas extra trabajadas en el periodo.
         */
        public final void setHorasExtra(int value) {
                _horasExtra = value;
        }

        public final double getImportePagado() {
                return _importePagado;
        }

        /**
         * @param value Requerido
         * @see expresar el importe pagado por las horas extra
         */
        public final void setImportePagado(double value) {
                _importePagado = value;
        }
}
