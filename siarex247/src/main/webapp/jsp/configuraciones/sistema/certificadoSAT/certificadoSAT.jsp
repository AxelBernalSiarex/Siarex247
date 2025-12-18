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
     <script src="/siarex247/jsp/configuraciones/sistema/certificadoSAT/cargaDatos.js"></script>   
</head>


				<form id="frmCargaCertificados" class="was-validated" novalidate>
						<div class="p-4 pb-0">
							<div class="p-4 pb-0">
							  <div class="mb-2 row">
								<label class="col-sm-3 col-form-label" for="TIENE_CERTIFICADO" id="CONF_SISTEMA_ETQ59">¿ Tiene Certificados SAT cargados en SIAREX ?</label>
								<div class="col-sm-3">
									<div class="form-check form-switch">
										<input class="form-check-input" id="TIENE_CERTIFICADO" name="TIENE_CERTIFICADO" type="checkbox" />
									</div>
								</div>
							  </div>
							</div>  
							  
							   <div class="p-4 pb-0">
								  <div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="fileCER" id="CONF_SISTEMA_ETQ60">Seleccione el archivo .CER</label>
										<div class="col-sm-3">
					   						<input id="fileCER" name="fileCER" class="form-control" type="file" accept=".cer" required />
					 					</div>
									</div>
								</div>
								
								<div class="p-4 pb-0">
								  <div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="fileKEY" id="CONF_SISTEMA_ETQ61">Seleccione el archivo .KEY</label>
										<div class="col-sm-3">
					   						<input id="fileKEY" name="fileKEY" class="form-control" type="file" accept=".key" required />
					 					</div>
									</div>
								</div>
								
								<div class="p-4 pb-0">
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="pwdSat" id="CONF_SISTEMA_ETQ62">Contraseña de E-firma</label>
										<div class="col-sm-3">
					   						<input id="pwdSat" name="pwdSat" class="form-control" type="password" required />
					 					</div>
									</div>
								</div>	
								
								<div class="p-4 pb-0">
									<div class="mb-2 row">
										<label class="col-sm-6 col-form-label" for="pwdSat" style="color: navy;" id="CONF_SISTEMA_ETQ63">
											Estimado cliente le recordamos que la información que usted ingresa, se guardará en nuestros servidores de manera
											encriptada por protocolo de seguridad, por lo que la información que usted ingrese no podrá ser visualizada ni compartida
											de manera interna o externa.
										</label>
										
									</div>
								</div>	
								
							<div class="col-md-6 text-center">
								<button type="submit" id="btnsaveCert" class="btn btn-primary">Guardar</button>
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
   $('#frmCargaCertificados').validate({
	   ignore: 'input[type=hidden]',
	   focusInvalid: false,
       keyUp: true,
       submitHandler: function(form) {
    	   guardarCertificado()
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
   
   iniciaFormConfigCertificado();
   asignarCertificado();	
   
   calcularEtiquetasConfSistemaCertificados();
});
  

function calcularEtiquetasConfSistemaCertificados(){
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
					
					document.getElementById("CONF_SISTEMA_ETQ59").innerHTML = data.ETQ59;
					document.getElementById("CONF_SISTEMA_ETQ60").innerHTML = data.ETQ60;
					document.getElementById("CONF_SISTEMA_ETQ61").innerHTML = data.ETQ61;
					document.getElementById("CONF_SISTEMA_ETQ62").innerHTML = data.ETQ62;
					document.getElementById("CONF_SISTEMA_ETQ63").innerHTML = data.ETQ63;
					document.getElementById("btnsaveCert").innerHTML = BTN_GUARDAR_MENU;
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasConfSistemaCertificados()_1_'+thrownError);
			}
		});	
	}


</script>
                                
</html>