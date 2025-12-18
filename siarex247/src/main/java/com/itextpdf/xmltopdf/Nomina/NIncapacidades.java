/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Nomina;

import java.util.ArrayList;

/**
 *
 * @author Faustino
 */
public class NIncapacidades {
        private ArrayList<NIncapacidad> _incapacidad;

        public final ArrayList<NIncapacidad> getIncapacidad() {
                return _incapacidad;
        }

        /**
         * @param value Incapacidad (1, Ilimitado)
         * @see Nodo condicional para expresar información de las incapacidades.
         */
        public final void setIncapacidad(ArrayList<NIncapacidad> value) {
                _incapacidad = value;
        }

}
