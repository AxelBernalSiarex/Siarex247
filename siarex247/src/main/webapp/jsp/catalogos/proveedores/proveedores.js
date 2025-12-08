
var posOrden = 0;

var tablaProveedores = null;

	$(document).ready(function() {
		try {
			tablaProveedores = $('#tablaProveedores').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 15,
				lengthChange: false,
				dom: "<'row mx-0'<'col-md-8'Q><'col-md-4'Bl>>" +
				     "<'row mx-0'<'col-md-12'f><'mb-2 row mx-0 px-0 areaFiltro'>>" + 
					 "<'table-responsive scrollbar'tr>" + 
					 "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				ordering    : true,
				serverSide	: false,
				fixedHeader : true,
				orderCellsTop: true,
				info		: true,
				select      : true,
				/*
				select : {
			          'style': 'multi',
			          'selector': 'td:first-child'
			       },
			      */ 
			       
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Catalogo de Proveedores', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'catalogoProveedores'}
				    		 
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
				   	},
				   	/*
				    select: {
			            rows: "" 
			        },
			        */
			        searchBuilder: {
		                add: 'Agregar Filtro',
		                condition: 'Operador',
		                clearAll: 'Limpiar',
		                delete: 'Borrar',
		                deleteTitle: 'Borrar Titulo',
		                data: 'Columna',
		                left: 'Izquierda',
		                leftTitle: 'Titulo Izquierdo',
		                logicAnd: 'And',
		                logicOr: 'or',
		                right: 'Derecho',
		                rightTitle: 'Titulo Derecho',
		                title: {
		                    0: '',
		                    _: 'Filtros (%d)'
		                },
		                value: 'Opción',
		                valueJoiner: 'et',
		                conditions :{
		                	string: {
		                		contains: 'Contiene',
		                        empty: 'Vacío',
		                        endsWith: 'Termina con',
		                        equals: 'Igual a',
		                        not: 'Diferente de',
		                        notContains: 'No Contiene',
		                        notEmpty: 'No Vacío',
		                        notEndsWith: 'No Termina con',
		                        notStartsWith: 'No Inicia con',
		                        startsWith: 'Inicia con'
		                    },
		                    number: {
		                    	equals: 'Igual a',
		                    	not: 'Diferente de',
	                            gt: 'Mayor a',
	                            gte: 'Mayor o igual a',
	                            lt: 'Menor a',
	                            lte: 'Menor o igual a',
	                            between: 'Entre',
	                            notBetween: 'No entre',
	                            empty: 'Vacío',
	                            notEmpty: 'No vacío'
		                    },
		                    date: {
	                            before: 'Antes',
	                            after: 'Después',
	                            equals: 'Igual a',
	                            not: 'Diferente de',
	                            between: 'Entre',
	                            notBetween: 'No entre',
	                            empty: 'Vacío',
	                            notEmpty: 'No vacío'
	                        },
	                        moment: {
	                            before: 'Antes',
	                            after: 'Después',
	                            equals: 'Igual a',
	                            not: 'Diferente de',
	                            between: 'Entre',
	                            notBetween: 'No entre',
	                            empty: 'Vacío',
	                            notEmpty: 'No vacío'
	                        }
		                }
		            }
				},
				ajax : {
					url: '/siarex247/catalogos/proveedores/listadoProveedores.action',
					type: 'POST'
				},
				
				aoColumns : [
					{ mData: null},
					{ mData: "claveRegistro", "sClass": "text-center"},
					{ mData: "idProveedor", "sClass": "text-center"},
					{ mData: "razonSocial"},
					{ mData: "rfc"},
					{ mData: "delegacion"},
					{ mData: "ciudad"},
					{ mData: "estado"},
					{ mData: "telefono"},
					{ mData: "extencion"},
					{ mData: "nombreContacto"},
					{ mData: "tipoProveedor"},
					{ mData: "anexo24"},
					{ mData: null, "sClass": "text-center"},
					{ mData: null, "sClass": "text-center"},
					{ mData: "banco"},
					{ mData: "sucursal"},
					{ mData: "nombreSucursal"},
					{ mData: "numeroCuenta"},
					{ mData: "cuentaClabe"},
					{ mData: "moneda"},
					{ mData: "aba"},
					{ mData: "conceptoServicio"},
					{ mData: "formaPago"},
					{ mData: "fechaAlta"}
					
					
				],
				columnDefs: [
					{
				        targets: 0,
				        render: function (data, type, row) {
				        	rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				        	rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'ver\', \'' + row.claveRegistro + '\');">'+BTN_VER_MENU+'</a>';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.claveRegistro + '\');">'+BTN_EDITAR_MENU+'</a>';
				            rowElement += '<div class="dropdown-divider"></div>';
				            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaCatalogo(\'' + row.claveRegistro + '\', \'' + row.razonSocial + '\');">'+BTN_ELIMINAR_MENU+'</a>';
				            rowElement += '</div>';
				            rowElement += '</div>';
				                return rowElement;
				          }
				      },
				      {
						targets: 13,
						render: function (data, type, row) {
							rowElement = '';
							if (data.tieneIMSS == 'S'){
								rowElement = '<img class="" src="/theme-falcon/repse247/img/pdf26.png" alt="" style="cursor: pointer;" title="Ver PDF" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="generarCertificado(1, \'' + row.claveRegistro+ '\', \'' + row.rfc+ '\');" />';	
							}
	                        return rowElement;
						}
				      },
				      {
						targets: 14,
						render: function (data, type, row) {
							rowElement = '';
							if (data.tieneSAT == 'S'){
								rowElement = '<img class="" src="/theme-falcon/repse247/img/pdf26.png" alt="" style="cursor: pointer;" title="Ver PDF" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="generarCertificado(2, \'' + row.claveRegistro+ '\', \'' + row.rfc+ '\');" />';	
							}
	                        return rowElement;							
						}
				      }
					
					
				  ],
				  createdRow : function( row, data, dataIndex){
		                if( data.estatusRegistro ==  'D'){
		                    $(row).addClass('colorTR');
		                }
		            },
				  /*
		           select: {
		        	   style:    'os',
		               selector: 'td:first-child'
		           },*/
		          initComplete: function () {
		        	  var btns = $('.dt-button');
		              btns.removeClass('dt-button');
		              
		              var btnsSubMenu = $('.dtb-b2');
		              btnsSubMenu.addClass('dropdown-menu dropdown-menu-end py-0 show');
		              
		              //Agrega los combos de los filtros para columnas 3, 5
		              var areaFiltro = '.areaFiltro';
		              var cont = 0;
		              this.api().columns([]).every(function () {
		            	  cont = cont + 1;
		            	  var lblTitulo = this.header().textContent;
		            	  			              
		            	  var gruposelect = '.gruposelect'+cont;
		            	  var gruposelectCSS = 'gruposelect'+cont;
		                  var column = this;
		                  
		                  var grupo = $('<label class="col-md-1 col-form-label">'+ lblTitulo +'</label>'+
	            		  '<div class="col-md-5 pb-2"><div class="form-group '+ gruposelectCSS + '"></div></div>').appendTo(areaFiltro);
		                  
		                  var select = $('<select class="form-select Filtros"><option value="">Seleccionar Todas</option></select>' +
		                		  		 '<script>$(".Filtros").select2({allowClear: true,theme: "bootstrap-5", '+
		                		  		 'dropddownAutoWidth: false,placeholder: "Selecciona una opción"});</script>')
		                      .appendTo(gruposelect)
		                      .on('change', function () {
		                          var val = $.fn.dataTable.util.escapeRegex( $(this).val() );
		                          column
		                              .search(val ? '^' + val + '$' : '', true, false)
		                              .draw();
		                      });
		   
		                  	column.data().unique().sort().each(function (d, j) {
		                      select.append('<option value="' + d + '">' + d + '</option>');
		                  	});
		              });
	              
		           },
				  drawCallback: function () {
					  
				  }
			});
			
		} catch(e) {
			alert('usuarios()_'+e);
		};
		
		tablaProveedores.on( 'draw', function () {
			 $('[data-bs-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	 $('#tablaProveedores tbody').on('click', 'tr', function () {
	        $(this).toggleClass('selected');
	    });


	// Aqui se agrega los filtros del encabezado
	$('#tablaProveedores thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if ( i == 0 ){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#tablaProveedores thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatos($(this).parent().index(), this.value);
	});
		
	function filtraDatos(columna, texto) {
		tablaProveedores
			.column(columna)
	        .search(texto)
	        .draw();
	}



	function abreModal(opcion, id) {
		$( "#btnSometer" ).show();
		$( "#rfc" ).prop( "disabled", false );
		$( "#idProveedor" ).prop( "disabled", false );
		$( "#usrAcceso" ).prop( "disabled", false );
		$( "#remplazarCertificacion" ).val( "INICIO");
		posOrden = 0;
		
		switch(opcion) {
		case "nuevo" : 
			$('#idRegistro_Catalogo').val(0);
			
			iniciaFormCatalogo();
			
			cargaRegimenFisca('');
			cargaNacionalidad('');
			cargaRazon('');
			cargaFormaPago('');
			cargaPagoDolares('NON')
			cargaPagoPesos('NON')
			cargaTipoConfirmacion('')
			cargaEstados('');
			cargaCentros('');
			
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_NEW_CATALOGO;
			$('#myModalDetalle').modal('show');
			break;
		case "editar" :
			$('#idRegistro_Catalogo').val(id);
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_EDIT_CATALOGO;
			iniciaFormCatalogo();
			buscaCatalogo(id, 'editar');
			
			break;		
		case "ver" :
			$('#idRegistro_Catalogo').val(id);
			document.getElementById("modal-title-catalogo").innerHTML = TITLE_VIEW_CATALOGO;
			iniciaFormCatalogo();
			buscaCatalogo(id, 'ver');
			// $('#myModalDetalle').modal('show');
			break;		
		case "validarIMSS" :
			$( "#tipoCertificado_Validar" ).val( "IMSS");
			obtenerProveedor('IMSS');
			break;	
		case "validarSAT" :
			$( "#tipoCertificado_Validar" ).val( "SAT");
			obtenerProveedor('SAT');
			break;	
		case "enviarAcceso" :
			buscaInfoAcceso();
			iniciaFormCatalogoEnviar();
			break;	
		default :
		}
	}
	
	function iniciaFormCatalogo() {
		
		/* Inicializa combos en el modal */ 

		
		$('#razonProveedor').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#razonProveedor').val(''); // Selecciona primer valor del combo
		$('#razonProveedor').trigger('change'); //Refresca el combo
		
		$('#tipoProveedor').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#tipoProveedor').val(''); // Selecciona primer valor del combo
		$('#tipoProveedor').trigger('change'); //Refresca el combo
		
		$('#formaPago').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#formaPago').val('WIR'); // Selecciona primer valor del combo
		$('#formaPago').trigger('change'); //Refresca el combo
		
		$('#pagoDolares').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#pagoDolares').val('NON'); // Selecciona primer valor del combo
		$('#pagoDolares').trigger('change'); //Refresca el combo
		
		
		$('#pagoPesos').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#pagoPesos').val('NON'); // Selecciona primer valor del combo
		$('#pagoPesos').trigger('change'); //Refresca el combo
		
		$('#tipoConfirmacion').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#tipoConfirmacion').val('0'); // Selecciona primer valor del combo
		$('#tipoConfirmacion').trigger('change'); //Refresca el combo
		
		$('#regimenFiscal').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#regimenFiscal').val(''); // Selecciona primer valor del combo
		$('#regimenFiscal').trigger('change'); //Refresca el combo
		
		$('#ciudad').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#ciudad').trigger('change'); //Refresca el combo
		
		$('#estado').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#estado').trigger('change'); //Refresca el combo
		
		
		/* Reset al Formulario */ 
		$("#frmProveedores").find('.has-success').removeClass("has-success");
	    $("#frmProveedores").find('.has-error').removeClass("has-error");
		$('#frmProveedores')[0].reset(); 
		$('#frmProveedores').removeClass("was-validated"); 
		
		
		$("#tabInfoPrincipal").attr('aria-selected', 'true');
		$("#tabInfoPrincipal").addClass("active");
		$("#infoPrincipal").addClass("active");
		
		$("#tabDatosBancarios").attr('aria-selected', 'false');
		$(" #tabDatosBancarios").removeClass("active");
		$("#datosBancarios").removeClass("active");
		
		$("#tabCalificacionProveedor").attr('aria-selected', 'false');
		$("#tabCalificacionProveedor").removeClass("active");
		$("#calificacionProveedor").removeClass("active");
		
		$("#tabInformacionAcceso").attr('aria-selected', 'false');
		$("#tabInformacionAcceso").removeClass("active");
		$("#informacionAcceso").removeClass("active");
		
		
		$("#accordion1 #heading1 accordion-button").removeClass("collapsed");
		$("#accordion1 #heading1  accordion-button").attr('aria-expanded', 'true');
		$("#accordion1 #collapse1").addClass("show");
		
		$("#accordion1 #heading2 accordion-button").addClass("collapsed");
		$("#accordion1 #heading2 accordion-button").attr('aria-expanded', 'false');
		$("#accordion1 #collapse2").removeClass("show");
		
		$("#accordion1 #heading3 accordion-button").addClass("collapsed");
		$("#accordion1 #heading3 accordion-button").attr('aria-expanded', 'false');
		$("#accordion1 #collapse3").removeClass("show");
		
		$("#accordion1 #heading4 accordion-button").addClass("collapsed");
		$("#accordion1 #heading4 accordion-button").attr('aria-expanded', 'false');
		$("#accordion1 #collapse4").removeClass("show");
		
		$("#accordion2 #heading5 accordion-button").addClass("collapsed");
		$("#accordion2 #heading5 accordion-button").attr('aria-expanded', 'false');
		$("#accordion2 #collapse5").removeClass("show");
		
		$("#accordion2 #heading6 accordion-button").addClass("collapsed");
		$("#accordion2 #heading6 accordion-button").attr('aria-expanded', 'false');
		$("#accordion2 #collapse6").removeClass("show");
		
		$("#accordion2 #heading7 accordion-button").addClass("collapsed");
		$("#accordion2 #heading7 accordion-button").attr('aria-expanded', 'false');
		$("#accordion2 #collapse7").removeClass("show");
				
	}

	
	

	function iniciaFormCatalogoEstatus() {
		
		/* Inicializa combos en el modal */ 

		$('#chkEstatusLbl').html("NO");
		
		/* Reset al Formulario */ 
		$("#form-Catalogo-Estatus").find('.has-success').removeClass("has-success");
	    $("#form-Catalogo-Estatus").find('.has-error').removeClass("has-error");
		$('#form-Catalogo-Estatus')[0].reset(); 
		$('#form-Catalogo-Estatus').removeClass("was-validated"); 
		
	}
	
	function buscaCatalogo(claveRegistro, accion){
		$.ajax({
			url  : '/siarex247/catalogos/proveedores/consultaProveedor.action',
			type : 'POST', 
			data : {
				claveRegistro : claveRegistro
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					if (data.estatusRegistro == 'D'){
						$('#claveRegistro_estatus').val(claveRegistro);
						iniciaFormCatalogoEstatus();
						$('#myModalEstatus').modal('show');
					}else{
						
						if (accion == 'ver'){
							 const frmProveedores = document.getElementById("frmProveedores");
						     const validarHabilitar = frmProveedores.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
						     for(i=0;i<validarHabilitar.length;i++){
						    	 var idElemento = validarHabilitar[i].id;
						    	 $('#'+idElemento).attr('disabled', true);
						     	
						     }
						}else{
							const frmProveedores = document.getElementById("frmProveedores");
						     const validarHabilitar = frmProveedores.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
						     for(i=0;i<validarHabilitar.length;i++){
						    	 var idElemento = validarHabilitar[i].id;
						    	 $('#'+idElemento).attr('disabled', false);
						     	
						     }
						}
						
						$('#idProveedor').val(data.idProveedor);
						$('#nombreContacto').val(data.nombreContacto);
						$('#razonSocial').val(data.razonSocial);
						$('#estado').val(data.estado);
						$('#rfc').val(data.rfc);
						$( "#rfc" ).prop( "disabled", true );
						$( "#idProveedor" ).prop( "disabled", true );
						
						$('#telefono').val(data.telefono);
						$('#email').val(data.email);
						
						
						cargaNacionalidad(data.tipoProveedor);
						cargaRazon(data.razonProveedor);
						cargaFormaPago(data.formaPago);
						cargaPagoDolares(data.pagoDolares)
						cargaPagoPesos(data.pagoPesos)
						cargaTipoConfirmacion(data.tipoConfirmacion)
						cargaEstados(data.estado);
						cargaCiudad(data.estado, data.ciudad);
						cargaCentros(data.centroCostos);
						
						$('#conServicio').val(data.conServicio);
						
						$('#numeroCuentaProveedor').val(data.numeroCuentaProveedor);
						$('#centroCostos').val(data.centroCostos);
						
						$("#bandDescuento").prop('checked', validaCheck(data.bandDescuento));
						$("#notComUsuario").prop('checked', validaCheck(data.notComUsuario));
						$("#notPagoUsuario").prop('checked', validaCheck(data.notPagoUsuario));
						
						
						
						if (data.anexo24 == '1') {
							$('#anexo24').checked = true;
						}
						
						$('#calle').val(data.calle);
						$('#numeroExt').val(data.numeroExt);
						$('#numeroInt').val(data.numeroInt);
						$('#colonia').val(data.colonia);
						$('#codigoPostal').val(data.codigoPostal);
						$('#delegacion').val(data.delegacion);
						$('#ciudad').val(data.ciudad);
						$('#email1').val(data.email1);
						$('#email2').val(data.email2);
						$('#email3').val(data.email3);
						$('#email4').val(data.email4);
						$('#email5').val(data.email5);

		 	 	 		$("#tipoEmail1").prop('checked', validaCheck(data.tipoEmail1));
		 	 	 		$("#tipoEmail2").prop('checked', validaCheck(data.tipoEmail2));
		 	 	 		$("#tipoEmail3").prop('checked', validaCheck(data.tipoEmail3));
		 	 	 		$("#tipoEmail4").prop('checked', validaCheck(data.tipoEmail4));
		 	 	 		$("#tipoEmail5").prop('checked', validaCheck(data.tipoEmail5));
		 	 	 		$("#tipoEmail6").prop('checked', validaCheck(data.tipoEmail6));
		 	 	 		$("#tipoEmail7").prop('checked', validaCheck(data.tipoEmail7));
		 	 	 		$("#tipoEmail8").prop('checked', validaCheck(data.tipoEmail8));
		 	 	 		$("#tipoEmail9").prop('checked', validaCheck(data.tipoEmail9));
		 	 	 		$("#tipoEmail10").prop('checked', validaCheck(data.tipoEmail10));
		 	 	 		
						$('#limiteTolerancia').val(data.limiteTolerancia);
						$('#limiteComplemento').val(data.limiteComplemento);
						$('#AMERICANOS_SERIE').val(data.AMERICANOS_SERIE);
						$('#AMERICANOS_FOLIO').val(data.AMERICANOS_FOLIO);
						cargaRegimenFisca(data.regimenFiscal);
						$('#numRegistro').val(data.numRegistro);
						
						$("#PERMITIR_ACCESO_GENERADOR").prop('checked', validaCheck(data.AMERICANOS_ACCESO));
						$("#bandImss").prop('checked', validaCheck(data.bandIMSS));
						
						$("#bandSat").prop('checked', validaCheck(data.bandSAT));
						$("#bandCartaPorte").prop('checked', validaCheck(data.cartaPorte));
						
						$('#banco').val(data.banco);
						$('#sucursal').val(data.sucursal);
						$('#nombreSucursal').val(data.nombreSucursal);
						$('#numeroCuenta').val(data.numeroCuenta);
						$('#cuentaClabe').val(data.cuentaClabe);
						$('#numeroConvenio').val(data.numeroConvenio);
						$('#moneda').val(data.moneda);
						$('#bancoDollar').val(data.bancoDollar);
						$('#sucursalDollar').val(data.sucursalDollar);
						$('#nombreSucursalDollar').val(data.nombreSucursalDollar);
						$('#numeroCuentaDollar').val(data.numeroCuentaDollar);
						$('#cuentaClabeDollar').val(data.cuentaClabeDollar);
						$('#numeroConvenioDollar').val(data.numeroConvenioDollar);
						$('#monedaDollar').val(data.monedaDollar);
						$('#abaDollar').val(data.abaDollar);
						$('#switfCodeDollar').val(data.switfCodeDollar);
						$('#bancoOtro').val(data.bancoOtro);
						$('#sucursalOtro').val(data.sucursalOtro);
						$('#nombreSucursalOtro').val(data.nombreSucursalOtro);
						$('#numeroCuentaOtro').val(data.numeroCuentaOtro);
						$('#cuentaClabeOtro').val(data.cuentaClabeOtro);
						$('#numeroConvenioOtro').val(data.numeroConvenioOtro);
						$('#monedaOtro').val(data.monedaOtro);
						$('#abaOtro').val(data.abaOtro);
						$('#switfCodeOtro').val(data.switfCodeOtro);
						
						if (data.tieneIMSS == 'S'){
							$('#txtCertificaIMSS').val('CON CERTIFICADO');
						}else{
							$('#txtCertificaIMSS').val('SIN CERTIFICADO');
						}
						
						if (data.tieneSAT == 'S'){
							$('#txtCertificaSAT').val('CON CERTIFICADO');
						}else {
							$('#txtCertificaSAT').val('SIN CERTIFICADO');
						}
						
						if (data.tieneConfidencial == 'S'){
							$('#txtConfidencial').val('CON CERTIFICADO');
						}else{
							$('#txtConfidencial').val('SIN CERTIFICADO');
						}
						
						
						/*
						if (data.usuariosForm.idUsuario == undefined){
							$( "#usrAcceso" ).prop( "disabled", false );	
						}else{
							$( "#usrAcceso" ).prop( "disabled", true );
						}
						*/
						
						$('#usrAcceso').val(data.usuariosForm.idUsuario);
						 if (data.usuariosForm.estatusRegistro == 'A'){
							$("#permitirAcceso").prop('checked', true);	
						 }
						
							
						if (accion == 'ver'){
							$( "#btnSometer" ).hide();
						}else{
							$( "#btnSometer" ).show();
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
	
	

	function eliminaCatalogo(claveRegistro, razonSocial){
		try{
			
			Swal.fire({
				  icon : 'question',
				  width: 600,
				  title: TITLE_DELETE_CATALOGO,
				  text: LABEL_PROVEEDORES + ' : '+razonSocial,
				  showDenyButton: true,
				  showCancelButton: false,
				  confirmButtonText: BTN_ACEPTAR_MENU,
				  denyButtonText: BTN_CANCELAR_MENU,
				}).then((result) => {
				  if (result.isConfirmed) {
					  $.ajax({
				           url:  '/siarex247/catalogos/proveedores/eliminaProveedores.action',
				           type: 'POST',
				            dataType : 'json',
				            data : {
				            	claveRegistro : claveRegistro
				            },
						    success: function(data){
						    	
						      if (data.codError == "000"){
						    		Swal.fire({
			 						  title: MSG_OPERACION_EXITOSA_MENU,
			 						  // html: '<p>El proveedor ha sido desactivado. </p>',
			 						     html: '<p>'+MSG_REGISTRO_ELIMINADO_MENU+' </p>',
										  showCancelButton: false,
										  confirmButtonText: BTN_ACEPTAR_MENU,
										  icon: 'success'
			 						}).then((result) => {
			 							$('#tablaProveedores').DataTable().ajax.reload(null,false);
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
			   var AMERICANOS_FOLIO = $('#AMERICANOS_FOLIO').val();
			   if (AMERICANOS_FOLIO == ''){
				   $('#AMERICANOS_FOLIO').val(0);
			   }
			   
			   var codigoPostal = $('#codigoPostal').val();
			   if (codigoPostal == ''){
				   $('#codigoPostal').val(0);
			   }
			   
			   var tipoProveedor = $('#tipoProveedor').val();
			   if (tipoProveedor == 'USA'){
				   $('#rfc').val('INTERNACIONAL');
			   }
			   
	            var formData = new FormData(document.getElementById("frmProveedores"));
	            $.ajax({
	            	url: '/siarex247/catalogos/proveedores/altaProveedores.action',
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
	 						 // html: '<p>Alta Exitosa</p>',
	 						  	html: '<p>'+MSG_ALTA_EXITOSA_MENU+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							 $('#myModalDetalle').modal('hide');
	  	 						$('#tablaProveedores').DataTable().ajax.reload(null,false);
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
	
	
	function actualizarCatalogo(){
		try{
			   var AMERICANOS_FOLIO = $('#AMERICANOS_FOLIO').val();
			   var rfc = $('#rfc').val();
			   if (AMERICANOS_FOLIO == ''){
				   $('#AMERICANOS_FOLIO').val(0);
			   }
			   
			   var codigoPostal = $('#codigoPostal').val();
			   if (codigoPostal == ''){
				   $('#codigoPostal').val(0);
			   }
			   
	            var formData = new FormData(document.getElementById("frmProveedores"));
	            $.ajax({
	            	url: '/siarex247/catalogos/proveedores/modificaProveedores.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	if (data.codError == '002') {
	            		tieneArchivos(data.mensajeError, rfc);
	            	}else  if (data.codError == '000') {
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 // html: '<p>Registro Actualizado</p>',
	 						  	html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalDetalle').modal('hide');
	  	 						$('#tablaProveedores').DataTable().ajax.reload(null,false);
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
	
	
	
	function actualizarEstatus(){
		try{
			
			var bandEstatus = $("#chkEstatus").prop('checked');
			if (bandEstatus){
				var formData = new FormData(document.getElementById("form-Catalogo-Estatus"));
	            $.ajax({
	            	url: '/siarex247/catalogos/proveedores/actualizaEstatus.action',
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
	 						  // html: '<p>Registro Actualizado</p>',
	 						  	  html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalEstatus').modal('hide');
	  	 						$('#tablaProveedores').DataTable().ajax.reload(null,false);
	 						});
					} else {
						Swal.fire({
  			                title: MSG_ERROR_OPERACION_MENU,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
	             });
			}else{
				$('#myModalEstatus').modal('hide');
			}
			
		}
		catch(e){
			alert('actualizarEstatus()_'+e);
		}
    }
	
	
	 function tieneArchivos(mensajeError, rfc){
		 try{
				Swal.fire({
					  icon : 'question',
					  title: mensajeError,
					  text: 'RFC : '+rfc,
					  showDenyButton: true,
					  showCancelButton: false,
					  confirmButtonText: BTN_ACEPTAR_MENU,
					  denyButtonText: BTN_CANCELAR_MENU,
					}).then((result) => {
					  if (result.isConfirmed) {
						  $('#remplazarCertificacion').val('REMPLAZA');
						  actualizarCatalogo();
					  } else if (result.isDenied) {
						  $('#remplazarCertificacion').val('NO_REMPLAZA');
						  actualizarCatalogo();
					  }
					})

		 }catch(e){
			 alert('tieneArchivos()_'+e);
		 }
	 }

	function validaCheck(valCheck){
		try{
			if (valCheck == ''){
				return false;
			}else if (valCheck == 'S'){
				return true;
			}else{
				return false;
			}
			
		}catch(e){
			alert('validaCheck()_'+e);
		}
	}
	
	
	
	
	function exportarCertificados(){
		var idProveedores = '';
		try{
			var dataSelect = tablaProveedores.rows('.selected').data(); 
			$.each(dataSelect, function(key, row) {
				idProveedores+= row.claveRegistro + ";"		
		    });
			
			if (idProveedores == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                //html: '<p>Debe seleccionar al menos un registro para continuar.</p>',	
		                html: '<p>'+VALIDACION_ETQ108+'</p>',
		                icon: 'info'
		            });
			}else{
				// window.open('/siarex247/excel/exportCertificados.action?idProveedores='+idProveedores,'zipFile');
				 document.getElementById('idProveedores').value = idProveedores;
			     document.frmExportarCertificados.submit();

			}
			
		}catch(e){
			alert('exportarCertificados()_'+e);
		}
	}
	
	
	function eliminarCertificados(){
		var idProveedores = '';
		try{
			var dataSelect = tablaProveedores.rows('.selected').data(); 
			$.each(dataSelect, function(key, row) {
				idProveedores+= row.claveRegistro + ";"		
		    });
			
			if (idProveedores == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                // html: '<p>Debe seleccionar al menos un registro para continuar.</p>',
		                html: '<p>'+VALIDACION_ETQ108+'</p>',
		                icon: 'info'
		            });
			}else{
				Swal.fire({
					  icon : 'question',
					  //title: '¿Estas seguro de eliminar los certificados ?',
					  title: LABEL_ELIMINAR_CERTIFICADOS,
					 // text: 'Razón Social : '+razonSocial,
					  showDenyButton: true,
					  showCancelButton: false,
					  confirmButtonText: BTN_ACEPTAR_MENU,
					  denyButtonText: BTN_CANCELAR_MENU,
					}).then((result) => {
					  if (result.isConfirmed) {
						  $.ajax({
					           url:  '/siarex247/catalogos/proveedores/eliminarCertificados.action',
					           type: 'POST',
					            dataType : 'json',
					            data : {
					            	idProveedores : idProveedores
					            },
							    success: function(data){
							    	
							    	if (data.codError == "000"){
							    		Swal.fire({
				 						  title: MSG_OPERACION_EXITOSA_MENU,
				 						 //  html: '<p>Los certificados se han eliminado satisfactoriamente. </p>',
				 						  	  html: '<p>'+MENSAJE_ELIMINAR_CERTIFICADOS+' </p>',
											  showCancelButton: false,
											  confirmButtonText: BTN_ACEPTAR_MENU,
											  icon: 'success'
				 						}).then((result) => {
				 							$('#tablaProveedores').DataTable().ajax.reload(null,false);
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

			}
			
		}catch(e){
			alert('exportarCertificados()_'+e);
		}
	}
	
	
	function generarCertificado(tipoArchivo, claveRegistro, rfcProveedor){
		  try{
	         document.getElementById('tipoArchivo').value = tipoArchivo;
	         document.getElementById('claveRegistroCertificado').value = claveRegistro;
	         document.getElementById('rfcProveedor').value = rfcProveedor;
	         document.frmAbrirFactura.submit();
		  }
		  catch(e){
			  alert('generarCertificado()_'+e);
		  }
		}

	
	
	

	function iniciaFormCatalogoEnviar() {
		
		$("#form-Enviar-Acceso").find('.has-success').removeClass("has-success");
	    $("#form-Enviar-Acceso").find('.has-error').removeClass("has-error");
		$('#form-Enviar-Acceso')[0].reset(); 
		$('#form-Enviar-Acceso').removeClass("was-validated"); 
		
	}
	
	
	function buscaInfoAcceso(){
		var idProveedores = '';
		try{
		   		var dataSelect = tablaProveedores.rows('.selected').data(); 
				$.each(dataSelect, function(key, row) {
					idProveedores+= row.claveRegistro + ";"		
				});
					
				if (idProveedores == ''){
					Swal.fire({
				        title: MSG_ERROR_OPERACION_MENU,
				        //html: '<p>Debe seleccionar un registro para continuar.</p>',	
				        html: '<p>'+VALIDACION_ETQ108+'</p>',
				        icon: 'info'
				     });
				}else{
					var cadRegistro = idProveedores.split(';');
					if (cadRegistro.length > 2){
						Swal.fire({
					        title: MSG_ERROR_OPERACION_MENU,
					        //html: '<p>Debe seleccionar solo 1 proveedor para continuar.</p>',	
					        html: '<p>'+VALIDACION_ETQ111+'</p>',
					        icon: 'info'
					     });
					}else{
						var claveRegistro = cadRegistro[0];
						$.ajax({
							url  : '/siarex247/catalogos/proveedores/consultaProveedor.action',
							type : 'POST', 
							data : {
								claveRegistro : claveRegistro
							},
							dataType : 'json',
							success  : function(data) {
								if($.isEmptyObject(data)) {
								   
								} else {
									$('#claveRegistro_Enviar').val(claveRegistro);
									$('#txtIdProveedor_Enviar').val(data.idProveedor);
									$('#txtRazonSocial_Enviar').val(data.razonSocial);
									$('#myModalEnviar').modal('show');
								}
							},
							error : function(xhr, ajaxOptions, thrownError) {
								alert('buscaInfoAcceso()_'+thrownError);
							}
						});							
					}
				}
	   }catch(e){
		   alert('buscaInfoAcceso()_'+e);
	   }
		
		
		
    }
	
	

	function enviarAcceso(){
		try{
			    var formData = new FormData(document.getElementById("form-Enviar-Acceso"));
	            $.ajax({
	            	url: '/siarex247/catalogos/proveedores/envioAcceso.action',
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
	 						 // html: '<p>Se ha enviado la información de acceso al proveedor.</p>',
	 						 html: '<p>'+MENSAJE_ENVIO_INFORMACION+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							 $('#myModalEnviar').modal('hide');
	  	 						$('#tablaProveedores').DataTable().ajax.reload(null,false);
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
			alert('enviarAcceso()_'+e);
		}
    }
	
	
	
	