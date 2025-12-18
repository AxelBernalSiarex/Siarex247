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

<form id="frmNuevaOrden" class="was-validated" novalidate>
	<input type="hidden" id="folioOrden" class="form-control" name="folioOrden" value="0"/>
	<input type="hidden" id="tipoOrden" class="form-control" name="tipoOrden" value=""/>
	<input type="hidden" id="ESTATUS_ORDEN_NUEVA" class="form-control" name="ESTATUS_ORDEN_NUEVA" value=""/>

	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
					<h5 class="mb-1" id="modal-title-catalogo">title</h5>
				</div>
				<div class="p-4 pb-3">
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="folioEmpresa" id="VISOR_ETQ36">N&uacute;mero de Orden </label>
							<div class="col-sm-4">
								<input id="folioEmpresa" name="folioEmpresa" class="form-control" type="text" maxlength="11" required>
							</div>
							<label class="col-sm-2 col-form-label" for="tipoMoneda" id="VISOR_ETQ37">Tipo de Moneda</label>
							<div class="col-sm-4">
								<div class="form-group">
								<select class="form-select" id="tipoMoneda" name="tipoMoneda" required>
									
								</select>
								</div>
							</div>
						</div>
						
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="descripcion" id="VISOR_ETQ38">Descripci&oacute;n </label>
							<div class="col-sm-10">
								<input id="descripcion" name="descripcion" class="form-control" type="text" maxlength="1024">
							</div>
						</div>
						

					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="monto" id="VISOR_ETQ39">Monto </label>
						<div class="col-sm-4">
							<input id="monto" name="monto" class="form-control" type="number" maxlength="16" required>
						</div>
						<label class="col-sm-2 col-form-label" for="serRecibido" id="VISOR_ETQ40">Servicio Recibido</label>
						<div class="col-sm-4">
							<div class="form-group">
								<div class="form-check form-switch">
									<input class="form-check-input validarGuardar" id="serRecibido" name="serRecibido" type="checkbox" onchange="validarObjetos();" />
							 	</div>
							</div>
						</div>
					</div>
					
					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="cmbEmpleado" id="VISOR_ETQ41">Asignar a Empleado</label>
						<div class="col-sm-4">
							<div class="form-group">
							<select class="form-select validarGuardar" id="cmbEmpleado" name="idEmpleado">
								
							</select>
							</div>
						</div>
						<label class="col-sm-2 col-form-label" for="cmbCentroCosto" id="VISOR_ETQ42">Centro de Costos</label>
						<div class="col-sm-4">
							<div class="form-group">
							<select class="form-select validarGuardar" id="cmbCentroCosto" name="idCentro">
							</select>
							</div>
						</div>
						
					</div>
					
					
					<div class="mb-2 row">
						
						<label class="col-sm-2 col-form-label" for="numeroCuenta" id="VISOR_ETQ43">N&uacute;mero de Cuenta </label>
						<div class="col-sm-4">
							<input id="numeroCuenta" name="numeroCuenta" class="form-control validarGuardar" type="text" maxlength="100">
						</div>
						
						<label class="col-sm-2 col-form-label" for="centroCostosProveedor" id="VISOR_ETQ44">Centro de Costos del Proveedor </label>
						<div class="col-sm-4">
							<input id="centroCostosProveedor" name="centroCostosProveedor" class="form-control validarGuardar" type="text" maxlength="100">
						</div>
						
					</div>
					
					<div class="mb-2 row" id="divEliminar">
						<label class="col-sm-2 col-form-label" for="eliminarFactura" id="VISOR_ETQ45">Eliminar Factura</label>
						<div class="col-sm-1">
							<div class="form-group">
								<div class="form-check form-switch">
									<input class="form-check-input validarGuardar" id="eliminarFactura" name="eliminarFactura" type="checkbox" />
							 	</div>
							</div>
						</div>
						
					</div>
					
					
					<div class="mb-2 row" id="divPagada">
						<label class="col-sm-2 col-form-label" for="facturaPagada" id="VISOR_ETQ46">Factura Pagada</label>
						<div class="col-sm-1">
							<div class="form-group">
								<div class="form-check form-switch">
									<input class="form-check-input validarGuardar" id="facturaPagada" name="facturaPagada" type="checkbox" />
							 	</div>
							</div>
						</div>
						
						<label class="col-sm-2 col-form-label" for="fechaPago" id="VISOR_ETQ47">Fecha de Pago</label>
						<div class="col-sm-2">
							<div class="form-group">
								<input class="form-control datetimepicker flatpickr-input active validarGuardar" id="fechaPago" name="fechaPago" type="text" placeholder="dd/mm/yyyy" data-options="{&quot;disableMobile&quot;:true}" readonly="readonly">
							</div>
						</div>
						
						
						<label class="col-sm-2 col-form-label" for="envioCorreo" id="VISOR_ETQ48">Enviar Correo </label>
						<div class="col-sm-1">
							<div class="form-group">
								<div class="form-check form-switch">
									<input class="form-check-input validarGuardar" id="envioCorreo" name="envioCorreo" type="checkbox" />
							 	</div>
							</div>
						</div>
						
					</div>					
	
					<div class="mb-2 row" id="omitirComplemento">
						<label class="col-sm-2 col-form-label" for="omitirComplemento" id="VISOR_ETQ49">Omitir Complemento</label>
						<div class="col-sm-1">
							<div class="form-group">
								<div class="form-check form-switch">
									<input class="form-check-input validarGuardar" id="omitirComplemento" name="omitirComplemento" type="checkbox" />
							 	</div>
							</div>
						</div>
					</div>	
					
									
					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="claveProveedor" id="VISOR_ETQ50">Proveedor</label>
						<div class="col-sm-10">
							<div class="form-group">
							<select class="form-select" id="claveProveedor" name="claveProveedor" required >
							</select>
							</div>
						</div>
					</div>					
				</div>

			</div>
			<div class="modal-footer">
				<button type="submit" id="btnSometer" class="btn btn-primary"> Guardar </button>
				<button class="btn btn-secondary" id="btnCerrar" type="button" data-bs-dismiss="modal"> Cerrar </button>
			</div>
		</div>
	</div>
</form>

<script type="text/javascript">


	$(document).ready(function () {

		
		 flatpickr(fechaPago, {
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
	   	          longhand: ['Enero', 'Febrero', 'Мarzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	   	        },
	   	      },
	   	    }); 
		 
		
		$("#frmNuevaOrden").on('submit', function (event) {
			   $(this).addClass('was-validated');
			 });

		   
		   /* Necesario para validacion del form y del Select2
		   -----------------------------------------------------*/
		   $('#frmNuevaOrden').validate({
			   ignore: 'input[type=hidden]',
			   focusInvalid: false,
	           errorClass: 'help-block animation-pullUp', errorElement: 'div',
	           keyUp: true,
	           submitHandler: function(form) {
	        	   var folioOrden = $('#folioOrden').val();
	        	   if (folioOrden == 0){
	        		   guardarOrden();
	        	   }else{
	        		   actualizarOrden();
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
		   
		   calcularEtiquetasVisorNuevo();
	});
	

	  function calcularEtiquetasVisorNuevo(){
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
						
						document.getElementById("VISOR_ETQ36").innerHTML = data.ETQ36;
						document.getElementById("VISOR_ETQ37").innerHTML = data.ETQ37;
						document.getElementById("VISOR_ETQ38").innerHTML = data.ETQ38;
						document.getElementById("VISOR_ETQ39").innerHTML = data.ETQ39;
						document.getElementById("VISOR_ETQ40").innerHTML = data.ETQ40;
						document.getElementById("VISOR_ETQ41").innerHTML = data.ETQ41;
						document.getElementById("VISOR_ETQ42").innerHTML = data.ETQ42;
						document.getElementById("VISOR_ETQ43").innerHTML = data.ETQ43;
						document.getElementById("VISOR_ETQ44").innerHTML = data.ETQ44;
						document.getElementById("VISOR_ETQ45").innerHTML = data.ETQ45;
						document.getElementById("VISOR_ETQ46").innerHTML = data.ETQ46;
						document.getElementById("VISOR_ETQ47").innerHTML = data.ETQ47;
						document.getElementById("VISOR_ETQ48").innerHTML = data.ETQ48;
						document.getElementById("VISOR_ETQ49").innerHTML = data.ETQ49;
						document.getElementById("VISOR_ETQ50").innerHTML = data.ETQ50;
						document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("btnCerrar").innerHTML = BTN_CERRAR_MENU;
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	  
	function cargaTipoMoneda(tipoMoneda) {
			try {
				$('#tipoMoneda').empty();
				
				if (tipoMoneda == 'MXN'){
					$('#tipoMoneda').append($('<option></option>').attr('selected', 'selected').attr('value', 'MXN').text('PESOS'));
					$('#tipoMoneda').append($('<option></option>').attr('value', 'USD').text('DOLARES'));	
				}else {
					$('#tipoMoneda').append($('<option></option>').attr('value', 'MXN').text('PESOS'));
					$('#tipoMoneda').append($('<option></option>').attr('selected', 'selected').attr('value', 'USD').text('DOLARES'));
				}
			}catch(e) {
				alert("cargaTipoMoneda()_"+e);
			} 
		}
	  
	  
	  function cargaEmpleados(idEmpleado) {
			try {
				$('#cmbEmpleado').empty();
				$.ajax({
					url:  '/siarex247/catalogos/empleados/comboEmpleados.action',
		           data : null,
		           type: 'POST',
		            dataType : 'json',
				    success: function(data){
				    	$('#cmbEmpleado').empty();
				    	$.each(data.data, function(key, text) {
				    		if (idEmpleado == text.idEmpleado){
				    			$('#cmbEmpleado').append($('<option></option>').attr('selected', 'selected').attr('value', text.idEmpleado).text(text.nombreCompleto));
				    		}else{
				    			$('#cmbEmpleado').append($('<option></option>').attr('value', text.idEmpleado).text(text.nombreCompleto));	
				    		}
				    		
				      	});
				    }
				});
			}catch(e) {
				alert("cargaEmpleados()_"+e);
			} 
		}
		  
	  
	  function cargaCentros(idCentro) {
			try {
				$('#cmbCentroCosto').empty();
				$.ajax({
					url:  '/siarex247/catalogos/centroCostos/comboCentrosCostos.action',
		           data : null,
		           type: 'POST',
		            dataType : 'json',
				    success: function(data){
				    	$('#cmbCentroCosto').empty();
				    	$.each(data.data, function(key, text) {
				    		if (idCentro == text.idCentroCosto){
				    			$('#cmbCentroCosto').append($('<option></option>').attr('selected', 'selected').attr('value', text.idCentroCosto).text(text.departamento));
				    		}else{
				    			$('#cmbCentroCosto').append($('<option></option>').attr('value', text.idCentroCosto).text(text.departamento));	
				    		}
				    		
				      	});
				    }
				});
			}catch(e) {
				alert("cargaCentros()_"+e);
			} 
		}
	  
	  
	  function cargaProveedores(claveProveedor) {
			try {
				$('#claveProveedor').empty();
				$.ajax({
					url:  '/siarex247/catalogos/proveedores/comboProveedores.action',
		           data : null,
		           type: 'POST',
		            dataType : 'json',
				    success: function(data){
				    	// $('#claveProveedor').empty();
				    	// $('#claveProveedor').append($('<option></option>').attr('value', 0).text('Seleccione un Proveedor'));
				    	$.each(data.data, function(key, text) {
				    		if (claveProveedor == text.claveRegistro){
				    			$('#claveProveedor').append($('<option></option>').attr('selected', 'selected').attr('value', text.claveRegistro).text(text.razonSocial));
				    		}else{
				    			$('#claveProveedor').append($('<option></option>').attr('value', text.claveRegistro).text(text.razonSocial));	
				    		}
				    		
				      	});
				    }
				});
			}catch(e) {
				alert("cargaProveedores()_"+e);
			} 
		}
	  
	  
</script>

</html>