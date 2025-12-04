
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
				order       : [ [ 5, 'desc' ] ],	
				buttons: [
					{ 	extend: "collection",
				    	autoClose : true,  
				    	text:'<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
				    	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    	buttons: [
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Centro de Costos', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'CatalogoCostos'}
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
					url: '/siarex247/cumplimientoFiscal/conciliacion/cartaPorte/detalleCartaPorte.action',
					type: 'POST',
					/*
					data : {
						anio : function() { return obtenerAnio(); },
						razonSocial : function() { return obtenerRS(); },
						mesCombo : function() { return obtenerMes(); },
						tipoComple : function() { return obtenerComp(); }
					}
					*/
					
				},
				aoColumns : [//, "sClass": "alinearDerecha"
					{ mData: "RFC"},
					{ mData: "RAZON_SOCIAL"},
					{ mData: "FOLIO_EMPRESA", "sClass": "alinearDerecha"},
					{ mData: "TIPO_MONEDA", "sClass": "alinearCentro"},
					{ mData: "SERIE_FOLIO", "sClass": "alinearCentro"},
					{ mData: "FECHAPAGO", "sClass": "alinearCentro"},
					{ mData: "UUID_ORDEN"},
					{ mData: "SUB_TOTAL", "sClass": "alinearDerecha"},
					{ mData: "IVA", "sClass": "alinearDerecha"},
					{ mData: "IVA_RET", "sClass": "alinearDerecha"},
					{ mData: "IMP_LOCALES", "sClass": "alinearDerecha"},
					{ mData: "TOTAL", "sClass": "alinearDerecha"},
					{ mData: "FECHAPAGO_XML", "sClass": "alinearCentro"},
					{ mData: "FECHATIMBRADO", "sClass": "alinearCentro"},
					{ mData: "UUID_CARTA_PORTE"},
					{ mData: "TOTAL_FACTURA"},
					{ mData: "ESTATUS_FACTURA"},
					{ mData: null, "sClass": "alinearCentro"},
					{ mData: null, "sClass": "alinearCentro"},
					{ mData: "ESTATUS_CONCILIACION", "sClass": "alinearCentro"}
				],
				columnDefs: [
					{
	                    targets: 17,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	if (data.ESTATUS_CONCILIACION != 'SIN COMPLEMENTO.'){
	                    		rowElement = '<img class="" src="/theme-falcon/repse247/img/xml26.png" alt="" style="cursor: pointer;" title="Ver XML" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo XML" onclick="generaXML_CP(\'' + row.FOLIO_EMPRESA+ '\', \'' + row.CLAVE_PROVEEDOR+ '\');" />';
	                    	}
	                        return rowElement;
	                      }
	                  },
	                  {
		                 targets: 18,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	if (data.ESTATUS_CONCILIACION != 'SIN COMPLEMENTO.'){
	                    		rowElement = '<img class="" src="/theme-falcon/repse247/img/pdf26.png" alt="" style="cursor: pointer;" title="Ver PDF" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="generaPDF_CP(\'' + row.FOLIO_EMPRESA+ '\', \'' + row.CLAVE_PROVEEDOR+ '\');" />';
	                    	}
	                        return rowElement;
	                      }
	                  }
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
		 if (i == -1){
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




	
	function generaPDF_CP(FOLIO_EMPRESA, CLAVE_PROVEEDOR){
		 document.getElementById('idRegistro_CP').value = FOLIO_EMPRESA;
		 document.getElementById('claveProveedor_CP').value = CLAVE_PROVEEDOR;
		 document.getElementById('tipoArchivo_CP').value = 'PDF';
	     document.frmAbrirArchivoCP.submit();
	 }
	  
	  function generaXML_CP(FOLIO_EMPRESA, CLAVE_PROVEEDOR){
		   document.getElementById('idRegistro_CP').value = FOLIO_EMPRESA;
		   document.getElementById('claveProveedor_CP').value = CLAVE_PROVEEDOR;
		   document.getElementById('tipoArchivo_CP').value = 'XML';
	       document.frmAbrirArchivoCP.submit();
	 }
	  
