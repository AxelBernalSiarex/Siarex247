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
<form id="form-Cargar-CFDI" class="was-validated" novalidate>
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div id="overSeccionNomina" class="overlay" style="display: none;text-align: right;">
				<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
			</div>
		  <div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="CAT_CONSTANCIA_SITUACION_ETQ19">Cargar Cedulas Fiscales de Proveedores</h5>
				</div>
				<div class="p-4 pb-0">
					
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="filePDF" id="CAT_CONSTANCIA_SITUACION_ETQ20">Seleccione los archivos CFDI</label>
						<div class="col-sm-8">
							<input class="form-control form-control-sm" type="file" id="filePDF" name="filePDF" accept="application/pdf" required multiple />
	 					</div>
					</div>

				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometerCargar" class="btn btn-primary" >Guardar</button>
				<button class="btn btn-secondary" type="button" id="CAT_CONSTANCIA_BTN_CerrarCargar" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>


 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   $("#form-Cargar-CFDI").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Cargar-CFDI').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   cargarCFDI();
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
	   
	   calcularEtiquetasCatalogoConstanciasModalCargar();
	});
	
   
   

	 function calcularEtiquetasCatalogoConstanciasModalCargar(){
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
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("CAT_CONSTANCIA_SITUACION_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("btnSometerCargar").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("CAT_CONSTANCIA_BTN_CerrarCargar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasCatalogoConstanciasModalCargar()_1_'+thrownError);
				}
			});	
		}
	 
 </script>
 
</html>