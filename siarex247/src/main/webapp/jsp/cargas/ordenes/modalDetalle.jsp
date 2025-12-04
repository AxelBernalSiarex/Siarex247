<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%	response.setHeader("Cache-Control", "no-Cache");
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
    <script src="/siarex247/jsp/cargas/ordenes/modalDetalle.js"></script>
    
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

<input type="hidden" name="claveRegistro" id="claveRegistro" value="0">
<input type="hidden" name="tipoReg" id="tipoReg" value="N">


	<div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		<div class="modal-content position-relative">
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="CARGA_ORDENES_ETQ19">Detallado Histórico de Cargas</h5>
				</div>
				<div class="p-4 pb-0">

					<div class="tab-content">
					
						<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
								
							<div class="tab-pane preview-tab-pane active" role="tabpanel" aria-labelledby="tab-dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282" id="dom-a6fa4703-a0a5-453f-a182-a8e9ae0a9282">
								<table id="detalleBitacora"class="table mb-0 data-table fs--1">
									<thead class="bg-200 text-900">
										<tr>
											<th class="sort pe-1 align-middle white-space-nowrap" data-sort="id" id="CARGA_ORDENES_ETQ20">Registro</th>
											<th class="sort pe-1 align-middle white-space-nowrap" data-sort="depto" id="CARGA_ORDENES_ETQ21">Folio</th>
											<th class="sort pe-1 align-middle white-space-nowrap" data-sort="correo" id="CARGA_ORDENES_ETQ22">Descripción</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
			
					</div> <!-- tab-content -->

				</div>
			</div>
			<div class="modal-footer">	
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="CARGA_ORDENES_Boton_CerrarDetalle">Cerrar</button>
			</div>
		</div>
	</div>


 <script type="text/javascript">
 
   $(document).ready(function() {

	
	   	   
	});
   
   

	 function calcularEtiquetasCargasDetalleModal(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CARGA_ORDENES'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CARGA_ORDENES_ETQ19").innerHTML = data.ETQ19;
						document.getElementById("CARGA_ORDENES_ETQ20").innerHTML = data.ETQ20;
						document.getElementById("CARGA_ORDENES_ETQ21").innerHTML = data.ETQ21;
						document.getElementById("CARGA_ORDENES_ETQ22").innerHTML = data.ETQ22;
						
						document.getElementById("CARGA_ORDENES_Boton_CerrarDetalle").innerHTML = BTN_CERRAR_MENU;
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasCargasDetalleModal()_1_'+thrownError);
				}
			});	
		}
	
 </script>
</html>