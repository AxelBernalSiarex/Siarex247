package com.siarex247.catalogos.ConstanciaSituacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.siarex247.pdf.PDFLayoutTextStripper;
import com.siarex247.utils.Utils;


public class ValidacionesSAT {
	
	public static final Logger logger = Logger.getLogger("siarex247");

	// private final String VACIO = ""; 
	private final String NO_ENCONTRADO = "NO ENCONTRADO";
//	private final String NO_ENCONTRADO = "";
	
	public static void main(String[] args) {
		try {
			ValidacionesSAT c = new ValidacionesSAT();
			 // c.obtenerCedulaFiscal("C:\\TRABAJO\\PROYECTOS_SIAREX\\038 - GENERACION DE EMPRESA ASP ELITE INTERNACIONAL\\PENDIENTES\\Constancias\\ConstanciaJose.pdf");
			ConstanciaSituacionForm constForm = new ConstanciaSituacionForm();
			constForm.setRfc("GOGM8011246B1");
			constForm.setCedulaFiscal("17090392655");
			c.obtenerDatosSAT(constForm);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String [] obtenerCedulaFiscal(String rutaPDF){
		PDDocument pd = null;
		StringBuffer sbContenido = new StringBuffer();
		String datosFiscales [] = null;
		try {
//			  PDPage page = null;
	          // ArrayList<PDPage> listaPaginas = new ArrayList<PDPage>();
	          
	          File filePDF = new File(rutaPDF);
              pd = Loader.loadPDF(filePDF);
              Splitter splitter = new Splitter();
              List<PDDocument> splittedDocuments = splitter.split(pd);
//              PDDocument hojaDocument = null;
              PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
              
              for (int x = 0; x < splittedDocuments.size(); x++) {
  //          	  page = pd.getPage(x);
            	  sbContenido.append(pdfTextStripper.getText(splittedDocuments.get(x)));

              }
              datosFiscales  = buscarDatosFiscales(sbContenido.toString());			
              pd.close();//CERRAMOS OBJETO ACROBAT
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (pd != null) {
					pd.close();
				}
			}catch(Exception e) {
				pd = null;
			}
		}
		return datosFiscales;
	}
	
	private String [] buscarDatosFiscales(String contenidoPDF){
		String cedulaFiscal = "";
		String RFCEmpleado = "";
		String datosFiscales [] = {"", ""};
		try {
			// System.err.println("Contenido==> "+ contenidoPDF);
			
			 int pos =  contenidoPDF.indexOf("RFC:                                              "); 
			 // System.err.println("pos ===>"+pos );
			 if ( pos > -1) {
				 int cadIni = pos + 45;
				 int cadFinal = cadIni + 20;
				 RFCEmpleado = Utils.noNuloNormal(contenidoPDF.substring(cadIni, cadFinal ));
			 }
			 
			
			 if ((contenidoPDF.indexOf("idCIF:") > -1 && contenidoPDF.indexOf(RFCEmpleado)  > -1 )) {
				 int cadIni = contenidoPDF.indexOf("idCIF") + 8;
				 int cadFinal = cadIni + 11;
				 cedulaFiscal = Utils.noNuloNormal(contenidoPDF.substring(cadIni, cadFinal ));
			 }
			 datosFiscales[0] = RFCEmpleado;
			 datosFiscales[1] = cedulaFiscal;
			 
			 /*
			 for (int line=0; line < arrContenido.length; line++) {
				 linePDF = arrContenido[line];
				 if ((linePDF.indexOf("idCIF:") > -1) ) {
					 int cadIni = linePDF.indexOf("idCIF") + 5;
					 int cadFinal = cadIni + 10;
					 cedulaFiscal = Utils.noNuloNormal(linePDF.substring(cadIni, cadFinal ));
					 break;
				 }
			 }
			 */
			 
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return datosFiscales;
	}
	
	
	
	
	
	
	public ConstanciaSituacionForm obtenerDatosSAT(ConstanciaSituacionForm constForm) {
		try {
			
			if (Utils.noNulo(constForm.getCedulaFiscal()).equals("") || Utils.noNulo(constForm.getRfc()).equals("")) {
				constForm.setCurp(NO_ENCONTRADO);
				constForm.setNombreEmpleado(NO_ENCONTRADO);
				constForm.setApellidoPaterno(NO_ENCONTRADO);
				constForm.setApellidoMaterno(NO_ENCONTRADO);
				constForm.setFechaNacimiento(NO_ENCONTRADO);
				constForm.setSituacionContribuyente(NO_ENCONTRADO);
				constForm.setCodigoPostal(0);
				constForm.setFechaOperaciones(NO_ENCONTRADO);
				constForm.setCorreoElectronico(NO_ENCONTRADO);
				constForm.setRfc(NO_ENCONTRADO);
				constForm.setRegimenCapital(NO_ENCONTRADO);
				constForm.setFechaUltCambioSituacion(NO_ENCONTRADO);
				constForm.setEntidadFederativa(NO_ENCONTRADO);
				constForm.setMunicipio(NO_ENCONTRADO);
				constForm.setColonia(NO_ENCONTRADO);
				constForm.setTipoVialidad(NO_ENCONTRADO);
				constForm.setNombreVialidad(NO_ENCONTRADO);
				constForm.setNumeroExt(NO_ENCONTRADO);
				constForm.setNumeroInt(NO_ENCONTRADO);
				constForm.setFechaAlta(NO_ENCONTRADO);
				constForm.setRegimen(NO_ENCONTRADO);
				constForm.setClaveRegimen(NO_ENCONTRADO);
				
			}else {
				String contenidoSAT = ejecutarLinkSAT(constForm.getCedulaFiscal(), constForm.getRfc());
				// logger.info("contenidoSAT===>"+contenidoSAT);
				//System.err.println("contenido===>"+contenidoSAT);
				
				if (contenidoSAT != null) {
					int posIni = 0;
					int posFin = 0;
					
					// constForm.setRfc(constForm.getRfc());
					
					try {
						try {
							posIni = contenidoSAT.indexOf("CURP:") + 62;
							posFin = posIni + 30;
							String curpTemp = contenidoSAT.substring(posIni, posFin);
							String curp = "";
							if (curpTemp.indexOf("</td>") > -1) {
							  int posValor = curpTemp.indexOf("</td>");
							  curp = curpTemp.substring(0, posValor);
							 // System.err.println("apellidoPaterno====>"+apellidoMaterno);  
							}
							constForm.setCurp(curp);
						} catch (Exception e) {
							constForm.setCurp(NO_ENCONTRADO);
						}
						
					} catch (Exception e) {
						constForm.setCurp(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("Denominac") + 85;
						posFin = posIni + 200;
						String razonSocialTemp = contenidoSAT.substring(posIni, posFin);
						String razonSocial = "";
						// logger.info("razonSocialTemp===>"+razonSocialTemp);
						if (razonSocialTemp.indexOf("</td>") > -1) {
						  int posNombre = razonSocialTemp.indexOf("</td>");
						  razonSocial = razonSocialTemp.substring(0, posNombre);
						 // System.err.println("nombre====>"+nombreEmpleado);  
						}
						constForm.setRazonSocial(razonSocial);
					} catch (Exception e) {
						constForm.setRazonSocial(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("Nombre:") + 64;
						posFin = posIni + 40;
						String nombreTemp = contenidoSAT.substring(posIni, posFin);
						 String nombreEmpleado = "";
						if (nombreTemp.indexOf("</td>") > -1) {
						  int posNombre = nombreTemp.indexOf("</td>");
						  nombreEmpleado = nombreTemp.substring(0, posNombre);
						 // System.err.println("nombre====>"+nombreEmpleado);  
						}
						constForm.setNombreEmpleado(nombreEmpleado);
					} catch (Exception e) {
						constForm.setNombreEmpleado(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("Apellido Paterno:") + 74;
						posFin = posIni + 40;
						String apellidoPaternoTemp = contenidoSAT.substring(posIni, posFin);
						String apellidoPaterno = "";
						if (apellidoPaternoTemp.indexOf("</td>") > -1) {
						  int posApellido = apellidoPaternoTemp.indexOf("</td>");
						  apellidoPaterno = apellidoPaternoTemp.substring(0, posApellido);
						 // System.err.println("apellidoPaterno====>"+apellidoPaterno);  
						}
						constForm.setApellidoPaterno(apellidoPaterno);
					} catch (Exception e) {
						constForm.setApellidoPaterno(NO_ENCONTRADO);
					}
					
					
					try {
						posIni = contenidoSAT.indexOf("Apellido Materno:") + 74;
						posFin = posIni + 40;
						String apellidoMaternoTemp = contenidoSAT.substring(posIni, posFin);
						String apellidoMaterno = "";
						if (apellidoMaternoTemp.indexOf("</td>") > -1) {
						  int posApellido = apellidoMaternoTemp.indexOf("</td>");
						  apellidoMaterno = apellidoMaternoTemp.substring(0, posApellido);
						 // System.err.println("apellidoPaterno====>"+apellidoMaterno);  
						}
						constForm.setApellidoMaterno(apellidoMaterno);
					} catch (Exception e) {
						constForm.setApellidoMaterno(NO_ENCONTRADO);
					}
					
					
					try {
						posIni = contenidoSAT.indexOf("Fecha Nacimiento:") + 74;
						posFin = posIni + 40;
						String fechaNacimientoTemp = contenidoSAT.substring(posIni, posFin);
						String fechaNacimiento = "";
						if (fechaNacimientoTemp.indexOf("</td>") > -1) {
						  int posValor = fechaNacimientoTemp.indexOf("</td>");
						  fechaNacimiento = fechaNacimientoTemp.substring(0, posValor);
						 // System.err.println("apellidoPaterno====>"+apellidoMaterno);  
						}
						constForm.setFechaNacimiento(fechaNacimiento);
					} catch (Exception e) {
						constForm.setFechaNacimiento(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf(" del contribuyente:") + 76;
						posFin = posIni + 6;
						String situacionContribuyente = contenidoSAT.substring(posIni, posFin);
						constForm.setSituacionContribuyente(situacionContribuyente);
					} catch (Exception e) {
						constForm.setSituacionContribuyente(NO_ENCONTRADO);
					}
					
					
					try {
						posIni = contenidoSAT.indexOf("CP:") + 60;
						posFin = posIni + 5;
						String codigoPostal = contenidoSAT.substring(posIni, posFin);
						// System.err.println("codigoPostal====>"+codigoPostal);
						try {
							constForm.setCodigoPostal(Utils.noNuloINT(codigoPostal));	
						}catch(Exception e) {
							constForm.setCodigoPostal(0);
						}
					} catch (Exception e) {
						constForm.setCodigoPostal(0);
					}
					
					try {
						posIni = contenidoSAT.indexOf("Fecha de Inicio de operaciones:") + 88;
						posFin = posIni + 10;
						String fechaOperaciones = contenidoSAT.substring(posIni, posFin);
						// System.err.println("fechaOperaciones====>"+fechaOperaciones);
						constForm.setFechaOperaciones(fechaOperaciones);
					} catch (Exception e) {
						constForm.setFechaOperaciones(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("Correo electr") + 76;
						posFin = posIni + 40;
						String correoElectronicoTemp = contenidoSAT.substring(posIni, posFin);
						String correoElectronico = "";
						if (correoElectronicoTemp.indexOf("</td>") > -1) {
						  int posApellido = correoElectronicoTemp.indexOf("</td>");
						  correoElectronico = correoElectronicoTemp.substring(0, posApellido);
						  // System.err.println("correoElectronico====>"+correoElectronico);  
						}
						constForm.setCorreoElectronico(correoElectronico);
					} catch (Exception e) {
						constForm.setCorreoElectronico(NO_ENCONTRADO);
					}
					
					
					try {
						posIni = contenidoSAT.indexOf("gimen de capital:") + 74;
						posFin = posIni + 18;
						String regimenCapitalTemp = contenidoSAT.substring(posIni, posFin);
						// System.err.println("regimenCapitalTemp====>"+regimenCapitalTemp);  
						String regimenCapital = "";
						if (regimenCapitalTemp.indexOf("</td>") > -1) {
						  int posRegimen = regimenCapitalTemp.indexOf("</td>");
						  regimenCapital = regimenCapitalTemp.substring(0, posRegimen);
						 // System.err.println("regimenCapital====>"+regimenCapital);  
						}
						constForm.setRegimenCapital(regimenCapital);
						
					} catch (Exception e) {
						constForm.setRegimenCapital(NO_ENCONTRADO);
					}
					
					
					try {
						posIni = contenidoSAT.indexOf("cambio de situaci") + 77;
						posFin = posIni + 20;
						String fechaUltCambioSituacionTemp = contenidoSAT.substring(posIni, posFin);
						// logger.info("fechaUltCambioSituacionTemp===>"+fechaUltCambioSituacionTemp);
						// System.err.println("fechaUltCambioSituacionTemp====>"+fechaUltCambioSituacionTemp);  
						String fechaUltCambioSituacion= "";
						if (fechaUltCambioSituacionTemp.indexOf("</td>") > -1) {
						  int posRegimen = fechaUltCambioSituacionTemp.indexOf("</td>");
						  fechaUltCambioSituacion = fechaUltCambioSituacionTemp.substring(0, posRegimen);
						 // System.err.println("fechaUltCambioSituacion====>"+fechaUltCambioSituacion);  
						}
						constForm.setFechaUltCambioSituacion(fechaUltCambioSituacion);
						
					} catch (Exception e) {
						constForm.setFechaUltCambioSituacion(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("Entidad Federativa:") + 76;
						posFin = posIni + 40;
						String entidadFederativaTemp = contenidoSAT.substring(posIni, posFin);
						// System.err.println("entidadFederativaTemp====>"+entidadFederativaTemp);  
						String entidadFederativa= ""; 
						if (entidadFederativaTemp.indexOf("</td>") > -1) {
						  int posValor = entidadFederativaTemp.indexOf("</td>");
						  entidadFederativa = entidadFederativaTemp.substring(0, posValor);
						//  System.err.println("entidadFederativa====>"+entidadFederativa);  
						}
						constForm.setEntidadFederativa(entidadFederativa);
						
					} catch (Exception e) {
						constForm.setEntidadFederativa(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("Municipio o delegaci") + 80;
						posFin = posIni + 40;
						String municipioTemp = contenidoSAT.substring(posIni, posFin);
						// System.err.println("municipioTemp====>"+municipioTemp);  
						String municipio = ""; 
						if (municipioTemp.indexOf("</td>") > -1) {
						  int posValor = municipioTemp.indexOf("</td>");
						  municipio = municipioTemp.substring(0, posValor);
						 // System.err.println("municipio====>"+municipio);  
						}
						constForm.setMunicipio(municipio);
						
					} catch (Exception e) {
						constForm.setMunicipio(NO_ENCONTRADO);
					}
					
					
					try {
						posIni = contenidoSAT.indexOf("Colonia:") + 65;
						posFin = posIni + 40;
						String coloniaTemp = contenidoSAT.substring(posIni, posFin);
						// System.err.println("coloniaTemp====>"+coloniaTemp);  
						String colonia = ""; 
						if (coloniaTemp.indexOf("</td>") > -1) {
						  int posValor = coloniaTemp.indexOf("</td>");
						  colonia = coloniaTemp.substring(0, posValor);
						 // System.err.println("colonia====>"+colonia);  
						}
						constForm.setColonia(colonia);
						
					} catch (Exception e) {
						constForm.setColonia(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("Tipo de vialidad:") + 74;
						posFin = posIni + 40;
						String tipoVialidadTemp = contenidoSAT.substring(posIni, posFin);
						// System.err.println("tipoVialidadTemp====>"+tipoVialidadTemp);  
						String tipoVialidad = ""; 
						if (tipoVialidadTemp.indexOf("</td>") > -1) {
						  int posValor = tipoVialidadTemp.indexOf("</td>");
						  tipoVialidad = tipoVialidadTemp.substring(0, posValor);
						//  System.err.println("tipoVialidad====>"+tipoVialidad);  
						}
						constForm.setTipoVialidad(tipoVialidad);
						
					} catch (Exception e) {
						constForm.setTipoVialidad(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("Nombre de la vialidad:") + 79;
						posFin = posIni + 40;
						String nombreVialidadTemp = contenidoSAT.substring(posIni, posFin);
						//System.err.println("nombreVialidadTemp====>"+nombreVialidadTemp);  
						String nombreVialidad = ""; 
						if (nombreVialidadTemp.indexOf("</td>") > -1) {
						  int posValor = nombreVialidadTemp.indexOf("</td>");
						  nombreVialidad = nombreVialidadTemp.substring(0, posValor);
						//  System.err.println("nombreVialidad====>"+nombreVialidad); 
						}
						constForm.setNombreVialidad(nombreVialidad);
						
					} catch (Exception e) {
						constForm.setNombreVialidad(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("mero exterior:") + 71;
						posFin = posIni + 40;
						String numeroExtTemp = contenidoSAT.substring(posIni, posFin);
						// System.err.println("numeroExtTemp====>"+numeroExtTemp);  
						String numeroExt = ""; 
						if (numeroExtTemp.indexOf("</td>") > -1) {
						  int posValor = numeroExtTemp.indexOf("</td>");
						  numeroExt = numeroExtTemp.substring(0, posValor);
						//  System.err.println("numeroExt====>"+numeroExt); 
						}
						constForm.setNumeroExt(numeroExt);
						
					} catch (Exception e) {
						constForm.setNumeroExt(NO_ENCONTRADO);
					}
					
					try {
						posIni = contenidoSAT.indexOf("mero interior:") + 71;
						posFin = posIni + 40;
						String numeroIntTemp = contenidoSAT.substring(posIni, posFin);
						// System.err.println("numeroIntTemp====>"+numeroIntTemp);  
						String numeroInt = ""; 
						if (numeroIntTemp.indexOf("</td>") > -1) {
						  int posValor = numeroIntTemp.indexOf("</td>");
						  numeroInt = numeroIntTemp.substring(0, posValor);
						//  System.err.println("numeroInt====>"+numeroInt); 
						}
						constForm.setNumeroInt(numeroInt);
						
					} catch (Exception e) {
						constForm.setNumeroInt(NO_ENCONTRADO);
					}
					
					try {
							int numVueltas = 1;
							posIni = contenidoSAT.indexOf("sticas fiscales (vigente)");
							int posFinal = contenidoSAT.length();
							String contenidoCaracteristicas = contenidoSAT.substring(posIni, posFinal);
							boolean continuar = true;
							do {
								try {
									posIni = contenidoCaracteristicas.indexOf("Fecha de alta:") + 71;
									posFin = posIni + 40;
									String fechaAltaTemp = contenidoCaracteristicas.substring(posIni, posFin);
									// logger.info("fechaAltaTemp===>"+fechaAltaTemp);
									String fechaAlta = ""; 
									if (fechaAltaTemp.indexOf("</td>") > -1) {
									  int posValor = fechaAltaTemp.indexOf("</td>");
									  fechaAlta = fechaAltaTemp.substring(0, posValor);
									 // System.err.println("fechaAlta====>"+fechaAlta); 
									}
									// logger.info("fechaAlta===>"+fechaAlta);
									constForm.setFechaAlta(fechaAlta);
									contenidoCaracteristicas = contenidoCaracteristicas.substring(posIni, contenidoCaracteristicas.length());
									if (contenidoCaracteristicas.indexOf("Fecha de alta:") > -1) {
										continuar = true;
									}else {
										continuar = false;
									}
									numVueltas++;
									if (numVueltas >= 11) {
										continuar = false;
									}
								}catch(Exception e) {
									continuar = false;
									constForm.setFechaAlta(NO_ENCONTRADO);
								}
								
							} while (continuar);
							
						
					} catch (Exception e) {
						constForm.setFechaAlta(NO_ENCONTRADO);
					}
					
					
					try {
						int numVueltas = 1;
						posIni = contenidoSAT.indexOf("sticas fiscales (vigente)");
						int posFinal = contenidoSAT.length();
						String contenidoCaracteristicas = contenidoSAT.substring(posIni, posFinal);
						boolean continuar = true;
						do {
							// System.err.println("contenidoCaracteristicas====>"+contenidoCaracteristicas);  
							try {
									posIni = contenidoCaracteristicas.indexOf("gimen:") + 63;
									posFin = posIni + 80;
									String regimenTemp = contenidoCaracteristicas.substring(posIni, posFin);
									//System.err.println("regimenTemp====>"+regimenTemp); 
									String regimen = ""; 
									if (regimenTemp.indexOf("</td>") > -1) {
									  int posValor = regimenTemp.indexOf("</td>");
									  regimen = regimenTemp.substring(0, posValor);
									 // System.err.println("regimen====>"+regimen); 
									}
									// logger.info("regimen====>"+regimen); 
									constForm.setRegimen(regimen);
									contenidoCaracteristicas = contenidoCaracteristicas.substring(posIni, contenidoCaracteristicas.length());
									if (contenidoCaracteristicas.indexOf("gimen:") > -1) {
										continuar = true;
									}else {
										continuar = false;
									}
									numVueltas++;
									if (numVueltas >= 11) {
										continuar = false;
									}
							}catch(Exception e){
								continuar = false;
								constForm.setRegimen(NO_ENCONTRADO);
							}
						} while (continuar);
							
							
						
					
				} catch (Exception e) {
					constForm.setRegimen(NO_ENCONTRADO);
				}
					
				}	
			}
			
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return constForm;
	}
	
	
	private String ejecutarLinkSAT(String cedulaFiscal, String RFCEmpleado) {
		StringBuffer sbLine = new StringBuffer();
		
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { 
		    new X509TrustManager() {     
		        public X509Certificate[] getAcceptedIssuers() { 
		            return new X509Certificate[0];
		        } 
		        public void checkClientTrusted( 
		            X509Certificate[] certs, String authType) {
		            } 
		        public void checkServerTrusted( 
		            X509Certificate[] certs, String authType) {
		        }
		    } 
		}; 

		// Install the all-trusting trust manager
		try {
		    SSLContext sc = SSLContext.getInstance("SSL"); 
		    sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (GeneralSecurityException e) {
		} 
		
		try {
			 URL url;
		        InputStream is = null;
		        BufferedReader br;
		        String line;
		        //Install Authenticator
		        try {
		        	String urlSAT = "https://siat.sat.gob.mx/app/qr/faces/pages/mobile/validadorqr.jsf?D1=10&D2=1&D3="+cedulaFiscal+"_"+RFCEmpleado;
//		        	logger.info("urlSAT===>"+urlSAT);
		            url = new URL(urlSAT);
		            is = url.openStream();  //throws an IOException
		            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		            while ((line = br.readLine()) != null) {
		            	// System.err.println("line====>"+line);
		                sbLine.append(line);
		            }
		        } catch (MalformedURLException mue) {
		             mue.printStackTrace();
		        } catch (IOException ioe) {
		             ioe.printStackTrace();
		        } finally {
		            try {
		                if (is != null) is.close();
		            } catch (IOException ioe) {
		                //nothing to see here
		            }
		        }

		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbLine.toString();
	}
	
}
