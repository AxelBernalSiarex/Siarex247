package com.siarex247.seguridad.Usuarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class UsuariosBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");

	
	public ArrayList<UsuariosForm> detalleUsuarios (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<UsuariosForm> listaUsuarios = new ArrayList<UsuariosForm>();
		UsuariosForm usuariosForm = new UsuariosForm();
		try {
			stmt = con.prepareStatement(UsuariosQuerys.getDetalleUsuarios(esquema));
			stmt.setString(1, "A");
			rs  = stmt.executeQuery();
			String tipoPerfil = "";
			while (rs.next()) {
				tipoPerfil = Utils.noNulo(rs.getString(7));
				if (!"ADM".equalsIgnoreCase(tipoPerfil)) {
					usuariosForm.setIdRegistro(rs.getInt(1));
					usuariosForm.setIdUsuario(Utils.noNuloNormal(rs.getString(2)));
					usuariosForm.setNombreCompleto(Utils.noNulo(rs.getString(3)));
					usuariosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
					usuariosForm.setIdPerfil(rs.getInt(5));
					usuariosForm.setDesPerfil(Utils.noNulo(rs.getString(6)));
					usuariosForm.setTipoPerfil(tipoPerfil);
					usuariosForm.setIdEmpleado(Utils.noNulo(rs.getString(8)));
					listaUsuarios.add(usuariosForm);
					usuariosForm = new UsuariosForm();
				}
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaUsuarios;
	}
	
	
	public UsuariosForm consultaUsuarios (Connection con, String esquema, int idRegistro){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UsuariosForm usuariosForm = new UsuariosForm();
		try {
			stmt = con.prepareStatement(UsuariosQuerys.getConsultaUsuario(esquema));
			stmt.setInt(1, idRegistro);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				usuariosForm.setIdRegistro(rs.getInt(1));
				usuariosForm.setIdUsuario(Utils.noNuloNormal(rs.getString(2)));
				usuariosForm.setNombreCompleto(Utils.noNulo(rs.getString(3)));
				usuariosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
				usuariosForm.setIdPerfil(rs.getInt(5));
				usuariosForm.setDesPerfil(Utils.noNulo(rs.getString(6)));
				usuariosForm.setIdEmpleado(Utils.noNulo(rs.getString(7)));
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return usuariosForm;
	}
	
	
	public int altaUsuarios (Connection con, String esquema, String idUsuario, String idEmpleado, String nombreCompleto, String correo, int idPerfil,  String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(UsuariosQuerys.getInsertUsuarios(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, idUsuario);
			stmt.setString(2, idEmpleado);
			stmt.setString(3, nombreCompleto);
			stmt.setString(4, correo);
			stmt.setInt(5, idPerfil);
			stmt.setString(6, usuarioHTTP);
			int cant = stmt.executeUpdate();
			if(cant > 0){
				rs = stmt.getGeneratedKeys();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
			}
			logger.info("El usuario "+usuarioHTTP + " ha guardado el usuario de acceso "+idUsuario);
		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return resultado;
	}
	
	
	
	public int updateUsuarios (Connection con, String esquema, int idRegistro, String nombreCompleto, String correo, int idPerfil,  String usuarioHTTP){
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(UsuariosQuerys.getUpdateUsuarios(esquema));
			stmt.setString(1, nombreCompleto);
			stmt.setString(2, correo);
			stmt.setInt(3, idPerfil);
			stmt.setString(4, usuarioHTTP);
			stmt.setInt(5, idRegistro);
			resultado = stmt.executeUpdate();
			logger.info("El usuario "+usuarioHTTP + " ha actualizado el usuario de acceso "+idRegistro);
		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return resultado;
	}
	
	
		
	public int deleteUsuarios (Connection con, String esquema, int idRegistro, String estatus, String usuarioHTTP){
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(UsuariosQuerys.getEliminaUsuarios(esquema));
			// stmt.setString(1, estatus);
			// stmt.setString(2, usuarioHTTP);
			stmt.setInt(1, idRegistro);
			resultado = stmt.executeUpdate();
			logger.info("El usuario "+usuarioHTTP + " ha eliminado el usuario de acceso "+idRegistro);
		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return resultado;
	}
	
	
	public int deleteUsuarioProveedor (Connection con, String esquema, int idRegistro){
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(UsuariosQuerys.getEliminaUsuarioProveedor(esquema));
			stmt.setInt(1, idRegistro);
			resultado = stmt.executeUpdate();
		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return resultado;
	}
	
	public UsuariosForm datosUsuario (Connection con, String esquema, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UsuariosForm usuariosForm = new UsuariosForm();
		try {
			
			stmt = con.prepareStatement(UsuariosQuerys.getDatosUsuario(esquema));
			stmt.setString(1, usuarioHTTP);
			stmt.setString(2, "A");
			stmt.setString(3, "F");
			
			// logger.info("stmtUsuario===>"+stmt);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				usuariosForm.setIdRegistro(rs.getInt(1));
				usuariosForm.setIdUsuario(Utils.noNulo(rs.getString(2)));
				usuariosForm.setIdEmpleado(Utils.noNulo(rs.getString(3)));
				usuariosForm.setNombreCompleto(Utils.noNulo(rs.getString(4)));
				usuariosForm.setCorreo(Utils.noNuloNormal(rs.getString(5)));
				usuariosForm.setIdPerfil(rs.getInt(6));
				usuariosForm.setDesPerfil(Utils.noNulo(rs.getString(7)));
				usuariosForm.setNombreCortoPerfil(Utils.noNulo(rs.getString(9)));
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return usuariosForm;
	}
	
	
	public UsuariosForm datosUsuarioEsquema (String esquemaEmpresa, String usuarioHTTP){
		Connection con = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UsuariosForm usuariosForm = new UsuariosForm();
		try {
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			
			stmt = con.prepareStatement(UsuariosQuerys.getDatosUsuario(rc.getEsquema()));
			stmt.setString(1, usuarioHTTP);
			stmt.setString(2, "A");
			stmt.setString(3, "F");
			rs  = stmt.executeQuery();
			if (rs.next()) {
				usuariosForm.setIdRegistro(rs.getInt(1));
				usuariosForm.setIdUsuario(Utils.noNulo(rs.getString(2)));
				usuariosForm.setIdEmpleado(Utils.noNulo(rs.getString(3)));
				usuariosForm.setNombreCompleto(Utils.noNulo(rs.getString(4)));
				usuariosForm.setCorreo(Utils.noNuloNormal(rs.getString(5)));
				usuariosForm.setIdPerfil(rs.getInt(6));
				usuariosForm.setDesPerfil(Utils.noNulo(rs.getString(7)));
				usuariosForm.setNombreCortoPerfil(Utils.noNulo(rs.getString(9)));
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
				if (con != null) {
					con.close();
				}
				con = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
				con = null;
			}
		}
		return usuariosForm;
	}
	
	public UsuariosForm informacionUsuario(Connection con, String esquema, String idEmpleado){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UsuariosForm usuariosForm = new UsuariosForm();
		
		try{
			stmt = con.prepareStatement(UsuariosQuerys.getInformacionUsuario(esquema));
			stmt.setString(1, "A");
			stmt.setString(2, idEmpleado);
			rs = stmt.executeQuery();
			if (rs.next()){
					usuariosForm.setIdRegistro(rs.getInt(1));
					usuariosForm.setIdUsuario(Utils.noNuloNormal(rs.getString(2)));
					usuariosForm.setNombreCompleto(Utils.noNulo(rs.getString(3)));
					usuariosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
					usuariosForm.setIdPerfil(rs.getInt(5));
					usuariosForm.setTipoPerfil(Utils.noNuloNormal(rs.getString(7)));
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				if (con != null){
					con.close();
				}
				con = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return usuariosForm;

	}
	
	
	public UsuariosForm informacionUsuarioProveedor(Connection con, String esquema, String idEmpleado){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UsuariosForm usuariosForm = new UsuariosForm();
		
		try{
			stmt = con.prepareStatement(UsuariosQuerys.getInformacionUsuarioProveedor(esquema));
			stmt.setString(1, idEmpleado);
			rs = stmt.executeQuery();
			if (rs.next()){
					usuariosForm.setIdRegistro(rs.getInt(1));
					usuariosForm.setIdUsuario(Utils.noNuloNormal(rs.getString(2)));
					usuariosForm.setNombreCompleto(Utils.noNulo(rs.getString(3)));
					usuariosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
					usuariosForm.setIdPerfil(rs.getInt(5));
					usuariosForm.setTipoPerfil(Utils.noNuloNormal(rs.getString(7)));
					usuariosForm.setEstatusRegistro(Utils.noNuloNormal(rs.getString(8)));
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				
			}catch(Exception e){
				rs = null;
				stmt = null;
			}
		}
		return usuariosForm;

	}
	
	
	public ArrayList<UsuariosForm> consultaUsuariosRH (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<UsuariosForm> listaUsuarios = new ArrayList<UsuariosForm>();
		UsuariosForm usuariosForm = new UsuariosForm();
		try {
			stmt = con.prepareStatement(UsuariosQuerys.getDetalleUsuariosRH(esquema));
			stmt.setString(1, "A");
			stmt.setString(2, "004");
			rs  = stmt.executeQuery();
			while (rs.next()) {
				usuariosForm.setIdRegistro(rs.getInt(1));
				usuariosForm.setIdUsuario(Utils.noNulo(rs.getString(2)));
				usuariosForm.setNombreCompleto(Utils.noNulo(rs.getString(3)));
				usuariosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
				usuariosForm.setIdPerfil(rs.getInt(5));
				usuariosForm.setDesPerfil(Utils.noNulo(rs.getString(6)));
				usuariosForm.setTipoPerfil(Utils.noNulo(rs.getString(7)));
				listaUsuarios.add(usuariosForm);
				usuariosForm = new UsuariosForm();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaUsuarios;
	}
	
}
