package com.siarex247.cumplimientoFiscal.DescargaSAT;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;

import ws_api.descarga.DescargaMasiva;

public class DescargaCFDITimbradoExpress {

	public static final Logger logger = Logger.getLogger("siarex247");
	
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
		
		if (horaDia >= 6) {
			if (isEjecuto) {
				// String rutaCertificado = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquemaEmpresa +"/CERTIFICADOS/"+empresasForm.getArchivoCer()+".cer";
				// String rutaKey = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquemaEmpresa +"/CERTIFICADOS/"+empresasForm.getArchivoKey()+".key";
				 String apiKey = UtilsPATH.API_KEY_TIMBRADO_DESCARGA;
				
				 //String password = empresasForm.getPwdSat();
				// String rfcSolicitante = empresasForm.getRfc();
				String rfcReceptor = empresasForm.getRfc();
				// logger.info("rutaCertificado===>"+rutaCertificado);
				// logger.info("rutaKey===>"+rutaKey);
				// logger.info("password===>"+password);
				// logger.info("rfcSolicitante===>"+rfcSolicitante);
				// logger.info("rfcReceptor===>"+rfcReceptor);
				
				String rutaDescarga = UtilsPATH.RUTA_PUBLIC_HTML + esquemaEmpresa + File.separator + "DESCARGA_SAT" + File.separator + "CFDI_TIMBRADO" + File.separator + fechaDescarga + File.separator;  // +"/descargaMasiva_" + fechaHoy + ".zip";
				// logger.info("rutaDescarga===>"+rutaDescarga);
				
				ArrayList<ProcesoDescargaSATForm> listaSolicitudes = consultarSolicitud(conEmpresa, rcEmpresa.getEsquema(), fechaDescarga, "CFDI_TIMBR");
				// String tokenNativo = null;
				// String paquete = null;
				String jsonRespuesta = null;
				String fechaInicio = null;
				String fechaFinal = null;
				for (int x = 0; x < listaSolicitudes.size(); x++) {
					ProcesoDescargaSATForm procDescargaSatForm = listaSolicitudes.get(x);
					if ("DESCARGA".equalsIgnoreCase(procDescargaSatForm.getAccionSat()) && "INI".equalsIgnoreCase(procDescargaSatForm.getEstatusDescarga())) {
						
						fechaInicio = getUltimaCorrida(conEmpresa, rcEmpresa.getEsquema());
						// logger.info("fechaInicio====>"+fechaInicio);
						fechaFinal = procDescargaSatForm.getFechaDescarga();
						// logger.info("fechaFinal====>"+fechaFinal);
						
						String rutaFinalDescrga = rutaDescarga + procDescargaSatForm.getTipoComprobante() + File.separator +  "descargaMasivaTimbrado.zip";
						String rutaFinalDirectorio = rutaDescarga + procDescargaSatForm.getTipoComprobante() + File.separator;
						
						jsonRespuesta = descargaMasivaBean.descargaCFDI(rfcReceptor, apiKey,fechaInicio , fechaFinal);
						// logger.info("jsonRespuesta===>"+jsonRespuesta);
						 if (Utils.noNulo(jsonRespuesta).equals("")) {
							actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "DESCARGA", procDescargaSatForm.getSolicitudSat(), procDescargaSatForm.getPaqueteSat(), "ERR", "ERROR AL DESCARGAR FACTURAS ");							
						 }else {
							JSONObject jsonArray   = new JSONObject(jsonRespuesta);
							String baseData64 = jsonArray.get("zip").toString();
							// String baseData64 = jsonZip;
							
							File fileDescarga = new File(rutaFinalDirectorio);
							fileDescarga.mkdirs();
							
							 descargaMasivaBean.generarXML(rutaFinalDescrga, baseData64);
						 }
						// logger.info("rutaFinalDescrga ===>"+rutaFinalDescrga );
						File fileDescarga = new File(rutaFinalDirectorio);
						fileDescarga.mkdirs();
						actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "BOVEDA", "", procDescargaSatForm.getPaqueteSat(), "BOV", "SOLICITUD DE DESCARGA CON EXITO");						
						
					}else if ("boveda".equalsIgnoreCase(procDescargaSatForm.getAccionSat()) && "BOV".equalsIgnoreCase(procDescargaSatForm.getEstatusDescarga())) {
						
						String archivoZip = rutaDescarga + procDescargaSatForm.getTipoComprobante() + File.separator +  "descargaMasivaTimbrado.zip";
						String rutaCarpeta = rutaDescarga + procDescargaSatForm.getTipoComprobante() + File.separator;
						// logger.info("archivoZip===>"+archivoZip);
						// logger.info("rutaCarpeta===>"+rutaCarpeta);
						ZipFiles.unzip(archivoZip, rutaCarpeta);
						boolean descomprime = true;
						if (descomprime) {
							String recibidos = rutaCarpeta + "recibidos" + File.separator;
							String emitidos = rutaCarpeta + "emitidos" + File.separator;
							File fileRecibidos = new File(recibidos);
							File fileEmitidos = new File(emitidos);
							Integer arrResultado [] = {0,0,0,0,0};
							cargarBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, fileRecibidos, arrResultado);
							cargarBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, fileEmitidos, arrResultado);
						    int regExitoso = arrResultado[0];
							int regDuplicado = arrResultado[1];
							int regErrorRFC = arrResultado[2];
							int numFilesXML = arrResultado[3];
				        	int totalXML = regExitoso + regDuplicado + regErrorRFC + numFilesXML;
				        	actualizarTotales(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), totalXML, regExitoso, regDuplicado, regErrorRFC);
						}
						 actualizarSolicitud(conEmpresa, rcEmpresa.getEsquema(), procDescargaSatForm.getIdRegistro(), "BOVEDA", procDescargaSatForm.getSolicitudSat(), procDescargaSatForm.getPaqueteSat(), "FIN", "PROCESO DE DESCARGA FINALIZADO SATISFACTORIAMENTE");
						 actualizarEncontradosBoveda(conEmpresa, rcEmpresa.getEsquema());
					}
				}
			}else {
				if (horaDia >= 1) {
					grabarProceso(conEmpresa, rcEmpresa.getEsquema(), 1, diaMes);
					registrarInicioSolicitud(conEmpresa, rcEmpresa.getEsquema(), "CFDI_TIMBR", "Todos", "DESCARGA", fechaDescarga);
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
	            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getObtenerUltimaCorrida(esquema));
	            stmt.setString(1, "DMT");
	            rs = stmt.executeQuery();

	            int numRow = 1;
	            while(rs.next()){
				 	if (numRow == 3) {
				 		fechaUltima = Utils.noNulo(rs.getString(1));
				 		break;
				 	}
				 	numRow++;
	            }
				
	            if ("".equalsIgnoreCase(fechaUltima)) {
	            	fechaUltima = "2017-01-01";
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
		
		

		 
		 private void cargarBoveda(Connection conEmpresa, String esquemaBD, String esquemaEmpresa, File carpeta,  Integer arrResultado []) {
				String temp = "";
				BovedaBean bovedaBean = new BovedaBean();
		        try {
		        	if(carpeta.exists()) {
		        		
			        	for (final File fileEntry : carpeta.listFiles()) {
			      	       if (fileEntry.isFile()) {
			      	          temp = fileEntry.getName();
			      	          if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equalsIgnoreCase("xml")) {
			      	        	  ArrayList<File> listaFile = new ArrayList<>();
			      	        	  listaFile.add(fileEntry);
			      	        	  bovedaBean.procesarXmlBoveda(conEmpresa, esquemaBD, esquemaEmpresa, fileEntry, arrResultado, false);
			      	        	  
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
		        	}
		        	
		        } catch (Exception ex) {
		            Utils.imprimeLog("", ex);
		        }
			}	 
		 
		 
		 
		 private void actualizarTotales(Connection con, String esquema, int idRegistro, int totalArchivos, int totExitosos, int noDuplicados, int errorRFC) {
		        PreparedStatement stmt = null;
		        try{
		            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getActualizarTotales(esquema));
		            stmt.setInt(1, totalArchivos);
		            stmt.setInt(2, totExitosos);
		            stmt.setInt(3, noDuplicados);
		            stmt.setInt(4, errorRFC);
		            stmt.setInt(5, idRegistro);
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
			
		 
		 public void actualizarEncontradosBoveda(Connection con, String esquema) {
		        PreparedStatement stmt = null;
		        try{
		            stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getEncontradosBoveda(esquema));
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
		 
}
