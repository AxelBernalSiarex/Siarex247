

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
				order       : [ [ 0, 'desc' ] ],	
				buttons: [
					{ 	// text:'<span class="fab fa-firefox-browser me-1" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Refrescar</span>',
						text: '<div id="btnRefresh_Catalogo"> </div>',
						className: 'btn btn-falcon-success btn-sm mb-2 mb-sm-0',
		                action: function ( e, dt, node, config ) {
		                	refrescar();
		                },
					},
			      	{ 	extend: "collection",
				    	autoClose : true,  
				    	text:'<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
				    	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    	buttons: [
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Detalle Cargas', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'cargaOrdenes'} 
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
					url: '/siarex247/procesos/layout/ordenesCompra/detalleCargas.action',
					type: 'POST'
				},
				aoColumns : [
					{ mData: "fechaTrans", "sClass": "alinearCentro"},
					{ mData: "nombreArchivo"},
					{ mData: "tipoCargaDes"},
					{ mData: "estatusDes"},
					{ mData: "usuarioTrans"},
					{ mData: null, "sClass": "alinearDerecha"},
					{ mData: null, "sClass": "alinearDerecha"},
					{ mData: "totRegistros", "sClass": "alinearDerecha"}
				],
				columnDefs: [
					{	targets: 5,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:mostrarDetalle(\'S\', \'' + row.claveRegistro + '\');">' + row.totOK + '</a>';
				            return rowElement;
				        }
				    },
				    {	targets: 6,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:mostrarDetalle(\'N\', \'' + row.claveRegistro + '\')">' + row.totNG + '</a>';
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
		 if (i == 10){
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




	function abreModal(opcion, id, tipoArchivo) 
	{
		
		switch(opcion) {
		case "ordenes" : 
			iniciaFormCatalogo();
			$('#myModalOrdenes').modal('show');
			break;	
		case "recibos" : 
			iniciaFormCatalogoRecibos();
			$('#myModalRecibos').modal('show');
			break;		
		case "eliminar" : 
			iniciaFormCatalogoEliminar();
			$('#myModalEliminar').modal('show');
			break;
		case "pagos" : 
			iniciaFormCatalogoPagos();
			$('#myModalPagos').modal('show');
			break;
		case "importar" : 
			iniciaFormCatalogoImportar();
			$('#myModalImportar').modal('show');
			break;		
		case "procesar" :
			procesarArchivo(id, tipoArchivo);
			break;
		case "ver" :
			$('#idRegistro_Catalogo').val(id);
			document.getElementById("modal-title-catalogo").innerHTML = "Ver Empleado";
			buscaCatalogo(id, 'ver');
			$('#myModalDetalle').modal('show');
			break;
		default :
		}
		
	}



	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		$("#frmOrdenes").find('.has-success').removeClass("has-success");
	    $("#frmOrdenes").find('.has-error').removeClass("has-error");
		$('#frmOrdenes')[0].reset(); 
		$('#frmOrdenes').removeClass("was-validated"); 
		   
	}
	
	
	


	function iniciaFormCatalogoRecibos(){
		/* Reset al Formulario */ 
		$("#frmContraRecibos").find('.has-success').removeClass("has-success");
	    $("#frmContraRecibos").find('.has-error').removeClass("has-error");
		$('#frmContraRecibos')[0].reset(); 
		$('#frmContraRecibos').removeClass("was-validated"); 
		   
	}
	
	


	function iniciaFormCatalogoEliminar(){
		/* Reset al Formulario */ 
		$("#frmEliminar").find('.has-success').removeClass("has-success");
	    $("#frmEliminar").find('.has-error').removeClass("has-error");
		$('#frmEliminar')[0].reset(); 
		$('#frmEliminar').removeClass("was-validated"); 
		   
	}
	


	function iniciaFormCatalogoPagos(){
		/* Reset al Formulario */ 
		$("#frmPagos").find('.has-success').removeClass("has-success");
	    $("#frmPagos").find('.has-error').removeClass("has-error");
		$('#frmPagos')[0].reset(); 
		$('#frmPagos').removeClass("was-validated"); 
		   
	}
	
	

	function iniciaFormCatalogoImportar(){
		/* Reset al Formulario */ 
		$("#frmImportar").find('.has-success').removeClass("has-success");
	    $("#frmImportar").find('.has-error').removeClass("has-error");
		$('#frmImportar')[0].reset(); 
		$('#frmImportar').removeClass("was-validated"); 
		   
	}
	
	
	
	function cargarOrdenes(){
			try{
				   // var bandProcesar = $("#bandProcesar").prop('checked');
		            var formData = new FormData(document.getElementById("frmOrdenes"));
		            // formData.append("bandProcesar", bandProcesar);
		            $.ajax({
		            	url: '/siarex247/procesos/layout/ordenesCompra/cargarOrdenes.action',
		                dataType: "json",
		                type: "post",
		                data: formData,
		                cache: false,
		                contentType: false,
			    		processData: false
		            }).done(function(data){
		            	var bandProcesar = true;
		            	if (data.codError == '000' && bandProcesar){
		            		$('#myModalOrdenes').modal('hide');
							$('#tablaDetalle').DataTable().ajax.reload(null,false);
							procesarArchivoOrdenes(data.claveRegistro)
		            	}else if (data.codError == '000') {
		            		Swal.fire({
		            			icon: 'success',
		            			title: MSG_OPERACION_EXITOSA_MENU,
		            			text: data.mensajeError,
		            			showCancelButton: false,
		            			confirmButtonText: BTN_ACEPTAR_MENU,
		            			denyButtonText: BTN_CANCELAR_MENU,
	 						}).then((result) => {
	 							if (result.isConfirmed) {
	 								$('#myModalOrdenes').modal('hide');
	 								$('#tablaDetalle').DataTable().ajax.reload(null,false);
	 								
	 							}
	 						});
		            	} else {
		            		Swal.fire({
		            			icon: 'error',
		            			title: MSG_ERROR_OPERACION_MENU,
		            			text: data.mensajeError
		            		});
		            	}
		             });
			}
			catch(e){
				alert('cargarOrdenes()_'+e);
			}
	}
	
	
	
	function cargarRecibos(){
		try{
			  // var bandProcesar = $("#bandProcesarContra").prop('checked');
	            var formData = new FormData(document.getElementById("frmContraRecibos"));
	            // formData.append("bandProcesar", bandProcesar);
	            $.ajax({
	            	url: '/siarex247/procesos/layout/recibos/cargarRecibos.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	var bandProcesar = true;
	            	if (data.codError == '000' && bandProcesar){
	            		$('#myModalRecibos').modal('hide');
						$('#tablaDetalle').DataTable().ajax.reload(null,false);
						procesarArchivoRecibos(data.claveRegistro)
	            	}else if (data.codError == '000') {
	            		Swal.fire({
	            			icon: 'success',
	            			title: MSG_OPERACION_EXITOSA_MENU,
	            			text: data.mensajeError,
	            			showCancelButton: false,
	            			confirmButtonText: BTN_ACEPTAR_MENU,
	            			denyButtonText: BTN_CANCELAR_MENU,
 						}).then((result) => {
 							if (result.isConfirmed) {
 								$('#myModalRecibos').modal('hide');
 								$('#tablaDetalle').DataTable().ajax.reload(null,false);
 								
 							}
 						});
	            	} else {
	            		Swal.fire({
	            			icon: 'error',
	            			title: MSG_ERROR_OPERACION_MENU,
	            			text: data.mensajeError
	            		});
	            	}
	             });
		}
		catch(e){
			alert('cargarRecibos()_'+e);
		}
   }
	
	
	function cargarEliminar(){
		try{
			  // var bandProcesar = $("#bandProcesarEliminar").prop('checked');
	            var formData = new FormData(document.getElementById("frmEliminar"));
	            // formData.append("bandProcesar", bandProcesar);
	            $.ajax({
	            	url: '/siarex247/procesos/layout/eliminarOrdenes/cargarEliminar.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	var bandProcesar = true;
	            	if (data.codError == '000' && bandProcesar){
	            		$('#myModalEliminar').modal('hide');
						$('#tablaDetalle').DataTable().ajax.reload(null,false);
						procesarArchivoEliminar(data.claveRegistro)
	            	}else if (data.codError == '000') {
	            		Swal.fire({
	            			icon: 'success',
	            			title: MSG_OPERACION_EXITOSA_MENU,
	            			text: data.mensajeError,
	            			showCancelButton: false,
	            			confirmButtonText: BTN_ACEPTAR_MENU,
	            			denyButtonText: BTN_CANCELAR_MENU,
 						}).then((result) => {
 							if (result.isConfirmed) {
 								$('#myModalEliminar').modal('hide');
 								$('#tablaDetalle').DataTable().ajax.reload(null,false);
 								
 							}
 						});
	            	} else {
	            		Swal.fire({
	            			icon: 'error',
	            			title: MSG_ERROR_OPERACION_MENU,
	            			text: data.mensajeError
	            		});
	            	}
	             });
		}
		catch(e){
			alert('cargarEliminar()_'+e);
		}
   }	
	
	

	function cargarPagos(){
		try{
			  // var bandProcesar = $("#bandProcesarPago").prop('checked');
	            var formData = new FormData(document.getElementById("frmPagos"));
	            // formData.append("bandProcesar", bandProcesar);
	            $.ajax({
	            	url: '/siarex247/procesos/layout/pagos/cargarPagos.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	var bandProcesar = true;
	            	if (data.codError == '000' && bandProcesar){
	            		$('#myModalPagos').modal('hide');
						$('#tablaDetalle').DataTable().ajax.reload(null,false);
						procesarArchivoPagos(data.claveRegistro)
	            	}else if (data.codError == '000') {
	            		Swal.fire({
	            			icon: 'success',
	            			title: MSG_OPERACION_EXITOSA_MENU,
	            			text: data.mensajeError,
	            			showCancelButton: false,
	            			confirmButtonText: BTN_ACEPTAR_MENU,
	            			denyButtonText: BTN_CANCELAR_MENU,
 						}).then((result) => {
 							if (result.isConfirmed) {
 								$('#myModalPagos').modal('hide');
 								$('#tablaDetalle').DataTable().ajax.reload(null,false);
 								
 							}
 						});
	            	} else {
	            		Swal.fire({
	            			icon: 'error',
	            			title: MSG_ERROR_OPERACION_MENU,
	            			text: data.mensajeError
	            		});
	            	}
	             });
		}
		catch(e){
			alert('cargarPagos()_'+e);
		}
   }	
	

	function cargarImportar(){
		try{
			   // var bandProcesar = $("#bandProcesarImportar").prop('checked');
	            var formData = new FormData(document.getElementById("frmImportar"));
	            // formData.append("bandProcesar", bandProcesar);
	            $.ajax({
	            	url: '/siarex247/procesos/layout/importarProveedores/cargarImportar.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	var bandProcesar = true;
	            	if (data.codError == '000' && bandProcesar){
	            		$('#myModalImportar').modal('hide');
						$('#tablaDetalle').DataTable().ajax.reload(null,false);
						procesarArchivoImportar(data.claveRegistro)
	            	}else if (data.codError == '000') {
	            		Swal.fire({
	            			icon: 'success',
	            			title: MSG_OPERACION_EXITOSA_MENU,
	            			text: data.mensajeError,
	            			showCancelButton: false,
	            			confirmButtonText: BTN_ACEPTAR_MENU,
	            			denyButtonText: BTN_CANCELAR_MENU,
 						}).then((result) => {
 							if (result.isConfirmed) {
 								$('#myModalImportar').modal('hide');
 								$('#tablaDetalle').DataTable().ajax.reload(null,false);
 								
 							}
 						});
	            	} else {
	            		Swal.fire({
	            			icon: 'error',
	            			title: MSG_ERROR_OPERACION_MENU,
	            			text: data.mensajeError
	            		});
	            	}
	             });
		}
		catch(e){
			alert('cargarImportar()_'+e);
		}
   }	
	
	
	
	
	function procesarArchivo(claveRegistro, tipoArchivo){
		
		if (tipoArchivo == '1'){
			procesarArchivoOrdenes(claveRegistro);
		}else if (tipoArchivo == '2'){
			procesarArchivoRecibos(claveRegistro);
		}else if (tipoArchivo == '3'){
			procesarArchivoPagos(claveRegistro);
		}else if (tipoArchivo == '4'){
			procesarArchivoImportar(claveRegistro);
		}else if (tipoArchivo == '5'){
			procesarArchivoEliminar(claveRegistro);
		}
	}
	
	function procesarArchivoOrdenes(claveRegistro){
		try{
			
			$.ajax({
				url  : '/siarex247/procesos/layout/ordenesCompra/procesarArchivo.action',
				type : 'POST', 
				data : {
					claveRegistro : claveRegistro
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
	 							// $('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalle').DataTable().ajax.reload(null,false);
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
					alert('procesarArchivoOrdenes()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('procesarArchivoOrdenes()_'+e);
		}
	 }
	
	
	function procesarArchivoRecibos(claveRegistro){
		try{
			
			$.ajax({
				url  : '/siarex247/procesos/layout/recibos/procesarArchivo.action',
				type : 'POST', 
				data : {
					claveRegistro : claveRegistro
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
	 							// $('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalle').DataTable().ajax.reload(null,false);
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
					alert('procesarArchivoOrdenes()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('procesarArchivoRecibos()_'+e);
		}
	 }
	
	
	function procesarArchivoEliminar(claveRegistro){
		try{
			
			$.ajax({
				url  : '/siarex247/procesos/layout/eliminarOrdenes/procesarArchivo.action',
				type : 'POST', 
				data : {
					claveRegistro : claveRegistro
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
	 							// $('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalle').DataTable().ajax.reload(null,false);
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
					alert('procesarArchivoEliminar()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('procesarArchivoEliminar()_'+e);
		}
	 }
	
	
	
	function procesarArchivoPagos(claveRegistro){
		try{
			
			$.ajax({
				url  : '/siarex247/procesos/layout/pagos/procesarArchivo.action',
				type : 'POST', 
				data : {
					claveRegistro : claveRegistro
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
	 							// $('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalle').DataTable().ajax.reload(null,false);
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
					alert('procesarArchivoPagos()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('procesarArchivoPagos()_'+e);
		}
	 }
	
	function procesarArchivoImportar(claveRegistro){
		try{
			
			$.ajax({
				url  : '/siarex247/procesos/layout/importarProveedores/procesarArchivo.action',
				type : 'POST', 
				data : {
					claveRegistro : claveRegistro
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
	 							// $('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalle').DataTable().ajax.reload(null,false);
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
					alert('procesarArchivoImportar()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('procesarArchivoImportar()_'+e);
		}
	 }
	
	
	function refrescar(){
		try{
			$('#tablaDetalle').DataTable().ajax.reload(null,false);
		}catch(e){
			alert('refrescar()_'+e);
		}
	}
	
	
	
	function obtenerPermisos(){
		try{
			$('#menuOrdenes').hide();
			$('#menuRecibos').hide();
			$('#menuEliminar').hide();
			$('#menuPagos').hide();
			$('#menuProveedores').hide();
				
			$.ajax({
				url  : '/siarex247/seguridad/usuarios/consultaPermisos.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
	 				if (data == null){
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 html: '<p>Error al obtener los permisos del usuario</p>',	
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 						});
	 					
	 				}else{
	 					if (data.idPerfil == 1 || data.idPerfil == 2){
	 						$('#menuOrdenes').show();
	 						$('#menuRecibos').show();
	 						$('#menuProveedores').show();
	 					}
	 					
	 					if (data.idPerfil == 1 || data.idPerfil == 3){
	 						$('#menuPagos').show();
	 						$('#menuEliminar').show();
	 					}
	 					
	 				}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerPermisos()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('obtenerPermisos()_'+e);
		}
	 }
	
	
	function mostrarDetalle(tipoReg, claveRegistro){
		try{
			$('#claveRegistro').val(claveRegistro);
			$('#tipoReg').val(tipoReg);
			recargarDetalle();
			$('#myModalDetalle').modal('show');
		}catch(e){
			alert('mostrarDetalle()_'+e);
		}
	}
	