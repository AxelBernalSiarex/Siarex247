package com.siarex247.configuraciones.ConfClaveUsoCP;

public class ConfClaveUsoCPQuerys {

	private static String queryConsultaUsoCP      = "select A.ID_REGISTRO, A.RFC, D.RAZON_SOCIAL, A.USO_CFDI, A.CLAVE_PRODUCTO, B.DESCRIPCION, C.DESCRIPCION, C.DIVISION, C.GRUPO, C.CLASE from USO_CFDI_CARTA_PORTE A left join CAT_USOCFDI B on  A.USO_CFDI = B. CLAVE_CFDI left join CAT_CLABE_PRODUCTO_SERVICIO C on A.CLAVE_PRODUCTO = C.CLAVE_PRODUCTO inner join PROVEEDORES D on A.RFC = D.RFC";
	private static String queryDesUsoCFDICP       = "select DESCRIPCION from CAT_USOCFDI where CLAVE_CFDI = ?";
	private static String queryDesClaveCP         = "select DESCRIPCION from CAT_CLABE_PRODUCTO_SERVICIO where CLAVE_PRODUCTO = ?";
	private static String queryInsertUsoCP        = "insert into USO_CFDI_CARTA_PORTE (RFC, USO_CFDI, CLAVE_PRODUCTO, USUARIO) values (?,?,?,?)";
	private static String queryUpdateUsoCP 		  = "update USO_CFDI_CARTA_PORTE set RFC = ?, USO_CFDI = ?, CLAVE_PRODUCTO = ?, USUARIO = ?, FECHA = current_timestamp  where ID_REGISTRO = ?";
	private static String queryBuscarUsoCP        = "select RFC, USO_CFDI, CLAVE_PRODUCTO from USO_CFDI_CARTA_PORTE where ID_REGISTRO = ? ";
	private static String queryDelUsoBitacoraCP   = "insert into USO_CFDI_CP_BITACORA (ID_RASTREO, RFC, USO_CFDI, CLAVE_PRODUCTO, USUARIO,FECHA, USUARIO_TRANS, ACCION) select ID_REGISTRO, RFC, USO_CFDI, CLAVE_PRODUCTO, USUARIO, FECHA, ?, ? from USO_CFDI_CARTA_PORTE where ID_REGISTRO = ?";
	private static String queryDeleteUsoCP        = "delete from USO_CFDI_CARTA_PORTE where ID_REGISTRO = ?";
	private static String queryDelUsoImpCP        = "delete from USO_CFDI_CARTA_PORTE";
	private static String queryBuscarRFC          = "select RAZON_SOCIAL from PROVEEDORES where RFC = ?";
	
	
	public static String getQueryConsultaUsoCP(String esquema) {
		return queryConsultaUsoCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryDesUsoCFDICP(String esquema) {
		return queryDesUsoCFDICP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryDesClaveCP(String esquema) {
		return queryDesClaveCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryInsertUsoCP(String esquema) {
		return queryInsertUsoCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryUpdateUsoCP(String esquema) {
		return queryUpdateUsoCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryBuscarUsoCP(String esquema) {
		return queryBuscarUsoCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryDelUsoBitacoraCP(String esquema) {
		return queryDelUsoBitacoraCP.replaceAll("<<esquema>>", esquema);
	}

	public static String getQueryDeleteUsoCP(String esquema) {
		return queryDeleteUsoCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryDelUsoImpCP(String esquema) {
		return queryDelUsoImpCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryBuscarRFC(String esquema) {
		return queryBuscarRFC.replaceAll("<<esquema>>", esquema);
	}
}
