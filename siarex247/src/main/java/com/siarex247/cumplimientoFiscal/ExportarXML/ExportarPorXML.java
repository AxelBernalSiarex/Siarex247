package com.siarex247.cumplimientoFiscal.ExportarXML;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class ExportarPorXML {

	public static final Logger logger = Logger.getLogger("siarex247");
	private final String ENCONTRADO = "ENCONTRADO";
	private final String VACIO = "";
	
	public ArrayList<ArrayList<String>> obtenerUUIDPorXML(String esquema, String rutaXMLProcesar, String rfcReceptor, long codeOperacion, String usuarioHTTP){
		ArrayList<String> listaXML = new ArrayList<>();
		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
		// DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
		Comprobante comprobante = null;
		try{

			String uuidXML = null;
			String tipoComprobante = null;
			File fileProcesar = new File(rutaXMLProcesar);
			DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
			String rutaDestinoXML = null;
			for (File fileXML : fileProcesar.listFiles()) {
				try {
					comprobante = LeerXML.ObtenerComprobante(fileXML.getAbsolutePath()) ;
			    } catch(Exception e) {
			    	Utils.imprimeLog("", e);
			    }
				
				if (comprobante == null) {
					
				}else {
					
					uuidXML = Utils.noNuloNormal(comprobante.getComplemento().getTimbreFiscalDigital().getUUID());
					tipoComprobante = Utils.noNuloNormal(comprobante.getTipoDeComprobante());
					
					if (rfcReceptor.equalsIgnoreCase(comprobante.getReceptor().getRfc())) {
						if ("I".equalsIgnoreCase(tipoComprobante) || "ingreso".equalsIgnoreCase(tipoComprobante) || "P".equalsIgnoreCase(tipoComprobante) 
								|| "E".equalsIgnoreCase(tipoComprobante) || "egreso".equalsIgnoreCase(tipoComprobante)) {
							
							rutaDestinoXML = fileXML.getParentFile() + File.separator + uuidXML + ".xml";
							Path origen = Paths.get(fileXML.getAbsolutePath());
							Path destino = Paths.get(rutaDestinoXML);

							Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
							
							if ("I".equalsIgnoreCase(tipoComprobante) || "ingreso".equalsIgnoreCase(tipoComprobante)) {
								listaXML.add(rutaDestinoXML);
								listaXML.add(uuidXML); // nombre del pdf
							}else {
								listaXML.add(VACIO);
								listaXML.add("NONE");
							}
							
							
							listaXML.add(ENCONTRADO);
							listaXML.add(VACIO);
							listaXML.add(Utils.regresaCaracteresNormales(Utils.noNulo(comprobante.getEmisor().getNombre() ))); // RAZON_SOCIAL
							listaXML.add(decimalFormat.format(comprobante.getSubTotal())); // SUB_TOTAL
							listaXML.add(String.valueOf(comprobante.getTotal())); // TOTAL
							listaXML.add(Utils.noNuloNormal(uuidXML)); // UUID
							listaXML.add(Utils.noNuloNormal(comprobante.getEmisor().getRfc())); // RFC
							listaXML.add(decimalFormat.format(comprobante.getTotal())); // TOTAL FORMATEADO
							listaXML.add(VACIO); // FOLIO EMPRESA
							listaXML.add(uuidXML); // UUID Solo
							listaXML.add(Utils.noNuloNormal(comprobante.getMoneda())); // TIPO DE MONEDA
							
							
							if ("P".equalsIgnoreCase(tipoComprobante)){
								listaXML.add(rutaDestinoXML); // NOMBRE_XML COMPLEMENTO
								listaXML.add(uuidXML); // NOMBRE_PDF COMPLEMENTO
								listaXML.add(Utils.noNuloNormal(VACIO)); // TOTAL_COMPLEMENTO COMPLEMENTO
								listaXML.add(Utils.noNuloNormal(uuidXML)); // UUID_COMPLEMENTO COMPLEMENTO
								
							}else {
								listaXML.add(VACIO); // NOMBRE_XML COMPLEMENTO
								listaXML.add(VACIO); // NOMBRE_PDF COMPLEMENTO
								listaXML.add(VACIO); // TOTAL_COMPLEMENTO COMPLEMENTO
								listaXML.add(VACIO); // UUID_COMPLEMENTO COMPLEMENTO
							}
								
							if ("E".equalsIgnoreCase(tipoComprobante) || "egreso".equalsIgnoreCase(tipoComprobante)){
								listaXML.add(rutaDestinoXML); // NOMBRE_XML NOTA CREDITO
								listaXML.add(uuidXML); // NOMBRE_PDF NOTA CREDITO
								listaXML.add(String.valueOf(comprobante.getTotal())); // TOTAL_COMPLEMENTO NOTA CREDITO
								listaXML.add(uuidXML); // UUID_COMPLEMENTO NOTA CREDITO
							}else {
								listaXML.add(VACIO); // NOMBRE_XML NOTA CREDITO
								listaXML.add(VACIO); // NOMBRE_PDF NOTA CREDITO
								listaXML.add(VACIO); // TOTAL_COMPLEMENTO NOTA CREDITO
								listaXML.add(VACIO); // UUID_COMPLEMENTO NOTA CREDITO
							}
							
							listaXML.add(tipoComprobante); // UUID_COMPLEMENTO NOTA CREDITO
							
							resultado.add(listaXML);
							listaXML = new ArrayList<>();
						}else {
							guardarBitacora(esquema, uuidXML, comprobante.getReceptor().getRfc(), comprobante.getReceptor().getNombre(), tipoComprobante, "Tipo de comprobante no permitido.", codeOperacion, usuarioHTTP);
						}
					}else {
						guardarBitacora(esquema, uuidXML, comprobante.getReceptor().getRfc(), comprobante.getReceptor().getNombre(), tipoComprobante, "RFC Receptor no permitido.", codeOperacion, usuarioHTTP);
					}
				}
					
			}
				
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
        return resultado;
	}
	
	private int guardarBitacora(String esquemaEmpresa, String uuid, String rfcReceptor, String razonSocial, String tipoComprobante, String mensajeError,  long codeOperacion, String usuarioHTTP) {
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			stmt = con.prepareStatement(ExportarXMLQuerys.getGuardarBitacoraXML(rc.getEsquema()));
			stmt.setString(1, uuid);
			stmt.setString(2, rfcReceptor);
			stmt.setString(3, razonSocial);
			stmt.setString(4, tipoComprobante);
			stmt.setString(5, mensajeError);
			stmt.setString(6, String.valueOf(codeOperacion));
			stmt.setString(7, usuarioHTTP);
			resultado = stmt.executeUpdate();
			
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
		return resultado;
	}
	
	
	public ArrayList<ValidacionXMLForm> consultarBitacora(Connection con, String esquemaBD, long codeOperacion) {
		ArrayList<ValidacionXMLForm> listaDetalle = new ArrayList<>();
		ValidacionXMLForm valForm = new ValidacionXMLForm();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			
			stmt = con.prepareStatement(ExportarXMLQuerys.getConsultarBitacora(esquemaBD));
			stmt.setString(1, String.valueOf(codeOperacion));
			rs = stmt.executeQuery();
			while (rs.next()) {
				valForm.setUuid(Utils.noNuloNormal(rs.getString(1)));
				valForm.setRfcReceptor(Utils.noNuloNormal(rs.getString(2)));
				valForm.setRazonSocial(Utils.noNuloNormal(rs.getString(3)));
				valForm.setTipoComprobante(Utils.noNuloNormal(rs.getString(4)));
				valForm.setMensajeOperacion(Utils.noNuloNormal(rs.getString(5)));
				listaDetalle.add(valForm);
				valForm = new ValidacionXMLForm();
				
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
				stmt = null;
				
			}
		}
		return listaDetalle;
	}
}
