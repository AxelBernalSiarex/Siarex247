package com.siarex247.configSistema.ConfigAdicionales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class ConfigAdicionalesBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	public static HashMap<String, Object> mapaVariables = new HashMap<>();
	
	
	
	public static String obtenerValorVariable(Connection con, String esquema, String variableConfig) {
		String valorVariable = null;
		try {
			if (mapaVariables.get(esquema) == null) {
        		HashMap<String, String> mapaConfig = calcularConfiguraciones(con, esquema);
        		mapaVariables.put(esquema, mapaConfig);
        		valorVariable = mapaConfig.get(variableConfig);
        	}else {
        		HashMap<String, String> mapaConfig = (HashMap<String, String>) mapaVariables.get(esquema);
				valorVariable = mapaConfig.get(variableConfig);
        	}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return valorVariable;
	}
	
	
	public static String obtenerValorVariable(String esquemaEmpresa, String variableConfig) {
		String valorVariable = null;
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rc = null;
		Connection con = null;
		
        try {
        	rc = connPool.getConnection(esquemaEmpresa);
        	con = rc.getCon();
        	
        	if (mapaVariables.get(rc.getEsquema()) == null) {
        		HashMap<String, String> mapaConfig = calcularConfiguraciones(con, rc.getEsquema());
        		mapaVariables.put(rc.getEsquema(), mapaConfig);
        		valorVariable = mapaConfig.get(variableConfig);
        	}else {
        		HashMap<String, String> mapaConfig = (HashMap<String, String>) mapaVariables.get(rc.getEsquema());
				valorVariable = mapaConfig.get(variableConfig);
        	}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(con != null)
	            	con.close();
	            con = null;
	        }catch(Exception e){
	            con = null;
	        }
        }
		return valorVariable;
	}
	
	
	public static HashMap<String, String> obtenerConfiguraciones(Connection con, String esquema) {
		HashMap<String, String> mapaConfig = null;
        try {
        	if (mapaVariables.get(esquema) == null) {
        		mapaConfig = calcularConfiguraciones(con, esquema);
        		mapaVariables.put(esquema, mapaConfig);
        	}else {
        		mapaConfig = (HashMap<String, String>) mapaVariables.get(esquema);
        	}
        	
        } catch(Exception e){
        	Utils.imprimeLog("", e);
        }
        return mapaConfig;
    }
	
	
	public HashMap<String, String> obtenerConfiguraciones(String esquemaEmpresa)
    {
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rc = null;
		Connection con = null;
		HashMap<String, String> mapaConfig = null;
		
        try {
        	rc = connPool.getConnection(esquemaEmpresa);
        	con = rc.getCon();
        	
        	if (mapaVariables.get(rc.getEsquema()) == null) {
        		mapaConfig = calcularConfiguraciones(con, rc.getEsquema());
        		mapaVariables.put(rc.getEsquema(), mapaConfig);
        	}else {
        		mapaConfig = (HashMap<String, String>) mapaVariables.get(rc.getEsquema());
        	}
        }
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(con != null)
	            	con.close();
	            con = null;
	        }catch(Exception e){
	            con = null;
	        }
        }
        return mapaConfig;
    }
	
	public static HashMap<String, String> calcularConfiguraciones(Connection con, String esquema)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> mapaConfig = new HashMap<String, String>();
        try {
            stmt = con.prepareStatement(ConfigAdicionalesQuerys.getObtenerVarible(esquema));
            rs = stmt.executeQuery();
            while (rs.next()){
            	mapaConfig.put(Utils.noNuloNormal(rs.getString(1)), Utils.noNuloNormal(rs.getString(3)));
            }
        }
        catch(Exception e){
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
	            stmt = null;
	        }
        }
        return mapaConfig;
    }
	
	public int insertVarible(Connection con, String esquema, String varible, String descripcion, String contenido)
    {
        PreparedStatement stmt = null;
        int res = 0;
        try
        {
            stmt = con.prepareStatement(ConfigAdicionalesQuerys.getInsertVarible(esquema));
            stmt.setString(1, varible);
            stmt.setString(2, descripcion);
            stmt.setString(3, contenido);
            
            res = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return res;
    }
	
	
	public int eliminarVarible(Connection con, String esquema, String varible)
    {
        PreparedStatement stmt = null;
        int res = 0;
        try
        {
            stmt = con.prepareStatement(ConfigAdicionalesQuerys.getEliminaVarible(esquema));
            stmt.setString(1, varible);
            res = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return res;
    }


	public static void setMapaVariables() {
		ConfigAdicionalesBean.mapaVariables = new HashMap<>();
	}


	
	
}
