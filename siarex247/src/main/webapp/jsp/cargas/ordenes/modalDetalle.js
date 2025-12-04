
var detalleBitacora = null;

	$(document).ready(function() {
		try {
			detalleBitacora = $('#detalleBitacora').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 10,
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
				order       : [ [ 0, 'desc' ] ],	
				buttons: [
					{ 	extend: "collection",
				    	autoClose : true,  
				    	text:'<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
				    	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    	buttons: [
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Detalle Historico de Cargas', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'detalleHistoricoCargas'} 
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
					url: '/siarex247/procesos/layout/ordenesCompra/detalleBitacora.action',
					data : {
						claveRegistro :  function() { return getClaveRegistro() },
						tipoReg :  function() { return getTipoReg() }
					},
					type: 'POST'
				},
				aoColumns : [
					{ mData: "claveRegistro", "sClass": "alinearCentro"},
					{ mData: "folioEmpresa", "sClass": "alinearCentro"},
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
		
		detalleBitacora.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	// Aqui se agrega los filtros del encabezado
	$('#detalleBitacora thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if (i == 10){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#detalleBitacora thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatos($(this).parent().index(), this.value);
	});
		
	function filtraDatos(columna, texto) {
		detalleBitacora
			.column(columna)
	        .search(texto)
	        .draw();
	}




	function recargarDetalle(){
		try{
			$('#detalleBitacora').DataTable().ajax.reload(null,false);
		}catch(e){
			alert('recargarDetalle()_'+e);
		}
	}
	
	
	function getClaveRegistro(){
		var claveRegistro = $('#claveRegistro').val();
		return claveRegistro;
	}
	
	function getTipoReg(){
		var tipoReg = $('#tipoReg').val();
		return tipoReg;
	}
	
	