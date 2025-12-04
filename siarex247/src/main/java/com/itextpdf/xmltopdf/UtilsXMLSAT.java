package com.itextpdf.xmltopdf;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;


public class UtilsXMLSAT {

	static SimpleDateFormat formatddMMMyyyy = new SimpleDateFormat("dd-MMM-yyyy");
	static SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	static SimpleDateFormat formatoFechaTimestamp = getFormatoFechaTimestamp();
	final static DecimalFormat serie = new DecimalFormat("000000");
	final static DecimalFormat linea = new DecimalFormat("0000");
	private static final String ORIGINAL = ",.?ÃƒÆ’";
	private static final String REPLACEMENT = "    ";

	public static final Logger logger = Logger.getLogger("siarex");
	
	public static String replaceCadena(String str) {
	if (str == null) {
	    return null;
	}
	char[] array = str.toCharArray();
	for (int index = 0; index < array.length; index++) {
	    int pos = ORIGINAL.indexOf(array[index]);
	    if (pos > -1) {
	        array[index] = REPLACEMENT.charAt(pos);
	    }
	}
	return new String(array);
	}
	
	public static String regresaCaracteresFormato(String cadenaOriginal, String razonSocial, String rfc, String usuario, String pwd){
		String cadenaFinal = null;
		try{
			cadenaFinal = cadenaOriginal.replace("|razonSocial|",razonSocial)
								.replace("|rfc|",rfc)
								.replace("|usuario|",usuario)
								.replace("|pwd|",pwd);
			return cadenaFinal;
		}catch(Exception e){
			imprimeLog("", e);
			return cadenaOriginal;
		}
	}
	
	
	public static void imprimeLog(String msg, Exception e){
		Writer result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		logger.info(msg + " : "+result.toString());
	}
	
	
	public static Object noNulo (Object cadena){
		return validaNulo(cadena).toString().trim();
	}
	
	
	
	public static String noNulo (String cadena){
		return validaNulo(cadena).toString().trim().toUpperCase();
	}
	
	public static String noNuloNormal (String cadena){
		return validaNulo(cadena).toString().trim();
	}
	
	public static Object noNuloUpperCase (Object cadena){
		return validaNulo(cadena).toString().trim().toUpperCase();
	}
	
	
	
	private static Object validaNulo (Object cadena){
		if (cadena == null){
			cadena = "";
		}
		return cadena;
	}
	
	public static int noNuloINT(String cadena){
		if (cadena == null || "".equals(cadena)){
			cadena = "0";
		}
		return Integer.parseInt(cadena.trim());
	}

	public static int noNuloINT(Object cadena){
		if (cadena == null){
			cadena = "0";
		}
		return Integer.parseInt(cadena.toString());
	}

	
	public static double noNuloDouble(String cadena){
		if (cadena == null || "".equals(cadena)){
			cadena = "0";
		}
		return Double.parseDouble(cadena.trim());
	}

	public static double noNuloDouble(Object cadena){
		try {
			if (cadena == null){
				cadena = "0";
			}
			return Double.parseDouble(cadena.toString());	
		}catch(Exception e) {
			cadena = "0";
		}
		return Double.parseDouble(cadena.toString());
	}
	

	public static float noNuloFloat(String cadena){
		if (cadena == null || "".equals(cadena)){
			cadena = "0";
		}
		return Float.parseFloat(cadena.trim());
	}

	public static float noNuloFloat(Object cadena){
		try {
			if (cadena == null){
				cadena = "0";
			}
			return Float.parseFloat(cadena.toString());	
		}catch(Exception e) {
			cadena = "0";
		}
		return Float.parseFloat(cadena.toString());
	}

	
	public static String eliminaCaracter(String nombreArchivo){
		String nombreArchivoFinal = nombreArchivo.replaceAll("&=", "");
		return nombreArchivoFinal ;	
	}
	
	
	public static String getFechaddMMMyyyy(Date fecha){
		if (fecha == null){
			return capitalizaMes(getFechaddMMMyyyy());
		}else{
			return capitalizaMes(formatddMMMyyyy.format(fecha));
			
		}
	}
	
	private static String getFechaddMMMyyyy(){
		Date fecha = new Date();
		return formatddMMMyyyy.format(fecha);
	}
	
	public static String getFechayyyyMMdd(){
		Date fecha = new Date();
		return formatyyyyMMdd.format(fecha);
	}
	
	
	
	
	public static String getFechaddMMMyyyyHHss(java.sql.Timestamp fecha){
		if (fecha == null){
			return capitalizaMes(getFechaddMMMyyyy());
		}else if ("0001-01-01 00:00:00.0".equals(fecha.toString())){
			return "";
		}else{
			return capitalizaMes(formatoFechaTimestamp.format(fecha));
		}
	}
	
	public static String capitalizaMes(String fechaTimestamp){
		if (fechaTimestamp.length() >= 10){
			return fechaTimestamp.substring(0,3) +  fechaTimestamp.substring(3,4).toUpperCase() + fechaTimestamp.substring(4); 
		}
		return "";
	}
	
	
	 public static String regresaFecha(String fecha)
	    {
	        String fechaInicio = "";
	        String dia = fecha.substring(0, 2);
	        String mes = fecha.substring(3, 5);
	        String anio = fecha.substring(6);
	        fechaInicio = anio.concat("-").concat(mes).concat("-").concat(dia);
	        return fechaInicio;
	    }
	
	 
	 public static String regresaFecha2(String fecha)
	    {    
	        String fechaInicio = ""; 
	        String mes = fecha.substring(0, 2);
	        String dia = fecha.substring(3, 5);
	        String anio = fecha.substring(6);
	        fechaInicio = anio.concat("-").concat(mes).concat("-").concat(dia);
	        return fechaInicio;
	    }
	 
	 
	 public static String regresaFecha3(String fecha)
	    {
	        String fechaInicio = "";
	        String mes = fecha.substring(0, 2);
	        String dia = fecha.substring(3, 5);
	        String anio = fecha.substring(6,10);
	        fechaInicio = anio.concat("-").concat(mes).concat("-").concat(dia);
	        return fechaInicio;
	    }
	 
	 
	 public static Date sumarRestarHorasFecha(Date fecha, int HORAS){
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(fecha); // Configuramos la fecha que se recibe
       calendar.add(Calendar.HOUR_OF_DAY, HORAS);  // numero de dÃƒÂ­as a aÃƒÂ±adir, o restar en caso de dÃƒÂ­as<0
       return calendar.getTime(); // Devuelve el objeto Date con los nuevos dÃƒÂ­as aÃƒÂ±adidos
	 }

	
	public static SimpleDateFormat getFormatoFechaTimestamp()
    {
		String formatFecha = null; 
		formatFecha = "dd-MMM-yyyy k:mm:ss";
		return new SimpleDateFormat(formatFecha);
    }
	
	
	public static SimpleDateFormat getFormatoFechaDate()
    {
		String formatFecha = null; 
		formatFecha = "dd-MMM-yyyy";
		return new SimpleDateFormat(formatFecha);
    }
	
	public static SimpleDateFormat getFormatoFechayyyymmdd()
    {
		String formatFecha = null; 
		formatFecha = "yyyy-MM-dd";
		return new SimpleDateFormat(formatFecha);
    }
	
	
	public static String getPerfil(int numPerfil)
    {
		if (numPerfil == 1){
			return "ADMINISTRADOR";
		}else if (numPerfil == 2){
			return "COMPRAS";
		}else if (numPerfil == 3){
			return "CUENTAS POR PAGAR";
		}else if (numPerfil == 4){
			return "PROVEEDORES";
		}
		return "";
    }
	
	
	
	
	public static String regresaMes(String mes){
		int mesValida = Integer.parseInt(mes);
		if (mesValida == 1){
			return "Enero";
		}else if (mesValida == 2){
			return "Febrero";
		}else if (mesValida == 3){
			return "Marzo";
		}else if (mesValida == 4){
			return "Abril";
		}else if (mesValida == 5){
			return "Mayo";
		}else if (mesValida == 6){
			return "Junio";
		}else if (mesValida == 7){
			return "Julio";
		}else if (mesValida == 8){
			return "Agosto";
		}else if (mesValida == 9){
			return "Septiembre";
		}else if (mesValida == 10){
			return "Octubre";
		}else if (mesValida == 11){
			return "Noviembre";
		}else if (mesValida == 12){
			return "Diciembre";
		}
		return "";
	}
	
	public static String formatFechaddMMyyyy(String fechaFormatear){
		StringBuffer sbFecha = new StringBuffer();
		if (fechaFormatear.length() == 10){
			sbFecha.append(fechaFormatear.substring(8, 10))
				.append("-")
				.append(regresaMes(fechaFormatear.substring(5, 7)))
				.append("-")
				.append(fechaFormatear.substring(0, 4));
		}
		return sbFecha.toString();
	}
	

	
	public static boolean validaFecha(String fechaActual, String fechaUltMov, int num_Dias){
		boolean cumple = false;
		Date dinicio = null, dfinal = null;
		Calendar cinicio = Calendar.getInstance();
	    Calendar cfinal = Calendar.getInstance();
	    long milis1, milis2, diff, difdias;
		try {
            dinicio = formatDate.parse(fechaUltMov);
            dfinal =  formatDate.parse(fechaActual);                    
		}
		catch (Exception e) {
            UtilsXMLSAT.imprimeLog("", e);
		}
        cinicio.setTime(dinicio);
        cfinal.setTime(dfinal);
        milis1 = cinicio.getTimeInMillis();
        milis2 = cfinal.getTimeInMillis();
        diff = milis2-milis1;
        difdias = Math.abs ( diff / (24 * 60 * 60 * 1000) );
        if(((int) difdias) >= num_Dias){
        	cumple = true;
        }
        dinicio = null;
        dfinal = null;
        cinicio = null;
        cfinal  = null;
		return cumple;
	}
	
	

	public static String getMensajeValidacion(String cadOriginal, String cadReplace, String cadValor){
		String cadenaFinal = null;
		try{
			cadenaFinal = cadOriginal.replace(cadReplace,cadValor);
			return cadenaFinal;
		}catch(Exception e){
			imprimeLog("", e);
			return cadenaFinal;
		}
	}

	
	public static int obtenerMesActual() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;

	}
	
	public static int obtenerDiaActual() {
		Calendar cal = Calendar.getInstance();
		return cal.get(cal.DAY_OF_MONTH);
	}
	
	
	public static Date obtenerDiaPago(int mesPago, int diaIni) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, mesPago);
		cal.set(Calendar.DAY_OF_MONTH, diaIni);
		return cal.getTime();

	}

	
	public static String cortarValor(String cadOriginal, int maximoCaracteres) {
		String cadResultado = "";
		try {
			if (cadOriginal.length() > maximoCaracteres) {
				cadResultado = cadOriginal.substring(0, maximoCaracteres); 
			}else {
				cadResultado = cadOriginal;
			}
		}catch(Exception e) {
			UtilsXMLSAT.imprimeLog("", e);
		}
		return cadResultado;
	}

	
	public static StringBuffer validaFechaPado(String fechaPago) {
		StringBuffer sbFechaPago = null;
		try {
			// 2017:7:1
			if (!fechaPago.equals("") && fechaPago.length() > 7) {
				sbFechaPago = new StringBuffer();
				String cadFecha [] = fechaPago.split(":");
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
			UtilsXMLSAT.imprimeLog("", e);
		}
		return sbFechaPago;
	}

 public static void main(String[] args) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int diaIni = 16;
			int diaFin = 31;
			int diaPago = 15;
			String bandMes = "S";
			int diaActual = obtenerDiaActual();
			if (diaActual >= diaIni && diaActual <= diaFin ) {
				int mesActual = obtenerMesActual();
				if ("N".equalsIgnoreCase(bandMes)) {
					mesActual--;
				}
				Date fechaPago = obtenerDiaPago(mesActual, diaPago);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		

	}
}
