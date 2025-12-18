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
				order       : [ [ 0, 'asc' ] ],	
				buttons: [
					{ 	//text: '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Nuevo</span>',
						text: '<div id="btnNuevo_Catalogo"> </div>',
						className: 'btn btn-primary me-1 mb-1 btn-sm btnPanel',
		                action: function ( e, dt, node, config ) {
		                    abreModal('nuevo', 0);
		                },
					},
					{ 	extend: "collection",
				    	autoClose : true,  
				    	text:'<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
				    	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    	buttons: [
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Configuracion de Uso CFDI', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'confUsoCFDI'},
				    		{ extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Configuracion de Uso CFDI", filename: 'confUsoCFDI', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/configuracion/clavesUsoCFDI/detalleUso.action',
					type: 'POST'
				},
				
				aoColumns : [
					{ mData: null, "sClass": "alinearCentro"},
					{ mData: "ID_REGISTRO", "sClass": "alinearCentro"},
					{ mData: "RFC"},
					{ mData: "RAZON_SOCIAL"},
					{ mData: "USO_CFDI"},
					{ mData: "CLAVE_PRODUCTO"},
					{ mData: "DES_USO_CFDI"},
					{ mData: "DES_CLAVE_PRODUCTO"},
					{ mData: "DIVISION"},
					{ mData: "GRUPO"},
					{ mData: "CLASE"},
					
				],
				columnDefs: [
					{	targets: 2,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:abreModal(\'ver\', \'' + row.ID_REGISTRO + '\');">' + row.RFC + '</a>';
				            return rowElement;
				        }
				    },
				   {
				        targets:0,
				        render: function (data, type, row) {
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'ver\', \'' + row.ID_REGISTRO + '\');">'+BTN_VER_MENU+'</a>';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.ID_REGISTRO + '\');">'+BTN_EDITAR_MENU+'</a>';
				            rowElement += '<div class="dropdown-divider"></div>';
				            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminarCatalogo(\'' + row.ID_REGISTRO + '\', \'' + row.USO_CFDI + '\', \'' + row.CLAVE_PRODUCTO + '\');">'+BTN_ELIMINAR_MENU+'</a>';
				            rowElement += '</div>';
				            rowElement += '</div>';
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
		 if (i == 0){
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


	function abreModal(opcion, id) {
		$("#btnSometer").show();
		switch(opcion) {
		case "nuevo" :
			$('#idRegistro_Catalogo').val(0);
			iniciaFormCatalogo();
			cargarRFC('');
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_NEW_CATALOGO;
			$('#myModalDetalle').modal('show');
			break;
		case "editar":
			$('#idRegistro_Catalogo').val(id);
			iniciaFormCatalogo();
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_EDIT_CATALOGO;
			buscaCatalogo(id, 'editar');
			$('#myModalDetalle').modal('show');
			break;
		case "ver":
			$('#idRegistro_Catalogo').val(id);
			iniciaFormCatalogo();
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_VIEW_CATALOGO;
			buscaCatalogo(id, 'ver');
			$('#myModalDetalle').modal('show');
			break;
		case "validar":
			$('#myModalvalidar').modal('show');
			buscarValidarPor();
			break;
		case "importar":
			iniciaFormImportar();
			$('#myModalImportar').modal('show');
			break;
		default :
		}
	}


	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		
		$('#rfc').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#rfc').val('0'); // Selecciona primer valor del combo
		$('#rfc').trigger('change'); //Refresca el combo
		
		
		$("#frmConfUso").find('.has-success').removeClass("has-success");
	    $("#frmConfUso").find('.has-error').removeClass("has-error");
		$('#frmConfUso')[0].reset(); 
		$('#frmConfUso').removeClass("was-validated"); 
		   
	}
	
	


	function iniciaFormImportar(){
		$("#form-Cargar-Proveedores").find('.has-success').removeClass("has-success");
	    $("#form-Cargar-Proveedores").find('.has-error').removeClass("has-error");
		$('#form-Cargar-Proveedores')[0].reset(); 
		$('#form-Cargar-Proveedores').removeClass("was-validated"); 
		   
	}
	
	
	function buscaCatalogo(idRegistro, accion){
		$.ajax({
			url  : '/siarex247/configuracion/clavesUsoCFDI/buscarConfUso.action',
			data : {
				idRegistro : idRegistro
			},
			type : 'POST',
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				}
				else {
					
					if (accion == 'ver'){
						 const formCatalogo = document.getElementById("frmConfUso");
					     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
					     for(i=0;i<validarHabilitar.length;i++){
					    	 var idElemento = validarHabilitar[i].id;
					    	 $('#'+idElemento).attr('disabled', true);
					     	
					     }
					}else{
						const formCatalogo = document.getElementById("frmConfUso");
					     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
					     for(i=0;i<validarHabilitar.length;i++){
					    	 var idElemento = validarHabilitar[i].id;
					    	 $('#'+idElemento).attr('disabled', false);
					     	
					     }
					}
					
					
					cargarRFC(data.rfc);
					$('#usoCFDI').val(data.usoCFDI);
	 	 	 		$('#claveProducto').val(data.claveProducto);
	 	 	 		
	 	 	 		if (accion == 'ver'){
	 	 	 			$("#btnSometer").hide();
	 	 	 		}
	 	 	 		
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				Swal.fire({
				  icon: 'error',
				  title: '¡Error en Operacion!',
				  text: 'Error al ejecutar buscaCatalogo()'
				});
			}
		});	
	}
	
	
	
	function eliminarCatalogo(idRegistro, usoCFDI, claveProducto) {
		Swal.fire({
			  icon: 'question',
			  title: TITLE_DELETE_CATALOGO,
			  html : '<p>Uso CFDI : '+usoCFDI+'</p> <p>Clave Producto : '+claveProducto+'</p>',
			  showDenyButton: true,
			  showCancelButton: false,
			  confirmButtonText: BTN_ACEPTAR_MENU,
			  denyButtonText: BTN_CANCELAR_MENU,
			}).then((result) => {
			  if (result.isConfirmed) {
				  $.ajax({
					url: '/siarex247/configuracion/clavesUsoCFDI/eliminarRegistro.action',
					data : {
						idRegistro : idRegistro
					},
      	 			type : 'POST',
      	 			dataType : 'json',
      	 			success  : function(data) {	
      	 				if (data.ESTATUS == 'OK') {
      	 					Swal.fire({
      	 						 icon: 'success',
    	 						  title: MSG_OPERACION_EXITOSA_MENU,
    	 						  html: '<p>'+MSG_REGISTRO_ELIMINADO_MENU+' </p>',
    	 						  showCancelButton: false,
    	 						  confirmButtonText: BTN_ACEPTAR_MENU,
    	 						  denyButtonText: BTN_CANCELAR_MENU,
    	 						}).then((result) => {
    	 						  if (result.isConfirmed) {
    	 							 $('#tablaDetalle').DataTable().ajax.reload(null,false);
    	 						  }
    	 					});
    					}
      	 				else {
						  Swal.fire({
							  icon: 'error',
							  title: '¡Error en Operacion!',
							  text : 'Ocurrio un error al eliminar ' + descripcion,
						  });
    					}
      	 			},
      	 			error : function(xhr, ajaxOptions, thrownError) {
      	 				swal.fire({
       						title : MSG_ERROR_OPERACION_MENU,
       						html  : "Error al ejecutar: eliminar(); ",
       						type: 'error',
       						confirmButtonText: BTN_ACEPTAR_MENU
       					}).then((result) => {
       						swal.close();
       					});
      	 			}
      	 		});
			  }
		})
	}
	
	function agregarValidaciones(){
		try{
			var rfc = $('#rfc').val();
			var usoCFDI = $('#usoCFDI').val();
			var claveProducto = $('#claveProducto').val();

	 		$.ajax({
	 			url  : '/siarex247/configuracion/clavesUsoCFDI/agregaRegistro.action',
				type : 'POST', 
				data : {
					rfc : rfc,
					usoCFDI : usoCFDI,
					claveProducto : claveProducto
				},
				dataType : 'json',
				success  : function(data) {//console.log('DATA AGREGA: ', data);
					if (data.ESTATUS == 'OK') {
	 					Swal.fire({
	 						  icon: 'success',
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 // text: 'El registro se guardó exitosamente',
	 						  html: '<p>'+MSG_ALTA_EXITOSA_MENU+'</p>',
	 						  showCancelButton: false,
	 						  confirmButtonText: BTN_ACEPTAR_MENU,
	 						  denyButtonText: BTN_CANCELAR_MENU,
	 						}).then((result) => {
	 						  if (result.isConfirmed) {
	 							 $('#myModalDetalle').modal('hide');
	  	 						 $('#tablaDetalle').DataTable().ajax.reload(null,false);
	 						  }
	 						  else if (result.isDenied) {}
	 					});
					} else if (data.ESTATUS == 'NO_USO') {
	 					Swal.fire({
						  icon: 'error',
						  title: MSG_ERROR_OPERACION_MENU,
						  text: MSG_CFDI_NO_EXISTE
						});
	 				} else if (data.ESTATUS == 'NO_CLAVE') {
	 					Swal.fire({
						  icon: 'error',
						  title: MSG_ERROR_OPERACION_MENU,
						  text: MSG_CLAVE_NO_EXISTE
						});
	 				} else {
					  Swal.fire({
						  icon: 'error',
						  title: MSG_ERROR_OPERACION_MENU,
						  text: 'Error el guardar el registro'
					  });
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('agregarValidaciones()_1_'+thrownError);
				}
			});			
		}
		catch(e){
			alert('agregarValidaciones(): ' + e);
		}
	 }
	
	
	
	function modificaValidacion(){
		try{
			var idRegistro = $('#idRegistro_Catalogo').val();
			var rfc = $('#rfc').val();
			var usoCFDI = $('#usoCFDI').val();
			var claveProducto = $('#claveProducto').val();

	 		$.ajax({
	 			url  : '/siarex247/configuracion/clavesUsoCFDI/modificaRegistro.action',
				type : 'POST', 
				data : {
					idRegistro : idRegistro,
					rfc : rfc,
					usoCFDI : usoCFDI,
					claveProducto : claveProducto
				},
				dataType : 'json',
				success  : function(data) {//console.log('DATA MODIFICA: ', data);
					if (data.ESTATUS == 'OK') {
	 					Swal.fire({
	 						  icon: 'success',
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						  html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
	 						  showCancelButton: false,
	 						  confirmButtonText: BTN_ACEPTAR_MENU,
	 						  denyButtonText: BTN_CANCELAR_MENU,
	 						}).then((result) => {
	 						  if (result.isConfirmed) {
	 							 $('#myModalDetalle').modal('hide');
	  	 						 $('#tablaDetalle').DataTable().ajax.reload(null,false);
	 						  }
	 						  else if (result.isDenied) {}
	 					});
					}
					else if (data.ESTATUS == 'NO_USO') {
	 					Swal.fire({
						  icon: 'error',
						  title: MSG_ERROR_OPERACION_MENU,
						  text: MSG_CFDI_NO_EXISTE
						});
	 				}
	 				else if (data.ESTATUS == 'NO_CLAVE') {
	 					Swal.fire({
						  icon: 'error',
						  title: MSG_ERROR_OPERACION_MENU,
						  text: MSG_CLAVE_NO_EXISTE
						});
	 				}
	 				else {
					  Swal.fire({
						  icon: 'error',
						  title: MSG_ERROR_OPERACION_MENU,
						  text: 'Error el modificar el registro'
					  });
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('modificaValidacion()_1_'+thrownError);
				}
			});			
		}
		catch(e){
			alert('modificaValidacion(): ' + e);
		}
	 }
	
	
	
	
	function submitFormTipo(tipo){
		$.ajax({
			url: '/siarex247/configuracion/clavesUsoCFDI/grabarUsoCFDI.action',
				data : {
					TIPO_VALIDACION : tipo
				},
	 			type : 'POST',
	 			dataType : 'json',
	 			success  : function(data) {
	 				if (data.ESTATUS == 'OK') {
	 					Swal.fire({
	 					  icon: 'success',
 						  title: MSG_OPERACION_EXITOSA_MENU,
 						  showCancelButton: false,
 						  confirmButtonText: BTN_ACEPTAR_MENU,
 						  denyButtonText: BTN_CANCELAR_MENU,
 						});
				}
	 			else {
				  Swal.fire({
					  icon: 'error',
					  title: '¡Error en Operacion!'
				  });
				}
	 			},
	 			error : function(xhr, ajaxOptions, thrownError) {
	 				alert('submitFormTipo()_1_'+thrownError);
	 			}
	 	});
	}
	
	
	function buscarValidarPor(){
		$.ajax({
			url: '/siarex247/configuracion/clavesUsoCFDI/buscarValidarPor.action',
	 			type : 'POST',
	 			dataType : 'json',
	 			success  : function(data) {
	 				if (data.VALIDACION_USOCFDI == 'VISOR'){
	 					$("#TIPO_VALIDACION_VISOR").prop('checked', true);
	 				}else if (data.VALIDACION_USOCFDI == 'TABLA'){
	 					$("#TIPO_VALIDACION_TABLA").prop('checked', true);
	 				}else if (data.VALIDACION_USOCFDI == 'XML'){
	 					$('#TIPO_VALIDACION_XML').prop('checked', true);
	 				}
	 			},
	 			error : function(xhr, ajaxOptions, thrownError) {
	 				swal.fire({
						title : "¡Error en Operacion!",
						html  : "Error al ejecutar: submitFormTipo(); ",
						type: 'error',
						confirmButtonText: BTN_ACEPTAR_MENU
					}).then((result) => {
						swal.close();
					});
	 			}
	 	});
	}
	
	
	function importarProveedores(){
		try{
			// alert('*********** importarProveedores ***************');
			$("#btnProcesar_Importar").prop('disabled', true);
	            var formData = new FormData(document.getElementById("form-Cargar-Proveedores"));
	            formData.append("dato", "valor");
	            
	            $.ajax({
	            	url: '/siarex247/configuracion/clavesUsoCFDI/importProveedores.action',
	                dataType: "json",
	                beforeSend: function( xhr ) {
	        			$('#overSeccionCA').css({display:'block'});
	        		},
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false,
		    		complete: function(jqXHR, textStatus){
		    		  $('#overSeccionCA').css({display:'none'});
		    		  $("#btnProcesar_Importar").prop('disabled', false);
			    	},
	            }).done(function(data){//console.log('DATA IMPORTAR: ', data);
	            if (data.ESTATUS == 'OK') {
 					Swal.fire({
 						  icon: 'success',
 						  title: MSG_OPERACION_EXITOSA_MENU,
 						  //text : 'Se importaron correctamente los proveedores',
 						  html: '<p>'+MSG_IMPORTAR_CORRECTO+'</p>',
 						  showCancelButton: false,
 						  confirmButtonText: BTN_ACEPTAR_MENU,
 						  denyButtonText: BTN_CANCELAR_MENU,
 						}).then((result) => {
 						  if (result.isConfirmed) {
 							 $('#tablaDetalle').DataTable().ajax.reload(null,false);
 							 $('#myModalImportar').modal('hide');
 						  }
 					});
				}
 				else {
				  Swal.fire({
					  icon: 'error',
					  title: '¡Error en Operacion!',
					  text : 'Ocurrio un error al importar los proveedores'
				  });
				}
	             });
		}
		catch(e){
			e = null;
		}
	}
	
	