

var tablaDetalleEliminarComplemento = null;

	$(document).ready(function() {
		try {
			tablaDetalleEliminarComplemento = $('#tablaDetalleEliminarComplemento').DataTable( {
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
					url: '/siarex247/visor/tablero/listadoOrdenesComplemento.action',
					data : {
						uuid : function() { return obtenerUUID_EliminarComplemento() }
						
					},
					type: 'POST'
				},
				aoColumns : [
					{ mData: "folioEmpresa","sClass": "alinearCentro"},
					{ mData: "montoOrden"},
					{ mData: "fechaPago"},
					{ mData: "uuidOrden"}
					
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
		
		tablaDetalleEliminarComplemento.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	
	
	
	function eliminarComplementoMenu(){
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
			                //html: '<p>Es necesario seleccionar solo un registro para eliminar.</p>',	
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
									if ( data.estatusOrden == 'A6'){
										iniciaFormEliminarComplemento();
										mostrarInformacionOrdenesEliminar(data.folioEmpresa)
									}else{
										Swal.fire({
				  			                title: MSG_ERROR_OPERACION_MENU,
				  			                //html: '<p>Estatus de la Orden es incorrecta para eliminar</p>',	
				  			                html: '<p>'+VISOR_MSG10+'</p>',
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
			  alert("eliminarComplementoMenu()_"+e);
		  }
	  }
	  
	  
	  function mostrarInformacionOrdenesEliminar(folioEmpresa){
		  try{
			  $.ajax({
					url  : '/siarex247/visor/tablero/consultaComplemento.action',
					type : 'POST', 
					data : {
						folioEmpresa : folioEmpresa
					},
					dataType : 'json',
					success  : function(data) {
						if($.isEmptyObject(data)) {
						} else {
							
							if (data.claveProveedor == 0){
								
								var MENSAJE_13 = VISOR_MSG13.replaceAll('<< FOLIO_EMPRESA >>', folioEmpresa);
								
								Swal.fire({
		  			                title: MSG_ERROR_OPERACION_MENU,
		  			                // html: '<p>La orden de compra '+folioEmpresa+' no tiene asignado complemento de pago</p>',	
		  			                html: '<p>'+MENSAJE_13+'</p>',
		  			                icon: 'info'
		  			            });	
							}else{
								$('#uuidXML_EliminarComplemento').val(data.uuidComplemento);
								$('#folioEmpresa_EliminarComplemento').val(folioEmpresa);
								
								$('#uuidComplemento_EliminarComplemento').val(data.uuidComplemento);
								$('#razonSocial_EliminarComplemento').val(data.razonSocial);
								$('#estatusSAT_EliminarComplemento').val(data.estatusComplemento);
								$('#tablaDetalleEliminarComplemento').DataTable().ajax.reload(null,false);
								$('#myModalEliminarComplemento').modal('show');
							}
						}
					},
					error : function(xhr, ajaxOptions, thrownError) {
						alert('procedeEliminarComplemento()_'+thrownError);
					}
				});
			  
			  
		  }catch(e){
			  alert('procedeEliminarComplemento()_'+e);
		  }
	  }
	  
	
	function iniciaFormEliminarComplemento(){
		/* Reset al Formulario */ 
		$("#frmEliminarComplemento").find('.has-success').removeClass("has-success");
	    $("#frmEliminarComplemento").find('.has-error').removeClass("has-error");
		$('#frmEliminarComplemento')[0].reset(); 
		$('#frmEliminarComplemento').removeClass("was-validated"); 
		   
	}
	
	
	
	
	function ejecutarEliminarComplementoOrdenes(){
		try{
			var uuid = $('#uuidXML_EliminarComplemento').val();
			var folioEmpresa = $('#folioEmpresa_EliminarComplemento').val();
				Swal.fire({
					  icon : 'question',
					 // title: '¿Estas seguro de eliminar el complemento de pago con el folio UUID  ?',
					  title: VISOR_TITLE18,
					  text: 'UUID : '+uuid,
					  showDenyButton: true,
					  showCancelButton: false,
					  confirmButtonText: BTN_ACEPTAR_MENU,
					  denyButtonText: BTN_CANCELAR_MENU,
					}).then((result) => {
					  if (result.isConfirmed) {
						  $.ajax({
							   url:  '/siarex247/visor/tablero/eliminarComplemento.action',
					           type: 'POST',
					            dataType : 'json',
					            data : {
					            	uuid : uuid,
					            	folioEmpresa : folioEmpresa
					            },
					            beforeSend: function( xhr ) {
				        			$('#btnEliminar_CargarComplemento').prop('disabled', true);
				        			$('#overSeccion_EliminarComplemento').css({display:'block'});
				        		},
				        		complete: function(jqXHR, textStatus){
				        			$('#btnEliminar_CargarComplemento').prop('disabled', false);
				        			$('#overSeccion_EliminarComplemento').css({display:'none'});
					    		},
					    		
							    success: function(data){
							    	
							    	if (data.codError == "000"){
							    		Swal.fire({
				 						  title: MSG_OPERACION_EXITOSA_MENU,
				 						  // html: '<p>El complemento de pago se ha eliminado satisfactoriamente. </p>',
				 						   html: '<p>'+ VISOR_MSG14 + ' </p>',
											  showCancelButton: false,
											  confirmButtonText: BTN_ACEPTAR_MENU,
											  icon: 'success'
				 						}).then((result) => {
				 							$('#myModalEliminarComplemento').modal('hide');
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
	
	
	
	function obtenerUUID_EliminarComplemento(){
		try{
			return $('#uuidXML_EliminarComplemento').val();
		}catch(e){
			alert("obtenerUUID_EliminarComplemento()_"+e);
		}
	}
	
	
	
	