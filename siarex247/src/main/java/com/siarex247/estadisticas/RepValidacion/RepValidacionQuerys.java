package com.siarex247.estadisticas.RepValidacion;

public class RepValidacionQuerys {

	private static String listaBitacora = "select a.ID_REGISTRO, a.FOLIO_EMPRESA, b.RAZON_SOCIAL, a.ETIQUETA, a.VALOR_XML, a.ESTATUS, a.RESULTADO, a.FECHA_TRASACCION from ESTADISTICAS_CARTA_PORTE a, PROVEEDORES b where a.CLAVE_PROVEEDOR = b.CLAVE_PROVEEDOR";
	private static String generaReporte = "	select B.FOLIO_EMPRESA, C.RFC, C.RAZON_SOCIAL, B.CONCEPTO, B.TIPO_MONEDA, B.MONTO, B.SERVICIO_PRODUCTO, B.ESTATUS_PAGO, B.SERIE, B.TOTAL, B.SUB_TOTAL, B.IVA, B.IVA_RET, B.ISR_RET, B.IMP_LOCALES,  B.FECHAPAGO, B.FECHAULTMOV, B.FECHAREGISTRO, A.RFC_CFDI, B.UUID, A.UUID_CFDI, B.FECHA_UUID, A.ESTADO_CFDI_SAT, A.ESTATUS_CFDI_SAT, A.RFC_COMPLEMENTO, D.UUID_COMPLEMENTO, A.UUID_COMPLEMENTO, A.ESTADO_COMPLEMENTO_SAT, A.ESTATUS_COMPLEMENTO_SAT, A.RFC_NOTA, E.UUID_CREDITO, A.UUID_NOTA, A.ESTADO_NOTA_SAT, A.ESTATUS_NOTA_SAT from ESTADISTICA_01 A inner join ORDENES B on A.FOLIO_ORDEN = B.FOLIO_ORDEN inner join PROVEEDORES C on B.CLAVE_PROVEEDOR = C.CLAVE_PROVEEDOR left join COMPLEMENTARIA D on A.FOLIO_ORDEN = D.FOLIO_ORDEN left join NOTAS_CREDITO E on A.FOLIO_ORDEN = E.FOLIO_ORDEN  where A.ID_BITACORA = ? ";
		
	
	public static String getListaBitacora(String esquema) {
		return listaBitacora.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGeneraReporte(String esquema) {
		return generaReporte.replaceAll("<<esquema>>", esquema);
	}
}
