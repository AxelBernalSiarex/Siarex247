/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos;

import java.util.ArrayList;

/**
 *
 * @author frack
 */
public class PTraslados {
        private ArrayList<PTraslado> _traslado;

        public final ArrayList<PTraslado> getTraslado() {
                return _traslado;
        }

        /**
         * @see Nodo condicional para capturar los impuestos trasladados aplicables.
         *      Traslado (1, Ilimitado)
         */
        public final void setTraslado(ArrayList<PTraslado> value) {
                _traslado = value;
        }

}