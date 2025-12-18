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
public class Emisor {

    private String _rfc = "";
    private String _nombre = "";
    private String _regimenFiscal = "";
    private String _facAtrAdquirente = "";
    private DomicilioFiscal _domicilioFiscal; // Solo es aplicable a la version 3.2
    private ExpedidoEn _expedidoEn; // Solo es aplicable a la version 3.2

    public final String getRfc() {
        return _rfc;
    }

    public DomicilioFiscal getDomicilioFiscal() {
        return _domicilioFiscal;
    }

    public void setDomicilioFiscal(DomicilioFiscal _domicilioFiscal) {
        this._domicilioFiscal = _domicilioFiscal;
    }

    public ExpedidoEn getExpedidoEn() {
        return _expedidoEn;
    }

    public void setExpedidoEn(ExpedidoEn _expedidoEn) {
        this._expedidoEn = _expedidoEn;
    }

    /**
     * @param value Requerido.
     * @see Atributo para precisar la clave del Registro Federal de
     *      Contribuyentes.
     */
    public final void setRfc(String value) {
        _rfc = value;
    }

    public final String getNombre() {
        return _nombre;
    }

    /**
     * @param value Opcional.
     * @see Atributo opcional para registrar el nombre, denominación o razón
     *      social del contribuyente emisor del comprobante.
     */
    public final void setNombre(String value) {
        _nombre = value;
    }

    public final String getRegimenFiscal() {
        return _regimenFiscal;
    }

    /**
     * @param value Requerido.
     * @see Atributo requerido para incorporar la clave del régimen del
     *      contribuyente emisor al que aplicara el efecto fiscal de este
     *      comprobante.
     */
    public final void setRegimenFiscal(String value) {
        _regimenFiscal = value;
    }

    public final String getFacAtrAdquirente() {
        return _facAtrAdquirente;
    }

    /**
     * @param value Requerido.
     * @see Atributo condicional para expresar el número de operación
     *      proporcionado por el SAT cuando se trate de un comprobante a través de
     *      un
     *      PCECFDI o un PCGCFDISP.
     */
    public final void setFacAtrAdquirente(String value) {
        _facAtrAdquirente = value;
    }

    public String getDomicilioFiscalCompleto() {
        StringBuilder domicilio = new StringBuilder();
        if (_domicilioFiscal != null) {
            domicilio.append(this._domicilioFiscal.calle + " ");
            domicilio.append(this._domicilioFiscal.noExterior + " ");
            domicilio.append(this._domicilioFiscal.noInterior + " ");
            domicilio.append(this._domicilioFiscal.colonia + " ");
            domicilio.append(this._domicilioFiscal.localidad + " ");
            domicilio.append(this._domicilioFiscal.municipio + " ");
            domicilio.append(this._domicilioFiscal.estado + " ");
            domicilio.append(this._domicilioFiscal.codigoPostal + " ");
            return domicilio.toString().trim();
        }
        return "";
    }

    public String getExpedidoEnCompleto() {
        StringBuilder expedido = new StringBuilder();
        if (_expedidoEn != null) {
            expedido.append(this._expedidoEn.calle + " ");
            expedido.append(this._expedidoEn.noExterior + " ");
            expedido.append(this._expedidoEn.noInterior + " ");
            expedido.append(this._expedidoEn.colonia + " ");
            expedido.append(this._expedidoEn.localidad + " ");
            expedido.append(this._expedidoEn.municipio + " ");
            expedido.append(this._expedidoEn.estado + " ");
            expedido.append(this._expedidoEn.codigoPostal + " ");
            expedido.append(this._expedidoEn.pais + " ");
            return expedido.toString().trim();
        }
        return "";
    }
}
