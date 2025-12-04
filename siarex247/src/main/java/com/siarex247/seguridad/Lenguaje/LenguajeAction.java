package com.siarex247.seguridad.Lenguaje;

import java.io.PrintWriter;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class LenguajeAction extends LenguajeSupport {

	private static final long serialVersionUID = 1500040941246370935L;

	
	
	
	
	
	public String obtenerEtiquetas() throws Exception {
		LenguajeBean lenguajeBean = LenguajeBean.instance();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				
				
				response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
		  		PrintWriter out = response.getWriter();
				HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), getNombrePantalla());
				
				JSONObject json = new JSONObject(mapaLenguaje);
				out.print(json);
	            out.flush();
	            out.close();
				
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return SUCCESS;
	}
	
}
