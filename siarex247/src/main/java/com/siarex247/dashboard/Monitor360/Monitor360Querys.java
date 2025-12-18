package com.siarex247.dashboard.Monitor360;

public class Monitor360Querys {

	private static String obtenerAnnios       = "select min(FECHA_FACTURA) from BOVEDA";
	private static String obtenerContribuyentes       = "select EMISOR_RFC, EMISOR_NOMBRE from DESCARGA_MASIVA_METADATA_TIMBRADO group by EMISOR_RFC, EMISOR_NOMBRE";
	private static String calcularUniverso    = "select EXISTE_BOVEDA, ESTATUS, count(*) from DESCARGA_MASIVA_METADATA_TIMBRADO where FECHA_EMISION between ? and ? and EFECTO_COMPROBANTE in (?, ?) <<tipoComprobantes>> group by EXISTE_BOVEDA, ESTATUS";
	// private static String calcularComplementos     = "SELECT sum(T.UNO) from ( select UUID_FACTURA, 1 as UNO  from BOVEDA A inner join COMPLEMENTARIA_BOVEDA C on A.UUID = C.UUID_FACTURA where A.FECHA_FACTURA between ? and ?  <<andCondicion>> group by UUID_FACTURA) as T";
	private static String calcularComplementos        = "select count(*) from DESCARGA_MASIVA_METADATA_TIMBRADO A inner join COMPLEMENTARIA_BOVEDA B on A.UUID = B.UUID_FACTURA where A.FECHA_EMISION between ? and ? and A.EFECTO_COMPROBANTE in (?, ?) <<andCondicion>>";
	//private static String calcularGrafica     = "select EMISOR_RFC, RECEPTOR_RFC, FECHA_EMISION, MONTO  from DESCARGA_MASIVA_METADATA_TIMBRADO where FECHA_EMISION between ? and ? and EFECTO_COMPROBANTE not in (?,?,?) and ESTATUS = ?";
	private static String calcularGraficaIngresos     = "select FECHA_FACTURA, TOTAL from BOVEDA_EMITIDOS where FECHA_FACTURA between ? and ? and TIPO_COMPROBANTE in (?,?) ";
	private static String calcularGraficaEgresos      = "select FECHA_FACTURA, TOTAL from BOVEDA where FECHA_FACTURA between ? and ? and TIPO_COMPROBANTE in (?,?,?,?)";
	private static String calcularIngresos    = "select sum(TOTAL), sum(SUB_TOTAL), sum(TOTAL_IMP_RETENIDO), sum(TOTAL_IMP_TRANSLADO) from BOVEDA_EMITIDOS where FECHA_FACTURA between ? and ? and TIPO_COMPROBANTE in (?,?) ";
	private static String calcularEgresos     = "select sum(TOTAL), sum(SUB_TOTAL), sum(TOTAL_IMP_RETENIDO), sum(TOTAL_IMP_TRANSLADO) from BOVEDA where FECHA_FACTURA between ? and ? and TIPO_COMPROBANTE in (?,?,?,?) ";
	private static String calcularListaNegra     = "select SUM(T.UNO) from (select count(*), 1 as UNO from DESCARGA_MASIVA_METADATA_TIMBRADO A inner join siarex_accesos.<<BASE_LISTA>> B on A.EMISOR_RFC = B.RFC where B.FECHA_TRASACCION between ? and ? and A.EFECTO_COMPROBANTE in (?, ?) and A.RECEPTOR_RFC = ?  and <<campoDisponible>> = ? <<AndCondicion>> group by A.EMISOR_RFC) as T";
	
	private static String detalleListaNegraMetadata   =  "select A.EMISOR_RFC, A.EMISOR_NOMBRE from DESCARGA_MASIVA_METADATA_TIMBRADO A inner join siarex_accesos.<<BASE_LISTA>> B on A.EMISOR_RFC = B.RFC where B.FECHA_TRASACCION between ? and ? and A.EFECTO_COMPROBANTE in (?, ?) and A.RECEPTOR_RFC = ? and <<campoDisponible>> = ?  and A.TIPO_MONEDA = ? group by A.EMISOR_RFC, A.EMISOR_NOMBRE";

	private static String detalleGraficaEgresos       = "select TIPO_COMPROBANTE AS tipo, MONTH(FECHA_FACTURA) AS mes, SUM(TOTAL) AS total from BOVEDA WHERE FECHA_FACTURA BETWEEN ? and ? and DES_TIPO_MONEDA = ? <<andCondicion>> group by TIPO_COMPROBANTE, MONTH(FECHA_FACTURA)";
	private static String detalleGraficaEgresosPagos  = "select month(FECHA_FACTURA) AS mes, sum(TOTAL) AS total from BOVEDA A inner join COMPLEMENTARIA_BOVEDA  B on A.UUID = B.UUID_FACTURA where A.FECHA_FACTURA BETWEEN ? and ? and A.DES_TIPO_MONEDA = ? <<andCondicion>> group by MONTH(FECHA_FACTURA)";
	private static String detalleGraficaEgresosNomina = "select month(FECHA_FACTURA) AS mes, sum(TOTAL) AS total from BOVEDA_NOMINA where FECHA_FACTURA BETWEEN ? and ? and MONEDA = ?  group by MONTH(FECHA_FACTURA)";


	public static String getObtenerAnnios(String esquema) {
		return obtenerAnnios.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getCalcularUniverso(String esquema, String tipoComprobante) {
		return calcularUniverso.replaceAll("<<esquema>>", esquema).replaceAll("<<tipoComprobantes>>", tipoComprobante);
	}
	
	public static String getCalcularComplementos(String esquema, String andCondicion) {
		return calcularComplementos.replaceAll("<<esquema>>", esquema).replaceAll("<<andCondicion>>", andCondicion);
	}
	
	

	public static String getCalcularGraficaIngresos(String esquema) {
		return calcularGraficaIngresos.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getCalcularGraficaEgresos(String esquema) {
		return calcularGraficaEgresos.replaceAll("<<esquema>>", esquema);
	}
	

	public static String getCalcularIngresos(String esquema) {
		return calcularIngresos.replaceAll("<<esquema>>", esquema);
	}

	public static String getCalcularEgresos(String esquema) {
		return calcularEgresos.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getObtenerContribuyentes(String esquema) {
		return obtenerContribuyentes.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getCalcularListaNegra(String esquema, String baseLista, String campoDisponible, String andCondicion) {
		return calcularListaNegra.replaceAll("<<esquema>>", esquema).replaceAll("<<BASE_LISTA>>", baseLista).replaceAll("<<campoDisponible>>", campoDisponible).replaceAll("<<AndCondicion>>", andCondicion);
	}
	
	public static String getDetalleListaNegraMetadata(String esquema, String baseLista, String campoDisponible) {
		return detalleListaNegraMetadata.replaceAll("<<esquema>>", esquema).replaceAll("<<BASE_LISTA>>", baseLista).replaceAll("<<campoDisponible>>", campoDisponible);
	}
	
	public static String getDetalleGraficaEgresos(String esquema, String andCondicion) {
		return detalleGraficaEgresos.replaceAll("<<esquema>>", esquema).replaceAll("<<andCondicion>>", andCondicion);
	}
	public static String getDetalleGraficaEgresosPagos(String esquema, String andCondicion) {
		return detalleGraficaEgresosPagos.replaceAll("<<esquema>>", esquema).replaceAll("<<andCondicion>>", andCondicion);
	}
	
	public static String getDetalleGraficaEgresosNomina(String esquema) {
		return detalleGraficaEgresosNomina.replaceAll("<<esquema>>", esquema);
	}
	
	
}
