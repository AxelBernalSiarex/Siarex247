
var tablaDetalle = null;

window.__rec_firstLoadSeen = false;   // se pondrá true cuando DataTables haga su 1er xhr
	$(document).ready(function() {
		try {
			

			// B) fallback: si por cualquier razón DataTables NO lanzó xhr, disparo UNO manual
			setTimeout(function(){
			  try{
			    if (!window.__rec_firstLoadSeen) {
			      const dt = $('#tablaDetalle').DataTable();
			      if (dt) dt.ajax.reload(null, false); // 🔁 sólo si NO hubo primera carga
			    }
			  }catch(_){}
			}, 0);

			tablaDetalle = $('#tablaDetalle').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 15,
				lengthChange: false,
//				dom: 'Blfrtip',
//				dom: "<'row mx-0'<'col-md-12'QB><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				dom: "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				ordering    : true,
				serverSide	: true,
				fixedHeader : true,
				deferLoading: 0,
				orderCellsTop: true,
				info		: true,
				select      : false,
				stateSave	: false, 
				order       : [ [ 0, 'asc' ] ],	
				buttons: [
				  // 1) Limpiar (Descarga Recibidos)
				  {
				    text:
				      '<span class="fas fa-broom" data-fa-transform="shrink-3 down-2"></span>' +
				      '<span class="d-none d-sm-inline-block ms-1">Limpiar</span>',
				    className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    action: function(){ limpiarRecibidos(); } // o limpiarDescargaRecibidos() si tienes uno específico
				  },

				  {
				    text:
				      '<span class="fab fa-firefox-browser me-1"></span>' +
				      '<span class="d-none d-sm-inline-block" id="VISOR_BTN_REFRESCAR">Actualizar</span>',
				    className: 'btn btn-falcon-success btn-sm mb-1 me-1',
				    action: function (e, dt, node) {
						validarFechas();
				    }
				  },

				  // 3) Exportar (Descarga Recibidos)
				  {
				    text:
				      '<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span>' +
				      '<span class="d-none d-sm-inline-block ms-1">Exportar Excel</span>',
				    className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    action: function () { exportExcelDescarga(); }
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
				  url: '/siarex247/cumplimientoFiscal/descargaSAT/recibidos/detalleRecibidos.action',

				  beforeSend: function(xhr){
				    // 🔒 lock SOLO aquí
				    if (window.__rec_inflight) {
				      try { xhr.abort(); } catch(_){}
				      // deja una recarga en cola
				      window.__rec_queue = true;
				      return false;
				    }
				    window.__rec_inflight = true;
				    console.debug('[RECIBIDOS] beforeSend → inflight ON');
				    $('#overSeccion_Descarga_Recibidos').css({display:'block'});
				  },

				  complete: function(jqXHR, textStatus){
				    $('#overSeccion_Descarga_Recibidos').css({display:'none'});
				    window.__rec_inflight = false;
				    console.debug('[RECIBIDOS] complete → inflight OFF ('+textStatus+')');

				    if (window.__rec_queue){
				      window.__rec_queue = false;
				      if (typeof window.refrescarRecibidos === 'function'){
				        console.debug('[RECIBIDOS] ejecutar refresh en cola…');
				        window.refrescarRecibidos('queued');
				      }
				    }
				  },

				  error: function(jqXHR, textStatus, errorThrown){
				    // 🔓 no te quedes “pegado” si falla
				    $('#overSeccion_Descarga_Recibidos').css({display:'none'});
				    window.__rec_inflight = false;
				    console.error('[RECIBIDOS] AJAX error:', textStatus, errorThrown, jqXHR && jqXHR.responseText);
				    alert('No se pudo consultar Recibidos: ' + (errorThrown || textStatus));
				  },

				  data: function(d){
				    // === EXISTENTES (siguen igual) ===
					d.rfcEmisor            = obtenerRFCEmisor();
					d.razonSocialEmisor    = obtenerRazonEmisor();
					d.existeBovedaDescarga = __normSel(existeBoveda());
					//d.tipoComprobanteDescarga = __normSel(obtenerTipoComprobante());
					const efectoThead = __normSel($('#efectoFilterInputR').val());
					   d.tipoComprobanteDescarga = efectoThead || __normSel($('#tipoComprobanteDescarga').val())
					                                         || __normSel($('#efectoComprobante').val())
					                                         || __normSel($('#tipoComprobante').val())
					                                         || '';
					d.fechaInicialDescarga = obtenerFechaInicio();
					d.fechaFinalDescarga   = obtenerFechaFinal();
					d.uuidDescarga         = obtenerUUID();
					d.estatusCFDI          = __normSel(obtenerEstatus());
					
					  // Compatibilidad con backends que esperan otro nombre:
					  // Compatibilidad con backends que esperan otros nombres
					  if (d.tipoComprobanteDescarga) {
					    d.efectoComprobante = d.tipoComprobanteDescarga;
					    d.tipoComprobante   = d.tipoComprobanteDescarga;
					  }

					  // ===== Operadores (DX-like) =====
					  d.uuidOperator    = $('#uuidOperatorR').val()    || 'contains';
					  d.rfcEmiOperator  = $('#rfcEmiOperatorR').val()  || 'contains';
					  d.nomEmiOperator  = $('#nomEmiOperatorR').val()  || 'contains';
					  // Efecto/Tipo: 'eq' si hay valor, vacío si no hay filtro
					 // d.efectoOperator  = $('#efectoOperatorR').val()  || (d.tipoComprobanteDescarga ? 'eq' : '');
					 // Efecto: si hay valor, operador = eq; si no, vacío (sin filtro)
					   d.efectoOperator  = $('#efectoOperatorR').val()  || (d.tipoComprobanteDescarga ? 'eq' : '');
					  // Estatus/Bóveda también como igualdad si hay valor
					  d.estatusOperator = $('#estatusOperatorR').val() || (d.estatusCFDI ? 'eq' : '');
					  d.bovedaOperator  = $('#bovedaOperatorR').val()  || (d.existeBovedaDescarga ? 'eq' : '');

					  // RFC receptor NO se envía (backend ya fija el RFC exacto del usuario)
					  d.rfcRecOperator = '';
					  d.rfcReceptor    = '';

				    d.nomRecOperator    = $('#nomRecOperatorR').val()   || 'contains';
				    d.pacOperator       = $('#pacOperatorR').val()      || 'contains';
				    d.efectoOperator    = $('#efectoOperatorR').val()   || 'contains';
				    d.estatusOperator   = $('#estatusOperatorR').val()  || 'contains';
				    d.bovedaOperator    = $('#bovedaOperatorR').val()   || 'contains';

				    d.montoV1           = $('#montoFilter1R').val();
				    d.montoV2           = $('#montoFilter2R').val();
				    d.montoOperator     = $('#montoOperatorR').val()    || 'eq';

				    d.emiDateOperator   = $('#emiDateOperatorR').val()  || 'eq';
				    d.emiDateV1         = $('#emiDateFilter1R').val();
				    d.emiDateV2         = $('#emiDateFilter2R').val();

				    d.cerDateOperator   = $('#cerDateOperatorR').val()  || 'eq';
				    d.cerDateV1         = $('#cerDateFilter1R').val();
				    d.cerDateV2         = $('#cerDateFilter2R').val();

				    d.canDateOperator   = $('#canDateOperatorR').val()  || 'eq';
				    d.canDateV1         = $('#canDateFilter1R').val();
				    d.canDateV2         = $('#canDateFilter2R').val();
					/*console.log('[RECIBIDOS] efecto/tipo =>', {
					  thead: $('#efectoFilterInputR').val(),
					  tipoComprobanteDescarga: d.tipoComprobanteDescarga,
					  efectoComprobante: d.efectoComprobante,
					  tipoComprobante: d.tipoComprobante,
					  efectoOperator: d.efectoOperator
					}); */


				    d._ = Date.now(); // anti-cache
					


				  },

				

					
					type: 'POST'
				}
				,
				aoColumns : [
					{ mData: "uuid", "sClass": "alinearCentro"},
					{ mData: "rfcEmisor", "sClass": "alinearCentro"},
					{ mData: "nombreEmisor", "sClass": "alinearIzquierda"},
					{ mData: "rfcReceptor", "sClass": "alinearCentro"},
					{ mData: "nombreReceptor"},
					{ mData: "rfcPac", "sClass": "alinearCentro"},
					{ mData: "fechaEmision", "sClass": "alinearCentro"},
					{ mData: "fechaCertificacion", "sClass": "alinearCentro"},
					{ mData: "montoDes", "sClass": "alinearDerecha"},
					{ mData: "efectoComprobante", "sClass": "alinearCentro"},
					{ mData: "estatus", "sClass": "alinearCentro"},
					{ mData: "fechaCancelacion"},
					{ mData: "existeBoveda", "sClass": "alinearCentro"}
					
				],
		          initComplete: function () {
		        	  var btns = $('.dt-button');
		              btns.removeClass('dt-button');
		              
		              var btnsSubMenu = $('.dtb-b2');
		              btnsSubMenu.addClass('dropdown-menu dropdown-menu-end py-0 show');
		              
		           },
				  drawCallback: function () {
					  
				  }
				  
			} ); 			  // kick inicial por si el serverSide no disparó solo
			
			  
			  

			
		
			

			
		} catch(e) {
			alert('usuarios()_'+e);
		};
		
		tablaDetalle.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	function limpiarRecibidos(){
	  try{
	    // 1) Limpia globales existentes en esta pantalla
	    $('#rfcEmisor,#razonSocialEmisor,#uuidDescarga,#rfcReceptor,#nombreReceptor,#rfcPac,#tipoComprobanteDescarga,#estatusCFDI,#existeBovedaDescarga').val('');
	    $('#fechaInicialDescarga,#fechaFinalDescarga').val('');
		$('#rfcReceptor').val('');       // NO prellenar el RFC del usuario aquí
		$('#rfcRecOperatorR').val('contains');


	    // 2) Helper de operador + etiqueta
	    const setOp=(hiddenSel,labelSel,val,labelHtml)=>{ $(hiddenSel).val(val); $(labelSel).html(labelHtml); };

	    // 3) Textos del thead
	    $('#uuidFilterInputR,#rfcEmiFilterInputR,#nomEmiFilterInputR,#rfcRecFilterInputR,#nomRecFilterInputR,#pacFilterInputR,#efectoFilterInputR,#estatusFilterInputR,#bovedaFilterInputR').val('');
	    setOp('#uuidOperatorR',   '#uuidOpBtnR .op-label',     'contains','<i class="fas fa-search"></i>');
	    setOp('#rfcEmiOperatorR', '#rfcEmiOpBtnR .op-label',   'contains','<i class="fas fa-search"></i>');
	    setOp('#nomEmiOperatorR', '#nomEmiOpBtnR .op-label',   'contains','<i class="fas fa-search"></i>');
	    setOp('#rfcRecOperatorR', '#rfcRecOpBtnR .op-label',   'contains','<i class="fas fa-search"></i>');
	    setOp('#nomRecOperatorR', '#nomRecOpBtnR .op-label',   'contains','<i class="fas fa-search"></i>');
	    setOp('#pacOperatorR',    '#pacOpBtnR .op-label',      'contains','<i class="fas fa-search"></i>');
	    setOp('#efectoOperatorR', '#efectoOpBtnR .op-label',   'contains','<i class="fas fa-search"></i>');
	    setOp('#estatusOperatorR','#estatusOpBtnR .op-label',  'contains','<i class="fas fa-search"></i>');
	    setOp('#bovedaOperatorR', '#bovedaOpBtnR .op-label',   'contains','<i class="fas fa-search"></i>');

	    // 4) Numéricos
	    function resetNum(opSel,v1Sel,v2Sel,btnSel){
	      $(opSel).val('eq'); $(v1Sel).val(''); $(v2Sel).val('').addClass('d-none'); $(btnSel+' .op-label').text('=');
	    }
	    resetNum('#montoOperatorR', '#montoFilter1R', '#montoFilter2R', '#montoOpBtnR');

	    // 5) Fechas
	    function resetDate(opSel,d1Sel,d2Sel,btnSel){
	      $(opSel).val('eq'); $(d1Sel).val(''); $(d2Sel).val('').addClass('d-none'); $(btnSel+' .op-label').text('=');
	    }
	    resetDate('#emiDateOperatorR','#emiDateFilter1R','#emiDateFilter2R','#emiDateOpBtnR');
	    resetDate('#cerDateOperatorR','#cerDateFilter1R','#cerDateFilter2R','#cerDateOpBtnR');
	    resetDate('#canDateOperatorR','#canDateFilter1R','#canDateFilter2R','#canDateOpBtnR');

	    // 6) Cierra menús
	    $('#tablaDetalle thead .dx-like-menu, .dtfh-floatingparent thead .dx-like-menu').removeClass('show');
		
		// limpiar rango
		var rangeDR = document.getElementById('CRMDateRangeDR');
		if (rangeDR){ rangeDR.value=''; rangeDR._flatpickr && rangeDR._flatpickr.clear(); }
		// limpiar thead
		$('#emiDateOperatorR').val('eq');
		$('#emiDateFilter1R').val('').hide();
		$('#emiDateFilter2R').val('').hide();
		$('#emiDateOpBtnR .op-label').text('=');
		
		obtenerFechasFiltroSatR();


	    // 7) Recarga
	    if (typeof refrescarRecibidos==='function') refrescarRecibidos('clear-all');
	    else $('#tablaDetalle').DataTable().ajax.reload(null,false);

	  }catch(e){ alert('limpiarRecibidos()_'+e); }
	}
	
	function obtenerFechasFiltroSatR(){
	console.log('ENTRO');
		$.ajax({
			url  : '/siarex247/cumplimientoFiscal/boveda/nomina/consultarFechasNomina.action',
			type : 'POST', 
						data : null,
						dataType : 'json',
						success  : function(data) {
							if($.isEmptyObject(data)) {
							   
							} else {
								// $('#fechaInicial_Recibidos').val(data.fechaInicial);
								// $('#fechaFinal_Recibidos').val(data.fechaFinal);
								var fechas = data.fechaInicial + '  a  ' + data.fechaFinal;
								$('#CRMDateRangeDR').val(fechas);
								
								
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert('obtenerFechasFiltro()_'+thrownError);
						}
					});	
		
	   }

	
	
	

		
	function filtraDatos(columna, texto) {
		tablaDetalle
			.column(columna)
	        .search(texto)
	        .draw();
	}

	
	function obtenerFechasFiltroDescarga(){
			$.ajax({
				url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/consultarFechas.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						 obtenerFechasMinimaDescarga(data.fechaInicial, data.fechaFinal);
						 
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerFechasFiltroDescarga()_'+thrownError);
				}
			});	
					
		}
		
		
		function validarFechas(){
				//var fechaInicial = $('#fechaInicial_Recibidos').val();
				//var fechaFinal = $('#fechaFinal_Recibidos').val();

				var fechaInicial = obtenerFechaInicio();
				var fechaFinal =   obtenerFechaFinal();

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
								window.__descRec_syncThead();
								refrescar()
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


		
		
		
		
	function obtenerRFCEmisor(){
		var rfcEmisor = $('#rfcEmisor').val();
		return $('#rfcEmisor').val();
	}
	
	function obtenerRazonEmisor(){
		return $('#razonSocialEmisor').val();
	}
	
	function obtenerFechaInicio(){
		return $('#fechaInicialDescarga').val();
	}
	
	function obtenerFechaFinal(){
		return $('#fechaFinalDescarga').val();
	}
	
	function existeBoveda(){
		var existeBovedaDescarga = $('#existeBovedaDescarga').val();
		return existeBovedaDescarga;
	}
	
	function obtenerTipoComprobante(){
	  // lee primero del thead; si no, de los hiddens (normalizados)
	  return (
	    __normSel($('#efectoFilterInputR').val())           ||  // ⬅️ thead directo
	    __normSel($('#tipoComprobanteDescarga').val())      ||
	    __normSel($('#efectoComprobante').val())            ||
	    __normSel($('#tipoComprobante').val())              ||
	    __normSel($('#efecto_Recibidos').val())             ||
	    ''
	  );
	}

	
	// Normaliza valores de selects: '' | 'ALL' | 'TODOS' => ''
	function __normSel(v){
	  v = (v||'').trim();
	  return (v === '' || v.toUpperCase() === 'ALL' || v.toUpperCase() === 'TODOS') ? '' : v;
	}


	
	function obtenerUUID(){
		return $('#uuidDescarga').val();
	}
	
	function obtenerEstatus(){
		return $('#estatusCFDI').val();
	}

	function refrescar(){
	  if (typeof refrescarRecibidos === 'function') {
	    refrescarRecibidos('manual');
	  } else {
	    $('#tablaDetalle').DataTable().ajax.reload(null,true);
	  }
	}

	
	function exportExcelDescarga(){
	  try{
			var fechaInicial = obtenerFechaInicio();
			var fechaFinal =   obtenerFechaFinal();

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
							// Si existen los helpers, sincroniza thead -> globales, igual que haces al refrescar
								    if (typeof window.__copiarTheadAGlobalesR === 'function') {
								      window.__copiarTheadAGlobalesR();
								    }

								    // ====== Básicos que ya tenías ======
								    var rfcEmisor               = obtenerRFCEmisor();
								    var razonSocialEmisor       = obtenerRazonEmisor();
								    var existeBovedaDescarga    = existeBoveda();
								    var tipoComprobanteDescarga = obtenerTipoComprobante();
								    var fechaInicialDescarga    = obtenerFechaInicio();
								    var fechaFinalDescarga      = obtenerFechaFinal();
								    var uuidDescarga            = obtenerUUID();
								    var estatusCFDI             = obtenerEstatus();

								    // ====== Operadores/valores DX-like (TEXTOS) ======
								    var uuidOperator    = $('#uuidOperatorR').val()    || 'contains';
								    var rfcEmiOperator  = $('#rfcEmiOperatorR').val()  || 'contains';
								    var nomEmiOperator  = $('#nomEmiOperatorR').val()  || 'contains';
								    var rfcRecOperator  = $('#rfcRecOperatorR').val()  || 'contains';
								    var nomRecOperator  = $('#nomRecOperatorR').val()  || 'contains';
								    var pacOperator     = $('#pacOperatorR').val()     || 'contains';
								    var efectoOperator  = $('#efectoOperatorR').val()  || 'contains';
								    var estatusOperator = $('#estatusOperatorR').val() || 'contains';
								    var bovedaOperator  = $('#bovedaOperatorR').val()  || 'contains';

								    // ====== Numérico (MONTO) ======
								    var montoV1       = $('#montoFilter1R').val();
								    var montoV2       = $('#montoFilter2R').val();
								    var montoOperator = $('#montoOperatorR').val()     || 'eq'; // eq, ne, lt, gt, le, ge, between

								    // ====== Fechas (Emisión / Certificación / Cancelación) ======
								    var emiDateOperator = $('#emiDateOperatorR').val() || 'eq'; // eq, ne, lt, gt, le, ge, bt
								    var emiDateV1       = $('#emiDateFilter1R').val();
								    var emiDateV2       = $('#emiDateFilter2R').val();

								    var cerDateOperator = $('#cerDateOperatorR').val() || 'eq';
								    var cerDateV1       = $('#cerDateFilter1R').val();
								    var cerDateV2       = $('#cerDateFilter2R').val();

								    var canDateOperator = $('#canDateOperatorR').val() || 'eq';
								    var canDateV1       = $('#canDateFilter1R').val();
								    var canDateV2       = $('#canDateFilter2R').val();

								    // ====== Llenar el form oculto (XLS) ======
								    $('#rfcEmisorXLS_Recibidos').val(rfcEmisor);
								    $('#razonSocialEmisorXLS_Recibidos').val(razonSocialEmisor);
								    $('#existeBovedaDescargaXLS_Recibidos').val(existeBovedaDescarga);
								    $('#tipoComprobanteDescargaXLS_Recibidos').val(tipoComprobanteDescarga);
								    $('#fechaInicialDescargaXLS_Recibidos').val(fechaInicialDescarga);
								    $('#fechaFinalDescargaXLS_Recibidos').val(fechaFinalDescarga);
								    $('#uuidDescargaXLS_Recibidos').val(uuidDescarga);
								    $('#estatusCFDIXLS_Recibidos').val(estatusCFDI);

								    // TEXTOS – operadores
								    $('#uuidOperatorXLS_Recibidos').val(uuidOperator);
								    $('#rfcEmiOperatorXLS_Recibidos').val(rfcEmiOperator);
								    $('#nomEmiOperatorXLS_Recibidos').val(nomEmiOperator);
								    $('#rfcRecOperatorXLS_Recibidos').val(rfcRecOperator);
								    $('#nomRecOperatorXLS_Recibidos').val(nomRecOperator);
								    $('#pacOperatorXLS_Recibidos').val(pacOperator);
								    $('#efectoOperatorXLS_Recibidos').val(efectoOperator);
								    $('#estatusOperatorXLS_Recibidos').val(estatusOperator);
								    $('#bovedaOperatorXLS_Recibidos').val(bovedaOperator);

								    // NUMÉRICO – monto
								    $('#montoV1XLS_Recibidos').val(montoV1);
								    $('#montoV2XLS_Recibidos').val(montoV2);
								    $('#montoOperatorXLS_Recibidos').val(montoOperator);

								    // FECHAS – emisión/certificación/cancelación
								    $('#emiDateOperatorXLS_Recibidos').val(emiDateOperator);
								    $('#emiDateV1XLS_Recibidos').val(emiDateV1);
								    $('#emiDateV2XLS_Recibidos').val(emiDateV2);

								    $('#cerDateOperatorXLS_Recibidos').val(cerDateOperator);
								    $('#cerDateV1XLS_Recibidos').val(cerDateV1);
								    $('#cerDateV2XLS_Recibidos').val(cerDateV2);

								    $('#canDateOperatorXLS_Recibidos').val(canDateOperator);
								    $('#canDateV1XLS_Recibidos').val(canDateV1);
								    $('#canDateV2XLS_Recibidos').val(canDateV2);

								    // ====== Submit ======
									if (window.DEBUG_REC) {
									  const dbg = {
									    rfcEmisor: $('#rfcEmisorXLS_Recibidos').val(),
									    razonSocialEmisor: $('#razonSocialEmisorXLS_Recibidos').val(),
									    rfcReceptor: $('#rfcReceptor').val(),             // si lo usas global
									    nombreReceptor: $('#nombreReceptor').val(),       // idem
									    rfcPac: $('#rfcPac').val(),                       // idem
									    existeBovedaDescarga: $('#existeBovedaDescargaXLS_Recibidos').val(),
									    tipoComprobanteDescarga: $('#tipoComprobanteDescargaXLS_Recibidos').val(),
									    estatusCFDI: $('#estatusCFDIXLS_Recibidos').val(),
									    uuidDescarga: $('#uuidDescargaXLS_Recibidos').val(),
									    // operadores
									    uuidOperator: $('#uuidOperatorXLS_Recibidos').val(),
									    rfcEmiOperator: $('#rfcEmiOperatorXLS_Recibidos').val(),
									    nomEmiOperator: $('#nomEmiOperatorXLS_Recibidos').val(),
									    rfcRecOperator: $('#rfcRecOperatorXLS_Recibidos').val(),
									    nomRecOperator: $('#nomRecOperatorXLS_Recibidos').val(),
									    pacOperator: $('#pacOperatorXLS_Recibidos').val(),
									    efectoOperator: $('#efectoOperatorXLS_Recibidos').val(),
									    estatusOperator: $('#estatusOperatorXLS_Recibidos').val(),
									    bovedaOperator: $('#bovedaOperatorXLS_Recibidos').val(),
									    // num/fechas
									    monto: `${$('#montoOperatorXLS_Recibidos').val()} ${$('#montoV1XLS_Recibidos').val()}..${$('#montoV2XLS_Recibidos').val()}`,
									    emiDate: `${$('#emiDateOperatorXLS_Recibidos').val()} ${$('#emiDateV1XLS_Recibidos').val()}..${$('#emiDateV2XLS_Recibidos').val()}`,
									    cerDate: `${$('#cerDateOperatorXLS_Recibidos').val()} ${$('#cerDateV1XLS_Recibidos').val()}..${$('#cerDateV2XLS_Recibidos').val()}`,
									    canDate: `${$('#canDateOperatorXLS_Recibidos').val()} ${$('#canDateV1XLS_Recibidos').val()}..${$('#canDateV2XLS_Recibidos').val()}`
									  };
									//  console.log('[XLS] exportExcel → campos ocultos que viajan al Action:', dbg);
									}

									
								    document.frmDescargaXMLExcelRecibidos.submit();

								   
						} else {
							Swal.fire({
	  			                title: '¡Rango de fechas no valido!',
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
		
	    // window.open('/siarex247/excel/exportExcelDescargaSAT.action?'+q, 'excel');

	  }catch(e){
	    alert('exportExcelDescarga(): ' + e);
	  }
	}
	
	/* =======================
	 * RECIBIDOS – Guard & Bridges (DX-like filtros) (FIX FUNCIONAL)
	 * ======================= */

	if (!window.__descRec_bootstrapped1) {

	  window.__descRec_bootstrapped1 = true;
	  window.__recBusy = false;
	  window.__recDebounce = null;
	  window.__recLastReq = 0;

	  // ===== Refresco con debounce y antirrebote =====
	  window.refrescarRecibidos = function (reason) {
	    try {
	      if (window.__recDebounce) clearTimeout(window.__recDebounce);
	      window.__recDebounce = setTimeout(function () {
	        const now = Date.now();
	        if (now - window.__recLastReq < 250) return; // evita doble disparo
	        window.__recLastReq = now;

	        const $tbl = $('#tablaDetalle');
	        if (!$tbl.length || !$.fn.dataTable.isDataTable($tbl)) return;

	        window.__recBusy = true;
	        $tbl.DataTable().ajax.reload(function () {
	          window.__recBusy = false;
	        }, false);
	      }, 100);
	    } catch (e) {
	      alert('refrescarRecibidos()_' + e);
	      window.__recBusy = false;
	    }
	  };

	  // ===== Helpers =====
	  function __cerrarMenusR() {
	    $('#tablaDetalle thead .dx-like-menu, .dtfh-floatingparent thead .dx-like-menu').removeClass('show');
	  }

	  function __valorPresenteR($filter) {
	    const $inputs = $filter.find('input,select');
	    let ok = false;
	    $inputs.each(function () {
	      const v = ($(this).val() || '').trim();
	      if (v.length >= 1) { ok = true; return false; }
	    });
	    return ok;
	  }

	  function __copiarTheadAGlobalesR() {
	    const map = [
	      ['#uuidFilterInputR',   '#uuidDescarga'],
	      ['#rfcEmiFilterInputR', '#rfcEmisor'],
	      ['#nomEmiFilterInputR', '#razonSocialEmisor'],
	      ['#nomRecFilterInputR', '#nombreReceptor'],
	      ['#pacFilterInputR',    '#rfcPac'],

	      // EFECTO/TIPO: llena varios hiddens por compatibilidad
	      ['#efectoFilterInputR', '#tipoComprobanteDescarga'],
	      ['#efectoFilterInputR', '#efectoComprobante'],
	      ['#efectoFilterInputR', '#tipoComprobante'],

	      ['#estatusFilterInputR', '#estatusCFDI'],
	      ['#bovedaFilterInputR',  '#existeBovedaDescarga']
	    ];
	    map.forEach(([from,to]) => {
	      if ($(from).length && $(to).length) $(to).val($(from).val());
	    });
	  }

	  // ===== Inicializadores =====
	  window.initDxLikeFilterR = function ({ inputId, hiddenOpId, targetInput }) {
	    const $input = $(inputId), $op = $(hiddenOpId);
	    if (!$input.length || !$op.length) return;
	    if (!$op.val()) $op.val('contains');

	    const ns = '.dxTxtEnterR';
	    $input.off('keydown' + ns).on('keydown' + ns, e => {
	      if (e.key === 'Enter') {
	        e.preventDefault();
	        if (targetInput) $(targetInput).val($input.val());
	        __copiarTheadAGlobalesR();
	       // refrescarRecibidos('enter');
		   validarFechas();
	      }
	    });
	  };

	  window.initNumericDxFilterR = function ({ v1Id, v2Id, opHiddenId }) {
	    const $v1 = $(v1Id), $v2 = $(v2Id);
	    const ns = '.dxNumEnterR';
	    const onEnter = e => {
	      if (e.key === 'Enter') {
	        e.preventDefault();
	        __copiarTheadAGlobalesR();
	        //refrescarRecibidos('enter-num');
			validarFechas();
	      }
	    };
	    $v1.off('keydown' + ns).on('keydown' + ns, onEnter);
	    if ($v2.length) $v2.off('keydown' + ns).on('keydown' + ns, onEnter);
	  };

	  window.initDxLikeDateFilterR = function ({ input1Id, input2Id, hiddenOpId }) {
	    const $d1 = $(input1Id), $d2 = $(input2Id);
	    const ns = '.dxDateEnterR';
	    const onEnter = e => {
	      if (e.key === 'Enter') {
	        e.preventDefault();
	        __copiarTheadAGlobalesR();
	        //refrescarRecibidos('enter-date');
			validarFechas();
	      }
	    };
	    $d1.off('keydown' + ns).on('keydown' + ns, onEnter);
	    if ($d2.length) $d2.off('keydown' + ns).on('keydown' + ns, onEnter);
	  };

	  // ===== Delegados =====
	  (function () {
	    function isNumericMenu(id) { return /montoOpMenuR$/i.test(id || ''); }
	    function isDateMenu(id) { return /(emiDateOpMenuR|cerDateOpMenuR|canDateOpMenuR)$/i.test(id || ''); }
	    function hiddenForMenu($menu) { const id = $menu.attr('id') || ''; return '#' + id.replace('OpMenu', 'Operator'); }
	    function defaultOp(menuId) { return (isNumericMenu(menuId) || isDateMenu(menuId)) ? 'eq' : 'contains'; }
	    function showSecond($f, show) {
	      const $second = $f.find('#montoFilter2R,#emiDateFilter2R,#cerDateFilter2R,#canDateFilter2R');
	      if (show) $second.removeClass('d-none'); else $second.addClass('d-none').val('');
	    }

	    $(document)
	      .off('click.dxRMenuToggle')
	      .on('click.dxRMenuToggle', '#tablaDetalle thead .op-btn, .dtfh-floatingparent thead .op-btn', function (e) {
	        e.stopPropagation();
	        const $menu = $(this).siblings('.dx-like-menu');
	        __cerrarMenusR();
	        $menu.addClass('show').css({ position: 'absolute', zIndex: 1090 });
	      });

	    $(document).off('click.dxRMenuClose').on('click.dxRMenuClose', function () { __cerrarMenusR(); });

	    $(document)
	      .off('click.dxRSelectOp')
	      .on('click.dxRSelectOp', '#tablaDetalle thead .dx-like-menu li, .dtfh-floatingparent thead .dx-like-menu li', function () {
	        const $li = $(this);
	        const op = String($li.data('op') || '');
	        const $menu = $li.closest('.dx-like-menu');
	        const mid = $menu.attr('id') || '';
	        const $f = $menu.closest('.dx-like-filter');
	        const $hidden = $(hiddenForMenu($menu));
	        const $label = $f.find('.op-label');

	        if (op === 'reset') {
	          const def = defaultOp(mid);
	          $hidden.val(def);
	          $label.html((isNumericMenu(mid) || isDateMenu(mid)) ? '=' : '<i class="fas fa-search"></i>');
	          $f.find('input').val('');
	          showSecond($f, false);
	          __cerrarMenusR();
	          __copiarTheadAGlobalesR();
	          refrescarRecibidos('reset');
	          return;
	        }

	        $hidden.val(op);
	        const firstToken = $.trim($li.text()).split(/\s+/)[0];
	        $label.text(firstToken);
	        const needSecond = isDateMenu(mid) ? (op === 'bt') : (op === 'between' || op === 'bt');
	        showSecond($f, needSecond);
	        __cerrarMenusR();

	        if (__valorPresenteR($f)) {
	          __copiarTheadAGlobalesR();
	          refrescarRecibidos('change-op');
	        }
	      });

	    // ENTER global
	    $(document)
	      .off('keydown.recGlobalEnter')
	      .on('keydown.recGlobalEnter', '#tablaDetalle thead tr.filters :input', function (e) {
	        if (e.key === 'Enter') {
	          e.preventDefault();
	          __copiarTheadAGlobalesR();
	          //refrescarRecibidos('enter-global');
			  validarFechas();
	        }
	      });

	    // Change automático (para selects)
		// Change automático SOLO para <select> (no text/number/date)
		$(document)
		  .off('change.recibidosAuto')
		  .on(
		    'change.recibidosAuto',
		    '#tablaDetalle thead tr.filters select, .dtfh-floatingparent thead tr.filters select',
		    function () {
		      // Copiamos thead -> globales y recargamos una vez
		      //if (typeof window.__copiarTheadAGlobalesR === 'function') window.__copiarTheadAGlobalesR();
		      //if (typeof refrescarRecibidos === 'function') refrescarRecibidos('change-select');
		    }
		  );

	  })();

	  // ===== Inicialización de filtros =====
	  $(function () {
	    initDxLikeFilterR({ inputId: '#uuidFilterInputR', hiddenOpId: '#uuidOperatorR', targetInput: '#uuidDescarga' });
	    initDxLikeFilterR({ inputId: '#rfcEmiFilterInputR', hiddenOpId: '#rfcEmiOperatorR', targetInput: '#rfcEmisor' });
	    initDxLikeFilterR({ inputId: '#nomEmiFilterInputR', hiddenOpId: '#nomEmiOperatorR', targetInput: '#razonSocialEmisor' });
	    initDxLikeFilterR({ inputId: '#nomRecFilterInputR', hiddenOpId: '#nomRecOperatorR', targetInput: '#nombreReceptor' });
	    initDxLikeFilterR({ inputId: '#pacFilterInputR', hiddenOpId: '#pacOperatorR', targetInput: '#rfcPac' });
	    initDxLikeFilterR({ inputId: '#efectoFilterInputR', hiddenOpId: '#efectoOperatorR', targetInput: '#tipoComprobanteDescarga' });
	    initDxLikeFilterR({ inputId: '#estatusFilterInputR', hiddenOpId: '#estatusOperatorR', targetInput: '#estatusCFDI' });
	    initDxLikeFilterR({ inputId: '#bovedaFilterInputR', hiddenOpId: '#bovedaOperatorR', targetInput: '#existeBovedaDescarga' });
	    initNumericDxFilterR({ v1Id: '#montoFilter1R', v2Id: '#montoFilter2R', opHiddenId: '#montoOperatorR' });
	    initDxLikeDateFilterR({ input1Id: '#emiDateFilter1R', input2Id: '#emiDateFilter2R', hiddenOpId: '#emiDateOperatorR' });
	    initDxLikeDateFilterR({ input1Id: '#cerDateFilter1R', input2Id: '#cerDateFilter2R', hiddenOpId: '#cerDateOperatorR' });
	    initDxLikeDateFilterR({ input1Id: '#canDateFilter1R', input2Id: '#canDateFilter2R', hiddenOpId: '#canDateOperatorR' });
	  });
	}



		 
	// === SINCRONIZAR THEAD (incl. clon de FixedHeader) → CAMPOS QUE LEE ajax.data (Recibidos/Descarga) ===
	window.__descRec_syncThead = window.__descRec_syncThead || function(){
		
	  // 1) Lee desde el thead flotante si existe; si no, del thead original de #tablaDetalle
	  function getVal(sel){
	    const $float = $('table.fixedHeader-floating thead').find(sel);
	    if ($float.length) return $float.val();
	    return $('#tablaDetalle thead').find(sel).val();
	  }
	  // normaliza selects tipo "ALL"/"TODOS"
	  function normSel(v){
	    v = (v || '').trim();
	    return (v === '' || v.toUpperCase() === 'ALL' || v.toUpperCase() === 'TODOS') ? '' : v;
	  }

	  // 2) Copia textos del thead → inputs/globales que usa tu backend en ajax.data
	  $('#uuidDescarga').val(           getVal('#uuidFilterInputR')   || '' );
	  $('#rfcEmisor').val(              getVal('#rfcEmiFilterInputR') || '' );
	  $('#razonSocialEmisor').val(      getVal('#nomEmiFilterInputR') || '' );
	  $('#rfcReceptor').val(            getVal('#rfcRecFilterInputR') || '' );  // si tu backend no lo usa, no estorba
	  $('#nombreReceptor').val(         getVal('#nomRecFilterInputR') || '' );
	  $('#rfcPac').val(                 getVal('#pacFilterInputR')    || '' );

	  // Efecto/Tipo de comprobante: llena varios hiddens por compatibilidad
	  const efectoVal = normSel( getVal('#efectoFilterInputR') || '' );
	  $('#tipoComprobanteDescarga').val( efectoVal );
	  $('#efectoComprobante').val(       efectoVal );
	  $('#tipoComprobante').val(         efectoVal );

	  // Estatus y Bóveda
	  $('#estatusCFDI').val(            getVal('#estatusFilterInputR') || '' );
	  $('#existeBovedaDescarga').val(   getVal('#bovedaFilterInputR')  || '' );

	  // 3) Fechas: mapea Emisión (thead) → rango global (si así lo espera tu backend)
	  // const emi1 = getVal('#emiDateFilter1R') || '';
	  // const emi2 = getVal('#emiDateFilter2R') || '';
	  // alert('emi1===>'+emi1);
	  
	  //if ($('#fechaInicialDescarga').length) $('#fechaInicialDescarga').val(emi1);
	  // if ($('#fechaFinalDescarga').length)   $('#fechaFinalDescarga').val(emi2);

	  // 4) Opcional: ajusta operador de “efecto/tipo” a EQ si hay valor (como en Emitidos)
	  if ($('#efectoOperatorR').length){
	    $('#efectoOperatorR').val( efectoVal ? 'eq' : 'contains' );
	  }
	};

		 
	
	