package com.siarex247.cumplimientoFiscal.ListaNegra;

public class ListaNegraQuerys {


	/*
	private static String detalleListaConf   =  "select ID_REGISTRO, TIPO_DIA, DIA_MES from CONF_LISTANEGRA_SAT";
	private static String guardarListaNegra   =  "insert into CONF_LISTANEGRA_SAT (TIPO_DIA, DIA_MES) values (?,?)";
	private static String eliminaListaNegra   =  "delete from CONF_LISTANEGRA_SAT where  ID_REGISTRO = ?";
	*/
	
	// pantalla de lista negra SAT
	private static String detalleListaNegra   =  "select RFC, RAZON_SOCIAL,SUPUESTO, NOMBRE_ARTICULO, ";
	private static String totalRegistros	  =  "select  count(*) ";
	private static String validarEstatus	  =  "select  ";
	
	private static String consultaColumnas    =  "select FECHAALTA from HISTORICO_PROCESOS where TIPO_PROCESO = 'NEG' and year(FECHAALTA) = ? order by FECHAALTA";

	private static String listaRFC 				=  "select A.RFC,";
	
	public static String getConsultaColumnas(String esquema) {
		return consultaColumnas.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDetalleListaNegra(String esquema) {
		return detalleListaNegra.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getListaRFC(String esquema) {
		return listaRFC.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getValidarEstatus(String esquema) {
		return validarEstatus.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTotalRegistros(String esquema) {
		return totalRegistros.replaceAll("<<esquema>>", esquema);
	}
	
	
}
