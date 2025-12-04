package com.siarex247.catalogos.ConstanciaSituacion;

public class ConstanciaSituacionQuerys {

	private static String detalle     =  "select ID_REGISTRO, RFC, RAZON_SOCIAL, CEDULA_FISCAL, REGIMEN_CAPITAL, NOMBRES_SAT, APATERNO_SAT,AMATERNO_SAT, FEC_NACIMIENTO_SAT, SITUACION_CONTRIBUYENTE, FECHA_ULTIMO_CAMBIO_SITUACION, CURP_SAT, ENTIDAD_FEDERATIVA, MUNICIPIO, COLONIA, TIPO_VIALIDAD, NOMBRE_VIALIDAD, NUMERO_EXTERIOR, NUMERO_INTERIOR, CODIGO_POSTAL_SAT, FEC_INICIO_OPERACIONES, EMAIL_SAT, CLAVE_REGIMEN, REGIMEN, FECHA_ALTA from CONSTANCIAS_SITUACION_FISCAL where CLAVE_EMPRESA = ? and ESTATUS = ?";
	private static String consulta	  =  "select ID_REGISTRO, RFC, RAZON_SOCIAL, CEDULA_FISCAL, REGIMEN_CAPITAL, NOMBRES_SAT, APATERNO_SAT,AMATERNO_SAT, FEC_NACIMIENTO_SAT, SITUACION_CONTRIBUYENTE, FECHA_ULTIMO_CAMBIO_SITUACION, CURP_SAT, ENTIDAD_FEDERATIVA, MUNICIPIO, COLONIA, TIPO_VIALIDAD, NOMBRE_VIALIDAD, NUMERO_EXTERIOR, NUMERO_INTERIOR, CODIGO_POSTAL_SAT, FEC_INICIO_OPERACIONES, EMAIL_SAT, CLAVE_REGIMEN, REGIMEN, FECHA_ALTA from CONSTANCIAS_SITUACION_FISCAL where ID_REGISTRO = ?";
	private static String consultaRFC =  "select ID_REGISTRO, RFC, RAZON_SOCIAL, CEDULA_FISCAL, REGIMEN_CAPITAL, NOMBRES_SAT, APATERNO_SAT,AMATERNO_SAT, FEC_NACIMIENTO_SAT, SITUACION_CONTRIBUYENTE, FECHA_ULTIMO_CAMBIO_SITUACION, CURP_SAT, ENTIDAD_FEDERATIVA, MUNICIPIO, COLONIA, TIPO_VIALIDAD, NOMBRE_VIALIDAD, NUMERO_EXTERIOR, NUMERO_INTERIOR, CODIGO_POSTAL_SAT, FEC_INICIO_OPERACIONES, EMAIL_SAT, CLAVE_REGIMEN, REGIMEN, FECHA_ALTA from CONSTANCIAS_SITUACION_FISCAL where CLAVE_EMPRESA = ? and RFC = ? and ESTATUS = ?";
	
	private static String consultaPDF =  "select RFC from CONSTANCIAS_SITUACION_FISCAL where ID_REGISTRO = ? and CEDULA_FISCAL = ? ";
	
	private static String elimina	  =  "update CONSTANCIAS_SITUACION_FISCAL set ESTATUS = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where ID_REGISTRO = ? ";
	
	private static String updateCedulaFiscal   =  "update <<esquema>>.CONSTANCIAS_SITUACION_FISCAL set RAZON_SOCIAL = ?, CEDULA_FISCAL = ?, REGIMEN_CAPITAL = ?, NOMBRES_SAT = ?, APATERNO_SAT = ?,AMATERNO_SAT = ?, FEC_NACIMIENTO_SAT = ?, SITUACION_CONTRIBUYENTE = ?, FECHA_ULTIMO_CAMBIO_SITUACION = ?, CURP_SAT = ?, ENTIDAD_FEDERATIVA = ?, MUNICIPIO = ?, COLONIA = ?, TIPO_VIALIDAD = ?, NOMBRE_VIALIDAD = ?, NUMERO_EXTERIOR = ?, NUMERO_INTERIOR = ?, CODIGO_POSTAL_SAT = ?, FEC_INICIO_OPERACIONES = ?, EMAIL_SAT = ?, CLAVE_REGIMEN = ?, REGIMEN = ?, FECHA_ALTA = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where CLAVE_EMPRESA = ? and RFC = ? and ESTATUS = ?";
	private static String insertarCedulaFiscal =  "insert into <<esquema>>.CONSTANCIAS_SITUACION_FISCAL (CLAVE_EMPRESA, RFC, RAZON_SOCIAL, CEDULA_FISCAL, REGIMEN_CAPITAL, NOMBRES_SAT, APATERNO_SAT,AMATERNO_SAT, FEC_NACIMIENTO_SAT, SITUACION_CONTRIBUYENTE, FECHA_ULTIMO_CAMBIO_SITUACION, CURP_SAT, ENTIDAD_FEDERATIVA, MUNICIPIO, COLONIA, TIPO_VIALIDAD, NOMBRE_VIALIDAD, NUMERO_EXTERIOR, NUMERO_INTERIOR, CODIGO_POSTAL_SAT, FEC_INICIO_OPERACIONES, EMAIL_SAT, CLAVE_REGIMEN, REGIMEN, FECHA_ALTA, USUARIO_TRAN) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static String consultaRegimen	  =  "select CLAVE_REGIMEN from CAT_REGIMEN_FISCAL_SAT where upper(DESCRIPCION_URL_SAL) = ? ";	

	
	public static String getDetalle(String esquema) {
		return detalle.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsulta(String esquema) {
		return consulta.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaRFC(String esquema) {
		return consultaRFC.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getUpdateCedulaFiscal(String esquema) {
		return updateCedulaFiscal.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getInsertarCedulaFiscal(String esquema) {
		return insertarCedulaFiscal.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaPDF(String esquema) {
		return consultaPDF.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getElimina(String esquema) {
		return elimina.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaRegimen(String esquema) {
		return consultaRegimen.replaceAll("<<esquema>>", esquema);
	}
	
	
}
