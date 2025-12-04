package com.siarex247.visor.Americanos;

public class GeneraDatosFacturaQuerys {

	
	
	private static String obtenerConsecutivo = "select max(ID_FOLIO) from CONSECUTIVO_PROVEEDORES where CLAVE_PROVEEDOR = ?";
	private static String guardaConsecutivo = "insert into CONSECUTIVO_PROVEEDORES (ID_FOLIO, CLAVE_PROVEEDOR, FOLIO_ORDEN, ESTATUS, USUARIO) values (?,?,?,?,?)";
	
	
	private static String datosEmpresaEmiror = "select NOMBRE_LARGO, CALLE, CALLE2, CIUDAD, ESTADO, CODIGO_POSTAL, RFC from EMPRESAS where ESQUEMA = ?";
	private static String datosOrdenes = "select CONCEPTO, MONTO from ORDENES where FOLIO_EMPRESA = ?";
	
	
	
	
	public static String getObtenerConsecutivo(String esquema) {
		return obtenerConsecutivo.replaceAll("<<esquema>>", esquema);
	}

	public static String getGuardaConsecutivo(String esquema) {
		return guardaConsecutivo.replaceAll("<<esquema>>", esquema);
	}

	public static String getDatosEmpresaEmiror(String esquema) {
		return datosEmpresaEmiror.replaceAll("<<esquema>>", esquema);
	}

	public static String getDatosOrdenes(String esquema) {
		return datosOrdenes.replaceAll("<<esquema>>", esquema);
	}
	
}
