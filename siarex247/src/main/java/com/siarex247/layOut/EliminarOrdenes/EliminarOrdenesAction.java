package com.siarex247.layOut.EliminarOrdenes;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.layOut.OrdenesCompra.OrdenesCompraBean;
import com.siarex247.layOut.OrdenesCompra.OrdenesCompraForm;
import com.siarex247.layOut.OrdenesCompra.OrdenesCompraModel;
import com.siarex247.layOut.OrdenesCompra.OrdenesCompraSupport;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class EliminarOrdenesAction extends OrdenesCompraSupport{

	private static final long serialVersionUID = -3245957525868186814L;


	public String cargarEliminar() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		OrdenesCompraBean ordenesCompraBean = new OrdenesCompraBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
    	
		try{
		  SiarexSession session = ObtenerSession.getSession(request);
		  if ("".equals(session.getEsquemaEmpresa())){
			return Action.LOGIN;
		  }
		  else{
			  response.setContentType("text/html; charset=UTF-8");
			  response.setCharacterEncoding("UTF-8");
			  PrintWriter out = response.getWriter();
				
			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
			  
			  OrdenesCompraModel ordenesCompraModel = new OrdenesCompraModel();

			  logger.info("bandProcesar===>"+getBandProcesar());
			  
			  Part filePart = request.getPart("fileCarga");
          	  File fileCarga = UtilsFile.getFileFromPart(filePart);
			  
			  
			  if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(UtilsFile.getContentType(fileCarga)) || 
					  "application/vnd.ms-excel".equalsIgnoreCase(UtilsFile.getContentType(fileCarga)) ) {
				  
				  int claveHistorico = ordenesCompraBean.grabarHistorial(con, session.getEsquemaEmpresa(), fileCarga.getName(), 0, "5", 1, 0, 0, getUsuario(request), null);
				  if (claveHistorico == -100) {
					  ordenesCompraModel.setCodError("001");
					  ordenesCompraModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					  ordenesCompraModel.setClaveRegistro(claveHistorico);
					}else {
						 String dirOrdenes = UtilsPATH.REPOSITORIO_DOCUMENTOS +session.getEsquemaEmpresa() + File.separator + "ORDENES_COMPRA" + File.separator + fileCarga.getName() ;
						 File fdes = new File(dirOrdenes);
						 UtilsFile.moveFileDirectory(fileCarga, fdes, true, false, true);
						  
						ordenesCompraModel.setCodError("000");
						ordenesCompraModel.setClaveRegistro(claveHistorico);
						ordenesCompraModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}
				  
			  }else {
				  
				  ordenesCompraModel.setCodError("001");
				  ordenesCompraModel.setMensajeError("Error al procesar el archivo de eliminar ordenes de compra, solo se permiten archivos excel.");  
			  }
			  
			  
				
			  JSONObject json = new JSONObject(ordenesCompraModel);
			  out.print(json);
	          out.flush();
	          out.close();
			  
		  }
		}
		catch(Exception e){
			Utils.imprimeLog("detalleCargas(): ", e);
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
	
	
	
	public String procesarArchivo() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		OrdenesCompraBean ordenesCompraBean = new OrdenesCompraBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
    	
		try{
		  SiarexSession session = ObtenerSession.getSession(request);
		  if ("".equals(session.getEsquemaEmpresa())){
			return Action.LOGIN;
		  }
		  else{
			  response.setContentType("text/html; charset=UTF-8");
			  response.setCharacterEncoding("UTF-8");
			  PrintWriter out = response.getWriter();
				
			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
			  
			  OrdenesCompraModel ordenesCompraModel = new OrdenesCompraModel();

			  int resultado = ordenesCompraBean.actualizaHistorial(con, session.getEsquemaEmpresa(), getClaveRegistro(), 0, 2, 0, 0, getUsuario(request));
			  
			  if (resultado == -100) {
				  ordenesCompraModel.setCodError("001");
				  ordenesCompraModel.setMensajeError("Error al iniciar proceso de carga de eliminar ordenes de compra, consulte a su administrador.");
				}else {
					
				  ordenesCompraModel.setCodError("000");
				  ordenesCompraModel.setMensajeError("El proceso de carga de eliminar ordenes de compra ha iniciado satisfactoriamente, revise el estatus una vez que ha terminado.");
				  
				  OrdenesCompraForm ordenesCompraForm = ordenesCompraBean.obtenerInformacionCarga(con, rc.getEsquema(), getClaveRegistro());

				  
				  String dirOrdenes = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator + "ORDENES_COMPRA" + File.separator + ordenesCompraForm.getNombreArchivo();
					
				  CargasEliminaComprasBean cargasElimina = new CargasEliminaComprasBean();
				  cargasElimina.iniciaCarga(dirOrdenes, ordenesCompraForm.getNombreArchivo(), getUsuario(request), session.getEsquemaEmpresa(), getClaveRegistro());
				  
				  
				}	
			  
			  JSONObject json = new JSONObject(ordenesCompraModel);
			  out.print(json);
	          out.flush();
	          out.close();
			  
		  }
		}
		catch(Exception e){
			Utils.imprimeLog("detalleCargas(): ", e);
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
	

}
