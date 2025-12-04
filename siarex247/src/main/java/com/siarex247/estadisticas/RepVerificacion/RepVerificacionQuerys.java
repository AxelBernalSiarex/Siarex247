package com.siarex247.estadisticas.RepVerificacion;

public class RepVerificacionQuerys {

	private static String listaBitacora = "select ID_REGISTRO, DESCRIPCION, VALIDAR_SAT_FACTURA, VALIDAR_SAT_COMPLEMENTO, VALIDAR_SAT_NOTA_CREDITO, FECHA_INICIO_PROCESO, FECHA_FIN_PROCESO, ESTATUS, TOTAL_PROCESAR, TOTAL_PROCESADOS from BITACORA_ESTADISTICAS order by FECHA_INICIO_PROCESO desc";
	private static String insertBitacora = "insert into BITACORA_ESTADISTICAS (DESCRIPCION, VALIDAR_SAT_FACTURA, VALIDAR_SAT_COMPLEMENTO, VALIDAR_SAT_NOTA_CREDITO, FECHA_INICIO_DATOS, FECHA_FIN_DATOS, FECHA_INICIO_PROCESO, ESTATUS, TIPO_SELECCION, USUARIO) values (?,?,?,?,?,?,current_timestamp,?,?,?)";
	private static String listadoOrdenes = "    select A.FOLIO_ORDEN, A.CLAVE_PROVEEDOR, A.NOMBRE_XML, C.NOMBRE_XML, D.NOMBRE_XML from ORDENES A inner join PROVEEDORES B on A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR and B.TIPO_PROVEEDOR = 'MEX' left join COMPLEMENTARIA C on A.FOLIO_ORDEN = C.FOLIO_ORDEN left join NOTAS_CREDITO D on A.FOLIO_ORDEN = D.FOLIO_ORDEN  where <<FECHA_FILTRO>> between ? and ? and ESTATUS_PAGO in ('A1','A3','A4')";
	private static String guardarEstadistica = "insert into ESTADISTICA_01 (ID_BITACORA, FOLIO_ORDEN, RFC_CFDI, RFC_COMPLEMENTO, RFC_NOTA, UUID_CFDI, UUID_COMPLEMENTO, UUID_NOTA, ESTADO_CFDI_SAT, ESTATUS_CFDI_SAT, ESTADO_COMPLEMENTO_SAT, ESTATUS_COMPLEMENTO_SAT, ESTADO_NOTA_SAT, ESTATUS_NOTA_SAT) values  ";
	private static String actualizaBitacora = " update BITACORA_ESTADISTICAS set FECHA_FIN_PROCESO = current_timestamp, ESTATUS = ? where ID_REGISTRO = ?";
	private static String generaReporte = "	select B.FOLIO_EMPRESA, C.RFC, C.RAZON_SOCIAL, B.CONCEPTO, B.TIPO_MONEDA, B.MONTO, B.SERVICIO_PRODUCTO, B.ESTATUS_PAGO, B.SERIE, B.TOTAL, B.SUB_TOTAL, B.IVA, B.IVA_RET, B.ISR_RET, B.IMP_LOCALES,  B.FECHAPAGO, B.FECHAULTMOV, B.FECHAREGISTRO, A.RFC_CFDI, B.UUID, A.UUID_CFDI, B.FECHA_UUID, A.ESTADO_CFDI_SAT, A.ESTATUS_CFDI_SAT, A.RFC_COMPLEMENTO, D.UUID_COMPLEMENTO, A.UUID_COMPLEMENTO, A.ESTADO_COMPLEMENTO_SAT, A.ESTATUS_COMPLEMENTO_SAT, A.RFC_NOTA, E.UUID_CREDITO, A.UUID_NOTA, A.ESTADO_NOTA_SAT, A.ESTATUS_NOTA_SAT from ESTADISTICA_01 A inner join ORDENES B on A.FOLIO_ORDEN = B.FOLIO_ORDEN inner join PROVEEDORES C on B.CLAVE_PROVEEDOR = C.CLAVE_PROVEEDOR left join COMPLEMENTARIA D on A.FOLIO_ORDEN = D.FOLIO_ORDEN left join NOTAS_CREDITO E on A.FOLIO_ORDEN = E.FOLIO_ORDEN  where A.ID_BITACORA = ? ";
	
	private static String actualizaAvance = " update BITACORA_ESTADISTICAS set TOTAL_PROCESAR = ?, TOTAL_PROCESADOS = ? where ID_REGISTRO = ?";
	private static String eliminarBitacora= " delete from BITACORA_ESTADISTICAS where ID_REGISTRO = ?";
	private static String eliminarEstadisticas = " delete from ESTADISTICA_01 where ID_BITACORA = ?";
	
	
	public static String getListaBitacora(String esquema) {
		return listaBitacora.replaceAll("<<esquema>>", esquema);
	}

	public static String getInsertBitacora(String esquema) {
		return insertBitacora.replaceAll("<<esquema>>", esquema);
	}

	public static String getListadoOrdenes(String esquema, String campoFecha) {
		return listadoOrdenes.replaceAll("<<esquema>>", esquema).replace("<<FECHA_FILTRO>>", campoFecha);
	}
	
	public static String getGuardarEstadistica(String esquema) {
		return guardarEstadistica.replaceAll("<<esquema>>", esquema);
	}

	public static String getActualizaBitacora(String esquema) {
		return actualizaBitacora.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getGeneraReporte(String esquema) {
		return generaReporte.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizaAvance(String esquema) {
		return actualizaAvance.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEliminarBitacora(String esquema) {
		return eliminarBitacora.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEliminarEstadisticas(String esquema) {
		return eliminarEstadisticas.replaceAll("<<esquema>>", esquema);
	}
	
}
