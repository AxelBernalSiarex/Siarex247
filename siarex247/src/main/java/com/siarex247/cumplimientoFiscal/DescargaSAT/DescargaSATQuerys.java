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
	
	
}
