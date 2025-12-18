
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
     <script src="/siarex247/jsp/configuraciones/sistema/correosRespaldo/correosRespaldo.js"></script>   
</head>


				<form id="form-CorreoRespaldo" class="was-validated" novalidate>
						<div class="p-2">

							<div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_ORDENES" id="CONF_SISTEMA_ETQ25">Correo Ordenes de Compra</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="correoOrdenes" id="CORREO_ORDENES" />
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_PAGOS" id="CONF_SISTEMA_ETQ26">Correo Ordenes de Pago</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="correoPagos" id="CORREO_PAGOS" />
								  </div>
								</div>
							  </div>

<!-- 
							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_RESPALDO" id="CONF_SISTEMA_ETQ27">Correo Ordenes de Respaldos</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="correoRespaldo" id="CORREO_RESPALDO"/>
								  </div>
								</div>
							  </div>

 -->

<!-- se paso a pantalla de alarma de complementos de pago
 
							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_COMPLEMENTO" id="CONF_SISTEMA_ETQ28">Correo Notificación de Complementos de Pago</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="correoComplemento" id="CORREO_COMPLEMENTO" />
								  </div>
								</div>
							  </div>
 -->
							  
							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_LISTA_NEGRA_SAT_1" id="CONF_SISTEMA_ETQ29">1. Correo Aviso Lista Negra SAT</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="CorreoListaNegra1" id="CORREO_LISTA_NEGRA_SAT_1" />
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_LISTA_NEGRA_SAT_2" id="CONF_SISTEMA_ETQ30">2. Correo Aviso Lista Negra SAT</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="CorreoListaNegra2" id="CORREO_LISTA_NEGRA_SAT_2" />
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_LISTA_NEGRA_SAT_3" id="CONF_SISTEMA_ETQ31">3. Correo Aviso Lista Negra SAT</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="CorreoListaNegra3" id="CORREO_LISTA_NEGRA_SAT_3" />
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_LISTA_NEGRA_SAT_4" id="CONF_SISTEMA_ETQ32">4. Correo Aviso Lista Negra SAT</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="CorreoListaNegra4" id="CORREO_LISTA_NEGRA_SAT_4" />
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_LISTA_NEGRA_SAT_5" id="CONF_SISTEMA_ETQ33">5. Correo Aviso Lista Negra SAT</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="CorreoListaNegra5" id="CORREO_LISTA_NEGRA_SAT_5" />
								  </div>
								</div>
							  </div>
							  
							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_AVISO_UUID_BOVEDA_1" id="CONF_SISTEMA_ETQ34"> 1. Notificar correo de UUID que no existen en Boveda</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="correoAvisoUuidBoveda1" id="CORREO_AVISO_UUID_BOVEDA_1" />
								  </div>
								</div>
							  </div>
							  
							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="CORREO_AVISO_UUID_BOVEDA_2" id="CONF_SISTEMA_ETQ35"> 2. Notificar correo de UUID que no existen en Boveda</label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control" type="email" name="correoAvisoUuidBoveda2" id="CORREO_AVISO_UUID_BOVEDA_2" />
								  </div>
								</div>
							  </div>
							  
							  
						
							<div class="col-md-6 text-center">
								<button type="submit" id="btnsaverespaldo" class="btn btn-primary">Guardar</button>
							</div>
						
						</div>

					</form>
			
<script type="text/javascript">
$(document).ready(function() {
	
	$("#form-CorreoRespaldo").on('submit', function (event) {
	   $(this).addClass('was-validated');
	 });
   
   /* Necesario para validacion del form y del Select2
   -----------------------------------------------------*/
   $('#form-CorreoRespaldo').validate({
	   ignore: 'input[type=hidden]',
	   focusInvalid: false,
       keyUp: true,
       submitHandler: function(form) {
    	   guardarDatosCorreoRespaldo()
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
   
   iniciaFormCorreoRespaldo();
   obtenerCorreos();
   calcularEtiquetasConfSistemaCorreosRespaldo();
});
  
  

function calcularEtiquetasConfSistemaCorreosRespaldo(){
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
					
					document.getElementById("CONF_SISTEMA_ETQ25").innerHTML = data.ETQ25;
					document.getElementById("CONF_SISTEMA_ETQ26").innerHTML = data.ETQ26;
					document.getElementById("CONF_SISTEMA_ETQ27").innerHTML = data.ETQ27;
					document.getElementById("CONF_SISTEMA_ETQ28").innerHTML = data.ETQ28;
					document.getElementById("CONF_SISTEMA_ETQ29").innerHTML = data.ETQ29;
					document.getElementById("CONF_SISTEMA_ETQ30").innerHTML = data.ETQ30;
					document.getElementById("CONF_SISTEMA_ETQ31").innerHTML = data.ETQ31;
					document.getElementById("CONF_SISTEMA_ETQ32").innerHTML = data.ETQ32;
					document.getElementById("CONF_SISTEMA_ETQ33").innerHTML = data.ETQ33;
					document.getElementById("CONF_SISTEMA_ETQ34").innerHTML = data.ETQ34;
					document.getElementById("CONF_SISTEMA_ETQ35").innerHTML = data.ETQ35;
					document.getElementById("btnsaverespaldo").innerHTML = BTN_GUARDAR_MENU;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasConfSistemaCloseYear()_1_'+thrownError);
			}
		});	
	}

</script>
                                
</html>