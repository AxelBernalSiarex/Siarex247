package com.siarex247.layOut.ImportarProveedores;

import java.sql.Connection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.layOut.OrdenesCompra.OrdenesCompraBean;
import com.siarex247.utils.ManipularLibros;
import com.siarex247.utils.Utils;

public class ImportarProveedoresBean extends ManipularLibros {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	private final String N = "N";
	private final String S = "S";

	
	private void procesaArchivo(String rutaArch,final String usuarioTrans, final String esquema, final int claveEmpresa, final int claveRegistro){
		try{
			   cargarArchivo(rutaArch);
					Thread a = new Thread(new Runnable() {
						HSSFSheet hojaPersonal = obtenerHoja(0);
						public void run() {
							procesoImport(hojaPersonal, esquema, usuarioTrans, claveEmpresa, claveRegistro);
						}
					});
					a.start();
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
	
	
	private void procesoImport(HSSFSheet hoja,String esquema, String usuarioTrans, int claveEmpresa, int claveRegistro){
		boolean bandRow = true;
		Iterator<Row> row = null;
		Row r = null;
		Iterator<Cell> celda = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		ResultadoConexion rc = null;
		
		OrdenesCompraBean historialBean = new OrdenesCompraBean();
		ProveedoresBean proveBean = new ProveedoresBean();
		try{
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
		
			row = hoja.rowIterator();
			ProveedoresForm proveForm = null;
			int totRegistros = 1;
			int regOK = 0;
			int regNG = 0;
			int claveProveedor = 0;
			String mensajeValida = "";
			while (row.hasNext()) {
				r = (Row)row.next();
				if (r != null && bandRow){ // se elimina el encabezado
					r = (Row)row.next();
					r = (Row)row.next();
					r = (Row)row.next();
					bandRow =  false;
				}
				celda = r.cellIterator();
				proveForm = llenaFormaOrdenes(celda);
				mensajeValida = validaCampos(proveForm);
				if ("OK".equals(mensajeValida)){
					if (proveForm.getAnexo24().equalsIgnoreCase("S")){
						proveForm.setAnexo24("1");
					}else{
						proveForm.setAnexo24("0");
					}
					proveForm.setServEsp("N");
					claveProveedor = proveBean.altaProveedores(con, esquema, proveForm);
					if (claveProveedor > 0 && claveProveedor != 1062){
						regOK++;
						historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, totRegistros, "Registro guardado satisfactoriamente.", S, null);
					}else if (claveProveedor == -1062 || claveProveedor == 1062){
						historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, totRegistros, "El RFC proporcionado en el registro "+totRegistros+" ya existe en la base de datos.", N, null);
						regNG++;
					}else{
						historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, totRegistros, "El registro "+totRegistros+" no se puedo grabar en la base de datos.", N, null);
						regNG++;
					}
				}else{
					historialBean.grabarHistorialDetallada(con, esquema, claveRegistro, totRegistros, mensajeValida, N, null);
					regNG++;
				}
				totRegistros++;
			}
			historialBean.actualizaHistorial(con, esquema, claveRegistro, totRegistros, 0, regOK, regNG, usuarioTrans);
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (con != null){
					con.close();
				}
				con = null;
			}catch(Exception e){
				con = null;
			}
		}
	}
	
	public String validaCampos(ProveedoresForm proveForm){
		String mensajeValida = "";
		try{
			if ("".equals(proveForm.getRazonSocial())){
				mensajeValida = "El campo razon social debe contener un valor";
			}else if ("".equals(proveForm.getRfc())){
				mensajeValida = "El campo RFC debe contener un valor";
			}else if ("".equals(proveForm.getCalle())){
				mensajeValida = "El campo calle debe contener un valor";
			}else if ("".equals(proveForm.getDelegacion())){
				mensajeValida = "El campo delegaci�n debe contener un valor";
			}else if ("".equals(proveForm.getEmail())){
				mensajeValida = "El campo Email debe contener un valor";
			}else if ("".equals(proveForm.getNumeroInt())){
				mensajeValida = "El campo numero interior debe contener un valor";
			}else if ("".equals(proveForm.getColonia())){
				mensajeValida = "El campo colonia debe contener un valor";
			}else if ("".equals(proveForm.getCodigoPostal())){
				mensajeValida = "El campo codigo postal debe contener un valor";
			}else if ("".equals(proveForm.getCiudad())){
				mensajeValida = "El campo ciudad debe contener un valor";
			}else if ("".equals(proveForm.getNombreContacto())){
				mensajeValida = "El campo nombre del contacto debe contener un valor";
			}else if ("".equals(proveForm.getEstado())){
				mensajeValida = "El campo estado debe contener un valor";
			}else if ("".equals(proveForm.getTelefono())){
				mensajeValida = "El campo telefono debe contener un valor";
			}else if ("".equals(proveForm.getTipoProveedor())){
				mensajeValida = "El campo tipo de proveedor debe contener un valor";
			}else if ("".equals(proveForm.getAnexo24())){
				mensajeValida = "El Anexo 24 debe contener un valor";
			}else if ("".equals(proveForm.getTipoConfirmacion())){
				mensajeValida = "El campo tipo de confirmacion debe contener un valor";
			}else if (!"USA".equals(proveForm.getTipoProveedor()) && !"MEX".equals(proveForm.getTipoProveedor())){
				mensajeValida = "El tipo de proveedor solo acepta MEX / USA";
			}else{
				if ("S".equalsIgnoreCase(proveForm.getAnexo24())){
					mensajeValida = "OK";
				}else if ("N".equalsIgnoreCase(proveForm.getAnexo24())){
					mensajeValida = "OK";
				}else{
					mensajeValida = "El Anexo 24 solo debe contener el valor S / N";	
				}
				
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return mensajeValida;
	}
	
	private ProveedoresForm llenaFormaOrdenes(Iterator<Cell> celda){
		ProveedoresForm proveForm = new ProveedoresForm();
		Cell cel = null;
		try{
			//ordenesCompraForm.setEstatus("A3"); // Listo para pago
			//String folioEmpresa = "";
			String serie = "";
			String linea = "";
			while (celda.hasNext()) {
				cel =(Cell) celda.next();
						if (cel.getColumnIndex() == 0){ // Id Proveedor
							int pos = cel.toString().indexOf("."); 
							if (pos > 0){
								proveForm.setIdProveedor(cel.toString().substring(0, pos));
							}else{
								proveForm.setIdProveedor(cel.toString());	
							}
						}else if (cel.getColumnIndex() == 1){ // Razon Social
							try{
								//folioEmpresa = cel.toString().trim().substring(0, pos);
								proveForm.setRazonSocial(cel.toString());
							}catch(Exception e){
								proveForm.setRazonSocial("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 2){ // rfc
							try{
								proveForm.setRfc(cel.toString());
							}catch(Exception e){
								proveForm.setRfc("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 3){ // calle
							try{
								proveForm.setCalle(cel.toString());
							}catch(Exception e){
								proveForm.setCalle("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 4){ // numero interior
							try{
								int pos = cel.toString().indexOf("."); 
								if (pos > 0){
									proveForm.setNumeroInt(cel.toString().substring(0, pos));
								}else{
									proveForm.setNumeroInt(cel.toString());	
								}
								
							}catch(Exception e){
								proveForm.setNumeroInt("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 5){ // colonia
							try{
								proveForm.setColonia(cel.toString());
							}catch(Exception e){
								proveForm.setColonia("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 6){ // codigo postal
							try{
								int pos = cel.toString().indexOf("."); 
								if (pos > 0){
									proveForm.setCodigoPostal(Integer.parseInt(cel.toString().substring(0,pos)));	
								}else{
									proveForm.setCodigoPostal(Integer.parseInt(cel.toString()));
								}
								
							}catch(Exception e){
								proveForm.setCodigoPostal(0);
								Utils.imprimeLog("", e);
							}

						}else if (cel.getColumnIndex() == 7){ // delegacion
							try{
								proveForm.setDelegacion(cel.toString());
							}catch(Exception e){
								proveForm.setDelegacion("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 8){ // ciudad
							try{
								proveForm.setCiudad(cel.toString());
							}catch(Exception e){
								proveForm.setCiudad("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 9){ // estado
							try{
								proveForm.setEstado(cel.toString());
							}catch(Exception e){
								proveForm.setEstado("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 10){ // telefono
							try{
								int pos = cel.toString().indexOf(".");
								if (pos > 0){
									serie = cel.toString().substring(0, pos);
									linea = cel.toString().substring(pos+1, cel.toString().length() - 3);
									proveForm.setTelefono(serie+linea);
								}else{
									proveForm.setTelefono(cel.toString());
								}
							}catch(Exception e){
								proveForm.setTelefono("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 11){ // extencion
							try{
								int pos = cel.toString().indexOf("."); 
								if (pos > 0){
									proveForm.setExtencion(cel.toString().substring(0, pos));	
								}else{
									proveForm.setExtencion(cel.toString());
								}
								
							}catch(Exception e){
								proveForm.setExtencion("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 12){ // nombreContacto
							try{
								proveForm.setNombreContacto(cel.toString());
							}catch(Exception e){
								proveForm.setNombreContacto("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 13){ // email
							try{
								proveForm.setEmail(cel.toString());
							}catch(Exception e){
								proveForm.setEmail("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 14){ // usuario de acceso
							try{
								proveForm.setUsrAcceso(cel.toString());
							}catch(Exception e){
								proveForm.setUsrAcceso("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 15){ // pwd de acceso
							try{
								proveForm.setPwdAcceso(cel.toString());
							}catch(Exception e){
								proveForm.setPwdAcceso("");
								Utils.imprimeLog("", e);
							}
							
						}else if (cel.getColumnIndex() == 16){ // tipo proveedor
							try{
								proveForm.setTipoProveedor(cel.toString());
							}catch(Exception e){
								proveForm.setTipoProveedor("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 17){ // tipo validacion
							try{
								int pos = cel.toString().indexOf("."); 
								if (pos > 0){
									proveForm.setTipoConfirmacion(cel.toString().substring(0, pos));	
								}else{
									proveForm.setTipoConfirmacion(cel.toString());
								}
								
							}catch(Exception e){
								proveForm.setTipoConfirmacion("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 18){ // anexo 24
							proveForm.setAnexo24(cel.toString());
						}else if (cel.getColumnIndex() == 19){ // limite de tolerancia
							proveForm.setLimiteTolerancia(cel.toString());
						}else if (cel.getColumnIndex() == 20){ // banco mexico
							try{
								proveForm.setBanco(cel.toString());
							}catch(Exception e){
								proveForm.setBanco("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 21){ // sucursal mexico
							try{
								proveForm.setSucursal(cel.toString());
							}catch(Exception e){
								proveForm.setSucursal("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 22){ // nombre sucursal mexico
							try{
								proveForm.setNombreSucursal(cel.toString());
							}catch(Exception e){
								proveForm.setNombreSucursal("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 23){ // numero de cuenta mexico
							try{
								proveForm.setNumeroCuenta(cel.toString());
							}catch(Exception e){
								proveForm.setNumeroCuenta("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 24){ // cuenta clabe mexico
							try{
								proveForm.setCuentaClabe(cel.toString());
							}catch(Exception e){
								proveForm.setCuentaClabe("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 25){ // numero de convenio mexico
							try{
								proveForm.setNumeroConvenio(cel.toString());
							}catch(Exception e){
								proveForm.setNumeroConvenio("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 26){ // moneda mexico
							try{
								proveForm.setMoneda(cel.toString());
							}catch(Exception e){
								proveForm.setMoneda("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 27){ // banco dollar
							try{
								proveForm.setBancoDollar(cel.toString());
							}catch(Exception e){
								proveForm.setBancoDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 28){ // sucursal dollar
							try{
								proveForm.setSucursalDollar(cel.toString());
							}catch(Exception e){
								proveForm.setSucursalDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 29){ // nombre de sucursal dollar
							try{
								proveForm.setNombreSucursalDollar(cel.toString());
							}catch(Exception e){
								proveForm.setNombreSucursalDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 30){ // numero de cuenta dollar
							try{
								proveForm.setNumeroCuentaDollar(cel.toString());
							}catch(Exception e){
								proveForm.setNumeroCuentaDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 31){ // cuenta clabe dollar
							try{
								proveForm.setCuentaClabeDollar(cel.toString());
							}catch(Exception e){
								proveForm.setCuentaClabeDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 32){ // numero de convenio dollar
							try{
								proveForm.setNumeroConvenioDollar(cel.toString());
							}catch(Exception e){
								proveForm.setNumeroConvenioDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 33){ // moneda dollar
							try{
								proveForm.setMonedaDollar(cel.toString());
							}catch(Exception e){
								proveForm.setMonedaDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 34){ // aba dollar
							try{
								proveForm.setAbaDollar(cel.toString());
							}catch(Exception e){
								proveForm.setAbaDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 35){ // switf dollar
							try{
								proveForm.setSwitfCodeDollar(cel.toString());
							}catch(Exception e){
								proveForm.setSwitfCodeDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 36){ // banco otro
							try{
								proveForm.setBancoDollar(cel.toString());
							}catch(Exception e){
								proveForm.setBancoDollar("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 37){ // sucursal otro
							try{
								proveForm.setSucursalOtro(cel.toString());
							}catch(Exception e){
								proveForm.setSucursalOtro("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 38){ // nombre de sucursal otro
							try{
								proveForm.setNombreSucursalOtro(cel.toString());
							}catch(Exception e){
								proveForm.setNombreSucursalOtro("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 39){ // numero de cuenta otro
							try{
								proveForm.setNumeroCuentaOtro(cel.toString());
							}catch(Exception e){
								proveForm.setNumeroCuentaOtro("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 40){ // cuenta clabe otro
							try{
								proveForm.setCuentaClabeOtro(cel.toString());
							}catch(Exception e){
								proveForm.setCuentaClabeOtro("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 41){ // numero convenio otro
							try{
								proveForm.setNumeroConvenioOtro(cel.toString());
							}catch(Exception e){
								proveForm.setNumeroConvenioOtro("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 42){ // moneda otro
							try{
								proveForm.setMonedaOtro(cel.toString());
							}catch(Exception e){
								proveForm.setMonedaOtro("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 43){ // aba otro
							try{
								proveForm.setAbaOtro(cel.toString());
							}catch(Exception e){
								proveForm.setAbaOtro("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 44){ // swift  otro
							try{
								proveForm.setSwitfCodeOtro(cel.toString());
							}catch(Exception e){
								proveForm.setSwitfCodeOtro("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 45){ // correo 1
							try{
								proveForm.setEmail1(cel.toString());
							}catch(Exception e){
								proveForm.setEmail1("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 46){ // correo 1, recibe pago 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail1("S");	
								}else {
									proveForm.setTipoEmail1("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail1("N");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 47){ // correo 1, recibe ordenes 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail2("S");	
								}else {
									proveForm.setTipoEmail2("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail2("N");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 48){ // correo 2
							try{
								proveForm.setEmail2(cel.toString());
							}catch(Exception e){
								proveForm.setEmail2("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 49){ // correo 2, recibe pago 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail3("S");	
								}else {
									proveForm.setTipoEmail3("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail3("N");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 50){ // correo 2, recibe ordenes 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail4("S");	
								}else {
									proveForm.setTipoEmail4("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail4("N");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 51){ // correo 3
							try{
								proveForm.setEmail3(cel.toString());
							}catch(Exception e){
								proveForm.setEmail3("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 52){ // correo 3, recibe pago 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail5("S");	
								}else {
									proveForm.setTipoEmail5("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail5("N");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 53){ // correo 3, recibe ordenes 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail6("S");	
								}else {
									proveForm.setTipoEmail6("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail6("N");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 54){ // correo 4
							try{
								proveForm.setEmail4(cel.toString());
							}catch(Exception e){
								proveForm.setEmail4("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 55){ // correo 4, recibe pago 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail7("S");	
								}else {
									proveForm.setTipoEmail7("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail7("N");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 56){ // correo 4, recibe ordenes 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail8("S");	
								}else {
									proveForm.setTipoEmail8("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail8("N");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 57){ // correo 5
							try{
								proveForm.setEmail5(cel.toString());
							}catch(Exception e){
								proveForm.setEmail5("");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 58){ // correo 5, recibe pago 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail9("S");	
								}else {
									proveForm.setTipoEmail9("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail9("N");
								Utils.imprimeLog("", e);
							}
						}else if (cel.getColumnIndex() == 59){ // correo 5, recibe ordenes 1
							try{
								if ("S".equalsIgnoreCase(cel.toString())) {
									proveForm.setTipoEmail10("S");	
								}else {
									proveForm.setTipoEmail10("N");
								}
								
							}catch(Exception e){
								proveForm.setTipoEmail10("N");
								Utils.imprimeLog("", e);
							}
						}
						
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return proveForm;
	}
	
	
	public void iniciaCarga(String rutaArchivo, String usuarioTrans, String esquemaEmpresa, int claveEmpresa, int claveRegistro){
		try{
			procesaArchivo(rutaArchivo, usuarioTrans, esquemaEmpresa, claveEmpresa, claveRegistro);
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
}
