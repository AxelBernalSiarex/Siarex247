package com.siarex247.seguridad.Usuarios;

public class UsuariosQuerys {

	
	private static String detalleUsuarios = "select A.ID_REGISTRO, A.ID_USUARIO, A.NOMBRE_COMPLETO, A.CORREO, A.ID_PERFIL, B.DESCRIPCION, B.TIPO_PERFIL, A.ID_EMPLEADO  from USUARIOS A inner join PERFILES B on A.ID_PERFIL = B.CLAVEPERFIL where A.ESTATUS_REGISTRO = ? and B.NOMBRE_CORTO not in ('003') order by A.ID_USUARIO, A.NOMBRE_COMPLETO";
	private static String detalleUsuariosRH = "select A.ID_REGISTRO, A.ID_USUARIO, A.NOMBRE_COMPLETO, A.CORREO, A.ID_PERFIL, B.DESCRIPCION, B.TIPO_PERFIL  from USUARIOS A inner join PERFILES B on A.ID_PERFIL = B.CLAVEPERFIL where A.ESTATUS_REGISTRO = ? and B.TIPO_PERFIL = ?  order by A.ID_USUARIO, A.NOMBRE_COMPLETO";
	private static String consultaUsuario = "select A.ID_REGISTRO, A.ID_USUARIO, A.NOMBRE_COMPLETO, A.CORREO, A.ID_PERFIL, B.DESCRIPCION, A.ID_EMPLEADO  from USUARIOS A inner join PERFILES B on A.ID_PERFIL = B.CLAVEPERFIL where A.ID_REGISTRO = ?";
	private static String insertUsuarios  = "insert into USUARIOS (ID_USUARIO, ID_EMPLEADO, NOMBRE_COMPLETO, CORREO, ID_PERFIL, USUARIO_TRAN) values (?,?,?,?,?,?)";
	private static String updateUsuarios  = "update USUARIOS set NOMBRE_COMPLETO = ?, CORREO = ?, ID_PERFIL = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where ID_REGISTRO = ?";
	//private static String eliminaUsuarios  = "update USUARIOS set ESTATUS_REGISTRO = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where ID_REGISTRO = ?";
	//private static String eliminaUsuarios  = "update USUARIOS set ESTATUS_REGISTRO = ?, USUARIO_CAMBIO = ?, FECHA_CAMBIO = current_timestamp where ID_REGISTRO = ?";
	private static String eliminaUsuarios  = "delete from USUARIOS where ID_REGISTRO = ?";
	
	private static String informacionUsuario  = "select A.ID_REGISTRO, A.ID_USUARIO, A.NOMBRE_COMPLETO, A.CORREO, A.ID_PERFIL, B.DESCRIPCION, B.TIPO_PERFIL  from USUARIOS A inner join PERFILES B on A.ID_PERFIL = B.CLAVEPERFIL where A.ESTATUS_REGISTRO = ? and A.ID_EMPLEADO = ?";
	
	private static String datosUsuario = "select A.ID_REGISTRO, A.ID_USUARIO, A.ID_EMPLEADO, A.NOMBRE_COMPLETO, A.CORREO, A.ID_PERFIL, B.DESCRIPCION, A.ESTATUS_REGISTRO, B.NOMBRE_CORTO  from USUARIOS A inner join PERFILES B on A.ID_PERFIL = B.CLAVEPERFIL where A.ID_USUARIO = ? and A.ESTATUS_REGISTRO in ( ? , ?)";
	
	private static String informacionUsuarioProveedor  = "select A.ID_REGISTRO, A.ID_USUARIO, A.NOMBRE_COMPLETO, A.CORREO, A.ID_PERFIL, B.DESCRIPCION, B.TIPO_PERFIL, A.ESTATUS_REGISTRO  from USUARIOS A inner join PERFILES B on A.ID_PERFIL = B.CLAVEPERFIL where A.ID_EMPLEADO = ?";
	
	
	private static String eliminaUsuarioProveedor  = "delete from USUARIOS where ID_REGISTRO = ?";	
	
	public static String getInformacionUsuario(String esquema) {
		return informacionUsuario.replaceAll("<<esquema>>", esquema);
	}
	
	public static String getInformacionUsuarioProveedor(String esquema) {
		return informacionUsuarioProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	
	
	
	public static String getDatosUsuario(String esquema) {
		return datosUsuario.replaceAll("<<esquema>>", esquema);
	}
	
	
	public static String getDetalleUsuarios(String esquema) {
		return detalleUsuarios.replaceAll("<<esquema>>", esquema);
	}

	public static String getDetalleUsuariosRH(String esquema) {
		return detalleUsuariosRH.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getConsultaUsuario(String esquema) {
		return consultaUsuario.replaceAll("<<esquema>>", esquema);
	}

	
	public static String getInsertUsuarios(String esquema) {
		return insertUsuarios.replaceAll("<<esquema>>", esquema);
	}

	public static String getUpdateUsuarios(String esquema) {
		return updateUsuarios.replaceAll("<<esquema>>", esquema);
	}

	public static String getEliminaUsuarios(String esquema) {
		return eliminaUsuarios.replaceAll("<<esquema>>", esquema);
	}

	public static String getEliminaUsuarioProveedor(String esquema) {
		return eliminaUsuarioProveedor.replaceAll("<<esquema>>", esquema);
	}
	
	
}
