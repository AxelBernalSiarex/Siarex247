package com.siarex247.seguridad.Bitacora;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class BitacoraAction extends BitacoraSupport{

	
	private static final long serialVersionUID = -1772099010798030359L;

	
	public String detalleHistorico(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();

    	BitacoraBean bitacoraBean = new BitacoraBean();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			} else{
				response.setContentType("text/html; charset=UTF-8");
		  		response.setCharacterEncoding("UTF-8");
				
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    JSONArray jsonArray  = null;
	            Map<String, Object > mapaRes = null;
	            
	            mapaRes  = bitacoraBean.detalleFormatosHistoricos(con, session.getEsquemaEmpresa());
	            
	            jsonArray  = (JSONArray) mapaRes.get("detalle");
		          
	            Map<String, Object> json = new HashMap<String, Object>();
	            json.put("data", jsonArray);
	            String jsonobj = org.json.simple.JSONObject.toJSONString(json);
	            out.print(jsonobj);
	            out.flush();
	            out.close(); 
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
		  try{
			if (con != null){
				con.close();
			}
			con = null;
		  }catch(Exception e){
			con = null;
		  }
		}
		return SUCCESS;
	}


}
