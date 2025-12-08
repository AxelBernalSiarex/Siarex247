package com.siarex247.utils;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;

public class DepuradorArchivos {

	public static final Logger logger = Logger.getLogger("siarex247");

	
	public void monitorDepurador(){
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
								eliminarDirectorioTEMP(empresasForm);
								eliminarDirectorioExportar(empresasForm);
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
	
	
	public void eliminarDirectorioTEMP(EmpresasForm empresasForm) {
		try {
			 Date horaActual = new Date();
			//String dirArchivoIncial = "C:\\Tomcat9\\siarex247\\public_html\\"+empresasForm.getEsquema()+"\\TEMP_PDF\\";
			 String dirArchivoIncial = "/home/siarex247/"+empresasForm.getEsquema()+"//TEMP_PDF//";
			 String dirArchivoMETADATA = "/home/siarex247/"+empresasForm.getEsquema()+"//EXPORTAR_METADATA//";
			 String dirArchivoCFDI_TIMBRADO = "/home/siarex247/"+empresasForm.getEsquema()+"//DESCARGA_SAT//CFDI_TIMBRADO//";
			 String dirArchivoCFDI_TempDir = "/home/tempDir/";
			 
			 String dirFilesApp = "/opt/tomcat11/webapps/siarex247/files/";
			 
			// logger.info("dirArchivoIncial===>"+dirArchivoIncial);
			File fileDir = new File(dirArchivoIncial);
			String listaArchivos [] = fileDir.list();
			
			File fileDirMetadata = new File(dirArchivoMETADATA);
			String listaArchivosMeTadata [] = fileDirMetadata.list();
			
			
			File fileDirTimbrado = new File(dirArchivoCFDI_TIMBRADO);
			String listaArchivosTimbrado [] = fileDirTimbrado.list();
			
			File fileDirTempDir = new File(dirArchivoCFDI_TempDir);
			String listaArchivosTempDir [] = fileDirTempDir.list();
			
			
			
			
			File fileDirApp = new File(dirFilesApp);
			String listaArchivosApp [] = fileDirApp.list();
			
			
			File fileElimina = null;
			
			String pattern = "yyyy-MM-dd H:m:s";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			
			String diaFinal = simpleDateFormat.format(horaActual);
			String diaInicial = null; 
			Integer[] datosTiempo = null;
			for (int x = 0; x < listaArchivos.length; x++) {
				fileElimina = new File(dirArchivoIncial + listaArchivos[x]);
			 	long lastModified = fileElimina.lastModified();
				Date lastModifiedDate = new Date( lastModified );
			 	diaInicial = simpleDateFormat.format(lastModifiedDate);
			 	datosTiempo = calcularHoras(diaInicial, diaFinal);
				if (datosTiempo[0] >= 1) {
					fileElimina.delete();
				}
			}
			
			for (int x = 0; x < listaArchivosMeTadata.length; x++) {
				fileElimina = new File(dirArchivoMETADATA + listaArchivosMeTadata[x]);
			 	long lastModified = fileElimina.lastModified();
				Date lastModifiedDate = new Date( lastModified );
			 	diaInicial = simpleDateFormat.format(lastModifiedDate);
			 	datosTiempo = calcularHoras(diaInicial, diaFinal);
				if (datosTiempo[0] >= 7) {
					fileElimina.delete();
				}
			}
			
			for (int x = 0; x < listaArchivosTimbrado.length; x++) {
				fileElimina = new File(dirArchivoCFDI_TIMBRADO + listaArchivosTimbrado[x]);
			 	long lastModified = fileElimina.lastModified();
				Date lastModifiedDate = new Date( lastModified );
			 	diaInicial = simpleDateFormat.format(lastModifiedDate);
			 	datosTiempo = calcularHoras(diaInicial, diaFinal);
				if (datosTiempo[0] >= 7) {
					fileElimina.delete();
				}
			}
			
			
			
			for (int x = 0; x < listaArchivosTempDir.length; x++) {
				fileElimina = new File(dirArchivoCFDI_TempDir + listaArchivosTempDir[x]);
			 	long lastModified = fileElimina.lastModified();
				Date lastModifiedDate = new Date( lastModified );
			 	diaInicial = simpleDateFormat.format(lastModifiedDate);
			 	datosTiempo = calcularHoras(diaInicial, diaFinal);
				if (datosTiempo[0] >= 1) {
					fileElimina.delete();
				}
			}
			
			
			
			for (int x = 0; x < listaArchivosApp.length; x++) {
				fileElimina = new File(dirFilesApp+ listaArchivosApp[x]);
			 	long lastModified = fileElimina.lastModified();
				Date lastModifiedDate = new Date( lastModified );
			 	diaInicial = simpleDateFormat.format(lastModifiedDate);
			 	datosTiempo = calcularHoras(diaInicial, diaFinal);
				if (datosTiempo[2] >= 30) {
					fileElimina.delete();
				}
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	public void eliminarDirectorioExportar(EmpresasForm empresasForm) {
		try {
			// logger.info("Eliminando directorio de exportar...");
			Date horaActual = new Date();
			 // String dirArchivoIncial = "C:\\Tomcat9\\REPOSITORIO_DOCUMENTOS\\SIAREX\\REPOSITORIOS\\"+empresasForm.getEsquema()+"\\EXPORTAR\\";
			 String dirArchivoIncial = "/home/REPOSITORIO_DOCUMENTOS/SIAREX/REPOSITORIOS/"+empresasForm.getEsquema()+"/EXPORTAR//";
			File fileDir = new File(dirArchivoIncial);
			String listaArchivos [] = fileDir.list();
			File fileElimina = null;
			
			String pattern = "yyyy-MM-dd H:m:s";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			
			String diaFinal = simpleDateFormat.format(horaActual);
			String diaInicial = null; 
			Integer[] datosTiempo = null;
			for (int x = 0; x < listaArchivos.length; x++) {
				fileElimina = new File(dirArchivoIncial + listaArchivos[x]);
			 	long lastModified = fileElimina.lastModified();
				Date lastModifiedDate = new Date( lastModified );
			 	diaInicial = simpleDateFormat.format(lastModifiedDate);
			 	datosTiempo = calcularHoras(diaInicial, diaFinal);
				if (datosTiempo[0] >= 1) {
					if (fileElimina.isDirectory()) {
						File fileDirecorio = new File(fileElimina.getAbsolutePath());
			            FileUtils.deleteDirectory(fileDirecorio);
					}else {
						fileElimina.delete();
					}
					
				}
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
	}
	
	public Integer[] calcularHoras(String diaInicial, String diaFinal){
		Integer[] datosTiempo = {0,0,0};
		try {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
	        
	        Date fechaInicial=dateFormat.parse(diaInicial);
	        Date fechaFinal=dateFormat.parse(diaFinal);

	        
	        int diferencia=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/1000);
	 
	        int dias=0;
	        int horas=0;
	        int minutos=0;
	        if(diferencia>86400) {
	            dias=(int)Math.floor(diferencia/86400);
	            diferencia=diferencia-(dias*86400);
	        }
	        if(diferencia>3600) {
	            horas=(int)Math.floor(diferencia/3600);
	            diferencia=diferencia-(horas*3600);
	        }
	        if(diferencia>60) {
	            minutos=(int)Math.floor(diferencia/60);
	            diferencia=diferencia-(minutos*60);
	        }
	        
	        datosTiempo[0] = dias;
	        datosTiempo[1] = horas;
	        datosTiempo[2] = minutos;
	        
	       // System.out.println("Hay "+dias+" dias, "+horas+" horas, "+minutos+" minutos y "+diferencia+" segundos de diferencia");
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return datosTiempo;
    }
	
	
	
}
