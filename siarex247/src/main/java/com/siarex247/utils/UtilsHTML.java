package com.siarex247.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.siarex247.catalogos.Proveedores.ProveedoresForm;
import com.siarex247.cumplimientoFiscal.HistorialPagos.HistorialPagosForm;
import com.siarex247.visor.VisorOrdenes.ComplementosForm;
import com.siarex247.visor.VisorOrdenes.NotaCreditoForm;

public class UtilsHTML {

	public static final Logger logger = Logger.getLogger("siarex247");
	
	private static final String coma = ";";
	

	public static String generaHTMLAcceso(String nombreUsuario, String urlAcceso, String dominio){
		StringBuffer sbHTML = new StringBuffer();
		try {
			
			sbHTML.append("<h3>").append(nombreUsuario).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Recibimos una petici(o)n para registrar tu contraseña en tu cuenta de SIAREX.</label></p>")
			  .append("<p><label>Para registrar tu contraseña, ingresa en el siguiente link <a href=").append(urlAcceso).append(">").append(dominio).append(" </a> </label></p> ");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLAccesoEnvioProveedor(String nombreUsuario, String urlAcceso, String dominio){
		StringBuffer sbHTML = new StringBuffer();
		try {
			
			sbHTML.append("<h3>").append(nombreUsuario).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Hacemos llegar la informaci(o)n correspondiente para el acceso a SIAREX.</label></p>")
			  .append("<p><label>Favor de seguir las instrucciones anexas en el archivo .PDF e ingresar al siguiente link <a href=").append(urlAcceso).append(">").append(dominio).append(" </a> </label></p> ");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTML (String cadenaProveedor, String razonSocial, String mensajeRazon, String fechaPago){
		StringBuffer sbHTML = new StringBuffer();
		StringBuffer sbUSD = new StringBuffer();
		StringBuffer sbMEX = new StringBuffer();
		try {
			String [] cadProv = cadenaProveedor.split("&");
			String [] cadRegistro = null;
			Integer listaOrdenada [] = new Integer[cadProv.length];
			
			for (int x = 0; x < cadProv.length; x++){
				cadRegistro = cadProv[x].split(coma);
				//listaOrdenes.add(cadRegistro[4].trim()); // se agrega el numero de orden de compra
				listaOrdenada[x] = Integer.parseInt(cadRegistro[4].trim());
			}
			
			Arrays.sort(listaOrdenada); // se orden los registros
			
			StringBuffer mensajeFinal = new StringBuffer();
			
			if (listaOrdenada.length == 1){
				mensajeFinal.append("Estimado proveedor ")
						    .append(mensajeRazon)
						    .append(", le informamos que la factura del siguiente listado ha sido pagada satisfactoriamente, con fecha de pago del d(i)a ")
						    .append(fechaPago.substring(0, 10));
			}else{
				mensajeFinal.append("Estimado proveedor ")
			    			.append(mensajeRazon)
			    			.append(", le informamos que las facturas del siguiente listado han sido pagadas satisfactoriamente, con fecha de pago del d(i)a ")
						    .append(fechaPago.substring(0, 10));
			}
			sbHTML.append("<table width='1000' cellpadding='1' cellspacing='1' border='0' style='font-family: Verdana;'>")
			    .append("<tr>")
			    .append(" <td align='center' height='24' colspan='4' style='font-size: 16px; color: #00539f;'>")
			    .append(mensajeFinal)
			    .append(" </td>")
			    .append("</tr>")
			    .append("<tr><td align='center' height='15' colspan='4' style='font-size: 16px;'>&nbsp;</td></tr>")
			    .append("<tr>")
			    .append(" <td align='center' height='24' colspan='4' style='font-size: 16px; color: #00539f; font-weight: bold;'>")
			    .append(razonSocial)
			    .append(" </td>")
			    .append("</tr>")
			    .append("<tr>")
			      .append(" <td align='center' height='24' colspan='4' style='font-size: 12px; color: #00539f;'>")
			        .append("Relaci(o)n de Facturas Pagadas")
			      .append("</td>")
			    .append("</tr>")
			    .append("<tr style='font-size: 14px; color: white; font-weight: bold; background-color: #00539f;'>")
			      .append("<td width='200px' align='center' height='24'>Orden de Compra</td>")
			      .append("<td width='100px' align='center' height='24'>Factura</td>")
			      .append("<td width='400px' align='center'>UUID</td>")
			      .append("<td width='150px' align='center'>Monto</td>")
			    .append("</tr>");
			
			double totalFacturaUSD = 0;
			double totalFacturaMEX = 0;
			DecimalFormat decimal = new DecimalFormat("###,###.##");
			
			double total = 0;
			cadProv = cadenaProveedor.split("&");
			String numeroOrden = null;
			String tipoOrden = null;
			HashMap<String, String> mapaTipos = new HashMap<String, String>();
			for (int y = 0; y < listaOrdenada.length; y++){
				numeroOrden = String.valueOf(listaOrdenada[y]);
				for (int x = 0; x < cadProv.length; x++){
					cadRegistro = cadProv[x].split(coma);
					tipoOrden = cadRegistro[5];
					
					if (numeroOrden.equalsIgnoreCase(cadRegistro[4])){
						if ("USD".equalsIgnoreCase(cadRegistro[3])){
							if ("N".equalsIgnoreCase(tipoOrden)){
								total = Double.parseDouble(cadRegistro[2]);
								totalFacturaUSD+= total; 	
							}else{
								if (!mapaTipos.containsKey(tipoOrden)){
									total = Double.parseDouble(cadRegistro[2]);
									totalFacturaUSD+= total;
									mapaTipos.put(tipoOrden, tipoOrden);	
								}
							}
							
							
							sbUSD.append("<tr style='font-size: 12px; color: #00539f; background-color: #dddddd; '>")
							  .append("<td height='24' align='center'>").append(cadRegistro[4]).append("</td>")
						      .append("<td height='24'>").append(cadRegistro[0]).append("</td>")
						      .append("<td>").append(cadRegistro[1]).append("</td>")
						      .append("<td align='right'>").append(decimal.format(total)).append("</td>")
						    .append("</tr>");
						}else{
							if ("N".equalsIgnoreCase(tipoOrden)){
								total = Double.parseDouble(cadRegistro[2]);
								totalFacturaMEX+=total; 	
							}else{
								if (!mapaTipos.containsKey(tipoOrden)){
									total = Double.parseDouble(cadRegistro[2]);
									totalFacturaMEX+=total;
									mapaTipos.put(tipoOrden, tipoOrden);	
								}
							}

							sbMEX.append("<tr style='font-size: 12px; color: #00539f; background-color: #dddddd; '>")
							  .append("<td height='24' align='center'>").append(cadRegistro[4]).append("</td>")
						      .append("<td height='24'>").append(cadRegistro[0]).append("</td>")
						      .append("<td>").append(cadRegistro[1]).append("</td>")
						      .append("<td align='right'>").append(decimal.format(total)).append("</td>")
						    .append("</tr>");
						}
						break;
					} // fin numeroOrden == cadRegistro[4]
				}
				
			}
			
			if (sbUSD.length() > 0){
				sbUSD.append("<tr style='font-size: 14px; font-weight: bold;'>")
			           .append("<td colspan='3' height='50' align='right' >Total Pagos \"USD\"</td>")
			           .append("<td align='right'>").append(decimal.format(totalFacturaUSD)).append("</td>")
			         .append("</tr>");
			}
			if (sbMEX.length() > 0){
				sbMEX.append("<tr style='font-size: 14px; font-weight: bold;'>")
			           .append("<td colspan='3' height='50' align='right' >Total Pagos \"MXN\"</td>")
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
	
	
	
	public static String generaHTMLExport(String nombreEmpleado, String urlAcceso, String dominio){
		StringBuffer sbHTML = new StringBuffer();
		try {
			
			sbHTML.append("<h3>").append(nombreEmpleado).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Recibimos una petici(o)n para realizar la descarga de las facturas.</label></p>")
			  .append("<p><label> Presione el siguiente link para iniciar la descarga <a href=").append(urlAcceso).append(">").append(dominio).append(" </a> </label></p> ");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLNuevaOrden (String razonSocial, String idEmpleado, String nombreEmpleado, long ordenCompra,  double montoOrden, String mensajeTabla){
		StringBuffer sbHTML = new StringBuffer();
		try {
			String mensajeFinal = Utils.getMensajeValidacion( Utils.getMensajeValidacion(
									  				Utils.getMensajeValidacion( 
									  							Utils.getMensajeValidacion(mensajeTabla, "<< usuario >>", (idEmpleado + " : " +nombreEmpleado)),
									  								"<< ordenCompra >>", String.valueOf(ordenCompra)),
									  									"<< monto >>", NumberFormat.getCurrencyInstance().format(montoOrden)), "<< razonSocial >>",razonSocial);	  
			
			 sbHTML.append("<body>")
			       .append("<p style='font-size: 16px; color: #00539f;'>")
			         .append(mensajeFinal)
			     .append("</p>");
				 sbHTML.append("</body>");
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLFacturaIncorrecta(long folioEmpresa, String razonSocial){
		StringBuffer sbHTML = new StringBuffer();
		try {
			
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioEmpresa+", no pudo ser procesada con exito debido a que la informacion proporcionada en el archivo es incorrecta, favor de proporcionar nuevamente los datos de la orden de compra.</label></p>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	

	public static String generaHTMLClavesCFDIOK(long folioEmpresa, String razonSocial){
		StringBuffer sbHTML = new StringBuffer();
		try {
			
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioEmpresa+" fue procesada exitosamente y sera pagada de acuerdo a nuestro calendario de pagos.</label></p>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLClavesCFDING(long folioEmpresa, String razonSocial){
		StringBuffer sbHTML = new StringBuffer();
		try {
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  //.append("<p><label>Estimado proveedor le informamos que su orden de compra "+folioEmpresa+" no pudo ser procesada exitosamente debido a que la Clave Producto seleccionado en su factura .XML es incorrecta.</label></p>");
			.append("<p><label>Por medio del presente correo le informamos que su orden de compra ").append(folioEmpresa).append(" no pudo ser procesada exitosamente debido a que el uso del cfdi o la clave del producto o servicio es incorrecta.</label></p>")
			.append("<p><label>Favor de corregir la informaci(o)n e intentar subir de nuevo sus archivos de facturaci(o)n.</label></p>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLClavesCartaPorteNG(long folioEmpresa, String razonSocial){
		StringBuffer sbHTML = new StringBuffer();
		try {
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Estimado proveedor le informamos que su orden de compra "+folioEmpresa+" no pudo ser procesada exitosamente debido a que la clave producto seleccionado en su carta porte es incorrecto.");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLClavesCFDIMultipleOK(String foliosEmpresa, String razonSocial){
		StringBuffer sbHTML = new StringBuffer();
		try {
			
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Estimado proveedor le informamos que la factura correspondiente a las ordenes de compra "+foliosEmpresa+" fueron procesadas exitosamente y seran pagadas de acuerdo a nuestro calendario de pagos.</label></p>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLClavesCFDIMultipleNG(String foliosEmpresa, String razonSocial){
		StringBuffer sbHTML = new StringBuffer();
		try {
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Estimado proveedor le informamos que sus ordenes de compra "+foliosEmpresa+" no fueron procesadas exitosamente debido a que las Claves de Producto seleccionado en su factura .xml es incorrecto.</label></p>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLNotaCreditoAceptada (String razonSocial){
		StringBuffer sbHTML = new StringBuffer();
		try {
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Estimado proveedor, le informamos que su nota de cr(e)dito fue procesada de manera exitosa y esta ser(a) aplicada dentro nuestro est(a)ndares de pago.");
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLComplementoEliminar (ArrayList<ComplementosForm> listaDetalle, String razonSocial, String UUID){
		StringBuffer sbHTML = new StringBuffer();
		ComplementosForm compleForm = null;
		try {
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
				  .append("<h3>SIAREX</h3>")
				  .append("<p><label>Estimado proveedor, por medio del presente le informamos que su complemento de pago con folio UUID \"")
				  .append(UUID)
				  .append("\"")
				  .append(", ha sido RECHAZADO por lo que le pedimos de favor comunicarse con el personal indicado para su revisi(o)n.</label></p>");
				  
			sbHTML.append("<table width='1100' cellpadding='1' cellspacing='1' border='0' style='font-family: Verdana;'>")
			    .append("<tr><td align='center' height='15' colspan='2' style='font-size: 16px;'>&nbsp;</td></tr>")
			    .append("<tr>")
			      .append(" <td align='center' height='24' colspan='4' style='font-size: 12px; color: #00539f;'>")
			        .append("Lista de Ordenes de Compra")
			      .append("</td>")
			    .append("</tr>")
			    .append("<tr style='font-size: 14px; color: white; font-weight: bold; background-color: #00539f;'>")
			      .append("<td width='150px' align='center' height='24'>Orden de Compra</td>")
			      .append("<td width='150px' align='center' height='24'>Monto</td>")
			      .append("<td width='150px' align='center' height='24'>Fecha de Pago</td>")
			      .append("<td width='600px' align='center' height='24'>UUID de Factura</td>")
			    .append("</tr>");
			
				for (int y = 0; y < listaDetalle.size(); y++){
					compleForm = listaDetalle.get(y);
					
					sbHTML.append("<tr style='font-size: 12px; color: #00539f; background-color: #dddddd; '>")
						  .append("<td height='24' align='center'>").append(compleForm.getFolioEmpresa()).append("</td>") // FOLIO_EMPRESA
						  .append("<td align='left'>").append(compleForm.getMontoOrden()).append("</td>") // MONTO
						  .append("<td align='left'>").append(compleForm.getFechaPago()).append("</td>") // FECHA_PAGO
						  .append("<td align='left'>").append(compleForm.getUuidOrden()).append("</td>") // UUID_ORDEN
					    .append("</tr>");
					
					
				}
			
			sbHTML.append("</table>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	

	public static String generaHTMLNotaCreditoEliminar (ArrayList<NotaCreditoForm> listaDetalle, String razonSocial, String UUID){
		StringBuffer sbHTML = new StringBuffer();
		NotaCreditoForm notaForm = null;
		try {
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Estimado proveedor, por medio del presente le informamos que su Nota de Cr(e)dito con folio UUID \"")
			  .append(UUID)
			  .append("\"")
			  .append(", ha sido RECHAZADO por lo que le pedimos de favor comunicarse con el personal indicado para su revisi(o)n.</label></p>");
			
			
			sbHTML.append("<table width='1100' cellpadding='1' cellspacing='1' border='0' style='font-family: Verdana;'>")
			    .append("<tr>")
			      .append(" <td align='center' height='24' colspan='4' style='font-size: 12px; color: #00539f;'>")
			        .append("Lista de Ordenes de Compra")
			      .append("</td>")
			    .append("</tr>")
			    .append("<tr style='font-size: 14px; color: white; font-weight: bold; background-color: #00539f;'>")
			      .append("<td width='200px' align='center' height='24'>Orden de Compra</td>")
			      .append("<td width='400px' align='center' height='24'>UUID</td>")
			      .append("<td width='250px' align='right' height='24'>Total Orden de Compra</td>")
			      .append("<td width='250px' align='right' height='24'>Total Nota de Cr(e)dito</td>")
			    .append("</tr>");
			
				for (int y = 0; y < listaDetalle.size(); y++){
					notaForm = listaDetalle.get(y);
					sbHTML.append("<tr style='font-size: 12px; color: #00539f; background-color: #dddddd; '>")
						  .append("<td height='24' align='center'>").append(notaForm.getFolioEmpresa()).append("</td>")
						  .append("<td align='left'>").append(notaForm.getUuidOrden()).append("</td>") 
						  .append("<td align='right'>").append(notaForm.getMontoOrden()).append("</td>") 
						  .append("<td align='right'>").append(notaForm.getMontoNota()).append("</td>") 
					    .append("</tr>");
				}
			
			sbHTML.append("</table>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}

	
	public static String generaHTMLValidarAmericanaOK (String razonSocial, long folioEmpresa){
		StringBuffer sbHTML = new StringBuffer();
		try {
			sbHTML.append("<h3>").append(razonSocial).append("</h3>")
			  .append("<h3>SIAREX</h3>")
			  .append("<p><label>Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioEmpresa+" fue procesada exitosamente y sera pagada de acuerdo a nuestro calendario de pagos.");
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}

	
	
	public static String generaHTMLSolicitudPago (String nombreEmpresa){
		StringBuffer sbHTML = new StringBuffer();
		try {
			
			sbHTML.append("<table width='1100' cellpadding='1' cellspacing='1' border='0' style='font-family: Verdana;'>")
			    .append("<tr><td align='center' height='15' colspan='2' style='font-size: 16px;'>&nbsp;</td></tr>")
			    .append("<tr>")
			    .append(" <td align='left' height='50' colspan='4' style='font-size: 16px; color: #00539f;'> Dear "+nombreEmpresa+" user, ")
			    .append(" </td>")
			    .append("</tr>")
			    .append("<tr>")
			    .append(" <td align='left' height='60' colspan='4' style='font-size: 16px; color: #00539f; '> Vendor has requested PO reception in the system. Please follow up according to the PO Receiving Standard")
			    .append(" </td>")
			    .append("</tr>")
			    .append("<tr>")
			    .append(" <td align='left' height='20' colspan='4' style='font-size: 16px; color: #00539f; '> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Day 0")
			    .append(" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ")
			    .append(" Day 1-3")
			    .append(" </td>")
			    .append("</tr>")
			    .append("<tr>")
			    .append(" <td align='left' height='20' colspan='4' style='font-size: 16px; color: #00539f; '>|---------------------------------| |----------------------------------------------------------|")
			    .append(" </td>")
			    .append("</tr>")
			    .append("<tr>")
			    .append(" <td align='left' height='20' colspan='4' style='font-size: 16px; color: #00539f; '>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Vendor delivers goods &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Confirm reception and receive in Peoplesoft")
			    .append(" </td>")
			    .append("</tr>")
			    .append("<tr>")
			    .append(" <td align='left' height='80' colspan='4' style='font-size: 16px; color: #00539f; '>Falling to follow this standardized times will result in the breaking of a <b><u> compliance policy </b></u> and will be proceeded as such.")
			    .append(" </td>")
			    .append("</tr>");
			sbHTML.append("</table>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLListaNegra(Iterator<JSONObject> iteMapa, int totProveedores, String nombreEmpresa){
		StringBuffer sbHTML = new StringBuffer();
		try {
			
			if (totProveedores > 0) {
				sbHTML.append("<body>");
				sbHTML.append("<h3>Estimado Usuario <b> &&NOMBRE_EMPLEADO&&, </b></h3>")
				  .append("<h3>SIAREX</h3>")
				  .append("<p><label>Por medio del presente correo le informamos que se realizo el proceso de validaci(o)n de proveedores en la lista negra del SAT encontr(a)ndose proveedores en estos listados le sugerimos validar la informaci(o)n a la brevedad.</label></p>");
				
				sbHTML.append("<table style='width: 1200px; border: 1px solid black;' border=0>")
					   .append("<tr style='background-color: #1E95D8; font-weight: bold;' align='center'>");
				
				sbHTML.append("<td width='800' height='24' style='color: white;'>Proveedor</td>")
					  .append("<td width='400' style='color: white;'>Estatus Actual</td>")
					  .append("<td width='400' style='color: white;'>Estatus Periodo 5 años</td>")
					  .append("</tr>");
					  
				JSONObject jsonobj = null;
				while(iteMapa.hasNext()){
					jsonobj = iteMapa.next();
					sbHTML.append("<tr>")
						  .append("<td height='24'>&nbsp;").append(jsonobj.get("RAZON_SOCIAL")).append("</td>")
						  .append("<td>&nbsp;Correcto</td>")
						  .append("<td style='color: red;'>&nbsp;Proveedor Encontrado en Lista Negra</td>")
						  .append("</tr>");
				}
				
				sbHTML.append("</table>");
			sbHTML.append("</body>");
				
			}else {
				sbHTML.append("<body>");
				sbHTML.append("<h3>Estimado Usuario <b> &&NOMBRE_EMPLEADO&&, </b></h3>")
				  .append("<h3>SIAREX</h3>")
				  .append("<p><label>Por medio del presente correo le informamos que se realiz(o) el proceso de validaci(o)n de proveedores en la lista negra del SAT sin encontrar ninguna coincidencia entre los RFC que se incluyen en la base de datos correspondiente a la empresa ")
				      .append(nombreEmpresa)
					  .append(" con los proveedores enlistados en la lista negra del SAT.")
					  .append("</label></p>");
				sbHTML.append("</body>");
			}
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	

	public static String generaHTMLDescargaMasiva(){
		StringBuffer sbHTML = new StringBuffer();
		try {
			
			sbHTML.append("<h3>SIAREX</h3>")
			  .append("<p><label>Hacemos llegar la informaci(o)n correspondiente de UUID que existen en SAT y aun no han sido asignados en Boveda de SIAREX.</label></p>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}

	public static String generaHTMLComplemento (ArrayList<ComplementosForm> listaEnviar, String razonSocial, String mensajeConfigurado){
		String mensajeFinal = "";
		StringBuffer sbTabla = new StringBuffer();
		ComplementosForm complementoForm = null;
		try {
			
			sbTabla.append("<table width='1000' cellpadding='1' cellspacing='1'  style='font-family: Verdana;'>")
				  .append("<tr>")
	   	            .append("<td align='center' height='24' colspan='5' style='font-size: 12px; color: #00539f;'>Relaci(o)n de Facturas sin Complemento de Pago</td>")
	              .append("</tr>")
	              .append("<tr style='font-size: 14px; color: white; font-weight: bold; background-color: #00539f;'>")
	                .append("<td width='200px' align='center' height='24'>Orden de Compra</td>")
	                .append("<td width='100px' align='center' height='24'>Factura</td>")
	                .append("<td width='400px' align='center'>Folio</td>")
	                .append("<td width='200px' align='center'>Fecha de Pago</td>")
	              .append("</tr>");
			String fechaPago = null;
			for(int x = 0; x < listaEnviar.size(); x++) {
				complementoForm = listaEnviar.get(x);
				fechaPago = complementoForm.getFechaPago();
				if (fechaPago != null && fechaPago.length() > 10) {
					fechaPago = fechaPago.substring(0, 10);
				}else {
					fechaPago = complementoForm.getFechaPago();
				}
				     sbTabla.append("<tr style='font-size: 12px; color: #00539f; background-color: #dddddd; '>")
                        	.append("<td height='24' align='center'>").append(complementoForm.getFolioEmpresa()).append("</td>")
                        	.append("<td height='24' align='center'>").append(complementoForm.getSerieOrden()).append("</td>")
                        	.append("<td height='24' align='left'>").append(complementoForm.getUuidComplemento()).append("</td>")
                        	.append("<td height='24' align='left'>").append(fechaPago).append("</td>")
                        .append("</tr>");
			}
			sbTabla.append("</table>");
			mensajeFinal =  mensajeConfigurado.replaceAll("&lt;&lt;table_complementos&gt;&gt;", sbTabla.toString()).replaceAll("&lt;&lt;razon_social&gt;&gt;", razonSocial);
			
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return mensajeFinal;
	}

	
	/*
	public static String generaHTMLComplemento (ArrayList<ComplementosForm> listaEnviar, String razonSocial, String tipoEnvio){
		StringBuffer sbHTML = new StringBuffer();
		StringBuffer sbTabla = new StringBuffer();
		ComplementosForm complementoForm = null;
		try {
			String dominio = UtilsPATH.DOMINIO_PRINCIPAL;
			sbHTML.append("<table width='1600' cellpadding='1' cellspacing='1'  style='font-family: Verdana;'>")
			      .append("<tr>")
			         .append("<td colspan='3' height='30' style='font-size: 16px; color: #00539f; text-align: justify;' >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Estimado proveedor ").append(razonSocial).append(", el motivo de esta notificaci(o)n es para recordarle que su complemento de pago correspondiente al siguiente listado de Facturas a(u)n no ha sido cargada a nuestro portal ")
			         .append("de <a href='https://www.").append(dominio).append("'> https://").append(dominio).append("</a>");
			
			if ("011".equalsIgnoreCase(tipoEnvio)) {
				sbHTML.append(", por tal motivo su cuenta estar(a) detenida y no se podr(a) cargar nuevas facturas para pago hasta que este requerimiento sea completado, en caso de tener alguna duda por favor ponerse en contacto al correo oscar.de.la.fuente@toyota.com.");
			}
			
			sbHTML.append("</td>")
			      .append("</tr>")
			      .append("<tr>")
			         .append("<td height='30'>&nbsp;</td>")
			      .append("</tr>")
			      .append("<tr>")
			          .append("<td width='200'>&nbsp;</td>")
			          .append("<td height='30' style='font-family: Arial;'>")
			                .append("<table width='800' cellpadding='1' cellspacing='1' border='0' style='font-family: Verdana;' >")
			                	.append("<tr>")
			                	     .append("<td align='center' height='24' colspan='4' style='font-size: 12px; color: #00539f;'>Relaci(o)n de Facturas sin Complemento de Pago</td>")
			                    .append("</tr>")
			                    .append("<tr style='font-size: 14px; color: white; font-weight: bold; background-color: #00539f;'>")
			                        .append("<td width='200px' align='center' height='24'>Orden de Compra</td>")
			                        .append("<td width='100px' align='center' height='24'>Factura</td>")
			                        .append("<td width='400px' align='center'>Folio</td>")
			                   .append("</tr>");
			                   
						for(int x = 0; x < listaEnviar.size(); x++) {
							complementoForm = listaEnviar.get(x);
							     sbTabla.append("<tr style='font-size: 12px; color: #00539f; background-color: #dddddd; '>")
			                        	.append("<td height='24' align='center'>").append(complementoForm.getFolioEmpresa()).append("</td>")
			                        	.append("<td height='24' align='center'>").append(complementoForm.getSerieOrden()).append("</td>")
			                        	.append("<td height='24' align='left'>").append(complementoForm.getUuidComplemento()).append("</td>")
			                        .append("</tr>");
						}
				
						
				sbHTML.append(sbTabla.toString());
						
				sbHTML.append("</table>")
			          .append("</td>")
			          .append("<td width='200'>&nbsp;</td>")
			     .append("</tr>")
			     .append("<tr>")
			     	.append("<td colspan='3' height='80' style='font-size: 16px; color: #00539f; text-align: justify;' >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A partir del 01 de septiembre de 2018, dio inicio la obligatoriedad de emitir el CFDI de complemento de pago, de acuerdo a nuestros est(a)ndares de pago y tal como lo establece el Art(i)culo 29, y 29-A del C(o)digo Fiscal de la Federaci(o)n (CFF), as(i) como de las Reglas 2.7.1.32 y 2.7.1.35 de la Resoluci(o)n Miscel(a)nea Fiscal (RMF) vigente, este complemento ser(a) indispensable emitirse cuando se reciba el pago total del CFDI con posterioridad a su emisi(o)n (Pago Diferido), este complemento deber(a) indicarse con M(e)todo de pago: PPD Pago en parcialidades o diferido. </td>")
			     .append("</tr>")
			     .append("<tr>")
			     	.append("<td colspan='3' height='80' style='font-size: 16px; color: #00539f; text-align: justify;' >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;El complemento deber(a) ser enviado por medio de nuestro portal <a href='https://www.").append(dominio).append("'>https://").append(dominio).append("</a> en un plazo no mayor al d(e)cimo d(i)a natural del mes siguiente al que se recibi(o) el pago, con esta herramienta podr(a) verificar cuales facturas ya cuentan con su complemento de pago de manera r(a)pida y sencilla, descargar bases de datos y recibir alertas para su pronta captura. </td>")
			     .append("</tr>")
			     .append("<tr>")
			     	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > Informaci(o)n Bancaria:</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > Toyota Motor Manufacturing de Baja California S de RL de CV</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > TMM020430FD1</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > Banco Mercantil del Norte</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > Dólares ( USD )</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > Cuenta 0174139917</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > CLABE 072027001741399174</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='40'>&nbsp;</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > Toyota Motor Manufacturing de Baja California S de RL de CV</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > TMM020430FD1</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > Banco Mercantil del Norte</td>")
			    .append("</tr>")
			    .append("<tr>")
			    .append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > Pesos ( MXN )</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > Cuenta 0170673589</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td colspan='3' height='20' style='font-size: 16px; color: #00539f; text-align: left: ;' > CLABE 072027001706735896</td>")
			    .append("</tr>")
			    .append("<tr>")
			    	.append("<td>&nbsp;</td>")
			    .append("</tr>");

		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	*/
	
	public static String generaHTMLConciliacionBoveda(String mensajeConfigurado, ProveedoresForm provForm){
		StringBuffer sbHTML = new StringBuffer();
		String mensajeFinal = "";
		try {
			
			mensajeFinal = mensajeConfigurado.replaceAll("<<razon_social>>", provForm.getRazonSocial())
					    .replaceAll("rfc", provForm.getRfc())
					    .replaceAll("vendor_id", provForm.getIdProveedor());
			sbHTML.append("<p><label>").append(mensajeFinal).append("</label></p>");
			
		} catch (Exception e) {
			Utils.imprimeLog("", e);
		}
		return sbHTML.toString();
	}
	
	
	public static String generaHTMLHistorialPagosHP(
	        ArrayList<HistorialPagosForm> listaDetalle,
	        String razonSocial,
	        String rfc,
	        String mensajeConfigurado) {

	    StringBuilder sbTabla = new StringBuilder(1024);
	    StringBuilder sbHTML = new StringBuilder(2048);
	    DecimalFormat decimal = new DecimalFormat("###,###.##");

	    try {

	        // ==============================
	        // 1) INICIO TABLA HTML
	        // ==============================
	        sbTabla.append("<table width='700' style='border-collapse:collapse;font-family:Arial;font-size:12px;' ")
	               .append("border='1' cellpadding='4' cellspacing='0'>");

	        // ENCABEZADOS
	        sbTabla.append("<tr style='background-color:#0070C0;color:#FFFFFF;'>")
	               .append("<th width='300'>UUID Factura</th>")
	               .append("<th width='140'>Tipo Moneda</th>")
	               .append("<th width='120'>Serie</th>")
	               .append("<th width='120'>Folio</th>")
	               .append("<th width='150'>Total</th>")
	               .append("</tr>");

	        // ==============================
	        // 2) DETALLE POR CADA COMPLEMENTO
	        // ==============================
	        if (listaDetalle != null) {
	            for (HistorialPagosForm historicoPagosForm : listaDetalle) {

	                sbTabla.append("<tr>");

	                // UUID FACTURA
	                sbTabla.append("<td>")
	                       .append(Utils.regresaCaracteresHTML(Utils.noNulo(historicoPagosForm.getUuidFactura())))
	                       .append("</td>");

	                // TIPO MONEDA
	                sbTabla.append("<td align='center'>")
	                       .append(Utils.regresaCaracteresHTML(Utils.noNulo(historicoPagosForm.getTipoMoneda())))
	                       .append("</td>");

	                // SERIE
	                sbTabla.append("<td>")
	                       .append(Utils.regresaCaracteresHTML(Utils.noNulo(historicoPagosForm.getSerie())))
	                       .append("</td>");

	                // FOLIO
	                sbTabla.append("<td>")
	                       .append(Utils.regresaCaracteresHTML(Utils.noNulo(historicoPagosForm.getFolio())))
	                       .append("</td>");

	                // TOTAL
	                sbTabla.append("<td align='right'> $ ")
	                       .append(decimal.format(historicoPagosForm.getTotal()))
	                       .append("</td>");

	                sbTabla.append("</tr>");
	            }
	        }

	        sbTabla.append("</table>");

	        // ==============================
	        // 3) REEMPLAZO DE PLACEHOLDERS
	        // ==============================
	        String mensajeTemplate = Utils.noNuloNormal(mensajeConfigurado);

	        String placeholderRazon1 = "&lt;&lt;razon_social&gt;&gt;";
	        String placeholderRazon2 = "<<razon_social>>";

	        String placeholderTabla1 = "&lt;&lt;table_complementos&gt;&gt;";
	        String placeholderTabla2 = "<<table_complementos>>";

	        mensajeTemplate = mensajeTemplate.replace(placeholderRazon1, razonSocial);
	        mensajeTemplate = mensajeTemplate.replace(placeholderRazon2, razonSocial);

	        String tablaHtml = sbTabla.toString();
	        mensajeTemplate = mensajeTemplate.replace(placeholderTabla1, tablaHtml);
	        mensajeTemplate = mensajeTemplate.replace(placeholderTabla2, tablaHtml);

	        sbHTML.append(mensajeTemplate);

	    } catch (Exception e) {
	        Utils.imprimeLog("generaHTMLHistorialPagosHP", e);
	    }

	    return sbHTML.toString();
	}

}

