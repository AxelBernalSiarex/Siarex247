package com.siarex247.layOut.Tareas;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class TareasAction extends TareasSupport{

	private static final long serialVersionUID = 519574025237254718L;

	
	public String detalleTareas() {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	TareasBean tareasBean = new TareasBean();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    
			    TareasModel tareasModel = new TareasModel();
	            ArrayList<TareasForm> listaTareas  = tareasBean.detalleTareasPantalla(con, session.getEsquemaEmpresa());
	            									   
	            tareasModel.setData(listaTareas);
	            tareasModel.setRecordsFiltered(20);
	            tareasModel.setDraw(-1);
	            tareasModel.setRecordsTotal(listaTareas.size());
				JSONObject json = new JSONObject(tareasModel);
				out.print(json);
	            out.flush();
	            out.close();
			}
		}catch(Exception e){
			logger.error(e);
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

	
	public String eliminaTarea() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TareasBean tareasBean = new TareasBean();
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
				TareasModel tareasModel = new TareasModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					int totReg = tareasBean.eliminaTarea(con, rc.getEsquema(), getClaveTarea());
					if (totReg == -100) {
						tareasModel.setCodError("001");
						tareasModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}else {
						tareasModel.setCodError("000");
						tareasModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}	
				
				JSONObject json = new JSONObject(tareasModel);
				out.print(json);
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
	
	
	
	public String cancelaTarea() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		TareasBean tareasBean = new TareasBean();
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
				TareasModel tareasModel = new TareasModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					int totReg = tareasBean.cancelaTarea(con, rc.getEsquema(), getClaveTarea());
					if (totReg == -100) {
						tareasModel.setCodError("001");
						tareasModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}else {
						tareasModel.setCodError("000");
						tareasModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}	
				
				JSONObject json = new JSONObject(tareasModel);
				out.print(json);
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
	
	
	
	public String guardarTarea() throws Exception{
		PrintWriter out = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Connection con = null;
		ResultadoConexion rc = null;
		TareasModel tareasModel = new TareasModel();
		LenguajeBean lenguajeBean = LenguajeBean.instance();
	  try{
	  
		  if ( "TRUE".equalsIgnoreCase(Utils.noNulo(getPreValida()))){
			 // preValidaTarea();
		  }
		  else{
			     response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
			    out = response.getWriter();
			    String nombreArchivo = "";
			    SiarexSession session = ObtenerSession.getSession(request);

				if ("".equals(session.getEsquemaEmpresa())){
					return Action.LOGIN;
				}
				else{
					rc = getConnection(session.getEsquemaEmpresa());
				    con = rc.getCon();
				    
				    HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "TAREAS");
				    Part filePart = request.getPart("filesPDF");
		          	File filesPDF = UtilsFile.getFileFromPart(filePart);
		          	  
		          	  
				   //if (getFilesPDF() != null){
				  		if (filesPDF == null || ! UtilsFile.getContentType(filesPDF).equalsIgnoreCase("application/pdf")){
					  		tareasModel.setCodError("001");
							// tareasModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .PDF para continuar.");
					  		tareasModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG1")));
							con.close();
							JSONObject json = new JSONObject(tareasModel);
							out.print(json);
				            out.flush();
				            out.close();
					        return null;
					  	}
				  		
				  		if ("GEN".equalsIgnoreCase(Utils.noNulo(getTipoAlarma()))) {
				  			if ( filesPDF.length() > 1000000){
				  				tareasModel.setCodError("001");
								// tareasModel.setMensajeError("Error el guardar la información del registro, el tamaño del archivo PDF excede el limite de 1 Mg.");
				  				tareasModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG2")));
								con.close();
								JSONObject json = new JSONObject(tareasModel);
								out.print(json);
					            out.flush();
					            out.close();
						        return null;
						  	}	
				  		}
				  		
						String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
						String rutaRepositorios = rutaFinal + session.getEsquemaEmpresa()+"/TAREAS_SIAREX/";
						File fdesPDF = new File(rutaRepositorios + filesPDF.getName());
						UtilsFile.moveFileDirectory(filesPDF, fdesPDF, true, false, true);
						nombreArchivo = filesPDF.getName();
				  	// }

				    String correoDe = "";
				    if ("on".equalsIgnoreCase(getRequieroCopia())){
				    	UsuariosBean usuariosBean = new UsuariosBean();
				    	UsuariosForm usuariosForm = usuariosBean.datosUsuario(con, rc.getEsquema(), getUsuario(request)); 
				    	correoDe = usuariosForm.getCorreo();
				    }
				    String estatus = "0";
				    if ("on".equalsIgnoreCase(Utils.noNulo(getEnviarAhora()))) {
				    	estatus = "2";
				    }
				    TareasBean tareasBean = new TareasBean();
				    String fechaTarea = getFechaTarea();
				    if (fechaTarea.length() > 19) {
				    	fechaTarea = fechaTarea.substring(0, 19);
				    }
				    String mensajeFormateado = getMensaje();
				    
				    String notificacion = "";
			        int claveTarea = tareasBean.altaTarea(con, session.getEsquemaEmpresa(), getSubject(),correoDe, mensajeFormateado, fechaTarea, nombreArchivo, 
			        		                                 getTipoEnvio(), estatus, getUsuario(request), getNum_Dias1(), getNum_Dias2(), notificacion, getTipoAlarma());
			        
			        if ("GEN".equalsIgnoreCase(getTipoAlarma())) {
			        	// String cadProveedores = getClaveProveedor();
			        	// if (cadProveedores == null || cadProveedores.length() == 0 ) {
			        		int res = tareasBean.altaTareProv(con, session.getEsquemaEmpresa(), claveTarea, getClaveProveedor());
			        	// }
			        }
			        
			        tareasModel.setCodError("000");
					// tareasModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
			        tareasModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG3")));
					JSONObject json = new JSONObject(tareasModel);
					out.print(json);
		            out.flush();
		            out.close();
				}
		  }
	  }
	  catch(Exception e){
		  Utils.imprimeLog("procesaTarea(): ", e);
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
	
	
	
	public String preValidaTarea(){
		PrintWriter out = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Connection con = null;
		ResultadoConexion rc = null;
		String rutaRepositorios = null;
		TareasModel tareasModel = new TareasModel();
		LenguajeBean lenguajeBean = LenguajeBean.instance();
		try{
		    out = response.getWriter();
		    String nombreArchivo = "";
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				 rc = getConnection(session.getEsquemaEmpresa());
				 con = rc.getCon();
				 
				 HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "TAREAS");
				 Part filePart = request.getPart("filesPDF");
		         File filesPDF = UtilsFile.getFileFromPart(filePart);
		          	
				    //logger.info("preValidaTarea() getFilesPDF() ---->" + getFilesPDF());
				    if (filesPDF == null) {
				    	tareasModel.setCodError("001");
						// tareasModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .PDF para continuar.");
				    	tareasModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG4")));
						con.close();
						JSONObject json = new JSONObject(tareasModel);
						out.print(json);
			            out.flush();
			            out.close();
				        return null;
				    }else if (filesPDF != null){
				  		if (!UtilsFile.getContentType(filesPDF).equalsIgnoreCase("application/pdf")){
				  			tareasModel.setCodError("001");
							//tareasModel.setMensajeError("Error el guardar la información del registro, debe seleccionar un archivo .PDF para continuar.");
				  			tareasModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG4")));
							con.close();
							JSONObject json = new JSONObject(tareasModel);
							out.print(json);
				            out.flush();
				            out.close();
					        return null;
					        
					  	}
				  		
						String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
						
						rutaRepositorios = rutaFinal + session.getEsquemaEmpresa()+"/TAREAS_SIAREX/";
						File fdesPDF = new File(rutaRepositorios + filesPDF.getName());
						UtilsFile.moveFileDirectory(filesPDF, fdesPDF, true, false, true);
						nombreArchivo = filesPDF.getName();
					  }
				  	
				    String correoDe = "";
				    
				    
				    String estatus = "0";
				    if ("on".equalsIgnoreCase(Utils.noNulo(getEnviarAhora()))) {
				    	estatus = "2";
				    }
				    
				    TareasBean tareasBean = new TareasBean();
				    String fechaTarea = getFechaTarea();
				    
				    if (fechaTarea.length() > 19) {
				    	fechaTarea = fechaTarea.substring(0, 19);
				    }
				    // 2023-05-31 12:05:53
				    // 2023-05-31 12:05:77
				    String mensajeFormateado = getMensaje();
				    
				    String notificacion = "";
			        int claveRegistro = tareasBean.altaTarea(con, session.getEsquemaEmpresa(), getSubject(), correoDe, mensajeFormateado, fechaTarea, nombreArchivo, 
			        		getTipoEnvio(), estatus, getUsuario(request), getNum_Dias1(), getNum_Dias2(), notificacion, "PRE");
			        
			        if (claveRegistro > 0){
			        	
			        	AccesoBean accesoBean = new AccesoBean();
			        	EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
			        	
			        	OrdenesAlertas ordenesAlertas = new OrdenesAlertas();
			        	String codigoEnvio = "";
			        	if ("NONE".equalsIgnoreCase(getTipoEnvio())){
			        		codigoEnvio = "0";
			        	}else if ("ALL".equalsIgnoreCase(getTipoEnvio())){
			        		codigoEnvio = "1";
			        	}else if ("MEX".equalsIgnoreCase(getTipoEnvio())){
			        		codigoEnvio = "2";
			        	}else if ("USA".equalsIgnoreCase(getTipoEnvio())){
			        		codigoEnvio = "3";
			        	}
			        	
			        	int res = ordenesAlertas.iniciaProceso(con, session.getEsquemaEmpresa(), claveRegistro, rutaRepositorios, rutaRepositorios, nombreArchivo, codigoEnvio, true, empresasForm.getTipoEmpresa());
			        		if (res == 100 || res == 101 || res == 102){ // encontro vendor sin registrar
				        		// out.print("ERROR_PREVALIDA:"+claveRegistro+":"+res);
				        		tareasModel.setCodError("002");
				        		tareasModel.setClaveRegistro(claveRegistro);
								//tareasModel.setMensajeError("El PDF seleccionado contiene proveedores y empleados no registrados en sistema SIAREX ¿ Desea Visualizarlos ?.");
				        		tareasModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG5")));
				        		
				        	}else{
				        		// out.print("PREVALIDA_OK");
				        		tareasModel.setCodError("000");
				        		// tareasModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				        		tareasModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG3")));
				        	}
			        }else {
			        	tareasModel.setCodError("001");
						// tareasModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
			        	tareasModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("MSG6")));
			        }
			        
			        JSONObject json = new JSONObject(tareasModel);
					out.print(json);
		            out.flush();
		            out.close();			        
			}
	  }catch(Exception e){
		  Utils.imprimeLog("preValidaTarea(): ", e);
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
	
	
	public String preValidacionOK()
    {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	TareasBean tareasBean = new TareasBean();
		try{
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    
			    TareasModel tareasModel = new TareasModel();
			    ArrayList<TareasForm> listaTareas = tareasBean.detalleTareasOrdenesOK(con, session.getEsquemaEmpresa(), getClaveTarea());
	            									   
			    tareasModel.setData(listaTareas);
	            tareasModel.setRecordsFiltered(20);
	            tareasModel.setDraw(-1);
	            tareasModel.setRecordsTotal(listaTareas.size());
				JSONObject json = new JSONObject(tareasModel);
				out.print(json);
	            out.flush();
	            out.close();
	            
			}
		}catch(Exception e){
			logger.error(e);
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
	
	
	public String preValidacionNE()
    {
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	TareasBean tareasBean = new TareasBean();
		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    TareasModel tareasModel = new TareasModel();
			    
			    ArrayList<TareasForm> listaTareas = tareasBean.detalleTareasOrdenesNE(con, session.getEsquemaEmpresa(), getClaveTarea());
	            									   
			    tareasModel.setData(listaTareas);
	            tareasModel.setRecordsFiltered(20);
	            tareasModel.setDraw(-1);
	            tareasModel.setRecordsTotal(listaTareas.size());
				JSONObject json = new JSONObject(tareasModel);
				out.print(json);
	            out.flush();
	            out.close();
			}
		}catch(Exception e){
			logger.error(e);
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
