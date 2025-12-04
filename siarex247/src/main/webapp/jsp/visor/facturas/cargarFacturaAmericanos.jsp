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


	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="VISOR_TITLE6">Cargar documentos de factura</h5>
				</div>
				<div class="p-4 pb-3">
				
					<div class="card overflow-hidden">
						<div class="accordion" id="accordion1">
							<div class="accordion-item"><!-- accordion-item Datos Generales -->
								<h2 class="accordion-header" id="heading1">
									<button class="accordion-button bg-200" type="button" data-bs-toggle="collapse" data-bs-target="#collapse1" aria-expanded="true" aria-controls="collapse1" id="VISOR_ETQ60">Por Carga de Archivo</button>
								</h2>
								<div class="accordion-collapse collapse show" id="collapse1" aria-labelledby="heading1" data-bs-parent="#accordion1">
									<div class="accordion-body">
										<form id="frmCargarAmericanosArchivo" class="was-validated" novalidate>
			                	         <div id="overSeccionAmericanosArchivo" class="overlay" style="display: none;text-align: right;">
											<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
										 </div>
										 
										 <div class="mb-2 row" id="valError_AmericanosArchivo_Cargar">
												<div class="col-sm-12">
													<div class="form-group">
														<div class="alert alert-danger border-2 d-flex align-items-center" role="alert">
														  <div class="bg-danger me-3 icon-item"><span class="fas fa-times-circle text-white fs-3"></span></div>
														  <p class="mb-0 flex-1" id="VISOR_CargarAmericana_Mensaje_ErrorValidacion1">Error en Validaci&oacute;n de Factura</p>
														</div>
													</div>
												</div>
											</div>
											
											<div class="mb-2 row" id="valExito_AmericanosArchivo_Cargar">
												<div class="col-sm-12">
													<div class="form-group">
														<div class="alert alert-success border-2 d-flex align-items-center" role="alert">
														  <div class="bg-success me-3 icon-item"><span class="fas fa-check-circle text-white fs-3"></span></div>
														  <p class="mb-0 flex-1" id="VISOR_CargarAmericana_Mensaje_CorrectaValidacion1">Validaci&oacute;n de Factura Correcta</p>
														</div>
													</div>
												</div>
											</div>
											<div class="mb-2 row">
												<label class="col-sm-2 col-form-label" for="folioEmpresa" id="VISOR_CargarAmericana_Etiqueta_NumeroOrden1" >N&uacute;mero de Orden :</label>
												<div class="col-sm-4">
						   						<input id="folioEmpresa_AmericanosArchivo_Cargar" name="folioEmpresa" class="form-control" type="number" maxlength="10"  required />
						 					</div>
											</div>
											<div class="mb-2 row">
												<label class="col-sm-2 col-form-label" for="filePDF_AmericanosArchivo_Cargar" id="VISOR_CargarAmericana_Etiqueta_ArchivoPDF1">Archivo PDF</label>
												<div class="col-sm-10">
													<input class="form-control form-control-sm" id="filePDF_AmericanosArchivo_Cargar" name="filePDF" type="file" accept="application/pdf" required/>
											 	</div>
											</div>
											<div class="mb-2 row">
												<label class="col-sm-2 col-form-label" for="fileTXT_AmericanosArchivo_Cargar" id="VISOR_CargarAmericana_Etiqueta_ArchivoTXT1">Archivo TXT </label>
												<div class="col-sm-10">
													<input class="form-control form-control-sm" id="fileTXT_AmericanosArchivo_Cargar" name="fileTXT" type="file" accept="text/plain" required/>
										 		</div>
											</div>	
											
											<div class="mb-2 row">
								              <label class="col-sm-2 col-form-label" for="mensajeRespuesta_AmericanosArchivo_Cargar" id="VISOR_ETQ61">Respuesta</label>
								              <div class="col-sm-10">
								                <textarea class="form-control" id="mensajeRespuesta_AmericanosArchivo_Cargar" name="mensajeRespuesta" rows="5" readonly="readonly"></textarea>
								              </div>
								            </div>
											
											
											
											<div class="mb-2 row">
											   <div class="col-sm-12 text-center" style="text-align: center; margin-top: 30px;">
											      <button class="btn btn-falcon-success btn-sm me-1 mb-2 mb-sm-1" type="submit" id="btnValidar_AmericanosArchivo_Cargar"><span class="fas fa-check me-1"> </span>Validar</button>
											   </div>
											</div>	
									    </form>
										
									</div>
								</div>	
							</div>
							
							
							<div class="accordion-item"> <!-- accordion-item Datos de Banco en Dolares -->
								<h2 class="accordion-header" id="heading6">
									<button class="accordion-button bg-200 collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse2" aria-expanded="true" aria-controls="collapse2" id="VISOR_ETQ62">Por Captura de Datos</button>
								</h2>
								<div class="accordion-collapse collapse" id="collapse2" aria-labelledby="heading2" data-bs-parent="#accordion1">
      								<div class="accordion-body">
      									<form id="frmCargarAmericanosCaptura" class="was-validated" novalidate>
      									 <div id="overSeccionAmericanosCaptura" class="overlay" style="display: none;text-align: right;">
											<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
										 </div>
										 
										 <div class="mb-2 row" id="valError_AmericanosCaptura_Cargar">
												<div class="col-sm-12">
													<div class="form-group">
														<div class="alert alert-danger border-2 d-flex align-items-center" role="alert">
														  <div class="bg-danger me-3 icon-item"><span class="fas fa-times-circle text-white fs-3"></span></div>
														  <p class="mb-0 flex-1" id="VISOR_CargarAmericana_Mensaje_ErrorValidacion2">Error en Validaci&oacute;n de Factura</p>
														</div>
													</div>
												</div>
											</div>
											
											<div class="mb-2 row" id="valExito_AmericanosCaptura_Cargar">
												<div class="col-sm-12">
													<div class="form-group">
														<div class="alert alert-success border-2 d-flex align-items-center" role="alert">
														  <div class="bg-success me-3 icon-item"><span class="fas fa-check-circle text-white fs-3"></span></div>
														  <p class="mb-0 flex-1" id="VISOR_CargarAmericana_Mensaje_CorrectaValidacion2">Validaci&oacute;n de Factura Correcta</p>
														</div>
													</div>
												</div>
											</div>
											<div class="mb-2 row">
												<label class="col-sm-2 col-form-label" for="folioEmpresa" id="VISOR_CargarAmericana_Etiqueta_NumeroOrden2">N&uacute;mero de Orden :</label>
												<div class="col-sm-4">
						   							<input id="folioEmpresa_AmericanosCaptura_Cargar" name="folioEmpresa" class="form-control" type="number" maxlength="10"  required />
						 						</div>
						 						<label class="col-sm-2 col-form-label" for="serieFolio_AmericanosCaptura_Cargar" id="VISOR_ETQ63">Factura :</label>
												<div class="col-sm-4">
						   							<input id="serieFolio_AmericanosCaptura_Cargar" name="serieFolio" class="form-control" type="text" maxlength="50"  required />
						 						</div>
						 					</div>
						 					<div class="mb-2 row">
												<label class="col-sm-2 col-form-label" for="fechaFactura_AmericanosCaptura_Cargar" id="VISOR_ETQ64">Fecha de Factura :</label>
												<div class="col-sm-4">
						   							<input class="form-control datetimepicker flatpickr-input active" id="fechaFactura_AmericanosCaptura_Cargar" name="fechaFactura" type="text" placeholder="mm/dd/yyyy" data-options="{&quot;disableMobile&quot;:true}" required>
						 						</div>
						 						<label class="col-sm-2 col-form-label" for="monto_AmericanosCaptura_Cargar" id="VISOR_CargarAmericana_Etiqueta_Monto">Monto :</label>
												<div class="col-sm-4">
						   							<input id="monto_AmericanosCaptura_Cargar" name="monto" class="form-control" type="number" maxlength="10"  required />
						 						</div>
						 					</div>
						 					<div class="mb-2 row">
												<label class="col-sm-2 col-form-label" for="tipoMoneda_AmericanosCaptura_Cargar" id="VISOR_CargarAmericana_Etiqueta_TipoMoneda">Tipo de Moneda :</label>
												<div class="col-sm-4">
						   							<select class="form-select" id="tipoMoneda_AmericanosCaptura_Cargar" name="tipoMoneda" required> </select>
						 						</div>
						 						<label class="col-sm-6 col-form-label" for="" style="color: navy; font-size: 10px;" id="VISOR_TEXTO1">Recuerde que el monto indicado en factura debe coincidir con el monto de la orden de compra </label>
												
						 					</div>
						 					<div class="mb-2 row">
												<label class="col-sm-2 col-form-label" for="filePDF_AmericanosCaptura_Cargar" id="VISOR_CargarAmericana_Etiqueta_ArchivoPDF2">Archivo PDF</label>
												<div class="col-sm-10">
													<input class="form-control form-control-sm" id="filePDF_AmericanosCaptura_Cargar" name="filePDF" type="file" accept="application/pdf" required/>
											 	</div>
											</div>
											<div class="mb-2 row">
								              <label class="col-sm-2 col-form-label" for="mensajeRespuesta_AmericanosCaptura_Cargar" id="VISOR_CargarAmericana_Etiqueta_Respuesta2">Respuesta</label>
								              <div class="col-sm-10">
								                <textarea class="form-control" id="mensajeRespuesta_AmericanosCaptura_Cargar" name="mensajeRespuesta" rows="5" readonly="readonly"></textarea>
								              </div>
								            </div>
								            
											<div class="mb-2 row">
											   <div class="col-sm-12 text-center" style="text-align: center; margin-top: 30px;">
											      <button class="btn btn-falcon-success btn-sm me-1 mb-2 mb-sm-1" type="submit" id="btnValidar_AmericanosCaptura_Cargar"><span class="fas fa-check me-1"> </span>Validar</button>
											   </div>
											</div>	
											
						 				</form>		
      								</div>
      							</div>	
							</div>
							
							
							
						</div>
					
					</div>
				
				</div>
			</div>
			<div class="modal-footer">	
			    <button class="btn btn-primary" type="button"   id="btnSometer_AmericanosCaptura_Cargar" onclick="guardarCambiosAmericano();" >Guardar</button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="btnCerrar_Americanos_Cargar">Cerrar</button>
			</div>
		</div>
	</div>


 <script type="text/javascript">
 
 
 $(document).ready(function() {
	 
	   $("#frmCargarAmericanosCaptura").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#frmCargarAmericanosCaptura').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
	       errorClass: 'help-block animation-pullUp', errorElement: 'div',
	       keyUp: true,
	       submitHandler: function(form) {
	      	 cargarFacturaAmericanaCaptura();
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
	  
	   
	 
	   $("#frmCargarAmericanosArchivo").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	  
	   $('#frmCargarAmericanosArchivo').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
         errorClass: 'help-block animation-pullUp', errorElement: 'div',
         keyUp: true,
         submitHandler: function(form) {
        	 cargarFacturaAmericanaArchivo();
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
	   

	   
	   flatpickr(fechaFactura_AmericanosCaptura_Cargar, {
	  	      minDate: '1920-01-01', 
	  	      //dateFormat : "d-m-Y", 
	  	      dateFormat : "m/d/Y",
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
	   
	   
	   cargaMonedaAmericana();
	   
	   calcularEtiquetasVisorCargarFacturaAmericana();
 });
 

 function calcularEtiquetasVisorCargarFacturaAmericana(){
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
					
					document.getElementById("VISOR_TITLE6").innerHTML = data.TITLE6;
					document.getElementById("VISOR_ETQ60").innerHTML = data.ETQ60;
					document.getElementById("VISOR_ETQ61").innerHTML = data.ETQ61;
					document.getElementById("VISOR_ETQ62").innerHTML = data.ETQ62;
					document.getElementById("VISOR_ETQ63").innerHTML = data.ETQ63;
					document.getElementById("VISOR_ETQ64").innerHTML = data.ETQ64;
					document.getElementById("VISOR_TEXTO1").innerHTML = data.TEXTO1;
					
					
					document.getElementById("VISOR_CargarAmericana_Mensaje_ErrorValidacion1").innerHTML = data.ETQ53;
					document.getElementById("VISOR_CargarAmericana_Mensaje_CorrectaValidacion1").innerHTML = data.ETQ54;
					document.getElementById("VISOR_CargarAmericana_Etiqueta_NumeroOrden1").innerHTML = data.ETQ55;
					document.getElementById("VISOR_CargarAmericana_Etiqueta_ArchivoPDF1").innerHTML = data.ETQ56;
					document.getElementById("VISOR_CargarAmericana_Etiqueta_ArchivoTXT1").innerHTML = data.ETQ59;
					document.getElementById("VISOR_CargarAmericana_Mensaje_ErrorValidacion2").innerHTML = data.ETQ53;
					document.getElementById("VISOR_CargarAmericana_Mensaje_CorrectaValidacion2").innerHTML = data.ETQ54;
					document.getElementById("VISOR_CargarAmericana_Etiqueta_NumeroOrden2").innerHTML = data.ETQ55;
					document.getElementById("VISOR_CargarAmericana_Etiqueta_Monto").innerHTML = data.ETQ39;
					document.getElementById("VISOR_CargarAmericana_Etiqueta_TipoMoneda").innerHTML = data.ETQ37;
					document.getElementById("VISOR_CargarAmericana_Etiqueta_ArchivoPDF2").innerHTML = data.ETQ56;
					document.getElementById("VISOR_CargarAmericana_Etiqueta_Respuesta2").innerHTML = data.ETQ61;
					
					
					
					document.getElementById("btnSometer_AmericanosCaptura_Cargar").innerHTML = BTN_GUARDAR_MENU;
					document.getElementById("btnCerrar_Americanos_Cargar").innerHTML = BTN_CERRAR_MENU;
					document.getElementById("btnValidar_AmericanosArchivo_Cargar").innerHTML = BTN_VALIDAR;
					document.getElementById("btnValidar_AmericanosCaptura_Cargar").innerHTML = BTN_VALIDAR;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
 
 

 function cargaMonedaAmericana() {
		try {
			$('#tipoMoneda_AmericanosCaptura_Cargar').empty();
			$('#tipoMoneda_AmericanosCaptura_Cargar').append($('<option></option>').attr('value', 'USD').text('DOLARES'));
			$('#tipoMoneda_AmericanosCaptura_Cargar').append($('<option></option>').attr('value', 'MXN').text('PESOS'));
			
		}catch(e) {
			alert("cargaMonedaAmericana()_"+e);
		} 
	}
 
  
 </script>
</html>