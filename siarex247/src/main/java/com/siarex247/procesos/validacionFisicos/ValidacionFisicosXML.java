package com.siarex247.procesos.validacionFisicos;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesBean;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesForm;

public class ValidacionFisicosXML {

	public static final Logger logger = Logger.getLogger("siarex");
	private PreparedStatement STMT_INSERT = null;

	public void monitoreaCorreo(){
		try{
				AccesoBean accesoBean = new AccesoBean();
				ConexionDB connPool = new ConexionDB();
				Connection con = null;
				ResultadoConexion rc = null;
				try{
					rc = connPool.getConnectionSiarex();
					con = rc.getCon();
					ArrayList<EmpresasForm> listaEmpresas = accesoBean.listaEmpresas(con, rc.getEsquema()); //  detalleEmpresas(con, "siarex");
					EmpresasForm empresasForm = null;
					
					for (int y = 0; y < listaEmpresas.size(); y++){
						empresasForm = listaEmpresas.get(y);
						// logger.info("Buscando Correos de la empresa : "+empresasForm.getEmailDominio());
						if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
							try {
								logger.info("Validacion XML de la empresa====>"+empresasForm.getEsquema());
								iniciaProcesoDescarga(empresasForm);
							} catch (Exception e) {
								Utils.imprimeLog("", e);
							}
						}
					}
				}catch(Exception e){
					Utils.imprimeLog("monitoreaCorreo ", e);
				}finally{
					try{
						if (con != null){
							con.close();
						}
					}catch(Exception e){
						con = null;
					}
				}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
	

 public void iniciaProcesoDescarga(EmpresasForm empresasForm) {
	 ConexionDB connPool = null;
     ResultadoConexion rcEmpresa = null;
     Connection conEmpresa = null;
     VisorOrdenesBean visorBean = new VisorOrdenesBean();
     
     try{
    	 connPool= new ConexionDB();
    	 rcEmpresa = connPool.getConnection(empresasForm.getEsquema());
    	 conEmpresa = rcEmpresa.getCon();
    	 
    	 String queryInsert = "insert into VALIDACION_ORDENES_FISICOS (FOLIO_EMPRESA, EXISTE_FACTURA_XML, EXISTE_FACTURA_PDF, EXISTE_COMPLEMENTO_XML, EXISTE_COMPLEMENTO_PDF, EXISTE_NOTA_XML, EXISTE_NOTA_PDF, UUID_FACTURA, UUID_COMPLEMENTO, UUID_NOTA) values (?,?,?,?,?,?,?,?,?,?)";
    	 STMT_INSERT = conEmpresa.prepareStatement(queryInsert);
    	 
    	 String arrOrdenes [] = {"A1", "A3", "A4"};
    	 
    	 String rutaRepositorio = UtilsPATH.REPOSITORIO_DOCUMENTOS + empresasForm.getEsquema() + File.separator + "PROVEEDORES" + File.separator; // + visorForm.getClaveProveedor() + File.separator + visorForm.getNombreXML();
    	 String rutaFacturaXML = null;
    	 String rutaFacturaPDF = null;
    	 String rutaCompleXML = null;
    	 String rutaComplePDF = null;
    	 String rutaNotaXML = null;
    	 String rutaNotaPDF = null;
    	 
    	 String existeFacturaXML = null;
    	 String existeFacturaPDF = null;
    	 String existeCompleXML = null;
    	 String existeComplePDF = null;
    	 String existeNotaXML = null;
    	 String existeNotaPDF = null;
    	 
    	 VisorOrdenesForm visorForm = null;
    	 for (int a = 0; a < arrOrdenes.length; a++) {
    		// ArrayList<VisorOrdenesForm> listaOrdenes =  visorBean.detalleOrdenes(conEmpresa, rcEmpresa.getEsquema(), "", arrOrdenes[a], 0, "2000-01-01", "2050-01-01", false);
    		 
    		 ArrayList<VisorOrdenesForm> listaOrdenes  = visorBean.detalleOrdenes(conEmpresa, rcEmpresa.getEsquema(), "MX", "", arrOrdenes[a], 0, 
					 	0, "", "", "", "", "2000-01-01", "2050-01-01", false, false, 0, 0, true);
    		 
    		 
    		 for (int x = 0; x < listaOrdenes.size(); x++) {
    			 try {
    				 visorForm = listaOrdenes.get(x);
    				 // logger.info("archivo..."+visorForm.getTieneXML());
    				 rutaFacturaXML = rutaRepositorio + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneXML();
        			 
        			 rutaFacturaPDF = rutaRepositorio + visorForm.getClaveProveedor() + File.separator + visorForm.getTienePDF().substring(2);
        			 rutaCompleXML = rutaRepositorio + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneComplementoXML();
        			 rutaComplePDF = rutaRepositorio + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneComplementoPDF();
        			 rutaNotaXML = rutaRepositorio + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneNotaCreditoXML();
        			 rutaNotaPDF = rutaRepositorio + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneNotaCreditoPDF();
        			 
        			 if (visorForm.getTieneXML().endsWith(".txt")) {
        				 existeFacturaXML = "A";
        			 }else if ("".equals(visorForm.getTieneXML())) {
        				 existeFacturaXML = "D";
        			 }else {
        				 rutaFacturaXML = rutaRepositorio + visorForm.getClaveProveedor() + File.separator + visorForm.getTieneXML().substring(2);
        				 existeFacturaXML = existeArchivo(rutaFacturaXML);
        			 }
        			 
        			 if ("".equals(visorForm.getTienePDF())) {
        				 existeFacturaPDF = "D";
        			 }else {
        				 existeFacturaPDF = existeArchivo(rutaFacturaPDF);	 
        			 }
        			 
        			 if ("".equals(visorForm.getTieneComplementoXML())) {
        				 existeCompleXML = "D";
        			 }else {
        				 existeCompleXML = existeArchivo(rutaCompleXML); 
        			 }
        			 
        			 if ("".equals(visorForm.getTieneComplementoPDF())) {
        				 existeComplePDF = "D";
        			 }else {
        				 existeComplePDF = existeArchivo(rutaComplePDF);
        			 }
        			 
        			 
        			 if ("".equals(visorForm.getTieneNotaCreditoXML())) {
        				 existeNotaXML = "D";
        			 }else {
        				 existeNotaXML = existeArchivo(rutaNotaXML);
        			 }
        			 
        			 if ("".equals(visorForm.getTieneNotaCreditoPDF())) {
        				 existeNotaPDF = "D";
        			 }else {
        				 existeNotaPDF = existeArchivo(rutaNotaPDF);
        			 }
        			 
        			 altaValidacion(conEmpresa, empresasForm.getEsquema(), visorForm.getFolioEmpresa(), existeFacturaXML, existeFacturaPDF, existeCompleXML, existeComplePDF, existeNotaXML, existeNotaPDF, visorForm.getUuidFactura(), visorForm.getUuidComplemento(), visorForm.getUuidNotaCredito());
        			 
    			 }catch(Exception e) {
    				 e.printStackTrace();
    			 }
    		 }
    		 
    	 }
    	 
    	 
     }catch(Exception e) {
		Utils.imprimeLog("", e);
	 }finally{
        try{
            if(STMT_INSERT != null)
            	STMT_INSERT.close();
            STMT_INSERT = null;
            if(conEmpresa != null)
            	conEmpresa.close();
            conEmpresa = null;
            
        }catch(Exception e){
            conEmpresa = null;
        }
     }
  }
 
 
 public int altaValidacion (Connection con, String esquema, long folioEmpresa, String existeFacturaXML, String existeFacturaPDF, String existeCompleXML, String existeComplePDF, String existeNotaXML, String existeNotaPDF, 
		 String uuidFactura, String uuidComplemento, String uuidNotaCredito){
		int resultado = 0;
		try {
			
			STMT_INSERT.setLong(1, folioEmpresa);
			STMT_INSERT.setString(2, existeFacturaXML);
			STMT_INSERT.setString(3, existeFacturaPDF);
			STMT_INSERT.setString(4, existeCompleXML);
			STMT_INSERT.setString(5, existeComplePDF);
			STMT_INSERT.setString(6, existeNotaXML);
			STMT_INSERT.setString(7, existeNotaPDF);
			STMT_INSERT.setString(8, uuidFactura);
			STMT_INSERT.setString(9, uuidComplemento);
			STMT_INSERT.setString(10, uuidNotaCredito);
			
			STMT_INSERT.executeUpdate();
			
		}catch(SQLException sql) {
			resultado = -100;
			Utils.imprimeLog("", sql);
		}catch(Exception e) {
			resultado = -100;
			Utils.imprimeLog("", e);
		}
		return resultado;
	}
 
 			
 	private String existeArchivo (String pathArchivo) {
 		String existeArchivo = "N";
 		try {
 			File fileArchivo = new File(pathArchivo);
 			if (fileArchivo.exists()) {
 				existeArchivo = "S";
 			}
 		}catch(Exception e) {
 			Utils.imprimeLog("", e);
 		}
 		return existeArchivo;
 	}
}
