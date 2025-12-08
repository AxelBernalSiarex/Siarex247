
	function iniciaTabAlarmaCompras() {
		
		$('#diaEjecucion').select2({
			//dropdownParent: $('#myModalDetalle .modal-body'),
			theme: 'bootstrap-5'
		});
		
		
		/* Reset al Formulario */ 
		$("#form-AlarmaCompras").find('.has-success').removeClass("has-success");
	    $("#form-AlarmaCompras").find('.has-error').removeClass("has-error");
		$('#form-AlarmaCompras')[0].reset(); 
		$('#form-AlarmaCompras').removeClass("was-validated"); 
		
		/* Funciones para cargar Combos que esten en el formulario */
		cargaListaProcesar('');
		
	}
	
	function cargaListaProcesar(id) {
  		try {
  			
  			$('#diaEjecucion').empty();
		    $('#diaEjecucion').append($('<option></option>').attr('value', '').text('Seleccione una opción ...'));
		    
		    if (id == '001'){
		    	$('#diaEjecucion').append($('<option></option>').attr('selected', 'selected').attr('value', '001').text('DIARIO'));
		    }else{
		    	$('#diaEjecucion').append($('<option></option>').attr('value', '001').text('DIARIO'));
		    }
		    if (id == '002'){
		    	$('#diaEjecucion').append($('<option></option>').attr('selected', 'selected').attr('value', '002').text('SEMANAL'));
		    }else{
		    	$('#diaEjecucion').append($('<option></option>').attr('value', '002').text('SEMANAL'));
		    }
		    if (id == '003'){
		    	$('#diaEjecucion').append($('<option></option>').attr('selected', 'selected').attr('value', '003').text('QUINCENAL'));
		    }else{
		    	$('#diaEjecucion').append($('<option></option>').attr('value', '003').text('QUINCENAL'));
		    }
		    if (id == '004'){
		    	$('#diaEjecucion').append($('<option></option>').attr('selected', 'selected').attr('value', '004').text('MENSUAL'));
		    }else{
		    	$('#diaEjecucion').append($('<option></option>').attr('value', '004').text('MENSUAL'));
		    }
		    if (id == '005'){
		    	$('#diaEjecucion').append($('<option></option>').attr('selected', 'selected').attr('value', '005').text('BIMESTRAL'));
		    }else{
		    	$('#diaEjecucion').append($('<option></option>').attr('value', '005').text('BIMESTRAL'));
		    }
		    if (id == '006'){
		    	$('#diaEjecucion').append($('<option></option>').attr('selected', 'selected').attr('value', '006').text('TRIMESTRAL'));
		    }else{
		    	$('#diaEjecucion').append($('<option></option>').attr('value', '006').text('TRIMESTRAL'));
		    }
		    if (id == '007'){
		    	$('#diaEjecucion').append($('<option></option>').attr('selected', 'selected').attr('value', '007').text('ANUAL'));
		    }else{
		    	$('#diaEjecucion').append($('<option></option>').attr('value', '007').text('ANUAL'));
		    }
		    
		     			
  		}catch(e) {
  			alert("cargaListaProcesar()_"+e);
  		} 
  	}   	
		
	function consultaConfiguracion(){
		$.ajax({
			url  : '/siarex247/configSistema/alertaBoveda/datosConfProceso.action',
			type : 'POST', 
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
					
				} else {//console.log('DATA: ', data);
					//$('#diaEjecucion').val(data.diaEjecucion);
					cargaListaProcesar(data.diaEjecucion);
					$('#txtSubject').val(data.subject);
					// $('#txtMsjError').val(data.mensajeError);
					
					$('#txtMsjError').summernote('code', data.mensajeError);
					$('#txtDestinatario1').val(data.destinatario1);
					$('#txtDestinatario2').val(data.destinatario2);
					$('#txtDestinatario3').val(data.destinatario3);
					$('#txtDestinatario4').val(data.destinatario4);
					$('#txtDestinatario5').val(data.destinatario5);
					
					if (data.adjuntar == "S"){
						$('#swtAdjuntarArchivo')[0].checked = true;
						$('#swtAdjuntarArchivoLbl').html("SI"); 
					}else{
						$('#swtAdjuntarArchivo')[0].checked = false;
						$('#swtAdjuntarArchivoLbl').html("NO");
					}
					
					if (data.activar == "S"){
						$('#swtActivarProceso')[0].checked = true;
						$('#swtActivarProcesoLbl').html("SI"); 
					}else{
						$('#swtActivarProceso')[0].checked = false;
						$('#swtActivarProcesoLbl').html("NO");
					}
					
					
					// $('#swtAdjuntarArchivo')[0].checked = data.adjuntar == "S" ? true : false;
					//$('#swtActivarProceso')[0].checked = data.activar == "S" ? true : false;
					
					
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('consultaConfiguracion()_'+thrownError);
			}
		});	
    }
	
	
	function guardarConfiguracion(){
		try{
				var adjuntar = $('#swtAdjuntarArchivo').prop('checked') ? "S": "N";
				var activar = $('#swtActivarProceso').prop('checked') ? "S": "N";
				var texto = $('#txtMsjError').summernote('code');
				
			    var formData = new FormData(document.getElementById("form-AlarmaCompras"));
	            formData.append("adjuntar", adjuntar);
	            formData.append("activar", activar);
	            formData.append("texto", texto);
	            
	            $.ajax({
	            	url  : '/siarex247/configSistema/alertaBoveda/configuracionProceso.action',
	                dataType: "json",
	                type: "post",
	                data: formData,
	                cache: false,
	                contentType: false,
		    		processData: false
	            }).done(function(data){
	            	if (data.codError == '000') {
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 // html: '<p>Alta Exitosa</p>',
	 						  	html: '<p>'+MSG_SOLICITUD_EXITOSA_MENU+'</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {

	 						});
					} else {
						Swal.fire({
  			                title: MSG_ERROR_OPERACION_MENU,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
	             });
		}
		catch(e){
			alert('guardarConfiguracion()_'+e);
		}
    }
