package com.siarex247.layOut.PagosOrdenesCompra;

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
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class PagosOrdenesCompraAction extends OrdenesCompraSupport{

	private static final long serialVersionUID = 226881998020068121L;

	
	

	public String cargarPagos() throws Exception {
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
			  
			  if ("".equals(Utils.noNulo(getFechaPago()))) {
				  ordenesCompraModel.setCodError("001");
				  ordenesCompraModel.setMensajeError("Error al procesar el archivo pagos ordenes de compra, Debe especificar una fecha de pago.");
				  
			  }else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(UtilsFile.getContentType(fileCarga)) || 
					  "application/vnd.ms-excel".equalsIgnoreCase(UtilsFile.getContentType(fileCarga)) ) {
				  
				  int claveHistorico = ordenesCompraBean.grabarHistorial(con, session.getEsquemaEmpresa(), fileCarga.getName(), 0, "3",1, 0, 0, getUsuario(request), getFechaPago());
				  
				  if (claveHistorico == -100) {
					  ordenesCompraModel.setCodError("001");
					  ordenesCompraModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					  ordenesCompraModel.setClaveRegistro(claveHistorico);
					}else {
						 String dirOrdenes = UtilsPATH.REPOSITORIO_DOCUMENTOS +session.getEsquemaEmpresa() + File.separator + "CUENTAS_PAGAR" + File.separator + fileCarga.getName() ;
						 File fdes = new File(dirOrdenes);
						 UtilsFile.moveFileDirectory(fileCarga, fdes, true, false, true);
						  
						ordenesCompraModel.setCodError("000");
						ordenesCompraModel.setClaveRegistro(claveHistorico);
						ordenesCompraModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}
				  
			  }else {
				  
				  ordenesCompraModel.setCodError("001");
				  ordenesCompraModel.setMensajeError("Error al procesar el archivo pagos ordenes de compra, solo se permiten archivos excel.");  
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
				  ordenesCompraModel.setMensajeError("El proceso de carga pagos ordenes de compra ha iniciado satisfactoriamente, revise el estatus una vez que ha terminado.");
				  
				  ResultadoConexion rcSiarex = getConnectionSiarex();
				  Connection conSiarex = rcSiarex.getCon();
				  
				  AccesoBean accesoBean = new AccesoBean();
				  EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa()); 
				  
				  OrdenesCompraForm ordenesCompraForm = ordenesCompraBean.obtenerInformacionCarga(con, rc.getEsquema(), getClaveRegistro());
				  
				  String dirOrdenes = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator + "CUENTAS_PAGAR" + File.separator + ordenesCompraForm.getNombreArchivo();
					
				  
				  logger.info("*************** PROCESANDO ORDENES DE PAGO ******************");
				  CargasCuentasPagarBean cuentasPagar = new CargasCuentasPagarBean();
				  cuentasPagar.iniciaCarga(dirOrdenes, getUsuario(request), session.getEsquemaEmpresa(), getClaveRegistro(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo(), empresasForm.getNombreLargo(), ordenesCompraForm.getFechaPago());
				  
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
