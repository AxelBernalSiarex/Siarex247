<%@page import="com.siarex247.utils.Utils"%>
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

  <script src="/siarex247/jsp/configuraciones/cfdi/etiquetasCartaPorte/etiquetasCartaPorte.js?v=<%=Utils.VERSION %>"></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONF_CFDI_CARTAP_TITLE1">Configurar Etiquetas Carta Porte</h5>
			</div>
		</div>
	</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					<table id="tablaDetalle"class="table mb-0 data-table fs--1">
						<thead class="bg-200 text-900">
							<tr>
								<!-- <th class="no-sort pe-1 align-middle data-table-row-action">Sel</th> -->
								<th class="no-sort pe-1 align-middle data-table-row-action" id="CONF_CFDI_CARTAP_ETQ12">Acciones</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="id" id="CONF_CFDI_CARTAP_ETQ1">Id. de Etiqueta</th>
								<!-- 
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="depto" id="CONF_CFDI_CARTAP_ETQ2">Path de XML</th>
								 -->
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="correo" id="CONF_CFDI_CARTAP_ETQ3">Descripción</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="correo" id="CONF_CFDI_CARTAP_ETQ4">Fecha de Inicio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="correo" id="CONF_CFDI_CARTAP_ETQ5">Fecha de Fin</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="correo" id="CONF_CFDI_CARTAP_ETQ6">Encabezado de Correo</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="correo" id="CONF_CFDI_CARTAP_ETQ7">Usuario</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="fecha" id="CONF_CFDI_CARTAP_ETQ8">Fecha</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="fecha" id="CONF_CFDI_CARTAP_ETQ9">Activo</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="fecha" id="CONF_CFDI_CARTAP_ETQ10">Validar Etiqueta</th>
								<th class="sort pe-1 align-middle white-space-nowrap" data-sort="fecha" id="CONF_CFDI_CARTAP_ETQ11">Versión XML</th>
								
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
<div class="modal fade bd-example-modal-lg" id="modalValidaciones" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>
<script type="text/javascript">
	$(document).ready(function() {
		 $("#myModalDetalle").load('/siarex247/jsp/configuraciones/cfdi/etiquetasCartaPorte/modalEtiquetasCP.jsp');
		 $("#modalValidaciones").load('/siarex247/jsp/configuraciones/cfdi/etiquetasCartaPorte/modalValidacionesCP.jsp');
		 
		 calcularEtiquetasConfCartaPorte();
	});
	
	

	 function calcularEtiquetasConfCartaPorte(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONF_CFDI_CARTAP'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONF_CFDI_CARTAP_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("CONF_CFDI_CARTAP_ETQ1").innerHTML = data.ETQ1;
						//document.getElementById("CONF_CFDI_CARTAP_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CONF_CFDI_CARTAP_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CONF_CFDI_CARTAP_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CONF_CFDI_CARTAP_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CONF_CFDI_CARTAP_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("CONF_CFDI_CARTAP_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("CONF_CFDI_CARTAP_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("CONF_CFDI_CARTAP_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("CONF_CFDI_CARTAP_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("CONF_CFDI_CARTAP_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("CONF_CFDI_CARTAP_ETQ12").innerHTML = data.ETQ12;
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasConfCartaPorte()_1_'+thrownError);
				}
			});	
		}
</script>




</html>