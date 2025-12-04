package com.itextpdf.xmltopdf.CartaPorte31;

import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author frack
 */
public class Mercancia {

    private String bienesTransp = "";
    private String claveSTCC = "";
    private String descripcion = "";
    private double cantidad;
    private String claveUnidad = "";
    private String unidad = "";
    private String dimensiones = "";
    private String materialPeligroso = "";
    private String cveMaterialPeligroso = "";
    private String embalaje = "";
    private String descripEmbalaje = "";
    private String sectorCOFEPRIS = "";
    private String nombreIngredienteActivo = "";
    private String nomQuimico = "";
    private String denominacionGenericaProd = "";
    private String denominacionDistintivaProd = "";
    private String fabricante = "";
    private LocalDateTime fechaCaducidad;
    private String loteMedicamento = "";
    private String formaFarmaceutica = "";
    private String condicionesEspTransp = "";
    private String registroSanitarioFolioAutorizacion = "";
    private String permisoImportacion = "";
    private String folioImpoVUCEM = "";
    private String numCAS = "";
    private String razonSocialEmpImp = "";
    private String numRegSanPlagCOFEPRIS = "";
    private String datosFabricante = "";
    private String datosFormulador = "";
    private String datosMaquilador = "";
    private String usoAutorizado = "";
    private double pesoEnKg;
    private double valorMercancia;
    private String moneda = "";
    private String fraccionArancelaria = "";
    private String uUIDComercioExt = "";
    private String tipoMateria = "";
    private String descripcionMateria = "";

    private ArrayList<DocumentacionAduanera> documentacionAduanera;
    private ArrayList<GuiasIdentificacion> guiasIdentificacion;
    private ArrayList<CantidadTransporta> cantidadTransporta;
    private DetalleMercancia detalleMercancia;

    public final ArrayList<DocumentacionAduanera> getDocumentacionAduanera() {
        return this.documentacionAduanera;
    }

    public final void setDocumentacionAduanera(ArrayList<DocumentacionAduanera> value) {
        this.documentacionAduanera = value;
    }

    public final ArrayList<GuiasIdentificacion> getGuiasIdentificacion() {
        return this.guiasIdentificacion;
    }

    public final void setGuiasIdentificacion(ArrayList<GuiasIdentificacion> value) {
        this.guiasIdentificacion = value;
    }

    public final ArrayList<CantidadTransporta> getCantidadTransporta() {
        return this.cantidadTransporta;
    }

    public final void setCantidadTransporta(ArrayList<CantidadTransporta> value) {
        this.cantidadTransporta = value;
    }

    public final DetalleMercancia getDetalleMercancia() {
        return this.detalleMercancia;
    }

    public final void setDetalleMercancia(DetalleMercancia value) {
        this.detalleMercancia = value;
    }

    public final String getBienesTransp() {
        return this.bienesTransp;
    }

    public final void setBienesTransp(String value) {
        this.bienesTransp = value;
    }

    public final String getClaveSTCC() {
        return this.claveSTCC;
    }

    public final void setClaveSTCC(String value) {
        this.claveSTCC = value;
    }

    public final String getDescripcion() {
        return this.descripcion;
    }

    public final void setDescripcion(String value) {
        this.descripcion = value;
    }

    public final double getCantidad() {
        return this.cantidad;
    }

    public final void setCantidad(double value) {
        this.cantidad = value;
    }

    public final String getClaveUnidad() {
        return this.claveUnidad;
    }

    public final void setClaveUnidad(String value) {
        this.claveUnidad = value;
    }

    public final String getUnidad() {
        return this.unidad;
    }

    public final void setUnidad(String value) {
        this.unidad = value;
    }

    public final String getDimensiones() {
        return this.dimensiones;
    }

    public final void setDimensiones(String value) {
        this.dimensiones = value;
    }

    public final String getMaterialPeligroso() {
        return this.materialPeligroso;
    }

    public final void setMaterialPeligroso(String value) {
        this.materialPeligroso = value;
    }

    public final String getCveMaterialPeligroso() {
        return this.cveMaterialPeligroso;
    }

    public final void setCveMaterialPeligroso(String value) {
        this.cveMaterialPeligroso = value;
    }

    public final String getEmbalaje() {
        return this.embalaje;
    }

    public final void setEmbalaje(String value) {
        this.embalaje = value;
    }

    public final String getDescripEmbalaje() {
        return this.descripEmbalaje;
    }

    public final void setDescripEmbalaje(String value) {
        this.descripEmbalaje = value;
    }

    public String getSectorCOFEPRIS() {
        return sectorCOFEPRIS;
    }

    public void setSectorCOFEPRIS(String sectorCOFEPRIS) {
        this.sectorCOFEPRIS = sectorCOFEPRIS;
    }

    public String getNombreIngredienteActivo() {
        return nombreIngredienteActivo;
    }

    public void setNombreIngredienteActivo(String nombreIngredienteActivo) {
        this.nombreIngredienteActivo = nombreIngredienteActivo;
    }

    public String getNomQuimico() {
        return nomQuimico;
    }

    public void setNomQuimico(String nomQuimico) {
        this.nomQuimico = nomQuimico;
    }

    public String getDenominacionGenericaProd() {
        return denominacionGenericaProd;
    }

    public void setDenominacionGenericaProd(String denominacionGenericaProd) {
        this.denominacionGenericaProd = denominacionGenericaProd;
    }

    public String getDenominacionDistintivaProd() {
        return denominacionDistintivaProd;
    }

    public void setDenominacionDistintivaProd(String denominacionDistintivaProd) {
        this.denominacionDistintivaProd = denominacionDistintivaProd;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public LocalDateTime getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDateTime fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getLoteMedicamento() {
        return loteMedicamento;
    }

    public void setLoteMedicamento(String loteMedicamento) {
        this.loteMedicamento = loteMedicamento;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public String getCondicionesEspTransp() {
        return condicionesEspTransp;
    }

    public void setCondicionesEspTransp(String condicionesEspTransp) {
        this.condicionesEspTransp = condicionesEspTransp;
    }

    public String getRegistroSanitarioFolioAutorizacion() {
        return registroSanitarioFolioAutorizacion;
    }

    public void setRegistroSanitarioFolioAutorizacion(String registroSanitarioFolioAutorizacion) {
        this.registroSanitarioFolioAutorizacion = registroSanitarioFolioAutorizacion;
    }

    public String getPermisoImportacion() {
        return permisoImportacion;
    }

    public void setPermisoImportacion(String permisoImportacion) {
        this.permisoImportacion = permisoImportacion;
    }

    public String getFolioImpoVUCEM() {
        return folioImpoVUCEM;
    }

    public void setFolioImpoVUCEM(String folioImpoVUCEM) {
        this.folioImpoVUCEM = folioImpoVUCEM;
    }

    public String getNumCAS() {
        return numCAS;
    }

    public void setNumCAS(String numCAS) {
        this.numCAS = numCAS;
    }

    public String getRazonSocialEmpImp() {
        return razonSocialEmpImp;
    }

    public void setRazonSocialEmpImp(String razonSocialEmpImp) {
        this.razonSocialEmpImp = razonSocialEmpImp;
    }

    public String getNumRegSanPlagCOFEPRIS() {
        return numRegSanPlagCOFEPRIS;
    }

    public void setNumRegSanPlagCOFEPRIS(String numRegSanPlagCOFEPRIS) {
        this.numRegSanPlagCOFEPRIS = numRegSanPlagCOFEPRIS;
    }

    public String getDatosFabricante() {
        return datosFabricante;
    }

    public void setDatosFabricante(String datosFabricante) {
        this.datosFabricante = datosFabricante;
    }

    public String getDatosFormulador() {
        return datosFormulador;
    }

    public void setDatosFormulador(String datosFormulador) {
        this.datosFormulador = datosFormulador;
    }

    public String getDatosMaquilador() {
        return datosMaquilador;
    }

    public void setDatosMaquilador(String datosMaquilador) {
        this.datosMaquilador = datosMaquilador;
    }

    public String getUsoAutorizado() {
        return usoAutorizado;
    }

    public void setUsoAutorizado(String usoAutorizado) {
        this.usoAutorizado = usoAutorizado;
    }

    public final double getPesoEnKg() {
        return this.pesoEnKg;
    }

    public final void setPesoEnKg(double value) {
        this.pesoEnKg = value;
    }

    public final double getValorMercancia() {
        return this.valorMercancia;
    }

    public final void setValorMercancia(double value) {
        this.valorMercancia = value;
    }

    public final String getMoneda() {
        return this.moneda;
    }

    public final void setMoneda(String value) {
        this.moneda = value;
    }

    public final String getFraccionArancelaria() {
        return this.fraccionArancelaria;
    }

    public final void setFraccionArancelaria(String value) {
        this.fraccionArancelaria = value;
    }

    public final String getUUIDComercioExt() {
        return this.uUIDComercioExt;
    }

    public final void setUUIDComercioExt(String value) {
        this.uUIDComercioExt = value;
    }

    public String getTipoMateria() {
        return tipoMateria;
    }

    public void setTipoMateria(String tipoMateria) {
        this.tipoMateria = tipoMateria;
    }

    public String getDescripcionMateria() {
        return descripcionMateria;
    }

    public void setDescripcionMateria(String descripcionMateria) {
        this.descripcionMateria = descripcionMateria;
    }
}
