package com.siarex247.configSistema.CertificadoSAT;

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
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class CertificadoSATAction extends CertificadoSATSupport{

	private static final long serialVersionUID = -7353981535606940789L;

	
	
	public String obtenerCertificados() throws Exception {
		
		
		AccesoBean accesoBean = new AccesoBean();
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	try{
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);

	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  } else{
	  			
				
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
				
				CertificadoSATModel certModel = new CertificadoSATModel();
				
				if ("".equalsIgnoreCase(empresaForm.getArchivoCer())) {
					certModel.setCodError("001");
					certModel.setMensajeError("No cuenta con certificados cargados en Siarex");
					certModel.setTIENE_CERTIFICADO("N");
				}else {
					certModel.setCodError("000");
					certModel.setMensajeError("Si cuenta con certificados cargados en Siarex");
					certModel.setTIENE_CERTIFICADO("S");
				}
				
				JSONObject json = new JSONObject(certModel);
				out.print(json);
	            out.flush();
	            out.close();
	            
	  		 }
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return SUCCESS;
	}
	
	
	public String guardarCertificados() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		CertificadoSATBean certBean = new CertificadoSATBean();
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	
    	
		try{
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);

	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  } else{
	  			rc = getConnectionSiarex();
				con = rc.getCon();

				String respuesta = "";
				CertificadoSATModel certModel = new CertificadoSATModel();
				
				 Part filePart = request.getPart("fileCER");
	          	 File fileCER = UtilsFile.getFileFromPart(filePart);
	          	  
				if (UtilsFile.getContentType(fileCER).equalsIgnoreCase("application/x-x509-ca-cert")){
					respuesta = "CER_VALIDO";
				}else {
					respuesta = "CER_INVALIDO";
					certModel.setCodError("001");
					certModel.setMensajeError("El archivo .CER es incorrecto!");
				}
				
				if (UtilsFile.getContentType(fileCER).equalsIgnoreCase("application/octet-stream") && "CER_VALIDO".equals(respuesta)){
					respuesta = "KEY_VALIDO";
				}else {
					respuesta = "KEY_INVALIDO";
					certModel.setCodError("001");
					certModel.setMensajeError("El archivo .KEY es incorrecto!");
				}
				
				if ("KEY_VALIDO".equals(respuesta)) {
					filePart = request.getPart("fileKEY");
		            File fileKEY = UtilsFile.getFileFromPart(filePart);
		            
					int posCer = fileCER.getName().toLowerCase().indexOf(".cer");
					int posKey = fileKEY.getName().toLowerCase().indexOf(".key");
					
					String nombreCer = fileCER.getName().substring(0, posCer);
					String nombreKey = fileKEY.getName().substring(0, posKey);
					
					certBean.actualizarCertificado(con, session.getEsquemaEmpresa(), nombreCer, nombreKey, getPwdSat());
					respuesta = "OK";
					
					String destinoCer = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator + "CERTIFICADOS" + File.separator + fileCER.getName();
					String destinoKey = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator + "CERTIFICADOS" + File.separator + fileKEY.getName();
					
					File fileCer = new File(destinoCer);
					File fileKey = new File(destinoKey);
					
					UtilsFile.moveFileDirectory(fileCER, fileCer, true, false, true);
					UtilsFile.moveFileDirectory(fileKEY, fileKey, true, false, true);
					
					certModel.setCodError("000");
					certModel.setMensajeError("El registro se ha guardado satisfactoriamente");
					
					
				}
				JSONObject json = new JSONObject(certModel);
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

	
	
}
