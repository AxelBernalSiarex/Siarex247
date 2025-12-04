package com.siarex247.catalogos.CentroCostos;

public class CentroCostosQuerys {

	private static String detalleCentros   =  "select ID_REGISTRO, NOMBRE_CORTO, DEPARTAMENTO, CORREO, USUARIO_TRAN, FECHA_TRANS from CENTRO_COSTOS ";
	private static String altaCentros      =  "insert into CENTRO_COSTOS (NOMBRE_CORTO,  DEPARTAMENTO, CORREO, USUARIO_TRAN) values (?,?,?,?) ";
	private static String eliminaCentros  =  "delete from CENTRO_COSTOS where ID_REGISTRO = ? ";
	private static String modificaCentros  =  "update CENTRO_COSTOS set NOMBRE_CORTO = ?, DEPARTAMENTO = ?, CORREO = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where ID_REGISTRO = ? ";
	private static String buscarCentros   =  "select ID_REGISTRO, NOMBRE_CORTO, DEPARTAMENTO, CORREO, USUARIO_TRAN, FECHA_TRANS from CENTRO_COSTOS where ID_REGISTRO = ?";
	
	private static String comboCentros   =  "select NOMBRE_CORTO, DEPARTAMENTO from CENTRO_COSTOS ";
	
	

	public static String getDetalleCentros(String esquema) {
		return detalleCentros.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getAltaCentros(String esquema) {
		return altaCentros.replaceAll("<<esquema>>", esquema);
	}

	public static String getModificaCentros(String esquema) {
		return modificaCentros.replaceAll("<<esquema>>", esquema);
	}

	public static String getEliminaCentros(String esquema) {
		return eliminaCentros.replaceAll("<<esquema>>", esquema);
	}

	public static String getBuscarCentros(String esquema) {
		return buscarCentros.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getComboCentros(String esquema) {
		return comboCentros.replaceAll("<<esquema>>", esquema);
	}
	
}
