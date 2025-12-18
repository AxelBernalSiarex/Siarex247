

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
			      	{ 	extend: "collection",
				    	autoClose : true,  
				    	text:'<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
				    	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    	buttons: [
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Descartar Ordenes de Compra', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'DescartarOrdenes'},
				    		{ extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Descartar Ordenes de Compra", filename: 'DescartarOrdenes', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/configuracion/descartarOrdenes/detalleDescartes.action',
					type: 'POST'
				},
				aoColumns : [
					{ mData: "RAZON_SOCIAL"},
					{ mData: "FOLIO_EMPRESA", "sClass": "alinearCentro"},
					{ mData: "SERIE_FOLIO"},
					{ mData: "ESTATUS_ORDEN"},
					{ mData: "TOTAL", "sClass": "alinearDerecha"},
					{ mData: "SUB-TOTAL", "sClass": "alinearDerecha"},
					{ mData: "ESTATUS_REGISTRO", "sClass": "alinearCentro"},
					{ mData: null, "sClass": "alinearCentro"}
				],
				columnDefs: [
				   {
				        targets: 7,
				        render: function (data, type, row) {
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
				            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaCatalogo(\'' + row.FOLIO_EMPRESA + '\');">Eliminar</a>';
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
		 if (i == 7){
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
		case "carga" : 
			iniciaFormCatalogo();
			$('#myModalDetalle').modal('show');
			break;			
		  default :
		}
	}



	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		$("#frmCargaMasiva").find('.has-success').removeClass("has-success");
	    $("#frmCargaMasiva").find('.has-error').removeClass("has-error");
		$('#frmCargaMasiva')[0].reset(); 
		$('#frmCargaMasiva').removeClass("was-validated"); 
		   
	}
	

	function eliminaCatalogo(folioEmpresa){
		try{
			
			Swal.fire({
				  icon : 'question',
				  title: TITLE_DELETE_CATALOGO,
				  text: LABEL_DESCARTAR +' : '+folioEmpresa,
				  showDenyButton: true,
				  showCancelButton: false,
				  confirmButtonText: BTN_ACEPTAR_MENU,
				  denyButtonText: BTN_CANCELAR_MENU,
				}).then((result) => {
				  if (result.isConfirmed) {
					  $.ajax({
				           url:  '/siarex247/configuracion/descartarOrdenes/eliminarRegistro.action',
				           type: 'POST',
				            dataType : 'json',
				            data : {
				            	folioEmpresa : folioEmpresa
				            },
						    success: function(data){
						    	if (data.codError == "000"){
						    		Swal.fire({
			 						  title: MSG_OPERACION_EXITOSA_MENU,
			 						   html: '<p>'+MSG_REGISTRO_ELIMINADO_MENU+' </p>',
										  showCancelButton: false,
										  confirmButtonText: 'Aceptar',
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
	
	function cargaMasiva(){
				try{
			            var formData = new FormData(document.getElementById("frmCargaMasiva"));
			            formData.append("dato", "valor");

			            $.ajax({
			            	url: '/siarex247/configuracion/descartarOrdenes/iniciaCargaTXT.action',
			                dataType: "json",
			                type: "post",
			                data: formData,
			                cache: false,
			                contentType: false,
				    		 processData: false
			            }).done(function(data){//console.log('DATA AGREGA: ', data);
				            if (data.ESTATUS == 'OK') {
			 					Swal.fire({
			 						  icon: 'success',
			 						  title: MSG_OPERACION_EXITOSA_MENU,
			 						 html: '<p>'+MSG_ALTA_EXITOSA_MENU+'</p>',
			 						  showCancelButton: false,
			 						  confirmButtonText: 'Aceptar',
			 						  denyButtonText: 'Cancelar',
			 						}).then((result) => {
			 						  if (result.isConfirmed) {
			 							 $('#myModalDetalle').modal('hide');
			  	 						 $('#tablaDetalle').DataTable().ajax.reload(null,false);
			 						  }
			 					});
							}
				            else if (data.ESTATUS == 'NO_XML') {
								  Swal.fire({
									  icon: 'error',
									  title: '¡Error en Operacion!',
									  text: 'El archivo seleccionado no es un XML'
								  });
							}
			 				else {
							  Swal.fire({
								  icon: 'error',
								  title: '¡Error en Operacion!',
								  text: 'Error en Carga'
							  });
							}
			            });
				}
				catch(e){
					e = null;
				}
	}
	