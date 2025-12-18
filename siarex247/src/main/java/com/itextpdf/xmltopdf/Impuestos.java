/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

import java.util.ArrayList;

/**
 *
 * @author Faustino
 */
public class Impuestos {
        private double _totalImpuestosRetenidos = -1;
        private double _totalImpuestosTrasladados = -1;
        private ArrayList<Retencion> _retenciones = new ArrayList<>();
        private ArrayList<Traslado> _traslados = new ArrayList<>();

        public final double getTotalImpuestosRetenidos() {
                return _totalImpuestosRetenidos;
        }

        /**
         * @param value Opcional.
         * @see Atributo condicional para expresar el total de los impuestos retenidos
         *      que se desprenden de los conceptos expresados en el comprobante fiscal
         *      digital por Internet. No se permiten valores negativos. Es requerido
         *      cuando en los conceptos se registren impuestos retenidos.
         */
        public final void setTotalImpuestosRetenidos(double value) {
                _totalImpuestosRetenidos = value;
        }

        public final double getTotalImpuestosTrasladados() {
                return _totalImpuestosTrasladados;
        }

        /**
         * @param value Opcional.
         * @see Atributo condicional para expresar el total de los impuestos trasladados
         *      que se desprenden de los conceptos expresados en el comprobante fiscal
         *      digital por Internet. No se permiten valores negativos. Es requerido
         *      cuando en los conceptos se registren impuestos trasladados.
         */
        public final void setTotalImpuestosTrasladados(double value) {
                _totalImpuestosTrasladados = value;
        }

        public final ArrayList<Retencion> getRetenciones() {
                return _retenciones;
        }

        /**
         * @param value Opcional.
         * @see Nodo condicional para capturar los impuestos retenidos aplicables. Es
         *      requerido cuando en los conceptos se registre algún impuesto retenido.
         */
        public final void setRetenciones(ArrayList<Retencion> value) {
                _retenciones = value;
        }

        public final ArrayList<Traslado> getTraslados() {
                return _traslados;
        }

        /**
         * @param value Opcional.
         * @see Nodo condicional para capturar los impuestos trasladados aplicables. Es
         *      requerido cuando en los conceptos se registre un impuesto trasladado.
         */
        public final void setTraslados(ArrayList<Traslado> value) {
                _traslados = value;
        }
}
