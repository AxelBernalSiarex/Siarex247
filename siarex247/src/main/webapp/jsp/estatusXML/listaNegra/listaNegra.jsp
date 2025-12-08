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
  
	<script src="/siarex247/jsp/estatusXML/listaNegra/listaNegra.js?v=<%=Utils.VERSION %>"></script>

</head>


<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="LISTA_NEGRA_TITLE1">Lista Negra SAT</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
				<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="kanbanMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
					<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="LISTA_NEGRA_ETQ1">Opciones</span>
				</button>
				<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="kanbanMenu">
					<a class="dropdown-item" href="javascript:generarCSV();" id="LISTA_NEGRA_ETQ2">Exportar CSV</a>
					<a class="dropdown-item" href="javascript:abrirModal();" id="LISTA_NEGRA_ETQ200">Estatus RFC</a>
				</div>
				 <div id="overSeccionLista" class="overlay" style="display: none;text-align: right;">
					<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
				</div>
				
			  </div>
			</div>


		</div>
	</div>
	
	<div class="card-header">
		<div class="mb-2 row">
              
                <label class="col-sm-1 col-form-label" for="anioListaNegra" id="LISTA_NEGRA_ETQ3">Año</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="anioListaNegra" name="anioListaNegra" class="form-control" type="number"  value="" maxlength="4"  />
					</div>
				</div>
             	<label class="col-sm-1 col-form-label" for="cmbSupuestos" id="LISTA_NEGRA_ETQ4">Supuesto </label>
				<div class="col-sm-3">
					<div class="form-group">
					   <select id="cmbSupuestos" class="form-select"> </select>
					</div>
				</div>
				<label class="col-sm-2 col-form-label" for="cmbNombreArticulo" id="LISTA_NEGRA_ETQ5">Nombre de Artículo </label>
				<div class="col-sm-3" style="z-index: 0;">
					<div class="form-group">
					   <select id="cmbNombreArticulo" class="form-select"> </select>
					</div>
				</div>
				
        </div>
        <div class="mb-2 row">
        	<label class="col-sm-1 col-form-label" for="rfcListaNegra" id="LISTA_NEGRA_ETQ6">RFC</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="rfcListaNegra" name="rfcListaNegra" class="form-control" type="text"  maxlength="15"  />
					</div>
				</div>
				
			<label class="col-sm-1 col-form-label" for="razonSocialListaNegra" id="LISTA_NEGRA_ETQ7">Razón Social</label>
				<div class="col-sm-2">
					<div class="form-group">
					   <input id="razonSocialListaNegra" name="razonSocialListaNegra" class="form-control" type="text"  maxlength="200"  />
					</div>
				</div>
				
				<div class="col-sm-2">
					<div class="form-group">
					   <button class="btn btn-falcon-success btn-sm mb-2 mb-sm-0" type="button" onclick="refrescar();" id="btnRefrescar_Nomina" ><span class="fab fa-firefox-browser me-1"></span> <span id="LISTA_NEGRA_ETQ8"> Refrescar </span> </button>
					</div>
				</div>
					
        </div>
	</div>
	
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					
				<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
					 <div id="overSeccion" class="overlay" style="display: none;text-align: right;">
						<img src="/siarex247/img/loading.gif" style="width: 30px; height: 30px;"> Procesando...
					</div>
					
					<div id="detColumnas" style="width: 100%; overflow: auto;">
					</div>
					
					
				</div>
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

<form action="/siarex247/excel/descargarListaNegraPDF.action" name="frmDescargarPDF" id="frmDescargarPDF" 
	accept-charset="UTF-8" target="frmOcultoSiarex" method="post">
   <input type="hidden" name="rfcListaNegra" value="" id="rfcListaNegra_Buscar">
   <input type="hidden" name="razonSocial" value="" id="razonListaNegra_Buscar">
   <input type="hidden" name="existeListaNegra" value="" id="existeListaNegra_Buscar">
   
</form>

	<iframe name="frmOcultoSiarex" id="frmOcultoSiarex" style="width: 0px; height: 0px; visibility: hidden;" marginheight="0" marginwidth="0" frameborder="0">
	</iframe>
<div class="modal fade" id="myModalBusqueda" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>


<script type="text/javascript">
$(document).ready(function() {
	$("#detColumnas").load('/siarex247/jsp/estatusXML/listaNegra/listaColumnas.jsp');
	$("#myModalBusqueda").load('/siarex247/jsp/estatusXML/listaNegra/modalBusqueda.jsp');
	
	$('#cmbSupuestos').select2({
		theme: 'bootstrap-5'
	});
	$('#cmbSupuestos').val(''); // Selecciona primer valor del combo
	$('#cmbSupuestos').trigger('change'); //Refresca el combo
	
	$('#cmbNombreArticulo').select2({
		theme: 'bootstrap-5'
	});
	$('#cmbNombreArticulo').val(''); // Selecciona primer valor del combo
	$('#cmbNombreArticulo').trigger('change'); //Refresca el combo
	
	cargaCmbSupuestos();
	cargaCmbNombreArticulo();
	calcularEtiquetasListaNegraXML();
	 
});



function calcularEtiquetasListaNegraXML(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'LISTA_NEGRA'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					document.getElementById("LISTA_NEGRA_TITLE1").innerHTML = data.TITLE1;
					document.getElementById("LISTA_NEGRA_ETQ1").innerHTML = data.ETQ1;
					document.getElementById("LISTA_NEGRA_ETQ2").innerHTML = data.ETQ2;
					document.getElementById("LISTA_NEGRA_ETQ3").innerHTML = data.ETQ3;
					document.getElementById("LISTA_NEGRA_ETQ4").innerHTML = data.ETQ4;
					document.getElementById("LISTA_NEGRA_ETQ5").innerHTML = data.ETQ5;
					document.getElementById("LISTA_NEGRA_ETQ6").innerHTML = data.ETQ6;
					document.getElementById("LISTA_NEGRA_ETQ7").innerHTML = data.ETQ7;
					document.getElementById("LISTA_NEGRA_ETQ8").innerHTML = data.ETQ8;
					// document.getElementById("LISTA_NEGRA_ETQ9").innerHTML = data.ETQ9;
					//document.getElementById("LISTA_NEGRA_ETQ10").innerHTML = data.ETQ10;
					//document.getElementById("LISTA_NEGRA_ETQ11").innerHTML = data.ETQ11;
					//document.getElementById("LISTA_NEGRA_ETQ12").innerHTML = data.ETQ12;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasVisor()_1_'+thrownError);
			}
		});	
	}
	
</script>



</html>