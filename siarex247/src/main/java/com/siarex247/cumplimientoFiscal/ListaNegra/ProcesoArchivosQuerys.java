package com.siarex247.cumplimientoFiscal.ListaNegra;

public class ProcesoArchivosQuerys {

	private static String guardarListaNegra        = "insert into LISTA_NEGRA_SAT_TEMP (RFC, RAZON_SOCIAL, SUPUESTO, NOMBRE_ARTICULO, DISPONIBLE) values ";
	// private static String guardarListaNegraCondonados        = "insert into LISTA_NEGRA_SAT_TEMP_2 (RFC, RAZON_SOCIAL, SUPUESTO, NOMBRE_ARTICULO, DISPONIBLE) values ";
	
	private static String guardarListaNegraUno     = "insert into LISTA_NEGRA_SAT_TEMP (RFC, RAZON_SOCIAL, SUPUESTO, NOMBRE_ARTICULO, DISPONIBLE) values (?,?,?,?,?)";
	private static String updateListaNegra  	   = "update LISTA_NEGRA_SAT set DISPONIBLE_<<numDisponible>> = 1, FECHA_TRASACCION = current_timestamp where RFC in (select RFC from LISTA_NEGRA_SAT_TEMP) and NOMBRE_ARTICULO = ?";
	// private static String updateListaNegraCondonados  	   = "update LISTA_NEGRA_SAT A inner join LISTA_NEGRA_SAT_TEMP_2 B on A.RFC = B.RFC  set A.DISPONIBLE_<<numDisponible>> = 1, A.RAZON_SOCIAL = B.RAZON_SOCIAL WHERE A.NOMBRE_ARTICULO  = ?";
	
	private static String guardarListaNegraMasivo  = "insert into LISTA_NEGRA_SAT (RFC, RAZON_SOCIAL,  SUPUESTO, NOMBRE_ARTICULO, DISPONIBLE_<<numDisponible>>) select RFC, RAZON_SOCIAL,  SUPUESTO, NOMBRE_ARTICULO, DISPONIBLE from LISTA_NEGRA_SAT_TEMP where RFC not in (select RFC from LISTA_NEGRA_SAT where NOMBRE_ARTICULO = ?)";
	private static String deleteListaNegraTemp 	   = "delete from LISTA_NEGRA_SAT_TEMP";
	
	private static String consultarConfiguracion  = "select DIA_MES, TIPO_DIA from CONF_LISTANEGRA_SAT";
	private static String guardarHistorico  	  = "insert into LISTA_NEGRA_SAT_HISTORICO  (RFC, RAZON_SOCIAL, SUPUESTO, NOMBRE_ARTICULO, DISPONIBLE_1, DISPONIBLE_2, DISPONIBLE_3, DISPONIBLE_4, DISPONIBLE_5, DISPONIBLE_6, DISPONIBLE_7, DISPONIBLE_8, DISPONIBLE_9, DISPONIBLE_10, DISPONIBLE_11, DISPONIBLE_12, DISPONIBLE_13, DISPONIBLE_14, DISPONIBLE_15, DISPONIBLE_16, DISPONIBLE_17, DISPONIBLE_18, DISPONIBLE_19, DISPONIBLE_20, DISPONIBLE_21, DISPONIBLE_22, DISPONIBLE_23, DISPONIBLE_24, DISPONIBLE_25, DISPONIBLE_26, DISPONIBLE_27, DISPONIBLE_28, DISPONIBLE_29, DISPONIBLE_30, DISPONIBLE_31, DISPONIBLE_32, DISPONIBLE_33, DISPONIBLE_34, DISPONIBLE_35, DISPONIBLE_36, DISPONIBLE_37, DISPONIBLE_38, DISPONIBLE_39, DISPONIBLE_40, DISPONIBLE_41, DISPONIBLE_42, DISPONIBLE_43, DISPONIBLE_44, DISPONIBLE_45, DISPONIBLE_46, DISPONIBLE_47, DISPONIBLE_48, FECHA_TRASACCION) "
													+ "select RFC, RAZON_SOCIAL, SUPUESTO, NOMBRE_ARTICULO, DISPONIBLE_1, DISPONIBLE_2, DISPONIBLE_3, DISPONIBLE_4, DISPONIBLE_5, DISPONIBLE_6, DISPONIBLE_7, DISPONIBLE_8, DISPONIBLE_9, DISPONIBLE_10, DISPONIBLE_11, DISPONIBLE_12, DISPONIBLE_13, DISPONIBLE_14, DISPONIBLE_15, DISPONIBLE_16, DISPONIBLE_17, DISPONIBLE_18, DISPONIBLE_19, DISPONIBLE_20, DISPONIBLE_21, DISPONIBLE_22, DISPONIBLE_23, DISPONIBLE_24, DISPONIBLE_25, DISPONIBLE_26, DISPONIBLE_27, DISPONIBLE_28, DISPONIBLE_29, DISPONIBLE_30, DISPONIBLE_31, DISPONIBLE_32, DISPONIBLE_33, DISPONIBLE_34, DISPONIBLE_35, DISPONIBLE_36, DISPONIBLE_37, DISPONIBLE_38, DISPONIBLE_39, DISPONIBLE_40, DISPONIBLE_41, DISPONIBLE_42, DISPONIBLE_43, DISPONIBLE_44, DISPONIBLE_45, DISPONIBLE_46, DISPONIBLE_47, DISPONIBLE_48, FECHA_TRASACCION from LISTA_NEGRA_SAT ";
	
	private static String eliminarListaNegra      = "delete from LISTA_NEGRA_SAT";
	private static String setAutoincrementListaNegra      = " alter table LISTA_NEGRA_SAT auto_increment = 1 ";
	
	//private static String ultimaCorrida =  "select max(FECHAALTA) from HISTORICO_PROCESOS where TIPO_PROCESO = ?";
	private static String ultimaCorrida =  "select FECHAALTA from HISTORICO_PROCESOS where TIPO_PROCESO = ? order by FECHAALTA desc limit 1";
	private static String buscarRespaldo =  "select TIPO_ENVIO from HISTORICO_PROCESOS where TIPO_PROCESO = ? and date(FECHAALTA) = ?";
	private static String grabarProceso =  "insert into HISTORICO_PROCESOS (TIPO_PROCESO, TIPO_ENVIO, TOTAL_ENVIOS, ESTATUS) values (?,?,?,?)";
	private static String numEjecuciones =  "select count(*) from HISTORICO_PROCESOS where TIPO_PROCESO = ? and year(FECHAALTA) = ?";

	
	
	public static String getGuardarListaNegra(String esquema) {
		return guardarListaNegra.replaceAll("<<esquema>>", esquema);
	}
	/*
	public static String getGuardarListaNegraCondonados(String esquema) {
		return guardarListaNegraCondonados.replaceAll("<<esquema>>", esquema);
	}
	*/
	
	
	public static String getGuardarListaNegraUno(String esquema) {
		return guardarListaNegraUno.replaceAll("<<esquema>>", esquema);
	}

	public static String getConsultarConfiguracion(String esquema) {
		return consultarConfiguracion.replaceAll("<<esquema>>", esquema);
	}

	
	
	public static String getUpdateListaNegra(String esquema, int numDisponible) {
		return updateListaNegra.replaceAll("<<esquema>>", esquema).replaceAll("<<numDisponible>>", String.valueOf(numDisponible));
	}
	
	/*
	public static String getUpdateListaNegraCondonados(String esquema, int numDisponible) {
		return updateListaNegraCondonados.replaceAll("<<esquema>>", esquema).replaceAll("<<numDisponible>>", String.valueOf(numDisponible));
	}
	*/
	public static String getGuardarListaNegraMasivo(String esquema, int numDisponible) {
		return guardarListaNegraMasivo.replaceAll("<<esquema>>", esquema).replaceAll("<<numDisponible>>", String.valueOf(numDisponible));
	}

	public static String getDeleteListaNegraTemp(String esquema) {
		return deleteListaNegraTemp.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getGuardarHistorico(String esquema) {
		return guardarHistorico.replaceAll("<<esquema>>", esquema);
	}

	public static String getEliminarListaNegra(String esquema) {
		return eliminarListaNegra.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getSetAutoincrementListaNegra(String esquema) {
		return setAutoincrementListaNegra.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getUltimaCorrida(String esquema) {
		return ultimaCorrida.replaceAll("<<esquema>>", esquema);
	}


	public static String getBuscarRespaldo(String esquema) {
		return buscarRespaldo.replaceAll("<<esquema>>", esquema);
	}

	public static String getGrabarProceso(String esquema) {
		return grabarProceso.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getNumEjecuciones(String esquema) {
		return numEjecuciones.replaceAll("<<esquema>>", esquema);
	}
	
}
