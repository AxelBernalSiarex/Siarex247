package com.siarex247.configuraciones.ListaNegra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.siarex247.utils.Utils;

public class EnviarCorreoListaBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	private final String DISPONIBLE_ = "DISPONIBLE_";
	
	
	public HashMap<String, JSONObject> obtenerEstatusActual(Connection con, String esquema, int anioListaInicial, int anioListaFinal){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        HashMap<String, JSONObject> mapaResultado = new HashMap<>();
        StringBuffer sbQuery = new StringBuffer();
        StringBuffer sbQueryFinal = new StringBuffer();
        
        ListaNegraBean listaNegraBean = new ListaNegraBean();
        
        //logger.info("********** CALCULANDO MAPA DE ESTATUS ******************");
        try{ // 
        	ArrayList<String> columnasFechas = listaNegraBean.consultaColumnas(esquema, anioListaInicial);
        	sbQuery.append("select B.RFC, B.RAZON_SOCIAL").append(columnasFechas.size() > 0 ? ", " : " ");
        	
        	int contColumnas = 1;
        	int numColActual = 0;
        	for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(DISPONIBLE_+contColumnas).append(",");
        		numColActual++;
        	}
        	
        	sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from siarex_accesos.LISTA_NEGRA_SAT A inner join PROVEEDORES B on A.RFC = B.RFC ");
        	
        	int contParam = 1;
        	
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? ");
        	
        	stmt = con.prepareStatement( sbQueryFinal.toString());
        	
    		stmt.setString(contParam, anioListaInicial+"-01-01 01:01:01");
    		contParam++;
    		stmt.setString(contParam, anioListaFinal+"-12-31 23:59:59");
    		contParam++;

    		// logger.info("stmt Mapa===>"+stmt);
            rs = stmt.executeQuery();
            String RFC = null;
            String DISPONIBLE_ACTUAL = null;
            String desEstatus = null;
            String desEstatusAnterior = null;
			while(rs.next()){
				desEstatus = null;
				desEstatusAnterior = "";
				
					RFC = Utils.noNulo(rs.getString(1));
					jsonobj.put("RFC",RFC);
					jsonobj.put("RAZON_SOCIAL",Utils.noNuloNormal(rs.getString(2)));
					DISPONIBLE_ACTUAL = Utils.noNulo(rs.getString(numColActual)); // se obtiene la columna del disponinle actual
					if ("0".equalsIgnoreCase(DISPONIBLE_ACTUAL)) {
						desEstatus = "Correcto";
					}else {
						desEstatus = "Proveedor Encontrado en Lista Negra";
					}
					
					for (int x = 3; x < numColActual-1; x++) {
						DISPONIBLE_ACTUAL = Utils.noNulo(rs.getString(x));
						if ("1".equalsIgnoreCase(DISPONIBLE_ACTUAL)) {
							desEstatusAnterior = "Proveedor Encontrado en Lista Negra";
							break;
						}else {
							desEstatusAnterior = "";
						}
					}
					jsonobj.put("ESTATUS_ACTUAL",desEstatus);
					jsonobj.put("ESTATUS_ANTERIOR",desEstatusAnterior);
					
					logger.info("jsonobj metiendo....."+jsonobj);
					mapaResultado.put(RFC, jsonobj);
					jsonobj = new JSONObject();	
			}
		
			//logger.info("retornando mapa....."+mapaResultado);
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
        return mapaResultado;
    }
	
	
	
	public HashMap<String, JSONObject> obtenerEstatusHistorico(Connection con, String esquema, HashMap<String, JSONObject> mapaResultado, int anioListaInicial, int anioListaFinal){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        
        
        StringBuffer sbQuery = new StringBuffer();
        StringBuffer sbQueryFinal = new StringBuffer();
        
        ListaNegraBean listaNegraBean = new ListaNegraBean();
        
        try{ // 
        	ArrayList<String> columnasFechas = listaNegraBean.consultaColumnas(esquema, anioListaFinal);
        	sbQuery.append("select B.RFC, B.RAZON_SOCIAL, ");
        	
        	int contColumnas = 1;
        	int numColActual = 0;
        	for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(DISPONIBLE_+contColumnas).append(",");
        		numColActual++;
        	}
        	sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from siarex_accesos.LISTA_NEGRA_SAT_HISTORICO A inner join PROVEEDORES B on A.RFC = B.RFC ");
        	
        	int contParam = 1;
        	
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? ");
        	
        	stmt = con.prepareStatement( sbQueryFinal.toString());
        	
    		stmt.setString(contParam, anioListaInicial+"-01-01 01:01:01");
    		contParam++;
    		stmt.setString(contParam, anioListaFinal+"-12-31 23:59:59");
    		contParam++;

           // logger.info("Query----->"+stmt );
            rs = stmt.executeQuery();
            String RFC = null;
            String DISPONIBLE_ACTUAL = null;
            String VACIO = "";
            String desEstatusAnterior = null;
            boolean buscarEncontradoRFC = false;
            boolean buscarNoEncontradoRFC = false;
			while(rs.next()){
				
				desEstatusAnterior = "";
				buscarEncontradoRFC = false;
				buscarNoEncontradoRFC = false;
				
					RFC = Utils.noNulo(rs.getString(1));
					if (mapaResultado.get(RFC) == null) {
						buscarNoEncontradoRFC = true;
						buscarEncontradoRFC = true;
						jsonobj = new JSONObject();
						jsonobj.put("RFC",RFC);
						jsonobj.put("RAZON_SOCIAL",Utils.noNuloNormal(rs.getString(2)));
						jsonobj.put("ESTATUS_ANTERIOR","");
						
					}else {
						jsonobj = mapaResultado.get(RFC);
						// logger.info("jsonobj===>"+jsonobj);
						desEstatusAnterior = jsonobj.get("ESTATUS_ANTERIOR").toString();
						if ("".equals(desEstatusAnterior)) {
							buscarEncontradoRFC = true;
						}
					}
					
					if (buscarEncontradoRFC) {
						for (int x = 3; x < numColActual; x++) {
							DISPONIBLE_ACTUAL = Utils.noNulo(rs.getString(x));
							if ("1".equalsIgnoreCase(DISPONIBLE_ACTUAL)) {
								desEstatusAnterior = "Proveedor Encontrado en Lista Negra";
								break;
							}else {
								desEstatusAnterior = "";
							}
						}
						if (buscarNoEncontradoRFC) {
							jsonobj.put("ESTATUS_ACTUAL","Correcto");	
						}
						jsonobj.put("ESTATUS_ANTERIOR",desEstatusAnterior);
					}
					
					mapaResultado.put(RFC, jsonobj);
					
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
        return mapaResultado;
    }
	
}
