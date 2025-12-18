<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />

	<script src="/siarex247/jsp/visor/fechaPago/fechaRecepcion.js"></script>

	<script>
		var isRTL = JSON.parse(localStorage.getItem("isRTL"));
		if (isRTL) {
			var linkDefault = document.getElementById("style-default");
			var userLinkDefault = document.getElementById("user-style-default");
			linkDefault.setAttribute("disabled", true);
			userLinkDefault.setAttribute("disabled", true);
			document.querySelector("html").setAttribute("dir", "rtl");
		} else {
			var linkRTL = document.getElementById("style-rtl");
			var userLinkRTL = document.getElementById("user-style-rtl");
			linkRTL.setAttribute("disabled", true);
			userLinkRTL.setAttribute("disabled", true);
		}
	</script>
</head>

<form id="frmFechaPago" class="was-validated" novalidate>
	<input type="hidden" id="folioOrden_FechaPago" class="form-control" name="folioOrden" value="0"/>

	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
					<h5 class="mb-1" id="VISOR_TITLE15">Actualizar Fecha de Pago a Ordenes de Compra</h5>
				</div>
				<div class="p-4 pb-3">
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="folioEmpresa_FechaPago" id="Visor_FechaRecepcion_Etiqueta_NumeroOrden">N&uacute;mero de Orden :</label>
						<div class="col-sm-8">
							<input id="folioEmpresa_FechaPago" name="folioEmpresa" class="form-control" type="text" maxlength="11" >
						</div>
					</div>
						
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="fechaActual_FechaPago" id="VISOR_ETQ95">Fecha de Pago Actual </label>
						<div class="col-sm-8">
							<input id="fechaActual_FechaPago" name="fechaActual" class="form-control" type="text" maxlength="1024">
						</div>
					</div>
						
					<div class="mb-2 row">
						
						<label class="col-sm-4 col-form-label" for="fechaNueva_FechaPago" id="VISOR_ETQ96">Nueva Fecha de Pago</label>
						<div class="col-sm-8">
							<div class="form-group">
								<input class="form-control datetimepicker flatpickr-input active" id="fechaNueva_FechaPago" name="fechaPago" type="text" placeholder="dd/mm/yyyy" data-options="{&quot;disableMobile&quot;:true}" required>
							</div>
						</div>
					</div>					
	
										
				</div>

			</div>
			<div class="modal-footer">
				<button type="submit" id="btnGuardar_FechaRecepcion" class="btn btn-primary"> Guardar </button>
				<button class="btn btn-secondary" id="btnCerrar_FechaRecepcion" type="button" data-bs-dismiss="modal"> Cerrar </button>
			</div>
		</div>
	</div>
</form>

<script type="text/javascript">


	$(document).ready(function () {

		
		 flatpickr(fechaNueva_FechaPago, {
	   	      minDate: '1920-01-01', 
	   	      //dateFormat : "d-m-Y", 
	   	      dateFormat : "Y-m-d",
	   	      locale: {
	   	        firstDayOfWeek: 1,
	   	        weekdays: {
	   	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
	   	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'],         
	   	        }, 
	   	        months: {
	   	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
	   	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	   	        },
	   	      },
	   	    }); 
		 
		
		$("#frmFechaPago").on('submit', function (event) {
			   $(this).addClass('was-validated');
			 });

		   
		   /* Necesario para validacion del form y del Select2
		   -----------------------------------------------------*/
		   $('#frmFechaPago').validate({
			   ignore: 'input[type=hidden]',
			   focusInvalid: false,
	           errorClass: 'help-block animation-pullUp', errorElement: 'div',
	           keyUp: true,
	           submitHandler: function(form) {
	        	   modificarFechaPago();
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
		   
		   calcularEtiquetasVisorFechaRecepcion();
	});
	
	

	 function calcularEtiquetasVisorFechaRecepcion(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'VISOR'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("VISOR_TITLE15").innerHTML = data.TITLE15;
						document.getElementById("VISOR_ETQ95").innerHTML = data.ETQ95;
						document.getElementById("VISOR_ETQ96").innerHTML = data.ETQ96;
						document.getElementById("Visor_FechaRecepcion_Etiqueta_NumeroOrden").innerHTML = data.ETQ36;
						
						
						document.getElementById("btnGuardar_FechaRecepcion").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("btnCerrar_FechaRecepcion").innerHTML = BTN_CERRAR_MENU;
						
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 
	  
</script>

</html>