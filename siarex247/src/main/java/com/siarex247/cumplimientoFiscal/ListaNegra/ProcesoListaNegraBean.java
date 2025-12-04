package com.siarex247.cumplimientoFiscal.ListaNegra;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class ProcesoListaNegraBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	private final String URL_1 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Cancelados.csv", "Cancelados.csv"}; // CANCELADOS
	private final String URL_2 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Condonadosart74CFF.csv", "Condonadosart74CFF.csv"}; // Condonados de multas (Artículo 74 del Código Fiscal de la Federación) 
	private final String URL_3 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Condonadosart146BCFF.csv", "Condonadosart146BCFF.csv"}; // Condonados de concurso mercantil (Artículo 146B del Código Fiscal de la Federación)
	private final String URL_4 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Condonadosart21CFF.csv", "Condonadosart21CFF.csv"}; // Condonados de recargos (Artículo 21 del Código Fiscal de la Federación)
	private final String URL_5 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/CondonadosporDecreto.csv", "CondonadosporDecreto.csv"}; // Condonados por Decreto (Del 22 de enero y 26 de marzo de 2015)
	private final String URL_6 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Retornoinversiones.csv", "Retornoinversiones.csv"}; // Retorno de inversiones
	private final String URL_7 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Definitivos.csv", "Definitivos.csv"}; 
	private final String URL_8 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Desvirtuados.csv", "Desvirtuados.csv"};
	private final String URL_9 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Presuntos.csv", "Presuntos.csv"};
	private final String URL_10 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/SentenciasFavorables.csv", "SentenciasFavorables.csv"};
	private final String URL_11 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Condonados_07_15.csv","Condonados_07_15.csv"};	
	private final String URL_12 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Cancelados_07_15.csv","Cancelados_07_15.csv"};
		
	private final String URL_13 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Exigibles.csv", "Exigibles.csv"};
	private final String URL_14 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Firmes.csv", "Firmes.csv"};
	private final String URL_15 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/No%20localizados.csv", "Nolocalizados.csv"};
	private final String URL_16 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Sentencias.csv", "Sentencias.csv"};
	private final String URL_17 [] = {"http://omawww.sat.gob.mx/cifras_sat/Documents/Eliminados.csv", "Eliminados.csv"};
	
	private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    private static final String UTF8 = "UTF-8";

		public void monitorListaNegra(){
			try{
				iniciaProceso();
			}catch(Exception e){
				Utils.imprimeLog("", e);
			}
		}
		
	
	public void iniciaProceso() {
		ConexionDB connPool = null;
        ResultadoConexion rcEmpresa = null;
        Connection conSiarex = null;
        String esquemaSiarex = null;
        try{
        	connPool = new ConexionDB();
        	rcEmpresa = connPool.getConnectionSiarex();
        	conSiarex = rcEmpresa.getCon();
        	esquemaSiarex = rcEmpresa.getEsquema();
			
			String fechaHoy = getFechaHoy();
			int mes = Integer.parseInt(fechaHoy.substring(5, 7));
			int year = Integer.parseInt(fechaHoy.substring(0, 4));
			int diaMes = Integer.parseInt(fechaHoy.substring(8, 10));
			HashMap<Integer, String> mapaConf = obtenerConfiguracion(conSiarex, esquemaSiarex);
			
			int ultimoDia = UtilsFechas.obtenerUltimoDiaMes(year, mes); 
			
			String correProceso = mapaConf.get(ultimoDia);
			
			if (diaMes == ultimoDia) {
				if (mapaConf.get(0) != null && mapaConf.get(0).equalsIgnoreCase("FIN")) {
					correProceso = "FIN";	
				}
			}
			
			// logger.info("correProceso..."+correProceso);
			String fechaUltimaCorrida = obtenerUltimaCorrida(conSiarex, esquemaSiarex);
			
			if ("FIN".equalsIgnoreCase(correProceso)) {
				boolean isEjecuto = isEjecuto(conSiarex, esquemaSiarex);
				if (isEjecuto) {
					// logger.info("Proceso ya ejecutado del dia..."+ultimoDia);
				}else {

					grabarProceso(conSiarex, esquemaSiarex, 0, String.valueOf(ultimoDia)); // se grabar en bitacora
					int numEjecuciones = numEjecuciones(conSiarex, esquemaSiarex, year);
					if (numEjecuciones <= 48) {
						// sacar la ultima fecha qeu corrio, para saber si hay un cambio de año
						int yearUltima = Integer.parseInt( fechaUltimaCorrida.substring(0, 4) );
						//  logger.info("year====>"+year + "_ yearUltima = "+yearUltima);
						if (year > yearUltima) {
							// se genera el historico
							logger.info("Respaldando informacion de la lista negra del año : "+yearUltima);
							guardarHistoricoListaNegra(conSiarex, esquemaSiarex);
							eliminarListaNegra(conSiarex, esquemaSiarex);
						}
						
						procesoListaNegraCondonados(esquemaSiarex, URL_1[0], URL_1[1], numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_2[0], URL_2[1], numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_3[0], URL_3[1], numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_4[0], URL_4[1], numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_5[0], URL_5[1], numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_6[0], URL_6[1], numEjecuciones, 2);
						procesoListaNegraCondonadosDefinitivos(esquemaSiarex, URL_7[0], URL_7[1], numEjecuciones, 4);
						procesoListaNegraCondonadosDefinitivos(esquemaSiarex, URL_8[0], URL_8[1], numEjecuciones, 4);
						procesoListaNegraCondonadosDefinitivos(esquemaSiarex, URL_9[0], URL_9[1], numEjecuciones, 4);
						procesoListaNegraCondonadosDefinitivos(esquemaSiarex, URL_10[0], URL_10[1], numEjecuciones, 4);
						procesoListaNegraCondal2015(esquemaSiarex, URL_11[0], URL_11[1], "CONDONADOS0715",  numEjecuciones, 2);
						procesoListaNegraCondal2015(esquemaSiarex, URL_12[0], URL_12[1], "CANCELADOS0715",  numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_13[0], URL_13[1], numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_14[0], URL_14[1], numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_15[0], URL_15[1], numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_16[0], URL_16[1], numEjecuciones, 2);
						procesoListaNegraCondonados(esquemaSiarex, URL_17[0], URL_17[1], numEjecuciones, 2);
						 
						// se envia correo de notificacion a los correos de respaldo
							EnviarCorreoListaBean  enviarCorreoListaBean = new EnviarCorreoListaBean();
							enviarCorreoListaBean.iniciarProceoCorreos(fechaHoy);
						// termina
						
					}
				}
			}else {
				String correHoy = mapaConf.get(diaMes);
				if ("NOR".equalsIgnoreCase(correHoy)) {
					boolean isEjecuto = isEjecuto(conSiarex, esquemaSiarex);
					if (isEjecuto) {
						// logger.info("Proceso ya ejecutado del dia..."+diaMes);
							//EnviarCorreoListaBean  enviarCorreoListaBean = new EnviarCorreoListaBean();
							//enviarCorreoListaBean.iniciarProceoCorreos(esquema, fechaHoy, nombreEmpresa,  usuarioEmisorCorreo, pwdEmisorCorreo);
							
					}else {
						grabarProceso(conSiarex, esquemaSiarex, 0, String.valueOf(diaMes)); // se grabar en bitacora
						int numEjecuciones = numEjecuciones(conSiarex, esquemaSiarex, year);
						if (numEjecuciones <= 48) {
							int yearUltima = Integer.parseInt( fechaUltimaCorrida.substring(0, 4) );
							if (year > yearUltima) {
								// se genera el historico
								logger.info("Respaldando informacion de la lista negra del año : "+yearUltima);
								guardarHistoricoListaNegra(conSiarex, esquemaSiarex);
								eliminarListaNegra(conSiarex, esquemaSiarex);
								setAutoIncrementListaNegra(conSiarex, esquemaSiarex);
							}

							procesoListaNegraCondonados(esquemaSiarex, URL_1[0], URL_1[1], numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_2[0], URL_2[1], numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_3[0], URL_3[1], numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_4[0], URL_4[1], numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_5[0], URL_5[1], numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_6[0], URL_6[1], numEjecuciones, 2);
							procesoListaNegraCondonadosDefinitivos(esquemaSiarex, URL_7[0], URL_7[1], numEjecuciones, 4);
							procesoListaNegraCondonadosDefinitivos(esquemaSiarex, URL_8[0], URL_8[1], numEjecuciones, 4);
							procesoListaNegraCondonadosDefinitivos(esquemaSiarex, URL_9[0], URL_9[1], numEjecuciones, 4);
							procesoListaNegraCondonadosDefinitivos(esquemaSiarex, URL_10[0], URL_10[1], numEjecuciones, 4);
							procesoListaNegraCondal2015(esquemaSiarex, URL_11[0], URL_11[1], "CONDONADOS0715",  numEjecuciones, 2);
							procesoListaNegraCondal2015(esquemaSiarex, URL_12[0], URL_12[1], "CANCELADOS0715",  numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_13[0], URL_13[1], numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_14[0], URL_14[1], numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_15[0], URL_15[1], numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_16[0], URL_16[1], numEjecuciones, 2);
							procesoListaNegraCondonados(esquemaSiarex, URL_17[0], URL_17[1], numEjecuciones, 2);
							
							// se envia correo de notificacion a los correos de respaldo
								EnviarCorreoListaBean  enviarCorreoListaBean = new EnviarCorreoListaBean();
								enviarCorreoListaBean.iniciarProceoCorreos(fechaHoy);
							// termina	
							
						}
					}
					
				}else {
					// logger.info("Dia "+diaMes + " No configurado para descargar archivos del SAT....");
					// se envia correo de notificacion a los correos de respaldo
					//EnviarCorreoListaBean  enviarCorreoListaBean = new EnviarCorreoListaBean();
					//enviarCorreoListaBean.iniciarProceoCorreos(esquema, fechaHoy, nombreEmpresa, usuarioEmisorCorreo, pwdEmisorCorreo);
				// termina	

					
					// se envia correo de notificacion a los correos de respaldo
				//	EnviarCorreoListaBean  enviarCorreoListaBean = new EnviarCorreoListaBean();
				//	enviarCorreoListaBean.iniciarProceoCorreos(esquema, fechaHoy);
				// termina	

					
				}
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(conSiarex != null)
	            	conSiarex.close();
	            conSiarex = null;
	            
	        }catch(Exception e){
	        	conSiarex = null;
	        }
        }
	}
	
	
	
	
	public void procesoListaNegraCondonados(String esquema, String urlSAT, String nombreArticulo, int numColumna, int numCabecero) {
		URL url = null;
		ArrayList<String[]> regSAT = new ArrayList<>();
		BufferedReader in = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		try {
			logger.info("Detonando descarga de lista negra del URL...."+urlSAT);
			if (urlSAT == null || "".equals(urlSAT)) {
				logger.info("No se encuentra configurada una URL para ejecutar el proceso...");
			}else {
				connPool = new ConexionDB();
				//rc = connPool.getConnectionJDBC(esquema);
				rc = connPool.getConnectionSiarex();
				con = rc.getCon();
				url = new URL(urlSAT);
				URLConnection uc = url.openConnection();
				in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.ISO_8859_1));
				String inputLine = null;
				String cadSAT [] = null;
				
				//int x = 0;
				int contExcel=0;
				List<String> line = null;
				String RAZON_SOCIAL = null;
				while ((inputLine = in.readLine()) != null) {
					contExcel++;
					cadSAT = new String[7];
					try {
						if (contExcel >= numCabecero) {
							line = parseLine(inputLine);
							if (line.size() > 2) {
								cadSAT[0] = line.get(0);
								RAZON_SOCIAL = line.get(1);
								if (RAZON_SOCIAL.length() > 1500) {
									cadSAT[1] = RAZON_SOCIAL.substring(0, 1500);	
								}else {
									cadSAT[1] = RAZON_SOCIAL;
								}
								
								cadSAT[3] = line.get(3);
								regSAT.add(cadSAT);	
							}
							
							/*
							if (inputLine.length() > 17) {
						    	 cadCOM = inputLine.split("\",");
						    	 if (cadCOM.length > 1) {
						    		 sbRazonSocial.setLength(0);
						    		 String cadRazon = cadCOM[0];
						    		 String car = null;
						    		 String razonSocial = null; 
						    		 int posIni = 0;
						    		 for (int y = 0; y < cadRazon.length(); y++) {
						    			 car = cadRazon.substring(y, y+1);
						    			 if (car.equalsIgnoreCase(",")) {
						    				 posIni = y + 2;
						    				 break;
						    			 }
						    		 }
						    		 if (posIni > 0) {
						    			 razonSocial = cadRazon.substring(posIni, cadRazon.length());
						    			 String cadRazonSimple [] = razonSocial.split(",");
						    			 for (int z = 0; z < cadRazonSimple.length; z++) {
						    				 sbRazonSocial.append(cadRazonSimple[z]).append("$");
						    			 }
						    			 razonSocial = sbRazonSocial.substring(0, sbRazonSocial.length() - 1);
						    			 newInputline1 = inputLine.substring(0, posIni-1);
								    	 newInputline2 = inputLine.substring(cadRazon.length() + 1, inputLine.length());
								    	 newInputlineFinal =  Utils.replaceCaracteresEspeciales( newInputline1 + razonSocial + newInputline2);
								    	 cadSAT = newInputlineFinal.split(",");
								    	 regSAT.add(cadSAT);
						    		 }
						    	 }else {
						    		 newInputlineFinal = Utils.replaceCaracteresEspeciales( inputLine );
							    	 cadSAT = newInputlineFinal.split(",");
							    	 regSAT.add(cadSAT);
						    	 }
						    	 
						     }else {
						    	 newInputlineFinal = Utils.replaceCaracteresEspeciales( inputLine );
						    	 cadSAT = newInputlineFinal.split(",");
						    	 regSAT.add(cadSAT);
						     }*/
							
					}	
					}catch(Exception e) {
						Utils.imprimeLog("", e);
					}
				}
				
				try {
					in.close();	
				}catch(Exception e) {
					in = null;
				}
						
				
				if (regSAT.size() > 0) {
					String fechaActual = Utils.getFechayyyyMMdd();
					deleteListaNegraTemp(con, esquema);
					iniciaProcesoGuardado(con, esquema, regSAT, fechaActual, nombreArticulo, numColumna);
					
					updateListaNegra(con, esquema, numColumna, nombreArticulo);
					guardarListaNegraMasivo(con, esquema, nombreArticulo,  numColumna);
					deleteListaNegraTemp(con, esquema);
					
				}			
			}
			
			// logger.info("URL..."+urlSAT+  " Registro descargados...."+regSAT.size());
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (in != null) {
					in.close();	
				}	
				if (con != null) {
					con.close();	
				}	
				con = null;
			}catch(Exception e) {
				e = null;
			}
		}
	}
	
	
	
	
	public String iniciaProcesoGuardado(Connection con, String esquema, ArrayList<String[]> regSAT, String fechaActual, String nombreArticulo, int numColumna) {
		StringBuffer sbQuery = new StringBuffer();
		String queryFinal = null;
		int totGuardar = 0;
		ArrayList<String[]> guardarLista = new ArrayList<>();
		try {
			sbQuery.append(ProcesoArchivosQuerys.getGuardarListaNegra(esquema));
			for (int x = 0; x < regSAT.size(); x++) {
				sbQuery.append("(?,?,?,?,?),");
				totGuardar++;
				
				guardarLista.add(regSAT.get(x));
				if (totGuardar == 500) {
					queryFinal = sbQuery.substring(0, sbQuery.length() - 1);
					
					guardarRegistros(con, esquema, guardarLista, queryFinal, fechaActual, nombreArticulo);
					
					totGuardar = 0;
					sbQuery.setLength(0);
					sbQuery.append(ProcesoArchivosQuerys.getGuardarListaNegra(esquema));
					guardarLista = new ArrayList<>();
				}
			}
			
			if (totGuardar > 0) {
				queryFinal = sbQuery.substring(0, sbQuery.length() - 1);
				guardarRegistros(con, esquema, guardarLista, queryFinal, fechaActual, nombreArticulo);
				totGuardar = 0;
				sbQuery.setLength(0);
				sbQuery.append(ProcesoArchivosQuerys.getGuardarListaNegra(esquema));
				guardarLista = new ArrayList<>();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return queryFinal;
	}
	
	/*
	public String iniciaProcesoGuardadoCondonados(Connection con, String esquema, ArrayList<String[]> regSAT, String fechaActual, String nombreArticulo, int numColumna) {
		StringBuffer sbQuery = new StringBuffer();
		String queryFinal = null;
		int totGuardar = 0;
		ArrayList<String[]> guardarLista = new ArrayList<>();
		try {
			sbQuery.append(ProcesoArchivosQuerys.getGuardarListaNegraCondonados(esquema));
			for (int x = 0; x < regSAT.size(); x++) {
				sbQuery.append("(?,?,?,?,?),");
				totGuardar++;
				
				guardarLista.add(regSAT.get(x));
				if (totGuardar == 500) {
					queryFinal = sbQuery.substring(0, sbQuery.length() - 1);
					
					guardarRegistros(con, esquema, guardarLista, queryFinal, fechaActual, nombreArticulo);
					
					totGuardar = 0;
					sbQuery.setLength(0);
					sbQuery.append(ProcesoArchivosQuerys.getGuardarListaNegraCondonados(esquema));
					guardarLista = new ArrayList<>();
				}
			}
			
			if (totGuardar > 0) {
				queryFinal = sbQuery.substring(0, sbQuery.length() - 1);
				guardarRegistros(con, esquema, guardarLista, queryFinal, fechaActual, nombreArticulo);
				totGuardar = 0;
				sbQuery.setLength(0);
				sbQuery.append(ProcesoArchivosQuerys.getGuardarListaNegraCondonados(esquema));
				guardarLista = new ArrayList<>();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return queryFinal;
	}
	*/
	

	public int guardarRegistros(Connection con, String esquema, ArrayList<String[]> guardarLista, String queryInsert, String fechaActual, String nombreArticulo ) {
		PreparedStatement stmt = null;
		String[] cadenaSAT = null;
		try {
			stmt = con.prepareStatement(queryInsert);
			int numParam=1;
			for (int x = 0; x < guardarLista.size(); x++) {
				cadenaSAT = guardarLista.get(x);
				stmt.setString(numParam++, cadenaSAT[0]); // RFC
				stmt.setString(numParam++, cadenaSAT[1]); // Razon Social
				stmt.setString(numParam++, cadenaSAT[3]);  // supuesto
				stmt.setString(numParam++, nombreArticulo); // nombre articulo 
				stmt.setString(numParam++, "1");			// disponible
				
				
			}
			stmt.executeUpdate();
		}catch(Exception e) {
			//System.err.println("stmt===>"+stmt);
			guardarRegistrosUno(con, esquema, guardarLista, fechaActual, nombreArticulo);
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return 100;
	}

	
	public int guardarRegistrosUno(Connection con, String esquema, ArrayList<String[]> guardarLista, String fechaActual, String nombreArticulo) {
		PreparedStatement stmt = null;
		String[] cadenaSAT = null;
		try {
			stmt = con.prepareStatement(ProcesoArchivosQuerys.getGuardarListaNegraUno(esquema));
			for (int x = 0; x < guardarLista.size(); x++) {
				try {
					cadenaSAT = guardarLista.get(x);
					if (cadenaSAT!=null && cadenaSAT.length > 2) {
						stmt.setString(1, cadenaSAT[0]);
						stmt.setString(2, cadenaSAT[1]);
						stmt.setString(3, cadenaSAT[3]);
						stmt.setString(4, nombreArticulo);
						stmt.setString(5, "1");			// disponible
						stmt.executeUpdate();	
					}
						
				}catch(Exception e) {
					//System.err.println("STMT..."+stmt + " _ RFC : "+cadenaSAT[0]);
					Utils.imprimeLog("", e);
					logger.info("Error al guardar registro del SAT : "+e + "Registro : "+cadenaSAT[0]+", RazonSocial : "+cadenaSAT[1] + ", Supuesto : "+ cadenaSAT[3]);
				}
				
			}
			
		}catch(Exception e) {
			System.err.println("stmt===>"+stmt);
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return 100;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void procesoListaNegraCondonadosDefinitivos(String esquema, String urlSAT, String nombreArticulo, int numColumna, int numCabecero) {
		URL url = null;
		ArrayList<String[]> regSAT = new ArrayList<>();
		BufferedReader in = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		try {
			logger.info("Detonando descarga de lista negra del URL...."+urlSAT);
			if (urlSAT == null || "".equals(urlSAT)) {
				logger.info("No se encuentra configurada una URL para ejecutar el proceso...");
			}else {
				connPool = new ConexionDB();
				//rc = connPool.getConnectionJDBC(esquema);
				rc = connPool.getConnectionSiarex();
				con = rc.getCon();
				
				url = new URL(urlSAT);
				
				url = new URL(urlSAT);
				URLConnection uc = url.openConnection();
				in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.ISO_8859_1));
				
				
				// in = new BufferedReader(new InputStreamReader(url.openStream(), UTF8));
				String inputLine = null;
				String cadSAT [] = null;
				
				//1,AAA120730823,"ASESORES Y ADMINISTRADORES AGRICOLAS, S. DE R.L. DE C.V.
				//int x = 0;
				
				int contExcel=0;
				List<String> line = null;
				String RAZON_SOCIAL = null;
				
				//Scanner scanner = new Scanner(new File("C:/Users/TIJUANA/Downloads/Definitivos_VALIO_2.csv"));
				
				while ((inputLine = in.readLine()) != null) {
				//while (scanner.hasNextLine()) {
					try {
						//inputLine = scanner.nextLine();
						contExcel++;
						cadSAT = new String[7];
						if (contExcel >= numCabecero) {
							if (inputLine.indexOf("WCO090506JQ0") > -1) {
								logger.info("inputLine--->"+inputLine);
							}
							line = parseLine(inputLine);
							if (line.size() > 2) {
								cadSAT[0] = line.get(1);
								RAZON_SOCIAL = line.get(2); 
								if (RAZON_SOCIAL.length() > 1500) {
									cadSAT[1] = RAZON_SOCIAL.substring(0, 1500);	
								}else {
									cadSAT[1] = RAZON_SOCIAL;
								}
								cadSAT[3] = line.get(3);
								regSAT.add(cadSAT);	
							}
							
							
							
							/*boolean comillasRazon = false;
								if (inputLine.length() > 17) {
							    	 cadCOM = inputLine.split("\",");
							    	 if (cadCOM.length > 1) {
							    		 sbRazonSocial.setLength(0);
							    		 String cadRazon = cadCOM[0];
							    		 String car = null;
							    		 String razonSocial = null;
							    		 
							    		 int posIni = 0;
							    		 for (int y = 0; y < cadRazon.length(); y++) {
							    			 car = cadRazon.substring(y, y+1);
							    			 if (car.equalsIgnoreCase(",")) {
							    				 if (comillasRazon) {
							    					 posIni = y + 2;
							    					 break;
							    				 }else {
							    					 comillasRazon = true;
							    				 }
							    			 }
							    		 }
							    		 if (posIni > 0) {
							    			 razonSocial = cadRazon.substring(posIni, cadRazon.length());
							    		 	 String cadRazonSimple [] = razonSocial.split(",");
							    			 for (int z = 0; z < cadRazonSimple.length; z++) {
							    				 sbRazonSocial.append(cadRazonSimple[z]).append("$");
							    			 }
							    			 razonSocial = sbRazonSocial.substring(0, sbRazonSocial.length() - 1);
							    			 newInputline1 = inputLine.substring(0, posIni-1);
									    	 newInputline2 = inputLine.substring(cadRazon.length() + 1, inputLine.length());
									    	 newInputlineFinal =  Utils.replaceCaracteresEspeciales( newInputline1 + razonSocial + newInputline2);
									    	 cadSAT = newInputlineFinal.split(",");
									    	 regSAT.add(cadSAT);
							    		 }
							    	 }else {
							    		 newInputlineFinal = Utils.replaceCaracteresEspeciales( inputLine );
								    	 cadSAT = newInputlineFinal.split(",");
								    	 regSAT.add(cadSAT);
							    	 }
							    	 
							     }else {
							    	 newInputlineFinal = Utils.replaceCaracteresEspeciales( inputLine );
							    	 cadSAT = newInputlineFinal.split(",");
							    	 regSAT.add(cadSAT);
							     }
								*/
						}						
					}catch(Exception e) {
						Utils.imprimeLog("", e);
						logger.info("inputLine----------->"+inputLine);
					}
				}
				//scanner.close();
				in.close();
				
				if (regSAT.size() > 0) {
					String fechaActual = Utils.getFechayyyyMMdd();
					deleteListaNegraTemp(con, esquema);
					iniciaProcesoGuardadoDefinitivo(con, esquema, regSAT, fechaActual, nombreArticulo, numColumna);
					
					updateListaNegra(con, esquema, numColumna, nombreArticulo);
					guardarListaNegraMasivo(con, esquema, nombreArticulo,  numColumna);
					deleteListaNegraTemp(con, esquema);
				}			
			}
			// logger.info("URL..."+urlSAT+  " Registro descargados...."+regSAT.size());
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (in != null) {
					in.close();	
				}	
				if (con != null) {
					con.close();	
				}	
				con = null;
			}catch(Exception e) {
				e = null;
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public String iniciaProcesoGuardadoDefinitivo(Connection con, String esquema, ArrayList<String[]> regSAT, String fechaActual, String nombreArticulo, int numColumna) {
		StringBuffer sbQuery = new StringBuffer();
		String queryFinal = null;
		int totGuardar = 0;
		ArrayList<String[]> guardarLista = new ArrayList<>();
		try {
			sbQuery.append(ProcesoArchivosQuerys.getGuardarListaNegra(esquema));
			for (int x = 0; x < regSAT.size(); x++) {
				sbQuery.append("(?,?,?,?,?),");
				totGuardar++;
				
				guardarLista.add(regSAT.get(x));
				if (totGuardar == 500) {
					queryFinal = sbQuery.substring(0, sbQuery.length() - 1);
					
					guardarRegistrosDefinitivo(con, esquema, guardarLista, queryFinal, fechaActual, nombreArticulo);
					
					totGuardar = 0;
					sbQuery.setLength(0);
					sbQuery.append(ProcesoArchivosQuerys.getGuardarListaNegra(esquema));
					guardarLista = new ArrayList<>();
				}
			}
			
			if (totGuardar > 0) {
				queryFinal = sbQuery.substring(0, sbQuery.length() - 1);
				guardarRegistrosDefinitivo(con, esquema, guardarLista, queryFinal, fechaActual, nombreArticulo);
				totGuardar = 0;
				sbQuery.setLength(0);
				sbQuery.append(ProcesoArchivosQuerys.getGuardarListaNegra(esquema));
				guardarLista = new ArrayList<>();
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return queryFinal;
	}
	

	public int guardarRegistrosDefinitivo(Connection con, String esquema, ArrayList<String[]> guardarLista, String queryInsert, String fechaActual, String nombreArticulo ) {
		PreparedStatement stmt = null;
		String[] cadenaSAT = null;
		try {
			stmt = con.prepareStatement(queryInsert);
			int numParam=1;
			for (int x = 0; x < guardarLista.size(); x++) {
				cadenaSAT = guardarLista.get(x);
				if (cadenaSAT != null && cadenaSAT.length > 2) {
					stmt.setString(numParam++, cadenaSAT[0]); // RFC
					stmt.setString(numParam++, cadenaSAT[1]); // Razon Social
					stmt.setString(numParam++, cadenaSAT[3]);  // supuesto
					stmt.setString(numParam++, nombreArticulo); // nombre articulo 
					stmt.setString(numParam++, "1");			// disponible	
				}
			}
			stmt.executeUpdate();
		}catch(Exception e) {
			//System.err.println("stmt===>"+stmt);
			guardarRegistrosUnoDefinitivo(con, esquema, guardarLista, fechaActual, nombreArticulo);
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return 100;
	}

	
	public int guardarRegistrosUnoDefinitivo(Connection con, String esquema, ArrayList<String[]> guardarLista, String fechaActual, String nombreArticulo) {
		PreparedStatement stmt = null;
		String[] cadenaSAT = null;
		try {
			stmt = con.prepareStatement(ProcesoArchivosQuerys.getGuardarListaNegraUno(esquema));
			for (int x = 0; x < guardarLista.size(); x++) {
				try {
					cadenaSAT = guardarLista.get(x);
					if (cadenaSAT != null &&  cadenaSAT.length > 2) {
						stmt.setString(1, cadenaSAT[0]);
						stmt.setString(2, cadenaSAT[1]);
						stmt.setString(3, cadenaSAT[3]);
						stmt.setString(4, nombreArticulo);
						stmt.setString(5, "1");
						stmt.executeUpdate();	
					}else {
						StringBuffer sbCadena = new StringBuffer();
						for (int y = 0; y < cadenaSAT.length; y++) {
							sbCadena.append(cadenaSAT[y]).append(" ");
						}
					}
				}catch(Exception e) {
					String rfc = "";
					if (cadenaSAT.length > 0) {
						rfc = cadenaSAT[1];
					}
					//Utils.imprimeLog(msg, e);
					logger.info("Error al guardar registro del SAT : "+e + "Registro : "+rfc);
				}
				
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return 100;
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
	 
	
	
	public void procesoListaNegraCondal2015(String esquema, String urlSAT, String nombreArticulo, String nombreSupuesto, int numColumna, int numCabecero) {
		URL url = null;
		ArrayList<String[]> regSAT = new ArrayList<>();
		BufferedReader in = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		
		String cadSAT [] = new String[4];
		String newInputlineFinal = null;
		String inputLine = null;
		try {
			logger.info("Detonando descarga de lista negra del URL...."+urlSAT);
			if (urlSAT == null || "".equals(urlSAT)) {
				logger.info("No se encuentra configurada una URL para ejecutar el proceso...");
			}else {
				connPool = new ConexionDB();
				//rc = connPool.getConnectionJDBC(esquema);
				rc = connPool.getConnectionSiarex();
				con = rc.getCon();
				
				url = new URL(urlSAT);
				
				url = new URL(urlSAT);
				URLConnection uc = url.openConnection();
				in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.ISO_8859_1));
				
				//in = new BufferedReader(new InputStreamReader(url.openStream(), UTF8));
				
				
				//int x = 0;
				int contExcel=0;
				//Scanner scanner = new Scanner(new File(csvFile));
				List<String> line = null;
				String RAZON_SOCIAL = null;
				while ((inputLine = in.readLine()) != null) {
					contExcel++;
					try {
						if (numCabecero <= contExcel) {
							line = parseLine(inputLine);
							if (line.size() > 2) {
								cadSAT[0] = line.get(2);
								RAZON_SOCIAL = line.get(3);
								if (RAZON_SOCIAL.length() > 1500) {
									cadSAT[1] = RAZON_SOCIAL.substring(0, 1500);
								}else {
									cadSAT[1] = RAZON_SOCIAL;	
								}
								cadSAT[3] = nombreSupuesto;
						    	 //cadSAT = newInputlineFinal.split(",");
								regSAT.add(cadSAT);	
							}
							
							cadSAT = new String[4];
						}
					}catch(Exception e) {
						logger.info("Cadena---->"+newInputlineFinal);
						Utils.imprimeLog("", e);
					}
					
					
				}
				in.close();
				
				logger.info("Total Condonados====>"+regSAT.size());
				if (regSAT.size() > 0) {
					String fechaActual = Utils.getFechayyyyMMdd();
					deleteListaNegraTemp(con, esquema);
					// iniciaProcesoGuardado(con, esquema, regSAT, fechaActual, nombreArticulo, numColumna);
					iniciaProcesoGuardado(con, esquema, regSAT, fechaActual, nombreArticulo, numColumna);
					updateListaNegra(con, esquema, numColumna, nombreArticulo);
					guardarListaNegraMasivo(con, esquema, nombreArticulo,  numColumna);
					deleteListaNegraTemp(con, esquema);
					
				}
				
			}
			
			logger.info("URL..."+urlSAT+  " Registro descargados...."+regSAT.size());
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
			
		}finally {
			try {
				if (in != null) {
					in.close();	
				}	
				if (con != null) {
					con.close();	
				}	
				con = null;
			}catch(Exception e) {
				e = null;
			}
		}
	}
	
	
	public int updateListaNegra(Connection con, String esquema, int numColumna, String nombrArticulo) {
		PreparedStatement stmt = null;
		int totReg = 0;
		try {
			//String CAMPO_DISPONIBLE = "DISPONIBLE_"+numColumna;
			stmt = con.prepareStatement(ProcesoArchivosQuerys.getUpdateListaNegra(esquema, numColumna));
			stmt.setString(1, nombrArticulo);
			totReg = stmt.executeUpdate();
			logger.info("Total de Registros actualizados...."+totReg);
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return totReg ;
	}
	
	/*
	public int updateListaNegraCondonados(Connection con, String esquema, int numColumna, String nombrArticulo) {
		PreparedStatement stmt = null;
		int totReg = 0;
		try {
			//String CAMPO_DISPONIBLE = "DISPONIBLE_"+numColumna;
			stmt = con.prepareStatement(ProcesoArchivosQuerys.getUpdateListaNegraCondonados(esquema, numColumna));
			stmt.setString(1, nombrArticulo);
			totReg = stmt.executeUpdate();
			logger.info("Total de Registros actualizados...."+totReg);
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return totReg ;
	}
	*/
	
	
	public int guardarListaNegraMasivo(Connection con, String esquema, String nombreArticulo, int numColumna) {
		PreparedStatement stmt = null;
		int totReg = 0;
		try {
			stmt = con.prepareStatement(ProcesoArchivosQuerys.getGuardarListaNegraMasivo(esquema, numColumna));
			stmt.setString(1, nombreArticulo);
			totReg = stmt.executeUpdate();
			// logger.info("Total de Registros insertados...."+totReg);
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return totReg ;
	}
	
	
	
	public int deleteListaNegraTemp(Connection con, String esquema) {
		PreparedStatement stmt = null;
		int totReg = 0;
		try {
			stmt = con.prepareStatement(ProcesoArchivosQuerys.getDeleteListaNegraTemp(esquema));
			totReg = stmt.executeUpdate();
			// logger.info("Total de Registros eliminado...."+totReg);
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return totReg ;
	}
	
	
	
	public String getMonto(String[] cadenaSAT) {
		String monto = null;
		try {
			if ("".equals(cadenaSAT[5])) {
				monto = "0";
			}else {
				monto = cadenaSAT[5];
			}
		}catch(Exception e) {
			monto = "0";
		}
		return monto;
	}
	
	public String getFecha(String[] cadenaSAT) {
		String fecha = null;
		try {
			if ("".equals(cadenaSAT[4])) {
				fecha = "";
			}else {
				fecha = cadenaSAT[4];
			}
		}catch(Exception e) {
			fecha = "";
		}
		return fecha;
	}

	
	public String getFechaPub(String[] cadenaSAT) {
		String fecha = null;
		try {
			if ("".equals(cadenaSAT[6])) {
				fecha = "";
			}else {
				fecha = cadenaSAT[6];
			}
		}catch(Exception e) {
			fecha = "";
		}
		return fecha;
	}
	
	
	public HashMap<Integer, String> obtenerConfiguracion(Connection con, String esquema){
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<Integer, String> mapaConf = new HashMap<>();
		try {
			stmt = con.prepareStatement(ProcesoArchivosQuerys.getConsultarConfiguracion(esquema));
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				mapaConf.put(rs.getInt(1), rs.getString(2));
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if(stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return mapaConf;
	}
	

	//@SuppressWarnings("unchecked")
		private boolean isEjecuto(Connection con, String esquema)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        try{
	        	String fecha = getFechaHoy();
	            stmt = con.prepareStatement( ProcesoArchivosQuerys.getBuscarRespaldo(esquema));
	            stmt.setString(1, "NEG");
	            stmt.setString(2, fecha);
	            rs = stmt.executeQuery();
				if(rs.next()){
					return true;
	            }
				return false;
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
	        return false;
	    }
		
		
	private int numEjecuciones (Connection con, String esquema, int year){
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int totEjecuciones = 1;
	        try{
	        	stmt = con.prepareStatement( ProcesoArchivosQuerys.getNumEjecuciones(esquema));
	            stmt.setString(1, "NEG");
	            stmt.setInt(2, year);
	            rs = stmt.executeQuery();
				if(rs.next()){
					totEjecuciones = rs.getInt(1);
	            }
				
				if (totEjecuciones == 0) {
					totEjecuciones = 1;
				}
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
	        return totEjecuciones;
	    }
				
				
		
		private void grabarProceso(Connection con, String esquema, int totEnvios, String tipoEnvio)
	    {
	        PreparedStatement stmt = null;
	        try{
	            stmt = con.prepareStatement( ProcesoArchivosQuerys.getGrabarProceso(esquema));
	            stmt.setString(1, "NEG");
	            stmt.setString(2, tipoEnvio);
	            stmt.setInt(3, totEnvios);
	            stmt.setString(4, "OK");
	            stmt.executeUpdate();
	        }catch(Exception e){
	        	Utils.imprimeLog("", e);
	        }finally{
		        try{
		            if(stmt != null)
		                stmt.close();
		            stmt = null;
		        }catch(Exception e){
		            stmt = null;
		        }
	        }
	    }
		
		public String getFechaHoy() {
			String fechaHoy = null;
			try {
					fechaHoy = Utils.getFechayyyyMMdd();
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}
			return fechaHoy ;
		}
		

		private String obtenerUltimaCorrida(Connection con, String esquema)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        String fechaCorrida = getFechaHoy(); 
	        try{
	        	 
	            stmt = con.prepareStatement( ProcesoArchivosQuerys.getUltimaCorrida(esquema));
	            stmt.setString(1, "NEG");
	            rs = stmt.executeQuery();
				if(rs.next()){
					fechaCorrida = Utils.noNulo(rs.getString(1));
	            }
				if ("".equalsIgnoreCase(fechaCorrida)) {
					fechaCorrida = getFechaHoy(); 
				}
				// logger.info("fechaCorrida===>"+fechaCorrida);
				
	        }catch(Exception e){
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
	        return fechaCorrida;
	    }

		
		public int  guardarHistoricoListaNegra(Connection con, String esquema){
			PreparedStatement stmt = null;
			int totalRegistro = 0;
			try {
				stmt = con.prepareStatement(ProcesoArchivosQuerys.getGuardarHistorico(esquema));
				totalRegistro = stmt.executeUpdate();
				
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally {
				try {
					if(stmt != null) {
						stmt.close();
					}
					stmt = null;
				}catch(Exception e) {
					stmt = null;
				}
			}
			return totalRegistro;
		}
		
		
		public int  eliminarListaNegra(Connection con, String esquema){
			PreparedStatement stmt = null;
			int totalRegistro = 0;
			try {
				stmt = con.prepareStatement(ProcesoArchivosQuerys.getEliminarListaNegra(esquema));
				totalRegistro = stmt.executeUpdate();
				
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally {
				try {
					if(stmt != null) {
						stmt.close();
					}
					stmt = null;
				}catch(Exception e) {
					stmt = null;
				}
			}
			return totalRegistro;
		}
		
		public int  setAutoIncrementListaNegra(Connection con, String esquema){
			PreparedStatement stmt = null;
			int totalRegistro = 0;
			try {
				stmt = con.prepareStatement(ProcesoArchivosQuerys.getSetAutoincrementListaNegra(esquema));
				totalRegistro = stmt.executeUpdate();
				
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally {
				try {
					if(stmt != null) {
						stmt.close();
					}
					stmt = null;
				}catch(Exception e) {
					stmt = null;
				}
			}
			return totalRegistro;
		}
		
		
		
	public static void main(String[] args) {
		ProcesoListaNegraBean p = new ProcesoListaNegraBean();
		try {
			p.iniciaProceso();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
