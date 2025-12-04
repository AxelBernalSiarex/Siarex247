/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

/**
 *
 * @author Faustino
 */
public class CuentaPredialC {
        private String _numero = "";

        public final String getNumero() {
                return _numero;
        }

        /**
         * @param value Requerido.
         * @see Atributo requerido para precisar el número de la cuenta predial del
         *      inmueble cubierto por el presente concepto, o bien para incorporar los
         *      datos de identificación del certificado de participación inmobiliaria no
         *      amortizable, tratándose de arrendamiento.
         *      </p>
         */
        public final void setNumero(String value) {
                _numero = value;
        }
}
