<%@page import="com.siarex247.utils.Utils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Expires" content="0">

  <script src='/siarex247/jsp/estatusXML/conciliacionBoveda/conciliacionBoveda.js?v=<%=Utils.VERSION%>'></script>

<%
Date fechaActual = new Date();
SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
String fechaHoy = formatDate.format(fechaActual);

int anioActual = Integer.parseInt(fechaHoy.substring(0, 4));




%>


</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">
	<input type="hidden" name="annioActual" id="annioActual" value="<%=anioActual%>">
	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONCILIACION_BOVEDA_TITLE1">Conciliación XML de la Boveda</h5>
			</div>
		</div>
	</div>
	
	<div class="card-header">
		<div class="mb-2 row">
			<label class="col-sm-1 col-form-label" for="anio" id="CONCILIACION_BOVEDA_ETQ1">Año</label>
			<div class="col-sm-1">
				<div class="form-group">
				<select class="form-select" id="anio" name="anio" onchange="">
				
				</select>
				</div>
			</div>
			
			<div class="col-sm-2">
				<div class="form-group">
					<select class="form-select" id="cmbMes" name="cmbMes" onchange="">
					</select>
				</div>
			</div>
			
			<div class="col-sm-2">
				<button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="buscarConsiliacion();" id="btnRefrescar_Nomina" ><span class="fab fa-firefox-browser me-1"></span> <span id="CONCILIACION_BOVEDA_ETQ2"> Refrescar  </span> </button>
			</div>
			
		</div>
		
		<div class="mb-2 row">
			   <label class="col-sm-3 col-form-label" for="cmbComple" id="CONCILIACION_BOVEDA_ETQ3">Mostrar Todo lo Pagado Durante el Mes Seleccionado </label>
				<div class="col-sm-2">
					<div class="form-group">
					<select class="form-select" id="cmbComple" name="cmbComple" >
					  
					</select>
					</div>
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
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ4">RFC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ5">Razón Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ6">Tipo Moneda</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ7">Serie/Folio</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ8">UUID Factura</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ9">Sub-Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ10">IVA</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ11">IVA RET</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ12">Imp.Locales</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ13">Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ14">Fecha de Pago Complemento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ15">Fecha de Timbrado</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ16">UUID Complemento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ17">Total Factura</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ18">Total Complemento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ19">Estatus Complemento</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ20">XML COMP.</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ21">PDF COMP.</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_BOVEDA_ETQ22">Estatus Conciliación</th>
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
							</tr>
						</thead>
					</table>
				</div>
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

<form action="/siarex247/jsp/estatusXML/conciliacionBoveda/mostrarBoveda.jsp" name="frmAbrirBoveda" id="frmAbrirBoveda" target="_blank" method="post">
   <input type="hidden" name="f" value="" id="idRegistro">
   <input type="hidden" name="t" value="" id="tipoArchivo">
</form>



<script type="text/javascript">
	$(document).ready(function() {
		$('#anio').select2({
			theme: 'bootstrap-5'
		});
		$('#anio').val('');
		$('#anio').trigger('change');
		
		$('#cmbMes').select2({
			theme: 'bootstrap-5'
		});
		$('#cmbMes').val('');
		$('#cmbMes').trigger('change');
		
		$('#cmbComple').select2({
			theme: 'bootstrap-5'
		});
		$('#cmbComple').val('');
		$('#cmbComple').trigger('change');
		
		cargaAnnios();
		cargaCmbMes();
		cargaCmbComplemento();
		calcularEtiquetasConciliacionBoveda();
	});
	
	

	 function calcularEtiquetasConciliacionBoveda(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONCILIACION_BOVEDA'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONCILIACION_BOVEDA_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("CONCILIACION_BOVEDA_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("CONCILIACION_BOVEDA_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CONCILIACION_BOVEDA_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CONCILIACION_BOVEDA_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CONCILIACION_BOVEDA_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CONCILIACION_BOVEDA_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("CONCILIACION_BOVEDA_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("CONCILIACION_BOVEDA_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("CONCILIACION_BOVEDA_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("CONCILIACION_BOVEDA_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("CONCILIACION_BOVEDA_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("CONCILIACION_BOVEDA_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("CONCILIACION_BOVEDA_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("CONCILIACION_BOVEDA_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("CONCILIACION_BOVEDA_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("CONCILIACION_BOVEDA_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("CONCILIACION_BOVEDA_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("CONCILIACION_BOVEDA_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("CONCILIACION_BOVEDA_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("CONCILIACION_BOVEDA_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("CONCILIACION_BOVEDA_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("CONCILIACION_BOVEDA_ETQ22").innerHTML = data.ETQ22;
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasVisor()_1_'+thrownError);
				}
			});	
		}
	 
</script>





</html>