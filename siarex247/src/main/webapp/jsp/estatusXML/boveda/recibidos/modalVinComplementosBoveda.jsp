<%@page import="com.siarex247.utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">   
    
    <script src='/siarex247/jsp/estatusXML/boveda/recibidos/vincularBovedaComplemento.js?v=<%=Utils.VERSION%>'></script>
    
    
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



<form id="form-vinculaboveda" class="was-validated" novalidate>
	<input type="hidden" name="iden" id="IDEN_Boveda" value="">
	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1"  id="BOVEDA_ETQ38">Vincular XML de Boveda a Complementos de Pago</h5>
				</div>
				<div class="p-4 pb-0">

					<div class="mb-2 row">
						<label class="col-sm-7 col-form-label" for="txtTotal" id="BOVEDA_ETQ39">Total de XML Complemento a Procesar</label>
						<div class="col-sm-5">
	   						<input id="totalXML_Boveda" name="totalXML" class="form-control" type="number" readonly="readonly" />
	 					</div>
					</div>

					<div class="mb-2 row">
						<label class="col-sm-7 col-form-label" for="" id="BOVEDA_ETQ40">Presione el boton "Iniciar", para procesar los XML</label>
						<div class="col-sm-5">
						<!-- 
							<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="btnSometerVincular" onclick="procesaVincular();">Iniciar</button>
							-->
							 <div class="col-sm-2">
								<div class="position-relative" id="emoji-button" onclick="procesaVincularBoveda();">
									<div id="btnVincularComplementoBoveda"  class="btn btn-info" data-picmo='{"position":"bottom-start"}'><span class="fas fa-hourglass-start"></span></div>
						  		</div>
							</div>
	 					</div>
					</div>

					<div class="mb-2 row">
						<label class="col-sm-7 col-form-label" for="totalVinculados" id="BOVEDA_ETQ41">Total de XML Complemento Vinculados</label>
						<div class="col-sm-5">
	   						<input id="totalVinculados_Boveda" name="totalVinculados" class="form-control" type="text" readonly="readonly" />
	 					</div>
					</div>

					<div class="mb-2 row">
						<label class="col-sm-7 col-form-label" for="totalError" id="BOVEDA_ETQ42">Total de XML Complemento con Error</label>
						<div class="col-sm-5">
	   						<input id="totalError_Boveda" name="totalError" class="form-control" type="text" readonly="readonly"/>
	 					</div>
					</div>

        			
					<div class="mb-2 row">
						<div class="tab-content">
					
							<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
									
								<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
									<table id="tablaDetalleBovedaComplemento"class="table mb-0 data-table fs--1">
										<thead class="bg-200 text-900">
											<tr>
												<th class="sort pe-1 align-middle white-space-nowrap" style="width: 15%;" id="BOVEDA_ETQ43">UUID</th>
												<th class="sort pe-1 align-middle white-space-nowrap" style="width: 10%;" id="BOVEDA_ETQ44">Estatus</th>
												<th class="sort pe-1 align-middle white-space-nowrap" style="width: 75%;" id="BOVEDA_ETQ45">Mensaje</th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
				
						</div> <!-- tab-content -->
					</div>

				</div>
			</div>
			<div class="modal-footer">	
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="BOVEDA_Boveda_Boton_CerrarVincularBoveda">Cerrar</button>
			</div>
		</div>
	</div>
</form>

 <script type="text/javascript">
 
   $(document).ready(function() {
	   
	   calcularEtiquetasBovedaVincularBovedaModal();
	   	   
	});
   

	 function calcularEtiquetasBovedaVincularBovedaModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'BOVEDA'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("BOVEDA_ETQ38").innerHTML = data.ETQ38;
						document.getElementById("BOVEDA_ETQ39").innerHTML = data.ETQ39;
						document.getElementById("BOVEDA_ETQ40").innerHTML = data.ETQ40;
						document.getElementById("BOVEDA_ETQ41").innerHTML = data.ETQ41;
						document.getElementById("BOVEDA_ETQ42").innerHTML = data.ETQ42;
						document.getElementById("BOVEDA_ETQ43").innerHTML = data.ETQ43;
						document.getElementById("BOVEDA_ETQ44").innerHTML = data.ETQ44;
						document.getElementById("BOVEDA_ETQ45").innerHTML = data.ETQ45;
						
						document.getElementById("BOVEDA_Boveda_Boton_CerrarVincularBoveda").innerHTML = BTN_CERRAR_MENU;
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasBovedaVincularBovedaModal()_1_'+thrownError);
				}
			});	
		}
	
 </script>
</html>