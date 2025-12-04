package com.siarex247.cumplimientoFiscal.DescargaSAT;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFile;

public class ExtraccionTXT {

	public static final Logger logger = Logger.getLogger("siarex");
	PreparedStatement STMT_GUARDAR = null;
	
	public void iniciarExtraccionTXT(Connection con, String esquemaEmpresa, File fileDescarga, EmpresasForm empresaForm) {
		List<String> lineScan = null;
		// BovedaBean bovedaBean = new BovedaBean();
		// JSONObject jsonBoveda = null;
		String efectoComprobante = null;
		try {
			ArrayList<String> listaTXT = null;
			File[] fileMetadata = fileDescarga.listFiles();
            STMT_GUARDAR = con.prepareStatement( ProcesoDescargaSATQuerys.getGuardarRegistroMetadata(esquemaEmpresa));
            for (int f = 0;  f < fileMetadata.length; f++) {
				File fileTXT = fileMetadata[f];
				// logger.info("fileTXT====>"+fileTXT.getAbsolutePath());
				if (fileTXT.getAbsolutePath().endsWith(".txt")) {
					listaTXT = UtilsFile.leeArchivoTXT(fileTXT.getAbsolutePath());
					int numRow = 0;
					for (int x = 0; x < listaTXT.size(); x++) {
						ExtraccionForm extraForm = new ExtraccionForm();
						try {
							if (numRow > 0) {
								lineScan = UtilsFile.parseLineSAT(listaTXT.get(x));
								// logger.info("lineScan====>"+lineScan);
								// Utils.eliminarComillasCadena(cadena)
								// Utils.eliminarComas( Utils.eliminarComillasCadena( lineScan.get(5)));
								if (lineScan.size() >= 12) {
									efectoComprobante = Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(9)));
									if (!"N".equalsIgnoreCase(efectoComprobante) &&  empresaForm.getRfc().equalsIgnoreCase(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(3))))) {
										// logger.info("UUID Procesando==>"+lineScan.get(0));
										extraForm.setUuid(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(0))));
										extraForm.setRfcEmisor(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(1))));
										extraForm.setNombreEmisor(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(2))));
										extraForm.setRfcReceptor(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(3))));
										extraForm.setNombreReceptor(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(4))));
										extraForm.setRfcPac(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(5))));
										extraForm.setFechaEmision(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(6))));
										extraForm.setFechaCertificacion(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(7))));
										extraForm.setMonto( Utils.noNuloDouble(Utils.eliminarComas(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(8))))));
										extraForm.setEfectoComprobante(efectoComprobante);
										extraForm.setEstatus(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(10))));
										extraForm.setFechaCancelacion(Utils.eliminarComillasCadena(Utils.noNuloNormal(lineScan.get(11))));
										// jsonBoveda = bovedaBean.consultaBovedaUUID(con, esquemaEmpresa, extraForm.getUuid());
//										if (jsonBoveda.isEmpty()) {
											// extraForm.setExisteBoveda("N");
//										}else {
											// extraForm.setExisteBoveda("S");
//										}
										extraForm.setExisteBoveda("N");
										guardarRegistroMetadata(con, esquemaEmpresa, extraForm);
										
									}
								}
								numRow++;
							}else {
								numRow++;
							}
						}catch(Exception e) {
							logger.info("UUID con error====>"+Utils.noNuloNormal(lineScan.get(0)));
							Utils.imprimeLog("", e);
						}
					}
				}
				
			}
			

            // SE ELIMINAN LOS TXT y solo queda el .zip
            for (int f = 0;  f < fileMetadata.length; f++) {
				File fileTXT = fileMetadata[f];
				if (fileTXT.getAbsolutePath().endsWith(".txt")) {
					fileTXT.delete();	
				}
            }
            
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(STMT_GUARDAR != null) {
	                STMT_GUARDAR.close();
	            }
	            STMT_GUARDAR = null;
	        }
	        catch(Exception e){
	            STMT_GUARDAR = null;
	        }
        }	
	}
	
	
	public void guardarRegistroMetadata(Connection con, String esquemaEmpresa, ExtraccionForm extraForm) {
		
        try{
        	String fechaCancelacion = extraForm.getFechaCancelacion();
        	if ("".equalsIgnoreCase(fechaCancelacion)) {
        		fechaCancelacion = null;
        	}
            STMT_GUARDAR.setString(1, extraForm.getUuid());
            STMT_GUARDAR.setString(2, extraForm.getRfcEmisor());
            STMT_GUARDAR.setString(3, extraForm.getNombreEmisor());
            STMT_GUARDAR.setString(4, extraForm.getRfcReceptor());
            STMT_GUARDAR.setString(5, extraForm.getNombreReceptor());
            STMT_GUARDAR.setString(6, extraForm.getRfcPac());
            STMT_GUARDAR.setString(7, extraForm.getFechaEmision());
            STMT_GUARDAR.setString(8, extraForm.getFechaCertificacion());
            STMT_GUARDAR.setDouble(9, extraForm.getMonto());
            STMT_GUARDAR.setString(10, extraForm.getEfectoComprobante());
            STMT_GUARDAR.setString(11, extraForm.getEstatus());
            STMT_GUARDAR.setString(12, fechaCancelacion);
            STMT_GUARDAR.setString(13, extraForm.getExisteBoveda());
            
            STMT_GUARDAR.executeUpdate();
        }
        catch(Exception e){
        	logger.info("STMT_GUARDAR===>"+STMT_GUARDAR);
        	Utils.imprimeLog("grabarProceso(): ", e);
        }
        	
	}
	
	

	
}
