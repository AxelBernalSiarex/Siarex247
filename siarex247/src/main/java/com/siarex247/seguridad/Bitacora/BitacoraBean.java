package com.siarex247.seguridad.Bitacora;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.utils.Utils;


public class BitacoraBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	
	public ArrayList<BitacoraForm> detalleBitacora (Connection con, String esquema, int idBitacora){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<BitacoraForm> listaBitacora = new ArrayList<>();
		BitacoraForm bitacoraForm = new BitacoraForm();
		try {	
			stmt = con.prepareStatement(BitacoraQuerys.getDetalleHistoricoBitacora(esquema));
			stmt.setInt(1, idBitacora);
			rs  = stmt.executeQuery();
			while (rs.next()) {
				bitacoraForm.setIdRegistro(rs.getInt(1));
				bitacoraForm.setIdLLave(Utils.noNulo(rs.getString(2)));
				bitacoraForm.setDesError(Utils.noNuloNormal(rs.getString(3)));
				listaBitacora.add(bitacoraForm);
				bitacoraForm = new BitacoraForm();
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
		return listaBitacora;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleFormatosHistoricos (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();

		try {
			StringBuffer sbQuery = new StringBuffer(BitacoraQuerys.getDetalleHistorico(esquema));
			stmt = con.prepareStatement(sbQuery.toString());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
					jsonobj.put("idRegistro", rs.getInt(1));
					jsonobj.put("descripcion", Utils.noNulo(rs.getString(2)));
					jsonobj.put("tipoProveedor", Utils.noNulo(rs.getString(3)));
					jsonobj.put("razonSocial", Utils.regresaCaracteresNormales( Utils.noNuloNormal(rs.getString(5))));
					jsonobj.put("usuarioHTTP", Utils.noNulo(rs.getString(7)));
					jsonobj.put("nombreUsuarioHTTP", Utils.noNulo(rs.getString(8)));
					jsonobj.put("fechaTarea", Utils.noNulo(rs.getString(9)));
					jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
			}
			
			mapaRes.put("detalle", jsonArray);
	        
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
		return mapaRes;
	}
	
	
	public int altaBitacora(Connection con, String esquema, String nombreArchivo, int totalRegistros, String tipoCarga, int regOK, int regNG, int estatus, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(BitacoraQuerys.getAltaBitacora(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, nombreArchivo);
			stmt.setInt(2, totalRegistros);
			stmt.setString(3, tipoCarga);
			stmt.setInt(4, regOK);
			stmt.setInt(5, regNG);
			stmt.setInt(6, estatus);
			stmt.setString(7, usuarioHTTP);
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
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return resultado;
	}
	
	
	public int altaHistorico(Connection con, String esquema, int numBitacora, String llaveRegistro, String desError){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(BitacoraQuerys.getAltaHistorico(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, numBitacora);
			stmt.setString(2, llaveRegistro);
			stmt.setString(3, "N");
			stmt.setString(4, desError);
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
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return resultado;
	}
	
	
	public int updateBitacora(Connection con, String esquema, int idTarea, int totalRegistros, int regOK, int regNG, int estatus, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(BitacoraQuerys.getUpdateBitacora(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, totalRegistros);
			stmt.setInt(2, regOK);
			stmt.setInt(3, regNG);
			stmt.setInt(4, estatus);
			stmt.setInt(5, idTarea);
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
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return resultado;
	}
	
	
}
