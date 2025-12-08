<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <meta charset="utf-8" />
   <meta name="viewport" content="width=device-width, initial-scale=1" />
   
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
<form id="form-Enviar-Acceso" class="was-validated" novalidate>
	<input type="hidden" name="claveRegistro" id="claveRegistro_Enviar" value="0">
	
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="CAT_PROVEEDORES_TITLE2">Envió Información de Acceso a Proveedores</h5>
				</div>
				<div class="p-4 pb-0">
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtIdProveedor_Enviar" id="CAT_PROVEEDORES_ETQ113">ID Proveedor </label>
						<div class="col-sm-8">
	   						<input id="txtIdProveedor_Enviar" name="idProveedor" class="form-control" type="text"  readonly="readonly" />
	 					</div>
					</div>

					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtRazonSocial_Enviar" id="CAT_PROVEEDORES_ETQ114">Razon Social </label>
						<div class="col-sm-8">
	   						<input id="txtRazonSocial_Enviar" name="razonSocial" class="form-control" type="text" readonly="readonly" />
	 					</div>
					</div>
					
					
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="filesPDF" id="CAT_PROVEEDORES_ETQ115">PDF Enviar</label>
						<div class="col-sm-8">
		   					<input id="filesPDF_Enviar" name="filesPDF" class="form-control" type="file" accept="application/pdf" required />
		 				</div>
					</div>
					
				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometerEnviar" class="btn btn-primary" >Enviar</button>
				<button class="btn btn-secondary" type="button" id="CAT_PROVEEDORES_Boton_CerrarAcceso" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>


 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   $("#form-Enviar-Acceso").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Enviar-Acceso').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   enviarAcceso();
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
	   	   
	   calcularEtiquetasCatalogoProveedoresAcceso();
	});
	
   

	 function calcularEtiquetasCatalogoProveedoresAcceso(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CAT_PROVEEDORES'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CAT_PROVEEDORES_TITLE2").innerHTML = data.TITLE2;
						document.getElementById("CAT_PROVEEDORES_ETQ113").innerHTML = data.ETQ113;
						document.getElementById("CAT_PROVEEDORES_ETQ114").innerHTML = data.ETQ114;
						document.getElementById("CAT_PROVEEDORES_ETQ115").innerHTML = data.ETQ115;
						
						document.getElementById("btnSometerEnviar").innerHTML = BTN_PROCESAR_MENU;
						document.getElementById("CAT_PROVEEDORES_Boton_CerrarAcceso").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasCatalogoProveedoresAcceso()_1_'+thrownError);
				}
			});	
		}
	 
 </script>
 
</html>