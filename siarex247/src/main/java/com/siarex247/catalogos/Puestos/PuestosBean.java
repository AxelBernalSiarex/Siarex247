package com.siarex247.catalogos.Puestos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class PuestosBean {

	public static final Logger logger = Logger.getLogger("siarex247");

	public ArrayList<PuestosForm> detallePuestos (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<PuestosForm> listaPuestos = new ArrayList<>();
		PuestosForm puestosForm = new PuestosForm();
		try {		
			stmt = con.prepareStatement(PuestosQuerys.getDetallePuestos(esquema));
			rs  = stmt.executeQuery();
			while (rs.next()) {
				puestosForm.setIdPuesto(rs.getInt(1));
				puestosForm.setNombreCorto(Utils.noNulo(rs.getString(2)));
				puestosForm.setDescripcion(Utils.noNulo(rs.getString(3)));
				listaPuestos.add(puestosForm);
				puestosForm = new PuestosForm();
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
		return listaPuestos;
	}
	
	
	
	public PuestosForm consultaPuesto (Connection con, String esquema, int idPuesto){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PuestosForm puestosForm = new PuestosForm();
		try {
			stmt = con.prepareStatement(PuestosQuerys.getConsultaPuesto(esquema));
			stmt.setInt(1, idPuesto);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				puestosForm.setIdPuesto(rs.getInt(1));
				puestosForm.setNombreCorto(Utils.noNulo(rs.getString(2)));
				puestosForm.setDescripcion(Utils.noNulo(rs.getString(3)));
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
		return puestosForm;
	}
	
	
	public int altaPuestos (Connection con, String esquema, String nombreCorto, String descripcion, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {		
			stmt = con.prepareStatement(PuestosQuerys.getGuardarPuesto(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, nombreCorto);
			stmt.setString(2, descripcion);
			int cant = stmt.executeUpdate();
			if(cant > 0){
				rs = stmt.getGeneratedKeys();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
			}
			logger.info("El usuario "+usuarioHTTP + " ha guardado el puesto "+resultado);
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
	
	
	public int modificaPuestos (Connection con, String esquema, int idPuesto, String nombreCorto, String descripcion, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {		
			logger.info("idPuesto: "+idPuesto+", nombreCorto "+nombreCorto + " descripcion "+descripcion);
			stmt = con.prepareStatement(PuestosQuerys.getUpdatePuesto(esquema));
			stmt.setString(1, nombreCorto);
			stmt.setString(2, descripcion);
			stmt.setInt(3, idPuesto);
			resultado = stmt.executeUpdate();
			logger.info("El usuario "+usuarioHTTP + " ha actualizado el puesto "+idPuesto);
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
	
	
	public int eliminaPuesto (Connection con, String esquema, int idPuesto, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {		
			stmt = con.prepareStatement(PuestosQuerys.getEliminaPuesto(esquema));
			stmt.setInt(1, idPuesto);
			resultado = stmt.executeUpdate();
			logger.info("El usuario "+usuarioHTTP + " ha eliminado el puesto "+idPuesto);
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
	
	public ArrayList<PuestosForm> comboPuestos (Connection con, String esquema, int idPuesto){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<PuestosForm> listaPuestos = new ArrayList<>();
		PuestosForm puestosForm = new PuestosForm();
		try {	
			stmt = con.prepareStatement(PuestosQuerys.getComboPuestos(esquema));
			rs  = stmt.executeQuery();
			int idPuestoRS = 0;
			while (rs.next()) {
				idPuestoRS = rs.getInt(1);
				if (idPuestoRS == idPuesto) {
					puestosForm.setSelected(true);
				}
				puestosForm.setIdPuesto(idPuestoRS);
				puestosForm.setDescripcion(Utils.noNulo(rs.getString(2)));
				listaPuestos.add(puestosForm);
				puestosForm = new PuestosForm();
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
		return listaPuestos;
	}
	
	
	
}
