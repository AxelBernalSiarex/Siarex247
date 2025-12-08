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

	<script src="/siarex247/jsp/envioCorreos/bitacora/bitConfidencialidad.js"></script>
</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="BITACORA_TITLE1">Bitácora de Confidencialidad</h5>
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
								<th class="sort pe-1 align-middle white-space-nowrap" id="BITACORA_ETQ1">Titulo del Correo</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BITACORA_ETQ2">Usuario</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BITACORA_ETQ3">Tipo de Tarea</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BITACORA_ETQ4">Fecha de la Tarea</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="BITACORA_ETQ5">Enviar a </th>
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

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

 <script type="text/javascript">

 $(document).ready(function() {
	 
	   calcularEtiquetasBitacoraConf();
 });
 
 

 function calcularEtiquetasBitacoraConf(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'ENVIO_BITACORA'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					document.getElementById("BITACORA_TITLE1").innerHTML = data.TITLE1;
					document.getElementById("BITACORA_ETQ1").innerHTML = data.ETQ1;
					document.getElementById("BITACORA_ETQ2").innerHTML = data.ETQ2;
					document.getElementById("BITACORA_ETQ3").innerHTML = data.ETQ3;
					document.getElementById("BITACORA_ETQ4").innerHTML = data.ETQ4;
					document.getElementById("BITACORA_ETQ5").innerHTML = data.ETQ5;
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
 
</script>



</html>