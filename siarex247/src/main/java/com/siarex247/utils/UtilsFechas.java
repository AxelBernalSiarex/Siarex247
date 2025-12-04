package com.siarex247.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

public class UtilsFechas {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	static Locale es_MX = new Locale("es","MX");
	
	static SimpleDateFormat formatddMMMyyyy = new SimpleDateFormat("dd-MMM-yyyy", es_MX);
	static SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
	// static SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyyMMddHHmmss");
	static SimpleDateFormat formaYYYY = new SimpleDateFormat("yyyy");
	static SimpleDateFormat formatyyyyMMddhhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static SimpleDateFormat formatoFechaTimestamp = getFormatoFechaTimestamp();

	
	
	public static String getFechaddMMMyyyyHHss(java.sql.Timestamp fecha){
		if (fecha == null){
			return capitalizaMes(getFechaddMMMyyyy());
		}else if ("0001-01-01 00:00:00.0".equals(fecha.toString())){
			return "";
		}else{
			return capitalizaMes(formatoFechaTimestamp.format(fecha));
		}
	}
	
	public static String getFechaddMMMyyyy(Date fecha){
		/*
            Locale[] locales = Locale.getAvailableLocales();
            for (Locale local:locales){
               System.out.println(local.getLanguage() + ", "+local.getCountry());
            }

		 */
		if (fecha == null){
			return capitalizaMes(getFechaddMMMyyyy());
		}else{
			return capitalizaMes(formatddMMMyyyy.format(fecha));
			
		}
	}
	
	
	public static String getFechayyyyMMdd(){
		Date fecha = new Date();
		return formatyyyyMMdd.format(fecha);
	}
	
	public static String getFechayyyyMMdd(Date fecha){
		if (fecha == null){
			return getFormatyyyyMMdd();
		}else{
			return formatyyyyMMdd.format(fecha);
			
		}
	}
	
	public static SimpleDateFormat getFormatoFechaTimestamp()
    {
		String formatFecha = null; 
		formatFecha = "dd-MMM-yyyy k:mm:ss";
		return new SimpleDateFormat(formatFecha, es_MX);
    }
	
	
	public static String getFechaddMMMyyyy(){
		Date fecha = new Date();
		return formatddMMMyyyy.format(fecha);
	}
	
	
	private static String getFormatyyyyMMdd(){
		Date fecha = new Date();
		return formatyyyyMMdd.format(fecha);
	}
	

	public static String getFechaActual(){
		  Date fechaHoy = new Date();
		  return formatyyyyMMddhhmmss.format(fechaHoy);
		}
	
	
	public static String getFechaActualNumero(){
		  Date fechaHoy = new Date();
		  return formatDate.format(fechaHoy);
		}
	
	/*
	public static String getFechaActualTime(){
		  Date fechaHoy = new Date();
		  return formatDateTime.format(fechaHoy);
		}
	*/
	
	
	
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
 
 public static String regresaFecha4(String fecha)
    {
        String fechaInicio = "";
        String mes = fecha.substring(0, 2);
        String dia = fecha.substring(3, 5);
        String anio = fecha.substring(6,10);
        fechaInicio = dia.concat("/").concat(mes).concat("/").concat(anio);
        return fechaInicio;
    }
 
	public static String capitalizaMes(String fechaTimestamp){
		/*
		if (fechaTimestamp.length() >= 10){
			return fechaTimestamp.substring(0,3) +  fechaTimestamp.substring(3,4).toUpperCase() + fechaTimestamp.substring(4); 
		}*/
		// fechaTimestamp = (fechaTimestamp.length() != 0) ? fechaTimestamp.toString().toLowerCase().substring(3,4).toUpperCase().concat(fechaTimestamp.substring(1)): fechaTimestamp;

		String mesFormat = fechaTimestamp.substring(3, 4).toUpperCase() + fechaTimestamp.substring(4, 6);
		String fechaRetorno = fechaTimestamp.substring(0, 3).concat(mesFormat).concat(fechaTimestamp.substring(6));
		return fechaRetorno;
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
	
	
	public static String getDescMes(String mes)
    {
    	String desc  = "";
    	
    	try
    	{
    		if(mes.equals("01"))
    		{
    			desc = "Enero";
    		}
    		else if(mes.equals("02"))
    		{
    			desc = "Febrero";
    		}
    		else if(mes.equals("03"))
    		{
    			desc = "Marzo";
    		}
    		else if(mes.equals("04"))
    		{
    			desc = "Abril";
    		}
    		else if(mes.equals("05"))
    		{
    			desc = "Mayo";
    		}
    		else if(mes.equals("06"))
    		{
    			desc = "Junio";
    		}
    		else if(mes.equals("07"))
    		{
    			desc = "Julio";
    		}
    		else if(mes.equals("08"))
    		{
    			desc = "Agosto";
    		}
    		else if(mes.equals("09"))
    		{
    			desc = "Septiembre";
    		}
    		else if(mes.equals("10"))
    		{
    			desc = "Octubre";
    		}
    		else if(mes.equals("11"))
    		{
    			desc = "Noviembre";
    		}
    		else if(mes.equals("12"))
    		{
    			desc = "Diciembre";
    		}
    	}
    	catch (Exception e)
        {
            Utils.imprimeLog("", e);
        }
    	
    	return desc;
    }
	
	public static String getDescMes2(String mes)
    {
    	String desc  = "";
    	
    	try
    	{
    		if(mes.equalsIgnoreCase("Jan"))
    		{
    			desc = "Enero";
    		}
    		else if(mes.equalsIgnoreCase("Feb"))
    		{
    			desc = "Febrero";
    		}
    		else if(mes.equalsIgnoreCase("Mar"))
    		{
    			desc = "Marzo";
    		}
    		else if(mes.equalsIgnoreCase("Apr"))
    		{
    			desc = "Abril";
    		}
    		else if(mes.equalsIgnoreCase("May"))
    		{
    			desc = "Mayo";
    		}
    		else if(mes.equalsIgnoreCase("Jun"))
    		{
    			desc = "Junio";
    		}
    		else if(mes.equalsIgnoreCase("Jul"))
    		{
    			desc = "Julio";
    		}
    		else if(mes.equalsIgnoreCase("Aug"))
    		{
    			desc = "Agosto";
    		}
    		else if(mes.equalsIgnoreCase("Sep"))
    		{
    			desc = "Septiembre";
    		}
    		else if(mes.equalsIgnoreCase("Oct"))
    		{
    			desc = "Octubre";
    		}
    		else if(mes.equalsIgnoreCase("Nov"))
    		{
    			desc = "Noviembre";
    		}
    		else if(mes.equalsIgnoreCase("Dec"))
    		{
    			desc = "Diciembre";
    		}
    	}
    	catch (Exception e)
        {
            Utils.imprimeLog("", e);
        }
    	
    	return desc;
    }
	
	
	
	public static String getMesEspaniol(String mes)
    {
    	String desc  = "";
    	
    	try
    	{
    		if(mes.equals("01")){
    			desc = "Ene";
    		}else if(mes.equals("02")){
    			desc = "Feb";
    		}else if(mes.equals("03")){
    			desc = "Mar";
    		}else if(mes.equals("04")){
    			desc = "Abr";
    		}else if(mes.equals("05")){
    			desc = "May";
    		}else if(mes.equals("06")){
    			desc = "Jun";
    		}else if(mes.equals("07")){
    			desc = "Jul";
    		}else if(mes.equals("08")){
    			desc = "Ago";
    		}else if(mes.equals("09")){
    			desc = "Sep";
    		}else if(mes.equals("10")){
    			desc = "Oct";
    		}else if(mes.equals("11")){
    			desc = "Nov";
    		}else if(mes.equals("12")){
    			desc = "Dic";
    		}
    	}
    	catch (Exception e)
        {
            Utils.imprimeLog("", e);
        }
    	
    	return desc;
    }

	public static String getFechaYear(){
		Date fecha = new Date();
		return formaYYYY.format(fecha);
	}


	 public static boolean compararDosFechas (String fecha1, String fecha2, String fechaValidar) {
			try {
				SimpleDateFormat formaDate = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaCom1 = formaDate.parse(fecha1);
				Date fechaCom2 = formaDate.parse(fecha2);
				Date fechaCom3 = formaDate.parse(fechaValidar);
				
				if ((fechaCom1.before(fechaCom3) || fechaCom1.equals(fechaCom3))    && (fechaCom2.after(fechaCom3) || fechaCom2.equals(fechaCom3))) {
					return true;
				}
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}
			return false;
		}

	 public static boolean compararFechaMayor (String fecha1, String fecha2) {
			try {
				SimpleDateFormat formaDate = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaCom1 = formaDate.parse(fecha1);
				Date fechaCom2 = formaDate.parse(fecha2);
				
				if (fechaCom1.compareTo(fechaCom2) >= 0) {
					return true;
				}
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}
			return false;
		}
	 
	 public static boolean validaFecha(String fechaActual, String fechaUltMov, int num_Dias){
			boolean cumple = false;
			Date dinicio = null, dfinal = null;
			Calendar cinicio = Calendar.getInstance();
		    Calendar cfinal = Calendar.getInstance();
		    long milis1, milis2, diff, difdias;
			try {
	            dinicio = formatyyyyMMdd.parse(fechaUltMov);
	            dfinal =  formatyyyyMMdd.parse(fechaActual);                    
			}
			catch (Exception e) {
	            Utils.imprimeLog("", e);
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
	 
	 
	 
	 public static String restarDiasFecha(String fechaInicial, int num_Dias){
		String fechaResultado = "";	
		 try {
				Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicial);
				Calendar calendario = Calendar.getInstance();
				calendario.setTime(fecha);
				calendario.add(Calendar.DAY_OF_MONTH, -num_Dias);
				fechaResultado = new SimpleDateFormat("yyyy-MM-dd").format(calendario.getTime());
			} catch (Exception e) {
				Utils.imprimeLog("", e);
			}
		  return fechaResultado;
		}
	 
	 public static int obtenerUltimoDiaMes (int anio, int mes) {

			Calendar cal = Calendar.getInstance();
			cal.set(anio, mes-1, 1);
			return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

			}
	 
	 
	 public static String formatFechaSat (LocalDateTime fechaXML) {
		   String fechaSat = "";
		   try {
				 	if(fechaXML != null) {
				 		fechaSat = fechaXML.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				 	}
						
			}catch(Exception e) {
				Utils.imprimeLog("", e);
				fechaSat = "";
			}
		   return fechaSat;

	}
	 

	 public static boolean validaBimestre(String fechaActual) {
	    	boolean esBimestre = false;
	    	try {
	    		String mesDia = fechaActual.substring(5);
	    		if ("02-28".equalsIgnoreCase(mesDia)) {
	    			esBimestre = true;
	    		}else if ("02-29".equalsIgnoreCase(mesDia)) {
	    			esBimestre = true;
	    		}else if ("04-30".equalsIgnoreCase(mesDia)) {
	    			esBimestre = true;
	    		}else if ("06-30".equalsIgnoreCase(mesDia)) {
	    			esBimestre = true;
	    		}else if ("08-31".equalsIgnoreCase(mesDia)) {
	    			esBimestre = true;
	    		}else if ("10-31".equalsIgnoreCase(mesDia)) {
	    			esBimestre = true;
	    		}else if ("12-31".equalsIgnoreCase(mesDia)) {
	    			esBimestre = true;
	    		}
	    	}catch(Exception e) {
	    		Utils.imprimeLog("", e);
	    	}
	    	return esBimestre;
	    }

	 public static boolean validaTrimestre(String fechaActual) {
	    	boolean esTrimestre = false;
	    	try {
	    		String mesDia = fechaActual.substring(5);
	    		if ("03-31".equalsIgnoreCase(mesDia)) {
	    			esTrimestre = true;
	    		}else if ("06-30".equalsIgnoreCase(mesDia)) {
	    			esTrimestre = true;
	    		}else if ("09-30".equalsIgnoreCase(mesDia)) {
	    			esTrimestre = true;
	    		}else if ("12-31".equalsIgnoreCase(mesDia)) {
	    			esTrimestre = true;
	    		}
	    	}catch(Exception e) {
	    		Utils.imprimeLog("", e);
	    	}
	    	return esTrimestre;
	    }
	 
	 
	 public static boolean validaAnnual(String fechaActual) {
	    	boolean esAnnual = false;
	    	try {
	    		String mesDia = fechaActual.substring(5);
	    		if ("12-31".equalsIgnoreCase(mesDia)) {
	    			esAnnual = true;
	    		}
	    	}catch(Exception e) {
	    		Utils.imprimeLog("", e);
	    	}
	    	return esAnnual;
	    }

	 public static String obtenerFechaFormateada() {
		    // Ejemplo de salida: "3 Noviembre 2025 03:11 PM"
		    LocalDateTime ahora = LocalDateTime.now();
		    // Locale en español para el mes en texto
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy hh:mm a", new Locale("es", "MX"));
		    String fechaFormateada = ahora.format(formatter);
		    // Separar en partes: día, mes, año, hora...
		    String[] partes = fechaFormateada.split(" ");
		    if (partes.length >= 2) {
		        // Capitalizar solo el mes (parte[1])
		        String mes = partes[1];
		        if (mes.length() > 1)
		            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);
		        partes[1] = mes;
		    }
		    // Reconstruir la fecha con los espacios originales
		    StringBuilder fechaFinal = new StringBuilder();
		    for (int i = 0; i < partes.length; i++) {
		        fechaFinal.append(partes[i]);
		        if (i < partes.length - 1) fechaFinal.append(" ");
		    }
		    return fechaFinal.toString();
		}
	 
	 
	 public static String obtenerFechaMesCompleto() {
		    // Ejemplo de salida: "3 Noviembre 2025 03:11 PM"
		    LocalDateTime ahora = LocalDateTime.now();
		    // Locale en español para el mes en texto
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("es", "MX"));
		    String fechaFormateada = ahora.format(formatter);
		    // Separar en partes: día, mes, año, hora...
		    String[] partes = fechaFormateada.split(" ");
		    if (partes.length >= 2) {
		        // Capitalizar solo el mes (parte[1])
		        String mes = partes[1];
		        if (mes.length() > 1)
		            mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);
		        partes[1] = mes;
		    }
		    // Reconstruir la fecha con los espacios originales
		    StringBuilder fechaFinal = new StringBuilder();
		    for (int i = 0; i < partes.length; i++) {
		        fechaFinal.append(partes[i]);
		        if (i < partes.length - 1) fechaFinal.append(" ");
		    }
		    return fechaFinal.toString();
		}
	 
	 public static boolean esFechaFormatoValido(String fecha) {
	        DateTimeFormatter formatter = DateTimeFormatter
	                .ofPattern("uuuu-MM-dd") // "uuuu" es mejor que "yyyy"
	                .withResolverStyle(ResolverStyle.STRICT); // valida fechas reales

	        try {
	            LocalDate.parse(fecha, formatter);
	            return true; // Fecha válida
	        } catch (DateTimeParseException e) {
	            return false; // Fecha inválida
	        }
	    }
	 
	 
	 public static void main(String[] args) {
		try {
			//String fechaFinal = restarDiasFecha("2023-08-24", 365);
			//boolean cumple =  validaFecha("2023-08-24 01:01:01", "2022-08-23 01:01:01", 366);
			String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmssSSSSSS").format(new java.util.Date());
			Date fechaHoy = new Date();
			System.err.println("timestamp===>"+timestamp.formatted(fechaHoy));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
