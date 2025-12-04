package com.siarex247.layOut.OrdenesCompra;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class OrdenesCompraAction extends OrdenesCompraSupport{

	private static final long serialVersionUID = 4558533833515072085L;

	
	public String detalleCargas() throws Exception {
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

			  UsuariosBean usuariosBean = new UsuariosBean();
			  UsuariosForm usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
			  
			  OrdenesCompraModel ordenesCompraModel = new OrdenesCompraModel();
			  ArrayList<OrdenesCompraForm> listaCargas = ordenesCompraBean.detalleHistoricoTotal(con, session.getEsquemaEmpresa(), usuariosForm.getIdPerfil(), getUsuario(request));
			  ordenesCompraModel.setData(listaCargas);
			  ordenesCompraModel.setRecordsFiltered(20);
			  ordenesCompraModel.setDraw(-1);
			  ordenesCompraModel.setRecordsTotal(listaCargas.size());
			  
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
	
	
	
	public String detalleBitacora() throws Exception {
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
			  ArrayList<OrdenesCompraForm> listaCargas = ordenesCompraBean.detalleHistorico(con, rc.getEsquema(), getClaveRegistro(), getTipoReg());
			  ordenesCompraModel.setData(listaCargas);
			  ordenesCompraModel.setRecordsFiltered(20);
			  ordenesCompraModel.setDraw(-1);
			  ordenesCompraModel.setRecordsTotal(listaCargas.size());
			  
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
	
	
	public String cargarOrdenes() throws Exception {
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

			  Part filePart = request.getPart("fileCarga");
          	  File fileCarga = UtilsFile.getFileFromPart(filePart);
          	  
			  if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(UtilsFile.getContentType(fileCarga)) || 
					  "application/vnd.ms-excel".equalsIgnoreCase(UtilsFile.getContentType(fileCarga)) ) {
				  
				  int claveHistorico = ordenesCompraBean.grabarHistorial(con, session.getEsquemaEmpresa(),  fileCarga.getName(), 0, "1",1, 0, 0, getUsuario(request), null);
				  if (claveHistorico == -100) {
					  ordenesCompraModel.setCodError("001");
					  ordenesCompraModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					  ordenesCompraModel.setClaveRegistro(claveHistorico);
					}else {
						 String dirOrdenes = UtilsPATH.REPOSITORIO_DOCUMENTOS +session.getEsquemaEmpresa() + File.separator + "ORDENES_COMPRA" + File.separator + fileCarga.getName() ;
						 File fdes = new File(dirOrdenes);
						 UtilsFile.moveFileDirectory( fileCarga, fdes, true, false, true);
						  
						ordenesCompraModel.setCodError("000");
						ordenesCompraModel.setClaveRegistro(claveHistorico);
						ordenesCompraModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}
				  
			  }else if ("text/plain".equalsIgnoreCase(UtilsFile.getContentType(fileCarga))) {
				  
				  int claveHistorico = ordenesCompraBean.grabarHistorial(con, session.getEsquemaEmpresa(), fileCarga.getName(), 0, "1",1, 0, 0, getUsuario(request), null);
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
				  
				  ordenesCompraModel.setCodError("000");
				  ordenesCompraModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
			  }else {
				  
				  ordenesCompraModel.setCodError("001");
				  ordenesCompraModel.setMensajeError("Error al procesar el archivo de ordenes de compra, solo se permiten archivos excel y/o TXT.");  
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
				  ordenesCompraModel.setMensajeError("Error al iniciar proceso de carga de ordenes de compra, consulte a su administrador.");
				}else {
					
				  ordenesCompraModel.setCodError("000");
				  ordenesCompraModel.setMensajeError("El proceso de carga de ordenes de compra ha iniciado satisfactoriamente, revise el estatus una vez que ha terminado.");
				  
				  ResultadoConexion rcSiarex = getConnectionSiarex();
				  Connection conSiarex = rcSiarex.getCon();
				  
				  AccesoBean accesoBean = new AccesoBean();
				  EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa()); 
				  
				  OrdenesCompraForm ordenesCompraForm = ordenesCompraBean.obtenerInformacionCarga(con, rc.getEsquema(), getClaveRegistro());
				  
				  String dirOrdenes = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator + "ORDENES_COMPRA" + File.separator + ordenesCompraForm.getNombreArchivo();
					
				  CargasComprasBean cargasCompras = new CargasComprasBean();
				  cargasCompras.iniciaCarga(dirOrdenes, ordenesCompraForm.getNombreArchivo(), getUsuario(request), session.getEsquemaEmpresa(), getClaveRegistro(), empresasForm.getRequisicion());
				  
				  conSiarex.close();
				  conSiarex = null;
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
