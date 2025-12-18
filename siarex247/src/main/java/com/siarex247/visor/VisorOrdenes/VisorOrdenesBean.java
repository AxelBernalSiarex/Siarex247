package com.siarex247.visor.VisorOrdenes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.catalogos.Empleados.EmpleadosBean;
import com.siarex247.catalogos.Empleados.EmpleadosForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsSiarex;

public class VisorOrdenesBean extends FiltrosVisorOrdenesFiltros {

	public static final Logger logger = Logger.getLogger("siarex247");
	private DecimalFormat decimal = new DecimalFormat("###,###.##");
	// private DecimalFormat decimalEntero = new DecimalFormat("###,###");

	public ArrayList<VisorOrdenesForm> detalleOrdenes(
		    Connection con, String esquema, String lenguaje,
		    boolean isProveedor, boolean isSoloConsulta, int claveProveedor,
		    int startPaginado, int endPaginado, boolean isExcel,

		    // ===== TEXTO / SELECTS (valor, operador) =====
		    String razonSocial,   String rsOp,

		    // ===== ORDEN DE COMPRA (numérico: operador, v1, v2) =====
		    String ocOp, String ocV1, String ocV2,

		    // ===== resto TEXTO / SELECTS =====
		    String descripcion,   String descOp,
		    String tipoMoneda,    String monedaOp,
		    String servicioRecibo,String reciboOp,
		    String estatusPago,   String estatusPagoOp,
		    String serieFolio,    String serieFolioOp,
		    String asignarA,      String asignarOp,
		    String estadoCfdi,    String estadoCfdiOp,
		    String estatusSat,    String estatusSatOp,
		    String usoCfdi,       String usoCfdiOp,
		    String cps,           String cpsOp,

		    // ===== NUMÉRICOS (operador, v1, v2) =====
		    String montoOp,     String montoV1,     String montoV2,
		    String totalOp,     String totalV1,     String totalV2,
		    String subtotalOp,  String subtotalV1,  String subtotalV2,
		    String ivaOp,       String ivaV1,       String ivaV2,
		    String ivaretOp,    String ivaretV1,    String ivaretV2,
		    String isrretOp,    String isrretV1,    String isrretV2,
		    String implocOp,    String implocV1,    String implocV2,
		    String totalncOp,   String totalncV1,   String totalncV2,
		    String pagotOp,     String pagotV1,     String pagotV2,
		    String ivaretncOp,  String ivaretncV1,  String ivaretncV2,

		    // ===== FECHAS (operador, v1, v2) =====
		    String fechapagoOp, String fechapagoV1, String fechapagoV2,
		    String ultmovOp,    String ultmovV1,    String ultmovV2
		){
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  ArrayList<VisorOrdenesForm> lista = new ArrayList<>();
		  java.text.DecimalFormat decimal = new java.text.DecimalFormat("###,###.##");

		  try{
		    StringBuilder sb = new StringBuilder(VisorOrdenesQuerys.getDetalleOrdenes(esquema));
		    // Siempre arranca con WHERE 1=1 para simplificar
		    if (sb.indexOf("where 1=1") == -1) sb.append(" WHERE 1=1 ");

		    java.util.List<Object> params = new java.util.ArrayList<>();

		    // Filtro fijo por proveedor (cuando aplica)
		    if (claveProveedor > 0){
		      sb.append(" and A.CLAVE_PROVEEDOR = ? ");
		      params.add(claveProveedor);
		    }

		    // Builder de WHERE parametrizado
		    FiltrosVisorOrdenesFiltros.Where w = new FiltrosVisorOrdenesFiltros.Where(){
		      @Override public void and(String frag, Object... vals){
		        if (frag == null || frag.isEmpty()) return;
		        sb.append(" and ").append(frag);
		        if (vals != null) for(Object v: vals) if (v != null) params.add(v);
		      }
		    };

		    // Aplicar filtros (¡ojo al orden!)
		    FiltrosVisorOrdenesFiltros.aplicarFiltrosVisor(
		            w,

		            // TEXTO
		            razonSocial, rsOp,

		            // ✅ OC numérico
		            ocOp, ocV1, ocV2,

		            // resto TEXTO / SELECTS
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

		    sb.append(" order by A.FECHAULTMOV DESC ");
		    if (!isExcel){
		      sb.append(" LIMIT ").append(startPaginado).append(", ").append(endPaginado);
		    }

		    stmt = con.prepareStatement(sb.toString());
		    int idx = 1;
		    for(Object p: params){ stmt.setString(idx++, String.valueOf(p)); }

		    logger.info("[VISOR/SQL-DX] " + stmt);
		    rs = stmt.executeQuery();

		    EmpleadosBean empleadosBean = new EmpleadosBean();
		    java.util.HashMap<String, EmpleadosForm> mapaEmpleados = empleadosBean.obteneEmpleados(con, esquema);

		    while(rs.next()){
		      VisorOrdenesForm f = new VisorOrdenesForm();

		      f.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(1))));
		      f.setFolioOrden(rs.getLong(2));
		      f.setFolioEmpresa(rs.getLong(3));

		      String descripcionRS = Utils.noNulo(rs.getString(4)).replaceAll("&;", "");
		      f.setDescripcion(descripcionRS);

		      f.setTipoMoneda(Utils.noNulo(rs.getString(5)));
		      f.setMonto(decimal.format(rs.getDouble(6)));
		      f.setServicioRecibido(desServicioProducto(Utils.noNulo(rs.getString(7))));
		      f.setDesEstatus(Utils.noNulo(rs.getString(8)) + " - " + UtilsSiarex.desEstatus(Utils.noNulo(rs.getString(8)), lenguaje));
		      f.setSerieFolio(Utils.noNulo(rs.getString(9)));

		      f.setTotal(decimal.format(rs.getDouble(10)));
		      f.setSubTotal(decimal.format(rs.getDouble(11)));
		      f.setIva(decimal.format(rs.getDouble(12)));
		      f.setIvaRet(decimal.format(rs.getDouble(13)));
		      f.setIsrRet(decimal.format(rs.getDouble(14)));
		      f.setImpLocales(decimal.format(rs.getDouble(15)));

		      // Archivos (XML/PDF y complementos/NC/CP)
		      f.setTieneXML(tieneArchivo(Utils.noNulo(rs.getString(16))));
		      f.setTienePDF(tieneArchivo(Utils.noNulo(rs.getString(17))));
		      f.setTieneComplementoXML(tieneArchivo(Utils.noNulo(rs.getString(18))));
		      f.setTieneComplementoPDF(tieneArchivo(Utils.noNulo(rs.getString(19))));
		      f.setTieneCartaPorteXML(tieneArchivo(Utils.noNulo(rs.getString(20))));
		      f.setTieneCartaPortePDF(tieneArchivo(Utils.noNulo(rs.getString(21))));
		      f.setTieneNotaCreditoXML(tieneArchivo(Utils.noNulo(rs.getString(22))));
		      f.setTieneNotaCreditoPDF(tieneArchivo(Utils.noNulo(rs.getString(23))));

		      
		      double totalOrden = rs.getDouble(10);
		      double totalNC = Utils.noNuloDouble(rs.getDouble(24));
		      double restaNC = totalOrden - totalNC;

		      if (totalNC == 0){
		        f.setTotalNC("");
		        f.setPagoTotal("");
		        f.setIvaRetNC("");
		      }else{
		        f.setPagoTotal(decimal.format(restaNC));
		        f.setIvaRetNC(decimal.format(rs.getDouble(27)));
		        f.setTotalNC(decimal.format(totalNC));
		      }

		      String fechaPago = Utils.noNulo(rs.getString(28));
		      f.setFechaPago("".equals(fechaPago) ? "" : UtilsFechas.getFechayyyyMMdd(rs.getDate(28)));

		      String asignarTo = Utils.noNulo(rs.getString(29));
		      if (asignarTo == null || "".equals(asignarTo)){
		        f.setAsignarA("");
		      }else if (asignarTo.startsWith("C_")){
		        f.setAsignarA(asignarTo.substring(2));
		      }else if (asignarTo.startsWith("E_")){
		        String claveEmpleado = asignarTo.substring(2);
		        EmpleadosForm emp = mapaEmpleados.get(claveEmpleado);
		        f.setAsignarA(emp == null ? "" : (emp.getIdEmpleado() + " : " + emp.getNombreCompleto()));
		      }else{
		        f.setAsignarA("");
		      }

		      f.setFechaUltimoMovimiento(Utils.noNulo(rs.getString(30)));
		      f.setEstadoCFDI(Utils.noNulo(rs.getString(31)));
		      f.setEstatusCFDI(Utils.noNulo(rs.getString(32)));
		      f.setUsoCFDI(Utils.noNulo(rs.getString(33)));
		      f.setClaveProducto(Utils.noNulo(rs.getString(34)));

		      // cols 35..38 fechas/flags varias (si las mapeas)
		      f.setOmitirComplemento(Utils.noNulo(rs.getString(38)));

		      // 39, 40 tipo confirmación / proveedor (si necesitas)
		      f.setClaveProveedor(rs.getInt(41));

		      f.setUuidFactura(Utils.noNuloNormal(rs.getString(42)));
		      f.setUuidComplemento(Utils.noNuloNormal(rs.getString(43)));
		      f.setUuidNotaCredito(Utils.noNuloNormal(rs.getString(44)));

		      f.setProveedor(isProveedor);
		      f.setSoloConsulta(isSoloConsulta);

		      lista.add(f);
		    }

		  }catch(Exception e){
		    Utils.imprimeLog("detalleOrdenes(DX): ", e);
		  }finally{
		    try{ if (rs!=null) rs.close(); }catch(Exception ignore){}
		    try{ if (stmt!=null) stmt.close(); }catch(Exception ignore){}
		  }
		  return lista;
		}
	
	public int totalRegistros(
		    Connection con, String esquema,
		    boolean isProveedor, boolean isSoloConsulta, int claveProveedor,

		    // ===== TEXTO / SELECTS =====
		    String razonSocial,   String rsOp,

		    // ===== ORDEN DE COMPRA (numérico) =====
		    String ocOp, String ocV1, String ocV2,

		    // ===== resto TEXTO / SELECTS =====
		    String descripcion,   String descOp,
		    String tipoMoneda,    String monedaOp,
		    String servicioRecibo,String reciboOp,
		    String estatusPago,   String estatusPagoOp,
		    String serieFolio,    String serieFolioOp,
		    String asignarA,      String asignarOp,
		    String estadoCfdi,    String estadoCfdiOp,
		    String estatusSat,    String estatusSatOp,
		    String usoCfdi,       String usoCfdiOp,
		    String cps,           String cpsOp,

		    // ===== NUMÉRICOS =====
		    String montoOp,     String montoV1,     String montoV2,
		    String totalOp,     String totalV1,     String totalV2,
		    String subtotalOp,  String subtotalV1,  String subtotalV2,
		    String ivaOp,       String ivaV1,       String ivaV2,
		    String ivaretOp,    String ivaretV1,    String ivaretV2,
		    String isrretOp,    String isrretV1,    String isrretV2,
		    String implocOp,    String implocV1,    String implocV2,
		    String totalncOp,   String totalncV1,   String totalncV2,
		    String pagotOp,     String pagotV1,     String pagotV2,
		    String ivaretncOp,  String ivaretncV1,  String ivaretncV2,

		    // ===== FECHAS =====
		    String fechapagoOp, String fechapagoV1, String fechapagoV2,
		    String ultmovOp,    String ultmovV1,    String ultmovV2
		) {
		  PreparedStatement stmt = null;
		  ResultSet rs = null;
		  int total = 0;

		  try{
		    StringBuilder inner = new StringBuilder(VisorOrdenesQuerys.getTotalRegistro(esquema));
		    if (inner.indexOf("WHERE 1=1") == -1) inner.append(" WHERE 1=1 ");

		    java.util.List<Object> params = new java.util.ArrayList<>();

		    if (claveProveedor > 0){
		      inner.append(" AND A.CLAVE_PROVEEDOR = ? ");
		      params.add(claveProveedor);
		    }

		    FiltrosVisorOrdenesFiltros.Where w = new FiltrosVisorOrdenesFiltros.Where(){
		      @Override public void and(String frag, Object... vals){
		        if (frag == null || frag.isEmpty()) return;
		        inner.append(" AND ").append(frag);
		        if (vals != null) for(Object v: vals) if (v != null) params.add(v);
		      }
		    };

		    FiltrosVisorOrdenesFiltros.aplicarFiltrosVisor(
		            w,
		            razonSocial, rsOp,
		            ocOp, ocV1, ocV2,
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
		            fechapagoOp, fechapagoV1, fechapagoV2,
		            ultmovOp,    ultmovV1,    ultmovV2
		        );

		   // StringBuilder sb = new StringBuilder();
		   // sb.append("SELECT COUNT(1) FROM (").append(inner).append(") T");

		    stmt = con.prepareStatement(inner.toString());
		    int idx = 1; for(Object p: params){ stmt.setString(idx++, String.valueOf(p)); }

		    logger.info("[VISOR/TOTAL-DX] " + stmt);
		    rs = stmt.executeQuery();
		    if (rs.next()) total = rs.getInt(1);

		  }catch(Exception e){
		    Utils.imprimeLog("totalRegistros(DX): ", e);
		  }finally{
		    try{ if (rs!=null) rs.close(); }catch(Exception ignore){}
		    try{ if (stmt!=null) stmt.close(); }catch(Exception ignore){}
		  }
		  return total;
		}


	
	

	
	
	public ArrayList<VisorOrdenesForm> listadoOrdenMultiple (Connection con, String esquema, String tipoOrden){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<VisorOrdenesForm> listaDetalle = new ArrayList<>();
		VisorOrdenesForm visorForm = new VisorOrdenesForm();
		try {
			
			stmt = con.prepareStatement(VisorOrdenesQuerys.getListadoMultiple(esquema));
			stmt.setString(1, tipoOrden);
			rs  = stmt.executeQuery();
			EmpleadosBean empleadosBean = new EmpleadosBean();
            HashMap<String, EmpleadosForm> mapaEmpleados = empleadosBean.obteneEmpleados(con, esquema);
            EmpleadosForm empleadosForm = null;
            String asignarTo = null;
            
			while (rs.next()) {
				visorForm.setFolioOrden(rs.getLong(1));
				visorForm.setFolioEmpresa(rs.getLong(2));
				visorForm.setDescripcion(Utils.noNuloNormal(rs.getString(3)));
				visorForm.setTipoMoneda(Utils.noNulo(rs.getString(4)));
				visorForm.setMonto(rs.getString(5));
				visorForm.setServicioRecibido(Utils.noNulo(rs.getString(6)));
				visorForm.setEstatusOrden(Utils.noNulo(rs.getString(7)));
				visorForm.setSerieFolio(Utils.noNuloNormal(rs.getString(8)));
				visorForm.setTotal(rs.getString(9));
				visorForm.setSubTotal(rs.getString(10));
				visorForm.setIva(rs.getString(11));
				visorForm.setIvaRet(rs.getString(12));
				visorForm.setIsrRet(rs.getString(13));
				visorForm.setImpLocales(rs.getString(14));
				visorForm.setNombreXML(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(15))));
				visorForm.setNombrePDF(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(16))));
				visorForm.setClaveProveedor(rs.getInt(17));
				visorForm.setUuid(Utils.noNuloNormal(rs.getString(18)));
				String fechaPago = Utils.noNulo(rs.getString(19));
				if ("".equalsIgnoreCase(fechaPago)) {
					visorForm.setFechaPago("");	
				}else {
					visorForm.setFechaPago(UtilsFechas.getFechayyyyMMdd(rs.getDate(19)));
				}
				
				visorForm.setFechaUltimoMovimiento(Utils.noNulo(rs.getString(20)));
				asignarTo = Utils.noNulo(rs.getString(21));
				if (asignarTo == null || "".equals(asignarTo)) {
					visorForm.setAsignarA("");
				}else if(asignarTo.startsWith("C_")){ // si es centro de costos
					visorForm.setAsignarA(asignarTo.substring(2, asignarTo.length()));
				}else if (asignarTo.startsWith("E_")){ // si es un empleado
					String claveEmpleado = asignarTo.substring(2, asignarTo.length());
					empleadosForm = mapaEmpleados.get(claveEmpleado);
					if (empleadosForm == null){
						visorForm.setAsignarA("");
					}else{
						visorForm.setAsignarA(empleadosForm.getIdEmpleado());
					}
				}else {
					visorForm.setAsignarA("");
				}
				
				visorForm.setEstadoCFDI(Utils.noNulo(rs.getString(22)));
				visorForm.setEstatusCFDI(Utils.noNulo(rs.getString(23)));
				visorForm.setUsoCFDI(Utils.noNulo(rs.getString(24)));
				visorForm.setClaveProducto(Utils.noNulo(rs.getString(25)));
				visorForm.setOmitirComplemento(Utils.noNulo(rs.getString(26)));
				visorForm.setNumeroCuentaProveedor(Utils.noNulo(rs.getString(27)));
				visorForm.setCentroCostosProveedor(Utils.noNulo(rs.getString(28)));
				visorForm.setTipoOrden(Utils.noNulo(rs.getString(29)));
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
	}
	
	
	public VisorOrdenesForm consultarOrdenXfolioEmpresa (Connection con, String esquema, long folioEmpresa){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		VisorOrdenesForm visorForm = new VisorOrdenesForm();
		try {
			
			stmt = con.prepareStatement(VisorOrdenesQuerys.getConsultarOrdenFolioEmpresa(esquema));
			stmt.setLong(1, folioEmpresa);
			rs  = stmt.executeQuery();
			EmpleadosBean empleadosBean = new EmpleadosBean();
            HashMap<String, EmpleadosForm> mapaEmpleados = empleadosBean.obteneEmpleados(con, esquema);
            EmpleadosForm empleadosForm = null;
            String asignarTo = null;
            
			if (rs.next()) {
				visorForm.setFolioOrden(rs.getLong(1));
				visorForm.setFolioEmpresa(rs.getLong(2));
				visorForm.setDescripcion(Utils.noNuloNormal(rs.getString(3)));
				visorForm.setTipoMoneda(Utils.noNulo(rs.getString(4)));
				visorForm.setMonto(rs.getString(5));
				visorForm.setServicioRecibido(Utils.noNulo(rs.getString(6)));
				visorForm.setEstatusOrden(Utils.noNulo(rs.getString(7)));
				visorForm.setSerieFolio(Utils.noNuloNormal(rs.getString(8)));
				visorForm.setTotal(rs.getString(9));
				visorForm.setSubTotal(rs.getString(10));
				visorForm.setIva(rs.getString(11));
				visorForm.setIvaRet(rs.getString(12));
				visorForm.setIsrRet(rs.getString(13));
				visorForm.setImpLocales(rs.getString(14));
				visorForm.setNombreXML(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(15))));
				visorForm.setNombrePDF(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(16))));
				visorForm.setClaveProveedor(rs.getInt(17));
				visorForm.setUuid(Utils.noNuloNormal(rs.getString(18)));
				visorForm.setFechaPago(Utils.noNulo(rs.getString(19)));
				visorForm.setFechaUltimoMovimiento(Utils.noNulo(rs.getString(20)));
				asignarTo = Utils.noNulo(rs.getString(21));
				if (asignarTo == null || "".equals(asignarTo)) {
					visorForm.setAsignarA("");
				}else if(asignarTo.startsWith("C_")){ // si es centro de costos
					visorForm.setAsignarA(asignarTo.substring(2, asignarTo.length()));
				}else if (asignarTo.startsWith("E_")){ // si es un empleado
					String claveEmpleado = asignarTo.substring(2, asignarTo.length());
					empleadosForm = mapaEmpleados.get(claveEmpleado);
					if (empleadosForm == null){
						visorForm.setAsignarA("");
					}else{
						visorForm.setAsignarA(empleadosForm.getIdEmpleado());
					}
				}else {
					visorForm.setAsignarA("");
				}
				
				visorForm.setEstadoCFDI(Utils.noNulo(rs.getString(22)));
				visorForm.setEstatusCFDI(Utils.noNulo(rs.getString(23)));
				visorForm.setUsoCFDI(Utils.noNulo(rs.getString(24)));
				visorForm.setClaveProducto(Utils.noNulo(rs.getString(25)));
				visorForm.setOmitirComplemento(Utils.noNulo(rs.getString(26)));
				visorForm.setNumeroCuentaProveedor(Utils.noNulo(rs.getString(27)));
				visorForm.setCentroCostosProveedor(Utils.noNulo(rs.getString(28)));
				visorForm.setTipoOrden(Utils.noNulo(rs.getString(29)));
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
		return visorForm;
	}
	
	
	public VisorOrdenesForm consultarOrden (Connection con, String esquema, long folioOrden){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		VisorOrdenesForm visorForm = new VisorOrdenesForm();
		try {
			
			stmt = con.prepareStatement(VisorOrdenesQuerys.getConsultarOrden(esquema));
			stmt.setLong(1, folioOrden);
			rs  = stmt.executeQuery();
			EmpleadosBean empleadosBean = new EmpleadosBean();
            HashMap<String, EmpleadosForm> mapaEmpleados = empleadosBean.obteneEmpleados(con, esquema);
            EmpleadosForm empleadosForm = null;
            String asignarTo = null;
            
			if (rs.next()) {
				visorForm.setFolioOrden(rs.getLong(1));
				visorForm.setFolioEmpresa(rs.getLong(2));
				visorForm.setDescripcion(Utils.noNuloNormal(rs.getString(3)));
				visorForm.setTipoMoneda(Utils.noNulo(rs.getString(4)));
				visorForm.setMonto(String.valueOf(rs.getDouble(5)));
				visorForm.setServicioRecibido(Utils.noNulo(rs.getString(6)));
				visorForm.setEstatusOrden(Utils.noNulo(rs.getString(7)));
				visorForm.setSerieFolio(Utils.noNuloNormal(rs.getString(8)));
				visorForm.setTotal(rs.getString(9));
				visorForm.setSubTotal(rs.getString(10));
				visorForm.setIva(rs.getString(11));
				visorForm.setIvaRet(rs.getString(12));
				visorForm.setIsrRet(rs.getString(13));
				visorForm.setImpLocales(rs.getString(14));
				visorForm.setNombreXML(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(15))));
				visorForm.setNombrePDF(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(16))));
				visorForm.setClaveProveedor(rs.getInt(17));
				visorForm.setUuid(Utils.noNuloNormal(rs.getString(18)));
				String fechaPago = Utils.noNulo(rs.getString(19));
				if ("".equalsIgnoreCase(fechaPago)) {
					visorForm.setFechaPago("");	
				}else {
					visorForm.setFechaPago(UtilsFechas.getFechayyyyMMdd(rs.getDate(19)));
				}
				
				visorForm.setFechaUltimoMovimiento(Utils.noNulo(rs.getString(20)));
				asignarTo = Utils.noNulo(rs.getString(21));
				if (asignarTo == null || "".equals(asignarTo)) {
					visorForm.setAsignarA("");
				}else if(asignarTo.startsWith("C_")){ // si es centro de costos
					visorForm.setAsignarA(asignarTo.substring(2, asignarTo.length()));
				}else if (asignarTo.startsWith("E_")){ // si es un empleado
					String claveEmpleado = asignarTo.substring(2, asignarTo.length());
					empleadosForm = mapaEmpleados.get(claveEmpleado);
					if (empleadosForm == null){
						visorForm.setAsignarA("");
					}else{
						visorForm.setAsignarA(empleadosForm.getIdEmpleado());
					}
				}else {
					visorForm.setAsignarA("");
				}
				
				visorForm.setEstadoCFDI(Utils.noNulo(rs.getString(22)));
				visorForm.setEstatusCFDI(Utils.noNulo(rs.getString(23)));
				visorForm.setUsoCFDI(Utils.noNulo(rs.getString(24)));
				visorForm.setClaveProducto(Utils.noNulo(rs.getString(25)));
				visorForm.setOmitirComplemento(Utils.noNulo(rs.getString(26)));
				visorForm.setNumeroCuentaProveedor(Utils.noNulo(rs.getString(27)));
				visorForm.setCentroCostosProveedor(Utils.noNulo(rs.getString(28)));
				visorForm.setTipoOrden(Utils.noNulo(rs.getString(29)));
				visorForm.setFechaFactura(Utils.noNulo(rs.getString(30)));
				visorForm.setNombreArchivo(Utils.noNuloNormal(rs.getString(31)));
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
		return visorForm;
	}
	
	public int nuevaOrden(Connection con, String esquema, long folioEmpresa, String descripcion, String tipoMoneda, 
			 double monto, int claveProveedor, String serRecibido, String asignarTO, String estatus, String centroCostosProveedor, 
			 String numeroCuenta, String usuario, String nombreArchivoPDF) {
	        PreparedStatement stmt = null;
	        int resultado =  0;
	        try{
	            stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryGrabarOrden(esquema));
	            stmt.setLong(1, folioEmpresa);
	            stmt.setString(2, Utils.noNulo(descripcion));
	            stmt.setString(3, tipoMoneda);
	            stmt.setDouble(4, monto);
	            stmt.setString(5, estatus);
	            stmt.setInt(6, claveProveedor);
	            stmt.setString(7, Utils.noNulo(serRecibido));
	            stmt.setString(8, Utils.noNulo(usuario));
	            stmt.setString(9, asignarTO);
	            stmt.setString(10, "");
	            
	            stmt.setString(11, "");
	            stmt.setString(12, "");
	            stmt.setString(13, numeroCuenta);
	            stmt.setString(14, centroCostosProveedor);
	            stmt.setString(15, nombreArchivoPDF);
	            
	            resultado = stmt.executeUpdate();
	        }catch(SQLException sql) {
	        	Utils.imprimeLog("", sql);
	        	resultado = sql.getErrorCode();
	        } catch(Exception e){
	            Utils.imprimeLog("", e);
	            resultado = 100;
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
	
	
	
	public int actualizarOrden(Connection con, String esquema, long folioOrden, String descripcion, String tipoMoneda, 
			 double monto, int claveProveedor, String serRecibido, String usuario, String asignarTO, String estatusPago, String bandElimina, String fechaPago, 
			 String tipoOrden, String centroCostosProveedor, String numeroCuenta) {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int resultado =  0;
	        try {
	            if ("0".equalsIgnoreCase(bandElimina)){
	            	if ("0".equals(tipoOrden) || "".equals(tipoOrden) ) {
	            		stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryActualizaOrdenVisor(esquema));	
	            	}else {
	            		stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryActualizaOrdenVisorMultiple(esquema));
	            	}
	            }else{
	            	if ("0".equals(tipoOrden) || "".equals(tipoOrden) ) {
	            		stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryActualizaOrdenVisorFactura(esquema));	
	            	}else {
	            		stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryActualizaOrdenVisorFacturaMultiple(esquema));
	            	}
	            }
	            
	            int contador = 1;
	        	
	            stmt.setString(contador++, Utils.noNulo(descripcion));
	            stmt.setString(contador++, tipoMoneda);
	            if ("0".equals(tipoOrden) || "".equals(tipoOrden) ) {
	            	stmt.setDouble(contador++, monto);
		            stmt.setInt(contador++, claveProveedor);
	            }
	            
	            stmt.setString(contador++, Utils.noNulo(serRecibido));
	            stmt.setString(contador++, usuario);
	            stmt.setString(contador++, asignarTO);
	            stmt.setString(contador++, estatusPago);
	            stmt.setString(contador++, fechaPago);
	            stmt.setString(contador++, numeroCuenta);
	            stmt.setString(contador++, centroCostosProveedor);
	            
	            // stmt.setString(contador++, OMITIR_COMPLEMENTO);
	            if ("0".equals(tipoOrden) || "".equals(tipoOrden) ) {
	            	stmt.setLong(contador++, folioOrden);	
	            }else {
	            	stmt.setString(contador++, tipoOrden);
	            }
	            logger.info("stmt--->"+stmt);
	            resultado = stmt.executeUpdate();
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
	        return resultado;
	    }	 
	
	
	
	 public int actualizarOrdenOmitirComplemento(Connection con, String esquema, long folioOrden, String OMITIR_COMPLEMENTO, String tipoOrden, String usuarioHTTP){
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int cant =  0;
	        try
	        {
         	if ("0".equals(tipoOrden) || "".equals(tipoOrden) ) {
         		stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryActualizaOrdenVisorOmitir(esquema));	
         	}else {
         		stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryActualizaOrdenVisorMultipleOmitir(esquema));
         	}
	            int contador = 1;
	            stmt.setString(contador++, usuarioHTTP);
	            stmt.setString(contador++, OMITIR_COMPLEMENTO);
	            if ("0".equals(tipoOrden) || "".equals(tipoOrden) ) {
	            	stmt.setLong(contador++, folioOrden);
	            }else {
	            	stmt.setString(contador++, tipoOrden);
	            }
	            logger.info("stmt--->"+stmt);
	            cant = stmt.executeUpdate();
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
	        return cant;
	    }
	 
	 
	 public int actualizarFechaPagoRecibida(Connection con, String esquema, long folioOrden, String usuario, String fechaReciboPago, String tipoOrden)
	    {
	        PreparedStatement stmUpdate = null;
	        PreparedStatement stmUpdateMultiple = null;
	        
	        int cant =  0;
	        try{
	        	
        		if (tipoOrden == null || "".equals(tipoOrden)) {
    	        	stmUpdate = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrdenPagadaReciboVisor(esquema));
    	        	stmUpdate.setString(1, usuario);
    	        	stmUpdate.setString(2, fechaReciboPago);
        			stmUpdate.setLong(3, folioOrden);
        			logger.info("stmUpdate----->"+stmUpdate);
        			cant = stmUpdate.executeUpdate();
        			
        		}else {
    	        	stmUpdateMultiple = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrdenPagadaReciboVisorMultiple(esquema));
    	        	stmUpdateMultiple.setString(1, usuario);
    	        	stmUpdateMultiple.setString(2, fechaReciboPago);
    	        	
        			stmUpdateMultiple.setString(3, tipoOrden);
        			logger.info("stmUpdateMultiple----->"+stmUpdateMultiple);
        			cant = stmUpdateMultiple.executeUpdate();
        		}
	        	
	        }catch(Exception e){
	            Utils.imprimeLog("", e);
	        }finally{
		        try{
		            if(stmUpdate != null)
		            	stmUpdate.close();
		            stmUpdate = null;
		            if(stmUpdateMultiple != null)
		            	stmUpdateMultiple.close();
		            stmUpdateMultiple = null;
		            
		        }catch(Exception e){
		        	stmUpdate = null;
		        	stmUpdateMultiple = null;
		        }
	        }
	        return cant;
	    }
	 
	
	 public int actualizarOrdenPagada(Connection con, String esquema, long folioSistema, String usuario, String tipoOrden, String fechaPago)
	    {
	        PreparedStatement stmUpdate = null;
	        ResultSet rs = null;
	        int cant =  0;
	        try{
	        	if ("0".equals(tipoOrden) || "".equals(tipoOrden) ) {
	        		stmUpdate = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrdenPagadaVisor(esquema));	
	        	}else {
	        		stmUpdate = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrdenPagadaVisorMultiple(esquema));
	        	}
	        	
	        	stmUpdate.setString(1, "A4");
	        	stmUpdate.setString(2, usuario);
	        	stmUpdate.setString(3, fechaPago);
	        	stmUpdate.setString(4, fechaPago);
	        	
	        	if ("0".equals(tipoOrden) || "".equals(tipoOrden) ) {
	        		stmUpdate.setLong(5, folioSistema);	
	        	}else {
	        		stmUpdate.setString(5, tipoOrden);
	        	}
	        	
	        	
	            cant = stmUpdate.executeUpdate();
	        }catch(Exception e){
	            Utils.imprimeLog("", e);
	        }finally{
		        try{
		            if(rs != null)
		                rs.close();
		            rs = null;
		            if(stmUpdate != null)
		            	stmUpdate.close();
		            stmUpdate = null;
		        }catch(Exception e){
		        	stmUpdate = null;
		        }
	        }
	        return cant;
	    }
	 

	 public int eliminaOrden(Connection con, String esquema, String foliosEliminar)
	    {
	        PreparedStatement stmt = null;
	        int resultado = 0;
	        String [] foliosEmpresaCadena = null;
	        try {
	        	if (foliosEliminar.indexOf(";") > -1){
	            	foliosEmpresaCadena = foliosEliminar.split(";"); 
	            }
	        	
	            stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryEliminaOrden(esquema));
	            
	            boolean bandEliminar = true;
	            
	            if (foliosEmpresaCadena != null ){
	            	VisorOrdenesForm visorForm = null;
	            	for (int x = 0; x < foliosEmpresaCadena.length; x++){
	            		visorForm = consultarOrden(con, esquema, Long.parseLong(foliosEmpresaCadena[x]));
	            		if ("A5".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A2".equalsIgnoreCase(visorForm.getEstatusOrden())) {
	            			bandEliminar = true;
	            		}else {
	            			resultado = -1;
	            			bandEliminar = false;
	            			break;
	            		}
	            		
	            	}
	            	
	        	}else {
	        		bandEliminar = false;
	        	}
	            
	            if (bandEliminar) {
	            	for (int x = 0; x < foliosEmpresaCadena.length; x++){
	            		stmt.setLong(1, Long.parseLong(foliosEmpresaCadena[x]));
	            		resultado = stmt.executeUpdate();
	            	}
	            }
	            
	        }
	        catch(Exception e){
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

	 
	 
	 public int actualizarOrdenClaveProducto(Connection con, String esquema, long folioEmpresa, String tipoAccion, String estatusPago, String usuarioHTTP)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int cant =  0;	        
	        try {
	        	if (tipoAccion.equalsIgnoreCase("CORRECTA")){
	        		stmt = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrdenClaveProductoOK(esquema));
		            stmt.setString(1, estatusPago);
		            stmt.setString(2, usuarioHTTP);
		            stmt.setLong(3, folioEmpresa);	
	        	}else{
	        		stmt = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrdenClaveProductoNG(esquema));
		            stmt.setString(1, estatusPago);
		            stmt.setObject(2, null);
		            stmt.setDouble(3, 0.0);
		            stmt.setDouble(4, 0.0);
		            stmt.setDouble(5, 0.0);
		            stmt.setDouble(6, 0.0);
		            stmt.setDouble(7, 0.0);
		            stmt.setObject(8, null);
		            stmt.setObject(9, null);
		            stmt.setObject(10, null);
		            stmt.setObject(11, null);
		            stmt.setObject(12, null);
		            stmt.setObject(13, null);
		            stmt.setObject(14, null);
		            stmt.setObject(15, null);
		            stmt.setObject(16, null);
		            stmt.setString(17, usuarioHTTP);
		            stmt.setLong(18, folioEmpresa);
	        	}
	            cant = stmt.executeUpdate();
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
	        return cant;
	    }	 
	 
	 
	 
	 public int actualizarOrdenClaveProductoMultiple(Connection con, String esquema, String idMultiple, String tipoAccion, String estatusPago)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int cant =  0;	        
	        try{
	        	if (tipoAccion.equalsIgnoreCase("CORRECTA")){
	        		stmt = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrdenClaveProductoOK_Multiple(esquema));
		            stmt.setString(1, estatusPago);
		            stmt.setString(2, idMultiple);	
	        	}else{
	        		stmt = con.prepareStatement(VisorOrdenesQuerys.getActualizaOrdenClaveProductoNG_Multiple(esquema));
		            stmt.setString(1, estatusPago);
		            stmt.setObject(2, null);
		            stmt.setDouble(3, 0.0);
		            stmt.setDouble(4, 0.0);
		            stmt.setDouble(5, 0.0);
		            stmt.setDouble(6, 0.0);
		            stmt.setDouble(7, 0.0);
		            stmt.setObject(8, null);
		            stmt.setObject(9, null);
		            stmt.setObject(10, null);
		            stmt.setObject(11, null);
		            stmt.setObject(12, null);
		            stmt.setObject(13, null);
		            stmt.setObject(14, null);
		            stmt.setObject(15, null);
		            stmt.setObject(16, null);
		            stmt.setObject(17, null);			            
		            stmt.setString(18, idMultiple);
	        	}
	            cant = stmt.executeUpdate();
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
	        return cant;
	    }

	 
	 public int actualizarOrdenAmericana(Connection con, String esquema, long folioOrden, String estatusPago, String tipoAccion, String fechaPago, String usuarioHTTP)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int cant =  0;
	        try {
	        	if (tipoAccion.equalsIgnoreCase("CORRECTO")){
	        		stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryActualizaOrdenAmericanaOK(esquema));
		            stmt.setString(1, Utils.noNulo(estatusPago));
		            stmt.setString(2, usuarioHTTP);
		            stmt.setString(3, fechaPago);
		            stmt.setLong(4, folioOrden);	
	        	}else{
	        		stmt = con.prepareStatement(VisorOrdenesQuerys.getQueryactualizaOrdenAmericanaNG(esquema));
		            stmt.setString(1, Utils.noNulo(estatusPago));
		            stmt.setString(2, "");
		            stmt.setDouble(3, 0.0);
		            stmt.setString(4, "");
		            stmt.setString(5, "");
		            stmt.setString(6, usuarioHTTP);
		            stmt.setLong(7, folioOrden);
	        	}
	            cant = stmt.executeUpdate();
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
	        return cant;
	    }	 
	 

	 public String consultarFechaMinima (Connection con, String esquema){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String fechaMinima = "";
			try {
				
				stmt = con.prepareStatement(VisorOrdenesQuerys.getConsultarFechaMinima(esquema));
				rs  = stmt.executeQuery();
				if (rs.next()) {
					fechaMinima = Utils.noNulo(rs.getString(1));
				}
				if ("".equalsIgnoreCase(fechaMinima)) {
					fechaMinima = UtilsFechas.getFechayyyyMMdd();
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
			return fechaMinima;
		}
	 
	 
	private String desServicioProducto(String servicioProducto){
		if ("0".equalsIgnoreCase(servicioProducto)){
			return "NO";
		}
		if ("1".equalsIgnoreCase(servicioProducto)){
			return "SI";
		}
	   return "";	
	}
	
	private String tieneArchivo(String nombreArchivo){
		if ("".equalsIgnoreCase(nombreArchivo)){
			return "N";
		}else {
			return "S";
		}	
	}
	
}
