package com.siarex247.layOut.ContraRecibos;

public class ContraRecibosQuerys {

	private static String actualizaOrdenRecibo  = "update ORDENES set MONTO = ?, ESTATUS_PAGO = ?, SERVICIO_PRODUCTO = ?, USUARIO = ?, FECHAULTMOV = current_timestamp, FECHA_RECIBO = current_timestamp, FECHAPAGO = ? where FOLIO_EMPRESA = ?";
	private static String infoCuentasPagarRecibo = "select NOMBRE_XML, ESTATUS_PAGO, MONTO from ORDENES where FOLIO_EMPRESA = ?";
	
	
	
	public static String getQueryActualizaOrdenRecibo(String esquema) {
		return actualizaOrdenRecibo.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryInfoCuentasPagarRecibo(String esquema) {
		return infoCuentasPagarRecibo.replaceAll("<<esquema>>", esquema);
	}
	
	
}
