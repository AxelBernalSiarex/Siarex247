package com.siarex247.visor.Americanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;

public class GeneraDatosFacturaBean {
	
	private double SUB_TOTAL = 0;
	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public GeneraDatosFacturaForm calculaDatos(Connection con, String esquema, int claveProveedor, String [] listaOrdenes, String usuarioHTTP, String rutaLogo) {
		GeneraDatosFacturaForm generaDatosFacturaForm = new GeneraDatosFacturaForm();
		try {

			ProveedoresBean provBean = new ProveedoresBean();
			ProveedoresForm provForm = provBean.consultarProveedor(con, esquema, claveProveedor);
			
			generaDatosFacturaForm.setDatosFechaFactura(generaFactura(con, esquema, claveProveedor, listaOrdenes, usuarioHTTP, provForm));
			generaDatosFacturaForm.setDatosProveedorEmisor(datosProveedorEmisor(con, esquema, claveProveedor, rutaLogo, provForm));
			generaDatosFacturaForm.setDatosEmpresaReceptor(datosEmpresaReceptor(esquema));
			generaDatosFacturaForm.setDatosTabla1Encabezado(datosTablaEncabezado1(listaOrdenes, provForm.getIdProveedor()));
			generaDatosFacturaForm.setDatosTablaContenido(datosTablaContenido(con, esquema, listaOrdenes));
			generaDatosFacturaForm.setDatosTablaSubtotal(datosTablaContenidoFinal());
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return generaDatosFacturaForm;
	}
	
	
	private String[][] generaFactura(Connection con, String esquema, int claveProveedor, String [] listaOrdenes, String usuarioHTTP, ProveedoresForm proveForm){
		SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
		Date fechaHoy = new Date();
		String[][] datosFechaFactura = { { "", "" }, { "", ""}, { "", ""}};
		String [] numeroFactura = {"INVOICE NO",""};
		String [] fechaFactura =  {"Date",""};
		String [] paginaFactura = {"Page","                    #                    "};
		
		
		try {
			//String fechaAmericana = formatDate.format(fechaHoy);
			if ("".equalsIgnoreCase(proveForm.getAMERICANOS_SERIE())) {
				// se obtiene bandera de tipo de validacion
				  ConfigAdicionalesBean confSistemaBean = new ConfigAdicionalesBean();
				  HashMap<String, String> mapaConf = confSistemaBean.obtenerConfiguraciones(con, esquema);
				// Termina
				String VALOR_SERIE_AMERICANAS = Utils.noNulo(mapaConf.get("VALOR_SERIE_AMERICANAS"));
				proveForm.setAMERICANOS_SERIE(VALOR_SERIE_AMERICANAS);
			}
			
			long folioConsecutivo = consultarConsecutivo(con, esquema, claveProveedor);
			if (folioConsecutivo == 0) { 
				folioConsecutivo = proveForm.getAMERICANOS_FOLIO() + 1;
				guardarConsecutivo(con, esquema, folioConsecutivo, claveProveedor, Long.parseLong( listaOrdenes[0] ), usuarioHTTP);
			}else {
				folioConsecutivo++;
				guardarConsecutivo(con, esquema, folioConsecutivo, claveProveedor, Long.parseLong( listaOrdenes[0] ), usuarioHTTP);
			}
			
			if (!"".equalsIgnoreCase(proveForm.getAMERICANOS_SERIE())) {
				String facturaAmericana = proveForm.getAMERICANOS_SERIE() + folioConsecutivo;
				int totEspacios = (28 - facturaAmericana.length()) / 2;
				
				numeroFactura[1] = rellenaEspacios(totEspacios, rellenaEspacios(totEspacios, facturaAmericana, false), true);
				fechaFactura[1] = rellenaEspacios(10, rellenaEspacios(10, formatDate.format(fechaHoy), false), true);	
			}
			
			
			datosFechaFactura[0] = numeroFactura;
			datosFechaFactura[1] = fechaFactura;
			datosFechaFactura[2] = paginaFactura;
			
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return datosFechaFactura;
	}
	
	
	
	private String[] datosProveedorEmisor(Connection con, String esquema, int claveProveedor, String rutaLogo, ProveedoresForm proveForm){
		String[] datosProveedorEmisor = {"","","","", "", ""};
		try {
			
			datosProveedorEmisor[0] = rutaLogo;
			datosProveedorEmisor[1] = proveForm.getCalle();
			datosProveedorEmisor[2] = proveForm.getCiudad();
			datosProveedorEmisor[3] = proveForm.getEstado();
			datosProveedorEmisor[4] = String.valueOf( proveForm.getCodigoPostal() );
			datosProveedorEmisor[5] = proveForm.getTelefono();
			
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return datosProveedorEmisor;
	}
	
	
	
	private String[] datosEmpresaReceptor(String esquema){
		String[] datosEmpresaReceptor = {"","","","", "", ""};
		EmpresasForm empresasForm = null;
		try {
			empresasForm = consultaDatosEmpresa(esquema);
			int numArr = 0;
			
			datosEmpresaReceptor[numArr++] = empresasForm.getNombreLargo();
			datosEmpresaReceptor[numArr++] = empresasForm.getCalle();
			if (!"".equalsIgnoreCase(empresasForm.getCalle2())) {
				datosEmpresaReceptor[numArr++] = empresasForm.getCalle2();	
			}
			datosEmpresaReceptor[numArr++] = empresasForm.getCiudad() + " "  + empresasForm.getEstado() + ", C.P. "+ empresasForm.getCodigoPostal() ;
			datosEmpresaReceptor[numArr++] = "México";
			datosEmpresaReceptor[numArr++] = "RFC : " + empresasForm.getRfc();
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return datosEmpresaReceptor;
	}
	
	
	private String[][] datosTablaEncabezado1(String [] listaOrdenes, String idVendor){
		String[][] datosTabla1Encabezado = { { "", "", "" }, { "", "", ""}};
		String [] columnas = {"Order No.","Customer ID", "Purchase Order No."};
		String [] valores  = {"INDIVIDUAL","", "REFER TO EACH ITEM"};
		
		try {
			//{ "Order No.", "Customer ID", "Purchase Order No." }, { "MULTIPLE", "852612", "REFER TO EACH ITEM" }
			
			datosTabla1Encabezado[0] = columnas;
			
			if (listaOrdenes.length > 1) {
				valores[0] = "MULTIPLE";
			}
			//1000000030
			int totEspacios = ((32 - idVendor.length()) / 2);
			valores[1] = rellenaEspacios( totEspacios, (rellenaEspacios(totEspacios, idVendor, false)), true);
			
			
			datosTabla1Encabezado[1] = valores;
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return datosTabla1Encabezado;
	}
	
	
	
	private ArrayList<String []> datosTablaContenido(Connection con, String esquema, String [] listaOrdenes){
		//String[][] datosTablaContenido = null;
		
		//String [] columnas = {"Ordered.", "TMMBC PO# - CKD INVOICE", "Description", "Unit Price", "Ext. Price"};
		String [] renglones  = {"", "", "", "", ""};
		ArrayList<String []> datosTablaContenido  = new ArrayList<>();
		try {
			/*
			{ "Ordered.", "TMMBC PO# - CKD INVOICE", "Description", "Unit Price", "Ext. Price" },
			if (listaOrdenes.length > 19) {
				datosTablaContenido = new String[listaOrdenes.length + 1][5];
			}else {
				datosTablaContenido = new String[19][5];
			}
			
			datosTablaContenido[0] = columnas;
			*/

			
			
			double totalPrice = 0;
			int orderedTotal = 1;
			
			VisorOrdenesForm visorForm = null;
			//int numRow = 1;
			DecimalFormat decimal = new DecimalFormat("###,###.##");
			String montoFormateado = "";
			int totEspacios = 0;
			for (int x = 0; x < listaOrdenes.length; x++) {
				visorForm = consultaDatosOrdenes(con, esquema, Long.parseLong( listaOrdenes[x] ));
				double montoOrden = Utils.noNuloDouble(visorForm.getMonto());
				montoFormateado = decimal.format(montoOrden);
				totalPrice = (orderedTotal * montoOrden );
				
				
				totEspacios = ((20 - String.valueOf( orderedTotal ).length()) / 2);
				
				renglones[0] = rellenaEspacios(totEspacios, rellenaEspacios(totEspacios, String.valueOf( orderedTotal ), false), true);
				
				totEspacios = ((40 - String.valueOf(listaOrdenes[x]).length()) / 2);
				renglones[1] = rellenaEspacios(totEspacios, rellenaEspacios(totEspacios, String.valueOf(listaOrdenes[x]), false), true);
				renglones[2] = visorForm.getDescripcion();
				
				
				totEspacios = (  (13 - montoFormateado.length() + 2));
				renglones[3] = rellenaEspacios(totEspacios, "$ " + montoFormateado, false);
				
				totEspacios = (  (13 - decimal.format(totalPrice).length() + 2));
				renglones[4] =   rellenaEspacios(totEspacios, "$ " +decimal.format(totalPrice), false);
				//datosTablaContenido[numRow] = renglones;
				datosTablaContenido.add(renglones);
				SUB_TOTAL+=totalPrice;
				//numRow++;
				renglones = new String[5];
			}
			/*
			String [] renglonesVacios  = null;
			for (int x = 0; x < datosTablaContenido.length; x++ ) {
				renglonesVacios = datosTablaContenido[x];
				if (renglonesVacios[0] == null) {
					renglonesVacios[0]  = "";
					renglonesVacios[1]  = "";
					renglonesVacios[2]  = "";
					renglonesVacios[3]  = "";
					renglonesVacios[4]  = "";
					datosTablaContenido[x] = renglonesVacios;
					renglonesVacios = new String[5];
				}
			}
			*/
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return datosTablaContenido;
	}
	
	
	private String[][] datosTablaContenidoFinal(){
		String[][] datosTablaSubtotal = null;
		
		String [] datosRenglon1 = {"", ""};
		String [] datosRenglon2 = {"", ""};
		String [] datosRenglon3 = {"", ""};
		String [] datosRenglon4 = {"", ""};
		String [] datosRenglon5 = {"", ""};
		
		
		DecimalFormat decimal = new DecimalFormat("###,###.##");
		try {
			//String[][] content4 = { { "Subtotal", "                     $8,666.67"}, { "Misc", ""}, { "Freight", ""}, { "Tax", ""},{ "Total", "                     $8,666.67"}};
			datosTablaSubtotal = new String[5][2];

			//double totalPrice = 0;
			String misc = "";
			String freigth = "";
			String tax = "";
			
			double granTotal = SUB_TOTAL + 0 + 0 + 0 + 0;
			
			
			int totEspacios = (  (24 - decimal.format(SUB_TOTAL).length() + 2));
			
			datosRenglon1[0] = "Subtotal";
			datosRenglon1[1] =  rellenaEspacios(totEspacios, "$ " +decimal.format(SUB_TOTAL), false);
			datosTablaSubtotal[0] = datosRenglon1;
			datosRenglon2[0] = "Misc";
			datosRenglon2[1] = misc;
			datosTablaSubtotal[1] = datosRenglon2;
			datosRenglon3[0] = "Freight";
			datosRenglon3[1] = freigth;
			datosTablaSubtotal[2] = datosRenglon3;
			datosRenglon4[0] = "Tax";
			datosRenglon4[1] = tax;
			datosTablaSubtotal[3] = datosRenglon4;
			datosRenglon5[0] = "Total";
			
			totEspacios = (  (24 - decimal.format(granTotal).length() + 2));
			datosRenglon5[1] =  rellenaEspacios(totEspacios, "$ " +decimal.format(granTotal), false);
			datosTablaSubtotal[4] = datosRenglon5;
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return datosTablaSubtotal;
	}
	
	
	private long consultarConsecutivo(Connection con, String esquema, int claveProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long folioFactura = 0;
		try{
			
			stmt = con.prepareStatement(GeneraDatosFacturaQuerys.getObtenerConsecutivo(esquema));
			stmt.setInt(1, claveProveedor);
			rs = stmt.executeQuery();
			if (rs.next()){
				folioFactura = rs.getLong(1);
			}
			if (folioFactura == 0) {
				folioFactura = 1;
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
				stmt = null;
			}
		}
		return folioFactura;
	}
	
	
	

	private int guardarConsecutivo(Connection con, String esquema, long folioFactura, int claveProveedor, long folioOrden, String idUsuario){
		PreparedStatement stmt = null;
		int totRes = 0;
		try{
			stmt = con.prepareStatement(GeneraDatosFacturaQuerys.getGuardaConsecutivo(esquema));
			stmt.setLong(1, folioFactura);
			stmt.setInt(2, claveProveedor);
			stmt.setLong(3, folioOrden);
			stmt.setString(4, "OK");
			stmt.setString(5, idUsuario);
			totRes = stmt.executeUpdate();
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
				
			}catch(Exception e){
				stmt = null;
			}
		}
		return totRes;
	}
	
	
	private EmpresasForm consultaDatosEmpresa(String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		EmpresasForm empresaForm = new EmpresasForm();
		try{
			
			connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
//NOMBRE_LARGO, CALLE, CALLE2, CIUDAD, ESTADO, CODIGO_POSTAL, RFC			
			stmt = con.prepareStatement(GeneraDatosFacturaQuerys.getDatosEmpresaEmiror(esquema));
			stmt.setString(1, esquema);
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				empresaForm.setNombreLargo(Utils.noNuloNormal(rs.getString(1)));
				empresaForm.setCalle(Utils.noNuloNormal(rs.getString(2)));
				empresaForm.setCalle2(Utils.noNuloNormal(rs.getString(3)));
				empresaForm.setCiudad(Utils.noNuloNormal(rs.getString(4)));
				empresaForm.setEstado(Utils.noNuloNormal(rs.getString(5)));
				empresaForm.setCodigoPostal(Utils.noNuloINT(rs.getString(6)));
				empresaForm.setRfc(Utils.noNuloNormal(rs.getString(7)));
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
				rs = null;
				stmt = null;
				
			}catch(Exception e){
				rs = null;
				stmt = null;
			}
		}
		return empresaForm;
	}
	
	
	public VisorOrdenesForm consultaDatosOrdenes(Connection con, String esquema, long folioEmpresa){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		VisorOrdenesForm visorForm = new VisorOrdenesForm();
		try{
			stmt = con.prepareStatement(GeneraDatosFacturaQuerys.getDatosOrdenes(esquema));
			stmt.setLong(1, folioEmpresa);
			// DecimalFormat decimal = new DecimalFormat("###,###.##");
			rs = stmt.executeQuery();
			String descripcion = null;
			if (rs.next()) {
				descripcion  = Utils.noNuloNormal(rs.getString(1));
				if (descripcion.length() > 35) {
					descripcion = descripcion.substring(0, 35);
				}
				descripcion = descripcion.replace("&;", "");
				visorForm.setFolioEmpresa(folioEmpresa);
				visorForm.setDescripcion(descripcion);
				visorForm.setMonto(rs.getString(2));
				// visorForm.setMontoFormateado(decimal.format(rs.getDouble(2)));
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
				rs = null;
				stmt = null;
				
			}catch(Exception e){
				rs = null;
				stmt = null;
			}
		}
		return visorForm;
	}
	
	
	private String rellenaEspacios(int totEspacios, String cadOriginal, boolean izqDer) {
		StringBuffer sbEspacios = new StringBuffer();
		StringBuffer sbCadena = new StringBuffer();
		try {
			for (int x = 1; x <= totEspacios; x++) {
				sbEspacios.append(" ");
			}
			
			if (izqDer) {
				sbCadena.append(cadOriginal).append(sbEspacios.toString());
			}else {
				sbCadena.append(sbEspacios.toString()).append(cadOriginal);
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbCadena.toString();
	}
	
}
