package com.siarex247.catalogos.CentroCostos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CentroCostosBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	 
	private String getRespuesta(Response response) {
		String jsonRespuesta = "";
		try {
			int status = response.code();
	        if (status == HttpURLConnection.HTTP_OK) {
	        	ByteArrayOutputStream result = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	            int length;
	            while ((length = response.body().byteStream().read(buffer)) != -1) {
	                result.write(buffer, 0, length);
	            }
	            jsonRespuesta = result.toString("utf-8");
			     
	        } else {
	            throw new IOException("Server returned non-OK status: " + status);
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonRespuesta;
	}
	
	public String detalleCentros() {
		String jsonRespuesta = "";
		Response response = null;
		try {
			logger.info("ejecutabdi centros de costos...");
			JSONObject jsonEnvio = new JSONObject();
			jsonEnvio.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb3NlLmJ1cmdvc0B0ZWNobm9sb2dpZXMyNDcuY29tIiwiZXhwIjoxNzQxMjkzOTkzLCJpYXQiOjE3NDEyOTIxOTMsIm5hbWUiOiJKT1NFIEdVQURBTFVQRSBCVVJHT1MifQ.-43i73mf4FceF3HwQSWbEo41okmqRIUtTIAbdFadpLQ");
			jsonEnvio.put("nombreEmpresa", "plamex");
			jsonEnvio.put("modulo", "SIAREX247");
			jsonEnvio.put("usuario", "jose.burgos@technologies247.com");
			
			String URL_FINAL = "http://localhost:3333/siarex247Rest/services/CentrosCostosService/detalleCentros";
			
			OkHttpClient client = new OkHttpClient().newBuilder()
					   .proxy(Proxy.NO_PROXY)
					   .connectTimeout(7000, TimeUnit.SECONDS)
						.writeTimeout(7000, TimeUnit.SECONDS)
						.readTimeout(7000, TimeUnit.SECONDS)
					  .build();
					MediaType mediaType = MediaType.parse("application/json");
					RequestBody body = RequestBody.create(mediaType, jsonEnvio.toString());
					Request request = new Request.Builder()
					  .url(URL_FINAL)
					  .method("POST", body)
					  .addHeader("Content-Type", "application/json")
					  .build();
					response = client.newCall(request).execute();
				
					jsonRespuesta = getRespuesta(response);
					logger.info("jsonRespuesta===>"+jsonRespuesta);
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return jsonRespuesta;
	}
	 
	
	@SuppressWarnings("unchecked")
	public Map<String, Object > detalleCentros(Connection conEmpresa, String esquema) {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        JSONObject jsonobj = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        Map<String, Object > mapaRes = new HashMap<String, Object>();
	        try {
	        	StringBuffer sbQuery = new StringBuffer(CentroCostosQuerys.getDetalleCentros(esquema));
	            stmt = conEmpresa.prepareStatement(sbQuery.toString());
	            rs = stmt.executeQuery();
				while(rs.next()) 
	            {
						jsonobj.put("ID_REGISTRO",rs.getInt(1));
						jsonobj.put("NOMBRE_CORTO",Utils.noNulo(rs.getString(2)));
						jsonobj.put("DEPARTAMENTO",Utils.noNulo(rs.getString(3)));
						jsonobj.put("CORREO",Utils.noNuloNormal(rs.getString(4)));
						jsonobj.put("USUARIO_TRAN",Utils.noNuloNormal(rs.getString(5)));
						jsonobj.put("FECHA_TRANS",UtilsFechas.getFechaddMMMyyyyHHss(rs.getTimestamp(6)));
						jsonArray.add(jsonobj);  
			        	jsonobj = new JSONObject();
					
	            }
				mapaRes.put("detalle", jsonArray);
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
	        return mapaRes;
	    }
	 
	
	public int grabarCentroCosto(Connection con, String esquema, String idCentro, String departamento, String correoCentro, String usuarioTrans){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try{ 
			stmt = con.prepareStatement(CentroCostosQuerys.getAltaCentros(esquema));
			stmt.setString(1, idCentro);
			stmt.setString(2, departamento);
			stmt.setString(3, correoCentro);
			stmt.setString(4, usuarioTrans);
			
			int cant = stmt.executeUpdate();
            resultado = cant;
            
		}catch(SQLException sql){
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);
		}catch(Exception e){
			Utils.imprimeLog("", e);
			resultado = 100;
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
	
	
	public int modificarCentroCosto(Connection con, String esquema, int idRegistro, String idCentro, String departamento, String correoCentro, String usuarioTrans){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try{ 
			stmt = con.prepareStatement(CentroCostosQuerys.getModificaCentros(esquema));
			stmt.setString(1, idCentro);
			stmt.setString(2, departamento);
			stmt.setString(3, correoCentro);
			stmt.setString(4, usuarioTrans);
			stmt.setInt(5, idRegistro);
			int cant = stmt.executeUpdate();
            resultado = cant;
            
		}catch(SQLException sql){
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);
		}catch(Exception e){
			Utils.imprimeLog("", e);
			resultado = 100;
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
	
	public int eliminaCentroCosto(Connection con, String esquema, int idRegistro){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try{ 
			stmt = con.prepareStatement(CentroCostosQuerys.getEliminaCentros(esquema));
			stmt.setInt(1, idRegistro);
			
			int cant = stmt.executeUpdate();
            resultado = cant;
            
		}catch(SQLException sql){
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);
		}catch(Exception e){
			Utils.imprimeLog("", e);
			resultado = 100;
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
	
	
	@SuppressWarnings("unchecked")
	public JSONObject buscarCentroCosto(Connection con, String esquema, int idRegistro){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		JSONObject jsonobj = new JSONObject();
		try{ 
			stmt = con.prepareStatement(CentroCostosQuerys.getBuscarCentros(esquema));
			stmt.setInt(1, idRegistro);
            rs = stmt.executeQuery();
            if(rs.next()) {
				jsonobj.put("idRegistro",rs.getInt(1));
				jsonobj.put("idCentro",Utils.noNulo(rs.getString(2)));
				jsonobj.put("departamento",Utils.noNulo(rs.getString(3)));
				jsonobj.put("correoCentro",Utils.noNuloNormal(rs.getString(4)));
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
		return jsonobj;

	}

	
		public ArrayList<CentroCostosForm> comboCentroCosto(Connection con, String esquema, String idLengueje) {
		        PreparedStatement stmt = null;
		        ResultSet rs = null;
		        ArrayList<CentroCostosForm>  listaDetalle = new ArrayList<>();
		        CentroCostosForm centrosForm = new CentroCostosForm();
		        LenguajeBean lenguajeBean = LenguajeBean.instance();
		        try{
					HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(idLengueje, "PAN_MSG_GENERALES");
					String msgSeleccione = null;
					if ("".equalsIgnoreCase(Utils.noNulo(mapaLenguaje.get("MSG_SELECCION_CENTRO")))) {
						msgSeleccione = "Seleccione un Centro de Costo";
					}else {
						msgSeleccione = mapaLenguaje.get("MSG_SELECCION_CENTRO");
					}
					
		        	StringBuffer sbQuery = new StringBuffer(CentroCostosQuerys.getComboCentros(esquema));
		        	// sbQuery.append("group by NOMBRE_CORTO order by NOMBRE_CORTO");
		        	stmt = con.prepareStatement(sbQuery.toString());
			        rs = stmt.executeQuery();
			        centrosForm.setIdCentroCosto("");
			        centrosForm.setDepartamento(msgSeleccione);
			        listaDetalle.add(centrosForm);
			        centrosForm = new CentroCostosForm();
			        
			        while (rs.next()){
			        	centrosForm.setIdCentroCosto(Utils.noNulo(rs.getString(1)));
				        centrosForm.setDepartamento(Utils.noNulo(rs.getString(1)) + " - " + Utils.noNulo(rs.getString(2)));
				        listaDetalle.add(centrosForm);
				        centrosForm = new CentroCostosForm();
			        	
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
	
	 

	 @SuppressWarnings("unchecked")
		public HashMap<String, String> obtenerCentros(Connection conEmpresa, String esquema)
		    {
		        PreparedStatement stmt = null;
		        ResultSet rs = null;
		        HashMap<String, String> mapaResultado = new HashMap<String, String>();
		        try
		        {
		        	StringBuffer sbQuery = new StringBuffer(CentroCostosQuerys.getComboCentros(esquema));
		        	sbQuery.append(" group by NOMBRE_CORTO order by NOMBRE_CORTO");
		        	
		        	stmt = conEmpresa.prepareStatement(sbQuery.toString());
		        	rs = stmt.executeQuery();
		            String idCentro = null;
		            mapaResultado.put("", "");
					
		            while(rs.next()) 
		            {
		            	idCentro = Utils.noNulo(rs.getString(1));
						mapaResultado.put(idCentro, idCentro);
						
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
		        return mapaResultado;
		    }
	 

		public CentroCostosForm consultaCentrosXid(Connection conEmpresa, String esquema, String idCentro){
		        PreparedStatement stmt = null;
		        ResultSet rs = null;
		        CentroCostosForm centroCostosForm = new CentroCostosForm();
		        try{
		        	StringBuffer sbQuery = new StringBuffer(CentroCostosQuerys.getDetalleCentros(esquema));
		        	sbQuery.append(" where NOMBRE_CORTO = ?");
		        	stmt = conEmpresa.prepareStatement(sbQuery.toString());
		        	stmt.setString(1, idCentro);
		            rs = stmt.executeQuery();
					if (rs.next()){
						centroCostosForm.setClaveRegistro(rs.getInt(1));
						centroCostosForm.setIdCentroCosto(Utils.noNulo(rs.getString(2)));
						centroCostosForm.setDepartamento(Utils.noNulo(rs.getString(3)));
						centroCostosForm.setCorreoCentro(Utils.noNuloNormal(rs.getString(4)));
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
		        return centroCostosForm;
		    }
		
		
		public ArrayList<CentroCostosForm> consultaCentrosListaTarea(Connection conEmpresa, String esquema, String idCentro){
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        ArrayList<CentroCostosForm> listaDetalle = new ArrayList<>();
	        CentroCostosForm centroCostosForm = new CentroCostosForm();
	        try{
	        	StringBuffer sbQuery = new StringBuffer(CentroCostosQuerys.getDetalleCentros(esquema));
	        	sbQuery.append(" where NOMBRE_CORTO = ?");
	        	stmt = conEmpresa.prepareStatement(sbQuery.toString());
	        	stmt.setString(1, idCentro);
	            rs = stmt.executeQuery();
				if (rs.next()){
					centroCostosForm.setClaveRegistro(rs.getInt(1));
					centroCostosForm.setIdCentroCosto(Utils.noNulo(rs.getString(2)));
					centroCostosForm.setDepartamento(Utils.noNulo(rs.getString(3)));
					centroCostosForm.setCorreoCentro(Utils.noNuloNormal(rs.getString(4)));
					listaDetalle.add(centroCostosForm);
					centroCostosForm = new CentroCostosForm();
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
	        return listaDetalle;
	    }
		 
	 
}
