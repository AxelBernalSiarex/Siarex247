var tablaHistorial = null;
var intervaloVerificacion = null;

$(document).ready(function() {
    try {
		consultarFechaHistorial();
        tablaHistorial = $('#tablaHistorialPagos').DataTable({
            paging      : true,
            retrieve    : true,
            pageLength  : 15,
            lengthChange: false,
            dom: "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" +
                 "<'table-responsive scrollbar'tr>" +
                 "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
            ordering    : true,
            serverSide  : false,
            fixedHeader : true,
            orderCellsTop: true,
            info        : true,
            select      : false,
            stateSave   : false,
            
            order       : [[ 1, 'desc' ]],
            buttons: [
               {
                    extend    : "collection",
                    autoClose : true,
                    text      : '<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span>' +
                                '<span class="d-none d-sm-inline-block ms-1">Export</span>',
                    className : 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
                    buttons   : [
                        {
                            extend : "excel",
                            text   : "Exportar Excel",
                            title  : "Historial de Pagos",
                            exportOptions: {
                                modifier: { selected: null, page: "all" }
                            },
                            filename: "historial_pagos"
                        }
                    ]
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
                    sFirst    : "Primero",
                    sLast     : "Último",
                    sNext     : "<span class='fa fa-chevron-right fa-w-10'></span>",
                    sPrevious : "<span class='fa fa-chevron-left fa-w-10'></span>"
                }
            },
            ajax : {
                url : '/siarex247/cumplimientoFiscal/historicoPagos/detalleHistorial.action',
                type: 'POST',
                dataType: 'json',
                dataSrc: 'data'
            },
			aoColumns : [
			    { data: "rfc" },
			    { data: "fechaPago",       className: "alinearCentro" },
			    { data: "tipoMoneda",      className: "alinearCentro" },
			    { data: "total",           className: "alinearDerecha",
			      render: $.fn.dataTable.render.number(',', '.', 2) },
			    { data: "uuidFactura"},
				{ data: "uuidComplemento"},
			    { data: "estatus" }
			],
			columnDefs: [
				{	targets: 4,
			        render: function (data, type, row) {
			        	rowElement = '<a href="javascript:verXMLBoveda(\'' + row.uuidFactura+ '\');">' + row.uuidFactura + '</a>';
			            return rowElement;
			        }
			    },
				{	targets: 5,
			        render: function (data, type, row) {
			        	rowElement = '<a href="javascript:verXMLBoveda(\'' + row.uuidComplemento+ '\');">' + row.uuidComplemento + '</a>';
			            return rowElement;
			        }
			    },
			],
			drawCallback: function () {
                $('[data-toggle="tooltip"]').tooltip();
            },
			initComplete: function () {

			
			    var btns = $('.dt-button');
			    btns.removeClass('dt-button');

			    var btnsSubMenu = $('.dtb-b2');
			    btnsSubMenu.addClass('dropdown-menu dropdown-menu-end py-0 show');

			    try {
			        var dtButtonsContainer = $('.dt-buttons'); 
			        if (dtButtonsContainer.length) {           
			            dtButtonsContainer.addClass('d-flex align-items-center justify-content-between w-100');
			            $('#fechaHistorialContainer').prependTo(dtButtonsContainer);
			            dtButtonsContainer.find('button').addClass('ms-auto');
			        }
			    } catch(e){
			        console.error('Error moviendo fecha:', e);
			    }

			}

        });

    } catch(e) {
        console.error('Error al iniciar tablaHistorialPagos:', e);
    }

    // === Filtros en encabezado (segunda fila del thead, igual patrón que Órdenes) ===
    $('#tablaHistorialPagos thead tr:eq(1) th').each(function (i) {
        var title = $(this).text();
        $(this).html(
            '<input type="text" class="form-control form-control-sm" ' +
            'placeholder="'+title+'" style="width: 100%;" />'
        );
    });

    $('#tablaHistorialPagos thead tr:eq(1) th').on('keyup', "input", function () {
        if (tablaHistorial) {
            tablaHistorial
                .column($(this).parent().index())
                .search(this.value)
                .draw();
        }
    });
});

/* =========================================================
   MODAL Y CARGA DE TXT
========================================================= */

function abreModalHistorial() {
    iniciaFormHistorial();
    $('#myModalHistorialPagos').modal('show');
}

function iniciaFormHistorial(){
    $("#frmHistorialPagos").find('.has-success').removeClass("has-success");
    $("#frmHistorialPagos").find('.has-error').removeClass("has-error");
    if($('#frmHistorialPagos').length > 0){
        $('#frmHistorialPagos')[0].reset();
        $('#frmHistorialPagos').removeClass("was-validated");
    }
}


	function cargarHistorialPagos(){
	   try{
		           var formData = new FormData(document.getElementById("frmHistorialPagos"));
		           formData.append("dato", "valor");
		           
		           $( "#submitImport" ).prop( "disabled", true );
		           $.ajax({
		        	   url: '/siarex247/cumplimientoFiscal/historicoPagos/cargarPagos.action',
		               type: "post",
		               dataType: "json",
		               beforeSend: function( xhr ) {
		        			$('#overSeccionImportar').css({display:'block'});
		        		},
		               data: formData,
		               complete: function(jqXHR, textStatus){
			    		  $('#overSeccionImportar').css({display:'none'});
			    		  $("#submitImport").prop('disabled', false);
					    },
		               cache: false,
		               contentType: false,
			    		 processData: false
		           })
		               .done(function(data){
	                		
		               	 if (data.noExitosos > 0){
		               		 
		               		Swal.fire({
		              		  icon : 'question',
		              		  title: MSG_ERROR_OPERACION_MENU,
		              		  html: 	data.mensajeError + "¿ Desea visualizar los registros con error ?",
		              		  showDenyButton: true,
		              		  showCancelButton: false,
		              		  confirmButtonText: BTN_ACEPTAR_MENU,
		              		  denyButtonText: BTN_CANCELAR_MENU,
		              		}).then((result) => {
			              		  if (result.isConfirmed) {
			              			abrirBitacora(data.idBitacora);
		 							 $('#myModalHistorialPagos').modal('hide');
		 							$('#tablaHistorialPagos').DataTable().ajax.reload(null,false);
			              		  } else if (result.isDenied) {
			              			$('#tablaHistorialPagos').modal('hide');
			           		 	 }
			              	});
		                 }else{
		               		if (data.codError == '001') {
			               			Swal.fire({
			               				title : MSG_ERROR_OPERACION_MENU,
			  			                html: '<p>'+data.mensajeError+'</p>',	
			  			                icon: 'error'
			  			            });
			               			
			                	}else{
		                			Swal.fire({
		                				title : MSG_ERROR_OPERACION_MENU,
			 							html  : data.mensajeError,
										showCancelButton: false,
										confirmButtonText: BTN_ACEPTAR_MENU,
										icon: 'success'
			 						}).then((result) => {
			 							$('#myModalHistorialPagos').modal('hide');
			  	 						$('#tablaHistorialPagos').DataTable().ajax.reload(null,false);
			 						});
			                	}
		                  }
	               });
	   }catch(e){
		   alert('importarProyectosBudget()_'+e);
	   }
   }
   
   

function refrescarHistorialPagos() {
    if(tablaHistorial != null){
        tablaHistorial.ajax.reload(null, false);
    }
}

   
   function abrirBitacora(idBitacora){
   	 try{
   		   iniciaFormImportarDetalle();
   		   $('#tablaDetalleBitacora').DataTable().ajax.url('/siarex247/cumplimientoFiscal/historicoPagos/detalleErrores.action?idBitacora='+idBitacora).load();
   		   $('#modalErrorPro').modal('show');
   		  
   	 }catch(e){
   		 alert('abrirBitacora()_'+e);
   	 }
    }
	
	// =========================================================
	// Abrir XML en Bóveda reutilizando mostrarXMLHistorial.jsp
	// =========================================================
	function verXMLBoveda(uuid) {
	    try {
	        // uuid: UUID que ya existe en la tabla BOVEDA.UUID
	        document.getElementById('idRegistroP').value = uuid;   // hidden name="f"
	        document.frmBoveda.submit();
	    } catch (e) {
	        alert('verXMLBoveda()_' + e);
	    }
	}
	
	function consultarFechaHistorial() {
	    $.ajax({
	        url  : '/siarex247/cumplimientoFiscal/historicoPagos/consultarUltimaFecha.action',
	        type : 'POST',
	        data : null,
	        dataType : 'json',
	        success  : function(data) {
	            if (!$.isEmptyObject(data)) {
	                document.getElementById("HP_ETQ_FECHA").innerHTML =
	                    "Última actualización: " + (data.fechaDescarga || "N/A");
	            }
	        }
	    });
	}



	


	





