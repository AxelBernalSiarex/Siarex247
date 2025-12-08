<%@page import="com.siarex247.utils.Utils"%>
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
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">

   <script src="/siarex247/jsp/estatusXML/pedimentos/pedimentos.js?v=<%=Utils.VERSION %>"></script>
   
</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="PEDIMENTOS_TITLE1">Pedimentos</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
				<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
					<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="PEDIMENTOS_ETQ1">Opciones</span>
				</button>
				<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
					<a class="dropdown-item" href="javascript:descargaPedimentos();" id="PEDIMENTOS_ETQ2">Descargar Pedimientos</a>
				</div>
			  </div>
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
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ3">Número de Pedimento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ4">Clave de Pedimento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ5">Regimen</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ6">DTA</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ7">IVA</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ8">IGI</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ9">PRV</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ10">IVAPRV</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ11">Efectivo</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ12">Otros</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ13">Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ14">Banco</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ15">Línea de Captura</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ16">Importe de Pago</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ17">Fecha de Pago</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ18">Número de Operación</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ19">Número SAT</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ20">Medio de Presentación</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ21">Medio de Recepción</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="PEDIMENTOS_ETQ22">Visualizar PDF</th>
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


<form action="/siarex247/jsp/estatusXML/pedimentos/mostrarPedimento.jsp" name="frmPedimentos" id="frmPedimentos" target="_blank" method="post">
   <input type="hidden" name="f" value="" id="idRegistroP">
   <input type="hidden" name="t" value="" id="tipoArchivoP">
</form>


<form action="/siarex247/excel/descargarPedimento.action" name="frmDescargaPedimentos" id="frmDescargaPedimentos" target="excel" method="post">
   <input type="hidden" name="bandSelecciono" value="" id="bandSelecciono">
   <input type="hidden" name="idRegistro" value="" id="idRegistro">
</form>


<div class="modal fade bd-example-modal-lg" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>


<script type="text/javascript">
	$(document).ready(function() {
		$("#myModalDetalle").load('/siarex247/jsp/estatusXML/pedimentos/modalPedimentos.jsp');
		calcularEtiquetasPedimentos();
	});
	
	

	 function calcularEtiquetasPedimentos(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'PEDIMENTOS'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("PEDIMENTOS_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("PEDIMENTOS_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("PEDIMENTOS_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("PEDIMENTOS_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("PEDIMENTOS_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("PEDIMENTOS_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("PEDIMENTOS_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("PEDIMENTOS_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("PEDIMENTOS_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("PEDIMENTOS_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("PEDIMENTOS_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("PEDIMENTOS_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("PEDIMENTOS_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("PEDIMENTOS_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("PEDIMENTOS_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("PEDIMENTOS_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("PEDIMENTOS_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("PEDIMENTOS_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("PEDIMENTOS_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("PEDIMENTOS_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("PEDIMENTOS_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("PEDIMENTOS_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("PEDIMENTOS_ETQ22").innerHTML = data.ETQ22;
						
						document.getElementById("btnNuevo_Catalogo").innerHTML = '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">'+BOTON_CARGAR_PEDIMENTOS+'</span>';
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 
	 
</script>





</html>