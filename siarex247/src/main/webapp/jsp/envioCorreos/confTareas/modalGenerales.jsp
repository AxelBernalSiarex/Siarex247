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
    <script src="/siarex247/jsp/envioCorreos/confTareas/filterProveedores.js"></script>
	
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

			<form id="form-generales" class="was-validated" novalidate>
				<input type="hidden" name="num_Dias1" id="num_Dias1" value="0">
				<input type="hidden" name="num_Dias2" id="num_Dias2" value="0">
				<input type="hidden" name="tipoAlarma" id="tipoAlarma" value="GEN">
				<input type="hidden" name="preValida" id="preValida" value="FALSE">
				<input type="hidden" name="tipoProveedor" id="tipoProveedor" value = "ALL">
				<input type="hidden" name="claveProveedor" id="claveProveedor" value = "">
				
								<div class="p-2 pb-0">
							
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="txtAsunto_Generales" id="TAREAS_ETQ29">Asunto</label>
										<div class="col-sm-9">
											   <input id="txtAsunto_Generales" name="subject" class="form-control" type="text"  required />
										 </div>
									</div>
				
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="txtMensaje_Generales" id="TAREAS_ETQ30">Mensaje </label>
										<div class="col-sm-9">
											<textarea class="form-control" id="txtMensaje_Generales" name="mensaje"  rows="3" required></textarea>
										 </div>
									</div>
				
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="" id="TAREAS_ETQ31">Requiero Copia del Correo</label>
										<div class="col-sm-9">
											<div class="form-check form-switch">
												<input class="form-check-input" id="chkRequieroCopia_Generales" name="requieroCopia" type="checkbox"  />
											</div>
										</div>
									</div>
				
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="" id="TAREAS_ETQ32">Enviar Ahora ?</label>
										<div class="col-sm-9">
											<div class="form-check form-switch">
												<input class="form-check-input" id="chkAhora_Generales" name="enviarAhora"  type="checkbox" />
											</div>
										</div>
									</div>


									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="filesPDF_Generales" id="TAREAS_ETQ33">Seleccione el Archivo .PDF a Enviar </label>
										<div class="col-sm-9">
											<input id="filesPDF_Generales" name="filesPDF" class="form-control" type="file" accept="application/pdf"  required />
										 </div>
									</div>

									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="txtfechaTarea_Generales" id="TAREAS_ETQ34">Fecha y Hora de Envío</label>
										<div class="col-sm-9">
										  <div class="form-group">
											<input class="form-control datetimepicker" id="txtfechaTarea_Generales" name="fechaTarea" type="text" placeholder="dd/mm/yyyy H:i" data-options='{"enableTime":true,"dateFormat":"dd/mm/yyyy H:i","disableMobile":true}' required />
										  </div>
										</div>
									</div>

									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="" id="TAREAS_ETQ35">Envío A </label>
										<div class="col-sm-9">
											
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Generales" type="radio" name="tipoEnvio" value="ALL" checked="" onclick="cargaProveedores(0, 'ALL');"/>
												<label class="form-check-label" for="radningunogral" id="TAREAS_ETQ36">Todos</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Generales" type="radio" name="tipoEnvio" value="MEX" onclick="cargaProveedores(0, 'MEX');"/>
												<label class="form-check-label" for="radningunogral" id="TAREAS_ETQ37">Proveedores de M&eacute;xico</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Generales" type="radio" name="tipoEnvio" value="USA" onclick="cargaProveedores(0, 'USA');" />
												<label class="form-check-label" for="radningunogral" id="TAREAS_ETQ38">Proveedores de USA</label>
											  </div>
										</div>
									</div>
									<div class="mb-2 row">
										<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
											<div class="tab-content">
												<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
													<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
														<table id="tablaDetalleProveedores"class="table mb-0 data-table fs--1">
															<thead class="bg-200 text-900">
																<tr>
																    <th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ40">RFC</th>
																	<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ41">Raz&oacute;n Social</th>
																</tr>
																<tr class="forFilters">
																	<th></th>
																	<th></th>
																</tr>
															</thead>
														</table>
													</div>
												</div>
											</div> <!-- tab-content -->
									  </div> <!-- card-body -->
									</div>
									
									<div class="mb-2 row">
										<div class="d-flex justify-content-evenly bd-highlight">
											<div class="p-2 bd-highlight">
												<button class="btn btn-primary" type="submit" id="TAREAS_Boton_GurdarGenerales">Guardar</button>
											</div>
										
										  </div>
									</div>

				
								</div>
							</form>


 <script type="text/javascript">
 
 $(document).ready(function() {
 	  
	 flatpickr(txtfechaTarea_Generales, {
  	      minDate: '1920-01-01', 
  	      //dateFormat : "d-m-Y",
  	      enableTime: true,
  	      dateFormat : "Y-m-d H:i",
  	      locale: {
  	        firstDayOfWeek: 1,
  	        weekdays: {
  	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
  	          longhand: ['Domingo', 'Lunes', 'Martes', 'Mi&eacute;rcoles', 'Jueves', 'Viernes', 'Sábado'],         
  	        }, 
  	        months: {
  	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Оct', 'Nov', 'Dic'],
  	          longhand: ['Enero', 'Febrero', 'Мarzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
  	        },
  	      },
  	    }); 
	 
	 
	   $("#form-generales").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });

	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-generales').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
         errorClass: 'help-block animation-pullUp', errorElement: 'div',
         keyUp: true,
         submitHandler: function(form) {
        	 guardarGenerales();
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
	   	   	   
	   iniciaFormGenerales();
	   calcularEtiquetasTareasEnvioCorreosGeneralesModal();   
	});
 
 
 
 function cargaProveedores(claveRegistro, tipoProveedor) {
	 try{
		 $('#tipoProveedor').val(tipoProveedor);
		 $('#tablaDetalleProveedores').DataTable().ajax.reload(null,false);
	 }catch(e){
		 alert('cargaProveedores()_'+e);
	 }
	 
	 
 }

 function calcularEtiquetasTareasEnvioCorreosGeneralesModal(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'TAREAS'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					document.getElementById("TAREAS_ETQ29").innerHTML = data.ETQ29;
					document.getElementById("TAREAS_ETQ30").innerHTML = data.ETQ30;
					document.getElementById("TAREAS_ETQ31").innerHTML = data.ETQ31;
					document.getElementById("TAREAS_ETQ32").innerHTML = data.ETQ32;
					document.getElementById("TAREAS_ETQ33").innerHTML = data.ETQ33;
					document.getElementById("TAREAS_ETQ34").innerHTML = data.ETQ34;
					document.getElementById("TAREAS_ETQ35").innerHTML = data.ETQ35;
					document.getElementById("TAREAS_ETQ36").innerHTML = data.ETQ36;
					document.getElementById("TAREAS_ETQ37").innerHTML = data.ETQ37;
					document.getElementById("TAREAS_ETQ38").innerHTML = data.ETQ38;
					// document.getElementById("TAREAS_ETQ39").innerHTML = data.ETQ39;
					document.getElementById("TAREAS_ETQ40").innerHTML = data.ETQ40;
					document.getElementById("TAREAS_ETQ41").innerHTML = data.ETQ41;
					
					document.getElementById("TAREAS_Boton_GurdarGenerales").innerHTML = BTN_GUARDAR_MENU;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasTareasEnvioCorreosModal()_1_'+thrownError);
			}
		});	
	}
 </script>
</html>