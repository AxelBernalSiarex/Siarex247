package com.siarex247.configuraciones.ConfClaveUsoCFDI;

public class ConfClaveUsoCFDIQuerys {

	
	private static String queryConsultaUso =  "select A.ID_REGISTRO, A.RFC, D.RAZON_SOCIAL, A.USO_CFDI, A.CLAVE_PRODUCTO, B.DESCRIPCION, C.DESCRIPCION, C.DIVISION, C.GRUPO, C.CLASE from USO_CFDI A left join CAT_USOCFDI B on  A.USO_CFDI = B. CLAVE_CFDI left join CAT_CLABE_PRODUCTO_SERVICIO C on A.CLAVE_PRODUCTO = C.CLAVE_PRODUCTO inner join PROVEEDORES D on A.RFC = D.RFC";
	private static String queryProveedoresMEX =  "select RFC, RAZON_SOCIAL, CLAVE_PROVEEDOR from PROVEEDORES where TIPO_PROVEEDOR = ?";
	private static String queryDesUsoCFDI  =   "select DESCRIPCION from CAT_USOCFDI where CLAVE_CFDI = ?";
	private static String queryDesClave    =   "select DESCRIPCION from CAT_CLABE_PRODUCTO_SERVICIO where CLAVE_PRODUCTO = ?";
	private static String queryInsertUso =  "insert into USO_CFDI (RFC, USO_CFDI, CLAVE_PRODUCTO, USUARIO) values (?,?,?,?)";
	private static String queryBuscarUso =  "select RFC, USO_CFDI, CLAVE_PRODUCTO from USO_CFDI where ID_REGISTRO = ? ";
	private static String queryUpdateUso =  "update USO_CFDI set RFC = ?, USO_CFDI = ?, CLAVE_PRODUCTO = ?, USUARIO = ?, FECHA = current_timestamp  where ID_REGISTRO = ?";
	private static String queryDeleteUsoBitacora =  "insert into USO_CFDI_BITACORA (ID_RASTREO, RFC, USO_CFDI, CLAVE_PRODUCTO, USUARIO,FECHA, USUARIO_TRANS, ACCION) select ID_REGISTRO, RFC, USO_CFDI, CLAVE_PRODUCTO, USUARIO, FECHA, ?, ? from USO_CFDI where ID_REGISTRO = ?";
	private static String queryDeleteUso =  "delete from USO_CFDI where ID_REGISTRO = ?";
	private static String eliminaVarible =  "delete from CONF_SISTEMA where VARIABLE = ?";
	private static String insertVarible =  "insert into CONF_SISTEMA (VARIABLE, DESCRIPCION, CONTENIDO) values (?,?,?)";
	private static String obtenerVarible =  "select VARIABLE, DESCRIPCION, CONTENIDO from CONF_SISTEMA where VARIABLE = ?";
	private static String queryDeleteUsoImp =  "delete from USO_CFDI";
	private static String queryBuscarRFC =  "select RAZON_SOCIAL from PROVEEDORES where RFC = ?";
	private static String queryDeleteClaveProductoXML_Multiple =  "delete from CLAVE_PRODUC_SERVICIO_XML_MULTIPLE where ID_MULTIPLE = ?";
	
	
	
	
	public static String getQueryConsultaUso(String esquema) {
		return queryConsultaUso.replaceAll("<<esquema>>", esquema);
	}
	public static String getQueryBuscarUso(String esquema) {
		return queryBuscarUso.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryDesUsoCFDI(String esquema) {
		return queryDesUsoCFDI.replaceAll("<<esquema>>", esquema);
	}

	public static String getQueryDesClave(String esquema) {
		return queryDesClave.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryInsertUso(String esquema, String qqq) {
		return queryInsertUso.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryUpdateUso(String esquema) {
		return queryUpdateUso.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryDeleteUsoBitacora(String esquema) {
		return queryDeleteUsoBitacora.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getQueryDeleteUso(String esquema) {
		return queryDeleteUso.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getQueryProveedoresMEX(String esquema) {
		return queryProveedoresMEX.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getEliminaVarible(String esquema) {
		return eliminaVarible.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getInsertVarible(String esquema) {
		return insertVarible.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getObtenerVarible(String esquema) {
		return obtenerVarible.replaceAll("<<esquema>>", esquema);
	}

	
	
	public static String getQueryDeleteUsoImp(String esquema) {
		return queryDeleteUsoImp.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryBuscarRFC(String esquema) {
		return queryBuscarRFC.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryDeleteClaveProductoXML_Multiple(String esquema) {
		return queryDeleteClaveProductoXML_Multiple.replaceAll("<<esquema>>", esquema);
	}
}
