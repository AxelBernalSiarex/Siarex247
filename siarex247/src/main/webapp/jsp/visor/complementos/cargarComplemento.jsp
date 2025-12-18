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
	<script src="/siarex247/jsp/visor/complementos/validacionesComplemento.js"></script>



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
	
	<input type="hidden" name="uuidXML_Complemento" id="uuidXML_Complemento" value="XXXXXX1234aa5">
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="VISOR_TITLE8">SIAREX - Cargar Complementos de Factura</h5>
				</div>
				<div class="p-4 pb-3">
					 <form id="frmCargarComplemento" name="frmCargarComplemento" class="was-validated" novalidate>
					     <div id="overSeccion_Complemento" class="overlay" style="display: none;text-align: right;">
							<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
						 </div>
						 	
						<div class="mb-2 row" id="error_Complemento">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-danger border-2 d-flex align-items-center" role="alert">
									  <div class="bg-danger me-3 icon-item"><span class="fas fa-times-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ70">Error al validar complemento de pago </p>
									</div>
								</div>
							</div>
						</div>
						
						<div class="mb-2 row" id="exito_Complemento">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-success border-2 d-flex align-items-center" role="alert">
									  <div class="bg-success me-3 icon-item"><span class="fas fa-check-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ66">La validación del complemento de pago fue exitosa</p>
									</div>
								</div>
							</div>
						</div>
						
						
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="fileXML_Cargar_Complemento" id="VISOR_CargarComplemento_Etiqueta_ArchivoXML">Archivo XML</label>
							<div class="col-sm-10">
								<input class="form-control form-control-sm" id="fileXML_Cargar_Complemento" name="fileXML" type="file" accept="text/xml" required/>
						 	</div>
						</div>
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="filePDF_Cargar_Complemento" id="VISOR_CargarComplemento_Etiqueta_ArchivoPDF">Archivo PDF</label>
							<div class="col-sm-8">
								<input class="form-control form-control-sm" id="filePDF_Cargar_Complemento" name="filePDF" type="file" accept="application/pdf" required />
						 	</div>
						 	
						 	<div class="col-sm-2">
						 	<!-- 
								 <div class="col-sm-2">
									<div class="position-relative" id="emoji-button" onclick="someterFormulario();">
										<div class="btn btn-info" data-picmo='{"position":"bottom-start"}'><span class="fas fa-search"></span></div>
							  		</div>
								</div>
							-->
									<button class="btn btn-falcon-success btn-sm me-1 mb-2 mb-sm-1" type="submit" id="btnSometerComplemento"><span class="fas fa-check me-1"> </span>Validar</button>	
				              </div>
				              
				              
						</div>	
						
						<div class="mb-2 row">
			              <label class="col-sm-2 col-form-label" for="mensajeRespuesta_Cargar_Complemento" id="VISOR_CargarComplemento_Etiqueta_Respuesta">Respuesta</label>
			              <div class="col-sm-10">
			                <textarea class="form-control" id="mensajeRespuesta_Cargar_Complemento" name="mensajeRespuesta" rows="5" ></textarea>
			              </div>
			            </div>
			            
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="razonSocial_Cargar_Complemento" id="VISOR_CargarComplemento_Etiqueta_RazonSocial">Razón Social</label>
							<div class="col-sm-10">
								<div class="form-group">
									<input id="razonSocial_Cargar_Complemento" name="razonSocial" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
						</div>
						
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="estadoCFDI_Cargar_Complemento" id="VISOR_CargarComplemento_Etiqueta_EstadoCFDI">Estado CFDI</label>
							<div class="col-sm-3">
								<div class="form-group">
									<input id="estadoCFDI_Cargar_Complemento" name="estadoCFDI" class="form-control" type="text" readonly="readonly">
								</div>
							</div>						
							<label class="col-sm-2 col-form-label" for="estatusCFDI_Cargar_Complemento" id="VISOR_CargarComplemento_Etiqueta_EstatusSAT">Estatus en SAT </label>
							<div class="col-sm-5">
								<div class="form-group">
									<input id="estatusCFDI_Cargar_Complemento" name="estatus" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
						</div>
						
						<div class="mb-2 row">
							
							<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
								<div class="tab-content">
											
									<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
										<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
											<table id="tablaDetalleCargarComplemento"class="table table-sm mb-0 data-table fs--1">
												<thead class="bg-200 text-900">
													<tr>
														<th class="no-sort pe-1 align-middle data-table-row-action" id="VISOR_CargarComplemento_Etiqueta_OrdenCompra">Orden de Compra</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_ETQ67">UUID</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_ETQ68">Total Orden de Compra</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_ETQ69">Total Complemento de Pago</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_CargarComplemento_Etiqueta_FechaPago">Fecha de Pago</th>
													</tr>
												</thead>
											</table>
										</div>
									</div>
						
								</div> <!-- tab-content -->
						
							</div> <!-- card-body -->
						</div>
					</form>
				
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary" type="button"   id="btnGuardar_CargarComplemento" onclick="guardarAsignarComplemento();" >Asignar</button>	
				<button class="btn btn-secondary" type="button" id="btnCerrar_CargarFactura" onclick="eliminarComplemento();">Cerrar</button>
			</div>
		</div>
	</div>


 <script type="text/javascript">
 
 
 $(document).ready(function() {
	 
	 $("#frmCargarComplemento").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#frmCargarComplemento').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
       errorClass: 'help-block animation-pullUp', errorElement: 'div',
       keyUp: true,
       submitHandler: function(form) {
      	   validarComplemento();
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
	   
	   calcularEtiquetasVisorCargarComplemento();
 });
 
 

 function calcularEtiquetasVisorCargarComplemento(){
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
					
					document.getElementById("VISOR_TITLE8").innerHTML = data.TITLE8;
					document.getElementById("VISOR_ETQ66").innerHTML = data.ETQ66;
					document.getElementById("VISOR_ETQ67").innerHTML = data.ETQ67;
					document.getElementById("VISOR_ETQ68").innerHTML = data.ETQ68;
					document.getElementById("VISOR_ETQ69").innerHTML = data.ETQ69;
					document.getElementById("VISOR_ETQ70").innerHTML = data.ETQ70;
					document.getElementById("VISOR_CargarComplemento_Etiqueta_ArchivoXML").innerHTML = data.ETQ57;
					document.getElementById("VISOR_CargarComplemento_Etiqueta_ArchivoPDF").innerHTML = data.ETQ56;
					document.getElementById("VISOR_CargarComplemento_Etiqueta_Respuesta").innerHTML = data.ETQ61;
					document.getElementById("VISOR_CargarComplemento_Etiqueta_RazonSocial").innerHTML = data.COL2;
					document.getElementById("VISOR_CargarComplemento_Etiqueta_EstadoCFDI").innerHTML = data.COL30;
					document.getElementById("VISOR_CargarComplemento_Etiqueta_EstatusSAT").innerHTML = data.COL31;
					document.getElementById("VISOR_CargarComplemento_Etiqueta_OrdenCompra").innerHTML = data.ETQ29;
					document.getElementById("VISOR_CargarComplemento_Etiqueta_FechaPago").innerHTML = data.COL27;
					
					
					document.getElementById("btnGuardar_CargarComplemento").innerHTML = BTN_ASIGNAR;
					document.getElementById("btnCerrar_CargarFactura").innerHTML = BTN_CERRAR_MENU;
					document.getElementById("btnSometerComplemento").innerHTML = BTN_VALIDAR;
					
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
 
  
 </script>
</html>