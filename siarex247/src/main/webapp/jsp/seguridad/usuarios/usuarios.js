


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
					{ 	text: '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Nuevo</span>',
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Detalle Usuarios', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'CatalogoUsuarios'},
				    		{ extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Detalle Usuarios", filename: 'CatalogoUsuarios', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/seguridad/usuarios/listaUsuarios.action',
					type: 'POST'
				},
				aoColumns : [
					{ mData: null },
					{ mData: "idRegistro", "sClass": "alinearCentro"},
					{ mData: "idUsuario"},
					{ mData: "nombreCompleto"},
					{ mData: "idEmpleado"},
					{ mData: "correo"},
					{ mData: "idPerfil", "sClass": "alinearCentro"},
					{ mData: "desPerfil"},
					{ mData: "tipoPerfil", "sClass": "alinearCentro"}
					
				],
				columnDefs: [
					{	targets: 2,
				        render: function (data, type, row) {
				        	rowElement = '<a href="javascript:abreModal(\'ver\', \'' + row.idRegistro + '\');">' + row.idUsuario + '</a>';
				            return rowElement;
				        }
				    },
				   {
				        targets: 0,
				        render: function (data, type, row) {
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'ver\', \'' + row.idRegistro + '\');">Ver</a>';
				            rowElement += '<a class="dropdown-item" href="javascript:abreModal(\'editar\', \'' + row.idRegistro + '\');">Editar</a>';
				            rowElement += '<div class="dropdown-divider"></div>';
				            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaCatalogo(\'' + row.idRegistro + '\', \'' + row.idUsuario + '\', \'' + row.nombreCompleto + '\');">Eliminar</a>';
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



	function abreModal(opcion, id) 
	{
			
		switch(opcion) {
		case "nuevo" : 
			$( "#btnSometer" ).show();
			$('#idRegistro_Catalogo').val(0);
			iniciaFormCatalogo();
			document.getElementById("modal-title-catalogo").innerHTML = "Nuevo Usuario";
			cargaPerfiles('');
			$('#myModalDetalle').modal('show');
			break;			
		case "editar" :
			$('#idRegistro_Catalogo').val(id);
			document.getElementById("modal-title-catalogo").innerHTML = "Editar Usuario";
			iniciaFormCatalogo();
			buscaCatalogo(id, 'editar');
			$('#myModalDetalle').modal('show');
			break;
		case "ver" :
			$('#idRegistro_Catalogo').val(id);
			document.getElementById("modal-title-catalogo").innerHTML = "Ver Usuario";
			//iniciaFormCatalogo();
			buscaCatalogo(id, 'ver');
			$('#myModalDetalle').modal('show');
			break;
		default :
		}
		
	}

	
	

	function iniciaFormCatalogo(){
		$('#cmbIdPerfil').select2({
			dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		$('#cmbIdPerfil').val('0'); // Selecciona primer valor del combo
		$('#cmbIdPerfil').trigger('change'); //Refresca el combo
		
		/* Reset al Formulario */ 
		$("#form-Catalogo").find('.has-success').removeClass("has-success");
	    $("#form-Catalogo").find('.has-error').removeClass("has-error");
		$('#form-Catalogo')[0].reset(); 
		$('#form-Catalogo').removeClass("was-validated"); 
		   
	}
	

	
	function buscaCatalogo(idReg, accion){
		$.ajax({
			url  : '/siarex247/seguridad/usuarios/consultaUsuarios.action',
			type : 'POST', 
			data : {
				idRegistro : idReg
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					if (accion == 'ver'){
						 const formCatalogo = document.getElementById("form-Catalogo");
					     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
					     for(i=0;i<validarHabilitar.length;i++){
					    	 var idElemento = validarHabilitar[i].id;
					    	 $('#'+idElemento).attr('disabled', true);
					     	
					     }
					}else{
						const formCatalogo = document.getElementById("form-Catalogo");
					     const validarHabilitar = formCatalogo.getElementsByClassName("validarHabilitar"); // a list of matching elements, *not* the element itself
					     for(i=0;i<validarHabilitar.length;i++){
					    	 var idElemento = validarHabilitar[i].id;
					    	 $('#'+idElemento).attr('disabled', false);
					     	
					     }
					}
					
					
					$('#idRegistro_Catalogo').val(data.idRegistro);
					$('#txtIdUsuario').val(data.idUsuario);
					$('#txtIdEmpleado').val(data.idEmpleado);
					$('#txtNombreCompleto').val(data.nombreCompleto);
					$('#txtCorreo').val(data.correo);
					cargaPerfiles(data.idPerfil);
					
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
		
    }
	
	

	function eliminaCatalogo(idReg, idUsuario, nombreCompleto){
		try{
			
			Swal.fire({
				  icon : 'warning',
				  title: '¿Estas seguro de eliminar Usuario ?',
				  text: 'Nombre : '+nombreCompleto,
				  showDenyButton: true,
				  showCancelButton: false,
				  confirmButtonText: 'Aceptar',
				  denyButtonText: 'Cancelar',
				}).then((result) => {
				  if (result.isConfirmed) {
					  $.ajax({
				           url:  '/siarex247/seguridad/usuarios/eliminaUsuarios.action',
				           type: 'POST',
				            dataType : 'json',
				            data : {
				            	idRegistro : idReg,
		      	 				idUsuario : idUsuario
				            },
				            	
						    success: function(data){
						    	
						    	if (data.codError == "000"){
						    		Swal.fire({
			 						  title: '¡Operación Exitosa!',
			 						 html: '<p>Registro Borrado. </p>',
										  showCancelButton: false,
										  confirmButtonText: 'Aceptar',
										  icon: 'success'
			 						}).then((result) => {
			 							$('#tablaDetalle').DataTable().ajax.reload(null,false);
			 						});
						    	}else{
						    		Swal.fire({
		  			                title: '¡Operación No Existosa!',
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
	
	
	function guardarCatalogo(){
		try{
			var idUsuario = $('#txtIdUsuario').val();
			var idEmpleado = $('#txtIdEmpleado').val();
			var nombreCompleto = $('#txtNombreCompleto').val();
			var correo = $('#txtCorreo').val();
			var idPerfil = $('#cmbIdPerfil').val();
			
	 		$.ajax({
				url  : '/siarex247/seguridad/usuarios/altaUsuarios.action',
				type : 'POST', 
				data : {
					idUsuario : idUsuario,
					idEmpleado : idEmpleado,
					nombreCompleto : nombreCompleto,
					correo : correo,
					idPerfil : idPerfil
					
				},
				dataType : 'json',
				success  : function(data) {
	 				if (data.codError == '000') {
	 					Swal.fire({
	 						  title: '¡Operación Exitosa!',
	 						 html: '<p>Alta Exitosa</p>',
								  showCancelButton: false,
								  confirmButtonText: 'Aceptar',
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalle').DataTable().ajax.reload(null,false);
	 						});
					} else {
						Swal.fire({
  			                title: '¡Operación No Existosa!',
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('guardarCatalogo()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('guardarCatalogo()_'+e);
		}
	 }
	
	
	
	function actualizarCatalogo(){
		try{
			var idReg = $('#idRegistro_Catalogo').val();
			var idUsuario = $('#txtIdUsuario').val();
			var idEmpleado = $('#txtIdEmpleado').val();
			var nombreCompleto = $('#txtNombreCompleto').val();
			var correo = $('#txtCorreo').val();
			var idPerfil = $('#cmbIdPerfil').val();
			
	 		$.ajax({
				url  : '/siarex247/seguridad/usuarios/actualizaUsuarios.action',
				type : 'POST', 
				data : {
					idRegistro : idReg,
					idUsuario : idUsuario,
					idEmpleado : idEmpleado,
					nombreCompleto : nombreCompleto,
					correo : correo,
					idPerfil : idPerfil
					
				},
				dataType : 'json',
				success  : function(data) {
	 				if (data.codError == '000') {
	 					Swal.fire({
	 						  title: '¡Operación Exitosa!',
	 						 html: '<p>Cambio Exitosa</p>',
								  showCancelButton: false,
								  confirmButtonText: 'Aceptar',
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalDetalle').modal('hide');
	  	 						$('#tablaDetalle').DataTable().ajax.reload(null,false);
	 						});
					} else {
						Swal.fire({
  			                title: '¡Operación No Existosa!',
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('actualizarCatalogo()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('actualizarCatalogo()_'+e);
		}
	 }
	