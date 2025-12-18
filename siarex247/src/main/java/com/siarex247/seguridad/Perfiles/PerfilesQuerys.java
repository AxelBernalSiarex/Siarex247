package com.siarex247.seguridad.Perfiles;

public class PerfilesQuerys {
	private static String detallePerfiles = "select CLAVEPERFIL, NOMBRE_CORTO, DESCRIPCION, MOSTRAR_PROYECTOS, TIPO_PERFIL  from PERFILES where ESTATUS_REGISTRO = ? ";
	private static String detallePerfilesNomina = "select CLAVEPERFIL, NOMBRE_CORTO, DESCRIPCION, MOSTRAR_PROYECTOS, TIPO_PERFIL  from PERFILES where ESTATUS_REGISTRO = ? and NOMBRE_CORTO in (?)";

	private static String comboEmpleados  = "select CLAVEPERFIL, NOMBRE_CORTO, DESCRIPCION, MOSTRAR_PROYECTOS, TIPO_PERFIL  from PERFILES where ESTATUS_REGISTRO = ? and NOMBRE_CORTO in (?,?,?,?);";
	private static String detallePerfil   = "select CLAVEPERFIL, NOMBRE_CORTO, DESCRIPCION, MOSTRAR_PROYECTOS, TIPO_PERFIL  from PERFILES where CLAVEPERFIL = ?";
	private static String insertPerfiles  = "insert into PERFILES (NOMBRE_CORTO, DESCRIPCION, MOSTRAR_PROYECTOS, TIPO_PERFIL, ESTATUS_REGISTRO, USRGENERA) values (?,?,?,?,?,?)";
	private static String updatePerfiles  = "update PERFILES set DESCRIPCION = ?, MOSTRAR_PROYECTOS = ?, TIPO_PERFIL = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where CLAVEPERFIL = ?";
	private static String eliminaPerfiles  = "update PERFILES set ESTATUS_REGISTRO = ?, USUARIO_CAMBIO = ? where CLAVEPERFIL = ?";
	
	public static String getDetallePerfiles(String esquema) {
		return detallePerfiles.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetallePerfilesNomina(String esquema) {
		return detallePerfilesNomina.replaceAll("<<esquema>>", esquema);
	}

	public static String getDetallePerfil(String esquema) {
		return detallePerfil.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getInsertPerfiles(String esquema) {
		return insertPerfiles.replaceAll("<<esquema>>", esquema);
	}

	public static String getUpdatePerfiles(String esquema) {
		return updatePerfiles.replaceAll("<<esquema>>", esquema);
	}

	public static String getEliminaPerfiles(String esquema) {
		return eliminaPerfiles.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getComboEmpleados(String esquema) {
		return comboEmpleados.replaceAll("<<esquema>>", esquema);
	}
}
