package com.siarex247.layOut.Tareas;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.simple.JSONObject;

import com.siarex247.catalogos.Empleados.EmpleadosBean;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.configSistema.ConfOrdenes.ConfOrdenesBean;
import com.siarex247.configuraciones.ListaNegra.EnviarCorreoListaBean;
import com.siarex247.pdf.PDFLayoutTextStripper;
import com.siarex247.utils.Utils;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesBean;

public class OrdenesAlertas {

	
	public static final Logger logger = Logger.getLogger("siarex");
	
	
	public String armarMensajeCorreo(String mensajeOriginal, String razonSocial, String razonSocialEmpresa, String nombreUsuario){
		String mensajeFinal = "";
		try{
			mensajeFinal = mensajeOriginal.replace("<<razonSocial>>", razonSocial)
									   .replace("<<razonSocialEmpresa>>", razonSocialEmpresa)
									   //.replace("<<factura>>", folioEmpresa)
									   .replace("<<usuario>>", nombreUsuario);
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return mensajeFinal;
	}
	
	
	public int iniciaProceso(Connection con, String esquema,int claveTarea, String rutaPDF, String rutaDes,  String nombreArchivo, String tipoEnvio,
			boolean bandPrevalida, String tipoEmpresa ){
		int res = 0;
		try{
			 // 1. se calcula la configuracion del PDF
			TareasBean tareasBean = new TareasBean();
			ProveedoresBean provBean = new ProveedoresBean();
			
			// HashMap<String, String> mapaConfOrdenes = tareasBean.obtenerConfiguracion(con, esquema);
			ConfOrdenesBean confOrdenes = new ConfOrdenesBean();
			HashMap<String, String> mapaConfOrdenes = confOrdenes.obtenerConfiguracion(con, esquema);
			
			OrdenesAlertas ord = new OrdenesAlertas();
			OrdenesAlertasForm ordenesForm = new OrdenesAlertasForm();
			ordenesForm.setLlaveFinPDF(mapaConfOrdenes.get("LLAVE_FINPDF"));
			
			ordenesForm.setLlaveOrdenes(mapaConfOrdenes.get("LLAVE_ORDENES"));
			ordenesForm.setLlaveFinOrdenes(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_ORDENES")));
			
			ordenesForm.setLlaveTotal(mapaConfOrdenes.get("LLAVE_TOTAL"));
			ordenesForm.setLlaveFinTotal(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_TOTAL")));
			
			ordenesForm.setLlaveVendor(mapaConfOrdenes.get("LLAVE_VENDOR"));
			ordenesForm.setLlaveFinVendor(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_VENDOR")));
			
			ordenesForm.setLlaveMoneda(mapaConfOrdenes.get("LLAVE_MONEDA"));
			ordenesForm.setLlaveFinMoneda(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_MONEDA")));
			
			ordenesForm.setLlaveShipTO(mapaConfOrdenes.get("LLAVE_SHIPTO"));
			ordenesForm.setLlaveFinShipTO(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_SHIPTO")));
			
			ordenesForm.setLlaveDescri(mapaConfOrdenes.get("LLAVE_DESCRI"));
			ordenesForm.setLlaveFinDescri(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_DESCRI")));

			
			// 2. Se manda separar el archivo PDF y se depositan los archivos en la ruta indicada
			ArrayList<HashMap<String, String>> datosPDF = ord.lecturaPDF(ordenesForm, rutaPDF,rutaDes , nombreArchivo);
			// 3. Validamos si existe el proveedor y si es MEX o USA o no Importa
			HashMap<String, String> mapaValores = null;
			String RFC = "";
			String TIPO_PROVEEDOR = "";
			int claveProveedor = 0;
			ArrayList<String> datosProv = null;
			ArrayList<HashMap<String, String>> datosProvNoEncontrados = new ArrayList<HashMap<String,String>>();
			HashMap<String, Integer> mapaProveedor = new HashMap<String, Integer>();
//			logger.info("Etiqueta del Vendor...."+ordenesForm.getLlaveVendor() + "......");
			boolean bandEncontro = false;
			VisorOrdenesBean visorBean = new VisorOrdenesBean();
			long numeroOrden = 0;
			double total = 0;
			String TIPO_MONEDA = null;
			String DESCRIPCION = null;
			
			String nombreArchivoPDF = null;
			String asignarTO = null;
			EmpleadosBean empleadosBean = new EmpleadosBean();
			int idEmpleado = 0;
			boolean bandEncontroEmpleado = false;

			EnviarCorreoListaBean listaNegraBean = new EnviarCorreoListaBean();
			HashMap<String, JSONObject> mapaResultado = null;
			if (bandPrevalida) {
				String fechaActual = Utils.getFechayyyyMMdd();
				int annioActual = Integer.parseInt(fechaActual.substring(0, 4));
				int annioAnterior = annioActual - 1; 
				mapaResultado = listaNegraBean.obtenerEstatusActual(con, esquema, annioActual, annioActual);
				listaNegraBean.obtenerEstatusHistorico(con, esquema, mapaResultado, 2015, annioAnterior);	
			}
			
			JSONObject jsonobj = null;
			
			for (int x = 0; x < datosPDF.size(); x++){
				mapaValores = datosPDF.get(x);
				datosProv = provBean.buscarRFCxNumero(con, esquema, mapaValores.get("VENDOR_ID").trim());
				idEmpleado = empleadosBean.buscarEmpleadoXshipTO(con, esquema, mapaValores.get("SHIPTO").trim());
				
				if (datosProv.isEmpty()){ // si no encontro al Vendor
					if (idEmpleado == 0) {
						mapaValores.put("ESTATUS_SHIPTO", "NE");
						bandEncontroEmpleado = true;
					}else {
						mapaValores.put("ESTATUS_SHIPTO", "OK");
					}
					datosProvNoEncontrados.add(mapaValores);
					
				}else{ // si cumplio con la validacion de Tipo y existe en BD
					 // se manda grabar en la tabla de TAREAS vs ORDENES
					claveProveedor = Integer.parseInt(datosProv.get(0));
					RFC = datosProv.get(1);
					TIPO_PROVEEDOR = datosProv.get(2);
					if (tipoEnvio.equals("1")){
						//sbClavesProveedor.append(claveProveedor).append(",");
						mapaProveedor.put("PROVEDOR"+claveProveedor, claveProveedor);
						bandEncontro = true;
					}else if (tipoEnvio.equals("2")){
						if ("MEX".equalsIgnoreCase(TIPO_PROVEEDOR)){
							//sbClavesProveedor.append(claveProveedor).append(",");
							mapaProveedor.put("PROVEDOR"+claveProveedor, claveProveedor);
							bandEncontro = true;
						}
					}else if (tipoEnvio.equals("3")){
						if ("USA".equalsIgnoreCase(TIPO_PROVEEDOR)){
							//sbClavesProveedor.append(claveProveedor).append(",");
							mapaProveedor.put("PROVEDOR"+claveProveedor, claveProveedor);
							bandEncontro = true;
						}
					}
					if (bandEncontro){
						if (idEmpleado == 0) {
							mapaValores.put("ESTATUS_SHIPTO", "NE");
							bandEncontroEmpleado = true;
						}else {
							mapaValores.put("ESTATUS_SHIPTO", "OK");
						}
						
						if (bandPrevalida) {
							jsonobj = mapaResultado.get(RFC);
							if (jsonobj == null) {
								grabarTareaOrden(con, esquema, claveTarea, claveProveedor, RFC, mapaValores, "OK", "N");
							}else {
								grabarTareaOrden(con, esquema, claveTarea, claveProveedor, RFC, mapaValores, "NE", "S");
								res = 100;
							}
						}else {
							grabarTareaOrden(con, esquema, claveTarea, claveProveedor, RFC, mapaValores, "OK", "N");
						}
						//grabarTareaOrden(con, esquema, claveTarea, claveProveedor, RFC, mapaValores, "OK");
						
							if (idEmpleado > 0) {
								asignarTO = "E_" + idEmpleado;	
							}else {
								asignarTO = "";
							}
							if (!bandPrevalida){
								numeroOrden = Long.parseLong(mapaValores.get("ORDEN_COMPRA").trim());
								nombreArchivoPDF = mapaValores.get("NOMBRE_ARCHIVO").trim();
								total = parserTotal(mapaValores.get("TOTAL").trim());
								TIPO_MONEDA = mapaValores.get("MONEDA").trim();
								DESCRIPCION = Utils.noNulo(mapaValores.get("DESCRIPCION")).trim();
								//logger.info("Descripcion---->"+mapaValores.get("DESCRIPCION"));
								//logger.info("TIPO_MONEDA---->"+TIPO_MONEDA);
								int totReg = 0;
								if ("S".equalsIgnoreCase(tipoEmpresa)) { // si nacional se actualiza el registro
									totReg = actualizaOrden(con, esquema, mapaValores, asignarTO);
									//if (totReg == 0) { // falta esta parte de agregar nueva orden cuando sea empresa MEX
									//	int resultado = visorBean.nuevaOrden(con, esquema, numeroOrden, "", TIPO_MONEDA, total, claveProveedor, "0", asignarTO, "A5", null, null, "", nombreArchivoPDF);
									//}
								}
									//asignarTO = "E_" + buscarEmpleadoOrdenCompra(con, esquema, numeroOrden);
								if (totReg == 0) {
									if (DESCRIPCION.length() > 1024) {
										DESCRIPCION = DESCRIPCION.substring(0, 1024);
									}
									
									int resultado = visorBean.nuevaOrden(con, esquema, numeroOrden, DESCRIPCION.trim(), TIPO_MONEDA, total, claveProveedor, "0", asignarTO, "A5", null, null, "", nombreArchivoPDF);	
								}
							}
					}
					bandEncontro = false;
				}
			}
			// 4. Se graba a los proveedores que se les va a enviar el correo
			//logger.info("mapaProveedor===>"+mapaProveedor);
			if (bandPrevalida){
				if (!datosProvNoEncontrados.isEmpty()){
					for (HashMap<String, String> mapaProv : datosProvNoEncontrados){
						grabarTareaOrden(con, esquema, claveTarea, 0, "", mapaProv, "NE", "N");
						res = 100;
					}
				}
				
				if (bandEncontroEmpleado && res == 100) {
					res = 101;
				}else if (bandEncontroEmpleado) {
					res = 102;
				}
				
				
			}else{
				if (!mapaProveedor.isEmpty()){
					 StringBuffer sbClavesProveedor = new StringBuffer();
					 Collection<Integer> valMapa =  mapaProveedor.values();
					 Iterator<Integer> iteMapa = valMapa.iterator();
					 while(iteMapa.hasNext()){
						 sbClavesProveedor.append(iteMapa.next()).append(",");
					 }
					 res = tareasBean.altaTareProv(con, esquema, claveTarea, sbClavesProveedor.toString());
				}
			}
						
		}catch(Exception e){
			System.err.println("****************** ENTRO AL ERROR...................");
			Utils.imprimeLog("", e);
		}
		return res;
	}
	
	
	
	public ArrayList<HashMap<String, String>> lecturaPDF(OrdenesAlertasForm ordenesForm, String rutaArchivos, String rutaDestino, String nombreArchivoProcesar){
		ArrayList<HashMap<String, String>> datosResultado = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> mapaValores = null;
		PDDocument pd = null;
		try{
		//	int i = 0;
	        String nombreArchivo="";
	        String cadFin = "";
	        StringBuffer sbContenido = new StringBuffer();
	        if (nombreArchivoProcesar == null){
	        	logger.info("No hay archivos en la carpeta rutaDestino : "+rutaDestino);
	        	return null;
	    	}else {
	          
	          //List l = null;
	      //    PageFormat pageFormat = null;
	          PDPage page = null;
	          ArrayList<PDPage> listaPaginas = new ArrayList<PDPage>();
	         // for (int x=0;x<ficheros.length;x++){
	            String ruta=new String();
	            ruta = rutaArchivos + nombreArchivoProcesar; //SE ALMACENA LA RUTA DEL ARCHIVO A LEER.
	            cadFin = ordenesForm.getLlaveFinPDF();
	            
	              try {
	            	  File filePDF = new File(ruta);
	                  pd = Loader.loadPDF(filePDF);
	                  Splitter splitter = new Splitter();
	                  List<PDDocument> splittedDocuments = splitter.split(pd);
//	                  PDDocument hojaDocument = null;
	                  PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
	                  for (int x = 0; x < splittedDocuments.size(); x++) {
	                	  //page = (PDPage) obj[i];
	                	  page = pd.getPage(x);
	                	  String contendidoPDF = pdfTextStripper.getText(splittedDocuments.get(x));
	                	  sbContenido.append(contendidoPDF);
	                	  
	                	  if (sbContenido.toString().indexOf(cadFin) > -1){ // encontro la cadena final
	                		  cadFin = "ENCONTRO_CADENA";
	                		   mapaValores = buscarValoresNuevo(ordenesForm, sbContenido.toString());
	                		 // mapaValores = buscarValoresPlamex(ordenesForm, sbContenido.toString());
	                		  nombreArchivo = mapaValores.get("ORDEN_COMPRA");
	                		  mapaValores.put("NOMBRE_ARCHIVO", nombreArchivo+".pdf");
	                		  datosResultado.add(mapaValores);
	                		  
	                		  sbContenido.setLength(0);
	                	  }
	                	  
	                	  listaPaginas.add(page);
	                	  if (cadFin.equals("ENCONTRO_CADENA")){
	                		  generaLibroNuevo(listaPaginas, nombreArchivo, rutaDestino);
	                		  listaPaginas = new ArrayList<PDPage>();
	                		  nombreArchivo = "";
	                		  cadFin = ordenesForm.getLlaveFinPDF();
	                	  }
	                	  
	                  }
	              } catch (IOException e) {
	                  Utils.imprimeLog("", e);
	              }finally {
	            	  try {
	            		  pd.close();//CERRAMOS OBJETO ACROBAT
	            		  pd = null;
	            	  }catch(Exception e) {
	            		  Utils.imprimeLog("", e);
	            	  }
	              }
	        }
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
        return datosResultado;
    }
	
	 private HashMap<String, String> buscarValoresPlamex(OrdenesAlertasForm ordenesForm, String contenidoPDF){
		 HashMap<String, String> mapaValores = new HashMap<String, String>();
		 try {
			 String ORDEN_COMPRA = ordenesForm.getLlaveOrdenes();
			 String VENDOR_ID = ordenesForm.getLlaveVendor();
			 String TOTAL = ordenesForm.getLlaveTotal();
			 String MONEDA = ordenesForm.getLlaveMoneda();
			 String SHIPTO = ordenesForm.getLlaveShipTO();
			 String DESCR = ordenesForm.getLlaveDescri();
			 
			 mapaValores.put("ORDEN_COMPRA", "");
			 mapaValores.put("VENDOR_ID", "");
			 mapaValores.put("TOTAL", "");
			 mapaValores.put("MONEDA", "");
			 mapaValores.put("SHIPTO", "");
			 mapaValores.put("DESCRIPCION", "");
			
			 String noOrden = "";
             String vendorID = "";
             String total = "";
             String ship_to = "";
             
				 int posIni = contenidoPDF.indexOf("1 of  1"); 
	           	  if (posIni > -1) { // se busca la orden de compra
	           		  int posIniCad = posIni - 70; 
	           		  int posFinCada = posIni - 30;
	           		  noOrden = contenidoPDF.substring(posIniCad, posFinCada);
	           		 // System.err.println("textLine........"+noOrden.trim());
	           		  mapaValores.put("ORDEN_COMPRA", noOrden.trim());
	           	  }
	           	  posIni = contenidoPDF.indexOf("FORMA  DE EMBARQUE"); 
	           	  if (posIni > -1) { // se busca la orden de compra
	           		  int posIniCad = posIni + 80; 
	           		  int posFinCada = posIniCad + 10;
	           		  vendorID = contenidoPDF.substring(posIniCad, posFinCada);
	           		  mapaValores.put("VENDOR_ID", vendorID.trim());
	           	  }
	           	  
	           	  posIni = contenidoPDF.indexOf("FORMA  DE EMBARQUE"); 
	           	  if (posIni > -1) { // se busca la orden de compra
	           		  int posIniCad = posIni + 80; 
	           		  int posFinCada = posIniCad + 10;
	           		  vendorID = contenidoPDF.substring(posIniCad, posFinCada);
	           		  mapaValores.put("VENDOR_ID", vendorID.trim());
	           	  }
	           	  
	           	  posIni = contenidoPDF.indexOf("TOTAL"); 
	           	  if (posIni > -1) { // se busca la orden de compra
	           		  int posIniCad = posIni + 15; 
	           		  int posFinCada = posIniCad + 20;
	           		  total = contenidoPDF.substring(posIniCad, posFinCada).trim();
	           		  String totalSinMoneda = total.substring(0, total.length() - 3);
	           		  String moneda = total.substring(total.length() - 3);
	           		  mapaValores.put("TOTAL", totalSinMoneda.trim());
	           		  mapaValores.put("MONEDA", moneda.trim());
	           	  }
	           	  
	           	  posIni = contenidoPDF.indexOf("COMPRADOR"); 
	           	  if (posIni > -1) { // se busca la orden de compra
	           		  int posIniCad = posIni + 190; 
	           		  int posFinCada = posIniCad + 70;
	           		  ship_to = contenidoPDF.substring(posIniCad, posFinCada);
	           		  mapaValores.put("SHIPTO", ship_to.trim());
	           	  }
	         
	         logger.info("MapaOrdenes===>"+mapaValores);  	  
	           	  
		 } catch (Exception e) {
				Utils.imprimeLog("", e);
		 }
		return mapaValores;
	 }
	 
	
	 private HashMap<String, String> buscarValoresNuevo(OrdenesAlertasForm ordenesForm, String contenidoPDF){
		 HashMap<String, String> mapaValores = new HashMap<String, String>();
		 try {
			 String ORDEN_COMPRA = ordenesForm.getLlaveOrdenes();
			 String VENDOR_ID = ordenesForm.getLlaveVendor();
			 String TOTAL = ordenesForm.getLlaveTotal();
			 String MONEDA = ordenesForm.getLlaveMoneda();
			 String SHIPTO = ordenesForm.getLlaveShipTO();
			 String DESCR = ordenesForm.getLlaveDescri();
			 
			 mapaValores.put("ORDEN_COMPRA", "");
			 mapaValores.put("VENDOR_ID", "");
			 mapaValores.put("TOTAL", "");
			 mapaValores.put("MONEDA", "");
			 mapaValores.put("SHIPTO", "");
			 mapaValores.put("DESCRIPCION", "");
			 String arrContenido [ ] = contenidoPDF.split("\n");
			 String linePDF = null;
			 boolean bandMoneda=false;
			 boolean bandVendor = true;
			 int cadFinalMoneda = ordenesForm.getLlaveFinMoneda(); 
			 StringBuffer sbDes = new StringBuffer();
			 boolean bandDesc = false;
			 int cadIniDes = 0;
					 
			 for (int line=0; line < arrContenido.length; line++) {
				// logger.info("Line------>"+arrContenido[line]);
				 linePDF = arrContenido[line];
				 // ORDEN DE COMPRA
				 if (linePDF.indexOf(ORDEN_COMPRA) > -1){ // orden de compra
					 int cadIni = linePDF.indexOf(ORDEN_COMPRA) + ordenesForm.getLlaveOrdenes().length();
					 int cadFinal = cadIni + ordenesForm.getLlaveFinOrdenes();
					 mapaValores.put("ORDEN_COMPRA", linePDF.substring(cadIni, cadFinal ));
				 }
				 // TIPO DE MONEDA
				 if (bandMoneda) {
					 bandMoneda=false;
					 int posIni = linePDF.length() - cadFinalMoneda;
					 mapaValores.put("MONEDA", linePDF.substring(posIni, linePDF.length()).trim());
				 }
				 
				 if (linePDF.indexOf(MONEDA) > -1){ // Moneda
					 bandMoneda=true;
				 }
				 
				 // SHIP_TO o VENDOR
				 if (linePDF.indexOf(SHIPTO) > -1){
					 int cadIni = linePDF.indexOf(SHIPTO) + ordenesForm.getLlaveShipTO().length();
					 int cadFinal = cadIni + ordenesForm.getLlaveFinShipTO();
					 mapaValores.put("SHIPTO", linePDF.substring(cadIni, cadFinal ).trim());
				 }
				 
				 
				 // Vendor
				 if (linePDF.indexOf(VENDOR_ID) > -1 && bandVendor){
					 int cadIni = linePDF.indexOf(VENDOR_ID) + ordenesForm.getLlaveVendor().length();
					 int cadFinal = cadIni + ordenesForm.getLlaveFinVendor();
					 mapaValores.put("VENDOR_ID", linePDF.substring(cadIni, cadFinal ).trim());
					 bandVendor=false;
				 }
				 
				 
				  // TOTAL
				 if (linePDF.indexOf(TOTAL) > -1){
					 int cadIni = linePDF.indexOf(TOTAL) + ordenesForm.getLlaveTotal().length();
					 int cadFinal = cadIni + ordenesForm.getLlaveFinTotal();
					 mapaValores.put("TOTAL", linePDF.substring(cadIni, cadFinal ).trim());
				 }
				 
				 
				// DESCRIPCION
				 if (linePDF.indexOf("1   -1") > -1 || linePDF.indexOf("2   -1") > -1 || linePDF.indexOf("3   -1") > -1 || linePDF.indexOf("4   -1") > -1 || linePDF.indexOf("5   -1") > -1 || linePDF.indexOf("6   -1") > -1 || linePDF.indexOf("7   -1") > -1){
					 bandDesc = true;
					 cadIniDes = linePDF.indexOf(DESCR) + ordenesForm.getLlaveDescri().length();
					 //int cadFinal = cadIni + ordenesForm.getLlaveFinDescri();
					 //mapaValores.put("DESCRIPCION", linePDF.substring(cadIni, cadFinal ).trim());
					 sbDes.append("&;");
				 }
				 
				 if (bandDesc) {
					 if (linePDF.indexOf("Schedule   Total") > -1) {
						 //mapaValores.put("DESCRI", sbDes.toString());
						 bandDesc = false;
					 }else {
						 int cadFinal = cadIniDes + ordenesForm.getLlaveFinDescri();
						 if (linePDF.trim().length() > 0) {
							 sbDes.append(linePDF.substring(cadIniDes, cadFinal ).trim()).append(" ");	 
						 }
						 	 
					 }
				 }
			 }
			// logger.info("sbDes--------------->"+sbDes.toString());
			 mapaValores.put("DESCRIPCION", sbDes.toString());
		 } catch (Exception e) {
				Utils.imprimeLog("", e);
		 }
		return mapaValores;
	 }
	
	 private void generaLibroNuevo(ArrayList<PDPage> listaPaginas, String nombre, String rutaDestino){
         try{
        	 // PDPage page
	       	  PDDocument document = new PDDocument();
	       	  for (int x = 0; x < listaPaginas.size(); x++){
	       		document.addPage( listaPaginas.get(x) );  
	       	  }
		     // logger.info("guardando archivo..."+rutaDestino+nombre);
		      document.save(rutaDestino+nombre+".pdf");// ruta donde deposita el archivo nuevo
		      document.close();
         }catch(Exception e){
        	 Utils.imprimeLog("", e);
         }
   }
	
	
	private int grabarTareaOrden(Connection con, String esquema, int claveTarea, int claveProveedor, String RFC, 
			HashMap<String, String> mapaValores, String estatusOrden, String listaNegra){
		String total = null;
		String moneda = null;
		String numeroOrden = null;
		String nombreArchivoPDF = null;
		String vendorID = null;
		String shipTO = null;
		String ESTATUS_SHIPTO = null;
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try{
			
			//logger.info("mapaValores===>"+mapaValores);
			numeroOrden = mapaValores.get("ORDEN_COMPRA").trim();
			total = mapaValores.get("TOTAL").trim();
			nombreArchivoPDF = mapaValores.get("NOMBRE_ARCHIVO").trim();
			moneda = mapaValores.get("MONEDA").trim();
			vendorID = mapaValores.get("VENDOR_ID").trim();
			shipTO  =  mapaValores.get("SHIPTO").trim();
			ESTATUS_SHIPTO  =  mapaValores.get("ESTATUS_SHIPTO").trim();
			
			
			stmt = con.prepareStatement(TareasQuerys.getQueryGrabarTareaOrden(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, claveTarea);
			stmt.setDouble(2, Double.parseDouble(numeroOrden));
			stmt.setInt(3, claveProveedor);
			stmt.setString(4, vendorID);
			stmt.setString(5, RFC);
			stmt.setDouble(6, parserTotal(total));
			stmt.setString(7, moneda);
			stmt.setString(8, nombreArchivoPDF);
			stmt.setString(9, estatusOrden);
			stmt.setString(10, shipTO);
			stmt.setString(11, ESTATUS_SHIPTO);
			stmt.setString(12, listaNegra);
			//logger.info("stmt===>"+stmt);
			int cant = stmt.executeUpdate();
            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next())
                    resultado = rs.getInt(1);
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
		return resultado;
	}
	
	
	private int actualizaOrden(Connection con, String esquema, HashMap<String, String> mapaValores, String asignarTO){
		String numeroOrden = null;
		String nombreArchivoPDF = null;
		String descripcion = null;
		PreparedStatement stmt = null;
		int resultado = 0;
		try{
			numeroOrden = mapaValores.get("ORDEN_COMPRA").trim();
			nombreArchivoPDF = mapaValores.get("NOMBRE_ARCHIVO").trim();
			descripcion = Utils.noNulo(mapaValores.get("DESCRIPCION"));
			if (descripcion.length() > 200) {
				descripcion = descripcion.substring(0, 200);
			}
			stmt = con.prepareStatement(TareasQuerys.getQueryActualizaOrden(esquema));
			stmt.setString(1, nombreArchivoPDF);
			stmt.setString(2, descripcion);
			stmt.setString(3, asignarTO);
			stmt.setDouble(4, Double.parseDouble(numeroOrden));
			resultado = stmt.executeUpdate();
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
		return resultado;
	}
	
	
	private double parserTotal(String cadTotal){
		double total = 0;
		try{
			// 22,589.63
			String arrTotal [] = cadTotal.split(",");
			StringBuffer sbTotal = new StringBuffer();
			for (int x = 0; x < arrTotal.length; x++){
				sbTotal.append(arrTotal[x]);
			}
			if (sbTotal.length() > 0){
				total = Double.parseDouble(sbTotal.toString()); 
			}else{
				total = Double.parseDouble(cadTotal);
			}
		}catch(Exception e){
			
		}
		return total;
	}
	
	
}
