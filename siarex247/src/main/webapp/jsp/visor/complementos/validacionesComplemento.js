

var tablaDetalleCargarComplemento = null;

	$(document).ready(function() {
		try {
			tablaDetalleCargarComplemento = $('#tablaDetalleCargarComplemento').DataTable( {
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Detalle Complementos de Pago', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'detalleComplemento'}
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
					url: '/siarex247/visor/tablero/detalleComplemento.action',
					data : {
						uuid : function() { return obtenerUUID_CargarComplemento() }
						
					},
					type: 'POST'
				},
				aoColumns : [
					{ mData: "folioEmpresa","sClass": "alinearCentro"},
					{ mData: "uuidOrden"},
					{ mData: "montoOrden"},
					{ mData: "montoComplemento"},
					{ mData: "fechaPago"}
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
		
		tablaDetalleCargarComplemento.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	
	function iniciaFormComplementos(){
		/* Reset al Formulario */ 
		$("#frmCargarComplemento").find('.has-success').removeClass("has-success");
	    $("#frmCargarComplemento").find('.has-error').removeClass("has-error");
		$('#frmCargarComplemento')[0].reset(); 
		$('#frmCargarComplemento').removeClass("was-validated"); 
		   
	}
	

	function validarComplemento(){
		try{
			 var formData = new FormData(document.getElementById("frmCargarComplemento"));
			    $.ajax({
	            	url: '/siarex247/visor/tablero/validarComplemento.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                beforeSend: function( xhr ) {
	        			$('#btnSometerComplemento').prop('disabled', true);
	        			$('#overSeccion_Complemento').css({display:'block'});
	        		},
	        		complete: function(jqXHR, textStatus){
	        			$('#btnSometerComplemento').prop('disabled', false);
	        			$('#overSeccion_Complemento').css({display:'none'});
		    		},
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	if (data.codError == '000') {
	            		$('#error_Complemento').hide();
	            		$('#exito_Complemento').show();
	            		$('#btnGuardar_CargarComplemento').prop('disabled', false);
	            		$('#uuidXML_Complemento').val(data.uuidValida);
	            		$('#mensajeRespuesta_Cargar_Complemento').val(data.mensajeValidacion);
	            		$('#razonSocial_Cargar_Complemento').val(data.razonSocial);
	            		$('#estadoCFDI_Cargar_Complemento').val(data.estadoSAT);
	            		$('#estatusCFDI_Cargar_Complemento').val(data.estatusSAT);
	            		$('#tablaDetalleCargarComplemento').DataTable().ajax.reload(null,false);
	            		//$('#estatusCFDI_Cargar_Nota').DataTable().ajax.reload(null,false);;
					} else {
						$('#mensajeRespuesta_Cargar_Complemento').val(data.mensajeValidacion);
						$('#exito_Complemento').hide();
						$('#error_Complemento').show();
					}
	             });
			
		}catch(e){
			alert('validarComplemento()_'+e);
		}
	 }
	
	
	function guardarAsignarComplemento(){
		try{
			
			var uuid = obtenerUUID_CargarComplemento();
	 		$.ajax({
				url  : '/siarex247/visor/tablero/cambiaEstatusComplemento.action',
				type : 'POST', 
				data : {
					uuid : uuid
					
				},
				dataType : 'json',
				success  : function(data) {
					if (data.codError == '000') {
	            		Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 // html: '<p>El complemento de pago se ha asignado satisfactoriamente. </p>',
	 						   html: '<p>'+VISOR_MSG15+' </p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalCargarComplemento').modal('hide');
	 							$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
	 						});
					} else {
						Swal.fire({
							 title: MSG_ERROR_OPERACION_MENU,
	 						 html: '<p>'+data.mensajeError+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'error'
	 						}).then((result) => {
	 							
	 						});
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('guardarAsignarComplemento()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('guardarAsignarComplemento()_'+e);
		}
	 }
	
	
	function eliminarComplemento(){
		try{
			
			var uuid = obtenerUUID_CargarComplemento();
	 		$.ajax({
				url  : '/siarex247/visor/tablero/eliminarComplementoPantalla.action',
				type : 'POST', 
				data : {
					uuid : uuid
					
				},
				dataType : 'json',
				success  : function(data) {
					$('#myModalCargarComplemento').modal('hide');
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('eliminarComplemento()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('eliminarComplemento()_'+e);
		}
	 }
	
	function obtenerUUID_CargarComplemento(){
		try{
			return $('#uuidXML_Complemento').val();
		}catch(e){
			alert('obtenerUUID_CargarComplemento()_'+e);
		}
	}
	
	
	
	
	
	
	
	
	
	