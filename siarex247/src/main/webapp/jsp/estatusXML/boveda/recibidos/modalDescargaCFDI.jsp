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



<form id="frmExportXML" class="was-validated" novalidate>
	
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="BOVEDA_ETQ28">Descarga de UUID</h5>
	   				<div id="overSeccionCA" class="overlay" style="display: none;text-align: right;">
						<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
					</div>
				</div>
				
				<div class="p-4 pb-0">
				   
				   <div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="modoAgrupar" id="EXPORTARXML_ETQ11">Modo de Agrupaci&oacute;n</label>
						<div class="col-sm-8">
							<div class="form-group">
								<select class="form-select" id="modoAgrupar" name="modoAgrupar" required>
									 <option value="NONE" selected=""></option>
	                                 <option value="1" id="EXPORTARXML_ETQ12">PROVEEDOR</option>
	                                 <option value="2" id="EXPORTARXML_ETQ13">TIPO DE MONEDA</option>
	                                 <option value="3" id="EXPORTARXML_ETQ14">TIPO DE MONEDA Y PROVEEDOR</option>
	                                 <option value="4" id="EXPORTARXML_ETQ15">PROVEEDOR Y TIPO DE MONEDA</option>
								</select>
							</div>
						</div>
					</div>
	
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="CORREO_RESPONSABLE" id="EXPORTARXML_ETQ16">Correo de notificaci&oacute;n al responsable</label>
						<div class="col-sm-8">
							<input id="CORREO_RESPONSABLE" name="correoResponsable" class="form-control" type="email" required />
						</div>
					</div>
	
	
					<div class="mb-2 row">
						
						<label class="col-sm-4 col-form-label" for="" id="EXPORTARXML_ETQ18">Validar Facturas en SAT</label>
						<div class="col-sm-2">
							<div class="form-check form-switch">
								<input class="form-check-input" id="validarSAT" name="validarSAT" type="checkbox"  />
							</div>
						</div>
						
					</div>
	
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="" id="EXPORTARXML_ETQ20">Validar Complemento en SAT</label>
						<div class="col-sm-2">
							<div class="form-check form-switch">
								<input class="form-check-input" id="complementoSAT" name="complementoSAT" type="checkbox"  />
							</div>
						</div>
					</div>
					
					
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="" id="EXPORTARXML_ETQ22">Validar Nota de Cr&eacute;dito en SAT</label>
						<div class="col-sm-2">
							<div class="form-check form-switch">
								<input class="form-check-input" id="notaCreditoSAT" name="notaCreditoSAT" type="checkbox"  />
							</div>
						</div>
					</div>
				</div>
				
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnProcesar_XML" class="btn btn-primary">Procesar</button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="BOVEDA_Boton_Cancelar">Cancelar</button>
			</div>
		</div>
	</div>
 </form>

 <script type="text/javascript">
 
   var MENSAJE_EXITOSO = null;
 
   $(document).ready(function() {
	   $('#modoAgrupar').select2({
			theme: "bootstrap-5",
		});
	   
	  
	   $("#frm-Carga-XML").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });

	   var elements = document.querySelectorAll('[data-input-mask]');
	    elements.forEach(function (item) {
	      var userOptions = utils.getData(item, 'input-mask');
	      var defaultOptions = {
	        showMaskOnFocus: false,
	        showMaskOnHover: false,
	        jitMasking: true
	      };

	      var maskOptions = window._.merge(defaultOptions, userOptions);

	      var inputmask = new window.Inputmask(_objectSpread({}, maskOptions)).mask(item);
	      return inputmask;
	    });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#frmExportXML').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   descargarCFDI();
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
	   	   	   
	   calcularEtiquetasDescargaCFDI();
	});
   
   

	function iniciaFormDescargaCFDI(){
		/* Reset al Formulario */ 
		$("#frmExportXML").find('.has-success').removeClass("has-success");
	    $("#frmExportXML").find('.has-error').removeClass("has-error");
		$('#frmExportXML')[0].reset(); 
		$('#frmExportXML').removeClass("was-validated"); 
		   
	}
	 
	

	function asignarCorreo(){
		try{
				
			$.ajax({
				url  : '/siarex247/seguridad/usuarios/consultaPermisos.action',
				type : 'POST', 
				data : null,
				dataType : 'json',
				success  : function(data) {
	 				if (data == null){
	 					Swal.fire({
	 						  title: '¡Operación Exitosa!',
	 						 html: '<p>Error al obtener los permisos del usuario</p>',	
								  showCancelButton: false,
								  confirmButtonText: 'Aceptar',
								  icon: 'success'
	 						}).then((result) => {
	 						});
	 					
	 				}else{
	 					$('#CORREO_RESPONSABLE').val(data.correo);
	 				}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('obtenerPermisos()_'+thrownError);
				}
			});			
			
		}catch(e){
			alert('obtenerPermisos()_'+e);
		}
	 }
	
	
	 function calcularEtiquetasDescargaCFDI(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'EXPORTARXML'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("EXPORTARXML_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("EXPORTARXML_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("EXPORTARXML_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("EXPORTARXML_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("EXPORTARXML_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("EXPORTARXML_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("EXPORTARXML_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("EXPORTARXML_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("EXPORTARXML_ETQ22").innerHTML = data.ETQ22;
						document.getElementById("btnProcesar_XML").innerHTML = BTN_PROCESAR_MENU;
						
						
						MENSAJE_EXITOSO = data.ETQ23;
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasDescargaCFDI()_1_'+thrownError);
				}
			});	
		}
	 
	
 </script>
</html>