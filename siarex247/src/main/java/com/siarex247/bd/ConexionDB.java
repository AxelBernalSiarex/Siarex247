package com.siarex247.bd;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;


public class ConexionDB {
	public static final Logger logger = Logger.getLogger("siarex");
	

	public final ResultadoConexion getConnectionSiarex() throws NamingException {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			// DataSource ds = net.jamb.jdbc.tester.XDataSource.getInstance((DataSource)envContext.lookup("jdbc/SIAREX_accesos_produccion"));
			DataSource ds = net.jamb.jdbc.tester.XDataSource.getInstance((DataSource)envContext.lookup("jdbc/SIAREX_accesos"));
			conn =  ds.getConnection();
			return new ResultadoConexion(conn, "VTA");
			
		} catch (SQLException e) {
			Utils.imprimeLog("", e);
		}
		return null;
	}

	
	public final ResultadoConexion getConnectionLenguaje() throws NamingException {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = net.jamb.jdbc.tester.XDataSource.getInstance((DataSource)envContext.lookup("jdbc/CM_SEGURIDAD"));
			conn =  ds.getConnection();
			return new ResultadoConexion(conn, "cm247_seguridad");
			
		} catch (SQLException e) {
			Utils.imprimeLog("", e);
		}
		return null;
	}
	
	
	public final ResultadoConexion getConnectionSAT() throws NamingException {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = net.jamb.jdbc.tester.XDataSource.getInstance((DataSource)envContext.lookup("jdbc/SIAREX_SAT"));
			conn =  ds.getConnection();
			return new ResultadoConexion(conn, "");
			
		} catch (SQLException e) {
			Utils.imprimeLog("", e);
		}
		return null;
	}
	

	public final ResultadoConexion getConnection(String nombreEsquema) throws NamingException {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = net.jamb.jdbc.tester.XDataSource.getInstance((DataSource)envContext.lookup("jdbc/SIAREX_"+nombreEsquema.toLowerCase()));
			conn =  ds.getConnection();
			return new ResultadoConexion(conn, "contrare_"+nombreEsquema);
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return null;
	}
	
	
	public final ResultadoConexion getConnectionListaNegra() throws NamingException {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = net.jamb.jdbc.tester.XDataSource.getInstance((DataSource)envContext.lookup("jdbc/SIAREX_LISTANEGRA"));
			conn =  ds.getConnection();
			return new ResultadoConexion(conn, "siarex_accesos");
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return null;
	}
	
	
	
	/*
	public final ResultadoConexion getConnectionSiarex() throws NamingException {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = net.jamb.jdbc.tester.XDataSource.getInstance((DataSource)envContext.lookup("jdbc/SIAREX_siarex"));
			conn =  ds.getConnection();
			return new ResultadoConexion(conn, "VTA");
			
		} catch (SQLException e) {
			Utils.imprimeLog("", e);
		}
		return null;
	}
*/
	
	
}
