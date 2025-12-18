package com.siarex247.catalogos.Empleados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.utils.Utils;

public class EmpleadosBean {

	public static final Logger logger = Logger.getLogger("siarex247");

	public ArrayList<EmpleadosForm> detalle (Connection con, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<EmpleadosForm> listaEmpleados = new ArrayList<>();
		EmpleadosForm empleadosForm = new EmpleadosForm();
		try {		
			stmt = con.prepareStatement(EmpleadosQuerys.getDetalle(esquema));
			rs  = stmt.executeQuery();
			while (rs.next()) {
				empleadosForm.setIdRegistro(rs.getInt(1));
				empleadosForm.setIdEmpleado(Utils.noNulo(rs.getString(2)));
				empleadosForm.setNombreCompleto(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(3))));
				empleadosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
				empleadosForm.setIdSupervisor(Utils.noNulo(rs.getString(5)));
				empleadosForm.setShipTo(Utils.noNuloNormal(rs.getString(6)));
				empleadosForm.setPermitirAlta(Utils.noNulo(rs.getString(7)));
				listaEmpleados.add(empleadosForm);
				empleadosForm = new EmpleadosForm();
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
		return listaEmpleados;
	}
	
	
	
	public EmpleadosForm consultaEmpleado (Connection con, String esquema, int idRegistro){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EmpleadosForm empleadosForm = new EmpleadosForm();
		try {
			stmt = con.prepareStatement(EmpleadosQuerys.getConsultaPuesto(esquema));
			stmt.setInt(1, idRegistro);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				empleadosForm.setIdRegistro(rs.getInt(1));
				empleadosForm.setIdEmpleado(Utils.noNulo(rs.getString(2)));
				empleadosForm.setNombreCompleto(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(3))));
				empleadosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
				empleadosForm.setIdSupervisor(Utils.noNulo(rs.getString(5)));
				empleadosForm.setShipTo(Utils.noNuloNormal(rs.getString(6)));
				empleadosForm.setPermitirAlta(Utils.noNulo(rs.getString(7)));

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
		return empleadosForm;
	}
	
	
	public EmpleadosForm consultaEmpleadoXid (Connection con, String esquema, String idEmpleado){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EmpleadosForm empleadosForm = new EmpleadosForm();
		try {
			stmt = con.prepareStatement(EmpleadosQuerys.getConsultaXid(esquema));
			stmt.setString(1, idEmpleado);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				empleadosForm.setIdRegistro(rs.getInt(1));
				empleadosForm.setIdEmpleado(Utils.noNulo(rs.getString(2)));
				empleadosForm.setNombreCompleto(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(3))));
				empleadosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
				empleadosForm.setIdSupervisor(Utils.noNulo(rs.getString(5)));
				empleadosForm.setShipTo(Utils.noNuloNormal(rs.getString(6)));
				empleadosForm.setPermitirAlta(Utils.noNulo(rs.getString(7)));

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
		return empleadosForm;
	}
	
	public EmpleadosForm consultaEmpleadoXCorreo (Connection con, String esquema, String correoEmpleado){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EmpleadosForm empleadosForm = new EmpleadosForm();
		try {
			stmt = con.prepareStatement(EmpleadosQuerys.getConsultaXid(esquema));
			stmt.setString(1, correoEmpleado);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				empleadosForm.setIdRegistro(rs.getInt(1));
				empleadosForm.setIdEmpleado(Utils.noNulo(rs.getString(2)));
				empleadosForm.setNombreCompleto(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(3))));
				empleadosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
				empleadosForm.setIdSupervisor(Utils.noNulo(rs.getString(5)));
				empleadosForm.setShipTo(Utils.noNuloNormal(rs.getString(6)));
				empleadosForm.setPermitirAlta(Utils.noNulo(rs.getString(7)));

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
		return empleadosForm;
	}
	
	
	public int altaEmpleado (Connection con, String esquema, String idEmpleado, String nombreCompleto, String correo, String idSupervisor, String shipTO, String permitirAlta, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {		
			stmt = con.prepareStatement(EmpleadosQuerys.getGuardar(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, idEmpleado);
			stmt.setString(2, nombreCompleto);
			stmt.setString(3, correo);
			stmt.setString(4, idSupervisor);
			stmt.setString(5, shipTO);
			stmt.setString(6, permitirAlta);
			stmt.setString(7, usuarioHTTP);
			
			int cant = stmt.executeUpdate();
			if(cant > 0){
				rs = stmt.getGeneratedKeys();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
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
	
	
	public int modificaEmpleado (Connection con, String esquema, int idRegistro, String idEmpleado, String nombreCompleto, String correo, String idSupervisor, String shipTO, String permitirAlta, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {		
			stmt = con.prepareStatement(EmpleadosQuerys.getUpdate(esquema));
			stmt.setString(1, nombreCompleto);
			stmt.setString(2, correo);
			stmt.setString(3, idSupervisor);
			stmt.setString(4, shipTO);
			stmt.setString(5, permitirAlta);
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
	
	
	public int eliminaEmpleado (Connection con, String esquema, int idRegistro, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {		
			stmt = con.prepareStatement(EmpleadosQuerys.getElimina(esquema));
			stmt.setInt(1, idRegistro);
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
	
	
	
	 public HashMap<String, String> obteneEmpleadosCarga(Connection conEmpresa, String esquema)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        HashMap<String, String> mapaResultado = new HashMap<String, String>();
	        try
	        {
	        	mapaResultado.put("", "0");
	        	StringBuffer sbQuery = new StringBuffer(EmpleadosQuerys.getSeleccionaEmpleado(esquema));
	        	stmt = conEmpresa.prepareStatement(sbQuery.toString());
	        	rs = stmt.executeQuery();
	            String idEmpleado = null;
	            String claveRegistro = "";
	            while(rs.next()) 
	            {
					claveRegistro = rs.getString(1);
					idEmpleado = Utils.noNulo(rs.getString(2));
					mapaResultado.put(idEmpleado, claveRegistro);
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
	        return mapaResultado;
	    }
	 
	
	 public int buscarEmpleadoXshipTO (Connection conEmpresa, String esquema, String shipTO)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        int idEmpleado = 0;
	        try
	        {
	            stmt = conEmpresa.prepareStatement(EmpleadosQuerys.getBuscarEmpleadoXshipTO(esquema));
	            stmt.setString(1, shipTO);
	            rs = stmt.executeQuery();
	            if(rs.next()){
	            	idEmpleado = rs.getInt(1);
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
	        return idEmpleado;
	    }
	 
	 
	 public HashMap<String, EmpleadosForm> obteneEmpleados(Connection conEmpresa, String esquema)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        HashMap<String, EmpleadosForm> mapaResultado = new HashMap<>();
	        try
	        {
	        	StringBuffer sbQuery = new StringBuffer(EmpleadosQuerys.getDetalle(esquema));
	        	stmt = conEmpresa.prepareStatement(sbQuery.toString());
	        	rs = stmt.executeQuery();
	            EmpleadosForm empleadosForm = new EmpleadosForm();
	            while(rs.next()) {
					empleadosForm.setIdRegistro(rs.getInt(1));
					empleadosForm.setIdEmpleado(Utils.noNulo(rs.getString(2)));
					empleadosForm.setNombreCompleto(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(3))));
					empleadosForm.setCorreo(Utils.noNuloNormal(rs.getString(4)));
					empleadosForm.setIdSupervisor(Utils.noNulo(rs.getString(5)));
					empleadosForm.setShipTo(Utils.noNuloNormal(rs.getString(6)));
					empleadosForm.setPermitirAlta(Utils.noNulo(rs.getString(7)));
					mapaResultado.put(empleadosForm.getIdEmpleado(), empleadosForm);
					empleadosForm = new EmpleadosForm();
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
	        return mapaResultado;
	    }
	 
	 
	 
	 public ArrayList<EmpleadosForm> comboEmpleados (Connection con, String esquema, String idLengueje){
			PreparedStatement stmt = null;
			ResultSet rs = null;
			ArrayList<EmpleadosForm> listaEmpleados = new ArrayList<>();
			EmpleadosForm empleadosForm = new EmpleadosForm();
			LenguajeBean lenguajeBean = LenguajeBean.instance();
			try {
				HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(idLengueje, "PAN_MSG_GENERALES");
				String msgSeleccione = null;
				if ("".equalsIgnoreCase(Utils.noNulo(mapaLenguaje.get("MSG_SELECCION_EMPLEADO")))) {
					msgSeleccione = "Seleccione un Empleado";
				}else {
					msgSeleccione = mapaLenguaje.get("MSG_SELECCION_EMPLEADO");
				}
				
				empleadosForm.setIdEmpleado("");
				empleadosForm.setNombreCompleto(msgSeleccione);
				listaEmpleados.add(empleadosForm);
				empleadosForm = new EmpleadosForm();
				
				stmt = con.prepareStatement(EmpleadosQuerys.getDetalle(esquema));
				rs  = stmt.executeQuery();
				while (rs.next()) {
					empleadosForm.setIdRegistro(rs.getInt(1));
					empleadosForm.setIdEmpleado(Utils.noNulo(rs.getString(2)));
					empleadosForm.setNombreCompleto(Utils.noNulo(rs.getString(2)) + " - " + Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(3))));
					listaEmpleados.add(empleadosForm);
					empleadosForm = new EmpleadosForm();
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
			return listaEmpleados;
		}
}
