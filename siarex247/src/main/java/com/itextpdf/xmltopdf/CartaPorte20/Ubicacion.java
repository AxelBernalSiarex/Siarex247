package com.itextpdf.xmltopdf.CartaPorte20;

/**
 *
 * @author frack
 */
import java.time.*;

public class Ubicacion {

    private Domicilio domicilio;
    private String tipoUbicacion = "";
    private String iDUbicacion = "";
    private String rFCRemitenteDestinatario = "";
    private String nombreRemitenteDestinatario = "";
    private String numRegIdTrib = "";
    private String residenciaFiscal = "";
    private String numEstacion = "";
    private String nombreEstacion = "";
    private String navegacionTrafico = "";
    private LocalDateTime fechaHoraSalidaLlegada = LocalDateTime.MIN;
    private String tipoEstacion = "";
    private double distanciaRecorrida = 0;

    public final Domicilio getDomicilio() {
        return this.domicilio;
    }

    public final void setDomicilio(Domicilio value) {
        this.domicilio = value;
    }

    public final String getTipoUbicacion() {
        return this.tipoUbicacion;
    }

    public final void setTipoUbicacion(String value) {
        this.tipoUbicacion = value;
    }

    public final String getIDUbicacion() {
        return this.iDUbicacion;
    }

    public final void setIDUbicacion(String value) {
        this.iDUbicacion = value;
    }

    public final String getRFCRemitenteDestinatario() {
        return this.rFCRemitenteDestinatario;
    }

    public final void setRFCRemitenteDestinatario(String value) {
        this.rFCRemitenteDestinatario = value;
    }

    public final String getNombreRemitenteDestinatario() {
        return this.nombreRemitenteDestinatario;
    }

    public final void setNombreRemitenteDestinatario(String value) {
        this.nombreRemitenteDestinatario = value;
    }

    public final String getNumRegIdTrib() {
        return this.numRegIdTrib;
    }

    public final void setNumRegIdTrib(String value) {
        this.numRegIdTrib = value;
    }

    public final String getResidenciaFiscal() {
        return this.residenciaFiscal;
    }

    public final void setResidenciaFiscal(String value) {
        this.residenciaFiscal = value;
    }

    public final String getNumEstacion() {
        return this.numEstacion;
    }

    public final void setNumEstacion(String value) {
        this.numEstacion = value;
    }

    public final String getNombreEstacion() {
        return this.nombreEstacion;
    }

    public final void setNombreEstacion(String value) {
        this.nombreEstacion = value;
    }

    public final String getNavegacionTrafico() {
        return this.navegacionTrafico;
    }

    public final void setNavegacionTrafico(String value) {
        this.navegacionTrafico = value;
    }

    public final LocalDateTime getFechaHoraSalidaLlegada() {
        return this.fechaHoraSalidaLlegada;
    }

    public final void setFechaHoraSalidaLlegada(LocalDateTime value) {
        this.fechaHoraSalidaLlegada = value;
    }

    public final String getTipoEstacion() {
        return this.tipoEstacion;
    }

    public final void setTipoEstacion(String value) {
        this.tipoEstacion = value;
    }

    public final double getDistanciaRecorrida() {
        return this.distanciaRecorrida;
    }

    public final void setDistanciaRecorrida(double value) {
        this.distanciaRecorrida = value;
    }

}
