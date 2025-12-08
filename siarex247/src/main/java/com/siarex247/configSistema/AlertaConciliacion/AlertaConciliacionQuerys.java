package com.siarex247.configSistema.AlertaConciliacion;

public class AlertaConciliacionQuerys {

	private static String confProcesoOut = "select DIA_EJECUCION, SUBJECT, MENSAJE, DESTINATARIO_1, DESTINATARIO_2, DESTINATARIO_3, DESTINATARIO_4, DESTINATARIO_5, ADJUNTAR_ARCHIVO, ACTIVAR from  CONF_PROCESOS_ALERTAS where ID_PROCESO = ?";
	private static String updConfProcesoOut = "update  CONF_PROCESOS_ALERTAS set DIA_EJECUCION = ?, SUBJECT = ?, MENSAJE = ?, DESTINATARIO_1 = ?, DESTINATARIO_2 = ?, DESTINATARIO_3 = ?, DESTINATARIO_4 = ?, DESTINATARIO_5 = ?, ADJUNTAR_ARCHIVO = ?, ACTIVAR = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = CURRENT_TIMESTAMP where ID_PROCESO = ?";
	private static String insProcesoOut     = "insert into  CONF_PROCESOS_ALERTAS (ID_PROCESO, DIA_EJECUCION, SUBJECT, MENSAJE, DESTINATARIO_1, DESTINATARIO_2, DESTINATARIO_3, DESTINATARIO_4, DESTINATARIO_5, ADJUNTAR_ARCHIVO, ACTIVAR, USUARIO_TRAN) values (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	
	public static String getQueryConfProceso(String esquema) {
		return confProcesoOut.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getQueryUpdProceso(String esquema) {
		return updConfProcesoOut.replaceAll("<<esquema>>", esquema);
	}
	
	

	public static String getQueryInsProceso(String esquema) {
		return insProcesoOut.replaceAll("<<esquema>>", esquema);
	}
	
	
	
}
