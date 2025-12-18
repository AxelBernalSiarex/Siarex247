package com.siarex247.dashboard.Monitor360;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaSATForm;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaSATQuerys;
import com.siarex247.cumplimientoFiscal.ListaNegra.ListaNegraBean;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import java.time.YearMonth;
public class Monitor360Bean {

	public static final Logger logger = Logger.getLogger("siarex247");
	private DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
	private DecimalFormat formatEntero = new DecimalFormat("######");
	private DecimalFormat formatEnteroDecimal = new DecimalFormat("######.##");
	private DecimalFormat formatEnteroSinDecimal = new DecimalFormat("###,###");
	
	private final String signoPesos = "$";
	
	public Monitor360Form calcularGrafica (Connection con, String esquema, String rfcEmpresa, int annio, String mesCombo, String tipo, 
			String contribuyente, String tipoMoneda){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Monitor360Form monitor360Form = new Monitor360Form();
		
		try {
			
			
			double arrIngresos [] = {0,0,0,0,0,0,0,0,0,0,0,0};
			double arrEgresos  [] = {0,0,0,0,0,0,0,0,0,0,0,0};
			if ("1".equalsIgnoreCase(Utils.noNulo(tipo))) {
				arrIngresos = calcularGraficaIngresos(con, esquema, annio, mesCombo, tipoMoneda);
            }else if ("2".equalsIgnoreCase(Utils.noNulo(tipo))) {
            	arrEgresos = calcularGraficaEgresos(con, esquema, annio, mesCombo,tipo, contribuyente, tipoMoneda);
            }else {
            	arrIngresos = calcularGraficaIngresos(con, esquema, annio, mesCombo, tipoMoneda);
            	arrEgresos = calcularGraficaEgresos(con, esquema, annio, mesCombo, tipo, contribuyente, tipoMoneda);
            }
			
			monitor360Form.setArrEgresos(arrEgresos);
			monitor360Form.setArrIngresos(arrIngresos);
			
			double numeroMayorIngresos = Utils.calcularNumeroMayor(arrIngresos);
			double numeroMayorEgresos = Utils.calcularNumeroMayor(arrEgresos);
			logger.info("numeroMayorIngresos==>"+numeroMayorIngresos);
			logger.info("numeroMayorEgresos==>"+numeroMayorEgresos);
			
			double numeroMayorGrafica = 0;
			if (numeroMayorIngresos > numeroMayorEgresos) {
				monitor360Form.setNumeroMayor(formatEntero.format(numeroMayorIngresos));
				numeroMayorGrafica = numeroMayorIngresos;
			}else {
				monitor360Form.setNumeroMayor(formatEntero.format(numeroMayorEgresos));
				numeroMayorGrafica = numeroMayorEgresos;
			}
			
			float intervalo = Math.round(numeroMayorGrafica / 10);
			logger.info("intervalo==>"+intervalo);
			
			monitor360Form.setIntervalo(intervalo);
			
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return monitor360Form;
	}
	
	
	
	private double[] calcularGraficaIngresos(Connection con, String esquema, int annio, String mesCombo, String tipoMoneda){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double arrIngresos [] = {0,0,0,0,0,0,0,0,0,0,0,0}; 
		try {	
			String fechaIni  = armarFechaInicial(annio, mesCombo);
            String fechaFin  = armarFechaFinal(annio, mesCombo);
            
            StringBuffer sbQuery = new StringBuffer();
            sbQuery.append(Monitor360Querys.getCalcularGraficaIngresos(esquema));
            
            if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
            	sbQuery.append(" and MONEDA = ? ");
            }
            
            stmt = con.prepareStatement(sbQuery.toString());
			stmt.setString(1, fechaIni);
			stmt.setString(2, fechaFin);
			stmt.setString(3, "I");
			// stmt.setString(4, "ingreso");
			stmt.setString(4, "E");
			// stmt.setString(6, "egreso");
			// stmt.setString(5, "V");
			if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
				stmt.setString(5, Utils.noNulo(tipoMoneda));
			}
			logger.info("stmtIngresos ==> "+stmt);
			rs  = stmt.executeQuery();
			String numMes = "";
			double totalRegistro = 0;
			while (rs.next()) {
				numMes = Utils.noNulo(rs.getString(1)).substring(5, 7);
				totalRegistro = rs.getDouble(2);
				int mesCalendario = validarMes(numMes);
				arrIngresos[mesCalendario]+= totalRegistro;
			}
			
			for (int x = 0; x < arrIngresos.length; x++) {
				arrIngresos[x] = Utils.noNuloDouble(formatEnteroDecimal.format(arrIngresos[x]));
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return arrIngresos;
	}
	
	
	
	private double[] calcularGraficaEgresos(Connection con, String esquema, int annio, String mesCombo, String tipo, String contribuyente, String tipoMoneda){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double arrEgresos [] = {0,0,0,0,0,0,0,0,0,0,0,0}; 
		try {	
			String fechaIni  = armarFechaInicial(annio, mesCombo);
            String fechaFin  = armarFechaFinal(annio, mesCombo);
            
            StringBuffer sbQuery = new StringBuffer(Monitor360Querys.getCalcularGraficaEgresos(esquema));
            
            if ("2".equalsIgnoreCase(tipo) && !"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
            	sbQuery.append(" and EMISOR_RFC = ?");
            }
            
            if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
            	sbQuery.append(" and DES_TIPO_MONEDA = ?");
            }
            
            stmt = con.prepareStatement(sbQuery.toString());
			stmt.setString(1, fechaIni);
			stmt.setString(2, fechaFin);
			stmt.setString(3, "I");
			stmt.setString(4, "ingreso");
			stmt.setString(5, "E");
			stmt.setString(6, "egreso");
			// stmt.setString(7, "V");
			
			int numParam = 7;
			if ("2".equalsIgnoreCase(tipo) && !"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
				stmt.setString(numParam++, contribuyente);
            }
			
			if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
				stmt.setString(numParam++, Utils.noNulo(tipoMoneda));
            }
			
			
			logger.info("stmtEgresos ==>"+stmt);
			rs  = stmt.executeQuery();
			String numMes = "";
			double totalRegistro = 0;
			while (rs.next()) {
				numMes = Utils.noNulo(rs.getString(1)).substring(5, 7);
				totalRegistro = rs.getDouble(2);
				int mesCalendario = validarMes(numMes);
				arrEgresos[mesCalendario]+= totalRegistro;
			}
			
			for (int x = 0; x < arrEgresos.length; x++) {
				arrEgresos[x] = Utils.noNuloDouble(formatEnteroDecimal.format(arrEgresos[x]));
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return arrEgresos;
	}
	
	
	public Monitor360Form calcularUniverso (Connection con, String esquema, String rfcEmpresa, int annio, String mesCombo, String tipo, String contribuyente, String tipoMoneda){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double totalUniverso = 0;
		double totalCancelados = 0;
		double totalMetadata = 0;
		Monitor360Form monitor360Form = new Monitor360Form();
		
		try {
			logger.info("contribuyente===>"+contribuyente);
			String fechaIni  = armarFechaInicial(annio, mesCombo);
            String fechaFin  = armarFechaFinal(annio, mesCombo);
			StringBuffer campoTipo = new StringBuffer("");
            if ("1".equalsIgnoreCase(Utils.noNulo(tipo))) {
            	campoTipo.append(" and EMISOR_RFC = ?");
            }else if ("2".equalsIgnoreCase(Utils.noNulo(tipo))) {
            	if (!"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
            		campoTipo.append(" and EMISOR_RFC = ? and RECEPTOR_RFC = ? ");
            	}else {
            		campoTipo.append(" and RECEPTOR_RFC = ? ");
            	}
            	
            }
            
            if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
            	campoTipo.append(" and TIPO_MONEDA = ? ");
            }
            
			stmt = con.prepareStatement(Monitor360Querys.getCalcularUniverso(esquema, campoTipo.toString()));
			stmt.setString(1, fechaIni);
			stmt.setString(2, fechaFin);
			stmt.setString(3, "I");
			stmt.setString(4, "E");
			
			int numParam=5;
			if ("1".equalsIgnoreCase(Utils.noNulo(tipo))) {
				stmt.setString(numParam++, rfcEmpresa);
            }else if ("2".equalsIgnoreCase(Utils.noNulo(tipo))) {
            	if (!"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
            		stmt.setString(numParam++, contribuyente);
            		stmt.setString(numParam++, rfcEmpresa);
            	}else {
            		stmt.setString(numParam++, rfcEmpresa);
            	}
            	
            }
			
			if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
				stmt.setString(numParam++, Utils.noNulo(tipoMoneda));
            }
			
			logger.info("stmtCebecero==>"+stmt);
			rs  = stmt.executeQuery();
			String estatus = "";
			String existeBoveda = "";
			while (rs.next()) {
				estatus = Utils.noNulo(rs.getString(2));
				existeBoveda = Utils.noNulo(rs.getString(1));
				totalUniverso+= rs.getInt(3);
				
				if ("CANCELADO".equalsIgnoreCase(estatus)) {
					totalCancelados+=  rs.getInt(3);
				}
				
				if ("S".equalsIgnoreCase(existeBoveda)) {
					totalMetadata+=  rs.getInt(3);
				}
			}
			
			double totalComplementos = calcularComplementos(con, esquema, rfcEmpresa, annio, mesCombo, tipo, contribuyente, tipoMoneda);
			int totalListaNegra = calcularListaNegra(con, esquema, annio, mesCombo, tipo, contribuyente, rfcEmpresa, tipoMoneda);
			logger.info("totalUniverso===>"+totalUniverso);
			logger.info("totalMetadata===>"+totalMetadata);
			logger.info("totalCancelados===>"+totalCancelados);
			logger.info("totalComplementos===>"+totalComplementos);
			logger.info("totalListaNegra===>"+totalListaNegra);
			
			double porcentajeMetadata =  Utils.calcularPorcentaje(totalMetadata, totalUniverso );
			double porcentajeCancelados = Utils.calcularPorcentaje(totalCancelados, totalUniverso);
			double porcentajeComplementos = Utils.calcularPorcentaje(totalComplementos, totalUniverso);
			
			
			monitor360Form.setTotalUniverso(formatEnteroSinDecimal.format(totalUniverso));
			monitor360Form.setPorcentajeMetadata(decimalFormat.format(porcentajeMetadata) + " %");
			monitor360Form.setPorcentajeCancelados(decimalFormat.format(porcentajeCancelados) + " %");
			monitor360Form.setPorcentajeComplementos(decimalFormat.format(porcentajeComplementos) + " %");
			monitor360Form.setTotalListaNegra(totalListaNegra);
			
			
			logger.info("porcentajeMetadata===>"+porcentajeMetadata);
			logger.info("porcentajeCancelados===>"+porcentajeCancelados);
			
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return monitor360Form;
	}
	
	
	
	public HashMap<String, String> calcularIngresos (Connection con, String esquema, int annio, String mesCombo, String tipo, String tipoMoneda){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double totalIngreso = 0;
		double subTotalIngreso = 0;
		double totalImpRetenido = 0;
		double totalImpTraslado = 0;
		HashMap<String, String> mapaIngresos = new HashMap<>();
		
		try {	
			String fechaIni  = armarFechaInicial(annio, mesCombo);
            String fechaFin  = armarFechaFinal(annio, mesCombo);
            
            mapaIngresos.put("TOTAL_INGRESOS", "0");
			mapaIngresos.put("SUBTOTAL_INGRESOS", "0");
			mapaIngresos.put("IMPRETENIDOS_INGRESOS", "0");
			mapaIngresos.put("IMPTRASLADO_INGRESOS", "0");
			
			if ("1".equalsIgnoreCase(Utils.noNulo(tipo)) || "".equalsIgnoreCase(Utils.noNulo(tipo))) {
				
				StringBuffer sbQuery = new StringBuffer();
	            sbQuery.append(Monitor360Querys.getCalcularIngresos(esquema));
	            
	            if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
	            	sbQuery.append(" and MONEDA = ? ");
	            }
	            
				
				stmt = con.prepareStatement(sbQuery.toString());
				stmt.setString(1, fechaIni);
				stmt.setString(2, fechaFin);
				stmt.setString(3, "I");
				// stmt.setString(4, "ingreso");
				stmt.setString(4, "E");
				// stmt.setString(6, "egreso");
				// stmt.setString(5, "V");
				
				 if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
					 stmt.setString(5, Utils.noNulo(tipoMoneda));
		         }
				 
				 logger.info("stmtIngresos Pie===>"+stmt);
				rs  = stmt.executeQuery();
				if (rs.next()) {
					totalIngreso = rs.getDouble(1);
					subTotalIngreso = rs.getDouble(2);
					totalImpRetenido = rs.getDouble(3);
					totalImpTraslado = rs.getDouble(4);
					
				}
				
				mapaIngresos.put("TOTAL_INGRESOS", signoPesos + " " + decimalFormat.format(totalIngreso));
				mapaIngresos.put("SUBTOTAL_INGRESOS", signoPesos + " " + decimalFormat.format(subTotalIngreso));
				mapaIngresos.put("IMPRETENIDOS_INGRESOS", signoPesos + " " + decimalFormat.format(totalImpRetenido));
				mapaIngresos.put("IMPTRASLADO_INGRESOS", signoPesos + " " + decimalFormat.format(totalImpTraslado));
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return mapaIngresos;
	}
	
	
	public double calcularComplementos (Connection con, String esquema, String rfcEmpresa, int annio, String mesCombo, String tipo, String contribuyente, String tipoMoneda){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double totalComplementos = 0;
		
		try {	
			String fechaIni  = armarFechaInicial(annio, mesCombo);
            String fechaFin  = armarFechaFinal(annio, mesCombo);
            if ("2".equalsIgnoreCase(Utils.noNulo(tipo)) || "".equalsIgnoreCase(Utils.noNulo(tipo))) {
            	
    			StringBuffer campoTipo = new StringBuffer("");
			 	if ("2".equalsIgnoreCase(Utils.noNulo(tipo))) {
	            	if (!"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
	            		campoTipo.append(" and A.EMISOR_RFC = ? and A.RECEPTOR_RFC = ? ");
	            	}else {
	            		campoTipo.append(" and A.RECEPTOR_RFC = ? ");
	            	}
	            	
	            }
    	            
                if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
                	campoTipo.append(" and A.TIPO_MONEDA = ? ");
                }
                
            	
                
    			stmt = con.prepareStatement( Monitor360Querys.getCalcularComplementos(esquema, campoTipo.toString()));
    			stmt.setString(1, fechaIni);
    			stmt.setString(2, fechaFin);
    			stmt.setString(3, "I");
    			stmt.setString(4, "E");
    			
    			int numParam=5;
    			if ("2".equalsIgnoreCase(Utils.noNulo(tipo))) {
                	if (!"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
                		stmt.setString(numParam++, contribuyente);
                		stmt.setString(numParam++, rfcEmpresa);
                	}else {
                		stmt.setString(numParam++, rfcEmpresa);
                	}
                }
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
    				stmt.setString(numParam++, Utils.noNulo(tipoMoneda));
                }
    			
    			logger.info("stmtComplementos ==>"+stmt);
    			rs  = stmt.executeQuery();
    			
    			if (rs.next()) {
    				totalComplementos = rs.getDouble(1);
    				
    			}
            }
            
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return totalComplementos;
	}
	
	public int calcularListaNegra (Connection con, String esquema, int annio, String mesCombo, String tipo, String contribuyente, String rfcEmpresa, String tipoMoneda){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totalListaNegra = 0;
		String baseLista = "LISTA_NEGRA_SAT";
		try {	
			int annioActual = Integer.parseInt(UtilsFechas.getFechaYear());
			
			String fechaIni  = armarFechaInicial(annio, "01");
            String fechaFin  = armarFechaFinal(annio, "12");
            
            if ("2".equalsIgnoreCase(Utils.noNulo(tipo)) || "".equalsIgnoreCase(Utils.noNulo(tipo))) {
            	ListaNegraBean listanegraBean = new ListaNegraBean();
            	ArrayList<String> arrLista =  listanegraBean.consultaColumnas(annio);
            	int numeroDisponible = arrLista.size();
            	
            	
            	String campoDisponible = "DISPONIBLE_"+numeroDisponible;
            	logger.info("campoDisponible===>"+campoDisponible);
            	
            	StringBuffer sbAnd = new StringBuffer();
            	if ("2".equalsIgnoreCase(Utils.noNulo(tipo)) && !"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
            		sbAnd.append(" and A.EMISOR_RFC = ? ");
            	}
            	
            	if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
            		sbAnd.append(" and A.TIPO_MONEDA = ? ");
            	}
            	
            	if (annioActual == annio) {
            		baseLista = "LISTA_NEGRA_SAT";
            	}else {
            		baseLista = "LISTA_NEGRA_SAT_HISTORICO";
            	}
            	
    			stmt = con.prepareStatement( Monitor360Querys.getCalcularListaNegra(esquema, baseLista, campoDisponible, sbAnd.toString()));
    			stmt.setString(1, fechaIni);
    			stmt.setString(2, fechaFin);
    			stmt.setString(3, "I");
    			stmt.setString(4, "E");
    			stmt.setString(5, rfcEmpresa);
    			stmt.setInt(6, 1);
    			
    			int numParam=7;
    			if ("2".equalsIgnoreCase(Utils.noNulo(tipo)) && !"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
    				stmt.setString(numParam++, contribuyente );
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
    				stmt.setString(numParam++, Utils.noNulo(tipoMoneda) );
            	}
    			
    			rs  = stmt.executeQuery();
    			logger.info("stmtListaNegra ==>"+stmt);
    			if (rs.next()) {
    				totalListaNegra = rs.getInt(1);
    				
    			}
            }
            
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return totalListaNegra;
	}
	
	
	public HashMap<String, String> calcularEgresos (Connection con, String esquema, int annio, String mesCombo, String tipo, String contribuyente, String tipoMoneda){
	
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double totalEgresos = 0;
		double subTotalEgresos = 0;
		double totalImpRetenido = 0;
		double totalImpTraslado = 0;
		HashMap<String, String> mapaEgresos = new HashMap<>();
		
		try {	
			String fechaIni  = armarFechaInicial(annio, mesCombo);
            String fechaFin  = armarFechaFinal(annio, mesCombo);
            
			mapaEgresos.put("TOTAL_EGRESOS", "0");
			mapaEgresos.put("SUBTOTAL_EGRESOS", "0");
			mapaEgresos.put("IMPRETENIDOS_EGRESOS", "0");
			mapaEgresos.put("IMPTRASLADO_EGRESOS", "0");
			if ("2".equalsIgnoreCase(Utils.noNulo(tipo)) || "".equalsIgnoreCase(Utils.noNulo(tipo))) {
				
				StringBuffer sbQuery = new StringBuffer(Monitor360Querys.getCalcularEgresos(esquema));
				if ("2".equalsIgnoreCase(Utils.noNulo(tipo)) && !"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
					sbQuery.append(" and EMISOR_RFC = ?");
				}
				if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
					sbQuery.append(" and DES_TIPO_MONEDA = ?");
				}
				
				stmt = con.prepareStatement(sbQuery.toString());
				stmt.setString(1, fechaIni);
				stmt.setString(2, fechaFin);
				stmt.setString(3, "I");
				stmt.setString(4, "ingresos");	
				stmt.setString(5, "E");
				stmt.setString(6, "egresos");
				// stmt.setString(7, "V");
				
				int numParam = 7;
				if ("2".equalsIgnoreCase(Utils.noNulo(tipo)) && !"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
					stmt.setString(numParam++, contribuyente );
				}
				
				if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
					stmt.setString(numParam++, Utils.noNulo(tipoMoneda) );
				}
				
				logger.info("stmtEgresos Pie===>"+stmt);
				rs  = stmt.executeQuery();
				
				if (rs.next()) {
					totalEgresos = rs.getDouble(1);
					subTotalEgresos = rs.getDouble(2);
					totalImpRetenido = rs.getDouble(3);
					totalImpTraslado = rs.getDouble(4);
					
				}
				
				mapaEgresos.put("TOTAL_EGRESOS", signoPesos + " " + decimalFormat.format(totalEgresos));
				mapaEgresos.put("SUBTOTAL_EGRESOS", signoPesos + " " + decimalFormat.format(subTotalEgresos));
				mapaEgresos.put("IMPRETENIDOS_EGRESOS", signoPesos + " " + decimalFormat.format(totalImpRetenido));
				mapaEgresos.put("IMPTRASLADO_EGRESOS", signoPesos + " " + decimalFormat.format(totalImpTraslado));
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return mapaEgresos;
	}
	
	
	public ArrayList<Monitor360Form> obtenerAnnios (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Monitor360Form> listaDetalle = new ArrayList<>();
		Monitor360Form monitor360Form = new Monitor360Form();
		try {	
			stmt = con.prepareStatement(Monitor360Querys.getObtenerAnnios(esquema));
			rs  = stmt.executeQuery();
			String fechaMinima = "";
			if (rs.next()) {
				fechaMinima = Utils.noNulo(rs.getString(1));
				int annioMin = Integer.parseInt(fechaMinima.substring(0, 4));
				int annioActual = Integer.parseInt(UtilsFechas.getFechaYear());
				for (int x = annioActual; x >= annioMin; x--) {
					monitor360Form.setAnnio(x);
					monitor360Form.setDesAnnio(String.valueOf(x));
					listaDetalle.add(monitor360Form);
					monitor360Form = new Monitor360Form();
				}
				
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaDetalle;
	}
	
	
	public ArrayList<Monitor360Form> obtenerContribuyentes (Connection con, String esquema, String tipo, String rfcEmpresa, String nombreEmpresa){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Monitor360Form> listaDetalle = new ArrayList<>();
		Monitor360Form monitor360Form = new Monitor360Form();
		try {	
			monitor360Form.setRfc("");
			monitor360Form.setRazonSocial("Seleccione un Contribuyente");
			listaDetalle.add(monitor360Form);
			monitor360Form = new Monitor360Form();
			
			// logger.info("tipo====>"+tipo);
			if ("1".equalsIgnoreCase(Utils.noNulo(tipo))) {
				monitor360Form.setRfc(rfcEmpresa);
				monitor360Form.setRazonSocial(rfcEmpresa + " - " + nombreEmpresa);
				listaDetalle.add(monitor360Form);
				monitor360Form = new Monitor360Form();
			}else if ("2".equalsIgnoreCase(Utils.noNulo(tipo))){
				stmt = con.prepareStatement(Monitor360Querys.getObtenerContribuyentes(esquema));
				// logger.info("stmtContribuyentes ====>"+stmt);
				rs  = stmt.executeQuery();
				while (rs.next()) {
					monitor360Form.setRfc(Utils.noNulo(rs.getString(1)));
					monitor360Form.setRazonSocial(monitor360Form.getRfc() + " - " + Utils.noNuloNormal(rs.getString(2)));
					listaDetalle.add(monitor360Form);
					monitor360Form = new Monitor360Form();
				}
			}
			
			
			
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaDetalle;
	}
	
	
	public ArrayList<DescargaSATForm>  detalleDescarga (Connection con, String esquema,  String rfcEmpresa,
			String fechaInicial, String fechaFinal, String tipoConsulta, String tipo, String contribuyente, String tipoMoneda){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<DescargaSATForm> listaDetalle = new ArrayList<DescargaSATForm>();
		DescargaSATForm descargaSATForm = new DescargaSATForm();
		try {
			StringBuffer sbQuery = new StringBuffer(DescargaSATQuerys.getDetalleGraficaMonitor(esquema));
			if ("CANCELADOS".equalsIgnoreCase(tipoConsulta)) {
				sbQuery.append(" and ESTATUS = ? ");
			}else if ("METADATA".equalsIgnoreCase(tipoConsulta)) {
				sbQuery.append(" and EXISTE_BOVEDA = ? ");
			}
			
			
			String campoTipo = "";
			if ("1".equalsIgnoreCase(Utils.noNulo(tipo))) {
            	campoTipo = " and EMISOR_RFC = ?";
            }else if ("2".equalsIgnoreCase(Utils.noNulo(tipo))) {
            	if (!"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
            		campoTipo = " and EMISOR_RFC = ? and RECEPTOR_RFC = ? ";
            	}else {
            		campoTipo = " and RECEPTOR_RFC = ? ";
            	}
            }
			
			sbQuery.append(campoTipo);
			
			
			if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
				sbQuery.append(" and TIPO_MONEDA = ?");
			}
			
			stmt = con.prepareStatement(sbQuery.toString());
			stmt.setString(1, fechaInicial);
			stmt.setString(2, fechaFinal);
			stmt.setString(3, "I");
			stmt.setString(4, "E");
			
			int numParam = 5;
			
			if ("CANCELADOS".equalsIgnoreCase(tipoConsulta)) {
				stmt.setString(numParam++, "CANCELADO");
			}else if ("METADATA".equalsIgnoreCase(tipoConsulta)) {
				stmt.setString(numParam++, "S");
			}
			
			if ("1".equalsIgnoreCase(Utils.noNulo(tipo))) {
				stmt.setString(numParam++, rfcEmpresa);
            }else if ("2".equalsIgnoreCase(Utils.noNulo(tipo))) {
            	if (!"".equalsIgnoreCase(Utils.noNulo(contribuyente))) {
            		stmt.setString(numParam++, contribuyente);
            		stmt.setString(numParam++, rfcEmpresa);
            		
            	}else {
            		stmt.setString(numParam++, rfcEmpresa);
            	}
            }
			
			if (!"".equalsIgnoreCase(Utils.noNulo(tipoMoneda))) {
				stmt.setString(numParam++, Utils.noNulo(tipoMoneda));
			}
			
			logger.info("stmt==>"+stmt);
			rs = stmt.executeQuery();
			while (rs.next()) {
				
					descargaSATForm.setIdRegistro(rs.getInt(1));
					descargaSATForm.setUuid(Utils.noNuloNormal(rs.getString(2)));
					descargaSATForm.setRfcEmisor(Utils.noNuloNormal(rs.getString(3)));
					descargaSATForm.setNombreEmisor(Utils.noNuloNormal(rs.getString(4)));
					descargaSATForm.setRfcReceptor(Utils.noNuloNormal(rs.getString(5)));
					descargaSATForm.setNombreReceptor(Utils.noNuloNormal(rs.getString(6)));
					//descargaSATForm.setRfcPac(Utils.noNuloNormal(rs.getString(7)));
					descargaSATForm.setRfcPac("");
					descargaSATForm.setFechaEmision(Utils.noNuloNormal(rs.getString(8)));
					descargaSATForm.setFechaCertificacion(Utils.noNuloNormal(rs.getString(9)));
					descargaSATForm.setMonto(rs.getDouble(10));
					descargaSATForm.setMontoDes(decimalFormat.format(rs.getDouble(10)));
					descargaSATForm.setEfectoComprobante(Utils.noNuloNormal(rs.getString(11)));
					descargaSATForm.setEstatus(Utils.noNuloNormal(rs.getString(12)));
					descargaSATForm.setTipoMoneda(Utils.noNuloNormal(rs.getString(13)));
					descargaSATForm.setFechaCancelacion(Utils.noNuloNormal(rs.getString(14)));
					descargaSATForm.setExisteBoveda(Utils.noNuloNormal(rs.getString(15)));
					listaDetalle.add(descargaSATForm);
					descargaSATForm = new DescargaSATForm();
			}
			
	        
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaDetalle;
	}
	
	
	public ArrayList<DescargaSATForm>  detalleListaNegra (Connection con, String esquema,  String rfcEmpresa,
			int annio, String fechaInicial, String fechaFinal, String tipoConsulta, String tipo, String contribuyente, String tipoMoneda){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<DescargaSATForm> listaDetalle = new ArrayList<DescargaSATForm>();
		DescargaSATForm descargaSATForm = new DescargaSATForm();
		String baseLista = "LISTA_NEGRA_SAT";
		try {
			
			ListaNegraBean listanegraBean = new ListaNegraBean();
        	ArrayList<String> arrLista =  listanegraBean.consultaColumnas(annio);
        	int numeroDisponible = arrLista.size();
        	
        	
        	String campoDisponible = "DISPONIBLE_"+numeroDisponible;
        	//logger.info("campoDisponible===>"+campoDisponible);
        	
        	int annioActual = Integer.parseInt(UtilsFechas.getFechaYear());
        	if (annioActual == annio) {
        		baseLista = "LISTA_NEGRA_SAT";
        	}else {
        		baseLista = "LISTA_NEGRA_SAT_HISTORICO";
        	}
        	
        	
			StringBuffer sbQuery = new StringBuffer(Monitor360Querys.getDetalleListaNegraMetadata(esquema, baseLista, campoDisponible));
			
			stmt = con.prepareStatement(sbQuery.toString());
			stmt.setString(1, fechaInicial);
			stmt.setString(2, fechaFinal);
			stmt.setString(3, "I");
			stmt.setString(4, "E");
			stmt.setString(5, rfcEmpresa);
			stmt.setInt(6, 1);
			stmt.setString(7, tipoMoneda);
			//logger.info("stmt===>"+stmt);
			rs = stmt.executeQuery();
			while (rs.next()) {
					descargaSATForm.setRfcEmisor(Utils.noNuloNormal(rs.getString(1)));
					descargaSATForm.setNombreEmisor(Utils.noNuloNormal(rs.getString(2)));
					listaDetalle.add(descargaSATForm);
					descargaSATForm = new DescargaSATForm();
			}
			
	        
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return listaDetalle;
	}
	
	private int validarMes(String numMes) {
		int mesCalendario = 0;
		try {
			if ("01".equalsIgnoreCase(numMes)) {
				mesCalendario = 0;
			}else if ("02".equalsIgnoreCase(numMes)) {
				mesCalendario = 1;
			}else if ("03".equalsIgnoreCase(numMes)) {
				mesCalendario = 2;
			}else if ("04".equalsIgnoreCase(numMes)) {
				mesCalendario = 3;
			}else if ("05".equalsIgnoreCase(numMes)) {
				mesCalendario = 4;
			}else if ("06".equalsIgnoreCase(numMes)) {
				mesCalendario = 5;
			}else if ("07".equalsIgnoreCase(numMes)) {
				mesCalendario = 6;
			}else if ("08".equalsIgnoreCase(numMes)) {
				mesCalendario = 7;
			}else if ("09".equalsIgnoreCase(numMes)) {
				mesCalendario = 8;
			}else if ("10".equalsIgnoreCase(numMes)) {
				mesCalendario = 9;
			}else if ("11".equalsIgnoreCase(numMes)) {
				mesCalendario = 10;
			}else if ("12".equalsIgnoreCase(numMes)) {
				mesCalendario = 11;
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return mesCalendario;
	}

	
	public String armarFechaInicial(int annio, String mesCombo) {
		String fechaInicial = null;
		if ("".equals(Utils.noNulo(mesCombo))) {
			fechaInicial = annio + "-01-01 00:00:00";
        }else {
        	if ("02".equalsIgnoreCase(mesCombo) && (annio == 2016 || annio == 2020 || annio == 2024 || annio == 2028 || annio == 2032 || annio == 2036)) {
        		fechaInicial = new StringBuffer().append(annio).append("-").append(mesCombo).append("-").append("01 00:00:00").toString();
        	}else if ("02".equalsIgnoreCase(mesCombo)) {
        		fechaInicial = new StringBuffer().append(annio).append("-").append(mesCombo).append("-").append("01 00:00:00").toString();
        	}else if ("04".equalsIgnoreCase(mesCombo) || "06".equalsIgnoreCase(mesCombo) || "09".equalsIgnoreCase(mesCombo) || "11".equalsIgnoreCase(mesCombo)) {
        		fechaInicial = new StringBuffer().append(annio).append("-").append(mesCombo).append("-").append("01 00:00:00").toString();
        	}else {
        		fechaInicial = new StringBuffer().append(annio).append("-").append(mesCombo).append("-").append("01 00:00:00").toString();
        	}
        }
		return fechaInicial;
	}
	
	
	public String armarFechaFinal(int annio, String mesCombo) {
		String fechaFinal = null;
		if ("".equals(Utils.noNulo(mesCombo))) {
	    	fechaFinal = annio + "-12-31 23:59:59";
	    }else {
	    	if ("02".equalsIgnoreCase(mesCombo) && (annio == 2016 || annio == 2020 || annio == 2024 || annio == 2028 || annio == 2032 || annio == 2036)) {
	    		fechaFinal = new StringBuffer().append(annio).append("-").append(mesCombo).append("-").append("29 23:59:59").toString();
	    	}else if ("02".equalsIgnoreCase(mesCombo)) {
	    		fechaFinal = new StringBuffer().append(annio).append("-").append(mesCombo).append("-").append("28 23:59:59").toString();
	    	}else if ("04".equalsIgnoreCase(mesCombo) || "06".equalsIgnoreCase(mesCombo) || "09".equalsIgnoreCase(mesCombo) || "11".equalsIgnoreCase(mesCombo)) {
	    		fechaFinal = new StringBuffer().append(annio).append("-").append(mesCombo).append("-").append("30 23:59:59").toString();
	    	}else {
	    		fechaFinal = new StringBuffer().append(annio).append("-").append(mesCombo).append("-").append("31 23:59:59").toString();
	    	}
	    }
		return fechaFinal;
	}
	
	public Monitor360Form calcularGraficaPorTipoComprobante(Connection con, String esquema, String rfcEmisor, String annio, String tipoMoneda,String mes) throws Exception {
	    Monitor360Form form = new Monitor360Form();
	    Map<String, double[]> mapaComprobantes = new HashMap<>();
	    double maxTotal = 0;

	    PreparedStatement psEmitidos = null;
	    PreparedStatement psNomina = null;
	    PreparedStatement psPagos = null;
	    
	    ResultSet rsEmitidos = null;
	    ResultSet rsPagos = null;
	    ResultSet rsNomina = null;

	    try {
	    	logger.info("rfcEmisor===>"+rfcEmisor);
	    	
	    	String fechaInicio, fechaFin;

	    	if (mes != null && !mes.isEmpty()) {
	    	    // Mes con padding (ej. "03" para marzo)
	    	    int mesInt = Integer.parseInt(mes);
	    	    String mesStr = String.format("%02d", mesInt);
	    	    fechaInicio = annio + "-" + mesStr + "-01 00:00:00";

	    	    // Obtener el último día del mes (para manejar meses con 28, 30 o 31 días)
	    	    YearMonth ym = YearMonth.of(Integer.parseInt(annio), mesInt);
	    	    int lastDay = ym.lengthOfMonth();
	    	    fechaFin = annio + "-" + mesStr + "-" + lastDay + " 23:59:59";
	    	} else {
	    	    fechaInicio = annio + "-01-01 00:00:00";
	    	    fechaFin = annio + "-12-31 23:59:59";
	    	}

	    	StringBuffer sbAnd = new StringBuffer();
	        if (!"".equalsIgnoreCase(Utils.noNulo(rfcEmisor))) {
	        	sbAnd.append(" and EMISOR_RFC = ?");
	        }

	        StringBuilder queryEmitidos = new StringBuilder(Monitor360Querys.getDetalleGraficaEgresos(esquema, sbAnd.toString()));
	        
	        psEmitidos = con.prepareStatement(queryEmitidos.toString());
	        int idx1 = 1;
	        psEmitidos.setString(idx1++, fechaInicio);
	        psEmitidos.setString(idx1++, fechaFin);
	        psEmitidos.setString(idx1++, tipoMoneda);
	        if (!"".equalsIgnoreCase(Utils.noNulo(rfcEmisor))) {
	        	psEmitidos.setString(idx1++, rfcEmisor);
	        }
	        logger.info("Query Emitidos===>"+psEmitidos);
	        
	        rsEmitidos = psEmitidos.executeQuery();
	        while (rsEmitidos.next()) {
	            String tipo = rsEmitidos.getString(1);

	            // ✅ Ignoramos manualmente los 'N' sin romper el índice
	            int mesEmitido  = rsEmitidos.getInt(2) - 1;
	            double total = rsEmitidos.getDouble(3);

	            mapaComprobantes.putIfAbsent(tipo, new double[12]);
	            mapaComprobantes.get(tipo)[mesEmitido ] += total;

	            if (mapaComprobantes.get(tipo)[mesEmitido ] > maxTotal) {
	                maxTotal = mapaComprobantes.get(tipo)[mesEmitido ];
	            }
	        }

	       
	        
	        psPagos= con.prepareStatement(Monitor360Querys.getDetalleGraficaEgresosPagos(esquema, sbAnd.toString()));
	        int idx2 = 1;
	        psPagos.setString(idx2++, fechaInicio);
	        psPagos.setString(idx2++, fechaFin);
	        psPagos.setString(idx2++, tipoMoneda);
	        if (!"".equalsIgnoreCase(Utils.noNulo(rfcEmisor))) {
	        	psPagos.setString(idx2++, rfcEmisor);
	        }

	        logger.info("Query Pagos===>"+psPagos);
	        rsPagos = psPagos.executeQuery();
	        String tipo = "P";
	        while (rsPagos.next()) {
	            int mesPago  = rsPagos.getInt(1) - 1;
	            double total = rsPagos.getDouble(2);

	            mapaComprobantes.putIfAbsent(tipo, new double[12]);
	            mapaComprobantes.get(tipo)[mesPago ] += total;

	            if (mapaComprobantes.get(tipo)[mesPago ] > maxTotal) {
	                maxTotal = mapaComprobantes.get(tipo)[mesPago ];
	            }
	        }
	        
	        psNomina = con.prepareStatement(Monitor360Querys.getDetalleGraficaEgresosNomina(esquema));
	        idx2 = 1;
	        psNomina.setString(idx2++, fechaInicio);
	        psNomina.setString(idx2++, fechaFin);
	        // if (filtrarRFC) psNomina.setString(idx2++, rfcEmisor);
	        psNomina.setString(idx2, tipoMoneda);
	        logger.info("Query Nomina ===>"+psNomina);
	        rsNomina = psNomina.executeQuery();
	        tipo = "N";
	        while (rsNomina.next()) {
	            
	            int mesNomina  = rsNomina.getInt(1) - 1;
	            double total = rsNomina.getDouble(2);

	            mapaComprobantes.putIfAbsent(tipo, new double[12]);
	            mapaComprobantes.get(tipo)[mesNomina ] += total;

	            if (mapaComprobantes.get(tipo)[mesNomina ] > maxTotal) {
	                maxTotal = mapaComprobantes.get(tipo)[mesNomina ];
	            }
	        }

	        form.setMapaPorTipo(mapaComprobantes);
	        form.setNumeroMayor(String.valueOf(maxTotal));
	        form.setIntervalo(Math.round(maxTotal / 10));

	    } catch (Exception e) {
	    	Utils.imprimeLog("", e);
	    } finally {
	        if (rsEmitidos != null) rsEmitidos.close();
	        if (rsPagos != null) rsPagos.close();
	        if (rsNomina != null) rsNomina.close();
	        if (psEmitidos != null) psEmitidos.close();
	        if (psPagos != null) psPagos.close();
	        if (psNomina != null) psNomina.close();
	    }

	    return form;
	}


	
	
	
	public static void main(String[] args) {
		try {
			int x = 0;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
