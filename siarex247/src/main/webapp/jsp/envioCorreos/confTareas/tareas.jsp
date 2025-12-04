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
	<script src="/siarex247/jsp/envioCorreos/confTareas/confTareas.js"></script>
	
	
</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="TAREAS_TITLE1">Configurador de Tareas</h5>
			</div>
		</div>
	</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalle"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ1">Id. Tarea</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ2">Titulo del Correo</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ3">Correo De</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ4">Tipo de Tarea</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ5">Fecha de la Tarea</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ6">Enviar a </th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ7">Estatus</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="TAREAS_ETQ8">Acciones</th>
							</tr>
							<tr class="forFilters">
								<th></th>
								<th></th>
								<th></th>
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
	
</div><!-- card mb-3 -->

<div class="modal fade bd-example-modal-lg" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalPreValidar" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel" tabindex="-1" role="dialog" aria-hidden="true"></div>

<script type="text/javascript">
	$(document).ready(function() {
		  $("#myModalDetalle").load('/siarex247/jsp/envioCorreos/confTareas/modalTareas.jsp');  
		  $("#myModalPreValidar").load('/siarex247/jsp/envioCorreos/confTareas/modalPreValidacion.jsp');
		  
		  calcularEtiquetasTareas();
	});
	
	

	 function calcularEtiquetasTareas(){
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
						
						document.getElementById("TAREAS_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("TAREAS_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("TAREAS_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("TAREAS_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("TAREAS_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("TAREAS_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("TAREAS_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("TAREAS_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("TAREAS_ETQ8").innerHTML = data.ETQ8;
						
						document.getElementById("btnNuevo_Catalogo").innerHTML = '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">'+BTN_NUEVO_MENU+'</span>';
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasTareas()_1_'+thrownError);
				}
			});	
		}
</script>





</html>