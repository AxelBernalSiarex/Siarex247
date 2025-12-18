package com.siarex247.cumplimientoFiscal.ConciliacionBoveda;

public class ConciliacionBovedaQuerys {

	
	private static String detalleConciliacion_Complemento    =  "select  A.ID_REGISTRO, A.EMISOR_RFC, A.EMISOR_NOMBRE, A.DES_TIPO_MONEDA, A.SERIE, C.FECHA_PAGO, C.FECHA_TIMBRADO, C.UUID_FACTURA, A.SUB_TOTAL, A.TRANSLADO_IVA, A.RETENCION_IVA, A.TOTAL, C.UUID_COMPLEMENTO, C.TOTAL_FACTURA, C.TOTAL_COMPLEMENTO, C.ESTATUS_COMPLEMENTO, C.NOMBRE_XML, C.NOMBRE_PDF, C.ESTATUS_CONCILIACION, A.FECHA_FACTURA from BOVEDA A inner join COMPLEMENTARIA_BOVEDA C on A.UUID = C.UUID_FACTURA where C.FECHA_PAGO between ? and ? ";
	private static String detalleConciliacion_SinComplemento =  "select  A.ID_REGISTRO, A.EMISOR_RFC, A.EMISOR_NOMBRE, A.DES_TIPO_MONEDA, A.SERIE, C.FECHA_PAGO, C.FECHA_TIMBRADO, A.UUID, A.SUB_TOTAL, A.TRANSLADO_IVA, A.RETENCION_IVA, A.TOTAL, C.UUID_COMPLEMENTO, A.TOTAL, C.TOTAL_COMPLEMENTO, C.ESTATUS_COMPLEMENTO, C.NOMBRE_XML, C.NOMBRE_PDF, C.ESTATUS_CONCILIACION, A.FECHA_FACTURA from BOVEDA A left join COMPLEMENTARIA_BOVEDA C on A.UUID = C.UUID_FACTURA where A.FECHA_FACTURA between ? and ? and A.TIPO_COMPROBANTE in ('I','ingreso', 'Ingreso') and C.ESTATUS_CONCILIACION is NULL ";
	private static String detalleConciliacion_ConSinComplemento =  "select  A.ID_REGISTRO, A.EMISOR_RFC, A.EMISOR_NOMBRE, A.DES_TIPO_MONEDA, A.SERIE, C.FECHA_PAGO, C.FECHA_TIMBRADO, A.UUID, A.SUB_TOTAL, A.TRANSLADO_IVA, A.RETENCION_IVA, A.TOTAL, C.UUID_COMPLEMENTO, A.TOTAL, C.TOTAL_COMPLEMENTO, C.ESTATUS_COMPLEMENTO, C.NOMBRE_XML, C.NOMBRE_PDF, C.ESTATUS_CONCILIACION, A.FECHA_FACTURA from BOVEDA A left join COMPLEMENTARIA_BOVEDA C on A.UUID = C.UUID_FACTURA where A.FECHA_FACTURA between ? and ? and A.TIPO_COMPROBANTE in ('I','ingreso', 'Ingreso') ";
	
	
	private static String consultaBovedaRegistro =  "select ID_REGISTRO, UUID, SERIE, FOLIO,FECHA_FACTURA, FORMA_PAGO, METODO_PAGO, TIPO_MONEDA, DES_TIPO_MONEDA, SUB_TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, TOTAL, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RETENCION_IVA, TRANSLADO_IVA, ISR_RET, TRANSLADOS_IEPS, NOMBRE_XML, TIPO_COMPROBANTE from BOVEDA where ID_REGISTRO = ?";
	private static String consultaBovedaUUID =  "select ID_REGISTRO, UUID, SERIE, FOLIO,FECHA_FACTURA, FORMA_PAGO, METODO_PAGO, TIPO_MONEDA, DES_TIPO_MONEDA, SUB_TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, TOTAL, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RETENCION_IVA, TRANSLADO_IVA, ISR_RET, TRANSLADOS_IEPS, NOMBRE_XML, TIPO_COMPROBANTE from BOVEDA where UUID = ?";
	
	
	
	public static String getDetalleConciliacion_Complemento(String esquema) {
		return detalleConciliacion_Complemento.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleConciliacion_SinComplemento(String esquema) {
		return detalleConciliacion_SinComplemento.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleConciliacion_ConSinComplemento(String esquema) {
		return detalleConciliacion_ConSinComplemento.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaBovedaRegistro(String esquema) {
		return consultaBovedaRegistro.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getConsultaBovedaUUID(String esquema) {
		return consultaBovedaUUID.replaceAll("<<esquema>>", esquema);
	}
	
}
