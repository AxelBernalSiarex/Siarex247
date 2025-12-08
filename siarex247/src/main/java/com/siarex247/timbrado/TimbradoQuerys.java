package com.siarex247.timbrado;

public class TimbradoQuerys {

	private static String consultarResultadoXML     =   "select ID_REGISTRO, EMPRESA, FOLIO_EMPRESA, TIPO_METODO, UUID, CODE,RESPUESTA, MENSAJE, USUARIO_TRANS from BITACORA_TIMBRADO_EXPRESS where UUID = ? order by ID_REGISTRO desc limit 1";
	private static String guardarBitacoraTimbrado   =   "insert into BITACORA_TIMBRADO_EXPRESS (EMPRESA, FOLIO_EMPRESA, TIPO_METODO, UUID, CODE,RESPUESTA, MENSAJE, USUARIO_TRANS  ) values (?,?,?,?,?,?,?,?)";
	
	
	
	public static String getConsultarResultadoXML(String esquema) {
		return consultarResultadoXML.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGuardarBitacoraTimbrado(String esquema) {
		return guardarBitacoraTimbrado.replaceAll("<<esquema>>", esquema);
	}

	
}
