package com.siarex247.catalogos.ProvExternos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class ProvExternosBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public ArrayList<ProvExternosForm> detalleProveedores(Connection con, String esquema ){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<ProvExternosForm> resultado = new ArrayList<ProvExternosForm>();
        ProvExternosForm proExtForm = new ProvExternosForm();

        try{
        	StringBuffer sbQuery = new StringBuffer(ProvExternosQuerys.getDetalleProveedor(esquema));
        	
            stmt = con.prepareStatement(sbQuery.toString());
            rs = stmt.executeQuery();

            while(rs.next()) {
            	proExtForm.setClaveRegistro(rs.getInt(1));
            	proExtForm.setRazonSocial(Utils.noNulo(rs.getString(2)));
            	proExtForm.setRfc(Utils.noNulo(rs.getString(3)));
            	proExtForm.setCalle(Utils.noNulo(rs.getString(4)));
            	proExtForm.setNumeroInt(Utils.noNulo(rs.getString(5)));
            	proExtForm.setNumeroExt(Utils.noNulo(rs.getString(6)));
            	proExtForm.setColonia(Utils.noNulo(rs.getString(7)));
            	proExtForm.setDelegacion(Utils.noNulo(rs.getString(8)));
            	proExtForm.setCiudad(Utils.noNulo(rs.getString(9)));
            	proExtForm.setEstado(Utils.noNulo(rs.getString(10)));
            	proExtForm.setCodigoPostal(rs.getInt(11));
            	proExtForm.setTelefono(Utils.noNulo(rs.getString(12)));
            	proExtForm.setNombreContacto(Utils.noNulo(rs.getString(13)));
            	proExtForm.setEmail(Utils.noNulo(rs.getString(14)));
            	proExtForm.setIdProveedor(Utils.noNulo(rs.getString(15)));
				resultado.add(proExtForm);
				proExtForm = new ProvExternosForm();	
            }
        }
        catch(Exception e){
            Utils.imprimeLog("detalleProveedores(): ", e);
        }finally{
	        try{
	            if(rs != null) {
	                rs.close();
	            }
	            rs = null;
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return resultado;
    }
	
	
	
	public ProvExternosForm consultarProveedor(Connection con, String esquema, int claveRegistro ){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ProvExternosForm proExtForm = new ProvExternosForm();

        try{
        	stmt = con.prepareStatement(ProvExternosQuerys.getConsultarProveedor(esquema));
        	stmt.setInt(1, claveRegistro);
            rs = stmt.executeQuery();

            if (rs.next()) {
            	proExtForm.setClaveRegistro(rs.getInt(1));
            	proExtForm.setRazonSocial(Utils.noNulo(rs.getString(2)));
            	proExtForm.setRfc(Utils.noNulo(rs.getString(3)));
            	proExtForm.setCalle(Utils.noNulo(rs.getString(4)));
            	proExtForm.setNumeroInt(Utils.noNulo(rs.getString(5)));
            	proExtForm.setNumeroExt(Utils.noNulo(rs.getString(6)));
            	proExtForm.setColonia(Utils.noNulo(rs.getString(7)));
            	proExtForm.setDelegacion(Utils.noNulo(rs.getString(8)));
            	proExtForm.setCiudad(Utils.noNulo(rs.getString(9)));
            	proExtForm.setEstado(Utils.noNulo(rs.getString(10)));
            	proExtForm.setCodigoPostal(rs.getInt(11));
            	proExtForm.setTelefono(Utils.noNulo(rs.getString(12)));
            	proExtForm.setNombreContacto(Utils.noNulo(rs.getString(13)));
            	proExtForm.setEmail(Utils.noNuloNormal(rs.getString(14)));
            	proExtForm.setIdProveedor(Utils.noNulo(rs.getString(15)));
            	proExtForm.setIdReceptor(rs.getInt(16));
					
            }
        }
        catch(Exception e){
            Utils.imprimeLog("consultarProveedor(): ", e);
        }finally{
	        try{
	            if(rs != null) {
	                rs.close();
	            }
	            rs = null;
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return proExtForm;
    }
	
	
	
	public int altaProveedores(Connection con, String esquema, ProvExternosForm ProveedoresExtForm ){
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try{
        	stmt = con.prepareStatement(ProvExternosQuerys.getAltaProveedor(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
        	stmt.setString(1, ProveedoresExtForm.getRazonSocial().toUpperCase());
            stmt.setString(2, ProveedoresExtForm.getRfc().toUpperCase());
            stmt.setString(3, ProveedoresExtForm.getCalle().toUpperCase());
            stmt.setString(4, ProveedoresExtForm.getNumeroInt().toUpperCase());
            stmt.setString(5, ProveedoresExtForm.getNumeroExt().toUpperCase());
            stmt.setString(6, ProveedoresExtForm.getColonia().toUpperCase());
            stmt.setString(7, ProveedoresExtForm.getDelegacion().toUpperCase());
            stmt.setString(8, ProveedoresExtForm.getCiudad().toUpperCase());
            stmt.setString(9, ProveedoresExtForm.getEstado().toUpperCase());
            stmt.setInt(10, ProveedoresExtForm.getCodigoPostal());
            stmt.setString(11, ProveedoresExtForm.getTelefono().toUpperCase());
            stmt.setString(12, ProveedoresExtForm.getNombreContacto().toUpperCase());
            stmt.setString(13, ProveedoresExtForm.getEmail());
            stmt.setString(14, ProveedoresExtForm.getIdProveedor().toUpperCase());
            stmt.setInt(15, ProveedoresExtForm.getIdReceptor());
            
            int cant = stmt.executeUpdate();
            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next()) {
                    resultado = rs.getInt(1);
                }
            }
        }
        catch(SQLException sql){
        	resultado = sql.getErrorCode();
        	Utils.imprimeLog("altaProveedores ", sql);
            
        }catch(Exception e){
        	resultado = 100;
            Utils.imprimeLog("altaProveedores 2 ", e);
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
        return resultado;
    }
	
	
	
	public int actualizaProveedores(Connection con, String esquema, ProvExternosForm ProveedoresExtForm ) {
        PreparedStatement stmt = null;
        int resultado = 0;
        try{
        	stmt = con.prepareStatement(ProvExternosQuerys.getActualizaProveedor(esquema));

            stmt.setString(1, ProveedoresExtForm.getRazonSocial().toUpperCase());
            stmt.setString(2, ProveedoresExtForm.getRfc().toUpperCase());
            stmt.setString(3, ProveedoresExtForm.getCalle().toUpperCase());
            stmt.setString(4, ProveedoresExtForm.getNumeroInt().toUpperCase());
            stmt.setString(5, ProveedoresExtForm.getNumeroExt().toUpperCase());
            stmt.setString(6, ProveedoresExtForm.getColonia().toUpperCase());
            stmt.setString(7, ProveedoresExtForm.getDelegacion().toUpperCase());
            stmt.setString(8, ProveedoresExtForm.getCiudad().toUpperCase());
            stmt.setString(9, ProveedoresExtForm.getEstado().toUpperCase());
            stmt.setInt(10, ProveedoresExtForm.getCodigoPostal());
            stmt.setString(11, ProveedoresExtForm.getTelefono().toUpperCase());
            stmt.setString(12, ProveedoresExtForm.getNombreContacto().toUpperCase());
            stmt.setString(13, ProveedoresExtForm.getEmail());
            stmt.setString(14, ProveedoresExtForm.getIdProveedor().toUpperCase());
            stmt.setInt(15, ProveedoresExtForm.getClaveRegistro());
            resultado = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("actualizaProveedores(): ", e);
        }
        finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return resultado;
    }
	
	
	public int eliminaProveedores(Connection con, String esquema, int claveProveedor ){
        PreparedStatement stmt = null;
        int resultado = 0;
        try
        {
        	stmt = con.prepareStatement(ProvExternosQuerys.getEliminaProveedor(esquema));
            stmt.setInt(1, claveProveedor);
            resultado = stmt.executeUpdate();
        }
        catch(Exception e){
        	Utils.imprimeLog("eliminaProveedores ", e);
        }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return resultado;
    }
	
}
