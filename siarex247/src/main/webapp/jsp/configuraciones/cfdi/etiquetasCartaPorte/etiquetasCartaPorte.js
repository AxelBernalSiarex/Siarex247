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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Configurar Etiquetas Carta Porte', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'configEtiquetasCartaPorte'},
				    		{ extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Configurar Etiquetas Carta Porte", filename: 'configEtiquetasCartaPorte', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/configuracion/etiquetas/cartaPorte/detConfEtiquetasCP.action',
					type: 'POST'
				},
				
				aoColumns : [
					{ mData: null },
					{ mData: "idEtiqueta"},
					//{ mData: "pathXML"},
					{ mData: "descripcion"},
					{ mData: "fechaIni", "sClass": "alinearCentro"},
					{ mData: "fechaFin", "sClass": "alinearCentro"},
					{ mData: "subject"},
					{ mData: "usuarioTrans"},
					{ mData: "fechaTrans", "sClass": "alinearCentro"},
					{ mData: "activo", "sClass": "alinearCentro"},
					{ mData: "valEtiqueta", "sClass": "alinearCentro"},
					{ mData: "version", "sClass": "alinearCentro"}
					
				],
				columnDefs: [
					{	targets: 1,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:abreModal(\'editar\', \'' + row.idRegistro + '\', \'' + row.idEtiqueta + '\', \'' + row.version + '\');">' + row.idEtiqueta + '</a>';
				            return rowElement;
				        }
				    },
				   {
				        targets: 0,
				        render: function (data, type, row) {
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.idRegistro + '\', \'' + row.idEtiqueta + '\', \'' + row.version + '\');">'+BOTON_MODIFICAR_CONFIGURACION+'</a>';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'agregar\', \'' + row.idRegistro + '\', \'' + row.idEtiqueta + '\', \'' + row.version + '\');">'+BOTON_AGREGAR_VALIDACIONES+'</a>';
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



	
	
	function abreModal(opcion, claveRegistro, etiqueta, version) {
		$('#idRegistro_Catalogo').val(claveRegistro);
		switch(opcion) {
		case "agregar" :
			buscaInfoXMLVal(claveRegistro, etiqueta, version);
			$('#modalValidaciones').modal('show');
			break;
		case "editar":
			iniciaFormCatalogo();
			buscaInfoXML(claveRegistro);
			$('#myModalDetalle').modal('show');
			break;
		default :
		}
	}


	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		$("#frmConfXML").find('.has-success').removeClass("has-success");
	    $("#frmConfXML").find('.has-error').removeClass("has-error");
		$('#frmConfXML')[0].reset(); 
		$('#frmConfXML').removeClass("was-validated"); 
		   
	}
	
	

	function buscaInfoXMLVal(claveRegistro, etiqueta, version){
		try{
			$("#modalValidaciones").load('/siarex247/jsp/configuraciones/cfdi/etiquetasCartaPorte/modalValidacionesCP.jsp?etiqueta='+etiqueta+'&version='+version);
			$('#modalValidaciones').modal('show');
		}catch(e){
			alert('buscaInfoXMLVal()_'+e);
		}
		
	}
	
	
	function buscaInfoXML(claveRegistro){
		$.ajax({
			url  : '/siarex247/configuracion/etiquetas/cartaPorte/buscarConfCP.action',
			type : 'POST', 
			data : {
				claveRegistro : claveRegistro
			},
			dataType : 'json',
			success  : function(data) {//console.log('DATA XML: ', data.data[0]);
				if($.isEmptyObject(data)) {
				}
				else {
					$('#etiqueta').val(data.etiqueta);
					$('#fechaInicial').val(data.fechaInicial);
					$('#fechaFinal').val(data.fechaFinal);
					$('#activadaSW').prop('checked', data.activo);
					$('#subject').val(data.subject);
					$('#mensajeError').text(data.mensajeError);
					$('#valEtiquetaSW').prop('checked', data.valEtiqueta);
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				Swal.fire({
				  icon: 'error',
				  title: '¡Error en Operacion!',
				  text: 'Error al ejecutar buscaInfoXML()'
				});
			}
		});	
	}
	

	
	function guardarDatos(){
		try{
			var claveRegistro = $('#idRegistro_Catalogo').val();
			var etiqueta = $('#etiqueta').val();
			var activadaSW = $("#activadaSW").prop('checked');
			var valEtiquetaSW = $("#valEtiquetaSW").prop('checked');
			var fechaInicial = $('#fechaInicial').val();
			var fechaFinal= $('#fechaFinal').val();
			var subject = $('#subject').val();
			var mensajeError = $('#mensajeError').val();
			
	 		$.ajax({
				url  : '/siarex247/configuracion/etiquetas/cartaPorte/actualizaConfiguracion.action',
				type : 'POST', 
				data : {
					claveRegistro : claveRegistro,
					etiqueta : etiqueta,
					activadaSW : activadaSW,
					valEtiquetaSW : valEtiquetaSW,
					fechaInicial : fechaInicial,
					fechaFinal : fechaFinal,
					subject : subject,
					mensajeError : mensajeError
					
				},
				dataType : 'json',
				success  : function(data) {
					if (data.ESTATUS == 'OK'){
	            		Swal.fire({
	              		  icon: 'success',
	            		  title: MSG_OPERACION_EXITOSA_MENU,
	  					  //text: 'La información se guardó exitosamente',
	            		  html: '<p>'+MSG_ALTA_EXITOSA_MENU+'</p>',
						  showCancelButton: false,
						  confirmButtonText: 'Aceptar',
						  denyButtonText: 'Cancelar'
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
						  title: MSG_ERROR_OPERACION_MENU,
						  text: 'Error al guardar la información'
					  });
	            	}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('actualizarCatalogo()_'+thrownError);
				}
			});
	 		
		} catch(e){
			alert('error : '+e);
		}
	}
	
	
	