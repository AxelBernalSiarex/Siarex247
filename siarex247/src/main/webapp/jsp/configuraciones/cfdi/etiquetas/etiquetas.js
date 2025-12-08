


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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Configurar Etiquetas XML', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'configEtiquetas'},
				    		{ extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Configurar Etiquetas XML", filename: 'configEtiquetas', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/configuracion/etiquetas/factura/detalleConf.action',
					type: 'POST'
				},
				
				aoColumns : [
					{ mData: "etiqueta"},
					{ mData: "desTipo"},
					{ mData: "fechaIni", "sClass": "alinearCentro"},
					{ mData: "fechaFin", "sClass": "alinearCentro"},
					{ mData: "activo", "sClass": "alinearCentro"},
					{ mData: "subject"},
					{ mData: "version", "sClass": "alinearCentro"},
					{ mData: "datoValida"},
					{ mData: "mensaje" },
					// { mData: "usuarioTrans"},
					// { mData: "fechaTrans"},
					
					{ mData: null , "sClass": "alinearCentro"}
				],
				columnDefs: [
					{	targets: 0,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:abreModal(\'editar\', \'' + row.claveRegistro + '\', \'' + row.etiqueta + '\', \'' + row.version + '\', \'' + row.tipo + '\');">' + row.etiqueta + '</a>';
				            return rowElement;
				        }
				    },
				   {
				        targets: 9,
				        render: function (data, type, row) {
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.claveRegistro + '\', \'' + row.etiqueta + '\', \'' + row.version + '\', \'' + row.tipo + '\');">'+BOTON_MODIFICAR_CONFIGURACION+'</a>';
				            
				            rowElement += '</div>';
				            rowElement += '</div>';
				                return rowElement;
				          }
				      },
				      {	targets: 7,
					    	visible: false,
					    	searchable: false,
					    	render: function (data, type, row) {
					        	rowElement = '<td > '+row.datoValida+'</td>';
					            return rowElement;
					        }
					    },
					      {	targets: 8,
						    	visible: false,
						    	searchable: false,
						    	render: function (data, type, row) {
						        	rowElement = '<td > '+row.mensaje+'</td>';
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
		 if (i == 9){
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


	function abreModal(opcion, claveRegistro, etiqueta, version, tipo) {
		$('#idRegistro_Catalogo').val(claveRegistro);
		switch(opcion) {
		case "agregar" :
			buscaInfoXMLVal(claveRegistro, etiqueta, version);
			break;
		case "editar":
			iniciaFormCatalogo();
			buscaInfoXML(claveRegistro);
			if (tipo == 'E'){
				buscaInfoXMLVal(etiqueta, version)	
			}else{
				$("#modalValidaciones").load('/siarex247/html/blanco.html');
			}
			
			$('#myModalDetalle').modal('show');
			break;
		default :
		}
	}



	function iniciaFormCatalogo(){
		$("#tabInfoPrincipal").attr('aria-selected', 'true');
		$("#tabInfoPrincipal").addClass("active");
		$("#infoPrincipal").addClass("active");
		
		$("#accordion1 #heading1 accordion-button").removeClass("collapsed");
		$("#accordion1 #heading1  accordion-button").attr('aria-expanded', 'true');
		$("#accordion1 #collapse1").addClass("show");
		
		$("#accordion1 #heading2 accordion-button").addClass("collapsed");
		$("#accordion1 #heading2 accordion-button").attr('aria-expanded', 'false');
		$("#accordion1 #collapse2").removeClass("show");
		
		/* Reset al Formulario */ 
		$("#frmConfXML").find('.has-success').removeClass("has-success");
	    $("#frmConfXML").find('.has-error').removeClass("has-error");
		$('#frmConfXML')[0].reset(); 
		$('#frmConfXML').removeClass("was-validated"); 
		   
	}
	
	
	function buscaInfoXMLVal(etiqueta, version){
		try{

			// $('#txtDatoValidar').focus();
			// 
			// document.getElementById("modal-title-catalogo-validaciones").innerHTML = "Agregar Valores a la Etiqueta \""+etiqueta+"\"";
			// $('#hdnEtiqueta_Validaciones').val(etiqueta);
			// $('#hdnVersion_Validaciones').val(version);
			// $('#tablaDetalleValidaciones').DataTable().ajax.url('/siarex247/configuracion/etiquetas/factura/detalleEtiquetas.action?etiqueta='+etiqueta+'&version='+version).load();
			$("#modalValidaciones").load('/siarex247/jsp/configuraciones/cfdi/etiquetas/modalValidaciones.jsp?etiqueta='+etiqueta+'&version='+version);
			// $('#modalValidaciones').modal('show');
		}catch(e){
			alert('buscaInfoXMLVal()_'+e);
		}
		
	}

	
	function buscaInfoXML(claveRegistro){
		$.ajax({
			url  : '/siarex247/configuracion/etiquetas/factura/buscarConf.action',
			type : 'POST', 
			data : {
				claveRegistro : claveRegistro
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				}
				else {
					$('#etiqueta').val(data.etiqueta);
					$('#fechaInicial').val(data.fechaInicial);
					$('#fechaFinal').val(data.fechaFinal);
					$("#activadaSW").prop('checked', data.activo);
					if (data.activo){
						 $("#activadaSWLbl").html("SI");
					}else{
						 $("#activadaSWLbl").html("NO");
					}
					$('#subject').val(data.subject);
					$('#mensajeError').text(data.mensajeError);
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('buscaInfoXML()_'+thrownError);
			}
		});	
	}
	

	
	function guardarDatos(){
		try{
			var claveRegistro = $('#idRegistro_Catalogo').val();
			var etiqueta = $('#etiqueta').val();
			var activadaSW = $("#activadaSW").prop('checked');
			var fechaInicial = $('#fechaInicial').val();
			var fechaFinal= $('#fechaFinal').val();
			var subject = $('#subject').val();
			var mensajeError = $('#mensajeError').val();
			
	 		$.ajax({
				url  : '/siarex247/configuracion/etiquetas/factura/actualizaConfiguracion.action',
				type : 'POST', 
				data : {
					claveRegistro : claveRegistro,
					etiqueta : etiqueta,
					activadaSW : activadaSW,
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
	  					 // text: 'La información se guardó exitosamente',
	            		  html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
						  showCancelButton: false,
						  confirmButtonText: 'Aceptar',
						  denyButtonText: 'Cancelar'
	 					}).then((result) => {
 						  if (result.isConfirmed) {
 							 $('#myModalDetalle').modal('hide');
 							 $('#tablaDetalle').DataTable().ajax.reload(null,false);
 						  }
 	 					});
					}else if (data.ESTATUS == 'NO_PERMISO'){
						Swal.fire({
							  icon: 'error',
							  title: MSG_ERROR_OPERACION_MENU,
							  text: 'Usuario no autorizado.'
						  });
					
	            	} else {
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
	