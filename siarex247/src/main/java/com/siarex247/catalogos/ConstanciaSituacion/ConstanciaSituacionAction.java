package com.siarex247.catalogos.ConstanciaSituacion;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.sat.Catalogos.CatalogosBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class ConstanciaSituacionAction extends ConstanciaSituacionSupport{

	private static final long serialVersionUID = -891071124456254140L;

	
	public String detalleConstancias() throws Exception {
		Connection conSAT = null;
		ResultadoConexion rcSAT = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
		ConstanciaSituacionBean constBean = new ConstanciaSituacionBean();

		try{
		  PrintWriter out = response.getWriter();
		  
		  response.setContentType("text/html; charset=UTF-8");
		  response.setCharacterEncoding("UTF-8");
			
		  SiarexSession session = ObtenerSession.getSession(request);
		  if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
		  }else{
			  rcSAT = getConnectionSAT();
			  conSAT = rcSAT.getCon();
			  
			  AccesoBean accesoBean = new AccesoBean();
              EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
              
			  ConstanciaSituacionModel constModel = new ConstanciaSituacionModel();
			  ArrayList<ConstanciaSituacionForm> listaDetalle = constBean.detalleConstancias(conSAT, rcSAT.getEsquema(), empresaForm.getClaveEmpresa());
			  constModel.setData(listaDetalle);
			  constModel.setRecordsFiltered(20);
			  constModel.setDraw(-1);
			  constModel.setRecordsTotal(listaDetalle.size());
			  JSONObject json = new JSONObject(constModel);
			  out.print(json);
              out.flush();
              out.close();
		  }
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
		  try{
			if (conSAT != null){
				conSAT.close();
			}
			conSAT = null;
		  }catch(Exception e){
			  conSAT = null;
		  }
		}
		return SUCCESS;
	}

	public String consultaConstancia() throws Exception {
		Connection conSAT = null;
		ResultadoConexion rcSAT = null;
		ConstanciaSituacionBean constBean = new ConstanciaSituacionBean();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				PrintWriter out = response.getWriter();
				rcSAT = getConnectionSAT();
				conSAT = rcSAT.getCon();
				ConstanciaSituacionForm constForm = constBean.consultarConstancia(conSAT, rcSAT.getEsquema(), getIdRegistro());
				
				JSONObject json = new JSONObject(constForm);
				out.print(json);
	            out.flush();
	            out.close();
				
			}
			
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				if (conSAT != null){
					conSAT.close();
				}
				conSAT = null;
			}catch(Exception e){
				conSAT = null;
			}
		}
		return SUCCESS;
	}
	
	public String altaCedulaFiscal() throws Exception
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ValidacionesSAT validacionesSAT = new ValidacionesSAT();
        ConstanciaSituacionBean constBean = new ConstanciaSituacionBean();
        ConstanciaSituacionModel contsModel = new ConstanciaSituacionModel();
        
        ResultadoConexion rcSAT = null;
        Connection conSAT = null;
        try {
        	response.setContentType("text/html; charset=UTF-8");
 			response.setCharacterEncoding("UTF-8");
 			
            SiarexSession session = ObtenerSession.getSession(request);

            if ("".equals(session.getEsquemaEmpresa())) {
                return Action.LOGIN;
            } else {
                PrintWriter out = response.getWriter();
                
                rcSAT = getConnectionSAT();
                conSAT = rcSAT.getCon();
                
                String pathCedula = null;
                String datosFiscales [] = null; 
                // LenguajeBean lenguajeBean = LenguajeBean.instance();
                // HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "CAT_CONSTANCIA_SITUACION");
                ConstanciaSituacionForm constForm = null;
                ConstanciaSituacionForm constConsultaForm  = null;
                ConstanciaSituacionForm constCatalogoForm  = null;
                
                CatalogosBean catalogosSAT = new CatalogosBean();
                
                AccesoBean accesoBean = new AccesoBean();
                EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
                
                if ("on".equalsIgnoreCase(Utils.noNulo(getReemplazarConstancia()))) {
                	 
                     String claveRegimen = null;
                     
                     Collection<Part> colectionPart  = request.getParts();
     		         ArrayList<File> listFilesPDF = UtilsFile.getFilesFromPart(colectionPart);
     		        
                      for (File file : listFilesPDF) {
                         datosFiscales = validacionesSAT.obtenerCedulaFiscal(file.getAbsolutePath());
                         if (datosFiscales.length >= 2 && datosFiscales[0].length() > 1) {
                         		constForm = new ConstanciaSituacionForm();
                         		
                         		String rfcConstancia = datosFiscales[0];
                         		constConsultaForm  = constBean.consultarConstanciaRFC(conSAT, rcSAT.getEsquema(), empresaForm.getClaveEmpresa(), rfcConstancia);
                         		constCatalogoForm = constBean.consultarConstancia(conSAT, rcSAT.getEsquema(),getIdRegistro());
                         		if (constConsultaForm.getIdRegistro() == 0 && "reemplazar".equalsIgnoreCase(getTipoAccion())) { // es nueva
                         			contsModel.setCodError("002");
                         			contsModel.setMensajeError("Estimado usuario la constancia que usted esta tratando de ingresar no corresponde al proveedor seleccionado.");
                         		}else if (!constCatalogoForm.getRfc().equalsIgnoreCase(rfcConstancia) && "reemplazar".equalsIgnoreCase(getTipoAccion())) {
                         			contsModel.setCodError("003");
                         			contsModel.setMensajeError("Estimado usuario la constancia que usted esta tratando de ingresar no corresponde al proveedor seleccionado.");
                         		}else {
                     				constForm.setRfc(Utils.noNulo(datosFiscales[0]));
                     				constForm.setCedulaFiscal(Utils.noNulo(datosFiscales[1]));
                     				validacionesSAT.obtenerDatosSAT(constForm);
                     				
                     				claveRegimen = catalogosSAT.consultaRegimenDescripcion(conSAT, rcSAT.getEsquema(), constForm.getRegimen());
                     				if ("".equalsIgnoreCase(claveRegimen)) {
                     					claveRegimen = constBean.consultarRegimen(conSAT, rcSAT.getEsquema(), constForm.getRegimen());
                     				}
                     				constForm.setClaveRegimen(claveRegimen);
                                   	constBean.guardarCedulaFiscal(conSAT, rcSAT.getEsquema(), empresaForm.getClaveEmpresa(), constForm, getUsuario(request));
                                 	pathCedula = UtilsPATH.REPOSITORIO_RAIZ_DOCUMENTOS + "SAT" + "/CEDULAS/" + File.separator + empresaForm.getClaveEmpresa() + File.separator + datosFiscales[0] + ".pdf";
                                 	File fileDest = new File(pathCedula);
                                 	Utils.moveFileDirectory(file, fileDest, true, false, true);
                                 	
                                 	contsModel.setCodError("000");
                 					contsModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
                         		}
                         		
                         }else {
                        	 contsModel.setCodError("001");
                        	 contsModel.setMensajeError("Error el guardar la información del registro, no fue posible leer el archivo .PDF");
                         }
                     }
                }else {
                	String nombreRegimen = catalogosSAT.consultaRegimen(conSAT, rcSAT.getEsquema(), Utils.noNulo(getRegimenFiscal()));
                	
                	constForm = new ConstanciaSituacionForm();
                	constForm.setRfc(Utils.noNulo(getRfc()));
                	constForm.setRazonSocial(Utils.noNulo(getRazonSocial()));
        			constForm.setCedulaFiscal(Utils.noNulo(getCedulaFiscal()));
        			constForm.setRegimenCapital(Utils.noNulo(getRegimenCapital()));
        			constForm.setNombreEmpleado(Utils.noNulo(getNombre()));
        			constForm.setApellidoPaterno(Utils.noNulo(getApellidoPaterno()));
        			constForm.setApellidoMaterno(Utils.noNulo(getApellidoMaterno()));
        			constForm.setFechaNacimiento(Utils.noNulo(getFechaNacimiento()));
        			constForm.setSituacionContribuyente(Utils.noNulo(getSituacionContribuyente()));
        			constForm.setFechaUltCambioSituacion(Utils.noNulo(getFechaUltimoCambio()));
        			constForm.setCurp(Utils.noNulo(getCurp()));
        			constForm.setEntidadFederativa(Utils.noNulo(getEstado()));
        			constForm.setMunicipio(Utils.noNulo(getCiudad()));
        			constForm.setColonia(Utils.noNulo(getColonia()));
        			constForm.setTipoVialidad(Utils.noNulo(getTipoVialidad()));
        			constForm.setNombreVialidad(Utils.noNulo(getNombreVialidad()));
        			constForm.setNumeroExt(Utils.noNulo(getNumExterior()));
        			constForm.setNumeroInt(Utils.noNulo(getNumInterior()));
        			constForm.setCodigoPostal(Utils.noNuloINT(getCodigoPostal()));
        			constForm.setFechaOperaciones(Utils.noNulo(getFechaInicioOperaciones()));
        			constForm.setCorreoElectronico(Utils.noNuloNormal(getCorreo()));
        			constForm.setClaveRegimen(Utils.noNulo(getRegimenFiscal()));
        			constForm.setRegimen(Utils.noNulo(nombreRegimen));
        			constForm.setFechaAlta(Utils.noNulo(getFechaAlta()));
        			
        			int totReg = constBean.guardarCedulaFiscal(conSAT, rcSAT.getEsquema(), empresaForm.getClaveEmpresa(), constForm, getUsuario(request));
                	if (totReg == -100) {
                		contsModel.setCodError("001");
      				  contsModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
                	}else {
                		contsModel.setCodError("000");
     					contsModel.setMensajeError("El registro se ha guardado satisfactoriamente.");	
                	}
        			
                }
               
                JSONObject json = new JSONObject(contsModel);
                out.print(json);
                out.flush();
                out.close();
                logger.info("JSON===>"+json);
            }

        }
        catch (Exception e) {
            Utils.imprimeLog("", e);
        }finally {
        	try {
        		if (conSAT != null) {
        			conSAT.close();
        		}
        		conSAT = null;
        	}catch(Exception e) {
        		conSAT = null;
        	}
        }
        return SUCCESS;
    }
	
	
	public String guardarCedulaFiscal() throws Exception
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ValidacionesSAT validacionesSAT = new ValidacionesSAT();
        ConstanciaSituacionBean constBean = new ConstanciaSituacionBean();
        ConstanciaSituacionModel contsModel = new ConstanciaSituacionModel();
        
        ResultadoConexion rcSAT = null;
        Connection conSAT = null;
        
        try {
        	response.setContentType("text/html; charset=UTF-8");
 			response.setCharacterEncoding("UTF-8");
 			
            SiarexSession session = ObtenerSession.getSession(request);

            if ("".equals(session.getEsquemaEmpresa())) {
                return Action.LOGIN;
            } else {
                PrintWriter out = response.getWriter();
                
                rcSAT = getConnectionSAT();
                conSAT = rcSAT.getCon();
                
                String pathCedula = null;
                int totalArchivos = 0;
                String datosFiscales [] = null; 
                
                ConstanciaSituacionForm constForm = null;
                CatalogosBean catalogosSAT = new CatalogosBean();
                String claveRegimen = null;
                
                AccesoBean accesoBean = new AccesoBean();
                EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
                
                
                Collection<Part> colectionPart  = request.getParts();
 		        ArrayList<File> listFilesPDF = com.siarex247.utils.UtilsFile.getFilesFromPart(colectionPart);
 		        
                for (File file : listFilesPDF) {
                    datosFiscales = validacionesSAT.obtenerCedulaFiscal(file.getAbsolutePath());
                    if (datosFiscales.length >= 2 && datosFiscales[0].length() > 1) {
                    	constForm = new ConstanciaSituacionForm();
        				constForm.setRfc(Utils.noNulo(datosFiscales[0]));
        				constForm.setCedulaFiscal(Utils.noNulo(datosFiscales[1]));
        				// constForm.setClaveRegimen("602");
        				validacionesSAT.obtenerDatosSAT(constForm);
        				logger.info("regimen===>"+constForm.getRegimen());
        				claveRegimen = catalogosSAT.consultaRegimenDescripcion(conSAT, rcSAT.getEsquema(), constForm.getRegimen());
        				if ("".equalsIgnoreCase(claveRegimen)) {
        					claveRegimen = constBean.consultarRegimen(conSAT, rcSAT.getEsquema(), constForm.getRegimen());
        				}
        				logger.info("claveRegimen===>"+claveRegimen);
        				constForm.setClaveRegimen(claveRegimen);
        				
                      	constBean.guardarCedulaFiscal(conSAT, rcSAT.getEsquema(), empresaForm.getClaveEmpresa(), constForm, getUsuario(request));
                    	pathCedula = UtilsPATH.REPOSITORIO_RAIZ_DOCUMENTOS + "SAT" + "/CEDULAS/" + File.separator + empresaForm.getClaveEmpresa() + File.separator + datosFiscales[0] + ".pdf";
                    	File fileDest = new File(pathCedula);
                    	Utils.moveFileDirectory(file, fileDest, true, false, true);	
                    }
                    totalArchivos++;
                }
                
                LenguajeBean lenguajeBean = LenguajeBean.instance();
                HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "CAT_CONSTANCIA_SITUACION");

                String MENSAJE_CARGA_EXITOSA = Utils.noNuloNormal(mapaLenguaje.get("ETQ22"))
                								.replaceAll("<<numFiles>>", String.valueOf(totalArchivos))
                								.replaceAll("<<totFiles>>", String.valueOf(listFilesPDF.size()));
                contsModel.setCodError("000");
                contsModel.setMensajeError(MENSAJE_CARGA_EXITOSA);

                JSONObject json = new JSONObject(contsModel);
                out.print(json);
                out.flush();
                out.close();

            }

        }
        catch (Exception e) {
            Utils.imprimeLog("", e);
        }finally {
        	try {
        		if (conSAT != null) {
        			conSAT.close();
        		}
        		conSAT = null;
        	}catch(Exception e) {
        		conSAT = null;
        	}
        }
        return SUCCESS;
    }
 
	public String eliminarConstancia() {
		Connection conSAT = null;
		ResultadoConexion rcSAT = null;
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ConstanciaSituacionBean constBean = new ConstanciaSituacionBean();
        ConstanciaSituacionModel contsModel = new ConstanciaSituacionModel();
		try{
			
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
		  SiarexSession session = ObtenerSession.getSession(request);
		  if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
		  }
		  else{
			  PrintWriter out = response.getWriter();
			  rcSAT = getConnectionSAT();
			  conSAT = rcSAT.getCon();
			  
			  
			  int totReg = constBean.eliminar(conSAT, rcSAT.getEsquema(), getIdRegistro(), getUsuario(request));
			  
			  if (totReg == -100) {
				  contsModel.setCodError("001");
				  contsModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
				}else {
					contsModel.setCodError("000");
					contsModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				}

			    org.json.JSONObject json = new org.json.JSONObject(contsModel);
				out.print(json);
	            out.flush();
	            out.close();
		  }
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
		  try{
			if (conSAT != null){
				conSAT.close();
			}
			conSAT = null;
		  }catch(Exception e){
			conSAT = null;
		  }
		}
		return SUCCESS;
	}
	
	public String mostrarDocumentos(HttpServletRequest request) throws Exception {
		String pathArchivo = "";
		ResultadoConexion rcSAT = null;
		ConexionDB connPool = new ConexionDB();
		Connection conSAT = null;
		ConstanciaSituacionBean constBean = new ConstanciaSituacionBean();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				rcSAT = connPool.getConnectionSAT();
				conSAT = rcSAT.getCon();
				
				String cedulaFiscal = Utils.noNulo(request.getParameter("cedulaFiscal"));
				int idRegistro = Utils.noNuloINT(request.getParameter("idRegistro"));
				
				AccesoBean accesoBean = new AccesoBean();
                EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
                
				ConstanciaSituacionForm constForm = constBean.consultarConstanciaPDF(conSAT, rcSAT.getEsquema(), idRegistro, cedulaFiscal);
				File fileArchivo = null;
				String nombreArchivo = null;
				String rutaPDF = UtilsPATH.REPOSITORIO_RAIZ_DOCUMENTOS + "SAT" + File.separator + "CEDULAS" + File.separator + empresaForm.getClaveEmpresa() + File.separator + constForm.getRfc() + ".pdf";
				fileArchivo = new File(rutaPDF);
				nombreArchivo  = constForm.getRfc() + ".pdf";
				
				InputStream imagenEmpleado = new FileInputStream(fileArchivo);
				String filePath = request.getSession().getServletContext().getRealPath("/");
				File file = new File(filePath + "/files/", nombreArchivo);
				BufferedInputStream in = new BufferedInputStream(imagenEmpleado);
				BufferedOutputStream out  = new BufferedOutputStream(new FileOutputStream(file));
				byte[] data = new byte[8896];
				int len = 0;
				while ((len = in.read(data)) > 0) {
					out.write(data, 0, len);
				}
				out.flush();
				out.close();
				in.close();
				pathArchivo = "/siarex247/files/"+nombreArchivo;
				
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
			pathArchivo = "/siarex247/html/sinOrden.html";
		}finally {
			try {
				if (conSAT != null) {
					conSAT.close();
				}
				conSAT = null;
			} catch (Exception e2) {
				conSAT = null;
			}
		}
		return pathArchivo;
	}
		
 }
