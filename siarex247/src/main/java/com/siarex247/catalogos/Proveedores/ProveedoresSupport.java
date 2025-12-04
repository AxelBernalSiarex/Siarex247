package com.siarex247.catalogos.Proveedores;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class ProveedoresSupport extends ActionDB {

	
	private static final long serialVersionUID = 7608636222640659563L;

	
	private int claveRegistro = 0;
	private int claveAcceso = 0;
	private String razonSocial = "";
	private String idProveedor = "";
	private String rfc = "";
	private String domicilio = "";
	private String calle = "";
	private String numeroInt = "";
	private String numeroExt = "";
	private String colonia = "";
	private String delegacion = "";
	private String ciudad = "";
	private String estado = "";
	private int codigoPostal = 0;
	private String telefono = "";
	private String extencion = "";
	private String nombreContacto = "";
	private String email = "";
	private String tipoProveedor = "";
	private String tipoConfirmacion = "";
	
	private String banco = "";
	private String sucursal = "";
	private String nombreSucursal = "";
	private String numeroCuenta = "";
	private String cuentaClabe = "";
	private String numeroConvenio = "";
	private String moneda = "";
	private String aba = "";
	private String switfCode = "";
	
	private String bancoDollar = "";
	private String sucursalDollar = "";
	private String nombreSucursalDollar = "";
	private String numeroCuentaDollar = "";
	private String cuentaClabeDollar = "";
	private String numeroConvenioDollar = "";
	private String monedaDollar = "";
	private String abaDollar = "";
	private String switfCodeDollar = "";
	
	private String bancoOtro = "";
	private String sucursalOtro = "";
	private String nombreSucursalOtro = "";
	private String numeroCuentaOtro = "";
	private String cuentaClabeOtro = "";
	private String numeroConvenioOtro = "";
	private String monedaOtro = "";
	private String abaOtro = "";
	private String switfCodeOtro = "";
	private String bandDescuento = "";
	
	private String usrAcceso = "";
	private String anexo24 = "";
	private String limiteTolerancia = "";
	private String limiteComplemento = "";
	private String notPagoUsuario = "";
	private String notComUsuario = "";
	
	private String fechaAlta = "";
	
	private String usuarioGeneracion = "";
	
	private String email1 = "";
	private String email2 = "";
	private String email3 = "";
	private String email4 = "";
	private String email5 = "";
	private String email6 = "";
	private String email7 = "";
	private String email8 = "";
	private String email9 = "";
	private String email10 = "";
	
	
	private String tipoEmail1 = "";
	private String tipoEmail2 = "";
	private String tipoEmail3 = "";
	private String tipoEmail4 = "";
	private String tipoEmail5 = "";
	private String tipoEmail6 = "";
	private String tipoEmail7 = "";
	private String tipoEmail8 = "";
	private String tipoEmail9 = "";
	private String tipoEmail10 = "";
	
	private String conServicio = "";
	private String formaPago = "";
	private int numEstrellas = 0;
	private String centroCostos = "";
	private String numeroCuentaProveedor = "";
	
	private String razonProveedor = "";

	private String pagoDolares = "";
	private String pagoPesos = "";
	
	
	private String AMERICANOS_SERIE;
	private int AMERICANOS_FOLIO;
	private String AMERICANOS_ACCESO;
	private String PERMITIR_ACCESO_GENERADOR;
	
	private String bandIMSS;
	private String bandSAT;

	// private String servEsp = "N";
	private String numRegistro;
	
	private String tieneIMSS;
	private String tieneSAT;
	private String tieneConfidencial;
	
	private String confirmarIMSS;
	private String confirmarSAT;
	
	private String nombreArchivo = "";
	private String validarEtiqueta = "";
	
	private String bandCartaPorte;
	private String regimenFiscal = "";
	
	
	private String notificaOrdenCompra;
	private String notificaOrdenPago;
	private String conceptoServicio;
	private String bandInstrucciones;
	
	private String permitirAcceso;
	
	
	
	private String bandTareas;
	
	public int getClaveRegistro() {
		return claveRegistro;
	}
	@StrutsParameter
	public void setClaveRegistro(int claveRegistro) {
		this.claveRegistro = claveRegistro;
	}
	public int getClaveAcceso() {
		return claveAcceso;
	}
	@StrutsParameter
	public void setClaveAcceso(int claveAcceso) {
		this.claveAcceso = claveAcceso;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	@StrutsParameter
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getIdProveedor() {
		return idProveedor;
	}
	@StrutsParameter
	public void setIdProveedor(String idProveedor) {
		this.idProveedor = idProveedor;
	}
	public String getRfc() {
		return rfc;
	}
	@StrutsParameter
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getDomicilio() {
		return domicilio;
	}
	@StrutsParameter
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getCalle() {
		return calle;
	}
	@StrutsParameter
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumeroInt() {
		return numeroInt;
	}
	@StrutsParameter
	public void setNumeroInt(String numeroInt) {
		this.numeroInt = numeroInt;
	}
	public String getNumeroExt() {
		return numeroExt;
	}
	@StrutsParameter
	public void setNumeroExt(String numeroExt) {
		this.numeroExt = numeroExt;
	}
	public String getColonia() {
		return colonia;
	}
	@StrutsParameter
	public void setColonia(String colonia) {
		this.colonia = colonia;
	}
	public String getDelegacion() {
		return delegacion;
	}
	@StrutsParameter
	public void setDelegacion(String delegacion) {
		this.delegacion = delegacion;
	}
	public String getCiudad() {
		return ciudad;
	}
	@StrutsParameter
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public String getEstado() {
		return estado;
	}
	@StrutsParameter
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getCodigoPostal() {
		return codigoPostal;
	}
	@StrutsParameter
	public void setCodigoPostal(int codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getTelefono() {
		return telefono;
	}
	@StrutsParameter
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getExtencion() {
		return extencion;
	}
	@StrutsParameter
	public void setExtencion(String extencion) {
		this.extencion = extencion;
	}
	public String getNombreContacto() {
		return nombreContacto;
	}
	@StrutsParameter
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	public String getEmail() {
		return email;
	}
	@StrutsParameter
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTipoProveedor() {
		return tipoProveedor;
	}
	@StrutsParameter
	public void setTipoProveedor(String tipoProveedor) {
		this.tipoProveedor = tipoProveedor;
	}
	public String getTipoConfirmacion() {
		return tipoConfirmacion;
	}
	@StrutsParameter
	public void setTipoConfirmacion(String tipoConfirmacion) {
		this.tipoConfirmacion = tipoConfirmacion;
	}
	public String getBanco() {
		return banco;
	}
	@StrutsParameter
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getSucursal() {
		return sucursal;
	}
	@StrutsParameter
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	public String getNombreSucursal() {
		return nombreSucursal;
	}
	@StrutsParameter
	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	@StrutsParameter
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public String getCuentaClabe() {
		return cuentaClabe;
	}
	@StrutsParameter
	public void setCuentaClabe(String cuentaClabe) {
		this.cuentaClabe = cuentaClabe;
	}
	public String getNumeroConvenio() {
		return numeroConvenio;
	}
	@StrutsParameter
	public void setNumeroConvenio(String numeroConvenio) {
		this.numeroConvenio = numeroConvenio;
	}
	public String getMoneda() {
		return moneda;
	}
	@StrutsParameter
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public String getAba() {
		return aba;
	}
	@StrutsParameter
	public void setAba(String aba) {
		this.aba = aba;
	}
	public String getSwitfCode() {
		return switfCode;
	}
	@StrutsParameter
	public void setSwitfCode(String switfCode) {
		this.switfCode = switfCode;
	}
	public String getBancoDollar() {
		return bancoDollar;
	}
	@StrutsParameter
	public void setBancoDollar(String bancoDollar) {
		this.bancoDollar = bancoDollar;
	}
	public String getSucursalDollar() {
		return sucursalDollar;
	}
	@StrutsParameter
	public void setSucursalDollar(String sucursalDollar) {
		this.sucursalDollar = sucursalDollar;
	}
	public String getNombreSucursalDollar() {
		return nombreSucursalDollar;
	}
	@StrutsParameter
	public void setNombreSucursalDollar(String nombreSucursalDollar) {
		this.nombreSucursalDollar = nombreSucursalDollar;
	}
	public String getNumeroCuentaDollar() {
		return numeroCuentaDollar;
	}
	@StrutsParameter
	public void setNumeroCuentaDollar(String numeroCuentaDollar) {
		this.numeroCuentaDollar = numeroCuentaDollar;
	}
	public String getCuentaClabeDollar() {
		return cuentaClabeDollar;
	}
	@StrutsParameter
	public void setCuentaClabeDollar(String cuentaClabeDollar) {
		this.cuentaClabeDollar = cuentaClabeDollar;
	}
	public String getNumeroConvenioDollar() {
		return numeroConvenioDollar;
	}
	@StrutsParameter
	public void setNumeroConvenioDollar(String numeroConvenioDollar) {
		this.numeroConvenioDollar = numeroConvenioDollar;
	}
	public String getMonedaDollar() {
		return monedaDollar;
	}
	@StrutsParameter
	public void setMonedaDollar(String monedaDollar) {
		this.monedaDollar = monedaDollar;
	}
	public String getAbaDollar() {
		return abaDollar;
	}
	@StrutsParameter
	public void setAbaDollar(String abaDollar) {
		this.abaDollar = abaDollar;
	}
	public String getSwitfCodeDollar() {
		return switfCodeDollar;
	}
	@StrutsParameter
	public void setSwitfCodeDollar(String switfCodeDollar) {
		this.switfCodeDollar = switfCodeDollar;
	}
	public String getBancoOtro() {
		return bancoOtro;
	}
	@StrutsParameter
	public void setBancoOtro(String bancoOtro) {
		this.bancoOtro = bancoOtro;
	}
	public String getSucursalOtro() {
		return sucursalOtro;
	}
	@StrutsParameter
	public void setSucursalOtro(String sucursalOtro) {
		this.sucursalOtro = sucursalOtro;
	}
	public String getNombreSucursalOtro() {
		return nombreSucursalOtro;
	}
	@StrutsParameter
	public void setNombreSucursalOtro(String nombreSucursalOtro) {
		this.nombreSucursalOtro = nombreSucursalOtro;
	}
	public String getNumeroCuentaOtro() {
		return numeroCuentaOtro;
	}
	@StrutsParameter
	public void setNumeroCuentaOtro(String numeroCuentaOtro) {
		this.numeroCuentaOtro = numeroCuentaOtro;
	}
	public String getCuentaClabeOtro() {
		return cuentaClabeOtro;
	}
	@StrutsParameter
	public void setCuentaClabeOtro(String cuentaClabeOtro) {
		this.cuentaClabeOtro = cuentaClabeOtro;
	}
	public String getNumeroConvenioOtro() {
		return numeroConvenioOtro;
	}
	@StrutsParameter
	public void setNumeroConvenioOtro(String numeroConvenioOtro) {
		this.numeroConvenioOtro = numeroConvenioOtro;
	}
	public String getMonedaOtro() {
		return monedaOtro;
	}
	@StrutsParameter
	public void setMonedaOtro(String monedaOtro) {
		this.monedaOtro = monedaOtro;
	}
	public String getAbaOtro() {
		return abaOtro;
	}
	@StrutsParameter
	public void setAbaOtro(String abaOtro) {
		this.abaOtro = abaOtro;
	}
	public String getSwitfCodeOtro() {
		return switfCodeOtro;
	}
	@StrutsParameter
	public void setSwitfCodeOtro(String switfCodeOtro) {
		this.switfCodeOtro = switfCodeOtro;
	}
	public String getBandDescuento() {
		return bandDescuento;
	}
	@StrutsParameter
	public void setBandDescuento(String bandDescuento) {
		this.bandDescuento = bandDescuento;
	}
	public String getUsrAcceso() {
		return usrAcceso;
	}
	@StrutsParameter
	public void setUsrAcceso(String usrAcceso) {
		this.usrAcceso = usrAcceso;
	}
	
	
	public String getAnexo24() {
		return anexo24;
	}
	@StrutsParameter
	public void setAnexo24(String anexo24) {
		this.anexo24 = anexo24;
	}
	public String getLimiteTolerancia() {
		return limiteTolerancia;
	}
	@StrutsParameter
	public void setLimiteTolerancia(String limiteTolerancia) {
		this.limiteTolerancia = limiteTolerancia;
	}
	public String getLimiteComplemento() {
		return limiteComplemento;
	}
	@StrutsParameter
	public void setLimiteComplemento(String limiteComplemento) {
		this.limiteComplemento = limiteComplemento;
	}
	public String getNotPagoUsuario() {
		return notPagoUsuario;
	}
	@StrutsParameter
	public void setNotPagoUsuario(String notPagoUsuario) {
		this.notPagoUsuario = notPagoUsuario;
	}
	public String getNotComUsuario() {
		return notComUsuario;
	}
	@StrutsParameter
	public void setNotComUsuario(String notComUsuario) {
		this.notComUsuario = notComUsuario;
	}
	public String getFechaAlta() {
		return fechaAlta;
	}
	@StrutsParameter
	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public String getUsuarioGeneracion() {
		return usuarioGeneracion;
	}
	@StrutsParameter
	public void setUsuarioGeneracion(String usuarioGeneracion) {
		this.usuarioGeneracion = usuarioGeneracion;
	}
	public String getEmail1() {
		return email1;
	}
	@StrutsParameter
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public String getEmail2() {
		return email2;
	}
	@StrutsParameter
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public String getEmail3() {
		return email3;
	}
	@StrutsParameter
	public void setEmail3(String email3) {
		this.email3 = email3;
	}
	public String getEmail4() {
		return email4;
	}
	@StrutsParameter
	public void setEmail4(String email4) {
		this.email4 = email4;
	}
	public String getEmail5() {
		return email5;
	}
	@StrutsParameter
	public void setEmail5(String email5) {
		this.email5 = email5;
	}
	public String getEmail6() {
		return email6;
	}
	@StrutsParameter
	public void setEmail6(String email6) {
		this.email6 = email6;
	}
	public String getEmail7() {
		return email7;
	}
	@StrutsParameter
	public void setEmail7(String email7) {
		this.email7 = email7;
	}
	public String getEmail8() {
		return email8;
	}
	@StrutsParameter
	public void setEmail8(String email8) {
		this.email8 = email8;
	}
	public String getEmail9() {
		return email9;
	}
	@StrutsParameter
	public void setEmail9(String email9) {
		this.email9 = email9;
	}
	public String getEmail10() {
		return email10;
	}
	@StrutsParameter
	public void setEmail10(String email10) {
		this.email10 = email10;
	}
	
	
	
	
	public String getTipoEmail1() {
		return tipoEmail1;
	}
	@StrutsParameter
	public void setTipoEmail1(String tipoEmail1) {
		this.tipoEmail1 = tipoEmail1;
	}
	public String getTipoEmail2() {
		return tipoEmail2;
	}
	@StrutsParameter
	public void setTipoEmail2(String tipoEmail2) {
		this.tipoEmail2 = tipoEmail2;
	}
	public String getTipoEmail3() {
		return tipoEmail3;
	}
	@StrutsParameter
	public void setTipoEmail3(String tipoEmail3) {
		this.tipoEmail3 = tipoEmail3;
	}
	public String getTipoEmail4() {
		return tipoEmail4;
	}
	@StrutsParameter
	public void setTipoEmail4(String tipoEmail4) {
		this.tipoEmail4 = tipoEmail4;
	}
	public String getTipoEmail5() {
		return tipoEmail5;
	}
	@StrutsParameter
	public void setTipoEmail5(String tipoEmail5) {
		this.tipoEmail5 = tipoEmail5;
	}
	public String getTipoEmail6() {
		return tipoEmail6;
	}
	@StrutsParameter
	public void setTipoEmail6(String tipoEmail6) {
		this.tipoEmail6 = tipoEmail6;
	}
	public String getTipoEmail7() {
		return tipoEmail7;
	}
	@StrutsParameter
	public void setTipoEmail7(String tipoEmail7) {
		this.tipoEmail7 = tipoEmail7;
	}
	public String getTipoEmail8() {
		return tipoEmail8;
	}
	@StrutsParameter
	public void setTipoEmail8(String tipoEmail8) {
		this.tipoEmail8 = tipoEmail8;
	}
	public String getTipoEmail9() {
		return tipoEmail9;
	}
	@StrutsParameter
	public void setTipoEmail9(String tipoEmail9) {
		this.tipoEmail9 = tipoEmail9;
	}
	public String getTipoEmail10() {
		return tipoEmail10;
	}
	@StrutsParameter
	public void setTipoEmail10(String tipoEmail10) {
		this.tipoEmail10 = tipoEmail10;
	}
	public String getConServicio() {
		return conServicio;
	}
	@StrutsParameter
	public void setConServicio(String conServicio) {
		this.conServicio = conServicio;
	}
	public String getFormaPago() {
		return formaPago;
	}
	@StrutsParameter
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}
	public int getNumEstrellas() {
		return numEstrellas;
	}
	@StrutsParameter
	public void setNumEstrellas(int numEstrellas) {
		this.numEstrellas = numEstrellas;
	}
	public String getCentroCostos() {
		return centroCostos;
	}
	@StrutsParameter
	public void setCentroCostos(String centroCostos) {
		this.centroCostos = centroCostos;
	}
	public String getNumeroCuentaProveedor() {
		return numeroCuentaProveedor;
	}
	@StrutsParameter
	public void setNumeroCuentaProveedor(String numeroCuentaProveedor) {
		this.numeroCuentaProveedor = numeroCuentaProveedor;
	}
	public String getRazonProveedor() {
		return razonProveedor;
	}
	@StrutsParameter
	public void setRazonProveedor(String razonProveedor) {
		this.razonProveedor = razonProveedor;
	}
	public String getPagoDolares() {
		return pagoDolares;
	}
	@StrutsParameter
	public void setPagoDolares(String pagoDolares) {
		this.pagoDolares = pagoDolares;
	}
	public String getPagoPesos() {
		return pagoPesos;
	}
	@StrutsParameter
	public void setPagoPesos(String pagoPesos) {
		this.pagoPesos = pagoPesos;
	}
	public String getAMERICANOS_SERIE() {
		return AMERICANOS_SERIE;
	}
	@StrutsParameter
	public void setAMERICANOS_SERIE(String aMERICANOS_SERIE) {
		AMERICANOS_SERIE = aMERICANOS_SERIE;
	}
	public int getAMERICANOS_FOLIO() {
		return AMERICANOS_FOLIO;
	}
	@StrutsParameter
	public void setAMERICANOS_FOLIO(int aMERICANOS_FOLIO) {
		AMERICANOS_FOLIO = aMERICANOS_FOLIO;
	}
	public String getAMERICANOS_ACCESO() {
		return AMERICANOS_ACCESO;
	}
	@StrutsParameter
	public void setAMERICANOS_ACCESO(String aMERICANOS_ACCESO) {
		AMERICANOS_ACCESO = aMERICANOS_ACCESO;
	}
	public String getBandIMSS() {
		return bandIMSS;
	}
	@StrutsParameter
	public void setBandIMSS(String bandIMSS) {
		this.bandIMSS = bandIMSS;
	}
	public String getBandSAT() {
		return bandSAT;
	}
	@StrutsParameter
	public void setBandSAT(String bandSAT) {
		this.bandSAT = bandSAT;
	}
	/*
	public String getServEsp() {
		return servEsp;
	}
	public void setServEsp(String servEsp) {
		this.servEsp = servEsp;
	}
	*/
	public String getNumRegistro() {
		return numRegistro;
	}
	@StrutsParameter
	public void setNumRegistro(String numRegistro) {
		this.numRegistro = numRegistro;
	}
	public String getTieneIMSS() {
		return tieneIMSS;
	}
	@StrutsParameter
	public void setTieneIMSS(String tieneIMSS) {
		this.tieneIMSS = tieneIMSS;
	}
	public String getTieneSAT() {
		return tieneSAT;
	}
	@StrutsParameter
	public void setTieneSAT(String tieneSAT) {
		this.tieneSAT = tieneSAT;
	}
	public String getTieneConfidencial() {
		return tieneConfidencial;
	}
	@StrutsParameter
	public void setTieneConfidencial(String tieneConfidencial) {
		this.tieneConfidencial = tieneConfidencial;
	}
	public String getConfirmarIMSS() {
		return confirmarIMSS;
	}
	@StrutsParameter
	public void setConfirmarIMSS(String confirmarIMSS) {
		this.confirmarIMSS = confirmarIMSS;
	}
	public String getConfirmarSAT() {
		return confirmarSAT;
	}
	@StrutsParameter
	public void setConfirmarSAT(String confirmarSAT) {
		this.confirmarSAT = confirmarSAT;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	@StrutsParameter
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getValidarEtiqueta() {
		return validarEtiqueta;
	}
	@StrutsParameter
	public void setValidarEtiqueta(String validarEtiqueta) {
		this.validarEtiqueta = validarEtiqueta;
	}
	
	
	
	public String getBandCartaPorte() {
		return bandCartaPorte;
	}
	@StrutsParameter
	public void setBandCartaPorte(String bandCartaPorte) {
		this.bandCartaPorte = bandCartaPorte;
	}
	public String getRegimenFiscal() {
		return regimenFiscal;
	}
	@StrutsParameter
	public void setRegimenFiscal(String regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}
	public String getNotificaOrdenCompra() {
		return notificaOrdenCompra;
	}
	@StrutsParameter
	public void setNotificaOrdenCompra(String notificaOrdenCompra) {
		this.notificaOrdenCompra = notificaOrdenCompra;
	}
	public String getNotificaOrdenPago() {
		return notificaOrdenPago;
	}
	@StrutsParameter
	public void setNotificaOrdenPago(String notificaOrdenPago) {
		this.notificaOrdenPago = notificaOrdenPago;
	}
	public String getConceptoServicio() {
		return conceptoServicio;
	}
	@StrutsParameter
	public void setConceptoServicio(String conceptoServicio) {
		this.conceptoServicio = conceptoServicio;
	}
	public String getPERMITIR_ACCESO_GENERADOR() {
		return PERMITIR_ACCESO_GENERADOR;
	}
	@StrutsParameter
	public void setPERMITIR_ACCESO_GENERADOR(String pERMITIR_ACCESO_GENERADOR) {
		PERMITIR_ACCESO_GENERADOR = pERMITIR_ACCESO_GENERADOR;
	}
	
	public String getBandInstrucciones() {
		return bandInstrucciones;
	}
	public void setBandInstrucciones(String bandInstrucciones) {
		this.bandInstrucciones = bandInstrucciones;
	}
	public String getPermitirAcceso() {
		return permitirAcceso;
	}
	@StrutsParameter
	public void setPermitirAcceso(String permitirAcceso) {
		this.permitirAcceso = permitirAcceso;
	}
	public String getBandTareas() {
		return bandTareas;
	}
	@StrutsParameter
	public void setBandTareas(String bandTareas) {
		this.bandTareas = bandTareas;
	}
	

	

	
}
