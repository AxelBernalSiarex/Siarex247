


function iniciaFormConfigAdicionales() {
	
	$("#form-ConfigAdicionales").find('.has-success').removeClass("has-success");
    $("#form-ConfigAdicionales").find('.has-error').removeClass("has-error");
	$('#form-ConfigAdicionales')[0].reset(); 
	$('#form-ConfigAdicionales').removeClass("was-validated"); 
			
}





function obtenerConfAdicionales(){
	$.ajax({
		url: '/siarex247/configSistema/configAdicionales/obtenerConfiguracion.action',
		type : 'POST',
		dataType : 'json',
		success  : function(data) {//console.log('DATA ADMON: ', data);
			if($.isEmptyObject(data)) {
			}
			else {
				$('#SERIE_FALTANTE').val(data.SERIE_FALTANTE);
		    	$('#DIA_APARTIR_COMPLE').val(data.DIA_APARTIR_COMPLE);
		    	$('#FECHA_APARTIR_COMPLE').val(data.FECHA_APARTIR_COMPLE);
				$('#RECHAZAR_COMPLE').prop('checked', data.RECHAZAR_COMPLE == 'S' ? true : false);
				$('#BLOQUEAR_PROVEEDORES').prop('checked', data.BLOQUEAR_PROVEEDORES == 'S' ? true : false);
				$('#BAND_VALIDFECHAS_COMPLE').prop('checked', data.BAND_VALIDFECHAS_COMPLE == 'S' ? true : false);
				$('#NOTIF_CORREO_ORDEN').prop('checked', data.NOTIF_CORREO_ORDEN == 'S' ? true : false);
				$('#VALIDAR_NOTAS').prop('checked', data.VALIDAR_NOTAS == 'S' ? true : false);
				$('#PREDEFINIR_VALOR_SERIE').prop('checked', data.PREDEFINIR_VALOR_SERIE == 'S' ? true : false);
				$('#VALOR_SERIE_AMERICANAS').val(data.VALOR_SERIE_AMERICANAS);
				$('#PERMITIR_ACCESO_GENERADOR').prop('checked', data.PERMITIR_ACCESO_GENERADOR == 'S' ? true : false);
				$('#RFC_RECEPTOR').prop('checked', data.RFC_RECEPTOR == 'S' ? true : false);
				$('#BLOQUEAR_IMSS').prop('checked', data.BLOQUEAR_IMSS == 'S' ? true : false);
				$('#BLOQUEAR_SAT').prop('checked', data.BLOQUEAR_SAT == 'S' ? true : false);
				$('#VALIDAR_CP').prop('checked', data.VALIDA_CARTA_PORTE == 'S' ? true : false);
				$('#PERMITIR_CARTA_PORTE').prop('checked', data.PERMITIR_CARTA_PORTE == 'S' ? true : false);
				$('#VALOR_RFC_RECEPTOR').val(data.VALOR_RFC_RECEPTOR);
				$("#VALOR_RFC_RECEPTOR").prop("disabled", data.RFC_RECEPTOR == 'S' ? false : true);
				$('#LABEL_LAYOUT_ORDEN').val(data.LABEL_LAYOUT_ORDEN);
				$('#LABEL_LAYOUT_MULTIPLE').val(data.LABEL_LAYOUT_MULTIPLE);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert('obtenerConfAdicionales()_'+thrownError);
		}
	});	
}



function guardarConfigAdicionales(){
	var formData = new FormData(document.getElementById("form-ConfigAdicionales"));
    formData.append("dato", "valor");

    $.ajax({
        url: '/siarex247/configSistema/configAdicionales/grabarValidacionSAT.action',
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
