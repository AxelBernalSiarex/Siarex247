package com.siarex247.cumplimientoFiscal.DescargaSAT;

public class ProcesoDescargaSATQuerys {

	private static String registrarInicioSolicitud   =  "insert into HISTORICO_PROCESO_SAT (TIPO_DESCARGA, TIPO_COMPROBANDO, ACCION_SAT, FECHA_INICIO, FECHA_DESCARGA, ESTATUS_DESCARGA) values (?,?,?,current_timestamp,?,?)";
	private static String consultarSolicitudes	     =  "select CLAVE_HISTORICO, TIPO_COMPROBANDO, ACCION_SAT, SOLICITUD_SAT, PAQUETE_SAT, FECHA_INICIO, FECHA_FIN, FECHA_DESCARGA, ESTATUS_DESCARGA, MENSAJE_SAT from HISTORICO_PROCESO_SAT where TIPO_DESCARGA = ? and FECHA_DESCARGA = ? ";
	private static String actualizarSolicitudes	     =  "update HISTORICO_PROCESO_SAT set ACCION_SAT = ?, SOLICITUD_SAT = ?, PAQUETE_SAT = ?, FECHA_FIN = current_timestamp, ESTATUS_DESCARGA = ?, MENSAJE_SAT = ? where CLAVE_HISTORICO = ?";
	
	private static String actualizarTotales	     	=  "update HISTORICO_PROCESO_SAT set TOTAL_ARCHIVOS = ?, ARCHIVOS_EXITOSOS = ?, ARCHIVOS_DUPLICADOS = ?, ARCHIVOS_ERROR_RFC = ?, ARCHIVOS_NOMINA = ? where CLAVE_HISTORICO = ?";
	
	private static String guardarRegistroMetadata   =  "insert into DESCARGA_MASIVA_METADATA (UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_PAC, FECHA_EMISION, FECHA_CERTIFICACION, MONTO, EFECTO_COMPROBANTE, ESTATUS, FECHA_CANCELACION, EXISTE_BOVEDA) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";	
	private static String encontradosBoveda			=  "update DESCARGA_MASIVA_METADATA set EXISTE_BOVEDA = ? where EXISTE_BOVEDA = ? and UUID in (select UUID from BOVEDA)";
	
	private static String obtenerUltimaCorrida		 =  "SELECT CURDATE() - interval ? DAY from HISTORICO_PROCESOS LIMIT 1";
	
	
	private static String obtenerFechaAyer 	   	   = "select SUBSTR(DATE_SUB(current_date, INTERVAL 1 DAY), 1, 10) as fechaUltimaCorrida from CIERRE_ANIO limit 1";
	private static String buscarRespaldo =  "select TIPO_ENVIO from HISTORICO_PROCESOS where TIPO_PROCESO = ? and date(FECHAALTA) = ?";
	private static String grabarProceso =  "insert into HISTORICO_PROCESOS (TIPO_PROCESO, TIPO_ENVIO, TOTAL_ENVIOS, ESTATUS) values (?,?,?,?)";

	private static String encontradosBovedaTimbradoEmitidos			=  "update DESCARGA_MASIVA_METADATA_TIMBRADO set EXISTE_BOVEDA = ? where EXISTE_BOVEDA = ? and UUID in (select UUID from BOVEDA_EMITIDOS)";
	private static String encontradosBovedaTimbradoRecibidos		=  "update DESCARGA_MASIVA_METADATA_TIMBRADO set EXISTE_BOVEDA = ? where EXISTE_BOVEDA = ? and UUID in (select UUID from BOVEDA)";
	private static String actualizarEstatusCancelado	 	=  "update DESCARGA_MASIVA_METADATA_TIMBRADO set ESTATUS = ? where UUID  = ?";
	
	private static String encontradosBovedaNomina			=  "update DESCARGA_MASIVA_METADATA_TIMBRADO set EXISTE_BOVEDA = ? where EXISTE_BOVEDA = ? and UUID in (select UUID from BOVEDA_NOMINA)";
	private static String updateCancelados					=  "update DESCARGA_MASIVA_METADATA_TIMBRADO set ESTATUS = ?, RECEPTOR_PAC = ?, FECHA_CANCELACION = ?, CLAVE_MOTIVO_CANCELACION = ?, DESCRIPCION_MOTIVO_CANCELACION = ?  where UUID = ?";
	private static String selectCancelado					=  "select EFECTO_COMPROBANTE, EMISOR_RFC, RECEPTOR_RFC from DESCARGA_MASIVA_METADATA_TIMBRADO where UUID = ?";

	
	public static String getRegistrarInicioSolicitud(String esquema) {
		return registrarInicioSolicitud.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getConsultarSolicitudes(String esquema) {
		return consultarSolicitudes.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getActualizarSolicitudes(String esquema) {
		return actualizarSolicitudes.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizarTotales(String esquema) {
		return actualizarTotales.replaceAll("<<esquema>>", esquema);
	}

	public static String getGuardarRegistroMetadata(String esquema) {
		return guardarRegistroMetadata.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEncontradosBoveda(String esquema) {
		return encontradosBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getObtenerUltimaCorrida(String esquema) {
		return obtenerUltimaCorrida.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getObtenerFechaAyer(String esquema) {
		return obtenerFechaAyer.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getBuscarRespaldo(String esquema) {
		return buscarRespaldo.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGrabarProceso(String esquema) {
		return grabarProceso.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getEncontradosBovedaTimbradoEmitidos(String esquema) {
		return encontradosBovedaTimbradoEmitidos.replaceAll("<<esquema>>", esquema);
	}

	public static String getEncontradosBovedaTimbradoRecibidos(String esquema) {
		return encontradosBovedaTimbradoRecibidos.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getActualizarEstatusCancelado(String esquema) {
		return actualizarEstatusCancelado.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEncontradosBovedaNomina(String esquema) {
		return encontradosBovedaNomina.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getUpdateCancelados(String esquema) {
		return updateCancelados.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getSelectCancelado(String esquema) {
		return selectCancelado.replaceAll("<<esquema>>", esquema);
	}
	
	
}
