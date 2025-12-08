

var tablaDetalleCFDI = null;

	$(document).ready(function() {
		try {
			tablaDetalleCFDI = $('#tablaDetalleCFDI').DataTable( {
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Detalle de Validación de Claves de Producto y Servicio', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'validacionClaves'}
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
					url: '/siarex247/visor/tablero/detalleClaveProductoPorOrdenXML.action',
					data : {
						folioEmpresa : function() { return obtenerFolioEmpresa_CFDI() }
						
					},
					type: 'POST'
				},
				aoColumns : [
					{ mData: "claveProduto","sClass": "alinearCentro"},
					{ mData: "descripcionXML"},
					{ mData: "descripcionSAT"},
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
		
		tablaDetalleCFDI.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	
	
	function validarClaves(){
		try{
			var folioOrden = getSelections();
			if (folioOrden == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		               // html: '<p>Es necesario seleccionar al menos un registro para validar.</p>',	
		                html: '<p>'+VISOR_MSG5+'</p>',
		                icon: 'info'
		            });
			}else{
				var arrFolios = folioOrden.split(";");
				if (arrFolios.length >= 3){
					Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                // html: '<p>Es necesario seleccionar solo un registro para validar.</p>',
		                html: '<p>'+VISOR_MSG6+'</p>',
		                icon: 'info'
		            });
				}else{
					var numeroOrden = arrFolios[0]; 
					$.ajax({
						url  : '/siarex247/visor/tablero/consultarOrden.action',
						type : 'POST', 
						data : {
							folioOrden : numeroOrden
						},
						dataType : 'json',
						success  : function(data) {
							if($.isEmptyObject(data)) {
							} else {
								if ( data.estatusOrden == 'A10'){
									consultarInformacion_CFDI();
									$('#myModalValidarCFDI').modal('show');
								}else{
									Swal.fire({
			  			                title: MSG_ERROR_OPERACION_MENU,
			  			                // html: '<p>Estatus de la Orden es incorrecta para validar</p>',
			  			                html: '<p>'+VISOR_MSG21+'</p>',
			  			                icon: 'info'
			  			            });									
								}
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert('validarClaves()_'+thrownError);
						}
					});
				}
			}
		}catch(e){
			alert('validarClaves()_'+e);
		}
	}
	
	
	function consultarInformacion_CFDI(){
		try{
			// 
			var cadFolios = getSelectionsEmpresa();
			var arrFolios = cadFolios.split(";");
			var folioEmpresa = arrFolios[0]; 
			$('#folioEmpresa_ValidarCFDI').val(folioEmpresa);
			
			$.ajax({
				url  : '/siarex247/visor/tablero/consultarClaveProductoPorOrden.action',
				type : 'POST', 
				data : {
					folioEmpresa : folioEmpresa
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					} else {
						$('#valError_ValidacionCFDI').hide();
						$('#valExito_ValidacionCFDI').hide();
						
						if (data.bandActivoCDFI == 'A'){
							$('#valExito_ValidacionCFDI').show();
						}else {
							$('#valError_ValidacionCFDI').show();
						}
						$('#razonSocial_ValidarCFDI').val(data.razonSocial);
						$('#rfc_ValidarCFDI').val(data.rfc);
						$('#ordenCompra_ValidarCFDI').val(data.folioEmpresa);
						$('#usoCFDI_ValidarCFDI').val(data.usoCFDI);
						$('#descripcionUso_ValidarCFDI').val(data.desUsoCFDI);
						
						$('#tablaDetalleCFDI').DataTable().ajax.reload(null,false);
						
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('validarClaves()_'+thrownError);
				}
			});
		}catch(e){
			alert('consultarInformacion_CFDI()_'+e);
		}
	}
	
	
	function guardarUsoCFDI(tipoAccion){
		try{
			
			var usoCFDI = $('#usoCFDI_ValidarCFDI').val();
			var folioEmpresa = $('#folioEmpresa_ValidarCFDI').val();
			
	 		$.ajax({
				url  : '/siarex247/visor/tablero/modificaOrdenClaveProducto.action',
				type : 'POST', 
				data : {
					tipoAccion : tipoAccion,
					usoCFDI : usoCFDI,
					folioEmpresa : folioEmpresa
					
				},
				beforeSend: function( xhr ) {
        			$('#btnAprobarClaves').prop('disabled', true);
        			$('#btnRechazarClaves').prop('disabled', true);
        		},
        		complete: function(jqXHR, textStatus){
        			$('#btnAprobarClaves').prop('disabled', false);
        			$('#btnRechazarClaves').prop('disabled', false);
	    		},
				dataType : 'json',
				success  : function(data) {
	 				if (data.codError == '000') {
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 html: '<p>'+data.mensajeError+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalValidarCFDI').modal('hide');
	  	 						$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
	 						});
					} else {
						Swal.fire({
  			                title: MSG_ERROR_OPERACION_MENU,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('guardarUsoCFDI()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('guardarCatalogo()_'+e);
		}
	 }
	
	function obtenerFolioEmpresa_CFDI(){
		try{
			return $('#folioEmpresa_ValidarCFDI').val();
		}catch(e){
			alert("obtenerFolioEmpresa_CFDI()_"+e);
		}
	}