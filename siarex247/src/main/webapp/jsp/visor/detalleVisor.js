

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
				ordering    : false,
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
	               
					{
				  text:
				 '<span class="fas fa-broom" data-fa-transform="shrink-3 down-2"></span>' +
				'<span class="d-none d-sm-inline-block ms-1">Limpiar</span>',
				className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				 action: function(){ limpiarVisor(); }
					 },
					 {
					   text:
					     '<span class="fab fa-firefox-browser me-1"></span>' +
					     '<span class="d-none d-sm-inline-block" id="VISOR_BTN_REFRESCAR">Actualizar</span>',
					   className: 'btn btn-falcon-success btn-sm mb-1 me-1',
					   action: function (e, dt, node) {
						validarFechasoOrdenes();
					   }
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
					data: function(d){
						
					     // ========= TEXTOS =========
					     d.rsOperator         = $('#rsOperatorV').val()          || 'contains';
					     d.rsValue            = ($('#rsFilterV').val()||'').trim();

					   //  d.ocOperator         = $('#ocOperatorV').val()          || 'contains';
					     d.ocValue            = ($('#ocFilterV').val()||'').trim();

					     d.descOperator       = $('#descOperatorV').val()        || 'contains';
					     d.descValue          = ($('#descFilterV').val()||'').trim();

					     d.sfOperator         = $('#sfOperatorV').val()          || 'contains';
					     d.sfValue            = ($('#sfFilterV').val()||'').trim();
						 // (opcional) Compatibilidad si tu backend espera otros nombres:
						 d.serieFolioOperator = d.sfOperator;
						 d.serieFolioValue    = d.sfValue;

					     d.asignarOperator    = $('#asignarOperatorV').val()     || 'contains';
					     d.asignarValue       = ($('#asignarFilterV').val()||'').trim();

					   //  d.estadoCfdiOperator = $('#estadoCfdiOperatorV').val()  || 'contains';
					    // d.estadoCfdiValue    = ($('#estadoCfdiFilterV').val()||'').trim();

					     d.cpsOperator        = $('#cpsOperatorV').val()         || 'contains';
					     d.cpsValue           = ($('#cpsFilterV').val()||'').trim();
						 
						 // 👇 OC numérico (antes era texto)
						 d.ocOperator         = ($('#ocOperatorV').val()||'eq');     // eq|ne|lt|le|gt|ge|bt
						 d.ocV1               = ($('#ocV1V').val()||'').trim();
						 d.ocV2               = ($('#ocV2V').val()||'').trim();
						 
					     // ========= SELECTS =========
					     // Tipo Moneda (ALL/MXN/USD)
					     d.monedaOperator     = $('#monedaOperatorV').val() || ( ($('#monedaFilterV').val()||'') && $('#monedaFilterV').val()!=='ALL' ? 'equals' : 'contains' );
					     d.monedaValue        = ($('#monedaFilterV').val()==='ALL') ? '' : (($('#monedaFilterV').val()||'').trim());

					     // Servicio Recibo ? (ALL/S/N)
					     d.reciboOperator     = $('#reciboOperatorV').val() || ( ($('#reciboFilterV').val()||'') && $('#reciboFilterV').val()!=='ALL' ? 'equals' : 'contains' );
					     d.reciboValue        = ($('#reciboFilterV').val()==='ALL') ? '' : (($('#reciboFilterV').val()||'').trim());

					     // Status de Pago (ALL/A1..A11/"")
					     d.estatusPagoOperator= $('#estatusPagoOperatorV').val() || ( ($('#estatusPagoFilterV').val()||'') && $('#estatusPagoFilterV').val()!=='ALL' ? 'equals' : 'contains' );
					     d.estatusPagoValue   = ($('#estatusPagoFilterV').val()==='ALL') ? '' : (($('#estatusPagoFilterV').val()||'').trim());

					     // Estatus en SAT (ALL/S/N)
					     d.estatusSatOperator = $('#estatusSatOperatorV').val() || ( ($('#estatusSatFilterV').val()||'') && $('#estatusSatFilterV').val()!=='ALL' ? 'equals' : 'contains' );
					     d.estatusSatValue    = ($('#estatusSatFilterV').val()==='ALL') ? '' : (($('#estatusSatFilterV').val()||'').trim());

					     // Uso CFDI (ALL/G03/D10/S01/OTROS)
					     d.usoCfdiOperator    = $('#usoCfdiOperatorV').val() || ( ($('#usoCfdiFilterV').val()||'') && $('#usoCfdiFilterV').val()!=='ALL' ? 'equals' : 'contains' );
					     d.usoCfdiValue       = ($('#usoCfdiFilterV').val()==='ALL') ? '' : (($('#usoCfdiFilterV').val()||'').trim());
						 
						 // Estado CFDI (ALL/VIGENTE/CANCELADO/NO ENCONTRADO)
						 d.estadoCfdiOperator = $('#estadoCfdiOperatorV').val()
						   || ( ($('#estadoCfdiFilterV').val()||'') && $('#estadoCfdiFilterV').val()!=='ALL' ? 'equals' : 'contains' );
						 d.estadoCfdiValue = ($('#estadoCfdiFilterV').val()==='ALL') ? '' : (($('#estadoCfdiFilterV').val()||'').trim());
						 d.ultmovV1			  = obtenerFechaIni();
						 d.ultmovV2			  = obtenerFechaFin();
						 						 
					     // ========= NUMÉRICOS (V1/V2 + Operator) =========
					     function packNum(prefix){
					       d[prefix+'V1']       = ($('#'+prefix+'Filter1V').val()||'').trim();
					       d[prefix+'V2']       = ($('#'+prefix+'Filter2V').val()||'').trim();
					       d[prefix+'Operator'] = ($('#'+prefix+'OperatorV').val()||'eq'); // eq|ne|lt|gt|le|ge|bt
					     }
					     // Mapeo exacto de tus IDs:
					     // monto, total, subtotal, iva, ivaret, isrret, imploc, totalnc, pagot, ivaretnc
					     ['monto','total','subtotal','iva','ivaret','isrret','imploc','totalnc','pagot','ivaretnc'].forEach(packNum);

					     // ========= FECHAS (V1/V2 + Operator) =========
					     function packDate(prefix){
					       d[prefix+'DateV1']       = ($('#'+prefix+'Filter1V').val()||'').trim();
					       d[prefix+'DateV2']       = ($('#'+prefix+'Filter2V').val()||'').trim();
					       d[prefix+'DateOperator'] = ($('#'+prefix+'OperatorV').val()||'eq'); // eq|ne|lt|gt|le|ge|bt
					     }
					     // fechapago, ultmov
					     //['fechapago','ultmov'].forEach(packDate);
						 ['fechapago'].forEach(packDate);

					     // Anti-cache opcional
					     d._ = Date.now();
					     // console.log('[payload.DetalleVisor]', d); // debug opcional
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
	

	function validarFechasoOrdenes(){
		var fechaInicial = obtenerFechaIni();
		var fechaFinal = obtenerFechaFin();
		
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
						window.refrescarVisor_DX();
						// refrescarVisor_DX();
					} else {
						Swal.fire({
			                title: '¡Rango de fechas no valido!',
							//title: LABEL_BOVEDA_TEXT5,
			                html: '<p>'+data.mensajeError+'</p>',	
			                icon: 'info'
			            });
					}
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('validarFechas()_'+thrownError);
			}
		});	
		
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
								 tablaDetalleVisor.page('first').draw('page');
	  	 						$('#tablaDetalleVisor').DataTable().ajax.reload(null,true);
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
								 tablaDetalleVisor.page('first').draw('page');
	  	 						$('#tablaDetalleVisor').DataTable().ajax.reload(null,true);
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
								 tablaDetalleVisor.page('first').draw('page');
	  	 						$('#tablaDetalleVisor').DataTable().ajax.reload(null,true);
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
					//$('#fechaInicial').val(data.fechaInicial);
					//$('#fechaFinal').val(data.fechaFinal);
					obtenerFechasMinima(data.fechaInicial, data.fechaFinal);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('obtenerFechasFiltro()_'+thrownError);
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
		tablaDetalleVisor.page('first').draw('page');
		$('#tablaDetalleVisor').DataTable().ajax.reload(null,true);	
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
												tablaDetalleVisor.page('first').draw('page');
					 							$('#tablaDetalleVisor').DataTable().ajax.reload(null,true);
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
	 

	 // Utilidad mínima que ya usas
	 function syncHiddenVisor(){
	   const toYMD = d => d.toISOString().slice(0,10);
	   const parse = s => (s && /^\d{4}-\d{2}-\d{2}$/.test(s)) ? new Date(s+'T00:00:00') : null;

	   const d1 = parse($('#fechaInicial').val());
	   const d2 = parse($('#fechaFinal').val());

	   if (d1 && d2){
	     $('#ultmovOperatorV').val('bt');
	     $('#ultmovFilter1V').val(toYMD(d1));
	     $('#ultmovFilter2V').val(toYMD(d2));
	   } else if (d1){
	     $('#ultmovOperatorV').val('ge');
	     $('#ultmovFilter1V').val(toYMD(d1));
	     $('#ultmovFilter2V').val('');
	   } else if (d2){
	     $('#ultmovOperatorV').val('le');
	     $('#ultmovFilter1V').val('');
	     $('#ultmovFilter2V').val(toYMD(d2));
	   } else {
	     $('#ultmovOperatorV').val('bt');
	     $('#ultmovFilter1V').val('');
	     $('#ultmovFilter2V').val('');
	   }
	 }

	 // ===== SOLO setea inputs con lo que regresa el back (sin más lógica) =====
	 function obtenerFechasFiltroVisor(){
	   return new Promise(resolve=>{
	     $.ajax({
	       url  : '/siarex247/cumplimientoFiscal/boveda/nomina/consultarFechasNomina.action',
	       type : 'POST',
	       data : null,
	       dataType : 'json'
	     })
	     .done(data=>{
	       if ($.isEmptyObject(data)) {
	         // si el back no manda nada: limpia
	         $('#fechaInicial').val('');
	         $('#fechaFinal').val('');
	       } else {
	         $('#fechaInicial').val((data.fechaInicial||'').trim());
	         $('#fechaFinal').val((data.fechaFinal||'').trim());
	       }
	       syncHiddenVisor();
	       resolve(data||null);
	     })
	     .fail((xhr,_,err)=>{
	       console.warn('obtenerFechasFiltroVisor()_', err);
	       // no impongo nada si falla, solo limpio y sincronizo
	       $('#fechaInicial').val('');
	       $('#fechaFinal').val('');
	       syncHiddenVisor();
	       resolve(null);
	     });
	   });
	 }


		 
		  function limpiarVisor(){
	 	   try{
	 	     const setOp=(opSel,btnSel,val,labelHtml)=>{ $(opSel).val(val); $(btnSel+' .op-label').html(labelHtml); };

	 	     // ==== TEXTOS ====
	 	     $('#rsFilterV,#ocFilterV,#descFilterV,#sfFilterV,#asignarFilterV,#estadoCfdiFilterV,#cpsFilterV').val('');
	 	     setOp('#rsOperatorV','#rsOpBtnV','contains','<i class="fas fa-search"></i>');
	 	    // setOp('#ocOperatorV','#ocOpBtnV','contains','<i class="fas fa-search"></i>');
	 	     setOp('#descOperatorV','#descOpBtnV','contains','<i class="fas fa-search"></i>');
	 	     setOp('#sfOperatorV','#sfOpBtnV','contains','<i class="fas fa-search"></i>');
	 	     setOp('#asignarOperatorV','#asignarOpBtnV','contains','<i class="fas fa-search"></i>');
	 	    // setOp('#estadoCfdiOperatorV','#estadoCfdiOpBtnV','contains','<i class="fas fa-search"></i>');
	 	     setOp('#cpsOperatorV','#cpsOpBtnV','contains','<i class="fas fa-search"></i>');

	 	     // ==== SELECTS ====
	 	     $('#monedaFilterV').val('ALL');     setOp('#monedaOperatorV','#monedaOpBtnV','contains','<i class="fas fa-search"></i>');
	 	     $('#reciboFilterV').val('ALL');     setOp('#reciboOperatorV','#reciboOpBtnV','contains','<i class="fas fa-search"></i>');
	 	     $('#estatusPagoFilterV').val('ALL');setOp('#estatusPagoOperatorV','#estatusPagoOpBtnV','contains','<i class="fas fa-search"></i>');
	 	     $('#estatusSatFilterV').val('ALL'); setOp('#estatusSatOperatorV','#estatusSatOpBtnV','contains','<i class="fas fa-search"></i>');
	 	     $('#usoCfdiFilterV').val('');    setOp('#usoCfdiOperatorV','#usoCfdiOpBtnV','contains','<i class="fas fa-search"></i>');
	 		 $('#estadoCfdiFilterV').val('ALL');
	 		 setOp('#estadoCfdiOperatorV','#estadoCfdiOpBtnV','contains','<i class="fas fa-search"></i>');

	 		 
	 		 // OC numérico
	 		 $('#ocOperatorV').val('eq');
	 		 $('#ocV1V').val('');
	 		 $('#ocV2V').val('').addClass('d-none');
	 		 $('#ocOpBtnV .op-label').text('=');


	 	     // ==== NUMÉRICOS ====
	 	     const resetNum=(op,v1,v2,btn)=>{ $(op).val('eq'); $(v1).val(''); $(v2).val('').addClass('d-none'); $(btn+' .op-label').text('='); };
	 	     [
	 	       ['#montoOperatorV','#montoFilter1V','#montoFilter2V','#montoOpBtnV'],
	 	       ['#totalOperatorV','#totalFilter1V','#totalFilter2V','#totalOpBtnV'],
	 	       ['#subtotalOperatorV','#subtotalFilter1V','#subtotalFilter2V','#subtotalOpBtnV'],
	 	       ['#ivaOperatorV','#ivaFilter1V','#ivaFilter2V','#ivaOpBtnV'],
	 	       ['#ivaretOperatorV','#ivaretFilter1V','#ivaretFilter2V','#ivaretOpBtnV'],
	 	       ['#isrretOperatorV','#isrretFilter1V','#isrretFilter2V','#isrretOpBtnV'],
	 	       ['#implocOperatorV','#implocFilter1V','#implocFilter2V','#implocOpBtnV'],
	 	       ['#totalncOperatorV','#totalncFilter1V','#totalncFilter2V','#totalncOpBtnV'],
	 	       ['#pagotOperatorV','#pagotFilter1V','#pagotFilter2V','#pagotOpBtnV'],
	 	       ['#ivaretncOperatorV','#ivaretncFilter1V','#ivaretncFilter2V','#ivaretncOpBtnV']
	 	     ].forEach(args=>resetNum(...args));

	 	     // ==== FECHAS ====
	 	     const resetDate=(op,d1,d2,btn)=>{ $(op).val('eq'); $(d1).val(''); $(d2).val('').addClass('d-none'); $(btn+' .op-label').text('='); };
	 	     resetDate('#fechapagoOperatorV','#fechapagoFilter1V','#fechapagoFilter2V','#fechapagoOpBtnV');
	 	     resetDate('#ultmovOperatorV','#ultmovFilter1V','#ultmovFilter2V','#ultmovOpBtnV');

	 	     // Cierra menús y recarga
	 	     $('#tablaDetalleVisor thead .dx-like-menu, .dtfh-floatingparent thead .dx-like-menu').removeClass('show');
	 		 
	 		 // --- Limpia el componente de rango de fechas (CRMDateRangeR) ---
	 		 /*
	 		 		var range = document.getElementById('CRMDateRangeV');
	 		 		if (range) {
	 		 		  range.value = '';
	 		 		  if (range._flatpickr) {
	 		 		    range._flatpickr.clear(); // limpia selección visual
	 		 		  }
	 		 		} */
	 				obtenerFechasFiltroVisor();

	 	     if (typeof window.refrescarVisor_DX==='function') window.refrescarVisor_DX('clear-all');
	 	     else if ($.fn.dataTable.isDataTable('#tablaDetalleVisor')) { tablaDetalleVisor.page('first').draw('page'); $('#tablaDetalleVisor').DataTable().ajax.reload(null,true); }
	 	   }catch(e){
	 	     alert('limpiarVisor(): '+e);
	 	   }
	 	 }
	

		
		function exportarFacturasDX(){
		// console.log('entro a exportar facturas DX');
		  const _nz  = v => (v ?? '').toString().trim();
		  const _sel = v => (v && v !== 'ALL') ? v : '';

		  // selección directa de filas
		  $('#dx_foliosExportar').val(getSelectionsEmpresa());

		  // TEXTO/SELECTS
		  $('#dx_razonSocial').val(_nz($('#rsFilterV').val()));
		  $('#dx_rsOperator').val(_nz($('#rsOperatorV').val()));

		  // OC (numérico)  👈 faltaba
		  $('#dx_ocOperator').val(_nz($('#ocOperatorV').val()));
		  $('#dx_ocV1').val(_nz($('#ocV1V').val()));
		  $('#dx_ocV2').val(_nz($('#ocV2V').val()));

		  $('#dx_descripcion').val(_nz($('#descFilterV').val()));
		  $('#dx_descOperator').val(_nz($('#descOperatorV').val()));

		  $('#dx_tipoMoneda').val(_sel($('#monedaFilterV').val()));
		  $('#dx_monedaOperator').val('contains');

		  $('#dx_servicioRecibo').val(_sel($('#reciboFilterV').val()));
		  $('#dx_reciboOperator').val('contains');

		  $('#dx_estatusPago').val(_sel($('#estatusPagoFilterV').val()));
		  $('#dx_estatusPagoOperator').val('contains');

		  $('#dx_serieFolio').val(_nz($('#sfFilterV').val()));
		  $('#dx_serieFolioOperator').val(_nz($('#sfOperatorV').val()));

		  $('#dx_asignarA').val(_nz($('#asignarFilterV').val()));
		  $('#dx_asignarOperator').val(_nz($('#asignarOperatorV').val()));

		  $('#dx_estadoCfdi').val(_sel($('#estadoCfdiFilterV').val()));
		  $('#dx_estadoCfdiOperator').val(_nz($('#estadoCfdiOperatorV').val()));

		  $('#dx_estatusSat').val(_sel($('#estatusSatFilterV').val()));
		  $('#dx_estatusSatOperator').val(_nz($('#estatusSatOperatorV').val()));

		  $('#dx_usoCfdi').val(_nz($('#usoCfdiFilterV').val()));
		  $('#dx_usoCfdiOperator').val(_nz($('#usoCfdiOperatorV').val()));

		  $('#dx_cps').val(_nz($('#cpsFilterV').val()));
		  $('#dx_cpsOperator').val(_nz($('#cpsOperatorV').val()));

		  // NUMÉRICOS
		  const mapNum = (base) => {
		    $('#dx_'+base+'Operator').val(_nz($('#'+base+'OperatorV').val()));
		    $('#dx_'+base+'V1').val(_nz($('#'+base+'Filter1V').val()));
		    $('#dx_'+base+'V2').val(_nz($('#'+base+'Filter2V').val()));
		  };
		  ['monto','total','subtotal','iva','ivaret','isrret','imploc','totalnc','pagot','ivaretnc'].forEach(mapNum);

		  // FECHAS
		  $('#dx_fechapagoOperator').val(_nz($('#fechapagoOperatorV').val()));
		  $('#dx_fechapagoV1').val(_nz($('#fechapagoFilter1V').val()));
		  $('#dx_fechapagoV2').val(_nz($('#fechapagoFilter2V').val()));

		  $('#dx_ultmovOperator').val(_nz($('#ultmovOperatorV').val()) || 'bt');
		  $('#dx_ultmovV1').val(_nz($('#ultmovFilter1V').val()));
		  $('#dx_ultmovV2').val(_nz($('#ultmovFilter2V').val()));

		  document.getElementById('frmExportarFacturas').submit();
		}

		function exportarlayOutXD(){
		  const _nz  = v => (v ?? '').toString().trim();
		  const _sel = v => (v && v !== 'ALL') ? v : '';

		  //console.log('entro a Exportar LayDX');

		  // ===== Selección directa =====
		  $('#dxlo_foliosExportar').val(getSelectionsEmpresa()); // "" => export por filtros

		  // ===== OC (numérico) =====
		  $('#dxlo_ocOperator').val(_nz($('#ocOperatorV').val()));
		  $('#dxlo_ocV1').val(_nz($('#ocV1V').val()));
		  $('#dxlo_ocV2').val(_nz($('#ocV2V').val()));

		  // ===== TEXTO / SELECTS =====
		  $('#dxlo_razonSocial').val(_nz($('#rsFilterV').val()));
		  $('#dxlo_rsOperator').val(_nz($('#rsOperatorV').val()));

		  $('#dxlo_descripcion').val(_nz($('#descFilterV').val()));
		  $('#dxlo_descOperator').val(_nz($('#descOperatorV').val()));

		  $('#dxlo_tipoMoneda').val(_sel($('#monedaFilterV').val()));
		  $('#dxlo_monedaOperator').val('contains');

		  $('#dxlo_servicioRecibo').val(_sel($('#reciboFilterV').val()));
		  $('#dxlo_reciboOperator').val('contains');

		  $('#dxlo_estatusPago').val(_sel($('#estatusPagoFilterV').val()));
		  $('#dxlo_estatusPagoOperator').val('contains');

		  $('#dxlo_serieFolio').val(_nz($('#sfFilterV').val()));
		  $('#dxlo_serieFolioOperator').val(_nz($('#sfOperatorV').val()));

		  $('#dxlo_asignarA').val(_nz($('#asignarFilterV').val()));
		  $('#dxlo_asignarOperator').val(_nz($('#asignarOperatorV').val()));

		  $('#dxlo_estadoCfdi').val(_sel($('#estadoCfdiFilterV').val()));
		  $('#dxlo_estadoCfdiOperator').val(_nz($('#estadoCfdiOperatorV').val()));

		  $('#dxlo_estatusSat').val(_sel($('#estatusSatFilterV').val()));
		  $('#dxlo_estatusSatOperator').val(_nz($('#estatusSatOperatorV').val()));

		  $('#dxlo_usoCfdi').val(_sel($('#usoCfdiFilterV').val()) || _nz($('#usoCfdiFilterV').val())); // texto o cat
		  $('#dxlo_usoCfdiOperator').val(_nz($('#usoCfdiOperatorV').val()));

		  $('#dxlo_cps').val(_nz($('#cpsFilterV').val()));
		  $('#dxlo_cpsOperator').val(_nz($('#cpsOperatorV').val()));

		  // ===== NUMÉRICOS (op, v1, v2) =====
		  const mapNum = (base) => {
		    $('#dxlo_'+base+'Operator').val(_nz($('#'+base+'OperatorV').val()));
		    $('#dxlo_'+base+'V1').val(_nz($('#'+base+'Filter1V').val()));
		    $('#dxlo_'+base+'V2').val(_nz($('#'+base+'Filter2V').val()));
		  };
		  ['monto','total','subtotal','iva','ivaret','isrret','imploc','totalnc','pagot','ivaretnc'].forEach(mapNum);

		  // ===== FECHAS =====
		  $('#dxlo_fechapagoOperator').val(_nz($('#fechapagoOperatorV').val()));
		  $('#dxlo_fechapagoV1').val(_nz($('#fechapagoFilter1V').val()));
		  $('#dxlo_fechapagoV2').val(_nz($('#fechapagoFilter2V').val()));

		  // Último movimiento: fuerza operador según lo capturado
		  const d1 = _nz($('#ultmovFilter1V').val());
		  const d2 = _nz($('#ultmovFilter2V').val());
		  let opUlt = _nz($('#ultmovOperatorV').val()); // puede venir "eq" por default
		  if (d1 && d2)      opUlt = 'bt';
		  else if (d1 && !d2) opUlt = (opUlt && opUlt !== 'bt') ? opUlt : 'ge';
		  else if (!d1 && d2) opUlt = (opUlt && opUlt !== 'bt') ? opUlt : 'le';
		  else                opUlt = 'bt'; // sin fechas => back usa su default de 365 días
		  $('#dxlo_ultmovOperator').val(opUlt);
		  $('#dxlo_ultmovV1').val(d1);
		  $('#dxlo_ultmovV2').val(d2);

		  // ===== Logs rápidos para validar =====
		  console.log('RS:', $('#dxlo_razonSocial').val(), 'op=', $('#dxlo_rsOperator').val());
		  console.log('DESC:', $('#dxlo_descripcion').val(), 'op=', $('#dxlo_descOperator').val());
		  console.log('SERIE:', $('#dxlo_serieFolio').val(), 'op=', $('#dxlo_serieFolioOperator').val());
		  console.log('ASIGNAR:', $('#dxlo_asignarA').val(), 'op=', $('#dxlo_asignarOperator').val());
		  console.log('CFDI:', $('#dxlo_estadoCfdi').val(), 'op=', $('#dxlo_estadoCfdiOperator').val());
		  console.log('EST SAT:', $('#dxlo_estatusSat').val(), 'op=', $('#dxlo_estatusSatOperator').val());
		  console.log('USO:', $('#dxlo_usoCfdi').val(), 'op=', $('#dxlo_usoCfdiOperator').val());
		  console.log('CPS:', $('#dxlo_cps').val(), 'op=', $('#dxlo_cpsOperator').val());
		  console.log('OC:', $('#dxlo_ocOperator').val(), $('#dxlo_ocV1').val(), $('#dxlo_ocV2').val());
		  console.log('ULTMOV:', $('#dxlo_ultmovOperator').val(), $('#dxlo_ultmovV1').val(), $('#dxlo_ultmovV2').val());

		  // ===== Enviar =====
		  document.getElementById('frmexportarLayOut').submit();
		}







		function exportarPlantillaDX(){
		  console.log('entro..');

		  const _nz  = v => (v ?? '').toString().trim();
		  const _sel = v => (v && v !== 'ALL') ? v : '';

		  // Selección directa
		  $('#plt_foliosExportar').val(getSelectionsEmpresa());

		  // ===== OC (numérico) =====
		  $('#plt_ocOp').val(_nz($('#ocOperatorV').val()));
		  $('#plt_ocV1').val(_nz($('#ocV1V').val()));
		  $('#plt_ocV2').val(_nz($('#ocV2V').val()));

		  // ===== TEXTO / SELECTS =====
		  $('#plt_razonSocial').val(_nz($('#rsFilterV').val()));
		  $('#plt_rsOp').val(_nz($('#rsOperatorV').val()));

		  $('#plt_descripcion').val(_nz($('#descFilterV').val()));
		  $('#plt_descOp').val(_nz($('#descOperatorV').val()));

		  $('#plt_tipoMoneda').val(_sel($('#monedaFilterV').val()));
		  $('#plt_monedaOp').val('contains');

		  $('#plt_servicioRecibo').val(_sel($('#reciboFilterV').val()));
		  $('#plt_reciboOp').val('contains');

		  $('#plt_estatusPago').val(_sel($('#estatusPagoFilterV').val()));
		  $('#plt_estatusPagoOp').val('contains');

		  $('#plt_serieFolio').val(_nz($('#sfFilterV').val()));
		  $('#plt_serieFolioOp').val(_nz($('#sfOperatorV').val()));

		  $('#plt_asignarA').val(_nz($('#asignarFilterV').val()));
		  $('#plt_asignarOp').val(_nz($('#asignarOperatorV').val()));

		  $('#plt_estadoCfdi').val(_sel($('#estadoCfdiFilterV').val()));
		  $('#plt_estadoCfdiOp').val(_nz($('#estadoCfdiOperatorV').val()));

		  $('#plt_estatusSat').val(_sel($('#estatusSatFilterV').val()));
		  $('#plt_estatusSatOp').val(_nz($('#estatusSatOperatorV').val()));

		  // puede ser combo o texto; tomamos el valor o el texto normalizado
		  $('#plt_usoCfdi').val(_sel($('#usoCfdiFilterV').val()) || _nz($('#usoCfdiFilterV').val()));
		  $('#plt_usoCfdiOp').val(_nz($('#usoCfdiOperatorV').val()));

		  $('#plt_cps').val(_nz($('#cpsFilterV').val()));
		  $('#plt_cpsOp').val(_nz($('#cpsOperatorV').val()));

		  // ===== NUMÉRICOS (Op, V1, V2) =====
		  const mapNum = (base) => {
		    $('#plt_'+base+'Op').val(_nz($('#'+base+'OperatorV').val()));
		    $('#plt_'+base+'V1').val(_nz($('#'+base+'Filter1V').val()));
		    $('#plt_'+base+'V2').val(_nz($('#'+base+'Filter2V').val()));
		  };
		  ['monto','total','subtotal','iva','ivaret','isrret','imploc','totalnc','pagot','ivaretnc'].forEach(mapNum);

		  // ===== FECHAS =====
		  $('#plt_fechapagoOp').val(_nz($('#fechapagoOperatorV').val()));
		  $('#plt_fechapagoV1').val(_nz($('#fechapagoFilter1V').val()));
		  $('#plt_fechapagoV2').val(_nz($('#fechapagoFilter2V').val()));

		  // Último movimiento: ajusta operador según lo capturado
		  const d1 = _nz($('#ultmovFilter1V').val());
		  const d2 = _nz($('#ultmovFilter2V').val());
		  let opUlt = _nz($('#ultmovOperatorV').val()); // suele venir "eq"
		  if (d1 && d2)       opUlt = 'bt';
		  else if (d1 && !d2) opUlt = (opUlt && opUlt !== 'bt') ? opUlt : 'ge';
		  else if (!d1 && d2) opUlt = (opUlt && opUlt !== 'bt') ? opUlt : 'le';
		  else                opUlt = 'bt'; // sin fechas => el back ya hace default de 365 días
		  $('#plt_ultmovOp').val(opUlt);
		  $('#plt_ultmovV1').val(d1);
		  $('#plt_ultmovV2').val(d2);

		  // ===== Logs rápidos para validar =====
		  console.log('RS:', $('#plt_razonSocial').val(), 'op=', $('#plt_rsOp').val());
		  console.log('DESC:', $('#plt_descripcion').val(), 'op=', $('#plt_descOp').val());
		  console.log('SERIE:', $('#plt_serieFolio').val(), 'op=', $('#plt_serieFolioOp').val());
		  console.log('ASIGNAR:', $('#plt_asignarA').val(), 'op=', $('#plt_asignarOp').val());
		  console.log('CFDI:', $('#plt_estadoCfdi').val(), 'op=', $('#plt_estadoCfdiOp').val());
		  console.log('EST SAT:', $('#plt_estatusSat').val(), 'op=', $('#plt_estatusSatOp').val());
		  console.log('USO:', $('#plt_usoCfdi').val(), 'op=', $('#plt_usoCfdiOp').val());
		  console.log('CPS:', $('#plt_cps').val(), 'op=', $('#plt_cpsOp').val());
		  console.log('OC:', $('#plt_ocOp').val(), $('#plt_ocV1').val(), $('#plt_ocV2').val());
		  console.log('ULTMOV:', $('#plt_ultmovOp').val(), $('#plt_ultmovV1').val(), $('#plt_ultmovV2').val());

		  // Enviar
		  document.getElementById('frmExportarPlantilla').submit();
		}

	 

