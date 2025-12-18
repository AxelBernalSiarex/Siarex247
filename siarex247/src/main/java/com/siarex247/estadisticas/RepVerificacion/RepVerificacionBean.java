package com.siarex247.estadisticas.RepVerificacion;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.w3c.dom.Document;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsColor;
import com.siarex247.utils.UtilsSiarex;
import com.siarex247.validaciones.UtilsSAT;

public class RepVerificacionBean {

	public static final Logger logger = Logger.getLogger("siarex");
	
	private String desEstatus (String estatus) {
		if ("PRO".equalsIgnoreCase(estatus)) {
			return "PROCESANDO";
		}else if ("TER".equalsIgnoreCase(estatus)) {
			return "TERMINADO";
		}else {
			return "";
		}
	}
	
	public ArrayList<RepVerificacionForm>  detalleBitacora (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<RepVerificacionForm> listaDetalle = new ArrayList<RepVerificacionForm>();
		RepVerificacionForm repBitForm = new RepVerificacionForm();
		// Map<String, Object > mapaRes = new HashMap<String, Object>();
		try {
			stmt = con.prepareStatement(RepVerificacionQuerys.getListaBitacora(esquema));
			rs = stmt.executeQuery();
            
			
			while (rs.next()) {
					repBitForm.setIdRegistro(rs.getInt(1));
					repBitForm.setDescripcion(Utils.noNulo(rs.getString(2)));
					repBitForm.setValidarFactura(Utils.noNulo(rs.getString(3)));
					repBitForm.setValidarComplemento(Utils.noNulo(rs.getString(4)));
					repBitForm.setValidarNota(Utils.noNulo(rs.getString(5)));
					repBitForm.setFecIni(Utils.noNulo(rs.getString(6)));
					repBitForm.setFecFin(Utils.noNulo(rs.getString(7)));
					repBitForm.setEstatus(Utils.noNulo(rs.getString(8)));
					repBitForm.setDesEstatus(desEstatus(repBitForm.getEstatus()));
					
					repBitForm.setTotProcesar(rs.getInt(9));
					repBitForm.setTotProcesados(rs.getInt(10));
					
					listaDetalle.add(repBitForm);
					repBitForm = new RepVerificacionForm();	
				
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
	
	
	public int altaBitacora (Connection con, String esquema, String descripcion, String validarFactura,  String validarComplemento, String validarNota,  String fecIni, String fecFin, String estatus, String tipoFecha, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(RepVerificacionQuerys.getInsertBitacora(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, descripcion);
			stmt.setString(2, validarFactura);
			stmt.setString(3, validarComplemento);
			stmt.setString(4, validarNota);
			stmt.setString(5, fecIni);
			stmt.setString(6, fecFin);
			stmt.setString(7, estatus);
			stmt.setString(8, tipoFecha);
			stmt.setString(9, usuarioHTTP);
			
			int cant = stmt.executeUpdate();
            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next())
                    resultado = rs.getInt(1);
            }

		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
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
		return resultado;
	}
	
	
	
	public int eliminaBitacora (Connection con, String esquema, int idBitacora){
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(RepVerificacionQuerys.getEliminarEstadisticas(esquema));
			stmt.setInt(1, idBitacora);
			stmt.executeUpdate();

			stmt.close();
			stmt = null;
			
			stmt = con.prepareStatement(RepVerificacionQuerys.getEliminarBitacora(esquema));
			stmt.setInt(1, idBitacora);
			stmt.executeUpdate();
			
		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
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
		return resultado;
	}
	
	
	
	public void procesoVerifica(final String esquema, final String fecIni, final String fecFin, final String verificaFactura, final String verificaComple, final String verificaNota, final String rutaRepositorio, final int idBitacora, final String tipoFecha ){
		try{
			   	Thread procesoVerifica = new Thread(new Runnable() {
						public void run() {
							iniciaProceso(esquema, fecIni, fecFin, verificaFactura, verificaComple, verificaNota, rutaRepositorio, idBitacora, tipoFecha );	
						}
					});
			   		procesoVerifica.start();
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
	
	
	public void iniciaProceso (String esquema, String fecIni, String fecFin, String verificaFactura, String verificaComple, String verificaNota, String rutaRepositorio, int idBitacora, String tipoFecha ) {
		final String PROVEEDORES = "PROVEEDORES";
		final String SEPARADOR = File.separator;
		final String S = "S";
		final String VACIO = "";
		try {
			
			
			logger.info("************ INICIANDO PROCESO DE REPORTE VALIDACION SAT ****************");
			
			ArrayList<DatosProcesoForm> listadoOrdenes = listadoOrdenes(esquema, fecIni, fecFin, tipoFecha);
			DatosProcesoForm datosProcesoForm = null;
			// String xmlFactura = null;
			String xmlNota = null;
			
			String rutaXMLFactura = null;
			String rutaXMLComplemento = null;
			String rutaXMLNotas = null;
			
			StringBuffer sbRutas = new StringBuffer();
			
			String rfcFacturaXML = null;
			String rfcFacturaComple = null;
			String rfcFacturaNota = null;
			
			String rfcReceptorXMLFactura = null;
			String rfcReceptorXMLComple = null;
			String rfcReceptorXMLNota = null;
			
			// String totalXMLFactura = null;
			double totalXMLComple = 0;
			String totalXMLNotas = null;
			
					
			String uuidXMLFactura = null;
			String uuidXMLComple = null;
			String uuidXMLNota = null;
			
			String datosSATFactura [] = null;
			String datosSATComple [] = null;
			String datosSANotaT [] = null;
			
			Document document = null;
			// CargasProveedorUtils cargaUtils = new CargasProveedorUtils();
			
			
			Comprobante _comprobante = null;
			LeerXML crea = null;
			
			
			actualizaAvance(esquema, idBitacora, listadoOrdenes.size(), 0); // se actualiza el avance
			
			ArrayList<DatosGuardarForm> datosGuardar = new ArrayList<>();
			int totProcesados = 0;
			
			
			
			for (int x = 0; x < listadoOrdenes.size(); x++) {
				datosProcesoForm = listadoOrdenes.get(x);
				DatosGuardarForm datosGuardarForm = new DatosGuardarForm();
				datosGuardarForm.setFolioOrden(datosProcesoForm.getFolioOrden());

				try {
					sbRutas.setLength(0);
					rutaXMLFactura = sbRutas.append(rutaRepositorio).append(PROVEEDORES).append(SEPARADOR).append(datosProcesoForm.getClaveProveedor()).append(SEPARADOR).append(datosProcesoForm.getNombreXMLFactura()).toString();
					
					_comprobante = LeerXML.ObtenerComprobante(rutaXMLFactura);
					/*
					xmlFactura = UtilsFile.leeArchivo(rutaXMLFactura);
					int pos = xmlFactura.indexOf("<cfdi:Comprobante ");
					if (pos > -1 ){
						xmlFactura = xmlFactura.substring(pos, xmlFactura.length());
					}
					
					document = XMLXPathManagerBase.parseXml(xmlFactura);
					
					rfcFacturaXML = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Emisor/@Rfc");
					uuidXMLFactura = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/TimbreFiscalDigital/@UUID");
					rfcReceptorXMLFactura = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Receptor/@Rfc");
					totalXMLFactura = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Total");
					*/
					rfcFacturaXML = _comprobante.getEmisor().getRfc();
					uuidXMLFactura = _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
					rfcReceptorXMLFactura = _comprobante.getReceptor().getRfc();
					double totalFactura = _comprobante.getTotal();
					
					if (S.equalsIgnoreCase(verificaFactura)) {
						datosSATFactura = UtilsSAT.validaSAT(rfcFacturaXML, rfcReceptorXMLFactura,  totalFactura, uuidXMLFactura);
						
					}else {
						datosSATFactura[0] = null;
						datosSATFactura[1] = null;
						
					}
					datosGuardarForm.setRfcFactura(rfcFacturaXML);
					datosGuardarForm.setUuidFactura(uuidXMLFactura);
					datosGuardarForm.setEstadoFactura(datosSATFactura[0]);
					datosGuardarForm.setEstatusFactura(datosSATFactura[1]);
					
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				}
				// complementos de pago
				try {
					if (!VACIO.equalsIgnoreCase(datosProcesoForm.getNombreXMLComple())) {
						sbRutas.setLength(0);
						rutaXMLComplemento = sbRutas.append(rutaRepositorio).append(PROVEEDORES).append(SEPARADOR).append(datosProcesoForm.getClaveProveedor()).append(SEPARADOR).append(datosProcesoForm.getNombreXMLComple()).toString();
				    	crea = new LeerXML();
						/*int codValidacion = crea.leeElementos(rutaXMLComplemento);
						_comprobante = crea.get_comprobante();*/
				    	_comprobante = LeerXML.ObtenerComprobante(rutaXMLComplemento);
						
						rfcFacturaComple  = _comprobante.getEmisor().getRfc();
						uuidXMLComple        = _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
						rfcReceptorXMLComple = _comprobante.getReceptor().getRfc();
						totalXMLComple       = _comprobante.getTotal();
						if (S.equalsIgnoreCase(verificaComple)) {
							datosSATComple = UtilsSAT.validaSAT(rfcFacturaComple, rfcReceptorXMLComple, totalXMLComple, uuidXMLComple);
						}else {
							datosSATComple[0] = null;
							datosSATComple[1] = null;
							
						}
						
						datosGuardarForm.setRfcComple(rfcFacturaComple);
						datosGuardarForm.setUuidComple(uuidXMLComple);
						datosGuardarForm.setEstadoComple(datosSATComple[0]);
						datosGuardarForm.setEstatusComple(datosSATComple[1]);
					}
			    }catch(Exception e) {
			    	Utils.imprimeLog("", e);
			    }
				try {
					
					if (!VACIO.equalsIgnoreCase(datosProcesoForm.getNombreXMLNotaCre())) {
						sbRutas.setLength(0);
						rutaXMLNotas = sbRutas.append(rutaRepositorio).append(PROVEEDORES).append(SEPARADOR).append(datosProcesoForm.getClaveProveedor()).append(SEPARADOR).append(datosProcesoForm.getNombreXMLNotaCre()).toString();
						
						_comprobante = LeerXML.ObtenerComprobante(rutaXMLNotas);
						/*
						xmlNota = UtilsFile.leeArchivo(rutaXMLNotas);
						int pos = xmlNota.indexOf("<cfdi:Comprobante ");
						if (pos > -1 ){
							xmlNota = xmlNota.substring(pos, xmlNota.length());
						}
						document = XMLXPathManagerBase.parseXml(xmlNota);
						
						rfcFacturaNota  = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Emisor/@Rfc");
						uuidXMLNota        = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Complemento/TimbreFiscalDigital/@UUID");
						rfcReceptorXMLNota = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/Receptor/@Rfc");
						totalXMLNotas       = XMLXPathManagerBase.getNodeValue(document, "/Comprobante/@Total");
						*/
						
						rfcFacturaXML = _comprobante.getEmisor().getRfc();
						uuidXMLFactura = _comprobante.getComplemento().TimbreFiscalDigital.getUUID();
						rfcReceptorXMLFactura = _comprobante.getReceptor().getRfc();
						double totalFactura = _comprobante.getTotal();
						
						
						if (S.equalsIgnoreCase(verificaNota)) {
							datosSANotaT  = UtilsSAT.validaSAT(rfcFacturaNota, rfcReceptorXMLNota, totalFactura, uuidXMLNota);
						}else {
							datosSANotaT[0] = null;
							datosSANotaT[1] = null;
							
						}
						datosGuardarForm.setRfcNota(rfcFacturaNota);
						datosGuardarForm.setUuidNota(uuidXMLNota);
						datosGuardarForm.setEstadoNota(datosSANotaT[0]);
						datosGuardarForm.setEstatusNota(datosSANotaT[1]);
					}
					
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				}
				datosGuardar.add(datosGuardarForm);
				
				totProcesados++;
				if (datosGuardar.size() == 100) {
					guardarRegistros(esquema, datosGuardar, idBitacora);
					actualizaAvance(esquema, idBitacora, listadoOrdenes.size(), totProcesados);
				}
			}
			
			if (datosGuardar.size() > 0) { // se guarda el resto..
				guardarRegistros(esquema, datosGuardar, idBitacora);
			}
			
			actualizaAvance(esquema, idBitacora, listadoOrdenes.size(), totProcesados);
			actulizaBitacora(esquema, idBitacora);
			
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	
	public int guardarRegistros(String esquema, ArrayList<DatosGuardarForm> datosGuardar, int idBitacora) {
		Connection con = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		PreparedStatement stmt = null;
		int totGuardar = 0;
		StringBuffer sbQuery = new StringBuffer();
		try {
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			
			//ID_BITACORA, FOLIO_ORDEN, RFC_CFDI, RFC_COMPLEMENTO, RFC_NOTA, UUID_CFDI, UUID_COMPLEMENTO, UUID_NOTA, ESTADO_CFDI_SAT, ESTATUS_CFDI_SAT, ESTADO_COMPLEMENTO_SAT, ESTATUS_COMPLEMENTO_SAT, ESTADO_NOTA_SAT, ESTATUS_NOTA_SAT			
			sbQuery.append(RepVerificacionQuerys.getGuardarEstadistica(esquema));
			final String signoComa = "(?,?,?,?,?,?,?,?,?,?,?,?,?,? ),";
			for (int x = 0; x < datosGuardar.size(); x++) {
				sbQuery.append(signoComa);
			}
			
			String queryFinal = sbQuery.toString().substring(0, sbQuery.toString().length() - 1);
			stmt = con.prepareStatement(queryFinal);
			
			DatosGuardarForm datosGuardarForm = null;
			int contador = 1;
			for (int x = 0; x < datosGuardar.size(); x++) {
				datosGuardarForm =  datosGuardar.get(x);
				stmt.setInt(contador++, idBitacora);
				stmt.setLong(contador++, datosGuardarForm.getFolioOrden());
				stmt.setString(contador++, datosGuardarForm.getRfcFactura());
				stmt.setString(contador++, datosGuardarForm.getRfcComple());
				stmt.setString(contador++, datosGuardarForm.getRfcNota());
				stmt.setString(contador++, datosGuardarForm.getUuidFactura());
				stmt.setString(contador++, datosGuardarForm.getUuidComple());
				stmt.setString(contador++, datosGuardarForm.getUuidNota());
				stmt.setString(contador++, datosGuardarForm.getEstadoFactura());
				stmt.setString(contador++, datosGuardarForm.getEstatusFactura());
				stmt.setString(contador++, datosGuardarForm.getEstadoComple());
				stmt.setString(contador++, datosGuardarForm.getEstatusComple());
				stmt.setString(contador++, datosGuardarForm.getEstadoNota());
				stmt.setString(contador++, datosGuardarForm.getEstatusNota());
			}
			
			totGuardar = stmt.executeUpdate();
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
				if (con != null) {
					con.close();
				}
				con = null;
				
			}catch(Exception e) {
				stmt = null;
				con = null;
			}
		}
		return totGuardar;
	}
	
	
	public int actualizaAvance(String esquema, int idBitacora, int totProcesar, int totAvance) {
		Connection con = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		PreparedStatement stmt = null;
		int totGuardar = 0;
		try {
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			
			stmt = con.prepareStatement(RepVerificacionQuerys.getActualizaAvance(esquema));
			stmt.setInt(1, totProcesar);
			stmt.setInt(2, totAvance);
			stmt.setInt(3, idBitacora);
			totGuardar = stmt.executeUpdate();
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
				if (con != null) {
					con.close();
				}
				con = null;
				
			}catch(Exception e) {
				stmt = null;
				con = null;
			}
		}
		return totGuardar;
	}
	
	
	
	public ArrayList<DatosProcesoForm> listadoOrdenes (String esquema, String fecIni, String fecFin, String tipoFecha ){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<DatosProcesoForm> listadoOrdenes = new ArrayList<DatosProcesoForm>();
		DatosProcesoForm datosForm = new DatosProcesoForm();
		Connection con = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		String campoFecha = null;
		try {
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			
			if ("PAG".equalsIgnoreCase(tipoFecha)) {
				campoFecha = "FECHAPAGO";
			}else if ("FAC".equalsIgnoreCase(tipoFecha)) {
				campoFecha = "FECHA_UUID";
			}else {
				campoFecha = "FECHAREGISTRO";
			}
			
			stmt = con.prepareStatement(RepVerificacionQuerys.getListadoOrdenes(esquema, campoFecha));
			stmt.setString(1, fecIni + " 01:01:01");
			stmt.setString(2, fecFin + " 23:59:59");
			
			logger.info("stmt------>"+stmt);
			rs = stmt.executeQuery();
			while (rs.next()) {
				datosForm.setFolioOrden(rs.getLong(1));
				datosForm.setClaveProveedor(rs.getInt(2));
				datosForm.setNombreXMLFactura(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(3))));
				datosForm.setNombreXMLComple(Utils.noNuloNormal(rs.getString(4)));
				datosForm.setNombreXMLNotaCre(Utils.noNuloNormal(rs.getString(5)));
				listadoOrdenes.add(datosForm);
				datosForm = new DatosProcesoForm();
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
				if (con != null) {
					con.close();
				}
				con = null;
				
			}catch(Exception e) {
				rs = null;
				stmt = null;
				con = null;
			}
		}
		return listadoOrdenes;
	}
	
	
	
	public int actulizaBitacora (String esquema, int idBitacora){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		Connection con = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		
		try {
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			
			stmt = con.prepareStatement(RepVerificacionQuerys.getActualizaBitacora(esquema));
			stmt.setString(1, "TER");
			stmt.setInt(2, idBitacora);
			resultado = stmt.executeUpdate();

		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
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
		return resultado;
	}
	
	
	
	public ArrayList<ReporteExcelForm> datosReporte (Connection con, String esquema, String lenguaje, int idBitacora){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ReporteExcelForm reporteExcelForm = new ReporteExcelForm();
		ArrayList<ReporteExcelForm> listaReporte = new ArrayList<>();
		try {
			stmt = con.prepareStatement(RepVerificacionQuerys.getGeneraReporte(esquema));
			stmt.setInt(1, idBitacora);
			rs = stmt.executeQuery();
			while (rs.next()) {
				reporteExcelForm.setFolioEmpresa(rs.getLong(1));
				reporteExcelForm.setRfcOrden(Utils.noNulo(rs.getString(2)));
				reporteExcelForm.setRazonSocialOrden(Utils.noNulo(rs.getString(3)));
				reporteExcelForm.setDescripcion(Utils.noNulo(rs.getString(4)));
				reporteExcelForm.setTipoMoneda(Utils.noNulo(rs.getString(5)));
				reporteExcelForm.setMonto(rs.getDouble(6));
				reporteExcelForm.setServicioRecibido(desServicioProducto(Utils.noNulo(rs.getString(7))));
				reporteExcelForm.setDesEstatusPago(Utils.noNulo(rs.getString(8)) + " - "+ UtilsSiarex.desEstatus(Utils.noNulo(rs.getString(8)), lenguaje));
				reporteExcelForm.setSerie(Utils.noNulo(rs.getString(9)));
				reporteExcelForm.setTotal(rs.getDouble(10));
				reporteExcelForm.setSubTotal(rs.getDouble(11));
				reporteExcelForm.setIva(rs.getDouble(12));
				reporteExcelForm.setIvaRet(rs.getDouble(13));
				reporteExcelForm.setIsrRet(rs.getDouble(14));
				reporteExcelForm.setImpLocales(rs.getDouble(15));
				reporteExcelForm.setFechaPago(Utils.noNulo(rs.getString(16)));
				reporteExcelForm.setFechaUltMov(Utils.noNulo(rs.getString(17)));
				reporteExcelForm.setFechaRegistro(Utils.noNulo(rs.getString(18)));
				reporteExcelForm.setRfcFactura(Utils.noNulo(rs.getString(19)));
				reporteExcelForm.setUuidOrden(Utils.noNulo(rs.getString(20)));
				reporteExcelForm.setUuidFactura(Utils.noNulo(rs.getString(21)));
				reporteExcelForm.setFechaUUID(Utils.noNulo(rs.getString(22)));
				reporteExcelForm.setEstadoFactura(Utils.noNuloNormal(rs.getString(23)));
				reporteExcelForm.setEstatusFactura(Utils.noNuloNormal(rs.getString(24)));
				reporteExcelForm.setRfcComplemento(Utils.noNulo(rs.getString(25)));
				reporteExcelForm.setUuidComplementoOrden(Utils.noNulo(rs.getString(26)));
				reporteExcelForm.setUuidComplemento(Utils.noNulo(rs.getString(27)));
				reporteExcelForm.setEstadoComplemento(Utils.noNuloNormal(rs.getString(28)));
				reporteExcelForm.setEstatusComplemento(Utils.noNuloNormal(rs.getString(29)));
				reporteExcelForm.setRfcNota(Utils.noNulo(rs.getString(30)));
				reporteExcelForm.setUuidNotaCreditoOrden(Utils.noNulo(rs.getString(31)));
				reporteExcelForm.setUuidNotaCredito(Utils.noNulo(rs.getString(32)));
				reporteExcelForm.setEstadoNota(Utils.noNuloNormal(rs.getString(33)));
				reporteExcelForm.setEstatusNota(Utils.noNuloNormal(rs.getString(34)));
				listaReporte.add(reporteExcelForm);
				reporteExcelForm = new ReporteExcelForm();
			}
			
		}catch(SQLException sql) {
			Utils.imprimeLog("", sql);
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
		return listaReporte;
	}
	
	
	
	
	public void generaReporte(SXSSFSheet hoja1 ,ArrayList<ReporteExcelForm> listaReporte, SXSSFWorkbook objLibro) { 
		  try {
			  
			   //final String[] nameColumns = {"Orden de Compra", "Monto Factura", "RFC", "Razon Social", "Tipo de Moneda", "Tipo de Confirmacion", "Importe Pagado" };
			  final String[] nameColumns = {"RFC","Razon Social","Orden de Compra","Concepto","Tipo de Moneda","Monto","Servicio Recibido ?","Status de Pago","Serie","Total","Sub Total","IVA","IVA RET","ISR RET","IMP Locales","Fecha de Pago"
					  ,"Fecha Ultimo Movimiento","Fecha Registro","RFC Factura","UUID Orden de Compra","UUID Factura","Fecha UUID","Estado SAT Factura","Estatus SAT Factura","RFC Complemento","UUID Complemento Orden","UUID Complemento Factura"
					  ,"Estado SAT Complemento","Estatus SAT Complemento","RFC Nota Credito","UUID Nota Credito Orden","UUID Nota Credito Factura","Estado SAT Nota Credito","Estatus SAT Nota Credito"};
			  
			   Row header = hoja1.createRow(0);
			   header.setHeightInPoints(18);
			   
			   
			   
			   Font fuenteEncabezado = objLibro.createFont();
			   fuenteEncabezado.setFontHeightInPoints((short)11);
			   fuenteEncabezado.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteEncabezado.setColor(IndexedColors.WHITE.index);
			  
			   Font fuenteDetalle = objLibro.createFont();
			   fuenteDetalle.setFontHeightInPoints((short)10);
			   fuenteDetalle.setFontName(HSSFFont.FONT_ARIAL);
			   fuenteDetalle.setBold(true);
			  
			   
			  
			   CellStyle estiloCelda3 = objLibro.createCellStyle();
			   //estiloCelda2.setWrapText(true);
			   estiloCelda3.setAlignment(HorizontalAlignment.CENTER);
			   estiloCelda3.setVerticalAlignment(VerticalAlignment.TOP);
			   estiloCelda3.setFont(fuenteDetalle);
			   estiloCelda3.setFillForegroundColor(IndexedColors.WHITE.index);
			   estiloCelda3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			   
			   
			   CellStyle styleTitulo = objLibro.createCellStyle();
			   Font headerFont = objLibro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true); // new java.awt.Color(12, 57, 90)
		       styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
		       styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleTitulo.setFont(headerFont);
		       styleTitulo.setAlignment(HorizontalAlignment.CENTER);
			   
			   
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Administración y Recepción de XML");
			   monthCell.setCellStyle(styleTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:AH1"));
			   
			   
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			    Cell monthCell2 = header.createCell(0);
			    monthCell2.setCellValue("Listado de Ordenes de Compra");
			    monthCell2.setCellStyle(estiloCelda3);
			    hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:AH2"));
			    
			    
			    
			   header = hoja1.createRow(2);
			   header.setHeightInPoints(18);
			   for (int i = 0; i < nameColumns.length; i++) {
			    monthCell = header.createCell(i);
			    monthCell.setCellValue(nameColumns[i]);
			    // monthCell.setCellStyle(encabezadoDetalle);
			   }
			   
			   ReporteExcelForm reporteForm = null;
			   
			   int rowNum = 3;
			   Row myRow = null;
			   
			   for (int x = 0; x < listaReporte.size(); x++) {
				   reporteForm = listaReporte.get(x);
				   myRow = hoja1.createRow(rowNum++);
				   
				   if (x % 100 == 0 && x != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hoja1).flushRows(100); // Retain the last 100 rows in memory
	                }

				   
				   myRow.createCell(0).setCellValue(reporteForm.getRfcOrden());
				   myRow.createCell(1).setCellValue(reporteForm.getRazonSocialOrden());
				   myRow.createCell(2).setCellValue(reporteForm.getFolioEmpresa());
				   myRow.createCell(3).setCellValue(reporteForm.getDescripcion());
				   myRow.createCell(4).setCellValue(reporteForm.getTipoMoneda());
				   myRow.createCell(5).setCellValue(reporteForm.getMonto());
				   myRow.createCell(6).setCellValue(reporteForm.getServicioRecibido());
				   myRow.createCell(7).setCellValue(reporteForm.getDesEstatusPago());
				   myRow.createCell(8).setCellValue(reporteForm.getSerie());
				   myRow.createCell(9).setCellValue(reporteForm.getTotal());
				   myRow.createCell(10).setCellValue(reporteForm.getSubTotal());
				   myRow.createCell(11).setCellValue(reporteForm.getIva());
				   myRow.createCell(12).setCellValue(reporteForm.getIvaRet());
				   myRow.createCell(13).setCellValue(reporteForm.getIsrRet());
				   myRow.createCell(14).setCellValue(reporteForm.getImpLocales());
				   myRow.createCell(15).setCellValue(reporteForm.getFechaPago());
				   myRow.createCell(16).setCellValue(reporteForm.getFechaUltMov());
				   myRow.createCell(17).setCellValue(reporteForm.getFechaRegistro());
				   myRow.createCell(18).setCellValue(reporteForm.getRfcFactura());
				   myRow.createCell(19).setCellValue(reporteForm.getUuidOrden());
				   myRow.createCell(20).setCellValue(reporteForm.getUuidFactura());
				   myRow.createCell(21).setCellValue(reporteForm.getFechaUUID());
				   myRow.createCell(22).setCellValue(reporteForm.getEstadoFactura());
				   myRow.createCell(23).setCellValue(reporteForm.getEstatusFactura());
				   myRow.createCell(24).setCellValue(reporteForm.getRfcComplemento());
				   myRow.createCell(25).setCellValue(reporteForm.getUuidComplementoOrden());
				   myRow.createCell(26).setCellValue(reporteForm.getUuidComplemento());
				   myRow.createCell(27).setCellValue(reporteForm.getEstadoComplemento());
				   myRow.createCell(28).setCellValue(reporteForm.getEstatusComplemento());
				   myRow.createCell(29).setCellValue(reporteForm.getRfcNota());
				   myRow.createCell(30).setCellValue(reporteForm.getUuidNotaCreditoOrden());
				   myRow.createCell(31).setCellValue(reporteForm.getUuidNotaCredito());
				   myRow.createCell(32).setCellValue(reporteForm.getEstatusNota());
				   myRow.createCell(33).setCellValue(reporteForm.getEstadoNota());
				  
			   }
		  } catch (Exception e) {
			  Utils.imprimeLog("", e);
		  }
	 }	
	
	private String desServicioProducto(String servicioProducto){
		if ("0".equalsIgnoreCase(servicioProducto)){
			return "NO";
		}
		if ("1".equalsIgnoreCase(servicioProducto)){
			return "SI";
		}
	   return "";	
	}
	
	
	
public boolean iniciarExcel(String esquemaEmpresa, String lenguaje,  SXSSFWorkbook libro, SXSSFSheet hojaReporte, int idBitacora, String usuarioHTTP) {
		
		boolean generoXLS = false;
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		
		
		try {
			
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();

			ArrayList<ReporteExcelForm> listaReporte =  datosReporte(con, esquemaEmpresa, lenguaje, idBitacora);
			generaReporte(hojaReporte, listaReporte, libro);
			 
            generoXLS = true;
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally
        {
            try
            {
                if (con != null)
                {
                    con.close();
                }
                con = null;
            }
            catch (Exception e)
            {
                con = null;
            }
        }
		return generoXLS;
	}

	
}
