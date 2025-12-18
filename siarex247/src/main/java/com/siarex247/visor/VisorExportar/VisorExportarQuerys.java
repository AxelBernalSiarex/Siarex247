package com.siarex247.visor.VisorExportar;

public class VisorExportarQuerys {

	
	
	private static String detallePlantilla = "select B.RFC, B.RAZON_SOCIAL, A.FOLIO_ORDEN, A.FOLIO_EMPRESA, A.TIPO_MONEDA, A.MONTO, A.TOTAL, A.SUB_TOTAL, B.TIPO_CONFIRMACION, B.TIPO_PROVEEDOR  FROM ORDENES A inner join PROVEEDORES B ON  A.CLAVE_PROVEEDOR =  B.CLAVE_PROVEEDOR where  A.FOLIO_EMPRESA in (";
	private static String detallePlantillaFiltros = "select B.RFC, B.RAZON_SOCIAL, A.FOLIO_ORDEN, A.FOLIO_EMPRESA, A.TIPO_MONEDA, A.MONTO, A.TOTAL, A.SUB_TOTAL, B.TIPO_CONFIRMACION, B.TIPO_PROVEEDOR  FROM ORDENES A inner join PROVEEDORES B ON  A.CLAVE_PROVEEDOR =  B.CLAVE_PROVEEDOR ";
	
	private static String detalleFacturas = "select A.ESTATUS_PAGO, A.NOMBRE_XML, A.NOMBRE_PDF, A.TIPO_ORDEN, A.CLAVE_PROVEEDOR, C.NOMBRE_XML as XML_COMPLE, C.NOMBRE_PDF as PDF_COMPLE, D.NOMBRE_XML as XML_NOTA, D.NOMBRE_PDF as PDF_NOTA from ORDENES A inner join PROVEEDORES B on  A.CLAVE_PROVEEDOR =  B.CLAVE_PROVEEDOR  left join COMPLEMENTARIA C on A.FOLIO_ORDEN = C.FOLIO_ORDEN left join NOTAS_CREDITO D on A.FOLIO_ORDEN = D.FOLIO_ORDEN left join CARTA_PORTE CP on A.FOLIO_ORDEN = CP.FOLIO_ORDEN where  A.FOLIO_EMPRESA in (";
	private static String detalleFacturasFiltros = "select A.ESTATUS_PAGO, A.NOMBRE_XML, A.NOMBRE_PDF, A.TIPO_ORDEN, A.CLAVE_PROVEEDOR, C.NOMBRE_XML as XML_COMPLE, C.NOMBRE_PDF as PDF_COMPLE, D.NOMBRE_XML as XML_NOTA, D.NOMBRE_PDF as PDF_NOTA from ORDENES A inner join PROVEEDORES B on  A.CLAVE_PROVEEDOR =  B.CLAVE_PROVEEDOR  left join COMPLEMENTARIA C on A.FOLIO_ORDEN = C.FOLIO_ORDEN left join NOTAS_CREDITO D on A.FOLIO_ORDEN = D.FOLIO_ORDEN left join CARTA_PORTE CP on A.FOLIO_ORDEN = CP.FOLIO_ORDEN ";
	private static String detalleLayOut = "select A.FOLIO_EMPRESA, A.ESTATUS_PAGO, A.MONTO, A.TIPO_MONEDA, A.UUID, A.SERIE, A.TOTAL, A.SUB_TOTAL, A.IVA, A.IVA_RET, A.ISR_RET, A.FECHA_FACTURA, B.TIPO_PROVEEDOR, B.PAGO_DOLARES, B.CONCEPTO_SERVICIO, A.TIPO_ORDEN, C.ID_REGISTRO, D.FOLIO_EMPRESA  from ORDENES A inner join PROVEEDORES B on  A.CLAVE_PROVEEDOR =  B.CLAVE_PROVEEDOR  left join CONSECUTIVO_ORDENES C on A.FOLIO_EMPRESA = C.ID_REGISTRO left join DESCARTES D on A.FOLIO_EMPRESA = D.FOLIO_EMPRESA where A.FOLIO_EMPRESA in (";
	private static String detalleLayOutFiltros = "select A.FOLIO_EMPRESA, A.ESTATUS_PAGO, A.MONTO, A.TIPO_MONEDA, A.UUID, A.SERIE, A.TOTAL, A.SUB_TOTAL, A.IVA, A.IVA_RET, A.ISR_RET, A.FECHA_FACTURA, B.TIPO_PROVEEDOR, B.PAGO_DOLARES, B.CONCEPTO_SERVICIO, A.TIPO_ORDEN, C.ID_REGISTRO, D.FOLIO_EMPRESA  from ORDENES A inner join PROVEEDORES B on  A.CLAVE_PROVEEDOR =  B.CLAVE_PROVEEDOR  left join CONSECUTIVO_ORDENES C on A.FOLIO_EMPRESA = C.ID_REGISTRO left join DESCARTES D on A.FOLIO_EMPRESA = D.FOLIO_EMPRESA ";
	
	public static String getDetallePlantilla(String esquema) {
		return detallePlantilla.replaceAll("<<esquema>>", esquema);
	}

	public static String getDetallePlantillaFiltros(String esquema) {
		return detallePlantillaFiltros.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleFacturas(String esquema) {
		return detalleFacturas.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleFacturasFiltros(String esquema) {
		return detalleFacturasFiltros.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleLayOut(String esquema) {
		return detalleLayOut.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleLayOutFiltros(String esquema) {
		return detalleLayOutFiltros.replaceAll("<<esquema>>", esquema);
	}
	
	
	
}
