package com.siarex247.procesos;

public class ProcesosMonitorQuerys {

	
	private static String queryRevisaMon =  "select APAGADO from PROCESO_SIAREX247 where CLAVE = ?";
	
	private static String queryApagaMon =  "update PROCESO_SIAREX247 set APAGADO = ? where CLAVE = ?";
	
	private static String queryBanderasMon =  "select CLAVE, APAGADO from PROCESO_SIAREX247";
	
	
	
	public static String getQueryRevisaMon(String esquema) {
		return queryRevisaMon.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryBanderasMon(String esquema) {
		return queryBanderasMon.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryApagaMon(String esquema) {
		return queryApagaMon.replaceAll("<<esquema>>", esquema);
	}
	
}
