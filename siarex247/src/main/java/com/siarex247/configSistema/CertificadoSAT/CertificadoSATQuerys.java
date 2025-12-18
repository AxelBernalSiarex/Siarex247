package com.siarex247.configSistema.CertificadoSAT;

public class CertificadoSATQuerys {

	private static String updateCertificados =  "update EMPRESAS set PASSWORD_SAT = ?, ARVHIVO_CER = ?, ARVHIVO_KEY = ? where ESQUEMA = ?";
	
	public static String getQueryUpdateCertificados(String esquema) {
		return updateCertificados.replaceAll("<<esquema>>", esquema);
	}
	
	
}
