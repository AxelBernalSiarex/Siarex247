


var tablaDetalle = null;

	$(document).ready(function() {
		try {
			
			var buttonCommon = {
					    exportOptions: {
					        format: {
					            body: function ( data, row, column, node ) {
									// alert('column ==> '+column);
									if (column == 0){
										// alert('data==>'+node.firstChild.tagName);
										const arrSigno = data.split(">");
										const noPedimento = arrSigno[1].substr(0, arrSigno[1].length - 3);
										return noPedimento;
										
									}else if (column == 13){ // subTotal
										const arrSigno = data.split("$");
										const arrDatos = arrSigno[1].split(",");
										if (arrDatos.length >= 2){
											// return 100;
											var strNumero = '';
											for (var x = 0; x < arrDatos.length; x++){
												strNumero+=arrDatos[x];
											}
											return strNumero;
										}else{
											return arrSigno[1];
										}
										
									}else if (column == 19){
										return '';
									
									}else{
										return data;	
									}
					            	
				            }
				        }
				    }
				};
				
				
			tablaDetalle = $('#tablaDetalle').DataTable( {
				paging      : true,
				retrieve: true,
				pageLength  : 15,
				lengthChange: false,
//				dom: 'Blfrtip',
//				dom: "<'row mx-0'<'col-md-12'QB><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				dom: "<'row mx-0'<'col-md-12'B><'col-md-6'l><'col-md-6'f>>" + "<'table-responsive scrollbar'tr>" + "<'row g-0 align-items-center justify-content-center justify-content-sm-between'<'col-auto mb-2 mb-sm-0 px-0'i><'col-auto px-0'p>>",
				ordering    : true,
				serverSide	: false,
				fixedHeader : true,
				orderCellsTop: true,
				info		: true,
				select      : true,
				stateSave	: false, 
				order       : [ [ 0, 'asc' ] ],	
				
				buttons: [
					{ 	// text: '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Cargar Pedimentos</span>',
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
							$.extend(!0, {}, buttonCommon,
									{ extend: "excel", text:'Exportar Excel',  title: 'Listado de Pedimentos', exportOptions: {modifier: {selected: null}}, filename: 'listadoPedimentos'},
								),
								
				    		// { extend: "excel", text:'Exportar Excel',  title: 'Listado de Pedimentos', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'listadoPedimentos'},
				    		// { extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Listado de Pedimentos", filename: 'listadoPedimentos', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/cumplimientoFiscal/pedimentos/detallePedimentos.action',
					data : {
						fechaInicial : function() { return obtenerFechaInicial(); },
						fechaFinal : function() { return obtenerFechaFinal(); }
					},
					type: 'POST'
				},
				aoColumns : [
					{ mData: "NUM_PEDIMENTO", "sClass": "alinearCentro"},
					{ mData: "CVE_PEDIMENTO", "sClass": "alinearCentro"},
					{ mData: "REGIMEN"},
					{ mData: "DTA", "sClass": "alinearCentro"},
					{ mData: "IVA", "sClass": "alinearCentro"},
					{ mData: "IGI", "sClass": "alinearCentro"},
					{ mData: "PRV", "sClass": "alinearCentro"},
					{ mData: "IVAPRV", "sClass": "alinearDerecha"},
					{ mData: "EFECTIVO", "sClass": "alinearDerecha"},
					{ mData: "OTROS", "sClass": "alinearDerecha"},
					{ mData: "TOTAL", "sClass": "alinearDerecha"},
					{ mData: "BANCO", "sClass": "alinearDerecha"},
					{ mData: "LINEA_CAPTURA", "sClass": "alinearCentro"},
					{ mData: "IMPORTE_PAGO", "sClass": "alinearCentro"},
					{ mData: "FECHA_PAGO", "sClass": "alinearCentro"},
					{ mData: "NUMERO_OPERACION", "sClass": "alinearCentro"},
					{ mData: "NUMERO_SAT", "sClass": "alinearCentro"},
					{ mData: "MEDIO_PRESENTACION", "sClass": "alinearCentro"},
					{ mData: "MEDIO_RECEPCION", "sClass": "alinearCentro"},
					{ mData: null, "sClass": "alinearCentro"}
				],
				columnDefs: [
					{	targets: 0,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:generaPDFPedimento(\'' + row.NUM_PEDIMENTO + '\');">' + row.NUM_PEDIMENTO + '</a>';
				            return rowElement;
				        }
				    },
				   {
				        targets: 19,
				        render: function (data, type, row) {
				        	rowElement = '<img class="" src="/theme-falcon/repse247/img/pdf26.png" alt="" style="cursor: pointer;" title="Ver PDF" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="generaPDFPedimento(\'' + row.NUM_PEDIMENTO + '\');" />';
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
		
		tablaDetalle.on( 'draw', function () {
			 $('[data-toggle="tooltip"]').tooltip();
		} );
			
	}); 
	
	// Aqui se agrega los filtros del encabezado
	$('#tablaDetalle thead tr:eq(1) th').each( function (i) {
		 var title = $(this).text();
		 if (i == 19){
		 } else {
			 $(this).html( '<input type="text" class="form-control form-control-sm" placeholder="'+title+'" style="width: 100%;" />' );	 
		 }
	});
	
	$('#tablaDetalle thead tr:eq(1) th').on('keyup', "input",function () {
		filtraDatos($(this).parent().index(), this.value);
	});
		
	function filtraDatos(columna, texto) {
		tablaDetalle
			.column(columna)
	        .search(texto)
	        .draw();
	}





	function abreModal(opcion, id) 
	{
			
		switch(opcion) {
		case "nuevo" : 
			iniciaFormCatalogo();
			$('#myModalDetalle').modal('show');
			break;			
		  default :
		}
	}

	
	function iniciaFormCatalogo(){
		/* Reset al Formulario */ 
		$("#frmCargaPedimentos").find('.has-success').removeClass("has-success");
	    $("#frmCargaPedimentos").find('.has-error').removeClass("has-error");
		$('#frmCargaPedimentos')[0].reset(); 
		$('#frmCargaPedimentos').removeClass("was-validated"); 
		   
	}
	
	
	function procesarPedimentos(){
				try{
					  $( "#btnSometer" ).prop( "disabled", true );
			            var formData = new FormData(document.getElementById("frmCargaPedimentos"));
			            formData.append("dato", "valor");

			            $.ajax({
			            	url: '/siarex247/cumplimientoFiscal/pedimentos/iniciaCargaPedimentos.action',
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
			            }).done(function(data){ //console.log('DATA AGREGA: ', data);
				            if (data.ESTATUS == 'OK') {
			 					Swal.fire({
			 						  icon: 'success',
			 						  title: MSG_OPERACION_EXITOSA_MENU,
			 						  html: '<p>'+MSG_ALTA_EXITOSA_MENU+'</p>',
			 						  showCancelButton: false,
			 						 confirmButtonText: BTN_ACEPTAR_MENU,
			 						  denyButtonText: BTN_CANCELAR_MENU,
			 						}).then((result) => {
			 						  if (result.isConfirmed) {
			 							 $('#myModalDetalle').modal('hide');
			  	 						 $('#tablaDetalle').DataTable().ajax.reload(null,false);
			 						  }
			 					});
							} else if (data.ESTATUS == 'ERROR') {
								 Swal.fire({
			 						  icon: 'success',
			 						  title: MSG_ERROR_OPERACION_MENU,
			 						  html: '<p>'+data.MENSAJE+'</p>',	
			 						  showCancelButton: false,
			 						 confirmButtonText: BTN_ACEPTAR_MENU,
			 						  denyButtonText: BTN_CANCELAR_MENU,
			 						}).then((result) => {
			 						  if (result.isConfirmed) {
			 							 $('#myModalDetalle').modal('hide');
			  	 						 $('#tablaDetalle').DataTable().ajax.reload(null,false);
			 						  }
			 					});
								  
								  
							} else {
							  Swal.fire({
								  icon: 'error',
								  title: '¡Error en Operacion!',
								  text: 'Error en Carga'
							  });
							}
			            });
				}
				catch(e){
					e = null;
				}
	}
	
	
	function obtenerFechaInicial(){
		return $('#fechaInicial').val();
	}
	
	function obtenerFechaFinal(){
		var fechaFinal = $('#fechaFinal').val();
		return fechaFinal;
	}

	
	function refrescar(){
		$('#tablaDetalle').DataTable().ajax.reload(null,false);
	}
	
	
	function generaPDFPedimento(NUM_PEDIMENTO){
		 document.getElementById('idRegistroP').value = NUM_PEDIMENTO;
		 document.getElementById('tipoArchivoP').value = 'PDF';
	     document.frmPedimentos.submit();
	}

	
	function descargaPedimentos(){
	      try{
	         // var idRegistro = getElementos();
	    	  var idRegistro = getElementosFilter();
	          var bandSelecciono = 'TRUE';
	          if (idRegistro == ''){
	        	  var noPedimento  = '';//obtenerPedimento();
	    		  var fechaInicial = '';//obtenerFechaInicial();
	    		  var fechaFinal = '';//obtenerFechaFinal();
	               // window.open('/siarex247/excel/descargarPedimento.action?bandSelecciono=FALSE','excel');
	    		  // bandSelecciono = 'FALSE';
	    		  idRegistro = getElementos();
	          }
	          else{
	              // window.open('/siarex247/excel/descargarPedimento.action?bandSelecciono=TRUE&idRegistro='+idRegistro,'excel');
	          }
	    	 document.getElementById('bandSelecciono').value = bandSelecciono;
	 		 document.getElementById('idRegistro').value = idRegistro;
	 	     document.frmDescargaPedimentos.submit();
	 	     
	      }
	      catch(e){
	          alert('descargaPedimentos(): ' + e);
	      }
		}
	



	 function getElementosFilter(){
		 var llavesUUID = '';
		 try{
			 var dataSelect = tablaDetalle.rows('.selected').data(); 
				$.each(dataSelect, function(key, row) {
					llavesUUID+= row.NUM_PEDIMENTO + ";"		
			    });

		 }catch(e){
			 alert('getElementosFilter()_'+e);
		 }
		 return llavesUUID;
	 }
	 
	
	
	 function getElementos(){
			var llaveRegistros = '';
			try{
				 var table = $('#tablaDetalle').DataTable();
				 var bandPrimero = true;
				 table.column(0, { search:'applied' }).data().each(function(value, index) {
					 if (bandPrimero){
						 llaveRegistros = value;
					 }else{
						 llaveRegistros = llaveRegistros +  ";"  + value;
					 }
					 bandPrimero = false;
				 });
				 
			}catch(e){
				alert('balanceTotales()_'+e);
			}
			return llaveRegistros;
		}	
	 