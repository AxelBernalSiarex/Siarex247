<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
%>

<%
  long t = System.currentTimeMillis();
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">


	  <script src='/siarex247/jsp/estatusXML/conciliacion/conciliacion.js?t=<%=t%>'></script>
	  
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
				<h5 class="mb-0" data-anchor="data-anchor" id="CONCILIACION_TITLE1">Conciliación XML</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
				<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
					<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="CONCILIACION_ETQ1">Opciones</span>
				</button>
				<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
					<a class="dropdown-item" href="javascript:exportarConsiliados();" id="CONCILIACION_ETQ2">Exportar Complemento</a>
				</div>
			  </div>
			</div>


		</div>
	</div>
	
	<div class="card-header">
		<div class="mb-2 row">
			<label class="col-sm-1 col-form-label" for="anio" id="CONCILIACION_ETQ3">Año</label>
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
				<button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="buscarConsiliacion();" id="btnRefrescar_Nomina" ><span class="fab fa-firefox-browser me-1"></span>  <span id="CONCILIACION_ETQ4"> Refrescar </span> </button>
			</div>
			
		</div>
		
		<div class="mb-2 row">
			   <label class="col-sm-3 col-form-label" for="cmbComple" id="CONCILIACION_ETQ5">Mostrar Todo lo Pagado Durante el Mes Seleccionado </label>
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
									  <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ6">Business Unit</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ7">RFC</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ8">Razón Social</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ9">Orden de Compra</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ10">Tipo Moneda</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ11">Serie / Folio</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ12">Fecha de Pago</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ13">UUID Factura</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ14">Sub Total</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ15">IVA</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ16">IVA RET</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ17">Imp. Locales</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ18">Total</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ19">Fecha de Pago Complemento</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ20">Fecha de Timbrado</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ21">UUID Complemento</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ22">Total Factura</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ23">Total Complemento</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ24">Estatus Factura</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ25">Estatus Complemeto</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ26">XML COMP.</th>
									  <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ27">PDF COMP.</th>
                                      <th class="sort pe-1 align-middle white-space-nowrap" id="CONCILIACION_ETQ28">Estatus Conciliación</th>
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



    <form id="frmMostrarDocumento" name="frmMostrarDocumento" class="easyui-form"  method="post" 
    	action="/siarex247/jsp/visor/mostrarDocumento.jsp" target="_blank">
      	<input type="hidden" name="tipoDocumento" id="tipoDocumento_MostrarDocumento" value="0">
      	<input type="hidden" name="folioOrden" id="folioOrden_MostrarDocumento" value="0">
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
		// $("#myModalexportar").load('configuracion/statusxml/conciliacion/modalconciliacion.html');
		calcularEtiquetasConciliacion();
	});
	

	 function calcularEtiquetasConciliacion(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONCILIACION'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONCILIACION_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("CONCILIACION_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("CONCILIACION_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CONCILIACION_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CONCILIACION_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CONCILIACION_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CONCILIACION_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("CONCILIACION_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("CONCILIACION_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("CONCILIACION_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("CONCILIACION_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("CONCILIACION_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("CONCILIACION_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("CONCILIACION_ETQ13").innerHTML = data.ETQ13;
						document.getElementById("CONCILIACION_ETQ14").innerHTML = data.ETQ14;
						document.getElementById("CONCILIACION_ETQ15").innerHTML = data.ETQ15;
						document.getElementById("CONCILIACION_ETQ16").innerHTML = data.ETQ16;
						document.getElementById("CONCILIACION_ETQ17").innerHTML = data.ETQ17;
						document.getElementById("CONCILIACION_ETQ18").innerHTML = data.ETQ18;
						document.getElementById("CONCILIACION_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("CONCILIACION_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("CONCILIACION_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("CONCILIACION_ETQ22").innerHTML = data.ETQ22;
						document.getElementById("CONCILIACION_ETQ23").innerHTML = data.ETQ23;
						document.getElementById("CONCILIACION_ETQ24").innerHTML = data.ETQ24;
						document.getElementById("CONCILIACION_ETQ25").innerHTML = data.ETQ25;
						document.getElementById("CONCILIACION_ETQ26").innerHTML = data.ETQ26;
						document.getElementById("CONCILIACION_ETQ27").innerHTML = data.ETQ27;
						document.getElementById("CONCILIACION_ETQ28").innerHTML = data.ETQ28;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasConciliacion()_1_'+thrownError);
				}
			});	
		}
	
</script>





</html>