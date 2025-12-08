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
	<input type="hidden" name="claveRegistro" id="idRegistro_Catalogo" value="0" >
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="modal-title-catalogo">title</h5>
				</div>
				<div class="p-4 pb-0">


					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="idReceptor" id="CAT_PROVEEDORES_EXTERNOS_ETQ11">Proveedor Directo</label>
						<div class="col-sm-10">
							<div class="form-group">
								<select class="form-select validarHabilitar" id="idReceptor" name="idReceptor"> </select>
							</div>
						</div>
					</div>


					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="idProveedor" id="CAT_PROVEEDORES_EXTERNOS_ETQ12">Id. Proveedor Ext.</label>
						<div class="col-sm-4">
	   						<input id="idProveedor" name="idProveedor" class="form-control validarHabilitar" type="text"  required />
	 					</div>

						 <label class="col-sm-2 col-form-label" for="nombreContacto" id="CAT_PROVEEDORES_Etiqueta_NombreContacto">Nombre del Contacto</label>
						 <div class="col-sm-4">
								<input id="nombreContacto" name="nombreContacto" class="form-control validarHabilitar" type="text"  required />
						  </div>
					</div>

					<div class="mb-2 row">
					    <label class="col-sm-2 col-form-label" for="rfc" id="CAT_PROVEEDORES_Etiqueta_RFC">RFC</label>
						<div class="col-sm-4">
	   						<input id="rfc" name="rfc" class="form-control validarHabilitar" type="text"  required />
	 					</div>
	 					
						<label class="col-sm-2 col-form-label" for="razonSocial" id="CAT_PROVEEDORES_Etiqueta_RazonSocial">Razon Social</label>
						<div class="col-sm-4">
	   						<input id="razonSocial" name="razonSocial" class="form-control validarHabilitar" type="text"  required />
	 					</div>
						 
					</div>


					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="email" id="CAT_PROVEEDORES_EXTERNOS_ETQ13">E-mail</label>
						<div class="col-sm-4">
	   						<input id="email" name="email" class="form-control validarHabilitar" type="email"  required />
	 					</div>

						 <label class="col-sm-2 col-form-label" for="telefono" id="CAT_PROVEEDORES_Etiqueta_Telefono">Teléfono</label>
						 <div class="col-sm-4">
								<input id="telefono" name="telefono" class="form-control validarHabilitar" type="text"  required />
						  </div>
					</div>


					<div class="mb-2 row">
						 <label class="col-sm-2 col-form-label" for="calle" id="CAT_PROVEEDORES_EXTERNOS_ETQ14">Calle</label>
						 <div class="col-sm-4">
								<input id="calle" name="calle" class="form-control validarHabilitar" type="text"  required />
						  </div>
						   <label class="col-sm-2 col-form-label" for="colonia" id="CAT_PROVEEDORES_EXTERNOS_ETQ15">Colonia</label>
							<div class="col-sm-4">
		   						<input id="colonia" name="colonia" class="form-control validarHabilitar" type="text"  required />
		 					</div>
		 					
					</div>


					<div class="mb-2 row">
						<label class="col-sm-2 col-form-label" for="numeroInt" id="CAT_PROVEEDORES_EXTERNOS_ETQ16">Numero Interior</label>
						<div class="col-sm-4">
	   						<input id="numeroInt" name="numeroInt" class="form-control validarHabilitar" type="text"  required />
	 					</div>

						 <label class="col-sm-2 col-form-label" for="numeroExt" id="CAT_PROVEEDORES_EXTERNOS_ETQ17">Numero Exterior</label>
						 <div class="col-sm-4">
								<input id="numeroExt" name="numeroExt" class="form-control validarHabilitar" type="text"  required />
						  </div>
					</div>


					<div class="mb-2 row">
 						<label class="col-sm-2 col-form-label" for="delegacion" id="CAT_PROVEEDORES_Etiqueta_Delegacion">Delegación</label>
						<div class="col-sm-4">
	   						<input id="delegacion" name="delegacion" class="form-control validarHabilitar" type="text"  required />
	 					</div>
	 					 <label class="col-sm-2 col-form-label" for="ciudad" id="CAT_PROVEEDORES_Etiqueta_Ciudad">Ciudad</label>
						 <div class="col-sm-4">
								<input id="ciudad" name="ciudad" class="form-control validarHabilitar" type="text"  required />
						  </div>
						  
					</div>


					<div class="mb-2 row">
						  <label class="col-sm-2 col-form-label" for="estado" id="CAT_PROVEEDORES_Etiqueta_Estado">Estado</label>
						 <div class="col-sm-4">
								<input id="estado" name="estado" class="form-control validarHabilitar" type="text"  required />
						  </div>
						
						 <label class="col-sm-2 col-form-label" for="codigoPostal" id="CAT_PROVEEDORES_EXTERNOS_ETQ18">Codigo Postal</label>
						 <div class="col-sm-4">
								<input id="codigoPostal" name="codigoPostal" class="form-control validarHabilitar" type="text"  required />
						  </div>
					</div>

					

				</div>
			</div>
			<div class="modal-footer">	
				<button type="submit" id="btnSometer" class="btn btn-primary">Guardar</button>
				<button class="btn btn-secondary" id="CAT_PROVEEDORES_Boton_Cerrar" type="button" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
   $(document).ready(function() {

	   $("#form-Catalogo").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });


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
	   	   
	   calcularEtiquetasCatalogoProvExternosModal();
	});

   
   
   

	 function calcularEtiquetasCatalogoProvExternosModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CAT_PROV_EXTERNOS'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CAT_PROVEEDORES_EXTERNOS_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("CAT_PROVEEDORES_EXTERNOS_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("CAT_PROVEEDORES_EXTERNOS_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("CAT_PROVEEDORES_EXTERNOS_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("CAT_PROVEEDORES_EXTERNOS_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("CAT_PROVEEDORES_EXTERNOS_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("CAT_PROVEEDORES_EXTERNOS_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("CAT_PROVEEDORES_EXTERNOS_ETQ18").innerHTML = data.ETQ18;
						
						document.getElementById("CAT_PROVEEDORES_Etiqueta_NombreContacto").innerHTML = data.ETQ9;
						document.getElementById("CAT_PROVEEDORES_Etiqueta_RFC").innerHTML = data.ETQ4;
						document.getElementById("CAT_PROVEEDORES_Etiqueta_RazonSocial").innerHTML = data.ETQ3;
						document.getElementById("CAT_PROVEEDORES_Etiqueta_Telefono").innerHTML = data.ETQ8;
						document.getElementById("CAT_PROVEEDORES_Etiqueta_Delegacion").innerHTML = data.ETQ5;
						document.getElementById("CAT_PROVEEDORES_Etiqueta_Ciudad").innerHTML = data.ETQ6;
						document.getElementById("CAT_PROVEEDORES_Etiqueta_Estado").innerHTML = data.ETQ7;
						
						
						document.getElementById("btnSometer").innerHTML = BTN_GUARDAR_MENU;
						document.getElementById("CAT_PROVEEDORES_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
   
   function cargaProveedoresExt(claveRegistro) {
		try {
			$.ajax({
	           url:  '/siarex247/catalogos/proveedores/comboProveedoresExt.action',
	           type: 'POST',
	            dataType : 'json',
			    success: function(data){
			    	$('#idReceptor').empty();
			    	$.each(data.data, function(key, text) {
			    		if (claveRegistro == text.claveRegistro){
			    			$('#idReceptor').append($('<option></option>').attr('selected', 'selected').attr('value', text.claveRegistro).text(text.razonSocial));
			    		}else{
			    			$('#idReceptor').append($('<option></option>').attr('value', text.claveRegistro).text(text.razonSocial));	
			    		}
			      	});
			    }
			});
		}
		catch(e) {
			swal.fire({
				title : "¡Error en Operacion!",
				html  : e,
				type: 'error',
				confirmButtonText: 'Aceptar'
			}).then((result) => {
				swal.close();
			});
		} 
	}
   
   
 </script>
</html>