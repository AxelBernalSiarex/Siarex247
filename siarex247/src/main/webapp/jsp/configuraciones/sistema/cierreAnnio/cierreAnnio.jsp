
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
     <script src="/siarex247/jsp/configuraciones/sistema/cierreAnnio/cierreAnnio.js"></script>   
</head>


				<form id="form-CierreAnnio" class="was-validated" novalidate>
						<div class="p-2">
							<div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="tipoCierre" id="CONF_SISTEMA_ETQ17">Tipo de Cierre</label>
								<div class="col-sm-3">
								  <div class="form-group">
										<select class="form-select" id="tipoCierre" name="tipoCierre" >
											<option value="F" selected="selected" id="CONF_SISTEMA_ETQ18">CIERRE DE FACTURAS</option>
											<option value="C" id="CONF_SISTEMA_ETQ19">CIERRE DE COMPLEMENTOS DE PAGO</option>
										</select>
								  </div>
								</div>
							  </div>
<!--  
							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="anio">A&ntilde;o</label>
								<div class="col-sm-1">
								  <div class="form-group">
									<input class="form-control" type="number" name="anio" id="anio" maxlength="4"/>
									
									<div class="position-relative" id="emoji-button" onclick="buscaTipoCierre();">
									  <div class="btn btn-info" data-picmo='{"position":"bottom-start"}'><span class="fab fa-sistrix"></span></div>
									</div>
								  </div>
								</div>
								
							  </div>
							  
 -->							  


							<div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="anio" id="CONF_SISTEMA_ETQ20">A&ntilde;o</label>
								<div class="col-sm-1">
			   						<input id="anio" name="anio" class="form-control" type="text" maxlength="4" required />
			 					</div>
								 <div class="col-sm-2">
									<div class="position-relative" id="emoji-button" onclick="buscaTipoCierre();">
										<div class="btn btn-info" data-picmo='{"position":"bottom-start"}'><span class="fab fa-sistrix"></span></div>
		
							  	</div>
								</div>
							</div>	
					
							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="fechaApartir" id="CONF_SISTEMA_ETQ21">Recibir a partir de la fecha </label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control datetimepicker flatpickr-input active" id="fechaApartir"
											name="fechaApartir" type="text" placeholder="dd/mm/yyyy"
											data-options='{"disableMobile":true}' readonly/>
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="mensajeError1" id="CONF_SISTEMA_ETQ22">Mensaje de Error </label>
								<div class="col-sm-5">
								  <div class="form-group">
									<textarea class="form-control" id="mensajeError1" name="mensajeError1"
											rows="4"></textarea>
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="fechaHasta" id="CONF_SISTEMA_ETQ23">Recibir Facturas Hasta </label>
								<div class="col-sm-3">
								  <div class="form-group">
									<input class="form-control datetimepicker flatpickr-input active" id="fechaHasta"
											name="fechaHasta" type="text" placeholder="dd/mm/yyyy"
											data-options='{"disableMobile":true}' />
								  </div>
								</div>
							  </div>

							  <div class="mb-2 row">
								<label class="col-sm-2 col-form-label" for="mensajeError2" id="CONF_SISTEMA_ETQ24">Mensaje de Error </label>
								<div class="col-sm-5">
								  <div class="form-group">
									<textarea class="form-control" id="mensajeError2" name="mensajeError2"
											rows="4"></textarea>
								  </div>
								</div>
							  </div>

								<div class="col-md-6 text-center">
									<button type="submit" id="btnsavecierre" class="btn btn-primary">Guardar</button>
								</div>
						</div>

					</form>

			
<script type="text/javascript">
$(document).ready(function() {
	
	 flatpickr(fechaApartir, {
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
	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	        },
	      },
	    }); 
	 
	 
	 flatpickr(fechaHasta, {
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
	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	        },
	      },
	    }); 
	
	$("#form-CierreAnnio").on('submit', function (event) {
	   $(this).addClass('was-validated');
	 });
   
   /* Necesario para validacion del form y del Select2
   -----------------------------------------------------*/
   $('#form-CierreAnnio').validate({
	   ignore: 'input[type=hidden]',
	   focusInvalid: false,
       keyUp: true,
       submitHandler: function(form) {
    	   guardarDatosCierre()
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
   
   $('#tipoCierre').select2({
		theme: 'bootstrap-5'
	});
   
   iniciaFormCierreAnnio();
   calcularEtiquetasConfSistemaCloseYear();
});
  
  

function calcularEtiquetasConfSistemaCloseYear(){
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
					
					document.getElementById("CONF_SISTEMA_ETQ17").innerHTML = data.ETQ17;
					document.getElementById("CONF_SISTEMA_ETQ18").innerHTML = data.ETQ18;
					document.getElementById("CONF_SISTEMA_ETQ19").innerHTML = data.ETQ19;
					document.getElementById("CONF_SISTEMA_ETQ20").innerHTML = data.ETQ20;
					document.getElementById("CONF_SISTEMA_ETQ21").innerHTML = data.ETQ21;
					document.getElementById("CONF_SISTEMA_ETQ22").innerHTML = data.ETQ22;
					document.getElementById("CONF_SISTEMA_ETQ23").innerHTML = data.ETQ23;
					document.getElementById("CONF_SISTEMA_ETQ24").innerHTML = data.ETQ24;
					document.getElementById("btnsavecierre").innerHTML = BTN_GUARDAR_MENU;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasConfSistemaCloseYear()_1_'+thrownError);
			}
		});	
	}
	

</script>
                                
</html>