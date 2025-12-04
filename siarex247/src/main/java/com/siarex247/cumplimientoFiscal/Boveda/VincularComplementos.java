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
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.itextpdf.xmltopdf.Pagos.PDoctoRelacionado;
import com.itextpdf.xmltopdf.Pagos.Pago;
import com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.utils.MensajesSIAREX;
import com.siarex247.utils.Utils;
import com.siarex247.validaciones.UtilsSAT;
import com.siarex247.validaciones.UtilsValidaciones;
import com.siarex247.validaciones.ValidacionesComplemento;
import com.siarex247.validaciones.ValidacionesComplementoQuerys;
import com.siarex247.visor.VisorOrdenes.ComplementosForm;

public class VincularComplementos extends UtilsValidaciones{

	public static final Logger logger = Logger.getLogger("siarex247");
	
	public HashMap<String, Object> procesarXML(Connection con, String esquema, String rutaXML, String usuarioHTTP, String idLenguaje)
    {
    	String mensajeXML = "";
    	HashMap<String, Object> mapaResultado = new HashMap<>();
		try{
			  mapaResultado.put("MENSAJE", "ERROR");
			  mapaResultado.put("rows", new JSONArray());

			logger.info("Ruta XML: "+rutaXML);
			
				File fileXML = new File(rutaXML);
			    //JSONObject jsonInfoUUID = null;
			    
			    String nombreXML = null;
			    String nombrePDF = null;
			    
			    //LeerXML crea = null;
			    Comprobante _comprobante = null;
			    try {
			    	_comprobante = LeerXML.ObtenerComprobante(rutaXML);
			    }catch(Exception e) {
			    	Utils.imprimeLog("", e);
			    }
			    
				logger.info("_comprobante------------>"+_comprobante);
				if (_comprobante == null) {
					MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
					String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32); 
					mapaResultado.put("MENSAJE", mensajeFinal);
	    			return mapaResultado;
				}
				
				String versionXML = null;
				if (_comprobante.getComplemento().getPagos20() != null) {
					versionXML = _comprobante.getComplemento().getPagos20().getVersion();
				}else {
					versionXML = _comprobante.getComplemento().getPagos().getVersion();
				}
				

				if ("".equalsIgnoreCase(versionXML)) {
					versionXML = "1.0";
				}
				
				
				logger.info("versionXML====>"+versionXML);
				if("1.0".equalsIgnoreCase(versionXML)) {
					
					if (_comprobante.getComplemento().Pagos.getPago().size() > 0) {
						
						if (!"P".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
							MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
							String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE42); 
							mapaResultado.put("MENSAJE", mensajeFinal);
			    			return mapaResultado;
						}else {
							HashMap<String, String> mapaMensajeComplemento = new ValidacionesComplemento().validarEtiquetasXMLCOMP(con, esquema, _comprobante, versionXML);
							logger.info("mapaMensajeComplemento ------------>" + mapaMensajeComplemento.get("ERROR"));
							if ("FALSE".equalsIgnoreCase(mapaMensajeComplemento.get("ERROR"))) {
								mapaResultado.put("MENSAJE", mapaMensajeComplemento.get("MENSAJE"));
				    			return mapaResultado;
							}
						}
						
						
						// se obtiene bandera de tipo de validacion

						// Termina
						DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
					    String rfcFacturaXML  = _comprobante.getEmisor().getRfc();
					    String razonSocialXML = _comprobante.getEmisor().getNombre();
						String folioXML       = _comprobante.getFolio();
						String serieXML       = _comprobante.getSerie();
						String uuidComplemento        = _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
						double totalXML       = _comprobante.getTotal();
						String rfcReceptorXML = _comprobante.getReceptor().getRfc();
						
						LocalDateTime FECHA_TIMBRADOLD = _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado();
						String FECHA_TIMBRADO = FECHA_TIMBRADOLD.format(formatter);
						String TIPO_COMPROBANTE = _comprobante.getTipoDeComprobante();
						LocalDateTime FECHA_XMLLD  = _comprobante.getFecha();
						String FECHA_XML  = FECHA_XMLLD.format(formatter);
							
						// Son etiquetas parte de los pagos
						double MONTO_PAGADO = 0;
						LocalDateTime FECHA_PAGOLD = null;
						String FECHA_PAGO = "";
						String FORMA_PAGO = "";
						
					   // boolean bandArchivo = true;

					    String validaCOMPLE = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "RECHAZAR_COMPLE");
						
						logger.info("validaCOMPLE----->"+validaCOMPLE);
						String estadoSAT = "";
						String estatusSAT = "";
						// validaCOMPLE = "N";
						if ("S".equalsIgnoreCase(validaCOMPLE)) {
							
							String datosSAT [] = UtilsSAT.validaSAT(rfcFacturaXML, rfcReceptorXML, totalXML, uuidComplemento);
							//String datosSAT [] = {"Vigente","S - Comprobante obtenido satisfactoriamente."};
							estadoSAT = datosSAT[0];
							estatusSAT = datosSAT[1];
							
							logger.info("estado--->"+estadoSAT);
							logger.info("estatusSAT--->"+estatusSAT);
							
							if (estadoSAT.toUpperCase().indexOf("CANCELADO") > -1 ) {
								MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
								//cadenaRetorno =  "CANCELADO"+ ";" + mensajeSIAREX.MENSAJE29;
								String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE29), "<< UUID_COMPLE >>", uuidComplemento); 
								mapaResultado.put("MENSAJE", mensajeFinal);
								actualizarHistoricoPagosCancelado(con, esquema, _comprobante, "C20", "006", uuidComplemento);
								return mapaResultado;
							}
						}//FOLIO_EMPRESA
						double MONTO_FACTURA = 0;
						ArrayList<PDoctoRelacionado> listaDocto = null;
						PDoctoRelacionado docRel = null;
						
						HashMap<String, String> mapaUUIDXML = new HashMap<>();
						int totComprobantes = _comprobante.getComplemento().Pagos.getPago().size();
						int totRelacionados = 0;
						double TOTAL_COMPLEMENTO = 0;
						JSONObject jsonInfoUUID = null;
						//String tipoMonedaDR = null;
						ComplementosForm compleForm = null;
						ArrayList<String> listaFacturas = new ArrayList<String>();
						for (int x = 0; x < totComprobantes; x++) {
							Pago pago = _comprobante.getComplemento().Pagos.getPago().get(x);
							FECHA_PAGOLD = pago.getFechaPago();
							FECHA_PAGO  = FECHA_PAGOLD.format(formatter);
							FORMA_PAGO = pago.getFormaDePagoP();
							// logger.info("Monto()---->"+pago.getMonto());
							MONTO_PAGADO = pago.getMonto();

							listaDocto = pago.getDoctoRelacionado();
							totRelacionados = listaDocto.size();

							// logger.info("totRelacionados---->"+totRelacionados);
							for (int y=0; y < totRelacionados; y++) {
								docRel = listaDocto.get(y);
								listaFacturas.add(docRel.getIdDocumento());
						    	// logger.info("UUID ORDEN------------->"+docRel.getIdDocumento());
				    			// logger.info("ImpPagado()---->"+docRel.getImpPagado());
						    	// logger.info("MONTO_PAGADO---->"+MONTO_PAGADO);

						    	jsonInfoUUID = buscarOrden(con, esquema, docRel.getIdDocumento());
						    	compleForm = buscarUUIDComple(con, esquema, docRel.getIdDocumento());
						    	
						    	if (jsonInfoUUID.isEmpty()) {
					    			// logger.info("*************** NO ENCONTRO INFORMACION *******************");
					    			eliminarComplementoBoveda(con, esquema, uuidComplemento);
					    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
					    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE38), "<< UUID_COMPLE >>", docRel.getIdDocumento()); 
					    			mapaResultado.put("MENSAJE", mensajeFinal);
					    			return mapaResultado;
						    	}

						    	if (totRelacionados == 1) {
						    		MONTO_FACTURA = MONTO_PAGADO;	
						    	}else {
						    		MONTO_FACTURA = docRel.getImpPagado();
						    	}
						    	
								boolean bandEntro = false;
						    	if (!jsonInfoUUID.get("RFC_HISTORICO").toString().equalsIgnoreCase("")) {
						    		bandEntro = true;
						    	}
						    	
						    	MONTO_FACTURA = docRel.getImpPagado();
						    	 
						    	if (bandEntro) {
						    		if (!jsonInfoUUID.get("RFC_HISTORICO").toString().equalsIgnoreCase(_comprobante.getEmisor().getRfc())){
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE91), "<< UUID_COMPLEMENTO >>", uuidComplemento);
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "001", uuidComplemento);
										return mapaResultado;
										
						    		}else if (!jsonInfoUUID.get("TIPO_MONEDA_HISTORICO").toString().equalsIgnoreCase(docRel.getMonedaDR())) {
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE92), "<< UUID_FACTURA >>", docRel.getIdDocumento());
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "002", uuidComplemento);
										return mapaResultado;
						    			
						    		}else if (MONTO_FACTURA != Double.parseDouble(jsonInfoUUID.get("TOTAL_HISTORICO").toString())) {
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE93), "<< UUID_FACTURA >>", docRel.getIdDocumento());
						    			mapaResultado.put("MENSAJE", mensajeFinal);
						    			eliminarComplementoBoveda(con, esquema, uuidComplemento);
						    			actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "003", uuidComplemento);
										return mapaResultado;
						    			
						    		}else if (!jsonInfoUUID.get("FECHA_PAGO_HISTORICO").toString().equalsIgnoreCase(FECHA_PAGO.substring(0, 10))){
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE34), "<< FOLIO_FACTURA >>", docRel.getIdDocumento());
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "004", uuidComplemento);
										return mapaResultado;
						    			
						    		}else {
						    			
						    		}
						    	}else {
						    		if (!jsonInfoUUID.get("RFC_FACTURA").toString().equalsIgnoreCase(_comprobante.getEmisor().getRfc())){
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE91), "<< UUID_COMPLEMENTO >>", uuidComplemento);
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "001", uuidComplemento);
										return mapaResultado;
										
						    		}else if (!jsonInfoUUID.get("TIPO_MONEDA_FACTURA").toString().equalsIgnoreCase(docRel.getMonedaDR())) {
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE92), "<< UUID_FACTURA >>", docRel.getIdDocumento());
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "002", uuidComplemento);
										return mapaResultado;
						    			
						    		}else if (MONTO_FACTURA != Double.parseDouble(jsonInfoUUID.get("TOTAL_FACTURA").toString())) {
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE93), "<< UUID_FACTURA >>", docRel.getIdDocumento());
						    			mapaResultado.put("MENSAJE", mensajeFinal);
						    			eliminarComplementoBoveda(con, esquema, uuidComplemento);
						    			actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "003", uuidComplemento);
										return mapaResultado;
						    			
						    		}/*else if (!jsonInfoUUID.get("FECHA_PAGO_HISTORICO").toString().equalsIgnoreCase(FECHA_PAGO.substring(0, 10))){
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE34), "<< FOLIO_FACTURA >>", docRel.getIdDocumento());
										mapaResultado.put("MENSAJE", mensajeFinal);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "004", uuidComplemento);
										return mapaResultado;
						    			
						    		}*/else {
						    			
						    		}
						    	}
						    	
						    	if (!Utils.noNulo(compleForm.getUuidComplemento()).equals("")) { // encontro informacion
							    	if (!uuidComplemento.equalsIgnoreCase(Utils.noNulo(compleForm.getUuidComplemento()))) { // ya esta asignado a otro complemento
										logger.info("*************** uuidComple_duplicado *******************" + uuidComplemento);
										MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
										String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE40), "<< UUID_COMPLEMENTO >>", uuidComplemento);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										mapaResultado.put("MENSAJE", mensajeFinal);
										if (bandEntro) { // se guarda en el historico de pagos
											actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "005", uuidComplemento);
										}
										return mapaResultado;
							    		
							    	}
						    	}
						    	
						    	
						    	nombreXML = uuidComplemento + ".xml";
						    	nombrePDF = uuidComplemento + ".pdf";
						    	mapaUUIDXML.put(docRel.getIdDocumento().toLowerCase(), docRel.getIdDocumento().toLowerCase()); // se van agregando los UUID del XML
						    	//int totGrabar = grabarComplementariaTrabajo(con, esquema, uuidComplemento, MONTO_FACTURA, MONTO_PAGADO, FECHA_PAGO, FECHA_TIMBRADO, estatusSAT, nombreXML, nombrePDF,  usuarioHTTP, docRel.IdDocumento(), TIPO_COMPROBANTE, FORMA_PAGO);
						    	int totGrabar = grabarComplementariaBoveda(con, esquema, uuidComplemento, docRel.getIdDocumento(),  MONTO_FACTURA, FECHA_PAGO, FECHA_TIMBRADO, estatusSAT, nombreXML, nombrePDF, usuarioHTTP, TIPO_COMPROBANTE, FORMA_PAGO);
						    	TOTAL_COMPLEMENTO+=MONTO_FACTURA;
						    }
						}
						
						// logger.info("totRelacionados---->"+totRelacionados);
						if (totRelacionados == 0) {
			    			// logger.info("*************** NO ENCONTRO NINGUN COMPLEMENTO PARA VALIDAR *******************");
			    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			    			String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE43); 
			    			mapaResultado.put("MENSAJE", mensajeFinal);
			    			return mapaResultado;
							
						}else {
							// 1. Guardar la informacion en complementaria_boveda
							String mensajeValidacion = "OK";
							if ("OK".equalsIgnoreCase(mensajeValidacion)) {
								String estatus_Conciliacion = "OK";
								actualizarTotalComplemento(con, esquema, TOTAL_COMPLEMENTO, estatus_Conciliacion, uuidComplemento);
								actualizarBoveda(con, esquema, uuidComplemento);
								actualizarHistoricoPagos(con, esquema, listaFacturas, "C02", null, uuidComplemento);
								mapaResultado.put("CUMPLE_OK", "OK");
								mapaResultado.put("MENSAJE", "OK");
								mapaResultado.put("UUID_COMPLEMENTO", uuidComplemento);
								mapaResultado.put("RAZON_SOCIAL", razonSocialXML);
								mapaResultado.put("ESTADO_SAT", estadoSAT);
								mapaResultado.put("ESTATUS_SAT", estatusSAT);
							}
						}
						
						return mapaResultado;
						
					}else if ( _comprobante != null && _comprobante.getTipoDeComprobante() != null &&
										!"P".equalsIgnoreCase(_comprobante.getTipoDeComprobante())){
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE42); 
						mapaResultado.put("MENSAJE", mensajeFinal);
		    			return mapaResultado;
		    			
					}else{
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32); 
						mapaResultado.put("MENSAJE", mensajeFinal);
		    			return mapaResultado;
					}
				}else if("2.0".equalsIgnoreCase(versionXML)) {
					logger.info("***************** else 4.0....");
					mapaResultado = procesarXML20(con, esquema, rutaXML, usuarioHTTP, versionXML, idLenguaje);
				}
				
		}catch(Exception e){
			Utils.imprimeLog("", e);
			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32); 
			mapaResultado.put("MENSAJE", mensajeFinal);
		}
		return mapaResultado;
	}
	

	
	@SuppressWarnings("unchecked")
	public JSONObject buscarOrden(Connection con, String esquema, String uuid)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        
        try {
// A.ID_REGISTRO, A.SUB_TOTAL, A.FECHA_FACTURA, A.EMISOR_RFC, A.DES_TIPO_MONEDA, A.TOTAL, B.RFC, B.FECHA_PAGO, B.TIPO_MONEDA, B.TOTAL
        	
            stmt = con.prepareStatement( BovedaQuerys.getBuscarBoveda(esquema));
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            DecimalFormat decimal = new DecimalFormat("###,###.##");
			if(rs.next()) {
					jsonobj.put("MONTO",decimal.format(rs.getDouble(2)));
					jsonobj.put("FECHA_PAGO",Utils.noNulo(rs.getString(3)));
					jsonobj.put("RFC_FACTURA",Utils.noNulo(rs.getString(4)));
					jsonobj.put("TIPO_MONEDA_FACTURA",Utils.noNulo(rs.getString(5)));
					jsonobj.put("TOTAL_FACTURA",rs.getDouble(6));
					jsonobj.put("RFC_HISTORICO",Utils.noNulo(rs.getString(7)));
					jsonobj.put("FECHA_PAGO_HISTORICO",Utils.noNulo(rs.getString(8)));
					jsonobj.put("TIPO_MONEDA_HISTORICO",Utils.noNulo(rs.getString(9)));
					jsonobj.put("TOTAL_HISTORICO",Utils.noNulo(rs.getString(10)));
					
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
	
	
	
	public int grabarComplementariaBoveda(Connection con, String esquema, String uuid_Complemento, String uuid_Factura, 
				    double MONTO_FACTURA, String FECHA_PAGO, String FECHA_TIMBRADO, String estatus_COMPLEMENTO, 
					String nombrXML, String nombrePDF, String usuarioHTTP, String TIPO_COMPROBANTE, String FORMA_PAGO)
		    {
		        PreparedStatement stmt = null;
		        ResultSet rs = null;
		        int totGrabar = 0;
		        try
		        {
		            stmt = con.prepareStatement( BovedaQuerys.getGuardarComplementariaBoveda(esquema));
		            stmt.setString(1, uuid_Complemento);
		            stmt.setString(2, uuid_Factura);
		            stmt.setDouble(3, MONTO_FACTURA);
		            stmt.setString(4, estatus_COMPLEMENTO);
		            stmt.setString(5, FECHA_PAGO);
		            stmt.setString(6, FECHA_TIMBRADO);
		            stmt.setString(7, nombrXML);
		            stmt.setString(8, nombrePDF);
		            stmt.setString(9, TIPO_COMPROBANTE);
		            stmt.setString(10, FORMA_PAGO);
		            stmt.setString(11, "OK");
		            stmt.setString(12, usuarioHTTP);
		            totGrabar = stmt.executeUpdate();
		            
		        }catch(SQLException sql) {
		        	logger.info("Error Code......."+sql.getErrorCode());
		        	if (sql.getErrorCode() == 1062 || sql.getErrorCode() == -1062) {
		        		totGrabar = 1062;
		        	}
		        	//Utils.imprimeLog("", sql);
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
		        return totGrabar;
		    }
			
	
	public int actualizarHistoricoPagos(Connection con, String esquema, String uuidFactura, String estatus, String codigoError, String uuidComplemento)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totGrabar = 0;
        try {
            stmt = con.prepareStatement( BovedaQuerys.getActualizarHistorialPagos(esquema));
            stmt.setString(1, estatus);
            stmt.setString(2, codigoError);
            stmt.setString(3, uuidComplemento);
            stmt.setString(4, uuidFactura);
            totGrabar = stmt.executeUpdate();
        } catch(Exception e){
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
        return totGrabar;
    }
	
	public int actualizarHistoricoPagos(Connection con, String esquema, ArrayList<String> listaFacturas, String estatus, String codigoError, String uuidComplemento)
	{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totGrabar = 0;
        try {
        	
        	stmt = con.prepareStatement( BovedaQuerys.getActualizarHistorialPagos(esquema));
            stmt.setString(1, estatus);
            stmt.setString(2, codigoError);
            stmt.setString(3, uuidComplemento);
            
            for (String uuidFactura : listaFacturas) {
            	stmt.setString(4, uuidFactura);
                totGrabar = stmt.executeUpdate();	
            }
        	
        } catch(Exception e){
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
        return totGrabar;
    }
	
	public int actualizarHistoricoPagosCancelado(Connection con, String esquema, Comprobante _comprobante, String estatus, String codigoError, String uuidComplemento)
	{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totGrabar = 0;
        try {
        	ArrayList<String> listaFacturas = new ArrayList<String>();
        	String versionXML = null;
			if (_comprobante.getComplemento().getPagos20() != null) {
				versionXML = _comprobante.getComplemento().getPagos20().getVersion();
				int totComprobantes = _comprobante.getComplemento().getPagos20().getPago().size();
				for (int x = 0; x < totComprobantes; x++) {
					com.itextpdf.xmltopdf.Pagos20.Pago pago =  _comprobante.getComplemento().getPagos20().getPago().get(x);
					// pago.getDoctoRelacionado()
					for (DoctoRelacionado doctoRelacionado : pago.getDoctoRelacionado()) {
						listaFacturas.add(doctoRelacionado.getIdDocumento());
					}
				}
				
			}else {
				versionXML = _comprobante.getComplemento().getPagos().getVersion();
				int totComprobantes = _comprobante.getComplemento().getPagos().getPago().size();
				for (int x = 0; x < totComprobantes; x++) {
					com.itextpdf.xmltopdf.Pagos.Pago pago =  _comprobante.getComplemento().getPagos().getPago().get(x);
					// pago.getDoctoRelacionado()
					for (PDoctoRelacionado doctoRelacionado : pago.getDoctoRelacionado()) {
						listaFacturas.add(doctoRelacionado.getIdDocumento());
					}
				}
				
			}
			stmt = con.prepareStatement( BovedaQuerys.getActualizarHistorialPagos(esquema));
            stmt.setString(1, estatus);
            stmt.setString(2, codigoError);
            stmt.setString(3, uuidComplemento);
            
            for (String uuidFactura : listaFacturas) {
            	stmt.setString(4, uuidFactura);
                totGrabar = stmt.executeUpdate();	
            }
        	
        } catch(Exception e){
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
        return totGrabar;
    }
	
	public int actualizarBoveda(Connection con, String esquema, String uuidComplemento)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totGrabar = 0;
        try {
            stmt = con.prepareStatement( BovedaQuerys.getActualizarConciliacion(esquema));
            stmt.setString(1, "S");
            stmt.setString(2, uuidComplemento);
            totGrabar = stmt.executeUpdate();
        } catch(Exception e){
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
        return totGrabar;
    }
	
	
	public int actualizarTotalComplemento(Connection con, String esquema, double MONTO_COMPLEMENTO, String estatusConciliacion, String uuidComplemento)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totGrabar = 0;
        try {
            stmt = con.prepareStatement( BovedaQuerys.getActualizarTotalComplemento(esquema));
            stmt.setDouble(1, MONTO_COMPLEMENTO);
            stmt.setString(2, estatusConciliacion);
            stmt.setString(3, uuidComplemento);
            totGrabar = stmt.executeUpdate();
        } catch(Exception e){
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
        return totGrabar;
    }
	
	public int eliminarComplementoBoveda(Connection con, String esquema, String uuidComplemento)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totGrabar = 0;
        try {
            stmt = con.prepareStatement( BovedaQuerys.getEliminarComplementoBoveda(esquema));
            stmt.setString(1, uuidComplemento);
            totGrabar = stmt.executeUpdate();
            
        } catch(Exception e){
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
        return totGrabar;
    }

	
	
	
	
	
	
	// PROCESO DE VINVULACION BOVEDA 2.0
	
	public HashMap<String, Object> procesarXML20(Connection con, String esquema, String rutaXML, String usuarioHTTP, String versionXML, String idLenguaje)
    {
    	//String mensajeXML = "";
    	HashMap<String, Object> mapaResultado = new HashMap<>();
		try{
			  mapaResultado.put("MENSAJE", "ERROR");
			  mapaResultado.put("rows", new JSONArray());

			  logger.info("Ruta XML: "+rutaXML);
			
				File fileXML = new File(rutaXML);
			    //JSONObject jsonInfoUUID = null;
			    
			    String nombreXML = null;
			    String nombrePDF = null;
			    
			    //LeerXML crea = null;
			    Comprobante _comprobante = null;
			    try {
			    	
			    	_comprobante = LeerXML.ObtenerComprobante(rutaXML);
			    }catch(Exception e) {
			    	Utils.imprimeLog("", e);
			    }
			    
				// logger.info("_comprobante------------>"+_comprobante);
				if (_comprobante == null) {
					MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
					String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32); 
					mapaResultado.put("MENSAJE", mensajeFinal);
	    			return mapaResultado;
				}
				
				if (_comprobante.getComplemento().Pagos20 != null && _comprobante.getComplemento().Pagos20.getPago().size() > 0) {
						
						if (!"P".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
							MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
							String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE42); 
							mapaResultado.put("MENSAJE", mensajeFinal);
			    			return mapaResultado;
						}else {
							HashMap<String, String> mapaMensajeComplemento = new ValidacionesComplemento().validarEtiquetasXMLCOMP(con, esquema, _comprobante, versionXML);
							logger.info("mapaMensajeComplemento ------------>" + mapaMensajeComplemento.get("ERROR"));
							if ("FALSE".equalsIgnoreCase(mapaMensajeComplemento.get("ERROR"))) {
								mapaResultado.put("MENSAJE", mapaMensajeComplemento.get("MENSAJE"));
				    			return mapaResultado;
							}
						}
						
						// se obtiene bandera de tipo de validacion
						// Termina
						  String rfcFacturaXML   = _comprobante.getEmisor().getRfc();
						    String razonSocialXML  = _comprobante.getEmisor().getNombre();
							String folioXML        = _comprobante.getFolio();
							String serieXML        = _comprobante.getSerie();
							String uuidComplemento = _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
							double totalXML       = _comprobante.getTotal();
							String rfcReceptorXML = _comprobante.getReceptor().getRfc();
							LocalDateTime FECHA_TIMBRADOLD = _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado();
							String TIPO_COMPROBANTE = _comprobante.getTipoDeComprobante();
							LocalDateTime FECHA_XMLLD  = _comprobante.getFecha();
							logger.info("rfcReceptorXML------------>"+rfcReceptorXML);
							String FECHA_TIMBRADO = null;
							if(FECHA_TIMBRADOLD != null) {
								FECHA_TIMBRADO = FECHA_TIMBRADOLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					    	}
							
							String FECHA_XML = null;
							if(FECHA_XMLLD != null) {
								FECHA_XML = FECHA_XMLLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					    	}
							
						// Son etiquetas parte de los pagos
						double MONTO_PAGADO = 0;
						LocalDateTime FECHA_PAGOLD = null;
						String FECHA_PAGO = "";
						String FORMA_PAGO = "";
						
					    // String validaCOMPLE = mapaConfig.get("RECHAZAR_COMPLE");
						String validaCOMPLE = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "RECHAZAR_COMPLE");
						
						logger.info("validaCOMPLE----->"+validaCOMPLE);
						String estadoSAT = "";
						String estatusSAT = "";
						// validaCOMPLE = "N";
						if ("S".equalsIgnoreCase(validaCOMPLE)) {
							
							String datosSAT [] = UtilsSAT.validaSAT(rfcFacturaXML, rfcReceptorXML, totalXML, uuidComplemento);
							//String datosSAT [] = {"Vigente","S - Comprobante obtenido satisfactoriamente."};
							estadoSAT = datosSAT[0];
							estatusSAT = datosSAT[1];
							
							logger.info("estado--->"+estadoSAT);
							logger.info("estatusSAT--->"+estatusSAT);
							
							if (estadoSAT.toUpperCase().indexOf("CANCELADO") > -1 ) {
								MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
								String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE29), "<< UUID_COMPLE >>", uuidComplemento); 
								mapaResultado.put("MENSAJE", mensajeFinal);
								actualizarHistoricoPagosCancelado(con, esquema, _comprobante, "C20", "006", uuidComplemento);
								
								return mapaResultado;
							}
						}//FOLIO_EMPRESA
						double MONTO_FACTURA = 0;
						List<DoctoRelacionado> listaDocto = null;
						DoctoRelacionado docRel = null;
						
						HashMap<String, String> mapaUUIDXML = new HashMap<>();
						int totComprobantes = _comprobante.getComplemento().Pagos20.getPago().size();
						int totRelacionados = 0;
						double TOTAL_COMPLEMENTO = 0;
						double TOTAL_FACTURAS = 0;
						JSONObject jsonInfoUUID = null;
						
						//String tipoMonedaDR = null;
						ComplementosForm compleForm = null;
						ArrayList<String> listaFacturas = new ArrayList<String>();
						for (int x = 0; x < totComprobantes; x++) {
							// Pago pago = _comprobante.getComplemento().Pagos20.getPago().get(x);
							com.itextpdf.xmltopdf.Pagos20.Pago pago =  _comprobante.getComplemento().getPagos20().getPago().get(x);

							FECHA_PAGOLD = pago.getFechaPago();

							if(FECHA_PAGOLD != null) {
								FECHA_PAGO = FECHA_PAGOLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					    	}
							
							
							// logger.info("Monto()---->"+pago.getMonto());
							MONTO_PAGADO = pago.getMonto();
							

							listaDocto = pago.getDoctoRelacionado();
							totRelacionados = listaDocto.size();

							
							
							// logger.info("totRelacionados---->"+totRelacionados);
							for (int y=0; y < totRelacionados; y++) {
								docRel = listaDocto.get(y);
						    	// logger.info("UUID ORDEN------------->"+docRel.getIdDocumento());
				    			// logger.info("ImpPagado()---->"+docRel.getImpPagado());
						    	// logger.info("MONTO_PAGADO---->"+MONTO_PAGADO);
								listaFacturas.add(docRel.getIdDocumento());
								
						    	jsonInfoUUID = buscarOrden(con, esquema, docRel.getIdDocumento());
						    	compleForm = buscarUUIDComple(con, esquema, docRel.getIdDocumento());
						    	if (jsonInfoUUID.isEmpty()) {
					    			// logger.info("*************** NO ENCONTRO INFORMACION *******************");
					    			eliminarComplementoBoveda(con, esquema, uuidComplemento);
					    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
					    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE38), "<< UUID_COMPLE >>", docRel.getIdDocumento()); 
					    			mapaResultado.put("MENSAJE", mensajeFinal);
					    			return mapaResultado;
						    	}
						    	
								boolean bandEntro = false;
						    	if (!jsonInfoUUID.get("RFC_HISTORICO").toString().equalsIgnoreCase("")) {
						    		bandEntro = true;
						    	}
						    	
						    	if (totRelacionados == 1) {
						    		MONTO_FACTURA = MONTO_PAGADO;	
						    	}else {
						    		MONTO_FACTURA = docRel.getImpPagado();
						    	}
						    	
						    	
						    	if (bandEntro) {
						    		if (!jsonInfoUUID.get("RFC_HISTORICO").toString().equalsIgnoreCase(_comprobante.getEmisor().getRfc())){
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE91), "<< UUID_COMPLEMENTO >>", uuidComplemento);
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "001", uuidComplemento);
										return mapaResultado;
										
						    		}else if (!jsonInfoUUID.get("TIPO_MONEDA_HISTORICO").toString().equalsIgnoreCase(docRel.getMonedaDR())) {
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE92), "<< UUID_FACTURA >>", docRel.getIdDocumento());
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "002", uuidComplemento);
										return mapaResultado;
						    			
						    		}else if (MONTO_FACTURA != Double.parseDouble(jsonInfoUUID.get("TOTAL_HISTORICO").toString())) {
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE93), "<< UUID_FACTURA >>", docRel.getIdDocumento());
						    			mapaResultado.put("MENSAJE", mensajeFinal);
						    			eliminarComplementoBoveda(con, esquema, uuidComplemento);
						    			actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "003", uuidComplemento);
										return mapaResultado;
						    			
						    		}else if (!jsonInfoUUID.get("FECHA_PAGO_HISTORICO").toString().equalsIgnoreCase(FECHA_PAGO.substring(0, 10))){
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE34), "<< FOLIO_FACTURA >>", docRel.getIdDocumento());
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "004", uuidComplemento);
										return mapaResultado;
						    			
						    		}else {
							    	
						    		}
						    	}else {
						    		if (!jsonInfoUUID.get("RFC_FACTURA").toString().equalsIgnoreCase(_comprobante.getEmisor().getRfc())){
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE91), "<< UUID_FACTURA >>", docRel.getIdDocumento());
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "001", uuidComplemento);
										return mapaResultado;
										
						    		}else if (!jsonInfoUUID.get("TIPO_MONEDA_FACTURA").toString().equalsIgnoreCase(docRel.getMonedaDR())) {
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE92), "<< UUID_FACTURA >>", docRel.getIdDocumento());
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "002", uuidComplemento);
										return mapaResultado;
						    			
						    		}else if (MONTO_FACTURA != Double.parseDouble(jsonInfoUUID.get("TOTAL_FACTURA").toString())) {
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE93), "<< UUID_FACTURA >>", docRel.getIdDocumento());
						    			mapaResultado.put("MENSAJE", mensajeFinal);
						    			eliminarComplementoBoveda(con, esquema, uuidComplemento);
						    			actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "003", uuidComplemento);
										return mapaResultado;
						    			
						    		}/*else if (!jsonInfoUUID.get("FECHA_PAGO_HISTORICO").toString().equalsIgnoreCase(FECHA_PAGO.substring(0, 10))){
						    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						    			String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE34), "<< FOLIO_FACTURA >>", docRel.getIdDocumento());
										mapaResultado.put("MENSAJE", mensajeFinal);
										actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "004", uuidComplemento);
										return mapaResultado;
						    			
						    		}*/else {
						    			
						    		}
						    	}
						    	
						    	if (!Utils.noNulo(compleForm.getUuidComplemento()).equals("")) { // encontro informacion
							    	if (!uuidComplemento.equalsIgnoreCase(Utils.noNulo(compleForm.getUuidComplemento()))) { // ya esta asignado a otro complemento
										logger.info("*************** uuidComple_duplicado *******************" + uuidComplemento);
										MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
										String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE40), "<< UUID_COMPLEMENTO >>", uuidComplemento);
										mapaResultado.put("MENSAJE", mensajeFinal);
										eliminarComplementoBoveda(con, esquema, uuidComplemento);
										if (bandEntro) { // se guarda en el historico de pagos
											actualizarHistoricoPagos(con, esquema, docRel.getIdDocumento(), "C20", "005", uuidComplemento);
										}
										return mapaResultado;
							    		
							    	}
						    	}
						    	
						    	
						    	nombreXML = uuidComplemento + ".xml";
						    	nombrePDF = uuidComplemento + ".pdf";
						    	mapaUUIDXML.put(docRel.getIdDocumento().toLowerCase(), docRel.getIdDocumento().toLowerCase()); // se van agregando los UUID del XML
						    	//int totGrabar = grabarComplementariaTrabajo(con, esquema, uuidComplemento, MONTO_FACTURA, MONTO_PAGADO, FECHA_PAGO, FECHA_TIMBRADO, estatusSAT, nombreXML, nombrePDF,  usuarioHTTP, docRel.IdDocumento(), TIPO_COMPROBANTE, FORMA_PAGO);
						    	int totGrabar = grabarComplementariaBoveda(con, esquema, uuidComplemento, docRel.getIdDocumento(),  MONTO_FACTURA, FECHA_PAGO, FECHA_TIMBRADO, estatusSAT, nombreXML, nombrePDF, usuarioHTTP, TIPO_COMPROBANTE, FORMA_PAGO);
						    	TOTAL_COMPLEMENTO+=MONTO_FACTURA;
						    	//TOTAL_FACTURAS+= Double.parseDouble( jsonInfoUUID.get("TOTAL_FACTURA").toString());
						    }
						}
						
						// logger.info("totRelacionados---->"+totRelacionados);
						if (totRelacionados == 0) {
			    			// logger.info("*************** NO ENCONTRO NINGUN COMPLEMENTO PARA VALIDAR *******************");
			    			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			    			String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE43); 
			    			mapaResultado.put("MENSAJE", mensajeFinal);
			    			return mapaResultado;
							
						}else {
							// 1. Guardar la informacion en complementaria_boveda
							String mensajeValidacion = "OK";
							if ("OK".equalsIgnoreCase(mensajeValidacion)) {
								String estatus_Conciliacion = "OK";
								actualizarTotalComplemento(con, esquema, TOTAL_COMPLEMENTO, estatus_Conciliacion, uuidComplemento);
								actualizarBoveda(con, esquema, uuidComplemento);
								actualizarHistoricoPagos(con, esquema, listaFacturas, "C02", null, uuidComplemento);
								mapaResultado.put("CUMPLE_OK", "OK");
								mapaResultado.put("MENSAJE", "OK");
								mapaResultado.put("UUID_COMPLEMENTO", uuidComplemento);
								mapaResultado.put("RAZON_SOCIAL", razonSocialXML);
								mapaResultado.put("ESTADO_SAT", estadoSAT);
								mapaResultado.put("ESTATUS_SAT", estatusSAT);
							}
						}
						
						return mapaResultado;
						
					} else if ( _comprobante != null && _comprobante.getTipoDeComprobante() != null &&
										!"P".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE42); 
						mapaResultado.put("MENSAJE", mensajeFinal);
		    			return mapaResultado;
		    			
					}else{
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32); 
						mapaResultado.put("MENSAJE", mensajeFinal);
		    			return mapaResultado;
					}
		}catch(Exception e){
			Utils.imprimeLog("", e);
			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32); 
			mapaResultado.put("MENSAJE", mensajeFinal);
		}
		return mapaResultado;
	}
	
	
	
	public ComplementosForm buscarUUIDComple(Connection con, String esquema, String uuidFactura) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ComplementosForm compleForm = new ComplementosForm();

		try {
			stmt = con.prepareStatement(BovedaQuerys.getBuscarComplementoUUID(esquema));
			stmt.setString(1, uuidFactura);
			rs = stmt.executeQuery();
			if (rs.next()) {
				compleForm.setUuidComplemento(Utils.noNulo(rs.getString(1)));
				compleForm.setEstatusComplemento(Utils.noNulo(rs.getString(2)));

			}
		} catch (Exception e) {
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
		return compleForm;
	}
	
	
	
	
}
