
function obtenerCorreos(){
	$.ajax({
		url: '/siarex247/configSistema/correosRespaldo/obtenerCorreos.action',
		type : 'POST',
		dataType : 'json',
		success  : function(data) {
			if($.isEmptyObject(data)) {
			}
			else {
				$('#CORREO_ORDENES').val(data.CORREO_ORDENES);
				$('#CORREO_PAGOS').val(data.CORREO_PAGOS);
				// $('#CORREO_RESPALDO').val(data.CORREO_RESPALDO);
				// $('#CORREO_COMPLEMENTO').val(data.CORREO_COMPLEMENTO);
				$('#CORREO_LISTA_NEGRA_SAT_1').val(data.CORREO_LISTA_NEGRA_SAT_1);
				$('#CORREO_LISTA_NEGRA_SAT_2').val(data.CORREO_LISTA_NEGRA_SAT_2);
				$('#CORREO_LISTA_NEGRA_SAT_3').val(data.CORREO_LISTA_NEGRA_SAT_3);
				$('#CORREO_LISTA_NEGRA_SAT_4').val(data.CORREO_LISTA_NEGRA_SAT_4);
				$('#CORREO_LISTA_NEGRA_SAT_5').val(data.CORREO_LISTA_NEGRA_SAT_5);
				$('#CORREO_AVISO_UUID_BOVEDA_1').val(data.CORREO_AVISO_UUID_BOVEDA_1);
				$('#CORREO_AVISO_UUID_BOVEDA_2').val(data.CORREO_AVISO_UUID_BOVEDA_2);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert('obtenerCorreos()_'+thrownError);
		}
	});	
}



function iniciaFormCorreoRespaldo() {
	
	/* Inicializa combos en el modal */ 
	/* Reset al Formulario */ 
	$("#form-CorreoRespaldo").find('.has-success').removeClass("has-success");
    $("#form-CorreoRespaldo").find('.has-error').removeClass("has-error");
	$('#form-CorreoRespaldo')[0].reset(); 
	$('#form-CorreoRespaldo').removeClass("was-validated"); 
			
}

 function guardarDatosCorreoRespaldo(){
	try{
            var formData = new FormData(document.getElementById("form-CorreoRespaldo"));
            formData.append("dato", "valor");

            $.ajax({
                url: '/siarex247/configSistema/correosRespaldo/grabarConf.action',
                type: "post",
                dataType: "json",
                data: formData,
                cache: false,
                contentType: false,
	    		processData: false
            }).done(function(data){//console.log('DATA CORREOS.....: ', data);
            	if (data.ESTATUS == 'OK'){
            		Swal.fire({
              		  icon: 'success',
					  //title: '¡Operación Exitosa!',
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