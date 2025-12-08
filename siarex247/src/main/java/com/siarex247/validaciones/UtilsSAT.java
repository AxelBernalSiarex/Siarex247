package com.siarex247.validaciones;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.siarex247.prodigia.api.ProdigiaClient;
import com.siarex247.prodigia.data.ConsultaComprobante;
import com.siarex247.timbrado.DetailError;
import com.siarex247.timbrado.TimbradoBean;
import com.siarex247.timbrado.TimbradoExpressForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

import Services.StatusCfdi.StatusCfdiService;
import Utils.Responses.StatusCfdi.StatusCfdiResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ws_api.RespuestaEstatusSAT;
import ws_api.RespuestaValidacion;

public class UtilsSAT {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	
	 public static void main(String[] args) {
		try {
			String RFC = "PLA720201746";
			String rfcReceptor = "XEXX010101000";
			double TOTAL_NOTA_CREDITO = 5136822.11;
			String UUID_NOTA_CREDITO = "1327CFEA-A6EE-4814-A1FE-DFD6D6B21CC1";
			
			String datosSAT [] = validaSAT(RFC, rfcReceptor, TOTAL_NOTA_CREDITO, UUID_NOTA_CREDITO);
			System.err.println("Valor 1===>"+datosSAT[0]);
			System.err.println("Valor 2===>"+datosSAT[1]);
			System.err.println("Valor 3===>"+datosSAT[2]);
			System.err.println("Valor 4===>"+datosSAT[3]);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static String [] validaSAT(String rfcEmisor, String rfcReceptor, double total, String uuid) {
		String retorno [] = {"","", "", ""};
		try {
			long tiempoIni = System.currentTimeMillis();
			// logger.info("BANDERA_VALIDA_PRODIGIA===>"+UtilsPATH.BANDERA_VALIDA_PRODIGIA);
			// System.err.println("BANDERA_VALIDA_PRODIGIA====>"+UtilsPATH.BANDERA_VALIDA_PRODIGIA);
			 
			if ("prodigia".equalsIgnoreCase(UtilsPATH.BANDERA_VALIDA_PRODIGIA) ) {
				ProdigiaClient.CONTRATO = UtilsPATH.NUMERO_CONTRATO_PRODIGIA;
				ProdigiaClient.AUTHORIZATION = UtilsPATH.AUTHORIZATION_CUENTA_PRODIGIA;
				
				ConsultaComprobante consultaComprobante = ProdigiaClient.consultaStatusCFDIporUUId(uuid, rfcEmisor, rfcReceptor, String.valueOf(total));
				retorno[0] = Utils.noNuloNormal(consultaComprobante.getEstado());
				retorno[1] = Utils.noNuloNormal(consultaComprobante.getCodigoEstatus());
				retorno[2] = Utils.noNuloNormal(consultaComprobante.getConsultaOk());
				retorno[3] = Utils.noNuloNormal(consultaComprobante.getEstatusCfdi());
				
				logger.info("**************** VALIDANDO FACTURA *************************");
				logger.info("codigo===>"+consultaComprobante.getCodigo());
				logger.info("codigoEstatus===>"+consultaComprobante.getCodigoEstatus());
				logger.info("consultaOk===>"+consultaComprobante.getConsultaOk());
				logger.info("esCancelable===>"+consultaComprobante.getEsCancelable());
				logger.info("estado===>"+consultaComprobante.getEstado());
				logger.info("estatusCfdi===>"+consultaComprobante.getEstatusCfdi());
			}else if ("timbrador".equalsIgnoreCase(UtilsPATH.BANDERA_VALIDA_PRODIGIA)) {
				String urlEndPoint = UtilsPATH.ENDPOINT_TIMBRADO_EXPRESSS;
				String API_KEY = UtilsPATH.API_KEY_TIMBRADO_VALIDAR;
				
				String respuestTimbrado = ejecutarEstadoSAT(urlEndPoint, API_KEY, rfcEmisor, rfcReceptor, total, uuid);
				JSONObject jsonRespuesta = new JSONObject(respuestTimbrado);
				// logger.info("jsonRespuesta===>"+jsonRespuesta);
				RespuestaEstatusSAT respuestaEstatusSAT = new RespuestaEstatusSAT();
				respuestaEstatusSAT.setCodigoEstatus(jsonRespuesta.getString("codigoEstatus"));
				respuestaEstatusSAT.setEstado(jsonRespuesta.getString("estado"));
				respuestaEstatusSAT.setEstatusCancelacion(jsonRespuesta.getString("estatusCancelacion"));
				respuestaEstatusSAT.setEsCancelable(jsonRespuesta.getString("esCancelable"));
				
				retorno[0] = Utils.noNuloNormal(respuestaEstatusSAT.getEstado());
				retorno[1] = Utils.noNuloNormal(respuestaEstatusSAT.getCodigoEstatus());
				retorno[2] = Utils.noNuloNormal("200");
				retorno[3] = Utils.noNuloNormal(respuestaEstatusSAT.getEsCancelable());
				
				
			}else {
				 
				StatusCfdiService app = new StatusCfdiService("https://consultaqr.facturaelectronica.sat.gob.mx/ConsultaCFDIService.svc", "http://tempuri.org/IConsultaCFDIService/Consulta");
				StatusCfdiResponse response = null;
				response = (StatusCfdiResponse) app.StatusCfdi(rfcEmisor, rfcReceptor, String.valueOf(total), uuid);
				retorno[0] = response.estado;
				retorno[1] = response.codigoEstatus;
				retorno[2] = response.Status;
				retorno[3] = String.valueOf(response.HttpStatusCode);
				/*
				logger.info("**************** VALIDANDO FACTURA UUID ("+uuid+" ) *************************");
				logger.info("codigoEstatus===>"+retorno[1]);
				logger.info("consultaOk===>"+retorno[3]);
				logger.info("estado===>"+retorno[0]);
				logger.info("estatusCfdi===>"+retorno[2]);
				*/
			 }
			logger.info("Tiempo total Validacion en SAT : ("+uuid+" ) "+(System.currentTimeMillis() - tiempoIni) );
			//String expect_status = "success";
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return retorno;
	}
	 
	
	
	public static TimbradoExpressForm validaSATimbradoExpress(String nombreEmpresa, long folioEmpresa, String pathXML, String usuarioHTTP) {
		String retorno [] = {"","", "", ""};
		TimbradoBean timbradoBean = new TimbradoBean();
		TimbradoExpressForm timbradoExpressForm = new TimbradoExpressForm();
		try {
			long tiempoIni = System.currentTimeMillis();
			
			String urlEndPoint = UtilsPATH.ENDPOINT_TIMBRADO_EXPRESSS;
			String API_KEY = UtilsPATH.API_KEY_TIMBRADO_VALIDAR;
			String xmlCFDI = UtilsFile.leeArchivo(pathXML);
			logger.info("********** ANTES DEL LLAMADO ********");
			logger.info("urlEndPoint==>"+urlEndPoint);
			logger.info("API_KEY==>"+API_KEY);
			logger.info("xmlCFDI==>"+xmlCFDI);
			 
			/*
			ServicioTimbradoWSPortTypeProxy sampleServicioTimbradoWSPortTypeProxyid = new ServicioTimbradoWSPortTypeProxy();
			sampleServicioTimbradoWSPortTypeProxyid.setEndpoint(urlEndPoint);
			
			RespuestaValidacion respuestaValidacion = sampleServicioTimbradoWSPortTypeProxyid.validar(API_KEY,xmlCFDI);
			*/
			String respuestTimbrado = ejecutarValidacion(urlEndPoint, API_KEY, xmlCFDI);
			JSONObject jsonRespuesta = new JSONObject(respuestTimbrado);
			RespuestaValidacion respuestaValidacion = new RespuestaValidacion();
			respuestaValidacion.setCode(jsonRespuesta.getString("code"));
			respuestaValidacion.setMessage(jsonRespuesta.getString("message"));
			respuestaValidacion.setData(jsonRespuesta.getString("data"));
			
			/*
			RespuestaValidacion respuestaValidacion = new RespuestaValidacion();
			respuestaValidacion.setCode("200");
			respuestaValidacion.setData("{\"detail\":[{\"detail\":[{\"message\":\"OK\",\"messageDetail\":\"Validacion de Estructura Correcta\",\"type\":1,\"typeValue\":\"Information\"}],\"section\":\"CFDI33 - Validacion de Estructura\"},{\"detail\":[{\"message\":\"CFDI33102 - El resultado de la digesti\\u00f3n debe ser igual al resultado de la desencripci\\u00f3n del sello.\",\"messageDetail\":\"CadenaOriginal: ||3.3|TF|78571578|2017-12-22T12:33:13|99|00001000000402403936|VENTA A PLAZOS|17849.00|10178.32|MXN|8897.99|I|PPD|22320|RDI841003QJ4|Radiom\\u00c3\\u00b3vil Dipsa, S.A. de C.V.|623|PLA720201746|PLAMEX SA DE CV|P01|43191501|000000000070011022|2.000|H87|PZA|APPLE IPHONE 6 SPACE GRAY 32GB-CLA|8824.50|17649.00|9978.32|7670.68|002|Tasa|0.160000|1227.31|43222900|000000000007004190|2.000|H87|PZA|TARJETA SIM TCM013 2FF\\/3FF\\/4FF TARIFARIO|100.00|200.00|200.00|002|Tasa|0.160000|1227.31|1227.31||\",\"type\":0,\"typeValue\":\"Error\"}],\"section\":\"CFDI33 - Validaciones Proveedor Comprobante ( CFDI33 )\"},{\"detail\":[{\"message\":\"OK\",\"messageDetail\":\"CFDI33 - Validaciones Proveedor Complemento tfd:TimbreFiscalDigital Correcta\",\"type\":1,\"typeValue\":\"Information\"}],\"section\":\"CFDI33 - Validaciones Proveedor Complemento tfd:TimbreFiscalDigital\"}],\"cadenaOriginalSAT\":\"||1.1|0bbecec0-cd67-4189-8f60-f92a6d23f9d3|2017-12-22T12:35:41|FID080111867|Fa\\/GoBkqk1ucRDOULqdBPjL92YcTra7wWE239rqdlFaZlzY8Kb6k\\/+XWsiN49Ux1VrjEv5qR8vMz3oWryKZnVrtMwjqLjMeSqA0\\/U4XKafrBWT3WgJjiPsHzfGFIVT72pNBSFweDJIOyxa3iLu7pmzH9hW3Xa5G\\/IgF8kzdhpaL6s31mqjJQhbwWgV4dr1NAAaj0ND09LEecI1JzQ\\/Ub2n9pcfmdbCmfBjIuUJPgVJJYIYhowHEE9uVe8O6IQCc+6PDuvsaZXOHztbpj7ovcQw\\/MHXkzdbQZXUfTeMx9xz+acPc+I+TiTnsrQOvo\\/6lKtwGhUI9tH1Y736RwqSDJvw==|00001000000405112669||\",\"cadenaOriginalComprobante\":\"||3.3|TF|78571578|2017-12-22T12:33:13|99|00001000000402403936|VENTA A PLAZOS|17849.00|10178.32|MXN|8897.99|I|PPD|22320|RDI841003QJ4|Radiom\\u00c3\\u00b3vil Dipsa, S.A. de C.V.|623|PLA720201746|PLAMEX SA DE CV|P01|43191501|000000000070011022|2.000|H87|PZA|APPLE IPHONE 6 SPACE GRAY 32GB-CLA|8824.50|17649.00|9978.32|7670.68|002|Tasa|0.160000|1227.31|43222900|000000000007004190|2.000|H87|PZA|TARJETA SIM TCM013 2FF\\/3FF\\/4FF TARIFARIO|100.00|200.00|200.00|002|Tasa|0.160000|1227.31|1227.31||\",\"uuid\":\"0bbecec0-cd67-4189-8f60-f92a6d23f9d3\",\"statusSat\":\"No encontrado\",\"statusCodeSat\":\"N - 602: Comprobante no encontrado.\",\"status\":\"success\"}");
			respuestaValidacion.setMessage("Solicitud de validación procesada.");
			*/
			
			/*
			logger.info("code==>"+respuestaValidacion.getCode());
			logger.info("data===>"+respuestaValidacion.getData());
			logger.info("message==>"+respuestaValidacion.getMessage());
			*/
			timbradoExpressForm.setCode(respuestaValidacion.getCode());
			if (respuestaValidacion != null && "200".equalsIgnoreCase(respuestaValidacion.getCode())) {
				
				JSONObject jsonObject = new JSONObject(respuestaValidacion.getData());
				String uuid = jsonObject.get("uuid").toString();
				String statusSat = jsonObject.get("statusSat").toString();
				String statusCodeSat = jsonObject.get("statusCodeSat").toString();
				String status = jsonObject.get("status").toString();
				
				
				logger.info("uuid==>"+uuid);
				logger.info("statusSat==>"+statusSat);
				logger.info("statusCodeSat==>"+statusCodeSat);
				logger.info("status==>"+status);
				
				
				timbradoExpressForm.setStatusSat(statusSat);
				timbradoExpressForm.setStatusCodeSat(statusCodeSat);
				timbradoExpressForm.setStatus(status);
				
				retorno[0] = jsonObject.get("status").toString();
				retorno[1] = jsonObject.get("statusCodeSat").toString();
				retorno[2] = jsonObject.get("statusSat").toString();
				
				timbradoBean.guardarTimbrado(nombreEmpresa, folioEmpresa, "validar", uuid, respuestaValidacion.getCode(), respuestaValidacion.getData(), respuestaValidacion.getMessage(), usuarioHTTP);
				JSONArray jsonArray = new JSONArray(jsonObject.get("detail").toString());
				
				DetailError detailError = new DetailError();
				for (int x = 0; x < jsonArray.length(); x++) {
					JSONObject jsonLista = (JSONObject) jsonArray.get(x);
					/*
					logger.info("************************************************************************************");
					logger.info("detail==>"+jsonLista.get("detail"));
					logger.info("section==>"+jsonLista.get("section"));
					*/
					JSONArray jsonSubDetail = new JSONArray(jsonLista.get("detail").toString());
					for (int y = 0; y < jsonSubDetail.length(); y++) {
						JSONObject jsonSubLista = (JSONObject) jsonSubDetail.get(y);
						// logger.info("-------------------------------------------------------------------------------------");
						if ("Error".equalsIgnoreCase(jsonSubLista.get("typeValue").toString())) {
							detailError.setMessage(Utils.noNulo(jsonSubLista.get("message")).toString());
							detailError.setTypeValue(Utils.noNulo(jsonSubLista.get("typeValue")).toString());
							detailError.setMessageDetail(Utils.noNulo(jsonSubLista.get("messageDetail")).toString());
							timbradoExpressForm.getListaErrores().add(detailError);
							detailError = new DetailError();
						}
						/*
						logger.info("typeValue======>"+jsonSubLista.get("typeValue"));
						logger.info("message========>"+jsonSubLista.get("message"));
						logger.info("messageDetail==>"+jsonSubLista.get("messageDetail"));
						*/
					}
				}
				
			}else {
				timbradoBean.guardarTimbrado(nombreEmpresa, folioEmpresa, "validar", null, respuestaValidacion.getCode(), respuestaValidacion.getData(), respuestaValidacion.getMessage(), usuarioHTTP);
			}
			
			logger.info("************** Total de Tiempo SAT : "+(System.currentTimeMillis() - tiempoIni) );
			//String expect_status = "success";
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return timbradoExpressForm;
	}
	
	
	
	private static String ejecutarValidacion(String endPoint,String API_KEY, String xmlCFDI) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			
			JSONObject jsonEnvio = new JSONObject();
			jsonEnvio.put("endPoint", endPoint);
			jsonEnvio.put("apiKey", API_KEY);
			jsonEnvio.put("xmlCFDI", xmlCFDI);
			
			
			String URL_FINAL = "https://servicios.siarex.com/RestJR/services/ValidacionService/validarXML";
			OkHttpClient client = new OkHttpClient().newBuilder()
					   .proxy(Proxy.NO_PROXY)
					   .connectTimeout(5000, TimeUnit.SECONDS)
						.writeTimeout(5000, TimeUnit.SECONDS)
						.readTimeout(5000, TimeUnit.SECONDS)
					  .build();
					MediaType mediaType = MediaType.parse("application/json");
					RequestBody body = RequestBody.create(mediaType, jsonEnvio.toString());
					Request request = new Request.Builder()
					  .url(URL_FINAL)
					  .method("POST", body)
					  .addHeader("Content-Type", "application/json")
					  .build();
					response = client.newCall(request).execute();
				
					jsonRespuesta =  getRespuesta(response);
					
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return jsonRespuesta;
	}
	

	private static String ejecutarEstadoSAT(String endPoint, String API_KEY, String rfcEmisor, String rfcReceptor, double total, String uuid) {
		String jsonRespuesta = "";
		Response response = null;
		try {
			
			JSONObject jsonEnvio = new JSONObject();
			jsonEnvio.put("endPoint", endPoint);
			jsonEnvio.put("apiKey", API_KEY);
			jsonEnvio.put("uuid", uuid);
			jsonEnvio.put("rfcEmisor", rfcEmisor);
			jsonEnvio.put("rfcReceptor", rfcReceptor);
			jsonEnvio.put("total", total);
			
			String URL_FINAL = "https://servicios.siarex.com/RestJR/services/ValidacionService/consultarEstadoSAT";
			OkHttpClient client = new OkHttpClient().newBuilder()
					   .proxy(Proxy.NO_PROXY)
					   .connectTimeout(5000, TimeUnit.SECONDS)
						.writeTimeout(5000, TimeUnit.SECONDS)
						.readTimeout(5000, TimeUnit.SECONDS)
					  .build();
					MediaType mediaType = MediaType.parse("application/json");
					RequestBody body = RequestBody.create(mediaType, jsonEnvio.toString());
					Request request = new Request.Builder()
					  .url(URL_FINAL)
					  .method("POST", body)
					  .addHeader("Content-Type", "application/json")
					  .build();
					response = client.newCall(request).execute();
				
					jsonRespuesta =  getRespuesta(response);
					
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return jsonRespuesta;
	}
	
	private  static String getRespuesta(Response response) {
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
