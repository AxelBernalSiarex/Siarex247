
var tablaDetalleNomina = null;

	$(document).ready(function() {
		try {
			tablaDetalleNomina = $('#tablaDetalleNomina').DataTable( {
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
				orderCellsTop: true,
				info		: true,
				select      : false,
				stateSave	: false, 
				order       : [ [ 0, 'asc' ] ],	
				buttons: [
				  // 1) Limpiar (Descarga Nómina)
				  {
				    text:
				      '<span class="fas fa-broom" data-fa-transform="shrink-3 down-2"></span>' +
				      '<span class="d-none d-sm-inline-block ms-1">Limpiar</span>',
				    className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    action: function(){ limpiarNomina(); }
				  },

				  // 2) Refrescar (diseño verde + ícono Firefox, con sync de filtros)
				  {
				    text:
				      '<span class="fab fa-firefox-browser me-1"></span>' +
				      '<span class="d-none d-sm-inline-block" id="VISOR_BTN_REFRESCAR">Actualizar</span>',
				    className: 'btn btn-falcon-success btn-sm mb-1 me-1',
				    action: function (e, dt, node) {
						validarFechasNomina();
				    }
				  },

				  // 3) Exportar Excel (Descarga Nómina)
				  {
				    text:
				      '<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span>' +
				      '<span class="d-none d-sm-inline-block ms-1">Exportar Excel</span>',
				    className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    action: function(){ exportExcelDescargaNomina(); }
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
				// === En la config DataTable de Nómina (sustituye ajax.data por función) ===
				ajax: {
				  url: '/siarex247/cumplimientoFiscal/descargaSAT/nomina/detalleNomina.action',
				  beforeSend: function(){ $('#overSeccion_Nomina').css({display:'block'}); },
				  complete: function(){ $('#overSeccion_Nomina').css({display:'none'}); },
				  data: function(d){
				    // 1) primero, intenta copiar thead -> globales (si existe tu helper)
				    if (typeof window.__copiarTheadAGlobalesN === 'function') {
				      window.__copiarTheadAGlobalesN();
				    }

				    // 2) valores base (ocultos globales)
				    d.rfcReceptor          = obtenerRFCReceptorNomina();
				    d.razonSocialReceptor  = obtenerRazonReceptorNomina();
				    d.existeBovedaDescarga = existeBovedaNomina();
				    d.fechaInicialDescarga = obtenerFechaInicioNomina();
				    d.fechaFinalDescarga   = obtenerFechaFinalNomina();
				    d.uuidDescarga         = obtenerUUIDNomina();
				    d.estatusCFDI          = obtenerEstatusNomina();

				    // 3) lee DIRECTO del thead visible (o cualquier copia) y pisa si hay valor
				    //    (esto evita depender de ENTER o de un handler previo)
				    var readAny = (id)=>{
				      var val = '';
				      $('[id="'+id+'"]').each(function(){
				        var v = $.trim($(this).val() || '');
				        if (v){ val = v; return false; }
				      });
				      return val;
				    };

				    var uuidHead   = readAny('uuidFilterInputN');
				    var rfcRecHead = readAny('rfcRecFilterInputN');
				    var nomRecHead = readAny('nomRecFilterInputN');

				    if (uuidHead){   d.uuidDescarga = uuidHead;   $('#uuidDescarga_Nomina').val(uuidHead); }
				    if (rfcRecHead){ d.rfcReceptor  = rfcRecHead; $('#rfcReceptor_Nomina').val(rfcRecHead); }
				    if (nomRecHead){ d.razonSocialReceptor = nomRecHead; $('#razonSocialReceptor_Nomina').val(nomRecHead); }

				 
					// 4) efecto (N/I/P) desde thead si está presente
					d.tipoComprobanteDescarga = 'N';
				    // 5) operadores
				    d.uuidOperator    = $('#uuidOperatorN').val()    || 'contains';
				    d.rfcEmiOperator  = $('#rfcEmiOperatorN').val()  || 'contains';
				    d.nomEmiOperator  = $('#nomEmiOperatorN').val()  || 'contains';
				    d.rfcRecOperator  = $('#rfcRecOperatorN').val()  || 'contains';
				    d.nomRecOperator  = $('#nomRecOperatorN').val()  || 'contains';
				    d.pacOperator     = $('#pacOperatorN').val()     || 'contains';
				    d.efectoOperator  = $('#efectoOperatorN').val()  || 'contains';
				    d.estatusOperator = $('#estatusOperatorN').val() || 'contains';
				    d.bovedaOperator  = $('#bovedaOperatorN').val()  || 'contains';

				    // 6) monto/fechas (igual que ya tenías)
				    d.montoV1        = $('#montoFilter1N').val();
				    d.montoV2        = $('#montoFilter2N').val();
				    d.montoOperator  = $('#montoOperatorN').val()    || 'eq';

				    d.emiDateOperator= $('#emiDateOperatorN').val()  || 'eq';
				    d.emiDateV1      = $('#emiDateFilter1N').val();
				    d.emiDateV2      = $('#emiDateFilter2N').val();

				    d.cerDateOperator= $('#cerDateOperatorN').val()  || 'eq';
				    d.cerDateV1      = $('#cerDateFilter1N').val();
				    d.cerDateV2      = $('#cerDateFilter2N').val();

				    d.canDateOperator= $('#canDateOperatorN').val()  || 'eq';
				    d.canDateV1      = $('#canDateFilter1N').val();
				    d.canDateV2      = $('#canDateFilter2N').val();

				    // debug
				    console.log('[NOMINA/AJAX] vals',
				      { uuid: d.uuidDescarga, rfcRec: d.rfcReceptor, nomRec: d.razonSocialReceptor },
				      '| ops', { uuidOp: d.uuidOperator, rfcRecOp: d.rfcRecOperator, nomRecOp: d.nomRecOperator }
				    );

				    d._ = Date.now();
			

				  },

				

				  type: 'POST'
				},
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
				//	{ mData: "efectoComprobante", "sClass": "alinearCentro"},
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
			});
			
		} catch(e) {
			alert('usuarios()_'+e);
		};
		
		tablaDetalleNomina.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	

	


	function obtenerFechasFiltroDescargaNomina(){
		$.ajax({
			url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/consultarFechas.action',
			type : 'POST', 
			data : null,
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					 obtenerFechasMinimaDescargaNomina(data.fechaInicial, data.fechaFinal);
					 
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('obtenerFechasFiltroDescargaNomina()_'+thrownError);
			}
		});	
				
	}
			
	

	function validarFechasNomina(){
			//var fechaInicial = $('#fechaInicial_Recibidos').val();
			//var fechaFinal = $('#fechaFinal_Recibidos').val();

			var fechaInicial = obtenerFechaInicioNomina();
			var fechaFinal =   obtenerFechaFinalNomina();

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
							window.__copiarTheadAGlobalesN();
							refrescarNomina();
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

		
	
	function obtenerRFCReceptorNomina(){
		var rfcReceptor = $('#rfcReceptor_Nomina').val();
		return $('#rfcReceptor_Nomina').val();
	}
	
	function obtenerRazonReceptorNomina(){
		return $('#razonSocialReceptor_Nomina').val();
	}
	
	function obtenerFechaInicioNomina(){
		return $('#fechaInicial_Nomina').val();
	}
	
	function obtenerFechaFinalNomina(){
		return $('#fechaFinal_Nomina').val();
	}
	
	function existeBovedaNomina(){
		var existeBovedaDescarga = $('#existeBoveda_Nomina').val();
		return existeBovedaDescarga;
	}
	
	
	function obtenerUUIDNomina(){
		return $('#uuidDescarga_Nomina').val();
	}
	
	function obtenerEstatusNomina(){
		return $('#estatusCFDI_Nomina').val();
	}

	function refrescarNomina(){
			$('#tablaDetalleNomina').DataTable().ajax.reload(null,true);	
	}
	
	
	function exportExcelDescargaNomina(){
	  try{
		
		var fechaInicial = obtenerFechaInicioNomina();
		var fechaFinal =   obtenerFechaFinalNomina();

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
						if (typeof window.__copiarTheadAGlobalesN === 'function') __copiarTheadAGlobalesN();

							var efecto = 'N';

							    var rfcReceptor             = obtenerRFCReceptorNomina();
							    var razonSocialReceptor     = obtenerRazonReceptorNomina();
							    var existeBovedaDescarga    = existeBovedaNomina();
							    var fechaInicialDescarga    = obtenerFechaInicioNomina();
							    var fechaFinalDescarga      = obtenerFechaFinalNomina();
							    var uuidDescarga            = obtenerUUIDNomina();
							    var estatusCFDI             = obtenerEstatusNomina();

							    // TEXTOS – operadores
							    var uuidOperator    = $('#uuidOperatorN').val()    || 'contains';
							    var rfcEmiOperator  = $('#rfcEmiOperatorN').val()  || 'contains';
							    var nomEmiOperator  = $('#nomEmiOperatorN').val()  || 'contains';
							    var rfcRecOperator  = $('#rfcRecOperatorN').val()  || 'contains';
							    var nomRecOperator  = $('#nomRecOperatorN').val()  || 'contains';
							    var pacOperator     = $('#pacOperatorN').val()     || 'contains';
							    var efectoOperator  = $('#efectoOperatorN').val()  || 'contains';
							    var estatusOperator = $('#estatusOperatorN').val() || 'contains';
							    var bovedaOperator  = $('#bovedaOperatorN').val()  || 'contains';

							    // Monto
							    var montoV1       = $('#montoFilter1N').val();
							    var montoV2       = $('#montoFilter2N').val();
							    var montoOperator = $('#montoOperatorN').val()     || 'eq';

							    // Fechas
							    var emiDateOperator = $('#emiDateOperatorN').val() || 'eq';
							    var emiDateV1       = $('#emiDateFilter1N').val();
							    var emiDateV2       = $('#emiDateFilter2N').val();

							    var cerDateOperator = $('#cerDateOperatorN').val() || 'eq';
							    var cerDateV1       = $('#cerDateFilter1N').val();
							    var cerDateV2       = $('#cerDateFilter2N').val();

							    var canDateOperator = $('#canDateOperatorN').val() || 'eq';
							    var canDateV1       = $('#canDateFilter1N').val();
							    var canDateV2       = $('#canDateFilter2N').val();

							    // ===== Llenar ocultos del form XLS =====
							    $('#rfcReceptorXLS_Nomina').val(rfcReceptor);
							    $('#razonSocialReceptorXLS_Nomina').val(razonSocialReceptor);
							    $('#existeBovedaDescargaXLS_Nomina').val(existeBovedaDescarga);

							    // AQUI el efecto correcto al form de Excel
							    $('#tipoComprobanteDescargaXLS_Nomina').val(efecto);

							    $('#fechaInicialDescargaXLS_Nomina').val(fechaInicialDescarga);
							    $('#fechaFinalDescargaXLS_Nomina').val(fechaFinalDescarga);
							    $('#uuidDescargaXLS_Nomina').val(uuidDescarga);
							    $('#estatusCFDIXLS_Nomina').val(estatusCFDI);

							    $('#uuidOperatorXLS_Nomina').val(uuidOperator);
							    $('#rfcEmiOperatorXLS_Nomina').val(rfcEmiOperator);
							    $('#nomEmiOperatorXLS_Nomina').val(nomEmiOperator);
							    $('#rfcRecOperatorXLS_Nomina').val(rfcRecOperator);
							    $('#nomRecOperatorXLS_Nomina').val(nomRecOperator);
							    $('#pacOperatorXLS_Nomina').val(pacOperator);
							    $('#efectoOperatorXLS_Nomina').val(efectoOperator);
							    $('#estatusOperatorXLS_Nomina').val(estatusOperator);
							    $('#bovedaOperatorXLS_Nomina').val(bovedaOperator);

							    $('#montoV1XLS_Nomina').val(montoV1);
							    $('#montoV2XLS_Nomina').val(montoV2);
							    $('#montoOperatorXLS_Nomina').val(montoOperator);

							    $('#emiDateOperatorXLS_Nomina').val(emiDateOperator);
							    $('#emiDateV1XLS_Nomina').val(emiDateV1);
							    $('#emiDateV2XLS_Nomina').val(emiDateV2);

							    $('#cerDateOperatorXLS_Nomina').val(cerDateOperator);
							    $('#cerDateV1XLS_Nomina').val(cerDateV1);
							    $('#cerDateV2XLS_Nomina').val(cerDateV2);

							    $('#canDateOperatorXLS_Nomina').val(canDateOperator);
							    $('#canDateV1XLS_Nomina').val(canDateV1);
							    $('#canDateV2XLS_Nomina').val(canDateV2);

							    // DEBUG
							    console.log('[XLS] efecto=', efecto, '| efectoOp=', efectoOperator);

							    document.frmDescargaXMLExcelNomina.submit();
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
	    alert('exportExcelDescargaNomina(): ' + e);
	  }
	}

	
	function limpiarNomina(){
	  try{
	    // 1) limpia globales nominales
	    $('#rfcReceptor_Nomina,#razonSocialReceptor_Nomina,#uuidDescarga_Nomina,#rfcEmisor_Nomina,#razonSocialEmisor_Nomina,#rfcPac_Nomina,#tipoComprobanteDescarga_Nomina,#estatusCFDI_Nomina,#existeBoveda_Nomina').val('');
	    $('#fechaInicial_Nomina,#fechaFinal_Nomina').val('');

	    // 2) helper
	    const setOp=(hiddenSel,labelSel,val,labelHtml)=>{ $(hiddenSel).val(val); $(labelSel).html(labelHtml); };

	    // 3) textos thead
	    $('#uuidFilterInputN,#rfcEmiFilterInputN,#nomEmiFilterInputN,#rfcRecFilterInputN,#nomRecFilterInputN,#pacFilterInputN,#efectoFilterInputN').val('');
	    setOp('#uuidOperatorN',   '#uuidOpBtnN .op-label',   'contains','<i class="fas fa-search"></i>');
	    setOp('#rfcEmiOperatorN', '#rfcEmiOpBtnN .op-label','contains','<i class="fas fa-search"></i>');
	    setOp('#nomEmiOperatorN', '#nomEmiOpBtnN .op-label','contains','<i class="fas fa-search"></i>');
	    setOp('#rfcRecOperatorN', '#rfcRecOpBtnN .op-label','contains','<i class="fas fa-search"></i>');
	    setOp('#nomRecOperatorN', '#nomRecOpBtnN .op-label','contains','<i class="fas fa-search"></i>');
	    setOp('#pacOperatorN',    '#pacOpBtnN .op-label',   'contains','<i class="fas fa-search"></i>');
	    setOp('#efectoOperatorN', '#efectoOpBtnN .op-label','contains','<i class="fas fa-search"></i>');

	    // 4) selects
	    $('#estatusFilterN').val('ALL'); setOp('#estatusOperatorN','#estatusOpBtnN .op-label','contains','<i class="fas fa-search"></i>');
	    $('#bovedaFilterN').val('ALL');  setOp('#bovedaOperatorN', '#bovedaOpBtnN .op-label', 'contains','<i class="fas fa-search"></i>');

	    // 5) num
	    function resetNum(opSel,v1Sel,v2Sel,btnSel){
	      $(opSel).val('eq'); $(v1Sel).val(''); $(v2Sel).val('').addClass('d-none'); $(btnSel+' .op-label').html('=');
	    }
	    resetNum('#montoOperatorN','#montoFilter1N','#montoFilter2N','#montoOpBtnN');

	    // 6) fechas
	    function resetDate(opSel,d1Sel,d2Sel,btnSel){
	      $(opSel).val('eq'); $(d1Sel).val(''); $(d2Sel).val('').addClass('d-none'); $(btnSel+' .op-label').html('=');
	    }
	    resetDate('#emiDateOperatorN','#emiDateFilter1N','#emiDateFilter2N','#emiDateOpBtnN');
	    resetDate('#cerDateOperatorN','#cerDateFilter1N','#cerDateFilter2N','#cerDateOpBtnN');
	    resetDate('#canDateOperatorN','#canDateFilter1N','#canDateFilter2N','#canDateOpBtnN');
		/*
		// limpiar rango
		var rangeDN = document.getElementById('CRMDateRangeDN');
		if (rangeDN){ rangeDN.value=''; rangeDN._flatpickr && rangeDN._flatpickr.clear(); }
		// limpiar thead
		$('#emiDateOperatorN').val('eq');
		$('#emiDateFilter1N').val('').hide();
		$('#emiDateFilter2N').val('').hide();
		$('#emiDateOpBtnN .op-label').text('='); */
		obtenerFechasFiltroDescargaNomina()


	    // 7) cerrar menús + recargar
	    $('#tablaDetalleNomina thead .dx-like-menu, .dtfh-floatingparent thead .dx-like-menu').removeClass('show');
		$('#tablaDetalleNomina').DataTable().ajax.reload(null,false);
		 
	  }catch(e){ alert('limpiarNomina()_'+e); }
	}
	
	
	
	/* =======================
	 * NOMINA
	 * ======================= */

	if (!window.__nominaBootstrapped){
	  window.__nominaBootstrapped = true;
	  window.__nomBusy = window.__nomBusy || false;
	  window.__nomDebounce = window.__nomDebounce || null;

	  // --- Debounced reload ---
	  window.refrescarNomina = function(reason){
	    try{
	      if (window.__nomDebounce) clearTimeout(window.__nomDebounce);
	      window.__nomDebounce = setTimeout(function(){
	        if (window.__nomBusy) return;
	        const dt = $('#tablaDetalleNomina').DataTable();
	        if (!dt) return;
	        window.__nomBusy = true;
	        dt.ajax.reload(function(){ window.__nomBusy = false; }, false);
	      }, 120);
	    }catch(e){
	      alert('refrescarNomina()_'+e);
	      window.__nomBusy = false;
	    }
	  };

	  function __cerrarMenusN(){
	    $('#tablaDetalleNomina thead .dx-like-menu, .dtfh-floatingparent thead .dx-like-menu').removeClass('show');
	  }
	  
	  function __valorPresenteN($filter){
	    const $inputs = $filter.find('input[type=text],input[type=number],input[type=date],select');
	    let ok=false;
	    $inputs.each(function(){ const v=($.trim($(this).val()||'')); if(v){ ok=true; return false; } });
	    return ok;
	  }

	  // fija TODAS las copias (thead original + flotante) de un hidden por id
	  function __setAllHiddenById(id, value){
	    $('[id="'+id+'"]').each(function(){ $(this).val(value); });
	  }

	  // === Mapa de inputs del thead -> globales (para copiar puntual del filtro activo) ===
	  const FIELD_MAP_N = {
	    'uuidFilterInputN':   '#uuidDescarga_Nomina',
	    'rfcRecFilterInputN': '#rfcReceptor_Nomina',
	    'nomRecFilterInputN': '#razonSocialReceptor_Nomina',
	    'rfcEmiFilterInputN': '#rfcEmisor_Nomina',
	    'nomEmiFilterInputN': '#razonSocialEmisor_Nomina',
	    'pacFilterInputN':    '#rfcPac_Nomina',
	    'efectoFilterInputN': '#tipoComprobanteDescarga_Nomina'
	  };

	  // Copia SOLO el valor del filtro visible actual a su global correspondiente
	  function __copiarFiltroActivoAGlobalN($filter){
	    let $ctrl = $filter.find('input[type=text],input[type=number],input[type=date]').first();
	    if (!$ctrl.length) $ctrl = $filter.find('select').first();
	    if (!$ctrl.length) return false;

	    const id = $ctrl.attr('id') || '';
	    const val = $.trim($ctrl.val() || '');
	    const target = FIELD_MAP_N[id];
	    if (!target) return false;

	    $(target).val(val);
	    return val.length > 0;
	  }

	  // Mapea thead -> globales (¡incluye UUID y EFECTO!)
	  window.__copiarTheadAGlobalesN = function(){
	    const mapTxt = [
	      ['#uuidFilterInputN', '#uuidDescarga_Nomina'],
	      ['#rfcRecFilterInputN', '#rfcReceptor_Nomina'],
	      ['#nomRecFilterInputN', '#razonSocialReceptor_Nomina'],
	      ['#rfcEmiFilterInputN', '#rfcEmisor_Nomina'],
	      ['#nomEmiFilterInputN', '#razonSocialEmisor_Nomina'],
	      ['#pacFilterInputN',    '#rfcPac_Nomina'],
	      ['#efectoFilterInputN', '#tipoComprobanteDescarga_Nomina']
	    ];
	    mapTxt.forEach(([from,to])=>{ if($(from).length && $(to).length) $(to).val($(from).val()); });
	
		/*
	    if ($('#emiDateFilter1N').length && $('#fechaInicial_Nomina').length){
	      $('#fechaInicial_Nomina').val($('#emiDateFilter1N').val());
	    }
	    if ($('#emiDateFilter2N').length && $('#fechaFinal_Nomina').length){
	      $('#fechaFinal_Nomina').val($('#emiDateFilter2N').val());
	    }
	*/
	    const est = ($('#estatusFilterN').val()||'').trim();
	    if ($('#estatusCFDI_Nomina').length){
	      $('#estatusCFDI_Nomina').val(est==='ALL'?'':est);
	    }
	    const bov = ($('#bovedaFilterN').val()||'').trim();
	    if ($('#existeBoveda_Nomina').length){
	      $('#existeBoveda_Nomina').val(bov==='ALL'?'':bov);
	    }
	  };

	  // Inicializadores (ENTER dispara copia+refresh)
	  window.initDxLikeFilterN = function({inputId, hiddenOpId, targetInput}){
	    const $input=$(inputId);
	    if(!$input.length) return;
	    __setAllHiddenById(hiddenOpId.replace('#',''), 'contains');
	    const ns='.dxTxtEnterN';
	    $input.off('keydown'+ns).on('keydown'+ns, e=>{
	      if(e.key==='Enter'){
	        e.preventDefault();
	        const val=($.trim($input.val()||'')); if(!val) return;
	        if (targetInput) $(targetInput).val(val);
	        __copiarTheadAGlobalesN();
			validarFechasNomina();
	        //if (typeof refrescarNomina==='function') refrescarNomina('enter-text');
	      }
	    });
	  };
	  window.initNumericDxFilterN = function({v1Id,v2Id,opHiddenId}){
	    const $v1=$(v1Id), $v2=$(v2Id);
	    if(!$v1.length) return;
	    __setAllHiddenById(opHiddenId.replace('#',''), 'eq');
	    const ns='.dxNumEnterN';
	    const onEnter=e=>{
	      if(e.key==='Enter'){
	        e.preventDefault();
	        const a=($.trim($v1.val()||'')), b=($.trim($v2.val()||'')); if(!a && !b) return;
	        __copiarTheadAGlobalesN();
			validarFechasNomina();
	       // if (typeof refrescarNomina==='function') refrescarNomina('enter-num');
	      }
	    };
	    $v1.off('keydown'+ns).on('keydown'+ns,onEnter);
	    if($v2.length) $v2.off('keydown'+ns).on('keydown'+ns,onEnter);
	  };
	  window.initDxLikeDateFilterN = function({input1Id,input2Id,hiddenOpId}){
	    const $d1=$(input1Id), $d2=$(input2Id);
	    if(!$d1.length) return;
	    __setAllHiddenById(hiddenOpId.replace('#',''), 'eq');
	    const ns='.dxDateEnterN';
	    const onEnter=e=>{
	      if(e.key==='Enter'){
	        e.preventDefault();
	        const a=($.trim($d1.val()||'')), b=($.trim($d2.val()||'')); if(!a && !b) return;
	        __copiarTheadAGlobalesN();
			validarFechasNomina();
	       //if (typeof refrescarNomina==='function') refrescarNomina('enter-date');
	      }
	    };
	    $d1.off('keydown'+ns).on('keydown'+ns,onEnter);
	    if($d2.length) $d2.off('keydown'+ns).on('keydown'+ns,onEnter);
	  };

	  /* ======= Operadores (incluye fix de BETWEEN/BT mostrando 2º input) ======= */

	  // Detecta tipo de menú por su id (…OpMenuN)
	  function isNumericMenuN(id){ return /(montoOpMenuN)$/i.test(id||''); }
	  function isDateMenuN(id){   return /(emiDateOpMenuN|cerDateOpMenuN|canDateOpMenuN)$/i.test(id||''); }

	  // De menú -> id de hidden operator (…OperatorN)
	  function hiddenForMenuN($menu){
	    const id = $menu.attr('id')||''; // ej: emiDateOpMenuN -> emiDateOperatorN
	    return '#'+id.replace('OpMenu','Operator');
	  }

	  // De menú -> id del segundo input (…Filter2N)
	  function secondIdForMenuN(menuId){
	    if (/montoOpMenuN$/i.test(menuId))     return 'montoFilter2N';
	    if (/emiDateOpMenuN$/i.test(menuId))   return 'emiDateFilter2N';
	    if (/cerDateOpMenuN$/i.test(menuId))   return 'cerDateFilter2N';
	    if (/canDateOpMenuN$/i.test(menuId))   return 'canDateFilter2N';
	    return null;
	  }

	  // Muestra/oculta TODAS las copias (original + flotante) del segundo input por ID derivado
	  // Muestra/oculta TODAS las copias (original + flotante) del 2º input y sus WRAPPERS
	  function toggleSecondByIdN(secondId, show){
	    if (!secondId) return;

	    const $all = $('[id="'+secondId+'"]'); // puede haber 2 (thead original + flotante)
	    $all.each(function(){
	      const $inp = $(this);

	      // wrappers típicos alrededor del segundo campo según tu markup
	      const $wrappers = $inp
	        .add($inp.parent())
	        .add($inp.closest('.input-group, .form-group, .dx-like-filter, th, .col, .row'));

	      if (show){
	        // habilita y muestra
	        $wrappers.removeClass('d-none').css('display',''); // limpia display
	        $inp.prop('disabled', false).removeClass('d-none').show().css('display','inline-block');
	      } else {
	        // limpia y oculta
	        $inp.val('').addClass('d-none').hide();
	        // opcional: no escondas todos los wrappers para no colapsar el th
	        // si quisieras ocultar wrappers, descomenta:
	        // $wrappers.addClass('d-none');
	      }
	    });
	  }


	  function defaultOpN(menuId){
	    return (isNumericMenuN(menuId) || isDateMenuN(menuId)) ? 'eq' : 'contains';
	  }

	  // Abrir menú (thead normal + flotante)
	  $(document)
	    .off('click.nomOpToggle')
	    .on('click.nomOpToggle', '#tablaDetalleNomina thead .op-btn, .dtfh-floatingparent thead .op-btn', function(e){
			
	      e.stopPropagation();
	      const $btn = $(this);
	      const $menu = $btn.siblings('.dx-like-menu');
		  const mid   = $menu.attr('id') || '';
		  const secondId = secondIdForMenuN(mid);
		  const $hid  = $(hiddenForMenuN($menu));
		  if ($hid.length && String($hid.val()||'').toLowerCase()==='bt') {
		    toggleSecondByIdN(secondId, true);
		  }
	      __cerrarMenusN();
	      $menu.addClass('show').css({position:'absolute', zIndex:1090});
	      const menuW=$menu.outerWidth(), thW=$btn.closest('th').outerWidth();
	      if(menuW>thW) $menu.css({left:0,right:'auto'});
	    });

	  // Cerrar menú en click-afuera/scroll/resize/redraw
	  $(document).off('click.nomOpClose').on('click.nomOpClose', function(){ __cerrarMenusN(); });
	  $(window).off('scroll.nomOpClose resize.nomOpClose')
	           .on('scroll.nomOpClose resize.nomOpClose', function(){ __cerrarMenusN(); });
	  $('#tablaDetalleNomina').off('draw.dt.nomOpClose')
	                          .on('draw.dt.nomOpClose', function(){ __cerrarMenusN(); });

	  // Selección de operador (aquí se abre/cierra el 2º input)
	  $(document)
	    .off('click.nomSelectOp')
	    .on('click.nomSelectOp', '#tablaDetalleNomina thead .dx-like-menu li, .dtfh-floatingparent thead .dx-like-menu li', function(e){
	      e.stopPropagation();
	      const $li   = $(this);
		  let opRaw = String($li.data('op')||'').toLowerCase();
		  if (!opRaw) {
		    // fallback por si el data-op no viene: leemos el texto del item
		    const txt = ($.trim($li.text())||'').toLowerCase();
		    if (txt.includes('between') || txt.includes('entre') || txt.includes('rango')) opRaw = 'between';
		  }
		  const op = (opRaw==='between' ? 'bt' : opRaw);
	      const $menu = $li.closest('.dx-like-menu');
	      const mid   = $menu.attr('id') || '';                    // ej: emiDateOpMenuN
	      const $f    = $menu.closest('.dx-like-filter');
	      const $hid  = $(hiddenForMenuN($menu));
	      const secondId = secondIdForMenuN(mid);
	      const $lbl  = $f.find('.op-label');
	      if(!$hid.length) return;

	      if(op==='reset'){
	        const def = defaultOpN(mid);
	        $hid.val(def);
	        if (isNumericMenuN(mid) || isDateMenuN(mid)) $lbl.text('=');
	        else $lbl.html('<i class="fas fa-search"></i>');
	        // limpia ambos inputs (primer y segundo) en TODAS las copias
	        $f.find('input[type=text],input[type=number],input[type=date]').val('');
	        toggleSecondByIdN(secondId, false);
	        $('#tablaDetalleNomina').DataTable().ajax.reload(null,false);
	        return;
	      }

	      // fija operador elegido
	      $hid.val(op);
	      const firstToken = $.trim($li.text()).split(/\s+/)[0];
	      $lbl.html(firstToken); // =, ≠, <, >, ≤, ≥, etc.

	      // fecha y numérico: 'bt' requiere mostrar el segundo input
	      const needSecond = (op === 'bt');
	      toggleSecondByIdN(secondId, needSecond);

	      // cierra menús
	      __cerrarMenusN();

	      // refresca solo si hay algún valor en el filtro
	      let hasVal=false;
	      $f.find('input[type=text],input[type=number],input[type=date],select').each(function(){
	        if($.trim($(this).val()||'').length){ hasVal=true; return false; }
	      });
	      if(hasVal) $('#tablaDetalleNomina').DataTable().ajax.reload(null,false);
	    });

	  // ENTER global en cualquier input del thead (original o flotante)
	  $(document)
	    .off('keydown.nominaEnter')
	    .on('keydown.nominaEnter',
	        '#tablaDetalleNomina thead tr.filters :input, .dtfh-floatingparent thead tr.filters :input',
	        function(e){
	          if(e.key==='Enter'){
	            e.preventDefault();
	            const v=($.trim($(this).val()||'')); if(!v) return;
	            __copiarTheadAGlobalesN();
				
	           // if(typeof refrescarNomina==='function') refrescarNomina('enter-global');
	          }
	        });

	  // Inits de textos
	  $(function(){
	    initDxLikeFilterN({ inputId:'#uuidFilterInputN',   hiddenOpId:'#uuidOperatorN',   targetInput:'#uuidDescarga_Nomina' });
	    initDxLikeFilterN({ inputId:'#rfcEmiFilterInputN', hiddenOpId:'#rfcEmiOperatorN', targetInput:'#rfcEmisor_Nomina' });
	    initDxLikeFilterN({ inputId:'#nomEmiFilterInputN', hiddenOpId:'#nomEmiOperatorN', targetInput:'#razonSocialEmisor_Nomina' });
	    initDxLikeFilterN({ inputId:'#rfcRecFilterInputN', hiddenOpId:'#rfcRecOperatorN', targetInput:'#rfcReceptor_Nomina' });
	    initDxLikeFilterN({ inputId:'#nomRecFilterInputN', hiddenOpId:'#nomRecOperatorN', targetInput:'#razonSocialReceptor_Nomina' });
	    initDxLikeFilterN({ inputId:'#pacFilterInputN',    hiddenOpId:'#pacOperatorN',    targetInput:'#rfcPac_Nomina' });

	    // Select “efecto” (N/I/P) — sincroniza operador y global
	    $(document).off('change.efectoN').on('change.efectoN', '#efectoFilterInputN', function(){
	      const v = $.trim($(this).val() || '');
	      if (!v){
	        __setAllHiddenById('efectoOperatorN','contains');
	        $('#efectoOpBtnN .op-label').html('<i class="fas fa-search"></i>');
	      } else {
	        __setAllHiddenById('efectoOperatorN','equals');
	        $('#efectoOpBtnN .op-label').html('=');
	      }
	      $('#tipoComprobanteDescarga_Nomina').val(v);
	      if (typeof refrescarNomina === 'function') refrescarNomina('change-efecto');
	    });

	    // selects estatus/bóveda
	    $(document).on('change','#estatusFilterN',function(){
	      const v=($.trim($(this).val()||'')); __setAllHiddenById('estatusOperatorN', v && v!=='ALL' ? 'equals' : 'contains');
	      __copiarTheadAGlobalesN(); validarFechasNomina();  //if(typeof refrescarNomina==='function') refrescarNomina('change-estatus');
	    });
	    $(document).on('change','#bovedaFilterN',function(){
	      const v=($.trim($(this).val()||'')); __setAllHiddenById('bovedaOperatorN', v && v!=='ALL' ? 'equals' : 'contains');
	      __copiarTheadAGlobalesN(); validarFechasNomina(); //if(typeof refrescarNomina==='function') refrescarNomina('change-boveda');
	    });

	    // numéricos y fechas
	    initNumericDxFilterN({ v1Id:'#montoFilter1N', v2Id:'#montoFilter2N', opHiddenId:'#montoOperatorN' });
	    initDxLikeDateFilterN({ input1Id:'#emiDateFilter1N', input2Id:'#emiDateFilter2N', hiddenOpId:'#emiDateOperatorN' });
	    initDxLikeDateFilterN({ input1Id:'#cerDateFilter1N', input2Id:'#cerDateFilter2N', hiddenOpId:'#cerDateOperatorN' });
	    initDxLikeDateFilterN({ input1Id:'#canDateFilter1N', input2Id:'#canDateFilter2N', hiddenOpId:'#canDateOperatorN' });
	  });
	}

	/* === Helpers opcionales que quizá ya tienes declarados fuera === */

	// Devuelve el valor no-vacío de cualquiera de las copias (original/flotante) del control con ese id
	function __readFromAnyCopy(id){
	  let val = '';
	  $('[id="'+id+'"]').each(function(){
	    const v = $.trim($(this).val() || '');
	    if (v){ val = v; return false; }
	  });
	  return val;
	}

	// Igual que arriba pero prioriza el visible
	function __readFromVisibleOrAny(id){
	  let val = '';
	  const $visibles = $('[id="'+id+'"]:visible');
	  if ($visibles.length){
	    $visibles.each(function(){
	      const v = $.trim($(this).val() || '');
	      if (v){ val = v; return false; }
	    });
	  }
	  if (!val) val = __readFromAnyCopy(id);
	  return val;
	}

	// Setter sincronizado para todas las copias con idéntico id
	function __setAllById(id, value){
	  $('[id="'+id+'"]').each(function(){ $(this).val(value); });
	}


	
	



	    
	
	
	