package com.siarex247.cumplimientoFiscal.BovedaEmitidos;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.Concepto;
import com.itextpdf.xmltopdf.LeerXML;
import com.itextpdf.xmltopdf.Retencion;
import com.itextpdf.xmltopdf.RetencionC;
import com.itextpdf.xmltopdf.Traslado;
import com.itextpdf.xmltopdf.TrasladoC;
import com.itextpdf.xmltopdf.Pagos.PDoctoRelacionado;
import com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado;
import com.itextpdf.xmltopdf.Pagos20.Pago;
import com.itextpdf.xmltopdf.Pagos20.RetencionDR;
import com.itextpdf.xmltopdf.Pagos20.RetencionesDR;
import com.itextpdf.xmltopdf.Pagos20.TrasladoDR;
import com.itextpdf.xmltopdf.Pagos20.TrasladosDR;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaQuerys;
import com.siarex247.cumplimientoFiscal.Boveda.DoctoRelacionadoForm;
import com.siarex247.cumplimientoFiscal.Boveda.ExtraerXMLBean;
import com.siarex247.cumplimientoFiscal.DescargaSAT.DescargaMasivaLocal;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.validaciones.UtilsValidaciones;


public class BovedaEmitidosBean extends FiltrosBovedaEmitidos  {

	public static final Logger logger = Logger.getLogger("siarex247");

	public ArrayList<BovedaEmitidosForm> detalleBoveda(
	        Connection con,
	        String esquema,
	        String rfc, String razonSocial, String folio, String serie,
	        String fechaInicial, String tipoComprobante, String uuidBoveda, String fechaFinal,
	        int start, int length, boolean isExcel,
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
	) {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<BovedaEmitidosForm> lista = new ArrayList<>();

	    try {
	        // Usa la consulta base de EMITIDOS
	        StringBuilder sb = new StringBuilder(BovedaEmitidosQuerys.getDetalleBoveda(esquema));
	        List<Object> params = new ArrayList<>();
	        Where w = new Where(sb, params);

	        // Aplica TODOS los filtros (texto, fecha con operador, numéricos, etc.)
	        aplicarFiltrosBoveda(
	            w,
	            // Texto (para EMITIDOS filtra por RECEPTOR; tu Where/aplicarFiltrosBoveda debe
	            // estar implementado con RECEPTOR_RFC / RECEPTOR_NOMBRE)
	            rfc,   rfcOperator,
	            razonSocial, razonOperator,
	            serie, serieOperator,
	            tipoComprobante, tipoOperator,
	            uuidBoveda, uuidOperator,

	            // Fecha con operadores + legado
	            dateOperator, dateV1, dateV2,
	            fechaInicial, fechaFinal,

	            // Numéricos
	            folio, folioOperator, folioV1, folioV2,
	            totalOperator, totalV1, totalV2,
	            subOperator,   subV1,   subV2,
	            ivaOperator,   ivaV1,   ivaV2,
	            ivaRetOperator,ivaRetV1,ivaRetV2,
	            isrOperator,   isrV1,   isrV2,
	            impLocOperator,impLocV1,impLocV2
	        );

	        // Orden y paginación (solo si NO es Excel)
	        sb.append(" ORDER BY FECHA_FACTURA DESC ");
	        if (!isExcel) {
	            sb.append(" LIMIT ").append(start).append(", ").append(length);
	        }

	        stmt = con.prepareStatement(sb.toString());

	        // Bindeo de parámetros
	        int idx = 1;
	        for (Object p : params) {
	            if (p instanceof BigDecimal) stmt.setBigDecimal(idx++, (BigDecimal) p);
	            else if (p instanceof Integer) stmt.setInt(idx++, (Integer) p);
	            else stmt.setString(idx++, String.valueOf(p));
	        }

	        // Log (opcional)
	        logger.info("📥 detalleBoveda(Emitidos) → " + stmt);

	        rs = stmt.executeQuery();

	        DecimalFormat decimal = new DecimalFormat("###,###.##");
	        while (rs.next()) {
	            BovedaEmitidosForm b = new BovedaEmitidosForm();

	            // === columnas base (coinciden con las que ya usabas en tu método Excel de Emitidos) ===
	            b.setIdRegistro(rs.getInt(1));                         // ID_REGISTRO
	            b.setUuid(Utils.noNuloNormal(rs.getString(2)));        // UUID
	            b.setSerie(Utils.noNulo(rs.getString(3)));             // SERIE
	            b.setFolio(Utils.noNulo(rs.getString(4)));             // FOLIO
	            b.setFechaFactura(Utils.noNulo(rs.getString(5)));      // FECHA_FACTURA

	            // Totales que tu DataTable muestra (mismos índices que en tu Excel previo)
	            b.setSubTotal(                decimal.format(rs.getDouble(9)));   // SUBTOTAL
	            b.setTotalImpuestoRetenido(   decimal.format(rs.getDouble(11)));  // TOTAL_RET
	            b.setTotalImpuestoTraslado(   decimal.format(rs.getDouble(12)));  // TOTAL_TRAS
	            b.setTotal(                   decimal.format(rs.getDouble(13)));  // TOTAL

	            // Receptor (en Emitidos, la grilla pide RFC/ Razón del RECEPTOR)
	            b.setRfcReceptor(Utils.noNulo(rs.getString(16)));                 // RECEPTOR_RFC
	            b.setRazonSocialReceptor(
	                Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(17)))
	            );                                                                // RECEPTOR_NOMBRE

	            // Tipo de comprobante
	            b.setTipoComprobante(Utils.noNulo(rs.getString(18)));

	            // Si en tu query incluyes el campo de XML, puedes descomentar esto y ajustar índice:
	            // b.setXml(Utils.noNuloNormal(rs.getString(23)));

	            lista.add(b);
	        }
	    } catch (Exception e) {
	        Utils.imprimeLog("detalleBoveda(Emitidos): ", e);
	    } finally {
	        try { if (rs != null) rs.close(); }   catch (Exception ignore) {}
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
		        try{
		            String base = BovedaEmitidosQuerys.getTotalRegistros(esquema); // o "SELECT COUNT(*) FROM <vista_emitidos>"
		            StringBuilder sb = new StringBuilder(base);
		            List<Object> params = new ArrayList<>();
		            Where w = new Where(sb, params);

		            aplicarFiltrosBoveda(
		                w,
		                rfc, rfcOperator,
		                razonSocial, razonOperator,
		                serie, serieOperator,
		                tipoComprobante, tipoOperator,
		                uuidBoveda, uuidOperator,
		                dateOperator, dateV1, dateV2,
		                fechaInicial, fechaFinal,
		                folio, folioOperator, folioV1, folioV2,
		                totalOperator, totalV1, totalV2,
		                subOperator, subV1, subV2,
		                ivaOperator, ivaV1, ivaV2,
		                ivaRetOperator, ivaRetV1, ivaRetV2,
		                isrOperator, isrV1, isrV2,
		                impLocOperator, impLocV1, impLocV2
		            );

		            stmt = con.prepareStatement(sb.toString());
		            int idx=1;
		            for (Object p : params){
		                if (p instanceof BigDecimal) stmt.setBigDecimal(idx++, (BigDecimal)p);
		                else if (p instanceof Integer) stmt.setInt(idx++, (Integer)p);
		                else stmt.setString(idx++, String.valueOf(p));
		            }

		            logger.info("📈 totalRegistros(Emitidos) → " + stmt);
		            rs = stmt.executeQuery();
		            if (rs.next()) total = rs.getInt(1);
		        } catch (Exception e){
		            Utils.imprimeLog("totalRegistros(Emitidos): ", e);
		        } finally {
		            try { if (rs!=null) rs.close(); } catch(Exception ignore){}
		            try { if (stmt!=null) stmt.close(); } catch(Exception ignore){}
		        }
		        return total;
		    }
	
	public void procesarXmlBoveda(Connection con, String esquema, String esquemaEmpresa, File fileHTTP, Integer arrResultado [], String usuarioHTTP, boolean bandGuardarDescarga) {
		boolean isNomina = false;
		Comprobante _comprobante = null;
		ArrayList<Comprobante> listaDetalle = new ArrayList<Comprobante>();
		
		String rutaDesXML = null;
		String uuid = null;
		// String serie = null;
		// String folio = null;
		String fechaFactura = null;
		LocalDateTime fechaFacturaLD = null;
		// String formaPago = null;
		// String metodoPago = null;
		// double subTotal = 0;
		// double descuento = 0;
		double totalImpuestoRet = 0;
		double totalImpuestoTranslado = 0;
		// double total = 0;
		String fechaPago   = null;
		String fechaTimbrado   = null;
		LocalDateTime fechaPagoLD   = null;
		LocalDateTime fechaTimbradoLD   = null;
		ArrayList<Retencion> listaRetenciones = null;
		ArrayList<Traslado> listaTraslados = null;
		ArrayList<Concepto> listaConceptoXML = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		XMLEmitidosForm cargasXMLForm = new XMLEmitidosForm();
		
		// arrResultado[0] = Exitoso;
		// arrResultado[1] = Duplicado;
		// arrResultado[2] = Error en RFC;
		// arrResultado[3] = Error en XML;
		// arrResultado[4] = Nomina;
		 PreparedStatement stmtBoveda = null;
		 PreparedStatement stmtSAT = null;
		 PreparedStatement stmtImpuestos = null;
		 PreparedStatement stmtConceptos = null;
		 PreparedStatement stmtConceptosImpuestos = null;
		 PreparedStatement stmtComplemento = null;
		 PreparedStatement stmtComplementoPago = null;
		 PreparedStatement stmtComplementoPagoDoctoR = null;
		 PreparedStatement stmtComplementoPagoImpuestos = null;
		  try{
			  
			  stmtBoveda = con.prepareStatement(BovedaEmitidosQuerys.getAltaBoveda(esquema));
			  stmtSAT = con.prepareStatement(BovedaEmitidosQuerys.getAltaBovedaSAT(esquema));
			  stmtImpuestos = con.prepareStatement(BovedaEmitidosQuerys.getAltaBovedaImpuestos(esquema));
			  stmtConceptos = con.prepareStatement(BovedaEmitidosQuerys.getAltaBovedaConceptos(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			  stmtConceptosImpuestos = con.prepareStatement(BovedaEmitidosQuerys.getAltaBovedaConceptosImp(esquema));
			  stmtComplemento = con.prepareStatement(BovedaEmitidosQuerys.getAltaBovedaComplemento(esquema));
			  stmtComplementoPago = con.prepareStatement(BovedaEmitidosQuerys.getAltaBovedaComplementoPagos(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			  stmtComplementoPagoDoctoR = con.prepareStatement(BovedaEmitidosQuerys.getAltaBovedaComplementoPagosDoctoR(esquema));
			  stmtComplementoPagoImpuestos = con.prepareStatement(BovedaEmitidosQuerys.getAltaBovedaComplementoPagosImpuestos(esquema));
			  
			    try {
			    	_comprobante = LeerXML.ObtenerComprobante(fileHTTP.getAbsolutePath());
			    	if (_comprobante == null) {
			    		logger.info("XML Fallo====>"+fileHTTP.getName());
			    	}
			    }catch(Exception e) {
			    	Utils.imprimeLog("", e);
			    }
			    
			 //   subTotal     =  _comprobante.getSubTotal();
			    uuid         =  _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			//	serie        =  _comprobante.getSerie();
			//	folio        =  _comprobante.getFolio();
				fechaFacturaLD =  _comprobante.getFecha();
				
				
				String tipoComprobante   =  _comprobante.getTipoDeComprobante();
				//logger.info("tipoComprobante====>"+tipoComprobante);
				if ("N".equalsIgnoreCase(tipoComprobante) || "nomina".equalsIgnoreCase(tipoComprobante)) {
					isNomina = true;
				}else if ("I".equalsIgnoreCase(tipoComprobante) || "ingreso".equalsIgnoreCase(tipoComprobante) || "e".equalsIgnoreCase(tipoComprobante)
						|| "egreso".equalsIgnoreCase(tipoComprobante)) {
					    // formaPago    =  _comprobante.getFormaPago();
					    // metodoPago   =  _comprobante.getMetodoPago();	
					    // descuento    =  _comprobante.getDescuento();
					
					    if(_comprobante.getImpuestos() != null) {
					    	totalImpuestoRet =  _comprobante.getImpuestos().getTotalImpuestosRetenidos();
					    	if (totalImpuestoRet < 0) {
					    		totalImpuestoRet = 0;
					    	}
					    	listaRetenciones = _comprobante.getImpuestos().getRetenciones();
					    }
						if( _comprobante.getImpuestos() != null) {
							totalImpuestoTranslado = _comprobante.getImpuestos().getTotalImpuestosTrasladados();
							if (totalImpuestoTranslado < 0) {
								totalImpuestoTranslado = 0;
					    	}
							listaTraslados = _comprobante.getImpuestos().getTraslados();
						}
						// total =  _comprobante.getTotal();
						//logger.info("listRetencion ------> " + listRetencion);
				 } else if ("T".equalsIgnoreCase(tipoComprobante)) {
					    // total = _comprobante.getTotal();
					    
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
					 	//total = _comprobante.getTotal(); 

					 	if(_comprobante.getComplemento().Pagos != null) {
					 		fechaPagoLD    = _comprobante.getComplemento().Pagos.getPago().get(0).getFechaPago();  
					 		fechaPago = fechaPagoLD.format(formatter);
					 	}

				    	fechaTimbradoLD   =  _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado(); 
				    	
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
					
					  String tipoMoneda = null;
	 					try {
	 	 					tipoMoneda = Utils.noNulo(_comprobante.getMoneda());
	 	 					if (tipoMoneda.length() > 3) {
	 	 						String desMoneda = UtilsValidaciones.validaDesMoneda(tipoMoneda);
	 	 						if (desMoneda == null) {
	 	 							tipoMoneda = "MXN";
	 	 						}else {
	 	 							String cadMoneda [] = desMoneda.split(";"); 
	 	 							tipoMoneda = cadMoneda [1];
	 	 						}
	 	 					}
	 					}catch(Exception e) {
	 						if (tipoMoneda.length() > 3) {
	 							tipoMoneda = _comprobante.getMoneda().substring(0,3);
	 						}
	 						
	 					}
	 					
					listaConceptoXML = _comprobante.getConceptos().getConcepto();
					
					fechaFactura = fechaFacturaLD.format(formatter);
					cargasXMLForm.setUuid(uuid);
					cargasXMLForm.setSerie(_comprobante.getSerie());
					cargasXMLForm.setFolio(_comprobante.getFolio());
					cargasXMLForm.setCertificado(_comprobante.getCertificado());
					cargasXMLForm.setCondicionesPago(_comprobante.getCondicionesDePago());
					cargasXMLForm.setTipoCambio(_comprobante.getTipoCambio());
					cargasXMLForm.setLugarExpedicion( _comprobante.getLugarExpedicion());
					
					cargasXMLForm.setFechaFactura(fechaFactura);
					cargasXMLForm.setFormaPago(_comprobante.getFormaPago());
					cargasXMLForm.setMetodoPago(_comprobante.getMetodoPago());
					cargasXMLForm.setMoneda(tipoMoneda);
					// cargasXMLForm.setNumeroCuentaPago(numeroCuentaPago);
					cargasXMLForm.setSubTotal(_comprobante.getSubTotal());
					cargasXMLForm.setDescuento(_comprobante.getDescuento());
					cargasXMLForm.setTotalImpuestoRet(totalImpuestoRet);
					cargasXMLForm.setTotalImpuestoTranslado(totalImpuestoTranslado);
					cargasXMLForm.setTotal(_comprobante.getTotal());
					cargasXMLForm.setEmisorRFC(_comprobante.getEmisor().getRfc());
					cargasXMLForm.setEmisorNombre(_comprobante.getEmisor().getNombre());
					cargasXMLForm.setReceptorRFC(_comprobante.getReceptor().getRfc());
					cargasXMLForm.setReceptorNombre(_comprobante.getReceptor().getNombre());
					cargasXMLForm.setReceptorResidencia(_comprobante.getReceptor().getResidenciaFiscal());
					cargasXMLForm.setReceptorDomicilio(_comprobante.getReceptor().getDomicilioFiscalReceptor());
					cargasXMLForm.setReceptorRegimen(_comprobante.getReceptor().getRegimenFiscalReceptor());
					cargasXMLForm.setVersionComplemento(_comprobante.getComplemento().getTimbreFiscalDigital().getVersion());
					cargasXMLForm.setRfcProvCert(_comprobante.getComplemento().getTimbreFiscalDigital().getRfcProvCertif());
					cargasXMLForm.setNoCertificado(_comprobante.getNoCertificado());
					cargasXMLForm.setSello(_comprobante.getSello());
					cargasXMLForm.setNoCertificadoSAT(_comprobante.getComplemento().getTimbreFiscalDigital().getNoCertificadoSAT());
					cargasXMLForm.setSelloCFD(_comprobante.getComplemento().getTimbreFiscalDigital().getSelloCFD());
					cargasXMLForm.setSelloSAT(_comprobante.getComplemento().getTimbreFiscalDigital().getSelloSAT());
					cargasXMLForm.setTipoComprobante(tipoComprobante);
					cargasXMLForm.setFechaPago(fechaPago);
					cargasXMLForm.setFechaTimbrado(fechaTimbrado);
				
					cargasXMLForm.setVersion(_comprobante.getVersion());
					cargasXMLForm.setCertificadoPago(_comprobante.getCertificado());
					cargasXMLForm.setEmisorRegimen(_comprobante.getEmisor().getRegimenFiscal());
					
					cargasXMLForm.setReceptorResidencia(_comprobante.getReceptor().getResidenciaFiscal());
					cargasXMLForm.setReceptorUso(_comprobante.getReceptor().getUsoCFDI());
				
					//String RFC_EMISOR = Utils.noNulo(mapaConfig.get("RFC_RECEPTOR"));
					String RFC_EMISOR = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "RFC_RECEPTOR");
					boolean bandAlta = false;
					 if ("S".equalsIgnoreCase(RFC_EMISOR)){
						String VALOR_RFC_RECEPTOR = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "VALOR_RFC_RECEPTOR"); 
						if (VALOR_RFC_RECEPTOR.equalsIgnoreCase(_comprobante.getEmisor().getRfc())) {
							 bandAlta = true;	
						}
					 }else {
						 bandAlta = true;
					 }
					 if (bandAlta){
						  int res = altaBoveda(stmtBoveda, cargasXMLForm, usuarioHTTP);
						  // falta actualizar el metadata cuando es manual
							if (res == 1062){
								// numFilesNG++;
								arrResultado[1]++;
							}else if (res == 1) {
								altaBovedaSAT(stmtSAT, cargasXMLForm, usuarioHTTP);
								if (listaTraslados != null && listaTraslados.size() > 0) {
									altaBovedaImpuestosTraslados(stmtImpuestos, cargasXMLForm.getUuid(), listaTraslados);
								}
								if (listaRetenciones != null && listaRetenciones.size() > 0) {
									altaBovedaImpuestosRetenciones(stmtImpuestos, cargasXMLForm.getUuid(), listaRetenciones);
								}
								
								if (listaConceptoXML != null && listaConceptoXML.size() > 0) {
									altaConceptos(stmtConceptos, stmtConceptosImpuestos, _comprobante);
								}
								
								if ("P".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
									altaComplemento(uuid, stmtComplemento, stmtComplementoPago, stmtComplementoPagoDoctoR, stmtComplementoPagoImpuestos, _comprobante);
								}
								
								rutaDesXML = UtilsPATH.REPOSITORIO_DOCUMENTOS +esquemaEmpresa+"/BOVEDA_EMITIDOS/" + uuid + ".xml";
								File fdesXML = new File(rutaDesXML);
								UtilsFile.moveFileDirectory(fileHTTP, fdesXML, true, false, true);
								// numFilesOK++;
								 arrResultado[0]++;
							}else {
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
		  }finally {
			  try {
					if (stmtBoveda != null)
						stmtBoveda.close();
					stmtBoveda = null;
					if (stmtSAT != null)
						stmtSAT.close();
					stmtSAT = null;
					if (stmtImpuestos != null)
						stmtImpuestos.close();
					stmtImpuestos = null;
					if (stmtConceptos != null)
						stmtConceptos.close();
					stmtConceptos = null;
					if (stmtConceptosImpuestos != null)
						stmtConceptosImpuestos.close();
					stmtConceptosImpuestos = null;
					if (stmtComplemento != null)
						stmtComplemento.close();
					stmtComplemento = null;
					if (stmtComplementoPago != null)
						stmtComplementoPago.close();
					stmtComplementoPago = null;
					
					if (stmtComplementoPagoDoctoR != null)
						stmtComplementoPagoDoctoR.close();
					stmtComplementoPagoDoctoR = null;
					
					if (stmtComplementoPagoImpuestos != null)
						stmtComplementoPagoImpuestos.close();
					stmtComplementoPagoImpuestos = null;
					
					
					
				} catch (Exception e) {
					stmtBoveda = null;
					stmtSAT = null;
					stmtImpuestos = null;
					stmtConceptos = null;
					stmtConceptosImpuestos = null;
					stmtComplementoPago = null;
				}
		  }
	}
	
	
	private int altaBoveda(PreparedStatement stmt, XMLEmitidosForm cargasXMLForm, String usuarioHTTP) {
		int resultado = 0;
		
		try {
			int numParam=1;
			stmt.setString(numParam++, cargasXMLForm.getUuid());
			stmt.setString(numParam++, cargasXMLForm.getVersion());
			stmt.setString(numParam++, cargasXMLForm.getSerie());
			stmt.setString(numParam++, cargasXMLForm.getFolio());
			stmt.setString(numParam++, cargasXMLForm.getFechaFactura());
			stmt.setString(numParam++, cargasXMLForm.getFormaPago());
			stmt.setString(numParam++, cargasXMLForm.getCondicionesPago());
			stmt.setString(numParam++, cargasXMLForm.getMoneda());
			stmt.setDouble(numParam++, cargasXMLForm.getTipoCambio());
			stmt.setString(numParam++, cargasXMLForm.getTipoComprobante());
			stmt.setString(numParam++, cargasXMLForm.getMetodoPago());
			stmt.setString(numParam++, cargasXMLForm.getLugarExpedicion());
			stmt.setDouble(numParam++, cargasXMLForm.getSubTotal());
			stmt.setDouble(numParam++, cargasXMLForm.getTotal());
			stmt.setDouble(numParam++, cargasXMLForm.getDescuento());
			stmt.setDouble(numParam++, cargasXMLForm.getTotalImpuestoRet());
			stmt.setDouble(numParam++, cargasXMLForm.getTotalImpuestoTranslado());
			stmt.setString(numParam++, cargasXMLForm.getEmisorRFC());
			stmt.setString(numParam++, cargasXMLForm.getEmisorNombre());
			stmt.setString(numParam++, cargasXMLForm.getEmisorRegimen());
			stmt.setString(numParam++, cargasXMLForm.getReceptorRFC());
			stmt.setString(numParam++, cargasXMLForm.getReceptorNombre());
			stmt.setString(numParam++, cargasXMLForm.getReceptorResidencia());
			stmt.setString(numParam++, cargasXMLForm.getReceptorDomicilio());
			stmt.setString(numParam++, cargasXMLForm.getReceptorRegimen());
			stmt.setString(numParam++, cargasXMLForm.getReceptorUso());
			stmt.setString(numParam++, cargasXMLForm.getVersionComplemento());
			stmt.setString(numParam++, cargasXMLForm.getFechaTimbrado());
			stmt.setString(numParam++, cargasXMLForm.getRfcProvCert());
			stmt.setString(numParam++, cargasXMLForm.getFechaPago());
			stmt.setString(numParam++, usuarioHTTP);
			resultado = stmt.executeUpdate();
			
		} catch (SQLException sql) {
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);

		} catch (Exception e) {
			resultado = 100;
			Utils.imprimeLog("", e);
		} 

		return resultado;
	}
	
	
	private int altaBovedaSAT(PreparedStatement stmtSAT, XMLEmitidosForm cargasXMLForm, String usuarioHTTP) {
		int resultado = 0;
		
		try {
			int numParam=1;
			stmtSAT.setString(numParam++, cargasXMLForm.getUuid());
			stmtSAT.setString(numParam++, cargasXMLForm.getNoCertificado());
			stmtSAT.setString(numParam++, cargasXMLForm.getSello());
			stmtSAT.setString(numParam++, cargasXMLForm.getCertificado());
			stmtSAT.setString(numParam++, cargasXMLForm.getNoCertificadoSAT());
			stmtSAT.setString(numParam++, cargasXMLForm.getSelloCFD());
			stmtSAT.setString(numParam++, cargasXMLForm.getSelloSAT());
			resultado = stmtSAT.executeUpdate();
			
		} catch (SQLException sql) {
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);

		} catch (Exception e) {
			resultado = 100;
			Utils.imprimeLog("", e);
		} 

		return resultado;
	}
	
	
	private int altaBovedaImpuestosTraslados(PreparedStatement stmtImpuestos, String uuid, ArrayList<Traslado> listaTraslados) {
		int resultado = 0;
		
		try {
			
			for (Traslado traslado : listaTraslados) {
				int numParam=1;
				stmtImpuestos.setString(numParam++, uuid);
				stmtImpuestos.setString(numParam++, "TRASLADO");
				stmtImpuestos.setDouble(numParam++, traslado.getBase());
				stmtImpuestos.setString(numParam++, traslado.getImpuesto());
				stmtImpuestos.setString(numParam++, traslado.getTipoFactor());
				stmtImpuestos.setDouble(numParam++, traslado.getTasaOCuota());
				stmtImpuestos.setDouble(numParam++, traslado.getImporte());
				resultado = stmtImpuestos.executeUpdate();
			}
			
			
		} catch (SQLException sql) {
			logger.info("uuid===>"+uuid);
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);

		} catch (Exception e) {
			resultado = 100;
			Utils.imprimeLog("", e);
		} 

		return resultado;
	}
	
	private int altaBovedaImpuestosRetenciones(PreparedStatement stmtImpuestos, String uuid, ArrayList<Retencion> listaRetenciones) {
		int resultado = 0;
		
		try {
			
			for (Retencion retencion : listaRetenciones) {
				int numParam=1;
				stmtImpuestos.setString(numParam++, uuid);
				stmtImpuestos.setString(numParam++, "RETENCION");
				stmtImpuestos.setDouble(numParam++, 0);
				stmtImpuestos.setString(numParam++, retencion.getImpuesto());
				stmtImpuestos.setString(numParam++, null);
				stmtImpuestos.setDouble(numParam++, 0);
				stmtImpuestos.setDouble(numParam++, retencion.getImporte());
				resultado = stmtImpuestos.executeUpdate();
			}
			
			
		} catch (SQLException sql) {
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);

		} catch (Exception e) {
			resultado = 100;
			Utils.imprimeLog("", e);
		} 

		return resultado;
	}
	
	
	private int altaConceptos(PreparedStatement stmtConcepto, PreparedStatement stmtConceptoImpuestos,  Comprobante _comprobante) {
		ResultSet rs = null;
		int idConcepto = 0;
		String uuid = null;
		try {
			uuid = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			ArrayList<Concepto> listaConceptoXML = _comprobante.getConceptos().getConcepto();
			String noIdentificacion = "";
			for (Concepto concepto : listaConceptoXML) {
				// conceptoXML = listaConceptoXML.get(x);
				noIdentificacion = concepto.getNoIdentificacion();
				if (Utils.noNulo(noIdentificacion).length() > 100) {
					noIdentificacion = noIdentificacion.substring(0, 10);
				}
				stmtConcepto.setString(1, uuid);
				stmtConcepto.setString(2, concepto.getClaveProdServ());
				stmtConcepto.setString(3, noIdentificacion);
				stmtConcepto.setDouble(4, concepto.getCantidad());
				stmtConcepto.setString(5, concepto.getClaveUnidad());
				stmtConcepto.setString(6, concepto.getUnidad());
				stmtConcepto.setObject(7, new String(concepto.getDescripcion().replace("″", "").replace("́ ", "")));
				stmtConcepto.setDouble(8, concepto.getValorUnitario());
				stmtConcepto.setDouble(9, concepto.getImporte());
				stmtConcepto.setDouble(10, concepto.getDescuento());
				stmtConcepto.setString(11, concepto.getObjetoImp());
				int cant = stmtConcepto.executeUpdate();
				// Utils.noNuloNormal(concepto.getDescripcion()).replace("″", "").replace("́ ", "") 
				if (cant > 0) {
					rs = stmtConcepto.getGeneratedKeys();
					if (rs.next()) {
						idConcepto = rs.getInt(1);
						if (concepto.getImpuestos() != null) {
							ArrayList<TrasladoC> listaTrasladosC = concepto.getImpuestos().getTraslados();
							if (listaTrasladosC == null) {
								listaTrasladosC = new ArrayList<>();
							}
							ArrayList<RetencionC> listaRetencionesC = concepto.getImpuestos().getRetenciones();
							if (listaRetencionesC == null) {
								listaRetencionesC = new ArrayList<>();
							}
							altaConceptosImpuestos(stmtConceptoImpuestos, uuid, idConcepto, 
									listaTrasladosC, listaRetencionesC);
						}
						
					}
					rs.close();
				}
			}
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtConcepto===>"+stmtConcepto);
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
			} catch (Exception e2) {
				rs = null;
			}
		}
		return idConcepto;
	}
	
	private int altaConceptosImpuestos(PreparedStatement stmtConceptoImp, String uuid, int idConcepto, 
			ArrayList<TrasladoC> listaTrasladosC, ArrayList<RetencionC> listaRetencionesC) {
		int resultado = 0;
		try {
			
			for (TrasladoC trasladoC : listaTrasladosC) {
				stmtConceptoImp.setString(1, uuid);
				stmtConceptoImp.setInt(2, idConcepto);
				stmtConceptoImp.setString(3, "TRASLADO");
				stmtConceptoImp.setDouble(4, trasladoC.getBase());
				stmtConceptoImp.setString(5, trasladoC.getImpuesto());
				stmtConceptoImp.setString(6, trasladoC.getTipoFactor());
				stmtConceptoImp.setDouble(7, trasladoC.getTasaOCuota());
				stmtConceptoImp.setDouble(8, trasladoC.getImporte());
				stmtConceptoImp.executeUpdate();
			}
			
			for (RetencionC retencionC : listaRetencionesC) {
				stmtConceptoImp.setString(1, uuid);
				stmtConceptoImp.setInt(2, idConcepto);
				stmtConceptoImp.setString(3, "RETENCION");
				stmtConceptoImp.setDouble(4, retencionC.getBase());
				stmtConceptoImp.setString(5, retencionC.getImpuesto());
				stmtConceptoImp.setString(6, retencionC.getTipoFactor());
				stmtConceptoImp.setDouble(7, retencionC.getTasaOCuota());
				stmtConceptoImp.setDouble(8, retencionC.getImporte());
				stmtConceptoImp.executeUpdate();
			}
			
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtConceptoImp===>"+stmtConceptoImp);
			Utils.imprimeLog("", e);
		}
		return resultado;
	}
	
	private int altaComplemento(String uuid, PreparedStatement stmtComplemento, PreparedStatement stmtPagos, PreparedStatement stmtDoctoR, 
			PreparedStatement stmtImpuestos, Comprobante _comprobante) {
		ExtraerXMLBean extraBean = new ExtraerXMLBean();
		ArrayList<Pago> listaPagos20 = null;
		ArrayList<com.itextpdf.xmltopdf.Pagos.Pago> listaPagos10 = null;
		ArrayList<DoctoRelacionadoForm> listaDoctosRelacionados = null;
		ResultSet rs = null;
		try {
			
			String versionComplemento = null;
			try {
				versionComplemento = _comprobante.getComplemento().getPagos20().getVersion();
				listaPagos20 = _comprobante.getComplemento().getPagos20().getPago();
			}catch(Exception e) {
				versionComplemento = _comprobante.getComplemento().getPagos().getVersion();
				listaPagos10 = _comprobante.getComplemento().getPagos().getPago();
			}
			
			if ("1.0".equalsIgnoreCase(versionComplemento)) {
				stmtComplemento.setString(1, uuid);
				stmtComplemento.setString(2, versionComplemento);
				stmtComplemento.setDouble(3, 0);
				stmtComplemento.setDouble(4, 0);
				stmtComplemento.setDouble(5, 0);
				stmtComplemento.setDouble(6, 0);
				stmtComplemento.setDouble(7, 0);
				stmtComplemento.setDouble(8, 0);
				stmtComplemento.setDouble(9, 0);
				stmtComplemento.setDouble(10,0);
			}else {
				stmtComplemento.setString(1, uuid);
				stmtComplemento.setString(2, versionComplemento);
				stmtComplemento.setDouble(3, _comprobante.getComplemento().getPagos20().getTotales().getTotalTrasladosBaseIVA16());
				stmtComplemento.setDouble(4, _comprobante.getComplemento().getPagos20().getTotales().getTotalTrasladosImpuestoIVA16());
				stmtComplemento.setDouble(5, _comprobante.getComplemento().getPagos20().getTotales().getTotalTrasladosBaseIVA8());
				stmtComplemento.setDouble(6, _comprobante.getComplemento().getPagos20().getTotales().getTotalTrasladosImpuestoIVA8());
				stmtComplemento.setDouble(7, _comprobante.getComplemento().getPagos20().getTotales().getTotalTrasladosBaseIVA0());
				stmtComplemento.setDouble(8, _comprobante.getComplemento().getPagos20().getTotales().getTotalTrasladosImpuestoIVA0());
				stmtComplemento.setDouble(9, _comprobante.getComplemento().getPagos20().getTotales().getTotalTrasladosBaseIVAExento());
				stmtComplemento.setDouble(10, _comprobante.getComplemento().getPagos20().getTotales().getMontoTotalPagos());
				
			}
			
			stmtComplemento.executeUpdate();
			
			if (listaPagos10 != null) { // si la version es 2.1 del complemento
				LocalDateTime fechaPagoLD = null;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String fechaPago = null;
				for (com.itextpdf.xmltopdf.Pagos.Pago pago : listaPagos10) {
					fechaPagoLD = pago.getFechaPago();
					fechaPago = fechaPagoLD.format(formatter);
					stmtPagos.setString(1, uuid);
					stmtPagos.setString(2, fechaPago);
					stmtPagos.setString(3, pago.getFormaDePagoP());
					stmtPagos.setString(4, pago.getMonedaP());
					stmtPagos.setDouble(5, pago.getTipoCambioP());
					stmtPagos.setDouble(6, pago.getMonto());
					stmtPagos.setString(7, pago.getCtaBeneficiario());
					stmtPagos.setString(8, pago.getNomBancoOrdExt());
					stmtPagos.setString(9, pago.getNumOperacion());
					stmtPagos.setString(10, pago.getRfcEmisorCtaBen());
					stmtPagos.setString(11, pago.getRfcEmisorCtaOrd());
					stmtPagos.setString(12, pago.getCtaOrdenante());
					stmtPagos.setString(13, pago.getTipoCadPago());
					int cant = stmtPagos.executeUpdate();
					if (cant > 0) {
						rs = stmtPagos.getGeneratedKeys();
						if (rs.next()) {
							int idPago = rs.getInt(1);
							if (pago.getDoctoRelacionado() != null) {
								altaComplementoPagoDoctoR(stmtDoctoR, stmtImpuestos, uuid, idPago, null, pago.getDoctoRelacionado());
							}
						}
						rs.close();
					}
				}
			}
			
			if (listaPagos20 != null) { // si la version es 2.0 del complemento
				LocalDateTime fechaPagoLD = null;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String fechaPago = null;
				for (Pago pago : listaPagos20) {
					fechaPagoLD = pago.getFechaPago();
					fechaPago = fechaPagoLD.format(formatter);
					stmtPagos.setString(1, uuid);
					stmtPagos.setString(2, fechaPago);
					stmtPagos.setString(3, pago.getFormaDePagoP());
					stmtPagos.setString(4, pago.getMonedaP());
					stmtPagos.setDouble(5, pago.getTipoCambioP());
					stmtPagos.setDouble(6, pago.getMonto());
					stmtPagos.setString(7, pago.getCtaBeneficiario());
					stmtPagos.setString(8, pago.getNomBancoOrdExt());
					stmtPagos.setString(9, pago.getNumOperacion());
					stmtPagos.setString(10, pago.getRfcEmisorCtaBen());
					stmtPagos.setString(11, pago.getRfcEmisorCtaOrd());
					stmtPagos.setString(12, pago.getCtaOrdenante());
					stmtPagos.setString(13, pago.getTipoCadPago());
					int cant = stmtPagos.executeUpdate();
					if (cant > 0) {
						rs = stmtPagos.getGeneratedKeys();
						if (rs.next()) {
							int idPago = rs.getInt(1);
							if (pago.getDoctoRelacionado() != null) {
								altaComplementoPagoDoctoR(stmtDoctoR, stmtImpuestos, uuid, idPago, pago.getDoctoRelacionado(), null);
							}
						}
						rs.close();
					}
				}
			}
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
			} catch (Exception e2) {
				rs = null;
			}
		}
		return 0;
	}
	
	private int altaComplementoPagoDoctoR(PreparedStatement stmtDoctoR, PreparedStatement stmtImpuestos, String uuid, int idPago, 
			ArrayList<DoctoRelacionado> listaDoctoRelacionado20, ArrayList<PDoctoRelacionado> listaDoctoRelacionado) {
		int resultado = 0;
		try {
			
			if (listaDoctoRelacionado != null) {
				for (PDoctoRelacionado doctoRelacionado : listaDoctoRelacionado) {
					stmtDoctoR.setString(1, uuid);
					stmtDoctoR.setInt(2, idPago);
					stmtDoctoR.setString(3, doctoRelacionado.getIdDocumento());
					stmtDoctoR.setString(4, doctoRelacionado.getSerie());
					stmtDoctoR.setDouble(5, 0);
					stmtDoctoR.setString(6, doctoRelacionado.getFolio());
					stmtDoctoR.setDouble(7, doctoRelacionado.getImpPagado());
					stmtDoctoR.setDouble(8, doctoRelacionado.getImpSaldoAnt());
					stmtDoctoR.setDouble(9, doctoRelacionado.getImpSaldoInsoluto());
					stmtDoctoR.setString(10, doctoRelacionado.getMonedaDR());
					stmtDoctoR.setString(11, doctoRelacionado.getNumParcialidad());
					stmtDoctoR.setString(12, null);
					stmtDoctoR.executeUpdate();
				}
			}
			
			if (listaDoctoRelacionado20 != null) {
				for (DoctoRelacionado doctoRelacionado : listaDoctoRelacionado20) {
					stmtDoctoR.setString(1, uuid);
					stmtDoctoR.setInt(2, idPago);
					stmtDoctoR.setString(3, doctoRelacionado.getIdDocumento());
					stmtDoctoR.setString(4, doctoRelacionado.getSerie());
					stmtDoctoR.setDouble(5, doctoRelacionado.getEquivalenciaDR());
					stmtDoctoR.setString(6, doctoRelacionado.getFolio());
					stmtDoctoR.setDouble(7, doctoRelacionado.getImpPagado());
					stmtDoctoR.setDouble(8, doctoRelacionado.getImpSaldoAnt());
					stmtDoctoR.setDouble(9, doctoRelacionado.getImpSaldoInsoluto());
					stmtDoctoR.setString(10, doctoRelacionado.getMonedaDR());
					stmtDoctoR.setString(11, doctoRelacionado.getNumParcialidad());
					stmtDoctoR.setString(12, doctoRelacionado.getObjetoImpDR());
					stmtDoctoR.executeUpdate();
					altaComplementoPagoImpuesto(stmtImpuestos, uuid, idPago, doctoRelacionado);
				}
			}
			
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtConceptoImp===>"+stmtDoctoR);
			Utils.imprimeLog("", e);
		}
		return resultado;
	}
	
	private int altaComplementoPagoImpuesto(PreparedStatement stmtImpuestos, String uuid, int idPago,  
			DoctoRelacionado doctoRelacionado) {
		int resultado = 0;
		try {
			TrasladosDR trasladosDR = doctoRelacionado.getImpuestosDR().getTrasladosDR();
			if (trasladosDR != null) {
				for (TrasladoDR trasladoDR : trasladosDR.getTrasladoDR()) {
					stmtImpuestos.setString(1, uuid);
					stmtImpuestos.setInt(2, idPago);
					stmtImpuestos.setString(3, doctoRelacionado.getIdDocumento());
					stmtImpuestos.setString(4, "TRASLADO");
					stmtImpuestos.setDouble(5, trasladoDR.getBaseDR());
					stmtImpuestos.setString(6, trasladoDR.getImpuestoDR());
					stmtImpuestos.setString(7, trasladoDR.getTipoFactorDR());
					stmtImpuestos.setDouble(8, trasladoDR.getTasaOCuotaDR());
					stmtImpuestos.setString(9, trasladoDR.getImpuestoDR());
					stmtImpuestos.executeUpdate();
				}
				
			}
			
			RetencionesDR retencionesDR = doctoRelacionado.getImpuestosDR().getRetencionesDR();
			if (retencionesDR != null) {
				for (RetencionDR retencionDR : retencionesDR.getRetencionDR()) {
					stmtImpuestos.setString(1, uuid);
					stmtImpuestos.setInt(2, idPago);
					stmtImpuestos.setString(3, doctoRelacionado.getIdDocumento());
					stmtImpuestos.setString(4, "RETENCION");
					stmtImpuestos.setDouble(5, retencionDR.getBaseDR());
					stmtImpuestos.setString(6, retencionDR.getImpuestoDR());
					stmtImpuestos.setString(7, retencionDR.getTipoFactorDR());
					stmtImpuestos.setDouble(8, retencionDR.getTasaOCuotaDR());
					stmtImpuestos.setString(9, retencionDR.getImpuestoDR());
					stmtImpuestos.executeUpdate();
				}
			}
			
		}catch(Exception e) {
			logger.info("uuid===>"+uuid);
			logger.info("stmtConceptoImp===>"+stmtImpuestos);
			Utils.imprimeLog("", e);
		}
		return resultado;
	}
	
	public BovedaEmitidosForm consultaBovedaRegistro(Connection con, String esquema, int idRegistro)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BovedaEmitidosForm bovedaForm = new BovedaEmitidosForm();
        try{
        	
			StringBuffer sbQuery = new StringBuffer(BovedaEmitidosQuerys.getConsultaBoveda(esquema));
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
				bovedaForm.setSubTotal(decimal.format(rs.getDouble(9)));
				bovedaForm.setTotal(decimal.format(rs.getDouble(13)));
				bovedaForm.setTotalImpuestoRetenido(decimal.format(rs.getDouble(11)));
				bovedaForm.setTotalImpuestoTraslado(decimal.format(rs.getDouble(12)));
				bovedaForm.setRfcReceptor(Utils.noNulo(rs.getString(16)));
				bovedaForm.setRazonSocialReceptor(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(17))));
				bovedaForm.setTipoComprobante(Utils.noNulo(rs.getString(18)));
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

	
	@SuppressWarnings("unchecked")
	public JSONObject consultaBovedaUUID(Connection con, String esquema, String uuid)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        try{
        	
        	StringBuffer sbQuery = new StringBuffer(BovedaEmitidosQuerys.getConsultaBovedaUUID(esquema));
        	stmt = con.prepareStatement(sbQuery.toString());
        	stmt.setString(1, uuid);
        	
        	rs = stmt.executeQuery();
            if(rs.next()){
					jsonobj.put("UUID", Utils.noNuloNormal(rs.getString(1)));
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
        PreparedStatement stmtBoveda = null;
        PreparedStatement stmtSAT = null;
        PreparedStatement stmtImpuestos = null;
        PreparedStatement stmtConceptos = null;
        int resultado = 0;
        try{
        	
        	String arrRegistros [] = cadRegistros.split(";");
        	
        	stmtBoveda = con.prepareStatement(BovedaEmitidosQuerys.getEliminaBoveda(esquema));
        	stmtSAT = con.prepareStatement(BovedaEmitidosQuerys.getEliminaBovedaSAT(esquema));
        	stmtImpuestos = con.prepareStatement(BovedaEmitidosQuerys.getEliminaBovedaImpuestos(esquema));
        	stmtConceptos = con.prepareStatement(BovedaEmitidosQuerys.getEliminaBovedaConceptos(esquema));
        	
        	String uuid = null;
        	String rutaFile = null;
        	String sourceFile = UtilsPATH.RUTA_PUBLIC_HTML + "BOVEDA_ELIMINADOS" + File.separator;
        	for (int x = 0; x < arrRegistros.length; x++){
        		// uuid = buscarUUID(con, esquema, Integer.parseInt(arrRegistros[x]));
        		uuid = arrRegistros[x];
        		stmtBoveda.setString(1, arrRegistros[x]);
        		resultado = stmtBoveda.executeUpdate();
        		if (resultado > 0){
        			stmtSAT.setString(1, arrRegistros[x]);
        			stmtSAT.executeUpdate();
        			stmtImpuestos.setString(1, arrRegistros[x]);
        			stmtImpuestos.executeUpdate();
        			stmtConceptos.setString(1, arrRegistros[x]);
        			stmtConceptos.executeUpdate();
        			
        			
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
	            if(stmtSAT != null)
	            	stmtSAT.close();
	            stmtSAT = null;
	            if(stmtImpuestos != null)
	            	stmtImpuestos.close();
	            stmtImpuestos = null;
	            if(stmtConceptos != null)
	            	stmtConceptos.close();
	            stmtConceptos = null;
	        }catch(Exception e){
	        	stmtBoveda = null;
	        	stmtSAT = null;
	        	stmtImpuestos = null;
	        	stmtConceptos = null;
	        }
        }
        
        return resultado;
    }

	
	
	public ArrayList<BovedaEmitidosForm> detalleBovedaZIP(Connection con, String esquema, String rfc,  String razonSocial,  String folio, String serie, String fechaInicial, 
			String tipoComprobante, String uuidBoveda, String fechaFinal, String cadRegistros)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BovedaEmitidosForm bovedaForm = new BovedaEmitidosForm();
		ArrayList<BovedaEmitidosForm> listaDetalle = new ArrayList<>();
        StringBuffer sbQueryFinal = new StringBuffer(); 
        try{
        	
        	StringBuffer sbQuery = new StringBuffer(BovedaEmitidosQuerys.getDetalleBoveda(esquema));
        	if (cadRegistros.length() > 0) {
        		String arrRegistros [] = cadRegistros.split(";");
        		sbQuery.append("where UUID in (");
        		for (int x = 0; x < arrRegistros.length; x++){
        			sbQuery.append("?,");
            	}
        		sbQueryFinal.append(sbQuery.substring(0, sbQuery.length() - 1));
        		sbQueryFinal.append(")");
        		stmt = con.prepareStatement(sbQueryFinal.toString());
        		
        		int numParam=1;
        		for (int x = 0; x < arrRegistros.length; x++){
        			stmt.setString(numParam++, arrRegistros[x] );
        		}
        		
        	}else {
        		if (!"".equalsIgnoreCase(rfc)){
            		sbQuery.append(" where RECEPTOR_RFC = ? ");
            	}
            	
            	
            	if (!"".equalsIgnoreCase(razonSocial) && (sbQuery.indexOf("where") > -1)){
            		sbQuery.append(" and RECEPTOR_NOMBRE like '%"+razonSocial+"%'");
            	}else if (!"".equalsIgnoreCase(razonSocial)){
            		sbQuery.append(" where RECEPTOR_NOMBRE like '%"+razonSocial+"%'");
            	}
            	
            	
            	if (!"".equalsIgnoreCase(folio) && (sbQuery.indexOf("where") > -1)){
            		sbQuery.append(" and FOLIO = ?");
            	}else if (!"".equalsIgnoreCase(folio)){
            		sbQuery.append(" where FOLIO = ?");
            	}

            	if (!"".equalsIgnoreCase(serie) && (sbQuery.indexOf("where") > -1)){
            		sbQuery.append(" and SERIE = ?");
            	}else if (!"".equalsIgnoreCase(serie)){
            		sbQuery.append(" where SERIE = ?");
            	}
            	 
            	if ((!fechaInicial.equalsIgnoreCase("") && !fechaFinal.equalsIgnoreCase("")) && sbQuery.indexOf("where") > -1){
            		sbQuery.append(" and FECHA_FACTURA between ? and ?");
            	}
            	else if (!fechaInicial.equalsIgnoreCase("") && !fechaFinal.equalsIgnoreCase("")){
            		sbQuery.append(" where FECHA_FACTURA between ? and ?");
            	}

            	if(tipoComprobante != null) {
    	        	if (!"".equalsIgnoreCase(tipoComprobante) && !"ALL".equalsIgnoreCase(tipoComprobante) && (sbQuery.indexOf("where") > -1)){
    	        		if ("I".equalsIgnoreCase(tipoComprobante)) {
    	        			sbQuery.append(" and TIPO_COMPROBANTE in (?, ?) ");	
    	        		}else {
    	        			sbQuery.append(" and TIPO_COMPROBANTE = ? ");
    	        		}
    	        		
    	        	}else if (!"".equalsIgnoreCase(tipoComprobante) && !"ALL".equalsIgnoreCase(tipoComprobante)){
    	        		if ("I".equalsIgnoreCase(tipoComprobante)) {
    	        			sbQuery.append(" where TIPO_COMPROBANTE in (?, ?) ");	
    	        		}else {
    	        			sbQuery.append(" where TIPO_COMPROBANTE = ? ");
    	        		}
    	        	}
            	}

            	if (!"".equalsIgnoreCase(uuidBoveda) && (sbQuery.indexOf("where") > -1)){
            		sbQuery.append(" and UUID = ? ");
            	}else if (!"".equalsIgnoreCase(uuidBoveda)){
            		sbQuery.append(" where UUID = ? ");
            	}
    			sbQuery.append(" order by FECHA_FACTURA desc ");
    			stmt = con.prepareStatement(sbQuery.toString());
    			
    			int paramSTMT = 1;

    			if (!"".equalsIgnoreCase(rfc)){
    				stmt.setString(paramSTMT++, rfc);
            	}
    			
            	if (!"".equalsIgnoreCase(folio)){
            		stmt.setString(paramSTMT++, folio);
            	}

            	if (!"".equalsIgnoreCase(serie)){
            		stmt.setString(paramSTMT++, serie);
            	}
            	
            	if ((!fechaInicial.equalsIgnoreCase("") && !fechaFinal.equalsIgnoreCase("")) ){
            		stmt.setString(paramSTMT++, fechaInicial + " 00:00:01");
            		stmt.setString(paramSTMT++, fechaFinal + " 23:59:59");
            	}

            	if(tipoComprobante != null) {
    	        	if (!"".equalsIgnoreCase(tipoComprobante) && !"ALL".equalsIgnoreCase(tipoComprobante)){
    	        		if ("I".equalsIgnoreCase(tipoComprobante)) {
    	        			stmt.setString(paramSTMT++, "I");
    	        			stmt.setString(paramSTMT++, "ingreso");
    	        		}else {
    	        			stmt.setString(paramSTMT++, tipoComprobante);
    	        		}
    	        	}
            	}
            	if (!"".equalsIgnoreCase(uuidBoveda)){
            		stmt.setString(paramSTMT++, uuidBoveda);
            	}
        	}
        	rs = stmt.executeQuery();
        	logger.info("stmtZip==>"+stmt);
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            
            while(rs.next()){
            	bovedaForm.setIdRegistro(rs.getInt(1));
				bovedaForm.setUuid(Utils.noNuloNormal(rs.getString(2)));
				bovedaForm.setSerie(Utils.noNulo(rs.getString(3)));
				bovedaForm.setFolio(Utils.noNulo(rs.getString(4)));
				bovedaForm.setFechaFactura(Utils.noNulo(rs.getString(5)));
				bovedaForm.setSubTotal(decimal.format(rs.getDouble(9)));
				bovedaForm.setTotal(decimal.format(rs.getDouble(13)));
				bovedaForm.setTotalImpuestoRetenido(decimal.format(rs.getDouble(11)));
				bovedaForm.setTotalImpuestoTraslado(decimal.format(rs.getDouble(12)));
				bovedaForm.setRfcReceptor(Utils.noNulo(rs.getString(16)));
				bovedaForm.setRazonSocialReceptor(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(17))));
				bovedaForm.setTipoComprobante(Utils.noNulo(rs.getString(18)));
				listaDetalle.add(bovedaForm);
				bovedaForm = new BovedaEmitidosForm();
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
    }
	
	
	// En BovedaEmitidosBean (que extiende FiltrosBoveda de Emitidos)
	public ArrayList<BovedaEmitidosForm> detalleBovedaEXCEL(
	    Connection con,
	    String esquema,
	    String rfc, String razonSocial, String folio, String serie,
	    String fechaInicial, String tipoComprobante, String uuidBoveda, String fechaFinal,
	    String cadRegistros,
	    // ==== TEXTO ====
	    String rfcOperator, String razonOperator, String serieOperator, String tipoOperator, String uuidOperator,
	    // ==== FECHA ====
	    String dateOperator, String dateV1, String dateV2,
	    // ==== NUMÉRICOS ====
	    String folioOperator,  String folioV1,  String folioV2,
	    String totalOperator,  String totalV1,  String totalV2,
	    String subOperator,    String subV1,    String subV2,
	    String trasOperator,   String trasV1,   String trasV2,   // Traslado total
	    String retOperator,    String retV1,    String retV2,    // Retenciones total
	    String isrOperator,    String isrV1,    String isrV2,
	    String impLocOperator, String impLocV1, String impLocV2
	){
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<BovedaEmitidosForm> datos = new ArrayList<>();

	    try {
	        StringBuilder sb = new StringBuilder(BovedaEmitidosQuerys.getDetalleBoveda(esquema));
	        java.util.List<Object> params = new java.util.ArrayList<>();
	        Where w = new Where(sb, params);

	        // 1) Si Excel envía selección explícita de UUIDs (uuid1;uuid2;...)
	        java.util.List<String> uuids = new java.util.ArrayList<>();
	        if (cadRegistros != null && !cadRegistros.trim().isEmpty()){
	            for (String u : cadRegistros.split(";")){
	                if (u != null && !(u = u.trim()).isEmpty()) uuids.add(u);
	            }
	        }
	        if (!uuids.isEmpty()){
	            addUuidIn(w, uuids);
	        } else {
	            // 2) Filtros estándar con operadores
	            //    OJO: para Emitidos usamos columnas del RECEPTOR
	            aplicarFiltrosBoveda(
	                w,
	                rfc,   rfcOperator,           // RECEPTOR_RFC
	                razonSocial, razonOperator,   // RECEPTOR_NOMBRE
	                serie, serieOperator,         // SERIE
	                tipoComprobante, tipoOperator,// TIPO_COMPROBANTE
	                uuidBoveda, uuidOperator,     // UUID

	                // fecha con operador + legado (fechaInicial/fechaFinal)
	                dateOperator, dateV1, dateV2, fechaInicial, fechaFinal,

	                // FOLIO: si mandas operador/valores => numérico; si no, usa exacto por texto
	                folio, folioOperator, folioV1, folioV2,

	                // Totales
	                totalOperator, totalV1, totalV2,   // TOTAL
	                subOperator,   subV1,   subV2,     // SUBTOTAL
	                trasOperator,  trasV1,  trasV2,    // TOTAL TRASLADO (col. 12)
	                retOperator,   retV1,   retV2,     // TOTAL RETENCIONES (col. 11)
	                isrOperator,   isrV1,   isrV2,     // ISR_RET (si existe en tu query; si no, queda sin efecto)
	                impLocOperator,impLocV1,impLocV2   // IMP_LOCALES (si aplica)
	            );
	        }

	        sb.append(" ORDER BY FECHA_FACTURA DESC");

	        stmt = con.prepareStatement(sb.toString());
	        int idx = 1;
	        for (Object p : params){
	            if (p instanceof java.math.BigDecimal) stmt.setBigDecimal(idx++, (java.math.BigDecimal)p);
	            else if (p instanceof Integer)        stmt.setInt(idx++, (Integer)p);
	            else                                   stmt.setString(idx++, String.valueOf(p));
	        }

	        logger.info("📤 detalleBovedaEXCEL(Emitidos) → " + stmt);
	        rs = stmt.executeQuery();

	        java.text.DecimalFormat decimal = new java.text.DecimalFormat("###,###.##");
	        while (rs.next()){
	            BovedaEmitidosForm b = new BovedaEmitidosForm();
	            b.setIdRegistro(rs.getInt(1));
	            b.setUuid(Utils.noNuloNormal(rs.getString(2)));
	            b.setSerie(Utils.noNulo(rs.getString(3)));
	            b.setFolio(Utils.noNulo(rs.getString(4)));
	            b.setFechaFactura(Utils.noNulo(rs.getString(5)));

	            // IMPORTES (según tu query de Emitidos)
	            b.setSubTotal(decimal.format(rs.getDouble(9)));
	            b.setTotalImpuestoRetenido(decimal.format(rs.getDouble(11)));
	            b.setTotalImpuestoTraslado(decimal.format(rs.getDouble(12)));
	            b.setTotal(decimal.format(rs.getDouble(13)));

	            // Receptor
	            b.setRfcReceptor(Utils.noNulo(rs.getString(16)));
	            b.setRazonSocialReceptor(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(17))));

	            b.setTipoComprobante(Utils.noNulo(rs.getString(18)));

	            datos.add(b);
	        }
	    } catch (Exception e){
	        Utils.imprimeLog("detalleBovedaEXCEL(Emitidos): ", e);
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
	    }
	    return datos;
	}

		
	
	public void actualizarEncontradosBoveda(Connection con, String esquema) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(BovedaEmitidosQuerys.getEncontradosBoveda(esquema));
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
	
	
	

	public ArrayList<BovedaEmitidosForm> comboProveedores(Connection con, String esquema, String idLengueje) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BovedaEmitidosForm bovedaForm = new BovedaEmitidosForm();
        ArrayList<BovedaEmitidosForm> listaCombo = new ArrayList<>();
        // LenguajeBean lenguajeBean = LenguajeBean.instance();
        
        try{
        	
        	bovedaForm.setRfcReceptor("");
        	bovedaForm.setRazonSocialReceptor("Seleccione un Receptor");
        	listaCombo.add(bovedaForm);
        	bovedaForm = new BovedaEmitidosForm();
        	
        	StringBuffer sbQuery = new StringBuffer(BovedaEmitidosQuerys.getComboProveedores(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            rs = stmt.executeQuery();
	        while (rs.next()){
	        	
	        	bovedaForm.setRfcReceptor(Utils.noNulo(rs.getString(1)));
	        	bovedaForm.setRazonSocialReceptor(bovedaForm.getRfcReceptor() + " - " +  Utils.noNulo(rs.getString(2)));
	        	listaCombo.add(bovedaForm);
	        	bovedaForm = new BovedaEmitidosForm();
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
	        	
	        	stmt = con.prepareStatement(BovedaEmitidosQuerys.getConsultaFechaMinima(esquema));
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
	 
	 public String obtenerUltimaFechaEmitidos(Connection con, String esquema) {
		    PreparedStatement ps = null;
		    ResultSet rs = null;
		    String fecha = "---";

		    try {
		        ps = con.prepareStatement(BovedaEmitidosQuerys.getUltimaFechaEmitidos(esquema));
		        rs = ps.executeQuery();

		        if (rs.next()) {
		            fecha = Utils.noNulo(rs.getString("FECHA"));
		            if ("".equals(fecha)) {
		                fecha = "---";
		            }
		        }

		    } catch (Exception e) {
		        Utils.imprimeLog("obtenerUltimaFechaEmitidos()", e);
		    } finally {
		        try { if (rs != null) rs.close(); } catch (Exception ex) {}
		        try { if (ps != null) ps.close(); } catch (Exception ex) {}
		    }

		    return fecha;
		}

	 
	 
}
