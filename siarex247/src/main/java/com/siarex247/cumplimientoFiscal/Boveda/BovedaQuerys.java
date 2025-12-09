package com.siarex247.cumplimientoFiscal.Boveda;

public class BovedaQuerys {

	private static String consultaBoveda =  "select ID_REGISTRO, UUID, SERIE, FOLIO,FECHA_FACTURA, FORMA_PAGO, METODO_PAGO, TIPO_MONEDA, DES_TIPO_MONEDA, SUB_TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, TOTAL, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RETENCION_IVA, TRANSLADO_IVA, ISR_RET, TRANSLADOS_IEPS, NOMBRE_XML, TIPO_COMPROBANTE from BOVEDA ";
	private static String totalRegistro  =  "select count(*) from BOVEDA ";

	private static String altaBoveda =  "insert into BOVEDA (UUID, SERIE, FOLIO,FECHA_FACTURA, FORMA_PAGO, METODO_PAGO, TIPO_MONEDA, DES_TIPO_MONEDA, SUB_TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, TOTAL, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RETENCION_IVA, TRANSLADO_IVA, ISR_RET, TRANSLADOS_IEPS, NOMBRE_XML, TIPO_COMPROBANTE, FECHA_PAGO, FECHA_TIMBRADO ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String encontradosBoveda		=  "update DESCARGA_MASIVA_METADATA_TIMBRADO set EXISTE_BOVEDA = ? where EXISTE_BOVEDA = ? and UUID in (select UUID from BOVEDA)";
	private static String consultaBovedaRegistro =  "select ID_REGISTRO, UUID, SERIE, FOLIO,FECHA_FACTURA, FORMA_PAGO, METODO_PAGO, TIPO_MONEDA, DES_TIPO_MONEDA, SUB_TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, TOTAL, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RETENCION_IVA, TRANSLADO_IVA, ISR_RET, TRANSLADOS_IEPS, NOMBRE_XML, TIPO_COMPROBANTE from BOVEDA where ID_REGISTRO = ?";
	private static String consultaBovedaUUID =  "select ID_REGISTRO, UUID, SERIE, FOLIO,FECHA_FACTURA, FORMA_PAGO, METODO_PAGO, TIPO_MONEDA, DES_TIPO_MONEDA, SUB_TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, TOTAL, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, RETENCION_IVA, TRANSLADO_IVA, ISR_RET, TRANSLADOS_IEPS, NOMBRE_XML, TIPO_COMPROBANTE from BOVEDA where UUID = ?";	
	private static String eliminaBoveda =  "delete from BOVEDA where UUID = ?";
	private static String buscaUUID =  "select UUID from BOVEDA where ID_REGISTRO = ?";
	private static String datosVincular =  "select ID_REGISTRO, UUID, NOMBRE_XML from BOVEDA where TIPO_COMPROBANTE = ?";
	private static String escribeBitacora =  "insert into CONSOLA_BOVEDA (UUID, ESTATUS, IDEN, MENSAJE) values (?,?,?,?)";
	private static String datosConsola =  "  select UUID, ESTATUS, MENSAJE from CONSOLA_BOVEDA where IDEN = ? order by ID_REGISTRO desc";
	private static String totalProcesado =  " select count(*) from CONSOLA_BOVEDA where IDEN = ? and ESTATUS = ? group by ESTATUS ";
	
	private static String totalVincular =  "select count(*) from BOVEDA where TIPO_COMPROBANTE = ?";
	//private static String buscarBoveda =  "select ID_REGISTRO, SUB_TOTAL, FECHA_FACTURA from BOVEDA where UUID = ?";
	private static String buscarBoveda =  "select A.ID_REGISTRO, A.SUB_TOTAL, A.FECHA_FACTURA, A.EMISOR_RFC, A.DES_TIPO_MONEDA, A.TOTAL, B.RFC, B.FECHA_PAGO, B.TIPO_MONEDA, B.TOTAL  from BOVEDA A left join HISTORIAL_PAGOS B on A.UUID = B.UUID_FACTURA where A.UUID = ?";
	private static String guardarComplementariaBoveda =  "insert into COMPLEMENTARIA_BOVEDA (UUID_COMPLEMENTO,UUID_FACTURA, TOTAL_FACTURA, ESTATUS_COMPLEMENTO, FECHA_PAGO, FECHA_TIMBRADO, NOMBRE_XML, NOMBRE_PDF, TIPO_COMPROBANTE, FORMA_PAGO, ESTATUS, USUARIO_TRASACCION ) values (?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String actualizarConciliacion =  "update BOVEDA set CONCILIACION = ? where UUID = ?";
	private static String actualizarTotalComplemento =  "update COMPLEMENTARIA_BOVEDA set TOTAL_COMPLEMENTO = ?, ESTATUS_CONCILIACION = ? where UUID_COMPLEMENTO = ?";
	private static String eliminarComplementoBoveda =  "delete from COMPLEMENTARIA_BOVEDA where UUID_COMPLEMENTO = ?";
	private static String comboProveedores	 =  "select EMISOR_RFC, EMISOR_NOMBRE from BOVEDA group by EMISOR_RFC, EMISOR_NOMBRE ";
	
	
	private static String consultaFechaMinima = "select min(FECHA_FACTURA) from BOVEDA";
	private static String buscarComplementoUUID =  "select UUID_COMPLEMENTO, ESTATUS from COMPLEMENTARIA_BOVEDA where UUID_FACTURA = ?";
	private static String actualizarHistorialPagos =  "update HISTORIAL_PAGOS set ESTATUS = ?, CODIGO_ERROR = ?, UUID_COMPLEMENTO = ? where UUID_FACTURA = ?";
	
	private static String fechaboveda =  "SELECT MAX(FECHA_TRANS) AS FECHA FROM BOVEDA";
	
	public static String getUltimaFechaTrans(String esquema) {
		return fechaboveda.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getBuscarBoveda(String esquema) {
		return buscarBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTotalRegistro(String esquema) {
		return totalRegistro.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaBoveda(String esquema) {
		return consultaBoveda.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getAltaBoveda(String esquema) {
		return altaBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getEncontradosBoveda(String esquema) {
		return encontradosBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getConsultaBovedaRegistro(String esquema) {
		return consultaBovedaRegistro.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getConsultaBovedaUUID(String esquema) {
		return consultaBovedaUUID.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getEliminaBoveda(String esquema) {
		return eliminaBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getBuscaUUID(String esquema) {
		return buscaUUID.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDatosVincular(String esquema) {
		return datosVincular.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getEscribeBitacora(String esquema) {
		return escribeBitacora.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getDatosConsola(String esquema) {
		return datosConsola.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getTotalVincular(String esquema) {
		return totalVincular.replaceAll("<<esquema>>", esquema);
	}

	public static String getTotalProcesado(String esquema) {
		return totalProcesado.replaceAll("<<esquema>>", esquema);
	}

	public static String getGuardarComplementariaBoveda(String esquema) {
		return guardarComplementariaBoveda.replaceAll("<<esquema>>", esquema);
	}

	public static String getActualizarConciliacion(String esquema) {
		return actualizarConciliacion.replaceAll("<<esquema>>", esquema);
	}

	public static String getActualizarTotalComplemento(String esquema) {
		return actualizarTotalComplemento.replaceAll("<<esquema>>", esquema);
	}

	public static String getEliminarComplementoBoveda(String esquema) {
		return eliminarComplementoBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getComboProveedores(String esquema) {
		return comboProveedores.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getConsultaFechaMinima(String esquema) {
		return consultaFechaMinima.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getBuscarComplementoUUID(String esquema) {
		return buscarComplementoUUID.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getActualizarHistorialPagos(String esquema) {
		return actualizarHistorialPagos.replaceAll("<<esquema>>", esquema);
	}
	
}
