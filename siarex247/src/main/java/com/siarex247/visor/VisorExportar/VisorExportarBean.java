package com.siarex247.visor.VisorExportar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;
import com.siarex247.visor.VisorOrdenes.FiltrosVisorOrdenesFiltros;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;

public class VisorExportarBean extends FiltrosVisorOrdenesFiltros {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	/*
	public ArrayList<VisorOrdenesForm> detallePlantillas (Connection con, String esquema, String tipoMoneda, String estatusOrden,  
			long folioEmpresa, String rfcProveedor, String razonSocial, String uuid, String serieFolio, String fechaInicial, String fechaFinal, String foliosEliminar){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		ArrayList<VisorOrdenesForm> listaDetalle = new ArrayList<>();
		VisorOrdenesForm visorForm = new VisorOrdenesForm();
		String [] foliosEmpresaCadena = null;
        try {
        	
        	
        	if ("".equalsIgnoreCase(foliosEliminar)) {
				StringBuffer sbQuery = new StringBuffer(VisorExportarQuerys.getDetallePlantillaFiltros(esquema));
        		
        		if (folioEmpresa > 0) {
    				sbQuery.append(" where A.FOLIO_EMPRESA = ? ");
    			}
    			
    			if (!"".equals(Utils.noNulo(fechaInicial)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.FECHAULTMOV between ? and ?");
    			}else if (!"".equals(Utils.noNulo(fechaInicial))){
    				sbQuery.append(" and A.FECHAULTMOV between ? and ?");
    			}
    			
    			if (!"".equals(Utils.noNulo(tipoMoneda)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.TIPO_MONEDA = ?");
    			}else if (!"".equals(Utils.noNulo(tipoMoneda))){
    				sbQuery.append(" and A.TIPO_MONEDA = ?");
    			}
    			
    			
    			if (!"".equals(Utils.noNulo(estatusOrden)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.ESTATUS_PAGO = ?");
    			}else if (!"".equals(Utils.noNulo(estatusOrden))){
    				sbQuery.append(" and A.ESTATUS_PAGO = ?");
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(rfcProveedor)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where B.RFC =  ?");
    			}else if (!"".equalsIgnoreCase(Utils.noNulo(rfcProveedor))) {
    				sbQuery.append(" and B.RFC =  ?");
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(razonSocial)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where B.RAZON_SOCIAL like '%").append(razonSocial).append("%'");
    			}else if (!"".equalsIgnoreCase(Utils.noNulo(razonSocial))) {
    				sbQuery.append(" and B.RAZON_SOCIAL like '%").append(razonSocial).append("%'");
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(uuid)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.UUID =  ?");
    			}else if (!"".equalsIgnoreCase(Utils.noNulo(uuid))) {
    				sbQuery.append(" and A.UUID =  ?");
    			}
    			
    			if (serieFolio.startsWith("%") && serieFolio.endsWith("%")){
    				if (sbQuery.indexOf("where ") == -1){
    					sbQuery.append(" where A.SERIE like  '"+serieFolio+"' ");	
    				}else {
    					sbQuery.append(" and A.SERIE like  '"+serieFolio+"' ");
    				}
    				
            	}else if (serieFolio.startsWith("%")){
            		if (sbQuery.indexOf("where ") == -1){
            			sbQuery.append(" where A.SERIE like '"+serieFolio+"%' ");	
            		}else {
            			sbQuery.append(" and A.SERIE like '"+serieFolio+"%' ");
            		}
            	}else if (!"".equalsIgnoreCase(serieFolio)){
            		if (sbQuery.indexOf("where ") == -1){
            			sbQuery.append(" where A.SERIE = '").append(serieFolio).append("'");	
            		}else {
            			sbQuery.append(" and A.SERIE = '").append(serieFolio).append("'");
            		}
            	}
    			
    			sbQuery.append(" order by A.FECHAULTMOV desc");
    			stmt = con.prepareStatement(sbQuery.toString());
    			
    			int numParam=1;
    			
    			if (folioEmpresa > 0) {
    				stmt.setLong(numParam++, folioEmpresa);
    			}

    			if (!"".equals(Utils.noNulo(fechaInicial))) {
    				stmt.setString(numParam++, fechaInicial + " 01:01:01");
    				stmt.setString(numParam++, fechaFinal + " 23:59:59");
    			}

    			
    			if (!"".equals(Utils.noNulo(tipoMoneda))) {
    				stmt.setString(numParam++, tipoMoneda);
    			}
    			
    			if (!"".equals(Utils.noNulo(estatusOrden))) {
    				stmt.setString(numParam++, estatusOrden);
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(rfcProveedor))) {
    				stmt.setString(numParam++, rfcProveedor);
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(uuid))) {
    				stmt.setString(numParam++, uuid);
    			}
    			
        	}else { // tiene seleccionado al menos 1 registro
        		if (foliosEliminar.indexOf(";") > -1){
                	foliosEmpresaCadena = foliosEliminar.split(";"); 
                }
            	
    			StringBuffer sbQuery = new StringBuffer(VisorExportarQuerys.getDetallePlantilla(esquema));
    			for (int x = 0; x < foliosEmpresaCadena.length; x++){
    				sbQuery.append("?,");
            	}
    			String queryFinal = sbQuery.substring(0, sbQuery.length() - 1) + ")";
    			sbQuery.append(" order by A.FECHAULTMOV desc");
    			
    			stmt = con.prepareStatement(queryFinal);
    			
    			int numParam=1;
    			for (int x = 0; x < foliosEmpresaCadena.length; x++){
            		stmt.setLong(numParam++, Long.parseLong(foliosEmpresaCadena[x]));
            	}
    			
        	}
        	
        	// logger.info("stmtPlantillas===>"+stmt);
        	
        	rs = stmt.executeQuery();
            while (rs.next()) {
            	visorForm.setRfc(Utils.noNulo(rs.getString(1)));
            	visorForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(2))));
            	visorForm.setFolioEmpresa(rs.getLong(4));
            	visorForm.setTipoMoneda(Utils.noNulo(rs.getString(5)));
            	visorForm.setMonto(Utils.noNulo(rs.getString(6)));
            	visorForm.setTotal(Utils.noNulo(rs.getString(7)));
            	visorForm.setSubTotal(Utils.noNulo(rs.getString(8)));
            	visorForm.setTipoValidacionPro(Utils.noNulo(rs.getString(9)));
            	visorForm.setTipoProveedor(Utils.noNulo(rs.getString(10)));
            	listaDetalle.add(visorForm);
            	visorForm = new VisorOrdenesForm();
				
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
	} */
	
	public ArrayList<VisorOrdenesForm> detallePlantillas(
		    Connection con, String esquema,
		    // ===== TEXTO/SELECTS =====
		    String razonSocial, String rsOp,
		    String ocOp, String ocV1, String ocV2,
		    String descripcion, String descOp,
		    String tipoMoneda, String monedaOp,
		    String servicioRecibo, String reciboOp,
		    String estatusPago, String estatusPagoOp,
		    String serieFolio, String serieFolioOp,
		    String asignarA, String asignarOp,
		    String estadoCfdi, String estadoCfdiOp,
		    String estatusSat, String estatusSatOp,
		    String usoCfdi, String usoCfdiOp,
		    String cps, String cpsOp,
		    // ===== NUM =====
		    String montoOp, String montoV1, String montoV2,
		    String totalOp, String totalV1, String totalV2,
		    String subtotalOp, String subtotalV1, String subtotalV2,
		    String ivaOp, String ivaV1, String ivaV2,
		    String ivaretOp, String ivaretV1, String ivaretV2,
		    String isrretOp, String isrretV1, String isrretV2,
		    String implocOp, String implocV1, String implocV2,
		    String totalncOp, String totalncV1, String totalncV2,
		    String pagotOp, String pagotV1, String pagotV2,
		    String ivaretncOp, String ivaretncV1, String ivaretncV2,
		    // ===== FECHAS =====
		    String fechapagoOp, String fechapagoV1, String fechapagoV2,
		    String ultmovOp, String ultmovV1, String ultmovV2,
		    // selección explícita
		    String foliosExportar
		){
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  ArrayList<VisorOrdenesForm> lista = new ArrayList<>();
		  try{
		    StringBuilder sb = new StringBuilder(VisorExportarQuerys.getDetallePlantillaFiltros(esquema));

		    // WHERE 1=1 si no existe
		    String lower = sb.toString().toLowerCase();
		    if (!lower.contains(" where ")) sb.append(" WHERE 1=1 ");

		    java.util.List<Object> params = new java.util.ArrayList<>();

		    if (isNotBlank(foliosExportar)) {
		      String[] items = foliosExportar.split(";");
		      sb.append(" AND A.FOLIO_EMPRESA IN (");
		      for (int i=0; i<items.length; i++){
		        sb.append(i==0 ? "?" : ",?");
		        params.add(items[i].trim());
		      }
		      sb.append(")");
		    } else {
		      FiltrosVisorOrdenesFiltros.Where w = new FiltrosVisorOrdenesFiltros.Where(){
		        @Override public void and(String frag, Object... vals){
		          if (frag==null || frag.isEmpty()) return;
		          sb.append(" AND ").append(frag);
		          if (vals!=null) for(Object v: vals) if (v!=null) params.add(v);
		        }
		      };
		      FiltrosVisorOrdenesFiltros.aplicarFiltrosVisor(
		        w,
		        // TEXTO / SELECTS
		        razonSocial, rsOp,
		        // OC
		        ocOp, ocV1, ocV2,
		        // resto
		        descripcion,  descOp,
		        tipoMoneda,   monedaOp,
		        servicioRecibo, reciboOp,
		        estatusPago,  estatusPagoOp,
		        serieFolio,   serieFolioOp,
		        asignarA,     asignarOp,
		        estadoCfdi,   estadoCfdiOp,
		        estatusSat,   estatusSatOp,
		        usoCfdi,      usoCfdiOp,
		        cps,          cpsOp,
		        // NUM
		        montoOp,     montoV1,     montoV2,
		        totalOp,     totalV1,     totalV2,
		        subtotalOp,  subtotalV1,  subtotalV2,
		        ivaOp,       ivaV1,       ivaV2,
		        ivaretOp,    ivaretV1,    ivaretV2,
		        isrretOp,    isrretV1,    isrretV2,
		        implocOp,    implocV1,    implocV2,
		        totalncOp,   totalncV1,   totalncV2,
		        pagotOp,     pagotV1,     pagotV2,
		        ivaretncOp,  ivaretncV1,  ivaretncV2,
		        // FECHAS
		        fechapagoOp, fechapagoV1, fechapagoV2,
		        ultmovOp,    ultmovV1,    ultmovV2
		      );
		    }

		    sb.append(" ORDER BY A.FECHAULTMOV DESC ");

		    stmt = con.prepareStatement(sb.toString());
		    int i=1; for(Object p: params){ stmt.setString(i++, String.valueOf(p)); }

		     logger.info("[EXPORT/PLANTILLAS/SQL-DX] " + stmt);

		    rs = stmt.executeQuery();
		    while(rs.next()){
		      VisorOrdenesForm v = new VisorOrdenesForm();
		      // Mapea aquí exactamente el orden de columnas que retorna:
		      // (coincide con tu anterior detallePlantillas())
		      v.setRfc(Utils.noNulo(rs.getString(1)));
		      v.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(2))));
		      v.setFolioEmpresa(rs.getLong(4));
		      v.setTipoMoneda(Utils.noNulo(rs.getString(5)));
		      v.setMonto(Utils.noNulo(rs.getString(6)));
		      v.setTotal(Utils.noNulo(rs.getString(7)));
		      v.setSubTotal(Utils.noNulo(rs.getString(8)));
		      v.setTipoValidacionPro(Utils.noNulo(rs.getString(9)));
		      v.setTipoProveedor(Utils.noNulo(rs.getString(10)));
		      lista.add(v);
		    }
		  }catch(Exception e){
		    Utils.imprimeLog("detallePlantillasDX(): ", e);
		  }finally{
		    try{ if (rs!=null) rs.close(); }catch(Exception ignore){}
		    try{ if (stmt!=null) stmt.close(); }catch(Exception ignore){}
		  }
		  return lista;
		}

	

	
	
	
	
	/*
	public ArrayList<VisorOrdenesForm> detalleFacturas (Connection con, String esquema, String tipoMoneda, String estatusOrden,  
			long folioEmpresa, String rfcProveedor, String razonSocial, String uuid, String serieFolio, String fechaInicial, String fechaFinal, String foliosEliminar){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		ArrayList<VisorOrdenesForm> listaDetalle = new ArrayList<>();
		VisorOrdenesForm visorForm = new VisorOrdenesForm();
		String [] foliosEmpresaCadena = null;
        try {
        	if ("".equalsIgnoreCase(foliosEliminar)) {
				StringBuffer sbQuery = new StringBuffer(VisorExportarQuerys.getDetalleFacturasFiltros(esquema));
        		
        		if (folioEmpresa > 0) {
    				sbQuery.append(" where A.FOLIO_EMPRESA = ? ");
    			}
    			
    			if (!"".equals(Utils.noNulo(fechaInicial)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.FECHAULTMOV between ? and ?");
    			}else if (!"".equals(Utils.noNulo(fechaInicial))){
    				sbQuery.append(" and A.FECHAULTMOV between ? and ?");
    			}
    			
    			if (!"".equals(Utils.noNulo(tipoMoneda)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.TIPO_MONEDA = ?");
    			}else if (!"".equals(Utils.noNulo(tipoMoneda))){
    				sbQuery.append(" and A.TIPO_MONEDA = ?");
    			}
    			
    			
    			if (!"".equals(Utils.noNulo(estatusOrden)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.ESTATUS_PAGO = ?");
    			}else if (!"".equals(Utils.noNulo(estatusOrden))){
    				sbQuery.append(" and A.ESTATUS_PAGO = ?");
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(rfcProveedor)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where B.RFC =  ?");
    			}else if (!"".equalsIgnoreCase(Utils.noNulo(rfcProveedor))) {
    				sbQuery.append(" and B.RFC =  ?");
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(razonSocial)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where B.RAZON_SOCIAL like '%").append(razonSocial).append("%'");
    			}else if (!"".equalsIgnoreCase(Utils.noNulo(razonSocial))) {
    				sbQuery.append(" and B.RAZON_SOCIAL like '%").append(razonSocial).append("%'");
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(uuid)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.UUID =  ?");
    			}else if (!"".equalsIgnoreCase(Utils.noNulo(uuid))) {
    				sbQuery.append(" and A.UUID =  ?");
    			}
    			
    			if (serieFolio.startsWith("%") && serieFolio.endsWith("%")){
    				if (sbQuery.indexOf("where ") == -1){
    					sbQuery.append(" where A.SERIE like  '"+serieFolio+"' ");	
    				}else {
    					sbQuery.append(" and A.SERIE like  '"+serieFolio+"' ");
    				}
    				
            	}else if (serieFolio.startsWith("%")){
            		if (sbQuery.indexOf("where ") == -1){
            			sbQuery.append(" where A.SERIE like '"+serieFolio+"%' ");	
            		}else {
            			sbQuery.append(" and A.SERIE like '"+serieFolio+"%' ");
            		}
            	}else if (!"".equalsIgnoreCase(serieFolio)){
            		if (sbQuery.indexOf("where ") == -1){
            			sbQuery.append(" where A.SERIE = '").append(serieFolio).append("'");	
            		}else {
            			sbQuery.append(" and A.SERIE = '").append(serieFolio).append("'");
            		}
            	}
    			
    			
    			stmt = con.prepareStatement(sbQuery.toString());
    			
    			int numParam=1;
    			
    			if (folioEmpresa > 0) {
    				stmt.setLong(numParam++, folioEmpresa);
    			}

    			if (!"".equals(Utils.noNulo(fechaInicial))) {
    				stmt.setString(numParam++, fechaInicial + " 01:01:01");
    				stmt.setString(numParam++, fechaFinal + " 23:59:59");
    			}

    			
    			if (!"".equals(Utils.noNulo(tipoMoneda))) {
    				stmt.setString(numParam++, tipoMoneda);
    			}
    			
    			if (!"".equals(Utils.noNulo(estatusOrden))) {
    				stmt.setString(numParam++, estatusOrden);
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(rfcProveedor))) {
    				stmt.setString(numParam++, rfcProveedor);
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(uuid))) {
    				stmt.setString(numParam++, uuid);
    			}
    			
        	}else { // tiene seleccionado al menos 1 registro
	        	if (foliosEliminar.indexOf(";") > -1){
	            	foliosEmpresaCadena = foliosEliminar.split(";"); 
	            }
	        	
				StringBuffer sbQuery = new StringBuffer(VisorExportarQuerys.getDetalleFacturas(esquema));
				for (int x = 0; x < foliosEmpresaCadena.length; x++){
					sbQuery.append("?,");
	        	}
				String queryFinal = sbQuery.substring(0, sbQuery.length() - 1) + ")";
				stmt = con.prepareStatement(queryFinal);
				
				int numParam=1;
				for (int x = 0; x < foliosEmpresaCadena.length; x++){
	        		stmt.setLong(numParam++, Long.parseLong(foliosEmpresaCadena[x]));
	        	}
        	}
			logger.info("stmtFacturas===>"+stmt);
			rs  = stmt.executeQuery();
			
            while (rs.next()) {
            	visorForm.setEstatusOrden(Utils.noNulo(rs.getString(1)));
            	visorForm.setNombreXML(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(2))));
				visorForm.setNombrePDF(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(3))));
				visorForm.setTipoOrden(Utils.noNulo(rs.getString(4)));
				visorForm.setClaveProveedor(rs.getInt(5));
				visorForm.setTieneComplementoXML(Utils.noNuloNormal(rs.getString(6)));
				visorForm.setTieneComplementoPDF(Utils.noNuloNormal(rs.getString(7)));
				visorForm.setTieneNotaCreditoXML(Utils.noNuloNormal(rs.getString(8)));
				visorForm.setTieneNotaCreditoPDF(Utils.noNuloNormal(rs.getString(9)));
				listaDetalle.add(visorForm);
				visorForm = new VisorOrdenesForm();
            	
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
	} */
	public ArrayList<VisorOrdenesForm> detalleFacturas(
		    Connection con, String esquema,
		    // ===== TEXTO/SELECTS =====
		    String razonSocial, String rsOp,
		    String ocOp, String ocV1, String ocV2,
		    String descripcion, String descOp,
		    String tipoMoneda, String monedaOp,
		    String servicioRecibo, String reciboOp,
		    String estatusPago, String estatusPagoOp,
		    String serieFolio, String serieFolioOp,
		    String asignarA, String asignarOp,
		    String estadoCfdi, String estadoCfdiOp,
		    String estatusSat, String estatusSatOp,
		    String usoCfdi, String usoCfdiOp,
		    String cps, String cpsOp,
		    // ===== NUM =====
		    String montoOp, String montoV1, String montoV2,
		    String totalOp, String totalV1, String totalV2,
		    String subtotalOp, String subtotalV1, String subtotalV2,
		    String ivaOp, String ivaV1, String ivaV2,
		    String ivaretOp, String ivaretV1, String ivaretV2,
		    String isrretOp, String isrretV1, String isrretV2,
		    String implocOp, String implocV1, String implocV2,
		    String totalncOp, String totalncV1, String totalncV2,
		    String pagotOp, String pagotV1, String pagotV2,
		    String ivaretncOp, String ivaretncV1, String ivaretncV2,
		    // ===== FECHAS =====
		    String fechapagoOp, String fechapagoV1, String fechapagoV2,
		    String ultmovOp, String ultmovV1, String ultmovV2,
		    // selección explícita
		    String foliosExportar
		){
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  ArrayList<VisorOrdenesForm> lista = new ArrayList<>();

		  try{
		    // 1) Parte fija (con alias A,B,C,N *correctos* para tus columnas)
		    StringBuilder sb = new StringBuilder(VisorExportarQuerys.getDetalleFacturasFiltros(esquema));

		    // Asegura WHERE 1=1 para facilitar ‘AND …’
		    String lower = sb.toString().toLowerCase();
		    if (!lower.contains(" where ")) sb.append(" WHERE 1=1 ");

		    java.util.List<Object> params = new java.util.ArrayList<>();

		    // 2) Si hay selección directa, IN (...)
		    if (foliosExportar != null && !foliosExportar.trim().isEmpty()){
		      String[] items = foliosExportar.split(";");
		      sb.append(" AND A.FOLIO_EMPRESA IN (");
		      for (int i=0;i<items.length;i++){
		        sb.append("?");
		        if (i<items.length-1) sb.append(",");
		        params.add(items[i].trim());
		      }
		      sb.append(")");
		    }else{
		      // 3) Aplica TODOS los filtros DX con tu builder (usa alias de ese SELECT)
		      FiltrosVisorOrdenesFiltros.Where w = new FiltrosVisorOrdenesFiltros.Where(){
		        @Override public void and(String frag, Object... vals){
		          if (frag == null || frag.isEmpty()) return;
		          sb.append(" AND ").append(frag);
		          if (vals != null) for(Object v: vals) if (v != null) params.add(v);
		        }
		      };

		      FiltrosVisorOrdenesFiltros.aplicarFiltrosVisor(
		        w,
		        // TEXTO/SELECTS
		        razonSocial, rsOp,
		        // OC numérico
		        ocOp, ocV1, ocV2,
		        // resto
		        descripcion, descOp,
		        tipoMoneda,  monedaOp,
		        servicioRecibo, reciboOp,
		        estatusPago, estatusPagoOp,
		        serieFolio,  serieFolioOp,
		        asignarA,    asignarOp,
		        estadoCfdi,  estadoCfdiOp,
		        estatusSat,  estatusSatOp,
		        usoCfdi,     usoCfdiOp,
		        cps,         cpsOp,
		        // NUM
		        montoOp,     montoV1,     montoV2,
		        totalOp,     totalV1,     totalV2,
		        subtotalOp,  subtotalV1,  subtotalV2,
		        ivaOp,       ivaV1,       ivaV2,
		        ivaretOp,    ivaretV1,    ivaretV2,
		        isrretOp,    isrretV1,    isrretV2,
		        implocOp,    implocV1,    implocV2,
		        totalncOp,   totalncV1,   totalncV2,
		        pagotOp,     pagotV1,     pagotV2,
		        ivaretncOp,  ivaretncV1,  ivaretncV2,
		        // FECHAS
		        fechapagoOp, fechapagoV1, fechapagoV2,
		        ultmovOp,    ultmovV1,    ultmovV2
		      );
		    }

		    // 4) Orden estable
		    sb.append(" ORDER BY A.FECHAULTMOV DESC ");

		    stmt = con.prepareStatement(sb.toString());
		    int i=1; for(Object p : params){ stmt.setString(i++, String.valueOf(p)); }

		    logger.info("[EXPORT/SQL-DX] " + stmt);

		    rs = stmt.executeQuery();
		    while(rs.next()){
		      VisorOrdenesForm v = new VisorOrdenesForm();
		      v.setEstatusOrden(Utils.noNulo(rs.getString(1)));
		      v.setNombreXML(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(2))));
		      v.setNombrePDF(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(3))));
		      v.setTipoOrden(Utils.noNulo(rs.getString(4)));
		      v.setClaveProveedor(rs.getInt(5));
		      v.setTieneComplementoXML(Utils.noNuloNormal(rs.getString(6)));
		      v.setTieneComplementoPDF(Utils.noNuloNormal(rs.getString(7)));
		      v.setTieneNotaCreditoXML(Utils.noNuloNormal(rs.getString(8)));
		      v.setTieneNotaCreditoPDF(Utils.noNuloNormal(rs.getString(9)));
		      lista.add(v);
		    }
		  }catch(Exception e){
		    Utils.imprimeLog("detalleFacturas(): ", e);
		  }finally{
		    try{ if (rs!=null) rs.close(); }catch(Exception ignore){}
		    try{ if (stmt!=null) stmt.close(); }catch(Exception ignore){}
		  }
		  return lista;
		}



	
	
	
	/*
	public ArrayList<VisorOrdenesForm> detalleLayOut(Connection con, String esquema, String tipoMoneda, String estatusOrden,  
			long folioEmpresa, String rfcProveedor, String razonSocial, String uuid, String serieFolio, String fechaInicial, String fechaFinal, String foliosEliminar)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        VisorOrdenesForm visorForm = new VisorOrdenesForm();
        ArrayList<VisorOrdenesForm> listaDetalle = new ArrayList<>();
        String [] foliosEmpresaCadena = null;
        try {
        	
        	
        	if ("".equalsIgnoreCase(foliosEliminar)) {
				StringBuffer sbQuery = new StringBuffer(VisorExportarQuerys.getDetalleLayOutFiltros(esquema));
        		
        		if (folioEmpresa > 0) {
    				sbQuery.append(" where A.FOLIO_EMPRESA = ? ");
    			}
    			
    			if (!"".equals(Utils.noNulo(fechaInicial)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.FECHAULTMOV between ? and ?");
    			}else if (!"".equals(Utils.noNulo(fechaInicial))){
    				sbQuery.append(" and A.FECHAULTMOV between ? and ?");
    			}
    			
    			if (!"".equals(Utils.noNulo(tipoMoneda)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.TIPO_MONEDA = ?");
    			}else if (!"".equals(Utils.noNulo(tipoMoneda))){
    				sbQuery.append(" and A.TIPO_MONEDA = ?");
    			}
    			
    			
    			if (!"".equals(Utils.noNulo(estatusOrden)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.ESTATUS_PAGO = ?");
    			}else if (!"".equals(Utils.noNulo(estatusOrden))){
    				sbQuery.append(" and A.ESTATUS_PAGO = ?");
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(rfcProveedor)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where B.RFC =  ?");
    			}else if (!"".equalsIgnoreCase(Utils.noNulo(rfcProveedor))) {
    				sbQuery.append(" and B.RFC =  ?");
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(razonSocial)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where B.RAZON_SOCIAL like '%").append(razonSocial).append("%'");
    			}else if (!"".equalsIgnoreCase(Utils.noNulo(razonSocial))) {
    				sbQuery.append(" and B.RAZON_SOCIAL like '%").append(razonSocial).append("%'");
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(uuid)) && sbQuery.indexOf("where ") == -1) {
    				sbQuery.append(" where A.UUID =  ?");
    			}else if (!"".equalsIgnoreCase(Utils.noNulo(uuid))) {
    				sbQuery.append(" and A.UUID =  ?");
    			}
    			
    			if (serieFolio.startsWith("%") && serieFolio.endsWith("%")){
    				if (sbQuery.indexOf("where ") == -1){
    					sbQuery.append(" where A.SERIE like  '"+serieFolio+"' ");	
    				}else {
    					sbQuery.append(" and A.SERIE like  '"+serieFolio+"' ");
    				}
    				
            	}else if (serieFolio.startsWith("%")){
            		if (sbQuery.indexOf("where ") == -1){
            			sbQuery.append(" where A.SERIE like '"+serieFolio+"%' ");	
            		}else {
            			sbQuery.append(" and A.SERIE like '"+serieFolio+"%' ");
            		}
            	}else if (!"".equalsIgnoreCase(serieFolio)){
            		if (sbQuery.indexOf("where ") == -1){
            			sbQuery.append(" where A.SERIE = '").append(serieFolio).append("'");	
            		}else {
            			sbQuery.append(" and A.SERIE = '").append(serieFolio).append("'");
            		}
            	}
    			
    			sbQuery.append(" order by A.FECHAULTMOV desc");
    			stmt = con.prepareStatement(sbQuery.toString());
    			
    			int numParam=1;
    			
    			if (folioEmpresa > 0) {
    				stmt.setLong(numParam++, folioEmpresa);
    			}

    			if (!"".equals(Utils.noNulo(fechaInicial))) {
    				stmt.setString(numParam++, fechaInicial + " 01:01:01");
    				stmt.setString(numParam++, fechaFinal + " 23:59:59");
    			}

    			
    			if (!"".equals(Utils.noNulo(tipoMoneda))) {
    				stmt.setString(numParam++, tipoMoneda);
    			}
    			
    			if (!"".equals(Utils.noNulo(estatusOrden))) {
    				stmt.setString(numParam++, estatusOrden);
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(rfcProveedor))) {
    				stmt.setString(numParam++, rfcProveedor);
    			}
    			
    			if (!"".equalsIgnoreCase(Utils.noNulo(uuid))) {
    				stmt.setString(numParam++, uuid);
    			}
    			
        	}else { // tiene seleccionado al menos 1 registro
        		if (foliosEliminar.indexOf(";") > -1){
                	foliosEmpresaCadena = foliosEliminar.split(";"); 
                }
            	
    			StringBuffer sbQuery = new StringBuffer(VisorExportarQuerys.getDetalleLayOut(esquema));
    			for (int x = 0; x < foliosEmpresaCadena.length; x++){
    				sbQuery.append("?,");
            	}
    			String queryFinal = sbQuery.substring(0, sbQuery.length() - 1) + ")";
    			stmt = con.prepareStatement(queryFinal);
    			
    			int numParam=1;
    			for (int x = 0; x < foliosEmpresaCadena.length; x++){
            		stmt.setLong(numParam++, Long.parseLong(foliosEmpresaCadena[x]));
            	}
    			
        	}
			// logger.info("stmtLayOut===>"+stmt);
            rs = stmt.executeQuery();
            
            String estatusOrdenRS = null;
            int isConsecutivo = 0;
            int isDescarte = 0;
			while(rs.next()) {
				estatusOrdenRS = Utils.noNulo(rs.getString(2));
				isConsecutivo = Utils.noNuloINT(rs.getString(17));
				isDescarte = Utils.noNuloINT(rs.getString(18));
				// logger.info("isConsecutivo===>"+isConsecutivo+", isDescarte===>"+isDescarte);
				if (isConsecutivo == 0 && !"A9".equalsIgnoreCase(estatusOrdenRS) && !"A10".equalsIgnoreCase(estatusOrdenRS) && !"A11".equalsIgnoreCase(estatusOrdenRS)) { // si no es consecutivo, entonces se agrega al layOut
					if (isDescarte == 0) { // si esta en la lista de descartes no se considera...
							visorForm.setFolioEmpresa(rs.getLong(1));
							visorForm.setEstatusOrden(estatusOrdenRS);
							visorForm.setMonto(Utils.noNulo(rs.getString(3)));
							visorForm.setTipoMoneda(Utils.noNulo(rs.getString(4)));
							visorForm.setUuid(Utils.noNulo(rs.getString(5)));
							visorForm.setSerieFolio(Utils.noNulo(rs.getString(6)));
							visorForm.setTotal(Utils.noNulo(rs.getString(7)));
							visorForm.setSubTotal(Utils.noNulo(rs.getString(8)));
							visorForm.setIva(Utils.noNulo(rs.getString(9)));
							visorForm.setIvaRet(Utils.noNulo(rs.getString(10)));
							visorForm.setIsrRet(Utils.noNulo(rs.getString(11)));
							visorForm.setFechaFactura(Utils.noNulo(rs.getString(12)));
							visorForm.setTipoProveedor(Utils.noNulo(rs.getString(13)));
							visorForm.setPagoDolares(Utils.noNulo(rs.getString(14)));
							visorForm.setConServicio(Utils.noNulo(rs.getString(15)));
							visorForm.setTipoOrden(Utils.noNulo(rs.getString(16)));
							listaDetalle.add(visorForm);
							visorForm = new VisorOrdenesForm();
							
					}
				}
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
        return listaDetalle;
    } */
	
	public ArrayList<VisorOrdenesForm> detalleLayOut(
		    Connection con, String esquema,
		    // ===== TEXTO/SELECTS =====
		    String razonSocial, String rsOp,
		    String ocOp, String ocV1, String ocV2,
		    String descripcion, String descOp,
		    String tipoMoneda, String monedaOp,
		    String servicioRecibo, String reciboOp,
		    String estatusPago, String estatusPagoOp,
		    String serieFolio, String serieFolioOp,
		    String asignarA, String asignarOp,
		    String estadoCfdi, String estadoCfdiOp,
		    String estatusSat, String estatusSatOp,
		    String usoCfdi, String usoCfdiOp,
		    String cps, String cpsOp,
		    // ===== NUM =====
		    String montoOp, String montoV1, String montoV2,
		    String totalOp, String totalV1, String totalV2,
		    String subtotalOp, String subtotalV1, String subtotalV2,
		    String ivaOp, String ivaV1, String ivaV2,
		    String ivaretOp, String ivaretV1, String ivaretV2,
		    String isrretOp, String isrretV1, String isrretV2,
		    String implocOp, String implocV1, String implocV2,
		    String totalncOp, String totalncV1, String totalncV2,
		    String pagotOp, String pagotV1, String pagotV2,
		    String ivaretncOp, String ivaretncV1, String ivaretncV2,
		    // ===== FECHAS =====
		    String fechapagoOp, String fechapagoV1, String fechapagoV2,
		    String ultmovOp, String ultmovV1, String ultmovV2,
		    // selección explícita
		    String foliosExportar
		){
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  ArrayList<VisorOrdenesForm> lista = new ArrayList<>();
		  try{
		    // Base SELECT del layout (orden de columnas que tu CSV espera)
		    StringBuilder sb = new StringBuilder(VisorExportarQuerys.getDetalleLayOutFiltros(esquema));

		    // Asegura WHERE 1=1
		    String lower = sb.toString().toLowerCase();
		    if (!lower.contains(" where ")) sb.append(" WHERE 1=1 ");

		    java.util.List<Object> params = new java.util.ArrayList<>();

		    if (isNotBlank(foliosExportar)){
		      String[] items = foliosExportar.split(";");
		      sb.append(" AND A.FOLIO_EMPRESA IN ("); // <-- si tu base usa FOLIO_ORDEN, cámbialo aquí
		      for (int i=0;i<items.length;i++){
		        sb.append(i==0 ? "?" : ",?");
		        params.add(items[i].trim());
		      }
		      sb.append(")");
		    }else{
		      FiltrosVisorOrdenesFiltros.Where w = new FiltrosVisorOrdenesFiltros.Where(){
		        @Override public void and(String frag, Object... vals){
		          if (frag==null || frag.isEmpty()) return;
		          sb.append(" AND ").append(frag);
		          if (vals!=null) for(Object v: vals) if (v!=null) params.add(v);
		        }
		      };
		      FiltrosVisorOrdenesFiltros.aplicarFiltrosVisor(
		        w,
		        // TEXTO/SELECTS
		        razonSocial, rsOp,
		        // OC
		        ocOp, ocV1, ocV2,
		        // resto
		        descripcion,  descOp,
		        tipoMoneda,   monedaOp,
		        servicioRecibo, reciboOp,
		        estatusPago,  estatusPagoOp,
		        serieFolio,   serieFolioOp,
		        asignarA,     asignarOp,
		        estadoCfdi,   estadoCfdiOp,
		        estatusSat,   estatusSatOp,
		        usoCfdi,      usoCfdiOp,
		        cps,          cpsOp,
		        // NUM
		        montoOp,     montoV1,     montoV2,
		        totalOp,     totalV1,     totalV2,
		        subtotalOp,  subtotalV1,  subtotalV2,
		        ivaOp,       ivaV1,       ivaV2,
		        ivaretOp,    ivaretV1,    ivaretV2,
		        isrretOp,    isrretV1,    isrretV2,
		        implocOp,    implocV1,    implocV2,
		        totalncOp,   totalncV1,   totalncV2,
		        pagotOp,     pagotV1,     pagotV2,
		        ivaretncOp,  ivaretncV1,  ivaretncV2,
		        // FECHAS
		        fechapagoOp, fechapagoV1, fechapagoV2,
		        ultmovOp,    ultmovV1,    ultmovV2
		      );
		    }

		    sb.append(" ORDER BY A.FECHAULTMOV DESC ");

		    stmt = con.prepareStatement(sb.toString());
		    int i=1; for(Object p: params){ stmt.setString(i++, String.valueOf(p)); }
		    logger.info("[EXPORT/LAYOUT/SQL-DX] " + stmt);

		    rs = stmt.executeQuery();
		    while(rs.next()){
		      VisorOrdenesForm v = new VisorOrdenesForm();
		      // El SELECT de getDetalleLayOutFiltros(esquema) ya trae:
		      // 1 folioEmpresa, 2 estatus, 3 monto, 4 tipoMoneda, 5 uuid, 6 serieFolio,
		      // 7 total, 8 subtotal, 9 iva, 10 ivaret, 11 isrret, 12 fechaFactura,
		      // 13 tipoProveedor, 14 pagoDolares, 15 conServicio, 16 tipoOrden,
		      // 17 isConsecutivo, 18 isDescarte (estos dos últimos no se copian al form).
		      v.setFolioEmpresa(rs.getLong(1));
		      v.setEstatusOrden(Utils.noNulo(rs.getString(2)));
		      v.setMonto(Utils.noNulo(rs.getString(3)));
		      v.setTipoMoneda(Utils.noNulo(rs.getString(4)));
		      v.setUuid(Utils.noNulo(rs.getString(5)));
		      v.setSerieFolio(Utils.noNulo(rs.getString(6)));
		      v.setTotal(Utils.noNulo(rs.getString(7)));
		      v.setSubTotal(Utils.noNulo(rs.getString(8)));
		      v.setIva(Utils.noNulo(rs.getString(9)));
		      v.setIvaRet(Utils.noNulo(rs.getString(10)));
		      v.setIsrRet(Utils.noNulo(rs.getString(11)));
		      v.setFechaFactura(Utils.noNulo(rs.getString(12)));
		      v.setTipoProveedor(Utils.noNulo(rs.getString(13)));
		      v.setPagoDolares(Utils.noNulo(rs.getString(14)));
		      v.setConServicio(Utils.noNulo(rs.getString(15)));
		      v.setTipoOrden(Utils.noNulo(rs.getString(16)));

		      // Filtrado por consecutivo/descartes lo haces en el Action
		      // (o aquí si prefieres), pero dejamos el record completo.
		      lista.add(v);
		    }
		  }catch(Exception e){
		    Utils.imprimeLog("detalleLayOutDX(): ", e);
		  }finally{
		    try{ if (rs!=null) rs.close(); }catch(Exception ignore){}
		    try{ if (stmt!=null) stmt.close(); }catch(Exception ignore){}
		  }
		  return lista;
		}

		private static boolean isNotBlank(String s){
		  return s != null && !s.trim().isEmpty();
		}

	
	
}
