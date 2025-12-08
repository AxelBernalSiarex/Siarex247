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
public class NEmisor {
        private String _curp = "";
        private String _registroPatronal = "";
        private String _rfcPatronOrigen = "";
        private NEntidadSNCF _entidadSNCF;

        public final String getCurp() {
                return _curp;
        }

        /**
         * @param value Opcional.
         * @see Atributo condicional para expresar la CURP del emisor del comprobante de
         *      nómina cuando es una persona física.
         */
        public final void setCurp(String value) {
                _curp = value;
        }

        public final String getRegistroPatronal() {
                return _registroPatronal;
        }

        /**
         * @param value Opcional.
         * @see Atributo condicional para expresar el registro patronal, clave de ramo -
         *      pagaduría o la que le asigne la institución de seguridad social al
         *      patrón, a 20 posiciones máximo. Se debe ingresar cuando se cuente con
         *      él, o se esté obligado conforme a otras disposiciones distintas a las
         *      fiscales.
         */
        public final void setRegistroPatronal(String value) {
                _registroPatronal = value;
        }

        public final String getRfcPatronOrigen() {
                return _rfcPatronOrigen;
        }

        /**
         * @param value Opcional.
         * @see Atributo opcional para expresar el RFC de la persona que fungió como
         *      patrón cuando el pago al trabajador se realice a través de un tercero
         *      como vehículo o herramienta de pago.
         */
        public final void setRfcPatronOrigen(String value) {
                _rfcPatronOrigen = value;
        }

        public final NEntidadSNCF getEntidadSNCF() {
                return _entidadSNCF;
        }

        /**
         * @param value Opcional.
         * @see Nodo condicional para que las entidades adheridas al Sistema Nacional de
         *      Coordinación Fiscal realicen la identificación del origen de los
         *      recursos utilizados en el pago de nómina del personal que presta o
         *      desempeña un servicio personal subordinado en las dependencias de la
         *      entidad federativa, del municipio o demarcación territorial de la Ciudad
         *      de México, así como en sus respectivos organismos autónomos y entidades
         *      paraestatales y paramunicipales.
         */
        public final void setEntidadSNCF(NEntidadSNCF value) {
                _entidadSNCF = value;
        }
}
