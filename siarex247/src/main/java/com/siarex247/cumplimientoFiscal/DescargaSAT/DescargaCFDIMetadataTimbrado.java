package com.siarex247.cumplimientoFiscal.DescargaSAT;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaBean;
import com.siarex247.cumplimientoFiscal.BovedaEmitidos.BovedaEmitidosBean;
import com.siarex247.cumplimientoFiscal.BovedaNomina.BovedaNominaBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;

import ws_api.descarga.DescargaMasiva;

public class DescargaCFDIMetadataTimbrado {

	public static final Logger logger = Logger.getLogger("siarex247");
	final String usuarioHTTP = "procesos@siarex.com";
	
	public void monitoreaCorreo(){
		try{
				AccesoBean accesoBean = new AccesoBean();
				ConexionDB connPool = new ConexionDB();
				Connection con = null;
				ResultadoConexion rc = null;
				try{
					rc = connPool.getConnectionSiarex();
					con = rc.getCon();
					ArrayList<EmpresasForm> listaEmpresas = accesoBean.listaEmpresas(con, rc.getEsquema()); //  detalleEmpresas(con, "siarex");
					EmpresasForm empresasForm = null;
					for (int y = 0; y < listaEmpresas.size(); y++){
						empresasForm = listaEmpresas.get(y);
						// logger.info("Buscando Correos de la empresa : "+empresasForm.getEmailDominio());
						// logger.info("estatus : "+empresasForm.getEstatus());
						if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
							try {
								if ("toyota".equalsIgnoreCase(empresasForm.getEsquema()) || "tmmbcservicios".equalsIgnoreCase(empresasForm.getEsquema())) {
									
								}else {
									logger.info("Inciando descarga timbrado de la empresa : "+empresasForm.getEsquema());
									iniciaProcesoDescarga(empresasForm);	
								}
								
							} catch (Exception e) {
								Utils.imprimeLog("", e);
							}
						}
					}
				}catch(Exception e){
					Utils.imprimeLog("monitoreaCorreo ", e);
				}finally{
					try{
						if (con != null){
							con.close();
						}
					}catch(Exception e){
						con = null;
					}
				}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	

 public void iniciaProcesoDescarga(EmpresasForm empresasForm) {
	 ConexionDB connPool = null;
     ResultadoConexion rcEmpresa = null;
     Connection conEmpresa = null;
     
     DescargaMasiva descargaMasivaBean = new DescargaMasiva();
     try{
		String esquemaEmpresa = empresasForm.getEsquema();
        connPool = new ConexionDB();
        rcEmpresa = connPool.getConnection(esquemaEmpresa);
        conEmpresa = rcEmpresa.getCon();
        
		String fechaProceso = getFechaHoy(conEmpresa, rcEmpresa.getEsquema());  
         //
		String diaMes = fechaProceso.substring(8); 
		boolean isEjecuto = isEjecuto(conEmpresa, rcEmpresa.getEsquema(), fechaProceso);
		
		String fechaHora = UtilsFechas.getFechaActual(); 
		
		int horaDia = Integer.parseInt(fechaHora.substring(11, 13));
		String fechaDescarga = getFechaAyer(conEmpresa, rcEmpresa.getEsquema());
		
		if (horaDia >= 4) {
			if (isEjecuto) {
				String apiKey = UtilsPATH.API_KEY_TIMBRADO_DESCARGA;
				String rfcReceptor = empresasForm.getRfc();
				String rutaDescarga = UtilsPATH.RUTA_PUBLIC_HTML + esquemaEmpresa + File.separator + "DESCARGA_SAT" + File.separator + "CFDI_TIMBRADO" + File.separator + fechaDescarga + File.separator;  // +"/descargaMasiva_" + fechaHoy + ".zip";
				
				
				ArrayList<ProcesoDescargaSATForm> listaSolicitudes = consultarSolicitud(conEmpresa, rcEmpresa.getEsquema(), fechaDescarga, "CFDI_TIMBR");
				String jsonRespuesta = null;
				String fechaInicio = null;
				String fechaFinal = null;
				ProcesoDescargaSATForm procDescargaSatForm = null;
				for (int x = 0; x < listaSolicitudes.size(); x++) {
					procDescargaSatForm = listaSolicitudes.get(x);
					fechaInicio = getUltimaCorrida(conEmpresa, rcEmpresa.getEsquema());
					fechaFinal = procDescargaSatForm.getFechaDescarga();
					logger.info("fechaInicio......"+fechaInicio);
					logger.info("fechaFinal......"+fechaFinal);
					
					if ("DESCARGA".equalsIgnoreCase(procDescargaSatForm.getAccionSat()) && "INI".equalsIgnoreCase(procDescargaSatForm.getEstatusDescarga())) {
						
								String rutaFinalDescrga = rutaDescarga + procDescargaSatForm.getTipoComprobante() + File.separator +  "descargaMasivaTimbrado.zip";
								String rutaFinalDirectorio = rutaDescarga + procDescargaSatForm.getTipoComprobante() + File.separator;
								/*
								logger.info("rutaDescarga====>"+rutaDescarga);
								logger.info("rfcReceptor====>"+rfcReceptor);
								logger.info("rutaFinalDescrga====>"+rutaFinalDescrga);
								logger.info("rutaFinalDirectorio====>"+rutaFinalDirectorio);
								logger.info("fechaInicio====>"+fechaInicio);
								logger.info("fechaFinal====>"+fechaFinal);
								*/
								jsonRespuesta = descargaMasivaBean.descargaCFDI(rfcReceptor, apiKey, fechaInicio , fechaFinal);
								// logger.info("jsonRespuesta===>"+jsonRespuesta);
								 if (Utils.noNulo(jsonRespuesta).equals("")) {
									actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "DESCARGA", procDescargaSatForm.getSolicitudSat(), procDescargaSatForm.getPaqueteSat(), "ERR", "ERROR AL DESCARGAR FACTURAS ");							
								 }else {
									actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "BOVEDA", "", procDescargaSatForm.getPaqueteSat(), "BOV", "SOLICITUD DE DESCARGA CON EXITO");							
									JSONObject jsonArray   = new JSONObject(jsonRespuesta);
									String baseData64 = jsonArray.get("zip").toString();
									// String baseData64 = jsonZip;
									
									File fileDescarga = new File(rutaFinalDirectorio);
									if (!fileDescarga.exists()) {
										fileDescarga.mkdirs();
									}
									descargaMasivaBean.generarZIP(rutaFinalDescrga, baseData64);
								 }
								// logger.info("rutaFinalDescrga ===>"+rutaFinalDescrga );
								// File fileDescarga = new File(rutaFinalDirectorio);
								// fileDescarga.mkdirs();
						
					}else if ("boveda".equalsIgnoreCase(procDescargaSatForm.getAccionSat()) && "BOV".equalsIgnoreCase(procDescargaSatForm.getEstatusDescarga())) {
						int totalDescargaZIP = 0;
						
							String rutaCarpeta = rutaDescarga + procDescargaSatForm.getTipoComprobante() + File.separator;
							totalDescargaZIP = 0;
							
								String archivoZip = rutaDescarga + procDescargaSatForm.getTipoComprobante() + File.separator +  "descargaMasivaTimbrado.zip";
								ZipFiles.unzip(archivoZip, rutaCarpeta);
								boolean descomprime = true;
								if (descomprime) {
									String recibidos = rutaCarpeta  + "recibidos" + File.separator;
									String emitidos  = rutaCarpeta  + "emitidos"  + File.separator;
									File fileRecibidos = new File(recibidos);
									File fileEmitidos = new File(emitidos);
									Integer arrResultado [] = {0,0,0,0,0};
									totalDescargaZIP = fileRecibidos.listFiles().length + fileEmitidos.listFiles().length;
									
									// quitar comentario de recibidos
									cargarBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, fileRecibidos, arrResultado, true);
									
									//logger.info("arrResultado arriba ===>"+arrResultado);
									cargarBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, fileEmitidos, arrResultado, false);
									//logger.info("arrResultado abajo ===>"+arrResultado);
									
									try {
										FileUtils.deleteDirectory(fileRecibidos);
										FileUtils.deleteDirectory(fileEmitidos);
										fileRecibidos.delete();
										fileEmitidos.delete();
										
									}catch(Exception er) {
										Utils.imprimeLog("", er);
									}
									
									
								    int regExitoso = arrResultado[0];
									int regDuplicado = arrResultado[1];
									int regErrorRFC = arrResultado[2];
									// int numFilesXML = arrResultado[3];
									int numFilesNomina = arrResultado[4];
									// int totalXML = regExitoso + regDuplicado + regErrorRFC + numFilesXML;
						        	/*
						        	logger.info("regExitoso ===>"+regExitoso);
						        	logger.info("regDuplicado ===>"+regDuplicado);
						        	logger.info("regErrorRFC ===>"+regErrorRFC);
						        	logger.info("numFilesXML ===>"+numFilesXML);
						        	logger.info("numFilesNomina ===>"+numFilesNomina);
						        	logger.info("totalXML ===>"+totalXML);
						        	
						        	logger.info("toArreglo del arreglo ===>"+totalDescargaZIP);
						        	*/
						        	actualizarTotales(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), totalDescargaZIP, regExitoso, regDuplicado, regErrorRFC, numFilesNomina);
								}
							    actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "DOREPORTE", procDescargaSatForm.getSolicitudSat(), procDescargaSatForm.getPaqueteSat(), "INI", "PROCESO DE ACTUALIZACION DE METADATA");
							    actualizarEncontradosBovedaRecibidos(conEmpresa, rcEmpresa.getEsquema());
							    actualizarEncontradosBovedaEmitidos(conEmpresa, rcEmpresa.getEsquema());
							    actualizarEncontradosBovedaNomina(conEmpresa, rcEmpresa.getEsquema());
					}else if ("DOREPORTE".equalsIgnoreCase(procDescargaSatForm.getAccionSat()) && "INI".equalsIgnoreCase(procDescargaSatForm.getEstatusDescarga())) {
						// aqui actualiza el metadata de nomina y emitidos
 					  
						/*
						String arrFechasIni [] = {//"2018-01-01", "2018-02-01", "2018-03-01", "2018-04-01", "2018-05-01", "2018-06-01", "2018-07-01", "2018-08-01", "2018-09-01", "2018-10-01", "2018-11-01", "2018-12-01",
												  //"2019-01-01", "2019-02-01", "2019-03-01", "2019-04-01", "2019-05-01", "2019-06-01", "2019-07-01", "2019-08-01", "2019-09-01", "2019-10-01", "2019-11-01", "2019-12-01",
												  //"2020-01-01", "2020-02-01", "2020-03-01", "2020-04-01", "2020-05-01", "2020-06-01", "2020-07-01", "2020-08-01", "2020-09-01", "2020-10-01", "2020-11-01", "2020-12-01",
												  //"2021-01-01", "2021-02-01", "2021-03-01", "2021-04-01", "2021-05-01", "2021-06-01", "2021-07-01", "2021-08-01", "2021-09-01", "2021-10-01", "2021-11-01", "2021-12-01",
												  //"2022-01-01", "2022-02-01", "2022-03-01", "2022-04-01", "2022-05-01", "2022-06-01", "2022-07-01", "2022-08-01", "2022-09-01", "2022-10-01", "2022-11-01", "2022-12-01",
												  //"2023-01-01", "2023-02-01", "2023-03-01", "2023-04-01", "2023-05-01", "2023-06-01", "2023-07-01", "2023-08-01", "2023-09-01", "2023-10-01", "2023-11-01", "2023-12-01",
												  //"2024-01-01", "2024-02-01", "2024-03-01", "2024-04-01", "2024-05-01", "2024-06-01", "2024-07-01", "2024-08-01", "2024-09-01", "2024-10-01", "2024-11-01", "2024-12-01", 
												  //"2025-01-01", "2025-02-01", "2025-03-01", "2025-04-01", "2025-05-01", "2025-06-01", "2025-07-01"};
						
						
						String arrFechasFin [] = {//"2018-01-31", "2018-02-29", "2018-03-31", "2018-04-30", "2018-05-31", "2018-06-30", "2018-07-31", "2018-08-30", "2018-09-30", "2018-10-31", "2018-11-30", "2018-12-31",
								  				  //"2019-01-01", "2019-02-28", "2019-03-31", "2019-04-30", "2019-05-31", "2019-06-30", "2019-07-31", "2019-08-30", "2019-09-30", "2019-10-31", "2019-11-30", "2019-12-31",
								  				  //"2020-01-01", "2020-02-28", "2020-03-31", "2020-04-30", "2020-05-31", "2020-06-30", "2020-07-31", "2020-08-30", "2020-09-30", "2020-10-31", "2020-11-30", "2020-12-31",
								  				  //"2021-01-01", "2021-02-28", "2021-03-31", "2021-04-30", "2021-05-31", "2021-06-30", "2021-07-31", "2021-08-30", "2021-09-30", "2021-10-31", "2021-11-30", "2021-12-31",
								  				  //"2022-01-01", "2022-02-29", "2022-03-31", "2022-04-30", "2022-05-31", "2022-06-30", "2022-07-31", "2022-08-30", "2022-09-30", "2022-10-31", "2022-11-30", "2022-12-31",
								  				  //"2023-01-01", "2023-02-28", "2023-03-31", "2023-04-30", "2023-05-31", "2023-06-30", "2023-07-31", "2023-08-30", "2023-09-30", "2023-10-31", "2023-11-30", "2023-12-31",
								  				  // "2024-01-31", "2024-02-28", "2024-03-31", "2024-04-30", "2024-05-31", "2024-06-30", "2024-07-31", "2024-08-30", "2024-09-30", "2024-10-31", "2024-11-30", "2024-12-31", 
								  				  // "2025-01-31", "2025-02-28", "2025-03-31", "2025-04-30", "2025-05-31", "2025-06-30", "2025-07-31"};
				 
						 for (int a = 0; a < arrFechasIni.length; a++) {
 							*/
							try {
								// logger.info("********* INICIANDO DOREPORTE A TIMBRADO ************"+arrFechasIni[a]+", FechaFinal===>"+arrFechasFin[a]);
								// fechaInicio = arrFechasIni[a];
								// fechaFinal = arrFechasFin[a];
								jsonRespuesta = descargaMasivaBean.doReporte(rfcReceptor, apiKey, fechaInicio, fechaFinal);
								// logger.info("Finalizo respuesta de API......"+jsonRespuesta);
								if (jsonRespuesta != null) {
									JSONObject jsonArray   = new JSONObject(jsonRespuesta);
									if (jsonArray.get("data").toString() != null) {
										try {
											logger.info("totalRegistro===>"+jsonArray.getInt("totalRegistros"));	
										}catch(Exception e) {
											logger.info("Error al leer el Total de Regstros...");
										}
										
										JSONArray jsonData   = new JSONArray(jsonArray.get("data").toString());
										int contador = 1;
										ArrayList<JSONObject> listaDetalle = new ArrayList<>();
										int resultado = 1;
										for (int y = 0; y < jsonData.length(); y++) {
											if (resultado > 0) {
												JSONObject jsonRow   = new JSONObject(jsonData.get(y).toString());
												listaDetalle.add(jsonRow);
												if (contador == 1000) {
													// resultado = guardarMetadataTimbrado(conEmpresa, rcEmpresa.getEsquema(),listaDetalle);
													// listaDetalle = new ArrayList<>();
													contador = 1;
												}else {
													contador++;
													resultado = 1;
												}
											}
										}
										// logger.info("listaDetalle===>"+listaDetalle.size());	
										if (listaDetalle.size() > 0) {
											resultado = guardarMetadataTimbrado(conEmpresa, rcEmpresa.getEsquema(),listaDetalle);
										}
									}
								}
							}catch(Exception e) {
								Utils.imprimeLog("", e);
							}
						// }
						
						actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "REVALIDA", procDescargaSatForm.getSolicitudSat(), procDescargaSatForm.getPaqueteSat(), "REV", "INICIANDO PROCESO DE REVALIDACION");
					}else if ("REVALIDA".equalsIgnoreCase(procDescargaSatForm.getAccionSat()) && "REV".equalsIgnoreCase(procDescargaSatForm.getEstatusDescarga())) {
						revalidarBoveda(conEmpresa, rcEmpresa.getEsquema(), empresasForm, rfcReceptor, procDescargaSatForm.getFechaDescarga(), procDescargaSatForm.getTipoComprobante());
						actualizarEncontradosBovedaRecibidos(conEmpresa, rcEmpresa.getEsquema());
						actualizarEncontradosBovedaEmitidos(conEmpresa, rcEmpresa.getEsquema());
						actualizarEncontradosBovedaNomina(conEmpresa, rcEmpresa.getEsquema());
						actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "NOTIFICA", procDescargaSatForm.getSolicitudSat(), procDescargaSatForm.getPaqueteSat(), "NOT", "ENVIAR NOTIFICACION");
					}else if ("NOTIFICA".equalsIgnoreCase(procDescargaSatForm.getAccionSat()) && "NOT".equalsIgnoreCase(procDescargaSatForm.getEstatusDescarga())) {
						enviarNotificacion(conEmpresa, rcEmpresa.getEsquema(), empresasForm);
						actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "NOTIFICA", procDescargaSatForm.getSolicitudSat(), procDescargaSatForm.getPaqueteSat(), "FIN", "PROCESO DE NOTIFICACION FINALIZADO");
						
					}
				}
			}else {
				if (horaDia >= 1) {
					grabarProceso(conEmpresa, rcEmpresa.getEsquema(), 1, diaMes);
					//registrarInicioSolicitud(conEmpresa, rcEmpresa.getEsquema(), "CFDI_TIMBR", "Todos", "DESCARGA", fechaDescarga);
					registrarInicioSolicitud(conEmpresa, rcEmpresa.getEsquema(), "CFDI_TIMBR", "Todos", "NOTIFICA", fechaDescarga);
					// SE GUARDA EN HISTORICO DE DESCARGAS LAS SOLICITUDES
				}
			}
		}
		
	 }catch(Exception e) {
		Utils.imprimeLog("", e);
	 }finally{
        try{
            if(conEmpresa != null)
            	conEmpresa.close();
            conEmpresa = null;
            
        }catch(Exception e){
            conEmpresa = null;
        }
     }
  }
 
 
 	public int guardarMetadataTimbrado(Connection con, String esquema, ArrayList<JSONObject> listaDetalle) {
 		PreparedStatement stmt = null;
 		StringBuffer sbQuery = new StringBuffer();
 		int resultado = 0;
 		JSONObject jsonObject = null;
 		try {
 			String fechaActual = UtilsFechas.getFechaActual();
 			
 			sbQuery.append(DescargaSATQuerys.getGuardarMetadataTimbrado(esquema));
 			sbQuery.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "); 
 			stmt = con.prepareStatement(sbQuery.toString());
 			String tipoMoneda = null;
 			int numError = 0;
 			for (int x = 0; x < listaDetalle.size(); x++) {
 				try {
	 				// int numParam=1;
 					try {
 	 					tipoMoneda = Utils.noNulo(jsonObject.getString("TipoMoneda"));
 	 					if (tipoMoneda.length() > 3) {
 	 						tipoMoneda = null;
 	 					}
 					}catch(Exception e) {
 						tipoMoneda = null;
 					}
	 				jsonObject = listaDetalle.get(x);
	 				// logger.info("X===>"+x+ ", UUID===>"+Utils.noNulo(jsonObject.getString("UUID")));
	 				stmt.setString(1, Utils.noNulo(jsonObject.getString("UUID")));
	 				stmt.setString(2, Utils.noNulo(jsonObject.getString("RfcEmisor")));
	 				stmt.setString(3, Utils.noNuloNormal(jsonObject.getString("nombreEmisor")));
	 				stmt.setString(4, Utils.noNulo(jsonObject.getString("RfcReceptor")));
	 				stmt.setString(5, Utils.noNuloNormal(jsonObject.getString("NombreReceptor")));
	 				stmt.setString(6, "PENDIENTE");
	 				stmt.setString(7, Utils.noNulo(jsonObject.getString("FechaTimbrado")).replace("T", " ").replace("+", ":").substring(0, 19));
	 				stmt.setDouble(8, Utils.noNuloDouble(jsonObject.getString("Total")));
	 				stmt.setString(9, Utils.noNulo(jsonObject.getString("TipoDeComprobante")));
	 				stmt.setString(10, tipoMoneda);
	 				stmt.setString(11, Utils.noNulo(jsonObject.getString("Status")));
	 				stmt.setString(12, "N");
	 				stmt.setString(13, usuarioHTTP);
	 				
	 				if ("CANCELADO".equalsIgnoreCase(Utils.noNulo(jsonObject.getString("Status"))) ) {
	 					stmt.setString(14, usuarioHTTP);
	 					stmt.setString(15, fechaActual);
	 				}else {
	 					stmt.setString(14, null);
	 					stmt.setString(15, null);
	 				}
	 				
	 				resultado+= stmt.executeUpdate();
 				}catch(SQLException sql) {
 					 if (sql.getErrorCode() == 1062) {
 						numError++;
 						// logger.info("ErroCode ====>"+sql.getErrorCode()+", uuid==>"+Utils.noNulo(jsonObject.getString("UUID")));	
 					}
 				}catch(Exception exa) {
 					// numError++;
 					// logger.info("stmtFallo ====>"+stmt);
 					logger.info("jsonObject fallo ====>"+jsonObject);
 					Utils.imprimeLog("", exa);
 				}
 			}
 			logger.info("resultado ====>"+resultado);
 			logger.info("numError ====>"+numError);
 			/*
 			sbQuery.append(DescargaSATQuerys.getGuardarMetadataTimbrado(esquema));
 			
 			for (int x = 0; x < listaDetalle.size(); x++) {
 				sbQuery.append(" (?,?,?,?,?,?,?,?,?,?,?,?),"); 
 			}
 			String queryFinal = sbQuery.toString().trim().substring(0, sbQuery.length() - 1);
 			stmt = con.prepareStatement(queryFinal);
 			
 			int numParam=1;
 			for (int x = 0; x < listaDetalle.size(); x++) {
 				jsonObject = listaDetalle.get(x);
 				stmt.setString(numParam++, Utils.noNulo(jsonObject.getString("UUID")));
 				stmt.setString(numParam++, Utils.noNulo(jsonObject.getString("RfcEmisor")));
 				stmt.setString(numParam++, Utils.noNuloNormal(jsonObject.getString("nombreEmisor")));
 				stmt.setString(numParam++, Utils.noNulo(jsonObject.getString("RfcReceptor")));
 				stmt.setString(numParam++, Utils.noNuloNormal(jsonObject.getString("NombreReceptor")));
 				stmt.setString(numParam++, "PENDIENTE");
 				stmt.setString(numParam++, Utils.noNulo(jsonObject.getString("FechaTimbrado")).replace("T", " ").replace("+", ":").substring(0, 19));
 				stmt.setDouble(numParam++, Utils.noNuloDouble(jsonObject.getString("Total")));
 				stmt.setString(numParam++, Utils.noNulo(jsonObject.getString("TipoDeComprobante")));
 				stmt.setString(numParam++, Utils.noNulo(jsonObject.getString("Status")));
 				stmt.setString(numParam++, "N");
 				stmt.setString(numParam++, usuarioHTTP);
 			}
 			resultado = stmt.executeUpdate();
 			
 			*/
 		}catch (Exception e) {
 			/*
 			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
				sbQuery.setLength(0);
				
			}catch(Exception ex) {
				stmt = null;
			}
 			*/
 			
			Utils.imprimeLog("", e);
			resultado = -1;
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
 		return resultado;
 	}
	 
	 public String getFechaHoy(Connection con, String esquema) {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String fechaHoy = null;
			try {
				fechaHoy = Utils.getFechayyyyMMdd();
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
					stmt = null;
				}
			}
			return fechaHoy ;
		}
	 
	 
	 private boolean isEjecuto(Connection con, String esquema, String fecha){
	        PreparedStatement stmt = null;
	        ResultSet rs = null;

	        try{
	            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getBuscarRespaldo(esquema));
	            stmt.setString(1, "DMT");
	            stmt.setString(2, fecha);
	            rs = stmt.executeQuery();
				if(rs.next()){
					return true;
	            }
				return false;
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("isEjecuto(): ", e);
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
	        return false;
	    }
 
 	

	 	public String getFechaAyer(Connection con, String esquema) {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String fechaAyer = null;
			try {
				stmt = con.prepareStatement(ProcesoDescargaSATQuerys.getObtenerFechaAyer(esquema));
				rs = stmt.executeQuery();
				if (rs.next()) {
					fechaAyer = Utils.noNulo(rs.getString(1));
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
					stmt = null;
				}
			}
			return fechaAyer ;
		}
	 	
	 	
	 	private void grabarProceso(Connection con, String esquema, int totEnvios, String tipoEnvio){
	        PreparedStatement stmt = null;
	        try{
	            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getGrabarProceso(esquema));
	            stmt.setString(1, "DMT");
	            stmt.setString(2, tipoEnvio);
	            stmt.setInt(3, totEnvios);
	            stmt.setString(4, "OK");
	            stmt.executeUpdate();
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("grabarProceso(): ", e);
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
		}
	 	
	 	
	 	private void registrarInicioSolicitud(Connection con, String esquema, String tipoDescarga, String tipoComprobante, String accionSAT, String fechaDescarga) {
	        PreparedStatement stmt = null;
	        try{
	       	
	            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getRegistrarInicioSolicitud(esquema));
	            stmt.setString(1, tipoDescarga);
	            stmt.setString(2, tipoComprobante);
	            stmt.setString(3, accionSAT);
	            stmt.setString(4, fechaDescarga);
	           // stmt.setString(5, "INI");
	            stmt.setString(5, "FIN");
	            
	            stmt.executeUpdate();
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("registrarIniciioSolicitud(): ", e);
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
		}
		
		
	 	private ArrayList<ProcesoDescargaSATForm> consultarSolicitud(Connection con, String esquema, String fechaBitacora, String tipoDescarga) {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        ArrayList<ProcesoDescargaSATForm> listaDetalle = new ArrayList<>();
	        ProcesoDescargaSATForm proDescargaForm = new ProcesoDescargaSATForm();
	        try{
	            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getConsultarSolicitudes(esquema));
	            stmt.setString(1, tipoDescarga);
	            stmt.setString(2, fechaBitacora);
	            rs = stmt.executeQuery();
	            while(rs.next()) {
	            	proDescargaForm.setIdRegistro(rs.getInt(1));
	            	proDescargaForm.setTipoComprobante(Utils.noNuloNormal(rs.getString(2)));
	            	proDescargaForm.setAccionSat(Utils.noNulo(rs.getString(3)));
	            	proDescargaForm.setSolicitudSat(Utils.noNuloNormal(rs.getString(4)));
	            	proDescargaForm.setPaqueteSat(Utils.noNuloNormal(rs.getString(5)));
	            	proDescargaForm.setFechaInicio(Utils.noNuloNormal(rs.getString(6)));
	            	proDescargaForm.setFechaFin(Utils.noNuloNormal(rs.getString(7)));
	            	proDescargaForm.setFechaDescarga(Utils.noNuloNormal(rs.getString(8)));
	            	proDescargaForm.setEstatusDescarga(Utils.noNuloNormal(rs.getString(9)));
	            	proDescargaForm.setMensajeSat(Utils.noNuloNormal(rs.getString(10)));
	            	listaDetalle.add(proDescargaForm);
	            	proDescargaForm = new ProcesoDescargaSATForm();
	            }
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("registrarIniciioSolicitud(): ", e);
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
	        return listaDetalle;
		}
	 	
	 	
	 	private String getUltimaCorrida(Connection con, String esquema){
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        String fechaUltima = "";
	        try{
	        	
	        	//int DIAS_PROCESAR = Utils.noNuloINT( Utils.noNulo(ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "NUMERO_DIAS_DESCARGA_SAT")));
	        	int DIAS_PROCESAR = Utils.noNuloINT( Utils.noNulo(ConfigAdicionalesBean.calcularConfiguraciones(con, esquema).get("NUMERO_DIAS_DESCARGA_SAT")));
	        	if (DIAS_PROCESAR == 0) {
	        		DIAS_PROCESAR = 10;
	        	}
	        	
	            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getObtenerUltimaCorrida(esquema));
	            stmt.setInt(1, DIAS_PROCESAR);
	            
	            logger.info("stmt===>"+stmt);	            
	            rs = stmt.executeQuery();

	            if (rs.next()){
	            	fechaUltima  = Utils.noNulo(rs.getString(1));
	            }
	            
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("isEjecuto(): ", e);
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
	        return fechaUltima;
	    }
		
	 	

		private void actualizarSolicitud(Connection con, String esquema, int idRegistro, String accionSAT, String idSolicitud, String idPaquete, String estatusDescarga, String mensajeSat) {
	        PreparedStatement stmt = null;
	        try{
	            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getActualizarSolicitudes(esquema));
	            stmt.setString(1, accionSAT);
	            stmt.setString(2, idSolicitud);
	            stmt.setString(3, idPaquete);
	            stmt.setString(4, estatusDescarga);
	            stmt.setString(5, mensajeSat);
	            stmt.setInt(6, idRegistro);
	            // logger.info("stmtActualiza==>"+stmt);
	            stmt.executeUpdate();
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("actualizarSolicitud(): ", e);
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
		}
		
		

		 
		 private void cargarBoveda(Connection conEmpresa, String esquemaBD, String esquemaEmpresa, File carpeta,  Integer arrResultado [], boolean isRecibido) {
				String temp = "";
				BovedaBean bovedaBean = new BovedaBean();
				BovedaEmitidosBean bovedaEmitidosBean = new BovedaEmitidosBean();
				BovedaNominaBean bovedaNominaBean = new BovedaNominaBean();
				Comprobante _comprobante = null;
		        try {
		        	ArrayList<File> listaNomina = new ArrayList<>();
		        	if(carpeta.exists()) {
		        		final String usuarioHTTP = "procesos@siarex.com";
		        		for (File fileEntry : carpeta.listFiles()) {
			      	       if (fileEntry.isFile()) {
			      	          temp = fileEntry.getName();
			      	          if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equalsIgnoreCase("xml")) {
			      	        	  // ArrayList<File> listaFile = new ArrayList<>();
			      	        	  // listaFile.add(fileEntry);
			      	        	  if (isRecibido) {
			      	        		 bovedaBean.procesarXmlBoveda(conEmpresa, esquemaBD, esquemaEmpresa, fileEntry, arrResultado, false); 
			      	        	  }else {
			      	        		  
			      	        		try {
			      				    	_comprobante = LeerXML.ObtenerComprobante(fileEntry.getAbsolutePath());
			      				    	if (_comprobante == null) {
			      				    		logger.info("XML Fallo proceso ====>"+fileEntry.getName());
			      				    	}else {
			      				    		if ("N".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
			      				    			// logger.info("Agregando Nomina ====>"+fileEntry.getName());
			      				    			listaNomina.add(fileEntry);
			      				    		}else {
			      				    			bovedaEmitidosBean.procesarXmlBoveda(conEmpresa, esquemaBD, esquemaEmpresa, fileEntry, arrResultado, usuarioHTTP, false);	
			      				    		}
			      				    					      				    		
			      				    	}
			      				    }catch(Exception e) {
			      				    	Utils.imprimeLog("", e);
			      				    }
			      	        	  }
			      	        	  
			      	        	  /*
			      	        	  resBoveda = iniciaCargaXML(conEmpresa, esquemaBD, esquemaEmpresa, fileEntry);
			      	        	  if (resBoveda == 100) { // fue exitoso
			      	        		regExitoso++;
			      	        	  }else if (resBoveda == 101) { // duplicado en boveda
			      	        		regDuplicado++;
			      	        	  }else if (resBoveda == 102) { // Error no coincide el RFC
			      	        		regErrorRFC++;
			      	        	  }
			      	        	  */
			      	          }
			      	        }
			      	    }
		        		
		        		if (listaNomina.size() > 0) {
		        			// logger.info("Tamaño====>"+listaNomina.size());
			      	       bovedaNominaBean.procesarXmlBoveda(conEmpresa, esquemaBD, esquemaEmpresa, listaNomina, arrResultado, usuarioHTTP, false);
			      	    }
		        	}
		        	
		        } catch (Exception ex) {
		            Utils.imprimeLog("", ex);
		        }
			}	 
		 
		 
		 
		 private void actualizarTotales(Connection con, String esquema, int idRegistro, int totalArchivos, int totExitosos, int noDuplicados, int errorRFC, int archivosNomina) {
		        PreparedStatement stmt = null;
		        try{
		            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getActualizarTotales(esquema));
		            stmt.setInt(1, totalArchivos);
		            stmt.setInt(2, totExitosos);
		            stmt.setInt(3, noDuplicados);
		            stmt.setInt(4, errorRFC);
		            stmt.setInt(5, archivosNomina);
		            stmt.setInt(6, idRegistro);
		            // logger.info("stmtActualiza==>"+stmt);
		            stmt.executeUpdate();
		        }
		        catch(Exception e){
		        	Utils.imprimeLog("actualizarSolicitud(): ", e);
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
		}
			
		 
		 public void actualizarEncontradosBovedaRecibidos(Connection con, String esquema) {
		        PreparedStatement stmt = null;
		        try{
		            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getEncontradosBovedaTimbradoRecibidos(esquema));
		            stmt.setString(1, "S");
		            stmt.setString(2, "N");
		            stmt.executeUpdate();
		        }
		        catch(Exception e){
		        	Utils.imprimeLog("actualizarSolicitud(): ", e);
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
			}
		 
		 public void actualizarEncontradosBovedaEmitidos(Connection con, String esquema) {
		        PreparedStatement stmt = null;
		        try{
		            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getEncontradosBovedaTimbradoEmitidos(esquema));
		            stmt.setString(1, "S");
		            stmt.setString(2, "N");
		            stmt.executeUpdate();
		        }
		        catch(Exception e){
		        	Utils.imprimeLog("actualizarSolicitud(): ", e);
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
			}
		 
		 
		 
		 
		 public void actualizarEncontradosBovedaNomina(Connection con, String esquema) {
		        PreparedStatement stmt = null;
		        try{
		            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getEncontradosBovedaNomina(esquema));
		            stmt.setString(1, "S");
		            stmt.setString(2, "N");
		            stmt.executeUpdate();
		        }
		        catch(Exception e){
		        	Utils.imprimeLog("actualizarSolicitud(): ", e);
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
			}
		 
		 public void actualizarCancelado(Connection con, String esquema, String uuid, String estatusXML) {
		        PreparedStatement stmt = null;
		        try{
		            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getActualizarEstatusCancelado(esquema));
		            stmt.setString(1, estatusXML);
		            stmt.setString(2, uuid);
		            stmt.executeUpdate();
		        }
		        catch(Exception e){
		        	Utils.imprimeLog("actualizarSolicitud(): ", e);
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
			}
		 

		 private void revalidarBoveda(Connection conEmpresa, String esquemaBD, EmpresasForm empresaForm, String rfcSolicitante,  String fechaDescarga, String tipoComprobante) {
				DescargaSATBean descargaSatBean = new DescargaSATBean();
				DescargaMasiva descargaMasivaBean = new DescargaMasiva();
				BovedaEmitidosBean bovedaEmitidosBean = new BovedaEmitidosBean();
				BovedaBean bovedaBean = new BovedaBean();
				String apiKey = UtilsPATH.API_KEY_TIMBRADO_DESCARGA;
				try {
					
					String rutaDescarga = UtilsPATH.RUTA_PUBLIC_HTML + empresaForm.getEsquema() + File.separator + "DESCARGA_SAT" + File.separator + "CFDI_TIMBRADO" + File.separator + fechaDescarga + File.separator;  // +"/descargaMasiva_" + fechaHoy + ".zip";
					String rutaFinalDescrga = rutaDescarga + tipoComprobante + File.separator + "REVALIDA" + File.separator;
					
					File fileDirectorio = new File(rutaFinalDescrga);
					if (!fileDirectorio.exists()) {
						//logger.info("************ creando directorio===>"+rutaFinalDescrga);
						fileDirectorio.mkdir();
					}
					
					ArrayList<DescargaSATForm> listaFaltantes = obtenerNoExisteBoveda(conEmpresa, esquemaBD, empresaForm.getRfc());
					DescargaSATForm descargaSATForm = null;
					String jsonRespuesta = null;
					String baseData64 = null;
					String status = null;
					Integer arrResultado [] = {0,0,0,0,0};
					String rutaXML = null;
					ArrayList<File> listaNomina = new ArrayList<>();
					for (int x = 0; x < listaFaltantes.size(); x++) {
						
						try {
							descargaSATForm = listaFaltantes.get(x);
							// logger.info("Recuperando CFDI===>"+descargaSATForm.getUuid());
							jsonRespuesta = descargaMasivaBean.recuperaCFDI(rfcSolicitante, apiKey, descargaSATForm.getUuid());
							if (jsonRespuesta != null) {
								JSONObject jsonArray   = new JSONObject(jsonRespuesta);
								if (jsonArray.get("data").toString() != null) {
									baseData64 = jsonArray.get("data").toString();
									
									try {
										if (jsonArray.get("status") == null) {
											status = "Vigente";
										}else {
											status = jsonArray.get("status").toString();
										}
									}catch(Exception e) {
										status = "Vigente";
									}
									
									
									if ("Vigente".equalsIgnoreCase(status)) {
										if (baseData64 == null || "null".equalsIgnoreCase(baseData64)) {
											// logger.info("No encontrado UUID===>"+descargaSATForm.getUuid()+", Archivo null, row==>"+x);
										}else {
											rutaXML = rutaFinalDescrga + descargaSATForm.getUuid() +".xml";
											// logger.info("rutaXML===>"+rutaXML);
											File fileXML = new File(rutaXML);
											descargaMasivaBean.generarXML(rutaXML, baseData64);
											
											//if ("N".equalsIgnoreCase(descargaSATForm.getEfectoComprobante()) && empresaForm.getRfc().equalsIgnoreCase(descargaSATForm.getRfcEmisor())) {
											if ("N".equalsIgnoreCase(descargaSATForm.getEfectoComprobante())) {
												listaNomina.add(new File(rutaXML));
											}else if (empresaForm.getRfc().equalsIgnoreCase(descargaSATForm.getRfcReceptor())){ // es recibido
												bovedaBean.procesarXmlBoveda(conEmpresa, esquemaBD, empresaForm.getEsquema(), fileXML, arrResultado, false);	
											}else {
												bovedaEmitidosBean.procesarXmlBoveda(conEmpresa, esquemaBD, empresaForm.getEsquema(), fileXML, arrResultado, usuarioHTTP, false);	
											}
										}
									}else if ("Cancelado".equalsIgnoreCase(status)){
										actualizarCancelado(conEmpresa, esquemaBD, descargaSATForm.getUuid(), "CANCELADO");
									}
									
								}else {
									logger.info("Retorno==>"+jsonArray);
								}
							}
						}catch(Exception e) {
							logger.info("UUID===>"+descargaSATForm.getUuid());
							Utils.imprimeLog("", e);
						}
					}
					
					// logger.info("listaNomina ====>"+listaNomina.size());
					if (listaNomina.size() > 0) {
						BovedaNominaBean bovedaNomina = new BovedaNominaBean();
						bovedaNomina.procesarXmlBoveda(conEmpresa, esquemaBD, empresaForm.getEsquema(), listaNomina, arrResultado, usuarioHTTP, false);
					}
					
					try {
						 FileUtils.deleteDirectory(fileDirectorio);
						 fileDirectorio.delete();
					}catch(Exception er) {
						Utils.imprimeLog("", er);
					}
					
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				} finally {
					
				}
			}
		 
		 
		 public ArrayList<DescargaSATForm>  obtenerNoExisteBoveda (Connection con, String esquema, String rfcEmisor){
				PreparedStatement stmt = null;
				ResultSet rs = null;
				ArrayList<DescargaSATForm> listaDescarga = new ArrayList<>();
				DescargaSATForm descargaSATForm = new DescargaSATForm();
				
				try {
					StringBuffer sbQuery = new StringBuffer();
					sbQuery.append(DescargaSATQuerys.getRevalidarBoveda(esquema));
					
					stmt = con.prepareStatement(sbQuery.toString());
					// stmt.setString(1, rfcEmisor);
					stmt.setString(1, "VIGENTE");
					// stmt.setString(3, "N");
					stmt.setString(2, "N");
					
					rs = stmt.executeQuery();
		        	while (rs.next()) {
		        		descargaSATForm.setUuid(Utils.noNuloNormal(rs.getString(2)));
		        		descargaSATForm.setRfcEmisor(Utils.noNuloNormal(rs.getString(3)));
		        		descargaSATForm.setRfcReceptor(Utils.noNuloNormal(rs.getString(5)));
		        		descargaSATForm.setEfectoComprobante(Utils.noNuloNormal(rs.getString(11)));
		        		listaDescarga.add(descargaSATForm);
		        		descargaSATForm = new DescargaSATForm();
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
				return listaDescarga;
			}

		 
			private void enviarNotificacion(Connection con, String esquema, EmpresasForm empresaForm) {
				try {
					
					String CORREO_AVISO_UUID_BOVEDA_1 = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "CORREO_AVISO_UUID_BOVEDA_1");
					String CORREO_AVISO_UUID_BOVEDA_2 = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "CORREO_AVISO_UUID_BOVEDA_2");

					
					String mensajeCorreo = UtilsHTML.generaHTMLDescargaMasiva();
					
					// logger.info("CORREO_AVISO_UUID_BOVEDA_1===>"+CORREO_AVISO_UUID_BOVEDA_1);
					// logger.info("CORREO_AVISO_UUID_BOVEDA_2===>"+CORREO_AVISO_UUID_BOVEDA_2);
					
					if (!"".equalsIgnoreCase(CORREO_AVISO_UUID_BOVEDA_1) || !"".equalsIgnoreCase(CORREO_AVISO_UUID_BOVEDA_2)) {
						String pathCSV = generarCSV(con, esquema, empresaForm);
						
						if ("".equalsIgnoreCase(pathCSV)) {
							logger.info("No se encontro informacion para enviar por correo....");	
						}else {
							File fileCSV = new File(pathCSV);
							if (fileCSV.exists()) {
								String emailTO [] = {CORREO_AVISO_UUID_BOVEDA_1, CORREO_AVISO_UUID_BOVEDA_2};
								EnviaCorreoGrid.enviarCorreo(pathCSV, mensajeCorreo, true, emailTO, null, "SIAREX - XML no encontrados en Boveda", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
								
							}else {
								String emailTO [] = {CORREO_AVISO_UUID_BOVEDA_1, CORREO_AVISO_UUID_BOVEDA_2};
								EnviaCorreoGrid.enviarCorreo(null, mensajeCorreo, false, emailTO, null, "SIAREX - XML no encontrados en Boveda", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
								
							}
							
						}
						
					}
					
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				} finally {
					
				}
			}
			
			
			public String generarCSV(Connection con, String esquema, EmpresasForm empresaForm) {
				// DescargaSATBean descargaSatBean = new DescargaSATBean();
				String pathCSV = "";
				try {
			    	 
			    	ArrayList<String> lineTXT = exportarCSV(con, esquema, "N");
			    	if (lineTXT.size() <= 1) {
			    		pathCSV = "";
			    	}else {
				    	Date fechaActual = new Date();
						String fecA = null;
						SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
						fecA = formato.format(fechaActual);
						
						String nombreReporte = "detalleMetadataBoveda_"+"_"+ empresaForm.getNombreCorto() + "_" +fecA+ ".csv";
						 pathCSV = UtilsPATH.RUTA_PUBLIC_HTML + empresaForm.getEsquema() + File.separator + "EXPORTAR_METADATA" + File.separator + nombreReporte;
						UtilsFile.crearArchivoSalto(lineTXT, pathCSV);
			    		
			    	}
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				}
				return pathCSV;
			}
			
			
			
			public ArrayList<String>  exportarCSV (Connection con, String esquema, String existeBovedaDescarga){
				PreparedStatement stmt = null;
				ResultSet rs = null;
				ArrayList<String> listaTXT = new ArrayList<String>();
				DecimalFormat decimal = new DecimalFormat("###,###.##");

				
				StringBuffer sbLine1 = new StringBuffer("UUID|RFC EMISOR|RAZON SOCIAL EMISOR|RFC RECEPTOR|RAZON SOCIAL RECEPTOR|PAC EMISOR|FECHA EMISION|FECHA DE CERTIFICACION|MONTO|EFECTO COMPROBANTE|ESTATUS|FECHA DE CANCELACION|EXISTE EN BOVEDA"); 
				try {
					StringBuffer sbQuery = new StringBuffer();
					sbQuery.append(DescargaSATQuerys.getDetalleExportarCSV(esquema));
					
					if (!"ALL".equalsIgnoreCase(existeBovedaDescarga)) {
						sbQuery.append(" where EXISTE_BOVEDA = ? and ESTATUS = ?");
					}
					stmt = con.prepareStatement(sbQuery.toString());
					
					int param=1;
					if (!"ALL".equalsIgnoreCase(existeBovedaDescarga)) {
						stmt.setString(param++, existeBovedaDescarga);
						stmt.setString(param++, "VIGENTE");
					}
					rs = stmt.executeQuery();
					listaTXT.add(sbLine1.toString());
		        	sbLine1.setLength(0);
		        	//sbLine1.append("|");
					
		        	while (rs.next()) {
						sbLine1.append(Utils.noNuloNormal(rs.getString(2))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(3))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(4))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(5))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(6))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(7))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(8))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(9))).append("|");
						sbLine1.append(decimal.format(rs.getDouble(10))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(11))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(12))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(13))).append("|");
						sbLine1.append(Utils.noNuloNormal(rs.getString(14)));
						
						listaTXT.add(sbLine1.toString());
						sbLine1.setLength(0);
			        	//sbLine1.append("|");
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
				return listaTXT;
			}
			
			
}

