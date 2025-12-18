
	$(document).ready(function() {
		
		consultarAccesos();
	}); 
	

	function consultarAccesos(){
		try{
			$.ajax({
				url  : '/siarex247/seguridad/usuarios/consultaPermisos.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						// alert('idPerfil===>'+data.idPerfil);
						if (data.idPerfil == 2){
							$('#menu_OrdenesPago').hide();
							$('#menu_EliminarOrdenes').hide();
						}
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerTotal()_'+thrownError);
				}
			});	
		}catch(e){
			alert('obtenerTotal()_'+e);
		}
		
    }
