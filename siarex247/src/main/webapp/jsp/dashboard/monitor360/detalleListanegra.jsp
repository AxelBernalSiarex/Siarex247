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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<%

String tipoConsulta = Utils.noNulo(request.getParameter("tipoConsulta"));
String annio = Utils.noNulo(request.getParameter("annio"));
String mes = Utils.noNulo(request.getParameter("mes"));
String tipo = Utils.noNulo(request.getParameter("tipo"));
String contribuyente = Utils.noNulo(request.getParameter("contribuyente"));
String tipoMoneda = Utils.noNulo(request.getParameter("tipoMoneda"));

%>

</head>

<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">
	<input type="hidden" name="TIPOCONSULTA" id="TIPOCONSULTA_LISTA" value="<%=tipoConsulta %>">
	<input type="hidden" name="ANNIO" id="ANNIO_LISTA" value="<%=annio %>">
	<input type="hidden" name="MES" id="MES_LISTA" value="<%=mes %>">
	<input type="hidden" name="TIPO" id="TIPO_LISTA" value="<%=tipo %>">
	<input type="hidden" name="CONTRIBUYENTE" id="CONTRIBUYENTE_LISTA" value="<%=contribuyente %>">
	<input type="hidden" name="TIPOMONEDA" id="TIPOMONEDA_LISTA" value="<%=tipoMoneda %>">
	
	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONF_DESCARGA_SAT_TITLE1">Resumen Lista Negra</h5>
			</div>
			<div class="col-auto d-flex"></div>
		</div>
	</div>
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalleLista"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<!-- <th class="no-sort pe-1 align-middle data-table-row-action">Sel</th> -->
								<th class="sort pe-1 align-middle white-space-nowrap" id="">RFC </th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="">Raz&oacute;n Social</th>
							</tr>
							<tr class="forFilters">
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
								<th class="sort pe-1 align-middle white-space-nowrap" ></th>
							</tr>
							
						</thead>
					</table>
				</div>
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

<script type="text/javascript">


var tablaDetalleLista = null;

	$(document).ready(function() {
		try {
			tablaDetalleLista = $('#tablaDetalleLista').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 15,
				lengthChange: false,
//				dom: 'Blfrtip',
//				dom: "<'row mx-0'<'col-md-12'QB><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Resumen Detalle Monitor 360', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'ResumenMonitor360'},
				    		 
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
					url: '/siarex247/visor/monitor360/detalleListaNegra.action',
					 
					data : {
						tipoConsulta : function() { return obtenerTipoConsulta(); },
						annio: function() { return obtenerAnnio(); },
						mes: function() { return obtenerMes(); },
						tipo : function() { return obtenerTipo(); },
						contribuyente: function() { return obtenerContribuyente(); },
						tipoMoneda   : function() { return obtenerTipoMoneda(); },
						
					},
					 
					type: 'POST'
				},
				aoColumns : [
					{ mData: "rfcEmisor", "sClass": "alinearCentro"},
					{ mData: "nombreEmisor", "sClass": "alinearIzquierda"}
					
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
		
		tablaDetalleLista.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	// Aqui se agrega los filtros del encabezado
	$('#tablaDetalleLista thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if (i == 50){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#tablaDetalleLista thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatosLista($(this).parent().index(), this.value);
	});
		
	function filtraDatosLista(columna, texto) {
		tablaDetalleLista
			.column(columna)
	        .search(texto)
	        .draw();
	}

	
	function obtenerTipoConsulta(){
		return $('#TIPOCONSULTA_LISTA').val();
	}
	
	function obtenerAnnio(){
		return $('#ANNIO_LISTA').val();
	}
	
	function obtenerMes(){
		return $('#MES_LISTA').val();
	}
	
	function obtenerTipo(){
		return $('#TIPO_LISTA').val();
	}
	
	function obtenerContribuyente(){
		return $('#CONTRIBUYENTE_LISTA').val();
	}
	function obtenerTipoMoneda(){
		return $('#TIPOMONEDA_LISTA').val();
	}


</script>
</html>