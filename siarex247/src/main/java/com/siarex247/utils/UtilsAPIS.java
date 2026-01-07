package com.siarex247.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.siarex247.leerCuenta.DoRegisterModel;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UtilsAPIS {
	
	
	private static final String HOST_APIS_TIMBRADO = "http://localhost:3333";
	//private static final String HOST_APIS_TIMBRADO = "https://siarex.com";
	private static final String GENERAR_TOKEN = HOST_APIS_TIMBRADO + "/RestJR/services/LoginService/generarToken"; 
	private static final String USER_TOKEN =  "timbrado.express@siarex.com";
	 private static final String ENDPOINT_DOREGISTER =  HOST_APIS_TIMBRADO + "/timbradoLinea/rest/DoRegister/register";
	
	
	public static final Logger logger = Logger.getLogger("siarex247");

	
	public static String generarToken(String usuarioHTTP){
		String tokenGenerado = null;
		JSONObject jsonRespuesta = null;
		try{
			JSONObject jsonEnvio = new JSONObject();
			jsonEnvio.put("user", USER_TOKEN);
			String resApi = ejecutarApi(jsonEnvio, GENERAR_TOKEN);
			
			jsonRespuesta = new JSONObject(resApi);
			
			logger.info("token===>"+jsonRespuesta.getString("token"));
			tokenGenerado = jsonRespuesta.getString("token");
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return tokenGenerado;
	}
	
	public static JSONObject doRegister(DoRegisterModel model, String token) {
		JSONObject jsonRespuesta = null;
		
		try{
			JSONObject jsonEnvio = new JSONObject(model);
			
			
			String resApi = ejecutarApiToken(jsonEnvio, ENDPOINT_DOREGISTER, token);
			
			jsonRespuesta = new JSONObject(resApi);
			
			
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return jsonRespuesta;
	}
	
	
	
	private static String ejecutarApi(JSONObject jsonEnvio, String URL_SERVICIO) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			
			// String URL_FINAL = "https://servicios.siarex.com/RestJR/services/TimbradorService/timbrarJSON2";
			OkHttpClient client = new OkHttpClient().newBuilder()
					   .proxy(Proxy.NO_PROXY)
					   .connectTimeout(7000, TimeUnit.SECONDS)
						.writeTimeout(7000, TimeUnit.SECONDS)
						.readTimeout(7000, TimeUnit.SECONDS)
					  .build();
					MediaType mediaType = MediaType.parse("application/json");
					RequestBody body = RequestBody.create(mediaType, jsonEnvio.toString());
					Request request = new Request.Builder()
					  .url(URL_SERVICIO)
					  .method("POST", body)
					  .addHeader("Content-Type", "application/json")
					  .build();
					response = client.newCall(request).execute();
				
					jsonRespuesta = getRespuesta(response);
					
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return jsonRespuesta;
	}
	
	private static String ejecutarApiToken(JSONObject jsonEnvio, String URL_SERVICIO, String token) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			
			// String URL_FINAL = "https://servicios.siarex.com/RestJR/services/TimbradorService/timbrarJSON2";
			OkHttpClient client = new OkHttpClient().newBuilder()
					   .proxy(Proxy.NO_PROXY)
					   .connectTimeout(7000, TimeUnit.SECONDS)
						.writeTimeout(7000, TimeUnit.SECONDS)
						.readTimeout(7000, TimeUnit.SECONDS)
					  .build();
					MediaType mediaType = MediaType.parse("application/json");
					RequestBody body = RequestBody.create(mediaType, jsonEnvio.toString());
					Request request = new Request.Builder()
					  .url(URL_SERVICIO)
					  .method("POST", body)
					  .addHeader("Content-Type", "application/json")
					  .addHeader("Authorization", "Bearer " + token)
					  .build();
					response = client.newCall(request).execute();
				
					jsonRespuesta = getRespuesta(response);
					
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return jsonRespuesta;
	}
	
	private static String getRespuesta(Response response) {
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
			Utils.imprimeLog("", e);
		}
		return jsonRespuesta;
	}
		 
}
