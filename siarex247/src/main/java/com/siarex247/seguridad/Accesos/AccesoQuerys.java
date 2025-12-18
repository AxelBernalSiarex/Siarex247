package com.siarex247.seguridad.Accesos;

public class AccesoQuerys {

	private static String consultaAcceso = "select 'sdsdskljk4645asdasda' from ACCESOS where CLAVE_USUARIO = ? and ESTATUS = ? limit 1;";
	private static String altaAcceso = "insert into ACCESOS (CLAVE_EMPRESA, CLAVE_USUARIO, PWD_USUARIO, APLICACION, PERFIL_USUARIO, NOMBRE_COMPLETO, CLAVE_PROVEEDOR, EMAIL, FECHA_GENERACION, NUMERO_EMPLEADO, RESET_PASSWORD, CODE_ACCESO, NVA_VERSION, USUARIO_TRANS) values (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ? , ?, ?, ?, ? )";
	private static String actualizaAcceso = "update ACCESOS set PERFIL_USUARIO = ?, NOMBRE_COMPLETO = ?, EMAIL = ?, NUMERO_EMPLEADO = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where CLAVE_EMPRESA = ? and CLAVE_USUARIO = ? and APLICACION = ?";
	
	private static String eliminaAcceso = "delete from ACCESOS where CLAVE_EMPRESA = ? and CLAVE_USUARIO = ? and APLICACION = ?";
	private static String eliminaAccesoUserTomcat = "delete from USUARIOS where USER_NAME = ?";
	private static String eliminaAccesoRolesTomcat = "delete from USUARIOS_ROLES where USER_NAME = ?";

	private static String listaEmpresas = "select CLAVE_EMPRESA, NOMBRE_CORTO, NOMBRE_LARGO, RFC, ESTATUS, ESQUEMA, NOMBRE_CONTACTO, EMAIL_DOMINIO, EMAIL_CONTACTO, PWD_CORREO, PASSWORD_SAT, ARVHIVO_CER, ARVHIVO_KEY, TIPO_EMPRESA from EMPRESAS where CLAVE_EMPRESA in (select CLAVE_EMPRESA from EMPRESAS_APLICACIONES where APLICACION = ?) and ESTATUS = ?";
	private static String consultaEmpresa = "select CLAVE_EMPRESA, NOMBRE_CORTO, NOMBRE_LARGO, RFC, ESTATUS, ESQUEMA, NOMBRE_CONTACTO, EMAIL_DOMINIO, EMAIL_CONTACTO, PWD_CORREO, MOSTRAR_EXTRAS from EMPRESAS where CLAVE_EMPRESA = ?";
	private static String consultaEmpresaEsquema = "select CLAVE_EMPRESA, NOMBRE_CORTO, NOMBRE_LARGO, RFC, ESTATUS, ESQUEMA, NOMBRE_CONTACTO, EMAIL_DOMINIO, EMAIL_CONTACTO, PWD_CORREO, MOSTRAR_EXTRAS, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO1, LOGO, TIPO_EMPRESA, REQUISICION, TIPO_ACCESO, PASSWORD_SAT, ARVHIVO_CER, ARVHIVO_KEY, CLAVE_REGIMEN_FISCAL from EMPRESAS where ESQUEMA = ?";
	private static String consultaEmpresaRFC = "select CLAVE_EMPRESA, NOMBRE_CORTO, NOMBRE_LARGO, RFC, ESTATUS, ESQUEMA, NOMBRE_CONTACTO, EMAIL_DOMINIO, EMAIL_CONTACTO, PWD_CORREO, MOSTRAR_EXTRAS, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO1, LOGO, TIPO_EMPRESA, REQUISICION, TIPO_ACCESO, PASSWORD_SAT, ARVHIVO_CER, ARVHIVO_KEY, CLAVE_REGIMEN_FISCAL from EMPRESAS where RFC = ?";
	
	private static String queryRevisaMon =  "select APAGADO from PROCESO where CLAVE = ?";
	
	
	
	public static String getConsultaAcceso(String esquema) {
		return consultaAcceso.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEliminaAccesoUserTomcat(String esquema) {
		return eliminaAccesoUserTomcat.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEliminaAccesoRolesTomcat(String esquema) {
		return eliminaAccesoRolesTomcat.replaceAll("<<esquema>>", esquema);
	}

	public static String getAltaAcceso(String esquema) {
		return altaAcceso.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizaAcceso(String esquema) {
		return actualizaAcceso.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEliminaAcceso(String esquema) {
		return eliminaAcceso.replaceAll("<<esquema>>", esquema);
	}

	public static String getListaEmpresas(String esquema) {
		return listaEmpresas.replaceAll("<<esquema>>", esquema);
	}

	public static String getQueryRevisaMon(String esquema) {
		return queryRevisaMon.replaceAll("<<esquema>>", esquema);
	}

	public static String getConsultaEmpresa(String esquema) {
		return consultaEmpresa.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaEmpresaEsquema(String esquema) {
		return consultaEmpresaEsquema.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaEmpresaRFC(String esquema) {
		return consultaEmpresaRFC.replaceAll("<<esquema>>", esquema);
	}
	
	
	
}
