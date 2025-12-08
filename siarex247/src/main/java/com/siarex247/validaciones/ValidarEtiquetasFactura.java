package com.siarex247.validaciones;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.configuraciones.Etiquetas.EtiquetasBean;
import com.siarex247.configuraciones.Etiquetas.EtiquetasForm;
import com.siarex247.configuraciones.RegimenFiscal.RegimenFiscalBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;

public class ValidarEtiquetasFactura {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	
	public  HashMap<String, String> validarEtiquetasXML(Connection con, String esquema, Comprobante _comprobante){
		EtiquetasBean etiquetasBean = new EtiquetasBean();
		HashMap<String, String> mapaRes = new HashMap<String, String>();

		try{
			mapaRes.put("SUBJECT", "");
			mapaRes.put("MENSAJE", "");
			mapaRes.put("ERROR", "TRUE");

			ArrayList<EtiquetasForm> datoXMLSistema = etiquetasBean.obtenerEtiquetas(con, esquema, _comprobante.getVersion(), "E");
			
			// logger.info("datoXMLSistema===>" + datoXMLSistema.size());
			EtiquetasForm etiquetasForm = null;
			 //ConfxmlForm confxmlFormMapa = null;
			 
			 ArrayList<String> datosValidar = null;
			 String datoXML = "";
			 boolean bandEncontro = false;
			 if (datoXMLSistema.isEmpty() || datoXMLSistema.size() <= 0){
				 mapaRes.put("ERROR", "TRUE");
			 }else{
				 int totEtiquetas = datoXMLSistema.size();
				 int numEtiqueta = 0;
				 String cadSinCaracteres = "";
				 
				 	for (int x = 0; x < datoXMLSistema.size(); x++){
						etiquetasForm = datoXMLSistema.get(x);
						bandEncontro = false;
						if (!"".equalsIgnoreCase(etiquetasForm.getEtiqueta())){ // si esta en el mapa, entonces se valida porque cumplio
							numEtiqueta++;
							datosValidar = etiquetasBean.obtenerDatoValidar(con, esquema, etiquetasForm.getEtiqueta(), _comprobante.getVersion());
							if ("M".equalsIgnoreCase(etiquetasForm.getTipoEtiqueta())){ // si es multiple opcion, viene una propiedad
								//	datoXML = buscarElementoComplemento(etiquetasForm.getEtiqueta(), _comprobante);
								 datoXML = buscarElemento(etiquetasForm.getEtiqueta(), _comprobante);
								 String sinSaltoLinea = datoXML.replace("\n", " ");
								 datoXML = sinSaltoLinea;
							}
							logger.info(" ******* Validadando Etiqueta ******* "+datoXML);
							for (int y = 0; y < datosValidar.size(); y++){
								cadSinCaracteres = Utils.replaceCaracteresEspeciales(datoXML);
								boolean bandArriba = false;
								if (cadSinCaracteres.equals(datosValidar.get(y)) ){
									bandEncontro = true;
									mapaRes.put("ERROR", "TRUE");
									bandArriba = true;
									//mapaRes.put("MENSAJE", "Error, la etiqueta contiene caracteres especiales");
									break;
								}
								
								if (!bandArriba && cadSinCaracteres.indexOf(datosValidar.get(y)) >= 0 ){
									bandEncontro = true;
									mapaRes.put("ERROR", "TRUE");
									//mapaRes.put("MENSAJE", "Error, la etiqueta contiene caracteres especiales");
									break;
								}
								
							}
							logger.info(" ******* bandEncontro ******* "+bandEncontro);
							logger.info(" ******* Fin de Validacion  ******* "+datoXML);
							if (bandEncontro){
								if (totEtiquetas == numEtiqueta ){
									break;
								}
							}else{
								//confxmlFormMapa = mapaEtiquetas.get(confxmlForm.getEtiqueta());
								logger.info(" ******* Mensaje  ******* "+etiquetasForm.getMensaje());
								mapaRes.put("SUBJECT", etiquetasForm.getSubject());
								mapaRes.put("MENSAJE", etiquetasForm.getMensaje());
								mapaRes.put("ERROR", "FALSE");
								break;
							}
						}
					}
			 }
		}catch(Exception e){
			Utils.imprimeLog("validarEtiquetasXML(): ", e);
		}
		return mapaRes;
   }
	
	public String buscarElemento(String elementoXML, Comprobante _comprobante) {
		String valorXML = null;
		try {
			// logger.info(" ******* elementoXML ******* "+elementoXML);
			if ("version".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getVersion();
			}else if ("moneda".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getMoneda();
			}else if ("tipoDeComprobante".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getTipoDeComprobante();
			}else if ("serie".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getSerie();
			}else if ("folio".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getFolio();
			}else if ("noCertificado".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getNoCertificado();
			}else if ("exportacion".equalsIgnoreCase(elementoXML)) {
				logger.info(" ******* _comprobante.getExportacion() ******* "+_comprobante.getExportacion());
				valorXML = _comprobante.getExportacion();
			}else if ("lugarExpedicion".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getLugarExpedicion();
			}else if ("sello".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getSello();
			}else if ("rfcEmisor".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getEmisor().getRfc();
			}else if ("nombreEmisor".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getEmisor().getNombre();
			}else if ("regimenFiscalEmisor".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getEmisor().getRegimenFiscal();
			}else if ("rfcReceptor".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getReceptor().getRfc();
			}else if ("nombreReceptor".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getReceptor().getNombre();
			}else if ("domicilioFiscalReceptor".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getReceptor().getDomicilioFiscalReceptor();
			}else if ("regimenFiscalReceptor".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getReceptor().getRegimenFiscalReceptor();
			}else if ("usoCFDIReceptor".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getReceptor().getUsoCFDI();
			}else if ("rfcProvCertif".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getComplemento().getTimbreFiscalDigital().getRfcProvCertif();
			}else if ("formaPago".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getFormaPago();
			}else if ("metodoPago".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getMetodoPago();
			}else if ("MonedaP".equalsIgnoreCase(elementoXML) || 
							"RfcEmisorCtaOrd".equalsIgnoreCase(elementoXML) || 
								"CtaOrdenante".equalsIgnoreCase(elementoXML) || 
									"RfcEmisorCtaBen".equalsIgnoreCase(elementoXML) || 
										"CtaBeneficiario".equalsIgnoreCase(elementoXML) ) {
				
				
			}else {
				valorXML = "";
			}
			return valorXML;
		}catch(Exception e) {
			Utils.imprimeLog("buscarElementoComplemento(): ", e);
		}
		return valorXML;
	}
	
	
	
	public  HashMap<String, String> validarEtiquetasBase(Connection con, String esquema, String esquemaEmpresa, Comprobante _comprobante){
		EtiquetasBean etiquetasBean = new EtiquetasBean();
		HashMap<String, String> mapaRes = new HashMap<String, String>();

		try{
			mapaRes.put("SUBJECT", "");
			mapaRes.put("MENSAJE", "");
			mapaRes.put("ERROR", "TRUE");

			ArrayList<EtiquetasForm> datoXMLSistema = etiquetasBean.obtenerEtiquetas(con, esquema, _comprobante.getVersion(), "B");
			//logger.info("datoXMLSistema===>" + datoXMLSistema.size());
			EtiquetasForm etiquetasForm = null;
			 
			 boolean bandEncontro = false;
			 if (datoXMLSistema.isEmpty() || datoXMLSistema.size() <= 0){
				 mapaRes.put("ERROR", "TRUE");
			 }else{
				// int totEtiquetas = datoXMLSistema.size();
				  // int numEtiqueta = 0;
				 // String cadSinCaracteres = "";
				//  logger.info("datoXMLSistema===>"+datoXMLSistema);
					for (int x = 0; x < datoXMLSistema.size(); x++){
						etiquetasForm = datoXMLSistema.get(x);
						// logger.info("Etiqueta===>"+etiquetasForm.getEtiqueta());
						bandEncontro = buscarElementosBase(con, esquema, esquemaEmpresa,  _comprobante, etiquetasForm.getEtiqueta());
						if (bandEncontro) {
							// numEtiqueta++;
						}else {
							mapaRes.put("SUBJECT", etiquetasForm.getSubject());
							mapaRes.put("MENSAJE", etiquetasForm.getMensaje());
							mapaRes.put("ERROR", "FALSE");
							break;
						}
					}
			 }
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return mapaRes;
   }
	
	
	private boolean  buscarElementosBase(Connection con, String esquema, String esquemaEmpresa, Comprobante _comprobante, String etiquetaBase) {
		boolean isEncontro = false;
		try {
			
			if ("rfcEmisor".equalsIgnoreCase(etiquetaBase)) {
				ProveedoresBean provBean = new ProveedoresBean();
				ProveedoresForm provForm = provBean.consultarProveedorXrfc(con, esquema, _comprobante.getEmisor().getRfc());
				if (Utils.noNulo(_comprobante.getEmisor().getRfc()).equalsIgnoreCase(provForm.getRfc())) {
					isEncontro = true;
				}
			}else if ("nombreEmisor".equalsIgnoreCase(etiquetaBase)) {
				ProveedoresBean provBean = new ProveedoresBean();
				ProveedoresForm provForm = provBean.consultarProveedorXrfc(con, esquema, _comprobante.getEmisor().getRfc());
				if (Utils.noNulo(_comprobante.getEmisor().getNombre()).equalsIgnoreCase(provForm.getRazonSocial())) {
					isEncontro = true;
				}
				
			}else if ("domicilioFiscalEmisor".equalsIgnoreCase(etiquetaBase)) {
				ProveedoresBean provBean = new ProveedoresBean();
				ProveedoresForm provForm = provBean.consultarProveedorXrfc(con, esquema, _comprobante.getEmisor().getRfc());
				if (Utils.noNuloINT(_comprobante.getLugarExpedicion()) == Utils.noNuloINT(provForm.getCodigoPostal())) {
					isEncontro = true;
				}
				
			}else if ("regimenFiscalEmisor".equalsIgnoreCase(etiquetaBase)) {
				
				ProveedoresBean provBean = new ProveedoresBean();
				ProveedoresForm provForm = provBean.consultarProveedorXrfc(con, esquema, _comprobante.getEmisor().getRfc());
				RegimenFiscalBean regimenBean = new RegimenFiscalBean();
				JSONObject jsonRegimenFiscal = regimenBean.buscarConfigurados(con, esquema, provForm.getClaveRegistro());
				boolean bandRegimenFiscal = false;
				if(jsonRegimenFiscal == null || jsonRegimenFiscal.size() <= 0) {
					bandRegimenFiscal = false;
				} else {
					if(jsonRegimenFiscal.containsKey(Utils.noNulo(_comprobante.getEmisor().getRegimenFiscal()))) {
						bandRegimenFiscal = true;
					}
				}
				if (bandRegimenFiscal) {
					isEncontro = true;
				}else {
					if (Utils.noNulo(_comprobante.getEmisor().getRegimenFiscal()).equalsIgnoreCase(provForm.getRegimenFiscal())) {
						isEncontro = true;
					}
				}
				
				
			}else if ("rfcReceptor".equalsIgnoreCase(etiquetaBase)) {
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquemaEmpresa);
				if (Utils.noNulo(_comprobante.getReceptor().getRfc()).equalsIgnoreCase(empresaForm.getRfc())) {
					isEncontro = true;
				}
			}else if ("nombreReceptor".equalsIgnoreCase(etiquetaBase)) {
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquemaEmpresa);
				if (Utils.noNulo(_comprobante.getReceptor().getNombre()).equalsIgnoreCase(empresaForm.getNombreLargo())) {
					isEncontro = true;
				}
			}else if ("domicilioFiscalReceptor".equalsIgnoreCase(etiquetaBase)) {
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquemaEmpresa);
				if (_comprobante.getReceptor().getDomicilioFiscalReceptor() != null) {
					if (Integer.parseInt(_comprobante.getReceptor().getDomicilioFiscalReceptor()) == empresaForm.getCodigoPostal()) {
						isEncontro = true;
					}
					
				}
			}else if ("regimenFiscalReceptor".equalsIgnoreCase(etiquetaBase)) {
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquemaEmpresa);
				if (_comprobante.getReceptor().getRegimenFiscalReceptor() != null) {
					if (Utils.noNulo(_comprobante.getReceptor().getRegimenFiscalReceptor()).equalsIgnoreCase(empresaForm.getRegimenFiscal())) {
						isEncontro = true;
					}
				}
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return isEncontro;
	}
	
	
	
	/*
	
	public  HashMap<String, String> validarEtiquetasXML(Connection con, String esquema, Comprobante _comprobante){
		EtiquetasBean etiquetasBean = new EtiquetasBean();
		HashMap<String, String> mapaRes = new HashMap<String, String>();
		try{
			mapaRes.put("SUBJECT", "");
			mapaRes.put("MENSAJE", "");
			mapaRes.put("ERROR", "SIN_VALIDAR");
			String versionXML = null;
			
				versionXML = Utils.noNuloNormal(_comprobante.getVersion());
				if("".equalsIgnoreCase(versionXML)) {
					versionXML = "3.3";
				}	
			
				logger.info("versionXML===>"+versionXML);
			// logger.info(" CargaProveedores versionXML---->" + versionXML);
			 Map<String, EtiquetasForm>  mapaEtiquetas = etiquetasBean.obtenerConfEtiquetas(con, esquema, versionXML);
			 ArrayList<EtiquetasForm> datoXMLSistema = etiquetasBean.obtenerEtiquetas(con, esquema, versionXML);
			 EtiquetasForm confxmlForm = null;
			 EtiquetasForm confxmlFormMapa = null;
			 
			 ArrayList<String> datosValidar = null;
			 String datoXML = "";
			 boolean bandEncontro = false;
			 if (mapaEtiquetas.isEmpty()){
				 mapaRes.put("ERROR", "TRUE");
			 }else{
				 int totEtiquetas = mapaEtiquetas.size();
				 int numEtiqueta = 0;
				 String cadSinCaracteres = "";
				 logger.info("mapaEtiquetas===>"+mapaEtiquetas);
					for (int x = 0; x < datoXMLSistema.size(); x++){
						confxmlForm = datoXMLSistema.get(x);
						bandEncontro = false;
						if (mapaEtiquetas.get(confxmlForm.getEtiqueta()) != null ){ // si esta en el mapa, entonces se valida porque cumplio
							numEtiqueta++;
							datosValidar = etiquetasBean.obtenerDatoValidar(con, esquema, confxmlForm.getEtiqueta(), versionXML);
							logger.info("tipoEtiqueta===>"+confxmlForm.getTipoEtiqueta());
							if ("M".equalsIgnoreCase(confxmlForm.getTipoEtiqueta())){ // si es multiple opcion, viene una propiedad
								//String fechaFact = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@fecha");
								datoXML = buscarElemento(confxmlForm.getPropiedad(), _comprobante);
							}
							logger.info(" ******* Validadando Etiqueta ******* "+datoXML);
							for (int y = 0; y < datosValidar.size(); y++){
								logger.info("dato a Validar---->"+datosValidar.get(y));
								cadSinCaracteres = Utils.replaceCaracteresEspeciales(datoXML);
								logger.info("cadSinCaracteres de la etiqueta---->"+cadSinCaracteres);
								if (cadSinCaracteres.equals(datosValidar.get(y)) ){
									bandEncontro = true;
									mapaRes.put("ERROR", "TRUE");
									break;
								}
							}
							logger.info(" ******* bandEncontro ******* "+bandEncontro);
							logger.info(" ******* Fin de Validacion  ******* "+datoXML);
							if (bandEncontro){
								logger.info(" ******* totEtiquetas  ******* "+totEtiquetas);
								logger.info(" ******* numEtiqueta  ******* "+numEtiqueta);
								if (totEtiquetas == numEtiqueta ){
									break;
								}
							}else{
								confxmlFormMapa = mapaEtiquetas.get(confxmlForm.getEtiqueta());
								mapaRes.put("SUBJECT", confxmlFormMapa.getSubject());
								mapaRes.put("MENSAJE", confxmlFormMapa.getMensaje());
								mapaRes.put("ERROR", "FALSE");
								break;
							}
						}
						
						
					}
			 }
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return mapaRes;
	}
	
	public String buscarElemento(String elementoXML, Comprobante _comprobante) {
		String valorXML = null;
		try {
			
			if ("formaDePago".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getFormaPago();
			}else if ("metodoPago".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getMetodoPago();
			}else if ("version".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getVersion();
			}else if ("moneda".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getMoneda();
			}else if ("TipoDeComprobante".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getTipoDeComprobante();
			}else if ("Certificado".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getCertificado();
			}else if ("TipoCambio".equalsIgnoreCase(elementoXML)) {
				valorXML = String.valueOf(_comprobante.getTipoCambio());
			}else if ("Fecha".equalsIgnoreCase(elementoXML)) {
				LocalDateTime fechaLD  = _comprobante.getFecha();
				if(fechaLD != null) {
					valorXML = fechaLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		    	}
			}else if ("Folio".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getFolio();
			}else if ("codigoPostal".equalsIgnoreCase(elementoXML)) {
				//valorXML = _comprobante.getComplemento().CartaPorte20.getUbicaciones().getUbicacion().get(0).getDomicilio().getCodigoPostal();
			}else if ("rfc".equalsIgnoreCase(elementoXML)) {
				//valorXML = _comprobante.getComplemento().CartaPorte20.getUbicaciones().getUbicacion().get(0).getRFCRemitenteDestinatario();
			}else if ("UsoCFDI".equalsIgnoreCase(elementoXML)) {
				valorXML = _comprobante.getReceptor().getUsoCFDI();
			}else {
				valorXML = "";
			}
			return valorXML;
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return valorXML;
	}

	*/
	
	
}
