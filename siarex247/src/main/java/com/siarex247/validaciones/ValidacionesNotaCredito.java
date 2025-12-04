package com.siarex247.validaciones;

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

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.utils.MensajesSIAREX;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.visor.VisorOrdenes.NotaCreditoForm;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;

public class ValidacionesNotaCredito {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public HashMap<String, Object> procesarXML(Connection con, String esquemaEmpresa, String nombreRepositorio, File fileXML, File filePDF, String pathRepositorio, String usuarioHTTP, String idLenguaje, boolean bandElimnaArchivos)
    {
    	//String mensajeXML = "";
    	HashMap<String, Object> mapaResultado = new HashMap<>();
    	
    	JSONObject datosXML = null;
    	
		try{
			  mapaResultado.put("MENSAJE", "ERROR");
			  mapaResultado.put("rows", new JSONArray());
			  
			  //JSONObject jsonInfoUUID = null;
			  String nombreXML = null;
			  String nombrePDF = null;
			  JSONArray jsonArray = obtenerUUID(fileXML);	
			  
			 // logger.info("jsonArray===>"+jsonArray);
			  if (jsonArray == null || jsonArray.size() == 0) { // error en el XML
				  //logger.info("************* ENTRO AQUI ************"+idLenguaje);
					MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
					//String mensajeFinal = "Estimado proveedor le informamos que su nota de credito, no pudo ser procesada con (e)xito debido a que la informaci(o)n proporcionada en el archivo .XML es incorrecta, favor de proporcionar o validar su archivo .XML de nota de credito.";					
					String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE46);
					mapaResultado.put("MENSAJE", mensajeFinal);
	    			return mapaResultado;
				}

				// se obtiene bandera de tipo de validacion
			  HashMap<String, String> mapaConf = ConfigAdicionalesBean.obtenerConfiguraciones(con, esquemaEmpresa);
			// Termina
			     datosXML = (JSONObject) jsonArray.get(0);
			     
				 String rfcFacturaXML  = Utils.noNulo(datosXML.get("RFC_XML")).toString();
				 String razonSocialXML = Utils.noNulo(datosXML.get("NOMBRE_XML")).toString();
				// String folioXML       = Utils.noNulo(datosXML.get("FOLIO_XML")).toString();
				// String serieXML       = Utils.noNulo(datosXML.get("SERIE_XML")).toString();
				 String uuidXML        = Utils.noNulo(datosXML.get("UUID_XML")).toString();
				 String totalXML        = datosXML.get("TOTAL_XML").toString();
				 String rfcReceptorXML = Utils.noNulo(datosXML.get("RFCRECEPTOR_XML")).toString();
				 String FECHA_TIMBRADO = Utils.noNulo(datosXML.get("FECHA_TIMBRADO")).toString();
				 String TIPO_COMPROBANTE = Utils.noNulo(datosXML.get("TIPO_COMPROBANTE")).toString();
				 String FECHA_XML  = Utils.noNulo(datosXML.get("FECHA_XML")).toString();
				 String UUID_RELACIONADO = Utils.noNulo(datosXML.get("UUID_RELACIONADO")).toString();
				 String FORMA_PAGO = Utils.noNulo(datosXML.get("FORMA_PAGO")).toString();
				 String TIPO_MONEDA = Utils.noNulo(datosXML.get("TIPO_MONEDA")).toString();
				 double SUBTOTAL_XML = Utils.noNuloDouble(datosXML.get("SUBTOTAL_XML"));
				 double IVA_XML = Utils.noNuloDouble(datosXML.get("IVA_XML"));
				 double ISR_RET_XML = Utils.noNuloDouble(datosXML.get("ISR_RET_XML"));
					
				 // logger.info("ISR_RET_XML===>"+ISR_RET_XML);
				 
				 String validaNOTA = mapaConf.get("VALIDAR_NOTAS");
				 //validaNOTA = "N";
				 /*
				 logger.info("***********  VALIDACION EN EL SAT ****************");
					logger.info("validaCOMPLE----->"+validaNOTA);
					logger.info("rfcFacturaXML----->"+rfcFacturaXML);
					logger.info("rfcReceptorXML----->"+rfcReceptorXML);
					logger.info("totalXML----->"+totalXML);
					logger.info("uuidXML----->"+uuidXML);
					logger.info("tipoMoneda----->"+TIPO_MONEDA);
					logger.info("ISR_RET_XML----->"+ISR_RET_XML);
				*/	
					String estadoSAT = "";
					String estatusSAT = "";
					double totalFactura = 0;
					try {
						totalFactura = Double.parseDouble(totalXML);
					}catch(Exception e) {
						totalFactura = 0;
					}
					
					try {
						
						String datosSAT [] = UtilsSAT.validaSAT(rfcFacturaXML, rfcReceptorXML, totalFactura, uuidXML);
						estadoSAT = datosSAT[0];
						estatusSAT = datosSAT[1];
						logger.info("estado--->"+estadoSAT);
						logger.info("estatusSAT--->"+estatusSAT);
					}catch(Exception e) {
						Utils.imprimeLog("", e);
					}
					//logger.info("***********  TERMINA ****************");
					if ("S".equalsIgnoreCase(validaNOTA)) {
						if (estadoSAT.toUpperCase().indexOf("CANCELADO") > -1 ) {
							MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
							//String mensajeFinal = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que esta no se encuentra vigente ante el SAT"; 
							String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE47);
							mapaResultado.put("MENSAJE", mensajeFinal);
							return mapaResultado;
						}else if (estadoSAT.toUpperCase().indexOf("NO ENCONTRADO") > -1) {
							MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
							//String mensajeFinal = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que esta no se encuentra ante el SAT";
							String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE47);
							mapaResultado.put("MENSAJE", mensajeFinal);
							return mapaResultado;
						}
					}//FOLIO_EMPRESA
					//estadoSAT = "Aceptado";
					//estatusSAT = "S : Registro obtenido Satisfactoriamente.";
					/*
					logger.info("************** DATOS PARA VALIDAR LA NOTA DE CREDITO ************");
					logger.info("rfcFacturaXML---->"+rfcFacturaXML);
					logger.info("UUID_RELACIONADO---->"+UUID_RELACIONADO);
					logger.info("uuidXML---->"+uuidXML);
					logger.info("totalXML---->"+totalXML);
					logger.info("subTotalXML---->"+SUBTOTAL_XML);
					logger.info("ivaXML---->"+IVA_XML);
					
					logger.info("TIPO_COMPROBANTE---->"+TIPO_COMPROBANTE);
					logger.info("******************************************************************");
					*/

					/*
					 String tipoMonedaBD = "";
					try{
						String cadMoneda = UtilsValidaciones.validaDesMoneda(TIPO_MONEDA);
							if (cadMoneda.indexOf(";") > -1){
								String arrMoneda [] = cadMoneda.split(";");
								tipoMonedaBD = arrMoneda[1];
							}
					}catch(Exception e){
						tipoMonedaBD = "SIN_MONEDA";
					}
					
					*/
					
					eliminarNotaCredito(con, esquemaEmpresa, uuidXML, "PRE"); // Se elimina si el UUID del XML fue procesado, pero no aceptado.
					VisorOrdenesForm visorForm = buscarOrden(con, esquemaEmpresa, UUID_RELACIONADO);
					NotaCreditoForm notaForm = buscarNota(con, esquemaEmpresa, UUID_RELACIONADO);
					//logger.info("************** ANTES DE LAS VALIDACIONES ******************");
					if ("SIN_NODO".equalsIgnoreCase(UUID_RELACIONADO)) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						//String mensajeFinal = "Estimado proveedor le informamos que su nota de credito no pudo ser procesado exitosamente, debido a que en el archivo .xml de la nota de credito deben de contener la etiqueta o nodo de nombre \"CfdiRelacionado\", validar que en su archivo .xml se incluya esta etiqueta."; 
						String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE48);
						mapaResultado.put("MENSAJE", mensajeFinal);
						return mapaResultado;
					}else if (!"E".equalsIgnoreCase(TIPO_COMPROBANTE)) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						//String mensajeFinal = "Estimado proveedor le informamos que su archivo .XML, no puede ser procesado con (e)xito debido a que la informaci(o)n proporcionada no corresponde a una Nota de Credito, favor de verificar la informaci(o)n e intentarlo de nuevo.";
						String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE49); 
						mapaResultado.put("MENSAJE", mensajeFinal);
						return mapaResultado;
					}else if (visorForm.getFolioEmpresa() == 0) { // busca el folio fiscal, de la orden de compra
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						//String mensajeFinal = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que el folio fiscal relacionado no se encuentra registrado en nuestra base de datos";
						String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE50),"<< UUID_INCLUIDO >>", UUID_RELACIONADO);
						mapaResultado.put("MENSAJE", mensajeFinal);
						return mapaResultado;
					}else if ("A6".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A10".equalsIgnoreCase(visorForm.getEstatusOrden()) || "A11".equalsIgnoreCase(visorForm.getEstatusOrden())) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeFinal = "";
						if ("A6".equalsIgnoreCase(visorForm.getEstatusOrden())) {
							mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE87),"<< FOLIO_FACTURA >>", Utils.noNulo(visorForm.getFolioEmpresa()).toString());
						}else if ("A10".equalsIgnoreCase(visorForm.getEstatusOrden())) {
							mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE88),"<< FOLIO_FACTURA >>", Utils.noNulo(visorForm.getFolioEmpresa()).toString());
						}else if ("A11".equalsIgnoreCase(visorForm.getEstatusOrden())) {
							mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE89),"<< FOLIO_FACTURA >>", Utils.noNulo(visorForm.getFolioEmpresa()).toString());
						}else {
							mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE90),"<< FOLIO_FACTURA >>", Utils.noNulo(visorForm.getFolioEmpresa()).toString());
						}
						
						mapaResultado.put("MENSAJE", mensajeFinal);
						return mapaResultado;
					}else if ( notaForm.getFolioOrden() > 0 ) { // si no es vacio, encontro un registro asignado
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String uuidAsociado = notaForm.getUuidNotaCredito();
						// String mensajeFinal = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que el folio fiscal << UUID_RELACIONADO >> asignado a su factura ya fue procesado y asignado al folio fiscal de la nota de credito << UUID_ASIGNADO >>"
						String mensajeFinal = Utils.getMensajeValidacion(Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE51), "<< UUID_RELACIONADO >>", UUID_RELACIONADO),
													"<< UUID_ASIGNADO >>",uuidAsociado); 
						mapaResultado.put("MENSAJE", mensajeFinal);
						return mapaResultado;
					}else if (!TIPO_MONEDA.equalsIgnoreCase(visorForm.getTipoMoneda())) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						//String mensajeFinal = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que el tipo de moneda registrado en nuestra base de datos es diferente al de su archivo .XML"
						String mensajeFinal = Utils.getMensajeValidacion( Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE54), "<< UUID_ASIGNADO >>", uuidXML); 
						mapaResultado.put("MENSAJE", mensajeFinal);
					}else { // PASO LAS VALIDACIONES
						nombreXML = "NOT_"+ rfcFacturaXML+ uuidXML + ".xml";
						nombrePDF = "NOT_"+ rfcFacturaXML+ uuidXML + ".pdf";
						
						//logger.info("Nombre del XML Final : "+nombreXML);
						//logger.info("Nombre del PDF Final : "+nombrePDF);
						
						int totGrabar = grabarNotaCredito(con, esquemaEmpresa, uuidXML, Double.parseDouble(totalXML), FECHA_XML, FECHA_TIMBRADO, nombreXML, nombrePDF, TIPO_COMPROBANTE, FORMA_PAGO, usuarioHTTP, UUID_RELACIONADO, estadoSAT, estatusSAT, SUBTOTAL_XML, IVA_XML, ISR_RET_XML);
						if (totGrabar == 1062) { // esta duplicado
							MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
							//String mensajeFinal = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que el folio fiscal relacionado ya se encuentra procesado y asignado";
							String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE52);
							mapaResultado.put("MENSAJE", mensajeFinal);
							return mapaResultado;
						}else {
							String rutaDesXML = null;
							String rutaDesPDF = null;
			    			File fdesXML = null;
						    File fdesPDF = null;
						    String directorioPDF = null;
						    String directorioXML = null;

						    directorioXML = nombreRepositorio + "/PROVEEDORES/" + visorForm.getClaveProveedor() + "/" + nombreXML;
						    directorioPDF = nombreRepositorio + "/PROVEEDORES/" + visorForm.getClaveProveedor() + "/" + nombrePDF;

							rutaDesXML = pathRepositorio + directorioXML;
						    fdesXML = new File(rutaDesXML);
						    UtilsFile.moveFileDirectory(fileXML, fdesXML, true, false, true, bandElimnaArchivos);
							
							rutaDesPDF = pathRepositorio + directorioPDF;
							fdesPDF = new File(rutaDesPDF);
							//logger.info("Ruta Final PDF : "+rutaDesPDF);
							//logger.info("Ruta Final XML : "+rutaDesXML);
							
						    UtilsFile.moveFileDirectory(filePDF, fdesPDF, true, false, true, bandElimnaArchivos);
							mapaResultado.put("MENSAJE", "OK");
							mapaResultado.put("RAZON_SOCIAL", razonSocialXML);
							mapaResultado.put("UUID_NOTA", uuidXML);
							mapaResultado.put("ESTADO_SAT", estadoSAT);
							mapaResultado.put("ESTATUS_SAT", estatusSAT);
							mapaResultado.put("CLAVE_PROVEEDOR", visorForm.getClaveProveedor());
						}
						
					}
				 
				 
		}catch(Exception e){
			Utils.imprimeLog("", e);
			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE61); 
			mapaResultado.put("MENSAJE", mensajeFinal);
		}
		return mapaResultado;
		
    }

	
	
	@SuppressWarnings("unchecked")
	private JSONArray obtenerUUID(File fileNotaCredito) {
    	JSONArray jsonArray = new JSONArray();
    	Comprobante _comprobante = null;
		try {
		    
			_comprobante = LeerXML.ObtenerComprobante(fileNotaCredito.getAbsolutePath());
		    
		    JSONObject datosXML = new JSONObject();
		    
		    /*
			String rfcFacturaXML  = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Emisor/@Rfc");
			String nombreFacturaXML  = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Emisor/@Nombre");
			String rfcReceptorXML = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Receptor/@Rfc");
			String folioXML       = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Folio");
			String serieXML       = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Serie");
			String uuidXML        = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/TimbreFiscalDigital/@UUID");
			String totalXML       = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Total");
			String subTotalXML       = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@SubTotal");
			String ivaXML = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Impuestos/@TotalImpuestosTrasladados");
			String isrRetXML = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Impuestos/@TotalImpuestosRetenidos");
			String fechaTimbrado  = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/TimbreFiscalDigital/@FechaTimbrado");
			String tipoComprobante   = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@TipoDeComprobante");
			String formaPago      = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@FormaPago");
			String fechaXML       = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Fecha");
			String uuidRelacionado= XMLXPathManagerBase.getNodeValue(document, "/Comprobante/CfdiRelacionados/CfdiRelacionado/@UUID");
			String tipoMoneda       = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Moneda");
			*/
		    
		    String rfcFacturaXML  = _comprobante.getEmisor().getRfc();
			String nombreFacturaXML  = _comprobante.getEmisor().getNombre();
			String rfcReceptorXML = _comprobante.getReceptor().getRfc();
			String folioXML       = _comprobante.getFolio();
			String serieXML       = _comprobante.getSerie();
			String uuidXML        = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID();
			double totalXML       = _comprobante.getTotal();
			double subTotalXML       = _comprobante.getSubTotal();
			double ivaXML = _comprobante.getImpuestos().getTotalImpuestosTrasladados();
			double isrRetXML = _comprobante.getImpuestos().getTotalImpuestosRetenidos();
			
			if (isrRetXML == -1) {
				isrRetXML = 0;
			}
			if (ivaXML == -1) {
				ivaXML = 0;
			}
			
			LocalDateTime fechaTimbradoXML  = _comprobante.getComplemento().getTimbreFiscalDigital().getFechaTimbrado();
			String tipoComprobante   = _comprobante.getTipoDeComprobante();
			String formaPago      = _comprobante.getFormaPago();
			LocalDateTime fechaXMLLocal       = _comprobante.getFecha();
			String uuidRelacionado = "";
			if (_comprobante.getCfdiRelacionados().getCfdiRelacionado().size() > 0) {
				uuidRelacionado= _comprobante.getCfdiRelacionados().getCfdiRelacionado().get(0).getUUID();	
			}
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			String fechaTimbrado = "";
			if(fechaTimbradoXML != null) {
				fechaTimbrado = fechaTimbradoXML.format(formatter);
	    	}
			
			String fechaXML = "";
			if(fechaXMLLocal != null) {
				fechaXML = fechaXMLLocal.format(formatter);
	    	}
			
			String tipoMoneda       = _comprobante.getMoneda();
			
			logger.info("uuidRelacionado---->"+uuidRelacionado);
			if ("".equals(uuidRelacionado)) {
				uuidRelacionado = "SIN_NODO";
			}
			datosXML.put("RFC_XML", rfcFacturaXML);
			datosXML.put("NOMBRE_XML", nombreFacturaXML);
			datosXML.put("FOLIO_XML", folioXML);
			datosXML.put("SERIE_XML", serieXML);
			datosXML.put("UUID_XML", uuidXML);
			datosXML.put("TOTAL_XML", totalXML);
			datosXML.put("SUBTOTAL_XML", subTotalXML);
			datosXML.put("IVA_XML", ivaXML);
			datosXML.put("ISR_RET_XML", isrRetXML);
			datosXML.put("RFCRECEPTOR_XML", rfcReceptorXML);
			datosXML.put("TIPO_COMPROBANTE", tipoComprobante);
			datosXML.put("FORMA_PAGO", formaPago);
			datosXML.put("FECHA_XML", fechaXML.substring(0, 10));
			datosXML.put("UUID_RELACIONADO", uuidRelacionado);
			datosXML.put("TIPO_MONEDA", tipoMoneda);
			
			
			if (fechaTimbrado.length() >= 10) {
				datosXML.put("FECHA_TIMBRADO", fechaTimbrado);	
			}else {
				datosXML.put("FECHA_TIMBRADO", fechaTimbrado);
			}
			jsonArray.add(datosXML);
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return jsonArray;
	}

	
	public int eliminarNotaCredito(Connection con, String esquema, String uuidXML, String estatusNota)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totGrabar = 0;
        try{
            stmt = con.prepareStatement( ValidacionesNotaCreditoQuerys.getEliminarNotasCredito(esquema));
            stmt.setString(1, uuidXML);
            stmt.setString(2, estatusNota);
            totGrabar = stmt.executeUpdate();
            
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
        return totGrabar;
    }

	
	
	public VisorOrdenesForm buscarOrden(Connection con, String esquema, String uuid)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        VisorOrdenesForm visorForm = new VisorOrdenesForm();
        try{
        	
            stmt = con.prepareStatement( ValidacionesNotaCreditoQuerys.getBuscarOrdenUUIDCredito(esquema));
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            String estatusPago = null;
			if (rs.next()){
				estatusPago = Utils.noNulo(rs.getString(7));
				//if ("A1".equalsIgnoreCase(estatusPago) || "A3".equalsIgnoreCase(estatusPago) || "A4".equalsIgnoreCase(estatusPago) || "A11".equalsIgnoreCase(estatusPago) ) {
				if ("A1".equalsIgnoreCase(estatusPago) || "A3".equalsIgnoreCase(estatusPago) || "A4".equalsIgnoreCase(estatusPago) 
						|| "A6".equalsIgnoreCase(estatusPago) || "A9".equalsIgnoreCase(estatusPago) || "A10".equalsIgnoreCase(estatusPago) 
							|| "A11".equalsIgnoreCase(estatusPago)) {
					visorForm.setFolioOrden(rs.getLong(1));
					visorForm.setFolioEmpresa(rs.getLong(2));
					visorForm.setMonto(decimal.format(rs.getDouble(3)));
					visorForm.setFechaPago(Utils.noNulo(rs.getString(4)));
					visorForm.setClaveProveedor(rs.getInt(5));
					visorForm.setTipoOrden(Utils.noNulo(rs.getString(6)));
					visorForm.setEstatusOrden(estatusPago);
					visorForm.setTipoMoneda(Utils.noNulo(rs.getString(8)));
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
        return visorForm;
    }
	
	
	
	public NotaCreditoForm buscarNota(Connection con, String esquema, String uuidXML)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        NotaCreditoForm notaForm = new NotaCreditoForm();
        try{
            stmt = con.prepareStatement( ValidacionesNotaCreditoQuerys.getBuscarUUIDCargado(esquema));
            stmt.setString(1, uuidXML);
            rs = stmt.executeQuery();
			if (rs.next()){
				notaForm.setFolioOrden(rs.getLong(1));
				notaForm.setUuidNotaCredito(rs.getString(2));
				// jsonLista.put("FOLIO_ORDEN", rs.getLong(1));
				// jsonLista.put("UUID_CREDITO", rs.getString(2));
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
        return notaForm;
    }
	
	
	public int grabarNotaCredito(Connection con, String esquema, String uuid_nota, double TOTAL_NOTA, String FECHA_PAGO, String FECHA_TIMBRADO,  
			String nombrXML, String nombrePDF, String TIPO_COMPROBANTE, String FORMA_PAGO, String usuarioHTTP, String uuid_Factura, String estadoSAT, String estatusSAT, double SUBTOTAL_XML, double IVA_XML, double ISRET_XML)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totGrabar = 0;
        try
        {
        	
            stmt = con.prepareStatement( ValidacionesNotaCreditoQuerys.getGrabarNota(esquema));
            stmt.setString(1, uuid_nota);
            stmt.setDouble(2, TOTAL_NOTA);
            stmt.setString(3, "PRE");
            stmt.setString(4, FECHA_PAGO);
            stmt.setString(5, FECHA_TIMBRADO);
            stmt.setString(6, nombrXML);
            stmt.setString(7, nombrePDF);
            stmt.setString(8, TIPO_COMPROBANTE);
            stmt.setString(9, FORMA_PAGO);
            stmt.setString(10, estadoSAT);
            stmt.setString(11, estatusSAT);
            stmt.setString(12, usuarioHTTP);
            stmt.setDouble(13, SUBTOTAL_XML);
            stmt.setDouble(14, IVA_XML);
            stmt.setDouble(15, ISRET_XML);
            stmt.setString(16, uuid_Factura);
            
            totGrabar = stmt.executeUpdate();
            logger.info("stmt......."+stmt);
            
        }catch(SQLException sql) {
        	logger.info("Error Code......."+sql.getErrorCode());
        	Utils.imprimeLog("", sql);
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

	
	
	
	public ArrayList<NotaCreditoForm> detalleXMLNotaCredito(Connection con, String esquema, String uuid_XML)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        NotaCreditoForm notaForm = new NotaCreditoForm();
        ArrayList<NotaCreditoForm> listaDetalle = new ArrayList<>();
        try{ 
            stmt = con.prepareStatement( ValidacionesNotaCreditoQuerys.getDetalleNotasCredito(esquema));
            stmt.setString(1, uuid_XML);
            rs = stmt.executeQuery();
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            double totOrden = 0;
            double totComplemento = 0;
			while(rs.next()){
				totOrden = rs.getDouble(2);
				totComplemento = rs.getDouble(3);
				notaForm.setFolioEmpresa(rs.getLong(1));
				notaForm.setMontoOrden(Utils.noNulo(decimal.format(totOrden)));
				notaForm.setMontoNota(Utils.noNulo(decimal.format(totComplemento)));
				notaForm.setUuidOrden(Utils.noNulo(rs.getString(4)));
				notaForm.setFechaPago(Utils.noNulo(rs.getString(5)));
				
				if (totOrden == totComplemento ) {
					notaForm.setBandNota("true");
				}else {
					notaForm.setBandNota("false");
				}
				listaDetalle.add(notaForm);
				notaForm = new NotaCreditoForm();
				
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
	
	
	
	public int actualizaEstatus(Connection con, String esquema, String uuid_XML, String usuarioHTTP)
    {
        PreparedStatement stmt = null;
        PreparedStatement stmtOrdenes = null;
        int totGrabar = 0;
        try{
        	
            stmt = con.prepareStatement( ValidacionesNotaCreditoQuerys.getActualizarCredito(esquema));
            stmt.setString(1, "OK");
            stmt.setString(2, uuid_XML);
            // logger.info("stmt===>"+stmt);
            totGrabar = stmt.executeUpdate();
            if (totGrabar > 0) {
            	stmtOrdenes = con.prepareStatement( ValidacionesNotaCreditoQuerys.getActualizarOrdenes(esquema));
            	stmtOrdenes.setString(1, "A11");
            	stmtOrdenes.setString(2, usuarioHTTP);
            	stmtOrdenes.setString(3, uuid_XML);
                stmtOrdenes.executeUpdate();
            }
        }catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	       try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(stmtOrdenes != null)
	            	stmtOrdenes.close();
	            stmtOrdenes = null;
	            
	       }catch(Exception e){
	            stmt = null;
	       }
        }
        return totGrabar;
    }
	
	
	
	public NotaCreditoForm buscarNotaCredito(Connection con, String esquema, long folioOrden)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        NotaCreditoForm notaForm = new NotaCreditoForm();
	        try
	        {
	            stmt = con.prepareStatement(ValidacionesNotaCreditoQuerys.getBuscarInfoNotaCredito(esquema));
	            stmt.setLong(1, folioOrden);
	            
	            logger.info("stmtNota===>"+stmt);
	            rs = stmt.executeQuery();
	            if (rs.next()){
	            	DecimalFormat decimal = new DecimalFormat("###,###.##");
	            	notaForm.setFolioOrden(folioOrden);
	            	notaForm.setFolioEmpresa(rs.getLong(1));
	            	notaForm.setClaveProveedor(rs.getInt(2));
	            	notaForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(3))));
	            	notaForm.setSerieFactura(Utils.noNuloNormal(rs.getString(4)));
	            	notaForm.setMontoNota(rs.getString(5));
	            	notaForm.setMontoNotaFormateado(decimal.format(rs.getDouble(5)));
	            	if ("".equals(Utils.noNulo(rs.getString(6)))){
	            		notaForm.setFechaFactura("");
				    }else {
				    	notaForm.setFechaFactura(UtilsFechas.getFechaddMMMyyyy(rs.getDate(6)));
				    }
				    	
	            	
	            	notaForm.setEstadoSAT(Utils.noNuloNormal(rs.getString(7)));
	            	notaForm.setEstatusSAT(Utils.noNuloNormal(rs.getString(8)));
	            	notaForm.setUuidNotaCredito(Utils.noNuloNormal(rs.getString(9)));
	            	notaForm.setNombreXML(Utils.noNuloNormal(rs.getString(10)));
	            	notaForm.setNombrePDF(Utils.noNuloNormal(rs.getString(11)));
	            	notaForm.setUuidOrden(Utils.noNuloNormal(rs.getString(12)));
	            	notaForm.setEstatusOrden(Utils.noNuloNormal(rs.getString(15)));
	            	notaForm.setSubTotalNota(decimal.format(rs.getDouble(16)));
	            	notaForm.setIvaNota(decimal.format(rs.getDouble(17)));
	            	notaForm.setIsrNota(decimal.format(rs.getDouble(17)));
	            	
	            	
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
	        return notaForm;
	    }

	
	
	 public int actualizaEstatusCuentas(Connection con, String esquema, String UUID_ORDEN, String UUID_NOTA)
	    {
	        PreparedStatement stmt = null;
	        PreparedStatement stmtOrdenes = null;
	        int totGrabar = 0;
	        try{
	        	
	            stmt = con.prepareStatement( ValidacionesNotaCreditoQuerys.getActualizarCreditoCuentas(esquema));
	            stmt.setInt(1, 1);
	            stmt.setString(2, UUID_NOTA);
	            totGrabar = stmt.executeUpdate();
	            if (totGrabar > 0) {
	            	stmtOrdenes = con.prepareStatement( ValidacionesNotaCreditoQuerys.getActualizarOrdenesCuentas(esquema));
	            	stmtOrdenes.setString(1, UUID_NOTA);
	            	stmtOrdenes.setString(2, UUID_ORDEN);
	                stmtOrdenes.executeUpdate();
	            }
	        }catch(Exception e){
	        	Utils.imprimeLog("", e);
	        }finally{
		       try{
		            if(stmt != null)
		                stmt.close();
		            stmt = null;
		            if(stmtOrdenes != null)
		            	stmtOrdenes.close();
		            stmtOrdenes = null;
		       }catch(Exception e){
		            stmt = null;
		       }
	        }
	        return totGrabar;
	    }

	 
	 
	 public int regresaEstatusOrden(Connection con, String esquema, String UUID_ORDEN, String UUID_NOTA)
	    {
	        PreparedStatement stmtOrdenes = null;
	        int totGrabar = 0;
	        try{
         	stmtOrdenes = con.prepareStatement( ValidacionesNotaCreditoQuerys.getActualizarOrdenesCuentas(esquema));
         	stmtOrdenes.setString(1, UUID_NOTA);
         	stmtOrdenes.setString(2, UUID_ORDEN);
         	totGrabar = stmtOrdenes.executeUpdate();
             eliminarNotaCredito(con, esquema, UUID_NOTA, "OK");
             
	        }catch(Exception e){
	        	Utils.imprimeLog("", e);
	        }finally{
		       try{
		            if(stmtOrdenes != null)
		            	stmtOrdenes.close();
		            stmtOrdenes = null;
		       }catch(Exception e){
		    	   stmtOrdenes  = null;
		       }
	        }
	        return totGrabar;
	    }
}
