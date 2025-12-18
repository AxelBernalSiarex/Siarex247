package com.siarex247.configuraciones.ConfClaveUsoCP;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.utils.Utils;

public class ConfClaveUsoCPBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleUsoCP(Connection con, String esquema) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();

        try{
        	StringBuffer sbQuery = new StringBuffer(ConfClaveUsoCPQuerys.getQueryConsultaUsoCP(esquema));
        	
        	sbQuery.append(" order by A.RFC, A.USO_CFDI, A.CLAVE_PRODUCTO ");

            stmt = con.prepareStatement(sbQuery.toString());
            
            rs = stmt.executeQuery();
            
			while(rs.next()) {
					jsonobj.put("ID_REGISTRO",rs.getInt(1));
					jsonobj.put("RFC",Utils.noNulo(rs.getString(2)));
					jsonobj.put("RAZON_SOCIAL",Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(3))));
					jsonobj.put("USO_CFDI",Utils.noNulo(rs.getString(4)));
					jsonobj.put("CLAVE_PRODUCTO",Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(5))));
					jsonobj.put("DES_USO_CFDI",Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(6))));
					jsonobj.put("DES_CLAVE_PRODUCTO",Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(7))));
					jsonobj.put("DIVISION",Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(8))));
					jsonobj.put("GRUPO",Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(9))));
					jsonobj.put("CLASE",Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(10))));
					
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
	public JSONObject buscarUsoCP(Connection con, String esquema, int idRegistro){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();

        try{
        	StringBuffer sbQuery = new StringBuffer(ConfClaveUsoCPQuerys.getQueryBuscarUsoCP(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setInt(1, idRegistro);
            rs = stmt.executeQuery();
            
			if(rs.next()){
				jsonobj.put("idRegistro",idRegistro);
				jsonobj.put("rfc",Utils.noNulo(rs.getString(1)));
				jsonobj.put("usoCFDI",Utils.noNulo(rs.getString(2)));
				jsonobj.put("claveProducto",Utils.noNulo(rs.getString(3)));
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
	
	
	public String getDescripcionUSOCFDICP(Connection con, String esquema, String idUsoCFDI){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String desUsoCFDI = null;

        try{
        	stmt = con.prepareStatement(ConfClaveUsoCPQuerys.getQueryDesUsoCFDICP(esquema));
        	stmt.setString(1, idUsoCFDI);
            rs = stmt.executeQuery();

            if(rs.next()){
            	desUsoCFDI = Utils.noNulo(rs.getString(1));
            }
            else {
            	desUsoCFDI = "SIN DESCRIPCION";
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
        return desUsoCFDI;
    }
	
	
	
	
	public String getDescripcionClaveProdCP(Connection con, String esquema, String claveProducto){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String desClaveProd = null;

        try{
        	stmt = con.prepareStatement(ConfClaveUsoCPQuerys.getQueryDesClaveCP(esquema));
        	stmt.setString(1, claveProducto);
            rs = stmt.executeQuery();

            if(rs.next()){
            	desClaveProd = Utils.noNulo(rs.getString(1));
            }
            else {
            	desClaveProd = "SIN DESCRIPCION";
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
        return desClaveProd;
    }
	
	
	
	public int grabarUsoCP(Connection con, String esquema, String rfc, String usoCFDI, String claveProducto, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;

		try{ 
			stmt = con.prepareStatement(ConfClaveUsoCPQuerys.getQueryInsertUsoCP(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, rfc);
			stmt.setString(2, usoCFDI);
			stmt.setString(3, claveProducto);
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

	
	
	public int updateUsoCP(Connection con, String esquema, String rfc, String usoCFDI, String claveProducto, int idRegistro, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int cant = 0;

		try{ 
			stmt = con.prepareStatement(ConfClaveUsoCPQuerys.getQueryUpdateUsoCP(esquema));
			stmt.setString(1, rfc);
			stmt.setString(2, usoCFDI);
			stmt.setString(3, claveProducto);
			stmt.setString(4, usuarioHTTP);
			stmt.setInt(5, idRegistro);
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
	
	
	
	public int deleteUsoCP(Connection con, String esquema, int idRegistro, String usuarioHTTP){
		PreparedStatement stmt = null;
		PreparedStatement stmtBitacora = null;
		int cant = 0;

		try{ 
			stmtBitacora = con.prepareStatement(ConfClaveUsoCPQuerys.getQueryDelUsoBitacoraCP(esquema)); 
			stmtBitacora.setString(1, usuarioHTTP);
			stmtBitacora.setString(2, "D");
			stmtBitacora.setInt(3, idRegistro);
			stmtBitacora.executeUpdate();
			
			stmt = con.prepareStatement(ConfClaveUsoCPQuerys.getQueryDeleteUsoCP(esquema));
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
	
	
	
	public String importarProvTXTCP(Connection con, String esquema, File fileTXT, String bandElimina, String usuarioHTTP){
		PreparedStatement stmt = null;
		PreparedStatement stmtElimina = null;
		String mensaje = "";
		int totOK = 0;
		int granTotal = 0;
		Scanner sc = null;

		try{ 
			stmtElimina = con.prepareStatement(ConfClaveUsoCPQuerys.getQueryDelUsoImpCP(esquema));
			stmt = con.prepareStatement(ConfClaveUsoCPQuerys.getQueryInsertUsoCP(esquema));
			stmt.setString(4, usuarioHTTP);
			
			sc = new Scanner(fileTXT);  
			String cadena [] = null;
			String str = null;
			boolean band = true;

			while (sc.hasNext()) {
				if (band && "TRUE".equalsIgnoreCase(bandElimina)) {
		        	int tot = stmtElimina.executeUpdate();
		        	logger.info("Registros eliminados--------->"+tot);
		        	band = false;
				}
				try{
					str = sc.nextLine();
			        cadena = str.trim().replace("|", ";").split(";");
			        if (cadena.length == 3 && buscarProveedorCP(con, esquema, cadena[0]) ){
			        	stmt.setString(1, cadena[0]);
						stmt.setString(2, cadena[1]);
						stmt.setString(3, cadena[2]);
						stmt.executeUpdate();
						totOK++;
			        }
			        granTotal++;        
			        	
				}catch(Exception e){
					Utils.imprimeLog("", e);
					granTotal++;
				}
		      }
			mensaje = "Se han grabado "+totOK+" de "+granTotal+" registros.";
		}
		catch(SQLException sql){
			Utils.imprimeLog("", sql);
		}
		catch(Exception e){
			Utils.imprimeLog("", e);
		}
		finally{
			try{
				if (sc != null){
					sc.close();
				}
				sc = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				if (stmtElimina != null){
					stmtElimina.close();
				}
				stmtElimina = null;
			}catch(Exception e){
				stmt = null;
			}
		}
		return mensaje;
	}
	
	
	public boolean buscarProveedorCP(Connection con, String esquema, String rfc){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean existe = false;

        try{
        	StringBuffer sbQuery = new StringBuffer(ConfClaveUsoCPQuerys.getQueryBuscarRFC(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setString(1, rfc);
            rs = stmt.executeQuery();
            
			if(rs.next()){
				existe = true;
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
        return existe;
    }
}
