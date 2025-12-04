package com.siarex247.validaciones;

public class ValidacionesCPQuerys {

	
	// private static String listadoValores         = "select B.DISPONIBLE1 from ETIQUETAS_CATALOGO A inner join CATALOGOS_CARTA_PORTE B on A.ID_VALOR = B.ID_REGISTRO where A.ID_CATALOGO = ?";
	private static String listadoValores         = "select DATO_VALIDA from ETIQUETAS_CATALOGO where ETIQUETA = ?";
	private static String insertarReporte         = "insert into ESTADISTICAS_CARTA_PORTE (FOLIO_EMPRESA, CLAVE_PROVEEDOR, ETIQUETA, VALOR_XML, ESTATUS, RESULTADO, USUARIO_TRASACCION) values (?,?,?,?,?,?,?)";
	private static String rfcEmisorReceptor      = "select CLAVE_RECEPTOR from PROVEEDORES_EXTERNOS where RFC = ?";
	private static String queryDeleteClaveProductoXML =  "delete from CLAVEPRODUCTO_CARTAPORTE where FOLIO_EMPRESA = ?";
	private static String queryInsertClaveProductoXML =  "insert into CLAVEPRODUCTO_CARTAPORTE (FOLIO_EMPRESA, USO_CFDI, CLAVE_PROD_SERV, DESCRIPCION, UNIDAD, CLAVE_UNIDAD, CANTIDAD, FRANCCION_ARANCELARIA, PESO_KG) values (?,?,?,?,?,?,?,?,?)";
	private static String queryValidaCLAVE   =   "select CLAVE_PROD_SERV from CLAVEPRODUCTO_CARTAPORTE where FOLIO_EMPRESA = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI_CARTA_PORTE where RFC = ? and USO_CFDI = ?) group by CLAVE_PROD_SERV";
	private static String grabarCartaPorte        =  "insert into CARTA_PORTE (FOLIO_ORDEN, FOLIO_EMPRESA, RFC_EMISOR, RFC_RECEPTOR, UUID, TOTAL_PAGADO, ESTATUS_CARTA_PORTE, FECHA_PAGO, FECHA_TIMBRADO, NOMBRE_XML, NOMBRE_PDF, TIPO_COMPROBANTE, FORMA_PAGO, ESTATUS_CONCILIACION, ESTATUS,ESTATUS_ORDEN, USUARIO_TRASACCION) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String tieneCartaPorte        = "select ID_REGISTRO from CARTA_PORTE where FOLIO_EMPRESA = ?";
	private static String getRFCProveedor        = "select RFC from PROVEEDORES where CLAVE_PROVEEDOR = ?";
	private static String infoCuentasPagarRecibo = "select NOMBRE_XML, ESTATUS_PAGO, MONTO from ORDENES where FOLIO_EMPRESA = ?";
	private static String validaEstatus   = "select ESTATUS_PAGO, MONTO, FOLIO_ORDEN, SUB_TOTAL, TOTAL, TIPO_MONEDA, SERIE, USO_CFDI, CLAVE_PRODUCTO_SERVICIO, UUID, FECHAPAGO from ORDENES where FOLIO_EMPRESA = ?";
	private static String actualizarOrdenCompra   =  "update ORDENES set ESTATUS_PAGO = ?, USUARIO = ?, FECHAULTMOV=current_timestamp where FOLIO_EMPRESA = ?";
	private static String cveOrdenXML             = "select ID_REGISTRO, USO_CFDI, CLAVE_PROD_SERV, DESCRIPCION, UNIDAD, CLAVE_UNIDAD, CANTIDAD, ESTATUS from CLAVEPRODUCTO_CARTAPORTE where FOLIO_EMPRESA = ?";
	private static String queryDesUsoCFDICP       = "select DESCRIPCION from CAT_USOCFDI where CLAVE_CFDI = ?";
	private static String buscarProveedor         = "select B.RFC, B.RAZON_SOCIAL, A.NOMBRE_XML, A.NOMBRE_PDF, B.CLAVE_PROVEEDOR, A.SERVICIO_PRODUCTO from ORDENES A inner join PROVEEDORES B ON A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR where A.FOLIO_EMPRESA = ?";
	private static String usoCFDIXML              = "select USO_CFDI from USO_CFDI_CARTA_PORTE where RFC = ? and USO_CFDI = ?";
	private static String clavePorOrden           = "select A.ID_REGISTRO, A.CLAVE_PROD_SERV, A.DESCRIPCION as DESCRIPCION_XML, B.DESCRIPCION as DESCRIPCION_SAT from CLAVEPRODUCTO_CARTAPORTE A left join CAT_CLABE_PRODUCTO_SERVICIO B on A.CLAVE_PROD_SERV = B.CLAVE_PRODUCTO where FOLIO_EMPRESA = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI_CARTA_PORTE where RFC = ? and USO_CFDI = ?)";
	private static String clavePorOrdenSinUSO     = "select A.ID_REGISTRO, A.CLAVE_PROD_SERV, A.DESCRIPCION as DESCRIPCION_XML, B.DESCRIPCION as DESCRIPCION_SAT  from CLAVEPRODUCTO_CARTAPORTE A left join CAT_CLABE_PRODUCTO_SERVICIO B on A.CLAVE_PROD_SERV = B.CLAVE_PRODUCTO where FOLIO_EMPRESA = ?";
	private static String consultaCarta           =  "select C.RFC, C.RAZON_SOCIAL, A.UUID, A.ESTATUS_CARTA_PORTE, A.NOMBRE_XML, A.NOMBRE_PDF, C.CLAVE_PROVEEDOR, A.TIPO_COMPROBANTE, A.ESTATUS_ORDEN from CARTA_PORTE A inner join ORDENES B on A.FOLIO_ORDEN = B.FOLIO_ORDEN inner join PROVEEDORES C on B.CLAVE_PROVEEDOR = C.CLAVE_PROVEEDOR where A.FOLIO_EMPRESA = ?";
	private static String insertUso               = "insert into USO_CFDI_CARTA_PORTE (RFC, USO_CFDI, CLAVE_PRODUCTO, USUARIO) values (?,?,?,?)";
	private static String deleteCartaPorteOrdenEmpresa	  =  "delete from CARTA_PORTE where FOLIO_EMPRESA = ?";
	
	
	
	public static String getListadoValores(String esquema) {
		return listadoValores.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getInsertarReporte(String esquema) {
		return insertarReporte.replaceAll("<<esquema>>", esquema);
	}

	public static String getRfcEmisorReceptor(String esquema) {
		return rfcEmisorReceptor.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryDeleteClaveProductoXML(String esquema) {
		return queryDeleteClaveProductoXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInsertClaveProductoXML(String esquema) {
		return queryInsertClaveProductoXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryValidaCLAVE(String esquema) {
		return queryValidaCLAVE.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getGrabarCartaPorte(String esquema) {
		return grabarCartaPorte.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTieneCartaPorte(String esquema) {
		return tieneCartaPorte.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getRfcProveedor(String esquema) {
		return getRFCProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInfoCuentasPagarRecibo(String esquema) {
		return infoCuentasPagarRecibo.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryValidaEstatus(String esquema) {
		return validaEstatus.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getActualizarOrdenCompra(String esquema) {
		return actualizarOrdenCompra.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryBuscarClavePorOrdenXML(String esquema) {
		return cveOrdenXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryDesUsoCFDICP(String esquema) {
		return queryDesUsoCFDICP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryBuscarProveedor(String esquema) {
		return buscarProveedor.replaceAll("<<esquema>>", esquema);
	}

	public static String getQueryUsoCFDIXML(String esquema) {
		return usoCFDIXML.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryBuscarClavePorOrden(String esquema) {
		return clavePorOrden.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryBuscarClavePorOrdenSinUSO(String esquema) {
		return clavePorOrdenSinUSO.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getConsultaCarta(String esquema) {
		return consultaCarta.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryInsertUso(String esquema) {
		return insertUso.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDeleteCartaPorteOrdenEmpresa(String esquema) {
		return deleteCartaPorteOrdenEmpresa.replaceAll("<<esquema>>", esquema);
	}
}
