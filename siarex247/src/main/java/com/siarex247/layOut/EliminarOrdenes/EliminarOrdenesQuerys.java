package com.siarex247.layOut.EliminarOrdenes;

public class EliminarOrdenesQuerys {

	private static String eliminarOrdenes = "delete from ORDENES where FOLIO_EMPRESA = ? and CLAVE_PROVEEDOR = ?";
	private static String infoProveedorRFC = "select RFC, EMAIL, TIPO_CONFIRMACION, RAZON_SOCIAL, CLAVE_PROVEEDOR from PROVEEDORES where upper(RFC) = upper(?)";
	
	
	public static String getEliminarOrdenes(String esquema) {
		return eliminarOrdenes.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInfoProveedorRFC(String esquema) {
		return infoProveedorRFC.replaceAll("<<esquema>>", esquema);
	}
	
}
