package com.siarex247.bd;

import org.apache.log4j.Logger;
import org.apache.struts2.ActionSupport;

import jakarta.servlet.http.HttpServletRequest;

public abstract class ActionDB extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final Logger logger = Logger.getLogger("siarex");
	public static final String OK = "OK";
	public static final String ERROR = "ERROR";
	public static final String MENSAJE = "MENSAJE";

	
	public String getUsuario(HttpServletRequest request) {
		return request.getRemoteUser();
		// return "siarex.admin@siarex.com";
	}
	
	public String getRemoteIp(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		  if ("0:0:0:0:0:0:0:1".equalsIgnoreCase(remoteAddr)) {
			  remoteAddr = "192.168.0.108";
		  }
		  
		return remoteAddr;
	}
	

	public ResultadoConexion getConnection(String nombreEsquema) {
		ResultadoConexion rc = null;
		try {
			ConexionDB connPool = new ConexionDB();
			rc = connPool.getConnection(nombreEsquema);
			return rc;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	
	public ResultadoConexion getConnectionSiarex() {
		ResultadoConexion rc = null;
		try {
			ConexionDB connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			return rc;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	
	public ResultadoConexion getConnectionLenguaje() {
		ResultadoConexion rc = null;
		try {
			ConexionDB connPool = new ConexionDB();
			rc = connPool.getConnectionLenguaje();
			return rc;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	public ResultadoConexion getConnectionListaNegra() {
		ResultadoConexion rc = null;
		try {
			ConexionDB connPool = new ConexionDB();
			rc = connPool.getConnectionListaNegra();
			return rc;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	
	public ResultadoConexion getConnectionSAT() {
		ResultadoConexion rc = null;
		try {
			ConexionDB connPool = new ConexionDB();
			rc = connPool.getConnectionSAT();
			return rc;
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

}
