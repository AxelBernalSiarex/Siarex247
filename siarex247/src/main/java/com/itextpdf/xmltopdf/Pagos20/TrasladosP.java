/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf.Pagos20;

import java.util.ArrayList;

/**
 *
 * @author frack
 */
public class TrasladosP {

    private ArrayList<TrasladoP> _trasladoP = new ArrayList<>();

    /**
     * <p>
     * Nodo condicional para capturar los impuestos trasladados aplicables.
     * </p>
     * Traslado (1, Ilimitado)
     */
    public final ArrayList<TrasladoP> getTrasladoP() {
        return _trasladoP;
    }

    public final void setTrasladoP(ArrayList<TrasladoP> value) {
        _trasladoP = value;
    }

}
