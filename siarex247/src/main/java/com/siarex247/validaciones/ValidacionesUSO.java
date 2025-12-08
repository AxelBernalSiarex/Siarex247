package com.siarex247.validaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.sat.Catalogos.CatalogosBean;
import com.siarex247.utils.Utils;


public class ValidacionesUSO {

	public static final Logger logger = Logger.getLogger("siarex247");

	
	public static boolean validarUsoRFC(Connection con, String esquema, String rfc)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean existe = false; 
        try
        {

        	StringBuffer sbQuery = new StringBuffer(ValidacionesQuerys.getQueryValidaRFC(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setString(1, rfc);
            rs = stmt.executeQuery();
            
			if(rs.next()){
				existe = true;
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
	            stmt = null;
	        }
        }
        return existe;
    }
	
	
	public static boolean validarUsoCDFI(Connection con, String esquema, String rfc, String usoCFDI)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean existe = false; 
        try
        {

        	StringBuffer sbQuery = new StringBuffer(ValidacionesQuerys.getQueryValidaUSO(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setString(1, rfc);
            stmt.setString(2, usoCFDI);
            rs = stmt.executeQuery();
            
			if(rs.next()){
				existe = true;
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
	            stmt = null;
	        }
        }
        return existe;
    }
	
	
	
	public static boolean validarUsoClave(Connection con, String esquema, String rfc, String usoCFDI, JSONArray jsonConceptos, long folioEmpresa)
    {
        PreparedStatement stmt = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtDelete = null;
        ResultSet rs = null;
        boolean existe = true; 
        JSONObject jsonobj = null;
        try{
       	
        	stmtDelete = con.prepareStatement(ValidacionesQuerys.getQueryDeleteClaveProductoXML(esquema, ""));
        	stmtDelete.setLong(1, folioEmpresa);
        	stmtDelete.executeUpdate();
        	
        	stmtInsert = con.prepareStatement(ValidacionesQuerys.getQueryInsertClaveProductoXML(esquema, ""));
        	for (int x = 0; x < jsonConceptos.size(); x++) {
        		jsonobj = (JSONObject) jsonConceptos.get(x);
        		try {
        			stmtInsert.setLong(1, folioEmpresa);
    				stmtInsert.setString(2, usoCFDI);
    				stmtInsert.setString(3, jsonobj.get("ClaveProdServ").toString());
    				try {
    					stmtInsert.setDouble(4, Double.parseDouble(jsonobj.get("Importe").toString()));	
    				}catch(Exception e) {
    					stmtInsert.setDouble(4, 0);
    				}
    				
    				stmtInsert.setString(5, jsonobj.get("ValorUnitario").toString());
    				stmtInsert.setString(6, jsonobj.get("Descripcion").toString());
    				stmtInsert.setString(7, jsonobj.get("Unidad").toString());
    				stmtInsert.setString(8, jsonobj.get("ClaveUnidad").toString());
    				stmtInsert.setString(9, jsonobj.get("Cantidad").toString());
    				stmtInsert.setString(10, jsonobj.get("NoIdentificacion").toString());
    				stmtInsert.executeUpdate();
        		}catch(Exception e) {
        			Utils.imprimeLog("", e);
        		}
        	}
        	
// select CLAVE_PROD_SERV from CLAVE_PRODUC_SERVICIO_XML where FOLIO_EMPRESA = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI where RFC = ? and USO_CFDI = ?)        	
        	StringBuffer sbQuery = new StringBuffer(ValidacionesQuerys.getQueryValidaCLAVE(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setLong(1, folioEmpresa);
            stmt.setString(2, rfc);
            stmt.setString(3, usoCFDI);
            rs = stmt.executeQuery();
            
			if(rs.next()){
				existe = false;
            }
        }catch(Exception e){
           Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            
	            if(stmtDelete != null)
	            	stmtDelete.close();
	            stmtDelete = null;
	            
	            if(stmtInsert != null)
	            	stmtInsert.close();
	            stmtInsert = null; 
	            
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return existe;
    }
	
	
	
	public static boolean validarUsoClaveMultiple(Connection con, String esquema, String rfc, String usoCFDI, JSONArray jsonConceptos, String idMultiple)
    {
        PreparedStatement stmt = null;
        PreparedStatement stmtInsert = null;
        ResultSet rs = null;
        boolean existe = true; 
        JSONObject jsonobj = null;
        try{
       	        	
        	stmtInsert = con.prepareStatement(ValidacionesQuerys.getQueryInsertClaveProductoXML_Multiple(esquema));
        	stmtInsert.setString(1, idMultiple);
			stmtInsert.setString(2, usoCFDI);
			
        	for (int x = 0; x < jsonConceptos.size(); x++) {
        		jsonobj = (JSONObject) jsonConceptos.get(x);
        		try {
    				stmtInsert.setString(3, jsonobj.get("ClaveProdServ").toString());
    				try {
    					stmtInsert.setDouble(4, Double.parseDouble(jsonobj.get("Importe").toString()));	
    				}catch(Exception e) {
    					stmtInsert.setDouble(4, 0);
    				}
    				stmtInsert.setString(5, jsonobj.get("ValorUnitario").toString());
    				stmtInsert.setString(6, jsonobj.get("Descripcion").toString());
    				stmtInsert.setString(7, jsonobj.get("Unidad").toString());
    				stmtInsert.setString(8, jsonobj.get("ClaveUnidad").toString());
    				stmtInsert.setString(9, jsonobj.get("Cantidad").toString());
    				stmtInsert.setString(10, jsonobj.get("NoIdentificacion").toString());
    				stmtInsert.executeUpdate();
        		}catch(Exception e) {
        			Utils.imprimeLog("", e);
        		}
        	}
        	
        	
        	StringBuffer sbQuery = new StringBuffer(ValidacionesQuerys.getQueryValidaCLAVE_Multiple(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setString(1, idMultiple);
            stmt.setString(2, rfc);
            stmt.setString(3, usoCFDI);
            rs = stmt.executeQuery();
            
			if(rs.next()){
				existe = false;
            }
        }catch(Exception e){
           Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            
	            if(stmtInsert != null)
	            	stmtInsert.close();
	            stmtInsert = null; 
	            
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return existe;
    }
	
	public static boolean existeCFDI(String usoCFDI_XML) {
        boolean existe = true; 
        CatalogosBean catalogosBean = new CatalogosBean();
        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rcSat = null;
        Connection con = null;
        try {
        	
        	rcSat  = connPool.getConnectionSAT();
        	con = rcSat.getCon();
        	String descripcion = catalogosBean.consultaUsoCFDI(con, rcSat.getEsquema(), usoCFDI_XML);
        	if ("".equalsIgnoreCase(descripcion)) {
        		existe = false;
        	}
        	
        }catch(Exception e){
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
        return existe;
    }
		
	
	
}
