package com.siarex247.cumplimientoFiscal.BovedaNomina;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.Concepto;
import com.itextpdf.xmltopdf.LeerXML;
import com.itextpdf.xmltopdf.Nomina.NDeduccion;
import com.itextpdf.xmltopdf.Nomina.NJubilacionPensionRetiro;
import com.itextpdf.xmltopdf.Nomina.NOtroPago;
import com.itextpdf.xmltopdf.Nomina.NPercepcion;
import com.itextpdf.xmltopdf.Nomina.NSeparacionIndemnizacion;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.BovedaEmitidos.BovedaEmitidosQuerys;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaMasivaLocal;
import com.siarex247.cumplimientoFiscal.ExportarXML.ExportarXMLQuerys;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class BovedaNominaBean extends FiltrosBovedaNomina {

	public static final Logger logger = Logger.getLogger("siarex247");

	
	public ArrayList<BovedaNominaForm> detalleBoveda(
	        Connection con,
	        String esquema,
	        String rfc, String razonSocial, String folio, String serie,
	        String fechaInicial, String uuidBoveda, String fechaFinal,
	        int start, int length, boolean isExcel,
	        // ===== operadores/valores =====
	        // texto
	        String rfcOperator,   String razonOperator, String serieOperator, String uuidOperator,
	        // fecha
	        String dateOperator,  String dateV1,        String dateV2,
	        // numéricos
	        String folioOperator, String folioV1,       String folioV2,
	        String totalOperator, String totalV1,       String totalV2,
	        String subOperator,   String subV1,         String subV2,
	        String descOperator,  String descV1,        String descV2,
	        String percOperator,  String percV1,        String percV2,
	        String dedOperator,   String dedV1,         String dedV2
	) {

	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<BovedaNominaForm> lista = new ArrayList<>();

	    try {
	        StringBuilder sb = new StringBuilder(BovedaNominaQuerys.getDetalleBoveda(esquema));
	        List<Object> params = new ArrayList<>();

	        // Tu SELECT base espera primero el tipo de comprobante
	        params.add("N");

	        // Where que SIEMPRE agrega " AND ... " (porque la base ya tiene WHERE)
	        FiltrosBovedaNomina.Where w = new FiltrosBovedaNomina.Where(sb, params) {
	            @Override
	            public void and(String frag, Object... vals) {
	                if (frag == null || frag.isEmpty()) return;
	                sb.append(" AND ").append(frag);
	                if (vals != null) for (Object v : vals) if (v != null) params.add(v);
	            }
	        };

	        // Aplica TODOS los filtros (texto, fecha, numéricos)
	        aplicarFiltrosNomina(
	            w,
	            // texto
	            rfc,   rfcOperator,
	            razonSocial, razonOperator,
	            serie, serieOperator,
	            uuidBoveda,  uuidOperator,
	            // fecha (con respaldo a fechaInicial/fechaFinal)
	            dateOperator, dateV1, dateV2, fechaInicial, fechaFinal,
	            // numéricos
	            folio, folioOperator, folioV1, folioV2,
	            totalOperator, totalV1, totalV2,
	            subOperator,   subV1,   subV2,
	            descOperator,  descV1,  descV2,
	            percOperator,  percV1,  percV2,
	            dedOperator,   dedV1,   dedV2
	        );

	        // Orden y paginación
	        sb.append(" ORDER BY FECHA_FACTURA DESC ");
	        if (!isExcel) {
	            sb.append(" LIMIT ").append(start).append(", ").append(length);
	        }

	        stmt = con.prepareStatement(sb.toString());
	        int idx = 1;
	        for (Object p : params) {
	            if (p instanceof java.math.BigDecimal) stmt.setBigDecimal(idx++, (java.math.BigDecimal)p);
	            else if (p instanceof Integer)         stmt.setInt(idx++, (Integer)p);
	            else                                    stmt.setString(idx++, String.valueOf(p));
	        }
	        logger.info("📈 detalleBoveda(Nómina) → " + stmt);

	        rs = stmt.executeQuery();
	        java.text.DecimalFormat decimal = new java.text.DecimalFormat("###,###.##");

	        while (rs.next()) {
	            BovedaNominaForm b = new BovedaNominaForm();
	            b.setIdRegistro(rs.getInt(1));
	            b.setUuid(Utils.noNuloNormal(rs.getString(2)));
	            b.setSerie(Utils.noNulo(rs.getString(3)));
	            b.setFolio(Utils.noNulo(rs.getString(4)));
	            b.setFechaFactura(Utils.noNulo(rs.getString(5)));

	            // OJO: índices iguales a tu SELECT actual
	            b.setSubTotalDouble(rs.getDouble(8));
	            b.setSubTotal(decimal.format(rs.getDouble(8)));

	            b.setTotalDouble(rs.getDouble(9));
	            b.setTotal(decimal.format(rs.getDouble(9)));

	            b.setDescuentoDouble(rs.getDouble(10));
	            b.setDescuento(decimal.format(rs.getDouble(10)));

	            b.setTotalPercepcionesDouble(rs.getDouble(11));
	            b.setTotalPercepciones(decimal.format(rs.getDouble(11)));

	            b.setTotalDeduccionesDouble(rs.getDouble(12));
	            b.setTotalDeducciones(decimal.format(rs.getDouble(12)));

	            b.setRfcEmisor(Utils.noNulo(rs.getString(13)));
	            b.setRazonSocialEmisor(Utils.noNuloNormal(rs.getString(14)));
	            b.setRfcReceptor(Utils.noNulo(rs.getString(15)));
	            b.setRazonSocialReceptor(Utils.noNuloNormal(rs.getString(16)));
	            b.setTipoComprobante(Utils.noNulo(rs.getString(17)));
	            b.setFechaTimbrado(Utils.noNulo(rs.getString(18)));

	            lista.add(b);
	        }
	    } catch (Exception e) {
	        Utils.imprimeLog("detalleBoveda(Nómina): ", e);
	    } finally {
	        try { if (rs != null)   rs.close(); } catch (Exception ignore) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
	    }
	    return lista;
	}


	
	// ===== Versión completa (con operadores) =====
	public int totalRegistros(
	        Connection con,
	        String esquema,
	        String rfc, String razonSocial, String folio, String serie,
	        String fechaInicial, String uuidBoveda, String fechaFinal,
	        // operadores texto
	        String rfcOperator,   String razonOperator, String serieOperator, String uuidOperator,
	        // fecha
	        String dateOperator,  String dateV1,        String dateV2,
	        // numéricos
	        String folioOperator, String folioV1,       String folioV2,
	        String totalOperator, String totalV1,       String totalV2,
	        String subOperator,   String subV1,         String subV2,
	        String descOperator,  String descV1,        String descV2,
	        String percOperator,  String percV1,        String percV2,
	        String dedOperator,   String dedV1,         String dedV2
	) {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    int total = 0;

	    try {
	        // Armamos el SELECT base (el mismo de detalle) y le aplicamos filtros.
	        StringBuilder inner = new StringBuilder(BovedaNominaQuerys.getTotalRegistros(esquema));
	        List<Object> params = new ArrayList<>();
	        // Primer parámetro de la consulta base: tipo de comprobante Nómina
	        params.add("N");

	        // WHERE que SIEMPRE agrega AND (porque el base ya trae WHERE TIPO_COMPROBANTE = ?)
	        FiltrosBovedaNomina.Where w = new FiltrosBovedaNomina.Where(inner, params) {
	            @Override
	            public void and(String frag, Object... vals) {
	                if (frag == null || frag.isEmpty()) return;
	                inner.append(" AND ").append(frag);
	                if (vals != null) for (Object v : vals) if (v != null) params.add(v);
	            }
	        };

	        // Aplica filtros (texto, fecha, numéricos)
	        aplicarFiltrosNomina(
	            w,
	            // texto
	            rfc,   rfcOperator,
	            razonSocial, razonOperator,
	            serie, serieOperator,
	            uuidBoveda,  uuidOperator,
	            // fecha (con respaldo a fechaInicial/fechaFinal)
	            dateOperator, dateV1, dateV2, fechaInicial, fechaFinal,
	            // numéricos
	            folio, folioOperator, folioV1, folioV2,
	            totalOperator, totalV1, totalV2,
	            subOperator,   subV1,   subV2,
	            descOperator,  descV1,  descV2,
	            percOperator,  percV1,  percV2,
	            dedOperator,   dedV1,   dedV2
	        );

	        // Envolvemos para contar
	       // StringBuilder sb = new StringBuilder();
	       // sb.append("SELECT COUNT(1) FROM (").append(inner).append(") AS T");

	        stmt = con.prepareStatement(inner.toString());
	        int idx = 1;
	        for (Object p : params) {
	            if (p instanceof java.math.BigDecimal) stmt.setBigDecimal(idx++, (java.math.BigDecimal)p);
	            else if (p instanceof Integer)         stmt.setInt(idx++, (Integer)p);
	            else                                    stmt.setString(idx++, String.valueOf(p));
	        }
	        logger.info("📈 totalRegistros(Nómina) → " + stmt);

	        rs = stmt.executeQuery();
	        if (rs.next()) total = rs.getInt(1);

	    } catch (Exception e) {
	        Utils.imprimeLog("totalRegistros(Nómina): ", e);
	    } finally {
	        try { if (rs != null) rs.close(); }   catch (Exception ignore) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
	    }
	    return total;
	}

	
	public void procesarXmlBoveda(Connection con, String esquema, String esquemaEmpresa, List<File> listaXML, Integer arrResultado [], String usuarioHTTP, boolean bandGuardarDescarga) {
		Comprobante _comprobante = null;
		ArrayList<Comprobante> listaDetalle = new ArrayList<Comprobante>();
		
		String uuid = null;
		LocalDateTime fechaFacturaLD = null;
		LocalDate fechaPagoLD = null;
		LocalDate fechaInicialPagoLD = null;
		LocalDate fechaFinalPagoLD = null;
		String fechaFactura = null;
		String fechaPago = null;
		String fechaInicialPago = null;
		String fechaFinalPago = null;
		String fechaTimbrado = null;
		 
		LocalDateTime fechaTimbradoLD   = null;
		 
		String rutaDesXML = null;
		String rutaRepositorio = null;
		
		ArrayList<Concepto> listaConceptoXML = null;
		
		PreparedStatement stmtBoveda = null;
		PreparedStatement stmtConceptos = null;
		PreparedStatement stmtEmisor = null;
		PreparedStatement stmtReceptor = null;
		PreparedStatement stmtPercepciones=null;
		PreparedStatement stmtPercepcionesDetalle = null;
		PreparedStatement stmtDeducciones = null;
		PreparedStatement stmtDeduccionesDetalle = null;
		PreparedStatement stmtOtroPago  = null;
		PreparedStatement stmtPercepcionesSeparacion = null;
		PreparedStatement stmtPercepcionesJubilacion = null;
		try {
			
			rutaRepositorio = UtilsPATH.REPOSITORIO_DOCUMENTOS +esquemaEmpresa+"/BOVEDA_NOMINA/";
					
			stmtBoveda = con.prepareStatement(BovedaNominaQuerys.getAltaBoveda(esquema));
			stmtConceptos = con.prepareStatement(BovedaEmitidosQuerys.getAltaBovedaConceptos(esquema));
			stmtEmisor = con.prepareStatement(BovedaNominaQuerys.getAltaEmisorNomina(esquema));
			stmtReceptor = con.prepareStatement(BovedaNominaQuerys.getAltaReceptorNomina(esquema));
			stmtPercepciones = con.prepareStatement(BovedaNominaQuerys.getAltaPercepciones(esquema));
			stmtPercepcionesDetalle = con.prepareStatement(BovedaNominaQuerys.getAltaPercepcionesDetalle(esquema));
			stmtDeducciones = con.prepareStatement(BovedaNominaQuerys.getAltaDeducciones(esquema));
			stmtDeduccionesDetalle = con.prepareStatement(BovedaNominaQuerys.getAltaDeduccionesDetalle(esquema));
			stmtOtroPago = con.prepareStatement(BovedaNominaQuerys.getAltaOtroPago(esquema));
			stmtPercepcionesSeparacion = con.prepareStatement(BovedaNominaQuerys.getAltaPercepcionesSeparacion(esquema));
			stmtPercepcionesJubilacion = con.prepareStatement(BovedaNominaQuerys.getAltaPercepcionesJubilacion(esquema));

			String RFC_EMISOR = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "RFC_RECEPTOR");
			String VALOR_RFC_RECEPTOR = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "VALOR_RFC_RECEPTOR");
			
			// logger.info("*********** INICIANDO PROCESO DE NOMINA ****************");
			for (File fileHTTP : listaXML){
				try {
					try {
						// logger.info("UUID=====>"+fileHTTP.getName());
				    	_comprobante = LeerXML.ObtenerComprobante(fileHTTP.getAbsolutePath());
				    	if (_comprobante == null) {
				    		logger.info("XML Fallo====>"+fileHTTP.getAbsolutePath());
				    	}
				    }catch(Exception e) {
				    	Utils.imprimeLog("", e);
				    }
					// logger.info("Tipo de Comprobante====>"+_comprobante.getTipoDeComprobante());
				    if ("N".equalsIgnoreCase(_comprobante.getTipoDeComprobante()) || "nomina".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) { // si es nomina
				    	uuid = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
				    	listaConceptoXML = _comprobante.getConceptos().getConcepto();
				    	
						 boolean bandAlta = false;
						 if ("S".equalsIgnoreCase(RFC_EMISOR)){
							if (VALOR_RFC_RECEPTOR.equalsIgnoreCase(_comprobante.getEmisor().getRfc())) {
								 bandAlta = true;	
							}
						 }else {
							 bandAlta = true;
						 }
						 
						 if (bandAlta){
							 XMLNominaForm cargasXMLForm = new XMLNominaForm();
							 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							 DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
							 
							 
							 fechaFacturaLD = _comprobante.getFecha();
							 fechaPagoLD = _comprobante.getComplemento().getNomina().getFechaPago();
							 fechaInicialPagoLD = _comprobante.getComplemento().getNomina().getFechaInicialPago();
							 fechaFinalPagoLD = _comprobante.getComplemento().getNomina().getFechaFinalPago();
							 
							 fechaTimbradoLD   = _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado();
							 
							 fechaPago = fechaPagoLD.format(formatterDate);
							 if (fechaPago.length() > 10) {
								 fechaPago = fechaPago.substring(0, 10);
							 }
							 
							 fechaInicialPago = fechaInicialPagoLD.format(formatterDate);
							 fechaFinalPago = fechaFinalPagoLD.format(formatterDate);
							 
							 fechaTimbrado = fechaTimbradoLD.format(formatter);
							 
							 	fechaFactura = fechaFacturaLD.format(formatter);
								cargasXMLForm.setUuid(uuid);
								cargasXMLForm.setSerie(_comprobante.getSerie());
								cargasXMLForm.setFolio(_comprobante.getFolio());
								cargasXMLForm.setCertificado(_comprobante.getCertificado());
								cargasXMLForm.setCondicionesPago(_comprobante.getCondicionesDePago());
								cargasXMLForm.setTipoCambio(_comprobante.getTipoCambio());
								cargasXMLForm.setLugarExpedicion(_comprobante.getLugarExpedicion());
								
								cargasXMLForm.setFechaFactura(fechaFactura);
								cargasXMLForm.setFormaPago(_comprobante.getFormaPago());
								cargasXMLForm.setMetodoPago(_comprobante.getMetodoPago());
								cargasXMLForm.setMoneda(Utils.noNulo(_comprobante.getMoneda()));
								// cargasXMLForm.setNumeroCuentaPago(numeroCuentaPago);
								cargasXMLForm.setSubTotal(_comprobante.getSubTotal());
								cargasXMLForm.setDescuento(_comprobante.getDescuento());
								cargasXMLForm.setTotalPercepciones(_comprobante.getComplemento().getNomina().getTotalPercepciones());
								cargasXMLForm.setTotalDeducciones(_comprobante.getComplemento().getNomina().getTotalDeducciones());
								cargasXMLForm.setTotalOtrosPagos(_comprobante.getComplemento().getNomina().getTotalOtrosPagos());
								cargasXMLForm.setTotal(_comprobante.getTotal());
								cargasXMLForm.setEmisorRFC(_comprobante.getEmisor().getRfc());
								cargasXMLForm.setEmisorNombre(_comprobante.getEmisor().getNombre());
								cargasXMLForm.setReceptorRFC(_comprobante.getReceptor().getRfc());
								cargasXMLForm.setReceptorNombre(_comprobante.getReceptor().getNombre());
								cargasXMLForm.setReceptorResidencia(_comprobante.getReceptor().getResidenciaFiscal());
								cargasXMLForm.setReceptorDomicilio(_comprobante.getReceptor().getDomicilioFiscalReceptor());
								cargasXMLForm.setReceptorRegimen(_comprobante.getReceptor().getRegimenFiscalReceptor());
								cargasXMLForm.setVersionNomina(_comprobante.getComplemento().getNomina().getVersion());
								cargasXMLForm.setRfcProvCert(_comprobante.getComplemento().getTimbreFiscalDigital().getRfcProvCertif());
								cargasXMLForm.setNoCertificado(_comprobante.getNoCertificado());
								cargasXMLForm.setSello(_comprobante.getSello());
								cargasXMLForm.setNoCertificadoSAT(_comprobante.getComplemento().getTimbreFiscalDigital().getNoCertificadoSAT());
								cargasXMLForm.setSelloCFD(_comprobante.getComplemento().getTimbreFiscalDigital().getSelloCFD());
								cargasXMLForm.setSelloSAT(_comprobante.getComplemento().getTimbreFiscalDigital().getSelloSAT());
								cargasXMLForm.setTipoComprobante(_comprobante.getTipoDeComprobante());
								cargasXMLForm.setFechaPago(fechaPago);
								cargasXMLForm.setFechaInicialPago(fechaInicialPago);
								cargasXMLForm.setFechaFinalPago(fechaFinalPago);
								cargasXMLForm.setNumDiasPagados(_comprobante.getComplemento().getNomina().getNumDiasPagados());
								cargasXMLForm.setTipoNomina(_comprobante.getComplemento().getNomina().getTipoNomina());
								cargasXMLForm.setFechaTimbrado(fechaTimbrado);
							
								cargasXMLForm.setVersion(_comprobante.getVersion());
								cargasXMLForm.setCertificadoPago(_comprobante.getCertificado());
								cargasXMLForm.setEmisorRegimen(_comprobante.getEmisor().getRegimenFiscal());
								
								cargasXMLForm.setReceptorResidencia(_comprobante.getReceptor().getResidenciaFiscal());
								cargasXMLForm.setReceptorUso(_comprobante.getReceptor().getUsoCFDI());
								int res = altaBoveda(stmtBoveda, cargasXMLForm, usuarioHTTP);
								if (res == 1) {
									
									if (listaConceptoXML != null && listaConceptoXML.size() > 0) {
										altaConceptos(stmtConceptos, _comprobante);
									}
									altaEmisor(stmtEmisor, _comprobante);
									altaReceptor(stmtReceptor, _comprobante);
									altaPercepciones(stmtPercepciones, stmtPercepcionesDetalle, stmtPercepcionesSeparacion, stmtPercepcionesJubilacion, _comprobante);
									altaDeducciones(stmtDeducciones, stmtDeduccionesDetalle, _comprobante);
									altaOtrosPago(stmtOtroPago, _comprobante);
									
									arrResultado[0]++; // exitoso
									
									rutaDesXML = rutaRepositorio + uuid + ".xml";
									File fdesXML = new File(rutaDesXML);
									UtilsFile.moveFileDirectory(fileHTTP, fdesXML, true, false, true);
									
									
								}else if (res == 1062){
									arrResultado[1]++; // duplicado
								}else {
									arrResultado[3]++; // error en XML
								}
								
								if (bandGuardarDescarga && (res == 1 || res == 1062)) {
									listaDetalle.add(_comprobante);
									// DescargaMasivaLocal descargaLocal = new DescargaMasivaLocal();
									// descargaLocal.guardarMetadataTimbrado(con, esquema, listaDetalle);
								}
								
								
						 }else {
							 arrResultado[2]++;
						 }
				    }else {
				    	arrResultado[3]++;
				    }
				    
				    
				}catch(Exception e) {
					Utils.imprimeLog("", e);
					arrResultado[3]++; // error en XML
				}
				
			}
		    
			
			if (bandGuardarDescarga) {
				DescargaMasivaLocal descargaLocal = new DescargaMasivaLocal();
				descargaLocal.guardarMetadataTimbrado(con, esquema, listaDetalle);
			}
			
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmtBoveda != null)
					stmtBoveda.close();
				stmtBoveda = null;
				if (stmtConceptos != null)
					stmtConceptos.close();
				stmtConceptos = null;
				if (stmtEmisor != null)
					stmtEmisor.close();
				stmtEmisor = null;
				if (stmtReceptor != null)
					stmtReceptor.close();
				stmtReceptor = null;
				if (stmtPercepciones != null)
					stmtPercepciones.close();
				stmtPercepciones = null;
				if (stmtPercepcionesDetalle != null)
					stmtPercepcionesDetalle.close();
				stmtPercepcionesDetalle = null;
				if (stmtDeducciones != null)
					stmtDeducciones.close();
				stmtDeducciones = null;
				if (stmtDeduccionesDetalle != null)
					stmtDeduccionesDetalle.close();
				stmtDeduccionesDetalle = null;
				if (stmtOtroPago != null)
					stmtOtroPago.close();
				stmtOtroPago = null;
				if (stmtPercepcionesSeparacion != null)
					stmtPercepcionesSeparacion.close();
				stmtPercepcionesSeparacion = null;
				
				
			} catch (Exception e2) {
				stmtBoveda = null;
				stmtConceptos = null;
				stmtEmisor = null;
				stmtReceptor = null;
				stmtPercepciones = null;
				stmtPercepcionesDetalle = null;
				stmtDeducciones = null;
				stmtDeduccionesDetalle = null;
				stmtOtroPago = null;
				stmtPercepcionesSeparacion = null;
			}
		}
	}
	
	
	
	private int altaBoveda(PreparedStatement stmt, XMLNominaForm cargasXMLForm, String usuarioHTTP) {
		int resultado = 0;
		
		try {
			int numParam=1;
			
// UUID, VERSION_XML, SERIE, FOLIO, FECHA_FACTURA, MONEDA, TIPO_COMPROBANTE, METODO_PAGO, LUGAR_EXPEDICION, SUB_TOTAL, TOTAL, DESCUENTO, TOTAL_PERCEPCIONES, TOTAL_DEDUCCIONES, TOTAL_OTROS, EMISOR_RFC, EMISOR_NOMBRE, EMISOR_REGIMEN, RECEPTOR_RFC, RECEPTOR_NOMBRE, RECEPTOR_RESIDENCIA, RECEPTOR_DOMICILIO, RECEPTOR_REGIMEN_FISCAL, RECEPTOR_USO, VERSION_NOMINA, FECHA_TIMBRADO, RFC_PROVCERT, FECHA_PAGO, USUARIO_TRAN			
			stmt.setString(numParam++, cargasXMLForm.getUuid());
			stmt.setString(numParam++, cargasXMLForm.getVersion());
			stmt.setString(numParam++, cargasXMLForm.getSerie());
			stmt.setString(numParam++, cargasXMLForm.getFolio());
			stmt.setString(numParam++, cargasXMLForm.getFechaFactura());
			stmt.setString(numParam++, cargasXMLForm.getMoneda());
			stmt.setString(numParam++, cargasXMLForm.getTipoComprobante());
			stmt.setString(numParam++, cargasXMLForm.getMetodoPago());
			stmt.setString(numParam++, cargasXMLForm.getLugarExpedicion());
			stmt.setDouble(numParam++, cargasXMLForm.getSubTotal());
			stmt.setDouble(numParam++, cargasXMLForm.getTotal());
			stmt.setDouble(numParam++, cargasXMLForm.getDescuento());
			stmt.setDouble(numParam++, cargasXMLForm.getTotalPercepciones());
			stmt.setDouble(numParam++, cargasXMLForm.getTotalDeducciones());
			stmt.setDouble(numParam++, cargasXMLForm.getTotalOtrosPagos());
			stmt.setString(numParam++, cargasXMLForm.getEmisorRFC());
			stmt.setString(numParam++, cargasXMLForm.getEmisorNombre());
			stmt.setString(numParam++, cargasXMLForm.getEmisorRegimen());
			stmt.setString(numParam++, cargasXMLForm.getReceptorRFC());
			stmt.setString(numParam++, cargasXMLForm.getReceptorNombre());
			stmt.setString(numParam++, cargasXMLForm.getReceptorDomicilio());
			stmt.setString(numParam++, cargasXMLForm.getReceptorRegimen());
			stmt.setString(numParam++, cargasXMLForm.getReceptorUso());
			stmt.setString(numParam++, cargasXMLForm.getVersionNomina());
			stmt.setString(numParam++, cargasXMLForm.getFechaTimbrado());
			stmt.setString(numParam++, cargasXMLForm.getRfcProvCert());
			stmt.setString(numParam++, cargasXMLForm.getFechaPago());
			stmt.setString(numParam++, cargasXMLForm.getFechaInicialPago());
			stmt.setString(numParam++, cargasXMLForm.getFechaFinalPago());
			stmt.setDouble(numParam++, cargasXMLForm.getNumDiasPagados());
			stmt.setString(numParam++, cargasXMLForm.getTipoNomina());
			stmt.setString(numParam++, usuarioHTTP);
			// logger.info("stmt==>"+stmt);
			resultado = stmt.executeUpdate();
			
		} catch (SQLException sql) {
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);
			logger.info("resultado==>"+resultado);
		} catch (Exception e) {
			resultado = -1;
			Utils.imprimeLog("", e);
		} 

		return resultado;
	}
	
	
	private int altaConceptos(PreparedStatement stmtConcepto, Comprobante _comprobante) {
		int idConcepto = 0;
		String uuid = null;
		try {
			uuid = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			ArrayList<Concepto> listaConceptoXML = _comprobante.getConceptos().getConcepto();
			for (Concepto concepto : listaConceptoXML) {
				// conceptoXML = listaConceptoXML.get(x);
				stmtConcepto.setString(1, uuid);
				stmtConcepto.setString(2, concepto.getClaveProdServ());
				stmtConcepto.setString(3, concepto.getNoIdentificacion());
				stmtConcepto.setDouble(4, concepto.getCantidad());
				stmtConcepto.setString(5, concepto.getClaveUnidad());
				stmtConcepto.setString(6, concepto.getUnidad());
				stmtConcepto.setObject(7, new String(concepto.getDescripcion().replace("″", "").replace("́ ", "")));
				stmtConcepto.setDouble(8, concepto.getValorUnitario());
				stmtConcepto.setDouble(9, concepto.getImporte());
				stmtConcepto.setDouble(10, concepto.getDescuento());
				stmtConcepto.setString(11, concepto.getObjetoImp());
				stmtConcepto.executeUpdate();
				// Utils.noNuloNormal(concepto.getDescripcion()).replace("″", "").replace("́ ", "") 
			}
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtConcepto===>"+stmtConcepto);
			Utils.imprimeLog("", e);
		}
		return idConcepto;
	}
	
	
	private int altaEmisor(PreparedStatement stmtEmisor, Comprobante _comprobante) {
		int resultado = 0;
		String uuid = null;
		try {
			uuid = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			
			stmtEmisor.setString(1, uuid);
			stmtEmisor.setString(2, _comprobante.getComplemento().getNomina().getEmisor().getCurp());
			stmtEmisor.setString(3, _comprobante.getComplemento().getNomina().getEmisor().getRegistroPatronal());
			stmtEmisor.setString(4, _comprobante.getComplemento().getNomina().getEmisor().getRfcPatronOrigen());
			if (_comprobante.getComplemento().getNomina().getEmisor().getEntidadSNCF() == null) {
				stmtEmisor.setString(5, "");
				stmtEmisor.setDouble(6, 0);
			}else {
				stmtEmisor.setString(5, _comprobante.getComplemento().getNomina().getEmisor().getEntidadSNCF().getOrigenRecurso());
				stmtEmisor.setDouble(6, _comprobante.getComplemento().getNomina().getEmisor().getEntidadSNCF().getMontoRecursoPropio());	
			}
			resultado = stmtEmisor.executeUpdate();
			
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtEmisor===>"+stmtEmisor);
			Utils.imprimeLog("", e);
		}
		return resultado;
	}
	
	private int altaReceptor(PreparedStatement stmtReceptor, Comprobante _comprobante) {
		int resultado = 0;
		String uuid = null;
		LocalDate fechaLaboralLD = null;
		try {
			uuid = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			 fechaLaboralLD = _comprobante.getComplemento().getNomina().getReceptor().getFechaInicioRelLaboral();
			 String fechaLaboral = fechaLaboralLD.format(formatterDate);
			 if (fechaLaboral.length() > 10) {
				 fechaLaboral = fechaLaboral.substring(0, 10);
			 }
			 
			stmtReceptor.setString(1, uuid);
			stmtReceptor.setString(2, _comprobante.getComplemento().getNomina().getReceptor().getNumEmpleado());
			stmtReceptor.setString(3, _comprobante.getComplemento().getNomina().getReceptor().getCurp());
			stmtReceptor.setString(4, _comprobante.getComplemento().getNomina().getReceptor().getNumSeguridadSocial());
			stmtReceptor.setString(5, fechaLaboral);
			stmtReceptor.setString(6, _comprobante.getComplemento().getNomina().getReceptor().getAntiguedad());
			stmtReceptor.setString(7, _comprobante.getComplemento().getNomina().getReceptor().getTipoContrato());
			stmtReceptor.setString(8, _comprobante.getComplemento().getNomina().getReceptor().getSindicalizado());
			stmtReceptor.setString(9, _comprobante.getComplemento().getNomina().getReceptor().getTipoJornada());
			stmtReceptor.setString(10, _comprobante.getComplemento().getNomina().getReceptor().getTipoRegimen());
			stmtReceptor.setString(11, _comprobante.getComplemento().getNomina().getReceptor().getDepartamento());
			stmtReceptor.setString(12, _comprobante.getComplemento().getNomina().getReceptor().getPuesto());
			stmtReceptor.setString(13, _comprobante.getComplemento().getNomina().getReceptor().getRiesgoPuesto());
			stmtReceptor.setString(14, _comprobante.getComplemento().getNomina().getReceptor().getPeriodicidadPago());
			stmtReceptor.setString(15, _comprobante.getComplemento().getNomina().getReceptor().getBanco());
			stmtReceptor.setString(16, _comprobante.getComplemento().getNomina().getReceptor().getCuentaBancaria());
			stmtReceptor.setDouble(17, _comprobante.getComplemento().getNomina().getReceptor().getSalarioBaseCotApor());
			stmtReceptor.setDouble(18, _comprobante.getComplemento().getNomina().getReceptor().getSalarioDiarioIntegrado());
			stmtReceptor.setString(19, _comprobante.getComplemento().getNomina().getReceptor().getClaveEntFed());
			resultado = stmtReceptor.executeUpdate();
			
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtReceptor===>"+stmtReceptor);
			Utils.imprimeLog("", e);
		}
		return resultado;
	}
	
	
	private int altaPercepciones(PreparedStatement stmtPercepciones, PreparedStatement stmtPercepcionesDetalle,  
			PreparedStatement stmtPercepcionesSeparacion, PreparedStatement stmtPercepcionesJubilacion, Comprobante _comprobante) {
		int resultado = 0;
		String uuid = null;
		try {
			uuid = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			
			stmtPercepciones.setString(1, uuid);
			if (_comprobante.getComplemento().getNomina().getPercepciones() == null) {
				stmtPercepciones.setDouble(2, 0);
				stmtPercepciones.setDouble(3, 0);
				stmtPercepciones.setDouble(4, 0);
				stmtPercepciones.setDouble(5, 0);
				stmtPercepciones.setDouble(6, 0);
				
			}else {
				stmtPercepciones.setDouble(2, _comprobante.getComplemento().getNomina().getPercepciones().getTotalSueldos());
				stmtPercepciones.setDouble(3, _comprobante.getComplemento().getNomina().getPercepciones().getTotalGravado());
				stmtPercepciones.setDouble(4, _comprobante.getComplemento().getNomina().getPercepciones().getTotalExento());
				stmtPercepciones.setDouble(5, _comprobante.getComplemento().getNomina().getPercepciones().getTotalSeparacionIndemnizacion());
				stmtPercepciones.setDouble(6, _comprobante.getComplemento().getNomina().getPercepciones().getTotalJubilacionPensionRetiro());
			
				ArrayList<NPercepcion> listaPercepciones = _comprobante.getComplemento().getNomina().getPercepciones().getPercepcion();
				if (listaPercepciones != null) {
					for (NPercepcion nPercepcion : listaPercepciones) {
						stmtPercepcionesDetalle.setString(1, uuid);
						stmtPercepcionesDetalle.setString(2, nPercepcion.getTipoPercepcion());
						stmtPercepcionesDetalle.setString(3, nPercepcion.getClave());
						stmtPercepcionesDetalle.setString(4, nPercepcion.getConcepto());
						stmtPercepcionesDetalle.setDouble(5, nPercepcion.getImporteGravado());
						stmtPercepcionesDetalle.setDouble(6, nPercepcion.getImporteExento());
						stmtPercepcionesDetalle.executeUpdate();
					}
				}
				
				if (_comprobante.getComplemento().getNomina().getPercepciones().getSeparacionIndeminzacion() != null) {
					NSeparacionIndemnizacion nSeparacionIndemnizacion = _comprobante.getComplemento().getNomina().getPercepciones().getSeparacionIndeminzacion();
					stmtPercepcionesSeparacion.setString(1, uuid);
					stmtPercepcionesSeparacion.setDouble(2, nSeparacionIndemnizacion.getTotalPagado());
					stmtPercepcionesSeparacion.setInt(3, nSeparacionIndemnizacion.getNumAnosServicio());
					stmtPercepcionesSeparacion.setDouble(4, nSeparacionIndemnizacion.getUltimoSueldoMensOrd());
					stmtPercepcionesSeparacion.setDouble(5, nSeparacionIndemnizacion.getIngresoAcumulable());
					stmtPercepcionesSeparacion.setDouble(6, nSeparacionIndemnizacion.getIngresoNoAcumulable());
					stmtPercepcionesSeparacion.executeUpdate();
				}
				
				if (_comprobante.getComplemento().getNomina().getPercepciones().getJubilacionPensionRetiro() != null) {
					NJubilacionPensionRetiro nJubilacionPensionRetiro = _comprobante.getComplemento().getNomina().getPercepciones().getJubilacionPensionRetiro();
					stmtPercepcionesJubilacion.setString(1, uuid);
					stmtPercepcionesJubilacion.setDouble(2, nJubilacionPensionRetiro.getTotalUnaExhibicion());
					stmtPercepcionesJubilacion.setDouble(3, nJubilacionPensionRetiro.getTotalParcialidad());
					stmtPercepcionesJubilacion.setDouble(4, nJubilacionPensionRetiro.getMontoDiario());
					stmtPercepcionesJubilacion.setDouble(5, nJubilacionPensionRetiro.getIngresoAcumulable());
					stmtPercepcionesJubilacion.setDouble(6, nJubilacionPensionRetiro.getIngresoNoAcumulable());
					stmtPercepcionesJubilacion.executeUpdate();
				}
				
			}
			resultado = stmtPercepciones.executeUpdate();
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtPercepciones===>"+stmtPercepciones);
			Utils.imprimeLog("", e);
		}
		return resultado;
	}
	
	
	private int altaDeducciones(PreparedStatement stmtDeducciones, PreparedStatement stmtDeduccionesDetalle,  Comprobante _comprobante) {
		int resultado = 0;
		String uuid = null;
		try {
			uuid = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			if (_comprobante.getComplemento().getNomina().getDeducciones() != null) {
				stmtDeducciones.setString(1, uuid);
				stmtDeducciones.setDouble(2, _comprobante.getComplemento().getNomina().getDeducciones().getTotalOtrasDeducciones());
				stmtDeducciones.setDouble(3, _comprobante.getComplemento().getNomina().getDeducciones().getTotalImpuestosRetenidos());
				resultado = stmtDeducciones.executeUpdate();
				
				ArrayList<NDeduccion> listaDeducciones = _comprobante.getComplemento().getNomina().getDeducciones().getDeduccion();
				if (listaDeducciones != null) {
					for (NDeduccion nDeducciones : listaDeducciones) {
						stmtDeduccionesDetalle.setString(1, uuid);
						stmtDeduccionesDetalle.setString(2, nDeducciones.getTipoDeduccion());
						stmtDeduccionesDetalle.setString(3, nDeducciones.getClave());
						stmtDeduccionesDetalle.setString(4, nDeducciones.getConcepto());
						stmtDeduccionesDetalle.setDouble(5, nDeducciones.getImporte());
						stmtDeduccionesDetalle.executeUpdate();
						
					}
				}
			}
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtDeducciones===>"+stmtDeducciones);
			Utils.imprimeLog("", e);
		}
		return resultado;
	}
	
	
	private int altaOtrosPago(PreparedStatement stmtOtroPago, Comprobante _comprobante) {
		int resultado = 0;
		String uuid = null;
		try {
			
			uuid = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			if (_comprobante.getComplemento().getNomina().getOtrosPagos() != null && _comprobante.getComplemento().getNomina().getOtrosPagos().getOtroPago() != null) {
				ArrayList<NOtroPago> listaOtro = _comprobante.getComplemento().getNomina().getOtrosPagos().getOtroPago();
				for (NOtroPago nOtroPago : listaOtro) {
					stmtOtroPago.setString(1, uuid);
					stmtOtroPago.setString(2, nOtroPago.getTipoOtroPago());
					stmtOtroPago.setString(3, nOtroPago.getClave());
					stmtOtroPago.setString(4, nOtroPago.getConcepto());
					stmtOtroPago.setDouble(5, nOtroPago.getImporte());
					
					if (nOtroPago.getSubsidioAlEmpleo() == null) {
						stmtOtroPago.setDouble(6, 0);
					}else {
						stmtOtroPago.setDouble(6, nOtroPago.getSubsidioAlEmpleo().getSubsidioCausado());	
					}
					
					
					if (nOtroPago.getCompensacionSaldosAFavor() == null) {
						stmtOtroPago.setDouble(7, 0);
						stmtOtroPago.setDouble(8, 0);
						stmtOtroPago.setString(9, "");
					}else {
						stmtOtroPago.setDouble(7, nOtroPago.getCompensacionSaldosAFavor().getSaldoAFavor());
						stmtOtroPago.setDouble(8, nOtroPago.getCompensacionSaldosAFavor().getRemanenteSalFav());
						stmtOtroPago.setString(9, nOtroPago.getCompensacionSaldosAFavor().getAno());
					}
					
					resultado = stmtOtroPago.executeUpdate();
				}
				
			}
			
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtOtroPago===>"+stmtOtroPago);
			Utils.imprimeLog("", e);
		}
		return resultado;
	}
	
	

	public BovedaNominaForm consultaBovedaRegistro(Connection con, String esquema, int idRegistro)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BovedaNominaForm bovedaForm = new BovedaNominaForm();
        try{
        	
			StringBuffer sbQuery = new StringBuffer(BovedaNominaQuerys.getConsultaBoveda(esquema));
        	stmt = con.prepareStatement(sbQuery.toString());
        	stmt.setInt(1, idRegistro);
        	rs = stmt.executeQuery();
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            if(rs.next()){
				
            	bovedaForm.setIdRegistro(rs.getInt(1));
				bovedaForm.setUuid(Utils.noNuloNormal(rs.getString(2)));
				bovedaForm.setSerie(Utils.noNulo(rs.getString(3)));
				bovedaForm.setFolio(Utils.noNulo(rs.getString(4)));
				bovedaForm.setFechaFactura(Utils.noNulo(rs.getString(5)));
				bovedaForm.setSubTotal(decimal.format(rs.getDouble(8)));
				bovedaForm.setTotal(decimal.format(rs.getDouble(9)));
				bovedaForm.setTotalPercepciones(decimal.format(rs.getDouble(11)));
				bovedaForm.setTotalDeducciones(decimal.format(rs.getDouble(12)));
				bovedaForm.setRfcReceptor(Utils.noNulo(rs.getString(13)));
				bovedaForm.setRazonSocialReceptor(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(14))));
				bovedaForm.setTipoComprobante(Utils.noNulo(rs.getString(16)));
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
        return bovedaForm;
    }

	
	public int eliminaBoveda(Connection con, String esquema, String cadRegistros, String rutaBoveda)
    {
		PreparedStatement stmtBoveda = null;
		PreparedStatement stmtConceptos = null;
		PreparedStatement stmtEmisor = null;
		PreparedStatement stmtReceptor = null;
		PreparedStatement stmtPercepciones=null;
		PreparedStatement stmtPercepcionesDetalle = null;
		PreparedStatement stmtDeducciones = null;
		PreparedStatement stmtDeduccionesDetalle = null;
		PreparedStatement stmtOtroPago  = null;
		
        int resultado = 0;
        try{
        	
        	String arrRegistros [] = cadRegistros.split(";");
        	
        	stmtBoveda = con.prepareStatement(BovedaNominaQuerys.getEliminarBovedaNomina(esquema));
        	stmtConceptos = con.prepareStatement(BovedaNominaQuerys.getEliminarBovedaNominaConceptos(esquema));
        	stmtEmisor = con.prepareStatement(BovedaNominaQuerys.getEliminarBovedaNominaEmisor(esquema));
        	stmtReceptor = con.prepareStatement(BovedaNominaQuerys.getEliminarBovedaNominaReceptor(esquema));
        	stmtPercepciones = con.prepareStatement(BovedaNominaQuerys.getEliminarBovedaNominaPercepciones(esquema));
        	stmtPercepcionesDetalle = con.prepareStatement(BovedaNominaQuerys.getEliminarBovedaNominaPercepcionesDetalle(esquema));
        	stmtDeducciones = con.prepareStatement(BovedaNominaQuerys.getEliminarBovedaNominaDeducciones(esquema));
        	stmtDeduccionesDetalle = con.prepareStatement(BovedaNominaQuerys.getEliminarBovedaNominaDeduccionesDetalle(esquema));
        	stmtOtroPago = con.prepareStatement(BovedaNominaQuerys.geteliminarBovedaNominaOtrosPago(esquema));
        	
        	String uuid = null;
        	String rutaFile = null;
        	String sourceFile = UtilsPATH.RUTA_PUBLIC_HTML + "BOVEDA_ELIMINADOS" + File.separator;
        	for (int x = 0; x < arrRegistros.length; x++){
        		// uuid = buscarUUID(con, esquema, Integer.parseInt(arrRegistros[x]));
        		uuid = arrRegistros[x];
        		stmtBoveda.setString(1, arrRegistros[x]);
        		resultado = stmtBoveda.executeUpdate();
        		if (resultado > 0){
        			stmtConceptos.setString(1, arrRegistros[x]);
        			stmtConceptos.executeUpdate();
        			stmtEmisor.setString(1, arrRegistros[x]);
        			stmtEmisor.executeUpdate();
        			stmtReceptor.setString(1, arrRegistros[x]);
        			stmtReceptor.executeUpdate();
        			
        			stmtPercepciones.setString(1, arrRegistros[x]);
        			stmtPercepciones.executeUpdate();
        			stmtPercepcionesDetalle.setString(1, arrRegistros[x]);
        			stmtPercepcionesDetalle.executeUpdate();
        			stmtDeducciones.setString(1, arrRegistros[x]);
        			stmtDeducciones.executeUpdate();
        			stmtDeduccionesDetalle.setString(1, arrRegistros[x]);
        			stmtDeduccionesDetalle.executeUpdate();
        			stmtOtroPago.setString(1, arrRegistros[x]);
        			stmtOtroPago.executeUpdate();
        			
        			rutaFile = rutaBoveda + uuid + ".xml";
        			File fileBov = new File(rutaFile);
        			if (fileBov.exists()){
        				//logger.info("Archivo ELiminado..."+fileBov.delete());
        				File fileSource = new File(sourceFile + uuid + ".xml");
        				UtilsFile.moveFileDirectory(fileBov,fileSource , true, false, true, true);
        			}
        			fileBov = null;
        		}
        	}
        	
        }catch(Exception e){
            Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(stmtBoveda != null)
	            	stmtBoveda.close();
	            stmtBoveda = null;
	            if(stmtConceptos != null)
	            	stmtConceptos.close();
	            stmtConceptos = null;
	            if(stmtEmisor != null)
	            	stmtEmisor.close();
	            stmtEmisor = null;
	            if(stmtReceptor != null)
	            	stmtReceptor.close();
	            stmtReceptor = null;
	            if(stmtPercepciones != null)
	            	stmtPercepciones.close();
	            stmtPercepciones = null;
	            if(stmtPercepcionesDetalle != null)
	            	stmtPercepcionesDetalle.close();
	            stmtPercepcionesDetalle = null;
	            if(stmtDeducciones != null)
	            	stmtDeducciones.close();
	            stmtDeducciones = null;
	            if(stmtDeduccionesDetalle != null)
	            	stmtDeduccionesDetalle.close();
	            stmtDeduccionesDetalle = null;
	            if(stmtOtroPago != null)
	            	stmtOtroPago.close();
	            stmtOtroPago = null;
	        }catch(Exception e){
	        	stmtBoveda = null;
	        	
	        }
        }
        
        return resultado;
    }


	
	public ArrayList<BovedaNominaForm> detalleBovedaZIP(
	        Connection con, String esquema,
	        String rfc, String razonSocial, String folio, String serie,
	        String fechaInicial, String uuidBoveda, String fechaFinal,
	        String cadRegistros,
	        // ==== Operadores texto ====
	        String rfcOperator, String razonOperator, String serieOperator, String uuidOperator,
	        // ==== Fecha ====
	        String dateOperator, String dateV1, String dateV2,
	        // ==== Numéricos ====
	        String folioOperator, String folioV1, String folioV2,
	        String totalOperator, String totalV1, String totalV2,
	        String subOperator,   String subV1,   String subV2,
	        String descOperator,  String descV1,  String descV2,
	        String percOperator,  String percV1,  String percV2,
	        String dedOperator,   String dedV1,   String dedV2
	){
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<BovedaNominaForm> lista = new ArrayList<>();

	    try {
	        StringBuilder sb = new StringBuilder(BovedaNominaQuerys.getDetalleBoveda(esquema));
	        List<Object> params = new ArrayList<>();
	        FiltrosBovedaNomina filtros = new FiltrosBovedaNomina();

	        // ¿El SELECT base ya trae WHERE? (ej. "... WHERE ES_NOMINA = ?")
	        boolean baseHasWhere = sb.toString().toLowerCase().contains(" where ");

	        // Crea Where con el ctor existente (2 args) y prima hasWhere por reflexión si es necesario
	        FiltrosBovedaNomina.Where w = new FiltrosBovedaNomina.Where(sb, params);
	        if (baseHasWhere) {
	            try {
	                java.lang.reflect.Field f = FiltrosBovedaNomina.Where.class.getDeclaredField("hasWhere");
	                f.setAccessible(true);
	                f.setBoolean(w, true); // <- dejamos el WHERE "activo" para que agregue "AND ..."
	            } catch (Exception ignore) {
	                // Si por alguna razón falla la reflexión, igual funcionará;
	                // pero ojo que podría insertar un WHERE extra si el base ya lo tenía.
	            }
	        }

	        // 1) Si viene selección explícita (UUIDs separados por ';') → IN (...)
	        if (cadRegistros != null && !cadRegistros.trim().isEmpty()) {
	            List<String> uuids = new ArrayList<>();
	            for (String u : cadRegistros.split(";")) {
	                if (u != null && !(u = u.trim()).isEmpty()) uuids.add(u);
	            }
	            filtros.addUuidIn(w, uuids);
	        } else {
	            // 2) Si no, aplica TODOS los filtros con operadores
	            filtros.aplicarFiltrosNomina(
	                w,
	                // Texto
	                rfc,   rfcOperator,
	                razonSocial, razonOperator,
	                serie, serieOperator,
	                uuidBoveda, uuidOperator,
	                // Fecha
	                dateOperator, dateV1, dateV2,
	                fechaInicial, fechaFinal,
	                // Numéricos
	                folio, folioOperator, folioV1, folioV2,
	                totalOperator, totalV1, totalV2,
	                subOperator,   subV1,   subV2,
	                descOperator,  descV1,  descV2,
	                percOperator,  percV1,  percV2,
	                dedOperator,   dedV1,   dedV2
	            );
	        }

	        sb.append(" ORDER BY FECHA_FACTURA DESC ");
	        stmt = con.prepareStatement(sb.toString());

	        int idx = 1;

	        // Si tu SELECT base tiene el primer parámetro (ej. ES_NOMINA = ?), déjalo:
	        stmt.setString(idx++, "N");
	        // Si NO lo tiene, comenta o elimina la línea anterior.

	        // Bind de parámetros generados por los filtros (en orden)
	        for (Object p : params) {
	            if (p instanceof java.math.BigDecimal) {
	                stmt.setBigDecimal(idx++, (java.math.BigDecimal) p);
	            } else {
	                stmt.setString(idx++, String.valueOf(p));
	            }
	        }

	        rs = stmt.executeQuery();
	        java.text.DecimalFormat decimal = new java.text.DecimalFormat("###,###.##");

	        while (rs.next()) {
	            BovedaNominaForm b = new BovedaNominaForm();
	            b.setIdRegistro(rs.getInt(1));
	            b.setUuid(Utils.noNuloNormal(rs.getString(2)));
	            b.setSerie(Utils.noNulo(rs.getString(3)));
	            b.setFolio(Utils.noNulo(rs.getString(4)));
	            b.setFechaFactura(Utils.noNulo(rs.getString(5)));

	            // Índices según tu SELECT de nómina:
	            b.setSubTotal(decimal.format(rs.getDouble(8)));
	            b.setTotal(decimal.format(rs.getDouble(9)));
	            b.setDescuento(decimal.format(rs.getDouble(10)));
	            b.setTotalPercepciones(decimal.format(rs.getDouble(11)));
	            b.setTotalDeducciones(decimal.format(rs.getDouble(12)));

	            b.setRfcReceptor(Utils.noNulo(rs.getString(15)));
	            b.setRazonSocialReceptor(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(16))));
	            b.setTipoComprobante(Utils.noNulo(rs.getString(17)));

	            lista.add(b);
	        }
	    } catch (Exception e) {
	        Utils.imprimeLog("detalleBovedaZIP(Nomina, filtros): ", e);
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
	    }
	    return lista;
	}




	
	public ArrayList<BovedaNominaForm> reporteDetalleXML(
	        Connection con,
	        String esquema,
	        String rfc, String razonSocial, String folio, String serie,
	        String fechaInicial, String uuidBoveda, String fechaFinal,
	        String idRegistro,                 // <-- NUEVO: selección de UUIDs "u1;u2;u3;"
	        int start, int length, boolean isExcel,
	        // ===== operadores/valores =====
	        // texto
	        String rfcOperator,   String razonOperator, String serieOperator, String uuidOperator,
	        // fecha
	        String dateOperator,  String dateV1,        String dateV2,
	        // numéricos
	        String folioOperator, String folioV1,       String folioV2,
	        String totalOperator, String totalV1,       String totalV2,
	        String subOperator,   String subV1,         String subV2,
	        String descOperator,  String descV1,        String descV2,
	        String percOperator,  String percV1,        String percV2,
	        String dedOperator,   String dedV1,         String dedV2
	) {

	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<BovedaNominaForm> listaDetalle = new ArrayList<>();

	    try {
	        StringBuilder sb = new StringBuilder(BovedaNominaQuerys.getReporteDetalle(esquema));
	        List<Object> params = new ArrayList<>();

	        // Tu SELECT base espera primero el tipo de comprobante 'N' como parámetro (posición 1)
	        params.add("N");

	        // WHERE builder que SIEMPRE agrega " AND ..." (porque tu SELECT base ya trae WHERE)
	        FiltrosBovedaNomina.Where w = new FiltrosBovedaNomina.Where(sb, params) {
	            @Override
	            public void and(String frag, Object... vals) {
	                if (frag == null || frag.isEmpty()) return;
	                sb.append(" AND ").append(frag);
	                if (vals != null) for (Object v : vals) if (v != null) params.add(v);
	            }
	        };

	        FiltrosBovedaNomina filtros = new FiltrosBovedaNomina();

	        // ===== Aplica filtros DX-like =====
	        filtros.aplicarFiltrosNomina(
	                w,
	                // Texto
	                rfc,   rfcOperator,
	                razonSocial, razonOperator,
	                serie, serieOperator,
	                uuidBoveda,  uuidOperator,
	                // Fecha (con respaldo a legado fechaInicial/fechaFinal)
	                dateOperator, dateV1, dateV2, fechaInicial, fechaFinal,
	                // Numéricos
	                folio, folioOperator, folioV1, folioV2,
	                totalOperator, totalV1, totalV2,
	                subOperator,   subV1,   subV2,
	                descOperator,  descV1,  descV2,
	                percOperator,  percV1,  percV2,
	                dedOperator,   dedV1,   dedV2
	        );

	        // ===== Selección de UUIDs (si viene idRegistro con "uuid;uuid;") =====
	        List<String> uuidList = new ArrayList<>();
	        if (idRegistro != null && !idRegistro.trim().isEmpty()) {
	            for (String u : idRegistro.split(";")) {
	                String z = (u == null) ? "" : u.trim();
	                if (!z.isEmpty()) uuidList.add(z);
	            }
	        }
	        filtros.addUuidIn(w, uuidList);

	        // ===== Orden + (sin paginar para Excel) =====
	        sb.append(" ORDER BY E.FECHA_FACTURA DESC ");
	        if (!isExcel && length > 0) {
	            sb.append(" LIMIT ").append(start).append(", ").append(length);
	        }

	        // ===== Prepara stmt y enlaza parámetros =====
	        stmt = con.prepareStatement(sb.toString());
	        int idx = 1;
	        for (Object p : params) {
	            if (p instanceof BigDecimal)     stmt.setBigDecimal(idx++, (BigDecimal) p);
	            else if (p instanceof Integer)   stmt.setInt(idx++, (Integer) p);
	            else                             stmt.setString(idx++, String.valueOf(p));
	        }

	        logger.info("📄 reporteDetalleXML (Nómina) → " + stmt);
	        rs = stmt.executeQuery();

	        java.text.DecimalFormat decimal = new java.text.DecimalFormat("###,###.##");

	        // ===== Mapea resultados (índices según tu SELECT de getReporteDetalle) =====
	        while (rs.next()) {
	            BovedaNominaForm b = new BovedaNominaForm();

	            b.setIdRegistro(rs.getInt(1));
	            b.setUuid(Utils.noNuloNormal(rs.getString(2)));
	            b.setRfcReceptor(Utils.noNulo(rs.getString(3)));
	            b.setRazonSocialReceptor(Utils.noNulo(rs.getString(4)));
	            b.setFechaPago(Utils.noNulo(rs.getString(5)));
	            b.setNumEmpleado(Utils.noNulo(rs.getString(6)));
	            b.setPuesto(Utils.noNulo(rs.getString(7)));
	            b.setRegistroPatronal(Utils.noNulo(rs.getString(8)));
	            b.setTipo(Utils.noNulo(rs.getString(9)));
	            b.setClave(Utils.noNulo(rs.getString(10)));
	            b.setConcepto(Utils.noNuloNormal(rs.getString(11)));

	            b.setImporteGravadoDouble(rs.getDouble(12));
	            b.setImporteGravado(decimal.format(b.getImporteGravadoDouble()));

	            b.setImporteExcentoDouble(rs.getDouble(13));
	            b.setImporteExcento(decimal.format(b.getImporteExcentoDouble()));

	            listaDetalle.add(b);
	        }

	    } catch (Exception e) {
	        Utils.imprimeLog("reporteDetalleXML(Nómina): ", e);
	    } finally {
	        try { if (rs != null)   rs.close(); } catch (Exception ignore) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
	    }
	    return listaDetalle;
	}
	

	
	public ArrayList<NominaForm> obtenerDeduccionesXML(PreparedStatement stmt, String uuid) {

		ResultSet rs = null;
		NominaForm nominaForm = new NominaForm();
		ArrayList<NominaForm> listaDetalle = new ArrayList<>();

		try {
			stmt.setString(1, uuid);
			// logger.info("stmtDeducciones===>"+stmt);
        	rs = stmt.executeQuery();
			DecimalFormat decimal = new DecimalFormat("###,###.##");
			while (rs.next()) {
				nominaForm.setTipo(Utils.noNulo(rs.getString(1)));
				nominaForm.setClave(Utils.noNulo(rs.getString(2)));
				nominaForm.setConcepto(Utils.noNulo(rs.getString(3)));
				nominaForm.setImporteGravadoDouble(rs.getDouble(4));
				nominaForm.setImporteGravado(decimal.format(nominaForm.getImporteGravadoDouble()));
				listaDetalle.add(nominaForm);
				nominaForm = new NominaForm();
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
			} catch (Exception e) {
				rs = null;
			}
		}
		return listaDetalle;
	}
	
	
	public ArrayList<NominaForm> obtenerOtroPagos(PreparedStatement stmt, String uuid) {

		ResultSet rs = null;
		NominaForm nominaForm = new NominaForm();
		ArrayList<NominaForm> listaDetalle = new ArrayList<>();

		try {
			stmt.setString(1, uuid);
			// logger.info("stmtDeducciones===>"+stmt);
        	rs = stmt.executeQuery();
			DecimalFormat decimal = new DecimalFormat("###,###.##");
			while (rs.next()) {
				nominaForm.setTipo(Utils.noNulo(rs.getString(1)));
				nominaForm.setClave(Utils.noNulo(rs.getString(2)));
				nominaForm.setConcepto(Utils.noNulo(rs.getString(3)));
				nominaForm.setImporteGravadoDouble(rs.getDouble(4));
				nominaForm.setImporteGravado(decimal.format(nominaForm.getImporteGravadoDouble()));
				listaDetalle.add(nominaForm);
				nominaForm = new NominaForm();
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				rs = null;
			} catch (Exception e) {
				rs = null;
			}
		}
		return listaDetalle;
	}
	
	
	
	public void actualizarEncontradosBoveda(Connection con, String esquema) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(BovedaNominaQuerys.getEncontradosBoveda(esquema));
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
	
	


	public int grabarDescarga(String esquema, String usuario, String rutaArchivo)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        ConexionDB connPool = null;
		ResultadoConexion rc = null;
		Connection con = null;
		
        try
        {
        	connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
            stmt = con.prepareStatement(ExportarXMLQuerys.getGrabarDescarga(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, rutaArchivo);
            stmt.setString(2, usuario);
            int cant = stmt.executeUpdate();
            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next())
                    resultado = rs.getInt(1);
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
	            if(con != null)
	                con.close();
	            con = null;
	        }catch(Exception e){
	        	rs = null;
	            stmt = null;
	            con = null;
	        }
        }
        return resultado;
    }
	

	

	public ArrayList<BovedaNominaForm> comboProveedores(Connection con, String esquema, String idLengueje) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BovedaNominaForm bovedaForm = new BovedaNominaForm();
        ArrayList<BovedaNominaForm> listaCombo = new ArrayList<>();
        // LenguajeBean lenguajeBean = LenguajeBean.instance();
        
        try{
        	
        	bovedaForm.setRfcReceptor("");
        	bovedaForm.setRazonSocialReceptor("Seleccione un Receptor");
        	listaCombo.add(bovedaForm);
        	bovedaForm = new BovedaNominaForm();
        	
        	StringBuffer sbQuery = new StringBuffer(BovedaNominaQuerys.getComboProveedores(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            rs = stmt.executeQuery();
	        
	        while (rs.next()){
	        	
	        	bovedaForm.setRfcReceptor(Utils.noNulo(rs.getString(1)));
	        	bovedaForm.setRazonSocialReceptor(bovedaForm.getRfcReceptor() + " - " +  Utils.noNulo(rs.getString(2)));
	        	listaCombo.add(bovedaForm);
	        	bovedaForm = new BovedaNominaForm();
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
        	
        	stmt = con.prepareStatement(BovedaNominaQuerys.getConsultaFechaMinima(esquema));
        	rs = stmt.executeQuery();
        	if (rs.next()){
        		fechaMinima = Utils.noNuloNormal(rs.getString(1));
        	}
        	if ("".equalsIgnoreCase(fechaMinima)) {
        		fechaMinima = UtilsFechas.getFechayyyyMMdd();
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
	
	public String obtenerUltimaFechaNomina(Connection con, String esquema) {
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String fecha = "";

	    try {
	        ps = con.prepareStatement(BovedaNominaQuerys.getUltimaFechaNomina(esquema));
	        rs = ps.executeQuery();

	        if (rs.next()) {
	            fecha = Utils.noNulo(rs.getString("FECHA"));
	        }

	    } catch (Exception e) {
	        Utils.imprimeLog("obtenerUltimaFechaNomina()", e);
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception ex) {}
	        try { if (ps != null) ps.close(); } catch (Exception ex) {}
	    }

	    return fecha;
	}

	
}
