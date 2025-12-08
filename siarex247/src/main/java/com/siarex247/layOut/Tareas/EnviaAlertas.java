package com.siarex247.layOut.Tareas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.seguridad.Accesos.EmpresasForm;
import com.siarex247.utils.EnviaCorreoGrid;
import com.siarex247.utils.Utils;
import com.siarex247.utils.UtilsBD;
import com.siarex247.utils.UtilsFechas;
import com.siarex247.visor.VisorOrdenes.VisorOrdenesQuerys;

public class EnviaAlertas {
	private final String VACIO = "";
	private static final Logger logger = Logger.getLogger("siarex");
	private final String coma = ";";
	
//infoProveeEmail
	
	public void alertasProveedores(Connection conEmpresa, EmpresasForm empresasForm, TareasForm tareasForm){
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
		String fechaActual = formatDate.format(new Date());
		String fecha = null;
//		String folioEmpresa = null;
		String razonSocialProveedor = null;
		int claveUsuario = 0;
		StringBuffer sbFecha = new StringBuffer(0);
		String fechaUltMov  = VACIO;
		int claveProveedor = 0;
		String mensajeCorreo = VACIO;
		String emailUsuario [] =  {"","", ""};
		boolean enviaCorreo = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		double monto = 0;
		String tipoMoneda = null;

		
		try{
			logger.info("..................................... ALARMAS DE PROVEEDORES ...........................................");
			int num_Dias = tareasForm.getNum_Dias1();
			stmt = conEmpresa.prepareStatement(VisorOrdenesQuerys.getQueryOrdenesUltimoMov(empresasForm.getEsquema()));
	        stmt.setString(1, "A2");
	        stmt.setString(2, "A5");
	        rs = stmt.executeQuery();
	        String listaCorreosProveedores [] = null;
	        
			HashMap<String, String> mapaUsuario = new HashMap<String, String>();
			StringBuffer sbDatos = new StringBuffer();
			HashMap<Integer, String> proveedoresArchivo = new HashMap<Integer, String>();
			ArrayList<Integer> llavesProveedor = new ArrayList<Integer>();
			ArrayList<String> llavesUsuario = new ArrayList<String>();
	        while(rs.next()){
	        	sbFecha.setLength(0);
//	        	folioEmpresa = Utils.noNuloNormal(rs.getString(2));
	        	fecha = Utils.noNuloNormal(rs.getString(3));
	        	fechaUltMov = sbFecha.append(fecha.substring(0,4))
						             .append(fecha.substring(5,7))
						             .append(fecha.substring(8,10))
						             .append(fecha.substring(11, 13))
						             .append(fecha.substring(14,16))
						             .append(fecha.substring(17,19)).toString();
	        	claveProveedor = rs.getInt(5);
	        	razonSocialProveedor = Utils.regresaCaracteresHTML(Utils.noNulo(rs.getString(6)));
	        	enviaCorreo = UtilsFechas.validaFecha(fechaActual, fechaUltMov, num_Dias);
	        	claveUsuario = rs.getInt(7);
	        	monto = rs.getDouble(8);
	        	tipoMoneda = Utils.noNulo(rs.getString(9));
	        	
	        	if (enviaCorreo){
	        		//se agrupan por proveedor
	        		if(proveedoresArchivo.containsKey(claveProveedor)){
	        			sbDatos.append(proveedoresArchivo.get(claveProveedor));
						sbDatos.append("&")
							   .append(razonSocialProveedor).append(coma)
					           .append(monto).append(coma)
					           .append(tipoMoneda).append(coma)
					           .append(claveUsuario).append(coma);
						proveedoresArchivo.put(claveProveedor, sbDatos.toString());
						sbDatos.setLength(0);
	        		}else{
	        			sbDatos.append(razonSocialProveedor).append(coma)
					       .append(monto).append(coma)
					       .append(tipoMoneda).append(coma)
				           .append(claveUsuario).append(coma);
	        			proveedoresArchivo.put(claveProveedor, sbDatos.toString());
	        			llavesProveedor.add(claveProveedor);
						sbDatos.setLength(0);
	        		}
	        		
	        		//se agrupan por usuario
	        		if (mapaUsuario.containsKey(claveUsuario+"&"+claveProveedor)){
	        			sbDatos.append(mapaUsuario.get(claveUsuario+"&"+claveProveedor));
						sbDatos.append("&")
							   .append(razonSocialProveedor).append(coma)
					           .append(monto).append(coma)
					           .append(tipoMoneda).append(coma)
					           .append(claveProveedor).append(coma)
					           .append(claveUsuario).append(coma);
						
						mapaUsuario.put(claveUsuario+"&"+claveProveedor, sbDatos.toString());
						sbDatos.setLength(0);
	         		}else{
        				sbDatos.append(razonSocialProveedor).append(coma)
					       .append(monto).append(coma)
					       .append(tipoMoneda).append(coma)
					       .append(claveProveedor).append(coma)
					       .append(claveUsuario).append(coma);
        				mapaUsuario.put(claveUsuario+"&"+claveProveedor, sbDatos.toString());
        				llavesUsuario.add(claveUsuario+"&"+claveProveedor);
						sbDatos.setLength(0);
        			}
	        	}
	        	
	        } // fin del while
	        
	        if ("N".equalsIgnoreCase(tareasForm.getNotificacion()) || mapaUsuario.isEmpty()){ // se envian agrupados por proveedor
	        	if (!proveedoresArchivo.isEmpty()){
					 String cadHTML = null;
			         //CorreoBean.passwordEmisorMensaje = empresasForm.getPwdCorreo();
			         //CorreoBean.usuarioEmisorMensaje = empresasForm.getEmailDominio();
			         String cadRegistro [] = null;
			         String cadRazon [] = null;
			         for (int x = 0; x < llavesProveedor.size(); x++){
			        	 claveProveedor = llavesProveedor.get(x); 
						 cadRegistro = proveedoresArchivo.get(claveProveedor).split("&");
						 cadRazon = cadRegistro[x].split(coma);
						 listaCorreosProveedores = UtilsBD.obtenerCorreorProveedor(conEmpresa, empresasForm.getEsquema(), claveProveedor, "N", "S", "N", "N");
						 mensajeCorreo = armarMensajeCorreo(tareasForm.getMensaje(),  cadRazon[0], "", "");
						 cadHTML = generaHTML(proveedoresArchivo.get(claveProveedor), empresasForm.getNombreLargo(), mensajeCorreo);
						 EnviaCorreoGrid.enviarCorreo(null, cadHTML, false, listaCorreosProveedores, null , tareasForm.getSubject(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo()); // se envia correo sin copia a usuario
					 }
	        	}
	        	
	        }else{ // se agrupan por usuario
	        	if (!mapaUsuario.isEmpty()){
					 String cadHTML = null;
					 String llaveMapaArray[] = null;
					 String llaveMapa = null;
					 String llaveUsuario = null;
			         //CorreoBean.passwordEmisorMensaje = empresasForm.getPwdCorreo();
			         //CorreoBean.usuarioEmisorMensaje = empresasForm.getEmailDominio();
			         String cadRegistro [] = null;
			         String cadRazon [] = null;
			         String correoUsuario [] = {""};
					 for (int x = 0; x < llavesUsuario.size(); x++){
						 llaveMapa = llavesUsuario.get(x); // 1&NAP920206CJ8
						 llaveMapaArray = llaveMapa.split("&");// [1, NAP920206CJ8]
						 llaveUsuario = llaveMapaArray[0]; // 1
						 claveProveedor =  Integer.parseInt( llaveMapaArray[1]); // NAP920206CJ8
						 emailUsuario  = UtilsBD.emailUsuario(conEmpresa, empresasForm.getEsquema(), llaveUsuario, "PROVEEDOR");
						 //[alma.apodaca1980@gmail.com, JOSE GUADALUPE BURGOS, 2000]
						 
						 cadRegistro = mapaUsuario.get(llaveMapa).split("&"); 
						 cadRazon = cadRegistro[0].split(coma);
						 cadRegistro = mapaUsuario.get(llaveMapa).split("&");
						 mensajeCorreo = armarMensajeCorreo(tareasForm.getMensaje(),  cadRazon[0], empresasForm.getNombreLargo(), emailUsuario[1]);
						 cadHTML = generaHTML(mapaUsuario.get(llaveMapa), empresasForm.getNombreLargo(), mensajeCorreo);
						 
						 listaCorreosProveedores = UtilsBD.obtenerCorreorProveedor(conEmpresa, empresasForm.getEsquema(), claveProveedor, "N", "S", "N","N");
						 correoUsuario[0] = emailUsuario[0];
						 //CorreoBean.enviarCorreo(null, cadHTML, false, listaCorreosProveedores, correoUsuario, tareasForm.getAsunto(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
						 EnviaCorreoGrid.enviarCorreo(null, cadHTML, false, listaCorreosProveedores, correoUsuario, tareasForm.getSubject(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
	        	      
					 }
				}	        	
	        }
	        
		}
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }
		finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
	}
	
	
	
	public void alertasUsuarios(Connection conEmpresa, EmpresasForm empresasForm, TareasForm tareasForm){
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmss");
		String fechaActual = formatDate.format(new Date());
		String fecha = null;
//		String folioEmpresa = null;
		String razonSocialProveedor = null;
		int claveUsuario = 0;
		String estatusPago = null;
		double monto = 0;
		String tipoMoneda = null;
		
		StringBuffer sbFecha = new StringBuffer(0);
		String fechaUltMov  = VACIO;
		String mensajeCorreo = VACIO;
		String emailUsuario[] = null;
		String emailSupervisor[] = {"","", ""};
		boolean enviaCorreo = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try{
			logger.info("..................................... ALARMAS DE USUARIOS ...........................................");
			int num_Dias = tareasForm.getNum_Dias1();
			int num_Dias2 = tareasForm.getNum_Dias2();
			stmt = conEmpresa.prepareStatement(VisorOrdenesQuerys.getQueryOrdenesUltimoMov(empresasForm.getEsquema()));
	        stmt.setString(1, "A1");
	        stmt.setString(2, "A5");
	        rs = stmt.executeQuery();
	        
			HashMap<Integer, String> mapaUsuario = new HashMap<Integer, String>();
			
			StringBuffer sbDatos = new StringBuffer();
			ArrayList<Integer> llavesUsuario = new ArrayList<Integer>();
			while(rs.next()){
	        	sbFecha.setLength(0);
//	        	folioEmpresa = Utils.noNuloNormal(rs.getString(2));
	        	fecha = Utils.noNuloNormal(rs.getString(3));
	        	fechaUltMov = sbFecha.append(fecha.substring(0,4))
						             .append(fecha.substring(5,7))
						             .append(fecha.substring(8,10))
						             .append(fecha.substring(11, 13))
						             .append(fecha.substring(14,16))
						             .append(fecha.substring(17,19)).toString();
	        	estatusPago  = Utils.noNulo(rs.getString(4));
	        	monto = rs.getDouble(8);
	        	tipoMoneda = Utils.noNulo(rs.getString(9));
//	        	logger.info("folioEmpresa ----->"+folioEmpresa );
	        	razonSocialProveedor = Utils.noNulo(rs.getString(6));
	        	if ("A5".equalsIgnoreCase(estatusPago)){ // no se a ingreso la factura
	        		enviaCorreo = UtilsFechas.validaFecha(fechaActual, fechaUltMov, num_Dias);	
	        	}else { // no se ha ingresado el contrarecibo
	        		enviaCorreo = UtilsFechas.validaFecha(fechaActual, fechaUltMov, num_Dias2);
	        	}
	        	
	        	claveUsuario = rs.getInt(7);
	        	
	        	if (enviaCorreo){
	        		if (mapaUsuario.containsKey(claveUsuario)){
	        			sbDatos.append(mapaUsuario.get(claveUsuario));
						sbDatos.append("&")
							   .append(razonSocialProveedor).append(coma)
					           .append(monto).append(coma)
					           .append(tipoMoneda).append(coma);
						
						mapaUsuario.put(claveUsuario, sbDatos.toString());
						sbDatos.setLength(0);
	         		}else{
        				sbDatos.append(razonSocialProveedor).append(coma)
					       .append(monto).append(coma)
					       .append(tipoMoneda).append(coma);
        				mapaUsuario.put(claveUsuario, sbDatos.toString());
						sbDatos.setLength(0);
						llavesUsuario.add(claveUsuario);
        			}
	        	}
	        } // fin while
	      
			logger.info("Notificacion al supervisor ----->"+tareasForm.getNotificacion() );
			if (!mapaUsuario.isEmpty()){
				 String cadHTML = null;
				 //CorreoBean.passwordEmisorMensaje = empresasForm.getPwdCorreo();
		         //CorreoBean.usuarioEmisorMensaje = empresasForm.getEmailDominio();
		         String correoUsuario [] = {""};
		         String correoSup [] = {""};
				 for (int x = 0; x < llavesUsuario.size(); x++){
					 claveUsuario = llavesUsuario.get(x);
					 emailUsuario  = UtilsBD.emailUsuario(conEmpresa, empresasForm.getEsquema(), String.valueOf(claveUsuario), "PROVEEDOR");
					 correoUsuario[0] = emailUsuario[0]; 
					 mensajeCorreo = armarMensajeCorreo(tareasForm.getMensaje(),"", empresasForm.getNombreLargo(), emailUsuario[1]);
					 
					 cadHTML = generaHTML(mapaUsuario.get(claveUsuario), empresasForm.getNombreLargo(), mensajeCorreo);
					 if (!emailUsuario[2].equals("0")){ // no se notifica al supervisor
						 if ("S".equalsIgnoreCase(tareasForm.getNotificacion())){
							 emailSupervisor  = UtilsBD.emailUsuario(conEmpresa, empresasForm.getEsquema(), emailUsuario[2], "SUPERVISOR");
						 }else{
							 emailSupervisor[0] = "";
							 emailSupervisor[1] = "";
							 emailSupervisor[2] = "";
						 }

						 correoSup[0] = emailSupervisor[0];
					 }
					// CorreoBean.enviarMensajeSupervisor(null, cadHTML, false, emailUsuario[0], emailSupervisor[0], tareasForm.getAsunto());
					 //CorreoBean.enviarCorreo(null, cadHTML, false, correoUsuario, correoSup, tareasForm.getAsunto(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
					 EnviaCorreoGrid.enviarCorreo(null, cadHTML, false, correoUsuario, correoSup, tareasForm.getSubject(), empresasForm.getEmailDominio(), empresasForm.getPwdCorreo());
					 
					 correoSup[0] = "";
				 }
				 
				 
			}
		}
        catch(Exception e){
        	Utils.imprimeLog("", e);
        }
		finally{
	        try{
	            if(rs != null)
	                rs.close();
	            rs = null;
	            if(stmt != null)
	                stmt.close();
	            stmt = null;
	        }
	        catch(Exception e){
	            stmt = null;
	        }
        }
	}
	
	
	
	
	public String armarMensajeCorreo(String mensajeOriginal, String razonSocial, String razonSocialEmpresa, String nombreUsuario){
		String mensajeFinal = "";
		try{
			mensajeFinal = mensajeOriginal.replace("<<razonSocial>>", razonSocial)
									   .replace("<<razonSocialEmpresa>>", razonSocialEmpresa)
									   //.replace("<<factura>>", folioEmpresa)
									   .replace("<<usuario>>", nombreUsuario);
		}catch(Exception e){
			Utils.imprimeLog("", e);
		}
		return mensajeFinal;
	}
	
	
	
	
	public String generaHTML (String cadenaHTML, String razonSocialEmpresa, String mensajeCorreo){
		StringBuffer sbHTML = new StringBuffer();
		StringBuffer sbUSD = new StringBuffer();
		StringBuffer sbMEX = new StringBuffer();
		try {
			
			sbHTML.append("<table width='1000' cellpadding='0' cellspacing='0' border='0' style='font-family: Verdana;'>")
			      .append("<tr>")
			      .append("<td align='left' height='24' colspan='3' style='font-size: 14px; color: #00539f;'>")
			      	.append(mensajeCorreo)
			      .append("</td>")
			    .append("</tr>")
			
			    .append("<tr>")
			    .append(" <td align='center' height='24' colspan='3' style='font-size: 16px; color: #00539f; font-weight: bold;;'>")
			    .append(razonSocialEmpresa)
			    .append(" </td>")
			    .append("</tr>")
			    .append("<tr>")
			      .append(" <td align='center' height='24' colspan='3' style='font-size: 12px; color: #00539f;'>")
			        .append("Relaci�n de Ordenes de Compra")
			      .append("</td>")
			    .append("</tr>")
			    .append("<tr style='font-size: 14px; color: white; font-weight: bold; background-color: #00539f;'>")
			    	.append("<td width='800px' align='center'>Raz�n Social</td>")
			    	.append("<td width='200px' align='center' height='24'>Monto</td>")
			    .append("</tr>");
			
			
			double totalFacturaUSD = 0;
			double totalFacturaMEX = 0;
			DecimalFormat decimal = new DecimalFormat("###,###.##");
			
			String [] cadHTML = cadenaHTML.split("&");
			String [] cadRegistro = null;
			double total = 0;
			for (int x = 0; x < cadHTML.length; x++){
				cadRegistro = cadHTML[x].split(coma);
				
				if ("USD".equalsIgnoreCase(cadRegistro[2])){
					total = Double.parseDouble(cadRegistro[1]);
					totalFacturaUSD+= total; 
					sbUSD.append("<tr style='font-size: 12px; color: #00539f; background-color: #dddddd; '>")
				      .append("<td>").append(cadRegistro[0]).append("</td>")
				      .append("<td height='24' align='right'>").append(decimal.format(total)).append("</td>")
				    .append("</tr>");
				}else{
					total = Double.parseDouble(cadRegistro[1]);
					totalFacturaMEX+=total;
					sbMEX.append("<tr style='font-size: 12px; color: #00539f; background-color: #dddddd; '>")
				      .append("<td>").append(cadRegistro[0]).append("</td>")
				      .append("<td height='24' align='right'>").append(decimal.format(total)).append("</td>")
				    .append("</tr>");
				}
			}
			
			if (sbUSD.length() > 0){
				sbUSD.append("<tr style='font-size: 14px; font-weight: bold;'>")
			           .append("<td height='50' align='right' >Total \"USD\"</td>")
			           .append("<td align='right'>").append(decimal.format(totalFacturaUSD)).append("</td>")
			         .append("</tr>");
			}
			if (sbMEX.length() > 0){
				sbMEX.append("<tr style='font-size: 14px; font-weight: bold;'>")
			           .append("<td height='50' align='right' >Total \"MXN\"</td>")
			           .append("<td align='right'>").append(decimal.format(totalFacturaMEX)).append("</td>")
			         .append("</tr>");
			}
			sbHTML.append(sbUSD).append(sbMEX)
				  .append("</table>");
			  
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
}
