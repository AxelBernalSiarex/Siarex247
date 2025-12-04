package com.siarex247.configSistema.CorreosRespaldo;

public class CorreosRespaldoQuerys {

	private static String obtenerVarible =  "select VARIABLE, DESCRIPCION, CONTENIDO from CONF_SISTEMA";
	private static String eliminaVarible =  "delete from CONF_SISTEMA where VARIABLE = ?";
	private static String insertVarible =  "insert into CONF_SISTEMA (VARIABLE, DESCRIPCION, CONTENIDO) values (?,?,?)";
	
	
	public static String getObtenerVarible(String esquema) {
		return obtenerVarible.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getEliminaVarible(String esquema) {
		return eliminaVarible.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getInsertVarible(String esquema) {
		return insertVarible.replaceAll("<<esquema>>", esquema);
	}
	
}
