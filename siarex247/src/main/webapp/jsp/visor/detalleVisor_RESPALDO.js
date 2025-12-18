

var tablaDetalleVisor = null;

	$(document).ready(function() {
		try {
			tablaDetalleVisor = $('#tablaDetalleVisor').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 50,
				lengthChange: false,
//				dom: 'Blfrtip',
//				dom: "<'row mx-0'<'col-md-12'QB><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				dom: "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				ordering    : true,
				serverSide	: true,
				fixedHeader : true,
				orderCellsTop: true,
				info		: true,
				select      : true,
				stateSave	: false, 
				order       : [ [ 0, 'asc' ] ],	
				buttons: [
	                { 	// text: '<div id="btnNuevo_Visor">  <span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Nuevo</span> </div>',
	                	text: '<div id="btnNuevo_Visor"> </div>',
	                    className: 'btn btn-primary me-1 mb-1 btn-sm btnPanel validarNuevo',
	                    action: function ( e, dt, node, config ) {
	                    	abreModal('nuevo', 0);
	                    },
	                },
	                /*
	                { 	text: '<div id="btnFilter_Visor">  <span class="fas fa-filter" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Filtro</span> </div>',
	                	// text: '<div id="btnNuevo_Visor"> </div>',
	                    className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
	                    action: function ( e, dt, node, config ) {
	                    	//abreModal('nuevo', 0);
	                    },
	                },
					*/
	                { 	text: '<div id="btnFilter_Visor">  <span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1"> <a class="me-1 mb-1 btn-sm btnClr" href="#filtrosBusquedaVisor" data-bs-toggle="collapse" aria-expanded="false" aria-controls="filtrosBusquedaVisor"> Filtros </a>       </span> </div>',
	                	// text: '<div id="btnNuevo_Visor"> </div>',
	                    className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
	                    action: function ( e, dt, node, config ) {
	                    	//abreModal('nuevo', 0);
	                    },
	                },
	                { 	
	                	text:'<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
	                	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
	                    action: function ( e, dt, node, config ) {
	                    	exportarDetalleExcel();
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
				   	},
				   	select: {
	                    rows: "" 
	                }
				},
				ajax : {
					url: '/siarex247/visor/tablero/detalleOrdenes.action',
					data : {
						
						tipoMoneda : function() { return obtenerTipoMoneda() },
						estatusOrden : function() { return obtenerEstatusOrden() },
						rfc : function() { return obtenerRfc() },
						razonSocial : function() { return obtenerRazonSocial() },
						uuid : function() { return obtenerUuid() },
						folioEmpresa : function() { return obtenerFolioEmpresa() },
						serieFolio : function() { return obtenerSerieFolio() },
						fechaInicial: function() { return obtenerFechaIni(); },
						fechaFinal: function() { return obtenerFechaFin(); }
						
					},
					beforeSend: function( xhr ) {
	        			$('#overSeccion').css({display:'block'});
	        		},
					complete: function(jqXHR, textStatus){
	    				 $('#overSeccion').css({display:'none'});			
		    		},
					type: 'POST'
				},
				aoColumns : [
					{ mData: null,"sClass": "alinearCentro"},
					{ mData: "razonSocial"},
					{ mData: "folioEmpresa","sClass": "alinearCentro"},
					{ mData: null},
					{ mData: "tipoMoneda","sClass": "alinearCentro"},
					{ mData: "monto","sClass": "alinearDerecha"},
					{ mData: "servicioRecibido","sClass": "alinearCentro"},
					{ mData: "desEstatus"},
					{ mData: "serieFolio","sClass": "alinearCentro"},
					{ mData: "total","sClass": "alinearDerecha"},
					{ mData: "subTotal","sClass": "alinearDerecha"},
					{ mData: "iva","sClass": "alinearDerecha"},
					{ mData: "ivaRet","sClass": "alinearDerecha"},
					{ mData: "isrRet","sClass": "alinearDerecha"},
					{ mData: "impLocales" ,"sClass": "alinearDerecha"},
					{ mData: null ,"sClass": "alinearCentro"},
					{ mData: null ,"sClass": "alinearCentro"},
					{ mData: null ,"sClass": "alinearCentro"},
					//{ mData: null ,"sClass": "alinearCentro"},
					//{ mData: null ,"sClass": "alinearCentro"},
					{ mData: null ,"sClass": "alinearCentro"},
					{ mData: null ,"sClass": "alinearCentro"},
					{ mData: null ,"sClass": "alinearCentro"},
					{ mData: "totalNC" ,"sClass": "alinearDerecha"},
					{ mData: "pagoTotal" ,"sClass": "alinearDerecha"},
					{ mData: "ivaRetNC" ,"sClass": "alinearDerecha"},
					{ mData: "fechaPago" ,"sClass": "alinearCentro"},
					{ mData: "asignarA"},
					{ mData: "fechaUltimoMovimiento" ,"sClass": "alinearCentro"},
					{ mData: "estadoCFDI"},
					{ mData: "estatusCFDI"},
					{ mData: "usoCFDI"},
					{ mData: "claveProducto"}
					
				],
				columnDefs: [
	            	 {
	                     targets: 0,
	                     render: function (data, type, row) {
	                     	rowElement = '';
	                     	    rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
	                             rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
	                             rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
	                             rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'ver\', \'' + row.folioOrden + '\');">'+BTN_VER_MENU+'</a>';
	                             if (!row.proveedor && !row.soloConsulta){
	                            	 rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.folioOrden + '\');">'+BTN_EDITAR_MENU+'</a>';	 
		                             rowElement += '<div class="dropdown-divider"></div>';
		                             rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminarOrdenCompra(\'' + row.folioOrden + '\', \'' + row.folioEmpresa + '\');">'+BTN_ELIMINAR_MENU+'</a>';
	                             }
	                             rowElement += '</div>';
	                             rowElement += '</div>';
	                     	
	                        return rowElement;
	                       }
	                   },
	                   {	targets: 2,
					        render: function (data, type, row) {
					        	rowElement = '<a href="javascript:verDocumento(\'PDF_ORDEN\',\'' + row.folioOrden + '\');">' + row.folioEmpresa + '</a>';
					            return rowElement;
					        }
					    },
	                {
	                    targets: 3,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	var descripcion = row.descripcion; 
	                    	if (descripcion.length > 100){
	                    		rowElement = '<label class="alinearCentro sorting_1" data-bs-toggle="tooltip" data-bs-placement="top" title="'+descripcion+'" >'+ descripcion.substring(0, 100) +'</label>';
	                    	}else{
	                    		rowElement = '<label class="alinearCentro sorting_1" data-bs-toggle="tooltip" data-bs-placement="top" title="'+descripcion+'" >'+ descripcion +'</label>';
	                    	}
	                    	
	                        return rowElement;
	                      }
	                  },
	                  {
	                      targets: 15,
	                      render: function (data, type, row) {
	                      	rowElement = '';
	                      	if (row.tieneXML == 'S'){
	                      		rowElement = '<img class="" src="/theme-falcon/images/app/xml26.png" alt="" style="cursor: pointer;" title="Archivo XML" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo XML" onclick="verDocumento(\'XML_FACTURA\',\'' + row.folioOrden + '\');" />&nbsp;&nbsp;';
	                      	}
	                          
	                          return rowElement;
	                        }
	                    },
	                  {
	                    targets: 16,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	if (row.tienePDF == 'S'){
	                    		rowElement = '<img class="" src="/theme-falcon/images/app/pdf26.png" alt="" style="cursor: pointer;" title="Ver Link_Chart" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="verDocumento(\'PDF_FACTURA\',\'' + row.folioOrden + '\');" />&nbsp;&nbsp;';
	                    	}
	                    	return rowElement;
	                      }
	                  },
	                  {
	                    targets: 17,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	if (row.omitirComplemento == 'N'){
	                    		if (row.tieneComplementoXML == 'S'){
	                        		rowElement = '<img class="" src="/theme-falcon/images/app/xml26.png" alt="" style="cursor: pointer;" title="Ver Link_Chart" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo XML" onclick="verDocumento(\'XML_COMPLEMENTO\',\'' + row.folioOrden + '\');" />&nbsp;&nbsp;';
	                        	}
	                    	}else{
	                    		rowElement = '<label class="text-red">OMITIDO</label>';
	                    	}
	                    	
	                        
	                        return rowElement;
	                      }
	                  },
	                  {
	                    targets: 18,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	if (row.omitirComplemento == 'N'){
	                    		if (row.tieneComplementoPDF == 'S'){
	                        		rowElement = '<img class="" src="/theme-falcon/images/app/pdf26.png" alt="" style="cursor: pointer;" title="Ver Link_Chart" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="verDocumento(\'PDF_COMPLEMENTO\',\'' + row.folioOrden + '\');" />&nbsp;&nbsp;';
	                        	}
	                            
	                    	}else{
	                    		rowElement = '<label class="text-red">OMITIDO</label>';
	                    	}
	                    	
	                        return rowElement;
	                      }
	                  },
	                  /*
	                  {
	                    targets: 19,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	if (row.tieneCartaPorteXML == 'S'){
	                    		rowElement = '<img class="" src="/theme-falcon/images/app/xml26.png" alt="" style="cursor: pointer;" title="Ver Link_Chart" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo XML" onclick="verDocumento(\'XML_CARTAPORTE\',\'' + row.folioOrden + '\');" />&nbsp;&nbsp;';
	                    	}
	                        return rowElement;
	                      }
	                  },
	                  {
	                    targets: 20,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	if (row.tieneCartaPortePDF == 'S'){
	                    		rowElement = '<img class="" src="/theme-falcon/images/app/pdf26.png" alt="" style="cursor: pointer;" title="Ver Link_Chart" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="verDocumento(\'PDF_CARTAPORTE\',\'' + row.folioOrden + '\');" />&nbsp;&nbsp;';
	                    	}
	                        return rowElement;
	                      }
	                  },
	                  */
	                  {
	                      targets: 19,
	                      render: function (data, type, row) {
	                      	rowElement = '';
	                      	if (row.tieneNotaCreditoXML == 'S'){
	                      		rowElement = '<img class="" src="/theme-falcon/images/app/xml26.png" alt="" style="cursor: pointer;" title="Ver Link_Chart" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo XML" onclick="verDocumento(\'XML_NOTA\',\'' + row.folioOrden + '\');" />&nbsp;&nbsp;';
	                      	}
	                          return rowElement;
	                        }
	                    },
	                    {
	                        targets: 20,
	                        render: function (data, type, row) {
	                        	rowElement = '';
	                        	if (row.tieneNotaCreditoPDF == 'S'){
	                        		rowElement = '<img class="" src="/theme-falcon/images/app/pdf26.png" alt="" style="cursor: pointer;" title="Ver Link_Chart" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="verDocumento(\'PDF_NOTA\',\'' + row.folioOrden + '\');" />&nbsp;&nbsp;';
	                        	}
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
		
		tablaDetalleVisor.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	// Aqui se agrega los filtros del encabezado
	$('#tablaDetalleVisor thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if (i == 0 || (i >= 15 && i <= 22 ) ){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#tablaDetalleVisor thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatosVisor($(this).parent().index(), this.value);
	});
		
	function filtraDatosVisor(columna, texto) {
		tablaDetalleVisor
			.column(columna)
	        .search(texto)
	        .draw();
	}




	function abreModal(opcion, id) {
		$( "#btnSometer" ).show();
		switch(opcion) {
		case "nuevo" : 
			$('#folioOrden').val(0);
			$('#divEliminar').hide();
			$('#divPagada').hide();
			$( "#omitirComplemento" ).hide();
			$('#folioEmpresa').attr('readonly', true);
			$("#facturaPagada").prop('disabled', false);
			$("#serRecibido").prop('disabled', false);
			
			$('#folioEmpresa').attr('readonly', false);
			$('#monto').attr('readonly', false);
			$("#tipoMoneda").prop( "disabled", false );
			$('#descripcion').attr('readonly', false);
			$("#claveProveedor").prop( "disabled", false);
			
			
			cargaTipoMoneda('MXN');
			cargaEmpleados(0);
			cargaCentros('');
			cargaProveedores(0);
			iniciaFormCatalogo();
			document.getElementById("modal-title-catalogo").innerHTML = MODAL_TITLE_NUEVO;
			$('#myModalDetalle').modal('show');
			break;			
		case "editar" :
			$('#folioOrden').val(id);
			document.getElementById("modal-title-catalogo").innerHTML = MODAL_TITLE_EDITAR;
			iniciaFormCatalogo();
			buscaCatalogo(id, 'editar');
			break;
		case "ver" :
			$('#folioOrden').val(id);
			document.getElementById("modal-title-catalogo").innerHTML = MODAL_TITLE_VER;
			iniciaFormCatalogo();
			buscaCatalogo(id, 'ver');
			break;
		case "cargarFactura" :
			
			$('#btnGuardar_CargarFactura').hide();
			$('#btnSometerIndividual').prop('disabled', false);
			$('#btnSometerMultiple').prop('disabled', false);
			iniciaFormCargarFacturas();
			iniciaFormCargarFacturasMultiple();
			$('#myModalCargarFactura').modal('show');
			
			
			break;	
		case "cargarFacturaAmericana" :
			
			$('#btnSometer_AmericanosCaptura_Cargar').hide();
			iniciaFormCargarFacturasAmericanasArchivo();
			iniciaFormCargarFacturasAmericanasCaptura();
			$('#myModalCargarFacturaAmericanos').modal('show');
			
			
			break;		
		case "cargarComplemento" :
			iniciaFormComplementos();
			$('#exito_Complemento').hide();
			$('#error_Complemento').hide();
			
			
			$('#btnGuardar_CargarComplemento').prop('disabled', true);
			$('#uuidXML_Complemento').val('XXXXXX1234aa5');
			$('#tablaDetalleCargarComplemento').DataTable().ajax.reload(null,false);
			$('#myModalCargarComplemento').modal('show');
			break;	
		case "cargarNota" :
			iniciaFormNota();
			$('#error_NotaCredito').hide();
			$('#exito_NotaCredito').hide();
			$('#btnGuardar_CargarNota').prop('disabled', true);
			$('#uuidXML_NotaCredito').val('XXXXXX12345');
			$('#tablaDetalleCargarNota').DataTable().ajax.reload(null,false);
			$('#myModalCargarNota').modal('show');
			break;		
			
		default :
		}
	}

	

	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		$('#tipoMoneda').select2({
			theme: "bootstrap-5",
			width: $( this ).data( 'width' ) ? $( this ).data( 'width' ) : $( this ).hasClass( 'w-100' ) ? '100%' : 'style',
			placeholder: $( this ).data( 'placeholder' ),
			dropdownParent: $('#myModalDetalle')
		});

		$('#cmbEmpleado').select2({
			theme: "bootstrap-5",
			width: $( this ).data( 'width' ) ? $( this ).data( 'width' ) : $( this ).hasClass( 'w-100' ) ? '100%' : 'style',
			placeholder: $( this ).data( 'placeholder' ),
			dropdownParent: $('#myModalDetalle')
		});

		$('#cmbCentroCosto').select2({
			theme: "bootstrap-5",
			width: $( this ).data( 'width' ) ? $( this ).data( 'width' ) : $( this ).hasClass( 'w-100' ) ? '100%' : 'style',
			placeholder: $( this ).data( 'placeholder' ),
			dropdownParent: $('#myModalDetalle')
		});

		$('#claveProveedor').select2({
			theme: "bootstrap-5",
			width: $( this ).data( 'width' ) ? $( this ).data( 'width' ) : $( this ).hasClass( 'w-100' ) ? '100%' : 'style',
			placeholder: $( this ).data( 'placeholder' ),
			dropdownParent: $('#myModalDetalle')
		});
		
		$("#tipoMoneda").on("change", function () {
			$(this).trigger("blur");
		});

		 $("#cmbEmpleado").on("change", function () {
		 	$(this).trigger("blur");
		 });

		 $("#cmbCentroCosto").on("change", function () {
		 	$(this).trigger("blur");
		 });

		 $("#claveProveedor").on("change", function () {
		 	$(this).trigger("blur");
		 });
		 
		$("#frmNuevaOrden").find('.has-success').removeClass("has-success");
	    $("#frmNuevaOrden").find('.has-error').removeClass("has-error");
		$('#frmNuevaOrden')[0].reset(); 
		$('#frmNuevaOrden').removeClass("was-validated"); 
		   
	}
	
	
	
	function iniciaFormCargarFacturas(){
		$('#mensajeError_Individual_Cargar').addClass('noColor');
		$("#frmCargarFactura").find('.has-success').removeClass("has-success");
	    $("#frmCargarFactura").find('.has-error').removeClass("has-error");
		$('#frmCargarFactura')[0].reset(); 
		$('#frmCargarFactura').removeClass("was-validated");
		
	}
	
	function iniciaFormCargarFacturasMultiple(){
		
		$('#valError_Individual_Cargar').hide();
		$('#valExito_Individual_Cargar').hide();
		
		$('#valError_Multiple_Cargar').hide();
		$('#valExito_Multiple_Cargar').hide();
		
		
		$("#frmCargarFacturaMultiple").find('.has-success').removeClass("has-success");
	    $("#frmCargarFacturaMultiple").find('.has-error').removeClass("has-error");
		$('#frmCargarFacturaMultiple')[0].reset(); 
		$('#frmCargarFacturaMultiple').removeClass("was-validated");
		
	}
	
	
	
	function eliminarOrdenCompra(folioOrden, folioEmpresa){
		
		try{
			
				Swal.fire({
					  icon : 'question',
					  title: QUESTION_ELIMINAR_ORDEN,
					  text: VISOR_LABEL_ORDEN_COMPRA + ' : '+folioEmpresa,
					  showDenyButton: true,
					  showCancelButton: false,
					  confirmButtonText: BTN_ACEPTAR_MENU,
					  denyButtonText: BTN_CANCELAR_MENU,
					}).then((result) => {
					  if (result.isConfirmed) {
						  var foliosEliminar = folioOrden + ";";
						  $.ajax({
							   url:  '/siarex247/visor/tablero/eliminaOrdenes.action',
					           type: 'POST',
					            dataType : 'json',
					            data : {
					            	foliosEliminar : foliosEliminar
					            },
							    success: function(data){
							    	
							    	if (data.codError == "000"){
							    		Swal.fire({
				 						  title: MSG_OPERACION_EXITOSA_MENU,
				 						 // html: '<p> La orden de compra se ha eliminado satisfactoriamente. </p>',
				 						 html: '<p> '+VISOR_MSG1_ELIMINAR+' </p>',
											  showCancelButton: false,
											  confirmButtonText: BTN_ACEPTAR_MENU,
											  icon: 'success'
				 						}).then((result) => {
				 							$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
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
			alert('exportarCertificados()_'+e);
		}
	}
	

	function buscaCatalogo(folioOrden, accion){
		$.ajax({
			url  : '/siarex247/visor/tablero/consultarOrden.action',
			type : 'POST', 
			data : {
				folioOrden : folioOrden
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					if ( (data.estatusOrden == 'A9' || data.estatusOrden == 'A10' || data.estatusOrden == 'A12') && accion == 'editar'){
						Swal.fire({
  			                title: MSG_ERROR_OPERACION_MENU,
  			                // html: '<p>Estatus de la Orden es incorrecto para modificar</p>',	
  			                html: '<p>'+VISOR_MSG2+'</p>',
  			                icon: 'info'
  			            });
						
					}else{
						
						$("#ESTATUS_ORDEN_NUEVA").val(data.estatusOrden);
						$( "#omitirComplemento" ).hide();
						$('#folioEmpresa').attr('readonly', true);
						$("#facturaPagada").prop('disabled', false);
						$("#serRecibido").prop('disabled', false);
						
						
						$('#folioEmpresa').val(data.folioEmpresa);
						$('#descripcion').val(data.descripcion);
						cargaTipoMoneda(data.tipoMoneda);
						$('#monto').val(data.monto);
						if (data.servicioRecibido == '1'){
							$("#serRecibido").prop('checked', true);	
						}else {
							$("#serRecibido").prop('checked', false);
						}
						
						$('#fechaPago').val(data.fechaPago);
						
						$('#numeroCuenta').val(data.numeroCuentaProveedor);
						$('#centroCostosProveedor').val(data.centroCostosProveedor);
						cargaEmpleados(data.asignarA);
						cargaCentros(data.asignarA);
						cargaProveedores(data.claveProveedor);
						

						if (data.estatusOrden == 'A1' || data.estatusOrden == 'A3' || data.estatusOrden == 'A4'){
							if (data.estatusOrden == 'A3' || data.estatusOrden == 'A4'){
								$('#divPagada').show();	
							}else{
								$('#divPagada').hide();
							}
							$('#divEliminar').show();
							
						}else if (data.estatusOrden == 'A5' || data.estatusOrden == 'A2'){
							$('#divPagada').hide();
							$('#divEliminar').hide();
						}
						
						
						if (accion == 'ver'){
							$( "#btnSometer" ).hide();
						}else{
							$( "#btnSometer" ).show();
						}
						
						
						if (data.estatusOrden == 'A3' || data.estatusOrden == 'A4' || data.estatusOrden == 'A1'){
							$('#monto').attr('readonly', true);
							$("#tipoMoneda").prop( "disabled", true );
							$('#descripcion').attr('readonly', true);
							$("#claveProveedor").prop( "disabled", true );
							
							if (data.estatusOrden == 'A4'){
								$("#facturaPagada").prop('checked', true);
								$("#facturaPagada").prop('disabled', true);
								$("#serRecibido").prop('disabled', true);
								$("#eliminarFactura").prop('disabled', true);
								$("#envioCorreo").prop('disabled', true);
								$("#fechaPago").prop('disabled', true);
								$( "#omitirComplemento" ).show();
							}else{
								$("#eliminarFactura").prop('disabled', false);
								$("#envioCorreo").prop('disabled', false);
							}
							
						}else{
							$('#monto').attr('readonly', false);
							$("#tipoMoneda").prop( "disabled", false );
							$('#descripcion').attr('readonly', false);
							$("#claveProveedor").prop( "disabled", false);
						}
						
						$("#tipoOrden").val(data.tipoOrden);
						
						if (data.proveedor || data.soloConsulta){
							 const frmNuevaOrden = document.getElementById("frmNuevaOrden");
						     const validarGuardar = frmNuevaOrden.getElementsByClassName("validarGuardar"); // a list of matching elements, *not* the element itself
						     for(i=0;i<validarGuardar.length;i++){
						    	 var idElemento = validarGuardar[i].id;
						    	 $('#'+idElemento).attr('disabled', true);
						     	
						     }
							
						}
						
						$('#myModalDetalle').modal('show');
					}
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('buscaCatalogo()_'+thrownError);
			}
		});	
		
    }
	
	
	function validarObjetos(){
		try{
			var bandServicio = $("#serRecibido").prop('checked');
			var folioOrden =  $('#folioOrden').val();
			
			$.ajax({
				url  : '/siarex247/visor/tablero/consultarOrden.action',
				type : 'POST', 
				data : {
					folioOrden : folioOrden
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						if ( data.estatusOrden == 'A3' || data.estatusOrden == 'A4'){
							if (bandServicio){
								$("#facturaPagada").prop('disabled', false);
								$("#fechaPago").prop('disabled', false);
								$("#envioCorreo").prop('disabled', false);
								
							}else{
								$("#facturaPagada").prop('checked', false);
								$("#facturaPagada").prop('disabled', true);
								
								$("#fechaPago").prop('disabled', true);
								$("#envioCorreo").prop('checked', false);
								$("#envioCorreo").prop('disabled', true);
							}
							
						}else{
							
							
						}
						
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('buscaCatalogo()_'+thrownError);
				}
			});	
		}catch(e){
			alert('validarObjetos()'+e);
		}
	}
	
	
	function guardarOrden(){
		try{
			var formData = new FormData(document.getElementById("frmNuevaOrden"));
	            $.ajax({
	            	url: '/siarex247/visor/tablero/nuevaOrden.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	if (data.codError == '000') {
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						  html: '<p> '+ MSG_ALTA_EXITOSA_MENU +' </p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							 $('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
	 						});
					} else {
						Swal.fire({
  			                title: MSG_ERROR_OPERACION_MENU,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
	             });
		}
		catch(e){
			alert('guardarCatalogo()_'+e);
		}
    }
	
	
	function actualizarOrden(){
		try{
			var tipoOrden = $("#tipoOrden").val();
			var estatusOrden = $("#ESTATUS_ORDEN_NUEVA").val();
			if (tipoOrden != ''){
				Swal.fire({
					  icon : 'question',
					 // title: '¿ Orden Multiple ?',
					 // text: 'Esta orden de compra esta asignada a una factura múltiple ¿ Desea realizar los cambios a todas las órdenes de compra asignadas a esta factura ?',
					  title: VISOR_TITLE16,
					  text: VISOR_MSG3,
					  
					  showDenyButton: true,
					  showCancelButton: false,
					  confirmButtonText: BTN_ACEPTAR_MENU,
					  denyButtonText: BTN_CANCELAR_MENU,
					}).then((result) => {
					  if (result.isConfirmed) {
						  if (estatusOrden == 'A4'){
							  procedeModificarOmitirComplemento();
						  }else{
							  procedeModificar();  
						  }
					  } else if (result.isDenied) {
						 
					  }
					}) 
			}else{
				 if (estatusOrden == 'A4'){
					 procedeModificarOmitirComplemento();
				 }else{
					 procedeModificar();	 
				 }
			}
		} catch(e){
			alert('guardarCatalogo()_'+e);
		}
    }
	
	
	
	function procedeModificar(){
		try{
				var formData = new FormData(document.getElementById("frmNuevaOrden"));
	            $.ajax({
	            	url: '/siarex247/visor/tablero/modificaOrden.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	if (data.codError == '000') {
	 					Swal.fire({
	 						  	title: MSG_OPERACION_EXITOSA_MENU,
	 						  // html: '<p>Cambio Exitoso</p>',
	 						  	 html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							 $('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
	 						});
					} else {
						Swal.fire({
  			                title: MSG_ERROR_OPERACION_MENU,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
	             });				
		}
		catch(e){
			alert('guardarCatalogo()_'+e);
		}
    }
	
	
	function procedeModificarOmitirComplemento(){
		try{
				var formData = new FormData(document.getElementById("frmNuevaOrden"));
	            $.ajax({
	            	url: '/siarex247/visor/tablero/modificaOrdenOmitirComple.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	if (data.codError == '000') {
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						  // html: '<p>Registro actualizado satisfactoriamente</p>',
	 						     html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							 $('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
	 						});
					} else {
						Swal.fire({
  			                title: MSG_ERROR_OPERACION_MENU,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
	             });				
		}
		catch(e){
			alert('guardarCatalogo()_'+e);
		}
    }
	
	
	

	
	
	function obtenerTipoMoneda(){
		try{
			return $('#tipoMoneda_Visor').val();
		}catch(e){
			alert("obtenerTipoMoneda()_"+e);
		}
	}
	
	function obtenerEstatusOrden(){
		try{
			return $('#estatusOrden_Visor').val();
		}catch(e){
			alert("obtenerEstatusOrden()_"+e);
		}
	}
	
	
	function obtenerRfc(){
		try{
			return $('#rfcFiltro_Visor').val();
		}catch(e){
			alert("obtenerRfc()_"+e);
		}
	}
	
	
	function obtenerRazonSocial(){
		try{
			return $('#razonSocialFiltro_Visor').val();
		}catch(e){
			alert("obtenerRazonSocial()_"+e);
		}
	}
	
	
	function obtenerUuid(){
		try{
			return $('#uuidFiltro_Visor').val();
		}catch(e){
			alert("obtenerUuid()_"+e);
		}
	}
	
	function obtenerFolioEmpresa(){
		try{
			var folioEmpresa = $('#ordenCompra_Visor').val();
			if (folioEmpresa == ''){
				folioEmpresa = 0;
			}
			return folioEmpresa;
		}catch(e){
			alert("obtenerFolioEmpresa()_"+e);
		}
	}
	
	function obtenerSerieFolio(){
		try{
			return $('#serieFolio_Visor').val();
		}catch(e){
			alert("obtenerSerieFolio()_"+e);
		}
	}
	
	
	
	function verDocumento(tipoDocumento, folioOrden){
		try{
			//alert('********** CONSULTANDO ORDENES *************');
			if (tipoDocumento == 'PDF_ORDEN'){
				$.ajax({
					url  : '/siarex247/visor/tablero/consultarOrden.action',
					type : 'POST', 
					data : {
						folioOrden : folioOrden
					},
					dataType : 'json',
					success  : function(data) {
						if($.isEmptyObject(data)) {
						} else {
							
							if ( data.nombreArchivo == ''){
								Swal.fire({
		  			                title: MSG_ERROR_OPERACION_MENU,
		  			                //html: '<p>La orden seleccionado no contiene un archivo .PDF</p>',	
		  			                html: '<p>' + VISOR_MSG4 + '</p>',
		  			                icon: 'info'
		  			            });
							}else{
								$('#tipoDocumento_MostrarDocumento').val(tipoDocumento);
								$('#folioOrden_MostrarDocumento').val(folioOrden);
								document.frmMostrarDocumento.submit();
							}
						}
					},
					error : function(xhr, ajaxOptions, thrownError) {
						alert('consultarNotaCredito()_'+thrownError);
					}
				});
			}else{
				$('#tipoDocumento_MostrarDocumento').val(tipoDocumento);
				$('#folioOrden_MostrarDocumento').val(folioOrden);
				document.frmMostrarDocumento.submit();	
			}
			
			
		}catch(e){
			alert('verDocumento()_'+e);
		}
	}
	
	
	
	
	
	function solicitarLiberacionPago(){
		try{
			var folioOrden = getSelections();
			if (folioOrden == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                //html: '<p>Es necesario seleccionar al menos un registro.</p>',	
		                html: '<p>'+VISOR_MSG5+'</p>',
		                icon: 'info'
		            });
			}else{
				var arrFolios = folioOrden.split(";");
				if (arrFolios.length >= 3){
					Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		               // html: '<p>Es necesario seleccionar solo un registro.</p>',	
		                html: '<p>'+VISOR_MSG6+'</p>',
		                icon: 'info'
		            });
				}else{
					var numeroOrden = arrFolios[0]; 
					$.ajax({
						url  : '/siarex247/visor/tablero/solicitudLiberacionPago.action',
						type : 'POST', 
						data : {
							folioOrden : numeroOrden
						},
						dataType : 'json',
						success  : function(data) {
							if($.isEmptyObject(data)) {
							} else {
								if (data.codError == '000') {
				 					Swal.fire({
				 						  title: MSG_OPERACION_EXITOSA_MENU,
				 						  // html: '<p>Su solicitud de liberación de pago fue enviado satisfactoriamente.</p>',
				 						   html: '<p> '+VISOR_MSG7 + '</p>',
											  showCancelButton: false,
											  confirmButtonText: BTN_ACEPTAR_MENU,
											  icon: 'success'
				 						}).then((result) => {
				 							 
				 						});
								} else {
									Swal.fire({
			  			                title: MSG_ERROR_OPERACION_MENU,
			  			                // html: '<p>Para realizar la solicitud de liberación de pago es necesario que la orden de compra no tenga Servicio Recibido</p>',
			  			                html: '<p>'+VISOR_MSG8+'</p>',
			  			                icon: 'error'
			  			            });
								}
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert('solicitarLiberacionPago()_'+thrownError);
						}
					});
				}
			}
		}catch(e){
			alert('solicitarLiberacionPago()_'+e);
		}
	}
	
	
	
	function obtenerFechaIni(){
		return $('#fechaInicial').val();
	}
	
	
	function obtenerFechaFin(){
		return $('#fechaFinal').val();
	}

	
	

	function obtenerFechasFiltro(){
		$.ajax({
			url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/consultarFechas.action',
			type : 'POST', 
			data : null,
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					$('#fechaInicial').val(data.fechaInicial);
					$('#fechaFinal').val(data.fechaFinal);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('buscaCatalogo()_'+thrownError);
			}
		});	
		
    }
	
	
	function validarFechas(){
		// var fechaInicial = $('#fechaInicial').val();
		// var fechaFinal = $('#fechaFinal').val();
		
		refrescarVisor();
		/*
		$.ajax({
			url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/validarFechas.action',
			data : {
				fechaInicial : fechaInicial,
				fechaFinal  : fechaFinal
			},
			type : 'POST', 
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					if (data.codError == '000') {
						refrescarVisor();
					} else {
						Swal.fire({
  			                //title: '¡Rango de fechas no valido!',
							title: VISOR_TITLE17,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'info'
  			            });
					}
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('buscaCatalogo()_'+thrownError);
			}
		});	
		*/
    }

	
	function refrescarVisor(){
		$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);	
	}
	

	 function validaGeneraFactura(){
		 try{
			 var folioEmpresa = getSelectionsEmpresa();
				if (folioEmpresa == ''){
					Swal.fire({
			                title: MSG_ERROR_OPERACION_MENU,
			                //html: '<p>Es necesario seleccionar un registro.</p>',	
			                html: '<p>'+VISOR_MSG5+'</p>',
			                icon: 'info'
			            });
				}else{
					var arrFolios = folioEmpresa.split(";");
					if (arrFolios.length >= 3){
						Swal.fire({
			                title: MSG_ERROR_OPERACION_MENU,
			               // html: '<p>Es necesario seleccionar solo un registro.</p>',	
			                html: '<p>'+VISOR_MSG6+'</p>',
			                icon: 'info'
			            });
					}else{
						var numeroOrden = arrFolios[0]; 
						$.ajax({
							url  : '/siarex247/visor/tablero/generarFactura.action',
							type : 'POST', 
							data : {
								folioEmpresa : numeroOrden
							},
							dataType : 'json',
							success  : function(data) {
								if($.isEmptyObject(data)) {
								} else {
									if (data.codError == '000') {
					 					Swal.fire({
					 						  title: MSG_OPERACION_EXITOSA_MENU,
					 						  // html: '<p>La factura se ha generado satisfactoriamente.</p>',
					 						   html: '<p>'+VISOR_MSG9+'</p>',
												  showCancelButton: false,
												  confirmButtonText: BTN_ACEPTAR_MENU,
												  icon: 'success'
					 						}).then((result) => {
					 							$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
					 						});
									} else {
										Swal.fire({
				  			                title: MSG_ERROR_OPERACION_MENU,
				  			                html: '<p>'+data.mensajeError+'</p>',	
				  			                icon: 'error'
				  			            });
									}
									
									
								}
							},
							error : function(xhr, ajaxOptions, thrownError) {
								alert('solicitarLiberacionPago()_'+thrownError);
							}
						});
					}
				}
		 }catch(e){
			 alert('validaGeneraFactura()_'+e);
		 }
	 }
	
	 
	 function abrirPantallaLogo(){
		 try{
			 iniciaFormLogoProveedor();
			 $('#myModalCargarLogo').modal('show');
		 }catch(e){
			 alert('cargarLogo()_'+e);
		 }
	 }
	 
	 
	 function exportarDetalleExcel(){
		   try{
			    var tipoMoneda = obtenerTipoMoneda();
			    var estatusOrden = obtenerEstatusOrden();
			    var rfc = obtenerRfc();
			    var razonSocial = obtenerRazonSocial();
			    var uuid = obtenerUuid();
			    var folioEmpresa = obtenerFolioEmpresa();
			    var serieFolio = obtenerSerieFolio();
			    var fechaInicial = obtenerFechaIni();
			    var fechaFinal = obtenerFechaFin();


	       	  $('#tipoMoneda_Exportar').val(tipoMoneda);
	       	  $('#estatusOrden_Exportar').val(estatusOrden);
	       	  $('#rfc_Exportar').val(rfc);
	       	  $('#razonSocial_Exportar').val(razonSocial);
	       	  $('#uuid_Exportar').val(uuid);
	       	  $('#folioEmpresa_Exportar').val(folioEmpresa);
	       	  $('#serieFolio_Exportar').val(serieFolio);
	       	  $('#fechaInicial_Exportar').val(fechaInicial);
	       	  $('#fechaFinal_Exportar').val(fechaFinal);
	       	  document.frmExportarDetalleExcel.submit();	
	       	
		   } catch(e){
			   alert('exportarDetalleExcel(): ' + e);
		   }
	 }
	 

		function limpiarVisor(){
			try{
				$('#rfcFiltro_Visor').val('');
				$('#razonSocialFiltro_Visor').val('');
				$('#fechaInicial').val('');
				$('#ordenCompra_Visor').val('');
				$('#uuidFiltro_Visor').val('');
				$('#fechaFinal').val('');
				$('#estatusOrden_Visor').val(''); 
				$('#estatusOrden_Visor').trigger('change');
				$('#serieFolio_Visor').val('');
				$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
			}catch(e){
				alert('limpiarRecibidos()_'+e);
			}
		}
		
	 