/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos;

import java.util.ArrayList;

/**
 *
 * @author Faustino
 */
public class Pagos {
        private String _version = "1.0";
        private ArrayList<Pago> _pago = new ArrayList<Pago>();

        /**
         * @param value Requerido
         * @see Indica la versión del complemento para recepción de pagos.
         */
        public final String getVersion() {
                return _version;
        }

        public final void setVersion(String value) {
                _version = value;
        }

        /**
         * @param value Requerido
         * @see Indica la versión del complemento para recepción de pagos.
         */
        public final ArrayList<Pago> getPago() {
                return _pago;
        }

        public final void setPago(ArrayList<Pago> value) {
                _pago = value;
        }
}
