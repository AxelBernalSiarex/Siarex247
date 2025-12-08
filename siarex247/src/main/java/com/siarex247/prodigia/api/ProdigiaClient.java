package com.siarex247.prodigia.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.siarex247.prodigia.data.ConsultaComprobante;
import com.siarex247.prodigia.data.ConsultaEFO;
import com.siarex247.prodigia.data.ConsultaRfcLco;
import com.siarex247.prodigia.data.DetalleConsultaEFO;
import com.siarex247.prodigia.data.LcoItem;


public class ProdigiaClient {
	//private static String urlConsultaCFDIxuuid = "https://timbrado.pade.mx/servicio/rest/consulta/cfdPorUUID";
	//private static String urlConsultaCFDIxuuid = "https://timbrado.pade.mx/servicio/rest/integracion/consultaCfdiPorUUID";
	private static String urlConsultaCFDIxuuid = "https://timbrado.pade.mx/servicio/rest/cancelacion/consultarEstatusComprobante";
	private static String urlConsultaRFCValido = "https://timbrado.pade.mx/servicio/rest/herramientas/consultarValidezRfc";
	private static String urlConsultaEFO       = "https://timbrado.pade.mx/servicio/rest/herramientas/consultarEfoByRfc";
	
	//private static String AUTHORIZATION = "Basic cm9iZXJ0by5tdXJpbGxvQG12LXNvbHVjaW9uZXMuY29tOlIwYjNydDAyMDIxIw";
	public static String AUTHORIZATION = "";
	
	public static String CONTRATO = "";
	// 2614f396-eb4e-4d89-a498-76a02c98c7c8
	
	public static ConsultaComprobante consultaStatusCFDIporUUId(String uuid, String rfcEmisor, String rfcReceptor, String total) {
		ConsultaComprobante cComprobante = new ConsultaComprobante();
		try {
			URL url = new URL(urlConsultaCFDIxuuid 
					+ "?contrato="+CONTRATO
					+ "&uuid=" + uuid
					+ "&rfcEmisor=" + rfcEmisor
					+ "&rfcReceptor=" + rfcReceptor
					+ "&total=" + total
					//+ "&opciones=REGRESAR_CADENA_ORIGINAL" //+ uuid
					);
	        
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/xml");
	        conn.setRequestProperty("Authorization", AUTHORIZATION);	
	      
	        if (conn.getResponseCode() != 202) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                    + conn.getResponseCode()
	                    + '\n' + conn.getResponseMessage());
	        }

	        BufferedReader br = new BufferedReader(new InputStreamReader(
	            (conn.getInputStream())));

	        String output;
	        StringBuilder sbr = new StringBuilder();
	        while ((output = br.readLine()) != null) {
	            sbr.append(output);   	            
	        }
	        conn.disconnect();

	        InputStream targetStream = new java.io.ByteArrayInputStream(sbr.toString().getBytes());
            
            Document doc = new XmlParser().parseXML(targetStream);
            NodeList list = doc.getElementsByTagName("servicioConsultaComprobante");
            if(list.getLength() == 1) {

                Node node = list.item(0);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;
                    cComprobante.setCodigo(XmlParser.nodeValue(element, "codigo"));
                    cComprobante.setConsultaOk(XmlParser.nodeValue(element, "consultaOk"));
                    cComprobante.setCodigoEstatus(XmlParser.nodeValue(element, "codigoEstatus"));
                    cComprobante.setEsCancelable(XmlParser.nodeValue(element, "esCancelable"));
                    cComprobante.setEstado(XmlParser.nodeValue(element, "estado"));
                    cComprobante.setEstatusCfdi(XmlParser.nodeValue(element, "estatusCfdi"));
                }
            }
            
		
		} catch (MalformedURLException e) {

		        e.printStackTrace();

		} catch (IOException e) {

		        e.printStackTrace();

		}
		return cComprobante;
	}

	public static ConsultaRfcLco consultaValidezRFC(String rfcConsulta) {
		ConsultaRfcLco rConsulta = new ConsultaRfcLco();
		
		try {
			URL url = new URL(urlConsultaRFCValido +"?contrato="+CONTRATO+"&busqueda=" + rfcConsulta);
	        
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-Type", "application/xml");
	        conn.setRequestProperty("Authorization", AUTHORIZATION);	
	      
	        if (conn.getResponseCode() != 202) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                    + conn.getResponseCode()
	                    + '\n' + conn.getResponseMessage());
	        }

	        BufferedReader br = new BufferedReader(new InputStreamReader(
	            (conn.getInputStream())));

	        String output;
	        StringBuilder sbr = new StringBuilder();
	        while ((output = br.readLine()) != null) {
	            sbr.append(output);
	            	            
	        }
	        conn.disconnect();

	        InputStream targetStream = new java.io.ByteArrayInputStream(sbr.toString().getBytes());
            
            Document doc = new XmlParser().parseXML(targetStream);
            NodeList list = doc.getElementsByTagName("ResultConsultaRfcLco");
            if(list.getLength() == 1) {
            	
                Node node = list.item(0);
            	
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                	Element element = (Element) node;
                	
                    rConsulta.setCodigo(XmlParser.nodeAtribute(element, "codigo"));
                	rConsulta.setConsultaOk(XmlParser.nodeAtribute(element, "consultaOk"));
                	rConsulta.setLcoItems(new ArrayList<LcoItem>());
                	rConsulta.setRfc(XmlParser.nodeAtribute(element, "rfc"));
                	
                	NodeList lcoItems = doc.getElementsByTagName("lcoItem");
                    for (int temp = 0; temp < lcoItems.getLength(); temp++) {
                    	Node nodelco = lcoItems.item(temp);
                    	if (nodelco.getNodeType() == Node.ELEMENT_NODE) {

	                    	Element lcoelement = (Element) nodelco;
	                    	
	                    	LcoItem lItem = new LcoItem();
	                    	lItem.setEstatus(XmlParser.nodeAtribute(lcoelement, "estatus"));
	                    	lItem.setFechaFin(XmlParser.nodeAtribute(lcoelement, "fechaFin"));
	                    	lItem.setFechaInicio(XmlParser.nodeAtribute(lcoelement, "fechaInicio"));
	                    	lItem.setNumCert(XmlParser.nodeAtribute(lcoelement, "numCert"));
	                    	lItem.setValidez(XmlParser.nodeAtribute(lcoelement, "validez"));
	                    	rConsulta.getLcoItems().add(lItem);
                    	}
                    }

                }
            }
		
		} catch (MalformedURLException e) {

		        e.printStackTrace();

		} catch (IOException e) {

		        e.printStackTrace();

		}
		return rConsulta;
	}
	
	public static ConsultaEFO ConsutaEFOByRFC(String rfcConsulta) {
	    ConsultaEFO	rConsulta = new ConsultaEFO();
		
		try {
			URL url = new URL(urlConsultaEFO +"?contrato="+CONTRATO+"&rfc=" + rfcConsulta);
	        
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-Type", "application/xml");
	        conn.setRequestProperty("Authorization", AUTHORIZATION);	
	      
	        if (conn.getResponseCode() != 202) {
	            throw new RuntimeException("Failed : HTTP error code : "
	                    + conn.getResponseCode()
	                    + '\n' + conn.getResponseMessage());
	        }

	        BufferedReader br = new BufferedReader(new InputStreamReader(
	            (conn.getInputStream()), StandardCharsets.UTF_8 ));

	        String output;
	        StringBuilder sbr = new StringBuilder();
	        while ((output = br.readLine()) != null) {
	            sbr.append(output);
	            	            
	        }
	        conn.disconnect();

	        InputStream targetStream = new java.io.ByteArrayInputStream(sbr.toString().getBytes());
            Document doc = new XmlParser().parseXML(targetStream);
            NodeList list = doc.getElementsByTagName("ResultConsultaEFO");
            if(list.getLength() == 1) {
            	
                Node node = list.item(0);
            	
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                	Element element = (Element) node;
                	
                    rConsulta.setCodigo(XmlParser.nodeValue(element, "codigo"));
                	rConsulta.setConsultaOk(XmlParser.nodeValue(element, "consultaOk"));
                	rConsulta.setMensaje(XmlParser.nodeValue(element, "mensaje"));
                	rConsulta.setDetalles(new ArrayList<DetalleConsultaEFO>());
                	rConsulta.setFechaLista(XmlParser.nodeValue(element, "fechaLista"));
                	
                	//NodeList detItems = doc.getElementsByTagName("detalle");
                	NodeList detalles = doc.getElementsByTagName("detalles");
                    for (int temp = 0; temp < detalles.getLength(); temp++) {
                    	Node nodeDEtalle = detalles.item(temp);
                    	if (nodeDEtalle.getNodeType() == Node.ELEMENT_NODE) {
	                    	Element lcoelement = (Element) nodeDEtalle;
	                    	
	                    	DetalleConsultaEFO lItem = new DetalleConsultaEFO();
	                    	lItem.setNumContribDesvirtuados(XmlParser.nodeAtribute(lcoelement, "estatus"));
	                    	lItem.setRfc(XmlParser.nodeValue(lcoelement, "rfc"));
                    		lItem.setRazonSocial(XmlParser.nodeValue(lcoelement, "razonSocial"));
                    		lItem.setSituacionContrib(XmlParser.nodeValue(lcoelement, "situacionContribuyente"));
                    		
                    		lItem.setNumGlobalPresuncion(XmlParser.nodeValue(lcoelement, "numGlobalPresuncion"));
                    		lItem.setNumFechaPresuncion(XmlParser.nodeValue(lcoelement, "numFechaPresuncion"));
                    		lItem.setPubFechaSatPresuntos(XmlParser.nodeValue(lcoelement, "pubFechaSatPresuntos"));
                    		lItem.setPubFechaDofPresuntos(XmlParser.nodeValue(lcoelement, "pubFechaDofPresuntos"));
                    		
                    		lItem.setPubSatDefinitivos(XmlParser.nodeValue(lcoelement, "pubSatDefinitivos"));
                    		lItem.setPubDofDefinitivos(XmlParser.nodeValue(lcoelement, "pubDofDefinitivos"));
                    		
                    		lItem.setNumFechaSentFav(XmlParser.nodeValue(lcoelement, "numFechaSentFav"));
                    		lItem.setPubSatSentFav(XmlParser.nodeValue(lcoelement, "pubSatSentFav"));
	                    	rConsulta.getDetalles().add(lItem);
                    	}
                    }

                }
            }
		
		} catch (MalformedURLException e) {

		        e.printStackTrace();

		} catch (IOException e) {

		        e.printStackTrace();

		}
		return rConsulta;
	}

}
