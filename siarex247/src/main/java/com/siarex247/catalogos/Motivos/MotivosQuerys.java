package com.siarex247.catalogos.Motivos;

public class MotivosQuerys {

	private static String detalleMotivos     = "select ID_REGISTRO, DESCRIPCION from MOTIVOS where TIPO_MOTIVO = ? order by DESCRIPCION asc";
	
	private static String obtenerMotivo     = "select NOMBRE_CORTO, DESCRIPCION, SUBJECT, MENSAJE from MOTIVOS where ID_REGISTRO = ?";
	

	public static String getQueryDetalleMotivos(String esquema) {
		return detalleMotivos.replaceAll("<<esquema>>", esquema).toUpperCase();
	}

	
	public static String getQueryoOtenerMotivo(String esquema) {
		return obtenerMotivo.replaceAll("<<esquema>>", esquema).toUpperCase();
	}

	
	
}
