<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

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
	<input type="hidden" name="idRegistro_Catalogo" id="idRegistro_Catalogo" value="0" >
	
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="modal-title-catalogo">Usuarios</h5>
				</div>
				<div class="p-4 pb-0">
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtIdUsuario">Id. Acceso</label>
						<div class="col-sm-8">
	   						<input id="txtIdUsuario" name="txtIdUsuario" class="form-control validarHabilitar" type="text" required />
	 					</div>
					</div>
					
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtIdEmpleado">Id. Empleado</label>
						<div class="col-sm-8">
	   						<input id="txtIdEmpleado" name="txtIdEmpleado" class="form-control validarHabilitar" type="text" required />
	 					</div>
					</div>
					
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtNombreCompleto">Nom. Completo</label>
						<div class="col-sm-8">
	   						<input id="txtNombreCompleto" name="txtNombreCompleto" class="form-control validarHabilitar" type="text" required />
	 					</div>
					</div>
					
					
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtCorreo">Correo</label>
						<div class="col-sm-8">
	   						<input id="txtCorreo" name="txtCorreo" class="form-control validarHabilitar" type="email" required />
	 					</div>
					</div>
					
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="cmbIdPerfil">Perfil</label>
						<div class="col-sm-8">
							<div class="form-group">
							<select class="form-select validarHabilitar" id="cmbIdPerfil" name="cmbIdPerfil" required>
							    <option value="">Seleccione una opción ...</option>
							</select>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary">Guardar</button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Cerrar</button>
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
	   	   
	   $('#cmbIdPerfil').on('change', function() { 
		   $(this).trigger('blur');
		   
	   });
	   /* ----------------------------------------------*/
	   	   
	});
   
   
   
   function cargaPerfiles(idPerfil) {
 		try {
 			$('#cmbIdPerfil').empty();
 			$.ajax({
 	           url:  '/siarex247/seguridad/perfiles/comboPerfilesUsuario.action?accion=1',
 	           type: 'POST',
 	            dataType : 'json',
 			    success: function(data){
 			    	$('#cmbIdPerfil').empty();
 			    	$.each(data.data, function(key, text) {
 				    	if (idPerfil == text.clavePerfil){
 				    		$('#cmbIdPerfil').append($('<option></option>').attr('selected', 'selected').attr('value', text.clavePerfil).text(text.descripcion));
 				    	}else{
 				    		$('#cmbIdPerfil').append($('<option></option>').attr('value', text.clavePerfil).text(text.descripcion));
 				    	}
 			    		
 			      	});
 			    }
 			});
 		}catch(e) {
 			alert("cargaPerfiles()_"+e);
 		} 
 	}
   
 </script>
 
</html>