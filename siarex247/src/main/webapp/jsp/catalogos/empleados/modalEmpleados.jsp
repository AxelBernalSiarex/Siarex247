<!DOCTYPE html>
<html>

    <%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
  
  
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">
         
	<script>
      var isRTL = JSON.parse(localStorage.getItem('isRTL'));
      if (isRTL) {
        var linkDefault = document.getElementById('style-default');
        var userLinkDefault = document.getElementById('user-style-default');
        linkDefault.setAttribute('disabled', true);
        userLinkDefault.setAttribute('disabled', true);
        document.querySelector('html').setAttribute('dir', 'rtl');
      } else {
        var linkRTL = document.getElementById('style-rtl');
        var userLinkRTL = document.getElementById('user-style-rtl');
        linkRTL.setAttribute('disabled', true);
        userLinkRTL.setAttribute('disabled', true);
      }
    </script>
	
</head>


<form id="form-Catalogo" class="was-validated" novalidate>
	<input type="hidden" name="idRegistro" id="idRegistro_Catalogo" value="0" >
	
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="modal-title-catalogo">title</h5>
				</div>
				<div class="p-4 pb-3">
				
					<div class="card overflow-hidden">
			
						<div class="card-header p-0 scrollbar-overlay border-bottom">
							<ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabAlarmas" role="tablist">
								<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1 active" id="tabInfoPrincipal" data-bs-toggle="tab" href="#infoPrincipal" role="tab" aria-controls="infoPrincipal" aria-selected="true">
										<span class="fas fa-address-card text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CAT_EMPLEADOS_ETQ7">Informaci&oacute;n Principal</h6>
									</a>
								</li>
								
		                      	<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabInformacionAcceso" data-bs-toggle="tab" href="#informacionAcceso" role="tab" aria-controls="informacionAcceso" aria-selected="false">
										<span class="fas fa-key icon text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CAT_EMPLEADOS_ETQ8">Información de Acceso</h6>
		                      		</a>
		                      	</li>
							</ul>
						</div>
		                
		                <div class="tab-content">
		                	
							<!-- Informacion Principal -->
		                	<div class="card-body bg-light tab-pane active" id="infoPrincipal" role="tabpanel" aria-labelledby="tabInfoPrincipal">
									
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="idEmpleado" id="CAT_EMPLEADOS_Etiqueta_IdEmpleado">Id. Empleado</label>
										<div class="col-sm-3">
					   						<input id="idEmpleado" name="idEmpleado" class="form-control validarHabilitar" type="text" required />
					 					</div>
					 					<label class="col-sm-2 col-form-label" for="correo" id="CAT_EMPLEADOS_Etiqueta_Correo">Correo</label>
										<div class="col-sm-4">
					   						<input id="correo" name="correo" class="form-control validarHabilitar" type="text"  required/>
					 					</div>
									</div>	
				
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="nombreCompleto" id="CAT_EMPLEADOS_Etiqueta_NombreCompleto">Nombre Completo</label>
										<div class="col-sm-9">
					   						<input id="nombreCompleto" name="nombreCompleto" class="form-control validarHabilitar" type="text"  required />
					 					</div>
					 					
										
									</div>			                		
				
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="idSupervisor" id="CAT_EMPLEADOS_Etiqueta_IdSupervisor">Id. Supervisor</label>
										<div class="col-sm-3">
					   						<input id="idSupervisor" name="idSupervisor" class="form-control validarHabilitar" type="text" required/>
					 					</div>
					 					<label class="col-sm-2 col-form-label" for="txtTelefono" id="CAT_EMPLEADOS_Etiqueta_ShipTO">ShipTO</label>
										<div class="col-sm-4">
											<input class="form-control validarHabilitar" id="shipTo" name="shipTo" required />
					 					</div>
									</div>
												
		                	</div>
		                	
		                	<div class="card-body bg-light tab-pane" id="informacionAcceso" role="tabpanel" aria-labelledby="tabInformacionAcceso">
								<div class="mb-2 row">
								    <label class="col-sm-3 col-form-label" for="chkAcceso" id="CAT_EMPLEADOS_ETQ9">Con Acceso a Siarex</label>
									<div class="col-sm-8">
									   <div class="switchToggle">																			
										<div class="form-check form-switch">
				   							<input class="form-check-input validarHabilitar" id="chkAcceso" name="chkAcceso" type="checkbox"/>
										</div>
									  </div>
									</div>
									
								</div>
								
								
								<div class="mb-2 row">
									<label class="col-sm-3 col-form-label" for="txtUsuarioAcceso" id="CAT_EMPLEADOS_ETQ10">Email de Acceso</label>
									<div class="col-sm-8">
										<input id="usuarioSiarex" name="usuarioSiarex" class="form-control validarHabilitar" type="email" />
				 					</div>
								</div>
								
								<div class="mb-2 row">
									<label class="col-sm-3 col-form-label" for="cmbIdPerfil" id="CAT_EMPLEADOS_ETQ11">Perfil</label>
									<div class="col-sm-8">
										<div class="form-group">
										<select class="form-select validarHabilitar" id="cmbIdPerfil" name="cmbIdPerfil" required>
										    <option value="">Seleccione una opción ...</option>
										</select>
										</div>
									</div>
								</div>
								
		                	</div> <!-- informacionAcceso -->
		                	
		                </div>
					
					</div>
				
				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary" id="btnSometer">Guardar</button>
				<button class="btn btn-secondary" id="CAT_EMPLEADOS_Boton_Cerrar" type="button" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
 
 $(document).ready(function() {
  	  
	   $("#form-Catalogo").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Catalogo').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
         errorClass: 'help-block animation-pullUp', errorElement: 'div',
         keyUp: true,
         submitHandler: function(form) {
				 var idRegistro = $('#idRegistro_Catalogo').val();
				   if (idRegistro == 0){
					   guardarCatalogo();
				   }else{
					   actualizarCatalogo();
				   }
			},
         errorPlacement: function (error, e) {
      	   e.parents('.form-group').append(error);
         },
         highlight: function (e) {
             $(e).closest('.form-group').removeClass('has-success has-error').addClass('has-error');
         },
         success: function (e) {
             e.closest('.form-group').removeClass('has-success has-error').addClass('has-success');
         }, rules:  {
             select: {required: true}
         }, messages: {
             select: {required: 'error'}
         }
     }).resetForm(); 
	   
	   $('#chkAcceso').change(function () {
           if ($(this).is(":checked")) {
              // $('#chkAccesoLbl').html("SI");               
           } else {
        	  // $('#chkAccesoLbl').html("NO");
           }
       });
	   
	   $('#cmbIdPerfil').on('change', function() { 
		   $(this).trigger('blur');
		   
	   });
	   
	   calcularEtiquetasCatalogoEmpleadosModal();
 });
 

 function calcularEtiquetasCatalogoEmpleadosModal(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'CAT_EMPLEADOS'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					document.getElementById("CAT_EMPLEADOS_ETQ7").innerHTML = data.ETQ7;
					document.getElementById("CAT_EMPLEADOS_ETQ8").innerHTML = data.ETQ8;
					document.getElementById("CAT_EMPLEADOS_ETQ9").innerHTML = data.ETQ9;
					document.getElementById("CAT_EMPLEADOS_ETQ10").innerHTML = data.ETQ10;
					document.getElementById("CAT_EMPLEADOS_ETQ11").innerHTML = data.ETQ11;
					document.getElementById("CAT_EMPLEADOS_Etiqueta_IdEmpleado").innerHTML = data.ETQ1;
					document.getElementById("CAT_EMPLEADOS_Etiqueta_Correo").innerHTML = data.ETQ3;
					document.getElementById("CAT_EMPLEADOS_Etiqueta_NombreCompleto").innerHTML = data.ETQ2;
					document.getElementById("CAT_EMPLEADOS_Etiqueta_IdSupervisor").innerHTML = data.ETQ4;
					
					
					document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
					document.getElementById("CAT_EMPLEADOS_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
 

 function cargaPerfiles(idPerfil) {
		try {
			$('#cmbIdPerfil').empty();
			$.ajax({
	           url:  '/siarex247/seguridad/perfiles/comboPerfilesEmpleado.action',
	           type: 'POST',
	            dataType : 'json',
			    success: function(data){
			    	$('#cmbIdPerfil').empty();
			    	$.each(data.data, function(key, text) {
				    	if (idPerfil == text.clavePerfil){
				    		$('#cmbIdPerfil').append($('<option></option>').attr('selected', 'selected').attr('value', text.clavePerfil).text(text.descripcion));
				    	}else{
				    		$('#cmbIdPerfil').append($('<option></option>').attr('value', text.clavePerfil).text(text.descripcion));
				    	}
			    		
			      	});
			    }
			});
		}catch(e) {
			alert("cargaPerfiles()_"+e);
		} 
	}
 
  
 </script>
</html>