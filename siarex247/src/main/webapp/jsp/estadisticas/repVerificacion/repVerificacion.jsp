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

  <script src="/siarex247/jsp/estadisticas/repVerificacion/repVerificacion.js"></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="REPORTE_SAT_TITLE1">Reporte de Verificación</h5>
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
								<th class="no-sort pe-1 align-middle data-table-row-action" id="REPORTE_SAT_ETQ11">Acciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ1">Id. Registro</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ2">Descripción</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ3">Validar Factura</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ4">Validar Complemento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ5">Validar Nota Credito</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ6">Fecha Inicial Proceso</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ7">Fecha Final Proceso</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ8">Estatus</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ9">Total Procesar</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="REPORTE_SAT_ETQ10">Total Procesados</th>
								 
								
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

<div class="modal fade bd-example-modal-lg" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<script type="text/javascript">


var TITLE_DELETE_CATALOGO = null;

var LABEL_REPORTE_VERIFICACION = null;
var LABEL_REPORTE_VERIFICACION_EXCEL = null;

	$(document).ready(function() {
		$("#myModalDetalle").load('/siarex247/jsp/estadisticas/repVerificacion/modalVerificacion.jsp');  
		calcularEtiquetasReporteVerificacion();
	});
	
	

	 function calcularEtiquetasReporteVerificacion(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'REPORTE_SAT'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("REPORTE_SAT_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("REPORTE_SAT_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("REPORTE_SAT_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("REPORTE_SAT_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("REPORTE_SAT_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("REPORTE_SAT_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("REPORTE_SAT_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("REPORTE_SAT_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("REPORTE_SAT_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("REPORTE_SAT_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("REPORTE_SAT_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("REPORTE_SAT_ETQ11").innerHTML = data.ETQ11;
						
						
						LABEL_REPORTE_VERIFICACION = data.ETQ14;
						TITLE_DELETE_CATALOGO = data.ETQ12;
						LABEL_REPORTE_VERIFICACION_EXCEL = data.ETQ14;
						
						document.getElementById("btnNuevo_Catalogo").innerHTML = '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">'+BTN_NUEVO_MENU+'</span>';
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
</script> 




</html>