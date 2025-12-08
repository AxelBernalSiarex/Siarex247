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
public class NOtrosPagos {
        private ArrayList<NOtroPago> _otroPago;

        public final ArrayList<NOtroPago> getOtroPago() {
                return _otroPago;
        }

        /**
         * @see Nodo condicional para expresar otros pagos aplicables.
         */
        public final void setOtroPago(ArrayList<NOtroPago> value) {
                _otroPago = value;
        }
}
