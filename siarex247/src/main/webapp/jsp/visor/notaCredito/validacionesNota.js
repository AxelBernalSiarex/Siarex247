

var tablaDetalleCargarNota = null;

	$(document).ready(function() {
		try {
			tablaDetalleCargarNota = $('#tablaDetalleCargarNota').DataTable( {
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Detalle Nota de Credito', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'detalleNotaCredito'}
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
					url: '/siarex247/visor/tablero/detalleNotaCredito.action',
					data : {
						uuid : function() { return obtenerUUID_CargarNota() }
						
					},
					type: 'POST'
				},
				aoColumns : [
					{ mData: "folioEmpresa","sClass": "alinearCentro"},
					{ mData: "uuidOrden"},
					{ mData: "montoOrden"},
					{ mData: "montoNota"}
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
		
		tablaDetalleCargarNota.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	




	function iniciaFormNota(){
		/* Reset al Formulario */ 
		$("#frmCargarNotas").find('.has-success').removeClass("has-success");
	    $("#frmCargarNotas").find('.has-error').removeClass("has-error");
		$('#frmCargarNotas')[0].reset(); 
		$('#frmCargarNotas').removeClass("was-validated"); 
		   
	}
	

	function validarNotaCredito(){
		try{
			 var formData = new FormData(document.getElementById("frmCargarNotas"));
			    $.ajax({
	            	url: '/siarex247/visor/tablero/validarNotaCredito.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                beforeSend: function( xhr ) {
	        			$('#btnSometerNota').prop('disabled', true);
	        			$('#overSeccion_NotaCredito').css({display:'block'});
	        		},
	        		complete: function(jqXHR, textStatus){
	        			$('#btnSometerNota').prop('disabled', false);
	        			$('#overSeccion_NotaCredito').css({display:'none'});
		    		},
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	if (data.codError == '000') {
	            		$('#error_NotaCredito').hide();
	            		$('#exito_NotaCredito').show();
	            		$('#btnGuardar_CargarNota').prop('disabled', false);
	            		$('#uuidXML_NotaCredito').val(data.uuidValida);
	            		$('#mensajeRespuesta_Cargar_Nota').val(data.mensajeValidacion);
	            		$('#razonSocial_Cargar_Nota').val(data.razonSocial);
	            		$('#estadoCFDI_Cargar_Nota').val(data.estadoSAT);
	            		$('#estatusCFDI_Cargar_Nota').val(data.estatusSAT);
	            		$('#tablaDetalleCargarNota').DataTable().ajax.reload(null,false);
	            		//$('#estatusCFDI_Cargar_Nota').DataTable().ajax.reload(null,false);;
					} else {
						$('#mensajeRespuesta_Cargar_Nota').val(data.mensajeValidacion);
						$('#exito_NotaCredito').hide();
						$('#error_NotaCredito').show();
					}
	             });
			
		}catch(e){
			alert('validarComplemento()_'+e);
		}
	 }
	
	
	function guardarAsignar(){
		try{

			var uuid = obtenerUUID_CargarNota();
	 		$.ajax({
				url  : '/siarex247/visor/tablero/cambiaEstatus.action',
				type : 'POST', 
				data : {
					uuid : uuid
					
				},
				dataType : 'json',
				success  : function(data) {
					if (data.codError == '000') {
	            		Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 // html: '<p>La nota de credito se ha asignado satisfactoriamente. </p>',
	 						   html: '<p>'+VISOR_MSG26+' </p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalCargarNota').modal('hide');
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
					alert('guardarAsignar()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('guardarAsignar()_'+e);
		}
	 }
	
	function obtenerUUID_CargarNota(){
		return $('#uuidXML_NotaCredito').val();
	}
	
	
	
	
	
	
	
	

	function iniciaFormValidarNotas(){
		$("#frmValidarNotaCredito").find('.has-success').removeClass("has-success");
	    $("#frmValidarNotaCredito").find('.has-error').removeClass("has-error");
		$('#frmValidarNotaCredito')[0].reset(); 
		$('#frmValidarNotaCredito').removeClass("was-validated"); 
		   
	}
	
	
	
	
	
	
	function consultarNotaCredito(){
		try{
			var folioOrden = getSelections();
			if (folioOrden == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		               // html: '<p>Es necesario seleccionar al menos un registro para validar.</p>',
		                html: '<p>'+VISOR_MSG5+' </p>',
		                icon: 'info'
		            });
			}else{
				var arrFolios = folioOrden.split(";");
				if (arrFolios.length >= 3){
					Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                // html: '<p>Es necesario seleccionar solo un registro para validar.</p>',
		                html: '<p>'+VISOR_MSG6+' </p>',
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
								if ( data.estatusOrden == 'A11'){
									iniciaFormValidarNotas();
									consultarInformacion_NotaCredito(numeroOrden);
									$('#myModalValidarNota').modal('show');
								}else{
									Swal.fire({
			  			                title: MSG_ERROR_OPERACION_MENU,
			  			                // html: '<p>Estatus de la Orden es incorrecta para validar</p>',
			  			                html: '<p>'+VISOR_MSG21+' </p>',
			  			                icon: 'info'
			  			            });									
								}
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert('consultarNotaCredito()_'+thrownError);
						}
					});
				}
			}
		}catch(e){
			alert('consultarNotaCredito()_'+e);
		}
	}
	
	
	function consultarInformacion_NotaCredito(folioOrden){
		try{
			// 
			// var cadFolios = getSelections();
			// var arrFolios = cadFolios.split(";");
			// var folioOrden = arrFolios[0]; 
			$('#folioOrden_ValidarNota').val(folioOrden);
			
			$.ajax({
				url  : '/siarex247/visor/tablero/buscarNotaCredito.action',
				type : 'POST', 
				data : {
					folioOrden : folioOrden
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					} else {
						$('#ordenCompra_ValidarNota').val(data.folioEmpresa);
						$('#razonSocial_ValidarNota').val(data.razonSocial);
						$('#folioFiscal_ValidarNota').val(data.uuidNotaCredito);
						$('#factura_ValidarNota').val(data.serieFactura);
						$('#total_ValidarNota').val(data.montoNotaFormateado);
						$('#fecha_ValidarNota').val(data.fechaFactura);
						$('#estadoSAT_ValidarNota').val(data.estadoSAT);
						$('#estatusSAT_ValidarNota').val(data.estatusSAT);
						cargaMotivos();
						window.open('/siarex247/files/'+data.nombrePDF,'frmPDFNotaCredito');
						
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('consultarInformacion_NotaCredito()_'+thrownError);
				}
			});
		}catch(e){
			alert('consultarInformacion_CFDI()_'+e);
		}
	}
	
	
	function modificarNotaCredito(tipoAccion){
		try{
			
			var claveMotivo = $('#motivoRechazo_ValidarNota').val();
			var folioOrden = $('#folioOrden_ValidarNota').val();
			
			if (claveMotivo == '' && tipoAccion == 'INCORRECTO'){
				Swal.fire({
					  title: MSG_ERROR_OPERACION_MENU,
					  // html: '<p>Debe seleccionar un motivo de rechazo</p>',
					  	html: '<p>'+VISOR_MSG22+'</p>',
						  showCancelButton: false,
						  confirmButtonText: BTN_ACEPTAR_MENU,
						  icon: 'info'
					}).then((result) => {
						
					});
			}else{
				if (tipoAccion == 'CORRECTO'){
					claveMotivo = 0;	
				}
				
				$.ajax({
					url  : '/siarex247/visor/tablero/modificaNotaCredito.action',
					type : 'POST', 
					data : {
						tipoAccion : tipoAccion,
						claveMotivo : claveMotivo,
						folioOrden : folioOrden
						
					},
					dataType : 'json',
					 beforeSend: function( xhr ) {
		        			$('#btnAprobarNotaCredito').prop('disabled', true);
		        			$('#btnRechazarNotaCredito').prop('disabled', true);
		        			$('#overSeccion_ValidarNotaCredito').css({display:'block'});
		        		},
		        		complete: function(jqXHR, textStatus){
		        			$('#btnAprobarNotaCredito').prop('disabled', false);
		        			$('#btnRechazarNotaCredito').prop('disabled', false);
		        			$('#overSeccion_ValidarNotaCredito').css({display:'none'});
			    		},
			    		
					success  : function(data) {
						if (data.codError == '000') {
		            		Swal.fire({
		 						  title: MSG_OPERACION_EXITOSA_MENU,
		 						 html: '<p>Cambio Exitoso. </p>',
									  showCancelButton: false,
									  confirmButtonText: BTN_ACEPTAR_MENU,
									  icon: 'success'
		 						}).then((result) => {
		 							$('#myModalValidarNota').modal('hide');
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
						alert('modificarNotaCredito()_'+thrownError);
					}
				});	
			}
			
		}catch(e){
			alert('modificarNotaCredito()_'+e);
		}
	}
	
	