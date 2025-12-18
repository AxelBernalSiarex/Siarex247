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

 <form id="frmValidarNotaCredito" name="frmValidarNotaCredito" class="was-validated" novalidate>
   <input type="hidden" name="folioOrden_ValidarNota" id="folioOrden_ValidarNota" value="0">
<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
	<div class="modal-content position-relative">
		<div class="modal-body p-0">
			<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
   				<h5 class="mb-1" id="VISOR_TITLE12" >SIAREX - Validar Notas de Crédito</h5>
			</div>
			<div class="p-4 pb-3">

			
				<div class="row g-0">
					
					<div class="col-xl-6 pe-xl-2">
						<div class="card mb-3">
							<div class="card-header" style="padding: 10px !important;">
								<div class="row flex-between-end">
									<div class="col-auto align-self-center">
										<h6 class="mb-0" data-anchor="data-anchor" id="VISOR_ETQ82">Vista Previa Nota de Credito</h6>											
									</div>
								</div>
							</div>
							<div class="card-body bg-light">
								<div id="portapdf" class="abrePDFVisor">
								  	<iframe src="" name="frmPDFNotaCredito" marginheight="0" marginwidth="0" 
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
										<h6 class="mb-0" data-anchor="data-anchor" id="VISOR_ETQ83">Datos de la Nota de Credito</h6>
									</div>
								</div>
							</div>
							<div class="card-body bg-light">
								<div id="portapdf" class="abrePDFVisor">
								   <div id="overSeccion_ValidarNotaCredito" class="overlay" style="display: none;text-align: right;">
									<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
								 </div>
															
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="ordenCompra_ValidarNota" id="VISOR_ValidarNotas_Etiqueta_OrdenCompra">Orden de Compra</label>
										<div class="col-sm-8">
	   										<input id="ordenCompra_ValidarNota" name="ordenCompra" class="form-control" type="text" readonly="readonly"/>
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="razonSocial_ValidarNota" id="VISOR_ValidarNotas_Etiqueta_RazonSocial">Razón Social</label>
										<div class="col-sm-8">
	   										<input id="razonSocial_ValidarNota" name="razonSocial" class="form-control" type="text" readonly="readonly"/>
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="folioFiscal_ValidarNota" id="VISOR_ETQ84">Folio Fiscal (UUID)</label>
										<div class="col-sm-8">
	   										<input id="folioFiscal_ValidarNota" name="folioFiscal" class="form-control" type="text" readonly="readonly"/>
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="factura_ValidarNota" id="VISOR_ValidarNotas_Etiqueta_Factura">Factura </label>
										<div class="col-sm-8">
	   										<input id="factura_ValidarNota" name="factura" class="form-control" type="text" readonly="readonly"/>
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="total_ValidarNota" id="VISOR_ValidarNotas_Etiqueta_Total">Total</label>
										<div class="col-sm-8">
	   										<input id="total_ValidarNota" name="total" class="form-control" type="text" readonly="readonly" />
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="fecha_ValidarNota" id="VISOR_ETQ85">Fecha </label>
										<div class="col-sm-8">
	   										<input id="fecha_ValidarNota" name="fecha" class="form-control" type="text" readonly="readonly" />
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="estadoSAT_ValidarNota" id="VISOR_ValidarNotas_Etiqueta_EstadoSAT">Estado SAT</label>
										<div class="col-sm-8">
	   										<input id="estadoSAT_ValidarNota" name="estadoSAT" class="form-control" type="text" readonly="readonly" />
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="estatusSAT_ValidarNota" id="VISOR_ValidarNotas_Etiqueta_EstatusSAT">Estatus SAT</label>
										<div class="col-sm-8">
	   										<input id="estatusSAT_ValidarNota" name="estatusSAT" class="form-control" type="text" readonly="readonly" />
	 									</div>
									</div>
									<div class="mb-2 row" >
										<label class="col-sm-4 col-form-label" for="motivoRechazo_ValidarNota" id="VISOR_ETQ86">Motivo de Rechazo </label>
										<div class="col-sm-8">
	   										<select class="form-select" id="motivoRechazo_ValidarNota" name="motivoRechazo" required style="cursor: pointer;">
												
											</select>
	 									</div>
									</div>
									
									
									<div class="mb-2 row" style="text-align: center !important; margin-top: 30px;">
										<div class="col-md-12" style="text-align: center !important">
										   <button class="btn btn-sm btn-primary btn-sm me-1 mb-2 mb-sm-0" type="button" onclick="modificarNotaCredito('CORRECTO');" id="btnAprobarNotaCredito">
               							  		<span class="far fa-thumbs-up me-1"> </span> <span id="VISOR_ETQ87">Nota de Crédito Correcta</span> 
               							  	</button>
               							  	
               							   <button class="btn btn-sm btn-danger btn-sm mb-2 mb-sm-0" type="button" onclick="modificarNotaCredito('INCORRECTO');" id="btnRechazarNotaCredito" >
               							   		<span class="far fa-thumbs-down me-1"></span> <span id="VISOR_ETQ88"> Nota de Crédito Incorrecta</span>
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
			<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="VISOR_ValidarNota_BOTON_Cerrar">Cerrar</button>
		</div>
	</div>
</div>
</form>
 <script type="text/javascript">
 
   $(document).ready(function() {
	   $(document).ready(function() {
			 
			 $("#frmValidarNotaCredito").on('submit', function (event) {
				   $(this).addClass('was-validated');
				 });
			   
			   /* Necesario para validacion del form y del Select2
			   -----------------------------------------------------*/
			   $('#frmValidarNotaCredito').validate({
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
			   
			 
		 });
	   	   
	   calcularEtiquetasVisorValidarNotaCredito();
	});
	
   
   function calcularEtiquetasVisorValidarNotaCredito(){
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
					
					document.getElementById("VISOR_TITLE12").innerHTML = data.TITLE12;
					document.getElementById("VISOR_ETQ82").innerHTML = data.ETQ82;
					document.getElementById("VISOR_ETQ83").innerHTML = data.ETQ83;
					document.getElementById("VISOR_ETQ84").innerHTML = data.ETQ84;
					document.getElementById("VISOR_ETQ85").innerHTML = data.ETQ85;
					document.getElementById("VISOR_ETQ86").innerHTML = data.ETQ86;
					document.getElementById("VISOR_ETQ87").innerHTML = data.ETQ87;
					document.getElementById("VISOR_ETQ88").innerHTML = data.ETQ88;
					document.getElementById("VISOR_ValidarNotas_Etiqueta_OrdenCompra").innerHTML = data.ETQ29;
					document.getElementById("VISOR_ValidarNotas_Etiqueta_RazonSocial").innerHTML = data.ETQ28;
					document.getElementById("VISOR_ValidarNotas_Etiqueta_Factura").innerHTML = data.ETQ63;
					document.getElementById("VISOR_ValidarNotas_Etiqueta_Total").innerHTML = data.COL10;
					document.getElementById("VISOR_ValidarNotas_Etiqueta_EstadoSAT").innerHTML = data.COL30;
					document.getElementById("VISOR_ValidarNotas_Etiqueta_EstatusSAT").innerHTML = data.COL31;
					
					document.getElementById("VISOR_ValidarNota_BOTON_Cerrar").innerHTML = BTN_CERRAR_MENU;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}


   
   function cargaMotivos() {
 		try {
 			$('#motivoRechazo_ValidarNota').empty();
 			$.ajax({
 	           url:  '/siarex247/catalogos/motivos/comboMotivos.action',
 	           type: 'POST',
 	            dataType : 'json',
 			    success: function(data){
 			    	$('#motivoRechazo_ValidarNota').empty();
 			    	$('#motivoRechazo_ValidarNota').append($('<option></option>').attr('value', '').text('Seleccione un Motivo'));
 			    	$.each(data.data, function(key, text) {
 			    		$('#motivoRechazo_ValidarNota').append($('<option></option>').attr('value', text.idRegistro).text(text.descripcion));
 			      	});
 			    }
 			});
 		}catch(e) {
 			alert("cargaRegimenFisca()_"+e);
 		} 
 	}
 </script>
</html>