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
    
    <script src="/siarex247/jsp/envioCorreos/confTareas/preValidacionOK.js"></script>
	<script src="/siarex247/jsp/envioCorreos/confTareas/preValidacionNG.js"></script>
	
	
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
	
	
 <style type="text/css">
   
   .text-red {
     color: red;
   }
   
 </style>
 
 
</head>


	<input type="hidden" name="claveTarea_PreValida" id="claveTarea_PreValida" value="0">

	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="TAREAS_ETQ57">Prevalidación de Ordenes de Compra</h5>
				</div>
				<div class="p-4 pb-0">
	
					
					<div class="card-header">
		              <div class="row flex-between-end">
		                <div class="col-auto align-self-center">
		                  <h6 class="mb-0" data-anchor="data-anchor" id="TAREAS_ETQ58">Ordenes de Compra con Vendor Asignado</h6>
		                </div>
		              </div>
		            </div>
					
					<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
					       <div class="tab-content">
								<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
										
									<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
										<table id="tablaSatisfactorias"class="table mb-0 data-table fs--1">
											<thead class="bg-200 text-900">
												<tr>
													<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ59">Vendor ID</th>
													<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ60">Número de Orden</th>
													<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ61">Razón Social</th>
												</tr>
												<tr class="forFilters">
													<th></th>
													<th></th>
													<th></th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
					
							</div> 
											
						</div>


					<div class="card-header">
		              <div class="row flex-between-end">
		                <div class="col-auto align-self-center">
		                  <h6 class="mb-0" data-anchor="data-anchor" id="TAREAS_ETQ62">Ordenes de Compra sin Vendor Asignado y/o sin Empleados Registrados en Siarex</h6>
		                </div>
		              </div>
		            </div>
		            
		            
					<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
							<div class="tab-content">
								<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
										
									<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
										<table id="tablaSinVendor"class="table mb-0 data-table fs--1">
											<thead class="bg-200 text-900">
												<tr>
													<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ63">Vendor ID</th>
													<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ64">Número de Orden</th>
													<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ65">Razón Social</th>
													<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ66">Empleado</th>
													<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ67">Estatus en Lista Negra</th>
												</tr>
												<tr class="forFilters">
													<th></th>
													<th></th>
													<th></th>
													<th></th>
													<th></th>
												</tr>
											</thead>
										</table>
									</div>
								</div>
					
							</div> 
											
						</div>
						

				</div>
			</div>
			<div class="modal-footer">	
			<!-- 
				<button type="submit" id="submit" class="btn btn-primary">Guardar</button>
			 -->	
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="TAREAS_Boton_CerrarPreValidacion">Cerrar</button>
			</div>
		</div>
	</div>

 <script type="text/javascript">
 
 	$(document).ready(function() {
				
 		calcularEtiquetasTareasPredefinidasModal();
	});
 	
 	

 	 function calcularEtiquetasTareasPredefinidasModal(){
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
 						
 						document.getElementById("TAREAS_ETQ57").innerHTML = data.ETQ57;
 						document.getElementById("TAREAS_ETQ58").innerHTML = data.ETQ58;
 						document.getElementById("TAREAS_ETQ59").innerHTML = data.ETQ59;
 						document.getElementById("TAREAS_ETQ60").innerHTML = data.ETQ60;
 						document.getElementById("TAREAS_ETQ61").innerHTML = data.ETQ61;
 						document.getElementById("TAREAS_ETQ62").innerHTML = data.ETQ62;
 						document.getElementById("TAREAS_ETQ63").innerHTML = data.ETQ63;
 						document.getElementById("TAREAS_ETQ64").innerHTML = data.ETQ64;
 						document.getElementById("TAREAS_ETQ65").innerHTML = data.ETQ65;
 						document.getElementById("TAREAS_ETQ66").innerHTML = data.ETQ66;
 						document.getElementById("TAREAS_ETQ67").innerHTML = data.ETQ67;
 						
 						document.getElementById("TAREAS_Boton_CerrarPreValidacion").innerHTML = BTN_CERRAR_MENU;
 						
 					}
 					
 				},
 				error : function(xhr, ajaxOptions, thrownError) {
 					alert('calcularEtiquetasTareasPredefinidasModal()_1_'+thrownError);
 				}
 			});	
 		}
 	 
	
 </script>
</html>

	
