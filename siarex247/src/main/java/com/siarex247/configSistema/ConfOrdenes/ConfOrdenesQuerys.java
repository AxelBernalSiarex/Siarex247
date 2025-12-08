package com.siarex247.configSistema.ConfOrdenes;

public class ConfOrdenesQuerys {

	private static String obtenerConfOrdenes = "select ETIQUETA, VALOR from CONFIGURACION_SIAREX";
	private static String actualizaConfOrdenes = "update CONFIGURACION_SIAREX set VALOR = ? where ETIQUETA = ?";
	
	
	public static String getQueryObtenerConfOrdenes(String esquema) {
		return obtenerConfOrdenes.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryActualizaConfOrdenes(String esquema) {
		return actualizaConfOrdenes.replaceAll("<<esquema>>", esquema);
	}
	
}
