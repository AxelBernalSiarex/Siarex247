

var tablaDetalleEliminarNotaCredito = null;

	$(document).ready(function() {
		try {
			tablaDetalleEliminarNotaCredito = $('#tablaDetalleEliminarNotaCredito').DataTable( {
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
						uuid : function() { return obtenerUUID_EliminarNotaCredito() }
						
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
		
		tablaDetalleEliminarNotaCredito.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	
	
	
	function eliminarNotaCreditoMenu(){
		  try{
			  var folioOrden = getSelections();
				if (folioOrden == ''){
					Swal.fire({
			                title: MSG_ERROR_OPERACION_MENU,
			                //html: '<p>Es necesario seleccionar un registro para eliminar.</p>',	
			                html: '<p>'+VISOR_MSG5+'</p>',
			                icon: 'info'
			            });
				}else{
					var arrFolios = folioOrden.split(";");
					if (arrFolios.length >= 3){
						Swal.fire({
			                title: MSG_ERROR_OPERACION_MENU,
			                // html: '<p>Es necesario seleccionar solo un registro para eliminar.</p>',
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
									if ( data.estatusOrden == 'A4' || data.estatusOrden == 'A6'){
										iniciaFormEliminarNotaCredito();
										mostrarInformacionOrdenesNotaCredito(numeroOrden, data.folioEmpresa)
									}else{
										Swal.fire({
				  			                title: MSG_ERROR_OPERACION_MENU,
				  			               // html: '<p>Estatus de la Orden es incorrecta para eliminar</p>',
				  			                html: '<p>'+VISOR_MSG10+'</p>',
				  			                icon: 'info'
				  			            });									
									}
								}
							},
							error : function(xhr, ajaxOptions, thrownError) {
								alert('eliminarNotaCreditoMenu()_'+thrownError);
							}
						});
					}
				}
		  }catch(e){
			  alert("eliminarNotaCreditoMenu()_"+e);
		  }
	  }
	  
	  
	  function mostrarInformacionOrdenesNotaCredito(folioOrden, folioEmpresa){
		  try{
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
							if (data.claveProveedor == 0){
								var MENSAJE_23 = VISOR_MSG23.replaceAll('<< FOLIO_EMPRESA >>', folioEmpresa)
								Swal.fire({
		  			                title: MSG_ERROR_OPERACION_MENU,
		  			                // html: '<p>La orden de compra '+folioEmpresa+' no tiene asignado una nota de credito.</p>',
		  			                html: '<p>'+MENSAJE_23+'</p>',
		  			                icon: 'info'
		  			            });	
							}else{
								$('#uuidXML_EliminarNotaCredito').val(data.uuidNotaCredito);
								$('#folioOrden_EliminarNotaCredito').val(folioOrden);
								$('#folioEmpresa_EliminarNotaCredito').val(folioEmpresa);
								
								$('#uuidNotaCredito_EliminarNotaCredito').val(data.uuidNotaCredito);
								$('#razonSocial_EliminarNotaCredito').val(data.razonSocial);
								$('#estatusSAT_EliminarNotaCredito').val(data.estatusSAT);
								$('#tablaDetalleEliminarNotaCredito').DataTable().ajax.reload(null,false);
								$('#myModalEliminarNotaCredito').modal('show');
							}
						}
					},
					error : function(xhr, ajaxOptions, thrownError) {
						alert('mostrarInformacionOrdenesNotaCredito()_'+thrownError);
					}
				});
			  
			  
		  }catch(e){
			  alert('mostrarInformacionOrdenesNotaCredito()_'+e);
		  }
	  }
	  
	
	function iniciaFormEliminarNotaCredito(){
		/* Reset al Formulario */ 
		$("#frmEliminarNotaCredito").find('.has-success').removeClass("has-success");
	    $("#frmEliminarNotaCredito").find('.has-error').removeClass("has-error");
		$('#frmEliminarNotaCredito')[0].reset(); 
		$('#frmEliminarNotaCredito').removeClass("was-validated"); 
		   
	}
	
	
	
	
	function ejecutarEliminarNotaCreditoOrdenes(){
		try{
			var uuid = $('#uuidXML_EliminarNotaCredito').val();
			var folioOrden = $('#folioOrden_EliminarNotaCredito').val();
			var folioEmpresa = $('#folioEmpresa_EliminarNotaCredito').val();
			
				Swal.fire({
					  icon : 'question',
					  //title: '¿Estas seguro de eliminar la Nota de Crédito con el folio UUID  ?',
					  title: VISOR_MSG24,
					  text: 'UUID : '+uuid,
					  showDenyButton: true,
					  showCancelButton: false,
					  confirmButtonText: BTN_ACEPTAR_MENU,
					  denyButtonText: BTN_CANCELAR_MENU,
					}).then((result) => {
					  if (result.isConfirmed) {
						  $.ajax({
							   url:  '/siarex247/visor/tablero/eliminarNotaCredito.action',
					           type: 'POST',
					            dataType : 'json',
					            data : {
					            	uuid : uuid,
					            	folioOrden : folioOrden,
					            	folioEmpresa : folioEmpresa
					            },
					            beforeSend: function( xhr ) {
				        			$('#btnEliminar_EliminarNotaCredito').prop('disabled', true);
				        			$('#overSeccion_EliminarNotaCredito').css({display:'block'});
				        		},
				        		complete: function(jqXHR, textStatus){
				        			$('#btnEliminar_EliminarNotaCredito').prop('disabled', false);
				        			$('#overSeccion_EliminarNotaCredito').css({display:'none'});
					    		},
					    		
							    success: function(data){
							    	
							    	if (data.codError == "000"){
							    		Swal.fire({
				 						  title: MSG_OPERACION_EXITOSA_MENU,
				 						  // html: '<p>La Nota de Crédito se ha eliminado satisfactoriamente. </p>',
				 						 html: '<p>'+VISOR_MSG25+' </p>',
											  showCancelButton: false,
											  confirmButtonText: BTN_ACEPTAR_MENU,
											  icon: 'success'
				 						}).then((result) => {
				 							$('#myModalEliminarNotaCredito').modal('hide');
				 							$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
				 						});
							    	}else{
							    		Swal.fire({
					 						  title: MSG_ERROR_OPERACION_MENU,
					 						 html: '<p>'+data.mensajeError+'</p>',
												  showCancelButton: false,
												  confirmButtonText: BTN_ACEPTAR_MENU,
												  icon: 'error'
					 						}).then((result) => {
					 							
					 						});
							    	}
							    	
							    	
							    }
							});
					  } else if (result.isDenied) {
						 
					  }
					}) 
				
			
		}catch(e){
			alert('eliminarOrdenes()_'+e);
		}
	}
	
	
	
	function obtenerUUID_EliminarNotaCredito(){
		try{
			return $('#uuidXML_EliminarNotaCredito').val();
		}catch(e){
			alert("obtenerUUID_EliminarNotaCredito()_"+e);
		}
	}
	
	
	
	