package com.siarex247.layOut.Formatos;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsPATH;

public class FormatosAction extends FormatosSupport{

	private static final long serialVersionUID = 1859523224550217711L;

	
	public String listaFormatos() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		FormatosBean formatosBean = new FormatosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				FormatosModel formatosModel = new FormatosModel();
				ArrayList<FormatosForm> listaFormatos = formatosBean.detalleFormatos(con, session.getEsquemaEmpresa(), getTipoProveedor());

				formatosModel.setData(listaFormatos);
				formatosModel.setRecordsFiltered(20);
				formatosModel.setDraw(-1);
				formatosModel.setRecordsTotal(listaFormatos.size());
				formatosModel.setTotal(listaFormatos.size());

				JSONObject json = new JSONObject(formatosModel);
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
	
	
	
	public String consultaFormato() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		FormatosBean formatoBean = new FormatosBean();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				FormatosForm formatoForm = formatoBean.consultaFormato(con, session.getEsquemaEmpresa(), getIdRegistro());
				JSONObject json = new JSONObject(formatoForm);
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
	
	public String altaFormato() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		FormatosBean formatoBean = new FormatosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				FormatosModel formatoModel = new FormatosModel();
				
				Part filePart = request.getPart("fileFormato");
	          	File fileFormato = UtilsFile.getFileFromPart(filePart);
	          	  
				int totReg = formatoBean.altaFormatos(con, session.getEsquemaEmpresa(), getDescripcion(), getSubjectCorreo(), getCuerpoCorreo(), fileFormato.getName(), getTipoProveedor(), getUsuario(request));
				if (totReg == -100) {
					formatoModel.setCodError("001");
					formatoModel.setMensajeError("Error el guardar la informacion del registro, consulte a su administrador.");
				}else {
					formatoModel.setCodError("000");
					formatoModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					String rutaFinal = UtilsPATH.RUTA_PUBLIC_PRINCIPAL;
					String directorio = "";
					directorio = "/REPOSITORIOS/"+session.getEsquemaEmpresa()+"/FORMATOS/" ;
					String rutaFormatos = rutaFinal + directorio + fileFormato.getName();
					File fileFormDest = new File(rutaFormatos);
					UtilsFile.moveFileDirectory(fileFormato, fileFormDest, true, false, true, false);
				}
				JSONObject json = new JSONObject(formatoModel);
				out.print(json);
	            out.flush();
	            out.close();
			}
		}catch(Exception e){
			Utils.imprimeLog("altaFormato(): ", e);
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
	
	
	public String actualizaFormato() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		FormatosBean formatoBean = new FormatosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				FormatosModel formatoModel = new FormatosModel();
				
				int totReg = formatoBean.updateFormatos(con, session.getEsquemaEmpresa(), getIdRegistro(), getDescripcion(), getSubjectCorreo(), getCuerpoCorreo(), getTipoProveedor(), getUsuario(request));
				if (totReg == -100) {
					formatoModel.setCodError("001");
					formatoModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
				}else {
					formatoModel.setCodError("000");
					formatoModel.setMensajeError("El registro se ha actualizado satisfactoriamente.");
				}
				
				JSONObject json = new JSONObject(formatoModel);
				out.print(json);
	            out.flush();
	            out.close();
			}
		}catch(Exception e){
			Utils.imprimeLog("actualizaFormato(): ", e);
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
	
	
	public String eliminaFormato() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		FormatosBean formatoBean = new FormatosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				PrintWriter out = response.getWriter();
				FormatosModel formatoModel = new FormatosModel();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				FormatosForm formatosForm = formatoBean.consultaFormato(con, session.getEsquemaEmpresa(), getIdRegistro());
				String rutaFinal = UtilsPATH.RUTA_PUBLIC_PRINCIPAL;
				String directorio = "";
				directorio = "/REPOSITORIOS/"+session.getEsquemaEmpresa()+"/FORMATOS/" ;
				String rutaFormatos = rutaFinal + directorio + formatosForm.getNombreArchivo();
				File fileFormato = new File(rutaFormatos);
				fileFormato.delete();
				
				
				int totReg = formatoBean.deleteFormatos(con, session.getEsquemaEmpresa(), getIdRegistro(), getUsuario(request));
				if (totReg == -100) {
					formatoModel.setCodError("001");
					formatoModel.setMensajeError("Error al eliminar el registro, consulte a su administrador.");
				}else {
					formatoModel.setCodError("000");
					formatoModel.setMensajeError("El registro se ha eliminado satisfactoriamente.");
				}
				JSONObject json = new JSONObject(formatoModel);
				out.print(json);
	            out.flush();
	            out.close();
					
			}
		}catch(Exception e){
			Utils.imprimeLog("eliminaFormato(): ", e);
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
	
	
	public String consultaFormatoInstrucciones() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		FormatosBean formatoBean = new FormatosBean();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				FormatosForm formatoForm = formatoBean.consultaFormatoInstrucciones(con, session.getEsquemaEmpresa(), getTipoProveedor());
				JSONObject json = new JSONObject(formatoForm);
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

	
	
	public String altaFormatoInstrucciones() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		FormatosBean formatoBean = new FormatosBean();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				FormatosModel formatoModel = new FormatosModel();
				int totReg = 0;	
				FormatosForm formatoForm = formatoBean.consultaFormatoInstrucciones(con, session.getEsquemaEmpresa(), getTipoProveedor());
				if (formatoForm.getIdRegistro() == 0) {
					totReg = formatoBean.altaFormatosInstrucciones(con, session.getEsquemaEmpresa(), getDescripcion(), getSubjectCorreo(), getCuerpoCorreo(), getTipoProveedor(), getCopiaPara(), getUsuario(request));
				}else {
					totReg = formatoBean.updateFormatosInstruc(con, session.getEsquemaEmpresa(), formatoForm.getIdRegistro(), getDescripcion(), getSubjectCorreo(), getCuerpoCorreo(), getTipoProveedor(), getCopiaPara(),getUsuario(request));
				}
				
				if (totReg == -100) {
					formatoModel.setCodError("001");
					formatoModel.setMensajeError("Error el guardar la informacion del registro, consulte a su administrador.");
				}else {
					formatoModel.setCodError("000");
					formatoModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					String rutaFinal = UtilsPATH.RUTA_PUBLIC_PRINCIPAL;
					String directorio = "";
					directorio = "/REPOSITORIOS/"+session.getEsquemaEmpresa() + File.separator + "FORMATOS"+ File.separator + getTipoProveedor() + File.separator;
					
					Part filePart = request.getPart("fileFormato");
		          	File fileFormato = UtilsFile.getFileFromPart(filePart);
		          	
					if (fileFormato != null) {
						String rutaFormatos = rutaFinal + directorio + "INSTRUCCIONES.pdf";
						File fileFormDest = new File(rutaFormatos);
						// UtilsFile.moveFileDirectory(getFileFormato(), fileFormDest, true, false, true, false);
						
						FileUtils.copyFile(fileFormato, fileFormDest);
						
					}

					filePart = request.getPart("fileFormatoXLS");
		          	File fileFormatoXLS = UtilsFile.getFileFromPart(filePart);
		          	
					if (fileFormatoXLS != null) {
						String rutaFormatosXLS = rutaFinal + directorio + "INSTRUCCIONES.xls";
						File fileFormDestXLS = new File(rutaFormatosXLS);
						// UtilsFile.moveFileDirectory(getFileFormatoXLS(), fileFormDestXLS, true, false, true, false);
						FileUtils.copyFile(fileFormatoXLS, fileFormDestXLS);
						
					}
					
				}
				JSONObject json = new JSONObject(formatoModel);
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
