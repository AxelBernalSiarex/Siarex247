package com.siarex247.cumplimientoFiscal.HistorialPagos;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.Action;
import org.json.JSONObject;

import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Bitacora.BitacoraBean;
import com.siarex247.seguridad.Bitacora.BitacoraForm;
import com.siarex247.seguridad.Bitacora.BitacoraModel;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile; // Necesario para convertir Part a File

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part; // <--- Importante: Usamos Parts nativos

public class HistorialPagosAction extends HistorialPagosSupport {

    private static final long serialVersionUID = 1L;

    public String detalleHistorial() throws Exception {

        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HistorialPagosModel model = new HistorialPagosModel();

        Connection con = null;
        ResultadoConexion rc = null;

        try {
            SiarexSession session = ObtenerSession.getSession(request);

            rc = getConnection(session.getEsquemaEmpresa());
            con = rc.getCon();
            
            // ===== Usuario / permisos =====
	        UsuariosBean uBean = new UsuariosBean();
	        UsuariosForm uForm = uBean.datosUsuario(con, rc.getEsquema(), getUsuario(request));
	        int  claveProveedor = 0;
	        
	        
	        
	        String rfcProveedor = "";

	        if (uForm.getIdPerfil() == 4) { // Proveedor
	            claveProveedor = Integer.parseInt(uForm.getIdEmpleado().substring(5));
	           ProveedoresBean provBean = new ProveedoresBean();
	           ProveedoresForm provForm = provBean.consultarProveedor(con, rc.getEsquema() , claveProveedor);
	           rfcProveedor = provForm.getRfc();
	           
	        } 
	        
	        
	        
	        

                       
            
            HistorialPagosBean bean = new HistorialPagosBean();
            model.setData(bean.listarHistorialPagos(con, session.getEsquemaEmpresa(), rfcProveedor ));
            model.setCodError("000");

        } catch (Exception e) {
            model.setCodError("999");
            model.setMensajeError("Error al obtener historial");
            Utils.imprimeLog("detalleHistorial(): ", e);
        } finally {
            if (con != null) con.close();
        }

        out.print(new org.json.JSONObject(model));
        out.flush();
        out.close();

        return SUCCESS;
    }


    public String cargarPagos() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        // Configuración estándar JSON
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = null;
        Connection con = null;
        ResultadoConexion rc = null;
        HistorialPagosBean bean = new HistorialPagosBean();
        
        try {
            out = response.getWriter();
            SiarexSession session = ObtenerSession.getSession(request);
            if (session == null) throw new Exception("Su sesión ha expirado.");

            rc = getConnection(session.getEsquemaEmpresa());
            con = rc.getCon();

            // 1. Buscar archivo (Estilo nativo Servlet)
            File fileTXT = null;
            	
        	Part filePart = request.getPart("fileCargaHistorial");
            fileTXT = UtilsFile.getFileFromPart(filePart);
            
            
            // logger.info("Contenido..."+UtilsFile.getContentType(fileTXT));
            HistorialPagosModel model = new HistorialPagosModel();
            if ("text/plain".equalsIgnoreCase(UtilsFile.getContentType(fileTXT))) {
            	// 2. LLAMAR AL BEAN
                
                
                HashMap<String, String> mapaResultado = new HashMap<>();
             //   mapaResultado = bean.procesarArchivoTXT(con, rc.getEsquema(), fileTXT.getAbsolutePath(), fileTXT.getName(), getUsuario(request));
             // === NUEVO: Obtener RFC real de la empresa ===
                AccesoBean accesoBean = new AccesoBean();
                EmpresasForm empresaForm = accesoBean.consultaEmpresaEsquema(session.getEsquemaEmpresa());
                String rfcEmpresa = empresaForm.getRfc();

                // === LLAMADA CON RFC EMPRESA (nuevo parámetro) ===
                mapaResultado = bean.procesarArchivoTXT(
                        con,
                        rc.getEsquema(),
                        fileTXT.getAbsolutePath(),
                        fileTXT.getName(),
                        getUsuario(request),
                        rfcEmpresa   // <<< NUEVO
                );

                //logger.info("ERROR_COLUMNAS===>"+mapaResultado.get("ERROR_COLUMNAS"));
                if ("false".equalsIgnoreCase(mapaResultado.get("ERROR_COLUMNAS"))) {
    				int idBitacora = Integer.parseInt( mapaResultado.get("ID_TAREA") );
    				int regOK = Integer.parseInt( mapaResultado.get("OK") );
    				int regNG = Integer.parseInt( mapaResultado.get("NG") );
    				model.setCodError("000");
    				// csModel.setMensajeError("El proceso de importar empleados fue satisfactorio con "+regOK + " registro(s) exitoso(s) y "+regNG+" registro(s) con error.");
    				logger.info("regOK====>"+regOK);
    				logger.info("regNG====>"+regNG);
    				
    				//model.setMensajeError(Utils.noNuloNormal("El proceso de importar empleados fue satisfactorio con "+regOK + " registro(s) exitoso(s) y "+regNG+" registro(s) con error.").replaceAll("<<totOK>>", String.valueOf(regOK)).replaceAll("<<totError>>", String.valueOf(regNG)));
    				model.setMensajeError(Utils.noNuloNormal("El proceso de importar facturas con fechas de pago fue satisfactorio con "+regOK + " registro(s) exitoso(s) y "+regNG+" registro(s) con error."));
    				model.setIdBitacora(idBitacora);
    				model.setNoExitosos(regNG);
    			}else {
    				model.setCodError("001");
    				model.setMensajeError(Utils.noNuloNormal("Error al procesar el archivo, el archivo TXT no contiene las columnas requeridas"));
    			}
            }else {
            	model.setCodError("001");
				model.setMensajeError(Utils.noNuloNormal("Error al procesar el archivo, favor de adjuntar un archivo TXT para procesar."));
            }
            
            JSONObject json = new JSONObject(model);
			out.print(json);
            out.flush();
            out.close();
            
        } catch (Throwable e) { 
            Utils.imprimeLog("Error Action Carga", (Exception) e);
        } finally {
            try { 
            	if (con != null) 
            		con.close(); 
            	con = null;
            } catch (Exception ex) {
            	con = null;
            }
        }
        return SUCCESS;
    }
    
    

	public String detalleErrores() throws Exception {
		Connection con = null;
		ResultadoConexion rc = null;
		BitacoraBean bitacoraBean = new BitacoraBean();
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
				BitacoraModel bitacoraModel = new BitacoraModel();
				ArrayList<BitacoraForm> listaBitacora = bitacoraBean.detalleBitacora(con, rc.getEsquema(), getIdBitacora()); 
				bitacoraModel.setData(listaBitacora);
				bitacoraModel.setRecordsFiltered(20);
				bitacoraModel.setDraw(-1);
				bitacoraModel.setRecordsTotal(listaBitacora.size());
				
				JSONObject json = new JSONObject(bitacoraModel);
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
		return null;
	}
	
	public String consultarUltimaFecha() {
	    HttpServletRequest request = ServletActionContext.getRequest();
	    HttpServletResponse response = ServletActionContext.getResponse();

	    response.setContentType("application/json; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");

	    PrintWriter out = null;
	    Connection con = null;
	    ResultadoConexion rc = null;

	    JSONObject json = new JSONObject();
	    HistorialPagosBean bean = new HistorialPagosBean();

	    try {
	        out = response.getWriter();
	        SiarexSession session = ObtenerSession.getSession(request);
	        if (session == null) throw new Exception("Sesión expirada.");

	        rc = getConnection(session.getEsquemaEmpresa());
	        con = rc.getCon();

	        String fecha = bean.obtenerFechaUltimaActualizacion(con, rc.getEsquema());
	        json.put("fechaDescarga", Utils.noNulo(fecha));

	    } catch (Exception e) {
	        Utils.imprimeLog("Error consultarUltimaFecha", e);
	        json.put("fechaDescarga", "");
	    } finally {
	        try { if (out != null) out.print(json.toString()); } catch(Exception ex){}
	        try { if (con != null) con.close(); } catch(Exception ex){}
	    }

	    return NONE;
	}

	
}