

var timerBoveda = 0;
var TIEMPO_LOG_BOVEDA = true;

var tablaDetalleBovedaComplemento = null;

	$(document).ready(function() {
		try {
			tablaDetalleBovedaComplemento = $('#tablaDetalleBovedaComplemento').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 10,
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Detalle Vincular', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'detalleVincular'}
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
					url: '/siarex247/cumplimientoFiscal/boveda/recibidos/mensajeComplementos.action',
					type: 'POST',
					data : {
						iden : function() { return obtenerIDENBoveda(); }
					}
					
				},
				aoColumns : [
					{ mData: "UUID", "sClass": "alinearCentro"},
					{ mData: "ESTATUS"},
					{ mData: "MENSAJE"}
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
		
		tablaDetalleBovedaComplemento.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	
	function procesaVincularBoveda(){
	  try{
	    // valores “básicos”
	    var fechaInicial  = obtenerFechaIni_Recibidos();
	    var fechaFinal    = obtenerFechaFin_Recibidos();
	    var rfc           = obtenerRFC_Recibidos();
	    var razonSocial   = obtenerRazon_Recibidos();
	    var uuid          = obtenerUUID_Recibidos();
	    var serie         = obtenerSerie_Recibidos();
	    var folio         = obtenerFolio_Recibidos();

	    // 🔽🔽 Operadores y valores EXACTO como los manda la tabla principal
	    var payload = {
	      rfc           : rfc,
	      razonSocial   : razonSocial,
	      folio         : folio,
	      serie         : serie,
	      fechaInicial  : fechaInicial,
	      fechaFinal    : fechaFinal,
	      uuid          : uuid,

	      // texto
	      rfcOperator    : ($('#rfcOperator').val()    || 'contains').toLowerCase(),
	      razonOperator  : ($('#razonOperator').val()  || 'contains').toLowerCase(),
	      serieOperator  : ($('#serieOperator').val()  || 'contains').toLowerCase(),
	      tipoOperator   : ($('#tipoOperator').val()   || 'equals').toLowerCase(),   // aunque no lo uses, va para uniformidad
	      uuidOperator   : ($('#uuidOperator').val()   || 'contains').toLowerCase(),

	      // numéricos
	      folioOperator  : $('#folioOperator').val() || 'eq',
	      folioV1        : $('#folioFilter1').val(),
	      folioV2        : $('#folioFilter2').val(),

	      totalOperator  : $('#totalOperator').val() || 'eq',
	      totalV1        : $('#totalFilter1').val(),
	      totalV2        : $('#totalFilter2').val(),

	      subOperator    : $('#subOperator').val()   || 'eq',
	      subV1          : $('#subFilter1').val(),
	      subV2          : $('#subFilter2').val(),

	      ivaOperator    : $('#ivaOperator').val()   || 'eq',
	      ivaV1          : $('#ivaFilter1').val(),
	      ivaV2          : $('#ivaFilter2').val(),

	      ivaRetOperator : $('#ivaRetOperator').val()|| 'eq',
	      ivaRetV1       : $('#ivaRetFilter1').val(),
	      ivaRetV2       : $('#ivaRetFilter2').val(),

	      isrOperator    : $('#isrOperator').val()   || 'eq',
	      isrV1          : $('#isrFilter1').val(),
	      isrV2          : $('#isrFilter2').val(),

	      impLocOperator : $('#impLocOperator').val()|| 'eq',
	      impLocV1       : $('#impLocFilter1').val(),
	      impLocV2       : $('#impLocFilter2').val(),

	      // fecha con operador
	      dateOperator   : $('#dateOperator').val() || 'eq',
	      dateV1         : $('#dateFilter1').val(),
	      dateV2         : $('#dateFilter2').val()
	    };

	    $('#btnVincularComplementoBoveda').hide();
	    $.ajax({
	      url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/vicularComplementosBoveda.action',
	      type : 'POST',
	      data : payload,
	      dataType : 'json',
	      success  : function(data) {
	        if (data.ESTATUS == 'OK') {
	          Swal.fire({
	            title: '¡Operación Exitosa!',
	            text: 'Proceso de Vincular complementos fue iniciado satisfactoriamente, por favor revise sus estatus de los totales.',
	            showCancelButton: false,
	            confirmButtonText: 'Aceptar',
	            icon: 'success'
	          }).then((result) => {
	            $('#IDEN_Boveda').val(data.IDEN);
	            monitorConsolaBoveda();
	          });
	        } else {
	          Swal.fire({
	            title: '¡Operación No Exitosa!',
	            html: '<p>'+ (data.mensajeError || 'Ocurrió un problema') +'</p>',
	            icon: 'error'
	          });
	        }
	      },
	      error : function(xhr, ajaxOptions, thrownError) {
	        alert('procesaVincularBoveda()_'+thrownError);
	      }
	    });

	  }catch(e){
	    alert('procesaVincularBoveda()_'+e);
	  }
	}

	
	function monitorConsolaBoveda(){
        try{
        	if (TIEMPO_LOG_BOVEDA){
        		timerBoveda = setTimeout(calcularProcesadosBoveda, 2000);
        	}
        }
        catch(e){
            alert('monitorConsolaBoveda(): ' + e);
        }
      }
   
	
	   function calcularProcesadosBoveda(){
			try{
				var IDEN = obtenerIDENBoveda();
				
		 		$.ajax({
					url  : '/siarex247/cumplimientoFiscal/boveda/recibidos/calcularProcesados.action',
					type : 'POST', 
					data : {
						iden : IDEN
					},
					dataType : 'json',
					success  : function(data) {
						try{
							  var totReg = parseFloat( $('#totalXML_Boveda').val());
							   var totalVinculados = data.TOT_OK;
							  var totalError = data.TOT_NOK;
							  
							  $('#totalVinculados_Boveda').val(data.TOT_OK);
							  $('#totalError_Boveda').val(data.TOT_NOK);
							   
							   
							  if (isNaN(totalVinculados) || totalVinculados == 'NaN' || totalVinculados == ''){
								  totalVinculados = 0;
							  }
							  
							  if (isNaN(totalError) || totalError == 'NaN' || totalError == ''){
								  totalError = 0;
							  }
							   var totProcesados =  (parseFloat(totalVinculados) + parseFloat(totalError));
							   if (totReg <= totProcesados ){
								    clearTimeout(timerBoveda);
									TIEMPO_LOG_BOVEDA = false;
					           }
							   $('#tablaDetalleBovedaComplemento').DataTable().ajax.reload(null,false);
							  monitorConsolaBoveda();
						}catch(e){
							alert('refreshVincular()_'+e);
						}
					},
					error : function(xhr, ajaxOptions, thrownError) {
						alert('calcularProcesadosBoveda()_'+thrownError);
					}
				});			
				
			}catch(e){
				alert('calcularProcesadosBoveda()_'+e);
			}
		}

	   
   
   function obtenerIDENBoveda(){
	   var IDEN = $('#IDEN_Boveda').val();
	   return IDEN;
   }
	
	function cierraVentana(){
		TIEMPO_LOG_BOVEDA = false;
    }

