package com.siarex247.cumplimientoFiscal.BovedaNomina;

public class BovedaNominaQuerys {

	
	private static String detalleBoveda =  "select ID_REGISTRO, UUID, SERIE, FOLIO,FECHA_FACTURA, METODO_PAGO, MONEDA, SUB_TOTAL, TOTAL, DESCUENTO, TOTAL_PERCEPCIONES, TOTAL_DEDUCCIONES, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, TIPO_COMPROBANTE, FECHA_TIMBRADO from BOVEDA_NOMINA E where E.TIPO_COMPROBANTE = ?";
	private static String totalRegistros =  "select count(*) from BOVEDA_NOMINA E where TIPO_COMPROBANTE = ?";
	
	private static String consultaBoveda =  "select ID_REGISTRO, UUID, SERIE, FOLIO,FECHA_FACTURA, METODO_PAGO, MONEDA, SUB_TOTAL, TOTAL, DESCUENTO, TOTAL_PERCEPCIONES, TOTAL_DEDUCCIONES, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, TIPO_COMPROBANTE from BOVEDA_NOMINA E where E.ID_REGISTRO = ?";
	
	private static String altaBoveda 	=  "insert into BOVEDA_NOMINA (UUID, VERSION_XML, SERIE, FOLIO, FECHA_FACTURA, MONEDA, TIPO_COMPROBANTE, METODO_PAGO, LUGAR_EXPEDICION, SUB_TOTAL, TOTAL, DESCUENTO, TOTAL_PERCEPCIONES, TOTAL_DEDUCCIONES, TOTAL_OTROS, EMISOR_RFC, EMISOR_NOMBRE, EMISOR_REGIMEN, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_DOMICILIO, RECEPTOR_REGIMEN_FISCAL, RECEPTOR_USO, VERSION_NOMINA, FECHA_TIMBRADO, RFC_PROVCERT, FECHA_PAGO, FECHA_INICIAL_PAGO, FECHA_FINAL_PAGO, NUM_DIAS_PAGADOS, TIPO_NOMINA, USUARIO_TRAN) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String altaEmisorNomina	= "insert into BOVEDA_NOMINA_EMISOR (UUID, CURP, REGISTRO_PATRONAL, RFC_PATRON_ORIGEN, ORIGEN_RECURSO, MONTO_RECURSO_PROPIO) values (?,?,?,?,?,?)";
	private static String altaReceptorNomina	= "insert into BOVEDA_NOMINA_RECEPTOR (UUID, NUM_EMPLEADO, CURP, NUM_SEGURO_SOCIAL, FECHA_INICIO_LABORAL, ANTIGUEDAD, TIPO_CONTRATO, SINDICALIZADO, TIPO_JORNADA, TIPO_REGIMENT, DEPARTAMENTO, PUESTO, RIESGO_PUESTO, PERIODICIDAD_PAGO, BANCO, CUENTA_BANCARIA, SALARIO_BASE_COT_APOR, SALARIO_DIARIO_INTEGRADO, CLAVE_ENT_FED) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String altaPercepciones	= "insert into BOVEDA_NOMINA_PERCEPCIONES (UUID, TOTAL_SUELDOS, TOTAL_GRAVADO, TOTAL_EXCENTO, TOTAL_SEPARACION_INDEMIZACION, TOTAL_JUBILACION_RETIRO) values (?,?,?,?,?,?)";
	private static String altaPercepcionesDetalle	= "insert into BOVEDA_NOMINA_PERCEPCIONES_DETALLE (UUID, TIPO_PERCEPCION, CLAVE, CONCEPTO, IMPORTE_GRAVADO, IMPORTE_EXCENTO) values (?,?,?,?,?,?)";
	private static String altaPercepcionesSeparacion = "insert into BOVEDA_NOMINA_PERCEPCIONES_SEPARACION (UUID, TOTAL_PAGADO, ANIOS_SERVICIO, ULTIMO_SUELDO_MENSUAL, INGRESO_ACUMULABLE, INGRESO_NO_ACUMULABLE) values (?,?,?,?,?,?)";
	private static String altaPercepcionesJubilacion = "insert into BOVEDA_NOMINA_PERCEPCIONES_JUBILACION (UUID, TOTAL_UNA_EXHIBICION, TOTAL_PARCILIDAD, MONTO_DIARIO, INGRESO_ACUMULABLE, INGRESO_NO_ACUMULABLE) values (?,?,?,?,?,?)";
	
	private static String altaDeducciones	   		= "insert into BOVEDA_NOMINA_DEDUCCIONES (UUID, TOTAL_OTRAS_DEDUCCIONES, TOTAL_IMPUESTOS_RETENIDOS) values (?,?,?)";
	private static String altaDeduccionesDetalle	= "insert into BOVEDA_NOMINA_DEDUCCIONES_DETALLE (UUID, TIPO_DEDUCCIONES, CLAVE, CONCEPTO, IMPORTE) values (?,?,?,?,?)";
	private static String altaOtroPago				= "insert into BOVEDA_NOMINA_OTROPAGOS (UUID, TIPO_OTRO_PAGO, CLAVE, CONCEPTO, IMPORTE, SUBSIDIO_CAUSADO, SALDO_FAVOR, REMANENTE_SALDO_FAVOR, ANNIO) values (?,?,?,?,?,?,?,?,?)";
	
	
	// querys para eliminar
	private static String eliminarBovedaNomina				= "delete from BOVEDA_NOMINA where UUID = ?"; 
	private static String eliminarBovedaNominaConceptos		= "delete from BOVEDA_CONCEPTOS where UUID = ?";
	private static String eliminarBovedaNominaEmisor		= "delete from BOVEDA_NOMINA_EMISOR where UUID = ?";
	private static String eliminarBovedaNominaReceptor		= "delete from BOVEDA_NOMINA_RECEPTOR where UUID = ?";
	private static String eliminarBovedaNominaPercepciones		= "delete from BOVEDA_NOMINA_PERCEPCIONES where UUID = ?";
	private static String eliminarBovedaNominaPercepcionesDetalle		= "delete from BOVEDA_NOMINA_PERCEPCIONES_DETALLE where UUID = ?";
	private static String eliminarBovedaNominaDeducciones		= "delete from BOVEDA_NOMINA_DEDUCCIONES where UUID = ?";
	private static String eliminarBovedaNominaDeduccionesDetalle		= "delete from BOVEDA_NOMINA_DEDUCCIONES_DETALLE where UUID = ?";
	private static String eliminarBovedaNominaOtrosPago		= "delete from BOVEDA_NOMINA_OTROPAGOS where UUID = ?";
	
	
	private static String encontradosBoveda			=  "update DESCARGA_MASIVA_METADATA_TIMBRADO set EXISTE_BOVEDA = ? where EXISTE_BOVEDA = ? and UUID in (select UUID from BOVEDA_NOMINA)";
	private static String reporteDetalle 			=  "select E.ID_REGISTRO, E.UUID, E.RECEPTOR_RFC, E.RECEPTOR_NOMBRE, E.FECHA_PAGO, B.NUM_EMPLEADO, B.PUESTO, C.REGISTRO_PATRONAL, D.TIPO_PERCEPCION, D.CLAVE, D.CONCEPTO, D.IMPORTE_GRAVADO, D.IMPORTE_EXCENTO from BOVEDA_NOMINA E left join BOVEDA_NOMINA_RECEPTOR B on E.UUID = B.UUID left join BOVEDA_NOMINA_EMISOR C on E.UUID = C.UUID left join BOVEDA_NOMINA_PERCEPCIONES_DETALLE D on E.UUID = D.UUID where E.TIPO_COMPROBANTE = ?";
	private static String obtenerDeducciones        =  "select TIPO_DEDUCCIONES, CLAVE, CONCEPTO, IMPORTE from BOVEDA_NOMINA_DEDUCCIONES_DETALLE where UUID = ?";
	private static String obtenerOtroPago	        =  "select TIPO_OTRO_PAGO, CLAVE, CONCEPTO, IMPORTE, SUBSIDIO_CAUSADO from BOVEDA_NOMINA_OTROPAGOS where UUID = ?";
	private static String comboProveedores			 =  "select RECEPTOR_RFC, RECEPTOR_NOMBRE from BOVEDA_NOMINA group by RECEPTOR_RFC, RECEPTOR_NOMBRE ";
	
	
	private static String consultaFechaMinima = "select min(FECHA_FACTURA) from BOVEDA_NOMINA";
	
	private static String fechaNomina =
		    "SELECT MAX(FECHA_TRANS) AS FECHA FROM BOVEDA_NOMINA";

		public static String getUltimaFechaNomina(String esquema) {
		    return fechaNomina.replace("<<esquema>>", esquema);
		}

	
	
	public static String getDetalleBoveda(String esquema) {
		return detalleBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTotalRegistros(String esquema) {
		return totalRegistros.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getAltaBoveda(String esquema) {
		return altaBoveda.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaEmisorNomina(String esquema) {
		return altaEmisorNomina.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaReceptorNomina(String esquema) {
		return altaReceptorNomina.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaPercepciones(String esquema) {
		return altaPercepciones.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaPercepcionesDetalle(String esquema) {
		return altaPercepcionesDetalle.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaDeducciones(String esquema) {
		return altaDeducciones.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaDeduccionesDetalle(String esquema) {
		return altaDeduccionesDetalle.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getAltaOtroPago(String esquema) {
		return altaOtroPago.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaBoveda(String esquema) {
		return consultaBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEliminarBovedaNomina(String esquema) {
		return eliminarBovedaNomina.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminarBovedaNominaConceptos(String esquema) {
		return eliminarBovedaNominaConceptos.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminarBovedaNominaEmisor(String esquema) {
		return eliminarBovedaNominaEmisor.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminarBovedaNominaReceptor(String esquema) {
		return eliminarBovedaNominaReceptor.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminarBovedaNominaPercepciones(String esquema) {
		return eliminarBovedaNominaPercepciones.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminarBovedaNominaPercepcionesDetalle(String esquema) {
		return eliminarBovedaNominaPercepcionesDetalle.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminarBovedaNominaDeducciones(String esquema) {
		return eliminarBovedaNominaDeducciones.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminarBovedaNominaDeduccionesDetalle(String esquema) {
		return eliminarBovedaNominaDeduccionesDetalle.replaceAll("<<esquema>>", esquema);
	}
	public static String geteliminarBovedaNominaOtrosPago(String esquema) {
		return eliminarBovedaNominaOtrosPago.replaceAll("<<esquema>>", esquema);
	}

	public static String getEncontradosBoveda(String esquema) {
		return encontradosBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getAltaPercepcionesSeparacion(String esquema) {
		return altaPercepcionesSeparacion.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaPercepcionesJubilacion(String esquema) {
		return altaPercepcionesJubilacion.replaceAll("<<esquema>>", esquema);
	}
	public static String getReporteDetalle(String esquema) {
		return reporteDetalle.replaceAll("<<esquema>>", esquema);
	}
	public static String getObtenerDeducciones(String esquema) {
		return obtenerDeducciones.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getObtenerOtroPago(String esquema) {
		return obtenerOtroPago.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getComboProveedores(String esquema) {
		return comboProveedores.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getConsultaFechaMinima(String esquema) {
		return consultaFechaMinima.replaceAll("<<esquema>>", esquema);
	}

	
 }
