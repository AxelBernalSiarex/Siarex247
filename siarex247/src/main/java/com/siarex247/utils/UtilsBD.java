package com.siarex247.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.siarex247.catalogos.Proveedores.ProveedoresQuerys;
import com.siarex247.layOut.Tareas.TareasQuerys;

public class UtilsBD {


	public static String buscarRazonProveedor(Connection con, String esquema, Integer claveProveedor)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String razonSocial = "";
        try
        {
            stmt = con.prepareStatement(ProveedoresQuerys.getQueryInfoBuscaRazon(esquema));
            stmt.setInt(1, claveProveedor);
            rs = stmt.executeQuery();
			if(rs.next()) 
            {
				razonSocial = Utils.regresaCaracteresHTML(rs.getString(4)); 
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
        return razonSocial;
    }
	
	
	
	public static String [] obtenerCorreorProveedor(Connection con, String esquema, int claveRegistro, String bandPagos, String bandOrdenes, String bandOutSourcing, String bandTodos)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String [] listaCorreos = {"N","N","N","N","N","N","N","N","N","N"}; 
        try
        {
            stmt = con.prepareStatement(ProveedoresQuerys.getQueryInfoProveeEmail(esquema));
            stmt.setInt(1, claveRegistro);

            rs = stmt.executeQuery();
            int x = 0;
            String bandPagosRS = null;
            String bandOrdenRS = null;
            String tipoCorreoRS = null;
			while(rs.next())
            {
				tipoCorreoRS = Utils.noNulo(rs.getString(2));
				bandPagosRS = Utils.noNulo(rs.getString(3));
				bandOrdenRS = Utils.noNulo(rs.getString(4));
				if ("P".equalsIgnoreCase(tipoCorreoRS)){
					listaCorreos[x] = Utils.noNuloNormal(rs.getString(1));
					x++;
				}else if ("S".equalsIgnoreCase(bandPagos)){
					if (bandPagosRS.equalsIgnoreCase("S")){
						listaCorreos[x] = Utils.noNuloNormal(rs.getString(1));
						x++;
					}
						
				}else if ("S".equalsIgnoreCase(bandOrdenes)){
					if (bandOrdenRS.equalsIgnoreCase("S")){
						listaCorreos[x] = Utils.noNuloNormal(rs.getString(1));
						x++;
					}
				}else if ("S".equalsIgnoreCase(bandTodos)) {
					listaCorreos[x] = Utils.noNuloNormal(rs.getString(1));
					x++;
				}else if ("S".equalsIgnoreCase(bandOutSourcing)) {
					listaCorreos[x] = Utils.noNuloNormal(rs.getString(1));
					x++;
				}
            }
        }
        catch(Exception e){
            Utils.imprimeLog("obteniendo al proveedor ", e);
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
        return listaCorreos;
    }
	
	
	public static String [] emailUsuario(Connection con, String esquema, String claveUsuario, String tipoAcceso)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String datosUsuario [] = {"","", ""}; 
        try
        {
        	if (tipoAcceso.equals("PROVEEDOR")){
        		stmt = con.prepareStatement(TareasQuerys.getQueryMailUsuarioAcceso(esquema));	
        	}else if (tipoAcceso.equals("SUPERVISOR")){
        		stmt = con.prepareStatement(TareasQuerys.getQueryMailUsuarioAccesoSup(esquema));
        	}
            
            stmt.setString(1, claveUsuario);
            rs = stmt.executeQuery();
            
            if (rs.next()){
            	datosUsuario[0] = Utils.noNuloNormal(rs.getString(1));
            	datosUsuario[1] = Utils.noNuloNormal(rs.getString(2));
            	datosUsuario[2] = Utils.noNuloNormal(rs.getString(3));
            }
        }
        catch(Exception e){
            Utils.imprimeLog("obteniendo al proveedor ", e);
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
        return datosUsuario;
    }

}
