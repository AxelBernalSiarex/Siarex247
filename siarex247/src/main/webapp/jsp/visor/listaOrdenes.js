
var tablaOrdenes = null;

	$(document).ready(function() {
		try {
			tablaOrdenes = $('#tablaOrdenes').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 10,
				lengthChange: false,
//				dom: 'Blfrtip',
//				dom: "<'row mx-0'<'col-md-12'QB><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				dom: "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				ordering    : false,
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Listado de Ordenes con ultimo movimiento', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'listadoOrdenes'} 
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
					url: '/siarex247/visor/dashboard/listaOrdenes.action',
					data : null,
					type: 'POST'
				},
				aoColumns : [
					{ mData: "razonSocial"},
					{ mData: "folioEmpresa", "sClass": "alinearCentro"},
					{ mData: "tipoMoneda", "sClass": "alinearCentro"},
					{ mData: "totalMonto",  "sClass": "text-end"},
					{ mData: "desEstatus"},
					{ mData: null}
					// { mData: null, "sClass": "alinearCentro"}
				],
				columnDefs: [
					{	targets: 1,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:abreModal(\'USD\', \'' + row.folioEmpresa + '\');">' + row.folioEmpresa + '</a>';
				            return rowElement;
				        }
				    },
				    {	targets: 5,
				        render: function (data, type, row) {
				        	rowElement = '';
				        	if (row.estatus == 'A1' || row.estatus == 'A2'){
				        		rowElement = '<span class="badge badge rounded-pill d-block badge-subtle-secondary">En espera <span class="ms-1 fas fa-ban" data-fa-transform="shrink-2"></span></span>';	
				        	}
				        	
				        	if (row.estatus == 'A3'){
				        		rowElement = '<span class="badge badge rounded-pill d-block badge-subtle-warning">Pendiente<span class="ms-1 fas fa-stream" data-fa-transform="shrink-2"></span></span>';
				        	}
				        	
				        	if (row.estatus == 'A4'){
				        		rowElement = '<span class="badge badge rounded-pill d-block badge-subtle-success">Completado<span class="ms-1 fas fa-check" data-fa-transform="shrink-2"></span></span>';
				        	}
				        	
				        	if (row.estatus == 'A5' || row.estatus == 'A9' || row.estatus == 'A10' || row.estatus == 'A11' || row.estatus == 'A12'){
				        		rowElement = '<span class="badge badge rounded-pill d-block badge-subtle-primary">Procesando<span class="ms-1 fas fa-redo" data-fa-transform="shrink-2"></span></span>';
				        	}
				        	
				            return rowElement;
				        }
				    } 
				    /*
				   {
				        targets: 6,
				        render: function (data, type, row) {
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'ver\', \'' + row.folioEmpresa + '\');">'+BTN_VER_MENU+'</a>';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.folioEmpresa + '\');">'+BTN_EDITAR_MENU+'</a>';
				            rowElement += '</div>';
				            rowElement += '</div>';
				                return rowElement;
				          }
				      }
				    */   
				  ],
		          initComplete: function () {
		        	  var btns = $('.dt-button');
		              btns.removeClass('dt-button');
		              
		              var btnsSubMenu = $('.dtb-b2');
		              btnsSubMenu.addClass('dropdown-menu dropdown-menu-end py-0 show');
		              
		        	  var VISOR_ETQ31 = $('#VISOR_ETQ31');
		        	  VISOR_ETQ31.removeClass('text-end');
		        	  
		              
		           },
				  drawCallback: function () {
					  
				  }
			});
			
		} catch(e) {
			alert('usuarios()_'+e);
		};
		
		tablaOrdenes.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	// Aqui se agrega los filtros del encabezado
	$('#tablaOrdenes thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if (i == 6){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#tablaOrdenes thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatosOrdenes($(this).parent().index(), this.value);
	});
		
	function filtraDatosOrdenes(columna, texto) {
		tablaOrdenes
			.column(columna)
	        .search(texto)
	        .draw();
	}

