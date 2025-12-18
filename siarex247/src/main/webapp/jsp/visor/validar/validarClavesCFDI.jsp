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
	<script src="/siarex247/jsp/visor/validar/validarCFDI.js"></script>



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
	
	<input type="hidden" name="folioEmpresa_ValidarCFDI" id="folioEmpresa_ValidarCFDI" value="0">
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light modalTitulo">
	   				<h5 class="mb-1" id="VISOR_TITLE11">SIAREX - Prevalidación de Ordenes de Uso CFDI, Claves y Producto de XML</h5>
				</div>
				<div class="p-4 pb-3">
				
						<div class="mb-2 row" id="valError_ValidacionCFDI">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-danger border-2 d-flex align-items-center" role="alert">
									  <div class="bg-danger me-3 icon-item"><span class="fas fa-times-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ75">Error en Validación de CFDI</p>
									</div>
								</div>
							</div>
						</div>
						
						<div class="mb-2 row" id="valExito_ValidacionCFDI">
							<div class="col-sm-12">
								<div class="form-group">
									<div class="alert alert-success border-2 d-flex align-items-center" role="alert">
									  <div class="bg-success me-3 icon-item"><span class="fas fa-check-circle text-white fs-3"></span></div>
									  <p class="mb-0 flex-1" id="VISOR_ETQ76">Validacion de CFDI Correcto</p>
									</div>
								</div>
							</div>
						</div>
						
						
		                <div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="razonSocial_ValidarCFDI" id="VISOR_ValidarClavesCFDI_Etqiueta_RazonSocial">Razón Social</label>
							<div class="col-sm-10">
								<div class="form-group">
									<input id="razonSocial_ValidarCFDI" name="razonSocial" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
						</div>
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="rfc_ValidarCFDI" id="VISOR_ValidarClavesCFDI_Etqiueta_RFC">RFC</label>
							<div class="col-sm-3">
								<div class="form-group">
									<input id="rfc_ValidarCFDI" name="rfc" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
							<label class="col-sm-3 col-form-label" for="ordenCompra_ValidarCFDI" id="VISOR_ValidarClavesCFDI_Etqiueta_OrdenCompra">Orden de Compra</label>
							<div class="col-sm-4">
								<div class="form-group">
									<input id="ordenCompra_ValidarCFDI" name="folioEmpresa" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
						</div>
						
						<div class="mb-2 row">
							<label class="col-sm-2 col-form-label" for="usoCFDI_ValidarCFDI" id="VISOR_ValidarClavesCFDI_Etqiueta_UsoCFDI">Uso CFDI</label>
							<div class="col-sm-3">
								<div class="form-group">
									<input id="usoCFDI_ValidarCFDI" name="usoCFDI" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
							<label class="col-sm-3 col-form-label" for="descripcionUso_ValidarCFDI" id="VISOR_ETQ77">Descripción de Uso CFDI</label>
							<div class="col-sm-3">
								<div class="form-group">
									<input id="descripcionUso_ValidarCFDI" name="descripcionUso" class="form-control" type="text" readonly="readonly">
								</div>
							</div>
							
						</div>
						<div class="mb-2 row">
							
							<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
								<div class="tab-content">
											
									<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
										<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
											<table id="tablaDetalleCFDI"class="table table-sm mb-0 data-table fs--1">
												<thead class="bg-200 text-900">
													<tr>
														<th class="no-sort pe-1 align-middle data-table-row-action"  id="VISOR_ValidarClavesCFDI_Etqiueta_ClaveProducto">Clave de Producto y Servicio</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_ETQ78">Descripción Clave de Producto del XML</th>
														<th class="sort pe-1 align-middle white-space-nowrap" id="VISOR_ETQ79">Descripción Clave de Producto del SAT</th>
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
				 <button class="btn btn-sm btn-primary btn-sm me-1 mb-2 mb-sm-0" type="button" onclick="guardarUsoCFDI('CORRECTA');" id="btnAprobarClaves">
               			<span class="far fa-thumbs-up me-1" > </span>  <span id="VISOR_ETQ80"> Información Correcta </span>
               	</button>
               	<button class="btn btn-sm btn-danger btn-sm mb-2 mb-sm-0" type="button" onclick="guardarUsoCFDI('INCORRECTA');" id="btnRechazarClaves" >
			   		<span class="far fa-thumbs-down me-1" ></span> <span id="VISOR_ETQ81"> Información Incorrecta </span>
			   </button>
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="btnCerrar_CargarFactura">Cerrar</button>
			</div>
		</div>
	</div>


 <script type="text/javascript">
 
 
 $(document).ready(function() {
	 
	 $('#valError_ValidacionCFDI').hide();
	 $('#valExito_ValidacionCFDI').hide();
	 calcularEtiquetasVisorValidarCFDI();
 });
 
 
 function calcularEtiquetasVisorValidarCFDI(){
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
					
					document.getElementById("VISOR_TITLE11").innerHTML = data.TITLE11;
					document.getElementById("VISOR_ETQ75").innerHTML = data.ETQ75;
					document.getElementById("VISOR_ETQ76").innerHTML = data.ETQ76;
					document.getElementById("VISOR_ETQ77").innerHTML = data.ETQ77;
					document.getElementById("VISOR_ETQ78").innerHTML = data.ETQ78;
					document.getElementById("VISOR_ETQ79").innerHTML = data.ETQ79;
					document.getElementById("VISOR_ETQ80").innerHTML = data.ETQ80;
					document.getElementById("VISOR_ETQ81").innerHTML = data.ETQ81;
					document.getElementById("VISOR_ValidarClavesCFDI_Etqiueta_RazonSocial").innerHTML = data.ETQ17;
					document.getElementById("VISOR_ValidarClavesCFDI_Etqiueta_RFC").innerHTML = data.ETQ18;
					document.getElementById("VISOR_ValidarClavesCFDI_Etqiueta_OrdenCompra").innerHTML = data.ETQ29;
					document.getElementById("VISOR_ValidarClavesCFDI_Etqiueta_UsoCFDI").innerHTML = data.COL32;
					document.getElementById("VISOR_ValidarClavesCFDI_Etqiueta_ClaveProducto").innerHTML = data.COL33;
					
					document.getElementById("btnCerrar_CargarFactura").innerHTML = BTN_CERRAR_MENU;
					
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}


 
  
 </script>
</html>