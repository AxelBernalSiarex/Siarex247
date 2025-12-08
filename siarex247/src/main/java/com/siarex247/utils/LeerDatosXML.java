package com.siarex247.utils;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaForm;
import com.siarex247.cumplimientoFiscal.BovedaEmitidos.BovedaEmitidosForm;

public class LeerDatosXML {
	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	
	public ArrayList<Comprobante> leerElementos(ArrayList<BovedaForm> listadoXML, String nombreRepositorio){
		Comprobante _comprobante = null;
		ArrayList<Comprobante> listaDatos = new ArrayList<>();
		try{
			String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + nombreRepositorio +"/BOVEDA/";
			String pathXML = null;
			BovedaForm bovedaForm = null;
			for (int x = 0; x < listadoXML.size(); x++) {
				bovedaForm = listadoXML.get(x);
				try {
					pathXML = rutaBoveda + bovedaForm.getUuid() + ".xml";
			    	_comprobante = LeerXML.ObtenerComprobante(pathXML) ;
			    }
			    catch(Exception e) {
			    	Utils.imprimeLog("", e);
			    }
				if (_comprobante == null) {
					
				}else {
					listaDatos.add(_comprobante);
				}
			}
			
		} catch(Exception e){
			Utils.imprimeLog("leerElementos(): ", e);
		}
		return listaDatos;
	}
	 
	
	
	public ArrayList<Comprobante> leerElementosEmitidos(ArrayList<BovedaEmitidosForm> listadoXML, String nombreRepositorio){
		Comprobante _comprobante = null;
		ArrayList<Comprobante> listaDatos = new ArrayList<>();
		try{
			String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + nombreRepositorio +"/BOVEDA_EMITIDOS/";
			String pathXML = null;
			BovedaEmitidosForm bovedaForm = null;
			for (int x = 0; x < listadoXML.size(); x++) {
				bovedaForm = listadoXML.get(x);
				try {
					pathXML = rutaBoveda + bovedaForm.getUuid() + ".xml";
			    	_comprobante = LeerXML.ObtenerComprobante(pathXML) ;
			    }
			    catch(Exception e) {
			    	Utils.imprimeLog("", e);
			    }
				if (_comprobante == null) {
					
				}else {
					listaDatos.add(_comprobante);
				}
			}
			
		} catch(Exception e){
			Utils.imprimeLog("leerElementos(): ", e);
		}
		return listaDatos;
	}
	
	
	public ArrayList<Comprobante> leerElementosExportar(ArrayList<BovedaForm> listadoXML){
		Comprobante _comprobante = null;
		ArrayList<Comprobante> listaDatos = new ArrayList<>();
		try{
			// String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + nombreRepositorio +"/BOVEDA/";
			String pathXML = null;
			BovedaForm bovedaForm = null;
			for (int x = 0; x < listadoXML.size(); x++) {
				bovedaForm = listadoXML.get(x);
				try {
					pathXML = bovedaForm.getUuid();
			    	_comprobante = LeerXML.ObtenerComprobante(pathXML) ;
			    }
			    catch(Exception e) {
			    	Utils.imprimeLog("", e);
			    }
				if (_comprobante == null) {
					
				}else {
					listaDatos.add(_comprobante);
				}
			}
			
		} catch(Exception e){
			Utils.imprimeLog("leerElementos(): ", e);
		}
		return listaDatos;
	}
	 
	
}
