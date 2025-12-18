var tablaDetalle = null;

	$(document).ready(function() {
		try {
			
			var buttonCommon = {
							    exportOptions: {
							        format: {
							            body: function ( data, row, column, node ) {
											// alert('column ==> '+column);
											if (column == 5 || column == 6 || column == 9 ){ // subTotal
												const arrDatos = data.split(",");
												if (arrDatos.length >= 2){
													// return 100;
													var strNumero = '';
													for (var x = 0; x < arrDatos.length; x++){
														strNumero+=arrDatos[x];
													}
													return strNumero;
												}else{
													return data;
												}
												
												
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
				select      : false,
				stateSave	: false, 
				order       : [ [ 0, 'asc' ] ],	
				
				buttons: [
					{ 	extend: "collection",
				    	autoClose : true,  
				    	text:'<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
				    	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    	buttons: [
							$.extend(!0, {}, buttonCommon,
								{ extend: "excel", text:'Exportar Excel',  title: 'Detalle de Validacion XML', exportOptions: {modifier: {selected: null}}, filename: 'detalleXML'},
							),
				    		// { extend: "excel", text:'Exportar Excel',  title: 'Detalle de Carga Masiva de XML', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'detalleXML'},
				    		// { extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Detalle de Carga Masiva de XML", filename: 'detalleXML', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/cumplimientoFiscal/validarXML/listaDetalle.action',
					data : {
						tipoGrupo: function() { return obtenerGPO(); }
					},
					type: 'POST'
				},
				aoColumns : [
					{ mData: "RFC", "sClass": "alinearCentro"},
					{ mData: "RAZON_SOCIAL"},
					{ mData: "TIPO_MONEDA", "sClass": "alinearCentro"},
					{ mData: "SERIE"},
					{ mData: "FOLIO"},
					{ mData: "TIPO_COMPROBANTE", "sClass": "alinearCentro" }, // Nueva definición
					{ mData: "SUBTOTAL", "sClass": "alinearDerecha"},
					{ mData: "TOTAL", "sClass": "alinearDerecha"},
					{ mData: "ESTADO_SAT"},
					{ mData: "ESTATUS_SAT"},
					{ mData: "GRAND_TOTAL", "sClass": "alinearDerecha"}
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
		$("#validarFacturasLbl").html("NO");
		$("#frm-Carga-XML").find('.has-success').removeClass("has-success");
	    $("#frm-Carga-XML").find('.has-error').removeClass("has-error");
		$('#frm-Carga-XML')[0].reset(); 
		$('#frm-Carga-XML').removeClass("was-validated"); 
		   
	}
	
	
	function procesaValidar(){
			
			try{
				$("#btnSometer").prop('disabled', true);
		            var formData = new FormData(document.getElementById("frm-Carga-XML"));
		            formData.append("dato", "valor");

		            $.ajax({
		            	url: '/siarex247/cumplimientoFiscal/validarXML/iniciaCargaXML.action',
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
		            }).done(function(data){//console.log('DATA AGREGA: ', data);
		            	
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
	  	 						   $('#tablaDetalle').DataTable().ajax.reload(null,false);
	 						    }
	 					   });	  
		            	/*
		              if (data.codError == '000') {
	 					Swal.fire({
	 						  icon: 'success',
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						  text: data.mensajeError,
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
						  text: data.mensajeError
					  });
					}
		            */
		            
		        });
			}
			catch(e){
				e = null;
			}
	}
	
	
	
	function obtenerGPO(){
		var grupo = $('#cmbGrupos').val() == null ? 'GPO_IDV' : $('#cmbGrupos').val();
		return grupo;
	}

	
	function recargarDetalle(){
		try{
			$('#tablaDetalle').DataTable().ajax.reload(null,false);
		}catch(e){
			alert('recargarDetalle()_'+e);
		}
	}
	