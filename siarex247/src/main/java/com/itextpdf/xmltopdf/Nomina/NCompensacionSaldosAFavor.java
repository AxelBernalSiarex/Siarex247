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
public class NCompensacionSaldosAFavor {
        private double _saldoAFavor = 0;
        private String _ano = "";
        private double _remanenteSalFav = 0;

        public final double getSaldoAFavor() {
                return _saldoAFavor;
        }

        /**
         * @param value Requerido
         * @see expresar el saldo a favor determinado por el patrón al trabajador en
         *      periodos o ejercicios anteriores.
         */
        public final void setSaldoAFavor(double value) {
                _saldoAFavor = value;
        }

        public final String getAno() {
                return _ano;
        }

        /**
         * @param value Requerido
         * @see expresar el año en que se determinó el saldo a favor del trabajador por
         *      el patrón que se incluye en el campo "RemanenteSalFav”.</para>
         */
        public final void setAno(String value) {
                _ano = value;
        }

        public final double getRemanenteSalFav() {
                return _remanenteSalFav;
        }

        /**
         * @param value Requerido
         * @see expresar el remanente del saldo a favor del trabajador.
         */
        public final void setRemanenteSalFav(double value) {
                _remanenteSalFav = value;
        }
}
