<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
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
	
	
<%

  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  Date fechaHoy = new Date();
  String fechaActual = dateFormat.format(fechaHoy);

%>


</head>



			<form id="form-EnvioCorreos" class="was-validated" novalidate>
				<input type="hidden" name="num_Dias1" id="num_Dias1" value="0">
				<input type="hidden" name="num_Dias2" id="num_Dias2" value="0">
				<input type="hidden" name="tipoAlarma" id="tipoAlarma" value="ORD">
				<input type="hidden" name="preValida" id="preValida" value="FALSE">
				
								<div class="p-2 pb-0">
							
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="txtAsunto_Envio" id="TAREAS_ETQ13">Asunto </label>
										<div class="col-sm-9">
											   <input id="txtAsunto_Envio" name="subject" class="form-control" type="text"  required />
										 </div>
									</div>
				
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="txtMensaje_Enviar" id="TAREAS_ETQ14">Mensaje </label>
										<div class="col-sm-9">
											<textarea class="form-control" id="txtMensaje_Enviar" name="mensaje"  rows="3" required></textarea>
										 </div>
									</div>
				
				
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="" id="TAREAS_ETQ15">Enviar Ahora ?</label>
										<div class="col-sm-9">
											<div class="form-check form-switch">
												<input class="form-check-input" id="chkAhora_Enviar" name="enviarAhora" type="checkbox" />
											</div>
										</div>
									</div>


									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="filesPDF_Enviar" id="TAREAS_ETQ16">Seleccione el Archivo .PDF a Enviar </label>
										<div class="col-sm-9">
											<input id="filesPDF_Enviar" name="filesPDF" class="form-control" type="file"  accept="application/pdf"  required />
										 </div>
									</div>

									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="txtfechaTarea_Enviar" id="TAREAS_ETQ17">Fecha y Hora de Envío</label>
										<div class="col-sm-9">
										  <div class="form-group">
											  <input class="form-control datetimepicker" id="txtfechaTarea_Enviar" name="fechaTarea" value="<%=fechaActual %>" type="text" placeholder="dd/mm/yyyy H:i" data-options='{"enableTime":true,"dateFormat":"dd/mm/yyyy H:i","disableMobile":true}' required />
											  
										  </div>
										</div>
									</div>

									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="" id="TAREAS_ETQ18">Envío A </label>
										<div class="col-sm-9">
											<div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Envio" type="radio" name="tipoEnvio" value="NONE" checked="" />
												<label class="form-check-label" for="radninguno" id="TAREAS_ETQ19">Ninguna</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Envio" type="radio" name="tipoEnvio" value="ALL" />
												<label class="form-check-label" for="radtodos" id="TAREAS_ETQ20">Todos</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Envio" type="radio" name="tipoEnvio" value="MEX" />
												<label class="form-check-label" for="radmex" id="TAREAS_ETQ21">Proveedores de M&eacute;xico</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Envio" type="radio" name="tipoEnvio" value="USA" />
												<label class="form-check-label" for="radusa" id="TAREAS_ETQ22">Proveedores de USA</label>
											  </div>
										</div>
									</div>

									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="" id="TAREAS_ETQ23">Cargar en Sistema A </label>
										<div class="col-sm-9">
											<div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkCargar_Envio" type="radio" name="cargarSistema" value="NONE" checked="" />
												<label class="form-check-label" for="radningunosist" id="TAREAS_ETQ24">Ninguna</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkCargar_Envio" type="radio" name="cargarSistema" value="ALL" />
												<label class="form-check-label" for="radtodossist" id="TAREAS_ETQ25">Todos</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkCargar_Envio" type="radio" name="cargarSistema" value="MEX" />
												<label class="form-check-label" for="radmexsist" id="TAREAS_ETQ26">Proveedores de M&eacute;xico</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkCargar_Envio" type="radio" name="cargarSistema" value="USA" />
												<label class="form-check-label" for="radusasist" id="TAREAS_ETQ27">Proveedores de USA</label>
											  </div>
										</div>
									</div>

									<div class="mb-2 row">
										<div class="d-flex justify-content-evenly bd-highlight">
											<div class="p-2 bd-highlight">
												<button class="btn btn-primary" type="submit" id="TAREAS_Boton_Guardar">Guardar</button>
											</div>
											<div class="p-2 bd-highlight">
												<button class="btn btn-secondary me-1 mb-1" type="button" onclick="preValidaTarea();" id="TAREAS_Boton_PreValidar">PreValidar</button>
											</div>
											
										  </div>
									</div>

				
								</div>
			</form>

	


 <script type="text/javascript">
 
 $(document).ready(function() {
  	  
	 flatpickr(txtfechaTarea_Enviar, {
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
	 
	 
	   $("#form-EnvioCorreos").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });

	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-EnvioCorreos').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
         errorClass: 'help-block animation-pullUp', errorElement: 'div',
         keyUp: true,
         submitHandler: function(form) {
        	 guardarEnvioCorreos();
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
	   	   	   
	   iniciaFormEnvioCorreos();
	   calcularEtiquetasTareasEnvioCorreosModal();
	});

 

 function calcularEtiquetasTareasEnvioCorreosModal(){
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
					
					document.getElementById("TAREAS_ETQ13").innerHTML = data.ETQ13;
					document.getElementById("TAREAS_ETQ14").innerHTML = data.ETQ14;
					document.getElementById("TAREAS_ETQ15").innerHTML = data.ETQ15;
					document.getElementById("TAREAS_ETQ16").innerHTML = data.ETQ16;
					document.getElementById("TAREAS_ETQ17").innerHTML = data.ETQ17;
					document.getElementById("TAREAS_ETQ18").innerHTML = data.ETQ18;
					document.getElementById("TAREAS_ETQ19").innerHTML = data.ETQ19;
					document.getElementById("TAREAS_ETQ20").innerHTML = data.ETQ20;
					document.getElementById("TAREAS_ETQ21").innerHTML = data.ETQ21;
					document.getElementById("TAREAS_ETQ22").innerHTML = data.ETQ22;
					document.getElementById("TAREAS_ETQ23").innerHTML = data.ETQ23;
					document.getElementById("TAREAS_ETQ24").innerHTML = data.ETQ24;
					document.getElementById("TAREAS_ETQ25").innerHTML = data.ETQ25;
					document.getElementById("TAREAS_ETQ26").innerHTML = data.ETQ26;
					document.getElementById("TAREAS_ETQ27").innerHTML = data.ETQ27;
					
					document.getElementById("TAREAS_Boton_PreValidar").innerHTML = data.ETQ28;
					document.getElementById("TAREAS_Boton_Guardar").innerHTML = BTN_GUARDAR_MENU;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasTareasEnvioCorreosModal()_1_'+thrownError);
			}
		});	
	}
 
 </script>
</html>