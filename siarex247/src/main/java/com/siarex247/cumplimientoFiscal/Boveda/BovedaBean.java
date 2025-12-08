package com.siarex247.cumplimientoFiscal.Boveda;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.itextpdf.xmltopdf.Retencion;
import com.itextpdf.xmltopdf.Traslado;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaMasivaLocal;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaSATBean;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.validaciones.UtilsValidaciones;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.math.BigDecimal;

public class BovedaBean extends FiltrosBoveda{

	public static final Logger logger = Logger.getLogger("siarex247");
	// En tu clase BovedaBean (arriba, como campo estático):
	private static final java.util.concurrent.atomic.AtomicLong HITS_DETALLE = new java.util.concurrent.atomic.AtomicLong(0);
	private static final java.util.concurrent.atomic.AtomicLong HITS_TOTAL   = new java.util.concurrent.atomic.AtomicLong(0);

	// (opcional) si quieres reiniciar contadores desde algún admin:
	public static void resetLogCounters() {
	    HITS_DETALLE.set(0);
	    HITS_TOTAL.set(0);
	}
	
	 /* =========================
     * DETALLE (paginado / pantalla)
     * ========================= */
	public ArrayList<BovedaForm> detalleBoveda(
		    Connection con,
		    String esquema,
		    String rfc, String razonSocial, String folio, String serie,
		    String fechaInicial, String tipoComprobante, String uuidBoveda, String fechaFinal,
		    int starPaginado, int endPaginado, boolean isExcel,
		    // operadores texto
		    String rfcOperator, String razonOperator, String serieOperator, String tipoOperator, String uuidOperator,
		    // fecha con operadores
		    String dateOperator, String dateV1, String dateV2,
		    // numéricos
		    String folioOperator,  String folioV1,  String folioV2,
		    String totalOperator,  String totalV1,  String totalV2,
		    String subOperator,    String subV1,    String subV2,
		    String ivaOperator,    String ivaV1,    String ivaV2,
		    String ivaRetOperator, String ivaRetV1, String ivaRetV2,
		    String isrOperator,    String isrV1,    String isrV2,
		    String impLocOperator, String impLocV1, String impLocV2
		) {
		    PreparedStatement stmt = null;
		    ResultSet rs = null;
		    ArrayList<BovedaForm> lista = new ArrayList<>();

		    try {
		        // 1) SQL base
		        final String baseSql = BovedaQuerys.getConsultaBoveda(esquema); // típicamente "... FROM BOVEDA ..."
		        StringBuilder sb = new StringBuilder(baseSql);
		        List<Object> params = new ArrayList<>();

		        // 2) Where helper y “siembra” correcta (detecta si el base ya trae WHERE)
		        FiltrosBoveda.Where w = new FiltrosBoveda.Where(sb, params);
		        boolean baseHasWhere = baseSql.toLowerCase().contains(" where ");
		        w.seedHasWhere(baseHasWhere); // <<< CLAVE: si NO tiene WHERE, el primer filtro será "WHERE ..."

		        // 3) Aplica todos los filtros
		        aplicarFiltrosBoveda(
		            w,
		            rfc, rfcOperator,
		            razonSocial, razonOperator,
		            serie, serieOperator,
		            tipoComprobante, tipoOperator,
		            uuidBoveda, uuidOperator,
		            // fecha + cabecera
		            dateOperator, dateV1, dateV2, fechaInicial, fechaFinal,
		            // numéricos
		            folio, folioOperator, folioV1, folioV2,
		            totalOperator, totalV1, totalV2,
		            subOperator, subV1, subV2,
		            ivaOperator, ivaV1, ivaV2,
		            ivaRetOperator, ivaRetV1, ivaRetV2,
		            isrOperator, isrV1, isrV2,
		            impLocOperator, impLocV1, impLocV2
		        );

		        // 4) Orden y paginado (solo en pantalla)
		        sb.append(" ORDER BY FECHA_FACTURA DESC ");
		        if (!isExcel) {
		            sb.append(" LIMIT ").append(starPaginado).append(", ").append(endPaginado);
		        }

		        // 5) Preparar y bindear
		        stmt = con.prepareStatement(sb.toString());
		        int idx = 1;
		        for (Object p : params) {
		            if (p instanceof BigDecimal) stmt.setBigDecimal(idx++, (BigDecimal)p);
		            else if (p instanceof Integer) stmt.setInt(idx++, (Integer)p);
		            else stmt.setString(idx++, String.valueOf(p));
		        }

		        logger.info("🔁 detalleBoveda N → " + stmt);


            rs = stmt.executeQuery();
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            while (rs.next()) {
                BovedaForm b = new BovedaForm();
                b.setIdRegistro(rs.getInt(1));
                b.setUuid(Utils.noNuloNormal(rs.getString(2)));
                b.setRfc(Utils.noNulo(rs.getString(15)));
                b.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(16))));
                b.setSerie(Utils.noNulo(rs.getString(3)));
                b.setFolio(Utils.noNulo(rs.getString(4)));
                b.setTotal(decimal.format(rs.getDouble(14)));
                b.setSubTotal(decimal.format(rs.getDouble(10)));
                b.setIva(decimal.format(rs.getDouble(13)));
                b.setRetIVA(decimal.format(rs.getDouble(19)));
                b.setRetISR(decimal.format(rs.getDouble(21)));
                b.setImpLocales("0"); // ajusta si tienes columna
                b.setXml(Utils.noNuloNormal(rs.getString(23)));
                b.setFechaFactura(Utils.noNulo(rs.getString(5)));
                b.setTipoComprobante(Utils.noNulo(rs.getString(24)));
                lista.add(b);
            }
        } catch (Exception e) {
            Utils.imprimeLog("detalleBoveda(): ", e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignore) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
        }
        return lista;
    }
	
	
	public int totalRegistros(
		    Connection con, String esquema,
		    String rfc, String razonSocial, String folio, String serie,
		    String fechaInicial, String tipoComprobante, String uuidBoveda, String fechaFinal,
		    String rfcOperator, String razonOperator, String serieOperator, String tipoOperator, String uuidOperator,
		    String dateOperator, String dateV1, String dateV2,
		    String folioOperator, String folioV1, String folioV2,
		    String totalOperator, String totalV1, String totalV2,
		    String subOperator, String subV1, String subV2,
		    String ivaOperator, String ivaV1, String ivaV2,
		    String ivaRetOperator, String ivaRetV1, String ivaRetV2,
		    String isrOperator, String isrV1, String isrV2,
		    String impLocOperator, String impLocV1, String impLocV2
		){
		    PreparedStatement stmt = null; ResultSet rs = null; int total=0;
		    try {
		        // 1) SQL base del conteo (sin WHERE)
		        final String baseSql = "SELECT COUNT(*) FROM BOVEDA";
		        StringBuilder sb = new StringBuilder(baseSql);
		        List<Object> params = new ArrayList<>();

		        // 2) Where helper
		        FiltrosBoveda.Where w = new FiltrosBoveda.Where(sb, params);
		        boolean baseHasWhere = baseSql.toLowerCase().contains(" where ");
		        w.seedHasWhere(baseHasWhere); // aquí será false → primer filtro usa WHERE

		        // 3) Filtros (mismo orden que detalle)
		        aplicarFiltrosBoveda(
		            w,
		            rfc, rfcOperator,
		            razonSocial, razonOperator,
		            serie, serieOperator,
		            tipoComprobante, tipoOperator,
		            uuidBoveda, uuidOperator,
		            dateOperator, dateV1, dateV2, fechaInicial, fechaFinal,
		            folio, folioOperator, folioV1, folioV2,
		            totalOperator, totalV1, totalV2,
		            subOperator, subV1, subV2,
		            ivaOperator, ivaV1, ivaV2,
		            ivaRetOperator, ivaRetV1, ivaRetV2,
		            isrOperator, isrV1, isrV2,
		            impLocOperator, impLocV1, impLocV2
		        );

		        stmt = con.prepareStatement(sb.toString());
		        int idx=1; for (Object p : params){
		            if (p instanceof BigDecimal) stmt.setBigDecimal(idx++, (BigDecimal)p);
		            else if (p instanceof Integer) stmt.setInt(idx++, (Integer)p);
		            else stmt.setString(idx++, String.valueOf(p));
		        }
		        logger.info("🔁 totalRegistros → " + stmt);
		        rs = stmt.executeQuery();
		        if (rs.next()) total = rs.getInt(1);
		    } catch (Exception e) {
		        Utils.imprimeLog("totalRegistros(): ", e);
		    } finally {
		        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
		        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
		    }
		    return total;
		}
    


	
	

	

	public int altaBoveda(Connection con, String esquema, XMLForm cargasXMLForm) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			
			// logger.info("UUID===========>"+cargasXMLForm.getUuid());
			//stmt = con.prepareStatement(BovedaQuerys.getAltaBoveda(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt = con.prepareStatement(BovedaQuerys.getAltaBoveda(esquema));
			stmt.setString(1, cargasXMLForm.getUuid());
			stmt.setString(2, cargasXMLForm.getSerie());
			stmt.setString(3, cargasXMLForm.getFolio());
			stmt.setString(4, cargasXMLForm.getFechaFactura());
			stmt.setString(5, cargasXMLForm.getFormaPago());
			stmt.setString(6, cargasXMLForm.getMetodoPago());
			stmt.setString(7, cargasXMLForm.getTipoMoneda());
			stmt.setString(8, cargasXMLForm.getDesTipoMoneda());
			stmt.setDouble(9, cargasXMLForm.getSubTotal());
			stmt.setDouble(10, cargasXMLForm.getDescuento());
			stmt.setDouble(11, cargasXMLForm.getTotalImpuestoRet());
			stmt.setDouble(12, cargasXMLForm.getTotalImpuestoTranslado());
			stmt.setDouble(13, cargasXMLForm.getTotal());
			stmt.setString(14, cargasXMLForm.getEmisorRFC());
			stmt.setString(15, cargasXMLForm.getEmisorNombre());
			stmt.setString(16, cargasXMLForm.getReceptorRFC());
			stmt.setString(17, cargasXMLForm.getReceptorNombre());
			stmt.setDouble(18, cargasXMLForm.getRetencionIVA());
			stmt.setDouble(19, cargasXMLForm.getTransladoIVA());
			stmt.setDouble(20, cargasXMLForm.getRetencionISR());
			stmt.setDouble(21, cargasXMLForm.getTransladoIEPS());
			stmt.setString(22, cargasXMLForm.getNombreXML());
			stmt.setString(23, cargasXMLForm.getTipoComprobante());
			stmt.setString(24, cargasXMLForm.getFechaPago());
			stmt.setString(25, cargasXMLForm.getFechaTimbrado());
			// logger.info("stmt===>"+stmt);
			resultado = stmt.executeUpdate();
			/*
			if (cant > 0) {
				rs = stmt.getGeneratedKeys();
				if (rs.next())
					resultado = rs.getInt(1);
			}
			*/
		} catch (SQLException sql) {
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);

		} catch (Exception e) {
			resultado = 100;
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
				if (stmt != null)
					stmt.close();
				stmt = null;
			} catch (Exception e) {
				stmt = null;
			}
		}

		return resultado;
	}

	
	 
	/* =========================
	 * DETALLE (Excel sin paginar)
	 * ========================= */
	public ArrayList<BovedaForm> detalleBovedaEXCEL(
	    Connection con,
	    String esquema,
	    String rfc, String razonSocial, String folio, String serie,
	    String fechaInicial, String tipoComprobante, String uuidBoveda, String fechaFinal,
	    String cadRegistros, // "uuid1;uuid2;..."
	    // operadores texto
	    String rfcOperator, String razonOperator, String serieOperator, String tipoOperator, String uuidOperator,
	    // fecha
	    String dateOperator, String dateV1, String dateV2,
	    // numéricos
	    String folioOperator,  String folioV1,  String folioV2,
	    String totalOperator,  String totalV1,  String totalV2,
	    String subOperator,    String subV1,    String subV2,
	    String ivaOperator,    String ivaV1,    String ivaV2,
	    String ivaRetOperator, String ivaRetV1, String ivaRetV2,
	    String isrOperator,    String isrV1,    String isrV2,
	    String impLocOperator, String impLocV1, String impLocV2
	){
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<BovedaForm> datos = new ArrayList<>();

	    try {
	        StringBuilder sb = new StringBuilder(BovedaQuerys.getConsultaBoveda(esquema));
	        List<Object> params = new ArrayList<>();
	        FiltrosBoveda.Where w = new FiltrosBoveda.Where(sb, params);

	        // 1) Si hay selección explícita de UUIDs, úsala primero (ignora el resto de filtros)
	        List<String> seleccion = new ArrayList<>();
	        if (!isBlank(cadRegistros)) {
	            for (String u : cadRegistros.split(";")) {
	                if (u != null) {
	                    u = u.trim();
	                    if (!u.isEmpty()) seleccion.add(u);
	                }
	            }
	        }
	        if (!seleccion.isEmpty()){
	            addUuidIn(w, seleccion); // agrega "AND UUID IN (?,?,...)" + params
	        } else {
	            // 2) Caso normal: aplica filtros DX (mismo orden que en detalleBoveda)
	            FiltrosBoveda.aplicarFiltrosBoveda(
	                w,
	                // TEXTO
	                rfc,           rfcOperator,
	                razonSocial,   razonOperator,
	                serie,         serieOperator,
	                tipoComprobante, tipoOperator,
	                uuidBoveda,    uuidOperator,

	                // FECHAS (con fallback cabecera)
	                dateOperator,  dateV1, dateV2, fechaInicial, fechaFinal,

	                // NUMÉRICOS + FOLIO (texto + num)
	                folio,         folioOperator, folioV1, folioV2,
	                totalOperator, totalV1, totalV2,
	                subOperator,   subV1,   subV2,
	                ivaOperator,   ivaV1,   ivaV2,
	                ivaRetOperator,ivaRetV1,ivaRetV2,
	                isrOperator,   isrV1,   isrV2,
	                impLocOperator,impLocV1,impLocV2
	            );
	        }

	        sb.append(" ORDER BY FECHA_FACTURA DESC");

	        stmt = con.prepareStatement(sb.toString());

	        // Bind seguro de parámetros (respeta BigDecimal/Integer si los metiste así)
	        
	        int idx = 1;
	        for (Object p : params) {
	            if (p instanceof BigDecimal)      stmt.setBigDecimal(idx++, (BigDecimal)p);
	            else if (p instanceof Integer)    stmt.setInt(idx++, (Integer)p);
	            else                               stmt.setString(idx++, String.valueOf(p));
	            
	        }
		 
	        logger.info("🔁 detalleBovedaEXCEL → " + stmt);
	        rs = stmt.executeQuery();

	        DecimalFormat decimal = new DecimalFormat("###,###.##");
	        while (rs.next()) {
	            BovedaForm b = new BovedaForm();
	            b.setIdRegistro(rs.getInt(1));
	            b.setUuid(Utils.noNuloNormal(rs.getString(2)));
	            b.setRfc(Utils.noNulo(rs.getString(15)));
	            b.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(16))));
	            b.setSerie(Utils.noNulo(rs.getString(3)));
	            b.setFolio(Utils.noNulo(rs.getString(4)));
	            b.setTotal(decimal.format(rs.getDouble(14)));
	            b.setSubTotal(decimal.format(rs.getDouble(10)));
	            b.setIva(decimal.format(rs.getDouble(13)));
	            b.setRetIVA(decimal.format(rs.getDouble(19)));
	            b.setRetISR(decimal.format(rs.getDouble(21)));
	            b.setImpLocales("0"); // ajusta si tienes columna real
	            b.setXml(Utils.noNuloNormal(rs.getString(23)));
	            b.setFechaFactura(Utils.noNulo(rs.getString(5)));
	            b.setTipoComprobante(Utils.noNulo(rs.getString(24)));
	            datos.add(b);
	        }
	    } catch (Exception e) {
	        Utils.imprimeLog("detalleBovedaEXCEL(): ", e);
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
	    }
	    return datos;
	}

	/* =========================
	 * Helpers locales
	 * ========================= */
	private static boolean isBlank(String s){ return s == null || s.trim().isEmpty(); }

	/** Agrega un IN por UUID, en bloques (por si la selección es larga) */
	private static void addUuidIn(FiltrosBoveda.Where w, List<String> uuids){
	    if (uuids == null || uuids.isEmpty()) return;

	    // Si tu motor tiene límite de variables, puedes trocear (ej. 1000 por bloque)
	    final int CHUNK = 800; // seguro para MySQL/MariaDB
	    int from = 0;
	    while (from < uuids.size()){
	        int to = Math.min(from + CHUNK, uuids.size());
	        List<String> sub = uuids.subList(from, to);

	        StringBuilder frag = new StringBuilder("UUID IN (");
	        List<Object> vals = new ArrayList<>(sub.size());
	        for (int i = 0; i < sub.size(); i++){
	            if (i > 0) frag.append(',');
	            frag.append('?');
	            vals.add(sub.get(i));
	        }
	        frag.append(')');
	        // Usa el Where para agregar fragmento y valores
	        w.and(frag.toString(), vals.toArray());
	        from = to;
	    }
	}


	
	

	   @SuppressWarnings("unchecked")
	   public Map<String, Object> detalleBovedaZIP(
	           Connection con,
	           String esquema,
	           String rfc,
	           String razonSocial,
	           String folio,
	           String serie,
	           String fechaInicial,     // legado (usar solo si NO mandas dateOperator)
	           String tipoComprobante,
	           String uuidBoveda,
	           String fechaFinal,       // legado (usar solo si NO mandas dateOperator)
	           String cadRegistros,     // "uuid1;uuid2;..."
	           // ===== operadores texto =====
	           String rfcOperator,
	           String razonOperator,
	           String serieOperator,
	           String tipoOperator,
	           String uuidOperator,
	           // ===== FECHA con operadores =====
	           String dateOperator,     // eq, ne, lt, gt, le, ge, bt
	           String dateV1,           // YYYY-MM-DD
	           String dateV2,           // YYYY-MM-DD
	           // ===== operadores/valores numéricos =====
	           String folioOperator,  String folioV1,  String folioV2,
	           String totalOperator,  String totalV1,  String totalV2,
	           String subOperator,    String subV1,    String subV2,
	           String ivaOperator,    String ivaV1,    String ivaV2,
	           String ivaRetOperator, String ivaRetV1, String ivaRetV2,
	           String isrOperator,    String isrV1,    String isrV2,
	           String impLocOperator, String impLocV1, String impLocV2
	   ) {
	       PreparedStatement stmt = null;
	       ResultSet rs = null;
	       Map<String, Object> mapaRes = new java.util.HashMap<>();
	       org.json.simple.JSONArray jsonArray = new org.json.simple.JSONArray();

	       try {
	           StringBuilder sb = new StringBuilder(BovedaQuerys.getConsultaBoveda(esquema));
	           java.util.List<Object> params = new java.util.ArrayList<>();
	           // usamos el helper Where del padre
	           Where w = new Where(sb, params);

	           // 1) Si viene selección explícita de UUIDs, úsala; si no, aplica filtros estándar
	           java.util.List<String> seleccion = new java.util.ArrayList<>();
	           if (cadRegistros != null && !cadRegistros.trim().isEmpty()) {
	               for (String u : cadRegistros.split(";")) {
	                   if (u != null) {
	                       String t = u.trim();
	                       if (!t.isEmpty()) seleccion.add(t);
	                   }
	               }
	           }

	           if (!seleccion.isEmpty()) {
	               addUuidIn(w, seleccion); // UUID IN (?, ?, ...)
	           } else {
	               aplicarFiltrosBoveda(
	                   w,
	                   rfc,           rfcOperator,
	                   razonSocial,   razonOperator,
	                   serie,         serieOperator,
	                   tipoComprobante, tipoOperator,
	                   uuidBoveda,    uuidOperator,
	                   // fecha
	                   dateOperator, dateV1, dateV2, fechaInicial, fechaFinal,
	                   // numéricos
	                   folio, folioOperator, folioV1, folioV2,
	                   totalOperator, totalV1, totalV2,
	                   subOperator,   subV1,   subV2,
	                   ivaOperator,   ivaV1,   ivaV2,
	                   ivaRetOperator,ivaRetV1,ivaRetV2,
	                   isrOperator,   isrV1,   isrV2,
	                   impLocOperator,impLocV1,impLocV2
	               );
	           }

	           // Orden
	           sb.append(" ORDER BY FECHA_FACTURA DESC");

	           // Preparar y bindear
	           stmt = con.prepareStatement(sb.toString());
	           int idx = 1;
	           for (Object p : params) {
	               if (p instanceof java.math.BigDecimal) {
	                   stmt.setBigDecimal(idx++, (java.math.BigDecimal)p);
	               } else if (p instanceof Integer) {
	                   stmt.setInt(idx++, (Integer)p);
	               } else {
	                   stmt.setString(idx++, String.valueOf(p));
	               }
	           }

	           logger.info("🔁 detalleBovedaZIPN → " + stmt);
	           rs = stmt.executeQuery();

	           java.text.DecimalFormat decimal = new java.text.DecimalFormat("###,###.##");
	           while (rs.next()) {
	               org.json.simple.JSONObject jsonobj = new org.json.simple.JSONObject();
	               jsonobj.put("ID_REGISTRO",   rs.getInt(1));
	               jsonobj.put("UUID",          Utils.noNuloNormal(rs.getString(2)));
	               jsonobj.put("RFC",           Utils.noNulo(rs.getString(15)));
	               jsonobj.put("RAZON_SOCIAL",  Utils.noNulo(rs.getString(16)));
	               jsonobj.put("SERIE",         Utils.noNulo(rs.getString(3)));
	               jsonobj.put("FOLIO",         Utils.noNulo(rs.getString(4)));
	               jsonobj.put("TOTAL",         decimal.format(rs.getDouble(14)));
	               jsonobj.put("SUB-TOTAL",     decimal.format(rs.getDouble(10)));
	               jsonobj.put("IVA",           decimal.format(rs.getDouble(13)));
	               jsonobj.put("ISR RET",       decimal.format(rs.getDouble(21)));
	               jsonobj.put("IMP_LOCALES",   0);
	               jsonobj.put("XML",           Utils.noNuloNormal(rs.getString(23)));
	               jsonobj.put("FECHA_FACTURA", Utils.noNulo(rs.getString(5)));
	               jsonobj.put("TIPO_COMPROBANTE", Utils.noNulo(rs.getString(24)));
	               jsonArray.add(jsonobj);
	           }

	           mapaRes.put("detalle", jsonArray);
	           mapaRes.put("count", jsonArray.size());
	       } catch (Exception e) {
	           Utils.imprimeLog("detalleBovedaZIP(): ", e);
	       } finally {
	           try { if (rs != null) rs.close(); } catch (Exception ignored) {}
	           try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
	       }
	       return mapaRes;
	   }



	
	 @SuppressWarnings("unchecked")
		public JSONObject consultaBovedaRegistro(Connection con, String esquema, int idRegistro)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        JSONObject jsonobj = new JSONObject();
	        try{
	        	
	        	StringBuffer sbQuery = new StringBuffer(BovedaQuerys.getConsultaBovedaRegistro(esquema));
	        	stmt = con.prepareStatement(sbQuery.toString());
	        	stmt.setInt(1, idRegistro);
	        	rs = stmt.executeQuery();
	            DecimalFormat decimal = new DecimalFormat("###,###.##");
	            if(rs.next()){
					
						jsonobj.put("ID_REGISTRO", rs.getInt(1));
						jsonobj.put("UUID", Utils.noNuloNormal(rs.getString(2)));
						jsonobj.put("RFC", Utils.noNulo(rs.getString(15)));
						jsonobj.put("RAZON_SOCIAL", Utils.noNulo(rs.getString(16)));
						jsonobj.put("SERIE", Utils.noNulo(rs.getString(3)));
						jsonobj.put("FOLIO", Utils.noNulo(rs.getString(4)));
						jsonobj.put("TOTAL", decimal.format(rs.getDouble(14)));
						jsonobj.put("SUB-TOTAL", decimal.format(rs.getDouble(10)));
						jsonobj.put("IVA", decimal.format(rs.getDouble(13)));
						jsonobj.put("ISR RET", decimal.format(rs.getDouble(21)));
						jsonobj.put("IMP_LOCALES", 0);
						jsonobj.put("XML", Utils.noNuloNormal(rs.getString(23)));
						jsonobj.put("FECHA_FACTURA", Utils.noNulo(rs.getString(5)));
						jsonobj.put("TIPO_COMPROBANTE", Utils.noNulo(rs.getString(24)));
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
	        return jsonobj;
	    }
	 
	 
	   @SuppressWarnings("unchecked")
		public JSONObject consultaBovedaUUID(Connection con, String esquema, String uuid)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        JSONObject jsonobj = new JSONObject();
	        try{
	        	
	        	StringBuffer sbQuery = new StringBuffer(BovedaQuerys.getConsultaBovedaUUID(esquema));
	        	stmt = con.prepareStatement(sbQuery.toString());
	        	stmt.setString(1, uuid);
	        	
	        	// logger.info("stmtBoveda===>"+stmt);
	        	rs = stmt.executeQuery();
	            DecimalFormat decimal = new DecimalFormat("###,###.##");
	            if(rs.next()){
					
						jsonobj.put("ID_REGISTRO", rs.getInt(1));
						jsonobj.put("UUID", Utils.noNuloNormal(rs.getString(2)));
						jsonobj.put("RFC", Utils.noNulo(rs.getString(15)));
						jsonobj.put("RAZON_SOCIAL", Utils.noNulo(rs.getString(16)));
						jsonobj.put("SERIE", Utils.noNulo(rs.getString(3)));
						jsonobj.put("FOLIO", Utils.noNulo(rs.getString(4)));
						jsonobj.put("TOTAL", decimal.format(rs.getDouble(14)));
						jsonobj.put("SUB-TOTAL", decimal.format(rs.getDouble(10)));
						jsonobj.put("IVA", decimal.format(rs.getDouble(13)));
						jsonobj.put("ISR RET", decimal.format(rs.getDouble(21)));
						jsonobj.put("IMP_LOCALES", 0);
						jsonobj.put("XML", Utils.noNuloNormal(rs.getString(23)));
						jsonobj.put("FECHA_FACTURA", Utils.noNulo(rs.getString(5)));
						jsonobj.put("TIPO_COMPROBANTE", Utils.noNulo(rs.getString(24)));
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
	        return jsonobj;
	    }

	 
	 
	 public int eliminaBoveda(Connection con, String esquema, String cadRegistros, String rutaBoveda)
	    {
	        PreparedStatement stmt = null;
	        int resultado = 0;
	        try{
	        	
	        	String arrRegistros [] = cadRegistros.split(";");
	        	
	        	stmt = con.prepareStatement(BovedaQuerys.getEliminaBoveda(esquema));
	        	String uuid = null;
	        	String rutaFile = null;
	        	for (int x = 0; x < arrRegistros.length; x++){
	        		// uuid = buscarUUID(con, esquema, Integer.parseInt(arrRegistros[x]));
	        		uuid = arrRegistros[x];
	        		stmt.setString(1, arrRegistros[x]);
	        		resultado = stmt.executeUpdate();
	        		if (resultado > 0){
	        			rutaFile = rutaBoveda + uuid + ".xml";
	        			File fileBov = new File(rutaFile);
	        			if (fileBov.exists()){
	        				logger.info("Archivo ELiminado..."+fileBov.delete());
	        			}
	        			fileBov = null;
	        		}
	        	}
	        	
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
	        
	        return resultado;
	    }

	 public String buscarUUID(Connection con, String esquema, int idRegistro)
	    {
	        PreparedStatement stmt = null;
	        String uuid = "";
	        ResultSet rs = null;
	        try{
	        	
	        	stmt = con.prepareStatement(BovedaQuerys.getBuscaUUID(esquema));
	        	stmt.setInt(1, idRegistro);
	        	rs = stmt.executeQuery();
	        	if (rs.next()){
	        		uuid = Utils.noNuloNormal(rs.getString(1));
	        	}
	        	
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
	        
	        return uuid;
	    }
	 
	 
	
	public void actualizarEncontradosBoveda(Connection con, String esquema) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(BovedaQuerys.getEncontradosBoveda(esquema));
			stmt.setString(1, "S");
			stmt.setString(2, "N");
			stmt.executeUpdate();
		} catch (Exception e) {
			Utils.imprimeLog("actualizarSolicitud(): ", e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			} catch (Exception e) {
				stmt = null;
			}
		}
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public JSONArray datosVincularComplemento(
	    Connection con, String esquema,
	    String rfc, String razonSocial, String folio, String serie,
	    String fechaInicial, String uuidBoveda, String fechaFinal,
	    String cadRegistros, String datoConciliacion,
	    BovedaAction.FiltrosEntrada f
	){
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    JSONObject json = new JSONObject();
	    JSONArray jarray = new JSONArray();

	    try {
	        String sqlBase = BovedaQuerys.getDatosVincular(esquema);
	        StringBuilder sb = new StringBuilder(sqlBase);
	        List<Object> params = new ArrayList<>();

	        final String baseUP = sqlBase.toUpperCase();
	        final boolean baseTieneWhere           = baseUP.contains(" WHERE ");
	        final boolean baseTieneTipoPlaceholder = baseUP.contains("TIPO_COMPROBANTE = ?");

	        if (baseTieneTipoPlaceholder) {
	            params.add("P"); // Pago
	        }

	        // Siembra correctamente el estado de WHERE
	        boolean hasWhereNow = baseTieneWhere;
	        if (!hasWhereNow) {
	            sb.append(" WHERE 1=1 ");
	            hasWhereNow = true;
	        }

	        // Where dinámico
	        FiltrosBoveda.Where w = new FiltrosBoveda.Where(sb, params);
	        w.seedHasWhere(hasWhereNow);   // ⬅️  IMPORTANTE

	        final String tipoComprobante = "";
	        final String tipoOp = (f != null ? f.tipoOperator : "equals");

	        FiltrosBoveda.aplicarFiltrosBoveda(
	            w,
	            rfc,                 (f != null ? f.rfcOperator    : "contains"),
	            razonSocial,         (f != null ? f.razonOperator  : "contains"),
	            serie,               (f != null ? f.serieOperator  : "contains"),
	            tipoComprobante,     tipoOp,
	            uuidBoveda,          (f != null ? f.uuidOperator   : "contains"),
	            (f != null ? f.dateOperator : "eq"),
	            (f != null ? f.dateV1       : ""),
	            (f != null ? f.dateV2       : ""),
	            fechaInicial, fechaFinal,
	            folio,                         (f != null ? f.folioOperator    : "eq"), (f != null ? f.folioV1    : ""), (f != null ? f.folioV2    : ""),
	            (f != null ? f.totalOperator : "eq"), (f != null ? f.totalV1  : ""), (f != null ? f.totalV2  : ""),
	            (f != null ? f.subOperator   : "eq"), (f != null ? f.subV1    : ""), (f != null ? f.subV2    : ""),
	            (f != null ? f.ivaOperator   : "eq"), (f != null ? f.ivaV1    : ""), (f != null ? f.ivaV2    : ""),
	            (f != null ? f.ivaRetOperator: "eq"), (f != null ? f.ivaRetV1 : ""), (f != null ? f.ivaRetV2 : ""),
	            (f != null ? f.isrOperator   : "eq"), (f != null ? f.isrV1    : ""), (f != null ? f.isrV2    : ""),
	            (f != null ? f.impLocOperator: "eq"), (f != null ? f.impLocV1 : ""), (f != null ? f.impLocV2 : "")
	        );

	        if (cadRegistros != null && !cadRegistros.trim().isEmpty()) {
	            String[] arr = cadRegistros.split(";");
	            StringJoiner sj = new StringJoiner(",", "(", ")");
	            for (int i = 0; i < arr.length; i++) sj.add("?");
	            sb.append(" AND ID_REGISTRO IN ").append(sj.toString());
	            for (String s : arr) params.add(Integer.parseInt(s.trim()));
	        }

	        if (datoConciliacion != null && !datoConciliacion.trim().isEmpty()) {
	            sb.append(" AND CONCILIACION = ? ");
	            params.add(datoConciliacion.trim());
	        }

	        sb.append(" ORDER BY FECHA_FACTURA DESC ");

	        stmt = con.prepareStatement(sb.toString());
	        int idx = 1;
	        for (Object p : params) {
	            if (p instanceof BigDecimal)      stmt.setBigDecimal(idx++, (BigDecimal)p);
	            else if (p instanceof Integer)    stmt.setInt(idx++, (Integer)p);
	            else                               stmt.setString(idx++, String.valueOf(p));
	        }

	        logger.info("Stmt Vincular Boveda (con filtros) ---> " + stmt);

	        rs = stmt.executeQuery();
	        while (rs.next()){
	            json.put("ID_REGISTRO", rs.getInt(1));
	            json.put("UUID",        Utils.noNuloNormal(rs.getString(2)));
	            json.put("NOMBRE_XML",  Utils.noNuloNormal(rs.getString(3)));
	            jarray.add(json);
	            json = new JSONObject();
	        }

	    } catch (Exception e){
	        Utils.imprimeLog("datosVincularComplemento(): ", e);
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception ignore){}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore){}
	    }
	    return jarray;
	}


	




	
	
	public int escribeBitacora(Connection con, String esquema, String UUID, String estatus, long iden, String mensaje)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try
        {
        	stmt = con.prepareStatement(BovedaQuerys.getEscribeBitacora(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
        	stmt.setString(1, UUID);
        	stmt.setString(2, estatus);
        	stmt.setLong(3, iden);
        	stmt.setString(4, mensaje);
        	// logger.info("****************  stmt BOVEDA ***********"+stmt);
        	int cant = stmt.executeUpdate();
            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next())
                    resultado = rs.getInt(1);
            }
        }
        catch(SQLException sql){
        	resultado = sql.getErrorCode();
        	Utils.imprimeLog("", sql);
            
        }catch(Exception e){
        	resultado = 100;
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
        
        return resultado;
    }

	
	 @SuppressWarnings({ "unused", "unchecked" })
	public Map<String, Object > datosConsola(Connection con, String esquema, long IDEN)
   {
       PreparedStatement stmt = null;
		String uuid = "";
       ResultSet rs = null;
       JSONObject json = new JSONObject();
       JSONArray jArray = new JSONArray();
       int totE = 0;
       int totNE = 0;
       Map<String, Object > mapaRes = new HashMap<String, Object>();
       try{
       	
       	stmt = con.prepareStatement(BovedaQuerys.getDatosConsola(esquema));
       	stmt.setLong(1, IDEN);
       	rs = stmt.executeQuery();
       	final String E = "E";
	        final String NE = "NE";
	        String estatus = null;
	        
       	while (rs.next()){
       		estatus = Utils.noNulo(rs.getString(2));
       		if (estatus.equals(E)) {
       			totE++;
       		}
       		if (estatus.equals(NE)) {
       			totNE++;
       		}
       		json.put("UUID", Utils.noNuloNormal(rs.getString(1)));
       		json.put("ESTATUS", cadExitoso(estatus));
       		json.put("MENSAJE", Utils.noNuloNormal(rs.getString(3)));
       		jArray.add(json);
       		json = new JSONObject();
       	}
       	mapaRes.put("DETALLE_CONSOLA", jArray);
       	mapaRes.put("TOTE", totE);
       	mapaRes.put("TOTNE", totNE);
       	
       	
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
       
       return mapaRes;
   }
	

	public int totalesEstatus(Connection con, String esquema, long IDEN, String tipoConsola)
   {
       PreparedStatement stmt = null;
	  // String uuid = "";
       ResultSet rs = null;
       int totEstatus = 0;
       try{
       	
       	stmt = con.prepareStatement(BovedaQuerys.getTotalProcesado(esquema));
       	stmt.setLong(1, IDEN);
       	stmt.setString(2, tipoConsola);
       	
       	rs = stmt.executeQuery();
//      final String E = "E";
//		final String NE = "NE";
//	        String estatus = null;
	        
       	while (rs.next()){
       	 totEstatus = rs.getInt(1);
       	}
       	
       	
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
       
       return totEstatus;
   }
	
	public int infoVincular(
		    Connection con,
		    String esquema,
		    String rfc, String razonSocial, String folio, String serie,
		    String fechaInicial,
		    String uuidBoveda,
		    String fechaFinal,
		    String cadRegistros,
		    String estatusConciliacion,

		    // ===== NUEVOS (alineado a UI) =====
		    String tipoComprobante, // si viene vacío, se usa "P"
		    // texto
		    String rfcOperator, String razonOperator, String serieOperator, String uuidOperator,
		    // fecha
		    String dateOperator, String dateV1, String dateV2,
		    // numéricos
		    String folioOperator, String folioV1, String folioV2,
		    String totalOperator, String totalV1, String totalV2,
		    String subOperator,   String subV1,   String subV2,
		    String ivaOperator,   String ivaV1,   String ivaV2,
		    String ivaRetOperator,String ivaRetV1,String ivaRetV2,
		    String isrOperator,   String isrV1,   String isrV2,
		    String impLocOperator,String impLocV1,String impLocV2
		) {
		    PreparedStatement ps = null;
		    ResultSet rs = null;
		    int totalArchivos = 0;

		    try {
		        final String baseSql = BovedaQuerys.getTotalVincular(esquema); // Debe traer ... WHERE TIPO_COMPROBANTE = ?
		        StringBuilder sb = new StringBuilder(baseSql);
		        java.util.List<Object> params = new java.util.ArrayList<>();

		        // ===== helpers mínimos =====
		        java.util.function.Function<String,String> nz = s -> (s == null) ? "" : s.trim();
		        java.util.function.Function<String,java.math.BigDecimal> toBD = s -> {
		            if (s == null) return null;
		            String z = s.trim().replace(",", "");
		            if (z.isEmpty()) return null;
		            return new java.math.BigDecimal(z);
		        };
		        // normalizador de operadores de TEXTO (local)
		        java.util.function.Function<String,String> normTxt = s -> {
		            String v = nz.apply(s).toLowerCase();
		            switch (v) {
		                case "equals": case "=": return "equals";
		                case "notequals": case "!=": case "<>": return "notequals";
		                case "startswith": case "startsWith": case "sw": return "startswith";
		                case "endswith": case "endsWith": case "ew": return "endswith";
		                case "notcontains": case "not like": return "notcontains";
		                case "contains": case "like": default: return "contains";
		            }
		        };
		        java.util.function.Function<String,String> opNum = s -> {
		            String v = nz.apply(s).toLowerCase();
		            if (v.isEmpty()) return "eq";
		            if ("between".equals(v) || "range".equals(v) || "rango".equals(v) || "entre".equals(v)) return "bt";
		            if ("=".equals(v) || "==".equals(v) || "equals".equals(v) || "equal".equals(v)) return "eq";
		            if ("!=".equals(v) || "<>".equals(v) || "notequals".equals(v)) return "ne";
		            return v;
		        };
		        java.util.function.BiFunction<String,String,String> opToSql = (op, col) -> {
		            switch (opNum.apply(op)) {
		                case "eq": return col + " = ?";
		                case "ne": return col + " <> ?";
		                case "lt": return col + " < ?";
		                case "gt": return col + " > ?";
		                case "le": return col + " <= ?";
		                case "ge": return col + " >= ?";
		                case "bt": return col + " BETWEEN ? AND ?";
		                default:   return col + " = ?";
		            }
		        };

		        // === Detector robusto de WHERE (case-insensitive) ===
		        final java.util.concurrent.atomic.AtomicBoolean hasWhere =
		                new java.util.concurrent.atomic.AtomicBoolean(baseSql.toLowerCase().contains(" where "));
		        java.util.function.Supplier<String> W = () -> { String sep = hasWhere.get() ? " AND " : " WHERE "; hasWhere.set(true); return sep; };
		        java.util.function.BiConsumer<String,Object> AND = (frag, val) -> {
		            if (frag == null || frag.isEmpty()) return;
		            sb.append(W.get()).append(frag);
		            if (val != null) params.add(val);
		        };
		        java.util.function.BiConsumer<String,Object[]> AND2 = (frag, vals) -> {
		            if (frag == null || frag.isEmpty()) return;
		            sb.append(W.get()).append(frag);
		            if (vals != null) for (Object v : vals) if (v != null) params.add(v);
		        };

		        // ===== 0) Tipo Comprobante (primer placeholder del baseSql)
		        String tc = nz.apply(tipoComprobante).isEmpty() ? "P" : tipoComprobante.trim();
		        // se bindea *primero* más abajo con ps.setString(1, tc)

		        // ===== 1) Texto =====
		        // RFC
		        String rfcOp = normTxt.apply(rfcOperator);
		        if (!nz.apply(rfc).isEmpty()) {
		            switch (rfcOp) {
		                case "equals":      AND.accept("EMISOR_RFC = ?", rfc);                 break;
		                case "notequals":   AND.accept("EMISOR_RFC <> ?", rfc);                break;
		                case "startswith":  AND.accept("EMISOR_RFC LIKE ?", rfc + "%");        break;
		                case "endswith":    AND.accept("EMISOR_RFC LIKE ?", "%" + rfc);        break;
		                case "notcontains": AND.accept("EMISOR_RFC NOT LIKE ?", "%" + rfc + "%"); break;
		                default:            AND.accept("EMISOR_RFC LIKE ?", "%" + rfc + "%");  break;
		            }
		        }
		        // Razón social
		        String razOp = normTxt.apply(razonOperator);
		        if (!nz.apply(razonSocial).isEmpty()) {
		            switch (razOp) {
		                case "equals":      AND.accept("EMISOR_NOMBRE = ?", razonSocial);                break;
		                case "notequals":   AND.accept("EMISOR_NOMBRE <> ?", razonSocial);               break;
		                case "startswith":  AND.accept("EMISOR_NOMBRE LIKE ?", razonSocial + "%");       break;
		                case "endswith":    AND.accept("EMISOR_NOMBRE LIKE ?", "%" + razonSocial);       break;
		                case "notcontains": AND.accept("EMISOR_NOMBRE NOT LIKE ?", "%" + razonSocial + "%"); break;
		                default:            AND.accept("EMISOR_NOMBRE LIKE ?", "%" + razonSocial + "%"); break;
		            }
		        }
		        // Serie
		        String serOp = normTxt.apply(serieOperator);
		        if (!nz.apply(serie).isEmpty()) {
		            switch (serOp) {
		                case "equals":      AND.accept("SERIE = ?", serie);                break;
		                case "notequals":   AND.accept("SERIE <> ?", serie);               break;
		                case "startswith":  AND.accept("SERIE LIKE ?", serie + "%");       break;
		                case "endswith":    AND.accept("SERIE LIKE ?", "%" + serie);       break;
		                case "notcontains": AND.accept("SERIE NOT LIKE ?", "%" + serie + "%"); break;
		                default:            AND.accept("SERIE LIKE ?", "%" + serie + "%"); break;
		            }
		        }
		        // UUID
		        String uOp = normTxt.apply(uuidOperator);
		        if (!nz.apply(uuidBoveda).isEmpty()) {
		            switch (uOp) {
		                case "equals":      AND.accept("UUID = ?", uuidBoveda);                break;
		                case "notequals":   AND.accept("UUID <> ?", uuidBoveda);               break;
		                case "startswith":  AND.accept("UUID LIKE ?", uuidBoveda + "%");       break;
		                case "endswith":    AND.accept("UUID LIKE ?", "%" + uuidBoveda);       break;
		                case "notcontains": AND.accept("UUID NOT LIKE ?", "%" + uuidBoveda + "%"); break;
		                default:            AND.accept("UUID LIKE ?", "%" + uuidBoveda + "%"); break;
		            }
		        }

		        // ===== 2) Fecha =====
		        String dOp = nz.apply(dateOperator).toLowerCase();
		        String d1  = nz.apply(dateV1);
		        String d2  = nz.apply(dateV2);

		        if (!dOp.isEmpty() && !d1.isEmpty()) {
		            switch (dOp) {
		                case "eq": AND2.accept("FECHA_FACTURA BETWEEN ? AND ?", new Object[]{d1+" 00:00:00", d1+" 23:59:59"}); break;
		                case "ne": AND2.accept("NOT (FECHA_FACTURA BETWEEN ? AND ?)", new Object[]{d1+" 00:00:00", d1+" 23:59:59"}); break;
		                case "lt": AND.accept("FECHA_FACTURA < ?",  d1 + " 00:00:00"); break;
		                case "gt": AND.accept("FECHA_FACTURA > ?",  d1 + " 23:59:59"); break;
		                case "le": AND.accept("FECHA_FACTURA <= ?", d1 + " 23:59:59"); break;
		                case "ge": AND.accept("FECHA_FACTURA >= ?", d1 + " 00:00:00"); break;
		                case "bt":
		                    if (!d2.isEmpty()) AND2.accept("FECHA_FACTURA BETWEEN ? AND ?", new Object[]{d1+" 00:00:00", d2+" 23:59:59"});
		                    break;
		                default:
		                    if (!d2.isEmpty()) AND2.accept("FECHA_FACTURA BETWEEN ? AND ?", new Object[]{d1+" 00:00:00", d2+" 23:59:59"});
		                    else               AND2.accept("FECHA_FACTURA BETWEEN ? AND ?", new Object[]{d1+" 00:00:00", d1+" 23:59:59"});
		            }
		        } else if (!nz.apply(fechaInicial).isEmpty() && !nz.apply(fechaFinal).isEmpty()) {
		            AND2.accept("FECHA_FACTURA BETWEEN ? AND ?", new Object[]{fechaInicial+" 00:00:01", fechaFinal+" 23:59:59"});
		        }

		        // ===== 3) FOLIO exacto o numérico =====
		        boolean folioNum = !nz.apply(folioOperator).isEmpty() && (!nz.apply(folioV1).isEmpty() || !nz.apply(folioV2).isEmpty());
		        if (folioNum) {
		            java.math.BigDecimal v1 = toBD.apply(folioV1), v2 = toBD.apply(folioV2);
		            String col  = "CAST(FOLIO AS DECIMAL(20,0))"; // consistente con el resto del código
		            String frag = opToSql.apply(folioOperator, col);
		            if ("bt".equals(opNum.apply(folioOperator))) {
		                if (v1 != null && v2 != null) AND2.accept(frag, new Object[]{v1, v2});
		            } else if (v1 != null) {
		                AND.accept(frag, v1);
		            }
		        } else if (!nz.apply(folio).isEmpty()) {
		            AND.accept("FOLIO = ?", folio);
		        }

		        // ===== 4) Resto numéricos =====
		        // TOTAL
		        if (!nz.apply(totalOperator).isEmpty() && (!nz.apply(totalV1).isEmpty() || !nz.apply(totalV2).isEmpty())) {
		            java.math.BigDecimal v1 = toBD.apply(totalV1), v2 = toBD.apply(totalV2);
		            String frag = opToSql.apply(totalOperator, "TOTAL");
		            if ("bt".equals(opNum.apply(totalOperator))) {
		                if (v1 != null && v2 != null) AND2.accept(frag, new Object[]{v1, v2});
		            } else if (v1 != null) AND.accept(frag, v1);
		        }
		        // SUB_TOTAL
		        if (!nz.apply(subOperator).isEmpty() && (!nz.apply(subV1).isEmpty() || !nz.apply(subV2).isEmpty())) {
		            java.math.BigDecimal v1 = toBD.apply(subV1), v2 = toBD.apply(subV2);
		            String frag = opToSql.apply(subOperator, "SUB_TOTAL");
		            if ("bt".equals(opNum.apply(subOperator))) {
		                if (v1 != null && v2 != null) AND2.accept(frag, new Object[]{v1, v2});
		            } else if (v1 != null) AND.accept(frag, v1);
		        }
		        // IVA trasladado
		        if (!nz.apply(ivaOperator).isEmpty() && (!nz.apply(ivaV1).isEmpty() || !nz.apply(ivaV2).isEmpty())) {
		            java.math.BigDecimal v1 = toBD.apply(ivaV1), v2 = toBD.apply(ivaV2);
		            String frag = opToSql.apply(ivaOperator, "TOTAL_IMP_TRANSLADO");
		            if ("bt".equals(opNum.apply(ivaOperator))) {
		                if (v1 != null && v2 != null) AND2.accept(frag, new Object[]{v1, v2});
		            } else if (v1 != null) AND.accept(frag, v1);
		        }
		        // IVA retenido
		        if (!nz.apply(ivaRetOperator).isEmpty() && (!nz.apply(ivaRetV1).isEmpty() || !nz.apply(ivaRetV2).isEmpty())) {
		            java.math.BigDecimal v1 = toBD.apply(ivaRetV1), v2 = toBD.apply(ivaRetV2);
		            String frag = opToSql.apply(ivaRetOperator, "RETENCION_IVA");
		            if ("bt".equals(opNum.apply(ivaRetOperator))) {
		                if (v1 != null && v2 != null) AND2.accept(frag, new Object[]{v1, v2});
		            } else if (v1 != null) AND.accept(frag, v1);
		        }
		        // ISR retenido
		        if (!nz.apply(isrOperator).isEmpty() && (!nz.apply(isrV1).isEmpty() || !nz.apply(isrV2).isEmpty())) {
		            java.math.BigDecimal v1 = toBD.apply(isrV1), v2 = toBD.apply(isrV2);
		            String frag = opToSql.apply(isrOperator, "ISR_RET");
		            if ("bt".equals(opNum.apply(isrOperator))) {
		                if (v1 != null && v2 != null) AND2.accept(frag, new Object[]{v1, v2});
		            } else if (v1 != null) AND.accept(frag, v1);
		        }
		        // Impuestos locales (si tu tabla/consulta los trae; si no, comenta este bloque)
		        if (!nz.apply(impLocOperator).isEmpty() && (!nz.apply(impLocV1).isEmpty() || !nz.apply(impLocV2).isEmpty())) {
		            java.math.BigDecimal v1 = toBD.apply(impLocV1), v2 = toBD.apply(impLocV2);
		            String frag = opToSql.apply(impLocOperator, "IMP_LOCALES"); // <-- verifica nombre real
		            if ("bt".equals(opNum.apply(impLocOperator))) {
		                if (v1 != null && v2 != null) AND2.accept(frag, new Object[]{v1, v2});
		            } else if (v1 != null) AND.accept(frag, v1);
		        }

		        // ===== 5) cadRegistros (ID_REGISTRO IN ...) =====
		        java.util.List<Integer> ids = new java.util.ArrayList<>();
		        if (!nz.apply(cadRegistros).isEmpty()) {
		            for (String u : cadRegistros.split(";")) {
		                if (u != null) {
		                    String t = u.trim();
		                    if (!t.isEmpty()) ids.add(Integer.parseInt(t));
		                }
		            }
		            if (!ids.isEmpty()) {
		                String placeholders = String.join(",", java.util.Collections.nCopies(ids.size(), "?"));
		                sb.append(W.get()).append("ID_REGISTRO IN (" + placeholders + ")");
		                params.addAll(ids);
		            }
		        }

		        // ===== 6) Estatus conciliación =====
		        if (!nz.apply(estatusConciliacion).isEmpty()) {
		            AND.accept("CONCILIACION = ?", estatusConciliacion.trim());
		        }

		        // Ejecutar (bind correcto: primero 'tc', luego resto en orden)
		        ps = con.prepareStatement(sb.toString());
		        int idx = 1;
		        ps.setString(idx++, tc);
		        for (Object p : params) {
		            if (p instanceof java.math.BigDecimal) {
		                ps.setBigDecimal(idx++, (java.math.BigDecimal)p);
		            } else if (p instanceof Integer) {
		                ps.setInt(idx++, (Integer)p);
		            } else {
		                ps.setString(idx++, String.valueOf(p));
		            }
		        }

		        logger.info("🔁 infoVincular → " + ps);
		        rs = ps.executeQuery();
		        if (rs.next()) totalArchivos = rs.getInt(1);

		    } catch (Exception e) {
		        Utils.imprimeLog("infoVincular(): ", e);
		    } finally {
		        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
		        try { if (ps != null) ps.close(); } catch (Exception ignore) {}
		    }
		    return totalArchivos;
		}
	
	
	
	





	 
		
		
		public void procesarXmlBoveda(Connection con, String esquema, String esquemaEmpresa, File fileHTTP, Integer arrResultado [], boolean bandGuardarDescarga) {
			boolean isNomina = false;
			Comprobante _comprobante = null;
			ArrayList<Comprobante> listaDetalle = new ArrayList<Comprobante>();
			
			String rutaDesXML = null;
			String uuid = null;
			String serie = null;
			String folio = null;
			String fechaFactura = null;
			LocalDateTime fechaFacturaLD = null;
			String formaPago = null;
			String metodoPago = null;
			String tipoMoneda = null;
			String desMoneda = null;
			String numeroCuentaPago = null;
			double subTotal = 0;
			double descuento = 0;
			double totalImpuestoRet = 0;
			double totalImpuestoTranslado = 0;
			double total = 0;
			String emisorRFC = null;
			String emisorNombre = null;
			String receptorRFC = null;
			String receptorNombre = null;
			String retencionIVA = "0";
			String transladoIVA = "0";
			String retencionISR = "0";
			String transladoIEPS = "0";
			// String cadMoneda = null;
			String fechaPago   = null;
			String fechaTimbrado   = null;
			LocalDateTime fechaPagoLD   = null;
			LocalDateTime fechaTimbradoLD   = null;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			
			// String arrMoneda [] = null;
			
			HashMap<String, String> mapaISR = null;
			HashMap<String, String> mapaTrans = null;
			
			// UtilsValidaciones utilsVal = new UtilsValidaciones();
			XMLForm cargasXMLForm = new XMLForm();
			
			// arrResultado[0] = Exitoso;
			// arrResultado[1] = Duplicado;
			// arrResultado[2] = Error en RFC;
			// arrResultado[3] = Error en XML;
			// arrResultado[4] = Nomina;
			
			  try{
				    try {
				    	_comprobante = LeerXML.ObtenerComprobante(fileHTTP.getAbsolutePath());
				    }catch(Exception e) {
				    	Utils.imprimeLog("", e);
				    }
				    
				    subTotal     =  _comprobante.getSubTotal();
				    uuid         =  _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
					serie        =  _comprobante.getSerie();
					folio        =  _comprobante.getFolio();
					fechaFacturaLD =  _comprobante.getFecha();
					tipoMoneda   = _comprobante.getMoneda();
					
					
					if ("MXN".equalsIgnoreCase(tipoMoneda)) {
						desMoneda = "MXN";
						tipoMoneda = "1";
					}else if ("USD".equalsIgnoreCase(tipoMoneda)) {
						tipoMoneda = "2";
						desMoneda = "USD";
					}else {
						tipoMoneda = "1";
						desMoneda = _comprobante.getMoneda();
					}
					
					/*
					cadMoneda 	= utilsVal.validaDesMoneda(tipoMoneda);
					if (cadMoneda.indexOf(";") > -1){
						arrMoneda  = cadMoneda.split(";");
						desMoneda = arrMoneda[0];
						tipoMoneda = arrMoneda[1];
						if ("MXN".equalsIgnoreCase(tipoMoneda)){
							tipoMoneda = "1";
						}else{
							tipoMoneda = "2";
						}
					}else{
						tipoMoneda = "1";
					}
					*/
					
					emisorRFC    =  _comprobante.getEmisor().getRfc(); 
					emisorNombre =  _comprobante.getEmisor().getNombre();
					receptorRFC  = _comprobante.getReceptor().getRfc();
					receptorNombre = _comprobante.getReceptor().getNombre();

					
					String tipoComprobante   =  _comprobante.getTipoDeComprobante();
					//logger.info("tipoComprobante====>"+tipoComprobante);
					if ("N".equalsIgnoreCase(tipoComprobante) || "nomina".equalsIgnoreCase(tipoComprobante)) {
						isNomina = true;
					}else if ("I".equalsIgnoreCase(tipoComprobante) || "ingreso".equalsIgnoreCase(tipoComprobante)) {
						    tipoComprobante = "I";
						    formaPago    =  _comprobante.getFormaPago();
						    metodoPago   =  _comprobante.getMetodoPago();	
						    numeroCuentaPago =  "";
						    descuento    =  _comprobante.getDescuento();
						    if(_comprobante.getImpuestos() != null) {
						    	totalImpuestoRet =  _comprobante.getImpuestos().getTotalImpuestosRetenidos();
						    	if (totalImpuestoRet < 0) {
						    		totalImpuestoRet = 0;
						    	}
						    }
							if( _comprobante.getImpuestos() != null) {
								totalImpuestoTranslado = _comprobante.getImpuestos().getTotalImpuestosTrasladados();
								if (totalImpuestoTranslado < 0) {
									totalImpuestoTranslado = 0;
						    	}
							}
							total =  _comprobante.getTotal();
							List<Retencion> listRetencion = null;
							
							if( _comprobante.getImpuestos() != null) {
								listRetencion = _comprobante.getImpuestos().getRetenciones();
							}
							
						    mapaISR = UtilsValidaciones.getImporteImpuestoISRNuevo(listRetencion);
						    if (!mapaISR.isEmpty()){
						    	retencionIVA = mapaISR.get("IVA");
								retencionISR = mapaISR.get("ISR");
							}

						    List<Traslado> listaTranslados = null;
						    if( _comprobante.getImpuestos() != null) {
						    	listaTranslados = _comprobante.getImpuestos().getTraslados();
							}
						    mapaTrans = UtilsValidaciones.getImporteTransladosISRNuevo(listaTranslados);
							transladoIVA = mapaTrans.get("IVA");
							transladoIEPS = "0";
							

					 } else if ("T".equalsIgnoreCase(tipoComprobante)) {
						    total = _comprobante.getTotal();
						    
					    	if(_comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado() != null) {
					    		fechaTimbradoLD = _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado();
					    	}
					    	
					    	if(fechaTimbradoLD != null) {
					    		fechaTimbrado = fechaTimbradoLD.format(formatter);
					    	}
					    	if (fechaTimbrado.length() > 10) {
					    		fechaTimbrado = fechaTimbrado.substring(0, 10);
					    	}
					 } else {
						 	total = _comprobante.getTotal(); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/Pagos/Pago/@"+utils.ucFirst("monto", bandXML));

						 	if(_comprobante.getComplemento().Pagos != null) {
						 		fechaPagoLD    = _comprobante.getComplemento().Pagos.getPago().get(0).getFechaPago();  //   XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/Pagos/Pago/@FechaPago");
						 		fechaPago = fechaPagoLD.format(formatter);
						 	}

					    	fechaTimbradoLD   =  _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado(); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/TimbreFiscalDigital/@FechaTimbrado");
					    	
					    	fechaTimbrado = fechaTimbradoLD.format(formatter);
					    	// fechaFactura = fechaFacturaLD.format(formatter);

					    	if (fechaPago != null && fechaPago.length() > 10) {
					    		fechaPago = fechaPago.substring(0, 10);
					    	}
					    	if (fechaTimbrado.length() > 10) {
					    		fechaTimbrado = fechaTimbrado.substring(0, 10);
					    	}
					 }
					 
					if (isNomina) {
						logger.info("Archivo de nomina......"+uuid);
						arrResultado[4]++;
						
					}else {
						if ("egreso".equalsIgnoreCase(tipoComprobante)) {
							tipoComprobante = "E";
						}
						
						
						 fechaFactura = fechaFacturaLD.format(formatter);
						cargasXMLForm.setUuid(uuid);
						cargasXMLForm.setSerie(serie);
						cargasXMLForm.setFolio(folio);
						cargasXMLForm.setFechaFactura(fechaFactura);
						cargasXMLForm.setFormaPago(formaPago);
						cargasXMLForm.setMetodoPago(metodoPago);
						cargasXMLForm.setDesTipoMoneda(desMoneda);
						cargasXMLForm.setTipoMoneda(tipoMoneda); // 1 = MXN, 2 = USD
						cargasXMLForm.setNumeroCuentaPago(numeroCuentaPago);
						cargasXMLForm.setSubTotal(subTotal);
						cargasXMLForm.setDescuento(descuento);
						cargasXMLForm.setTotalImpuestoRet(totalImpuestoRet);
						cargasXMLForm.setTotalImpuestoTranslado(totalImpuestoTranslado);
						cargasXMLForm.setTotal(total);
						cargasXMLForm.setEmisorRFC(emisorRFC);
						cargasXMLForm.setEmisorNombre(emisorNombre);
						cargasXMLForm.setReceptorRFC(receptorRFC);
						cargasXMLForm.setReceptorNombre(receptorNombre);
						cargasXMLForm.setRetencionIVA(Utils.convertirDouble(retencionIVA));
						cargasXMLForm.setTransladoIVA(Utils.convertirDouble(transladoIVA));
						cargasXMLForm.setRetencionISR(Utils.convertirDouble(retencionISR));
						cargasXMLForm.setTransladoIEPS(Utils.convertirDouble(transladoIEPS));
						cargasXMLForm.setNombreXML(uuid+".xml");
						cargasXMLForm.setTipoComprobante(tipoComprobante);
						cargasXMLForm.setFechaPago(fechaPago);
						cargasXMLForm.setFechaTimbrado(fechaTimbrado);
					
						String RFC_RECEPTOR = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "RFC_RECEPTOR");
						// HashMap<String, String> mapaConfig = confAicionales.obtenerConfiguraciones(con, esquema); 
						//String RFC_RECEPTOR = Utils.noNulo(mapaConfig.get("RFC_RECEPTOR"));
						boolean bandAlta = false;
						 if ("S".equalsIgnoreCase(RFC_RECEPTOR)){
							String VALOR_RFC_RECEPTOR = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "VALOR_RFC_RECEPTOR");
							if (VALOR_RFC_RECEPTOR.equalsIgnoreCase(receptorRFC)) {
								 bandAlta = true;	
							}
						 }else {
							 bandAlta = true;
						 }
						 if (bandAlta){
							  int res = altaBoveda(con, esquema, cargasXMLForm);
								if (res == 1062){
									// numFilesNG++;
									arrResultado[1]++;
								}else if (res == 1) {
									rutaDesXML = UtilsPATH.REPOSITORIO_DOCUMENTOS +esquemaEmpresa+"/BOVEDA/" + uuid + ".xml";
									File fdesXML = new File(rutaDesXML);
									UtilsFile.moveFileDirectory(fileHTTP, fdesXML, true, false, true);
									// numFilesOK++;
									 arrResultado[0]++;
								}else{
									arrResultado[3]++;
								}
								
								
								if (bandGuardarDescarga) {
									listaDetalle.add(_comprobante);
									DescargaMasivaLocal descargaLocal = new DescargaMasivaLocal();
									descargaLocal.guardarMetadataTimbrado(con, esquema, listaDetalle);
								}
								
						 }else {
							 // numFilesRFC++;
							 arrResultado[2]++;
						 }
				 }
			  }catch(Exception e){
				  Utils.imprimeLog("", e);
				  // numFilesXML++;
				  arrResultado[3]++;
			  }
		}
		
	 
		private String cadExitoso(String idExitoso) {
			if ("E".equalsIgnoreCase(idExitoso)) {
				return "Exitoso";
			}
			return "No Exitoso";
		}
	 

		
		public ArrayList<BovedaForm> comboProveedores(Connection con, String esquema, String idLengueje) {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        BovedaForm bovedaForm = new BovedaForm();
	        ArrayList<BovedaForm> listaCombo = new ArrayList<>();
	        // LenguajeBean lenguajeBean = LenguajeBean.instance();
	        
	        try{
	        	
	        	bovedaForm.setRfc("");
	        	bovedaForm.setRazonSocial("Seleccione un Emisor");
	        	listaCombo.add(bovedaForm);
	        	bovedaForm = new BovedaForm();
	        	
	        	StringBuffer sbQuery = new StringBuffer(BovedaQuerys.getComboProveedores(esquema));
	        	
	            stmt = con.prepareStatement(sbQuery.toString());
	            rs = stmt.executeQuery();
		        
		        while (rs.next()){
		        	
		        	bovedaForm.setRfc(Utils.noNulo(rs.getString(1)));
		        	bovedaForm.setRazonSocial(bovedaForm.getRfc() + " - " +  Utils.noNulo(rs.getString(2)));
		        	listaCombo.add(bovedaForm);
		        	bovedaForm = new BovedaForm();
		        }
		        
		        
	        } catch(Exception e){
	        	Utils.imprimeLog("", e);
	        } finally{
	        	try{
	   	        	 if(rs != null) {
	   	                rs.close();
	   	            }
	   	            rs = null;
	   	            if(stmt != null) {
	   	                stmt.close();
	   	            }
	   	            stmt = null;
	        	}
	        	catch(Exception e){
	        		rs = null;
	        		stmt = null;
	        	}
	        }
	        return listaCombo;
	    }

		
		 public String consultarFechaMinima(Connection con, String esquema)
		    {
		        PreparedStatement stmt = null;
		        String fechaMinima = "";
		        ResultSet rs = null;
		        try{
		        	
		        	stmt = con.prepareStatement(BovedaQuerys.getConsultaFechaMinima(esquema));
		        	rs = stmt.executeQuery();
		        	if (rs.next()){
		        		fechaMinima = Utils.noNuloNormal(rs.getString(1));
		        	}
		        	
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
		        
		        return fechaMinima;
		    }
		 
		 
}
