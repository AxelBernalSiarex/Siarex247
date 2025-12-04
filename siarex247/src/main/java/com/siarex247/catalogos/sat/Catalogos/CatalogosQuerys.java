package com.siarex247.catalogos.sat.Catalogos;

public class CatalogosQuerys {

	
	private static String validarClaveProducto  = "select DESCRIPCION from CAT_CLAVE_PRODUCTO_SERVICIO_SAT where CLAVE_PRODUCTO = ?";
	private static String consultarEstado	   = "select NOMBRE_ESTADO from CAT_ESTADO_SAT where CLAVE_ESTADO = ?";
	private static String consultarCiudad	   = "select DESCRIPCION from CAT_MUNICIPIO_SAT where CLAVE_ESTADO = ? and CLAVE_MUNICIPIO = ?";
	private static String consultarRegimen	   = "select DESCRIPCION from CAT_REGIMEN_FISCAL_SAT where CLAVE_REGIMEN = ?";
	private static String consultarRegimenDescripcion   = "select CLAVE_REGIMEN from CAT_REGIMEN_FISCAL_SAT where DESCRIPCION = ?";
	
	private static String consultarExportacion = "select DESCRIPCION from CAT_EXPORTACION_SAT where CLAVE_EXPORTACION = ?";
	private static String consultarMetodoPago = "select DESCRIPCION from CAT_METODO_PAGO_SAT where CLAVE_METODO = ?";
	private static String consultarUsoCFDI = "select DESCRIPCION from CAT_USO_CFDI_SAT where CLAVE_USO = ?";
	private static String consultarFormaPago = "select DESCRIPCION from CAT_FORMA_PAGO_SAT where CLAVE_FORMA = ?";
	
	private static String consultarUnidad	   = "select NOMBRE from CAT_UNIDAD_SAT where CLAVE_UNIDAD = ?";
	private static String consultarTasaCuota   = "select VALOR_MAXIMO from CAT_TASA_CUOTA_SAT where ID_REGISTRO = ?";
	
	
	private static String detalleMonedas  = "select CLAVE_MONEDA, DESCRIPCION from CAT_MONEDA_PAGO_SAT";
	private static String detalleFormaPago  = "select CLAVE_FORMA, DESCRIPCION from CAT_FORMA_PAGO_SAT";
	private static String detalleTipoRelacion  = "select CLAVE_TIPO_RELACION, DESCRIPCION from CAT_TIPO_RELACION_SAT";
	private static String detalleCodigoPostal  = "select CODIGO_POSTAL, ESTADO from CAT_CODIGO_POSTAL_SAT";
	private static String detalleMetodoPago  = "select CLAVE_METODO, DESCRIPCION from CAT_METODO_PAGO_SAT";
	private static String detalleRegimenFiscal  = "select CLAVE_REGIMEN, DESCRIPCION from CAT_REGIMEN_FISCAL_SAT";
	private static String detalleUsoCFDI  = "select CLAVE_USO, DESCRIPCION from CAT_USO_CFDI_SAT";
	private static String detalleProductoServicio  = "select CLAVE_PRODUCTO, DESCRIPCION from CAT_CLAVE_PRODUCTO_SERVICIO_SAT";
	private static String detalleTipoNomina  = "select CLAVE_TIPO, DESCRIPCION from CAT_TIPO_PERCEPCION_NOMINA_SAT";
	private static String detalleTipoDeducciones = "select CLAVE_TIPO, DESCRIPCION from CAT_TIPO_DEDUCCIONES_NOMINA_SAT";
	private static String detalleOtroPagos		 = "select CLAVE_TIPO, DESCRIPCION from CAT_TIPO_OTROPAGO_NOMINA_SAT";
	private static String detalleExportacion  = "select CLAVE_EXPORTACION, DESCRIPCION from CAT_EXPORTACION_SAT";
	
	
	private static String detalleTipoComprobante  = "select CLAVE_TIPO, DESCRIPCION from CAT_TIPO_COMPROBANTE_SAT";
	
	
	public static String getValidarClaveProducto(String esquema) {
		return validarClaveProducto.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getConsultarEstado(String esquema) {
		return consultarEstado.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarCiudad(String esquema) {
		return consultarCiudad.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarUnidad(String esquema) {
		return consultarUnidad.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarTasaCuota(String esquema) {
		return consultarTasaCuota.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleMonedas(String esquema) {
		return detalleMonedas.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDetalleFormaPago(String esquema) {
		return detalleFormaPago.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleTipoRelacion(String esquema) {
		return detalleTipoRelacion.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleCodigoPostal(String esquema) {
		return detalleCodigoPostal.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleMetodoPago(String esquema) {
		return detalleMetodoPago.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDetalleRegimenFiscal(String esquema) {
		return detalleRegimenFiscal.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDetalleUsoCFDI(String esquema) {
		return detalleUsoCFDI.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getDetalleProductoServicio(String esquema) {
		return detalleProductoServicio.replaceAll("<<esquema>>", esquema);
	}

	public static String getConsultarRegimen(String esquema) {
		return consultarRegimen.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarExportacion(String esquema) {
		return consultarExportacion.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarMetodoPago(String esquema) {
		return consultarMetodoPago.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarUsoCFDI(String esquema) {
		return consultarUsoCFDI.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarFormaPago(String esquema) {
		return consultarFormaPago.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleTipoNomina(String esquema) {
		return detalleTipoNomina.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleTipoDeducciones(String esquema) {
		return detalleTipoDeducciones.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleOtroPagos(String esquema) {
		return detalleOtroPagos.replaceAll("<<esquema>>", esquema);
	}

	public static String getDetalleTipoComprobante(String esquema) {
		return detalleTipoComprobante.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultarRegimenDescripcion(String esquema) {
		return consultarRegimenDescripcion.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getDetalleExportacion(String esquema) {
		return detalleExportacion.replaceAll("<<esquema>>", esquema);
	}
	
	
	
}
