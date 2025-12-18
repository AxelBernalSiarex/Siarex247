package com.siarex247.estadisticas.reporteNomina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.Utils;

public class ProcesoReporteNomina {

	public static final Logger logger = Logger.getLogger("siarex247");
	final String usuarioHTTP = "procesos@siarex.com";
	
	private PreparedStatement stmtSelectSubsidioCausado = null;
	private PreparedStatement stmtUpdateSubsidioCausado = null;
	private PreparedStatement stmtTotalPercepciones = null;
	private PreparedStatement stmtTotalDeducciones = null;
	private PreparedStatement stmtTotalOtroPagos = null;
	
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
						if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
							try {
								if ("toyota".equalsIgnoreCase(empresasForm.getEsquema()) || "tmmbcservicios".equalsIgnoreCase(empresasForm.getEsquema())) {
									
								}else {
									logger.info("Inciando descarga timbrado de la empresa : "+empresasForm.getEsquema());
									iniciaProcesoDescarga(empresasForm);
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
	
	
	public void iniciaProcesoDescarga(EmpresasForm empresasForm) {
		 ConexionDB connPool = null;
	     ResultadoConexion rcEmpresa = null;
	     Connection conEmpresa = null;
	    		
		try {
			String esquemaEmpresa = empresasForm.getEsquema();
	        connPool = new ConexionDB();
	        rcEmpresa = connPool.getConnection(esquemaEmpresa);
	        conEmpresa = rcEmpresa.getCon();

	        String fechaMaxima = obtenerFechaMaxima(conEmpresa, esquemaEmpresa);
	        int totRegistro = gudarRegistrosNomina(conEmpresa, esquemaEmpresa, fechaMaxima);
	        // logger.info("totRegistro ===>"+totRegistro);
	        if (totRegistro > 0) {
	        	gudarPercepciones(conEmpresa, esquemaEmpresa, fechaMaxima); // se guardan las percepciones
	        	guardarDeducciones(conEmpresa, esquemaEmpresa, fechaMaxima);  // se guardan las deducciones
	        	guardarOtroPago(conEmpresa, esquemaEmpresa, fechaMaxima);  // se guardan los otros pagos
	        	updateMasivo(conEmpresa, esquemaEmpresa, fechaMaxima);
	        }
	        logger.info("Finalizo de procesar empresa ===>"+empresasForm.getEsquema());
	        
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (conEmpresa != null) {
					conEmpresa.close();
				}
				conEmpresa = null;
			} catch (Exception e2) {
				conEmpresa = null;
			}
		}
	}
	
	
	private int gudarRegistrosNomina(Connection con, String esquema, String fechaMaxima) {
		PreparedStatement stmt = null;
	    int totRegistros = 0;
		try {
			stmt = con.prepareStatement( ProcesoNominaQuerys.getGuardarRegistrosNomina(esquema));
			stmt.setString(1, fechaMaxima);
			totRegistros = stmt.executeUpdate();
            
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        } catch(Exception e){
	            stmt = null;
	        }
        }
		return totRegistros;
	}
	
	
	private int updateMasivo(Connection con, String esquema, String fechaMaxima) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totRegistros = 0;
		String uuid = null;
		try {
			// se preparan todos los prepareStatement que se van actualizar...
			prepararStament(con, esquema);
			
			stmt = con.prepareStatement( ProcesoNominaQuerys.getObtenerUUID(esquema));
			stmt.setString(1, fechaMaxima);
			rs = stmt.executeQuery();
			while (rs.next()){
				uuid = Utils.noNulo(rs.getString(1));
				gudarSubsidioCausado(con, esquema, uuid);
				calcularPercepciones(con, esquema, uuid);
				calcularDeducciones(con, esquema, uuid);
				calcularOtroPago(con, esquema, uuid);
            }
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null) {
	                rs.close();
	            }
	            rs = null;
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	            if(stmtSelectSubsidioCausado != null) {
	            	stmtSelectSubsidioCausado.close();
	            }
	            stmtSelectSubsidioCausado = null;
	            if(stmtUpdateSubsidioCausado != null) {
	            	stmtUpdateSubsidioCausado.close();
	            }
	            stmtUpdateSubsidioCausado = null;
	            if(stmtTotalPercepciones != null) {
	            	stmtTotalPercepciones.close();
	            }
	            stmtTotalPercepciones = null;
	            if(stmtTotalDeducciones != null) {
	            	stmtTotalDeducciones.close();
	            }
	            stmtTotalDeducciones = null;
	            if(stmtTotalOtroPagos != null) {
	            	stmtTotalOtroPagos.close();
	            }
	            stmtTotalOtroPagos = null;
	            
	        } catch(Exception e){
	            stmt = null;
	            stmtSelectSubsidioCausado = null;
	            stmtUpdateSubsidioCausado = null;
	        }
        }
		return totRegistros;
	}
	
	
	private int gudarSubsidioCausado(Connection con, String esquema, String uuid) {
		ResultSet rs = null;
	    int totRegistros = 0;
		try {
			stmtSelectSubsidioCausado.setString(1, uuid);
			rs = stmtSelectSubsidioCausado.executeQuery();
			if (rs.next()){
				double subsidioCausado = rs.getDouble(1); 
				stmtUpdateSubsidioCausado.setDouble(1, subsidioCausado);
				stmtUpdateSubsidioCausado.setString(2, uuid);
				stmtUpdateSubsidioCausado.executeUpdate();
				
            }
            
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null) {
	            	rs.close();
	            }
	            rs = null;
	        } catch(Exception e){
	        	rs = null;
	        }
        }
		return totRegistros;
	}
	
	
	private int calcularPercepciones(Connection con, String esquema, String uuid) {
		
		ResultSet rs = null;
		int totRegistros = 0;
		try {
			stmtTotalPercepciones.setString(1, uuid);
            rs = stmtTotalPercepciones.executeQuery();
            String tipoPercepcion = null;
			while (rs.next()){
				tipoPercepcion = Utils.noNulo(rs.getString(1));
				double importeGravado = rs.getDouble(2);
				double importeExento = rs.getDouble(3);
				updatePercepciones(con, esquema, uuid, tipoPercepcion, importeGravado, importeExento);
            }
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null) {
	            	rs.close();
	            }
	            rs = null;
	        } catch(Exception e){
	        	rs = null;
	        }
        }
		return totRegistros;
	}
	
	
	private void gudarPercepciones(Connection con, String esquema, String fechaMaxima) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement( ProcesoNominaQuerys.getGuardarPercepciones(esquema));
			stmt.setString(1, fechaMaxima);
			stmt.executeUpdate();
            
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        } catch(Exception e){
	            stmt = null;
	        }
        }
	}
	
	private int updatePercepciones(Connection con, String esquema, String uuid, String tipoPercepcion, double importeGravado, double importeExento) {
		PreparedStatement stmt = null;
		int totUpdate = 0;
		try {
			
			stmt = con.prepareStatement( queryPercepciones(tipoPercepcion, esquema));
			stmt.setDouble(1, importeGravado);
			stmt.setDouble(2, importeExento);
			stmt.setString(3, uuid);
			totUpdate = stmt.executeUpdate();
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return totUpdate;
	}
	
	
	private void guardarDeducciones(Connection con, String esquema, String fechaMaxima) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement( ProcesoNominaQuerys.getGuardarDeducciones(esquema));
			stmt.setString(1, fechaMaxima);
			stmt.executeUpdate();
            
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        } catch(Exception e){
	            stmt = null;
	        }
        }
	}
	
	
	private int calcularDeducciones(Connection con, String esquema, String uuid) {
		
		ResultSet rs = null;
		int totRegistros = 0;
		try {
			stmtTotalDeducciones.setString(1, uuid);
            rs = stmtTotalDeducciones.executeQuery();
            String tipoDeduccion = null;
			while (rs.next()){
				tipoDeduccion = Utils.noNulo(rs.getString(1));
				double importe = rs.getDouble(2);
				updateDeduccion(con, esquema, uuid, tipoDeduccion, importe);
            }
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null) {
	            	rs.close();
	            }
	            rs = null;
	        } catch(Exception e){
	        	rs = null;
	        }
        }
		return totRegistros;
	}
	
	
	private int updateDeduccion(Connection con, String esquema, String uuid, String tipoDeduccion, double importe) {
		PreparedStatement stmt = null;
		int totUpdate = 0;
		try {
			
			stmt = con.prepareStatement( queryDeducciones(tipoDeduccion, esquema));
			stmt.setDouble(1, importe);
			stmt.setString(2, uuid);
			totUpdate = stmt.executeUpdate();
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return totUpdate;
	}
	
	private void guardarOtroPago(Connection con, String esquema, String fechaMaxima) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement( ProcesoNominaQuerys.getGuardarOtroPago(esquema));
			stmt.setString(1, fechaMaxima);
			stmt.executeUpdate();
            
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        } catch(Exception e){
	            stmt = null;
	        }
        }
	}
	
	private int calcularOtroPago(Connection con, String esquema, String uuid) {
		
		ResultSet rs = null;
		int totRegistros = 0;
		try {
			
			stmtTotalOtroPagos.setString(1, uuid);
            rs = stmtTotalOtroPagos.executeQuery();
            String tipoOtroPago = null;
			while (rs.next()){
				tipoOtroPago = Utils.noNulo(rs.getString(1));
				double importe = rs.getDouble(2);
				updateOtroPago(con, esquema, uuid, tipoOtroPago, importe);
            }
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if(rs != null) {
	            	rs.close();
	            }
	            rs = null;
	        } catch(Exception e){
	        	rs = null;
	        }
        }
		return totRegistros;
	}
	
	
	private int updateOtroPago(Connection con, String esquema, String uuid, String tipoOtroPago, double importe) {
		PreparedStatement stmt = null;
		int totUpdate = 0;
		try {
			
			stmt = con.prepareStatement( queryOtroPago(tipoOtroPago, esquema));
			stmt.setDouble(1, importe);
			stmt.setString(2, uuid);
			totUpdate = stmt.executeUpdate();
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				stmt = null;
			}catch(Exception e) {
				stmt = null;
			}
		}
		return totUpdate;
	}
	
	 private String obtenerFechaMaxima(Connection con, String esquema){
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        String fechaMaxima = "";
	        try{
	            stmt = con.prepareStatement( ProcesoNominaQuerys.getObtenerFechaMaxima(esquema));
	            rs = stmt.executeQuery();
				if(rs.next()){
					fechaMaxima = Utils.noNulo(rs.getString(1));
	            }
				
				if ("".equalsIgnoreCase(fechaMaxima)) {
					fechaMaxima = "2023-10-11 01:01:01";
				}
				
	        }
	        catch(Exception e){
	        	Utils.imprimeLog("", e);
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
	        return fechaMaxima;
	    }
	
	 
	 private void prepararStament(Connection con, String esquema) {
			try {
				// se preparan todos los prepareStatement que se van actualizar...
				stmtSelectSubsidioCausado = con.prepareStatement( ProcesoNominaQuerys.getObtenerSubsidioCausado(esquema));
				stmtUpdateSubsidioCausado = con.prepareStatement( ProcesoNominaQuerys.getUpdateSubsidioCausado(esquema));
				stmtTotalPercepciones = con.prepareStatement( ProcesoNominaQuerys.getObtenerPercepcionesUUID(esquema));
				stmtTotalDeducciones = con.prepareStatement( ProcesoNominaQuerys.getObtenerDeduccionesUUID(esquema));
				stmtTotalOtroPagos = con.prepareStatement( ProcesoNominaQuerys.getObtenerOtroPagoUUID(esquema));
				
			} catch (Exception e) {
				Utils.imprimeLog("", e);
			}
	}
	 
	 
	 private String queryPercepciones(String tipoPercepcion, String esquema) {
		 StringBuffer sbQuery = new StringBuffer();
		 String queryFinal = null;
		 try {
			 String campoGravado = "PERCEPCION_GRAVADO_"+tipoPercepcion;
			 String campoExcento = "PERCEPCION_EXCENTO_"+tipoPercepcion;
			 // update REPORTE_NOMINA set <<PERCEPCION_GRAVADO>> = ?, <<PERCEPCION_EXCENTO>> = ? where UUID = ?
			 sbQuery.append(ProcesoNominaQuerys.getUpdatePercepcionesUUID(esquema));
			 
			 queryFinal = sbQuery.toString().replaceAll("<<PERCEPCION_GRAVADO>>", campoGravado)
					 							   .replaceAll("<<PERCEPCION_EXCENTO>>", campoExcento);
			 
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		 return queryFinal;
	 }
	 
	 
	 private String queryDeducciones(String tipoDeduccion, String esquema) {
		 StringBuffer sbQuery = new StringBuffer();
		 String queryFinal = null;
		 try {
			 String campoDeduccion = "DEDUCCIONES_"+tipoDeduccion;
			 
			 // update REPORTE_NOMINA set <<PERCEPCION_GRAVADO>> = ?, <<PERCEPCION_EXCENTO>> = ? where UUID = ?
			 sbQuery.append(ProcesoNominaQuerys.getUpdateDeduccionesUUID(esquema));
			 
			 queryFinal = sbQuery.toString().replaceAll("<<DEDUCCIONES>>", campoDeduccion);
			 
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		 return queryFinal;
	 }
	 
	 private String queryOtroPago(String tipoOtroPago, String esquema) {
		 StringBuffer sbQuery = new StringBuffer();
		 String queryFinal = null;
		 try {
			 String campoDeduccion = "OTRO_PAGO_"+tipoOtroPago;
			 
			 // update REPORTE_NOMINA set <<PERCEPCION_GRAVADO>> = ?, <<PERCEPCION_EXCENTO>> = ? where UUID = ?
			 sbQuery.append(ProcesoNominaQuerys.getUpdateOtroPagoUUID(esquema));
			 
			 queryFinal = sbQuery.toString().replaceAll("<<OTRO_PAGO>>", campoDeduccion);
			 
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		 return queryFinal;
	 }
}
