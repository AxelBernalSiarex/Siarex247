package com.siarex247.catalogos.sat.Catalogos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class CatalogosBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	public boolean validarClaveProducto(Connection con, String esquema, String claveProductoServicio){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isExiste = false;
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getValidarClaveProducto(esquema));
			stmt.setString(1, claveProductoServicio);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				isExiste = true;
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
		return isExiste;
	}
	
	
	public String obtenerDescripcionProducto(Connection con, String esquema, String claveProductoServicio){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String desProducto = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getValidarClaveProducto(esquema));
			stmt.setString(1, claveProductoServicio);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				desProducto = Utils.noNuloNormal(rs.getString(1));
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
		return desProducto;
	}
	
	
	public String consultaEstado (Connection con, String esquema, String claveEstado){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String descripcion = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarEstado(esquema));
			stmt.setString(1, claveEstado);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				descripcion = Utils.noNulo(rs.getString(1));
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
		return descripcion;
	}
	
	
	public String consultaCiudad (Connection con, String esquema, String claveEstado, String claveLocalidad){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String descripcion = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarCiudad(esquema));
			stmt.setString(1, claveEstado);
			stmt.setString(2, claveLocalidad);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				descripcion = Utils.noNulo(rs.getString(1));
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
		return descripcion;
	}
	
	
	public String consultaUnidad (Connection con, String esquema, String claveUnidad){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String descripcion = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarUnidad(esquema));
			stmt.setString(1, claveUnidad);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				descripcion = Utils.noNuloNormal(rs.getString(1));
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
		return descripcion;
	}
	
	
	public String consultaTasaCuota (Connection con, String esquema, int claveRegistro){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String tasaCuota = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarTasaCuota(esquema));
			stmt.setInt(1, claveRegistro);

			rs  = stmt.executeQuery();
			if (rs.next()) {
				tasaCuota = Utils.noNulo(rs.getString(1));
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
		return tasaCuota;
	}
	
	
	public String consultaRegimen(Connection con, String esquema, String claveRegimen){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String descripcion = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarRegimen(esquema));
			stmt.setString(1, claveRegimen);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				descripcion = Utils.noNuloNormal(rs.getString(1));
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
		return descripcion;
	}
	
	public String consultaRegimenDescripcion(Connection con, String esquema, String descripcion){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String claveRegimen = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarRegimenDescripcion(esquema));
			stmt.setString(1, descripcion);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				claveRegimen = Utils.noNuloNormal(rs.getString(1));
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
		return claveRegimen;
	}
	
	public String consultaExportacion(Connection con, String esquema, String claveExportacion){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String descripcion = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarExportacion(esquema));
			stmt.setString(1, claveExportacion);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				descripcion = Utils.noNuloNormal(rs.getString(1));
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
		return descripcion;
	}
	
	
	public String consultaMetodoPago(Connection con, String esquema, String claveMetodoPago){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String descripcion = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarMetodoPago(esquema));
			stmt.setString(1, claveMetodoPago);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				descripcion = Utils.noNuloNormal(rs.getString(1));
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
		return descripcion;
	}
	
	public String consultaUsoCFDI(Connection con, String esquema, String claveUsoCFDI){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String descripcion = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarUsoCFDI(esquema));
			stmt.setString(1, claveUsoCFDI);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				descripcion = Utils.noNuloNormal(rs.getString(1));
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
		return descripcion;
	}
	
	
	public String consultaFormaPago(Connection con, String esquema, String claveFormaPago){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String descripcion = "";
		try {
			stmt = con.prepareStatement(CatalogosQuerys.getConsultarFormaPago(esquema));
			stmt.setString(1, claveFormaPago);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				descripcion = Utils.noNuloNormal(rs.getString(1));
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
		return descripcion;
	}
	
	public HashMap<String, CatalogosForm> detalleMoneda (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaMonedas = new HashMap<>();
		CatalogosForm monedaPagoForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleMonedas(esquema));
			rs  = stmt.executeQuery();
			String claveMoneda = null;
			while (rs.next()) {
				claveMoneda = Utils.noNulo(rs.getString(1));
				monedaPagoForm.setClave(claveMoneda);
				monedaPagoForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				mapaMonedas.put(claveMoneda, monedaPagoForm);
				monedaPagoForm = new CatalogosForm();
				
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
		return mapaMonedas;
	}
	
	
	public HashMap<String, CatalogosForm> detalleFormas (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaFormas = new HashMap<>();
		CatalogosForm formaPagoForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleFormaPago(esquema));
			rs  = stmt.executeQuery();
			String claveForma = null;
			while (rs.next()) {
				claveForma = Utils.noNulo(rs.getString(1));
				formaPagoForm.setClave(claveForma);
				formaPagoForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				// logger.info("Clave===>"+formaPagoForm.getClave()+", Descripcion==>"+formaPagoForm.getDescripcion());
				mapaFormas.put(claveForma, formaPagoForm);
				formaPagoForm = new CatalogosForm();
				
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
		return mapaFormas;
	}
	
	
	public HashMap<String, CatalogosForm> detalleExportacion (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaFormas = new HashMap<>();
		CatalogosForm exportacionForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleExportacion(esquema));
			rs  = stmt.executeQuery();
			String claveForma = null;
			while (rs.next()) {
				claveForma = Utils.noNulo(rs.getString(1));
				exportacionForm.setClave(claveForma);
				exportacionForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				// logger.info("Clave===>"+formaPagoForm.getClave()+", Descripcion==>"+formaPagoForm.getDescripcion());
				mapaFormas.put(claveForma, exportacionForm);
				exportacionForm = new CatalogosForm();
				
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
		return mapaFormas;
	}
	
	public HashMap<String, CatalogosForm> detalleTipoRelacion (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaTipoRelacion = new HashMap<>();
		CatalogosForm tipoRelacionForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleTipoRelacion(esquema));
			rs  = stmt.executeQuery();
			String claveForma = null;
			while (rs.next()) {
				claveForma = Utils.noNulo(rs.getString(1));
				tipoRelacionForm.setClave(claveForma);
				tipoRelacionForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				mapaTipoRelacion.put(claveForma, tipoRelacionForm);
				tipoRelacionForm = new CatalogosForm();
				
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
		return mapaTipoRelacion;
	}
	
	
	public HashMap<String, CatalogosForm> detalleCodigoPostal (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaCodigoPostal = new HashMap<>();
		CatalogosForm codigoPostalForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleCodigoPostal(esquema));
			rs  = stmt.executeQuery();
			String claveCodigo = null;
			while (rs.next()) {
				claveCodigo = Utils.noNulo(rs.getString(1));
				codigoPostalForm.setCodigoPostal(claveCodigo);
				codigoPostalForm.setEstado(Utils.noNuloNormal(rs.getString(2)));
				mapaCodigoPostal.put(claveCodigo, codigoPostalForm);
				codigoPostalForm = new CatalogosForm();
				
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
		return mapaCodigoPostal;
	}
	
	
	public HashMap<String, CatalogosForm> detalleMetodoPago (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaMetodo = new HashMap<>();
		CatalogosForm metodoPagoForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleMetodoPago(esquema));
			rs  = stmt.executeQuery();
			String claveMetodo = null;
			while (rs.next()) {
				claveMetodo = Utils.noNulo(rs.getString(1));
				metodoPagoForm.setClave(claveMetodo);
				metodoPagoForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				mapaMetodo.put(claveMetodo, metodoPagoForm);
				metodoPagoForm = new CatalogosForm();
				
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
		return mapaMetodo;
	}
	
	
	public HashMap<String, CatalogosForm> detalleRegimenFiscal (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaRegimen = new HashMap<>();
		CatalogosForm regimenFiscalForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleRegimenFiscal(esquema));
			rs  = stmt.executeQuery();
			String claveRegimen = null;
			while (rs.next()) {
				claveRegimen = Utils.noNulo(rs.getString(1));
				regimenFiscalForm.setClave(claveRegimen);
				regimenFiscalForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				mapaRegimen.put(claveRegimen, regimenFiscalForm);
				regimenFiscalForm = new CatalogosForm();
				
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
		return mapaRegimen;
	}
	
	
	public HashMap<String, CatalogosForm> detalleUsoCFDI (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaUso = new HashMap<>();
		CatalogosForm usoCFDIForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleUsoCFDI(esquema));
			rs  = stmt.executeQuery();
			String claveUso = null;
			while (rs.next()) {
				claveUso = Utils.noNulo(rs.getString(1));
				usoCFDIForm.setClave(claveUso);
				usoCFDIForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				mapaUso.put(claveUso, usoCFDIForm);
				usoCFDIForm = new CatalogosForm();
				
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
		return mapaUso;
	}
	
	
	public HashMap<String, CatalogosForm> detalleClaveProductos (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapProductos = new HashMap<>();
		CatalogosForm claveProductoForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleProductoServicio(esquema));
			rs  = stmt.executeQuery();
			String clavProducto = null;
			while (rs.next()) {
				clavProducto = Utils.noNulo(rs.getString(1));
				claveProductoForm.setClave(clavProducto);
				claveProductoForm.setDescripcion( Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(2))));
				mapProductos.put(clavProducto, claveProductoForm);
				claveProductoForm = new CatalogosForm();
				
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
		return mapProductos;
	}
	
	
	public HashMap<String, CatalogosForm> detalleTipoNomina (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaTipoNomina = new HashMap<>();
		CatalogosForm tipoNominaForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleTipoNomina(esquema));
			rs  = stmt.executeQuery();
			String claveMoneda = null;
			while (rs.next()) {
				claveMoneda = Utils.noNulo(rs.getString(1));
				tipoNominaForm.setClave(claveMoneda);
				tipoNominaForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				mapaTipoNomina.put(claveMoneda, tipoNominaForm);
				tipoNominaForm = new CatalogosForm();
				
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
		return mapaTipoNomina;
	}
	
	
	public HashMap<String, CatalogosForm> detalleTipoDeducciones (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaTipoDeducciones = new HashMap<>();
		CatalogosForm tipoDeduccionesForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleTipoDeducciones(esquema));
			rs  = stmt.executeQuery();
			String claveMoneda = null;
			while (rs.next()) {
				claveMoneda = Utils.noNulo(rs.getString(1));
				tipoDeduccionesForm.setClave(claveMoneda);
				tipoDeduccionesForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				mapaTipoDeducciones.put(claveMoneda, tipoDeduccionesForm);
				tipoDeduccionesForm = new CatalogosForm();
				
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
		return mapaTipoDeducciones;
	}
	
	
	public HashMap<String, CatalogosForm> detalleOtroPagos (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaTipoOtroPagos = new HashMap<>();
		CatalogosForm tipoOtroPagosForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleOtroPagos(esquema));
			rs  = stmt.executeQuery();
			String claveMoneda = null;
			while (rs.next()) {
				claveMoneda = Utils.noNulo(rs.getString(1));
				tipoOtroPagosForm.setClave(claveMoneda);
				tipoOtroPagosForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				mapaTipoOtroPagos.put(claveMoneda, tipoOtroPagosForm);
				tipoOtroPagosForm = new CatalogosForm();
				
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
		return mapaTipoOtroPagos;
	}

	
	public HashMap<String, CatalogosForm> detalleVersion (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaFormas = new HashMap<>();
		CatalogosForm formaPagoForm = new CatalogosForm();
		try {		
			
				String claveForma = "3.3";
				formaPagoForm.setClave(claveForma);
				formaPagoForm.setDescripcion("Versión 3.3");
				mapaFormas.put(claveForma, formaPagoForm);
				formaPagoForm = new CatalogosForm();
				
				claveForma = "4.0";
				formaPagoForm.setClave(claveForma);
				formaPagoForm.setDescripcion("Versión 4.0");
				mapaFormas.put(claveForma, formaPagoForm);
				formaPagoForm = new CatalogosForm();
				
			
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
		return mapaFormas;
	}

	
	public HashMap<String, CatalogosForm> detalleTipoComprobante (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaFormas = new HashMap<>();
		CatalogosForm tipoComprobanteForm = new CatalogosForm();
		try {		
			stmt = con.prepareStatement(CatalogosQuerys.getDetalleTipoComprobante(esquema));
			rs  = stmt.executeQuery();
			String claveForma = null;
			while (rs.next()) {
				claveForma = Utils.noNulo(rs.getString(1));
				tipoComprobanteForm.setClave(claveForma);
				tipoComprobanteForm.setDescripcion(Utils.noNuloNormal(rs.getString(2)));
				// logger.info("Clave===>"+formaPagoForm.getClave()+", Descripcion==>"+formaPagoForm.getDescripcion());
				mapaFormas.put(claveForma, tipoComprobanteForm);
				tipoComprobanteForm = new CatalogosForm();
				
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
		return mapaFormas;
	}
	

	
	public HashMap<String, CatalogosForm> detalleVersionComplemento (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<String, CatalogosForm> mapaFormas = new HashMap<>();
		CatalogosForm formaPagoForm = new CatalogosForm();
		try {		
			
				String claveForma = "1.0";
				formaPagoForm.setClave(claveForma);
				formaPagoForm.setDescripcion("Versión 1.0");
				mapaFormas.put(claveForma, formaPagoForm);
				formaPagoForm = new CatalogosForm();
				
				claveForma = "2.0";
				formaPagoForm.setClave(claveForma);
				formaPagoForm.setDescripcion("Versión 2.0");
				mapaFormas.put(claveForma, formaPagoForm);
				formaPagoForm = new CatalogosForm();
				
			
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
		return mapaFormas;
	}
	
}
