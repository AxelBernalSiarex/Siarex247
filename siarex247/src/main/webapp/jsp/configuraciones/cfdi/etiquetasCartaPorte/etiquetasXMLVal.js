var tablaDetalleValidaciones = null;

	$(document).ready(function() {
		try {
			tablaDetalleValidaciones = $('#tablaDetalleValidaciones').DataTable( {
				paging      : false,
				retrieve: true,
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
					url: '/siarex247/configuracion/etiquetas/cartaPorte/detalleEtiquetas.action',
					data : {
						etiqueta : function() { return obtenerEtiqueta() },
						version : function() { return obtenerVersion() }    
					},
					type: 'POST'
				},
				aoColumns : [
					{ mData: "datoValida", "sClass": "alinearCentro"},
					{ mData: null, "sClass": "alinearCentro" }
				],
				columnDefs: [
					{	targets: 1,
				        render: function (data, type, row) {
				        	  rowElement = '<button class="btn btn-link p-0 ms-2" type="button" data-bs-toggle="tooltip" data-bs-placement="top" title="Eliminar Valor" onclick="eliminaValor(\'' + data.claveRegistro + '\', \'' + data.datoValida + '\');"><span class="text-500 fas fa-trash-alt"></span></button>';
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



	
	function guardarDatosVal(){
		try{
			var etiqueta = $('#hdnEtiqueta_Validaciones').val();
			var datoValida = $('#txtDatoValidar').val();
			var version = $('#hdnVersion_Validaciones').val();
			
			
			if (datoValida == ''){
				 Swal.fire({
					  icon: 'error',
					  title: '¡Error en Operación!',
					  text: 'Debe especificar un valor en campo de captura'
				  });
			}else{
				$.ajax({
					url  : '/siarex247/configuracion/etiquetas/cartaPorte/agregarEtiqueta.action',
					type : 'POST', 
					data : {
						etiqueta : etiqueta,
						datoValida : datoValida,
						version : version
						
					},
					dataType : 'json',
					success  : function(data) {
						if (data.ESTATUS == 'OK'){
							 $('#tablaDetalleValidaciones').DataTable().ajax.reload(null,false);
							 $('#txtDatoValidar').val('');
							 $('#txtDatoValidar').focus();
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



	function eliminaValor(claveRegistro, claveValida){
		Swal.fire({
		  icon: 'question',
		  title: '¿Estás seguro de eliminar el valor?',
		  text: claveValida,
		  showDenyButton: true,
		  showCancelButton: false,
		  confirmButtonText: 'Aceptar',
		  denyButtonText: 'Cancelar',
		}).then((result) => {
		  if (result.isConfirmed) {
			  $.ajax({
				url: '/siarex247/configuracion/etiquetas/cartaPorte/eliminarRegistro.action',
				data : {
					claveRegistro : claveRegistro,
					
				},
  	 			type : 'POST',
  	 			dataType : 'json',
  	 			success  : function(data) {		
  	 				if (data.ESTATUS == 'OK') {
  	 				   $('#tablaDetalleValidaciones').DataTable().ajax.reload(null,false);
					}
  	 				else {
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
		});
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
	
