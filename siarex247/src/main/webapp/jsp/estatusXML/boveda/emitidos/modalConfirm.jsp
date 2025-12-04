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



<form id="frmConfirmEmail_Emitidos" class="was-validated" novalidate>
	
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="BOVEDA_EMITIDOS_ETQ31">Introduzca una dirección de correo electrónico</h5>
				</div>
				<div class="p-4 pb-0">
					<div class="mb-2 row">
						<label class="col-sm-12 col-form-label" for="" id="BOVEDA_EMITIDOS_ETQ32">La cantidad permitida para descargar los XML excede, favor de introducir un correo electrónico en el cual le haremos llegar la información requerida </label>						
					</div>
				    <div class="mb-2 row">
						<div class="col-sm-12">
	   						<input id="emailNotificacion" name="emailNotificacion" placeholder="" class="form-control" type="email" required />
	 					</div>
					</div>

				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="BOVEDA_EMITIDOS_BTN_ProcesarConfir" class="btn btn-primary">Procesar</button>
				<button class="btn btn-secondary" id="BOVEDA_EMITIDOS_BTN_CancelarConfir"  type="button" data-bs-dismiss="modal" id="">Cancelar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   $("#frmConfirmEmail_Emitidos").on('submit', function (event) {
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
	   $('#frmConfirmEmail_Emitidos').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   procesaDescargarCFDIEmitidos();
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
	   	   	   
	
	   calcularEtiquetasBovedaEmitidosConfirmModal();
	   	   
	});
   
   

	 function calcularEtiquetasBovedaEmitidosConfirmModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'BOVEDA_EMITIDOS'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("BOVEDA_EMITIDOS_ETQ31").innerHTML = data.ETQ31;
						document.getElementById("BOVEDA_EMITIDOS_ETQ32").innerHTML = data.ETQ32;
						document.getElementById("BOVEDA_EMITIDOS_BTN_ProcesarConfir").innerHTML = BTN_PROCESAR_MENU;
						document.getElementById("BOVEDA_EMITIDOS_BTN_CancelarConfir").innerHTML = BTN_CANCELAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasBovedaEmitidosConfirmModal()_1_'+thrownError);
				}
			});	
		}
	 
	 
	 function procesaDescargarCFDIEmitidos(){
			
			try{
				
					var fechaInicial  = obtenerFechaIni_Emitidos();
					var fechaFinal = obtenerFechaFin_Emitidos();
					var rfc = obtenerRFC_Emitidos();
					var razonSocial = obtenerRazon_Emitidos();
					var uuid = obtenerUUID_Emitidos();
					var tipoComprobante = obtenerComprobante_Emitidos();
					var serie = obtenerSerie_Emitidos();
					var folio  = obtenerFolio_Emitidos();
					var uuidRegistro = '';
		        
				
		            var formData = new FormData(document.getElementById("frmConfirmEmail_Emitidos"));
		            formData.append("fechaInicial", fechaInicial);
		            formData.append("fechaFinal", fechaFinal);
		            formData.append("rfc", rfc);
		            formData.append("razonSocial", razonSocial);
		            formData.append("uuid", uuid);
		            formData.append("tipoComprobante", tipoComprobante);
		            formData.append("serie", serie);
		            formData.append("folio", folio);
		            formData.append("uuidRegistro", uuidRegistro);
		            

		            $.ajax({
		            	url: '/siarex247/cumplimientoFiscal/boveda/emitidos/procesaDescargarCFDI.action',
		                dataType: "json",
		                beforeSend: function( xhr ) {
		                	
		        		},
		                type: "post",
		                data: formData,
		                cache: false,
		                contentType: false,
			    		processData: false,
			    		complete: function(jqXHR, textStatus){
				    		  
					    },
					    
		            }).done(function(data){
		            	
			            if (data.codError == '000') {
		 					Swal.fire({
		 						  icon: 'success',
		 						  title: MSG_OPERACION_EXITOSA_MENU,
		 						 // text: data.MENSAJE,
		 						  html: '<p>'+data.mensajeError+' </p>',
		 						  showCancelButton: false,
		 						  confirmButtonText: BTN_ACEPTAR_MENU,
		 						  denyButtonText: BTN_CANCELAR_MENU,
		 						}).then((result) => {
		 						  if (result.isConfirmed) {
		 							 $('#myModalNotifica_Emitidos').modal('hide');
		  	 						// $('#tablaDetalleNomina').DataTable().ajax.reload(null,false);
		 						  }
		 					});
						} else {
						  Swal.fire({
							  icon: 'error',
							  title: '¡Error en Operacion!',
							  text: data.mensajeError
						  });
						}
		         });
			}
			catch(e){
				e = null;
			}
	}
	
 </script>
</html>