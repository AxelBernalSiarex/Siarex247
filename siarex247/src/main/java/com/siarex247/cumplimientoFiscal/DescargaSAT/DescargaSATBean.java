package com.siarex247.cumplimientoFiscal.DescargaSAT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsColor;
import com.siarex247.utils.UtilsFechas;

public class DescargaSATBean extends FiltrosDescargaSAT {

	
	private DecimalFormat decimal = new DecimalFormat("###,###.##");
	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public ArrayList<DescargaSATForm> detalleDescarga(
	        Connection con, String esquema,
	        // ===== básicos (los que ya recibías) =====
	        String rfcReceptor, String rfcEmisor, String razonSocialEmisor,
	        String existeBovedaDescarga, String tipoComprobante,
	        String fechaInicial, String fechaFinal,
	        String uuidDescarga, String estatusCFDI,
	        int startPaginado, int endPaginado, boolean isExcel,

	        // ===== valores EXTRA para filtros que antes no pasabas =====
	        String nombreReceptor,   // <-- NUEVO (viene del UI: “Razón social receptor”)
	        String rfcPac,           // <-- NUEVO (viene del UI: “PAC emisor” o “PAC receptor” según tu columna)

	        // ===== operadores/valores DX-like =====
	        // Texto: operador para cada campo (el valor viene de los básicos/extra)
	        String uuidOperator,     // sobre uuidDescarga
	        String rfcEmiOperator,   // sobre rfcEmisor
	        String nomEmiOperator,   // sobre razonSocialEmisor
	        String rfcRecOperator,   // sobre rfcReceptor
	        String nomRecOperator,   // sobre nombreReceptor
	        String pacOperator,      // sobre rfcPac
	        String efectoOperator,   // sobre tipoComprobante (si != ALL)
	        String estatusOperator,  // sobre estatusCFDI (si != ALL)
	        String bovedaOperator,   // sobre existeBovedaDescarga (si != ALL)

	        // Numérico (monto)
	        String montoOperator, String montoV1, String montoV2,

	        // Fechas
	        String emiDateOperator, String emiDateV1, String emiDateV2,
	        String cerDateOperator, String cerDateV1, String cerDateV2,
	        String canDateOperator, String canDateV1, String canDateV2
	) {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<DescargaSATForm> lista = new ArrayList<>();
	    java.text.DecimalFormat decimal = new java.text.DecimalFormat("###,###.##");

	    try {
	        StringBuilder sb = new StringBuilder();
	        sb.append(DescargaSATQuerys.getDetalle(esquema));

	        // Acumulador de parámetros para el PreparedStatement
	        java.util.List<Object> params = new java.util.ArrayList<>();

	        // SELECT base inicia con WHERE RECEPTOR_RFC = ?
	        params.add(Utils.noNulo(rfcReceptor));

	        // WHERE helper (mismo patrón que en Nómina)
	        FiltrosDescargaSAT.Where w = new FiltrosDescargaSAT.Where() {
	            @Override public void and(String frag, Object... vals) {
	                if (frag == null || frag.isEmpty()) return;
	                sb.append(" AND ").append(frag);
	                if (vals != null) for (Object v : vals) if (v != null) params.add(v);
	            }
	        };

	        // Normaliza dropdowns ALL -> null (para que el helper no agregue condición)
	        String efectoVal  = "ALL".equalsIgnoreCase(Utils.noNulo(tipoComprobante))     ? null : Utils.noNulo(tipoComprobante);
	        String estatusVal = "ALL".equalsIgnoreCase(Utils.noNulo(estatusCFDI))         ? null : Utils.noNulo(estatusCFDI);
	        String bovedaVal  = "ALL".equalsIgnoreCase(Utils.noNulo(existeBovedaDescarga))? null : Utils.noNulo(existeBovedaDescarga);
	       

	        // ====== Aplicar TODOS los filtros DX-like ======
	        // Usa aquí LOS NOMBRES DE COLUMNA REALES de tu SELECT base:
	        //   UUID, EMISOR_RFC, EMISOR_NOMBRE, RECEPTOR_RFC, RECEPTOR_NOMBRE,
	        //   RECEPTOR_PAC, EFECTO_COMPROBANTE, ESTATUS, EXISTE_BOVEDA,
	        //   MONTO, FECHA_EMISION, FECHA_CERTIFICACION, FECHA_CANCELACION
	     // RFC de sesión (el que amarra el WHERE base)
	        final String rfcDeSesion = Utils.noNulo(rfcReceptor);

	        // Deriva SI se debe aplicar un filtro adicional sobre RECEPTOR_RFC
	        String rfcReceptorFiltro = null;   // <- SOLO se llena si el usuario pidió algo distinto
	        String rfcRecOperatorEff = null;

	        // Regla: si el “filtro” del receptor es IGUAL al de sesión, NO agregues nada.
//	               si está vacío, tampoco.
//	               si es diferente (o un fragmento), sí pásalo al helper.
	        if (!Utils.noNulo(rfcReceptor).isEmpty() && !rfcDeSesion.isEmpty()) {
	            if (!rfcDeSesion.equals(rfcReceptor)) {
	                rfcReceptorFiltro = rfcReceptor;
	                rfcRecOperatorEff = Utils.noNulo(rfcRecOperator).isEmpty() ? "contains" : rfcRecOperator;
	            }
	        } else if (!Utils.noNulo(rfcReceptor).isEmpty()) {
	            // No hay sesión (?) pero hay valor: aplica filtro como venía
	            rfcReceptorFiltro = rfcReceptor;
	            rfcRecOperatorEff = Utils.noNulo(rfcRecOperator).isEmpty() ? "contains" : rfcRecOperator;
	        }

	        // A partir de aquí, usa rfcReceptorFiltro / rfcRecOperatorEff en lugar del par original

	       // String rfcReceptorFiltro = null; // <- clave
	        FiltrosDescargaSAT.aplicarFiltrosRecibidos(
	            w,
	            /* columnas */ 
	            "UUID",
	            "EMISOR_RFC",
	            "EMISOR_NOMBRE",
	            "RECEPTOR_RFC",
	            "RECEPTOR_NOMBRE",
	            "RECEPTOR_PAC",
	            "EFECTO_COMPROBANTE",
	            "ESTATUS",
	            "EXISTE_BOVEDA",
	            "MONTO",
	            "FECHA_EMISION",
	            "FECHA_CERTIFICACION",
	            "FECHA_CANCELACION",

	            /* valores + operadores */
	            // TEXTO
	            Utils.noNulo(uuidDescarga),   Utils.noNulo(uuidOperator),
	            Utils.noNulo(rfcEmisor),      Utils.noNulo(rfcEmiOperator),
	            Utils.noNulo(razonSocialEmisor), Utils.noNulo(nomEmiOperator),
	           // Utils.noNulo(rfcReceptor),    Utils.noNulo(rfcRecOperator),
	            Utils.noNulo(rfcReceptorFiltro), Utils.noNulo(rfcRecOperatorEff),
	            Utils.noNulo(nombreReceptor), Utils.noNulo(nomRecOperator),
	            Utils.noNulo(rfcPac),         Utils.noNulo(pacOperator),
	            efectoVal,                    Utils.noNulo(efectoOperator),
	            estatusVal,                   Utils.noNulo(estatusOperator),
	            bovedaVal,                    Utils.noNulo(bovedaOperator),

	            // FECHAS (emisión con fallback a los rangos globales cabecera)
	            Utils.noNulo(emiDateOperator), Utils.noNulo(emiDateV1), Utils.noNulo(emiDateV2),
	            Utils.noNulo(fechaInicial),    Utils.noNulo(fechaFinal),

	            // certificación / cancelación
	            Utils.noNulo(cerDateOperator), Utils.noNulo(cerDateV1), Utils.noNulo(cerDateV2),
	            Utils.noNulo(canDateOperator), Utils.noNulo(canDateV1), Utils.noNulo(canDateV2),

	            // Numérico (monto)
	            Utils.noNulo(montoOperator),   Utils.noNulo(montoV1),   Utils.noNulo(montoV2)
	        );

	        // Orden y paginación
	        sb.append(" order by FECHA_EMISION DESC ");
	        if (!isExcel) {
	            sb.append(" LIMIT ").append(startPaginado).append(", ").append(endPaginado);
	        }

	        stmt = con.prepareStatement(sb.toString());

	        // Bind de parámetros en orden
	        int idx = 1;
	        for (Object p : params) {
	            stmt.setString(idx++, String.valueOf(p));
	           // logger.info("[SAT/SQL] base+filtros → " + sb);
	            for (int i = 0; i < params.size(); i++) {
	              //logger.info(String.format("[SAT/SQL] param(%02d) = %s", (i+1), String.valueOf(params.get(i))));
	            }

	        }

	        logger.info("DESCARGA SAT () => " + stmt);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            DescargaSATForm f = new DescargaSATForm();
	            f.setIdRegistro(rs.getInt(1));
	            f.setUuid(Utils.noNuloNormal(rs.getString(2)));
	            f.setRfcEmisor(Utils.noNuloNormal(rs.getString(3)));
	            f.setNombreEmisor(Utils.noNuloNormal(rs.getString(4)));
	            f.setRfcReceptor(Utils.noNuloNormal(rs.getString(5)));
	            f.setNombreReceptor(Utils.noNuloNormal(rs.getString(6)));
	            // Si quisieras mostrar el PAC:
	            // f.setRfcPac(Utils.noNuloNormal(rs.getString(7)));
	            f.setRfcPac("");

	            f.setFechaEmision(Utils.noNuloNormal(rs.getString(8)));
	            f.setFechaCertificacion(Utils.noNuloNormal(rs.getString(9)));

	            f.setMonto(rs.getDouble(10));
	            f.setMontoDes(decimal.format(rs.getDouble(10)));

	            f.setEfectoComprobante(Utils.noNuloNormal(rs.getString(11)));
	            f.setEstatus(Utils.noNuloNormal(rs.getString(12)));
	            f.setFechaCancelacion(Utils.noNuloNormal(rs.getString(13)));
	            f.setExisteBoveda(Utils.noNuloNormal(rs.getString(14)));

	            lista.add(f);
	        }

	    } catch (Exception e) {
	        Utils.imprimeLog("detalleDescarga(): ", e);
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
	    }
	    return lista;
	}


	

	
	public int getTotalRegistros(
	        Connection con, String esquema,
	        // ===== básicos =====
	        String rfcReceptor, String rfcEmisor, String razonSocialEmisor,
	        String existeBovedaDescarga, String tipoComprobante,
	        String fechaInicial, String fechaFinal,
	        String uuidDescarga, String estatusCFDI,
	        // ===== extras de texto que también quieres filtrar =====
	        String nombreReceptor, String rfcPac,
	        // ===== operadores/valores DX-like =====
	        // Texto (el valor viene de los básicos / extras; aquí van operadores)
	        String uuidOperator, String rfcEmiOperator, String nomEmiOperator,
	        String rfcRecOperator, String nomRecOperator, String pacOperator,
	        String efectoOperator, String estatusOperator, String bovedaOperator,
	        // Numérico
	        String montoOperator, String montoV1, String montoV2,
	        // Fechas
	        String emiDateOperator, String emiDateV1, String emiDateV2,
	        String cerDateOperator, String cerDateV1, String cerDateV2,
	        String canDateOperator, String canDateV1, String canDateV2
	){
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    int total = 0;

	    try {
	        // SELECT base (mismo que detalle)
	        StringBuilder inner = new StringBuilder();
	        inner.append(DescargaSATQuerys.getTotalRegistros(esquema));

	        // Primer parámetro del base: RECEPTOR_RFC
	        java.util.List<Object> params = new java.util.ArrayList<>();
	        params.add(Utils.noNulo(rfcReceptor));

	        // Helper WHERE
	        FiltrosDescargaSAT.Where w = new FiltrosDescargaSAT.Where() {
	            @Override public void and(String frag, Object... vals) {
	                if (frag == null || frag.isEmpty()) return;
	                inner.append(" AND ").append(frag);
	                if (vals != null) for (Object v : vals) if (v != null) params.add(v);
	            }
	        };

	        // Normaliza dropdowns ALL -> null (sin filtro)
	        String efectoVal  = "ALL".equalsIgnoreCase(Utils.noNulo(tipoComprobante))      ? null : Utils.noNulo(tipoComprobante);
	        String estatusVal = "ALL".equalsIgnoreCase(Utils.noNulo(estatusCFDI))          ? null : Utils.noNulo(estatusCFDI);
	        String bovedaVal  = "ALL".equalsIgnoreCase(Utils.noNulo(existeBovedaDescarga)) ? null : Utils.noNulo(existeBovedaDescarga);

	        // RFC de sesión (el que amarra el WHERE base)
	        final String rfcDeSesion = Utils.noNulo(rfcReceptor);

	        // Deriva SI se debe aplicar un filtro adicional sobre RECEPTOR_RFC
	        String rfcReceptorFiltro = null;   // <- SOLO se llena si el usuario pidió algo distinto
	        String rfcRecOperatorEff = null;

	        // Regla: si el “filtro” del receptor es IGUAL al de sesión, NO agregues nada.
//	               si está vacío, tampoco.
//	               si es diferente (o un fragmento), sí pásalo al helper.
	        if (!Utils.noNulo(rfcReceptor).isEmpty() && !rfcDeSesion.isEmpty()) {
	            if (!rfcDeSesion.equals(rfcReceptor)) {
	                rfcReceptorFiltro = rfcReceptor;
	                rfcRecOperatorEff = Utils.noNulo(rfcRecOperator).isEmpty() ? "contains" : rfcRecOperator;
	            }
	        } else if (!Utils.noNulo(rfcReceptor).isEmpty()) {
	            // No hay sesión (?) pero hay valor: aplica filtro como venía
	            rfcReceptorFiltro = rfcReceptor;
	            rfcRecOperatorEff = Utils.noNulo(rfcRecOperator).isEmpty() ? "contains" : rfcRecOperator;
	        }

	        // A partir de aquí, usa rfcReceptorFiltro / rfcRecOperatorEff en lugar del par original

	       // String rfcReceptorFiltro = null; // <- clave
	        FiltrosDescargaSAT.aplicarFiltrosRecibidos(
	            w,
	            // columnas reales del SELECT
	            "UUID",
	            "EMISOR_RFC",
	            "EMISOR_NOMBRE",
	            "RECEPTOR_RFC",
	            "RECEPTOR_NOMBRE",
	            "RECEPTOR_PAC",
	            "EFECTO_COMPROBANTE",
	            "ESTATUS",
	            "EXISTE_BOVEDA",
	            "MONTO",
	            "FECHA_EMISION",
	            "FECHA_CERTIFICACION",
	            "FECHA_CANCELACION",

	            // ===== valores + operadores =====
	            // TEXTO
	            Utils.noNulo(uuidDescarga),      Utils.noNulo(uuidOperator),
	            Utils.noNulo(rfcEmisor),         Utils.noNulo(rfcEmiOperator),
	            Utils.noNulo(razonSocialEmisor), Utils.noNulo(nomEmiOperator),
	           // Utils.noNulo(rfcReceptor),       Utils.noNulo(rfcRecOperator),
	            Utils.noNulo(rfcReceptorFiltro), Utils.noNulo(rfcRecOperatorEff),
	            Utils.noNulo(nombreReceptor),    Utils.noNulo(nomRecOperator),
	            Utils.noNulo(rfcPac),            Utils.noNulo(pacOperator),
	            efectoVal,                       Utils.noNulo(efectoOperator),
	            estatusVal,                      Utils.noNulo(estatusOperator),
	            bovedaVal,                       Utils.noNulo(bovedaOperator),

	            // FECHA EMISIÓN con fallback a cabecera
	            Utils.noNulo(emiDateOperator), Utils.noNulo(emiDateV1), Utils.noNulo(emiDateV2),
	            Utils.noNulo(fechaInicial),    Utils.noNulo(fechaFinal),

	            // FECHAS: certificación / cancelación
	            Utils.noNulo(cerDateOperator), Utils.noNulo(cerDateV1), Utils.noNulo(cerDateV2),
	            Utils.noNulo(canDateOperator), Utils.noNulo(canDateV1), Utils.noNulo(canDateV2),

	            // Numérico (monto)
	            Utils.noNulo(montoOperator), Utils.noNulo(montoV1), Utils.noNulo(montoV2)
	        );

	        // Envolver con COUNT
	       // StringBuilder sb = new StringBuilder();
	       // sb.append("SELECT COUNT(1) FROM (").append(inner).append(") T");

	        stmt = con.prepareStatement(inner.toString());

	        int idx = 1;
	        for (Object p : params) {
	            stmt.setString(idx++, String.valueOf(p));
	        }
	        
	        
	        logger.info("📈 totalRegistros(sat) → " + stmt);

	        rs = stmt.executeQuery();
	        if (rs.next()) total = rs.getInt(1);

	    } catch (Exception e) {
	        Utils.imprimeLog("getTotalRegistros(): ", e);
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
	    }
	    return total;
	}



	public String consultarFechaMinima(Connection con, String esquema, String rfcRecibido) {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    String fechaMinima = "";
	    try {
	    	stmt = con.prepareStatement(DescargaSATQuerys.getConsultarFechaMinima(esquema));
	    	stmt.setString(1, rfcRecibido);
	    	rs = stmt.executeQuery();
	        if (rs.next()) {
	        	fechaMinima = Utils.noNulo(rs.getString(1));
	        }

	    } catch (Exception e) {
	        Utils.imprimeLog("detalleDescarga(): ", e);
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
	    }
	    return fechaMinima;
	}

	
	
	
	public DescargaSATForm  consultarUltimaFecha (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DescargaSATForm descargaSATForm = new DescargaSATForm();
		try {
			
			stmt = con.prepareStatement(DescargaSATQuerys.getUltimaFecha(esquema));
			stmt.setString(1, "FIN");
			rs = stmt.executeQuery();

			if (rs.next()) {
				descargaSATForm.setFechaDescarga(Utils.noNulo(rs.getString(1)));
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
		return descargaSATForm;
	}
	
	

	public void generaReporte(SXSSFWorkbook objLibro, SXSSFSheet hoja1 , ArrayList<DescargaSATForm> listaReporte, String idLenguaje, 
			int regInicial, int regFinal) { 
		  try {
			  
			  final String[] nameColumns = {"UUID","RFC Emisor","Razon Social Emisor","RFC Receptor","Razon Social Receptor","Pac Emisor","Fecha Emision",
					  "Fecha Certificacion","Monto","Efecto de Comprobante","Estatus","Fecha Cancelacion","Existe Boveda"};
			  
			  logger.info("********* ENTRO A EXCEL RECXIBIDOS bean *************");
			   CellStyle styleTitulo = objLibro.createCellStyle();
			   Font headerFont = objLibro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true);
		       styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
		       styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleTitulo.setFont(headerFont);
		       styleTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       CellStyle styleSubTitulo = objLibro.createCellStyle();
			   Font fontSub = objLibro.createFont();
			   fontSub.setFontHeightInPoints((short)10);
			   fontSub.setFontName("Arial");
			   fontSub.setBold(true);
		       styleSubTitulo.setFont(fontSub);
		       styleSubTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
			  hoja1.setColumnWidth(0, 11000);
			  hoja1.setColumnWidth(1, 6000);
			  hoja1.setColumnWidth(2, 12000);
			  hoja1.setColumnWidth(3, 6000);
			  hoja1.setColumnWidth(4, 11000);
			  hoja1.setColumnWidth(5, 6000);
			  hoja1.setColumnWidth(6, 7000);
			  hoja1.setColumnWidth(7, 7000);
			  hoja1.setColumnWidth(8, 4000);
			  hoja1.setColumnWidth(9, 7000);
			  hoja1.setColumnWidth(10, 4000);
			  hoja1.setColumnWidth(11, 7000);
			  hoja1.setColumnWidth(12, 5000);
			  
			  
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   
			    Cell monthCell = header.createCell(0);
			    monthCell.setCellValue("Sistema de Administración y Recepción de XML");
			    monthCell.setCellStyle(styleTitulo);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:M1"));
			    
			   
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			    Cell monthCell2 = header.createCell(0);
			    monthCell2.setCellValue("Detalle de XML Recibidos");
			    monthCell2.setCellStyle(styleSubTitulo);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:M2"));
			    
			    
			    
			   header = hoja1.createRow(2);
			   header.setHeightInPoints(18);
			   for (int i = 0; i < nameColumns.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(nameColumns[i]);
			   // monthCell.setCellStyle(encabezadoDetalle);
			   }
			   
			   DescargaSATForm descargaSATForm = null;
			   
			   int rowNum = 3;
			   Row myRow = null;
			   
			   for (int x = regInicial; x < regFinal; x++) {
				   descargaSATForm = listaReporte.get(x);
				  
				   if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hoja1).flushRows(100); // Retain the last 100 rows in memory
	               }
				   
				   myRow = hoja1.createRow(rowNum++);
				   myRow.createCell(0).setCellValue(descargaSATForm.getUuid());
				   myRow.createCell(1).setCellValue(descargaSATForm.getRfcEmisor());
				   myRow.createCell(2).setCellValue(descargaSATForm.getNombreEmisor());
				   myRow.createCell(3).setCellValue(descargaSATForm.getRfcReceptor());
				   myRow.createCell(4).setCellValue(descargaSATForm.getNombreReceptor());
				   myRow.createCell(5).setCellValue(descargaSATForm.getRfcPac());
				   myRow.createCell(6).setCellValue(descargaSATForm.getFechaEmision());
				   myRow.createCell(7).setCellValue(descargaSATForm.getFechaCertificacion());
				   myRow.createCell(8).setCellValue(descargaSATForm.getMonto());
				   myRow.createCell(9).setCellValue(descargaSATForm.getEfectoComprobante());
				   myRow.createCell(10).setCellValue(descargaSATForm.getEstatus());
				   myRow.createCell(11).setCellValue(descargaSATForm.getFechaCancelacion());
				   myRow.createCell(12).setCellValue(descargaSATForm.getExisteBoveda());
				  
			   }
		  } catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	 }	
	
	
	
	
	public ArrayList<String>  exportarCSV (Connection con, String esquema, String existeBovedaDescarga){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> listaTXT = new ArrayList<String>();

		StringBuffer sbLine1 = new StringBuffer("UUID|RFC EMISOR|RAZON SOCIAL EMISOR|RFC RECEPTOR|RAZON SOCIAL RECEPTOR|PAC EMISOR|FECHA EMISION|FECHA DE CERTIFICACION|MONTO|EFECTO COMPROBANTE|ESTATUS|FECHA DE CANCELACION|EXISTE EN BOVEDA"); 
		try {
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append(DescargaSATQuerys.getDetalle(esquema));
			
			if (!"".equalsIgnoreCase(existeBovedaDescarga) && !"ALL".equalsIgnoreCase(existeBovedaDescarga) && sbQuery.toString().indexOf(" where ") > -1) {
				sbQuery.append(" and EXISTE_BOVEDA = ? and ESTATUS = ?");
			}else if (!"".equalsIgnoreCase(existeBovedaDescarga)  && !"ALL".equalsIgnoreCase(existeBovedaDescarga)) {
				sbQuery.append(" where EXISTE_BOVEDA = ? and ESTATUS = ?");
			}
			stmt = con.prepareStatement(sbQuery.toString());
			
			int param=1;
			if (!"".equalsIgnoreCase(existeBovedaDescarga) && !"ALL".equalsIgnoreCase(existeBovedaDescarga)) {
				stmt.setString(param++, existeBovedaDescarga);
				stmt.setString(param++, "VIGENTE");
			}
			rs = stmt.executeQuery();
			listaTXT.add(sbLine1.toString());
        	sbLine1.setLength(0);
        	//sbLine1.append("|");
			
        	while (rs.next()) {
				sbLine1.append(Utils.noNuloNormal(rs.getString(2))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(3))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(4))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(5))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(6))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(7))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(8))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(9))).append("|");
				sbLine1.append(decimal.format(rs.getDouble(10))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(11))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(12))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(13))).append("|");
				sbLine1.append(Utils.noNuloNormal(rs.getString(14)));
				
				listaTXT.add(sbLine1.toString());
				sbLine1.setLength(0);
	        	//sbLine1.append("|");
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
		return listaTXT;
	}
	
	
	
	public ArrayList<DescargaSATForm>  obtenerNoExisteBoveda (Connection con, String esquema, String existeBovedaDescarga){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<DescargaSATForm> listaDescarga = new ArrayList<>();
		DescargaSATForm descargaSATForm = new DescargaSATForm();
		
		try {
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append(DescargaSATQuerys.getDetalle(esquema));
			
			if (!"".equalsIgnoreCase(existeBovedaDescarga) && !"ALL".equalsIgnoreCase(existeBovedaDescarga) && sbQuery.toString().indexOf(" where ") > -1) {
				sbQuery.append(" and EXISTE_BOVEDA = ? and ESTATUS = ?");
			}else if (!"".equalsIgnoreCase(existeBovedaDescarga)  && !"ALL".equalsIgnoreCase(existeBovedaDescarga)) {
				sbQuery.append(" where EXISTE_BOVEDA = ? and ESTATUS = ?");
			}
			stmt = con.prepareStatement(sbQuery.toString());
			
			int param=1;
			if (!"".equalsIgnoreCase(existeBovedaDescarga) && !"ALL".equalsIgnoreCase(existeBovedaDescarga)) {
				stmt.setString(param++, existeBovedaDescarga);
				stmt.setString(param++, "1");
			}
			rs = stmt.executeQuery();
        	while (rs.next()) {
        		descargaSATForm.setUuid(Utils.noNuloNormal(rs.getString(2)));
        		listaDescarga.add(descargaSATForm);
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
		return listaDescarga;
	}

	
	
	// =========================
//  EMITIDOS – detalle DX-like
// =========================
public ArrayList<DescargaSATForm> detalleDescargaEmitidos(
    Connection con, String esquema,
    // ===== básicos =====
    String rfcEmisor, String rfcReceptor, String razonSocialReceptor,
    String existeBovedaDescarga, String tipoComprobante,
    String fechaInicial, String fechaFinal,
    String uuidDescarga, String estatusCFDI,
    int startPaginado, int endPaginado, boolean isExcel,

    // ===== extras de texto =====
    String razonSocialEmisor, String rfcPac,

    // ===== operadores (texto) =====
    String uuidOperator, String rfcEmiOperator, String nomEmiOperator,
    String rfcRecOperator, String nomRecOperator, String pacOperator,
    String efectoOperator, String estatusOperator, String bovedaOperator,

    // ===== numérico =====
    String montoOperator, String montoV1, String montoV2,

    // ===== fechas =====
    String emiDateOperator, String emiDateV1, String emiDateV2,
    String cerDateOperator, String cerDateV1, String cerDateV2,
    String canDateOperator, String canDateV1, String canDateV2
){
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ArrayList<DescargaSATForm> lista = new ArrayList<>();
    java.text.DecimalFormat decimal = new java.text.DecimalFormat("###,###.##");

    try {
        StringBuilder sb = new StringBuilder();
        sb.append(DescargaSATQuerys.getDetalleEmitidos(esquema)); // ← base: WHERE EMISOR_RFC = ?
        java.util.List<Object> params = new java.util.ArrayList<>();

        // Primer parámetro del base
        params.add(Utils.noNulo(rfcEmisor));

        // Reglas del módulo: excluir Nómina en Emitidos
        sb.append(" AND EFECTO_COMPROBANTE NOT IN (?) ");
        params.add("N");

        // WHERE helper
        FiltrosDescargaSAT.Where w = new FiltrosDescargaSAT.Where() {
            @Override public void and(String frag, Object... vals) {
                if (frag == null || frag.isEmpty()) return;
                sb.append(" AND ").append(frag);
                if (vals != null) for (Object v : vals) if (v != null) params.add(v);
            }
        };

        // Normalizar dropdowns "ALL" → null
        String efectoVal  = "ALL".equalsIgnoreCase(Utils.noNulo(tipoComprobante))      ? null : Utils.noNulo(tipoComprobante);
        String estatusVal = "ALL".equalsIgnoreCase(Utils.noNulo(estatusCFDI))          ? null : Utils.noNulo(estatusCFDI);
        String bovedaVal  = "ALL".equalsIgnoreCase(Utils.noNulo(existeBovedaDescarga)) ? null : Utils.noNulo(existeBovedaDescarga);

        // ===== Aplicar filtros (columnas de EMITIDOS) =====
        // Nota: para EMITIDOS el PAC suele ser EMISOR_PAC
        String rfcReceptorFiltro = null; // <- clave
        FiltrosDescargaSAT.aplicarFiltrosRecibidos(
            w,
            /* columnas personalizadas */
            "UUID", "EMISOR_RFC", "EMISOR_NOMBRE", "RECEPTOR_RFC", "RECEPTOR_NOMBRE",
            "EMISOR_PAC", "EFECTO_COMPROBANTE", "ESTATUS", "EXISTE_BOVEDA",
            "MONTO", "FECHA_EMISION", "FECHA_CERTIFICACION", "FECHA_CANCELACION",

            /* valores + operadores */
            // TEXTO
            Utils.noNulo(uuidDescarga),      Utils.noNulo(uuidOperator),
            "",         "",
            Utils.noNulo(razonSocialEmisor), Utils.noNulo(nomEmiOperator),
            Utils.noNulo(rfcReceptor),       Utils.noNulo(rfcRecOperator),
            Utils.noNulo(razonSocialReceptor), Utils.noNulo(nomRecOperator),
            Utils.noNulo(rfcPac),            Utils.noNulo(pacOperator),
            efectoVal,                       Utils.noNulo(efectoOperator),
            estatusVal,                      Utils.noNulo(estatusOperator),
            bovedaVal,                       Utils.noNulo(bovedaOperator),

            // FECHAS (emisión con fallback a global cabecera)
            Utils.noNulo(emiDateOperator), Utils.noNulo(emiDateV1), Utils.noNulo(emiDateV2),
            Utils.noNulo(fechaInicial),    Utils.noNulo(fechaFinal),

            // Certificación / Cancelación
            Utils.noNulo(cerDateOperator), Utils.noNulo(cerDateV1), Utils.noNulo(cerDateV2),
            Utils.noNulo(canDateOperator), Utils.noNulo(canDateV1), Utils.noNulo(canDateV2),

            // NUMÉRICO
            Utils.noNulo(montoOperator), Utils.noNulo(montoV1), Utils.noNulo(montoV2)
        );

        sb.append(" ORDER BY FECHA_EMISION DESC ");
        if (!isExcel){
            sb.append(" LIMIT ").append(startPaginado).append(", ").append(endPaginado);
        }

        stmt = con.prepareStatement(sb.toString());
        int idx = 1;
        for (Object p : params) {
            stmt.setString(idx++, String.valueOf(p));
        }

        logger.info("[EMITIDOS/SQL] → " + stmt);
        rs = stmt.executeQuery();

        while (rs.next()){
            DescargaSATForm f = new DescargaSATForm();
            f.setIdRegistro(rs.getInt(1));
            f.setUuid(Utils.noNuloNormal(rs.getString(2)));
            f.setRfcEmisor(Utils.noNuloNormal(rs.getString(3)));
            f.setNombreEmisor(Utils.noNuloNormal(rs.getString(4)));
            f.setRfcReceptor(Utils.noNuloNormal(rs.getString(5)));
            f.setNombreReceptor(Utils.noNuloNormal(rs.getString(6)));
            f.setRfcPac(""); // si luego quieres mostrar EMISOR_PAC, toma rs.getString(7)
            f.setFechaEmision(Utils.noNuloNormal(rs.getString(8)));
            f.setFechaCertificacion(Utils.noNuloNormal(rs.getString(9)));
            f.setMonto(rs.getDouble(10));
            f.setMontoDes(decimal.format(rs.getDouble(10)));
            f.setEfectoComprobante(Utils.noNuloNormal(rs.getString(11)));
            f.setEstatus(Utils.noNuloNormal(rs.getString(12)));
            f.setFechaCancelacion(Utils.noNuloNormal(rs.getString(13)));
            f.setExisteBoveda(Utils.noNuloNormal(rs.getString(14)));
            lista.add(f);
        }
    }catch(Exception e){
        Utils.imprimeLog("detalleDescargaEmitidos(): ", e);
    }finally{
        try{ if(rs!=null) rs.close(); }catch(Exception ignore){}
        try{ if(stmt!=null) stmt.close(); }catch(Exception ignore){}
    }
    return lista;
}

// =========================
//  EMITIDOS – total DX-like
// =========================
public int getTotalRegistrosEmitidos(
    Connection con, String esquema,
    // ===== básicos =====
    String rfcEmisor, String rfcReceptor, String razonSocialReceptor,
    String existeBovedaDescarga, String tipoComprobante,
    String fechaInicial, String fechaFinal,
    String uuidDescarga, String estatusCFDI,
    // ===== extras =====
    String razonSocialEmisor, String rfcPac,
    // ===== operadores =====
    String uuidOperator, String rfcEmiOperator, String nomEmiOperator,
    String rfcRecOperator, String nomRecOperator, String pacOperator,
    String efectoOperator, String estatusOperator, String bovedaOperator,
    // ===== numérico =====
    String montoOperator, String montoV1, String montoV2,
    // ===== fechas =====
    String emiDateOperator, String emiDateV1, String emiDateV2,
    String cerDateOperator, String cerDateV1, String cerDateV2,
    String canDateOperator, String canDateV1, String canDateV2
){
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int total = 0;

    try{
        StringBuilder inner = new StringBuilder();
        inner.append(DescargaSATQuerys.getTotalRegistrosEmitidos(esquema)); // WHERE EMISOR_RFC = ?
        java.util.List<Object> params = new java.util.ArrayList<>();
        params.add(Utils.noNulo(rfcEmisor));

        inner.append(" AND EFECTO_COMPROBANTE NOT IN (?) ");
        params.add("N");

        FiltrosDescargaSAT.Where w = new FiltrosDescargaSAT.Where() {
            @Override public void and(String frag, Object... vals) {
                if (frag == null || frag.isEmpty()) return;
                inner.append(" AND ").append(frag);
                if (vals != null) for (Object v : vals) if (v != null) params.add(v);
            }
        };

        String efectoVal  = "ALL".equalsIgnoreCase(Utils.noNulo(tipoComprobante))      ? null : Utils.noNulo(tipoComprobante);
        String estatusVal = "ALL".equalsIgnoreCase(Utils.noNulo(estatusCFDI))          ? null : Utils.noNulo(estatusCFDI);
        String bovedaVal  = "ALL".equalsIgnoreCase(Utils.noNulo(existeBovedaDescarga)) ? null : Utils.noNulo(existeBovedaDescarga);
        String rfcReceptorFiltro = null; // <- clave

        FiltrosDescargaSAT.aplicarFiltrosRecibidos(
            w,
            "UUID", "EMISOR_RFC", "EMISOR_NOMBRE", "RECEPTOR_RFC", "RECEPTOR_NOMBRE",
            "EMISOR_PAC", "EFECTO_COMPROBANTE", "ESTATUS", "EXISTE_BOVEDA",
            "MONTO", "FECHA_EMISION", "FECHA_CERTIFICACION", "FECHA_CANCELACION",

            // TEXTO
            Utils.noNulo(uuidDescarga),      Utils.noNulo(uuidOperator),
            "",         "",
            Utils.noNulo(razonSocialEmisor), Utils.noNulo(nomEmiOperator),
            Utils.noNulo(rfcReceptor),       Utils.noNulo(rfcRecOperator),
            Utils.noNulo(razonSocialReceptor), Utils.noNulo(nomRecOperator),
            Utils.noNulo(rfcPac),            Utils.noNulo(pacOperator),
            efectoVal,                       Utils.noNulo(efectoOperator),
            estatusVal,                      Utils.noNulo(estatusOperator),
            bovedaVal,                       Utils.noNulo(bovedaOperator),

            // FECHAS (emisión con fallback)
            Utils.noNulo(emiDateOperator), Utils.noNulo(emiDateV1), Utils.noNulo(emiDateV2),
            Utils.noNulo(fechaInicial),    Utils.noNulo(fechaFinal),

            // Certificación / Cancelación
            Utils.noNulo(cerDateOperator), Utils.noNulo(cerDateV1), Utils.noNulo(cerDateV2),
            Utils.noNulo(canDateOperator), Utils.noNulo(canDateV1), Utils.noNulo(canDateV2),

            // Numérico
            Utils.noNulo(montoOperator), Utils.noNulo(montoV1), Utils.noNulo(montoV2)
        );

      // StringBuilder sb = new StringBuilder();
      //  sb.append("SELECT COUNT(1) FROM (").append(inner).append(") T");

        stmt = con.prepareStatement(inner.toString());
        int idx = 1;
        for (Object p : params) stmt.setString(idx++, String.valueOf(p));

        logger.info("[EMITIDOS/TOTAL] → " + stmt);
        rs = stmt.executeQuery();
        if (rs.next()) total = rs.getInt(1);

    }catch(Exception e){
        Utils.imprimeLog("getTotalRegistrosEmitidosDX(): ", e);
    }finally{
        try{ if(rs!=null) rs.close(); }catch(Exception ignore){}
        try{ if(stmt!=null) stmt.close(); }catch(Exception ignore){}
    }
    return total;
}



public String consultarFechaMinimaEmitidos(Connection con, String esquema, String rfcEmisor) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String fechaMinima = "";
    try {
    	stmt = con.prepareStatement(DescargaSATQuerys.getConsultarFechaMinimaEmitidos(esquema));
    	stmt.setString(1, rfcEmisor);
    	stmt.setString(2, "N");
    	rs = stmt.executeQuery();
        if (rs.next()) {
        	fechaMinima = Utils.noNulo(rs.getString(1));
        }
        
        if ("".equalsIgnoreCase(fechaMinima)) {
        	fechaMinima = UtilsFechas.getFechayyyyMMdd();
        }
        
    } catch (Exception e) {
        Utils.imprimeLog("", e);
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
    }
    return fechaMinima;
}

	

	public void generaReporteEmitidos(SXSSFWorkbook objLibro, SXSSFSheet hoja1 ,ArrayList<DescargaSATForm> listaReporte, 
			String idLenguaje, int regInicial, int regFinal) { 
		  try {
			  
			  final String[] nameColumns = {"UUID","RFC Emisor","Razon Social Emisor","RFC Receptor","Razon Social Receptor","Pac Emisor","Fecha Emision",
					  "Fecha Certificacion","Monto","Efecto de Comprobante","Estatus","Fecha Cancelacion","Existe Boveda"};
			  
			   CellStyle styleTitulo = objLibro.createCellStyle();
			   Font headerFont = objLibro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true);
		       styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
		       styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleTitulo.setFont(headerFont);
		       styleTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       CellStyle styleSubTitulo = objLibro.createCellStyle();
			   Font fontSub = objLibro.createFont();
			   fontSub.setFontHeightInPoints((short)10);
			   fontSub.setFontName("Arial");
			   fontSub.setBold(true);
		       styleSubTitulo.setFont(fontSub);
		       styleSubTitulo.setAlignment(HorizontalAlignment.CENTER);
			   
			  
			  hoja1.setColumnWidth(0, 11000);
			  hoja1.setColumnWidth(1, 6000);
			  hoja1.setColumnWidth(2, 12000);
			  hoja1.setColumnWidth(3, 6000);
			  hoja1.setColumnWidth(4, 11000);
			  hoja1.setColumnWidth(5, 6000);
			  hoja1.setColumnWidth(6, 7000);
			  hoja1.setColumnWidth(7, 7000);
			  hoja1.setColumnWidth(8, 4000);
			  hoja1.setColumnWidth(9, 7000);
			  hoja1.setColumnWidth(10, 4000);
			  hoja1.setColumnWidth(11, 7000);
			  hoja1.setColumnWidth(12, 5000);
			  
			  
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   
			    Cell monthCell = header.createCell(0);
			    monthCell.setCellValue("Sistema de Administración y Recepción de XML");
			    monthCell.setCellStyle(styleTitulo);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:M1"));
			    
			   
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			    Cell monthCell2 = header.createCell(0);
			    monthCell2.setCellValue("Detalle de XML Emitidos ");
			    monthCell2.setCellStyle(styleSubTitulo);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:M2"));
			    
			    
			    
			   header = hoja1.createRow(2);
			   header.setHeightInPoints(18);
			   for (int i = 0; i < nameColumns.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(nameColumns[i]);
			   // monthCell.setCellStyle(encabezadoDetalle);
			   }
			   
			   DescargaSATForm descargaSATForm = null;
			   
			   int rowNum = 3;
			   Row myRow = null;
			   
			   for (int x = regInicial; x < regFinal; x++) {
				   descargaSATForm = listaReporte.get(x);
				   if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hoja1).flushRows(100); // Retain the last 100 rows in memory
	                }

				   myRow = hoja1.createRow(rowNum++);
				   myRow.createCell(0).setCellValue(descargaSATForm.getUuid());
				   myRow.createCell(1).setCellValue(descargaSATForm.getRfcEmisor());
				   myRow.createCell(2).setCellValue(descargaSATForm.getNombreEmisor());
				   myRow.createCell(3).setCellValue(descargaSATForm.getRfcReceptor());
				   myRow.createCell(4).setCellValue(descargaSATForm.getNombreReceptor());
				   myRow.createCell(5).setCellValue(descargaSATForm.getRfcPac());
				   myRow.createCell(6).setCellValue(descargaSATForm.getFechaEmision());
				   myRow.createCell(7).setCellValue(descargaSATForm.getFechaCertificacion());
				   myRow.createCell(8).setCellValue(descargaSATForm.getMonto());
				   myRow.createCell(9).setCellValue(descargaSATForm.getEfectoComprobante());
				   myRow.createCell(10).setCellValue(descargaSATForm.getEstatus());
				   myRow.createCell(11).setCellValue(descargaSATForm.getFechaCancelacion());
				   myRow.createCell(12).setCellValue(descargaSATForm.getExisteBoveda());
				  
			   }
		  } catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	 }	
	
	
	
	public ArrayList<DescargaSATForm> detalleDescargaNomina(
		    Connection con, String esquema,
		    // básicos (Emitidos)
		    String rfcEmisor, String rfcReceptor, String razonSocialReceptor,
		    String existeBovedaDescarga, String tipoComprobante,
		    String fechaInicial, String fechaFinal,
		    String uuidDescarga, String estatusCFDI,
		    int startPaginado, int endPaginado, boolean isExcel,

		    // extras (texto)
		    String razonSocialEmisor, String rfcPac,

		    // operadores texto
		    String uuidOperator, String rfcEmiOperator, String nomEmiOperator,
		    String rfcRecOperator, String nomRecOperator, String pacOperator,
		    String efectoOperator, String estatusOperator, String bovedaOperator,

		    // num
		    String montoOperator, String montoV1, String montoV2,

		    // fechas
		    String emiDateOperator, String emiDateV1, String emiDateV2,
		    String cerDateOperator, String cerDateV1, String cerDateV2,
		    String canDateOperator, String canDateV1, String canDateV2
		){
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  ArrayList<DescargaSATForm> lista = new ArrayList<>();
		  java.text.DecimalFormat decimal = new java.text.DecimalFormat("###,###.##");

		  try{
		    StringBuilder sb = new StringBuilder();
		    sb.append(DescargaSATQuerys.getDetalleEmitidos(esquema)); // <-- NO agregues WHERE extra aquí

		    // Acumulador de params (DEBES cargar primero los del SELECT base)
		    java.util.List<Object> params = new java.util.ArrayList<>();

		    // ⚠️ Muy importante: empuja los parámetros que espera el SELECT base
		    // Si tu SELECT base es "… WHERE EMISOR_RFC = ? AND CANCELADO = ? …", haz:
		 // ✅ Después:
		    params.add(Utils.noNulo(rfcEmisor)); // EMISOR_RFC
		    // NO agregues nada más aquí a menos que tu SQL base realmente tenga otro “?”


		    // Helper
		    FiltrosDescargaSAT.Where w = new FiltrosDescargaSAT.Where() {
		      @Override public void and(String frag, Object... vals) {
		        if (frag == null || frag.isEmpty()) return;
		        sb.append(" AND ").append(frag);
		        if (vals != null) for (Object v : vals) if (v != null) params.add(v);
		      }
		    };

		    // Normalizar dropdowns
		    String efectoVal  = "ALL".equalsIgnoreCase(Utils.noNulo(tipoComprobante))     ? null : Utils.noNulo(tipoComprobante);
		    String estatusVal = "ALL".equalsIgnoreCase(Utils.noNulo(estatusCFDI))         ? null : Utils.noNulo(estatusCFDI);
		    String bovedaVal  = "ALL".equalsIgnoreCase(Utils.noNulo(existeBovedaDescarga))? null : Utils.noNulo(existeBovedaDescarga);

		    // Para evitar duplicar condición sobre EMISOR_RFC, NO lo pasamos a los filtros DX:
		    String rfcEmisorParaDX = ""; // ← vacío => no genera AND adicional
		    String rfcReceptorFiltro = null; // <- clave

		    FiltrosDescargaSAT.aplicarFiltrosRecibidos(
		        w,
		        // columnas:
		        "UUID","EMISOR_RFC","EMISOR_NOMBRE","RECEPTOR_RFC","RECEPTOR_NOMBRE",
		        "EMISOR_PAC","EFECTO_COMPROBANTE","ESTATUS","EXISTE_BOVEDA",
		        "MONTO","FECHA_EMISION","FECHA_CERTIFICACION","FECHA_CANCELACION",

		        // TEXTO (valor + op):
		        Utils.noNulo(uuidDescarga),      Utils.noNulo(uuidOperator),
		        rfcEmisorParaDX,                 Utils.noNulo(rfcEmiOperator), // ← vacío, evita duplicado
		        Utils.noNulo(razonSocialEmisor), Utils.noNulo(nomEmiOperator),
		        Utils.noNulo(rfcReceptor),       Utils.noNulo(rfcRecOperator),
		        Utils.noNulo(razonSocialReceptor), Utils.noNulo(nomRecOperator),
		        Utils.noNulo(rfcPac),            Utils.noNulo(pacOperator),
		        efectoVal,                       Utils.noNulo(efectoOperator),
		        estatusVal,                      Utils.noNulo(estatusOperator),
		        bovedaVal,                       Utils.noNulo(bovedaOperator),

		        // FECHAS (emisión con fallback a cabecera)
		        Utils.noNulo(emiDateOperator), Utils.noNulo(emiDateV1), Utils.noNulo(emiDateV2),
		        Utils.noNulo(fechaInicial),    Utils.noNulo(fechaFinal),

		        // certificación / cancelación
		        Utils.noNulo(cerDateOperator), Utils.noNulo(cerDateV1), Utils.noNulo(cerDateV2),
		        Utils.noNulo(canDateOperator), Utils.noNulo(canDateV1), Utils.noNulo(canDateV2),

		        // NUMÉRICO
		        Utils.noNulo(montoOperator),   Utils.noNulo(montoV1),   Utils.noNulo(montoV2)
		    );

		    // Orden / paginación
		    sb.append(" ORDER BY FECHA_EMISION DESC ");
		    if (!isExcel){
		      sb.append(" LIMIT ").append(startPaginado).append(", ").append(endPaginado);
		    }

		    stmt = con.prepareStatement(sb.toString());

		    // Bind en orden exacto
		    int idx = 1;
		    for (Object p : params) {
		      stmt.setString(idx++, String.valueOf(p));
		    }
		   // logger.info("[NOMINA/SQL] → " + efectoVal);
		    logger.info("[NOMINA/SQL] → " + stmt);
		    rs = stmt.executeQuery();
		    while (rs.next()){
		      DescargaSATForm f = new DescargaSATForm();
		      f.setIdRegistro(rs.getInt(1));
		      f.setUuid(Utils.noNuloNormal(rs.getString(2)));
		      f.setRfcEmisor(Utils.noNuloNormal(rs.getString(3)));
		      f.setNombreEmisor(Utils.noNuloNormal(rs.getString(4)));
		      f.setRfcReceptor(Utils.noNuloNormal(rs.getString(5)));
		      f.setNombreReceptor(Utils.noNuloNormal(rs.getString(6)));
		      f.setRfcPac("");
		      f.setFechaEmision(Utils.noNuloNormal(rs.getString(8)));
		      f.setFechaCertificacion(Utils.noNuloNormal(rs.getString(9)));
		      f.setMonto(rs.getDouble(10));
		      f.setMontoDes(decimal.format(rs.getDouble(10)));
		      f.setEfectoComprobante(Utils.noNuloNormal(rs.getString(11)));
		      f.setEstatus(Utils.noNuloNormal(rs.getString(12)));
		      f.setFechaCancelacion(Utils.noNuloNormal(rs.getString(13)));
		      f.setExisteBoveda(Utils.noNuloNormal(rs.getString(14)));
		      lista.add(f);
		    }
		  } catch(Exception e){
		    Utils.imprimeLog("detalleDescargaNominaDX(): ", e);
		  } finally {
		    try{ if (rs!=null) rs.close(); } catch(Exception ignore){}
		    try{ if (stmt!=null) stmt.close(); } catch(Exception ignore){}
		  }
		  return lista;
		}


	
	public int getTotalRegistrosNomina(
		    Connection con, String esquema,
		    // básicos
		    String rfcEmisor, String rfcReceptor, String razonSocialReceptor,
		    String existeBovedaDescarga, String tipoComprobante,
		    String fechaInicial, String fechaFinal,
		    String uuidDescarga, String estatusCFDI,

		    // extras
		    String razonSocialEmisor, String rfcPac,

		    // operadores
		    String uuidOperator, String rfcEmiOperator, String nomEmiOperator,
		    String rfcRecOperator, String nomRecOperator, String pacOperator,
		    String efectoOperator, String estatusOperator, String bovedaOperator,

		    // num
		    String montoOperator, String montoV1, String montoV2,

		    // fechas
		    String emiDateOperator, String emiDateV1, String emiDateV2,
		    String cerDateOperator, String cerDateV1, String cerDateV2,
		    String canDateOperator, String canDateV1, String canDateV2
		){
		  PreparedStatement stmt = null; ResultSet rs = null; int total = 0;
		  try{
		    StringBuilder sb = new StringBuilder();
		    sb.append(DescargaSATQuerys.getTotalRegistrosEmitidos(esquema)); // COUNT base

		    java.util.List<Object> params = new java.util.ArrayList<>();

		    // ⚠️ Igual que en detalle: empuja primero los parámetros que pide el COUNT base
		 // ✅ Después:
		    params.add(Utils.noNulo(rfcEmisor));


		    FiltrosDescargaSAT.Where w = new FiltrosDescargaSAT.Where() {
		      @Override public void and(String frag, Object... vals) {
		        if (frag == null || frag.isEmpty()) return;
		        sb.append(" AND ").append(frag);
		        if (vals != null) for (Object v : vals) if (v != null) params.add(v);
		      }
		    };

		    String efectoVal  = "ALL".equalsIgnoreCase(Utils.noNulo(tipoComprobante))     ? null : Utils.noNulo(tipoComprobante);
		    String estatusVal = "ALL".equalsIgnoreCase(Utils.noNulo(estatusCFDI))         ? null : Utils.noNulo(estatusCFDI);
		    String bovedaVal  = "ALL".equalsIgnoreCase(Utils.noNulo(existeBovedaDescarga))? null : Utils.noNulo(existeBovedaDescarga);

		    String rfcEmisorParaDX = ""; // evita duplicar condición sobre EMISOR_RFC
		    String rfcReceptorFiltro = null; // <- clave

		    FiltrosDescargaSAT.aplicarFiltrosRecibidos(
		        w,
		        "UUID","EMISOR_RFC","EMISOR_NOMBRE","RECEPTOR_RFC","RECEPTOR_NOMBRE",
		        "EMISOR_PAC","EFECTO_COMPROBANTE","ESTATUS","EXISTE_BOVEDA",
		        "MONTO","FECHA_EMISION","FECHA_CERTIFICACION","FECHA_CANCELACION",

		        Utils.noNulo(uuidDescarga),      Utils.noNulo(uuidOperator),
		        rfcEmisorParaDX,                 Utils.noNulo(rfcEmiOperator),
		        Utils.noNulo(razonSocialEmisor), Utils.noNulo(nomEmiOperator),
		        Utils.noNulo(rfcReceptor),       Utils.noNulo(rfcRecOperator),
		        Utils.noNulo(razonSocialReceptor), Utils.noNulo(nomRecOperator),
		        Utils.noNulo(rfcPac),            Utils.noNulo(pacOperator),
		        efectoVal,                       Utils.noNulo(efectoOperator),
		        estatusVal,                      Utils.noNulo(estatusOperator),
		        bovedaVal,                       Utils.noNulo(bovedaOperator),

		        Utils.noNulo(emiDateOperator), Utils.noNulo(emiDateV1), Utils.noNulo(emiDateV2),
		        Utils.noNulo(fechaInicial),    Utils.noNulo(fechaFinal),

		        Utils.noNulo(cerDateOperator), Utils.noNulo(cerDateV1), Utils.noNulo(cerDateV2),
		        Utils.noNulo(canDateOperator), Utils.noNulo(canDateV1), Utils.noNulo(canDateV2),

		        Utils.noNulo(montoOperator),   Utils.noNulo(montoV1),   Utils.noNulo(montoV2)
		    );

		    stmt = con.prepareStatement(sb.toString());
		    int idx=1; for (Object p: params) stmt.setString(idx++, String.valueOf(p));

		    logger.info("NOMINA total => " + stmt);
		    rs = stmt.executeQuery();
		    if (rs.next()) total = rs.getInt(1);
		  } catch(Exception e){
		    Utils.imprimeLog("getTotalRegistrosNominaDX(): ", e);
		  } finally {
		    try{ if (rs!=null) rs.close(); } catch(Exception ignore){}
		    try{ if (stmt!=null) stmt.close(); } catch(Exception ignore){}
		  }
		  return total;
		}

	
	

public String consultarFechaMinimaNomina(Connection con, String esquema) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String fechaMinima = "";
    try {
    	stmt = con.prepareStatement(DescargaSATQuerys.getConsultarFechaMinimaNomina(esquema));
    	stmt.setString(1, "N");
    	rs = stmt.executeQuery();
        if (rs.next()) {
        	fechaMinima = Utils.noNulo(rs.getString(1));
        }
        
        if ("".equalsIgnoreCase(fechaMinima)) {
        	fechaMinima = UtilsFechas.getFechayyyyMMdd();
        }
        
    } catch (Exception e) {
        Utils.imprimeLog("", e);
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
    }
    return fechaMinima;
}


	
	public void generaReporteNomina(SXSSFWorkbook objLibro, SXSSFSheet hoja1 ,ArrayList<DescargaSATForm> listaReporte, 
			String idLenguaje, int regInicial, int regFinal) { 
		  try {
			  
			  final String[] nameColumns = {"UUID","RFC Emisor","Razon Social Emisor","RFC Receptor","Razon Social Receptor","Pac Emisor","Fecha Emision",
					  "Fecha Certificacion","Monto","Efecto de Comprobante","Estatus","Fecha Cancelacion","Existe Boveda"};
			  
			  
			  
			  
			   CellStyle styleTitulo = objLibro.createCellStyle();
			   Font headerFont = objLibro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true);
		       styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
		       styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleTitulo.setFont(headerFont);
		       styleTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       CellStyle styleSubTitulo = objLibro.createCellStyle();
			   Font fontSub = objLibro.createFont();
			   fontSub.setFontHeightInPoints((short)10);
			   fontSub.setFontName("Arial");
			   fontSub.setBold(true);
		       styleSubTitulo.setFont(fontSub);
		       styleSubTitulo.setAlignment(HorizontalAlignment.CENTER);
			   
		       
			  hoja1.setColumnWidth(0, 11000);
			  hoja1.setColumnWidth(1, 6000);
			  hoja1.setColumnWidth(2, 12000);
			  hoja1.setColumnWidth(3, 6000);
			  hoja1.setColumnWidth(4, 11000);
			  hoja1.setColumnWidth(5, 6000);
			  hoja1.setColumnWidth(6, 7000);
			  hoja1.setColumnWidth(7, 7000);
			  hoja1.setColumnWidth(8, 4000);
			  hoja1.setColumnWidth(9, 7000);
			  hoja1.setColumnWidth(10, 4000);
			  hoja1.setColumnWidth(11, 7000);
			  hoja1.setColumnWidth(12, 5000);
			  
			  
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   
			    Cell monthCell = header.createCell(0);
			    monthCell.setCellValue("Sistema de Administración y Recepción de XML");
			    monthCell.setCellStyle(styleTitulo);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:M1"));
			    
			   
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			    Cell monthCell2 = header.createCell(0);
			    monthCell2.setCellValue("Detalle de XML Nomina ");
			    monthCell2.setCellStyle(styleSubTitulo);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:M2"));
			    
			    
			    
			   header = hoja1.createRow(2);
			   header.setHeightInPoints(18);
			   for (int i = 0; i < nameColumns.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(nameColumns[i]);
			   // monthCell.setCellStyle(encabezadoDetalle);
			   }
			   DescargaSATForm descargaSATForm = null;
			   int rowNum = 3;
			   Row myRow = null;
			   for (int x = regInicial; x < regFinal; x++) {
				   descargaSATForm = listaReporte.get(x);
				   if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hoja1).flushRows(100); // Retain the last 100 rows in memory
	                }

				   myRow = hoja1.createRow(rowNum++);
				   myRow.createCell(0).setCellValue(descargaSATForm.getUuid());
				   myRow.createCell(1).setCellValue(descargaSATForm.getRfcEmisor());
				   myRow.createCell(2).setCellValue(descargaSATForm.getNombreEmisor());
				   myRow.createCell(3).setCellValue(descargaSATForm.getRfcReceptor());
				   myRow.createCell(4).setCellValue(descargaSATForm.getNombreReceptor());
				   myRow.createCell(5).setCellValue(descargaSATForm.getRfcPac());
				   myRow.createCell(6).setCellValue(descargaSATForm.getFechaEmision());
				   myRow.createCell(7).setCellValue(descargaSATForm.getFechaCertificacion());
				   myRow.createCell(8).setCellValue(descargaSATForm.getMonto());
				   myRow.createCell(9).setCellValue(descargaSATForm.getEfectoComprobante());
				   myRow.createCell(10).setCellValue(descargaSATForm.getEstatus());
				   myRow.createCell(11).setCellValue(descargaSATForm.getFechaCancelacion());
				   myRow.createCell(12).setCellValue(descargaSATForm.getExisteBoveda());
				   
				   
				  
			   }
		  } catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	 }	
}
