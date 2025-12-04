
function obtenerConfOrdenes(){
	$.ajax({
		url: '/siarex247/configSistema/configOrdenes/obtenerConfOrden.action',
		type : 'POST',
		dataType : 'json',
		success  : function(data) {
			if($.isEmptyObject(data)) {
			}
			else {
				$('#TXTEMPRESA').val(data.EMPRESA);
				$('#LLAVE_FINPDF').val(data.LLAVE_FINPDF);
				$('#LLAVE_ORDENES').val(data.LLAVE_ORDENES);
				$('#LLAVE_FIN_ORDENES').val(data.LLAVE_FIN_ORDENES);
				$('#LLAVE_VENDOR').val(data.LLAVE_VENDOR);
				$('#LLAVE_FIN_VENDOR').val(data.LLAVE_FIN_VENDOR);
				$('#LLAVE_TOTAL').val(data.LLAVE_TOTAL);
				$('#LLAVE_FIN_TOTAL').val(data.LLAVE_FIN_TOTAL);
				$('#LLAVE_MONEDA').val(data.LLAVE_MONEDA);
				$('#LLAVE_FIN_MONEDA').val(data.LLAVE_FIN_MONEDA);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			Swal.fire({
			  icon: 'error',
			  title: '¡Error en Operacion!',
			  text: 'Error al ejecutar obtenerConfOrdenes()'
			});
		}
	});	
}



function iniciaFormConfOrdenes() {
	
	/* Inicializa combos en el modal */ 
	/* Reset al Formulario */ 
	$("#form-ConfOrdenes").find('.has-success').removeClass("has-success");
    $("#form-ConfOrdenes").find('.has-error').removeClass("has-error");
	$('#form-ConfOrdenes')[0].reset(); 
	$('#form-ConfOrdenes').removeClass("was-validated"); 
			
}



 function guardarDatos(){
	try{
            var formData = new FormData(document.getElementById("form-ConfOrdenes"));
            formData.append("dato", "valor");

            $.ajax({
                url: '/siarex247/configSistema/configOrdenes/actualizaOrdenes.action',
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
            		  title: MSG_OPERACION_EXITOSA_MENU,
  					 // text: 'La información se guardó exitosamente',
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