package com.siarex247.prodigia.api;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlParser {

	public Document parseXML(InputStream xmlDoc) {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document doc = db.parse(xmlDoc);
			
			doc.getDocumentElement().normalize();
			
			//System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
	        //System.out.println("------");
			
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return null;
	}
	
	public static String nodeValue(Element element, String nodeName) {
		
		return element.getElementsByTagName(nodeName).item(0) == null? "":
			element.getElementsByTagName(nodeName).item(0).getTextContent();
	}
	
	public static String nodeAtribute(Element element, String attributeName) {
		
		return element.getAttribute(attributeName) == null? "":
			element.getAttribute(attributeName);
	}
}
