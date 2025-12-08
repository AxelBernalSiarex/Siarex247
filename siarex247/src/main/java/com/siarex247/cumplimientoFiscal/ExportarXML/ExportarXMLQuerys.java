package com.siarex247.cumplimientoFiscal.ExportarXML;

public class ExportarXMLQuerys {

	
//	private static String consultarUUID =  "select A.ESTATUS_PAGO, A.NOMBRE_XML, A.NOMBRE_PDF, A.CLAVE_PROVEEDOR, A.UUID, A.TOTAL, A.SUB_TOTAL, B.RAZON_SOCIAL, B.RFC, A.FOLIO_EMPRESA, A.TIPO_MONEDA, C.NOMBRE_XML, C.NOMBRE_PDF, C.TOTAL_COMPLEMENTO, UUID_COMPLEMENTO from ORDENES A inner join PROVEEDORES B on A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR left join COMPLEMENTARIA C on A.FOLIO_ORDEN = C.FOLIO_ORDEN where (A.FOLIO_EMPRESA = ? or upper(A.UUID) = ?)";
	private static String consultarUUID =  "select A.ESTATUS_PAGO, A.NOMBRE_XML, A.NOMBRE_PDF, A.CLAVE_PROVEEDOR, A.UUID, A.TOTAL, A.SUB_TOTAL, B.RAZON_SOCIAL, B.RFC, A.FOLIO_EMPRESA, A.TIPO_MONEDA, C.NOMBRE_XML, C.NOMBRE_PDF, C.TOTAL_COMPLEMENTO, C.UUID_COMPLEMENTO, D.NOMBRE_XML, D.NOMBRE_PDF, D.TOTAL_PAGADO, D.UUID_CREDITO from ORDENES A inner join PROVEEDORES B on A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR left join COMPLEMENTARIA C on A.FOLIO_ORDEN = C.FOLIO_ORDEN left join NOTAS_CREDITO D  on A.FOLIO_ORDEN = D.FOLIO_ORDEN where A.FOLIO_EMPRESA = ? or upper(A.UUID) = ? or C.UUID_COMPLEMENTO = ? or D.UUID_CREDITO = ?";
	private static String consultarRFC =  "select RFC from EMPRESAS where ESQUEMA = ?";
	private static String grabarDescarga =  "insert into DESCARGA_XML (RUTA_ARCHIVO, USUARIO) values (?,?)";
	private static String consultarDescarga =  "select RUTA_ARCHIVO, FECHAALTA from DESCARGA_XML where CLAVE_REGISTRO = ? and USUARIO = ?";
	
	
	private static String consultarBoveda =  "select UUID, EMISOR_NOMBRE, SUB_TOTAL, TOTAL, EMISOR_RFC, ID_REGISTRO, TIPO_COMPROBANTE, DES_TIPO_MONEDA from BOVEDA where EMISOR_RFC = ? and FECHA_FACTURA between ? and ?";
	private static String guardarBitacoraXML =  "insert into <<esquema>>.BITACORA_EXPORTAR_XML (UUID, RFC_RECEPTOR, RAZON_SOCIAL, TIPO_COMPROBANTE, MENSAJE_VALIDACION, CODE_OPERACION, USUARIO_TRAN) values (?,?,?,?,?,?,?)";
	private static String consultarBitacora =  "select UUID, RFC_RECEPTOR, RAZON_SOCIAL, TIPO_COMPROBANTE, MENSAJE_VALIDACION from <<esquema>>.BITACORA_EXPORTAR_XML where CODE_OPERACION = ?";
	private static String comboProveedores  =  "select EMISOR_RFC, EMISOR_NOMBRE from BOVEDA group by EMISOR_RFC, EMISOR_NOMBRE order by EMISOR_RFC";
	
	
	public static String getConsultarUUID(String esquema) {
		return consultarUUID.replaceAll("<<esquema>>", esquema);
	}
	
	
	
	public static String getConsultarRFC(String esquema) {
		return consultarRFC.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getGrabarDescarga(String esquema) {
		return grabarDescarga.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarDescarga(String esquema) {
		return consultarDescarga.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getConsultarBoveda(String esquema) {
		return consultarBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGuardarBitacoraXML(String esquema) {
		return guardarBitacoraXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarBitacora(String esquema) {
		return consultarBitacora.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getComboProveedores(String esquema) {
		return comboProveedores.replaceAll("<<esquema>>", esquema);
	}
	
}
