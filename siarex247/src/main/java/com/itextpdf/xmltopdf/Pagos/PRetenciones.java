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
public class PRetenciones {
        private ArrayList<PRetencion> _pretencion;

        public final ArrayList<PRetencion> getRetencion() {
                return _pretencion;
        }

        /***
         * @param value Requerido
         * @see Nodo requerido para registrar la información detallada de una retención
         *      de impuesto específico.
         */
        public final void setRetencion(ArrayList<PRetencion> value) {
                _pretencion = value;
        }
}
