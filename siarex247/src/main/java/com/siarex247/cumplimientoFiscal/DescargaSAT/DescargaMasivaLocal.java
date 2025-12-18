package com.siarex247.cumplimientoFiscal.DescargaSAT;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaBean;
import com.siarex247.cumplimientoFiscal.BovedaEmitidos.BovedaEmitidosBean;
import com.siarex247.cumplimientoFiscal.BovedaNomina.BovedaNominaBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.ManipularLibros;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;
import com.siarex247.validaciones.UtilsSAT;
import com.siarex247.validaciones.UtilsValidaciones;

public class DescargaMasivaLocal {

	public static final Logger logger = Logger.getLogger("siarex247");
	final String usuarioHTTP = "procesos@siarex.com";
	private HashMap<String, String> mapaRFC = new HashMap<String, String>();
	public void monitoreaCorreo(){
		try{
				try{
					mapaRFC = new HashMap<String, String>();
					
					logger.info("Descargando archivo .ZIP del server...............");
					String rutaZip = UtilsPATH.RUTA_PUBLIC_HTML + "DESCARGA_LOCAL" + File.separator + "TRABAJO" + File.separator;
					String rutaEnProceso = UtilsPATH.RUTA_PUBLIC_HTML + "DESCARGA_LOCAL" + File.separator + "EN_PROCESO" + File.separator;
					String unZip = UtilsPATH.RUTA_PUBLIC_HTML + "DESCARGA_LOCAL" + File.separator + "UNZIP" + File.separator;
					
					
					//int numFile = 1;
					File listFileZIP = new File(rutaZip);
					String enProceso = null; 
					for (File fileZIP : listFileZIP.listFiles()) {
						enProceso = rutaEnProceso + fileZIP.getName();
						File fileDest = new File(enProceso);
						UtilsFile.moveFileDirectory(fileZIP, fileDest, false, false, true, true);
					}
					
					listFileZIP = new File(rutaEnProceso);
					for (File fileZIP : listFileZIP.listFiles()) {
						logger.info("Procesando archivo........."+fileZIP.getAbsolutePath());
						ZipFiles.unzip(fileZIP.getAbsolutePath(), unZip);
						File folderUnZip = new File(unZip);
						for (File FileXML : folderUnZip.listFiles()) {
							logger.info("tipo de archivo........."+UtilsFile.getContentType(FileXML));
							// logger.info("Ruta........."+FileXML.getAbsolutePath());
							if ( "application/vnd.ms-excel".equalsIgnoreCase(UtilsFile.getContentType(FileXML)) || "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(UtilsFile.getContentType(FileXML))) { // es un excel, motivo de cancelacion
								procesarXLS(FileXML);
							}else {
								procesarXML(FileXML);	
							}
						}
						 
						 
						File delelteUnZip = new File(unZip); 
						UtilsFile.deleteDirectoryContentA(delelteUnZip);
						Path origenPath = Paths.get(fileZIP.getAbsolutePath()); // se elimina el archivo .ZIP
						Files.deleteIfExists(origenPath);
						 
						 
					}
					/*
					File listFileProcesados = new File(rutaEnProceso);
					for (File fileZIP : listFileProcesados.listFiles()) {
						Path origenPath = Paths.get(fileZIP.getAbsolutePath()); // se elimina el archivo .ZIP
				    	Files.deleteIfExists(origenPath);
					}
					*/
					actualizarMetadata();
				}catch(Exception e){
					Utils.imprimeLog("monitoreaCorreo ", e);
				}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
	private void actualizarMetadata() {
		ResultadoConexion rcEmpresa = null;
		Connection conEmpresa = null;
		ConexionDB connPool = new ConexionDB();
		
		EmpresasForm empresaForm = null;
		AccesoBean accesoBean = new AccesoBean();
		
		try {
			
			Set<String> keyReceptor = mapaRFC.keySet();
			for (String rfcReceptor : keyReceptor) {
	            try {
	            	
	            	empresaForm = accesoBean.consultaEmpresaRFC(rfcReceptor);
	            	String esquemaEmpresa = empresaForm.getEsquema();
	    	        rcEmpresa = connPool.getConnection(esquemaEmpresa);
	    	        conEmpresa = rcEmpresa.getCon();
	    	        
	            	actualizarEncontradosBovedaRecibidos(conEmpresa, rcEmpresa.getEsquema());
					actualizarEncontradosBovedaEmitidos(conEmpresa, rcEmpresa.getEsquema());
					actualizarEncontradosBovedaNomina(conEmpresa, rcEmpresa.getEsquema());
					
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
	
	private void procesarXML(File fileXML) {
		EmpresasForm empresaReceptorForm = null;
		EmpresasForm empresaEmisorForm = null;
		AccesoBean accesoBean = new AccesoBean();
		try {
			Comprobante _comprobante = LeerXML.ObtenerComprobante(fileXML.getAbsolutePath());
			String rfcReceptor = Utils.noNulo(_comprobante.getReceptor().getRfc());
			String rfcEmisor = Utils.noNulo(_comprobante.getEmisor().getRfc());
			
			if ("N".equalsIgnoreCase(_comprobante.getTipoDeComprobante()) || "nomina".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
				empresaEmisorForm = accesoBean.consultaEmpresaRFC(rfcEmisor);
				if (rfcEmisor.equalsIgnoreCase(empresaEmisorForm.getRfc())) {
					iniciarProceso(empresaEmisorForm, fileXML, _comprobante);	
					mapaRFC.put(empresaEmisorForm.getRfc(), empresaEmisorForm.getRfc());
				}
				
			}else {
				
				boolean validarRecibido = false;
				empresaEmisorForm = accesoBean.consultaEmpresaRFC(rfcEmisor);
				
				if (rfcEmisor.equalsIgnoreCase(empresaEmisorForm.getRfc())) { // es un emitido
					mapaRFC.put(empresaEmisorForm.getRfc(), empresaEmisorForm.getRfc());
					iniciarProceso(empresaEmisorForm, fileXML, _comprobante);	
					validarRecibido = true;
				}else {
					empresaReceptorForm = accesoBean.consultaEmpresaRFC(rfcReceptor);
					if (rfcReceptor.equalsIgnoreCase(empresaReceptorForm.getRfc())) { // es un recibido
						mapaRFC.put(empresaReceptorForm.getRfc(), empresaReceptorForm.getRfc());
						iniciarProceso(empresaReceptorForm, fileXML, _comprobante);
					}
				}
				
				// se valida si tambien se debe agregar al RFC de Recibido
				if (validarRecibido) {
					empresaReceptorForm = accesoBean.consultaEmpresaRFC(rfcReceptor);
					if (rfcReceptor.equalsIgnoreCase(empresaReceptorForm.getRfc())) { // es un recibido del Receptor 2
						mapaRFC.put(empresaReceptorForm.getRfc(), empresaReceptorForm.getRfc());
						iniciarProceso(empresaReceptorForm, fileXML, _comprobante);
					}
				}
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	private void iniciarProceso(EmpresasForm empresaForm, File fileXML, Comprobante _comprobante) {
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
			
			try {
				
				if (_comprobante == null) {
			    		logger.info("XML Fallo proceso ====>");
			    }else if ("N".equalsIgnoreCase(Utils.noNulo(_comprobante.getTipoDeComprobante())) || "nomina".equalsIgnoreCase(Utils.noNulo(_comprobante.getTipoDeComprobante()))) { // es de nomina
			    	listaNomina.add(fileXML);
			    	listaComprobantes.add(_comprobante);
			    }else if (empresaForm.getRfc().equalsIgnoreCase(Utils.noNulo(_comprobante.getEmisor().getRfc()))) { // es un emitido
			    	bovedaEmitidosBean.procesarXmlBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, fileXML, arrResultadoEmitido, usuarioHTTP, false);
			    	listaComprobantes.add(_comprobante);
	    		}else { // es un recibido
	    			 bovedaBean.procesarXmlBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, fileXML, arrResultado, false);
	    			 listaComprobantes.add(_comprobante);
			    }
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}
			 
			if (listaNomina.size() > 0) {
      	       bovedaNominaBean.procesarXmlBoveda(conEmpresa, rcEmpresa.getEsquema(), esquemaEmpresa, listaNomina, arrResultadoNomina, usuarioHTTP, false);
      	    }
			
			if (listaComprobantes.size() > 0) {
				guardarMetadataTimbrado(conEmpresa, rcEmpresa.getEsquema(), listaComprobantes);
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
 			String fechaActual = UtilsFechas.getFechaActual();
 			
 			stmtSelect = con.prepareStatement(DescargaSATQuerys.getExisteUUID(esquema));
 			stmtUpdate = con.prepareStatement(DescargaSATQuerys.getActualizarMetadataTimbrado(esquema));
 			
 			sbQuery.append(DescargaSATQuerys.getGuardarMetadataTimbrado(esquema));
 			sbQuery.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "); 
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
 						if ("".equalsIgnoreCase(arrEstatus[0]) || "NO ENCONTRADO".equalsIgnoreCase(arrEstatus[0])) {
 	 						estatusSAT = "VIGENTE";
 	 					}else {
 	 						estatusSAT = arrEstatus[0];
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
 		 				stmt.setString(11, estatusSAT);
 		 				stmt.setString(12, "N");
 		 				stmt.setString(13, usuarioHTTP);
 		 				if (estatusSAT.equalsIgnoreCase("CANCELADO")) {
 		 					stmt.setString(14, usuarioHTTP);
 		 					stmt.setString(15, fechaActual);
 		 				}else {
 		 					stmt.setString(14, null);
 		 					stmt.setString(15, null);
 		 				}
 		 				resultado+= stmt.executeUpdate();
 					}else if ("VIGENTE".equalsIgnoreCase(estatusSAT) ) { // se revalida su estatus en el SAT
 						// logger.info("************ ACTUALIZANDO ESTATUS*******************");
 						arrEstatus = UtilsSAT.validaSAT(_comprobante.getEmisor().getRfc(), _comprobante.getReceptor().getRfc(), _comprobante.getTotal(), _comprobante.getComplemento().getTimbreFiscalDigital().getUUID());
 	 					if ("".equalsIgnoreCase(arrEstatus[0]) || "NO ENCONTRADO".equalsIgnoreCase(arrEstatus[0])) {
 	 						estatusSAT = "VIGENTE";
 	 					}else {
 	 						estatusSAT = arrEstatus[0];
 	 					}
 	 					if (estatusSAT.equalsIgnoreCase("CANCELADO")) {
 	 						stmtUpdate.setString(1, estatusSAT);
 	 						stmtUpdate.setString(2, tipoMoneda);
 	 						stmtUpdate.setString(3, usuarioHTTP);
 	 						//stmtUpdate.setString(4, Utils.noNulo(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID()));
 	 						stmtUpdate.setString(4, fechaActual);
 	 						stmtUpdate.setString(5, Utils.noNulo(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID()));
 	 						
 	 						resultado+= stmtUpdate.executeUpdate();
 	 					}
 					}
 				}catch(SQLException sql) {
 					 if (sql.getErrorCode() == 1062) {
 						numError++;
 						// logger.info("ErroCode ====>"+sql.getErrorCode()+", uuid==>"+Utils.noNulo(jsonObject.getString("UUID")));	
 					}else {
 						logger.info("ErroCode ====>"+sql.getErrorCode()+", uuid==>"+Utils.noNulo(_comprobante.getComplemento().getTimbreFiscalDigital().getUUID()));
 					}
 					Utils.imprimeLog("", sql);
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
	 
	 
	 
	 // PROCESANDO ARCHIVOS DE MOTIVOS DE CANCELACION
	 
	 private void procesarXLS(File fileXLS) {
			EmpresasForm empresaReceptorForm = null;
			EmpresasForm empresaEmisorForm = null;
			AccesoBean accesoBean = new AccesoBean();
			Iterator<Row> row = null;
			Iterator<Cell> celda = null;
			Row r = null;
			XSSFSheet hojaMotivos;
			boolean bandRow = true;
			MotivosCancelacionForm motivosForm = null;
			String rfcReceptor = null;
			String rfcEmisor = null;
			try {
				
				// logger.info("Manipular Libro====>"+fileXLS.getAbsolutePath());
				ManipularLibros librosExcel = new ManipularLibros();
				librosExcel.cargarArchivoXLSX(fileXLS.getAbsolutePath());
				hojaMotivos = librosExcel.obtenerHojaXLSX(0);
				row = hojaMotivos.iterator();
				while (row.hasNext()) {
					r = (Row)row.next();
					if (r != null && bandRow) { // se elimina el encabezado
						r = (Row)row.next();
						bandRow =  false;
					}
					celda = r.cellIterator();
					motivosForm = llenaFormaMotivos(celda);
					if (!"".equalsIgnoreCase(motivosForm.getUuid())) {
						rfcReceptor = Utils.noNulo(motivosForm.getRfcReceptor());
						rfcEmisor = Utils.noNulo(motivosForm.getRfcEmisor());
						
						boolean validarRecibido = false;
						empresaEmisorForm = accesoBean.consultaEmpresaRFC(rfcEmisor);
						if (rfcEmisor.equalsIgnoreCase(empresaEmisorForm.getRfc())) { // es un emitido
							int res = iniciarProcesoCancelacion(empresaEmisorForm, motivosForm);	
							if (res > 0) {
								actualizarEstatusEmitidos(empresaEmisorForm, motivosForm);
							}
							validarRecibido = true;
						}else {
							empresaReceptorForm = accesoBean.consultaEmpresaRFC(rfcReceptor);
							// logger.info("rfcReceptor ====>"+rfcReceptor+", Receptor BD===>"+empresaReceptorForm.getRfc());
							
							if (rfcReceptor.equalsIgnoreCase(empresaReceptorForm.getRfc())) { // es un recibido
								int res = iniciarProcesoCancelacion(empresaReceptorForm, motivosForm);
								if (res > 0) {
									actualizarEstatusRecibidos(empresaReceptorForm, motivosForm);	
								}
							}
						}
						
						// se valida si tambien se debe agregar al RFC de Recibido
						if (validarRecibido) {
							empresaReceptorForm = accesoBean.consultaEmpresaRFC(rfcReceptor);
							if (rfcReceptor.equalsIgnoreCase(empresaReceptorForm.getRfc())) { // es un recibido del Receptor 2
								int res = iniciarProcesoCancelacion(empresaReceptorForm, motivosForm);
								if (res > 0) {
									actualizarEstatusRecibidos(empresaReceptorForm, motivosForm);	
								}
							}
						}
					}
					
				}
				
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}
		}
	 
	 
	 private int iniciarProcesoCancelacion(EmpresasForm empresaForm, MotivosCancelacionForm motivosForm) {
			ConexionDB connPool = null;
		     ResultadoConexion rcEmpresa = null;
		     Connection conEmpresa = null;
		     PreparedStatement stmt = null;
		     int resultado = 0;
			try {
				String esquemaEmpresa = empresaForm.getEsquema();
		        connPool = new ConexionDB();
		        rcEmpresa = connPool.getConnection(esquemaEmpresa);
		        conEmpresa = rcEmpresa.getCon();
				try {
					// logger.info("Estatus Comprobante...."+motivosForm.getEstadoComprobante());
					// logger.info("DescripcionCancelacion..."+motivosForm.getDescripcionCancelacion());
					if ("CANCELADO".equalsIgnoreCase(motivosForm.getEstadoComprobante())) {
						
						if ("".equalsIgnoreCase(Utils.noNulo(motivosForm.getClaveCancelacion()))) {
							motivosForm.setClaveCancelacion("NE");
						}
						
				        stmt = conEmpresa.prepareStatement( ProcesoDescargaSATQuerys.getUpdateCancelados(rcEmpresa.getEsquema()));
				        stmt.setString(1, "CANCELADO");
				        stmt.setString(2, motivosForm.getPacCertifico());
				        stmt.setString(3, motivosForm.getFechaCancelacion().replaceAll("T", " "));
				        stmt.setString(4, motivosForm.getClaveCancelacion());
				        stmt.setString(5, motivosForm.getDescripcionCancelacion());
				        stmt.setString(6, motivosForm.getUuid());
				        // logger.info("stmtActualiza===>"+stmt);
				        resultado = stmt.executeUpdate();
					}
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				}
				 
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally{
		        try{
		            if(stmt != null)
		            	stmt.close();
		            stmt = null;
		            if(conEmpresa != null)
		            	conEmpresa.close();
		            conEmpresa = null;
		            
		        }catch(Exception e){
		        	stmt = null;
		            conEmpresa = null;
		        }
		     }
			return resultado;
		}
	 
	 
	 private void actualizarEstatusRecibidos(EmpresasForm empresaForm,  MotivosCancelacionForm motivosForm) {
			ConexionDB connPool = null;
		     ResultadoConexion rcEmpresa = null;
		     Connection conEmpresa = null;
		     PreparedStatement stmtBoveda = null;
			try {
				
				String esquemaEmpresa = empresaForm.getEsquema();
		        connPool = new ConexionDB();
		        rcEmpresa = connPool.getConnection(esquemaEmpresa);
		        conEmpresa = rcEmpresa.getCon();
		        
				stmtBoveda = conEmpresa.prepareStatement(DescargaSATQuerys.getActualizarBovedaEstatus(rcEmpresa.getEsquema()));
				stmtBoveda.setString(1, "C");
				stmtBoveda.setString(2, motivosForm.getUuid());
				stmtBoveda.executeUpdate();
				// logger.info("stmtBoveda===>"+stmtBoveda);
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally{
		        try{
		            if(stmtBoveda != null)
		            	stmtBoveda.close();
		            stmtBoveda = null;
		            if(conEmpresa != null)
		            	conEmpresa.close();
		            conEmpresa = null;
		            
		        }catch(Exception e){
		        	stmtBoveda = null;
		            conEmpresa = null;
		        }
		     }
		}
	 
	 private void actualizarEstatusEmitidos(EmpresasForm empresaForm,  MotivosCancelacionForm motivosForm) {
			ConexionDB connPool = null;
		     ResultadoConexion rcEmpresa = null;
		     Connection conEmpresa = null;
		     PreparedStatement stmtNomina = null;
		     PreparedStatement stmtEmitidos = null;
		     PreparedStatement stmtSelect = null;
		     ResultSet rs = null;
			try {
				
				String esquemaEmpresa = empresaForm.getEsquema();
		        connPool = new ConexionDB();
		        rcEmpresa = connPool.getConnection(esquemaEmpresa);
		        conEmpresa = rcEmpresa.getCon();
		        
		        stmtSelect = conEmpresa.prepareStatement(ProcesoDescargaSATQuerys.getSelectCancelado(rcEmpresa.getEsquema()));
		        stmtSelect.setString(1, motivosForm.getUuid());
		        rs = stmtSelect.executeQuery();
		        String tipoComprobante = null;
		        if (rs.next()) {
		        	tipoComprobante = Utils.noNulo(rs.getString(1));
		        }
		        
		        if (tipoComprobante == null) {
		        	logger.info("No se encontro el comprobante en descarga masiva...."+motivosForm.getUuid());
		        }else if ("N".equalsIgnoreCase(tipoComprobante)) { // si es nomina, entonces actualizada nomina
		        	stmtNomina = conEmpresa.prepareStatement(DescargaSATQuerys.getActualizarBovedaNominaEstatus(rcEmpresa.getEsquema()));
		        	stmtNomina.setString(1, "C");
		        	stmtNomina.setString(2, motivosForm.getUuid());
		        	stmtNomina.executeUpdate();
		        	// logger.info("stmtNomina===>"+stmtNomina);
		        	
		        }else {
		        	stmtEmitidos = conEmpresa.prepareStatement(DescargaSATQuerys.getActualizarBovedaEmitidosEstatus(rcEmpresa.getEsquema()));
		        	stmtEmitidos.setString(1, "C");
		        	stmtEmitidos.setString(2, motivosForm.getUuid());
		        	stmtEmitidos.executeUpdate();
		        	// logger.info("stmtEmitidos===>"+stmtEmitidos);
		        }
						
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally{
		        try{
		        	if(rs != null)
		        		rs.close();
		        	rs = null;
		        	
	        	   if(stmtSelect != null)
		            	stmtSelect.close();
		            stmtSelect = null;
			            
		            if(stmtNomina != null)
		            	stmtNomina.close();
		            stmtNomina = null;
		            
		            if(stmtEmitidos != null)
		            	stmtEmitidos.close();
		            stmtEmitidos = null;
		            
		            if(conEmpresa != null)
		            	conEmpresa.close();
		            conEmpresa = null;
		            
		        }catch(Exception e){
		        	stmtNomina = null;
		        	stmtEmitidos = null;
		        	stmtSelect = null;
		            conEmpresa = null;
		        }
		     }
		}
	 
	 
	 private MotivosCancelacionForm llenaFormaMotivos(Iterator<Cell> celda){
			MotivosCancelacionForm motivosForm = new MotivosCancelacionForm();
			Cell cel = null;
			try{
				while (celda.hasNext()) {
					  cel =(Cell) celda.next();
						if (cel.getColumnIndex() == 2){ // uuid
							motivosForm.setUuid(Utils.noNulo(cel.toString()));
						}else if (cel.getColumnIndex() == 3){ // rfcEmisor
							motivosForm.setRfcEmisor(Utils.noNulo(cel.toString()));
						}else if (cel.getColumnIndex() == 5){ // rfc Receptor
							motivosForm.setRfcReceptor(Utils.noNulo(cel.toString()));
						}else if (cel.getColumnIndex() == 9){ // pac Certifico
							motivosForm.setPacCertifico(Utils.noNulo(cel.toString()));
						}else if (cel.getColumnIndex() == 13){ // estado comprobante
							motivosForm.setEstadoComprobante(Utils.noNulo(cel.toString()));
						}else if (cel.getColumnIndex() == 16){ // fecha cancelacion
							motivosForm.setFechaCancelacion(Utils.noNulo(cel.toString()));
						}else if (cel.getColumnIndex() == 17){ // motivo de cancelacion
							String valorCelda = Utils.noNulo(cel.toString());
							motivosForm.setDescripcionCancelacion(valorCelda);
							motivosForm.setClaveCancelacion(obtenerClave(valorCelda));
							
						}
						
				}
			}catch(Exception e){
				Utils.imprimeLog("", e);
			}
			return motivosForm;
		}
	
	 private String obtenerClave(String desClave) {
		 
		 if (desClave == null || desClave.length() ==0 || "".equalsIgnoreCase(Utils.noNulo(desClave))) {
			 return "NEX";
		 }else if (desClave.toLowerCase().indexOf("Comprobantes emitidos con errores con relaci") > -1 ) {
			 return "01";
		 }else if (desClave.toLowerCase().indexOf("Comprobantes emitidos con errores sin relaci") > -1 ) {
			 return "02";
		 }else if (desClave.toLowerCase().indexOf(" a cabo la operaci") > 0) {
			 return "03";
		 }else if (desClave.toLowerCase().indexOf("nominativa relacionada en una factura global") > -1 ) {
			 return "04";
		 }
		 return "NEX";
	 }
}
