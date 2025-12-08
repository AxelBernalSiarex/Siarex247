package com.siarex247.configSistema.CierreAnnio;

public class CierreAnnioQuerys {

	private static String eliminaAnio  = "delete from CIERRE_ANIO where ANIO = ? and TIPO_CIERRE = ?";
	private static String cierreAnio  = "insert into CIERRE_ANIO (ANIO, FECHA_APARTIR, MENSAJE_APARTIR, FECHA_HASTA, MENSAJE_HASTA, TIPO_CIERRE, USUARIO) values (?,?,?,?,?,?,?)";
	private static String obtenerInfoCierre  = "select  ANIO, FECHA_APARTIR, MENSAJE_APARTIR, FECHA_HASTA, MENSAJE_HASTA, USUARIO from CIERRE_ANIO where ANIO = ? and TIPO_CIERRE = ?";
	
	public static String getQueryEliminaAnio(String esquema) {
		return eliminaAnio.replaceAll("<<esquema>>", esquema).toUpperCase();
	}
	
	public static String getQueryCierreAnio(String esquema) {
		return cierreAnio.replaceAll("<<esquema>>", esquema).toUpperCase();
	}
	
	
	public static String getQueryObtenerInfoCierre(String esquema) {
		return obtenerInfoCierre.replaceAll("<<esquema>>", esquema).toUpperCase();
	}
}
