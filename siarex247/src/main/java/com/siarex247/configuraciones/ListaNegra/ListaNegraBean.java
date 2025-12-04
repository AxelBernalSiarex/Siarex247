package com.siarex247.configuraciones.ListaNegra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class ListaNegraBean {

	
	
	@SuppressWarnings("unchecked")
	public JSONArray detalleListaConf(Connection con, String esquema)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{ 
            stmt = con.prepareStatement( ListaNegraQuerys.getDetalleListaConf(esquema));
            rs = stmt.executeQuery();
            String tipoRegistro = null;
			while(rs.next()){
				//TIPO_DIA, DIA_MES
				tipoRegistro = Utils.noNulo(rs.getString(2));
				if ("NOR".equalsIgnoreCase(tipoRegistro)) {
					jsonobj.put("TIPO_DIA","Dia del Mes");
					jsonobj.put("DIA_MES",Utils.noNulo(rs.getString(3)));
				}else {
					jsonobj.put("TIPO_DIA","Fin del Mes");
					jsonobj.put("DIA_MES","");
				}
				jsonobj.put("ID_REGISTRO",rs.getInt(1));
				
				
				
				jsonArray.add(jsonobj);
				jsonobj = new JSONObject();
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
        return jsonArray;
    }
	
	@SuppressWarnings("unchecked")
	public JSONObject buscarListaConf(Connection con, String esquema, int idRegistro)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        try{ 
            stmt = con.prepareStatement( ListaNegraQuerys.getConsultarListaConf(esquema));
            stmt.setInt(1, idRegistro);
            rs = stmt.executeQuery();
			if(rs.next()){
				jsonobj.put("ID_REGISTRO",rs.getInt(1));
				jsonobj.put("DIA_MES",Utils.noNulo(rs.getString(3)));
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
        return jsonobj;
    }
	
	public int altaListaNegra(Connection con, String esquema, String diaMes){
        PreparedStatement stmt = null;
        int totReg = 0;
        try{ 
            stmt = con.prepareStatement( ListaNegraQuerys.getGuardarListaNegra(esquema));
            
            String tipoDia = "NOR";
            
            if ("100".equalsIgnoreCase(diaMes)) {
            	tipoDia = "FIN";
            	diaMes = "0";
            }
            
            stmt.setString(1, tipoDia);
            stmt.setString(2, diaMes);
            totReg = stmt.executeUpdate();
            
        }catch(Exception e){
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
        return totReg;
    }
	
	
	public int modificarListaNegra(Connection con, String esquema, int idRegistro, String diaMes){
        PreparedStatement stmt = null;
        int totReg = 0;
        try{ 
            stmt = con.prepareStatement( ListaNegraQuerys.getActualizarListaNegra(esquema));
            
            String tipoDia = "NOR";
            
            if ("100".equalsIgnoreCase(diaMes)) {
            	tipoDia = "FIN";
            	diaMes = "0";
            }
            
            stmt.setString(1, tipoDia);
            stmt.setString(2, diaMes);
            stmt.setInt(3, idRegistro);
            totReg = stmt.executeUpdate();
            
        }catch(Exception e){
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
        return totReg;
    }
	
	public int eliminaListaNegra(Connection con, String esquema, int idRegistro){
        PreparedStatement stmt = null;
        int totReg = 0;
        try{ 
            stmt = con.prepareStatement( ListaNegraQuerys.getEliminaListaNegra(esquema));
            stmt.setInt(1, idRegistro);
            totReg = stmt.executeUpdate();
            
        }catch(Exception e){
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
        return totReg;
    }

	

	public ArrayList<String> consultaColumnas(String esquema, int annio){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
        Date fechaActual = new Date();
        ArrayList<String> listaFechas = new ArrayList<>();
        ResultadoConexion rc = null;
        ConexionDB connPool = new ConexionDB();
        Connection con = null;
        
        try{ 
        	rc = connPool.getConnectionSiarex();
        	con  = rc.getCon();
        	
        	int year = Integer.parseInt(formatDate.format(fechaActual));
        	if (annio > 0) {
        		year = annio;	
        	}
        	
            stmt = con.prepareStatement( ListaNegraQuerys.getConsultaColumnas(esquema));
            stmt.setInt(1, year);
            rs = stmt.executeQuery();
			while(rs.next()){
				listaFechas.add(Utils.noNulo(rs.getString(1)).substring(0, 10) );
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
	            if(con != null)
	                con.close();
	            con = null;
	        }catch(Exception e){
	        	rs = null;
	            stmt = null;
	            con = null;
	        }
        }
        return listaFechas;
    }
	
	
}
