package com.siarex247.validaciones;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.itextpdf.xmltopdf.CartaPorte20.Mercancia;
import com.itextpdf.xmltopdf.CartaPorte20.Ubicacion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.catalogos.Proveedores.ProveedoresQuerys;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.configuraciones.ConfClaveUsoCFDI.ConfClaveUsoCFDIForm;
import com.siarex247.configuraciones.EtiquetasCP.EtiquetasCPBean;
import com.siarex247.configuraciones.EtiquetasCP.EtiquetasCPForm;
import com.siarex247.utils.MensajesSIAREX;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class ValidacionesCartaPorte {

	public static final Logger logger = Logger.getLogger("siarex247");
		
	
	public HashMap<String, Object> procesarXML(Connection con, String esquema, File fileXML, File filePDF, String usuarioHTTP, String idLenguaje, 
					boolean bandElimnaArchivos, long folioEmpresa, double total, long folioOrden, String tipoAcceso){
    	HashMap<String, Object> mapaResultado = new HashMap<>();
    	
    	Comprobante _comprobante = null;

		try{
				mapaResultado.put("ERROR", "OK");
				mapaResultado.put("MENSAJE", "OK");
				mapaResultado.put("SUBJECT", "OK");
				
			    String nombreXML = null;
			    String nombrePDF = null;
			    String rutaDesXML = null;
				String rutaDesPDF = null;

				_comprobante = LeerXML.ObtenerComprobante(fileXML.getAbsolutePath());

				if (_comprobante == null) {
					mapaResultado.put("MENSAJE", Utils.regresaCaracteresNormales(new MensajesSIAREX(idLenguaje).MENSAJE69));
	    			return mapaResultado;
				}
				else {
					String tipoDeComprobante = _comprobante.getTipoDeComprobante(); //XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@TipoDeComprobante");
					if (!"I".equalsIgnoreCase(tipoDeComprobante) && !"T".equalsIgnoreCase(tipoDeComprobante)) {
						MensajesSIAREX MensajeSiarex = new MensajesSIAREX(idLenguaje);
						mapaResultado.put("ERROR", "ERROR");
						mapaResultado.put("SUBJECT", "");
						mapaResultado.put("MENSAJE", Utils.regresaCaracteresNormales(MensajeSiarex.MENSAJE70));
		    			return mapaResultado;
					}
					else {
						HashMap<String, String> validaciones = new HashMap<>();
						
						String rfcEmisor = _comprobante.getEmisor().getRfc(); // XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Emisor/@Rfc");

						String rfcReceptor = _comprobante.getReceptor().getRfc(); //XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Receptor/@Rfc");
						
						String UUID = _comprobante.getComplemento().getTimbreFiscalDigital().getUUID(); //XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/TimbreFiscalDigital/@UUID");
						String usoCFDI_XML = _comprobante.getReceptor().getUsoCFDI(); //XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Receptor/@UsoCFDI");
						
						String versionCartaPorte = null;
						try {
							versionCartaPorte = _comprobante.getComplemento().getCartaPorte10().getVersion(); //XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/CartaPorte/@Version");
						}catch(Exception e) {
							versionCartaPorte = null;
						}
						
						try {
							if (versionCartaPorte == null) {
								versionCartaPorte = _comprobante.getComplemento().getCartaPorte20().getVersion();
							}	
						}catch(Exception e) {
							versionCartaPorte = null;
						}
						
						try {
							if (versionCartaPorte == null) {
								versionCartaPorte = _comprobante.getComplemento().getCartaPorte30().getVersion();
							}	
						}catch(Exception e) {
							versionCartaPorte = null;
						}

						try {
							if (versionCartaPorte == null) {
								versionCartaPorte = _comprobante.getComplemento().getCartaPorte31().getVersion();
							}	
						}catch(Exception e) {
							versionCartaPorte = "3.1";
						}

						
						boolean cumplioVersion = false;
						
						if(versionCartaPorte == null || "".equalsIgnoreCase(versionCartaPorte)) {
							versionCartaPorte = "1.0";
						}else {
							cumplioVersion = true;
						}
						
						JSONArray jsonConceptos = null;
						
						if (cumplioVersion) {
							if("1.0".equalsIgnoreCase(versionCartaPorte)) {
								jsonConceptos = getConceptos10(_comprobante);
							} else if ("2.0".equalsIgnoreCase(versionCartaPorte)) {
								jsonConceptos = getConceptos20(_comprobante);
							}else if ("3.0".equalsIgnoreCase(versionCartaPorte)) {
								jsonConceptos = getConceptos30(_comprobante);
							}else if ("3.1".equalsIgnoreCase(versionCartaPorte)) {
								jsonConceptos = getConceptos31(_comprobante);
							}
						}
						
						
						String formaPago = "";
						 // HashMap<String, String> mapaConf = confSistemaBean.obtenerConfiguraciones(con, esquema);
						  
						//String PERMITIR_CARTA_PORTE =  mapaConf.get("PERMITIR_CARTA_PORTE"); //obtenerConfiguracionesVariable(con, esquema, "PERMITIR_CARTA_PORTE");
						String PERMITIR_CARTA_PORTE = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "PERMITIR_CARTA_PORTE");
						//String validaCARTA = mapaConf.get("RECHAZAR_CARTA_PORTE");
						
						if ("I".equalsIgnoreCase(tipoDeComprobante)) {
							if ("FACTURA".equalsIgnoreCase(tipoAcceso)) {
								LocalDateTime fechaTimbradoXML = _comprobante.getComplemento().getTimbreFiscalDigital().getFechaTimbrado();
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
								String fechaTimbrado = "";
								if(fechaTimbradoXML != null) {
									fechaTimbrado = fechaTimbradoXML.format(formatter);
						    	}
								 
								
								if(fechaTimbrado.length() > 10) {
									fechaTimbrado = fechaTimbrado.substring(0, 10);
								}
								
								String valBandera[] = banderaCartaPorteRFC(con, esquema, rfcEmisor);
								int claveProveedor = Utils.noNuloINT(valBandera[0]);
								String bandCartaPorte = Utils.noNuloNormal(valBandera[1]);
								if("S".equalsIgnoreCase(bandCartaPorte)) {
									if("1.0".equalsIgnoreCase(versionCartaPorte)) {
										// validaciones = new CartaPorteValidaciones().validarEtiquetasCartaPorte(con, esquema, document, claveProveedor);
										 if (cumplioVersion) {
											// validaciones = validarEtiquetasCartaPorte(con, esquema, fileXML, folioEmpresa, claveProveedor, idLenguaje, usuarioHTTP); // revisar esta seccion ya que se elimino el jar
											 
										}
									} else if ("2.0".equalsIgnoreCase(versionCartaPorte)) {
										if (cumplioVersion) {
											validaciones = validarEtiquetasCartaPorte20(con, esquema, folioOrden, claveProveedor, _comprobante, versionCartaPorte, idLenguaje, usuarioHTTP); 
													
										}
									}else if ("3.0".equalsIgnoreCase(versionCartaPorte)) {
										if (cumplioVersion) {
											validaciones = validarEtiquetasCartaPorte30(con, esquema, folioOrden, claveProveedor, _comprobante, versionCartaPorte, idLenguaje, usuarioHTTP); 
													
										}
									}else if ("3.1".equalsIgnoreCase(versionCartaPorte)) {
										if (cumplioVersion) {
											validaciones = validarEtiquetasCartaPorte31(con, esquema, folioOrden, claveProveedor, _comprobante, versionCartaPorte, idLenguaje, usuarioHTTP); 
													
										}
									}
									 if (!cumplioVersion) {
										MensajesSIAREX MensajeSiarex = new MensajesSIAREX(idLenguaje);
										mapaResultado.put("ERROR", "ERROR");
										mapaResultado.put("SUBJECT", "");
										mapaResultado.put("MENSAJE", Utils.regresaCaracteresNormales(MensajeSiarex.MENSAJE79));
										return mapaResultado;
									}else if ("N".equalsIgnoreCase(PERMITIR_CARTA_PORTE)) {
										mapaResultado.put("ERROR", "OK");
										mapaResultado.put("MENSAJE", "OK");
										mapaResultado.put("SUBJECT", "OK");
										mapaResultado.put("CLAVE_CARTA_PORTE", "0");	
										return mapaResultado;
									}else if(validaciones.get("ERROR").equalsIgnoreCase("TRUE")) {
										mapaResultado.put("ERROR", "ERROR");
										mapaResultado.put("MENSAJE", validaciones.get("MENSAJE"));
										mapaResultado.put("SUBJECT", validaciones.get("SUBJECT"));
						    			return mapaResultado;
									}else if (!validarUsoClave(con, esquema, rfcEmisor, usoCFDI_XML, jsonConceptos, folioEmpresa)){  // no encontro las claves y producto
										mapaResultado.put("ERROR", "A12");
										mapaResultado.put("MENSAJE", "OK");
										mapaResultado.put("SUBJECT", "");
										
						    			// return mapaResultado;
									}
									
									String MENSAJE = Utils.noNulo(mapaResultado.get("ERROR")).toString();
									if ("OK".equalsIgnoreCase(MENSAJE) || "A12".equalsIgnoreCase(MENSAJE)) {
										String fechaPago = null;
										String estatusCarta = "";

										//formaPago = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@FormaPago");
										formaPago = _comprobante.getFormaPago();
										int CLAVE_CARTA_PORTE = grabarCartaPorte(con, esquema, folioOrden, folioEmpresa, rfcEmisor, rfcReceptor, UUID, total, estatusCarta, fechaPago, fechaTimbrado, nombreXML, nombrePDF, tipoDeComprobante, formaPago, "OK", "OK", null, usuarioHTTP);
										if ("A12".equalsIgnoreCase(MENSAJE)) {
											mapaResultado.put("ERROR", "A12");
											mapaResultado.put("MENSAJE", "OK");
											mapaResultado.put("SUBJECT", "OK");
										}else {
											mapaResultado.put("ERROR", "OK");
											mapaResultado.put("MENSAJE", "OK");
											mapaResultado.put("SUBJECT", "OK");
											mapaResultado.put("CLAVE_CARTA_PORTE", CLAVE_CARTA_PORTE);	
										}
									}
								} else {
									mapaResultado.put("ERROR", "OK");
									mapaResultado.put("MENSAJE", "OK");
									mapaResultado.put("SUBJECT", "OK");

								}
							}else {
								MensajesSIAREX MensajeSiarex = new MensajesSIAREX(idLenguaje);
								mapaResultado.put("ERROR", "ERROR");
								mapaResultado.put("SUBJECT", "");
								mapaResultado.put("MENSAJE", Utils.regresaCaracteresNormales(MensajeSiarex.MENSAJE70));
							}
						} else if ("T".equalsIgnoreCase(tipoDeComprobante)) {
							
							//String fechaTimbrado = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Fecha");
							//String totalXML = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Total");
							
							logger.info("tipoDeComprobante========>"+tipoDeComprobante);
							LocalDateTime fechaTimbradoXML = _comprobante.getFecha();
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							String fechaTimbrado = "";
							if(fechaTimbradoXML != null) {
								fechaTimbrado = fechaTimbradoXML.format(formatter);
					    	}

							double totalXML = _comprobante.getTotal();

							if(fechaTimbrado.length() > 10) {
								fechaTimbrado = fechaTimbrado.substring(0, 10);
							}
							
							nombreXML = "CP_"+ rfcEmisor+ UUID + ".xml";
							nombrePDF = "CP_"+ rfcEmisor+ UUID + ".pdf";

							String valBandera[] = banderaCartaPorteRFC(con, esquema, rfcReceptor);
							int claveProveedor = Utils.noNuloINT(valBandera[0]);
							String bandCartaPorte = Utils.noNuloNormal(valBandera[1]);
							String directorioXML = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquema + "/PROVEEDORES/" + claveProveedor + "/" + nombreXML;
							String directorioPDF = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquema + "/PROVEEDORES/" + claveProveedor +"/" + nombrePDF;
							if("S".equalsIgnoreCase(bandCartaPorte)) {
								int bandEmisor = banderaRFCEmisor(con, esquema, rfcEmisor);
								
								if(bandEmisor > 0) {
									if("1.0".equalsIgnoreCase(versionCartaPorte)) { // revisar esta seccion ya que se elimino el jar
										// validaciones = validarEtiquetasCartaPorte(con, esquema, fileXML, folioEmpresa, claveProveedor, idLenguaje, usuarioHTTP);
									} else if ("2.0".equalsIgnoreCase(versionCartaPorte)){
										validaciones = validarEtiquetasCartaPorte20(con, esquema, folioEmpresa, claveProveedor, _comprobante, versionCartaPorte, idLenguaje, usuarioHTTP);
									}else if ("3.0".equalsIgnoreCase(versionCartaPorte)) {
										validaciones = validarEtiquetasCartaPorte30(con, esquema, folioEmpresa, claveProveedor, _comprobante, versionCartaPorte, idLenguaje, usuarioHTTP);
									}else if ("3.1".equalsIgnoreCase(versionCartaPorte)) {
										validaciones = validarEtiquetasCartaPorte31(con, esquema, folioEmpresa, claveProveedor, _comprobante, versionCartaPorte, idLenguaje, usuarioHTTP);
									}
									// validaciones.put("ERROR", "FALSE"); // eliminar
									/*if("".equalsIgnoreCase(Utils.noNulo(UUID))) {
										mapaResultado.put("ERROR", "ERROR");
										mapaResultado.put("MENSAJE", Utils.regresaCaracteresNormales(new MensajesSIAREX(idLenguaje).MENSAJE75));
										mapaResultado.put("SUBJECT", "");
						    			return mapaResultado;
									}else*/ if (tieneCartaPorte(con, esquema, folioEmpresa)) { // validar si la orden ya tiene una carta porte
										mapaResultado.put("ERROR", "ERROR");
										mapaResultado.put("MENSAJE", Utils.regresaCaracteresNormales(new MensajesSIAREX(idLenguaje).MENSAJE76));
										mapaResultado.put("SUBJECT", "");
									}else if(validaciones.get("ERROR").equalsIgnoreCase("TRUE")) {
										mapaResultado.put("ERROR", "ERROR");
										mapaResultado.put("MENSAJE", validaciones.get("MENSAJE"));
										mapaResultado.put("SUBJECT", validaciones.get("SUBJECT"));
						    			//return mapaResultado;
									} else if(!rfcAsignado(con, esquema, bandEmisor, rfcReceptor)) {
										mapaResultado.put("ERROR", "ERROR");
										mapaResultado.put("MENSAJE", Utils.regresaCaracteresNormales(new MensajesSIAREX(idLenguaje).MENSAJE71));
										mapaResultado.put("SUBJECT", "");
						    			// return mapaResultado;
									} else if(!obtenerEstatusOrden(con, esquema, folioEmpresa)) {
										mapaResultado.put("ERROR", "ERROR");
										mapaResultado.put("MENSAJE", Utils.regresaCaracteresNormales(new MensajesSIAREX(idLenguaje).MENSAJE72));
										mapaResultado.put("SUBJECT", "");
						    			// return mapaResultado;
									}else if (!validarUsoClave(con, esquema, rfcReceptor, usoCFDI_XML, jsonConceptos, folioEmpresa)){  // no encontro las claves y producto
										mapaResultado.put("ERROR", "A12");
										mapaResultado.put("MENSAJE", "OK");
										mapaResultado.put("SUBJECT", "");
										
						    			// return mapaResultado;
									}
									
									String MENSAJE = Utils.noNulo(mapaResultado.get("ERROR")).toString();
									if ("OK".equalsIgnoreCase(MENSAJE) || "A12".equalsIgnoreCase(MENSAJE)) {
										  // ConfSistemaBean confSistemaBean = new ConfSistemaBean();
										  // HashMap<String, String> mapaConf = confSistemaBean.obtenerConfiguraciones(con, esquema);
										//String validaCARTA = mapaConf.get("RECHAZAR_CARTA_PORTE");
										String validaCARTA = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "RECHAZAR_CARTA_PORTE");
										
										String estadoSAT = "";
										String estatusSAT = "";
										validaCARTA = "S";
										if ("S".equalsIgnoreCase(validaCARTA)) {
											
											String datosSAT [] = UtilsSAT.validaSAT(rfcEmisor, rfcReceptor, totalXML, UUID);
											//String datosSAT [] = {"Vigente","S - Comprobante obtenido satisfactoriamente."};
											estadoSAT = datosSAT[0];
											estatusSAT = datosSAT[1];
											
											if (estadoSAT.toUpperCase().indexOf("CANCELADO") > -1 ) {
												MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
												//cadenaRetorno =  "CANCELADO"+ ";" + mensajeSIAREX.MENSAJE29;
												String mensajeFinal = Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE29), "<< UUID_COMPLE >>", UUID); 
												mapaResultado.put("MENSAJE", mensajeFinal);
												return mapaResultado;
											}
										} // FOLIO_EMPRESA
										
										
										CartasPorteForm cartaForm  = datosOrden(con, esquema, folioEmpresa);
										String fechaPago = "";
											if(total == 0) {
												total = cartaForm.getTotal();
											}

											folioOrden = cartaForm.getFolioOrden();
											fechaPago  = cartaForm.getFechaPago();
											//String datosSAT [] = validaSAT(rfcFacturaXML, rfcReceptorXML, totalXML, uuidComplemento);
											//estatusCarta  = cartaForm.getEstatusCarta();

											if(fechaPago.length() > 10) {
												fechaPago = fechaPago.substring(0, 10);
											}
										

										int CLAVE_CARTA_PORTE = grabarCartaPorte(con, esquema, folioOrden, folioEmpresa, rfcEmisor, rfcReceptor, UUID, total, estatusSAT, fechaPago, fechaTimbrado, nombreXML, nombrePDF, tipoDeComprobante, formaPago, "OK", "OK", cartaForm.getEstatusCarta(), usuarioHTTP);
										
										if ("A12".equalsIgnoreCase(MENSAJE)) {
											// se actualiza la orden de compra a A12
											actualizarEstatusOrden(con, esquema, folioEmpresa, "A12",usuarioHTTP);
											mapaResultado.put("ERROR", "A12");
											mapaResultado.put("MENSAJE", Utils.regresaCaracteresNormales(new MensajesSIAREX(idLenguaje).MENSAJE77));
											mapaResultado.put("SUBJECT", "OK");
										}else {
											mapaResultado.put("ERROR", "OK");
											mapaResultado.put("MENSAJE", "OK");
											mapaResultado.put("SUBJECT", "OK");
											mapaResultado.put("CLAVE_CARTA_PORTE", CLAVE_CARTA_PORTE);	
										}
										rutaDesXML = directorioXML;
										File fdesXML = new File(rutaDesXML);
									    UtilsFile.moveFileDirectory(fileXML, fdesXML, true, false, true, bandElimnaArchivos);

									    rutaDesPDF = directorioPDF;
									    File fdesPDF = new File(rutaDesPDF);
									    UtilsFile.moveFileDirectory(filePDF, fdesPDF, true, false, true, bandElimnaArchivos);
									}
								}else {
									
									MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
									String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE74);
									mapaResultado.put("ERROR", "ERROR");
									mapaResultado.put("MENSAJE", mensajeFinal);
									mapaResultado.put("SUBJECT", "");
					    			return mapaResultado;
								}
							} else {
								
								MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
								String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE73);
								mapaResultado.put("ERROR", "ERROR");
								mapaResultado.put("MENSAJE", mensajeFinal);
								mapaResultado.put("SUBJECT", "OK");
				    			return mapaResultado;
							}
						}
					}
				}
				
				// NUEVAS VALIDACIONES
				// ************* COMPROBANTE TIPO = I ******************
				// 1. Validar que el tipoComprobante = I, si no retorna mensaje de error
				// 2. Validar si el RFC Emisor tiene activa la opcion de CARTA_PORTE
				// 3. Mandar llamar mi pieza de validaciones validarEtiquetasCartaPorte
				// CUMPLICO VALIDACIONES
				// 1. Guardar en tabla CARTA_PORTE
				// 2. Retornar mensaje de exito
				
				// ************* COMPROBANTE TIPO = T ******************
				// 1. Validar que el tipoComprobante = T, si no retorna mensaje de error
				// 2. Sacar el RFC Emisor y RFC Receptor
				//  2a Validar que el RFC Receptor tenga activado la opcion de CARTA PORTE, si no retorna mensaje
				//  2b Validar que el RFC Emisor, se encuentre en catalogo de proveedores externos y este asignado al RFC Receptor, si no retorna mensaje
				// 3. Validar el numero de orden de compra se encuentre en estatus A3, A4 o que el campo NOMBRE_XML tenga un valor (ORDENES), si no retorna mensaje de error
				// 4. Mandar llamar mi pieza de validaciones validarEtiquetasCartaPorte
				
				// CUMPLICO VALIDACIONES Tipo T
				// 1. Con el numero de orden sacar valores (FOLIO_ORDEN, FOLIO_EMPRESA, TOTAL ) para guardar en tabla CARTA_PORTE
				// 2. En los campos NOMBRE_XML y NOMBRE_PDF guardarlo el directorio igual que COMPLEMENTO
				// 2. Retornar mensaje de exito
		}
		catch(Exception e){
			Utils.imprimeLog("procesarXML(): ", e);
			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32); 
			mapaResultado.put("MENSAJE", mensajeFinal);
		}
		return mapaResultado;
	}

	
	
	@SuppressWarnings("unchecked")
	public JSONArray getConceptos10(Comprobante _comprobante){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonobj = null;
		
		try{
			
			if (_comprobante != null) {
				ArrayList<com.itextpdf.xmltopdf.CartaPorte10.Mercancia> mercancias = _comprobante.getComplemento().getCartaPorte10().getMercancias().getMercancia();
				
				for (int i = 0; i < mercancias.size(); i++) {
					jsonobj = new JSONObject();

					jsonobj.put("bienesTransp", mercancias.get(i).getBienesTransp() == null ? "" : mercancias.get(i).getBienesTransp());
					jsonobj.put("descripcion", mercancias.get(i).getDescripcion() == null ? "" : mercancias.get(i).getDescripcion());
					jsonobj.put("unidad", String.valueOf(mercancias.get(i).getValorMercancia()) == null ? "" : String.valueOf(mercancias.get(i).getValorMercancia()));
					jsonobj.put("claveUnidad", mercancias.get(i).getClaveUnidad() == null ? "" : mercancias.get(i).getClaveUnidad());
					jsonobj.put("cantidad", String.valueOf(mercancias.get(i).getCantidad()) == null ? "" : String.valueOf(mercancias.get(i).getCantidad()));
					jsonobj.put("fraccionArancelaria", mercancias.get(i).getFraccionArancelaria() == null ? "" : mercancias.get(i).getFraccionArancelaria());
					jsonobj.put("pesoKG", String.valueOf(mercancias.get(i).getPesoEnKg()) == null ? "" : String.valueOf(mercancias.get(i).getPesoEnKg()));
					//jsonobj.put("materialPeligroso", mercancias.get(i).getMaterialPeligroso());
					//jsonobj.put("embalaje", mercancias.get(i).getEmbalaje());

					jsonArray.add(jsonobj);
				}
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return jsonArray;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public JSONArray getConceptos20(Comprobante _comprobante){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonobj = null;
		
		try{
			
			if (_comprobante != null) {
				ArrayList<Mercancia> mercancias = _comprobante.getComplemento().CartaPorte20.getMercancias().getMercancia();
				
				for (int i = 0; i < mercancias.size(); i++) {
					jsonobj = new JSONObject();

					jsonobj.put("bienesTransp", mercancias.get(i).getBienesTransp() == null ? "" : mercancias.get(i).getBienesTransp());
					jsonobj.put("descripcion", mercancias.get(i).getDescripcion() == null ? "" : mercancias.get(i).getDescripcion());
					jsonobj.put("unidad", String.valueOf(mercancias.get(i).getValorMercancia()) == null ? "" : String.valueOf(mercancias.get(i).getValorMercancia()));
					jsonobj.put("claveUnidad", mercancias.get(i).getClaveUnidad() == null ? "" : mercancias.get(i).getClaveUnidad());
					jsonobj.put("cantidad", String.valueOf(mercancias.get(i).getCantidad()) == null ? "" : String.valueOf(mercancias.get(i).getCantidad()));
					jsonobj.put("fraccionArancelaria", mercancias.get(i).getFraccionArancelaria() == null ? "" : mercancias.get(i).getFraccionArancelaria());
					jsonobj.put("pesoKG", String.valueOf(mercancias.get(i).getPesoEnKg()) == null ? "" : String.valueOf(mercancias.get(i).getPesoEnKg()));
					//jsonobj.put("materialPeligroso", mercancias.get(i).getMaterialPeligroso());
					//jsonobj.put("embalaje", mercancias.get(i).getEmbalaje());

					jsonArray.add(jsonobj);
				}
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return jsonArray;
	}

	
	@SuppressWarnings("unchecked")
	public JSONArray getConceptos30(Comprobante _comprobante){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonobj = null;
		
		try{
			
			if (_comprobante != null) {
				ArrayList<com.itextpdf.xmltopdf.CartaPorte30.Mercancia> mercancias = _comprobante.getComplemento().getCartaPorte30().getMercancias().getMercancia();
				
				for (int i = 0; i < mercancias.size(); i++) {
					jsonobj = new JSONObject();

					jsonobj.put("bienesTransp", mercancias.get(i).getBienesTransp() == null ? "" : mercancias.get(i).getBienesTransp());
					jsonobj.put("descripcion", mercancias.get(i).getDescripcion() == null ? "" : mercancias.get(i).getDescripcion());
					jsonobj.put("unidad", String.valueOf(mercancias.get(i).getValorMercancia()) == null ? "" : String.valueOf(mercancias.get(i).getValorMercancia()));
					jsonobj.put("claveUnidad", mercancias.get(i).getClaveUnidad() == null ? "" : mercancias.get(i).getClaveUnidad());
					jsonobj.put("cantidad", String.valueOf(mercancias.get(i).getCantidad()) == null ? "" : String.valueOf(mercancias.get(i).getCantidad()));
					jsonobj.put("fraccionArancelaria", mercancias.get(i).getFraccionArancelaria() == null ? "" : mercancias.get(i).getFraccionArancelaria());
					jsonobj.put("pesoKG", String.valueOf(mercancias.get(i).getPesoEnKg()) == null ? "" : String.valueOf(mercancias.get(i).getPesoEnKg()));
					//jsonobj.put("materialPeligroso", mercancias.get(i).getMaterialPeligroso());
					//jsonobj.put("embalaje", mercancias.get(i).getEmbalaje());

					jsonArray.add(jsonobj);
				}
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return jsonArray;
	}

	
	
	@SuppressWarnings("unchecked")
	public JSONArray getConceptos31(Comprobante _comprobante){
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonobj = null;
		
		try{
			
			if (_comprobante != null) {
				ArrayList<com.itextpdf.xmltopdf.CartaPorte31.Mercancia> mercancias = _comprobante.getComplemento().getCartaPorte31().getMercancias().getMercancia();
				
				for (int i = 0; i < mercancias.size(); i++) {
					jsonobj = new JSONObject();

					jsonobj.put("bienesTransp", mercancias.get(i).getBienesTransp() == null ? "" : mercancias.get(i).getBienesTransp());
					jsonobj.put("descripcion", mercancias.get(i).getDescripcion() == null ? "" : mercancias.get(i).getDescripcion());
					jsonobj.put("unidad", String.valueOf(mercancias.get(i).getValorMercancia()) == null ? "" : String.valueOf(mercancias.get(i).getValorMercancia()));
					jsonobj.put("claveUnidad", mercancias.get(i).getClaveUnidad() == null ? "" : mercancias.get(i).getClaveUnidad());
					jsonobj.put("cantidad", String.valueOf(mercancias.get(i).getCantidad()) == null ? "" : String.valueOf(mercancias.get(i).getCantidad()));
					jsonobj.put("fraccionArancelaria", mercancias.get(i).getFraccionArancelaria() == null ? "" : mercancias.get(i).getFraccionArancelaria());
					jsonobj.put("pesoKG", String.valueOf(mercancias.get(i).getPesoEnKg()) == null ? "" : String.valueOf(mercancias.get(i).getPesoEnKg()));
					//jsonobj.put("materialPeligroso", mercancias.get(i).getMaterialPeligroso());
					//jsonobj.put("embalaje", mercancias.get(i).getEmbalaje());

					jsonArray.add(jsonobj);
				}
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return jsonArray;
	}
	
	
	public String[] banderaCartaPorteRFC(Connection con, String esquema, String rfcProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String valBandera[] = {"0", "N"};

		try {		
			stmt = con.prepareStatement(ProveedoresQuerys.getIsCartaPorteRFC(esquema));
			stmt.setString(1, rfcProveedor);
			rs  = stmt.executeQuery();
			if  (rs.next()) {
				valBandera[0] = Utils.noNulo(rs.getString(1)); //CLAVE_PROVEEDOR
				valBandera[1] = Utils.noNulo(rs.getString(2)); //CARTA_PORTE
			}
		}
		catch(Exception e) {
			Utils.imprimeLog("banderaCartaPorteRFC(): ", e);
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return valBandera;
	}

	
	/*
	public  HashMap<String, String> validarEtiquetasCartaPorte(Connection con, String esquema, File fileXML, long folioOrden, int claveProveedor, String idLenguaje, String usuarioHTTP){
		HashMap<String, String> mapaEtiquetas = new HashMap<>();
		EtiquetasCPBean cartaPorteBean = new EtiquetasCPBean();		
		
		SimpleDateFormat formaDate = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaHoy = new Date();
		String fechaActual = "";
		String datoXML = "";
		ArrayList<String> datosValidar = null;
		String cadSinCaracteres = "";
		boolean bandEncontro = false;
		ArrayList<ReporteForm> listaReporte = new ArrayList<>();
		ReporteForm reporteForm = new ReporteForm();
		StringBuffer sbEtiquetas = new StringBuffer();
		
		try {
			
			String xmlFactura = UtilsFile.leeArchivo(fileXML.getAbsolutePath());

			int posXML = xmlFactura.indexOf("<cfdi:Comprobante ");
			if (posXML > -1 ){
				xmlFactura = xmlFactura.substring(posXML, xmlFactura.length());
			}

			Document document = XMLXPathManagerBase.parseXml(xmlFactura);
			
			
			fechaActual = formaDate.format(fechaHoy);
			mapaEtiquetas.put("ERROR", "false");
			mapaEtiquetas.put("SUBJECT", "");
			mapaEtiquetas.put("MENSAJE", "");
			// 1. Validar si el proveedor tiene encendida la opcion
			// 2. Obtener las etiquetas
			// 3. Validar si se encuentra dentro del rango de fechas de validacion
			// 4. Calcular los valores asignados por cada etiqueta
			// 5. Comparar el valor del XML vs Configurado, si existe es exitoso
			// String bandEtiqueta = cartaPorteBean.banderaCartaPorteProveedor(con, esquema, claveProveedor);
			ProveedoresBean provBean = new ProveedoresBean();
			ProveedoresForm provForm = provBean.consultarProveedor(con, esquema, claveProveedor);
			
			if ("S".equalsIgnoreCase(provForm.getCartaPorte())) {
				ArrayList<EtiquetasCPForm> listadoEtiquetas = cartaPorteBean.listadoActivos20(con, esquema, "1.0");
				EtiquetasCPForm cartaPorteForm = null;
				for (int x = 0; x < listadoEtiquetas.size(); x++) {
					cartaPorteForm = listadoEtiquetas.get(x);
					bandEncontro = false;
					boolean pasoFecha = UtilsFechas.compararDosFechas(cartaPorteForm.getFechaIni(), cartaPorteForm.getFechaFin(), fechaActual);
					if (pasoFecha) {
						
						String attributo = null;
						if (cartaPorteForm.getIdEtiqueta().indexOf("_Origen") > 0) {
							int pos = cartaPorteForm.getIdEtiqueta().indexOf("_Origen");
							attributo = cartaPorteForm.getIdEtiqueta().substring(0, pos);
							NodeList Origen = XMLXPathManagerBase.getNodeListByPath(document, cartaPorteForm.getPathXML()  +  "@" + attributo);
							List<Node> listOrigen = new ArrayList<Node>(Origen.getLength());
							for (int i = 0, l = Origen.getLength(); i < l; i++)
								listOrigen.add(Origen.item(i));
							datoXML = listOrigen.get(0).getNodeValue();
						}else if (cartaPorteForm.getIdEtiqueta().indexOf("_Destino") > 0) {
							int pos = cartaPorteForm.getIdEtiqueta().indexOf("_Destino");
							attributo = cartaPorteForm.getIdEtiqueta().substring(0, pos);
							NodeList Destino = XMLXPathManagerBase.getNodeListByPath(document, cartaPorteForm.getPathXML()  +  "@" + attributo);
							List<Node> listOrigen = new ArrayList<Node>(Destino.getLength());
							for (int i = 0, l = Destino.getLength(); i < l; i++)
								listOrigen.add(Destino.item(i));
							datoXML = listOrigen.get(1).getNodeValue();
						} else {
							datoXML = XMLXPathManagerBase.getNodeValue(document, cartaPorteForm.getPathXML()  +  "@" + cartaPorteForm.getIdEtiqueta());
						}
						
						reporteForm.setFolioOrden(folioOrden);
						reporteForm.setClaveProveedor(claveProveedor);
						reporteForm.setEtiquetaCartaPorte(cartaPorteForm.getIdEtiqueta());
						reporteForm.setValorXML(datoXML);
						
						if ("S".equalsIgnoreCase(cartaPorteForm.getValidarVacio())) {
							if (!"".equalsIgnoreCase(Utils.noNuloNormal(datoXML))){
								bandEncontro = true;
								reporteForm.setEstatus("000 - INFORMACION CORRECTA");
								reporteForm.setResultado("S");
							}else {
								reporteForm.setEstatus("001 - SIN INFORMACION");
								reporteForm.setResultado("N");
								sbEtiquetas.append(cartaPorteForm.getIdEtiqueta()).append(", ");
							}
						}else {
							// Falta cambiar logica de configuracion.....
							datosValidar = datosValidar(con, esquema, cartaPorteForm.getIdEtiqueta());
							for (int y = 0; y < datosValidar.size(); y++){
								cadSinCaracteres = Utils.replaceCaracteresEspeciales(Utils.noNuloNormal(datoXML));
								if (cadSinCaracteres.equals(datosValidar.get(y)) ){
									bandEncontro = true;
									reporteForm.setEstatus("000 - INFORMACION CORRECTA");
									reporteForm.setResultado("S");
									break;
								}
							}
							
							if (!bandEncontro) {
								reporteForm.setEstatus("002 - INFORMACION NO ENCONTRADA");
								reporteForm.setResultado("N");
								sbEtiquetas.append(cartaPorteForm.getIdEtiqueta()).append(", ");
							}							
						}

						
						listaReporte.add(reporteForm);
						reporteForm = new ReporteForm();
					}
				}
				
				if (sbEtiquetas.length() > 0) {
					MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
					String mensajeValidacion = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE78, "<<listadoEtiquetas>>", sbEtiquetas.toString());
					mapaEtiquetas.put("ERROR", "TRUE");
					mapaEtiquetas.put("SUBJECT", "Error de Validacion XML");
					mapaEtiquetas.put("MENSAJE", mensajeValidacion);
				}
				guardarEstadisticas(con, esquema, listaReporte, usuarioHTTP);
				
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
			mapaEtiquetas.put("ERROR", "TRUE");
			mapaEtiquetas.put("SUBJECT", "");
			mapaEtiquetas.put("MENSAJE", "Error al leer el archivo .XML en la informacion de carta porte, consultar a su administrador.");
		}
		return mapaEtiquetas;
	}
	
	*/
	
	public  HashMap<String, String> validarEtiquetasCartaPorte20(Connection con, String esquema, long folioOrden, int claveProveedor, Comprobante _comprobante, String versionXML, String idLenguaje, String usuarioHTTP){
		HashMap<String, String> mapaEtiquetas = new HashMap<>();
				
		EtiquetasCPBean etiquetasCPBean = new EtiquetasCPBean();
		SimpleDateFormat formaDate = new SimpleDateFormat("yyyy-MM-dd");
		String fechaActual = "";
		String datoXML = "";
		ArrayList<String> datosValidar = null;
		String cadSinCaracteres = "";
		boolean bandEncontro = false;
		ArrayList<Ubicacion> ubicacion = null;
		ArrayList<ReporteForm> listaReporte = new ArrayList<>();
		ReporteForm reporteForm = new ReporteForm();
		StringBuffer sbEtiquetas = new StringBuffer();

		try {
			fechaActual = formaDate.format(new Date());
			mapaEtiquetas.put("ERROR", "false");
			mapaEtiquetas.put("SUBJECT", "");
			mapaEtiquetas.put("MENSAJE", "");

			ProveedoresBean provBean = new ProveedoresBean();
			ProveedoresForm provForm = provBean.consultarProveedor(con, esquema, claveProveedor);

			if ("S".equalsIgnoreCase(provForm.getCartaPorte())) {
				if (_comprobante != null) {
					ArrayList<EtiquetasCPForm> listadoEtiquetas = etiquetasCPBean.listadoActivos20(con, esquema, versionXML);
					EtiquetasCPForm cartaPorteForm = null;
					ubicacion = _comprobante.getComplemento().CartaPorte20.getUbicaciones().getUbicacion();
					ArrayList<Mercancia> mercancias = _comprobante.getComplemento().CartaPorte20.getMercancias().getMercancia();
					
					for (int x = 0; x < listadoEtiquetas.size(); x++) {
						cartaPorteForm = listadoEtiquetas.get(x);
						bandEncontro = false;
						boolean pasoFecha = UtilsFechas.compararDosFechas(cartaPorteForm.getFechaIni(), cartaPorteForm.getFechaFin(), fechaActual);
						if (pasoFecha) {
							
							if("Estado_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getEstado();
							} else if("RFCRemitente".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getRFCRemitenteDestinatario();
							} else if("Calle_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getCalle();
							} else if("Pais_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getPais();
							} else if("CodigoPostal_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getCodigoPostal();
							} else if("Estado_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getEstado();
							} else if("RFCDestinatario".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getRFCRemitenteDestinatario();
							} else if("Calle_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getCalle();
							} else if("Pais_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getPais();
							} else if("CodigoPostal_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getCodigoPostal();
							} else if("PesoBrutoTotal".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = String.valueOf(_comprobante.getComplemento().CartaPorte20.getMercancias().getPesoBrutoTotal());
							} else if("UnidadPeso".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = _comprobante.getComplemento().CartaPorte20.getMercancias().getUnidadPeso();
							} else if("NumTotalMercancias".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = String.valueOf(_comprobante.getComplemento().CartaPorte20.getMercancias().getNumTotalMercancias());
							}

							for (int i = 0; i < mercancias.size(); i++) {
								if("BienesTransp".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getBienesTransp();
								} else if("Descripcion".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getDescripcion();
								} else if("Cantidad".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = String.valueOf(mercancias.get(i).getCantidad());
								} else if("ClaveUnidad".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getClaveUnidad();
								} else if("PesoEnKg".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = String.valueOf(mercancias.get(i).getPesoEnKg());
								} else if("MaterialPeligroso".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getMaterialPeligroso();
								} else if("Embalaje".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getEmbalaje();
								}
								else {
									break;
								}

								if(datoXML != null || "".equalsIgnoreCase(datoXML)) {
									break;
								}
							}
							
							reporteForm.setFolioOrden(folioOrden);
							reporteForm.setClaveProveedor(claveProveedor);
							reporteForm.setEtiquetaCartaPorte(cartaPorteForm.getIdEtiqueta());
							reporteForm.setValorXML(datoXML);
							
							
							if ("S".equalsIgnoreCase(cartaPorteForm.getValidarVacio())) {
								if (!"".equalsIgnoreCase(Utils.noNuloNormal(datoXML))){
									bandEncontro = true;
									reporteForm.setEstatus("000 - INFORMACION CORRECTA");
									reporteForm.setResultado("S");
								}else {
									reporteForm.setEstatus("001 - SIN INFORMACION");
									reporteForm.setResultado("N");
									sbEtiquetas.append(cartaPorteForm.getIdEtiqueta()).append(", ");
								}
							} else {
								datosValidar = datosValidar(con, esquema, cartaPorteForm.getIdEtiqueta());
								for (int y = 0; y < datosValidar.size(); y++){
									cadSinCaracteres = Utils.replaceCaracteresEspeciales(Utils.noNuloNormal(datoXML));
									if (cadSinCaracteres.equals(datosValidar.get(y)) ){
										bandEncontro = true;
										reporteForm.setEstatus("000 - INFORMACION CORRECTA");
										reporteForm.setResultado("S");
										break;
									}
								}
								if (!bandEncontro) {
									reporteForm.setEstatus("002 - INFORMACION NO ENCONTRADA");
									reporteForm.setResultado("N");
									sbEtiquetas.append(cartaPorteForm.getIdEtiqueta()).append(", ");
								}
							}
							
							listaReporte.add(reporteForm);
							reporteForm = new ReporteForm();
							/*
							if (!bandEncontro) {
								mapaEtiquetas.put("ERROR", "TRUE");
								mapaEtiquetas.put("SUBJECT", cartaPorteForm.getSubject());
								mapaEtiquetas.put("MENSAJE", cartaPorteForm.getMensaje());
								break;
							}
							*/
							
						}
					}
					
					if (sbEtiquetas.length() > 0) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeValidacion = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE78, "<<listadoEtiquetas>>", sbEtiquetas.toString());
						mapaEtiquetas.put("ERROR", "TRUE");
						mapaEtiquetas.put("SUBJECT", "Error de Validacion XML");
						mapaEtiquetas.put("MENSAJE", mensajeValidacion);
					}
					guardarEstadisticas(con, esquema, listaReporte, usuarioHTTP);
					
				}
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
			mapaEtiquetas.put("ERROR", "TRUE");
			mapaEtiquetas.put("SUBJECT", "");
			mapaEtiquetas.put("MENSAJE", "Error al leer el archivo .XML, consultar a su administrador.");
		}
		return mapaEtiquetas;
	}
	
	
	public  HashMap<String, String> validarEtiquetasCartaPorte30(Connection con, String esquema, long folioOrden, int claveProveedor, Comprobante _comprobante, String versionXML, String idLenguaje, String usuarioHTTP){
		HashMap<String, String> mapaEtiquetas = new HashMap<>();
				
		EtiquetasCPBean etiquetasCPBean = new EtiquetasCPBean();
		SimpleDateFormat formaDate = new SimpleDateFormat("yyyy-MM-dd");
		String fechaActual = "";
		String datoXML = "";
		ArrayList<String> datosValidar = null;
		String cadSinCaracteres = "";
		boolean bandEncontro = false;
		ArrayList<com.itextpdf.xmltopdf.CartaPorte30.Ubicacion> ubicacion = null;
		ArrayList<ReporteForm> listaReporte = new ArrayList<>();
		ReporteForm reporteForm = new ReporteForm();
		StringBuffer sbEtiquetas = new StringBuffer();

		try {
			fechaActual = formaDate.format(new Date());
			mapaEtiquetas.put("ERROR", "false");
			mapaEtiquetas.put("SUBJECT", "");
			mapaEtiquetas.put("MENSAJE", "");

			ProveedoresBean provBean = new ProveedoresBean();
			ProveedoresForm provForm = provBean.consultarProveedor(con, esquema, claveProveedor);

			if ("S".equalsIgnoreCase(provForm.getCartaPorte())) {
				if (_comprobante != null) {
					ArrayList<EtiquetasCPForm> listadoEtiquetas = etiquetasCPBean.listadoActivos20(con, esquema, versionXML);
					EtiquetasCPForm cartaPorteForm = null;
					//ubicacion = _comprobante.getComplemento().CartaPorte20.getUbicaciones().getUbicacion();
					ubicacion = _comprobante.getComplemento().getCartaPorte30().getUbicaciones().getUbicacion();
					//ArrayList<Mercancia> mercancias = _comprobante.getComplemento().CartaPorte20.getMercancias().getMercancia();
					ArrayList<com.itextpdf.xmltopdf.CartaPorte30.Mercancia> mercancias = _comprobante.getComplemento().getCartaPorte30().getMercancias().getMercancia();
					
					for (int x = 0; x < listadoEtiquetas.size(); x++) {
						cartaPorteForm = listadoEtiquetas.get(x);
						bandEncontro = false;
						boolean pasoFecha = UtilsFechas.compararDosFechas(cartaPorteForm.getFechaIni(), cartaPorteForm.getFechaFin(), fechaActual);
						if (pasoFecha) {
							
							if("Estado_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getEstado();
							} else if("RFCRemitente".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getRFCRemitenteDestinatario();
							} else if("Calle_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getCalle();
							} else if("Pais_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getPais();
							} else if("CodigoPostal_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getCodigoPostal();
							} else if("Estado_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getEstado();
							} else if("RFCDestinatario".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getRFCRemitenteDestinatario();
							} else if("Calle_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getCalle();
							} else if("Pais_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getPais();
							} else if("CodigoPostal_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getCodigoPostal();
							} else if("PesoBrutoTotal".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = String.valueOf(_comprobante.getComplemento().getCartaPorte30().getMercancias().getPesoBrutoTotal());
							} else if("UnidadPeso".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = _comprobante.getComplemento().getCartaPorte30().getMercancias().getUnidadPeso();
							} else if("NumTotalMercancias".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = String.valueOf(_comprobante.getComplemento().getCartaPorte30().getMercancias().getNumTotalMercancias());
							}

							for (int i = 0; i < mercancias.size(); i++) {
								if("BienesTransp".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getBienesTransp();
								} else if("Descripcion".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getDescripcion();
								} else if("Cantidad".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = String.valueOf(mercancias.get(i).getCantidad());
								} else if("ClaveUnidad".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getClaveUnidad();
								} else if("PesoEnKg".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = String.valueOf(mercancias.get(i).getPesoEnKg());
								} else if("MaterialPeligroso".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getMaterialPeligroso();
								} else if("Embalaje".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getEmbalaje();
								}
								else {
									break;
								}

								if(datoXML != null || "".equalsIgnoreCase(datoXML)) {
									break;
								}
							}
							
							reporteForm.setFolioOrden(folioOrden);
							reporteForm.setClaveProveedor(claveProveedor);
							reporteForm.setEtiquetaCartaPorte(cartaPorteForm.getIdEtiqueta());
							reporteForm.setValorXML(datoXML);
							
							
							if ("S".equalsIgnoreCase(cartaPorteForm.getValidarVacio())) {
								if (!"".equalsIgnoreCase(Utils.noNuloNormal(datoXML))){
									bandEncontro = true;
									reporteForm.setEstatus("000 - INFORMACION CORRECTA");
									reporteForm.setResultado("S");
								}else {
									reporteForm.setEstatus("001 - SIN INFORMACION");
									reporteForm.setResultado("N");
									sbEtiquetas.append(cartaPorteForm.getIdEtiqueta()).append(", ");
								}
							} else {
								datosValidar = datosValidar(con, esquema, cartaPorteForm.getIdEtiqueta());
								for (int y = 0; y < datosValidar.size(); y++){
									cadSinCaracteres = Utils.replaceCaracteresEspeciales(Utils.noNuloNormal(datoXML));
									if (cadSinCaracteres.equals(datosValidar.get(y)) ){
										bandEncontro = true;
										reporteForm.setEstatus("000 - INFORMACION CORRECTA");
										reporteForm.setResultado("S");
										break;
									}
								}
								if (!bandEncontro) {
									reporteForm.setEstatus("002 - INFORMACION NO ENCONTRADA");
									reporteForm.setResultado("N");
									sbEtiquetas.append(cartaPorteForm.getIdEtiqueta()).append(", ");
								}
							}
							
							listaReporte.add(reporteForm);
							reporteForm = new ReporteForm();
							/*
							if (!bandEncontro) {
								mapaEtiquetas.put("ERROR", "TRUE");
								mapaEtiquetas.put("SUBJECT", cartaPorteForm.getSubject());
								mapaEtiquetas.put("MENSAJE", cartaPorteForm.getMensaje());
								break;
							}
							*/
							
						}
					}
					
					if (sbEtiquetas.length() > 0) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeValidacion = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE78, "<<listadoEtiquetas>>", sbEtiquetas.toString());
						mapaEtiquetas.put("ERROR", "TRUE");
						mapaEtiquetas.put("SUBJECT", "Error de Validacion XML");
						mapaEtiquetas.put("MENSAJE", mensajeValidacion);
					}
					guardarEstadisticas(con, esquema, listaReporte, usuarioHTTP);
					
				}
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
			mapaEtiquetas.put("ERROR", "TRUE");
			mapaEtiquetas.put("SUBJECT", "");
			mapaEtiquetas.put("MENSAJE", "Error al leer el archivo .XML, consultar a su administrador.");
		}
		return mapaEtiquetas;
	}
	
	
	
	public  HashMap<String, String> validarEtiquetasCartaPorte31(Connection con, String esquema, long folioOrden, int claveProveedor, Comprobante _comprobante, String versionXML, String idLenguaje, String usuarioHTTP){
		HashMap<String, String> mapaEtiquetas = new HashMap<>();
				
		EtiquetasCPBean etiquetasCPBean = new EtiquetasCPBean();
		SimpleDateFormat formaDate = new SimpleDateFormat("yyyy-MM-dd");
		String fechaActual = "";
		String datoXML = "";
		ArrayList<String> datosValidar = null;
		String cadSinCaracteres = "";
		boolean bandEncontro = false;
		ArrayList<com.itextpdf.xmltopdf.CartaPorte31.Ubicacion> ubicacion = null;
		ArrayList<ReporteForm> listaReporte = new ArrayList<>();
		ReporteForm reporteForm = new ReporteForm();
		StringBuffer sbEtiquetas = new StringBuffer();

		try {
			fechaActual = formaDate.format(new Date());
			mapaEtiquetas.put("ERROR", "false");
			mapaEtiquetas.put("SUBJECT", "");
			mapaEtiquetas.put("MENSAJE", "");

			ProveedoresBean provBean = new ProveedoresBean();
			ProveedoresForm provForm = provBean.consultarProveedor(con, esquema, claveProveedor);

			if ("S".equalsIgnoreCase(provForm.getCartaPorte())) {
				if (_comprobante != null) {
					ArrayList<EtiquetasCPForm> listadoEtiquetas = etiquetasCPBean.listadoActivos20(con, esquema, versionXML);
					EtiquetasCPForm cartaPorteForm = null;
					//ubicacion = _comprobante.getComplemento().CartaPorte20.getUbicaciones().getUbicacion();
					ubicacion = _comprobante.getComplemento().getCartaPorte31().getUbicaciones().getUbicacion();
					//ArrayList<Mercancia> mercancias = _comprobante.getComplemento().CartaPorte20.getMercancias().getMercancia();
					ArrayList<com.itextpdf.xmltopdf.CartaPorte31.Mercancia> mercancias = _comprobante.getComplemento().getCartaPorte31().getMercancias().getMercancia();
					
					for (int x = 0; x < listadoEtiquetas.size(); x++) {
						cartaPorteForm = listadoEtiquetas.get(x);
						bandEncontro = false;
						boolean pasoFecha = UtilsFechas.compararDosFechas(cartaPorteForm.getFechaIni(), cartaPorteForm.getFechaFin(), fechaActual);
						if (pasoFecha) {
							
							if("Estado_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getEstado();
							} else if("RFCRemitente".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getRFCRemitenteDestinatario();
							} else if("Calle_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getCalle();
							} else if("Pais_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getPais();
							} else if("CodigoPostal_Origen".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(0).getDomicilio().getCodigoPostal();
							} else if("Estado_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getEstado();
							} else if("RFCDestinatario".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getRFCRemitenteDestinatario();
							} else if("Calle_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getCalle();
							} else if("Pais_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getPais();
							} else if("CodigoPostal_Destino".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = ubicacion.get(1).getDomicilio().getCodigoPostal();
							} else if("PesoBrutoTotal".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = String.valueOf(_comprobante.getComplemento().getCartaPorte31().getMercancias().getPesoBrutoTotal());
							} else if("UnidadPeso".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = _comprobante.getComplemento().getCartaPorte31().getMercancias().getUnidadPeso();
							} else if("NumTotalMercancias".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
								datoXML = String.valueOf(_comprobante.getComplemento().getCartaPorte31().getMercancias().getNumTotalMercancias());
							}

							for (int i = 0; i < mercancias.size(); i++) {
								if("BienesTransp".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getBienesTransp();
								} else if("Descripcion".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getDescripcion();
								} else if("Cantidad".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = String.valueOf(mercancias.get(i).getCantidad());
								} else if("ClaveUnidad".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getClaveUnidad();
								} else if("PesoEnKg".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = String.valueOf(mercancias.get(i).getPesoEnKg());
								} else if("MaterialPeligroso".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getMaterialPeligroso();
								} else if("Embalaje".equalsIgnoreCase(cartaPorteForm.getIdEtiqueta())) {
									datoXML = mercancias.get(i).getEmbalaje();
								}
								else {
									break;
								}

								if(datoXML != null || "".equalsIgnoreCase(datoXML)) {
									break;
								}
							}
							
							reporteForm.setFolioOrden(folioOrden);
							reporteForm.setClaveProveedor(claveProveedor);
							reporteForm.setEtiquetaCartaPorte(cartaPorteForm.getIdEtiqueta());
							reporteForm.setValorXML(datoXML);
							
							
							if ("S".equalsIgnoreCase(cartaPorteForm.getValidarVacio())) {
								if (!"".equalsIgnoreCase(Utils.noNuloNormal(datoXML))){
									bandEncontro = true;
									reporteForm.setEstatus("000 - INFORMACION CORRECTA");
									reporteForm.setResultado("S");
								}else {
									reporteForm.setEstatus("001 - SIN INFORMACION");
									reporteForm.setResultado("N");
									sbEtiquetas.append(cartaPorteForm.getIdEtiqueta()).append(", ");
								}
							} else {
								datosValidar = datosValidar(con, esquema, cartaPorteForm.getIdEtiqueta());
								for (int y = 0; y < datosValidar.size(); y++){
									cadSinCaracteres = Utils.replaceCaracteresEspeciales(Utils.noNuloNormal(datoXML));
									if (cadSinCaracteres.equals(datosValidar.get(y)) ){
										bandEncontro = true;
										reporteForm.setEstatus("000 - INFORMACION CORRECTA");
										reporteForm.setResultado("S");
										break;
									}
								}
								if (!bandEncontro) {
									reporteForm.setEstatus("002 - INFORMACION NO ENCONTRADA");
									reporteForm.setResultado("N");
									sbEtiquetas.append(cartaPorteForm.getIdEtiqueta()).append(", ");
								}
							}
							
							listaReporte.add(reporteForm);
							reporteForm = new ReporteForm();
							/*
							if (!bandEncontro) {
								mapaEtiquetas.put("ERROR", "TRUE");
								mapaEtiquetas.put("SUBJECT", cartaPorteForm.getSubject());
								mapaEtiquetas.put("MENSAJE", cartaPorteForm.getMensaje());
								break;
							}
							*/
							
						}
					}
					
					if (sbEtiquetas.length() > 0) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeValidacion = Utils.getMensajeValidacion(mensajeSIAREX.MENSAJE78, "<<listadoEtiquetas>>", sbEtiquetas.toString());
						mapaEtiquetas.put("ERROR", "TRUE");
						mapaEtiquetas.put("SUBJECT", "Error de Validacion XML");
						mapaEtiquetas.put("MENSAJE", mensajeValidacion);
					}
					guardarEstadisticas(con, esquema, listaReporte, usuarioHTTP);
					
				}
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
			mapaEtiquetas.put("ERROR", "TRUE");
			mapaEtiquetas.put("SUBJECT", "");
			mapaEtiquetas.put("MENSAJE", "Error al leer el archivo .XML, consultar a su administrador.");
		}
		return mapaEtiquetas;
	}
	
	public int banderaRFCEmisor(Connection con, String esquema, String rfcEmisor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int claveReceptor = 0;

		try {		
			stmt = con.prepareStatement(ValidacionesCPQuerys.getRfcEmisorReceptor(esquema));
			stmt.setString(1, rfcEmisor);
			rs  = stmt.executeQuery();

			if  (rs.next()) {
				claveReceptor = Utils.noNuloINT(rs.getString(1));//CLAVE_RECEPTOR
			}
		}
		catch(Exception e) {
			Utils.imprimeLog("banderaRFCEmisor(): ", e);
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return claveReceptor;
	}
	
	
	public ArrayList<String> datosValidar (Connection con, String esquema, String idEtiqueta){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> listaDetalle = new ArrayList<>();
		try {		
			stmt = con.prepareStatement(ValidacionesCPQuerys.getListadoValores(esquema));
			stmt.setString(1, idEtiqueta);
			rs  = stmt.executeQuery();
			while  (rs.next()) {
				listaDetalle.add(Utils.noNuloNormal(rs.getString(1)));
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

	
	
	public int guardarEstadisticas (Connection con, String esquema, ArrayList<ReporteForm> listaReporte, String usuarioHTTP){
		PreparedStatement stmt = null;
		ReporteForm reporteForm = null;
		int totRes = 0;
		try {		
			stmt = con.prepareStatement(ValidacionesCPQuerys.getInsertarReporte(esquema));
			for (int x = 0; x < listaReporte.size(); x++) {
				reporteForm = listaReporte.get(x);
				stmt.setLong(1, reporteForm.getFolioOrden());
				stmt.setInt(2, reporteForm.getClaveProveedor());
				stmt.setString(3, reporteForm.getEtiquetaCartaPorte());
				stmt.setString(4, reporteForm.getValorXML());
				stmt.setString(5, reporteForm.getEstatus());
				stmt.setString(6, reporteForm.getResultado());
				stmt.setString(7, usuarioHTTP);
				totRes+=stmt.executeUpdate();
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return totRes;
	}

	
	
	public static boolean validarUsoClave(Connection con, String esquema, String rfc, String usoCFDI, JSONArray jsonConceptos, long folioEmpresa)
    {
        PreparedStatement stmt = null;
        PreparedStatement stmtInsert = null;
        PreparedStatement stmtDelete = null;
        ResultSet rs = null;
        boolean existe = true; 
        JSONObject jsonobj = null;
        try{
       	
        	stmtDelete = con.prepareStatement(ValidacionesCPQuerys.getQueryDeleteClaveProductoXML(esquema));
        	stmtDelete.setLong(1, folioEmpresa);
        	stmtDelete.executeUpdate();
        	
        	stmtInsert = con.prepareStatement(ValidacionesCPQuerys.getQueryInsertClaveProductoXML(esquema));
        	
        	for (int x = 0; x < jsonConceptos.size(); x++) {
        		jsonobj = (JSONObject) jsonConceptos.get(x);
        		try {
        			stmtInsert.setLong(1, folioEmpresa);
    				stmtInsert.setString(2, usoCFDI);
    				stmtInsert.setString(3, jsonobj.get("bienesTransp").toString());
    				stmtInsert.setString(4, jsonobj.get("descripcion").toString());
    				stmtInsert.setString(5, jsonobj.get("unidad").toString());
    				stmtInsert.setString(6, jsonobj.get("claveUnidad").toString());
    				stmtInsert.setString(7, jsonobj.get("cantidad").toString());
    				stmtInsert.setString(8, jsonobj.get("fraccionArancelaria").toString());
    				stmtInsert.setString(9, jsonobj.get("pesoKG").toString());
    				stmtInsert.executeUpdate();
        		}catch(Exception e) {
        			Utils.imprimeLog("", e);
        		}
        	}
        	
// select CLAVE_PROD_SERV from CLAVE_PRODUC_SERVICIO_XML where FOLIO_EMPRESA = ? and CLAVE_PROD_SERV not in (select CLAVE_PRODUCTO from USO_CFDI where RFC = ? and USO_CFDI = ?)        	
        	StringBuffer sbQuery = new StringBuffer(ValidacionesCPQuerys.getQueryValidaCLAVE(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setLong(1, folioEmpresa);
            stmt.setString(2, rfc);
            stmt.setString(3, usoCFDI);
            
            
            rs = stmt.executeQuery();
            
			if(rs.next()){
				existe = false;
            }
        }catch(Exception e){
           Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            
	            if(stmtDelete != null)
	            	stmtDelete.close();
	            stmtDelete = null;
	            
	            if(stmtInsert != null)
	            	stmtInsert.close();
	            stmtInsert = null; 
	            
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return existe;
    }


	public int grabarCartaPorte(Connection con, String esquema, long FOLIO_ORDEN, long FOLIO_EMPRESA, String RFC_EMISOR, String RFC_RECEPTOR,
            String UUID, double TOTAL_PAGADO, String ESTATUS_CARTA_PORTE, String FECHA_PAGO, String FECHA_TIMBRADO,
            String NOMBRE_XML, String NOMBRE_PDF, String TIPO_COMPROBANTE, String FORMA_PAGO, String ESTATUS_CONCILIACION,
            String ESTATUS, String ESTATUS_ORDEN, String USUARIO_TRASACCION) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try{
			stmt = con.prepareStatement(ValidacionesCPQuerys.getGrabarCartaPorte(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, FOLIO_ORDEN);
			stmt.setLong(2, FOLIO_EMPRESA);
			stmt.setString(3, RFC_EMISOR);
			stmt.setString(4, RFC_RECEPTOR);
			stmt.setString(5, UUID);
			stmt.setDouble(6, TOTAL_PAGADO);
			stmt.setString(7, ESTATUS_CARTA_PORTE);
			stmt.setString(8, FECHA_PAGO);
			stmt.setString(9, FECHA_TIMBRADO);
			stmt.setString(10, NOMBRE_XML);
			stmt.setString(11, NOMBRE_PDF);
			stmt.setString(12, TIPO_COMPROBANTE);
			stmt.setString(13, FORMA_PAGO);
			stmt.setString(14, ESTATUS_CONCILIACION);
			stmt.setString(15, ESTATUS);
			stmt.setString(16, ESTATUS_ORDEN);
			stmt.setString(17, USUARIO_TRASACCION);

			int cant = stmt.executeUpdate();
			if(cant > 0){
				rs = stmt.getGeneratedKeys();

				if(rs.next()) {
					resultado = rs.getInt(1);
				}
			}
		}
		catch(SQLException sql) {
			if (sql.getErrorCode() == 1062 || sql.getErrorCode() == -1062) {
				resultado = 1062;
			}
			Utils.imprimeLog("", sql);
		}
		catch(Exception e){
			Utils.imprimeLog("grabarCartaPorte(): ", e);
		}
		finally{
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
				stmt = null;
			}
		}
		return resultado;
	}



	public boolean tieneCartaPorte(Connection con, String esquema, long ordenCompra){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean bandTiene = false;

		try {		
			stmt = con.prepareStatement(ValidacionesCPQuerys.getTieneCartaPorte(esquema));
			stmt.setLong(1, ordenCompra);
			rs  = stmt.executeQuery();
			if  (rs.next()) {
				bandTiene = true;
			}
		}
		catch(Exception e) {
			Utils.imprimeLog("tieneCartaPorte(): ", e);
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return bandTiene;
	}
	

	
	public boolean rfcAsignado(Connection con, String esquema, int claveReceptor, String rfcReceptor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String rfcEmisor = "";
		boolean asignado = false;

		try {		
			stmt = con.prepareStatement(ValidacionesCPQuerys.getRfcProveedor(esquema));
			stmt.setInt(1, claveReceptor);
			rs  = stmt.executeQuery();

			if  (rs.next()) {
				rfcEmisor = Utils.noNuloNormal(rs.getString(1));
				
				if(rfcEmisor.equalsIgnoreCase(rfcReceptor)) {
					asignado = true;
				}
			}
		}
		catch(Exception e) {
			Utils.imprimeLog("rfcAsignado(): ", e);
		}
		finally {
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
		return asignado;
	}

	
	
	public boolean obtenerEstatusOrden(Connection con, String esquema, long ordenCompra){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean cumpleEstatus = false;
		String statusPago = "", nombreXML = "";

		try{
			stmt = con.prepareStatement(ValidacionesCPQuerys.getQueryInfoCuentasPagarRecibo(esquema));
			stmt.setLong(1, ordenCompra);
			rs = stmt.executeQuery();

			if (rs.next()){
				nombreXML  = Utils.noNuloNormal(rs.getString(1));
				statusPago = Utils.noNuloNormal(rs.getString(2));
				
				if(!"".equalsIgnoreCase(nombreXML)) {
					cumpleEstatus = true;
				//} else  if("A3".equalsIgnoreCase(statusPago) || "A4".equalsIgnoreCase(statusPago)) {
				} else  if("A3".equalsIgnoreCase(statusPago) || "A1".equalsIgnoreCase(statusPago)) {
					cumpleEstatus = true;
				}
			}
		}
		catch(Exception e){
			Utils.imprimeLog("obtenerEstatusOrden(): ", e);
		}
		finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e){
				stmt = null;
			}
		}
		return cumpleEstatus;
	}

	
	public CartasPorteForm datosOrden(Connection con, String esquema, long ordenCompra){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		CartasPorteForm cartaPorteForm = new CartasPorteForm();

		try {		
			stmt = con.prepareStatement(ValidacionesCPQuerys.getQueryValidaEstatus(esquema));
			stmt.setLong(1, ordenCompra);
			rs  = stmt.executeQuery();
			if  (rs.next()) {
				cartaPorteForm.setEstatusCarta(Utils.noNuloNormal(rs.getString(1)));
				cartaPorteForm.setFolioOrden(Utils.noNuloINT(rs.getString(3)));
				cartaPorteForm.setTotal(Utils.noNuloDouble(rs.getString(5)));
				cartaPorteForm.setFechaPago(Utils.noNuloNormal(rs.getString(11)));
			}
		}
		catch(Exception e) {
			Utils.imprimeLog("datosOrden(): ", e);
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}
			catch(Exception e) {
				rs = null;
				stmt = null;
			}
		}
		return cartaPorteForm;
	}

	
	
	public int actualizarEstatusOrden(Connection con, String esquema,  long FOLIO_EMPRESA, String ESTATUS_ORDEN, String usuarioHTTP) {
		PreparedStatement stmt = null;
        int resultado = 0;
        try{
        	stmt = con.prepareStatement(ValidacionesCPQuerys.getActualizarOrdenCompra(esquema));
            stmt.setString(1, ESTATUS_ORDEN);
            stmt.setString(2, usuarioHTTP);
            stmt.setLong(3, FOLIO_EMPRESA);
            
            resultado = stmt.executeUpdate();
        }
        catch(SQLException sql) {
        	Utils.imprimeLog("", sql);
        }
        catch(Exception e){
        	Utils.imprimeLog("grabarCartaPorte(): ", e);
        }
        finally{
	        try{
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return resultado;
    }
	
	
	
	public CartasPorteForm getDescripciones(Connection con, String esquema, long folioEmpresa){
		PreparedStatement stmt = null;
        ResultSet rs = null;
        CartasPorteForm cartasPorteForm = new CartasPorteForm();

        try{
        	stmt = con.prepareStatement(ValidacionesCPQuerys.getQueryBuscarClavePorOrdenXML(esquema));
        	stmt.setLong(1, folioEmpresa);
            rs = stmt.executeQuery();
            String usoCFDI = null;

            if(rs.next()){
            	usoCFDI = Utils.noNulo(rs.getString(2));
            	cartasPorteForm.setUsoCFDI(usoCFDI);
            	cartasPorteForm.setDesUsoCFDI(getDescripcionUSOCFDI(con, esquema, usoCFDI));
            	
            }
            
            try {
            	rs.close();
            	stmt.close();
            }
            catch(Exception e) {
            	Utils.imprimeLog("", e);
            }
            
            try {
            	rs.close();
            	stmt.close();
            }
            catch(Exception e) {
            	Utils.imprimeLog("", e);
            }
            
            stmt = con.prepareStatement(ValidacionesCPQuerys.getQueryBuscarProveedor(esquema));
        	stmt.setLong(1, folioEmpresa);
            rs = stmt.executeQuery();

            if(rs.next()){
            	
            	cartasPorteForm.setRfc(Utils.noNulo(rs.getString(1)));
            	cartasPorteForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(2))));
            	cartasPorteForm.setNombreXML(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(3))));
            	cartasPorteForm.setNombrePDF(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(4))));
            	cartasPorteForm.setClaveProveedor(rs.getInt(5));
            	cartasPorteForm.setServicioRecibo(Utils.noNulo(rs.getString(6)));
            }
            cartasPorteForm.setFolioEmpresa(folioEmpresa);
            try {
            	rs.close();
            	stmt.close();
            }
            catch(Exception e) {
            	Utils.imprimeLog("", e);
            }
            
            stmt = con.prepareStatement(ValidacionesCPQuerys.getQueryUsoCFDIXML(esquema));
            
            stmt.setString(1, cartasPorteForm.getRfc());
        	stmt.setString(2, cartasPorteForm.getUsoCFDI());
            rs = stmt.executeQuery();
            
            if(rs.next()){
            	cartasPorteForm.setBandActivo("A");
            }
            else {
            	cartasPorteForm.setBandActivo("D");
            }
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
        }
        finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return cartasPorteForm;
    }
	
	
	public String getDescripcionUSOCFDI(Connection con, String esquema, String idUsoCFDI){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String desUsoCFDI = null;

        try{
        	stmt = con.prepareStatement(ValidacionesCPQuerys.getQueryDesUsoCFDICP(esquema));
        	stmt.setString(1, idUsoCFDI);
            rs = stmt.executeQuery();

            if(rs.next()){
            	desUsoCFDI = Utils.noNulo(rs.getString(1));
            }
            else {
            	desUsoCFDI = "SIN DESCRIPCION";
            }
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
        }
        finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return desUsoCFDI;
    }
	
	
	public  ArrayList<ConfClaveUsoCFDIForm> buscarClavePorOrden(Connection con, String esquema, long folioEmpresa, String rfc, String usoCFDI){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        ArrayList<ConfClaveUsoCFDIForm> listaDetalle = new ArrayList<>();
        ConfClaveUsoCFDIForm confUsoForm = new ConfClaveUsoCFDIForm();

        try{
        	stmt = con.prepareStatement(ValidacionesCPQuerys.getQueryBuscarClavePorOrden(esquema));
        	stmt.setLong(1, folioEmpresa);
        	stmt.setString(2, rfc);
        	stmt.setString(3, usoCFDI);
            rs = stmt.executeQuery();

            while(rs.next()){
            	confUsoForm.setIdRegistro(rs.getInt(1));
            	confUsoForm.setClaveProduto(Utils.noNulo(rs.getString(2)));
            	confUsoForm.setDescripcionXML(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(3))));
            	confUsoForm.setDescripcionSAT(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(4))));
				listaDetalle.add(confUsoForm);
				confUsoForm = new ConfClaveUsoCFDIForm();
            }
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
        }
        finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return listaDetalle;
    }
	
	
	public ArrayList<ConfClaveUsoCFDIForm> buscarClavePorOrdenSinUso(Connection con, String esquema, long folioEmpresa, String rfc, String usoCFDI){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ConfClaveUsoCFDIForm confUsoForm = new ConfClaveUsoCFDIForm();
        ArrayList<ConfClaveUsoCFDIForm> listaDetalle = new ArrayList<>();

        try{
        	stmt = con.prepareStatement(ValidacionesCPQuerys.getQueryBuscarClavePorOrdenSinUSO(esquema));
        	stmt.setLong(1, folioEmpresa);
            rs = stmt.executeQuery();

            while(rs.next()){
            	confUsoForm.setIdRegistro(rs.getInt(1));
            	confUsoForm.setClaveProduto(Utils.noNulo(rs.getString(2)));
            	confUsoForm.setDescripcionXML(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(3))));
            	confUsoForm.setDescripcionSAT(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(4))));
				listaDetalle.add(confUsoForm);
				confUsoForm = new ConfClaveUsoCFDIForm();
				
            }
			
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
        }
        finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return listaDetalle;
    }
	

	public CartasPorteForm consultaCartas(Connection con, String esquema, double folioEmpresa) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CartasPorteForm cartaForm = new CartasPorteForm();
        
        try {
            stmt = con.prepareStatement(ValidacionesCPQuerys.getConsultaCarta(esquema));
            stmt.setDouble(1, folioEmpresa);
            rs = stmt.executeQuery();
            
			if(rs.next()) {
				cartaForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(2))));
				cartaForm.setUuidCarta(Utils.noNuloNormal(rs.getString(3)));
				cartaForm.setEstatusCarta(Utils.noNulo(rs.getString(4)));
				cartaForm.setNombreXML(Utils.noNuloNormal(rs.getString(5)));
				cartaForm.setNombrePDF(Utils.noNuloNormal(rs.getString(6)));
				cartaForm.setClaveProveedor(rs.getInt(7));
				cartaForm.setTipoComprobante(Utils.noNulo(rs.getString(8)));
				cartaForm.setEstatusOrden(Utils.noNulo(rs.getString(9)));
            }
        }
        catch(Exception e){
        	Utils.imprimeLog("consultaCartas(): ", e);
        }
        finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return cartaForm;
    }
	
	
	public int guardarClavesProducto(Connection con, String esquema, long folioEmpresa, String rfc,  String usoCFDI, String usuarioHTTP){
        PreparedStatement stmt = null;
        PreparedStatement stmtInsert = null;
        ResultSet rs = null;
        int totGuardados = 0;

        try{
        	stmt = con.prepareStatement(ValidacionesCPQuerys.getQueryValidaCLAVE(esquema));
            
        	stmt.setLong(1, folioEmpresa);
        	stmt.setString(2, rfc);
        	stmt.setString(3, usoCFDI);
        	
            rs = stmt.executeQuery();
            
            stmtInsert = con.prepareStatement(ValidacionesCPQuerys.getQueryInsertUso(esquema));
            stmtInsert.setString(1, rfc);
        	stmtInsert.setString(2, usoCFDI);
        	stmtInsert.setString(4, usuarioHTTP);
        	        	
            String claveProducto = null;

            while(rs.next()){
            	try {
            		claveProducto = Utils.noNulo(rs.getString(1));
                	stmtInsert.setString(3, claveProducto);
                	stmtInsert.executeUpdate();
                	totGuardados++;
            	}
            	catch(Exception e) {
            		Utils.imprimeLog("", e);
            	}
            }
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
        }
        finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(stmtInsert != null)
	            	stmtInsert.close();
	            stmtInsert = null;
	        }
	        catch(Exception e){
	            stmt = null;
	            stmtInsert = null;
	        }
        }
        return totGuardados;
    }

	
	
	public int eliminarCartaPorteFolioEmpresa(Connection con, String esquema, long folioEmpresa){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int totGrabar = 0;

        try{
            stmt = con.prepareStatement(ValidacionesCPQuerys.getDeleteCartaPorteOrdenEmpresa(esquema));
            stmt.setLong(1, folioEmpresa);
            logger.info("stmtElima===>"+stmt);
            totGrabar = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("eliminarCartaPorte(): ", e);
        }
        finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
        return totGrabar;
    }
}
