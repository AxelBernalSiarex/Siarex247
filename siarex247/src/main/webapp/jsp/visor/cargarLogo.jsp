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
<form id="form-Cargar-Logo" class="was-validated" novalidate>
 
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="VISOR_TITLE7">Logo de Proveedores</h5>
				</div>
				<div class="p-4 pb-0">
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="fileLogo" id="VISOR_ETQ65">Seleccionar archivos .PNG</label>
						<div class="col-sm-8">
							<input class="form-control form-control-sm" type="file" id="fileLogo" name="fileLogo" accept="image/png"/>
	 					</div>
					</div>
					
				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometerLogo" class="btn btn-primary" >Guardar</button>
				<button class="btn btn-secondary" id="btnCerrarLogo" type="button" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>


 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   $("#form-Cargar-Logo").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Cargar-Logo').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   cargarLogo();
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
	   	   
	   calcularEtiquetasVisorCargarLogo();
	});
	
   
   

	function iniciaFormLogoProveedor(){
		
		$("#form-Cargar-Logo").find('.has-success').removeClass("has-success");
	    $("#form-Cargar-Logo").find('.has-error').removeClass("has-error");
		$('#form-Cargar-Logo')[0].reset(); 
		$('#form-Cargar-Logo').removeClass("was-validated"); 
		   
	}
	


	 function calcularEtiquetasVisorCargarLogo(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'VISOR'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("VISOR_TITLE7").innerHTML = data.TITLE7;
						document.getElementById("VISOR_ETQ65").innerHTML = data.ETQ65;
						
						document.getElementById("btnSometerLogo").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("btnCerrarLogo").innerHTML = BTN_CERRAR_MENU;
						
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 

   function cargarLogo(){
		try{
			var formData = new FormData(document.getElementById("form-Cargar-Logo"));
	        
	        $.ajax({
				url  : '/siarex247/visor/tablero/cargarLogo.action',
				type : 'POST', 
				dataType : 'json',
				data: formData,
				cache: false,
	            contentType: false,
	    		processData: false,
	    		success  : function(data) {
	    			if (data.codError == '000') {
	 					Swal.fire({
	 						  title: MSG_OPERACION_EXITOSA_MENU,
	 						 html: '<p>El logo se ha adjuntado satisfactoriamente.</p>',
								  showCancelButton: false,
								  confirmButtonText: BTN_ACEPTAR_MENU,
								  icon: 'success'
	 						}).then((result) => {
	 							$('#myModalCargarLogo').modal('hide');
	 						});
					} else {
						Swal.fire({
  			                title: MSG_ERROR_OPERACION_MENU,
  			                html: '<p>'+data.mensajeError+'</p>',	
  			                icon: 'error'
  			            });
					}
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('Error al cargar archivo cargarPersonal()...');
				}
			});
			
		}catch(e){
			alert('cargaArchivo(): ' + e);
		}
	}



 </script>
 
</html>