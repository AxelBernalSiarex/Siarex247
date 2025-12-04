
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
    <script src="/siarex247/jsp/configuraciones/sistema/confOrdenes/confOrdenes.js"></script>
        
</head>


				<form id="form-ConfOrdenes" class="was-validated" novalidate>
						<div class="p-2">

							 <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="TXTEMPRESA" id="CONF_SISTEMA_ETQ7">Empresa </label>
								<div class="col-sm-4">
								  <div class="form-group">
									<input class="form-control" type="text" name="TXTEMPRESA" id="TXTEMPRESA" disabled/>
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="LLAVE_FINPDF" id="CONF_SISTEMA_ETQ8">Llave Final PDF</label>
								<div class="col-sm-4">
								  <div class="form-group">
									<input class="form-control" type="text" name="llaveFinPDF" id="LLAVE_FINPDF" required/>
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="LLAVE_ORDENES" id="CONF_SISTEMA_ETQ9">Llave Orden de Compra </label>
								<div class="col-sm-2">
								  <div class="form-group">
									<input class="form-control" type="text" name="llaveOrdenes" id="LLAVE_ORDENES" required/>
								  </div>
								</div>

								<label class="col-sm-2 col-form-label" for="LLAVE_FIN_ORDENES" id="CONF_SISTEMA_ETQ10">Número de Caracteres</label>
								<div class="col-sm-1">
								  <div class="form-group">
									<input class="form-control" type="number" name="llaveFinOrdenes"
											id="LLAVE_FIN_ORDENES" required/>
								  </div>
								</div>
							  </div>


							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="LLAVE_VENDOR" id="CONF_SISTEMA_ETQ11">Llave Vendor </label>
								<div class="col-sm-2">
								  <div class="form-group">
									<input class="form-control" type="text" name="llaveVendor" id="LLAVE_VENDOR" required/>
								  </div>
								</div>

								<label class="col-sm-2 col-form-label"  for="LLAVE_FIN_VENDOR" id="CONF_SISTEMA_ETQ12">Número de Caracteres</label>
								<div class="col-sm-1">
								  <div class="form-group">
									<input class="form-control" type="number" name="llaveFinVendor" id="LLAVE_FIN_VENDOR" required/>
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="LLAVE_TOTAL" id="CONF_SISTEMA_ETQ13">Llave Total </label>
								<div class="col-sm-2">
								  <div class="form-group">
									<input class="form-control" type="text" name="llaveTotal" id="LLAVE_TOTAL" required/>
								  </div>
								</div>

								<label class="col-sm-2 col-form-label"  for="LLAVE_FIN_TOTAL" id="CONF_SISTEMA_ETQ14">Número de Caracteres</label>
								<div class="col-sm-1">
								  <div class="form-group">
									<input class="form-control" type="number" name="llaveFinTotal" id="LLAVE_FIN_TOTAL" required/>
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="LLAVE_MONEDA" id="CONF_SISTEMA_ETQ15">Llave Moneda </label>
								<div class="col-sm-2">
								  <div class="form-group">
									<input class="form-control" type="text" name="llaveMoneda" id="LLAVE_MONEDA" required/>
								  </div>
								</div>

								<label class="col-sm-2 col-form-label"  for="LLAVE_FIN_MONEDA" id="CONF_SISTEMA_ETQ16">Número de Caracteres</label>
								<div class="col-sm-1">
								  <div class="form-group">
									<input class="form-control" type="number" name="llaveFinMondeda" id="LLAVE_FIN_MONEDA" required/>
								  </div>
								</div>
							  </div>

								<div class="col-md-12 text-center">
									<button type="submit" id="btnsaveconfigorden" class="btn btn-primary">Guardar</button>
								</div>
						</div>
					</form>

			
<script type="text/javascript">
$(document).ready(function() {
	
	obtenerConfOrdenes();
	
	$("#form-ConfOrdenes").on('submit', function (event) {
	   $(this).addClass('was-validated');
	 });
   
   /* Necesario para validacion del form y del Select2
   -----------------------------------------------------*/
   $('#form-ConfOrdenes').validate({
	   ignore: 'input[type=hidden]',
	   focusInvalid: false,
       keyUp: true,
       submitHandler: function(form) {
    	   guardarDatos();
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
   
   iniciaFormConfOrdenes();
   
   calcularEtiquetasConfSistemaOrder();
});
  
  
  

function calcularEtiquetasConfSistemaOrder(){
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
					
					document.getElementById("CONF_SISTEMA_ETQ7").innerHTML = data.ETQ7;
					document.getElementById("CONF_SISTEMA_ETQ8").innerHTML = data.ETQ8;
					document.getElementById("CONF_SISTEMA_ETQ9").innerHTML = data.ETQ9;
					document.getElementById("CONF_SISTEMA_ETQ10").innerHTML = data.ETQ10;
					document.getElementById("CONF_SISTEMA_ETQ11").innerHTML = data.ETQ11;
					document.getElementById("CONF_SISTEMA_ETQ12").innerHTML = data.ETQ12;
					document.getElementById("CONF_SISTEMA_ETQ13").innerHTML = data.ETQ13;
					document.getElementById("CONF_SISTEMA_ETQ14").innerHTML = data.ETQ14;
					document.getElementById("CONF_SISTEMA_ETQ15").innerHTML = data.ETQ15;
					document.getElementById("CONF_SISTEMA_ETQ16").innerHTML = data.ETQ16;
					
					
					document.getElementById("btnsaveconfigorden").innerHTML = BTN_GUARDAR_MENU;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasConfSistema()_1_'+thrownError);
			}
		});	
	}

</script>
                                
</html>