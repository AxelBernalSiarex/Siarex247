package com.siarex247.validaciones;

public class ValidacionesComplementoQuerys {

	private static String buscarOrdenUUID =  "select FOLIO_ORDEN, FOLIO_EMPRESA, MONTO, FECHAPAGO, CLAVE_PROVEEDOR, TIPO_ORDEN, ESTATUS_PAGO from ORDENES where UUID = ?";
	private static String deleteComplementariaTrabajo =  "delete from TRABAJO_COMPLEMENTARIA where UUID_COMPLEMENTO = ?";
	private static String buscarComplementoUUIDTrabajo =  "select UUID_COMPLEMENTO, ESTATUS from TRABAJO_COMPLEMENTARIA where FOLIO_EMPRESA = ?";
	private static String buscarComplementoUUID =  "select UUID_COMPLEMENTO, ESTATUS from COMPLEMENTARIA where FOLIO_EMPRESA = ?";
	private static String grabarComplementariaTrabajo =  "insert into TRABAJO_COMPLEMENTARIA (FOLIO_ORDEN, FOLIO_EMPRESA, UUID_COMPLEMENTO, TOTAL_PAGADO,TOTAL_COMPLEMENTO, FECHA_PAGO, FECHA_TIMBRADO, ESTATUS_COMPLEMENTO, NOMBRE_XML,NOMBRE_PDF, USUARIO_TRASACCION, TIPO_COMPROBANTE, FORMA_PAGO)  select  FOLIO_ORDEN, FOLIO_EMPRESA, ?, ?, ?, ?, ?, ?,?,?,?,?,? from ORDENES where UUID = ?";
	private static String deleteComplementaria =  "delete from COMPLEMENTARIA where UUID_COMPLEMENTO = ?";
	private static String updateComplementariaTrabajo =  "update TRABAJO_COMPLEMENTARIA set ESTATUS_CONCILIACION = ?, TOTAL_COMPLEMENTO = ?  where UUID_COMPLEMENTO = ?";
	private static String obtenerFechaFactura    =  "select B.FOLIO_EMPRESA, B.FECHAPAGO from COMPLEMENTARIA A inner join ORDENES B on A.FOLIO_ORDEN = B.FOLIO_ORDEN where A.UUID_COMPLEMENTO =  ?";
	private static String totalOrdenesTrabajo           =  "select B.TOTAL, B.TIPO_ORDEN, B.FECHAPAGO, B.UUID, A.TOTAL_PAGADO, B.FOLIO_ORDEN from TRABAJO_COMPLEMENTARIA A inner join ORDENES B on A.FOLIO_ORDEN = B.FOLIO_ORDEN where A.UUID_COMPLEMENTO =  ? order by B.TIPO_ORDEN";
	private static String detalleComplementariaTrabajo =  "select B.FOLIO_EMPRESA, B.TOTAL, A.TOTAL_PAGADO, B.FECHAPAGO, B.UUID from TRABAJO_COMPLEMENTARIA A inner join ORDENES B on A.FOLIO_EMPRESA = B.FOLIO_EMPRESA where UUID_COMPLEMENTO = ?";
	private static String updateEstatus =  "insert into  COMPLEMENTARIA (select FOLIO_ORDEN, FOLIO_EMPRESA, UUID_COMPLEMENTO, TOTAL_PAGADO, TOTAL_COMPLEMENTO, ESTATUS_COMPLEMENTO, FECHA_PAGO, FECHA_TIMBRADO, NOMBRE_XML, NOMBRE_PDF, TIPO_COMPROBANTE, FORMA_PAGO, ESTATUS_CONCILIACION, 'OK',FECHA_TRASACCION, USUARIO_TRASACCION from TRABAJO_COMPLEMENTARIA where  UUID_COMPLEMENTO = ?)";
	private static String consultaComplemento=  "select C.RFC, C.RAZON_SOCIAL, A.UUID_COMPLEMENTO, A.ESTATUS_COMPLEMENTO, A.NOMBRE_XML, A.NOMBRE_PDF, C.CLAVE_PROVEEDOR from COMPLEMENTARIA A inner join ORDENES B on A.FOLIO_ORDEN = B.FOLIO_ORDEN inner join PROVEEDORES C on B.CLAVE_PROVEEDOR = C.CLAVE_PROVEEDOR where A.FOLIO_EMPRESA = ?";
	private static String listadoOrdenesComplemento =  "select B.FOLIO_EMPRESA, B.TOTAL, A.TOTAL_PAGADO, B.FECHAPAGO, B.UUID from COMPLEMENTARIA A inner join ORDENES B on A.FOLIO_EMPRESA = B.FOLIO_EMPRESA where UUID_COMPLEMENTO = ? ";
	private static String tieneOmitirComplemento    =  "select OMITIR_COMPLEMENTO from ORDENES where FOLIO_EMPRESA = ?";
	private static String actualizarEstatusOrdenComplemento    =  "update ORDENES A inner join COMPLEMENTARIA B on A.FOLIO_EMPRESA = B.FOLIO_EMPRESA set A.ESTATUS_PAGO = ?, USUARIO_CAMBIO = ?, FECHAULTMOV = current_timestamp where B.UUID_COMPLEMENTO = ?";
	
	
	public static String getBuscarOrdenUUID(String esquema) {
		return buscarOrdenUUID.replaceAll("<<esquema>>", esquema);
	}
	
	public static String deleteComplementariaTrabajo(String esquema) {
		return deleteComplementariaTrabajo.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getBuscarComplementoUUIDTrabajo(String esquema) {
		return buscarComplementoUUIDTrabajo.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getBuscarComplementoUUID(String esquema) {
		return buscarComplementoUUID.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGrabarComplementariaTrabajo(String esquema) {
		return grabarComplementariaTrabajo.replaceAll("<<esquema>>", esquema);
	}
	
	public static String deleteComplementaria(String esquema) {
		return deleteComplementaria.replaceAll("<<esquema>>", esquema);
	}
	
	public static String updateComplementariaTrabajo(String esquema) {
		return updateComplementariaTrabajo.replaceAll("<<esquema>>", esquema);
	}
	
	public static String obtenerFechaFactura(String esquema) {
		return obtenerFechaFactura.replaceAll("<<esquema>>", esquema);
	}
	
	public static String totalOrdenesTrabajo(String esquema) {
		return totalOrdenesTrabajo.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDetalleComplementariaTrabajo(String esquema) {
		return detalleComplementariaTrabajo.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getUpdateEstatus(String esquema) {
		return updateEstatus.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaComplemento(String esquema) {
		return consultaComplemento.replaceAll("<<esquema>>", esquema);
	}

	public static String getListadoOrdenesComplemento(String esquema) {
		return listadoOrdenesComplemento.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getTieneOmitirComplemento(String esquema) {
		return tieneOmitirComplemento.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizarEstatusOrdenComplemento(String esquema) {
		return actualizarEstatusOrdenComplemento.replaceAll("<<esquema>>", esquema);
	}
	
	
}
