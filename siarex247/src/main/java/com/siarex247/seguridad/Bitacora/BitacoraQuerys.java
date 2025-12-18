package com.siarex247.seguridad.Bitacora;

public class BitacoraQuerys {

	private static String detalleHistorico = "select A.CLAVE_REGISTRO, A.TITULO_CORREO, A.TIPO_TAREA, A.ID_PROVEEDOR, B.RAZON_SOCIAL, A.SUBJECT, A.CLAVE_USUARIO, A.NOMBRE_USUARIO, FECHA_TAREA from HISTORICO_ENVIO_FORMATOS A inner join PROVEEDORES B on A.ID_PROVEEDOR = B.CLAVE_PROVEEDOR order by FECHA_TAREA desc";
	private static String detalleHistoricoBitacora		= "select B.CLAVE_REGISTRO, B.LLAVE, B.DESCRIPCION_ERROR  from IMPORTAR A inner join HISTORICO_IMPORTAR B on A.CLAVE_HISTORICO = B.CLAVE_HISTORICO where A.CLAVE_HISTORICO = ?";
	
	private static String altaBitacora 		= "insert into IMPORTAR (NOMBRE_ARCHIVO, TOTAL_REGISTROS, TIPO_CARGA, REGISTROS_OK, REGISTROS_NG, ESTATUS, CLAVE_USUARIO) values (?,?,?,?,?,?,?)";
	private static String altaHistorico		= "insert into HISTORICO_IMPORTAR (CLAVE_HISTORICO, LLAVE, TIPO_REGISTRO, DESCRIPCION_ERROR) values (?,?,?,?)";
	private static String updateBitacora 	= "update IMPORTAR set TOTAL_REGISTROS = ?, REGISTROS_OK = ?, REGISTROS_NG = ?, ESTATUS = ? where CLAVE_HISTORICO = ?";
	
	
	public static String getDetalleHistorico(String esquema) {
		return detalleHistorico.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleHistoricoBitacora(String esquema) {
		return detalleHistoricoBitacora.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getAltaBitacora(String esquema) {
		return altaBitacora.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getAltaHistorico(String esquema) {
		return altaHistorico.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getUpdateBitacora(String esquema) {
		return updateBitacora.replaceAll("<<esquema>>", esquema);
	}
	
	
}
