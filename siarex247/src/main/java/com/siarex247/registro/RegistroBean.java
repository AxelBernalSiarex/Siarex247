package com.siarex247.registro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class RegistroBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public String [] tieneAccesoAplicacion(Connection con, int claveEmpresa, String usrAcceso){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String [] resul = {"false","N", "N"};
		try{
			stmt = con.prepareStatement(RegistroQuerys.getTienePermisoAplicacion(""));
			stmt.setInt(1, claveEmpresa);
			stmt.setString(2, usrAcceso);
			stmt.setString(3, "SIAREX247");
			rs = stmt.executeQuery();
			if (rs.next()){
				resul[0] = "true";
				resul[1] = Utils.noNulo(rs.getString(2));
				resul[2] = "S";
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return resul;
	}
	

	public RegistroForm datosEmpresa(Connection con, int claveEmpresa){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		RegistroForm registroForm = new RegistroForm();
		try{
			stmt = con.prepareStatement(RegistroQuerys.getDatosEmpresa(""));
			stmt.setInt(1, claveEmpresa);
			rs = stmt.executeQuery();
			
			String codigoPostal = "";
			if (rs.next()){
				registroForm.setNombreEmpresa(Utils.noNulo(rs.getString(1)));
				registroForm.setEsquemaEmpresa(Utils.noNuloNormal(rs.getString(2)));
				registroForm.setEmailEmpresa(Utils.noNuloNormal(rs.getString(3)));
				registroForm.setPwdEmail(Utils.noNuloNormal(rs.getString(4)));
				codigoPostal = Utils.noNuloNormal(rs.getString(13));
				
				if(codigoPostal.indexOf(".") > 0) {
					codigoPostal = codigoPostal.substring(0, codigoPostal.indexOf("."));
				}
				
				registroForm.setRfc(Utils.noNuloNormal(rs.getString(5)));
				registroForm.setCalle(Utils.noNuloNormal(rs.getString(6)));
				registroForm.setCalleDos(Utils.noNuloNormal(rs.getString(7)));
				registroForm.setNumInt(Utils.noNuloNormal(rs.getString(8)));
				registroForm.setNumExt(Utils.noNuloNormal(rs.getString(9)));
				registroForm.setColonia(Utils.noNuloNormal(rs.getString(10)));
				registroForm.setCiudad(Utils.noNuloNormal(rs.getString(11)));
				registroForm.setEstado(Utils.noNuloNormal(rs.getString(12)));
				registroForm.setCodigoPostal(codigoPostal);
				registroForm.setNombreContacto(Utils.noNuloNormal(rs.getString(14)));
				registroForm.setEmailContacto(Utils.noNuloNormal(rs.getString(15)));
				registroForm.setTelefonoUno(Utils.noNuloNormal(rs.getString(16)));
				registroForm.setExtUno(Utils.noNuloNormal(rs.getString(17)));
				registroForm.setTelefonoDos(Utils.noNuloNormal(rs.getString(18)));
				registroForm.setExtDos(Utils.noNuloNormal(rs.getString(19)));
				registroForm.setLogoEmpresa(Utils.noNuloNormal(rs.getString(20)));
				// registroForm.setTipoEmpresa(rs.getInt(21));
				registroForm.setDireccionEmpresa("");
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
				con = null;				
			}
		}
		return registroForm;
	}
	

	
	public int getPerfil(Connection con, String esquema, String usrAcceso){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resul = 0;
		
		try{
			stmt = con.prepareStatement(RegistroQuerys.getPerfil(esquema));
			stmt.setString(1, usrAcceso);
			rs = stmt.executeQuery();
			if (rs.next()){
				resul = rs.getInt(1);
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
			}
		}
		return resul;
	}

	
	
	public boolean validarUsuarioDescarga(Connection con, String esquema, String usrAcceso){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isCorrecta = false;
		
		try{
			stmt = con.prepareStatement(RegistroQuerys.getValidarUsuarioDescarga(esquema));
			stmt.setString(1, usrAcceso);
			stmt.setString(2, "SIAREX247");
			stmt.setString(3, "A");
			stmt.setString(4, "S");
			
			logger.info("stmt===>"+stmt);
			rs = stmt.executeQuery();
			if (rs.next()){
				isCorrecta = true;
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (rs != null){
					rs.close();
				}
				rs = null;
				if (stmt != null){
					stmt.close();
				}
				stmt = null;
			}catch(Exception e){
				rs = null;
				stmt = null;
			}
		}
		return isCorrecta;
	}
	
	
}