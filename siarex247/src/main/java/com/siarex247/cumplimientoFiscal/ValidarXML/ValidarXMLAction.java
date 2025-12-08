package com.siarex247.cumplimientoFiscal.ValidarXML;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.LeerXML;
import com.itextpdf.xmltopdf.Retencion;
import com.itextpdf.xmltopdf.Traslado;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.validaciones.UtilsSAT;
import com.siarex247.validaciones.UtilsValidaciones;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class ValidarXMLAction extends ValidarXMLSupport{

	private static final long serialVersionUID = 6380786113519974721L;

	
	public String listaDetalle(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ValidarXMLBean validarXMLBean = new ValidarXMLBean();

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
			    JSONArray jsonArray  = null;
	            Map<String, Object > mapaRes = null;
	            String tipoGrupo = getTipoGrupo();
	            if (tipoGrupo == null){
	            	tipoGrupo = "GPO_IDV";
	            }
	            
	            mapaRes  = validarXMLBean.detalleXML(con, session.getEsquemaEmpresa(), getUsuario(request), tipoGrupo);
	            jsonArray  = (JSONArray) mapaRes.get("detalle");
	            Object totReg = mapaRes.get("totReg");
	            int total = Integer.parseInt(totReg.toString());
	          
	            Map<String, Object> json = new HashMap<String, Object>();
	            JSONArray jsonArrayTotal  = (JSONArray) mapaRes.get("granTotal");
	            
	            json.put("total", total);
	            json.put("data", jsonArray);
	            json.put("granTotal", jsonArrayTotal);
	            
	            String jsonobj = JSONObject.toJSONString(json);
	            out.print(jsonobj);
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
	
	
	

	@SuppressWarnings("unchecked")
	public String iniciaCargaXML(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Connection con = null;
		ResultadoConexion rc = null;

		ValidarXMLForm validarXMLForm = new ValidarXMLForm();
		ValidarXMLBean validarXMLBean = new ValidarXMLBean();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		boolean bandElimina = true;
		int totIngresos = 0;
		int totPago = 0;
		int totEgreso = 0;
		int totNomina = 0;
		int totTraslado = 0;
		int totError = 0;
		
		try{
		   SiarexSession session = ObtenerSession.getSession(request);
		  if ("".equals(session.getEsquemaEmpresa())){
			return Action.LOGIN;
		  }else{
			  response.setContentType("text/html; charset=UTF-8");
	  		  response.setCharacterEncoding("UTF-8");
	  			
			  PrintWriter out = response.getWriter();
			  
			  String bandValidaSAT= getValidarFacturas();

			  Collection<Part> colectionPart  = request.getParts();
	          ArrayList<File> listFiles = UtilsFile.getFilesFromPart(colectionPart);
	            
			  for (File fileContent : listFiles){
						if (!UtilsFile.getContentType(fileContent).equalsIgnoreCase("application/xml") && !UtilsFile.getContentType(fileContent).equalsIgnoreCase("text/xml") ){
							
							JSONObject jsonobj   = new JSONObject();
							jsonobj.put("codError", "001");
							jsonobj.put("MENSAJE", "Error al procesar la información, Debe seleccionar solo archivos con extención .XML");
					        out.print(JSONObject.toJSONString(jsonobj));
				            out.flush();
				            out.close();
					        return null;
						}						
				}
			  
			  
			   rc = getConnection(session.getEsquemaEmpresa());
			   con = rc.getCon();
			    
			  
			  Comprobante _comprobante = null;
			  
			  String fechaFactura = null;
			  String retencionIVA = null;
			  String transladoIVA = null;
			  String retencionISR = null;
			  String tipoMoneda = null;
			  String desMoneda = null;
			  String cadMoneda = null;
			  String arrMoneda [] = null;
			  String datosSAT [] = {"",""};
			  
			  HashMap<String, String> mapaISR = null;
			  HashMap<String, String> mapaTrans = null;
				
			  // UtilsValidaciones utilsVal = new UtilsValidaciones();
			 // int i = 0;
			  
			  for (File file : listFiles){
				  // XML_PROCESANDO = getFilesXMLFileName().get(i);
				 // i++;
				  try {
					  
				     _comprobante = LeerXML.ObtenerComprobante(file.getAbsolutePath());
				     
				     if ("I".equalsIgnoreCase(_comprobante.getTipoDeComprobante()) || "ingreso".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
						  	fechaFactura = _comprobante.getFecha().format(formatter);
						  	tipoMoneda   = _comprobante.getMoneda();
						  	desMoneda 	 = _comprobante.getMoneda();
						  	
						  	cadMoneda = UtilsValidaciones.validaDesMoneda(tipoMoneda);
						  	if (cadMoneda.indexOf(";") > -1){
								arrMoneda  = cadMoneda.split(";");
								// desMoneda = arrMoneda[0];
								tipoMoneda = arrMoneda[1];
								if ("MXN".equalsIgnoreCase(tipoMoneda)){
									tipoMoneda = "1";
								}else{
									tipoMoneda = "2";
								}
							}else{
								tipoMoneda = "1";
							}
							 
						  	
						  	List<Retencion> listRetencion = null;
						  	 List<Traslado> listaTranslados = null;
						  	 
						  	if( _comprobante.getImpuestos() != null) {
								listRetencion = _comprobante.getImpuestos().getRetenciones();
							}
						  	
						  	if( _comprobante.getImpuestos() != null) {
							   	listaTranslados = _comprobante.getImpuestos().getTraslados();
							}
						  	 
						  	mapaISR = UtilsValidaciones.getImporteImpuestoISRNuevo(listRetencion);
						  	mapaTrans = UtilsValidaciones.getImporteTransladosISRNuevo(listaTranslados);
						  	
						  	retencionIVA = mapaISR.get("IVA");
							retencionISR = mapaISR.get("ISR");
							
							transladoIVA = mapaTrans.get("IVA");
							
						    validarXMLForm.setUuid(_comprobante.getComplemento().TimbreFiscalDigital.getUUID());
							validarXMLForm.setSerie(_comprobante.getSerie());
							validarXMLForm.setFolio(_comprobante.getFolio());
							validarXMLForm.setTipoComprobante(Utils.noNuloNormal(_comprobante.getTipoDeComprobante()));
							validarXMLForm.setFechaFactura(fechaFactura);
							validarXMLForm.setFormaPago(_comprobante.getFormaPago());
							validarXMLForm.setMetodoPago(_comprobante.getMetodoPago());
							validarXMLForm.setDesTipoMoneda(desMoneda);
							validarXMLForm.setTipoMoneda(tipoMoneda); // 1 = MXN, 2 = USD
							validarXMLForm.setNumeroCuentaPago(_comprobante.getNumCtaPago());
							validarXMLForm.setSubTotal(_comprobante.getSubTotal());
							validarXMLForm.setDescuento(_comprobante.getDescuento());
							try {
								validarXMLForm.setTotalImpuestoRet(_comprobante.getImpuestos().getTotalImpuestosRetenidos());
								validarXMLForm.setTotalImpuestoTranslado(_comprobante.getImpuestos().getTotalImpuestosTrasladados());
							}catch(Exception e) {
								validarXMLForm.setTotalImpuestoRet(0);
								validarXMLForm.setTotalImpuestoTranslado(0);
							}
							
							validarXMLForm.setTotal(_comprobante.getTotal());
							validarXMLForm.setEmisorRFC(_comprobante.getEmisor().getRfc());
							validarXMLForm.setEmisorNombre(_comprobante.getEmisor().getNombre());
							validarXMLForm.setReceptorRFC(_comprobante.getReceptor().getRfc());
							validarXMLForm.setReceptorNombre(_comprobante.getReceptor().getNombre());
							validarXMLForm.setRetencionIVA(Utils.convertirDouble(retencionIVA));
							validarXMLForm.setTransladoIVA(Utils.convertirDouble(transladoIVA));
							validarXMLForm.setRetencionISR(Utils.convertirDouble(retencionISR));
							validarXMLForm.setTransladoIEPS(0); //esta en ceros
							
							// CORRECCIÓN: Se agrega AQUÍ antes de guardar en BD y usando la instancia correcta
							validarXMLForm.setTipoComprobante(_comprobante.getTipoDeComprobante()); 
							
							if (bandElimina){
								validarXMLBean.eliminaXML(con, session.getEsquemaEmpresa(), getUsuario(request));
								bandElimina = false;
							}
							if ("on".equalsIgnoreCase(bandValidaSAT)){
								datosSAT = UtilsSAT.validaSAT(validarXMLForm.getEmisorRFC(), validarXMLForm.getReceptorRFC(), validarXMLForm.getTotal(), validarXMLForm.getUuid());	
							}
							
							validarXMLForm.setEstadoSAT(datosSAT[0]);
							validarXMLForm.setEstatusSAT(datosSAT[1]);
							
							// Aquí se guarda, por lo que el setTipoComprobante debe haber ocurrido antes
							int resAlta = validarXMLBean.altaXML(con, rc.getEsquema(), validarXMLForm, getUsuario(request));
							if (resAlta == 1) {
								totIngresos++;
							}
						  
					  }else if ("P".equalsIgnoreCase(_comprobante.getTipoDeComprobante()) || "ingreso".equalsIgnoreCase(_comprobante.getTipoDeComprobante())){
						  totPago++;
					  }else if ("E".equalsIgnoreCase(_comprobante.getTipoDeComprobante()) || "egreso".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
						  totEgreso++;
					  }else if ("T".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
						  totTraslado++;
					  }else if ("N".equalsIgnoreCase(_comprobante.getTipoDeComprobante()) || "nomina".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
						  totNomina++;
					  }else {
						  totError++;
					  }
				     
				  }catch(Exception e) {
					  Utils.imprimeLog("", e);
					  totError++;
				  }
				  
			  }
			  
			  
			  JSONObject jsonRetorno   = new JSONObject();
			  jsonRetorno.put("ESTATUS", "OK");
	          jsonRetorno.put("MENSAJE",  "Total de Archivos : "+listFiles.size() +"<br> Archivos Pago : "+totPago+"<br> Archivos Egresos : "+totEgreso+ "<br> Archivos Traslado : " + totTraslado+ "<br> Archivos Nomina : " + totNomina+ "<br> Archivos con Error XML : " + totError);
	          out.print(JSONObject.toJSONString(jsonRetorno));
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
