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

 <form id="frmValidarAmericanas" name="frmValidarAmericanas" class="was-validated" novalidate>
   <input type="hidden" name="folioOrden" id="folioOrden_ValidarAmericanas" value="0">
<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
	<div class="modal-content position-relative">
		<div class="modal-body p-0">
			<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
   				<h5 class="mb-1" id="VISOR_TILE14">SIAREX - Prevalidación de Ordenes de Compra Americanas</h5>
			</div>
			<div class="p-4 pb-3">

			
				<div class="row g-0">
					
					<div class="col-xl-6 pe-xl-2">
						<div class="card mb-3">
							<div class="card-header" style="padding: 10px !important;">
								<div class="row flex-between-end">
									<div class="col-auto align-self-center">
										<h6 class="mb-0" data-anchor="data-anchor" id="VISOR_ETQ91">Vista Previa</h6>											
									</div>
								</div>
							</div>
							<div class="card-body bg-light">
								<div id="portapdf" class="abrePDFVisor">
								  	<iframe src="" name="frmPDFAmericana" marginheight="0" marginwidth="0" 
					                	       	width="100%" height="100%">
					               	</iframe>	
								</div>
							</div>
						</div>
					</div>
											
					<div class="col-xl-6 pe-xl-2">
						<div class="card mb-3">
							<div class="card-header" style="padding: 10px !important;">
								<div class="row flex-between-end">
									<div class="col-auto align-self-center">
										<h6 class="mb-0" data-anchor="data-anchor" id="VISOR_ETQ92">Datos de la Orden</h6>
									</div>
								</div>
							</div>
							<div class="card-body bg-light">
								<div id="portapdf" class="abrePDFVisor">
								   <div id="overSeccion_ValidarAmericanas" class="overlay" style="display: none;text-align: right;">
									<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
								 </div>
															
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="ordenCompra_ValidarAmericanas" id="VISOR_ValidarAmericana_Etiqueta_OrdenCompra">Orden de Compra</label>
										<div class="col-sm-8">
	   										<input id="ordenCompra_ValidarAmericanas" name="ordenCompra" class="form-control" type="text" readonly="readonly"/>
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="razonSocial_ValidarAmericanas" id="VISOR_ValidarAmericana_Etiqueta_RazonSocial">Razón Social</label>
										<div class="col-sm-8">
	   										<input id="razonSocial_ValidarAmericanas" name="razonSocial" class="form-control" type="text" readonly="readonly"/>
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="factura_ValidarAmericanas" id="VISOR_ValidarAmericana_Etiqueta_Factura">Factura </label>
										<div class="col-sm-8">
	   										<input id="factura_ValidarAmericanas" name="factura" class="form-control" type="text" readonly="readonly"/>
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="total_ValidarAmericanas" id="VISOR_ValidarAmericana_Etiqueta_Total">Total</label>
										<div class="col-sm-8">
	   										<input id="total_ValidarAmericanas" name="total" class="form-control" type="text" readonly="readonly" />
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="fecha_ValidarAmericanas" id="VISOR_ValidarAmericana_Etiqueta_Fecha">Fecha </label>
										<div class="col-sm-8">
	   										<input id="fecha_ValidarAmericanas" name="fecha" class="form-control" type="text" readonly="readonly" />
	 									</div>
									</div>
									
									<div class="mb-2 row" >
										<label class="col-sm-4 col-form-label" for="motivoRechazo_ValidarAmericanas" id="VISOR_ValidarAmericana_Etiqueta_MotivoRechazo">Motivo de Rechazo </label>
										<div class="col-sm-8">
	   										<select class="form-select" id="motivoRechazo_ValidarAmericanas" name="motivoRechazo" required style="cursor: pointer;">
												
											</select>
	 									</div>
									</div>
									
									
									<div class="mb-2 row" style="text-align: center !important; margin-top: 30px;">
										<div class="col-md-12" style="text-align: center !important">
										   <button class="btn btn-sm btn-primary btn-sm me-1 mb-2 mb-sm-0" type="button" onclick="guardarValidarAmericana('CORRECTO');" id="btnAprobarValidarAmericanas">
               							  		<span class="far fa-thumbs-up me-1"> </span> <span id="VISOR_ETQ93"> Factura Correcta </span>
               							  	</button>
               							  	
               							   <button class="btn btn-sm btn-danger btn-sm mb-2 mb-sm-0" type="button" onclick="guardarValidarAmericana('INCORRECTO');" id="btnRechazarValidarAmericanas" >
               							   		<span class="far fa-thumbs-down me-1"></span>  <span id="VISOR_ETQ94"> Factura Incorrecta </span>
               							   </button>	
	 									</div>
									</div>
									
								</div>
							</div>
						</div>
					</div>
					
				</div>
			
			</div>
		</div>
		
		<div class="modal-footer">	
			<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="VISOR_ValidarAmericana_Boton_Cerrar" >Cerrar</button>
		</div>
	</div>
</div>
</form>
 <script type="text/javascript">
 
   $(document).ready(function() {
	   $(document).ready(function() {
			 
			 $("#frmValidarAmericanas").on('submit', function (event) {
				   $(this).addClass('was-validated');
				 });
			   
			   /* Necesario para validacion del form y del Select2
			   -----------------------------------------------------*/
			   $('#frmValidarAmericanas').validate({
				   ignore: 'input[type=hidden]',
				   focusInvalid: false,
		       errorClass: 'help-block animation-pullUp', errorElement: 'div',
		       keyUp: true,
		       submitHandler: function(form) {
		      	  
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
			   
			 
			   calcularEtiquetasVisorValidarAmericana();
		   
		 });
	   	   
	});
	
   
	 function calcularEtiquetasVisorValidarAmericana(){
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
						
						document.getElementById("VISOR_TILE14").innerHTML = data.TILE14;
						document.getElementById("VISOR_ETQ91").innerHTML = data.ETQ91;
						document.getElementById("VISOR_ETQ92").innerHTML = data.ETQ92;
						document.getElementById("VISOR_ETQ93").innerHTML = data.ETQ93;
						document.getElementById("VISOR_ETQ94").innerHTML = data.ETQ94;
						
						document.getElementById("VISOR_ValidarAmericana_Etiqueta_OrdenCompra").innerHTML = data.ETQ29;
						document.getElementById("VISOR_ValidarAmericana_Etiqueta_RazonSocial").innerHTML = data.ETQ17;
						document.getElementById("VISOR_ValidarAmericana_Etiqueta_Factura").innerHTML = data.ETQ63;
						document.getElementById("VISOR_ValidarAmericana_Etiqueta_Total").innerHTML = data.COL10;
						document.getElementById("VISOR_ValidarAmericana_Etiqueta_Fecha").innerHTML = data.ETQ85;
						document.getElementById("VISOR_ValidarAmericana_Etiqueta_MotivoRechazo").innerHTML = data.ETQ86;
						
						
						document.getElementById("VISOR_ValidarAmericana_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
						
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 
   
   function cargaMotivosAmericanos() {
 		try {
 			$('#motivoRechazo_ValidarAmericanas').empty();
 			$.ajax({
 	           url:  '/siarex247/catalogos/motivos/comboMotivos.action',
 	           type: 'POST',
 	            dataType : 'json',
 			    success: function(data){
 			    	$('#motivoRechazo_ValidarAmericanas').empty();
 			    	$('#motivoRechazo_ValidarAmericanas').append($('<option></option>').attr('value', '').text('Seleccione un Motivo'));
 			    	$.each(data.data, function(key, text) {
 			    		$('#motivoRechazo_ValidarAmericanas').append($('<option></option>').attr('value', text.idRegistro).text(text.descripcion));
 			      	});
 			    }
 			});
 		}catch(e) {
 			alert("cargaMotivosAmericanos()_"+e);
 		} 
 	}
 </script>
</html>