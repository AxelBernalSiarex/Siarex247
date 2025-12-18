


 var tablaDetalleBoveda = null;
 // Apaga cualquier handler viejo que use este namespace o ese selector
// $('#bovedaRecContainer').off('change.recibidosTipo');
 //$('#bovedaRecContainer').off('change', '#tipoComprobante_Recibidos');

	$(document).ready(function() {
		try {
			
			tablaDetalleBoveda = $('#tablaDetalleBoveda').DataTable( {
				destroy: true,     // 👈 EVITA dobles al re-entrar
				  retrieve: false, 
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
				searching: true,
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
				 ajax : {
						url: '/siarex247/cumplimientoFiscal/boveda/recibidos/detalleBoveda.action',
						beforeSend: function( xhr ) {
		        			$('#overSeccion_Boveda_Recibidos').css({display:'block'});
							$('#btnRefrescar').prop('disabled', true);
		        		},
		        		complete: function(jqXHR, textStatus){
				    		$('#overSeccion_Boveda_Recibidos').css({display:'none'});
							$('#btnRefrescar').prop('disabled', false);
					    },	
						 data : function(d){
						   // Forzar a NO usar buscador global (y logear todo el paquete)
						   if (!d.search) d.search = {};
						   d.search.value = ''; // <- mata el 'Buscar:' global en el POST
						   if (typeof window.__bvdRec_syncThead === 'function') __bvdRec_syncThead();

						   // Básicos
						   d.fechaInicial = obtenerFechaIni_Recibidos();
						   d.fechaFinal   = obtenerFechaFin_Recibidos();
						   d.rfc          = $('#rfcGridFilter').val() || obtenerRFC_Recibidos();
						   d.razonSocial  = obtenerRazon_Recibidos();
						   d.uuid         = obtenerUUID_Recibidos();
						   d.tipoComprobante = obtenerComprobante_Recibidos();
						   d.serie        = obtenerSerie_Recibidos();
						   d.folio        = obtenerFolio_Recibidos();
						
						   // Operadores/valores
						   d.rfcOperator    = ($('#rfcOperator').val()    || 'contains'); // ¡sin toLowerCase!
						   d.razonOperator  = ($('#razonOperator').val()  || 'contains');
						   d.serieOperator  = ($('#serieOperator').val()  || 'contains');
						   d.tipoOperator   = ($('#tipoOperator').val()   || 'equals');
						   d.uuidOperator   = ($('#uuidOperator').val()   || 'contains');
						
						   d.folioOperator  = $('#folioOperator').val() || 'eq';
						   d.folioV1        = $('#folioFilter1').val();
					       d.folioV2        = $('#folioFilter2').val();
						
						   d.totalOperator  = $('#totalOperator').val() || 'eq';
						   d.totalV1        = $('#totalFilter1').val();
						   d.totalV2        = $('#totalFilter2').val();
						
						   d.subOperator    = $('#subOperator').val() || 'eq';
						   d.subV1          = $('#subFilter1').val();
						   d.subV2          = $('#subFilter2').val();
						
						   d.ivaOperator    = $('#ivaOperator').val() || 'eq';
						   d.ivaV1          = $('#ivaFilter1').val();
						   d.ivaV2          = $('#ivaFilter2').val();
						
						   d.ivaRetOperator = $('#ivaRetOperator').val() || 'eq';
						   d.ivaRetV1       = $('#ivaRetFilter1').val();
						   d.ivaRetV2       = $('#ivaRetFilter2').val();
						
						   d.isrOperator    = $('#isrOperator').val() || 'eq';
						   d.isrV1          = $('#isrFilter1').val();
						   d.isrV2          = $('#isrFilter2').val();
						
						   d.impLocOperator = $('#impLocOperator').val() || 'eq';
						  d.impLocV1       = $('#impLocFilter1').val();
						   d.impLocV2       = $('#impLocFilter2').val();
						
						   d.dateOperator   = $('#dateOperator').val() || 'eq';
						  d.dateV1         = $('#dateFilter1').val();
						  d.dateV2         = $('#dateFilter2').val();
						
						   // 🔍 LOG del payload completo
						   if (window.DEBUG_BOVEDA) {
						     console.log('[BOVEDA] DataTables payload →', JSON.parse(JSON.stringify(d)));
						   }
						 },
						type: 'POST'
					},
					aoColumns : [
						{ mData: null, "sClass": "alinearCentro"},
						{ mData: "rfc", "sClass": "alinearCentro"},
						{ mData: "razonSocial"},
						{ mData: "serie", "sClass": "alinearCentro"},
						{ mData: "tipoComprobante", "sClass": "alinearCentro"},
						{ mData: "folio", "sClass": "alinearCentro"},
						{ mData: "total", "sClass": "alinearDerecha"},
						{ mData: "subTotal", "sClass": "alinearDerecha"},
						{ mData: "iva", "sClass": "alinearDerecha"},
						{ mData: "retIVA", "sClass": "alinearDerecha"},
						{ mData: "retISR", "sClass": "alinearDerecha"},
						{ mData: "impLocales", "sClass": "alinearDerecha"},
						{ mData: null, "sClass": "alinearCentro"},
						{ mData: null, "sClass": "alinearCentro"},
						{ mData: "uuid"},
						{ mData: "fechaFactura"}
						
					],
					columnDefs: [
						 {
		                    targets: 12,
		                    render: function (data, type, row) {
		                    	rowElement = '<img class="" src="/theme-falcon/repse247/img/xml26.png" alt="" style="cursor: pointer;" title="Ver XML" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo XML" onclick="generaArchivo(\'XML\', \'' + row.idRegistro+ '\');" />';
		                        return rowElement;
		                      }
		                 },
		                 {
			                 targets: 13,
		                    render: function (data, type, row) {
		                    	rowElement = '<img class="" src="/theme-falcon/repse247/img/pdf26.png" alt="" style="cursor: pointer;" title="Ver PDF" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="generaArchivo(\'PDF\', \'' + row.idRegistro+ '\');" />';
		                        return rowElement;
		                      }
		                 },
		                 {	targets: 14,
						        render: function (data, type, row) {
						        	rowElement = '<a href="javascript:generaArchivo(\'PDF\', \'' + row.idRegistro+ '\');">' + row.uuid + '</a>';
						            return rowElement;
						        }
						    },
						   {
						        targets: 0,
						        render: function (data, type, row) {
						            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
						            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
						            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
						            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaBoveda(\'' + row.uuid + '\');">'+BTN_ELIMINAR_MENU+'</a>';
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
					  window.__boveda_booting = false; 
		              
		           },
				  drawCallback: function () {
					  
				  }
			});
			
		} catch(e) {
			alert('usuarios()_'+e);
		};
		
		tablaDetalleBoveda.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 



	function abreModal(opcion, id) { 
		switch(opcion) {
		case "nuevo" :
			$("#btnSometer").prop('disabled', false);
			iniciaFormCatalogo();    	
			$('#myModalDetalle').modal('show');
			break;
		default :
		}
	}

	


	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		$("#frm-Carga-XML").find('.has-success').removeClass("has-success");
	    $("#frm-Carga-XML").find('.has-error').removeClass("has-error");
		$('#frm-Carga-XML')[0].reset(); 
		$('#frm-Carga-XML').removeClass("was-validated"); 
		   
	}
	
	
	
	function eliminaBoveda(idRegistro){
		try{
			
			if (idRegistro == 'MULTIPLE'){
				idRegistro =  getElementosFilter();
			}
			
			
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
				           url:  '/siarex247/cumplimientoFiscal/boveda/recibidos/eliminarBoveda.action',
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
			 							$('#tablaDetalleBoveda').DataTable().ajax.reload(null,false);
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
	

	
	function eliminaBovedaMenu(){
		try{
			var idRegistro = getElementosFilter();
			if (idRegistro == ''){
				Swal.fire({
	    			title: MSG_ERROR_OPERACION_MENU,
	                	//html: '<p>¡ Debe seleccionar al menos un registro para eliminar !</p>',	
	    				html: '<p>'+LABEL_BOVEDA_TEXT3+'</p>',
	                	icon: 'info'
	    		});
			}else {
				eliminaBoveda('MULTIPLE');
			}
			 
			
		}catch(e){
			alert('eliminaBovedaMenu()_'+e);
		}
		
	}
	

	function procesaValidar(){
			
			try{
				$("#btnSometer").prop('disabled', true);
		            var formData = new FormData(document.getElementById("frm-Carga-XML"));
		            formData.append("dato", "valor");

		            $.ajax({
		            	url: '/siarex247/cumplimientoFiscal/boveda/recibidos/iniciaCargaXML.action',
		                dataType: "json",
		                beforeSend: function( xhr ) {
		        			$('#overSeccionCA').css({display:'block'});
		        		},
		                type: "post",
		                data: formData,
		                cache: false,
		                contentType: false,
			    		processData: false,
			    		complete: function(jqXHR, textStatus){
				    		  $('#overSeccionCA').css({display:'none'});
				    		  $("#btnSometer").prop('disabled', false);
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
		 							 $('#myModalDetalle').modal('hide');
		  	 						 $('#tablaDetalleBoveda').DataTable().ajax.reload(null,false);
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
		 							 $('#myModalDetalle').modal('hide');
		  	 						 $('#tablaDetalleBoveda').DataTable().ajax.reload(null,false);
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
	
	
	
	function generaArchivo(tipo, idRegistro){
		 document.getElementById('idRegistroP').value = idRegistro;
		 document.getElementById('tipoArchivoP').value = tipo;
	     document.frmBoveda.submit();
	}
	
	
	
	var seleccionados = [];
	var listaSel = '';
	var x = 0;
	
	

	function iniciaFormNotificacionRecibidos(){
		/* Reset al Formulario */ 
		$("#frmConfirmEmail_Recibidos").find('.has-success').removeClass("has-success");
	    $("#frmConfirmEmail_Recibidos").find('.has-error').removeClass("has-error");
		$('#frmConfirmEmail_Recibidos')[0].reset(); 
		$('#frmConfirmEmail_Recibidos').removeClass("was-validated"); 
		   
	}
	
	
	function consultarTotalesRecibidos() {
		try{
			var fechaInicial = obtenerFechaIni_Recibidos();
			var fechaFinal =   obtenerFechaFin_Recibidos();

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
							iniciaFormDescargaCFDI();
							 asignarCorreo();
							 $('#modalDescargaCFDI').modal('show');
						} else {
							Swal.fire({
	  			                //title: '¡Rango de fechas no valido!',
								title: LABEL_BOVEDA_TEXT5,
	  			                html: '<p>'+data.mensajeError+'</p>',	
	  			                icon: 'info'
	  			            });
						}
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('consultarTotalesRecibidos()_'+thrownError);
				}
			});	
			
			
			 
		}catch(e){
			alert('consultarTotalesRecibidos()_'+e);
		}
	}
	
	
	/*
	 
	function consultarTotalesRecibidos(){
		try{
			var uuidRegistro = '';
	        var dataSelect = tablaDetalleBoveda.rows('.selected').data(); 
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

					var fechaInicial  = obtenerFechaIni_Recibidos();
					var fechaFinal = obtenerFechaFin_Recibidos();
					var rfc = obtenerRFC_Recibidos();
					var razonSocial = obtenerRazon_Recibidos();
					var uuid = obtenerUUID_Recibidos();
					var tipoComprobante = obtenerComprobante_Recibidos();
					var serie = obtenerSerie_Recibidos();
					var folio  = obtenerFolio_Recibidos();
					
					$.ajax({
						url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/obtenerTotales.action',
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
									descargarCFDI();
								}else{
									iniciaFormNotificacionRecibidos();
									$("#myModalNotifica_Recibidos").modal("show");
									$("#emailNotificacion_Recibidos").focus();
									
								}
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert('consultarTotalesRecibidos()_'+thrownError);
						}
					});	
				}else{
					descargarCFDI();
					
				}
			}
		}catch(e){
			alert('consultarTotalesRecibidos()_'+e);
		}
	}
	 
	*/
	
	function descargarCFDI(){
	  try{
	    // ===== 1) UUIDs seleccionados en la tabla =====
	    var uuidRegistro = '';
	    var dataSelect = tablaDetalleBoveda.rows('.selected').data();
	    $.each(dataSelect, function(key, row) {
	      uuidRegistro += (row.uuid || '') + ';';
	    });

	    // ===== 2) Filtros base (ya los tenías) =====
	    var fechaInicial    = obtenerFechaIni_Recibidos();
	    var fechaFinal      = obtenerFechaFin_Recibidos();
	    var rfc             = obtenerRFC_Recibidos();
	    var razonSocial     = obtenerRazon_Recibidos();
	    var uuid            = obtenerUUID_Recibidos();
	    var tipoComprobante = obtenerComprobante_Recibidos();
	    var serie           = obtenerSerie_Recibidos();
	    var folio           = obtenerFolio_Recibidos();

	    var correoResponsable = $('#CORREO_RESPONSABLE').val();
	    var modoAgrupar       = $('#modoAgrupar').val();

	    var validarSAT     = $('#validarSAT').prop('checked');
	    var complementoSAT = $('#complementoSAT').prop('checked');
	    var notaCreditoSAT = $('#notaCreditoSAT').prop('checked');

	    // ===== 3) Operadores y valores avanzados (mismos que usa la grilla) =====
	    // Texto
	    var rfcOp   = ($('#rfcOperator').val()   || 'contains').toLowerCase();
	    var razonOp = ($('#razonOperator').val() || 'contains').toLowerCase();
	    var serieOp = ($('#serieOperator').val() || 'contains').toLowerCase();
	    var tipoOp  = ($('#tipoOperator').val()  || 'equals').toLowerCase();
	    var uuidOp  = ($('#uuidOperator').val()  || 'contains').toLowerCase();

	    // Fecha
	    var dateOp = ($('#dateOperator').val() || 'eq').toLowerCase();
	    var dateV1 = $('#dateFilter1').val() || '';
	    var dateV2 = $('#dateFilter2').val() || '';

	    // Numéricos (ajusta IDs si tus inputs tienen otros nombres)
	    var folioOp = ($('#folioOperator').val() || '').toLowerCase();
	    var folioV1 = $('#folioFilter1').val() || '';
	    var folioV2 = $('#folioFilter2').val() || '';

	    var totalOp = ($('#totalOperator').val() || '').toLowerCase();
	    var totalV1 = $('#totalFilter1').val() || '';
	    var totalV2 = $('#totalFilter2').val() || '';

	    var subOp = ($('#subOperator').val() || '').toLowerCase();
	    var subV1 = $('#subFilter1').val() || '';
	    var subV2 = $('#subFilter2').val() || '';

	    var ivaOp = ($('#ivaOperator').val() || '').toLowerCase();
	    var ivaV1 = $('#ivaFilter1').val() || '';
	    var ivaV2 = $('#ivaFilter2').val() || '';

	    var ivaRetOp = ($('#ivaRetOperator').val() || '').toLowerCase();
	    var ivaRetV1 = $('#ivaRetFilter1').val() || '';
	    var ivaRetV2 = $('#ivaRetFilter2').val() || '';

	    var isrOp = ($('#isrOperator').val() || '').toLowerCase();
	    var isrV1 = $('#isrFilter1').val() || '';
	    var isrV2 = $('#isrFilter2').val() || '';

	    var impLocOp = ($('#impLocOperator').val() || '').toLowerCase();
	    var impLocV1 = $('#impLocFilter1').val() || '';
	    var impLocV2 = $('#impLocFilter2').val() || '';

	    // ===== 4) POST al backend incluyendo TODOS los filtros =====
	    $.ajax({
	      url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/exportBovedaZIP.action',
	      type : 'POST',
	      data : {
	        // base
	        rfc            : rfc,
	        razonSocial    : razonSocial,
	        fechaInicial   : fechaInicial,
	        fechaFinal     : fechaFinal,
	        uuid           : uuid,
	        tipoComprobante: tipoComprobante,
	        serie          : serie,
	        folio          : folio,
	        correoResponsable: correoResponsable,
	        modoAgrupar    : modoAgrupar,
	        validarSAT     : validarSAT,
	        complementoSAT : complementoSAT,
	        notaCreditoSAT : notaCreditoSAT,
	        idRegistro     : uuidRegistro, // lista "uuid1;uuid2;..."

	        // === operadores de texto ===
	        rfcOperator    : rfcOp,
	        razonOperator  : razonOp,
	        serieOperator  : serieOp,
	        tipoOperator   : tipoOp,
	        uuidOperator   : uuidOp,

	        // === fecha con operador ===
	        dateOperator   : dateOp,  // eq, ne, lt, gt, le, ge, bt
	        dateV1         : dateV1,  // YYYY-MM-DD
	        dateV2         : dateV2,  // YYYY-MM-DD

	        // === numéricos ===
	        folioOperator  : folioOp, folioV1 : folioV1, folioV2 : folioV2,
	        totalOperator  : totalOp, totalV1 : totalV1, totalV2 : totalV2,
	        subOperator    : subOp,   subV1   : subV1,   subV2   : subV2,
	        ivaOperator    : ivaOp,   ivaV1   : ivaV1,   ivaV2   : ivaV2,
	        ivaRetOperator : ivaRetOp,ivaRetV1: ivaRetV1,ivaRetV2: ivaRetV2,
	        isrOperator    : isrOp,   isrV1   : isrV1,   isrV2   : isrV2,
	        impLocOperator : impLocOp,impLocV1: impLocV1,impLocV2: impLocV2
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
	              $('#modalDescargaCFDI').modal('hide');
	            }
	          });
	        } else {
	          Swal.fire({
	            title: MSG_ERROR_OPERACION_MENU,
	            html : '<p>'+ (data.mensajeError || 'Error al iniciar la descarga') +'</p>',
	            icon : 'error'
	          });
	        }
	      },
	      error : function(xhr, ajaxOptions, thrownError) {
	        alert('descargarCFDI()_'+thrownError);
	      }
	    });

	  } catch(e){
	    alert('descargarCFDI()_'+e);
	  }
	}

	
	/*
	function descargarCFDI(){
	      try{
	          var uuidRegistro = '';
	          var dataSelect = tablaDetalleBoveda.rows('.selected').data(); 
				$.each(dataSelect, function(key, row) {
					uuidRegistro+= row.uuid + ";"		
			    });
				
					var arrElementos = uuidRegistro.split(';');
					if (arrElementos.length > 50000){
						Swal.fire({
			    			title: MSG_ERROR_OPERACION_MENU,
			                	//html: '<p>¡ Debe exportar una cantidad menor a 50000 registros !</p>',	
			    				html: '<p>'+LABEL_BOVEDA_TEXT4+'</p>',
			                	icon: 'info'
			    		});
					}else{
						var fechaInicial  = obtenerFechaIni_Recibidos();
						var fechaFinal = obtenerFechaFin_Recibidos();
						var rfc = obtenerRFC_Recibidos();
						var razonSocial = obtenerRazon_Recibidos();
						var uuid = obtenerUUID_Recibidos();
						var tipoComprobante = obtenerComprobante_Recibidos();
						var serie = obtenerSerie_Recibidos();
						var folio  = obtenerFolio_Recibidos();

						document.getElementById('fechaInicialZIP').value = fechaInicial;
						document.getElementById('fechaFinalZIP').value = fechaFinal;
						document.getElementById('rfcZIP').value = rfc;
						document.getElementById('razonSocialZIP').value = razonSocial;
						document.getElementById('uuidBovedaZIP').value = uuid;
						document.getElementById('tipoComprobanteZIP').value = tipoComprobante;
						document.getElementById('serieZIP').value = serie;
						document.getElementById('folioZIP').value = folio;
						document.getElementById('idRegistroZIP').value = uuidRegistro;				
						document.frmBovedaZIP.submit();	
					}
	      }
	      catch(e){
	          alert('descargarCFDI(): '+e);
	      }
	  }
	
	*/
	
 // pillo
 function convXMLAExcel(){
   var uuidRegistro = '';
   var bandFiltros = false;

   try{
	
		var fechaInicial = obtenerFechaIni_Recibidos();
		var fechaFinal =   obtenerFechaFin_Recibidos();

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
						// 1) UUIDs seleccionados
						    var dataSelect = tablaDetalleBoveda.rows('.selected').data();
						    $.each(dataSelect, function(key, row) {
						      uuidRegistro += (row.uuid || '') + ';';
						    });
						    if (bandFiltros){
						      // uuidRegistro = getElementos(); // si algún día usas otra selección
						    }

						    // 2) Valores base (ya los tenías)
						  //  var fechaInicial    = obtenerFechaIni_Recibidos();
						  //  var fechaFinal      = obtenerFechaFin_Recibidos();
						    var rfc             = obtenerRFC_Recibidos();
						    var razonSocial     = obtenerRazon_Recibidos();
						    var uuid            = obtenerUUID_Recibidos();
						    var tipoComprobante = obtenerComprobante_Recibidos();
						    var serie           = obtenerSerie_Recibidos();
						    var folio           = obtenerFolio_Recibidos();

						    // 3) Operadores y valores de filtros avanzados (mismos ids que usas en la grilla)
						    // Texto
						    var rfcOp   = ( $('#rfcOperator').val()   || 'contains' ).toLowerCase();
						    var razOp   = ( $('#razonOperator').val() || 'contains' ).toLowerCase();
						    var serOp   = ( $('#serieOperator').val() || 'contains' ).toLowerCase();
						    var tipoOp  = ( $('#tipoOperator').val()  || 'equals'   ).toLowerCase();
						    var uuidOp  = ( $('#uuidOperator').val()  || 'contains' ).toLowerCase();

						    // Fecha
						    var dateOp  = ( $('#dateOperator').val()  || 'eq' ).toLowerCase();
						    var dateV1  = $('#dateFilter1').val() || '';
						    var dateV2  = $('#dateFilter2').val() || '';

						    // Numéricos (ajusta ids si tus inputs tienen otros nombres)
						    var folioOp = ( $('#folioOperator').val() || '' ).toLowerCase();
						    var folioV1 = $('#folioFilter1').val() || '';
						    var folioV2 = $('#folioFilter2').val() || '';

						    var totalOp = ( $('#totalOperator').val() || '' ).toLowerCase();
						    var totalV1 = $('#totalFilter1').val() || '';
						    var totalV2 = $('#totalFilter2').val() || '';

						    var subOp   = ( $('#subOperator').val()   || '' ).toLowerCase();
						    var subV1   = $('#subFilter1').val() || '';
						    var subV2   = $('#subFilter2').val() || '';

						    var ivaOp   = ( $('#ivaOperator').val()   || '' ).toLowerCase();
						    var ivaV1   = $('#ivaFilter1').val() || '';
						    var ivaV2   = $('#ivaFilter2').val() || '';

						    var ivaRetOp= ( $('#ivaRetOperator').val()|| '' ).toLowerCase();
						    var ivaRetV1= $('#ivaRetFilter1').val() || '';
						    var ivaRetV2= $('#ivaRetFilter2').val() || '';

						    var isrOp   = ( $('#isrOperator').val()   || '' ).toLowerCase();
						    var isrV1   = $('#isrFilter1').val() || '';
						    var isrV2   = $('#isrFilter2').val() || '';

						    var impLocOp= ( $('#impLocOperator').val()|| '' ).toLowerCase();
						    var impLocV1= $('#impLocFilter1').val() || '';
						    var impLocV2= $('#impLocFilter2').val() || '';

						    // 4) Helper: asegura (o crea) hidden inputs en el form antes del submit
						    function ensureHidden(form, id, name, value){
						      var el = document.getElementById(id);
						      if(!el){
						        el = document.createElement('input');
						        el.type = 'hidden';
						        el.id = id;
						        el.name = name; // el 'name' es lo que llega al Action (getXxx())
						        form.appendChild(el);
						      }
						      el.value = (value == null ? '' : value);
						    }

						    var form = document.forms['frmBovedaXMLExcel'];

						    // 5) Set de los campos base (los que ya tenías)
						    ensureHidden(form, 'fechaInicial_ConvXML',  'fechaInicial',  fechaInicial);
						    ensureHidden(form, 'fechaFinal_ConvXML',    'fechaFinal',    fechaFinal);
						    ensureHidden(form, 'rfcXMLExcel',           'rfc',           rfc);
						    ensureHidden(form, 'razonSocialXMLExcel',   'razonSocial',   razonSocial);
						    ensureHidden(form, 'uuidBovedaXMLExcel',    'uuid',          uuid);
						    ensureHidden(form, 'tipoComprobanteXMLExcel','tipoComprobante', tipoComprobante);
						    ensureHidden(form, 'serieXMLExcel',         'serie',         serie);
						    ensureHidden(form, 'folioXMLExcel',         'folio',         folio);
						    ensureHidden(form, 'idRegistroXMLExcel',    'cadRegistros',  uuidRegistro);

						    // 6) NUEVO: operadores y filtros avanzados (names deben coincidir con getters del Action)
						    // Texto
						    ensureHidden(form, 'rfcOperatorXMLExcel',   'rfcOperator',   rfcOp);
						    ensureHidden(form, 'razonOperatorXMLExcel', 'razonOperator', razOp);
						    ensureHidden(form, 'serieOperatorXMLExcel', 'serieOperator', serOp);
						    ensureHidden(form, 'tipoOperatorXMLExcel',  'tipoOperator',  tipoOp);
						    ensureHidden(form, 'uuidOperatorXMLExcel',  'uuidOperator',  uuidOp);

						    // Fecha
						    ensureHidden(form, 'dateOperatorXMLExcel',  'dateOperator',  dateOp);
						    ensureHidden(form, 'dateV1XMLExcel',        'dateV1',        dateV1);
						    ensureHidden(form, 'dateV2XMLExcel',        'dateV2',        dateV2);

						    // Numéricos
						    ensureHidden(form, 'folioOperatorXMLExcel', 'folioOperator', folioOp);
						    ensureHidden(form, 'folioV1XMLExcel',       'folioV1',       folioV1);
						    ensureHidden(form, 'folioV2XMLExcel',       'folioV2',       folioV2);

						    ensureHidden(form, 'totalOperatorXMLExcel', 'totalOperator', totalOp);
						    ensureHidden(form, 'totalV1XMLExcel',       'totalV1',       totalV1);
						    ensureHidden(form, 'totalV2XMLExcel',       'totalV2',       totalV2);

						    ensureHidden(form, 'subOperatorXMLExcel',   'subOperator',   subOp);
						    ensureHidden(form, 'subV1XMLExcel',         'subV1',         subV1);
						    ensureHidden(form, 'subV2XMLExcel',         'subV2',         subV2);

						    ensureHidden(form, 'ivaOperatorXMLExcel',   'ivaOperator',   ivaOp);
						    ensureHidden(form, 'ivaV1XMLExcel',         'ivaV1',         ivaV1);
						    ensureHidden(form, 'ivaV2XMLExcel',         'ivaV2',         ivaV2);

						    ensureHidden(form, 'ivaRetOperatorXMLExcel','ivaRetOperator',ivaRetOp);
						    ensureHidden(form, 'ivaRetV1XMLExcel',      'ivaRetV1',      ivaRetV1);
						    ensureHidden(form, 'ivaRetV2XMLExcel',      'ivaRetV2',      ivaRetV2);

						    ensureHidden(form, 'isrOperatorXMLExcel',   'isrOperator',   isrOp);
						    ensureHidden(form, 'isrV1XMLExcel',         'isrV1',         isrV1);
						    ensureHidden(form, 'isrV2XMLExcel',         'isrV2',         isrV2);

						    ensureHidden(form, 'impLocOperatorXMLExcel','impLocOperator',impLocOp);
						    ensureHidden(form, 'impLocV1XMLExcel',      'impLocV1',      impLocV1);
						    ensureHidden(form, 'impLocV2XMLExcel',      'impLocV2',      impLocV2);

						    // 7) Submit
						    form.submit();
					} else {
						Swal.fire({
  			                //title: '¡Rango de fechas no valido!',
							title: LABEL_BOVEDA_TEXT5,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'info'
  			            });
					}
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('convXMLAExcel()_'+thrownError);
			}
		});	

   }catch(e){
     alert('convXMLAExcel(): ' + e);
   }
 }

	
	  


	  function abrirExcel(nombreArchivo){
	  	 try{
	  		 window.open('/siarex247/files/'+nombreArchivo,'_blank');
	  	 }catch(e){
	  		 alert('abrirExcel()_'+e);
	  	 }
	  }
	  
	
	  function vincularComplementos(){
	    try{
			
			var fechaInicial = obtenerFechaIni_Recibidos();
			var fechaFinal =   obtenerFechaFin_Recibidos();

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
							Swal.fire({
								        icon: 'question',
								        title: LABEL_BOVEDA_TEXT1,
								        showDenyButton: true,
								        showCancelButton: false,
								        confirmButtonText: BTN_ACEPTAR_MENU,
								        denyButtonText: BTN_CANCELAR_MENU
								      }).then((result) => {
								        if (!result.isConfirmed) return;

								        $('#btnVincularComplemento').prop('disabled', true).show();

								        // ===== filtros base =====
								        var rfc             = obtenerRFC_Recibidos();
								        var razonSocial     = obtenerRazon_Recibidos();
								        var uuid            = obtenerUUID_Recibidos();
								        var tipoComprobante = obtenerComprobante_Recibidos();
								        var serie           = obtenerSerie_Recibidos();
								        var folio           = obtenerFolio_Recibidos();

								        // ===== operadores texto =====
								        var rfcOp   = ($('#rfcOperator').val()   || 'contains').toLowerCase();
								        var razonOp = ($('#razonOperator').val() || 'contains').toLowerCase();
								        var serieOp = ($('#serieOperator').val() || 'contains').toLowerCase();
								        var tipoOp  = ($('#tipoOperator').val()  || 'equals').toLowerCase();
								        var uuidOp  = ($('#uuidOperator').val()  || 'contains').toLowerCase();

								        // ===== fecha con operador =====
								        var dateOp = ($('#dateOperator').val() || 'eq').toLowerCase();
								        var dateV1 = $('#dateFilter1').val() || '';
								        var dateV2 = $('#dateFilter2').val() || '';

								        // ===== numéricos (ajusta IDs si difieren en tu UI) =====
								        var folioOp = ($('#folioOperator').val() || '').toLowerCase();
								        var folioV1 = $('#folioFilter1').val() || '';
								        var folioV2 = $('#folioFilter2').val() || '';

								        var totalOp = ($('#totalOperator').val() || '').toLowerCase();
								        var totalV1 = $('#totalFilter1').val() || '';
								        var totalV2 = $('#totalFilter2').val() || '';

								        var subOp = ($('#subOperator').val() || '').toLowerCase();
								        var subV1 = $('#subFilter1').val() || '';
								        var subV2 = $('#subFilter2').val() || '';

								        var ivaOp = ($('#ivaOperator').val() || '').toLowerCase();
								        var ivaV1 = $('#ivaFilter1').val() || '';
								        var ivaV2 = $('#ivaFilter2').val() || '';

								        var ivaRetOp = ($('#ivaRetOperator').val() || '').toLowerCase();
								        var ivaRetV1 = $('#ivaRetFilter1').val() || '';
								        var ivaRetV2 = $('#ivaRetFilter2').val() || '';

								        var isrOp = ($('#isrOperator').val() || '').toLowerCase();
								        var isrV1 = $('#isrFilter1').val() || '';
								        var isrV2 = $('#isrFilter2').val() || '';

								        var impLocOp = ($('#impLocOperator').val() || '').toLowerCase();
								        var impLocV1 = $('#impLocFilter1').val() || '';
								        var impLocV2 = $('#impLocFilter2').val() || '';

								        $.ajax({
								          url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/infoVincular.action',
								          type : 'POST',
								          dataType : 'json',
								          data : {
								            // base
								            fechaInicial : fechaInicial,
								            fechaFinal   : fechaFinal,
								            rfc          : rfc,
								            razonSocial  : razonSocial,
								            uuid         : uuid,
								            tipoComprobante : tipoComprobante,
								            serie        : serie,
								            folio        : folio,

								            // operadores texto
								            rfcOperator   : rfcOp,
								            razonOperator : razonOp,
								            serieOperator : serieOp,
								            tipoOperator  : tipoOp,
								            uuidOperator  : uuidOp,

								            // fecha con operador
								            dateOperator  : dateOp,
								            dateV1        : dateV1,
								            dateV2        : dateV2,

								            // numéricos
								            folioOperator : folioOp, folioV1 : folioV1, folioV2 : folioV2,
								            totalOperator : totalOp, totalV1 : totalV1, totalV2 : totalV2,
								            subOperator   : subOp,   subV1   : subV1,   subV2   : subV2,
								            ivaOperator   : ivaOp,   ivaV1   : ivaV1,   ivaV2   : ivaV2,
								            ivaRetOperator: ivaRetOp,ivaRetV1: ivaRetV1,ivaRetV2: ivaRetV2,
								            isrOperator   : isrOp,   isrV1   : isrV1,   isrV2   : isrV2,
								            impLocOperator: impLocOp,impLocV1: impLocV1,impLocV2: impLocV2
								          },
								          success: function(data){
								            var totalXML = (data && data.totalXML != null) ? data.totalXML : 0;
								            $('#totalXML').val(totalXML);
								            $('#myModalVincular').modal('show');
								          },
								          error: function(xhr, ajaxOptions, thrownError){
								            alert('vincularComplementos()_' + thrownError);
								          },
								          complete: function(){
								            $('#btnVincularComplemento').prop('disabled', false);
								          }
								        });
								      });
						} else {
							Swal.fire({
	  			                //title: '¡Rango de fechas no valido!',
								title: LABEL_BOVEDA_TEXT5,
	  			                html: '<p>'+data.mensajeError+'</p>',	
	  			                icon: 'info'
	  			            });
						}
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('vincularComplementos()_'+thrownError);
				}
			});	
	      
	    }catch(e){
	      alert('vincularComplementos()_' + e);
	    }
	  }

	
	  function vincularComplementosBoveda(){
	    try{
			
			var fechaInicial = obtenerFechaIni_Recibidos();
			var fechaFinal =   obtenerFechaFin_Recibidos();

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

							      Swal.fire({
							        icon: 'question',
							        title: LABEL_BOVEDA_TEXT1,
							        showDenyButton: true,
							        showCancelButton: false,
							        confirmButtonText: BTN_ACEPTAR_MENU,
							        denyButtonText: BTN_CANCELAR_MENU
							      }).then((result) => {
							        if (!result.isConfirmed) return;

							        $('#btnVincularComplementoBoveda').prop('disabled', true).show();

							        // ===== filtros base (mismos getters de Recibidos) =====
							        var rfc             = obtenerRFC_Recibidos();
							        var razonSocial     = obtenerRazon_Recibidos();
							        var uuid            = obtenerUUID_Recibidos();
							        var tipoComprobante = obtenerComprobante_Recibidos();
							        var serie           = obtenerSerie_Recibidos();
							        var folio           = obtenerFolio_Recibidos();

							        // ===== operadores texto =====
							        var rfcOp   = ($('#rfcOperator').val()   || 'contains').toLowerCase();
							        var razonOp = ($('#razonOperator').val() || 'contains').toLowerCase();
							        var serieOp = ($('#serieOperator').val() || 'contains').toLowerCase();
							        var tipoOp  = ($('#tipoOperator').val()  || 'equals').toLowerCase();
							        var uuidOp  = ($('#uuidOperator').val()  || 'contains').toLowerCase();

							        // ===== fecha con operador =====
							        var dateOp = ($('#dateOperator').val() || '').toLowerCase();
							        var dateV1 = $('#dateFilter1').val() || '';
							        var dateV2 = $('#dateFilter2').val() || '';

							        // ===== numéricos (ajusta IDs si difieren en tu UI) =====
							        var folioOp = ($('#folioOperator').val() || '').toLowerCase();
							        var folioV1 = $('#folioFilter1').val() || '';
							        var folioV2 = $('#folioFilter2').val() || '';

							        var totalOp = ($('#totalOperator').val() || '').toLowerCase();
							        var totalV1 = $('#totalFilter1').val() || '';
							        var totalV2 = $('#totalFilter2').val() || '';

							        var subOp = ($('#subOperator').val() || '').toLowerCase();
							        var subV1 = $('#subFilter1').val() || '';
							        var subV2 = $('#subFilter2').val() || '';

							        var ivaOp = ($('#ivaOperator').val() || '').toLowerCase();
							        var ivaV1 = $('#ivaFilter1').val() || '';
							        var ivaV2 = $('#ivaFilter2').val() || '';

							        var ivaRetOp = ($('#ivaRetOperator').val() || '').toLowerCase();
							        var ivaRetV1 = $('#ivaRetFilter1').val() || '';
							        var ivaRetV2 = $('#ivaRetFilter2').val() || '';

							        var isrOp = ($('#isrOperator').val() || '').toLowerCase();
							        var isrV1 = $('#isrFilter1').val() || '';
							        var isrV2 = $('#isrFilter2').val() || '';

							        var impLocOp = ($('#impLocOperator').val() || '').toLowerCase();
							        var impLocV1 = $('#impLocFilter1').val() || '';
							        var impLocV2 = $('#impLocFilter2').val() || '';

							        $.ajax({
							          url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/infoVincularBoveda.action',
							          type : 'POST',
							          dataType : 'json',
							          data : {
							            // base
							            fechaInicial : fechaInicial,
							            fechaFinal   : fechaFinal,
							            rfc          : rfc,
							            razonSocial  : razonSocial,
							            uuid         : uuid,
							            tipoComprobante : tipoComprobante,
							            serie        : serie,
							            folio        : folio,

							            // operadores texto
							            rfcOperator   : rfcOp,
							            razonOperator : razonOp,
							            serieOperator : serieOp,
							            tipoOperator  : tipoOp,
							            uuidOperator  : uuidOp,

							            // fecha con operador
							            dateOperator  : dateOp,
							            dateV1        : dateV1,
							            dateV2        : dateV2,

							            // numéricos
							            folioOperator : folioOp, folioV1 : folioV1, folioV2 : folioV2,
							            totalOperator : totalOp, totalV1 : totalV1, totalV2 : totalV2,
							            subOperator   : subOp,   subV1   : subV1,   subV2   : subV2,
							            ivaOperator   : ivaOp,   ivaV1   : ivaV1,   ivaV2   : ivaV2,
							            ivaRetOperator: ivaRetOp,ivaRetV1: ivaRetV1,ivaRetV2: ivaRetV2,
							            isrOperator   : isrOp,   isrV1   : isrV1,   isrV2   : isrV2,
							            impLocOperator: impLocOp,impLocV1: impLocV1,impLocV2: impLocV2
							          },
							          success: function(data){
							            var totalXML = (data && data.totalXML != null) ? data.totalXML : 0;
							            $('#totalXML_Boveda').val(totalXML);
							            $('#myModalVincularComplemento').modal('show');
							          },
							          error: function(xhr, ajaxOptions, thrownError){
							            alert('vincularComplementosBoveda()_' + thrownError);
							          },
							          complete: function(){
							            $('#btnVincularComplementoBoveda').prop('disabled', false);
							          }
							        });

							      });
						} else {
							Swal.fire({
	  			                //title: '¡Rango de fechas no valido!',
								title: LABEL_BOVEDA_TEXT5,
	  			                html: '<p>'+data.mensajeError+'</p>',	
	  			                icon: 'info'
	  			            });
						}
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('vincularComplementosBoveda_'+thrownError);
				}
			});	
			
			
		
	    }catch(e){
	      alert('vincularComplementosBoveda()_' + e);
	    }
	  }


	 function getElementos(){
			var llaveRegistros = '';
			try{
				 var table = $('#tablaDetalleBoveda').DataTable();
				 var bandPrimero = true;
				 table.column(14, { search:'applied' }).data().each(function(value, index) {
					 if (bandPrimero){
						 llaveRegistros = value;
					 }else{
						 llaveRegistros = llaveRegistros +  ";"  + value;
					 }
					 bandPrimero = false;
				 });
			}catch(e){
				alert('getElementos()_'+e);
			}
			return llaveRegistros;
		}	
	 

	 function getElementosFilter(){
		 var llavesUUID = '';
		 try{
			 var dataSelect = tablaDetalleBoveda.rows('.selected').data(); 
				$.each(dataSelect, function(key, row) {
					llavesUUID+= row.uuid + ";"		
			    });

		 }catch(e){
			 alert('getElementosFilter()_'+e);
		 }
		 return llavesUUID;
	 }
	 

	 
	 
	function consultarFecha(){
		$.ajax({
			url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/consultarFecha.action',
			type : 'POST', 
			data : null,
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					//document.getElementById("fechaUltimaActualizacion").innerHTML = LABEL_BOVEDA_ETQ8 + ' ' + data.fechaDescarga;
					document.getElementById("BOVEDA_ETQ8").innerHTML = LABEL_BOVEDA_ETQ8 + ' ' + data.fechaDescarga;
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('buscaCatalogo()_'+thrownError);
			}
		});	
		
    }
	
	
	
	// ÚNICA versión
	window.__rb_hits = 0;
	function refrescarBoveda() {
	  const now = Date.now();

	  // si hay petición en curso, o la última fue hace <350ms, no hagas nada
	  if (window.__boveda_inflight) return;
	  if (now - (window.__boveda_lastReload || 0) < 350) return;
	  window.__boveda_lastReload = now;
	  if ($.fn.DataTable.isDataTable('#tablaDetalleBoveda')) {
		//tablaDetalleBoveda.page('first').draw('page');
	    $('#tablaDetalleBoveda').DataTable().ajax.reload(null, true);
	  }
	}

	
	function exportExcelRecibidos(){
	  try{
	    // ===== Valores base (los que ya usabas) =====
	    var fechaInicial    = obtenerFechaIni_Recibidos();
	    var fechaFinal      = obtenerFechaFin_Recibidos();
	    var rfc             = obtenerRFC_Recibidos();
	    var razonSocial     = obtenerRazon_Recibidos();
	    var uuid            = obtenerUUID_Recibidos();
	    var tipoComprobante = obtenerComprobante_Recibidos();
	    var serie           = obtenerSerie_Recibidos();
	    var folio           = obtenerFolio_Recibidos();

	    // ===== Operadores DX-like (opcionales; si no existen, no estorban) =====
	    var rfcOp    = ($('#rfcOperator').val()   || 'contains').toLowerCase();
	    var razonOp  = ($('#razonOperator').val() || 'contains').toLowerCase();
	    var serieOp  = ($('#serieOperator').val() || 'contains').toLowerCase();
	    var tipoOp   = ($('#tipoOperator').val()  || 'equals').toLowerCase();
	    var uuidOp   = ($('#uuidOperator').val()  || 'contains').toLowerCase();

	    var dateOp   = ($('#dateOperator').val() || 'eq').toLowerCase();
	    var dateV1   = $('#dateFilter1').val() || '';
	    var dateV2   = $('#dateFilter2').val() || '';

	    var folioOp  = ($('#folioOperator').val() || 'eq').toLowerCase();
	    var folioV1  = $('#folioFilter1').val() || '';
	    var folioV2  = $('#folioFilter2').val() || '';

	    var totalOp  = ($('#totalOperator').val() || 'eq').toLowerCase();
	    var totalV1  = $('#totalFilter1').val() || '';
	    var totalV2  = $('#totalFilter2').val() || '';

	    var subOp    = ($('#subOperator').val() || 'eq').toLowerCase();
	    var subV1    = $('#subFilter1').val() || '';
	    var subV2    = $('#subFilter2').val() || '';

	    var ivaOp    = ($('#ivaOperator').val() || 'eq').toLowerCase();
	    var ivaV1    = $('#ivaFilter1').val() || '';
	    var ivaV2    = $('#ivaFilter2').val() || '';

	    var ivaRetOp = ($('#ivaRetOperator').val() || 'eq').toLowerCase();
	    var ivaRetV1 = $('#ivaRetFilter1').val() || '';
	    var ivaRetV2 = $('#ivaRetFilter2').val() || '';

	    var isrOp    = ($('#isrOperator').val() || 'eq').toLowerCase();
	    var isrV1    = $('#isrFilter1').val() || '';
	    var isrV2    = $('#isrFilter2').val() || '';

	    var impLocOp = ($('#impLocOperator').val() || 'eq').toLowerCase();
	    var impLocV1 = $('#impLocFilter1').val() || '';
	    var impLocV2 = $('#impLocFilter2').val() || '';

	    // ===== Helper para setear/crear hiddens =====
	    function put(id, name, val){
	      var el = document.getElementById(id);
	      if(!el){
	        el = document.createElement('input');
	        el.type = 'hidden';
	        el.id   = id;
	        el.name = name;
	        document.frmExportarDetalleExcel.appendChild(el);
	      }
	      el.value = (val == null ? '' : val);
	    }

	    // ===== Llenar form y enviar =====
	    put('rfc_Exportar',            'rfc',            rfc);
	    put('razonSocial_Exportar',    'razonSocial',    razonSocial);
	    put('folio_Exportar',          'folio',          folio);
	    put('serie_Exportar',          'serie',          serie);
	    put('fechaInicial_Exportar',   'fechaInicial',   fechaInicial);
	    put('fechaFinal_Exportar',     'fechaFinal',     fechaFinal);
	    put('tipoComprobante_Exportar','tipoComprobante',tipoComprobante);
	    put('uuid_Exportar',           'uuid',           uuid);

	    // ===== (Opcional) Operadores/valores avanzados =====
	    put('rfcOperator_Exportar',     'rfcOperator',     rfcOp);
	    put('razonOperator_Exportar',   'razonOperator',   razonOp);
	    put('serieOperator_Exportar',   'serieOperator',   serieOp);
	    put('tipoOperator_Exportar',    'tipoOperator',    tipoOp);
	    put('uuidOperator_Exportar',    'uuidOperator',    uuidOp);

	    put('dateOperator_Exportar',    'dateOperator',    dateOp);
	    put('dateV1_Exportar',          'dateV1',          dateV1);
	    put('dateV2_Exportar',          'dateV2',          dateV2);

	    put('folioOperator_Exportar',   'folioOperator',   folioOp);
	    put('folioV1_Exportar',         'folioV1',         folioV1);
	    put('folioV2_Exportar',         'folioV2',         folioV2);

	    put('totalOperator_Exportar',   'totalOperator',   totalOp);
	    put('totalV1_Exportar',         'totalV1',         totalV1);
	    put('totalV2_Exportar',         'totalV2',         totalV2);

	    put('subOperator_Exportar',     'subOperator',     subOp);
	    put('subV1_Exportar',           'subV1',           subV1);
	    put('subV2_Exportar',           'subV2',           subV2);

	    put('ivaOperator_Exportar',     'ivaOperator',     ivaOp);
	    put('ivaV1_Exportar',           'ivaV1',           ivaV1);
	    put('ivaV2_Exportar',           'ivaV2',           ivaV2);

	    put('ivaRetOperator_Exportar',  'ivaRetOperator',  ivaRetOp);
	    put('ivaRetV1_Exportar',        'ivaRetV1',        ivaRetV1);
	    put('ivaRetV2_Exportar',        'ivaRetV2',        ivaRetV2);

	    put('isrOperator_Exportar',     'isrOperator',     isrOp);
	    put('isrV1_Exportar',           'isrV1',           isrV1);
	    put('isrV2_Exportar',           'isrV2',           isrV2);

	    put('impLocOperator_Exportar',  'impLocOperator',  impLocOp);
	    put('impLocV1_Exportar',        'impLocV1',        impLocV1);
	    put('impLocV2_Exportar',        'impLocV2',        impLocV2);

	    // (Opcional) debug
	     console.log('[Bóveda/Excel] payload listo →', $(document.frmExportarDetalleExcel).serialize());

	    document.frmExportarDetalleExcel.submit();
	  } catch(e){
	    alert('exportExcelRecibidos(): ' + e);
	  }
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
					// const calendario = flatpickr("#fechaInicial_Filtro", { dateFormat: "Y-m-d" });
					// calendario.setDate(data.fechaInicial);  
					
					 //$('#fechaInicial_Filtro').val(data.fechaInicial);
					 // $('#fechaFinal_Filtro').val(data.fechaFinal);
					 
					 obtenerFechasMinima(data.fechaInicial, data.fechaFinal);
					 
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('obtenerFechasFiltro()_'+thrownError);
			}
		});	
				
	}
	

	function validarFechas(){
		//var fechaInicial = $('#fechaInicial_Recibidos').val();
		//var fechaFinal = $('#fechaFinal_Recibidos').val();

		var fechaInicial = obtenerFechaIni_Recibidos();
		var fechaFinal =   obtenerFechaFin_Recibidos();
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
						window.__bvdRec_syncThead();
						refrescarBoveda();
					} else {
						Swal.fire({
  			                //title: '¡Rango de fechas no valido!',
							title: LABEL_BOVEDA_TEXT5,
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
	
	
	function obtenerRFC_Recibidos(){
	//	var rfcRecibidos = $('#rfc_Recibidos').val();
		return $('#rfc_Recibidos').val();
	}
	
	function obtenerRazon_Recibidos(){
		return $('#razonSocial_Recibidos').val();
	}
	
	function obtenerFolio_Recibidos(){
		return $('#folio_Recibidos').val();
	}
	
	function obtenerSerie_Recibidos(){
		return $('#serie_Recibidos').val();
	}
	
	function obtenerFechaIni_Recibidos(){
		return $('#fechaInicial_Filtro').val();
	}
	
	
	function obtenerFechaFin_Recibidos(){
		return $('#fechaFinal_Filtro').val();
	}
	
	function __tipoNorm(v){
	  v = (v == null ? '' : String(v)).trim();
	  return (v.toUpperCase() === 'ALL') ? '' : v;
	}
	
	function obtenerComprobante_Recibidos(){
	  const v = ($('#tipoComprobante_Recibidos').val() || '').trim();
	  // 🔑 si el usuario elige “Todos” (ALL), al backend se le manda vacío (sin filtro)
	  return (v.toUpperCase() === 'ALL') ? '' : v;
	}


	
	function obtenerUUID_Recibidos(){
		return $('#uuid_Recibidos').val();
	}
	
	
	// 2) FUNCIÓN: limpiar todo y recargar
	function limpiarRecibidos(){
	  try{
	    // --- Campos principales (backend) ---
	    $('#rfc_Recibidos, #razonSocial_Recibidos, #folio_Recibidos, #serie_Recibidos, #fechaInicial_Recibidos, #fechaFinal_Recibidos, #uuid_Recibidos').val('');
	    $('#tipoComprobante_Recibidos').val('ALL');

	    // --- Auxiliar para operadores/etiquetas ---
	    const setOp = (hiddenSel, labelSel, val, labelHtml) => {
	      $(hiddenSel).val(val);
	      $(labelSel).html(labelHtml);
	    };

	    // --- Filtros de encabezado (texto) ---
	    $('#rfcFilterInput, #razonFilterInput, #serieFilterInput, #uuidFilterInput').val('');
	    setOp('#rfcOperator',   '#rfcOpBtn .op-label',   'contains', '<i class="fas fa-search"></i>');
	    setOp('#razonOperator', '#razonOpBtn .op-label', 'contains', '<i class="fas fa-search"></i>');
	    setOp('#serieOperator', '#serieOpBtn .op-label', 'contains', '<i class="fas fa-search"></i>');
	    setOp('#uuidOperator',  '#uuidOpBtn .op-label',  'contains', '<i class="fas fa-search"></i>');

	    // --- Tipo (select) ---
	    $('#tipoFilterInput').val('');
	    setOp('#tipoOperator', '#tipoOpBtn .op-label', 'equals', '=');

	    // --- Filtros numéricos (valor 1/2 + operador + etiqueta) ---
	    function resetNum(opSel, v1Sel, v2Sel, btnSel){
	      $(opSel).val('eq');
	      $(v1Sel).val('');
	      $(v2Sel).val('').addClass('d-none'); // oculta el 2do valor si estaba en "between"
	      $(btnSel+' .op-label').text('=');
	    }
	    resetNum('#folioOperator',   '#folioFilter1',   '#folioFilter2',   '#folioOpBtn');
	    resetNum('#totalOperator',   '#totalFilter1',   '#totalFilter2',   '#totalOpBtn');
	    resetNum('#subOperator',     '#subFilter1',     '#subFilter2',     '#subOpBtn');
	    resetNum('#ivaOperator',     '#ivaFilter1',     '#ivaFilter2',     '#ivaOpBtn');
	    resetNum('#ivaRetOperator',  '#ivaRetFilter1',  '#ivaRetFilter2',  '#ivaRetOpBtn');
	    resetNum('#isrOperator',     '#isrFilter1',     '#isrFilter2',     '#isrOpBtn');
	    resetNum('#impLocOperator',  '#impLocFilter1',  '#impLocFilter2',  '#impLocOpBtn');

	    // --- Fecha con operador ---
	    $('#dateOperator').val('eq');
	    $('#dateFilter1').val('');
	    $('#dateFilter2').val('').hide();
	    $('#dateOpBtn .op-label').text('=');

	    // --- Otros ocultos usados por tu backend ---
	    // $('#rfcGridFilter').val('');
		
		// --- Limpia el componente de rango de fechas (CRMDateRangeR) ---
		/*
		var range = document.getElementById('CRMDateRangeR');
		if (range) {
		  range.value = '';
		  if (range._flatpickr) {
		    range._flatpickr.clear(); // limpia selección visual
		  }
		}
	*/
		obtenerFechasFiltro();

	    // --- Recarga de la tabla (sin cambiar de página) ---
	    $('#tablaDetalleBoveda').DataTable().ajax.reload(null, false);
	  }catch(e){
	    alert('limpiarRecibidos()_'+e);
	  }
	}
	
	
	

	function cargaProveedoresRecibidos() {
		try {
			$.ajax({
	           url: '/siarex247/cumplimientoFiscal/boveda/recibidos/catProveedores.action',
	           type: 'POST',
	            dataType : 'json',
			    success: function(data){
			    	$('#rfc_Recibidos').empty();
			    	$.each(data.data, function(key, text) {
			    		$('#rfc_Recibidos').append($('<option></option>').attr('value', text.rfc).text(text.razonSocial));	
			    		
			      	});
			    }
			});
		}
		catch(e) {
			alert('cargaProveedoresEmitidos()_'+e);
		} 
	}
	
		
	if (!window.__bvdRec_bootstrapped) {

	  window.__bvdRec_syncThead = window.__bvdRec_syncThead || function(){
	    [
	      ['#uuidFilterInput', '#uuid_Recibidos'],
	      ['#rfcFilterInput', '#rfc_Recibidos'],
	      ['#razonFilterInput', '#razonSocial_Recibidos'],
	      ['#serieFilterInput', '#serie_Recibidos'],
	      ['#tipoFilterInput',  '#tipoComprobante_Recibidos']
	    ].forEach(([from,to]) => { if ($(from).length && $(to).length) $(to).val($(from).val()); });
	    if ($('#dateFilter1').length && $('#fechaInicial_Recibidos').length) $('#fechaInicial_Recibidos').val($('#dateFilter1').val());
	    if ($('#dateFilter2').length && $('#fechaFinal_Recibidos').length)  $('#fechaFinal_Recibidos').val($('#dateFilter2').val());
	  };

  }


