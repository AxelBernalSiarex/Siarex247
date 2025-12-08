<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">

  <script src='/siarex247/jsp/estatusXML/conciliacionCP/conciliacionCP.js'></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONCILIACION_CARTAP_TITLE1">Conciliacion Carta Porte</h5>
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
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ1">RFC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ2">Razón Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ3">Orden de Compra</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ4">Tipo Moneda</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ5">Serie/Folio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ6">Fecha de Pago</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ7">UUID Factura</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ8">Sub-Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ9">IVA</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ10">IVA RET</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ11">Imp.Locales</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ12">Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ13">Fecha de Pago Complemento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ14">Fecha de Timbrado</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ15">UUID Carta Porte</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ16">Total Factura</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ17">Estatus Factura</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ18">XML Carta Porte</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ19">PDF Carta Porte</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_CARTAP_ETQ20">Estatus Conciliación</th>
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

<form action="/siarex247/jsp/estatusXML/conciliacionCP/mostrarConsolidacionCP.jsp" name="frmAbrirArchivoCP" id="frmAbrirArchivoCP" target="_blank" method="post">
    <input type="hidden" name="f" value="" id="idRegistro_CP">
    <input type="hidden" name="t" value="" id="tipoArchivo_CP">
    <input type="hidden" name="p" value="" id="claveProveedor_CP">
 </form>



<script type="text/javascript">
	$(document).ready(function() {
		calcularEtiquetasConciliacionCartaPorte();
	});
	

	 function calcularEtiquetasConciliacionCartaPorte(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONCILIACION_CARTAP'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONCILIACION_CARTAP_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("CONCILIACION_CARTAP_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("CONCILIACION_CARTAP_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CONCILIACION_CARTAP_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CONCILIACION_CARTAP_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CONCILIACION_CARTAP_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CONCILIACION_CARTAP_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("CONCILIACION_CARTAP_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("CONCILIACION_CARTAP_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("CONCILIACION_CARTAP_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("CONCILIACION_CARTAP_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("CONCILIACION_CARTAP_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("CONCILIACION_CARTAP_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("CONCILIACION_CARTAP_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("CONCILIACION_CARTAP_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("CONCILIACION_CARTAP_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("CONCILIACION_CARTAP_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("CONCILIACION_CARTAP_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("CONCILIACION_CARTAP_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("CONCILIACION_CARTAP_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("CONCILIACION_CARTAP_ETQ20").innerHTML = data.ETQ20;
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 
</script>
</html>