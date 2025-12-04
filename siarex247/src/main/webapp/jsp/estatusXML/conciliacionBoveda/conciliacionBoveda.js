

var tablaDetalle = null;

	$(document).ready(function() {
		try {
			var buttonCommon = {
				    exportOptions: {
				        format: {
				            body: function ( data, row, column, node ) {
								// alert('column ==> '+column);
								if (column == 5 || column == 6 || column == 7 || column == 8 || column == 9 || column == 13 || column == 14 ){ // subTotal
									const arrDatos = data.split(",");
									debugger;
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
				    		// { extend: "excel", text:'Exportar Excel',  title: 'Conciliacion Complementos de Pago', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'conciliacionComplementos'},
							$.extend(!0, {}, buttonCommon,
								{ extend: "excel", text:'Exportar Excel',  title: 'Conciliacion Complementos de Pago', exportOptions: {modifier: {selected: null}}, filename: 'conciliacionComplementos'},
							),
													 
				    		// { extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Conciliacion Complementos de Pago", filename: 'conciliacionComplementos', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/cumplimientoFiscal/conciliacion/boveda/detalleConsiliados.action',
					type: 'POST',
					data : {
						anio : function() { return obtenerAnio(); },
						mesCombo : function() { return obtenerMes(); },
						tipoComple : function() { return obtenerComp(); }
					}
				},
				aoColumns : [//, "sClass": "alinearDerecha"
					{ mData: "rfc"},
					{ mData: "razonSocial"},
					{ mData: "tipoMoneda", "sClass": "alinearCentro"},
					{ mData: "serieFolio", "sClass": "alinearCentro"},
					{ mData: "uuidOrden", "sClass": "alinearCentro"},
					{ mData: "subTotal", "sClass": "alinearDerecha"},
					/*
					{ mData: 'subTotal', "sClass": "alinearDerecha", render: function (data, type, row) {
	                        const cadenaObjecto = new String(row.subTotal);
	                        const negarivo = cadenaObjecto.split("-");
	                        const longitud = cadenaObjecto.length;
	                        if(longitud==3){
	                            var data ="0" +  row.subTotal
	                             return data;
	                        }else if(longitud==4 && negarivo[0]==""){
	                             var data ="-0" +  negarivo[1]
	                              return data;
	                         }else{
	                            var data =  row.subTotal;
	                             return data;
	                        }
					                      
					} },
					*/
									
					{ mData: "iva", "sClass": "alinearDerecha"},
					{ mData: "ivaRet", "sClass": "alinearDerecha"},
					{ mData: "impLocales", "sClass": "alinearDerecha"},
					{ mData: "total", "sClass": "alinearDerecha"},
					{ mData: "fechaPago", "sClass": "alinearCentro"},
					{ mData: "fechaTimbrado", "sClass": "alinearCentro"},
					{ mData: "uuidComplemento"},
					{ mData: "totalFactura", "sClass": "alinearDerecha"},
					{ mData: "totalComplemento", "sClass": "alinearDerecha"},
					{ mData: "estatusComplemento"},
					{ mData: null, "sClass": "alinearCentro"},
					{ mData: null, "sClass": "alinearCentro"},
					{ mData: "estatusConciliacion"}
				],
				columnDefs: [
					{
	                    targets: 16,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	//alert('NOMBRE_XML==>'+data.NOMBRE_XML+', NOMBRE_PDF===>'+data.NOMBRE_PDF+',pillo');
	                    	if (data.nombreXML == ''){
	                    		
	                    	}else{
	                    		rowElement = '<img class="" src="/theme-falcon/repse247/img/xml26.png" alt="" style="cursor: pointer;" title="Ver XML" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo XML" onclick="generaXML_Boveda( \'' + row.uuidComplemento + '\');" />';	
	                    	}
	                    	
	                        return rowElement;
	                      }
	                  },
	                  {
		                 targets: 17,
	                    render: function (data, type, row) {
	                    	rowElement = '';
	                    	if (data.nombrePDF == ''){
	                    		
	                    	}else{
	                    		rowElement = '<img class="" src="/theme-falcon/repse247/img/pdf26.png" alt="" style="cursor: pointer;" title="Ver PDF" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="generaPDF_Boveda( \'' + row.uuidComplemento + '\');" />';	
	                    	}
	                    	
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
		 if (i == -1){
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



	function generaPDF_Boveda(UUID_COMPLEMENTO){
		 document.getElementById('idRegistro').value = UUID_COMPLEMENTO;
		 document.getElementById('tipoArchivo').value = 'PDF';
	     document.frmAbrirBoveda.submit();
	 }
	  
	  function generaXML_Boveda(UUID_COMPLEMENTO){
		  document.getElementById('idRegistro').value = UUID_COMPLEMENTO;
		   document.getElementById('tipoArchivo').value = 'XML';
	       document.frmAbrirBoveda.submit();
	 }

	  
	  
	  

		 function cargaAnnios() {
				try {
					var annioActual = $('#annioActual').val();
					$('#anio').empty();
					$.ajax({
			           url:  '/siarex247/cumplimientoFiscal/conciliacion/boveda/comboAnnios.action',
			           type: 'POST',
			            dataType : 'json',
					    success: function(data){
					    	$('#anio').empty();
					    	$.each(data.data, function(key, text) {
						    	if (annioActual == text.annio){
						    		$('#anio').append($('<option></option>').attr('selected', 'selected').attr('value', text.annio).text(text.annio));
						    	}else{
						    		$('#anio').append($('<option></option>').attr('value', text.annio).text(text.annio));
						    	}
					    		
					      	});
					    }
					});
				}catch(e) {
					alert("cargaAnnios()_"+e);
				} 
			}
		 
		 
		 
		 function cargaCmbMes(){
				try {
					var mesActual = $('#mesActual').val();
					$('#cmbMes').empty();
					// $('#cmbMes').append($('<option></option>').attr('value', 'NONE').text('Seleccione mes'));
					$('#cmbMes').append($('<option></option>').attr('value', 'NONE').text(LABEL_SELECCIONE_MES_MENU));
					
					if (mesActual == '01'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '01').text(LABEL_ENERO_MENU));	
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '01').text(LABEL_ENERO_MENU));
					}
					if (mesActual == '02'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '02').text(LABEL_FEBRERO_MENU));	
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '02').text(LABEL_FEBRERO_MENU));
					}
					if (mesActual == '03'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '03').text(LABEL_MARZO_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '03').text(LABEL_MARZO_MENU));	
					}
					if (mesActual == '04'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '04').text(LABEL_ABRIL_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '04').text(LABEL_ABRIL_MENU));	
					}
					if (mesActual == '05'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '05').text(LABEL_MAYO_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '05').text(LABEL_MAYO_MENU));	
					}
					if (mesActual == '06'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '06').text(LABEL_JUNIO_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '06').text(LABEL_JUNIO_MENU));	
					}
					if (mesActual == '07'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '07').text(LABEL_JULIO_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '07').text(LABEL_JULIO_MENU));	
					}
					if (mesActual == '08'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '08').text(LABEL_AGOSTO_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '08').text(LABEL_AGOSTO_MENU));	
					}
					if (mesActual == '09'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '09').text(LABEL_SEPTIEMBRE_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '09').text(LABEL_SEPTIEMBRE_MENU));	
					}
					if (mesActual == '10'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '10').text(LABEL_OCTUBRE_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '10').text(LABEL_OCTUBRE_MENU));	
					}
					if (mesActual == '11'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '11').text(LABEL_NOVIEMBRE_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '11').text(LABEL_NOVIEMBRE_MENU));	
					}
					if (mesActual == '12'){
						$('#cmbMes').append($('<option></option>').attr('selected', 'selected').attr('value', '12').text(LABEL_DICIEMBRE_MENU));
					}else{
						$('#cmbMes').append($('<option></option>').attr('value', '12').text(LABEL_DICIEMBRE_MENU));	
					}
					
					
				}
				catch(e) {
					e = null;
				}
			}
		
		function cargaCmbComplemento(){
			try {
				$('#cmbComple').empty();
				$('#cmbComple').append($('<option></option>').attr('selected', 'selected').attr('value', 'CON_SIN_COMPLE').text('CON O SIN COMPLEMENTO'));
				$('#cmbComple').append($('<option></option>').attr('value', 'CON_COMPLE').text('CON COMPLEMENTO'));
				$('#cmbComple').append($('<option></option>').attr('value', 'SIN_COMPLE').text('SIN COMPLEMENTO'));
			}
			catch(e) {
				e = null;
			}
		}
		
		function buscarConsiliacion(){
			try {
				$('#tablaDetalle').DataTable().ajax.reload(null,false);
			}
			catch(e) {
				e = null;
			}
		}
		
		
		
		  function exportarConsiliados(){
		      try{
		          var anio = obtenerAnio();
		          var razonSocial  = obtenerRS();
		          var mes = obtenerMes();
		          var tipoComple = obtenerComp();
		          var idRegistro = "0"; 
		          if (idRegistro == ""){
		              idRegistro = "0";
		          }
		          window.open('/siarex247/excel/exportComplementosZIP.action?idRegistro='+idRegistro+'&anio='+anio+'&razonSocial='+razonSocial+'&mesCombo='+mes+'&tipoComple='+tipoComple,'excel');  
		      }catch(e){
		          alert('exportarConsiliados()_'+e);
		      }
		  }
		  
		  
		function obtenerAnio(){
			//annioActual
			var annio = $('#anio').val();
			if (annio == null){
				annio = $('#annioActual').val();
			}
			// alert('annio===>'+annio);
			return annio;
		}
		
		function obtenerRS(){
			return $('#razonSocial').val();
		}
		
		function obtenerMes(){
			return $('#cmbMes').val();
		}
		
		function obtenerComp(){
			return $('#cmbComple').val();
		}

		
		
