package ws_api.descarga;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DescargaMasiva {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	private String URL_ADDFIEL = "https://sistema.bovedafacturalo.com/api/addFiel";
	private String URL_CONSULTAR_FECHA = "https://sistema.bovedafacturalo.com/ws/consultarFecha/<<RFC_SOLICITANTE>>";
	private String URL_REPORTE = "https://sistema.bovedafacturalo.com/ws/doReporte";
	private String URL_DESCARGA = "https://sistema.bovedafacturalo.com/ws/descargaCFDI";
	private String URL_RECUPERA = "https://sistema.bovedafacturalo.com/ws/recuperaCFDI";
	private String URL_ACTUALIZA_ESTATUS = "https://sistema.bovedafacturalo.com/ws/actualizaEstatus";
	
	
	private int timeOut = 50000;
	
/*
	

	private void addFormDataPart(Map<String, String> formData) {
		try {
			 String keyForm = null;
			 String dataValue = null;
			if (formData != null && formData.size() > 0) {
	            Iterator<String> it = formData.keySet().iterator();
	            while (it.hasNext()) {
	                keyForm = it.next();
	                dataValue = formData.get(keyForm);
	                build.addFormDataPart(keyForm, dataValue);
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void addFormFilePart(Map<String, String> formFileData) {
		try {
			String keyForm = null;
			String pathFile = null;
			if (formFileData != null && formFileData.size() > 0) {
	            Iterator<String> it = formFileData.keySet().iterator();
	            while (it.hasNext()) {
	                keyForm = it.next();
	                pathFile = formFileData.get(keyForm);
	                build.addFormDataPart(keyForm,pathFile,
	    				    RequestBody.create(MediaType.parse("application/octet-stream"),
	    				    new File(pathFile)));
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private RequestBody createBuildForm() {
		RequestBody body = null;
		try {
			body = build.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}
	*/
	
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
			Utils.imprimeLog("", e);
		}
		return jsonRespuesta;
	}
	
	
	public String addFiel(String rfcSolicitante, String contrasena, String apiKey, String fInicial, String fFinal, String automatico, String pathCer, String pathKey) {
		String jsonRespuesta = "";
		Response responseFiel = null;
		try {
			
			/*
			X509TrustManager TRUST_ALL_CERTS  = new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
				}

				@Override 
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[] {};
				}
			};
			
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
	        sslContext.init(null, new X509TrustManager[] { TRUST_ALL_CERTS }, new java.security.SecureRandom());
			
	        */
				
					OkHttpClient client = new OkHttpClient().newBuilder()
						.proxy(Proxy.NO_PROXY)
						.connectTimeout(timeOut, TimeUnit.SECONDS)
						.writeTimeout(timeOut, TimeUnit.SECONDS)
						.readTimeout(timeOut, TimeUnit.SECONDS)
						.build();

			
					RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("cerFiel",pathCer,
					    RequestBody.create(MediaType.parse("application/o-ctet-stream"),
					    new File(pathCer)))
					  .addFormDataPart("keyFiel",pathKey,
					    RequestBody.create(MediaType.parse("application/octet-stream"),
					    new File(pathKey)))
					  .addFormDataPart("rfc",rfcSolicitante)
					  .addFormDataPart("contrasena",contrasena)
					  .addFormDataPart("apiKey",apiKey)
					  .addFormDataPart("fInicial",fInicial)
					  .addFormDataPart("fFinal",fFinal)
					  .addFormDataPart("automatico",automatico)
					  .build();
					Request request = new Request.Builder()
					  .url(URL_ADDFIEL)
					  .method("POST", body)
					  .build();
					responseFiel = client.newCall(request).execute();
					jsonRespuesta = getRespuesta(responseFiel);
					
					logger.info("AddFiel Exitoso===>"+jsonRespuesta);
					
		} catch (Exception e) {
			Utils.imprimeLog("", e);
			e.printStackTrace();
		}finally {
			try {
				if (responseFiel != null) {
					responseFiel.close();
				}
				responseFiel = null;
			}catch(Exception e) {
				responseFiel = null;
			}
		}
		return jsonRespuesta;
	}
	
	
	
	public String consultarFecha(String rfcSolicitante, String apiKey) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			String URL_FINAL = URL_CONSULTAR_FECHA.replaceAll("<<RFC_SOLICITANTE>>", rfcSolicitante);
			
			OkHttpClient client = new OkHttpClient().newBuilder()
					.proxy(Proxy.NO_PROXY)
					.connectTimeout(timeOut, TimeUnit.SECONDS)
					.writeTimeout(timeOut, TimeUnit.SECONDS)
					.readTimeout(timeOut, TimeUnit.SECONDS)
					.build();
			
					RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("apikey",apiKey)
					  .build();
					Request request = new Request.Builder()
					  .url(URL_FINAL)
					  .method("GET", body)
					  .addHeader("apikey", apiKey)
					  .addHeader("rfc", rfcSolicitante)
					  .build();
					 response = client.newCall(request).execute();
					
					 jsonRespuesta = getRespuesta(response);
					 
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (response != null) {
					response.close();
				}
				response = null;
			}catch(Exception e) {
				response = null;
			}
		}
		return jsonRespuesta;
	}
	
	
	
	public String doReporte(String rfcSolicitante,  String apiKey, String fInicial, String fFinal ) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			OkHttpClient clientReport = new OkHttpClient().newBuilder()
					.proxy(Proxy.NO_PROXY)
					.connectTimeout(timeOut, TimeUnit.SECONDS)
					.writeTimeout(timeOut, TimeUnit.SECONDS)
					.readTimeout(timeOut, TimeUnit.SECONDS)
					.build();
			
					RequestBody bodyReport = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("apikey",apiKey)
					  .addFormDataPart("fInicial",fInicial)
					  .addFormDataPart("fFinal",fFinal)
					  .addFormDataPart("rfc",rfcSolicitante)
					  .addFormDataPart("layout","TIPO_MONEDA")
					  .build();
					Request requestReport = new Request.Builder()
					  .url(URL_REPORTE)
					  .method("POST", bodyReport)
					  .addHeader("rfc", rfcSolicitante)
					  .build();
					response = clientReport.newCall(requestReport).execute();
					jsonRespuesta = getRespuesta(response);
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (response != null) {
					response.close();
				}
				response = null;
			}catch(Exception e) {
				response = null;
			}
		}
		return jsonRespuesta;
	}
	
	
	public String doReporteEstatus(String rfcSolicitante,  String apiKey, String fInicial, String fFinal,  String estatus ) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			OkHttpClient clientReport = new OkHttpClient().newBuilder()
					.proxy(Proxy.NO_PROXY)
					.connectTimeout(timeOut, TimeUnit.SECONDS)
					.writeTimeout(timeOut, TimeUnit.SECONDS)
					.readTimeout(timeOut, TimeUnit.SECONDS)
					.build();
			
					RequestBody bodyReport = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("apikey",apiKey)
					  .addFormDataPart("fInicial",fInicial)
					  .addFormDataPart("fFinal",fFinal)
					  .addFormDataPart("rfc",rfcSolicitante)
					  .addFormDataPart("layout","TIPO_MONEDA")
					  .addFormDataPart("estatus",estatus)
					  .build();
					Request requestReport = new Request.Builder()
					  .url(URL_REPORTE)
					  .method("POST", bodyReport)
					  .addHeader("rfc", rfcSolicitante)
					  .build();
					response = clientReport.newCall(requestReport).execute();
					jsonRespuesta = getRespuesta(response);
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (response != null) {
					response.close();
				}
				response = null;
			}catch(Exception e) {
				response = null;
			}
		}
		return jsonRespuesta;
	}
	
	public String actualizarEstatus(String rfcSolicitante,  String apiKey, String fInicial, String fFinal ) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			OkHttpClient clientReport = new OkHttpClient().newBuilder()
					.proxy(Proxy.NO_PROXY)
					.connectTimeout(timeOut, TimeUnit.SECONDS)
					.writeTimeout(timeOut, TimeUnit.SECONDS)
					.readTimeout(timeOut, TimeUnit.SECONDS)
					.build();
			
					RequestBody bodyReport = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("apikey",apiKey)
					  .addFormDataPart("fechaInicio",fInicial)
					  .addFormDataPart("fechaFin",fFinal)
					  .addFormDataPart("rfc",rfcSolicitante)
					  .build();
					Request requestReport = new Request.Builder()
					  .url(URL_ACTUALIZA_ESTATUS)
					  .method("POST", bodyReport)
					  .addHeader("rfc", rfcSolicitante)
					  .build();
					response = clientReport.newCall(requestReport).execute();
					jsonRespuesta = getRespuesta(response);
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (response != null) {
					response.close();
				}
				response = null;
			}catch(Exception e) {
				response = null;
			}
		}
		return jsonRespuesta;
	}
	
	public String descargaCFDI(String rfcSolicitante,  String apiKey, String fInicial, String fFinal) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			OkHttpClient client = new OkHttpClient().newBuilder()
					.proxy(Proxy.NO_PROXY)
					.connectTimeout(timeOut, TimeUnit.SECONDS)
					.writeTimeout(timeOut, TimeUnit.SECONDS)
					.readTimeout(timeOut, TimeUnit.SECONDS)
					.build();
			
					RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("apikey",apiKey)
					  .addFormDataPart("rfc",rfcSolicitante)
					  .addFormDataPart("fInicial",fInicial)
					  .addFormDataPart("fFinal",fFinal)
					  .build();
					Request request = new Request.Builder()
					  .url(URL_DESCARGA)
					  .method("POST", body)
					  .build();
					response = client.newCall(request).execute();
					jsonRespuesta = getRespuesta(response);
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (response != null) {
					response.close();
				}
				response = null;
			}catch(Exception e) {
				response = null;
			}
		}
		return jsonRespuesta;
	}
	

	public String recuperaCFDI(String rfcSolicitante,  String apiKey, String uuid) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			
			OkHttpClient client = new OkHttpClient().newBuilder()
					.proxy(Proxy.NO_PROXY)
					.connectTimeout(timeOut, TimeUnit.SECONDS)
					.writeTimeout(timeOut, TimeUnit.SECONDS)
					.readTimeout(timeOut, TimeUnit.SECONDS)
					.build();
			
					RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
					  .addFormDataPart("apikey",apiKey)
					  .addFormDataPart("rfc",rfcSolicitante)
					  .addFormDataPart("uuid",uuid)
					  .build();
					Request request = new Request.Builder()
					  .url(URL_RECUPERA)
					  .method("POST", body)
					  .build();
					response = client.newCall(request).execute();
					jsonRespuesta = getRespuesta(response);
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (response != null) {
					response.close();
				}
				response = null;
			}catch(Exception e) {
				response = null;
			}
		}
		return jsonRespuesta;
	}
	
	
	public boolean generarXML(String pathDestino, String codeBase64) {
		boolean bandGenero = false;
		try {
		   byte[] data = Base64.getDecoder().decode(codeBase64);
           FileOutputStream fos = null;
           fos = new FileOutputStream(pathDestino);
           fos.write(data);
           fos.close();
          // logger.info("Archivo XML Generado..."+pathDestino);
           bandGenero = true;
		} catch (Exception e) {
			Utils.imprimeLog("", e);
			bandGenero = false;
		}
		return bandGenero;
	}
	
	public boolean generarZIP(String pathDestino, String codeBase64) {
		boolean bandGenero = false;
		try {
		   byte[] data = Base64.getDecoder().decode(codeBase64);
           FileOutputStream fos = null;
           fos = new FileOutputStream(pathDestino);
           fos.write(data);
           fos.close();
          // logger.info("Archivo XML Generado..."+pathDestino);
           bandGenero = true;
		} catch (Exception e) {
			Utils.imprimeLog("", e);
			bandGenero = false;
		}
		return bandGenero;
	}
	
	public static void main(String[] args) {
		DescargaMasiva d = new DescargaMasiva();
		try {
			 
			
			String jsonRespuesta = d.addFiel("PLA720201746", "Plamex2021", "a7458b44-0860-42a2-9d72-fe84b0e50c57", "2017-10-01", "2023-12-31", "1", 
						"C:\\PERSONAL\\SAT_JACKY\\PLAMEX\\00001000000509230268.cer", "C:\\PERSONAL\\SAT_JACKY\\PLAMEX\\Claveprivada_FIEL_PLA720201746_20211001_101151.key");
			System.err.println("jsonRespuesta===>"+jsonRespuesta);
			   
			
			/*
			String jsonRespuesta = d.consultarFecha("PLA720201746", "a7458b44-0860-42a2-9d72-fe84b0e50c57");
			System.err.println("jsonRespuesta===>"+jsonRespuesta);
			*/
			
			
			/*
			String jsonRespuesta = d.doReporte("MUVR7512295L3", "a7458b44-0860-42a2-9d72-fe84b0e50c57", "2023-04-01", "2023-08-15");
			System.err.println("jsonRespuesta===>"+jsonRespuesta);
			*/
			/*
			ArrayList<String> listaTXT = UtilsFile.leeArchivoTXT("C:\\Users\\jose_\\Desktop\\DESCARGA MASIVA\\TIMBRADOEXPRESS\\PLA720201746\\ZIP.txt");
			String jsonZip = null;
			for (int x = 0; x < listaTXT.size(); x++) {
				jsonZip = listaTXT.get(x);
			}
			*/
			
			/*
			 String jsonRespuesta = d.descargaCFDI("PLA720201746", "a7458b44-0860-42a2-9d72-fe84b0e50c57", "2023-01-01", "2023-01-15");
			/// String jsonRespuesta = "{\"codigo\": 200,\"idRFC\": null,\"zip\": \"UEsDBBQAAAAAAOFSLlcAAAAAAAAAAAAAAAAKAAAAcmVjaWJpZG9zL1BLAwQUAAAAAADhUi5XAAAAAAAAAAAAAAAACQAAAGVtaXRpZG9zL1BLAQIUAxQAAAAAAOFSLlcAAAAAAAAAAAAAAAAKAAAAAAAAAAAAAAD/QQAAAAByZWNpYmlkb3MvUEsBAhQDFAAAAAAA4VIuVwAAAAAAAAAAAAAAAAkAAAAAAAAAAAAAAP9BKAAAAGVtaXRpZG9zL1BLBQYAAAAAAgACAG8AAABPAAAAAAA=\"}";
			 System.err.println("jsonRespuesta===>"+jsonRespuesta);
			
			 JSONObject jsonArray   = new JSONObject(jsonRespuesta);
			 String baseData64 = jsonArray.get("zip").toString();
			// String baseData64 = jsonZip;
			 d.generarXML("C:\\Users\\jose_\\Desktop\\DESCARGA MASIVA\\TIMBRADOEXPRESS\\TOYOTA\\descargaMasiva.zip", baseData64);
			  */			
			
			
			   /*
			String jsonRespuesta = d.recuperaCFDI("PLA720201746", "a7458b44-0860-42a2-9d72-fe84b0e50c57", "CB995084-6215-48B4-A73B-07FF4EED67F3");
			System.err.println("jsonRespuesta===>"+jsonRespuesta);
			
			JSONObject jsonArray   = new JSONObject(jsonRespuesta);
			String baseData64 = jsonArray.get("data").toString();
			d.generarXML("C:\\Users\\jose_\\Desktop\\VALIDAR\\CB995084-6215-48B4-A73B-07FF4EED67F3.xml", baseData64);
			  */
			
			/*
			String uuidTXT = null;
			File fileTXT = new File("C:\\Users\\jose_\\Desktop\\DESCARGA MASIVA\\TIMBRADOEXPRESS\\PLA720201746\\ZIP.txt");
			
			int row = 1;
			ArrayList<String> listaTXT = UtilsFile.leeArchivoTXT(fileTXT.getAbsolutePath());
			for (int x = 0; x < listaTXT.size(); x++) {
				    uuidTXT = null;
					uuidTXT = listaTXT.get(x);
					String jsonRespuesta = d.recuperaCFDI("PLA720201746", "a7458b44-0860-42a2-9d72-fe84b0e50c57", uuidTXT);
					JSONObject jsonArray   = new JSONObject(jsonRespuesta);
					String baseData64 = jsonArray.get("data").toString();
					if (baseData64 == null || "null".equalsIgnoreCase(baseData64)) {
						System.err.println("UUID===>"+uuidTXT+", Archivo null, row==>"+row);
					}else {
						d.generarXML("C:\\Users\\jose_\\Desktop\\DESCARGA MASIVA\\TIMBRADOEXPRESS\\PLA720201746\\"+uuidTXT+".xml", baseData64);
					}
					row++;
			}
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> listaUUID(){
		ArrayList<String> listaTXT = new ArrayList<>();
		try {
			listaTXT.add("");
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return listaTXT;
	}

	
}
