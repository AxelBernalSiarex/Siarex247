package com.siarex247.configuraciones.Descartar;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;

public class DescartarAction extends DescartarSupport {

	private static final long serialVersionUID = -2367375139014322249L;

	
	
	public String detalleDescartes(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	DescartarBean descartarBean = new DescartarBean();

		try{
			PrintWriter out = response.getWriter();
			Map<String, Object > mapaRes = null;
			JSONArray jsonArray  = null;
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				mapaRes  = descartarBean.detalleDescartes(con, session.getEsquemaEmpresa(), session.getLenguaje());
	            jsonArray  = (JSONArray) mapaRes.get("detalle");

	            Map<String, Object> json = new HashMap<String, Object>();

	        	json.put("data", jsonArray);

	            String jsonobj = JSONObject.toJSONString(json);
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
	
	
	

	public String eliminarRegistro(){
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
		Connection con = null;
		ResultadoConexion rc = null;
		DescartarBean descartarBean = new DescartarBean();
		try{
			response = ServletActionContext.getResponse();
	    	request = ServletActionContext.getRequest();
			out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();

			    int eliminado = descartarBean.eliminaDescartes(con, rc.getEsquema(), getFolioEmpresa());

			    Map<String, Object> json = new HashMap<String, Object>();
			    if (eliminado == 1) {
			    	json.put("codError", "000");
			    	json.put("mensajeError", "El registro se ha guardado satisfactoriamente.");
			    }else {
			    	json.put("codError", "001");
			    	json.put("mensajeError", "Error al eliminar el registro, consulte a su administrador.");
			    }
			    // json.put("ESTATUS", eliminado > 0 ? "000" : "001");
	            out.print(JSONObject.toJSONString(json));
	            out.flush();
	            out.close();
			}
		}
		catch(Exception e){
			Utils.imprimeLog("eliminarRegistro(): ", e);
		}
		finally{
		  try{
			if (con != null){
				con.close();
			}
			con = null;
		  }
		  catch(Exception e){
			con = null;
		  }
		}
		return SUCCESS;
	}
	
	
	public String iniciaCargaTXT(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Connection con = null;
		ResultadoConexion rc = null;
		PrintWriter out = null;
		DescartarBean descartesBean = new DescartarBean();
		Map<String, Object> json = new HashMap<String, Object>();

		try {
			logger.info("************* DETONANDO PROCESO DEL DESCARTE ******************");
			logger.info("Tiempo Inicial : "+System.currentTimeMillis());
			   SiarexSession session = ObtenerSession.getSession(request);
			  if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			  }else{
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					out = response.getWriter();
					String eliminarBase = getEliminarBase();
					boolean eliminarRegistro = false;
					if ("on".equalsIgnoreCase(eliminarBase)) {
						eliminarRegistro = true;
					}
					Part filePart = request.getPart("fileTXT");
		          	File fileTXT = UtilsFile.getFileFromPart(filePart);
		          	  
					int grabar = descartesBean.grabarDescarte(con, session.getEsquemaEmpresa(), fileTXT, getUsuario(request), eliminarRegistro);
					logger.info("grabar : "+grabar);
		        	json.put("ESTATUS", grabar >= 1 ? "OK" : "ERROR");
		            out.print(JSONObject.toJSONString(json));
		            out.flush();
		            out.close();
			  }
			  logger.info("Tiempo Final : "+System.currentTimeMillis());
		}
		catch(Exception e) {
			Utils.imprimeLog("iniciaCargaTXT(): ", e);
		}
		return SUCCESS;
	}
}
