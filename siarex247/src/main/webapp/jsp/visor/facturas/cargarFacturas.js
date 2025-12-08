

function iniciaFormCargarFacturasAmericanasArchivo(){
		resetearAcordeon();
		$('#valError_AmericanosArchivo_Cargar').hide();
		$('#valExito_AmericanosArchivo_Cargar').hide();
		$('#btnValidar_AmericanosArchivo_Cargar').prop('disabled', false);
		 
		$("#frmCargarAmericanosArchivo").find('.has-success').removeClass("has-success");
	    $("#frmCargarAmericanosArchivo").find('.has-error').removeClass("has-error");
		$('#frmCargarAmericanosArchivo')[0].reset(); 
		$('#frmCargarAmericanosArchivo').removeClass("was-validated");
		
	}
	
	function iniciaFormCargarFacturasAmericanasCaptura(){
		$('#valError_AmericanosCaptura_Cargar').hide();
		$('#valExito_AmericanosCaptura_Cargar').hide();
		$('#btnValidar_AmericanosCaptura_Cargar').prop('disabled', false);
		
		
		$('#tipoMoneda_AmericanosCaptura_Cargar').select2({
			theme: "bootstrap-5",
			width: $( this ).data( 'width' ) ? $( this ).data( 'width' ) : $( this ).hasClass( 'w-100' ) ? '100%' : 'style',
			placeholder: $( this ).data( 'placeholder' ),
			dropdownParent: $('#myModalCargarFacturaAmericanos')
		});
		
		
		$("#frmCargarAmericanosCaptura").find('.has-success').removeClass("has-success");
	    $("#frmCargarAmericanosCaptura").find('.has-error').removeClass("has-error");
		$('#frmCargarAmericanosCaptura')[0].reset(); 
		$('#frmCargarAmericanosCaptura').removeClass("was-validated");
		
	}
	
	
	function iniciaFormCargarCartaPorte(){
		$("#error_Cargar_CartaPorte").hide();
		$("#exito_Cargar_CartaPorte").hide();
		$("#frmCargarCartaPorte").find('.has-success').removeClass("has-success");
	    $("#frmCargarCartaPorte").find('.has-error').removeClass("has-error");
		$('#frmCargarCartaPorte')[0].reset(); 
		$('#frmCargarCartaPorte').removeClass("was-validated");
		
	}

	

	function resetearAcordeon(){
		$("#accordion1 #heading1 accordion-button").removeClass("collapsed");
		$("#accordion1 #heading1  accordion-button").attr('aria-expanded', 'true');
		$("#accordion1 #collapse1").addClass("show");
		
		$("#accordion1 #heading2 accordion-button").addClass("collapsed");
		$("#accordion1 #heading2 accordion-button").attr('aria-expanded', 'false');
		$("#accordion1 #collapse2").removeClass("show");
		
	}
	
	
	


	function iniciaFormValidarAmericanas(){
		$("#frmValidarAmericanas").find('.has-success').removeClass("has-success");
	    $("#frmValidarAmericanas").find('.has-error').removeClass("has-error");
		$('#frmValidarAmericanas')[0].reset(); 
		$('#frmValidarAmericanas').removeClass("was-validated"); 
		   
	}
	
	
	
	function cargarFactura(){
		try{
			
			var formData = new FormData(document.getElementById("frmCargarFactura"));
	            $.ajax({
	            	url: '/siarex247/visor/tablero/cargarFactura.action',
	                dataType: "json",
	                beforeSend: function( xhr ) {
	        			$('#overSeccionIndividual').css({display:'block'});
	        			$('#btnSometerIndividual').prop('disabled', true);
	        			$('#btnCerrar_CargarFactura').prop('disabled', true);
	        		},
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false,
		    		complete: function(jqXHR, textStatus){
	    				$('#overSeccionIndividual').css({display:'none'});
	    				$('#btnCerrar_CargarFactura').prop('disabled', false);
		    		},
	            }).done(function(data){
	            	if (data.codError == '000') {
	            		$('#mensajeError_Individual_Cargar').val(data.mensajeError);
	            		$('#btnGuardar_CargarFactura').show();
	            		$('#valExito_Individual_Cargar').show();
	            		$('#valError_Individual_Cargar').hide();
	            		$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
					}else {
						$('#mensajeError_Individual_Cargar').val(data.mensajeError);
						$('#btnSometerIndividual').prop('disabled', false);
						$('#valError_Individual_Cargar').show();
						$('#valExito_Individual_Cargar').hide();
					} 
	            	
	             });
		}
		catch(e){
			alert('cargarFactura()_'+e);
		}
    }
	
	
	function cargarFacturaMultiple(){
		try{
			
			var formData = new FormData(document.getElementById("frmCargarFacturaMultiple"));
	            $.ajax({
	            	url: '/siarex247/visor/tablero/cargarFacturaMultiple.action',
	                dataType: "json",
	                beforeSend: function( xhr ) {
	        			$('#overSeccionMultiple').css({display:'block'});
	        			$('#btnSometerMultiple').prop('disabled', true);
	        			$('#btnCerrar_CargarFactura').prop('disabled', true);
	        		},
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false,
		    		complete: function(jqXHR, textStatus){
	    				$('#overSeccionMultiple').css({display:'none'});
	    				$('#btnCerrar_CargarFactura').prop('disabled', false);
		    		},
	            }).done(function(data){
	            	if (data.codError == '000') {
	            		$('#mensajeError_Multiple_Cargar').val(data.mensajeError);
	            		$('#btnGuardar_CargarFactura').show();
	            		$('#valExito_Multiple_Cargar').show();
	            		$('#valError_Multiple_Cargar').hide();
	            		$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
					}else {
						$('#mensajeError_Multiple_Cargar').val(data.mensajeError);
						$('#btnSometerMultiple').prop('disabled', false);
						$('#valError_Multiple_Cargar').show();
						$('#valExito_Multiple_Cargar').hide();
					} 
	            	
	             });
		}
		catch(e){
			alert('cargarFacturaMultiple()_'+e);
		}
    }
	
	
	function guardarCargarFactura(){
		try{
			$('#myModalCargarFactura').modal('hide');
			// $('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
			
		} catch(e){
			alert('guardarCargarFactura()_'+e);
		}
    }

	
	
	function cargarFacturaAmericanaArchivo(){
		try{
			
			var formData = new FormData(document.getElementById("frmCargarAmericanosArchivo"));
	            $.ajax({
	            	url: '/siarex247/visor/tablero/cargarFacturaAmericanaArchivo.action',
	                dataType: "json",
	                beforeSend: function( xhr ) {
	        			$('#overSeccionAmericanosArchivo').css({display:'block'});
	        			$('#btnValidar_AmericanosArchivo_Cargar').prop('disabled', true);
	        			$('#btnCerrar_Americanos_Cargar').prop('disabled', true);
	        		},
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false,
		    		complete: function(jqXHR, textStatus){
	    				$('#overSeccionAmericanosArchivo').css({display:'none'});
	    				$('#btnCerrar_Americanos_Cargar').prop('disabled', false);
		    		},
	            }).done(function(data){
	            	if (data.codError == '000') {
	            		$('#mensajeRespuesta_AmericanosArchivo_Cargar').val(data.mensajeError);
	            		$('#btnSometer_AmericanosCaptura_Cargar').show();
	            		$('#valExito_AmericanosArchivo_Cargar').show();
	            		$('#valError_AmericanosArchivo_Cargar').hide();
	            		$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
					}else {
						$('#btnValidar_AmericanosArchivo_Cargar').prop('disabled', false);
						$('#mensajeRespuesta_AmericanosArchivo_Cargar').val(data.mensajeError);
						$('#valError_AmericanosArchivo_Cargar').show();
						$('#valExito_AmericanosArchivo_Cargar').hide();
	            		
					} 
	            	
	             });
		}
		catch(e){
			alert('cargarFacturaAmericanaArchivo()_'+e);
		}
    }
	
	
	
	function cargarFacturaAmericanaCaptura(){
		try{
			
			var formData = new FormData(document.getElementById("frmCargarAmericanosCaptura"));
	            $.ajax({
	            	url: '/siarex247/visor/tablero/cargarFacturaAmericanaCaptura.action',
	                dataType: "json",
	                beforeSend: function( xhr ) {
	        			$('#overSeccionAmericanosCaptura').css({display:'block'});
	        			$('#btnValidar_AmericanosCaptura_Cargar').prop('disabled', true);
	        			$('#btnCerrar_Americanos_Cargar').prop('disabled', true);
	        		},
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false,
		    		complete: function(jqXHR, textStatus){
	    				$('#overSeccionAmericanosCaptura').css({display:'none'});
	    				$('#btnCerrar_Americanos_Cargar').prop('disabled', false);
		    		},
	            }).done(function(data){
	            	if (data.codError == '000') {
	            		$('#mensajeRespuesta_AmericanosCaptura_Cargar').val(data.mensajeError);
	            		$('#btnSometer_AmericanosCaptura_Cargar').show();
	            		$('#valExito_AmericanosCaptura_Cargar').show();
	            		$('#valError_AmericanosCaptura_Cargar').hide();
					}else {
						$('#btnValidar_AmericanosCaptura_Cargar').prop('disabled', false);
						$('#mensajeRespuesta_AmericanosCaptura_Cargar').val(data.mensajeError);
						$('#valError_AmericanosCaptura_Cargar').show();
						$('#valExito_AmericanosCaptura_Cargar').hide();
	            		
					} 
	            	
	             });
		}
		catch(e){
			alert('cargarFacturaAmericanaCaptura()_'+e);
		}
    }
	
	
	function guardarCambiosAmericano(){
		try{
			$('#myModalCargarFacturaAmericanos').modal('hide');
			$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
		}catch(e){
			alert('guardarCambiosAmericano'+e);
		}
	}
	
	
	
	function consultarEstatusAmericanaValidacion(){
		try{
			var folioOrden = getSelections();
			if (folioOrden == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                // html: '<p>Es necesario seleccionar al menos un registro para validar.</p>',
		                html: '<p>'+VISOR_MSG5+'</p>',
		                icon: 'info'
		            });
			}else{
				var arrFolios = folioOrden.split(";");
				if (arrFolios.length >= 3){
					Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                // html: '<p>Es necesario seleccionar solo un registro para validar.</p>',
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
								if ( data.estatusOrden == 'A9'){
									iniciaFormValidarAmericanas();
									consultarInformacion_ValidarAmericana(numeroOrden);
									$('#myModalValidarFacturaAmericanos').modal('show');
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
							alert('consultarEstatusAmericanaValidacion()_'+thrownError);
						}
					});
				}
			}
		}catch(e){
			alert('consultarEstatusAmericanaValidacion()_'+e);
		}
	}
	
	
	function consultarInformacion_ValidarAmericana(folioOrden){
		try{
			$('#folioOrden_ValidarAmericanas').val(folioOrden);
			
			$.ajax({
				url  : '/siarex247/visor/tablero/buscarOrdenAmericana.action',
				type : 'POST', 
				data : {
					folioOrden : folioOrden,
					tipoDocumento : 'PDF_FACTURA'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					} else {
						$('#ordenCompra_ValidarAmericanas').val(data.folioEmpresa);
						$('#razonSocial_ValidarAmericanas').val(data.razonSocial);
						$('#factura_ValidarAmericanas').val(data.serieFolio);
						$('#total_ValidarAmericanas').val(data.monto);
						$('#fecha_ValidarAmericanas').val(data.fechaFactura);
						cargaMotivosAmericanos();
						window.open('/siarex247/files/'+data.nombrePDF,'frmPDFAmericana');
						
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('consultarInformacion_ValidarAmericana()_'+thrownError);
				}
			});
		}catch(e){
			alert('consultarInformacion_ValidarAmericana()_'+e);
		}
	}
	
	
	function guardarValidarAmericana(tipoAccion){
		try{
			
			var claveMotivo = $('#motivoRechazo_ValidarAmericanas').val();
			var folioOrden = $('#folioOrden_ValidarAmericanas').val();
			
			if (claveMotivo == '' && tipoAccion == 'INCORRECTO'){
				Swal.fire({
					  title: MSG_ERROR_OPERACION_MENU,
					  // html: '<p>Debe seleccionar un motivo de rechazo</p>',
					  html: '<p>'+VISOR_MSG22+'</p>',
						  showCancelButton: false,
						  confirmButtonText: BTN_ACEPTAR_MENU,
						  icon: 'info'
					}).then((result) => {
						
					});
			}else{
				if (tipoAccion == 'CORRECTO'){
					claveMotivo = 0;	
				}
				
				$.ajax({
					url  : '/siarex247/visor/tablero/modificaOrdenAmericana.action',
					type : 'POST', 
					data : {
						tipoAccion : tipoAccion,
						claveMotivo : claveMotivo,
						folioOrden : folioOrden
						
					},
					dataType : 'json',
					 beforeSend: function( xhr ) {
		        			$('#btnAprobarValidarAmericanas').prop('disabled', true);
		        			$('#btnRechazarValidarAmericanas').prop('disabled', true);
		        			$('#overSeccion_ValidarAmericanas').css({display:'block'});
		        		},
		        		complete: function(jqXHR, textStatus){
		        			$('#btnAprobarValidarAmericanas').prop('disabled', false);
		        			$('#btnRechazarValidarAmericanas').prop('disabled', false);
		        			$('#overSeccion_ValidarAmericanas').css({display:'none'});
			    		},
			    		
					success  : function(data) {
						if (data.codError == '000') {
		            		Swal.fire({
		 						  title: MSG_OPERACION_EXITOSA_MENU,
		 						 html: '<p>Cambio Exitoso. </p>',
									  showCancelButton: false,
									  confirmButtonText: BTN_ACEPTAR_MENU,
									  icon: 'success'
		 						}).then((result) => {
		 							$('#myModalValidarFacturaAmericanos').modal('hide');
		 							$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
		 						});
						} else {
							Swal.fire({
		 						  title: MSG_ERROR_OPERACION_MENU,
		 						 html: '<p>'+data.mensajeError+'</p>',
									  showCancelButton: false,
									  confirmButtonText: BTN_ACEPTAR_MENU,
									  icon: 'error'
		 						}).then((result) => {
		 							
		 						});
						}
					},
					error : function(xhr, ajaxOptions, thrownError) {
						alert('guardarValidarAmericana()_'+thrownError);
					}
				});	
			}
			
		}catch(e){
			alert('guardarValidarAmericana()_'+e);
		}
	}
	
	
	
	function cargarCartaPorte(){
		try{
			 $('#btnGuardar_CargarCartaPorte').hide();
			 $('#btnSometerCartaPorte').prop('disabled', false);
			 iniciaFormCargarCartaPorte();
			$('#myModalCargarCartaPorte').modal('show');
		}catch(e) {
			alert('cargarCartaPorte()_'+e);
		}
	}
	
	
	
	function validarCartaPorte(){
		try{
			
			var formData = new FormData(document.getElementById("frmCargarCartaPorte"));
	            $.ajax({
	            	url: '/siarex247/visor/tablero/cargarCartaPorte.action',
	                dataType: "json",
	                beforeSend: function( xhr ) {
	        			$('#overSeccion_Cargar_CartaPorte').css({display:'block'});
	        			$('#btnSometerCartaPorte').prop('disabled', true);
	        			$('#btnCerrar_CargarCartaPorte').prop('disabled', true);
	        		},
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false,
		    		complete: function(jqXHR, textStatus){
	    				$('#overSeccion_Cargar_CartaPorte').css({display:'none'});
	    				$('#btnCerrar_CargarCartaPorte').prop('disabled', false);
		    		},
	            }).done(function(data){
	            	if (data.codError == '000') {
	            		$('#mensajeRespuesta_Cargar_CartaPorte').val(data.mensajeError);
	            		$('#btnGuardar_CargarCartaPorte').show();
	            		$('#exito_Cargar_CartaPorte').show();
	            		$('#error_Cargar_CartaPorte').hide();
					}else {
						$('#btnSometerCartaPorte').prop('disabled', false);
						$('#mensajeRespuesta_Cargar_CartaPorte').val(data.mensajeError);
						$('#error_Cargar_CartaPorte').show();
						$('#exito_Cargar_CartaPorte').hide();
	            		
					} 
	            	
	             });
		}
		catch(e){
			alert('validarCartaPorte()_'+e);
		}
    }