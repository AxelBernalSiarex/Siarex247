package com.siarex247.visor.VisorOrdenes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.configuraciones.ConfClaveUsoCFDI.ConfClaveUsoCFDIForm;
import com.siarex247.utils.Utils;


public class VisorClavesCFDI {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	 public String isMultiple(Connection con, String esquema, long folioEmpresa) {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        String idMultiple = "";
	        try{
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryIsMultiple(esquema));
	        	stmt.setLong(1, folioEmpresa);
	            rs = stmt.executeQuery();
	            if(rs.next()){
	            	idMultiple = Utils.noNulo(rs.getString(1));
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
	        return idMultiple;
	    }
	 
	 

		public VisorOrdenesForm getDescripciones(Connection con, String esquema, long folioEmpresa) {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        VisorOrdenesForm visorForm = new VisorOrdenesForm();
	        try{
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryBuscarClavePorOrdenXML(esquema));
	        	stmt.setLong(1, folioEmpresa);
	            rs = stmt.executeQuery();
	            String usoCFDI = null;
	            if(rs.next()){
	            	usoCFDI = Utils.noNulo(rs.getString(2));
	            	visorForm.setUsoCFDI(usoCFDI);
	            	visorForm.setDesUsoCFDI(getDescripcionUSOCFDI(con, esquema, usoCFDI));
	            }
	            
	            try {
	            	rs.close();
	            	stmt.close();
	            }catch(Exception e) {
	            	Utils.imprimeLog("", e);
	            }
	            
	            
	            try {
	            	rs.close();
	            	stmt.close();
	            }catch(Exception e) {
	            	Utils.imprimeLog("", e);
	            }
	            
	            
	            
	            
	            stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryBuscarProveedor(esquema));
	        	stmt.setLong(1, folioEmpresa);
	            rs = stmt.executeQuery();
	            if(rs.next()){
	            	
	            	visorForm.setRfc(Utils.noNulo(rs.getString(1)));
	            	visorForm.setRazonSocial(Utils.noNulo(rs.getString(2)));
	            	visorForm.setNombreXML(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(3))));
	            	visorForm.setNombrePDF(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(4))));
	            	visorForm.setClaveProveedor(rs.getInt(5));
	            	visorForm.setServicioRecibido(Utils.noNulo(rs.getString(6)));
	            	
	            	
	            }
	            visorForm.setFolioEmpresa(folioEmpresa);
	            
	            try {
	            	rs.close();
	            	stmt.close();
	            }catch(Exception e) {
	            	Utils.imprimeLog("", e);
	            }
	            
	            
	            stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryUsoCFDIXML(esquema));
	        	
	            stmt.setString(1, visorForm.getRfc());
	        	stmt.setString(2, visorForm.getUsoCFDI());
	            rs = stmt.executeQuery();
	            
	            if(rs.next()){
	            	visorForm.setBandActivoCDFI("A");
	            }else {
	            	visorForm.setBandActivoCDFI("D");
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
	        return visorForm;
	    }
	 
	 
	 public String getDescripcionUSOCFDI(Connection con, String esquema, String idUsoCFDI)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        String desUsoCFDI = null;
	        try{
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryDesUsoCFDI(esquema));
	        	stmt.setString(1, idUsoCFDI);
	            rs = stmt.executeQuery();
	            if(rs.next()){
	            	desUsoCFDI = Utils.noNulo(rs.getString(1));
	            }else {
	            	desUsoCFDI = "SIN DESCRIPCION";
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
	        return desUsoCFDI;
	    }
	 
	 
	 
		public VisorOrdenesForm getDescripcionesMultiple(Connection con, String esquema, String idMultiple, long folioEmpresa)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        VisorOrdenesForm visorForm = new VisorOrdenesForm();
	        try{
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryBuscarClavePorOrdenXML_Multiple(esquema));
	        	stmt.setString(1, idMultiple);
	            rs = stmt.executeQuery();
	            String usoCFDI = null;
	            if(rs.next()){
	            	usoCFDI = Utils.noNulo(rs.getString(2));
	            	visorForm.setUsoCFDI(usoCFDI);
	            	visorForm.setDesUsoCFDI(getDescripcionUSOCFDI(con, esquema, usoCFDI));
	            	
	            }
	            
	            try {
	            	rs.close();
	            	stmt.close();
	            }catch(Exception e) {
	            	Utils.imprimeLog("", e);
	            }
	            
	            
	            try {
	            	rs.close();
	            	stmt.close();
	            }catch(Exception e) {
	            	Utils.imprimeLog("", e);
	            }
	            
	            
	            stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryBuscarProveedor(esquema));
	        	stmt.setLong(1, folioEmpresa);
	            rs = stmt.executeQuery();
	            if(rs.next()){
	            	visorForm.setRfc(Utils.noNulo(rs.getString(1)));
	            	visorForm.setRazonSocial(Utils.noNulo(rs.getString(2)));
	            	visorForm.setNombreXML(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(3))));
	            	visorForm.setNombrePDF(Utils.eliminaCaracter(Utils.noNuloNormal(rs.getString(4))));
	            	visorForm.setClaveProveedor(rs.getInt(5));
	            	visorForm.setServicioRecibido(Utils.noNulo(rs.getString(6)));
	            	
	            	
	            }
	            visorForm.setFolioEmpresa(folioEmpresa);
	            
	            try {
	            	rs.close();
	            	stmt.close();
	            }catch(Exception e) {
	            	Utils.imprimeLog("", e);
	            }
	            
	            stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryUsoCFDIXML(esquema));
	        	stmt.setString(1, visorForm.getRfc());
	        	stmt.setString(2, visorForm.getUsoCFDI());
	        	
	            rs = stmt.executeQuery();
	            
	            if(rs.next()){
	            	visorForm.setBandActivoCDFI("A");
	            }else {
	            	visorForm.setBandActivoCDFI("D");
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
	        return visorForm;
	    }
		
		
		public ArrayList<ConfClaveUsoCFDIForm> buscarClavePorOrden(Connection con, String esquema, long folioEmpresa, String rfc, String usoCFDI) {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        ConfClaveUsoCFDIForm confUsoForm = new ConfClaveUsoCFDIForm();
	        ArrayList<ConfClaveUsoCFDIForm> listaDetalle = new ArrayList<>();
	        
	        try{
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryBuscarClavePorOrden(esquema));
	        	stmt.setLong(1, folioEmpresa);
	        	stmt.setString(2, rfc);
	        	stmt.setString(3, usoCFDI);
	            rs = stmt.executeQuery();
	            while(rs.next()){
	            	confUsoForm.setIdRegistro(rs.getInt(1));
	            	confUsoForm.setClaveProduto(Utils.noNulo(rs.getString(2)));
	            	confUsoForm.setDescripcionXML(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(3))));
	            	confUsoForm.setDescripcionSAT(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(4))));
					listaDetalle.add(confUsoForm);
					confUsoForm = new ConfClaveUsoCFDIForm();
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
		
		
		
		public ArrayList<ConfClaveUsoCFDIForm> buscarClavePorOrdenSinUso(Connection con, String esquema, long folioEmpresa, String rfc, String usoCFDI)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        ConfClaveUsoCFDIForm confUsoForm = new ConfClaveUsoCFDIForm();
	        ArrayList<ConfClaveUsoCFDIForm> listaDetalle = new ArrayList<>();
	        
	        try{
	        	
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryBuscarClavePorOrdenSinUSO(esquema));
	        	stmt.setLong(1, folioEmpresa);
	        	//stmt.setString(2, rfc);
	            rs = stmt.executeQuery();
	            while(rs.next()){
						confUsoForm.setIdRegistro(rs.getInt(1));
		            	confUsoForm.setClaveProduto(Utils.noNulo(rs.getString(2)));
		            	confUsoForm.setDescripcionXML(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(3))));
		            	confUsoForm.setDescripcionSAT(Utils.regresaCaracteresNormales(Utils.noNuloNormal(rs.getString(4))));
						listaDetalle.add(confUsoForm);
						confUsoForm = new ConfClaveUsoCFDIForm();
						
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
		
		
		
		public ArrayList<ConfClaveUsoCFDIForm> buscarClavePorOrdenMultiple(Connection con, String esquema, String idMultiple,  String rfc, String usoCFDI)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        ConfClaveUsoCFDIForm confUsoForm = new ConfClaveUsoCFDIForm();
	        ArrayList<ConfClaveUsoCFDIForm> listaDetalle = new ArrayList<>();
	        try{
        	
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryBuscarClavePorOrden_Multiple(esquema));
	        	stmt.setString(1, idMultiple);
	        	stmt.setString(2, rfc);
	        	stmt.setString(3, usoCFDI);
	            rs = stmt.executeQuery();
	            String claveProductoServicio = "";
	            String claveProductoServicioTMP = "";
	            
	            while(rs.next()){
	            	claveProductoServicio = Utils.noNulo(rs.getString(2));
	            	if ("".equals(claveProductoServicioTMP) || !claveProductoServicioTMP.equalsIgnoreCase(claveProductoServicio)) {
	            		confUsoForm.setIdRegistro(rs.getInt(1));
		            	confUsoForm.setClaveProduto(Utils.noNulo(rs.getString(2)));
		            	confUsoForm.setDescripcionXML(Utils.noNuloNormal(rs.getString(3)));
		            	confUsoForm.setDescripcionSAT(Utils.noNuloNormal(rs.getString(4)));
						listaDetalle.add(confUsoForm);
						confUsoForm = new ConfClaveUsoCFDIForm();
						
	            	}
	            	claveProductoServicioTMP = claveProductoServicio;
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
		
		
		public int guardarClavesProducto(Connection con, String esquema, long folioEmpresa, String rfc,  String usoCFDI, String usuarioHTTP)
	    {
	        PreparedStatement stmt = null;
	        PreparedStatement stmtInsert = null;
	        ResultSet rs = null;
	        int totGuardados = 0;
	        try{
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryValidaCLAVE(esquema));
	            
	        	stmt.setLong(1, folioEmpresa);
	        	stmt.setString(2, rfc);
	        	stmt.setString(3, usoCFDI);
	        	
	        	
	            rs = stmt.executeQuery();
	            
	            stmtInsert = con.prepareStatement(VisorClavesCFDIQuerys.getQueryInsertUso(esquema, ""));
	            stmtInsert.setString(1, rfc);
	        	stmtInsert.setString(2, usoCFDI);
	        	stmtInsert.setString(4, usuarioHTTP);
	        	        	
	            String claveProducto = null;
	            while(rs.next()){
	            	try {
	            		claveProducto = Utils.noNulo(rs.getString(1));
	                	stmtInsert.setString(3, claveProducto);
	                	stmtInsert.executeUpdate();
	                	totGuardados++;
	            	}catch(Exception e) {
	            		Utils.imprimeLog("", e);
	            	}
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
		            if(stmtInsert != null)
		            	stmtInsert.close();
		            stmtInsert = null;
		        }catch(Exception e){
		            stmt = null;
		            stmtInsert = null;
		        }
	        }
	        return totGuardados;
	    }
		
		
		public int eliminaClavesProducto(Connection con, String esquema, long folioEmpresa)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int totGuardados = 0;
	        try{
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryDeleteClaveProductoXML(esquema, ""));
	        	stmt.setLong(1, folioEmpresa);
	            stmt.executeUpdate();
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
	        return totGuardados;
	    }
		
		
		public String getTodasOrdenesMultiple(Connection con, String esquema, String idMultiple)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        StringBuffer sbOrdenes = new StringBuffer();
	        String todasOrdenes = "";
	        try{
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQuerySelectOrdenes_Multiple(esquema));
	        	stmt.setString(1, idMultiple);
	            rs = stmt.executeQuery();
	            while(rs.next()){
	            	sbOrdenes.append(Utils.noNulo(rs.getString(1))).append(",");
	            }
	            
	            logger.info("sbOrdenes----->"+sbOrdenes);
	            if (sbOrdenes.length() > 0) {
	            	todasOrdenes = sbOrdenes.substring(0, sbOrdenes.length() - 1);
	            }
	            logger.info("todasOrdenes----->"+todasOrdenes);
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
	        return todasOrdenes;
	    }
		
		
		public int guardarClavesProductoMultiple(Connection con, String esquema, String idMultiple, String rfc,  String usoCFDI, String usuarioHTTP)
	    {
	        PreparedStatement stmt = null;
	        PreparedStatement stmtInsert = null;
	        ResultSet rs = null;
	        int totGuardados = 0;
	        try{
	        	stmt = con.prepareStatement(VisorClavesCFDIQuerys.getQueryValidaCLAVE_Multiple(esquema));
	            
	        	stmt.setString(1, idMultiple);
	        	stmt.setString(2, rfc);
	        	stmt.setString(3, usoCFDI);
	            rs = stmt.executeQuery();
	            
	            stmtInsert = con.prepareStatement(VisorClavesCFDIQuerys.getQueryInsertUso(esquema, ""));
	            stmtInsert.setString(1, rfc);
	        	stmtInsert.setString(2, usoCFDI);
	        	stmtInsert.setString(4, usuarioHTTP);
	        	
	            String claveProducto = null;
	            while(rs.next()){
	            	try {
	            		claveProducto = Utils.noNulo(rs.getString(1));
	                	stmtInsert.setString(3, claveProducto);
	                	stmtInsert.executeUpdate();
	                	totGuardados++;
	            	}catch(Exception e) {
	            		Utils.imprimeLog("", e);
	            	}
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
		            if(stmtInsert != null)
		            	stmtInsert.close();
		            stmtInsert = null;
		        }catch(Exception e){
		            stmt = null;
		            stmtInsert = null;
		        }
	        }
	        return totGuardados;
	    }
}
