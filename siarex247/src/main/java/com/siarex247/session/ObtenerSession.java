
package com.siarex247.session;

import java.util.HashMap;

import org.apache.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;


public class ObtenerSession {
	public static final Logger logger = Logger.getLogger("siarex247");
	public static HashMap<String, SiarexSession> mapaAplicacion = new HashMap<>();
	
	
	static public SiarexSession getSession(HttpServletRequest request) {
		SiarexSession session = null;
		try {
			session = new SiarexSession(request.getSession());
			if("".equals(session.getEsquemaEmpresa())){
				//asignarValoresRespaldo(cfgIni, usuario, ip_agen);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return session;
	}
	
	
}