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

  <script src="/siarex247/jsp/cargas/ordenes/ordenes.js"></script>
  <script src="/siarex247/jsp/cargas/ordenes/accesosPermitidos.js"></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CARGA_ORDENES_TITLE1">Carga de Ordenes</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
				<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
					<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="CARGA_ORDENES_ETQ1"> Opciones </span>
				</button>
				<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
					<a id="menu_OrdenesCompra" class="dropdown-item" href="javascript:abreModal('ordenes', 0, '');">Ordenes de Compra</a>
					<a id="menu_ContraRecibos" class="dropdown-item" href="javascript:abreModal('recibos', 0, '');">Contra Recibos</a>
					<a id="menu_OrdenesPago" class="dropdown-item" href="javascript:abreModal('pagos', 0, '');">Ordenes de Pago</a>
					<a id="menu_EliminarOrdenes" class="dropdown-item" href="javascript:abreModal('eliminar', 0, '');" >Eliminar Ordenes</a>
					<a id="menu_ImportarProv" class="dropdown-item" href="javascript:abreModal('importar', 0, '');">Importar Proveedores</a>
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
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="id" id="CARGA_ORDENES_ETQ2">Fecha de Carga</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="depto" id="CARGA_ORDENES_ETQ3">Nombre del Archivo</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="correo" id="CARGA_ORDENES_ETQ4">Tipo de Carga</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="fecha" id="CARGA_ORDENES_ETQ5">Estatus</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="fecha" id="CARGA_ORDENES_ETQ6">Usuarios</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="fecha" id="CARGA_ORDENES_ETQ7">Registros Exitosos</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="fecha" id="CARGA_ORDENES_ETQ8">Registros No Exitosos</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="fecha" id="CARGA_ORDENES_ETQ9">Total de Registros</th>
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

<div class="modal fade bd-example-modal-lg" id="myModalOrdenes" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalRecibos" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalEliminar" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalImportar" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalPagos" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>

<script type="text/javascript">
	$(document).ready(function() {
		   $("#myModalOrdenes").load('/siarex247/jsp/cargas/ordenes/modalOrdenes.jsp');
		   $("#myModalRecibos").load('/siarex247/jsp/cargas/ordenes/modalContraRecibos.jsp');
		   $("#myModalEliminar").load('/siarex247/jsp/cargas/ordenes/modalEliminar.jsp');
		   $("#myModalImportar").load('/siarex247/jsp/cargas/ordenes/modalImportar.jsp');
		   $("#myModalPagos").load('/siarex247/jsp/cargas/ordenes/modalPagos.jsp');
		   $("#myModalDetalle").load('/siarex247/jsp/cargas/ordenes/modalDetalle.jsp');
		   calcularEtiquetasCargasOrdenes();
	});
	
	

	 function calcularEtiquetasCargasOrdenes(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CARGA_ORDENES'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CARGA_ORDENES_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("CARGA_ORDENES_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("CARGA_ORDENES_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CARGA_ORDENES_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CARGA_ORDENES_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CARGA_ORDENES_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CARGA_ORDENES_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("CARGA_ORDENES_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("CARGA_ORDENES_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("CARGA_ORDENES_ETQ9").innerHTML = data.ETQ9;
						
						document.getElementById("menu_OrdenesCompra").innerHTML = data.MENU1;
						document.getElementById("menu_ContraRecibos").innerHTML = data.MENU2;
						document.getElementById("menu_OrdenesPago").innerHTML = data.MENU3;
						document.getElementById("menu_EliminarOrdenes").innerHTML = data.MENU4;
						document.getElementById("menu_ImportarProv").innerHTML = data.MENU5;
						
						document.getElementById("btnRefresh_Catalogo").innerHTML = '<span class="fab fa-firefox-browser me-1" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">'+BTN_REFRESCAR+'</span>';
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasCargasOrdenes()_1_'+thrownError);
				}
			});	
		}
	 
</script>





</html>