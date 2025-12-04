package com.siarex247.configuraciones.Descartar;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsSiarex;

public class DescartarBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleDescartes(Connection con, String esquema, String lenguaje)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        try{
        	
        	StringBuffer sbQuery = new StringBuffer(DescartarQuerys.getDetalleDescartes(esquema));
        	stmt = con.prepareStatement(sbQuery.toString());
        	rs = stmt.executeQuery();
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            String razonSocial = "";
            final String VACIO = "";
            String estatusPago = null;
            while(rs.next()){
					estatusPago = Utils.noNulo(rs.getString(4)).concat(" - ").concat(UtilsSiarex.desEstatus(Utils.noNulo(rs.getString(4)), lenguaje ));
					if (estatusPago.length() == 3) {
						estatusPago = "";
					}
					razonSocial = Utils.regresaCaracteresNormales( Utils.noNuloNormal(rs.getString(1)));
					jsonobj.put("FOLIO_EMPRESA", rs.getInt(2));
					jsonobj.put("SERIE_FOLIO", Utils.noNuloNormal(rs.getString(3)));
					jsonobj.put("ESTATUS_ORDEN", estatusPago);
					jsonobj.put("TOTAL", decimal.format(rs.getDouble(5)));
					jsonobj.put("SUB-TOTAL", decimal.format(rs.getDouble(6)));
					if ("".equalsIgnoreCase(razonSocial)) {
						jsonobj.put("RAZON_SOCIAL", VACIO);
						jsonobj.put("ESTATUS_REGISTRO", "NO" );	
					}else {
						jsonobj.put("RAZON_SOCIAL", razonSocial);
						jsonobj.put("ESTATUS_REGISTRO", "SI" );
					}
					
					
					jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
				
            }
			mapaRes.put("detalle", jsonArray);
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
        return mapaRes;
    }
	
	
	
	public int eliminaDescartes(Connection con, String esquema, long folioEmpresa){
        PreparedStatement stmt = null;
        int resultado = 0;
        try{
        	//String arrRegistros [] = cadRegistros.split(";");
        	stmt = con.prepareStatement(DescartarQuerys.getDeleteDescartes(esquema));
        	//for (int x = 0; x < arrRegistros.length; x++){
        		stmt.setLong(1, folioEmpresa);
        		resultado = stmt.executeUpdate();
        	//}
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
        
        return resultado;
    }
	
	
	
	public int grabarDescarte(Connection con, String esquema, File fileTXT, String usuarioTrans, boolean eliminarRegistro)
    {
        PreparedStatement stmt = null;
        PreparedStatement stmtDelete = null;
        int totRegistro = 0;
        Scanner scanner = null;
        String folioEmpresa = "";
        //StringBuffer sbQuery = new StringBuffer();
        try{
        	
        	if (eliminarRegistro) {
        		stmtDelete = con.prepareStatement(DescartarQuerys.getEliminarTodo(esquema));
        		stmtDelete.executeUpdate();
        	}
        	
        	stmt = con.prepareStatement(DescartarQuerys.getInsertDescartes(esquema));
        	File fileInput = new File(fileTXT.getAbsolutePath()); // estp te la manda ivan
			scanner = new Scanner(fileInput);
			
        	stmt.setString(2, usuarioTrans);
        	con.setAutoCommit(false);
        	int contador = 0;
        	while (scanner.hasNext()) {
        		try {
        			folioEmpresa = scanner.nextLine();
            		stmt.setLong(1, Long.parseLong(folioEmpresa));
            		totRegistro+=stmt.executeUpdate();
            		if (contador == 100) {
            			con.commit();
            			contador = 0;
            		}else {
            			contador++;	
            		}
            		
        		}catch(Exception e) {
        			Utils.imprimeLog("", e);
        		}
        	}
        	if (contador > 0 && contador <= 99) {
        		con.commit();
        	}
        	con.setAutoCommit(true);
        	logger.info("totRegistro Descartados---->"+totRegistro);
        }
        catch(SQLException sql){
        	totRegistro = sql.getErrorCode();
        	Utils.imprimeLog("", sql);
            
        }catch(Exception e){
        	totRegistro = 100;
            Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(stmtDelete != null)
	            	stmtDelete.close();
	            stmtDelete = null;
	        }catch(Exception e){
	            stmt = null;
	            stmtDelete= null;
	        }
        }
        
        return totRegistro;
    }
	
}
