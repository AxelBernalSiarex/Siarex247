package com.siarex247.seguridad.Perfiles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.utils.Utils;

public class PerfilesBean {
	public static final Logger logger = Logger.getLogger("siarex247");
	
	public ArrayList<PerfilesForm> detallePerfiles (Connection con, String esquema, String perfilUsuario){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<PerfilesForm> listaPerfiles = new ArrayList<PerfilesForm>();
		PerfilesForm perfilesForm = new PerfilesForm();
		try {
			if ("009".equalsIgnoreCase(perfilUsuario)) {
            	stmt = con.prepareStatement(PerfilesQuerys.getDetallePerfilesNomina(esquema));
            	stmt.setString(2, "007");
        	}else {
            	stmt = con.prepareStatement(PerfilesQuerys.getDetallePerfiles(esquema));

        	}
			stmt.setString(1, "A");
			rs  = stmt.executeQuery();
			String nombreCorto = null;
			while (rs.next()) {
				nombreCorto = Utils.noNuloNormal(rs.getString(2));
				if ("008".equalsIgnoreCase(perfilUsuario) && ("ADM".equalsIgnoreCase(nombreCorto) || "009".equalsIgnoreCase(nombreCorto) || "007".equalsIgnoreCase(nombreCorto))) {
					logger.info("Perfil no permitido para usuario administrador.....");
				}else {
					perfilesForm.setClavePerfil(rs.getInt(1));
					perfilesForm.setNombreCorto(nombreCorto);
					perfilesForm.setDescripcion(Utils.noNulo(rs.getString(3)));
					perfilesForm.setMostrarProyectos(Utils.noNuloNormal(rs.getString(4)));
					perfilesForm.setTipoPerfil(Utils.noNuloNormal(rs.getString(5)));
					listaPerfiles.add(perfilesForm);
					perfilesForm = new PerfilesForm();
				}
				
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
		return listaPerfiles;
	}
	
	public PerfilesForm consultaPerfil (Connection con, String esquema, int idRegistro){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PerfilesForm perfilesForm = new PerfilesForm();
		try {
			stmt = con.prepareStatement(PerfilesQuerys.getDetallePerfil(esquema));
			stmt.setInt(1, idRegistro);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				perfilesForm.setClavePerfil(rs.getInt(1));
				perfilesForm.setNombreCorto(Utils.noNuloNormal(rs.getString(2)));
				perfilesForm.setDescripcion(Utils.noNulo(rs.getString(3)));
				perfilesForm.setMostrarProyectos(Utils.noNuloNormal(rs.getString(4)));
				perfilesForm.setTipoPerfil(Utils.noNuloNormal(rs.getString(5)));
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
		return perfilesForm;
	}
	
	public PerfilesForm consultaPerfilEsquema (String esquemaEmpresa, int idRegistro){
		Connection con= null;
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rc = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PerfilesForm perfilesForm = new PerfilesForm();
		try {
			rc = connPool.getConnection(esquemaEmpresa);
			con = rc.getCon();
			stmt = con.prepareStatement(PerfilesQuerys.getDetallePerfil(rc.getEsquema()));
			stmt.setInt(1, idRegistro);
			rs  = stmt.executeQuery();
			if (rs.next()) {
				perfilesForm.setClavePerfil(rs.getInt(1));
				perfilesForm.setNombreCorto(Utils.noNuloNormal(rs.getString(2)));
				perfilesForm.setDescripcion(Utils.noNulo(rs.getString(3)));
				perfilesForm.setMostrarProyectos(Utils.noNuloNormal(rs.getString(4)));
				perfilesForm.setTipoPerfil(Utils.noNuloNormal(rs.getString(5)));
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
				
				if (con != null) {
					con.close();
				}
				con = null;
			}catch(Exception e) {
				rs = null;
				stmt = null;
				con = null;
			}
		}
		return perfilesForm;
	}
	
	public int altaPerfiles (Connection con, String esquema, String nombreCorto, String descripcion, String mostrarProyectos, String tipoPerfil, String usuarioHTTP){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(PerfilesQuerys.getInsertPerfiles(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, nombreCorto);
			stmt.setString(2, descripcion);
			stmt.setString(3, mostrarProyectos);
			stmt.setString(4, tipoPerfil);
			stmt.setString(5, "A");
			stmt.setString(6, usuarioHTTP);
			int cant = stmt.executeUpdate();
			if(cant > 0){
				rs = stmt.getGeneratedKeys();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
			}
			logger.info("El usuario "+usuarioHTTP + " ha guardado el perfil: "+descripcion);
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
	
	public int updatePerfiles (Connection con, String esquema, int idRegistro, String nombreCorto, String descripcion, String mostrarProyectos, String tipoPerfil,  String usuarioHTTP){
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(PerfilesQuerys.getUpdatePerfiles(esquema));
			// stmt.setString(1, nombreCorto);
			stmt.setString(1, descripcion);
			stmt.setString(2, mostrarProyectos);
			stmt.setString(3, tipoPerfil);
			stmt.setString(4, usuarioHTTP);
			stmt.setInt(5, idRegistro);
			resultado = stmt.executeUpdate();
			logger.info("El usuario "+usuarioHTTP + " ha actualizado el perfil de acceso "+idRegistro);
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
		
	public int deletePerfiles (Connection con, String esquema, int idRegistro, String usuarioHTTP){
		PreparedStatement stmt = null;
		int resultado = 0;
		try {
			stmt = con.prepareStatement(PerfilesQuerys.getEliminaPerfiles(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, "D");
			stmt.setString(2, usuarioHTTP);
			stmt.setInt(3, idRegistro);
			resultado = stmt.executeUpdate();
			logger.info("El usuario "+usuarioHTTP + " ha eliminado el perfil: "+idRegistro);
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
	
	
	public ArrayList<PerfilesForm> comboPerfiles(Connection con, String esquema, String perfilUsuario) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<PerfilesForm> listaPerfiles = new ArrayList<>();
        PerfilesForm perfilesForm = new PerfilesForm();
        try{
        	if ("009".equalsIgnoreCase(perfilUsuario)) {
            	stmt = con.prepareStatement(PerfilesQuerys.getDetallePerfilesNomina(esquema));
            	stmt.setString(2, "007");
        	}else {
            	stmt = con.prepareStatement(PerfilesQuerys.getDetallePerfiles(esquema));

        	}
			stmt.setString(1, "A");
	        rs = stmt.executeQuery();

	        perfilesForm.setClavePerfil(0);
	        perfilesForm.setDescripcion("Seleccione un Perfil");
	        listaPerfiles.add(perfilesForm);
	        perfilesForm = new PerfilesForm();
	        int clavePerfil = 0;
	        String nombreCorto = null;
	        while (rs.next()){
	        	nombreCorto = Utils.noNulo(rs.getString(2));
	        	if ("008".equalsIgnoreCase(perfilUsuario) && ("ADM".equalsIgnoreCase(nombreCorto) || "009".equalsIgnoreCase(nombreCorto) || "007".equalsIgnoreCase(nombreCorto))) {
					logger.info("Perfil no permitido para usuario administrador.....");
				}else {
					clavePerfil = rs.getInt(1);
		        	if (clavePerfil > 1 && !"003".equalsIgnoreCase(nombreCorto)) {
		        		perfilesForm.setClavePerfil(clavePerfil);
				        perfilesForm.setDescripcion(Utils.noNuloNormal(rs.getString(3)));
				        listaPerfiles.add(perfilesForm);
				        perfilesForm = new PerfilesForm();
		        	}
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

        	}catch(Exception e){
        		rs = null;
        		stmt = null;
        	}
        }
        return listaPerfiles;
    }
	
	
	public ArrayList<PerfilesForm> comboPerfilesEmpleado(Connection con, String esquema) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<PerfilesForm> listaPerfiles = new ArrayList<>();
        PerfilesForm perfilesForm = new PerfilesForm();
        try{
        	stmt = con.prepareStatement(PerfilesQuerys.getComboEmpleados(esquema));
			stmt.setString(1, "A");
			stmt.setString(2, "001");
			stmt.setString(3, "002");
			stmt.setString(4, "004");
			stmt.setString(5, "005");
			
	        rs = stmt.executeQuery();

	        perfilesForm.setClavePerfil(0);
	        perfilesForm.setDescripcion("Seleccione un Perfil");
	        listaPerfiles.add(perfilesForm);
	        perfilesForm = new PerfilesForm();
	        
	        while (rs.next()){
	        	perfilesForm.setClavePerfil(rs.getInt(1));
		        perfilesForm.setDescripcion(Utils.noNuloNormal(rs.getString(3)));
		        listaPerfiles.add(perfilesForm);
		        perfilesForm = new PerfilesForm();
	        	
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
        return listaPerfiles;
    }
}
