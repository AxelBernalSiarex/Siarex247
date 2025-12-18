<%@page import="com.siarex247.utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


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

	<script src='/siarex247/jsp/estatusXML/validarXML/validarXML.js?v=<%=Utils.VERSION %>'></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="VALIDARXML_TITLE1">Detalle de XML</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
				<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
					<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="VALIDARXML_ETQ1">Opciones</span>
				</button>
				<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
					<a class="dropdown-item" href="javascript:abreModal('nuevo');" id="VALIDARXML_ETQ2">Cargar XML</a>
					
					<!-- 
					<a class="dropdown-item" href="javascript:abreModal('imprimir');">Imprimir</a>
					 -->
				</div>
			  </div>
			</div>


		</div>
	</div>
	
	<div class="card-header">
		<div class="mb-2 row">
			<label class="col-sm-1 col-form-label" for="cmbGrupos" id="VALIDARXML_ETQ3">Mostrar por </label>
			<div class="col-sm-2">
				<div class="form-group">
				<select class="form-select" id="cmbGrupos" name="cmbGrupos" onchange="recargarDetalle();">
					<option selected="selected" value="GPO_IDV" id="VALIDARXML_ETQ4">GRUPO INDIVIDUAL</option>
                    <option value="GPO_RFC" id="VALIDARXML_ETQ5">GRUPO RFC</option>
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
								<th class="sort pe-1 align-middle white-space-nowrap" id="VALIDARXML_ETQ6">RFC</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VALIDARXML_ETQ7">Razón Social</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VALIDARXML_ETQ8">Tipo de Moneda</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="">Serie</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="">Folio</th>
								<th class="sort pe-1 align-middle white-space-nowrap">Tipo Comprobante</th>
								
								<th class="sort pe-1 align-middle white-space-nowrap" id="VALIDARXML_ETQ9">Sub-Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VALIDARXML_ETQ10">Total</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VALIDARXML_ETQ11">Estado SAT</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VALIDARXML_ETQ12">Estatus SAT</th>
								<th class="sort pe-1 align-middle white-space-nowrap" id="VALIDARXML_ETQ13">Gran Total</th>
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

<script type="text/javascript">
	$(document).ready(function() {
		$("#myModalDetalle").load('/siarex247/jsp/estatusXML/validarXML/modalValidarXML.jsp');
		calcularEtiquetasValidarXML();
	});
	
	

	 function calcularEtiquetasValidarXML(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'VALIDARXML'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("VALIDARXML_TITLE1").innerHTML = data.TITLE1;
						document.getElementById("VALIDARXML_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("VALIDARXML_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("VALIDARXML_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("VALIDARXML_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("VALIDARXML_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("VALIDARXML_ETQ6").innerHTML = data.ETQ6;
						document.getElementById("VALIDARXML_ETQ7").innerHTML = data.ETQ7;
						document.getElementById("VALIDARXML_ETQ8").innerHTML = data.ETQ8;
						document.getElementById("VALIDARXML_ETQ9").innerHTML = data.ETQ9;
						document.getElementById("VALIDARXML_ETQ10").innerHTML = data.ETQ10;
						document.getElementById("VALIDARXML_ETQ11").innerHTML = data.ETQ11;
						document.getElementById("VALIDARXML_ETQ12").innerHTML = data.ETQ12;
						document.getElementById("VALIDARXML_ETQ13").innerHTML = data.ETQ13;
						
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasValidarXML()_1_'+thrownError);
				}
			});	
		}
	 
</script>





</html>