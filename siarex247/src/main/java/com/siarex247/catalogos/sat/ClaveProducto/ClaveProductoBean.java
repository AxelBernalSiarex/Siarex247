package com.siarex247.catalogos.sat.ClaveProducto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class ClaveProductoBean {

	public static final Logger logger = Logger.getLogger("siarex247");

	public HashMap<String, ClaveProductoForm> detalleClaveProductos (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, ClaveProductoForm> mapProductos = new HashMap<>();
		ClaveProductoForm claveProductoForm = new ClaveProductoForm();
		try {		
			stmt = con.prepareStatement(ClaveProductoQuerys.getDetalle(esquema));
			rs  = stmt.executeQuery();
			String clavProducto = null;
			while (rs.next()) {
				clavProducto = Utils.noNulo(rs.getString(1));
				claveProductoForm.setClaveProducto(clavProducto);
				claveProductoForm.setDescripcion( Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(2))));
				mapProductos.put(clavProducto, claveProductoForm);
				claveProductoForm = new ClaveProductoForm();
				
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
		return mapProductos;
	}
	
}
