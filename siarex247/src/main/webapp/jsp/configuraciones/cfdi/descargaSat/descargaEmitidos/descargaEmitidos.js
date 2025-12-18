
var tablaDetalleEmitidos_Descarga = null;

	$(document).ready(function() {
		try {
			tablaDetalleEmitidos_Descarga = $('#tablaDetalleEmitidos_Descarga').DataTable( {
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
				  // 1) Limpiar (Descarga Emitidos)
				  {
				    text:
				      '<span class="fas fa-broom" data-fa-transform="shrink-3 down-2"></span>' +
				      '<span class="d-none d-sm-inline-block ms-1">Limpiar</span>',
				    className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    action: function(){ limpiarEmitidos(); }
				  },

				  // 2) Refrescar (diseño solicitado)
				  {
				    text:
				      '<span class="fab fa-firefox-browser me-1"></span>' +
				      '<span class="d-none d-sm-inline-block" id="VISOR_BTN_REFRESCAR">Actualizar</span>',
				    className: 'btn btn-falcon-success btn-sm mb-1 me-1',
				    action: function (e, dt, node) {
				      validarFechasEmitidos();
				      
				    }
				  },

				  // 3) Exportar Excel (Descarga Emitidos)
				  {
				    text:
				      '<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span>' +
				      '<span class="d-none d-sm-inline-block ms-1">Exportar Excel</span>',
				    className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    action: function(){ exportExcelDescargaEmitidos(); }
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
					url: '/siarex247/cumplimientoFiscal/descargaSAT/emitidos/detalleEmitidos.action',
	                beforeSend: function( xhr ) {
	        			$('#overSeccion_Emitidos').css({display:'block'});
	        		},
	        		complete: function(jqXHR, textStatus){
			    		  $('#overSeccion_Emitidos').css({display:'none'});
				    },					
					data: function(d){
					   // ===== EXISTENTES (básicos) =====
					   d.rfcReceptor               = obtenerRFCReceptor();
					   d.razonSocialReceptor       = obtenerRazonReceptor();
					   d.existeBovedaDescarga      = existeBovedaEmitidos();
					   d.tipoComprobanteDescarga   = obtenerTipoComprobanteEmitidos();
					   d.fechaInicialDescarga      = obtenerFechaInicioEmitidos();
					   d.fechaFinalDescarga        = obtenerFechaFinalEmitidos();
					   d.uuidDescarga              = obtenerUUIDEmitidos();
					   d.estatusCFDI               = obtenerEstatusEmitidos();
					   tipoComprobanteDescarga   = $('#tipoComprobante_Emitidos').val();

					   // ===== NUEVOS operadores/valores =====
					   // Textos
					   d.uuidOperator   = $('#uuidOperatorE').val()   || 'contains';
					   d.rfcEmiOperator = $('#rfcEmiOperatorE').val() || 'contains';
					   d.nomEmiOperator = $('#nomEmiOperatorE').val() || 'contains';
					   d.rfcRecOperator = $('#rfcRecOperatorE').val() || 'contains';
					   d.nomRecOperator = $('#nomRecOperatorE').val() || 'contains';
					   d.pacOperator    = $('#pacOperatorE').val()    || 'contains';
					   d.efectoOperator = $('#efectoOperatorE').val() || 'contains';
					   d.estatusOperator= $('#estatusOperatorE').val()|| 'contains';
					   d.bovedaOperator = $('#bovedaOperatorE').val() || 'contains';
					   d.pacOperator     = $('#pacOperatorE').val()     || 'contains';
					     // Para selects, mejor 'eq' cuando hay valor (si no viene del hidden)
					      d.efectoOperator  = $('#efectoOperatorE').val()  || ( ($('#efecto_Emitidos').val()||'') ? 'eq' : '' );
					    d.estatusOperator = $('#estatusOperatorE').val() || ( ($('#estatusCFDI_Emitidos').val()||'') ? 'eq' : '' );
					    d.bovedaOperator  = $('#bovedaOperatorE').val()  || ( ($('#existeBoveda_Emitidos').val()||'') ? 'eq' : '' );


					   // Numérico
					   d.montoV1       = $('#montoFilter1E').val();
					   d.montoV2       = $('#montoFilter2E').val();
					   d.montoOperator = $('#montoOperatorE').val()   || 'eq';

					   // Fechas
					   d.emiDateOperator = $('#emiDateOperatorE').val() || 'eq';
					   d.emiDateV1       = $('#emiDateFilter1E').val();
					   d.emiDateV2       = $('#emiDateFilter2E').val();

					   d.cerDateOperator = $('#cerDateOperatorE').val() || 'eq';
					   d.cerDateV1       = $('#cerDateFilter1E').val();
					   d.cerDateV2       = $('#cerDateFilter2E').val();

					   d.canDateOperator = $('#canDateOperatorE').val() || 'eq';
					   d.canDateV1       = $('#canDateFilter1E').val();
					   d.canDateV2       = $('#canDateFilter2E').val();
					   
					     // Debug opcional: inspecciona lo que realmente envías
					     console.log('[payload.descarga]', {
					     tipoComprobanteDescarga: d.tipoComprobanteDescarga,
					      efectoOperator: d.efectoOperator,
					       estatusCFDI: d.estatusCFDI,
					       estatusOperator: d.estatusOperator,
					       existeBovedaDescarga: d.existeBovedaDescarga,
					       bovedaOperator: d.bovedaOperator
					      });

					   d._ = Date.now(); // anti-cache opcional
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
			});
			
		} catch(e) {
			alert('usuarios()_'+e);
		};
		
		tablaDetalleEmitidos_Descarga.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	
	
	function filtraDatos(columna, texto) {
		tablaDetalleEmitidos_Descarga
			.column(columna)
	        .search(texto)
	        .draw();
	}

	
	function obtenerFechasFiltroDescargaEmitidos(){
			$.ajax({
				url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/consultarFechas.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						 obtenerFechasMinimaDescargaEmitidos(data.fechaInicial, data.fechaFinal);
						 
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerFechasFiltroDescargaEmitidos()_'+thrownError);
				}
			});	
					
		}
		
		
	function validarFechasEmitidos(){
				//var fechaInicial = $('#fechaInicial_Recibidos').val();
				//var fechaFinal = $('#fechaFinal_Recibidos').val();

				var fechaInicial = obtenerFechaInicioEmitidos();
				var fechaFinal =   obtenerFechaFinalEmitidos();

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
								window.__copiarTheadAGlobalesE_DESC();
								refrescarEmitidos_DESC()
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
						alert('validarFechasEmitidos()_'+thrownError);
					}
				});	
				
		    }


					
					
	
	function obtenerRFCReceptor(){
		var rfcReceptor = $('#rfcReceptor_Emitidos').val();
		return $('#rfcReceptor_Emitidos').val();
	}
	
	function obtenerRazonReceptor(){
		return $('#razonSocialReceptor_Emitidos').val();
	}
	
	function obtenerFechaInicioEmitidos(){
		return $('#fechaInicial_Emitidos').val();
	}
	
	function obtenerFechaFinalEmitidos(){
		return $('#fechaFinal_Emitidos').val();
	}
	
	function existeBovedaEmitidos(){
		var existeBovedaDescarga = $('#existeBoveda_Emitidos').val();
		return existeBovedaDescarga;
	}
	
	 function obtenerTipoComprobanteEmitidos(){
	   // Prioriza el hidden que sí se sincroniza desde el thead (efecto)
	  // y si no existe, usa el antiguo por compatibilidad.
	  return $('#efecto_Emitidos').val() || $('#tipoComprobante_Emitidos').val() || '';
	 }

	
	function obtenerUUIDEmitidos(){
		return $('#uuidDescarga_Emitidos').val();
	}
	
	function obtenerEstatusEmitidos(){
		return $('#estatusCFDI_Emitidos').val();
	}

	function refrescarEmitidos_DESC(){
			$('#tablaDetalleEmitidos_Descarga').DataTable().ajax.reload(null,true);	
	}
	
		 
		 function limpiarEmitidos(){
		   try{
		     // 1) Globales (tu página usa estos ids)
		     $('#rfcReceptor_Emitidos,#razonSocialReceptor_Emitidos,#uuidDescarga_Emitidos,#rfcEmisor_Emitidos,#razonSocialEmisor_Emitidos,#rfcPac_Emitidos,#tipoComprobante_Emitidos,#estatusCFDI_Emitidos,#existeBoveda_Emitidos').val('');
		     $('#fechaInicial_Emitidos,#fechaFinal_Emitidos').val('');

		     // 2) Helper
		     const setOp=(hiddenSel,labelSel,val,labelHtml)=>{ $(hiddenSel).val(val); $(labelSel).html(labelHtml); };

		     // 3) Textos thead
		     $('#uuidFilterInputE,#rfcEmiFilterInputE,#nomEmiFilterInputE,#rfcRecFilterInputE,#nomRecFilterInputE,#pacFilterInputE').val('');
		     setOp('#uuidOperatorE','#uuidOpBtnE .op-label','contains','<i class="fas fa-search"></i>');
		     setOp('#rfcEmiOperatorE','#rfcEmiOpBtnE .op-label','contains','<i class="fas fa-search"></i>');
		     setOp('#nomEmiOperatorE','#nomEmiOpBtnE .op-label','contains','<i class="fas fa-search"></i>');
		     setOp('#rfcRecOperatorE','#rfcRecOpBtnE .op-label','contains','<i class="fas fa-search"></i>');
		     setOp('#nomRecOperatorE','#nomRecOpBtnE .op-label','contains','<i class="fas fa-search"></i>');
		     setOp('#pacOperatorE',   '#pacOpBtnE .op-label',   'contains','<i class="fas fa-search"></i>');

		     // selects
		     $('#efectoFilterInputE').val(''); setOp('#efectoOperatorE','#efectoOpBtnE .op-label','contains','<i class="fas fa-search"></i>');
		     $('#estatusFilterE').val('ALL'); setOp('#estatusOperatorE','#estatusOpBtnE .op-label','contains','<i class="fas fa-search"></i>');
		     $('#bovedaFilterE').val('ALL'); setOp('#bovedaOperatorE','#bovedaOpBtnE .op-label','contains','<i class="fas fa-search"></i>');

		     // 4) Numéricos
		     const resetNum=(opSel,v1Sel,v2Sel,btnSel)=>{ $(opSel).val('eq'); $(v1Sel).val(''); $(v2Sel).val('').addClass('d-none'); $(btnSel+' .op-label').text('='); };
		     resetNum('#montoOperatorE','#montoFilter1E','#montoFilter2E','#montoOpBtnE');

		     // 5) Fechas
		     const resetDate=(opSel,d1Sel,d2Sel,btnSel)=>{ $(opSel).val('eq'); $(d1Sel).val(''); $(d2Sel).val('').addClass('d-none'); $(btnSel+' .op-label').text('='); };
		     resetDate('#emiDateOperatorE','#emiDateFilter1E','#emiDateFilter2E','#emiDateOpBtnE');
		     resetDate('#cerDateOperatorE','#cerDateFilter1E','#cerDateFilter2E','#cerDateOpBtnE');
		     resetDate('#canDateOperatorE','#canDateFilter1E','#canDateFilter2E','#canDateOpBtnE');

		     // 6) Cerrar menús
		     $('#tablaDetalleEmitidos_Descarga thead .dx-like-menu, .dtfh-floatingparent thead .dx-like-menu').removeClass('show');
			 
			 // limpiar rango
			 var rangeDE = document.getElementById('CRMDateRangeDE');
			 if (rangeDE){ rangeDE.value=''; rangeDE._flatpickr && rangeDE._flatpickr.clear(); }
			 // limpiar thead
			 $('#emiDateOperatorE').val('eq');
			 $('#emiDateFilter1E').val('').hide();
			 $('#emiDateFilter2E').val('').hide();
			 $('#emiDateOpBtnE .op-label').text('=');
			 
			 obtenerFechasFiltroSatE();


		     // 7) Recargar
		     if (typeof refrescarEmitidos_DESC==='function') refrescarEmitidos_DESC('clear-all');
		     else $('#tablaDetalleEmitidos_Descarga').DataTable().ajax.reload(null,false);
		   }catch(e){ alert('limpiarEmitidos()_'+e); }
		 }
		 
		 function obtenerFechasFiltroSatE(){
		 //console.log('ENTRO');
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
		 							$('#CRMDateRangeDE').val(fechas);
		 							
		 							
		 						}
		 					},
		 					error : function(xhr, ajaxOptions, thrownError) {
		 						alert('obtenerFechasFiltro()_'+thrownError);
		 					}
		 				});	
		 	
		    }
		 
		 

		 function exportExcelDescargaEmitidos(){
		   try{
			var fechaInicial = obtenerFechaInicioEmitidos();
			var fechaFinal =   obtenerFechaFinalEmitidos();

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
							if (typeof __copiarTheadAGlobalesE_DESC==='function') __copiarTheadAGlobalesE_DESC();

									     // Básicos
									     var rfcReceptor               = $('#rfcReceptor_Emitidos').val();
									     var razonSocialReceptor       = $('#razonSocialReceptor_Emitidos').val();
									     var existeBovedaDescarga      = $('#existeBoveda_Emitidos').val();
									     var tipoComprobanteDescarga   = $('#tipoComprobante_Emitidos').val();
									     var fechaInicialDescarga      = $('#fechaInicial_Emitidos').val();
									     var fechaFinalDescarga        = $('#fechaFinal_Emitidos').val();
									     var uuidDescarga              = $('#uuidDescarga_Emitidos').val();
									     var estatusCFDI               = $('#estatusCFDI_Emitidos').val();

									     // Operadores
									     var uuidOperator    = $('#uuidOperatorE').val()    || 'contains';
									     var rfcEmiOperator  = $('#rfcEmiOperatorE').val()  || 'contains';
									     var nomEmiOperator  = $('#nomEmiOperatorE').val()  || 'contains';
									     var rfcRecOperator  = $('#rfcRecOperatorE').val()  || 'contains';
									     var nomRecOperator  = $('#nomRecOperatorE').val()  || 'contains';
									     var pacOperator     = $('#pacOperatorE').val()     || 'contains';
									     var efectoOperator  = $('#efectoOperatorE').val()  || 'contains';
									     var estatusOperator = $('#estatusOperatorE').val() || 'contains';
									     var bovedaOperator  = $('#bovedaOperatorE').val()  || 'contains';

									     var montoV1         = $('#montoFilter1E').val();
									     var montoV2         = $('#montoFilter2E').val();
									     var montoOperator   = $('#montoOperatorE').val()   || 'eq';

									     var emiDateOperator = $('#emiDateOperatorE').val() || 'eq';
									     var emiDateV1       = $('#emiDateFilter1E').val();
									     var emiDateV2       = $('#emiDateFilter2E').val();

									     var cerDateOperator = $('#cerDateOperatorE').val() || 'eq';
									     var cerDateV1       = $('#cerDateFilter1E').val();
									     var cerDateV2       = $('#cerDateFilter2E').val();

									     var canDateOperator = $('#canDateOperatorE').val() || 'eq';
									     var canDateV1       = $('#canDateFilter1E').val();
									     var canDateV2       = $('#canDateFilter2E').val();

									     // Llenar form XLS
									     $('#rfcReceptorXLS_Emitidos').val(rfcReceptor);
									     $('#razonSocialReceptorXLS_Emitidos').val(razonSocialReceptor);
									     $('#existeBovedaDescargaXLS_Emitidos').val(existeBovedaDescarga);
									     $('#tipoComprobanteDescargaXLS_Emitidos').val(tipoComprobanteDescarga);
									     $('#fechaInicialDescargaXLS_Emitidos').val(fechaInicialDescarga);
									     $('#fechaFinalDescargaXLS_Emitidos').val(fechaFinalDescarga);
									     $('#uuidDescargaXLS_Emitidos').val(uuidDescarga);
									     $('#estatusCFDIXLS_Emitidos').val(estatusCFDI);

									     $('#uuidOperatorXLS_Emitidos').val(uuidOperator);
									     $('#rfcEmiOperatorXLS_Emitidos').val(rfcEmiOperator);
									     $('#nomEmiOperatorXLS_Emitidos').val(nomEmiOperator);
									     $('#rfcRecOperatorXLS_Emitidos').val(rfcRecOperator);
									     $('#nomRecOperatorXLS_Emitidos').val(nomRecOperator);
									     $('#pacOperatorXLS_Emitidos').val(pacOperator);
									     $('#efectoOperatorXLS_Emitidos').val(efectoOperator);
									     $('#estatusOperatorXLS_Emitidos').val(estatusOperator);
									     $('#bovedaOperatorXLS_Emitidos').val(bovedaOperator);

									     $('#montoV1XLS_Emitidos').val(montoV1);
									     $('#montoV2XLS_Emitidos').val(montoV2);
									     $('#montoOperatorXLS_Emitidos').val(montoOperator);

									     $('#emiDateOperatorXLS_Emitidos').val(emiDateOperator);
									     $('#emiDateV1XLS_Emitidos').val(emiDateV1);
									     $('#emiDateV2XLS_Emitidos').val(emiDateV2);

									     $('#cerDateOperatorXLS_Emitidos').val(cerDateOperator);
									     $('#cerDateV1XLS_Emitidos').val(cerDateV1);
									     $('#cerDateV2XLS_Emitidos').val(cerDateV2);

									     $('#canDateOperatorXLS_Emitidos').val(canDateOperator);
									     $('#canDateV1XLS_Emitidos').val(canDateV1);
									     $('#canDateV2XLS_Emitidos').val(canDateV2);

									     document.frmDescargaXMLExcelEmitidos.submit();
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
					alert('exportExcelDescargaEmitidos()_'+thrownError);
				}
			});	
			
		   }catch(e){ alert('exportExcelDescargaEmitidos(): '+e); }
		 }
		 
	
		
		
		 /* =======================
		  * DESCARGA EMITIDOS – Filtros DX-like
		  * Menús + Enter + AUTO en Estatus/Efecto/Bóveda
		  * Independiente de Bóveda - VERSIÓN CORREGIDA (+ Enter abre segundo input)
		  * ======================= */
		 (function(){
		   'use strict';

		   const NS    = 'DESC_EMI';
		   const TABLE = '#tablaDetalleEmitidos_Descarga';

		   if (window.__descEmiBoot) return;
		   window.__descEmiBoot = true;

		   /* -------- Helpers -------- */
		   function __isFromClone(el){
		     return !!$(el).closest('.dtfh-floatingparent').length;
		   }
		   function __hiddenForMenu($menu){
		     const id = $menu.attr('id') || '';
		     return '#' + id.replace('OpMenu','Operator');
		   }
		   function __secondIdForMenu(mid){
		     if (/^montoOpMenu$/i.test(mid))   return 'montoFilter2E';
		     if (/^emiDateOpMenu$/i.test(mid)) return 'emiDateFilter2E';
		     if (/^cerDateOpMenu$/i.test(mid)) return 'cerDateFilter2E';
		     if (/^canDateOpMenu$/i.test(mid)) return 'canDateFilter2E';
		     return null;
		   }
		   // ✅ NUEVOS: toggle del segundo input y utilidades de inputs
		   function __toggleSecondByFilter($filter, show){
		     // Busca el 2º input dentro del filtro (text/number/date)
		     const $inputs = $filter.find('input[type=text], input[type=number], input[type=date]');
		     const $second = $inputs.eq(1);
		     if (!$second.length) return;

		     if (show){
		       $second.removeClass('d-none').closest('.d-none').removeClass('d-none');
		       // Si el segundo input está oculto por CSS/estilos inline, quítalo
		       $second.show();
		       // En algunos layouts el 2º input está envuelto: intenta quitar d-none al contenedor inmediato
		       const $wrap = $second.closest('.second-input-wrap');
		       if ($wrap.length) $wrap.removeClass('d-none');
		     } else {
		       // Limpia valor y oculta
		       $second.val('').addClass('d-none');
		       const $wrap = $second.closest('.second-input-wrap');
		       if ($wrap.length) $wrap.addClass('d-none');
		     }
		   }

		   // ✅ Ayuda: ¿los inputs del filtro tienen valor suficiente para disparar?
		   function __inputsHaveValue($filterContainer) {
		     const $inps = $filterContainer.find('input[type=text], input[type=number], input[type=date]');
		     const $inp1 = $inps.eq(0);
		     const $inp2 = $inps.eq(1);
		     const op = ($filterContainer.find('input[id$="OperatorE"]').val() || '').toLowerCase();

		     const v1 = ($inp1.val() || '').trim();
		     if (op === 'bt') { // between/entre
		       const v2 = ($inp2.is(':visible') && !$inp2.hasClass('d-none')) ? ($inp2.val() || '').trim() : '';
		       return v1 !== '' && v2 !== '';
		     }
		     return v1 !== '';
		   }

		   // Copia valores del thead a los hiddens que lee ajax.data
		   window.__copiarTheadAGlobalesE_DESC = function(){
		     const pairs = [
		       ['#uuidFilterInputE',    '#uuidDescarga_Emitidos'],
		       ['#rfcEmiFilterInputE',  '#rfcEmisor_Emitidos'],
		       ['#nomEmiFilterInputE',  '#razonSocialEmisor_Emitidos'],
		       ['#rfcRecFilterInputE',  '#rfcReceptor_Emitidos'],
		       ['#nomRecFilterInputE',  '#razonSocialReceptor_Emitidos'],
		       ['#pacFilterInputE',     '#rfcPac_Emitidos'],
		       ['#estatusFilterE',      '#estatusCFDI_Emitidos'],
		       ['#bovedaFilterE',       '#existeBoveda_Emitidos'],
		       ['#efectoFilterInputE',  '#efecto_Emitidos'],
		       ['#efectoFilterInputE',  '#tipoComprobante_Emitidos']
		     ];
		     pairs.forEach(([from,to])=>{ if ($(from).length && $(to).length) $(to).val(($(from).val()||'').trim()); });

		     /*const dates = [
		       ['#emiDateFilter1E', '#fechaInicial_Emitidos'],
		       ['#emiDateFilter2E', '#fechaFinal_Emitidos']
		     ];*/
		     // dates.forEach(([from,to])=>{ if ($(from).length && $(to).length) $(to).val($(from).val()||''); });
		   };

		   /* -------- Recarga coalescida -------- */
		   let __busy=false, __queue=false, __deb=null;
		   window.refrescarEmitidos_DESC = function(reason){
		     if (__deb) clearTimeout(__deb);
		     __deb = setTimeout(function(){
		       const dt = $.fn.dataTable.isDataTable(TABLE) ? $(TABLE).DataTable() : null;
		       if (!dt) return;
		       if (__busy){ __queue=true; return; }
		       __busy = true;
		       try{
		         if (typeof __copiarTheadAGlobalesE_DESC==='function') __copiarTheadAGlobalesE_DESC();
		         dt.ajax.reload(function(){
		           __busy=false;
		           if (__queue){ __queue=false; window.refrescarEmitidos_DESC('coalesced'); }
		         }, false);
		       }catch(e){
		         __busy=false; console.error('[DESC_EMI] reload', e);
		       }
		     }, 120);
		   };

		   /* ======================================================
		    * 1) ABRIR MENÚS (click en el BOTÓN/ICONO del filtro)
		    * ====================================================== */
		   const FILTER_HEAD = TABLE+' thead .dx-like-filter, .dtfh-floatingparent thead .dx-like-filter';
		   const MENU_BUTTON = FILTER_HEAD + ' .op-btn';
		   
		   /* ==========================================================
		    * PARCHE v2 – Solo abre con .op-btn, PERO deja seleccionar en el menú
		    *  - Permite clics dentro de .dx-like-menu (li, etc.)
		    *  - Sigue bloqueando que el input/contendor abran el menú
		    * ========================================================== */
		   (function(){
		     const ROOT_SEL    = '#tablaDetalleEmitidos_Descarga';
		     const FILTER_HEAD = ROOT_SEL + ' thead .dx-like-filter, .dtfh-floatingparent thead .dx-like-filter';
		     const FILTER_MENU = FILTER_HEAD + ' .dx-like-menu';
		     const FILTER_INP  = FILTER_HEAD + ' input, ' + FILTER_HEAD + ' textarea, ' + FILTER_HEAD + ' select';

		     // Limpia listeners del parche previo (si existían)
		     $(document).off('.DESC_EMI_SHIELD').off('.DESC_EMI_INPUT');

		     // 1) Captura nativa: BLOQUEA clics en el filtro que NO sean:
		     //    - el botón .op-btn, o
		     //    - elementos dentro del menú (.dx-like-menu)
		     document.addEventListener('click', function(ev){
		       const t       = ev.target;
		       const inFilter= !!t.closest('.dx-like-filter');
		       const isBtn   = !!t.closest('.op-btn');
		       const inMenu  = !!t.closest('.dx-like-menu');
		       if (inFilter && !isBtn && !inMenu){
		         ev.stopPropagation();
		         // opcional: si tienes handlers muy agresivos, habilita:
		         // ev.preventDefault();
		       }
		     }, true); // CAPTURA

		     // Cierra menús si entras al input (pero no estorba al menú)
		     document.addEventListener('focus', function(ev){
		       const t      = ev.target;
		       const inMenu = t && t.closest && t.closest('.dx-like-menu');
		       const inFilt = t && t.closest && t.closest('.dx-like-filter');
		       if (inFilt && !inMenu && !t.closest('.op-btn')){
		         if (typeof __cerrarMenus === 'function') __cerrarMenus();
		         ev.stopPropagation();
		       }
		     }, true); // CAPTURA

		     // 2) Shield jQuery: si el click fue en el contenedor del filtro,
		     //    PERO no viene de .op-btn ni de dentro del menú, lo anulamos.
		     $(document)
		       .off('click.DESC_EMI_SHIELD', FILTER_HEAD)
		       .on('click.DESC_EMI_SHIELD', FILTER_HEAD, function(e){
		         const $t = $(e.target);
		         const fromBtn  = !!$t.closest('.op-btn').length;
		         const inMenu   = !!$t.closest('.dx-like-menu').length;
		         if (!fromBtn && !inMenu){
		           e.stopImmediatePropagation();
		           // e.preventDefault(); // usa si otro script aún abre el menú
		         }
		       });

		     // 3) Inputs: nunca deben abrir menú; cerrar menú al entrar
		     $(document)
		       .off('focus.DESC_EMI_INPUT', FILTER_INP)
		       .on('focus.DESC_EMI_INPUT', FILTER_INP, function(e){
		         e.stopPropagation();
		         if (typeof __cerrarMenus === 'function') __cerrarMenus();
		       })
		       .off('click.DESC_EMI_INPUT', FILTER_INP)
		       .on('click.DESC_EMI_INPUT', FILTER_INP, function(e){
		         e.stopPropagation();
		       })
		       .off('mousedown.DESC_EMI_INPUT', FILTER_INP)
		       .on('mousedown.DESC_EMI_INPUT', FILTER_INP, function(e){
		         e.stopPropagation();
		       })
		       .off('keydown.DESC_EMI_INPUT', FILTER_INP)
		       .on('keydown.DESC_EMI_INPUT', FILTER_INP, function(e){
		         e.stopPropagation();
		       });
		   })();

		   
		   

		   $(document)
		     .off('click.'+NS, MENU_BUTTON)
		     .on('click.'+NS,  MENU_BUTTON, function(e){
		       if (__isFromClone(this)) return;
		       e.preventDefault(); e.stopPropagation();
		       const $f    = $(this).closest('.dx-like-filter');
		       const $menu = $f.find('.dx-like-menu');
		       $(FILTER_HEAD+' .dx-like-menu').not($menu).removeClass('show'); // cierra otros
		       $menu.toggleClass('show');
		     });

		   function __cerrarMenus(){ $(FILTER_HEAD+' .dx-like-menu').removeClass('show'); }
		   $(document).on('click.'+NS+'-close', __cerrarMenus);
		   $(window).on('scroll.'+NS+' resize.'+NS, __cerrarMenus);
		   $(TABLE).on('draw.dt.'+NS, __cerrarMenus);

		   /* ======================================================
		    * 2) CAMBIO DE OPERADOR (click en <li>) => recarga condicional
		    *    y si es "Entre", mostrar el 2º input
		    * ====================================================== */
		   const MENU_ITEMS = TABLE+' thead .dx-like-menu li, .dtfh-floatingparent thead .dx-like-menu li';

		   $(document)
		     .off('click.'+NS, MENU_ITEMS)
		     .on('click.'+NS,  MENU_ITEMS, function(e){
		       if (__isFromClone(this)) return;
		       e.preventDefault(); e.stopPropagation();

		       const $li   = $(this);
		       let op      = String($li.data('op')||'').toLowerCase();
		       op = (op==='between' ? 'bt' : op);

		       const $menu = $li.closest('.dx-like-menu');
		       const mid   = $menu.attr('id') || '';
		       const $f    = $menu.closest('.dx-like-filter');
		       const $hid  = $(__hiddenForMenu($menu));
		       const $lbl  = $f.find('.op-label');
		       if (!$hid.length) return;

		       const firstToken = $.trim($li.text()).split(/\s+/)[0];
		       $lbl.text(firstToken);
		       $hid.val(op);

		       // ⬇️ Mostrar/ocultar segundo input según operador
		       __toggleSecondByFilter($f, op === 'bt');

		       $menu.removeClass('show');

		       // Si ya hay valores suficientes, dispara; si no, espera
		       if (__inputsHaveValue($f)) {
		         window.refrescarEmitidos_DESC('menu-op');
		       } else {
		         // si es "Entre", enfoca el 2º para que el usuario complete
		         if (op === 'bt'){
		           const $inps = $f.find('input[type=text], input[type=number], input[type=date]');
		           $inps.eq(1).focus();
		         }
		       }
		     });

		   /* ======================================================
		    * 2B) SELECTS: Estatus / Efecto / Bóveda => AUTO
		    * ====================================================== */
		   const SELECTS = '#estatusFilterE, #efectoFilterInputE, #bovedaFilterE';
		   $(document)
		     .off('change.'+NS, SELECTS)
		     .on('change.'+NS,  SELECTS, function(){
		       window.refrescarEmitidos_DESC('select-change-'+this.id);
		     });

		   /* ======================================================
		    * 3) ENTER en inputs => si es "Entre" y estás en el 1º,
		    *    abre el 2º y enfoca; si ya están ambos, recarga.
		    * ====================================================== */
		   const INPUTS = 'input[id$="FilterInputE"], input[id$="Filter1E"], input[id$="Filter2E"]';
		   $(document)
		     .off('keydown.'+NS, INPUTS)
		     .on('keydown.'+NS,  INPUTS, function(e){
		       if (e.key !== 'Enter') return;
		       if (__isFromClone(this)) return;
		       e.preventDefault();

		       const $f     = $(this).closest('.dx-like-filter');
		       const $inps  = $f.find('input[type=text], input[type=number], input[type=date]');
		       const $inp1  = $inps.eq(0);
		       const $inp2  = $inps.eq(1);
		       const op     = ($f.find('input[id$="OperatorE"]').val()||'').toLowerCase();
		       const v1     = ($inp1.val()||'').trim();
		       const v2     = ($inp2.val()||'').trim();

		       // Caso especial: operador "Entre"
		       if (op === 'bt'){
		         // Si el 2º está oculto o vacío, muéstralo y enfoca, NO recargues aún
		         const hiddenSecond = !$inp2.is(':visible') || $inp2.hasClass('d-none');
		         if (hiddenSecond){
		           __toggleSecondByFilter($f, true);
		           $inp2.focus();
		           return;
		         }
		         // Si el 2º ya está visible pero falta v2, enfoca
		         if (!v2){
		           $inp2.focus();
		           return;
		         }
		         // Si v1 y v2 están listos, recarga
		         if (v1 && v2){
		           window.refrescarEmitidos_DESC('enter-between');
		           return;
		         }
		         // Si falta v1, regresa al 1º
		         if (!v1){
		           $inp1.focus();
		           return;
		         }
		         return;
		       }

		       // Operadores distintos a "Entre": con 1+ char dispara
		       if (v1.length >= 1){
				 window.__copiarTheadAGlobalesE_DESC();
		         // window.refrescarEmitidos_DESC('enter');
				 validarFechasEmitidos();
		       }
		     });

		 })();


		 


		