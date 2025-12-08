package com.siarex247.configSistema.CertificadoSAT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.siarex247.utils.Utils;

public class CertificadoSATBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	
	public int actualizarCertificado (Connection con, String esquemaEmpresa, String nombreCer, String nombreKey, String pwdSat){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int resultado = 0;
		
		try {
			stmt = con.prepareStatement(CertificadoSATQuerys.getQueryUpdateCertificados(""));
			stmt.setString(1, pwdSat);
			stmt.setString(2, nombreCer);
			stmt.setString(3, nombreKey);
			stmt.setString(4, esquemaEmpresa);
//			logger.info("stmt===>"+stmt);
			stmt.executeUpdate();
			
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
	
}
