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



<form id="form-Busqueda" class="was-validated" novalidate>
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="modal-title-busqueda">Busqueda de RFC en Lista Negra</h5>
					<div id="overSeccion" class="overlay" style="display: none;text-align: right;">
						<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
					 </div>
	   				
				</div>
				<div class="p-4 pb-0">
						 	
						<div class="mb-2 row" id="error_ListaNegra">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-danger border-2 d-flex align-items-center" role="alert">
									  <div class="bg-danger me-3 icon-item"><span class="fas fa-times-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ70">El RFC solicitado SI se encontró en la base de datos de lista negra </p>
									</div>
								</div>
							</div>
						</div>
						
						<div class="mb-2 row" id="exito_ListaNegra">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-success border-2 d-flex align-items-center" role="alert">
									  <div class="bg-success me-3 icon-item"><span class="fas fa-check-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ66">El RFC solicitado NO se encontró en la base de datos de lista negra</p>
									</div>
								</div>
							</div>
						</div>

					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtRFC" id="">RFC  </label>
						<div class="col-sm-4">
	   						<input id="txtRFC" name="rfc" class="form-control" type="text"  maxlength="15" required />
	 					</div>
					</div>
					<div class="mb-2 row">
						<label class="col-sm-4 col-form-label" for="txtRazonSocial" id="">Razón Social  </label>
						<div class="col-sm-8">
	   						<input id="txtRazonSocial" name="razonSocial" class="form-control" type="text"  maxlength="200" required />
	 					</div>
					</div>
					<div class="mb-2 row" id="descargarDocumento">
						<label class="col-sm-4 col-form-label"></label>
						<div class="col-sm-8">
							<button class="btn btn-falcon-primary me-1 mb-1" type="button" style="height: 60px; width: 100%;" id="btnDescargarPDF" name="btnDescargarPDF" onclick="descargarPDF();">
								<i class="far fa-file-pdf mx-2"></i>Descargar PDF
							</button>
						</div>
						
						
					</div>

				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary">Buscar</button>
				<button class="btn btn-secondary" id="CAT_CENTROS_Boton_Cerrar" type="button" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   $("#form-Busqueda").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });

	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#form-Busqueda').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
           errorClass: 'help-block animation-pullUp', errorElement: 'div',
           keyUp: true,
           submitHandler: function(form) {
        	   buscarRFC();
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
	   	   	   
	   	   
	});
	

	 function calcularEtiquetasCatalogoCentrosModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CAT_CENTROS'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CAT_CENTROS_Etiqueta_CentroCostos").innerHTML = data.ETQ1;
						document.getElementById("CAT_CENTROS_Etiqueta_Departamento").innerHTML = data.ETQ2;
						document.getElementById("CAT_CENTROS_Etiqueta_Correo").innerHTML = data.ETQ3;
						document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("CAT_CENTROS_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 
 </script>
</html>