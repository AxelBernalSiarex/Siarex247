package com.siarex247.cumplimientoFiscal.DescargaSAT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsPATH;

import ws_api.descarga.DescargaMasiva;

public class ActualizarEstatusCancelarSAT {

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
						if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
							try {
								if ("toyota".equalsIgnoreCase(empresasForm.getEsquema()) || "tmmbcservicios".equalsIgnoreCase(empresasForm.getEsquema())) {
									
								}else {
									 // if ("plamex".equalsIgnoreCase(empresasForm.getEsquema())) {
										logger.info("Inciando descarga timbrado de la empresa : "+empresasForm.getEsquema());
										iniciaProcesoDescarga(empresasForm);	
									 // }
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
			String jsonRespuesta = null;
			String fechaInicio = null;
			String fechaFinal = null;
			fechaInicio = getUltimaCorrida(conEmpresa, rcEmpresa.getEsquema());
			String apiKey = UtilsPATH.API_KEY_TIMBRADO_DESCARGA;
			String rfcReceptor = empresasForm.getRfc();
			// String arrFechasIni [] = {"2018-01-01", "2019-01-01", "2020-01-01", "2021-01-01", "2022-01-01", "2023-01-01", "2024-01-01"};
			// String arrFechasFin [] = {"2018-12-31", "2019-12-31", "2020-12-31", "2021-12-31", "2022-12-31", "2023-12-31", "2024-12-31"};
			if (isEjecuto) {
				if (horaDia >= 6) {
					ArrayList<ProcesoDescargaSATForm> listaSolicitudes = consultarSolicitud(conEmpresa, rcEmpresa.getEsquema(), fechaDescarga, "CFDI_ACSAT");
					ProcesoDescargaSATForm procDescargaSatForm = null;
					
					for (int x = 0; x < listaSolicitudes.size(); x++) {
						procDescargaSatForm = listaSolicitudes.get(x);
						
						fechaFinal = procDescargaSatForm.getFechaDescarga();
						 
						// logger.info("apiKey==>"+apiKey);
						/*
						for (int a = 0; a < arrFechasIni.length; a++) {
							 fechaInicio = arrFechasIni[a];
							 fechaFinal = arrFechasFin[a];
						*/	
							logger.info("rfcReceptor==>"+rfcReceptor);
							logger.info("fechaInicio==>"+fechaInicio);
							logger.info("fechaFinal==>"+fechaFinal);
							 
							if ("DOREPORTE".equalsIgnoreCase(procDescargaSatForm.getAccionSat()) && "INI".equalsIgnoreCase(procDescargaSatForm.getEstatusDescarga())) {
								logger.info("Iniciando actualizacion de estatus en el sat.....");
								// se inicia con el doReporte
								jsonRespuesta = descargaMasivaBean.doReporteEstatus(rfcReceptor, apiKey, fechaInicio, fechaFinal, "cancelado");
								if (jsonRespuesta != null) {
									JSONObject jsonArray   = new JSONObject(jsonRespuesta);
									if (jsonArray.get("data").toString() != null) {
										JSONArray jsonData   = new JSONArray(jsonArray.get("data").toString());
										int contador = 1;
										ArrayList<JSONObject> listaDetalle = new ArrayList<>();
										int resultado = 1;
										for (int y = 0; y < jsonData.length(); y++) {
											if (resultado > 0) {
												JSONObject jsonRow   = new JSONObject(jsonData.get(y).toString());
												//if ("Cancelado".equalsIgnoreCase(Utils.noNulo(jsonRow.getString("Status")))) {
													listaDetalle.add(jsonRow);
													if (contador == 1000) {
														// resultado = guardarMetadataTimbrado(conEmpresa, rcEmpresa.getEsquema(),listaDetalle);
														// listaDetalle = new ArrayList<>();
														contador = 1;
													}else {
														contador++;
														resultado = 1;
													}
												// }
											}
										}
										if (listaDetalle.size() > 0) {
											resultado = actualizarMetadataTimbrado(conEmpresa, rcEmpresa.getEsquema(),rfcReceptor, listaDetalle);
										}
										
									}
								}
							}
						//}
						actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "DOREPORTE", "", procDescargaSatForm.getPaqueteSat(), "FIN", "SOLICITUD DE ACTUALIZACION EN SAT CON EXITO.");
						
					}
				}
			}else {
				logger.info("actualizando estatus en el SAT.........");
				grabarProceso(conEmpresa, rcEmpresa.getEsquema(), 1, diaMes);
				registrarInicioSolicitud(conEmpresa, rcEmpresa.getEsquema(), "CFDI_ACSAT", "Todos", "DOREPORTE", fechaDescarga);
				// SE GUARDA EN HISTORICO DE DESCARGAS LAS SOLICITUDES
				jsonRespuesta = descargaMasivaBean.actualizarEstatus(rfcReceptor, apiKey, fechaInicio, fechaDescarga);
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
            stmt.setString(1, "AES"); // Actualizar Estatus SAT
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

	private void grabarProceso(Connection con, String esquema, int totEnvios, String tipoEnvio){
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getGrabarProceso(esquema));
            stmt.setString(1, "AES");
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
            stmt.setString(5, "INI");
            
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
        	
        	int numDias = Utils.noNuloINT(ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "NUM_DIAS_CANCELACION"));
        	if (numDias == 0) {
        		numDias = 7;
        	}
        	String fechaHoy = UtilsFechas.getFechayyyyMMdd();
        	fechaUltima = UtilsFechas.restarDiasFecha(fechaHoy, numDias);
        } catch(Exception e){
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

	
	public int actualizarMetadataTimbrado(Connection con, String esquema, String rfcEmpresa, ArrayList<JSONObject> listaDetalle) {
 		PreparedStatement stmtTimbrado = null;
 		 
 		PreparedStatement stmtBoveda = null;
 		PreparedStatement stmtEmitidos = null;
 		PreparedStatement stmtNomina = null;
 		 
 		
 		int resultado = 0;
 		JSONObject jsonObject = null;
 		try {
 			
 			stmtTimbrado = con.prepareStatement(DescargaSATQuerys.getActualizarMetadataTimbrado(esquema));
 			stmtBoveda = con.prepareStatement(DescargaSATQuerys.getActualizarBovedaEstatus(esquema));
 			stmtEmitidos = con.prepareStatement(DescargaSATQuerys.getActualizarBovedaEmitidosEstatus(esquema));
 			stmtNomina = con.prepareStatement(DescargaSATQuerys.getActualizarBovedaNominaEstatus(esquema));
 			
 			String tipoComprobante = null;
 			String rfcEmisorXML = null;
 			String rfcReceptorXML = null;
 			 
 			stmtNomina.setString(1, "C");
 			stmtEmitidos.setString(1, "C");
 			stmtBoveda.setString(1, "C");
 			 
 			String tipoMoneda = null;
 			for (int x = 0; x < listaDetalle.size(); x++) {
 				try {
	 				// int numParam=1;
 					jsonObject = listaDetalle.get(x);
 					try {
 	 					tipoMoneda = Utils.noNulo(jsonObject.getString("TipoMoneda"));
 	 					if (tipoMoneda.length() > 3) {
 	 						tipoMoneda = null;
 	 					}
 					}catch(Exception e) {
 						tipoMoneda = null;
 					}
 					 
 					tipoComprobante = Utils.noNulo(jsonObject.getString("TipoDeComprobante"));
 					rfcEmisorXML = Utils.noNulo(jsonObject.getString("RfcEmisor"));
 					rfcReceptorXML = Utils.noNulo(jsonObject.getString("RfcReceptor"));
 					
 					stmtTimbrado.setString(1, Utils.noNulo(jsonObject.getString("Status")));
 					stmtTimbrado.setString(2, tipoMoneda);
 					stmtTimbrado.setString(3, usuarioHTTP);
 					stmtTimbrado.setString(4, Utils.noNulo(jsonObject.getString("UUID")));
	 				resultado = stmtTimbrado.executeUpdate();
	 				// logger.info("UUID ===> "+Utils.noNulo(jsonObject.getString("UUID"))+", resultado===>"+resultado);
	 				
	 				 
	 				if ("N".equalsIgnoreCase(tipoComprobante)) { // es de nomina
	 					stmtNomina.setString(2, Utils.noNulo(jsonObject.getString("UUID")));
		 				resultado = stmtNomina.executeUpdate();
	 				}else if (rfcEmpresa.equalsIgnoreCase(rfcEmisorXML)) { // es un emitido
	 					stmtEmitidos.setString(2, Utils.noNulo(jsonObject.getString("UUID")));
		 				resultado = stmtEmitidos.executeUpdate();
	 				}else if (rfcEmpresa.equalsIgnoreCase(rfcReceptorXML)) { // es un recibido
	 					stmtBoveda.setString(2, Utils.noNulo(jsonObject.getString("UUID")));
		 				resultado = stmtBoveda.executeUpdate();
	 				}else {
	 					stmtNomina.setString(2, Utils.noNulo(jsonObject.getString("UUID")));
		 				stmtNomina.executeUpdate();
		 				stmtEmitidos.setString(2, Utils.noNulo(jsonObject.getString("UUID")));
		 				stmtEmitidos.executeUpdate();
		 				stmtBoveda.setString(2, Utils.noNulo(jsonObject.getString("UUID")));
		 				stmtBoveda.executeUpdate();
	 				}
	 				 
 				}catch(Exception exa) {
 					logger.info("stmt ====>"+stmtTimbrado);
 					logger.info("jsonObject fallo ====>"+jsonObject);
 					Utils.imprimeLog("", exa);
 				}
 			}
 		}catch (Exception e) {
			Utils.imprimeLog("", e);
			resultado = -1;
		}finally {
			try {
				if (stmtTimbrado != null) {
					stmtTimbrado.close();
				}
				stmtTimbrado = null;
				 
				if (stmtBoveda != null) {
					stmtBoveda.close();
				}
				stmtBoveda = null;
				if (stmtEmitidos != null) {
					stmtEmitidos.close();
				}
				stmtEmitidos = null;
				if (stmtNomina != null) {
					stmtNomina.close();
				}
				stmtNomina = null;
				 
			}catch(Exception e) {
				stmtTimbrado = null;
				stmtBoveda = null;
				stmtEmitidos = null;
				stmtNomina = null;
				 
			}
		}
 		return resultado;
 	}
}
