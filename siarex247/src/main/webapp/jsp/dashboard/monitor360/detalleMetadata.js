
var tablaDetalle = null;

	$(document).ready(function() {
		try {
			tablaDetalle = $('#tablaDetalle').DataTable( {
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
					url: '/siarex247/visor/monitor360/detalleMetadata.action',
					 
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
					{ mData: "uuid", "sClass": "alinearCentro"},
					{ mData: "rfcEmisor", "sClass": "alinearCentro"},
					{ mData: "nombreEmisor", "sClass": "alinearIzquierda"},
					{ mData: "rfcReceptor", "sClass": "alinearCentro"},
					{ mData: "nombreReceptor"},
					{ mData: "rfcPac", "sClass": "alinearCentro"},
					{ mData: "fechaEmision", "sClass": "alinearCentro"},
					{ mData: "fechaCertificacion", "sClass": "alinearCentro"},
					{ mData: "montoDes", "sClass": "alinearDerecha"},
					{ mData: "efectoComprobante", "sClass": "alinearCentro"},
					{ mData: "tipoMoneda", "sClass": "alinearCentro"},
					{ mData: "estatus", "sClass": "alinearCentro"},
					{ mData: "fechaCancelacion"},
					{ mData: "existeBoveda", "sClass": "alinearCentro"}
					
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
		
		tablaDetalle.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	// Aqui se agrega los filtros del encabezado
	$('#tablaDetalle thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if (i == 50){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#tablaDetalle thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatos($(this).parent().index(), this.value);
	});
		
	function filtraDatos(columna, texto) {
		tablaDetalle
			.column(columna)
	        .search(texto)
	        .draw();
	}

	
	function obtenerTipoConsulta(){
		return $('#TIPOCONSULTA_DETALLE').val();
	}
	
	function obtenerAnnio(){
		return $('#ANNIO_DETALLE').val();
	}
	
	function obtenerMes(){
		return $('#MES_DETALLE').val();
	}
	
	function obtenerTipo(){
		return $('#TIPO_DETALLE').val();
	}
	
	function obtenerContribuyente(){
		return $('#CONTRIBUYENTE_DETALLE').val();
	}
	function obtenerTipoMoneda(){
		return $('#TIPOMONEDA_DETALLE').val();
	}

