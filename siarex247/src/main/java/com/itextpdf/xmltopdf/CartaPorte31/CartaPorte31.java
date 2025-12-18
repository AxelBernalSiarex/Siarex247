package com.itextpdf.xmltopdf.CartaPorte31;

public class CartaPorte31 {

    private String version = "";
    private String idCCP = "";
    private String transpInternac = "";
    private String regimenAduanero = "";
    private String entradaSalidaMerc = "";
    private String paisOrigenDestino = "";
    private String viaEntradaSalida = "";
    private double totalDistRec = 0;
    private String registroISTMO = "";
    private String ubicacionPoloOrigen = "";
    private String ubicacionPoloDestino = "";

    private Ubicaciones ubicaciones;
    private Mercancias mercancias;
    private FiguraTransporte figuraTransporte;

    public CartaPorte31() {
        this.version = "3.0";
    }

    public final String getVersion() {
        return this.version;
    }

    public final void setVersion(String value) {
        this.version = value;
    }

    public final String getIdCCP() {
        return this.idCCP;
    }

    public final void setIdCCP(String value) {
        this.idCCP = value;
    }

    public final String getTranspInternac() {
        return this.transpInternac;
    }

    public final void setTranspInternac(String value) {
        this.transpInternac = value;
    }

    public final String getRegimenAduanero() {
        return this.regimenAduanero;
    }

    public final void setRegimenAduanero(String value) {
        this.regimenAduanero = value;
    }

    public final String getEntradaSalidaMerc() {
        return this.entradaSalidaMerc;
    }

    public final void setEntradaSalidaMerc(String value) {
        this.entradaSalidaMerc = value;
    }

    public final String getPaisOrigenDestino() {
        return this.paisOrigenDestino;
    }

    public final void setPaisOrigenDestino(String value) {
        this.paisOrigenDestino = value;
    }

    public final String getViaEntradaSalida() {
        return this.viaEntradaSalida;
    }

    public final void setViaEntradaSalida(String value) {
        this.viaEntradaSalida = value;
    }

    public final double getTotalDistRec() {
        return this.totalDistRec;
    }

    public final void setTotalDistRec(double value) {
        this.totalDistRec = value;
    }

    public final String getRegistroISTMO() {
        return this.registroISTMO;
    }

    public final void setRegistroISTMO(String value) {
        this.registroISTMO = value;
    }

    public final String getUbicacionPoloOrigen() {
        return this.ubicacionPoloOrigen;
    }

    public final void setUbicacionPoloOrigen(String value) {
        this.ubicacionPoloOrigen = value;
    }

    public final String getUbicacionPoloDestino() {
        return this.ubicacionPoloDestino;
    }

    public final void setUbicacionPoloDestino(String value) {
        this.ubicacionPoloDestino = value;
    }

    public final Ubicaciones getUbicaciones() {
        return this.ubicaciones;
    }

    public final void setUbicaciones(Ubicaciones value) {
        this.ubicaciones = value;
    }

    public final Mercancias getMercancias() {
        return this.mercancias;
    }

    public final void setMercancias(Mercancias value) {
        this.mercancias = value;
    }

    public final FiguraTransporte getFiguraTransporte() {
        return this.figuraTransporte;
    }

    public final void setFiguraTransporte(FiguraTransporte value) {
        this.figuraTransporte = value;
    }
}
