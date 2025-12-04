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

		<form id="form-Predefinidas" class="was-validated" novalidate>
				<input type="hidden" name="num_Dias1" id="num_Dias1" value="0">
				<input type="hidden" name="num_Dias2" id="num_Dias2" value="0">
				<input type="hidden" name="tipoAlarma" id="tipoAlarma" value="ORD">
				<input type="hidden" name="preValida" id="preValida" value="FALSE">
		
								<div class="p-2 pb-0">
							
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="txtAsunto_Predefinidas" id="TAREAS_ETQ42">Asunto </label>
										<div class="col-sm-9">
											   <input id="txtAsunto_Predefinidas" name="subject" class="form-control" type="text" value="NOTIFICACION DE ORDENES" required />
										 </div>
									</div>
				
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="txtMensaje_Predefinidas" id="TAREAS_ETQ43">Mensaje </label>
										<div class="col-sm-9">
											<textarea class="form-control" id="txtMensaje_Predefinidas" name="mensaje"   rows="3" required >ENVIO DE ORDENES DE COMPRA</textarea>
										 </div>
									</div>
				
				
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="" id="TAREAS_ETQ44">Enviar Ahora ?</label>
										<div class="col-sm-9">
											<div class="form-check form-switch">
												<input class="form-check-input" id="chkAhora_Predefinidas" name="enviarAhora" type="checkbox" checked="" />
											</div>
										</div>
									</div>


									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="filesPDF_Predefinidas" id="TAREAS_ETQ45">Seleccione el Archivo .PDF a Enviar </label>
										<div class="col-sm-9">
											<input id="filesPDF_Predefinidas" name="filesPDF" class="form-control" type="file" accept="application/pdf"  />
										 </div>
									</div>

									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="txtfechaTarea_Predefinidas" id="TAREAS_ETQ46">Fecha y Hora de Envío</label>
										<div class="col-sm-9">
										  <div class="form-group">
											<input class="form-control datetimepicker" id="txtfechaTarea_Predefinidas" name="fechaTarea" value="<%=fechaActual %>" type="text" placeholder="dd/mm/yyyy H:i" data-options='{"enableTime":true,"dateFormat":"dd/mm/yyyy H:i","disableMobile":true}' required />
										  </div>
										</div>
									</div>

									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="" id="TAREAS_ETQ47">Envío A </label>
										<div class="col-sm-9">
											<div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Predefinidas" type="radio" name="tipoEnvio" value="NONE"  />
												<label class="form-check-label" for="radningunoenv" id="TAREAS_ETQ48">Ninguna</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Predefinidas" type="radio" name="tipoEnvio" value="ALL" checked="" />
												<label class="form-check-label" for="radningunoenv" id="TAREAS_ETQ49">Todos</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Predefinidas" type="radio" name="tipoEnvio" value="MEX" />
												<label class="form-check-label" for="radningunoenv" id="TAREAS_ETQ50">Proveedores de M&eacute;xico</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkEnvioA_Predefinidas" type="radio" name="tipoEnvio" value="USA" />
												<label class="form-check-label" for="radningunoenv" id="TAREAS_ETQ51">Proveedores de USA</label>
											  </div>
										</div>
									</div>

									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="" id="TAREAS_ETQ52">Cargar en Sistema A </label>
										<div class="col-sm-9">
											<div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkCargar_Predefinidas" type="radio" name="cargarSistema" value="NONE" />
												<label class="form-check-label" for="radningunosistenv" id="TAREAS_ETQ53">Ninguna</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkCargar_Predefinidas" type="radio" name="cargarSistema" value="ALL" checked="" />
												<label class="form-check-label" for="radningunosistenv" id="TAREAS_ETQ54">Todos</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkCargar_Predefinidas" type="radio" name="cargarSistema" value="MEX" />
												<label class="form-check-label" for="radningunosistenv" id="TAREAS_ETQ55">Proveedores de M&eacute;xico</label>
											  </div>
											  <div class="form-check form-check-inline">
												<input class="form-check-input rounded-circle form-check-line-through form-check-input-primary" id="chkCargar_Predefinidas" type="radio" name="cargarSistema" value="USA" />
												<label class="form-check-label" for="radningunosistenv" id="TAREAS_ETQ56">Proveedores de USA</label>
											  </div>
										</div>
									</div>

									<div class="mb-2 row">
										<div class="d-flex justify-content-evenly bd-highlight">
											<div class="p-2 bd-highlight">
												<button class="btn btn-primary" type="submit" id="TAREAS_Boton_GuardarPreDefinidas">Guardar</button>
											</div>
											<div class="p-2 bd-highlight">
												<button class="btn btn-secondary me-1 mb-1" type="button" onclick="preValidaTareaPredefinidas()" id="TAREAS_Boton_PreValidarPrdefinidas">PreValidar</button>
											</div>
										  </div>
									</div>

				
								</div>
							</form>


	


 <script type="text/javascript">
 
 $(document).ready(function() {
 	  
	 flatpickr(txtfechaTarea_Predefinidas, {
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
	 
	 
	   $("#form-Predefinidas").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });

	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Predefinidas').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
         errorClass: 'help-block animation-pullUp', errorElement: 'div',
         keyUp: true,
         submitHandler: function(form) {
        	 guardarPredefinidas();
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
	   	   	   
	   iniciaFormPredefinidas();
	   calcularEtiquetasTareasEnvioPredifinidasModal();
	});
 
 
 

 function calcularEtiquetasTareasEnvioPredifinidasModal(){
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
					
					document.getElementById("TAREAS_ETQ42").innerHTML = data.ETQ42;
					document.getElementById("TAREAS_ETQ43").innerHTML = data.ETQ43;
					document.getElementById("TAREAS_ETQ44").innerHTML = data.ETQ44;
					document.getElementById("TAREAS_ETQ45").innerHTML = data.ETQ45;
					document.getElementById("TAREAS_ETQ46").innerHTML = data.ETQ46;
					document.getElementById("TAREAS_ETQ47").innerHTML = data.ETQ47;
					document.getElementById("TAREAS_ETQ48").innerHTML = data.ETQ48;
					document.getElementById("TAREAS_ETQ49").innerHTML = data.ETQ49;
					document.getElementById("TAREAS_ETQ50").innerHTML = data.ETQ50;
					document.getElementById("TAREAS_ETQ51").innerHTML = data.ETQ51;
					document.getElementById("TAREAS_ETQ52").innerHTML = data.ETQ52;
					document.getElementById("TAREAS_ETQ53").innerHTML = data.ETQ53;
					document.getElementById("TAREAS_ETQ54").innerHTML = data.ETQ54;
					document.getElementById("TAREAS_ETQ55").innerHTML = data.ETQ55;
					document.getElementById("TAREAS_ETQ56").innerHTML = data.ETQ56;
					
					document.getElementById("TAREAS_Boton_PreValidarPrdefinidas").innerHTML = data.ETQ28;
					document.getElementById("TAREAS_Boton_GuardarPreDefinidas").innerHTML = BTN_GUARDAR_MENU;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasTareasEnvioCorreosModal()_1_'+thrownError);
			}
		});	
	}
 
 
	
 </script>
</html>