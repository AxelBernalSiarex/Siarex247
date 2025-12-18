package com.siarex247.validaciones;

public class CartasPorteForm {
	private double idRegistro = 0;
	private long folioEmpresa = 0;
	private long folioOrden = 0;
	private long folioRegistro = 0;
	private String rfc = "";
	private String razonSocial = "";
	private String emailPrimario = "";
	private double montoPagado = 0;
	private String uuid = "";
	private String usoCFDI;
	private String desUsoCFDI;
	private String servicioRecibo;
	private String bandActivo;
	
	private String uuidCarta = "";
	private String tipoOrden = "";
	private String tipoMoneda = "";
	private String fechaPago = "";
	private String fechaTimbrado = "";
	private String fechaPagoXML = "";
	
	private String serieOrden = "";
	
	private double subTotal = 0;
	private double iva = 0;
	private double ivaRet = 0;
	private double impLocales = 0;
	private double total = 0;
	
	private double totalFactura = 0;
	private double totalCarta = 0;
	
	private String estatusFactura = "";
	private String estatusCarta = "";
	private String nombreXML = "";
	private String nombrePDF = "";
	private String estatusConciliacion = "";
	private String estatusConciliacionExcel = "";
	private int claveProveedor = 0;
	
	
	
	private String idCatalogo;
	private String idEtiqueta;
	private String pathXML;
	private String fechaIni;
	private String fechaFin;
	private String subject;
	private String mensaje;
	private String activo;
	private String validarVacio;
	private String tipoComprobante;
	private String estatusOrden;
	
	public double getIdRegistro() {
		return idRegistro;
	}
	public void setIdRegistro(double idRegistro) {
		this.idRegistro = idRegistro;
	}
	
	
	
	public String getEstatusConciliacion() {
		return estatusConciliacion;
	}
	public void setEstatusConciliacion(String estatusConciliacion) {
		this.estatusConciliacion = estatusConciliacion;
	}
	public String getEstatusConciliacionExcel() {
		return estatusConciliacionExcel;
	}
	public void setEstatusConciliacionExcel(String estatusConciliacionExcel) {
		this.estatusConciliacionExcel = estatusConciliacionExcel;
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
	public String getEstatusFactura() {
		return estatusFactura;
	}
	public void setEstatusFactura(String estatusFactura) {
		this.estatusFactura = estatusFactura;
	}
	public String getEstatusCarta() {
		return estatusCarta;
	}
	public void setEstatusCarta(String estatusCarta) {
		this.estatusCarta = estatusCarta;
	}
	public double getTotalFactura() {
		return totalFactura;
	}
	public void setTotalFactura(double totalFactura) {
		this.totalFactura = totalFactura;
	}
	public double getTotalCarta() {
		return totalCarta;
	}
	public void setTotalCarta(double totalCarta) {
		this.totalCarta = totalCarta;
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
	public double getImpLocales() {
		return impLocales;
	}
	public void setImpLocales(double impLocales) {
		this.impLocales = impLocales;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public long getFolioRegistro() {
		return folioRegistro;
	}
	public void setFolioRegistro(long folioRegistro) {
		this.folioRegistro = folioRegistro;
	}
	public long getFolioEmpresa() {
		return folioEmpresa;
	}
	public void setFolioEmpresa(long folioEmpresa) {
		this.folioEmpresa = folioEmpresa;
	}
	public long getFolioOrden() {
		return folioOrden;
	}
	public void setFolioOrden(long folioOrden) {
		this.folioOrden = folioOrden;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getEmailPrimario() {
		return emailPrimario;
	}
	public void setEmailPrimario(String emailPrimario) {
		this.emailPrimario = emailPrimario;
	}
	public double getMontoPagado() {
		return montoPagado;
	}
	public void setMontoPagado(double montoPagado) {
		this.montoPagado = montoPagado;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUuidCarta() {
		return uuidCarta;
	}
	public void setUuidCarta(String uuidCarta) {
		this.uuidCarta = uuidCarta;
	}
	public String getTipoOrden() {
		return tipoOrden;
	}
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}
	public String getSerieOrden() {
		return serieOrden;
	}
	public void setSerieOrden(String serieOrden) {
		this.serieOrden = serieOrden;
	}
	public String getFechaTimbrado() {
		return fechaTimbrado;
	}
	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}
	public String getFechaPagoXML() {
		return fechaPagoXML;
	}
	public void setFechaPagoXML(String fechaPagoXML) {
		this.fechaPagoXML = fechaPagoXML;
	}
	public int getClaveProveedor() {
		return claveProveedor;
	}
	public void setClaveProveedor(int claveProveedor) {
		this.claveProveedor = claveProveedor;
	}
	public String getIdCatalogo() {
		return idCatalogo;
	}
	public void setIdCatalogo(String idCatalogo) {
		this.idCatalogo = idCatalogo;
	}
	public String getIdEtiqueta() {
		return idEtiqueta;
	}
	public void setIdEtiqueta(String idEtiqueta) {
		this.idEtiqueta = idEtiqueta;
	}
	public String getPathXML() {
		return pathXML;
	}
	public void setPathXML(String pathXML) {
		this.pathXML = pathXML;
	}
	public String getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(String fechaIni) {
		this.fechaIni = fechaIni;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	public String getValidarVacio() {
		return validarVacio;
	}
	public void setValidarVacio(String validarVacio) {
		this.validarVacio = validarVacio;
	}
	public String getUsoCFDI() {
		return usoCFDI;
	}
	public void setUsoCFDI(String usoCFDI) {
		this.usoCFDI = usoCFDI;
	}
	public String getDesUsoCFDI() {
		return desUsoCFDI;
	}
	public void setDesUsoCFDI(String desUsoCFDI) {
		this.desUsoCFDI = desUsoCFDI;
	}
	public String getServicioRecibo() {
		return servicioRecibo;
	}
	public void setServicioRecibo(String servicioRecibo) {
		this.servicioRecibo = servicioRecibo;
	}
	public String getBandActivo() {
		return bandActivo;
	}
	public void setBandActivo(String bandActivo) {
		this.bandActivo = bandActivo;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	public String getEstatusOrden() {
		return estatusOrden;
	}
	public void setEstatusOrden(String estatusOrden) {
		this.estatusOrden = estatusOrden;
	}
	


	
	
}
