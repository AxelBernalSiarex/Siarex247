package com.siarex247.configuraciones.Etiquetas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.sat.Catalogos.CatalogosBean;
import com.siarex247.catalogos.sat.Catalogos.CatalogosForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class EtiquetasBean {

	public static final Logger logger = Logger.getLogger("siarex247");

	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleConfiguracion(Connection con, String esquema)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        String where = "";
        try
        {
        	String sbQuery = EtiquetasQuerys.getQueryDetalleConf(esquema) + where + " order by VERSION asc";
            stmt = con.prepareStatement(sbQuery);
            rs = stmt.executeQuery();
            String configurado = "N";
            String fechaIni = null;
            String fechaFin = null;
            String version;
            String etiqueta;
            ArrayList<String> listaValores = null;
            StringBuffer sbValores = new StringBuffer();
            String valorFinal = null;
            while(rs.next()) {
            	sbValores.setLength(0);
            	version = Utils.noNulo(rs.getString(11));
            	etiqueta = Utils.noNuloNormal(rs.getString(2));
					jsonobj.put("claveRegistro",rs.getInt(1));
					jsonobj.put("etiqueta",etiqueta);
					
					fechaIni = Utils.noNulo(rs.getString(5));
					fechaFin = Utils.noNulo(rs.getString(6));
					if ("".equals(fechaIni)){
						configurado = "N";
					}else{
						configurado = "S";
						fechaIni  = UtilsFechas.getFechaddMMMyyyy(rs.getDate(5));
						fechaFin  = UtilsFechas.getFechaddMMMyyyy(rs.getDate(6));

					}
					
					listaValores = valorEtiqueta(con, esquema, etiqueta, version);
					for (int x = 0; x < listaValores.size(); x++) {
						sbValores.append(listaValores.get(x)).append(",");
					}
					if (sbValores.length() > 1) {
						valorFinal = sbValores.substring(0, sbValores.length() - 1);
					}else {
						valorFinal = sbValores.toString();
					}
					// logger.info("Valores===>"+sbValores.toString()+", Etiqueta==>"+etiqueta+", Version==>"+version);
					jsonobj.put("activo",Utils.noNuloNormal(rs.getString(3)));
					jsonobj.put("configurado",configurado);
					jsonobj.put("fechaIni",fechaIni);
					jsonobj.put("fechaFin",fechaFin);
					jsonobj.put("subject",Utils.noNuloNormal(rs.getString(7)));
					jsonobj.put("usuarioTrans",Utils.noNuloNormal(rs.getString(8)));
					jsonobj.put("fechaTrans",UtilsFechas.getFechaddMMMyyyyHHss(rs.getTimestamp(9)));
					jsonobj.put("version", version);
					jsonobj.put("datoValida",valorFinal);
					jsonobj.put("mensaje",Utils.noNuloNormal(rs.getString(10)));
					jsonobj.put("tipo",Utils.noNulo(rs.getString(12)));
					jsonobj.put("desTipo",desTipoValidacion(Utils.noNulo(rs.getString(12))));
					
		        	jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
				
            }
			mapaRes.put("detalle", jsonArray);
        }
        catch(Exception e){
            Utils.imprimeLog("Error detalle configuracion...", e);
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
	
	
	/*
	@SuppressWarnings("unchecked")
	public Map<String, Object > detConfComplementos(Connection con, String esquema, String aaa) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        try
        {
        	String sbQuery = EtiquetasQuerys.getQueryDetalleConfComp(esquema) + " order by VERSION asc";
            stmt = con.prepareStatement(sbQuery);
            rs = stmt.executeQuery();
            
            String configurado = "N";
            String fechaIni = null;
            String fechaFin = null;
            
            while(rs.next()) {
					jsonobj.put("claveRegistro",rs.getInt(1));
					jsonobj.put("etiqueta",Utils.noNuloNormal(rs.getString(2)));
					
					fechaIni = Utils.noNulo(rs.getString(5));
					fechaFin = Utils.noNulo(rs.getString(6));
					if ("".equals(fechaIni)){
						configurado = "N";
					}else{
						configurado = "S";
						fechaIni  = UtilsFechas.getFechaddMMMyyyy(rs.getDate(5));
						fechaFin  = UtilsFechas.getFechaddMMMyyyy(rs.getDate(6));

					}
					
					jsonobj.put("activo",Utils.noNuloNormal(rs.getString(3)));
					jsonobj.put("configurado",configurado);
					jsonobj.put("fechaIni",fechaIni);
					jsonobj.put("fechaFin",fechaFin);
					jsonobj.put("subject",Utils.noNuloNormal(rs.getString(7)));
					jsonobj.put("usuarioTrans",Utils.noNulo(rs.getString(8)));
					jsonobj.put("fechaTrans",Utils.noNulo(rs.getString(9)));
					jsonobj.put("version",Utils.noNulo(rs.getString(11)));
		        	jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
				
            }
			mapaRes.put("detalle", jsonArray);
        }
        catch(Exception e){
            Utils.imprimeLog("Error detalle configuracion complementos...", e);
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
*/
	
	
	public int actualizaConfiguracion(Connection con, String esquema, int idRegistro, String etiqueta, String  activada, String fechaInicial, String fechaFinal,
				String subject, String mensajeError, String usuarioMod )
    {
        PreparedStatement stmt = null;
        int resultado = 0;
        try
        {
        	stmt = con.prepareStatement(EtiquetasQuerys.getQueryActualizaConf(esquema));
        	stmt.setString(1, activada);
        	stmt.setString(2, fechaInicial);
            stmt.setString(3, fechaFinal);
            stmt.setString(4, subject);
            stmt.setString(5, mensajeError);
            stmt.setString(6, usuarioMod);
            stmt.setInt(7, idRegistro);
            resultado = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("actualizaConfiguracion ", e);
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

	/*
	public int actualizaConfComp(Connection con, String esquema, int idRegistro, String etiqueta, String  activadaTemp,
			                          String fechaInicial, String fechaFinal, String subject, String mensajeError, String usuarioMod ) {
	    PreparedStatement stmt = null;
	    int resultado = 0;

	    try {
	    	stmt = con.prepareStatement(EtiquetasQuerys.getQueryActualizaConfComp(esquema));
	    	stmt.setString(1, activadaTemp);
	    	stmt.setString(2, fechaInicial);
	        stmt.setString(3, fechaFinal);
	        stmt.setString(4, subject);
	        stmt.setString(5, mensajeError);
	        stmt.setString(6, usuarioMod);
	        stmt.setInt(7, idRegistro);
	        resultado = stmt.executeUpdate();
	    }
	    catch(Exception e){
	    	Utils.imprimeLog("actualizaConfComp: ", e);
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
	*/
	
	
	//ETIQUETA, ACTIVO, CONFIGURADO, FECHAINI,FECHAFIN, SUBJECT, USUARIO_TRAN, FECHA_TRANS
	 @SuppressWarnings("unchecked")
		public JSONObject buscarConf (Connection con, String esquema, int idRegistro)
		    {
		        PreparedStatement stmt = null;
		        ResultSet rs = null;
		        JSONObject jsonobj = new JSONObject();
		        try
		        {
		        	//ETIQUETA, ACTIVO, CONFIGURADO, FECHAINI,FECHAFIN,MENSAJE, SUBJECT, USUARIO_TRAN, FECHA_TRANS
		        	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		            stmt = con.prepareStatement(EtiquetasQuerys.getQueryBuscarConf(esquema));
		            stmt.setInt(1, idRegistro);
		            rs = stmt.executeQuery();
		            String activado = null;
					if(rs.next()) 
		            {
							jsonobj.put("etiqueta",Utils.noNuloNormal(rs.getString(1)));
							activado = Utils.noNuloNormal(rs.getString(2));
							if ("S".equalsIgnoreCase(activado)){
								jsonobj.put("activo",true);	
							}else{
								jsonobj.put("activo",false);
							}
							
							jsonobj.put("fechaInicial",dateFormat.format(rs.getDate(4)));
							jsonobj.put("fechaFinal",dateFormat.format(rs.getDate(5)));
							jsonobj.put("subject",Utils.noNuloNormal(rs.getString(6)));
							jsonobj.put("mensajeError",Utils.noNuloNormal(rs.getString(7)));
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
		        return jsonobj;
		    }
		 
	 /*
	 @SuppressWarnings("unchecked")
		public JSONObject buscarConfComp (Connection con, String esquema, int idRegistro) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();

        try {
        	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            stmt = con.prepareStatement(EtiquetasQuerys.getQueryBuscarConfComp(esquema));
            stmt.setInt(1, idRegistro);
            rs = stmt.executeQuery();
            String activado = null;

			if(rs.next()) {
				jsonobj.put("etiqueta",Utils.noNuloNormal(rs.getString(1)));
				activado = Utils.noNuloNormal(rs.getString(2));
				if ("S".equalsIgnoreCase(activado)){
					jsonobj.put("activo",true);	
				}else{
					jsonobj.put("activo",false);
				}
				
				jsonobj.put("fechaInicial",dateFormat.format(rs.getDate(4)));
				jsonobj.put("fechaFinal",dateFormat.format(rs.getDate(5)));
				jsonobj.put("subject",Utils.noNuloNormal(rs.getString(6)));
				jsonobj.put("mensajeError",Utils.noNuloNormal(rs.getString(7)));
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
        return jsonobj;
    }
	 */
	 
	 @SuppressWarnings("unchecked")
		public JSONObject buscarConfMap(Connection con, String esquema, int idRegistro){
     PreparedStatement stmt = null;
     ResultSet rs = null;
     JSONObject jsonobj = new JSONObject();
     try {
         stmt = con.prepareStatement(EtiquetasQuerys.getQueryBuscarConf(esquema));
         stmt.setInt(1, idRegistro);
         rs = stmt.executeQuery();
         String activado = null;

			if(rs.next()) {
				jsonobj.put("etiqueta",Utils.noNuloNormal(rs.getString(1)));
				activado = Utils.noNuloNormal(rs.getString(2));

				if ("S".equalsIgnoreCase(activado)){
					jsonobj.put("activo",true);	
				}
				else{
					jsonobj.put("activo",false);
				}
				
				jsonobj.put("fechaInicial",Utils.noNulo(rs.getDate(4)));
				jsonobj.put("fechaFinal",Utils.noNulo(rs.getDate(5)));
				jsonobj.put("subject",Utils.noNuloNormal(rs.getString(6)));
				jsonobj.put("mensajeError",Utils.noNuloNormal(rs.getString(7)));
         }
     }
     catch(Exception e){
         Utils.imprimeLog("buscarConfMap(): ", e);
     }
     finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
     }
     return jsonobj;
	 }
	 
	 
	 @SuppressWarnings("unchecked")
		public Map<String, Object > detalleEtiqueta(Connection con, String esquema, String etiqueta, String version)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        JSONObject jsonobj = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        Map<String, Object > mapaRes = new HashMap<String, Object>();
	        
	        CatalogosBean catalogosSAT = new CatalogosBean();
	        ResultadoConexion rcSAT = null;
	        ConexionDB connPool = new ConexionDB();
	        Connection conSAT = null;
	        try {
	        	
	        	rcSAT = connPool.getConnectionSAT();
	        	conSAT = rcSAT.getCon();
	        	
	            String sbQuery = EtiquetasQuerys.getQueryDetalleEtiquetas(esquema) + " and VERSION = '" + version + "'";
	            stmt = con.prepareStatement(sbQuery);
	            stmt.setString(1, etiqueta);
	            rs = stmt.executeQuery();
	            
	            EtiquetasForm etiquetasForm = new EtiquetasForm();
	            ArrayList<EtiquetasForm> listaDetalle = new ArrayList<>();
	            while(rs.next()) {
	            	/*
						jsonobj.put("claveRegistro",rs.getInt(1));
						jsonobj.put("etiqueta",Utils.noNuloNormal(rs.getString(2)));
						jsonobj.put("datoValida",Utils.noNuloNormal(rs.getString(3)));
			        	jsonArray.add(jsonobj);  
			        	jsonobj = new JSONObject();
			        */
	            	etiquetasForm.setClaveRegistro(rs.getInt(1));
	            	etiquetasForm.setEtiqueta(Utils.noNuloNormal(rs.getString(2)));
	            	etiquetasForm.setDatoValida(Utils.noNuloNormal(rs.getString(3)));
	            	listaDetalle.add(etiquetasForm);
	            	etiquetasForm = new EtiquetasForm();
	            }
				// mapaRes.put("detalle", jsonArray);
	            /*
	            HashMap<String, CatalogosForm> mapaFormas = null;
	            if ("formaDePago".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleFormas(conSAT, rcSAT.getEsquema());	
	            }else if ("metodoPago".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleMetodoPago(conSAT, rcSAT.getEsquema());
	            }else if ("moneda".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleMoneda(conSAT, rcSAT.getEsquema());
	            }else if ("UsoCFDI".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleUsoCFDI(conSAT, rcSAT.getEsquema());
	            }else if ("version".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleVersion(conSAT, rcSAT.getEsquema());
	            }
	            */
	            
	            HashMap<String, CatalogosForm> mapaFormas = null;
	            if ("tipoDeComprobante".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleTipoComprobante(conSAT, rcSAT.getEsquema());	
	            }else if ("moneda".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleMoneda(conSAT, rcSAT.getEsquema());
	            }else if ("usoCFDIReceptor".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleUsoCFDI(conSAT, rcSAT.getEsquema());
	            }else if ("version".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleVersion(conSAT, rcSAT.getEsquema());
	            }else if ("formaPago".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleFormas(conSAT, rcSAT.getEsquema());
	            }else if ("exportacion".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleExportacion(conSAT, rcSAT.getEsquema());
	            }else if ("metodoPago".equalsIgnoreCase(etiqueta)) {
	            	mapaFormas = catalogosSAT.detalleMetodoPago(conSAT, rcSAT.getEsquema());
	            }
	            
	            
	            
	            List<String> listLlaves = new ArrayList<>(mapaFormas.keySet());
				Collections.sort(listLlaves);
				String llaveMapa = null;
				CatalogosForm catalogoSATForm = null;
				//String descripcion = null;
				int idIdentificador = 1;
				for (int x = 0; x < listLlaves.size(); x++) {
					llaveMapa = listLlaves.get(x);
					catalogoSATForm = mapaFormas.get(llaveMapa);
					
					for (int y = 0; y < listaDetalle.size(); y++) {
						etiquetasForm = listaDetalle.get(y);
						if (catalogoSATForm.getClave().equalsIgnoreCase(etiquetasForm.getDatoValida())) {
							jsonobj.put("checked","true");
							jsonobj.put("claveRegistro",etiquetasForm.getClaveRegistro());
							break;
						}else {
							jsonobj.put("checked","false");
							jsonobj.put("claveRegistro",-100);
						}
					}
					
					if (listaDetalle.size() == 0) {
						jsonobj.put("checked","false");
						jsonobj.put("claveRegistro",-100);
					}
					
					
					jsonobj.put("datoValida",catalogoSATForm.getClave());
					jsonobj.put("desEtiqueta",catalogoSATForm.getClave() + " - " + catalogoSATForm.getDescripcion());	
					jsonobj.put("idIdentificador",idIdentificador++);
					
					
					// logger.info("jsonobj===>"+jsonobj);
		        	jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
		        	
				}
				mapaRes.put("detalle", jsonArray);
	        }
	        catch(Exception e){
	            Utils.imprimeLog("detalleEtiqueta_", e);
	        }finally{
		        try{
		            if(rs != null)
		                rs.close();
		            rs = null;
		            if(stmt != null)
		                stmt.close();
		            stmt = null;
		            if(conSAT != null)
		            	conSAT.close();
		            conSAT = null;
		        }catch(Exception e){
		        	rs = null;
		            stmt = null;
		            conSAT = null;
		        }
	        }
	        return mapaRes;
	    }
		
	 /*
	 @SuppressWarnings("unchecked")
		public Map<String, Object > detalleEtiquetaComp(Connection con, String esquema, String etiqueta, String version)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        JSONObject jsonobj = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        Map<String, Object > mapaRes = new HashMap<String, Object>();
	        try
	        {
	            String sbQuery = EtiquetasQuerys.getQueryDetalleEtiquetasComp(esquema) + " and VERSION = '" + version + "'";
//	            logger.info("sbQuery------------>"+sbQuery);
	            stmt = con.prepareStatement(sbQuery);
	            stmt.setString(1, etiqueta);
	            rs = stmt.executeQuery();
	            while(rs.next()) {
					jsonobj.put("claveRegistro",rs.getInt(1));
					jsonobj.put("etiqueta",Utils.noNuloNormal(rs.getString(2)));
					jsonobj.put("datoValida",Utils.noNuloNormal(rs.getString(3)));
		        	jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
	            }
				mapaRes.put("detalle", jsonArray);
	        }
	        catch(Exception e){
	            Utils.imprimeLog("detalleEtiqueta_", e);
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
	 */
	 public int altaEtiqueta(Connection con, String esquema, String etiqueta, String datoValida, String version)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int resultado = 0;
	        try
	        {
	        	stmt = con.prepareStatement(EtiquetasQuerys.getQueryAgregarEtiquetas(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
	        	
	        	stmt.setString(1, etiqueta);
	            stmt.setString(2, datoValida);
	            stmt.setString(3, version);
	            int cant = stmt.executeUpdate();
	            if(cant > 0){
	                rs = stmt.getGeneratedKeys();
	                if(rs.next())
	                    resultado = rs.getInt(1);
	            }
	        }
	        catch(SQLException sql){
	        	resultado = sql.getErrorCode();
	        	Utils.imprimeLog("altaEtiqueta ", sql);
	            
	        }catch(Exception e){
	        	resultado = 100;
	            Utils.imprimeLog("altaEtiqueta 2 ", e);
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
	        return resultado;
	    }
		
	 /*
	 public int altaEtiquetaComp(Connection con, String esquema, String etiqueta, String datoValida, String version)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int resultado = 0;
	        try
	        {
	        	stmt = con.prepareStatement(EtiquetasQuerys.getQueryAgregarEtiquetasComp(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
	        	stmt.setString(1, etiqueta);
	            stmt.setString(2, datoValida);
	            stmt.setString(3, version);
	            int cant = stmt.executeUpdate();
	            if(cant > 0){
	                rs = stmt.getGeneratedKeys();
	                if(rs.next())
	                    resultado = rs.getInt(1);
	            }
	        }
	        catch(SQLException sql){
	        	resultado = sql.getErrorCode();
	        	Utils.imprimeLog("altaEtiquetaComp ", sql);
	            
	        }catch(Exception e){
	        	resultado = 100;
	            Utils.imprimeLog("altaEtiquetaComp 2 ", e);
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
	        return resultado;
	    }
	 */
	 
	 public int eliminaEtiqueta(Connection con, String esquema, String etiqueta, String datoValida, String version) {
	        PreparedStatement stmt = null;
	        int resultado = 0;
	        try {
	        	stmt = con.prepareStatement(EtiquetasQuerys.getQueryEliminaEtiquetas(esquema));
	        	stmt.setString(1, etiqueta);
	        	stmt.setString(2, datoValida);
	        	stmt.setString(3, version);
	        	//logger.info("stmt===>"+stmt);
	        	resultado = stmt.executeUpdate();
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("eliminaConfiguracionReg ", e);
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

	 /*
	 public int eliminaEtiquetaComp(Connection con, String esquema, int claveRegistro) {
	        PreparedStatement stmt = null;
	        int resultado = 0;
	        try {
	        	stmt = con.prepareStatement(EtiquetasQuerys.getQueryEliminaEtiquetasComp(esquema));
	        	stmt.setInt(1, claveRegistro);
	        	resultado = stmt.executeUpdate();
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("eliminaEtiquetaComp ", e);
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
	 */
	 

	public Map<String, EtiquetasForm > obtenerConfEtiquetas(Connection con, String esquema, String versionXML)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        Map<String, EtiquetasForm> mapaRes = new HashMap<String, EtiquetasForm>();
	        EtiquetasForm confxmlForm = new EtiquetasForm();
	        try
	        {
	            String sbQuery = EtiquetasQuerys.getQueryDetalleConf(esquema) + " where VERSION = '" + versionXML + "'";
	            stmt = con.prepareStatement(sbQuery);
	            rs = stmt.executeQuery();
	            String fechaActual = UtilsFechas.getFechayyyyMMdd();
	            long fechaLong = Long.parseLong( fechaActual.substring(0,4) + 
	            									fechaActual.substring(5,7) +
	            										fechaActual.substring(8));
	            String fechaIni = null;
	            String fechaFin = null;
	            long fechaIniLong = 0;
	            long fechaFinLong = 0;
	            String activada = null;
	            while(rs.next()){
	            	activada = Utils.noNuloNormal(rs.getString(3));
	            	if ("S".equalsIgnoreCase(activada)){
	            		fechaIni = Utils.noNulo(rs.getString(5));
						fechaFin = Utils.noNulo(rs.getString(6));
						fechaIniLong = Long.parseLong( fechaIni.substring(0,4) + 
	 										fechaIni.substring(5,7) +
	 											fechaIni.substring(8));

						fechaFinLong = Long.parseLong( fechaFin.substring(0,4) + 
										fechaFin.substring(5,7) +
											fechaFin.substring(8));
	            		
						if (fechaLong >= fechaIniLong && fechaLong <= fechaFinLong){
		            		confxmlForm.setClaveRegistro(rs.getInt(1));
		            		confxmlForm.setEtiqueta(Utils.noNuloNormal(rs.getString(2)));
		            		confxmlForm.setFechaIni(rs.getDate(5));
		            		confxmlForm.setFechaFin(rs.getDate(6));
		            		confxmlForm.setSubject(Utils.noNuloNormal(rs.getString(7)));
		            		confxmlForm.setMensaje(Utils.noNuloNormal(rs.getString(10)));
		            		mapaRes.put(confxmlForm.getEtiqueta(), confxmlForm);
		            		confxmlForm = new EtiquetasForm();
						}
	            	}
	            }
	        }
	        catch(Exception e){
	            Utils.imprimeLog("obtenerEtiquetas_", e);
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
		/*
	public ArrayList<EtiquetasForm> obtenerConfEtiquetasComp(Connection con, String esquema, String versionXML) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<EtiquetasForm> datos = new ArrayList<EtiquetasForm>();
        EtiquetasForm confxmlForm = new EtiquetasForm();
        try
        {
            String sbQuery = EtiquetasQuerys.getQueryDetalleConfComp(esquema) + " where VERSION = '" + versionXML + "'";
            stmt = con.prepareStatement(sbQuery);
            rs = stmt.executeQuery();
            String fechaActual = UtilsFechas.getFechayyyyMMdd();
            long fechaLong = Long.parseLong( fechaActual.substring(0,4) + 
            									fechaActual.substring(5,7) +
            										fechaActual.substring(8));
            String fechaIni = null;
            String fechaFin = null;
            long fechaIniLong = 0;
            long fechaFinLong = 0;
            String activada = null;
            while(rs.next()){
            	activada = Utils.noNuloNormal(rs.getString(3));
            	if ("S".equalsIgnoreCase(activada)){
            		fechaIni = Utils.noNulo(rs.getString(5));
					fechaFin = Utils.noNulo(rs.getString(6));
					fechaIniLong = Long.parseLong( fechaIni.substring(0,4) + 
 										fechaIni.substring(5,7) +
 											fechaIni.substring(8));

					fechaFinLong = Long.parseLong( fechaFin.substring(0,4) + 
									fechaFin.substring(5,7) +
										fechaFin.substring(8));
            		
					if (fechaLong >= fechaIniLong && fechaLong <= fechaFinLong){
	            		confxmlForm.setClaveRegistro(rs.getInt(1));
	            		confxmlForm.setEtiqueta(Utils.noNuloNormal(rs.getString(2)));
	            		confxmlForm.setFechaIni(rs.getDate(5));
	            		confxmlForm.setFechaFin(rs.getDate(6));
	            		confxmlForm.setSubject(Utils.noNuloNormal(rs.getString(7)));
	            		confxmlForm.setMensaje(Utils.noNuloNormal(rs.getString(10)));
	            		confxmlForm.setTipoEtiqueta("M");
	            		confxmlForm.setRutaXML(Utils.noNuloNormal(rs.getString(12)));
	            		confxmlForm.setPropiedad(Utils.noNuloNormal(rs.getString(13)));
	            		datos.add(confxmlForm);
	                	confxmlForm = new EtiquetasForm();
					}
            	}
            }
        }
        catch(Exception e){
            Utils.imprimeLog("obtenerConfEtiquetasComp(): ", e);
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
        return datos;
    }
		*/
	
	
	public ArrayList<String> obtenerDatoValidar(Connection con, String esquema, String etiqueta, String versionXML)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        ArrayList<String> datos = new ArrayList<String>();
	        
	        try
	        {
	            String sbQuery = EtiquetasQuerys.getQueryDetalleEtiquetas(esquema) + " and VERSION = '" + versionXML + "'";
	            stmt = con.prepareStatement(sbQuery);
	            stmt.setString(1, etiqueta);
	            rs = stmt.executeQuery();

	            while(rs.next()) 
	            {
	            	datos.add(Utils.noNuloNormal(rs.getString(3)));
	            }
	        }
	        catch(Exception e){
	            Utils.imprimeLog("obtenerDatoValidar", e);
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
	        return datos;
	    }
		
	/*
	public ArrayList<String> obtenerDatoValidarComp(Connection con, String esquema, String etiqueta, String versionXML) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<String> datos = new ArrayList<String>();
        
        try
        {
            String sbQuery = EtiquetasQuerys.getQueryDetalleEtiquetasComp(esquema) + " and VERSION = '" + versionXML + "'";
            stmt = con.prepareStatement(sbQuery);
            stmt.setString(1, etiqueta);
            rs = stmt.executeQuery();

            while(rs.next()) 
            {
            	datos.add(Utils.noNuloNormal(rs.getString(3)));
            }
        }
        catch(Exception e){
            Utils.imprimeLog("obtenerDatoValidarComp(): ", e);
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
        return datos;
    }
	*/
	
	
	public ArrayList<EtiquetasForm> obtenerEtiquetas(Connection con, String esquema, String versionXML, String tipoValidacion) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<EtiquetasForm> datos = new ArrayList<EtiquetasForm>();
        EtiquetasForm etiquetaForm = new EtiquetasForm();
        try
        {
            StringBuffer sbQuery = new StringBuffer(EtiquetasQuerys.getQueryDetalleConf(esquema)).append(" where VERSION = '").append(versionXML).append("'");
            
            if (!"".equalsIgnoreCase(tipoValidacion)) {
            	sbQuery.append(" and TIPO_VALIDACION = ?" );
            	
            }
            
            stmt = con.prepareStatement(sbQuery.toString());
            if (!"".equalsIgnoreCase(tipoValidacion)) {
            	stmt.setString(1, tipoValidacion);
            }
            rs = stmt.executeQuery();
            String fechaActual = Utils.getFechayyyyMMdd();
            long fechaLong = Long.parseLong( fechaActual.substring(0,4) + 
            									fechaActual.substring(5,7) +
            										fechaActual.substring(8));
            String fechaIni = null;
            String fechaFin = null;
            long fechaIniLong = 0;
            long fechaFinLong = 0;
            String activada = null;
            
            while(rs.next()){
            	activada = Utils.noNuloNormal(rs.getString(3));
            	if ("S".equalsIgnoreCase(activada)){
            		fechaIni = Utils.noNulo(rs.getString(5));
					fechaFin = Utils.noNulo(rs.getString(6));
					fechaIniLong = Long.parseLong( fechaIni.substring(0,4) + 
 										fechaIni.substring(5,7) +
 											fechaIni.substring(8));

					fechaFinLong = Long.parseLong( fechaFin.substring(0,4) + 
									fechaFin.substring(5,7) +
										fechaFin.substring(8));
            		
					if (fechaLong >= fechaIniLong && fechaLong <= fechaFinLong){
						etiquetaForm.setClaveRegistro(rs.getInt(1));
						etiquetaForm.setEtiqueta(Utils.noNuloNormal(rs.getString(2)));
						etiquetaForm.setFechaIni(rs.getDate(5));
						etiquetaForm.setFechaFin(rs.getDate(6));
						etiquetaForm.setSubject(Utils.noNuloNormal(rs.getString(7)));
						etiquetaForm.setMensaje(Utils.noNuloNormal(rs.getString(10)));
						etiquetaForm.setTipoEtiqueta("M");
	            		datos.add(etiquetaForm);
	            		etiquetaForm = new EtiquetasForm();
					}
            	}
            }
        }
        catch(Exception e){
            Utils.imprimeLog("obtenerEtiquetas(): ", e);
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
        return datos;
    }
 
	

	
		public ArrayList<String> valorEtiqueta(Connection con, String esquema, String etiqueta, String version)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        ArrayList<String> listaDetalle = new ArrayList<>();
	        try {
	        	
	            String sbQuery = EtiquetasQuerys.getQueryDetalleEtiquetas(esquema) + " and VERSION = '" + version + "'";
	            stmt = con.prepareStatement(sbQuery);
	            stmt.setString(1, etiqueta);
	            // logger.info("stmtEtiqueta===>"+stmt);
	            rs = stmt.executeQuery();
	            
	            while(rs.next()) {
	            	listaDetalle.add(Utils.noNuloNormal(rs.getString(3)));
	            }
				
	        }
	        catch(Exception e){
	            Utils.imprimeLog("detalleEtiqueta_", e);
	        }finally{
		        try{
		            if(rs != null)
		                rs.close();
		            rs = null;
		            if(stmt != null)
		                stmt.close();
		            stmt = null;
		        }catch(Exception e){
		        	rs = null;
		            stmt = null;
		        }
	        }
	        return listaDetalle;
	    }

		
		private String desTipoValidacion(String tipo) {
			 String desTipo = "";
			 try {
				 if ("E".equalsIgnoreCase(tipo)) {
					 desTipo = "Etiqueta";
				 }else if ("B".equalsIgnoreCase(tipo)) {
					 desTipo = "Base";
				 }
			 }catch(Exception e) {
				 Utils.imprimeLog("", e);
			 }
			 return desTipo;
		 }
		
}
