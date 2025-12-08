package com.siarex247.cumplimientoFiscal.Boveda;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.CreaPDF;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.BovedaNomina.BovedaNominaBean;
import com.siarex247.cumplimientoFiscal.ExportarXML.ExportarXMLBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.ConvierteEXCEL;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.LeerDatosXML;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;
import com.siarex247.validaciones.ValidacionesComplemento;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class BovedaAction extends BovedaSupport {

	private static final long serialVersionUID = -112258213613275729L;
	private InputStream inputStream;
	private String reportFile;

	public List<File> filesXMLProceso = new ArrayList<>();

	public String detalleBoveda() {
		Connection con = null;
		ResultadoConexion rc = null;

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		BovedaBean bovedaBean = new BovedaBean();

		try {
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {

				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				String fechaInicial = Utils.noNulo(getFechaInicial());
				String fechaFinal = Utils.noNulo(getFechaFinal());
				String dateOperator = null;
				if ("".equalsIgnoreCase(fechaInicial)) {
					fechaFinal = UtilsFechas.getFechayyyyMMdd();
					fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
					dateOperator = "bt";
				} else {
					dateOperator = getDateOperator();
				}
				dateOperator = "bt";

				// boolean pasoValidacion = UtilsFechas.validaFecha(fechaInicial, fechaInicial,
				// 365);
				HttpServletRequest req = ServletActionContext.getRequest();
				String searchGlobal = Utils.noNulo(req.getParameter("search[value]"));
				String drawStr = Utils.noNulo(req.getParameter("draw"));
				String startStr = Utils.noNulo(req.getParameter("start"));
				String lenStr = Utils.noNulo(req.getParameter("length"));

				/*
				 * logger.info(String.format(
				 * "🧩 [BOVEDA/ACTION] draw=%s start=%s length=%s search[value]=%s", drawStr,
				 * startStr, lenStr, searchGlobal ));
				 * 
				 * logger.info(String.format(
				 * "🧩 [BOVEDA/ACTION] filtros texto → rfc=%s (op=%s), razonSocial=%s (op=%s), serie=%s (op=%s), tipo=%s (op=%s), uuid=%s (op=%s)"
				 * , Utils.noNulo(getRfc()), Utils.noNulo(getRfcOperator()),
				 * Utils.noNulo(getRazonSocial()), Utils.noNulo(getRazonOperator()),
				 * Utils.noNulo(getSerie()), Utils.noNulo(getSerieOperator()),
				 * Utils.noNulo(getTipoComprobante()), Utils.noNulo(getTipoOperator()),
				 * Utils.noNulo(getUuid()), Utils.noNulo(getUuidOperator()) ));
				 * 
				 * logger.info(String.format(
				 * "🧩 [BOVEDA/ACTION] fechas → op=%s, %s..%s  | cabecera=%s..%s",
				 * Utils.noNulo(getDateOperator()), Utils.noNulo(getDateV1()),
				 * Utils.noNulo(getDateV2()), Utils.noNulo(getFechaInicial()),
				 * Utils.noNulo(getFechaFinal()) ));
				 * 
				 * logger.info(String.format(
				 * "🧩 [BOVEDA/ACTION] numéricos → folio[%s]=%s..%s, total[%s]=%s..%s, sub[%s]=%s..%s, iva[%s]=%s..%s, ivaRet[%s]=%s..%s, isr[%s]=%s..%s, impLoc[%s]=%s..%s"
				 * , Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()),
				 * Utils.noNulo(getFolioV2()), Utils.noNulo(getTotalOperator()),
				 * Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
				 * Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()),
				 * Utils.noNulo(getSubV2()), Utils.noNulo(getIvaOperator()),
				 * Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
				 * Utils.noNulo(getIvaRetOperator()),Utils.noNulo(getIvaRetV1()),Utils.noNulo(
				 * getIvaRetV2()), Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()),
				 * Utils.noNulo(getIsrV2()),
				 * Utils.noNulo(getImpLocOperator()),Utils.noNulo(getImpLocV1()),Utils.noNulo(
				 * getImpLocV2()) )); u
				 */

				BovedaModel bovedaModel = new BovedaModel();
				ArrayList<BovedaForm> listaDetalle = bovedaBean.detalleBoveda(con, session.getEsquemaEmpresa(),
						Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()),
						Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()),
						Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), getStart(), getLength(), false, // isExcel

						// ===== operadores texto =====
						Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()),
						Utils.noNulo(getSerieOperator()), Utils.noNulo(getTipoOperator()),
						Utils.noNulo(getUuidOperator()),

						// ===== FECHA con operadores =====
						Utils.noNulo(dateOperator), // eq, ne, lt, gt, le, ge, bt
						Utils.noNulo(getDateV1()), // YYYY-MM-DD
						Utils.noNulo(getDateV2()), // YYYY-MM-DD (solo para bt)

						// ===== operadores/valores numéricos =====
						Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
						Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
						Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
						Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
						Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
						Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),

						// ===== impuestos locales =====
						Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

				// int totalRegistro = bovedaBean.totalRegistros(con,
				// session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
				// Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()),
				// Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial),
				// Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()),
				// Utils.noNulo(fechaFinal), Utils.noNulo(getRfcOperator())) ;
				int totalRegistro = bovedaBean.totalRegistros(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
						Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
						Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()),
						Utils.noNulo(fechaFinal),

						// operadores de texto
						Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()),
						Utils.noNulo(getSerieOperator()), Utils.noNulo(getTipoOperator()),
						Utils.noNulo(getUuidOperator()),

						// FECHA con operadores (nuevo bloque)
						Utils.noNulo(dateOperator), // eq, ne, lt, gt, le, ge, bt
						Utils.noNulo(getDateV1()), // YYYY-MM-DD
						Utils.noNulo(getDateV2()), // YYYY-MM-DD (solo para bt)

						// filtros numéricos
						Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
						Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
						Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
						Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
						Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
						Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
						Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

				bovedaModel.setData(listaDetalle);
				bovedaModel.setRecordsTotal(totalRegistro);
				bovedaModel.setRecordsFiltered(totalRegistro);
				bovedaModel.setDraw(getDraw());

				org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
				out.print(json);
				out.flush();
				out.close();
				// logger.info("json==>"+json);
			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String obtenerTotales() {
		Connection con = null;
		ResultadoConexion rc = null;

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		BovedaBean bovedaBean = new BovedaBean();

		try {
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {

				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				int draw = getDraw();
				if (draw == 0) {
					draw = 1;
				}

				String fechaInicial = Utils.noNulo(getFechaInicial());
				String fechaFinal = Utils.noNulo(getFechaFinal());
				String dateOperator = null;
				if ("".equalsIgnoreCase(fechaInicial)) {
					fechaFinal = UtilsFechas.getFechayyyyMMdd();
					fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
					dateOperator = "bt";
				} else {
					dateOperator = getDateOperator();
				}
				dateOperator = "bt";

				BovedaModel bovedaModel = new BovedaModel();
				// int totalRegistro = bovedaBean.totalRegistros(con,
				// session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
				// Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()),
				// Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial),
				// Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()),
				// Utils.noNulo(fechaFinal), Utils.noNulo(getRfcOperator()));
				int totalRegistro = bovedaBean.totalRegistros(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
						Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
						Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()),
						Utils.noNulo(fechaFinal),

						// operadores de texto
						Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()),
						Utils.noNulo(getSerieOperator()), Utils.noNulo(getTipoOperator()),
						Utils.noNulo(getUuidOperator()),

						// FECHA con operadores (nuevo bloque)
						Utils.noNulo(dateOperator), // eq, ne, lt, gt, le, ge, bt
						Utils.noNulo(getDateV1()), // YYYY-MM-DD
						Utils.noNulo(getDateV2()), // YYYY-MM-DD (solo para bt)

						// filtros numéricos
						Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
						Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
						Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
						Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
						Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
						Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
						Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

				bovedaModel.setRecordsTotal(totalRegistro);
				bovedaModel.setCodError("000");

				int TOT_REGISTROS_DESCARGAR = Utils.noNuloINT(
						ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "TOT_REGISTROS_DESCARGAR"));
				if (totalRegistro > TOT_REGISTROS_DESCARGAR) {
					bovedaModel.setCodError("001");
				}

				org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
				out.print(json);
				out.flush();
				out.close();
			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String consultarFechas() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				BovedaForm bovedaForm = new BovedaForm();
				String fechaFinal = UtilsFechas.getFechayyyyMMdd();
				String fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);

				bovedaForm.setFechaInicial(fechaInicial);
				bovedaForm.setFechaFinal(fechaFinal);

				org.json.JSONObject json = new org.json.JSONObject(bovedaForm);
				out.print(json);
				out.flush();
				out.close();

			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String validarFechas() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();

		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				BovedaModel bovedaModel = new BovedaModel();

				boolean isMayor = Utils.compararFechaMayor(Utils.noNulo(getFechaInicial()),
						Utils.noNulo(getFechaFinal()));
				logger.info("isMayor====>" + isMayor);

				if (!isMayor) {
					bovedaModel.setCodError("001");
					bovedaModel.setMensajeError("La fecha inicial no puede ser mayor que la fecha final.");
				} else {
					boolean bandFechas = UtilsFechas.validaFecha(Utils.noNulo(getFechaInicial()),
							Utils.noNulo(getFechaFinal()), 366);
					if (bandFechas) {
						bovedaModel.setCodError("001");
						bovedaModel.setMensajeError("Debe especificar rango de fechas menor a 1 año.");
					} else {
						bovedaModel.setCodError("000");
						bovedaModel.setMensajeError("Filtro seleccionado correctamente.");
					}
				}

				org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
				out.print(json);
				out.flush();
				out.close();
			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String consultarFechaMinima() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		BovedaBean bovedaBean = new BovedaBean();
		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				BovedaForm bovedaForm = new BovedaForm();
				String fechaMinima = bovedaBean.consultarFechaMinima(con, rc.getEsquema());
				String fechaFinal = UtilsFechas.getFechayyyyMMdd();

				bovedaForm.setFechaInicial(fechaMinima.substring(0, 10));
				bovedaForm.setFechaFinal(fechaFinal);

				org.json.JSONObject json = new org.json.JSONObject(bovedaForm);
				out.print(json);
				out.flush();
				out.close();

			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String eliminarBoveda() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		BovedaBean bovedaBean = new BovedaBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator
						+ "BOVEDA" + File.separator;

				int eliminado = bovedaBean.eliminaBoveda(con, session.getEsquemaEmpresa(), getIdRegistro(), rutaBoveda);
				Map<String, Object> json = new HashMap<String, Object>();
				if (eliminado == 1) {
					json.put("codError", "000");
					json.put("mensajeError", "El registro se ha guardado satisfactoriamente.");
				} else {
					json.put("codError", "001");
					json.put("mensajeError", "Error al eliminar el registro, consulte a su administrador.");
				}
				// json.put("ESTATUS", eliminado > 0 ? "000" : "001");
				out.print(JSONObject.toJSONString(json));
				out.flush();
				out.close();

			}
		} catch (Exception e) {
			Utils.imprimeLog("elimnarBoveda(): ", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String iniciaCargaXML() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Connection con = null;
		ResultadoConexion rc = null;
		PrintWriter out = null;

		logger.info("Tiempo Inicial : " + System.currentTimeMillis());
		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {

				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				out = response.getWriter();

				Collection<Part> colectionPart = request.getParts();
				ArrayList<File> listFilesXML = com.siarex247.utils.UtilsFile.getFilesFromPart(colectionPart);

				BovedaBean bovedaBean = new BovedaBean();

				// LeerXML crea = null;
				Integer arrResultado[] = { 0, 0, 0, 0, 0 };
				for (File fileHTTP : listFilesXML) {
					// arrResultado[0] = Exitoso;
					// arrResultado[1] = Duplicado;
					// arrResultado[2] = Error en RFC;
					// arrResultado[3] = Error en XML;
					// arrResultado[4] = Nomina;
					bovedaBean.procesarXmlBoveda(con, rc.getEsquema(), session.getEsquemaEmpresa(), fileHTTP,
							arrResultado, true);
				}

				// se actualiza el metadata para señalar los UUID encotnrados
				bovedaBean.actualizarEncontradosBoveda(con, session.getEsquemaEmpresa());

				// se actualiza el metadata para señalar los UUID encotnrados
				try {
					if (con != null) {
						con.close();
					}
					con = null;
				} catch (Exception e) {
					con = null;
				}
				// logger.info("numFilesNG---------------------------->"+numFilesNG);
				int numFiles = arrResultado[0] + arrResultado[1] + arrResultado[2] + arrResultado[3];
				int numFilesOK = arrResultado[0];
				int numFilesNG = arrResultado[1];
				int numFilesRFC = arrResultado[2];
				int numFilesXML = arrResultado[3];
				int numFilesNomina = arrResultado[4];

				Map<String, Object> jsonRetorno = new HashMap<String, Object>();
				if (numFilesNG > 0 || numFilesRFC > 0 || numFilesXML > 0) {
					jsonRetorno.put("ESTATUS", "OK_CON_ERROR");
					jsonRetorno.put("MENSAJE",
							"Total de Archivos : " + numFiles + "<br> Archivos Exitosos : " + numFilesOK
									+ "<br> Archivos Duplicados : " + numFilesNG + "<br> Archivos con error en RFC : "
									+ numFilesRFC + "<br> Archivos con error en XML : " + numFilesXML
									+ "<br> Archivos de Nomina : " + numFilesNomina);

				} else {
					jsonRetorno.put("ESTATUS", "OK");
					jsonRetorno.put("MENSAJE",
							"Total de Archivos : " + numFiles + "<br> Archivos Exitosos : " + numFilesOK
									+ "<br> Archivos Duplicados : " + numFilesNG + "<br> Archivos con error en RFC : "
									+ numFilesRFC + "<br> Archivos con error en XML : " + numFilesXML
									+ "<br> Archivos de Nomina : " + numFilesNomina);
				}
				out.print(JSONObject.toJSONString(jsonRetorno));
				out.flush();
				out.close();

				logger.info("JsonRespuesta===>" + JSONObject.toJSONString(jsonRetorno));

			}

			logger.info("Tiempo Final.............." + System.currentTimeMillis());

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return null;
	}

	public String convXMLAExcel() {
		// HSSFWorkbook libro = new HSSFWorkbook();
		// HSSFSheet hoja1 = libro.createSheet("Complementos Recibidas");
		// HSSFSheet hoja2 = libro.createSheet("Ingresos Recibidas");
		// HSSFSheet hoja3 = libro.createSheet("Egresos Recibidas");

		SXSSFWorkbook libro = new SXSSFWorkbook(100); // Keep 100 rows in memory
		SXSSFSheet hoja1 = libro.createSheet("Complementos Recibidas");
		SXSSFSheet hoja2 = libro.createSheet("Ingresos Recibidas");
		SXSSFSheet hoja3 = libro.createSheet("Egresos Recibidas");

		HttpServletRequest request = ServletActionContext.getRequest();
		// HttpServletResponse response = ServletActionContext.getResponse();

		Connection con = null;
		ResultadoConexion rc = null;

		Connection conSAT = null;
		ResultadoConexion rcSAT = null;

		// ArrayList<XMLForm> datosPForm = new ArrayList<XMLForm>();
		// ArrayList<XMLForm> datosIForm = new ArrayList<XMLForm>();
		// XMLForm cargasXMLForm = new XMLForm();
		// LeerFactura factura = new LeerFactura();
		int x = 0;

		try {
			SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())) {
				libro.close();
				return Action.LOGIN;
			} else {
				// PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				rcSAT = getConnectionSAT();
				conSAT = rcSAT.getCon();

				String fechaInicial = Utils.noNulo(getFechaInicial());
				String fechaFinal = Utils.noNulo(getFechaFinal());
				String dateOperator = null;
				if ("".equalsIgnoreCase(fechaInicial)) {
					fechaFinal = UtilsFechas.getFechayyyyMMdd();
					fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
					dateOperator = "bt";
				} else {
					dateOperator = getDateOperator();
				}
				dateOperator = "bt";

				/*
				 * ArrayList<BovedaForm> datosBovedaP = new BovedaBean().detalleBovedaEXCEL(con,
				 * session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
				 * Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()),
				 * Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), "P",
				 * Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), getIdRegistro());
				 * ArrayList<BovedaForm> datosBovedaI = new BovedaBean().detalleBovedaEXCEL(con,
				 * session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
				 * Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()),
				 * Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), "I",
				 * Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), getIdRegistro());
				 * ArrayList<BovedaForm> datosBovedaE = new BovedaBean().detalleBovedaEXCEL(con,
				 * session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
				 * Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()),
				 * Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), "E",
				 * Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), getIdRegistro());
				 */
				ArrayList<BovedaForm> datosBovedaP = new BovedaBean().detalleBovedaEXCEL(con,
						session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()),
						Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), "P",
						Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), Utils.noNulo(getIdRegistro()),

						// ===== operadores de texto =====
						Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()),
						Utils.noNulo(getSerieOperator()), Utils.noNulo(getTipoOperator()),
						Utils.noNulo(getUuidOperator()),

						// ===== FECHA con operadores =====
						Utils.noNulo(dateOperator), Utils.noNulo(getDateV1()), Utils.noNulo(getDateV2()),

						// ===== numéricos =====
						Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
						Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
						Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
						Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
						Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
						Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
						Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

				ArrayList<BovedaForm> datosBovedaI = new BovedaBean().detalleBovedaEXCEL(con,
						session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()),
						Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), "I",
						Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), Utils.noNulo(getIdRegistro()),

						Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()),
						Utils.noNulo(getSerieOperator()), Utils.noNulo(getTipoOperator()),
						Utils.noNulo(getUuidOperator()),

						Utils.noNulo(dateOperator), Utils.noNulo(getDateV1()), Utils.noNulo(getDateV2()),

						Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
						Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
						Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
						Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
						Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
						Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
						Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

				ArrayList<BovedaForm> datosBovedaE = new BovedaBean().detalleBovedaEXCEL(con,
						session.getEsquemaEmpresa(), Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()),
						Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), "E",
						Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), Utils.noNulo(getIdRegistro()),

						Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()),
						Utils.noNulo(getSerieOperator()), Utils.noNulo(getTipoOperator()),
						Utils.noNulo(getUuidOperator()),

						Utils.noNulo(dateOperator), Utils.noNulo(getDateV1()), Utils.noNulo(getDateV2()),

						Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
						Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
						Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
						Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
						Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
						Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
						Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

				ExtraerXMLBean extraerBean = new ExtraerXMLBean();

				LeerDatosXML leerXML = new LeerDatosXML();
				ArrayList<Comprobante> listaComplementos = leerXML.leerElementos(datosBovedaP, session.getEsquemaEmpresa());
				ArrayList<Comprobante> listaFacturas = leerXML.leerElementos(datosBovedaI, session.getEsquemaEmpresa());
				ArrayList<Comprobante> listaNotaC = leerXML.leerElementos(datosBovedaE, session.getEsquemaEmpresa());

				extraerBean.generarExcelComplemento(conSAT, rcSAT.getEsquema(), hoja1, listaComplementos, libro, false,null);
				extraerBean.generarExcelFacturas(conSAT, rcSAT.getEsquema(), hoja2, listaFacturas, libro, false, null);
				extraerBean.generarExcelNotaCredito(conSAT, rcSAT.getEsquema(), hoja3, listaNotaC, libro, false, null);

				try {
					AccesoBean accesoBean = new AccesoBean();
					EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());

					String fechaHoy = UtilsFechas.getFechaActualNumero();
					reportFile = empresasForm.getRfc() + "_InformacionCompleta_" + fechaHoy.substring(0, 8) + "_"
							+ fechaHoy.substring(8, 12) + ".xlsx";
					// String destinoArchivo =
					// request.getSession().getServletContext().getRealPath("/") + "files" +
					// File.separator + reportFile;

					logger.info("Generando Reporte de Salido....." + reportFile);
					// extraerBean.generarExcel(libro, hoja1);

					ByteArrayOutputStream boas = new ByteArrayOutputStream();
					libro.write(boas);
					setInputStream(new ByteArrayInputStream(boas.toByteArray()));
					boas.close();
					/*
					 * File fileXLS = new File(destinoArchivo); if (fileXLS.exists()) {
					 * fileXLS.delete(); }
					 * 
					 * FileOutputStream elFichero = new FileOutputStream(destinoArchivo);
					 * libro.write(elFichero);
					 * 
					 * elFichero.close();
					 */

					/*
					 * BovedaModel bovedaModel = new BovedaModel(); bovedaModel.setCodError("000");
					 * bovedaModel.setMensajeError(reportFile);
					 * 
					 * 
					 * org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
					 * out.print(json); out.flush(); out.close();
					 */

				} catch (Exception e) {
					Utils.imprimeLog("convXMLAExcel(IOE): ", e);
				} finally {
					try {
						if (libro != null) {
							libro.close();
						}
						libro = null;
						if (con != null) {
							con.close();
						}
						con = null;
						if (conSAT != null) {
							conSAT.close();
						}
						conSAT = null;
					} catch (Exception e) {
						con = null;
						conSAT = null;
					}
				}

			}
		} catch (Exception e) {
			Utils.imprimeLog("convXMLAExcel(): ", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
				if (conSAT != null) {
					conSAT.close();
				}
				conSAT = null;
			} catch (Exception e) {
				con = null;
				conSAT = null;
			}
		}
		return SUCCESS;
	}

	public String procesaDescargarCFDI() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		Connection con = null;
		ResultadoConexion rc = null;
		try {
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			rc = getConnection(session.getEsquemaEmpresa());
			con = rc.getCon();

			logger.info("*********** EXPORTANDO ARCHIVOS DE NOMINA ****************");
			String emailNotificacion = getEmailNotificacion();
			logger.info("*********** emailNotificacion****************" + emailNotificacion);
			AccesoBean accesoBean = new AccesoBean();
			EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());

			UsuariosBean usuariosBean = new UsuariosBean();
			UsuariosForm usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));

			Thread procDescargaRecibidos = new Thread(new Runnable() {
				public void run() {
					// exportBovedaZIP();

					procesoDescargaZIP(session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
							Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
							Utils.noNulo(getFechaInicial()), Utils.noNulo(getTipoComprobante()),
							Utils.noNulo(getUuid()), Utils.noNulo(getFechaFinal()), Utils.noNulo(getIdRegistro()),
							emailNotificacion, empresasForm.getRfc(), empresasForm.getEmailDominio(),
							empresasForm.getPwdCorreo(), getUsuario(request), usuariosForm.getNombreCompleto());
				}
			});
			procDescargaRecibidos.setName("procDescargaRecibidos");
			procDescargaRecibidos.start();

			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");

			LenguajeBean lenguajeBean = LenguajeBean.instance();
			HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "BOVEDA");
			BovedaModel bovedaModel = new BovedaModel();
			bovedaModel.setCodError("000");
			if ("".equalsIgnoreCase(mapaLenguaje.get("ETQ57"))) {
				bovedaModel.setMensajeError(
						"El proceso de descarga de facturas se ha iniciado satisfactoriamente. En unos momentos recibirá un correo para descargar el archivo de facturas.");
			} else {
				bovedaModel.setMensajeError(mapaLenguaje.get("ETQ57"));
			}

			org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
			out.print(json);
			out.flush();
			out.close();
		} catch (Exception e) {
			Utils.imprimeLog("procesaDescargarCFDI ", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	private String procesoDescargaZIP(String esquemaEmpresa, String rfc, String razonSocial, String folio, String serie,
			String fechaInicial, String tipoComprobante, String uuid, String fechaFinal, String idRegistro,
			String emailNotificacion, String rfcEmpresa, String emailDominio, String pwdCorreo, String usuarioHTTP,
			String nombreCompleto) {
		Connection con = null;
		ResultadoConexion rc = null;

		BovedaBean bovedaBean = new BovedaBean();
		String directorioXML = null;
		// ArrayList<String> alFiles = new ArrayList<String>();

		String logo = "logoToyota.png";
		String bandLogo = "S";
		String rutaHTML = null;

		Map<String, Object> mapaRes = null;
		JSONArray jsonArray = null;
		try {
			// PrintWriter out = response.getWriter();
			rc = getConnection(esquemaEmpresa);
			con = rc.getCon();

			rutaHTML = UtilsPATH.RUTA_PUBLIC_HTML + esquemaEmpresa + File.separator + "TEMP_PDF" + File.separator;
			File fileTemp = new File(rutaHTML);
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}
			String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquemaEmpresa + "/BOVEDA/";

			String pathPDF = "";

			String dateOperator = null;
			if ("".equalsIgnoreCase(fechaInicial)) {
				dateOperator = "bt";
			} else {
				dateOperator = getDateOperator();
			}

			dateOperator = "bt";
			// mapaRes = bovedaBean.detalleBovedaZIP(con, esquemaEmpresa, rfc, razonSocial,
			// folio, serie, fechaInicial, tipoComprobante, uuid, fechaFinal, idRegistro);
			mapaRes = bovedaBean.detalleBovedaZIP(con, esquemaEmpresa, Utils.noNulo(rfc), Utils.noNulo(razonSocial),
					Utils.noNulo(folio), Utils.noNulo(serie), Utils.noNulo(fechaInicial), Utils.noNulo(tipoComprobante),
					Utils.noNulo(uuid), Utils.noNulo(fechaFinal), Utils.noNulo(idRegistro), // cadRegistros:
																							// "uuid1;uuid2;..."

					// ===== operadores de texto =====
					Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()), Utils.noNulo(getSerieOperator()),
					Utils.noNulo(getTipoOperator()), Utils.noNulo(getUuidOperator()),

					// ===== fecha con operadores =====
					Utils.noNulo(dateOperator), // eq, ne, lt, gt, le, ge, bt
					Utils.noNulo(getDateV1()), // YYYY-MM-DD
					Utils.noNulo(getDateV2()), // YYYY-MM-DD

					// ===== numéricos =====
					Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
					Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
					Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
					Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
					Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
					Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
					Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

			jsonArray = (JSONArray) mapaRes.get("detalle");
			JSONObject jsonobj = null;

			bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA");

			if (bandLogo.equalsIgnoreCase("S")) {
				logo = "logoFactura.png";
			} else {
				logo = "logoVacio.png";
			}

			Date fecha = new Date();
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmm");
			String fechaActual = formatDate.format(fecha);

			String rutaArchivos = "/REPOSITORIOS/" + esquemaEmpresa + "/EXPORTAR/" + fechaActual + "/" + rfcEmpresa
					+ "_RECIBIDOS";

			String rutaDestinoXML = null;
			String rutaDestinoPDF = null;
			final String EXTENCION_XML = ".xml";
			final String EXTENCION_PDF = ".pdf";

			for (int x = 0; x < jsonArray.size(); x++) {
				jsonobj = (JSONObject) jsonArray.get(x);
				if (!"".equals(jsonobj.get("XML"))) {
					directorioXML = rutaBoveda + jsonobj.get("XML");

					File fileXML = new File(directorioXML); // XML
					rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + jsonobj.get("UUID")
							+ EXTENCION_XML;
					File fileXMLDest = new File(rutaDestinoXML); // XML
					UtilsFile.moveFileDirectory(fileXML, fileXMLDest, true, true, true, false);
					fileXML = null;
					fileXMLDest = null;
					pathPDF = rutaHTML + jsonobj.get("UUID") + EXTENCION_PDF;
					if (!pathPDF.equals("")) {
						try {
							new CreaPDF().GenerarByXML(directorioXML, pathPDF, (rutaBoveda + "/" + logo));
							File filePDF = new File(pathPDF); // PDF Complemento
							rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + jsonobj.get("UUID")
									+ EXTENCION_PDF;
							File filePDFDest = new File(rutaDestinoPDF); // PDF
							UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
							filePDF = null;
							filePDFDest = null;
						} catch (Exception e) {

						}
					}
				}
			}

			String rutaRep = "/REPOSITORIOS/" + esquemaEmpresa + "/EXPORTAR/" + fechaActual + File.separator
					+ rfcEmpresa + "_RECIBIDOS" + ".zip";
			String rutaEliminar = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + "/REPOSITORIOS/" + esquemaEmpresa + "/EXPORTAR/"
					+ fechaActual + File.separator + rfcEmpresa + "_RECIBIDOS";
			String rutaZippear = "/REPOSITORIOS/" + esquemaEmpresa + "/EXPORTAR/" + fechaActual;
			logger.info("*********** iniciando comprension de archivos **************************");
			ZipFiles zipFiles = new ZipFiles();
			String rutaZIP = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaZippear;
			String zipDirName = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaRep;
			File dir = new File(rutaZIP); // origen
			zipFiles.zipDirectory(dir, zipDirName); // se genera el archivo .zip
			logger.info("*********** fin de comprencion de archivos **************************");
			boolean bandZIP = true;
			int idArchivo = 0;
			if (bandZIP) {
				BovedaNominaBean bovNomina = new BovedaNominaBean();
				idArchivo = bovNomina.grabarDescarga(esquemaEmpresa, usuarioHTTP, rutaRep);
			}

			// String dominio = Utils.getInfoCorreo("DOM");
			String dominio = UtilsPATH.DOMINIO_PRINCIPAL;
			String urlZIP = "https://" + dominio + "/siarexDescarga/descargarSiarex.jsp?idArchivo=" + idArchivo;
			// String urlZIP =
			// "http://localhost:8080/siarexDescarga/descargarSiarex.jsp?idArchivo="+idArchivo;

			logger.info("*********** emailUsuario **************************" + emailNotificacion);
			String listaCorreos[] = { emailNotificacion };
			String sbHTML = UtilsHTML.generaHTMLExport(nombreCompleto, urlZIP, dominio);
			logger.info("*********** sbHTML **************************" + sbHTML);
			EnviaCorreoGrid.enviarCorreo(null, sbHTML, false, listaCorreos, null, "Descarga de XML Recibidos",
					emailDominio, pwdCorreo);

			logger.info("Eliminando Directorio....." + rutaEliminar);
			File fileEliminar = new File(rutaEliminar);
			FileUtils.deleteDirectory(fileEliminar);

		} catch (Exception e) {
			Utils.imprimeLog("procesoDescargaZIP(): ", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String exportBovedaZIP() {
		Connection con = null;
		ResultadoConexion rc = null;

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		BovedaBean bovedaBean = new BovedaBean();

		String rutaHTML = null;
		try {
			Map<String, Object> mapaRes = null;
			JSONArray jsonArray = null;
			SiarexSession session = ObtenerSession.getSession(request);
			PrintWriter out = response.getWriter();

			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {

				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				rutaHTML = UtilsPATH.RUTA_PUBLIC_HTML + session.getEsquemaEmpresa() + File.separator + "TEMP_PDF"
						+ File.separator;
				File fileTemp = new File(rutaHTML);
				if (!fileTemp.exists()) {
					fileTemp.mkdirs();
				}
				// String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS
				// +session.getEsquemaEmpresa() + "/BOVEDA/";

				String idRegistro = getIdRegistro();
				String fechaInicial = Utils.noNulo(getFechaInicial());
				String fechaFinal = Utils.noNulo(getFechaFinal());
				String dateOperator = null;
				if ("".equalsIgnoreCase(fechaInicial)) {
					fechaFinal = UtilsFechas.getFechayyyyMMdd();
					fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
					dateOperator = "bt";
				} else {
					dateOperator = getDateOperator();
				}
				dateOperator = "bt";

				// mapaRes = bovedaBean.detalleBovedaZIP(con, session.getEsquemaEmpresa(),
				// Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()),
				// Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
				// Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()),
				// Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), idRegistro);
				mapaRes = bovedaBean.detalleBovedaZIP(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
						Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
						Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()),
						Utils.noNulo(fechaFinal), idRegistro, // cadRegistros: "uuid1;uuid2;..."

						// ===== operadores de texto =====
						Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()),
						Utils.noNulo(getSerieOperator()), Utils.noNulo(getTipoOperator()),
						Utils.noNulo(getUuidOperator()),

						// ===== fecha con operadores =====
						Utils.noNulo(dateOperator), Utils.noNulo(getDateV1()), Utils.noNulo(getDateV2()),

						// ===== numéricos =====
						Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
						Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
						Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
						Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
						Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
						Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
						Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));
				jsonArray = (JSONArray) mapaRes.get("detalle");
				JSONObject jsonobj = null;

				/*
				 * ConfigAdicionalesBean configAdicionalesBean = new ConfigAdicionalesBean();
				 * HashMap<String, String> mapaConfig =
				 * configAdicionalesBean.obtenerConfiguraciones(con,
				 * session.getEsquemaEmpresa()); bandLogo =
				 * Utils.noNulo(mapaConfig.get("BANDERA_LOGO_TOYOTA"));
				 * if(bandLogo.equalsIgnoreCase("S")) { logo = "logoFactura.png"; } else { logo
				 * = "logoVacio.png"; }
				 */

				ArrayList<String> listaTXT = new ArrayList<String>();

				String fecA = UtilsFechas.getFechaActualNumero();
				String nombreReporte = "descargaCFDI_Boveda_" + "_" + session.getEsquemaEmpresa() + "_" + fecA + ".txt";
				String pathCSV = UtilsPATH.RUTA_PUBLIC_HTML + session.getEsquemaEmpresa() + File.separator + "TEMP_PDF"
						+ File.separator + nombreReporte;

				for (int x = 0; x < jsonArray.size(); x++) {
					jsonobj = (JSONObject) jsonArray.get(x);
					listaTXT.add(jsonobj.get("UUID").toString());
				}

				if (listaTXT.size() > 0) {
					UtilsFile.crearArchivoSalto(listaTXT, pathCSV);
				}

				String modoAgrupar = getModoAgrupar();
				if (modoAgrupar == null) {
					modoAgrupar = "NONE";
				}
				String CORREO_RESPONSABLE = getCorreoResponsable();
				String validarSAT = getValidarSAT();
				String complementoSAT = getComplementoSAT();
				String notaCreditoSAT = Utils.noNulo(getNotaCreditoSAT());

				// logger.info("validarSAT arriba===>"+validarSAT);
				if ("true".equalsIgnoreCase(validarSAT)) {
					validarSAT = "S";
				} else {
					validarSAT = "N";
				}

				if ("true".equalsIgnoreCase(complementoSAT)) {
					complementoSAT = "S";
				} else {
					complementoSAT = "N";
				}

				if ("true".equalsIgnoreCase(notaCreditoSAT)) {
					notaCreditoSAT = "S";
				} else {
					notaCreditoSAT = "N";
				}

				logger.info("CORREO_RESPONSABLE===>" + CORREO_RESPONSABLE);
				logger.info("validarSAT===>" + validarSAT);
				logger.info("complementoSAT===>" + complementoSAT);
				logger.info("notaCreditoSAT===>" + notaCreditoSAT);
				logger.info("modoAgrupar===>" + modoAgrupar);

				String rutaDest = UtilsPATH.RUTA_PUBLIC_LAYOUT + nombreReporte;

				File fileXMLDest = new File(rutaDest);
				File fileTXT = new File(pathCSV);

				UtilsFile.moveFileDirectory(fileTXT, fileXMLDest, true, true, true, false);

				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());

				String descargarFacturas = "S";
				String descargarComplemento = "S";
				String descargarNotaCredito = "S";
				String tipoBusqueda = "TEXTO";
				String rfcProveedor = "";
				String rutaXMLProcesar = "";
				long codeOperacion = 0;

				UsuariosBean usuariosBean = new UsuariosBean();
				ExportarXMLBean exportarXMLBean = new ExportarXMLBean();
				UsuariosForm usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
				exportarXMLBean.procesaArchivo(fileXMLDest, session.getEsquemaEmpresa(), getUsuario(request),
						empresasForm.getEmailDominio(), empresasForm.getPwdCorreo(), usuariosForm.getNombreCompleto(),
						CORREO_RESPONSABLE, modoAgrupar, validarSAT, complementoSAT, notaCreditoSAT, descargarFacturas,
						descargarComplemento, descargarNotaCredito, tipoBusqueda, rfcProveedor, fechaInicial,
						fechaFinal, rutaXMLProcesar, codeOperacion);

			}

			/*
			 * if (alFiles.isEmpty()){ addActionMessage("Usurio y/o Pasword Incorrecto!");
			 * return ERROR; } else{ AccesoBean accesoBean = new AccesoBean(); EmpresasForm
			 * empresasForm =
			 * accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
			 * 
			 * reportFile = empresasForm.getRfc() + "_RECIBIDOS.zip";
			 * 
			 * ZipFiles zipFiles = new ZipFiles(); //zipFiles.generaZIP(alFiles,
			 * "C:/EJEMPLO_PDF/bovedaZIP.zip"); ByteArrayOutputStream dest =
			 * zipFiles.zipFiles(alFiles); setInputStream(new
			 * ByteArrayInputStream(dest.toByteArray())); }
			 */

			// se elimina los fuentes PDF del directorio..
			/*
			 * File fileTemp = new File(rutaHTML); fileTemp.delete();
			 * logger.info("El directorio ha sido borrado.....");
			 */
			BovedaModel bovedaModel = new BovedaModel();
			bovedaModel.setCodError("000");
			bovedaModel.setMensajeError("Filtro seleccionado correctamente.");

			org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
			out.print(json);
			out.flush();
			out.close();

		} catch (Exception e) {
			Utils.imprimeLog("exportBovedaZIP(): ", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	/*
	 * public String exportBovedaZIP() { Connection con = null; ResultadoConexion rc
	 * = null;
	 * 
	 * HttpServletRequest request = ServletActionContext.getRequest(); BovedaBean
	 * bovedaBean = new BovedaBean();
	 * 
	 * String directorioXML = null; ArrayList<String> alFiles = new
	 * ArrayList<String>();
	 * 
	 * String logo = "logoToyota.png"; String bandLogo = "S"; String rutaHTML =
	 * null; try{ Map<String, Object > mapaRes = null; JSONArray jsonArray = null;
	 * SiarexSession session = ObtenerSession.getSession(request);
	 * 
	 * if ("".equals(session.getEsquemaEmpresa())){ return Action.LOGIN; } else{ rc
	 * = getConnection(session.getEsquemaEmpresa()); con = rc.getCon();
	 * 
	 * rutaHTML = UtilsPATH.RUTA_PUBLIC_HTML +session.getEsquemaEmpresa() +
	 * File.separator + "TEMP_PDF" + File.separator; File fileTemp = new
	 * File(rutaHTML); if (!fileTemp.exists()) { fileTemp.mkdirs(); } String
	 * rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS +session.getEsquemaEmpresa() +
	 * "/BOVEDA/";
	 * 
	 * String idRegistro = getIdRegistro(); String pathPDF = ""; String fechaInicial
	 * = Utils.noNulo(getFechaInicial()); String fechaFinal =
	 * Utils.noNulo(getFechaFinal());
	 * 
	 * mapaRes = bovedaBean.detalleBovedaZIP(con, session.getEsquemaEmpresa(),
	 * Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()),
	 * Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
	 * Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()),
	 * Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), idRegistro); jsonArray =
	 * (JSONArray) mapaRes.get("detalle"); JSONObject jsonobj = null;
	 * 
	 * ConfigAdicionalesBean configAdicionalesBean = new ConfigAdicionalesBean();
	 * HashMap<String, String> mapaConfig =
	 * configAdicionalesBean.obtenerConfiguraciones(con,
	 * session.getEsquemaEmpresa()); bandLogo =
	 * Utils.noNulo(mapaConfig.get("BANDERA_LOGO_TOYOTA"));
	 * 
	 * if(bandLogo.equalsIgnoreCase("S")) { logo = "logoFactura.png"; } else { logo
	 * = "logoVacio.png"; }
	 * 
	 * for (int x = 0; x < jsonArray.size(); x++){ jsonobj = (JSONObject)
	 * jsonArray.get(x); if (!"".equals(jsonobj.get("XML"))){ directorioXML =
	 * rutaBoveda + jsonobj.get("XML"); alFiles.add(directorioXML); pathPDF =
	 * rutaHTML + jsonobj.get("UUID") + ".pdf"; if(!pathPDF.equals("")) { try { new
	 * CreaPDF().GenerarByXML(directorioXML, pathPDF, (rutaBoveda + "/" + logo));
	 * alFiles.add(pathPDF); }catch(Exception e) {
	 * 
	 * } } } } } if (alFiles.isEmpty()){
	 * addActionMessage("Usurio y/o Pasword Incorrecto!"); return ERROR; } else{
	 * AccesoBean accesoBean = new AccesoBean(); EmpresasForm empresasForm =
	 * accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
	 * 
	 * reportFile = empresasForm.getRfc() + "_RECIBIDOS.zip";
	 * 
	 * ZipFiles zipFiles = new ZipFiles(); //zipFiles.generaZIP(alFiles,
	 * "C:/EJEMPLO_PDF/bovedaZIP.zip"); ByteArrayOutputStream dest =
	 * zipFiles.zipFiles(alFiles); setInputStream(new
	 * ByteArrayInputStream(dest.toByteArray())); }
	 * 
	 * // se elimina los fuentes PDF del directorio.. File fileTemp = new
	 * File(rutaHTML); fileTemp.delete();
	 * logger.info("El directorio ha sido borrado.....");
	 * 
	 * }catch(Exception e){ Utils.imprimeLog("exportBovedaZIP(): ", e); }finally{
	 * try{ if (con != null){ con.close(); } con = null; }catch(Exception e){ con =
	 * null; } } return SUCCESS; }
	 */

	public String generaPDF(String idRegistro, HttpServletRequest request) {
		String pathPDF = "";
		BovedaBean bovedaBean = new BovedaBean();
		ResultadoConexion rc = null;
		JSONObject jsonobj = null;
		Connection con = null;
		String documentoPDF = "";
		String logo = "logoToyota.png";
		String bandLogo = "S";

		try {
			logger.info("********* GENERANDO PDF **********");
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA");
				if (bandLogo.equalsIgnoreCase("S")) {
					logo = "logoToyota.png";
				} else {
					logo = "logoVacio.png";
				}

				try {
					jsonobj = bovedaBean.consultaBovedaRegistro(con, rc.getEsquema(), Integer.parseInt(idRegistro));
				} catch (Exception e) {
					jsonobj = bovedaBean.consultaBovedaUUID(con, session.getEsquemaEmpresa(), idRegistro);
				}

				// pathPDF = request.getRealPath(".") + File.separator +
				// "files"+File.separator+"bovedaPDF.pdf";
				pathPDF = request.getSession().getServletContext().getRealPath("/") + File.separator + "files"
						+ File.separator + jsonobj.get("UUID") + ".pdf";

				// String repBoveda = "/REPOSITORIOS/"+session.getEsquemaEmpresa()+"/BOVEDA/";
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator
						+ "BOVEDA" + File.separator;
				String xmlBoveda = rutaBoveda + jsonobj.get("XML");

				new CreaPDF().GenerarByXML(xmlBoveda, pathPDF, (rutaBoveda + "/" + logo));
				// com.itextpdf.xmltopdf.CreaPDF.CreaPDF()
				documentoPDF = "/siarex247/files/" + jsonobj.get("UUID") + ".pdf";
			}
		} catch (Exception e) {
			Utils.imprimeLog("generaPDF(): ", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return documentoPDF;
	}

	public String generaXML(String idRegistro, HttpServletRequest request) {
		// String pathPDF = "";
		BovedaBean bovedaBean = new BovedaBean();
		ResultadoConexion rc = null;
		JSONObject jsonobj = null;
		Connection con = null;
		String documentoXML = "";
		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				try {
					jsonobj = bovedaBean.consultaBovedaRegistro(con, rc.getEsquema(), Integer.parseInt(idRegistro));
				} catch (Exception e) { // busca por el UUID
					jsonobj = bovedaBean.consultaBovedaUUID(con, rc.getEsquema(), idRegistro);
				}
				// pathPDF = request.getRealPath(".") + File.separator +
				// "files"+File.separator+"bovedaPDF.pdf";
				// pathPDF = request.getSession().getServletContext().getRealPath("/") +
				// File.separator + "files"+File.separator+ "bovedaPDF.pdf";;

				// String rutaFinal = UtilsPATH.getRutaServer(request);
				// String repBoveda = "/REPOSITORIOS/"+session.getEsquemaEmpresa()+"/BOVEDA/";
				String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator
						+ "BOVEDA" + File.separator;

				String destFile = request.getSession().getServletContext().getRealPath("/") + "files" + File.separator
						+ Utils.dobleEncryptarMD5(jsonobj.get("XML").toString()) + ".xml";

				// String rutaBoveda = rutaFinal + repBoveda;
				String xmlBoveda = rutaBoveda + jsonobj.get("XML");

				File fileXML = new File(xmlBoveda);
				if (fileXML.exists()) {
					// logger.info("xmlBoveda====>"+xmlBoveda);
					documentoXML = "/siarex247/files/" + Utils.dobleEncryptarMD5(jsonobj.get("XML").toString()) + ".xml";

					File sourceArchivo = new File(xmlBoveda);
					File destArchivo = new File(destFile);
					UtilsFile.moveFileDirectory(sourceArchivo, destArchivo, true, true, true, false);
					
				}else {
					documentoXML = "/siarex247/html/sinArchivo.html";
				}

				// /home/siarex247/jvm/apache-tomcat-9.0.30/domains/siarex247.com/siarex
				// //jvm/apache-tomcat-9.0.30/domains/siarex247.com/siarex/files
			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
			documentoXML = "/siarex247/html/sinArchivo.html";
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return documentoXML;
	}

	public String infoVincular() {
		Connection con = null;
		ResultadoConexion rc = null;

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		BovedaBean bovedaBean = new BovedaBean();
		try {
			PrintWriter out = response.getWriter();
			JSONObject json = new JSONObject();
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {
				logger.info("*******************  INFO VINCULAR *************************");
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				// String idRegistro = request.getParameter("idRegistro");
				String idRegistro = Utils.noNulo(getIdRegistro());
				if (idRegistro == null) {
					idRegistro = "";
				}
				logger.info("folio===>" + getFolio());

				String fechaInicial = Utils.noNulo(getFechaInicial());
				String fechaFinal = Utils.noNulo(getFechaFinal());
				String dateOperator = null;
				if ("".equalsIgnoreCase(fechaInicial)) {
					fechaFinal = UtilsFechas.getFechayyyyMMdd();
					fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
					dateOperator = "bt";
				} else {
					dateOperator = getDateOperator();
				}
				dateOperator = "bt";
				// int totRe = bovedaBean.infoVincular(con, session.getEsquemaEmpresa(),
				// Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()),
				// Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
				// Utils.noNulo(getFechaInicial()), Utils.noNulo(getUuid()),
				// Utils.noNulo(getFechaFinal()), idRegistro, "");
				int totRe = bovedaBean.infoVincular(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
						Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
						Utils.noNulo(fechaInicial), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal),
						Utils.noNulo(idRegistro), // cadRegistros: "id1;id2;..."
						"", // estatusConciliacion (vacío como en tu código)
						"P", // tipoComprobante (Complemento de Pago)

						// ===== operadores de texto =====
						Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()),
						Utils.noNulo(getSerieOperator()), Utils.noNulo(getUuidOperator()),

						// ===== fecha con operadores =====
						Utils.noNulo(dateOperator), Utils.noNulo(getDateV1()), Utils.noNulo(getDateV2()),

						// ===== numéricos =====
						Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
						Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
						Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
						Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
						Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
						Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
						Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

				json.put("totalXML", totRe);
				/*
				 * logger.
				 * info("infoVincular params → rfc={}, razon={}, folioOp={}, folioV1={}, dateOp={}, dateV1={}, tipoComprobante={}"
				 * + getRfc() + getRazonSocial() + getFolioOperator() + getFolioV1() +
				 * getDateOperator() + getDateV1() + getTipoComprobante());
				 */

				String jsonobj = JSONObject.toJSONString(json);
				out.print(jsonobj);
				out.flush();
				out.close();

			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String vicularComplementos() {
		final long IDEN = System.currentTimeMillis();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		SiarexSession session = ObtenerSession.getSession(request);

		try {
			PrintWriter out = response.getWriter();
			final String esquemEmpresa = session.getEsquemaEmpresa();
			final String usuarioHTTP = getUsuario(request);
			final String idLenguaje = session.getLenguaje();
			// request.getParameter("idRegistro")
			final String cadRegistro = Utils.noNulo(getIdRegistro());
			final FiltrosEntrada filtros = new FiltrosEntrada();
			filtros.rfcOperator = nn(request, "rfcOperator", "contains");
			filtros.razonOperator = nn(request, "razonOperator", "contains");
			filtros.serieOperator = nn(request, "serieOperator", "contains");
			filtros.tipoOperator = nn(request, "tipoOperator", "equals");
			filtros.uuidOperator = nn(request, "uuidOperator", "contains");
			filtros.dateOperator = nn(request, "dateOperator", "eq");
			filtros.dateV1 = nn(request, "dateV1", "");
			filtros.dateV2 = nn(request, "dateV2", "");
			filtros.folioOperator = nn(request, "folioOperator", "eq");
			filtros.folioV1 = nn(request, "folioV1", "");
			filtros.folioV2 = nn(request, "folioV2", "");
			filtros.totalOperator = nn(request, "totalOperator", "eq");
			filtros.totalV1 = nn(request, "totalV1", "");
			filtros.totalV2 = nn(request, "totalV2", "");
			filtros.subOperator = nn(request, "subOperator", "eq");
			filtros.subV1 = nn(request, "subV1", "");
			filtros.subV2 = nn(request, "subV2", "");
			filtros.ivaOperator = nn(request, "ivaOperator", "eq");
			filtros.ivaV1 = nn(request, "ivaV1", "");
			filtros.ivaV2 = nn(request, "ivaV2", "");
			filtros.ivaRetOperator = nn(request, "ivaRetOperator", "eq");
			filtros.ivaRetV1 = nn(request, "ivaRetV1", "");
			filtros.ivaRetV2 = nn(request, "ivaRetV2", "");
			filtros.isrOperator = nn(request, "isrOperator", "eq");
			filtros.isrV1 = nn(request, "isrV1", "");
			filtros.isrV2 = nn(request, "isrV2", "");
			filtros.impLocOperator = nn(request, "impLocOperator", "eq");
			filtros.impLocV1 = nn(request, "impLocV1", "");
			filtros.impLocV2 = nn(request, "impLocV2", "");

			Thread procVincular = new Thread(() -> {
				procesoVincular(esquemEmpresa, IDEN, cadRegistro, usuarioHTTP, idLenguaje, filtros);
			});
			procVincular.setName("procVincular");
			procVincular.start();

			Map<String, Object> json = new HashMap<String, Object>();
			json.put("IDEN", String.valueOf(IDEN));
			json.put("ESTATUS", "OK");
			out.print(JSONObject.toJSONString(json));
			out.flush();
			out.close();
			logger.info("json===>" + json);
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return SUCCESS;
	}

	public String mensajeComplementos() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		Connection con = null;
		ResultadoConexion rc = null;
		try {
			PrintWriter out = response.getWriter();
			Map<String, Object> mapaRes = null;
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				BovedaBean bovedaBean = new BovedaBean();
				JSONArray jsonArray = null;

				// String paramIDEN = request.getParameter("IDEN");
				String paramIDEN = Utils.noNulo(getIden());
				if (paramIDEN == null || "".equals(paramIDEN)) {
					paramIDEN = "0";
				}

				long IDEN = Long.parseLong(paramIDEN);

				// logger.info("IDEN ====>"+IDEN);

				mapaRes = bovedaBean.datosConsola(con, session.getEsquemaEmpresa(), IDEN);
				jsonArray = (JSONArray) mapaRes.get("DETALLE_CONSOLA");
//	            Object totE = mapaRes.get("TOTE");
//	            Object totNE = mapaRes.get("TOTNE");

				Map<String, Object> json = new HashMap<String, Object>();
///	            json.put("totalVinculados", totE);		// total ?  
//	            json.put("totalError", totNE);			// total ?  
				json.put("data", jsonArray);
				String jsonobj = JSONObject.toJSONString(json);
				out.print(jsonobj);
				out.flush();
				out.close();
				// logger.info("jsonobj====>"+jsonobj);
			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String calcularProcesados() {
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		SiarexSession session = ObtenerSession.getSession(request);

		BovedaBean bovedaBean = new BovedaBean();

		Connection con = null;
		ResultadoConexion rc = null;

		try {
			PrintWriter out = response.getWriter();

			rc = getConnection(session.getEsquemaEmpresa());
			con = rc.getCon();

			// String paramIDEN = request.getParameter("IDEN");
			String paramIDEN = Utils.noNulo(getIden());

			if (paramIDEN == null || "".equals(paramIDEN)) {
				paramIDEN = "0";
			}

			long IDEN = Long.parseLong(paramIDEN);

			// logger.info("************* CALCULANDO PROCESADOS ****************"+IDEN);
			int totExitosos = bovedaBean.totalesEstatus(con, session.getEsquemaEmpresa(), IDEN, "E");
			int totNExitosos = bovedaBean.totalesEstatus(con, session.getEsquemaEmpresa(), IDEN, "NE");

			Map<String, Object> json = new HashMap<String, Object>();
			json.put("TOT_OK", totExitosos);
			json.put("TOT_NOK", totNExitosos);
			out.print(JSONObject.toJSONString(json));
			out.flush();
			out.close();

			// logger.info("json==>"+json);
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	private void procesoVincular(String esquemaEmpresa, long IDEN, String cadRegistro, String usuarioHTTP,
			String idLenguaje, FiltrosEntrada filtros) {
		ResultadoConexion rc = null;
		BovedaBean bovedaBean = new BovedaBean();
		JSONObject jsonobj = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		String logo = "logoToyota.png";
		String bandLogo = "S";

		try {
			JSONArray jsonArray = null;

			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();

			String repBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquemaEmpresa + File.separator + "BOVEDA"
					+ File.separator;
			String fechaInicial = Utils.noNulo(getFechaInicial());
			String fechaFinal = Utils.noNulo(getFechaFinal());
			String dateOperator = null;
			if ("".equalsIgnoreCase(fechaInicial)) {
				fechaFinal = UtilsFechas.getFechayyyyMMdd();
				fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
				dateOperator = "bt";
			} else {
				dateOperator = getDateOperator();
			}
			dateOperator = "bt";
			jsonArray = bovedaBean.datosVincularComplemento(con, esquemaEmpresa, Utils.noNulo(getRfc()),
					Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()), fechaInicial,
					Utils.noNulo(getUuid()), fechaFinal, cadRegistro, "", filtros);
//	            ComplementosBean compleBean = new ComplementosBean(); quitar comentario
			ValidacionesComplemento visorCompleBean = new ValidacionesComplemento();
			String nombreArchivo = null;
			StringBuffer sbElimina = new StringBuffer();
			int idRegistro = 0;
			String uuid = null;
			String uuid_complemento = null;

			final String E = "E";
			final String NE = "NE";
			final String ID_REGISTRO = "ID_REGISTRO";
			final String NOMBRE_XML = "NOMBRE_XML";
			final String UUID = "UUID";
			final String MENSAJE_OK = "El Complemento de Pago ha sido procesado satisfactoriamente.";
			String mensajeComplemento = null;
			String cadrespuesta[] = null;
			HashMap<String, Object> MAPA_RESULTADO = new HashMap<>();
			StringBuffer sbRetorno = new StringBuffer();
			String MENSAJE = null;

			bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA");

			if (bandLogo.equalsIgnoreCase("S")) {
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(esquemaEmpresa);
				logo = UtilsPATH.REPOSITORIO_DOCUMENTOS + "LOGOS" + File.separator + empresasForm.getLogoEmpresa();
			} else {
				logo = "logoVacio.png";
			}

			for (int x = 0; x < jsonArray.size(); x++) {
				try {
					jsonobj = (JSONObject) jsonArray.get(x);
					idRegistro = Integer.parseInt(jsonobj.get(ID_REGISTRO).toString());
					uuid = jsonobj.get(UUID).toString();
					nombreArchivo = jsonobj.get(NOMBRE_XML).toString();

					File fileCompleXML = new File(repBoveda + nombreArchivo);
					File fileComplePDF = new File(repBoveda + "PDF_COMPLEMENTO.pdf");
					// File fileComplePDF = new File(rutaBoveda + uuid+ ".pdf" );
					logger.info("*************  VALIDANDO UUID DESDE BOVEDA ****************************");
					logger.info("uuid ****************************" + uuid);
					if (fileCompleXML.exists()) {
						logger.info("Vinculando Archivo : " + nombreArchivo);
						new CreaPDF().GenerarByXML(fileCompleXML.getAbsolutePath(), fileComplePDF.getAbsolutePath(),
								(repBoveda + "/" + logo));

						MAPA_RESULTADO = visorCompleBean.procesarXML(con, esquemaEmpresa,
								fileCompleXML.getAbsolutePath(), fileComplePDF.getAbsolutePath(), usuarioHTTP,
								idLenguaje, false);

						if (!"".equals(Utils.noNulo(MAPA_RESULTADO.get("MENSAJE")))) {
							MENSAJE = Utils.noNulo(MAPA_RESULTADO.get("MENSAJE")).toString();
						} else {
							MENSAJE = "ERROR";
						}

						sbRetorno.setLength(0);
						if ("OK".equalsIgnoreCase(MENSAJE)) {
							sbRetorno.append(MENSAJE).append(";").append(MAPA_RESULTADO.get("UUID_COMPLEMENTO"))
									.append(";").append(MAPA_RESULTADO.get("RAZON_SOCIAL")).append(";")
									.append(MAPA_RESULTADO.get("ESTADO_SAT")).append(";")
									.append(MAPA_RESULTADO.get("ESTATUS_SAT")).append(";")
									.append("Estimado Proveedor su Complemento de Pago ha sigo validado satisfactoriamente, presione el botón de \"Asignar\" para terminar el proceso.")
									.append(";");
						} else {
							sbRetorno.append(MENSAJE).append(";");
							if ("NO_CUMPLIO"
									.equalsIgnoreCase(Utils.noNulo(MAPA_RESULTADO.get("BAND_TOTAL")).toString())) {
								sbRetorno.append(MAPA_RESULTADO.get("UUID_COMPLEMENTO")).append(";").append("TOTAL_NG")
										.append(";");
							}
						}

						mensajeComplemento = sbRetorno.toString();
						cadrespuesta = mensajeComplemento.split(";");
						MENSAJE = cadrespuesta[0];

						if ("OK".equalsIgnoreCase(MENSAJE)) {
							logger.info("El complemento de pago fue satisfactorio......" + uuid);
							fileComplePDF.delete();
							sbElimina.append(idRegistro).append(";");
							uuid_complemento = cadrespuesta[1];
							// compleBean.actualizaEstatus(con, esquemaEmpresa, uuid_complemento); quitar
							// comentario
							bovedaBean.escribeBitacora(con, esquemaEmpresa, uuid, E, IDEN, MENSAJE_OK);
						} else {
							bovedaBean.escribeBitacora(con, esquemaEmpresa, uuid, NE, IDEN, MENSAJE);
							fileComplePDF.delete();
						}

						// mapaErrores.put("ARCHIVO_"+x, nombreArchivo);
						logger.info("mensajeComplemento---->" + mensajeComplemento);

					} else {
						logger.info("El archivo " + nombreArchivo + " No existe en el directorio de la boveda.");
					}
					logger.info("*************  TERMINO ****************************");
					// Thread.sleep(4000); // eliminar
				} catch (Exception e) {
					Utils.imprimeLog("", e);
					bovedaBean.escribeBitacora(con, esquemaEmpresa, uuid, NE, IDEN,
							"Error al procesar archivo XML del complemento : " + e.getMessage());
				}

			}

			if (sbElimina.length() > 0) {
				bovedaBean.eliminaBoveda(con, esquemaEmpresa, sbElimina.toString(), repBoveda);
			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public String infoVincularBoveda() {
		Connection con = null;
		ResultadoConexion rc = null;

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		BovedaBean bovedaBean = new BovedaBean();
		try {
			PrintWriter out = response.getWriter();
			JSONObject json = new JSONObject();
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {
				logger.info("*******************  INFO VINCULAR *************************");
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				// String idRegistro = Utils.noNulo(request.getParameter("idRegistro"));
				String idRegistro = Utils.noNulo(getIdRegistro());
				if (idRegistro == null) {
					idRegistro = "";
				}
				String fechaInicial = Utils.noNulo(getFechaInicial());
				String fechaFinal = Utils.noNulo(getFechaFinal());
				String dateOperator = null;
				if ("".equalsIgnoreCase(fechaInicial)) {
					fechaFinal = UtilsFechas.getFechayyyyMMdd();
					fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
					dateOperator = "bt";
				} else {
					dateOperator = getDateOperator();
				}
				dateOperator = "bt";
				logger.info("folio===>" + getFolio());
				// int totRe = bovedaBean.infoVincular(con, session.getEsquemaEmpresa(),
				// Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()),
				// Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
				// Utils.noNulo(getFechaInicial()), Utils.noNulo(getUuid()), getFechaFinal(),
				// idRegistro, "N");
				int totRe = bovedaBean.infoVincular(con, session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
						Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
						Utils.noNulo(fechaInicial), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal),
						Utils.noNulo(idRegistro), // cadRegistros: "id1;id2;..."
						"N", // estatusConciliacion
						"P", // tipoComprobante (Complemento de Pago)

						// ===== operadores de texto =====
						Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()),
						Utils.noNulo(getSerieOperator()), Utils.noNulo(getUuidOperator()),

						// ===== fecha con operadores =====
						Utils.noNulo(dateOperator), Utils.noNulo(getDateV1()), Utils.noNulo(getDateV2()),

						// ===== numéricos =====
						Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
						Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
						Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
						Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
						Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
						Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
						Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

				json.put("totalXML", totRe);
				String jsonobj = JSONObject.toJSONString(json);
				out.print(jsonobj);
				out.flush();
				out.close();

			}
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String vicularComplementosBoveda() {
		final long IDEN = System.currentTimeMillis();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		SiarexSession session = ObtenerSession.getSession(request);

		try {
			final String cadRegistro = Utils.noNulo(getIdRegistro());
			final String esquemEmpresa = session.getEsquemaEmpresa();
			final String usuarioHTTP = getUsuario(request);
			final String idLenguaje = "MX";

			// 🔽 Construye FiltrosEntrada desde request
			final FiltrosEntrada f = buildFiltrosEntrada(request);

			PrintWriter out = response.getWriter();
			logger.info("*********** CARGANDO COMPLEMENTOS BOVEDA ****************");

			Thread procVincularBoveda = new Thread(new Runnable() {
				public void run() {
					procesoVincularBoveda(esquemEmpresa, IDEN, cadRegistro, usuarioHTTP, idLenguaje, f);
				}
			});
			procVincularBoveda.setName("procVincularBoveda");
			procVincularBoveda.start();

			Map<String, Object> json = new HashMap<String, Object>();
			json.put("IDEN", String.valueOf(IDEN));
			json.put("ESTATUS", "OK");
			out.print(JSONObject.toJSONString(json));
			out.flush();
			out.close();
		} catch (Exception e) {
			Utils.imprimeLog("procVincularBoveda ", e);
		}
		return SUCCESS;
	}

	public void procesoVincularBoveda(String esquemaEmpresa, long IDEN, String cadRegistro, String usuarioHTTP,
			String idLenguaje, BovedaAction.FiltrosEntrada filtros) {
		ResultadoConexion rc = null;
		BovedaBean bovedaBean = new BovedaBean();
		JSONObject jsonobj = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;

		String logo = "logoToyota.png";
		String bandLogo = "S";

		try {
			JSONArray jsonArray = null;
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			String fechaInicial = Utils.noNulo(getFechaInicial());
			String fechaFinal = Utils.noNulo(getFechaFinal());
			if ("".equalsIgnoreCase(fechaInicial)) {
				fechaFinal = UtilsFechas.getFechayyyyMMdd();
				fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
			}

			logger.info("fechaInicial===>" + fechaInicial);
			logger.info("fechaFinal===>" + fechaFinal);

			String rutaBoveda = UtilsPATH.REPOSITORIO_DOCUMENTOS + esquemaEmpresa + File.separator + "BOVEDA"
					+ File.separator;

			// === Obtén los registros a procesar aplicando los mismos filtros del grid ===
			jsonArray = bovedaBean.datosVincularComplemento(con, esquemaEmpresa, Utils.noNulo(getRfc()),
					Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()), Utils.noNulo(getSerie()),
					Utils.noNulo(fechaInicial), Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), cadRegistro, "N",
					filtros // <<< operadores y valores desde el front
			);

			// logger.info("jsonArray===>" + jsonArray.size());

			VincularComplementos compleBean = new VincularComplementos();
			String nombreArchivo = null;
			StringBuffer sbElimina = new StringBuffer();
			int idRegistro = 0;
			String uuid = null;
			String uuid_complemento = null;

			final String E = "E";
			final String NE = "NE";
			final String ID_REGISTRO = "ID_REGISTRO";
			final String NOMBRE_XML = "NOMBRE_XML";
			final String UUID = "UUID";
			final String MENSAJE_OK = "El Complemento de Pago ha sido procesado satisfactoriamente.";

			String mensajeComplemento = null;
			String[] cadrespuesta = null;
			HashMap<String, Object> MAPA_RESULTADO = new HashMap<>();
			StringBuffer sbRetorno = new StringBuffer();
			String MENSAJE = null;

			// === Logo
			bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA");
			if ("S".equalsIgnoreCase(bandLogo)) {
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(esquemaEmpresa);
				logo = UtilsPATH.REPOSITORIO_DOCUMENTOS + "LOGOS" + File.separator + empresasForm.getLogoEmpresa();
			} else {
				logo = "logoVacio.png";
			}

			for (int x = 0; x < jsonArray.size(); x++) {
				try {
					jsonobj = (JSONObject) jsonArray.get(x);
					idRegistro = Integer.parseInt(String.valueOf(jsonobj.get(ID_REGISTRO)));
					uuid = String.valueOf(jsonobj.get(UUID));
					nombreArchivo = String.valueOf(jsonobj.get(NOMBRE_XML));

					File fileCompleXML = new File(rutaBoveda + nombreArchivo);
					// Si deseas generar PDF, descomenta y usa tu clase CreaPDF:
					// File fileComplePDF = new File(rutaBoveda + "PDF_COMPLEMENTO.pdf");

					logger.info("*************  VALIDANDO UUID DESDE BOVEDA ****************************");
					logger.info("uuid **************************** " + uuid);

					if (fileCompleXML.exists()) {
						logger.info("Vinculando Archivo : " + nombreArchivo);

						// Si vas a generar PDF, hazlo aquí
						// new CreaPDF().GenerarByXML(fileCompleXML.getAbsolutePath(),
						// fileComplePDF.getAbsolutePath(), (rutaBoveda + "/" + logo));

						MAPA_RESULTADO = compleBean.procesarXML(con, esquemaEmpresa, fileCompleXML.getAbsolutePath(),
								usuarioHTTP, idLenguaje);

						// === Forzar a String todo lo que sale del mapa para evitar type mismatch
						MENSAJE = String.valueOf(MAPA_RESULTADO.get("MENSAJE"));
						if (MENSAJE == null || MENSAJE.trim().isEmpty())
							MENSAJE = "ERROR";

						sbRetorno.setLength(0);
						if ("OK".equalsIgnoreCase(MENSAJE)) {
							sbRetorno.append(MENSAJE).append(";")
									.append(String.valueOf(MAPA_RESULTADO.get("UUID_COMPLEMENTO"))).append(";")
									.append(String.valueOf(MAPA_RESULTADO.get("RAZON_SOCIAL"))).append(";")
									.append(String.valueOf(MAPA_RESULTADO.get("ESTADO_SAT"))).append(";")
									.append(String.valueOf(MAPA_RESULTADO.get("ESTATUS_SAT"))).append(";")
									.append("Estimado Proveedor su Complemento de Pago ha sigo validado satisfactoriamente, presione el botón de \"Asignar\" para terminar el proceso.")
									.append(";");
						} else {
							sbRetorno.append(MENSAJE).append(";");
							String bandTotal = String.valueOf(MAPA_RESULTADO.get("BAND_TOTAL"));
							if ("NO_CUMPLIO".equalsIgnoreCase(bandTotal)) {
								sbRetorno.append(String.valueOf(MAPA_RESULTADO.get("UUID_COMPLEMENTO"))).append(";")
										.append("TOTAL_NG").append(";");
							}
						}

						mensajeComplemento = sbRetorno.toString();
						cadrespuesta = mensajeComplemento.split(";");
						MENSAJE = cadrespuesta[0];

						if ("OK".equalsIgnoreCase(MENSAJE)) {
							logger.info("El complemento de pago fue satisfactorio......" + uuid);
							uuid_complemento = (cadrespuesta.length > 1) ? cadrespuesta[1] : null;
							bovedaBean.escribeBitacora(con, esquemaEmpresa, uuid, E, IDEN, MENSAJE_OK);
							// Marca para eliminación si así lo manejas después:
							sbElimina.append(idRegistro).append(";");
						} else {
							bovedaBean.escribeBitacora(con, esquemaEmpresa, uuid, NE, IDEN, MENSAJE);
						}

					} else {
						logger.info("El archivo " + nombreArchivo + " no existe en el directorio de la boveda.");
					}

					logger.info("*************  TERMINO ****************************");

				} catch (Exception e) {
					Utils.imprimeLog("", e);
					// En caso de error, intenta registrar en bitácora con el uuid disponible
					try {
						bovedaBean.escribeBitacora(con, esquemaEmpresa, uuid, NE, IDEN,
								"Error al procesar archivo XML del complemento : " + e.getMessage());
					} catch (Exception ignored) {
					}
				}
			}

			// Si deseas eliminar procesados, descomenta:
			// if (sbElimina.length() > 0) {
			// bovedaBean.eliminaBoveda(con, esquemaEmpresa, sbElimina.toString(),
			// rutaBoveda);
			// }

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception ignore) {
			}
			con = null;
		}
	}

	public String exportExcel() {
		SXSSFWorkbook myWorkBook = new SXSSFWorkbook(100); // Keep 100 rows in memory
		SXSSFSheet mySheet = myWorkBook.createSheet("Detalle Recibidos");

		Connection con = null;
		ResultadoConexion rc = null;
		BovedaBean bovedaBean = new BovedaBean();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			SiarexSession session = ObtenerSession.getSession(request);

			rc = getConnection(session.getEsquemaEmpresa());
			con = rc.getCon();
			String fechaInicial = Utils.noNulo(getFechaInicial());
			String fechaFinal = Utils.noNulo(getFechaFinal());
			String dateOperator = null;
			if ("".equalsIgnoreCase(fechaInicial)) {
				fechaFinal = UtilsFechas.getFechayyyyMMdd();
				fechaInicial = UtilsFechas.restarDiasFecha(fechaFinal, 365);
				dateOperator = "bt";
			} else {
				dateOperator = getDateOperator();
			}
			dateOperator = "bt";
			// ArrayList<BovedaForm> datosBoveda = new BovedaBean().detalleBovedaEXCEL(con,
			// session.getEsquemaEmpresa(), rfc, folio, fechaInicial, tipoComprobante, uuid,
			// fechaFinal, "");
			// ArrayList<BovedaForm> datosBoveda = bovedaBean.detalleBoveda(con,
			// session.getEsquemaEmpresa(), Utils.noNulo(getRfc()),
			// Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()),
			// Utils.noNulo(getSerie()), Utils.noNulo(getFechaInicial()),
			// Utils.noNulo(getTipoComprobante()), Utils.noNulo(getUuid()),
			// Utils.noNulo(getFechaFinal()), 0, 0, true);
			ArrayList<BovedaForm> datosBoveda = bovedaBean.detalleBoveda(con, session.getEsquemaEmpresa(),
					Utils.noNulo(getRfc()), Utils.noNulo(getRazonSocial()), Utils.noNulo(getFolio()),
					Utils.noNulo(getSerie()), Utils.noNulo(fechaInicial), Utils.noNulo(getTipoComprobante()),
					Utils.noNulo(getUuid()), Utils.noNulo(fechaFinal), 0, // start (ignorado cuando isExcel = true)
					0, // end (ignorado cuando isExcel = true)
					true, // isExcel

					// ===== operadores de texto =====
					Utils.noNulo(getRfcOperator()), Utils.noNulo(getRazonOperator()), Utils.noNulo(getSerieOperator()),
					Utils.noNulo(getTipoOperator()), Utils.noNulo(getUuidOperator()),

					// ===== FECHA con operadores =====
					Utils.noNulo(dateOperator), // eq, ne, lt, gt, le, ge, bt
					Utils.noNulo(getDateV1()), // YYYY-MM-DD
					Utils.noNulo(getDateV2()), // YYYY-MM-DD (solo para bt)

					// ===== filtros numéricos =====
					Utils.noNulo(getFolioOperator()), Utils.noNulo(getFolioV1()), Utils.noNulo(getFolioV2()),
					Utils.noNulo(getTotalOperator()), Utils.noNulo(getTotalV1()), Utils.noNulo(getTotalV2()),
					Utils.noNulo(getSubOperator()), Utils.noNulo(getSubV1()), Utils.noNulo(getSubV2()),
					Utils.noNulo(getIvaOperator()), Utils.noNulo(getIvaV1()), Utils.noNulo(getIvaV2()),
					Utils.noNulo(getIvaRetOperator()), Utils.noNulo(getIvaRetV1()), Utils.noNulo(getIvaRetV2()),
					Utils.noNulo(getIsrOperator()), Utils.noNulo(getIsrV1()), Utils.noNulo(getIsrV2()),
					// impuestos locales
					Utils.noNulo(getImpLocOperator()), Utils.noNulo(getImpLocV1()), Utils.noNulo(getImpLocV2()));

			ConvierteEXCEL convExcel = new ConvierteEXCEL();
			int REG_NUEVA_PAGINA = 1000000;
			if (datosBoveda.size() >= REG_NUEVA_PAGINA) {
				SXSSFSheet mySheet2 = myWorkBook.createSheet("Detalle Recibidos (2)");
				convExcel.toExcel(mySheet, datosBoveda, myWorkBook, 0, REG_NUEVA_PAGINA, session.getLenguaje());
				convExcel.toExcel(mySheet2, datosBoveda, myWorkBook, REG_NUEVA_PAGINA, datosBoveda.size(),
						session.getLenguaje());
			} else {
				convExcel.toExcel(mySheet, datosBoveda, myWorkBook, 0, datosBoveda.size(), session.getLenguaje());
			}
			// new ConvierteEXCEL().toExcel(mySheet, datosBoveda, myWorkBook);

			try {
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());

				reportFile = empresasForm.getRfc() + "_RECIBIDOS.xlsx";

				ByteArrayOutputStream boas = new ByteArrayOutputStream();
				myWorkBook.write(boas);
				setInputStream(new ByteArrayInputStream(boas.toByteArray()));
				myWorkBook.close();
			} catch (IOException e) {
				Utils.imprimeLog("exportExcel(IOE): ", e);
			}
		} catch (Exception e) {
			Utils.imprimeLog("exportExcel(e): ", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public String catProveedores() {
		Connection con = null;
		ResultadoConexion rc = null;

		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		BovedaBean bovedaBean = new BovedaBean();

		try {
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())) {
				return Action.LOGIN;
			} else {

				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				BovedaModel bovedaModel = new BovedaModel();
				ArrayList<BovedaForm> listaDetalle = bovedaBean.comboProveedores(con, rc.getEsquema(),
						session.getLenguaje());

				bovedaModel.setData(listaDetalle);
				org.json.JSONObject json = new org.json.JSONObject(bovedaModel);
				out.print(json);
				out.flush();
				out.close();
				// logger.info("json==>"+json);
			}

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e) {
				con = null;
			}
		}
		return SUCCESS;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getReportFile() {
		return reportFile;
	}

	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

	// DTO
	public static class FiltrosEntrada {
		public String rfcOperator, razonOperator, serieOperator, tipoOperator, uuidOperator;
		public String dateOperator, dateV1, dateV2;
		public String folioOperator, folioV1, folioV2;
		public String totalOperator, totalV1, totalV2;
		public String subOperator, subV1, subV2;
		public String ivaOperator, ivaV1, ivaV2;
		public String ivaRetOperator, ivaRetV1, ivaRetV2;
		public String isrOperator, isrV1, isrV2;
		public String impLocOperator, impLocV1, impLocV2;
	}

	private static String nn(HttpServletRequest r, String k, String d) {
		String v = r.getParameter(k);
		return (v == null || v.trim().isEmpty()) ? d : v.trim();
	}

	private FiltrosEntrada buildFiltrosEntrada(HttpServletRequest req) {
		FiltrosEntrada f = new FiltrosEntrada();

		String fechaInicial = Utils.noNulo(getFechaInicial());
		String dateOperator = null;
		if ("".equalsIgnoreCase(fechaInicial)) {
			dateOperator = "bt";
		} else {
			dateOperator = getDateOperator();
		}
		dateOperator = "bt";
		// texto
		f.rfcOperator = Utils.noNulo(req.getParameter("rfcOperator"));
		f.razonOperator = Utils.noNulo(req.getParameter("razonOperator"));
		f.serieOperator = Utils.noNulo(req.getParameter("serieOperator"));
		f.tipoOperator = Utils.noNulo(req.getParameter("tipoOperator"));
		f.uuidOperator = Utils.noNulo(req.getParameter("uuidOperator"));

		// fecha
		f.dateOperator = Utils.noNulo(dateOperator);
		f.dateV1 = Utils.noNulo(req.getParameter("dateV1"));
		f.dateV2 = Utils.noNulo(req.getParameter("dateV2"));

		// numéricos
		f.folioOperator = Utils.noNulo(req.getParameter("folioOperator"));
		f.folioV1 = Utils.noNulo(req.getParameter("folioV1"));
		f.folioV2 = Utils.noNulo(req.getParameter("folioV2"));

		f.totalOperator = Utils.noNulo(req.getParameter("totalOperator"));
		f.totalV1 = Utils.noNulo(req.getParameter("totalV1"));
		f.totalV2 = Utils.noNulo(req.getParameter("totalV2"));

		f.subOperator = Utils.noNulo(req.getParameter("subOperator"));
		f.subV1 = Utils.noNulo(req.getParameter("subV1"));
		f.subV2 = Utils.noNulo(req.getParameter("subV2"));

		f.ivaOperator = Utils.noNulo(req.getParameter("ivaOperator"));
		f.ivaV1 = Utils.noNulo(req.getParameter("ivaV1"));
		f.ivaV2 = Utils.noNulo(req.getParameter("ivaV2"));

		f.ivaRetOperator = Utils.noNulo(req.getParameter("ivaRetOperator"));
		f.ivaRetV1 = Utils.noNulo(req.getParameter("ivaRetV1"));
		f.ivaRetV2 = Utils.noNulo(req.getParameter("ivaRetV2"));

		f.isrOperator = Utils.noNulo(req.getParameter("isrOperator"));
		f.isrV1 = Utils.noNulo(req.getParameter("isrV1"));
		f.isrV2 = Utils.noNulo(req.getParameter("isrV2"));

		f.impLocOperator = Utils.noNulo(req.getParameter("impLocOperator"));
		f.impLocV1 = Utils.noNulo(req.getParameter("impLocV1"));
		f.impLocV2 = Utils.noNulo(req.getParameter("impLocV2"));

		return f;
	}

}
