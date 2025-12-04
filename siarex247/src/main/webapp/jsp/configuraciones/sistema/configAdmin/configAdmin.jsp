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
     <script src="/siarex247/jsp/configuraciones/sistema/configAdmin/configAdmin.js"></script>   
</head>


				<form id="frmAdminSAT" class="was-validated" novalidate>
						<div class="p-4 pb-0">

							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ53">Rechazar los XML Cancelados en SAT</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="VALIDA_SAT" name="validaSat" type="checkbox" checked="" />
									</div>
								</div>
							  </div>
							  
							  
							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ54">Validar XML con cobro </label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="VALIDA_XML_TIMBRADO" name="validaXMLTimbrado" type="checkbox" checked="" />
									</div>
								</div>
							  </div>
							  

							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ55">Validar XML fuera de la Fecha de Cierre</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="VALIDAR_CIERRE_ADMIN" name="validarCierreAdmin" type="checkbox" checked="" />
									</div>
								</div>
							  </div>


							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ56">Validar Complementos de Pago a las Facturas</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="VALIDAR_COMPLEMENTO_ADMIN" name="validarComplementoAdmin" type="checkbox" checked="" />
									</div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label"  for="" id="CONF_SISTEMA_ETQ57">Validar Contrato de Confidencialidad</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="VALIDAR_CONTRATO_CONFIDENCIALIDAD" name="validarContratoConfidencialidad" type="checkbox"
											checked="" />
									</div>
								</div>
							  </div>



							  <div class="mb-2 row">
								<label class="col-sm-4 col-form-label" for="" id="CONF_SISTEMA_ETQ58">Mostrar Logo de la empresa en Boveda</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="BANDERA_LOGO_TOYOTA" name="banderaLogoToyota" type="checkbox" checked="" />
									</div>
								</div>
							  </div>


							<div class="col-md-6 text-center">
								<button type="submit" id="btnsaveconfiadicionalAdmin" class="btn btn-primary">Guardar</button>
							</div>

						</div>
					</form>

			
<script type="text/javascript">
$(document).ready(function() {
	
	
	$("#frmAdminSAT").on('submit', function (event) {
	   $(this).addClass('was-validated');
	 });
   
   /* Necesario para validacion del form y del Select2
   -----------------------------------------------------*/
   $('#frmAdminSAT').validate({
	   ignore: 'input[type=hidden]',
	   focusInvalid: false,
       keyUp: true,
       submitHandler: function(form) {
    	   guardarConfigAdmin()
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
   
   iniciaFormConfigAdmin();
   obtenerConfAdministrador();	
   calcularEtiquetasConfSistemaCorreosConfAdmin();
});
  

function calcularEtiquetasConfSistemaCorreosConfAdmin(){
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
					
					document.getElementById("CONF_SISTEMA_ETQ53").innerHTML = data.ETQ53;
					document.getElementById("CONF_SISTEMA_ETQ54").innerHTML = data.ETQ54;
					document.getElementById("CONF_SISTEMA_ETQ55").innerHTML = data.ETQ55;
					document.getElementById("CONF_SISTEMA_ETQ56").innerHTML = data.ETQ56;
					document.getElementById("CONF_SISTEMA_ETQ57").innerHTML = data.ETQ57;
					document.getElementById("CONF_SISTEMA_ETQ58").innerHTML = data.ETQ58;
					
					document.getElementById("btnsaveconfiadicionalAdmin").innerHTML = BTN_GUARDAR_MENU;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasConfSistemaCloseYear()_1_'+thrownError);
			}
		});	
	}


</script>
                                
</html>