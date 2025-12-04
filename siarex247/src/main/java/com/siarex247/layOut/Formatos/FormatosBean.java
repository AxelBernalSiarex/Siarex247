package com.siarex247.layOut.Formatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsBD;

public class FormatosBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	public ArrayList<FormatosForm> detalleFormatos (Connection con, String esquema, String tipoProveedor){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FormatosForm> listaFormatos = new ArrayList<FormatosForm>();
		FormatosForm formatosForm = new FormatosForm();
		try {
			StringBuffer sbQuery = new StringBuffer(FormatosQuerys.getDetalle(esquema));
			
			if ("MEX".equalsIgnoreCase(tipoProveedor) || "USA".equalsIgnoreCase(tipoProveedor)) {
				sbQuery.append(" and TIPO_PROVEEDOR = ?" );
			}
			
			stmt = con.prepareStatement(sbQuery.toString());
			
			if ("MEX".equalsIgnoreCase(tipoProveedor) || "USA".equalsIgnoreCase(tipoProveedor)) {
				stmt.setString(1, "ALL");
			}
			rs  = stmt.executeQuery();
			while (rs.next()) {
				formatosForm.setIdRegistro(rs.getInt(1));
				formatosForm.setDescripcion(Utils.noNulo(rs.getString(2)));
				formatosForm.setSubjectCorreo(Utils.noNuloNormal(rs.getString(3)));
				formatosForm.setNombreArchivo(Utils.noNuloNormal(rs.getString(4)));
				formatosForm.setTipoProveedor(Utils.noNulo(rs.getString(5)));
				listaFormatos.add(formatosForm);
				formatosForm = new FormatosForm();
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
		return listaFormatos;
	}
	
	
	
	public FormatosForm consultaFormato (Connection con, String esquema, int idRegistro){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FormatosForm formatosForm = new FormatosForm();
		try {
			stmt = con.prepareStatement(FormatosQuerys.getConsulta(esquema));
			stmt.setInt(1, idRegistro);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				formatosForm.setIdRegistro(rs.getInt(1));
				formatosForm.setDescripcion(Utils.noNulo(rs.getString(2)));
				formatosForm.setSubjectCorreo(Utils.noNuloNormal(rs.getString(3)));
				formatosForm.setCuerpoCorreo(Utils.noNuloNormal(rs.getString(4)));
				formatosForm.setNombreArchivo(Utils.noNuloNormal(rs.getString(5)));
				formatosForm.setTipoProveedor(Utils.noNulo(rs.getString(6)));
				
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
		return formatosForm;
	}

	
	
	
	public int altaFormatos (Connection con, String esquema, String descripcion, String subject, String cuerpoMensaje, String nombreArchivo, String tipoProveedor, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(FormatosQuerys.getAlta(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, descripcion);
			stmt.setString(2, subject);
			stmt.setString(3, cuerpoMensaje);
			stmt.setString(4, nombreArchivo);
			stmt.setString(5, tipoProveedor);
			stmt.setString(6, usuarioHTTP);
			
			int cant = stmt.executeUpdate();
			if(cant > 0){
				rs = stmt.getGeneratedKeys();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
			}
			logger.info("El usuario "+usuarioHTTP + " ha agregado el centro de Formatos "+resultado);
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
	
	
	public int updateFormatos (Connection con, String esquema, int idRegistro, String descripcion, String subject, String cuerpoMensaje, String tipoProveedor, String usuarioHTTP){
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(FormatosQuerys.getUpdate(esquema));
			stmt.setString(1, descripcion);
			stmt.setString(2, subject);
			stmt.setString(3, cuerpoMensaje);
			stmt.setString(4, tipoProveedor);
			stmt.setString(5, usuarioHTTP);
			stmt.setInt(6, idRegistro);
			resultado = stmt.executeUpdate();
			logger.info("El usuario "+usuarioHTTP + " ha actualizado el centro de Formatos "+resultado);
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
	
	
	public int deleteFormatos (Connection con, String esquema, int idRegistro, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(FormatosQuerys.getElimina(esquema));
			stmt.setInt(1, idRegistro);
			resultado = stmt.executeUpdate();
			logger.info("El usuario "+usuarioHTTP + " ha eliminado el Formatos "+idRegistro);
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
	
	
	
	public FormatosForm consultaFormatoInstrucciones (Connection con, String esquema, String tipoFormato){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FormatosForm formatosForm = new FormatosForm();
		try {
			stmt = con.prepareStatement(FormatosQuerys.getConsultaInstruc(esquema));
			stmt.setString(1, tipoFormato);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				formatosForm.setIdRegistro(rs.getInt(1));
				formatosForm.setDescripcion(Utils.noNulo(rs.getString(2)));
				formatosForm.setSubjectCorreo(Utils.noNuloNormal(rs.getString(3)));
				formatosForm.setCuerpoCorreo(Utils.noNuloNormal(rs.getString(4)));
				formatosForm.setNombreArchivo(Utils.noNuloNormal(rs.getString(5)));
				formatosForm.setCopiaPara(Utils.noNuloNormal(rs.getString(7)));
				
				
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
		return formatosForm;
	}
	
	
	
	public int altaFormatosInstrucciones (Connection con, String esquema, String descripcion, String subject, String cuerpoMensaje, String tipoFormato, String copiaPara, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			// insert into FORMATOS (DESCRIPCION, SUBJECT, CUERPO_MENSAJE, TIPO_PROVEEDOR, TIPO_FORMATO, COPIA_PARA, CLAVE_USUARIO) 
			// values (?,?,?,?,?,?)
			stmt = con.prepareStatement(FormatosQuerys.getAltaInstru(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, descripcion);
			stmt.setString(2, subject);
			stmt.setString(3, cuerpoMensaje);
			stmt.setString(4, tipoFormato);
			stmt.setString(5, tipoFormato);
			stmt.setString(6, copiaPara);
			stmt.setString(7, usuarioHTTP);
			
			int cant = stmt.executeUpdate();
			if(cant > 0){
				rs = stmt.getGeneratedKeys();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
			}
			logger.info("El usuario "+usuarioHTTP + " ha agregado el centro de Formatos "+resultado);
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
	
	
	
	public int updateFormatosInstruc (Connection con, String esquema, int idRegistro, String descripcion, String subject, String cuerpoMensaje, String tipoProveedor, String copiaPara, String usuarioHTTP){
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
// update FORMATOS set DESCRIPCION = ?, SUBJECT = ?, CUERPO_MENSAJE = ?, CLAVE_USUARIO = ? where CLAVE_REGISTRO = ?			
			stmt = con.prepareStatement(FormatosQuerys.getUpdateInstru(esquema));
			stmt.setString(1, descripcion);
			stmt.setString(2, subject);
			stmt.setString(3, cuerpoMensaje);
			stmt.setString(4, copiaPara);
			stmt.setString(5, usuarioHTTP);
			stmt.setInt(6, idRegistro);
			resultado = stmt.executeUpdate();
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
	
	
	public int enviaFormatoProveedor(Connection con, String esquema, int idProveedor, String razonSocial, String rfc, String tipoProveedor, String rutaFormatos, String usuarioAcceso, String usuarioEmisorMensaje, String passwordEmisorMensaje ) {
		int totEnviados = 0;
		String archivoFormatoPDF = null;
		String archivoFormatoXLS = null;
		String files[] = {null, null}; 
		try {
			String listaCorreosProveedores [] = UtilsBD.obtenerCorreorProveedor(con, esquema, idProveedor, "N", "N", "N","N");
			FormatosForm formatosForm = consultaFormatoInstrucciones(con, esquema, tipoProveedor);
			String mensajeCorreo =  Utils.regresaCaracteresFormato(formatosForm.getCuerpoCorreo(), razonSocial, rfc, usuarioAcceso); // ESTE MENSAJE VA A CAMBIAR
			
			archivoFormatoPDF = rutaFormatos + "INSTRUCCIONES.pdf";
			archivoFormatoXLS = rutaFormatos + "INSTRUCCIONES.xls";
			
			files[0] = archivoFormatoPDF;
			files[1] = archivoFormatoXLS;
			
			String [] emailCC = {null};
			if ("".equalsIgnoreCase(formatosForm.getCopiaPara())) {
				emailCC[0] = "N";
			}else {
				emailCC[0] = formatosForm.getCopiaPara();
			}
			
			EnviaCorreoGrid.enviarCorreoMultipleFiles(files, mensajeCorreo, true, listaCorreosProveedores, emailCC, formatosForm.getSubjectCorreo(), usuarioEmisorMensaje, passwordEmisorMensaje);			
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return totEnviados;
	}
}
