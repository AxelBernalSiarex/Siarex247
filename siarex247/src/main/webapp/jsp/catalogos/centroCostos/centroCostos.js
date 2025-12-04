
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Centro de Costos', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'CatalogoCostos'},
				    		{ extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Centro de Costos", filename: 'CatalogoCostos', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/catalogos/centroCostos/detalleCentros.action',
					type: 'POST'
				},
				aoColumns : [
					{ mData: null, "sClass": "alinearCentro"},
					{ mData: null, "sClass": "alinearCentro"},
					{ mData: "DEPARTAMENTO"},
					{ mData: "CORREO"},
					{ mData: "FECHA_TRANS"}
					
				],
				columnDefs: [
					{	targets: 1,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:abreModal(\'ver\', \'' + row.ID_REGISTRO + '\');">' + row.NOMBRE_CORTO + '</a>';
				            return rowElement;
				        }
				    },
				   {
				        targets: 0,
				        render: function (data, type, row) {
				        	 
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" style="z-index: 2;" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton" style="z-index: 3;" >';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'ver\', \'' + row.ID_REGISTRO + '\');">'+BTN_VER_MENU+'</a>';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.ID_REGISTRO + '\');">'+BTN_EDITAR_MENU+'</a>';
				            rowElement += '<div class="dropdown-divider"></div>';
				            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaCatalogo(\'' + row.ID_REGISTRO + '\', \'' + row.NOMBRE_CORTO + '\');">'+BTN_ELIMINAR_MENU+'</a>';
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





	function abreModal(opcion, id) 
	{
			
		switch(opcion) {
		case "nuevo" : 
			$('#idRegistro_Catalogo').val(0);
			iniciaFormCatalogo();
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_NEW_CATALOGO;
			$('#myModalDetalle').modal('show');
			break;			
		case "editar" :
			$('#idRegistro_Catalogo').val(id);
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_EDIT_CATALOGO;
			iniciaFormCatalogo();
			buscaCatalogo(id, 'editar');
			$('#myModalDetalle').modal('show');
			break;
		case "ver" :
			$('#idRegistro_Catalogo').val(id);
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_VIEW_CATALOGO;
			//iniciaFormCatalogo();
			buscaCatalogo(id, 'ver');
			$('#myModalDetalle').modal('show');
			break;
		default :
		}
	}



	

	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		$("#form-Catalogo").find('.has-success').removeClass("has-success");
	    $("#form-Catalogo").find('.has-error').removeClass("has-error");
		$('#form-Catalogo')[0].reset(); 
		$('#form-Catalogo').removeClass("was-validated"); 
		   
	}
	

	
	function buscaCatalogo(idReg, accion){
		$.ajax({
			url  : '/siarex247/catalogos/centroCostos/buscarCentroCosto.action',
			type : 'POST', 
			data : {
				idRegistro : idReg
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					if (accion == 'ver'){
						 const formCatalogo = document.getElementById("form-Catalogo");
					     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
					     for(i=0;i<validarHabilitar.length;i++){
					    	 var idElemento = validarHabilitar[i].id;
					    	 $('#'+idElemento).attr('disabled', true);
					     	
					     }
					}else{
						const formCatalogo = document.getElementById("form-Catalogo");
					     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
					     for(i=0;i<validarHabilitar.length;i++){
					    	 var idElemento = validarHabilitar[i].id;
					    	 $('#'+idElemento).attr('disabled', false);
					     	
					     }
					}
					
					$('#idRegistro_Catalogo').val(data.idRegistro);
					$('#txtIdCentro').val(data.idCentro);
					$('#txtDepartamento').val(data.departamento);
					$('#txtCorreo').val(data.correoCentro);
					
					if (accion == 'ver'){
						$( "#btnSometer" ).hide();
					}else{
						$( "#btnSometer" ).show();
					}
					
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('buscaCatalogo()_'+thrownError);
			}
		});	
		
    }
	
	

	function eliminaCatalogo(idRegistro, idCentro){
		try{
			
			Swal.fire({
				  icon : 'question',
				 // title: '¿Estas seguro de eliminar el Centro de Costos ?',
				  title: TITLE_DELETE_CATALOGO,
				 // text: 'Centro de Costos : '+idCentro,
				  text: LABEL_CENTRO_COSTOS + ' : '+idCentro,
				  showDenyButton: true,
				  showCancelButton: false,
				  confirmButtonText: BTN_ACEPTAR_MENU,
				  denyButtonText: BTN_CANCELAR_MENU,
				}).then((result) => {
				  if (result.isConfirmed) {
					  $.ajax({
				           url:  '/siarex247/catalogos/centroCostos/eliminarCentro.action',
				           type: 'POST',
				            dataType : 'json',
				            data : {
				            	idRegistro : idRegistro
				            },
						    success: function(data){
						    	
						    	if (data.codError == "000"){
						    		Swal.fire({
			 						  title: MSG_OPERACION_EXITOSA_MENU,
			 						 html: '<p>'+MSG_REGISTRO_ELIMINADO_MENU+' </p>',
										  showCancelButton: false,
										  confirmButtonText: BTN_ACEPTAR_MENU,
										  icon: 'success'
			 						}).then((result) => {
			 							$('#tablaDetalle').DataTable().ajax.reload(null,false);
			 						});
						    	}else{
						    		Swal.fire({
		  			                title: MSG_ERROR_OPERACION_MENU,
		  			                html: '<p>'+data.mensajeError+'</p>',	
		  			                icon: 'error'
		  			            });
						    	}
						    	
						    	
						    }
						});
				  } else if (result.isDenied) {
					 
				  }
				})
		}catch(e){
			alert('eliminaCatalogo()_'+e);
		}
		
	}	
	
	
	function guardarCatalogo(){
		try{
			
			var idCentro = $('#txtIdCentro').val();
			var departamento = $('#txtDepartamento').val();
			var correoCentro = $('#txtCorreo').val();
			
	 		$.ajax({
				url  : '/siarex247/catalogos/centroCostos/altaCentro.action',
				type : 'POST', 
				data : {
					idCentro : idCentro,
					departamento : departamento,
					correoCentro : correoCentro
					
				},
				dataType : 'json',
				success  : function(data) {
	 				if (data.codError == '000') {
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 html: '<p>'+MSG_ALTA_EXITOSA_MENU+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalDetalle').modal('hide');
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
					alert('guardarCatalogo()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('guardarCatalogo()_'+e);
		}
	 }
	
	
	
	function actualizarCatalogo(){
		try{
			var idRegistro = $('#idRegistro_Catalogo').val();
			var idCentro = $('#txtIdCentro').val();
			var departamento = $('#txtDepartamento').val();
			var correoCentro = $('#txtCorreo').val();
			
	 		$.ajax({
				url  : '/siarex247/catalogos/centroCostos/modificaCentro.action',
				type : 'POST', 
				data : {
					idRegistro : idRegistro,
					idCentro : idCentro,
					departamento : departamento,
					correoCentro : correoCentro
					
				},
				dataType : 'json',
				success  : function(data) {
	 				if (data.codError == '000') {
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalDetalle').modal('hide');
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
					alert('actualizarCatalogo()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('actualizarCatalogo()_'+e);
		}
	 }
	