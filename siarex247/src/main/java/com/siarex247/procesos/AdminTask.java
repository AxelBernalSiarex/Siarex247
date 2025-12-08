package com.siarex247.procesos;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.siarex247.bd.ConexionDB;
import com.siarex247.bd.ResultadoConexion;
import com.siarex247.catalogos.CentroCostos.CentroCostosBean;
import com.siarex247.catalogos.CentroCostos.CentroCostosForm;
import com.siarex247.configSistema.ConfigAdicionales.ConfigAdicionalesBean;
import com.siarex247.layOut.Tareas.EnviaAlertas;
import com.siarex247.layOut.Tareas.OrdenesAlertas;
import com.siarex247.layOut.Tareas.TareasBean;
import com.siarex247.layOut.Tareas.TareasForm;
import com.siarex247.layOut.Tareas.TareasOrdenesForm;
import com.siarex247.seguridad.Accesos.AccesoBean;
import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsBD;
import com.siarex247.utils.UtilsPATH;

public class AdminTask {
	public static final Logger logger = Logger.getLogger("siarex247");
	
	
	
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
						// logger.info("Buscando tareas de la empresa : "+empresasForm.getNombreLargo());
						if ("A".equalsIgnoreCase(empresasForm.getEstatus())){
							try {
								enviaCorreo(empresasForm);
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
	
	
	public void enviaCorreo(EmpresasForm empresasForm){
		Connection conEmpresa = null;
		ResultadoConexion rcEmpresa = null;
		//Connection conSistema = null;
	//	ResultadoConexion rcSistema = null;
		
		ConexionDB connPool = null;
		
		TareasBean tareasBean = new TareasBean();
		try{
			// logger.info("Buscando informacion de tareas de la empresa----->"+empresasForm.getNombreLargo());
			connPool = new ConexionDB();
			rcEmpresa = connPool.getConnection(empresasForm.getEsquema());
			//rcSistema = connPool.getConnectionJDBC("siarex");
			
		    conEmpresa = rcEmpresa.getCon();
		  //  conSistema = rcSistema.getCon();
		    
			ArrayList<TareasForm> datos = tareasBean.buscarTareas(conEmpresa, empresasForm.getEsquema());
			ArrayList<TareasForm> datosPro = null;
			TareasForm tareasForm = null;
			TareasForm tareasFormProv = null;
			String pathTareas = UtilsPATH.REPOSITORIO_DOCUMENTOS + empresasForm.getEsquema() +"/TAREAS_SIAREX/";
			//logger.info("pathTareas----->"+pathTareas);

			Date fechaActual = new Date();
			String fechaRegistro = null;
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
			String tiempoServerTMP = formatDate.format(fechaActual);
			
			long tiempoServer =  Long.parseLong(tiempoServerTMP);
			
			boolean bandEnviaArchivo = false;
			EnviaAlertas enviaAlertas = new EnviaAlertas();
			StringBuffer sbFecha = new StringBuffer();
			String bandEntro = "";
			OrdenesAlertas ordenesAlertas = new OrdenesAlertas();
			for (int x = 0; x < datos.size(); x++){
				sbFecha.setLength(0);
				tareasForm = datos.get(x);
				fechaRegistro = sbFecha.append(tareasForm.getFechaTarea().substring(0,4))
								 .append(tareasForm.getFechaTarea().substring(5,7))
								 .append(tareasForm.getFechaTarea().substring(8,10))
								 .append(tareasForm.getFechaTarea().substring(11, 13))
								 .append(tareasForm.getFechaTarea().substring(14,16))
								 .append(tareasForm.getFechaTarea().substring(17,19)).toString();
				
				//formatear la fechaRegistro para solo tomarlas hhmmss
				long tiempoRegistro =  Long.parseLong(fechaRegistro);
				if ( "GEN".equalsIgnoreCase(tareasForm.getTipoTarea()) || "TRH".equalsIgnoreCase(tareasForm.getTipoTarea())){
					if ((tiempoServer >= tiempoRegistro) || tareasForm.getEstatus().equals("2")){ // se envia ahora por estatus 2
						datosPro = tareasBean.buscarEmailProv(conEmpresa, empresasForm.getEsquema(), tareasForm.getClaveTarea());
						if (datosPro.isEmpty()){
							datosPro = tareasBean.buscarEmailTipo(conEmpresa, empresasForm.getEsquema(), tareasForm.getTipoEnvio());
						}
						if (!"".equals(tareasForm.getNombreArchivo())){
							bandEnviaArchivo = true;
						}
						String listaCorreosProveedores [] = null;
						//CorreoBean.passwordEmisorMensaje = empresasForm.getPwdCorreo();
				        //CorreoBean.usuarioEmisorMensaje = empresasForm.getEmailDominio();
				        
				        String correoUsuario [] = {"N"};
				        if (!tareasForm.getCorreoDe().equals("")){ // requiere copia del correo....
				        	correoUsuario[0] = tareasForm.getCorreoDe();
				        }
						for (int y = 0; y < datosPro.size(); y++){
							tareasFormProv = datosPro.get(y);
							listaCorreosProveedores = UtilsBD.obtenerCorreorProveedor(conEmpresa, empresasForm.getEsquema(), tareasFormProv.getClaveProveedor(), "S", "S", "N", "N");
							EnviaCorreoGrid.enviarCorreo(pathTareas + tareasForm.getNombreArchivo(), tareasForm.getMensaje(), bandEnviaArchivo, listaCorreosProveedores, correoUsuario, tareasForm.getSubject(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo()); // se envia correo sin copia a usuario
						}
						  int res = tareasBean.actualizaTarea(conEmpresa, empresasForm.getEsquema(), tareasForm.getClaveTarea());
						  logger.info("La tarea "+tareasForm.getClaveTarea()+" Se ha actualizado satisfactoriamente.....");
					}
				}
				else if ( "PRO".equalsIgnoreCase(tareasForm.getTipoTarea()) || "USR".equalsIgnoreCase(tareasForm.getTipoTarea()) ) {
					if (tiempoServer >= tiempoRegistro){
						// AQUI SE DEBE LLEVAR UN CONTROL PARA IDENTIFICAR SI 
						// YA SE EJECUTO EL PROCESO EN ESTE DIA, EN BASE AL ID DE LA TAREA
						bandEntro = tareasBean.buscarTareasHistorico(conEmpresa, empresasForm.getEsquema(), tareasForm.getClaveTarea());
						
						if ("N".equalsIgnoreCase(bandEntro)){
							if ("PRO".equalsIgnoreCase(tareasForm.getTipoTarea())){ // es de proveedores el proceso
								enviaAlertas.alertasProveedores(conEmpresa, empresasForm, tareasForm);
							}else{ // es de usuarios
								enviaAlertas.alertasUsuarios(conEmpresa, empresasForm, tareasForm);
							}
							tareasBean.grabarHistorico(conEmpresa, empresasForm.getEsquema(), tareasForm.getClaveTarea(), tareasForm.getFechaTarea());
						}
					}
				}else if ( "ORD".equalsIgnoreCase(tareasForm.getTipoTarea())) {
					if (tiempoServer >= tiempoRegistro || tareasForm.getEstatus().equals("2")){ // se envia ahora por estatus 2
						
						int res = ordenesAlertas.iniciaProceso(conEmpresa, empresasForm.getEsquema(), tareasForm.getClaveTarea(), pathTareas, pathTareas, tareasForm.getNombreArchivo(), tareasForm.getTipoEnvio(), 
								false, empresasForm.getTipoEmpresa());

				        //CorreoBean.passwordEmisorMensaje = empresasForm.getPwdCorreo();
				        //CorreoBean.usuarioEmisorMensaje = empresasForm.getEmailDominio();
				        
						if (res > 0){ // si grabo proveedores
							datosPro = tareasBean.buscarEmailProv(conEmpresa, empresasForm.getEsquema(), tareasForm.getClaveTarea()); // busca los proveedores
							ArrayList<TareasOrdenesForm> datosOrd = null;
							TareasOrdenesForm tareasOrdForm = null;
							String mensajeCorreo = null;
							String listaCorreosProveedores [] = null;
							String llaveUsuario = null;
							String emailUsuario [] =  null;
							String correoUsuario [] = null;
							// HashMap<String, String> mapaOrdenes = null;
							String CORREO_ORDENES = ConfigAdicionalesBean.obtenerValorVariable(conEmpresa, empresasForm.getEsquema(), "CORREO_ORDENES");
							StringBuffer sbMail = new StringBuffer();
							for (int y = 0; y < datosPro.size(); y++){
								tareasFormProv = datosPro.get(y);
								sbMail.setLength(0);
								datosOrd = tareasBean.buscarOrdenesXtarea(conEmpresa, empresasForm.getEsquema(), tareasForm.getClaveTarea(), tareasFormProv.getClaveProveedor(), "OK");
								mensajeCorreo = ordenesAlertas.armarMensajeCorreo(tareasForm.getMensaje(), tareasFormProv.getRazonSocialProv(), empresasForm.getNombreLargo(), "");
								listaCorreosProveedores = UtilsBD.obtenerCorreorProveedor(conEmpresa, empresasForm.getEsquema(), tareasFormProv.getClaveProveedor(), "N", "S", "N", "N");
								// mapaOrdenes = confSistemaBean.obtenerConfiguraciones(conEmpresa, empresasForm.getEsquema());
								for (int a = 0; a < datosOrd.size(); a++){
									tareasOrdForm = datosOrd.get(a);
									sbMail.setLength(0);
									if ("1".equalsIgnoreCase(tareasFormProv.getNotOrdenUsuario())){ // SE ENVIA COPIA AL USUARIO
										llaveUsuario = tareasBean.buscarEmpleadoOrden(conEmpresa, empresasForm.getEsquema(), tareasOrdForm.getNumeroOrden());
										if (llaveUsuario.equalsIgnoreCase("")){ // no esta dada de alta
											/*
											emailUsuario[0] =  "";
											emailUsuario[1] =  "";
											emailUsuario[2] =  "";
											*/
											sbMail.append("N").append(";");
										}else{
											String asignarTo = llaveUsuario;
											if(asignarTo.startsWith("C_")){ // si es centro de costos
												CentroCostosBean centroBean = new CentroCostosBean();
												ArrayList<CentroCostosForm> listaCentros = centroBean.consultaCentrosListaTarea(conEmpresa, empresasForm.getEsquema(), asignarTo.substring(2, asignarTo.length()));
												CentroCostosForm centroCostosForm = null;
												for (int xCentros = 0; xCentros < listaCentros.size(); xCentros++) {
													centroCostosForm = listaCentros.get(xCentros);
													sbMail.append(centroCostosForm.getCorreoCentro()).append(";");
												}
												//logger.info("Email Centro de Costos------->"+sbMail.toString());
												//emailUsuario = sbMail.toString().split(";");
											}else if (asignarTo.startsWith("E_")){ // si es un empleado
												emailUsuario  = UtilsBD.emailUsuario(conEmpresa, empresasForm.getEsquema(), asignarTo.substring(2, asignarTo.length()), "PROVEEDOR");
												sbMail.append(emailUsuario[0]).append(";");
											}
												
										}
										
									}else{
										/*
										emailUsuario[0] =  "";
										emailUsuario[1] =  "";
										emailUsuario[2] =  "";
										*/
										sbMail.append("N").append(";");
									}
									//correoUsuario[0] = emailUsuario[0];
									
									if (!"".equalsIgnoreCase(CORREO_ORDENES)){
										sbMail.append(CORREO_ORDENES).append(";");
									}
									
									logger.info("Lista de correos...."+sbMail.toString());
									correoUsuario = sbMail.toString().split(";"); // se obtiene la lista de los correos 
									//correoUsuario[1] = mapaOrdenes.get("CORREO_ORDENES");
							        //CorreoBean.enviarCorreo(pathTareas + tareasOrdForm.getNombreArchivo(), mensajeCorreo, true, listaCorreosProveedores, correoUsuario, tareasForm.getAsunto(), empresasForm.getEmailDominio(),empresasForm.getPwdCorreo() ); // se envia correo sin copia a usuario
									EnviaCorreoGrid.enviarCorreo(pathTareas + tareasOrdForm.getNombreArchivo(), mensajeCorreo, true, listaCorreosProveedores, correoUsuario, tareasForm.getSubject(), empresasForm.getEmailDominio(),empresasForm.getPwdCorreo() ); // se envia correo sin copia a usuario
								}
							}
						}
						   res = tareasBean.actualizaTarea(conEmpresa, empresasForm.getEsquema(), tareasForm.getClaveTarea());
						  logger.info("La tarea "+tareasForm.getClaveTarea()+" Se ha actualizado satisfactoriamente.....");
					}
			  }
			  bandEnviaArchivo = false;
			}
		    
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}finally{
	        try{
	            if (conEmpresa != null){
	            	conEmpresa.close();
				}
	        }catch(Exception e){
	        	conEmpresa = null;
	        }
        }
	}
	
	
	public static void main(String[] args) {
		try{
			AdminTask l = new AdminTask();
			l.monitoreaCorreo();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}