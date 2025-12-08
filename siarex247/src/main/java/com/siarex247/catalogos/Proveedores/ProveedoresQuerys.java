package com.siarex247.catalogos.Proveedores;

public class ProveedoresQuerys {

	private static String detalleProveedor = "select CLAVE_PROVEEDOR, RAZON_SOCIAL, RFC, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, DELEGACION, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO1, EXTENCION, NOMBRE_CONTACTO, TIPO_PROVEEDOR, BANCO, SUCURSAL, NOMBRE_SUCURSAL, NUMERO_CUENTA, CUENTA_CLAVE, NUMERO_CONVENIO, MONEDA, BANCO_DOLLAR, SUCURSAL_DOLLAR, NOMBRE_SUCURSAL_DOLLAR, NUMERO_CUENTA_DOLLAR, CUENTA_CLAVE_DOLLAR, NUMERO_CONVENIO_DOLLAR, MONEDA_DOLLAR, ABA_DOLLAR, SWIFT_CODE_DOLLAR, BANCO_OTRO, SUCURSAL_OTRO, NOMBRE_SUCURSAL_OTRO, NUMERO_CUENTA_OTRO, CUENTA_CLAVE_OTRO, NUMERO_CONVENIO_OTRO, MONEDA_OTRO, ABA_OTRO, SWIFT_CODE_OTRO, USUARIO_ACCESO, FECHAALTA, TIPO_CONFIRMACION, EMAIL, ANEXO24, NUMERO_PROVEEDOR, LIMITE_TOLERANCIA, NOTIFICAR_ORDEN, NOTIFICAR_PAGO, BAND_DESCUENTO, CONCEPTO_SERVICIO, FORMA_PAGO, RANKING, NUMERO_CUENTA_COSTO, CENTRO_COSTOS, PAGO_DOLARES, PAGO_PESOS, BLOQUEO_IMSS, BLOQUEO_SAT, TIENE_DOCUMENTO_IMSS, TIENE_DOCUMENTO_SAT, SERVICIOS_ESPECIALIZADOS, NUMERO_REGISTRO, CARTA_PORTE, REGIMEN_FISCAL, ESTATUS_REGISTRO from PROVEEDORES";
	private static String altaProveedor = "insert into PROVEEDORES (RAZON_SOCIAL, RFC, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, DELEGACION, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO1, EXTENCION, NOMBRE_CONTACTO, TIPO_PROVEEDOR, BANCO, SUCURSAL, NOMBRE_SUCURSAL, NUMERO_CUENTA, CUENTA_CLAVE, NUMERO_CONVENIO, MONEDA, BANCO_DOLLAR, SUCURSAL_DOLLAR, NOMBRE_SUCURSAL_DOLLAR, NUMERO_CUENTA_DOLLAR, CUENTA_CLAVE_DOLLAR, NUMERO_CONVENIO_DOLLAR, MONEDA_DOLLAR, ABA_DOLLAR, SWIFT_CODE_DOLLAR, BANCO_OTRO, SUCURSAL_OTRO, NOMBRE_SUCURSAL_OTRO, NUMERO_CUENTA_OTRO, CUENTA_CLAVE_OTRO, NUMERO_CONVENIO_OTRO, MONEDA_OTRO, ABA_OTRO, SWIFT_CODE_OTRO, USUARIO_ACCESO, TIPO_CONFIRMACION, EMAIL, ANEXO24, NUMERO_PROVEEDOR, LIMITE_TOLERANCIA, CAMPO_BUSQUEDA, NOTIFICAR_ORDEN, NOTIFICAR_PAGO, BAND_DESCUENTO, CONCEPTO_SERVICIO, FORMA_PAGO, RANKING, LIMITE_TOLERANCIA_COMPLEMENTO, NUMERO_CUENTA_COSTO, CENTRO_COSTOS, RAZON_PROVEEDOR, PAGO_DOLARES, PAGO_PESOS, AMERICANOS_SERIE, AMERICANOS_FOLIO, AMERICANOS_ACCESO, BLOQUEO_IMSS, BLOQUEO_SAT, TIENE_DOCUMENTO_IMSS, TIENE_DOCUMENTO_SAT, TIENE_DOCUMENTO_CONFIDENCIADILIDAD, SERVICIOS_ESPECIALIZADOS, NUMERO_REGISTRO, CARTA_PORTE, REGIMEN_FISCAL) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String actualizaProveedor = "update PROVEEDORES set RAZON_SOCIAL = ?, CALLE = ?, NUMERO_INT = ?, NUMERO_EXT = ?, COLONIA = ?, DELEGACION = ?, CIUDAD = ?, ESTADO = ?, CODIGO_POSTAL = ?, TELEFONO1 = ?, EXTENCION = ?, NOMBRE_CONTACTO = ?, TIPO_PROVEEDOR = ?,  BANCO = ?, SUCURSAL = ?, NOMBRE_SUCURSAL = ?, NUMERO_CUENTA = ?, CUENTA_CLAVE = ?, NUMERO_CONVENIO = ?, MONEDA = ?, BANCO_DOLLAR = ?, SUCURSAL_DOLLAR = ?, NOMBRE_SUCURSAL_DOLLAR = ?, NUMERO_CUENTA_DOLLAR = ?, CUENTA_CLAVE_DOLLAR = ?, NUMERO_CONVENIO_DOLLAR = ?, MONEDA_DOLLAR = ?, ABA_DOLLAR = ?, SWIFT_CODE_DOLLAR = ?, BANCO_OTRO = ?, SUCURSAL_OTRO = ?, NOMBRE_SUCURSAL_OTRO = ?, NUMERO_CUENTA_OTRO = ?, CUENTA_CLAVE_OTRO = ?, NUMERO_CONVENIO_OTRO = ?, MONEDA_OTRO = ?, ABA_OTRO = ?, SWIFT_CODE_OTRO = ?, USUARIO_ACCESO = ?, TIPO_CONFIRMACION = ?, EMAIL = ?, ANEXO24 = ?, LIMITE_TOLERANCIA = ?, CAMPO_BUSQUEDA = ?, NOTIFICAR_ORDEN=?, NOTIFICAR_PAGO=?, BAND_DESCUENTO = ?, CONCEPTO_SERVICIO = ?, FORMA_PAGO = ?, RANKING = ?, LIMITE_TOLERANCIA_COMPLEMENTO = ?, NUMERO_CUENTA_COSTO = ?, CENTRO_COSTOS = ?, RAZON_PROVEEDOR = ?, PAGO_DOLARES = ?, PAGO_PESOS = ?, AMERICANOS_SERIE = ?, AMERICANOS_FOLIO = ?, AMERICANOS_ACCESO = ?, BLOQUEO_IMSS = ?, BLOQUEO_SAT = ?, TIENE_DOCUMENTO_IMSS = ?, TIENE_DOCUMENTO_SAT = ?, TIENE_DOCUMENTO_CONFIDENCIADILIDAD = ?, CONFIRMAR_DOCUMENTO_IMSS = ?, CONFIRMAR_DOCUMENTO_SAT = ?, SERVICIOS_ESPECIALIZADOS = ?, NUMERO_REGISTRO = ?, CARTA_PORTE = ?, REGIMEN_FISCAL = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where CLAVE_PROVEEDOR = ?";
	private static String actualizaEstatus = "update PROVEEDORES set ESTATUS_REGISTRO = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where CLAVE_PROVEEDOR = ?";
	private static String infoBuscaRazon = "select RFC, EMAIL, TIPO_CONFIRMACION, RAZON_SOCIAL, CLAVE_PROVEEDOR from PROVEEDORES where CLAVE_PROVEEDOR = ?";
	private static String infoProveeEmail      = "select B.EMAIL, B.TIPO, B.BAND_PAGOS, B.BAND_ORDEN from PROVEEDORES A, PROVEEDORES_EMAIL B where A.CLAVE_PROVEEDOR = ? and A.CLAVE_PROVEEDOR =  B.CLAVE_PROVEEDOR";
	private static String altaProveedorMail   = "insert into PROVEEDORES_EMAIL (CLAVE_PROVEEDOR, EMAIL, TIPO, BAND_PAGOS, BAND_ORDEN) VALUES (?,?,?,?,?)";
	private static String infoProvee    	  = "select RAZON_SOCIAL, RFC, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, DELEGACION, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO1, EXTENCION, NOMBRE_CONTACTO, TIPO_PROVEEDOR, BANCO, SUCURSAL, NOMBRE_SUCURSAL, NUMERO_CUENTA, CUENTA_CLAVE, NUMERO_CONVENIO, MONEDA, BANCO_DOLLAR, SUCURSAL_DOLLAR, NOMBRE_SUCURSAL_DOLLAR, NUMERO_CUENTA_DOLLAR, CUENTA_CLAVE_DOLLAR, NUMERO_CONVENIO_DOLLAR, MONEDA_DOLLAR, ABA_DOLLAR, SWIFT_CODE_DOLLAR, BANCO_OTRO, SUCURSAL_OTRO, NOMBRE_SUCURSAL_OTRO, NUMERO_CUENTA_OTRO, CUENTA_CLAVE_OTRO, NUMERO_CONVENIO_OTRO, MONEDA_OTRO, ABA_OTRO, SWIFT_CODE_OTRO, USUARIO_ACCESO, TIPO_CONFIRMACION, EMAIL, ANEXO24, NUMERO_PROVEEDOR, LIMITE_TOLERANCIA, CAMPO_BUSQUEDA, NOTIFICAR_ORDEN, NOTIFICAR_PAGO, BAND_DESCUENTO, CONCEPTO_SERVICIO, FORMA_PAGO, RANKING, LIMITE_TOLERANCIA_COMPLEMENTO, NUMERO_CUENTA_COSTO, CENTRO_COSTOS, RAZON_PROVEEDOR, PAGO_DOLARES, PAGO_PESOS, AMERICANOS_SERIE, AMERICANOS_FOLIO, AMERICANOS_ACCESO, BLOQUEO_IMSS, BLOQUEO_SAT, TIENE_DOCUMENTO_IMSS, TIENE_DOCUMENTO_SAT, TIENE_DOCUMENTO_CONFIDENCIADILIDAD, SERVICIOS_ESPECIALIZADOS, NUMERO_REGISTRO, CARTA_PORTE, REGIMEN_FISCAL, CONFIRMAR_DOCUMENTO_IMSS, CONFIRMAR_DOCUMENTO_SAT, ESTATUS_REGISTRO from PROVEEDORES where  CLAVE_PROVEEDOR = ?";
	private static String infoProveeXrfc    	  = "select CLAVE_PROVEEDOR, RAZON_SOCIAL, RFC, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, DELEGACION, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO1, EXTENCION, NOMBRE_CONTACTO, TIPO_PROVEEDOR, BANCO, SUCURSAL, NOMBRE_SUCURSAL, NUMERO_CUENTA, CUENTA_CLAVE, NUMERO_CONVENIO, MONEDA, BANCO_DOLLAR, SUCURSAL_DOLLAR, NOMBRE_SUCURSAL_DOLLAR, NUMERO_CUENTA_DOLLAR, CUENTA_CLAVE_DOLLAR, NUMERO_CONVENIO_DOLLAR, MONEDA_DOLLAR, ABA_DOLLAR, SWIFT_CODE_DOLLAR, BANCO_OTRO, SUCURSAL_OTRO, NOMBRE_SUCURSAL_OTRO, NUMERO_CUENTA_OTRO, CUENTA_CLAVE_OTRO, NUMERO_CONVENIO_OTRO, MONEDA_OTRO, ABA_OTRO, SWIFT_CODE_OTRO, USUARIO_ACCESO, TIPO_CONFIRMACION, EMAIL, ANEXO24, NUMERO_PROVEEDOR, LIMITE_TOLERANCIA, CAMPO_BUSQUEDA, NOTIFICAR_ORDEN, NOTIFICAR_PAGO, BAND_DESCUENTO, CONCEPTO_SERVICIO, FORMA_PAGO, RANKING, LIMITE_TOLERANCIA_COMPLEMENTO, NUMERO_CUENTA_COSTO, CENTRO_COSTOS, RAZON_PROVEEDOR, PAGO_DOLARES, PAGO_PESOS, AMERICANOS_SERIE, AMERICANOS_FOLIO, AMERICANOS_ACCESO, BLOQUEO_IMSS, BLOQUEO_SAT, TIENE_DOCUMENTO_IMSS, TIENE_DOCUMENTO_SAT, TIENE_DOCUMENTO_CONFIDENCIADILIDAD, SERVICIOS_ESPECIALIZADOS, NUMERO_REGISTRO, CARTA_PORTE, REGIMEN_FISCAL, CONFIRMAR_DOCUMENTO_IMSS, CONFIRMAR_DOCUMENTO_SAT, ESTATUS_REGISTRO from PROVEEDORES where  RFC = ? and ESTATUS_REGISTRO = ?";
	
	private static String eliminaProveedorMail = "delete from PROVEEDORES_EMAIL where CLAVE_PROVEEDOR = ? and TIPO not in (?)";
	private static String eliminaProveedor = "update PROVEEDORES set ESTATUS_REGISTRO = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where CLAVE_PROVEEDOR = ?";
	private static String infoProveedorMail = "select CLAVE_REGISTRO, EMAIL, TIPO, BAND_PAGOS, BAND_ORDEN from PROVEEDORES_EMAIL where CLAVE_PROVEEDOR = ? and TIPO = ? order by clave_registro asc";
	private static String listaExportarZIP     = "select CLAVE_PROVEEDOR, RFC, TIENE_DOCUMENTO_IMSS, TIENE_DOCUMENTO_SAT from PROVEEDORES where CLAVE_PROVEEDOR in (";
	private static String eliminarCertificados = "update PROVEEDORES set TIENE_DOCUMENTO_IMSS = ?, TIENE_DOCUMENTO_SAT = ?, CONFIRMAR_DOCUMENTO_IMSS = ?, CONFIRMAR_DOCUMENTO_SAT = ?  where CLAVE_PROVEEDOR in (";
	private static String infoProveedorCertificado = "select CLAVE_PROVEEDOR, RAZON_SOCIAL, RFC, TIENE_DOCUMENTO_IMSS, TIENE_DOCUMENTO_SAT, CONFIRMAR_DOCUMENTO_IMSS, CONFIRMAR_DOCUMENTO_SAT from  PROVEEDORES where CLAVE_PROVEEDOR = ?";
	private static String actualizaCertificadoComprasIMSS = "update PROVEEDORES set CONFIRMAR_DOCUMENTO_IMSS = ?, TIENE_DOCUMENTO_IMSS = ? where CLAVE_PROVEEDOR = ?";
	private static String actualizaCertificadoComprasSAT = "update PROVEEDORES set CONFIRMAR_DOCUMENTO_SAT = ?, TIENE_DOCUMENTO_SAT = ? where CLAVE_PROVEEDOR = ?";
	private static String proveedoresCP                  = "select CLAVE_PROVEEDOR, RAZON_SOCIAL, RFC from PROVEEDORES where CARTA_PORTE = ?";
	private static String comboProveedores 	= "select CLAVE_PROVEEDOR, RAZON_SOCIAL, RFC from PROVEEDORES where ESTATUS_REGISTRO = ? ";
	private static String queryBuscarNumeroProveedor = "select CLAVE_PROVEEDOR, RFC, TIPO_PROVEEDOR from PROVEEDORES where NUMERO_PROVEEDOR = ?";
	private static String isCartaPorteRFC        = "select CLAVE_PROVEEDOR, CARTA_PORTE from PROVEEDORES where RFC = ?";
	
	
	private static String queryProveedoresTipo = "select CLAVE_PROVEEDOR, RFC, RAZON_SOCIAL, NOTIFICAR_ORDEN, EMAIL from PROVEEDORES where  TIPO_PROVEEDOR in (?,?);";
	
	public static String getQueryDetalleProveedor(String esquema) {
		return detalleProveedor.replaceAll("<<esquema>>", esquema);
	}

	public static String getQueryInfoProvee(String esquema) {
		return infoProvee.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getInfoProveeXrfc(String esquema) {
		return infoProveeXrfc.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizaEstatus(String esquema) {
		return actualizaEstatus.replaceAll("<<esquema>>", esquema);
	}
	
	
	
	public static String getQueryAltaProveedor(String esquema) {
		return altaProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryactualizaProveedor(String esquema) {
		return actualizaProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInfoBuscaRazon(String esquema) {
		return infoBuscaRazon.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryInfoProveeEmail(String esquema) {
		return infoProveeEmail.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryAltaProveedorMail(String esquema) {
		return altaProveedorMail.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryEliminaProveedorMail(String esquema) {
		return eliminaProveedorMail.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryEliminaProveedor(String esquema) {
		return eliminaProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryListaExportarZIP(String esquema) {
		return listaExportarZIP.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryEliminarCertificados(String esquema) {
		return eliminarCertificados.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInfoProveedorMail(String esquema) {
		return infoProveedorMail.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryInfoProveedorCertificado(String esquema) {
		return infoProveedorCertificado.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryActualizaCertificadoComprasIMSS(String esquema) {
		return actualizaCertificadoComprasIMSS.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryActualizaCertificadoComprasSAT(String esquema) {
		return actualizaCertificadoComprasSAT.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryProveedoresCP(String esquema) {
		return proveedoresCP.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getComboProveedores(String esquema) {
		return comboProveedores.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryBuscarNumeroProveedor(String esquema) {
		return queryBuscarNumeroProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getIsCartaPorteRFC(String esquema) {
		return isCartaPorteRFC.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryProveedoresTipo(String esquema) {
		return queryProveedoresTipo.replaceAll("<<esquema>>", esquema);
	}
}

