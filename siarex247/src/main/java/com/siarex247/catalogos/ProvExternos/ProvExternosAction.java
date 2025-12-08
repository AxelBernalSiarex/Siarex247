package com.siarex247.catalogos.ProvExternos;

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

public class ProvExternosAction extends ProvExternosSupport{

	
	private static final long serialVersionUID = -7622551001742847289L;

	
	public String listaDetalle(){
		Connection con = null;
		ResultadoConexion rc = null;

		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ProvExternosBean provBean = new ProvExternosBean();

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
		    rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();

			ProvExternosModel proExtModel = new ProvExternosModel();
			ArrayList<ProvExternosForm> listaProveedores = provBean.detalleProveedores(con, session.getEsquemaEmpresa());
			
			proExtModel.setData(listaProveedores);
			proExtModel.setRecordsFiltered(20);
			proExtModel.setDraw(-1);
			proExtModel.setRecordsTotal(listaProveedores.size());
			JSONObject json = new JSONObject(proExtModel);
			out.print(json);
            out.flush();
            out.close();
            
		}
		catch(Exception e){
			Utils.imprimeLog("", e);
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

	
	
	
	public String consultarProveedor(){
		Connection con = null;
		ResultadoConexion rc = null;

		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ProvExternosBean provBean = new ProvExternosBean();

		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
		    rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();

			ProvExternosForm provForm = provBean.consultarProveedor(con, rc.getEsquema(), getClaveRegistro());
			
			JSONObject json = new JSONObject(provForm);
			out.print(json);
            out.flush();
            out.close();
            
		}
		catch(Exception e){
			Utils.imprimeLog("", e);
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
	
	
	
	public String altaProveedor() throws Exception {
		Connection conEmpresa = null;
		ResultadoConexion rc = null;
		ProvExternosModel provModel = new ProvExternosModel();
		try{
	    	HttpServletRequest request = ServletActionContext.getRequest();
	    	HttpServletResponse response = ServletActionContext.getResponse();
	    	PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			
		
	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  }else{
	  			rc = getConnection(session.getEsquemaEmpresa());
				conEmpresa = rc.getCon();
				
		
				ProvExternosForm proveForm = new ProvExternosForm();
				proveForm.setIdProveedor(getIdProveedor());
				proveForm.setRazonSocial(getRazonSocial());
				proveForm.setRfc(getRfc());
				proveForm.setCalle(getCalle());
				proveForm.setNumeroInt(getNumeroInt());
				proveForm.setNumeroExt(getNumeroExt());
				proveForm.setColonia(getColonia());
				proveForm.setDelegacion(getDelegacion());
				proveForm.setCiudad(getCiudad());
				proveForm.setEstado(getEstado());

				try{
					proveForm.setCodigoPostal(Integer.parseInt(getCodigoPostal()));	
				}
				catch(NumberFormatException num){
					proveForm.setCodigoPostal(0);
				}

				proveForm.setTelefono(getTelefono());
				proveForm.setNombreContacto(getNombreContacto());
				proveForm.setEmail(getEmail());
				proveForm.setIdReceptor(getIdReceptor());
				
				int claveRegistro = new  ProvExternosBean().altaProveedores(conEmpresa, session.getEsquemaEmpresa(), proveForm);
				
				 if (claveRegistro == -100) {
					 provModel.setCodError("001");
					 provModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}else {
						provModel.setCodError("000");
						provModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}
				  
				    JSONObject json = new JSONObject(provModel);
					out.print(json);
		            out.flush();
		            out.close();
		            
					
	  		  }
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (conEmpresa != null){
					conEmpresa.close();
				}
				conEmpresa = null;
			}catch(Exception e){
				conEmpresa = null;
			}
		}
		return SUCCESS;
	}
	
	
	public String modificaProveedor() throws Exception {
		Connection conEmpresa = null;
		ResultadoConexion rc = null;
		ProvExternosModel provModel = new ProvExternosModel();
		try{
	    	HttpServletRequest request = ServletActionContext.getRequest();
	    	HttpServletResponse response = ServletActionContext.getResponse();
	    	PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			
		
	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  }else{
	  			rc = getConnection(session.getEsquemaEmpresa());
				conEmpresa = rc.getCon();
				
		
				ProvExternosForm proveForm = new ProvExternosForm();
				proveForm.setClaveRegistro(getClaveRegistro());
				proveForm.setIdProveedor(getIdProveedor());
				proveForm.setRazonSocial(getRazonSocial());
				proveForm.setRfc(getRfc());
				proveForm.setCalle(getCalle());
				proveForm.setNumeroInt(getNumeroInt());
				proveForm.setNumeroExt(getNumeroExt());
				proveForm.setColonia(getColonia());
				proveForm.setDelegacion(getDelegacion());
				proveForm.setCiudad(getCiudad());
				proveForm.setEstado(getEstado());

				try{
					proveForm.setCodigoPostal(Integer.parseInt(getCodigoPostal()));	
				}
				catch(NumberFormatException num){
					proveForm.setCodigoPostal(0);
				}

				proveForm.setTelefono(getTelefono());
				proveForm.setNombreContacto(getNombreContacto());
				proveForm.setEmail(getEmail());
				proveForm.setIdReceptor(getIdReceptor());
				
				int totReg = new  ProvExternosBean().actualizaProveedores(conEmpresa, rc.getEsquema(), proveForm);
				
				if (totReg == -100) {
					 provModel.setCodError("001");
					 provModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
				}else {
					provModel.setCodError("000");
					provModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				}
				  
				    JSONObject json = new JSONObject(provModel);
					out.print(json);
		            out.flush();
		            out.close();
		            
					
	  		  }
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (conEmpresa != null){
					conEmpresa.close();
				}
				conEmpresa = null;
			}catch(Exception e){
				conEmpresa = null;
			}
		}
		return SUCCESS;
	}
	
	
	
	public String eliminaProveedores() throws Exception {
		Connection con = null;
		Connection conEmpresa = null;
		ResultadoConexion rc = null;
		ProvExternosBean proveBean = new ProvExternosBean();
		ProvExternosModel provModel = new ProvExternosModel();
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			PrintWriter out = response.getWriter();
			
			SiarexSession session = ObtenerSession.getSession(request);
			rc = getConnection(session.getEsquemaEmpresa());
			conEmpresa = rc.getCon();
			
			int totReg = proveBean.eliminaProveedores(conEmpresa, session.getEsquemaEmpresa(), getClaveRegistro());
			if (totReg == -100) {
				 provModel.setCodError("001");
				 provModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
			}else {
				provModel.setCodError("000");
				provModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
			}
			  
		    JSONObject json = new JSONObject(provModel);
			out.print(json);
            out.flush();
            out.close();
			  
		}catch(Exception e){
			logger.error(e);
		}finally{
			try{
				if (con != null){
					con.close();
				}
				con = null;
				if (conEmpresa != null){
					conEmpresa.close();
				}
				conEmpresa = null;
			}catch(Exception e){
				con = null;
				conEmpresa = null;
			}
		}
		return SUCCESS;
	}
	
}
