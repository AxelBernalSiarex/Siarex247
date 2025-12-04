package com.siarex247.visor.VisorOrdenes;

public class VisorClavesCFDIQuerys {

	
	
	private static String queryIsMultiple   =   "select TIPO_ORDEN from ORDENES where FOLIO_EMPRESA = ?";
	private static String queryBuscarClavePorOrdenXML  =   "select ID_REGISTRO, USO_CFDI, CLAVE_PROD_SERV, IMPORTE, VALOR_UNITARIO, DESCRIPCION, UNIDAD, CLAVE_UNIDAD, CANTIDAD, NUM_IDENTIFICACION, ESTATUS from CLAVE_PRODUC_SERVICIO_XML where FOLIO_EMPRESA = ?";
	private static String queryBuscarProveedor =  "select B.RFC, B.RAZON_SOCIAL, A.NOMBRE_XML, A.NOMBRE_PDF, B.CLAVE_PROVEEDOR, A.SERVICIO_PRODUCTO from ORDENES A inner join PROVEEDORES B ON A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR where A.FOLIO_EMPRESA = ?";
	private static String queryUsoCFDIXML  =   "select USO_CFDI from USO_CFDI where RFC = ? and USO_CFDI = ?";
	private static String queryDesUsoCFDI  =   "select DESCRIPCION from CAT_USOCFDI where CLAVE_CFDI = ?";
	private static String queryBuscarClavePorOrdenXML_Multiple  =   "select ID_REGISTRO, USO_CFDI, CLAVE_PROD_SERV, IMPORTE, VALOR_UNITARIO, DESCRIPCION, UNIDAD, CLAVE_UNIDAD, CANTIDAD, NUM_IDENTIFICACION, ESTATUS from CLAVE_PRODUC_SERVICIO_XML_MULTIPLE where ID_MULTIPLE = ?";
	private static String queryBuscarClavePorOrden   =   "select A.ID_REGISTRO, A.CLAVE_PROD_SERV, A.DESCRIPCION as DESCRIPCION_XML, B.DESCRIPCION as DESCRIPCION_SAT  from CLAVE_PRODUC_SERVICIO_XML A left join CAT_CLABE_PRODUCTO_SERVICIO B on A.CLAVE_PROD_SERV = B.CLAVE_PRODUCTO where FOLIO_EMPRESA = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI where RFC = ? and USO_CFDI = ?)";
	private static String queryBuscarClavePorOrdenSinUSO   =   "select A.ID_REGISTRO, A.CLAVE_PROD_SERV, A.DESCRIPCION as DESCRIPCION_XML, B.DESCRIPCION as DESCRIPCION_SAT  from CLAVE_PRODUC_SERVICIO_XML A left join CAT_CLABE_PRODUCTO_SERVICIO B on A.CLAVE_PROD_SERV = B.CLAVE_PRODUCTO where FOLIO_EMPRESA = ?";
	private static String queryBuscarClavePorOrden_Multiple   =   "select A.ID_REGISTRO, A.CLAVE_PROD_SERV, A.DESCRIPCION as DESCRIPCION_XML, B.DESCRIPCION as DESCRIPCION_SAT  from CLAVE_PRODUC_SERVICIO_XML_MULTIPLE A left join CAT_CLABE_PRODUCTO_SERVICIO B on A.CLAVE_PROD_SERV = B.CLAVE_PRODUCTO where ID_MULTIPLE = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI where RFC = ? and USO_CFDI = ?) order by A.CLAVE_PROD_SERV";

	
	private static String queryValidaCLAVE   =   "select CLAVE_PROD_SERV from CLAVE_PRODUC_SERVICIO_XML where FOLIO_EMPRESA = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI where RFC = ? and USO_CFDI = ?) group by CLAVE_PROD_SERV";	
	private static String queryInsertUso =  "insert into USO_CFDI (RFC, USO_CFDI, CLAVE_PRODUCTO, USUARIO) values (?,?,?,?)";
	private static String queryDeleteClaveProductoXML =  "delete from CLAVE_PRODUC_SERVICIO_XML where FOLIO_EMPRESA = ?";
	private static String querySelectOrdenes_Multiple =  "select FOLIO_EMPRESA from ORDENES where TIPO_ORDEN = ?";
	private static String queryValidaCLAVE_Multiple   =   "select CLAVE_PROD_SERV from CLAVE_PRODUC_SERVICIO_XML_MULTIPLE where ID_MULTIPLE = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI where RFC = ? and USO_CFDI = ?) group by CLAVE_PROD_SERV";	
	
	
	public static String getQueryIsMultiple(String esquema) {
		return queryIsMultiple.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryBuscarClavePorOrdenXML(String esquema) {
		return queryBuscarClavePorOrdenXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryBuscarProveedor(String esquema) {
		return queryBuscarProveedor.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryUsoCFDIXML(String esquema) {
		return queryUsoCFDIXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryDesUsoCFDI(String esquema) {
		return queryDesUsoCFDI.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryBuscarClavePorOrdenXML_Multiple(String esquema) {
		return queryBuscarClavePorOrdenXML_Multiple.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryBuscarClavePorOrden(String esquema) {
		return queryBuscarClavePorOrden.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getQueryBuscarClavePorOrdenSinUSO(String esquema) {
		return queryBuscarClavePorOrdenSinUSO.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryBuscarClavePorOrden_Multiple(String esquema) {
		return queryBuscarClavePorOrden_Multiple.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryValidaCLAVE(String esquema) {
		return queryValidaCLAVE.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryInsertUso(String esquema, String qqq) {
		return queryInsertUso.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryDeleteClaveProductoXML(String esquema, String aaa) {
		return queryDeleteClaveProductoXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQuerySelectOrdenes_Multiple(String esquema) {
		return querySelectOrdenes_Multiple.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryValidaCLAVE_Multiple(String esquema) {
		return queryValidaCLAVE_Multiple.replaceAll("<<esquema>>", esquema);
	}
}
