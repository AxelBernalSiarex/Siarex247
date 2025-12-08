


function cargaCmbSupuestos(){
		try {
			$('#cmbSupuestos').empty();
			$('#cmbSupuestos').append($('<option></option>').attr('selected', 'selected').attr('value', '').text('Selecciones una Opcion'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'CANCELADOS').text('Cancelados'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'CANCELADOS0715').text('Cancelados 0715'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'CANCELADOS POR INCOSTEABILIDAD').text('CANCELADOS POR INCOSTEABILIDAD'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'CANCELADOS POR INSOLVENCIA').text('CANCELADOS POR INSOLVENCIA'));
			
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'CONDONADOS').text('Condonados'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'Artículo 74').text('Articulo 74'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'CONDONADOS0715').text('Condonados 0715'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'DEFINITIVO').text('Definitivo'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'DESVIRTUADO').text('Desvirtuado'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'EXIGIBLES').text('Exigibles'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'FIRMES').text('Firmes'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'NO LOCALIZADOS').text('NO Localizados'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'PRESUNTO').text('Presunto'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'RETORNO INVERSIONES').text('Retorno Inversiones'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'SENTENCIA FAVORABLE').text('Sentencia Favorable'));
			$('#cmbSupuestos').append($('<option></option>').attr('value', 'SENTENCIAS').text('Sentencias'));
		}
		catch(e) {
			e = null;
		}
	}

	function cargaCmbNombreArticulo(){
		try {
			$('#cmbNombreArticulo').empty();
			$('#cmbNombreArticulo').append($('<option></option>').attr('selected', 'selected').attr('value', '').text('Selecciones una Opcion'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Cancelados.csv').text('Cancelados'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Cancelados_07_15.csv').text('Cancelados 0715'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Condonadosart146BCFF.csv').text('Condonados art 146BCFF'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Condonadosart21CFF.csv').text('Condonados art 21CFF'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Condonadosart74CFF.csv').text('Condonados art 74CFF'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'CondonadosporDecreto.csv').text('Condonados Decreto'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Condonados_07_15.csv').text('Condonados 0715'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Definitivos.csv').text('Definitivos'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Desvirtuados.csv').text('Desvirtuados'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Eliminados.csv').text('Eliminados'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Exigibles.csv').text('Exigibles'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Firmes.csv').text('Firmes'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Nolocalizados.csv').text('No Localizados'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Presuntos.csv').text('Presuntos'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Retornoinversiones.csv').text('Retorno Inversiones'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'Sentencias.csv').text('Sentencias'));
			$('#cmbNombreArticulo').append($('<option></option>').attr('value', 'SentenciasFavorables.csv').text('Sentencias Favorables'));
		}
		catch(e) {
			e = null;
		}
	}
	
	
	
	function obtenerRS(){
		return $('#razonSocialListaNegra').val();
	}
	
	function obtenerRLN(){
		return $('#rfcListaNegra').val();
	}
	
	function obtenerIDS(){
		return $('#cmbSupuestos').val();
	}
	
	function obtenerIDA(){
		return $('#cmbNombreArticulo').val();
	}
	
	function obtenerALN(){
		return $('#anioListaNegra').val() == '' ? '0' : $('#anioListaNegra').val();
	}
	
	
	function refrescar(){
		var annio = obtenerALN(); 
		if (annio == '' || annio.trim() == '' ||  annio == 0){
			Swal.fire({
	                title: '¡Operación No Existosa!',
	                html: '<p>Debe especificar el año de consulta.</p>',	
	                icon: 'info'
	            });
		}else{
			// $('#tablaDetalle').DataTable().ajax.reload(null,true);
			var annioConsulta = $('#anioListaNegra').val();
			$("#detColumnas").load('/siarex247/jsp/estatusXML/listaNegra/listaColumnas.jsp?annioConsulta='+annioConsulta);
		}
		
	}

	
	
	function generarCSV(){
		 try{
			 
			 	var razonSocial =  obtenerRS();
			 	var rfcListaNegra =  obtenerRLN();
			 	var idSupuesto =  obtenerIDS();
			 	var idNombreArticulo = obtenerIDA();
			 	var anioListaNegra = obtenerALN();
				
			 	  $.ajax({
						url  : '/siarex247/cumplimientoFiscal/listaNegra/generarCSV.action',
						data : {
							razonSocial: razonSocial,
							rfcListaNegra: rfcListaNegra,
							idSupuesto: idSupuesto,
							idNombreArticulo: idNombreArticulo,
							anioListaNegra: anioListaNegra
						},
						type : 'POST', 
						beforeSend: function( xhr ) {
		        			$('#overSeccionLista').css({display:'block'});
		        		},
		        		complete: function(jqXHR, textStatus){
				    		  $('#overSeccionLista').css({display:'none'});
					    },
						dataType : 'json',
						success  : function(data) {
							if($.isEmptyObject(data)) {
							   
							} else {
								
								if (data.codError == '000') {
				 					Swal.fire({
				 						  title: '¡Operación Exitosa!',
				 						 html: '<p>El archivo CSV se genero satisfactoriamente.</p>',
											  showCancelButton: false,
											  confirmButtonText: 'Aceptar',
											  icon: 'success'
				 						}).then((result) => {
				 							abrirCSV(data.mensajeError);
				 						});
								} else {
									Swal.fire({
			  			                title: '¡Operación No Existosa!',
			  			                html: '<p>'+data.mensajeError+'</p>',	
			  			                icon: 'error'
			  			            });
								}
							}
						},
						error : function(xhr, ajaxOptions, thrownError) {
							alert('generarCSV()_'+thrownError);
						}
					});	
		 }catch(e){
			 alert('generarCSV()_'+e);
		 }
		 
	 }
	
	
	function abrirModal(){
		try{
			iniciaFormBusqueda();
			$('#myModalBusqueda').modal('show');
			$('#txtRFC').focus();
			
		}catch(e){
			alert('abrirModal()_'+e);
		}
	}
	
	
	function iniciaFormBusqueda(){
		/* Reset al Formulario */
		$('#error_ListaNegra').hide();
		$('#exito_ListaNegra').hide();
		$('#descargarDocumento').hide();
		
		$("#form-Busqueda").find('.has-success').removeClass("has-success");
	    $("#form-Busqueda").find('.has-error').removeClass("has-error");
		$('#form-Busqueda')[0].reset(); 
		$('#form-Busqueda').removeClass("was-validated"); 
		   
	}
	
	
	function buscarRFC(){
		$('#error_ListaNegra').hide();
		$('#exito_ListaNegra').hide();
		$('#descargarDocumento').hide();
		var rfcListaNegra = $('#txtRFC').val();
		var razonSocial = $('#txtRazonSocial').val();
		$.ajax({
			url: '/siarex247/cumplimientoFiscal/listaNegra/buscarRFC.action',
			type : 'POST', 
			data : {
				rfcListaNegra : rfcListaNegra,
				razonSocial   : razonSocial
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					if (data.recordsTotal >= 1){
						$('#error_ListaNegra').show();
						$('#existeListaNegra_Buscar').val('S');
					}else{
						$('#exito_ListaNegra').show();
						$('#existeListaNegra_Buscar').val('N');
					}
					$('#descargarDocumento').show();
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('buscarRFC()_'+thrownError);
			}
		});	
    }

	function descargarPDF(){
		try {
			var rfcListaNegra = $('#txtRFC').val();
			var razonSocial = $('#txtRazonSocial').val();
			var existe = $('#existeListaNegra_Buscar').val();
			
			$('#rfcListaNegra_Buscar').val(rfcListaNegra);
			$('#razonListaNegra_Buscar').val(razonSocial);
			
			 document.frmDescargarPDF.submit();
		     
		}catch(e){
			alert('descargarPDF()_'+e);
		}
	}
	
	 function abrirCSV(nombreArchivo){
	 	 try{
	 		 window.open('/siarex247/files/'+nombreArchivo,'_blank');
	 	 }catch(e){
	 		 alert('abrirCSV()_'+e);
	 	 }
	 }