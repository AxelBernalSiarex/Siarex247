
 var tablaDetalleEmitidos = null;

	$(document).ready(function() {
		try {
			tablaDetalleEmitidos = $('#tablaDetalleEmitidos').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 15,
				lengthChange: false,
//				dom: 'Blfrtip',
//				dom: "<'row mx-0'<'col-md-12'QB><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				//dom: "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				dom		: '<"top">t<"bottom"ilp<"clear">>',
				ordering    : false,
				serverSide	: true,
				fixedHeader : true,
				orderCellsTop: true,
				info		: true,
				select      : true,
				stateSave	: false, 
				order       : [ [ 0, 'asc' ] ],	
				buttons: null,

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
				
				ajax: {
				  url: '/siarex247/cumplimientoFiscal/boveda/emitidos/detalleBoveda.action',
				  beforeSend: function () { 
					$('#overSeccion_Boveda_Emitidos').css({display:'block'}); 
					$('#btnRefrescar_Emitidos').prop('disabled', false);
					
				  },
				  complete  : function () { 
						$('#overSeccion_Boveda_Emitidos').css({display:'none'}); 
						$('#btnRefrescar_Emitidos').prop('disabled', true); 
				 },
				  type: 'POST',
				  data: function (d) {
				    // ===== Base =====
				    d.fechaInicial    = obtenerFechaIni_Emitidos();
				    d.fechaFinal      = obtenerFechaFin_Emitidos();
				    d.rfc             = $('#rfcGridFilterE').val() || obtenerRFC_Emitidos();
				    d.razonSocial = ($('#razonFilterInputE').val() || '').trim() || obtenerRazon_Emitidos();
				    d.uuid            = obtenerUUID_Emitidos();
				    d.serie           = obtenerSerie_Emitidos();
				    d.folio           = obtenerFolio_Emitidos();
					
					const domTipo = ($('#tipoComprobante_Emitidos').val() || '').trim(); // 0 / I / E / P / T
					    d.tipoComprobante = __toPayloadTipo(domTipo); // => 'ALL' o el código
					    d.tipoOperator    = ($('#tipoOperatorE').val() || 'equals').toLowerCase();

				    // ===== Texto =====
				    d.rfcOperator   = ($('#rfcOperatorE').val()   || 'contains').toLowerCase();
				    d.razonOperator = ($('#razonOperatorE').val() || 'contains').toLowerCase();
				    d.serieOperator = ($('#serieOperatorE').val() || 'contains').toLowerCase();
				    d.uuidOperator  = ($('#uuidOperatorE').val()  || 'contains').toLowerCase();

				    // ===== Numéricos =====
				    d.folioOperator = $('#folioOperatorE').val() || 'eq';
				    d.folioV1       = $('#folioFilter1E').val();
				    d.folioV2       = $('#folioFilter2E').val();

				    d.totalOperator = $('#totalOperatorE').val() || 'eq';
				    d.totalV1       = $('#totalFilter1E').val();
				    d.totalV2       = $('#totalFilter2E').val();

				    d.subOperator   = $('#subOperatorE').val()   || 'eq';
				    d.subV1         = $('#subFilter1E').val();
				    d.subV2         = $('#subFilter2E').val();

				    // 🔧 NOMBRES QUE ESPERA EL BACKEND
				    d.ivaRetOperator = $('#retOperatorE').val()  || 'eq';
				    d.ivaRetV1       = $('#retFilter1E').val();
				    d.ivaRetV2       = $('#retFilter2E').val();

				    d.ivaOperator    = $('#trasOperatorE').val() || 'eq';
				    d.ivaV1          = $('#trasFilter1E').val();
				    d.ivaV2          = $('#trasFilter2E').val();

				    // ===== Fecha =====
				    d.dateOperator = ($('#dateOperatorE').val() || 'eq').toLowerCase();
				    d.dateV1       = $('#dateFilter1E').val();
				    d.dateV2       = $('#dateFilter2E').val();
					 
					
					
				  },
				  complete: function () {
				    $('#overSeccion_Boveda_Emitidos').css({display:'none'});
				    // apaga el flag después del request
				    window.__emitidosForzarTipoVacio = false;
				  },
					type: 'POST'
					},
					aoColumns : [
						{ mData: null, "sClass": "alinearCentro"},
						{ mData: "rfcReceptor", "sClass": "alinearCentro"},
						{ mData: "razonSocialReceptor"},
						{ mData: "serie", "sClass": "alinearCentro"},
						{ mData: "tipoComprobante", "sClass": "alinearCentro"},
						{ mData: "folio", "sClass": "alinearCentro"},
						{ mData: "total", "sClass": "alinearDerecha"},
						{ mData: "subTotal", "sClass": "alinearDerecha"},
						{ mData: "totalImpuestoRetenido", "sClass": "alinearDerecha"},
						{ mData: "totalImpuestoTraslado", "sClass": "alinearDerecha"},
						{ mData: null, "sClass": "alinearCentro"},
						{ mData: null, "sClass": "alinearCentro"},
						{ mData: "uuid"},
						{ mData: "fechaFactura"},
						
					],
					
					columnDefs: [
						 {
		                    targets: 10,
		                    render: function (data, type, row) {
		                    	rowElement = '<img class="" src="/theme-falcon/repse247/img/xml26.png" alt="" style="cursor: pointer;" title="Ver XML" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo XML" onclick="generaArchivoEmitidos(\'XML\', \'' + row.idRegistro+ '\');" />';
		                        return rowElement;
		                      }
		                 },
		                 {
			                 targets: 11,
		                    render: function (data, type, row) {
		                    	rowElement = '<img class="" src="/theme-falcon/repse247/img/pdf26.png" alt="" style="cursor: pointer;" title="Ver PDF" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="generaArchivoEmitidos(\'PDF\', \'' + row.idRegistro+ '\');" />';
		                        return rowElement;
		                      }
		                 },
		                 {	targets: 12,
						        render: function (data, type, row) {
						        	rowElement = '<a href="javascript:generaArchivoEmitidos(\'PDF\', \'' + row.idRegistro+ '\');">' + row.uuid + '</a>';
						            return rowElement;
						        }
						    },
					    {
					        targets: 0,
					        render: function (data, type, row) {
					            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
					            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
					            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
					            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaBovedaEmitidos(\'' + row.uuid + '\');">'+BTN_ELIMINAR_MENU+'</a>';
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
		
		tablaDetalleEmitidos.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	function abreModal_Emitidos(opcion, id) { 
		switch(opcion) {
		case "nuevo" :
			$("#btnSometer_Emitidos").prop('disabled', false);
			iniciaFormEmitidos();    	
			$('#myModalDetalle_Emitidos').modal('show');
			break;
		default :
		}
	}
	
	


	function iniciaFormEmitidos(){
		/* Reset al Formulario */ 
		$("#frm-Carga-XML-Emitidos").find('.has-success').removeClass("has-success");
	    $("#frm-Carga-XML-Emitidos").find('.has-error').removeClass("has-error");
		$('#frm-Carga-XML-Emitidos')[0].reset(); 
		$('#frm-Carga-XML-Emitidos').removeClass("was-validated"); 
		   
	}

	function generaArchivoEmitidos(tipo, idRegistro){
		 document.getElementById('idRegistroP_Emitidos').value = idRegistro;
		 document.getElementById('tipoArchivoP_Emitidos').value = tipo;
	     document.frmBovedaEmitidos.submit();
	}
	
	function procesaValidarEmitidos(){
			
			try{
				$("#btnSometer_Emitidos").prop('disabled', true);
		            var formData = new FormData(document.getElementById("frm-Carga-XML-Emitidos"));
		            formData.append("dato", "valor");

		            $.ajax({
		            	url: '/siarex247/cumplimientoFiscal/boveda/emitidos/iniciaCargaXML.action',
		                dataType: "json",
		                beforeSend: function( xhr ) {
		        			$('#overSeccion_Emitidos').css({display:'block'});
		        		},
		                type: "post",
		                data: formData,
		                cache: false,
		                contentType: false,
			    		processData: false,
			    		complete: function(jqXHR, textStatus){
				    		  $('#overSeccion_Emitidos').css({display:'none'});
				    		  $("#btnSometer_Emitidos").prop('disabled', false);
					    },
					    
		            }).done(function(data){
		            	
			            if (data.ESTATUS == 'OK') {
		 					Swal.fire({
		 						  icon: 'success',
		 						  title: MSG_OPERACION_EXITOSA_MENU,
		 						 // text: data.MENSAJE,
		 						  html: '<p>'+data.MENSAJE+' </p>',
		 						  showCancelButton: false,
		 						  confirmButtonText: BTN_ACEPTAR_MENU,
		 						  denyButtonText: BTN_CANCELAR_MENU,
		 						}).then((result) => {
		 						  if (result.isConfirmed) {
		 							 $('#myModalDetalle_Emitidos').modal('hide');
		  	 						 $('#tablaDetalleEmitidos').DataTable().ajax.reload(null,false);
		 						  }
		 					});
						}else if (data.ESTATUS == 'OK_CON_ERROR'){
							Swal.fire({
		 						  icon: 'success',
		 						  title: MSG_OPERACION_EXITOSA_MENU,
		 						  // text: data.MENSAJE,
		 						 html: '<p>'+data.MENSAJE+' </p>',
		 						  showCancelButton: false,
		 						  confirmButtonText: BTN_ACEPTAR_MENU,
		 						  denyButtonText: BTN_CANCELAR_MENU,
		 						}).then((result) => {
		 						  if (result.isConfirmed) {
		 							 $('#myModalDetalle_Emitidos').modal('hide');
		  	 						 $('#tablaDetalleEmitidos').DataTable().ajax.reload(null,false);
		 						  }
		 					});
						} else {
						  Swal.fire({
							  icon: 'error',
							  title: '¡Error en Operacion!',
							  text: data.MENSAJE
						  });
						}
		         });
			}
			catch(e){
				e = null;
			}
	}
	
	function eliminaBovedaEmitidos(idRegistro){
		try{
			
			if (idRegistro == 'MULTIPLE'){
				idRegistro =  getElementosFilterEmitidos();
			}
			
			if (idRegistro == ''){
				Swal.fire({
	    			title: MSG_ERROR_OPERACION_MENU,
	                	//html: '<p>¡ Debe seleccionar al menos un registro para eliminar !</p>',	
	    				html: '<p>'+LABEL_BOVEDA_TEXT3+'</p>',
	                	icon: 'info'
	    		});
			}else {

				Swal.fire({
					  icon : 'question',
					 // title: '¿Estas seguro de eliminar el registro de Boveda ?',
					  title: TITLE_DELETE_CATALOGO,
					  // text: 'UUID : '+uuidBoveda,
					  showDenyButton: true,
					  showCancelButton: false,
					  confirmButtonText: BTN_ACEPTAR_MENU,
					  denyButtonText: BTN_CANCELAR_MENU,
					}).then((result) => {
					  if (result.isConfirmed) {
						  $.ajax({
					           url:  '/siarex247/cumplimientoFiscal/boveda/emitidos/eliminarBoveda.action',
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
				 							$('#tablaDetalleEmitidos').DataTable().ajax.reload(null,false);
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
			alert('eliminaCatalogo()_'+e);
		}
		
	}
	
	
	function exportExcelEmitidos(){
	  try{
	    // ===== Valores base (los que ya usas) =====
	    var fechaInicial    = obtenerFechaIni_Emitidos();
	    var fechaFinal      = obtenerFechaFin_Emitidos();
	    var rfc             = obtenerRFC_Emitidos();
	    var razonSocial     = obtenerRazon_Emitidos();
	    var uuid            = obtenerUUID_Emitidos();
	    var serie           = obtenerSerie_Emitidos();
	    var folio           = obtenerFolio_Emitidos();

	    // Tipo: prioriza input del thead; si no hay, usa el select; fallback ALL
	    var typedTipo       = ($.trim($('#tipoFilterInputE').val() || '') );
	    var selectTipo      = ($.trim($('#tipoComprobante_Emitidos').val() || '') );
	    var tipoComprobante = typedTipo || selectTipo || 'ALL';

	    // ===== Operadores DX-like (misma convención que Recibidos) =====
	    var rfcOp    = ($('#rfcOperatorE').val()   || 'contains').toLowerCase();
	    var razonOp  = ($('#razonOperatorE').val() || 'contains').toLowerCase();
	    var serieOp  = ($('#serieOperatorE').val() || 'contains').toLowerCase();
	    var tipoOp   = ($('#tipoOperatorE').val()  || 'equals').toLowerCase();
	    var uuidOp   = ($('#uuidOperatorE').val()  || 'contains').toLowerCase();

	    var dateOp   = ($('#dateOperatorE').val() || 'eq').toLowerCase();
	    var dateV1   = $('#dateFilter1E').val() || '';
	    var dateV2   = $('#dateFilter2E').val() || '';

	    var folioOp  = ($('#folioOperatorE').val() || 'eq').toLowerCase();
	    var folioV1  = $('#folioFilter1E').val() || '';
	    var folioV2  = $('#folioFilter2E').val() || '';

	    var totalOp  = ($('#totalOperatorE').val() || 'eq').toLowerCase();
	    var totalV1  = $('#totalFilter1E').val() || '';
	    var totalV2  = $('#totalFilter2E').val() || '';

	    var subOp    = ($('#subOperatorE').val() || 'eq').toLowerCase();
	    var subV1    = $('#subFilter1E').val() || '';
	    var subV2    = $('#subFilter2E').val() || '';

	    // Nota: en tu backend usas “ivaRet” para retenidos y “iva” para traslados
	    var ivaRetOp = ($('#retOperatorE').val() || 'eq').toLowerCase();
	    var ivaRetV1 = $('#retFilter1E').val() || '';
	    var ivaRetV2 = $('#retFilter2E').val() || '';

	    var ivaOp    = ($('#trasOperatorE').val() || 'eq').toLowerCase();
	    var ivaV1    = $('#trasFilter1E').val() || '';
	    var ivaV2    = $('#trasFilter2E').val() || '';

	    // ===== Helper para asegurar / setear hiddens =====
	    function put(id, name, val){
	      var el = document.getElementById(id);
	      if(!el){
	        el = document.createElement('input');
	        el.type = 'hidden';
	        el.id   = id;
	        el.name = name;
	        document.frmExportarDetalleExcel_Emitidos.appendChild(el);
	      }
	      el.value = (val == null ? '' : val);
	    }

	    // ===== Llenar form =====
	    put('rfc_Emitidos_Exportar',             'rfc',             rfc);
	    put('razonSocial_Emitidos_Exportar',     'razonSocial',     razonSocial);
	    put('folio_Emitidos_Exportar',           'folio',           folio);
	    put('serie_Emitidos_Exportar',           'serie',           serie);          // ← corregido
	    put('fechaInicial_Emitidos_Exportar',    'fechaInicial',    fechaInicial);
	    put('fechaFinal_Emitidos_Exportar',      'fechaFinal',      fechaFinal);
	    put('tipoComprobante_Emitidos_Exportar', 'tipoComprobante', tipoComprobante);
	    put('uuid_Emitidos_Exportar',            'uuid',            uuid);

	    // Operadores / valores
	    put('rfcOperator_Emitidos_Exportar',     'rfcOperator',     rfcOp);
	    put('razonOperator_Emitidos_Exportar',   'razonOperator',   razonOp);
	    put('serieOperator_Emitidos_Exportar',   'serieOperator',   serieOp);
	    put('tipoOperator_Emitidos_Exportar',    'tipoOperator',    tipoOp);
	    put('uuidOperator_Emitidos_Exportar',    'uuidOperator',    uuidOp);

	    put('dateOperator_Emitidos_Exportar',    'dateOperator',    dateOp);
	    put('dateV1_Emitidos_Exportar',          'dateV1',          dateV1);
	    put('dateV2_Emitidos_Exportar',          'dateV2',          dateV2);

	    put('folioOperator_Emitidos_Exportar',   'folioOperator',   folioOp);
	    put('folioV1_Emitidos_Exportar',         'folioV1',         folioV1);
	    put('folioV2_Emitidos_Exportar',         'folioV2',         folioV2);

	    put('totalOperator_Emitidos_Exportar',   'totalOperator',   totalOp);
	    put('totalV1_Emitidos_Exportar',         'totalV1',         totalV1);
	    put('totalV2_Emitidos_Exportar',         'totalV2',         totalV2);

	    put('subOperator_Emitidos_Exportar',     'subOperator',     subOp);
	    put('subV1_Emitidos_Exportar',           'subV1',           subV1);
	    put('subV2_Emitidos_Exportar',           'subV2',           subV2);

	    put('ivaRetOperator_Emitidos_Exportar',  'ivaRetOperator',  ivaRetOp);
	    put('ivaRetV1_Emitidos_Exportar',        'ivaRetV1',        ivaRetV1);
	    put('ivaRetV2_Emitidos_Exportar',        'ivaRetV2',        ivaRetV2);

	    put('ivaOperator_Emitidos_Exportar',     'ivaOperator',     ivaOp);
	    put('ivaV1_Emitidos_Exportar',           'ivaV1',           ivaV1);
	    put('ivaV2_Emitidos_Exportar',           'ivaV2',           ivaV2);

	    // (Opcional) debug:
	    console.log('[Emitidos/Excel] →', $(document.frmExportarDetalleExcel_Emitidos).serialize());

	    // ===== Enviar =====
	    document.frmExportarDetalleExcel_Emitidos.submit();

	  } catch(e){
	    alert('exportExcelEmitidos(): ' + e);
	  }
	}

	
	

	function iniciaFormNotificacionEmitidos(){
		/* Reset al Formulario */ 
		$("#frmConfirmEmail_Emitidos").find('.has-success').removeClass("has-success");
	    $("#frmConfirmEmail_Emitidos").find('.has-error').removeClass("has-error");
		$('#frmConfirmEmail_Emitidos')[0].reset(); 
		$('#frmConfirmEmail_Emitidos').removeClass("was-validated"); 
		   
	}
	
	function consultarTotalesEmitidos() {
		try{
			
					var fechaInicial = obtenerFechaIni_Emitidos();
					var fechaFinal = obtenerFechaFin_Emitidos();
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
									iniciaFormDescargaCFDI_Emitidos();
									asignarCorreoEmitidos();
									$('#modalDescargaCFDI_Emitidos').modal('show');
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

			
			
			 
		}catch(e){
			alert('consultarTotalesEmitidos()_'+e);
		}
	}


	/*
	function consultarTotalesEmitidos(){
		try{
			var uuidRegistro = '';
	        var dataSelect = tablaDetalle.rows('.selected').data(); 
			$.each(dataSelect, function(key, row) {
				uuidRegistro+= row.uuid + ";"		
		    });
			var arrElementos = uuidRegistro.split(';');
			if (arrElementos.length > 50000){
				Swal.fire({
	    			title: MSG_ERROR_OPERACION_MENU,
	    				html: '<p>'+LABEL_BOVEDA_TEXT4+'</p>',
	                	icon: 'info'
	    		});
			}else{
				if (arrElementos.length == 1){

					var fechaInicial  = obtenerFechaIni_Emitidos();
					var fechaFinal = obtenerFechaFin_Emitidos();
					var rfc = obtenerRFC_Emitidos();
					var razonSocial = obtenerRazon_Emitidos();
					var uuid = obtenerUUID_Emitidos();
					var tipoComprobante = obtenerComprobante_Emitidos();
					var serie = obtenerSerie_Emitidos();
					var folio  = obtenerFolio_Emitidos();
					
					$.ajax({
						url  : '/siarex247/cumplimientoFiscal/boveda/emitidos/obtenerTotales.action',
						type : 'POST', 
						data : {
							rfc : rfc,
							razonSocial : razonSocial,
							fechaInicial : fechaInicial,
							fechaFinal : fechaFinal,
							uuid : uuid,
							tipoComprobante : tipoComprobante,
							serie : serie,
							folio : folio,
							idRegistro : uuidRegistro
						},
						dataType : 'json',
						success  : function(data) {
							if($.isEmptyObject(data)) {
							   
							} else {
								if (data.codError == '000'){
									descargarCFDIEmitidos();
								}else{
									iniciaFormNotificacionEmitidos();
									$("#myModalNotifica_Emitidos").modal("show");
									$("#emailNotificacion_Emitidos").focus();
								}
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert('consultarTotalesEmitidos()_'+thrownError);
						}
					});	
				}else{
					descargarCFDIEmitidos();
					
				}
			}
		}catch(e){
			alert('consultarTotalesEmitidos()_'+e);
		}
	}
	*/
	
	 
	function descargarCFDIEmitidos(){
	  try{
	    // ===== 1) UUIDs seleccionados =====
	    var uuidRegistro = '';
	    var table = window.tablaDetalleEmitidos || $('#tablaDetalleEmitidos').DataTable();
	    var dataSelect = table.rows('.selected').data();
	    $.each(dataSelect, function(_, row) {
	      uuidRegistro += ((row.uuid || row.UUID || '') + ';');
	    });

	    // ===== 2) Filtros base =====
	    var fechaInicial    = obtenerFechaIni_Emitidos();
	    var fechaFinal      = obtenerFechaFin_Emitidos();
	    var rfc             = obtenerRFC_Emitidos();
	    var razonSocial     = obtenerRazon_Emitidos();
	    var uuid            = obtenerUUID_Emitidos();
	    var serie           = obtenerSerie_Emitidos();
	    var folio           = obtenerFolio_Emitidos();

	    // TIPO: del <select> o, si está vacío, del input del header; default ALL
	    var tipoUI = ($.trim($('#tipoComprobante_Emitidos').val() || '') ||
	                  $.trim($('#tipoFilterInputE').val() || ''));
	    var tipoComprobante = tipoUI ? tipoUI : 'ALL';

	    // Correos / switches / agrupación
	    var correoResponsable = $('#CORREO_RESPONSABLE_EMITIDOS').val();
	    var modoAgrupar       = $('#modoAgrupar_Emitidos').val();
	    var validarSAT        = $('#validarSAT_Emitidos').prop('checked');
	    var complementoSAT    = $('#complementoSAT_Emitidos').prop('checked');
	    var notaCreditoSAT    = $('#notaCreditoSAT_Emitidos').prop('checked');

	    // ===== 3) Operadores y valores avanzados =====
	    // Texto
	    var rfcOp   = ($('#rfcOperatorE').val()   || 'contains').toLowerCase();
	    var razonOp = ($('#razonOperatorE').val() || 'contains').toLowerCase();
	    var serieOp = ($('#serieOperatorE').val() || 'contains').toLowerCase();
	    var tipoOp  = ($('#tipoOperatorE').val()  || 'equals').toLowerCase();
	    var uuidOp  = ($('#uuidOperatorE').val()  || 'contains').toLowerCase();

	    // Fecha
	    var dateOp = ($('#dateOperatorE').val() || 'eq').toLowerCase();
	    var dateV1 = $('#dateFilter1E').val() || '';
	    var dateV2 = $('#dateFilter2E').val() || '';

	    // Numéricos
	    var folioOp = ($('#folioOperatorE').val() || '').toLowerCase();
	    var folioV1 = $('#folioFilter1E').val() || '';
	    var folioV2 = $('#folioFilter2E').val() || '';

	    var totalOp = ($('#totalOperatorE').val() || '').toLowerCase();
	    var totalV1 = $('#totalFilter1E').val() || '';
	    var totalV2 = $('#totalFilter2E').val() || '';

	    var subOp   = ($('#subOperatorE').val()   || '').toLowerCase();
	    var subV1   = $('#subFilter1E').val() || '';
	    var subV2   = $('#subFilter2E').val() || '';

	    var retOp   = ($('#retOperatorE').val()   || '').toLowerCase();
	    var retV1   = $('#retFilter1E').val() || '';
	    var retV2   = $('#retFilter2E').val() || '';

	    var trasOp  = ($('#trasOperatorE').val()  || '').toLowerCase();
	    var trasV1  = $('#trasFilter1E').val() || '';
	    var trasV2  = $('#trasFilter2E').val() || '';

	    // ===== 4) POST al backend =====
	    $.ajax({
	      url  : '/siarex247/cumplimientoFiscal/boveda/emitidos/exportBovedaZIP.action',
	      type : 'POST',
	      data : {
	        // base
	        rfc: rfc,
	        razonSocial: razonSocial,
	        fechaInicial: fechaInicial,
	        fechaFinal: fechaFinal,
	        uuid: uuid,
	        tipoComprobante: tipoComprobante,
	        serie: serie,
	        folio: folio,
	        correoResponsable: correoResponsable,
	        modoAgrupar: modoAgrupar,
	        validarSAT: validarSAT,
	        complementoSAT: complementoSAT,
	        notaCreditoSAT: notaCreditoSAT,
	        idRegistro: uuidRegistro,

	        // texto
	        rfcOperator: rfcOp,
	        razonOperator: razonOp,
	        serieOperator: serieOp,
	        tipoOperator: tipoOp,
	        uuidOperator: uuidOp,

	        // fecha
	        dateOperator: dateOp,
	        dateV1: dateV1,
	        dateV2: dateV2,

	        // numéricos
	        folioOperator: folioOp, folioV1: folioV1, folioV2: folioV2,
	        totalOperator: totalOp, totalV1: totalV1, totalV2: totalV2,
	        subOperator:   subOp,   subV1:   subV1,   subV2:   subV2,
	        retOperator:   retOp,   retV1:   retV1,   retV2:   retV2,
	        trasOperator:  trasOp,  trasV1:  trasV1,  trasV2:  trasV2
	      },
	      dataType : 'json',
	      success  : function(data) {
	        if ($.isEmptyObject(data)) return;
	        if (data.codError == '000'){
	          Swal.fire({
	            icon: 'success',
	            title: MSG_OPERACION_EXITOSA_MENU,
	            text: MENSAJE_EXITOSO,
	            showCancelButton: false,
	            confirmButtonText: BTN_ACEPTAR_MENU,
	            denyButtonText: BTN_CANCELAR_MENU
	          }).then(function(result){
	            if (result.isConfirmed){
	              $('#modalDescargaCFDI_Emitidos').modal('hide');
	            }
	          });
	        } else {
	          Swal.fire({
	            title: MSG_ERROR_OPERACION_MENU,
	            html : '<p>'+(data.mensajeError || 'Error al iniciar la descarga')+'</p>',
	            icon : 'error'
	          });
	        }
	      },
	      error : function(xhr, ajaxOptions, thrownError) {
	        alert('descargarCFDIEmitidos()_'+thrownError);
	      }
	    });

	  }catch(e){
	    alert('descargarCFDIEmitidos()_'+e);
	  }
	}


		
	function convXMLAExcelEmitidos(){
		
		var fechaInicial = obtenerFechaIni_Emitidos();
		var fechaFinal = obtenerFechaFin_Emitidos();
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
						var uuidRegistro = '';
						  var bandFiltros = false;
						  try{
						    // 1) UUIDs seleccionados
						    var dataSelect = tablaDetalleEmitidos.rows('.selected').data();
						    $.each(dataSelect, function(key, row) { uuidRegistro += (row.uuid || '') + ';'; });

						    // 2) Base
						    var rfc             = obtenerRFC_Emitidos();
						    var razonSocial     = obtenerRazon_Emitidos();
						    var uuid            = obtenerUUID_Emitidos();
						    var tipoComprobante = obtenerComprobante_Emitidos();
						    var serie           = obtenerSerie_Emitidos();
						    var folio           = obtenerFolio_Emitidos();

						    // 3) Operadores/valores
						    var rfcOp   = ($('#rfcOperatorE').val()   || 'contains').toLowerCase();
						    var razOp   = ($('#razonOperatorE').val() || 'contains').toLowerCase();
						    var serOp   = ($('#serieOperatorE').val() || 'contains').toLowerCase();
						    var tipoOp  = ($('#tipoOperatorE').val()  || 'equals').toLowerCase();
						    var uuidOp  = ($('#uuidOperatorE').val()  || 'contains').toLowerCase();

						    var dateOp  = ($('#dateOperatorE').val() || 'eq').toLowerCase();
						    var dateV1  = $('#dateFilter1E').val() || '';
						    var dateV2  = $('#dateFilter2E').val() || '';

						    var folioOp = ($('#folioOperatorE').val() || '').toLowerCase();
						    var folioV1 = $('#folioFilter1E').val() || '';
						    var folioV2 = $('#folioFilter2E').val() || '';

						    var totalOp = ($('#totalOperatorE').val() || '').toLowerCase();
						    var totalV1 = $('#totalFilter1E').val() || '';
						    var totalV2 = $('#totalFilter2E').val() || '';

						    var subOp   = ($('#subOperatorE').val()   || '').toLowerCase();
						    var subV1   = $('#subFilter1E').val() || '';
						    var subV2   = $('#subFilter2E').val() || '';

						    var retOp   = ($('#retOperatorE').val()   || '').toLowerCase();
						    var retV1   = $('#retFilter1E').val() || '';
						    var retV2   = $('#retFilter2E').val() || '';

						    var trasOp  = ($('#trasOperatorE').val()  || '').toLowerCase();
						    var trasV1  = $('#trasFilter1E').val() || '';
						    var trasV2  = $('#trasFilter2E').val() || '';

						    // 4) Helper: asegura/crea hidden inputs
						    function ensureHidden(form, id, name, value){
						      var el = document.getElementById(id);
						      if(!el){
						        el = document.createElement('input');
						        el.type = 'hidden';
						        el.id = id;
						        el.name = name; // lo que recibe el Action
						        form.appendChild(el);
						      }
						      el.value = (value == null ? '' : value);
						    }

						    var form = document.forms['frmBovedaXMLExcelEmitidos'];

						    // 5) Campos base (ya existen en el JSP con sufijo _Emitidos)
						    ensureHidden(form, 'fechaInicial_ConvXML_Emitidos',  'fechaInicial',  fechaInicial);
						    ensureHidden(form, 'fechaFinal_ConvXML_Emitidos',    'fechaFinal',    fechaFinal);
						    ensureHidden(form, 'rfcXMLExcel_Emitidos',           'rfc',           rfc);
						    ensureHidden(form, 'razonSocialXMLExcel_Emitidos',   'razonSocial',   razonSocial);
						    ensureHidden(form, 'uuidBovedaXMLExcel_Emitidos',    'uuid',          uuid);
						    ensureHidden(form, 'tipoComprobanteXMLExcel_Emitidos','tipoComprobante', tipoComprobante);
						    ensureHidden(form, 'serieXMLExcel_Emitidos',         'serie',         serie);
						    ensureHidden(form, 'folioXMLExcel_Emitidos',         'folio',         folio);
						    ensureHidden(form, 'idRegistroXMLExcel_Emitidos',    'cadRegistros',  uuidRegistro);

						    // 6) NUEVO: operadores/valores avanzados (names como en el backend)
						    // Texto
						    ensureHidden(form, 'rfcOperatorXMLExcel_Emitidos',   'rfcOperator',   rfcOp);
						    ensureHidden(form, 'razonOperatorXMLExcel_Emitidos', 'razonOperator', razOp);
						    ensureHidden(form, 'serieOperatorXMLExcel_Emitidos', 'serieOperator', serOp);
						    ensureHidden(form, 'tipoOperatorXMLExcel_Emitidos',  'tipoOperator',  tipoOp);
						    ensureHidden(form, 'uuidOperatorXMLExcel_Emitidos',  'uuidOperator',  uuidOp);

						    // Fecha
						    ensureHidden(form, 'dateOperatorXMLExcel_Emitidos',  'dateOperator',  dateOp);
						    ensureHidden(form, 'dateV1XMLExcel_Emitidos',        'dateV1',        dateV1);
						    ensureHidden(form, 'dateV2XMLExcel_Emitidos',        'dateV2',        dateV2);

						    // Numéricos
						    ensureHidden(form, 'folioOperatorXMLExcel_Emitidos', 'folioOperator', folioOp);
						    ensureHidden(form, 'folioV1XMLExcel_Emitidos',       'folioV1',       folioV1);
						    ensureHidden(form, 'folioV2XMLExcel_Emitidos',       'folioV2',       folioV2);

						    ensureHidden(form, 'totalOperatorXMLExcel_Emitidos', 'totalOperator', totalOp);
						    ensureHidden(form, 'totalV1XMLExcel_Emitidos',       'totalV1',       totalV1);
						    ensureHidden(form, 'totalV2XMLExcel_Emitidos',       'totalV2',       totalV2);

						    ensureHidden(form, 'subOperatorXMLExcel_Emitidos',   'subOperator',   subOp);
						    ensureHidden(form, 'subV1XMLExcel_Emitidos',         'subV1',         subV1);
						    ensureHidden(form, 'subV2XMLExcel_Emitidos',         'subV2',         subV2);

						    ensureHidden(form, 'retOperatorXMLExcel_Emitidos',   'retOperator',   retOp);
						    ensureHidden(form, 'retV1XMLExcel_Emitidos',         'retV1',         retV1);
						    ensureHidden(form, 'retV2XMLExcel_Emitidos',         'retV2',         retV2);

						    ensureHidden(form, 'trasOperatorXMLExcel_Emitidos',  'trasOperator',  trasOp);
						    ensureHidden(form, 'trasV1XMLExcel_Emitidos',        'trasV1',        trasV1);
						    ensureHidden(form, 'trasV2XMLExcel_Emitidos',        'trasV2',        trasV2);

						    // 7) Submit
						    form.submit();

						  }catch(e){
						    alert('convXMLAExcelEmitidos(): ' + e);
						  }
						
						
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
				alert('convXMLAExcelEmitidos()_'+thrownError);
			}
		});			
	 
	}

	
	
	 function getElementosFilterEmitidos(){
		 var llavesUUID = '';
		 try{
			 var dataSelect = tablaDetalleEmitidos.rows('.selected').data(); 
				$.each(dataSelect, function(key, row) {
					llavesUUID+= row.uuid + ";"		
			    });

		 }catch(e){
			 alert('getElementosFilterEmitidos()_'+e);
		 }
		 return llavesUUID;
	 }

	 
	 function getElementosEmitidos(){
			var llaveRegistros = '';
			try{
				 var table = $('#tablaDetalleEmitidos').DataTable();
				 var bandPrimero = true;
				 table.column(12, { search:'applied' }).data().each(function(value, index) {
					 if (bandPrimero){
						 llaveRegistros = value;
					 }else{
						 llaveRegistros = llaveRegistros +  ";"  + value;
					 }
					 bandPrimero = false;
				 });
			}catch(e){
				alert('getElementosEmitidos()_'+e);
			}
			return llaveRegistros;
		}	
	 
	 
		function obtenerFechasFiltroEmitidos(){
			$.ajax({
				url  : '/siarex247/cumplimientoFiscal/boveda/nomina/consultarFechasNomina.action',
				type : 'POST', 
							data : null,
							dataType : 'json',
							success  : function(data) {
								if($.isEmptyObject(data)) {
								   
								} else {
									 //$('#fechaInicial_Emitidos_Filtro').val(data.fechaInicial);
									 //$('#fechaFinal_Emitidos_Filtro').val(data.fechaFinal);
									// var fechas = data.fechaInicial + '  a  ' + data.fechaFinal;
									// $('#CRMDateRangeE').val(fechas);
									
									obtenerFechasMinimaEmitidos(data.fechaInicial, data.fechaFinal);
									
								}
							},
							error : function(xhr, ajaxOptions, thrownError) {
								alert('obtenerFechasFiltro()_'+thrownError);
							}
						});	
						
						}

	 
	function validarFechas_Emitidos(){
		var fechaInicial = obtenerFechaIni_Emitidos();
		var fechaFinal = obtenerFechaFin_Emitidos();
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
						window.__copiarTheadAGlobalesE();
						refrescarBovedaEmitidos();
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
	


	function consultarFechaEmitidos(){
		$.ajax({
			url  : '/siarex247/cumplimientoFiscal/descargaSAT/recibidos/consultarUltimaFecha.action',
			type : 'POST', 
			data : null,
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					//document.getElementById("fechaUltimaActualizacion").innerHTML = LABEL_BOVEDA_ETQ8 + ' ' + data.fechaDescarga;
					document.getElementById("BOVEDA_EMITIDOS_ETQ6").innerHTML = LABEL_BOVEDA_EMITIDOS_ETQ8 + ' ' + data.fechaDescarga;
					
					
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('consultarFechaEmitidos()_'+thrownError);
			}
		});	
		
    }
	
	// Debounce + lock para evitar múltiples llamadas
	//let __emitidosBusy = false;
	//let __emitidosDebounce = null;

	function refrescarBovedaEmitidos(){
	  try{
	    if (__emitidosDebounce) clearTimeout(__emitidosDebounce);
	    __emitidosDebounce = setTimeout(function(){
	      if (__emitidosBusy) return;            // ya hay un request en vuelo
	      __emitidosBusy = true;

	      // 👉 NADA de clear().draw(); sólo un reload
		 // tablaDetalleEmitidos.page('first').draw('page');
	      $('#tablaDetalleEmitidos').DataTable().ajax.reload(function(){
	        __emitidosBusy = false;
	      }, true); // false = conserva la página actual
	    }, 120);     // pequeño debounce por si el usuario teclea rápido
	  }catch(e){
	    alert('refrescarBovedaEmitidos()_ ' + e);
	    __emitidosBusy = false;
	  }
	}

	
	

	function obtenerRFC_Emitidos(){
		var rfcEmitidos = $('#rfc_Emitidos').val();
		return rfcEmitidos;
	}
	
	function obtenerRazon_Emitidos(){
		return $('#razonSocial_Emitidos').val();
	}
	
	function obtenerFolio_Emitidos(){
		return $('#folio_Emitidos').val();
	}
	
	function obtenerSerie_Emitidos(){
		return $('#serie_Emitidos').val();
	}
	
	function obtenerFechaIni_Emitidos(){
		return $('#fechaInicial_Emitidos_Filtro').val();
	}
	
	
	function obtenerFechaFin_Emitidos(){
		return $('#fechaFinal_Emitidos_Filtro').val();
	}
	
	function obtenerComprobante_Emitidos(){
		return $('#tipoComprobante_Emitidos').val();
	}
	
	function obtenerUUID_Emitidos(){
		return $('#uuid_Emitidos').val();
	}
	
	
	function limpiarEmitidos(){
	  try{
	    window.__emiSilentReset = true; // evita bucles en handlers

	    // Backend fields
	    $('#rfc_Emitidos, #razonSocial_Emitidos, #folio_Emitidos, #serie_Emitidos, #fechaInicial_Emitidos, #fechaFinal_Emitidos, #uuid_Emitidos').val('');

	    // --- TIPO: principal = '0' (Todos)
	    const $tipoMain = $('#tipoComprobante_Emitidos');
	    if ($tipoMain.length){
	      if ($tipoMain.find('option[value="0"]').length === 0){
	        $tipoMain.prepend('<option value="0">Todos</option>');
	      }
	      $tipoMain.val('0');
	      $tipoMain.trigger($tipoMain.hasClass('select2-hidden-accessible') ? 'change.select2' : 'change');
	    }

	    // --- TIPO en THEAD: 'ALL' (y también el clon de FixedHeader)
	    $('#tipoFilterInputE').val('ALL');
	    $('.dtfh-floatingparent thead #tipoFilterInputE').val('ALL');

	    // Operador visual del tipo
	    $('#tipoOperatorE').val('equals');
	    $('#tipoOpBtnE .op-label').text('=');

	    // Resto de filtros (los tuyos)...
	    $('#rfcFilterInputE, #razonFilterInputE, #serieFilterInputE, #uuidFilterInputE').val('');
	    $('#rfcOperatorE, #razonOperatorE, #serieOperatorE, #uuidOperatorE').val('contains');
	    $('#rfcOpBtnE .op-label, #razonOpBtnE .op-label, #serieOpBtnE .op-label, #uuidOpBtnE .op-label').html('<i class="fas fa-search"></i>');

	    function resetNum(opSel, v1Sel, v2Sel, btnSel){
	      $(opSel).val('eq'); $(v1Sel).val(''); $(v2Sel).val('').addClass('d-none'); $(btnSel+' .op-label').text('=');
	    }
	    resetNum('#folioOperatorE', '#folioFilter1E', '#folioFilter2E', '#folioOpBtnE');
	    resetNum('#totalOperatorE', '#totalFilter1E', '#totalFilter2E', '#totalOpBtnE');
	    resetNum('#subOperatorE',   '#subFilter1E',   '#subFilter2E',   '#subOpBtnE');
	    resetNum('#retOperatorE',   '#retFilter1E',   '#retFilter2E',   '#retOpBtnE');
	    resetNum('#trasOperatorE',  '#trasFilter1E',  '#trasFilter2E',  '#trasOpBtnE');

	    $('#dateOperatorE').val('eq'); $('#dateFilter1E').val(''); $('#dateFilter2E').val('').addClass('d-none');
	    $('#dateOpBtnE .op-label').text('=');

	    $('#rfcGridFilterE').val('');
		
		// --- Limpia el componente de rango de fechas (CRMDateRangeR) ---
		/*
		var range = document.getElementById('CRMDateRangeE');
		if (range) {
		  range.value = '';
		  if (range._flatpickr) {
		    range._flatpickr.clear(); // limpia selección visual
		  }
		}*/
		
		obtenerFechasFiltroEmitidos();

	    // Recarga tabla sin cambiar de página
	    $('#tablaDetalleEmitidos').DataTable().ajax.reload(null, false);
	  }catch(e){
	    alert('limpiarEmitidos()_' + e);
	  }finally{
	    setTimeout(()=>{ window.__emiSilentReset = false; }, 0);
	  }
	}
	


	
	function cargaProveedoresEmitidos() {
		try {
			$.ajax({
	           url: '/siarex247/cumplimientoFiscal/boveda/emitidos/catProveedores.action',
	           type: 'POST',
	            dataType : 'json',
			    success: function(data){
			    	$('#rfc_Emitidos').empty();
			    	$.each(data.data, function(key, text) {
			    		$('#rfc_Emitidos').append($('<option></option>').attr('value', text.rfcReceptor).text(text.razonSocialReceptor));	
			      	});
			    }
			});
		}
		catch(e) {
			alert('cargaProveedoresRecibidos()_'+e);
		} 
	}
	/* =========================================
	 * Bóveda Emitidos – Filtros (FIX COMPLETO)
	 * ========================================= */

	/* ---- Guards globales ---- */
	window.__emitidosBusy      = window.__emitidosBusy      || false;
	window.__emitidosDebounce  = window.__emitidosDebounce  || null;
	window.__emitidos_booting  = true;   // ⬅️ evita reloads durante el arranque

	/* Recarga con debounce y lock (tabla Emitidos) */
	function refrescarBovedaEmitidos(){
	  try{
	    if (window.__emitidos_booting) return; // ⛔️ no recargar durante el boot
	    if (window.__emitidosDebounce) clearTimeout(window.__emitidosDebounce);
	    window.__emitidosDebounce = setTimeout(function(){
	      if (window.__emitidosBusy) return;

	      var $tbl = $('#tablaDetalleEmitidos');
	      if (!$tbl.length || !$.fn.dataTable.isDataTable($tbl)) return;

	      window.__emitidosBusy = true;
	      $tbl.DataTable().ajax.reload(function(){
	        window.__emitidosBusy = false;
	      }, false);
	    }, 120);
	  }catch(e){
	    alert('refrescarBovedaEmitidos()_' + e);
	    window.__emitidosBusy = false;
	  }
	}
	
	
	function __copiarTheadAGlobalesE(){
		  const mapTxt = [
			['#rfcFilterInputE',   '#rfc_Emitidos'],
		    ['#razonFilterInputE', '#razonSocial_Emitidos'],
		    ['#serieFilterInputE', '#serie_Emitidos'],
		    ['#uuidFilterInputE',  '#uuid_Emitidos']
		  ];
		  mapTxt.forEach(([from, to]) => {
		    if ($(from).length && $(to).length) $(to).val($(from).val());
		  });

		  if ($('#dateFilter1E').length && $('#fechaInicial_Emitidos').length) {
		    $('#fechaInicial_Emitidos').val($('#dateFilter1N').val());
		  }
		  if ($('#dateFilter2E').length && $('#fechaFinal_Emitidos').length) {
		    $('#fechaFinal_Emitidos').val($('#dateFilter2E').val());
		  }
		}
		

	/* ===== Helper: sincroniza thead → inputs globales ===== */
	function __syncFilterToGlobalE($filter){
		
	  const map = [
	    ['#rfcFilterInputE',   '#rfc_Emitidos'],
	    ['#razonFilterInputE', '#razonSocial_Emitidos'],
	    ['#serieFilterInputE', '#serie_Emitidos'],
	    ['#uuidFilterInputE',  '#uuid_Emitidos']
	  ];
	  for (const [from, to] of map){
	    if ($filter.find(from).length && $(to).length){
	      $(to).val($filter.find(from).val());
	      break;
	    }
	  }
	  if ($filter.find('#dateFilter1E').length){
	    $('#fechaInicial_Emitidos').val($('#dateFilter1E').val() || '');
	    $('#fechaFinal_Emitidos').val($('#dateFilter2E').val() || '');
	  }
	}

	/* ===== Helpers de inicialización ===== */
	window.initDxLikeFilterE = function ({ btnId, menuId, inputId, hiddenOpId, targetInput }) {
	  const $input = $(inputId), $op = $(hiddenOpId);
	  if (!$input.length || !$op.length) return;
	  const isTipo = (hiddenOpId || '').toLowerCase().includes('tipooperator');
	  if (!$op.val()) $op.val(isTipo ? 'equals' : 'contains');

	  const ns = '.dxTxtEnterE';
	  $input.off('keydown' + ns).on('keydown' + ns, e => {
	    if (e.key === 'Enter') {
	      e.preventDefault();
	      if (targetInput) $(targetInput).val($input.val());
	      __syncFilterToGlobalE($input.closest('.dx-like-filter'));
		  //tablaDetalleEmitidos.page('first').draw('page');
	      //refrescarBovedaEmitidos();
		  validarFechas_Emitidos();
	    }
	  });
	};

	window.initNumericDxFilterE = function ({ v1Id, v2Id, opHiddenId }) {
	  const $v1 = $(v1Id), $v2 = $(v2Id), $op = $(opHiddenId);
	  if (!$v1.length || !$op.length) return;
	  if (!$op.val()) $op.val('eq');

	  const ns = '.dxNumEnterE';
	  const onEnter = e => {
	    if (e.key === 'Enter'){
	      e.preventDefault();
	      __syncFilterToGlobalE($(e.target).closest('.dx-like-filter'));
		  //tablaDetalleEmitidos.page('first').draw('page');
	      //refrescarBovedaEmitidos();
		  validarFechas_Emitidos();
	    }
	  };
	  $v1.off('keydown' + ns).on('keydown' + ns, onEnter);
	  if ($v2.length) $v2.off('keydown' + ns).on('keydown' + ns, onEnter);
	};

	window.initDxLikeDateFilterE = function ({ input1Id, input2Id, hiddenOpId }) {
	  const $d1 = $(input1Id), $d2 = $(input2Id), $op = $(hiddenOpId);
	  if (!$d1.length || !$op.length) return;
	  if (!$op.val()) $op.val('eq');

	  const ns = '.dxDateEnterE';
	  const onEnter = e => {
	    if (e.key === 'Enter'){
	      e.preventDefault();
	      __syncFilterToGlobalE($(e.target).closest('.dx-like-filter'));
	      // refrescarBovedaEmitidos();
		  validarFechas_Emitidos();
	    }
	  };
	  $d1.off('keydown' + ns).on('keydown' + ns, onEnter);
	  if ($d2.length) $d2.off('keydown' + ns).on('keydown' + ns, onEnter);
	};

	/* ====== Delegados globales ====== */
	(function () {

	  function isNumericMenu(id){ return /(folio|total|sub|ret|tras)OpMenuE$/i.test(id||''); }
	  function isDateMenu(id){   return /dateOpMenuE$/i.test(id||''); }
	  function isTipoMenu(id){   return /tipoOpMenuE$/i.test(id||''); }

	  function hiddenForMenu($menu){
	    const id = $menu.attr('id') || '';
	    return '#'+id.replace('OpMenu','Operator');
	  }
	  function defaultOp(menuId){
	    if (isTipoMenu(menuId)) return 'equals';
	    if (isDateMenu(menuId) || isNumericMenu(menuId)) return 'eq';
	    return 'contains';
	  }
	  function showSecond($filter, show){
	    const $second = $filter.find('#folioFilter2E,#totalFilter2E,#subFilter2E,#retFilter2E,#trasFilter2E,#dateFilter2E');
	    if (show) $second.removeClass('d-none'); else $second.addClass('d-none').val('');
	  }
	  function __cerrarMenusEmitidos(){
	    $('#tablaDetalleEmitidos thead .dx-like-menu, .dtfh-floatingparent thead .dx-like-menu').removeClass('show');
	  }
	  function __valorPresente($filter){
	    const $inputs = $filter.find('input[type=text],input[type=number],input[type=date],select');
	    let ok = false;
	    $inputs.each(function(){
	      const v = ($(this).val() || '').trim();
	      if (v.length >= 1) { ok = true; return false; }
	    });
	    return ok;
	  }

	  /* --- abrir/cerrar menú --- */
	  $(document)
	    .off('click.dxMenuToggleE')
	    .on('click.dxMenuToggleE',
	      '#tablaDetalleEmitidos thead .op-btn, .dtfh-floatingparent thead .op-btn',
	      function(e){
	        e.stopPropagation();
	        const $btn  = $(this);
	        const $menu = $btn.siblings('.dx-like-menu');
	        __cerrarMenusEmitidos();
	        $menu.addClass('show').css({ position:'absolute', zIndex: 1090 });

	        const menuW = $menu.outerWidth();
	        const thW   = $btn.closest('th').outerWidth();
	        if (menuW > thW) $menu.css({ left: 0, right: 'auto' });
	      }
	    );

	  $(document).off('click.dxMenuCloseE').on('click.dxMenuCloseE', function(){ __cerrarMenusEmitidos(); });
	  $(window).off('scroll.dxMenuCloseE resize.dxMenuCloseE')
	           .on('scroll.dxMenuCloseE resize.dxMenuCloseE', function(){ __cerrarMenusEmitidos(); });
	  $('#tablaDetalleEmitidos').off('draw.dt.dxMenuCloseE')
	                            .on('draw.dt.dxMenuCloseE', function(){ __cerrarMenusEmitidos(); });

	  /* --- selección de operador --- */
	  $(document)
	    .off('click.dxSelectOpE')
	    .on('click.dxSelectOpE',
	      '#tablaDetalleEmitidos thead .dx-like-menu li, .dtfh-floatingparent thead .dx-like-menu li',
	      function(e){
	        e.stopPropagation();

	        const $li   = $(this);
	        const op    = String($li.data('op')||'').toLowerCase();
	        const $menu = $li.closest('.dx-like-menu');
	        const mid   = $menu.attr('id') || '';
	        const $f    = $menu.closest('.dx-like-filter');
	        const hid   = hiddenForMenu($menu);
	        const $hidden = $(hid);
	        const $label  = $f.find('.op-label');

	        if (!$hidden.length) return;

	        if (op === 'reset'){
	          const def = defaultOp(mid);
	          $hidden.val(def);
	          if (isNumericMenu(mid) || isDateMenu(mid) || isTipoMenu(mid)) $label.text('=');
	          else $label.html('<i class="fas fa-search"></i>');
	          $f.find('input[type=text],input[type=number],input[type=date]').val('');
	          showSecond($f, false);
	          __cerrarMenusEmitidos();
	          __syncFilterToGlobalE($f);
	          //refrescarBovedaEmitidos();
			  validarFechas_Emitidos();
	          return;
	        }

	        $hidden.val(op);
	        const firstToken = $.trim($li.text()).split(/\s+/)[0];
	        $label.text(firstToken);
	        const needSecond = (isDateMenu(mid) ? (op === 'bt') : (op === 'between' || op === 'bt'));
	        showSecond($f, needSecond);
	        __cerrarMenusEmitidos();

	        if (__valorPresente($f)) {
	          __syncFilterToGlobalE($f);
	          //refrescarBovedaEmitidos();
			  validarFechas_Emitidos();
	        }
	      }
	    );

	  /* --- ENTER global --- */
	  $(document)
	    .off('keydown.emitidosGlobalEnterFix')
	    .on('keydown.emitidosGlobalEnterFix',
	      '#tablaDetalleEmitidos thead tr.filters :input, .dtfh-floatingparent thead tr.filters :input',
	      function(e){
	        if(e.key === 'Enter'){
	          e.preventDefault();
	          __syncFilterToGlobalE($(this).closest('.dx-like-filter'));
	          //refrescarBovedaEmitidos();
			 // validarFechas_Emitidos();
	        }
	      });

		  /* =======================
		   * Emitidos – Fix: "Todos" se queda pegado
		   * Select ↔ Thead sync sin loops + normalización ALL
		   * ======================= */
		  (function(){
		    const TABLE = '#tablaDetalleEmitidos';
		    const SEL   = '#tipoComprobante_Emitidos';
		    const INP   = '#tipoFilterInputE';
		    const OPE   = '#tipoOperatorE';

		    // Asegura que exista "Todos" como '0' (canónico)
		    (function ensureTodos(){
		      const $sel = $(SEL);
		      if ($sel.length && $sel.find('option[value="0"]').length === 0){
		        $sel.prepend('<option value="0">Todos</option>');
		      }
		    })();

		    function isAll(v){
		      const s = (v == null ? '' : String(v)).trim().toUpperCase();
		      return s === '' || s === '0' || s === 'ALL';
		    }

		    // Lock para evitar rebotes/loops (select -> input -> select -> …)
		    let syncing = false;

		    // Limpia binds previos sólo para este feature
		    $(document).off('change.emitidosTipoSync', SEL)
		               .off('change.emitidosTipoSync', TABLE + ' thead ' + INP);

		    $(document)
		      // Cambio en SELECT principal
		      .on('change.emitidosTipoSync', SEL, function(){
		        if (syncing) return;
		        syncing = true;

		        const $sel = $(SEL);
		        const $inp = $(INP);

		        let val = ($sel.val() || '').trim();
		        if (isAll(val)){
		          // UI: muestra "Todos" en select, thead vacío
		          $sel.val('1');
		          $inp.val('');
		          $(OPE).val('contains');   // o 'equals' si tu backend lo espera así
		        } else {
		          $inp.val(val);
		          $(OPE).val('equals');
		        }

		        // Refresca select2 si aplica
		        if ($sel.hasClass('select2-hidden-accessible')) $sel.trigger('change.select2');

		        // Dispara recarga si ya está montada la tabla
		        if (!window.__emitidos_booting && $.fn.dataTable.isDataTable(TABLE)){
		          // usa tu refresco; si tienes otro nombre, cámbialo aquí
		          if (typeof window.refrescarBovedaEmitidos === 'function'){
		            //window.refrescarBovedaEmitidos();
					// validarFechas_Emitidos();
		          }
		        }

		        syncing = false;
		      })

		      // Cambio en INPUT del thead
		      .on('change.emitidosTipoSync', TABLE + ' thead ' + INP, function(){
		        if (syncing) return;
		        syncing = true;

		        const $sel = $(SEL);
		        const $inp = $(INP);

		        let typed = ($inp.val() || '').trim();
		        if (isAll(typed)){
		          $sel.val('0');
		          $(OPE).val('contains');   // o 'equals' según tu backend
		        } else {
		          $sel.val(typed);
		          $(OPE).val('equals');
		        }

		        if ($sel.hasClass('select2-hidden-accessible')) $sel.trigger('change.select2');

		        if (!window.__emitidos_booting && $.fn.dataTable.isDataTable(TABLE)){
		          if (typeof window.refrescarBovedaEmitidos === 'function'){
		            //window.refrescarBovedaEmitidos();
					//validarFechas_Emitidos();
		          }
		        }

		        syncing = false;
		      });
		  })();


	  /* --- Anti-ráfaga --- */
	  (function(){
	    let lastXhrAt = 0;
	    $('#tablaDetalleEmitidos')
	      .off('preXhr.dt.emitidos')
	      .on('preXhr.dt.emitidos', function(e){
	        const now = Date.now();
	        if (now - lastXhrAt < 200) { e.preventDefault(); return false; }
	        lastXhrAt = now;
	      });
	  })();

	})(); // IIFE de filtros

	/* 🟢 Desbloqueo tras primera carga */
	$('#tablaDetalleEmitidos')
	  .off('xhr.dt.emitidosInitOnce')
	  .on('xhr.dt.emitidosInitOnce', function() {
	    window.__emitidos_booting = false;
	    $(this).off('xhr.dt.emitidosInitOnce');
	  });

	/* ===== Tipo helpers ===== */
	function __isAllTipo(v){
	  const s = (v || '').trim();
	  return s === '' || s === '0' || s.toUpperCase() === 'ALL';
	}
	function __toPayloadTipo(v){
	  return __isAllTipo(v) ? 'ALL' : (v || '').trim().toUpperCase();
	}

		// === 1) SINCRONIZAR THEAD → CAMPOS DEL BACKEND (Emitidos) ===
		window.__emi_syncThead = window.__emi_syncThead || function(){
		  // toma el valor desde el clon flotante si existe; si no, desde el thead original
		  function getVal(sel){
		    const $float = $('table.fixedHeader-floating thead').find(sel);
		    if ($float.length) return $float.val();
		    return $('#tablaDetalleEmitidos thead').find(sel).val();
		  }
		  // texto
		  $('#uuid_Emitidos').val( getVal('#uuidFilterInputE') || '' );
		  $('#rfcEmi_Emitidos').val( getVal('#rfcEmiFilterInputE') || '' );
		  $('#nomEmi_Emitidos').val( getVal('#nomEmiFilterInputE') || '' );
		  $('#rfcRec_Emitidos').val( getVal('#rfcRecFilterInputE') || '' );
		  $('#nomRec_Emitidos').val( getVal('#nomRecFilterInputE') || '' );
		  $('#pac_Emitidos').val( getVal('#pacFilterInputE') || '' );
		  $('#efecto_Emitidos').val( getVal('#efectoFilterInputE') || '' );

		  // tipo de comprobante (input thead ↔ select externo)
		  const tipoVal = (getVal('#tipoFilterInputE') || '').trim();
		  $('#tipoComprobante_Emitidos').val(tipoVal || 'ALL');
		  if ($('#tipoOperatorE').length){
		    $('#tipoOperatorE').val(tipoVal ? 'equals' : 'contains');
		  }

		  // numéricos (ajusta IDs si usas otros)
		  $('#folioFilter1E').val( getVal('#folioFilter1E') || '' );
		  $('#folioFilter2E').val( getVal('#folioFilter2E') || '' );

		  $('#montoFilter1E').val( getVal('#montoFilter1E') || '' );
		  $('#montoFilter2E').val( getVal('#montoFilter2E') || '' );

		  // fechas (si las tienes en thead)
		  const f1 = getVal('#emiDateFilter1') || '';
		  const f2 = getVal('#emiDateFilter2') || '';
		  if ($('#fechaInicial_Emitidos').length) $('#fechaInicial_Emitidos').val(f1);
		  if ($('#fechaFinal_Emitidos').length)   $('#fechaFinal_Emitidos').val(f2);
		};


