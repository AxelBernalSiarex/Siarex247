package com.siarex247.layOut.OrdenesCompra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class OrdenesCompraBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	public ArrayList<OrdenesCompraForm> detalleHistoricoTotal(Connection con, String esquema, int perfilUsuario, String usuario)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<OrdenesCompraForm> resultado = new ArrayList<OrdenesCompraForm>();
        OrdenesCompraForm ordenesCompraForm = new OrdenesCompraForm();
        try
        {
        	if (perfilUsuario == 1){
        		stmt = con.prepareStatement(OrdenesCompraQuerys.getQueryDetalleHistorico(esquema));	
        	}else{
        		stmt = con.prepareStatement(OrdenesCompraQuerys.getDetalleHistoricoPerfil(esquema));
        		stmt.setString(1, usuario);
        	}
        	
            rs = stmt.executeQuery();
            while(rs.next()) {
					ordenesCompraForm.setClaveRegistro(rs.getInt(1));
					ordenesCompraForm.setNombreArchivo(Utils.noNuloNormal(rs.getString(2)));
					ordenesCompraForm.setTotRegistros(rs.getInt(3));
					ordenesCompraForm.setTipoCarga(Utils.noNulo(rs.getString(4)));
					ordenesCompraForm.setTipoCargaDes(Utils.noNulo(rs.getString(4)).concat(" - ").concat(desTipoCarga(Utils.noNulo(rs.getString(4)))) );
					ordenesCompraForm.setTotOK(rs.getInt(5));
					ordenesCompraForm.setTotNG(rs.getInt(6));
					ordenesCompraForm.setEstatus(rs.getInt(7));
					ordenesCompraForm.setEstatusDes(desEstatus(rs.getInt(7)));
					ordenesCompraForm.setUsuarioTrans(Utils.noNuloNormal(rs.getString(8)));
					//ordenesCompraForm.setFechaTrans(UtilsFechas.getFechaddMMMyyyyHHss(rs.getTimestamp(9)));
					ordenesCompraForm.setFechaTrans(rs.getString(9));
					resultado.add(ordenesCompraForm);
					ordenesCompraForm = new OrdenesCompraForm();	
				
				
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
        return resultado;
    }
	
	
	public int grabarHistorial(Connection con, String esquema, 
			String nombreArchivo, int totRegistros, String tipoCarga, int estatusCarga, int regOK, int regNG, String usuarioTrans, String fechaPago)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try
        {
            stmt = con.prepareStatement(OrdenesCompraQuerys.getQueryHistorialCargas(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nombreArchivo);
            stmt.setInt(2, totRegistros);
            stmt.setString(3, tipoCarga);
            stmt.setInt(4, regOK);
            stmt.setInt(5, regNG);
            stmt.setString(6, usuarioTrans);
            stmt.setInt(7, estatusCarga);
            stmt.setString(8, fechaPago);
            int cant = stmt.executeUpdate();
             if(cant > 0){
                 rs = stmt.getGeneratedKeys();
                 if(rs.next())
                     resultado = rs.getInt(1);
             }
             
             
        }
        catch(Exception e){
            Utils.imprimeLog("", e);
            resultado = -100;
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
	
	
	public int grabarHistorialCorreo(String esquemaEmpresa, String nombreArchivo, int totRegistros, String tipoCarga, int estatusCarga, int regOK, int regNG, String usuarioTrans, String fechaPago)
    {
        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        Connection con = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try {
        	rc = connPool.getConnection(esquemaEmpresa);
        	con = rc.getCon();
            stmt = con.prepareStatement(OrdenesCompraQuerys.getQueryHistorialCargas(esquemaEmpresa), PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nombreArchivo);
            stmt.setInt(2, totRegistros);
            stmt.setString(3, tipoCarga);
            stmt.setInt(4, regOK);
            stmt.setInt(5, regNG);
            stmt.setString(6, usuarioTrans);
            stmt.setInt(7, estatusCarga);
            stmt.setString(8, fechaPago);
            int cant = stmt.executeUpdate();
             if(cant > 0){
                 rs = stmt.getGeneratedKeys();
                 if(rs.next())
                     resultado = rs.getInt(1);
             }
        } catch(Exception e){
            Utils.imprimeLog("", e);
            resultado = -100;
        }finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(con != null)
	                con.close();
	            con = null;
	        }catch(Exception e){
	            stmt = null;
	            con = null;
	        }
        }
        return resultado;
    }
	
	public int actualizaHistorial(Connection con, String esquema,int claveRegistro, int totRegistros, int estatusCarga, int regOK, int regNG, String usuarioTrans)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try
        {
            stmt = con.prepareStatement(OrdenesCompraQuerys.getQueryActualizaCargas(esquema));
            
            stmt.setInt(1, totRegistros);
            stmt.setInt(2, regOK);
            stmt.setInt(3, regNG);
            stmt.setString(4, usuarioTrans);
            stmt.setInt(5, estatusCarga);
            stmt.setInt(6, claveRegistro);
            resultado = stmt.executeUpdate();
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
           resultado = -100;
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
	
	
	public int actualizaHistorialCorreo(String esquemaEmpresa,int claveRegistro, int totRegistros, int estatusCarga, int regOK, int regNG, String usuarioTrans)
    {
		ConexionDB connPool = new ConexionDB();
        ResultadoConexion rc = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try
        {
        	rc = connPool.getConnection(esquemaEmpresa);
        	con = rc.getCon();
            stmt = con.prepareStatement(OrdenesCompraQuerys.getQueryActualizaCargas(esquemaEmpresa));
            
            stmt.setInt(1, totRegistros);
            stmt.setInt(2, regOK);
            stmt.setInt(3, regNG);
            stmt.setString(4, usuarioTrans);
            stmt.setInt(5, estatusCarga);
            stmt.setInt(6, claveRegistro);
            resultado = stmt.executeUpdate();
        }
        catch(Exception e){
           Utils.imprimeLog("", e);
           resultado = -100;
        }finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(con != null)
	                con.close();
	            con = null;
	        }catch(Exception e){
	            stmt = null;
	            con = null;
	        }
        }
        return resultado;
    }
	
	public int grabarHistorialDetallada(Connection con, String esquema, 
			int claveHistorico, long folioEmpresa, String desError, String tipoRegistro, String valorCarga)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        try
        {
            stmt = con.prepareStatement(OrdenesCompraQuerys.getQueryGrabaHistorialDetallada(esquema));
            stmt.setInt(1, claveHistorico);
            stmt.setLong(2, folioEmpresa);
            stmt.setString(3, tipoRegistro);
            stmt.setString(4, desError);
            stmt.setString(5, valorCarga);
            resultado = stmt.executeUpdate();
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
        return resultado;
    }

	
	public OrdenesCompraForm obtenerInformacionCarga(Connection con, String esquema, int claveRegistro)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OrdenesCompraForm ordenesCompraForm = new OrdenesCompraForm();
        try
        {

        	stmt = con.prepareStatement(OrdenesCompraQuerys.getObtenerNombreCarga(esquema));
        	stmt.setInt(1, claveRegistro);
            rs = stmt.executeQuery();
            if(rs.next()) {
            	ordenesCompraForm.setNombreArchivo(Utils.noNuloNormal(rs.getString(1)));
            	ordenesCompraForm.setFechaPago(Utils.noNulo(rs.getString(2)));
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
        return ordenesCompraForm;
    }
	


	public ArrayList<OrdenesCompraForm> detalleHistorico(Connection con, String esquema, int claveHistorico, String tipoReg)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<OrdenesCompraForm> resultado = new ArrayList<OrdenesCompraForm>();
        OrdenesCompraForm ordenesCompraForm = new OrdenesCompraForm();
        try
        {
            stmt = con.prepareStatement(OrdenesCompraQuerys.getQueryHistoricoDetallado(esquema));
            stmt.setInt(1, claveHistorico);
            rs = stmt.executeQuery();
            String tipoRegistro = null;
			while(rs.next()) 
            {
				tipoRegistro = Utils.noNulo(rs.getString(4));
				if (tipoRegistro.equalsIgnoreCase(tipoReg)){
					ordenesCompraForm.setClaveRegistro(rs.getInt(1));
					ordenesCompraForm.setFolioEmpresa(rs.getLong(2));
					ordenesCompraForm.setDesError(Utils.noNuloNormal(rs.getString(3)));
					resultado.add(ordenesCompraForm);
					ordenesCompraForm = new OrdenesCompraForm();	
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
        return resultado;
    }
	

	
	private String desTipoCarga(String numTipoCarga){
		if ("1".equals(numTipoCarga)){
			return "COMPRAS";
		}else if ("2".equals(numTipoCarga)){
			return "CONTRA RECIBOS";
		}else if ("3".equals(numTipoCarga)){
			return "ORDENES DE PAGO";
		}else if ("4".equals(numTipoCarga)){
			return "PROVEEDORES";
		}else if ("5".equals(numTipoCarga)){
			return "ELIMINAR ORDENES";
		}
		return "";
	}
	
	
	private String desEstatus(int numEstatus){
		if (numEstatus == 0){
			return "TERMINADO";
		}else if (numEstatus == 1){
			return "ADJUNTADO";
		}else if (numEstatus == 2){
			return "PROCESANDO";
		}else if (numEstatus == 3){
			return "ERROR";
		}
		return "";
	}
}
