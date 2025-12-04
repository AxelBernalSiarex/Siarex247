package com.siarex247.cumplimientoFiscal.BovedaEmitidos;

public class BovedaEmitidosQuerys {

	
	private static String detalleBoveda =  "select ID_REGISTRO, UUID, SERIE, FOLIO,FECHA_FACTURA, FORMA_PAGO, METODO_PAGO, MONEDA, SUB_TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, TOTAL, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, TIPO_COMPROBANTE from BOVEDA_EMITIDOS";
	private static String totalRegistros =  "select count(*) from BOVEDA_EMITIDOS ";	
	private static String consultaBoveda =  "select ID_REGISTRO, UUID, SERIE, FOLIO,FECHA_FACTURA, FORMA_PAGO, METODO_PAGO, MONEDA, SUB_TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, TOTAL, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE, TIPO_COMPROBANTE from BOVEDA_EMITIDOS where ID_REGISTRO = ?";
	private static String altaBoveda 	=  "insert into BOVEDA_EMITIDOS (UUID, VERSION_XML, SERIE, FOLIO, FECHA_FACTURA, FORMA_PAGO, CONDICIONES_PAGO, MONEDA, TIPO_CAMBIO, TIPO_COMPROBANTE, METODO_PAGO, LUGAR_EXPEDICION, SUB_TOTAL, TOTAL, DESCUENTO, TOTAL_IMP_RETENIDO, TOTAL_IMP_TRANSLADO, EMISOR_RFC, EMISOR_NOMBRE, EMISOR_REGIMEN, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_RESIDENCIA, RECEPTOR_DOMICILIO, RECEPTOR_REGIMEN_FISCAL, RECEPTOR_USO, COMPLEMENTO_VERSION, FECHA_TIMBRADO, RFC_PROVCERT, FECHA_PAGO, USUARIO_TRAN) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String altaBovedaSAT	=  "insert into BOVEDA_SAT (UUID, NO_CERTIFICADO, SELLO, CERTIFICADO, NO_CERTIFICADO_SAT, SELLO_CFD, SELLO_SAT) values (?,?,?,?,?,?,?)";
	private static String altaBovedaImpuestos =  "insert into BOVEDA_IMPUESTOS (UUID, TIPO_IMPUESTO, BASE, IMPUESTO, TIPO_FACTOR, TASA_CUOTA, IMPORTE) values (?,?,?,?,?,?,?)";
	private static String altaBovedaConceptos =  "insert into BOVEDA_CONCEPTOS (UUID, CLAVE_PRODUCTO_SERVICIO, NO_IDENTIFICACION, CANTIDAD, CLAVE_UNIDAD, UNIDAD, DESCRIPCION, VALOR_UNITARIO, IMPORTE, DESCUENTO, OBJETO_IMPUESTO) values (?,?,?,?,?,?,?,?,?,?,?)";
	private static String altaBovedaConceptosImp =  "insert into BOVEDA_CONCEPTOS_IMPUESTOS (UUID, ID_CONCEPTO, TIPO_IMPUESTO, BASE, IMPUESTO, TIPO_FACTOR, TASA_CUOTA, IMPORTE) values (?,?,?,?,?,?,?,?)";
	private static String altaBovedaComplemento =  "insert into BOVEDA_COMPLEMENTOS (UUID, VERSION_COMPLEMENTO, TOTAL_TRASLADO_BASE_IVA_16, TOTAL_TRASLADO_IMPUESTO_16, TOTAL_TRASLADO_BASE_IVA_8, TOTAL_TRASLADO_IMPUESTO_8, TOTAL_TRASLADO_BASE_IVA_0, TOTAL_TRASLADO_IMPUESTO_0, TOTAL_TRASLADO_BASE_IVA_EXENTO, MONTO_TOTAL_PAGOS) values (?,?,?,?,?,?,?,?,?,?)";
	private static String altaBovedaComplementoPagos =  "insert into BOVEDA_COMPLEMENTOS_PAGOS (UUID, FECHA_PAGO, FORMA_PAGO, MONEDA_PAGO, TIPO_CAMBIO, MONTO, CTA_BENEFICIARIO, NOMBRE_BANCO_EXT, NUM_OPERACION, RFC_EMISOR_CTABENEFICIARIO, RFC_EMISOR_CTAORDINARIO, CTA_ORDENANTE, TIPO_CADENA_PAGO) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String altaBovedaComplementoPagosDoctoR =  "insert into BOVEDA_COMPLEMENTOS_PAGOS_DOCTORELACIONADO (UUID, ID_PAGO, ID_DOCUMENTO, SERIE, EQUIVALENCIA_DR, FOLIO, IMPORTE_PAGADO, IMPORTE_SALDO_ANTERIOR, IMPORTE_SALDO_INSOLUTO, MONEDA_DR, NUM_PARCIALIDAD, OBJETO_IMPUESTO_DR) values (?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String altaBovedaComplementoPagosImpuestos =  "insert into BOVEDA_COMPLEMENTOS_PAGOS_IMPUESTOS (UUID, ID_PAGO, ID_DOCUMENTO, TIPO_IMPUESTO, BASE_DR, IMPUESTO_DR, TIPO_FACTOR_DR, TASA_CUOTA_DR, IMPORTE_DR) values (?,?,?,?,?,?,?,?,?)";

	
	private static String eliminaBoveda =  "delete from BOVEDA_EMITIDOS where UUID = ?";
	private static String eliminaBovedaSAT =  "delete from BOVEDA_SAT where UUID = ?";
	private static String eliminaBovedaImpuestos =  "delete from BOVEDA_IMPUESTOS where UUID = ?";
	private static String eliminaBovedaConceptos =  "delete from BOVEDA_CONCEPTOS where UUID = ?";
	//private static String eliminaBoveda =  "delete from BOVEDA_EMITIDOS where UUID = ?";
	
	private static String encontradosBoveda			=  "update DESCARGA_MASIVA_METADATA_TIMBRADO set EXISTE_BOVEDA = ? where EXISTE_BOVEDA = ? and UUID in (select UUID from BOVEDA_EMITIDOS)";
	private static String comboProveedores			 =  "select RECEPTOR_RFC, RECEPTOR_NOMBRE from BOVEDA_EMITIDOS group by RECEPTOR_RFC, RECEPTOR_NOMBRE ";
	private static String consultaBovedaUUID 		 =  "select UUID from BOVEDA_EMITIDOS where UUID = ?";
	
	private static String consultaFechaMinima = "select min(FECHA_FACTURA) from BOVEDA_EMITIDOS";
	
	public static String getDetalleBoveda(String esquema) {
		return detalleBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTotalRegistros(String esquema) {
		return totalRegistros.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getConsultaBoveda(String esquema) {
		return consultaBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getAltaBoveda(String esquema) {
		return altaBoveda.replaceAll("<<esquema>>", esquema);
	}

	public static String getAltaBovedaSAT(String esquema) {
		return altaBovedaSAT.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getAltaBovedaImpuestos(String esquema) {
		return altaBovedaImpuestos.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaBovedaConceptos(String esquema) {
		return altaBovedaConceptos.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminaBoveda(String esquema) {
		return eliminaBoveda.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminaBovedaSAT(String esquema) {
		return eliminaBovedaSAT.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminaBovedaImpuestos(String esquema) {
		return eliminaBovedaImpuestos.replaceAll("<<esquema>>", esquema);
	}
	public static String getEliminaBovedaConceptos(String esquema) {
		return eliminaBovedaConceptos.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaBovedaConceptosImp(String esquema) {
		return altaBovedaConceptosImp.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getAltaBovedaComplemento(String esquema) {
		return altaBovedaComplemento.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaBovedaComplementoPagos(String esquema) {
		return altaBovedaComplementoPagos.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaBovedaComplementoPagosDoctoR(String esquema) {
		return altaBovedaComplementoPagosDoctoR.replaceAll("<<esquema>>", esquema);
	}
	public static String getAltaBovedaComplementoPagosImpuestos(String esquema) {
		return altaBovedaComplementoPagosImpuestos.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEncontradosBoveda(String esquema) {
		return encontradosBoveda.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getComboProveedores(String esquema) {
		return comboProveedores.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaBovedaUUID(String esquema) {
		return consultaBovedaUUID.replaceAll("<<esquema>>", esquema);
	}


	
	public static String getConsultaFechaMinima(String esquema) {
		return consultaFechaMinima.replaceAll("<<esquema>>", esquema);
	}
	
 }
