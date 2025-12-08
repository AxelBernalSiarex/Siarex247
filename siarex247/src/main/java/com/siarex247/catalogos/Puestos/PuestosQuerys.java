package com.siarex247.catalogos.Puestos;

public class PuestosQuerys {

	private static String comboPuestos   = "select ID_PUESTO, DESCRIPCION from PUESTOS ";	
	private static String detallePuestos = "select ID_PUESTO, NOMBRE_CORTO, DESCRIPCION from PUESTOS";
	private static String consultaPuesto = "select ID_PUESTO, NOMBRE_CORTO, DESCRIPCION from PUESTOS where ID_PUESTO = ?";
	private static String guardarPuesto  = "insert into PUESTOS (NOMBRE_CORTO, DESCRIPCION ) values (?,?)";
	private static String updatePuesto   = "update PUESTOS set NOMBRE_CORTO = ?, DESCRIPCION = ? where ID_PUESTO = ?";
	private static String eliminaPuesto   = "delete from PUESTOS where ID_PUESTO = ?";
	
	
	public static String getComboPuestos(String esquema) {
		return comboPuestos.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getDetallePuestos(String esquema) {
		return detallePuestos.replaceAll("<<esquema>>", esquema);
	}

	public static String getConsultaPuesto(String esquema) {
		return consultaPuesto.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getGuardarPuesto(String esquema) {
		return guardarPuesto.replaceAll("<<esquema>>", esquema);
	}

	public static String getUpdatePuesto(String esquema) {
		return updatePuesto.replaceAll("<<esquema>>", esquema);
	}

	public static String getEliminaPuesto(String esquema) {
		return eliminaPuesto.replaceAll("<<esquema>>", esquema);
	}
	
}
