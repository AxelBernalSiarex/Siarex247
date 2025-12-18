package com.siarex247.cumplimientoFiscal.ConciliacionCP;

public class ConciliacionCPQuerys {

	
	private static String detalleConSinCP         =  "select P.RFC, P.RAZON_SOCIAL, A.FOLIO_ORDEN, A.FOLIO_EMPRESA, A.TIPO_MONEDA, A.SERIE, A.FECHAPAGO,  C.FECHA_PAGO, C.FECHA_TIMBRADO, A.UUID, A.SUB_TOTAL, A.IVA, A.IVA_RET, A.IMP_LOCALES, A.TOTAL, C.UUID, C.TOTAL_PAGADO, A.ESTATUS_SAT, C.ESTATUS_CARTA_PORTE, C.NOMBRE_XML, C.NOMBRE_PDF, C.ESTATUS_CONCILIACION, P.CLAVE_PROVEEDOR, A.TIPO_ORDEN from ORDENES A inner join PROVEEDORES P on A.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR and CARTA_PORTE = ? left join CARTA_PORTE C on A.FOLIO_EMPRESA = C.FOLIO_EMPRESA  where A.ESTATUS_PAGO in (?, ?) and A.FECHAPAGO between ? and ? ";
	private static String detalleConCP            =  "select P.RFC, P.RAZON_SOCIAL, A.FOLIO_ORDEN, A.FOLIO_EMPRESA, A.TIPO_MONEDA, A.SERIE, A.FECHAPAGO,  C.FECHA_PAGO, C.FECHA_TIMBRADO, A.UUID, A.SUB_TOTAL, A.IVA, A.IVA_RET, A.IMP_LOCALES, A.TOTAL, C.UUID, C.TOTAL_PAGADO, A.ESTATUS_SAT, C.ESTATUS_CARTA_PORTE, C.NOMBRE_XML, C.NOMBRE_PDF, C.ESTATUS_CONCILIACION, P.CLAVE_PROVEEDOR, A.TIPO_ORDEN from ORDENES A inner join PROVEEDORES P on A.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR inner join CARTA_PORTE C on A.FOLIO_EMPRESA = C.FOLIO_EMPRESA  where A.ESTATUS_PAGO in (?, ?) and A.FECHAPAGO between ? and ? ";
	private static String detalleSinCP            =  "select  P.RFC, P.RAZON_SOCIAL, A.FOLIO_ORDEN, A.FOLIO_EMPRESA, A.TIPO_MONEDA, A.SERIE, A.FECHAPAGO,  C.FECHA_PAGO, C.FECHA_TIMBRADO, A.UUID, A.SUB_TOTAL, A.IVA, A.IVA_RET, A.IMP_LOCALES, A.TOTAL, C.UUID, C.TOTAL_PAGADO, A.ESTATUS_SAT, C.ESTATUS_CARTA_PORTE, C.NOMBRE_XML, C.NOMBRE_PDF, C.ESTATUS_CONCILIACION, P.CLAVE_PROVEEDOR, A.TIPO_ORDEN from ORDENES A inner join PROVEEDORES P on A.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR left join CARTA_PORTE C on A.FOLIO_EMPRESA = C.FOLIO_EMPRESA  where A.ESTATUS_PAGO in (?, ?) and A.FECHAPAGO between ? and ? and C.FOLIO_ORDEN is NULL ";
	private static String buscarDocsFolioEmpresa  =  "select NOMBRE_XML, NOMBRE_PDF, ESTATUS, UUID from CARTA_PORTE where FOLIO_EMPRESA = ?";
	

	
	public static String getDetalleConSinCP(String esquema) {
		return detalleConSinCP.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleConCP(String esquema) {
		return detalleConCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDetalleSinCP(String esquema) {
		return detalleSinCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getBuscarDocumentosFolioEmpresa(String esquema) {
		return buscarDocsFolioEmpresa.replaceAll("<<esquema>>", esquema);
	}
	
}
