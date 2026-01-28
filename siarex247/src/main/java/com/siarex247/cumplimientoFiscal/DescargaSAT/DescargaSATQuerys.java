package com.siarex247.cumplimientoFiscal.DescargaSAT;

public class DescargaSATQuerys {

	
	private static String detalle   =  "select ID_REGISTRO, UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, ESTATUS, FECHA_CANCELACION, EXISTE_BOVEDA from DESCARGA_MASIVA_METADATA_TIMBRADO where RECEPTOR_RFC = ? ";
	private static String detalleExportarCSV   =  "select ID_REGISTRO, UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, ESTATUS, FECHA_CANCELACION, EXISTE_BOVEDA from DESCARGA_MASIVA_METADATA_TIMBRADO ";
	
	private static String ultimaFecha   =  "select max(FECHA_DESCARGA) from HISTORICO_PROCESO_SAT where ESTATUS_DESCARGA = ? ";
	private static String totalRegistros  =  "select count(*) from DESCARGA_MASIVA_METADATA_TIMBRADO where RECEPTOR_RFC = ? ";
//	private static String guardarMetadataTimbrado  =  "insert into DESCARGA_MASIVA_METADATA_TIMBRADO (UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, ESTATUS, FECHA_CANCELACION, EXISTE_BOVEDA, USUARIO_TRAN) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String guardarMetadataTimbrado  =  "insert into DESCARGA_MASIVA_METADATA_TIMBRADO (UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, FECHA_EMISION, MONTO, EFECTO_COMPROBANTE,  TIPO_MONEDA, ESTATUS, EXISTE_BOVEDA, USUARIO_TRAN, USUARIO_CAMBIO, FECHA_CAMBIO) values ";
	private static String actualizarMetadataTimbrado  =  "update DESCARGA_MASIVA_METADATA_TIMBRADO set ESTATUS = ?, TIPO_MONEDA = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = ? where UUID = ? ";
	private static String actualizarBovedaEstatus	   =  "update BOVEDA set ESTATUS_SAT = ? where UUID = ? ";
	private static String actualizarBovedaEmitidosEstatus   =  "update BOVEDA_EMITIDOS set ESTATUS_SAT = ? where UUID = ? ";
	private static String actualizarBovedaNominaEstatus  =  "update BOVEDA_NOMINA set ESTATUS_SAT = ? where UUID = ? ";
	
	
 // emitidos
	private static String detalleEmitidos   =  "select ID_REGISTRO, UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, ESTATUS, FECHA_CANCELACION, EXISTE_BOVEDA from DESCARGA_MASIVA_METADATA_TIMBRADO where EMISOR_RFC = ? ";
	private static String totalRegistrosEmitidos  =  "select count(*) from DESCARGA_MASIVA_METADATA_TIMBRADO where EMISOR_RFC = ? ";
	//private static String revalidarBovedaEmitidos   =  "select ID_REGISTRO, UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, ESTATUS, FECHA_CANCELACION, EXISTE_BOVEDA from DESCARGA_MASIVA_METADATA_TIMBRADO where EMISOR_RFC = ? AND ESTATUS = ? AND EXISTE_BOVEDA = ? ";
	private static String revalidarBoveda			  =  "select ID_REGISTRO, UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, ESTATUS, FECHA_CANCELACION, EXISTE_BOVEDA from DESCARGA_MASIVA_METADATA_TIMBRADO where ESTATUS = ? AND EXISTE_BOVEDA = ? ";
	
	private static String detalleGraficaMonitor   =  "select ID_REGISTRO, UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, TIPO_MONEDA, ESTATUS, FECHA_CANCELACION, EXISTE_BOVEDA from DESCARGA_MASIVA_METADATA_TIMBRADO where FECHA_EMISION between ? and ? and EFECTO_COMPROBANTE in (?,?) ";
	
	
	private static String existeUUID  =  "select ESTATUS, TIPO_MONEDA from DESCARGA_MASIVA_METADATA_TIMBRADO where UUID = ? ";
	
	private static String consultarFechaMinima  =  "select min(FECHA_EMISION) from DESCARGA_MASIVA_METADATA_TIMBRADO where RECEPTOR_RFC = ? ";
	private static String consultarFechaMinimaEmitidos   =  "select min(FECHA_EMISION) from DESCARGA_MASIVA_METADATA_TIMBRADO where EMISOR_RFC = ? and EFECTO_COMPROBANTE not in (?)";	
	private static String consultarFechaMinimaNomina  =  "select min(FECHA_EMISION) from DESCARGA_MASIVA_METADATA_TIMBRADO where EFECTO_COMPROBANTE = ?";
	
	// TEMPLATE (privado)
	private static final String consultarHistoricoMetadataHoy =
		    "SELECT " +
		    "CLAVE_HISTORICO, TIPO_DESCARGA, TIPO_COMPROBANDO, ACCION_SAT, " +
		    "SOLICITUD_SAT, PAQUETE_SAT, FECHA_INICIO, FECHA_FIN, FECHA_DESCARGA, " +
		    "ESTATUS_DESCARGA, MENSAJE_SAT, TOTAL_ARCHIVOS, ARCHIVOS_EXITOSOS, " +
		    "ARCHIVOS_DUPLICADOS, ARCHIVOS_ERROR_RFC, ARCHIVOS_NOMINA, ESTATUS " +
		    "FROM `contrare_<<esquema>>`.`HISTORICO_PROCESO_SAT` " +
		    "WHERE TRIM(TIPO_DESCARGA) = ? AND DATE(FECHA_DESCARGA) = CURDATE() " +
		    "ORDER BY FECHA_DESCARGA DESC";
	
	// Pendientes: no depende de CURDATE(), permite continuar lo de ayer
	private static final String consultarHistoricoMetadataPendientes =
	    "SELECT " +
	    "CLAVE_HISTORICO, TIPO_DESCARGA, TIPO_COMPROBANDO, ACCION_SAT, " +
	    "SOLICITUD_SAT, PAQUETE_SAT, FECHA_INICIO, FECHA_FIN, FECHA_DESCARGA, " +
	    "ESTATUS_DESCARGA, MENSAJE_SAT, TOTAL_ARCHIVOS, ARCHIVOS_EXITOSOS, " +
	    "ARCHIVOS_DUPLICADOS, ARCHIVOS_ERROR_RFC, ARCHIVOS_NOMINA, ESTATUS " +
	    "FROM `contrare_<<esquema>>`.`HISTORICO_PROCESO_SAT` " +
	    "WHERE TRIM(TIPO_DESCARGA) = ? " +
	    "  AND TRIM(ESTATUS_DESCARGA) IN ('INI','SOL','MET','NOT') " +
	    "  AND FECHA_DESCARGA >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
	    "ORDER BY FECHA_DESCARGA DESC, CLAVE_HISTORICO DESC";

	public static String getConsultarHistoricoMetadataPendientes(String esquema) {
	    return consultarHistoricoMetadataPendientes.replace("<<esquema>>", esquema);
	}

	
	// UPDATE: cuando SAT aceptó (guardar idSolicitud y estatus SOL)
	private static final String actualizarHistoricoSolicitudSat =
	    "UPDATE `contrare_<<esquema>>`.`HISTORICO_PROCESO_SAT` " +
	    "SET ACCION_SAT = ?, SOLICITUD_SAT = ?, ESTATUS_DESCARGA = ?, MENSAJE_SAT = ?, " +
	    "FECHA_FIN = CURRENT_TIMESTAMP, FECHA_DESCARGA = CURDATE() " +
	    "WHERE CLAVE_HISTORICO = ?";

	// UPDATE: cuando hay error (estatus ERR + mensaje)
	private static final String actualizarHistoricoErrorSat =
	    "UPDATE `contrare_<<esquema>>`.`HISTORICO_PROCESO_SAT` " +
	    "SET ESTATUS_DESCARGA = ?, MENSAJE_SAT = ?, " +
	    "FECHA_FIN = CURRENT_TIMESTAMP, FECHA_DESCARGA = CURDATE() " +
	    "WHERE CLAVE_HISTORICO = ?";
	
	// UPDATE: guardar paquete + estatus (SOL/MET/ERR) + mensaje
	private static final String actualizarHistoricoPaqueteSat =
	    "UPDATE `contrare_<<esquema>>`.`HISTORICO_PROCESO_SAT` " +
	    "SET ACCION_SAT = ?, PAQUETE_SAT = ?, ESTATUS_DESCARGA = ?, MENSAJE_SAT = ?, " +
	    "FECHA_FIN = CURRENT_TIMESTAMP, FECHA_DESCARGA = CURDATE() " +
	    "WHERE CLAVE_HISTORICO = ?";

	public static String getExisteUUID1(String esquema) {
	    String db = dbContrare(esquema);
	    return "SELECT IFNULL(ID_REGISTRO,0) AS ID_REGISTRO "
	         + "FROM `" + db + "`.`descarga_masiva_metadata_timbrado` "
	         + "WHERE UUID = ? LIMIT 1";
	}

	public static String getGuardarMetadataTimbrado1(String esquema) {
	    String db = dbContrare(esquema);
	    return "INSERT INTO `" + db + "`.`descarga_masiva_metadata_timbrado` "
	         + "(UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, "
	         + " FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, TIPO_MONEDA, ESTATUS, "
	         + " FECHA_CANCELACION, EXISTE_BOVEDA, USUARIO_TRAN) "
	         + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}
	
	// NUEVA: export por rango FI/FF y por RFC (emisor o receptor)
	// NUEVA: export por FECHA_TRANS (lo cargado en la corrida) y por RFC (emisor o receptor)
	// NUEVA: export CSV por rango de FECHA_TRANS (corrida) y por RFC (emisor o receptor)
	private static final String detalleExportarCSVPorTransRango =
	    "SELECT ID_REGISTRO, UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, " +
	    "RECEPTOR_PAC, FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, " +
	    "TIPO_MONEDA, ESTATUS, FECHA_CANCELACION, EXISTE_BOVEDA " +
	    "FROM `contrare_<<esquema>>`.`descarga_masiva_metadata_timbrado` " +
	    "WHERE FECHA_TRANS BETWEEN ? AND ? " +   // <-- OJO: FECHA_TRANS
	    "  AND (EMISOR_RFC = ? OR RECEPTOR_RFC = ?) ";

	public static String getDetalleExportarCSVPorTransRango(String esquema) {
	    return detalleExportarCSVPorTransRango.replace("<<esquema>>", esquema);
	}



	
	


	public static String getActualizarHistoricoPaqueteSat(String esquema) {
	    return actualizarHistoricoPaqueteSat.replace("<<esquema>>", esquema);
	}




	public static String getDetalle(String esquema) {
		return detalle.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getDetalleExportarCSV(String esquema) {
		return detalleExportarCSV.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getUltimaFecha(String esquema) {
		return ultimaFecha.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getTotalRegistros(String esquema) {
		return totalRegistros.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getGuardarMetadataTimbrado(String esquema) {
		return guardarMetadataTimbrado.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getActualizarMetadataTimbrado(String esquema) {
		return actualizarMetadataTimbrado.replaceAll("<<esquema>>", esquema);
	}	
	
	
	public static String getDetalleEmitidos(String esquema) {
		return detalleEmitidos.replaceAll("<<esquema>>", esquema);
	}	
	public static String getTotalRegistrosEmitidos(String esquema) {
		return totalRegistrosEmitidos.replaceAll("<<esquema>>", esquema);
	}	
	public static String getRevalidarBoveda(String esquema) {
		return revalidarBoveda.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getActualizarBovedaEstatus(String esquema) {
		return actualizarBovedaEstatus.replaceAll("<<esquema>>", esquema);
	}
	public static String getActualizarBovedaEmitidosEstatus(String esquema) {
		return actualizarBovedaEmitidosEstatus.replaceAll("<<esquema>>", esquema);
	}
	public static String getActualizarBovedaNominaEstatus(String esquema) {
		return actualizarBovedaNominaEstatus.replaceAll("<<esquema>>", esquema);
	}

	public static String getDetalleGraficaMonitor(String esquema) {
		return detalleGraficaMonitor.replaceAll("<<esquema>>", esquema);
	}
	public static String getExisteUUID(String esquema) {
		return existeUUID.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarFechaMinima(String esquema) {
		return consultarFechaMinima.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getConsultarFechaMinimaEmitidos(String esquema) {
		return consultarFechaMinimaEmitidos.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getConsultarFechaMinimaNomina(String esquema) {
		return consultarFechaMinimaNomina.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getActualizarHistoricoSolicitudSat(String esquema) {
	    return actualizarHistoricoSolicitudSat.replace("<<esquema>>", esquema);
	}

	public static String getActualizarHistoricoErrorSat(String esquema) {
	    return actualizarHistoricoErrorSat.replace("<<esquema>>", esquema);
	}

	// GET público
	public static String getConsultarHistoricoMetadataHoy(String esquema) {
	    return consultarHistoricoMetadataHoy.replace("<<esquema>>", esquema);
	}
	
	private static String dbContrare(String esquema) {
	    String e = (esquema == null ? "" : esquema.trim());
	    if (e.toLowerCase().startsWith("contrare_")) return e;   // por si algún día ya viene con prefijo
	    return "contrare_" + e.toLowerCase();
	}
	

	

	
	


	
	
}
