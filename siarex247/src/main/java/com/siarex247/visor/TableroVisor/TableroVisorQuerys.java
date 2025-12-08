package com.siarex247.visor.TableroVisor;

public class TableroVisorQuerys {

	
	private static String totalOrdenes =  "select count(*) from ORDENES ";
	private static String totalOrdenesXmoneda =  "select sum(MONTO) from ORDENES where TIPO_MONEDA = ?";
	private static String totalEstatus =  "select count(*), ESTATUS_PAGO from ORDENES ";
	private static String totalFacturas =  "select sum(T.UNO) from (select SERIE, 1 AS UNO from ORDENES <<CLAVE_PROVEEDOR>>  group by SERIE) AS T";
	private static String totalComplementos =  "select sum(T.UNO) from (select A.UUID_COMPLEMENTO, 1 AS UNO from COMPLEMENTARIA A inner join ORDENES B on A.FOLIO_EMPRESA = B.FOLIO_EMPRESA <<CLAVE_PROVEEDOR>> group by A.UUID_COMPLEMENTO) as T";
	private static String totalNotaCredito =  "select sum(T.UNO) from ( select A.UUID_CREDITO, 1 AS UNO from NOTAS_CREDITO A inner join ORDENES B on A.FOLIO_EMPRESA = B.FOLIO_EMPRESA <<CLAVE_PROVEEDOR>> group by A.UUID_CREDITO) AS T";
	private static String totalProveedores =  "select count(*) from PROVEEDORES where ESTATUS_REGISTRO = ?";
	private static String topProveedoresPagados =  "select sum(TOTAL) as TOTAL_PAGADO, CLAVE_PROVEEDOR from ORDENES where TIPO_MONEDA = ? and ESTATUS_PAGO = ? <<CLAVE_PROVEEDOR>> group by CLAVE_PROVEEDOR";
	private static String listaTopProveedores =  "select B.RAZON_SOCIAL, B.RFC,  A.CLAVE_PROVEEDOR, sum(A.MONTO) as TOTAL_MONTO, sum(A.TOTAL) as TOTAL_FACTURADO from ORDENES A inner join PROVEEDORES B on A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR  where TIPO_MONEDA = ? <<CLAVE_PROVEEDOR>> group by A.CLAVE_PROVEEDOR order by TOTAL_MONTO desc";
	private static String listaOrdenes		 =  "select B.RAZON_SOCIAL, B.RFC, A.FOLIO_ORDEN, A.FOLIO_EMPRESA, A.TIPO_MONEDA, A.MONTO, A.ESTATUS_PAGO from ORDENES A inner join PROVEEDORES B on A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR <<CLAVE_PROVEEDOR>> order by FECHAULTMOV desc limit 5000 ";
	
	public static String getTotalOrdenes(String esquema) {
		return totalOrdenes.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTotalOrdenesXmoneda(String esquema) {
		return totalOrdenesXmoneda.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getTotalEstatus(String esquema) {
		return totalEstatus.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTotalFacturas(String esquema, String _WHERE_PROVEEDOR) {
		return totalFacturas.replaceAll("<<esquema>>", esquema).replaceAll("<<CLAVE_PROVEEDOR>>", _WHERE_PROVEEDOR);
	}
	
	public static String getTotalComplementos(String esquema, String _WHERE_PROVEEDOR) {
		return totalComplementos.replaceAll("<<esquema>>", esquema).replaceAll("<<CLAVE_PROVEEDOR>>", _WHERE_PROVEEDOR);
	}
	
	public static String getTotalNotaCredito(String esquema, String _WHERE_PROVEEDOR) {
		return totalNotaCredito.replaceAll("<<esquema>>", esquema).replaceAll("<<CLAVE_PROVEEDOR>>", _WHERE_PROVEEDOR);
	}
	
	public static String getTotalProveedores(String esquema) {
		return totalProveedores.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTopProveedoresPagados(String esquema, String _WHERE_PROVEEDOR) {
		return topProveedoresPagados.replaceAll("<<esquema>>", esquema).replaceAll("<<CLAVE_PROVEEDOR>>", _WHERE_PROVEEDOR);
	}
	
	public static String getListaTopProveedores(String esquema, String _WHERE_PROVEEDOR) {
		return listaTopProveedores.replaceAll("<<esquema>>", esquema).replaceAll("<<CLAVE_PROVEEDOR>>", _WHERE_PROVEEDOR);
	}
	
	public static String getListaOrdenes(String esquema, String _WHERE_PROVEEDOR) {
		return listaOrdenes.replaceAll("<<esquema>>", esquema).replaceAll("<<CLAVE_PROVEEDOR>>", _WHERE_PROVEEDOR);
	}
}
