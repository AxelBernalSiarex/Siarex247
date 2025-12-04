package com.siarex247.layOut.PagosOrdenesCompra;

public class PagosOrdenesCompraQuerys {

	
	
	private static String actualizaOrdenPagada  = "update ORDENES set ESTATUS_PAGO = ?, USUARIO = ?, FECHAPAGO = ?, FECHA_RECEPCION = ?, FECHAULTMOV = current_timestamp where FOLIO_EMPRESA = ?";
	private static String infoCuentasPagar = "select ESTATUS_PAGO, CLAVE_PROVEEDOR, TOTAL, SERIE, UUID, TIPO_MONEDA, TIPO_ORDEN from ORDENES where FOLIO_EMPRESA = ?";
	private static String infoCuentasPagarMultiple = "select ESTATUS_PAGO, CLAVE_PROVEEDOR, TOTAL, SERIE, UUID, TIPO_MONEDA, FOLIO_EMPRESA, TIPO_ORDEN from ORDENES where TIPO_ORDEN = ? limit 1";
	private static String actualizaOrdenPagadaMultiple  = "update ORDENES set ESTATUS_PAGO = ?, USUARIO = ?, FECHAPAGO = ?, FECHAULTMOV = current_timestamp, FECHA_RECEPCION = ? where TIPO_ORDEN = ?";
	private static String totalOrdenesMultiples = "select count(*) from ORDENES where TIPO_ORDEN = ?";
	private static String envioOrdenesUsuario = "select A.FOLIO_EMPRESA, A.TIPO_MONEDA, A.SERIE, A.TOTAL, A.SUB_TOTAL, A.IVA, A.UUID, A.ID_EMPLEADO, A.CLAVE_PROVEEDOR, A.TIPO_ORDEN from ORDENES A, PROVEEDORES B where A.FECHAPAGO = ? and A.ID_EMPLEADO > ? and A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR and B.NOTIFICAR_PAGO = ? order by A.ID_EMPLEADO, A.FOLIO_EMPRESA";
	
	
	
	public static String getQueryActualizaOrdenPagada(String esquema) {
		return actualizaOrdenPagada.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInfoCuentasPagar(String esquema) {
		return infoCuentasPagar.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getInfoCuentasPagarMultiple(String esquema) {
		return infoCuentasPagarMultiple.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryActualizaOrdenPagadaMultiple(String esquema) {
		return actualizaOrdenPagadaMultiple.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTotalOrdenesMultiples(String esquema) {
		return totalOrdenesMultiples.replaceAll("<<esquema>>", esquema);
	}

	public static String getEnvioOrdenesUsuario(String esquema) {
		return envioOrdenesUsuario.replaceAll("<<esquema>>", esquema);
	}
	
}
