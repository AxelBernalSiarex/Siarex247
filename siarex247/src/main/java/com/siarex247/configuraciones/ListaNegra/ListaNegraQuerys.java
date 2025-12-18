package com.siarex247.configuraciones.ListaNegra;

public class ListaNegraQuerys {

	private static String detalleListaConf   =  "select ID_REGISTRO, TIPO_DIA, DIA_MES from CONF_LISTANEGRA_SAT";
	private static String consultarListaConf =  "select ID_REGISTRO, TIPO_DIA, DIA_MES from CONF_LISTANEGRA_SAT where ID_REGISTRO = ?";
	private static String guardarListaNegra   =  "insert into CONF_LISTANEGRA_SAT (TIPO_DIA, DIA_MES) values (?,?)";
	private static String actualizarListaNegra =  "update CONF_LISTANEGRA_SAT set TIPO_DIA = ?, DIA_MES = ? where ID_REGISTRO = ?";
	private static String eliminaListaNegra   =  "delete from CONF_LISTANEGRA_SAT where  ID_REGISTRO = ?";
	private static String consultaColumnas    =  "select FECHAALTA from HISTORICO_PROCESOS where TIPO_PROCESO = 'NEG' and year(FECHAALTA) = ? order by FECHAALTA";

	
	
	public static String getDetalleListaConf(String esquema) {
		return detalleListaConf.replaceAll("<<esquema>>", esquema);
	}

	public static String getConsultarListaConf(String esquema) {
		return consultarListaConf.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGuardarListaNegra(String esquema) {
		return guardarListaNegra.replaceAll("<<esquema>>", esquema);
	}

	public static String getActualizarListaNegra(String esquema) {
		return actualizarListaNegra.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEliminaListaNegra(String esquema) {
		return eliminaListaNegra.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaColumnas(String esquema) {
		return consultaColumnas.replaceAll("<<esquema>>", esquema);
	}
	
}

