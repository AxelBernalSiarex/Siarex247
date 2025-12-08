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



<form id="form-Catalogo" class="was-validated" novalidate>
	<input type="hidden" name="idRegistro" id="idRegistro_Catalogo" value="0" >
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="modal-title-catalogo">title</h5>
				</div>
				<div class="p-4 pb-0">

					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtNombreCorto" id="CAT_PUESTOS_Etiqueta_NombreCorto">Nombre Corto </label>
						<div class="col-sm-8">
	   						<input id="txtNombreCorto" name="nombreCorto" class="form-control validarHabilitar" type="text" maxlength="3"  required />
	 					</div>
					</div>

					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtDescripcion" id="CAT_PUESTOS_Etiqueta_Descripcion">Descripción</label>
						<div class="col-sm-8">
	   						<input id="txtDescripcion" name="descripcion" class="form-control validarHabilitar" type="text" maxlength="100" required />
	 					</div>
					</div>

				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary">Guardar</button>
				<button class="btn btn-secondary" id="CAT_PUESTOS_Boton_Cerrar" type="button" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   $("#form-Catalogo").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });

	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Catalogo').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   var idRegistro = $('#idRegistro_Catalogo').val();
			   if (idRegistro == 0){
				   guardarCatalogo();
			   }else{
				   actualizarCatalogo();
			   }
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
	   	   	   
	   calcularEtiquetasCatalogoPuestosModal();
	});
   
   
   function calcularEtiquetasCatalogoPuestosModal(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'CAT_PUESTOS'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					document.getElementById("CAT_PUESTOS_Etiqueta_NombreCorto").innerHTML = data.TITLE1;
					document.getElementById("CAT_PUESTOS_Etiqueta_Descripcion").innerHTML = data.ETQ1;

					document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
					document.getElementById("CAT_PUESTOS_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
	
 </script>
</html>