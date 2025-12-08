package com.siarex247.cumplimientoFiscal.ListaNegra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Empleados.EmpleadosBean;
import com.siarex247.catalogos.Empleados.EmpleadosForm;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsHTML;

public class EnviarCorreoListaBean {

	public static final Logger logger = Logger.getLogger("siarex");
	
	private final String DISPONIBLE_ = "DISPONIBLE_";
		

	public void iniciarProceoCorreos(String fechaActual) {
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		AccesoBean accesoBean = new AccesoBean();
		try {
			rc = connPool.getConnectionSiarex();
			con = rc.getCon();
			
			ArrayList<EmpresasForm> listaEmpresas = accesoBean.listaEmpresas(con, rc.getEsquema());   //  detalleEmpresas(con, "siarex");
			EmpresasForm empresasForm = null;
			
			for (int y = 0; y < listaEmpresas.size(); y++){
				empresasForm = listaEmpresas.get(y);
				if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
					try {
						enviarNotificacion(empresasForm, fechaActual);
					} catch (Exception e) {
						Utils.imprimeLog("", e);
					}
				}
			}
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
	}
	
	
	private void enviarNotificacion(EmpresasForm empresaForm, String fechaActual) {
		ResultadoConexion rc = null;
		ConexionDB connPool = new ConexionDB();
		Connection con = null;
		String esquema = null;
		EmpleadosBean empleadosBean = new EmpleadosBean();
		try {
			rc = connPool.getConnection(empresaForm.getEsquema());
			con = rc.getCon();
			esquema = rc.getEsquema();
			
			int annioActual = Integer.parseInt(fechaActual.substring(0, 4));
			int annioAnterior = annioActual - 1; 
			
			HashMap<String, JSONObject> mapaResultado = obtenerEstatusActual(empresaForm.getEsquema(), annioActual, annioActual);
			obtenerEstatusHistorico(empresaForm.getEsquema(), mapaResultado, 2015, annioAnterior);
			
			Collection<JSONObject> valMapa =  mapaResultado.values();
			Iterator<JSONObject> iteMapa = valMapa.iterator();
			
			String CORREO_LISTA_NEGRA_SAT_1 = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "CORREO_LISTA_NEGRA_SAT_1");
			String CORREO_LISTA_NEGRA_SAT_2 = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "CORREO_LISTA_NEGRA_SAT_2");
			String CORREO_LISTA_NEGRA_SAT_3 = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "CORREO_LISTA_NEGRA_SAT_3");
			String CORREO_LISTA_NEGRA_SAT_4 = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "CORREO_LISTA_NEGRA_SAT_4");
			String CORREO_LISTA_NEGRA_SAT_5 = ConfigAdicionalesBean.obtenerValorVariable(con, esquema, "CORREO_LISTA_NEGRA_SAT_5");

			
			String mensajeCorreo = UtilsHTML.generaHTMLListaNegra(iteMapa, mapaResultado.size(), empresaForm.getNombreLargo());
			
			if (!"".equalsIgnoreCase(CORREO_LISTA_NEGRA_SAT_1)) {
				
				EmpleadosForm empleadosForm = empleadosBean.consultaEmpleadoXCorreo(con, esquema, CORREO_LISTA_NEGRA_SAT_1);
				String mensajeCorreoFinal = mensajeCorreo.replaceAll("&&NOMBRE_EMPLEADO&&", empleadosForm.getNombreCompleto());
				
				String emailTO [] = {CORREO_LISTA_NEGRA_SAT_1};
				// CorreoBean.enviarCorreo(null, mensajeCorreoFinal, false, emailTO, null, "Status de Proveedores en Lista Negra SAT (PROBLEMA)", usuarioEmisorCorreo, pwdEmisorCorreo);
				// logger.info("mensajeCorreo (1)=====>"+mensajeCorreoFinal);
				EnviaCorreoGrid.enviarCorreo(null, mensajeCorreoFinal, false, emailTO, null, "Siarex - Status de Proveedores en Lista Negra SAT (PROBLEMA)", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
			}
			
			if (!"".equalsIgnoreCase(CORREO_LISTA_NEGRA_SAT_2)) {
				EmpleadosForm empleadosForm = empleadosBean.consultaEmpleadoXCorreo(con, esquema, CORREO_LISTA_NEGRA_SAT_2);
				String mensajeCorreoFinal = mensajeCorreo.replaceAll("&&NOMBRE_EMPLEADO&&", empleadosForm.getNombreCompleto()); 
				
				String emailTO [] = {CORREO_LISTA_NEGRA_SAT_2};
				EnviaCorreoGrid.enviarCorreo(null, mensajeCorreoFinal, false, emailTO, null, "Siarex - Status de Proveedores en Lista Negra SAT (PROBLEMA)", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
				logger.info("mensajeCorreo (2)=====>"+mensajeCorreoFinal);
			}
			if (!"".equalsIgnoreCase(CORREO_LISTA_NEGRA_SAT_3)) {
				EmpleadosForm empleadosForm = empleadosBean.consultaEmpleadoXCorreo(con, esquema, CORREO_LISTA_NEGRA_SAT_3);
				String mensajeCorreoFinal = mensajeCorreo.replaceAll("&&NOMBRE_EMPLEADO&&", empleadosForm.getNombreCompleto());
				
				String emailTO [] = {CORREO_LISTA_NEGRA_SAT_3};
				EnviaCorreoGrid.enviarCorreo(null, mensajeCorreoFinal, false, emailTO, null, "Siarex - Status de Proveedores en Lista Negra SAT (PROBLEMA)", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
				logger.info("mensajeCorreo (3)=====>"+mensajeCorreoFinal);
			}
			if (!"".equalsIgnoreCase(CORREO_LISTA_NEGRA_SAT_4)) {
				EmpleadosForm empleadosForm = empleadosBean.consultaEmpleadoXCorreo(con, esquema, CORREO_LISTA_NEGRA_SAT_4);
				String mensajeCorreoFinal = mensajeCorreo.replaceAll("&&NOMBRE_EMPLEADO&&", empleadosForm.getNombreCompleto());
				
				String emailTO [] = {CORREO_LISTA_NEGRA_SAT_4};
				EnviaCorreoGrid.enviarCorreo(null, mensajeCorreoFinal, false, emailTO, null, "Siarex - Status de Proveedores en Lista Negra SAT (PROBLEMA)", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
				logger.info("mensajeCorreo (4)=====>"+mensajeCorreoFinal);
			}
			if (!"".equalsIgnoreCase(CORREO_LISTA_NEGRA_SAT_5)) {
				EmpleadosForm empleadosForm = empleadosBean.consultaEmpleadoXCorreo(con, esquema, CORREO_LISTA_NEGRA_SAT_5);
				String mensajeCorreoFinal = mensajeCorreo.replaceAll("&&NOMBRE_EMPLEADO&&", empleadosForm.getNombreCompleto());
				
				String emailTO [] = {CORREO_LISTA_NEGRA_SAT_5};
				EnviaCorreoGrid.enviarCorreo(null, mensajeCorreoFinal, false, emailTO, null, "Siarex - Status de Proveedores en Lista Negra SAT (PROBLEMA)", empresaForm.getEmailDominio(), empresaForm.getPwdCorreo());
				logger.info("mensajeCorreo (5)=====>"+mensajeCorreoFinal);
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				con = null;
			} catch (Exception e2) {
				con = null;
			}
		}
	}
	
	
	
	
	public HashMap<String, JSONObject> obtenerEstatusActual(String esquemaEmpresa, int anioListaInicial, int anioListaFinal){
        ConexionDB connPool = new ConexionDB();
        ResultadoConexion rcLista = null;
        ResultadoConexion rcEmpresa = null;
        
        Connection conLista = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        HashMap<String, JSONObject> mapaResultado = new HashMap<>();
        StringBuffer sbQuery = new StringBuffer();
        StringBuffer sbQueryFinal = new StringBuffer();
        
        ListaNegraBean listaNegraBean = new ListaNegraBean();
        
        try{ // 
        	rcEmpresa = connPool.getConnection(esquemaEmpresa);
        	rcLista = connPool.getConnectionListaNegra();
        	conLista = rcLista.getCon();
        	
        	ArrayList<String> columnasFechas = listaNegraBean.consultaColumnas( anioListaInicial);
        	sbQuery.append("select B.RFC, B.RAZON_SOCIAL").append(columnasFechas.size() > 0 ? ", " : " ");
        	
        	int contColumnas = 1;
        	int numColActual = 0;
        	for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(DISPONIBLE_+contColumnas).append(",");
        		numColActual++;
        	}
        	
        	sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(rcLista.getEsquema()).append(".LISTA_NEGRA_SAT A inner join ").append(rcEmpresa.getEsquema()).append(".PROVEEDORES B on A.RFC = B.RFC ");
        	
        	int contParam = 1;
        	
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? ");
        	
        	stmt = conLista.prepareStatement( sbQueryFinal.toString());
        	
    		stmt.setString(contParam, anioListaInicial+"-01-01 01:01:01");
    		contParam++;
    		stmt.setString(contParam, anioListaFinal+"-12-31 23:59:59");
    		contParam++;

            // logger.info("Query----->"+stmt );
            rs = stmt.executeQuery();
            String RFC = null;
            String DISPONIBLE_ACTUAL = null;
            String desEstatus = null;
            String desEstatusAnterior = null;
			while(rs.next()){
				desEstatus = null;
				desEstatusAnterior = "";
				
					RFC = Utils.noNulo(rs.getString(1));
					jsonobj.put("RFC",RFC);
					jsonobj.put("RAZON_SOCIAL",Utils.noNuloNormal(rs.getString(2)));
					DISPONIBLE_ACTUAL = Utils.noNulo(rs.getString(numColActual)); // se obtiene la columna del disponinle actual
					if ("0".equalsIgnoreCase(DISPONIBLE_ACTUAL)) {
						desEstatus = "Correcto";
					}else {
						desEstatus = "Proveedor Encontrado en Lista Negra";
					}
					
					for (int x = 3; x < numColActual-1; x++) {
						DISPONIBLE_ACTUAL = Utils.noNulo(rs.getString(x));
						if ("1".equalsIgnoreCase(DISPONIBLE_ACTUAL)) {
							desEstatusAnterior = "Proveedor Encontrado en Lista Negra";
							break;
						}else {
							desEstatusAnterior = "";
						}
					}
					jsonobj.put("ESTATUS_ACTUAL",desEstatus);
					jsonobj.put("ESTATUS_ANTERIOR",desEstatusAnterior);
					
					mapaResultado.put(RFC, jsonobj);
					jsonobj = new JSONObject();	
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
	            if(conLista != null)
	            	conLista.close();
	            conLista = null;
	            if(rcEmpresa.getCon() != null)
	            	rcEmpresa.getCon().close();
	            rcEmpresa = null;
	            
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return mapaResultado;
    }
	
	public HashMap<String, JSONObject> obtenerEstatusHistorico(String esquemaEmpresa, HashMap<String, JSONObject> mapaResultado, int anioListaInicial, int anioListaFinal){
		ConexionDB connPool = new ConexionDB();
		ResultadoConexion rcLista = null;
        ResultadoConexion rcEmpresa = null;
        Connection conLista = null;
        
		PreparedStatement stmt = null;
        ResultSet rs = null;
        JSONObject jsonobj = new JSONObject();
        
        
        StringBuffer sbQuery = new StringBuffer();
        StringBuffer sbQueryFinal = new StringBuffer();
        
        ListaNegraBean listaNegraBean = new ListaNegraBean();
        
        try{ // 
        	
        	rcEmpresa = connPool.getConnection(esquemaEmpresa);
        	rcLista = connPool.getConnectionListaNegra();
        	conLista = rcLista.getCon();
        	
        	
        	ArrayList<String> columnasFechas = listaNegraBean.consultaColumnas(anioListaFinal);
        	sbQuery.append("select B.RFC, B.RAZON_SOCIAL, ");
        	
        	int contColumnas = 1;
        	int numColActual = 0;
        	for (int x = 0; x < columnasFechas.size(); x++) {
        		sbQuery.append(DISPONIBLE_+contColumnas).append(",");
        		numColActual++;
        	}
        	sbQueryFinal.append(sbQuery.toString().substring(0, sbQuery.length() - 1)).append(" from ").append(rcLista.getEsquema()).append(".LISTA_NEGRA_SAT_HISTORICO A inner join ").append(rcEmpresa.getEsquema()).append(".PROVEEDORES B on A.RFC = B.RFC ");
        	
        	int contParam = 1;
        	
        	sbQueryFinal.append(" where FECHA_TRASACCION between ? and ? ");
        	
        	stmt = conLista.prepareStatement( sbQueryFinal.toString());
        	
    		stmt.setString(contParam, anioListaInicial+"-01-01 01:01:01");
    		contParam++;
    		stmt.setString(contParam, anioListaFinal+"-12-31 23:59:59");
    		contParam++;

           // logger.info("Query----->"+stmt );
            rs = stmt.executeQuery();
            String RFC = null;
            String DISPONIBLE_ACTUAL = null;
            String VACIO = "";
            String desEstatusAnterior = null;
            boolean buscarEncontradoRFC = false;
            boolean buscarNoEncontradoRFC = false;
			while(rs.next()){
				
				desEstatusAnterior = null;
				buscarEncontradoRFC = false;
				buscarNoEncontradoRFC = false;
				
					RFC = Utils.noNulo(rs.getString(1));
					if (mapaResultado.get(RFC) == null) {
						buscarNoEncontradoRFC = true;
						buscarEncontradoRFC = true;
						jsonobj = new JSONObject();
						jsonobj.put("RFC",RFC);
						jsonobj.put("RAZON_SOCIAL",Utils.noNuloNormal(rs.getString(2)));
						
					}else {
						jsonobj = mapaResultado.get(RFC);
						desEstatusAnterior = jsonobj.get("ESTATUS_ANTERIOR").toString();
						if ("".equals(desEstatusAnterior)) {
							buscarEncontradoRFC = true;
						}
					}
					
					if (buscarEncontradoRFC) {
						for (int x = 3; x < numColActual; x++) {
							DISPONIBLE_ACTUAL = Utils.noNulo(rs.getString(x));
							if ("1".equalsIgnoreCase(DISPONIBLE_ACTUAL)) {
								desEstatusAnterior = "Proveedor Encontrado en Lista Negra";
								break;
							}else {
								desEstatusAnterior = "";
							}
						}
						if (buscarNoEncontradoRFC) {
							jsonobj.put("ESTATUS_ACTUAL","Correcto");	
						}
						jsonobj.put("ESTATUS_ANTERIOR",desEstatusAnterior);
					}
					
					mapaResultado.put(RFC, jsonobj);
					
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
	            if(conLista != null)
	            	conLista.close();
	            conLista = null;
	            
	            if(rcEmpresa.getCon() != null)
	            	rcEmpresa.getCon().close();
	            rcEmpresa = null;
	        }catch(Exception e){
	            stmt = null;
	            rcEmpresa = null;
	        }
        }
        return mapaResultado;
    }
}
