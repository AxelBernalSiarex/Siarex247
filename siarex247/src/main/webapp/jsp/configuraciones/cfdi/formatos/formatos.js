



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
					{ 	// text: '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Nuevo</span>',
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Configuración de Formatos', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'ConfFormatos'},
				    		{ extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Configuracion de Formatos", filename: 'ConfFormatos', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/procesos/formatos/listaFormatos.action',
					type: 'POST'
				},
				aoColumns : [
					{ mData: null, "sClass": "alinearCentro" },
					{ mData: "idRegistro", "sClass": "alinearCentro"},
					{ mData: "descripcion"},
					{ mData: "subjectCorreo"},
					{ mData: "nombreArchivo"},
					{ mData: "tipoProveedor", "sClass": "alinearCentro"}
					
				],
				columnDefs: [
					{	targets: 2,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:abreModal(\'ver\', \'' + row.idRegistro + '\');">' + row.descripcion + '</a>';
				            return rowElement;
				        }
				    },
				   {
				        targets: 0,
				        render: function (data, type, row) {
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'ver\', \'' + row.idRegistro + '\');">'+BTN_VER_MENU+'</a>';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.idRegistro + '\');">'+BTN_EDITAR_MENU+'</a>';
				            rowElement += '<div class="dropdown-divider"></div>';
				            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaFormato(\'' + row.idRegistro + '\', \'' + row.descripcion + '\');">'+BTN_ELIMINAR_MENU+'</a>';
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
		$("#fileFormato").show();
		$("#CONF_ENVIO_FORMATOS_ETQ15").show();
		switch(opcion) {
		case "nuevo" :
			$('#idRegistro_Catalogo').val(0);
			$('#cuerpoCorreo').text('');
			// $('#fileFormato').next('label').html('Seleccionar archivo PDF');
			// $('#fileFormato').val(null);
			iniciaFormCatalogo();    	
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_NEW_CATALOGO;
			$('#myModalDetalle').modal('show');
			break;
		case "editar":
			$('#idRegistro_Catalogo').val(id);
			iniciaFormCatalogo();
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_EDIT_CATALOGO;
			buscaFormato(id, 'editar');
			$('#myModalDetalle').modal('show');
			break;
		case "ver":
			$('#idRegistro_Catalogo').val(id);
			iniciaFormCatalogo();
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_VIEW_CATALOGO;
			buscaFormato(id, 'ver');
			$('#myModalDetalle').modal('show');
			break;	
		case "insMEX":
			iniciaFormCatalogoMEX();
			buscaInfo('MEX');
			$('#myModalMEX').modal('show');
			break;
		case "insUSA":
			// $('#fileFormatoUSA').next('label').html('Seleccionar archivo PDF');
			// $('#fileFormatoUSA').val(null);
			// $('#fileFormatoXLSUSA').next('label').html('Seleccionar archivo EXCEL');
			// $('#fileFormatoXLSUSA').val(null);
			iniciaFormCatalogoUSA();
			buscaInfo('USA');
			$('#myModalUSA').modal('show');
			break;
		default :
		}
	}

	
	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		$("#frmFormatos").find('.has-success').removeClass("has-success");
	    $("#frmFormatos").find('.has-error').removeClass("has-error");
		$('#frmFormatos')[0].reset(); 
		$('#frmFormatos').removeClass("was-validated"); 
		   
	}
	

	function iniciaFormCatalogoMEX(){
		/* Reset al Formulario */ 
		$("#frmFormatosMEX").find('.has-success').removeClass("has-success");
	    $("#frmFormatosMEX").find('.has-error').removeClass("has-error");
		$('#frmFormatosMEX')[0].reset(); 
		$('#frmFormatosMEX').removeClass("was-validated"); 
		   
	}
	
	
	function iniciaFormCatalogoUSA(){
		/* Reset al Formulario */ 
		$("#frmFormatosUSA").find('.has-success').removeClass("has-success");
	    $("#frmFormatosUSA").find('.has-error').removeClass("has-error");
		$('#frmFormatosUSA')[0].reset(); 
		$('#frmFormatosUSA').removeClass("was-validated"); 
		   
	}
	
	function buscaFormato(idRegistro, accion){
		$.ajax({
			url  : '/siarex247/procesos/formatos/consultaFormato.action',
			type : 'POST', 
			data : {
				idRegistro : idRegistro
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				}
				else {
					
					if (accion == 'ver'){
						 const formCatalogo = document.getElementById("frmFormatos");
					     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
					     for(i=0;i<validarHabilitar.length;i++){
					    	 var idElemento = validarHabilitar[i].id;
					    	 $('#'+idElemento).attr('disabled', true);
					     	
					     }
					}else{
						const formCatalogo = document.getElementById("frmFormatos");
					     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
					     for(i=0;i<validarHabilitar.length;i++){
					    	 var idElemento = validarHabilitar[i].id;
					    	 $('#'+idElemento).attr('disabled', false);
					     	
					     }
					}
					
					$('#descripcion').val(data.descripcion);
	 	 	 		$('#subjectCorreo').val(data.subjectCorreo);
	 	 	 		$('#cuerpoCorreo').text(data.cuerpoCorreo);
	 	 	 		if (accion == 'ver'){
	 	 	 			$("#btnSometer").hide();
	 	 	 			$("#CONF_ENVIO_FORMATOS_ETQ15").hide();
	 	 	 			$("#fileFormato").hide();
	 	 	 		}
	 	 	 		
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				Swal.fire({
				  icon: 'error',
				  title: '¡Error en Operacion!',
				  text: 'Error al ejecutar buscaFormato()'
				});
			}
		});
	}

	function guardaFormato(){
		$(function(){
			try{
		            var formData = new FormData(document.getElementById("frmFormatos"));
		            formData.append("dato", "valor");

		            $.ajax({
		            	url: '/siarex247/procesos/formatos/altaFormato.action',
		                dataType: "json",
		                type: "post",
		                data: formData,
		                cache: false,
		                contentType: false,
			    		 processData: false
		            }).done(function(data){//console.log('DATA AGREGA: ', data);
		            if (data.codError == '000') {
	 					Swal.fire({
	 						  icon: 'success',
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						  text: data.mensajeError,
	 						  showCancelButton: false,
	 						  confirmButtonText: BTN_ACEPTAR_MENU,
	 						  denyButtonText: BTN_CANCELAR_MENU,
	 						}).then((result) => {
	 						  if (result.isConfirmed) {
	 							 $('#myModalDetalle').modal('hide');
	  	 						 $('#tablaDetalle').DataTable().ajax.reload(null,false);
	 						  }
	 					});
					}
	 				else {
					  Swal.fire({
						  icon: 'error',
						  title: '¡Error en Operacion!',
						  text: data.mensajeError
					  });
					}
		             });
			}
			catch(e){
				e = null;
			}
	    });
	}
	
	function modificaFormato(){
		$(function(){
			try{
		            var formData = new FormData(document.getElementById("frmFormatos"));
		            formData.append("dato", "valor");

		            $.ajax({
		            	url: '/siarex247/procesos/formatos/actualizaFormato.action',
		                dataType: "json",
		                type: "post",
		                data: formData,
		                cache: false,
		                contentType: false,
			    		 processData: false
		            }).done(function(data){//console.log('DATA MODIFICA: ', data);
		            if (data.codError == '000') {
	 					Swal.fire({
	 						  icon: 'success',
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						  text: data.mensajeError,
	 						  showCancelButton: false,
	 						  confirmButtonText: BTN_ACEPTAR_MENU,
	 						  denyButtonText: BTN_CANCELAR_MENU,
	 						}).then((result) => {
	 						  if (result.isConfirmed) {
	 							 $('#myModalDetalle').modal('hide');
	  	 						 $('#tablaDetalle').DataTable().ajax.reload(null,false);
	 						  }
	 					});
					}
	 				else {
					  Swal.fire({
						  icon: 'error',
						  title: '¡Error en Operacion!',
						  text: data.mensajeError
					  });
					}
		             });
			}
			catch(e){
				e = null;
			}
	    });
	}
	
	function eliminaFormato(idRegistro, descripcion) {
		Swal.fire({
		  icon: 'question',
		  title: TITLE_DELETE_CATALOGO,
		  text: LABEL_CONF_FORMATOS + ' : ' + descripcion,
		  showDenyButton: true,
		  showCancelButton: false,
		  confirmButtonText: BTN_ACEPTAR_MENU,
		  denyButtonText: BTN_CANCELAR_MENU,
		}).then((result) => {
		  if (result.isConfirmed) {
			  $.ajax({
				  	url  : '/siarex247/procesos/formatos/eliminaFormato.action',
      	 			type : 'POST', 
      	 			data : {
      	 				idRegistro : idRegistro
      	 			},
      	 			dataType : 'json',
      	 			success  : function(data) {						
      	 				if (data.codError == '000') {
      	 					Swal.fire({
      	 						 icon: 'success',
    	 						  title: MSG_OPERACION_EXITOSA_MENU,
    	 						  text: data.mensajeError,
    	 						  showCancelButton: false,
    	 						  confirmButtonText: BTN_ACEPTAR_MENU,
    	 						  denyButtonText: BTN_CANCELAR_MENU,
    	 						}).then((result) => {
    	 						  if (result.isConfirmed) {
    	 							 $('#tablaDetalle').DataTable().ajax.reload(null,false);	    	 							  
    	 						  } else if (result.isDenied) {
    	 						    
    	 						  }
    	 					});
    					}
      	 				else {
						  Swal.fire({
							  icon: 'error',
							  title: '¡Error en Operacion!',
							  text: data.mensajeError
						  });
    					}
      	 			},
      	 			error : function(xhr, ajaxOptions, thrownError) {
      	 				alert('eliminaFormato()_1_'+thrownError);
      	 			}
      	 		});
		  } else if (result.isDenied) {
		    
		  }
		})
	}
	
	function buscaInfo(proveedor){
		$.ajax({
			url  : '/siarex247/procesos/formatos/consultaFormatoInstrucciones.action',
			data : {
				tipoProveedor : proveedor
			},
			type : 'POST',
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				}
				else {
					$('#descripcion'+proveedor).val(data.descripcion);
	 	 	 		$('#subjectCorreo'+proveedor).val(data.subjectCorreo);
	 	 	 		$('#cuerpoCorreo'+proveedor).text(data.cuerpoCorreo);
	 	 	 		$('#copiaPara'+proveedor).val(data.copiaPara);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				Swal.fire({
				  icon: 'error',
				  title: '¡Error en Operacion!',
				  text: 'Error al ejecutar buscaInfo()'
				});
			}
		});
	}
	
	
	
	function guardaFormatoMEX(){
		$(function(){
			try{
		            var formData = new FormData(document.getElementById("frmFormatosMEX"));
		            formData.append("dato", "valor");

		            $.ajax({
		            	url: '/siarex247/procesos/formatos/altaFormatoInstrucciones.action',
		                dataType: "json",
		                type: "post",
		                data: formData,
		                cache: false,
		                contentType: false,
			    		 processData: false
		            }).done(function(data){//console.log('DATA AGREGA: ', data);
		            if (data.codError == '000') {
	 					Swal.fire({
	 						  icon: 'success',
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						  text: data.mensajeError,
	 						  showCancelButton: false,
	 						  confirmButtonText: BTN_ACEPTAR_MENU,
	 						  denyButtonText: BTN_CANCELAR_MENU,
	 						}).then((result) => {
	 						  if (result.isConfirmed) {
	 							 $('#myModalMEX').modal('hide');
	 						  }
	 					});
					}
	 				else {
					  Swal.fire({
						  icon: 'error',
						  title: '¡Error en Operacion!',
						  text: data.mensajeError
					  });
					}
		             });
			}
			catch(e){
				e = null;
			}
	    });
	}
	
	
	
	function guardaFormatoUSA(){
		$(function(){
			try{
		            var formData = new FormData(document.getElementById("frmFormatosUSA"));
		            formData.append("dato", "valor");

		            $.ajax({
		            	url: '/siarex247/procesos/formatos/altaFormatoInstrucciones.action',
		                dataType: "json",
		                type: "post",
		                data: formData,
		                cache: false,
		                contentType: false,
			    		 processData: false
		            }).done(function(data){//console.log('DATA AGREGA: ', data);
		            if (data.codError == '000') {
	 					Swal.fire({
	 						  icon: 'success',
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						  text: data.mensajeError,
	 						  showCancelButton: false,
	 						  confirmButtonText: BTN_ACEPTAR_MENU,
	 						  denyButtonText: BTN_CANCELAR_MENU,
	 						}).then((result) => {
	 						  if (result.isConfirmed) {
	 							 $('#myModalUSA').modal('hide');
	 						  }
	 					});
					}
	 				else {
					  Swal.fire({
						  icon: 'error',
						  title: '¡Error en Operacion!',
						  text: data.mensajeError
					  });
					}
		             });
			}
			catch(e){
				e = null;
			}
	    });
	}
	