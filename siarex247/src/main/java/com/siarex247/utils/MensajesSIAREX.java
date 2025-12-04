package com.siarex247.utils;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.siarex247.seguridad.Lenguaje.LenguajeBean;


public class MensajesSIAREX {
	public static final Logger logger = Logger.getLogger("siarex");
	
/*	
	public static final String MENSAJE1 =  "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada con exito debido a no coincide el RFC registrado en nuestra base de datos contra el que usted esta incluyendo en las facturas .xml";
	public static final String MENSAJE2 =  "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada con exito debido a que el RFC receptor no corresponde al RFC de nuestra empresa.";
	public static final String MENSAJE3 =  "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada con exito debido a que el Folio Fiscal de su archivo .xml no coincide contra el Folio Fiscal de su factura.PDF.";
	public static final String MENSAJE4 =  "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada con exito debido a que no coincide el monto de su factura contra la orden de compra.";
	public static final String MENSAJE5 =  "Estimado proveedor su orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada debido a que el n(u)mero de folio ya se encuentra en nuestra base de datos";
	public static final String MENSAJE6 =  "Estimado proveedor le informamos que su factura no pudo ser procesada con exito, debido a que su orden de compra <<FOLIO_FACTURA>> no existe en nuestra base de datos.";
	public static final String MENSAJE7 =  "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada con exito debido a que no se encontro el sello digital en su archivo .xml";
	public static final String MENSAJE8 =  "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada con exito debido a que el tipo de moneda proporcionado en el archivo .xml no se encontro en la base de datos.";
	public static final String MENSAJE9 =  "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada con exito debido a que el tipo de moneda proporcionado en el archivo .xml no es igual al registrado en la base de datos";
	public static final String MENSAJE10 = "Estimado proveedor le informamos que la orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada con exito debido a que no cumple con los requisitos estipulados en el SAT con respecto a la miscelanea fiscal del anexo 24.";
	public static final String MENSAJE11 = "Estimado proveedor <<RAZON_EMISOR>> le informamos que su orden de compra <<FOLIO_FACTURA>> no pudo ser procesada con (e)xito debido a que el uso del CFDI utilizado en su facturaci(o)n electr(o)nica no concuerda con el registrado por la empresa <<RAZON_RECEPTOR>>, le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.";
	public static final String MENSAJE12 = "Estimado proveedor <<RAZON_EMISOR>> le informamos que su orden de compra <<FOLIO_FACTURA>> no pudo ser procesada con (e)xito debido a que la clave del producto o servicio utilizado en su facturaci(o)n electr(o)nica no concuerda con el registrado por la empresa <<RAZON_RECEPTOR>>, le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.";
	public static final String MENSAJE13 = "Estimado proveedor <<RAZON_EMISOR>> le informamos que su orden de compra <<FOLIO_FACTURA>> no pudo ser procesada con (e)xito debido a que la empresa <<RAZON_RECEPTOR>>,  aun no configura sus RFC para recibir facturaci(o)n electr(o)nica con versi(o)n 3.3 le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.";
	public static final String MENSAJE14 = "Estimado proveedor <<RAZON_EMISOR>> le informamos que su orden de compra <<FOLIO_FACTURA>> no pudo ser procesada con (e)xito debido a que el uso del CFDI utilizado en su facturaci(o)n electr(o)nica no concuerda con el registrado por la empresa <<RAZON_RECEPTOR>>, le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.";
	public static final String MENSAJE15 = "Estimado proveedor <<RAZON_EMISOR>> le informamos que su orden de compra <<FOLIO_FACTURA>> no pudo ser procesada con (e)xito debido a que la clave del producto o servicio utilizado en su facturaci(o)n electr(o)nica no concuerda con el registrado por la empresa <<RAZON_RECEPTOR>>, le sugerimos ponerse en contacto con el personal de cuentas por pagar y aclarar esta situaci(o)n.";
	public static final String MENSAJE16 = "Estimado proveedor le informamos que su orden de compra <<FOLIO_FACTURA>> se encuentra en proceso de validacion y una vez que sea analizada le enviaremos un correo electronico indicando el estado de su factura.";
	public static final String MENSAJE17 = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra <<FOLIO_FACTURA>> fue procesada exitosamente y sera pagada de acuerdo a nuestro calendario de pagos.";
	public static final String MENSAJE18 = "Estimado proveedor le informamos que su factura no pudo ser procesada con exito, debido a que su orden de compra <<FOLIO_FACTURA>> no existe en nuestra base de datos.";
	public static final String MENSAJE19 = "Estimado proveedor le informamos que la factura correndiente a la orden de compra <<FOLIO_FACTURA>> no pudo ser procesada con exito debido a que ya se encuentra registrada y asignada a la factura con numero <<SERIE_FOLIO>>";
	public static final String MENSAJE20 = "Su factura no pudo ser procesada, favor de verificar la informacion e intentarlo de nuevo.";
	public static final String MENSAJE21 = "Estimado proveedor le informamos que su factura no pudo ser procesada con exito, debido a que su orden de compra <<FOLIO_FACTURA>> no existe en nuestra base de datos.";

	public static final String MENSAJE41 = "Estimado proveedor le informamos que la factura correndiente a la orden de compra <<FOLIO_FACTURA>> no pudo ser procesada con exito debido a que ya se encuentra registrada y asignada al folio UUID <<SERIE_FOLIO>>";
	public static final String MENSAJE42 = "Estimado proveedor le informamos que su archivo .XML, no puede ser procesado con (e)xito debido a que la informaci(o)n proporcionada no corresponde a un complemento de pago, favor de verificar la informacion e intentarlo de nuevo.";
	public static final String MENSAJE43 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesado con éxito debido a que la información proporcionada en el archivo .XML es incorrecta, esto debido a que no se está incluyendo el nodo “DoctoRelacionado” solicitado por el SAT de manera obligatoria.";
	public static final String MENSAJE44 = "Estimado proveedor le informamos que su complemento de pago no pudo ser procesado exitosamente, debido a que en los archivos .xml de los complementos de pago deben de contener 1 sola etiqueta o nodo de nombre “Complemento” y en su archivo .xml se están incluyendo 2 etiquetas de nombre “Complemento”.";
	public static final String MENSAJE45 = "Estimado proveedor le informamos que su archivo .XML, no puede ser procesado con éxito debido a que la información proporcionada no corresponde a una factura de tipo Ingreso, favor de verificar la información e intentarlo de nuevo.";
	
	
	**************************  VALIDCIONES DE USO CFDI **************
	public static final String MENSAJE60 = "Estimado proveedor le informamos que su archivo .XML, no puede ser procesado con (e)xito debido a que la informaci(o)n proporcionada en el XML no contiene algunas etiquetas de Conceptos de Servicio";
	
	
	*************** AMERICANOS ****************
	public static final String MENSAJE22 = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a que no coincide el monto de su factura contra la orden de compra."
	public static final String MENSAJE23 = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a que el tipo de moneda proporcionado en el archivo .txt no se encontro en la base de datos."
	public static final String MENSAJE24 = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+", no pudo ser procesada con exito debido a que el formato de fecha proporcionado en el .txt no es correcto debe proporcionarlo con el siguiente formato (mm/dd/yyyy)."
	public static final String MENSAJE25 = "Estimado proveedor le informamos que la factura correndiente a la orden de compra "+folioFactura+" no pudo ser procesada con exito debido a que ya se encuentra registrada y asignada a la factura con numero "+ordenesAmeForm.getSerieOrden()"
	public static final String MENSAJE26 = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra "+folioFactura+" fue procesada exitosamente.";
	public static final String MENSAJE27 = "Estimado proveedor <<RAZON_EMISOR>> le informamos que su orden de compra <<FOLIO_FACTURA>> no pudo ser procesada debido a que el n(u)mero de folio que se incluye en su factura se encuentra cancelado en el SAT Servicio de administraci(o)n tributaria.";
	public static final String MENSAJE33 = "Estimado proveedor le informamos que la factura correndiente a la orden de compra <<FOLIO_FACTURA>> no pudo ser procesada con exito debido a que ya se encuentra registrada y asignada a la factura con numero <<SERIE_FOLIO>>";
	
	******************* SUBJECT ***************
	public static final String SUBJECT1 = "Factura Procesada con Exito";
	
	public static final String SUBJECT2 = "Error al procesar su factura.";
	

	
	**************** PROVEEDORES ******************
	public static final String MENSAJE28 = "Estimado proveedor << RAZON_EMISOR >>, por medio del presente le hacemos llegar su informacion de acceso al sistema SIAREX. Presione el siguiente link https://www.contrarecibo.com" para ingresar.";
	public static final String SUBJECT3 = "Acceso al sistema SIAREX";


	**************** COMPLEMENTARIA ******************
	public static final String MENSAJE29 = "Estimado Proveedores, por medio del presente le informamos que su archivo complemento de pago, se encuentra cancelado en el SAT Servicio de Administraci(o)n Tributaria. ";
	public static final String MENSAJE30 = "Estimado proveedor, le ha surgido un error al momento de procesar los archivos.";
	public static final String MENSAJE31 = "Estimado proveedor, le informamos que su solicitud ha sido procesada satisfactoriamente.";
	public static final String MENSAJE32 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesada con exito debido a que la informacion proporcionada en el archivo es incorrecta, favor de proporcionar validar su archivo de complemento de pago.";
	public static final String MENSAJE34 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesado con (e)xito debido a que el folio << FOLIO_FACTURA >> no coincide la fecha de pago vs fecha de pago del complemento.";
	public static final String MENSAJE35 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesado con éxito debido a que el monto total de su complemento de pago no coincide con el monto total de las facturas a las cuales usted desea asignar este complemento.";
	public static final String MENSAJE36 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesado con (e)xito debido a el estatus de la orden de compra << FOLIO_FACTURA >>, se encuentra en un estatus incorrecto para su procesamiento.";
	public static final String MENSAJE37 = "Estimado proveedor le informamos que su factura correspondiente a la orden de compra <<FOLIO_FACTURA>>, no pudo ser procesada con exito debido a aun tiene pendientes complementos de pago.";
	public static final String MENSAJE38 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesado con (e)xito debido a existen folios UUID en su archivo .XML que no estan asociados a ninguna factura de pago.";
	public static final String MENSAJE40 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesado con (e)xito debido a que los folio(s) ya fueron asignados a otro complemento de pago.";
	public static final String MENSAJE91 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesado con (e)xito debido a que el RFC Emisor del complemento es diferente al de la factura con folio << UUID_FACTURA >>";
	public static final String MENSAJE92 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesado con (e)xito debido a que el Tipo de Moneda de su factura << UUID_FACTURA >>, es diferente al de su complemento de pago.;
	public static final String MENSAJE93 = "Estimado proveedor le informamos que su complemento de pago, no pudo ser procesado con (e)xito debido a que el Total Pagado de su factura << UUID_FACTURA >>, es diferente al de su complemento de pago.;

	
	
	public static final String MENSAJE39 = "Estimado proveedor le informamos que su orden de compra << FOLIO_FACTURA >>, no pudo ser procesada debido a que ya se encuentra registrada en el sistema una factura con el mismo n(u)mero de folio y serie";
		
	public static final String SUBJECT4 = "Error al procesar su Archivo";


	*******************  NOTAS DE CREDITO **************************
	public static final String MENSAJE46 = "Estimado proveedor le informamos que su nota de credito, no pudo ser procesada con (e)xito debido a que la informaci(o)n proporcionada en el archivo .XML es incorrecta, favor de proporcionar o validar su archivo .XML de nota de credito.";
	public static final String MENSAJE47 = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que esta no se encuentra vigente ante el SAT";
    public static final String MENSAJE48 = "Estimado proveedor le informamos que su nota de credito no pudo ser procesado exitosamente, debido a que en el archivo .xml de la nota de credito deben de contener la etiqueta o nodo de nombre \"CfdiRelacionado\", validar que en su archivo .xml se incluya esta etiqueta."
    public static final String MENSAJE49 = "Estimado proveedor le informamos que su archivo .XML, no puede ser procesado con (e)xito debido a que la informaci(o)n proporcionada no corresponde a una Nota de Credito, favor de verificar la informaci(o)n e intentarlo de nuevo.";
    public static final String MENSAJE50 = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que el folio fiscal  \"<< UUID_INCLUIDO >>\" incluido en su .XML no se encuentra registrado en nuestra base de datos"
    public static final String MENSAJE51 = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que el folio fiscal relacionado ya se encuentra procesado y asignado al folio fiscal UUID << UUID_ASIGNADO >>";
    public static final String MENSAJE52 = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que el folio fiscal relacionado ya se encuentra procesado y asignado"
    public static final String MENSAJE53 = "Estimado proveedor, le informamos que su nota de cr(e)dito fue procesada de manera exitosa y esta ser(a) aplicada dentro nuestro est(a)ndares de pago, presione el bot(o)n de \"Asignar\" para terminar el proceso."
    public static final String MENSAJE54 = "Estimado proveedor, le informamos que su nota de credito fue rechazada debido a que el tipo de moneda registrado en nuestra base de datos es diferente al de su archivo .XML";
    public static final String MENSAJE61 = "Estimado proveedor le informamos que su nota de credito, no pudo ser procesada con exito debido a que la informacion proporcionada en el archivo es incorrecta, favor de proporcionar validar su archivo de nota de credito.";
    public static final String MENSAJE87 = "Estimado proveedor, le informamos que su nota de cr(e)dito fue rechazada debido a que el folio de la orden de compra << FOLIO_FACTURA >> se encuentra en estatus "A6-Factura con complemento de pago" por lo cual le sugerimos solicitar al (a)rea de cuentas por pagar la eliminaci(o)n del complemento de pago asignado a esta orden de compra"
    public static final String MENSAJE88  = "Estimado proveedor, le informamos que su nota de cr(e)dito fue rechazada debido a que el folio de la orden de compra << FOLIO_FACTURA >> se encuentra en estatus de bajo validaci(o)n por lo tanto aun NO es posible asignarle notas de cr(e)dito a esta factura."
    public static final String MENSAJE89  = "Estimado proveedor, le informamos que su nota de credito fue aceptada sin embargo aun esta pendiente la validacion y/o autorizacion por parte del area de cuentas por pagar, en cuanto este proceso se lleve a cabo por parte del usuario se lo haremos saber via correo electronico."
    public static final String MENSAJE90  = "Estimado proveedor, le informamos que su nota de cr(e)dito fue rechazada debido a que el folio de la orden de compra << FOLIO_FACTURA >> se encuentra en estatus incorrecto para asignar notas de cr(e)dito"
        
    
    
    ****************** MENSAJES PARA EL PROCESO DE CARGAS DE ARCHIVOS ***************
	public static final String MENSAJE55 = "El folio de la orden de compra no debe ser << valor_remplazo >>.";        
	public static final String MENSAJE56 = "La columna de empleado debe contener al menos un valor.";
	public static final String MENSAJE57 = "El empleado << valor_remplazo >> no se encontro en la base de datos SIAREX.";
	public static final String MENSAJE58 = "El folio << valor_remplazo >> ya existe en la base de datos.";
	public static final String MENSAJE59 = "Registro guardado satisfactoriamente.";
	
	
	***********************  MENSAJES PARA CERTIFICACION *************************
	public static final String MENSAJE62 = "Estimado proveedor le informamos que la factura correspondiente a la orden de compra << FOLIO_FACTURA >>, NO pudo ser procesada debido a que su razón social aún no cuenta con su opinión positiva otorgada por el SAT, le sugerimos contactar al área de compras y aclarar este punto.";
	public static final String MENSAJE63 = “Estimado proveedor le informamos que la factura correspondiente a la orden de compra << FOLIO_FACTURA >>, NO pudo ser procesada debido a que su razón social aún no cuenta con su certificación de cumplimiento otorgado por el Instituto Mexicano del Seguro Social, le sugerimos contactar al área de compras y aclarar este punto”;


	***********************  MENSAJES PARA CARTA PORTE *************************
	public static final String MENSAJE66 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesado con (e)xito debido a que el monto total de pago no coincide con el monto total de las facturas a las cuales usted desea asignar.";
	public static final String MENSAJE67 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que el folio << FOLIO_FACTURA >> no coincide la fecha de pago vs fecha de pago la carta porte.";
	public static final String MENSAJE68 = "Desea Vincular las Carta Porte, este proceso tardar(a) de acuerdo al n(u)mero de cartas a vincular.";
	public static final String MENSAJE69 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que la informaci(o)n proporcionada en el archivo .XML es incorrecta, favor de proporcionar o validar su archivo .XML de carta porte.";
	public static final String MENSAJE70 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que el tipo de comprobante no contiene la informaci(o)n de carta porte.";
	public static final String MENSAJE71 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que el rfc emisor no tiene asignado un proveedor valido.";
	public static final String MENSAJE72 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que la orden proporcionada no cumple con un estatus valido.";
	public static final String MENSAJE73 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que el proveedor no tiene asignada la carta porte para su validaci(o)n.";
	public static final String MENSAJE74 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que el rfc emisor no se encuentra asignado al catalogo de proveedores externos.";
	public static final String MENSAJE75 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que el documento XML no cuenta con un folio fiscal UUID.";
	public static final String MENSAJE76 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que la orden de compra proporcionado ya tiene asignado un documento CFDI de carta porte";
	public static final String MENSAJE77 = "Estimado proveedor le informamos que su carta porte se encuentra en proceso de validacion y una vez que sea analizada le enviaremos un correo electronico indicando el estado de su factura.";
	public static final String MENSAJE78 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que no cumple con las siguientes etiquetas <<listadoEtiquetas>>";
	public static final String MENSAJE79 = "Estimado proveedor le informamos que su carta porte, no pudo ser procesada con (e)xito debido a que el documento .XML de su factura no contiene el complemento de carta porte.";
	public static final String MENSAJE86 = "Estimado proveedor le informamos que su orden de compra << FOLIO_FACTURA >> no pudo ser procesada exitosamente debido a que el Uso del CFDI indicado en su archivo .xml no existe en el catalogo publicado por el SAT.";
	
    
*/
	
	public String Idlenguaje = "";
	
	public String MENSAJE1  = null;
	public String MENSAJE2  = null;
	public String MENSAJE3  = null;
	public String MENSAJE4  = null;
	public String MENSAJE5  = null;
	public String MENSAJE6  = null;
	public String MENSAJE7  = null;
	public String MENSAJE8  = null;
	public String MENSAJE9  = null;
	public String MENSAJE10 = null;
	public String MENSAJE11 = null;
	public String MENSAJE12 = null;
	public String MENSAJE13 = null;
	public String MENSAJE14 = null;
	public String MENSAJE15 = null;
	public String MENSAJE16 = null;
	public String MENSAJE17 = null;
	//public String MENSAJE18 = null;
	public String MENSAJE19 = null;
	public String MENSAJE20 = null;
	//public String MENSAJE21 = null;
	public String MENSAJE22 = null;
	public String MENSAJE23 = null;
	public String MENSAJE24 = null;
	public String MENSAJE25 = null;
	public String MENSAJE26 = null;
	public String MENSAJE27 = null;
	public String MENSAJE28 = null;
	public String MENSAJE29 = null;
	public String MENSAJE30 = null;
	public String MENSAJE31 = null;
	public String MENSAJE32= null;
	public String MENSAJE33= null;
	public String MENSAJE34= null;
	public String MENSAJE35= null;
	public String MENSAJE36= null;
	public String MENSAJE37= null;
	public String MENSAJE38= null;
	public String MENSAJE39= null;
	public String MENSAJE40= null;
	public String MENSAJE41= null;
	public String MENSAJE42= null;
	public String MENSAJE43= null;
	public String MENSAJE44= null;
	public String MENSAJE45= null;
	public String MENSAJE46= null;
	public String MENSAJE47= null;
	public String MENSAJE48= null;
	public String MENSAJE49= null;
	public String MENSAJE50= null;
	public String MENSAJE51= null;
	public String MENSAJE52= null;
	public String MENSAJE53= null;
	public String MENSAJE54= null;
	public String MENSAJE55= null;
	public String MENSAJE56= null;
	public String MENSAJE57= null;
	public String MENSAJE58= null;
	public String MENSAJE59= null;
	public String MENSAJE60= null;
	public String MENSAJE61= null;
	public String MENSAJE62= null;
	public String MENSAJE63= null;
	
	public String MENSAJE66= null;
	public String MENSAJE67= null;
	public String MENSAJE68= null;
	
	public String MENSAJE69= null;
	public String MENSAJE70= null;
	public String MENSAJE71= null;
	public String MENSAJE72= null;
	public String MENSAJE73= null;
	public String MENSAJE74= null;
	public String MENSAJE75= null;
	public String MENSAJE76= null;
	public String MENSAJE77= null;
	public String MENSAJE78= null;
	public String MENSAJE79= null;
	public String MENSAJE80= null;
	public String MENSAJE81= null;
	public String MENSAJE82= null;
	public String MENSAJE83= null;
	public String MENSAJE84= null;
	public String MENSAJE85= null;
	public String MENSAJE86= null;
	public String MENSAJE87= null;
	public String MENSAJE88= null;
	public String MENSAJE89= null;
	public String MENSAJE90= null;
	public String MENSAJE91= null;
	public String MENSAJE92= null;
	public String MENSAJE93= null;
	
	
	
	public String SUBJECT1 = null;
	public String SUBJECT2 = null;
	public String SUBJECT3 = null;
	public String SUBJECT4 = null;
	
	
	
	public MensajesSIAREX(String LEN) {
		this.Idlenguaje = LEN;
		MENSAJE1  = getMensaje("MSG1");
		MENSAJE2  = getMensaje("MSG2");
		MENSAJE3  = getMensaje("MSG3");
		MENSAJE4  = getMensaje("MSG4");
		MENSAJE5  = getMensaje("MSG5");
		MENSAJE6  = getMensaje("MSG6");
		MENSAJE7  = getMensaje("MSG7");
		MENSAJE8  = getMensaje("MSG8");
		MENSAJE9  = getMensaje("MSG9");
		MENSAJE10 = getMensaje("MSG10");
		MENSAJE11 = getMensaje("MSG11");
		MENSAJE12 = getMensaje("MSG12");
		MENSAJE13 = getMensaje("MSG13");
		MENSAJE14 = getMensaje("MSG14");
		MENSAJE15 = getMensaje("MSG15");
		MENSAJE16 = getMensaje("MSG16");
		MENSAJE17 = getMensaje("MSG17");
		//MENSAJE18 = getMensaje("MSG18");
		MENSAJE19 = getMensaje("MSG19");
		MENSAJE20 = getMensaje("MSG20");
		//MENSAJE21 = getMensaje("MSG21");
		MENSAJE22 = getMensaje("MSG22");
		MENSAJE23 = getMensaje("MSG23");
		MENSAJE24 = getMensaje("MSG24");
		MENSAJE25 = getMensaje("MSG25");
		MENSAJE26 = getMensaje("MSG26");
		MENSAJE27 = getMensaje("MSG27");
		MENSAJE28 = getMensaje("MSG28");
		MENSAJE29 = getMensaje("MSG29");
		MENSAJE30 = getMensaje("MSG30");
		MENSAJE31 = getMensaje("MSG31");
		MENSAJE32 = getMensaje("MSG32");
		MENSAJE33 = getMensaje("MSG33");
		MENSAJE34 = getMensaje("MSG34");
		MENSAJE35 = getMensaje("MSG35");
		MENSAJE36 = getMensaje("MSG36");
		MENSAJE37 = getMensaje("MSG37");
		MENSAJE38 = getMensaje("MSG38");
		MENSAJE39 = getMensaje("MSG39");
		MENSAJE40 = getMensaje("MSG40");
		MENSAJE41 = getMensaje("MSG41");
		MENSAJE42 = getMensaje("MSG42");
		MENSAJE43 = getMensaje("MSG43");
		MENSAJE44 = getMensaje("MSG44");
		MENSAJE45 = getMensaje("MSG45");
		MENSAJE46 = getMensaje("MSG46");
		MENSAJE47 = getMensaje("MSG47");
		MENSAJE48 = getMensaje("MSG48");
		MENSAJE49 = getMensaje("MSG49");
		MENSAJE50 = getMensaje("MSG50");
		MENSAJE51 = getMensaje("MSG51");
		MENSAJE52 = getMensaje("MSG52");
		MENSAJE53 = getMensaje("MSG53");
		MENSAJE54 = getMensaje("MSG54");
		MENSAJE55 = getMensaje("MSG55");
		MENSAJE56 = getMensaje("MSG56");
		MENSAJE57 = getMensaje("MSG57");
		MENSAJE58 = getMensaje("MSG58");
		MENSAJE59 = getMensaje("MSG59");
		MENSAJE60 = getMensaje("MSG60");
		MENSAJE61 = getMensaje("MSG61");
		MENSAJE62 = getMensaje("MSG62");
		MENSAJE63 = getMensaje("MSG63");
		
		MENSAJE66 = getMensaje("MSG66");
		MENSAJE67 = getMensaje("MSG67");
		MENSAJE68 = getMensaje("MSG68");
		
		MENSAJE69 = getMensaje("MSG69");
		MENSAJE70 = getMensaje("MSG70");
		MENSAJE71 = getMensaje("MSG71");
		MENSAJE72 = getMensaje("MSG72");
		MENSAJE73 = getMensaje("MSG73");
		MENSAJE74 = getMensaje("MSG74");
		MENSAJE75 = getMensaje("MSG75");
		MENSAJE76 = getMensaje("MSG76");
		MENSAJE77 = getMensaje("MSG77");
		MENSAJE78 = getMensaje("MSG78");
		MENSAJE79 = getMensaje("MSG79");
		MENSAJE80 = getMensaje("MSG80");
		MENSAJE81 = getMensaje("MSG81");
		MENSAJE82 = getMensaje("MSG82");
		MENSAJE83 = getMensaje("MSG83");
		MENSAJE84 = getMensaje("MSG84");
		MENSAJE85 = getMensaje("MSG85");
		MENSAJE86 = getMensaje("MSG86");
		MENSAJE87 = getMensaje("MSG87");
		MENSAJE88 = getMensaje("MSG88");
		MENSAJE89 = getMensaje("MSG89");
		MENSAJE90 = getMensaje("MSG90");
		MENSAJE91 = getMensaje("MSG91");
		MENSAJE92 = getMensaje("MSG92");
		MENSAJE93 = getMensaje("MSG93");
		
		
		SUBJECT1 = getMensaje("SUB1");
		SUBJECT2 = getMensaje("SUB2");
		SUBJECT3 = getMensaje("SUB3");
		SUBJECT4 = getMensaje("SUB4");
		
	}
	
	
	
	
	private String getMensaje(String idEtiqueta) {
		LenguajeBean lenBean = LenguajeBean.instance();
		try {
			 HashMap <String, String> mapaLen = lenBean.obtenerEtiquetas(this.Idlenguaje, "PROCE");
			 if (mapaLen.get(idEtiqueta) == null) {
				 return "";
			 }
			 return mapaLen.get(idEtiqueta);
		}catch(Exception e) {
			Utils.imprimeLog("", e);
		}
		return "";
	}
	
}
