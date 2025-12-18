package com.siarex247.validaciones;

public class ValidacionesQuerys {

	
	// private static String banderaProveedor   = "select SERVICIOS_ESPECIALIZADOS from PROVEEDORES where CLAVE_PROVEEDOR = ?";
	private static String tieneComplementoPendiente =  "select A.FECHA_RECEPCION from ORDENES A inner join PROVEEDORES P on A.CLAVE_PROVEEDOR  = ? and A.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR left join COMPLEMENTARIA C on A.FOLIO_EMPRESA = C.FOLIO_EMPRESA  where A.ESTATUS_PAGO = ? and A.FECHA_RECEPCION between ? and ? and A.OMITIR_COMPLEMENTO = ? and C.FOLIO_EMPRESA is NULL";
	private static String validaUUID	   = "select FOLIO_EMPRESA from ORDENES where upper(UUID) = upper(?)";
	private static String validaSERIE	   = "select FOLIO_EMPRESA from ORDENES where CLAVE_PROVEEDOR = ? and SERIE = ?";
	private static String queryValidaRFC   =   "select ID_REGISTRO from USO_CFDI where RFC = ?";
	private static String queryValidaUSO   =   "select ID_REGISTRO from USO_CFDI where RFC = ? and USO_CFDI = ?";
	private static String queryDeleteClaveProductoXML =  "delete from CLAVE_PRODUC_SERVICIO_XML where FOLIO_EMPRESA = ?";
	private static String queryInsertClaveProductoXML =  "insert into CLAVE_PRODUC_SERVICIO_XML (FOLIO_EMPRESA, USO_CFDI, CLAVE_PROD_SERV, IMPORTE, VALOR_UNITARIO, DESCRIPCION, UNIDAD, CLAVE_UNIDAD, CANTIDAD, NUM_IDENTIFICACION) values (?,?,?,?,?,?,?,?,?,?)";
	private static String queryValidaCLAVE   =   "select CLAVE_PROD_SERV from CLAVE_PRODUC_SERVICIO_XML where FOLIO_EMPRESA = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI where RFC = ? and USO_CFDI = ?) group by CLAVE_PROD_SERV";
	private static String generaOrden = "insert into ORDENES (FOLIO_EMPRESA, RFC, MONTO) VALUES (?,?,?)";
	private static String actualizaOrden  = "update ORDENES set  FOLIO_EMPRESA = ?, ESTATUS_PAGO = ?, SERIE = ?, TOTAL = ?, SUB_TOTAL = ?, IVA = ?, IVA_RET = ?, ISR_RET = ?, IMP_LOCALES = ?, NOMBRE_XML = ?, NOMBRE_PDF = ?, FECHAULTMOV = current_timestamp, SERVICIO_PRODUCTO = ?, UUID = ?, FECHA_UUID = ?, USUARIO_CAMBIO = ?, ESTADO_SAT = ?, ESTATUS_SAT = ?, FECHA_FACTURA = ?, USO_CFDI = ?, CLAVE_PRODUCTO_SERVICIO = ?, FECHAPAGO = ?  where FOLIO_ORDEN = ?";
	private static String queryInsertClaveProductoXML_Multiple =  "insert into CLAVE_PRODUC_SERVICIO_XML_MULTIPLE (ID_MULTIPLE, USO_CFDI, CLAVE_PROD_SERV, IMPORTE, VALOR_UNITARIO, DESCRIPCION, UNIDAD, CLAVE_UNIDAD, CANTIDAD, NUM_IDENTIFICACION) values (?,?,?,?,?,?,?,?,?,?)";
	private static String queryValidaCLAVE_Multiple   =   "select CLAVE_PROD_SERV from CLAVE_PRODUC_SERVICIO_XML_MULTIPLE where ID_MULTIPLE = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI where RFC = ? and USO_CFDI = ?) group by CLAVE_PROD_SERV";
	
	/*
	public static String getBanderaProveedor(String esquema) {
		return banderaProveedor.replaceAll("<<esquema>>", esquema);
	}
	*/
	
	public static String getTieneComplementoPendiente(String esquema) {
		return tieneComplementoPendiente.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getValidaUUID(String esquema) {
		return validaUUID.replaceAll("<<esquema>>", esquema);
	}

	public static String getValidaSERIE(String esquema) {
		return validaSERIE.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryValidaRFC(String esquema) {
		return queryValidaRFC.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryValidaUSO(String esquema) {
		return queryValidaUSO.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryDeleteClaveProductoXML(String esquema, String aaa) {
		return queryDeleteClaveProductoXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInsertClaveProductoXML(String esquema, String aaa) {
		return queryInsertClaveProductoXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryValidaCLAVE(String esquema) {
		return queryValidaCLAVE.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getGeneraOrden(String esquema) {
		return generaOrden.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryActualizaOrden(String esquema) {
		return actualizaOrden.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInsertClaveProductoXML_Multiple(String esquema) {
		return queryInsertClaveProductoXML_Multiple.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryValidaCLAVE_Multiple(String esquema) {
		return queryValidaCLAVE_Multiple.replaceAll("<<esquema>>", esquema);
	}
}
