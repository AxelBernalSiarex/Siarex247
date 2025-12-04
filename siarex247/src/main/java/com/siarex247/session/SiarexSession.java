package com.siarex247.session;
import java.io.Serializable;

import jakarta.servlet.http.HttpSession;

public class SiarexSession implements Serializable {

	private HttpSession sessionHTTP = null;
	private static final long serialVersionUID = 6443205255784503800L;
	
	
	public SiarexSession(HttpSession session) {
		super();
		if(session==null){
			throw new IllegalArgumentException("El parametro session no puede ser nulo");
		}
		sessionHTTP = session;
	}

	public String getAttribute(String atributo, String valDef) {
		String res;
		try {
			res = (String) sessionHTTP.getAttribute(atributo);
			if (res == null || "".equals(res.trim())) {
				res = valDef;
			}
		} catch (Exception e) {
			res = valDef;
			e.printStackTrace();
		}
		return res;
	}
	
	public int getAttributeInt(String atributo) {
		int val = 0;
		
		try {
			val = ((Integer)sessionHTTP.getAttribute(atributo)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}
	
	public boolean getAttributeBol(String atributo) {
		boolean val = false;
		try {
			val = ((Boolean)sessionHTTP.getAttribute(atributo)).booleanValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}
	
	public String getAttribute(String atributo) {
		return getAttribute(atributo, "");
	}
	
	public void setAttribute(String atributo, String valor) {
		try {
			sessionHTTP.setAttribute(atributo, valor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void setAttribute(String atributo, int valor) {
		try {
			sessionHTTP.setAttribute(atributo, new Integer(valor));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getEsquemaEmpresa() {
		 return getAttribute("esquemaEmpresa");
		// return "mvs"; // lupillo
	}
	
	public void setEsquemaEmpresa(String esquemaEmpresa) {
		sessionHTTP.setAttribute("esquemaEmpresa", esquemaEmpresa);
	}
	
	
	public String getLenguaje() {
		return getAttribute("lenguaje");
		// return "MX";
	}
	
	public void setLenguaje(String lenguaje) {
		sessionHTTP.setAttribute("lenguaje", lenguaje);
	}
	
	
	
	
/*
	public int getClaveEmpresa() {
		return getAttributeInt("claveEmpresa");
	}
	
	public void setClaveEmpresa(int claveEmpresa) {
		sessionHTTP.setAttribute("claveEmpresa", claveEmpresa);
	}

	public String getNombreEmpresa() {
		return getAttribute("nombreEmpresa");
	}
	
	public void setNombreEmpresa(String nombreEmpresa) {
		sessionHTTP.setAttribute("nombreEmpresa", nombreEmpresa);
	}
	
	
	public String getNombrePerfil() {
		return getAttribute("nombrePerfil");
	}
	
	public void setNombrePerfil(String nombrePerfil) {
		sessionHTTP.setAttribute("nombrePerfil", nombrePerfil);
	}
	

	public String getNombreUsuario() {
		return getAttribute("nombreUsuario");
	}
	
	public void setNombreUsuario(String nombreUsuario) {
		sessionHTTP.setAttribute("nombreUsuario", nombreUsuario);
	}
	
	
	public String getNumeroEmpleado() {
		return getAttribute("numeroEmpleado");
	}
	
	public void setNumeroEmpleado(String numeroEmpleado) {
		sessionHTTP.setAttribute("numeroEmpleado", numeroEmpleado);
	}
	
	
	public String getImagenEmpresa() {
		return getAttribute("imagenEmpresa");
	}
	
	public void setImagenEmpresa(String rutaImagen) {
		sessionHTTP.setAttribute("imagenEmpresa", rutaImagen);
	}
	
	*/
	
}
