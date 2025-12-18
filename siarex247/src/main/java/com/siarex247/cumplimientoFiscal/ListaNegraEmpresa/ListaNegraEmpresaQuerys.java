package com.siarex247.cumplimientoFiscal.ListaNegraEmpresa;

public class ListaNegraEmpresaQuerys {

	
	private static String proveedoresEmitidos    =  "select RECEPTOR_RFC from BOVEDA_EMITIDOS group by RECEPTOR_RFC";
	private static String proveedoresRecibidos   =  "select EMISOR_RFC from BOVEDA group by EMISOR_RFC";

	// pantalla de lista negra SAT
	private static String detalleListaNegra   =  "select RFC, RAZON_SOCIAL,SUPUESTO, NOMBRE_ARTICULO, ";
	private static String totalRegistros   =  "select count(*) ";
	private static String consultaColumnas    =  "select FECHAALTA from HISTORICO_PROCESOS where TIPO_PROCESO = 'NEG' and year(FECHAALTA) = ? order by FECHAALTA";

	private static String listaRFC 				=  "select A.RFC,";
	
	public static String getConsultaColumnas(String esquema) {
		return consultaColumnas.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDetalleListaNegra(String esquema) {
		return detalleListaNegra.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getListaRFC(String esquema) {
		return listaRFC.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getTotalRegistros(String esquema) {
		return totalRegistros.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getProveedoresEmitidos(String esquema) {
		return proveedoresEmitidos.replaceAll("<<esquema>>", esquema);
	}
	public static String getProveedoresRecibidos(String esquema) {
		return proveedoresRecibidos.replaceAll("<<esquema>>", esquema);
	}
	
	
}
