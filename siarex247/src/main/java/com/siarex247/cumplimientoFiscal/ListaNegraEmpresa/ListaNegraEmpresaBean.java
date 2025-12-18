package com.siarex247.cumplimientoFiscal.ListaNegraEmpresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class ListaNegraEmpresaBean {

	private final String DISPONIBLE_ = "DISPONIBLE_";
	private final String pipe = "|";
	private final String NO_ENCONTRADO = "NO ENCONTRADO";
	private final String RFC_CON_PROBLEMAS = "RFC CON PROBLEMAS";
	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleListaNegra(Connection con, String esquema, String nombreEmpresa, String razonSocial, String rfcListaNegra, 
			String idSupuesto, String idNombreArticulo, String idEstatus, int anioLista, String tipoFacturas, boolean bandBusqueda, int starPaginado, int endPaginado)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        StringBuffer sbQuery = new StringBuffer();
        StringBuffer sbQueryFinal = new StringBuffer();
        try{ //
        	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
            Date fechaActual = new Date();
            
        	int yearActual = Integer.parseInt(formatDate.format(fechaActual));
        	if (anioLista == 0) {
        		anioLista = yearActual;
        	}
        	// logger.info("anioLista===>"+anioLista);
        	ArrayList<String> columnasFechas = consultaColumnas(anioLista);
        	HashMap<String, String> mapaRFC = consultaProveedores(nombreEmpresa, tipoFacturas);
        	
        	
        	sbQuery.append(ListaNegraEmpresaQuerys.getDetalleListaNegra(esquema));
        	
        	int contColumnas = 1;
        	
        	for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(DISPONIBLE_+contColumnas).append(",");
        		contColumnas++;
        	}
        	
        	if (yearActual == anioLista) {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from LISTA_NEGRA_SAT ");	
        	}else {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from LISTA_NEGRA_SAT_HISTORICO ");
        	}
        	int contParam = 1;
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? and RFC in (");
        	
        	
        	List<String> listLlaves = new ArrayList<>(mapaRFC.keySet());
			for (int x = 0; x < listLlaves.size(); x++) {
				sbQueryFinal.append("?,");
			}
        	
			String queryIn = sbQueryFinal.substring(0, sbQueryFinal.length() - 1);
			sbQueryFinal.setLength(0);
			sbQueryFinal.append(queryIn).append(") ");
        	
			
        	if (!"".equalsIgnoreCase(Utils.noNulo(razonSocial))) {
        		sbQueryFinal.append(" and RAZON_SOCIAL  like '%"+Utils.noNulo(razonSocial)+"%' ");
        	}
        	
        	if (!"".equalsIgnoreCase(Utils.noNulo(rfcListaNegra))) {
        		sbQueryFinal.append(" and RFC = ? ");
        	}

        	if (!"".equalsIgnoreCase(Utils.noNulo(idSupuesto))) {
        		sbQueryFinal.append(" and SUPUESTO = ? ");
        	}
        	
        	
        	if (!"".equalsIgnoreCase(Utils.noNulo(idNombreArticulo))) {
        		sbQueryFinal.append(" and NOMBRE_ARTICULO = ? ");
        	}
        	
        	//sbQueryFinal.append(" limit 20000" + starPaginado + ", "+endPaginado);	
        	if (!bandBusqueda) {
        		sbQueryFinal.append(" limit " + starPaginado + ", "+endPaginado);	
        	}
        	
        	stmt = con.prepareStatement( sbQueryFinal.toString());
        	//logger.info("anioLista----->" + anioLista);
    		stmt.setString(contParam, anioLista+"-01-01 01:01:01");
    		contParam++;
    		stmt.setString(contParam, anioLista+"-12-31 23:59:59");
    		contParam++;

    		for (int x = 0; x < listLlaves.size(); x++) {
    			stmt.setString(contParam, listLlaves.get(x));
    			contParam++;
			}
    		
    		if (!"".equalsIgnoreCase(rfcListaNegra)) {
        		stmt.setString(contParam, rfcListaNegra);
        		contParam++;
        	}
        	
        	if (!"".equalsIgnoreCase(idSupuesto)) {
        		stmt.setString(contParam, idSupuesto);
        		contParam++;
        	}
        	
        	if (!"".equalsIgnoreCase(idNombreArticulo)) {
        		stmt.setString(contParam, idNombreArticulo);
        		contParam++;
        	}
        	
           // logger.info("stm----->"+stmt );
            rs = stmt.executeQuery();
            contColumnas = 1;
            
            
           // HashMap<String, String> mapaDisponible = null;
            String VALOR_DISPONIBLE = null;
            String RFC = null;
           // logger.info("columnasFechas===>"+columnasFechas);
            String rfcProveedor = "";
            
           // logger.info("mapaRFC ----->"+mapaRFC);
            
			while(rs.next()){
				int numColDisponible = 5;
				contColumnas = 1;
				RFC = Utils.noNulo(rs.getString(1));
				jsonobj.put("RFC",RFC);
				jsonobj.put("RAZON_SOCIAL",remplazaDinero(Utils.noNulo(rs.getString(2))));
				jsonobj.put("SUPUESTO",Utils.noNulo(rs.getString(3)));
				jsonobj.put("NOMBRE_ARTICULO",Utils.noNuloNormal(rs.getString(4)));
				// mapaDisponible = mapaRFC.get(RFC);
				if (mapaRFC == null || mapaRFC.isEmpty()) {
					for (int x = 0; x < columnasFechas.size(); x++) {
						jsonobj.put(DISPONIBLE_+contColumnas, NO_ENCONTRADO);
						contColumnas++;
					}
				}else {
					rfcProveedor = mapaRFC.get(RFC);
//					logger.info("rfcProveedor ----->"+rfcProveedor);
					for (int x = 0; x < columnasFechas.size(); x++) {
						// String llaveDisponible = DISPONIBLE_+contColumnas;
						VALOR_DISPONIBLE = Utils.noNulo(rs.getString(numColDisponible));
//						logger.info("VALOR_DISPONIBLE===>"+VALOR_DISPONIBLE+", contColumnas==>"+contColumnas);
						if (rfcProveedor == null) {
							jsonobj.put(DISPONIBLE_+contColumnas, NO_ENCONTRADO);
						}else if ("0".equalsIgnoreCase(VALOR_DISPONIBLE)) {
							jsonobj.put(DISPONIBLE_+contColumnas, NO_ENCONTRADO );
						}else if ("1".equalsIgnoreCase(VALOR_DISPONIBLE)) {
							jsonobj.put(DISPONIBLE_+contColumnas, RFC_CON_PROBLEMAS );
						}
						
						contColumnas++;
						numColDisponible++;
					}
				}
				//logger.info("jsonobj==>"+jsonobj);
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

	
	public int obtenerTotal(Connection con, String esquema, String nombreEmpresa, String razonSocial, String rfcListaNegra, String idSupuesto, String idNombreArticulo, String idEstatus, int anioLista, String tipoFacturas)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sbQuery = new StringBuffer();
        StringBuffer sbQueryFinal = new StringBuffer();
        int totRegistro = 0;
        try{ //
        	
        	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
            Date fechaActual = new Date();
            
            HashMap<String, String> mapaRFC = consultaProveedores(nombreEmpresa, tipoFacturas);
            
        	int yearActual = Integer.parseInt(formatDate.format(fechaActual));
        	if (anioLista == 0) {
        		anioLista = yearActual;
        	}

        	sbQuery.append(ListaNegraEmpresaQuerys.getTotalRegistros(esquema));
        	
        	if (yearActual == anioLista) {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from LISTA_NEGRA_SAT ");	
        	}else {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from LISTA_NEGRA_SAT_HISTORICO ");
        	}
        	
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? and RFC in (");
        	int contParam = 1;
        	
        	List<String> listLlaves = new ArrayList<>(mapaRFC.keySet());
			for (int x = 0; x < listLlaves.size(); x++) {
				sbQueryFinal.append("?,");
			}
        	
			String queryIn = sbQueryFinal.substring(0, sbQueryFinal.length() - 1);
			sbQueryFinal.setLength(0);
			sbQueryFinal.append(queryIn).append(") ");
        	
			
			
        	if (!"".equalsIgnoreCase(razonSocial)) {
        		sbQueryFinal.append(" and RAZON_SOCIAL  like '%"+razonSocial+"%' ");
        	}
        	
        	if (!"".equalsIgnoreCase(rfcListaNegra)) {
        		sbQueryFinal.append(" and RFC = ? ");
        	}

        	if (!"".equalsIgnoreCase(idSupuesto)) {
        		sbQueryFinal.append(" and SUPUESTO = ? ");
        	}
        	
        	
        	if (!"".equalsIgnoreCase(idNombreArticulo)) {
        		sbQueryFinal.append(" and NOMBRE_ARTICULO = ? ");
        	}
        	
        	stmt = con.prepareStatement( sbQueryFinal.toString());
        	//logger.info("anioLista----->" + anioLista);
    		stmt.setString(contParam, anioLista+"-01-01 01:01:01");
    		contParam++;
    		stmt.setString(contParam, anioLista+"-12-31 23:59:59");
    		contParam++;

    		for (int x = 0; x < listLlaves.size(); x++) {
    			stmt.setString(contParam, listLlaves.get(x));
    			contParam++;
			}
    		
    		if (!"".equalsIgnoreCase(rfcListaNegra)) {
        		stmt.setString(contParam, rfcListaNegra);
        		contParam++;
        	}
        	
        	if (!"".equalsIgnoreCase(idSupuesto)) {
        		stmt.setString(contParam, idSupuesto);
        		contParam++;
        	}
        	
        	if (!"".equalsIgnoreCase(idNombreArticulo)) {
        		stmt.setString(contParam, idNombreArticulo);
        		contParam++;
        	}
        	
        	//logger.info("stmtTotal===>"+stmt);
            rs = stmt.executeQuery();
            
			if (rs.next()){
				totRegistro = rs.getInt(1);
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
        return totRegistro;
    }
	
	public ArrayList<String> consultaColumnas(int annio){
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
        	
            stmt = con.prepareStatement( ListaNegraEmpresaQuerys.getConsultaColumnas(rc.getEsquema()));
            stmt.setInt(1, year);
            
           //  logger.info("stmt===>"+stmt);
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
	
	
	public HashMap<String, String> consultaProveedores(String esquemaEmpresa, String tipoFacturas){
        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rcEmpresa = null;
        Connection conEmpresa = null;
        
        PreparedStatement stmtEmitidos = null;
        ResultSet rsEmitidos = null;
        
        PreparedStatement stmtRecibidos = null;
        ResultSet rsRecibidos = null;
        
        HashMap<String, String> mapaRFC = new HashMap<>();
        
        try{ 
        	// Emitidos
        	// Recibidos
        	String rfcRS = null;
        	rcEmpresa = connPool.getConnection(esquemaEmpresa);
        	conEmpresa = rcEmpresa.getCon();
        	
        	if ("".equalsIgnoreCase(Utils.noNulo(tipoFacturas)) || "E".equalsIgnoreCase(tipoFacturas)) {
            	stmtEmitidos = conEmpresa.prepareStatement(ListaNegraEmpresaQuerys.getProveedoresEmitidos(rcEmpresa.getEsquema()));
            	rsEmitidos = stmtEmitidos.executeQuery();
    			while(rsEmitidos.next()){
    				rfcRS = Utils.noNulo(rsEmitidos.getString(1));
    				mapaRFC.put(rfcRS, rfcRS);
    			}
        	}
        	
        	if ("".equalsIgnoreCase(Utils.noNulo(tipoFacturas)) || "R".equalsIgnoreCase(tipoFacturas)) {
    			stmtRecibidos = conEmpresa.prepareStatement(ListaNegraEmpresaQuerys.getProveedoresRecibidos(rcEmpresa.getEsquema()));
            	
    			rsRecibidos = stmtRecibidos.executeQuery();
    			while(rsRecibidos.next()){
    				rfcRS = Utils.noNulo(rsRecibidos.getString(1));
    				mapaRFC.put(rfcRS, rfcRS);
    				
    			}
        	}
			
        }
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(rsEmitidos != null)
	            	rsEmitidos.close();
	            rsEmitidos = null;
	            if(rsRecibidos != null)
	            	rsRecibidos.close();
	            rsRecibidos = null;
	            if(stmtEmitidos != null)
	            	stmtEmitidos.close();
	            stmtEmitidos = null;
	            if(stmtRecibidos != null)
	            	stmtRecibidos.close();
	            stmtRecibidos = null;
	            if(conEmpresa != null)
	            	conEmpresa.close();
	            conEmpresa = null;
	            
	        }catch(Exception e){
	        	rsEmitidos = null;
	        	stmtEmitidos = null;
	            conEmpresa = null;
	        }
        }
        return mapaRFC;
    }
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> detalleListaNegraCSV(Connection con, String esquema, String nombreEmpresa, String razonSocial, String rfcListaNegra, String idSupuesto, String idNombreArticulo, String idEstatus, int anioLista, String tipoFacturas)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
       // JSONObject jsonobj = new JSONObject();
        //JSONArray jsonArray = new JSONArray();
        StringBuffer sbQuery = new StringBuffer();
        StringBuffer sbQueryFinal = new StringBuffer();
        
        StringBuffer sbLine1 = new StringBuffer("|RFC|RAZON SOCIAL|SUPUESTO|NOMBRE DEL ARTICULO|"); 
        ArrayList<String> listaTXT = new ArrayList<String>();
        
        try{ //
        	
        	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
            Date fechaActual = new Date();
            
        	int yearActual = Integer.parseInt(formatDate.format(fechaActual));
        	if (anioLista == 0) {
        		anioLista = yearActual;
        	}
        	
        	
        	ArrayList<String> columnasFechas = consultaColumnas(anioLista);
        	HashMap<String, String> mapaRFC = consultaProveedores(nombreEmpresa, tipoFacturas);
        	sbQuery.append(ListaNegraEmpresaQuerys.getDetalleListaNegra(esquema));
        	
        	int contColumnas = 1;
        	
        	for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(DISPONIBLE_+contColumnas).append(",");
        		contColumnas++;
        		sbLine1.append(columnasFechas.get(x)).append(pipe);
        	}
        	listaTXT.add(sbLine1.toString());
        	sbLine1.setLength(0);
        	sbLine1.append(pipe);
        	
        	if (yearActual == anioLista) {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from LISTA_NEGRA_SAT ");	
        	}else {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from LISTA_NEGRA_SAT_HISTORICO ");
        	}
        	
        	int contParam = 1;
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? and RFC in (");
        	
        	
        	List<String> listLlaves = new ArrayList<>(mapaRFC.keySet());
			for (int x = 0; x < listLlaves.size(); x++) {
				sbQueryFinal.append("?,");
			}
        	
			String queryIn = sbQueryFinal.substring(0, sbQueryFinal.length() - 1);
			sbQueryFinal.setLength(0);
			sbQueryFinal.append(queryIn).append(") ");
        	
			
        	
        	if (!"".equalsIgnoreCase(razonSocial)) {
        		sbQueryFinal.append(" and RAZON_SOCIAL  like '%"+razonSocial+"%' ");
        	}
        	
        	if (!"".equalsIgnoreCase(rfcListaNegra)) {
        		sbQueryFinal.append(" and RFC = ? ");
        	}

        	if (!"".equalsIgnoreCase(idSupuesto)) {
        		sbQueryFinal.append(" and SUPUESTO = ? ");
        	}
        	
        	
        	if (!"".equalsIgnoreCase(idNombreArticulo)) {
        		sbQueryFinal.append(" and NOMBRE_ARTICULO = ? ");
        	}

        	stmt = con.prepareStatement( sbQueryFinal.toString());
        	
    		stmt.setString(contParam, anioLista+"-01-01 01:01:01");
    		contParam++;
    		stmt.setString(contParam, anioLista+"-12-31 23:59:59");
    		contParam++;

    		for (int x = 0; x < listLlaves.size(); x++) {
    			stmt.setString(contParam, listLlaves.get(x));
    			contParam++;
			}
    		
    		if (!"".equalsIgnoreCase(rfcListaNegra)) {
        		stmt.setString(contParam, rfcListaNegra);
        		contParam++;
        	}
        	
        	if (!"".equalsIgnoreCase(idSupuesto)) {
        		stmt.setString(contParam, idSupuesto);
        		contParam++;
        	}
        	
        	if (!"".equalsIgnoreCase(idNombreArticulo)) {
        		stmt.setString(contParam, idNombreArticulo);
        		contParam++;
        	}
            rs = stmt.executeQuery();
            contColumnas = 1;
            
           
            //HashMap<String, String> mapaDisponible = null;
            String VALOR_DISPONIBLE = null;
            String RFC = null;
            String RAZON_SOCIAL = null;
            String SUPUESTO = null;
            String NOMBRE_ARTICULO = null;
            String rfcProveedor = null;
            
			while(rs.next()){
				contColumnas = 1;
				int numColDisponible = 5;
					RFC = Utils.noNulo(rs.getString(1));
					//RFC  = RFC.replaceAll("\"", "");
					RAZON_SOCIAL = remplazaDinero(Utils.noNulo(rs.getString(2)));
		            
					if (RAZON_SOCIAL.indexOf("\"") > -1) {
						RAZON_SOCIAL = RAZON_SOCIAL.replaceAll("\"", " ");
					}
					
		            SUPUESTO = Utils.noNulo(rs.getString(3));
		            NOMBRE_ARTICULO = Utils.noNuloNormal(rs.getString(4));
					
					sbLine1.append(RFC).append(pipe).append(RAZON_SOCIAL).append(pipe).append(SUPUESTO).append(pipe).append(NOMBRE_ARTICULO).append(pipe);
					
					if (mapaRFC == null || mapaRFC.isEmpty()) {
						for (int x = 0; x < columnasFechas.size(); x++) {
							sbLine1.append(NO_ENCONTRADO).append(pipe);
							contColumnas++;
						}
					}else {
						rfcProveedor = mapaRFC.get(RFC);
						for (int x = 0; x < columnasFechas.size(); x++) {
							VALOR_DISPONIBLE = Utils.noNulo(rs.getString(numColDisponible));
							if (rfcProveedor == null) {
								sbLine1.append(NO_ENCONTRADO).append(pipe);
							}else if ("0".equalsIgnoreCase(VALOR_DISPONIBLE)) {
								sbLine1.append(NO_ENCONTRADO).append(pipe);
							}else if ("1".equalsIgnoreCase(VALOR_DISPONIBLE)) {
								sbLine1.append(RFC_CON_PROBLEMAS).append(pipe);
							}
							contColumnas++;
							numColDisponible++;
						}
					}
					
					listaTXT.add(sbLine1.toString());
					sbLine1.setLength(0);
					sbLine1.append(pipe);
				
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
        return listaTXT;
    }

	private String remplazaDinero(String cadena) {
		String cadenaFinal = null;
		try {
			cadenaFinal = cadena.replace("$", ",");
		}catch(Exception e) {
			cadenaFinal = "";
		}
		return cadenaFinal;
	}
	

}
