package com.siarex247.configuraciones.RegimenFiscal;

public class RegimenFiscalQuerys {

	
	private static String queryConsultaRF    = "select a.ID_REGISTRO, a.CLAVE_PROVEEDOR, c.CLAVE_REGIMEN, c.DESCRIPCION,  a.USUARIO_TRAN, a.FECHA_TRANS, b.NUMERO_PROVEEDOR, b.RAZON_SOCIAL from CONFIGURACION_REGIMEN_FISCAL a inner join PROVEEDORES b on a.CLAVE_PROVEEDOR = b.CLAVE_PROVEEDOR inner join REGIMEN_FISCAL c on a.CLAVE_REGIMEN = c.CLAVE_REGIMEN";
	private static String comboClavesRegimen = "select CLAVE_REGIMEN, DESCRIPCION from REGIMEN_FISCAL";
	private static String queryInsertRF      = "insert into CONFIGURACION_REGIMEN_FISCAL (CLAVE_PROVEEDOR, CLAVE_REGIMEN, USUARIO_TRAN) values (?,?,?)";
	private static String queryUpdateRF 	 = "update CONFIGURACION_REGIMEN_FISCAL set CLAVE_REGIMEN = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where ID_REGISTRO = ?";
	private static String queryBuscarConfRF  = "select CLAVE_PROVEEDOR, CLAVE_REGIMEN from CONFIGURACION_REGIMEN_FISCAL where ID_REGISTRO = ? ";
	private static String queryDeleteRF      = "delete from CONFIGURACION_REGIMEN_FISCAL where ID_REGISTRO = ?";
	private static String queryBuscarConf    = "select CLAVE_REGIMEN from CONFIGURACION_REGIMEN_FISCAL where CLAVE_PROVEEDOR = ? ";
	
	
	public static String getQueryConsultaRF(String esquema) {
		return queryConsultaRF.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getComboClavesRegimen(String esquema) {
		return comboClavesRegimen.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryInsertRF(String esquema) {
		return queryInsertRF.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryUpdateRF(String esquema) {
		return queryUpdateRF.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryBuscarConfRF(String esquema) {
		return queryBuscarConfRF.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryDelRF(String esquema) {
		return queryDeleteRF.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryBuscarConfigurados(String esquema) {
		return queryBuscarConf.replaceAll("<<esquema>>", esquema);
	}
}
