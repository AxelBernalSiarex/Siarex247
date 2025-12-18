package com.siarex247.configuraciones.EtiquetaComp;

public class EtiquetaCompQuerys {

	private static String detConfComp       = "select ID_REGISTRO, ETIQUETA, ACTIVO, CONFIGURADO, FECHAINI,FECHAFIN, SUBJECT, USUARIO_TRAN, FECHA_TRANS, MENSAJE, VERSION, TIPO_VALIDACION from CONFIGURACION_COMPLEMENTOS ";
	private static String buscarConfComp    = "select ETIQUETA, ACTIVO, CONFIGURADO, FECHAINI,FECHAFIN,SUBJECT,MENSAJE, USUARIO_TRAN, FECHA_TRANS from CONFIGURACION_COMPLEMENTOS where ID_REGISTRO = ?";
	private static String actualizaConfComp = "update CONFIGURACION_COMPLEMENTOS set ACTIVO = ?, FECHAINI = ?, FECHAFIN = ?, SUBJECT = ?, MENSAJE= ?, USUARIO_TRAN = ?, FECHA_TRANS = current_timestamp  where ID_REGISTRO = ?";

	private static String detalleEtiquetasComp  = "select ID_REGISTRO, ETIQUETA, DATO_VALIDA from CONFIGURACION_ETIQUETAS_COMPLEMENTOS where ETIQUETA = ?";
	private static String agregarEtiquetasComp = "insert into CONFIGURACION_ETIQUETAS_COMPLEMENTOS (ETIQUETA, DATO_VALIDA, VERSION) values (?,?,?)";
	private static String eliminaEtiquetasComp  = "delete from CONFIGURACION_ETIQUETAS_COMPLEMENTOS where ETIQUETA = ? and DATO_VALIDA = ? and VERSION = ?";
	private static String detalleEtiquetas  = "select ID_REGISTRO, ETIQUETA, DATO_VALIDA from CONFIGURACION_ETIQUETAS_COMPLEMENTOS where ETIQUETA = ?";
	
	
	public static String getQueryDetalleConfComp(String esquema) {
		return detConfComp.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryBuscarConfComp(String esquema) {
		return buscarConfComp.replaceAll("<<esquema>>", esquema);
	}

	public static String getQueryActualizaConfComp(String esquema) {
		return actualizaConfComp.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryDetalleEtiquetasComp(String esquema) {
		return detalleEtiquetasComp.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryAgregarEtiquetasComp(String esquema) {
		return agregarEtiquetasComp.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getQueryEliminaEtiquetasComp(String esquema) {
		return eliminaEtiquetasComp.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryDetalleEtiquetas(String esquema) {
		return detalleEtiquetas.replaceAll("<<esquema>>", esquema);
	}
	
	
	
}
