package com.siarex247.layOut.EliminarOrdenes;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.layOut.OrdenesCompra.CargaOrdenesForm;
import com.siarex247.layOut.OrdenesCompra.OrdenesCompraBean;
import com.siarex247.utils.ManipularLibros;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;


public class CargasEliminaComprasBean extends ManipularLibros{

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
	
	
	private void procesaArchivo(final String rutaArch,final String nombreArchivo, final String usuarioTrans, final String esquema, final int claveRegistro){
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
								procesoOrdenes(rutaArch, row, esquema, nombreArchivo, usuarioTrans, claveRegistro);
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
	
	
	
	private void procesoOrdenes(String rutaArch, Iterator<Row> row, String esquema, String nombreArchivo, String usuarioTrans, int claveRegistro){
		boolean bandRow = true;
		Row r = null;
		Iterator<Cell> celda = null;
		PreparedStatement stmDelete = null;
		PreparedStatement stmRFC = null;
		ResultSet rs = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		ResultadoConexion rc = null;
		try{
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			stmDelete = con.prepareStatement(EliminarOrdenesQuerys.getEliminarOrdenes(esquema));
			stmRFC = con.prepareStatement(EliminarOrdenesQuerys.getQueryInfoProveedorRFC(esquema));
			CargaOrdenesForm ordenesCompraForm = null;
			int totRegistros = 0;
			int regOK = 0;
			int regNG = 0;
			boolean rfcProveedor = false;
			int claveProveedor = 0;
			OrdenesCompraBean historialBean = new OrdenesCompraBean();
			while (row.hasNext()) {
					r = (Row)row.next();
					if (r != null && bandRow){ // se elimina el encabezado
						r = (Row)row.next();
						r = (Row)row.next();
						r = (Row)row.next();
						bandRow =  false;
					}
					
					celda = r.cellIterator();
					ordenesCompraForm = llenaFormaOrdenes(celda, logger);
					if (ordenesCompraForm.getNumeroFolioEmpresa() == 0){
						historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, 0, "El folio de la factura no debe ser 0.", N, null);
						regNG++;
					}/*else if (ordenesCompraForm.getMonto() == 0){
						historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El monto de la factura no debe ser 0.");
						regNG++;
					}*/else if ("".equals(ordenesCompraForm.getRfc())){
						historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El RFC del proveedor no debe ser vacio.", N, null);
						regNG++;
					}else {
						stmRFC.setString(1, ordenesCompraForm.getRfc());
						rs = stmRFC.executeQuery();
						if (rs.next()){
							rfcProveedor = true;
							claveProveedor = rs.getInt(5);
						}
						logger.info("RFC----->"+rfcProveedor+" dato..."+ordenesCompraForm.getRfc());
						if (rfcProveedor){ // si encontro el proveedor
							int numReg = eliminarOrden(stmDelete, ordenesCompraForm, claveProveedor); // se guarda el registro de la orden
							if (numReg == 1 ){
								regOK++;
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "Registro guardado satisfactoriamente.", S, null);
							}else if (numReg == 0 ){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El folio "+ordenesCompraForm.getNumeroFolioEmpresa()+" asociado al RFC "+ordenesCompraForm.getRfc()+" no existe en la base de datos.", N, null);
								regNG++;
							}else{
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "Error al eliminar el folio "+ordenesCompraForm.getNumeroFolioEmpresa() +" con Error "+numReg, N, null);
								regNG++;
							}
						}else{
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El RFC del proveedor no existe en la base de datos.", N, null);
							regNG++;
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
					logger.info("Actualizando el historico_1 : "+e);
					 e.printStackTrace();
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
			
		}finally{
			try{
				if (stmDelete != null){
					stmDelete.close();
				}
				stmDelete = null;
				if (con != null){
					con.close();
				}
				con = null;
			}catch(Exception e){
				stmDelete = null;
				con = null;
			}
		}
	}
	
	
	private CargaOrdenesForm llenaFormaOrdenes(Iterator<Cell> celda, Logger logger){
		CargaOrdenesForm ordenesCompraForm = new CargaOrdenesForm();
		Cell cel = null;
		try{
			String folioEmpresa = "";
			ordenesCompraForm.setEstatus("A5");
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
							e.printStackTrace();
						}
					}else if (cel.getColumnIndex() == 1){ // RFC
						ordenesCompraForm.setRfc(cel.toString());
					}/*else if (cel.getColumnIndex() == 2){ // cantidad
						try{
							ordenesCompraForm.setMonto(Double.parseDouble(cel.toString()));
						}catch(Exception e){
							ordenesCompraForm.setMonto(0);
						}
					}*/
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ordenesCompraForm;
	}
	
	private int eliminarOrden (PreparedStatement stmDelete, CargaOrdenesForm ordenesCompraForm, int claveProveedor){
		int totReg = 0;
		try{
			
			stmDelete.setLong(1, ordenesCompraForm.getNumeroFolioEmpresa());
			stmDelete.setInt(2, claveProveedor);
			logger.info("stmDelete....."+stmDelete);
			totReg = stmDelete.executeUpdate();
			
		}catch(SQLException sql){
			totReg = sql.getErrorCode();
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return totReg;
	}
	
	public void iniciaCarga(String rutaArchivo, String nombreArchivo, String usuarioTrans, String esquemaEmpresa, int claveRegistro){
		try{
			/*
			moverArchivo("C:\\Tomcat7\\siarex\\repSAI\\REPOSITORIOS\\CALIMAX\\ARCHIVOS_CARGADOS\\",
					    "20150303.xls",
						"C:\\Tomcat7\\siarex\\repSAI\\REPOSITORIOS\\CALIMAX\\ORDENES_COMPRA\\");
			*/
			if (nombreArchivo.endsWith("txt")){
				logger.info("Procesando un archivo TXT.............");
				//CargasComprasTXTBean comprasTXT = new CargasComprasTXTBean();
				//comprasTXT.procesaArchivoTXT(rutaArchivo, nombreArchivo, usuarioTrans, esquemaEmpresa, claveRegistro);
			}else{
				procesaArchivo(rutaArchivo, nombreArchivo, usuarioTrans, esquemaEmpresa, claveRegistro);
			}
			
		}catch(Exception e){
            Utils.imprimeLog("Obteniendo detalle del Visor", e);
		}
	}
	
	
	public static void main(String[] args) {
		CargasEliminaComprasBean c = new CargasEliminaComprasBean();
		try{
			/*
			c.moverArchivo("C:\\Tomcat7\\siarex\\repSAI\\REPOSITORIOS\\CALIMAX\\ARCHIVOS_CARGADOS\\", 
						"20150216.xls", 
						"C:\\Tomcat7\\siarex\\repSAI\\REPOSITORIOS\\CALIMAX\\ORDENES_COMPRA\\");
			*/
			
			c.procesaArchivo("C:\\Tomcat7\\siarex\\repSAI\\REPOSITORIOS\\CALIMAX\\ORDENES_COMPRA\\20150216.xls", 
							"20150216.xls","DSBUMAJTJ","CALIMAX", 0);
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
