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

	<script src="/siarex247/jsp/catalogos/proveedores/validarCertificados.js"></script>
	
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


<input type="hidden" name="claveRegistro_Validar" id="claveRegistro_Validar" value="">
<input type="hidden" name="tipoCertificado_Validar" id="tipoCertificado_Validar" value="">

<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
	<div class="modal-content position-relative">
		
		<div class="modal-body p-0">
			<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
   				<h5 class="mb-1" id="CAT_PROVEEDORES_TITLE4">Validar Certificados IMSS</h5>
			</div>
			<div class="p-4 pb-3">
			
				<div class="row g-0">
					
					<div class="col-xl-6 pe-xl-2">
						<div class="card mb-3">
							<div class="card-header" style="padding: 10px !important;">
								<div class="row flex-between-end">
									<div class="col-auto align-self-center">
										<h6 class="mb-0" data-anchor="data-anchor" id="CAT_PROVEEDORES_ETQ117">Visor de Certificado</h6>											
									</div>
								</div>
							</div>
							<div class="card-body bg-light">
								<div id="portapdf" class="abrePDFVisor">
								  <!-- 
   									<object data="/theme-falcon/repse247/pdf/ArchivoPDF.pdf" type="application/pdf" width="100%" height="100%" id="objVisorPDF"></object>
   								  -->
   								  	<iframe src="" name="frmPDFOrdenes" marginheight="0" marginwidth="0" 
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
										<h6 class="mb-0" data-anchor="data-anchor" id="CAT_PROVEEDORES_ETQ118">Datos del Proveedor</h6>
									</div>
								</div>
							</div>
							<div class="card-body bg-light">
								<div id="portapdf" class="abrePDFVisor">
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="txtrfc_Validar" id="CAT_PROVEEDORES_ETQ119">RFC</label>
										<div class="col-sm-8">
	   										<input id="txtrfc_Validar" name="rfc" class="form-control" type="text" readonly="readonly"/>
	 									</div>
									</div>
									<div class="mb-2 row">
										<label class="col-sm-4 col-form-label" for="txtRazon_Validar" id="CAT_PROVEEDORES_ETQ120">Razon Social</label>
										<div class="col-sm-8">
	   										<input id="txtRazon_Validar" name="razonSocial" class="form-control" type="text" readonly="readonly" />
	 									</div>
									</div>
									
									<div class="mb-2 row" style="text-align: center !important">
										<div class="col-md-12" style="text-align: center !important">
										   <button class="btn btn-sm btn-primary btn-sm me-1 mb-2 mb-sm-0" type="button" onclick="actualizarCertificado('CORRECTO');" id="btnAprobarCertificado">
               							  		<span class="far fa-thumbs-up me-1"> </span> <span id="CAT_PROVEEDORES_ETQ121"> Certificado Correcto </span> 
               							  	</button>
               							  	
               							   <button class="btn btn-sm btn-danger btn-sm mb-2 mb-sm-0" type="button" onclick="actualizarCertificado('INCORRECTO');" id="btnAprobarCertificado" >
               							   		<span class="far fa-thumbs-down me-1"></span> <span id="CAT_PROVEEDORES_ETQ122"> Certificado Incorrecto </span>
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
			<button class="btn btn-secondary" type="button" id="CAT_PROVEEDORES_Boton_CerrarValidar" data-bs-dismiss="modal">Cerrar</button>
		</div>
	</div>
</div>

 <script type="text/javascript">
 
   $(document).ready(function() {
	   	  
	   calcularEtiquetasCatalogoProveedoresValidar();
	});
	

	 function calcularEtiquetasCatalogoProveedoresValidar(){
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
						
						document.getElementById("CAT_PROVEEDORES_TITLE4").innerHTML = data.TITLE4;
						document.getElementById("CAT_PROVEEDORES_ETQ117").innerHTML = data.ETQ117;
						document.getElementById("CAT_PROVEEDORES_ETQ118").innerHTML = data.ETQ118;
						document.getElementById("CAT_PROVEEDORES_ETQ119").innerHTML = data.ETQ119;
						document.getElementById("CAT_PROVEEDORES_ETQ120").innerHTML = data.ETQ120;
						document.getElementById("CAT_PROVEEDORES_ETQ121").innerHTML = data.ETQ121;
						document.getElementById("CAT_PROVEEDORES_ETQ122").innerHTML = data.ETQ122;
						
						document.getElementById("CAT_PROVEEDORES_Boton_CerrarValidar").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasCatalogoProveedoresAcceso()_1_'+thrownError);
				}
			});	
		}
	 
 </script>
</html>