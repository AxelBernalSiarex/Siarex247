package com.siarex247.cumplimientoFiscal.ValidarXML;

public class ValidarXMLQuerys {

	
	private static String detalleXMLInd =  "select EMISOR_RFC, EMISOR_NOMBRE, SUB_TOTAL, TOTAL, DES_TIPO_MONEDA, ESTADO_SAT, ESTATUS_SAT, SERIE, FOLIO, TIPO_COMPROBANTE from INFORMACION_XML where ID_EMPLEADO = ? order by EMISOR_NOMBRE asc, TIPO_MONEDA asc";
	private static String detalleXMLGpo =  "select EMISOR_RFC, EMISOR_NOMBRE, SUM(SUB_TOTAL), SUM(TOTAL), DES_TIPO_MONEDA, ESTADO_SAT, ESTATUS_SAT, SERIE, FOLIO from INFORMACION_XML where ID_EMPLEADO = ? GROUP BY EMISOR_RFC, TIPO_MONEDA order by EMISOR_NOMBRE asc, TIPO_MONEDA asc";
	private static String sumaTotales =  "select EMISOR_RFC,sum(TOTAL) from INFORMACION_XML where ID_EMPLEADO = ? group by EMISOR_RFC order by EMISOR_RFC asc";
	private static String eliminarXML =  "delete from INFORMACION_XML where ID_EMPLEADO = ?";
	private static String agregaXML =  "insert into INFORMACION_XML ( UUID, SERIE, FOLIO, FECHA_FACTURA, FORMA_PAGO, METODO_PAGO,TIPO_MONEDA, DES_TIPO_MONEDA, NUMERO_CUENTA_PAGO, SUB_TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, TOTAL, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RETENCION_IVA, TRANSLADO_IVA, ISR_RET, TRANSLADOS_IEPS, ID_EMPLEADO, ESTADO_SAT, ESTATUS_SAT , TIPO_COMPROBANTE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	public static String getQueryDetalleXMLInd(String esquema) {
		return detalleXMLInd.replaceAll("<<esquema>>", esquema);
	}
	
	
	
	public static String getQueryDetalleXMLGpo(String esquema) {
		return detalleXMLGpo.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQuerySumaTotales(String esquema) {
		return sumaTotales.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryEliminarXML(String esquema) {
		return eliminarXML.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryAgregaXML(String esquema) {
		return agregaXML.replaceAll("<<esquema>>", esquema);
	}
	
}
