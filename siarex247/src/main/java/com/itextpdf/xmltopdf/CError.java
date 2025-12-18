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
public class CError {
    private String _error;
    private Boolean _hayError;

    public String Error() {
        return _error;
    }

    public void Error(String error) {
        _error = error;
    }

    public Boolean HayError() {
        return _hayError;
    }

    public void HayError(Boolean error) {
        _hayError = error;
    }

}
