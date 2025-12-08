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

  <script src='/siarex247/jsp/configuraciones/cfdi/listaNegra/listaNegra.js'></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONF_LISTA_NEGRA_TITLE1">Configurar Lista Negra del SAT</h5>
			</div>
			<div class="col-auto d-flex"></div>
		</div>
	</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalle"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
							<!-- 
								<th class="no-sort pe-1 align-middle data-table-row-action" id="CONF_LISTA_NEGRA_ETQ4">Acciones</th>
							 -->	
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_LISTA_NEGRA_ETQ1">Id. Registro</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_LISTA_NEGRA_ETQ2">Tipo de Registro</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_LISTA_NEGRA_ETQ3">Día de Configuración</th>
								
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

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->


<div class="modal fade bd-example-modal-lg" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>



<script type="text/javascript">

var TITLE_NEW_CATALOGO = null;
var TITLE_EDIT_CATALOGO = null;
var TITLE_VIEW_CATALOGO = null;
var TITLE_DELETE_CATALOGO = null;

var LABEL_CONF_LISTA_NEGRA = null;

	$(document).ready(function() {
		
		$("#myModalDetalle").load('/siarex247/jsp/configuraciones/cfdi/listaNegra/modalListaNegra.jsp');
		
		calcularEtiquetasConfListaNegra();
	});
	

	 function calcularEtiquetasConfListaNegra(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONF_LISTA_NEGRA'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONF_LISTA_NEGRA_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("CONF_LISTA_NEGRA_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("CONF_LISTA_NEGRA_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CONF_LISTA_NEGRA_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CONF_LISTA_NEGRA_ETQ4").innerHTML = data.ETQ4;
						
						TITLE_NEW_CATALOGO = data.ETQ5;
						TITLE_EDIT_CATALOGO = data.ETQ6;
						TITLE_VIEW_CATALOGO = data.ETQ7;
						TITLE_DELETE_CATALOGO = data.ETQ9;
						LABEL_CONF_LISTA_NEGRA = data.ETQ10;
						
						// document.getElementById("btnNuevo_Catalogo").innerHTML = '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">'+BTN_NUEVO_MENU+'</span>';
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasConfListaNegra()_1_'+thrownError);
				}
			});	
		}
	 
</script>




</html>