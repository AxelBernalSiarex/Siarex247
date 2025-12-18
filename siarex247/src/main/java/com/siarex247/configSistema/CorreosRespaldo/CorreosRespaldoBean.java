package com.siarex247.configSistema.CorreosRespaldo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.siarex247.utils.Utils;

public class CorreosRespaldoBean {

	
	public HashMap<String, String> obtenerConfiguraciones(Connection con, String esquema)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> mapaOrdenes = new HashMap<String, String>();
        try
        {
            stmt = con.prepareStatement(CorreosRespaldoQuerys.getObtenerVarible(esquema));
            rs = stmt.executeQuery();
            while (rs.next()){
            	mapaOrdenes.put(Utils.noNuloNormal(rs.getString(1)), Utils.noNuloNormal(rs.getString(3)));
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
        return mapaOrdenes;
    }

	
	
	public int eliminarVarible(Connection con, String esquema, String varible)
    {
        PreparedStatement stmt = null;
        int res = 0;
        try
        {
            stmt = con.prepareStatement(CorreosRespaldoQuerys.getEliminaVarible(esquema));
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
	
	
	
	public int insertVarible(Connection con, String esquema, String varible, String descripcion, String contenido)
    {
        PreparedStatement stmt = null;
        int res = 0;
        try
        {
            stmt = con.prepareStatement(CorreosRespaldoQuerys.getInsertVarible(esquema));
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
}
