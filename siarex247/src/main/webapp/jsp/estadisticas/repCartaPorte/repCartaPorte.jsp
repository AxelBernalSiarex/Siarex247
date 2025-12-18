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

  <script src="/siarex247/jsp/estadisticas/repCartaPorte/repCartaPorte.js"></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="REPORTE_CARTA_PORTE_TITLE1">Reporte de Validación Carta Porte</h5>
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
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_CARTA_PORTE_ETQ1">>Id. Registro</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_CARTA_PORTE_ETQ2">Orden de Compra</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_CARTA_PORTE_ETQ3">Razón Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_CARTA_PORTE_ETQ4">Etiqueta</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_CARTA_PORTE_ETQ5">Valor XML</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_CARTA_PORTE_ETQ6">Estatus</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_CARTA_PORTE_ETQ7">Resultado</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_CARTA_PORTE_ETQ8">Fecha de Transaccion</th>
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

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->


<script type="text/javascript">
	$(document).ready(function() {
		  
		calcularEtiquetasReporteCartaPorte();
	});
	
	

	 function calcularEtiquetasReporteCartaPorte(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'REPORTE_CARTA_PORTE'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("REPORTE_CARTA_PORTE_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("REPORTE_CARTA_PORTE_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("REPORTE_CARTA_PORTE_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("REPORTE_CARTA_PORTE_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("REPORTE_CARTA_PORTE_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("REPORTE_CARTA_PORTE_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("REPORTE_CARTA_PORTE_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("REPORTE_CARTA_PORTE_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("REPORTE_CARTA_PORTE_ETQ8").innerHTML = data.ETQ8;
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasReporteCartaPorte()_1_'+thrownError);
				}
			});	
		}
	 
</script> 




</html>