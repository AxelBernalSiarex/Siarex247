package com.siarex247.catalogos.Proveedores;

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
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Puestos.PuestosModel;
import com.siarex247.layOut.Formatos.FormatosBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;

public class ProveedoresAction extends ProveedoresSupport{

	
	private static final long serialVersionUID = 1303366290439356751L;

	private InputStream inputStream;
	
	
	public String listadoProveedores(){
		Connection con = null;
		ResultadoConexion rc = null;
		
		HttpServletResponse response = ServletActionContext.getResponse();
    	HttpServletRequest request = ServletActionContext.getRequest();
    	ProveedoresBean provBean = new ProveedoresBean();
		try{
			PrintWriter out = response.getWriter();
		    SiarexSession session = ObtenerSession.getSession(request);
		    rc = getConnection(session.getEsquemaEmpresa());
		    con = rc.getCon();

		    response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
            ProveedoresModel provModel = new ProveedoresModel();
            ArrayList<ProveedoresForm> listaProveedores = provBean.detalleProveedores(con, session.getEsquemaEmpresa());
            
            provModel.setData(listaProveedores);
            provModel.setRecordsFiltered(20);
            provModel.setDraw(-1);
            provModel.setRecordsTotal(listaProveedores.size());
			JSONObject json = new JSONObject(provModel);
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
	
	
	
	public String consultaProveedor() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		ProveedoresBean provBean = new ProveedoresBean();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		
		UsuariosBean usuariosBean = new UsuariosBean();
		UsuariosForm usuariosForm = null;
		try{
			SiarexSession session = ObtenerSession.getSession(request);
			if ("".equals(session.getEsquemaEmpresa())){
				return Action.LOGIN;
			}else{
		    	response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");

				PrintWriter out = response.getWriter();
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();
				ProveedoresForm provForm = provBean.consultarProveedor(con, rc.getEsquema(), getClaveRegistro());
				provBean.infoProveedoresMail(con, rc.getEsquema(), getClaveRegistro(), provForm);
				
				usuariosForm = usuariosBean.informacionUsuarioProveedor(con, rc.getEsquema(), "PROV_"+getClaveRegistro());
				
				provForm.setUsuariosForm(usuariosForm);
				
				JSONObject json = new JSONObject(provForm);
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
	
	
	public String altaProveedores() throws Exception {
		Connection conEmpresa = null;
		ResultadoConexion rc = null;
		ProveedoresBean proveBean = new ProveedoresBean();
		ProveedoresModel provModel = new ProveedoresModel();
		LenguajeBean lenguajeBean = LenguajeBean.instance();
		try{
	    	HttpServletRequest request = ServletActionContext.getRequest();
	    	HttpServletResponse response = ServletActionContext.getResponse();
	    	PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			
	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  }else{
	  			rc = getConnection(session.getEsquemaEmpresa());
				conEmpresa = rc.getCon();
				
				HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "CAT_PROVEEDORES");

				
				ProveedoresForm proveForm = new ProveedoresForm();
				proveForm.setIdProveedor(getIdProveedor());
				proveForm.setRazonSocial(getRazonSocial());
				proveForm.setRfc(getRfc());
				proveForm.setDomicilio(getDomicilio());
				proveForm.setCalle(getCalle());
				proveForm.setNumeroInt(getNumeroInt());
				proveForm.setNumeroExt(getNumeroExt());
				proveForm.setColonia(getColonia());
				proveForm.setDelegacion(getDelegacion());
				proveForm.setCiudad(getCiudad());
				proveForm.setEstado(getEstado());
				try{
					proveForm.setCodigoPostal(getCodigoPostal());	
				}catch(NumberFormatException num){
					proveForm.setCodigoPostal(0);
				}
				proveForm.setTelefono(getTelefono());
				proveForm.setExtencion(getExtencion());
				proveForm.setNombreContacto(getNombreContacto());
				proveForm.setEmail(getEmail());
				proveForm.setTipoProveedor(getTipoProveedor());
				proveForm.setTipoConfirmacion(getTipoConfirmacion());
				proveForm.setBanco(getBanco());
				proveForm.setSucursal(getSucursal());
				proveForm.setNombreSucursal(getNombreSucursal());
				proveForm.setNumeroCuenta(getNumeroCuenta());
				proveForm.setCuentaClabe(getCuentaClabe());
				proveForm.setNumeroConvenio(getNumeroConvenio());
				proveForm.setMoneda(getMoneda());

				proveForm.setBancoDollar(getBancoDollar());
				proveForm.setSucursalDollar(getSucursalDollar());
				proveForm.setNombreSucursalDollar(getNombreSucursalDollar());
				proveForm.setNumeroCuentaDollar(getNumeroCuentaDollar());
				proveForm.setCuentaClabeDollar(getCuentaClabeDollar());
				proveForm.setNumeroConvenioDollar(getNumeroConvenioDollar());
				proveForm.setMonedaDollar(getMonedaDollar());
				proveForm.setAbaDollar(getAbaDollar());
				proveForm.setSwitfCodeDollar(getSwitfCodeDollar());
				
				proveForm.setBancoOtro(getBancoOtro());
				proveForm.setSucursalOtro(getSucursalOtro());
				proveForm.setNombreSucursalOtro(getNombreSucursalOtro());
				proveForm.setNumeroCuentaOtro(getNumeroCuentaOtro());
				proveForm.setCuentaClabeOtro(getCuentaClabeOtro());
				proveForm.setNumeroConvenioOtro(getNumeroConvenioOtro());
				proveForm.setMonedaOtro(getMonedaOtro());
				proveForm.setAbaOtro(getAbaOtro());
				proveForm.setSwitfCodeOtro(getSwitfCodeOtro());
				
				if ("on".equalsIgnoreCase(Utils.noNulo(getAnexo24()))) {
					proveForm.setAnexo24("1");	
				}else {
					proveForm.setAnexo24("0");
				}
				
				
				if ("".equals(getLimiteTolerancia())){
					setLimiteTolerancia("0");
				}
				proveForm.setLimiteTolerancia(getLimiteTolerancia());
				
				if ("".equals(getLimiteComplemento())){
					setLimiteComplemento("0");
				}
				proveForm.setLimiteComplemento(getLimiteComplemento());
				
				
				proveForm.setNotComUsuario(Utils.validaCheck(getNotComUsuario()));
				proveForm.setNotPagoUsuario(Utils.validaCheck(getNotPagoUsuario()));
				proveForm.setBandDescuento(Utils.validaCheck(getBandDescuento()));
				proveForm.setNumeroCuentaProveedor(getNumeroCuentaProveedor());
				proveForm.setCentroCostos(getCentroCostos());
				
				proveForm.setUsuarioGeneracion(getUsuario(request).toLowerCase());
				
				proveForm.setEmail1(getEmail1());
				proveForm.setEmail2(getEmail2());
				proveForm.setEmail3(getEmail3());
				proveForm.setEmail4(getEmail4());
				proveForm.setEmail5(getEmail5());
				

				proveForm.setTipoEmail1(Utils.validaCheck(getTipoEmail1()));
				proveForm.setTipoEmail2(Utils.validaCheck(getTipoEmail2()));
				proveForm.setTipoEmail3(Utils.validaCheck(getTipoEmail3()));
				proveForm.setTipoEmail4(Utils.validaCheck(getTipoEmail4()));
				proveForm.setTipoEmail5(Utils.validaCheck(getTipoEmail5()));
				proveForm.setTipoEmail6(Utils.validaCheck(getTipoEmail6()));
				proveForm.setTipoEmail7(Utils.validaCheck(getTipoEmail7()));
				proveForm.setTipoEmail8(Utils.validaCheck(getTipoEmail8()));
				proveForm.setTipoEmail9(Utils.validaCheck(getTipoEmail9()));
				proveForm.setTipoEmail10(Utils.validaCheck(getTipoEmail10()));
				
				proveForm.setConServicio(getConServicio());
				proveForm.setFormaPago(getFormaPago());
				proveForm.setNumEstrellas(getNumEstrellas());
				proveForm.setRazonProveedor(getRazonProveedor());
				
				proveForm.setPagoDolares(getPagoDolares());
				proveForm.setPagoPesos(getPagoPesos());
				
				proveForm.setAMERICANOS_SERIE(getAMERICANOS_SERIE());
				proveForm.setAMERICANOS_FOLIO(getAMERICANOS_FOLIO());	
				
				proveForm.setAMERICANOS_ACCESO(Utils.validaCheck(getPERMITIR_ACCESO_GENERADOR()));
				proveForm.setBandSAT(Utils.validaCheck(getBandSAT()));
				proveForm.setBandIMSS(Utils.validaCheck(getBandIMSS()));
				proveForm.setNumRegistro(getNumRegistro());
				//proveForm.setServEsp(getServEsp());
				proveForm.setServEsp("N");
				proveForm.setCartaPorte(Utils.validaCheck(getBandCartaPorte()));
				proveForm.setRegimenFiscal(getRegimenFiscal());

				Part filePart = null;
				File filesIMSS = null;
				try {
					filePart = request.getPart("filesIMSS");
		          	filesIMSS = UtilsFile.getFileFromPart(filePart);	
				}catch(Exception e) {
					filesIMSS = null;
				}
				
	          	  
				if (filesIMSS == null) {
					proveForm.setTieneIMSS("N");	
				} else {
					proveForm.setTieneIMSS("S");
					if (!"application/pdf".equalsIgnoreCase(UtilsFile.getContentType(filesIMSS))) {
						// retornar error no selecciono archivo pdf del IMSS
						provModel.setCodError("001");
						//provModel.setMensajeError("Error el guardar la información del registro, debe especificar un archivo PDF en el certificado del IMSS.");
						provModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("VAL1")));
						try {
							conEmpresa.close();
						}catch(Exception e) {
							conEmpresa = null;
						}
						
						return null;  
						
					}
				}
				
				File filesSAT = null;
				try {
					filePart = request.getPart("filesSAT");
					filesSAT = UtilsFile.getFileFromPart(filePart);	
				}catch(Exception e) {
					filesSAT = null;
				}
				
				if (filesSAT == null) {
					proveForm.setTieneSAT("N");	
				}else {
					proveForm.setTieneSAT("S");
					if (!"application/pdf".equalsIgnoreCase(UtilsFile.getContentType(filesSAT))) {
						provModel.setCodError("001");
						//provModel.setMensajeError("Error el guardar la información del registro, debe especificar un archivo PDF en el certificado del SAT.");
						provModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("VAL2")));
						try {
							conEmpresa.close();
						}catch(Exception e) {
							conEmpresa = null;
						}
						
						return null;  
					}
				}
				
				File filesConfidencialidad = null;
				try {
					filePart = request.getPart("filesConfidencialidad");
					filesConfidencialidad = UtilsFile.getFileFromPart(filePart);	
				}catch(Exception e) {
					filesConfidencialidad = null;
				}
				
				if (filesConfidencialidad == null) {
					proveForm.setTieneConfidencial("N");	
				}else {
					proveForm.setTieneConfidencial("S");
					if (!"application/pdf".equalsIgnoreCase(UtilsFile.getContentType(filesConfidencialidad))) {
						provModel.setCodError("001");
						//provModel.setMensajeError("Error el guardar la información del registro, debe especificar un archivo PDF en el contrato de confidencialidad.");
						provModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("VAL3")));
						try {
							conEmpresa.close();
						}catch(Exception e) {
							conEmpresa = null;
						}
						
						return null;  
					}
				}
				
				ProveedoresForm provConsulta =  null;
				if ("MEX".equalsIgnoreCase(proveForm.getTipoProveedor())) {
					provConsulta =  proveBean.consultarProveedorXrfc(conEmpresa, rc.getEsquema(), proveForm.getRfc());	
				}else {
					provConsulta =  new ProveedoresForm();
				}
				
				
				
				if (provConsulta.getClaveRegistro() > 0) {
					provModel.setCodError("001");
					//provModel.setMensajeError("Error el guardar la información del registro, El RFC especificado ya existe en nuestra base de dato.");
					provModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("VAL4")));
				}else {
					int claveRegistro = proveBean.altaProveedores(conEmpresa, session.getEsquemaEmpresa(), proveForm);
					//logger.info("claveRegistro===>"+claveRegistro);
					if (claveRegistro == 1062) {
						provModel.setCodError("001");
						//provModel.setMensajeError("Error el guardar la información del registro, el RFC del proveedor ya se encuentra asignado.");
						provModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("VAL5")));
					}else {
						provModel.setCodError("000");
				  		provModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
				  		
				  		proveForm.setClaveRegistro(claveRegistro);
				  		proveBean.altaProveedoresMail(conEmpresa, session.getEsquemaEmpresa(), proveForm);
				  		
				  		
						String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
						String directorio = "";
						directorio =  session.getEsquemaEmpresa()+"/PROVEEDORES/" + proveForm.getClaveRegistro() + "/" ;
						  
						String nombreArchivoIMSS = "RFC"+proveForm.getRfc() +"_" + "Cert_IMSS.pdf";
						String rutaPDFOriginal = rutaFinal + directorio + nombreArchivoIMSS;

						if (filesIMSS != null) {
							File filePDFDest = new File(rutaPDFOriginal);
							UtilsFile.moveFileDirectory(filesIMSS, filePDFDest, true, false, true, false);
							
						}
						
						String nombreArchivoSAT = "RFC"+proveForm.getRfc() +"_" + "Cert_SAT.pdf";
						String rutaPDFSATOriginal = rutaFinal + directorio + nombreArchivoSAT;

						if (filesSAT != null) {
							File filePDFDestSAT = new File(rutaPDFSATOriginal);
							UtilsFile.moveFileDirectory(filesSAT, filePDFDestSAT, true, false, true, false);					
						}

						
						String nombreArchivoConfidencialidad = "RFC"+proveForm.getRfc() +"_" + "Conf.pdf";
						String rutaPDFConfidencialidadOriginal = rutaFinal + directorio + nombreArchivoConfidencialidad;

						if (filesConfidencialidad != null) {
							File filePDFDestConf = new File(rutaPDFConfidencialidadOriginal);
							UtilsFile.moveFileDirectory(filesConfidencialidad, filePDFDestConf, true, false, true, false);					
						}

						  //logger.info("... nuevoProveedores() 8 ..." + claveRegistro);
							
						//logger.info("... nuevoProveedores() 9 ...");
						if ("on".equalsIgnoreCase(getBandInstrucciones()) || "S".equalsIgnoreCase(getBandInstrucciones())) {
							FormatosBean formatoBean = new FormatosBean();
							AccesoBean accesoBean = new AccesoBean();
							EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
							String directorioFormato = "";
							directorioFormato =  session.getEsquemaEmpresa() + File.separator + "FORMATOS"+ File.separator + proveForm.getTipoProveedor() + File.separator;
							String  rutaFormatos = rutaFinal + directorioFormato;
							formatoBean.enviaFormatoProveedor(conEmpresa, session.getEsquemaEmpresa(), claveRegistro, proveForm.getRazonSocial(), proveForm.getRfc(), proveForm.getTipoProveedor(), rutaFormatos, getUsrAcceso(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
							//logger.info("... nuevoProveedores() 10 ...");
						}
						
						if ("S".equalsIgnoreCase(Utils.validaCheck(getPermitirAcceso()))) {
							UsuariosBean usuariosBean = new UsuariosBean();
							usuariosBean.altaUsuarios(conEmpresa, session.getEsquemaEmpresa(), getUsrAcceso().toLowerCase(), "PROV_"+claveRegistro, getRazonSocial(), getUsrAcceso(), 4, getUsuario(request));
							
							AccesoBean accesoBean = new AccesoBean();
							EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
							String pwdUsuario = accesoBean.existeSiarexActivo(getUsrAcceso());
							long tiempoAcceso = System.currentTimeMillis();
						  	String codigoAcceso = Utils.encryptarMD5(String.valueOf( tiempoAcceso ));
						  	
							if ("".equalsIgnoreCase(pwdUsuario) ) { // si NO existe, se envia correo
							  	accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getRazonSocial(), getUsrAcceso(), null, getUsrAcceso(), 4, "PROV_"+claveRegistro, "N", codigoAcceso, getUsuario(request));
							  	
							  	String urlAcceso = "https://"+ UtilsPATH.DOMINIO_PRINCIPAL + "/siarexLogin/registroSiarex.jsp?c="+ codigoAcceso;
							    String htmlAcceso = UtilsHTML.generaHTMLAcceso(getRazonSocial(), urlAcceso, UtilsPATH.DOMINIO_PRINCIPAL);
							   // logger.info("htmlAcceso====>"+htmlAcceso);
							    String emailTO [] = {getUsrAcceso()};
							    EnviaCorreoGrid.enviarCorreo(null, htmlAcceso, false, emailTO, null, "SIAREX - Registrar Acceso", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());						    
							    
							}else {
								accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getRazonSocial(), getUsrAcceso(), pwdUsuario, getUsrAcceso(), 4, "PROV_"+claveRegistro, "S", codigoAcceso, getUsuario(request));
							}							
						}
						
					}	
				}
	  		  }
	  		
	  		JSONObject json = new JSONObject(provModel);
			out.print(json);
            out.flush();
            out.close();
            
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
	
	
	public String modificaProveedores() throws Exception {
		Connection conEmpresa = null;
		ResultadoConexion rc = null;
		ProveedoresBean proveBean = new ProveedoresBean();
		HttpServletResponse response = ServletActionContext.getResponse();
		ProveedoresModel provModel = new ProveedoresModel();
		LenguajeBean lenguajeBean = LenguajeBean.instance();

		try{
			HttpServletRequest request = ServletActionContext.getRequest();	
			PrintWriter out = response.getWriter();
			SiarexSession session = ObtenerSession.getSession(request);

	  		  if ("".equals(session.getEsquemaEmpresa())){
	  				return Action.LOGIN;
	  		  }else{
	  			rc = getConnection(session.getEsquemaEmpresa());
				conEmpresa = rc.getCon();
				
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
				HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(session.getLenguaje(), "CAT_PROVEEDORES");


				String remplazarCertificacion = Utils.noNulo(request.getParameter("remplazarCertificacion"));
				// logger.info("remplazarCertificacion------->"+remplazarCertificacion);
				

				logger.info("**************** MODIFICANDO PROVEEDOR *****************" + getRazonSocial());
				logger.info("**************** getIdProveedor *****************" + getIdProveedor());
				ProveedoresForm proveForm = new ProveedoresForm();
				proveForm.setClaveRegistro(getClaveRegistro());
				proveForm.setIdProveedor(getIdProveedor());
				proveForm.setRazonSocial(getRazonSocial());
				proveForm.setRfc(getRfc());
				proveForm.setDomicilio(getDomicilio());
				proveForm.setCalle(getCalle());
				proveForm.setNumeroInt(getNumeroInt());
				proveForm.setNumeroExt(getNumeroExt());
				proveForm.setColonia(getColonia());
				proveForm.setDelegacion(getDelegacion());
				proveForm.setCiudad(getCiudad());
				proveForm.setEstado(getEstado());
				try{
					proveForm.setCodigoPostal(getCodigoPostal());	
				}catch(NumberFormatException num){
					proveForm.setCodigoPostal(0);
				}
				proveForm.setTelefono(getTelefono());
				proveForm.setExtencion(getExtencion());
				proveForm.setNombreContacto(getNombreContacto());
				proveForm.setEmail(getEmail());
				proveForm.setTipoProveedor(getTipoProveedor());
				proveForm.setTipoConfirmacion(getTipoConfirmacion());
				proveForm.setBanco(getBanco());
				proveForm.setSucursal(getSucursal());
				proveForm.setNombreSucursal(getNombreSucursal());
				proveForm.setNumeroCuenta(getNumeroCuenta());
				proveForm.setCuentaClabe(getCuentaClabe());
				proveForm.setNumeroConvenio(getNumeroConvenio());
				proveForm.setMoneda(getMoneda());

				proveForm.setBancoDollar(getBancoDollar());
				proveForm.setSucursalDollar(getSucursalDollar());
				proveForm.setNombreSucursalDollar(getNombreSucursalDollar());
				proveForm.setNumeroCuentaDollar(getNumeroCuentaDollar());
				proveForm.setCuentaClabeDollar(getCuentaClabeDollar());
				proveForm.setNumeroConvenioDollar(getNumeroConvenioDollar());
				proveForm.setMonedaDollar(getMonedaDollar());
				proveForm.setAbaDollar(getAbaDollar());
				proveForm.setSwitfCodeDollar(getSwitfCodeDollar());
				
				proveForm.setBancoOtro(getBancoOtro());
				proveForm.setSucursalOtro(getSucursalOtro());
				proveForm.setNombreSucursalOtro(getNombreSucursalOtro());
				proveForm.setNumeroCuentaOtro(getNumeroCuentaOtro());
				proveForm.setCuentaClabeOtro(getCuentaClabeOtro());
				proveForm.setNumeroConvenioOtro(getNumeroConvenioOtro());
				proveForm.setMonedaOtro(getMonedaOtro());
				proveForm.setAbaOtro(getAbaOtro());
				proveForm.setSwitfCodeOtro(getSwitfCodeOtro());
				
				if ("on".equalsIgnoreCase(Utils.noNulo(getAnexo24()))) {
					proveForm.setAnexo24("1");	
				}else {
					proveForm.setAnexo24("0");
				}
				
				
				if ("".equals(getLimiteTolerancia())){
					setLimiteTolerancia("0");
				}
				proveForm.setLimiteTolerancia(getLimiteTolerancia());
				
				if ("".equals(getLimiteComplemento())){
					setLimiteComplemento("0");
				}
				proveForm.setLimiteComplemento(getLimiteComplemento());
				
				
				proveForm.setNotComUsuario(Utils.validaCheck(getNotComUsuario()));
				proveForm.setNotPagoUsuario(Utils.validaCheck(getNotPagoUsuario()));
				proveForm.setBandDescuento(Utils.validaCheck(getBandDescuento()));
				proveForm.setNumeroCuentaProveedor(getNumeroCuentaProveedor());
				proveForm.setCentroCostos(getCentroCostos());
				
				proveForm.setUsuarioGeneracion(getUsuario(request).toLowerCase());
				
				proveForm.setEmail1(getEmail1());
				proveForm.setEmail2(getEmail2());
				proveForm.setEmail3(getEmail3());
				proveForm.setEmail4(getEmail4());
				proveForm.setEmail5(getEmail5());
				

				proveForm.setTipoEmail1(Utils.validaCheck(getTipoEmail1()));
				proveForm.setTipoEmail2(Utils.validaCheck(getTipoEmail2()));
				proveForm.setTipoEmail3(Utils.validaCheck(getTipoEmail3()));
				proveForm.setTipoEmail4(Utils.validaCheck(getTipoEmail4()));
				proveForm.setTipoEmail5(Utils.validaCheck(getTipoEmail5()));
				proveForm.setTipoEmail6(Utils.validaCheck(getTipoEmail6()));
				proveForm.setTipoEmail7(Utils.validaCheck(getTipoEmail7()));
				proveForm.setTipoEmail8(Utils.validaCheck(getTipoEmail8()));
				proveForm.setTipoEmail9(Utils.validaCheck(getTipoEmail9()));
				proveForm.setTipoEmail10(Utils.validaCheck(getTipoEmail10()));
				
				proveForm.setConServicio(getConServicio());
				proveForm.setFormaPago(getFormaPago());
				proveForm.setNumEstrellas(getNumEstrellas());
				proveForm.setRazonProveedor(getRazonProveedor());
				
				proveForm.setPagoDolares(getPagoDolares());
				proveForm.setPagoPesos(getPagoPesos());
				
				proveForm.setAMERICANOS_SERIE(getAMERICANOS_SERIE());
				proveForm.setAMERICANOS_FOLIO(getAMERICANOS_FOLIO());	
				
				proveForm.setAMERICANOS_ACCESO(Utils.validaCheck(getPERMITIR_ACCESO_GENERADOR()));
				proveForm.setBandSAT(Utils.validaCheck(getBandSAT()));
				proveForm.setBandIMSS(Utils.validaCheck(getBandIMSS()));
				proveForm.setNumRegistro(getNumRegistro());
				// proveForm.setServEsp(getServEsp());
				proveForm.setServEsp("N");
				proveForm.setCartaPorte(Utils.validaCheck(getBandCartaPorte()));
				proveForm.setRegimenFiscal(getRegimenFiscal());
				
				ProveedoresForm proveFormBuscar = proveBean.consultarProveedor(conEmpresa, session.getEsquemaEmpresa(), getClaveRegistro());
				
				if (proveFormBuscar == null) {
					proveFormBuscar = new ProveedoresForm();
				}
				
				Part filePart = null;
				File filesIMSS = null;
				try {
					filePart = request.getPart("filesIMSS");
					filesIMSS = UtilsFile.getFileFromPart(filePart);
				}catch (Exception e) {
					filesIMSS = null;
				}
				
				File filesSAT = null;
				try {
					filePart = request.getPart("filesSAT");
					filesSAT = UtilsFile.getFileFromPart(filePart);
				}catch (Exception e) {
					filesSAT = null;
				}
				
				File filesConfidencialidad = null;
				try {
					filePart = request.getPart("filesConfidencialidad");
					filesConfidencialidad = UtilsFile.getFileFromPart(filePart);
				}catch (Exception e) {
					filesConfidencialidad = null;
				}
				
				if ((  "S".equalsIgnoreCase(proveFormBuscar.getTieneIMSS()) || "S".equalsIgnoreCase(proveFormBuscar.getTieneSAT()) || "S".equalsIgnoreCase(proveFormBuscar.getTieneConfidencial()) ) &&
						filesIMSS != null || filesSAT != null ||  filesConfidencialidad != null) {
					if ("INICIO".equalsIgnoreCase(remplazarCertificacion)) {
							provModel.setCodError("002");
							// provModel.setMensajeError("AVISO, Este proveedor ya cuenta con una certificación en el sistema desea usted reemplazarla?.");
							provModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("VAL6")));
							try {
								conEmpresa.close();
							}catch(Exception e) {
								conEmpresa = null;
							}
							JSONObject json = new JSONObject(provModel);
							out.print(json);
				            out.flush();
				            out.close();
							return null;  
							
				       
					}
					else {
						if ("REMPLAZA".equalsIgnoreCase(remplazarCertificacion)) {
							
							proveForm.setTieneIMSS(proveFormBuscar.getTieneIMSS());
							proveForm.setTieneSAT(proveFormBuscar.getTieneSAT());
							proveForm.setTieneConfidencial(proveFormBuscar.getTieneConfidencial());
							
							if (filesIMSS != null) {
								proveForm.setTieneIMSS("S");
								proveForm.setConfirmarIMSS("N");
								if (!"application/pdf".equalsIgnoreCase(UtilsFile.getContentType(filesIMSS))) {
									provModel.setCodError("001");
									//provModel.setMensajeError("Error el guardar la información del registro, debe especificar un archivo PDF en el certificado del IMSS.");
									provModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("VAL1")));
									try {
										conEmpresa.close();
									}catch(Exception e) {
										conEmpresa = null;
									}
									JSONObject json = new JSONObject(provModel);
									out.print(json);
						            out.flush();
						            out.close();
									return null;  
								}
								
							}
							if (filesSAT != null) {
								proveForm.setTieneSAT("S");
								proveForm.setConfirmarSAT("N");
								
								if (!"application/pdf".equalsIgnoreCase(UtilsFile.getContentType(filesSAT))) {
									provModel.setCodError("001");
									//provModel.setMensajeError("Error el guardar la información del registro, debe especificar un archivo PDF en el certificado del SAT.");
									provModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("VAL2")));
									try {
										conEmpresa.close();
									}catch(Exception e) {
										conEmpresa = null;
									}
									JSONObject json = new JSONObject(provModel);
									out.print(json);
						            out.flush();
						            out.close();
									return null;  
								}
								
							}
							
							if (filesConfidencialidad != null) {
								proveForm.setTieneConfidencial("S");
								if (!"application/pdf".equalsIgnoreCase(UtilsFile.getContentType(filesConfidencialidad))) {
									provModel.setCodError("001");
									//provModel.setMensajeError("Error el guardar la información del registro, debe especificar un archivo PDF en el certificado del confidencialidad.");
									provModel.setMensajeError(Utils.noNuloNormal(mapaLenguaje.get("VAL3")));
									try {
										conEmpresa.close();
									}catch(Exception e) {
										conEmpresa = null;
									}
									JSONObject json = new JSONObject(provModel);
									out.print(json);
						            out.flush();
						            out.close();
									return null;  
								}
								
							}
							
						}else if ("NO_REMPLAZA".equalsIgnoreCase(remplazarCertificacion)) {
							proveForm.setTieneIMSS(proveFormBuscar.getTieneIMSS());
							proveForm.setTieneSAT(proveFormBuscar.getTieneSAT());
							proveForm.setTieneConfidencial(proveFormBuscar.getTieneConfidencial());
						}
					}
				}else {
					if (filesIMSS == null) {
						proveForm.setTieneIMSS(proveFormBuscar.getTieneIMSS());
						proveForm.setConfirmarIMSS(proveFormBuscar.getConfirmarIMSS());
					}else {
						proveForm.setTieneIMSS("S");
						proveForm.setConfirmarIMSS("N");
					}
					
					
					if (filesSAT == null) {
						proveForm.setTieneSAT(proveFormBuscar.getTieneSAT());
						proveForm.setConfirmarSAT(proveFormBuscar.getConfirmarSAT());				
					}else {
						proveForm.setTieneSAT("S");
						proveForm.setConfirmarSAT("N");
					}
					
					if (filesConfidencialidad == null) {
						proveForm.setTieneConfidencial(proveFormBuscar.getTieneConfidencial());
					}else {
						proveForm.setTieneConfidencial("S");
						
					}
					/*
						proveForm.setTieneIMSS(proveFormBuscar.getTieneIMSS());
						proveForm.setConfirmarIMSS(proveFormBuscar.getConfirmarIMSS());
					
						proveForm.setTieneSAT(proveFormBuscar.getTieneSAT());
						proveForm.setConfirmarSAT(proveFormBuscar.getConfirmarSAT());
					
						proveForm.setTieneConfidencial(proveFormBuscar.getTieneConfidencial());
					*/
				}

				
				String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
				String directorio = "";
				directorio = session.getEsquemaEmpresa()+"/PROVEEDORES/" + proveForm.getClaveRegistro() + "/" ;
				  
				String nombreArchivoIMSS = "RFC"+proveFormBuscar.getRfc() +"_" + "Cert_IMSS.pdf";
				String rutaPDFOriginal = rutaFinal + directorio + nombreArchivoIMSS;

				if (filesIMSS != null) {
					File filePDFDest = new File(rutaPDFOriginal);
					UtilsFile.moveFileDirectory(filesIMSS, filePDFDest, true, false, true, false);
					
				}
				
				String nombreArchivoSAT = "RFC"+proveFormBuscar.getRfc() +"_" + "Cert_SAT.pdf";
				String rutaPDFSATOriginal = rutaFinal + directorio + nombreArchivoSAT;

				if (filesSAT != null) {
					File filePDFDestSAT = new File(rutaPDFSATOriginal);
					UtilsFile.moveFileDirectory(filesSAT, filePDFDestSAT, true, false, true, false);					
				}
				
				
				String nombreArchivoConf = "RFC"+proveFormBuscar.getRfc() +"_" + "Conf.pdf";
				String rutaPDFConfOriginal = rutaFinal + directorio + nombreArchivoConf;

				if (filesConfidencialidad != null) {
					File filePDFDestConf = new File(rutaPDFConfOriginal);
					UtilsFile.moveFileDirectory(filesConfidencialidad, filePDFDestConf, true, false, true, false);					
				}
				
				// logger.info("************* INICIANDO ACTUALIZACION ****************");
				proveBean.actualizaProveedores(conEmpresa, session.getEsquemaEmpresa(), proveForm, getUsuario(request));
				proveBean.eliminaProveedoresMail(conEmpresa, session.getEsquemaEmpresa(), proveForm.getClaveRegistro());
				proveBean.altaProveedoresMail(conEmpresa, session.getEsquemaEmpresa(), proveForm);
				// logger.info("************* FIN DE ACTUALIZACION ****************");
				
				
				if ("on".equalsIgnoreCase(getBandInstrucciones()) || "S".equalsIgnoreCase(getBandInstrucciones())) {
					FormatosBean formatoBean = new FormatosBean();
					AccesoBean accesoBean = new AccesoBean();
					EmpresasForm empresasForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
					String directorioFormato = "";
					directorioFormato =  session.getEsquemaEmpresa() + File.separator + "FORMATOS"+ File.separator + proveForm.getTipoProveedor() + File.separator;
					String  rutaFormatos = rutaFinal + directorioFormato;
					formatoBean.enviaFormatoProveedor(conEmpresa, session.getEsquemaEmpresa(), getClaveRegistro(), proveForm.getRazonSocial(), proveForm.getRfc(), proveForm.getTipoProveedor(), rutaFormatos, getUsrAcceso(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
					//logger.info("... nuevoProveedores() 10 ...");
				}
				UsuariosBean usuariosBean = new UsuariosBean();
				UsuariosForm usuariosForm = usuariosBean.informacionUsuarioProveedor(conEmpresa, rc.getEsquema(), "PROV_"+getClaveRegistro());
				//logger.info("usrAcceso===>"+getUsrAcceso());
				
				if (!getUsrAcceso().equalsIgnoreCase(Utils.noNulo(usuariosForm.getIdUsuario())) && usuariosForm.getIdRegistro() > 0) {
					
					usuariosBean.deleteUsuarioProveedor(conEmpresa, rc.getEsquema(), usuariosForm.getIdRegistro());
					
					AccesoBean accesoBean = new AccesoBean();
					EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
					accesoBean.eliminaAcceso(empresaForm.getClaveEmpresa(),  usuariosForm.getIdUsuario());
					
					String codeAcceso = accesoBean.existeSiarexActivo(usuariosForm.getIdUsuario());
					
					if ("".equalsIgnoreCase(codeAcceso)) {
						accesoBean.eliminaAccesoTomcat(usuariosForm.getIdUsuario());	
					}
					usuariosForm = new UsuariosForm();
				}
				
				if (usuariosForm.getIdRegistro() == 0) { // no se ha generado
					if ("S".equalsIgnoreCase(Utils.validaCheck(getPermitirAcceso()))) {
						usuariosBean.altaUsuarios(conEmpresa, session.getEsquemaEmpresa(), getUsrAcceso().toLowerCase(), "PROV_"+getClaveRegistro(), getRazonSocial(), getUsrAcceso(), 4, getUsuario(request));
						
						AccesoBean accesoBean = new AccesoBean();
						EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
						String pwdUsuario = accesoBean.existeSiarexActivo(getUsrAcceso());
						long tiempoAcceso = System.currentTimeMillis();
					  	String codigoAcceso = Utils.encryptarMD5(String.valueOf( tiempoAcceso ));
					  	
						if ("".equalsIgnoreCase(pwdUsuario) ) { // si NO existe, se envia correo
						  	accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getRazonSocial(), getUsrAcceso(), null, getUsrAcceso(), 4, "PROV_"+getClaveRegistro(), "N", codigoAcceso, getUsuario(request));
						  	
						  	String urlAcceso = "https://"+ UtilsPATH.DOMINIO_PRINCIPAL + "/siarexLogin/registroSiarex.jsp?c="+ codigoAcceso;
						    String htmlAcceso = UtilsHTML.generaHTMLAcceso(getRazonSocial(), urlAcceso, UtilsPATH.DOMINIO_PRINCIPAL);
						    String emailTO [] = {getUsrAcceso()};
						    EnviaCorreoGrid.enviarCorreo(null, htmlAcceso, false, emailTO, null, "SIAREX - Registrar Acceso", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());						    
						    
						}else {
							accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getRazonSocial(), getUsrAcceso(), pwdUsuario, getUsrAcceso(), 4, "PROV_"+getClaveRegistro(), "S", codigoAcceso, getUsuario(request));
						}							
					}else {
						/*
						long tiempoAcceso = System.currentTimeMillis();
						String codigoAcceso = Utils.encryptarMD5(String.valueOf( tiempoAcceso ));
						usuariosBean.altaUsuarios(conEmpresa, session.getEsquemaEmpresa(), getUsrAcceso().toLowerCase(), "PROV_"+getClaveRegistro(), getRazonSocial(), getUsrAcceso(), 4, getUsuario(request));
						AccesoBean accesoBean = new AccesoBean();
						EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
						accesoBean.altaAcceso(empresaForm.getClaveEmpresa(), getRazonSocial(), getUsrAcceso(), null, getUsrAcceso(), 4, "PROV_"+getClaveRegistro(), "N", codigoAcceso, getUsuario(request));
						
						usuariosForm = usuariosBean.informacionUsuarioProveedor(conEmpresa, rc.getEsquema(), "PROV_"+getClaveRegistro());
						usuariosBean.deleteUsuarios(conEmpresa, rc.getEsquema(), usuariosForm.getIdRegistro(), "D", getUsuario(request));
						*/
					}
				}else if ("S".equalsIgnoreCase(Utils.validaCheck(getPermitirAcceso())) && "D".equalsIgnoreCase(usuariosForm.getEstatusRegistro())) {
					usuariosBean.deleteUsuarios(conEmpresa, rc.getEsquema(), usuariosForm.getIdRegistro(), "A", getUsuario(request));
				}else if ("N".equalsIgnoreCase(Utils.validaCheck(getPermitirAcceso())) && "A".equalsIgnoreCase(usuariosForm.getEstatusRegistro())) {
					usuariosBean.deleteUsuarios(conEmpresa, rc.getEsquema(), usuariosForm.getIdRegistro(), "D", getUsuario(request));
				}

	  		  }
	  		
	  		provModel.setCodError("000");
	  		provModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
			
	  		JSONObject json = new JSONObject(provModel);
			out.print(json);
            out.flush();
            out.close();
            
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
	
	
	
	public String actualizaEstatus() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		ProveedoresBean provBean = new ProveedoresBean();
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
				PuestosModel puestosModel = new PuestosModel();
				
					rc = getConnection(session.getEsquemaEmpresa());
					con = rc.getCon();
					int totReg = provBean.actualizaEstatus(con, rc.getEsquema(), getClaveRegistro(), getUsuario(request));
					if (totReg == -100) {
						puestosModel.setCodError("001");
						puestosModel.setMensajeError("Error el guardar la información del registro, consulte a su administrador.");
					}else {
						puestosModel.setCodError("000");
						puestosModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
					}	
				
				JSONObject json = new JSONObject(puestosModel);
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
	
	
	
	public String eliminaProveedores() throws Exception {
		Connection conEmpresa = null;
		ResultadoConexion rc = null;
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		
		ProveedoresBean proveBean = new ProveedoresBean();
		ProveedoresModel provModel = new ProveedoresModel();
		try{
			PrintWriter out = response.getWriter();
	    	SiarexSession session = ObtenerSession.getSession(request);
			rc = getConnection(session.getEsquemaEmpresa());
			conEmpresa = rc.getCon();
			
			UsuariosBean usuariosBean = new UsuariosBean();
			UsuariosForm usuariosForm = usuariosBean.informacionUsuarioProveedor(conEmpresa, rc.getEsquema(), "PROV_"+getClaveRegistro());
			
			int totReg = proveBean.eliminaProveedores(conEmpresa, session.getEsquemaEmpresa(), getClaveRegistro(), getUsuario(request));
			if (totReg > 0){
				logger.info("Se ha eliminado el proveedor : "+getRazonSocial());
				proveBean.eliminaProveedoresMail(conEmpresa, session.getEsquemaEmpresa(), getClaveRegistro());
				
				AccesoBean accesoBean = new AccesoBean();
				EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
				accesoBean.eliminaAcceso(empresaForm.getClaveEmpresa(),  usuariosForm.getIdUsuario());
				
				String codeAcceso = accesoBean.existeSiarexActivo(usuariosForm.getIdUsuario());
				
				if ("".equalsIgnoreCase(codeAcceso)) {
					accesoBean.eliminaAccesoTomcat(usuariosForm.getIdUsuario());	
				}
				
				provModel.setCodError("000");
				provModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
			}else {
				provModel.setCodError("001");
				provModel.setMensajeError("Error al eliminar el registro, consulte a su administrador.");
			}
			JSONObject json = new JSONObject(provModel);
			out.print(json);
            out.flush();
            out.close();
            
		}catch(Exception e){
			logger.error(e);
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
	
	
	
	 public String exportarCertificados() {//logger.info("... exportarCertificados() 1 ...");
		  Connection con = null;
		  HttpServletRequest request = ServletActionContext.getRequest();
			ResultadoConexion rc = null;
			ProveedoresBean provBean = new ProveedoresBean();
			ProveedoresForm proveedoresForm = null;
			
			ProveedoresModel provModel = new ProveedoresModel();
			try{
			  
			  SiarexSession session = ObtenerSession.getSession(request);
			  rc = getConnection(session.getEsquemaEmpresa());
			  con = rc.getCon();
	   		 	String listaProveedor = Utils.noNulo(request.getParameter("idProveedores"));
	   		 	String arrListaProv [] = listaProveedor.split(";");
	   		 	ArrayList<ProveedoresForm> datosProveedor = provBean.exportarCertificados(con, session.getEsquemaEmpresa(), arrListaProv);
			    String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
				String directorio = "";
				directorio =  session.getEsquemaEmpresa()+"/PROVEEDORES/"  ;

				String rutaArchivoIMSS = null;
				String rutaArchivoSAT = null;
				ArrayList<String> alFiles = new ArrayList<String>();
				for (int x = 0; x < datosProveedor.size(); x++){
					proveedoresForm = datosProveedor.get(x);
					rutaArchivoIMSS = rutaFinal + directorio + proveedoresForm.getClaveRegistro() + File.separator + "RFC"+proveedoresForm.getRfc() +"_" + "Cert_IMSS.pdf";
					rutaArchivoSAT  = rutaFinal + directorio + proveedoresForm.getClaveRegistro() + File.separator + "RFC"+proveedoresForm.getRfc() +"_" + "Cert_SAT.pdf";
					alFiles.add(rutaArchivoIMSS);
					alFiles.add(rutaArchivoSAT);
					
					// logger.info("rutaArchivoIMSS===>"+rutaArchivoIMSS);
					// logger.info("rutaArchivoSAT===>"+rutaArchivoSAT);
					
					
				}
				if (!alFiles.isEmpty()){
					ZipFiles zipFiles = new ZipFiles();
					ByteArrayOutputStream dest = zipFiles.zipFiles(alFiles);
					setInputStream(new ByteArrayInputStream(dest.toByteArray()));
					provModel.setCodError("000");
					provModel.setMensajeError("Los certificados se han exportado satisfactoriamente.");
				} else{
					provModel.setCodError("001");
					provModel.setMensajeError("Error al realizar la exportacion, no se han encontrado archivos del certificado.");
					addActionMessage("No se encontraron registros!");
					return  ERROR;
					
				}
		   
		  } catch (Exception e) {
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
	 
	
	 public String eliminarCertificados() {
		    
		    HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			
			ResultadoConexion rc = null;
			Connection con = null;
			
			ProveedoresBean provBean = new ProveedoresBean();
			ProveedoresForm proveedoresForm = null;
			
			boolean eliminado = false;
			ProveedoresModel provModel = new ProveedoresModel();
			
			try{
				PrintWriter out = response.getWriter();
				SiarexSession session = ObtenerSession.getSession(request);
				rc = getConnection(session.getEsquemaEmpresa());
				con = rc.getCon();

	   		 	String listaProveedor = Utils.noNulo(request.getParameter("idProveedores"));
	   		 	String arrListaProv [] = listaProveedor.split(";");
	   		 	
	   		 	ArrayList<ProveedoresForm> datosProveedor = provBean.exportarCertificados(con, session.getEsquemaEmpresa(), arrListaProv);
	   		 	int totReg = provBean.eliminarCertificados(con, session.getEsquemaEmpresa(), arrListaProv);
			    String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
				String directorio = "";
				directorio = session.getEsquemaEmpresa()+"/PROVEEDORES/"  ;
				String rutaArchivoIMSS = null;
				String rutaArchivoSAT = null;

				for (int x = 0; x < datosProveedor.size(); x++){
					proveedoresForm = datosProveedor.get(x);
					rutaArchivoIMSS = rutaFinal + directorio + proveedoresForm.getClaveRegistro() + File.separator + "RFC"+proveedoresForm.getRfc() +"_" + "Cert_IMSS.pdf";
					rutaArchivoSAT  = rutaFinal + directorio + proveedoresForm.getClaveRegistro() + File.separator + "RFC"+proveedoresForm.getRfc() +"_" + "Cert_SAT.pdf";
					File fileIMSS = new File(rutaArchivoIMSS);
					File fileSAT = new File(rutaArchivoSAT);
					if (fileIMSS.exists()) {
						fileIMSS.delete();
					}
					if (fileSAT.exists()) {
						fileSAT.delete();
						eliminado = true;
					}
				}

				if (eliminado) {
					provModel.setCodError("000");
					provModel.setMensajeError("El registro se ha guardado satisfactoriamente."); 
				}else {
					provModel.setCodError("001");
					provModel.setMensajeError("Error al eliminar certificados, consulte a su administrador.");
				}
				
				JSONObject json = new JSONObject(provModel);
				out.print(json);
	            out.flush();
	            out.close();
	            
		  } catch (Exception e) {
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


	 
	 public String mostrarCertificado(HttpServletRequest request, String nombreArchivo, String nombreRepositorio, int claveProveedor) throws Exception {
			String pathArchivo = "";
			try{
				
				 String fileFactura = UtilsPATH.REPOSITORIO_DOCUMENTOS + nombreRepositorio + File.separator +  "PROVEEDORES" + File.separator + claveProveedor + File.separator + nombreArchivo;
				InputStream imagenEmpleado = new FileInputStream(new File(fileFactura));
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
				
			}catch(Exception e){
				Utils.imprimeLog("", e);
				pathArchivo = "/siarex/files/sinOrden.html";
			}
			return pathArchivo;
		}
	 
	 
	 
		public String buscarCertificados(){ 
			ResultadoConexion rc = null;
			Connection con = null;
			ProveedoresBean provBean = new ProveedoresBean();
			HttpServletResponse response = ServletActionContext.getResponse();
			HttpServletRequest request = ServletActionContext.getRequest();	

			try {
				response.setContentType("text/html; charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				
			    SiarexSession session = ObtenerSession.getSession(request);
			    rc = getConnection(session.getEsquemaEmpresa());
			    con = rc.getCon();
			    PrintWriter out = response.getWriter();
			    
			    String tipoCertificado = Utils.noNulo(request.getParameter("tipoCertificado"));
			    ProveedoresForm provForm = provBean.infoProveedoresCertificados(con, session.getEsquemaEmpresa(), getClaveRegistro(), tipoCertificado);

				if ("true".equalsIgnoreCase(provForm.getEstatusRegistro())) {
					mostrarCertificado(request, provForm.getNombreArchivo().toString(), session.getEsquemaEmpresa(), getClaveRegistro());
				}
			    
				JSONObject json = new JSONObject(provForm);
				out.print(json);
	            out.flush();
	            out.close();
	            
			    
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}finally {
				try {
					if (con != null) {
						con.close();
					}
					con = null;
				}catch(Exception e) {
					con = null;
				}
			}
			return SUCCESS;
	    }

		
		
		public String actualizaCertificadoCompras() {
			Connection con = null;
			Connection conEmpresa = null;
			ResultadoConexion rc = null;
			ProveedoresBean proveBean = new ProveedoresBean();
			HttpServletResponse response = ServletActionContext.getResponse();
			ProveedoresForm proveForm = new ProveedoresForm();
			try{
				HttpServletRequest request = ServletActionContext.getRequest();	
				PrintWriter out = response.getWriter();
				
				SiarexSession session = ObtenerSession.getSession(request);
		  		  if ("".equals(session.getEsquemaEmpresa())){
		  				return Action.LOGIN;
		  		  }else{
		  			rc = getConnection(session.getEsquemaEmpresa());
		  			conEmpresa = rc.getCon();
				    
		  			  proveForm.setClaveRegistro(getClaveRegistro());
		  			  String tipoCertificado = Utils.noNulo(request.getParameter("tipoCertificado"));
		  			  String bandCertificado = Utils.noNulo(request.getParameter("bandCertificado"));

		  			  
		  			  logger.info("claveRegistro====>"+getClaveRegistro());
		  			logger.info("tipoCertificado====>"+tipoCertificado);
		  			logger.info("bandCertificado====>"+bandCertificado);
		  			  
		  			  
		  			  boolean eliminarArchivoIMSS = false;
		  			  boolean eliminarArchivoSAT = false;
		  			  if ("IMSS".equalsIgnoreCase(tipoCertificado) && "CORRECTO".equalsIgnoreCase(bandCertificado)) {
		  				proveForm.setConfirmarIMSS("S");
		  				proveForm.setTieneIMSS("S");
		  			  }else if ("IMSS".equalsIgnoreCase(tipoCertificado) && "INCORRECTO".equalsIgnoreCase(bandCertificado)) {
		  				proveForm.setConfirmarIMSS("N");
		  				proveForm.setTieneIMSS("N");
		  				eliminarArchivoIMSS = true;
		  			  }else if ("SAT".equalsIgnoreCase(tipoCertificado) && "CORRECTO".equalsIgnoreCase(bandCertificado)) {
		  				proveForm.setConfirmarSAT("S");
		  				proveForm.setTieneSAT("S");
		  			  }else if ("SAT".equalsIgnoreCase(tipoCertificado) && "INCORRECTO".equalsIgnoreCase(bandCertificado)) {
		  				proveForm.setConfirmarSAT("N");
		  				proveForm.setTieneSAT("N");
		  				eliminarArchivoSAT = true;
		  			  }
		  			  
		  			  
	  			  	  int resultado = proveBean.confirmarCertificadoCompras(conEmpresa, session.getEsquemaEmpresa(), proveForm, tipoCertificado);
	  			  	  if (resultado == 1) { // elimina el archivo
	  			  		
		  			  	String rutaFinal = UtilsPATH.REPOSITORIO_DOCUMENTOS;
						String directorio = "";
						directorio = session.getEsquemaEmpresa()+"/PROVEEDORES/" + proveForm.getClaveRegistro() + "/" ;
						
						String nombreArchivo = null;
						if (eliminarArchivoIMSS) {
							nombreArchivo = "RFC"+getRfc() +"_" + "Cert_IMSS.pdf";	
						}else if (eliminarArchivoSAT) {
							nombreArchivo = "RFC"+getRfc() +"_" + "Cert_SAT.pdf";	
						}
						
						String rutaPDF = rutaFinal + directorio + nombreArchivo;
						File filePDF = new File(rutaPDF);
						if (filePDF.exists()) {
							logger.info("se ha eliminado el archivo................");
							filePDF.delete();
						}
						
	  			  	  }
		  			
	  			  	ProveedoresModel   provModel = new ProveedoresModel();
	  			  	provModel.setCodError("000");
	  		  		provModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
		  		  	JSONObject json = new JSONObject(provModel);
					out.print(json);
		            out.flush();
		            out.close();
						
		  		  }
			}catch(Exception e) {
				Utils.imprimeLog("", e);
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
				}
			}
			return SUCCESS;
		  			  
		}
	 

		
		
		
		public String envioAcceso() throws Exception {
			Connection conEmpresa = null;
			ResultadoConexion rc = null;
			ProveedoresBean proveBean = new ProveedoresBean();
			
			try{
				HttpServletRequest request = ServletActionContext.getRequest();
				HttpServletResponse response = ServletActionContext.getResponse();
				PrintWriter out = response.getWriter();
				
				 SiarexSession session = ObtenerSession.getSession(request);
				 
				 ProveedoresModel   provModel = new ProveedoresModel();
	  			
				 Part filePart = request.getPart("filesPDF");
	          	  File filesPDF = UtilsFile.getFileFromPart(filePart);	
				 
				 if (!UtilsFile.getContentType(filesPDF).equalsIgnoreCase("application/pdf")){
					provModel.setCodError("001");
					provModel.setMensajeError("Error al Enviar Acceso, Debe especificar un arhico .PDF");
					JSONObject jsonError = new JSONObject(provModel);
					out.print(jsonError);
		            out.flush();
		            out.close();
				     return null;  
			  	 }
				 
				 rc = getConnection(session.getEsquemaEmpresa());
				 conEmpresa = rc.getCon();
				 
				 // JSONObject jsonobj = proveBean.infoProveedores(conEmpresa, session.getEsquemaEmpresa(), getClaveRegistro());
				 // ProveedoresForm provForm = proveBean.consultarProveedor(conEmpresa, session.getEsquemaEmpresa(), getClaveRegistro());
				 
				 UsuariosBean usuariosBean = new UsuariosBean();
				 UsuariosForm usuariosForm = usuariosBean.informacionUsuarioProveedor(conEmpresa, rc.getEsquema(), "PROV_"+getClaveRegistro());
				 
				 if (usuariosForm.getIdRegistro() == 0 || "D".equalsIgnoreCase(usuariosForm.getEstatusRegistro())) {
					provModel.setCodError("001");
					provModel.setMensajeError("Error al Enviar Acceso, el proveedor no cuenta con permiso de acceso a SIAREX.");
			  		 
				 }else {
					 AccesoBean accesoBean = new AccesoBean();
					 EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
					 
					 String urlAcceso = "https://"+ UtilsPATH.DOMINIO_PRINCIPAL;
					 String sbHTML = UtilsHTML.generaHTMLAccesoEnvioProveedor(usuariosForm.getNombreCompleto(), urlAcceso, UtilsPATH.DOMINIO_PRINCIPAL);
					 String repositorioArchivos =  UtilsPATH.RUTA_CORREO + File.separator + session.getEsquemaEmpresa() + File.separator;
					 File filePDF = new File(filesPDF.getAbsolutePath());
					 File fdesPDF = new File(repositorioArchivos + filesPDF.getName());
					 UtilsFile.moveFileDirectory(filePDF, fdesPDF, true, false, true);
					 String listaCorreosProveedores [] = {usuariosForm.getCorreo()};
					 EnviaCorreoGrid.enviarCorreo(fdesPDF.getAbsolutePath(), sbHTML, true, listaCorreosProveedores, null, "SIAREX - Envió información de acceso", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
					 
					 provModel.setCodError("000");
			  		 provModel.setMensajeError("El registro se ha guardado satisfactoriamente.");
			  		  	
				 }
				 
				JSONObject json = new JSONObject(provModel);
				out.print(json);
		        out.flush();
		        out.close();
		            
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
		

		
		
		public String comboProveedores(){
	        Connection con = null;
	        ResultadoConexion rc = null;
	        HttpServletResponse response = ServletActionContext.getResponse();
	    	HttpServletRequest request = ServletActionContext.getRequest();
	    	ProveedoresBean provBean = new ProveedoresBean();
	        try{
	        	response.setCharacterEncoding("UTF-8");
	        	SiarexSession session = ObtenerSession.getSession(request);
	        	PrintWriter out = response.getWriter();
	            rc = getConnection(session.getEsquemaEmpresa());
	            con = rc.getCon();

	            ProveedoresModel provModel = new ProveedoresModel();
	            ArrayList<ProveedoresForm> listaCombo = provBean.comboProveedores(con, rc.getEsquema(), getBandTareas(), getTipoProveedor(), session.getLenguaje());  
	            provModel.setData(listaCombo);
	            
				JSONObject json = new JSONObject(provModel);
				out.print(json);
	            out.flush();
	            out.close();
	            
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("comboProveedoresExt(): ", e);
	            logger.error(e);
	        }finally {
		        try{
		            if(con != null) {
		                con.close();
		            }
		            con = null;
		        }
		        catch(Exception e){
		            con = null;
		        }
			}
	        return null;
	    }
		
		
		
		public String comboProveedoresExt(){
	        Connection con = null;
	        ResultadoConexion rc = null;
	        HttpServletResponse response = ServletActionContext.getResponse();
	    	HttpServletRequest request = ServletActionContext.getRequest();
	    	ProveedoresBean provBean = new ProveedoresBean();
	        try{
	        	response.setCharacterEncoding("UTF-8");
	        	SiarexSession session = ObtenerSession.getSession(request);
	        	PrintWriter out = response.getWriter();
	            rc = getConnection(session.getEsquemaEmpresa());
	            con = rc.getCon();

	            ProveedoresModel provModel = new ProveedoresModel();
	            ArrayList<ProveedoresForm> listaCombo = provBean.comboProveedoresExt(con, rc.getEsquema());  
	            provModel.setData(listaCombo);
	            
				JSONObject json = new JSONObject(provModel);
				out.print(json);
	            out.flush();
	            out.close();
	            
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("comboProveedoresExt(): ", e);
	            logger.error(e);
	        }
	        
	        try{
	            if(con != null) {
	                con.close();
	            }
	            con = null;
	        }
	        catch(Exception e){
	            con = null;
	        }
	        return null;
	    }
	 
	 
	 public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	 
	 
	 
	 
}
