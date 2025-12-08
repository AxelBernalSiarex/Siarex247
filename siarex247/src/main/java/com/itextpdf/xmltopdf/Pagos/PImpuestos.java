/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos;

/**
 *
 * @author frack
 */
public class PImpuestos {
        private double _TotalImpuestosRetenidos = 0;
        private double _TotalImpuestosTrasladados = 0;
        private PRetenciones _retenciones;
        private PTraslados _traslados;

        public final double getTotalImpuestosRetenidos() {
                return _TotalImpuestosRetenidos;
        }

        /**
         * @param value Opcional
         * @see Expresar el total de los impuestos retenidos que se desprenden del pago.
         *      No se permiten valores negativos.
         */
        public final void setTotalImpuestosRetenidos(double value) {
                _TotalImpuestosRetenidos = value;
        }

        public final double getTotalImpuestosTrasladados() {
                return _TotalImpuestosTrasladados;
        }

        /**
         * @param value Opcional
         * @see Expresar el total de los impuestos trasladados que se desprenden del
         *      pago. No se permiten valores negativos.
         */
        public final void setTotalImpuestosTrasladados(double value) {
                _TotalImpuestosTrasladados = value;
        }

        public final PRetenciones getRetenciones() {
                return _retenciones;
        }

        /**
         * @param value Opcional
         * @see Nodo condicional para capturar los impuestos retenidos aplicables.
         */
        public final void setRetenciones(PRetenciones value) {
                _retenciones = value;
        }

        public final PTraslados getTraslados() {
                return _traslados;
        }

        /**
         * @param value Opcional
         * @see Nodo condicional para capturar los impuestos trasladados aplicables.
         */
        public final void setTraslados(PTraslados value) {
                _traslados = value;
        }
}
