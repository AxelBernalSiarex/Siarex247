	var posOrden = 0;
	
	
	function  obtenerProveedor(tipoCertificado){
		var idProveedores = '';
		try{
			var dataSelect = tablaProveedores.rows('.selected').data(); 
			$.each(dataSelect, function(key, row) {
				idProveedores+= row.claveRegistro + ";"		
		    });
			
			if (idProveedores == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                //html: '<p>Debe seleccionar al menos un registro para continuar.</p>',	
		                html: '<p>'+VALIDACION_ETQ108+'</p>',
		                icon: 'info'
		            });
			}else{
				  
				if (idProveedores != null && idProveedores.indexOf(';') > -1){
		    		  var arrOrdenes = idProveedores.split(';');
		              if (arrOrdenes.length - 1 == posOrden){
		            	 // $('#myModalValidar').modal('hide');
		            	  	Swal.fire({
		 						 title: MSG_ERROR_OPERACION_MENU,
		 						 //html: '<p>Finalizó proceso validación de certificados</p>',
		 						   html: '<p>'+MENSAJE_FINALIZO_CERTIFICADOS+'</p>',
									  showCancelButton: false,
									  confirmButtonText: BTN_ACEPTAR_MENU,
									  icon: 'info'
		 						}).then((result) => {
		 							$('#myModalValidar').modal('hide');
		  	 						$('#tablaProveedores').DataTable().ajax.reload(null,false);
		 						});
		            	  
		              }else{
		            	  var claveRegistro = arrOrdenes[posOrden]; 
		            	  // document.getElementById('claveRegistro').value = claveRegistro;
		            	  consultarCertificado(claveRegistro, tipoCertificado);
		                  posOrden++;
		              }
		    	  }
			}
		}catch(e){
			alert('obtenerProveedor()_'+e);
		}
	}
	

	function consultarCertificado(claveRegistro, tipoCertificado){
		
		try{
			
			$.ajax({
				url  : '/siarex247/catalogos/proveedores/buscarCertificados.action',
				type : 'post', 
				data : {
					claveRegistro : claveRegistro,
					tipoCertificado : tipoCertificado
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						if (data.estatusRegistro == 'true'){
							$('#claveRegistro_Validar').val(claveRegistro)
							 $('#txtrfc_Validar').val(data.rfc);
							 $('#txtRazon_Validar').val(data.razonSocial);
							 var nombreArchivo = data.nombreArchivo;
							 window.open('/siarex247/files/'+nombreArchivo,'frmPDFOrdenes');
							 $('#myModalValidar').modal('show');
						}else{
							obtenerProveedor(tipoCertificado);
							/*
							Swal.fire({
		 						 title: '¡Operación Exitosa!',
		 						 html: '<p>Finalizó proceso validación de certificados</p>',
									  showCancelButton: false,
									  confirmButtonText: 'Aceptar',
									  icon: 'info'
		 						}).then((result) => {
		 							$('#myModalValidar').modal('hide');
		  	 						$('#tablaProveedores').DataTable().ajax.reload(null,false);
		 						});
							
							 */
						}
						
						 
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('consultarCertificado()_'+thrownError);
				}
			});			
		}catch(e){
			alert('consultarCertificado()_'+e);
		}
    }
	
	
	function actualizarCertificado(bandCertificado){
		try{
			  var claveRegistro = $('#claveRegistro_Validar').val();
			  var tipoCertificado = $('#tipoCertificado_Validar').val();
			  var rfc = $('#txtrfc_Validar').val();
			  
	 		$.ajax({
				url  : '/siarex247/catalogos/proveedores/actualizaCertificadoCompras.action',
				type : 'POST', 
				data : {
					claveRegistro : claveRegistro,
                	tipoCertificado : tipoCertificado,
                	bandCertificado : bandCertificado,
                	rfc			    : rfc
					
				},
				dataType : 'json',
				success  : function(data) {
					obtenerProveedor(tipoCertificado);
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('actualizarCertificado()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('actualizarCertificado()_'+e);
		}
	 }
	
	
	
	