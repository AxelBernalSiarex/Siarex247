package com.siarex247.estadisticas.reporteNomina;

public class ProcesoNominaQuerys {

	private static String obtenerFechaMaxima   =  "select max(FECHA_MOVIMIENTO) from REPORTE_NOMINA";
	private static String guardarRegistrosNomina  =  "insert into REPORTE_NOMINA (UUID, TIPO_NOMINA, FECHA_PAGO, FECHA_INICIAL_PAGO, FECHA_FINAL_PAGO, NUM_DIAS_PAGADOS, TOTAL_PERCEPCIONES, TOTAL_DEDUCCIONES, TOTAL_OTROS, TOTAL, ORIGEN_RECURSO, ESTATUS, SERIE, FOLIO, FECHA_FACTURA, FECHA_TIMBRADO, EMISOR_RFC, EMISOR_NOMBRE, REGISTRO_PATRONAL, RECEPTOR_RFC, RECEPTOR_NOMBRE, CURP, NUM_SEGURO_SOCIAL, FECHA_INICIO_LABORAL, ANTIGUEDAD, TIPO_CONTRATO, TIPO_JORNADA, TIPO_REGIMENT, NUM_EMPLEADO, RIESGO_PUESTO, DEPARTAMENTO, PUESTO, SINDICALIZADO, PERIODICIDAD_PAGO, SALARIO_BASE_COT_APOR, SALARIO_DIARIO_INTEGRADO, CLAVE_ENT_FED, INGRESO_ACUMULABLE, INGRESO_NO_ACUMULABLE, ULTIMO_SUELDO_MENSUAL, ANIOS_SERVICIO, TOTAL_PAGADO, FECHA_MOVIMIENTO) " +
														"select A.UUID, A.TIPO_NOMINA, A.FECHA_PAGO, A.FECHA_INICIAL_PAGO, A.FECHA_FINAL_PAGO, A.NUM_DIAS_PAGADOS, A.TOTAL_PERCEPCIONES, A.TOTAL_DEDUCCIONES, A.TOTAL_OTROS, A.TOTAL, C.ORIGEN_RECURSO, 'ESTATUS', A.SERIE, A.FOLIO, A.FECHA_FACTURA, A.FECHA_TIMBRADO, A.EMISOR_RFC, A.EMISOR_NOMBRE, C.REGISTRO_PATRONAL, A.RECEPTOR_RFC, A.RECEPTOR_NOMBRE, B.CURP, B.NUM_SEGURO_SOCIAL, B.FECHA_INICIO_LABORAL, B.ANTIGUEDAD, B.TIPO_CONTRATO, B.TIPO_JORNADA, B.TIPO_REGIMENT, B.NUM_EMPLEADO, B.RIESGO_PUESTO, B.DEPARTAMENTO, B.PUESTO, B.SINDICALIZADO, B.PERIODICIDAD_PAGO, B.SALARIO_BASE_COT_APOR, B.SALARIO_DIARIO_INTEGRADO, B.CLAVE_ENT_FED, E.INGRESO_ACUMULABLE, E.INGRESO_NO_ACUMULABLE, E.ULTIMO_SUELDO_MENSUAL, E.ANIOS_SERVICIO, E.TOTAL_PAGADO, A.FECHA_TRANS from BOVEDA_NOMINA A left join BOVEDA_NOMINA_RECEPTOR B on A.UUID = B.UUID left join BOVEDA_NOMINA_EMISOR C on A.UUID = C.UUID left join BOVEDA_NOMINA_PERCEPCIONES_SEPARACION E on A.UUID = E.UUID where A.FECHA_TRANS > ?";
	
	private static String obtenerUUID   			=  "select UUID from REPORTE_NOMINA where FECHA_MOVIMIENTO > ?";
	private static String updateSubsidioCausado		=  "update REPORTE_NOMINA set SUBSIDIO_CAUSADO = ? where UUID = ?";
	private static String obtenerSubsidioCausado	=  "select SUBSIDIO_CAUSADO from BOVEDA_NOMINA_OTROPAGOS WHERE UUID = ? limit 1";

	private static String guardarPercepciones  		=  "insert into REPORTE_NOMINA_PERCEPCIONES (UUID, FECHA_MOVIMIENTO) " +
														"select UUID, FECHA_TRANS from BOVEDA_NOMINA where FECHA_TRANS > ?";

	private static String obtenerPercepcionesUUID	=  "select TIPO_PERCEPCION, sum(IMPORTE_GRAVADO), sum(IMPORTE_EXCENTO) from BOVEDA_NOMINA_PERCEPCIONES_DETALLE where UUID = ? group by TIPO_PERCEPCION";
	private static String updatePercepcionesUUID	=  "update REPORTE_NOMINA_PERCEPCIONES set <<PERCEPCION_GRAVADO>> = ?, <<PERCEPCION_EXCENTO>> = ? where UUID = ?";
	

	private static String guardarDeducciones  		=  "insert into REPORTE_NOMINA_DEDUCCIONES (UUID, FECHA_MOVIMIENTO) " +
														"select UUID, FECHA_TRANS from BOVEDA_NOMINA where FECHA_TRANS > ?";

	private static String obtenerDeduccionesUUID	=  "select TIPO_DEDUCCIONES, sum(IMPORTE) from BOVEDA_NOMINA_DEDUCCIONES_DETALLE where UUID = ? group by TIPO_DEDUCCIONES";
	private static String updateDeduccionesUUID		=  "update REPORTE_NOMINA_DEDUCCIONES set <<DEDUCCIONES>> = ? where UUID = ?";

	
	private static String guardarOtroPago  			=  "insert into REPORTE_NOMINA_OTROPAGO (UUID, FECHA_MOVIMIENTO) " +
														"select UUID, FECHA_TRANS from BOVEDA_NOMINA where FECHA_TRANS > ?";

	private static String obtenerOtroPagoUUID		=  "select TIPO_OTRO_PAGO, sum(IMPORTE) from BOVEDA_NOMINA_OTROPAGOS where UUID = ? group by TIPO_OTRO_PAGO";
	private static String updateOtroPagoUUID		=  "update REPORTE_NOMINA_OTROPAGO set <<OTRO_PAGO>> = ? where UUID = ?";

	
	public static String getObtenerFechaMaxima(String esquema) {
		return obtenerFechaMaxima.replaceAll("<<esquema>>", esquema);
	}	

	public static String getGuardarRegistrosNomina(String esquema) {
		return guardarRegistrosNomina.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getObtenerUUID(String esquema) {
		return obtenerUUID.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getUpdateSubsidioCausado(String esquema) {
		return updateSubsidioCausado.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getObtenerSubsidioCausado(String esquema) {
		return obtenerSubsidioCausado.replaceAll("<<esquema>>", esquema);
	}	
	
	
	
	public static String getGuardarPercepciones(String esquema) {
		return guardarPercepciones.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getObtenerPercepcionesUUID(String esquema) {
		return obtenerPercepcionesUUID.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getUpdatePercepcionesUUID(String esquema) {
		return updatePercepcionesUUID.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGuardarDeducciones(String esquema) {
		return guardarDeducciones.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getObtenerDeduccionesUUID(String esquema) {
		return obtenerDeduccionesUUID.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getUpdateDeduccionesUUID(String esquema) {
		return updateDeduccionesUUID.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGuardarOtroPago(String esquema) {
		return guardarOtroPago.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getObtenerOtroPagoUUID(String esquema) {
		return obtenerOtroPagoUUID.replaceAll("<<esquema>>", esquema);
	}

	public static String getUpdateOtroPagoUUID(String esquema) {
		return updateOtroPagoUUID.replaceAll("<<esquema>>", esquema);
	}
	
	
}
