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
public class NSubsidioAlEmpleo {
        private double _subsidioCausado = 0;

        public final double getSubsidioCausado() {
                return _subsidioCausado;
        }

        /**
         * @param value Requerido
         * @see Expresar el subsidio causado conforme a la tabla del subsidio para el
         *      empleo publicada en el Anexo 8 de la RMF vigente
         */
        public final void setSubsidioCausado(double value) {
                _subsidioCausado = value;
        }
}
