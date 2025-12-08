package com.siarex247.layOut.Formatos;

public class FormatosQuerys {

	private static String detalle = " select CLAVE_REGISTRO, DESCRIPCION, SUBJECT, NOMBRE_ARCHIVO, TIPO_PROVEEDOR from FORMATOS where TIPO_FORMATO is null ";
	private static String consulta = " select CLAVE_REGISTRO, DESCRIPCION, SUBJECT, CUERPO_MENSAJE, NOMBRE_ARCHIVO, TIPO_PROVEEDOR from FORMATOS where CLAVE_REGISTRO = ?";
	private static String alta = " insert into FORMATOS (DESCRIPCION, SUBJECT, CUERPO_MENSAJE, NOMBRE_ARCHIVO, TIPO_PROVEEDOR, CLAVE_USUARIO) values (?,?,?,?,?,?)";
	private static String update = " update FORMATOS set DESCRIPCION = ?, SUBJECT = ?, CUERPO_MENSAJE = ?, TIPO_PROVEEDOR = ?, CLAVE_USUARIO = ? where CLAVE_REGISTRO = ?";
	private static String elimina = " delete from FORMATOS where CLAVE_REGISTRO = ?";
	private static String consultaInstruc = " select CLAVE_REGISTRO, DESCRIPCION, SUBJECT, CUERPO_MENSAJE, NOMBRE_ARCHIVO, TIPO_PROVEEDOR, COPIA_PARA from FORMATOS where TIPO_FORMATO = ?";
	private static String altaInstru = " insert into FORMATOS (DESCRIPCION, SUBJECT, CUERPO_MENSAJE, TIPO_PROVEEDOR, TIPO_FORMATO, COPIA_PARA, CLAVE_USUARIO) values (?,?,?,?,?,?,?)";
	private static String updateInstru = "update FORMATOS set DESCRIPCION = ?, SUBJECT = ?, CUERPO_MENSAJE = ?, COPIA_PARA = ?, CLAVE_USUARIO = ? where CLAVE_REGISTRO = ?";

	
	
	public static String getDetalle(String esquema) {
		return detalle.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getConsulta(String esquema) {
		return consulta.replaceAll("<<esquema>>", esquema);
	}
	public static String getAlta(String esquema) {
		return alta.replaceAll("<<esquema>>", esquema);
	}
	public static String getUpdate(String esquema) {
		return update.replaceAll("<<esquema>>", esquema);
	}
	public static String getElimina(String esquema) {
		return elimina.replaceAll("<<esquema>>", esquema);
	}
	public static String getConsultaInstruc(String esquema) {
		return consultaInstruc.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getAltaInstru(String esquema) {
		return altaInstru.replaceAll("<<esquema>>", esquema);
	}

	public static String getUpdateInstru(String esquema) {
		return updateInstru.replaceAll("<<esquema>>", esquema);
	}
}
