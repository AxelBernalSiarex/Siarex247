package com.siarex247.cumplimientoFiscal.Pedimentos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;

public class PedimentosAction extends PedimentosSupport{

	private static final long serialVersionUID = -5234556796453849687L;

	
	public String detallePedimentos(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	
		try{
			PrintWriter out = response.getWriter();
			Map<String, Object > mapaRes = null;
			JSONArray jsonArray  = null;
			SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				String noPedimento = getNoPedimento();
				String fechaInicial = Utils.noNulo(getFechaInicial());
				String fechaFinal = Utils.noNulo(getFechaFinal());

				
				mapaRes = new PedimentosBean().getPedimentos(con, session.getEsquemaEmpresa(), noPedimento, fechaInicial, fechaFinal);

	            jsonArray  = (JSONArray) mapaRes.get("detalle");
	            Map<String, Object> json = new HashMap<String, Object>();

	            json.put("data", jsonArray);
		        String jsonobj = JSONObject.toJSONString(json);
	            out.print(jsonobj);
	            out.flush();
	            out.close();	
			}
		}
		catch(Exception e){
			Utils.imprimeLog("detallePedimentos(): ", e);
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
	
	

	public String iniciaCargaPedimentos(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Connection con = null;
		ResultadoConexion rc = null;
		PrintWriter out = null;
		PedimentosBean pedimentos = new PedimentosBean();
		ArrayList<PedimentosForm> listaPedimentos = null;
		int x = 0;
		int res = 0;
		int numFiles = 0;
		int numFilesOK = 0;
		int numFilesNG = 0;

		try {
			logger.info("************* DETONANDO PROCESO DE PEDIMENTOS ******************");
			logger.info("Tiempo Inicial : "+System.currentTimeMillis());
			   SiarexSession session = ObtenerSession.getSession(request);

			  if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			  }
			  else{
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					out = response.getWriter();
					
					Collection<Part> colectionPart  = request.getParts();
			        ArrayList<File> listFilesPDF = com.siarex247.utils.UtilsFile.getFilesFromPart(colectionPart);
			        
					for (File fileHTTP : listFilesPDF){
						//logger.info("getFilePDF().getAbsolutePath() ===>" + fileHTTP.getAbsolutePath());
						logger.info("NombreArchivo modificado ===>" + fileHTTP.getName());
						listaPedimentos = pedimentos.iniciaProceso(fileHTTP.getAbsolutePath(), session.getEsquemaEmpresa());
						if(listaPedimentos.size() > 0) {
							PedimentosForm pedimentosForm = null;
							
							for(x  = 0; x < listaPedimentos.size(); x++) {
								pedimentosForm = listaPedimentos.get(x);
								res = pedimentos.grabarPedimento(con, session.getEsquemaEmpresa(), pedimentosForm, getUsuario(request));
								if (res == 1062){
									numFilesNG++;
								}
								else {
									numFilesOK++;
								}
								numFiles++;
							 }
						}
					}
					
					logger.info("numFiles ===>" + numFiles);
					logger.info("numFilesOK ===>" + numFilesOK);
					logger.info("numFilesNG ===>" + numFilesNG);
					
			        	Map<String, Object> jsonRetorno = new HashMap<String, Object>();
			        	if (numFilesNG > 0){
			        		jsonRetorno.put("ESTATUS", "ERROR");
			        		jsonRetorno.put("MENSAJE", "Total de Archivos : "+numFiles +", Archivos Exitosos : "+numFilesOK+", Archivos Duplicados : "+numFilesNG);
			        	}else {
			        		jsonRetorno.put("ESTATUS", "OK");
			        		jsonRetorno.put("MENSAJE", "");
			        	}
			            out.print(JSONObject.toJSONString(jsonRetorno));
			            out.flush();
			            out.close();
			        
			  }
			  logger.info("Tiempo Final : "+System.currentTimeMillis());
		}
		catch(Exception e) {
			Utils.imprimeLog("iniciaCargaPedimentos(): ", e);
		}
		return SUCCESS;
	}
	
	public String generaPDF(String numPedimento, HttpServletRequest request) {
		ResultadoConexion rc = null;
    	Connection con = null;
    	String documentoPDF = "";

		try {
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				
				String nombreArchivoSistema = numPedimento + ".pdf";
				String rutaDocumento = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator + "PEDIMENTOS" + File.separator + nombreArchivoSistema;
	  		    InputStream imagenDocumento = new FileInputStream(new File(rutaDocumento));
	  		    String filePath = request.getSession().getServletContext().getRealPath("/");
				File file = new File(filePath + "/files/", nombreArchivoSistema);
				BufferedInputStream in = new BufferedInputStream(imagenDocumento);
				BufferedOutputStream out  = new BufferedOutputStream(new FileOutputStream(file));

				byte[] data = new byte[8896];
				int len = 0;
				while ((len = in.read(data)) > 0) {
					out.write(data, 0, len);
				}
				out.flush();
				out.close();
				in.close();
				documentoPDF = "/siarex247/files/"+nombreArchivoSistema;
			}
		}
		catch(Exception e) {
			Utils.imprimeLog("generaPDF(): ", e);
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
		return documentoPDF;
	}
	
	

	public String exportPedimentoZIP() {
		Connection con = null;
		ResultadoConexion rc = null;
		
    	HttpServletRequest request = ServletActionContext.getRequest();
    	
    	// String directorioXML = null;
    	ArrayList<String> alFiles = new ArrayList<String>();
    	
    	String rutaHTML = null;
    	String nombreArchivoSistema = "";

		try{
			Map<String, Object > mapaRes = null;
			JSONArray jsonArray  = null;
			SiarexSession session = ObtenerSession.getSession(request);

			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}
			else{
				logger.info("Exportando pedimentos...........");
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

				//String rutaFinal = UtilsPATH.getRutaServer(request);
		    	rutaHTML = UtilsPATH.RUTA_PUBLIC_HTML +session.getEsquemaEmpresa() + File.separator +  "TEMP_PDF" + File.separator;				
				// logger.info("rutaHTML====>"+rutaHTML);
				File fileTemp = new File(rutaHTML);
				if (!fileTemp.exists()) {
					fileTemp.mkdirs();
				}
		    	//String repPedimento = "/REPOSITORIOS/"+session.getEsquemaEmpresa()+"/PEDIMENTOS/";
				//String rutaPedimento = rutaFinal + repPedimento;
				
				String bandera = Utils.noNulo(request.getParameter("bandSelecciono"));
				String idRegistro = Utils.noNulo(request.getParameter("idRegistro"));
				String noPedimento = getNoPedimento();
				String fechaInicial = getFechaInicial();
				String fechaFinal = getFechaFinal();

				
				//logger.info("idRegistro ===>" + idRegistro);
				String pathPDF = "";
				logger.info("bandera ===>" + bandera);
				logger.info("idRegistro ===>" + idRegistro);
				
				if("TRUE".equalsIgnoreCase(bandera)) {
					mapaRes  = new PedimentosBean().detallePedimentoZIP(con, session.getEsquemaEmpresa(), idRegistro, noPedimento);
				} else {
					mapaRes = new PedimentosBean().getPedimentos(con, session.getEsquemaEmpresa(), noPedimento, fechaInicial, fechaFinal);
				}

				jsonArray  = (JSONArray) mapaRes.get("detalle");
				JSONObject jsonobj = null;
				//logger.info("jsonArray ===>" + jsonArray);
				// bandLogo = new ConfSistemaBean().obtenerConfiguracionesVariable(con, session.getEsquemaEmpresa(), "BANDERA_LOGO_TOYOTA");

				String dirPedimento = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa() + File.separator + "PEDIMENTOS" + File.separator;
				String filePath = "";
				String rutaDocumento = "";
				for (int x = 0; x < jsonArray.size(); x++){
					jsonobj = (JSONObject) jsonArray.get(x);
					if (!"".equals(jsonobj.get("NUM_PEDIMENTO"))){
						//directorioXML = rutaPedimento + jsonobj.get("NUM_PEDIMENTO")+ ".xml";
						//alFiles.add(directorioXML);

						pathPDF = rutaHTML + jsonobj.get("NUM_PEDIMENTO") + ".pdf";
						// logger.info("directorioXML ===>" + directorioXML);
						// logger.info("pathPDF ===>" + jsonobj.get("NUM_PEDIMENTO") + ".pdf");
						if(!pathPDF.equals("")) {
							try {
								nombreArchivoSistema = jsonobj.get("NUM_PEDIMENTO") + ".pdf";
								rutaDocumento = dirPedimento + nombreArchivoSistema;
								// logger.info("rutaDocumento===>"+rutaDocumento);
					  		    InputStream imagenDocumento = new FileInputStream(new File(rutaDocumento));
					  		    filePath = request.getSession().getServletContext().getRealPath("/");
								File file = new File(filePath + "/files/", nombreArchivoSistema);
								BufferedInputStream in = new BufferedInputStream(imagenDocumento);
								BufferedOutputStream out  = new BufferedOutputStream(new FileOutputStream(file));
								byte[] data = new byte[8896];
								int len = 0;
								while ((len = in.read(data)) > 0) {
									out.write(data, 0, len);
								}
								out.flush();
								out.close();
								in.close();
								//new CreaPDF().GenerarByXML(directorioXML, pathPDF, (rutaPedimento + "/" + logo));
								//new CreaPDF().GenerarPDF(pathPDF, (rutaPedimento + "/" + logo));
								alFiles.add(rutaDocumento);	
							}catch(Exception e) {
								
							}
						}
					}
				}
			}
			if (alFiles.isEmpty()){
				addActionMessage("Usurio y/o Pasword Incorrecto!");
				return  ERROR;
			}
			else{
				ZipFiles zipFiles = new ZipFiles();
				ByteArrayOutputStream dest = zipFiles.zipFiles(alFiles);
				setInputStream(new ByteArrayInputStream(dest.toByteArray()));
			}
			
			File fileTemp = new File(rutaHTML);
			fileTemp.delete();
			logger.info("El directorio ha sido borrado.....");
			
		}catch(Exception e){
			Utils.imprimeLog("exportPedimentoZIP(): ", e);
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
