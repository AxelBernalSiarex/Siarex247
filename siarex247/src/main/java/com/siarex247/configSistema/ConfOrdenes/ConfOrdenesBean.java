package com.siarex247.configSistema.ConfOrdenes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class ConfOrdenesBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	public HashMap<String, String> obtenerConfiguracion(Connection con, String esquema)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> mapaOrdenes = new HashMap<String, String>();
        try
        {
            stmt = con.prepareStatement(ConfOrdenesQuerys.getQueryObtenerConfOrdenes(esquema));
            rs = stmt.executeQuery();
			while(rs.next()) 
            {
				mapaOrdenes.put(rs.getString(1), rs.getString(2));	
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
	
	
	
	public int guardarConfiguracion(Connection con, String esquema, String LLAVE_FINPDF, String LLAVE_ORDENES, 
 			String LLAVE_FIN_ORDENES, String LLAVE_TOTAL, String LLAVE_FIN_TOTAL, String LLAVE_VENDOR, String LLAVE_FIN_VENDOR, 
 			String LLAVE_MONEDA, String LLAVE_FIN_MONEDA){
        PreparedStatement stmt = null;
        int res = 0;
        try
        {
            stmt = con.prepareStatement(ConfOrdenesQuerys.getQueryActualizaConfOrdenes(esquema));
            stmt.setString(1,LLAVE_FINPDF );
            stmt.setString(2,"LLAVE_FINPDF");
            stmt.executeUpdate();
            
            stmt.setString(1,LLAVE_ORDENES );
            stmt.setString(2,"LLAVE_ORDENES");
            stmt.executeUpdate();
            
            stmt.setString(1,LLAVE_FIN_ORDENES );
            stmt.setString(2,"LLAVE_FIN_ORDENES");
            stmt.executeUpdate();
            
            stmt.setString(1,LLAVE_TOTAL );
            stmt.setString(2,"LLAVE_TOTAL");
            stmt.executeUpdate();
            
            stmt.setString(1,LLAVE_FIN_TOTAL );
            stmt.setString(2,"LLAVE_FIN_TOTAL");
            stmt.executeUpdate();
            
            stmt.setString(1,LLAVE_VENDOR );
            stmt.setString(2,"LLAVE_VENDOR");
            stmt.executeUpdate();
            
            stmt.setString(1,LLAVE_FIN_VENDOR );
            stmt.setString(2,"LLAVE_FIN_VENDOR");
            stmt.executeUpdate();
            
            stmt.setString(1,LLAVE_MONEDA );
            stmt.setString(2,"LLAVE_MONEDA");
            stmt.executeUpdate();
            
            stmt.setString(1,LLAVE_FIN_MONEDA );
            stmt.setString(2,"LLAVE_FIN_MONEDA");
            stmt.executeUpdate();
            res = 9;
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
