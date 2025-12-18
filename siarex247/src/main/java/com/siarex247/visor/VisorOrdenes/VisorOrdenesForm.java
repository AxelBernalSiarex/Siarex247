package com.siarex247.visor.VisorOrdenes;

import com.siarex247.seguridad.Usuarios.UsuariosForm;

public class VisorOrdenesForm {

	
	private long folioOrden;
	private long folioEmpresa;
	private int claveProveedor;
	private String razonSocial;
	private String rfc;
	private String rfcEmisorXML;
	private String rfcReceptorXML;
	
	private String descripcion;
	private String tipoMoneda;
	private String monto;
	private String servicioRecibido;
	private String estatusOrden;
	private String desEstatus;
	private String serieFolio;
	private String serieFinal;
	private String folioXML;
	private String total;
	private String subTotal;
	private String iva;
	private String ivaRet;
	private String isrRet;
	private String impLocales;
	private String tieneXML;
	private String tienePDF;
	private String nombreXML;
	private String nombrePDF;
	private String nombreTXT;
	private String tieneComplementoXML;
	private String tieneComplementoPDF;
	private String tieneCartaPortePDF;
	private String tieneCartaPorteXML;
	private String tieneNotaCreditoXML;
	private String tieneNotaCreditoPDF;
	private String totalNC;
	private String pagoTotal;
	private String ivaRetNC;
	private String uuid;
	private String fechaPago;
	private String asignarA;
	private String fechaUltimoMovimiento;
	private String estadoCFDI;
	private String estatusCFDI;
	private String usoCFDI;
	private String desUsoCFDI;
	private String claveProducto;
	private String omitirComplemento;
	private String numeroCuentaProveedor;
	private String centroCostosProveedor;
	private String tipoOrden;
	private String fechaFactura;
	private String fechaUUID;
	private String bandActivoCDFI;
	private String tipoValidacionPro;
	private String tipoProveedor;
	private String pagoDolares;
	private String conServicio;
	private String tipoCambio;
	private String nombreArchivo;
	
	private String uuidFactura;
	private String uuidComplemento;
	private String uuidNotaCredito;
	
	private boolean isProveedor;
	private boolean soloConsulta;
	
	public long getFolioOrden() {
		return folioOrden;
	}
	public void setFolioOrden(long folioOrden) {
		this.folioOrden = folioOrden;
	}
	public long getFolioEmpresa() {
		return folioEmpresa;
	}
	public void setFolioEmpresa(long folioEmpresa) {
		this.folioEmpresa = folioEmpresa;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getServicioRecibido() {
		return servicioRecibido;
	}
	public void setServicioRecibido(String servicioRecibido) {
		this.servicioRecibido = servicioRecibido;
	}
	public String getDesEstatus() {
		return desEstatus;
	}
	public void setDesEstatus(String desEstatus) {
		this.desEstatus = desEstatus;
	}
	public String getSerieFolio() {
		return serieFolio;
	}
	public void setSerieFolio(String serieFolio) {
		this.serieFolio = serieFolio;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}
	public String getIva() {
		return iva;
	}
	public void setIva(String iva) {
		this.iva = iva;
	}
	public String getIvaRet() {
		return ivaRet;
	}
	public void setIvaRet(String ivaRet) {
		this.ivaRet = ivaRet;
	}
	public String getIsrRet() {
		return isrRet;
	}
	public void setIsrRet(String isrRet) {
		this.isrRet = isrRet;
	}
	public String getImpLocales() {
		return impLocales;
	}
	public void setImpLocales(String impLocales) {
		this.impLocales = impLocales;
	}
	public String getTieneXML() {
		return tieneXML;
	}
	public void setTieneXML(String tieneXML) {
		this.tieneXML = tieneXML;
	}
	public String getTienePDF() {
		return tienePDF;
	}
	public void setTienePDF(String tienePDF) {
		this.tienePDF = tienePDF;
	}
	public String getTieneComplementoXML() {
		return tieneComplementoXML;
	}
	public void setTieneComplementoXML(String tieneComplementoXML) {
		this.tieneComplementoXML = tieneComplementoXML;
	}
	public String getTieneComplementoPDF() {
		return tieneComplementoPDF;
	}
	public void setTieneComplementoPDF(String tieneComplementoPDF) {
		this.tieneComplementoPDF = tieneComplementoPDF;
	}
	public String getTieneCartaPortePDF() {
		return tieneCartaPortePDF;
	}
	public void setTieneCartaPortePDF(String tieneCartaPortePDF) {
		this.tieneCartaPortePDF = tieneCartaPortePDF;
	}
	public String getTieneCartaPorteXML() {
		return tieneCartaPorteXML;
	}
	public void setTieneCartaPorteXML(String tieneCartaPorteXML) {
		this.tieneCartaPorteXML = tieneCartaPorteXML;
	}
	public String getTieneNotaCreditoXML() {
		return tieneNotaCreditoXML;
	}
	public void setTieneNotaCreditoXML(String tieneNotaCreditoXML) {
		this.tieneNotaCreditoXML = tieneNotaCreditoXML;
	}
	public String getTieneNotaCreditoPDF() {
		return tieneNotaCreditoPDF;
	}
	public void setTieneNotaCreditoPDF(String tieneNotaCreditoPDF) {
		this.tieneNotaCreditoPDF = tieneNotaCreditoPDF;
	}
	public String getTotalNC() {
		return totalNC;
	}
	public void setTotalNC(String totalNC) {
		this.totalNC = totalNC;
	}
	public String getPagoTotal() {
		return pagoTotal;
	}
	public void setPagoTotal(String pagoTotal) {
		this.pagoTotal = pagoTotal;
	}
	public String getIvaRetNC() {
		return ivaRetNC;
	}
	public void setIvaRetNC(String ivaRetNC) {
		this.ivaRetNC = ivaRetNC;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getAsignarA() {
		return asignarA;
	}
	public void setAsignarA(String asignarA) {
		this.asignarA = asignarA;
	}
	public String getFechaUltimoMovimiento() {
		return fechaUltimoMovimiento;
	}
	public void setFechaUltimoMovimiento(String fechaUltimoMovimiento) {
		this.fechaUltimoMovimiento = fechaUltimoMovimiento;
	}
	public String getEstadoCFDI() {
		return estadoCFDI;
	}
	public void setEstadoCFDI(String estadoCFDI) {
		this.estadoCFDI = estadoCFDI;
	}
	public String getEstatusCFDI() {
		return estatusCFDI;
	}
	public void setEstatusCFDI(String estatusCFDI) {
		this.estatusCFDI = estatusCFDI;
	}
	
	public String getClaveProducto() {
		return claveProducto;
	}
	public void setClaveProducto(String claveProducto) {
		this.claveProducto = claveProducto;
	}
	
	public String getUsoCFDI() {
		return usoCFDI;
	}
	public void setUsoCFDI(String usoCFDI) {
		this.usoCFDI = usoCFDI;
	}
	public String getEstatusOrden() {
		return estatusOrden;
	}
	public void setEstatusOrden(String estatusOrden) {
		this.estatusOrden = estatusOrden;
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
	public int getClaveProveedor() {
		return claveProveedor;
	}
	public void setClaveProveedor(int claveProveedor) {
		this.claveProveedor = claveProveedor;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getOmitirComplemento() {
		return omitirComplemento;
	}
	public void setOmitirComplemento(String omitirComplemento) {
		this.omitirComplemento = omitirComplemento;
	}
	public String getNumeroCuentaProveedor() {
		return numeroCuentaProveedor;
	}
	public void setNumeroCuentaProveedor(String numeroCuentaProveedor) {
		this.numeroCuentaProveedor = numeroCuentaProveedor;
	}
	public String getCentroCostosProveedor() {
		return centroCostosProveedor;
	}
	public void setCentroCostosProveedor(String centroCostosProveedor) {
		this.centroCostosProveedor = centroCostosProveedor;
	}
	public String getTipoOrden() {
		return tipoOrden;
	}
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}
	public String getFolioXML() {
		return folioXML;
	}
	public void setFolioXML(String folioXML) {
		this.folioXML = folioXML;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public String getFechaUUID() {
		return fechaUUID;
	}
	public void setFechaUUID(String fechaUUID) {
		this.fechaUUID = fechaUUID;
	}
	public String getDesUsoCFDI() {
		return desUsoCFDI;
	}
	public void setDesUsoCFDI(String desUsoCFDI) {
		this.desUsoCFDI = desUsoCFDI;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getBandActivoCDFI() {
		return bandActivoCDFI;
	}
	public void setBandActivoCDFI(String bandActivoCDFI) {
		this.bandActivoCDFI = bandActivoCDFI;
	}
	public String getTipoValidacionPro() {
		return tipoValidacionPro;
	}
	public void setTipoValidacionPro(String tipoValidacionPro) {
		this.tipoValidacionPro = tipoValidacionPro;
	}
	public String getTipoProveedor() {
		return tipoProveedor;
	}
	public void setTipoProveedor(String tipoProveedor) {
		this.tipoProveedor = tipoProveedor;
	}
	public String getPagoDolares() {
		return pagoDolares;
	}
	public void setPagoDolares(String pagoDolares) {
		this.pagoDolares = pagoDolares;
	}
	public String getConServicio() {
		return conServicio;
	}
	public void setConServicio(String conServicio) {
		this.conServicio = conServicio;
	}
	public String getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(String tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public String getSerieFinal() {
		return serieFinal;
	}
	public void setSerieFinal(String serieFinal) {
		this.serieFinal = serieFinal;
	}
	public String getNombreTXT() {
		return nombreTXT;
	}
	public void setNombreTXT(String nombreTXT) {
		this.nombreTXT = nombreTXT;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getRfcEmisorXML() {
		return rfcEmisorXML;
	}
	public void setRfcEmisorXML(String rfcEmisorXML) {
		this.rfcEmisorXML = rfcEmisorXML;
	}
	public String getRfcReceptorXML() {
		return rfcReceptorXML;
	}
	public void setRfcReceptorXML(String rfcReceptorXML) {
		this.rfcReceptorXML = rfcReceptorXML;
	}
	public String getUuidComplemento() {
		return uuidComplemento;
	}
	public String getUuidNotaCredito() {
		return uuidNotaCredito;
	}
	public void setUuidComplemento(String uuidComplemento) {
		this.uuidComplemento = uuidComplemento;
	}
	public void setUuidNotaCredito(String uuidNotaCredito) {
		this.uuidNotaCredito = uuidNotaCredito;
	}
	public String getUuidFactura() {
		return uuidFactura;
	}
	public void setUuidFactura(String uuidFactura) {
		this.uuidFactura = uuidFactura;
	}
	public boolean isProveedor() {
		return isProveedor;
	}
	public void setProveedor(boolean isProveedor) {
		this.isProveedor = isProveedor;
	}
	public boolean isSoloConsulta() {
		return soloConsulta;
	}
	public void setSoloConsulta(boolean soloConsulta) {
		this.soloConsulta = soloConsulta;
	}

	
}
