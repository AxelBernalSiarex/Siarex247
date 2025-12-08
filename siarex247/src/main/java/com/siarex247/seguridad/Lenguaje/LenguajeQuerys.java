package com.siarex247.seguridad.Lenguaje;

public class LenguajeQuerys {

	private static String queryEtiquetasLenguaje =  "select CAMPO, VALOR_MX, VALOR_US from <<esquema>>.LENGUAJE where APLICACION = ? and ID_PANTALLA = ?";
	
	
	public static String getQueryEtiquetasLenguaje(String esquema) {
		return queryEtiquetasLenguaje.replaceAll("<<esquema>>", esquema);
	}
	
}
