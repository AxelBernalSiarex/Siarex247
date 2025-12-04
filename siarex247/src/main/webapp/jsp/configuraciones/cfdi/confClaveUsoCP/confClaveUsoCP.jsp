<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">
	
	<script src="/siarex247/jsp/configuraciones/cfdi/confClaveUsoCP/confClaveUsoCP.js"></script>
</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CAT_CLAVE_USO_CP_TILE1">Configuración Clave y Uso Carta Porte</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
				<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
					<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="CAT_CLAVE_USO_CP_ETQ1">Opciones</span>
				</button>
				<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
					<a class="dropdown-item" href="javascript:abreModal('importar');" id="CAT_CLAVE_USO_CP_ETQ3">Importar Proveedores</a>
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
								<th class="no-sort pe-1 align-middle data-table-row-action" id="CAT_CLAVE_USO_CP_ETQ14">Acciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ4">Id. de Etiqueta</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ5">RFC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ6">Razón Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ7">Uso CDFI</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ8">Descripción Uso CDFI</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ9">Clave Producto</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ10">Descripción Clave Producto</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ11">División</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ12">Grupo</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CAT_CLAVE_USO_CP_ETQ13">Clase</th>
								
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
<div class="modal fade bd-example-modal-lg" id="myModalImportar" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<div class="modal fade bd-example-modal-lg" id="myModalvalidar" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<script type="text/javascript">


var TITLE_NEW_CATALOGO = null;
var TITLE_EDIT_CATALOGO = null;
var TITLE_VIEW_CATALOGO = null;
var TITLE_DELETE_CATALOGO = null;

var MSG_CFDI_NO_EXISTE = null;
var MSG_CLAVE_NO_EXISTE = null;
var MSG_IMPORTAR_CORRECTO = null;


	$(document).ready(function() {
		$("#myModalDetalle").load('/siarex247/jsp/configuraciones/cfdi/confClaveUsoCP/modalConfUsoCP.jsp');
		$("#myModalImportar").load('/siarex247/jsp/configuraciones/cfdi/confClaveUsoCP/modalImportarCP.jsp');
		
		calcularEtiquetasCatalogoClaveUsoCP();
	});
	
	

	 function calcularEtiquetasCatalogoClaveUsoCP(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CAT_CLAVE_USO_CP'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CAT_CLAVE_USO_CP_TILE1").innerHTML = data.TITLE1;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ8").innerHTML = data.ETQ9;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ9").innerHTML = data.ETQ8;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("CAT_CLAVE_USO_CP_ETQ14").innerHTML = data.ETQ14;
						
						TITLE_NEW_CATALOGO = data.ETQ15;
						TITLE_EDIT_CATALOGO = data.ETQ16;
						TITLE_VIEW_CATALOGO = data.ETQ17;
						TITLE_DELETE_CATALOGO = data.ETQ18;
						MSG_CFDI_NO_EXISTE = data.MSG1;
						MSG_CLAVE_NO_EXISTE = data.MSG2;
						MSG_IMPORTAR_CORRECTO = data.MSG3;
						
						document.getElementById("btnNuevo_Catalogo").innerHTML = '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">'+BTN_NUEVO_MENU+'</span>';
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasCatalogoClaveUsoCP()_1_'+thrownError);
				}
			});	
		}
	 
</script>




</html>