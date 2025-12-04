package com.siarex247.estadisticas.RepValidacion;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class RepValidacionAction extends RepValidacionSupport{
	private static final long serialVersionUID = 4436489872800760049L;

	public String detalleReporte(){
    	
    	HttpServletResponse response = null;
    	HttpServletRequest request = null;
    	PrintWriter out = null;
    
		Connection con = null;
		ResultadoConexion rc = null;
		
		RepValidacionBean repVerificaBean = new RepValidacionBean();

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
			    
			    RepValidacionModel repModel = new RepValidacionModel();
			    ArrayList<RepValidacionForm> listaDetalle = repVerificaBean.detalleBitacora(con, session.getEsquemaEmpresa());
			    repModel.setData(listaDetalle);
			    repModel.setRecordsFiltered(20);
			    repModel.setDraw(-1);
			    repModel.setRecordsTotal(listaDetalle.size());
				JSONObject json = new JSONObject(repModel);
				out.print(json);
	            out.flush();
	            out.close();
			    
			}
		}
		catch(Exception e){
			Utils.imprimeLog("detalleReporte(): ", e);
		}
		finally{
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
