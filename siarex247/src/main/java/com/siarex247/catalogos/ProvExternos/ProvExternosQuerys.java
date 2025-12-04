package com.siarex247.catalogos.ProvExternos;

public class ProvExternosQuerys {

	
	private static String detalleProveedor = "select CLAVE_PROVEEDOR, RAZON_SOCIAL, RFC, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, DELEGACION, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO, NOMBRE_CONTACTO, EMAIL, NUMERO_PROVEEDOR, CLAVE_RECEPTOR from PROVEEDORES_EXTERNOS";
	private static String consultarProveedor = "select CLAVE_PROVEEDOR, RAZON_SOCIAL, RFC, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, DELEGACION, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO, NOMBRE_CONTACTO, EMAIL, NUMERO_PROVEEDOR, CLAVE_RECEPTOR from PROVEEDORES_EXTERNOS where CLAVE_PROVEEDOR = ?";
	private static String altaProveedor = "insert into PROVEEDORES_EXTERNOS (RAZON_SOCIAL, RFC, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, DELEGACION, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO, NOMBRE_CONTACTO, EMAIL, NUMERO_PROVEEDOR, CLAVE_RECEPTOR) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String actualizaProveedor = "update PROVEEDORES_EXTERNOS set RAZON_SOCIAL = ?, RFC = ?, CALLE = ?, NUMERO_INT = ?, NUMERO_EXT = ?, COLONIA = ?, DELEGACION = ?, CIUDAD = ?, ESTADO = ?, CODIGO_POSTAL = ?, TELEFONO = ?, NOMBRE_CONTACTO = ?, EMAIL = ?, NUMERO_PROVEEDOR = ?  where CLAVE_PROVEEDOR = ?";
	private static String eliminaProveedor = "delete from PROVEEDORES_EXTERNOS where CLAVE_PROVEEDOR = ?";
	
	
	
	
	
	public static String getDetalleProveedor(String esquema) {
		return detalleProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getConsultarProveedor(String esquema) {
		return consultarProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getAltaProveedor(String esquema) {
		return altaProveedor.replaceAll("<<esquema>>", esquema);
	}

	public static String getActualizaProveedor(String esquema) {
		return actualizaProveedor.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getEliminaProveedor(String esquema) {
		return eliminaProveedor.replaceAll("<<esquema>>", esquema);
	}
	
}
