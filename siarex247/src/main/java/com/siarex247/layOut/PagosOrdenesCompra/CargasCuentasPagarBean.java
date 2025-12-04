package com.siarex247.layOut.PagosOrdenesCompra;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.CorreosRespaldo.CorreosRespaldoBean;
import com.siarex247.layOut.OrdenesCompra.CargaOrdenesForm;
import com.siarex247.layOut.OrdenesCompra.OrdenesCompraBean;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.ManipularLibros;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsBD;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;

public class CargasCuentasPagarBean extends ManipularLibros{

	public static final Logger logger = Logger.getLogger("siarex");
	private final String N = "N";
	private final String S = "S";
	private final String coma = ";";

	
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
			Utils.imprimeLog("", e);
		}
		return false;
	}


	private void procesaArchivo(final String rutaArch, final String usuarioTrans, final String esquema, final int claveRegistro,
			final String emailEmpresa, final String pwdCorreo, final String nombreLargo, final String fechaPago){
		try{
			   cargarArchivo(rutaArch);
					Thread a = new Thread(new Runnable() {
						HSSFSheet hojaPersonal = obtenerHoja(0);
						public void run() {
							procesoPagos(rutaArch, hojaPersonal, esquema, usuarioTrans, claveRegistro, emailEmpresa, pwdCorreo, nombreLargo, fechaPago);
						}
					});
					a.start();

		}catch(Exception e){
			Utils.imprimeLog("al obtener archivo ", e);
			eliminaArchivo(rutaArch);
		}
	}


	

	private void procesoPagos(String rutaArch, HSSFSheet hoja, String esquema, String usuarioTrans, int claveRegistro, String emailEmpresa,  String pwdCorreo, String nombreLargo,String fechaPago){
		boolean bandRow = true;
		Iterator<Row> row = null;
		Row r = null;
		Iterator<Cell> celda = null;
		PreparedStatement stmUpdate = null;
		PreparedStatement stmSelect = null;
		ResultSet rs = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		ResultadoConexion rc = null;
		int claveProveedor = 0;
		String estatusOrden = null;
		String serieFolio = null;
		String uuid = null;
		String tipoMoneda = null;
		String HoraPago = null;
		StringBuffer sbDatos = new StringBuffer();
		StringBuffer sbDatosMul = new StringBuffer();
		String [] listaCorreos = null;
		HashMap<Integer, String> proveedoresArchivo = new HashMap<Integer, String>();
		HashMap<String, String> ordenesMultiple = new HashMap<String, String>();
		
		double totalFactura = 0;
		OrdenesCompraBean historialBean = new OrdenesCompraBean();
		CargaOrdenesForm ordenesCompraForm = null;
		ArrayList<Integer> llavesRFC = new ArrayList<Integer>();
		
		int totRegistros = 0;
		int regOK = 0;
		int regNG = 0;
		String tipoOrden = "";
		String idTipo = "";
		try{
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			stmUpdate = con.prepareStatement(PagosOrdenesCompraQuerys.getQueryActualizaOrdenPagada(esquema));
			stmSelect = con.prepareStatement(PagosOrdenesCompraQuerys.getQueryInfoCuentasPagar(esquema));
			//yyyy-MM-dd
			SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm:ss");
			// SimpleDateFormat formatDia = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
			Date HoraActual = new Date();
			HoraPago = formatDate.format(HoraActual);
			
			// se agrega la fecha de pago
			
			row = hoja.rowIterator();
			boolean bandMultiple = false;
			StringBuffer fechaPagoFinal = new StringBuffer(fechaPago).append(" ");
			fechaPagoFinal.append(HoraPago);
			
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
						stmSelect.setLong(1, ordenesCompraForm.getNumeroFolioEmpresa());
						rs = stmSelect.executeQuery();
						if (rs.next()){
							claveProveedor = rs.getInt(2);
							estatusOrden = Utils.noNulo(rs.getString(1));
							serieFolio   = Utils.noNulo(rs.getString(4));
							totalFactura = rs.getDouble(3);
							uuid 		 = Utils.noNulo(rs.getString(5));
							tipoMoneda   = Utils.noNulo(rs.getString(6));
							tipoOrden    = Utils.noNulo(rs.getString(7));
							idTipo       = Utils.noNulo(rs.getString(7));
							if (tipoOrden.length() > 0 && tipoOrden.substring(0,1).equals("M") ){
								bandMultiple = true;
								if (ordenesMultiple.containsKey(idTipo)){
									sbDatosMul.append(ordenesMultiple.get(idTipo));
									sbDatosMul.append("&");
									sbDatosMul.append(idTipo).append(coma)
											  .append(ordenesCompraForm.getNumeroFolioEmpresa()).append(coma)
											  .append(ordenesCompraForm.getMonto()).append(coma)
											  .append(ordenesCompraForm.getRfc()).append(coma)
											  .append(estatusOrden).append(coma)
											  .append(ordenesCompraForm.getTotal()).append(coma);
									ordenesMultiple.put(idTipo, sbDatosMul.toString());
									// logger.info("sbDatosMul arriba ===>"+sbDatosMul.toString());
									sbDatosMul.setLength(0);
									
								}else{
									sbDatosMul.append(idTipo).append(coma)
											  .append(ordenesCompraForm.getNumeroFolioEmpresa()).append(coma)
									          .append(ordenesCompraForm.getMonto()).append(coma)
									          .append(ordenesCompraForm.getRfc()).append(coma)
									          .append(estatusOrden).append(coma)
									          .append(ordenesCompraForm.getTotal()).append(coma);
									ordenesMultiple.put(idTipo, sbDatosMul.toString());
									logger.info("sbDatosMul abajo ===>"+sbDatosMul.toString());
									sbDatosMul.setLength(0);
								}
								
							}else if (ordenesCompraForm.getNumeroFolioEmpresa() == 0){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El folio de la empresa no debe ser igual a 0.", N, null);
								regNG++;
							}else if (ordenesCompraForm.getMonto() == 0){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El importe de la factura no debe ser igual a 0.", N, null);
								regNG++;
							}else if ("".equals(ordenesCompraForm.getRfc())){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El RFC del proveedor no debe ser vacio.", N, null);
								regNG++;
							}else if (!estatusOrden.equalsIgnoreCase("A3")){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "La orden "+ordenesCompraForm.getNumeroFolioEmpresa()+" no se encuentra en estatus correcto.", N, null);
								regNG++;
							}else if (ordenesCompraForm.getTotal() == 0){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El importe pagado no debe ser igual a 0.", N, null);
								regNG++;
							}else if (totalFactura != ordenesCompraForm.getTotal()){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "El importe pagado es diferente al importe de la factura.", N, null);
								regNG++;
							}else{
								actualizaOrden(stmUpdate, ordenesCompraForm, usuarioTrans, fechaPagoFinal.toString());
								regOK++;
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioEmpresa(), "Registro guardado satisfactoriamente.", S, null);
									
									if(proveedoresArchivo.containsKey(claveProveedor)){
										sbDatos.append(proveedoresArchivo.get(claveProveedor));
										sbDatos.append("&")
											   .append(serieFolio).append(coma)
									           .append(uuid).append(coma)
									           .append(totalFactura).append(coma)
									           .append(tipoMoneda).append(coma)
									           .append(ordenesCompraForm.getNumeroFolioEmpresa()).append(coma)
									           .append("N").append(coma);
										
										proveedoresArchivo.put(claveProveedor, sbDatos.toString());
										sbDatos.setLength(0);
									}
									else{
										sbDatos.append(serieFolio).append(coma)
										       .append(uuid).append(coma)
										       .append(totalFactura).append(coma)
										       .append(tipoMoneda).append(coma)
										       .append(ordenesCompraForm.getNumeroFolioEmpresa()).append(coma)
										       .append("N").append(coma);
										       
										proveedoresArchivo.put(claveProveedor, sbDatos.toString());
										sbDatos.setLength(0);
										llavesRFC.add(claveProveedor);
									}
							    }
						}else{
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, ordenesCompraForm.getNumeroFolioSistema(), "El folio "+ordenesCompraForm.getNumeroFolioSistema()+" del sistema no se encontro en la base de datos.", N, null);
							regNG++;
						}
						if (rs != null){
							rs.close();
						}
						rs = null;
					totRegistros++;
			}
			HashMap<String, Integer> mapaRes = null;
			if (bandMultiple){ // hay ordenes multiples
				mapaRes = validaMultiples(con, esquema, ordenesMultiple, regNG, regOK, claveRegistro, usuarioTrans, fechaPagoFinal.toString(), proveedoresArchivo, llavesRFC);
				regOK = mapaRes.get("REG_OK");
				regNG = mapaRes.get("REG_NG");
			}
			
			
			logger.info("proveedoresArchivo---->"+proveedoresArchivo);
			logger.info("llavesRFC---->"+llavesRFC);
			logger.info("regOK---->"+regOK);
			logger.info("regNG---->"+regNG);
			
			
			Integer rfcLlave = 0;
			String sbHTML = null;
			String razonSocial = null;
			String subJect = "";
			String [] cadProv = null;
			HashMap<Integer, String> mapaRazon = new HashMap<Integer, String>();
			if (!proveedoresArchivo.isEmpty()){
		         //CorreoBean.passwordEmisorMensaje = pwdCorreo;
		         //CorreoBean.usuarioEmisorMensaje = emailEmpresa;
				String cadenaProveedor = null;
				for (int y = 0; y < llavesRFC.size(); y++){
					rfcLlave = llavesRFC.get(y);
					cadenaProveedor = proveedoresArchivo.get(rfcLlave);
					razonSocial = UtilsBD.buscarRazonProveedor(con, esquema, rfcLlave);
					mapaRazon.put(rfcLlave, razonSocial);
					sbHTML = UtilsHTML.generaHTML(cadenaProveedor, nombreLargo, razonSocial, fechaPagoFinal.toString());
//					logger.info("sbHTML------>"+Utils.regresaCaracteresHTML(sbHTML));
					cadProv = cadenaProveedor.split("&");
					if (cadProv.length == 1){
						subJect = "SIAREX - Factura Pagada por "+nombreLargo;
					}else{
						subJect = "SIAREX - Facturas Pagadas por "+nombreLargo;
					}
					listaCorreos = UtilsBD.obtenerCorreorProveedor(con, esquema, rfcLlave, "S", "N", "N","N");
					EnviaCorreoGrid.enviarCorreo(null, sbHTML, false, listaCorreos, null,  subJect, emailEmpresa, pwdCorreo);	
					
		       }
			}
			
			
			
			CorreosRespaldoBean correosRespaldoBean = new CorreosRespaldoBean();
			
			sbDatos.setLength(0);
			HashMap<String, String> mapaOrdenes = correosRespaldoBean.obtenerConfiguraciones(con, esquema);
			
			if (mapaOrdenes.get("CORREO_PAGOS") == null){
				mapaOrdenes.put("CORREO_PAGOS", "N");
			}
			
			
			// termina
			logger.info("********** actualizando historirial ********");
			historialBean.actualizaHistorial(con, esquema, claveRegistro, totRegistros, 0, regOK, regNG, usuarioTrans);
			eliminaArchivo(rutaArch);
		}catch(Exception e){
			Utils.imprimeLog("procesando archivo ", e);
			eliminaArchivo(rutaArch);
		}finally{
			try{
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
				con = null;
			}
		}
	}

	
	
	private HashMap<String, Integer> validaMultiples(Connection con, String esquema, HashMap<String, String> ordenesMultiple, 
			int regNG, int regOK, int claveRegistro, String usuarioHTTP, String fechaPago, 
			HashMap<Integer, String> proveedoresArchivo, ArrayList<Integer> llavesRFC){
		
		PreparedStatement stmt = null;
		PreparedStatement stmtTotal = null;
		ResultSet rs = null;
		ResultSet rsTotal = null;
		OrdenesCompraBean historialBean = new OrdenesCompraBean();
		PreparedStatement stmMultiple = null;
		StringBuffer sbDatos = new StringBuffer();
		HashMap<String, Integer> mapaRes = new HashMap<String, Integer>();
		try{
			mapaRes.put("REG_OK", regOK);
			mapaRes.put("REG_NG", regNG);
			
			StringBuffer sbClavesOrden = new StringBuffer();
			 Collection<String> valMapa =  ordenesMultiple.values();
			 Iterator<String> iteMapa = valMapa.iterator();
			 while(iteMapa.hasNext()){
				 sbClavesOrden.append(iteMapa.next()).append(",");
			 }
			
			 stmt = con.prepareStatement(PagosOrdenesCompraQuerys.getInfoCuentasPagarMultiple(esquema));
			 stmMultiple = con.prepareStatement(PagosOrdenesCompraQuerys.getQueryActualizaOrdenPagadaMultiple(esquema));
			 stmtTotal = con.prepareStatement(PagosOrdenesCompraQuerys.getTotalOrdenesMultiples(esquema));
			 
			 String arrOrden [] = sbClavesOrden.toString().split(",");
			 String arrOrdenMul [] = null;
			 String regOrdenMul [] = null;
			 String tipoOrden = null;
			 long folioExcel = 0;
			 String montoExcel = null;
			 String estatusOrden = null;
			 double totalExcel = 0.0;
			 boolean bandMultiple = false;
			 double totalRS = 0.0;
			 int claveProveedor = 0;
			 int totalOrdenesRS = 0;
			
			 
			 String serieFolio = null;
			 String uuid = null;
			 String tipoMoneda = null;
			 ArrayList<Long> arrOrdenes = new ArrayList<Long>();
			 for (int x = 0; x < arrOrden.length; x++ ){ // este contiene las llaves
				 arrOrdenMul = arrOrden[x].split("&");
				 int totalOrdenesExcel = 0;
				 bandMultiple = false;
				 
				 for (int y = 0; y < arrOrdenMul.length; y++ ){ // por cada tipo de orden multiple diferente
					 regOrdenMul = arrOrdenMul[y].split(coma);
					 /*
					 logger.info("*********************************************************");
					 logger.info("Tipo Orden---->"+regOrdenMul[0]);
					 logger.info("Folio---->"+regOrdenMul[1]);
					 logger.info("Monto---->"+regOrdenMul[2]);
					 logger.info("RFC---->"+regOrdenMul[3]);
					 logger.info("Estatus---->"+regOrdenMul[4]);
					 logger.info("Total---->"+regOrdenMul[5]);
					 */
					 
					 tipoOrden = regOrdenMul[0];
					 folioExcel = Long.parseLong( regOrdenMul[1] );
					 montoExcel = regOrdenMul[2];
//					 rfcExcel = regOrdenMul[3];
					 estatusOrden = regOrdenMul[4];
					 totalExcel = Double.parseDouble( regOrdenMul[5]);
					 
					 stmt.setString(1, tipoOrden); // tipo de orden
					 stmtTotal.setString(1, tipoOrden); // tipo de orden
					 
					 rs = stmt.executeQuery();
					 if (rs.next()){
							claveProveedor = rs.getInt(2);
							serieFolio   = Utils.noNulo(rs.getString(4));
							totalRS      = rs.getDouble(3);
							uuid 		 = Utils.noNulo(rs.getString(5));
							tipoMoneda   = Utils.noNulo(rs.getString(6));
							totalOrdenesExcel++;
							arrOrdenes.add(folioExcel);
						 	if (folioExcel == 0 ){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, folioExcel, "El folio multiple de la empresa no debe ser igual a 0.", N, null);
								regNG++;
								bandMultiple = true;
							}else if (montoExcel.equals("0")){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, folioExcel, "El importe multiple de la factura no debe ser igual a 0.", N, null);
								regNG++;
								bandMultiple = true;
							}/*else if ("".equals(rfcExcel)){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, folioExcel, "El RFC multiple del proveedor no debe ser vacio.", N);
								regNG++;
								bandMultiple = true;
							}*/else if (!estatusOrden.equalsIgnoreCase("A3")){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, folioExcel, "La orden multiple "+folioExcel+" no se encuentra en estatus correcto.", N, null);
								regNG++;
								bandMultiple = true;
							}else if (totalExcel == 0){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, folioExcel, "El importe multiple pagado no debe ser igual a 0.", N, null);
								regNG++;
								bandMultiple = true;
							}else if (totalRS != totalExcel){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, folioExcel, "El importe multiple pagado es diferente al importe de la factura.", N, null);
								regNG++;
								bandMultiple = true;
							}/*else if (!rfcProveedor.equalsIgnoreCase(rfcExcel)){
								historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, folioExcel, "La orden multiple "+folioExcel+" no le corresponde al RFC asignado.", N);
								regNG++;
								bandMultiple = true;
							}*/
						 	rs.close();
						 	
					 }else{
						 bandMultiple = true;
						 break;
					 }
					 
					 /*
					 if (bandMultiple){
						 break;
					 }
					 */
					 
				 }
				 
				 if (!bandMultiple){ // fue exitoso
					 rsTotal = stmtTotal.executeQuery();
					 
					 if (rsTotal.next()){
						 totalOrdenesRS = rsTotal.getInt(1);
					 }
					 rsTotal.close();
					 
					 
					 logger.info("totalOrdenesRS---->"+totalOrdenesRS);
					 logger.info("totalOrdenesExcel---->"+totalOrdenesExcel);
					 if (totalOrdenesRS == totalOrdenesExcel){
						 actualizaOrdenMultiple(con, esquema, stmMultiple, usuarioHTTP, fechaPago, tipoOrden);
						 regOK++;
						 mapaRes.put("REG_OK", regOK);	
						 historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, folioExcel, "Orden Multiple guardada satisfactoriamente.", S, null);
							for (int y = 0; y < arrOrdenes.size(); y++){
								if(proveedoresArchivo.containsKey(claveProveedor)){
									sbDatos.append(proveedoresArchivo.get(claveProveedor));
									sbDatos.append("&")
										   .append(serieFolio).append(coma)
								           .append(uuid).append(coma)
								           .append(totalRS).append(coma)
								           .append(tipoMoneda).append(coma)
								           .append(arrOrdenes.get(y)).append(coma)
										   .append(tipoOrden).append(coma);
									proveedoresArchivo.put(claveProveedor, sbDatos.toString());
									sbDatos.setLength(0);
								}
								else{
									sbDatos.append(serieFolio).append(coma)
									       .append(uuid).append(coma)
									       .append(totalRS).append(coma)
									       .append(tipoMoneda).append(coma)
									       .append(arrOrdenes.get(y)).append(coma)
									       .append(tipoOrden).append(coma);
									       
									proveedoresArchivo.put(claveProveedor, sbDatos.toString());
									sbDatos.setLength(0);
									llavesRFC.add(claveProveedor);
								}
							}
					 }else{
							historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, folioExcel, "La cantidad de ordenes multiples del archivo no coincide con las del sistema", N, null);
							regNG++;
							mapaRes.put("REG_OK", regNG);
					 }
				 }
				 
				 arrOrdenes.clear();
			 }
			
			 mapaRes.put("REG_OK", regOK);
			mapaRes.put("REG_NG", regNG);
				
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (rsTotal != null){
					rsTotal.close();
				}
				rsTotal = null;
				
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				if (stmtTotal != null){
					stmtTotal.close();
				}
				stmtTotal = null;
				
				if (stmMultiple != null){
					stmMultiple.close();
				}
				stmMultiple = null;
			}catch(Exception e){
				rs = null;
				stmMultiple = null;
				stmt = null;
			}
		}
		return mapaRes;
	}
	
	
	private ArrayList<CargaOrdenesForm> consultarOrdenes(Connection con, String esquema, String fechaPago){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		CargaOrdenesForm ordenesForm = new CargaOrdenesForm();
		ArrayList<CargaOrdenesForm> datos = new ArrayList<CargaOrdenesForm>();
		try{
			stmt = con.prepareStatement(PagosOrdenesCompraQuerys.getEnvioOrdenesUsuario(esquema));
			stmt.setString(1, fechaPago);
			stmt.setInt(2, 0);
			stmt.setString(3, "1");
			rs = stmt.executeQuery();
			while (rs.next()){
				ordenesForm.setNumeroFolioEmpresa(rs.getLong(1));
				ordenesForm.setTipoMoneda(Utils.noNulo(rs.getString(2)));
				ordenesForm.setSerie(Utils.noNulo(rs.getString(3)));
				ordenesForm.setTotal(rs.getDouble(4));
				ordenesForm.setSubTotal(rs.getDouble(5));
				ordenesForm.setIva(rs.getDouble(6));
				ordenesForm.setUuid(Utils.noNulo(rs.getString(7)));
				ordenesForm.setAsignarTO(Utils.noNulo(rs.getString(8)));
				ordenesForm.setClaveProveedor(rs.getInt(9));
				ordenesForm.setTipoOrden(Utils.noNulo(rs.getString(10)));
				datos.add(ordenesForm);
				ordenesForm = new CargaOrdenesForm();
				
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
			}
		}
		return datos;
	}
	

	private CargaOrdenesForm llenaFormaOrdenes(Iterator<Cell> celda){
		CargaOrdenesForm ordenesCompraForm = new CargaOrdenesForm();
		Cell cel = null;
		try{
			ordenesCompraForm.setEstatus("A4"); // Listo para pago
			String folioEmpresa = "";
			while (celda.hasNext()) {
				  cel =(Cell) celda.next();
					if (cel.getColumnIndex() == 0){ // Folio de la empresa
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
					}else if (cel.getColumnIndex() == 6){ // Importe pagado
						try{
							ordenesCompraForm.setTotal(Double.parseDouble(cel.toString()));
						}catch(Exception e){
							ordenesCompraForm.setTotal(0);
						}
				    }
			}
		}catch(Exception e){
			Utils.imprimeLog("llenando la forma ", e);
		}
		return ordenesCompraForm;
	}


	

	private int actualizaOrden (PreparedStatement stmUpdate, CargaOrdenesForm ordenesCompraForm, String usuarioHTTP, String fechaPago){
		int totReg = 0;
		try{
			stmUpdate.setString(1, ordenesCompraForm.getEstatus());
			stmUpdate.setString(2, usuarioHTTP);
			stmUpdate.setString(3, fechaPago);
			stmUpdate.setString(4, fechaPago);
			stmUpdate.setLong(5, ordenesCompraForm.getNumeroFolioEmpresa());
			totReg = stmUpdate.executeUpdate();
		}catch(Exception e){
			Utils.imprimeLog("actualizando orden", e);
		}
		return totReg;
	}

	
	private int actualizaOrdenMultiple (Connection con, String esquema, PreparedStatement stmMultiple, String usuarioHTTP, String fechaPago, String idMultiple){
		int totReg = 0;
		try{
			stmMultiple.setString(1, "A4");
			stmMultiple.setString(2, usuarioHTTP);
			stmMultiple.setString(3, fechaPago);
			stmMultiple.setString(4, fechaPago);
			stmMultiple.setString(5, idMultiple);
			totReg = stmMultiple.executeUpdate();
		}catch(Exception e){
			Utils.imprimeLog("actualizando orden", e);
		}
		return totReg;
	}
	


	public void iniciaCarga(String rutaArchivo, String usuarioTrans, String esquemaEmpresa, int claveRegistro, String emailEmpresa, String pwdEmpresa, String nombreLargo, String fechaPago){
		try{

			procesaArchivo(rutaArchivo,usuarioTrans,esquemaEmpresa, claveRegistro, emailEmpresa, pwdEmpresa, nombreLargo, fechaPago);
		}catch(Exception e){
			Utils.imprimeLog("iniciando carga de cuentas por pagar ", e);
			eliminaArchivo(rutaArchivo);
		}
	}


	private void eliminaArchivo(String rutaArchivo){
		File file = new File(rutaArchivo);
		logger.info("Eliminando archivo : "+rutaArchivo);
		file.delete();
	}

 }
