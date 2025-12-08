package com.siarex247.cumplimientoFiscal.ListaNegra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresQuerys;
import com.siarex247.utils.Utils;

public class ListaNegraBean {

	private final String DISPONIBLE_ = "DISPONIBLE_";
	private final String pipe = "|";
	private final String NO_ENCONTRADO = "NO ENCONTRADO";
	private final String ENCONTRADO = "ENCONTRADO";
	private final String RFC_CON_PROBLEMAS = "RFC CON PROBLEMAS";
	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleListaNegra(Connection con, String esquema, String nombreEmpresa, String razonSocial, String rfcListaNegra, 
			String idSupuesto, String idNombreArticulo, String idEstatus, int anioLista, boolean bandBusqueda, int starPaginado, int endPaginado)
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
        	
        	sbQuery.append(ListaNegraQuerys.getDetalleListaNegra(esquema));
        	
        	int contColumnas = 1;
        	
        	for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(DISPONIBLE_+contColumnas).append(",");
        		contColumnas++;
        	}
        	
        	// logger.info("yearActual===>"+yearActual+", anioLista===>"+anioLista);
        	if (yearActual == anioLista) {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(esquema).append(".LISTA_NEGRA_SAT ");	
        	}else {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(esquema).append(".LISTA_NEGRA_SAT_HISTORICO ");
        	}
        	int contParam = 1;
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? ");
        	
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
        	
          // logger.info("stmLista----->"+stmt );
            rs = stmt.executeQuery();
            contColumnas = 1;
            
            HashMap<String, String> mapaRFC = consultaProveedores(nombreEmpresa);
           // HashMap<String, String> mapaDisponible = null;
            String VALOR_DISPONIBLE = null;
            String RFC = null;
           // logger.info("mapaDisponible===>"+mapaDisponible);
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
					// logger.info("rfcProveedor ----->"+rfcProveedor);
					for (int x = 0; x < columnasFechas.size(); x++) {
						// String llaveDisponible = DISPONIBLE_+contColumnas;
						VALOR_DISPONIBLE = Utils.noNulo(rs.getString(numColDisponible));
						//logger.info("VALOR_DISPONIBLE===>"+VALOR_DISPONIBLE+", contColumnas==>"+contColumnas);
						if (rfcProveedor == null) {
							jsonobj.put(DISPONIBLE_+contColumnas, NO_ENCONTRADO);
						}else if ("0".equalsIgnoreCase(VALOR_DISPONIBLE)) {
							jsonobj.put(DISPONIBLE_+contColumnas, NO_ENCONTRADO );
						}else if ("1".equalsIgnoreCase(VALOR_DISPONIBLE)) {
							jsonobj.put(DISPONIBLE_+contColumnas, RFC_CON_PROBLEMAS );
						}
						
						/* if ("GOGM8011246B1".equalsIgnoreCase(rfcProveedor)) {
							
						}*/
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

	
	public int obtenerTotal(Connection con, String esquema, String razonSocial, String rfcListaNegra, String idSupuesto, String idNombreArticulo, String idEstatus, int anioLista)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sbQuery = new StringBuffer();
        StringBuffer sbQueryFinal = new StringBuffer();
        int totRegistro = 0;
        try{ //
        	
        	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
            Date fechaActual = new Date();
            
        	int yearActual = Integer.parseInt(formatDate.format(fechaActual));
        	if (anioLista == 0) {
        		anioLista = yearActual;
        	}

        	sbQuery.append(ListaNegraQuerys.getTotalRegistros(esquema));
        	
        	if (yearActual == anioLista) {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(esquema).append(".LISTA_NEGRA_SAT ");	
        	}else {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(esquema).append(".LISTA_NEGRA_SAT_HISTORICO ");
        	}
        	
        	
        	int contParam = 1;
        	
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? ");
        	
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
        	
            stmt = con.prepareStatement( ListaNegraQuerys.getConsultaColumnas(rc.getEsquema()));
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
	
	

/*
	public HashMap<String, HashMap<String, String>> consultaProveedores(String esquemaEmpresa, ArrayList<String> columnasFechas, int anioConsulta, int yearActual){
        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        ResultadoConexion rcEmpresa = null;
        
        Connection con = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, HashMap<String, String>> mapaRFC = new HashMap<>();
        HashMap<String, String> mapaDisponibles = new HashMap<>();
        StringBuffer sbQuery = new StringBuffer();
        StringBuffer sbQueryFinal = new StringBuffer();
        
        try{ 
        	rcEmpresa = connPool.getConnection(esquemaEmpresa);
        	rc = connPool.getConnectionListaNegra();
        	con = rc.getCon();
        	sbQuery.append(ListaNegraQuerys.getListaRFC(rc.getEsquema()));
        	
        	int contColumnas = 1;
        	for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(" B.DISPONIBLE_"+contColumnas).append(",");
        		contColumnas++;
        	}
        	
        	if (yearActual == anioConsulta) {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(rcEmpresa.getEsquema()).append(".PROVEEDORES A inner join ").append(rc.getEsquema()).append(".LISTA_NEGRA_SAT B on B.FECHA_TRASACCION between ? and ? and A.RFC = B.RFC ");	
        	}else {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(rcEmpresa.getEsquema()).append(".PROVEEDORES A inner join ").append(rc.getEsquema()).append(".LISTA_NEGRA_SAT_HISTORICO B on B.FECHA_TRASACCION between ? and ? and A.RFC = B.RFC ");
        	}
        	
        	stmt = con.prepareStatement(sbQueryFinal.toString() );
    		stmt.setString(1, anioConsulta+"-01-01 01:01:01");
    		stmt.setString(2, anioConsulta+"-12-31 23:59:59");
        	
        	logger.info("stmt===>"+stmt);
            rs = stmt.executeQuery();
            String rfcRS = null;
            int colDisponible = 2;
			while(rs.next()){
				rfcRS = Utils.noNulo(rs.getString(1));
				contColumnas = 1;
				colDisponible = 2;
				
				for (int x = 0; x < columnasFechas.size(); x++) {
					mapaDisponibles.put(DISPONIBLE_+contColumnas, rs.getString(colDisponible));
					colDisponible++;
					contColumnas++;
				}
				mapaRFC.put(rfcRS, mapaDisponibles);
				mapaDisponibles = new HashMap<>();
				
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
	            
	            if(rcEmpresa.getCon() != null)
	            	rcEmpresa.getCon().close();
	            rcEmpresa = null;
	            
	        }catch(Exception e){
	        	rs = null;
	            stmt = null;
	            con = null;
	            rcEmpresa = null;
	        }
        }
        return mapaRFC;
    }
	*/
	
	
	public HashMap<String, String> consultaProveedores(String esquemaEmpresa){
        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rcEmpresa = null;
        Connection conEmpresa = null;
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        HashMap<String, String> mapaRFC = new HashMap<>();
        
        try{ 
        	rcEmpresa = connPool.getConnection(esquemaEmpresa);
        	conEmpresa = rcEmpresa.getCon();
        	stmt = conEmpresa.prepareStatement(ProveedoresQuerys.getQueryDetalleProveedor(rcEmpresa.getEsquema()));
        	
        	//logger.info("stmt===>"+stmt);
            rs = stmt.executeQuery();
            String rfcRS = null;
			while(rs.next()){
				rfcRS = Utils.noNulo(rs.getString(3));
				mapaRFC.put(rfcRS, rfcRS);
				
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
	            if(conEmpresa != null)
	            	conEmpresa.close();
	            conEmpresa = null;
	            
	        }catch(Exception e){
	        	rs = null;
	            stmt = null;
	            conEmpresa = null;
	        }
        }
        return mapaRFC;
    }
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> detalleListaNegraCSV(Connection con, String esquema, String nombreEmpresa, String razonSocial, String rfcListaNegra, String idSupuesto, String idNombreArticulo, String idEstatus, int anioLista)
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
        	sbQuery.append(ListaNegraQuerys.getDetalleListaNegra(esquema));
        	
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
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(esquema).append(".LISTA_NEGRA_SAT ");	
        	}else {
        		sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(esquema).append(".LISTA_NEGRA_SAT_HISTORICO ");
        	}
        	
        	int contParam = 1;
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? ");
        	
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
            
            HashMap<String, String> mapaRFC = consultaProveedores(nombreEmpresa);
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

	
	public boolean validarEstatus(Connection con, String esquema, String razonSocial, String rfcListaNegra, String idSupuesto, String idNombreArticulo, String idEstatus, int anioLista)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sbQuery = new StringBuffer();
        boolean isEncontro = false;
        try{ //
        	
        	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
            Date fechaActual = new Date();
            
        	int yearActual = Integer.parseInt(formatDate.format(fechaActual));
        	if (anioLista == 0) {
        		anioLista = yearActual;
        	}

        	ArrayList<String> columnasFechas = consultaColumnas(anioLista);
        	
        	sbQuery.append(ListaNegraQuerys.getValidarEstatus(esquema));
        	
        	int contColumnas = columnasFechas.size() - 1;
        	
        	//for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(DISPONIBLE_+contColumnas);
        		contColumnas++;
        	//}        	
        	

        	
        	if (yearActual == anioLista) {
        		sbQuery.append(" from ").append(esquema).append(".LISTA_NEGRA_SAT ");	
        	}else {
        		sbQuery.append(" from ").append(esquema).append(".LISTA_NEGRA_SAT_HISTORICO ");
        	}
        	
        	
        	int contParam = 1;
        	
        	sbQuery.append(" where FECHA_TRASACCION between ? and ? ");
        	
        	if (!"".equalsIgnoreCase(rfcListaNegra)) {
        		sbQuery.append(" and RFC = ? ");
        	}

        	if (!"".equalsIgnoreCase(razonSocial)) {
        		sbQuery.append(" and RAZON_SOCIAL  = ? ");
        	}
        	
        	
        	stmt = con.prepareStatement( sbQuery.toString());
        	//logger.info("anioLista----->" + anioLista);
    		stmt.setString(contParam, anioLista+"-01-01 01:01:01");
    		contParam++;
    		stmt.setString(contParam, anioLista+"-12-31 23:59:59");
    		contParam++;

    		if (!"".equalsIgnoreCase(rfcListaNegra)) {
        		stmt.setString(contParam, rfcListaNegra);
        		contParam++;
        	}
        	
    		if (!"".equalsIgnoreCase(razonSocial)) {
        		stmt.setString(contParam, razonSocial);
        		contParam++;
        	}
        	
        	logger.info("stmt===>"+stmt);
            rs = stmt.executeQuery();
            String VALOR_DISPONIBLE = null;
			while (rs.next()){
				VALOR_DISPONIBLE = rs.getString(1);
				if ("0".equalsIgnoreCase(VALOR_DISPONIBLE)) {
					isEncontro = false;
				}else if ("1".equalsIgnoreCase(VALOR_DISPONIBLE)) {
					isEncontro = true;
					break;
				}
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
        return isEncontro;
    }
	
	
	public HashMap<String, String> validarEstatusPDF(Connection con, String esquema, String razonSocial, String rfcListaNegra, String idSupuesto, String idNombreArticulo, String idEstatus, int anioLista)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer sbQuery = new StringBuffer();
        HashMap<String, String> mapaLista = new HashMap<String, String>();
        try{ //
        	
        	mapaLista.put("CANCELADOS POR INSOLVENCIA", "NO ENCONTRADO"); // ya
        	mapaLista.put("CANCELADOS", "NO ENCONTRADO"); // ya
        	mapaLista.put("CANCELADOS POR INCOSTEABILIDAD", "NO ENCONTRADO"); // ya
        	mapaLista.put("Artículo 74", "NO ENCONTRADO");  // ya
        	mapaLista.put("CONDONADOS", "NO ENCONTRADO"); // ya
        	mapaLista.put("RETORNO INVERSIONES", "NO ENCONTRADO");  // ya
        	mapaLista.put("Definitivo", "NO ENCONTRADO");   // ya
        	mapaLista.put("Desvirtuado", "NO ENCONTRADO");   // ya
        	mapaLista.put("Presunto", "NO ENCONTRADO");		// ya
        	mapaLista.put("Sentencia Favorable", "NO ENCONTRADO");  // ya
        	mapaLista.put("CONDONADOS0715", "NO ENCONTRADO"); // ya
        	mapaLista.put("CANCELADOS0715", "NO ENCONTRADO"); // ya
        	mapaLista.put("EXIGIBLES", "NO ENCONTRADO");   // ya
        	mapaLista.put("FIRMES", "NO ENCONTRADO");	// ya
        	mapaLista.put("NO LOCALIZADOS", "NO ENCONTRADO"); // ya
        	mapaLista.put("SENTENCIAS", "NO ENCONTRADO");	// ya
        	
        	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
            Date fechaActual = new Date();
            
        	int yearActual = Integer.parseInt(formatDate.format(fechaActual));
        	if (anioLista == 0) {
        		anioLista = yearActual;
        	}

        	ArrayList<String> columnasFechas = consultaColumnas(anioLista);
        	
        	sbQuery.append(ListaNegraQuerys.getValidarEstatus(esquema));
        	
        	int contColumnas = columnasFechas.size() - 1;
        	
        	//for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(DISPONIBLE_+contColumnas);
        		contColumnas++;
        	//}        	
        	

        	
        	if (yearActual == anioLista) {
        		sbQuery.append(", SUPUESTO from ").append(esquema).append(".LISTA_NEGRA_SAT ");	
        	}else {
        		sbQuery.append(", SUPUESTO from ").append(esquema).append(".LISTA_NEGRA_SAT_HISTORICO ");
        	}
        	
        	
        	int contParam = 1;
        	
        	sbQuery.append(" where FECHA_TRASACCION between ? and ? ");
        	
        	if (!"".equalsIgnoreCase(rfcListaNegra)) {
        		sbQuery.append(" and RFC = ? ");
        	}

        	if (!"".equalsIgnoreCase(razonSocial)) {
        		sbQuery.append(" and RAZON_SOCIAL  = ? ");
        	}
        	
        	
        	stmt = con.prepareStatement( sbQuery.toString());
        	//logger.info("anioLista----->" + anioLista);
    		stmt.setString(contParam, anioLista+"-01-01 01:01:01");
    		contParam++;
    		stmt.setString(contParam, anioLista+"-12-31 23:59:59");
    		contParam++;

    		if (!"".equalsIgnoreCase(rfcListaNegra)) {
        		stmt.setString(contParam, rfcListaNegra);
        		contParam++;
        	}
        	
    		if (!"".equalsIgnoreCase(razonSocial)) {
        		stmt.setString(contParam, razonSocial);
        		contParam++;
        	}
    		
    		
        	
        	logger.info("stmt===>"+stmt);
            rs = stmt.executeQuery();
            String VALOR_DISPONIBLE = null;
            String NOMBRE_SUPUESTO = null;
			while (rs.next()){
				VALOR_DISPONIBLE = rs.getString(1);
				NOMBRE_SUPUESTO = Utils.noNuloNormal(rs.getString(2));
				if ("0".equalsIgnoreCase(VALOR_DISPONIBLE)) {
					mapaLista.put(NOMBRE_SUPUESTO, NO_ENCONTRADO);
				}else if ("1".equalsIgnoreCase(VALOR_DISPONIBLE)) {
					mapaLista.put(NOMBRE_SUPUESTO, ENCONTRADO);
				}
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
        return mapaLista;
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
