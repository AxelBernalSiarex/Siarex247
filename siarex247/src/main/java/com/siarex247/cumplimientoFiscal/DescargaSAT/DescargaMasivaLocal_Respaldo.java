package com.siarex247.cumplimientoFiscal.DescargaSAT;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaBean;
import com.siarex247.cumplimientoFiscal.BovedaEmitidos.BovedaEmitidosBean;
import com.siarex247.cumplimientoFiscal.BovedaNomina.BovedaNominaBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;
import com.siarex247.validaciones.UtilsSAT;
import com.siarex247.validaciones.UtilsValidaciones;

public class DescargaMasivaLocal_Respaldo {

	public static final Logger logger = Logger.getLogger("siarex247");
	final String usuarioHTTP = "procesos@siarex.com";
	
	public void monitoreaCorreo(){
		try{
				AccesoBean accesoBean = new AccesoBean();
				try{
					logger.info("Descargando archivo .ZIP del server...............");
					String rutaZip = UtilsPATH.RUTA_PUBLIC_HTML + "DESCARGA_LOCAL" + File.separator + "TRABAJO" + File.separator;
					String rutaProcesados = UtilsPATH.RUTA_PUBLIC_HTML + "DESCARGA_LOCAL" + File.separator + "PROCESADOS" + File.separator;
					
					int numFile = 1;
					File listFileZIP = new File(rutaZip);
					String rutaArchivo = null;
					EmpresasForm empresaForm = null;
					
					for (File fileZIP : listFileZIP.listFiles()) {
						rutaArchivo = rutaZip + "ARCHIVO_"+numFile;
						ZipFiles.unzip(fileZIP.getAbsolutePath(), rutaArchivo);
						logger.info("Se ha descomprimido el archivo..............."+fileZIP.getAbsolutePath());
						numFile++;
						
						File fileRFC = new File(rutaArchivo);
						for (File folderFile : fileRFC.listFiles()) {
							empresaForm = accesoBean.consultaEmpresaRFC(folderFile.getName());
							if (folderFile.getName().equalsIgnoreCase(empresaForm.getRfc())) {
								iniciarProceso(empresaForm, folderFile.getAbsolutePath());	
							}else {
								FileUtils.deleteDirectory(folderFile); // se elimina el directorio
							}
							 
							
						}
						FileUtils.deleteDirectory(fileRFC); // se elimina el directorio
						String rutaDescarga = rutaProcesados + fileZIP.getName();
						File destFile = new File(rutaDescarga);
						
						UtilsFile.moveFileDirectory(fileZIP, destFile, false, false, true, true);
					}
					
				}catch(Exception e){
					Utils.imprimeLog("monitoreaCorreo ", e);
				}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
	
	private void iniciarProceso(EmpresasForm empresaForm, String rutaFolderRFC) {
		ConexionDB connPool = null;
	     ResultadoConexion rcEmpresa = null;
	     Connection conEmpresa = null;
	     BovedaEmitidosBean bovedaEmitidosBean = new BovedaEmitidosBean();
	     BovedaBean bovedaBean = new BovedaBean();
	     BovedaNominaBean bovedaNominaBean = new BovedaNominaBean();
	     
	     Integer arrResultadoEmitido [] = {0,0,0,0,0};
	     Integer arrResultadoNomina [] = {0,0,0,0,0};
	     Integer arrResultado [] = {0,0,0,0,0};
	     ArrayList<Comprobante> listaComprobantes = new ArrayList<Comprobante>();
		try {
			
			String esquemaEmpresa = empresaForm.getEsquema();
	        connPool = new ConexionDB();
	        rcEmpresa = connPool.getConnection(esquemaEmpresa);
	        conEmpresa = rcEmpresa.getCon();
	        
	        ArrayList<File> listaNomina = new ArrayList<>();
			List<String>  listaXML = UtilsFile.obtenerArchivosRecursivamente(rutaFolderRFC);
			for (String rutaXML : listaXML) {
				try {
					File fileXML = new File(rutaXML);
					Comprobante _comprobante = LeerXML.ObtenerComprobante(rutaXML);
					
					if (_comprobante == null) {
				    		logger.info("XML Fallo proceso ====>"+fileXML.getName());
				    }else if ("N".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) { // es de nomina
				    	listaNomina.add(fileXML);
				    	listaComprobantes.add(_comprobante);
				    }else if (empresaForm.getRfc().equalsIgnoreCase(_comprobante.getEmisor().getRfc())) { // es un emitido
				    	bovedaEmitidosBean.procesarXmlBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, fileXML, arrResultadoEmitido, usuarioHTTP, false);
				    	listaComprobantes.add(_comprobante);
		    		}else { // es un recibido
		    			 bovedaBean.procesarXmlBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, fileXML, arrResultado, false);
		    			 listaComprobantes.add(_comprobante);
				    }
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				}
			}

			if (listaNomina.size() > 0) {
      	       bovedaNominaBean.procesarXmlBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, listaNomina, arrResultadoNomina, usuarioHTTP, false);
      	    }
			
			if (listaComprobantes.size() > 0) {
				guardarMetadataTimbrado(conEmpresa, rcEmpresa.getEsquema(), listaComprobantes);
				actualizarEncontradosBovedaRecibidos(conEmpresa, rcEmpresa.getEsquema());
				actualizarEncontradosBovedaEmitidos(conEmpresa, rcEmpresa.getEsquema());
				actualizarEncontradosBovedaNomina(conEmpresa, rcEmpresa.getEsquema());
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
	
	
	
	public int guardarMetadataTimbrado(Connection con, String esquema, ArrayList<Comprobante> listaDetalle) {
 		PreparedStatement stmt = null;
 		PreparedStatement stmtSelect = null;
 		PreparedStatement stmtUpdate = null;
 		ResultSet rs = null;
 		StringBuffer sbQuery = new StringBuffer();
 		int resultado = 0;
 		LocalDateTime fechaTimbradoLD   = null;
 		String fechaTimbrado = null;
 		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 		try {
 			
 			stmtSelect = con.prepareStatement(DescargaSATQuerys.getExisteUUID(esquema));
 			stmtUpdate = con.prepareStatement(DescargaSATQuerys.getActualizarMetadataTimbrado(esquema));
 			
 			sbQuery.append(DescargaSATQuerys.getGuardarMetadataTimbrado(esquema));
 			sbQuery.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?) "); 
 			stmt = con.prepareStatement(sbQuery.toString());
 			String tipoMoneda = null;
 			int numError = 0;
 			String []  arrEstatus = null;
 			String estatusSAT = null;
 			String tipoComprobante = null;
 			for (Comprobante _comprobante : listaDetalle) {
 				try {
 					estatusSAT = null;
	 				// int numParam=1;
 					stmtSelect.setString(1, Utils.noNulo(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID()));
 					
 					rs = stmtSelect.executeQuery();
 					if (rs.next()) {
 						estatusSAT = Utils.noNulo(rs.getString(1));
 						tipoMoneda = Utils.noNulo(rs.getString(2));
 					}
 					
 					try {
 						rs.close();
 						rs = null;
 					}catch(Exception e) {
 						rs = null;
 					}
 					
 					if (estatusSAT == null) { // no existe
 						arrEstatus = UtilsSAT.validaSAT(_comprobante.getEmisor().getRfc(), _comprobante.getReceptor().getRfc(), _comprobante.getTotal(), _comprobante.getComplemento().getTimbreFiscalDigital().getUUID());
 	 					if ("".equalsIgnoreCase(arrEstatus[0])) {
 	 						estatusSAT = "VIGENTE";
 	 					}
 	 					
 	 					try {
 	 	 					tipoMoneda = Utils.noNulo(_comprobante.getMoneda());
 	 	 					if (tipoMoneda.length() > 3) {
 	 	 						String desMoneda = UtilsValidaciones.validaDesMoneda(tipoMoneda);
 	 	 						if (desMoneda == null) {
 	 	 							tipoMoneda = "MXN";
 	 	 						}else {
 	 	 							String cadMoneda [] = desMoneda.split(";"); 
 	 	 							tipoMoneda = cadMoneda [1];
 	 	 						}
 	 	 					}
 	 					}catch(Exception e) {
 	 						tipoMoneda = null;
 	 					}
 	 					
 	 					fechaTimbradoLD   = _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado();
 	 					fechaTimbrado = fechaTimbradoLD.format(formatter);
 	 					
 	 					tipoComprobante = Utils.noNulo(_comprobante.getTipoDeComprobante());
 	 					if ("ingreso".equalsIgnoreCase(tipoComprobante)) {
 	 						tipoComprobante = "I";
 	 					}else if ("egreso".equalsIgnoreCase(tipoComprobante)) {
 	 						tipoComprobante = "E";
 	 					}else if ("traslado".equalsIgnoreCase(tipoComprobante)) {
 	 						tipoComprobante = "T";
 	 					}
 	 					
 		 				stmt.setString(1, Utils.noNulo(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID()));
 		 				stmt.setString(2, Utils.noNulo(_comprobante.getEmisor().getRfc()));
 		 				stmt.setString(3, Utils.noNuloNormal(_comprobante.getEmisor().getNombre()));
 		 				stmt.setString(4, Utils.noNulo(_comprobante.getReceptor().getRfc()));
 		 				stmt.setString(5, Utils.noNuloNormal(_comprobante.getReceptor().getNombre()));
 		 				stmt.setString(6, "PENDIENTE");
 		 				stmt.setString(7, fechaTimbrado);
 		 				stmt.setDouble(8, _comprobante.getTotal());
 		 				stmt.setString(9, tipoComprobante);
 		 				stmt.setString(10, tipoMoneda);
 		 				stmt.setString(11, Utils.noNulo(arrEstatus[0]));
 		 				stmt.setString(12, "N");
 		 				stmt.setString(13, usuarioHTTP);
 		 				resultado+= stmt.executeUpdate();
 					}else if ("VIGENTE".equalsIgnoreCase(estatusSAT)) { // se revalida su estatus en el SAT
 						// logger.info("************ ACTUALIZANDO ESTATUS*******************");
 						arrEstatus = UtilsSAT.validaSAT(_comprobante.getEmisor().getRfc(), _comprobante.getReceptor().getRfc(), _comprobante.getTotal(), _comprobante.getComplemento().getTimbreFiscalDigital().getUUID());
 	 					if ("".equalsIgnoreCase(arrEstatus[0])) {
 	 						estatusSAT = "VIGENTE";
 	 					}else {
 	 						estatusSAT = arrEstatus[0];
 	 					}
 	 					if (estatusSAT.equalsIgnoreCase("CANCELADO")) {
 	 						stmtUpdate.setString(1, tipoMoneda);
 	 						stmtUpdate.setString(2, estatusSAT);
 	 						stmtUpdate.setString(3, Utils.noNulo(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID()));
 	 						stmtUpdate.executeUpdate();
 	 					}
 	 					
 					}
 					
 				}catch(SQLException sql) {
 					 if (sql.getErrorCode() == 1062) {
 						numError++;
 						// logger.info("ErroCode ====>"+sql.getErrorCode()+", uuid==>"+Utils.noNulo(jsonObject.getString("UUID")));	
 					}else {
 						logger.info("ErroCode ====>"+sql.getErrorCode()+", uuid==>"+Utils.noNulo(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID()));
 					}
 				}catch(Exception exa) {
 					// numError++;
 					// logger.info("stmtFallo ====>"+stmt);
 					logger.info("jsonObject fallo ====>"+_comprobante.getComplemento().getTimbreFiscalDigital().getUUID());
 					Utils.imprimeLog("", exa);
 				}
 			}
 			logger.info("resultado ====>"+resultado);
 			logger.info("numError ====>"+numError);
 			
 		}catch (Exception e) {
 			Utils.imprimeLog("", e);
			resultado = -1;
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
				if (stmtSelect != null) {
					stmtSelect.close();
				}
				stmtSelect = null;
				if (stmtUpdate != null) {
					stmtUpdate.close();
				}
				stmtUpdate = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
 		return resultado;
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
	 
	
}
