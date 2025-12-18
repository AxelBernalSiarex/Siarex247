package com.siarex247.cumplimientoFiscal.HistorialPagos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.Proveedores.ProveedoresBean;
import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.configSistema.AlertaComplemento.AlertaComplementoBean;
import com.siarex247.configSistema.AlertaComplemento.AlertaComplementoForm;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsBD;
import com.siarex247.utils.UtilsHTML;
import com.siarex247.validaciones.ComplementoProcesoQuerys;

public class HistoricoPagosProceso {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	public void monitoreaComplementoHistoricoP(int diaProceso){
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
	                        if ("toyota".equalsIgnoreCase(empresasForm.getEsquema()) 
	                         || "tmmbcservicios".equalsIgnoreCase(empresasForm.getEsquema())) {
	                           
	                        } else {
	                        	iniciarProcesoHistoricoPagos(empresasForm, diaProceso);
	                        }
	                    } catch (Exception e) {
	                        Utils.imprimeLog("monitoreaComplementoHistoricoP", e);
	                    }
	                }
	            }
	        }catch(Exception e){
	            Utils.imprimeLog("monitoreaComplementoHistoricoP - monitoreaCorreo ", e);
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
	        Utils.imprimeLog("monitoreaComplementoHistoricoP", e);
	    }
	}
	
	public void iniciarProcesoHistoricoPagos(EmpresasForm empresaForm, int diaProceso) {
		logger.info("ENTRO A iniciarProcesoComplementoHP.");
	    ConexionDB connPool = null;
	    ResultadoConexion rcEmpresa = null;
	    Connection conEmpresa = null;
	    AlertaComplementoBean confBean = new AlertaComplementoBean();
	    boolean ejecutarProceso = false;
	    try{
	        connPool = new ConexionDB();
	        rcEmpresa = connPool.getConnection(empresaForm.getEsquema());
	        conEmpresa = rcEmpresa.getCon();
	       
	        AlertaComplementoForm configForm = 
	                confBean.buscarConfProceso(conEmpresa, rcEmpresa.getEsquema(), "PRO02");
	        
	        if ("S".equalsIgnoreCase(Utils.noNulo(configForm.getActivar()))) {
	            ejecutarProceso = true;
	        }
	        
	        if (ejecutarProceso) {
	            notificarComplementoHistoricoP(empresaForm, diaProceso);
	        }
	        
	    } catch(Exception e) {
	        Utils.imprimeLog("iniciarProcesoComplementoHP", e);
	    } finally {
	        try {
	            if (conEmpresa != null) {
	                conEmpresa.close();
	            }
	            conEmpresa = null;
	        } catch(Exception e) {
	            conEmpresa = null;
	        }
	    }
	}
	
	
	
	private void notificarComplementoHistoricoP(EmpresasForm empresaForm, int diaProceso) {

	    ConexionDB connPool = null;
	    ResultadoConexion rcEmpresa = null;
	    Connection conEmpresa = null;
	    AlertaComplementoBean confBean = new AlertaComplementoBean();

	    try {
	        boolean forzarEjecucion = (diaProceso == -1);

	        Date fechaActual = new Date();
	        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
	        String horaActual = formatTime.format(fechaActual);

	        connPool = new ConexionDB();
	        rcEmpresa = connPool.getConnection(empresaForm.getEsquema());
	        conEmpresa = rcEmpresa.getCon();

	        String fechaHoy = Utils.getFechayyyyMMdd();   // yyyy-MM-dd
	        int dia = Integer.parseInt(fechaHoy.substring(8, 10));
	        int horaDia = Integer.parseInt(horaActual.substring(0, 2));

	        AlertaComplementoForm configForm =
	                confBean.buscarConfProceso(conEmpresa, rcEmpresa.getEsquema(), "PRO02");

	        String dias = Utils.noNulo(configForm.getDiasProcesar()).trim();
	        String[] arrDias = null;
	        boolean ejecutarProceso = false;

	        logger.info("=== INICIO PROCESO HP01 (HISTORIAL_PAGOS) esquema="
	                    + empresaForm.getEsquema()
	                    + " diaProceso=" + diaProceso
	                    + " forzarEjecucion=" + forzarEjecucion
	                    + " DIAS_PROCESAR='" + dias + "'"
	                    + " diaHoy=" + dia
	                    + " horaDia=" + horaDia + " ===");

	        if (!forzarEjecucion) {
	            if (dias.isEmpty()) {
	                logger.info("[HP01][" + empresaForm.getEsquema()
	                            + "] DIAS_PROCESAR vacío -> se ejecuta cualquier día");
	                ejecutarProceso = true;
	            } else {
	                if (dias.contains(",")) {
	                    arrDias = dias.split(",");
	                } else if (dias.contains(";")) {
	                    arrDias = dias.split(";");
	                } else if (dias.contains("|")) {
	                    arrDias = dias.split("\\|");
	                } else {
	                    arrDias = new String[]{ dias };
	                }

	                if (arrDias != null) {
	                    for (int x = 0; x < arrDias.length; x++) {
	                        try {
	                            logger.info("[HP01][" + empresaForm.getEsquema()
	                                        + "] Comparando diaHoy=" + dia
	                                        + " con DIAS_PROCESAR[" + x + "]=" + arrDias[x]);
	                            if (dia == Utils.noNuloINT(arrDias[x])) {
	                                ejecutarProceso = true;
	                                break;
	                            }
	                        } catch (Exception e) {
	                            ejecutarProceso = false;
	                        }
	                    }
	                }
	            }

	            logger.info("[HP01][" + empresaForm.getEsquema()
	                        + "] Resultado validación días: ejecutarProceso=" + ejecutarProceso
	                        + " horaDia=" + horaDia);

	            if (!(ejecutarProceso && horaDia >= 6)) {
	                logger.info("[HP01][" + empresaForm.getEsquema()
	                            + "] No se ejecuta HP01: ejecutarProceso=" + ejecutarProceso
	                            + ", horaDia=" + horaDia);
	                return;
	            }
	        } else {
	            logger.info("[HP01][" + empresaForm.getEsquema()
	                        + "] MODO FORZADO: se ejecuta sin validar DIAS_PROCESAR ni hora");
	            ejecutarProceso = true;
	        }

	        // 🔹 Rango fijo 2010-01-01 a hoy
	        String fechaInicial = "";
	        fechaInicial = "2010-01-01";
	        String fechaFinal   = fechaHoy;

	        logger.info("[HP01][" + empresaForm.getEsquema()
	                    + "] Rango de fechas HISTORIAL_PAGOS: " + fechaInicial + " a " + fechaFinal);

	        // 1) Lista de RFCs
	        ArrayList<HistorialPagosForm> listaHistorial = obtenerPagosHistorialPagos(conEmpresa, empresaForm.getEsquema(), fechaInicial, fechaFinal);

	        logger.info("[HP01][" + empresaForm.getEsquema() + "] TOTAL RFCs en HISTORIAL_PAGOS: "
	                    + listaHistorial.size());

	        String tipoEnvio = null;
			if (dia >= 10) {
				tipoEnvio = "0"+dia;
			}else {
				tipoEnvio = "00"+dia;
			}
			
			boolean isEjecuto = isEjecuto(conEmpresa, empresaForm.getEsquema(), tipoEnvio);
	        logger.info("isEjecuto : " + isEjecuto);
			if (!isEjecuto) {
				 logger.info("ENNTRO A ENVIO DE CORREOS");
			     // 2) Enviar correo por RFC usando el método nuevo
		        int totEnvios = enviarCorreoHistorialPagosHP( conEmpresa, empresaForm.getEsquema(), listaHistorial, empresaForm.getEmailDominio(), empresaForm.getPwdCorreo(), fechaInicial, fechaFinal);
		       grabarProceso(conEmpresa, empresaForm.getEsquema(), totEnvios, tipoEnvio); // se grabar en bitacora
		        
		        logger.info("[HP01][" + empresaForm.getEsquema() + "] TOTAL correos enviados HP01: " + totEnvios);
			}


	    } catch (Exception e) {
	        Utils.imprimeLog("notificarComplementoHistoricoP", e);
	    } finally {
	        try {
	            if (conEmpresa != null) {
	                conEmpresa.close();
	            }
	        } catch (Exception e) {
	            conEmpresa = null;
	        }
	    }
	}

	
	private ArrayList<HistorialPagosForm> obtenerPagosHistorialPagos(
	        Connection con, String esquema, String fechaInicial, String fechaFinal) {

	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<HistorialPagosForm> lista = new ArrayList<HistorialPagosForm>();

	    try {
	        String sql = HistorialPagosQuery.getObtenerPagosHistorialRango(esquema);
	        stmt = con.prepareStatement(sql);

	        // ESTATUS C01 = complemento correcto, C20 = con error
	        stmt.setString(1, "C01");
	        stmt.setString(2, "C20");
	        stmt.setString(3, fechaInicial);  // '2010-01-01'
	        stmt.setString(4, fechaFinal);    // fechaHoy

	        logger.info("[HP01][" + esquema + "] Ejecutando query HISTORIAL_PAGOS: " + stmt);

	        rs = stmt.executeQuery();

	        while (rs.next()) {
	        	HistorialPagosForm hp = new HistorialPagosForm();
	            hp.setRfc(Utils.noNulo(rs.getString(1)));
	            lista.add(hp);
	        }

	    } catch (Exception e) {
	        Utils.imprimeLog("obtenerPagosHistorialPagos", e);
	    } finally {
	        try {
	            if (rs != null) {
	                rs.close();
	            }
	            rs = null;
	            if (stmt != null) {
	                stmt.close();
	            }
	            stmt = null;
	        } catch (Exception e) {
	            rs = null;
	            stmt = null;
	        }
	    }
	    return lista;
	}
	
	private ArrayList<HistorialPagosForm> obtenerPagosHistorialPorRfc(
	        Connection con, String esquema, String rfc, String fechaInicial, String fechaFinal) {

	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<HistorialPagosForm> lista = new ArrayList<HistorialPagosForm>();

	    try {
	        String sql = HistorialPagosQuery.getObtenerPagosHistorialDetallePorRfc(esquema);
	        stmt = con.prepareStatement(sql);

	        stmt.setString(1, rfc);
	        stmt.setString(2, "C01");
	        stmt.setString(3, "C20");
	        //stmt.setString(4, fechaInicial);
	        // stmt.setString(5, fechaFinal);

	        logger.info("[HP01][" + esquema + "] Ejecutando detalle HISTORIAL_PAGOS para RFC=" + rfc + ": " + stmt);

	        rs = stmt.executeQuery();

	        while (rs.next()) {
	        	HistorialPagosForm hp = new HistorialPagosForm();
	            hp.setRfc(rfc);
	            hp.setSerie(Utils.noNulo(rs.getString("SERIE")));
	            hp.setFolio(Utils.noNulo(rs.getString("FOLIO")));
	            hp.setFechaPago(Utils.noNuloNormal(rs.getString("FECHA_PAGO")));
	            hp.setUuidFactura(Utils.noNuloNormal(rs.getString("UUID_FACTURA")));
	            hp.setTipoMoneda(Utils.noNulo(rs.getString("TIPO_MONEDA")));
	            hp.setTotal(rs.getDouble("TOTAL"));
	            lista.add(hp);
	        }

	    } catch (Exception e) {
	        Utils.imprimeLog("obtenerPagosHistorialPorRfc", e);
	    } finally {
	        try {
	            if (rs != null)  rs.close();
	            if (stmt != null) stmt.close();
	        } catch (Exception e) {
	            rs = null;
	            stmt = null;
	        }
	    }
	    return lista;
	}
	
	private int enviarCorreoHistorialPagosHP(
	        Connection conEmpresa,
	        String esquemaEmpresa,
	        ArrayList<HistorialPagosForm> listaRfc,
	        String usuarioEmisor,
	        String passwordEmisorMensaje,
	        String fechaInicial,
	        String fechaFinal) {
		logger.info("enviarCorreoHistorialPagosHP");

	    int totEnvios = 0;

	    // Nada que procesar
	    if (listaRfc == null || listaRfc.isEmpty()) {
	        logger.info("[HP01][" + esquemaEmpresa + "] Sin RFCs a procesar para envío de correo.");
	        return 0;
	    }

	    AlertaComplementoBean confOutBean = null;
	    AlertaComplementoForm configForm = null;
	    ProveedoresBean provBean = null;
	    ProveedoresForm provForm = null;

	    String[] emailCC = null;  // si no quieres copia, luego mandamos null al enviarCorreo
	    String[] emailTO = null;

	    String rfc = null;
	    String razonSocial = null;
	    int claveProveedor = 0;

	    ArrayList<HistorialPagosForm> listaDetalle = null;

	    try {
	        // Config de la alerta (MENSAJE, SUBJECT, destinatarios, etc.)
	        confOutBean = new AlertaComplementoBean();
	        configForm = confOutBean.buscarConfProceso(conEmpresa, esquemaEmpresa, "PRO02");

	        // Si NO quieres mandar copia a los de la alerta, puedes comentar esto
	        // y dejar emailCC = null.
	        emailCC = new String[] {
	            configForm.getDestinatario1(),
	            configForm.getDestinatario2(),
	            configForm.getDestinatario3(),
	            configForm.getDestinatario4(),
	            configForm.getDestinatario5()
	        };

	        logger.info("[HP01][" + esquemaEmpresa + "] emailCC (alerta) = "
	                + java.util.Arrays.toString(emailCC));

	        provBean = new ProveedoresBean();

	        // Plantilla original UNA sola vez, fuera del ciclo
	        final String mensajeTemplateOriginal = Utils.noNuloNormal(configForm.getMensajeError());

	        String sbHTML = null; 
	        		
	        // ===== CICLO POR CADA RFC =====
	        for (HistorialPagosForm rfcForm : listaRfc) {

	            rfc = Utils.noNulo(rfcForm.getRfc());
	            if ("".equals(rfc)) {
	                continue;
	            }

	            logger.info("[HP01][" + esquemaEmpresa + "] Procesando RFC=" + rfc + " para envío de correo.");

	            // 1) Buscar proveedor por RFC
	            provForm = provBean.consultarProveedorXrfc(conEmpresa, esquemaEmpresa, rfc);
	            if (provForm == null) {
	                logger.info("[HP01][" + esquemaEmpresa + "] RFC sin proveedor registrado: " + rfc);
	                continue;
	            }

	            // idProveedor viene como String, UtilsBD espera int
	            razonSocial = Utils.noNuloNormal(provForm.getRazonSocial());

	            // =======================
	            // 2) CLAVE_PROVEEDOR Y CORREOS
	            // =======================
	            claveProveedor = provForm.getClaveRegistro();
	            if (claveProveedor <= 0) {
	                logger.info("[HP01][" + esquemaEmpresa + "] claveProveedor inválida ("
	                        + claveProveedor + ") para RFC=" + rfc + ". Se omite envío.");
	                continue;
	            }

	            // Correos del proveedor (PROVEEDORES_EMAIL, pagos='S')
	            emailTO = UtilsBD.obtenerCorreorProveedor(
	                    conEmpresa,
	                    esquemaEmpresa,
	                    claveProveedor,     // CLAVE_PROVEEDOR
	                    "S", "N", "N", "N"  // Solo los de pagos
	            );

	            // Validar que sí haya correos válidos
	            boolean tieneCorreosPagos = false;
	            if (emailTO != null) {
	                for (String correo : emailTO) {
	                    String c = Utils.noNulo(correo).trim();
	                    if (!"".equals(c) && !"N".equalsIgnoreCase(c)) {
	                        tieneCorreosPagos = true;
	                        break;
	                    }
	                }
	            }

	            if (!tieneCorreosPagos) {
	                logger.info("[HP01][" + esquemaEmpresa + "] Proveedor sin correos de pagos en PROVEEDORES_EMAIL. "
	                        + "RFC=" + rfc + " claveProveedor=" + claveProveedor + ". Se omite envío.");
	                continue;
	            }

	            // Logs de destinatarios finales
	            logger.info("[HP01][" + esquemaEmpresa + "] RFC=" + rfc
	                    + " claveProveedor=" + claveProveedor
	                    + " emailTO (PROVEEDOR) = " + java.util.Arrays.toString(emailTO));
	            logger.info("[HP01][" + esquemaEmpresa + "] emailCC (ALERTA) = "
	                    + java.util.Arrays.toString(emailCC));

	            // 3) Detalle facturas pora RFC
	            listaDetalle = obtenerPagosHistorialPorRfc( conEmpresa, esquemaEmpresa, rfc, fechaInicial, fechaFinal );

	            if (listaDetalle == null || listaDetalle.isEmpty()) {
	                logger.info("[HP01][" + esquemaEmpresa + "] Sin detalle de facturas para RFC=" + rfc);
	                continue;
	            }

	            // 4) HTML completo usando UtilsHTML (tabla + reemplazo de << >>)
	            sbHTML = UtilsHTML.generaHTMLHistorialPagosHP( listaDetalle, razonSocial, rfc, mensajeTemplateOriginal);
	            //logger.info("Mensaje a Enviar..."+sbHTML);
	            // 5) Enviar correo
	            EnviaCorreoGrid.enviarCorreo( null, sbHTML, false, emailTO, emailCC, "SIAREX - " + configForm.getSubject(), usuarioEmisor, passwordEmisorMensaje);

	            totEnvios++;
	            logger.info("[HP01][" + esquemaEmpresa + "] Correo enviado a RFC=" + rfc + ")");
	        }

	    } catch (Exception e) {
	        Utils.imprimeLog("enviarCorreoHistorialPagosHP", e);
	    }

	    return totEnvios;
	}

				

	//@SuppressWarnings("unchecked")
	private boolean isEjecuto(Connection con, String esquema, String tipoEnvio)
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	String fecha = Utils.getFechayyyyMMdd();
            stmt = con.prepareStatement( ComplementoProcesoQuerys.getBuscarRespaldo(esquema));
            stmt.setString(1, "HPA");
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
	            stmt.setString(1, "HPA");
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
		
 }
