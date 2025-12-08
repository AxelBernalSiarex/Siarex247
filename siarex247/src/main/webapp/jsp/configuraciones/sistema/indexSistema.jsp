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
    
	
    
</head>

<div class="card mb-3" style="box-shadow: none; margin-bottom: 0rem !important;">

	<div class="card-header">
		<div class="row flex-between-end">
			<div class="col-auto align-self-center">
				<h5 class="mb-0" data-anchor="data-anchor" id="CONF_SISTEMA_TITLE1">Configuraciones Siarex</h5>
			</div>
			<div class="col-auto d-flex">
			</div>
		</div>
	</div>
	
	<div class="card-body bg-light pt-0" style="padding-top: 10px !important;">
		
		<div class="tab-content">
					
			<div class="card overflow-hidden">
			
				<div class="card-header p-0 scrollbar-overlay border-bottom">
					<ul class="nav nav-tabs border-0 flex-column flex-sm-row" id="tabAlarmas" role="tablist">
						<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1 active" id="tabConfigOrdenes" data-bs-toggle="tab" href="#configOrdenes" role="tab" aria-controls="configOrdenes" aria-selected="true">
								<span class="fas fa-file-invoice text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_SISTEMA_ETQ1">Configuracion de Ordenes</h6>
							</a>
						</li>
						<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabCierreAnnio" data-bs-toggle="tab" href="#cierreAnnio" role="tab" aria-controls="cierreAnnio" aria-selected="false">
								<span class="far fa-calendar-alt icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_SISTEMA_ETQ2">Cierre de Año</h6>
                      		</a>
                      	</li>
                      	
						<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabCorreosRespaldo" data-bs-toggle="tab" href="#correosRespaldo" role="tab" aria-controls="correosRespaldo" aria-selected="false">
								<span class="far fa-hdd icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_SISTEMA_ETQ3">Correos Respaldo</h6>
                      		</a>
                      	</li>
                      	
                      	<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabConfigAdicionales" data-bs-toggle="tab" href="#configAdicionales" role="tab" aria-controls="configAdicionales" aria-selected="false">
								<span class="fas fa-cog icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_SISTEMA_ETQ4">Configuraciones Adicionales</h6>
                      		</a>
                      	</li>
                      	
                      	<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabConfigAdmin" data-bs-toggle="tab" href="#configAdmin" role="tab" aria-controls="configAdmin" aria-selected="false">
								<span class="fas fa-cogs icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_SISTEMA_ETQ5">Configuraciones del Administrador</h6>
                      		</a>
                      	</li>
                      	<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabCertificado" data-bs-toggle="tab" href="#certificadoSat" role="tab" aria-controls="certificadoSat" aria-selected="false">
								<span class="fas fa-key icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_SISTEMA_ETQ6">Certificados SAT</h6>
                      		</a>
                      	</li>
                      	<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabConciliacion" data-bs-toggle="tab" href="#Alertaconciliacion" role="tab" aria-controls="Alertaconciliacion" aria-selected="false">
								<span class="fas fa-traffic-light icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="CONF_SISTEMA_ETQ200">Alerta Conciliacion Boveda</h6>
                      		</a>
                      	</li>
                      	
                      	<li class="nav-item text-nowrap" role="presentation">
							<a class="nav-link mb-0 d-flex align-items-center gap-1 py-3 px-x1" id="tabAlertaComplemento" data-bs-toggle="tab" href="#AlertaComplemento" role="tab" aria-controls="AlertaComplemento" aria-selected="false">
								<span class="fas fa-traffic-light icon text-600"></span>
								<h6 class="mb-0 text-600 fontTituloTab" style="padding-left: 10px;" id="">Alerta Complemento de Pago</h6>
                      		</a>
                      	</li>
                      	
                      	
					</ul>
				</div>
                
                <div class="tab-content">
                	
                	<div class="card-body bg-light tab-pane active" id="configOrdenes" role="tabpanel" aria-labelledby="tabConfigOrdenes">
                		<h6 class="mb-0 text-700">Configuracion de Ordenes</h6>
                	</div>
                	
                	 <div class="card-body bg-light tab-pane" id="cierreAnnio" role="tabpanel" aria-labelledby="tabCierreAnnio">
                		<h6 class="mb-0 text-700">Cierre de Año</h6>
                	</div>
                	
                	<div class="card-body bg-light tab-pane" id="correosRespaldo" role="tabpanel" aria-labelledby="tabCorreosRespaldo">
                		<h6 class="mb-0 text-700">Correos Respaldo</h6>
                	</div>

                	<div class="card-body bg-light tab-pane" id="configAdicionales" role="tabpanel" aria-labelledby="tabConfigAdicionales">
                		<h6 class="mb-0 text-700">Configuraciones Adicionales</h6>
                	</div>
                	
                	<div class="card-body bg-light tab-pane" id="configAdmin" role="tabpanel" aria-labelledby="tabConfigAdmin">
                		<h6 class="mb-0 text-700">Configuraciones del Administrador</h6>
                	</div>
                	<div class="card-body bg-light tab-pane" id="certificadoSat" role="tabpanel" aria-labelledby="tabCertificado">
                		<h6 class="mb-0 text-700">Certificado SAT</h6>
                	</div>
                	<div class="card-body bg-light tab-pane" id="Alertaconciliacion" role="tabpanel" aria-labelledby="tabConciliacion">
                		<h6 class="mb-0 text-700">Alerta Conciliacion Boveda</h6>
                	</div>
                	<div class="card-body bg-light tab-pane" id="AlertaComplemento" role="tabpanel" aria-labelledby="tabAlertaComplemento">
                		<h6 class="mb-0 text-700">Alerta Complemento de Pago</h6>
                	</div>
                	
                	
                </div>
			
			</div>

		</div> <!-- tab-content -->
						
	</div> <!-- card-body -->
	
</div><!-- card mb-3 -->

<script type="text/javascript">

//alarmaUsuario.html

	$(document).ready(function() {
		$("#configOrdenes").load('/siarex247/jsp/configuraciones/sistema/confOrdenes/confOrdenes.jsp');

		$(".nav-tabs a").click(function(){
		    $(this).tab('show');
		});

		
		$('.nav-tabs a').on('shown.bs.tab', function(event){
			var target = event.target || event.srcElement;
			var id = target.id
			if (id == "tabConfigOrdenes") {
				$("#configOrdenes").load('/siarex247/jsp/configuraciones/sistema/confOrdenes/confOrdenes.jsp');
			} else if (id == "tabCierreAnnio"){
				$("#cierreAnnio").load('/siarex247/jsp/configuraciones/sistema/cierreAnnio/cierreAnnio.jsp');
			} else if (id == "tabCorreosRespaldo"){
				$("#correosRespaldo").load('/siarex247/jsp/configuraciones/sistema/correosRespaldo/correosRespaldo.jsp');
			} else if (id == "tabConfigAdicionales"){
				$("#configAdicionales").load('/siarex247/jsp/configuraciones/sistema/configAdicionales/configAdicionales.jsp');
			} else if (id == "tabConfigAdmin"){
				$("#configAdmin").load('/siarex247/jsp/configuraciones/sistema/configAdmin/configAdmin.jsp');
			}else if (id == "tabCertificado"){
				$("#certificadoSat").load('/siarex247/jsp/configuraciones/sistema/certificadoSAT/certificadoSAT.jsp');
			}else if (id == "tabConciliacion"){
				$("#Alertaconciliacion").load('/siarex247/jsp/configuraciones/sistema/alertaConciliacion/alertaConciliacion.jsp');
			}else if (id == "tabAlertaComplemento"){
				$("#AlertaComplemento").load('/siarex247/jsp/configuraciones/sistema/alertaComplemento/alertaComplemento.jsp');
			}
		});
		
		
		calcularEtiquetasConfSistema();
				
	});
	
	

	 function calcularEtiquetasConfSistema(){
			$.ajax({
				url  : '/siarex247/lenguaje/obtenerEtiquetas.action',
				type : 'POST', 
				data : {
					nombrePantalla : 'CONF_SISTEMA'
				},
				dataType : 'json',
				success  : function(data) {
					if($.isEmptyObject(data)) {
					   
					} else {
						
						document.getElementById("CONF_SISTEMA_ETQ1").innerHTML = data.ETQ1;
						document.getElementById("CONF_SISTEMA_ETQ2").innerHTML = data.ETQ2;
						document.getElementById("CONF_SISTEMA_ETQ3").innerHTML = data.ETQ3;
						document.getElementById("CONF_SISTEMA_ETQ4").innerHTML = data.ETQ4;
						document.getElementById("CONF_SISTEMA_ETQ5").innerHTML = data.ETQ5;
						document.getElementById("CONF_SISTEMA_ETQ6").innerHTML = data.ETQ6;
						
						
					}
					
				},
				error : function(xhr, ajaxOptions, thrownError) {
					alert('calcularEtiquetasConfSistema()_1_'+thrownError);
				}
			});	
		}
	 
</script>

</html>