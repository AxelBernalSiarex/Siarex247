
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
					url: '/siarex247/estadisticas/repVerificacion/detalleReporte.action',
					type: 'POST'
				},
				aoColumns : [
					{ mData: null, "sClass": "alinearCentro"},
					{ mData: "idRegistro", "sClass": "alinearCentro"},
					{ mData: "descripcion"},
					{ mData: "validarFactura", "sClass": "alinearCentro"},
					{ mData: "validarComplemento", "sClass": "alinearCentro"},
					{ mData: "validarNota", "sClass": "alinearCentro"},
					{ mData: "fecIni", "sClass": "alinearCentro"},
					{ mData: "fecFin", "sClass": "alinearCentro"},
					{ mData: "desEstatus"},
					{ mData: "totProcesar", "sClass": "alinearDerecha"},
					{ mData: "totProcesados", "sClass": "alinearDerecha"}
					
				],
				columnDefs: [
				   {
				        targets: 0,
				        render: function (data, type, row) {
				        	
				        	 rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
					            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
					            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
					            rowElement += '<a class="dropdown-item" href="javascript:generarReporte(\'' + row.idRegistro + '\');">Exportar</a>';
					            rowElement += '<div class="dropdown-divider"></div>';
					            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaCatalogo(\'' + row.idRegistro + '\', \'' + row.descripcion + '\');">'+BTN_ELIMINAR_MENU+'</a>';
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
			iniciaFormCatalogo();
			$('#myModalDetalle').modal('show');
			break;			
		default :
		}
	}



	

	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		$("#form-Reporte").find('.has-success').removeClass("has-success");
	    $("#form-Reporte").find('.has-error').removeClass("has-error");
		$('#form-Reporte')[0].reset(); 
		$('#form-Reporte').removeClass("was-validated"); 
		   
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
	
	

	function eliminaCatalogo(idBitacora, descripcion){
		try{
			
			Swal.fire({
				  icon : 'question',
				  title: TITLE_DELETE_CATALOGO,
				  text: LABEL_REPORTE_VERIFICACION + ' : '+descripcion,
				  showDenyButton: true,
				  showCancelButton: false,
				  confirmButtonText: BTN_ACEPTAR_MENU,
				  denyButtonText: BTN_CANCELAR_MENU,
				}).then((result) => {
				  if (result.isConfirmed) {
					  $.ajax({
				           url:  '/siarex247/estadisticas/repVerificacion/eliminaReporte.action',
				           type: 'POST',
				            dataType : 'json',
				            data : {
				            	idBitacora : idBitacora
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
		  			                title: '¡Operación No Existosa!',
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
	            var formData = new FormData(document.getElementById("form-Reporte"));
	            formData.append("dato", "valor");

	            $.ajax({
	            	url: '/siarex247/estadisticas/repVerificacion/altaReporte.action',
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
		}catch(e){
			alert('guardarCatalogo()_'+e);
		}
	 }
	
	
	
	function generarReporte(idBitacora){
		 try{
			 
			 	  $.ajax({
						url  : '/siarex247/estadisticas/repVerificacion/generaReporte.action',
						data : {
							idBitacora : idBitacora
						},
						type : 'POST', 
						dataType : 'json',
						success  : function(data) {
							if($.isEmptyObject(data)) {
							   
							} else {
								
								if (data.codError == '000') {
				 					Swal.fire({
				 						  title: MSG_OPERACION_EXITOSA_MENU,
				 						// html: '<p>El archivo excel se genero satisfactoriamente.</p>',
				 						 html: '<p>'+LABEL_REPORTE_VERIFICACION_EXCEL+'</p>',
											  showCancelButton: false,
											  confirmButtonText: BTN_ACEPTAR_MENU,
											  icon: 'success'
				 						}).then((result) => {
				 							abrirExcel(data.mensajeError);
				 						});
								} else {
									Swal.fire({
			  			                title: '¡Operación No Existosa!',
			  			                html: '<p>'+data.mensajeError+'</p>',	
			  			                icon: 'error'
			  			            });
								}
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert('generarReporte()_'+thrownError);
						}
					});	
		 }catch(e){
			 alert('generarExcelVisor()_'+e);
		 }
		 
	 }
	
	

	 function abrirExcel(nombreArchivo){
	 	 try{
	 		 window.open('/siarex247/files/'+nombreArchivo,'_blank');
	 	 }catch(e){
	 		 alert('abrirExcel()_'+e);
	 	 }
	 }
	
	
	