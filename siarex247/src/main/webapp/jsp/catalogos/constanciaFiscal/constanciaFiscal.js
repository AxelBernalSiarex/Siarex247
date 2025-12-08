
var tablaDetalle = null;

	$(document).ready(function() {
		try {
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
					/*{ 	text: '<div id="btnNuevo_Catalogo"> </div>',
						className: 'btn btn-primary me-1 mb-1 btn-sm btnPanel',
		                action: function ( e, dt, node, config ) {
		                    abreModal('nuevo', 0);
		                },
					},
					*/
			      	{ 	extend: "collection",
				    	autoClose : true,  
				    	text:'<span class="fas fa-external-link-alt" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Export</span>',
				    	className: 'btn btn-outline-secondary me-1 mb-1 btn-sm btnClr',
				    	buttons: [
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Constancia de Situación Fiscal', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'CatalogoConstancias'}
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
					url: '/siarex247/catalogos/constanciaSituacion/detalleConstancias.action',
					type: 'POST'
				},
				
				aoColumns : [
					{ mData: null, "sClass": "alinearCentro"},
					{ mData: "cedulaFiscal"},
					{ mData: "rfc"},
					{ mData: "razonSocial"},
					{ mData: "regimenCapital"},
					{ mData: "nombreEmpleado"},
					{ mData: "apellidoPaterno"},
					{ mData: "apellidoMaterno"},
					{ mData: "fechaOperaciones", "sClass": "alinearCentro"},
					{ mData: "situacionContribuyente"},
					{ mData: "fechaUltCambioSituacion"},
					{ mData: "entidadFederativa"},
					{ mData: "municipio"},
					{ mData: "colonia"},
					{ mData: "tipoVialidad"},
					{ mData: "nombreVialidad"},
					{ mData: "numeroExt"},
					{ mData: "numeroInt"},
					{ mData: "codigoPostal", "sClass": "alinearCentro"},
					{ mData: "correoElectronico"},
					{ mData: "regimen"},
					{ mData: "fechaAlta"}
				],
				columnDefs: [
					 {
						targets: 1,
						render: function (data, type, row) {
							rowElement = '<a href="javascript:abrirCedula(\'' + row.idRegistro + '\', \'' + row.cedulaFiscal + '\');">' + row.cedulaFiscal + '</a>';
							// rowElement = '<img class="" src="/theme-falcon/repse247/img/pdf26.png" alt="" style="cursor: pointer;" title="Ver PDF" data-bs-toggle="tooltip" data-bs-placement="top" title="Archivo PDF" onclick="generarCertificado(1, \'' + row.idRegistro+ '\', \'' + row.cedulaFiscal+ '\');" />';	
							return rowElement;
						}
				      },
					  {
	  				  targets: 2,
	  					render: function (data, type, row) {
	  						rowElement = '<lable ondblclick="abreModal(\'editar\', \'' + row.idRegistro + '\');">'+row.rfc+' </label>'
	  						return rowElement;
	  					 }
	  				  },
				      {
							targets: 3,
							render: function (data, type, row) {
								rowElement = '<lable ondblclick="abreModal(\'editar\', \'' + row.idRegistro + '\');">'+row.razonSocial+' </label>'
								return rowElement;
							}
					  },
					      
				      
				   {
				        targets: 0,
				        render: function (data, type, row) {
				        	 
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" style="z-index: 2;" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton" style="z-index: 3;" >';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'ver\', \'' + row.idRegistro + '\');">'+BTN_VER_MENU+'</a>';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.idRegistro + '\');">'+BTN_EDITAR_MENU+'</a>';
				            rowElement += '<div class="dropdown-divider"></div>';
				            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaCatalogo(\'' + row.idRegistro + '\', \'' + row.rfc + '\');">'+BTN_ELIMINAR_MENU+'</a>';
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
		              
		              var CAT_CONSTANCIA_SITUACION_ETQ3 = $('#CAT_CONSTANCIA_SITUACION_ETQ3');
		              CAT_CONSTANCIA_SITUACION_ETQ3.removeClass('alinearCentro');

		              var CAT_CONSTANCIA_SITUACION_ETQ4 = $('#CAT_CONSTANCIA_SITUACION_ETQ4');
		              CAT_CONSTANCIA_SITUACION_ETQ4.removeClass('alinearCentro');

		              
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
		 if (i == 0){
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


	 function abreModal(opcion, id){
		
		try{
			switch(opcion) {
			case "nuevo" : 
				$("#idEmpleado").prop('disabled', false);
				$('#idRegistro_Catalogo').val(0);
				iniciaFormCatalogo();
				buscaCatalogo(0, 'nuevo');
				document.getElementById("modal-title-catalogo").innerHTML = TITLE_NEW_CATALOGO;
				$('#myModalDetalle').modal('show');
				break;			
			case "editar" :
				$('#idRegistro_Catalogo').val(id);
				document.getElementById("modal-title-catalogo").innerHTML = TITLE_EDIT_CATALOGO;
				iniciaFormCatalogo();
				buscaCatalogo(id, 'editar');
				$('#myModalDetalle').modal('show');
				break;
			case "ver" :
				$('#idRegistro_Catalogo').val(id);
				document.getElementById("modal-title-catalogo").innerHTML = TITLE_VIEW_CATALOGO;
				//iniciaFormCatalogo();
				buscaCatalogo(id, 'ver');
				$('#myModalDetalle').modal('show');
				break;
			case "cfdi" : 
				iniciaFormCatalogoCargar();
				$('#myModalCargaCFDI').modal('show');
				break;
			default :
			}
		}catch(e){
			alert('abreModal()_'+e);
		}
	 }

	 

		function iniciaFormCatalogo(){
			
			$('#regimenFiscal').select2({
				dropdownParent: $('#myModalDetalle .modal-body'),
				theme: 'bootstrap-5'
			});
			$('#regimenFiscal').trigger('change'); //Refresca el combo
			
			$("#form-Catalogo-Constancia").find('.has-success').removeClass("has-success");
		    $("#form-Catalogo-Constancia").find('.has-error').removeClass("has-error");
			$('#form-Catalogo-Constancia')[0].reset(); 
			$('#form-Catalogo-Constancia').removeClass("was-validated"); 
			   
		}
		

	function iniciaFormCatalogoCargar(){
		$("#form-Cargar-CFDI").find('.has-success').removeClass("has-success");
	    $("#form-Cargar-CFDI").find('.has-error').removeClass("has-error");
		$('#form-Cargar-CFDI')[0].reset(); 
		$('#form-Cargar-CFDI').removeClass("was-validated"); 
		   
	}
	

	function eliminaCatalogo(idRegistro, rfc){
		try{
			
			Swal.fire({
				  icon : 'question',
				 // title: '¿Estas seguro de eliminar la Constancia de Situación Fiscal ?',
				  title: TITLE_DELETE_CATALOGO,
				 // text: 'RFC : '+rfc,
				  text: LABEL_CONSTANCIAS + ' : '+rfc,
				  showDenyButton: true,
				  showCancelButton: false,
				  confirmButtonText: BTN_ACEPTAR_MENU,
				  denyButtonText: BTN_CANCELAR_MENU,
				}).then((result) => {
				  if (result.isConfirmed) {
					  $.ajax({
				           url:  '/siarex247/catalogos/constanciaSituacion/eliminarConstancia.action',
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
			 							$('#tablaDetalle').DataTable().ajax.reload(null,false);
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
	

	function buscaCatalogo(idReg, accion){
		try{
			
			
			$.ajax({
				url  : '/siarex247/catalogos/constanciaSituacion/consultaConstancia.action',
				type : 'POST', 
				data : {
					idRegistro : idReg
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						// 
						// 
						
						if (accion == 'ver' || accion == 'nuevo'){
							 const formCatalogo = document.getElementById("form-Catalogo-Constancia");
						     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
						     for(i=0;i<validarHabilitar.length;i++){
						    	 var idElemento = validarHabilitar[i].id;
						    	 $('#'+idElemento).attr('disabled', true);
						     	
						     }
						}else{
							const formCatalogo = document.getElementById("form-Catalogo-Constancia");
						     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
						     for(i=0;i<validarHabilitar.length;i++){
						    	 var idElemento = validarHabilitar[i].id;
						    	 $('#'+idElemento).attr('disabled', false);
						     	
						     }
						}
						
						$('#idRegistro_Catalogo').val(idReg);
						$('#cedulaFiscal_Catalogo').val('');
						
						
						if (accion == 'nuevo'){
							$('#filePDF').attr('disabled', false);
						}else{
							$('#cedulaFiscal_Catalogo').val(data.cedulaFiscal);
							$('#rfc').val(data.rfc);
							$('#razonSocial').val(data.razonSocial);
							$('#regimenCapital').val(data.regimenCapital);
							// $('#fechaNacimiento').val(data.fechaNacimiento);
							$('#nombre').val(data.nombreEmpleado);
							$('#apellidoPaterno').val(data.apellidoPaterno);
							$('#apellidoMaterno').val(data.apellidoMaterno);
							$('#situacionContribuyente').val(data.situacionContribuyente);
							$('#curp').val(data.curp);
							$('#fechaUltimoCambio').val(data.fechaUltCambioSituacion);
							$('#estado').val(data.entidadFederativa);
							$('#ciudad').val(data.municipio);
							$('#colonia').val(data.colonia);
							$('#tipoVialidad').val(data.tipoVialidad);
							$('#nombreVialidad').val(data.nombreVialidad);
							$('#fechaInicioOperaciones').val(data.fechaOperaciones);
							$('#numExterior').val(data.numeroExt);
							$('#numInterior').val(data.numeroInt);
							$('#codigoPostal').val(data.codigoPostal);
							$('#correo').val(data.correoElectronico);
							$('#fechaAlta').val(data.fechaAlta);
							// $('#regimenFiscal').val(data.rfc);
							cargaRegimenFiscal(data.claveRegimen);
						}
						
						if (accion == 'ver'){
							$( "#btnSometer" ).hide();
						}else{
							$( "#btnSometer" ).show();
						}
						
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('buscaCatalogo()_'+thrownError);
				}
			});	
			
			
		}catch(e){
			alert('buscaCatalogo()_'+e);
		}
		
    }
	
	

	 function guardarCatalogo(nuevaConstancia){
		try{
			var formData = new FormData(document.getElementById("form-Catalogo-Constancia"));
			formData.append("tipoAccion", nuevaConstancia);
	        var URL = '/siarex247/catalogos/constanciaSituacion/altaCedulaFiscal.action';
	        
	        $.ajax({
				url  : URL,
				type : 'POST', 
				dataType : 'json',
				beforeSend: function( xhr ) {
	    			$('#overSeccion').css({display:'block'});
	    			$( "#btnSometer" ).prop( "disabled", true );
	    			
	    		},
				data: formData,
				cache: false,
	            contentType: false,
	    		processData: false,
	    		complete: function(jqXHR, textStatus){
					$('#overSeccion').css({display:'none'});		
					$( "#btnSometer" ).prop( "disabled", false );
	    		},
				success  : function(data) {
					
					if (data.codError == '002'){ // es nueva
						Swal.fire({
							  icon : 'question',
							  title: '¿ Desea agregar esta constancia como nueva ?',
							 // title: TITLE_DELETE_CATALOGO,
							  text: data.mensajeError,
							  showDenyButton: true,
							  showCancelButton: false,
							  confirmButtonText: BTN_ACEPTAR_MENU,
							  denyButtonText: BTN_CANCELAR_MENU,
							}).then((result) => {
							  if (result.isConfirmed) {
								 guardarCatalogo('nueva');
							  } else if (result.isDenied) {
								$('#tablaDetalle').DataTable().ajax.reload(null,false);
								$('#myModalDetalle').modal('hide');
							  }
							})
					}else if (data.codError == '003') {
						Swal.fire({
			                title: '¡Error en Operacion!',
			                html: '<p>'+data.mensajeError+'</p>',	
			                icon: 'error'
			            });
					}else if (data.codError == '000') {
						Swal.fire({
	 						 title: MSG_OPERACION_EXITOSA_MENU,
	 						 html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
								  showCancelButton: false,
								  confirmButtonText: 'Aceptar',
								  icon: 'success'
	 						}).then((result) => {
	 							$('#tablaDetalle').DataTable().ajax.reload(null,false);
	 							$('#myModalDetalle').modal('hide');
	 						});
					} else {
						Swal.fire({
				                title: '¡Error en Operacion!',
				                html: '<p>'+data.mensajeError+'</p>',	
				                icon: 'error'
				            });
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('Error al cargar archivo cargarNomina()...');
				}
			});
			
		}catch(e){
			alert('guardarCatalogo(): ' + e);
		}
	}
	 
	 
	 function cargarCFDI(){
			try{
				var formData = new FormData(document.getElementById("form-Cargar-CFDI"));
		        formData.append("dato", "valor");
		        formData.append("nuevaVersion", "true");
		        var URL = '/siarex247/catalogos/constanciaSituacion/guardarCedulaFiscal.action';
		        
		        $.ajax({
					url  : URL,
					type : 'POST', 
					dataType : 'json',
					beforeSend: function( xhr ) {
		    			$('#overSeccionNomina').css({display:'block'});
		    			$( "#btnSometerCargar" ).prop( "disabled", true );
		    			
		    		},
					data: formData,
					cache: false,
		            contentType: false,
		    		processData: false,
		    		complete: function(jqXHR, textStatus){
						$('#overSeccionNomina').css({display:'none'});		
						$( "#btnSometerCargar" ).prop( "disabled", false );
		    		},
					success  : function(data) {
						if (data.codError == '000') {
							Swal.fire({
		 						 title: TITLE_CARGA_EXITOSA,
		 						 html: '<p>'+data.mensajeError+'</p>',
									  showCancelButton: false,
									  confirmButtonText: 'Aceptar',
									  icon: 'success'
		 						}).then((result) => {
		 							$('#tablaDetalle').DataTable().ajax.reload(null,false);
		 							$('#myModalCargaCFDI').modal('hide');
		 						});
						} else {
							Swal.fire({
					                title: '¡Error en Operacion!',
					                html: '<p>'+data.mensajeError+'</p>',	
					                icon: 'error'
					            });
						}
					},
					error : function(xhr, ajaxOptions, thrownError) {
						alert('Error al cargar archivo cargarNomina()...');
					}
				});
				
			}catch(e){
				alert('cargarCFDI(): ' + e);
			}
		}
	 
		function abrirCedula(idRegistro, cedulaFiscal){
			  try{
		         document.getElementById('cedulaFiscal_PDF').value = cedulaFiscal;
		         document.getElementById('idRegistro_PDF').value = idRegistro;
		         document.frmExportarCedula.submit();
			  }
			  catch(e){
				  alert('abrirCedula()_'+e);
			  }
			}

		
	