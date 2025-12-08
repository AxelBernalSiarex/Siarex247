package com.siarex247.catalogos.Motivos;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;

public class MotivosAction extends MotivosSupport {

	private static final long serialVersionUID = 1188505760319155498L;

	
 public String comboMotivos()
    {
        Connection con = null;
        
        ResultadoConexion rc = null;
        MotivosBean motivosBean = new MotivosBean();
        try{
        	HttpServletResponse response = ServletActionContext.getResponse();
        	HttpServletRequest request = ServletActionContext.getRequest();

        	response.setContentType("text/html; charset=UTF-8");
 			response.setCharacterEncoding("UTF-8");
 			
        	SiarexSession session = ObtenerSession.getSession(request);
        	PrintWriter out = response.getWriter();
            rc = getConnection(session.getEsquemaEmpresa());
            con = rc.getCon();
           
            
            MotivosModel motivosModel = new MotivosModel();
			ArrayList<MotivosForm> listaPuestos = motivosBean.comboMotivos(con, rc.getEsquema(), "AME");
			motivosModel.setData(listaPuestos);
			JSONObject json = new JSONObject(motivosModel);
			out.print(json);
            out.flush();
            out.close();
            
        }catch(Exception e){
        	Utils.imprimeLog("", e);
            logger.error(e);
        }finally {
            try{
                if(con != null)
                    con.close();
                con = null;
            }catch(Exception e){
                con = null;
            }
		}
        return null;
    }
	
	
}
