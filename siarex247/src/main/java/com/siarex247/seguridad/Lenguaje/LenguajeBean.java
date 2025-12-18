package com.siarex247.seguridad.Lenguaje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;


public class LenguajeBean {

private static HashMap<String, HashMap<String, String > > mapaLenguaje = null;
private static LenguajeBean  _instance = null;

	public static synchronized LenguajeBean instance() {
		if (_instance == null) {
			_instance = new LenguajeBean();
			mapaLenguaje = new HashMap<String, HashMap<String, String>>();
		}
		return _instance;
	}
		

	
	public HashMap<String, String> obtenerEtiquetas (String idLenguaje, String nombrePantalla){
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rc = null;
		Connection con = null;
		String esquema = null;
		HashMap<String, String> mapaPermisos = null;
		try {
			
			String llaveMapa = idLenguaje + "_" + nombrePantalla;
			if (null == mapaLenguaje.get(llaveMapa)) {
				rc = connPool.getConnectionLenguaje();	
				con = rc.getCon();
				esquema = rc.getEsquema();
				mapaPermisos = calcularEtiquetas(con, esquema, idLenguaje, nombrePantalla);
				mapaLenguaje.put(llaveMapa, mapaPermisos);
			}else {
				return mapaLenguaje.get(llaveMapa);
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			}catch(Exception e) {
				con = null;
			}
		}
		return mapaPermisos;
	}
	
	
	private static HashMap<String, String> calcularEtiquetas (Connection con, String esquema, String idLenguaje, String nombrePantalla)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String > mapaRes = new HashMap<String, String>();
        try{
        	
            stmt = con.prepareStatement(LenguajeQuerys.getQueryEtiquetasLenguaje(esquema));
            stmt.setString(1, "SIAREX");
            stmt.setString(2, nombrePantalla);
            rs = stmt.executeQuery();
	        String mensajeLenguaje = "";
	        while (rs.next()){
	        	if ("MX".equalsIgnoreCase(idLenguaje)) {
	        		mensajeLenguaje = Utils.noNuloNormal(rs.getString(2));
	        	}else {
	        		mensajeLenguaje = Utils.noNuloNormal(rs.getString(3));
	        	}
	        	mapaRes.put(Utils.noNulo(rs.getString(1)), mensajeLenguaje);
	        }
        }catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
        	try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;

        	}catch(Exception e){
        		rs = null;
        		stmt = null;
        	}
        }
        return mapaRes;
    }
	
	
}
