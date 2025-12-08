package com.siarex247.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class Utils {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    static SimpleDateFormat formatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    
     public static String VERSION = "1.11";
     // public static String VERSION = String.valueOf(System.currentTimeMillis());
	
	public static String replaceCaracteresEspeciales(String cadena){
		String cadenaFinal = null;
		try{
			cadenaFinal = cadena.replace("Ñ","(N)")
								.replace("ñ","(n)")
								.replace("á","(a)")
								.replace("é","(e)")
								.replace("í","(i)")
								.replace("ó","(o)")
								.replace("ú","(u)")
								.replace("ü","(uu)")
								.replace("Á","(A)")
								.replace("É","(E)")
								.replace("Í","(I)")
								.replace("Ó","(O)")
								.replace("Ú","(U)")
								.replace("Ü","(UU)");
			return cadenaFinal;
		}catch(Exception e){
			imprimeLog("", e);
			return cadena;
		}
	}
	
	
	public static Object noNulo (Object cadena){
		return validaNulo(cadena).toString().trim();
	}
	
	/*
	public static boolean esTodoDia(String bandDia){
		if ("S".equalsIgnoreCase(noNulo(bandDia))) {
			return true;
		}
		return false;
	}
	
	
	public static String carTodoDia(boolean bandTodoDia){
		if (bandTodoDia) {
			return "S";
		}
		return "N";
	}
	*/
	
	public static String getFechayyyyMMdd(){
		Date fecha = new Date();
		return formatyyyyMMdd.format(fecha);
	}
	
	
	public static int obtenerDiaActual() {
		Calendar cal = Calendar.getInstance();
		return cal.get(cal.DAY_OF_MONTH);
	}
	
	
	public static int obtenerMesActual() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;

	}

	
	public static Date obtenerDiaPago(int mesPago, int diaIni) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, mesPago);
		cal.set(Calendar.DAY_OF_MONTH, diaIni);
		return cal.getTime();

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

	public static double convertirDouble (String cadenaNumero){
		StringBuffer sbRetorno = new StringBuffer();
		try {
			if (cadenaNumero == null) {
				return 0;
			}else if (cadenaNumero.indexOf(",") > -1) {
				String arrNumero [] = cadenaNumero.split(",");
				for (int x = 0; x < arrNumero.length; x++) {
					sbRetorno.append(arrNumero[x]);
				}
				return Double.parseDouble(sbRetorno.toString());
			}else if (cadenaNumero.indexOf(",") == -1 || cadenaNumero.indexOf(".") > -1) {
				return Double.parseDouble(cadenaNumero);
			}
			
		}catch(Exception e) {
			sbRetorno.append("0");
		}
		return 0;
		
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
		}catch(Exception e) {
			cadena = "0";
		}
		
		return Double.parseDouble(cadena.toString());
	}
	
	public static long noNuloLong(String cadena) {
		try {
			if(cadena == null || "".equals(cadena))
			{
				cadena = "0";
			}
		}catch(Exception e) {
			cadena = "0";
		}
		return Long.parseLong(cadena.trim());
	}
	
	public static long noNuloLong(Object cadena)
	{
		if(cadena == null || "".equals(cadena))
		{
			cadena = "0";
		}
		return Long.parseLong(cadena.toString());
	}
	

	public static String capitalizaMes(String fechaTimestamp){
		/*
		if (fechaTimestamp.length() >= 10){
			return fechaTimestamp.substring(0,3) +  fechaTimestamp.substring(3,4).toUpperCase() + fechaTimestamp.substring(4); 
		}*/
		fechaTimestamp = (fechaTimestamp.length() != 0) ?fechaTimestamp.toString().toLowerCase().substring(0,1).toUpperCase().concat(fechaTimestamp.substring(1)): fechaTimestamp;

		return fechaTimestamp;
	}
	
	public static void imprimeLog(String msg, Exception e){
		Writer result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		logger.info(msg + " : "+result.toString());
	}

	
	public static String regresaCaracteresHTML(String cadena){
		String cadenaFinal = null;
		try{
			cadenaFinal = cadena.replace("(N)","&Ntilde;")
								.replace("(n)","&ntilde;")
								.replace("(a)","&aacute;")
								.replace("(e)","&eacute;")
								.replace("(i)","&iacute;")
								.replace("(o)","&oacute;")
								.replace("(u)","&uacute;")
								.replace("(uu)","&uuml;")
								.replace("(A)","&Aacute;")
								.replace("(E)","&Eacute;")
								.replace("(I)","&Iacute;")
								.replace("(O)","&Oacute;")
								.replace("(U)","&Uacute;")
								.replace("(UU)","&Uuml;");
			return cadenaFinal;
		}catch(Exception e){
			imprimeLog("", e);
			return cadena;
		}
	}

	
	
	public static String regresaCaracteresNormales(String cadena){
		String cadenaFinal = null;
		try{
			cadenaFinal = cadena.replace("(N)","Ñ")
								.replace("(n)","ñ")
								.replace("(a)","á")
								.replace("(e)","é")
								.replace("(i)","í")
								.replace("(o)","ó")
								.replace("(u)","ú")
								.replace("(uu)","ü")
								.replace("(A)","Á")
								.replace("(E)","É")
								.replace("(I)","Í")
								.replace("(O)","Ó")
								.replace("(U)","Ú")
								.replace("(UU)","Ü");
			return cadenaFinal;
		}catch(Exception e){
			imprimeLog("", e);
			return cadena;
		}
	}
	
	public static String remplazaCaracter(String cadena, String caracter){
		String cadenaFinal = null;
		try{
			cadenaFinal = cadena.replace(caracter,"");
			return cadenaFinal;
		}catch(Exception e){
			imprimeLog("", e);
			return cadena;
		}
	}
	
	
	public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }
	
	public static String redondearDecimalesComas(double valorInicial, int numeroDecimales, boolean signoPesos) {
		String valorNuevo = String.format("%,." + numeroDecimales + "f", valorInicial);
		
		if(signoPesos) {
			valorNuevo = String.format("$%,." + numeroDecimales + "f", valorInicial);
		}
        return valorNuevo;
    }
	
	public static double calcularPorcentaje(double porcent, double cant){
		double resultado;
		if (porcent == 0 || cant == 0) {
			resultado = 0;
		}else {
			resultado = (porcent / cant) * 100;	
		}
        return resultado; 
    }
	
	
	 private static String getHash(String txt, String hashType) {
	        try {
	            java.security.MessageDigest md = java.security.MessageDigest
	                    .getInstance(hashType);
	            byte[] array = md.digest(txt.getBytes());
	            StringBuffer sb = new StringBuffer();
	            for (int i = 0; i < array.length; ++i) {
	                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
	                        .substring(1, 3));
	            }
	            return sb.toString();
	        } catch (java.security.NoSuchAlgorithmException e) {
	            System.out.println(e.getMessage());
	        }
	        return null;
	    }
	 
	    public static String dobleEncryptarMD5(String txt) {
	    	String encryptar1 = getHash(txt, "MD5");
	        return getHash(encryptar1, "MD5");
	    }

	 /* Retorna un hash MD5 a partir de un texto */
	    public static String encryptarMD5(String txt) {
	        return getHash(txt, "MD5");
	    }
	 
	    /* Retorna un hash SHA1 a partir de un texto */
	    public static String encryptarSHA1(String txt) {
	        return getHash(txt, "SHA1");
	    }
	    
	    
	    public static String eliminarComillasCadena(String cadena) {
			 String result = null;
			 if (cadena == null) {
				 return "";
			 }else {
				 result = cadena.replaceAll("^[\"']+|[\"']+$", "");	 
			 }
			 return result;
		  }
	    
		
	    public static String eliminarComas(String cadena) {
			 StringBuffer result = null;
			 if (cadena == null) {
				 return "0";
			 }else {
				 result = new StringBuffer();
				 String cadDecimal [] = cadena.split(",");
				 for (int x = 0; x < cadDecimal.length; x++) {
					 result.append(cadDecimal[x]);
				 }
			 }
			 return result.toString();
		  }
	    
	    
	    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

	        List<String> result = new ArrayList<>();

	        //if empty, return!
	        if (cvsLine == null && cvsLine.isEmpty()) {
	            return result;
	        }

	        if (customQuote == ' ') {
	            customQuote = DEFAULT_QUOTE;
	        }

	        if (separators == ' ') {
	            separators = DEFAULT_SEPARATOR;
	        }

	        StringBuffer curVal = new StringBuffer();
	        boolean inQuotes = false;
	        boolean startCollectChar = false;
	        boolean doubleQuotesInColumn = false;

	        char[] chars = cvsLine.toCharArray();

	        for (char ch : chars) {

	            if (inQuotes) {
	                startCollectChar = true;
	                if (ch == customQuote) {
	                    inQuotes = false;
	                    doubleQuotesInColumn = false;
	                } else {

	                    //Fixed : allow "" in custom quote enclosed
	                    if (ch == '\"') {
	                        if (!doubleQuotesInColumn) {
	                            curVal.append(ch);
	                            doubleQuotesInColumn = true;
	                        }
	                    } else {
	                        curVal.append(ch);
	                    }

	                }
	            } else {
	                if (ch == customQuote) {

	                    inQuotes = true;

	                    //Fixed : allow "" in empty quote enclosed
	                    if (chars[0] != '"' && customQuote == '\"') {
	                        curVal.append('"');
	                    }

	                    //double quotes in column will hit this!
	                    if (startCollectChar) {
	                        curVal.append('"');
	                    }

	                } else if (ch == separators) {

	                    result.add(curVal.toString());

	                    curVal = new StringBuffer();
	                    startCollectChar = false;

	                } else if (ch == '\r') {
	                    //ignore LF characters
	                    continue;
	                } else if (ch == '\n') {
	                    //the end, break!
	                    break;
	                } else {
	                    curVal.append(ch);
	                }
	            }

	        }

	        result.add(curVal.toString());

	        return result;
	    }

	 	
		public static List<String> parseLine(String cvsLine, char separators) {
	        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	    }
		
		
		 public static List<String> parseLine(String cvsLine) {
		        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
		  }
	    
		
		
		
	    
	public static void main(String[] args) {
		try{
			/*
				long totDias = tiempoTranscurridoDosFechas(2021, 9, 1, 2022, 8, 1);
			*/
			
			 String codeMD5 = dobleEncryptarMD5("Polycom2021");
			System.err.println("codeMD5===>"+codeMD5);
	        
			// int numero =  (int) (Math.random() * 10 ) + 1;
			// System.err.println("numero==>"+numero);
	        
		}catch(Exception e) {
			e.printStackTrace();
			// Utils.imprimeLog("", e);
		}
	}
	
	
	public static String moveFileDirectory(
			File sourceFile,
			File destFile,
			boolean overwrite,
			boolean preserveLastModified,
			boolean datos)
			throws IOException {

			try {
				if (overwrite
					|| !destFile.exists()
					|| destFile.lastModified() < sourceFile.lastModified()) {
					File parent = new File(destFile.getParent());
					if (!parent.exists()) {
						parent.mkdirs();

					}

					FileInputStream in = new FileInputStream(sourceFile);
					FileOutputStream out = new FileOutputStream(destFile);

					byte[] buffer = new byte[8 * 1024];
					int count = 0;
					do {
						if (datos) {
							out.write(buffer, 0, count);
						}
						count = in.read(buffer, 0, buffer.length);
					} while (count != -1);

					in.close();
					out.close();

					if (preserveLastModified) {
						destFile.setLastModified(sourceFile.lastModified());
						
					}
				}
				 sourceFile.delete();
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
			return "";
		}
	
	public static long tiempoTranscurridoActual(int anio, int mes, int dia){
		  final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; // Milisegundos al d�a 
		  //final long MILLSECS_PER_DAY = (60 * 60 * 1000); // Milisegundos al d�a
		  Date hoy = new Date(); //Fecha de hoy 
		  
		 // tiempotranscurrido = Math.rint(((double) tempresta / 3600000) * 100) / 100;

		 // SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  
		  Calendar calendar = Calendar.getInstance();
		  
		  //ahoraIni.set(annio,(mes - 1),dia);
		  calendar.set(anio, (mes - 1), dia);
		  /*
		  calendar.set(Calendar.YEAR, anio);
		  calendar.set(Calendar.MONTH, mes - 1);
		  calendar.set(Calendar.DAY_OF_MONTH, dia);
		  calendar.set(Calendar.HOUR, 18);
		  calendar.set(Calendar.MINUTE, 0);
		  calendar.set(Calendar.SECOND, 0);
		  */
		  Date fecha = new Date(calendar.getTimeInMillis());
		  
		  long diferencia = ( hoy.getTime() - fecha.getTime() ) / MILLSECS_PER_DAY;
		  return diferencia ;
	}

	
	public static long tiempoTranscurridoDosFechas(int anioIni, int mesIni, int diaIni, int anioFin, int mesFin, int diaFin){
		  final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; // Milisegundos al d�a 
//		  Date hoy = new Date(); //Fecha de hoy 
		  
//		  SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  
		  Calendar calendarIni = Calendar.getInstance();
		  Calendar calendarFin = Calendar.getInstance();
		  
		  calendarIni.set(anioIni, (mesIni - 1), diaIni);
		  calendarFin.set(anioFin, (mesFin - 1), diaFin);
		  
		  Date fechaIni = new Date(calendarIni.getTimeInMillis());
		  Date fechaFin = new Date(calendarFin.getTimeInMillis());
		  
		  
		  long diferencia = ( fechaFin.getTime() - fechaIni.getTime() ) / MILLSECS_PER_DAY;
		  return diferencia ;
	}
	
	
	public static String eliminaCaracter(String nombreArchivo){
		String nombreArchivoFinal = nombreArchivo.replaceAll("&=", "");
		return nombreArchivoFinal ;	
	}
	
	
	
	public static String firstHex(int dec) {
        String numeroHex="";
        if(dec < 16) {
            switch(dec) {
                    case 10:
                    numeroHex="A";
                    break;
                    case 11:
                    numeroHex="B";
                    break;
                    case 12:
                    numeroHex="C";
                    break;
                    case 13:
                    numeroHex="D";
                    break;
                    case 14:
                    numeroHex="E";
                    break;
                    case 15:
                    numeroHex="F";
                    break;
                    default:
                    numeroHex=Integer.toString(dec);
                    break;
                }
            return numeroHex;
        } else {
        return "false";
        }
    }
	
	public static String toHex(int dec) {
        int cociente=16, residuo=0;
        String numeroHex="", numeroHex1="";
        if(dec < 16) {             
        	numeroHex=firstHex(dec);         
        } else {         
        	do {
        			cociente=dec/16;                         
        			residuo=dec%16;             
        			dec=cociente;             
        			numeroHex1=firstHex(residuo);                        
        			numeroHex=numeroHex1+numeroHex;             
        			dec=cociente;         
        		} while (dec >= 16);
		        numeroHex1=firstHex(dec);
		        numeroHex=numeroHex1+numeroHex;
        }
        return numeroHex;
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

	
	
	public static String regresaCaracteresFormato(String cadenaOriginal, String razonSocial, String rfc, String usuario){
		String cadenaFinal = null;
		try{
			cadenaFinal = cadenaOriginal.replace("|razonSocial|",razonSocial)
								.replace("|rfc|",rfc)
								.replace("|usuario|",usuario);
			return cadenaFinal;
		}catch(Exception e){
			imprimeLog("", e);
			return cadenaOriginal;
		}
	}

	
	public static String validaCheck(String valorCheck){
		try{
			if ("on".equalsIgnoreCase(noNulo(valorCheck))) {
				return "S";
			}else {
				return "N";
			}
		}catch(Exception e){
			imprimeLog("", e);
		}
		return "N";
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
			Utils.imprimeLog("", e);
		}
		return cadResultado;
	}

	
	public static boolean validaCaracteresEspeciales(String cadenaValidar) {
		char arrCar [] = {'#','[',']','{','}','%','^','+','=','•','£','€','>','~','?','!',')','(','!','”', '&', '$'};
		for (int x = 0; x < arrCar.length; x++) {
			if (cadenaValidar.indexOf(arrCar[x]) > -1) {
				return true;
			}
		}
		return false;
	}
	

	 public static boolean compararFechaMayor (String fecha1, String fecha2) {
			try {
				SimpleDateFormat formaDate = new SimpleDateFormat("yyyy-MM-dd");
				Date fechaCom1 = formaDate.parse(fecha1);
				Date fechaCom2 = formaDate.parse(fecha2);
				
				if (fechaCom2.compareTo(fechaCom1) >= 0) {
					return true;
				}
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}
			return false;
		}
	
	 
	 public static double calcularNumeroMayor (double arrNumeros[]) {
			double numeroMayor = 0;
		 	try {
		 		numeroMayor = arrNumeros[0];
		 		for(int i=0; i < arrNumeros.length; i++){
		            if(arrNumeros[i] > numeroMayor){ // 
		            	numeroMayor = arrNumeros[i];
		            }
		 		}
		            
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}
			return numeroMayor;
		}
	 
	 	public static String eliminarGuiones(String rfcCadena) {
	 		String arrGuines [] = rfcCadena.split("-");
	  		StringBuffer sbRFC = new StringBuffer();
	  		if (arrGuines.length == 0) {
	  			sbRFC.append(rfcCadena);
	  		}else {
	  			for (int x = 0; x < arrGuines.length; x++) {
		  			sbRFC.append(arrGuines[x]);
		  		}
	  		}
	  		
	  		return sbRFC.toString();
	 	}
	 
	 
/*
	 public static String validarRegimen (String desRegimen) {
		 String claveRegimen = "";
		 try {
			 if ("Régimen de Sueldos y Salarios e Ingresos Asimilados a Salarios".equalsIgnoreCase(desRegimen)) {
				 claveRegimen = "605";
			 }else if ("Régimen General de Ley Personas Morales".equalsIgnoreCase(desRegimen)) {
				 claveRegimen = "601";
			 }else {
				 claveRegimen = "";
			 }
		 }catch(Exception e) {
			 Utils.imprimeLog("", e);
		 }
		 return claveRegimen;
	 }
 */
	 
	 
}
