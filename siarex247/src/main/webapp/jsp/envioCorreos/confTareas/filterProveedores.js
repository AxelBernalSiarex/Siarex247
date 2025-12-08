
var tablaDetalleProveedores = null;

	$(document).ready(function() {
		try {
			tablaDetalleProveedores = $('#tablaDetalleProveedores').DataTable( {
				paging      : false,
				retrieve: true,
				pageLength  : 15,
				lengthChange: false,
				dom: 'rtipl',
//				dom: "<'row mx-0'<'col-md-12'QB><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
//				dom: "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				ordering    : true,
				serverSide	: false,
				fixedHeader : true,
				orderCellsTop: true,
				info		: true,
				select      : true,
				stateSave	: false, 
				order       : [ [ 0, 'desc' ] ],	
				buttons: null,
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
					select: {
	                    rows: "" 
	                },
					oPaginate: {
				           sFirst : "Primero",
				           sLast  : "Último",
				           sNext  : "<span class='fa fa-chevron-right fa-w-10'></span>",
				           sPrevious : "<span class='fa fa-chevron-left fa-w-10'></span>"
				   	}
				},
				ajax : {
					url: '/siarex247/catalogos/proveedores/comboProveedores.action',
					data : {
						tipoProveedor : function() { return obtenerTipoProveedor() },
						bandTareas : 'true'
		             },	
					type: 'POST'
				},
				aoColumns : [
					//{ mData: null},
					{ mData: "rfc"},
					{ mData: "razonSocial"}
				],
				  
			});
			
		} catch(e) {
			alert('usuarios()_'+e);
		};
		
		tablaDetalleProveedores.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	// Aqui se agrega los filtros del encabezado
	$('#tablaDetalleProveedores thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if (i == 5){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#tablaDetalleProveedores thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatos($(this).parent().index(), this.value);
	});
		
	function filtraDatos(columna, texto) {
		tablaDetalleProveedores
			.column(columna)
	        .search(texto)
	        .draw();
	}
	
	
	function obtenerTipoProveedor(tipoProveedor){
		var tipoProveedor = null;
		try{
			tipoProveedor =  $('#tipoProveedor').val();
		}catch(e){
			alert('obtenerTipoProveedor()_'+e);
		}
		return tipoProveedor;
	}
	
	
	function getSelectionsDatatable(){
		var llavesProveedores  = '';
		try{
			var dataSelect = tablaDetalleProveedores.rows('.selected').data(); 
			$.each(dataSelect, function(key, row) {
				llavesProveedores += row.claveRegistro + ",";
		    });
			
		}catch(e){
			alert('getSelectionsDatatable()_'+e);
		}
		return llavesProveedores;
	}
	
	