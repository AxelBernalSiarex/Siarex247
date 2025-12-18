



	function iniciaFormFechaPago(){
		$("#fechaActual_FechaPago").prop('disabled', true);
		$("#folioEmpresa_FechaPago").prop('disabled', true);
		
		$("#frmFechaPago").find('.has-success').removeClass("has-success");
	    $("#frmFechaPago").find('.has-error').removeClass("has-error");
		$('#frmFechaPago')[0].reset(); 
		$('#frmFechaPago').removeClass("was-validated"); 
		   
	}
	
	
	
	function consultarFechaPago(){
		try{
			var folioOrden = getSelections();
			if (folioOrden == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		               // html: '<p>Es necesario seleccionar al menos un registro.</p>',
		                html: '<p>'+VISOR_MSG5+'</p>',
		                icon: 'info'
		            });
			}else{
				var arrFolios = folioOrden.split(";");
				if (arrFolios.length >= 3){
					Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		               // html: '<p>Es necesario seleccionar solo un registro.</p>',
		                html: '<p>'+VISOR_MSG6+'</p>',
		                icon: 'info'
		            });
				}else{
					var numeroOrden = arrFolios[0]; 
					$.ajax({
						url  : '/siarex247/visor/tablero/consultarOrden.action',
						type : 'POST', 
						data : {
							folioOrden : numeroOrden
						},
						dataType : 'json',
						success  : function(data) {
							if($.isEmptyObject(data)) {
							} else {
								if ( data.estatusOrden == 'A4' || data.estatusOrden == 'A6'){
									iniciaFormFechaPago();
									// consultarInformacion_NotaCredito(numeroOrden);
									$("#folioOrden_FechaPago").val(numeroOrden);
									$("#fechaActual_FechaPago").val(data.fechaPago);
									$("#folioEmpresa_FechaPago").val(data.folioEmpresa);
									$('#myModalFechaRecepcion').modal('show');
								}else{
									Swal.fire({
			  			                title: MSG_ERROR_OPERACION_MENU,
			  			                // html: '<p>Estatus de la Orden es incorrecta para validar</p>',
			  			                html: '<p>'+VISOR_MSG21+'</p>',
			  			                icon: 'info'
			  			            });									
								}
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert('consultarNotaCredito()_'+thrownError);
						}
					});
				}
			}
		}catch(e){
			alert('consultarFechaPago()_'+e);
		}
	}
	
	
	
	function modificarFechaPago(){
		try{
				var formData = new FormData(document.getElementById("frmFechaPago"));
	            $.ajax({
	            	url: '/siarex247/visor/tablero/modificarFechaPago.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
	                beforeSend: function( xhr ) {
	                	$('#folioEmpresa_FechaPago').prop('disabled', false);
	        		},
	        		complete: function(jqXHR, textStatus){
		    		},
		    		processData: false
	            }).done(function(data){
	            	if (data.codError == '000') {
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 html: '<p>Cambio Exitoso</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalFechaRecepcion').modal('hide');
	  	 						$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
	 						});
					} else {
						Swal.fire({
  			                title: MSG_ERROR_OPERACION_MENU,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
	             });				
		}
		catch(e){
			alert('modificarFechaPago()_'+e);
		}
    }
