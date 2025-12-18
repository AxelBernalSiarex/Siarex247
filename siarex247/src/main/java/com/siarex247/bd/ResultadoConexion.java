/*
 * Created on Mar 2, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siarex247.bd;

import java.sql.Connection;

public class ResultadoConexion {
	private Connection con;
	private String esquema;
	
	
	public ResultadoConexion(Connection con, String esquema) {
		this.con = con;
		this.esquema = esquema;
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public String getEsquema() {
		return esquema;
	}

	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}

	
}

