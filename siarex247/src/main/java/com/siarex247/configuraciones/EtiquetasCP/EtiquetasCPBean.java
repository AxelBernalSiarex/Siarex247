package com.siarex247.configuraciones.EtiquetasCP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class EtiquetasCPBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detConfEtiquetasCP(Connection con, String esquema){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        String where = "";

        try {
        	String sbQuery = EtiquetasCPQuerys.getQueryDetConfCP(esquema) + where + " order by VERSION asc";
            stmt = con.prepareStatement(sbQuery);
            rs = stmt.executeQuery();
            String fechaIni = null;
            String fechaFin = null;
            
            while(rs.next()) {
					jsonobj.put("idRegistro", rs.getInt(1));
					jsonobj.put("idCatalogo", Utils.noNuloNormal(rs.getString(2)));
					jsonobj.put("idEtiqueta", Utils.noNuloNormal(rs.getString(3)));
					jsonobj.put("pathXML", Utils.noNuloNormal(rs.getString(4)));
					jsonobj.put("descripcion", Utils.noNuloNormal(rs.getString(5)));
					
					fechaIni = Utils.noNulo(rs.getString(6));
					fechaFin = Utils.noNulo(rs.getString(7));

					if (!"".equals(fechaIni)){
						fechaIni  = UtilsFechas.getFechaddMMMyyyy(rs.getDate(6));
						fechaFin  = UtilsFechas.getFechaddMMMyyyy(rs.getDate(7));
					}
					
					jsonobj.put("fechaIni", fechaIni);
					jsonobj.put("fechaFin", fechaFin);
					jsonobj.put("subject", Utils.noNuloNormal(rs.getString(8)));
					jsonobj.put("mensaje", Utils.noNuloNormal(rs.getString(9)));
					jsonobj.put("usuarioTrans", Utils.noNuloNormal(rs.getString(10)));
					jsonobj.put("fechaTrans", UtilsFechas.getFechaddMMMyyyyHHss(rs.getTimestamp(11)));
					jsonobj.put("activo", Utils.noNuloNormal(rs.getString(12)));
					jsonobj.put("valEtiqueta", Utils.noNuloNormal(rs.getString(13)));
					jsonobj.put("version", Utils.noNuloNormal(rs.getString(14)));

		        	jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
            
            }
        	mapaRes.put("detalle", jsonArray);
        }catch(Exception e){
            Utils.imprimeLog("detConfEtiquetasCP(): ", e);
        }
        finally{
	        try{
	            if(rs != null) {
	                rs.close();
	            }
	            rs = null;
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return mapaRes;
    }
	
	
	
	@SuppressWarnings("unchecked")
	public JSONObject buscarConfMapCP(Connection con, String esquema, int idRegistro){
	 PreparedStatement stmt = null;
	 ResultSet rs = null;
	 JSONObject jsonobj = new JSONObject();
	 try {
		 String sbQuery = EtiquetasCPQuerys.getQueryDetConfCP(esquema);
     	
     	 if(idRegistro > 0) {
     		sbQuery += " where ID_REGISTRO = " + idRegistro;
     	 }
	     stmt = con.prepareStatement(sbQuery);
	     rs = stmt.executeQuery();
	     String activado = null;
	     String valEtiqueta = null;
	
			if(rs.next()) {
				jsonobj.put("etiqueta",Utils.noNuloNormal(rs.getString(3)));
				activado = Utils.noNuloNormal(rs.getString(12));
				valEtiqueta = Utils.noNuloNormal(rs.getString(13));

				if ("S".equalsIgnoreCase(activado)){
					jsonobj.put("activo",true);	
				}
				else{
					jsonobj.put("activo",false);
				}
				
				if ("S".equalsIgnoreCase(valEtiqueta)){
					jsonobj.put("valEtiqueta",true);	
				}
				else{
					jsonobj.put("valEtiqueta",false);
				}
				
				jsonobj.put("fechaInicial",rs.getString(6));
				jsonobj.put("fechaFinal",rs.getString(7));
				jsonobj.put("subject",Utils.noNuloNormal(rs.getString(8)));
				jsonobj.put("mensajeError",Utils.noNuloNormal(rs.getString(9)));
	     }
	 }
	 catch(Exception e){
	     Utils.imprimeLog("buscarConfMapCP(): ", e);
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
	
	
	
	public int actualizaConfCP(Connection con, String esquema, int idRegistro, String etiqueta, String  activadaTemp,
	        String fechaInicial, String fechaFinal, String subject, String mensajeError, String usuarioMod, String valEtiquetaTemp) {
			PreparedStatement stmt = null;
			int resultado = 0;
			try {
				stmt = con.prepareStatement(EtiquetasCPQuerys.getQueryActualizaConf(esquema));
				stmt.setString(1, activadaTemp);
				stmt.setString(2, fechaInicial);
				stmt.setString(3, fechaFinal);
				stmt.setString(4, subject);
				stmt.setString(5, mensajeError);
				stmt.setString(6, usuarioMod);
				stmt.setString(7, valEtiquetaTemp);
				stmt.setInt(8, idRegistro);
				resultado = stmt.executeUpdate();
			}
			catch(Exception e){
				Utils.imprimeLog("actualizaConfCP ", e);
			}
			finally{
				try{
					if(stmt != null) {
					 stmt.close();
					}
					stmt = null;
				}
				catch(Exception e){
					stmt = null;
				}
			}
			return resultado;
	   }
	
	
	 @SuppressWarnings("unchecked")
		public Map<String, Object > detalleEtiqueta(Connection con, String esquema, String etiqueta, String version)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        JSONObject jsonobj = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        Map<String, Object > mapaRes = new HashMap<String, Object>();
	        try
	        {
	            String sbQuery = EtiquetasCPQuerys.getDetalleEtiquetasCP(esquema) + " and VERSION = '" + version + "'";
	            stmt = con.prepareStatement(sbQuery);
	            stmt.setString(1, etiqueta);
	            
	            rs = stmt.executeQuery();
	            while(rs.next()) 
	            {
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
	
	
	public int altaEtiqueta(Connection con, String esquema, String etiqueta, String datoValida, String version, String usuarioHTTP){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;

        try {
        	stmt = con.prepareStatement(EtiquetasCPQuerys.getQueryAgregarEtiquetas(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
        	stmt.setString(1, etiqueta);
            stmt.setString(2, datoValida);
            stmt.setString(3, version);
            stmt.setString(4, usuarioHTTP);
            int cant = stmt.executeUpdate();

            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next()) {
                    resultado = rs.getInt(1);
                }
            }
        }
        catch(SQLException sql){
        	resultado = sql.getErrorCode();
        	Utils.imprimeLog("altaEtiqueta(sql): ", sql);
        }
        catch(Exception e){
        	resultado = 100;
            Utils.imprimeLog("altaEtiqueta(e): ", e);
        }
        finally{
        	try{
  	        	 if(rs != null) {
  	                rs.close();
  	            }
  	            rs = null;
  	            if(stmt != null) {
  	                stmt.close();
  	            }
  	            stmt = null;
	       	}
	       	catch(Exception e){
	       		rs = null;
	       		stmt = null;
	       	}
        }
        return resultado;
    }
	
	
	
	public int eliminaEtiqueta(Connection con, String esquema, int claveRegistro) {
        PreparedStatement stmt = null;
        int resultado = 0;

        try {
        	stmt = con.prepareStatement(EtiquetasCPQuerys.getQueryEliminaEtiquetas(esquema));
        	stmt.setInt(1, claveRegistro);
        	resultado = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("eliminaEtiqueta(); ", e);
        }
        finally{
	        try{
	        	if(stmt != null) {
  	                stmt.close();
  	            }
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return resultado;
    }
	
	
	public ArrayList<EtiquetasCPForm> listadoActivos (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<EtiquetasCPForm> listaDetalle = new ArrayList<>();
		EtiquetasCPForm cartaPorteForm = new EtiquetasCPForm();
		try {		
			stmt = con.prepareStatement(EtiquetasCPQuerys.getListadoActivos(esquema));
			stmt.setString(1, "S");
			rs  = stmt.executeQuery();
			while  (rs.next()) {
				cartaPorteForm.setIdRegistro(rs.getInt(1));
				cartaPorteForm.setIdCatalogo(Utils.noNulo(rs.getString(2)));
				cartaPorteForm.setIdEtiqueta(Utils.noNuloNormal(rs.getString(3)));
				cartaPorteForm.setPathXML(Utils.noNuloNormal(rs.getString(4)));
				cartaPorteForm.setFechaIni(Utils.noNuloNormal(rs.getString(6)));
				cartaPorteForm.setFechaFin(Utils.noNuloNormal(rs.getString(7)));
				cartaPorteForm.setSubject(Utils.noNuloNormal(rs.getString(8)));
				cartaPorteForm.setMensaje(Utils.noNuloNormal(rs.getString(9)));
				cartaPorteForm.setValidarVacio(Utils.noNuloNormal(rs.getString(10)));
				listaDetalle.add(cartaPorteForm);
				cartaPorteForm = new EtiquetasCPForm();
				
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaDetalle;
	}
	
	
	public ArrayList<EtiquetasCPForm> listadoActivos20 (Connection con, String esquema, String versionXML){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<EtiquetasCPForm> listaDetalle = new ArrayList<>();
		EtiquetasCPForm cartaPorteForm = new EtiquetasCPForm();
		try {		
			stmt = con.prepareStatement(EtiquetasCPQuerys.getListadoActivos20(esquema));
			stmt.setString(1, "S");
			stmt.setString(2, versionXML);
			rs  = stmt.executeQuery();
			while  (rs.next()) {
				cartaPorteForm.setIdRegistro(rs.getInt(1));
				cartaPorteForm.setIdCatalogo(Utils.noNulo(rs.getString(2)));
				cartaPorteForm.setIdEtiqueta(Utils.noNuloNormal(rs.getString(3)));
				cartaPorteForm.setPathXML(Utils.noNuloNormal(rs.getString(4)));
				cartaPorteForm.setFechaIni(Utils.noNuloNormal(rs.getString(6)));
				cartaPorteForm.setFechaFin(Utils.noNuloNormal(rs.getString(7)));
				cartaPorteForm.setSubject(Utils.noNuloNormal(rs.getString(8)));
				cartaPorteForm.setMensaje(Utils.noNuloNormal(rs.getString(9)));
				cartaPorteForm.setValidarVacio(Utils.noNuloNormal(rs.getString(10)));
				listaDetalle.add(cartaPorteForm);
				cartaPorteForm = new EtiquetasCPForm();
				
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaDetalle;
	}
}
