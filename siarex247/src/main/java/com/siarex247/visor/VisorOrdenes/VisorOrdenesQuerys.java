package com.siarex247.visor.VisorOrdenes;

public class VisorOrdenesQuerys {

	
	private static String detalleOrdenes =  "select B.RAZON_SOCIAL, A.FOLIO_ORDEN, A.FOLIO_EMPRESA, A.CONCEPTO, A.TIPO_MONEDA, A.MONTO, A.SERVICIO_PRODUCTO, A.ESTATUS_PAGO, A.SERIE, A.TOTAL, A.SUB_TOTAL, A.IVA, A.IVA_RET, A.ISR_RET, A.IMP_LOCALES, A.NOMBRE_XML, A.NOMBRE_PDF,C.NOMBRE_XML as XML_COMPLE, C.NOMBRE_PDF as PDF_COMPLE, CP.UUID as XML_CP, CP.UUID as PDF_CP, D.NOMBRE_XML as XML_NOTA, D.NOMBRE_PDF as PDF_NOTA, D.TOTAL_PAGADO as TOTAL_NOTA, D.SUB_TOTAL as SUBTOTAL_NOTA, D.IVA as IVA_NOTA, D.ISR_RET as ISR_NOTA, A.FECHAPAGO, A.ASIGNAR_A, A.FECHAULTMOV, A.ESTADO_SAT, A.ESTATUS_SAT, A.USO_CFDI, A.CLAVE_PRODUCTO_SERVICIO, A.FECHA_FACTURA, A.FECHAREGISTRO,  A.FECHA_RECEPCION, A.OMITIR_COMPLEMENTO, B.TIPO_CONFIRMACION, B.TIPO_PROVEEDOR, A.CLAVE_PROVEEDOR, A.UUID, C.UUID_COMPLEMENTO, D.UUID_CREDITO  from  ORDENES A inner join PROVEEDORES B ON  A.CLAVE_PROVEEDOR =  B.CLAVE_PROVEEDOR  left join COMPLEMENTARIA C on A.FOLIO_ORDEN = C.FOLIO_ORDEN left join NOTAS_CREDITO D on A.FOLIO_ORDEN = D.FOLIO_ORDEN left join CARTA_PORTE CP on A.FOLIO_ORDEN = CP.FOLIO_ORDEN ";
	private static String totalRegistro =  "select count(*)  from ORDENES A inner join PROVEEDORES B ON  A.CLAVE_PROVEEDOR =  B.CLAVE_PROVEEDOR  left join COMPLEMENTARIA C on A.FOLIO_ORDEN = C.FOLIO_ORDEN left join NOTAS_CREDITO D on A.FOLIO_ORDEN = D.FOLIO_ORDEN left join CARTA_PORTE CP on A.FOLIO_ORDEN = CP.FOLIO_ORDEN ";
	
	private static String consultarOrden =  "select FOLIO_ORDEN, FOLIO_EMPRESA, CONCEPTO, TIPO_MONEDA, MONTO, SERVICIO_PRODUCTO, ESTATUS_PAGO, SERIE, TOTAL, SUB_TOTAL, IVA, IVA_RET, ISR_RET, IMP_LOCALES, NOMBRE_XML, NOMBRE_PDF, CLAVE_PROVEEDOR, UUID, FECHAPAGO, FECHAULTMOV, ASIGNAR_A, ESTADO_SAT, ESTATUS_SAT, USO_CFDI, CLAVE_PRODUCTO_SERVICIO, OMITIR_COMPLEMENTO, NUMERO_CUENTA_COSTO_PROVEEDOR, CENTRO_COSTOS_PROVEEDOR, TIPO_ORDEN, FECHA_FACTURA, NOMBRE_ARCHIVO  from ORDENES where FOLIO_ORDEN = ?";
	private static String listadoMultiple =  "select FOLIO_ORDEN, FOLIO_EMPRESA, CONCEPTO, TIPO_MONEDA, MONTO, SERVICIO_PRODUCTO, ESTATUS_PAGO, SERIE, TOTAL, SUB_TOTAL, IVA, IVA_RET, ISR_RET, IMP_LOCALES, NOMBRE_XML, NOMBRE_PDF, CLAVE_PROVEEDOR, UUID, FECHAPAGO, FECHAULTMOV, ASIGNAR_A, ESTADO_SAT, ESTATUS_SAT, USO_CFDI, CLAVE_PRODUCTO_SERVICIO, OMITIR_COMPLEMENTO, NUMERO_CUENTA_COSTO_PROVEEDOR, CENTRO_COSTOS_PROVEEDOR, TIPO_ORDEN  from ORDENES where TIPO_ORDEN = ?";
	private static String consultarOrdenFolioEmpresa =  "select FOLIO_ORDEN, FOLIO_EMPRESA, CONCEPTO, TIPO_MONEDA, MONTO, SERVICIO_PRODUCTO, ESTATUS_PAGO, SERIE, TOTAL, SUB_TOTAL, IVA, IVA_RET, ISR_RET, IMP_LOCALES, NOMBRE_XML, NOMBRE_PDF, CLAVE_PROVEEDOR, UUID, FECHAPAGO, FECHAULTMOV, ASIGNAR_A, ESTADO_SAT, ESTATUS_SAT, USO_CFDI, CLAVE_PRODUCTO_SERVICIO, OMITIR_COMPLEMENTO, NUMERO_CUENTA_COSTO_PROVEEDOR, CENTRO_COSTOS_PROVEEDOR, TIPO_ORDEN  from ORDENES where FOLIO_EMPRESA = ?";
	private static String grabarOrden 	 = "insert into ORDENES (FOLIO_EMPRESA,CONCEPTO, TIPO_MONEDA, MONTO, ESTATUS_PAGO, CLAVE_PROVEEDOR, SERVICIO_PRODUCTO, USUARIO, FECHAULTMOV, ASIGNAR_A, NUMERO_REQUISICION, USO_CFDI, CLAVE_PRODUCTO_SERVICIO, NUMERO_CUENTA_COSTO_PROVEEDOR, CENTRO_COSTOS_PROVEEDOR, NOMBRE_ARCHIVO  ) values (?,?,?,?,?,?,?,?,current_timestamp, ?, ?, ?, ?, ?, ?, ?)";
	private static String actualizaOrdenVisor = "update ORDENES set CONCEPTO = ?, TIPO_MONEDA = ?, MONTO = ?, CLAVE_PROVEEDOR = ?, SERVICIO_PRODUCTO = ?, USUARIO_CAMBIO = ?, ASIGNAR_A = ?, ESTATUS_PAGO = ?, FECHAULTMOV = current_timestamp, FECHAPAGO = ?, NUMERO_CUENTA_COSTO_PROVEEDOR = ?, CENTRO_COSTOS_PROVEEDOR = ? where FOLIO_ORDEN = ?";
	private static String actualizaOrdenVisorMultiple = "update ORDENES set CONCEPTO = ?, TIPO_MONEDA = ?,  SERVICIO_PRODUCTO = ?, USUARIO_CAMBIO = ?, ASIGNAR_A = ?, ESTATUS_PAGO = ?, FECHAULTMOV = current_timestamp, FECHAPAGO = ?, NUMERO_CUENTA_COSTO_PROVEEDOR = ?, CENTRO_COSTOS_PROVEEDOR = ? where TIPO_ORDEN = ?";
	private static String actualizaOrdenVisorFactura = "update ORDENES set CONCEPTO = ?, TIPO_MONEDA = ?, MONTO = ?, CLAVE_PROVEEDOR = ?, SERVICIO_PRODUCTO = ?, USUARIO_CAMBIO = ?, ASIGNAR_A = ?, ESTATUS_PAGO = ?, SERIE = null, TOTAL = 0, SUB_TOTAL = 0, IVA = 0, IVA_RET = 0, ISR_RET = 0, IMP_LOCALES = 0, NOMBRE_XML = null, NOMBRE_PDF = null, UUID = null, FECHA_UUID = null, EMAIL_PROVEEDOR = null, FECHAULTMOV = current_timestamp, FECHAPAGO = ?, NUMERO_CUENTA_COSTO_PROVEEDOR = ?, CENTRO_COSTOS_PROVEEDOR = ?, ESTADO_SAT = null, ESTATUS_SAT = null, USO_CFDI = null, CLAVE_PRODUCTO_SERVICIO = null where FOLIO_ORDEN = ?";
	private static String actualizaOrdenVisorFacturaMultiple = "update ORDENES set CONCEPTO = ?, TIPO_MONEDA = ?, SERVICIO_PRODUCTO = ?, USUARIO_CAMBIO = ?, ASIGNAR_A = ?, ESTATUS_PAGO = ?, SERIE = null, TOTAL = 0, SUB_TOTAL = 0, IVA = 0, IVA_RET = 0, ISR_RET = 0, IMP_LOCALES = 0, NOMBRE_XML = null, NOMBRE_PDF = null, UUID = null, FECHA_UUID = null, EMAIL_PROVEEDOR = null, FECHAULTMOV = current_timestamp, FECHAPAGO = ?, NUMERO_CUENTA_COSTO_PROVEEDOR = ?, CENTRO_COSTOS_PROVEEDOR = ?, ESTADO_SAT = ?, ESTATUS_SAT = ?, USO_CFDI = ?, CLAVE_PRODUCTO_SERVICIO = null where TIPO_ORDEN = ?";
	private static String actualizaOrdenPagadaVisor  = "update ORDENES set ESTATUS_PAGO = ?, USUARIO = ?, FECHAPAGO = ?, FECHA_RECEPCION = ?, FECHAULTMOV = current_timestamp where FOLIO_ORDEN = ?";
	private static String actualizaOrdenPagadaVisorMultiple  = "update ORDENES set ESTATUS_PAGO = ?, USUARIO = ?, FECHAPAGO = ?, FECHA_RECEPCION = ?, FECHAULTMOV = current_timestamp where TIPO_ORDEN = ?";
	private static String eliminaOrden  = "delete from ORDENES where FOLIO_ORDEN = ?";
	

	private static String actualizaOrdenClaveProductoOK = "update ORDENES set ESTATUS_PAGO = ?, USUARIO_CAMBIO = ?, FECHAULTMOV = current_timestamp where FOLIO_EMPRESA = ?";
	private static String actualizaOrdenClaveProductoNG = "update ORDENES set ESTATUS_PAGO = ?, SERIE = ?, TOTAL = ?, SUB_TOTAL = ?, IVA = ?, IVA_RET = ?, ISR_RET= ?, UUID = ?, FECHA_UUID = ?, ESTADO_SAT = ?, ESTATUS_SAT = ?, FECHA_FACTURA = ?, USO_CFDI = ?, CLAVE_PRODUCTO_SERVICIO = ?, NOMBRE_XML = ?, NOMBRE_PDF = ?, USUARIO_CAMBIO = ?, FECHAULTMOV = current_timestamp where FOLIO_EMPRESA = ?";
	

	// Uso de CFDI Multiples
	private static String actualizaOrdenClaveProductoOK_Multiple = "update ORDENES set ESTATUS_PAGO = ?, FECHAULTMOV = current_timestamp where TIPO_ORDEN = ?";
	private static String actualizaOrdenClaveProductoNG_Multiple = "update ORDENES set ESTATUS_PAGO = ?, SERIE = ?, TOTAL = ?, SUB_TOTAL = ?, IVA = ?, IVA_RET = ?, ISR_RET= ?, UUID = ?, FECHA_UUID = ?, ESTADO_SAT = ?, ESTATUS_SAT = ?, FECHA_FACTURA = ?, USO_CFDI = ?, CLAVE_PRODUCTO_SERVICIO = ?, NOMBRE_XML = ?, NOMBRE_PDF = ?, TIPO_ORDEN = ?, FECHAULTMOV = current_timestamp where TIPO_ORDEN = ?";
	private static String fechasCierre = "select ANIO, FECHA_APARTIR, MENSAJE_APARTIR, FECHA_HASTA, MENSAJE_HASTA from CIERRE_ANIO where ANIO = ? and TIPO_CIERRE = ?";
	private static String esOrdenProveedor = "select FOLIO_EMPRESA from ORDENES where FOLIO_EMPRESA = ? and CLAVE_PROVEEDOR = ?";
	private static String actualizaOrdenFolioEmpresa  = "update ORDENES set  ESTATUS_PAGO = ?, SERIE = ?, TOTAL = ?, SUB_TOTAL = ?, IVA = ?, IVA_RET = ?, ISR_RET = ?, IMP_LOCALES = ?, NOMBRE_XML = ?, NOMBRE_PDF = ?, FECHAULTMOV = current_timestamp, SERVICIO_PRODUCTO = ?, UUID = ?, FECHA_UUID = ?, USUARIO_CAMBIO = ?, TIPO_ORDEN = ?, ESTADO_SAT = ?, ESTATUS_SAT = ?, USO_CFDI = ?, CLAVE_PRODUCTO_SERVICIO = ?, FECHAPAGO = ?, FECHA_FACTURA = ?  where FOLIO_EMPRESA = ?";
	
	
	
	private static String existeSerieFolio = "select SERIE from ORDENES where SERIE = ?";
	private static String actualizaOrden = "update ORDENES set ESTATUS_PAGO = ?, TOTAL = ?, SERIE = ?, NOMBRE_XML = ?, NOMBRE_PDF = ?, USUARIO_CAMBIO = ?, FECHA_FACTURA = ?,  FECHAULTMOV = current_timestamp where FOLIO_EMPRESA = ?";
	
	private static String actualizaOrdenAmericanaOK = "update ORDENES set ESTATUS_PAGO = ?, USUARIO_CAMBIO = ?, FECHAULTMOV = current_timestamp, FECHAPAGO = ? where FOLIO_ORDEN = ?";
	private static String actualizaOrdenAmericanaNG = "update ORDENES set ESTATUS_PAGO = ?, SERIE = ?, TOTAL = ?, NOMBRE_XML = ?, NOMBRE_PDF = ?, USUARIO_CAMBIO = ?, FECHAULTMOV = current_timestamp where FOLIO_ORDEN = ?";
	
	
	private static String actualizaOrdenVisorOmitir = "update ORDENES set USUARIO_CAMBIO = ?, FECHAULTMOV = current_timestamp, OMITIR_COMPLEMENTO = ? where FOLIO_ORDEN = ?";
	private static String actualizaOrdenVisorMultipleOmitir = "update ORDENES set USUARIO_CAMBIO = ?, FECHAULTMOV = current_timestamp,  OMITIR_COMPLEMENTO = ? where TIPO_ORDEN = ?";
	
	
	private static String actualizaOrdenPagadaReciboVisorMultiple  = "update ORDENES set USUARIO_CAMBIO = ?, FECHAPAGO = ?, FECHAULTMOV = current_timestamp where TIPO_ORDEN = ?";
	private static String actualizaOrdenPagadaReciboVisor  = "update ORDENES set USUARIO_CAMBIO = ?, FECHAPAGO = ?, FECHAULTMOV = current_timestamp where FOLIO_ORDEN = ?";
	
	
	private static String ordenesUltimoMov = "select A.FOLIO_ORDEN, A.FOLIO_EMPRESA, A.FECHAULTMOV, A.ESTATUS_PAGO, A.CLAVE_PROVEEDOR, B.RAZON_SOCIAL, A.ID_EMPLEADO, A.MONTO, A.TIPO_MONEDA from ORDENES A, PROVEEDORES B where ESTATUS_PAGO in(?,?) and A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR";
	
	private static String consultarFechaMinima = "select min(FECHAULTMOV) from ORDENES";
	
	
	
	public static String getDetalleOrdenes(String esquema) {
		return detalleOrdenes.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTotalRegistro(String esquema) {
		return totalRegistro.replaceAll("<<esquema>>", esquema);
	}
	
	
	
	public static String getConsultarOrden(String esquema) {
		return consultarOrden.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getListadoMultiple(String esquema) {
		return listadoMultiple.replaceAll("<<esquema>>", esquema);
	}

	
	
	public static String getConsultarOrdenFolioEmpresa(String esquema) {
		return consultarOrdenFolioEmpresa.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryGrabarOrden(String esquema) {
		return grabarOrden.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryActualizaOrdenVisor(String esquema) {
		return actualizaOrdenVisor.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryActualizaOrdenVisorMultiple(String esquema) {
		return actualizaOrdenVisorMultiple.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryActualizaOrdenVisorFactura(String esquema) {
		return actualizaOrdenVisorFactura.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryActualizaOrdenVisorFacturaMultiple(String esquema) {
		return actualizaOrdenVisorFacturaMultiple.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getActualizaOrdenPagadaVisor(String esquema) {
		return actualizaOrdenPagadaVisor.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getActualizaOrdenPagadaVisorMultiple(String esquema) {
		return actualizaOrdenPagadaVisorMultiple.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryEliminaOrden(String esquema) {
		return eliminaOrden.replaceAll("<<esquema>>", esquema).toUpperCase();
	}
	

	public static String getActualizaOrdenClaveProductoOK(String esquema) {
		return actualizaOrdenClaveProductoOK.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getActualizaOrdenClaveProductoNG(String esquema) {
		return actualizaOrdenClaveProductoNG.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getActualizaOrdenClaveProductoOK_Multiple(String esquema) {
		return actualizaOrdenClaveProductoOK_Multiple.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizaOrdenClaveProductoNG_Multiple(String esquema) {
		return actualizaOrdenClaveProductoNG_Multiple.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryFechasCierreQuery(String esquema) {
		return fechasCierre.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEsOrdenProveedor(String esquema) {
		return esOrdenProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizaOrdenFolioEmpresa(String esquema) {
		return actualizaOrdenFolioEmpresa.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getExisteSerieFolio(String esquema) {
		return existeSerieFolio.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizaOrden(String esquema) {
		return actualizaOrden.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryActualizaOrdenAmericanaOK(String esquema) {
		return actualizaOrdenAmericanaOK.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryactualizaOrdenAmericanaNG(String esquema) {
		return actualizaOrdenAmericanaNG.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryActualizaOrdenVisorOmitir(String esquema) {
		return actualizaOrdenVisorOmitir.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryActualizaOrdenVisorMultipleOmitir(String esquema) {
		return actualizaOrdenVisorMultipleOmitir.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getActualizaOrdenPagadaReciboVisorMultiple(String esquema) {
		return actualizaOrdenPagadaReciboVisorMultiple.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getActualizaOrdenPagadaReciboVisor(String esquema) {
		return actualizaOrdenPagadaReciboVisor.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryOrdenesUltimoMov(String esquema) {
		return ordenesUltimoMov.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarFechaMinima(String esquema) {
		return consultarFechaMinima.replaceAll("<<esquema>>", esquema);
	}
	
}
