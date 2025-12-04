/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

import java.util.ArrayList;

/**
 *
 * @author frack
 */
public class Conceptos {
        private ArrayList<Concepto> _concepto = new ArrayList<>();

        public final ArrayList<Concepto> getConcepto() {
                return _concepto;
        }

        /**
         * @param value Requerido.
         * @see Nodo requerido para expresar la informacion detallada de un bien o
         *      servicio amparado en el comprobante.
         */
        public final void setConcepto(ArrayList<Concepto> value) {
                _concepto = value;
        }

}
