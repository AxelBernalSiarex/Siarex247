package com.siarex247.validaciones;

public class ValidacionesNotaCreditoQuerys {

	
	
	private static String buscarOrdenUUIDCredito =  "select FOLIO_ORDEN, FOLIO_EMPRESA, MONTO, FECHAPAGO, CLAVE_PROVEEDOR, TIPO_ORDEN, ESTATUS_PAGO, TIPO_MONEDA from ORDENES where UUID = ?";
	private static String eliminarNotasCredito =  "delete from NOTAS_CREDITO where UUID_CREDITO = ? and ESTATUS_NOTA = ?";
	private static String buscarUUIDCargado    =  "select A.FOLIO_ORDEN, N.UUID_CREDITO from NOTAS_CREDITO N inner join ORDENES A on A.FOLIO_ORDEN = N.FOLIO_ORDEN where A.UUID = ?";
	private static String grabarNota 		   =  "insert into NOTAS_CREDITO (FOLIO_ORDEN, FOLIO_EMPRESA, UUID_CREDITO,TOTAL_PAGADO, ESTATUS_NOTA,FECHA_PAGO,FECHA_TIMBRADO,NOMBRE_XML,NOMBRE_PDF,TIPO_COMPROBANTE,FORMA_PAGO,ESTATUS_ORDEN, ESTADO_SAT, ESTATUS_SAT, USUARIO_TRASACCION, SUB_TOTAL, IVA, ISR_RET) select FOLIO_ORDEN, FOLIO_EMPRESA, ?, ?, ?, ?, ?, ?, ?, ?, ?, ESTATUS_PAGO, ?, ?, ?, ?, ?, ? from ORDENES where UUID = ?";
	private static String detalleNotasCredito  =  "select B.FOLIO_EMPRESA, B.TOTAL, A.TOTAL_PAGADO, B.UUID, A.FECHA_PAGO from NOTAS_CREDITO A inner join ORDENES B on B.FOLIO_ORDEN = A.FOLIO_ORDEN where A.UUID_CREDITO = ?";
	private static String actualizarCredito =  "update NOTAS_CREDITO set ESTATUS_NOTA = ? where UUID_CREDITO = ?";
	private static String actualizarOrdenes =  "update ORDENES set ESTATUS_PAGO = ?, USUARIO_CAMBIO = ?, FECHAULTMOV = current_timestamp where FOLIO_ORDEN in (select FOLIO_ORDEN from NOTAS_CREDITO where UUID_CREDITO = ?)";
	private static String buscarInfoNotaCredito   =  "select A.FOLIO_EMPRESA, P.CLAVE_PROVEEDOR, P.RAZON_SOCIAL,A.SERIE, N.TOTAL_PAGADO, A.FECHA_FACTURA, N.ESTADO_SAT, N.ESTATUS_SAT, N.UUID_CREDITO,  N.NOMBRE_XML, N.NOMBRE_PDF, A.UUID, N.ESTATUS_NOTA, N.UUID_CREDITO, N.ESTATUS_ORDEN, N.SUB_TOTAL, N.IVA, N.ISR_RET from ORDENES A  inner join NOTAS_CREDITO N on N.FOLIO_ORDEN = A.FOLIO_ORDEN inner join PROVEEDORES P on A.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR where A.FOLIO_ORDEN = ?";
	private static String actualizarCreditoCuentas =  "update NOTAS_CREDITO set ESTATUS_CUENTAS = ? where UUID_CREDITO = ?";
	private static String actualizarOrdenesCuentas =  "update ORDENES set ESTATUS_PAGO = (select ESTATUS_ORDEN from NOTAS_CREDITO where UUID_CREDITO = ? limit 1), FECHAULTMOV = current_timestamp where UUID = ?";
	
	
	
	public static String getEliminarNotasCredito(String esquema) {
		return eliminarNotasCredito.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getBuscarOrdenUUIDCredito(String esquema) {
		return buscarOrdenUUIDCredito.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getBuscarUUIDCargado(String esquema) {
		return buscarUUIDCargado.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getGrabarNota(String esquema) {
		return grabarNota.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleNotasCredito(String esquema) {
		return detalleNotasCredito.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizarCredito(String esquema) {
		return actualizarCredito.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizarOrdenes(String esquema) {
		return actualizarOrdenes.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getBuscarInfoNotaCredito(String esquema) {
		return buscarInfoNotaCredito.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizarCreditoCuentas(String esquema) {
		return actualizarCreditoCuentas.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizarOrdenesCuentas(String esquema) {
		return actualizarOrdenesCuentas.replaceAll("<<esquema>>", esquema);
	}
}
