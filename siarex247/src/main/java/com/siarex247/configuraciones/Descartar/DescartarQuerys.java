package com.siarex247.configuraciones.Descartar;

public class DescartarQuerys {

	
	private static String detalleDescartes =  "select P.RAZON_SOCIAL, D.FOLIO_EMPRESA, O.SERIE, O.ESTATUS_PAGO, O.TOTAL, O.SUB_TOTAL, O.CLAVE_PROVEEDOR from DESCARTES D left join ORDENES O on D.FOLIO_EMPRESA = O.FOLIO_EMPRESA left join PROVEEDORES P on O.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR";
	private static String deleteDescartes  =  "delete from DESCARTES where FOLIO_EMPRESA = ?";
	private static String eliminarTodo     =  "delete from DESCARTES";
	private static String insertDescartes  =  "insert into DESCARTES ( FOLIO_EMPRESA, USUARIO_TRANS) values (?,?)";
	
	
	
	public static String getDetalleDescartes(String esquema) {
		return detalleDescartes.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDeleteDescartes(String esquema) {
		return deleteDescartes.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getEliminarTodo(String esquema) {
		return eliminarTodo.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getInsertDescartes(String esquema) {
		return insertDescartes.replaceAll("<<esquema>>", esquema);
	}
	
}
