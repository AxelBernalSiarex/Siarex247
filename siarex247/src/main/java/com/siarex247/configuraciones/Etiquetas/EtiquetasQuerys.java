package com.siarex247.configuraciones.Etiquetas;

public class EtiquetasQuerys {
	private static String detalleConf       = "select ID_REGISTRO, ETIQUETA, ACTIVO, CONFIGURADO, FECHAINI,FECHAFIN, SUBJECT, USUARIO_TRAN, FECHA_TRANS, MENSAJE, VERSION, TIPO_VALIDACION from CONFIGURACION";

	private static String actualizaConf     = "update CONFIGURACION set ACTIVO = ?, FECHAINI = ?, FECHAFIN = ?, SUBJECT = ?, MENSAJE= ?, USUARIO_TRAN = ?, FECHA_TRANS = current_timestamp  where ID_REGISTRO = ?";

	private static String buscarConf        = "select ETIQUETA, ACTIVO, CONFIGURADO, FECHAINI,FECHAFIN,SUBJECT,MENSAJE, USUARIO_TRAN, FECHA_TRANS from CONFIGURACION where ID_REGISTRO = ?";
	
	private static String detalleEtiquetas  = "select ID_REGISTRO, ETIQUETA, DATO_VALIDA from CONFIGURACION_ETIQUETAS where ETIQUETA = ?";
	private static String agregarEtiquetas  = "insert into CONFIGURACION_ETIQUETAS (ETIQUETA, DATO_VALIDA, VERSION) values (?,?,?)";
	private static String eliminaEtiquetas  = "delete from CONFIGURACION_ETIQUETAS where ETIQUETA = ? and DATO_VALIDA = ? and VERSION = ?";
	
	private static String etiquetasSistema  = "select ETIQUETA, RUTA_XML, PROPIEDAD, TIPO, SUBJECT, MENSAJE from ETIQUETAS";
	
	/*
	private static String detConfComp       = "select ID_REGISTRO, ETIQUETA, ACTIVO, CONFIGURADO, FECHAINI,FECHAFIN, SUBJECT, USUARIO_TRAN, FECHA_TRANS, MENSAJE, VERSION, RUTA_XML, PROPIEDAD from CONFIGURACION_COMPLEMENTOS";
	private static String buscarConfComp    = "select ETIQUETA, ACTIVO, CONFIGURADO, FECHAINI,FECHAFIN,SUBJECT,MENSAJE, USUARIO_TRAN, FECHA_TRANS from CONFIGURACION_COMPLEMENTOS where ID_REGISTRO = ?";
	private static String actualizaConfComp = "update CONFIGURACION_COMPLEMENTOS set ACTIVO = ?, FECHAINI = ?, FECHAFIN = ?, SUBJECT = ?, MENSAJE= ?, USUARIO_TRAN = ?, FECHA_TRANS = current_timestamp  where ID_REGISTRO = ?";
	private static String detalleEtiquetasComp  = "select ID_REGISTRO, ETIQUETA, DATO_VALIDA from CONFIGURACION_ETIQUETAS_COMPLEMENTOS where ETIQUETA = ?";
	private static String agregarEtiquetasComp = "insert into CONFIGURACION_ETIQUETAS_COMPLEMENTOS (ETIQUETA, DATO_VALIDA, VERSION) values (?,?,?)";
	private static String eliminaEtiquetasComp  = "delete from CONFIGURACION_ETIQUETAS_COMPLEMENTOS where ID_REGISTRO = ?";
	*/
	
	public static String getQueryDetalleConf(String esquema) {
		return detalleConf.replaceAll("<<esquema>>", esquema);
	}
	
	
	/*
	public static String getQueryDetalleConfComp(String esquema) {
		return detConfComp.replaceAll("<<esquema>>", esquema);
	}
	*/
	public static String getQueryActualizaConf(String esquema) {
		return actualizaConf.replaceAll("<<esquema>>", esquema);
	}
	/*
	public static String getQueryActualizaConfComp(String esquema) {
		return actualizaConfComp.replaceAll("<<esquema>>", esquema);
	}
	*/
	public static String getQueryBuscarConf(String esquema) {
		return buscarConf.replaceAll("<<esquema>>", esquema);
	}
	/*
	public static String getQueryBuscarConfComp(String esquema) {
		return buscarConfComp.replaceAll("<<esquema>>", esquema);
	}
	*/
	public static String getQueryDetalleEtiquetas(String esquema) {
		return detalleEtiquetas.replaceAll("<<esquema>>", esquema);
	}
	/*
	public static String getQueryDetalleEtiquetasComp(String esquema) {
		return detalleEtiquetasComp.replaceAll("<<esquema>>", esquema);
	}
	*/
	public static String getQueryAgregarEtiquetas(String esquema) {
		return agregarEtiquetas.replaceAll("<<esquema>>", esquema);
	}
	/*
	public static String getQueryAgregarEtiquetasComp(String esquema) {
		return agregarEtiquetasComp.replaceAll("<<esquema>>", esquema);
	}
	*/
	public static String getQueryEliminaEtiquetas(String esquema) {
		return eliminaEtiquetas.replaceAll("<<esquema>>", esquema);
	}
	/*
	public static String getQueryEliminaEtiquetasComp(String esquema) {
		return eliminaEtiquetasComp.replaceAll("<<esquema>>", esquema);
	}
	*/
	public static String getQueryetiquetasSistema(String esquema) {
		return etiquetasSistema.replaceAll("<<esquema>>", esquema);
	}
	
 }
