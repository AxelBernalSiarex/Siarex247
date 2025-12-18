package com.siarex247.configSistema.CertificadoSAT;

import org.apache.struts2.interceptor.parameter.StrutsParameter;

import com.siarex247.bd.ActionDB;

public class CertificadoSATSupport extends ActionDB{


	private static final long serialVersionUID = 7410300564176107747L;

	
	
	private String pwdSat = "";

	public String getPwdSat() {
		return pwdSat;
	}
	@StrutsParameter
	public void setPwdSat(String pwdSat) {
		this.pwdSat = pwdSat;
	}
	
	
}
