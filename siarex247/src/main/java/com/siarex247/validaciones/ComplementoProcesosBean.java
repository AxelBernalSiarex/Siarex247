package com.siarex247.validaciones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.configSistema.AlertaComplemento.AlertaComplementoBean;
import com.siarex247.configSistema.AlertaComplemento.AlertaComplementoForm;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.visor.VisorOrdenes.ComplementosForm;

public class ComplementoProcesosBean {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public void monitoreaComplemento(int diaProceso){
		try{
				AccesoBean accesoBean = new AccesoBean();
				ConexionDB connPool = new ConexionDB();
				Connection con = null;
				ResultadoConexion rc = null;
				
				try{
					rc = connPool.getConnectionSiarex();
					con = rc.getCon();
					ArrayList<EmpresasForm> listaEmpresas = accesoBean.listaEmpresas(con, rc.getEsquema());
					EmpresasForm empresasForm = null;
					for (int y = 0; y < listaEmpresas.size(); y++){
						empresasForm = listaEmpresas.get(y);
						if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
							try {
								if ("toyota".equalsIgnoreCase(empresasForm.getEsquema()) || "tmmbcservicios".equalsIgnoreCase(empresasForm.getEsquema())) {
									
								}else {
									iniciarProcesoComplemento(empresasForm, diaProceso);	
									
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
	
	

	 public void iniciarProcesoComplemento(EmpresasForm empresaForm, int diaProceso) {
		 ConexionDB connPool = null;
	     ResultadoConexion rcEmpresa = null;
	     Connection conEmpresa = null;
	     AlertaComplementoBean confBean = new AlertaComplementoBean();
	     boolean ejecutarProceso = false;
	     try{
	    	 connPool= new ConexionDB();
	    	 rcEmpresa = connPool.getConnection(empresaForm.getEsquema());
	    	 conEmpresa = rcEmpresa.getCon();
	    	 
	    	 AlertaComplementoForm configForm = confBean.buscarConfProceso(conEmpresa, rcEmpresa.getEsquema(), "PRO02");
	    	   
	    	 if ("S".equalsIgnoreCase(configForm.getActivar())) { // si esta activo, se ejecuta el proceso
	    		 ejecutarProceso = true;
	    	 }
	    	 
	    	 if (ejecutarProceso) {
	    		 notificarComplemento(empresaForm, diaProceso);
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
	 
	 
	private void notificarComplemento(EmpresasForm empresaForm, int diaProceso) {
		
		ConexionDB connPool = null;
        ResultadoConexion rcEmpresa = null;
        Connection conEmpresa = null;
        AlertaComplementoBean confBean = new AlertaComplementoBean();
		try {
			Date fechaActual = new Date();
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			String horaActual = formatTime.format(fechaActual);
			
			
			connPool = new ConexionDB();
        	rcEmpresa = connPool.getConnection(empresaForm.getEsquema());
        	conEmpresa = rcEmpresa.getCon();
        	
        	
			String fechaHoy = Utils.getFechayyyyMMdd();
			// logger.info("fechaHoy--------------->"+fechaHoy);
			int mes = Integer.parseInt(fechaHoy.substring(5, 7));
			int year = Integer.parseInt(fechaHoy.substring(0, 4));
			int dia = Integer.parseInt(fechaHoy.substring(8, 10));
			
			int horaDia = Integer.parseInt(horaActual.substring(0, 2));
			
			
			AlertaComplementoForm configForm = confBean.buscarConfProceso(conEmpresa, rcEmpresa.getEsquema(), "PRO02");
			
			String arrDias [] = null;
			if (!"".equalsIgnoreCase(configForm.getDiasProcesar())) {
				arrDias = configForm.getDiasProcesar().split(",");
				if (arrDias.length == 0) {
					arrDias = configForm.getDiasProcesar().split(";");
					if (arrDias.length == 0) {
						arrDias = configForm.getDiasProcesar().split("|");
					}
				}
			}
			
			boolean ejecutarProceso = false;
			if (arrDias != null) {
				for (int x = 0; x < arrDias.length; x++) {
					try {
						logger.info("arrDias[x]===>"+arrDias[x]);
						if (dia == Utils.noNuloINT(arrDias[x])) {
							ejecutarProceso = true;
							break;
						}
					}catch(Exception e) {
						ejecutarProceso = false;
					}
				}
			}
			// logger.info("ejecutarProceso===>"+ejecutarProceso);
			//if (dia <= 11 && horaDia >= 4) {
			if (ejecutarProceso && horaDia >= 6) {
				if (mes == 1) {
					year = year - 1;
					mes = 12;
				}else {
					mes = mes - 1;
				}
				
				String mesC = null;
				if (mes <= 9) {
					mesC = "0" + mes;
				}else {
					mesC = String.valueOf(mes);
				}
				
				String tipoEnvio = null;
				if (dia >= 10) {
					tipoEnvio = "0"+dia;
				}else {
					tipoEnvio = "00"+dia;
				}
				
				// logger.info("tipoEnvio--------------->"+tipoEnvio);
                boolean isEjecuto = isEjecuto(conEmpresa, empresaForm.getEsquema(), tipoEnvio);
				// logger.info("isEjecuto--------------->"+isEjecuto);
				if (!isEjecuto) {
					//String fechaInicial = String.valueOf(year) + "-"  + mesC + "-" + "01" + " 01:01:01.0"; // fecha Inicial
					String fechaInicial = configForm.getFechaPago() + " 01:01:01.0";
					String fechaFinal = String.valueOf(year) + "-" + mesC + "-" + "31" + " 23:59:59.0"; // fecha Final
					// logger.info("fechaIni----->"+fechaInicial);
					// logger.info("fechaFin----->"+fechaFinal);
					
					ArrayList<ComplementosForm> listaPagadas = obtenerOrdenesPagadas(conEmpresa, empresaForm.getEsquema(), fechaInicial, fechaFinal);
					int totEnvios = enviarCorreo(conEmpresa, empresaForm.getEsquema(), listaPagadas, empresaForm.getEmailDominio(), empresaForm.getPwdCorreo(), tipoEnvio);
					// logger.info("totEnvios------->"+totEnvios);
					grabarProceso(conEmpresa, empresaForm.getEsquema(), totEnvios, tipoEnvio); // se grabar en bitacora
				}
			}
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}finally {
			try {
				if (conEmpresa  != null) {
					conEmpresa.close();
				}
			}catch(Exception e) {
				conEmpresa = null;
			}
		}
	}
	 
	
	//@SuppressWarnings("unchecked")
	private boolean isEjecuto(Connection con, String esquema, String tipoEnvio)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	String fecha = Utils.getFechayyyyMMdd();
            stmt = con.prepareStatement( ComplementoProcesoQuerys.getBuscarRespaldo(esquema));
            stmt.setString(1, "COM");
            stmt.setString(2, fecha);
            rs = stmt.executeQuery();
			if(rs.next()){
				//String tipoEnvioRS = Utils.noNulo(rs.getString(1));
				//if (tipoEnvioRS.equalsIgnoreCase(tipoEnvio)) {
					return true;
				//}
            }
			return false;
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
	        }catch(Exception e){
	            stmt = null;
	        }
        }
        return false;
    }
	
	
	//@SuppressWarnings("unchecked") TIPO_ENVIO, TOTAL_ENVIOS, ESTATUS
		private void grabarProceso(Connection con, String esquema, int totEnvios, String tipoEnvio)
	    {
	        PreparedStatement stmt = null;
	        try{
	            stmt = con.prepareStatement( ComplementoProcesoQuerys.getGrabarProceso(esquema));
	            stmt.setString(1, "COM");
	            stmt.setString(2, tipoEnvio);
	            stmt.setInt(3, totEnvios);
	            stmt.setString(4, "OK");
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
		
	
	//@SuppressWarnings("unchecked")
		private ArrayList<ComplementosForm> obtenerOrdenesPagadas(Connection con, String esquema, String fechaInicial, String fechaFinal)
	    {
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        ArrayList<ComplementosForm> listaOrdenes = new ArrayList<ComplementosForm>();
	        ComplementosForm complementoForm = new ComplementosForm();
	        try
	        {
	            stmt = con.prepareStatement( ComplementoProcesoQuerys.getObtenerPagadas(esquema));
	            stmt.setString(1, "A4");
	            stmt.setString(2, fechaInicial);
	            stmt.setString(3, fechaFinal);
	            stmt.setString(4, "MEX");
	            
	            //logger.info("stmtFecha==>"+stmt);
	            rs = stmt.executeQuery();
	            //DecimalFormat decimal = new DecimalFormat("###,###.##");
	           // String folioComplemento = "";
	           while(rs.next()){
					// folioComplemento = Utils.noNulo(rs.getString(9));
					// if ("".equals(folioComplemento)) { // aun no tiene complemento
						complementoForm.setFolioEmpresa(rs.getLong(1));
						complementoForm.setMontoComplemento(rs.getString(2));
						complementoForm.setUuidComplemento(Utils.noNuloNormal(rs.getString(3)));
						complementoForm.setSerieOrden(Utils.noNulo(rs.getString(4)));
						complementoForm.setTipoOrden(Utils.noNulo(rs.getString(5)));
						complementoForm.setRfc(Utils.noNulo(rs.getString(6)));
						complementoForm.setRazonSocial(Utils.regresaCaracteresNormales(Utils.noNulo(rs.getString(7))));
						complementoForm.setEmailPrimario(Utils.noNuloNormal(rs.getString(8)));
						complementoForm.setFechaPago(Utils.noNuloNormal(rs.getString(9)));
						listaOrdenes.add(complementoForm);
						complementoForm = new ComplementosForm();
					//}
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
		        }catch(Exception e){
		            stmt = null;
		        }
	        }
	        return listaOrdenes;
	    }
	
	
	
	private int enviarCorreo(Connection conEmpresa, String esquemaEmpresa, ArrayList<ComplementosForm> listaPagadas, String usuarioEmisor, String passwordEmisorMensaje, String tipoEnvio) {
		String sbHTML = null;
		AlertaComplementoBean confOutBean = new AlertaComplementoBean();
		ArrayList<ComplementosForm> listaEnviar = new ArrayList<ComplementosForm>();
		int totEnvios = 0;
		try {
			
			AlertaComplementoForm configForm = confOutBean.buscarConfProceso(conEmpresa, esquemaEmpresa, "PRO02");
			
			String rfcRS = null;
			String razonSocial = null;
			ComplementosForm complementoForm = null;
			String emailTO [] = {""};
			String emailProveedor = "";
			
			// se obtiene bandera de tipo de validacion
			  //String correoRespaldo = ConfigAdicionalesBean.obtenerValorVariable(conEmpresa, esquemaEmpresa, "CORREO_COMPLEMENTO");
			  
			// Termina

			String emailCC [] = {configForm.getDestinatario1(), configForm.getDestinatario2(), configForm.getDestinatario3(), configForm.getDestinatario4(), configForm.getDestinatario5()};

			for (int x = 0; x < listaPagadas.size(); x++) {
				complementoForm  = listaPagadas.get(x);
				if (rfcRS == null || rfcRS.equalsIgnoreCase(complementoForm .getRfc())) {
					listaEnviar.add(complementoForm);
					rfcRS = complementoForm.getRfc();
					emailProveedor = complementoForm.getEmailPrimario();
					razonSocial = complementoForm.getRazonSocial();
				}else {
					
					// SE ENVIA EL CORREO AL PROVEEDOR
					
					//CorreoBean.usuarioEmisorMensaje = usuarioEmisor;
					//CorreoBean.passwordEmisorMensaje = passwordEmisorMensaje;
					// logger.info("Mensaje antes de envio----->"+configForm.getMensajeError());
					emailTO[0] = emailProveedor;
					sbHTML = UtilsHTML.generaHTMLComplemento(listaEnviar, razonSocial, configForm.getMensajeError());
					//logger.info("Enviando complementos a : "+emailProveedor);
					//logger.info("sbHTML----->"+Utils.regresaCaracteresHTML(sbHTML));
					EnviaCorreoGrid.enviarCorreo(null, sbHTML, false, emailTO, emailCC, "SIAREX - "+configForm.getSubject(), usuarioEmisor, passwordEmisorMensaje);
					totEnvios++;
					// TERMINA
					
					
					
					listaEnviar.clear();
					listaEnviar.add(complementoForm);
					rfcRS = complementoForm.getRfc();
					emailProveedor = complementoForm.getEmailPrimario();
					razonSocial = complementoForm.getRazonSocial();
					
				}
			}
			
			
			if (rfcRS != null) {
				// SE ENVIA EL CORREO AL PROVEEDOR
				
				//CorreoBean.usuarioEmisorMensaje = usuarioEmisor;
				//CorreoBean.passwordEmisorMensaje = passwordEmisorMensaje;
				emailTO[0] = emailProveedor;
				sbHTML = UtilsHTML.generaHTMLComplemento(listaEnviar, razonSocial, configForm.getMensajeError());
				// logger.info("Enviando complementos a : "+emailProveedor);
				// logger.info("sbHTML----->"+Utils.regresaCaracteresHTML(sbHTML));
				
				// CorreoBean.enviarCorreo(null, sbHTML, false, emailTO, emailCC, "ALERTA COMPLEMENTO DE PAGO", usuarioEmisor, passwordEmisorMensaje);
				EnviaCorreoGrid.enviarCorreo(null, sbHTML, false, emailTO, emailCC, "SIAREX - "+configForm.getSubject(), usuarioEmisor, passwordEmisorMensaje);
				
				totEnvios++;
				// TERMINA
			}
			
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return totEnvios;
	}
	
}
