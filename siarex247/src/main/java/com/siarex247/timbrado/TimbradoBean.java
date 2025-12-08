package com.siarex247.timbrado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class TimbradoBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public TimbradoForm consultarRespuestaTimbrado (String uuidXML){
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rc = null;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		TimbradoForm timbradoForm = new TimbradoForm();
		try {		
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			stmt = con.prepareStatement(TimbradoQuerys.getConsultarResultadoXML(rc.getEsquema()));
			stmt.setString(1, uuidXML);
			
			rs  = stmt.executeQuery();
			if (rs.next()) {
				timbradoForm.setIdRegistro(rs.getInt(1));
				timbradoForm.setEmpresa(Utils.noNulo(rs.getString(2)));
				timbradoForm.setFolioEmpresa(rs.getLong(3));
				timbradoForm.setTipoMetodo(Utils.noNuloNormal(rs.getString(4)));
				timbradoForm.setUuid(Utils.noNuloNormal(rs.getString(5)));
				timbradoForm.setCode(Utils.noNuloNormal(rs.getString(6)));
				timbradoForm.setRespuesta(Utils.noNuloNormal(rs.getString(7)));
				timbradoForm.setMensaje(Utils.noNuloNormal(rs.getString(8)));
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
			}
		}
		return timbradoForm;
	}
	
	public int guardarTimbrado (String nombreEmpresa, long folioEmpresa, String tipoMetodo, String uuid, String code, String data, String messaje, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rc = null;
		Connection con = null;
		try {
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
			stmt = con.prepareStatement(TimbradoQuerys.getGuardarBitacoraTimbrado(rc.getEsquema()), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, nombreEmpresa);
			stmt.setLong(2, folioEmpresa);
			stmt.setString(3, tipoMetodo);
			stmt.setString(4, uuid);
			stmt.setString(5, code);
			stmt.setString(6, data);
			stmt.setString(7, messaje);
			stmt.setString(8, usuarioHTTP);
			int cant = stmt.executeUpdate();
			if(cant > 0){
				rs = stmt.getGeneratedKeys();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
			}
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
	
}
