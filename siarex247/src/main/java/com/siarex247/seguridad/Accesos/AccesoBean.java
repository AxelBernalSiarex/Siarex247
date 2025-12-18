package com.siarex247.seguridad.Accesos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class AccesoBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");

	
	
	public String existeSiarexActivo (String usuario){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ResultadoConexion rc = null;
		Connection con = null;
		ConexionDB connPool = null;
		String codeAcceso = "";
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
			stmt = con.prepareStatement(AccesoQuerys.getConsultaAcceso(""));	
			stmt.setString(1, usuario);
			stmt.setString(2, "A");
			rs  = stmt.executeQuery();
			if (rs.next()) {
				codeAcceso = Utils.noNuloNormal(rs.getString(1));
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
		return codeAcceso;
	}
	
	
	
	public int altaAcceso (int claveEmpresa, String nombreUsuario, String idUsuario, String pwdUsuario, String emailUsuario, int perfilUsuario,
			String idEmpleado, String resetPassword, String codigoAcceso, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		ResultadoConexion rc = null;
		Connection con = null;
		ConexionDB connPool = null;
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
			stmt = con.prepareStatement(AccesoQuerys.getAltaAcceso(""));
			stmt.setInt(1, claveEmpresa);
			stmt.setString(2, idUsuario);
			stmt.setString(3, pwdUsuario);
			stmt.setString(4, "SIAREX247");
			stmt.setInt(5, perfilUsuario);
			stmt.setString(6, nombreUsuario);
			stmt.setInt(7, 0);
			stmt.setString(8, emailUsuario);
			
			stmt.setString(9, idEmpleado);
			stmt.setString(10, resetPassword);
			stmt.setString(11, codigoAcceso);
			stmt.setString(12, "S");
			stmt.setString(13, usuarioHTTP);
			
			// logger.info("stmt Acceso====>"+stmt);
			stmt.executeUpdate();
			
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
		return resultado;
	}
	
	
	  
	public int actualizaAcceso (int claveEmpresa, String nombreUsuario, String idUsuario, int perfilUsuario, String emailUsuario, String idEmpleado, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		ResultadoConexion rc = null;
		Connection con = null;
		ConexionDB connPool = null;
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();		
			stmt = con.prepareStatement(AccesoQuerys.getActualizaAcceso(""));
			stmt.setInt(1, perfilUsuario);
			stmt.setString(2, nombreUsuario);
			stmt.setString(3, emailUsuario);
			stmt.setString(4, idEmpleado);
			stmt.setString(5, usuarioHTTP);
			stmt.setInt(6, claveEmpresa);
			stmt.setString(7, idUsuario);
			stmt.setString(8, "SIAREX247");
			stmt.executeUpdate();
			
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
		return resultado;
	}
	
	
	
	public int eliminaAcceso (int claveEmpresa, String idUsuario){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		ResultadoConexion rc = null;
		Connection con = null;
		ConexionDB connPool = null;
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
			stmt = con.prepareStatement(AccesoQuerys.getEliminaAcceso(""));
			
			stmt.setInt(1, claveEmpresa);
			stmt.setString(2, idUsuario);
			stmt.setString(3, "SIAREX247");
			stmt.executeUpdate();
			
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
		return resultado;
	}
	

	public int eliminaAccesoTomcat (String idUsuario){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		ResultadoConexion rc = null;
		Connection con = null;
		ConexionDB connPool = null;
		try {
			connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
			stmt = con.prepareStatement(AccesoQuerys.getEliminaAccesoUserTomcat(""));
			stmt.setString(1, idUsuario);
			stmt.executeUpdate();
			
			stmt.close();
			
			stmt = con.prepareStatement(AccesoQuerys.getEliminaAccesoRolesTomcat(""));
			stmt.setString(1, idUsuario);
			stmt.executeUpdate();
			
			
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
		return resultado;
	}
	
	public ArrayList<EmpresasForm> listaEmpresas (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<EmpresasForm> listaEmpresas = new ArrayList<EmpresasForm>();
		EmpresasForm empresasForm = new EmpresasForm();
		try {
			
			stmt = con.prepareStatement(AccesoQuerys.getListaEmpresas(""));
			stmt.setString(1, "SIAREX247");
			stmt.setString(2, "A");
			rs  = stmt.executeQuery();
			while (rs.next()) {
				empresasForm.setClaveEmpresa(rs.getInt(1));
				empresasForm.setNombreCorto(Utils.noNulo(rs.getString(2)));
				empresasForm.setNombreLargo(Utils.noNulo(rs.getString(3)));
				empresasForm.setRfc(Utils.noNulo(rs.getString(4)));
				empresasForm.setEstatus(Utils.noNulo(rs.getString(5)));
				empresasForm.setEsquema(Utils.noNuloNormal(rs.getString(6)));
				empresasForm.setNombreContacto(Utils.noNulo(rs.getString(7)));
				empresasForm.setEmailDominio(Utils.noNuloNormal(rs.getString(8)));
				empresasForm.setEmailContacto(Utils.noNuloNormal(rs.getString(9)));
				empresasForm.setPwdCorreo(Utils.noNuloNormal(rs.getString(10)));
				empresasForm.setPwdSat(Utils.noNuloNormal(rs.getString(11)));
				empresasForm.setArchivoCer(Utils.noNuloNormal(rs.getString(12)));
				empresasForm.setArchivoKey(Utils.noNuloNormal(rs.getString(13)));
				empresasForm.setTipoEmpresa(Utils.noNuloNormal(rs.getString(14)));
				listaEmpresas.add(empresasForm);
				empresasForm = new EmpresasForm();
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
		return listaEmpresas;
	}

	
	
	
	public EmpresasForm consultaEmpresa (Connection con, String esquema, int claveEmpresa){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EmpresasForm empresasForm = new EmpresasForm();
		try {
			stmt = con.prepareStatement(AccesoQuerys.getConsultaEmpresa(""));
			stmt.setInt(1, claveEmpresa);
			
			rs  = stmt.executeQuery();
			if (rs.next()) {
				empresasForm.setClaveEmpresa(rs.getInt(1));
				empresasForm.setNombreCorto(Utils.noNulo(rs.getString(2)));
				empresasForm.setNombreLargo(Utils.noNulo(rs.getString(3)));
				empresasForm.setRfc(Utils.noNulo(rs.getString(4)));
				empresasForm.setEstatus(Utils.noNulo(rs.getString(5)));
				empresasForm.setEsquema(Utils.noNuloNormal(rs.getString(6)));
				empresasForm.setNombreContacto(Utils.noNulo(rs.getString(7)));
				empresasForm.setEmailDominio(Utils.noNuloNormal(rs.getString(8)));
				empresasForm.setEmailContacto(Utils.noNuloNormal(rs.getString(9)));
				empresasForm.setPwdCorreo(Utils.noNuloNormal(rs.getString(10)));
				// empresasForm.setMostrarExtras(Utils.noNuloNormal(rs.getString(11)));
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
		return empresasForm;
	}
	
	
	
	public EmpresasForm consultaEmpresaEsquema (String esquemaEmpresa){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EmpresasForm empresasForm = new EmpresasForm();
		
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		try {
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
			stmt = con.prepareStatement(AccesoQuerys.getConsultaEmpresaEsquema(""));
			stmt.setString(1, esquemaEmpresa);
			// MOSTRAR_EXTRAS, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO1, LOGO
			
			// logger.info("stmtEmpresas===>"+stmt);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				empresasForm.setClaveEmpresa(rs.getInt(1));
				empresasForm.setNombreCorto(Utils.noNulo(rs.getString(2)));
				empresasForm.setNombreLargo(Utils.noNulo(rs.getString(3)));
				empresasForm.setRfc(Utils.noNulo(rs.getString(4)));
				empresasForm.setEstatus(Utils.noNulo(rs.getString(5)));
				empresasForm.setEsquema(Utils.noNuloNormal(rs.getString(6)));
				empresasForm.setNombreContacto(Utils.noNulo(rs.getString(7)));
				empresasForm.setEmailDominio(Utils.noNuloNormal(rs.getString(8)));
				empresasForm.setEmailContacto(Utils.noNuloNormal(rs.getString(9)));
				empresasForm.setPwdCorreo(Utils.noNuloNormal(rs.getString(10)));
				// empresasForm.setMostrarExtras(Utils.noNuloNormal(rs.getString(11)));
				empresasForm.setCalle(Utils.noNuloNormal(rs.getString(12)));
				empresasForm.setNumeroInterior(Utils.noNuloNormal(rs.getString(13)));
				empresasForm.setNumeroExterior(Utils.noNuloNormal(rs.getString(14)));
				empresasForm.setColonia(Utils.noNuloNormal(rs.getString(15)));
				empresasForm.setCiudad(Utils.noNuloNormal(rs.getString(16)));
				empresasForm.setEstado(Utils.noNuloNormal(rs.getString(17)));
				empresasForm.setCodigoPostal(rs.getInt(18));
				empresasForm.setTelefono(Utils.noNuloNormal(rs.getString(19)));
				empresasForm.setLogoEmpresa(Utils.noNuloNormal(rs.getString(20)));
				empresasForm.setTipoEmpresa(Utils.noNuloNormal(rs.getString(21))); // S = Nacional, N = Extranjera
				empresasForm.setRequisicion(Utils.noNuloNormal(rs.getString(22)));
				empresasForm.setTipoAcceso(Utils.noNuloNormal(rs.getString(23)));
				empresasForm.setPwdSat(Utils.noNuloNormal(rs.getString(24)));
				empresasForm.setArchivoCer(Utils.noNuloNormal(rs.getString(25)));
				empresasForm.setArchivoKey(Utils.noNuloNormal(rs.getString(26)));
				empresasForm.setRegimenFiscal(Utils.noNuloNormal(rs.getString(27)));
				
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
		return empresasForm;
	}

	
	public EmpresasForm consultaEmpresaRFC (String rfcEmpresa){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EmpresasForm empresasForm = new EmpresasForm();
		
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		try {
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
			stmt = con.prepareStatement(AccesoQuerys.getConsultaEmpresaRFC(""));
			stmt.setString(1, rfcEmpresa);
			// MOSTRAR_EXTRAS, CALLE, NUMERO_INT, NUMERO_EXT, COLONIA, CIUDAD, ESTADO, CODIGO_POSTAL, TELEFONO1, LOGO
			
			// logger.info("stmtEmpresas===>"+stmt);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				empresasForm.setClaveEmpresa(rs.getInt(1));
				empresasForm.setNombreCorto(Utils.noNulo(rs.getString(2)));
				empresasForm.setNombreLargo(Utils.noNulo(rs.getString(3)));
				empresasForm.setRfc(Utils.noNulo(rs.getString(4)));
				empresasForm.setEstatus(Utils.noNulo(rs.getString(5)));
				empresasForm.setEsquema(Utils.noNuloNormal(rs.getString(6)));
				empresasForm.setNombreContacto(Utils.noNulo(rs.getString(7)));
				empresasForm.setEmailDominio(Utils.noNuloNormal(rs.getString(8)));
				empresasForm.setEmailContacto(Utils.noNuloNormal(rs.getString(9)));
				empresasForm.setPwdCorreo(Utils.noNuloNormal(rs.getString(10)));
				// empresasForm.setMostrarExtras(Utils.noNuloNormal(rs.getString(11)));
				empresasForm.setCalle(Utils.noNuloNormal(rs.getString(12)));
				empresasForm.setNumeroInterior(Utils.noNuloNormal(rs.getString(13)));
				empresasForm.setNumeroExterior(Utils.noNuloNormal(rs.getString(14)));
				empresasForm.setColonia(Utils.noNuloNormal(rs.getString(15)));
				empresasForm.setCiudad(Utils.noNuloNormal(rs.getString(16)));
				empresasForm.setEstado(Utils.noNuloNormal(rs.getString(17)));
				empresasForm.setCodigoPostal(rs.getInt(18));
				empresasForm.setTelefono(Utils.noNuloNormal(rs.getString(19)));
				empresasForm.setLogoEmpresa(Utils.noNuloNormal(rs.getString(20)));
				empresasForm.setTipoEmpresa(Utils.noNuloNormal(rs.getString(21))); // S = Nacional, N = Extranjera
				empresasForm.setRequisicion(Utils.noNuloNormal(rs.getString(22)));
				empresasForm.setTipoAcceso(Utils.noNuloNormal(rs.getString(23)));
				empresasForm.setPwdSat(Utils.noNuloNormal(rs.getString(24)));
				empresasForm.setArchivoCer(Utils.noNuloNormal(rs.getString(25)));
				empresasForm.setArchivoKey(Utils.noNuloNormal(rs.getString(26)));
				empresasForm.setRegimenFiscal(Utils.noNuloNormal(rs.getString(27)));
				
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
		return empresasForm;
	}
}
