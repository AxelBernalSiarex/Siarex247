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
	<script src="/siarex247/jsp/visor/facturas/validarCP.js"></script>



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
	
	<input type="hidden" name="folioEmpresa_ValidarCP" id="folioEmpresa_ValidarCP" value="0">
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="VISOR_TITLE13">SIAREX - Prevalidación de Ordenes de Clave Producto XML</h5>
				</div>
				<div class="p-4 pb-3">
				
						<div class="mb-2 row" id="valError_ValidarCP">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-danger border-2 d-flex align-items-center" role="alert">
									  <div class="bg-danger me-3 icon-item"><span class="fas fa-times-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ89">Error en Validación de CFDI</p>
									</div>
								</div>
							</div>
						</div>
						
						<div class="mb-2 row" id="valExito_ValidarCP">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-success border-2 d-flex align-items-center" role="alert">
									  <div class="bg-success me-3 icon-item"><span class="fas fa-check-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ90">Validacion de CFDI Correcto</p>
									</div>
								</div>
							</div>
						</div>
						
						
		                <div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="razonSocial_ValidarCP" id="VISOR_ValidarCP_Etiqueta_RazonSocial">Razón Social</label>
							<div class="col-sm-10">
								<div class="form-group">
									<input id="razonSocial_ValidarCP" name="razonSocial" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
						</div>
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="rfc_ValidarCP" id="VISOR_ValidarCP_Etiqueta_RFC">RFC</label>
							<div class="col-sm-3">
								<div class="form-group">
									<input id="rfc_ValidarCP" name="rfc" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
							<label class="col-sm-3 col-form-label" for="ordenCompra_ValidarCP" id="VISOR_ValidarCP_Etiqueta_OrdenCompra">Orden de Compra</label>
							<div class="col-sm-4">
								<div class="form-group">
									<input id="ordenCompra_ValidarCP" name="folioEmpresa" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
						</div>
						
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="usoCFDI_ValidarCP" id="VISOR_ValidarCP_Etiqueta_UsoCFDI">Uso CFDI</label>
							<div class="col-sm-3">
								<div class="form-group">
									<input id="usoCFDI_ValidarCP" name="usoCFDI" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
							<label class="col-sm-3 col-form-label" for="descripcionUso_ValidarCP" id="VISOR_ValidarCP_Etiqueta_DescripcionCFDI">Descripción de Uso CFDI</label>
							<div class="col-sm-3">
								<div class="form-group">
									<input id="descripcionUso_ValidarCP" name="descripcionUso" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
							
						</div>
						<div class="mb-2 row">
							
							<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
								<div class="tab-content">
											
									<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
										<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
											<table id="tablaDetalleValidarCP"class="table table-sm mb-0 data-table fs--1">
												<thead class="bg-200 text-900">
													<tr>
														<th class="no-sort pe-1 align-middle data-table-row-action" id="VISOR_ValidarCP_Etiqueta_ClaveProducto">Clave de Producto y Servicio</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_ValidarCP_Etiqueta_DescripcionProductoXML">Descripción Clave de Producto del XML</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_ValidarCP_Etiqueta_DescripcionProductoSAT">Descripción Clave de Producto del SAT</th>
													</tr>
													
												</thead>
											</table>
										</div>
									</div>
						
								</div> <!-- tab-content -->
						
							</div> <!-- card-body -->
						</div>
					
				
				</div>
			</div>
			<div class="modal-footer">	
				 <button class="btn btn-sm btn-primary btn-sm me-1 mb-2 mb-sm-0" type="button" onclick="guardarUsoCartaPorte('CORRECTA');" id="btnAprobarCartaPorte">
               			<span class="far fa-thumbs-up me-1"> </span> <span id="VISOR_ValidarCP_Etiqueta_InformacionCorrecta"> Información Correcta </span> 
               	</button>
               	<button class="btn btn-sm btn-danger btn-sm mb-2 mb-sm-0" type="button" onclick="guardarUsoCartaPorte('INCORRECTA');" id="btnRechazarCartaPorte" >
			   		<span class="far fa-thumbs-down me-1"></span>  <span id="VISOR_ValidarCP_Etiqueta_InformacionIncorrecta"> Información Incorrecta </span>
			   </button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="VISOR_ValidarCP_Boton_Cerrar">Cerrar</button>
			</div>
		</div>
	</div>


 <script type="text/javascript">
 
 
 $(document).ready(function() {
	 
	 $('#valError_ValidarCP').hide();
	 $('#valExito_ValidarCP').hide();
	  
	 calcularEtiquetasVisorValidarCP();
 });
 

 function calcularEtiquetasVisorValidarCP(){
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
					
					document.getElementById("VISOR_TITLE13").innerHTML = data.TITLE13;
					document.getElementById("VISOR_ETQ89").innerHTML = data.ETQ89;
					document.getElementById("VISOR_ETQ90").innerHTML = data.ETQ90;
					document.getElementById("VISOR_ValidarCP_Etiqueta_RazonSocial").innerHTML = data.ETQ17;
					document.getElementById("VISOR_ValidarCP_Etiqueta_RFC").innerHTML = data.ETQ18;
					document.getElementById("VISOR_ValidarCP_Etiqueta_OrdenCompra").innerHTML = data.ETQ29;
					document.getElementById("VISOR_ValidarCP_Etiqueta_UsoCFDI").innerHTML = data.COL32;
					document.getElementById("VISOR_ValidarCP_Etiqueta_DescripcionCFDI").innerHTML = data.ETQ77;
					document.getElementById("VISOR_ValidarCP_Etiqueta_ClaveProducto").innerHTML = data.COL33;
					document.getElementById("VISOR_ValidarCP_Etiqueta_DescripcionProductoXML").innerHTML = data.ETQ78;
					document.getElementById("VISOR_ValidarCP_Etiqueta_DescripcionProductoSAT").innerHTML = data.ETQ79;
					document.getElementById("VISOR_ValidarCP_Etiqueta_InformacionCorrecta").innerHTML = data.ETQ80;
					document.getElementById("VISOR_ValidarCP_Etiqueta_InformacionIncorrecta").innerHTML = data.ETQ81;
					
					document.getElementById("VISOR_ValidarCP_Boton_Cerrar").innerHTML = BTN_CERRAR_MENU;
					
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
 
 
  
 </script>
</html>