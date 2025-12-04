package com.siarex247.cumplimientoFiscal.Pedimentos;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.siarex247.pdf.PDFLayoutTextStripper;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class PedimentosBean {

	
	
public static final Logger logger = Logger.getLogger("siarex");
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPedimentos(Connection con, String esquema, String noPedimento, String fechaInicial, String fechaFinal){
		PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Map<String, Object > mapaRes = new HashMap<String, Object>();
        int paramSTMT = 1;

        try{
        	StringBuffer sbQuery = new StringBuffer(PedimentosQuerys.getConsultaPedimentos(esquema));
        	/*
        	if (!"".equalsIgnoreCase(noPedimento)){
        		sbQuery.append(" where NUM_PEDIMENTO like '%"+noPedimento+"%'");
        	}
        	*/
        	
        	if ((!fechaInicial.equalsIgnoreCase("") && !fechaFinal.equalsIgnoreCase("")) && sbQuery.indexOf("where") > -1){
        		sbQuery.append(" and FECHA_PAGO between ? and ?");
        	}
        	else if (!fechaInicial.equalsIgnoreCase("") && !fechaFinal.equalsIgnoreCase("")){
        		sbQuery.append(" where FECHA_PAGO between ? and ?");
        	}
        	
        	sbQuery.append(" order by FECHA_TRANS desc");
        	stmt = con.prepareStatement(sbQuery.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        	
        	if ((!fechaInicial.equalsIgnoreCase("") && !fechaFinal.equalsIgnoreCase("")) && sbQuery.indexOf("where") > -1){
        		stmt.setString(paramSTMT++, fechaInicial);
        		stmt.setString(paramSTMT++, fechaFinal);
        	}
        	else if (!fechaInicial.equalsIgnoreCase("") && !fechaFinal.equalsIgnoreCase("")){
        		stmt.setString(paramSTMT++, fechaInicial);
        		stmt.setString(paramSTMT++, fechaFinal);
        	}
        	
        	rs = stmt.executeQuery();

            while(rs.next()) {
	            	jsonobj.put("ID_REGISTRO", rs.getInt(1));
					jsonobj.put("NUM_PEDIMENTO", Utils.noNuloNormal(rs.getString(2)));
					jsonobj.put("CVE_PEDIMENTO", Utils.noNuloNormal(rs.getString(3)));
					jsonobj.put("REGIMEN", Utils.noNuloNormal(rs.getString(4)));
					jsonobj.put("DTA", Utils.noNuloNormal(rs.getString(5)));
					jsonobj.put("IVA", Utils.noNuloNormal(rs.getString(6)));
					jsonobj.put("IGI", Utils.noNuloNormal(rs.getString(7)));
					jsonobj.put("PRV", Utils.noNuloNormal(rs.getString(8)));
					jsonobj.put("IVAPRV", Utils.noNuloNormal(rs.getString(9)));
					jsonobj.put("EFECTIVO", Utils.noNuloNormal(rs.getString(10)));
					jsonobj.put("OTROS", Utils.noNuloNormal(rs.getString(11)));
					jsonobj.put("TOTAL", Utils.noNuloNormal(rs.getString(12)));
					jsonobj.put("BANCO", Utils.noNuloNormal(rs.getString(13)));
					jsonobj.put("LINEA_CAPTURA", Utils.noNuloNormal(rs.getString(14)));
					jsonobj.put("IMPORTE_PAGO", Utils.noNuloNormal(rs.getString(15)));
					jsonobj.put("FECHA_PAGO", Utils.noNuloNormal(rs.getString(16)));
					jsonobj.put("NUMERO_OPERACION", Utils.noNuloNormal(rs.getString(17)));
					jsonobj.put("NUMERO_SAT", Utils.noNuloNormal(rs.getString(18)));
					jsonobj.put("MEDIO_PRESENTACION", Utils.noNuloNormal(rs.getString(19)));
					jsonobj.put("MEDIO_RECEPCION", Utils.noNuloNormal(rs.getString(20)));
					
					jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
            	
            }
            mapaRes.put("detalle", jsonArray);
        }
        catch(Exception e){
        	Utils.imprimeLog("getPedimentos(): ", e);
        }
        finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return mapaRes;
    }
	
	
	public ArrayList<PedimentosForm> iniciaProceso(String rutaPDF, String esquema){
		ArrayList<PedimentosForm> listaPedimentos = null;
		try{
			
			File filePDF = new File(rutaPDF);
			String nombreArchivo = filePDF.getName();
			String rutaPedimentos = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquema + File.separator + "PEDIMENTOS" + File.separator;
			String rutaTrabajo = UtilsPATH.RUTA_PUBLIC_HTML + esquema + File.separator + "PEDIMENTOS" + File.separator + nombreArchivo;

			 // String rutaDes = "C:/PEDIMENTOS/DESTINO/POLY.pdf";
			 // String rutaPedimentos = "C:/PEDIMENTOS/DESTINO/"; 

			
			File fileDes = new File(rutaTrabajo);
			UtilsFile.moveFileDirectory(filePDF, fileDes, true, false, true);
			
			//logger.info("rutaDes==============>"+rutaTrabajo);
			//logger.info("rutaPedimentos==============>"+rutaPedimentos);
			
			// 1. se calcula la configuracion del PDF
			
			HashMap<String, String> mapaConfOrdenes = new HashMap<>();
			
			//mapaConfOrdenes.put("LLAVE_FINPDF", "****FIN  DE PEDIMENTO");
			mapaConfOrdenes.put("LLAVE_FINPDF", "***********FIN  DE PEDIMENTO");
			mapaConfOrdenes.put("LLAVE_FINPDF_2", "***    FIN  DE PEDIMENTO");
			mapaConfOrdenes.put("LLAVE_FINPDF_3", "****     FIN DE PEDIMENTO");

			//mapaConfOrdenes.put("LLAVE_PEDIMENTO", "NUM.   PEDIMENTO:");
			mapaConfOrdenes.put("LLAVE_PEDIMENTO", "NUM.   PEDIMENTO:");
			mapaConfOrdenes.put("LLAVE_PEDIMENTO_2", "NUM.  PEDIMENTO:");
			mapaConfOrdenes.put("LLAVE_PEDIMENTO_3", "NUM. PEDIMENTO:");
			mapaConfOrdenes.put("LLAVE_FIN_PEDIMENTO", "7");
			
			mapaConfOrdenes.put("LLAVE_CVE_PEDIMENTO",   "CVE.  PEDIMENTO:");
			mapaConfOrdenes.put("LLAVE_CVE_PEDIMENTO_2", "CVE. PEDIMENTO:");
			mapaConfOrdenes.put("LLAVE_FIN_CVE_PEDIMENTO", "7");
			
			mapaConfOrdenes.put("LLAVE_REGIMEN", "REGIMEN:");
			mapaConfOrdenes.put("LLAVE_FIN_REGIMEN", "19");

			//mapaConfOrdenes.put("LLAVE_DTA", "DTA           ");
			mapaConfOrdenes.put("LLAVE_DTA", "DTA         ");
			//mapaConfOrdenes.put("LLAVE_FIN_DTA", "40");
			mapaConfOrdenes.put("LLAVE_FIN_DTA", "15");
			
			mapaConfOrdenes.put("LLAVE_IVA",   "IVA           ");
			mapaConfOrdenes.put("LLAVE_IVA_2", "IVA         ");
			mapaConfOrdenes.put("LLAVE_FIN_IVA", "12");
			
			mapaConfOrdenes.put("LLAVE_IGI", "IGI           ");
			mapaConfOrdenes.put("LLAVE_IGI_2", "IGI         ");
			mapaConfOrdenes.put("LLAVE_FIN_IGI", "12");
			
			//mapaConfOrdenes.put("LLAVE_PRV", "PRV           ");
			mapaConfOrdenes.put("LLAVE_PRV", "PRV         ");
			//mapaConfOrdenes.put("LLAVE_FIN_PRV", "12");
			mapaConfOrdenes.put("LLAVE_FIN_PRV", "15");

			
			mapaConfOrdenes.put("LLAVE_IVAPRV", "IVA/PRV     ");
			mapaConfOrdenes.put("LLAVE_IVAPRV_2", "IVA PRV     ");
			mapaConfOrdenes.put("LLAVE_FIN_IVAPRV", "15");

			mapaConfOrdenes.put("LLAVE_EFECTIVO",   "EFECTIVO               ");
			mapaConfOrdenes.put("LLAVE_EFECTIVO_2", "EFECTIVO           ");
			mapaConfOrdenes.put("LLAVE_EFECTIVO_3", "EFECTIVO         ");
			mapaConfOrdenes.put("LLAVE_FIN_EFECTIVO", "20");

			mapaConfOrdenes.put("LLAVE_OTROS", "OTROS                ");
			mapaConfOrdenes.put("LLAVE_OTROS_2", "OTROS              ");
			mapaConfOrdenes.put("LLAVE_OTROS_3", "OTROS            ");
			mapaConfOrdenes.put("LLAVE_FIN_OTROS", "24");

			mapaConfOrdenes.put("LLAVE_TOTAL", "TOTAL           ");
			mapaConfOrdenes.put("LLAVE_FIN_TOTAL", "23");

			mapaConfOrdenes.put("LLAVE_BANCO", "Institución  Bancaria:");
			mapaConfOrdenes.put("LLAVE_BANCO_2", "BANCO: ");
			mapaConfOrdenes.put("LLAVE_FIN_BANCO", "80");

			mapaConfOrdenes.put("LLAVE_LINEA_CAPTURA",   "Línea de Captura:");
			mapaConfOrdenes.put("LLAVE_LINEA_CAPTURA_2", "Línea  de  Captura:");
			mapaConfOrdenes.put("LLAVE_LINEA_CAPTURA_3", "Línea de  Captura:");
			mapaConfOrdenes.put("LLAVE_LINEA_CAPTURA_4", "LINEA  DE CAPTURA:");
			mapaConfOrdenes.put("LLAVE_FIN_LINEA_CAPTURA", "120");

			mapaConfOrdenes.put("LLAVE_IMPORTE_PAGO",   "Importe de Pago:");
			mapaConfOrdenes.put("LLAVE_IMPORTE_PAGO_2", "Importe  de  Pago:");
			mapaConfOrdenes.put("LLAVE_IMPORTE_PAGO_3", "IMPORTE  PAGADO:");
			mapaConfOrdenes.put("LLAVE_FIN_IMPORTE_PAGO", "19");
			
			mapaConfOrdenes.put("LLAVE_FECHA_PAGO",   "Fecha  de pago:");
			mapaConfOrdenes.put("LLAVE_FECHA_PAGO_2", "Fecha de  pago:");
			mapaConfOrdenes.put("LLAVE_FECHA_PAGO_3", "Fecha de pago:");
			mapaConfOrdenes.put("LLAVE_FECHA_PAGO_4", "FECHA DE PAGO:");
			mapaConfOrdenes.put("LLAVE_FIN_FECHA_PAGO", "14");
			
			mapaConfOrdenes.put("LLAVE_OPERACION_BANCARIA",   "operación  bancaria:");
			mapaConfOrdenes.put("LLAVE_OPERACION_BANCARIA_2", "operacion  bancaria:");
			mapaConfOrdenes.put("LLAVE_OPERACION_BANCARIA_3", "operación bancaria:");
			mapaConfOrdenes.put("LLAVE_OPERACION_BANCARIA_4", "OPERACION BANCARIA:");
			mapaConfOrdenes.put("LLAVE_FIN_OPERACION_BANCARIA", "100");

			mapaConfOrdenes.put("LLAVE_TRANSACCION_SAT",   "Transacción  SAT:");
			mapaConfOrdenes.put("LLAVE_TRANSACCION_SAT_2", "Transacción SAT:");
			mapaConfOrdenes.put("LLAVE_TRANSACCION_SAT_3", "TRANSACCION SAT:");
			
			mapaConfOrdenes.put("LLAVE_FIN_TRANSACCION_SAT", "100");

			mapaConfOrdenes.put("LLAVE_MEDIO_PRESENTACION",   "Medio  de presentación:");
			mapaConfOrdenes.put("LLAVE_MEDIO_PRESENTACION_2", "Medio de  presentación:");
			mapaConfOrdenes.put("LLAVE_MEDIO_PRESENTACION_3", "Medio de presentación:");
			mapaConfOrdenes.put("LLAVE_MEDIO_PRESENTACION_4", "MEDIO  DE PRESENTACION:");
			mapaConfOrdenes.put("LLAVE_FIN_MEDIO_PRESENTACION", "100");

			
			mapaConfOrdenes.put("LLAVE_MEDIO_RECEPCION",   "Medio  de recepción/cobro:");
			mapaConfOrdenes.put("LLAVE_MEDIO_RECEPCION_2", "Medio de  recepción/cobro:");
			mapaConfOrdenes.put("LLAVE_MEDIO_RECEPCION_3", "Medio de recepción/cobro:");
			mapaConfOrdenes.put("LLAVE_MEDIO_RECEPCION_4", "MEDIO  DE RECEPCION/COBRO:");
			mapaConfOrdenes.put("LLAVE_FIN_MEDIO_RECEPCION", "100");

			
			EtiquetasPedimentosForm ordenesForm = new EtiquetasPedimentosForm();
			ordenesForm.setLlaveFinPDF(mapaConfOrdenes.get("LLAVE_FINPDF"));
			ordenesForm.setLlaveFinPDF2(mapaConfOrdenes.get("LLAVE_FINPDF_2"));
			ordenesForm.setLlaveFinPDF3(mapaConfOrdenes.get("LLAVE_FINPDF_3"));
			
			ordenesForm.setLlavePedimento(mapaConfOrdenes.get("LLAVE_PEDIMENTO"));
			ordenesForm.setLlavePedimento2(mapaConfOrdenes.get("LLAVE_PEDIMENTO_2"));
			ordenesForm.setLlavePedimento3(mapaConfOrdenes.get("LLAVE_PEDIMENTO_3"));
			ordenesForm.setLlaveFinPedimento(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_PEDIMENTO")));
			
			ordenesForm.setLlaveClavePedimento(mapaConfOrdenes.get("LLAVE_CVE_PEDIMENTO"));
			ordenesForm.setLlaveClavePedimento2(mapaConfOrdenes.get("LLAVE_CVE_PEDIMENTO_2"));
			ordenesForm.setLlaveFinClavePedimento(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_CVE_PEDIMENTO")));

			ordenesForm.setLlaveRegimen(mapaConfOrdenes.get("LLAVE_REGIMEN"));
			ordenesForm.setLlaveFinRegimen(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_REGIMEN")));

			
			ordenesForm.setLlaveDTA(mapaConfOrdenes.get("LLAVE_DTA"));
			ordenesForm.setLlaveFinDTA(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_DTA")));

			
			ordenesForm.setLlaveIVA(mapaConfOrdenes.get("LLAVE_IVA"));
			ordenesForm.setLlaveIVA2(mapaConfOrdenes.get("LLAVE_IVA_2"));
			ordenesForm.setLlaveFinIVA(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_IVA")));

			ordenesForm.setLlaveIGI(mapaConfOrdenes.get("LLAVE_IGI"));
			ordenesForm.setLlaveIGI2(mapaConfOrdenes.get("LLAVE_IGI_2"));
			ordenesForm.setLlaveFinIGI(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_IGI")));

			ordenesForm.setLlavePRV(mapaConfOrdenes.get("LLAVE_PRV"));
			ordenesForm.setLlaveFinPRV(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_PRV")));

			
			ordenesForm.setLlaveIVAPRV(mapaConfOrdenes.get("LLAVE_IVAPRV"));
			ordenesForm.setLlaveIVAPRV2(mapaConfOrdenes.get("LLAVE_IVAPRV_2"));
			ordenesForm.setLlaveFinIVAPRV(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_IVAPRV")));

			ordenesForm.setLlaveEfectivo(mapaConfOrdenes.get("LLAVE_EFECTIVO"));
			ordenesForm.setLlaveEfectivo2(mapaConfOrdenes.get("LLAVE_EFECTIVO_2"));
			ordenesForm.setLlaveEfectivo3(mapaConfOrdenes.get("LLAVE_EFECTIVO_3"));
			ordenesForm.setLlaveFinEfectivo(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_EFECTIVO")));

			ordenesForm.setLlaveOtros(mapaConfOrdenes.get("LLAVE_OTROS"));
			ordenesForm.setLlaveOtros2(mapaConfOrdenes.get("LLAVE_OTROS_2"));
			ordenesForm.setLlaveOtros3(mapaConfOrdenes.get("LLAVE_OTROS_3"));
			ordenesForm.setLlaveFinOtros(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_OTROS")));

			ordenesForm.setLlaveTotal(mapaConfOrdenes.get("LLAVE_TOTAL"));
			ordenesForm.setLlaveFinTotal(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_TOTAL")));

			ordenesForm.setLlaveBanco(mapaConfOrdenes.get("LLAVE_BANCO"));
			ordenesForm.setLlaveBanco2(mapaConfOrdenes.get("LLAVE_BANCO_2"));
			ordenesForm.setLlaveFinBanco(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_BANCO")));

			ordenesForm.setLlaveLineaCaptura(mapaConfOrdenes.get("LLAVE_LINEA_CAPTURA"));
			ordenesForm.setLlaveLineaCaptura2(mapaConfOrdenes.get("LLAVE_LINEA_CAPTURA_2"));
			ordenesForm.setLlaveLineaCaptura3(mapaConfOrdenes.get("LLAVE_LINEA_CAPTURA_3"));
			ordenesForm.setLlaveLineaCaptura4(mapaConfOrdenes.get("LLAVE_LINEA_CAPTURA_4"));
			ordenesForm.setLlaveFinLineaCaptura(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_LINEA_CAPTURA")));

			ordenesForm.setLlaveImportePago(mapaConfOrdenes.get("LLAVE_IMPORTE_PAGO"));
			ordenesForm.setLlaveImportePago2(mapaConfOrdenes.get("LLAVE_IMPORTE_PAGO_2"));
			ordenesForm.setLlaveImportePago3(mapaConfOrdenes.get("LLAVE_IMPORTE_PAGO_3"));
			ordenesForm.setLlaveFinImportePago(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_IMPORTE_PAGO")));

			ordenesForm.setLlaveFechaPago(mapaConfOrdenes.get("LLAVE_FECHA_PAGO"));
			ordenesForm.setLlaveFechaPago2(mapaConfOrdenes.get("LLAVE_FECHA_PAGO_2"));
			ordenesForm.setLlaveFechaPago3(mapaConfOrdenes.get("LLAVE_FECHA_PAGO_3"));
			ordenesForm.setLlaveFechaPago4(mapaConfOrdenes.get("LLAVE_FECHA_PAGO_4"));
			ordenesForm.setLlaveFinFechaPago(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_FECHA_PAGO")));

			ordenesForm.setLlaveOperacionBancaria(mapaConfOrdenes.get("LLAVE_OPERACION_BANCARIA"));
			ordenesForm.setLlaveOperacionBancaria2(mapaConfOrdenes.get("LLAVE_OPERACION_BANCARIA_2"));
			ordenesForm.setLlaveOperacionBancaria3(mapaConfOrdenes.get("LLAVE_OPERACION_BANCARIA_3"));
			ordenesForm.setLlaveOperacionBancaria4(mapaConfOrdenes.get("LLAVE_OPERACION_BANCARIA_4"));
			ordenesForm.setLlaveFinOperacionBancaria(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_OPERACION_BANCARIA")));

			ordenesForm.setLlaveTransaccionSat(mapaConfOrdenes.get("LLAVE_TRANSACCION_SAT"));
			ordenesForm.setLlaveTransaccionSat2(mapaConfOrdenes.get("LLAVE_TRANSACCION_SAT_2"));
			ordenesForm.setLlaveTransaccionSat3(mapaConfOrdenes.get("LLAVE_TRANSACCION_SAT_3"));
			ordenesForm.setLlaveFinTransaccionSat(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_TRANSACCION_SAT")));

			ordenesForm.setLlaveMedioPresentacion(mapaConfOrdenes.get("LLAVE_MEDIO_PRESENTACION"));
			ordenesForm.setLlaveMedioPresentacion2(mapaConfOrdenes.get("LLAVE_MEDIO_PRESENTACION_2"));
			ordenesForm.setLlaveMedioPresentacion3(mapaConfOrdenes.get("LLAVE_MEDIO_PRESENTACION_3"));
			ordenesForm.setLlaveMedioPresentacion4(mapaConfOrdenes.get("LLAVE_MEDIO_PRESENTACION_4"));
			ordenesForm.setLlaveFinMedioPresentacion(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_MEDIO_PRESENTACION")));

			ordenesForm.setLlaveMedioRecepcion(mapaConfOrdenes.get("LLAVE_MEDIO_RECEPCION"));
			ordenesForm.setLlaveMedioRecepcion2(mapaConfOrdenes.get("LLAVE_MEDIO_RECEPCION_2"));
			ordenesForm.setLlaveMedioRecepcion3(mapaConfOrdenes.get("LLAVE_MEDIO_RECEPCION_3"));
			ordenesForm.setLlaveMedioRecepcion4(mapaConfOrdenes.get("LLAVE_MEDIO_RECEPCION_4"));
			ordenesForm.setLlaveFinMedioRecepcion(Integer.parseInt(mapaConfOrdenes.get("LLAVE_FIN_MEDIO_RECEPCION")));			
			
			
			//ArrayList<HashMap<String, String>> datosPDF = lecturaPDF(ordenesForm, rutaDes,rutaPedimentos , nombreArchivo);
			listaPedimentos = lecturaPDF(ordenesForm, rutaTrabajo, rutaPedimentos , nombreArchivo);
			
						
		}catch(Exception e){
			Utils.imprimeLog("", e);
			//e.printStackTrace();
		}
		return listaPedimentos;
	}
	
	
	 public void lecturaPDF(String rutaArchivos, String rutaDestino, String nombreArchivoProcesar) {
		 PDDocument pd = null;
		 PDPage page = null;
		 StringBuffer sbContenido = new StringBuffer();
		 try {
			  ArrayList<PDPage> listaPaginas = new ArrayList<PDPage>();
			  String ruta=new String();
	          ruta = rutaArchivos + nombreArchivoProcesar; 
	          File filePDF = new File(ruta);
              pd = Loader.loadPDF(filePDF);
              Splitter splitter = new Splitter();
              List<PDDocument> splittedDocuments = splitter.split(pd);
//              PDDocument hojaDocument = null;
              PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
              
              for (int x = 0; x < splittedDocuments.size(); x++) {
            	  page = pd.getPage(x);
            	  sbContenido.append(pdfTextStripper.getText(splittedDocuments.get(x)));
            	  //System.err.println("Contenido=====>"+pdfTextStripper.getText(splittedDocuments.get(x)));
            	  int posIni = sbContenido.indexOf("NUM.   PEDIMENTO:") + 22; 
            	  int podFin = posIni + 24;
            	  String numPedimento = sbContenido.substring(posIni + 17, podFin);
            	 // System.err.println("numPedimento = "+numPedimento);
              }
		 }catch(Exception e) {
			 //e.printStackTrace();
			 Utils.imprimeLog("", e);
		 }finally {
			 try {
				 if (pd != null) {
					 pd.close(); 
	             }	 
			 }catch(Exception e) {
				 pd = null;
			 }
		 }
	 }
	 
	 public ArrayList<PedimentosForm> lecturaPDF(EtiquetasPedimentosForm ordenesForm, String rutaArchivos, String rutaDestino, String nombreArchivoProcesar){
			//ArrayList<HashMap<String, String>> datosResultado = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> mapaValores = null;
			 PDDocument pd = null;
			 int totDocumentos = 0;
			 ArrayList<PedimentosForm> listaPedimentos = new ArrayList<>();
			 PedimentosForm pedimentosForm = null;
			try{
			//	int i = 0;
		        String nombreArchivo="";
		        String cadFin = "";
		        String cadFin2 = "";
		        String cadFin3 = "";
		        StringBuffer sbContenido = new StringBuffer();
		        if (nombreArchivoProcesar == null){
		        	System.err.println("No hay archivos en la carpeta rutaDestino : "+rutaDestino);
		        	return null;
		    	}else {
		         
		          //List l = null;
		      //    PageFormat pageFormat = null;
		          PDPage page = null;
		          ArrayList<PDPage> listaPaginas = new ArrayList<PDPage>();
		         // for (int x=0;x<ficheros.length;x++){
		           // String ruta=new String();
		            // ruta = rutaArchivos + nombreArchivoProcesar; //SE ALMACENA LA RUTA DEL ARCHIVO A LEER.
		            cadFin = ordenesForm.getLlaveFinPDF();
		            cadFin2 = ordenesForm.getLlaveFinPDF2();
		            cadFin3 = ordenesForm.getLlaveFinPDF3();
		            // System.err.println("Llave Fin del DocumentoF**********"+cadFin);
		              try {
		            	  File filePDF = new File(rutaArchivos);
		                  pd = Loader.loadPDF(filePDF);
		                  Splitter splitter = new Splitter();
		                  List<PDDocument> splittedDocuments = splitter.split(pd);
//		                  PDDocument hojaDocument = null;
		                  PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
		                  
		                  for (int x = 0; x < splittedDocuments.size(); x++) {
		                	  //page = (PDPage) obj[i];
		                	  // System.err.println("Leyendo el documento PDF**********");
		                	  page = pd.getPage(x);
		                	  sbContenido.append(pdfTextStripper.getText(splittedDocuments.get(x)));
		                	//  System.err.println("***********************************************************************************");
		                	//    System.err.println(sbContenido.toString());
		                	//  System.err.println("***********************************************************************************");  
		                	  if (sbContenido.toString().indexOf(cadFin) > -1 || sbContenido.toString().indexOf(cadFin2) > -1 || sbContenido.toString().indexOf(cadFin3) > -1){ // encontro la cadena final
		                		  cadFin = "ENCONTRO_CADENA";
		                		  pedimentosForm = buscarValoresNuevo(ordenesForm, sbContenido.toString());
		                    	  nombreArchivo = pedimentosForm.getNumPedimento();
		                		  sbContenido.setLength(0);
		                		  listaPedimentos.add(pedimentosForm);
		                	  }
		                	  
		                	  listaPaginas.add(page);
		                	  if (cadFin.equals("ENCONTRO_CADENA")){
		                		  totDocumentos++;
		                		  logger.info("nombreArchivo===>"+nombreArchivo);
		                		  generaLibroNuevo(listaPaginas, nombreArchivo, rutaDestino);
		                		  listaPaginas = new ArrayList<PDPage>();
		                		  nombreArchivo = "";
		                		  cadFin = ordenesForm.getLlaveFinPDF();
		                	  }
		                  }
		                  pd.close();//CERRAMOS OBJETO ACROBAT
		              } catch (IOException e) {
		                  Utils.imprimeLog("", e);
		            	  //e.printStackTrace();
		              }
		        }
			}catch(Exception e){
				Utils.imprimeLog("", e);
				//e.printStackTrace();
			}finally {
				try {
					if (pd != null) {
						pd.close();
					}
				}catch(Exception e) {
					pd = null;
				}
			}
	        return listaPedimentos;
	    }
	 
	 
	 private void generaLibroNuevo(ArrayList<PDPage> listaPaginas, String nombre, String rutaDestino){
	        try{
	       	 // PDPage page
		       	  PDDocument document = new PDDocument();
		       	  for (int x = 0; x < listaPaginas.size(); x++){
		       		document.addPage( listaPaginas.get(x) );  
		       	  }
		       	  String nombrePDF = rutaDestino+nombre+".pdf";
		       	  File fileExist = new File(nombrePDF);
		       	  if (fileExist.exists()) {
		       		logger.info("Ya existe el documento PDF.**********");
		       	  }else {
		       		  document.save(rutaDestino+nombre+".pdf");// ruta donde deposita el archivo nuevo
				      document.close();  
		       	  }
		       	logger.info("Guardando documento en **********"+nombrePDF);
			      
	        }catch(Exception e){
	       	  Utils.imprimeLog("", e);
	        	//e.printStackTrace();
	        }
	  }
	 
	 
	 private PedimentosForm buscarValoresNuevo(EtiquetasPedimentosForm ordenesForm, String contenidoPDF){
			// HashMap<String, String> mapaValores = new HashMap<String, String>();
			 PedimentosForm pedimentosForm = new PedimentosForm();
			 try {
				 String NUM_PEDIMENTO = ordenesForm.getLlavePedimento();
				 String NUM_PEDIMENTO2 = ordenesForm.getLlavePedimento2();
				 String NUM_PEDIMENTO3 = ordenesForm.getLlavePedimento3();
				 String CVE_PEDIMENTO = ordenesForm.getLlaveClavePedimento();
				 String CVE_PEDIMENTO_2 = ordenesForm.getLlaveClavePedimento2();
				 String REGIMEN = ordenesForm.getLlaveRegimen();
				 String DTA = ordenesForm.getLlaveDTA();
				 String IVA = ordenesForm.getLlaveIVA();
				 String IVA_2 = ordenesForm.getLlaveIVA2();
				 String IGI = ordenesForm.getLlaveIGI();
				 String IGI_2 = ordenesForm.getLlaveIGI2();
				 String PRV = ordenesForm.getLlavePRV();
				 String IVAPRV = ordenesForm.getLlaveIVAPRV();
				 String IVAPRV_2 = ordenesForm.getLlaveIVAPRV2();
				 String EFECTIVO = ordenesForm.getLlaveEfectivo();
				 String EFECTIVO_2 = ordenesForm.getLlaveEfectivo2();
				 String EFECTIVO_3 = ordenesForm.getLlaveEfectivo3();
				 String OTROS = ordenesForm.getLlaveOtros();
				 String OTROS_2 = ordenesForm.getLlaveOtros2();
				 String OTROS_3 = ordenesForm.getLlaveOtros3();
				 
				 String TOTAL = ordenesForm.getLlaveTotal();
				 String BANCO = ordenesForm.getLlaveBanco();
				 String BANCO_2 = ordenesForm.getLlaveBanco2();
				 
				 String LINEA_CAPTURA = ordenesForm.getLlaveLineaCaptura();
				 String LINEA_CAPTURA_2 = ordenesForm.getLlaveLineaCaptura2();
				 String LINEA_CAPTURA_3 = ordenesForm.getLlaveLineaCaptura3();
				 String LINEA_CAPTURA_4 = ordenesForm.getLlaveLineaCaptura4();
				 
				 String IMPORTE_PAGO = ordenesForm.getLlaveImportePago();
				 String IMPORTE_PAGO_2 = ordenesForm.getLlaveImportePago2();
				 String IMPORTE_PAGO_3 = ordenesForm.getLlaveImportePago3();
				 
				 String FECHA_PAGO = ordenesForm.getLlaveFechaPago();
				 String FECHA_PAGO_2 = ordenesForm.getLlaveFechaPago2();
				 String FECHA_PAGO_3 = ordenesForm.getLlaveFechaPago3();
				 String FECHA_PAGO_4 = ordenesForm.getLlaveFechaPago4();
				 
				 String OPERACION_BANCARIA = ordenesForm.getLlaveOperacionBancaria();
				 String OPERACION_BANCARIA_2 = ordenesForm.getLlaveOperacionBancaria2();
				 String OPERACION_BANCARIA_3 = ordenesForm.getLlaveOperacionBancaria3();
				 String OPERACION_BANCARIA_4 = ordenesForm.getLlaveOperacionBancaria4();
				 
				 String TRANSACCION_SAT = ordenesForm.getLlaveTransaccionSat();
				 String TRANSACCION_SAT_2 = ordenesForm.getLlaveTransaccionSat2();
				 String TRANSACCION_SAT_3 = ordenesForm.getLlaveTransaccionSat3();
				 
				 String MEDIO_PRESENTACION = ordenesForm.getLlaveMedioPresentacion();
				 String MEDIO_PRESENTACION_2 = ordenesForm.getLlaveMedioPresentacion2();
				 String MEDIO_PRESENTACION_3 = ordenesForm.getLlaveMedioPresentacion3();
				 String MEDIO_PRESENTACION_4 = ordenesForm.getLlaveMedioPresentacion4();
				 
				 String MEDIO_RECEPCION = ordenesForm.getLlaveMedioRecepcion();
				 String MEDIO_RECEPCION_2 = ordenesForm.getLlaveMedioRecepcion2();
				 String MEDIO_RECEPCION_3 = ordenesForm.getLlaveMedioRecepcion3();
				 String MEDIO_RECEPCION_4 = ordenesForm.getLlaveMedioRecepcion4();
				 
				 /*
				 mapaValores.put("NUM_PEDIMENTO", "");
				 mapaValores.put("CVE_PEDIMENTO", "");
				 mapaValores.put("REGIMEN", "");
				 mapaValores.put("DTA", "");
				 mapaValores.put("IVA", "");
				 mapaValores.put("IGI", "");
				 mapaValores.put("PRV", "");
				 mapaValores.put("IVAPRV", "");
				 mapaValores.put("EFECTIVO", "");
				 mapaValores.put("OTROS", "");
				 mapaValores.put("TOTAL", "");
				 mapaValores.put("BANCO", "");
				 mapaValores.put("LINEA_CAPTURA", "");
				 mapaValores.put("IMPORTE_PAGO", "");
				 mapaValores.put("FECHA_PAGO", "");
				 mapaValores.put("OPERACION_BANCARIA", "");
				 mapaValores.put("TRANSACCION_SAT", "");
				 mapaValores.put("MEDIO_PRESENTACION", "");
				 mapaValores.put("MEDIO_RECEPCION", "");
				 */
				 String arrContenido [ ] = contenidoPDF.split("\n");
				 String linePDF = null;
				 
				 // StringBuffer sbDes = new StringBuffer();
				 		 
	    		 //  int posIni = sbContenido.indexOf(ordenesForm.getLlaveOrdenes()) + 22; 
	        	 //  int podFin = posIni + ordenesForm.getLlaveFinOrdenes(); 
	        	 // nombreArchivo = sbContenido.substring(posIni + 17, podFin);

				 
				 boolean entroPedimento = true;
				 boolean entroDTA = true;
				 boolean entroIVAPRV  = true;
				 boolean entroClavePedimento  = true;
				 boolean entroClaveRegiment  = true;
				 boolean entroClaveIva  = true;
				 boolean entroIgi  = true;
				 boolean entroPrv  = true;
				 boolean entroTotal  = true;
				 int cadIni = 0;
				 int cadFinal = 0;
				 for (int line=0; line < arrContenido.length; line++) {
					// logger.info("Line------>"+arrContenido[line]);
					 linePDF = arrContenido[line];
					 // ORDEN DE COMPRA
					//  System.err.println("linePDF_===>"+linePDF);
					 if ((linePDF.indexOf(NUM_PEDIMENTO) > -1  || linePDF.indexOf(NUM_PEDIMENTO2) > -1 || linePDF.indexOf(NUM_PEDIMENTO3) > -1) && entroPedimento){ // orden de compra
						 cadIni = 0;
						 cadFinal = 0;
						 if (linePDF.indexOf(NUM_PEDIMENTO) > -1) {
							 // cadIni = linePDF.indexOf(NUM_PEDIMENTO) + ordenesForm.getLlavePedimento().length() + 22;
							 // cadFinal = cadIni + ordenesForm.getLlaveFinPedimento();
							 cadIni = linePDF.indexOf(NUM_PEDIMENTO) + ordenesForm.getLlavePedimento().length() + 22;
							 cadFinal = cadIni + ordenesForm.getLlaveFinPedimento();
						 }else if (linePDF.indexOf(NUM_PEDIMENTO2) > -1) {
							 cadIni = linePDF.indexOf(NUM_PEDIMENTO2) + ordenesForm.getLlavePedimento().length() + 18;
							 cadFinal = cadIni + ordenesForm.getLlaveFinPedimento() + 1;
						 }else {
							 cadIni = linePDF.indexOf(NUM_PEDIMENTO3) + ordenesForm.getLlavePedimento().length() + 14;
							 cadFinal = cadIni + ordenesForm.getLlaveFinPedimento() + 1;
							 
						 }
						 // mapaValores.put("NUM_PEDIMENTO", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 entroPedimento = false;
						// logger.info("Numero de Pedimento...."+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setNumPedimento(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }
					 
					 if ((linePDF.indexOf(CVE_PEDIMENTO) > -1 || linePDF.indexOf(CVE_PEDIMENTO_2) > -1 )&& entroClavePedimento) { // orden de compra
						 if (linePDF.indexOf(CVE_PEDIMENTO) > -1) {
							 cadIni = linePDF.indexOf(CVE_PEDIMENTO) + ordenesForm.getLlaveClavePedimento().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinClavePedimento();
						 }else {
							 cadIni = linePDF.indexOf(CVE_PEDIMENTO_2) + ordenesForm.getLlaveClavePedimento().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinClavePedimento();
							 
						 }
						// logger.info("CVE_PEDIMENTO====>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("CVE_PEDIMENTO", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setCvePedimento(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 entroClavePedimento = false;
					 }

					 if (linePDF.indexOf(REGIMEN) > -1 && entroClaveRegiment ){ // orden de compra
						 cadIni = linePDF.indexOf(REGIMEN) + ordenesForm.getLlaveRegimen().length();
						 cadFinal = cadIni + ordenesForm.getLlaveFinRegimen();
						 // mapaValores.put("REGIMEN", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setRegimen(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 entroClaveRegiment = false;
					 }

					 if (linePDF.indexOf(DTA) > -1){ // orden de compra
						 cadIni = linePDF.indexOf(DTA) + ordenesForm.getLlaveDTA().length() + 5;
						 cadFinal = cadIni + ordenesForm.getLlaveFinDTA();
						 // mapaValores.put("DTA", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						// logger.info("DTA------>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setDta(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 entroDTA = false;
					 }
					 
					 
					 if ((linePDF.indexOf(IVA) > -1  || linePDF.indexOf(IVA_2) > -1)&& entroClaveIva){ // IVA
						 if (linePDF.indexOf(IVA) > -1) {
							 cadIni = linePDF.indexOf(IVA) + ordenesForm.getLlaveIVA().length() + 5;
							 cadFinal = cadIni + ordenesForm.getLlaveFinIVA();	 
						 }else {
							 cadIni = linePDF.indexOf(IVA_2) + ordenesForm.getLlaveIVA().length() + 5;
							 cadFinal = cadIni + ordenesForm.getLlaveFinIVA();
						 }
						// logger.info("IVA------>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("IVA", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setIva(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 entroClaveIva = false;
					 }

					 if ((linePDF.indexOf(IGI) > -1 || linePDF.indexOf(IGI_2) > -1) && entroIgi){ // IGI
						 if (linePDF.indexOf(IGI) > -1) {
							 cadIni = linePDF.indexOf(IGI) + ordenesForm.getLlaveIGI().length() + 5;
							 cadFinal = cadIni + ordenesForm.getLlaveFinIVA();	 
						 }else {
							 cadIni = linePDF.indexOf(IGI_2) + ordenesForm.getLlaveIGI().length() + 5;
							 cadFinal = cadIni + ordenesForm.getLlaveFinIVA();
						 }
						// logger.info("IGI------>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("IGI", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setIgi(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 entroIgi = false;
					 }
					 
					 if (linePDF.indexOf(PRV) > -1){ // PRV
						 cadIni = linePDF.indexOf(PRV) + ordenesForm.getLlavePRV().length() + 6;
						 cadFinal = cadIni + ordenesForm.getLlaveFinPRV();
						// logger.info("PRV------>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("PRV", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setPrv(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 entroPrv = false;
					 }
					 
					 if (linePDF.indexOf(IVAPRV) > -1 || linePDF.indexOf(IVAPRV_2) > -1){ // IVAPRV
						 
						 if (linePDF.indexOf(IVAPRV) > -1) {
							 cadIni = linePDF.indexOf(IVAPRV) + ordenesForm.getLlaveIVAPRV().length() + 7;
							 cadFinal = cadIni + ordenesForm.getLlaveFinIVAPRV();	 
						 }else {
							 cadIni = linePDF.indexOf(IVAPRV_2) + ordenesForm.getLlaveIVAPRV2().length() + 7;
							 cadFinal = cadIni + ordenesForm.getLlaveFinIVAPRV();
						 }
						 
						 // mapaValores.put("IVAPRV", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						// logger.info("IVAPRD------>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setIvaPRV(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 entroIVAPRV = false;
					 }
					 
					 if (linePDF.indexOf(EFECTIVO) > -1 || linePDF.indexOf(EFECTIVO_2) > -1 || linePDF.indexOf(EFECTIVO_3) > -1){ // EFECTIVO
						 if (linePDF.indexOf(EFECTIVO) > -1) {
							 cadIni = linePDF.indexOf(EFECTIVO) + ordenesForm.getLlaveEfectivo().trim().length() + 5;
							 cadFinal = cadIni + ordenesForm.getLlaveFinEfectivo();
						 }else if (linePDF.indexOf(EFECTIVO_2) > -1) {
							 cadIni = linePDF.indexOf(EFECTIVO_2) + ordenesForm.getLlaveEfectivo2().trim().length() + 5;
							 cadFinal = cadIni + ordenesForm.getLlaveFinEfectivo();
						 }else {
							 cadIni = linePDF.indexOf(EFECTIVO_3) + ordenesForm.getLlaveEfectivo3().trim().length() + 5;
							 cadFinal = cadIni + ordenesForm.getLlaveFinEfectivo();
							 
						 }
						 // mapaValores.put("EFECTIVO", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						// logger.info("EFECTIVO===>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setEfectivo(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }

					 if (linePDF.indexOf(OTROS) > -1 || linePDF.indexOf(OTROS_2) > -1 || linePDF.indexOf(OTROS_3) > -1){ // OTROS
						 if (linePDF.indexOf(OTROS) > -1) {
							 cadIni = linePDF.indexOf(OTROS) + ordenesForm.getLlaveOtros().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinOtros();
						 }else if (linePDF.indexOf(OTROS_2) > -1) {
							 cadIni = linePDF.indexOf(OTROS_2) + ordenesForm.getLlaveOtros2().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinOtros();
						 }else {
							 cadIni = linePDF.indexOf(OTROS_3) + ordenesForm.getLlaveOtros3().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinOtros();	 
						 }
						 
						 // mapaValores.put("OTROS", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						// logger.info("OTROS===>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setOtros(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }
					 
					 if (linePDF.indexOf(TOTAL) > -1 && entroTotal){ // OTROS
						 cadIni = linePDF.indexOf(TOTAL) + ordenesForm.getLlaveTotal().trim().length();
						 cadFinal = cadIni + ordenesForm.getLlaveFinTotal();
						 // mapaValores.put("TOTAL", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						// logger.info("TOTAL===>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setTotal(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 entroTotal = false;
						 
					 }
					 
					 if (linePDF.indexOf(BANCO) > -1 ||  linePDF.indexOf(BANCO_2) > -1){ // BANCO
						 
						 if (linePDF.indexOf(BANCO) > -1) {
							 cadIni = linePDF.indexOf(BANCO) + ordenesForm.getLlaveBanco().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinBanco();	 
						 }else {
							 cadIni = linePDF.indexOf(BANCO_2) + ordenesForm.getLlaveBanco2().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinBanco();
						 }
						// logger.info("BANCO====>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("BANCO", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setBanco(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }
					 
					 if (linePDF.indexOf(LINEA_CAPTURA) > -1 || linePDF.indexOf(LINEA_CAPTURA_2) > -1 || linePDF.indexOf(LINEA_CAPTURA_3) > -1 || linePDF.indexOf(LINEA_CAPTURA_4) > -1){ // LINEA DE CAPTURA
						 if (linePDF.indexOf(LINEA_CAPTURA) > -1) {
							 cadIni = linePDF.indexOf(LINEA_CAPTURA) + ordenesForm.getLlaveLineaCaptura().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinLineaCaptura();
						 }else if (linePDF.indexOf(LINEA_CAPTURA_2) > -1) {
							 cadIni = linePDF.indexOf(LINEA_CAPTURA_2) + ordenesForm.getLlaveLineaCaptura().trim().length() + 2;
							 cadFinal = cadIni + ordenesForm.getLlaveFinLineaCaptura();
						 }else if (linePDF.indexOf(LINEA_CAPTURA_3) > -1) {
							 cadIni = linePDF.indexOf(LINEA_CAPTURA_3) + ordenesForm.getLlaveLineaCaptura().trim().length() + 2;
							 cadFinal = cadIni + ordenesForm.getLlaveFinLineaCaptura();
						 }else {
							 cadIni = linePDF.indexOf(LINEA_CAPTURA_4) + ordenesForm.getLlaveLineaCaptura4().trim().length() + 2;
							 cadFinal = cadIni + ordenesForm.getLlaveFinLineaCaptura();
						 }
						// logger.info("LINEA_CAPTURA====>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("LINEA_CAPTURA", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setLineaCaptura(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }
					 
					 if (linePDF.indexOf(IMPORTE_PAGO) > -1 || linePDF.indexOf(IMPORTE_PAGO_2) > -1 || linePDF.indexOf(IMPORTE_PAGO_3) > -1){ // IMPORTE DE PAGO
						 if (linePDF.indexOf(IMPORTE_PAGO) > -1) {
							 cadIni = linePDF.indexOf(IMPORTE_PAGO) + ordenesForm.getLlaveImportePago().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinImportePago();
						 }else if (linePDF.indexOf(IMPORTE_PAGO_2) > -1) {
							 cadIni = linePDF.indexOf(IMPORTE_PAGO_2) + ordenesForm.getLlaveImportePago().trim().length() + 2;
							 cadFinal = cadIni + ordenesForm.getLlaveFinImportePago();
						 }else {
							 cadIni = linePDF.indexOf(IMPORTE_PAGO_3) + ordenesForm.getLlaveImportePago().trim().length() + 2;
							 cadFinal = cadIni + ordenesForm.getLlaveFinImportePago();
							 
						 }
						// logger.info("IMPORTE_PAGO====>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("IMPORTE_PAGO", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setImporte(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }
					 if (linePDF.indexOf(FECHA_PAGO) > -1  || linePDF.indexOf(FECHA_PAGO_2) > -1 || linePDF.indexOf(FECHA_PAGO_3) > -1 || linePDF.indexOf(FECHA_PAGO_4) > -1){ // FECHA DE PAGO
						 if (linePDF.indexOf(FECHA_PAGO) > -1) {
							 cadIni = linePDF.indexOf(FECHA_PAGO) + ordenesForm.getLlaveFechaPago().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinFechaPago();
						 }else if (linePDF.indexOf(FECHA_PAGO_2) > -1) {
							 cadIni = linePDF.indexOf(FECHA_PAGO_2) + ordenesForm.getLlaveFechaPago().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinFechaPago();
						 }else if (linePDF.indexOf(FECHA_PAGO_3) > -1) {
							 cadIni = linePDF.indexOf(FECHA_PAGO_3) + ordenesForm.getLlaveFechaPago().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinFechaPago();
						 }else {
							 cadIni = linePDF.indexOf(FECHA_PAGO_4) + ordenesForm.getLlaveFechaPago().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinFechaPago();
						 }
						// logger.info("FECHA_PAGO====>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("FECHA_PAGO", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setFechaPago(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }
					 
					 if (linePDF.indexOf(OPERACION_BANCARIA) > -1 || linePDF.indexOf(OPERACION_BANCARIA_2) > -1 || linePDF.indexOf(OPERACION_BANCARIA_3) > -1 || linePDF.indexOf(OPERACION_BANCARIA_4) > -1){ // OPERACION BANCARIA
						 if (linePDF.indexOf(OPERACION_BANCARIA) > -1) {
							 cadIni = linePDF.indexOf(OPERACION_BANCARIA) + ordenesForm.getLlaveOperacionBancaria().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinOperacionBancaria();
						 }else if (linePDF.indexOf(OPERACION_BANCARIA_2) > -1 ){
							 cadIni = linePDF.indexOf(OPERACION_BANCARIA_2) + ordenesForm.getLlaveOperacionBancaria().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinOperacionBancaria();
						 }else if (linePDF.indexOf(OPERACION_BANCARIA_3) > -1 ) {
							 cadIni = linePDF.indexOf(OPERACION_BANCARIA_3) + ordenesForm.getLlaveOperacionBancaria().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinOperacionBancaria();
						 }else {
							 cadIni = linePDF.indexOf(OPERACION_BANCARIA_4) + ordenesForm.getLlaveOperacionBancaria4().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinOperacionBancaria();
						 }
						// logger.info("OPERACION_BANCARIA====>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("OPERACION_BANCARIA", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setOperacionBancaria(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }
					 
					 if (linePDF.indexOf(TRANSACCION_SAT) > -1 || linePDF.indexOf(TRANSACCION_SAT_2) > -1 || linePDF.indexOf(TRANSACCION_SAT_3) > -1){ // TRANSACCION SAT
						 if (linePDF.indexOf(TRANSACCION_SAT) > -1) {
							 cadIni = linePDF.indexOf(TRANSACCION_SAT) + ordenesForm.getLlaveTransaccionSat().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinTransaccionSat();
						 }else if (linePDF.indexOf(TRANSACCION_SAT_2) > -1) {
							 cadIni = linePDF.indexOf(TRANSACCION_SAT_2) + ordenesForm.getLlaveTransaccionSat2().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinTransaccionSat();
						 }else {
							 cadIni = linePDF.indexOf(TRANSACCION_SAT_3) + ordenesForm.getLlaveTransaccionSat3().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinTransaccionSat();
						 }
						// logger.info("TRANSACCION_SAT====>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("TRANSACCION_SAT", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setTransaccionSAT(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }
					 
					 if (linePDF.indexOf(MEDIO_PRESENTACION) > -1 || linePDF.indexOf(MEDIO_PRESENTACION_2) > -1 || linePDF.indexOf(MEDIO_PRESENTACION_3) > -1 || linePDF.indexOf(MEDIO_PRESENTACION_4) > -1){ // MEDIO DE PRESENTACION
						 if (linePDF.indexOf(MEDIO_PRESENTACION) > -1 ) {
							 cadIni = linePDF.indexOf(MEDIO_PRESENTACION) + ordenesForm.getLlaveMedioPresentacion().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinMedioPresentacion();
						 }else if (linePDF.indexOf(MEDIO_PRESENTACION_2) > -1){
							 cadIni = linePDF.indexOf(MEDIO_PRESENTACION_2) + ordenesForm.getLlaveMedioPresentacion().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinMedioPresentacion();
						 }else if (linePDF.indexOf(MEDIO_PRESENTACION_3) > -1) {
							 cadIni = linePDF.indexOf(MEDIO_PRESENTACION_3) + ordenesForm.getLlaveMedioPresentacion().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinMedioPresentacion();
						 }else {
							 cadIni = linePDF.indexOf(MEDIO_PRESENTACION_4) + ordenesForm.getLlaveMedioPresentacion().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinMedioPresentacion();
						 }
						// logger.info("MEDIO_PRESENTACION====>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("MEDIO_PRESENTACION", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setMedioPresentacion(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }
					 
					 if (linePDF.indexOf(MEDIO_RECEPCION) > -1 || linePDF.indexOf(MEDIO_RECEPCION_2) > -1 || linePDF.indexOf(MEDIO_RECEPCION_3) > -1 || linePDF.indexOf(MEDIO_RECEPCION_4) > -1){ // MEDIO DE PRESENTACION
						 if (linePDF.indexOf(MEDIO_RECEPCION) > -1 ) {
							 cadIni = linePDF.indexOf(MEDIO_RECEPCION) + ordenesForm.getLlaveMedioRecepcion().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinMedioRecepcion();
							 
						 }else if (linePDF.indexOf(MEDIO_RECEPCION_2) > -1 ){
							 cadIni = linePDF.indexOf(MEDIO_RECEPCION_2) + ordenesForm.getLlaveMedioRecepcion().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinMedioRecepcion();
						 }else if (linePDF.indexOf(MEDIO_RECEPCION_3) > -1 ) {
							 cadIni = linePDF.indexOf(MEDIO_RECEPCION_3) + ordenesForm.getLlaveMedioRecepcion().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinMedioRecepcion();
						 }else {
							 cadIni = linePDF.indexOf(MEDIO_RECEPCION_4) + ordenesForm.getLlaveMedioRecepcion().trim().length();
							 cadFinal = cadIni + ordenesForm.getLlaveFinMedioRecepcion();
						 }
						// logger.info("MEDIO_RECEPCION====>"+Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 // mapaValores.put("MEDIO_RECEPCION", Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
						 pedimentosForm.setMedioRecepcion(Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal )));
					 }

				 }
				 // logger.info("mapaValores===>"+mapaValores);
				// logger.info("sbDes--------------->"+sbDes.toString());
				 // System.err.println("mapaValores:="+mapaValores);
			 } catch (Exception e) {
				  Utils.imprimeLog("", e);
				 //e.printStackTrace();
			 }
			return pedimentosForm;
		 }

	 
	 public int grabarPedimento(Connection con, String esquema, PedimentosForm pedimentosForm, String usuarioHTTP){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			int resultado = 0;
			try{ 		
				stmt = con.prepareStatement(PedimentosQuerys.getAltaPedimento(esquema));
				stmt.setString(1, pedimentosForm.getNumPedimento());
				stmt.setString(2, pedimentosForm.getCvePedimento());
				stmt.setString(3, pedimentosForm.getRegimen());
				stmt.setString(4, pedimentosForm.getDta());
				stmt.setString(5, pedimentosForm.getIva());
				stmt.setString(6, pedimentosForm.getIgi());
				stmt.setString(7, pedimentosForm.getPrv());
				stmt.setString(8, pedimentosForm.getIvaPRV());
				stmt.setString(9, pedimentosForm.getEfectivo());
				stmt.setString(10, pedimentosForm.getOtros());
				stmt.setString(11, pedimentosForm.getTotal());
				stmt.setString(12, pedimentosForm.getBanco());
				stmt.setString(13, pedimentosForm.getLineaCaptura());
				stmt.setString(14, pedimentosForm.getImporte());
				stmt.setString(15, UtilsFechas.regresaFecha(pedimentosForm.getFechaPago()));
				stmt.setString(16, pedimentosForm.getOperacionBancaria());
				stmt.setString(17, pedimentosForm.getTransaccionSAT());
				stmt.setString(18, pedimentosForm.getMedioPresentacion());
				stmt.setString(19, pedimentosForm.getMedioRecepcion());
				stmt.setString(20, usuarioHTTP);
				int cant = stmt.executeUpdate();
				/*
	            if(cant > 0){
	                rs = stmt.getGeneratedKeys();
	                if(rs.next())
	                    resultado = rs.getInt(1);
	            	resultado = cant;
	            }
	            */
				resultado = cant;
			}catch(SQLException sql){
				resultado = sql.getErrorCode();
				if (resultado == 1062){
					
				}else {
					logger.info("resultado----->"+resultado);	
				}
				
				Utils.imprimeLog("", sql);
			}catch(Exception e){
				Utils.imprimeLog("", e);
				resultado = 100;
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
			return resultado;

		}

	 
	 
	 @SuppressWarnings("unchecked")
		public Map<String, Object > detallePedimentoZIP(Connection con, String esquema, String cadRegistros, String noPedimento) {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        JSONObject jsonobj = new JSONObject();
	        JSONArray jsonArray = new JSONArray();
	        Map<String, Object > mapaRes = new HashMap<String, Object>();
	        StringBuffer sbQueryFinal = new StringBuffer();
	        
	        try{
	        	
	        	StringBuffer sbQuery = new StringBuffer(PedimentosQuerys.getConsultaPedimentos(esquema));
	        	if (cadRegistros.length() > 0) {
	        		String arrRegistros [] = cadRegistros.split(";");
	        		sbQuery.append("where NUM_PEDIMENTO in (");
	        		for (int x = 0; x < arrRegistros.length; x++){
	        			sbQuery.append("?,");
	            	}
	        		sbQueryFinal.append(sbQuery.substring(0, sbQuery.length() - 1));
	        		sbQueryFinal.append(")");
	        		stmt = con.prepareStatement(sbQueryFinal.toString());
	        		
	        		int numParam=1;
	        		for (int x = 0; x < arrRegistros.length; x++){
	        			stmt.setInt(numParam++, Integer.parseInt( arrRegistros[x] ));
	        		}
	        	}
	        	
	        	logger.info("stmt===>"+stmt);
	        	rs = stmt.executeQuery();
	            
	            while(rs.next()){
	            	jsonobj.put("ID_REGISTRO", rs.getInt(1));
					jsonobj.put("NUM_PEDIMENTO", Utils.noNuloNormal(rs.getString(2)));
					jsonobj.put("CVE_PEDIMENTO", Utils.noNuloNormal(rs.getString(3)));
					jsonobj.put("REGIMEN", Utils.noNuloNormal(rs.getString(4)));
					jsonobj.put("DTA", Utils.noNuloNormal(rs.getString(5)));
					jsonobj.put("IVA", Utils.noNuloNormal(rs.getString(6)));
					jsonobj.put("IGI", Utils.noNuloNormal(rs.getString(7)));
					jsonobj.put("PRV", Utils.noNuloNormal(rs.getString(8)));
					jsonobj.put("IVAPRV", Utils.noNuloNormal(rs.getString(9)));
					jsonobj.put("EFECTIVO", Utils.noNuloNormal(rs.getString(10)));
					jsonobj.put("OTROS", Utils.noNuloNormal(rs.getString(11)));
					jsonobj.put("TOTAL", Utils.noNuloNormal(rs.getString(12)));
					jsonobj.put("BANCO", Utils.noNuloNormal(rs.getString(13)));
					jsonobj.put("LINEA_CAPTURA", Utils.noNuloNormal(rs.getString(14)));
					jsonobj.put("IMPORTE_PAGO", Utils.noNuloNormal(rs.getString(15)));
					jsonobj.put("FECHA_PAGO", Utils.noNuloNormal(rs.getString(16)));
					jsonobj.put("NUMERO_OPERACION", Utils.noNuloNormal(rs.getString(17)));
					jsonobj.put("NUMERO_SAT", Utils.noNuloNormal(rs.getString(18)));
					jsonobj.put("MEDIO_PRESENTACION", Utils.noNuloNormal(rs.getString(19)));
					jsonobj.put("MEDIO_RECEPCION", Utils.noNuloNormal(rs.getString(20)));
						
					jsonArray.add(jsonobj);  
		        	jsonobj = new JSONObject();
	            }
				mapaRes.put("detalle", jsonArray);
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("", e);
	        }finally{
		        try{
		            if(rs != null)
		                rs.close();
		            rs = null;
		            if(stmt != null)
		                stmt.close();
		            stmt = null;
		        }catch(Exception e){
		            stmt = null;
		        }
	        }
	        return mapaRes;
	    }
		
}
