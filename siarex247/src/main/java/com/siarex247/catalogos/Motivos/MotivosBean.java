package com.siarex247.catalogos.Motivos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.siarex247.utils.Utils;

public class MotivosBean {

	
		public ArrayList<MotivosForm> comboMotivos(Connection con, String esquema, String tipoMotivo)
		    {
		        PreparedStatement stmt = null;
		        ResultSet rs = null;
		        MotivosForm motivosForm = new MotivosForm();
		        ArrayList<MotivosForm> listaMotivos = new ArrayList<>();
		        
		        try{
		        	
		        	stmt = con.prepareStatement(MotivosQuerys.getQueryDetalleMotivos(esquema));
		            stmt.setString(1, tipoMotivo);
			        rs = stmt.executeQuery();
			        while (rs.next()){
			        	motivosForm.setIdRegistro(rs.getInt(1));
			        	motivosForm.setDescripcion(Utils.noNulo(rs.getString(2)));
			        	listaMotivos.add(motivosForm);
			        	motivosForm = new MotivosForm();
			        	
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
		        		rs = null;
		        		stmt = null;
		        	}
		        }
		        return listaMotivos;
		    }
	 
	
		public MotivosForm obtenerMotivo(Connection con, String esquema, int claveMotivo)
		    {
		        PreparedStatement stmt = null;
		        ResultSet rs = null;
		        MotivosForm motivosForm = new MotivosForm();
		        try{
		            stmt = con.prepareStatement(MotivosQuerys.getQueryoOtenerMotivo(esquema));
		            stmt.setInt(1, claveMotivo);
			        rs = stmt.executeQuery();
			        if (rs.next()){
			        	motivosForm.setNombreCorto(Utils.noNulo(rs.getString(1)));
			        	motivosForm.setDescripcion(Utils.noNulo(rs.getString(2)));
			        	motivosForm.setSubject(Utils.noNuloNormal(rs.getString(3)));
			        	motivosForm.setMensaje(Utils.noNuloNormal(rs.getString(4)));
			        	
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
		        		rs = null;
		        		stmt = null;
		        	}
		        }
		        return motivosForm;
		    }
	 
	 
}
