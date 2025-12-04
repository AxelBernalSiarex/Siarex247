package com.siarex247.catalogos.Empleados;

public class EmpleadosQuerys {

	private static String detalle 	= "select ID_REGISTRO, ID_EMPLEADO, NOMBRE_COMPLETO, CORREO, ID_SUPERVIDOR, SHIPTO, PERMITIR_ALTA from EMPLEADOS";
	private static String consulta	= "select ID_REGISTRO, ID_EMPLEADO, NOMBRE_COMPLETO, CORREO, ID_SUPERVIDOR, SHIPTO, PERMITIR_ALTA from  EMPLEADOS where ID_REGISTRO = ?";
	private static String consultaXid	= "select ID_REGISTRO, ID_EMPLEADO, NOMBRE_COMPLETO, CORREO, ID_SUPERVIDOR, SHIPTO, PERMITIR_ALTA from  EMPLEADOS where ID_EMPLEADO = ?";
	private static String consultaXCorreo	= "select ID_REGISTRO, ID_EMPLEADO, NOMBRE_COMPLETO, CORREO, ID_SUPERVIDOR, SHIPTO, PERMITIR_ALTA from  EMPLEADOS where CORREO = ?";

	private static String guardar	= "insert into  EMPLEADOS (ID_EMPLEADO, NOMBRE_COMPLETO, CORREO, ID_SUPERVIDOR, SHIPTO, PERMITIR_ALTA, USUARIO_TRAN ) values (?,?,?,?,?,?,?)";
	private static String update	= "update EMPLEADOS set NOMBRE_COMPLETO = ?, CORREO = ?, ID_SUPERVIDOR = ?, SHIPTO = ?, PERMITIR_ALTA = ? where ID_REGISTRO = ?";
	private static String elimina	= "delete from EMPLEADOS where ID_REGISTRO = ?";
	private static String seleccionaEmpleado   =  "select ID_REGISTRO,ID_EMPLEADO, NOMBRE_COMPLETO, ID_PUESTO, CORREO, ID_SUPERVIDOR, ACCESO_SIAREX, CLAVE_ACCESO, USUARIO_TRAN, FECHA_TRANS from EMPLEADOS where ID_REGISTRO > 0 ";
	private static String buscarEmpleadoXshipTO =  "select ID_REGISTRO from EMPLEADOS where SHIPTO = ?";
	
	
	public static String getDetalle(String esquema) {
		return detalle.replaceAll("<<esquema>>", esquema);
	}

	public static String getConsultaPuesto(String esquema) {
		return consulta.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getConsultaXid(String esquema) {
		return consultaXid.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getGuardar(String esquema) {
		return guardar.replaceAll("<<esquema>>", esquema);
	}

	public static String getUpdate(String esquema) {
		return update.replaceAll("<<esquema>>", esquema);
	}

	public static String getElimina(String esquema) {
		return elimina.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getSeleccionaEmpleado(String esquema) {
		return seleccionaEmpleado.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getBuscarEmpleadoXshipTO(String esquema) {
		return buscarEmpleadoXshipTO.replaceAll("<<esquema>>", esquema);
	}

	public static String getConsultaXcorreo(String esquema) {
		return consultaXCorreo.replaceAll("<<esquema>>", esquema);
	}
	
	
}
