






  function habilitarAgrupacion(){
	  try {
		  var isCheck = $("#agruparFacturasSW").prop('checked');
		  if (isCheck){
			  $( "#modoAgrupar" ).prop( "disabled", false );
		  }else{
			  $( "#modoAgrupar" ).prop( "disabled", true );
		  }
	  }catch(e){
		  alert('habilitarAgrupacion()_'+e);
	  }
  }
  
  
  function cambiarModo(valorModo){
	  try{
		  if (valorModo == 'TEXTO'){
			  $( "#divBusquedaTxt" ).show();
			  $( "#divBusquedaRFC" ).hide();
			  $( "#divBusquedaXML" ).hide();
			  $( "#fileTXT" ).prop( "disabled", false );
		  }else if (valorModo == 'RFC'){
			  $( "#divBusquedaTxt" ).show();
			  $( "#divBusquedaRFC" ).show();
			  $( "#fileTXT" ).prop( "disabled", true );
			  $( "#divBusquedaXML" ).hide();
		  }else if (valorModo == 'XML'){
			  $( "#divBusquedaXML" ).show();
			  $( "#divBusquedaRFC" ).hide();
			  $( "#divBusquedaTxt" ).hide();
			  $( "#fileTXT" ).prop( "disabled", false );
		  }else {
			 
			  //$( "#divBusquedaTxt" ).hide();
		  }
	  }catch(e){
		  alert('cambiarModo()_'+e);
	  }
  }

	function iniciaFormCatalogo(){
		
		$('#rfcProveedor').select2({
			// dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		
		
		
		/* Reset al Formulario */ 
		$("#frmExportXML").find('.has-success').removeClass("has-success");
	    $("#frmExportXML").find('.has-error').removeClass("has-error");
		$('#frmExportXML')[0].reset(); 
		$('#frmExportXML').removeClass("was-validated"); 
	
		cargaProveedores();
	}
	
  
  
   function guardarDatos(){
  	try{
              var formData = new FormData(document.getElementById("frmExportXML"));
              formData.append("dato", "valor");

              $.ajax({
                  url: '/siarex247/cumplimientoFiscal/exportarXML/exportarFacturas.action',
                  type: "post",
                  dataType: "json",
                  data: formData,
                  beforeSend: function( xhr ) {
	        			$('#overSeccionCA').css({display:'block'});
	        			$("#btnProcesar_XML").prop('disabled', true);
	        	  },
	        	  complete: function(jqXHR, textStatus){
		    		  $('#overSeccionCA').css({display:'none'});
		    		  $("#btnProcesar_XML").prop('disabled', false);
			      },	
                  cache: false,
                  contentType: false,
  	    		processData: false
              }).done(function(data){
            	  if (data.codError == '000'){
            		  Swal.fire({
  						  icon: 'success',
  						  title: MSG_OPERACION_EXITOSA_MENU,
  						  // text: 'El proceso de exportar facturas se ha iniciado satisfactoriamente. En unos momentos recibirá un correo para descargar el archivo de facturas.',
  						  text: MENSAJE_EXITOSO,
  						  showCancelButton: false,
  						  confirmButtonText: BTN_ACEPTAR_MENU,
  						  denyButtonText: BTN_CANCELAR_MENU,
  						}).then((result) => {
  						  if (result.isConfirmed) {
  						
  						  }
  						});
            	  }else{
            		  Swal.fire({
			                title: MSG_ERROR_OPERACION_MENU,
			                html: '<p>'+data.mensajeError+'</p>',	
			                icon: 'error'
			            });
            	  }
            	  
            	  /*
              	if (data.ESTATUS == 'OK'){
              		Swal.fire({
  						  icon: 'success',
  						  title: '¡Operación Exitosa!',
  						  text: 'El proceso de exportar facturas se ha iniciado satisfactoriamente. En unos momentos recibirá un correo para descargar el archivo de facturas.',
  						  showCancelButton: false,
  						  confirmButtonText: 'Aceptar',
  						  denyButtonText: 'Cancelar',
  						}).then((result) => {
  						  if (result.isConfirmed) {
  						
  						  }
  						});
  						  
              	} else {
              	  Swal.fire({
  					  icon: 'error',
  					  title: '¡Error en Operación!',
  					  text: 'Error al guardar la información'
  				  });
              	}
              	*/
             });
  	}
  	catch(e){
  		alert('error : ' + e);
  	}
  }
   
   
   

	function asignarCorreo(){
		try{
				
			$.ajax({
				url  : '/siarex247/seguridad/usuarios/consultaPermisos.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
	 				if (data == null){
	 					Swal.fire({
	 						  title: '¡Operación Exitosa!',
	 						 html: '<p>Error al obtener los permisos del usuario</p>',	
								  showCancelButton: false,
								  confirmButtonText: 'Aceptar',
								  icon: 'success'
	 						}).then((result) => {
	 						});
	 					
	 				}else{
	 					$('#CORREO_RESPONSABLE').val(data.correo);
	 				}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerPermisos()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('obtenerPermisos()_'+e);
		}
	 }
	