package com.siarex247.procesos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class ProcesoMonitorBean {

	public static final Logger logger = Logger.getLogger("siarex247");

	
	public String revisaMonitoreo(int clave) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		ResultadoConexion rc = null;
		String bandMonitoreo = "";
		try {
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();

			stmt = con.prepareStatement(ProcesosMonitorQuerys.getQueryRevisaMon(""));
			stmt.setInt(1, clave);
			rs = stmt.executeQuery();
			if (rs.next()) {
				bandMonitoreo = rs.getString(1);
			} else {
				bandMonitoreo = "N";
			}
		} catch (Exception e) {
			Utils.imprimeLog("revisaMonitoreo ", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
				if (stmt != null)
					stmt.close();
				stmt = null;
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				rs = null;
				stmt = null;
				con = null;
			}
		}
		return bandMonitoreo;
	}
	
	
	public String terminaProceso(int clave){
		 PreparedStatement stmt = null;
			ConexionDB connPool = new ConexionDB();
			Connection con = null;
			ResultadoConexion rc = null;
	        String bandMonitoreo = "";
	        try
	        {
				rc = connPool.getConnectionSiarex();
				con = rc.getCon();
	        	
	            stmt = con.prepareStatement(ProcesosMonitorQuerys.getQueryApagaMon(""));
	            stmt.setString(1, "S");
	            stmt.setInt(2, clave);
	            stmt.executeUpdate();
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("terminaProceso ", e);
	        }finally{
		        try{
		            if(stmt != null)
		                stmt.close();
		            stmt = null;
		            if (con != null){
						con.close();
					}
		        }catch(Exception e){
		            stmt = null;
		            con = null;
		        }
	        }
	        return bandMonitoreo;
	}
	
	
	public String enciendeProceso(int clave){
		 PreparedStatement stmt = null;
			ConexionDB connPool = new ConexionDB();
			Connection con = null;
			ResultadoConexion rc = null;
	        String bandMonitoreo = "";
	        try
	        {
				rc = connPool.getConnectionSiarex();
				con = rc.getCon();
	        	
	            stmt = con.prepareStatement(ProcesosMonitorQuerys.getQueryApagaMon(""));
	            stmt.setString(1, "N");
	            stmt.setInt(2, clave);
	            
	            stmt.executeUpdate();
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("terminaProceso ", e);
	        }finally{
		        try{
		            if(stmt != null)
		                stmt.close();
		            stmt = null;
		            if (con != null){
						con.close();
					}
		        }catch(Exception e){
		            stmt = null;
		            con = null;
		        }
	        }
	        return bandMonitoreo;
	}
	
	public HashMap<String, String> getBanderas() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		ResultadoConexion rc = null;
		HashMap<String, String> mapaProceso = new HashMap<String, String>();
		try {
			rc = connPool.getConnectionSiarex();
					
			con = rc.getCon();

			stmt = con.prepareStatement(ProcesosMonitorQuerys.getQueryBanderasMon(""));
			rs = stmt.executeQuery();
			while (rs.next()) {
				mapaProceso.put("PROC_"+rs.getInt(1), Utils.noNulo(rs.getString(2)));
			} 
		} catch (Exception e) {
			Utils.imprimeLog("revisaMonitoreo ", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
				if (stmt != null)
					stmt.close();
				stmt = null;
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				rs = null;
				stmt = null;
				con = null;
			}
		}
		return mapaProceso;
	}

}
