/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.xmltopdf;

import java.time.LocalDateTime;

/**
 *
 * @author Faustino
 *
 *
 */
public class Comprobante {

    private String _version = "4.0";
    private String _serie = "";
    private String _folio = "";
    private LocalDateTime _fecha; // Fecha y hora de expedicion del comprobante
    private String _sello = "";
    private String _formaPago = ""; // Opcional
    private String _noCertificado = "";
    private String _certificado = "";
    private String _condicionesDePago = "";
    private double _subtotal = 0;
    private double _descuento = 0;
    private String _moneda = "";
    private double _tipoCambio = 0;
    private double _total = 0;
    private String _tipoDeComprobante = "";
    private String _exportacion = "";
    private String _metodoPago = ""; // Opcional
    private String _lugarExpedicion = "";
    private String _confirmacion = ""; // Opcional
    private InformacionGlobal _informacionGlobal; // Opcional
    private CfdiRelacionados _cfdiRelacionados; // Opcional
    private Emisor _emisor;
    private Receptor _receptor;
    private Conceptos _conceptos;
    private Impuestos _impuestos;
    private Complemento _complemento;
    private Addenda _addenda;
    private boolean _estaCancelado = false;
    public String _totalLetra = "";
    public Acuse AcuseCancelacion;
    public String XML = "";
    private String _motivoDescuento = ""; // Solo es aplicable a la version 3.2
    private String _numCtaPago = ""; // Solo es aplicable a la version 3.2
    private String _folioFiscalOrig = ""; // Solo es aplicable a la version 3.2
    private String _serieFolioFiscalOrig = ""; // Solo es aplicable a la version 3.2
    private LocalDateTime _fechaFolioFiscalOrig; // Solo es aplicable a la version 3.2
    private double _montoFolioFiscalOrig = 0; // Solo es aplicable a la version 3.2

    public double getMontoFolioFiscalOrig() {
        return _montoFolioFiscalOrig;
    }

    public void setMontoFolioFiscalOrig(double _montoFolioFiscalOrig) {
        this._montoFolioFiscalOrig = _montoFolioFiscalOrig;
    }

    public final String getVersion() {
        return _version;
    }

    /**
     * @param value Requerido.
     * @see Atributo requerido con valor prefijo a 3.3 que indica la versión del
     *      estándar bajo el que se encuentra expresado el comprobante.
     *
     */
    public final void setVersion(String value) {
        _version = value;
    }

    public String getNumCtaPago() {
        return _numCtaPago;
    }

    public void setNumCtaPago(String _numCtaPago) {
        this._numCtaPago = _numCtaPago;
    }

    public String getFolioFiscalOrig() {
        return _folioFiscalOrig;
    }

    public void setFolioFiscalOrig(String _folioFiscalOrig) {
        this._folioFiscalOrig = _folioFiscalOrig;
    }

    public String getSerieFolioFiscalOrig() {
        return _serieFolioFiscalOrig;
    }

    public void setSerieFolioFiscalOrig(String _serieFolioFiscalOrig) {
        this._serieFolioFiscalOrig = _serieFolioFiscalOrig;
    }

    public LocalDateTime getFechaFolioFiscalOrig() {
        return _fechaFolioFiscalOrig;
    }

    public void setFechaFolioFiscalOrig(LocalDateTime _fechaFolioFiscalOrig) {
        this._fechaFolioFiscalOrig = _fechaFolioFiscalOrig;
    }

    public String getMotivoDescuento() {
        return _motivoDescuento;
    }

    public void setMotivoDescuento(String _motivoDescuento) {
        this._motivoDescuento = _motivoDescuento;
    }

    public final String getSerie() {
        return _serie;
    }

    /**
     * @param value Opcional.
     * @see Este atributo acepta una cadena de caracteres.
     */
    public final void setSerie(String value) {
        _serie = value;
    }

    public final String getFolio() {
        return _folio;
    }

    /**
     * @param value Opcional.
     * @see Este atributo acepta una cadena de caracteres.
     */
    public final void setFolio(String value) {
        _folio = value;
    }

    public final LocalDateTime getFecha() {
        return _fecha;
    }

    /**
     * @param value Requerido
     * @see Se expresa en la forma AAAA-MM-DD:Thh:mm:ss
     */
    public final void setFecha(LocalDateTime value) {
        _fecha = value;
    }

    public final String getSello() {
        return _sello;
    }

    /**
     * @param value Requerido.
     * @see Debe ser expresado como una cadeba de texto en formato Base 64.
     */
    public final void setSello(String value) {
        _sello = value;
    }

    public final String getFormaPago() {
        return _formaPago;
    }

    /**
     * @param value Opcional.
     * @see Expresa la clave de la forma de pago.
     */
    public final void setFormaPago(String value) {
        _formaPago = value;
    }

    public final String getNoCertificado() {
        return _noCertificado;
    }

    /**
     * @param value Requerido.
     * @see Expresa el numero de serie del certificado de sello digital que
     *      ampara al comprobante de acuerdo con el acuse correspondiente a 20
     *      posiciones otorgadas por el sistema del SAT.
     */
    public final void setNoCertificado(String value) {
        _noCertificado = value;
    }

    public final String getCertificado() {
        return _certificado;
    }

    /**
     * @param value Requerido.
     * @see El certificado del sello digital que ampara al comprobante, como
     *      texto en formato base 64.
     */
    public final void setCertificado(String value) {
        _certificado = value;
    }

    public final String getCondicionesDePago() {
        return _condicionesDePago;
    }

    /**
     * @param value Opcional.
     * @see Atributo condicional para expresar las condiciones comerciales
     *      aplicables para el pago del CFDI. Este atributo puede ser condicionado
     *      mediante atributos o complementos.
     */
    public final void setCondicionesDePago(String value) {
        _condicionesDePago = value;
    }

    public final double getSubTotal() {
        return _subtotal;
    }

    /**
     * @param value Requerido.
     * @see Representa la suma de los conceptos antes de descuentos e impuestos.
     *      No se permiten valores negativos.
     */
    public final void setSubTotal(double value) {
        _subtotal = value;
    }

    public final double getDescuento() {
        return _descuento;
    }

    /**
     * @param value Opcional.
     * @see Representa el importe total de los descuentos aplicables antes de
     *      impuestos. No se permiten valores negativos. Se debe aplicar cuando
     *      existan conceptos con descuento.
     */
    public final void setDescuento(double value) {
        _descuento = value;
    }

    public final String getMoneda() {
        return _moneda;
    }

    /**
     * @param value Requerido.
     * @see Requerido para identificar la clave de la moneda utilizada para
     *      expresar los montos. Cuando se usa moneda nacional se registra MXN.
     */
    public final void setMoneda(String value) {
        _moneda = value;
    }

    public final double getTipoCambio() {
        return _tipoCambio;
    }

    /**
     * @param value Opcional.
     * @see Es requerido cuando la clave de moneda es distinta de MXN y de XXX.
     */
    public final void setTipoCambio(double value) {
        _tipoCambio = value;
    }

    public final double getTotal() {
        return _total;
    }

    /**
     * @param value Requerido.
     * @see Representa la suma del subtotal, menos los descuentos aplicables,
     *      mas las contribuciones recibidas(impuestos trasladados - federales o
     *      locales, derechos, productos, aprovechamientos, aportaciones de
     *      seguridad
     *      social, contribuciones de mejoras) menos los impuestos retenidos.No se
     *      permiten valores negativos.
     */
    public final void setTotal(double value) {
        _total = value;
    }

    public final String getTipoDeComprobante() {
        return _tipoDeComprobante;
    }

    /**
     * @param value Requerido.
     * @see La clave del efecto del comprobante fiscal para el contribuyente
     *      emisor.
     */
    public final void setTipoDeComprobante(String value) {
        _tipoDeComprobante = value;
    }

    public final String getExportacion() {
        return _exportacion;
    }

    /**
     * @param value Requerido.
     * @see Atributo requerido para expresar si el comprobante ampara una
     *      operación de exportación..
     */
    public final void setExportacion(String value) {
        _exportacion = value;
    }

    public final String getMetodoPago() {
        return _metodoPago;
    }

    /**
     * @param value Opcional.
     * @see Atributos condicional para precisar la clave del metodo de pago que
     *      aplica para este CFDI.
     */
    public final void setMetodoPago(String value) {
        _metodoPago = value;
    }

    public final String getLugarExpedicion() {
        return _lugarExpedicion;
    }

    /**
     * @param value Requerido.
     * @see Incorpora el código postal del lugar de expedición del comprobante.
     *      (Domicilio de la matriz o de la sucursal)
     */
    public final void setLugarExpedicion(String value) {
        _lugarExpedicion = value;
    }

    public final String getConfirmacion() {
        return _confirmacion;
    }

    /**
     * @param value Opcional.
     * @see Atributo condicional para registrar la clave de confirmación que
     *      entrege el PAC para expedir el comprobante con importes grandes.
     */
    public final void setConfirmacion(String value) {
        _confirmacion = value;
    }

    public final InformacionGlobal getInformacionGlobal() {
        return _informacionGlobal;
    }

    /**
     * @param value Opcional.
     * @see Nodo condicional para precisar la información relacionada con el
     *      comprobante global.
     */
    public final void setInformacionGlobal(InformacionGlobal value) {
        _informacionGlobal = value;
    }

    public final CfdiRelacionados getCfdiRelacionados() {
        return _cfdiRelacionados;
    }

    /**
     * @param value Opcional.
     * @see Nodo opcional para precisar la informacion de los comprobante
     *      relacionados.
     */
    public final void setCfdiRelacionados(CfdiRelacionados value) {
        _cfdiRelacionados = value;
    }

    public final Emisor getEmisor() {
        return _emisor;
    }

    /**
     * @param value Requerido.
     * @see Nodo requerido para expresar la informacion del contribuyente emisor
     *      del comprobante.
     */
    public final void setEmisor(Emisor value) {
        _emisor = value;
    }

    public final Receptor getReceptor() {
        return _receptor;
    }

    /**
     * @param value Requerido.
     * @see Nodo requerido para precisar la información del contribuyente
     *      receptor del comprobante.
     */
    public final void setReceptor(Receptor value) {
        _receptor = value;
    }

    public final Conceptos getConceptos() {
        return _conceptos;
    }

    /**
     * @param value Requerido.
     * @see Nodo requerido para listar los conceptos cubiertos por el
     *      comprobante.
     */
    public final void setConceptos(Conceptos value) {
        _conceptos = value;
    }

    public final Impuestos getImpuestos() {
        return _impuestos;
    }

    /**
     * @param value Opcional.
     * @see Nodo condicional para expresar el resumen de los impuestos
     *      aplicables.
     */
    public final void setImpuestos(Impuestos value) {
        _impuestos = value;
    }

    public final Complemento getComplemento() {
        return _complemento;
    }

    /**
     * @param value Opcional.
     * @see Nodo opcional donde se incluye el complemento TImbre Fiscal Digital
     *      de manera obligatoria y los nodos complementarios determinados por el
     *      SAT.
     */
    public final void setComplemento(Complemento value) {
        _complemento = value;
    }

    public final Addenda getAddenda() {
        return _addenda;
    }

    public final void setAddenda(Addenda value) {
        _addenda = value;
    }

    /**
     * ************* Falta agregar el complemento ADDENDA
     * *******************************
     */
    /**
     * @return
     */
    public final String getTotalLetra() {
        return _totalLetra;
    }

    public final void setTotalLetra(String value) {
        _totalLetra = value;
    }

    public final boolean getEstaCancelado() {
        return _estaCancelado;
    }

    public final void setEstaCancelado(boolean value) {
        _estaCancelado = value;
    }

    public final CError EsInfoCorrecta() {
        CError error = new CError();
        if (!_version.equals("3.3")) {
            error.HayError(true);
            error.Error("La version del comprobante no corresponde a la 3.3");
        } else if (_subtotal <= 0) {
            error.HayError(true);
            error.Error("El subtotal no debe ser igual a 0.00");
        } else if (_total <= 0) {
            error.HayError(true);
            error.Error("El total no debe ser igual a 0.00");
        } else if (_moneda.equals("")) {
            error.HayError(true);
            error.Error("Debe especificar la clave de la moneda utilizada");
        } else if (_tipoDeComprobante.equals("")) {
            error.HayError(true);
            error.Error("Debe especificar la clave del tipo de comprobante");
        } else if (_lugarExpedicion.equals("")) {
            error.HayError(true);
            error.Error("Debe especificar el atributo LugarExpedicion");
        } else if (_lugarExpedicion.equals("")) {
            error.HayError(true);
            error.Error("Debe especificar el atributo LugarExpedicion");
        } else if (_emisor.getRfc().equals("")) {
            error.HayError(true);
            error.Error("Debe especificar el atributo Rfc del emisor del comprobante");
        } else if (_emisor.getRegimenFiscal().equals("")) {
            error.HayError(true);
            error.Error("Debe especificar la clave del RegimenFiscal del emisor del comprobante");
        }
        return error;
    }

}
