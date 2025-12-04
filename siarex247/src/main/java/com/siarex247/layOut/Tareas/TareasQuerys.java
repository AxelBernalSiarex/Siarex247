package com.siarex247.layOut.Tareas;

public class TareasQuerys {

	
	private static String queryDetalleTareasPanta =  "select  CLAVE_TAREA, SUBJECT, CORREO_DE, CUERPO_MENSAJE, FECHA_TAREA,TIPO_ENVIO, ESTATUS, TIPO_TAREA from TAREAS where TIPO_TAREA in (?,?) order by CLAVE_TAREA desc";
	private static String queryEliminaTareas    =  "delete from TAREAS where CLAVE_TAREA = ?";
	private static String queryEliminaTareasDetalle    =  "delete from TAREAS_PROVEEDORES where CLAVE_TAREA = ?";
	private static String queryActualizaTareas    =  "update TAREAS set ESTATUS = ? where CLAVE_TAREA = ?";
	private static String queryInsertTarea =  "insert into TAREAS (SUBJECT, CORREO_DE, CUERPO_MENSAJE, FECHA_TAREA, NOMBRE_ARCHIVO, TIPO_ENVIO, ESTATUS, CLAVE_USUARIO, NUM_DIAS1, NUM_DIAS2, NOTIFICACION, TIPO_TAREA ) values (?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String queryInsertTareaProv =  "insert into TAREAS_PROVEEDORES (CLAVE_TAREA, CLAVE_PROVEEDOR) values (?,?)";
	private static String grabarTareaOrden = "insert into TAREAS_ORDENES (CLAVE_TAREA, NUMERO_ORDEN, CLAVE_PROVEEDOR, VENDOR_ID, RFC, TOTAL, TIPO_MONEDA, NOMBRE_ARCHIVO, ESTATUS, SHIPTO, ESTATUS_SHIPTO, LISTA_NEGRA) values (?,?,?,?,?,?,?,?, ?, ?, ?,?)";
	private static String actualizaOrden = "update ORDENES set NOMBRE_ARCHIVO = ?, CONCEPTO = ?, ASIGNAR_A = case WHEN ASIGNAR_A is null then ? else ASIGNAR_A end where FOLIO_EMPRESA = ?";
	private static String detalleTareasOrdenesOK =  "select A.CLAVE_REGISTRO, A.NUMERO_ORDEN, A.CLAVE_PROVEEDOR, A.VENDOR_ID, A.TOTAL, A.TIPO_MONEDA, A.NOMBRE_ARCHIVO, P.RAZON_SOCIAL, A.ESTATUS, A.ESTATUS_SHIPTO from TAREAS_ORDENES A inner join PROVEEDORES P on A.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR where A.CLAVE_TAREA = ?";
	private static String detalleTareasOrdenesNG =  "select A.CLAVE_REGISTRO, A.NUMERO_ORDEN, A.CLAVE_PROVEEDOR, A.VENDOR_ID, A.TOTAL, A.TIPO_MONEDA, A.NOMBRE_ARCHIVO, P.RAZON_SOCIAL, A.SHIPTO, A.ESTATUS, A.ESTATUS_SHIPTO, A.LISTA_NEGRA from TAREAS_ORDENES A left join PROVEEDORES P on A.CLAVE_PROVEEDOR = P.CLAVE_PROVEEDOR where A.CLAVE_TAREA = ?";
	
	
	// PROCESO
	private static String queryDetalleTareas    =  "select CLAVE_TAREA, SUBJECT, CORREO_DE, CUERPO_MENSAJE, FECHA_TAREA, NOMBRE_ARCHIVO, TIPO_ENVIO, ESTATUS, CLAVE_USUARIO, NUM_DIAS1, NUM_DIAS2, NOTIFICACION, TIPO_TAREA from TAREAS where ESTATUS in (?,?) ";
	private static String queryDetalleTareasPro =  "select A.CLAVE_PROVEEDOR, B.RFC, B.RAZON_SOCIAL, B.NOTIFICAR_ORDEN, B.EMAIL from TAREAS_PROVEEDORES A, PROVEEDORES B  where  A.CLAVE_TAREA = ? and A.CLAVE_PROVEEDOR = B.CLAVE_PROVEEDOR";
	private static String buscarHistoricoTarea = "select BANDERA from HISTORICO_TAREAS where CLAVE_TAREA = ?";
	private static String grabarHistoricoTarea = "insert into HISTORICO_TAREAS (CLAVE_TAREA, BANDERA, FECHA_TAREA) values (?,?,?)";
	private static String obtenerOrdXtareas = "select CLAVE_REGISTRO, NUMERO_ORDEN, CLAVE_PROVEEDOR, VENDOR_ID, RFC, TOTAL, TIPO_MONEDA, NOMBRE_ARCHIVO from TAREAS_ORDENES where CLAVE_TAREA = ? and CLAVE_PROVEEDOR = ? and ESTATUS = ?";
	private static String buscarOrdenCompra =  "select ASIGNAR_A from ORDENES where FOLIO_EMPRESA = ?";
	
	private static String mailUsuarioAcceso = "select CORREO, NOMBRE_COMPLETO, ID_SUPERVIDOR  from EMPLEADOS where  ID_REGISTRO = ?";
	private static String mailUsuarioAccesoSup = "select CORREO, NOMBRE_COMPLETO, ID_SUPERVIDOR  from EMPLEADOS where  ID_EMPLEADO = ?";
	
	

	public static String getDetalleTareasPanta(String esquema) {
		return queryDetalleTareasPanta.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getEliminaTareas(String esquema) {
		return queryEliminaTareas.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getEliminaTareasDetalle(String esquema) {
		return queryEliminaTareasDetalle.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getActualizaTareas(String esquema) {
		return queryActualizaTareas.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getInsertTarea(String esquema) {
		return queryInsertTarea.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryInsertTareaProv(String esquema) {
		return queryInsertTareaProv.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryGrabarTareaOrden(String esquema) {
		return grabarTareaOrden.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryActualizaOrden(String esquema) {
		return actualizaOrden.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryDetalleTareasOrdenesOK(String esquema) {
		return detalleTareasOrdenesOK.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryDetalleTareasOrdenesNG(String esquema) {
		return detalleTareasOrdenesNG.replaceAll("<<esquema>>", esquema);
	}	
	
	public static String getQueryDetalleTareas(String esquema) {
		return queryDetalleTareas.replaceAll("<<esquema>>", esquema).toUpperCase();
	}

	
	public static String getQueryDetalleTareasPro(String esquema) {
		return queryDetalleTareasPro.replaceAll("<<esquema>>", esquema).toUpperCase();
	}
	
	public static String getQueryActualizaTareas(String esquema) {
		return queryActualizaTareas.replaceAll("<<esquema>>", esquema).toUpperCase();
	}

	
	public static String getQueryBuscarHistoricoTarea(String esquema) {
		return buscarHistoricoTarea.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryGrabarHistoricoTarea(String esquema) {
		return grabarHistoricoTarea.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryObtenerOrdXtareas(String esquema) {
		return obtenerOrdXtareas.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryBuscarOrdenCompra(String esquema) {
		return buscarOrdenCompra.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getQueryMailUsuarioAcceso(String esquema) {
		return mailUsuarioAcceso.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryMailUsuarioAccesoSup(String esquema) {
		return mailUsuarioAccesoSup.replaceAll("<<esquema>>", esquema);
	}
}
