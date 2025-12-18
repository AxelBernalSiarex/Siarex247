package com.siarex247.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;

public class UtilsPATH {

	public static final Logger logger = Logger.getLogger("SAI");

	public static final String DOMINIO_PRINCIPAL = getRuta("DOM");
	public static final String HOST_CORREO = getRuta("HST");
	public static final String PASSWORD_DOMINIOS_SIAREX = getRuta("PWD");

	
	public static final String REPOSITORIO_DOCUMENTOS = getRuta_SIAREX("RR");
	public static final String RUTA_CORREO = getRuta_SIAREX("RC");
	public static final String RUTA_PUBLIC_PRINCIPAL = getRuta_SIAREX("RPP");
	public static final String RUTA_PUBLIC_HTML = getRuta_SIAREX("RPH");
	public static final String RUTA_PUBLIC_LAYOUT = getRuta_SIAREX("RPL");
	public static final String BANDERA_VALIDA_PRODIGIA = getRuta_SIAREX("BVP");
	public static final String NUMERO_CONTRATO_PRODIGIA = getRuta_SIAREX("NCP");
	public static final String AUTHORIZATION_CUENTA_PRODIGIA = getRuta_SIAREX("ACP");
	// public static final String RUTA_CERTIFICADOS_SAT = getRuta_SIAREX("RSA");
	public static final String HOST_CORREO_PROCESO = getRuta_SIAREX("PMS");
	public static final String API_KEY_TIMBRADO_DESCARGA = getRuta_SIAREX("APT");
	public static final String ENDPOINT_TIMBRADO_EXPRESSS = getRuta_SIAREX("TEP");
	public static final String API_KEY_TIMBRADO_VALIDAR = getRuta_SIAREX("APV");
	public static final String REPOSITORIO_RAIZ_DOCUMENTOS = getRuta_SIAREX("RRA");
	
	
	
	private static String getRuta(String idRuta) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rc = null;
		Connection con = null;
		StringBuffer sbQuery = new StringBuffer();
		String resPath = null;
		try {
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			sbQuery.append("select VALOR from PATHS where ID = ?");
			stmt = con.prepareStatement(sbQuery.toString());
			stmt.setString(1, idRuta);
			rs = stmt.executeQuery();
			if(rs.next()) {
				resPath = Utils.noNuloNormal(rs.getString(1));
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
		return resPath;
	}
	
	
	
	private static String getRuta_SIAREX(String idRuta) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rc = null;
		Connection con = null;
		StringBuffer sbQuery = new StringBuffer();
		String resPath = null;
		try {
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			sbQuery.append("select VALOR from PATHS_SIAREX247 where ID = ?");
			stmt = con.prepareStatement(sbQuery.toString());
			stmt.setString(1, idRuta);
			rs = stmt.executeQuery();
			if(rs.next()) {
				resPath = Utils.noNuloNormal(rs.getString(1));
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
		return resPath;
	}
	
	
}
