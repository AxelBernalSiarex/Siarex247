package com.siarex247.visor.VisorOrdenes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.apache.struts2.action.Action;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Usuarios.UsuariosBean;
import com.siarex247.seguridad.Usuarios.UsuariosForm;
import com.siarex247.session.ObtenerSession;
import com.siarex247.session.SiarexSession;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.validaciones.CartasPorteForm;
import com.siarex247.validaciones.ValidacionesCartaPorte;
import com.siarex247.validaciones.ValidacionesComplemento;
import com.siarex247.validaciones.ValidacionesNotaCredito;

public class VisorDocumentos {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	
	public String mostrarDocumentos(HttpServletRequest request) throws Exception {
	    String pathArchivo = "";
	    ResultadoConexion rc = null;
	    ConexionDB connPool = new ConexionDB();
	    Connection con = null;

	    try {
	        SiarexSession session = ObtenerSession.getSession(request);
	        if ("".equals(session.getEsquemaEmpresa())) {
	            return Action.LOGIN;
	        }

	        rc = connPool.getConnection(session.getEsquemaEmpresa());
	        con = rc.getCon();

	        String tipoDocumento = Utils.noNulo(request.getParameter("tipoDocumento"));
	        long folioOrden = Utils.noNuloLong(request.getParameter("folioOrden"));

	        UsuariosBean usuarioBean = new UsuariosBean();
	        UsuariosForm usuariosForm = usuarioBean.datosUsuario(con, rc.getEsquema(), request.getRemoteUser());

	        int claveProveedor = 0;
	        if (usuariosForm.getIdPerfil() == 4) {
	            claveProveedor = Integer.parseInt(usuariosForm.getIdEmpleado().substring(5));
	        }

	        VisorOrdenesBean visorBean = new VisorOrdenesBean();
	        VisorOrdenesForm visorForm = visorBean.consultarOrden(con, rc.getEsquema(), folioOrden);

	        boolean permitirDescarga = true;
	        if (usuariosForm.getIdPerfil() == 4 && claveProveedor > 0) {
	            permitirDescarga = (claveProveedor == visorForm.getClaveProveedor());
	        }

	        if (!permitirDescarga) {
	            return "/siarex247/html/sinOrden.html";
	        }

	        File fileArchivo = null;
	        String nombreArchivo = null;

	        if ("XML_FACTURA".equalsIgnoreCase(tipoDocumento)) {

	            String rutaXML = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa()
	                    + File.separator + "PROVEEDORES" + File.separator + visorForm.getClaveProveedor()
	                    + File.separator + visorForm.getNombreXML();
	            fileArchivo = new File(rutaXML);
	            nombreArchivo = Utils.dobleEncryptarMD5(visorForm.getNombreXML()) + ".xml";

	        } else if ("PDF_FACTURA".equalsIgnoreCase(tipoDocumento)) {

	            String rutaPDF = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa()
	                    + File.separator + "PROVEEDORES" + File.separator + visorForm.getClaveProveedor()
	                    + File.separator + visorForm.getNombrePDF();
	            fileArchivo = new File(rutaPDF);
	            nombreArchivo = visorForm.getNombrePDF();

	        } else if ("XML_COMPLEMENTO".equalsIgnoreCase(tipoDocumento)) {

	            ValidacionesComplemento visorComple = new ValidacionesComplemento();
	            ComplementosForm compleForm = visorComple.consultaComplemento(con, rc.getEsquema(), visorForm.getFolioEmpresa());
	            String rutaXML = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa()
	                    + File.separator + "PROVEEDORES" + File.separator + compleForm.getClaveProveedor()
	                    + File.separator + compleForm.getNombreXML();
	            fileArchivo = new File(rutaXML);
	            nombreArchivo = Utils.dobleEncryptarMD5(compleForm.getNombreXML()) + ".xml";

	        } else if ("PDF_COMPLEMENTO".equalsIgnoreCase(tipoDocumento)) {

	            ValidacionesComplemento visorComple = new ValidacionesComplemento();
	            ComplementosForm compleForm = visorComple.consultaComplemento(con, rc.getEsquema(), visorForm.getFolioEmpresa());
	            String rutaPDF = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa()
	                    + File.separator + "PROVEEDORES" + File.separator + compleForm.getClaveProveedor()
	                    + File.separator + compleForm.getNombrePDF();
	            fileArchivo = new File(rutaPDF);
	            nombreArchivo = compleForm.getNombrePDF();

	        } else if ("XML_NOTA".equalsIgnoreCase(tipoDocumento)) {

	            ValidacionesNotaCredito visorNota = new ValidacionesNotaCredito();
	            NotaCreditoForm notaForm = visorNota.buscarNotaCredito(con, rc.getEsquema(), folioOrden);
	            String rutaXML = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa()
	                    + File.separator + "PROVEEDORES" + File.separator + notaForm.getClaveProveedor()
	                    + File.separator + notaForm.getNombreXML();
	            fileArchivo = new File(rutaXML);
	            nombreArchivo = Utils.dobleEncryptarMD5(notaForm.getNombreXML()) + ".xml";

	        } else if ("PDF_NOTA".equalsIgnoreCase(tipoDocumento)) {

	            ValidacionesNotaCredito visorNota = new ValidacionesNotaCredito();
	            NotaCreditoForm notaForm = visorNota.buscarNotaCredito(con, rc.getEsquema(), folioOrden);
	            String rutaPDF = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa()
	                    + File.separator + "PROVEEDORES" + File.separator + notaForm.getClaveProveedor()
	                    + File.separator + notaForm.getNombrePDF();
	            fileArchivo = new File(rutaPDF);
	            nombreArchivo = notaForm.getNombrePDF();

	        } else if ("PDF_ORDEN".equalsIgnoreCase(tipoDocumento)) {

	            // ✅ 1) INTENTAR PRIMERO EL HTM EN CORREO_FACTURAS (tu ruta real)
	            // folioEmpresa es el "4514143696" que coincide con el nombre del archivo .htm
	        	String folioEmpresa = String.valueOf(visorForm.getFolioEmpresa());
	        	folioEmpresa = (folioEmpresa == null ? "" : folioEmpresa).trim();
	        	if ("null".equalsIgnoreCase(folioEmpresa)) {
	        	    folioEmpresa = "";
	        	}

	            String nombreHtm = safeFileName(folioEmpresa + ".htm");

	            File htm = null;
	            if (!"".equals(nombreHtm)) {
	                String rutaHtm = "C:\\Program Files\\Apache Software Foundation\\Tomcat 11.0\\siarex247\\mario\\CORREO_FACTURAS"
	                        + File.separator + nombreHtm;
	                htm = new File(rutaHtm);
	            }

	            if (htm != null && htm.exists() && htm.isFile()) {
	                fileArchivo = htm;
	                nombreArchivo = nombreHtm; // "4514143696.htm"
	            } else {
	                // ✅ 2) FALLBACK: PDF como hoy
	                String pdfName = safeFileName(visorForm.getNombreArchivo());
	                String rutaPDF = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa()
	                        + File.separator + "TAREAS_SIAREX" + File.separator + pdfName;

	                fileArchivo = new File(rutaPDF);
	                nombreArchivo = pdfName;
	            }

	        } else if ("XML_CARTAPORTE".equalsIgnoreCase(tipoDocumento)) {

	            ValidacionesCartaPorte valCartaBean = new ValidacionesCartaPorte();
	            CartasPorteForm cartaForm = valCartaBean.consultaCartas(con, session.getEsquemaEmpresa(), visorForm.getFolioEmpresa());

	            nombreArchivo = Utils.dobleEncryptarMD5(visorForm.getNombreXML()) + ".xml";
	            if ("T".equalsIgnoreCase(cartaForm.getTipoComprobante())) {
	                nombreArchivo = Utils.dobleEncryptarMD5(cartaForm.getNombreXML()) + ".xml";
	            }

	            String rutaXML = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa()
	                    + File.separator + "PROVEEDORES" + File.separator + visorForm.getClaveProveedor()
	                    + File.separator + nombreArchivo;

	            fileArchivo = new File(rutaXML);

	        } else if ("PDF_CARTAPORTE".equalsIgnoreCase(tipoDocumento)) {

	            ValidacionesCartaPorte valCartaBean = new ValidacionesCartaPorte();
	            CartasPorteForm cartaForm = valCartaBean.consultaCartas(con, session.getEsquemaEmpresa(), visorForm.getFolioEmpresa());

	            nombreArchivo = visorForm.getNombrePDF();
	            if ("T".equalsIgnoreCase(cartaForm.getTipoComprobante())) {
	                nombreArchivo = cartaForm.getNombreXML();
	            }

	            nombreArchivo = safeFileName(nombreArchivo);

	            String rutaPDF = UtilsPATH.REPOSITORIO_DOCUMENTOS + session.getEsquemaEmpresa()
	                    + File.separator + "PROVEEDORES" + File.separator + visorForm.getClaveProveedor()
	                    + File.separator + nombreArchivo;

	            fileArchivo = new File(rutaPDF);
	        }

	        if (fileArchivo == null || nombreArchivo == null || "".equals(nombreArchivo)) {
	            return "/siarex247/html/sinOrden.html";
	        }

	        if (!fileArchivo.exists() || !fileArchivo.isFile()) {
	            // No existe ni HTM ni PDF (o ruta mal)
	            return "/siarex247/html/sinOrden.html";
	        }

	        // Copiar al /files/ del webapp y devolver URL pública
	        copyToWebFiles(request, fileArchivo, nombreArchivo);
	        pathArchivo = "/siarex247/files/" + nombreArchivo;

	    } catch (Exception e) {
	        Utils.imprimeLog("VisorDocumentos.mostrarDocumentos error", e);
	        pathArchivo = "/siarex247/html/sinOrden.html";
	    } finally {
	        try { if (con != null) con.close(); } catch (Exception ignore) {}
	    }

	    return pathArchivo;
	}

	
	private static String safeFileName(String name) {
	    String n = Utils.noNulo(name).trim();
	    // Permite letras, números, guion, guion bajo y punto. Evita path traversal.
	    if (!n.matches("[A-Za-z0-9._-]+")) {
	        return "";
	    }
	    return n;
	}

	private static void copyToWebFiles(HttpServletRequest request, File source, String nombreArchivo) throws Exception {
	    String filePath = request.getSession().getServletContext().getRealPath("/");
	    File dir = new File(filePath, "files");
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }

	    File target = new File(dir, nombreArchivo);

	    try (InputStream in0 = new FileInputStream(source);
	         BufferedInputStream in = new BufferedInputStream(in0);
	         BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target))) {

	        byte[] data = new byte[8192];
	        int len;
	        while ((len = in.read(data)) > 0) {
	            out.write(data, 0, len);
	        }
	        out.flush();
	    }
	}

	
	
}
