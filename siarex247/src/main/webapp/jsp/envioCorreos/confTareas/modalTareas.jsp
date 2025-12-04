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


	<div class="modal-dialog modal-dialog-centered modal-xl" role="document">
		<div class="modal-content position-relative">
			
			<div class="modal-body p-0">
				<div class="rounded-top-3 py-3 ps-4 pe-6 bg-light">
	   				<h5 class="mb-1" id="TAREAS_ETQ9">Modulo de Configuración para Envió de Alarmas</h5>
				</div>
				<div class="p-4 pb-0">
	
					<div class="tab-content">
						<div class="card overflow-hidden">
							<div class="card-header p-0 scrollbar-overlay border-bottom">
								<ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabAlarmas" role="tablist">
									<li class="nav-item text-nowrap" role="presentation">
										<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1 active" id="tabOrdenesCompra" data-bs-toggle="tab" href="#correosOrdenes" role="tab" aria-controls="correosOrdenes" aria-selected="true">
											<span class="fas fa-file-invoice text-600"></span>
											<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="TAREAS_ETQ10">Correo Ordenes de Compra</h6>
										</a>
									</li>
									<li class="nav-item text-nowrap" role="presentation">
										<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabGenerales" data-bs-toggle="tab" href="#correosGenerales" role="tab" aria-controls="correosGenerales" aria-selected="false">
											<span class="far fa-calendar-alt icon text-600"></span>
											<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="TAREAS_ETQ11">Correos Generales</h6>
			                      		</a>
			                      	</li>
			                      	
									<li class="nav-item text-nowrap" role="presentation">
										<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabPredefinidas" data-bs-toggle="tab" href="#correosPredefinidas" role="tab" aria-controls="correosPredefinidas" aria-selected="false">
											<span class="far fa-hdd icon text-600"></span>
											<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="TAREAS_ETQ12">Envio de Ordenes Predefinidas</h6>
			                      		</a>
			                      	</li>
								</ul>
							</div>
							
							<div class="tab-content">
			                	<div class="card-body bg-light tab-pane active" id="correosOrdenes" role="tabpanel" aria-labelledby="tabOrdenesCompra">
			                		<h6 class="mb-0 text-700">Correo Ordenes de Compra</h6>
			                	</div>
			                	
			                	 <div class="card-body bg-light tab-pane" id="correosGenerales" role="tabpanel" aria-labelledby="tabGenerales">
			                		<h6 class="mb-0 text-700">Cierre de Año</h6>
			                	</div>
			                	
			                	<div class="card-body bg-light tab-pane" id="correosPredefinidas" role="tabpanel" aria-labelledby="tabPredefinidas">
			                		<h6 class="mb-0 text-700">Correos Respaldo</h6>
			                	</div>
			
			                </div>
						</div>
					</div>

				</div>
			</div>
			<div class="modal-footer">	
			<!-- 
				<button type="submit" id="submit" class="btn btn-primary">Guardar</button>
			 -->	
				<button class="btn btn-secondary" type="button" data-bs-dismiss="modal" id="Tareas_Boton_CerrarPrincipal">Cerrar</button>
			</div>
		</div>
	</div>

 <script type="text/javascript">
 
 $(document).ready(function() {
		

		$(".nav-tabs a").click(function(){
		    $(this).tab('show');
		});
				
		$('.nav-tabs a').on('shown.bs.tab', function(event){
			var target = event.target || event.srcElement;
			var id = target.id
			if (id == "tabOrdenesCompra") {
				$("#correosOrdenes").load('/siarex247/jsp/envioCorreos/confTareas/modalEnvioCorreos.jsp');
			} else if (id == "tabGenerales"){
				$("#correosGenerales").load('/siarex247/jsp/envioCorreos/confTareas/modalGenerales.jsp');
			} else if (id == "tabPredefinidas"){
				$("#correosPredefinidas").load('/siarex247/jsp/envioCorreos/confTareas/modalPredefinidas.jsp');
			} 
		});
				
		calcularEtiquetasTareasModal();	
	});
 
 

 function calcularEtiquetasTareasModal(){
		$.ajax({
			url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
			type : 'POST', 
			data : {
				nombrePantalla : 'TAREAS'
			},
			dataType : 'json',
			success  : function(data) {
				if($.isEmptyObject(data)) {
				   
				} else {
					
					document.getElementById("TAREAS_ETQ9").innerHTML = data.ETQ9;
					document.getElementById("TAREAS_ETQ10").innerHTML = data.ETQ10;
					document.getElementById("TAREAS_ETQ11").innerHTML = data.ETQ11;
					document.getElementById("TAREAS_ETQ12").innerHTML = data.ETQ12;
					
					document.getElementById("Tareas_Boton_CerrarPrincipal").innerHTML = BTN_CERRAR_MENU;
					
					
				}
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert('calcularEtiquetasTareasModal()_1_'+thrownError);
			}
		});	
	}
	
 </script>
</html>

	
