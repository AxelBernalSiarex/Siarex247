
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
						if (data.idPerfil == 4){
							$('#btnNuevo_Visor').hide();
							var btnNuevo =  $('.validarNuevo');
		            	 	btnNuevo.addClass("ocultarBoton");
		            	 	
							
							$('#menuValidar_Completo').hide();
							$('#menuMovimientos_Completo').hide();
							if (data.isAmericano == 'S'){
								$('#menuCargar_Factura').hide();	
								$('#menuCargar_Complemento').hide();
								$('#menuCargar_NotaCredito').hide();
								$('#menuCargar_CartaPorte').hide();
								// $('#menuCargar_SolicitarLiberacion').hide();
							}else{
								$('#menuCargar_FacturaAmericana').hide();
							}
							
						}else if (data.idPerfil == 2){
							$('#menuValidar_Completo').hide();
							$('#menuMovimientos_FechaRecepcion').hide();
							$('#menuMovimientos_EliminarOrdenes').hide();
							$('#menuMovimientos_EliminarComplemento').hide();
							$('#menuMovimientos_EliminarNota').hide();
							$('#menuMovimientos_EliminarCartaPorte').hide();
							
						}else{
							// $('#menuCargar_SolicitarLiberacion').hide();
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
