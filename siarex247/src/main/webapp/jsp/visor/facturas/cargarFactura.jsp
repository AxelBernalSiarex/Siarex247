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
	   				<h5 class="mb-1" id="VISOR_TITLE5">Cargar documentos de factura</h5>
				</div>
				<div class="p-4 pb-3">
				
					<div class="card overflow-hidden">
			
						<div class="card-header p-0 scrollbar-overlay border-bottom">
							<ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabAlarmas" role="tablist">
								<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1 active" id="tabIndividual" data-bs-toggle="tab" href="#infoIndividual" role="tab" aria-controls="infoIndividual" aria-selected="true">
										<span class="fas fa-address-card text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="VISOR_ETQ51">Individual</h6>
									</a>
								</li>
								
		                      	<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabMultiple" data-bs-toggle="tab" href="#infoMultiple" role="tab" aria-controls="infoMultiple" aria-selected="false">
										<span class="fas fa-key icon text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="VISOR_ETQ52">Multiple</h6>
		                      		</a>
		                      	</li>
							</ul>
						</div>
		                
		                <div class="tab-content">
		                	
							<!-- Informacion Individual -->
		                	<div class="card-body bg-light tab-pane active" id="infoIndividual" role="tabpanel" aria-labelledby="tabIndividual">
									<form id="frmCargarFactura" class="was-validated" novalidate>
										<div class="mb-2 row" id="valError_Individual_Cargar">
											<div class="col-sm-12">
												<div class="form-group">
													<div class="alert alert-danger border-2 d-flex align-items-center" role="alert">
													  <div class="bg-danger me-3 icon-item"><span class="fas fa-times-circle text-white fs-3"></span></div>
													  <p class="mb-0 flex-1" id="VISOR_ETQ53">Error en Validación de Factura</p>
													</div>
												</div>
											</div>
										</div>
										
										<div class="mb-2 row" id="valExito_Individual_Cargar">
											<div class="col-sm-12">
												<div class="form-group">
													<div class="alert alert-success border-2 d-flex align-items-center" role="alert">
													  <div class="bg-success me-3 icon-item"><span class="fas fa-check-circle text-white fs-3"></span></div>
													  <p class="mb-0 flex-1" id="VISOR_ETQ54">Validacion de Factura Correcta</p>
													</div>
												</div>
											</div>
										</div>
										
									    <div id="overSeccionIndividual" class="overlay" style="display: none;text-align: right;">
											<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
										 </div>
										 
										<div class="mb-2 row">
											<label class="col-sm-3 col-form-label" for="folioEmpresa" id="VISOR_ETQ55">Número de Orden</label>
											<div class="col-sm-3">
						   						<input id="folioEmpresa_Cargar" name="folioEmpresa" class="form-control" type="number" required />
						 					</div>
										</div>
										<div class="mb-2 row">
											<label class="col-sm-3 col-form-label" for="filePDF_Individual_Cargar" id="VISOR_ETQ56">Archivo PDF</label>
											<div class="col-sm-9">
												<input class="form-control form-control-sm" id="filePDF_Individual_Cargar" name="filePDF" type="file" accept="application/pdf" required />
										 	</div>
										</div>	
										<div class="mb-2 row">
											<label class="col-sm-3 col-form-label" for="fileXML_Individual_Cargar" id="VISOR_ETQ57">Archivo XML</label>
											<div class="col-sm-9">
												<input class="form-control form-control-sm" id="fileXML_Individual_Cargar" name="fileXML" type="file" accept="text/xml" required/>
										 	</div>
										</div>
											
										<div class="mb-2 row">
							              <label class="col-sm-3 col-form-label" for="mensajeError" id="VISOR_ETQ58">Mensaje de Respuesta</label>
							              <div class="col-sm-9">
							                <textarea class="form-control" id="mensajeError_Individual_Cargar" name="mensajeError" rows="7" readonly="readonly"></textarea>
							              </div>
							            </div>
							            
										<div class="mb-2 row">
										   <div class="col-sm-12 text-center" style="text-align: center; margin-top: 30px;">
										      <button class="btn btn-falcon-success btn-sm me-1 mb-2 mb-sm-1" type="submit" id="btnSometerIndividual"><span class="fas fa-check me-1"> </span>Validar</button>
										   </div>
										</div>									
									</form>
		                	</div>
		                	
		                	<div class="card-body bg-light tab-pane" id="infoMultiple" role="tabpanel" aria-labelledby="tabMultiple">
		                	     <form id="frmCargarFacturaMultiple" class="was-validated" novalidate>
		                	         <div id="overSeccionMultiple" class="overlay" style="display: none;text-align: right;">
										<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
									 </div>
									 
									 <div class="mb-2 row" id="valError_Multiple_Cargar">
											<div class="col-sm-12">
												<div class="form-group">
													<div class="alert alert-danger border-2 d-flex align-items-center" role="alert">
													  <div class="bg-danger me-3 icon-item"><span class="fas fa-times-circle text-white fs-3"></span></div>
													  <p class="mb-0 flex-1" id="VISOR_MensajeMultiple_Error">Error en Validación de Factura</p>
													</div>
												</div>
											</div>
										</div>
										
										<div class="mb-2 row" id="valExito_Multiple_Cargar">
											<div class="col-sm-12">
												<div class="form-group">
													<div class="alert alert-success border-2 d-flex align-items-center" role="alert">
													  <div class="bg-success me-3 icon-item"><span class="fas fa-check-circle text-white fs-3"></span></div>
													  <p class="mb-0 flex-1" id="VISOR_MensajeMultiple_Correcto">Validacion de Factura Correcta</p>
													</div>
												</div>
											</div>
										</div>
										
									 
		                	         <div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="fileTXT_Multiple_Cargar" id="VISOR_ETQ59">Archivo TXT </label>
										<div class="col-sm-9">
											<input class="form-control form-control-sm" id="fileTXT_Multiple_Cargar" name="fileTXT" type="file" accept="text/plain" required/>
									 	</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="filePDF_Multiple_Cargar" id="VISOR_Multiple_FilePDF">Archivo PDF</label>
										<div class="col-sm-9">
											<input class="form-control form-control-sm" id="filePDF_Multiple_Cargar" name="filePDF" type="file" accept="application/pdf" required/>
									 	</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-3 col-form-label" for="fileXML_Multiple_Cargar" id="VISOR_Multiple_FileXML">Archivo XML</label>
										<div class="col-sm-9">
											<input class="form-control form-control-sm" id="fileXML_Multiple_Cargar" name="fileXML" type="file" accept="text/xml" required/>
									 	</div>
									</div>
									<div class="mb-2 row">
							              <label class="col-sm-3 col-form-label" for="mensajeError" id="VISOR_MensajeRespuesta_Multiple">Mensaje de Respuesta</label>
							              <div class="col-sm-9">
							                <textarea class="form-control" id="mensajeError_Multiple_Cargar" name="mensajeError" rows="7" readonly="readonly"></textarea>
							              </div>
							            </div>
							            
									<div class="mb-2 row">
										   <div class="col-sm-12 text-center" style="text-align: center; margin-top: 30px;">
										   <!-- 
										      <button type="submit" id="btnSometerMultiple" class="btn btn-primary" style="width: 30%;" >Validar</button>
										    -->   
										      <button class="btn btn-falcon-success btn-sm me-1 mb-2 mb-sm-1" type="submit" id="btnSometerMultiple"><span class="fas fa-check me-1"> </span>Validar</button>
										   </div>
										</div>
		                	     </form>
		                	</div> <!-- infoMultiple -->
		                	
		                </div>
					
					</div>
				
				</div>
			</div>
			<div class="modal-footer">	
				<button class="btn btn-primary" type="button"   id="btnGuardar_CargarFactura" onclick="guardarCargarFactura();" >Guardar</button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="btnCerrar_CargarFactura">Cerrar</button>
			</div>
		</div>
	</div>


 <script type="text/javascript">
 
 
 $(document).ready(function() {
  	  
	   $("#frmCargarFactura").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#frmCargarFactura').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
         errorClass: 'help-block animation-pullUp', errorElement: 'div',
         keyUp: true,
         submitHandler: function(form) {
        	 cargarFactura();
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
	   

	   
	   $("#frmCargarFacturaMultiple").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#frmCargarFacturaMultiple').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
         errorClass: 'help-block animation-pullUp', errorElement: 'div',
         keyUp: true,
         submitHandler: function(form) {
        	 cargarFacturaMultiple();
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
	   
	   calcularEtiquetasVisorCargarFactura();
	  
 });
 

 function calcularEtiquetasVisorCargarFactura(){
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
					
					document.getElementById("VISOR_TITLE5").innerHTML = data.TITLE5;
					document.getElementById("VISOR_ETQ51").innerHTML = data.ETQ51;
					document.getElementById("VISOR_ETQ52").innerHTML = data.ETQ52;
					document.getElementById("VISOR_ETQ53").innerHTML = data.ETQ53;
					document.getElementById("VISOR_ETQ54").innerHTML = data.ETQ54;
					document.getElementById("VISOR_ETQ55").innerHTML = data.ETQ55;
					document.getElementById("VISOR_ETQ56").innerHTML = data.ETQ56;
					document.getElementById("VISOR_ETQ57").innerHTML = data.ETQ57;
					document.getElementById("VISOR_ETQ58").innerHTML = data.ETQ58;
					document.getElementById("VISOR_MensajeMultiple_Error").innerHTML = data.ETQ53;
					document.getElementById("VISOR_MensajeMultiple_Correcto").innerHTML = data.ETQ55;
					document.getElementById("VISOR_ETQ59").innerHTML = data.ETQ59;
					document.getElementById("VISOR_Multiple_FilePDF").innerHTML = data.ETQ56;
					document.getElementById("VISOR_Multiple_FileXML").innerHTML = data.ETQ57;
					document.getElementById("VISOR_MensajeRespuesta_Multiple").innerHTML = data.ETQ58;
					
					document.getElementById("btnGuardar_CargarFactura").innerHTML = BTN_GUARDAR_MENU;
					document.getElementById("btnCerrar_CargarFactura").innerHTML = BTN_CERRAR_MENU;
					document.getElementById("btnSometerIndividual").innerHTML = BTN_VALIDAR;
					document.getElementById("btnSometerMultiple").innerHTML = BTN_VALIDAR;
					
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
 

  
 </script>
</html>