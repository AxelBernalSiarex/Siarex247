package com.siarex247.catalogos.ConstanciaSituacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class ConstanciaSituacionBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	

	public ArrayList<ConstanciaSituacionForm> detalleConstancias(Connection con, String esquema, int claveEmpresa) {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        ArrayList<ConstanciaSituacionForm> listaDetalle = new ArrayList<>();
	        ConstanciaSituacionForm constForm = new ConstanciaSituacionForm();
	        try {
	        	StringBuffer sbQuery = new StringBuffer(ConstanciaSituacionQuerys.getDetalle(esquema));
	            stmt = con.prepareStatement(sbQuery.toString());
	            stmt.setInt(1, claveEmpresa);
	            stmt.setString(2, "A");
	            logger.info("stmt===>"+stmt);
	            rs = stmt.executeQuery();
				while(rs.next()){
					constForm.setIdRegistro(rs.getInt(1));
					constForm.setRfc(Utils.noNulo(rs.getString(2)));
					constForm.setRazonSocial(Utils.noNulo(rs.getString(3)));
					constForm.setCedulaFiscal(Utils.noNulo(rs.getString(4)));
					constForm.setRegimenCapital(Utils.noNulo(rs.getString(5)));
					constForm.setNombreEmpleado(Utils.noNulo(rs.getString(6)));
					constForm.setApellidoPaterno(Utils.noNulo(rs.getString(7)));
					constForm.setApellidoMaterno(Utils.noNulo(rs.getString(8)));
					constForm.setFechaNacimiento(Utils.noNulo(rs.getString(9)));
					constForm.setSituacionContribuyente(Utils.noNulo(rs.getString(10)));
					constForm.setFechaUltCambioSituacion(Utils.noNulo(rs.getString(11)));
					constForm.setCurp(Utils.noNulo(rs.getString(12)));
					constForm.setEntidadFederativa(Utils.noNulo(rs.getString(13)));
					constForm.setMunicipio(Utils.noNulo(rs.getString(14)));
					constForm.setColonia(Utils.noNulo(rs.getString(15)));
					constForm.setTipoVialidad(Utils.noNulo(rs.getString(16)));
					constForm.setNombreVialidad(Utils.noNulo(rs.getString(17)));
					constForm.setNumeroExt(Utils.noNulo(rs.getString(18)));
					constForm.setNumeroInt(Utils.noNulo(rs.getString(19)));
					constForm.setCodigoPostal(rs.getInt(20));
					constForm.setFechaOperaciones(Utils.noNulo(rs.getString(21)));
					constForm.setCorreoElectronico(Utils.noNuloNormal(rs.getString(22)));
					constForm.setClaveRegimen(Utils.noNulo(rs.getString(23)));
					constForm.setRegimen(Utils.noNulo(rs.getString(23)) + " - " + Utils.noNulo(rs.getString(24)));
					constForm.setFechaAlta(Utils.noNulo(rs.getString(25)));
					
					// constForm.setIdProveedor(rs.getInt(14));
					listaDetalle.add(constForm);
					constForm = new ConstanciaSituacionForm();
	            }
				
	        }catch(Exception e){
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
	 
	public ConstanciaSituacionForm consultarConstancia(Connection con, String esquema, int idRegistro) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ConstanciaSituacionForm constForm = new ConstanciaSituacionForm();
        try {
        	StringBuffer sbQuery = new StringBuffer(ConstanciaSituacionQuerys.getConsulta(esquema));
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setInt(1, idRegistro);
            //logger.info("stmt==>"+stmt);
            rs = stmt.executeQuery();
			if (rs.next()){
				constForm.setIdRegistro(rs.getInt(1));
				constForm.setRfc(Utils.noNulo(rs.getString(2)));
				constForm.setRazonSocial(Utils.noNulo(rs.getString(3)));
				constForm.setCedulaFiscal(Utils.noNulo(rs.getString(4)));
				constForm.setRegimenCapital(Utils.noNulo(rs.getString(5)));
				constForm.setNombreEmpleado(Utils.noNulo(rs.getString(6)));
				constForm.setApellidoPaterno(Utils.noNulo(rs.getString(7)));
				constForm.setApellidoMaterno(Utils.noNulo(rs.getString(8)));
				constForm.setFechaNacimiento(Utils.noNulo(rs.getString(9)));
				constForm.setSituacionContribuyente(Utils.noNulo(rs.getString(10)));
				constForm.setFechaUltCambioSituacion(Utils.noNulo(rs.getString(11)));
				constForm.setCurp(Utils.noNulo(rs.getString(12)));
				constForm.setEntidadFederativa(Utils.noNulo(rs.getString(13)));
				constForm.setMunicipio(Utils.noNulo(rs.getString(14)));
				constForm.setColonia(Utils.noNulo(rs.getString(15)));
				constForm.setTipoVialidad(Utils.noNulo(rs.getString(16)));
				constForm.setNombreVialidad(Utils.noNulo(rs.getString(17)));
				constForm.setNumeroExt(Utils.noNulo(rs.getString(18)));
				constForm.setNumeroInt(Utils.noNulo(rs.getString(19)));
				constForm.setCodigoPostal(rs.getInt(20));
				constForm.setFechaOperaciones(Utils.noNulo(rs.getString(21)));
				constForm.setCorreoElectronico(Utils.noNuloNormal(rs.getString(22)));
				constForm.setClaveRegimen(Utils.noNulo(rs.getString(23)));
				constForm.setRegimen(Utils.noNulo(rs.getString(23)) + " - " + Utils.noNulo(rs.getString(24)));
				constForm.setFechaAlta(Utils.noNulo(rs.getString(25)));
            }
			
        }catch(Exception e){
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
        return constForm;
    }
	
	
	public ConstanciaSituacionForm consultarConstanciaRFC(Connection con, String esquema, int claveEmpresa, String rfc) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ConstanciaSituacionForm constForm = new ConstanciaSituacionForm();
        try {
        	StringBuffer sbQuery = new StringBuffer(ConstanciaSituacionQuerys.getConsultaRFC(esquema));
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setInt(1, claveEmpresa);
            stmt.setString(2, rfc);
            stmt.setString(3, "A");
            logger.info("stmt==>"+stmt);
            rs = stmt.executeQuery();
			if (rs.next()){
				constForm.setIdRegistro(rs.getInt(1));
				constForm.setRfc(Utils.noNulo(rs.getString(2)));
				constForm.setRazonSocial(Utils.noNulo(rs.getString(3)));
				constForm.setCedulaFiscal(Utils.noNulo(rs.getString(4)));
				constForm.setRegimenCapital(Utils.noNulo(rs.getString(5)));
				constForm.setNombreEmpleado(Utils.noNulo(rs.getString(6)));
				constForm.setApellidoPaterno(Utils.noNulo(rs.getString(7)));
				constForm.setApellidoMaterno(Utils.noNulo(rs.getString(8)));
				constForm.setFechaNacimiento(Utils.noNulo(rs.getString(9)));
				constForm.setSituacionContribuyente(Utils.noNulo(rs.getString(10)));
				constForm.setFechaUltCambioSituacion(Utils.noNulo(rs.getString(11)));
				constForm.setCurp(Utils.noNulo(rs.getString(12)));
				constForm.setEntidadFederativa(Utils.noNulo(rs.getString(13)));
				constForm.setMunicipio(Utils.noNulo(rs.getString(14)));
				constForm.setColonia(Utils.noNulo(rs.getString(15)));
				constForm.setTipoVialidad(Utils.noNulo(rs.getString(16)));
				constForm.setNombreVialidad(Utils.noNulo(rs.getString(17)));
				constForm.setNumeroExt(Utils.noNulo(rs.getString(18)));
				constForm.setNumeroInt(Utils.noNulo(rs.getString(19)));
				constForm.setCodigoPostal(rs.getInt(20));
				constForm.setFechaOperaciones(Utils.noNulo(rs.getString(21)));
				constForm.setCorreoElectronico(Utils.noNuloNormal(rs.getString(22)));
				constForm.setClaveRegimen(Utils.noNulo(rs.getString(23)));
				constForm.setRegimen(Utils.noNulo(rs.getString(23)) + " - " + Utils.noNulo(rs.getString(24)));
				constForm.setFechaAlta(Utils.noNulo(rs.getString(25)));
            }
			
        }catch(Exception e){
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
        return constForm;
    }
	
	
	public ConstanciaSituacionForm consultarConstanciaPDF(Connection con, String esquema, int idRegistro, String cedulaFiscal) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ConstanciaSituacionForm constForm = new ConstanciaSituacionForm();
        try {
        	StringBuffer sbQuery = new StringBuffer(ConstanciaSituacionQuerys.getConsultaPDF(esquema));
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setInt(1, idRegistro);
            stmt.setString(2, cedulaFiscal);
            rs = stmt.executeQuery();
			if(rs.next()){
				constForm.setRfc(Utils.noNulo(rs.getString(1)));
			}
			
        }catch(Exception e){
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
        return constForm;
    }
 
	
	public String consultarRegimen(Connection con, String esquema, String descripcionSAT) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String claveRegimen = "";
        try {
        	StringBuffer sbQuery = new StringBuffer(ConstanciaSituacionQuerys.getConsultaRegimen(esquema));
            stmt = con.prepareStatement(sbQuery.toString());
            stmt.setString(1, descripcionSAT.toUpperCase());
            rs = stmt.executeQuery();
			if(rs.next()){
				claveRegimen = Utils.noNulo(rs.getString(1));
			}
			
        }catch(Exception e){
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
        return claveRegimen;
    }
	
	 
	public int guardarCedulaFiscal(Connection con, String esquema, int claveEmpresa, ConstanciaSituacionForm constForm, String usuarioHTTP)
	{
		PreparedStatement stmt = null;
		int resultado = 0;
		
		try {
			int numParam = 1;
			stmt = con.prepareStatement(ConstanciaSituacionQuerys.getUpdateCedulaFiscal(esquema));
			stmt.setString(numParam++, constForm.getRazonSocial());
			stmt.setString(numParam++, constForm.getCedulaFiscal());
			stmt.setString(numParam++, constForm.getRegimenCapital());
			stmt.setString(numParam++, constForm.getNombreEmpleado());
			stmt.setString(numParam++, constForm.getApellidoPaterno());
			stmt.setString(numParam++, constForm.getApellidoMaterno());
			stmt.setString(numParam++, constForm.getFechaNacimiento());
			stmt.setString(numParam++, constForm.getSituacionContribuyente());
			stmt.setString(numParam++, constForm.getFechaUltCambioSituacion());
			stmt.setString(numParam++, constForm.getCurp());
			stmt.setString(numParam++, constForm.getEntidadFederativa());
			stmt.setString(numParam++, constForm.getMunicipio());
			stmt.setString(numParam++, constForm.getColonia());
			stmt.setString(numParam++, constForm.getTipoVialidad());
			stmt.setString(numParam++, constForm.getNombreVialidad());
			stmt.setString(numParam++, constForm.getNumeroExt());
			stmt.setString(numParam++, constForm.getNumeroInt());
			stmt.setInt(numParam++, constForm.getCodigoPostal());
			stmt.setString(numParam++, constForm.getFechaOperaciones());
			stmt.setString(numParam++, constForm.getCorreoElectronico());
			stmt.setString(numParam++, constForm.getClaveRegimen());
			stmt.setString(numParam++, constForm.getRegimen());
			stmt.setString(numParam++, constForm.getFechaAlta());
			stmt.setString(numParam++, usuarioHTTP);
			stmt.setInt(numParam++, claveEmpresa);
			stmt.setString(numParam++, constForm.getRfc());
			stmt.setString(numParam++, "A");
			int cant = stmt.executeUpdate();
			
			if (cant == 0) {
				stmt.close();
				numParam = 1;	
				stmt = con.prepareStatement(ConstanciaSituacionQuerys.getInsertarCedulaFiscal(esquema));
				stmt.setInt(numParam++, claveEmpresa);
				stmt.setString(numParam++, constForm.getRfc());
				stmt.setString(numParam++, constForm.getRazonSocial());
				stmt.setString(numParam++, constForm.getCedulaFiscal());
				stmt.setString(numParam++, constForm.getRegimenCapital());
				stmt.setString(numParam++, constForm.getNombreEmpleado());
				stmt.setString(numParam++, constForm.getApellidoPaterno());
				stmt.setString(numParam++, constForm.getApellidoMaterno());
				stmt.setString(numParam++, constForm.getFechaNacimiento());
				stmt.setString(numParam++, constForm.getSituacionContribuyente());
				stmt.setString(numParam++, constForm.getFechaUltCambioSituacion());
				stmt.setString(numParam++, constForm.getCurp());
				stmt.setString(numParam++, constForm.getEntidadFederativa());
				stmt.setString(numParam++, constForm.getMunicipio());
				stmt.setString(numParam++, constForm.getColonia());
				stmt.setString(numParam++, constForm.getTipoVialidad());
				stmt.setString(numParam++, constForm.getNombreVialidad());
				stmt.setString(numParam++, constForm.getNumeroExt());
				stmt.setString(numParam++, constForm.getNumeroInt());
				stmt.setInt(numParam++, constForm.getCodigoPostal());
				stmt.setString(numParam++, constForm.getFechaOperaciones());
				stmt.setString(numParam++, constForm.getCorreoElectronico());
				stmt.setString(numParam++, constForm.getClaveRegimen());
				stmt.setString(numParam++, constForm.getRegimen());
				stmt.setString(numParam++, constForm.getFechaAlta());
				stmt.setString(numParam++, usuarioHTTP);
				
				stmt.executeUpdate();
				
			}
			
		} catch (SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		} catch (Exception e) {
			resultado = -100;
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			} catch (Exception e) {
				stmt = null;
			}
		}
		
		return resultado;
	}
	 
	
	public int eliminar(Connection con, String esquema, int idRegistro, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try{ 
			stmt = con.prepareStatement(ConstanciaSituacionQuerys.getElimina(esquema));
			stmt.setString(1, "D");
			stmt.setString(2, usuarioHTTP);
			stmt.setInt(3, idRegistro);
			resultado = stmt.executeUpdate();
            
		}catch(SQLException sql){
			resultado = sql.getErrorCode();
			Utils.imprimeLog("", sql);
		}catch(Exception e){
			Utils.imprimeLog("", e);
			resultado = 100;
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
				stmt = null;
			}
		}
		return resultado;

	}
	
}
