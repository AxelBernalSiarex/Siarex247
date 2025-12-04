package com.siarex247.configSistema.CierreAnnio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class CierreAnnioBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	public int altaCierre(Connection con, String esquema, int anio, String fechaApartir, 
			 String mensaje1, String fechaHasta, String mensaje2, String usuario, String tipoCierre)
	    {
	        PreparedStatement stmt = null;
	        PreparedStatement stmtDelete = null;
	        ResultSet rs = null;
	        int resultado = 0;
	        try
	        {
	        	
	        	stmtDelete = con.prepareStatement(CierreAnnioQuerys.getQueryEliminaAnio(esquema));
	        	stmtDelete.setInt(1, anio);
	        	stmtDelete.setString(2, tipoCierre);
	        	stmtDelete.executeUpdate();
	        	
	            stmt = con.prepareStatement(CierreAnnioQuerys.getQueryCierreAnio(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
	            stmt.setInt(1, anio);
	            stmt.setString(2, fechaApartir);
	            stmt.setString(3, mensaje1);
	            stmt.setString(4, fechaHasta);
	            stmt.setString(5, mensaje2);
	            stmt.setString(6, tipoCierre);
	            stmt.setString(7, usuario);
	            
	            int cant = stmt.executeUpdate();
	            if(cant > 0){
	                rs = stmt.getGeneratedKeys();
	                if(rs.next())
	                    resultado = rs.getInt(1);
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
		            if(stmtDelete != null)
		            	stmtDelete.close();
		            stmtDelete = null;
		        }catch(Exception e){
		            stmt = null;
		            stmtDelete = null;
		        }
	        }
	        return resultado;
	    }
	
	
	
	 @SuppressWarnings("unchecked")
		public JSONObject obtenerCierre(Connection con, String esquema, int anio, String tipoCierre)
		    {
		        PreparedStatement stmt = null;
		        ResultSet rs = null;
		        JSONObject jsonobj = new JSONObject();
		        try
		        {
		            stmt = con.prepareStatement(CierreAnnioQuerys.getQueryObtenerInfoCierre(esquema));
		            stmt.setInt(1, anio);
		            stmt.setString(2, tipoCierre);
		            rs = stmt.executeQuery();
	            	jsonobj.put("anio",anio);
	            	jsonobj.put("fechaApartir", "");
	            	jsonobj.put("mensajeError1", "");
	            	jsonobj.put("fechaHasta", "");
	            	jsonobj.put("mensajeError2", "");
		            if (rs.next()){
		            	jsonobj.put("anio", rs.getInt(1));
		            	jsonobj.put("fechaApartir", Utils.noNulo(rs.getDate(2)));
		            	jsonobj.put("mensajeError1", Utils.noNuloNormal(rs.getString(3)));
		            	jsonobj.put("fechaHasta", Utils.noNulo(rs.getDate(4)));
		            	jsonobj.put("mensajeError2", Utils.noNuloNormal(rs.getString(5)));
		            	
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
		 
	 
	 
	 public  String [] obtenerFechas(Connection con, String esquema, int anio, String tipoCierre){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String [] resultado = {"","","","","", "",""};
			try{
				stmt = con.prepareStatement(CierreAnnioQuerys.getQueryObtenerInfoCierre(esquema));
				stmt.setInt(1, anio);
				stmt.setString(2, tipoCierre);
				rs = stmt.executeQuery();
				//SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
				if (rs.next()){
					resultado[0] = Utils.noNulo(rs.getString(1));
					resultado[1] = Utils.noNulo(rs.getString(2));
					resultado[2] = Utils.noNuloNormal(rs.getString(3));
					resultado[3] = Utils.noNulo(rs.getString(4));
					resultado[4] = Utils.noNuloNormal(rs.getString(5));
					if (!resultado[1].equals("")){
						resultado[5] =  UtilsFechas.formatFechaddMMyyyy(resultado[1]);
					}
					if (!resultado[3].equals("")){
						resultado[6] =  UtilsFechas.formatFechaddMMyyyy(resultado[3]);
					}
					
				}
			}catch(Exception e){
				Utils.imprimeLog("", e);
			}finally{
				try{
					if (rs != null){
						rs.close();
					}
					rs = null;
					if (stmt != null){
						stmt.close();
					}
					stmt = null;
					
				}catch(Exception e){
					stmt = null;
				}
			}
			return resultado;
		}
	 
}
