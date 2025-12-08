package com.siarex247.validaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.Concepto;
import com.itextpdf.xmltopdf.Conceptos;
import com.itextpdf.xmltopdf.Retencion;
import com.itextpdf.xmltopdf.Traslado;
import com.siarex247.prodigia.api.ProdigiaClient;
import com.siarex247.prodigia.data.ConsultaComprobante;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesQuerys;

import Services.StatusCfdi.StatusCfdiService;
import Utils.Responses.StatusCfdi.StatusCfdiResponse;

public class UtilsValidaciones {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	public  static HashMap<String, String> getMonedaMEX(){
		HashMap<String, String> mapaMEX = new HashMap<String, String>();
		try{
			mapaMEX.put("MXN", "MXN;MXN");
			mapaMEX.put("NACIONAL", "NACIONAL;MXN");
			mapaMEX.put("Peso_Mexicano", "Peso Mexicano;MXN");
			mapaMEX.put("Pesos_Mexicanos", "Pesos Mexicanos;MXN");
			mapaMEX.put("Pesos", "Pesos;MXN");
			mapaMEX.put("PESOS", "PESOS;MXN");
			mapaMEX.put("M.N", "M.N;MXN");
			mapaMEX.put("m.n", "m.n;MXN");
			mapaMEX.put("m.n.", "m.n.;MXN");
			mapaMEX.put("M.N.", "M.N.;MXN");
			mapaMEX.put("MONEDA_NACIONAL", "MONEDA NACIONAL;MXN");
			mapaMEX.put("MXP", "MXP;MXN");
			mapaMEX.put("Peso", "Peso;MXN");
			mapaMEX.put("mnx", "mnx;MXN");
			mapaMEX.put("MNX", "MNX;MXN");
			
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return mapaMEX;
	}
	
	
	public  static HashMap<String, String> getMonedaUSA(){
		HashMap<String, String> mapaUSA = new HashMap<String, String>();
		try{
			mapaUSA.put("Dolar_Americano", "Dolar Americano;USD");
			mapaUSA.put("DOLARES", "DOLARES;USD");
			mapaUSA.put("Dolares", "Dolares;USD");
			mapaUSA.put("M.A.", "M.A.;USD");
			mapaUSA.put("USD", "USD;USD");
			mapaUSA.put("usd", "usd;USD");
			mapaUSA.put("MA", "MA;USD");
			mapaUSA.put("ma", "ma;USD");
			mapaUSA.put("DOLAR", "DOLAR;USD");
			mapaUSA.put("Dolar_Americano", "Dolar Americano;USD");
			mapaUSA.put("dolar", "dolar;USD");
			mapaUSA.put("m.a.", "m.a.;USD");
			mapaUSA.put("DLS", "DLS;USD");
			mapaUSA.put("dls", "dls;USD");
			mapaUSA.put("Dls", "Dls;USD");
			mapaUSA.put("Dlls", "Dlls;USD");
			mapaUSA.put("dlls", "dlls;USD");
			mapaUSA.put("M.A", "M.A;USD");
			mapaUSA.put("M.a", "M.a;USD");
			mapaUSA.put("u.s.d.", "u.s.d.;USD");
			mapaUSA.put("U.S.D.", "U.S.D.;USD");
			mapaUSA.put("DOLARES_AMERICANOS_USD", "DOLARES AMERICANOS USD;USD");
			mapaUSA.put("Dólar", "Dólar;USD");
			
			
			// logger.info("mapaUSA----->"+mapaUSA);
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return mapaUSA;
	}
	
	
	public static String validaDesMoneda(String monedaXML){
		String monedaXMLSinEspacios = "SIN_MONEDA";
		String desMoneda = "";
		try{
			monedaXMLSinEspacios = monedaXML.replaceAll(" ", "_");
			//logger.info("monedaXMLSinEspacios----->"+monedaXMLSinEspacios);
			monedaXML = monedaXMLSinEspacios; 
			if ("".equals(monedaXML)){
				monedaXML = "MXN";
			}
			if (monedaXML.toLowerCase().indexOf("lar_americano") > -1){
				monedaXML = "Dolar_Americano";
			}
			if (monedaXML.indexOf("lares") > -1){
				monedaXML = "Dolares";
			}
			
			desMoneda = Utils.noNulo(getMonedaMEX().get(monedaXML));
			if ("".equals(desMoneda)){
				desMoneda = Utils.noNulo(getMonedaUSA().get(monedaXML));
			}
			
		}catch(Exception e){
			monedaXML = "SIN_MONEDA";
		}
		return desMoneda;
	}
	 
	
	public static HashMap<String, String> getImporteImpuestoISRNuevo(List<Retencion> listRetencion){
		HashMap<String, String> mapaISR = new HashMap<String, String>();
		try{
			mapaISR.put("ISR", "0");
			mapaISR.put("IVA", "0");
			
			if(listRetencion != null) {
				Retencion retencion = null;
				for (int x = 0; x < listRetencion.size(); x++) {
					retencion = listRetencion.get(x);
					if ("001".equalsIgnoreCase(retencion.getImpuesto())) {
						mapaISR.put("ISR", String.valueOf(retencion.getImporte()));
					}else if ("002".equalsIgnoreCase(retencion.getImpuesto())) {
						mapaISR.put("IVA", String.valueOf(retencion.getImporte()));
					}
				}
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
			mapaISR.put("ISR", "0");
			mapaISR.put("IVA", "0");
		}
		
		return mapaISR;
	}	
	
	
	@SuppressWarnings("unchecked")
	public static JSONArray getConceptosNuevo(Comprobante _comprobante){
		JSONArray jsonArray = new JSONArray();
		try{
			
			Conceptos listaConceptos = _comprobante.getConceptos();
			Concepto concepto = null;
			
			for (int x = 0; x < listaConceptos.getConcepto().size(); x++){
				JSONObject jsonobj = new JSONObject();
				concepto = listaConceptos.getConcepto().get(x);
				jsonobj.put("ClaveProdServ", concepto.getClaveProdServ());
				jsonobj.put("Importe", concepto.getImporte());
				jsonobj.put("ValorUnitario", concepto.getValorUnitario());
				jsonobj.put("Descripcion", concepto.getDescripcion());
				jsonobj.put("Unidad", concepto.getUnidad());
				jsonobj.put("ClaveUnidad", concepto.getClaveUnidad());
				jsonobj.put("Cantidad", concepto.getCantidad());
				jsonobj.put("NoIdentificacion", concepto.getNoIdentificacion());
				
				jsonArray.add(jsonobj);
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		
		return jsonArray;
	}
	
	public static HashMap<String, String> getImporteTransladosISRNuevo(List<Traslado> listaTranslados){
		HashMap<String, String> mapaISR = new HashMap<String, String>();
		try{
			mapaISR.put("ISR", "0");
			mapaISR.put("IVA", "0");
			
			if(listaTranslados != null) {
				Traslado translado = null;
				for (int x = 0; x < listaTranslados.size(); x++) {
					translado = listaTranslados.get(x);
					if ("001".equalsIgnoreCase(translado.getImpuesto())) {
						mapaISR.put("ISR", String.valueOf(translado.getImporte()));
					}else if ("002".equalsIgnoreCase(translado.getImpuesto())) {
						mapaISR.put("IVA", String.valueOf(translado.getImporte()));
					}
				}
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
			mapaISR.put("ISR", "0");
			mapaISR.put("IVA", "0");
		}
		
		return mapaISR;
	}
	
	
	public static boolean validaTiposMoneda(String tipoXML, String tipoBD ){
		try{
			if (tipoBD.equalsIgnoreCase(tipoXML)){
				return true;
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return false;
	}
	
	
	public static String validaAnexo24(String monedaXML, String tipoCambio, String anexo24BD, String monedaBD){
		String valAnexo24 = "OK";
		try{
			// logger.info("anexo24---->"+anexo24BD);
			if ("DOLARES".equalsIgnoreCase(monedaBD) && "1".equalsIgnoreCase(anexo24BD)){
				if ("USD".equalsIgnoreCase(monedaXML)){
					// logger.info("tipoCambio---->"+tipoCambio);
					try{
						double tipoCambioXML = Double.parseDouble(tipoCambio);
						// logger.info("tipoCambioXML---->"+tipoCambioXML);
						if (tipoCambioXML > 1){
							valAnexo24 = "OK";
						}else{
							valAnexo24 = "NG";
						}
					}catch(Exception e){
						valAnexo24 = "NG";
					}
				}else{
					valAnexo24 = "NG";
				}
			}else if ("PESOS".equalsIgnoreCase(monedaBD) && "1".equalsIgnoreCase(anexo24BD)){
				if ("MXN".equalsIgnoreCase(monedaXML)){
					valAnexo24 = "OK";
				}else{
					valAnexo24 = "NG";
				}
			}
			// logger.info("valAnexo24---->"+valAnexo24);
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return valAnexo24;
	}
	
	
	public static String obtenerFechaPago(HashMap<String, String> mapaConf) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Iterator<String>  llavesMapa =  mapaConf.keySet().iterator();
			String nombreLlave = null;
			String cadFecha [] = null;
			String bandMes = null;
			while (llavesMapa.hasNext()) {
				nombreLlave = llavesMapa.next();
				if (nombreLlave.indexOf("FECHA_PAGO") > -1) {
					cadFecha = mapaConf.get(nombreLlave).split(";");
					int diaIni = Integer.parseInt(cadFecha[0]);
					int diaFin = Integer.parseInt(cadFecha[1]);
					int diaPago = Integer.parseInt(cadFecha[2]);
					bandMes = cadFecha[3];
					
					int diaActual = Utils.obtenerDiaActual();
					if (diaActual >= diaIni && diaActual <= diaFin ) {
						int mesActual = Utils.obtenerMesActual();
						if ("N".equalsIgnoreCase(bandMes)) {
							mesActual--;
						}
						Date fechaPago = Utils.obtenerDiaPago(mesActual, diaPago);
						return formatDate.format(fechaPago);
					}
				}
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return null;
	}
	
	
	
	public static String validaFecha(Connection con, String esquema, String fechaFact, String factura, String tipoCierre){
		long fechaHasta = 0;
		long fechaApartir = 0;
		long fechaFactura = 0;
		long fechaActual = 0;
		try{
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
			Date fecha = new Date();
			String fechaActualSimple = "";
			fechaActualSimple = formatDate.format(fecha);
			int anio = Integer.parseInt(fechaFact.substring(0, 4));
			
			
			String [] fechasRecepcion = obtenerFechas(con, esquema, anio, tipoCierre);
			StringBuffer sbFecha = new StringBuffer();
			if (!fechasRecepcion[1].equals("")){
				fechaApartir = Long.parseLong(Utils.replaceCaracteresEspeciales(sbFecha.append(fechasRecepcion[1].substring(0,4))
						 .append(fechasRecepcion[1].substring(5,7))
						 .append(fechasRecepcion[1].substring(8,10)).toString()));
			}
			
			sbFecha.setLength(0);
			if (!fechasRecepcion[3].equals("")){
				fechaHasta = Long.parseLong(Utils.replaceCaracteresEspeciales(sbFecha.append(fechasRecepcion[3].substring(0,4))
						 .append(fechasRecepcion[3].substring(5,7))
						 .append(fechasRecepcion[3].substring(8,10)).toString()));
			}
			
			sbFecha.setLength(0);
			if (!fechaFact.equals("")){
				fechaFactura = Long.parseLong(Utils.replaceCaracteresEspeciales(sbFecha.append(fechaFact.substring(0,4))
						 .append(fechaFact.substring(5,7))
						 .append(fechaFact.substring(8,10)).toString()));
			}
			
			sbFecha.setLength(0);
			
			if (!fechaActualSimple.equals("")){
				fechaActual = Long.parseLong(fechaActualSimple);
			}

			logger.info("Fecha FacturaLong---->"+fechaFactura);
			logger.info("Fecha ApartirLong---->"+fechaApartir);
			logger.info("Fecha HastaLong---->"+fechaHasta);
			logger.info("Fecha ActualLong ---->"+fechaActual);

			
			boolean cumpleHasta = false;
			boolean cumpleApartir = false;
			boolean bandNoValidar = true;
			
			// fechaFactura = 20160103
			// fechaHasta   = 20161218
			
			// fechaApartir = 20160105
			// fechaActual  = 20160104
			
			// true, si cumplio no se manda mensaje
			// false, no cumplio no se manda mensaje
			
			if (fechaHasta > 0 && fechaHasta >= fechaFactura){
				if (fechaHasta >= fechaActual){
					cumpleHasta = true;
				}else{
					bandNoValidar = false;
				}
			}else{
				bandNoValidar = false;
			}
			
			if (bandNoValidar){ // significa que cumplio la fecha hasta y se valida las de apartir
				if (fechaApartir > 0 && fechaApartir <= fechaFactura){
					if (fechaApartir <= fechaActual){
						cumpleApartir = true;
					}
				}
			}
			
			if (!cumpleHasta && fechaHasta > 0){
				String mensajeHasta = fechasRecepcion[4].replace("<<factura>>", factura)
										.replace("<<fecha_hasta>>", fechasRecepcion[6]);
				logger.info("mensajeHasta ---->"+mensajeHasta);
				return mensajeHasta;
			}else if (!cumpleApartir && fechaApartir > 0){
				String mensajeApartir = fechasRecepcion[2].replace("<<factura>>", factura)
						.replace("<<fecha_apartir>>", fechasRecepcion[5]);
				logger.info("mensajeApartir---->"+mensajeApartir);
				return mensajeApartir;
			}else{
				return "OK";
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
			return "NG";
		}
	}
	
	
	
	public static String [] obtenerFechas(Connection con, String esquema, int anio, String tipoCierre){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String [] resultado = {"","","","","", "",""};
		try{
			logger.info("************ OBTENIENDO LAS FECHAS DE RECEPCION ******************");
			stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryFechasCierreQuery(esquema));
			stmt.setInt(1, anio);
			stmt.setString(2, tipoCierre);
			rs = stmt.executeQuery();
			//SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
			if (rs.next()){
				resultado[0] = Utils.noNulo(rs.getString(1));
				resultado[1] = Utils.noNulo(rs.getString(2));
				resultado[2] = Utils.noNuloNormal(rs.getString(3));
				resultado[3] = Utils.noNulo(rs.getString(4));
				resultado[4] = Utils.noNuloNormal(rs.getString(5));
				if (!resultado[1].equals("")){
					resultado[5] =  UtilsFechas.formatFechaddMMyyyy(resultado[1]);
				}
				if (!resultado[3].equals("")){
					resultado[6] =  UtilsFechas.formatFechaddMMyyyy(resultado[3]);
				}
				
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
		return resultado;
	}

	
	
	public static  boolean algoritoTotal(double totalCompararXMLFinal, double totalComparar, String limiteTolerancia){
		boolean bandTotal = false;
		try{
			double totTolerancia = 0;
	    	boolean bandPrimera = false;
	    	boolean bandSegunda = false;
	    	boolean bandTercera = false;
			try{
				totTolerancia = Double.parseDouble(limiteTolerancia);
			}catch(Exception e){
				totTolerancia = 0;
			}
			totalCompararXMLFinal = (totalCompararXMLFinal + totTolerancia);
			logger.info("totalCompararXMLFinal------>"+totalCompararXMLFinal);
			logger.info("totalComparar------>"+totalComparar);
			if (totalCompararXMLFinal >= totalComparar ){
				bandPrimera = true;
			}
			
			if (totalComparar <= totalCompararXMLFinal){
				bandSegunda = true;
			}
			totalCompararXMLFinal = (totalCompararXMLFinal - totTolerancia);
			logger.info("totalCompararXMLFinal (2)------>"+totalCompararXMLFinal);
			if (totalCompararXMLFinal <= totalComparar ){
				bandTercera = true;
			}else if (totalCompararXMLFinal <= (totalComparar +totTolerancia)){
				bandTercera = true;
			}
			
			if (totalComparar >= totalCompararXMLFinal){
				bandTercera = true;
			}else if ((totalComparar + totTolerancia) >= totalCompararXMLFinal){
				bandTercera = true;
			}
			logger.info("bandPrimera------>"+bandPrimera);
			logger.info("bandSegunda------>"+bandSegunda);
			logger.info("bandTercera------>"+bandTercera);
			
			if (bandPrimera && bandSegunda && bandTercera){
				bandTotal = true;
			}
			
		}catch(Exception e){
			Utils.imprimeLog("Validando los totales", e);
		}
		
		return bandTotal;
	}
	
	
	
	public static StringBuffer validaFechaPado(String fechaPago) {
		StringBuffer sbFechaPago = null;
		try {
			// 2017:7:1
			if (!fechaPago.equals("") && fechaPago.length() > 7) {
				sbFechaPago = new StringBuffer();
				String cadFecha [] = fechaPago.split("-");
				int year = Integer.parseInt(cadFecha[0]); 
				int mes  = Integer.parseInt(cadFecha[1]);
				int dia  = Integer.parseInt(cadFecha[2]);
				
				sbFechaPago.append(year).append("-");
				if (mes <= 9) {
					sbFechaPago.append("0").append(mes).append("-");
				}else {
					sbFechaPago.append(mes).append("-");
				}
				if (dia <= 9) {
					sbFechaPago.append("0").append(dia).append("-");
				}else {
					sbFechaPago.append(dia).append("-");
				}
			}
			 
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbFechaPago;
	}
}
