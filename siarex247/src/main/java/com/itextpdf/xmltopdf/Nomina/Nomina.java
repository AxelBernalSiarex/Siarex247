/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Nomina;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Faustino
 */
public class Nomina {
        private String _version = "1.2";
        private String _tipoNomina = "";
        private LocalDate _fechaPago = LocalDate.now();
        private LocalDate _fechaInicialPago = LocalDate.now();
        private LocalDate _fechaFinalPago = LocalDate.now();
        private double _numDiasPagados = 0;
        private double _totalPercepciones = 0;
        private double _totalDeducciones = 0;
        private double _totalOtrosPagos = 0;
        private NEmisor _emisor = new NEmisor();
        private NReceptor _receptor = new NReceptor();
        private NPercepciones _percepciones = new NPercepciones(); // Condicional
        private NDeducciones _deducciones = new NDeducciones(); // Condicional
        private NOtrosPagos _otrosPagos; // Condicional
        private NIncapacidades _incapacidades = new NIncapacidades();
        private ArrayList<NHorasExtra> _horasExtra;

        public final String getVersion() {
                return _version;
        }

        /**
         * @param value Obligatorio.
         * @see Atributo requerido para la expresión de la versión del complemento.
         *      Valor prefijado a 1.2.
         */
        public final void setVersion(String value) {
                _version = value;
        }

        public final String getTipoNomina() {
                return _tipoNomina;
        }

        /**
         * @param value Obligatorio.
         * @see Atributo requerido para indicar el tipo de nómina, puede ser O= Nómina
         *      ordinaria o E= Nómina extraordinaria.
         */
        public final void setTipoNomina(String value) {
                _tipoNomina = value;
        }

        public final LocalDate getFechaPago() {
                return _fechaPago;
        }

        /**
         * @param value Obligatorio.
         * @see Atributo requerido para la expresión de la fecha efectiva de erogación
         *      del gasto. Se expresa en la forma aaaa-mm-dd.
         */
        public final void setFechaPago(LocalDate value) {
                _fechaPago = value;
        }

        public final LocalDate getFechaInicialPago() {
                return _fechaInicialPago;
        }

        /**
         * @param value Obligatorio.
         * @see Atributo requerido para la expresión de la fecha efectiva de erogación
         *      del gasto. Se expresa en la forma aaaa-mm-dd.
         */
        public final void setFechaInicialPago(LocalDate value) {
                _fechaInicialPago = value;
        }

        public final LocalDate getFechaFinalPago() {
                return _fechaFinalPago;
        }

        /**
         * @param value Obligatorio.
         * @see Atributo requerido para la expresión de la fecha final del período de
         *      pago. Se expresa en la forma aaaa-mm-dd.
         */
        public final void setFechaFinalPago(LocalDate value) {
                _fechaFinalPago = value;
        }

        public final double getNumDiasPagados() {
                return _numDiasPagados;
        }

        /**
         * @param value Obligatorio.
         * @see Atributo requerido para la expresión del número o la fracción de días
         *      pagados. Posiciones decimales 3
         */
        public final void setNumDiasPagados(double value) {
                _numDiasPagados = value;
        }

        public final double getTotalPercepciones() {
                return _totalPercepciones;
        }

        /**
         * @param value Opcional.
         * @see Atributo condicional para representar la suma de las percepciones.
         */
        public final void setTotalPercepciones(double value) {
                _totalPercepciones = value;
        }

        public final double getTotalDeducciones() {
                return _totalDeducciones;
        }

        /**
         * @param value Opcional.
         * @see Atributo condicional para representar la suma de las deducciones
         *      aplicables.
         */
        public final void setTotalDeducciones(double value) {
                _totalDeducciones = value;
        }

        public final double getTotalOtrosPagos() {
                return _totalOtrosPagos;
        }

        /**
         * @param value Opcional.
         * @see Atributo condicional para representar la suma de otros pagos.
         */
        public final void setTotalOtrosPagos(double value) {
                _totalOtrosPagos = value;
        }

        public final NEmisor getEmisor() {
                return _emisor;
        }

        /**
         * @param value Opcional.
         * @see Nodo condicional para expresar la información del contribuyente emisor
         *      del comprobante de nómina.
         */
        public final void setEmisor(NEmisor value) {
                _emisor = value;
        }

        public final NReceptor getReceptor() {
                return _receptor;
        }

        /**
         * @param value Requerido.
         * @see Nodo requerido para precisar la información del contribuyente receptor
         *      del comprobante de nómina.
         */
        public final void setReceptor(NReceptor value) {
                _receptor = value;
        }

        public final NPercepciones getPercepciones() {
                return _percepciones;
        }

        /**
         * @param value Opcional.
         * @see Nodo condicional para expresar las percepciones aplicables.
         */
        public final void setPercepciones(NPercepciones value) {
                _percepciones = value;
        }

        public final NDeducciones getDeducciones() {
                return _deducciones;
        }

        /**
         * @param value Opcional.
         * @see Nodo condicional para expresar las deducciones aplicables.
         */
        public final void setDeducciones(NDeducciones value) {
                _deducciones = value;
        }

        public final NOtrosPagos getOtrosPagos() {
                return _otrosPagos;
        }

        /**
         * @param value Opcional.
         * @see Nodo condicional para expresar otros pagos aplicables.
         */
        public final void setOtrosPagos(NOtrosPagos value) {
                _otrosPagos = value;
        }

        public final NIncapacidades getIncapacidades() {
                return _incapacidades;
        }

        /**
         * @param value Opcional.
         * @see Nodo condicional para expresar información de las incapacidades.
         */
        public final void setIncapacidades(NIncapacidades value) {
                _incapacidades = value;
        }

        public final ArrayList<NHorasExtra> getHorasExtra() {
                return _horasExtra;
        }

        public final void setHorasExtra(ArrayList<NHorasExtra> value) {
                _horasExtra = value;
        }

        public final int Count() {
                throw new UnsupportedOperationException();
        }
}
