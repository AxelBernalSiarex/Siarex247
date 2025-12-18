package com.siarex247.cumplimientoFiscal.Pedimentos;

public class PedimentosQuerys {

	
	private static String consultaPedimentos =  "select ID_REGISTRO, NUM_PEDIMENTO, CVE_PEDIMENTO, REGIMEN, DTA, IVA, IGI, PRV, IVAPRV, EFECTIVO, OTROS, TOTAL, BANCO, LINEA_CAPTURA, IMPORTE_PAGO, FECHA_PAGO, NUMERO_OPERACION, NUMERO_SAT, MEDIO_PRESENTACION, MEDIO_RECEPCION from PEDIMENTOS ";
	private static String altaPedimento      =  "insert into PEDIMENTOS (NUM_PEDIMENTO, CVE_PEDIMENTO, REGIMEN, DTA, IVA, IGI, PRV, IVAPRV, EFECTIVO, OTROS, TOTAL, BANCO, LINEA_CAPTURA, IMPORTE_PAGO, FECHA_PAGO, NUMERO_OPERACION, NUMERO_SAT, MEDIO_PRESENTACION, MEDIO_RECEPCION, USUARIO_TRAN) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

	public static String getConsultaPedimentos(String esquema) {
		return consultaPedimentos.replaceAll("<<esquema>>", esquema);
	}

	public static String getAltaPedimento(String esquema) {
		return altaPedimento.replaceAll("<<esquema>>", esquema);
	}
	
	
}
