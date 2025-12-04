package com.siarex247.configuraciones.RegimenFiscal;

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

public class RegimenFiscalBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleRF(Connection con, String esquema) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();

        try{
        	StringBuffer sbQuery = new StringBuffer(RegimenFiscalQuerys.getQueryConsultaRF(esquema));

            stmt = con.prepareStatement(sbQuery.toString());
            
            rs = stmt.executeQuery();
            String FECHA_TRANS = null;
			while(rs.next()) {
					FECHA_TRANS  = UtilsFechas.getFechaddMMMyyyy(rs.getDate(6));
					
					jsonobj.put("ID_REGISTRO",rs.getInt(1));
					jsonobj.put("CLAVE_PROVEEDOR",Utils.noNulo(rs.getString(7)));
					jsonobj.put("CLAVE_REGIMEN",Utils.noNuloNormal(rs.getString(3)));
					jsonobj.put("DES_REGIMEN",Utils.noNuloNormal(rs.getString(4)));
					jsonobj.put("USUARIO_TRAN",Utils.noNuloNormal(rs.getString(5)));
					jsonobj.put("FECHA_TRANS", FECHA_TRANS);
					jsonobj.put("RAZON_SOCIAL",Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(8))));
					
					jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
            }
			mapaRes.put("detalle", jsonArray);
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
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
        return mapaRes;
    }
	
	@SuppressWarnings("unchecked")
	public JSONObject buscarConfRF(Connection con, String esquema, int idRegistro){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();

        try{
        	StringBuffer sbQuery = new StringBuffer(RegimenFiscalQuerys.getQueryBuscarConfRF(esquema));
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setInt(1, idRegistro);
            rs = stmt.executeQuery();
            
			if(rs.next()){
				jsonobj.put("idRegistro", idRegistro);
				jsonobj.put("CLAVE_PROVEEDOR", Utils.noNulo(rs.getString(1)));
				jsonobj.put("CLAVE_REGIMEN", Utils.noNulo(rs.getString(2)));
            }
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
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
	
	
	public int grabarRF(Connection con, String esquema, String razonSocial, String regimenFiscal, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;

		try{ 
			stmt = con.prepareStatement(RegimenFiscalQuerys.getQueryInsertRF(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, razonSocial);
			stmt.setString(2, regimenFiscal);
			stmt.setString(3, usuarioHTTP);
			
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
			Utils.imprimeLog("", sql);
		}
		catch(Exception e){
			Utils.imprimeLog("", e);
			resultado = 100;
		}
		finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
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
	
	
	
	public int updateRF(Connection con, String esquema, String razonSocial, String regimenFiscal, int idRegistro, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int cant = 0;

		try{ 
			stmt = con.prepareStatement(RegimenFiscalQuerys.getQueryUpdateRF(esquema));
			stmt.setString(1, regimenFiscal);
			stmt.setString(2, usuarioHTTP);
			stmt.setInt(3, idRegistro);
			cant = stmt.executeUpdate();
		}
		catch(SQLException sql){
			Utils.imprimeLog("", sql);
		}
		catch(Exception e){
			Utils.imprimeLog("", e);
		}
		finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e){
				stmt = null;
			}
		}
		return cant;

	}
	
	
	
	public int deleteRF(Connection con, String esquema, int idRegistro, String usuarioHTTP){
		PreparedStatement stmt = null;
		PreparedStatement stmtBitacora = null;
		int cant = 0;

		try{ 
			stmt = con.prepareStatement(RegimenFiscalQuerys.getQueryDelRF(esquema));
			stmt.setInt(1, idRegistro);
			cant = stmt.executeUpdate();
		}
		catch(SQLException sql){
			Utils.imprimeLog("", sql);
		}
		catch(Exception e){
			Utils.imprimeLog("", e);
		}
		finally{
			try{
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				
				if (stmtBitacora != null){
					stmtBitacora.close();
				}
				stmtBitacora = null;
				
			}
			catch(Exception e){
				stmt = null;
				stmtBitacora = null;
			}
		}
		return cant;
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONObject buscarConfigurados(Connection con, String esquema, int claveProveedor){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();

        try{
        	StringBuffer sbQuery = new StringBuffer(RegimenFiscalQuerys.getQueryBuscarConfigurados(esquema));
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setInt(1, claveProveedor);
            rs = stmt.executeQuery();
            
			while (rs.next()){
				jsonobj.put(Utils.noNulo(rs.getString(1)), Utils.noNulo(rs.getString(1)));
            }
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
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
	
	
	public ArrayList<RegimenForm> comboClavesRegimen(Connection con, String esquema, int accion) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<RegimenForm> listaDetalle = new ArrayList<>();
        RegimenForm regimenForm = new RegimenForm();
        
        try{
        	
    	   regimenForm.setClaveRegimen("");
    	   regimenForm.setDescripcion("Seleccione un Registro");
    	   listaDetalle.add(regimenForm);
    	   regimenForm = new RegimenForm();
	    	   
            stmt = con.prepareStatement(RegimenFiscalQuerys.getComboClavesRegimen(esquema) + " order by CLAVE_REGIMEN asc");
	        rs = stmt.executeQuery();

	       while (rs.next()){
	    	   regimenForm.setClaveRegimen(Utils.noNuloNormal(rs.getString(1)));
	    	   regimenForm.setDescripcion(Utils.noNuloNormal(rs.getString(1)) + " - " + Utils.noNuloNormal(rs.getString(2)));
	    	   listaDetalle.add(regimenForm);
	    	   regimenForm = new RegimenForm();
	        	
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
        		rs = null;
        		stmt = null;
        	}
        }
        return listaDetalle;
    }
}
