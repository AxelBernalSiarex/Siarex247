package com.siarex247.layOut.OrdenesCompra;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.CentroCostos.CentroCostosBean;
import com.siarex247.catalogos.Empleados.EmpleadosBean;
import com.siarex247.utils.ManipularLibros;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;

public class CargasComprasTXTBean extends ManipularLibros {
	public static final Logger logger = Logger.getLogger("siarex");
	private final String N = "N";
	private final String S = "S";
	
	
	private boolean moverArchivo(String rutaArchivo, String nombreArchivo, String rutaRepositorio){
		try{
			// Movemos el archivo
			File fsource = new File(rutaArchivo.concat(nombreArchivo));
			File fdes = new File(rutaRepositorio.concat(nombreArchivo) );
			if (UtilsFile.existe(rutaArchivo)){
				UtilsFile.moveFileDirectory(fsource,fdes, true, false, true);
				return true;
			}
			return false;
		}catch(Exception e){
			Utils.imprimeLog("moviendo el archivo ", e);
		}
		return false;
	}
	
	
	
	public void procesaArchivoTXT(final String rutaArch,final String nombreArchivo, final String usuarioTrans, final String esquema, final int claveRegistro, final String requisicion ){
		try{
			   	Thread a = new Thread(new Runnable() {
						public void run() {
							File fileTXT = new File(rutaArch);
							procesoOrdenesTXT(rutaArch, esquema, nombreArchivo, usuarioTrans, claveRegistro, requisicion);	
														
						}
					});
					a.start();
		}catch(Exception e){
			Utils.imprimeLog("Procesando Archivo TXT ", e);
			OrdenesCompraBean historialBean = new OrdenesCompraBean();
			ConexionDB connPool = new ConexionDB();
			Connection con = null;
			ResultadoConexion rc = null;
			try{
				rc = connPool.getConnection(esquema);
				con = rc.getCon();
				historialBean.actualizaHistorial(con, esquema, claveRegistro, 0, 3, 0, 0, usuarioTrans);
			}catch(Exception err){
				Utils.imprimeLog("Al procesar el archivo ", err);
				 eliminaArchivo(rutaArch);
			}finally{
				try{
					if (con != null){
						con.close();
					}
					con = null;
				}catch(Exception sql){
					con = null;
				}
			}
		}
	}
	
	
	public void procesoOrdenesTXT(String rutaArch, String esquema, String nombreArchivo, String usuarioTrans, int claveRegistro, String requisicion){
		PreparedStatement stmInsert = null;
		PreparedStatement stmRFC = null;
		PreparedStatement stmVendor = null;
		PreparedStatement stmInsertSR = null;
		ResultSet rs = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		ResultadoConexion rc = null;
		try{
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			stmInsert = con.prepareStatement(OrdenesCompraQuerys.getQueryGrabarOrden(esquema));
			stmInsertSR = con.prepareStatement(OrdenesCompraQuerys.getQueryGrabarOrdenServRecibido(esquema));
			
			stmRFC = con.prepareStatement(OrdenesCompraQuerys.getQueryInfoProveedorRFC(esquema));
			stmVendor = con.prepareStatement(OrdenesCompraQuerys.getQueryInfoProveedorVendor(esquema));
			
			CargaOrdenesForm ordenesCompraForm = null;
			int totRegistros = 0;
			int regOK = 0;
			int regNG = 0;
			int rfcProveedorRS = 0;
			OrdenesCompraBean historialBean = new OrdenesCompraBean();
			ArrayList<String> fileTXT = UtilsFile.leeArchivoTXT(rutaArch);
			int x = 0;
			
			EmpleadosBean empleadosBean = new EmpleadosBean();
			HashMap<String, String> mapaEmpleados = empleadosBean.obteneEmpleadosCarga(con, esquema);
			String claveEmpleado = null;
			CentroCostosBean centroCostosBean = new CentroCostosBean();
			HashMap<String, String> mapaCostos = centroCostosBean.obtenerCentros(con, esquema);
			String asignarTO = null;
			for (x = 0; x < fileTXT.size(); x++){
				rfcProveedorRS = 0;
				if (!"".equals(fileTXT.get(x))){
					ordenesCompraForm = llenaFormaOrdenesTXT(fileTXT.get(x), requisicion);
					if (ordenesCompraForm.getNumeroFolioEmpresa() > -1){
						if (ordenesCompraForm.getNumeroFolioEmpresa() == 0){
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, 0, "El folio de la factura no debe ser 0.", N, null);
							regNG++;
						}else if (ordenesCompraForm.getMonto() == 0){
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El monto de la factura no debe ser 0.", N, null);
							regNG++;
						}else if ("".equals(ordenesCompraForm.getRfc()) && "NO_VALIDAR".equalsIgnoreCase(ordenesCompraForm.getIdVendor())){
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El RFC del proveedor no debe ser vacio.", N, null);
							regNG++;
						}else if ("".equals(ordenesCompraForm.getTipoMoneda()) || (!"MXN".equalsIgnoreCase(ordenesCompraForm.getTipoMoneda())) && !"USD".equalsIgnoreCase(ordenesCompraForm.getTipoMoneda())) {
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "Tipo de moneda no valido.", N, null);
							regNG++;
						}else {
							ordenesCompraForm.setUsuario(usuarioTrans);
							if ("NO_VALIDAR".equals(ordenesCompraForm.getIdVendor())){
								stmRFC.setString(1, ordenesCompraForm.getRfc());
								rs = stmRFC.executeQuery();
							}else{
								stmVendor.setString(1, ordenesCompraForm.getIdVendor().toUpperCase());
								rs = stmVendor.executeQuery();
							}
							
							if (rs.next()){
								rfcProveedorRS = rs.getInt(5);
							}
							
							if (rfcProveedorRS == 0){ // si encontro el proveedor
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El proveedor no existe en la base de datos.", N, null);
								regNG++;
							}else{
								ordenesCompraForm.setClaveProveedor(rfcProveedorRS);
								asignarTO = Utils.noNulo(mapaCostos.get(ordenesCompraForm.getAsignarTO()));
								claveEmpleado = mapaEmpleados.get(ordenesCompraForm.getAsignarTO());
								if (claveEmpleado != null && !"0".equalsIgnoreCase(claveEmpleado)){
									ordenesCompraForm.setAsignarTO("E_"+claveEmpleado);
								}
								
								if (!"".equalsIgnoreCase(asignarTO)) {
									ordenesCompraForm.setAsignarTO("C_"+asignarTO);
								}
								
								if ("".equalsIgnoreCase(ordenesCompraForm.getAsignarTO())) {
									ordenesCompraForm.setAsignarTO(null);
								}
								
								int numReg = grabarOrden(stmInsert, stmInsertSR, ordenesCompraForm); // se guarda el registro de la orden
								if (numReg == -803 || numReg == 803 || numReg == 1062 || numReg == -1062){
									historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El folio "+ordenesCompraForm.getNumeroFolioEmpresa()+" ya existe en la base de datos.", N, null);
									regNG++;
								}else{
									regOK++;
									historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "Registro guardado satisfactoriamente.", S, null);
								}
							}
							if (rs != null){
								rs.close();
							}
							rs = null;
							
						}
						totRegistros++;
					}
				}
			}
			
			historialBean.actualizaHistorial(con, esquema, claveRegistro, totRegistros, 0, regOK, regNG, usuarioTrans);
			eliminaArchivo(rutaArch);
		}catch(Exception e){
			 Utils.imprimeLog("Error al procesar el archivo", e);
			 eliminaArchivo(rutaArch);
				OrdenesCompraBean historialBean = new OrdenesCompraBean();
				try{
					historialBean.actualizaHistorial(con, esquema, claveRegistro, 0, 3, 0, 0, usuarioTrans);
				}catch(Exception err){
					logger.info("Actualizando el historico_1 : "+e);
				}
		}finally{
			try{
				if (stmInsert != null){
					stmInsert.close();
				}
				stmInsert = null;
				if (stmInsertSR != null){
					stmInsertSR.close();
				}
				stmInsertSR = null;
				if (con != null){
					con.close();
				}
				con = null;
			}catch(Exception e){
				stmInsert = null;
				con = null;
			}
		}
	}
	
	
	private CargaOrdenesForm llenaFormaOrdenesTXT(String registroTXT, String requisicion){
		CargaOrdenesForm ordenesCompraForm = new CargaOrdenesForm();
		String servRecibido = "";
		int numRen = 0;
		try{
			if (!"".equals(registroTXT) && registroTXT.indexOf(";") > -1){
				String folioEmpresa = "";
				ordenesCompraForm.setEstatus("A5");
				String cadenaRow [] = registroTXT.split(";");
							try{
								int pos = 0;
								pos = cadenaRow[0].toString().indexOf("."); 
								if ( pos > -1){
									folioEmpresa = cadenaRow[0].toString().trim().substring(0, pos);
								}else{
									folioEmpresa = cadenaRow[0].toString().trim();
								}
								ordenesCompraForm.setNumeroFolioEmpresa(Long.parseLong(folioEmpresa));
							}catch(Exception e){
								ordenesCompraForm.setNumeroFolioEmpresa(0);
								e.printStackTrace();
							}

							ordenesCompraForm.setConcepto(cadenaRow[1].toString().trim());

							ordenesCompraForm.setTipoMoneda(cadenaRow[2].toString().trim());

							try{
								ordenesCompraForm.setMonto(Double.parseDouble(cadenaRow[3].toString().trim()));
							}catch(Exception e){
								ordenesCompraForm.setMonto(0);
							}
							
							try{
								if ("".equalsIgnoreCase(cadenaRow[4].trim()) || "INTERNACIONAL".equalsIgnoreCase(cadenaRow[4].trim())){ // si no viene el RFC se toma el vendor
									ordenesCompraForm.setRfc(cadenaRow[4].trim());	
									ordenesCompraForm.setIdVendor(cadenaRow[5].trim());
								}else{
									ordenesCompraForm.setRfc(cadenaRow[4].trim());	
									ordenesCompraForm.setIdVendor("NO_VALIDAR");
								}
								
							}catch(Exception e){
								ordenesCompraForm.setRfc("");
								ordenesCompraForm.setIdVendor("NO_VALIDAR");
							}
							
							try{
								if ("N".equalsIgnoreCase(requisicion)){
									numRen = 6;
									if ("".equalsIgnoreCase(cadenaRow[numRen].toString().trim())) {
										ordenesCompraForm.setAsignarTO("");	
									}else {
										ordenesCompraForm.setAsignarTO(cadenaRow[numRen].toString().trim());
									}
								}else{
									numRen = 6;
									ordenesCompraForm.setNumeroRequisicion(cadenaRow[numRen].trim());
								}
							}catch(Exception e){
								ordenesCompraForm.setNumeroRequisicion("");
							}
							
							try{
								if ("S".equalsIgnoreCase(requisicion)){
									numRen = 7;
									//ordenesCompraForm.setIdEmpleado(cadenaRow[numRen].toString().trim());
									if ("".equalsIgnoreCase(cadenaRow[numRen].toString().trim())) {
										ordenesCompraForm.setAsignarTO("");	
									}else {
										ordenesCompraForm.setAsignarTO(cadenaRow[numRen].toString().trim());
									}
								}
							}catch(Exception e){
								ordenesCompraForm.setAsignarTO("");
							}
							
							
							try{
								numRen++;
								servRecibido = cadenaRow[numRen].toString().trim();
								if (servRecibido == null || "".equals(servRecibido)){
									ordenesCompraForm.setEstatus("A5");
								}else if ("1".equalsIgnoreCase(servRecibido)){
									ordenesCompraForm.setEstatus("A2");
								}else{
									ordenesCompraForm.setEstatus("A5");
								}
							}catch(Exception e){
								ordenesCompraForm.setEstatus("A5");
							}
							
							try{
								numRen++;
								ordenesCompraForm.setUsoCFDI(cadenaRow[numRen].toString().trim());
							}catch(Exception e){
								ordenesCompraForm.setUsoCFDI("");
							}
							
							try{
								numRen++;
								ordenesCompraForm.setClaveProdServ(cadenaRow[numRen].toString().trim());
							}catch(Exception e){
								ordenesCompraForm.setClaveProdServ("");
							}
				}else{
					ordenesCompraForm.setNumeroFolioEmpresa(-1);
				}
		}catch(Exception e){
			Utils.imprimeLog("llenaFormaOrdenesTXT", e);
		}
		return ordenesCompraForm;
	}
	
	
	
	private CargaOrdenesForm llenaFormaOrdenesAmericanasTXT(String registroTXT, String requisicion){
		CargaOrdenesForm ordenesCompraForm = new CargaOrdenesForm();
		try{
			if (!"".equals(registroTXT) && registroTXT.indexOf(";") > -1){
				String folioEmpresa = "";
				ordenesCompraForm.setEstatus("A5");
				String cadenaRow [] = registroTXT.split(";");
							try{
								int pos = 0;
								pos = cadenaRow[0].toString().indexOf("."); 
								if ( pos > -1){
									folioEmpresa = cadenaRow[0].toString().trim().substring(0, pos);
								}else{
									folioEmpresa = cadenaRow[0].toString().trim();
								}
								ordenesCompraForm.setNumeroFolioEmpresa(Long.parseLong(folioEmpresa));
							}catch(Exception e){
								ordenesCompraForm.setNumeroFolioEmpresa(0);
								e.printStackTrace();
							}

							ordenesCompraForm.setConcepto(cadenaRow[1].toString().trim());

							ordenesCompraForm.setTipoMoneda(cadenaRow[2].toString().trim());

							try{
								ordenesCompraForm.setMonto(Double.parseDouble(cadenaRow[3].toString().trim()));
							}catch(Exception e){
								ordenesCompraForm.setMonto(0);
							}
							
							try{
								ordenesCompraForm.setRfc(cadenaRow[4].trim());
							}catch(Exception e){
								ordenesCompraForm.setAsignarTO("");
							}
							
							try{
								if ("".equalsIgnoreCase(cadenaRow[5].toString().trim())) {
									ordenesCompraForm.setAsignarTO("");	
								}else {
									ordenesCompraForm.setAsignarTO(cadenaRow[5].toString().trim());
								}
							}catch(Exception e){
								ordenesCompraForm.setAsignarTO("");
							}
							
							
							try{
								ordenesCompraForm.setFechaUUID(cadenaRow[6].toString().trim());
							}catch(Exception e){
								ordenesCompraForm.setFechaUUID("");
							}
							
							
							
				}else{
					ordenesCompraForm.setNumeroFolioEmpresa(-1);
				}
		}catch(Exception e){
			Utils.imprimeLog("llenaFormaOrdenesTXT", e);
		}
		return ordenesCompraForm;
	}
	
	
	
	private int grabarOrden (PreparedStatement stmInsert, PreparedStatement stmInsertRS, CargaOrdenesForm ordenesCompraForm){
		int totReg = 0;
		try{
			if (ordenesCompraForm.getEstatus().equals("A5")){
				stmInsert.setLong(1, ordenesCompraForm.getNumeroFolioEmpresa());
				stmInsert.setString(2, ordenesCompraForm.getConcepto());
				stmInsert.setString(3, ordenesCompraForm.getTipoMoneda());
				stmInsert.setDouble(4, ordenesCompraForm.getMonto());
				stmInsert.setString(5, ordenesCompraForm.getEstatus());
				stmInsert.setInt(6, ordenesCompraForm.getClaveProveedor());
				stmInsert.setString(7, "0");
				stmInsert.setString(8, ordenesCompraForm.getUsuario());
				stmInsert.setString(9, ordenesCompraForm.getAsignarTO());
				stmInsert.setString(10, ordenesCompraForm.getNumeroRequisicion());
				stmInsert.setString(11, ordenesCompraForm.getUsoCFDI());
				stmInsert.setString(12, ordenesCompraForm.getClaveProdServ());
				stmInsert.setString(13, ordenesCompraForm.getNumeroCuenta());
				stmInsert.setString(14, ordenesCompraForm.getCentroCostosProveedor());
				stmInsert.setString(15, null);
				
				totReg = stmInsert.executeUpdate();
			}else{
				stmInsertRS.setLong(1, ordenesCompraForm.getNumeroFolioEmpresa());
				stmInsertRS.setString(2, ordenesCompraForm.getConcepto());
				stmInsertRS.setString(3, ordenesCompraForm.getTipoMoneda());
				stmInsertRS.setDouble(4, ordenesCompraForm.getMonto());
				stmInsertRS.setString(5, ordenesCompraForm.getEstatus());
				stmInsertRS.setInt(6, ordenesCompraForm.getClaveProveedor());
				stmInsertRS.setString(7, "1");
				stmInsertRS.setString(8, ordenesCompraForm.getUsuario());
				stmInsertRS.setString(9, ordenesCompraForm.getAsignarTO());
				stmInsertRS.setString(10, ordenesCompraForm.getNumeroRequisicion());
				stmInsertRS.setString(11, ordenesCompraForm.getUsoCFDI());
				stmInsertRS.setString(12, ordenesCompraForm.getClaveProdServ());
				stmInsert.setString(13, ordenesCompraForm.getNumeroCuenta());
				stmInsert.setString(14, ordenesCompraForm.getCentroCostosProveedor());
				
				totReg = stmInsertRS.executeUpdate();
			}
		}catch(SQLException sql){
			totReg = sql.getErrorCode();
			Utils.imprimeLog("", sql);
		}catch(Exception e){
			Utils.imprimeLog("Grabando la orden de compra", e);
		}
		return totReg;
	}

	
	private int grabarOrdenAmericana (int claveProveedor, PreparedStatement stmInsert2, CargaOrdenesForm ordenesCompraForm){
		int totReg = 0;
		try{
			stmInsert2.setLong(1, ordenesCompraForm.getNumeroFolioEmpresa());
			stmInsert2.setString(2, ordenesCompraForm.getEstatus());
			stmInsert2.setInt(3, claveProveedor);
			stmInsert2.setString(4, ordenesCompraForm.getTipoMoneda());
			stmInsert2.setDouble(5, ordenesCompraForm.getMonto());
			stmInsert2.setString(6, ordenesCompraForm.getFechaUUID());
			totReg = stmInsert2.executeUpdate();
		}catch(SQLException sql){
			totReg = sql.getErrorCode();
			Utils.imprimeLog("", sql);
		}catch(Exception e){
			Utils.imprimeLog("Grabando la orden de compra", e);
		}
		return totReg;
	}
	
	
	private void eliminaArchivo(String rutaArchivo){
			File file = new File(rutaArchivo);
			logger.info("Eliminando archivo : "+rutaArchivo);
			file.delete();
	}
	
	
	
}

