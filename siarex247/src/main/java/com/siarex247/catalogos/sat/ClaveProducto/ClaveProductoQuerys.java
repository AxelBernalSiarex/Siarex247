package com.siarex247.catalogos.sat.ClaveProducto;

public class ClaveProductoQuerys {

	private static String detalle  = "select CLAVE_PRODUCTO, DESCRIPCION FROM CAT_CLABE_PRODUCTO_SERVICIO";
	
	
	public static String getDetalle(String esquema) {
		return detalle.replaceAll("<<esquema>>", esquema);
	}
	

	
}
