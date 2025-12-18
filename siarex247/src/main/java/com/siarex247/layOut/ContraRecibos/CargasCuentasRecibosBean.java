package com.siarex247.layOut.ContraRecibos;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.layOut.OrdenesCompra.CargaOrdenesForm;
import com.siarex247.layOut.OrdenesCompra.OrdenesCompraBean;
import com.siarex247.utils.ManipularLibros;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.validaciones.UtilsValidaciones;

public class CargasCuentasRecibosBean extends ManipularLibros{

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
			   cargarArchivo(rutaArch);
					Thread a = new Thread(new Runnable() {
						HSSFSheet hojaPersonal = obtenerHoja(0);
						public void run() {
							procesoRecibo(rutaArch, hojaPersonal, esquema, nombreArchivo, usuarioTrans, claveRegistro);							
						}
					});
					a.start();
		}catch(Exception e){
			Utils.imprimeLog("procesando archivo ", e);
			eliminaArchivo(rutaArch);
		}
	}
	
	
	
	private void procesoRecibo(String rutaArch, HSSFSheet hoja, String esquema, String nombreArchivo, String usuarioTrans, int claveRegistro){
		boolean bandRow = true;
		Iterator<Row> row = null;
		Row r = null;
		Iterator<Cell> celda = null;
		PreparedStatement stmUpdate = null;
		PreparedStatement stmSelect = null;
		//PreparedStatement stmSelectPro = null;
		ResultSet rs = null;
		//ResultSet rsPro = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		ResultadoConexion rc = null;
		//String rfcProveedor = null;
		String estatusOrden = null;
		String nombreXML = null;
		double totalFactura = 0;
		OrdenesCompraBean historialBean = new OrdenesCompraBean();
		try{
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			
			stmUpdate = con.prepareStatement(ContraRecibosQuerys.getQueryActualizaOrdenRecibo(esquema));
			
			stmSelect = con.prepareStatement(ContraRecibosQuerys.getQueryInfoCuentasPagarRecibo(esquema));
			
			//stmSelectPro = con.prepareStatement(ProveedoresQuerys.getQueryTipoValidacionImporte(esquema));
			
			row = hoja.rowIterator();
			CargaOrdenesForm ordenesCompraForm = null;
			int totRegistros = 0;
			int regOK = 0;
			int regNG = 0;
			//String tipoValidacionImporte = "";
			
			// se calcula la fecha de pago
			HashMap<String, String> mapaConf = ConfigAdicionalesBean.obtenerConfiguraciones(con, esquema);
    		String fechaPago = UtilsValidaciones.obtenerFechaPago(mapaConf);  
    		
			while (row.hasNext()) {
				r = (Row)row.next();
				if (r != null && bandRow){ // se elimina el encabezado
					r = (Row)row.next();
					r = (Row)row.next();
					r = (Row)row.next();
					bandRow =  false;
				}
				celda = r.cellIterator();
				ordenesCompraForm = llenaFormaOrdenes(celda);
				// logger.info("Procesando la orden : "+ordenesCompraForm.getNumeroFolioEmpresa()+" del proveedor : "+ordenesCompraForm.getRfc());
					stmSelect.setLong(1, ordenesCompraForm.getNumeroFolioEmpresa());
					//stmSelect.setString(2, ordenesCompraForm.getRfc());
					
					rs = stmSelect.executeQuery()	;
					if (rs.next()){
						// NOMBRE_XML, ESTATUS_PAGO, RFC, MONTO
						nombreXML = Utils.noNulo(rs.getString(1));
						//rfcProveedor = Utils.noNulo(rs.getString(3));
						estatusOrden = Utils.noNulo(rs.getString(2));
						totalFactura = rs.getDouble(3); 
						
						boolean bandXML = true;
						if ("".equals(nombreXML)){
							bandXML = false;
						}
						if (ordenesCompraForm.getNumeroFolioEmpresa() == 0){
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El folio de la empresa no debe ser igual a 0.", N, null);
							regNG++;
						}else if (ordenesCompraForm.getMonto() == 0 && bandXML){
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El importe de la factura no debe ser igual a 0.", N, null);
							regNG++;
						}/*else if ("".equals(ordenesCompraForm.getRfc())){
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El RFC del proveedor no debe ser vacio.", N);
							regNG++;
						}*/else if (ordenesCompraForm.getTotal() == 0){
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El importe pagado no debe ser igual a 0.", N, null);
							regNG++;
						}else if (totalFactura != ordenesCompraForm.getTotal() && bandXML){
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El importe pagado es diferente al importe de la factura.", N, null);
							regNG++;
						}/*else if (!rfcProveedor.equalsIgnoreCase(ordenesCompraForm.getRfc())){
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "La orden "+ordenesCompraForm.getNumeroFolioEmpresa()+" no le corresponde al RFC asignado.", N);
							regNG++;
							
						}*/else{
							if ("".equals(nombreXML)){
								ordenesCompraForm.setEstatus("A2");
								ordenesCompraForm.setServicioProducto("1");
								ordenesCompraForm.setFechaPago(null);
							}else{
								ordenesCompraForm.setEstatus("A3");
								ordenesCompraForm.setServicioProducto("1");
								ordenesCompraForm.setFechaPago(fechaPago);
							}
							
							if (estatusOrden.equalsIgnoreCase("A5") || estatusOrden.equalsIgnoreCase("A1")){
								actualizaOrden(stmUpdate, ordenesCompraForm, usuarioTrans);
								regOK++;
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "Registro guardado satisfactoriamente.", S, null);
						    }else{
						    	historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "La orden "+ordenesCompraForm.getNumeroFolioEmpresa()+" no se encuentra en estatus correcto.", N, null);
								regNG++;
						    }
						}
					}else{
						historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioSistema(), "El folio "+ordenesCompraForm.getNumeroFolioSistema()+"del sistema no se encontro en la base de datos.", N, null);
						regNG++;
					}
					/*
					if (rsPro != null){
						rsPro.close();
					}
					rsPro = null;
					*/
					if (rs != null){
						rs.close();
					}
					rs = null;
					
				totRegistros++;
			}
			historialBean.actualizaHistorial(con, esquema, claveRegistro, totRegistros, 0, regOK, regNG, usuarioTrans);
			eliminaArchivo(rutaArch);
		}catch(Exception e){
			Utils.imprimeLog("procesando archivo ", e);
			eliminaArchivo(rutaArch);
		}finally{
			try{
				/*
				if (stmSelectPro != null){
					stmSelectPro.close();
				}
				stmSelectPro = null;
				*/
				
				if (stmSelect != null){
					stmSelect.close();
				}
				stmSelect = null;
				if (stmUpdate != null){
					stmUpdate.close();
				}
				stmUpdate = null;
				if (con != null){
					con.close();
				}
				con = null;
			}catch(Exception e){
				stmUpdate = null;
				stmSelect = null;
				//stmSelectPro = null;
				con = null;
			}
		}
	}
	
	
	private CargaOrdenesForm llenaFormaOrdenes(Iterator<Cell> celda){
		CargaOrdenesForm ordenesCompraForm = new CargaOrdenesForm();
		Cell cel = null;
		try{
			//ordenesCompraForm.setEstatus("A3"); // Listo para pago
			String folioEmpresa = "";
			
			while (celda.hasNext()) {
				cel =(Cell) celda.next();
						if (cel.getColumnIndex() == 0){ // Orden del proveedor
							try{
								int pos = 0;
								pos = cel.toString().indexOf("."); 
								if ( pos > -1){
									folioEmpresa = cel.toString().trim().substring(0, pos);
								}
								ordenesCompraForm.setNumeroFolioEmpresa(Long.parseLong(folioEmpresa));
							}catch(Exception e){
								ordenesCompraForm.setNumeroFolioEmpresa(0);
								e.printStackTrace();
							}
						}
						else if (cel.getColumnIndex() == 1){ // Importe de la factura
							try{
								ordenesCompraForm.setMonto(Double.parseDouble(cel.toString()));
							}catch(Exception e){
								ordenesCompraForm.setMonto(0);
							}
								
						}else if (cel.getColumnIndex() == 2){ // RFC
							ordenesCompraForm.setRfc(cel.toString());
						}else if (cel.getColumnIndex() == 6){ // Total o Sub-Total
							try{
								ordenesCompraForm.setTotal(Double.parseDouble(cel.toString()));
							}catch(Exception e){
								ordenesCompraForm.setTotal(0);
							}
					    }
			}
		}catch(Exception e){
			Utils.imprimeLog("al obtener los datos", e);
		}
		return ordenesCompraForm;
	}
	
	
	private int actualizaOrden (PreparedStatement stmUpdate, CargaOrdenesForm ordenesCompraForm, String usuarioTrans){
		int totReg = 0;
		try{
			stmUpdate.setDouble(1, ordenesCompraForm.getTotal());
			stmUpdate.setString(2, ordenesCompraForm.getEstatus());
			stmUpdate.setString(3, ordenesCompraForm.getServicioProducto());
			stmUpdate.setString(4, usuarioTrans);
			stmUpdate.setString(5, ordenesCompraForm.getFechaPago());
			stmUpdate.setLong(6, ordenesCompraForm.getNumeroFolioEmpresa());
			totReg = stmUpdate.executeUpdate();
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return totReg;
	}
	
	
	public void iniciaCarga(String rutaArchivo, String nombreArchivo, String usuarioTrans, String esquemaEmpresa, int claveRegistro){
		try{
			procesaArchivo(rutaArchivo, nombreArchivo,usuarioTrans, esquemaEmpresa, claveRegistro);
		}catch(Exception e){
			Utils.imprimeLog("", e);
			eliminaArchivo(rutaArchivo);
		}
	}
	
	
	
	private void eliminaArchivo(String rutaArchivo){
		File file = new File(rutaArchivo);
		logger.info("Eliminando archivo : "+rutaArchivo);
		file.delete();
	}
}
