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
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.itextpdf.xmltopdf.Pagos.PDoctoRelacionado;
import com.itextpdf.xmltopdf.Pagos.Pago;
import com.itextpdf.xmltopdf.Pagos20.DoctoRelacionado;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.configuraciones.EtiquetaComp.EtiquetaCompBean;
import com.siarex247.configuraciones.EtiquetaComp.EtiquetaCompForm;
import com.siarex247.configuraciones.RegimenFiscal.RegimenFiscalBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.MensajesSIAREX;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.visor.VisorOrdenes.ComplementosForm;
import com.siarex247.visor.VisorOrdenes.NotaCreditoForm;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;

public class ValidacionesComplemento {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public HashMap<String, Object> procesarXML(Connection con, String esquema, String rutaXML, String rutaPDF, String usuarioHTTP, String idLenguaje, boolean bandElimnaArchivos) {
		// String mensajeXML = "";
		HashMap<String, Object> mapaResultado = new HashMap<>();
		try {
			String pathRepositorio = UtilsPATH.REPOSITORIO_DOCUMENTOS;
			
			mapaResultado.put("MENSAJE", "ERROR");
			mapaResultado.put("rows", new JSONArray());

			File fileXML = new File(rutaXML);
			File filePDF = new File(rutaPDF);

			VisorOrdenesForm visorForm = null;

			String nombreXML = null;
			String nombrePDF = null;

			Comprobante _comprobante = null;
			try {
				_comprobante = LeerXML.ObtenerComprobante(rutaXML);
			} catch (Exception e) {
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
			
			
			logger.info("versionXML====>"+versionXML);
			if ("".equalsIgnoreCase(versionXML)) {
				versionXML = "1.0";
			}

			//logger.info("_comprobante.getVersion()------------>" + _comprobante.getVersion());
			//logger.info("_comprobante.getTipoDeComprobante()------------>" + _comprobante.getTipoDeComprobante());

			if ("1.0".equalsIgnoreCase(versionXML)) {
				if (_comprobante.getComplemento().Pagos != null
						&& _comprobante.getComplemento().Pagos.getPago().size() > 0) {
					if (!"P".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE42);
						mapaResultado.put("MENSAJE", mensajeFinal);
						return mapaResultado;
					} else {
						HashMap<String, String> mapaMensajeComplemento = validarEtiquetasXMLCOMP(con, esquema, _comprobante, versionXML);

						if ("FALSE".equalsIgnoreCase(mapaMensajeComplemento.get("ERROR"))) {
							mapaResultado.put("MENSAJE", mapaMensajeComplemento.get("MENSAJE"));
							return mapaResultado;
						}
					}
					
					// se validan las etiquetas Base
					HashMap<String, String> mapaMensajeBase = validarEtiquetasBase(con, esquema, _comprobante, versionXML);
					logger.info("mapaMensajeBase ------------>" + mapaMensajeBase.get("ERROR"));
					if ("FALSE".equalsIgnoreCase(mapaMensajeBase.get("ERROR"))) {
						mapaResultado.put("MENSAJE", mapaMensajeBase.get("MENSAJE"));
						return mapaResultado;
					}
					

					// se obtiene bandera de tipo de validacion

					String rfcFacturaXML = _comprobante.getEmisor().getRfc();
					String razonSocialXML = _comprobante.getEmisor().getNombre();
					// String folioXML = _comprobante.getFolio();
					// String serieXML = _comprobante.getSerie();
					String uuidComplemento = _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
					double totalXML = _comprobante.getTotal();
					String rfcReceptorXML = _comprobante.getReceptor().getRfc();
					LocalDateTime FECHA_TIMBRADOLD = _comprobante.getComplemento().TimbreFiscalDigital
							.getFechaTimbrado();
					String TIPO_COMPROBANTE = _comprobante.getTipoDeComprobante();
					LocalDateTime FECHA_XMLLD = _comprobante.getFecha();
					//logger.info("rfcReceptorXML------------>" + rfcReceptorXML);
					String FECHA_TIMBRADO = null;
					if (FECHA_TIMBRADOLD != null) {
						FECHA_TIMBRADO = FECHA_TIMBRADOLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					}

					String FECHA_XML = null;
					if (FECHA_XMLLD != null) {
						FECHA_XML = FECHA_XMLLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					}

					// Son etiquetas parte de los pagos
					double MONTO_PAGADO = 0;
					String FECHA_PAGO = "";
					String FORMA_PAGO = "";

					nombreXML = "COM_" + rfcFacturaXML + uuidComplemento + ".xml";
					nombrePDF = "COM_" + rfcFacturaXML + uuidComplemento + ".pdf";

					//logger.info("Nombre del XML Final : " + nombreXML);
					//logger.info("Nombre del PDF Final : " + nombrePDF);

					boolean bandArchivo = true;

					String validaCOMPLE = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "RECHAZAR_COMPLE");
					
					//logger.info("validaCOMPLE----->" + validaCOMPLE);
					String estadoSAT = "";
					String estatusSAT = "";
					if ("S".equalsIgnoreCase(validaCOMPLE)) {

						String datosSAT[] = UtilsSAT.validaSAT(rfcFacturaXML, rfcReceptorXML, totalXML, uuidComplemento);
						estadoSAT = datosSAT[0];
						estatusSAT = datosSAT[1];

						//logger.info("estado--->" + estadoSAT);
						//logger.info("estatusSAT--->" + estatusSAT);

						if (estadoSAT.toUpperCase().indexOf("CANCELADO") > -1) {
							MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
							// cadenaRetorno = "CANCELADO"+ ";" + mensajeSIAREX.MENSAJE29;
							String mensajeFinal = Utils.getMensajeValidacion(
									Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE29), "<< UUID_COMPLE >>",
									uuidComplemento);
							mapaResultado.put("MENSAJE", mensajeFinal);
							return mapaResultado;
						}
					} // FOLIO_EMPRESA
					String rutaDesXML = null;
					String rutaDesPDF = null;
					File fdesXML = null;
					File fdesPDF = null;
					String directorioPDF = null;
					String directorioXML = null;

					double MONTO_FACTURA = 0;
					double TOTAL_COMPLEMENTO = 0;
					int claveProveedor = 0;

					List<PDoctoRelacionado> listaDocto = null;
					PDoctoRelacionado docRel = null;

					ComplementosForm compleForm = null;
					ComplementosForm compleTrabajoForm = null;

					boolean esIgualComple = false;
					HashMap<String, String> mapaUUIDXML = new HashMap<>();
					int totComprobantes = _comprobante.getComplemento().Pagos.getPago().size();
					int totRelacionados = 0;

					// List<CfdiRelacionado> listRel =
					// _comprobante.CfdiRelacionados().CfdiRelacionado();
					eliminarComplementariaTrabajo(con, esquema, uuidComplemento); // se eliminan las ordens del
																					// complemento.

					//logger.info("totComprobantes---->" + totComprobantes);
					for (int x = 0; x < totComprobantes; x++) {
						Pago pago = _comprobante.getComplemento().Pagos.getPago().get(x);

						LocalDateTime FECHA_PAGOLD = pago.getFechaPago();

						if (FECHA_PAGOLD != null) {
							FECHA_PAGO = FECHA_PAGOLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
						}

						FORMA_PAGO = pago.getFormaDePagoP();
						//logger.info("Monto()---->" + pago.getMonto());
						MONTO_PAGADO = pago.getMonto();

						listaDocto = pago.getDoctoRelacionado();
						totRelacionados = listaDocto.size();

						//logger.info("totRelacionados---->" + totRelacionados);
						for (int y = 0; y < totRelacionados; y++) {
							// jsonUUID = (JSONObject) jsonArray.get(x);
							docRel = listaDocto.get(y);
							visorForm = buscarOrden(con, esquema, docRel.getIdDocumento());

							//logger.info("UUID ORDEN------------->" + docRel.getIdDocumento());
							//logger.info("Folio------------->" + visorForm.getFolioEmpresa());

							compleTrabajoForm = buscarUUIDCompleTrabajo(con, esquema, visorForm.getFolioEmpresa());

							String estatusPago = visorForm.getEstatusOrden(); // Utils.noNulo(jsonInfoUUID.get("ESTATUS_PAGO")).toString();
							if ("".equals(Utils.noNulo(visorForm.getEstatusOrden()))) { // no encontro registros
								//logger.info("*************** NO ENCONTRO INFORMACION *******************");
								MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
								String mensajeFinal = Utils.getMensajeValidacion(
										Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE38), "<< UUID_COMPLE >>",
										docRel.getIdDocumento());
								mapaResultado.put("MENSAJE", mensajeFinal);
								return mapaResultado;

							} else if (!"A4".equalsIgnoreCase(estatusPago)) {
								// return "ESTATUS_NG";
								// logger.info("*************** EL ESTATUS ES DIFERENTE DE A4 *******************");
								MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
								String mensajeFinal = Utils.getMensajeValidacion(
										Utils.getMensajeValidacion(
												Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE36),
												"<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())),
										"<< UUID_COMPLE >>", docRel.getIdDocumento());
								mapaResultado.put("MENSAJE", mensajeFinal);
								return mapaResultado;
							}

							if (bandArchivo) {
								//logger.info("*************** CUMPLIO CON VALIDACIONES DE LA ORDEN *******************" + visorForm.getFolioEmpresa());
								claveProveedor = visorForm.getClaveProveedor();
								directorioXML = esquema + "/PROVEEDORES/" + visorForm.getClaveProveedor() + "/"
										+ nombreXML;
								directorioPDF = esquema + "/PROVEEDORES/" + visorForm.getClaveProveedor() + "/"
										+ nombrePDF;
								bandArchivo = false;
							}

							if (!"".equals(Utils.noNulo(compleTrabajoForm.getUuidComplemento()))) { // si encontro registro en la complementaria
								String estatusComple = compleTrabajoForm.getEstatusComplemento();
								if ("PDT".equalsIgnoreCase(estatusComple)) {
									String uuidComple = compleTrabajoForm.getUuidComplemento();
									eliminarComplementariaTrabajo(con, esquema, uuidComple); // se eliminan las ordens del complemento anterior.
								}
							}

							compleForm = buscarUUIDComple(con, esquema, visorForm.getFolioEmpresa());
							// datosComple = buscarUUIDComple(con, esquema, visorForm.getFolioEmpresa());
							//logger.info("datosComple------------->" + Utils.noNulo(compleForm.getUuidComplemento()));

							if (!"".equals(Utils.noNulo(compleForm.getUuidComplemento()))) {
								String uuidComple = compleForm.getUuidComplemento();

								if (uuidComple.equalsIgnoreCase(uuidComplemento)) { // es el mismo XML del proveedor y
																					// el que se subio por boveda
									esIgualComple = true;
								} else {
									eliminarComplementariaTrabajo(con, esquema, uuidComplemento); // se eliminan las ordens del complemento.
									//logger.info("*************** REGISTRO DUPLICADO *******************");

									//logger.info("*************** uuidComple_duplicado *******************" + uuidComple);
									MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
									String mensajeFinal = Utils.getMensajeValidacion(
											Utils.getMensajeValidacion(
													Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE40),
													"<< UUID_COMPLEMENTO >>", uuidComple),
											"<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
									mapaResultado.put("MENSAJE", mensajeFinal);
									return mapaResultado;
								}
							}

							//logger.info("ImpPagado()---->" + docRel.getImpPagado());
							//logger.info("MONTO_PAGADO---->" + MONTO_PAGADO);

							if (totRelacionados == 1) {
								MONTO_FACTURA = MONTO_PAGADO;
							} else {
								MONTO_FACTURA = docRel.getImpPagado();
							}

							mapaUUIDXML.put(docRel.getIdDocumento().toLowerCase(),
									docRel.getIdDocumento().toLowerCase()); // se van agregando los UUID del XML
							//logger.info("MONTO_FACTURA---->" + MONTO_FACTURA);
							//logger.info("MONTO_PAGADO---->" + MONTO_PAGADO);

							int totGrabar = grabarComplementariaTrabajo(con, esquema, uuidComplemento, MONTO_FACTURA,
									MONTO_PAGADO, FECHA_PAGO, FECHA_TIMBRADO, estatusSAT, nombreXML, nombrePDF,
									usuarioHTTP, docRel.getIdDocumento(), TIPO_COMPROBANTE, FORMA_PAGO);

							if (totGrabar == 1062) { // esta duplicado
								String uuidComple = compleTrabajoForm.getUuidComplemento();
								eliminarComplementariaTrabajo(con, esquema, uuidComple); // se eliminan las ordens del
																							// complemento.
								totGrabar = grabarComplementariaTrabajo(con, esquema, uuidComplemento, MONTO_FACTURA,
										MONTO_PAGADO, FECHA_PAGO, FECHA_TIMBRADO, estatusSAT, nombreXML, nombrePDF,
										usuarioHTTP, docRel.getIdDocumento(), TIPO_COMPROBANTE, FORMA_PAGO);
							}

							TOTAL_COMPLEMENTO += MONTO_FACTURA;
						}
					}

					//logger.info("TOTAL_COMPLEMENTO---->" + TOTAL_COMPLEMENTO);
					//logger.info("totRelacionados---->" + totRelacionados);

					// String mensajeValidacion = cumpleValidaciones(con, esquema, uuidComplemento,
					// FECHA_PAGO, GRAN_TOTAL_COMPLEMENTO, idLenguaje, claveProveedor, FECHA_XML,
					// mapaUUIDXML); // se realizan las validaciones
					// String estatus_Conciliacion = "NG";
					// String cadenaRetorno = null;
					if (totRelacionados == 0) {
						//logger.info("*************** NO ENCONTRO NINGUN COMPLEMENTO PARA VALIDAR *******************");
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE43);
						mapaResultado.put("MENSAJE", mensajeFinal);
						return mapaResultado;

					} else {
						DecimalFormat df = new DecimalFormat("#.##");
						String GRAN_TOTAL_COMPLEMENTO = df.format(TOTAL_COMPLEMENTO);
						logger.info("GRAN_TOTAL_COMPLEMENTO---->" + GRAN_TOTAL_COMPLEMENTO);

						mapaResultado = cumpleValidaciones(con, esquema, uuidComplemento, FECHA_PAGO,
								GRAN_TOTAL_COMPLEMENTO, idLenguaje, claveProveedor, FECHA_XML, mapaUUIDXML); // se realizan las validaciones
						String mensajeValidacion = Utils.noNulo(mapaResultado.get("MENSAJE")).toString();
						String estatus_Conciliacion = "NG";
						//logger.info("mensajeValidacion---->" + mensajeValidacion);
						if ("OK".equalsIgnoreCase(mensajeValidacion)) {
							if (esIgualComple) { // se eliminan los registros de la complementaria
								eliminarComplementaria(con, esquema, uuidComplemento);
								//logger.info("************* ELIMINANDO COMPLEMENTO ************************");
							}

							estatus_Conciliacion = "OK";
							updateComplementariaTrabajo(con, esquema, estatus_Conciliacion, uuidComplemento,
									GRAN_TOTAL_COMPLEMENTO);
							mapaResultado.put("CUMPLE_OK", "OK");
							mapaResultado.put("UUID_COMPLEMENTO", uuidComplemento);
							mapaResultado.put("RAZON_SOCIAL", razonSocialXML);
							mapaResultado.put("ESTADO_SAT", estadoSAT);
							mapaResultado.put("ESTATUS_SAT", estatusSAT);
							// cadenaRetorno = "CUMPLE_OK" + ";" + uuidComplemento + ";"+ razonSocialXML +
							// ";" + estadoSAT + ";" + estatusSAT;

							rutaDesXML = pathRepositorio + directorioXML;
							fdesXML = new File(rutaDesXML);
							UtilsFile.moveFileDirectory(fileXML, fdesXML, true, false, true, bandElimnaArchivos);

							rutaDesPDF = pathRepositorio + directorioPDF;
							fdesPDF = new File(rutaDesPDF);
							logger.info("Ruta Final PDF : " + rutaDesPDF);
							logger.info("Ruta Final XML : " + rutaDesXML);

							UtilsFile.moveFileDirectory(filePDF, fdesPDF, true, false, true, bandElimnaArchivos);

						} else {
							// cadenaRetorno = mensajeValidacion;
							// eliminarComplementariaTrabajo(con, esquema, uuidComplemento); // se eliminan
							// las ordens del complemento.
							rutaDesXML = pathRepositorio + directorioXML;
							rutaDesPDF = pathRepositorio + directorioPDF;

							fdesXML = new File(rutaDesXML);
							fdesPDF = new File(rutaDesPDF);
							fdesXML.delete();
							fdesPDF.delete();
						}
					}

					return mapaResultado;

				} else if (_comprobante != null && _comprobante.getTipoDeComprobante() != null
						&& !"P".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
					MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
					String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE42);
					mapaResultado.put("MENSAJE", mensajeFinal);
					logger.info("mensajeFinal 1...." + mensajeFinal);
					return mapaResultado;

				} else {
					MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
					String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32);
					mapaResultado.put("MENSAJE", mensajeFinal);
					logger.info("mensajeFinal 2...." + mensajeFinal);
					return mapaResultado;
				}
			} else if ("2.0".equalsIgnoreCase(versionXML)) {
//				logger.info("else 4.0....");
				mapaResultado = procesarXML20(con, esquema, rutaXML, rutaPDF, pathRepositorio, usuarioHTTP, idLenguaje, bandElimnaArchivos, versionXML);
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32);
			mapaResultado.put("MENSAJE", mensajeFinal);
		}
		return mapaResultado;
	}

	private HashMap<String, Object> cumpleValidaciones(Connection con, String esquema, String uuidComplemento,
			String FECHA_PAGO, String GRAN_TOTAL_COMPLEMENTO, String idLenguaje, int claveProveedor,
			String fechaFactura, HashMap<String, String> mapaUUIDXML) {
		String mensajeFinal = "";
		MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
		// DecimalFormat decimalFormat = new DecimalFormat("#.##");
		DecimalFormat decimalFormat = new DecimalFormat("######.##");
		// UtilsValidaciones utilsValidaciones = new UtilsValidaciones();
		ProveedoresBean provBean = new ProveedoresBean();
		ProveedoresForm proveForm = null;
		HashMap<String, Object> mapaResultado = new HashMap<>();

		try {

			mapaResultado.put("MENSAJE", "ERROR");
			mapaResultado.put("rows", new JSONArray());

			// se obtiene bandera de tipo de validacion
			// Termina

			proveForm = provBean.consultarProveedor(con, esquema, claveProveedor);

			String bandValidaFechas = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "BAND_VALIDFECHAS_COMPLE");
			
			String limiteComplemento = proveForm.getLimiteComplemento();
			if (limiteComplemento == null || limiteComplemento.equals("")) {
				limiteComplemento = "0";
			}

			//logger.info("bandValidaFechas-------------------->" + bandValidaFechas);
			//logger.info("limiteTolerancia-------------------->" + limiteComplemento);

			HashMap<String, String> mapaFechaPago = null;
			if ("S".equalsIgnoreCase(bandValidaFechas)) {
				mapaFechaPago = validarFechaPago(con, esquema, uuidComplemento, FECHA_PAGO);
			} else {
				mapaFechaPago = new HashMap<>();
				mapaFechaPago.put("BANDERA_CUMPLE", "true");
				mapaFechaPago.put("FOLIO_EMPRESA", "");

			}

			String bandCumpleFechas = mapaFechaPago.get("BANDERA_CUMPLE");
			// logger.info("mapaFechaPago-------------------->"+mapaFechaPago);
			String folioEmpresa = mapaFechaPago.get("FOLIO_EMPRESA");
			HashMap<String, Object> mapaOrdenes = obtenerTotalOrdenes(con, esquema, uuidComplemento, mapaUUIDXML);
			double totalOrdenes = Double.parseDouble(mapaOrdenes.get("TOTAL_ORDENES").toString());
			// logger.info("mapaOrdenes-------------------->"+mapaOrdenes);
			mapaOrdenes.remove("TOTAL_ORDENES"); // se elimina del mapa
			// logger.info("totalOrdenes antes-------------------->"+totalOrdenes);
			String totalOrdenesStr = decimalFormat.format(totalOrdenes);
			// logger.info("totalOrdenesStr-------------------->"+totalOrdenesStr);
			double totalOrdenesFin = Double.parseDouble(totalOrdenesStr);

			/*
			 * logger.info("totalOrdenes-------------------->"+totalOrdenesFin);
			 * logger.info("TOTAL_COMPLEMENTO parseado-------------------->"+Double.
			 * parseDouble( GRAN_TOTAL_COMPLEMENTO ));
			 * logger.info("TOTAL_COMPLEMENTO-------------------->"+GRAN_TOTAL_COMPLEMENTO);
			 * logger.info("bandCumpleFechas-------------------->"+bandCumpleFechas);
			 */
			// logger.info(" *********** CALCULANDO EL CIERRE DE AÑO DE LOS COMPLEMENTOS
			// *****************");
			String mensajeFecha = UtilsValidaciones.validaFecha(con, esquema, fechaFactura, "", "C");
			// logger.info("bandCumpleFechas-------------------->"+bandCumpleFechas);

			if ("false".equalsIgnoreCase(bandCumpleFechas)) {
				// logger.info("*************** NO CUMPLIO CON FECHAS **********************");
				mensajeFinal = Utils.regresaCaracteresNormales(Utils.getMensajeValidacion(
						Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE34), "<< FOLIO_FACTURA >>", folioEmpresa));
				mapaResultado.put("MENSAJE", mensajeFinal);
			} else if (!UtilsValidaciones.algoritoTotal(Double.parseDouble(GRAN_TOTAL_COMPLEMENTO), totalOrdenesFin,
					limiteComplemento)) {
				// logger.info("*************** NO CUMPLIO EL TOTAL **********************");
				mensajeFinal = Utils.regresaCaracteresNormales(Utils.getMensajeValidacion(
						Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE35), "<< FOLIO_FACTURA >>", folioEmpresa));
				// JSONArray rowArray = comparaTotales(mapaUUIDXML, mapaOrdenes);
				mapaResultado.put("MENSAJE", mensajeFinal);
				mapaResultado.put("BAND_TOTAL", "NO_CUMPLIO");
				mapaResultado.put("UUID_COMPLEMENTO", uuidComplemento);

			} else if (!"OK".equalsIgnoreCase(mensajeFecha) && !"NG".equalsIgnoreCase(mensajeFecha)) {
				// logger.info("*************** NO CUMPLIO FECHA DE CIERRE
				// **********************"+mensajeFecha);
				mensajeFinal = Utils.regresaCaracteresNormales(mensajeFecha);
				mapaResultado.put("MENSAJE", mensajeFinal);
			} else {
				// logger.info("*************** PASO TODAS LAS VALIDACIONES
				// **********************");
				mensajeFinal = "OK";
				mapaResultado.put("MENSAJE", mensajeFinal);
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return mapaResultado;
	}

	public VisorOrdenesForm buscarOrden(Connection con, String esquema, String uuid) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		VisorOrdenesForm visorForm = new VisorOrdenesForm();

		try {

			stmt = con.prepareStatement(ValidacionesComplementoQuerys.getBuscarOrdenUUID(esquema));
			stmt.setString(1, uuid);
			// logger.info("stmt===>"+stmt);
			rs = stmt.executeQuery();

			DecimalFormat decimal = new DecimalFormat("###,###.##");
			String estatusPago = null;
			while (rs.next()) {
				estatusPago = Utils.noNulo(rs.getString(7));
				visorForm.setFolioOrden(rs.getLong(1));
				visorForm.setFolioEmpresa(rs.getLong(2));
				visorForm.setMonto(decimal.format(rs.getDouble(3)));
				visorForm.setFechaPago(Utils.noNulo(rs.getString(4)));
				visorForm.setClaveProveedor(rs.getInt(5));
				visorForm.setTipoOrden(Utils.noNulo(rs.getString(6)));
				visorForm.setEstatusOrden(estatusPago);

				if (!"A4".equalsIgnoreCase(estatusPago)) {
					break;
				}
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
		return visorForm;
	}

	public ComplementosForm buscarUUIDCompleTrabajo(Connection con, String esquema, long folioEmpresa) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ComplementosForm compleForm = new ComplementosForm();
		try {
			stmt = con.prepareStatement(ValidacionesComplementoQuerys.getBuscarComplementoUUIDTrabajo(esquema));
			stmt.setLong(1, folioEmpresa);
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

	public ComplementosForm buscarUUIDComple(Connection con, String esquema, long folioEmpresa) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ComplementosForm compleForm = new ComplementosForm();

		try {
			stmt = con.prepareStatement(ValidacionesComplementoQuerys.getBuscarComplementoUUID(esquema));
			stmt.setLong(1, folioEmpresa);
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

	public int grabarComplementariaTrabajo(Connection con, String esquema, String uuid_Complemento,
			double MONTO_FACTURA, double MONTO_PAGADO, String FECHA_PAGO, String FECHA_TIMBRADO,
			String estatus_COMPLEMENTO, String nombrXML, String nombrePDF, String usuarioHTTP, String uuid_Factura,
			String TIPO_COMPROBANTE, String FORMA_PAGO) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totGrabar = 0;
		try {
			stmt = con.prepareStatement(ValidacionesComplementoQuerys.getGrabarComplementariaTrabajo(esquema));
			stmt.setString(1, uuid_Complemento);
			stmt.setDouble(2, MONTO_FACTURA);
			stmt.setDouble(3, MONTO_PAGADO);
			stmt.setString(4, FECHA_PAGO);
			stmt.setString(5, FECHA_TIMBRADO);
			stmt.setString(6, estatus_COMPLEMENTO);
			stmt.setString(7, nombrXML);
			stmt.setString(8, nombrePDF);
			stmt.setString(9, usuarioHTTP);
			stmt.setString(10, TIPO_COMPROBANTE);
			stmt.setString(11, FORMA_PAGO);
			stmt.setString(12, uuid_Factura);
			totGrabar = stmt.executeUpdate();

		} catch (SQLException sql) {
			logger.info("Error Code......." + sql.getErrorCode());
			if (sql.getErrorCode() == 1062 || sql.getErrorCode() == -1062) {
				totGrabar = 1062;
			}
			// Utils.imprimeLog("", sql);
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
		return totGrabar;
	}

	public int eliminarComplementariaTrabajo(Connection con, String esquema, String uuid_Complemento) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totGrabar = 0;
		try {
			stmt = con.prepareStatement(ValidacionesComplementoQuerys.deleteComplementariaTrabajo(esquema));
			stmt.setString(1, uuid_Complemento);
			totGrabar = stmt.executeUpdate();

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
		return totGrabar;
	}

	public int eliminarComplementaria(Connection con, String esquema, String uuid_Complemento) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totGrabar = 0;
		try {
			stmt = con.prepareStatement(ValidacionesComplementoQuerys.deleteComplementaria(esquema));
			stmt.setString(1, uuid_Complemento);
			totGrabar = stmt.executeUpdate();

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
		return totGrabar;
	}

	public int updateComplementariaTrabajo(Connection con, String esquema, String estatus_Conciliacion,
			String uuid_Complemento, String TOTAL_COMPLEMENTO) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totGrabar = 0;
		try {
			stmt = con.prepareStatement(ValidacionesComplementoQuerys.updateComplementariaTrabajo(esquema));
			stmt.setString(1, estatus_Conciliacion);
			stmt.setString(2, TOTAL_COMPLEMENTO);
			stmt.setString(3, uuid_Complemento);

			totGrabar = stmt.executeUpdate();

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
		return totGrabar;
	}

	public HashMap<String, String> validarFechaPago(Connection con, String esquema, String uuid_Complemento,
			String fechaPagoComplemento) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, String> mapaCumple = new HashMap<String, String>();

		try {
			mapaCumple.put("BANDERA_CUMPLE", "true");
			mapaCumple.put("FOLIO_EMPRESA", "0");

			stmt = con.prepareStatement(ValidacionesComplementoQuerys.obtenerFechaFactura(esquema));
			stmt.setString(1, uuid_Complemento);
			rs = stmt.executeQuery();
			String fechaPagoOrden = null;
			long folioEmpresa = 0;
			while (rs.next()) {
				folioEmpresa = rs.getLong(1);
				fechaPagoOrden = Utils.noNulo(rs.getString(2));

				if (fechaPagoOrden.length() > 10) {
					fechaPagoOrden = fechaPagoOrden.substring(0, 10);
				}
				if (fechaPagoComplemento.equalsIgnoreCase(fechaPagoOrden)) {
					mapaCumple.put("BANDERA_CUMPLE", "true");
					mapaCumple.put("FOLIO_EMPRESA", String.valueOf(folioEmpresa));

				} else {
					mapaCumple.put("BANDERA_CUMPLE", "false");
					mapaCumple.put("FOLIO_EMPRESA", String.valueOf(folioEmpresa));
					break;
				}
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
		logger.info("bandCumple Fechas de complemento--------->" + mapaCumple);
		return mapaCumple;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> obtenerTotalOrdenes(Connection con, String esquema, String uuid_Complemento,
			HashMap<String, String> mapaUUIDXML) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double totalOrdenes = 0;
		double totalComple = 0;
		HashMap<String, Object> mapaTOTAL = new HashMap<>();

		JSONObject jsonCons = new JSONObject();
		try {
			mapaTOTAL.put("TOTAL_ORDENES", totalOrdenes);
			stmt = con.prepareStatement(ValidacionesComplementoQuerys.totalOrdenesTrabajo(esquema));
			stmt.setString(1, uuid_Complemento);
			rs = stmt.executeQuery();
			String tipoOrden = null;
			String tipoOrdenAnterior = null;
			String uuidOrden = null;
			NotaCreditoForm notaCreditoForm = null;
			 ValidacionesNotaCredito notaCreditoBean = new ValidacionesNotaCredito();
			 long folioOrden = 0;

			while (rs.next()) {
				tipoOrden = Utils.noNulo(rs.getString(2));
				if (tipoOrden.equalsIgnoreCase(tipoOrdenAnterior) && !"".equalsIgnoreCase(tipoOrden)) {
					logger.info("Validando orden : " + uuid_Complemento + "_ Tipo de ORDEN : " + tipoOrden);
				} else {

					folioOrden = rs.getLong(6);
					// jsonNotas.clear();
					notaCreditoForm = notaCreditoBean.buscarNotaCredito(con, esquema, folioOrden); //
					if (notaCreditoForm.getFolioEmpresa() > 0) {
						double totalOrden = rs.getDouble(1);
						double totalNota = Double.parseDouble(notaCreditoForm.getMontoNota());
						double restaTotales = totalOrden - totalNota;
						totalOrdenes += restaTotales;
					} else {
						totalOrdenes += rs.getDouble(1);
					}

					totalComple = rs.getDouble(5);
					uuidOrden = Utils.noNulo(rs.getString(4)).toLowerCase();
					jsonCons.put("UUID_XML", uuidOrden);
					jsonCons.put("TOTAL_COMPLE", totalComple);
					jsonCons.put("TOTAL_ORDEN", rs.getDouble(1));

					mapaTOTAL.put(uuidOrden, jsonCons);
					mapaUUIDXML.remove(uuidOrden); // se elimina el UUID
					jsonCons = new JSONObject();
				}
				// logger.info("Sumando.............."+rs.getDouble(1));
				tipoOrdenAnterior = tipoOrden;
			}

			if (!mapaUUIDXML.isEmpty()) {
				logger.info("Estos no coinciden...." + mapaUUIDXML);
			}
			logger.info("mapaUUIDXML : " + mapaUUIDXML);
			mapaTOTAL.put("TOTAL_ORDENES", totalOrdenes);
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
		// logger.info("totalOrdenes--------->"+totalOrdenes);
		return mapaTOTAL;
	}

	/*
	private JSONArray comparaTotales(HashMap<String, String> mapaUUIDXML, HashMap<String, Object> mapaOrdenes) {
		JSONArray jsonTabla = new JSONArray();
		JSONObject jsonMapa = null;
		try {
			Iterator<String> llavesMapa = mapaOrdenes.keySet().iterator();
			String uuidOrden = null;

			while (llavesMapa.hasNext()) {
				uuidOrden = llavesMapa.next();
				jsonMapa = (JSONObject) mapaOrdenes.get(uuidOrden);
				logger.info("UUID_XML---->" + jsonMapa.get("UUID_XML"));
				logger.info("TOTAL_COMPLE---->" + jsonMapa.get("TOTAL_COMPLE"));
				logger.info("TOTAL_ORDEN---->" + jsonMapa.get("TOTAL_ORDEN"));
				logger.info(
						"**********************************************************************************************");
				if (jsonMapa.get("TOTAL_COMPLE").equals(jsonMapa.get("TOTAL_ORDEN"))) {
					jsonMapa.put("ES_IGUAL", true);
				} else {
					jsonMapa.put("ES_IGUAL", false);
				}
				jsonTabla.add(jsonMapa);
			}
			logger.info("jsonTabla : " + jsonTabla);
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return jsonTabla;
	}
	*/

	public HashMap<String, Object> procesarXML20(Connection con, String esquema, String rutaXML, String rutaPDF,
			String pathRepositorio, String usuarioHTTP, String idLenguaje, boolean bandElimnaArchivos,
			String versionXML) {
		logger.info("<------------ procesarXML complementos 2.0------------>");
		HashMap<String, Object> mapaResultado = new HashMap<>();

		try {
			mapaResultado.put("MENSAJE", "ERROR");
			mapaResultado.put("rows", new JSONArray());

			File fileXML = new File(rutaXML);
			File filePDF = new File(rutaPDF);

			// JSONObject jsonInfoUUID = null;
			VisorOrdenesForm visorForm = null;

			String nombreXML = null;
			String nombrePDF = null;

			Comprobante _comprobante = null;

			try {
				_comprobante = LeerXML.ObtenerComprobante(rutaXML);
			} catch (Exception e) {
				Utils.imprimeLog("", e);
			}

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
				} else {
					HashMap<String, String> mapaMensajeComplemento = validarEtiquetasXMLCOMP(con, esquema, _comprobante, versionXML);
					logger.info("mapaMensajeComplemento ------------>" + mapaMensajeComplemento.get("ERROR"));
					if ("FALSE".equalsIgnoreCase(mapaMensajeComplemento.get("ERROR"))) {
						mapaResultado.put("MENSAJE", mapaMensajeComplemento.get("MENSAJE"));
						return mapaResultado;
					}
				}

				// se validan las etiquetas Base
					HashMap<String, String> mapaMensajeBase = validarEtiquetasBase(con, esquema, _comprobante, versionXML);
					logger.info("mapaMensajeBase ------------>" + mapaMensajeBase.get("ERROR"));
					if ("FALSE".equalsIgnoreCase(mapaMensajeBase.get("ERROR"))) {
						mapaResultado.put("MENSAJE", mapaMensajeBase.get("MENSAJE"));
						return mapaResultado;
					}
					
				// termina
				// se obtiene bandera de tipo de validacion
				// Termina
				String rfcFacturaXML = _comprobante.getEmisor().getRfc();
				String razonSocialXML = _comprobante.getEmisor().getNombre();
				// String folioXML = _comprobante.getFolio();
				// String serieXML = _comprobante.getSerie();
				String uuidComplemento = _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
				double totalXML = _comprobante.getTotal();
				String rfcReceptorXML = _comprobante.getReceptor().getRfc();
				LocalDateTime FECHA_TIMBRADOLD = _comprobante.getComplemento().TimbreFiscalDigital.getFechaTimbrado();
				String TIPO_COMPROBANTE = _comprobante.getTipoDeComprobante();
				LocalDateTime FECHA_XMLLD = _comprobante.getFecha();
				logger.info("rfcReceptorXML------------>" + rfcReceptorXML);
				String FECHA_TIMBRADO = null;
				if (FECHA_TIMBRADOLD != null) {
					FECHA_TIMBRADO = FECHA_TIMBRADOLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				}

				String FECHA_XML = null;
				if (FECHA_XMLLD != null) {
					FECHA_XML = FECHA_XMLLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				}

				// Son etiquetas parte de los pagos
				double MONTO_PAGADO = 0;
				String FECHA_PAGO = "";
				String FORMA_PAGO = "";

				nombreXML = "COM_" + rfcFacturaXML + uuidComplemento + ".xml";
				nombrePDF = "COM_" + rfcFacturaXML + uuidComplemento + ".pdf";

				boolean bandArchivo = true;

				String validaCOMPLE = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "RECHAZAR_COMPLE");
				
				logger.info("validaCOMPLE----->" + validaCOMPLE);
				String estadoSAT = "";
				String estatusSAT = "";
				if ("S".equalsIgnoreCase(validaCOMPLE)) {

					String datosSAT[] = UtilsSAT.validaSAT(rfcFacturaXML, rfcReceptorXML, totalXML, uuidComplemento);
					// String datosSAT [] = {"Vigente","S - Comprobante obtenido
					// satisfactoriamente."};
					estadoSAT = datosSAT[0];
					estatusSAT = datosSAT[1];

					logger.info("estado--->" + estadoSAT);
					logger.info("estatusSAT--->" + estatusSAT);

					if (estadoSAT.toUpperCase().indexOf("CANCELADO") > -1) {
						MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
						String mensajeFinal = Utils.getMensajeValidacion(
								Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE29), "<< UUID_COMPLE >>",
								uuidComplemento);
						mapaResultado.put("MENSAJE", mensajeFinal);
						return mapaResultado;
					}
				} // FOLIO_EMPRESA
				String rutaDesXML = null;
				String rutaDesPDF = null;
				File fdesXML = null;
				File fdesPDF = null;
				String directorioPDF = null;
				String directorioXML = null;

				double MONTO_FACTURA = 0;
				double TOTAL_COMPLEMENTO = 0;
				int claveProveedor = 0;

				List<DoctoRelacionado> listaDocto = null;
				DoctoRelacionado docRel = null;

				// JSONObject datosComple = null;
				ComplementosForm compleForm = null;
				ComplementosForm compleTrabajoForm = null;
				//JSONObject datosCompleTrabajo = null;

				boolean esIgualComple = false;
				HashMap<String, String> mapaUUIDXML = new HashMap<>();
				int totComprobantes = _comprobante.getComplemento().Pagos20.getPago().size();
				int totRelacionados = 0;

				
				eliminarComplementariaTrabajo(con, esquema, uuidComplemento); // se eliminan las ordens del complemento.

				logger.info("totComprobantes---->" + totComprobantes);
				for (int x = 0; x < totComprobantes; x++) {
					com.itextpdf.xmltopdf.Pagos20.Pago pago = _comprobante.getComplemento().Pagos20.getPago().get(x);

					LocalDateTime FECHA_PAGOLD = pago.getFechaPago();

					if (FECHA_PAGOLD != null) {
						FECHA_PAGO = FECHA_PAGOLD.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					}

					FORMA_PAGO = pago.getFormaDePagoP();
					logger.info("Monto()---->" + pago.getMonto());
					MONTO_PAGADO = pago.getMonto();

					listaDocto = pago.getDoctoRelacionado();
					totRelacionados = listaDocto.size();

					logger.info("totRelacionados---->" + totRelacionados);
					for (int y = 0; y < totRelacionados; y++) {
						docRel = listaDocto.get(y);
						visorForm = buscarOrden(con, esquema, docRel.getIdDocumento());

						logger.info("UUID ORDEN------------->" + docRel.getIdDocumento());
						compleForm  = buscarUUIDComple(con, esquema, visorForm.getFolioEmpresa());
						logger.info("Folio------------->" + visorForm.getFolioEmpresa());
						logger.info("datosComple------------->" +  Utils.noNulo(compleForm.getUuidComplemento()));

						compleTrabajoForm = buscarUUIDCompleTrabajo(con, esquema,visorForm.getFolioEmpresa());

						String estatusPago = visorForm.getEstatusOrden();
						if ("".equalsIgnoreCase(Utils.noNulo(visorForm.getEstatusOrden()))) { // no encontro registros
							logger.info("*************** NO ENCONTRO INFORMACION *******************");
							MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
							String mensajeFinal = Utils.getMensajeValidacion(
									Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE38), "<< UUID_COMPLE >>",
									docRel.getIdDocumento());
							mapaResultado.put("MENSAJE", mensajeFinal);
							return mapaResultado;

						} else if (!"A4".equalsIgnoreCase(estatusPago)) {
							// return "ESTATUS_NG";
							logger.info("*************** EL ESTATUS ES DIFERENTE DE A4 *******************");
							MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
							String mensajeFinal = Utils.getMensajeValidacion(
									Utils.getMensajeValidacion(Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE36),
											"<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa())),
									"<< UUID_COMPLE >>", docRel.getIdDocumento());
							mapaResultado.put("MENSAJE", mensajeFinal);
							return mapaResultado;
						}

						if (bandArchivo) {
							logger.info("*************** CUMPLIO CON VALIDACIONES DE LA ORDEN *******************" + visorForm.getFolioEmpresa());
							claveProveedor = visorForm.getClaveProveedor();
							directorioXML = esquema + "/PROVEEDORES/" + visorForm.getClaveProveedor() + "/"
									+ nombreXML;
							directorioPDF = esquema + "/PROVEEDORES/" + visorForm.getClaveProveedor() + "/"
									+ nombrePDF;
							bandArchivo = false;
						}

						if (!"".equals(Utils.noNulo(compleTrabajoForm.getUuidComplemento()))) { // si encontro registro en la complementaria
							String estatusComple = compleTrabajoForm.getEstatusComplemento();
							if ("PDT".equalsIgnoreCase(estatusComple)) {
								String uuidComple = compleTrabajoForm.getUuidComplemento();
								eliminarComplementariaTrabajo(con, esquema, uuidComple); // se eliminan las ordens del complemento anterior.
							}
						}

						if (!"".equals(Utils.noNulo(compleForm.getUuidComplemento()))) {
							String uuidComple = compleForm.getUuidComplemento();

							if (uuidComple.equalsIgnoreCase(uuidComplemento)) { // es el mismo XML del proveedor y el que se subio por boveda
								esIgualComple = true;
							} else {
								eliminarComplementariaTrabajo(con, esquema, uuidComplemento); // se eliminan las ordens del complemento.
								logger.info("*************** REGISTRO DUPLICADO *******************");

								logger.info("*************** uuidComple_duplicado *******************" + uuidComple);
								MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
								String mensajeFinal = Utils.getMensajeValidacion(
										Utils.getMensajeValidacion(
												Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE40),
												"<< UUID_COMPLEMENTO >>", uuidComple),
										"<< FOLIO_FACTURA >>", String.valueOf(visorForm.getFolioEmpresa()));
								mapaResultado.put("MENSAJE", mensajeFinal);
								return mapaResultado;
							}
						}

						logger.info("ImpPagado()---->" + docRel.getImpPagado());
						logger.info("MONTO_PAGADO---->" + MONTO_PAGADO);

						if (totRelacionados == 1) {
							MONTO_FACTURA = MONTO_PAGADO;
						} else {
							MONTO_FACTURA = docRel.getImpPagado();
						}

						mapaUUIDXML.put(docRel.getIdDocumento().toLowerCase(), docRel.getIdDocumento().toLowerCase()); // se van agregando los UUID del XML
						logger.info("MONTO_FACTURA---->" + MONTO_FACTURA);
						logger.info("MONTO_PAGADO---->" + MONTO_PAGADO);

						int totGrabar = grabarComplementariaTrabajo(con, esquema, uuidComplemento,
								MONTO_FACTURA, MONTO_PAGADO, FECHA_PAGO, FECHA_TIMBRADO, estatusSAT, nombreXML,
								nombrePDF, usuarioHTTP, docRel.getIdDocumento(), TIPO_COMPROBANTE, FORMA_PAGO);

						if (totGrabar == 1062) { // esta duplicado
							String uuidComple = compleTrabajoForm.getUuidComplemento();
							eliminarComplementariaTrabajo(con, esquema, uuidComple); // se eliminan las ordens del complemento.
							totGrabar = grabarComplementariaTrabajo(con, esquema, uuidComplemento,
									MONTO_FACTURA, MONTO_PAGADO, FECHA_PAGO, FECHA_TIMBRADO, estatusSAT, nombreXML,
									nombrePDF, usuarioHTTP, docRel.getIdDocumento(), TIPO_COMPROBANTE, FORMA_PAGO);
						}

						TOTAL_COMPLEMENTO += MONTO_FACTURA;
					}
				}

				logger.info("TOTAL_COMPLEMENTO---->" + TOTAL_COMPLEMENTO);
				logger.info("totRelacionados---->" + totRelacionados);

				// String mensajeValidacion = cumpleValidaciones(con, esquema, uuidComplemento,
				// FECHA_PAGO, GRAN_TOTAL_COMPLEMENTO, idLenguaje, claveProveedor, FECHA_XML,
				// mapaUUIDXML); // se realizan las validaciones
				// String estatus_Conciliacion = "NG";
				// String cadenaRetorno = null;
				if (totRelacionados == 0) {
					logger.info("*************** NO ENCONTRO NINGUN COMPLEMENTO PARA VALIDAR *******************");
					MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
					String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE43);
					mapaResultado.put("MENSAJE", mensajeFinal);
					return mapaResultado;

				} else {
					DecimalFormat df = new DecimalFormat("#.##");
					String GRAN_TOTAL_COMPLEMENTO = df.format(TOTAL_COMPLEMENTO);
					logger.info("GRAN_TOTAL_COMPLEMENTO---->" + GRAN_TOTAL_COMPLEMENTO);

					mapaResultado = cumpleValidaciones(con, esquema, uuidComplemento, FECHA_PAGO,
							GRAN_TOTAL_COMPLEMENTO, idLenguaje, claveProveedor, FECHA_XML, mapaUUIDXML); // se realizan las validaciones
					String mensajeValidacion = Utils.noNulo(mapaResultado.get("MENSAJE")).toString();
					String estatus_Conciliacion = "NG";
					logger.info("mensajeValidacion---->" + mensajeValidacion);
					if ("OK".equalsIgnoreCase(mensajeValidacion)) {
						if (esIgualComple) { // se eliminan los registros de la complementaria
							eliminarComplementaria(con, esquema, uuidComplemento);
							logger.info("************* ELIMINANDO COMPLEMENTO ************************");
						}

						estatus_Conciliacion = "OK";
						updateComplementariaTrabajo(con, esquema, estatus_Conciliacion, uuidComplemento,
								GRAN_TOTAL_COMPLEMENTO);
						mapaResultado.put("CUMPLE_OK", "OK");
						mapaResultado.put("UUID_COMPLEMENTO", uuidComplemento);
						mapaResultado.put("RAZON_SOCIAL", razonSocialXML);
						mapaResultado.put("ESTADO_SAT", estadoSAT);
						mapaResultado.put("ESTATUS_SAT", estatusSAT);
						// cadenaRetorno = "CUMPLE_OK" + ";" + uuidComplemento + ";"+ razonSocialXML +
						// ";" + estadoSAT + ";" + estatusSAT;

						rutaDesXML = pathRepositorio + directorioXML;
						fdesXML = new File(rutaDesXML);
						UtilsFile.moveFileDirectory(fileXML, fdesXML, true, false, true, bandElimnaArchivos);

						rutaDesPDF = pathRepositorio + directorioPDF;
						fdesPDF = new File(rutaDesPDF);
						logger.info("Ruta Final PDF : " + rutaDesPDF);
						logger.info("Ruta Final XML : " + rutaDesXML);

						UtilsFile.moveFileDirectory(filePDF, fdesPDF, true, false, true, bandElimnaArchivos);

					} else {
						rutaDesXML = pathRepositorio + directorioXML;
						rutaDesPDF = pathRepositorio + directorioPDF;

						fdesXML = new File(rutaDesXML);
						fdesPDF = new File(rutaDesPDF);
						fdesXML.delete();
						fdesPDF.delete();
					}
				}

				return mapaResultado;

			} else if (_comprobante != null && _comprobante.getTipoDeComprobante() != null && !"P".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
				MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
				String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE42);
				mapaResultado.put("MENSAJE", mensajeFinal);
				logger.info("mensajeFinal 1...." + mensajeFinal);
				return mapaResultado;

			} else {
				MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
				String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32);
				mapaResultado.put("MENSAJE", mensajeFinal);
				logger.info("mensajeFinal 2...." + mensajeFinal);
				return mapaResultado;
			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
			MensajesSIAREX mensajeSIAREX = new MensajesSIAREX(idLenguaje);
			String mensajeFinal = Utils.regresaCaracteresNormales(mensajeSIAREX.MENSAJE32);
			mapaResultado.put("MENSAJE", mensajeFinal);
		}
		return mapaResultado;
	}

	
	public String buscarElementoComplemento(String elementoXML, Comprobante _comprobante) {
		String valorXML = null;
		try {
			
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
			}else if ("formaDePagoP".equalsIgnoreCase(elementoXML) || 
						"MonedaP".equalsIgnoreCase(elementoXML) || 
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
	
	
	

	public ArrayList<ComplementosForm> detalleXMLCompleTrabajo(Connection con, String esquema, String uuid_XML)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ComplementosForm compleForm = new ComplementosForm();
        ArrayList<ComplementosForm> listaDetalle = new ArrayList<>();
        try
        {          	
            stmt = con.prepareStatement( ValidacionesComplementoQuerys.getDetalleComplementariaTrabajo(esquema));
            stmt.setString(1, uuid_XML);
            rs = stmt.executeQuery();
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            double totOrden = 0;
            double totComplemento = 0;
			while(rs.next()) 
            {
				totOrden = rs.getDouble(2);
				totComplemento = rs.getDouble(3);
				compleForm.setFolioEmpresa(rs.getLong(1));
				compleForm.setMontoOrden(Utils.noNulo(decimal.format(totOrden)));
				compleForm.setMontoComplemento(Utils.noNulo(decimal.format(totComplemento)));
				compleForm.setFechaPago(UtilsFechas.getFechaddMMMyyyy(rs.getDate(4)));
				compleForm.setUuidOrden(Utils.noNulo(rs.getString(5)));
				if (totOrden == totComplemento ) {
					compleForm.setBandComplemento("true");
				}else {
					compleForm.setBandComplemento("false");
				}
				
				listaDetalle.add(compleForm);
				compleForm = new ComplementosForm();
				
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
	
	
	public int guardarComplementaria(Connection con, String esquema, String uuid_XML)
    {
        PreparedStatement stmt = null;
        int totGrabar = 0;
        try{
        	
            stmt = con.prepareStatement( ValidacionesComplementoQuerys.getUpdateEstatus(esquema));
//            stmt.setString(1, "OK");
            stmt.setString(1, uuid_XML);
            logger.info("stmtL------>"+stmt);
            totGrabar = stmt.executeUpdate();
            
            eliminarComplementariaTrabajo(con, esquema, uuid_XML);
            
        }catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return totGrabar;
    }
	
	public int actualizaEstatusOrden(Connection con, String esquema, String uuidComplemento, String estatusOrden, String usuarioHTTP)
    {
        PreparedStatement stmt = null;
        int totGrabar = 0;
        try{
        	
            stmt = con.prepareStatement( ValidacionesComplementoQuerys.getActualizarEstatusOrdenComplemento(esquema));
//            stmt.setString(1, "OK");
            stmt.setString(1, estatusOrden);
            stmt.setString(2, usuarioHTTP);
            stmt.setString(3, uuidComplemento);
            logger.info("stmt------>"+stmt);
            totGrabar = stmt.executeUpdate();
            
        }catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return totGrabar;
    }
	
	
	public ComplementosForm consultaComplemento(Connection con, String esquema, long folioEmpresa)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ComplementosForm compleForm = new ComplementosForm();
        
        try
        {
            stmt = con.prepareStatement( ValidacionesComplementoQuerys.getConsultaComplemento(esquema));
            stmt.setLong(1, folioEmpresa);
            rs = stmt.executeQuery();
            
			if(rs.next()) {
				compleForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(2))));
				compleForm.setUuidComplemento(Utils.noNulo(rs.getString(3)));
				compleForm.setEstatusComplemento(Utils.noNuloNormal(rs.getString(4)));
				compleForm.setNombreXML(Utils.noNuloNormal(rs.getString(5)));
				compleForm.setNombrePDF(Utils.noNuloNormal(rs.getString(6)));
				compleForm.setClaveProveedor(rs.getInt(7));
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
        return compleForm;
    }
	
	
	public ArrayList<ComplementosForm> listadoComplementoOrdenes(Connection con, String esquema, String uuid_XML)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ComplementosForm compleForm = new ComplementosForm();
        ArrayList<ComplementosForm> listaDetalle = new ArrayList<>();
        try
        {          	
            stmt = con.prepareStatement( ValidacionesComplementoQuerys.getListadoOrdenesComplemento(esquema));
            stmt.setString(1, uuid_XML);
            rs = stmt.executeQuery();
            DecimalFormat decimal = new DecimalFormat("###,###.##");
            double totOrden = 0;
            double totComplemento = 0;
			while(rs.next()) 
            {
				totOrden = rs.getDouble(2);
				totComplemento = rs.getDouble(3);
				compleForm.setFolioEmpresa(rs.getLong(1));
				compleForm.setMontoOrden(Utils.noNulo(decimal.format(totOrden)));
				compleForm.setMontoComplemento(Utils.noNulo(decimal.format(totComplemento)));
				compleForm.setFechaPago(UtilsFechas.getFechaddMMMyyyy(rs.getDate(4)));
				compleForm.setUuidOrden(Utils.noNulo(rs.getString(5)));
				if (totOrden == totComplemento ) {
					compleForm.setBandComplemento("true");
				}else {
					compleForm.setBandComplemento("false");
				}
				
				listaDetalle.add(compleForm);
				compleForm = new ComplementosForm();
				
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
	
	
	public boolean descartaValidacionComplementoOrden(Connection con, String esquema, long folioEmpresa)
    {
        PreparedStatement stmtOrdenes = null;
        ResultSet rsOrdenes = null;
        boolean descartaComplemento = false;
        try{
        	logger.info("*********** VALIDACION SI TIENE COMPLEMENTOS PENDIENTES *************");
			// se obtiene bandera de tipo de validacion
			// Termina
//			  String BLOQUEAR_PROVEEDORES = mapaConf.get("BLOQUEAR_PROVEEDORES");
			  String BLOQUEAR_PROVEEDORES = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "BLOQUEAR_PROVEEDORES");
			  
			  if ("S".equalsIgnoreCase(BLOQUEAR_PROVEEDORES)) {
				  
				  // se valida si la orden de compra esta activada para omitir el complemento
				    
				     stmtOrdenes =  con.prepareStatement( ValidacionesComplementoQuerys.getTieneOmitirComplemento(esquema));
				     stmtOrdenes.setLong(1, folioEmpresa);
				     rsOrdenes = stmtOrdenes.executeQuery();
				     String tiene_omitir = null;
				     if (rsOrdenes.next()) {
				    	 tiene_omitir = Utils.noNulo(rsOrdenes.getString(1));
				     }
				     logger.info("Tiene Omitir Complemento la Orden : "+folioEmpresa + " Con bandera : "+tiene_omitir);
				     if ("S".equals(tiene_omitir)) {
				    	 descartaComplemento = true;
				     }
				  // termina
			  }
        }
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }finally{
	        try{
	        	
	            if(rsOrdenes != null)
	                rsOrdenes.close();
	            rsOrdenes = null;
	            
	            if(stmtOrdenes != null)
	                stmtOrdenes.close();
	            stmtOrdenes = null;
	            
	        }catch(Exception e){
	        	stmtOrdenes = null;
	        }
        }
        logger.info("descartaComplemento--------->"+descartaComplemento);
        return descartaComplemento;
    }
	
	
		public boolean tieneComplementoPendientes(Connection con, String esquema, int claveProveedor) {
	        PreparedStatement stmt = null;
	        
	        ResultSet rs = null;
	        
	        String fechaHoy = Utils.getFechayyyyMMdd();
	        boolean bandTienePendientes = false;
	        try{
	        	logger.info("*********** VALIDACION SI TIENE COMPLEMENTOS PENDIENTES *************");
	        	int dia = Integer.parseInt(fechaHoy.substring(8, 10));
	        	int mes = Integer.parseInt(fechaHoy.substring(5, 7));
	        	int year = Integer.parseInt(fechaHoy.substring(0, 4));
	        	if (mes == 1) {
					year = year - 1;
					mes = 12;
				}else {
					mes = mes - 1;
				}
	        	
	        	String mesC = null;
				if (mes <= 9) {
					mesC = "0" + mes;
				}else {
					mesC = String.valueOf(mes);
				}
				
	        	String FECHA_HASTA = String.valueOf(year) + "-" + mesC + "-" + "31" + " 23:59:59.0"; // fecha Final
	        	
				// se obtiene bandera de tipo de validacion
				// Termina
				  String BLOQUEAR_PROVEEDORES = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "BLOQUEAR_PROVEEDORES");
				  if ("S".equalsIgnoreCase(BLOQUEAR_PROVEEDORES)) {
						    int DIA_APARTIR_COMPLE = Utils.noNuloINT(ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "DIA_APARTIR_COMPLE"));
							logger.info("DIA_APARTIR_COMPLE--------------->"+DIA_APARTIR_COMPLE);
//							String FECHA_APARTIR_COMPLE = UtilsFechas.regresaFecha2(mapaConf.get("FECHA_APARTIR_COMPLE")) + "-01.01.01.000001";
							String FECHA_APARTIR_COMPLE = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "FECHA_APARTIR_COMPLE") + "-01.01.01.000001";
							logger.info("FECHA_APARTIR_COMPLE--------------->"+FECHA_APARTIR_COMPLE);
							
				            stmt = con.prepareStatement( ValidacionesQuerys.getTieneComplementoPendiente(esquema));
				            stmt.setInt(1, claveProveedor);
				            stmt.setString(2, "A4");
				            stmt.setString(3, FECHA_APARTIR_COMPLE);
				            stmt.setString(4, FECHA_HASTA);
				            stmt.setString(5, "N");
				            logger.info("stmt--------------->"+stmt);
				            rs = stmt.executeQuery();
				            String fechaPagoRS = null;
				            while (rs.next()) {
				            	fechaPagoRS = Utils.noNulo(rs.getString(1));
				            	logger.info("fechaPagoRS--------------->"+fechaPagoRS);
				            	int mesRS = Integer.parseInt(fechaPagoRS.substring(5, 7));
				            	if (mesRS == mes) {
				            		if (DIA_APARTIR_COMPLE < dia) { // se compara contra el dia de hoy, si es menor al dia configurado entonces no cumple
				            			bandTienePendientes = true;
				            			break;
				            		}else {
				            			bandTienePendientes = false;
				            		}
				            	}else {
				            		bandTienePendientes = true;
				            		break;
				            	}
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
	        logger.info("bandTienePendientes--------->"+bandTienePendientes);
	        return bandTienePendientes;
	    }
		
		public  HashMap<String, String> validarEtiquetasXMLCOMP(Connection con, String esquema, Comprobante _comprobante, String versionXML){
			EtiquetaCompBean etiquetaCompBean = new EtiquetaCompBean();
			HashMap<String, String> mapaRes = new HashMap<String, String>();

			try{
				mapaRes.put("SUBJECT", "");
				mapaRes.put("MENSAJE", "");
				mapaRes.put("ERROR", "TRUE");

				ArrayList<EtiquetaCompForm> datoXMLSistema = etiquetaCompBean.obtenerConfEtiquetasComp(con, esquema, versionXML, "E");
				// logger.info("datoXMLSistema===>" + datoXMLSistema.size());
				EtiquetaCompForm etiquetaCompForm = null;
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
					 // logger.info("datoXMLSistema===>"+datoXMLSistema);
						for (int x = 0; x < datoXMLSistema.size(); x++){
							etiquetaCompForm = datoXMLSistema.get(x);
							bandEncontro = false;
							logger.info("confxmlForm.getEtiqueta()===>"+etiquetaCompForm.getEtiqueta());
							if (!"".equalsIgnoreCase(etiquetaCompForm.getEtiqueta())){ // si esta en el mapa, entonces se valida porque cumplio
								numEtiqueta++;
								datosValidar = etiquetaCompBean.obtenerDatoValidarComp(con, esquema, etiquetaCompForm.getEtiqueta(), versionXML);
								if ("M".equalsIgnoreCase(etiquetaCompForm.getTipoEtiqueta())){ // si es multiple opcion, viene una propiedad
									//logger.info(" ******* 	Ruta XML ******* "+confxmlForm.getRutaXML());
									//logger.info(" ******* Propiedad ******* "+confxmlForm.getEtiqueta());
									//logger.info("versionXML===>"+versionXML);
									//if("4.0".equalsIgnoreCase(versionXML)) {
										datoXML = buscarElementoComplemento(etiquetaCompForm.getEtiqueta(), _comprobante);
									//}else {
										//logger.info(" ******* confxmlForm.getRutaXML() ******* "+confxmlForm.getRutaXML());
										//logger.info(" ******* confxmlForm.getEtiqueta() ******* "+confxmlForm.getPropiedad());
										//datoXML = XMLXPathManagerBase.getNodeValue(document, confxmlForm.getRutaXML()  +  "@" +  ucFirst( confxmlForm.getPropiedad(), true));
										//logger.info(" ******* datoXML ******* "+datoXML);
									//}
									 String sinSaltoLinea = datoXML.replace("\n", " ");
									 // logger.info(" *******sinSaltoLinea ******* "+sinSaltoLinea);
									 datoXML = sinSaltoLinea;
								}
								logger.info(" ******* Validadando Etiqueta ******* "+datoXML);
								for (int y = 0; y < datosValidar.size(); y++){
									logger.info("dato a Validar---->"+datosValidar.get(y));
									cadSinCaracteres = Utils.replaceCaracteresEspeciales(datoXML);
									//logger.info("cadSinCaracteres de la etiqueta---->"+cadSinCaracteres);
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
									logger.info(" ******* Mensaje  ******* "+etiquetaCompForm.getMensaje());
									mapaRes.put("SUBJECT", etiquetaCompForm.getSubject());
									mapaRes.put("MENSAJE", etiquetaCompForm.getMensaje());
									mapaRes.put("ERROR", "FALSE");
									break;
								}
							}
						}
				 }
			}catch(Exception e){
				Utils.imprimeLog("validarEtiquetasXMLCOMP(): ", e);
			}
			return mapaRes;
	   }
		
		public  HashMap<String, String> validarEtiquetasBase(Connection con, String esquema, Comprobante _comprobante, String versionXML){
			EtiquetaCompBean etiquetaCompBean = new EtiquetaCompBean();
			HashMap<String, String> mapaRes = new HashMap<String, String>();

			try{
				mapaRes.put("SUBJECT", "");
				mapaRes.put("MENSAJE", "");
				mapaRes.put("ERROR", "TRUE");

				ArrayList<EtiquetaCompForm> datoXMLSistema = etiquetaCompBean.obtenerConfEtiquetasComp(con, esquema, versionXML, "B");
				logger.info("datoXMLSistema===>" + datoXMLSistema.size());
				EtiquetaCompForm etiquetaCompForm = null;
				 //ConfxmlForm confxmlFormMapa = null;
				 
				 // ArrayList<String> datosValidar = null;
				// String datoXML = "";
				 boolean bandEncontro = false;
				 if (datoXMLSistema.isEmpty() || datoXMLSistema.size() <= 0){
					 mapaRes.put("ERROR", "TRUE");
				 }else{
					// int totEtiquetas = datoXMLSistema.size();
					  int numEtiqueta = 0;
					 // String cadSinCaracteres = "";
					 logger.info("datoXMLSistema===>"+datoXMLSistema);
						for (int x = 0; x < datoXMLSistema.size(); x++){
							etiquetaCompForm = datoXMLSistema.get(x);
							logger.info("Etiqueta===>"+etiquetaCompForm.getEtiqueta());
							bandEncontro = buscarElementosBase(con, esquema, _comprobante, etiquetaCompForm.getEtiqueta());
							if (bandEncontro) {
								numEtiqueta++;
							}else {
								mapaRes.put("SUBJECT", etiquetaCompForm.getSubject());
								mapaRes.put("MENSAJE", etiquetaCompForm.getMensaje());
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
		
		
		private boolean  buscarElementosBase(Connection con, String esquema, Comprobante _comprobante, String etiquetaBase) {
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
					if (Utils.noNulo(_comprobante.getLugarExpedicion()).equalsIgnoreCase(provForm.getRazonSocial())) {
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
					EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquema);
					if (Utils.noNulo(_comprobante.getReceptor().getRfc()).equalsIgnoreCase(empresaForm.getRfc())) {
						isEncontro = true;
					}
				}else if ("nombreReceptor".equalsIgnoreCase(etiquetaBase)) {
					AccesoBean accesoBean = new AccesoBean();
					EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquema);
					if (Utils.noNulo(_comprobante.getReceptor().getNombre()).equalsIgnoreCase(empresaForm.getNombreLargo())) {
						isEncontro = true;
					}
				}else if ("domicilioFiscalReceptor".equalsIgnoreCase(etiquetaBase)) {
					AccesoBean accesoBean = new AccesoBean();
					EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquema);
					if (_comprobante.getReceptor().getDomicilioFiscalReceptor() != null) {
						if (Integer.parseInt(_comprobante.getReceptor().getDomicilioFiscalReceptor()) == empresaForm.getCodigoPostal()) {
							isEncontro = true;
						}
						
					}
				}else if ("regimenFiscalReceptor".equalsIgnoreCase(etiquetaBase)) {
					AccesoBean accesoBean = new AccesoBean();
					EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(esquema);
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
		
		
}
