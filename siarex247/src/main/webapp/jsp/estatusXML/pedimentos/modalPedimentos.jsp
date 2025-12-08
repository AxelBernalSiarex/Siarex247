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



<form id="frmCargaPedimentos" class="was-validated" novalidate>
	 <div id="overSeccionCA" class="overlay" style="display: none;text-align: right;">
		<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
	</div>
	
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="PEDIMENTOS_ETQ23">Cargar Archivo PDF de Pedimentos</h5>
				</div>
				<div class="p-4 pb-0">

					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="filePDF" id="PEDIMENTOS_ETQ24">Archivos PDF</label>
						<div class="col-sm-8">
	   						<input id="filePDF" name="filePDF" class="form-control" type="file" multiple accept="application/pdf" requiered />
	 					</div>
					</div>

				

				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary">Procesar</button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="PEDIMENTOS_Boton_Cancelar">Cancelar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
   $(document).ready(function() {

	   	  
	   $("#frmCargaPedimentos").on('submit', function (event) {
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
	   $('#frmCargaPedimentos').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   procesarPedimentos();
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
	   	 
	   calcularEtiquetasPedimentosModal();
	});
   
   


	 function calcularEtiquetasPedimentosModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'PEDIMENTOS'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("PEDIMENTOS_ETQ23").innerHTML = data.ETQ23;
						document.getElementById("PEDIMENTOS_ETQ24").innerHTML = data.ETQ24;
						document.getElementById("btnSometer").innerHTML = BTN_PROCESAR_MENU;
						document.getElementById("PEDIMENTOS_Boton_Cancelar").innerHTML = BTN_CANCELAR_MENU;
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasPedimentosModal()_1_'+thrownError);
				}
			});	
		}
	 
	
 </script>
</html>