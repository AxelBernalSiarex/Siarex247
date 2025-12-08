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

  <script src="/siarex247/jsp/estatusXML/descartar/descartar.js"></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="DESCARTAR_TITLE1">Lista de Ordenes de Compra para Descartar en LayOut</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
				<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
					<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="DESCARTAR_ETQ1">Opciones</span>
				</button>
				<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
					<a class="dropdown-item" href="javascript:abreModal('carga');" id="DESCARTAR_ETQ2">Cargar Archivo</a>
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
								<th class="sort pe-1 align-middle white-space-nowrap" id="DESCARTAR_ETQ3">Razón Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="DESCARTAR_ETQ4">Orden de Compra</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="DESCARTAR_ETQ5">Serie/Folio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="DESCARTAR_ETQ6">Estatus de Pago</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="DESCARTAR_ETQ7">Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="DESCARTAR_ETQ8">Sub-Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="DESCARTAR_ETQ9">Existe en Siarex ?</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="DESCARTAR_ETQ10">Acciones</th>
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

<div class="modal fade bd-example-modal-lg" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>

<script type="text/javascript">

var TITLE_DELETE_CATALOGO = null;
var LABEL_DESCARTAR = null;
	$(document).ready(function() {
		$("#myModalDetalle").load('/siarex247/jsp/estatusXML/descartar/modalDescartar.jsp');
		calcularEtiquetasDescartar();
	});
	
	

	 function calcularEtiquetasDescartar(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'DESCARTAR'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("DESCARTAR_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("DESCARTAR_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("DESCARTAR_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("DESCARTAR_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("DESCARTAR_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("DESCARTAR_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("DESCARTAR_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("DESCARTAR_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("DESCARTAR_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("DESCARTAR_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("DESCARTAR_ETQ10").innerHTML = data.ETQ10;
						
						TITLE_DELETE_CATALOGO = data.ETQ14;
						LABEL_DESCARTAR = data.ETQ4;
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasDescartar()_1_'+thrownError);
				}
			});	
		}
	 
</script>





</html>