package com.siarex247.configuraciones.EtiquetasCP;

public class EtiquetasCPQuerys {

	
	private static String detConfCP               = "select ID_REGISTRO, ID_CATALOGO, ID_ETIQUETA, PATH_XML, DESCRIPCION, FECHA_INI, FECHA_FIN, SUBJECT, MENSAJE, USUARIO_TRAN, FECHA_TRANS, ACTIVO, VAL_ETIQUETA, VERSION from ETIQUETAS_CARTA_PORTE";
	private static String actualizaConfCP         = "update ETIQUETAS_CARTA_PORTE set ACTIVO = ?, FECHA_INI = ?, FECHA_FIN = ?, SUBJECT = ?, MENSAJE= ?, USUARIO_TRAN = ?, FECHA_TRANS = CURRENT_TIMESTAMP, VAL_ETIQUETA = ? where ID_REGISTRO = ?";
	// private static String detalleEtiquetas        = "select a.ID_REGISTRO, a.ID_CATALOGO, b.DISPONIBLE1, b.DISPONIBLE2 from ETIQUETAS_CATALOGO a, CATALOGOS_CARTA_PORTE b where a.ID_VALOR = b.ID_REGISTRO and a.ID_CATALOGO = ?";
	private static String detalleEtiquetasCP	  = "select ID_REGISTRO, ETIQUETA, DATO_VALIDA from ETIQUETAS_CATALOGO where ETIQUETA = ?";
	private static String agregarEtiquetas        = "insert into ETIQUETAS_CATALOGO (ETIQUETA, DATO_VALIDA, VERSION, USUARIO_TRAN) values (?,?,?,?)";
	private static String eliminaEtiquetas        = "delete from ETIQUETAS_CATALOGO where ID_REGISTRO = ?";
	private static String listadoActivos20       = "select ID_REGISTRO, ID_CATALOGO, ID_ETIQUETA, PATH_XML, DESCRIPCION, FECHA_INI, FECHA_FIN, SUBJECT, MENSAJE, VAL_ETIQUETA from ETIQUETAS_CARTA_PORTE where ACTIVO = ? and VERSION = ?";
	private static String listadoActivos         = "select ID_REGISTRO, ID_CATALOGO, ID_ETIQUETA, PATH_XML, DESCRIPCION, FECHA_INI, FECHA_FIN, SUBJECT, MENSAJE, VAL_ETIQUETA from ETIQUETAS_CARTA_PORTE where ACTIVO = ? and VERSION = ?";
	
	
	
	public static String getQueryDetConfCP(String esquema) {
		return detConfCP.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryActualizaConf(String esquema) {
		return actualizaConfCP.replaceAll("<<esquema>>", esquema);
	}
	
	/*
	public static String getQueryDetalleEtiquetas(String esquema) {
		return detalleEtiquetas.replaceAll("<<esquema>>", esquema);
	}
	*/
	
	public static String getDetalleEtiquetasCP(String esquema) {
		return detalleEtiquetasCP.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryAgregarEtiquetas(String esquema) {
		return agregarEtiquetas.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryEliminaEtiquetas(String esquema) {
		return eliminaEtiquetas.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getListadoActivos20(String esquema) {
		return listadoActivos20.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getListadoActivos(String esquema) {
		return listadoActivos.replaceAll("<<esquema>>", esquema);
	}
	
}
