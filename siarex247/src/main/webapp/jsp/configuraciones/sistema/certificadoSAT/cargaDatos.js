

function iniciaFormConfigCertificado() {
	
	$("#frmCargaCertificados").find('.has-success').removeClass("has-success");
    $("#frmCargaCertificados").find('.has-error').removeClass("has-error");
	$('#frmCargaCertificados')[0].reset(); 
	$('#frmCargaCertificados').removeClass("was-validated"); 
			
}



function asignarCertificado(){
	$.ajax({
		url: '/siarex247/configSistema/certificadosSAT/obtenerCertificados.action',
		type : 'POST',
		dataType : 'json',
		success  : function(data) {//console.log('DATA ADMON: ', data);
			if($.isEmptyObject(data)) {
			}
			else {
				if (data.TIENE_CERTIFICADO == 'S'){
					$('#TIENE_CERTIFICADO').prop('checked', true);
				}else{
					$('#TIENE_CERTIFICADO').prop('checked', false);
				}
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert('asignarCertificado()_'+thrownError);
		}
	});	
}

  function guardarCertificado(){
	  try{
          var formData = new FormData(document.getElementById("frmCargaCertificados"));
          formData.append("dato", "valor");

          $.ajax({
              url: '/siarex247/configSistema/certificadosSAT/guardarCertificados.action',
              type: "post",
              dataType: "json",
              data: formData,
              cache: false,
              contentType: false,
	    		processData: false
          }).done(function(data){
        	  
        	  	if (data.codError == '000'){
        	  		Swal.fire({
        	  			 // title: '¡Operación Exitosa!',
						 // html: '<p>Cambio Exitosa</p>',
        	  			  title: MSG_OPERACION_EXITOSA_MENU,
            		  	  html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
						  showCancelButton: false,
						  confirmButtonText: BTN_ACEPTAR_MENU,
						  icon: 'success'
						}).then((result) => {
							asignarCertificado();
							$("#frmCargaCertificados")[0].reset();
						});
        	  		
        	  	}else{
        	  		 Swal.fire({
   					  icon: 'error',
   					  title: '¡Error en Operación!',
   					  html: '<p>'+data.mensajeError+'</p>',	
   					  
   				  });
        	  	}
         });
	}
	catch(e){
		alert('guardarCertificado()_'+e);
	}	  
  }

