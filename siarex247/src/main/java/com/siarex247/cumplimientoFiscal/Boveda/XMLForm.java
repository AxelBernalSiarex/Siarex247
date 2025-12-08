package com.siarex247.cumplimientoFiscal.Boveda;


public class XMLForm {
	private String uuid = ""; 
	private String serie        = "";
    private String folio        = "";
	private String fechaFactura = "";
	private String formaPago    = "";
	private String metodoPago   = "";
	private String tipoMoneda = "";
	private String desTipoMoneda = "";	
	private String numeroCuentaPago = ""; 
	private double subTotal     = 0;
	private double descuento    = 0; 
	private double totalImpuestoRet = 0; 
	private double totalImpuestoTranslado = 0; 
	private double total        = 0; 
	private String emisorRFC    = "";  
	private String  emisorNombre = ""; 
	private String receptorRFC  = ""; 
	private String receptorNombre = "";
	private double retencionIVA = 0;
	private double transladoIVA = 0;
	private double retencionISR = 0;
	private double transladoIEPS = 0;
	private String estadoSAT = "";
	private String estatusSAT = "";
	private String nombreXML = "";
	private String tipoComprobante = "";
	private String fechaPago = "";
	private String fechaTimbrado = "";
	
	//NUEVAS VARIABLES
	String formaPagoDesc = null;
	String metodoPagoDesc = null;
	String desMoneda = null;
	String cadMoneda = null;
	String fechaComprobante = null;
	String lugarExpedicion = null;
	String usoCFDI = null;
	String usoCFDIDesc = null;
	String regimenFiscal = null;
	String regimenFiscalDesc = null;
	String claveUnidad = null;
	String claveUnidadDesc = null;
	String claveProdServ = null;
	String claveProdServDesc = null;
	double cantidad = 0;
	double valorUnitario = 0;
	String descripcion = null;
	double importe = 0;
	String claveImpuestoTras = null;
	double importeTraslado = 0;
	String claveImpuestoTrasDesc = null;
	String baseTraslado = null;
	double tasaCuotaTraslado = 0;
	
	String claveImpuestoRet = null;
	String claveImpuestoRetDesc = null;
	double importeRetencion = 0;
	String tasaCuotaRet = null;
	double tipoCambio = 0;
	String tipoFactorRet = null;
	String tipoFactorTras = null;
	String impLocTotalTras = null;
	String impLocTasaTras = null;
	String impLocTasaRet = null;
	String impLocNombreRet = null;
	String impLocImporteTras = null;
	String impLocTotalRet = null;
	String impLocImporteRet = null;
	String impLocNombreTras = null;
	double totaImpuestosRet = 0;
	double totaImpuestosTras = 0;
	
	String totalIvaTras = null;
	String totalIsrRet = null;
	String totalIvaRet = null;
	String totalIepsRet = null;
	String totalIepsTras = null;
	String baseRetencion = null;
	String numIdentificacion = null;
	String unidad = null;
	String tipoRelacion = null;
	String tipoRelacionDesc = null;
	String uuidRelacionado = null;
	String fechaCancelacion = null;
	String estatus = null;
	String proceso = null;
	String tipoDocumento = null;
	String validez = null;
	String estadoPagado = null;
	String observaciones = null;
	String referencia = null;

	String asociadoBancos = null;
	String asociadoComercial = null;
	String asociadoContabilidad = null;
	String estadoCancelacionDocumento = null;
	String responsable = null;
	String annioComprobante = null;
	String versionComprobante = null;
	String mesComprobante = null;
	String numCertificadoSat = null;
	String numCtaPago = null;
	String lugarExpDesc = null;
	String annioTimbrado = null;
	String mesTimbrado = null;
	String receptorResidenciaFiscal = null;
	String receptorResidenciaFiscalDesc = null;
	String receptorNumRegistroTributario = null;
	String monedaDesc = null;
	String totalDescuento = null;
	String tipoDeComprobante = null;
	String tipoDeComprobanteDesc = null;
	String guid = null;
	String idPago = null;
	String condicionesPago = null;

	//Tipo I
	String drSerie = null;
	String drFolio = null;
	String drMoneda = null;
	String drMonedaDesc = null;
	double drSaldoAnterior = 0;
	double drImportePagado = 0;
	double drSaldoInsoluto = 0;
	String drIdentificador = null;
	String fechaCancelacionDoc = null;
	String estadoCancelacionDoc = null;
	String versionComplePago = null;
	String annioFechaPago = null;
	String mesFechaPago = null;
	String monedaPagoDesc = null;
	String numOperacionPago = null;
	String RfcBancoOrden = null;
	String nombreBancoExt = null;
	String ctaBancoOrden = null;
	String rfcBancoBenef = null;
	String tipoCadenaPago = null;
	String tipoCadenaPagoDesc = null;
	String certificadoPago = null;
	String cadenaPago = null;
	String selloPago = null;
	String drTipoCambio = null;
	String drMetodoPago = null;
	String drMetodoPagoDesc = null;
	String drNumParcialidad = null;

	public String getFormaPagoDesc() {
		return formaPagoDesc;
	}
	public void setFormaPagoDesc(String formaPagoDesc) {
		this.formaPagoDesc = formaPagoDesc;
	}
	public String getMetodoPagoDesc() {
		return metodoPagoDesc;
	}
	public void setMetodoPagoDesc(String metodoPagoDesc) {
		this.metodoPagoDesc = metodoPagoDesc;
	}
	public String getDesMoneda() {
		return desMoneda;
	}
	public void setDesMoneda(String desMoneda) {
		this.desMoneda = desMoneda;
	}
	public String getCadMoneda() {
		return cadMoneda;
	}
	public void setCadMoneda(String cadMoneda) {
		this.cadMoneda = cadMoneda;
	}
	public String getFechaComprobante() {
		return fechaComprobante;
	}
	public void setFechaComprobante(String fechaComprobante) {
		this.fechaComprobante = fechaComprobante;
	}
	public String getLugarExpedicion() {
		return lugarExpedicion;
	}
	public void setLugarExpedicion(String lugarExpedicion) {
		this.lugarExpedicion = lugarExpedicion;
	}
	public String getUsoCFDI() {
		return usoCFDI;
	}
	public void setUsoCFDI(String usoCFDI) {
		this.usoCFDI = usoCFDI;
	}
	public String getUsoCFDIDesc() {
		return usoCFDIDesc;
	}
	public void setUsoCFDIDesc(String usoCFDIDesc) {
		this.usoCFDIDesc = usoCFDIDesc;
	}
	public String getRegimenFiscal() {
		return regimenFiscal;
	}
	public void setRegimenFiscal(String regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}
	public String getRegimenFiscalDesc() {
		return regimenFiscalDesc;
	}
	public void setRegimenFiscalDesc(String regimenFiscalDesc) {
		this.regimenFiscalDesc = regimenFiscalDesc;
	}
	public String getClaveUnidad() {
		return claveUnidad;
	}
	public void setClaveUnidad(String claveUnidad) {
		this.claveUnidad = claveUnidad;
	}
	public String getClaveUnidadDesc() {
		return claveUnidadDesc;
	}
	public void setClaveUnidadDesc(String claveUnidadDesc) {
		this.claveUnidadDesc = claveUnidadDesc;
	}
	public String getClaveProdServ() {
		return claveProdServ;
	}
	public void setClaveProdServ(String claveProdServ) {
		this.claveProdServ = claveProdServ;
	}
	public String getClaveProdServDesc() {
		return claveProdServDesc;
	}
	public void setClaveProdServDesc(String claveProdServDesc) {
		this.claveProdServDesc = claveProdServDesc;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public double getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
	}
	public String getClaveImpuestoTras() {
		return claveImpuestoTras;
	}
	public void setClaveImpuestoTras(String claveImpuestoTras) {
		this.claveImpuestoTras = claveImpuestoTras;
	}
	public String getClaveImpuestoTrasDesc() {
		return claveImpuestoTrasDesc;
	}
	public void setClaveImpuestoTrasDesc(String claveImpuestoTrasDesc) {
		this.claveImpuestoTrasDesc = claveImpuestoTrasDesc;
	}
	public String getBaseTraslado() {
		return baseTraslado;
	}
	public void setBaseTraslado(String baseTraslado) {
		this.baseTraslado = baseTraslado;
	}
	public double getTasaCuotaTraslado() {
		return tasaCuotaTraslado;
	}
	public void setTasaCuotaTraslado(double tasaCuotaTraslado) {
		this.tasaCuotaTraslado = tasaCuotaTraslado;
	}
	public String getClaveImpuestoRet() {
		return claveImpuestoRet;
	}
	public void setClaveImpuestoRet(String claveImpuestoRet) {
		this.claveImpuestoRet = claveImpuestoRet;
	}
	public String getClaveImpuestoRetDesc() {
		return claveImpuestoRetDesc;
	}
	public void setClaveImpuestoRetDesc(String claveImpuestoRetDesc) {
		this.claveImpuestoRetDesc = claveImpuestoRetDesc;
	}
	
	
	public double getImporteRetencion() {
		return importeRetencion;
	}
	public void setImporteRetencion(double importeRetencion) {
		this.importeRetencion = importeRetencion;
	}
	public String getTasaCuotaRet() {
		return tasaCuotaRet;
	}
	public void setTasaCuotaRet(String tasaCuotaRet) {
		this.tasaCuotaRet = tasaCuotaRet;
	}
	public double getTipoCambio() {
		return tipoCambio;
	}
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	public String getTipoFactorRet() {
		return tipoFactorRet;
	}
	public void setTipoFactorRet(String tipoFactorRet) {
		this.tipoFactorRet = tipoFactorRet;
	}
	public String getTipoFactorTras() {
		return tipoFactorTras;
	}
	public void setTipoFactorTras(String tipoFactorTras) {
		this.tipoFactorTras = tipoFactorTras;
	}
	public String getImpLocTotalTras() {
		return impLocTotalTras;
	}
	public void setImpLocTotalTras(String impLocTotalTras) {
		this.impLocTotalTras = impLocTotalTras;
	}
	public String getImpLocTasaTras() {
		return impLocTasaTras;
	}
	public void setImpLocTasaTras(String impLocTasaTras) {
		this.impLocTasaTras = impLocTasaTras;
	}
	public String getImpLocTasaRet() {
		return impLocTasaRet;
	}
	public void setImpLocTasaRet(String impLocTasaRet) {
		this.impLocTasaRet = impLocTasaRet;
	}
	public String getImpLocNombreRet() {
		return impLocNombreRet;
	}
	public void setImpLocNombreRet(String impLocNombreRet) {
		this.impLocNombreRet = impLocNombreRet;
	}
	public String getImpLocImporteTras() {
		return impLocImporteTras;
	}
	public void setImpLocImporteTras(String impLocImporteTras) {
		this.impLocImporteTras = impLocImporteTras;
	}
	public String getImpLocTotalRet() {
		return impLocTotalRet;
	}
	public void setImpLocTotalRet(String impLocTotalRet) {
		this.impLocTotalRet = impLocTotalRet;
	}
	public String getImpLocImporteRet() {
		return impLocImporteRet;
	}
	public void setImpLocImporteRet(String impLocImporteRet) {
		this.impLocImporteRet = impLocImporteRet;
	}
	public String getImpLocNombreTras() {
		return impLocNombreTras;
	}
	public void setImpLocNombreTras(String impLocNombreTras) {
		this.impLocNombreTras = impLocNombreTras;
	}
	
	public double getTotaImpuestosRet() {
		return totaImpuestosRet;
	}
	public void setTotaImpuestosRet(double totaImpuestosRet) {
		this.totaImpuestosRet = totaImpuestosRet;
	}
	public double getTotaImpuestosTras() {
		return totaImpuestosTras;
	}
	public void setTotaImpuestosTras(double totaImpuestosTras) {
		this.totaImpuestosTras = totaImpuestosTras;
	}
	public String getTotalIvaTras() {
		return totalIvaTras;
	}
	public void setTotalIvaTras(String totalIvaTras) {
		this.totalIvaTras = totalIvaTras;
	}
	public String getTotalIsrRet() {
		return totalIsrRet;
	}
	public void setTotalIsrRet(String totalIsrRet) {
		this.totalIsrRet = totalIsrRet;
	}
	public String getTotalIvaRet() {
		return totalIvaRet;
	}
	public void setTotalIvaRet(String totalIvaRet) {
		this.totalIvaRet = totalIvaRet;
	}
	public String getTotalIepsRet() {
		return totalIepsRet;
	}
	public void setTotalIepsRet(String totalIepsRet) {
		this.totalIepsRet = totalIepsRet;
	}
	public String getTotalIepsTras() {
		return totalIepsTras;
	}
	public void setTotalIepsTras(String totalIepsTras) {
		this.totalIepsTras = totalIepsTras;
	}
	public String getBaseRetencion() {
		return baseRetencion;
	}
	public void setBaseRetencion(String baseRetencion) {
		this.baseRetencion = baseRetencion;
	}
	public String getNumIdentificacion() {
		return numIdentificacion;
	}
	public void setNumIdentificacion(String numIdentificacion) {
		this.numIdentificacion = numIdentificacion;
	}
	public String getUnidad() {
		return unidad;
	}
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	public String getTipoRelacion() {
		return tipoRelacion;
	}
	public void setTipoRelacion(String tipoRelacion) {
		this.tipoRelacion = tipoRelacion;
	}
	public String getTipoRelacionDesc() {
		return tipoRelacionDesc;
	}
	public void setTipoRelacionDesc(String tipoRelacionDesc) {
		this.tipoRelacionDesc = tipoRelacionDesc;
	}
	public String getUuidRelacionado() {
		return uuidRelacionado;
	}
	public void setUuidRelacionado(String uuidRelacionado) {
		this.uuidRelacionado = uuidRelacionado;
	}
	public String getFechaCancelacion() {
		return fechaCancelacion;
	}
	public void setFechaCancelacion(String fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	public String getProceso() {
		return proceso;
	}
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
	public String getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getValidez() {
		return validez;
	}
	public void setValidez(String validez) {
		this.validez = validez;
	}
	public String getEstadoPagado() {
		return estadoPagado;
	}
	public void setEstadoPagado(String estadoPagado) {
		this.estadoPagado = estadoPagado;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getAsociadoBancos() {
		return asociadoBancos;
	}
	public void setAsociadoBancos(String asociadoBancos) {
		this.asociadoBancos = asociadoBancos;
	}
	public String getAsociadoComercial() {
		return asociadoComercial;
	}
	public void setAsociadoComercial(String asociadoComercial) {
		this.asociadoComercial = asociadoComercial;
	}
	public String getAsociadoContabilidad() {
		return asociadoContabilidad;
	}
	public void setAsociadoContabilidad(String asociadoContabilidad) {
		this.asociadoContabilidad = asociadoContabilidad;
	}
	public String getEstadoCancelacionDocumento() {
		return estadoCancelacionDocumento;
	}
	public void setEstadoCancelacionDocumento(String estadoCancelacionDocumento) {
		this.estadoCancelacionDocumento = estadoCancelacionDocumento;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getAnnioComprobante() {
		return annioComprobante;
	}
	public void setAnnioComprobante(String annioComprobante) {
		this.annioComprobante = annioComprobante;
	}
	public String getVersionComprobante() {
		return versionComprobante;
	}
	public void setVersionComprobante(String versionComprobante) {
		this.versionComprobante = versionComprobante;
	}
	public String getMesComprobante() {
		return mesComprobante;
	}
	public void setMesComprobante(String mesComprobante) {
		this.mesComprobante = mesComprobante;
	}
	public String getNumCertificadoSat() {
		return numCertificadoSat;
	}
	public void setNumCertificadoSat(String numCertificadoSat) {
		this.numCertificadoSat = numCertificadoSat;
	}
	public String getNumCtaPago() {
		return numCtaPago;
	}
	public void setNumCtaPago(String numCtaPago) {
		this.numCtaPago = numCtaPago;
	}
	public String getLugarExpDesc() {
		return lugarExpDesc;
	}
	public void setLugarExpDesc(String lugarExpDesc) {
		this.lugarExpDesc = lugarExpDesc;
	}
	public String getAnnioTimbrado() {
		return annioTimbrado;
	}
	public void setAnnioTimbrado(String annioTimbrado) {
		this.annioTimbrado = annioTimbrado;
	}
	public String getMesTimbrado() {
		return mesTimbrado;
	}
	public void setMesTimbrado(String mesTimbrado) {
		this.mesTimbrado = mesTimbrado;
	}
	public String getReceptorResidenciaFiscal() {
		return receptorResidenciaFiscal;
	}
	public void setReceptorResidenciaFiscal(String receptorResidenciaFiscal) {
		this.receptorResidenciaFiscal = receptorResidenciaFiscal;
	}
	public String getReceptorResidenciaFiscalDesc() {
		return receptorResidenciaFiscalDesc;
	}
	public void setReceptorResidenciaFiscalDesc(String receptorResidenciaFiscalDesc) {
		this.receptorResidenciaFiscalDesc = receptorResidenciaFiscalDesc;
	}
	public String getReceptorNumRegistroTributario() {
		return receptorNumRegistroTributario;
	}
	public void setReceptorNumRegistroTributario(String receptorNumRegistroTributario) {
		this.receptorNumRegistroTributario = receptorNumRegistroTributario;
	}
	public String getMonedaDesc() {
		return monedaDesc;
	}
	public void setMonedaDesc(String monedaDesc) {
		this.monedaDesc = monedaDesc;
	}
	public String getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(String totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
	public String getTipoDeComprobante() {
		return tipoDeComprobante;
	}
	public void setTipoDeComprobante(String tipoDeComprobante) {
		this.tipoDeComprobante = tipoDeComprobante;
	}
	public String getTipoDeComprobanteDesc() {
		return tipoDeComprobanteDesc;
	}
	public void setTipoDeComprobanteDesc(String tipoDeComprobanteDesc) {
		this.tipoDeComprobanteDesc = tipoDeComprobanteDesc;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getIdPago() {
		return idPago;
	}
	public void setIdPago(String idPago) {
		this.idPago = idPago;
	}
	public String getDrSerie() {
		return drSerie;
	}
	public void setDrSerie(String drSerie) {
		this.drSerie = drSerie;
	}
	public String getDrFolio() {
		return drFolio;
	}
	public void setDrFolio(String drFolio) {
		this.drFolio = drFolio;
	}
	public String getDrMoneda() {
		return drMoneda;
	}
	public void setDrMoneda(String drMoneda) {
		this.drMoneda = drMoneda;
	}
	public String getDrMonedaDesc() {
		return drMonedaDesc;
	}
	public void setDrMonedaDesc(String drMonedaDesc) {
		this.drMonedaDesc = drMonedaDesc;
	}
	
	public double getDrSaldoAnterior() {
		return drSaldoAnterior;
	}
	public void setDrSaldoAnterior(double drSaldoAnterior) {
		this.drSaldoAnterior = drSaldoAnterior;
	}
	
	
	public double getDrImportePagado() {
		return drImportePagado;
	}
	public void setDrImportePagado(double drImportePagado) {
		this.drImportePagado = drImportePagado;
	}
	
	
	
	public double getDrSaldoInsoluto() {
		return drSaldoInsoluto;
	}
	public void setDrSaldoInsoluto(double drSaldoInsoluto) {
		this.drSaldoInsoluto = drSaldoInsoluto;
	}
	public String getDrIdentificador() {
		return drIdentificador;
	}
	public void setDrIdentificador(String drIdentificador) {
		this.drIdentificador = drIdentificador;
	}
	public String getFechaCancelacionDoc() {
		return fechaCancelacionDoc;
	}
	public void setFechaCancelacionDoc(String fechaCancelacionDoc) {
		this.fechaCancelacionDoc = fechaCancelacionDoc;
	}
	public String getEstadoCancelacionDoc() {
		return estadoCancelacionDoc;
	}
	public void setEstadoCancelacionDoc(String estadoCancelacionDoc) {
		this.estadoCancelacionDoc = estadoCancelacionDoc;
	}
	public String getVersionComplePago() {
		return versionComplePago;
	}
	public void setVersionComplePago(String versionComplePago) {
		this.versionComplePago = versionComplePago;
	}
	public String getAnnioFechaPago() {
		return annioFechaPago;
	}
	public void setAnnioFechaPago(String annioFechaPago) {
		this.annioFechaPago = annioFechaPago;
	}
	public String getMesFechaPago() {
		return mesFechaPago;
	}
	public void setMesFechaPago(String mesFechaPago) {
		this.mesFechaPago = mesFechaPago;
	}
	public String getMonedaPagoDesc() {
		return monedaPagoDesc;
	}
	public void setMonedaPagoDesc(String monedaPagoDesc) {
		this.monedaPagoDesc = monedaPagoDesc;
	}
	public String getNumOperacionPago() {
		return numOperacionPago;
	}
	public void setNumOperacionPago(String numOperacionPago) {
		this.numOperacionPago = numOperacionPago;
	}
	public String getRfcBancoOrden() {
		return RfcBancoOrden;
	}
	public void setRfcBancoOrden(String rfcBancoOrden) {
		RfcBancoOrden = rfcBancoOrden;
	}
	public String getNombreBancoExt() {
		return nombreBancoExt;
	}
	public void setNombreBancoExt(String nombreBancoExt) {
		this.nombreBancoExt = nombreBancoExt;
	}
	public String getCtaBancoOrden() {
		return ctaBancoOrden;
	}
	public void setCtaBancoOrden(String ctaBancoOrden) {
		this.ctaBancoOrden = ctaBancoOrden;
	}
	public String getRfcBancoBenef() {
		return rfcBancoBenef;
	}
	public void setRfcBancoBenef(String rfcBancoBenef) {
		this.rfcBancoBenef = rfcBancoBenef;
	}
	public String getTipoCadenaPago() {
		return tipoCadenaPago;
	}
	public void setTipoCadenaPago(String tipoCadenaPago) {
		this.tipoCadenaPago = tipoCadenaPago;
	}
	public String getTipoCadenaPagoDesc() {
		return tipoCadenaPagoDesc;
	}
	public void setTipoCadenaPagoDesc(String tipoCadenaPagoDesc) {
		this.tipoCadenaPagoDesc = tipoCadenaPagoDesc;
	}
	public String getCertificadoPago() {
		return certificadoPago;
	}
	public void setCertificadoPago(String certificadoPago) {
		this.certificadoPago = certificadoPago;
	}
	public String getCadenaPago() {
		return cadenaPago;
	}
	public void setCadenaPago(String cadenaPago) {
		this.cadenaPago = cadenaPago;
	}
	public String getSelloPago() {
		return selloPago;
	}
	public void setSelloPago(String selloPago) {
		this.selloPago = selloPago;
	}
	public String getDrTipoCambio() {
		return drTipoCambio;
	}
	public void setDrTipoCambio(String drTipoCambio) {
		this.drTipoCambio = drTipoCambio;
	}
	public String getDrMetodoPago() {
		return drMetodoPago;
	}
	public void setDrMetodoPago(String drMetodoPago) {
		this.drMetodoPago = drMetodoPago;
	}
	public String getDrMetodoPagoDesc() {
		return drMetodoPagoDesc;
	}
	public void setDrMetodoPagoDesc(String drMetodoPagoDesc) {
		this.drMetodoPagoDesc = drMetodoPagoDesc;
	}
	public String getDrNumParcialidad() {
		return drNumParcialidad;
	}
	public void setDrNumParcialidad(String drNumParcialidad) {
		this.drNumParcialidad = drNumParcialidad;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public String getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	public String getNumeroCuentaPago() {
		return numeroCuentaPago;
	}
	public void setNumeroCuentaPago(String numeroCuentaPago) {
		this.numeroCuentaPago = numeroCuentaPago;
	}
	public String getEmisorRFC() {
		return emisorRFC;
	}
	public void setEmisorRFC(String emisorRFC) {
		this.emisorRFC = emisorRFC;
	}
	public String getEmisorNombre() {
		return emisorNombre;
	}
	public void setEmisorNombre(String emisorNombre) {
		this.emisorNombre = emisorNombre;
	}
	public String getReceptorRFC() {
		return receptorRFC;
	}
	public void setReceptorRFC(String receptorRFC) {
		this.receptorRFC = receptorRFC;
	}
	public String getReceptorNombre() {
		return receptorNombre;
	}
	public void setReceptorNombre(String receptorNombre) {
		this.receptorNombre = receptorNombre;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	public double getTotalImpuestoRet() {
		return totalImpuestoRet;
	}
	public void setTotalImpuestoRet(double totalImpuestoRet) {
		this.totalImpuestoRet = totalImpuestoRet;
	}
	public double getTotalImpuestoTranslado() {
		return totalImpuestoTranslado;
	}
	public void setTotalImpuestoTranslado(double totalImpuestoTranslado) {
		this.totalImpuestoTranslado = totalImpuestoTranslado;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getRetencionIVA() {
		return retencionIVA;
	}
	public void setRetencionIVA(double retencionIVA) {
		this.retencionIVA = retencionIVA;
	}
	public double getTransladoIVA() {
		return transladoIVA;
	}
	public void setTransladoIVA(double transladoIVA) {
		this.transladoIVA = transladoIVA;
	}
	public double getRetencionISR() {
		return retencionISR;
	}
	public void setRetencionISR(double retencionISR) {
		this.retencionISR = retencionISR;
	}
	public double getTransladoIEPS() {
		return transladoIEPS;
	}
	public void setTransladoIEPS(double transladoIEPS) {
		this.transladoIEPS = transladoIEPS;
	}
	public String getTipoMoneda() {
		return tipoMoneda;
	}
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}
	public String getDesTipoMoneda() {
		return desTipoMoneda;
	}
	public void setDesTipoMoneda(String desTipoMoneda) {
		this.desTipoMoneda = desTipoMoneda;
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
	public String getNombreXML() {
		return nombreXML;
	}
	public void setNombreXML(String nombreXML) {
		this.nombreXML = nombreXML;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getFechaTimbrado() {
		return fechaTimbrado;
	}
	public void setFechaTimbrado(String fechaTimbrado) {
		this.fechaTimbrado = fechaTimbrado;
	}
	public String getCondicionesPago() {
		return condicionesPago;
	}
	public void setCondicionesPago(String condicionesPago) {
		this.condicionesPago = condicionesPago;
	}
	public double getImporteTraslado() {
		return importeTraslado;
	}
	public void setImporteTraslado(double importeTraslado) {
		this.importeTraslado = importeTraslado;
	}
	
	
	
}
