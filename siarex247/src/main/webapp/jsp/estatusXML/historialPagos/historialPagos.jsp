<%@page import="com.siarex247.utils.Utils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	response.setHeader("Cache-Control", "no-Cache");
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Expires", "0");
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

  <script src="/siarex247/jsp/estatusXML/historialPagos/historialPagos.js?v=<%=Utils.VERSION%>"></script>
</head>

<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" id="HISTORIAL_PAGOS_TITLE1">Historial de Pagos</h5>
			</div>
			<div class="col-auto d-flex">
				<div class="dropdown font-sans-serif">
					<button class="btn btn-sm btn-falcon-default dropdown-toggle dropdown-caret-none" type="button" id="historialPagosMenu" data-bs-toggle="dropdown" data-boundary="viewport" aria-haspopup="true" aria-expanded="false">
						<span class="fas fa-plus" data-fa-transform="shrink-3 down-2"></span><span class="d-none d-sm-inline-block ms-1" id="HISTORIAL_PAGOS_BTN_OPCIONES"> Opciones </span>
					</button>
					<div class="dropdown-menu dropdown-menu-end border py-2" aria-labelledby="historialPagosMenu">
						<a id="menu_CargarHistorial" class="dropdown-item" href="javascript:abreModalHistorial();">Cargar relacion de pagos</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="modalMensajeTXT" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalMensajeTXTLabel">Mensaje</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <div class="modal-body" id="modalMensajeTXTBody">
        <!-- AquÃ­ se va a escribir el MENSAJE -->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Aceptar</button>
      </div>
    </div>
  </div>
</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		<div class="tab-content">
			<div class="tab-pane preview-tab-pane active" role="tabpanel" id="tab-historial">
				<table id="tablaHistorialPagos" class="table mb-0 data-table fs--1">
					<thead class="bg-200 text-900">
						<tr>
							<th class="sort pe-1 align-middle white-space-nowrap" id="HISTORIAL_PAGOS_ETQ1">RFC</th>
							<th class="sort pe-1 align-middle white-space-nowrap" id="HISTORIAL_PAGOS_ETQ2">Fecha Pago</th>
							<th class="sort pe-1 align-middle white-space-nowrap" id="HISTORIAL_PAGOS_ETQ4">Moneda</th>
							<th class="sort pe-1 align-middle white-space-nowrap text-end" id="HISTORIAL_PAGOS_ETQ5">Total</th>
							<th class="sort pe-1 align-middle white-space-nowrap" id="HISTORIAL_PAGOS_ETQ3">UUID Factura</th>
							<th class="sort pe-1 align-middle white-space-nowrap" id="HISTORIAL_PAGOS_ETQ7">UUID Complemento</th>
							<th class="sort pe-1 align-middle white-space-nowrap text-center" id="HISTORIAL_PAGOS_ETQ6">Estatus</th>
							
						</tr>
						<tr class="forFilters">
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
						</tr>
							
					</thead>
					<tbody class="list">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="myModalHistorialPagos" tabindex="-1" role="dialog" aria-labelledby="modalLabelHistorial" aria-hidden="true"></div>
<div class="modal fade" id="modalErrorPro" data-bs-keyboard="false" data-bs-backdrop="static" aria-labelledby="staticBackdropLabel"  tabindex="-1" role="dialog" aria-hidden="true"></div>


<form action="/siarex247/jsp/estatusXML/boveda/recibidos/mostrarBoveda.jsp" name="frmBoveda" id="frmBoveda" target="_blank" method="post">
   <input type="hidden" name="f" value="" id="idRegistroP">
   <input type="hidden" name="t" value="XML" id="tipoArchivoP">
</form>

<script type="text/javascript">
	$(document).ready(function() {
		// 1. Cargar el modal en el contenedor vacÃ­o
		$("#myModalHistorialPagos").load('/siarex247/jsp/estatusXML/historialPagos/modalHistorialPagos.jsp');
		$("#modalErrorPro").load('/siarex247/jsp/estatusXML/historialPagos/modalError.jsp');
		
		// 2. Calcular etiquetas (La funciÃ³n se define en el JS, pero se llama aquÃ­)
		// calcularEtiquetasHistorialPagos();
	});
	
	/* =========================================================
	   ETIQUETAS DE LENGUAJE
	========================================================= */
	function calcularEtiquetasHistorialPagos(){
	    $.ajax({
	        url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
	        type : 'POST',
	        data : {
	            nombrePantalla : 'HISTORIAL_PAGOS'
	        },
	        dataType : 'json',
	        success  : function(data) {
	            if(!$.isEmptyObject(data)) {

	                document.getElementById("HISTORIAL_PAGOS_TITLE1").innerHTML = data.TITLE1;
                    document.getElementById("HISTORIAL_PAGOS_BTN_OPCIONES").innerHTML = data.BTN_OPCIONES || "Opciones";
	                // Encabezados de tabla
	                document.getElementById("HISTORIAL_PAGOS_ETQ1").innerHTML = data.ETQ1;
	                document.getElementById("HISTORIAL_PAGOS_ETQ2").innerHTML = data.ETQ2;
	                document.getElementById("HISTORIAL_PAGOS_ETQ3").innerHTML = data.ETQ3;
	                document.getElementById("HISTORIAL_PAGOS_ETQ4").innerHTML = data.ETQ4;
	                document.getElementById("HISTORIAL_PAGOS_ETQ5").innerHTML = data.ETQ5;
	                document.getElementById("HISTORIAL_PAGOS_ETQ6").innerHTML = data.ETQ6;
	                document.getElementById("HISTORIAL_PAGOS_ETQ7").innerHTML = data.ETQ7;
                    document.getElementById("HISTORIAL_PAGOS_MODAL_TITULO").innerHTML = data.MODAL_TITULO || "Cargar Archivo";
                    document.getElementById("btnRefresh_HistorialPagos").innerHTML = '<span class="fab fa-firefox-browser me-1" data-fa-transform="shrink-3 down-2"></span>' + '<span class="d-none d-sm-inline-block ms-1">'+ BTN_REFRESCAR +'</span>';
	            }
	        },
	        error : function(xhr, ajaxOptions, thrownError) {
	            alert('calcularEtiquetasHistorialPagos()_1_' + thrownError);
	        }
	    });
	}
	
	</script>

</html>