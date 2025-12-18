package com.siarex247.procesos.vincularBoveda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaAction;
import com.siarex247.cumplimientoFiscal.Boveda.BovedaAction.FiltrosEntrada;
import com.siarex247.cumplimientoFiscal.DescargaSAT.ProcesoDescargaSATQuerys;
import com.siarex247.cumplimientoFiscal.ListaNegra.ProcesoArchivosQuerys;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsFechas;

public class VincularBovedaBean {

	public static final Logger logger = Logger.getLogger("siarex");

	
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
					
					try{
						if (con != null){
							con.close();
						}
					}catch(Exception e){
						con = null;
					}
					
					for (int y = 0; y < listaEmpresas.size(); y++){
						empresasForm = listaEmpresas.get(y);
						// logger.info("Buscando Correos de la empresa : "+empresasForm.getEmailDominio());
						if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
							try {
								// logger.info("Validacion XML de la empresa====>"+empresasForm.getEsquema());
								if ("toyota".equalsIgnoreCase(empresasForm.getEsquema()) || "tmmbcservicios".equalsIgnoreCase(empresasForm.getEsquema())) {
									
								}else {
									iniciaProcesoVincular(empresasForm);	
								}
								
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
	
	

 public void iniciaProcesoVincular(EmpresasForm empresasForm) {
	 ConexionDB connPool = null;
     ResultadoConexion rcEmpresa = null;
     Connection conEmpresa = null;
     try{
    	 connPool= new ConexionDB();
    	 rcEmpresa = connPool.getConnection(empresasForm.getEsquema());
    	 conEmpresa = rcEmpresa.getCon();
    	 
    	
    	 String fechaProceso = getFechaHoy(conEmpresa, rcEmpresa.getEsquema());  
         //
		String diaMes = fechaProceso.substring(8); 
		boolean isEjecuto = isEjecuto(conEmpresa, rcEmpresa.getEsquema(), fechaProceso);
		
		float fechaHora = Float.parseFloat(UtilsFechas.getFechaActualNumero().substring(8));
		
		
		if (isEjecuto) {
			// logger.info("Proceso ya ejecutado del dia..."+fechaHora);
		}else {
			 if (fechaHora >= 050000) {
				grabarProceso(conEmpresa, rcEmpresa.getEsquema(), 0, diaMes); // se grabar en bitacora
				
				conEmpresa.close();
				conEmpresa = null;
				
				BovedaAction bovedaAction = new BovedaAction();
				long IDEN = System.currentTimeMillis();
				FiltrosEntrada filtros = new FiltrosEntrada();
				filtros.dateOperator = "bt";
				filtros.dateV1 = "2010-01-01";
				filtros.dateV2 = UtilsFechas.getFechayyyyMMdd();
				bovedaAction.procesoVincularBoveda(empresasForm.getEsquema(), IDEN, "", "admin.procesos@siarex.com", "MX", filtros);
			 }
		}
			
     }catch(Exception e) {
		Utils.imprimeLog("", e);
	 }finally {
		try {
			if (conEmpresa != null) {
				conEmpresa.close();
			}
			conEmpresa = null;
		}catch(Exception e) {
			conEmpresa = null;
		}
	}
  }
 
 public String getFechaHoy(Connection con, String esquema) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String fechaHoy = null;
		try {
			fechaHoy = Utils.getFechayyyyMMdd();
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				rs = null;
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return fechaHoy ;
	}
 
 private boolean isEjecuto(Connection con, String esquema, String fecha){
     PreparedStatement stmt = null;
     ResultSet rs = null;

     try{
         stmt = con.prepareStatement( ProcesoDescargaSATQuerys.getBuscarRespaldo(esquema));
         stmt.setString(1, "VCB"); // Vincular Complementos Boveda
         stmt.setString(2, fecha);
         rs = stmt.executeQuery();
			if(rs.next()){
				return true;
         }
			return false;
     }
     catch(Exception e){
     	Utils.imprimeLog("isEjecuto(): ", e);
     }
     finally{
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
	            stmt = null;
	        }
     }
     return false;
  }
 
 private void grabarProceso(Connection con, String esquema, int totEnvios, String tipoEnvio)
 {
     PreparedStatement stmt = null;
     try{
         stmt = con.prepareStatement( ProcesoArchivosQuerys.getGrabarProceso(esquema));
         stmt.setString(1, "VCB");
         stmt.setString(2, tipoEnvio);
         stmt.setInt(3, totEnvios);
         stmt.setString(4, "OK");
         
         logger.info("stmt===>"+stmt);
         stmt.executeUpdate();
     }catch(Exception e){
     	Utils.imprimeLog("", e);
     }finally{
	        try{
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }catch(Exception e){
	            stmt = null;
	        }
     }
 }
 
}
