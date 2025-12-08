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

	<script src="/siarex247/jsp/visor/complementos/eliminarComplementos.js"></script>

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
	
	<input type="hidden" name="uuid" id="uuidXML_EliminarComplemento" value="ASAS$SDSD">
	<input type="hidden" name="folioEmpresa" id="folioEmpresa_EliminarComplemento" value="ASAS$SDSD">
	
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="VISOR_TITLE19" >SIAREX - Eliminar Complemento</h5>
				</div>
				<div class="p-4 pb-3">
					 <form id="frmEliminarComplemento" name="frmEliminarComplemento" class="was-validated" novalidate>
					     <div id="overSeccion_EliminarComplemento" class="overlay" style="display: none;text-align: right;">
							<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
						 </div>
						 	
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="uuidComplemento_EliminarComplemento" id="VISOR_ETQ97">UUID Complemento</label>
							<div class="col-sm-10">
								<div class="form-group">
									<input id="uuidComplemento_EliminarComplemento" name="razonSocial" class="form-control" type="text" readonly="readonly" required>
								</div>
							</div>
						</div>
						
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="razonSocial_EliminarComplemento" id="VISOR_EliminarComplemento_Etiqueta_RazonSocial">Razón Social</label>
							<div class="col-sm-10">
								<div class="form-group">
									<input id="razonSocial_EliminarComplemento" name="razonSocial" class="form-control" type="text" readonly="readonly" required>
								</div>
							</div>
						</div>
						
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="estatusSAT_EliminarComplemento" id="VISOR_EliminarComplemento_Etiqueta_EstatusSAT">Estatus en SAT</label>
							<div class="col-sm-10">
								<div class="form-group">
									<input id="estatusSAT_EliminarComplemento" name="estatusSAT" class="form-control" type="text" readonly="readonly">
								</div>
							</div>						
							
						</div>
						
						<div class="mb-2 row">
							
							<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
								<div class="tab-content">
											
									<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
										<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
											<table id="tablaDetalleEliminarComplemento"class="table table-sm mb-0 data-table fs--1">
												<thead class="bg-200 text-900">
													<tr>
														<th class="no-sort pe-1 align-middle data-table-row-action" id="VISOR_EliminarComplemento_Etiqueta_OrdenCompra">Orden de Compra</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_EliminarComplemento_Etiqueta_Monto">Monto</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_EliminarComplemento_Etiqueta_FechaPago">Fecha de Pago</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_EliminarComplemento_Etiqueta_UUID">UUID</th>
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
				<button type="button" id="btnEliminar_CargarComplemento" class="btn btn-primary" onclick="ejecutarEliminarComplementoOrdenes();">Eliminar</button>	
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="VISOR_EliminarComplemento_Boton_Cerrar">Cerrar</button>
			</div>
		</div>
	</div>


 <script type="text/javascript">
 
 
 $(document).ready(function() {
	 
	 $("#frmEliminarComplemento").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#frmEliminarComplemento').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
       errorClass: 'help-block animation-pullUp', errorElement: 'div',
       keyUp: true,
       submitHandler: function(form) {
    	   // ejecutarEliminarComplementoOrdenes();
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
	   
	   calcularEtiquetasVisorEliminarComplemento();
 });
 
 

 function calcularEtiquetasVisorEliminarComplemento(){
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
					
					document.getElementById("VISOR_TITLE19").innerHTML = data.TITLE19;
					document.getElementById("VISOR_ETQ97").innerHTML = data.ETQ97;
					document.getElementById("VISOR_EliminarComplemento_Etiqueta_RazonSocial").innerHTML = data.ETQ17;
					document.getElementById("VISOR_EliminarComplemento_Etiqueta_EstatusSAT").innerHTML = data.COL31;
					document.getElementById("VISOR_EliminarComplemento_Etiqueta_OrdenCompra").innerHTML = data.ETQ29;
					document.getElementById("VISOR_EliminarComplemento_Etiqueta_Monto").innerHTML = data.ETQ31;
					document.getElementById("VISOR_EliminarComplemento_Etiqueta_FechaPago").innerHTML = data.COL27;
					document.getElementById("VISOR_EliminarComplemento_Etiqueta_UUID").innerHTML = data.ETQ67;
					
					
					document.getElementById("btnEliminar_CargarComplemento").innerHTML = BTN_ELIMINAR_MENU;
					document.getElementById("VISOR_EliminarComplemento_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
 
  
 </script>
</html>