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

  <script src="/siarex247/jsp/configuraciones/cfdi/etiquetasComp/etiquetasComp.js"></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONF_CFDI_ETIQUETAS_TITLE1">Configurar Etiquetas XML Complementos</h5>
			</div>
		</div>
	</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
				
				
				
				
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalleComplemento"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<!-- <th class="no-sort pe-1 align-middle data-table-row-action">Sel</th> -->
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ3" >Etiqueta Configurada</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ18">Tipo de Validación</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ2" >Configuración Activada</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ4" >Fecha de Inicio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ5" >Fecha de Fin</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ6" >Asunto</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ9" >Versión XML</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ17" >Dato a Validar</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ15" >Mensaje de Error</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONF_CFDI_ETIQUETAS_ETQ10" >Acciones</th>
							</tr>
							<tr class="forFilters">
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->


<div class="modal fade bd-example-modal-lg" id="myModalDetalle" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<!-- 
	<div class="modal fade bd-example-modal-lg" id="modalValidaciones" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
 -->
<script type="text/javascript">
	$(document).ready(function() {
		$("#myModalDetalle").load('/siarex247/jsp/configuraciones/cfdi/etiquetasComp/modalEtiquetasComp.jsp');
		// $("#modalValidaciones").load('/siarex247/jsp/configuraciones/cfdi/etiquetasComp/modalValidacionesComp.jsp');
		
		calcularEtiquetasConfComplemento();
	});
	

	 function calcularEtiquetasConfComplemento(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONF_CFDI_COMPLEMENT'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						document.getElementById("CONF_CFDI_ETIQUETAS_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("CONF_CFDI_ETIQUETAS_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CONF_CFDI_ETIQUETAS_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CONF_CFDI_ETIQUETAS_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CONF_CFDI_ETIQUETAS_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CONF_CFDI_ETIQUETAS_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("CONF_CFDI_ETIQUETAS_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("CONF_CFDI_ETIQUETAS_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("CONF_CFDI_ETIQUETAS_ETQ18").innerHTML = data.ETQ18;
						// document.getElementById("CONF_CFDI_ETIQUETAS_ETQ15").innerHTML = data.ETQ15;
						// document.getElementById("CONF_CFDI_ETIQUETAS_ETQ17").innerHTML = data.ETQ17;
						
						
											
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasConfComplemento()_1_'+thrownError);
				}
			});	
		}
	 
	 
</script>




</html>