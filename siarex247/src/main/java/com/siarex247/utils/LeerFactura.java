package com.siarex247.utils;

import java.io.File;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.Concepto;
import com.itextpdf.xmltopdf.Conceptos;
import com.itextpdf.xmltopdf.LeerXML;
import com.itextpdf.xmltopdf.Retencion;
import com.itextpdf.xmltopdf.Traslado;
import com.itextpdf.xmltopdf.Pagos.PDoctoRelacionado;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaXMLForm;
import com.siarex247.cumplimientoFiscal.Boveda.XMLForm;
import com.siarex247.validaciones.UtilsValidaciones;

public class LeerFactura {
	public static final Logger logger = Logger.getLogger("siarex247");
	
	public static ArrayList<BovedaXMLForm> leerElementos(String nombreArchivo) {
		ArrayList<BovedaXMLForm> datos = new ArrayList<BovedaXMLForm>();
		//BovedaXMLForm forma = new BovedaXMLForm();
		try {
			// Generador de constructor de objetos XML
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            // Esto es para agilizar la lectura de archivos grandes
            documentBuilderFactory.setNamespaceAware(false);
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            // constructor de objetos XML
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // Ruta del archivo XML
            //String nombreArchivo = "C:\\Users\\jose_\\Desktop\\BORRAR\\FACTURAS_SIAREX\\ROBERTO\\108248\\MUVR7512295L3-A-957__.xml";
            File archivo = new File(nombreArchivo);
            
            // Objeto Documento XML
            Document documento = documentBuilder.parse(archivo);

            // Esto ayuda al procesamiento
            documento.getDocumentElement().normalize();

            // XPath nos permite seleccionar objetos via su ubicacion en la estructura del XML
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            // La ruta del elemento que deseamos, para este omitir el prefijo cfdi:
            String expresionTranslados = "/Comprobante/Impuestos/Traslados/Traslado";
            
            // Obtenemos todos los nodos que empatan con la ruta que indicamos
            NodeList nodeListTranslados = (NodeList) xPath.compile(expresionTranslados).evaluate(documento, XPathConstants.NODESET);
            
            
            
            // Obtenemos el primer elemento de esa lista
            Element translado = (Element) nodeListTranslados.item(0);
            if(translado != null) {
	            // Presentamos los atributos de ese elemento
	
	            // Ruta de los conceptos d ela factura
	            String expresionConceptos = "/Comprobante/Conceptos/Concepto";
	            
	            // Lista de nodos de conceptos
	            NodeList nodeListConceptos = (NodeList) xPath.compile(expresionConceptos).evaluate(documento, XPathConstants.NODESET);
	            
	            
	            if(nodeListConceptos.getLength() > 0) {
		            // Avanzamos por la lista para presentar los conceptos
		            for (int temp = 0; temp < nodeListConceptos.getLength(); temp++) {
		                // Obtenemos un nodo
		                Node nodoConcepto = nodeListConceptos.item(temp);
		
		                // Verificamos que el nodo sea un elemento, para prevenir errores
		                if (nodoConcepto.getNodeType() == Node.ELEMENT_NODE) {
		                    // Convertimos de Node a elemento
		                    Element elementoConcepto = (Element) nodoConcepto;

		                    // Presentamos los datos de cada elemento de la factura
		                    /*forma.setClaveProdServ(Utils.noNuloINT(elementoConcepto.getAttribute("ClaveProdServ")));
		                    forma.setValorUnitario(Utils.noNuloINT(elementoConcepto.getAttribute("ValorUnitario")));
		                    forma.setClaveUnidad(Utils.noNuloNormal(elementoConcepto.getAttribute("ClaveUnidad")));
		                    forma.setDescripcion(Utils.noNuloNormal(elementoConcepto.getAttribute("Descripcion")));
		                    forma.setNoIdentificacion(Utils.noNuloNormal(elementoConcepto.getAttribute("NoIdentificacion")));
		                    datos.add(forma);
		                    forma = new BovedaXMLForm();*/
		                }
		            }
	            }
            }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return datos;
	}
	
	public static BovedaXMLForm leerElementos(BovedaXMLForm forma, String nombreArchivo) {
		try {
			// Generador de constructor de objetos XML
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            // Esto es para agilizar la lectura de archivos grandes
            documentBuilderFactory.setNamespaceAware(false);
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            // constructor de objetos XML
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            
            // Ruta del archivo XML
            //String nombreArchivo = "C:\\Users\\jose_\\Desktop\\BORRAR\\FACTURAS_SIAREX\\ROBERTO\\108248\\MUVR7512295L3-A-957__.xml";
            File archivo = new File(nombreArchivo);
            
            // Objeto Documento XML
            Document documento = documentBuilder.parse(archivo);

            // Esto ayuda al procesamiento
            documento.getDocumentElement().normalize();

            // XPath nos permite seleccionar objetos via su ubicacion en la estructura del XML
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            // La ruta del elemento que deseamos, para este omitir el prefijo cfdi:
            String expresionTranslados = "/Comprobante/Impuestos/Traslados/Traslado";
            
            // Obtenemos todos los nodos que empatan con la ruta que indicamos
            NodeList nodeListTranslados = (NodeList) xPath.compile(expresionTranslados).evaluate(documento, XPathConstants.NODESET);
            
            
            
            // Obtenemos el primer elemento de esa lista
            Element translado = (Element) nodeListTranslados.item(0);
            if(translado != null) {
	            // Presentamos los atributos de ese elemento
	            // Ruta de los conceptos d ela factura
	            String expresionConceptos = "/Comprobante/Conceptos/Concepto";
	            
	            // Lista de nodos de conceptos
	            NodeList nodeListConceptos = (NodeList) xPath.compile(expresionConceptos).evaluate(documento, XPathConstants.NODESET);
	            
	            if(nodeListConceptos.getLength() > 0) {
		            // Avanzamos por la lista para presentar los conceptos
		            for (int temp = 0; temp < nodeListConceptos.getLength(); temp++) {
		                // Obtenemos un nodo
		                Node nodoConcepto = nodeListConceptos.item(temp);
		
		                // Verificamos que el nodo sea un elemento, para prevenir errores
		                if (nodoConcepto.getNodeType() == Node.ELEMENT_NODE) {
		                    // Convertimos de Node a elemento
		                    Element elementoConcepto = (Element) nodoConcepto;
		
		                    // Presentamos los datos de cada elemento de la factura
		                    forma.setClaveProdServ(Utils.noNuloINT(elementoConcepto.getAttribute("ClaveProdServ")));
		                    forma.setValorUnitario(Utils.noNuloINT(elementoConcepto.getAttribute("ValorUnitario")));
		                    forma.setClaveUnidad(Utils.noNuloNormal(elementoConcepto.getAttribute("ClaveUnidad")));
		                    forma.setDescripcion(Utils.noNuloNormal(elementoConcepto.getAttribute("Descripcion")));
		                    forma.setNoIdentificacion(Utils.noNuloNormal(elementoConcepto.getAttribute("NoIdentificacion")));
		                }
		            }
	            }
            }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return forma;
	}
	
	public XMLForm leerElementos(Connection con, String nombreArchivo, XMLForm cargasXMLForm){
		HashMap<String, String> mapaISR = null;
		HashMap<String, String> mapaTrans = null;
		
		try{
			String uuid = null;
			String serie = null;
			String folio = null;
			String fechaFactura = null;
			String formaPago = null;
			String formaPagoDesc = null;
			String metodoPago = null;
			String metodoPagoDesc = null;
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
			String retencionIVA = null;
			String transladoIVA = null;
			String retencionISR = null;
			String transladoIEPS = null;
			String cadMoneda = null;
			String fechaPago   = null;
			String fechaTimbrado = null;
			String fechaComprobante = null;
			String condicionesPago = null;
			String lugarExpedicion = null;
			String usoCFDI = null;
			String usoCFDIDesc = null;
			String regimenFiscal = null;
			String regimenFiscalDesc = null;
			String claveUnidad = null;
			String claveUnidadDesc = null;
			String claveProdServ = null;
			String claveProdServDesc = null;
			double cantidad = 0;
			double valorUnitario = 0;
			String descripcion = null;
			double importe = 0;
			String claveImpuestoTras = null;
			double importeTraslado = 0;
			String claveImpuestoTrasDesc = null;
			String baseTraslado = null;
			double tasaCuotaTraslado = 0;
			
			String claveImpuestoRet = null;
			String claveImpuestoRetDesc = null;
			double importeRetencion = 0;
			String tasaCuotaRet = null;
			double tipoCambio = 0;
			String tipoFactorRet = null;
			String tipoFactorTras = null;
			String impLocTotalTras = null;
			String impLocTasaTras = null;
			String impLocTasaRet = null;
			String impLocNombreRet = null;
			String impLocImporteTras = null;
			String impLocTotalRet = null;
			String impLocImporteRet = null;
			String impLocNombreTras = null;
			double totaImpuestosRet = 0;
			double totaImpuestosTras = 0;//1
			
			String totalIvaTras = null;
			String totalIsrRet = null;
			String totalIvaRet = null;
			String totalIepsRet = null;
			String totalIepsTras = null;
			String baseRetencion = null;
			String numIdentificacion = null;
			String unidad = null;
			String tipoRelacion = null;
			String tipoRelacionDesc = null;
			String uuidRelacionado = null;
			String fechaCancelacion = null;
			String estatus = null;
			String proceso = null;
			String tipoDocumento = null;
			String validez = null;
			String estadoPagado = null;
			String observaciones = null;
			String referencia = null;//2

			String asociadoBancos = null;
			String asociadoComercial = null;
			String asociadoContabilidad = null;
			String estadoCancelacionDocumento = null;
			String responsable = null;
			String annioComprobante = null;
			String versionComprobante = null;
			String mesComprobante = null;
			String numCertificadoSat = null;
			String numCtaPago = null;
			String lugarExpDesc = null;
			String annioTimbrado = null;
			String mesTimbrado = null;
			String receptorResidenciaFiscal = null;
			String receptorResidenciaFiscalDesc = null;
			String receptorNumRegistroTributario = null;
			String monedaDesc = null;
			String totalDescuento = null;
			String tipoDeComprobante = null;
			String tipoDeComprobanteDesc = null;
			String guid = null;
			String idPago = null;//3

			//Tipo I
			String drSerie = null;
			String drFolio = null;
			String drMoneda = null;
			String drMonedaDesc = null;
			double drSaldoAnterior = 0;
			double drImportePagado = 0;
			double drSaldoInsoluto = 0;
			String drIdentificador = null;
			String fechaCancelacionDoc = null;
			String estadoCancelacionDoc = null;
			String versionComplePago = null;
			String annioFechaPago = null;
			String mesFechaPago = null;
			String monedaPagoDesc = null;
			String numOperacionPago = null;
			String RfcBancoOrden = null;
			String nombreBancoExt = null;
			String ctaBancoOrden = null;
			String rfcBancoBenef = null;
			String tipoCadenaPago = null;
			String tipoCadenaPagoDesc = null;
			String certificadoPago = null;
			String cadenaPago = null;
			String selloPago = null;
			double drTipoCambio = 0;
			String drMetodoPago = null;
			String drMetodoPagoDesc = null;
			String drNumParcialidad = null;

			UtilsValidaciones utilsVal = new UtilsValidaciones();

			//LeerXML crea = null;
		    Comprobante _comprobante = null;
		    String arrMoneda [] = null;

		    try {
		    	_comprobante = LeerXML.ObtenerComprobante(nombreArchivo);
		    }
		    catch(Exception e) {
		    	Utils.imprimeLog("convXMLAExcel(): ", e);
		    }
		    //ESTOS SI VAN INCLUIDOS FACTURACION
		    uuid           = _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
		    receptorRFC    = _comprobante.getReceptor().getRfc();
		    receptorNombre = _comprobante.getReceptor().getNombre();
		    emisorRFC      = _comprobante.getEmisor().getRfc(); 
			emisorNombre   = _comprobante.getEmisor().getNombre();
			
			LocalDateTime fechaTimbradoLD  = _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado();
			
			if(fechaTimbradoLD != null) {
	    		fechaTimbrado = fechaTimbradoLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    	}

			//lugarExpDesc = "???";
			
	    	if (fechaTimbrado != null && fechaTimbrado.length() > 10) {
	    		fechaTimbrado = fechaTimbrado.substring(0, 10);
	    		annioTimbrado = fechaTimbrado.substring(0, 4);
	    		mesTimbrado = fechaTimbrado.substring(5, 7);
	    	}
	    	
	    	//receptorResidenciaFiscal = "???";
	    	//receptorResidenciaFiscalDesc = "???";
	    	//receptorNumRegistroTributario = "???";
	    	
	    	LocalDateTime fechaComprobanteLD = _comprobante.getFecha();
	    	
	    	if(fechaComprobanteLD != null) {
	    		fechaComprobante = fechaComprobanteLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    	}
			
			if (fechaComprobante != null && fechaComprobante.length() > 10) {
				fechaComprobante = fechaComprobante.substring(0, 10);
	    	}

	    	serie           = _comprobante.getSerie();
			folio           = _comprobante.getFolio();
			lugarExpedicion = _comprobante.getLugarExpedicion();
			usoCFDI         = _comprobante.getReceptor().getUsoCFDI();
			
			// logger.info("serie===>"+serie);
			// logger.info("folio===>"+folio);
			// logger.info("lugarExpedicion===>"+lugarExpedicion);
			// logger.info("usoCFDI===>"+usoCFDI);
			try {
				versionComplePago = _comprobante.getComplemento().Pagos.getVersion();	
			}catch(Exception e) {
				versionComplePago = ""; 
			}
			
			selloPago = _comprobante.getSello();
			
			if(usoCFDI.equals("")) {
				usoCFDIDesc = "???";
			}

			regimenFiscal   = _comprobante.getEmisor().getRegimenFiscal();

			if(regimenFiscal.equals("")) {
				regimenFiscalDesc = "???";
			}
			
			Conceptos listaConcepto = _comprobante.getConceptos();
			if(listaConcepto != null) {
				Concepto concepto = listaConcepto.getConcepto().get(0);
				claveUnidad = concepto.getClaveUnidad();
				claveUnidadDesc = concepto.getDescripcion();
				claveProdServ = concepto.getClaveProdServ();
				
				if(claveProdServ.equals("")) {
					//claveProdServDesc = "???";
				}
				
				cantidad = concepto.getCantidad();
				valorUnitario = concepto.getValorUnitario();
				descripcion = concepto.getDescripcion();
				importe = concepto.getImporte();
				numIdentificacion = concepto.getNoIdentificacion();
				unidad = concepto.getUnidad();
			}

			//tipoRelacion = ???
			//tipoRelacionDesc = ???
			//uuidRelacionado = ???
			//fechaCancelacion = ???
			//estatus = ???
			//proceso = ???
			//tipoDocumento = ???
			//validez = ???
			//estadoPagado = ???
			//observaciones = ???
			//referencia = ???
			
			tipoMoneda = _comprobante.getMoneda();

			if(tipoMoneda.equals("")) {
				desMoneda = "???";
			}

		    //ESTOS SI VAN INCLUIDOS FACTURACION
		    
		    subTotal     =  _comprobante.getSubTotal();

		    LocalDateTime fechaFacturaLD =  _comprobante.getFecha();
		    
		    if(fechaFacturaLD != null) {
		    	fechaFactura = fechaFacturaLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    	}
			
			/*
			cadMoneda = provUtils.validaDesMoneda(tipoMoneda);

			if (cadMoneda.indexOf(";") > -1){
				arrMoneda  = cadMoneda.split(";");
				desMoneda = arrMoneda[0];
				tipoMoneda = arrMoneda[1];

				if ("MXN".equalsIgnoreCase(tipoMoneda)){
					tipoMoneda = "1";
				}
				else{
					tipoMoneda = "2";
				}
			}
			else{
				tipoMoneda = "1";
			}*/

			tipoDeComprobante   =  _comprobante.getTipoDeComprobante();
			
			if(tipoDeComprobante.equals("P")) {
				tipoDeComprobanteDesc = "COMPLEMENTO DE PAGO";
			}
			else if(tipoDeComprobante.equals("I")) {
				tipoDeComprobanteDesc = "INGRESO";
			}
			
			//totalDescuento = "???";
			//guid = "???";
			//idPago = "???";
			
			tipoCambio = _comprobante.getTipoCambio();

			try {
				List<PDoctoRelacionado> listaDoctoRelacionado = _comprobante.getComplemento().Pagos.getPago().get(0).getDoctoRelacionado();
				LocalDateTime annioFechaPagoLD = _comprobante.getComplemento().Pagos.getPago().get(0).getFechaPago();
				tipoMoneda = _comprobante.getComplemento().Pagos.getPago().get(0).getMonedaP();
				
				if(annioFechaPagoLD != null) {
					annioFechaPago = annioFechaPagoLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		    	}
				
				
				if (!"".equalsIgnoreCase(annioFechaPago)) {
					mesFechaPago = annioFechaPago.substring(5, 7);
				}
				monedaPagoDesc = _comprobante.getComplemento().Pagos.getPago().get(0).getMonedaP();
				if(listaDoctoRelacionado != null && listaDoctoRelacionado.size() > 0) {
					PDoctoRelacionado doctoRelacionado = listaDoctoRelacionado.get(0);
					drSaldoAnterior = doctoRelacionado.getImpSaldoAnt();
					drSaldoInsoluto = doctoRelacionado.getImpSaldoInsoluto();
					drImportePagado = doctoRelacionado.getImpPagado();
					drIdentificador = doctoRelacionado.getIdDocumento();
					drMetodoPago = doctoRelacionado.getMetodoDePagoDR();
					drNumParcialidad = doctoRelacionado.getNumParcialidad();
					drTipoCambio = doctoRelacionado.getTipoCambioDR();
					
					drSerie = doctoRelacionado.getSerie();
					drFolio = doctoRelacionado.getFolio();
					drMoneda = doctoRelacionado.getMonedaDR();
					
					if(drMoneda.equals("MXN")) {
						drMonedaDesc = "PESOS";
					}
					else {
						drMonedaDesc = "DOLARES";
					}
				}
			}
			catch(Exception e) {
				e = null;
			}
			
			// drSaldoAnterior = "ya";
			//drImportePagado = "ya";
			//drSaldoInsoluto = "ya";
			//drIdentificador = "ya";
			//fechaCancelacionDoc = "???";
			//estadoCancelacionDoc = "???";
			//versionComplePago = "ya";
			//annioFechaPago = "ya";
			//mesFechaPago = "ya";
			//monedaPagoDesc = "ya";
			//numOperacionPago = "???";
			//RfcBancoOrden = "???";
			//nombreBancoExt = "???";
			//ctaBancoOrden = "???";
			//rfcBancoBenef = "???";
			//tipoCadenaPago = "???";
			//tipoCadenaPagoDesc = "???";
			//certificadoPago = "???";
			//cadenaPago = "???";
			//selloPago = "ya";
			//drTipoCambio = "ya";
			//drMetodoPago = "ya";
			//drMetodoPagoDesc = "???";
			//drNumParcialidad = "ya";

			// logger.info("tipoDeComprobante====>"+tipoDeComprobante);
			if ("I".equalsIgnoreCase(tipoDeComprobante) || "ingreso".equalsIgnoreCase(tipoDeComprobante)) {
				//ESTOS SI VAN INCLUIDOS FACTURACION
			    formaPago    =  _comprobante.getFormaPago();
			    //formaPagoDesc = ???
			    metodoPago   =  _comprobante.getMetodoPago();
			    
			    if(metodoPago.equals("")) {
			    	metodoPagoDesc = "";
			    }
			    
			    try {
				    List<Traslado> listaTraslado = _comprobante.getImpuestos().getTraslados();
					if(listaTraslado != null && listaTraslado.size() > 0) {
						Traslado traslado = listaTraslado.get(0);
						//baseTraslado = ???
						tasaCuotaTraslado = traslado.getTasaOCuota();
						claveImpuestoTras = traslado.getImpuesto();
						importeTraslado = traslado.getImporte();
						if(claveImpuestoTras.equals("001")) {
							claveImpuestoTrasDesc = "";
						}
						else if(claveImpuestoTras.equals("002")) {
							claveImpuestoTrasDesc = "";
						}
						tipoFactorTras = traslado.getTipoFactor();
					}
			    }
				catch(Exception e) {
					e = null;
				}

			    try {
					List<Retencion> listaRetencion = _comprobante.getImpuestos().getRetenciones();
					if(listaRetencion != null && listaRetencion.size() > 0) {
						Retencion retencion = listaRetencion.get(0);
						claveImpuestoRet = retencion.getImpuesto();
	
						if(claveImpuestoRet.equals("001")) {
							claveImpuestoRetDesc = "ISR";
						}
						else if(claveImpuestoRet.equals("002")) {
							claveImpuestoRetDesc = "IVA";
						}
						importeRetencion = retencion.getImporte();
						//tasaCuotaRet = retencion.???
						//tipoFactorRet = retencion.???
					}
			    }
				catch(Exception e) {
					e = null;
				}

				//impLocTotalTras = ???
				//impLocTasaTras = ???
				//impLocTasaRet = ???
				//impLocNombreRet = ???
				//impLocImporteTras = ???
				//impLocTotalRet = ???
				//impLocImporteRet = ???
				//impLocNombreTras = ???
			    try {
			    	totaImpuestosRet = _comprobante.getImpuestos().getTotalImpuestosRetenidos();	
			    }catch(Exception e) {
			    	totaImpuestosRet = 0;
			    }
				
			    try {
			    	totaImpuestosTras = _comprobante.getImpuestos().getTotalImpuestosTrasladados();	
			    }catch(Exception e) {
			    	totaImpuestosTras = 0;
			    }
				
				//totalIvaTras = ???
				//totalIsrRet = ???
				//totalIvaRet = ???
				//totalIepsRet = ???
				//totalIepsTras = ???
				//baseRetencion = ???
				//asociadoBancos = ???
				//asociadoComercial = ???
				//asociadoContabilidad = ???
				//estadoCancelacionDocumento = ???
				//responsable = ???
				//annioComprobante = ???
				//versionComprobante = ???
				//mesComprobante = ???
				numCertificadoSat = _comprobante.getComplemento().TimbreFiscalDigital.getNoCertificadoSAT();
			    //ESTOS SI VAN INCLUIDOS FACTURACION

			    numeroCuentaPago =  ""; // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@NumCtaPago");
			    numCtaPago = "";
			    descuento    =  _comprobante.getDescuento();  // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@"+utils.ucFirst("descuento", bandXML));
				total =  _comprobante.getTotal(); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@"+utils.ucFirst("total", bandXML));
				// impuestoRet =  XMLXPathManagerBase.getNodeListByPath(document, "/Comprobante/Impuestos/Retenciones/Retencion/@"+utils.ucFirst("impuesto", bandXML));
				// importeRet  = XMLXPathManagerBase.getNodeListByPath(document, "/Comprobante/Impuestos/Retenciones/Retencion/@"+utils.ucFirst("importe", bandXML));

			    try {
					totalImpuestoRet =  _comprobante.getImpuestos().getTotalImpuestosRetenidos();         // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Impuestos/@"+utils.ucFirst("totalImpuestosRetenidos", bandXML));
					totalImpuestoTranslado = _comprobante.getImpuestos().getTotalImpuestosTrasladados();  // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Impuestos/@"+utils.ucFirst("totalImpuestosTrasladados", bandXML));																								          
					List<Retencion> listRetencion = _comprobante.getImpuestos().getRetenciones();  
				    mapaISR = utilsVal.getImporteImpuestoISRNuevo(listRetencion);

				    if (!mapaISR.isEmpty()){
				    	retencionIVA = mapaISR.get("IVA");
						retencionISR = mapaISR.get("ISR");
					}
				    List<Traslado> listaTranslados = _comprobante.getImpuestos().getTraslados();
				    mapaTrans = utilsVal.getImporteTransladosISRNuevo(listaTranslados);
					// mapaTrans = provUtils.getImporteImpuestoISR(importeTrans, impuestoTrans);
					transladoIVA = mapaTrans.get("IVA");
					//transladoIEPS = XMLXPathManagerBase.getNodeValue(document, "");
					transladoIEPS = "0";
				    
			    }catch(Exception e) {
			    	
			    }

			 }
			 else {
			    total        = _comprobante.getTotal(); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/Pagos/Pago/@"+utils.ucFirst("monto", bandXML));
			    
			    LocalDateTime fechaPagoLD     = null;
			    
			    // logger.info("nombreXML===>"+nombreArchivo);
			    if(_comprobante.getComplemento().Pagos != null) {
			    	fechaPagoLD     = _comprobante.getComplemento().Pagos.getPago().get(0).getFechaPago();
			    }else if (_comprobante.getComplemento().getPagos20() != null){
			    	fechaPagoLD     = _comprobante.getComplemento().getPagos20().getPago().get(0).getFechaPago();
			    }
			    
			    fechaTimbradoLD = _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado();
		    	
		    	if(fechaPagoLD != null) {
		    		fechaPago = fechaPagoLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		    	}
		    	
		    	if(fechaTimbradoLD != null) {
		    		fechaTimbrado = fechaTimbradoLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		    	}
		    	
		    	if (fechaPagoLD != null && fechaPago.length() > 10) {
		    		fechaPago = fechaPago.substring(0, 10);
		    	}

		    	if (fechaTimbradoLD != null && fechaTimbrado.length() > 10) {
		    		fechaTimbrado = fechaTimbrado.substring(0, 10);
		    	}
			}

			cargasXMLForm.setUuid(uuid);
			cargasXMLForm.setSerie(serie);
			cargasXMLForm.setFolio(folio);
			cargasXMLForm.setFechaFactura(fechaFactura);
			cargasXMLForm.setFormaPago(formaPago);
			cargasXMLForm.setMetodoPago(metodoPago);
			cargasXMLForm.setDesTipoMoneda(desMoneda);
			cargasXMLForm.setTipoMoneda(tipoMoneda);
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
			cargasXMLForm.setRetencionIVA(parseaDoble(retencionIVA));
			cargasXMLForm.setTransladoIVA(parseaDoble(transladoIVA));
			cargasXMLForm.setRetencionISR(parseaDoble(retencionISR));
			cargasXMLForm.setTransladoIEPS(parseaDoble(transladoIEPS));
			cargasXMLForm.setNombreXML(uuid+".xml");
			cargasXMLForm.setTipoComprobante(tipoDeComprobante);
			cargasXMLForm.setFechaPago(fechaPago);
			cargasXMLForm.setFechaTimbrado(fechaTimbrado);
			
			//NUEVAS VARIABLES
			cargasXMLForm.setFormaPagoDesc(formaPagoDesc);
			cargasXMLForm.setMetodoPagoDesc(metodoPagoDesc);
			cargasXMLForm.setDesMoneda(desMoneda);
			cargasXMLForm.setCadMoneda(cadMoneda);
			cargasXMLForm.setFechaComprobante(fechaComprobante);
			cargasXMLForm.setLugarExpedicion(lugarExpedicion);
			cargasXMLForm.setUsoCFDI(usoCFDI);
			cargasXMLForm.setUsoCFDIDesc(usoCFDIDesc);
			cargasXMLForm.setRegimenFiscal(regimenFiscal);
			cargasXMLForm.setRegimenFiscalDesc(regimenFiscalDesc);
			cargasXMLForm.setClaveUnidad(claveUnidad);
			cargasXMLForm.setClaveUnidadDesc(claveUnidadDesc);
			cargasXMLForm.setClaveProdServ(claveProdServ);
			cargasXMLForm.setClaveProdServDesc(claveProdServDesc);
			cargasXMLForm.setCantidad(cantidad);
			cargasXMLForm.setValorUnitario(valorUnitario);
			cargasXMLForm.setDescripcion(descripcion);
			cargasXMLForm.setImporte(importe);
			cargasXMLForm.setClaveImpuestoTras(claveImpuestoTras);
			cargasXMLForm.setImporteTraslado(importeTraslado);
			cargasXMLForm.setClaveImpuestoTrasDesc(claveImpuestoTrasDesc);
			cargasXMLForm.setBaseTraslado(baseTraslado);
			cargasXMLForm.setTasaCuotaTraslado(tasaCuotaTraslado);
			
			cargasXMLForm.setClaveImpuestoRet(claveImpuestoRet);
			cargasXMLForm.setClaveImpuestoRetDesc(claveImpuestoRetDesc);
			cargasXMLForm.setImporteRetencion(importeRetencion);
			cargasXMLForm.setTasaCuotaRet(tasaCuotaRet);
			cargasXMLForm.setTipoCambio(tipoCambio);
			cargasXMLForm.setTipoFactorRet(tipoFactorRet);
			cargasXMLForm.setTipoFactorTras(tipoFactorTras);
			cargasXMLForm.setImpLocTotalTras(impLocTotalTras);
			cargasXMLForm.setImpLocTasaTras(impLocTasaTras);
			cargasXMLForm.setImpLocTasaRet(impLocTasaRet);
			cargasXMLForm.setImpLocNombreRet(impLocNombreRet);
			cargasXMLForm.setImpLocImporteTras(impLocImporteTras);
			cargasXMLForm.setImpLocTotalRet(impLocTotalRet);
			cargasXMLForm.setImpLocImporteRet(impLocImporteRet);
			cargasXMLForm.setImpLocNombreTras(impLocNombreTras);
			cargasXMLForm.setTotaImpuestosRet(totaImpuestosRet);
			cargasXMLForm.setTotaImpuestosTras(totaImpuestosTras);
			
			cargasXMLForm.setTotalIvaTras(totalIvaTras);
			cargasXMLForm.setTotalIsrRet(totalIsrRet);
			cargasXMLForm.setTotalIvaRet(totalIvaRet);
			cargasXMLForm.setTotalIepsRet(totalIepsRet);
			cargasXMLForm.setTotalIepsTras(totalIepsTras);
			cargasXMLForm.setBaseRetencion(baseRetencion);
			cargasXMLForm.setNumIdentificacion(numIdentificacion);
			cargasXMLForm.setUnidad(unidad);
			cargasXMLForm.setTipoRelacion(tipoRelacion);
			cargasXMLForm.setTipoRelacionDesc(tipoRelacionDesc);
			cargasXMLForm.setUuidRelacionado(uuidRelacionado);
			cargasXMLForm.setFechaCancelacion(fechaCancelacion);
			cargasXMLForm.setEstatus(estatus);
			cargasXMLForm.setProceso(proceso);
			cargasXMLForm.setTipoDocumento(tipoDocumento);
			cargasXMLForm.setValidez(validez);
			cargasXMLForm.setEstadoPagado(estadoPagado);
			cargasXMLForm.setObservaciones(observaciones);
			cargasXMLForm.setReferencia(referencia);

			cargasXMLForm.setAsociadoBancos(asociadoBancos);
			cargasXMLForm.setAsociadoComercial(asociadoComercial);
			cargasXMLForm.setAsociadoContabilidad(asociadoContabilidad);
			cargasXMLForm.setEstadoCancelacionDocumento(estadoCancelacionDocumento);
			cargasXMLForm.setResponsable(responsable);
			cargasXMLForm.setAnnioComprobante(annioComprobante);
			cargasXMLForm.setVersionComprobante(versionComprobante);
			cargasXMLForm.setMesComprobante(mesComprobante);
			cargasXMLForm.setNumCertificadoSat(numCertificadoSat);
			cargasXMLForm.setNumCtaPago(numCtaPago);
			cargasXMLForm.setLugarExpDesc(lugarExpDesc);
			cargasXMLForm.setAnnioTimbrado(annioTimbrado);
			cargasXMLForm.setMesTimbrado(mesTimbrado);
			cargasXMLForm.setReceptorResidenciaFiscal(receptorResidenciaFiscal);
			cargasXMLForm.setReceptorResidenciaFiscalDesc(receptorResidenciaFiscalDesc);
			cargasXMLForm.setReceptorNumRegistroTributario(receptorNumRegistroTributario);
			cargasXMLForm.setMonedaDesc(monedaDesc);
			cargasXMLForm.setTotalDescuento(totalDescuento);
			cargasXMLForm.setTipoDeComprobante(tipoDeComprobante);
			cargasXMLForm.setTipoDeComprobanteDesc(tipoDeComprobanteDesc);
			cargasXMLForm.setGuid(guid);
			cargasXMLForm.setIdPago(idPago);

			//Tipo I
			cargasXMLForm.setDrSerie(drSerie);
			cargasXMLForm.setDrFolio(drFolio);
			cargasXMLForm.setDrMoneda(drMoneda);
			cargasXMLForm.setDrMonedaDesc(drMonedaDesc);
			cargasXMLForm.setDrSaldoAnterior(drSaldoAnterior);
			cargasXMLForm.setDrImportePagado(drImportePagado);
			cargasXMLForm.setDrSaldoInsoluto(drSaldoInsoluto);
			cargasXMLForm.setDrIdentificador(drIdentificador);
			cargasXMLForm.setFechaCancelacionDoc(fechaCancelacionDoc);
			cargasXMLForm.setEstadoCancelacionDoc(estadoCancelacionDoc);
			cargasXMLForm.setVersionComplePago(versionComplePago);
			cargasXMLForm.setAnnioFechaPago(annioFechaPago);
			cargasXMLForm.setMesFechaPago(mesFechaPago);
			cargasXMLForm.setMonedaPagoDesc(monedaPagoDesc);
			cargasXMLForm.setNumOperacionPago(numOperacionPago);
			cargasXMLForm.setRfcBancoOrden(RfcBancoOrden);
			cargasXMLForm.setNombreBancoExt(nombreBancoExt);
			cargasXMLForm.setCtaBancoOrden(ctaBancoOrden);
			cargasXMLForm.setRfcBancoBenef(rfcBancoBenef);
			cargasXMLForm.setTipoCadenaPago(tipoCadenaPago);
			cargasXMLForm.setTipoCadenaPagoDesc(tipoCadenaPagoDesc);
			cargasXMLForm.setCertificadoPago(certificadoPago);
			cargasXMLForm.setCadenaPago(cadenaPago);
			cargasXMLForm.setSelloPago(selloPago);
			cargasXMLForm.setDrTipoCambio(String.valueOf(drTipoCambio));
			cargasXMLForm.setDrMetodoPago(drMetodoPago);
			cargasXMLForm.setDrMetodoPagoDesc(drMetodoPagoDesc);
			cargasXMLForm.setDrNumParcialidad(drNumParcialidad);
			cargasXMLForm.setCondicionesPago(condicionesPago);
		}
		catch(Exception e){
			Utils.imprimeLog("leerElementos(): ", e);
		}
		finally{
		  try{
			if (con != null){
				con.close();
			}
			con = null;
		  }catch(Exception e){
			con = null;
		  }
		}
		return cargasXMLForm;
	}
	
	private Double parseaDoble(String valor){
		double retorno = 0;
		try{
			retorno = Double.parseDouble(valor);
		}catch(Exception e){
			retorno = 0;
		}
		return retorno;
	}
	
	public static void main(String[] args) {
		try {
			String fechaTimbrado = "2019-11-10T01:19:07";
			String annioTimbrado = "";
			String mesTimbrado = "";

			if (fechaTimbrado.length() > 10) {
	    		fechaTimbrado = fechaTimbrado.substring(0, 10);
	    		annioTimbrado = fechaTimbrado.substring(0, 4);
	    		mesTimbrado = fechaTimbrado.substring(5, 7);
	    	}
			//LeerFactura l = new LeerFactura();
			//l.leerElementos("");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
