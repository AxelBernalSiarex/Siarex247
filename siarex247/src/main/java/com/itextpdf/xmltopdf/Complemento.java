/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

import com.itextpdf.xmltopdf.Nomina.Nomina;
import com.itextpdf.xmltopdf.Pagos.Pagos;
import com.itextpdf.xmltopdf.Pagos20.Pagos20;
import com.itextpdf.xmltopdf.CartaPorte20.CartaPorte20;
import com.itextpdf.xmltopdf.CartaPorte10.CartaPorte10;
import com.itextpdf.xmltopdf.CartaPorte30.CartaPorte30;
import com.itextpdf.xmltopdf.CartaPorte31.CartaPorte31;

/**
 *
 * @author Faustino
 */
public class Complemento {

    public Pagos Pagos;
    public Nomina Nomina;
    public CartaPorte20 CartaPorte20;
    public CartaPorte10 CartaPorte10;
    public CartaPorte30 CartaPorte30;
    public CartaPorte31 CartaPorte31;
    public Pagos20 Pagos20;
    public TimbreFiscalDigital TimbreFiscalDigital;

    public TimbreFiscalDigital getTimbreFiscalDigital() {
        return TimbreFiscalDigital;
    }

    public void setTimbreFiscalDigital(TimbreFiscalDigital TimbreFiscalDigital) {
        this.TimbreFiscalDigital = TimbreFiscalDigital;
    }

    public Pagos getPagos() {
        return Pagos;
    }

    public void setPagos(Pagos Pagos) {
        this.Pagos = Pagos;
    }

    public Nomina getNomina() {
        return Nomina;
    }

    public void setNomina(Nomina Nomina) {
        this.Nomina = Nomina;
    }

    public CartaPorte10 getCartaPorte10() {
        return CartaPorte10;
    }

    public void setCartaPorte10(CartaPorte10 CartaPorte10) {
        this.CartaPorte10 = CartaPorte10;
    }

    public CartaPorte20 getCartaPorte20() {
        return CartaPorte20;
    }

    public void setCartaPorte30(CartaPorte30 CartaPorte30) {
        this.CartaPorte30 = CartaPorte30;
    }

    public CartaPorte30 getCartaPorte30() {
        return this.CartaPorte30;
    }

    public void setCartaPorte20(CartaPorte20 CartaPorte20) {
        this.CartaPorte20 = CartaPorte20;
    }

    public Pagos20 getPagos20() {
        return Pagos20;
    }

    public void setPagos20(Pagos20 Pagos20) {
        this.Pagos20 = Pagos20;
    }

	public CartaPorte31 getCartaPorte31() {
		return CartaPorte31;
	}

	public void setCartaPorte31(CartaPorte31 cartaPorte31) {
		CartaPorte31 = cartaPorte31;
	}
    
    
}
