
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
     <script src="/siarex247/jsp/configuraciones/sistema/configAdicionales/configAdicionales.js"></script>   
</head>


				<form id="form-ConfigAdicionales" class="was-validated" novalidate>
						<div class="p-4 pb-0">

							<div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="txtFaltante" id="CONF_SISTEMA_ETQ36">Predefinir Valor por Serie Faltante</label>
								<div class="col-sm-1">
								  <div class="form-group">
									<input class="form-control" type="text" name="serieFaltante" id="SERIE_FALTANTE" />
								  </div>
								</div>
							  </div>

		
							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ37">Rechazar Complementos de Pago Con Error</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="RECHAZAR_COMPLE" name="rechazarComple" type="checkbox" checked="" />
									</div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ38">Bloquear acceso a proveedores con complementos pendientes</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="BLOQUEAR_PROVEEDORES" name="bloquearProveedores" type="checkbox" checked="" />
									</div>
								</div>
							  </div>


							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="DIA_APARTIR_COMPLE" id="CONF_SISTEMA_ETQ39">Introduzca el dia a partir del cual desea bloquear a los proveedores</label>
								<div class="col-sm-1">
								  <div class="form-group">
									<input class="form-control" type="text" name="diaApartirComple" id="DIA_APARTIR_COMPLE" />
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="FECHA_APARTIR_COMPLE" id="CONF_SISTEMA_ETQ40">A partir de cual fecha se deben tomar en cuenta los complementos de pago</label>
								<div class="col-sm-2">
								  <div class="form-group">
									<input class="form-control datetimepicker flatpickr-input active" id="FECHA_APARTIR_COMPLE"
										name="fechaApartirComple" type="text" placeholder="dd/mm/yyyy"
										data-options='{"disableMobile":true}' />
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ41">Validar Fecha de Pago en Complementos</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="BAND_VALIDFECHAS_COMPLE" name="bandValidFechasComple" type="checkbox" checked="" />
									</div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label"  for="" id="CONF_SISTEMA_ETQ42">Notificar por Correo al Usuario al generar una Nueva Orden de Compra por Visor</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="NOTIF_CORREO_ORDEN" name="notifCorreoOrden" type="checkbox"
											checked="" />
									</div>
								</div>
							  </div>



							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ43">Validar Notas de Credito en el SAT</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="VALIDAR_NOTAS" name="validarNotas" type="checkbox" checked="" />
									</div>
								</div>
							  </div>

							<div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ44">Predifinir valor general para facturas americanas</label>
								<div class="col-sm-1">
			   						<div class="form-check form-switch">
										  <input class="form-check-input" id="PREDEFINIR_VALOR_SERIE" name="predefinirValorSerie" type="checkbox" checked="" />
									</div>
			 					</div>
								 <div class="col-sm-1">
									<input class="form-control" type="text" name="valorSerieAmericanas" id="VALOR_SERIE_AMERICANAS" />
							  	</div>
							</div>
							
							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ45">Permitir acceso general al Generador de Facturas</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="PERMITIR_ACCESO_GENERADOR" name="permitirAccesoGenerador" type="checkbox" checked="" />
									</div>
								</div>

							  </div>

 
<!-- 
							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label"  for="">Validar RFC en Boveda</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<label class="form-check-label d-inline-block me-1" for="RFC_RECEPTOR"
											id="RFC_RECEPTOR_LBL">SI</label>
										<input class="form-check-input" id="RFC_RECEPTOR" name="RFC_RECEPTOR" type="checkbox" checked="" />
										 <input class="form-control" type="text" name="VALOR_RFC_RECEPTOR" id="VALOR_RFC_RECEPTOR"  />
									</div>
								</div>
							  </div>

 -->


							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ46">Validar RFC en Boveda</label>
								<div class="col-sm-1">
			   						<div class="form-check form-switch">
										  <input class="form-check-input" id="RFC_RECEPTOR" name="rfcReceptor" type="checkbox" checked="" />
									</div>
			 					</div>
								 <div class="col-sm-2">
									<input class="form-control" type="text" name="valorRfcReceptor" id="VALOR_RFC_RECEPTOR"  />
							  	</div>
							  </div>

 

							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ47">Bloquear Acceso a Proveedores con Cert. IMSS Pendiente</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="BLOQUEAR_IMSS" name="bloquearImss" type="checkbox" checked="" />
									</div>
								</div>
							  </div>


							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ48">Bloquear Acceso a Proveedores con Cert. SAT Pendiente</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="BLOQUEAR_SAT" name="bloquearSat" type="checkbox" checked="" />
									</div>
								</div>
							  </div>


							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ49">Validar Carta Porte</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="VALIDAR_CP" name="validarCP" type="checkbox" checked="" />
									</div>
								</div>

							  </div>

							  <div class="mb-2 row">

								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ50">Bloquear Facturas con Carta Porte Rechazadas</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="PERMITIR_CARTA_PORTE" name="permitirCartaPorte" type="checkbox"
											checked="" />
									</div>
								</div>
							  </div>


							<div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="LABEL_LAYOUT_ORDEN" id="CONF_SISTEMA_ETQ51">Etiqueta de LayOut por Orden</label>
								<div class="col-sm-1">
								  <div class="form-group">
									<input class="form-control" type="text" name="labelLayOutOrden" id="LABEL_LAYOUT_ORDEN" maxlength="20"/>
								  </div>
								</div>
							  </div>
							  
							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="LABEL_LAYOUT_MULTIPLE" id="CONF_SISTEMA_ETQ52">Etiqueta de LayOut por Orden Multiple</label>
								<div class="col-sm-1">
								  <div class="form-group">
									<input class="form-control" type="text" name="labelLayOutMultiple" id="LABEL_LAYOUT_MULTIPLE" maxlength="20" />
								  </div>
								</div>
							  </div>
							  

							<div class="col-md-6 text-center">
								<button type="submit" id="btnsaveconfiadicional" class="btn btn-primary">Guardar</button>
							</div>

						</div>
					</form>

			
<script type="text/javascript">
$(document).ready(function() {
	
	flatpickr(FECHA_APARTIR_COMPLE, {
	      minDate: '1920-01-01', 
	      //dateFormat : "d-m-Y", 
	      dateFormat : "Y-m-d",
	      locale: {
	        firstDayOfWeek: 1,
	        weekdays: {
	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],         
	        }, 
	        months: {
	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Оct', 'Nov', 'Dic'],
	          longhand: ['Enero', 'Febrero', 'Мarzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	        },
	      },
	    }); 
	
	
	$("#form-ConfigAdicionales").on('submit', function (event) {
	   $(this).addClass('was-validated');
	 });
   
   /* Necesario para validacion del form y del Select2
   -----------------------------------------------------*/
   $('#form-ConfigAdicionales').validate({
	   ignore: 'input[type=hidden]',
	   focusInvalid: false,
       keyUp: true,
       submitHandler: function(form) {
    	   guardarConfigAdicionales()
		},
       errorPlacement: function (error, e) {
       },
       highlight: function (e) {
       },
       success: function (e) {
       }, rules:  {
           select: {required: true}
       }, messages: {
           select: {required: 'error'}
       }
   }).resetForm(); 
   
   iniciaFormConfigAdicionales();
   obtenerConfAdicionales();
   
   
   
   
   	const activarRFC = (e) => {
	   if ($("#RFC_RECEPTOR").is(":checked")) {
		   $("#VALOR_RFC_RECEPTOR").prop("disabled", false);
	   } else {
		   $("#VALOR_RFC_RECEPTOR").prop("disabled", true);
	   }
	 };

	 // Attaching the click event on the button
	 $(document).on("click", "#RFC_RECEPTOR", activarRFC);
	 
   
	 calcularEtiquetasConfSistemaCorreosConfAdicionales();
});
  
  

function calcularEtiquetasConfSistemaCorreosConfAdicionales(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'CONF_SISTEMA'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					document.getElementById("CONF_SISTEMA_ETQ36").innerHTML = data.ETQ36;
					document.getElementById("CONF_SISTEMA_ETQ37").innerHTML = data.ETQ37;
					document.getElementById("CONF_SISTEMA_ETQ38").innerHTML = data.ETQ38;
					document.getElementById("CONF_SISTEMA_ETQ39").innerHTML = data.ETQ39;
					document.getElementById("CONF_SISTEMA_ETQ40").innerHTML = data.ETQ40;
					document.getElementById("CONF_SISTEMA_ETQ41").innerHTML = data.ETQ41;
					document.getElementById("CONF_SISTEMA_ETQ42").innerHTML = data.ETQ42;
					document.getElementById("CONF_SISTEMA_ETQ43").innerHTML = data.ETQ43;
					document.getElementById("CONF_SISTEMA_ETQ44").innerHTML = data.ETQ44;
					document.getElementById("CONF_SISTEMA_ETQ45").innerHTML = data.ETQ45;
					document.getElementById("CONF_SISTEMA_ETQ46").innerHTML = data.ETQ46;
					document.getElementById("CONF_SISTEMA_ETQ47").innerHTML = data.ETQ47;
					document.getElementById("CONF_SISTEMA_ETQ48").innerHTML = data.ETQ48;
					document.getElementById("CONF_SISTEMA_ETQ49").innerHTML = data.ETQ49;
					document.getElementById("CONF_SISTEMA_ETQ50").innerHTML = data.ETQ50;
					document.getElementById("CONF_SISTEMA_ETQ51").innerHTML = data.ETQ51;
					document.getElementById("CONF_SISTEMA_ETQ52").innerHTML = data.ETQ52;
					document.getElementById("btnsaveconfiadicional").innerHTML = BTN_GUARDAR_MENU;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasConfSistemaCloseYear()_1_'+thrownError);
			}
		});	
	}
	

</script>
                                
</html>