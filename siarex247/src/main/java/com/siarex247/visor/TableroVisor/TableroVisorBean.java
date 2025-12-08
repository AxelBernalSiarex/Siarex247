package com.siarex247.visor.TableroVisor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsSiarex;

public class TableroVisorBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	private DecimalFormat decimal = new DecimalFormat("###,###.##");
	private DecimalFormat decimalEntero = new DecimalFormat("###,###");
	
	
	
	public int obtenerTotalOrdenes (Connection con, String esquema, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totalOrdenes = 0;
		try {
			StringBuffer sbQuery = new StringBuffer(TableroVisorQuerys.getTotalOrdenes(esquema));
			if (claveProveedor > 0) {
				sbQuery.append(" where CLAVE_PROVEEDOR = ? ");
			}
			stmt = con.prepareStatement(sbQuery.toString());
			if (claveProveedor > 0) {
				stmt.setInt(1, claveProveedor);
			}
			
			rs  = stmt.executeQuery();
			if (rs.next()) {
				totalOrdenes = rs.getInt(1);
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
		return totalOrdenes;
	}
	
	
	public String obtenerTotalXmoneda (Connection con, String esquema, String tipoMoneda, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String totalMoneda = "";
		try {
			StringBuffer sbQuery = new StringBuffer(TableroVisorQuerys.getTotalOrdenesXmoneda(esquema));
			if (claveProveedor > 0) {
				sbQuery.append(" and CLAVE_PROVEEDOR = ? ");
			}
			
			stmt = con.prepareStatement(sbQuery.toString());
			stmt.setString(1, tipoMoneda);
			if (claveProveedor > 0) {
				stmt.setInt(2, claveProveedor);
			}
			rs  = stmt.executeQuery();
			if (rs.next()) {
				totalMoneda = decimal.format(rs.getDouble(1));
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
		return totalMoneda;
	}
	
	
	
	public HashMap<String, TableroVisorForm> obtenerTotalEstatus (Connection con, String esquema, String lenguaje, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, TableroVisorForm> mapaEstatus = new HashMap<>();
		TableroVisorForm tableroForm = new TableroVisorForm();
		
		try {
			mapaEstatus.put("A1", null);
			mapaEstatus.put("A2", null);
			mapaEstatus.put("A3", null);
			mapaEstatus.put("A4", null);
			mapaEstatus.put("A5", null);
			mapaEstatus.put("A6", null);
			
			float totalOrdenes = obtenerTotalOrdenes(con, esquema, 0);
			
			StringBuffer sbQuery = new StringBuffer(TableroVisorQuerys.getTotalEstatus(esquema));
			if (claveProveedor > 0) {
				sbQuery.append(" where CLAVE_PROVEEDOR = ? ");
			}
			sbQuery.append(" group by ESTATUS_PAGO ");
			stmt = con.prepareStatement(sbQuery.toString());
			if (claveProveedor > 0) {
				stmt.setInt(1, claveProveedor);
			}
			
			rs  = stmt.executeQuery();
			String estatusOrden = null;
			long totalEstatus = 0;
			
			double totPorcentaje = 0;
			while (rs.next()) {
				totalEstatus = rs.getLong(1);
				estatusOrden = Utils.noNulo(rs.getString(2));
				totPorcentaje = ((totalEstatus / totalOrdenes) * 100);
				
				// logger.info("totalOrdenes===>"+totalOrdenes+", estatusOrden===>"+estatusOrden+", totalEstatus===>"+totalEstatus+", totPorcentaje ===>"+totPorcentaje );
				
				tableroForm.setEstatus(estatusOrden);
				tableroForm.setDesEstatus(UtilsSiarex.desEstatus(estatusOrden, lenguaje));
				tableroForm.setPorcentajeEstatus(decimal.format(totPorcentaje) + "%");
				tableroForm.setTotalEstatus(totalEstatus);
				tableroForm.setDesTotalEstatus(decimalEntero.format(totalEstatus));
				
				mapaEstatus.put(estatusOrden, tableroForm);
				tableroForm = new TableroVisorForm();
			}
			
			if (mapaEstatus.get("A1") == null) {
				tableroForm = new TableroVisorForm();
				tableroForm.setEstatus("A1");
				tableroForm.setDesEstatus(UtilsSiarex.desEstatus("A1", lenguaje));
				tableroForm.setPorcentajeEstatus(0 + "%");
				tableroForm.setTotalEstatus(0);
				tableroForm.setDesTotalEstatus("0");
			}
			if (mapaEstatus.get("A2") == null) {
				tableroForm = new TableroVisorForm();
				tableroForm.setEstatus("A2");
				tableroForm.setDesEstatus(UtilsSiarex.desEstatus("A2", lenguaje));
				tableroForm.setPorcentajeEstatus(0 + "%");
				tableroForm.setTotalEstatus(0);
				tableroForm.setDesTotalEstatus("0");
			}
			if (mapaEstatus.get("A3") == null) {
				tableroForm = new TableroVisorForm();
				tableroForm.setEstatus("A3");
				tableroForm.setDesEstatus(UtilsSiarex.desEstatus("A3", lenguaje));
				tableroForm.setPorcentajeEstatus(0 + "%");
				tableroForm.setTotalEstatus(0);
				tableroForm.setDesTotalEstatus("0");
			}
			if (mapaEstatus.get("A4") == null) {
				tableroForm = new TableroVisorForm();
				tableroForm.setEstatus("A4");
				tableroForm.setDesEstatus(UtilsSiarex.desEstatus("A4", lenguaje));
				tableroForm.setPorcentajeEstatus(0 + "%");
				tableroForm.setTotalEstatus(0);
				tableroForm.setDesTotalEstatus("0");
			}
			if (mapaEstatus.get("A5") == null) {
				tableroForm = new TableroVisorForm();
				tableroForm.setEstatus("A5");
				tableroForm.setDesEstatus(UtilsSiarex.desEstatus("A5", lenguaje));
				tableroForm.setPorcentajeEstatus(0 + "%");
				tableroForm.setTotalEstatus(0);
				tableroForm.setDesTotalEstatus("0");
			}
			
			if (mapaEstatus.get("A6") == null) {
				tableroForm = new TableroVisorForm();
				tableroForm.setEstatus("A6");
				tableroForm.setDesEstatus(UtilsSiarex.desEstatus("A6", lenguaje));
				tableroForm.setPorcentajeEstatus(0 + "%");
				tableroForm.setTotalEstatus(0);
				tableroForm.setDesTotalEstatus("0");
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
		return mapaEstatus;
	}

	
	public int obtenerTotalFactura (Connection con, String esquema, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totalFacturas = 0;
		try {
			String _WHERE_PROVEEDOR = "";
			if (claveProveedor > 0) {
				_WHERE_PROVEEDOR = " where CLAVE_PROVEEDOR = ? ";
			}
			
			StringBuffer sbQuery = new StringBuffer(TableroVisorQuerys.getTotalFacturas(esquema, _WHERE_PROVEEDOR));
			
			
			stmt = con.prepareStatement(sbQuery.toString());
			if (claveProveedor > 0) {
				stmt.setInt(1, claveProveedor);
			}
			
			rs  = stmt.executeQuery();
			if (rs.next()) {
				totalFacturas = rs.getInt(1);
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
		return totalFacturas;
	}
	
	
	public int obtenerTotalComplemento (Connection con, String esquema, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totalComplemento = 0;
		try {
			String _WHERE_PROVEEDOR = "";
			if (claveProveedor > 0) {
				_WHERE_PROVEEDOR = " where B.CLAVE_PROVEEDOR = ? ";
			}
			
			stmt = con.prepareStatement(TableroVisorQuerys.getTotalComplementos(esquema, _WHERE_PROVEEDOR));
			if (claveProveedor > 0) {
				stmt.setInt(1, claveProveedor);
			}
			rs  = stmt.executeQuery();
			if (rs.next()) {
				totalComplemento = rs.getInt(1);
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
		return totalComplemento;
	}
	
	
	public int obtenerTotalNotasCredito (Connection con, String esquema, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totalNotas = 0;
		try {
			String _WHERE_PROVEEDOR = "";
			if (claveProveedor > 0) {
				_WHERE_PROVEEDOR = " where B.CLAVE_PROVEEDOR = ? ";
			}
			
			stmt = con.prepareStatement(TableroVisorQuerys.getTotalNotaCredito(esquema, _WHERE_PROVEEDOR));
			if (claveProveedor > 0) {
				stmt.setInt(1, claveProveedor);
			}
			rs  = stmt.executeQuery();
			if (rs.next()) {
				totalNotas = rs.getInt(1);
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
		return totalNotas;
	}
	
	
	public int obtenerTotalProveedores (Connection con, String esquema, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totalProveedores = 0;
		try {
			
			StringBuffer sbQuery = new StringBuffer(TableroVisorQuerys.getTotalProveedores(esquema));
			if (claveProveedor > 0) {
				sbQuery.append(" and CLAVE_PROVEEDOR = ? ");
			}
			stmt = con.prepareStatement(sbQuery.toString());
			stmt.setString(1, "A");
			if (claveProveedor > 0) {
				stmt.setInt(2, claveProveedor);
			}
			
			rs  = stmt.executeQuery();
			if (rs.next()) {
				totalProveedores = rs.getInt(1);
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
		return totalProveedores;
	}
	
	public ArrayList<TableroVisorForm> listaProveedoresTOP (Connection con, String esquema, String tipoMoneda, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		ArrayList<TableroVisorForm> listaDetalle = new ArrayList<>();
		TableroVisorForm tableroForm = new TableroVisorForm();
		try {
			
			String _WHERE_PROVEEDOR = "";
			if (claveProveedor > 0) {
				_WHERE_PROVEEDOR = " and A.CLAVE_PROVEEDOR = ? ";
			}
			
			stmt = con.prepareStatement(TableroVisorQuerys.getListaTopProveedores(esquema, _WHERE_PROVEEDOR));
			stmt.setString(1, tipoMoneda);
			if (claveProveedor > 0) {
				stmt.setInt(2, claveProveedor);
			}
			rs  = stmt.executeQuery();
			
			HashMap<Integer, Double> mapaPagados = topProveedorPagados(con, esquema, tipoMoneda, claveProveedor);
			int claveProveedorRS = 0;
			while (rs.next()) {
				claveProveedorRS = rs.getInt(3);
				
				double totalPagado = 0;
				
				if (mapaPagados.get(claveProveedorRS) == null) {
					totalPagado = 0;
				}else {
					totalPagado = mapaPagados.get(claveProveedorRS);
				}
				
				tableroForm.setRazonSocial(Utils.regresaCaracteresNormales(rs.getString(1)));
				tableroForm.setRfc(Utils.noNulo(rs.getString(2)));
				tableroForm.setClaveProveedor(claveProveedorRS);
				tableroForm.setTotalMonto(decimal.format(rs.getDouble(4)));
				tableroForm.setTotalFacturado(decimal.format(rs.getDouble(5)));
				tableroForm.setTotalPagado(decimal.format(totalPagado));
				
				listaDetalle.add(tableroForm);
				tableroForm = new TableroVisorForm();
				
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
		return listaDetalle;
	}
	

	private HashMap<Integer, Double> topProveedorPagados (Connection con, String esquema, String tipoMoneda, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<Integer, Double> mapaPagados = new HashMap<>();
		try {
			String _WHERE_PROVEEDOR = "";
			if (claveProveedor > 0) {
				_WHERE_PROVEEDOR = " and CLAVE_PROVEEDOR = ? ";
			}
			
			stmt = con.prepareStatement(TableroVisorQuerys.getTopProveedoresPagados(esquema, _WHERE_PROVEEDOR));
			stmt.setString(1, tipoMoneda);
			stmt.setString(2, "A4");
			if (claveProveedor > 0) {
				stmt.setInt(3, claveProveedor);
			}
			rs  = stmt.executeQuery();
			
			while (rs.next()) {
				int claveProveedorRS = rs.getInt(2);
				mapaPagados.put(claveProveedorRS, rs.getDouble(1));
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
		return mapaPagados;
	}
	
	
	
	public ArrayList<TableroVisorForm> listaOrdenes (Connection con, String esquema, String lenguaje, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		ArrayList<TableroVisorForm> listaDetalle = new ArrayList<>();
		TableroVisorForm tableroForm = new TableroVisorForm();
		try {
			String _WHERE_PROVEEDOR = "";
			if (claveProveedor > 0) {
				_WHERE_PROVEEDOR = " where A.CLAVE_PROVEEDOR = ? ";
			}
			
			stmt = con.prepareStatement(TableroVisorQuerys.getListaOrdenes(esquema, _WHERE_PROVEEDOR));
			if (claveProveedor > 0) {
				stmt.setInt(1, claveProveedor);
			}
			rs  = stmt.executeQuery();
			
			while (rs.next()) {
				
				tableroForm.setRazonSocial(Utils.regresaCaracteresNormales(rs.getString(1)));
				// tableroForm.setRfc(Utils.noNulo(rs.getString(2)));
				// tableroForm.setFolioOrden(rs.getLong(3));
				tableroForm.setFolioEmpresa(rs.getLong(4));
				tableroForm.setTipoMoneda(Utils.noNulo(rs.getString(5)));
				tableroForm.setTotalMonto(decimal.format(rs.getDouble(6)));
				tableroForm.setEstatus(rs.getString(7));
				tableroForm.setDesEstatus(rs.getString(7) + " - "+  UtilsSiarex.desEstatus(rs.getString(7), lenguaje));
				listaDetalle.add(tableroForm);
				tableroForm = new TableroVisorForm();
				
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
		return listaDetalle;
	}
	
	
	
	public static void main(String[] args) {
		try {
			
			int num1 = 5;
			int num2 = 20;
			
			double porcentaje = ( num1 / num2 ) * 100;
			
			System.err.println("porcentaje===>"+porcentaje);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
