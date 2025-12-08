package com.siarex247.layOut.OrdenesCompra;

public class CargaOrdenesForm {

	
	private long numeroFolioSistema = 0;
	private long numeroFolioEmpresa = 0;
	private String concepto = "";
	private double monto = 0;
	private double cantidadFacturada = 0;
	private String usuario = "";
	private String estatus = "";
	private String servicioProducto = "";
	private String tipoMoneda = "";
	private String tipoCambio = "";
	private String rfc = "";
	private String numRecibo = "";

	private String serie = "";
	private double total = 0;
	private double subTotal = 0;
	private double iva = 0;
	private double ivaRet = 0;
	private double isrRet = 0;
	private double impLocales = 0;
	private String nombreXML = "";
	private String nombrePDF = "";
	private String mensajeValidacion = "";
	private String subjectValidacion = "";
	private String uuid = "";
	private String fechaUUID = "";
	private String folioXML = "";
	private String serieFolio = "";
	private String monedaXML = "";
	private String rfcEmisor = "";
	private String razonSocialEmisor = "";
	private String idVendor = "";
	
	private String asignarTO = "";
	
	private String serieXML = "";
	private String numeroRequisicion = "";
	private String tipoOrden = "";
	private int claveProveedor = 0;
	private String estadoSAT = "";
	private String estatusSAT = "";
	private String fechaFact = "";
	private String fechaPago = "";
	private String usoCFDI = "";
	private String claveProdServ = "";
	private String estatusCFDI = "";
	private String centroCostosProveedor = null;
	private String numeroCuenta = null;
	
	private String existeXML = "N";
	private String existePDF = "N";
	
	
	public long getNumeroFolioSistema() {
		return numeroFolioSistema;
	}
	public void setNumeroFolioSistema(long numeroFolioSistema) {
		this.numeroFolioSistema = numeroFolioSistema;
	}
	public long getNumeroFolioEmpresa() {
		return numeroFolioEmpresa;
	}
	public void setNumeroFolioEmpresa(long numeroFolioEmpresa) {
		this.numeroFolioEmpresa = numeroFolioEmpresa;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public double getCantidadFacturada() {
		return cantidadFacturada;
	}
	public void setCantidadFacturada(double cantidadFacturada) {
		this.cantidadFacturada = cantidadFacturada;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getServicioProducto() {
		return servicioProducto;
	}
	public void setServicioProducto(String servicioProducto) {
		this.servicioProducto = servicioProducto;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getNumRecibo() {
		return numRecibo;
	}
	public void setNumRecibo(String numRecibo) {
		this.numRecibo = numRecibo;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public double getIva() {
		return iva;
	}
	public void setIva(double iva) {
		this.iva = iva;
	}
	public double getIvaRet() {
		return ivaRet;
	}
	public void setIvaRet(double ivaRet) {
		this.ivaRet = ivaRet;
	}
	public double getIsrRet() {
		return isrRet;
	}
	public void setIsrRet(double isrRet) {
		this.isrRet = isrRet;
	}
	public double getImpLocales() {
		return impLocales;
	}
	public void setImpLocales(double impLocales) {
		this.impLocales = impLocales;
	}
	public String getNombreXML() {
		return nombreXML;
	}
	public void setNombreXML(String nombreXML) {
		this.nombreXML = nombreXML;
	}
	public String getNombrePDF() {
		return nombrePDF;
	}
	public void setNombrePDF(String nombrePDF) {
		this.nombrePDF = nombrePDF;
	}
	public String getMensajeValidacion() {
		return mensajeValidacion;
	}
	public void setMensajeValidacion(String mensajeValidacion) {
		this.mensajeValidacion = mensajeValidacion;
	}
	public String getSubjectValidacion() {
		return subjectValidacion;
	}
	public void setSubjectValidacion(String subjectValidacion) {
		this.subjectValidacion = subjectValidacion;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getFechaUUID() {
		return fechaUUID;
	}
	public void setFechaUUID(String fechaUUID) {
		this.fechaUUID = fechaUUID;
	}
	public String getFolioXML() {
		return folioXML;
	}
	public void setFolioXML(String folioXML) {
		this.folioXML = folioXML;
	}
	public String getSerieFolio() {
		return serieFolio;
	}
	public void setSerieFolio(String serieFolio) {
		this.serieFolio = serieFolio;
	}
	public String getMonedaXML() {
		return monedaXML;
	}
	public void setMonedaXML(String monedaXML) {
		this.monedaXML = monedaXML;
	}
	public String getRfcEmisor() {
		return rfcEmisor;
	}
	public void setRfcEmisor(String rfcEmisor) {
		this.rfcEmisor = rfcEmisor;
	}
	public String getRazonSocialEmisor() {
		return razonSocialEmisor;
	}
	public void setRazonSocialEmisor(String razonSocialEmisor) {
		this.razonSocialEmisor = razonSocialEmisor;
	}
	public String getIdVendor() {
		return idVendor;
	}
	public void setIdVendor(String idVendor) {
		this.idVendor = idVendor;
	}
	public String getAsignarTO() {
		return asignarTO;
	}
	public void setAsignarTO(String asignarTO) {
		this.asignarTO = asignarTO;
	}
	public String getSerieXML() {
		return serieXML;
	}
	public void setSerieXML(String serieXML) {
		this.serieXML = serieXML;
	}
	public String getNumeroRequisicion() {
		return numeroRequisicion;
	}
	public void setNumeroRequisicion(String numeroRequisicion) {
		this.numeroRequisicion = numeroRequisicion;
	}
	public String getTipoOrden() {
		return tipoOrden;
	}
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}
	public int getClaveProveedor() {
		return claveProveedor;
	}
	public void setClaveProveedor(int claveProveedor) {
		this.claveProveedor = claveProveedor;
	}
	public String getEstadoSAT() {
		return estadoSAT;
	}
	public void setEstadoSAT(String estadoSAT) {
		this.estadoSAT = estadoSAT;
	}
	public String getEstatusSAT() {
		return estatusSAT;
	}
	public void setEstatusSAT(String estatusSAT) {
		this.estatusSAT = estatusSAT;
	}
	public String getFechaFact() {
		return fechaFact;
	}
	public void setFechaFact(String fechaFact) {
		this.fechaFact = fechaFact;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getUsoCFDI() {
		return usoCFDI;
	}
	public void setUsoCFDI(String usoCFDI) {
		this.usoCFDI = usoCFDI;
	}
	public String getClaveProdServ() {
		return claveProdServ;
	}
	public void setClaveProdServ(String claveProdServ) {
		this.claveProdServ = claveProdServ;
	}
	public String getEstatusCFDI() {
		return estatusCFDI;
	}
	public void setEstatusCFDI(String estatusCFDI) {
		this.estatusCFDI = estatusCFDI;
	}
	public String getCentroCostosProveedor() {
		return centroCostosProveedor;
	}
	public void setCentroCostosProveedor(String centroCostosProveedor) {
		this.centroCostosProveedor = centroCostosProveedor;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public String getExisteXML() {
		return existeXML;
	}
	public void setExisteXML(String existeXML) {
		this.existeXML = existeXML;
	}
	public String getExistePDF() {
		return existePDF;
	}
	public void setExistePDF(String existePDF) {
		this.existePDF = existePDF;
	}
	
	
	
}
