

	function getSelections(){
		var folioOrden = '';
		try{
			var dataSelect = tablaDetalleVisor.rows('.selected').data(); 
			$.each(dataSelect, function(key, row) {
				folioOrden+= row.folioOrden + ";"		
		    });
			
		}catch(e){
			alert('getSelections()_'+e);
		}
		return folioOrden;
	}
	
	
	function getSelectionsEmpresa(){
		var folioEmpresa = '';
		try{
			var dataSelect = tablaDetalleVisor.rows('.selected').data(); 
			$.each(dataSelect, function(key, row) {
				folioEmpresa+= row.folioEmpresa + ";"		
		    });
			
		}catch(e){
			alert('getSelectionsEmpresa()_'+e);
		}
		return folioEmpresa;
	}
	
	
	function getElementosDatatable(){
		var llaveRegistros = '';
		try{
			 var table = $('#tablaDetalleVisor').DataTable();
			 table.column(2, { search:'applied' }).data().each(function(value, index) {
				 llaveRegistros+= value + ";";
			 });
			 
		}catch(e){
			alert('getElementosDatatable()_'+e);
		}
		return llaveRegistros;
	}	
	

	function eliminarOrdenes(){
		try{
			var folioOrden = getSelections();
			if (folioOrden == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                //html: '<p>Es necesario seleccionar al menos un registro para eliminar.</p>',	
		                html: '<p>'+VISOR_MSG5+'</p>',
		                icon: 'info'
		            });
			}else{
				Swal.fire({
					  icon : 'question',
					  //title: '¿Estas seguro de eliminar las Ordenes de Compra seleccionadas ?',
					  title: VISOR_MSG16,
					  // text: 'Orden de Compra : ',
					  showDenyButton: true,
					  showCancelButton: false,
					  confirmButtonText: BTN_ACEPTAR_MENU,
					  denyButtonText: BTN_CANCELAR_MENU,
					}).then((result) => {
					  if (result.isConfirmed) {
						  $.ajax({
							   url:  '/siarex247/visor/tablero/eliminaOrdenes.action',
					           type: 'POST',
					            dataType : 'json',
					            data : {
					            	foliosEliminar : folioOrden
					            },
							    success: function(data){
							    	
							    	if (data.codError == "000"){
							    		Swal.fire({
				 						  title: MSG_OPERACION_EXITOSA_MENU,
				 						  html: '<p> Registros eliminados satisfactoriamente. </p>',
											  showCancelButton: false,
											  confirmButtonText: BTN_ACEPTAR_MENU,
											  icon: 'success'
				 						}).then((result) => {
				 							$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
				 						});
							    	}else{
							    		
							    		Swal.fire({
					 						  title: MSG_ERROR_OPERACION_MENU,
					 						 html: '<p>'+data.mensajeError+'</p>',
												  showCancelButton: false,
												  confirmButtonText: BTN_ACEPTAR_MENU,
												  icon: 'error'
					 						}).then((result) => {
					 							// $('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
					 						});
							    	}
							    	
							    	
							    }
							});
					  } else if (result.isDenied) {
						 
					  }
					}) 
				
			}
		}catch(e){
			alert('eliminarOrdenes()_'+e);
		}
	}
	
	
	function eliminarCartaPorte(){
		try{
			var folioOrden = getSelections();
			if (folioOrden == ''){
				Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                //html: '<p>Es necesario seleccionar un registro para eliminar carta porte.</p>',	
		                html: '<p>'+VISOR_MSG17+'</p>',
		                icon: 'info'
		            });
			}else{
				var arrFolios = folioOrden.split(";");
				if (arrFolios.length >= 3){
					Swal.fire({
		                title: MSG_ERROR_OPERACION_MENU,
		                // html: '<p>Debe seleccionar solo un registro para eliminar carta porte.</p>',
		                html: '<p>'+VISOR_MSG18+'</p>',
		                icon: 'info'
		            });
				}else{
					var numeroOrden = arrFolios[0];
					
					Swal.fire({
						  icon : 'question',
						 // title: '¿Estas seguro de eliminar la carta porte ?',
						  html: '<p>'+VISOR_MSG19+'</p>',
						  //text: 'Orden de Compra : ',
						  showDenyButton: true,
						  showCancelButton: false,
						  confirmButtonText: BTN_ACEPTAR_MENU,
						  denyButtonText: BTN_CANCELAR_MENU,
						}).then((result) => {
						  if (result.isConfirmed) {
							  
								$.ajax({
									url  : '/siarex247/visor/tablero/eliminaCartaPorte.action',
									type : 'POST', 
									data : {
										folioOrden : numeroOrden
									},
									dataType : 'json',
									success  : function(data) {
										if($.isEmptyObject(data)) {
										} else {
											if (data.codError == "000"){
									    		Swal.fire({
						 						  title: MSG_OPERACION_EXITOSA_MENU,
						 						 // html: '<p>La Carta Porte se ha eliminado satisfactoriamente. </p>',
						 						  html: '<p>'+VISOR_MSG20+'</p>',
													  showCancelButton: false,
													  confirmButtonText: BTN_ACEPTAR_MENU,
													  icon: 'success'
						 						}).then((result) => {
						 							$('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
						 						});
									    	}else{
									    		
									    		Swal.fire({
							 						  title: MSG_ERROR_OPERACION_MENU,
							 						 html: '<p>'+data.mensajeError+'</p>',
														  showCancelButton: false,
														  confirmButtonText: BTN_ACEPTAR_MENU,
														  icon: 'error'
							 						}).then((result) => {
							 							// $('#tablaDetalleVisor').DataTable().ajax.reload(null,false);
							 						});
									    	}
										}
									},
									error : function(xhr, ajaxOptions, thrownError) {
										alert('eliminarCartaPorte()_'+thrownError);
									}
								});
						  } else if (result.isDenied) {
							 
						  }
						}) 
				}
			}
		}catch(e){
			alert('eliminarCartaPorte()_'+e);
		}
	}
	

  function exportarPlantilla(){
	  try{
		  var foliosExportar = getSelectionsEmpresa();
		  /*
		  if (foliosExportar == ''){
			  foliosExportar = getElementosDatatable();
		  }
		  
		  if (foliosExportar == ''){
			  Swal.fire({
	                title: MSG_ERROR_OPERACION_MENU,
	               // html: '<p>Es necesario seleccionar al menos un registro para exportar.</p>',	
	                html: '<p>'+VISOR_MSG5+'</p>',
	                icon: 'info'
	            });
		  }else{*/
		  
		  	var tipoMoneda = obtenerTipoMoneda();
			var estatusOrden = obtenerEstatusOrden();
			var rfc = obtenerRfc();
			var razonSocial = obtenerRazonSocial();
			var uuid = obtenerUuid();
			var folioEmpresa = obtenerFolioEmpresa();
			var serieFolio = obtenerSerieFolio();
			var fechaInicial= obtenerFechaIni();
			var fechaFinal = obtenerFechaFin();
			
		  
			  $('#tipoMoneda_Exportar_Plantilla').val(tipoMoneda);
			  $('#estatusOrden_Exportar_Plantilla').val(estatusOrden);
			  $('#rfc_Exportar_Plantilla').val(rfc);
			  $('#razonSocial_Exportar_Plantilla').val(razonSocial);
			  $('#uuid_Exportar_Plantilla').val(uuid);
			  $('#folioEmpresa_Exportar_Plantilla').val(folioEmpresa);
			  $('#serieFolio_Exportar_Plantilla').val(serieFolio);
			  $('#fechaInicial_Exportar_Plantilla').val(fechaInicial);
			  $('#fechaFinal_Exportar_Plantilla').val(fechaFinal);
		  
		  
			  $('#foliosExportar_Exportar_Plantilla').val(foliosExportar);
			  document.frmExportarPlantilla.submit();
		  //}
		  
	  }catch(e){
		  alert('exportarPlantillas()_'+e);
	  }
  }
  
  
  
  function exportarFacturas(){
	  try{
		  var foliosExportar = getSelectionsEmpresa();
		  /*
		  if (foliosExportar == ''){
			  foliosExportar = getElementosDatatable();
			  alert('foliosExportar Abajo===>'+foliosExportar);
		  }
		  */
		  
		  /* if (foliosExportar == ''){
			  Swal.fire({
	                title: MSG_ERROR_OPERACION_MENU,
	                //html: '<p>Es necesario seleccionar al menos un registro para exportar.</p>',	
	                html: '<p>'+VISOR_MSG5+'</p>',
	                icon: 'info'
	            });
		  }else{ */
			  
			    var tipoMoneda = obtenerTipoMoneda();
				var estatusOrden = obtenerEstatusOrden();
				var rfc = obtenerRfc();
				var razonSocial = obtenerRazonSocial();
				var uuid = obtenerUuid();
				var folioEmpresa = obtenerFolioEmpresa();
				var serieFolio = obtenerSerieFolio();
				var fechaInicial= obtenerFechaIni();
				var fechaFinal = obtenerFechaFin();
				
			  
			  $('#foliosExportar_Facturas').val(foliosExportar);
			  $('#tipoMoneda_Facturas').val(tipoMoneda);
			  $('#estatusOrden_Facturas').val(estatusOrden);
			  $('#rfc_Facturas').val(rfc);
			  $('#razonSocial_Facturas').val(razonSocial);
			  $('#uuid_Facturas').val(uuid);
			  $('#folioEmpresa_Facturas').val(folioEmpresa);
			  $('#serieFolio_Facturas').val(serieFolio);
			  $('#fechaInicial_Facturas').val(fechaInicial);
			  $('#fechaFinal_Facturas').val(fechaFinal);
			  
			  document.frmExportarFacturas.submit();
		//  }
		  
	  }catch(e){
		  alert('exportarPlantillas()_'+e);
	  }
  }
  
  
  function exportarlayOut(){
	  try{
		  var foliosExportar = getSelectionsEmpresa();
		  /*
		  if (foliosExportar == ''){
			  foliosExportar = getElementosDatatable();
		  }
		  
		  if (foliosExportar == ''){
			  Swal.fire({
	                title: MSG_ERROR_OPERACION_MENU,
	               // html: '<p>Es necesario seleccionar al menos un registro para exportar.</p>',
	                html: '<p>'+VISOR_MSG5+'</p>',
	                icon: 'info'
	            });
		  }else{*/
		  
		  		var tipoMoneda = obtenerTipoMoneda();
				var estatusOrden = obtenerEstatusOrden();
				var rfc = obtenerRfc();
				var razonSocial = obtenerRazonSocial();
				var uuid = obtenerUuid();
				var folioEmpresa = obtenerFolioEmpresa();
				var serieFolio = obtenerSerieFolio();
				var fechaInicial= obtenerFechaIni();
				var fechaFinal = obtenerFechaFin();
				
			  
			  $('#foliosExportar_LayOut').val(foliosExportar);
			  $('#tipoMoneda_LayOut').val(tipoMoneda);
			  $('#estatusOrden_LayOut').val(estatusOrden);
			  $('#rfc_LayOut').val(rfc);
			  $('#razonSocial_LayOut').val(razonSocial);
			  $('#uuid_LayOut').val(uuid);
			  $('#folioEmpresa_LayOut').val(folioEmpresa);
			  $('#serieFolio_LayOut').val(serieFolio);
			  $('#fechaInicial_LayOut').val(fechaInicial);
			  $('#fechaFinal_LayOut').val(fechaFinal);
			  
		  
			  $('#foliosExportar_LayOut').val(foliosExportar);
			  document.frmexportarLayOut.submit();
		  //}
		  
	  }catch(e){
		  alert('exportarPlantillas()_'+e);
	  }
  }
  
  

  
  