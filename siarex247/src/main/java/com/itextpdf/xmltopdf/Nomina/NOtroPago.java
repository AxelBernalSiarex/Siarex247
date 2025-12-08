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
public class NOtroPago {
        private String _tipoOtroPago = "";
        private String _clave = "";
        private String _concepto = "";
        private double _importe = 0;
        private NSubsidioAlEmpleo _subsidioAlEmpleo;
        public NCompensacionSaldosAFavor _compensacionSaldosAFavor;

        public final String getTipoOtroPago() {
                return _tipoOtroPago;
        }

        /**
         * @param value Requerido
         * @see Expresar la clave agrupadora bajo la cual se clasifica el otro pago
         */
        public final void setTipoOtroPago(String value) {
                _tipoOtroPago = value;
        }

        public final String getClave() {
                return _clave;
        }

        /**
         * @param value Requerido
         * @see Representa la clave de otro pago de nómina propia de la contabilidad de
         *      cada patrón, puede conformarse desde 3 hasta 15 caracteres.
         */
        public final void setClave(String value) {
                _clave = value;
        }

        public final String getConcepto() {
                return _concepto;
        }

        /**
         * @param value Requerido
         * @see la descripción del concepto de otro pago.
         */
        public final void setConcepto(String value) {
                _concepto = value;
        }

        public final double getImporte() {
                return _importe;
        }

        /**
         * @param value Requerido
         * @see Expresar el importe del concepto de otro pago.
         */
        public final void setImporte(double value) {
                _importe = value;
        }

        public final NSubsidioAlEmpleo getSubsidioAlEmpleo() {
                return _subsidioAlEmpleo;
        }

        /**
         * @param value Opcional
         * @see Nodo requerido para expresar la información referente al subsidio al
         *      empleo del trabajador.
         *      SubsidioAlEmpleo (0, 1)
         */
        public final void setSubsidioAlEmpleo(NSubsidioAlEmpleo value) {
                _subsidioAlEmpleo = value;
        }

        public final NCompensacionSaldosAFavor getCompensacionSaldosAFavor() {
                return _compensacionSaldosAFavor;
        }

        /**
         * @param value Opcional
         * @see Nodo condicional para expresar la información referente a la
         *      compensación de saldos a favor de un trabajador
         *      CompensacionSaldosAFavor (0, 1)
         */
        public final void setCompensacionSaldosAFavor(NCompensacionSaldosAFavor value) {
                _compensacionSaldosAFavor = value;
        }
}
