package com.siarex247.layOut.OrdenesCompra;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.CentroCostos.CentroCostosBean;
import com.siarex247.catalogos.Empleados.EmpleadosBean;
import com.siarex247.utils.ManipularLibros;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;


public class CargasComprasBean extends ManipularLibros{

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
			e.printStackTrace();
		}
		return false;
	}
	
	
	private void procesaArchivo(final String rutaArch,final String nombreArchivo, final String usuarioTrans, final String esquema, final int claveRegistro, final String requisicion){
		try{
			   	Thread a = new Thread(new Runnable() {
						public void run() {
							ManipularLibros librosExcel = new ManipularLibros();
							try {
								Iterator<Row> row = null;
								if (nombreArchivo.toLowerCase().endsWith("xlsx")){
									XSSFSheet hojaPersonal;
									librosExcel.cargarArchivoXLSX(rutaArch);
									hojaPersonal = librosExcel.obtenerHojaXLSX(0);
									row = hojaPersonal.iterator();
								}else{
									HSSFSheet hojaPersonal;
									librosExcel.cargarArchivo(rutaArch);
									hojaPersonal = librosExcel.obtenerHoja(0);
									row = hojaPersonal.rowIterator();
								}
								procesoOrdenes(rutaArch, row, esquema, nombreArchivo, usuarioTrans, claveRegistro, requisicion);
							} catch (IOException e) {
								Utils.imprimeLog("Procesando Archivo", e);
							}
						}
					});
					a.start();
		}catch(Exception e){
			Utils.imprimeLog("Procesando Archivo", e);
			OrdenesCompraBean historialBean = new OrdenesCompraBean();
			ConexionDB connPool = new ConexionDB();
			Connection con = null;
			ResultadoConexion rc = null;
			try{
				rc = connPool.getConnection(esquema);
				con = rc.getCon();
				historialBean.actualizaHistorial(con, esquema, claveRegistro, 0, 3, 0, 0, usuarioTrans);
			}catch(Exception err){
				Utils.imprimeLog("", err);
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
	
	
	
	private void procesoOrdenes(String rutaArch, Iterator<Row> row, String esquema, String nombreArchivo, String usuarioTrans, int claveRegistro, String requisicion){
		boolean bandRow = true;
		Row r = null;
		Iterator<Cell> celda = null;
		PreparedStatement stmInsert = null;
		PreparedStatement stmInsertSR = null;
		PreparedStatement stmRFC = null;
		PreparedStatement stmVendor = null;
		ResultSet rs = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		ResultadoConexion rc = null;
		try{
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			stmInsert 	= con.prepareStatement(OrdenesCompraQuerys.getQueryGrabarOrden(esquema));
			stmInsertSR = con.prepareStatement(OrdenesCompraQuerys.getQueryGrabarOrdenServRecibido(esquema));
			
			stmRFC = con.prepareStatement(OrdenesCompraQuerys.getQueryInfoProveedorRFC(esquema));
			stmVendor = con.prepareStatement(OrdenesCompraQuerys.getQueryInfoProveedorVendor(esquema));
			
			CargaOrdenesForm ordenesCompraForm = null;
			int totRegistros = 0;
			int regOK = 0;
			int regNG = 0;
			int rfcProveedorRS = 0;
			OrdenesCompraBean historialBean = new OrdenesCompraBean();
			EmpleadosBean empleadosBean = new EmpleadosBean();
			HashMap<String, String> mapaEmpleados = empleadosBean.obteneEmpleadosCarga(con, esquema);
			String claveEmpleado = null;
			CentroCostosBean centroCostosBean = new CentroCostosBean();
			HashMap<String, String> mapaCostos = centroCostosBean.obtenerCentros(con, esquema);
			String asignarTO = null;
			while (row.hasNext()) {
					r = (Row)row.next();
					if (r != null && bandRow){ // se elimina el encabezado
						r = (Row)row.next();
						r = (Row)row.next();
						r = (Row)row.next();
						bandRow =  false;
					}
					rfcProveedorRS = 0;
					celda = r.cellIterator();
					ordenesCompraForm = llenaFormaOrdenes(celda, requisicion);
					if (ordenesCompraForm.getNumeroFolioEmpresa() == 0){
						historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, 0, "El folio de la factura no debe ser 0.", N, null);
						regNG++;
					}/*else if ("".equals(ordenesCompraForm.getConcepto())){
						historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "La descripcion de la factura no debe ser vacio.");
						regNG++;
					}*/else if (ordenesCompraForm.getMonto() == 0){
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
							logger.info("stmVendor Vendor : "+stmVendor);
							rs = stmVendor.executeQuery();
						}
						
						if (rs.next()){
							rfcProveedorRS = rs.getInt(5);
						}
						if (rfcProveedorRS == 0){ // si no encontro el proveedor
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
								ordenesCompraForm.setAsignarTO("C_"+ asignarTO);
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
			historialBean.actualizaHistorial(con, esquema, claveRegistro, totRegistros, 0, regOK, regNG, usuarioTrans);
			eliminaArchivo(rutaArch);
		}catch(Exception e){
			 Utils.imprimeLog("Error al procesar el archivo", e);
				OrdenesCompraBean historialBean = new OrdenesCompraBean();
				try{
					
					historialBean.actualizaHistorial(con, esquema, claveRegistro, 0, 3, 0, 0, usuarioTrans);
				}catch(Exception err){
					Utils.imprimeLog("", err);
				}finally{
					try{
						if (rs != null){
							rs.close();
						}
						rs = null;
						
						if (stmInsert != null){
							stmInsert.close();
						}
						stmInsert = null;
						if (stmInsertSR != null){
							stmInsertSR.close();
						}
						stmInsertSR = null;
						
						if (stmRFC != null){
							stmRFC.close();
						}
						stmRFC = null;
						
						if (con != null){
							con.close();
						}
						con = null;
					}catch(Exception sql){
						con = null;
					}
				}
			
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmInsert != null){
					stmInsert.close();
				}
				stmInsert = null;
				if (stmRFC!= null){
					stmRFC.close();
				}
				stmRFC = null;
				if (stmInsertSR != null){
					stmInsertSR.close();
				}
				stmInsertSR = null;
				if (stmVendor != null){
					stmVendor.close();
				}
				stmVendor = null;
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
	
	
	private CargaOrdenesForm llenaFormaOrdenes(Iterator<Cell> celda, String requisicion){
		CargaOrdenesForm ordenesCompraForm = new CargaOrdenesForm();
		Cell cel = null;
		try{
			String folioEmpresa = "";
			ordenesCompraForm.setEstatus("A5");
			ordenesCompraForm.setIdVendor("NO_VALIDAR");
			DecimalFormat df = new DecimalFormat("#######0.##");
			String servRecibido = "";
			double numeroVendor = 0;
			while (celda.hasNext()) {
				  cel =(Cell) celda.next();
					if (cel.getColumnIndex() == 0){ // numero de folio empresa 
						try{
							int pos = 0;
							pos = cel.toString().indexOf("."); 
							if ( pos > -1){
								folioEmpresa = cel.toString().trim().substring(0, pos);
							}else{
								folioEmpresa = cel.toString();
							}
							ordenesCompraForm.setNumeroFolioEmpresa(Long.parseLong(folioEmpresa));
						}catch(Exception e){
							ordenesCompraForm.setNumeroFolioEmpresa(0);
							// e.printStackTrace();
						}
					}else if (cel.getColumnIndex() == 1){ // descripcion
						ordenesCompraForm.setConcepto(cel.toString());
					}else if (cel.getColumnIndex() == 2){ // tipo de moneda
						ordenesCompraForm.setTipoMoneda(cel.toString());
					}else if (cel.getColumnIndex() == 3){ // cantidad
						try{
							ordenesCompraForm.setMonto(Double.parseDouble(cel.toString()));
						}catch(Exception e){
							ordenesCompraForm.setMonto(0);
						}
					}else if (cel.getColumnIndex() == 4){ // RFC
						ordenesCompraForm.setRfc(cel.toString());
					}else if (cel.getColumnIndex() == 5){ // IdVendor
						if ("".equalsIgnoreCase(cel.toString())){
							ordenesCompraForm.setIdVendor("NO_VALIDAR");
						}else{
							try {
								numeroVendor = Utils.noNuloDouble(cel.toString());
								ordenesCompraForm.setIdVendor(df.format(numeroVendor));	
							}catch(Exception e) {
								
							}
							
							
						}
					}else if (cel.getColumnIndex() == 6){ // Requisicion o Empleado
						if ("N".equalsIgnoreCase(requisicion)){
							if ("".equalsIgnoreCase(cel.toString())) {
								ordenesCompraForm.setAsignarTO(cel.toString());	
							}else {
								ordenesCompraForm.setAsignarTO(cel.toString());
							}
							
						}else{
							ordenesCompraForm.setNumeroRequisicion(cel.toString());	
						}
					}else if (cel.getColumnIndex() == 7){ // Empleado
						//ordenesCompraForm.setAsignarTO(cel.toString());
						if ("".equalsIgnoreCase(cel.toString())) {
							ordenesCompraForm.setAsignarTO(cel.toString());	
						}else {
							ordenesCompraForm.setAsignarTO(cel.toString());
						}
					}else if (cel.getColumnIndex() == 8){ // Servicio Recibido
						servRecibido = cel.toString();
						if (servRecibido == null || "".equals(servRecibido)){
							ordenesCompraForm.setEstatus("A5");
						}else if ("1.0".equalsIgnoreCase(servRecibido) || "1".equalsIgnoreCase(servRecibido)){
							ordenesCompraForm.setEstatus("A2");
						}else{
							ordenesCompraForm.setEstatus("A5");
						}
					}else if (cel.getColumnIndex() == 9){ // USO CFDI
						ordenesCompraForm.setUsoCFDI(cel.toString());
					}else if (cel.getColumnIndex() == 10){ // CLAVE DE PRODUCTO SERVICIO
						ordenesCompraForm.setClaveProdServ(cel.toString());
					}
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
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
//				logger.info("Asignar antes...."+ordenesCompraForm.getAsignarTO());
				stmInsert.setString(10, ordenesCompraForm.getNumeroRequisicion());
				stmInsert.setString(11, ordenesCompraForm.getUsoCFDI());
				stmInsert.setString(12, ordenesCompraForm.getClaveProdServ());
				stmInsert.setString(13, ordenesCompraForm.getNumeroCuenta());
				stmInsert.setString(14, ordenesCompraForm.getCentroCostosProveedor());
				stmInsert.setString(15, null);
				totReg = stmInsert.executeUpdate();
			}else{
//insert into ORDENES (FOLIO_EMPRESA,CONCEPTO, TIPO_MONEDA, MONTO, ESTATUS_PAGO, CLAVE_PROVEEDOR, SERVICIO_PRODUCTO, USUARIO, FECHAULTMOV, ID_EMPLEADO, NUMERO_REQUISICION, FECHA_RECIBO,USO_CFDI, CLAVE_PRODUCTO_SERVICIO  ) 
// values (?,?,?,?,?,?,?,?,current_timestamp, ?, ?, current_timestamp, ?, ?)				
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
	
	public void iniciaCarga(String rutaArchivo,  String nombreArchivo, String usuarioTrans, String esquemaEmpresa, int claveRegistro, String requisicion){
		try{
			
			logger.info("nombreArchivo............."+nombreArchivo);
			if (nombreArchivo.endsWith("txt")){
				logger.info("Procesando un archivo TXT.............");
				CargasComprasTXTBean comprasTXT = new CargasComprasTXTBean();
				comprasTXT.procesaArchivoTXT(rutaArchivo, nombreArchivo, usuarioTrans, esquemaEmpresa, claveRegistro, requisicion);
			}else{
				procesaArchivo(rutaArchivo, nombreArchivo, usuarioTrans, esquemaEmpresa, claveRegistro, requisicion);
			}
			
		}catch(Exception e){
            Utils.imprimeLog("Obteniendo detalle del Visor", e);
		}
	}
	
	
	public static void main(String[] args) {
		CargasComprasBean c = new CargasComprasBean();
		try{
			/*
			c.moverArchivo("C:\\Tomcat7\\siarex\\repSAI\\REPOSITORIOS\\CALIMAX\\ARCHIVOS_CARGADOS\\", 
						"20150216.xls", 
						"C:\\Tomcat7\\siarex\\repSAI\\REPOSITORIOS\\CALIMAX\\ORDENES_COMPRA\\");
			*/
			
			c.procesaArchivo("C:\\Tomcat7\\siarex\\repSAI\\REPOSITORIOS\\CALIMAX\\ORDENES_COMPRA\\20150216.xls", 
							"20150216.xls","DSBUMAJTJ","CALIMAX", 0, "S");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void eliminaArchivo(String rutaArchivo){
		File file = new File(rutaArchivo);
		logger.info("Eliminando archivo : "+rutaArchivo);
		file.delete();
}
	
	
}
