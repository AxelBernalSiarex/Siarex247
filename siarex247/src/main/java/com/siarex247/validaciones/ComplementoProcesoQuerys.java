package com.siarex247.validaciones;

public class ComplementoProcesoQuerys {

	
	private static String buscarRespaldo =  "select TIPO_ENVIO from HISTORICO_PROCESOS where TIPO_PROCESO = ? and date(FECHAALTA) = ?";
	private static String grabarProceso =  "insert into HISTORICO_PROCESOS (TIPO_PROCESO, TIPO_ENVIO, TOTAL_ENVIOS, ESTATUS) values (?,?,?,?)";
	//private static String obtenerPagadas =  "select A.FOLIO_EMPRESA, A.TOTAL, A.UUID, A.SERIE, A.TIPO_ORDEN, P.RFC, P.RAZON_SOCIAL, P.EMAIL, C.FOLIO_EMPRESA, C.TOTAL_COMPLEMENTO, A.FECHAPAGO from ORDENES A inner join PROVEEDORES P on A.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR left join COMPLEMENTARIA C on A.FOLIO_EMPRESA = C.FOLIO_EMPRESA  where A.ESTATUS_PAGO = ? and A.FECHAPAGO between ? and ? and P.TIPO_PROVEEDOR = ? order by A.CLAVE_PROVEEDOR, A.FECHAULTMOV";
	private static String obtenerPagadas =  "select A.FOLIO_EMPRESA, A.TOTAL, A.UUID, A.SERIE, A.TIPO_ORDEN, P.RFC, P.RAZON_SOCIAL, P.EMAIL, A.FECHAPAGO from ORDENES A inner join PROVEEDORES P on A.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR  where A.ESTATUS_PAGO = ? and A.FECHAPAGO between ? and ? and P.TIPO_PROVEEDOR = ? order by A.CLAVE_PROVEEDOR, A.FECHAULTMOV";
	
	
	
	public static String getBuscarRespaldo(String esquema) {
		return buscarRespaldo.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGrabarProceso(String esquema) {
		return grabarProceso.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getObtenerPagadas(String esquema) {
		return obtenerPagadas.replaceAll("<<esquema>>", esquema);
	}
	
}
