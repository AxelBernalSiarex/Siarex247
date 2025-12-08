
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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


</head>


<form id="form-Catalogo-Detalle" class="was-validated" novalidate>
	
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="modal-title-bitacora"> Detalle de Errores Historico de Pagos </h5>
				</div>
				<div class="p-4 pb-0">

					<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
						<div class="tab-content">
							<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
								<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
									<table id="tablaDetalleBitacora"class="table mb-0 data-table fs--1">
										<thead class="bg-200 text-900">
											<tr>
												<th class="sort pe-1 align-middle white-space-nowrap">Registro</th>
												<th class="sort pe-1 align-middle white-space-nowrap">Descripción</th>
											</tr>
											<tr class="forFilters">
												<th></th>
												<th></th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
						</div> <!-- tab-content -->
					</div> <!-- card-body -->
				</div>
			</div>
			<div class="modal-footer">	
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 var tablaDetalleBitacora = null;

	$(document).ready(function() {
		try {
			tablaDetalleBitacora = $('#tablaDetalleBitacora').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 15,
				lengthChange: false,
				dom: "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				ordering    : true,
				serverSide	: false,
				fixedHeader : true,
				orderCellsTop: true,
				info		: true,
				select      : false,
				stateSave	: false, 
				order       : [ [ 0, 'asc' ] ],	
				buttons: [
					{ 	extend: "collection",
				    	autoClose : true,  
				    	text:'<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
				    	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    	buttons: [
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Detalle Bitacora de Errores', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'BitacoraCargas'},
				        ], 
			      	}
				],
				language : {
					processing:     "Procesando...",
					zeroRecords:    "No se encontraron resultados",
					emptyTable:     "Ningún dato disponible en esta tabla",
					info:           "Mostrando _START_ al _END_ de _TOTAL_ registros",
					infoEmpty:      "No hay registros disponibles",
					infoFiltered:   "(filtrado de un total de _MAX_ registros)",
					infoPostFix:    "",
					search:         "Buscar:",
					url:            "",
					infoThousands:  ",",
					loadingRecords: "Cargando...",
					oPaginate: {
				           sFirst : "Primero",
				           sLast  : "Último",
				           sNext  : "<span class='fa fa-chevron-right fa-w-10'></span>",
				           sPrevious : "<span class='fa fa-chevron-left fa-w-10'></span>"
				   	}
				},
				ajax : {
					url : '/siarex247/cumplimientoFiscal/historicoPagos/detalleErrores.action',
					type: 'POST'
				},
				aoColumns : [
					{ mData: "idLLave",  "sClass": "alinearCentro"},
					{ mData: "desError"}
				],
				 initComplete: function () {
		        	  var btns = $('.dt-button');
		              btns.removeClass('dt-button');
		              
		              var btnsSubMenu = $('.dtb-b2');
		              btnsSubMenu.addClass('dropdown-menu dropdown-menu-end py-0 show');
		              
		           },
				  drawCallback: function () {
					  
				  }
			});
			
		} catch(e) {
			alert('usuarios()_'+e);
		};
		
		tablaDetalleBitacora.on( 'draw', function () {
			 $('[data-bs-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	// Aqui se agrega los filtros del encabezado
	$('#tablaDetalleBitacora thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if (i == 10){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#tablaDetalleBitacora thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatosErrores($(this).parent().index(), this.value);
	});
		
	function filtraDatosErrores(columna, texto) {
		tablaDetalleBitacora
			.column(columna)
	        .search(texto)
	        .draw();
	}
	
	
	function iniciaFormImportarDetalle(){
		/* Reset al Formulario */ 
		$("#form-Catalogo-Detalle").find('.has-success').removeClass("has-success");
	    $("#form-Catalogo-Detalle").find('.has-error').removeClass("has-error");
		$('#form-Catalogo-Detalle')[0].reset(); 
		$('#form-Catalogo-Detalle').removeClass("was-validated"); 
		   
	}
 </script>
</html>