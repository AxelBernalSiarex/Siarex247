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
public class PRetencion {
        private String _impuesto = "";
        private double _importe = 0;

        public final String getImpuesto() {
                return _impuesto;
        }

        /**
         * @param value Requerido
         * @see señalar la clave del tipo de impuesto retenido.
         */
        public final void setImpuesto(String value) {
                _impuesto = value;
        }

        public final double getImporte() {
                return _importe;
        }

        /**
         * @param value Requerido
         * @see señalar el importe o monto del impuesto retenido. No se permiten valores
         *      negativos.
         */
        public final void setImporte(double value) {
                _importe = value;
        }
}
