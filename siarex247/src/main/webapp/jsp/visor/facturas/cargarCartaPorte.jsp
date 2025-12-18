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
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<script src="/siarex247/jsp/visor/complementos/validacionesComplemento.js"></script>



	<script>
		var isRTL = JSON.parse(localStorage.getItem("isRTL"));
		if (isRTL) {
			var linkDefault = document.getElementById("style-default");
			var userLinkDefault = document.getElementById("user-style-default");
			linkDefault.setAttribute("disabled", true);
			userLinkDefault.setAttribute("disabled", true);
			document.querySelector("html").setAttribute("dir", "rtl");
		} else {
			var linkRTL = document.getElementById("style-rtl");
			var userLinkRTL = document.getElementById("user-style-rtl");
			linkRTL.setAttribute("disabled", true);
			userLinkRTL.setAttribute("disabled", true);
		}
	</script>
</head>
	
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="VISOR_TITLE10">SIAREX - Cargar documentos de Carta Porte</h5>
				</div>
				<div class="p-4 pb-3">
					 <form id="frmCargarCartaPorte" name="frmCargarCartaPorte" class="was-validated" novalidate>
					     <div id="overSeccion_Cargar_CartaPorte" class="overlay" style="display: none;text-align: right;">
							<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
						 </div>
						 	
						<div class="mb-2 row" id="error_Cargar_CartaPorte">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-danger border-2 d-flex align-items-center" role="alert">
									  <div class="bg-danger me-3 icon-item"><span class="fas fa-times-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ73">Error al validar carta porte </p>
									</div>
								</div>
							</div>
						</div>
						
						<div class="mb-2 row" id="exito_Cargar_CartaPorte">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-success border-2 d-flex align-items-center" role="alert">
									  <div class="bg-success me-3 icon-item"><span class="fas fa-check-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ74">La validación de carta porte fue exitosa</p>
									</div>
								</div>
							</div>
						</div>
						
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="folioEmpresa_Cargar_CartaPorte" id="VISOR_CargarCartaPorte_Etiqueta_NumeroOrden">Número de Orden</label>
							<div class="col-sm-3">
		   						<input id="folioEmpresa_Cargar_CartaPorte" name="folioEmpresa" class="form-control" type="number" required />
		 					</div>
						</div>
						
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="fileXML_Cargar_CartaPorte" id="VISOR_CargarCartaPorte_Etiqueta_ArchivoXML">Archivo XML</label>
							<div class="col-sm-10">
								<input class="form-control form-control-sm" id="fileXML_Cargar_CartaPorte" name="fileXML" type="file" accept="text/xml" required/>
						 	</div>
						</div>
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="filePDF_Cargar_CartaPorte" id="VISOR_CargarCartaPorte_Etiqueta_ArchivoPDF">Archivo PDF</label>
							<div class="col-sm-8">
								<input class="form-control form-control-sm" id="filePDF_Cargar_CartaPorte" name="filePDF" type="file" accept="application/pdf" required />
						 	</div>
						 	
						 	<div class="col-sm-2">
						 	<!-- 
								 <div class="col-sm-2">
									<div class="position-relative" id="emoji-button" onclick="someterFormulario();">
										<div class="btn btn-info" data-picmo='{"position":"bottom-start"}'><span class="fas fa-search"></span></div>
							  		</div>
								</div>
							-->
									<button class="btn btn-falcon-success btn-sm me-1 mb-2 mb-sm-1" type="submit" id="btnSometerCartaPorte"><span class="fas fa-check me-1"> </span>Validar</button>	
				              </div>
				              
				              
						</div>	
						
						<div class="mb-2 row">
			              <label class="col-sm-2 col-form-label" for="mensajeRespuesta_Cargar_CartaPorte" id="VISOR_CargarCartaPorte_Etiqueta_Respuesta">Respuesta</label>
			              <div class="col-sm-10">
			                <textarea class="form-control" id="mensajeRespuesta_Cargar_CartaPorte" name="mensajeRespuesta" rows="5" ></textarea>
			              </div>
			            </div>
						
					</form>
				
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-primary" type="button"   id="btnGuardar_CargarCartaPorte" onclick="guardarAsignarCargarCartaPorte();" >Asignar</button>	
				<button class="btn btn-secondary" type="button" id="btnCerrar_CargarCartaPorte" data-bs-dismiss="modal">Cerrar</button>
			</div>
		</div>
	</div>


 <script type="text/javascript">
 
 
 $(document).ready(function() {
	 
	 $("#frmCargarCartaPorte").on('submit', function (event) {
		   $(this).addClass('was-validated');
		 });
	   
	   /* Necesario para validacion del form y del Select2
	   -----------------------------------------------------*/
	   $('#frmCargarCartaPorte').validate({
		   ignore: 'input[type=hidden]',
		   focusInvalid: false,
       errorClass: 'help-block animation-pullUp', errorElement: 'div',
       keyUp: true,
       submitHandler: function(form) {
      	   validarCartaPorte();
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
	   
	   calcularEtiquetasVisorCargarCartaPorte();
	 
 });
 
 

 function calcularEtiquetasVisorCargarCartaPorte(){
	  	
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
					
					document.getElementById("VISOR_TITLE10").innerHTML = data.TITLE10;
					document.getElementById("VISOR_ETQ73").innerHTML = data.ETQ73;
					document.getElementById("VISOR_ETQ74").innerHTML = data.ETQ74;
					document.getElementById("VISOR_CargarCartaPorte_Etiqueta_NumeroOrden").innerHTML = data.ETQ36;
					document.getElementById("VISOR_CargarCartaPorte_Etiqueta_ArchivoXML").innerHTML = data.ETQ57;
					document.getElementById("VISOR_CargarCartaPorte_Etiqueta_ArchivoPDF").innerHTML = data.ETQ56;
					document.getElementById("VISOR_CargarCartaPorte_Etiqueta_Respuesta").innerHTML = data.ETQ61;
					
					
					document.getElementById("btnGuardar_CargarCartaPorte").innerHTML = BTN_ASIGNAR;
					document.getElementById("btnCerrar_CargarCartaPorte").innerHTML = BTN_CERRAR_MENU;
					document.getElementById("btnSometerCartaPorte").innerHTML = BTN_VALIDAR;
					
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
 
 
  
 </script>
</html>