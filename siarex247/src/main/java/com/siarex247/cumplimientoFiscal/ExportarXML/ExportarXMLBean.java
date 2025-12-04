package com.siarex247.cumplimientoFiscal.ExportarXML;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.json.simple.JSONObject;

import com.itextpdf.xmltopdf.Comprobante;
import com.itextpdf.xmltopdf.CreaPDF;
import com.itextpdf.xmltopdf.LeerXML;
import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaBean;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaForm;
import com.siarex247.cumplimientoFiscal.Boveda.ExtraerXMLBean;
import com.siarex247.cumplimientoFiscal.BovedaEmitidos.BovedaEmitidosBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.seguridad.Lenguaje.LenguajeBean;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.LeerDatosXML;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsColor;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.utils.UtilsFile;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.utils.UtilsPATH;
import com.siarex247.utils.ZipFiles;
import com.siarex247.validaciones.UtilsSAT;

import ws_api.descarga.DescargaMasiva;

public class ExportarXMLBean {

	
	public static final Logger logger = Logger.getLogger("siarex247");
	private final String ENCONTRADO = "ENCONTRADO";
	private final String NO_ENCONTRADO = "NO ENCONTRADO";
	private final String VACIO = "";
	private HashMap<String, String> MAPA_VALIDACION_SAL = new HashMap<>();
	private String usuarioHTTP = "proceso.exportar@siarex.com";
	
	public ArrayList<ArrayList<String>> obtenerUUID(File fileTXT, String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> listaXML = null;
		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
		ConexionDB connPool = null;
		ResultadoConexion rc = null;
		Connection con = null;
		String directorioXML = null;
		String directorioPDF = null;
		String directorioXML_Complemento = null;
		String directorioPDF_Complemento = null;
		
		String directorioXML_NotaCredito = null;
		String directorioPDF_NotaCredito = null;
		
		
		String rutaArchivoXML = null;
		String rutaArchivoPDF = null;
		
		try{

//A.ESTATUS_PAGO, A.NOMBRE_XML, A.NOMBRE_PDF, A.CLAVE_PROVEEDOR, A.UUID, A.TOTAL, A.SUB_TOTAL, B.RAZON_SOCIAL, B.RFC, A.FOLIO_EMPRESA
			connPool = new ConexionDB();
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			ArrayList<String> infoUUID = UtilsFile.leeArchivoTXT(fileTXT.getAbsolutePath());
			//ArrayList<String> infoUUID = UtilsFile.leeArchivoTXT("C:\\Users\\TIJUANA\\Desktop\\ORDENES DE PRUEBA\\UUID.txt");
			stmt = con.prepareStatement(ExportarXMLQuerys.getConsultarUUID(esquema));
			//String rowTXT [] = null;
			long numeroOrden = 0;
			
			
			for (int x = 0; x < infoUUID.size(); x++ ){
				listaXML = new ArrayList<String>();
				//rowTXT = infoUUID.get(x).toUpperCase().split(";");
				try{
					numeroOrden = Long.parseLong(infoUUID.get(x));
				}catch(Exception e){
					numeroOrden = 0;
				}
				stmt.setLong(1, numeroOrden);
				stmt.setString(2, infoUUID.get(x).trim());
				stmt.setString(3, infoUUID.get(x).trim());
				stmt.setString(4, infoUUID.get(x).trim());
				
				rs = stmt.executeQuery();
				String estatusPago = null;
				
				
				final String A1 = "A1";
				final String A3 = "A3";
				final String A4 = "A4";
				final String A11 = "A1";
				DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
				String XML_COMPLEMENTO = null;
				String XML_NOTA_CREDITO = null;
				
				boolean siEncontro = false;
				while (rs.next()){
					siEncontro = true;
					estatusPago = Utils.noNulo(rs.getString(1));
					if (A1.equalsIgnoreCase(estatusPago) || A3.equalsIgnoreCase(estatusPago) || A4.equalsIgnoreCase(estatusPago) || A11.equalsIgnoreCase(estatusPago)) {
						listaXML = new ArrayList<String>();
						int claveProveedor = rs.getInt(4);
						directorioXML = File.separator + "REPOSITORIOS" + File.separator +esquema+ File.separator+ "PROVEEDORES" + File.separator + claveProveedor+ File.separator + eliminaCaracter(Utils.noNuloNormal(rs.getString(2)));
						directorioPDF = File.separator + "REPOSITORIOS" + File.separator +esquema+ File.separator+ "PROVEEDORES" + File.separator + claveProveedor+ File.separator +  eliminaCaracter(Utils.noNuloNormal(rs.getString(3)));
						rutaArchivoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + directorioXML;
						rutaArchivoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + directorioPDF;
							
						listaXML.add(rutaArchivoXML);
						listaXML.add(rutaArchivoPDF);
						listaXML.add(ENCONTRADO);
						listaXML.add(desEstatus(estatusPago));
						listaXML.add(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(8)))); // RAZON_SOCIAL
						
						listaXML.add(decimalFormat.format(rs.getDouble(7))); // SUB_TOTAL
						listaXML.add(Utils.noNulo(rs.getString(6))); // TOTAL
						listaXML.add(Utils.noNuloNormal(rs.getString(5))); // UUID
						listaXML.add(Utils.noNuloNormal(rs.getString(9))); // RFC
						listaXML.add(decimalFormat.format(rs.getDouble(6))); // TOTAL FORMATEADO
						listaXML.add(Utils.noNuloNormal(rs.getString(10))); // FOLIO EMPRESA
						listaXML.add(Utils.noNuloNormal(rs.getString(5))); // UUID Solo
						listaXML.add(Utils.noNuloNormal(rs.getString(11))); // TIPO DE MONEDA

						XML_COMPLEMENTO = Utils.noNuloNormal(rs.getString(12));
						XML_NOTA_CREDITO = Utils.noNuloNormal(rs.getString(16));
						
						if (VACIO.equalsIgnoreCase(XML_COMPLEMENTO)) {
							listaXML.add(VACIO); // NOMBRE_XML COMPLEMENTO
							listaXML.add(VACIO); // NOMBRE_PDF COMPLEMENTO
							listaXML.add(VACIO); // TOTAL_COMPLEMENTO COMPLEMENTO
							listaXML.add(VACIO); // UUID_COMPLEMENTO COMPLEMENTO
						}else {
							directorioXML_Complemento = File.separator + "REPOSITORIOS" + File.separator +esquema+ File.separator+ "PROVEEDORES" + File.separator + claveProveedor+ File.separator + eliminaCaracter(Utils.noNuloNormal(rs.getString(12)));
							directorioPDF_Complemento = File.separator + "REPOSITORIOS" + File.separator +esquema+ File.separator+ "PROVEEDORES" + File.separator + claveProveedor+ File.separator +  eliminaCaracter(Utils.noNuloNormal(rs.getString(13)));
							rutaArchivoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + directorioXML_Complemento;
							rutaArchivoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + directorioPDF_Complemento;
							
							listaXML.add(rutaArchivoXML); // NOMBRE_XML COMPLEMENTO
							listaXML.add(rutaArchivoPDF); // NOMBRE_PDF COMPLEMENTO
							listaXML.add(Utils.noNuloNormal(rs.getString(14))); // TOTAL_COMPLEMENTO COMPLEMENTO
							listaXML.add(Utils.noNuloNormal(rs.getString(15))); // UUID_COMPLEMENTO COMPLEMENTO
							
						}

						if (VACIO.equalsIgnoreCase(XML_NOTA_CREDITO)) {
							listaXML.add(VACIO); // NOMBRE_XML NOTA CREDITO
							listaXML.add(VACIO); // NOMBRE_PDF NOTA CREDITO
							listaXML.add(VACIO); // TOTAL_NOTA CREDITO
							listaXML.add(VACIO); // UUID_NOTA CREDITO
						}else {
							directorioXML_NotaCredito = File.separator + "REPOSITORIOS" + File.separator +esquema+ File.separator+ "PROVEEDORES" + File.separator + claveProveedor+ File.separator + eliminaCaracter(Utils.noNuloNormal(rs.getString(16)));
							directorioPDF_NotaCredito = File.separator + "REPOSITORIOS" + File.separator +esquema+ File.separator+ "PROVEEDORES" + File.separator + claveProveedor+ File.separator +  eliminaCaracter(Utils.noNuloNormal(rs.getString(17)));
							rutaArchivoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + directorioXML_NotaCredito;
							rutaArchivoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + directorioPDF_NotaCredito;
							
							listaXML.add(rutaArchivoXML); // NOMBRE_XML NOTA CREDITO
							listaXML.add(rutaArchivoPDF); // NOMBRE_PDF NOTA CREDITO
							listaXML.add(Utils.noNuloNormal(rs.getString(18))); // TOTAL_COMPLEMENTO NOTA CREDITO
							listaXML.add(Utils.noNuloNormal(rs.getString(19))); // UUID_COMPLEMENTO NOTA CREDITO
							
						}
						
						
					} else {
						
						listaXML = new ArrayList<String>();
						listaXML.add(infoUUID.get(x));
						listaXML.add(VACIO);
						listaXML.add(NO_ENCONTRADO);	
						listaXML.add(desEstatus(estatusPago));
						listaXML.add(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(8))));
						listaXML.add(VACIO);
						listaXML.add(VACIO);
						
						if (numeroOrden == 0){
							listaXML.add(infoUUID.get(x) + "_"+ NO_ENCONTRADO); // UUID	
						}else{
							listaXML.add(VACIO); // UUID
							
						}
						listaXML.add(Utils.noNuloNormal(rs.getString(9))); // RFC
						listaXML.add(VACIO); // TOTAL FORMATEADO
						if (numeroOrden == 0){
							listaXML.add(Utils.noNuloNormal(rs.getString(10))); // FOLIO EMPRESA
						}else{
							listaXML.add(numeroOrden + "_" + NO_ENCONTRADO); // FOLIO EMPRESA
						}
						listaXML.add(infoUUID.get(x)); // UUID Solo
						listaXML.add(VACIO); // TIPO DE MONEDA
						
						listaXML.add(VACIO); // NOMBRE_XML COMPLEMENTO
						listaXML.add(VACIO); // NOMBRE_PDF COMPLEMENTO
						listaXML.add(VACIO); // TOTAL_COMPLEMENTO COMPLEMENTO
						listaXML.add(VACIO); // UUID_COMPLEMENTO COMPLEMENTO

						listaXML.add(VACIO); // NOMBRE_XML NOTA CREDITO
						listaXML.add(VACIO); // NOMBRE_PDF NOTA CREDITO
						listaXML.add(VACIO); // TOTAL_COMPLEMENTO NOTA CREDITO
						listaXML.add(VACIO); // UUID_COMPLEMENTO NOTA CREDITO
						
						
					}
					
					resultado.add(listaXML);
					
					
				}
				
				if (!siEncontro) {
					listaXML = new ArrayList<String>();
					listaXML.add(infoUUID.get(x));
					listaXML.add(VACIO);
					listaXML.add(NO_ENCONTRADO);
					listaXML.add(VACIO);
					listaXML.add(VACIO);
					listaXML.add(VACIO);
					listaXML.add(VACIO);
					if (numeroOrden == 0){
						listaXML.add(infoUUID.get(x) + " _ "+ NO_ENCONTRADO);	
					}else{
						listaXML.add(VACIO);
						
					}
					listaXML.add(VACIO);
					listaXML.add(VACIO);
					if (numeroOrden == 0){
						listaXML.add(VACIO);
					}else{
						listaXML.add(numeroOrden + " _ " + NO_ENCONTRADO);
							
					}
					listaXML.add(infoUUID.get(x)); // UUID Solo
					listaXML.add(VACIO); // TIPO DE MONEDA
					listaXML.add(VACIO); // NOMBRE_XML COMPLEMENTO
					listaXML.add(VACIO); // NOMBRE_PDF COMPLEMENTO
					listaXML.add(VACIO); // TOTAL_COMPLEMENTO COMPLEMENTO
					listaXML.add(VACIO); // UUID_COMPLEMENTO COMPLEMENTO
					
					resultado.add(listaXML);
					
				}
				
				 if(rs != null){
		            rs.close();
		            rs = null;
				 }
			}

		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(con != null)
	                con.close();
	            con = null;
	        }catch(Exception e){
	            rs = null;
	            stmt = null;
	            con = null;
	        }
        }
		return resultado;
	}
	
	
	/*
	public ArrayList<ArrayList<String>> obtenerUUIDPorRFC(String esquema, String rfc, String fechaInicial){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> listaXML = new ArrayList<>();
		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
		ConexionDB connPool = null;
		ResultadoConexion rc = null;
		Connection con = null;
		// DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
		try{

			connPool = new ConexionDB();
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			stmt = con.prepareStatement(ExportarXMLQuerys.getConsultarBoveda(esquema));
			stmt.setString(1, rfc);
			stmt.setString(2, fechaInicial + " 01:01:01");
			
			logger.info("stmt===>"+stmt);
			rs = stmt.executeQuery();
			
			
			while (rs.next()){
				listaXML.add(Utils.noNuloNormal(rs.getString(1)));
				listaXML.add("");
				listaXML.add(NO_ENCONTRADO);
				resultado.add(listaXML);
				listaXML = new ArrayList<>();
			}
				
			
			

		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(con != null)
	                con.close();
	            con = null;
	        }catch(Exception e){
	            rs = null;
	            stmt = null;
	            con = null;
	        }
        }
		return resultado;
	}
	*/
	
	
	

	public ArrayList<ArrayList<String>> obtenerUUIDPorRFC(String esquema, String rfc, String fechaInicial, String fechaFinal){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> listaXML = new ArrayList<>();
		ArrayList<ArrayList<String>> resultado = new ArrayList<ArrayList<String>>();
		ConexionDB connPool = null;
		ResultadoConexion rc = null;
		Connection con = null;
		// DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
		try{

			connPool = new ConexionDB();
			rc = connPool.getConnection(esquema);
			con = rc.getCon();
			stmt = con.prepareStatement(ExportarXMLQuerys.getConsultarBoveda(esquema));
			stmt.setString(1, rfc);
			stmt.setString(2, fechaInicial + " 01:01:01");
			stmt.setString(3, fechaFinal + " 23:59:59");
			//logger.info("stmt===>"+stmt);
			rs = stmt.executeQuery();
			
			String uuidXML = null;
			
			while (rs.next()){
				uuidXML = Utils.noNuloNormal(rs.getString(1));
				
				listaXML.add(uuidXML);
				listaXML.add(VACIO);
				listaXML.add(NO_ENCONTRADO);
				listaXML.add(VACIO);
				listaXML.add(VACIO);
				listaXML.add(VACIO);
				listaXML.add(VACIO);
				listaXML.add(uuidXML + " _ "+ NO_ENCONTRADO);	
				listaXML.add(VACIO);
				listaXML.add(VACIO);
				listaXML.add(VACIO);
				listaXML.add(uuidXML); // UUID Solo
				listaXML.add(VACIO); // TIPO DE MONEDA
				listaXML.add(VACIO); // NOMBRE_XML COMPLEMENTO
				listaXML.add(VACIO); // NOMBRE_PDF COMPLEMENTO
				listaXML.add(VACIO); // TOTAL_COMPLEMENTO COMPLEMENTO
				listaXML.add(VACIO); // UUID_COMPLEMENTO COMPLEMENTO
				
				resultado.add(listaXML);
				
				listaXML = new ArrayList<>();
					
			}
				
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(con != null)
	                con.close();
	            con = null;
	        }catch(Exception e){
	            rs = null;
	            stmt = null;
	            con = null;
	        }
        }
		return resultado;
	}
	 
	
	
	public int grabarDescarga(String esquema, String usuario, String rutaArchivo)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int resultado = 0;
        ConexionDB connPool = null;
		ResultadoConexion rc = null;
		Connection con = null;
		
        try
        {
        	connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
            stmt = con.prepareStatement(ExportarXMLQuerys.getGrabarDescarga(esquema), PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, rutaArchivo);
            stmt.setString(2, usuario);
            int cant = stmt.executeUpdate();
            if(cant > 0){
                rs = stmt.getGeneratedKeys();
                if(rs.next())
                    resultado = rs.getInt(1);
            }
        }
        catch(Exception e){
            Utils.imprimeLog("", e);
        }finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(con != null)
	                con.close();
	            con = null;
	        }catch(Exception e){
	        	rs = null;
	            stmt = null;
	            con = null;
	        }
        }
        return resultado;
    }

	
	public String consultaRFC (String esquema){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String rfcReceptor = "";
		Connection con = null;
		ResultadoConexion rc = null;
		ConexionDB connPool = null;
		try{
			
			connPool = new ConexionDB();
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
			stmt = con.prepareStatement(ExportarXMLQuerys.getConsultarRFC("siarex"));
			stmt.setString(1, esquema);
			rs = stmt.executeQuery();
			if (rs.next()){
				rfcReceptor = Utils.noNuloNormal(rs.getString(1));
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            if(con != null)
	                con.close();
	            con = null;
	        }catch(Exception e){
	            rs = null;
	            stmt = null;
	            con = null;
	        }
        }
		return rfcReceptor;
	}
	
	
	public String getDescarga (Connection con, String esquema, int idArchivo, String usuarioHttp){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String fechaGeneracion = "";
		try{
			stmt = con.prepareStatement(ExportarXMLQuerys.getConsultarDescarga(esquema));
			
			stmt.setInt(1, idArchivo);
			stmt.setString(2, usuarioHttp);
			rs = stmt.executeQuery();
			if (rs.next()){
				fechaGeneracion = Utils.noNuloNormal(rs.getString(2));
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	            
	        }catch(Exception e){
	            rs = null;
	            stmt = null;
	        }
        }
		return fechaGeneracion;
	}
	
	
	
	public String consultarDescarga (Connection con, String esquema, int idArchivo, String usuarioHttp){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String pathArchivo = "";
		try{
			stmt = con.prepareStatement(ExportarXMLQuerys.getConsultarDescarga(esquema));
			
			stmt.setInt(1, idArchivo);
			stmt.setString(2, usuarioHttp);
			rs = stmt.executeQuery();
			if (rs.next()){
				pathArchivo = Utils.noNuloNormal(rs.getString(1));
			}
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	             
	        }catch(Exception e){
	            rs = null;
	            stmt = null;
	             
	        }
        }
		return pathArchivo;
	}
	
	
	
	public boolean deleteWithChildren(String path) {  
	    File file = new File(path);  
	    if (!file.exists()) {  
	        return true;  
	    }  
	    if (!file.isDirectory()) {  
	        return file.delete();  
	    }  
	    return this.deleteChildren(file) && file.delete();  
	}  
	  
	private boolean deleteChildren(File dir) {  
	    File[] children = dir.listFiles();  
	    boolean childrenDeleted = true;  
	    for (int i = 0; children != null && i < children.length; i++) {  
	        File child = children[i];  
	        if (child.isDirectory()) {  
	            childrenDeleted = this.deleteChildren(child) && childrenDeleted;  
	        }  
	        if (child.exists()) {  
	            childrenDeleted = child.delete() && childrenDeleted;  
	        }  
	    }  
	    return childrenDeleted;  
	}  
	
	
	
	public void generaZIP(ArrayList<ArrayList<String>> listaUUID, String repositorioEmpresa, 
			String usuario, String emailEmpresa, String pwdCorreo, String emailUsuario, String modoAgrupar, 
			String validarSAT, String complementoSAT, String notaCreditoSAT, String descargarFacturas, 
			String descargarComplemento, String descargarNotaCredito, String nombreEmpleado, String tipoBusqueda, long codeOperacion){
		ArrayList<String> rowXML = null;
		//List<String> zipXML = new ArrayList<String>();
		ArrayList<String> zipXML  = new ArrayList<String>();
		ArrayList<ArrayList<String>> zipDatos = new ArrayList<ArrayList<String>>();
		ArrayList<String> xmlNoEncontrados = new ArrayList<String>();
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rc = null;
		Connection con = null;
		try{
			
			rc = connPool.getConnection(repositorioEmpresa);
			con = rc.getCon();
			
			
			for (int x = 0; x < listaUUID.size(); x++){
				rowXML = listaUUID.get(x);
				if (ENCONTRADO.equalsIgnoreCase(rowXML.get(2))){
					zipXML.add(rowXML.get(0)); // XML
					zipXML.add(rowXML.get(1)); // PDF
					zipXML.add(rowXML.get(4)); // Razon Social
					zipXML.add(rowXML.get(12)); // Tipo de Moneda
					zipXML.add(rowXML.get(13)); // XML Complemento
					zipXML.add(rowXML.get(14)); // PDF Complemento

					zipXML.add(rowXML.get(17)); // XML Nota de Credito
					zipXML.add(rowXML.get(18)); // PDF Nota de Credito
					zipXML.add(ENCONTRADO); // Encontrado
					
					zipDatos.add(zipXML);
					zipXML =  new ArrayList<String>();
				}else{
					xmlNoEncontrados.add(rowXML.get(0));
				}
			}
			
			Date fecha = new Date();
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmm");
			String fechaActual = formatDate.format(fecha);
			
			String rutaArchivos = "/REPOSITORIOS/"+repositorioEmpresa+"/EXPORTAR/" + fechaActual + "/" + usuario +"_"+fechaActual;
			String rutaExportar = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + File.separator + "test.txt";
			File fileDir = new File(rutaExportar);
			File parent = new File(fileDir.getParent());
			if (!parent.exists()) {
				parent.mkdirs();

			}
			
			String logo = "logoToyota.png";
			String bandLogo = "S";
			bandLogo = ConfigAdicionalesBean.obtenerValorVariable(con, rc.getEsquema(), "BANDERA_LOGO_TOYOTA");
			if(bandLogo.equalsIgnoreCase("S")) {
				logo = "logoToyota.png";
			} else {
				logo = "logoVacio.png";
			}
			String rutaBoveda = null;
			String repBoveda = "/REPOSITORIOS/"+repositorioEmpresa+"/BOVEDA/";
			String repBovedaEmitidos = "/REPOSITORIOS/"+repositorioEmpresa+"/BOVEDA_EMITIDOS/";
			
			if (!xmlNoEncontrados.isEmpty()){
				String razonEmisor = null;
				String razonReceptor = null;
				String monedaEmisor = null;
				String xmlFactura = null;
				Comprobante _comprobante = null;
				BovedaBean bovedaBean = new BovedaBean();
				BovedaEmitidosBean bovedaEmitidosBean = new BovedaEmitidosBean();
				JSONObject jsonBoveda = null;
				
				for (int y = 0; y < xmlNoEncontrados.size(); y++){
					boolean isEmitido = false;
					jsonBoveda = bovedaBean.consultaBovedaUUID(con, rc.getEsquema(), xmlNoEncontrados.get(y).trim());
					
						if ("".equalsIgnoreCase(Utils.noNulo(jsonBoveda.get("UUID")).toString())) {
							// logger.info("No existe UUID en Boveda===>"+xmlNoEncontrados.get(y));
							
							jsonBoveda = bovedaEmitidosBean.consultaBovedaUUID(con, repBoveda, xmlNoEncontrados.get(y));
							if ("".equalsIgnoreCase(Utils.noNulo(jsonBoveda.get("UUID")).toString())) {
								descargarXML(con, rc.getEsquema(), repositorioEmpresa, xmlNoEncontrados.get(y));
								jsonBoveda = bovedaBean.consultaBovedaUUID(con, rc.getEsquema(), xmlNoEncontrados.get(y).trim());
								if ("".equalsIgnoreCase(Utils.noNulo(jsonBoveda.get("UUID")).toString())) {
									jsonBoveda = bovedaEmitidosBean.consultaBovedaUUID(con, repBoveda, xmlNoEncontrados.get(y));
									isEmitido = true;
								}
							}else {
								isEmitido = true;
							}
							
						}
						if (isEmitido) {
							rutaBoveda = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBovedaEmitidos + jsonBoveda.get("UUID") + ".xml";
						}else {
							rutaBoveda = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + jsonBoveda.get("UUID") + ".xml";	
						}
						
						// logger.info("rutaBoveda====>"+rutaBoveda);
						File fileBoveda = new File(rutaBoveda);
						if (fileBoveda.exists()){
							// xmlFactura = UtilsFile.leeArchivo(fileBoveda.getAbsolutePath());
							if (!"".equalsIgnoreCase(xmlFactura)){
								try {
									_comprobante = LeerXML.ObtenerComprobante(fileBoveda.getAbsolutePath());
								}catch(Exception e) {
									Utils.imprimeLog("", e);
								}
								
								razonEmisor = _comprobante.getEmisor().getNombre();
								monedaEmisor = _comprobante.getMoneda();
								razonReceptor = _comprobante.getReceptor().getNombre();
								
								if ("I".equalsIgnoreCase(Utils.noNulo(_comprobante.getTipoDeComprobante())) || "ingreso".equalsIgnoreCase(Utils.noNulo(_comprobante.getTipoDeComprobante()))) {
									zipXML.add(fileBoveda.getAbsolutePath()); // XML de BOVEDA
									String UUID = jsonBoveda.get("UUID").toString();
									zipXML.add(UUID); // PDF 
								}else {
									zipXML.add(VACIO); // XML de BOVEDA
									zipXML.add("NONE"); // PDF
								}
								
								zipXML.add(razonEmisor); // Razon Social
								zipXML.add(monedaEmisor); // Tipo de Moneda
								
								if ("P".equalsIgnoreCase(Utils.noNulo(_comprobante.getTipoDeComprobante()))) {
									zipXML.add(fileBoveda.getAbsolutePath()); // XML de BOVEDA
									
									// se genera el PDF del complemento
									String UUID = jsonBoveda.get("UUID").toString();
									zipXML.add(UUID); // PDF Complemento
								}else {
									zipXML.add(VACIO); // XML Complemento
									zipXML.add(VACIO); // PDF Complemento
								}
								
								
								if ("E".equalsIgnoreCase(Utils.noNulo(_comprobante.getTipoDeComprobante())) || "egreso".equalsIgnoreCase(Utils.noNulo(_comprobante.getTipoDeComprobante()))) {
									zipXML.add(fileBoveda.getAbsolutePath()); // XML de Nota Credito
									
									// se genera el PDF de la Nota de Credito
									String UUID = jsonBoveda.get("UUID").toString();
									zipXML.add(UUID); // PDF Nota Credito
									
								}else {
									zipXML.add(VACIO); // XML Nota Credito
									zipXML.add(VACIO); // PDF Nota Credito
								}
								
								zipXML.add(ENCONTRADO); // No Encontrado
								
								if (isEmitido) {
									zipXML.add("true"); // Es un Emitido
								}else {
									zipXML.add("false"); // Es un Emitido
								}
								zipXML.add(razonReceptor);
								
								zipDatos.add(zipXML);
								zipXML =  new ArrayList<String>();							
							}
						}
				}
			}
			
			try {
				if (con != null) {
					con.close();	
				}
				con = null;
			}catch(Exception e) {
				Utils.imprimeLog("", e);
			}
			
			
			//logger.info("zipDatos----------->"+zipDatos.size());
			if (zipDatos.size() > 0) { // si encontro algun XML
				// Seccion para generar los archivos agrupados...			
				
					String rutaDestinoXML = null;
					String rutaDestinoPDF = null;
					ArrayList<String> getDatos = null;
					for (int x = 0; x < zipDatos.size(); x++) {
						getDatos = zipDatos.get(x);
						//logger.info("getDatos===>"+getDatos);
						
						if ("NONE".equalsIgnoreCase(modoAgrupar)) { //  Sin agrupacion de XML
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(0)) && "S".equalsIgnoreCase(descargarFacturas)) {
								File fileXML = new File(getDatos.get(0)); // XML
								rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + fileXML.getName();
								File fileXMLDest = new File(rutaDestinoXML); // XML
								UtilsFile.moveFileDirectory(fileXML, fileXMLDest, true, true, true, false);
								fileXML = null;
								fileXMLDest = null;	
							}
							
							
							if (!"NONE".equalsIgnoreCase(getDatos.get(1)) && "S".equalsIgnoreCase(descargarFacturas)) {
								File filePDF = new File(getDatos.get(1));   // PDF
								if (filePDF.exists()) { // si no existe el pdf
									
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + filePDF.getName();
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF del complemento
									// logger.info("generando PDF----------->"+getDatos.get(1));
									String pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(1) + ".pdf";	
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(0).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
								
								
							}
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(4)) && "S".equalsIgnoreCase(descargarComplemento)) { // Busca el XML del complemento
								File fileXML = null;
								fileXML = new File(getDatos.get(4));   // XML Complemento	
								rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + fileXML.getName();
								File filePDFDest = new File(rutaDestinoPDF); // PDF
								UtilsFile.moveFileDirectory(fileXML, filePDFDest, true, true, true, false);
								fileXML = null;
								filePDFDest = null;
							}
							
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(5)) && "S".equalsIgnoreCase(descargarComplemento)) { // Busca el PDF del complemento
								File filePDF = new File(getDatos.get(5));   // PDF Complemento
								if (filePDF.exists()) { // si no existe el pdf
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + filePDF.getName();
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
								}else {
									// se genera el PDF del complemento
									String pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(5) + ".pdf";
									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(4), pathPDF, rutaLogo);
									}catch(Exception e) {
										
									}
								}
							}
							if (!VACIO.equalsIgnoreCase(getDatos.get(6)) && "S".equalsIgnoreCase(descargarNotaCredito)) { // Busca el XML de Nota de Credito
								// File filePDF = new File(getDatos.get(6));   // XML Nota de Credito
								File filePDF = null;
								filePDF = new File(getDatos.get(6));   // XML Nota de Credito	
								rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + filePDF.getName();
								File filePDFDest = new File(rutaDestinoPDF); // PDF
								UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
								filePDF = null;
								filePDFDest = null;
							}
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(7)) && "S".equalsIgnoreCase(descargarNotaCredito)) { // Busca el PDF del Nota de Credito
								File filePDF = new File(getDatos.get(7));   // PDF Complemento
								if (filePDF.exists()) { // si no existe el pdf
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + filePDF.getName();
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;								
								}else {
									// se genera el PDF del complemento
									String pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(7) + ".pdf";
									
										try {
											String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
											new CreaPDF().GenerarByXML(getDatos.get(6), pathPDF, rutaLogo);
										}catch(Exception e) {
											
										}
										
								}
								
								
							}
							
						}else if ("1".equalsIgnoreCase(modoAgrupar)) { //  es por PROVEEDOR que es RAZON SOCIAL
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(0)) && "S".equalsIgnoreCase(descargarFacturas)) {
								File fileXML = new File(getDatos.get(0)); // XML
								
								if ("true".equalsIgnoreCase(getDatos.get(9))) {
									rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" +  fileXML.getName();	
								}else {
									rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" +  fileXML.getName();
								}
								
								
								File fileXMLDest = new File(rutaDestinoXML); // XML
								UtilsFile.moveFileDirectory(fileXML, fileXMLDest, true, true, true, false);
								fileXML = null;
								fileXMLDest = null;	
							}
							
							
							if (!"NONE".equalsIgnoreCase(getDatos.get(1)) && "S".equalsIgnoreCase(descargarFacturas)) {
								File filePDF = new File(getDatos.get(1));   // PDF
								if (filePDF.exists()) { // si no existe el pdf
									
									
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" +  filePDF.getName();	
									}else {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" +  filePDF.getName();
									}

									
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF del complemento
									String pathPDF = null;
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(1) + ".pdf";	
									}else {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(1) + ".pdf";
									}
									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(0).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
							}

							if (!VACIO.equalsIgnoreCase(getDatos.get(4)) && "S".equalsIgnoreCase(descargarComplemento)) {
								File filePDF = new File(getDatos.get(4));   // XML
								 
								if ("true".equalsIgnoreCase(getDatos.get(9))) {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" +  filePDF.getName();	
								}else {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" +  filePDF.getName();
								}

								
								File filePDFDest = new File(rutaDestinoPDF); // PDF
								UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
								filePDF = null;
								filePDFDest = null;
							}
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(5)) && "S".equalsIgnoreCase(descargarComplemento)) {
								File filePDF = new File(getDatos.get(5));   // PDF
								if (filePDF.exists()) { // si no existe el pdf
									
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" +  filePDF.getName();	
									}else {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" +  filePDF.getName();
									}

									
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF del complemento
									String pathPDF = null;
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(5) + ".pdf";	
									}else {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(5) + ".pdf";
									}
									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(4).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}

							}
							
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(6)) && "S".equalsIgnoreCase(descargarNotaCredito)) {
								File filePDF = new File(getDatos.get(6));   // XML Nota
								// 
								if ("true".equalsIgnoreCase(getDatos.get(9))) {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" +  filePDF.getName();	
								}else {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" +  filePDF.getName();
								}
								File filePDFDest = new File(rutaDestinoPDF); // PDF
								UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
								filePDF = null;
								filePDFDest = null;
							}
							
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(7)) && "S".equalsIgnoreCase(descargarNotaCredito)) {
								File filePDF = new File(getDatos.get(7));   // PDF Nota
								
								if (filePDF.exists()) { // si existe el pdf
									
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" +  filePDF.getName();	
									}else {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" +  filePDF.getName();
									}
									
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF del complemento
									String pathPDF = null;
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(7) + ".pdf";	
									}else {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(7) + ".pdf";
									}
									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(6).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
								
							}
							
							
						}else if ("2".equalsIgnoreCase(modoAgrupar)) { // Tipo de Moneda
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(0)) && "S".equalsIgnoreCase(descargarFacturas)) {
								File fileXML = new File(getDatos.get(0)); // XML
								rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + fileXML.getName();
								File fileXMLDest = new File(rutaDestinoXML); // XML
								UtilsFile.moveFileDirectory(fileXML, fileXMLDest, true, true, true, false);
								fileXML = null;
								fileXMLDest = null;	
							}
							

							if (!"NONE".equalsIgnoreCase(getDatos.get(1)) && "S".equalsIgnoreCase(descargarFacturas)) { // pdf
								File filePDF = new File(getDatos.get(1));   // PDF
								if (filePDF.exists()) { // si no existe el pdf
									
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" +  filePDF.getName();
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF del complemento
									String pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(1) + ".pdf";
									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(0).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}

								
							}
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(4)) && "S".equalsIgnoreCase(descargarComplemento)) { // xml
								File filePDF = new File(getDatos.get(4));   // PDF
								rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + filePDF.getName();
								if (filePDF.exists()) {
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDFDest = null;
								}
								filePDF = null;
							}
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(5)) && "S".equalsIgnoreCase(descargarComplemento)) { // pdf complemento
								File filePDF = new File(getDatos.get(5));   // PDF
								if (filePDF.exists()) { // si no existe el pdf
									
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" +  filePDF.getName();
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF del complemento
									String pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(5) + ".pdf";
									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(4).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
							}
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(6)) && "S".equalsIgnoreCase(descargarNotaCredito)) {
								File filePDF = new File(getDatos.get(6));   // XML Nota
								rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + filePDF.getName();
								if (filePDF.exists()) {
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDFDest = null;
								}
								filePDF = null;
							}
							
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(7)) && "S".equalsIgnoreCase(descargarNotaCredito)) {
								File filePDF = new File(getDatos.get(7));   // PDF Nota
								if (filePDF.exists()) { // si no existe el pdf
									
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" +  filePDF.getName();
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
								}else {
									// se genera el PDF del complemento
									String pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(7) + ".pdf";
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(6).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
								
							}
							
							
						}else if ("3".equalsIgnoreCase(modoAgrupar)) { // Tipo de Moneda y Proveedor
							if (!VACIO.equalsIgnoreCase(getDatos.get(0)) && "S".equalsIgnoreCase(descargarFacturas)) {
								File fileXML = new File(getDatos.get(0)); // XML
								
								
								if ("true".equalsIgnoreCase(getDatos.get(9))) {
									rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(10)  + "/" + fileXML.getName();
								}else {
									rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(2)  + "/" + fileXML.getName();	
								}
								
								if (fileXML.exists()) {
									File fileXMLDest = new File(rutaDestinoXML); // XML
									UtilsFile.moveFileDirectory(fileXML, fileXMLDest, true, true, true, false);
									fileXMLDest = null;
								}
								fileXML = null;	
							}
							
							if (!"NONE".equalsIgnoreCase(getDatos.get(1)) && "S".equalsIgnoreCase(descargarFacturas)) { // pdf
								File filePDF = new File(getDatos.get(1));   // PDF
								
								if (filePDF.exists()) { // si no existe el pdf
									
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(10)  + "/" +  filePDF.getName();
									}else {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(2)  + "/" +  filePDF.getName();	
									}

									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF del complemento
									String pathPDF = null;
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(10)  + "/" + getDatos.get(1) + ".pdf";
									}else {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(2)  + "/" + getDatos.get(1) + ".pdf";	
									}									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(0).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
								
							}
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(4)) && "S".equalsIgnoreCase(descargarComplemento)) {
								File filePDF = new File(getDatos.get(4)); 
								
								if ("true".equalsIgnoreCase(getDatos.get(9))) {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(10)  + "/" + filePDF.getName();
								}else {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(2)  + "/" + filePDF.getName();	
								}									

								
								File filePDFDest = new File(rutaDestinoPDF); // PDF
								if (filePDF.exists()) {
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDFDest = null;		
								}
								filePDF = null;
							}
							
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(5)) && "S".equalsIgnoreCase(descargarComplemento)) {
								File filePDF = new File(getDatos.get(5));   // PDF
								
								if (filePDF.exists()) { // si no existe el pdf
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(10)  + "/" +  filePDF.getName();
									}else {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(2)  + "/" +  filePDF.getName();	
									}									

									
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF del complemento
									String pathPDF = null;
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(10)  + "/" + getDatos.get(5) + ".pdf";
									}else {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(2)  + "/" + getDatos.get(5) + ".pdf";	
									}
									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(4).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
							}
							
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(6)) && "S".equalsIgnoreCase(descargarNotaCredito)) {
								File filePDF = new File(getDatos.get(6));   // PDF
								
								if ("true".equalsIgnoreCase(getDatos.get(9))) {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(10)  + "/" + filePDF.getName();
								}else {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(2)  + "/" + filePDF.getName();	
								}

								File filePDFDest = new File(rutaDestinoPDF); // PDF
								if (filePDF.exists()) {
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDFDest = null;		
								}
								filePDF = null;
							}
							
							 
							if (!VACIO.equalsIgnoreCase(getDatos.get(7)) && "S".equalsIgnoreCase(descargarNotaCredito)) {
								File filePDF = new File(getDatos.get(7));   // PDF
								
								if (filePDF.exists()) { // si no existe el pdf
									
									
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(10)  + "/" +  filePDF.getName();
									}else {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(2)  + "/" +  filePDF.getName();	
									}

									
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
								}else {
									// se genera el PDF del complemento
									String pathPDF = null;
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(10)  + "/" + getDatos.get(7) + ".pdf";
									}else {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(3) + "/" + getDatos.get(2)  + "/" + getDatos.get(7) + ".pdf";	
									}

									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(6).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
							}
							
						}else { // Proveedor y Tipo de Moneda
							if (!VACIO.equalsIgnoreCase(getDatos.get(0)) && "S".equalsIgnoreCase(descargarFacturas)) {
								File fileXML = new File(getDatos.get(0)); // XML
								
								
								if ("true".equalsIgnoreCase(getDatos.get(9))) {
									rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(3)  + "/" + fileXML.getName();
								}else {
									rutaDestinoXML = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(3)  + "/" + fileXML.getName();	
								}

								
								File fileXMLDest = new File(rutaDestinoXML); // XML
								UtilsFile.moveFileDirectory(fileXML, fileXMLDest, true, true, true, false);
								fileXML = null;
								fileXMLDest = null;	
							}
							
							//logger.info("getDatos.get(1)===>"+getDatos.get(1));
							 
							if (!"NONE".equalsIgnoreCase(getDatos.get(1)) && "S".equalsIgnoreCase(descargarFacturas)) {
								File filePDF = new File(getDatos.get(1));   // PDF
								if (filePDF.exists()) { // si no existe el pdf
									
									
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(3)  + "/" +  filePDF.getName();
									}else {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(3)  + "/" +  filePDF.getName();	
									}

									
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF de la factura
									String pathPDF = null;
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(3)  + "/" + getDatos.get(1) + ".pdf";
									}else {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(3)  + "/" + getDatos.get(1) + ".pdf";	
									}

									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(0).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
							}
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(4))  && "S".equalsIgnoreCase(descargarComplemento)) {
								File filePDF = new File(getDatos.get(4));   // PDF
								
								if ("true".equalsIgnoreCase(getDatos.get(9))) {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(3)  + "/" + filePDF.getName();
								}else {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(3)  + "/" + filePDF.getName();	
								}

								
								File filePDFDest = new File(rutaDestinoPDF); // PDF
								if (filePDF.exists()) {
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDFDest = null;		
								}
								filePDF = null;								
							}
							
							 
							if (!VACIO.equalsIgnoreCase(getDatos.get(5))  && "S".equalsIgnoreCase(descargarComplemento)) {
								File filePDF = new File(getDatos.get(5));   // PDF
								if (filePDF.exists()) { // si no existe el pdf
									
									
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(3)  + "/" +  filePDF.getName();
									}else {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(3)  + "/" +  filePDF.getName();	
									}

									
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
									
								}else {
									// se genera el PDF del complemento
									String pathPDF = null;
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(3)  + "/" + getDatos.get(5) + ".pdf";
									}else {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(3)  + "/" + getDatos.get(5) + ".pdf";	
									}

									
									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(4).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
							}
							
							
							if (!VACIO.equalsIgnoreCase(getDatos.get(6))  && "S".equalsIgnoreCase(descargarNotaCredito)) {
								File filePDF = new File(getDatos.get(6));   // PDF
								if ("true".equalsIgnoreCase(getDatos.get(9))) {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(3)  + "/" + filePDF.getName();
								}else {
									rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(3)  + "/" + filePDF.getName();	
								}

								
								File filePDFDest = new File(rutaDestinoPDF); // PDF
								if (filePDF.exists()) {
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDFDest = null;		
								}
								filePDF = null;								
							}
							
							 
							if (!VACIO.equalsIgnoreCase(getDatos.get(7))  && "S".equalsIgnoreCase(descargarNotaCredito)) {
								File filePDF = new File(getDatos.get(7));   // PDF
								if (filePDF.exists()) { // si no existe el pdf
									
									
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(3)  + "/" +  filePDF.getName();
									}else {
										rutaDestinoPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(3)  + "/" +  filePDF.getName();	
									}

									
									File filePDFDest = new File(rutaDestinoPDF); // PDF
									UtilsFile.moveFileDirectory(filePDF, filePDFDest, true, true, true, false);
									filePDF = null;
									filePDFDest = null;
								}else {
									// se genera el PDF del complemento
									String pathPDF = null;
									if ("true".equalsIgnoreCase(getDatos.get(9))) {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(10) + "/" + getDatos.get(3)  + "/" + getDatos.get(7) + ".pdf";
									}else {
										pathPDF = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaArchivos + "/" + getDatos.get(2) + "/" + getDatos.get(3)  + "/" + getDatos.get(7) + ".pdf";	
									}

									try {
										String rutaLogo = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + logo;
										new CreaPDF().GenerarByXML(getDatos.get(6).toString(), pathPDF, rutaLogo);
									}catch(Exception e) {
										Utils.imprimeLog("", e);
									}
								}
								
							}
							
						}
					}
	// Termina...
				// Se genera el excel con la informacion				
				// logger.info("UtilsPATH.RUTA_PUBLIC_PRINCIPAL----->"+UtilsPATH.RUTA_PUBLIC_PRINCIPAL);
				// logger.info("usuario----->"+usuario);
				// logger.info("fechaActual----->"+fechaActual);
				
				String rfcReceptor = consultaRFC(repositorioEmpresa);
				try {
					rc = connPool.getConnection(repositorioEmpresa);
					con = rc.getCon();
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				}

				
				// String rfcReceptor = "PLA720201746"; 
				String rutaDestinoXLS =  generaExcel(con, rc.getEsquema(), listaUUID, UtilsPATH.RUTA_PUBLIC_PRINCIPAL, repositorioEmpresa, usuario, fechaActual, rfcReceptor, validarSAT, complementoSAT, notaCreditoSAT);
				if (!"".equals(rutaDestinoXLS )){
					zipXML.add(rutaDestinoXLS); // XLS
				}
				
				String rutaDestinoXLS_Bitacora =  generaExcelBitacora(con, rc.getEsquema(), UtilsPATH.RUTA_PUBLIC_PRINCIPAL, repositorioEmpresa, fechaActual, rfcReceptor, codeOperacion);
				if (!"".equals(rutaDestinoXLS_Bitacora )){
					zipXML.add(rutaDestinoXLS_Bitacora); // XLS
				}
				
				// genera excel de convertirXML
				ArrayList<BovedaForm> listaBoveda = new ArrayList<>();
				for (int x = 0; x < zipDatos.size(); x++) {
					ArrayList<String> listaDatos =  zipDatos.get(x);
					if (!"".equalsIgnoreCase(listaDatos.get(0))) {
						BovedaForm bovedaForm = new BovedaForm();
						bovedaForm.setUuid(listaDatos.get(0));	
						listaBoveda.add(bovedaForm);
					}
					
					if (!"".equalsIgnoreCase(listaDatos.get(4))) {
						BovedaForm bovedaForm = new BovedaForm();
						bovedaForm.setUuid(listaDatos.get(4));
						listaBoveda.add(bovedaForm);
					}
					if (!"".equalsIgnoreCase(listaDatos.get(6))) {
						BovedaForm bovedaForm = new BovedaForm();
						bovedaForm.setUuid(listaDatos.get(6));
						listaBoveda.add(bovedaForm);
					}
					// logger.info("listaDatos===>"+bovedaForm.getUuid());
				}
				 
				
				SXSSFWorkbook  libro = new SXSSFWorkbook(100); // Keep 100 rows in memory
				SXSSFSheet hoja1 = libro.createSheet("Complementos Recibidas");
				SXSSFSheet hoja2 = libro.createSheet("Ingresos Recibidas");
				SXSSFSheet hoja3 = libro.createSheet("Egresos Recibidas");
				
				LeerDatosXML leerXML = new LeerDatosXML();
			    ArrayList<Comprobante> listaComprobantes = leerXML.leerElementosExportar(listaBoveda);
			    
			    ExtraerXMLBean extraerBean = new ExtraerXMLBean();
			    
			    ResultadoConexion rcSAT = null;
			    Connection conSAT = null;
			    try {
					rcSAT = connPool.getConnectionSAT();
					conSAT = rcSAT.getCon();
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				}
			    
			    extraerBean.generarExcelComplemento(conSAT, rcSAT.getEsquema(), hoja1, listaComprobantes, libro, true, MAPA_VALIDACION_SAL);
			    extraerBean.generarExcelFacturas(conSAT, rcSAT.getEsquema(), hoja2, listaComprobantes, libro, true, MAPA_VALIDACION_SAL);
			    extraerBean.generarExcelNotaCredito(conSAT, rcSAT.getEsquema(), hoja3, listaComprobantes, libro, true, MAPA_VALIDACION_SAL);
			    
			    
			    try {
			    	conSAT.close();
			    	conSAT = null;
				}catch(Exception e) {
					Utils.imprimeLog("", e);
				}
			    
			    // siarexXMLAEXCEL
			    // Date fechaHoy = new Date();
				//String fecA = null;
				// SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
				// fecA = formato.format(fechaHoy);
				
			    // String nombreReporte = "siarexXMLAEXCEL_"+fecA;
			    String fechaHoy = UtilsFechas.getFechaActualNumero();
			    String nombreReporte = rfcReceptor+"_InformacionCompleta_"+fechaHoy.substring(0, 8) + "_" + fechaHoy.substring(8, 12);
			    String rutaDestinoXLSConvertir = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + "/REPOSITORIOS/"+repositorioEmpresa+"/EXPORTAR/"+fechaActual+"/"+nombreReporte+".xlsx";
				// String destinoArchivo = "C:/Tomcat9/ass247/cm/files/"+ nombreReporte; 
				File fileXLS = new File(rutaDestinoXLSConvertir);
				if (fileXLS.exists()) {
					fileXLS.delete();
				}
				
				FileOutputStream elFichero = new FileOutputStream(rutaDestinoXLSConvertir);
	            libro.write(elFichero);
	            elFichero.close();
	            libro.close();
	            
	// Se Termina			
				boolean bandZIP = false;
				String rutaRep = null;
				
				String rutaZippear = "/REPOSITORIOS/"+repositorioEmpresa+"/EXPORTAR/"+fechaActual;
				// String rutaZipDest = "/REPOSITORIOS/"+esquema+"/EXPORTAR";
				rutaRep = "/REPOSITORIOS/"+repositorioEmpresa+"/EXPORTAR/"+usuario+"_"+fechaActual+".zip";
				
				ZipFiles zipFiles = new ZipFiles();
				String rutaZIP = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaZippear;
				String zipDirName = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaRep;
				
				logger.info("rutaZIP--------->"+rutaZIP);
				File dir = new File(rutaZIP); // origen
		        // String zipDirName = rutaZIPDest + "/" + usuario + "_" + fechaActual + ".zip";   // destino
		        zipFiles.zipDirectory(dir, zipDirName); // se genera el archivo .zip
		        bandZIP = true;
		        
		        String pathDelete = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + rutaZippear;
		        deleteWithChildren(pathDelete); // se elimina el directorio con los archivos...
		        	
				//CorreoBean.passwordEmisorMensaje = pwdCorreo;
		        //CorreoBean.usuarioEmisorMensaje  = emailEmpresa;
		        
		        int idArchivo = 0;
		        if (bandZIP){
		        	idArchivo = grabarDescarga(repositorioEmpresa, usuario, rutaRep);	
				}
		        
		        // String dominio = Utils.getInfoCorreo("DOM");
		        String dominio = UtilsPATH.DOMINIO_PRINCIPAL;
		        String urlZIP = "https://"+dominio+"/siarexDescarga/descargarSiarex.jsp?idArchivo="+idArchivo;
		        // String urlZIP = "http://localhost:8080/siarexDescarga/descargarSiarex.jsp?idArchivo="+idArchivo;
		        
		        logger.info("*********** bandZIP **************************"+bandZIP);
		        logger.info("*********** emailUsuario **************************"+emailUsuario);
		        String listaCorreos [] = {emailUsuario};
	        	String sbHTML = UtilsHTML.generaHTMLExport(nombreEmpleado, urlZIP, dominio);
	        	logger.info("*********** sbHTML **************************"+sbHTML);
	        	EnviaCorreoGrid.enviarCorreo(null, sbHTML, false, listaCorreos, null,  "Descarga de Facturas SIAREX", emailEmpresa,pwdCorreo );
			}else {
				String listaCorreos [] = {emailUsuario};
				String sbHTML = "<p style='font-size: 16px; color: #00539f;'>Estimado usuario, le informamos que no se encontro informaci(o)n con los datos proporcionados.</p>";
	        	logger.info("*********** sbHTML **************************"+sbHTML);
	        	EnviaCorreoGrid.enviarCorreo(null, sbHTML, false, listaCorreos, null,  "Descarga de Facturas SIAREX", emailEmpresa,pwdCorreo);
			}
			
        	
		}catch(Exception e){
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
	}
	
	
	 
	public String generaExcel(Connection con, String esquemaBD, ArrayList<ArrayList<String>> listaUUID, String rutaFinal, String repositorioEmpresa, String usuario, String fechaActual, 
			String rfcReceptor, String validarSAT, String complementoSAT, String notaCreditoSAT){
		SXSSFWorkbook  libro = new SXSSFWorkbook(100); // Keep 100 rows in memory
		SXSSFSheet hoja1 = libro.createSheet("Informacion Basica");
	    
		String rutaDestinoXLS = null;
		 Comprobante _comprobante = null;
		try{
			Row header = hoja1.createRow(0);
			
			   CellStyle styleTitulo = libro.createCellStyle();
			   Font headerFont = libro.createFont();
		       headerFont.setFontHeightInPoints((short)10);
		       headerFont.setColor(IndexedColors.WHITE.getIndex());
		       headerFont.setFontName("Arial");
		       headerFont.setBold(true);
		       //styleTitulo.setFillForegroundColor(new XSSFColor(new java.awt.Color(12, 57, 90)));
		       styleTitulo.setFillForegroundColor(new XSSFColor(UtilsColor.getBytes(12, 57, 90)));
		       
		       
		       styleTitulo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		       styleTitulo.setFont(headerFont);
		       styleTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
		       
		       CellStyle styleSubTitulo = libro.createCellStyle();
			   Font fontSub = libro.createFont();
			   fontSub.setFontHeightInPoints((short)10);
			   fontSub.setFontName("Arial");
			   fontSub.setBold(true);
		       styleSubTitulo.setFont(fontSub);
		       styleSubTitulo.setAlignment(HorizontalAlignment.CENTER);
		       
			   Cell monthCell = header.createCell(0);
			   monthCell.setCellValue("Sistema de Recepcion de XML");
			   monthCell.setCellStyle(styleTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A1:N1"));
			  
			   header = hoja1.createRow(1);
			   header.setHeightInPoints(18);
			   Cell monthCell2 = header.createCell(0);
			   monthCell2.setCellValue("Detalle Boveda XML");
			   monthCell2.setCellStyle(styleSubTitulo);
			   hoja1.addMergedRegion(CellRangeAddress.valueOf("A2:N2"));
			    
			
			
			String[] headers = new String[]{
					"Orden de Compra",
		            "UUID",
		            "Razón Social",
		            "Sub-Total",
		            "Total",
		            "Status CFDI",
		            "Estado Factura",
		            "UUID Complemento",
		            "Status Complemento",
		            "Estado Complemento",
		            "UUID Nota de Credito",
		            "Status Nota de Credito",
		            "Estado Nota de Credito",
		            "Tipo de Comprobante",
		        };
			
			   
			Row headerRow = hoja1.createRow(2);
	        String headerEncabezados = null;
	        Cell cell = null;
	        for (int i = 0; i < headers.length; ++i) {
	        	headerEncabezados = headers[i];
	            cell = headerRow.createCell(i);
	            // cell.setCellStyle(headerStyle);
	            cell.setCellValue(headerEncabezados);
	            
	        }
	        ArrayList<String> rowXML = null;
	        String UUID = null;
	        String ORDEN_COMPRA = null;
	        String RAZON_SOCIAL = null;
	        String SUB_TOTAL = null;
	       //String TOTAL_FORMATEADO = null;
	        double TOTAL = 0;
	        // double TOTAL_COMPLEMENTO = 0;
	        // double TOTAL_NOTA_CREDITO = 0;
	        String ESTATUS_SAT = null;
	        String ESTADO_SAT = null;
	        String ENCONTRADO_ROW = null;
	        String RFC = null;
	        String UUID_COMPLEMENTO = null;
	        String UUID_NOTA_CREDITO = null;
	        String ESTATUS_COMPLEMENTO_SAT = null;
	        String ESTADO_COMPLEMENTO_SAT = null;
	        String ESTATUS_NOTA_CREDITO_SAT = null;
	        String ESTADO_NOTA_CREDITO_SAT = null;
	        
	        String NOMBRE_XML_COMPLEMENTO = null;
	        String NOMBRE_XML_NOTA_CREDITO = null;
	        String TIPO_COMPROBANTE = null;
	        
	        String datosSAT [] = {"",""};
	        
	        Row dataRow = null;
	        String repBoveda = "/REPOSITORIOS/"+repositorioEmpresa+"/BOVEDA/";
	        String repBovedaEmitidos = "/REPOSITORIOS/"+repositorioEmpresa+"/BOVEDA_EMITIDOS/";
			
	        
			// DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
			
			JSONObject jsonBoveda = null;
			BovedaBean bovedaBean = new BovedaBean();
			BovedaEmitidosBean bovedaEmitidosBean = new BovedaEmitidosBean();
			String xmlComprobante = "";
			int numRow = 3;
			
			String rfcValidarSAT = null;
	        for (int i = 0; i < listaUUID.size(); ++i) {
	        	
	        	rowXML = listaUUID.get(i);
	        	
	        	 if (i % 100 == 0 && i != 0) { // Flush every 100 rows (after the first 100)
	                    ((SXSSFSheet) hoja1).flushRows(100); // Retain the last 100 rows in memory
	                }

	            dataRow = hoja1.createRow(numRow++);
	            
	            ESTATUS_COMPLEMENTO_SAT = VACIO;
        		ESTADO_COMPLEMENTO_SAT  = VACIO;
        		ESTATUS_NOTA_CREDITO_SAT = VACIO;
        		ESTADO_NOTA_CREDITO_SAT  = VACIO;
        		UUID_COMPLEMENTO = VACIO;
        		UUID_NOTA_CREDITO = VACIO;
        		
	            ESTATUS_SAT = VACIO;
	            ESTADO_SAT = VACIO;
	            ENCONTRADO_ROW = rowXML.get(2);
	            RAZON_SOCIAL = rowXML.get(4);
	            SUB_TOTAL = rowXML.get(5);
	            UUID = rowXML.get(7);
	            RFC  = rowXML.get(8);
	            // TOTAL_FORMATEADO = rowXML.get(9);
	            
	            ORDEN_COMPRA = rowXML.get(10);
	            TIPO_COMPROBANTE = VACIO;
	            
	            TOTAL = 0;
	            if (ENCONTRADO.equalsIgnoreCase(ENCONTRADO_ROW)){
	              // CALCULA EN EL SAT EL XML
	            	TOTAL = Double.parseDouble( rowXML.get(6));
	            	if ("S".equalsIgnoreCase(validarSAT)) {
	            		datosSAT = UtilsSAT.validaSAT(RFC, rfcReceptor, TOTAL, UUID);
	            		ESTATUS_SAT = datosSAT[1];
	            		ESTADO_SAT  = datosSAT[0];
	            	}else {
	            		ESTATUS_SAT = VACIO;
	            		ESTADO_SAT  = VACIO;
	            	}
	            	MAPA_VALIDACION_SAL.put(UUID, ESTADO_SAT);
	            	
	            	NOMBRE_XML_COMPLEMENTO = rowXML.get(13);
	            	if (!VACIO.equalsIgnoreCase(NOMBRE_XML_COMPLEMENTO)) {
	            		// TOTAL_COMPLEMENTO  = Double.parseDouble( rowXML.get(15));
	            		UUID_COMPLEMENTO = rowXML.get(16);
	            		if ("S".equalsIgnoreCase(complementoSAT)) {
		            		datosSAT = UtilsSAT.validaSAT(RFC, rfcReceptor, 0, UUID_COMPLEMENTO);
		            		ESTATUS_COMPLEMENTO_SAT = datosSAT[1];
		            		ESTADO_COMPLEMENTO_SAT  = datosSAT[0];
		            	}else {
		            		ESTATUS_COMPLEMENTO_SAT = VACIO;
		            		ESTADO_COMPLEMENTO_SAT  = VACIO;
		            	}
	            		MAPA_VALIDACION_SAL.put(UUID_COMPLEMENTO, ESTADO_COMPLEMENTO_SAT);
	            	}
	            	
	            	
	            	NOMBRE_XML_NOTA_CREDITO = rowXML.get(17);
	            	if (!VACIO.equalsIgnoreCase(NOMBRE_XML_NOTA_CREDITO)) {
	            		double TOTAL_NOTA_CREDITO  = Double.parseDouble(rowXML.get(19));
	            		UUID_NOTA_CREDITO = rowXML.get(20);
	            		if ("S".equalsIgnoreCase(notaCreditoSAT)) {
	            			
		            		datosSAT = UtilsSAT.validaSAT(RFC, rfcReceptor, TOTAL_NOTA_CREDITO, UUID_NOTA_CREDITO);
		            		ESTATUS_NOTA_CREDITO_SAT = datosSAT[1];
		            		ESTADO_NOTA_CREDITO_SAT  = datosSAT[0];
		            	}else {
		            		ESTATUS_NOTA_CREDITO_SAT = VACIO;
		            		ESTADO_NOTA_CREDITO_SAT  = VACIO;
		            	}
	            		MAPA_VALIDACION_SAL.put(UUID_NOTA_CREDITO, ESTADO_NOTA_CREDITO_SAT);
	            	}
	            	
	            	if (rowXML.size() == 22) {
	            		TIPO_COMPROBANTE = rowXML.get(21);
	            	}
	            	
	            	
	            }else {
	            	// rowXML.get(11), es el UUID Solo
	            	// buscar en boveda nuevamente......
	            	boolean isExiste = false;
	            	boolean isEmitido = false;
	            	try {
	            		 jsonBoveda = bovedaBean.consultaBovedaUUID(con, esquemaBD, rowXML.get(11));
	            		 if ("".equalsIgnoreCase(Utils.noNulo(jsonBoveda.get("UUID")).toString())) {
	            			 jsonBoveda = bovedaEmitidosBean.consultaBovedaUUID(con, esquemaBD, rowXML.get(11));
	            			 if (!"".equalsIgnoreCase(Utils.noNulo(jsonBoveda.get("UUID")).toString())) {
	            				 isEmitido = true;
	            			 }
	            		 }else {
	            			 isEmitido = false;
	            		 }
						// rutaBoveda = UtilsPATH.RUTA_PUBLIC_PRINCIPAL + repBoveda + xmlNoEncontrados.get(y) + ".xml";
						
						if ("".equalsIgnoreCase(Utils.noNulo(jsonBoveda.get("UUID")).toString())) {
							 // String xmlComprobante = rutaFinal + repBoveda + rowXML.get(11) + ".xml";
					    	// _comprobante = LeerXML.ObtenerComprobante(xmlComprobante);
					    	isExiste = false;
						}else {
							
							if (isEmitido) {
								xmlComprobante = rutaFinal + repBovedaEmitidos + jsonBoveda.get("UUID") + ".xml";	
							}else {
								xmlComprobante = rutaFinal + repBoveda + jsonBoveda.get("UUID") + ".xml";
							}
					    	_comprobante = LeerXML.ObtenerComprobante(xmlComprobante);
					    	if (_comprobante == null) {
					    		isExiste = false;
					    	}else {
					    		isExiste = true;	
					    	}
					    	
						}
				    }catch(Exception e) {
				    	Utils.imprimeLog("", e);
				    	isExiste = false;
				    }

	            	if (isExiste && _comprobante != null) {
	            		
	            		SUB_TOTAL  =  String.valueOf(_comprobante.getSubTotal());
	            		if (isEmitido) {
	            			RAZON_SOCIAL = _comprobante.getReceptor().getNombre();
	            			rfcValidarSAT = _comprobante.getReceptor().getRfc();
	            		}else {
	            			RAZON_SOCIAL = _comprobante.getEmisor().getNombre();
	            			rfcValidarSAT = rfcReceptor;
	            		}
	            		
	            		// String totalXML =  String.valueOf(_comprobante.getTotal());
	            		UUID = rowXML.get(11);
	            		RFC    = _comprobante.getEmisor().getRfc();
	            		TOTAL        = _comprobante.getTotal();
	            		// TOTAL_FORMATEADO = decimalFormat.format(TOTAL);
	            		TIPO_COMPROBANTE = _comprobante.getTipoDeComprobante();
	            		
	            		if ("S".equalsIgnoreCase(validarSAT)) {
					    	datosSAT = UtilsSAT.validaSAT(RFC, rfcValidarSAT, TOTAL, rowXML.get(11));
			            	ESTATUS_SAT = datosSAT[1];
			            	ESTADO_SAT  = datosSAT[0];
					    }else {
					    	ESTATUS_SAT = VACIO;
					    	ESTADO_SAT  = VACIO;
					    }
	            		MAPA_VALIDACION_SAL.put(rowXML.get(11), ESTADO_SAT);
	            	} 
	            }
	            
	            dataRow.createCell(0).setCellValue(ORDEN_COMPRA);
	            dataRow.createCell(1).setCellValue(UUID);
	            dataRow.createCell(2).setCellValue(RAZON_SOCIAL);
	            dataRow.createCell(3).setCellValue(Utils.convertirDouble(SUB_TOTAL));
	            //dataRow.createCell(4).setCellValue(TOTAL_FORMATEADO);
	            dataRow.createCell(4).setCellValue(Utils.noNuloDouble(TOTAL));
	            dataRow.createCell(5).setCellValue(ESTATUS_SAT);
	            dataRow.createCell(6).setCellValue(ESTADO_SAT);
	            dataRow.createCell(7).setCellValue(UUID_COMPLEMENTO);
	            dataRow.createCell(8).setCellValue(ESTATUS_COMPLEMENTO_SAT);
	            dataRow.createCell(9).setCellValue(ESTADO_COMPLEMENTO_SAT);
	            dataRow.createCell(10).setCellValue(UUID_NOTA_CREDITO);
	            dataRow.createCell(11).setCellValue(ESTATUS_NOTA_CREDITO_SAT);
	            dataRow.createCell(12).setCellValue(ESTADO_NOTA_CREDITO_SAT);
	            dataRow.createCell(13).setCellValue(TIPO_COMPROBANTE);
	            
	            
	        }

	        // logger.info("rutaDestinoXLS====>"+rutaDestinoXLS);
	        
	      //rutaDestinoXLS = rutaFinal + "/REPOSITORIOS/"+repositorioEmpresa+"/EXPORTAR/"+fechaActual+"/"+usuario+"_"+fechaActual+".xls";
	        // yyyyMMdd HH:mm:ss
	        // yyyyMMddHHmmss
	        String fechaHoy = UtilsFechas.getFechaActualNumero();
			String nombreReporte = rfcReceptor+"_InformacionBasica_"+fechaHoy.substring(0, 8) + "_" + fechaHoy.substring(8, 12);
	        rutaDestinoXLS = rutaFinal + "/REPOSITORIOS/"+repositorioEmpresa+"/EXPORTAR/"+fechaActual+"/"+nombreReporte+".xlsx";
	        
	        FileOutputStream file = new FileOutputStream(rutaDestinoXLS);
	        libro.write(file);
	        file.close();

	        
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				libro.close();
			}catch(Exception e){
				libro = null;
			}
		}
		return rutaDestinoXLS;
	}
	
	
	
	public String generaExcelBitacora(Connection con, String esquemaBD,  String rutaFinal, String repositorioEmpresa, String fechaActual, String rfcReceptor, long codeOperacion){
		SXSSFWorkbook  workbook = new SXSSFWorkbook(100); // Keep 100 rows in memory
		SXSSFSheet sheet = workbook.createSheet();
		String rutaDestinoXLS = "";
		 ExportarPorXML expXMLBean = new ExportarPorXML();
		try{
			
			workbook.setSheetName(0, "XML no permitidos");
			String[] headers = new String[]{
					"UUID",
		            "RFC Receptor",
		            "Razón Social",
		            "Tipo de Comprobante",
		            "Mensaje de Validacion",
		    };
			
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);
	        
	        CellStyle style = workbook.createCellStyle();
	        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        Font fuente = workbook.createFont();
			fuente.setFontHeightInPoints((short)10);
			fuente.setFontName(HSSFFont.FONT_ARIAL);
			   
	        Row headerRow = sheet.createRow(0);
	        String header = null;
	        Cell cell = null;
	        for (int i = 0; i < headers.length; ++i) {
	            header = headers[i];
	            cell = headerRow.createCell(i);
	            cell.setCellStyle(headerStyle);
	            cell.setCellValue(header);
	            
	        }
	        Row dataRow = null;
			
	        sheet.setColumnWidth(0, 13000);
	        sheet.setColumnWidth(1, 5000);
	        sheet.setColumnWidth(2, 15000);
	        sheet.setColumnWidth(3, 10000);
	        sheet.setColumnWidth(4, 20000);
	        
        	ArrayList<ValidacionXMLForm> listaDetalle = expXMLBean.consultarBitacora(con, esquemaBD, codeOperacion);
        	ValidacionXMLForm valXML = null;
	        for (int i = 0; i < listaDetalle.size(); ++i) {
	        	valXML = listaDetalle.get(i);
	            dataRow = sheet.createRow(i + 1);
	            
	            dataRow.createCell(0).setCellValue(valXML.getUuid());
	            dataRow.createCell(1).setCellValue(valXML.getRfcReceptor());
	            dataRow.createCell(2).setCellValue(valXML.getRazonSocial());
	            dataRow.createCell(3).setCellValue(valXML.getTipoComprobante());
	            dataRow.createCell(4).setCellValue(valXML.getMensajeOperacion());
	            
	            
	        }
	        
	        if (listaDetalle.size() > 0) {
		        String fechaHoy = UtilsFechas.getFechaActualNumero();
				String nombreReporte = rfcReceptor+"_DetalleErroresXML_"+fechaHoy.substring(0, 8) + "_" + fechaHoy.substring(8, 12);
		        rutaDestinoXLS = rutaFinal + "/REPOSITORIOS/"+repositorioEmpresa+"/EXPORTAR/"+fechaActual+"/"+nombreReporte+".xlsx";
		        
		        FileOutputStream file = new FileOutputStream(rutaDestinoXLS);
		        workbook.write(file);
		        file.close();
	        }
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
			try{
				workbook.close();
			}catch(Exception e){
				workbook = null;
			}
		}
		return rutaDestinoXLS;
	}
	
	
	private Double parseaDoble(String valor){
		double retorno = 0;
		try{
			retorno = Double.parseDouble(valor);
		}catch(Exception e){
			retorno = 0;
		}
		return retorno;
	}
	
	public void procesaArchivo(final File fileTXT, final String esquema, final String usuarioHTTP, 
				final String emailEmpresa, final String pwdCorreo, final String nombreEmpleado, final String emailUsuario, final String modoAgrupar, 
				final String validarSAT, final String complementoSAT, final String notaCreditoSAT, final String descargarFacturas, 
				final String descargarComplemento, final String descargarNotaCredito, final String tipoBusqueda, final String rfcProveedor, 
				final String fechaInicial, final String fechaFinal, final String rutaXMLProcesar, final long codeOperacion){
		try{
			Thread generaXML = new Thread(new Runnable() {
				public void run() {
					logger.info("*********** PROCESO DE EXPORTAR ARCHIVO XML **************************");
					ArrayList<ArrayList<String>> listaUUID =  null;
					if ("TEXTO".equalsIgnoreCase(tipoBusqueda)) {
						listaUUID =  obtenerUUID(fileTXT, esquema);
						// generaZIP(listaUUID, esquema, usuario, emailEmpresa, pwdCorreo, emailUsuario, modoAgrupar, validarSAT, complementoSAT, notaCreditoSAT, descargarFacturas, descargarComplemento, descargarComplemento, nombreEmpleado, tipoBusqueda);
					}else if ("RFC".equalsIgnoreCase(tipoBusqueda)) {
						listaUUID = obtenerUUIDPorRFC(esquema, rfcProveedor, fechaInicial, fechaFinal);
						// generaZIP(listaUUID, esquema, usuario, emailEmpresa, pwdCorreo, emailUsuario, modoAgrupar, validarSAT, complementoSAT, notaCreditoSAT, descargarFacturas, descargarComplemento, descargarComplemento, nombreEmpleado, tipoBusqueda);
					}else if ("XML".equalsIgnoreCase(tipoBusqueda)){
						logger.info("Procesando informacion en bean de exportar........");
						ExportarPorXML exportarXML = new ExportarPorXML();
						String rfcReceptor = consultaRFC(esquema);
						listaUUID = exportarXML.obtenerUUIDPorXML(esquema, rutaXMLProcesar, rfcReceptor, codeOperacion, usuarioHTTP);
						
					}else {
						listaUUID =  obtenerUUID(fileTXT, esquema);
						// generaZIP(listaUUID, esquema, usuario, emailEmpresa, pwdCorreo, emailUsuario, modoAgrupar, validarSAT, complementoSAT, notaCreditoSAT, descargarFacturas, descargarComplemento, descargarComplemento, nombreEmpleado, tipoBusqueda);
					}
					
					generaZIP(listaUUID, esquema, usuarioHTTP, emailEmpresa, pwdCorreo, emailUsuario, modoAgrupar, validarSAT, complementoSAT, notaCreditoSAT, descargarFacturas, descargarComplemento, descargarComplemento, nombreEmpleado, tipoBusqueda, codeOperacion);
					// eliminaArchivo(fileTXT.getAbsolutePath());
					if (!"".equalsIgnoreCase(rutaXMLProcesar)) {
						eliminaDirectorio(rutaXMLProcesar);
					}
					
					logger.info("*********** TERMINO PROCESO DE EXPORTAR **************************");
				}
			});
			generaXML.start();

		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
	}
	
	
	
	private void eliminaArchivo(String rutaArchivo){
		try {
			File file = new File(rutaArchivo);
			logger.info("Eliminando archivo : "+rutaArchivo);
			file.delete();	
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		
	}
	
	private void eliminaDirectorio(String rutaDir){
		try {
			File archivo = new File(rutaDir);
		    deleteFile(archivo);
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	private void deleteFile(File file){
	    if (file.exists()) {
	        if (file.isFile())
	            file.delete();
	        else {
	            File f[]=file.listFiles();
	            for (int i = 0; i < f.length; i++) {
	                    deleteFile(f[i]);
	            }
	            file.delete();
	        }
	    }
	       
	}
	
	private String eliminaCaracter(String nombreArchivo){
		String nombreArchivoFinal = nombreArchivo.replaceAll("&=", "");
		return nombreArchivoFinal ;	
	}
	


	private String desEstatus(String claveEstatus){
		if ("A1".equalsIgnoreCase(claveEstatus)){
			return "SERV. NO RECIBIDO";
		}
		if ("A2".equalsIgnoreCase(claveEstatus)){
			return "SERVICIO CON RECIBO Y SIN FACTURA";
		}
		if ("A3".equalsIgnoreCase(claveEstatus)){
			return "LISTO PARA PAGO";
		}
		if ("A4".equalsIgnoreCase(claveEstatus)){
			return "PAGADO";
		}
		if ("A5".equalsIgnoreCase(claveEstatus)){
			return "SERV. SIN RECIBO Y NO FACTURA";
		}
	   return "";	
	}


	
	
	private void descargarXML(Connection conEmpresa, String esquema, String nombreRepositorio, String uuidTXT) {
		DescargaMasiva descargaMasivaBean = new DescargaMasiva();
		String jsonRespuesta = null;
		String baseData64 = null;
		BovedaBean bovedaBean = new BovedaBean();
		Comprobante _comprobante = null;
		BovedaEmitidosBean bovedaEmitidosBean = new BovedaEmitidosBean();
		
		try {
			
			AccesoBean acceesoBean = new AccesoBean();
			EmpresasForm empresaForm = acceesoBean.consultaEmpresaEsquema(nombreRepositorio);
			
			
			String rutaPublicHTML = UtilsPATH.RUTA_PUBLIC_HTML + nombreRepositorio + File.separator;
			long nombreXML = System.currentTimeMillis();
			String apiKey = UtilsPATH.API_KEY_TIMBRADO_DESCARGA;
			String rfcSolicitante = consultaRFC("");
			// String rfcSolicitante = "PLA720201746";
			jsonRespuesta = descargaMasivaBean.recuperaCFDI(rfcSolicitante, apiKey, uuidTXT);
			Integer arrResultado [] = {0,0,0,0,0};
			if (jsonRespuesta != null) {
				org.json.JSONObject jsonArray   = new org.json.JSONObject(jsonRespuesta);
				if (jsonArray.get("data").toString() != null) {
					baseData64 = jsonArray.get("data").toString();
					if (baseData64 == null || "null".equalsIgnoreCase(baseData64)) {
						logger.info("No encontrado UUID===>"+uuidTXT );
					}else {
						String rutaXML = rutaPublicHTML + nombreXML +".xml";
						// logger.info("rutaXML===>"+rutaXML );
						File fileXML = new File(rutaXML);
						descargaMasivaBean.generarXML(rutaXML, baseData64);
						 
						_comprobante = LeerXML.ObtenerComprobante(fileXML.getAbsolutePath());
						/*if ("N".equalsIgnoreCase(_comprobante.getTipoDeComprobante())) {
							ArrayList<File> listaNomina = new ArrayList<File>();
							listaNomina.add(new File(rutaXML));
							bovedaNomina.procesarXmlBoveda(conEmpresa, esquema, empresaForm.getEsquema(), listaNomina, arrResultado, usuarioHTTP);
						}else */ if (empresaForm.getRfc().equalsIgnoreCase(_comprobante.getReceptor().getRfc())){ // es recibido
							bovedaBean.procesarXmlBoveda(conEmpresa, esquema, empresaForm.getEsquema(), fileXML, arrResultado, true);	
						}else {
							bovedaEmitidosBean.procesarXmlBoveda(conEmpresa, esquema, empresaForm.getEsquema(), fileXML, arrResultado, usuarioHTTP, true);	
						}
						
					}
				}
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}

	
	public ArrayList<ProveedoresForm> comboProveedores(Connection con, String esquema, String idLengueje) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ProveedoresForm proveedoresForm = new ProveedoresForm();
        ArrayList<ProveedoresForm> listaProveedores = new ArrayList<>();
        LenguajeBean lenguajeBean = LenguajeBean.instance();
        try{
        	String msgSeleccione = null;
        	HashMap<String, String> mapaLenguaje = lenguajeBean.obtenerEtiquetas(idLengueje, "PAN_MSG_GENERALES");
        	if ("".equalsIgnoreCase(Utils.noNulo(mapaLenguaje.get("MSG_SELECCION_PROVEE")))) {
				msgSeleccione = "Seleccione un Proveedor";
			}else {
				msgSeleccione = mapaLenguaje.get("MSG_SELECCION_PROVEE");
			}
        	proveedoresForm.setRfc("");
	        proveedoresForm.setRazonSocial(msgSeleccione);
	        listaProveedores.add(proveedoresForm);
	        proveedoresForm = new ProveedoresForm();
	        
        	
        	StringBuffer sbQuery = new StringBuffer(ExportarXMLQuerys.getComboProveedores(esquema));
            stmt = con.prepareStatement(sbQuery.toString());
            rs = stmt.executeQuery();
	        
	        while (rs.next()){
	        	
		        proveedoresForm.setRfc(Utils.noNuloNormal(rs.getString(1)));
		        proveedoresForm.setRazonSocial(proveedoresForm.getRfc() + " - " + Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(2))));
		        listaProveedores.add(proveedoresForm);
		        proveedoresForm = new ProveedoresForm();
	        }
	        
	        
        } catch(Exception e){
        	Utils.imprimeLog("comboProveedores(): ", e);
        } finally{
        	try{
   	        	 if(rs != null) {
   	                rs.close();
   	            }
   	            rs = null;
   	            if(stmt != null) {
   	                stmt.close();
   	            }
   	            stmt = null;
        	}
        	catch(Exception e){
        		rs = null;
        		stmt = null;
        	}
        }
        return listaProveedores;
    }
	

}