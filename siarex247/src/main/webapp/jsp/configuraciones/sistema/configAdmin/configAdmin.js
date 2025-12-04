

function iniciaFormConfigAdmin() {
	
	$("#frmAdminSAT").find('.has-success').removeClass("has-success");
    $("#frmAdminSAT").find('.has-error').removeClass("has-error");
	$('#frmAdminSAT')[0].reset(); 
	$('#frmAdminSAT').removeClass("was-validated"); 
			
}



function obtenerConfAdministrador(){
	$.ajax({
		url: '/siarex247/configSistema/configAdmin/obtenerConfiguracion.action',
		type : 'POST',
		dataType : 'json',
		success  : function(data) {//console.log('DATA ADMON: ', data);
			if($.isEmptyObject(data)) {
			}
			else {
				$('#VALIDA_SAT').prop('checked', data.VALIDA_SAT == 'S' ? true : false);
				$('#VALIDA_XML_TIMBRADO').prop('checked', data.VALIDA_XML_TIMBRADO == 'S' ? true : false);
				$('#VALIDAR_CIERRE_ADMIN').prop('checked', data.VALIDAR_CIERRE_ADMIN == 'S' ? true : false);
				$('#VALIDAR_COMPLEMENTO_ADMIN').prop('checked', data.VALIDAR_COMPLEMENTO_ADMIN == 'S' ? true : false);
				$('#VALIDAR_CONTRATO_CONFIDENCIALIDAD').prop('checked', data.VALIDAR_CONTRATO_CONFIDENCIALIDAD == 'S' ? true : false);
				$('#BANDERA_OUTSOURCING_PROVEEDOR').prop('checked', data.BANDERA_OUTSOURCING_PROVEEDOR == 'S' ? true : false);
				$('#BANDERA_LOGO_TOYOTA').prop('checked', data.BANDERA_LOGO_TOYOTA == 'S' ? true : false);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			Swal.fire({
			  icon: 'error',
			  title: '¡Error en Operacion!',
			  text: 'Error al ejecutar obtenerConfAdministrador()'
			});
		}
	});	
}

  function guardarConfigAdmin(){
	  try{
          var formData = new FormData(document.getElementById("frmAdminSAT"));
          formData.append("dato", "valor");

          $.ajax({
              url: '/siarex247/configSistema/configAdmin/grabarAdmin.action',
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

