
var tablaDetalleValidaciones = null;

	$(document).ready(function() {
		try {
			tablaDetalleValidaciones = $('#tablaDetalleValidaciones').DataTable( {
				paging      : false,
				retrieve: true,
				scrollCollapse: true,
			    scrollY: '15em',
				pageLength  : 15,
				lengthChange: false,
				dom         : 'rtipl',
				ordering    : true,
				serverSide	: false,
				fixedHeader : true,
				orderCellsTop: true,
				info		: false,
				select      : false,
				stateSave	: false, 
				order       : [ [ 0, 'asc' ] ],	
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
					url: '/siarex247/configuracion/etiquetas/factura/detalleEtiquetas.action',
					data : {
						etiqueta : function() { return obtenerEtiqueta() },
						version : function() { return obtenerVersion() }    
					},
					type: 'POST'
				},
				aoColumns : [
					{ mData: null},
					{ mData: null, "sClass": "alinearCentro" }
				],
				columnDefs: [
					 {
		                    targets: 0,
		                    render: function (data, type, row) {
		                    	rowElement = '';
		                    	var descripcion = row.desEtiqueta; 
		                    	if (descripcion.length > 60){
		                    		rowElement = '<label class="alinearCentro sorting_1" data-bs-toggle="tooltip" data-bs-placement="top" title="'+descripcion+'" >'+ descripcion.substring(0, 60) +'...</label>';
		                    	}else{
		                    		rowElement = '<label class="alinearCentro sorting_1" data-bs-toggle="tooltip" data-bs-placement="top" title="'+descripcion+'" >'+ descripcion +'</label>';
		                    	}
		                    	
		                        return rowElement;
		                      }
		                  },
					{	targets: 1,
				        render: function (data, type, row) {
				        	// rowElement = '<button class="btn btn-link p-0 ms-2" type="button" data-bs-toggle="tooltip" data-bs-placement="top" title="Eliminar Valor" onclick="eliminaValor(\'' + data.claveRegistro + '\', \'' + data.datoValida + '\');"><span class="text-500 fas fa-trash-alt"></span></button>';
				        	if (data.checked == 'true'){
				        		// rowElement = '<input class="form-check-input" name="chkElement" id="chkVal_'+data.idIdentificador+'" type="checkbox" checked="" value="'+data.claveRegistro+'" onclick="guardarValor(this.id, \'' + data.claveRegistro + '\', \'' + data.datoValida + '\');" />';
				        			rowElement = '<div class="form-check form-switch">';
				        				rowElement+= '<label class="form-check-label d-inline-block me-1" for="label_'+data.idIdentificador+'"  id="label_'+data.idIdentificador+'" >'+LABEL_SI+'</label>';
				        				rowElement+= '<input class="form-check-input" id="chkVal_'+data.idIdentificador+'" name="chkValida" type="checkbox" checked=""  value="'+data.claveRegistro+'" onclick="guardarValor(this.id, \'' + data.claveRegistro + '\', \'' + data.datoValida + '\', \'' + data.idIdentificador + '\');"/>';
				        			rowElement+= '</div>';
								
				        	}else{
				        		// rowElement = '<input class="form-check-input" name="chkElement" id="chkVal_'+data.idIdentificador+'" type="checkbox" value="'+data.claveRegistro+'" onclick="guardarValor(this.id, \'' + data.claveRegistro + '\', \'' + data.datoValida + '\');" />';
			        			rowElement = '<div class="form-check form-switch">';
		        					rowElement+= '<label class="form-check-label d-inline-block me-1" for="label_'+data.idIdentificador+'"  id="label_'+data.idIdentificador+'" >'+LABEL_NO+'</label>';
		        					rowElement+= '<input class="form-check-input" id="chkVal_'+data.idIdentificador+'" name="chkValida" type="checkbox" value="'+data.claveRegistro+'" onclick="guardarValor(this.id, \'' + data.claveRegistro + '\', \'' + data.datoValida + '\', \'' + data.idIdentificador + '\');" />';
		        				rowElement+= '</div>';
				        		
				        	}
				        	
				            return rowElement;
				        }
				    }
				  ]
			});
			
		} catch(e) {
			alert('usuarios()_'+e);
		};
		
		tablaDetalleValidaciones.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 


	// Aqui se agrega los filtros del encabezado
	$('#tablaDetalleValidaciones thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if (i == 1){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#tablaDetalleValidaciones thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatosValidacion($(this).parent().index(), this.value);
	});
		
	function filtraDatosValidacion(columna, texto) {
		tablaDetalleValidaciones
			.column(columna)
	        .search(texto)
	        .draw();
	}
	
 
	function guardarDatosVal(datoValida){
		try{
			var etiqueta = $('#hdnEtiqueta_Validaciones').val();
			var version = $('#hdnVersion_Validaciones').val();
			
			
			if (datoValida == ''){
				 Swal.fire({
					  icon: 'error',
					  title: '¡Error en Operación!',
					  text: 'Debe especificar un valor en campo de captura'
				  });
			}else{
				$.ajax({
					url  : '/siarex247/configuracion/etiquetas/factura/agregarEtiqueta.action',
					type : 'POST', 
					data : {
						etiqueta : etiqueta,
						datoValida : datoValida,
						version : version
						
					},
					dataType : 'json',
					success  : function(data) {
						if (data.ESTATUS == 'OK'){
							// $('#tablaDetalleValidaciones').DataTable().ajax.reload(null,false);
							// $('#txtDatoValidar').val('');
							// $('#txtDatoValidar').focus();
		            	}else if (data.ESTATUS == 'NO_PERMISO'){
		            		Swal.fire({
								  icon: 'error',
								  title: MSG_ERROR_OPERACION_MENU,
								  text: 'Usuario no autorizado.'
							  });
		            	}
		            	else {
		            	  Swal.fire({
							  icon: 'error',
							  title: '¡Error en Operación!',
							  text: 'Error al guardar la información'
						  });
		            	}
					},
					error : function(xhr, ajaxOptions, thrownError) {
						alert('guardarDatosVal()_'+thrownError);
					}
				});
			}
			
			
		}catch(e){
			alert('error : '+e);
		}
	}



	function eliminaValor(claveRegistro, datoValida){
		var etiqueta = $('#hdnEtiqueta_Validaciones').val();
		var version = $('#hdnVersion_Validaciones').val();

		$.ajax({
			url: '/siarex247/configuracion/etiquetas/factura/eliminarRegistro.action',
			data : {
				etiqueta : etiqueta,
				version  : version,
				datoValida : datoValida
				
			},
	 			type : 'POST',
	 			dataType : 'json',
	 			success  : function(data) {		
	 				if (data.ESTATUS == 'OK') {
	 				 //  $('#tablaDetalleValidaciones').DataTable().ajax.reload(null,false);
	 				 //  $('#txtDatoValidar').focus();
	 				} else {
	 					Swal.fire({
	 						icon: 'error',
	 						title: '¡Error en Operacion!',
	 						text: 'Ocurrió un error el eliminar el valor'
				  });
				}
	 			},
	 			error : function(xhr, ajaxOptions, thrownError) {
	 				alert('eliminaValor()_'+e);
	 			}
	 		});
		
	 }
 
	
	function guardarValor(idCheck, claveRegistro, datoValida, idIdentificador){
		try{
			// var
			
			var isCheck = $("#"+idCheck).prop('checked');
			var labekCheck = $("#label_"+idIdentificador);
			if (isCheck){
				labekCheck.html(LABEL_SI);
				guardarDatosVal(datoValida);
			}else{
				labekCheck.html(LABEL_NO);
				eliminaValor(claveRegistro, datoValida);
			}
		}catch(e){
			alert('guardarValor()_'+e);
		}
	}
	
	function obtenerEtiqueta(){
		try{
			return $('#hdnEtiqueta_Validaciones').val();
		}catch(e){
			alert("obtenerEtiqueta()_"+e);
		}
	}
	
	function obtenerVersion(){
		try{
			return $('#hdnVersion_Validaciones').val();
		}catch(e){
			alert("obtenerVersion()_"+e);
		}
	}

	function refrescarEtiqueta(){
		$('#tablaDetalleValidaciones').DataTable().ajax.reload(null,false);	
	}

	
	
