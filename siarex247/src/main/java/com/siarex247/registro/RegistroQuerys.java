package com.siarex247.registro;

public class RegistroQuerys {

	private static String tienePermiso = "select CLAVE_REGISTRO, RESET_PASSWORD from USUARIOS where CLAVE_EMPRESA = ? and CLAVE_USUARIO = ? and PWD_USUARIO = ?";
	private static String tienePermisoAplicacion = "select CLAVE_REGISTRO, RESET_PASSWORD, NVA_VERSION from ACCESOS where CLAVE_EMPRESA = ? and CLAVE_USUARIO = ? and APLICACION = ?";
	
	private static String datosEmpresa = "select NOMBRE_LARGO, ESQUEMA, EMAIL_DOMINIO, PWD_CORREO, RFC, CALLE, CALLE2, NUMERO_INT, NUMERO_EXT, COLONIA, CIUDAD, ESTADO, CODIGO_POSTAL, NOMBRE_CONTACTO, EMAIL_CONTACTO, TELEFONO1, EXT_1, TELEFONO2, EXT_2, LOGO, TIPO_EMPRESA from EMPRESAS where CLAVE_EMPRESA = ?";
	
	private static String updatePassword = "update USUARIOS set PWD_USUARIO = ?, RESET_PASSWORD = ? where CLAVE_EMPRESA = ? and CLAVE_USUARIO = ?  ";
	private static String recuperaUsuario = "select CLAVE_EMPRESA, CLAVE_USUARIO, PWD_USUARIO from USUARIOS where upper(EMAIL) = upper(?)";
	
	private static String perfil = "select ID_PERFIL from USUARIOS where ID_USR = ?";
	
	private static String altaAcceso = "insert into USUARIOS (CLAVE_EMPRESA, CLAVE_USUARIO, PWD_USUARIO, EMAIL,CODE_ACCESO) values (?,?,?,?,?)";

	private static String validarUsuarioDescarga = "select CLAVE_REGISTRO, RESET_PASSWORD, NVA_VERSION from ACCESOS where CLAVE_USUARIO =  ? and APLICACION = ? and ESTATUS = ? and RESET_PASSWORD = ? limit 1";
	
	
	public static String getTienePermiso(String esquema) {
		return tienePermiso.replaceAll("<<esquema>>", esquema);
	}

	public static String getTienePermisoAplicacion(String esquema) {
		return tienePermisoAplicacion.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getDatosEmpresa(String esquema) {
		return datosEmpresa.replaceAll("<<esquema>>", esquema);
	}

	public static String getUpdatePassword(String esquema) {
		return updatePassword.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryRecuperaUsuario(String esquema) {
		return recuperaUsuario.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getPerfil(String esquema) {
		return perfil.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getAltaAcceso(String esquema) {
		return altaAcceso.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getValidarUsuarioDescarga(String esquema) {
		return validarUsuarioDescarga.replaceAll("<<esquema>>", esquema);
	}
	
}