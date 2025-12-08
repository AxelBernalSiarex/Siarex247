package com.siarex247.layOut.OrdenesCompra;

public class OrdenesCompraQuerys {

	private static String detalleHistoricoPerfil = "select CLAVE_HISTORICO, NOMBRE_ARCHIVO, TOTAL_REGISTROS, TIPO_CARGA, REGISTROS_OK, REGISTROS_NG, ESTATUS, CLAVE_USUARIO, FECHAALTA from HISTORICO_CARGAS where CLAVE_USUARIO = ? limit 20";
	private static String detalleHistorico = "select CLAVE_HISTORICO, NOMBRE_ARCHIVO, TOTAL_REGISTROS, TIPO_CARGA, REGISTROS_OK, REGISTROS_NG, ESTATUS, CLAVE_USUARIO, FECHAALTA from HISTORICO_CARGAS limit 20";
	private static String obtenerNombreCarga  = "select NOMBRE_ARCHIVO, FECHA_PAGO from HISTORICO_CARGAS where CLAVE_HISTORICO = ?";
	private static String historialCargas  = "insert into HISTORICO_CARGAS (NOMBRE_ARCHIVO, TOTAL_REGISTROS, TIPO_CARGA, REGISTROS_OK, REGISTROS_NG, CLAVE_USUARIO, ESTATUS, FECHA_PAGO) values (?,?,?,?,?,?,?,?)";
	private static String actualizaCargas  = "update HISTORICO_CARGAS set TOTAL_REGISTROS = ?, REGISTROS_OK = ?, REGISTROS_NG = ?, CLAVE_USUARIO = ?, ESTATUS = ? where CLAVE_HISTORICO = ?";
	private static String grabaHistorialDetallada = "insert into DETALLE_HISTORICO (CLAVE_HISTORICO, FOLIO_EMPRESA, TIPO_REGISTRO, DESCRIPCION_ERROR, VALOR_CARGA) values (?,?,?,?,?)";
	
	
	// querys del proceso de ordenes
	private static String grabarOrden = "insert into ORDENES (FOLIO_EMPRESA,CONCEPTO, TIPO_MONEDA, MONTO, ESTATUS_PAGO, CLAVE_PROVEEDOR, SERVICIO_PRODUCTO, USUARIO, FECHAULTMOV, ASIGNAR_A, NUMERO_REQUISICION, USO_CFDI, CLAVE_PRODUCTO_SERVICIO, NUMERO_CUENTA_COSTO_PROVEEDOR, CENTRO_COSTOS_PROVEEDOR, NOMBRE_ARCHIVO  ) values (?,?,?,?,?,?,?,?,current_timestamp, ?, ?, ?, ?, ?, ?, ?)";
	private static String grabarOrdenServRecibido = "insert into ORDENES (FOLIO_EMPRESA,CONCEPTO, TIPO_MONEDA, MONTO, ESTATUS_PAGO, CLAVE_PROVEEDOR, SERVICIO_PRODUCTO, USUARIO, FECHAULTMOV, ASIGNAR_A, NUMERO_REQUISICION, FECHA_RECIBO,USO_CFDI, CLAVE_PRODUCTO_SERVICIO  ) values (?,?,?,?,?,?,?,?,current_timestamp, ?, ?, current_timestamp, ?, ?)";
	private static String infoProveedorRFC = "select RFC, EMAIL, TIPO_CONFIRMACION, RAZON_SOCIAL, CLAVE_PROVEEDOR from PROVEEDORES where upper(RFC) = upper(?)";
	private static String infoProveedorVendor = "select RFC, EMAIL, TIPO_CONFIRMACION, RAZON_SOCIAL, CLAVE_PROVEEDOR from PROVEEDORES where NUMERO_PROVEEDOR = ?";
	private static String historicoDetallado = "select CLAVE_REGISTRO, FOLIO_EMPRESA, DESCRIPCION_ERROR, TIPO_REGISTRO, VALOR_CARGA from DETALLE_HISTORICO where CLAVE_HISTORICO = ?";
	
	
	public static String getQueryDetalleHistorico(String esquema) {
		return detalleHistorico.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getObtenerNombreCarga(String esquema) {
		return obtenerNombreCarga.replaceAll("<<esquema>>", esquema);
	}
	
	
	
	public static String getDetalleHistoricoPerfil(String esquema) {
		return detalleHistoricoPerfil.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryHistorialCargas(String esquema) {
		return historialCargas.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryActualizaCargas(String esquema) {
		return actualizaCargas.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryGrabaHistorialDetallada(String esquema) {
		return grabaHistorialDetallada.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryGrabarOrden(String esquema) {
		return grabarOrden.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryGrabarOrdenServRecibido(String esquema) {
		return grabarOrdenServRecibido.replaceAll("<<esquema>>", esquema);		
	}
	
	
	public static String getQueryInfoProveedorRFC(String esquema) {
		return infoProveedorRFC.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInfoProveedorVendor(String esquema) {
		return infoProveedorVendor.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryHistoricoDetallado(String esquema) {
		return historicoDetallado.replaceAll("<<esquema>>", esquema);
	}
	
	
}
