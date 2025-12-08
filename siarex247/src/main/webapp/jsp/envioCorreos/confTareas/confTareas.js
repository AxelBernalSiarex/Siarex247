
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
				order       : [ [ 0, 'desc' ] ],	
				buttons: [
					{ 	// text: '<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1">Nuevo</span>',
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
				    		{ extend: "excel", text:'Exportar Excel',  title: 'Configurador de Tareas', exportOptions: {modifier: {selected: null, page: 'all'}}, filename: 'confTareas'},
				    		{ extend: "pdf", text:'Exportar PDF', orientation: "landscape", pageSize: "LEGAL", title: "Configurador de Tareas", filename: 'confTareas', exportOptions: { modifier: { page: "all", }, }, }, 
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
					url: '/siarex247/procesos/tareas/detalleTareas.action',
					data : null,
					type: 'POST'
				},
				aoColumns : [
					{ mData: "claveTarea", "sClass": "alinearCentro"},
					{ mData: "subject"},
					{ mData: "correoDe"},
					{ mData: "tipoTarea"},
					{ mData: "fechaTarea"},
					{ mData: "tipoEnvio"},
					{ mData: "estatusDes"},
					{ mData: null, "sClass": "alinearCentro" }
				],
				columnDefs: [
				   {
				        targets: 7,
				        render: function (data, type, row) {
				            rowElement = '<div class="d-flex flex-column flex-sm-row text-center" style="width: 50px; text-align: center;">';
				            rowElement += '<button class="btn btn-falcon-default dropdown-toggle" id="dropdownMenuButton" type="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">...</button>';
				            rowElement += '<div class="dropdown-menu dropdown-menu-end py-0" aria-labelledby="dropdownMenuButton">';
				            rowElement += '<a class="dropdown-item" href="javascript:cancelarRegistro(\'' + row.claveTarea + '\', \'' + row.subject + '\');">'+BTN_CANCELAR_MENU+'</a>';
				            rowElement += '<div class="dropdown-divider"></div>';
				            rowElement += '<a class="dropdown-item text-danger" href="javascript:eliminaCatalogo(\'' + row.claveTarea + '\', \'' + row.subject + '\');">'+BTN_ELIMINAR_MENU+'</a>';
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
		 if (i == 7){
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




	function abreModal(opcion, id) {
		switch(opcion) {
		case "nuevo" : 
			
			$("#correosOrdenes").load('/siarex247/jsp/envioCorreos/confTareas/modalEnvioCorreos.jsp');
			$("#correosGenerales").load('/siarex247/jsp/envioCorreos/confTareas/modalGenerales.jsp');
			$("#correosPredefinidas").load('/siarex247/jsp/envioCorreos/confTareas/modalPredefinidas.jsp');
			
			$('#myModalDetalle').modal('show');
			try{
				// iniciaFormEnvioCorreos();
				// iniciaFormGenerales();
				// iniciaFormPredefinidas();
			}catch(e){
				e = null;
			}
			break;			
		default :
		}
	}

	
	
	function iniciaFormEnvioCorreos(){
	   
		$("#form-EnvioCorreos").find('.has-success').removeClass("has-success");
	    $("#form-EnvioCorreos").find('.has-error').removeClass("has-error");
		$('#form-EnvioCorreos')[0].reset(); 
		$('#form-EnvioCorreos').removeClass("was-validated"); 
		
	}

	
	function iniciaFormGenerales(){
		   
		$("#form-generales").find('.has-success').removeClass("has-success");
	    $("#form-generales").find('.has-error').removeClass("has-error");
		$('#form-generales')[0].reset(); 
		$('#form-generales').removeClass("was-validated"); 
		
	}

	
	function iniciaFormPredefinidas(){
		   
		$("#form-Predefinidas").find('.has-success').removeClass("has-success");
	    $("#form-Predefinidas").find('.has-error').removeClass("has-error");
		$('#form-Predefinidas')[0].reset(); 
		$('#form-Predefinidas').removeClass("was-validated"); 
		
	}
	
	
	function eliminaCatalogo(claveTarea, subject){
		try{
			
			Swal.fire({
				  icon : 'question',
				  title: '¿Estas seguro de eliminar la Tarea ?',
				  text: 'Subject : '+subject,
				  showDenyButton: true,
				  showCancelButton: false,
				  confirmButtonText: BTN_ACEPTAR_MENU,
				  denyButtonText: BTN_CANCELAR_MENU,
				}).then((result) => {
				  if (result.isConfirmed) {
					  $.ajax({
				           url:  '/siarex247/procesos/tareas/eliminaTarea.action',
				           type: 'POST',
				            dataType : 'json',
				            data : {
				            	claveTarea : claveTarea
				            },
						    success: function(data){
						    	
						    	if (data.codError == "000"){
						    		Swal.fire({
			 						  title: MSG_OPERACION_EXITOSA_MENU,
			 						 html: '<p>Registro Borrado. </p>',
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
	
	
	
	function cancelarRegistro(claveTarea, subject){
		try{
			
			Swal.fire({
				  icon : 'question',
				  title: '¿Estas seguro de cancelar la Tarea ?',
				  text: 'Subject : '+subject,
				  showDenyButton: true,
				  showCancelButton: false,
				  confirmButtonText: BTN_ACEPTAR_MENU,
				  denyButtonText: BTN_CANCELAR_MENU,
				}).then((result) => {
				  if (result.isConfirmed) {
					  $.ajax({
				           url:  '/siarex247/procesos/tareas/cancelaTarea.action',
				           type: 'POST',
				            dataType : 'json',
				            data : {
				            	claveTarea : claveTarea
				            },
						    success: function(data){
						    	
						    	if (data.codError == "000"){
						    		Swal.fire({
			 						  title: MSG_OPERACION_EXITOSA_MENU,
			 						 html: '<p>Registro Cancelado. </p>',
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
	
	function guardarEnvioCorreos(){
		try{
            var formData = new FormData(document.getElementById("form-EnvioCorreos"));
            $.ajax({
            	url: '/siarex247/procesos/tareas/guardarTarea.action',
                dataType: "json",
                type: "post",
                data: formData,
                cache: false,
                contentType: false,
	    		processData: false
            }).done(function(data){
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
            			title: MSG_ERROR_OPERACION_MENU,
            			text: data.mensajeError
            		});
            	}
             });
			
		}catch(e){
			alert('guardarOrdenes()_'+e);
		}
	}
	
	
	
	function preValidaTarea(){
		try{
            var formData = new FormData(document.getElementById("form-EnvioCorreos"));
            $.ajax({
            	url: '/siarex247/procesos/tareas/preValidaTarea.action',
                dataType: "json",
                type: "post",
                data: formData,
                cache: false,
                contentType: false,
	    		processData: false
            }).done(function(data){
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
            	}else if (data.codError == '002'){
            		Swal.fire({
      				  icon : 'question',
      				  title: data.mensajeError,
      				  // text: 'Subject : '+subject,
      				  showDenyButton: true,
      				  showCancelButton: false,
      				  confirmButtonText: BTN_ACEPTAR_MENU,
      				  denyButtonText: BTN_CANCELAR_MENU,
      				}).then((result) => {
      				  if (result.isConfirmed) {
      					$('#claveTarea_PreValida').val(data.claveRegistro);
      					
      					$('#tablaSatisfactorias').DataTable().ajax.reload(null,false);
      					$('#tablaSinVendor').DataTable().ajax.reload(null,false);
      					
      					// $('#myModalDetalle').modal('hide');
      					$('#myModalPreValidar').modal('show');
      				  } else if (result.isDenied) {
      					$('#myModalDetalle').modal('hide');
      					$('#tablaDetalle').DataTable().ajax.reload(null,false);
      				  }
      				})
            	} else {
            		Swal.fire({
            			icon: 'error',
            			title: MSG_ERROR_OPERACION_MENU,
            			text: data.mensajeError
            		});
            	}
             });
			
		}catch(e){
			alert('guardarOrdenes()_'+e);
		}
	}
	
	
	function guardarGenerales(){
		try{
			
			var claveProveedor = getSelectionsDatatable();
			$('#claveProveedor').val(claveProveedor);
			
            var formData = new FormData(document.getElementById("form-generales"));
            $.ajax({
            	url: '/siarex247/procesos/tareas/guardarTarea.action',
                dataType: "json",
                type: "post",
                data: formData,
                cache: false,
                contentType: false,
	    		processData: false
            }).done(function(data){
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
            			title: MSG_ERROR_OPERACION_MENU,
            			text: data.mensajeError
            		});
            	}
             });
            
            
			
		}catch(e){
			alert('guardarOrdenes()_'+e);
		}
	}
	
	
	function guardarPredefinidas(){
		try{
            var formData = new FormData(document.getElementById("form-Predefinidas"));
            $.ajax({
            	url: '/siarex247/procesos/tareas/guardarTarea.action',
                dataType: "json",
                type: "post",
                data: formData,
                cache: false,
                contentType: false,
	    		processData: false
            }).done(function(data){
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
            			title: MSG_ERROR_OPERACION_MENU,
            			text: data.mensajeError
            		});
            	}
             });
			
		}catch(e){
			alert('guardarOrdenes()_'+e);
		}
	}
	
	
	function preValidaTareaPredefinidas(){
		try{
            var formData = new FormData(document.getElementById("form-Predefinidas"));
            $.ajax({
            	url: '/siarex247/procesos/tareas/preValidaTarea.action',
                dataType: "json",
                type: "post",
                data: formData,
                cache: false,
                contentType: false,
	    		processData: false
            }).done(function(data){
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
            	}else if (data.codError == '002'){
            		Swal.fire({
      				  icon : 'question',
      				  title: data.mensajeError,
      				  // text: 'Subject : '+subject,
      				  showDenyButton: true,
      				  showCancelButton: false,
      				  confirmButtonText: BTN_ACEPTAR_MENU,
      				  denyButtonText: BTN_CANCELAR_MENU,
      				}).then((result) => {
      				  if (result.isConfirmed) {
      					$('#claveTarea_PreValida').val(data.claveRegistro);
      					
      					$('#tablaSatisfactorias').DataTable().ajax.reload(null,false);
      					$('#tablaSinVendor').DataTable().ajax.reload(null,false);
      					
      					// $('#myModalDetalle').modal('hide');
      					$('#myModalPreValidar').modal('show');
      				  } else if (result.isDenied) {
      					$('#myModalDetalle').modal('hide');
      					$('#tablaDetalle').DataTable().ajax.reload(null,false);
      				  }
      				})
            	} else {
            		Swal.fire({
            			icon: 'error',
            			title: MSG_ERROR_OPERACION_MENU,
            			text: data.mensajeError
            		});
            	}
             });
			
		}catch(e){
			alert('guardarOrdenes()_'+e);
		}
	}