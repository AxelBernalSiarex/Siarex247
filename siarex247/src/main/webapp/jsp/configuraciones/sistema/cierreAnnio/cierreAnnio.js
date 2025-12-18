



function iniciaFormCierreAnnio() {
	
	/* Inicializa combos en el modal */ 
	/* Reset al Formulario */ 
	$("#form-CierreAnnio").find('.has-success').removeClass("has-success");
    $("#form-CierreAnnio").find('.has-error').removeClass("has-error");
	$('#form-CierreAnnio')[0].reset(); 
	$('#form-CierreAnnio').removeClass("was-validated"); 
			
}



 function guardarDatosCierre(){
	try{
            var f = $(this);
            var formData = new FormData(document.getElementById("form-CierreAnnio"));
            formData.append("dato", "valor");

            $.ajax({
                url: '/siarex247/configSistema/cierreAnnio/cierreAnio.action',
                type: "post",
                dataType: "json",
                data: formData,
                cache: false,
                contentType: false,
	    		processData: false
            }).done(function(data){
            	if (data.ESTATUS == 'OK'){
            		Swal.fire({
              		  icon: 'success',
            		  // title: '¡Operación Exitosa!',
  					  // text: 'La información se guardó exitosamente',
              		   title: MSG_OPERACION_EXITOSA_MENU,
        		  	  html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
					  showCancelButton: false,
					  confirmButtonText: BTN_ACEPTAR_MENU,
					  denyButtonText: BTN_CANCELAR_MENU
 					});
            	}
            	else {
            	  Swal.fire({
					  icon: 'error',
					  title: '¡Error en Operación!',
					  text: 'Error al guardar la información'
				  });
            	}
           });
	}
	catch(e){
		alert('error : '+e);
	}
}
 
 
 function buscaTipoCierre() {
	var anio =  $('#anio').val();
	var tipoCierre =  $('#tipoCierre').val();

	try {
		
		if (anio == ''){
			Swal.fire({
				  icon: 'error',
				  title: '¡Error en Operacion!',
				  text: 'Debe especificar un año'
			  });
		}else if (anio.length <= 3){
			Swal.fire({
				  icon: 'error',
				  title: '¡Error en Operacion!',
				  text: 'Debe especificar un año valido'
			  });
		}else{
			$.ajax({
	            url:  '/siarex247/configSistema/cierreAnnio/obtenerCierre.action',
	            data : {
	            	anio : anio,
	            	tipoCierre : tipoCierre
	            },
	            type: 'POST',
	            dataType : 'json',
			    success: function(data){
			    	if (data.fechaApartir == ''){
			    		Swal.fire({
							  icon: 'info',
							  title: '¡No Encontro Información!',
							  text: 'No se encontro información con el año proporcionado.'
						  });
			    	}else{
			    		$('#mensajeError1').text(data.mensajeError1);
				    	$('#mensajeError2').text(data.mensajeError2);
					    $('#fechaApartir').val(data.fechaApartir);
						$('#fechaHasta').val(data.fechaHasta);	
			    	}
			    	
			    }
			});
		}
		
		
	}
	catch(e) {
		alert('buscaTipoCierre()_'+e);
	}
 }
