<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">
    
    <script>
      var isRTL = JSON.parse(localStorage.getItem('isRTL'));
      if (isRTL) {
        var linkDefault = document.getElementById('style-default');
        var userLinkDefault = document.getElementById('user-style-default');
        linkDefault.setAttribute('disabled', true);
        userLinkDefault.setAttribute('disabled', true);
        document.querySelector('html').setAttribute('dir', 'rtl');
      } else {
        var linkRTL = document.getElementById('style-rtl');
        var userLinkRTL = document.getElementById('user-style-rtl');
        linkRTL.setAttribute('disabled', true);
        userLinkRTL.setAttribute('disabled', true);
      }
    </script>
	
</head>


<form id="form-Catalogo-Constancia" class="was-validated" novalidate>
	<input type="hidden" name="idRegistro" id="idRegistro_Catalogo" value="0" >
	<input type="hidden" name="cedulaFiscal" id="cedulaFiscal_Catalogo" value="0" >
	
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="modal-title-catalogo">title</h5>
					<div id="overSeccion" class="overlay" style="display: none;text-align: right;">
						<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
					</div>
	   				
				</div>
				<div class="p-4 pb-3">
				
					<div class="card overflow-hidden">
			
						<div class="card-header p-0 scrollbar-overlay border-bottom">
							<ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabAlarmas" role="tablist">
								<li class="nav-item text-nowrap" role="presentation">
									<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1 active" id="tabInfoPrincipal" data-bs-toggle="tab" href="#infoPrincipal" role="tab" aria-controls="infoPrincipal" aria-selected="true">
										<span class="fas fa-address-card text-600"></span>
										<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CAT_CONSTANCIA_SITUACION_ETQ37">Informaci&oacute;n de Constancia de Situacion Fiscal</h6>
									</a>
								</li>
								
							</ul>
						</div>
		                
		                <div class="tab-content">
		                	
							<!-- Informacion Principal -->
		                	<div class="card-body bg-light tab-pane active" id="infoPrincipal" role="tabpanel" aria-labelledby="tabInfoPrincipal">
								
								<div class="accordion" id="accordion1">
									
									<div class="accordion-item"><!-- accordion-item Datos Generales -->
    									<h2 class="accordion-header" id="heading1">
      										<button class="accordion-button bg-200" type="button" data-bs-toggle="collapse" data-bs-target="#collapse1" aria-expanded="true" aria-controls="collapse1" id="CAT_CONSTANCIA_SITUACION_ETQ38">Constancia de Situaci&oacute;n Fiscal</button>
    									</h2>
    									<div class="accordion-collapse collapse show" id="collapse1" aria-labelledby="heading1" data-bs-parent="#accordion1">
      										<div class="accordion-body">
      											
      											<div class="mb-2 row">
													<label class="col-sm-4 col-form-label" for="reemplazarConstancia" id="CAT_CONSTANCIA_SITUACION_ETQ39">Reemplazar Constancia</label>
													<div class="col-sm-8">
								   						<div class="form-check form-switch">
								   							<input class="form-check-input validarHabilitar" id="reemplazarConstancia" name="reemplazarConstancia" type="checkbox"/>
														</div>
								 					</div>
								 				</div>
												
												<div class="mb-2 row">
													<label class="col-sm-4 col-form-label" for="filePDF" id="CAT_CONSTANCIA_SITUACION_ETQ40">Seleccionar archivo .PDF</label>
													<div class="col-sm-8">
														<input class="form-control form-control-sm validarHabilitar" type="file" id="filePDF" name="filePDF" accept="application/pdf" />
								 					</div>
												</div>
																			
									            
      										</div>
    									</div>
  									</div> 
  									
  									
  									<div class="accordion-item"> 
										<h2 class="accordion-header" id="heading2">
											<button class="accordion-button bg-200 collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse2" aria-expanded="true" aria-controls="collapse2" id="CAT_CONSTANCIA_SITUACION_ETQ41">Informaci&oacute;n de la Constancia</button>
										</h2>
										<div class="accordion-collapse collapse" id="collapse2" aria-labelledby="heading2" data-bs-parent="#accordion1">
											<div class="accordion-body">
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="rfc" id="CAT_CONSTANCIA_SITUACION_ETQ1_RFC"> RFC</label>
													<div class="col-sm-3">
								   						<input id="rfc" name="rfc" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
								 					<label class="col-sm-3 col-form-label" for="razonSocial" id="CAT_CONSTANCIA_SITUACION_ETQ2_RAZONSOCIAL">Raz&oacute;n Social</label>
													<div class="col-sm-4">
								   						<input id="razonSocial" name="razonSocial" class="form-control validarHabilitar" type="text" />
								 					</div>
													
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="regimenCapital" id="CAT_CONSTANCIA_SITUACION_ETQ26_REGIMEN_CAPITAL">R&eacute;gimen Capital</label>
													<div class="col-sm-3">
								   						<input id="regimenCapital" name="regimenCapital" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
								 					<label class="col-sm-3 col-form-label" for="fechaInicioOperaciones" id="CAT_CONSTANCIA_SITUACION_ETQ17_FECHA_INICIO_OPERACIONES">Fecha de Inicio de Operaciones</label>
													<div class="col-sm-4">
										                <div class="form-group">
										                  <input class="form-control datetimepicker flatpickr-input active" id="fechaInicioOperaciones" name=fechaInicioOperaciones type="text" placeholder="dd-mm-yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
										                </div>
									                </div>
													
												</div>
												
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="nombre" id="CAT_CONSTANCIA_SITUACION_ETQ11_NOMBRE">Nombre</label>
													<div class="col-sm-3">
								   						<input id="nombre" name="nombre" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
								 					<label class="col-sm-3 col-form-label" for="apellidoPaterno" id="CAT_CONSTANCIA_SITUACION_ETQ12_APELLIDO_PATERNO">Apellido Paterno</label>
													<div class="col-sm-4">
								   						<input id="apellidoPaterno" name="apellidoPaterno" class="form-control validarHabilitar" type="text" />
								 					</div>
													
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="apellidoMaterno" id="CAT_CONSTANCIA_SITUACION_ETQ13_APELLIDO_MATERNO">Apellido Materno</label>
													<div class="col-sm-3">
								   						<input id="apellidoMaterno" name="apellidoMaterno" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
								 					<label class="col-sm-3 col-form-label" for="situacionContribuyente" id="CAT_CONSTANCIA_SITUACION_ETQ15_SITUACION_CONTRIBUYENTE">Situaci&oacute;n del Contribuyente</label>
													<div class="col-sm-4">
								   						<input id="situacionContribuyente" name="situacionContribuyente" class="form-control validarHabilitar" type="text" />
								 					</div>
													
												</div>
												<div class="mb-2 row">
								 					<label class="col-sm-2 col-form-label" for="curp" id="CAT_CONSTANCIA_SITUACION_ETQ16_CURP">CURP</label>
													<div class="col-sm-3">
								   						<input id="curp" name="curp" class="form-control validarHabilitar" type="text" />
								 					</div>
													<label class="col-sm-3 col-form-label" for="fechaUltimoCambio" id="CAT_CONSTANCIA_SITUACION_ETQ27_FECHA_ULTIMO_CAMBIO_SITUACION">Fecha de Ultimo Cambio de Situaci&oacute;n</label>
													<div class="col-sm-4">
										                <div class="form-group">
										                  <input class="form-control datetimepicker flatpickr-input active" id="fechaUltimoCambio" name=fechaUltimoCambio type="text" placeholder="dd-mm-yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
										                </div>
									               </div>
													
												</div>
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="estado" id="CAT_CONSTANCIA_SITUACION_ETQ28_ENTIDAD_FEDERATIVA">Entidad Federativa</label>
													<div class="col-sm-3">
								   						<input id="estado" name="estado" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
								 					<label class="col-sm-3 col-form-label" for="ciudad" id="CAT_CONSTANCIA_SITUACION_ETQ29_MUNICIPIO">Municipo</label>
													<div class="col-sm-4">
								   						<input id="ciudad" name="ciudad" class="form-control validarHabilitar" type="text" />
								 					</div>
													
												</div>
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="colonia" id="CAT_CONSTANCIA_SITUACION_ETQ30_COLONIA">Colonia</label>
													<div class="col-sm-3">
								   						<input id="colonia" name="colonia" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
								 					<label class="col-sm-3 col-form-label" for="tipoVialidad" id="CAT_CONSTANCIA_SITUACION_ETQ31_TIPO_VIALIDAD">Tipo de Vialidad</label>
													<div class="col-sm-4">
								   						<input id="tipoVialidad" name="tipoVialidad" class="form-control validarHabilitar" type="text" />
								 					</div>
													
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="nombreVialidad" id="CAT_CONSTANCIA_SITUACION_ETQ32_NOMBRE_VIALIDAD">Nombre de Vialidad</label>
													<div class="col-sm-10">
								   						<input id="nombreVialidad" name="nombreVialidad" class="form-control validarHabilitar" type="text" />
								 					</div>
												</div>
												
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="numExterior" id="CAT_CONSTANCIA_SITUACION_ETQ33_NUMERO_EXTERIOR">N&uacute;mero Exterior</label>
													<div class="col-sm-3">
								   						<input id="numExterior" name="numExterior" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
								 					<label class="col-sm-3 col-form-label" for="numInterior" id="CAT_CONSTANCIA_SITUACION_ETQ34_NUMERO_INTERIOR">N&uacute;mero Interior</label>
													<div class="col-sm-4">
								   						<input id="numInterior" name="numInterior" class="form-control validarHabilitar" type="text" />
								 					</div>
													
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="codigoPostal" id="CAT_CONSTANCIA_SITUACION_ETQ4_CODIGO_POSTAL">C&oacute;digo Postal</label>
													<div class="col-sm-3">
								   						<input id="codigoPostal" name="codigoPostal" class="form-control validarHabilitar" type="text" />
								 					</div>
								 					
								 					<label class="col-sm-3 col-form-label" for="correo" id="CAT_CONSTANCIA_SITUACION_ETQ18_CORREO_ELECTRONICO">Correo electr&oacute;nico</label>
													<div class="col-sm-4">
								   						<input id="correo" name="correo" class="form-control validarHabilitar" type="email" />
								 					</div>
													
												</div>
												
												<div class="mb-2 row">
													<label class="col-sm-2 col-form-label" for="fechaAlta" id="CAT_CONSTANCIA_SITUACION_ETQ36_FECHA_ALTA">Fecha de Alta</label>
													<div class="col-sm-3">
										                <div class="form-group">
										                  <input class="form-control datetimepicker flatpickr-input active" id="fechaAlta" name=fechaAlta type="text" placeholder="dd-mm-yyyy" data-options='{"disableMobile":true}' readonly="readonly"/>
										                </div>
									                </div>
								 					
								 					<label class="col-sm-3 col-form-label" for="regimenFiscal" id="CAT_CONSTANCIA_SITUACION_ETQ35_REGIMEN_FISCAL">R&eacute;gimen</label>
													<div class="col-sm-4">
														<div class="form-group">
														<select class="form-select validarHabilitar" id="regimenFiscal" name="regimenFiscal">
														    
														</select>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div><!-- accordion-item Datos de la Cuenta -->
								</div><!-- accordion -->
		                	</div><!-- Informacion Principal -->
		                </div>
					</div>
				
				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary">Guardar</button>
				<button class="btn btn-secondary" type="button" id="CAT_CONSTANCIA_SITUACION_Boton_Cerrar" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>



 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   
	   flatpickr(fechaUltimoCambio, {
	   	      minDate: '1920-01-01', 
	   	      //dateFormat : "d-m-Y", 
	   	      dateFormat : "d-m-Y",
	   	      locale: {
	   	        firstDayOfWeek: 1,
	   	        weekdays: {
	   	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
	   	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],         
	   	        }, 
	   	        months: {
	   	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Оct', 'Nov', 'Dic'],
	   	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	   	        },
	   	      },
	   	    }); 
	   
	   flatpickr(fechaInicioOperaciones, {
	   	      minDate: '1920-01-01', 
	   	      //dateFormat : "d-m-Y", 
	   	      dateFormat : "d-m-Y",
	   	      locale: {
	   	        firstDayOfWeek: 1,
	   	        weekdays: {
	   	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
	   	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],         
	   	        }, 
	   	        months: {
	   	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Оct', 'Nov', 'Dic'],
	   	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	   	        },
	   	      },
	   	    }); 
	   
	   flatpickr(fechaAlta, {
	   	      minDate: '1920-01-01', 
	   	      //dateFormat : "d-m-Y", 
	   	      dateFormat : "d-m-Y",
	   	      locale: {
	   	        firstDayOfWeek: 1,
	   	        weekdays: {
	   	          shorthand: ['Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'],
	   	          longhand: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],         
	   	        }, 
	   	        months: {
	   	          shorthand: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Оct', 'Nov', 'Dic'],
	   	          longhand: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
	   	        },
	   	      },
	   	    }); 
	   
	   
	   
	   $("#form-Catalogo-Constancia").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });

	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Catalogo-Constancia').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   var idRegistro = $('#idRegistro_Catalogo').val();
			   //if (idRegistro == 0){
				  guardarCatalogo('reemplazar');
			   /*}else{
				   actualizarCatalogo();
			   }*/
				  
			},
           errorPlacement: function (error, e) {
        	   e.parents('.form-group').append(error);
           },
           highlight: function (e) {
               $(e).closest('.form-group').removeClass('has-success has-error').addClass('has-error');
           },
           success: function (e) {
               e.closest('.form-group').removeClass('has-success has-error').addClass('has-success');
           }, rules:  {
               select: {required: true}
           }, messages: {
               select: {required: 'error'}
           }
       }).resetForm(); 
	   	   	   
	   calcularEtiquetasCatalogoConstanciasModal();
	   	   
	});
	

	 function calcularEtiquetasCatalogoConstanciasModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CAT_CONSTANCIA_SITUACION'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ1_RFC").innerHTML = data.ETQ1;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ2_RAZONSOCIAL").innerHTML = data.ETQ2;
						// document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ4_CODIGO_POSTAL").innerHTML = data.ETQ4;
						// document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ11_NOMBRE").innerHTML = data.ETQ11;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ12_APELLIDO_PATERNO").innerHTML = data.ETQ12;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ13_APELLIDO_MATERNO").innerHTML = data.ETQ13;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ15_SITUACION_CONTRIBUYENTE").innerHTML = data.ETQ15;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ16_CURP").innerHTML = data.ETQ16;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ17_FECHA_INICIO_OPERACIONES").innerHTML = data.ETQ17;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ18_CORREO_ELECTRONICO").innerHTML = data.ETQ18;
						// document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ23").innerHTML = data.ETQ23;
						// document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ24").innerHTML = data.ETQ24;
						
						
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ26_REGIMEN_CAPITAL").innerHTML = data.ETQ26;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ27_FECHA_ULTIMO_CAMBIO_SITUACION").innerHTML = data.ETQ27;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ28_ENTIDAD_FEDERATIVA").innerHTML = data.ETQ28;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ29_MUNICIPIO").innerHTML = data.ETQ29;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ30_COLONIA").innerHTML = data.ETQ30;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ31_TIPO_VIALIDAD").innerHTML = data.ETQ31;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ32_NOMBRE_VIALIDAD").innerHTML = data.ETQ32;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ33_NUMERO_EXTERIOR").innerHTML = data.ETQ33;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ34_NUMERO_INTERIOR").innerHTML = data.ETQ34;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ35_REGIMEN_FISCAL").innerHTML = data.ETQ35;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ36_FECHA_ALTA").innerHTML = data.ETQ36;
						
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ37").innerHTML = data.ETQ37;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ38").innerHTML = data.ETQ38;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ39").innerHTML = data.ETQ39;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ40").innerHTML = data.ETQ40;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ41").innerHTML = data.ETQ41;
						
						document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("CAT_CONSTANCIA_SITUACION_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 

	 function cargaProveedores(idProveedor) {
			try {
				$('#cmbProveedor').empty();
				$.ajax({
		           url:  '/siarex247/catalogos/proveedores/comboProveedores.action',
		           data : {
		        	   tipoProveedor : 'MEX'
		           },
		           type: 'POST',
		            dataType : 'json',
				    success: function(data){
				    	$('#cmbProveedor').empty();
				    	$.each(data.data, function(key, text) {
				    		var descripcion = text.rfc + ' - ' + text.razonSocial;
					    	if (idProveedor == text.claveRegistro){
					    		$('#cmbProveedor').append($('<option></option>').attr('selected', 'selected').attr('value', text.claveRegistro).text(descripcion));
					    	}else{
					    		$('#cmbProveedor').append($('<option></option>').attr('value', text.claveRegistro).text(descripcion));
					    	}
				      	});
				    }
				});
			}catch(e) {
				alert("cargaProveedores()_"+e);
			} 
		}
	 
 </script>
</html>